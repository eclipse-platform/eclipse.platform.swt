package org.eclipse.swt.custom;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.io.*;
import java.util.Vector;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.printing.*;

class StyledTextPrinter implements Runnable {
	class RTFState {
		int fontStyle;
		int foreground;
		int background;
	}
	Vector savedState = new Vector();
	
	Printer printer;
	GC gc;
	String rtf;
	StringBuffer wordBuffer;
	int index, end;
	String tabs = "";
	int tabWidth = 0;
	int lineHeight = 0;
	int leftMargin, rightMargin, topMargin, bottomMargin;
	int x, y;

	/* We can optimize for fonts because we know styledText only has one font.
	 * As soon as we know the font name and point size, we will create and store
	 * fonts for the following styles: normal, bold, italic, and bold italic.
	 */
	Font fontTable[][] = new Font[1][4];
	boolean creatingFontTable = false;
	String fontName;
	int fontSize;
	int currentFontStyle = SWT.NORMAL;
	int currentFont = -1;
	int defaultFont = -1;

	Vector colorTable = new Vector();
	boolean creatingColorTable = false;
	int red, green, blue;
	int currentForeground = -1;
	int currentBackground = -1;
	
	static void print(StyledText styledText) {
		Printer printer = new Printer();
		new StyledTextPrinter(styledText, printer).run();
		printer.dispose();
	}
	
	StyledTextPrinter(StyledText styledText, Printer printer) {
		this.printer = printer;

		/* Create a buffer for computing tab width. */
		int tabSize = styledText.getTabs();
		StringBuffer tabBuffer = new StringBuffer(tabSize);
		for (int i = 0; i < tabSize; i++) tabBuffer.append(' ');
		tabs = tabBuffer.toString();

		/* Get RTF from the StyledText.*/
		rtf = styledText.getRtf();
	}
	
	public void run() {
		if (printer.startJob("Printing")) {
			Rectangle clientArea = printer.getClientArea();
			Rectangle trim = printer.computeTrim(0, 0, 0, 0);
			Point dpi = printer.getDPI();
			leftMargin = dpi.x + trim.x; // one inch from left side of paper
			rightMargin = clientArea.width - dpi.x + trim.x + trim.width; // one inch from right side of paper
			topMargin = dpi.y + trim.y; // one inch from top edge of paper
			bottomMargin = clientArea.height - dpi.y + trim.y + trim.height; // one inch from bottom edge of paper
			
			/* Create a printer GC and print the RTF to it. */
			gc = new GC(printer);
			x = leftMargin;
			y = topMargin;
			printer.startPage();
			end = rtf.length();
			index = 0;
			wordBuffer = new StringBuffer();
			while (index < end) {
				char c = rtf.charAt(index);
				index++;
				switch (c) {
					case '\\':
						printWordBuffer();
						parseControlWord();
						break;
					case '{':
						printWordBuffer();
						saveState();
						break;
					case '}':
						printWordBuffer();
						restoreState();
						break;
					case 0x0a:
					case 0x0d:
						printWordBuffer();
						break;
					default:
						parseChar(c);
				}
			}
			if (y + lineHeight <= bottomMargin) {
				printer.endPage();
			}
			printer.endJob();

			/* Cleanup */
			gc.dispose();
			for (int i = 0; i < 4; i++) {
				fontTable[currentFont][i].dispose();
			}
			for (int i = 0; i < colorTable.size(); i++) {
				((Color)colorTable.elementAt(i)).dispose();
			}
		}
	}
	
	void parseControlWord() {
		if (index >= end) return;
		char c = rtf.charAt(index);
		index++;
		if (!Character.isLetter(c)) {
			handleControlSymbol(c);
			return;
		}
		StringBuffer controlWord = new StringBuffer();
		controlWord.append(c);
		while (index < end) {
			c = rtf.charAt(index);
			index++;
			if (!Character.isLetter(c)) break;
			controlWord.append(c);
		}
		boolean isNegative = false;
		if (c == '-') {
			isNegative = true;
			c = rtf.charAt(index);
			index++;
		}
		boolean hasParameter = false;
		StringBuffer paramBuffer = new StringBuffer();
		int parameter = 0;
		if (Character.isDigit(c)) {
			hasParameter = true;
			paramBuffer.append(c);
			while (index < end) {
				c = rtf.charAt(index);
				index++;
				if (!Character.isDigit(c)) break;
				paramBuffer.append(c);
			}
			try {
				parameter = Integer.valueOf(paramBuffer.toString()).intValue();
			} catch (NumberFormatException e) {}
			if (isNegative) parameter = -parameter;
		}
		if (c != ' ') index--;
		if (hasParameter) {
			handleControlWord(controlWord.toString(), parameter);
		} else {
			handleControlWord(controlWord.toString());
		}
	}
	
	void parseChar(char c) {
		if (c == 0) return;
		if (c == ';') {
			if (creatingFontTable) {
				fontName = wordBuffer.toString();
				wordBuffer = new StringBuffer();
				creatingFontTable = false;
				return;
			}
			if (creatingColorTable) {
				colorTable.addElement(new Color(printer, red, green, blue));
				red = green = blue = 0;
				return;
			}
		}
		if (c != '\t') {
			wordBuffer.append(c);
		}
		if (!Character.isLetterOrDigit(c) && !creatingFontTable) {
			printWordBuffer();
			if (c == '\t') {
				x += tabWidth;
			}
		}
	}
	
