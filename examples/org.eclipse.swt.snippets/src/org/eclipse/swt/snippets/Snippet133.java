/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 502845
 *******************************************************************************/
package org.eclipse.swt.snippets;


/*
 * Printing example snippet: print text to printer, with word wrap and pagination
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import static org.eclipse.swt.events.SelectionListener.*;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.printing.*;
import org.eclipse.swt.widgets.*;

public class Snippet133 {
	Display display;
	Shell shell;
	Text text;
	Font font;
	Color foregroundColor, backgroundColor;

	Printer printer;
	GC gc;
	FontData[] printerFontData;
	RGB printerForeground, printerBackground;

	int lineHeight = 0;
	int tabWidth = 0;
	int leftMargin, rightMargin, topMargin, bottomMargin;
	int x, y;
	int index, end;
	String textToPrint;
	String tabs;
	StringBuilder wordBuffer;

	public static void main(String[] args) {
		new Snippet133().open();
	}

	void open() {
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setText("Print Text");
		text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);

		Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		MenuItem item = new MenuItem(menuBar, SWT.CASCADE);
		item.setText("&File");
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		item.setMenu(fileMenu);
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText("&Open...");
		item.setAccelerator(SWT.CTRL + 'O');
		item.addSelectionListener(widgetSelectedAdapter(e-> menuOpen()));
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText("Font...");
		item.addSelectionListener(widgetSelectedAdapter(e-> menuFont()));
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText("Foreground Color...");
		item.addSelectionListener(widgetSelectedAdapter(e-> menuForegroundColor()));
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText("Background Color...");
		item.addSelectionListener(widgetSelectedAdapter(e-> menuBackgroundColor()));
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText("&Print...");
		item.setAccelerator(SWT.CTRL + 'P');
		item.addSelectionListener(widgetSelectedAdapter(e-> menuPrint()));
		new MenuItem(fileMenu, SWT.SEPARATOR);
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText("E&xit");
		item.addSelectionListener(widgetSelectedAdapter(e->	shell.dispose()));

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		if (font != null) font.dispose();
		if (foregroundColor != null) foregroundColor.dispose();
		if (backgroundColor != null) backgroundColor.dispose();
		display.dispose();
	}

	void menuOpen() {
		final String textString;
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.java", "*.*" });
		String name = dialog.open();
		if ((name == null) || (name.length() == 0))
			return;

		File file = new File(name);
		try (FileInputStream stream = new FileInputStream(file.getPath())) {
			Reader in = new BufferedReader(new InputStreamReader(stream));
			char[] readBuffer = new char[2048];
			StringBuilder buffer = new StringBuilder((int) file.length());
			int n;
			while ((n = in.read(readBuffer)) > 0) {
				buffer.append(readBuffer, 0, n);
			}
			textString = buffer.toString();
			stream.close();
		} catch (FileNotFoundException e) {
			MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
			box.setMessage("File not found:\n" + name);
			box.open();
			return;
		} catch (IOException e) {
			MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
			box.setMessage("Error reading file:\n" + name);
			box.open();
			return;
		}

		text.setText(textString);
	}

	void menuFont() {
		FontDialog fontDialog = new FontDialog(shell);
		fontDialog.setFontList(text.getFont().getFontData());
		FontData fontData = fontDialog.open();
		if (fontData != null) {
			if (font != null) font.dispose();
			font = new Font(display, fontData);
			text.setFont(font);
		}
	}

	void menuForegroundColor() {
		ColorDialog colorDialog = new ColorDialog(shell);
		colorDialog.setRGB(text.getForeground().getRGB());
		RGB rgb = colorDialog.open();
		if (rgb != null) {
			if (foregroundColor != null) foregroundColor.dispose();
			foregroundColor = new Color(rgb);
			text.setForeground(foregroundColor);
		}
	}

	void menuBackgroundColor() {
		ColorDialog colorDialog = new ColorDialog(shell);
		colorDialog.setRGB(text.getBackground().getRGB());
		RGB rgb = colorDialog.open();
		if (rgb != null) {
			if (backgroundColor != null) backgroundColor.dispose();
			backgroundColor = new Color(rgb);
			text.setBackground(backgroundColor);
		}
	}

	void menuPrint() {
		PrintDialog dialog = new PrintDialog(shell, SWT.NONE);
		PrinterData data = dialog.open();
		if (data == null) return;
		if (data.printToFile) {
			data.fileName = "print.out"; // you probably want to ask the user for a filename
		}

		/* Get the text to print from the Text widget (you could get it from anywhere, i.e. your java model) */
		textToPrint = text.getText();

		/* Get the font & foreground & background data. */
		printerFontData = text.getFont().getFontData();
		printerForeground = text.getForeground().getRGB();
		printerBackground = text.getBackground().getRGB();

		/* Do the printing in a background thread so that spooling does not freeze the UI. */
		printer = new Printer(data);
		Thread printingThread = new Thread("Printing") {
			@Override
			public void run() {
				print(printer);
				printer.dispose();
			}
		};
		printingThread.start();
	}

	void print(Printer printer) {
		if (printer.startJob("Text")) {   // the string is the job name - shows up in the printer's job list
			Rectangle clientArea = printer.getClientArea();
			Rectangle trim = printer.computeTrim(0, 0, 0, 0);
			Point dpi = printer.getDPI();
			leftMargin = dpi.x + trim.x; // one inch from left side of paper
			rightMargin = clientArea.width - dpi.x + trim.x + trim.width; // one inch from right side of paper
			topMargin = dpi.y + trim.y; // one inch from top edge of paper
			bottomMargin = clientArea.height - dpi.y + trim.y + trim.height; // one inch from bottom edge of paper

			/* Create a buffer for computing tab width. */
			int tabSize = 4; // is tab width a user setting in your UI?
			StringBuilder tabBuffer = new StringBuilder(tabSize);
			for (int i = 0; i < tabSize; i++) tabBuffer.append(' ');
			tabs = tabBuffer.toString();

			/* Create printer GC, and create and set the printer font & foreground color. */
			gc = new GC(printer);
			Font printerFont = new Font(printer, printerFontData);
			Color printerForegroundColor = new Color(printerForeground);
			Color printerBackgroundColor = new Color(printerBackground);

			gc.setFont(printerFont);
			gc.setForeground(printerForegroundColor);
			gc.setBackground(printerBackgroundColor);
			tabWidth = gc.stringExtent(tabs).x;
			lineHeight = gc.getFontMetrics().getHeight();

			/* Print text to current gc using word wrap */
			printText();
			printer.endJob();

			/* Cleanup graphics resources used in printing */
			printerFont.dispose();
			printerForegroundColor.dispose();
			printerBackgroundColor.dispose();
			gc.dispose();
		}
	}

	void printText() {
		printer.startPage();
		wordBuffer = new StringBuilder();
		x = leftMargin;
		y = topMargin;
		index = 0;
		end = textToPrint.length();
		while (index < end) {
			char c = textToPrint.charAt(index);
			index++;
			if (c != 0) {
				if (c == 0x0a || c == 0x0d) {
					if (c == 0x0d && index < end && textToPrint.charAt(index) == 0x0a) {
						index++; // if this is cr-lf, skip the lf
					}
					printWordBuffer();
					newline();
				} else {
					if (c != '\t') {
						wordBuffer.append(c);
					}
					if (Character.isWhitespace(c)) {
						printWordBuffer();
						if (c == '\t') {
							x += tabWidth;
						}
					}
				}
			}
		}
		if (y + lineHeight <= bottomMargin) {
			printer.endPage();
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
			gc.drawString(word, x, y, false);
			x += wordWidth;
			wordBuffer = new StringBuilder();
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
}