	void printWordBuffer() {
		if (wordBuffer.length() > 0) {
			String word = wordBuffer.toString();
			int wordWidth = gc.stringExtent(word).x;
			if (x + wordWidth > rightMargin) {
				/* word doesn't fit on current line, so wrap */
				newline();
			}
			gc.drawString(word, x, y, true);
			x += wordWidth;
			wordBuffer = new StringBuffer();
		}
	}
	
	void handleControlSymbol(char c) {
		switch (c) {
			case '\\':
			case '{':
			case '}':
				parseChar(c);
		}
	}
	
	void handleControlWord(String controlWord) {
		if (controlWord.equals("par")) newline();
		else if (controlWord.equals("b")) setFontStyle(currentFontStyle | SWT.BOLD);
		else if (controlWord.equals("i")) setFontStyle(currentFontStyle | SWT.ITALIC);
		else if (controlWord.equals("fnil")) setFont(defaultFont);
		else if (controlWord.equals("fonttbl")) createFontTable();
		else if (controlWord.equals("colortbl")) createColorTable();
	}
	
	void handleControlWord(String controlWord, int parameter) {
		if (controlWord.equals("highlight")) setBackground(parameter);
		else if (controlWord.equals("cf")) setForeground(parameter);
		else if (controlWord.equals("b")) setFontStyle(currentFontStyle & ~SWT.BOLD);
		else if (controlWord.equals("i")) setFontStyle(currentFontStyle & ~SWT.ITALIC);
		else if (controlWord.equals("f")) setFont(parameter);
		else if (controlWord.equals("fs")) setFontSize(parameter);
		else if (controlWord.equals("red")) red = parameter;
		else if (controlWord.equals("green")) green = parameter;
		else if (controlWord.equals("blue")) blue = parameter;
		else if (controlWord.equals("deff")) setDefaultFont(parameter);
	}
	
	void setDefaultFont(int number) {
		defaultFont = number;
	}
	
	void setFont(int number) {
		currentFont = number;
	}
	
	void createFontTable() {
		creatingFontTable = true;
		currentFont = 0;
	}
	
	void setFontSize(int size) {
		fontSize = size / 2;
		createFonts();
	}
	
	void createFonts() {
		if (fontName != null && fontSize != -1) {
			// currentFont must already be set
			fontTable[currentFont][0] = new Font(printer, fontName, fontSize, SWT.NORMAL);
			fontTable[currentFont][1] = new Font(printer, fontName, fontSize, SWT.BOLD);
			fontTable[currentFont][2] = new Font(printer, fontName, fontSize, SWT.ITALIC);
			fontTable[currentFont][3] = new Font(printer, fontName, fontSize, SWT.BOLD | SWT.ITALIC);
			setFontStyle(SWT.NORMAL);
		}
	}
	
	void setFontStyle(int style) {
		// currentFont must already be set
		Font font;
		if ((style & SWT.BOLD) != 0) {
			if ((style & SWT.ITALIC) != 0) {
				font = fontTable[currentFont][3];
			} else {
				font = fontTable[currentFont][1];
			}
		} else if ((style & SWT.ITALIC) != 0) {
			font = fontTable[currentFont][2];
		} else {
			font = fontTable[currentFont][0];
		}
		gc.setFont(font);
		tabWidth = gc.stringExtent(tabs).x;
		lineHeight = gc.getFontMetrics().getHeight();
		currentFontStyle = style;
	}
	
	void createColorTable() {
		creatingColorTable = true;
		red = green = blue = 0;
	}
	
	void setForeground(int color) {
		if (color != currentForeground) {
			// colors must already be in table
			gc.setForeground((Color)colorTable.elementAt(color));
			currentForeground = color;
		}
	}
	
	void setBackground(int color) {
		if (color != currentBackground) {
			// colors must already be in table
			gc.setBackground((Color)colorTable.elementAt(color));
			currentBackground = color;
		}
	}
	
	void newline() {
		x = leftMargin;
		y += lineHeight;
		if (y + lineHeight > bottomMargin) {
			printer.endPage();
			if (index + 1 < end) {
				y = topMargin;
				printer.startPage();
			}
		}
	}
	
	void saveState() {
		RTFState state = new RTFState();
		state.fontStyle = currentFontStyle;
		state.foreground = currentForeground;
		state.background = currentBackground;
		savedState.addElement(state);
	}
	
	void restoreState() {
		if (savedState.isEmpty()) return;
		if (creatingColorTable) {
			setForeground(0);
			setBackground(1);
			creatingColorTable = false;
		}
		
		int index = savedState.size() - 1;
		RTFState state = (RTFState)savedState.elementAt(index);
		savedState.removeElementAt(index);
		
		setFontStyle(state.fontStyle);
		if (state.foreground != -1) setForeground(state.foreground);
		if (state.background != -1) setBackground(state.background);
	}
}
