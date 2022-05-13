/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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
 *     Andrey Loskutov <loskutov@gmx.de> - bug 488172
 *     Stefan Xenos (Google) - bug 487254 - StyledText.getTopIndex() can return negative values
 *     Angelo Zerr <angelo.zerr@gmail.com> - Customize different line spacing of StyledText - Bug 522020
 *     Karsten Thoms <thoms@itemis.de> - bug 528746 add getOffsetAtPoint(Point)
 *******************************************************************************/
package org.eclipse.swt.custom;


import java.util.*;
import java.util.List;
import java.util.stream.*;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.printing.*;
import org.eclipse.swt.widgets.*;

/**
 * A StyledText is an editable user interface object that displays lines
 * of text.  The following style attributes can be defined for the text:
 * <ul>
 * <li>foreground color
 * <li>background color
 * <li>font style (bold, italic, bold-italic, regular)
 * <li>underline
 * <li>strikeout
 * </ul>
 * <p>
 * In addition to text style attributes, the background color of a line may
 * be specified.
 * </p><p>
 * There are two ways to use this widget when specifying text style information.
 * You may use the API that is defined for StyledText or you may define your own
 * LineStyleListener.  If you define your own listener, you will be responsible
 * for maintaining the text style information for the widget.  IMPORTANT: You may
 * not define your own listener and use the StyledText API.  The following
 * StyledText API is not supported if you have defined a LineStyleListener:</p>
 * <ul>
 * <li>getStyleRangeAtOffset(int)
 * <li>getStyleRanges()
 * <li>replaceStyleRanges(int,int,StyleRange[])
 * <li>setStyleRange(StyleRange)
 * <li>setStyleRanges(StyleRange[])
 * </ul>
 * <p>
 * There are two ways to use this widget when specifying line background colors.
 * You may use the API that is defined for StyledText or you may define your own
 * LineBackgroundListener.  If you define your own listener, you will be responsible
 * for maintaining the line background color information for the widget.
 * IMPORTANT: You may not define your own listener and use the StyledText API.
 * The following StyledText API is not supported if you have defined a
 * LineBackgroundListener:</p>
 * <ul>
 * <li>getLineBackground(int)
 * <li>setLineBackground(int,int,Color)
 * </ul>
 * <p>
 * The content implementation for this widget may also be user-defined.  To do so,
 * you must implement the StyledTextContent interface and use the StyledText API
 * setContent(StyledTextContent) to initialize the widget.
 * </p>
 * <dl>
 * <dt><b>Styles:</b><dd>FULL_SELECTION, MULTI, READ_ONLY, SINGLE, WRAP
 * <dt><b>Events:</b><dd>ExtendedModify, LineGetBackground, LineGetSegments, LineGetStyle, Modify, Selection, Verify, VerifyKey, OrientationChange
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#styledtext">StyledText snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Examples: CustomControlExample, TextEditor</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class StyledText extends Canvas {
	static final char TAB = '\t';
	static final String PlatformLineDelimiter = System.lineSeparator();
	static final int BIDI_CARET_WIDTH = 3;
	static final int DEFAULT_WIDTH	= 64;
	static final int DEFAULT_HEIGHT = 64;
	static final int V_SCROLL_RATE = 50;
	static final int H_SCROLL_RATE = 10;
	static final int PREVIOUS_OFFSET_TRAILING = 0;
	static final int OFFSET_LEADING = 1;

	static final String STYLEDTEXT_KEY = "org.eclipse.swt.internal.cocoa.styledtext"; //$NON-NLS-1$
	private static int getX(Point p) { return p.x; } // workaround p -> p.x lose typing and cannot be chained in comparing...
	static final Comparator<Point> SELECTION_COMPARATOR = Comparator.comparingInt(StyledText::getX).thenComparingInt(p -> p.y);

	Color selectionBackground;	// selection background color
	Color selectionForeground;	// selection foreground color
	StyledTextContent content;			// native content (default or user specified)
	StyledTextRenderer renderer;
	Listener listener;
	TextChangeListener textChangeListener;	// listener for TextChanging, TextChanged and TextSet events from StyledTextContent
	int verticalScrollOffset = 0;		// pixel based
	int horizontalScrollOffset = 0;		// pixel based
	boolean alwaysShowScroll = true;
	int ignoreResize = 0;
	int topIndex = 0;					// top visible line
	int topIndexY;
	int clientAreaHeight = 0;			// the client area height. Needed to calculate content width for new visible lines during Resize callback
	int clientAreaWidth = 0;			// the client area width. Needed during Resize callback to determine if line wrap needs to be recalculated
	int tabLength = 4;					// number of characters in a tab
	int [] tabs;
	int leftMargin;
	int topMargin;
	int rightMargin;
	int bottomMargin;
	Color marginColor;
	int columnX;						// keep track of the horizontal caret position when changing lines/pages. Fixes bug 5935
	Caret[] carets;
	int[] caretOffsets = {0};
	int caretAlignment;
	Point[] selection = { new Point(0, 0) }; // x and y are start and end caret offsets of selection (x <= y)
	int[] selectionAnchors = {0};				// position of selection anchor for the selection ranges. 0 based offset from beginning of text
	Point clipboardSelection;           // x and y are start and end caret offsets of previous selection
	Point doubleClickSelection;			// selection after last mouse double click
	boolean editable = true;
	boolean wordWrap = false;			// text is wrapped automatically
	boolean visualWrap = false;		// process line breaks inside logical lines (inserted by BidiSegmentEvent)
	boolean hasStyleWithVariableHeight = false;
	boolean hasVerticalIndent = false;
	boolean doubleClickEnabled = true;	// see getDoubleClickEnabled
	boolean overwrite = false;			// insert/overwrite edit mode
	int textLimit = -1;					// limits the number of characters the user can type in the widget. Unlimited by default.
	Map<Integer, Integer> keyActionMap = new HashMap<>();
	Color background = null;			// workaround for bug 4791
	Color foreground = null;			//
	/** True if a non-default background color is set */
	boolean customBackground;
	/** True if a non-default foreground color is set */
	boolean customForeground;
	/** False iff the widget is disabled */
	boolean enabled = true;
	/** True iff the widget is in the midst of being enabled or disabled */
	boolean insideSetEnableCall;
	Clipboard clipboard;
	int clickCount;
	int autoScrollDirection = SWT.NULL;	// the direction of autoscrolling (up, down, right, left)
	int autoScrollDistance = 0;
	int lastTextChangeStart;			// cache data of the
	int lastTextChangeNewLineCount;		// last text changing
	int lastTextChangeNewCharCount;		// event for use in the
	int lastTextChangeReplaceLineCount;	// text changed handler
	int lastTextChangeReplaceCharCount;
	int lastCharCount = 0;
	int lastLineBottom;					// the bottom pixel of the last line been replaced
	boolean bidiColoring = false;		// apply the BIDI algorithm on text segments of the same color
	Image leftCaretBitmap = null;
	Image rightCaretBitmap = null;
	int caretDirection = SWT.NULL;
	int caretWidth = 0;
	Caret defaultCaret = null;
	boolean updateCaretDirection = true;
	boolean dragDetect = true;
	IME ime;
	Cursor cursor;
	int alignment;
	boolean justify;
	int indent, wrapIndent;
	int lineSpacing;
	int alignmentMargin;
	int newOrientation = SWT.NONE;
	int accCaretOffset;
	Accessible acc;
	AccessibleControlAdapter accControlAdapter;
	AccessibleAttributeAdapter accAttributeAdapter;
	AccessibleEditableTextListener accEditableTextListener;
	AccessibleTextExtendedAdapter accTextExtendedAdapter;
	AccessibleAdapter accAdapter;
	MouseNavigator mouseNavigator;
	boolean middleClickPressed;

	//block selection
	boolean blockSelection;
	int blockXAnchor = -1, blockYAnchor = -1;
	int blockXLocation = -1, blockYLocation = -1;

	final static boolean IS_MAC, IS_GTK;
	static {
		String platform = SWT.getPlatform();
		IS_MAC = "cocoa".equals(platform);
		IS_GTK = "gtk".equals(platform);
	}

	/**
	 * The Printing class implements printing of a range of text.
	 * An instance of <code>Printing</code> is returned in the
	 * StyledText#print(Printer) API. The run() method may be
	 * invoked from any thread.
	 */
	static class Printing implements Runnable {
		final static int LEFT = 0;						// left aligned header/footer segment
		final static int CENTER = 1;					// centered header/footer segment
		final static int RIGHT = 2;						// right aligned header/footer segment

		Printer printer;
		StyledTextRenderer printerRenderer;
		StyledTextPrintOptions printOptions;
		Rectangle clientArea;
		FontData fontData;
		Font printerFont;
		Map<Resource, Resource> resources;
		int tabLength;
		GC gc;											// printer GC
		int pageWidth;									// width of a printer page in pixels
		int startPage;									// first page to print
		int endPage;									// last page to print
		int scope;										// scope of print job
		int startLine;									// first (wrapped) line to print
		int endLine;									// last (wrapped) line to print
		boolean singleLine;								// widget single line mode
		Point[] selection = { new Point(0, 0) }; // x and y are start and end caret offsets of selection (x <= y)
		boolean mirrored;						// indicates the printing gc should be mirrored
		int lineSpacing;
		int printMargin;

	/**
	 * Creates an instance of <code>Printing</code>.
	 * Copies the widget content and rendering data that needs
	 * to be requested from listeners.
	 *
	 * @param parent StyledText widget to print.
	 * @param printer printer device to print on.
	 * @param printOptions print options
	 */
	Printing(StyledText styledText, Printer printer, StyledTextPrintOptions printOptions) {
		this.printer = printer;
		this.printOptions = printOptions;
		this.mirrored = (styledText.getStyle() & SWT.MIRRORED) != 0;
		singleLine = styledText.isSingleLine();
		startPage = 1;
		endPage = Integer.MAX_VALUE;
		PrinterData data = printer.getPrinterData();
		scope = data.scope;
		if (scope == PrinterData.PAGE_RANGE) {
			startPage = data.startPage;
			endPage = data.endPage;
			if (endPage < startPage) {
				int temp = endPage;
				endPage = startPage;
				startPage = temp;
			}
		} else if (scope == PrinterData.SELECTION) {
			selection = Arrays.copyOf(styledText.selection, styledText.selection.length);
		}
		printerRenderer = new StyledTextRenderer(printer, null);
		printerRenderer.setContent(copyContent(styledText.getContent()));
		cacheLineData(styledText);
	}
	/**
	 * Caches all line data that needs to be requested from a listener.
	 *
	 * @param printerContent <code>StyledTextContent</code> to request
	 * 	line data for.
	 */
	void cacheLineData(StyledText styledText) {
		StyledTextRenderer renderer = styledText.renderer;
		renderer.copyInto(printerRenderer);
		fontData = styledText.getFont().getFontData()[0];
		tabLength = styledText.tabLength;
		int lineCount = printerRenderer.lineCount;
		if (styledText.isListening(ST.LineGetBackground) || (styledText.isListening(ST.LineGetSegments)) || styledText.isListening(ST.LineGetStyle)) {
			StyledTextContent content = printerRenderer.content;
			for (int i = 0; i < lineCount; i++) {
				String line = content.getLine(i);
				int lineOffset = content.getOffsetAtLine(i);
				StyledTextEvent event = styledText.getLineBackgroundData(lineOffset, line);
				if (event != null && event.lineBackground != null) {
					printerRenderer.setLineBackground(i, 1, event.lineBackground);
				}
				event = styledText.getBidiSegments(lineOffset, line);
				if (event != null) {
					printerRenderer.setLineSegments(i, 1, event.segments);
					printerRenderer.setLineSegmentChars(i, 1, event.segmentsChars);
				}
				event = styledText.getLineStyleData(lineOffset, line);
				if (event != null) {
					printerRenderer.setLineIndent(i, 1, event.indent);
					printerRenderer.setLineAlignment(i, 1, event.alignment);
					printerRenderer.setLineJustify(i, 1, event.justify);
					printerRenderer.setLineBullet(i, 1, event.bullet);
					StyleRange[] styles = event.styles;
					if (styles != null && styles.length > 0) {
						printerRenderer.setStyleRanges(event.ranges, styles);
					}
				}
			}
		}
		Point screenDPI = styledText.getDisplay().getDPI();
		Point printerDPI = printer.getDPI();
		resources = new HashMap<> ();
		for (int i = 0; i < lineCount; i++) {
			Color color = printerRenderer.getLineBackground(i, null);
			if (color != null) {
				if (printOptions.printLineBackground) {
					Color printerColor = (Color)resources.get(color);
					if (printerColor == null) {
						printerColor = new Color (color.getRGB());
						resources.put(color, printerColor);
					}
					printerRenderer.setLineBackground(i, 1, printerColor);
				} else {
					printerRenderer.setLineBackground(i, 1, null);
				}
			}
			int indent = printerRenderer.getLineIndent(i, 0);
			if (indent != 0) {
				printerRenderer.setLineIndent(i, 1, indent * printerDPI.x / screenDPI.x);
			}
		}
		StyleRange[] styles = printerRenderer.styles;
		for (int i = 0; i < printerRenderer.styleCount; i++) {
			StyleRange style = styles[i];
			Font font = style.font;
			if (style.font != null) {
				Font printerFont = (Font)resources.get(font);
				if (printerFont == null) {
					printerFont = new Font (printer, font.getFontData());
					resources.put(font, printerFont);
				}
				style.font = printerFont;
			}
			Color color = style.foreground;
			if (color != null) {
				Color printerColor = (Color)resources.get(color);
				if (printOptions.printTextForeground) {
					if (printerColor == null) {
						printerColor = new Color (color.getRGB());
						resources.put(color, printerColor);
					}
					style.foreground = printerColor;
				} else {
					style.foreground = null;
				}
			}
			color = style.background;
			if (color != null) {
				Color printerColor = (Color)resources.get(color);
				if (printOptions.printTextBackground) {
					if (printerColor == null) {
						printerColor = new Color (color.getRGB());
						resources.put(color, printerColor);
					}
					style.background = printerColor;
				} else {
					style.background = null;
				}
			}
			if (!printOptions.printTextFontStyle) {
				style.fontStyle = SWT.NORMAL;
			}
			style.rise = style.rise * printerDPI.y / screenDPI.y;
			GlyphMetrics metrics = style.metrics;
			if (metrics != null) {
				metrics.ascent = metrics.ascent * printerDPI.y / screenDPI.y;
				metrics.descent = metrics.descent * printerDPI.y / screenDPI.y;
				metrics.width = metrics.width * printerDPI.x / screenDPI.x;
			}
		}
		lineSpacing = styledText.lineSpacing * printerDPI.y / screenDPI.y;
		if (printOptions.printLineNumbers) {
			printMargin = 3 * printerDPI.x / screenDPI.x;
		}
	}
	/**
	 * Copies the text of the specified <code>StyledTextContent</code>.
	 *
	 * @param original the <code>StyledTextContent</code> to copy.
	 */
	StyledTextContent copyContent(StyledTextContent original) {
		StyledTextContent printerContent = new DefaultContent();
		int insertOffset = 0;
		for (int i = 0; i < original.getLineCount(); i++) {
			int insertEndOffset;
			if (i < original.getLineCount() - 1) {
				insertEndOffset = original.getOffsetAtLine(i + 1);
			} else {
				insertEndOffset = original.getCharCount();
			}
			printerContent.replaceTextRange(insertOffset, 0, original.getTextRange(insertOffset, insertEndOffset - insertOffset));
			insertOffset = insertEndOffset;
		}
		return printerContent;
	}
	/**
	 * Disposes of the resources and the <code>PrintRenderer</code>.
	 */
	void dispose() {
		if (gc != null) {
			gc.dispose();
			gc = null;
		}
		if (resources != null) {
			for (Resource resource : resources.values()) {
				resource.dispose();
			}
			resources = null;
		}
		if (printerFont != null) {
			printerFont.dispose();
			printerFont = null;
		}
		if (printerRenderer != null) {
			printerRenderer.dispose();
			printerRenderer = null;
		}
	}
	void init() {
		Rectangle trim = printer.computeTrim(0, 0, 0, 0);
		Point dpi = printer.getDPI();

		printerFont = new Font(printer, fontData.getName(), fontData.getHeight(), SWT.NORMAL);
		clientArea = printer.getClientArea();
		pageWidth = clientArea.width;
		// one inch margin around text
		clientArea.x = dpi.x + trim.x;
		clientArea.y = dpi.y + trim.y;
		clientArea.width -= (clientArea.x + trim.width);
		clientArea.height -= (clientArea.y + trim.height);

		int style = mirrored ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT;
		gc = new GC(printer, style);
		gc.setFont(printerFont);
		printerRenderer.setFont(printerFont, tabLength);
		int lineHeight = printerRenderer.getLineHeight();
		if (printOptions.header != null) {
			clientArea.y += lineHeight * 2;
			clientArea.height -= lineHeight * 2;
		}
		if (printOptions.footer != null) {
			clientArea.height -= lineHeight * 2;
		}

		// TODO not wrapped
		StyledTextContent content = printerRenderer.content;
		startLine = 0;
		endLine = singleLine ? 0 : content.getLineCount() - 1;
		if (scope == PrinterData.PAGE_RANGE) {
			int pageSize = clientArea.height / lineHeight;//WRONG
			startLine = (startPage - 1) * pageSize;
		} else if (scope == PrinterData.SELECTION) {
			startLine = content.getLineAtOffset(selection[0].x);
			if (selection[0].y > 0) {
				endLine = content.getLineAtOffset(selection[0].y);
			} else {
				endLine = startLine - 1;
			}
		}
	}
	/**
	 * Prints the lines in the specified page range.
	 */
	void print() {
		Color background = gc.getBackground();
		Color foreground = gc.getForeground();
		int paintY = clientArea.y;
		int paintX = clientArea.x;
		int width = clientArea.width;
		int page = startPage;
		int pageBottom = clientArea.y + clientArea.height;
		int orientation =  gc.getStyle() & (SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT);
		TextLayout printLayout = null;
		if (printOptions.printLineNumbers || printOptions.header != null || printOptions.footer != null) {
			printLayout = new TextLayout(printer);
			printLayout.setFont(printerFont);
		}
		if (printOptions.printLineNumbers) {
			int numberingWidth = 0;
			int count = endLine - startLine + 1;
			String[] lineLabels = printOptions.lineLabels;
			if (lineLabels != null) {
				for (int i = startLine; i < Math.min(count, lineLabels.length); i++) {
					if (lineLabels[i] != null) {
						printLayout.setText(lineLabels[i]);
						int lineWidth = printLayout.getBounds().width;
						numberingWidth = Math.max(numberingWidth, lineWidth);
					}
				}
			} else {
				StringBuilder buffer = new StringBuilder("0");
				while ((count /= 10) > 0) buffer.append("0");
				printLayout.setText(buffer.toString());
				numberingWidth = printLayout.getBounds().width;
			}
			numberingWidth += printMargin;
			if (numberingWidth > width) numberingWidth = width;
			paintX += numberingWidth;
			width -= numberingWidth;
		}
		for (int i = startLine; i <= endLine && page <= endPage; i++) {
			if (paintY == clientArea.y) {
				printer.startPage();
				printDecoration(page, true, printLayout);
			}
			TextLayout layout = printerRenderer.getTextLayout(i, orientation, width, lineSpacing);
			Color lineBackground = printerRenderer.getLineBackground(i, background);
			int paragraphBottom = paintY + layout.getBounds().height;
			if (paragraphBottom <= pageBottom) {
				//normal case, the whole paragraph fits in the current page
				printLine(paintX, paintY, gc, foreground, lineBackground, layout, printLayout, i);
				paintY = paragraphBottom;
			} else {
				int lineCount = layout.getLineCount();
				while (paragraphBottom > pageBottom && lineCount > 0) {
					lineCount--;
					paragraphBottom -= layout.getLineBounds(lineCount).height + layout.getSpacing();
				}
				if (lineCount == 0) {
					//the whole paragraph goes to the next page
					printDecoration(page, false, printLayout);
					printer.endPage();
					page++;
					if (page <= endPage) {
						printer.startPage();
						printDecoration(page, true, printLayout);
						paintY = clientArea.y;
						printLine(paintX, paintY, gc, foreground, lineBackground, layout, printLayout, i);
						paintY += layout.getBounds().height;
					}
				} else {
					//draw paragraph top in the current page and paragraph bottom in the next
					int height = paragraphBottom - paintY;
					gc.setClipping(clientArea.x, paintY, clientArea.width, height);
					printLine(paintX, paintY, gc, foreground, lineBackground, layout, printLayout, i);
					gc.setClipping((Rectangle)null);
					printDecoration(page, false, printLayout);
					printer.endPage();
					page++;
					if (page <= endPage) {
						printer.startPage();
						printDecoration(page, true, printLayout);
						paintY = clientArea.y - height;
						int layoutHeight = layout.getBounds().height;
						gc.setClipping(clientArea.x, clientArea.y, clientArea.width, layoutHeight - height);
						printLine(paintX, paintY, gc, foreground, lineBackground, layout, printLayout, i);
						gc.setClipping((Rectangle)null);
						paintY += layoutHeight;
					}
				}
			}
			printerRenderer.disposeTextLayout(layout);
		}
		if (page <= endPage && paintY > clientArea.y) {
			// close partial page
			printDecoration(page, false, printLayout);
			printer.endPage();
		}
		if (printLayout != null) printLayout.dispose();
	}
	/**
	 * Print header or footer decorations.
	 *
	 * @param page page number to print, if specified in the StyledTextPrintOptions header or footer.
	 * @param header true = print the header, false = print the footer
	 */
	void printDecoration(int page, boolean header, TextLayout layout) {
		String text = header ? printOptions.header : printOptions.footer;
		if (text == null) return;
		int lastSegmentIndex = 0;
		for (int i = 0; i < 3; i++) {
			int segmentIndex = text.indexOf(StyledTextPrintOptions.SEPARATOR, lastSegmentIndex);
			String segment;
			if (segmentIndex == -1) {
				segment = text.substring(lastSegmentIndex);
				printDecorationSegment(segment, i, page, header, layout);
				break;
			} else {
				segment = text.substring(lastSegmentIndex, segmentIndex);
				printDecorationSegment(segment, i, page, header, layout);
				lastSegmentIndex = segmentIndex + StyledTextPrintOptions.SEPARATOR.length();
			}
		}
	}
	/**
	 * Print one segment of a header or footer decoration.
	 * Headers and footers have three different segments.
	 * One each for left aligned, centered, and right aligned text.
	 *
	 * @param segment decoration segment to print
	 * @param alignment alignment of the segment. 0=left, 1=center, 2=right
	 * @param page page number to print, if specified in the decoration segment.
	 * @param header true = print the header, false = print the footer
	 */
	void printDecorationSegment(String segment, int alignment, int page, boolean header, TextLayout layout) {
		int pageIndex = segment.indexOf(StyledTextPrintOptions.PAGE_TAG);
		if (pageIndex != -1) {
			int pageTagLength = StyledTextPrintOptions.PAGE_TAG.length();
			StringBuilder buffer = new StringBuilder(segment.substring (0, pageIndex));
			buffer.append (page);
			buffer.append (segment.substring(pageIndex + pageTagLength));
			segment = buffer.toString();
		}
		if (segment.length() > 0) {
			layout.setText(segment);
			int segmentWidth = layout.getBounds().width;
			int segmentHeight = printerRenderer.getLineHeight();
			int drawX = 0, drawY;
			if (alignment == LEFT) {
				drawX = clientArea.x;
			} else if (alignment == CENTER) {
				drawX = (pageWidth - segmentWidth) / 2;
			} else if (alignment == RIGHT) {
				drawX = clientArea.x + clientArea.width - segmentWidth;
			}
			if (header) {
				drawY = clientArea.y - segmentHeight * 2;
			} else {
				drawY = clientArea.y + clientArea.height + segmentHeight;
			}
			layout.draw(gc, drawX, drawY);
		}
	}
	void printLine(int x, int y, GC gc, Color foreground, Color background, TextLayout layout, TextLayout printLayout, int index) {
		if (background != null) {
			Rectangle rect = layout.getBounds();
			gc.setBackground(background);
			gc.fillRectangle(x, y, rect.width, rect.height);

//			int lineCount = layout.getLineCount();
//			for (int i = 0; i < lineCount; i++) {
//				Rectangle rect = layout.getLineBounds(i);
//				rect.x += paintX;
//				rect.y += paintY + layout.getSpacing();
//				rect.width = width;//layout bounds
//				gc.fillRectangle(rect);
//			}
		}
		if (printOptions.printLineNumbers) {
			FontMetrics metrics = layout.getLineMetrics(0);
			printLayout.setAscent(metrics.getAscent() + metrics.getLeading());
			printLayout.setDescent(metrics.getDescent());
			String[] lineLabels = printOptions.lineLabels;
			if (lineLabels != null) {
				if (0 <= index && index < lineLabels.length && lineLabels[index] != null) {
					printLayout.setText(lineLabels[index]);
				} else {
					printLayout.setText("");
				}
			} else {
				printLayout.setText(String.valueOf(index));
			}
			int paintX = x - printMargin - printLayout.getBounds().width;
			printLayout.draw(gc, paintX, y);
			printLayout.setAscent(-1);
			printLayout.setDescent(-1);
		}
		gc.setForeground(foreground);
		layout.draw(gc, x, y);
	}
	/**
	 * Starts a print job and prints the pages specified in the constructor.
	 */
	@Override
	public void run() {
		String jobName = printOptions.jobName;
		if (jobName == null) {
			jobName = "Printing";
		}
		if (printer.startJob(jobName)) {
			init();
			print();
			dispose();
			printer.endJob();
		}
	}
	}
	/**
	 * The <code>RTFWriter</code> class is used to write widget content as
	 * rich text. The implementation complies with the RTF specification
	 * version 1.5.
	 * <p>
	 * toString() is guaranteed to return a valid RTF string only after
	 * close() has been called.
	 * </p><p>
	 * Whole and partial lines and line breaks can be written. Lines will be
	 * formatted using the styles queried from the LineStyleListener, if
	 * set, or those set directly in the widget. All styles are applied to
	 * the RTF stream like they are rendered by the widget. In addition, the
	 * widget font name and size is used for the whole text.
	 * </p>
	 */
	class RTFWriter extends TextWriter {
		static final int DEFAULT_FOREGROUND = 0;
		static final int DEFAULT_BACKGROUND = 1;
		List<Color> colorTable;
		List<Font> fontTable;

	/**
	 * Creates a RTF writer that writes content starting at offset "start"
	 * in the document.  <code>start</code> and <code>length</code>can be set to specify partial
	 * lines.
	 *
	 * @param start start offset of content to write, 0 based from
	 * 	beginning of document
	 * @param length length of content to write
	 */
	public RTFWriter(int start, int length) {
		super(start, length);
		colorTable = new ArrayList<>();
		fontTable = new ArrayList<>();
		colorTable.add(getForeground());
		colorTable.add(getBackground());
		fontTable.add(getFont());
	}
	/**
	 * Closes the RTF writer. Once closed no more content can be written.
	 * <b>NOTE:</b>  <code>toString()</code> does not return a valid RTF string until
	 * <code>close()</code> has been called.
	 */
	@Override
	public void close() {
		if (!isClosed()) {
			writeHeader();
			write("\n}}\0");
			super.close();
		}
	}
	/**
	 * Returns the index of the specified color in the RTF color table.
	 *
	 * @param color the color
	 * @param defaultIndex return value if color is null
	 * @return the index of the specified color in the RTF color table
	 * 	or "defaultIndex" if "color" is null.
	 */
	int getColorIndex(Color color, int defaultIndex) {
		if (color == null) return defaultIndex;
		int index = colorTable.indexOf(color);
		if (index == -1) {
			index = colorTable.size();
			colorTable.add(color);
		}
		return index;
	}
	/**
	 * Returns the index of the specified color in the RTF color table.
	 *
	 * @param color the color
	 * @param defaultIndex return value if color is null
	 * @return the index of the specified color in the RTF color table
	 * 	or "defaultIndex" if "color" is null.
	 */
	int getFontIndex(Font font) {
		int index = fontTable.indexOf(font);
		if (index == -1) {
			index = fontTable.size();
			fontTable.add(font);
		}
		return index;
	}
	/**
	 * Appends the specified segment of "string" to the RTF data.
	 * Copy from <code>start</code> up to, but excluding, <code>end</code>.
	 *
	 * @param string string to copy a segment from. Must not contain
	 * 	line breaks. Line breaks should be written using writeLineDelimiter()
	 * @param start start offset of segment. 0 based.
	 * @param end end offset of segment
	 */
	void write(String string, int start, int end) {
		for (int index = start; index < end; index++) {
			char ch = string.charAt(index);
			if (ch > 0x7F) {
				// write the sub string from the last escaped character
				// to the current one. Fixes bug 21698.
				if (index > start) {
					write(string.substring(start, index));
				}
				write("\\u");
				write(Integer.toString((short) ch));
				write('?');						// ANSI representation (1 byte long, \\uc1)
				start = index + 1;
			} else if (ch == '}' || ch == '{' || ch == '\\') {
				// write the sub string from the last escaped character
				// to the current one. Fixes bug 21698.
				if (index > start) {
					write(string.substring(start, index));
				}
				write('\\');
				write(ch);
				start = index + 1;
			}
		}
		// write from the last escaped character to the end.
		// Fixes bug 21698.
		if (start < end) {
			write(string.substring(start, end));
		}
	}
	/**
	 * Writes the RTF header including font table and color table.
	 */
	void writeHeader() {
		StringBuilder header = new StringBuilder();
		FontData fontData = getFont().getFontData()[0];
		header.append("{\\rtf1\\ansi");
		// specify code page, necessary for copy to work in bidi
		// systems that don't support Unicode RTF.
		String cpg = System.getProperty("file.encoding").toLowerCase();
		if (cpg.startsWith("cp") || cpg.startsWith("ms")) {
			cpg = cpg.substring(2, cpg.length());
			header.append("\\ansicpg");
			header.append(cpg);
		}
		header.append("\\uc1\\deff0{\\fonttbl{\\f0\\fnil ");
		header.append(fontData.getName());
		header.append(";");
		for (int i = 1; i < fontTable.size(); i++) {
			header.append("\\f");
			header.append(i);
			header.append(" ");
			FontData fd = fontTable.get(i).getFontData()[0];
			header.append(fd.getName());
			header.append(";");
		}
		header.append("}}\n{\\colortbl");
		for (Color color : colorTable) {
			header.append("\\red");
			header.append(color.getRed());
			header.append("\\green");
			header.append(color.getGreen());
			header.append("\\blue");
			header.append(color.getBlue());
			header.append(";");
		}
		// some RTF readers ignore the deff0 font tag. Explicitly
		// set the font for the whole document to work around this.
		header.append("}\n{\\f0\\fs");
		// font size is specified in half points
		header.append(fontData.getHeight() * 2);
		header.append(" ");
		write(header.toString(), 0);
	}
	/**
	 * Appends the specified line text to the RTF data.  Lines will be formatted
	 * using the styles queried from the LineStyleListener, if set, or those set
	 * directly in the widget.
	 *
	 * @param line line text to write as RTF. Must not contain line breaks
	 * 	Line breaks should be written using writeLineDelimiter()
	 * @param lineOffset offset of the line. 0 based from the start of the
	 * 	widget document. Any text occurring before the start offset or after the
	 * 	end offset specified during object creation is ignored.
	 * @exception SWTException <ul>
	 *   <li>ERROR_IO when the writer is closed.</li>
	 * </ul>
	 */
	@Override
	public void writeLine(String line, int lineOffset) {
		if (isClosed()) {
			SWT.error(SWT.ERROR_IO);
		}
		int lineIndex = content.getLineAtOffset(lineOffset);
		int lineAlignment, lineIndent;
		boolean lineJustify;
		int[] ranges;
		StyleRange[] styles;
		StyledTextEvent event = getLineStyleData(lineOffset, line);
		if (event != null) {
			lineAlignment = event.alignment;
			lineIndent = event.indent;
			lineJustify = event.justify;
			ranges = event.ranges;
			styles = event.styles;
		} else {
			lineAlignment = renderer.getLineAlignment(lineIndex, alignment);
			lineIndent =  renderer.getLineIndent(lineIndex, indent);
			lineJustify = renderer.getLineJustify(lineIndex, justify);
			ranges = renderer.getRanges(lineOffset, line.length());
			styles = renderer.getStyleRanges(lineOffset, line.length(), false);
		}
		if (styles == null) styles = new StyleRange[0];
		Color lineBackground = renderer.getLineBackground(lineIndex, null);
		event = getLineBackgroundData(lineOffset, line);
		if (event != null && event.lineBackground != null) lineBackground = event.lineBackground;
		writeStyledLine(line, lineOffset, ranges, styles, lineBackground, lineIndent, lineAlignment, lineJustify);
	}
	/**
	 * Appends the specified line delimiter to the RTF data.
	 *
	 * @param lineDelimiter line delimiter to write as RTF.
	 * @exception SWTException <ul>
	 *   <li>ERROR_IO when the writer is closed.</li>
	 * </ul>
	 */
	@Override
	public void writeLineDelimiter(String lineDelimiter) {
		if (isClosed()) {
			SWT.error(SWT.ERROR_IO);
		}
		write(lineDelimiter, 0, lineDelimiter.length());
		write("\\par ");
	}
	/**
	 * Appends the specified line text to the RTF data.
	 * <p>
	 * Use the colors and font styles specified in "styles" and "lineBackground".
	 * Formatting is written to reflect the text rendering by the text widget.
	 * Style background colors take precedence over the line background color.
	 * Background colors are written using the \chshdng0\chcbpat tag (vs. the \cb tag).
	 * </p>
	 *
	 * @param line line text to write as RTF. Must not contain line breaks
	 * 	Line breaks should be written using writeLineDelimiter()
	 * @param lineOffset offset of the line. 0 based from the start of the
	 * 	widget document. Any text occurring before the start offset or after the
	 * 	end offset specified during object creation is ignored.
	 * @param styles styles to use for formatting. Must not be null.
	 * @param lineBackground line background color to use for formatting.
	 * 	May be null.
	 */
	void writeStyledLine(String line, int lineOffset, int ranges[], StyleRange[] styles, Color lineBackground, int indent, int alignment, boolean justify) {
		int lineLength = line.length();
		int startOffset = getStart();
		int writeOffset = startOffset - lineOffset;
		if (writeOffset >= lineLength) return;
		int lineIndex = Math.max(0, writeOffset);

		write("\\fi");
		write(indent);
		switch (alignment) {
			case SWT.LEFT: write("\\ql"); break;
			case SWT.CENTER: write("\\qc"); break;
			case SWT.RIGHT: write("\\qr"); break;
		}
		if (justify) write("\\qj");
		write(" ");

		if (lineBackground != null) {
			write("{\\chshdng0\\chcbpat");
			write(getColorIndex(lineBackground, DEFAULT_BACKGROUND));
			write(" ");
		}
		int endOffset = startOffset + super.getCharCount();
		int lineEndOffset = Math.min(lineLength, endOffset - lineOffset);
		for (int i = 0; i < styles.length; i++) {
			StyleRange style = styles[i];
			int start, end;
			if (ranges != null) {
				start = ranges[i << 1] - lineOffset;
				end = start + ranges[(i << 1) + 1];
			} else {
				start = style.start - lineOffset;
				end = start + style.length;
			}
			// skip over partial first line
			if (end < writeOffset) {
				continue;
			}
			// style starts beyond line end or RTF write end
			if (start >= lineEndOffset) {
				break;
			}
			// write any unstyled text
			if (lineIndex < start) {
				// copy to start of style
				// style starting beyond end of write range or end of line
				// is guarded against above.
				write(line, lineIndex, start);
				lineIndex = start;
			}
			// write styled text
			write("{\\cf");
			write(getColorIndex(style.foreground, DEFAULT_FOREGROUND));
			int colorIndex = getColorIndex(style.background, DEFAULT_BACKGROUND);
			if (colorIndex != DEFAULT_BACKGROUND) {
				write("\\chshdng0\\chcbpat");
				write(colorIndex);
			}
			int fontStyle = style.fontStyle;
			Font font = style.font;
			if (font != null) {
				int fontIndex = getFontIndex(font);
				write("\\f");
				write(fontIndex);
				FontData fontData = font.getFontData()[0];
				write("\\fs");
				write(fontData.getHeight() * 2);
				fontStyle = fontData.getStyle();
			}
			if ((fontStyle & SWT.BOLD) != 0) {
				write("\\b");
			}
			if ((fontStyle & SWT.ITALIC) != 0) {
				write("\\i");
			}
			if (style.underline) {
				write("\\ul");
			}
			if (style.strikeout) {
				write("\\strike");
			}
			write(" ");
			// copy to end of style or end of write range or end of line
			int copyEnd = Math.min(end, lineEndOffset);
			// guard against invalid styles and let style processing continue
			copyEnd = Math.max(copyEnd, lineIndex);
			write(line, lineIndex, copyEnd);
			if ((fontStyle & SWT.BOLD) != 0) {
				write("\\b0");
			}
			if ((style.fontStyle & SWT.ITALIC) != 0) {
				write("\\i0");
			}
			if (style.underline) {
				write("\\ul0");
			}
			if (style.strikeout) {
				write("\\strike0");
			}
			write("}");
			lineIndex = copyEnd;
		}
		// write unstyled text at the end of the line
		if (lineIndex < lineEndOffset) {
			write(line, lineIndex, lineEndOffset);
		}
		if (lineBackground != null) write("}");
	}
	}
	/**
	 * The <code>TextWriter</code> class is used to write widget content to
	 * a string.  Whole and partial lines and line breaks can be written. To write
	 * partial lines, specify the start and length of the desired segment
	 * during object creation.
	 * <p>
	 * <b>NOTE:</b> <code>toString()</code> is guaranteed to return a valid string only after close()
	 * has been called.
	 * </p>
	 */
	static class TextWriter {
		private StringBuilder buffer;
		private int startOffset;	// offset of first character that will be written
		private int endOffset;		// offset of last character that will be written.
									// 0 based from the beginning of the widget text.
		private boolean isClosed = false;

	/**
	 * Creates a writer that writes content starting at offset "start"
	 * in the document.  <code>start</code> and <code>length</code> can be set to specify partial lines.
	 *
	 * @param start start offset of content to write, 0 based from beginning of document
	 * @param length length of content to write
	 */
	public TextWriter(int start, int length) {
		buffer = new StringBuilder(length);
		startOffset = start;
		endOffset = start + length;
	}
	/**
	 * Closes the writer. Once closed no more content can be written.
	 * <b>NOTE:</b>  <code>toString()</code> is not guaranteed to return a valid string unless
	 * the writer is closed.
	 */
	public void close() {
		if (!isClosed) {
			isClosed = true;
		}
	}
	/**
	 * Returns the number of characters to write.
	 * @return the integer number of characters to write
	 */
	public int getCharCount() {
		return endOffset - startOffset;
	}
	/**
	 * Returns the offset where writing starts. 0 based from the start of
	 * the widget text. Used to write partial lines.
	 * @return the integer offset where writing starts
	 */
	public int getStart() {
		return startOffset;
	}
	/**
	 * Returns whether the writer is closed.
	 * @return a boolean specifying whether or not the writer is closed
	 */
	public boolean isClosed() {
		return isClosed;
	}
	/**
	 * Returns the string.  <code>close()</code> must be called before <code>toString()</code>
	 * is guaranteed to return a valid string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return buffer.toString();
	}
	/**
	 * Appends the given string to the data.
	 */
	void write(String string) {
		buffer.append(string);
	}
	/**
	 * Inserts the given string to the data at the specified offset.
	 * <p>
	 * Do nothing if "offset" is &lt; 0 or &gt; getCharCount()
	 * </p>
	 *
	 * @param string text to insert
	 * @param offset offset in the existing data to insert "string" at.
	 */
	void write(String string, int offset) {
		if (offset < 0 || offset > buffer.length()) {
			return;
		}
		buffer.insert(offset, string);
	}
	/**
	 * Appends the given int to the data.
	 */
	void write(int i) {
		buffer.append(i);
	}
	/**
	 * Appends the given character to the data.
	 */
	void write(char i) {
		buffer.append(i);
	}
	/**
	 * Appends the specified line text to the data.
	 *
	 * @param line line text to write. Must not contain line breaks
	 * 	Line breaks should be written using writeLineDelimiter()
	 * @param lineOffset offset of the line. 0 based from the start of the
	 * 	widget document. Any text occurring before the start offset or after the
	 *	end offset specified during object creation is ignored.
	 * @exception SWTException <ul>
	 *   <li>ERROR_IO when the writer is closed.</li>
	 * </ul>
	 */
	public void writeLine(String line, int lineOffset) {
		if (isClosed) {
			SWT.error(SWT.ERROR_IO);
		}
		int writeOffset = startOffset - lineOffset;
		int lineLength = line.length();
		int lineIndex;
		if (writeOffset >= lineLength) {
			return;							// whole line is outside write range
		} else if (writeOffset > 0) {
			lineIndex = writeOffset;		// line starts before write start
		} else {
			lineIndex = 0;
		}
		int copyEnd = Math.min(lineLength, endOffset - lineOffset);
		if (lineIndex < copyEnd) {
			write(line.substring(lineIndex, copyEnd));
		}
	}
	/**
	 * Appends the specified line delimiter to the data.
	 *
	 * @param lineDelimiter line delimiter to write
	 * @exception SWTException <ul>
	 *   <li>ERROR_IO when the writer is closed.</li>
	 * </ul>
	 */
	public void writeLineDelimiter(String lineDelimiter) {
		if (isClosed) {
			SWT.error(SWT.ERROR_IO);
		}
		write(lineDelimiter);
	}
	}

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 *
 * @see SWT#FULL_SELECTION
 * @see SWT#MULTI
 * @see SWT#READ_ONLY
 * @see SWT#SINGLE
 * @see SWT#WRAP
 * @see #getStyle
 */
public StyledText(Composite parent, int style) {
	super(parent, checkStyle(style));
	// set the fg in the OS to ensure that these are the same as StyledText, necessary
	// for ensuring that the bg/fg the IME box uses is the same as what StyledText uses
	super.setForeground(getForeground());
	super.setDragDetect(false);
	Display display = getDisplay();
	if ((style & SWT.READ_ONLY) != 0) {
		setEditable(false);
	}
	leftMargin = rightMargin = isBidiCaret() ? BIDI_CARET_WIDTH - 1: 0;
	if ((style & SWT.SINGLE) != 0 && (style & SWT.BORDER) != 0) {
		leftMargin = topMargin = rightMargin = bottomMargin = 2;
	}
	alignment = style & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	if (alignment == 0) alignment = SWT.LEFT;
	clipboard = new Clipboard(display);
	installDefaultContent();
	renderer = new StyledTextRenderer(getDisplay(), this);
	renderer.setContent(content);
	renderer.setFont(getFont(), tabLength);
	ime = new IME(this, SWT.NONE);
	defaultCaret = new Caret(this, SWT.NONE);
	if ((style & SWT.WRAP) != 0) {
		setWordWrap(true);
	}
	if (isBidiCaret()) {
		createCaretBitmaps();
		Runnable runnable = () -> {
			int direction = BidiUtil.getKeyboardLanguage() == BidiUtil.KEYBOARD_BIDI ? SWT.RIGHT : SWT.LEFT;
			if (direction == caretDirection) return;
			if (getCaret() != defaultCaret) return;
			setCaretLocations(Arrays.stream(caretOffsets).mapToObj(this::getPointAtOffset).toArray(Point[]::new), direction);
		};
		BidiUtil.addLanguageListener(this, runnable);
	}
	setCaret(defaultCaret);
	calculateScrollBars();
	createKeyBindings();
	super.setCursor(display.getSystemCursor(SWT.CURSOR_IBEAM));
	installListeners();
	initializeAccessible();
	setData("DEFAULT_DROP_TARGET_EFFECT", new StyledTextDropTargetEffect(this));
	if (IS_MAC) setData(STYLEDTEXT_KEY);
}
/**
 * Adds an extended modify listener. An ExtendedModify event is sent by the
 * widget when the widget text has changed.
 *
 * @param extendedModifyListener the listener
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void addExtendedModifyListener(ExtendedModifyListener extendedModifyListener) {
	checkWidget();
	if (extendedModifyListener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	StyledTextListener typedListener = new StyledTextListener(extendedModifyListener);
	addListener(ST.ExtendedModify, typedListener);
}
/**
 * Adds a bidirectional segment listener.
 * <p>
 * A BidiSegmentEvent is sent
 * whenever a line of text is measured or rendered. You can
 * specify text ranges in the line that should be treated as if they
 * had a different direction than the surrounding text.
 * This may be used when adjacent segments of right-to-left text should
 * not be reordered relative to each other.
 * E.g., multiple Java string literals in a right-to-left language
 * should generally remain in logical order to each other, that is, the
 * way they are stored.
 * </p>
 *
 * @param listener the listener
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 * @see BidiSegmentEvent
 * @since 2.0
 */
public void addBidiSegmentListener(BidiSegmentListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	addListener(ST.LineGetSegments, new StyledTextListener(listener));
	resetCache(0, content.getLineCount());
	setCaretLocations();
	super.redraw();
}
/**
 * Adds a caret listener. CaretEvent is sent when the caret offset changes.
 *
 * @param listener the listener
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 *
 * @since 3.5
 */
public void addCaretListener(CaretListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	addListener(ST.CaretMoved, new StyledTextListener(listener));
}
/**
 * Adds a line background listener. A LineGetBackground event is sent by the
 * widget to determine the background color for a line.
 *
 * @param listener the listener
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void addLineBackgroundListener(LineBackgroundListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (!isListening(ST.LineGetBackground)) {
		renderer.clearLineBackground(0, content.getLineCount());
	}
	addListener(ST.LineGetBackground, new StyledTextListener(listener));
}
/**
 * Adds a line style listener. A LineGetStyle event is sent by the widget to
 * determine the styles for a line.
 *
 * @param listener the listener
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void addLineStyleListener(LineStyleListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (!isListening(ST.LineGetStyle)) {
		setStyleRanges(0, 0, null, null, true);
		renderer.clearLineStyle(0, content.getLineCount());
	}
	addListener(ST.LineGetStyle, new StyledTextListener(listener));
	setCaretLocations();
}
/**
 * Adds a modify listener. A Modify event is sent by the widget when the widget text
 * has changed.
 *
 * @param modifyListener the listener
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void addModifyListener(ModifyListener modifyListener) {
	checkWidget();
	if (modifyListener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	addListener(SWT.Modify, new TypedListener(modifyListener));
}
/**
 * Adds a paint object listener. A paint object event is sent by the widget when an object
 * needs to be drawn.
 *
 * @param listener the listener
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 *
 * @since 3.2
 *
 * @see PaintObjectListener
 * @see PaintObjectEvent
 */
public void addPaintObjectListener(PaintObjectListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	addListener(ST.PaintObject, new StyledTextListener(listener));
}
/**
 * Adds a selection listener. A Selection event is sent by the widget when the
 * user changes the selection.
 * <p>
 * When <code>widgetSelected</code> is called, the event x and y fields contain
 * the start and end caret indices of the selection[0]. The selection values returned are visual
 * (i.e., x will always always be &lt;= y).
 * No event is sent when the caret is moved while the selection length is 0.
 * </p><p>
 * <code>widgetDefaultSelected</code> is not called for StyledTexts.
 * </p>
 *
 * @param listener the listener which should be notified when the user changes the receiver's selection

 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	addListener(SWT.Selection, new TypedListener(listener));
}
/**
 * Adds a verify key listener. A VerifyKey event is sent by the widget when a key
 * is pressed. The widget ignores the key press if the listener sets the doit field
 * of the event to false.
 *
 * @param listener the listener
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void addVerifyKeyListener(VerifyKeyListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	addListener(ST.VerifyKey, new StyledTextListener(listener));
}
/**
 * Adds a verify listener. A Verify event is sent by the widget when the widget text
 * is about to change. The listener can set the event text and the doit field to
 * change the text that is set in the widget or to force the widget to ignore the
 * text change.
 *
 * @param verifyListener the listener
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void addVerifyListener(VerifyListener verifyListener) {
	checkWidget();
	if (verifyListener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	addListener(SWT.Verify, new TypedListener(verifyListener));
}
/**
 * Adds a word movement listener. A movement event is sent when the boundary
 * of a word is needed. For example, this occurs during word next and word
 * previous actions.
 *
 * @param movementListener the listener
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 *
 * @see MovementEvent
 * @see MovementListener
 * @see #removeWordMovementListener
 *
 * @since 3.3
 */
public void addWordMovementListener(MovementListener movementListener) {
	checkWidget();
	if (movementListener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	addListener(ST.WordNext, new StyledTextListener(movementListener));
	addListener(ST.WordPrevious, new StyledTextListener(movementListener));
}
/**
 * Appends a string to the text at the end of the widget.
 *
 * @param string the string to be appended
 * @see #replaceTextRange(int,int,String)
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void append(String string) {
	checkWidget();
	if (string == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	int lastChar = Math.max(getCharCount(), 0);
	replaceTextRange(lastChar, 0, string);
}
/**
 * Calculates the scroll bars
 */
void calculateScrollBars() {
	ScrollBar horizontalBar = getHorizontalBar();
	ScrollBar verticalBar = getVerticalBar();
	setScrollBars(true);
	if (verticalBar != null) {
		verticalBar.setIncrement(getVerticalIncrement());
	}
	if (horizontalBar != null) {
		horizontalBar.setIncrement(getHorizontalIncrement());
	}
}
/**
 * Calculates the top index based on the current vertical scroll offset.
 * The top index is the index of the topmost fully visible line or the
 * topmost partially visible line if no line is fully visible.
 * The top index starts at 0.
 */
void calculateTopIndex(int delta) {
	int oldDelta = delta;
	int oldTopIndex = topIndex;
	int oldTopIndexY = topIndexY;
	if (isFixedLineHeight()) {
		int verticalIncrement = getVerticalIncrement();
		if (verticalIncrement == 0) {
			return;
		}
		topIndex = Compatibility.ceil(getVerticalScrollOffset(), verticalIncrement);
		// Set top index to partially visible top line if no line is fully
		// visible but at least some of the widget client area is visible.
		// Fixes bug 15088.
		if (topIndex >= 0) {
			if (clientAreaHeight > 0) {
				int bottomPixel = getVerticalScrollOffset() + clientAreaHeight;
				topIndexY = getLinePixel(topIndex);
				int fullLineTopPixel = topIndex * verticalIncrement;
				int fullLineVisibleHeight = bottomPixel - fullLineTopPixel;
				// set top index to partially visible line if no line fully fits in
				// client area or if space is available but not used (the latter should
				// never happen because we use claimBottomFreeSpace)
				if (fullLineVisibleHeight < verticalIncrement) {
					topIndex = getVerticalScrollOffset() / verticalIncrement;
				}
			} else if (topIndex >= content.getLineCount()) {
				topIndex = content.getLineCount() - 1;
			}
		}
	} else {
		if (delta >= 0) {
			delta -= topIndexY;
			int lineIndex = topIndex;
			int lineCount = content.getLineCount();
			while (lineIndex < lineCount) {
				if (delta <= 0) break;
				delta -= renderer.getCachedLineHeight(lineIndex++);
			}
			if (lineIndex < lineCount && -delta + renderer.getCachedLineHeight(lineIndex) <= clientAreaHeight - topMargin - bottomMargin) {
				topIndex = lineIndex;
				topIndexY = -delta;
			} else {
				topIndex = lineIndex - 1;
				topIndexY = -renderer.getCachedLineHeight(topIndex) - delta;
			}
		} else {
			delta -= topIndexY;
			int lineIndex = topIndex;
			while (lineIndex > 0) {
				int lineHeight = renderer.getCachedLineHeight(lineIndex - 1);
				if (delta + lineHeight > 0) break;
				delta += lineHeight;
				lineIndex--;
			}
			if (lineIndex == 0 || -delta + renderer.getCachedLineHeight(lineIndex) <= clientAreaHeight - topMargin - bottomMargin) {
				topIndex = lineIndex;
				topIndexY = - delta;
			} else {
				topIndex = lineIndex - 1;
				topIndexY = - renderer.getCachedLineHeight(topIndex) - delta;
			}
		}
	}
	if (topIndex < 0) {
		// TODO: This logging is in place to determine why topIndex is getting set to negative values.
		// It should be deleted once we fix the root cause of this issue. See bug 487254 for details.
		System.err.println("StyledText: topIndex was " + topIndex
				+ ", isFixedLineHeight() = " + isFixedLineHeight()
				+ ", delta = " + delta
				+ ", content.getLineCount() = " + content.getLineCount()
				+ ", clientAreaHeight = " + clientAreaHeight
				+ ", oldTopIndex = " + oldTopIndex
				+ ", oldTopIndexY = " + oldTopIndexY
				+ ", getVerticalScrollOffset = " + getVerticalScrollOffset()
				+ ", oldDelta = " + oldDelta
				+ ", getVerticalIncrement() = " + getVerticalIncrement());
		topIndex = 0;
	}
	if (topIndex != oldTopIndex || oldTopIndexY != topIndexY) {
		int width = renderer.getWidth();
		renderer.calculateClientArea();
		if (width != renderer.getWidth()) {
			setScrollBars(false);
		}
	}
}
/**
 * Hides the scroll bars if widget is created in single line mode.
 */
static int checkStyle(int style) {
	if ((style & SWT.SINGLE) != 0) {
		style &= ~(SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP | SWT.MULTI);
	} else {
		style |= SWT.MULTI;
		if ((style & SWT.WRAP) != 0) {
			style &= ~SWT.H_SCROLL;
		}
	}
	style |= SWT.NO_REDRAW_RESIZE | SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND;
	/* Clear SWT.CENTER to avoid the conflict with SWT.EMBEDDED */
	return style & ~SWT.CENTER;
}
/**
 * Scrolls down the text to use new space made available by a resize or by
 * deleted lines.
 */
void claimBottomFreeSpace() {
	if (ime.getCompositionOffset() != -1) return;
	if (isFixedLineHeight()) {
		int newVerticalOffset = Math.max(0, renderer.getHeight() - clientAreaHeight);
		if (newVerticalOffset < getVerticalScrollOffset()) {
			scrollVertical(newVerticalOffset - getVerticalScrollOffset(), true);
		}
	} else {
		int bottomIndex = getPartialBottomIndex();
		int height = getLinePixel(bottomIndex + 1);
		if (clientAreaHeight > height) {
			scrollVertical(-getAvailableHeightAbove(clientAreaHeight - height), true);
		}
	}
}
/**
 * Scrolls text to the right to use new space made available by a resize.
 */
void claimRightFreeSpace() {
	int newHorizontalOffset = Math.max(0, renderer.getWidth() - clientAreaWidth);
	if (newHorizontalOffset < horizontalScrollOffset) {
		// item is no longer drawn past the right border of the client area
		// align the right end of the item with the right border of the
		// client area (window is scrolled right).
		scrollHorizontal(newHorizontalOffset - horizontalScrollOffset, true);
	}
}
void clearBlockSelection(boolean reset, boolean sendEvent) {
	if (reset) resetSelection();
	blockXAnchor = blockYAnchor = -1;
	blockXLocation = blockYLocation = -1;
	caretDirection = SWT.NULL;
	updateCaretVisibility();
	super.redraw();
	if (sendEvent) sendSelectionEvent();
}
/**
 * Removes the widget selection.
 *
 * @param sendEvent a Selection event is sent when set to true and when the selection is actually reset.
 */
void clearSelection(boolean sendEvent) {
	int selectionStart = selection[0].x;
	int selectionEnd = selection[0].y;
	resetSelection();
	// redraw old selection, if any
	if (selectionEnd - selectionStart > 0) {
		int length = content.getCharCount();
		// called internally to remove selection after text is removed
		// therefore make sure redraw range is valid.
		int redrawStart = Math.min(selectionStart, length);
		int redrawEnd = Math.min(selectionEnd, length);
		if (redrawEnd - redrawStart > 0) {
			internalRedrawRange(redrawStart, redrawEnd - redrawStart);
		}
		if (sendEvent) {
			sendSelectionEvent();
		}
	}
}
@Override
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int lineCount = (getStyle() & SWT.SINGLE) != 0 ? 1 : content.getLineCount();
	int width = 0;
	int height = 0;
	if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
		Display display = getDisplay();
		int maxHeight = display.getClientArea().height;
		for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
			TextLayout layout = renderer.getTextLayout(lineIndex);
			int wrapWidth = layout.getWidth();
			if (wordWrap) layout.setWidth(wHint == 0 ? 1 : wHint == SWT.DEFAULT ? SWT.DEFAULT : Math.max(1, wHint - leftMargin - rightMargin));
			Rectangle rect = layout.getBounds();
			height += rect.height;
			width = Math.max(width, rect.width);
			layout.setWidth(wrapWidth);
			renderer.disposeTextLayout(layout);
			if (isFixedLineHeight() && height > maxHeight) break;
		}
		if (isFixedLineHeight()) {
			height = lineCount * renderer.getLineHeight();
		}
	}
	// Use default values if no text is defined.
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int wTrim = getLeftMargin() + rightMargin + getCaretWidth();
	int hTrim = topMargin + bottomMargin;
	Rectangle rect = computeTrim(0, 0, width + wTrim, height + hTrim);
	return new Point (rect.width, rect.height);
}
/**
 * Copies the selected text to the <code>DND.CLIPBOARD</code> clipboard.
 * <p>
 * The text will be put on the clipboard in plain text format and RTF format.
 * The <code>DND.CLIPBOARD</code> clipboard is used for data that is
 * transferred by keyboard accelerator (such as Ctrl+C/Ctrl+V) or
 * by menu action.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void copy() {
	checkWidget();
	copySelection(DND.CLIPBOARD);
}
/**
 * Copies the selected text to the specified clipboard.  The text will be put in the
 * clipboard in plain text format and RTF format.
 * <p>
 * The clipboardType is  one of the clipboard constants defined in class
 * <code>DND</code>.  The <code>DND.CLIPBOARD</code>  clipboard is
 * used for data that is transferred by keyboard accelerator (such as Ctrl+C/Ctrl+V)
 * or by menu action.  The <code>DND.SELECTION_CLIPBOARD</code>
 * clipboard is used for data that is transferred by selecting text and pasting
 * with the middle mouse button.
 * </p>
 *
 * @param clipboardType indicates the type of clipboard
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void copy(int clipboardType) {
	checkWidget();
	copySelection(clipboardType);
}
boolean copySelection(int type) {
	if (type != DND.CLIPBOARD && type != DND.SELECTION_CLIPBOARD) return false;
	try {
		if (blockSelection && blockXLocation != -1) {
			String text = getBlockSelectionText(PlatformLineDelimiter);
			if (text.length() > 0) {
				//TODO RTF support
				TextTransfer plainTextTransfer = TextTransfer.getInstance();
				Object[] data = new Object[]{text};
				Transfer[] types = new Transfer[]{plainTextTransfer};
				clipboard.setContents(data, types, type);
				return true;
			}
		} else if (getSelectionRanges().length > 2) {
			StringBuilder text = new StringBuilder();
			int[] ranges = getSelectionRanges();
			for (int i = 0; i < ranges.length; i += 2) {
				int offset = ranges[i];
				int length = ranges[i + 1];
				text.append(length == 0 ? "" : getText(offset, offset + length - 1));
				text.append(PlatformLineDelimiter);
			}
			text.delete(text.length() - PlatformLineDelimiter.length(), text.length());
			if (text.length() > 0) {
				//TODO RTF support
				clipboard.setContents(new Object[]{text.toString()},  new Transfer[]{TextTransfer.getInstance()}, type);
				return true;
			}
		} else {
			int length = selection[0].y - selection[0].x;
			if (length > 0) {
				setClipboardContent(selection[0].x, length, type);
				return true;
			}
		}
	} catch (SWTError error) {
		// Copy to clipboard failed. This happens when another application
		// is accessing the clipboard while we copy. Ignore the error.
		// Rethrow all other errors. Fixes bug 17578.
		if (error.code != DND.ERROR_CANNOT_SET_CLIPBOARD) {
			throw error;
		}
	}
	return false;
}
/**
 * Returns the alignment of the widget.
 *
 * @return the alignment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *R
 * @see #getLineAlignment(int)
 *
 * @since 3.2
 */
public int getAlignment() {
	checkWidget();
	return alignment;
}
/**
 * Returns the Always Show Scrollbars flag.  True if the scrollbars are
 * always shown even if they are not required.  False if the scrollbars are only
 * visible when some part of the content needs to be scrolled to be seen.
 * The H_SCROLL and V_SCROLL style bits are also required to enable scrollbars in the
 * horizontal and vertical directions.
 *
 * @return the Always Show Scrollbars flag value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.8
 */
public boolean getAlwaysShowScrollBars() {
	checkWidget();
	return alwaysShowScroll;
}
int getAvailableHeightAbove(int height) {
	int maxHeight = verticalScrollOffset;
	if (maxHeight == -1) {
		int lineIndex = topIndex - 1;
		maxHeight = -topIndexY;
		if (topIndexY > 0) {
			maxHeight += renderer.getLineHeight(lineIndex--);
		}
		while (height > maxHeight && lineIndex >= 0) {
			maxHeight += renderer.getLineHeight(lineIndex--);
		}
	}
	return Math.min(height, maxHeight);
}
int getAvailableHeightBellow(int height) {
	int partialBottomIndex = getPartialBottomIndex();
	int topY = getLinePixel(partialBottomIndex);
	int lineHeight = renderer.getLineHeight(partialBottomIndex);
	int availableHeight = 0;
	int clientAreaHeight = this.clientAreaHeight - topMargin - bottomMargin;
	if (topY + lineHeight > clientAreaHeight) {
		availableHeight = lineHeight - (clientAreaHeight - topY);
	}
	int lineIndex = partialBottomIndex + 1;
	int lineCount = content.getLineCount();
	while (height > availableHeight && lineIndex < lineCount) {
		availableHeight += renderer.getLineHeight(lineIndex++);
	}
	return Math.min(height, availableHeight);
}
/**
 * Returns the color of the margins.
 *
 * @return the color of the margins.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.5
 */
public Color getMarginColor() {
	checkWidget();
	return marginColor != null ? marginColor : getBackground();
}
/**
 * Returns a string that uses only the line delimiter specified by the
 * StyledTextContent implementation.
 * <p>
 * Returns only the first line if the widget has the SWT.SINGLE style.
 * </p>
 *
 * @param text the text that may have line delimiters that don't
 * 	match the model line delimiter. Possible line delimiters
 * 	are CR ('\r'), LF ('\n'), CR/LF ("\r\n")
 * @return the converted text that only uses the line delimiter
 * 	specified by the model. Returns only the first line if the widget
 * 	has the SWT.SINGLE style.
 */
String getModelDelimitedText(String text) {
	int length = text.length();
	if (length == 0) {
		return text;
	}
	int crIndex = 0;
	int lfIndex = 0;
	int i = 0;
	StringBuilder convertedText = new StringBuilder(length);
	String delimiter = getLineDelimiter();
	while (i < length) {
		if (crIndex != -1) {
			crIndex = text.indexOf(SWT.CR, i);
		}
		if (lfIndex != -1) {
			lfIndex = text.indexOf(SWT.LF, i);
		}
		if (lfIndex == -1 && crIndex == -1) {	// no more line breaks?
			break;
		} else if ((crIndex < lfIndex && crIndex != -1) || lfIndex == -1) {
			convertedText.append(text.substring(i, crIndex));
			if (lfIndex == crIndex + 1) {		// CR/LF combination?
				i = lfIndex + 1;
			} else {
				i = crIndex + 1;
			}
		} else {									// LF occurs before CR!
			convertedText.append(text.substring(i, lfIndex));
			i = lfIndex + 1;
		}
		if (isSingleLine()) {
			break;
		}
		convertedText.append(delimiter);
	}
	// copy remaining text if any and if not in single line mode or no
	// text copied thus far (because there only is one line)
	if (i < length && (!isSingleLine() || convertedText.length() == 0)) {
		convertedText.append(text.substring(i));
	}
	return convertedText.toString();
}
boolean checkDragDetect(Event event) {
	if (!isListening(SWT.DragDetect)) return false;
	if (event.button != 1) return false;
	if (blockSelection && blockXLocation != -1) {
		Rectangle rect = getBlockSelectionRectangle();
		if (rect.contains(event.x, event.y)) {
			return dragDetect(event);
		}
	} else {
		if (selection[0].x == selection[0].y) return false;
		int offset = getOffsetAtPoint(event.x, event.y, null, true);
		if (selection[0].x <= offset && offset < selection[0].y) {
			return dragDetect(event);
		}

	}
	return false;
}

/**
 * Creates default key bindings.
 */
void createKeyBindings() {
	int nextKey = isMirrored() ? SWT.ARROW_LEFT : SWT.ARROW_RIGHT;
	int previousKey = isMirrored() ? SWT.ARROW_RIGHT : SWT.ARROW_LEFT;

	// Navigation
	setKeyBinding(SWT.ARROW_UP, ST.LINE_UP);
	setKeyBinding(SWT.ARROW_DOWN, ST.LINE_DOWN);
	if (IS_MAC) {
		setKeyBinding(previousKey | SWT.MOD1, ST.LINE_START);
		setKeyBinding(nextKey | SWT.MOD1, ST.LINE_END);
		setKeyBinding(SWT.HOME, ST.TEXT_START);
		setKeyBinding(SWT.END, ST.TEXT_END);
		setKeyBinding(SWT.ARROW_UP | SWT.MOD1, ST.TEXT_START);
		setKeyBinding(SWT.ARROW_DOWN | SWT.MOD1, ST.TEXT_END);
		setKeyBinding(nextKey | SWT.MOD3, ST.WORD_NEXT);
		setKeyBinding(previousKey | SWT.MOD3, ST.WORD_PREVIOUS);
	} else {
		setKeyBinding(SWT.HOME, ST.LINE_START);
		setKeyBinding(SWT.END, ST.LINE_END);
		setKeyBinding(SWT.HOME | SWT.MOD1, ST.TEXT_START);
		setKeyBinding(SWT.END | SWT.MOD1, ST.TEXT_END);
		setKeyBinding(nextKey | SWT.MOD1, ST.WORD_NEXT);
		setKeyBinding(previousKey | SWT.MOD1, ST.WORD_PREVIOUS);
	}
	setKeyBinding(SWT.PAGE_UP, ST.PAGE_UP);
	setKeyBinding(SWT.PAGE_DOWN, ST.PAGE_DOWN);
	setKeyBinding(SWT.PAGE_UP | SWT.MOD1, ST.WINDOW_START);
	setKeyBinding(SWT.PAGE_DOWN | SWT.MOD1, ST.WINDOW_END);
	setKeyBinding(nextKey, ST.COLUMN_NEXT);
	setKeyBinding(previousKey, ST.COLUMN_PREVIOUS);

	// Selection
	setKeyBinding(SWT.ARROW_UP | SWT.MOD2, ST.SELECT_LINE_UP);
	setKeyBinding(SWT.ARROW_DOWN | SWT.MOD2, ST.SELECT_LINE_DOWN);
	if (IS_MAC) {
		setKeyBinding(previousKey | SWT.MOD1 | SWT.MOD2, ST.SELECT_LINE_START);
		setKeyBinding(nextKey | SWT.MOD1 | SWT.MOD2, ST.SELECT_LINE_END);
		setKeyBinding(SWT.HOME | SWT.MOD2, ST.SELECT_TEXT_START);
		setKeyBinding(SWT.END | SWT.MOD2, ST.SELECT_TEXT_END);
		setKeyBinding(SWT.ARROW_UP | SWT.MOD1 | SWT.MOD2, ST.SELECT_TEXT_START);
		setKeyBinding(SWT.ARROW_DOWN | SWT.MOD1 | SWT.MOD2, ST.SELECT_TEXT_END);
		setKeyBinding(nextKey | SWT.MOD2 | SWT.MOD3, ST.SELECT_WORD_NEXT);
		setKeyBinding(previousKey | SWT.MOD2 | SWT.MOD3, ST.SELECT_WORD_PREVIOUS);
	} else  {
		setKeyBinding(SWT.HOME | SWT.MOD2, ST.SELECT_LINE_START);
		setKeyBinding(SWT.END | SWT.MOD2, ST.SELECT_LINE_END);
		setKeyBinding(SWT.HOME | SWT.MOD1 | SWT.MOD2, ST.SELECT_TEXT_START);
		setKeyBinding(SWT.END | SWT.MOD1 | SWT.MOD2, ST.SELECT_TEXT_END);
		setKeyBinding(nextKey | SWT.MOD1 | SWT.MOD2, ST.SELECT_WORD_NEXT);
		setKeyBinding(previousKey | SWT.MOD1 | SWT.MOD2, ST.SELECT_WORD_PREVIOUS);
	}
	setKeyBinding(SWT.PAGE_UP | SWT.MOD2, ST.SELECT_PAGE_UP);
	setKeyBinding(SWT.PAGE_DOWN | SWT.MOD2, ST.SELECT_PAGE_DOWN);
	setKeyBinding(SWT.PAGE_UP | SWT.MOD1 | SWT.MOD2, ST.SELECT_WINDOW_START);
	setKeyBinding(SWT.PAGE_DOWN | SWT.MOD1 | SWT.MOD2, ST.SELECT_WINDOW_END);
	setKeyBinding(nextKey | SWT.MOD2, ST.SELECT_COLUMN_NEXT);
	setKeyBinding(previousKey | SWT.MOD2, ST.SELECT_COLUMN_PREVIOUS);

	// Modification
	// Cut, Copy, Paste
	setKeyBinding('X' | SWT.MOD1, ST.CUT);
	setKeyBinding('C' | SWT.MOD1, ST.COPY);
	setKeyBinding('V' | SWT.MOD1, ST.PASTE);
	if (IS_MAC) {
		setKeyBinding(SWT.DEL | SWT.MOD2, ST.DELETE_NEXT);
		setKeyBinding(SWT.BS | SWT.MOD3, ST.DELETE_WORD_PREVIOUS);
		setKeyBinding(SWT.DEL | SWT.MOD3, ST.DELETE_WORD_NEXT);
	} else {
		// Cut, Copy, Paste Wordstar style
		setKeyBinding(SWT.DEL | SWT.MOD2, ST.CUT);
		setKeyBinding(SWT.INSERT | SWT.MOD1, ST.COPY);
		setKeyBinding(SWT.INSERT | SWT.MOD2, ST.PASTE);
	}
	setKeyBinding(SWT.BS | SWT.MOD2, ST.DELETE_PREVIOUS);
	setKeyBinding(SWT.BS, ST.DELETE_PREVIOUS);
	setKeyBinding(SWT.DEL, ST.DELETE_NEXT);
	setKeyBinding(SWT.BS | SWT.MOD1, ST.DELETE_WORD_PREVIOUS);
	setKeyBinding(SWT.DEL | SWT.MOD1, ST.DELETE_WORD_NEXT);

	// Miscellaneous
	setKeyBinding(SWT.INSERT, ST.TOGGLE_OVERWRITE);
}
/**
 * Create the bitmaps to use for the caret in bidi mode.  This
 * method only needs to be called upon widget creation and when the
 * font changes (the caret bitmap height needs to match font height).
 */
void createCaretBitmaps() {
	int caretWidth = BIDI_CARET_WIDTH;
	Display display = getDisplay();
	if (leftCaretBitmap != null) {
		if (defaultCaret != null && leftCaretBitmap.equals(defaultCaret.getImage())) {
			defaultCaret.setImage(null);
		}
		leftCaretBitmap.dispose();
	}
	int lineHeight = renderer.getLineHeight();
	leftCaretBitmap = new Image(display, caretWidth, lineHeight);
	GC gc = new GC (leftCaretBitmap);
	gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
	gc.fillRectangle(0, 0, caretWidth, lineHeight);
	gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
	gc.drawLine(0,0,0,lineHeight);
	gc.drawLine(0,0,caretWidth-1,0);
	gc.drawLine(0,1,1,1);
	gc.dispose();

	if (rightCaretBitmap != null) {
		if (defaultCaret != null && rightCaretBitmap.equals(defaultCaret.getImage())) {
			defaultCaret.setImage(null);
		}
		rightCaretBitmap.dispose();
	}
	rightCaretBitmap = new Image(display, caretWidth, lineHeight);
	gc = new GC (rightCaretBitmap);
	gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
	gc.fillRectangle(0, 0, caretWidth, lineHeight);
	gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
	gc.drawLine(caretWidth-1,0,caretWidth-1,lineHeight);
	gc.drawLine(0,0,caretWidth-1,0);
	gc.drawLine(caretWidth-1,1,1,1);
	gc.dispose();
}
/**
 * Moves the selected text to the clipboard.  The text will be put in the
 * clipboard in plain text format and RTF format.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void cut() {
	checkWidget();
	// Abort cut operation if copy to clipboard fails.
	// Fixes bug 21030.
	if (copySelection(DND.CLIPBOARD)) {
		if (blockSelection && blockXLocation != -1) {
			insertBlockSelectionText((char)0, SWT.NULL);
		} else {
			doDelete();
		}
	}
}
/**
 * A mouse move event has occurred.  See if we should start autoscrolling.  If
 * the move position is outside of the client area, initiate autoscrolling.
 * Otherwise, we've moved back into the widget so end autoscrolling.
 */
void doAutoScroll(Event event) {
	int caretLine = getFirstCaretLine();
	if (event.y > clientAreaHeight - bottomMargin && caretLine != content.getLineCount() - 1) {
		doAutoScroll(SWT.DOWN, event.y - (clientAreaHeight - bottomMargin));
	} else if (event.y < topMargin && caretLine != 0) {
		doAutoScroll(SWT.UP, topMargin - event.y);
	} else if (event.x < leftMargin && !wordWrap) {
		doAutoScroll(ST.COLUMN_PREVIOUS, leftMargin - event.x);
	} else if (event.x > clientAreaWidth - rightMargin && !wordWrap) {
		doAutoScroll(ST.COLUMN_NEXT, event.x - (clientAreaWidth - rightMargin));
	} else {
		endAutoScroll();
	}
}
/**
 * Initiates autoscrolling.
 *
 * @param direction SWT.UP, SWT.DOWN, SWT.COLUMN_NEXT, SWT.COLUMN_PREVIOUS
 */
void doAutoScroll(int direction, int distance) {
	autoScrollDistance = distance;
	// If we're already autoscrolling in the given direction do nothing
	if (autoScrollDirection == direction) {
		return;
	}

	Runnable timer = null;
	final Display display = getDisplay();
	// Set a timer that will simulate the user pressing and holding
	// down a cursor key (i.e., arrowUp, arrowDown).
	if (direction == SWT.UP) {
		timer = new Runnable() {
			@Override
			public void run() {
				/* Bug 437357 - NPE in StyledText.getCaretLine
				 * StyledText.content is null at times, probably because the
				 * widget itself has been disposed.
				 */
				if (isDisposed()) return;
				if (autoScrollDirection == SWT.UP) {
					if (blockSelection) {
						int verticalScrollOffset = getVerticalScrollOffset();
						int y = blockYLocation - verticalScrollOffset;
						int pixels = Math.max(-autoScrollDistance, -verticalScrollOffset);
						if (pixels != 0) {
							setBlockSelectionLocation(blockXLocation - horizontalScrollOffset, y + pixels, true);
							scrollVertical(pixels, true);
						}
					} else {
						doSelectionPageUp(autoScrollDistance);
					}
					display.timerExec(V_SCROLL_RATE, this);
				}
			}
		};
		autoScrollDirection = direction;
		display.timerExec(V_SCROLL_RATE, timer);
	} else if (direction == SWT.DOWN) {
		timer = new Runnable() {
			@Override
			public void run() {
				/* Bug 437357 - NPE in StyledText.getCaretLine
				 * StyledText.content is null at times, probably because the
				 * widget itself has been disposed.
				 */
				if (isDisposed()) return;
				if (autoScrollDirection == SWT.DOWN) {
					if (blockSelection) {
						int verticalScrollOffset = getVerticalScrollOffset();
						int y = blockYLocation - verticalScrollOffset;
						int max = renderer.getHeight() - verticalScrollOffset - clientAreaHeight;
						int pixels = Math.min(autoScrollDistance, Math.max(0,max));
						if (pixels != 0) {
							setBlockSelectionLocation(blockXLocation - horizontalScrollOffset, y + pixels, true);
							scrollVertical(pixels, true);
						}
					} else {
						doSelectionPageDown(autoScrollDistance);
					}
					display.timerExec(V_SCROLL_RATE, this);
				}
			}
		};
		autoScrollDirection = direction;
		display.timerExec(V_SCROLL_RATE, timer);
	} else if (direction == ST.COLUMN_NEXT) {
		timer = new Runnable() {
			@Override
			public void run() {
				/* Bug 437357 - NPE in StyledText.getCaretLine
				 * StyledText.content is null at times, probably because the
				 * widget itself has been disposed.
				 */
				if (isDisposed()) return;
				if (autoScrollDirection == ST.COLUMN_NEXT) {
					if (blockSelection) {
						int x = blockXLocation - horizontalScrollOffset;
						int max = renderer.getWidth() - horizontalScrollOffset - clientAreaWidth;
						int pixels = Math.min(autoScrollDistance, Math.max(0,max));
						if (pixels != 0) {
							setBlockSelectionLocation(x + pixels, blockYLocation - getVerticalScrollOffset(), true);
							scrollHorizontal(pixels, true);
						}
					} else {
						doVisualNext();
						setMouseWordSelectionAnchor();
						doMouseSelection();
					}
					display.timerExec(H_SCROLL_RATE, this);
				}
			}
		};
		autoScrollDirection = direction;
		display.timerExec(H_SCROLL_RATE, timer);
	} else if (direction == ST.COLUMN_PREVIOUS) {
		timer = new Runnable() {
			@Override
			public void run() {
				/* Bug 437357 - NPE in StyledText.getCaretLine
				 * StyledText.content is null at times, probably because the
				 * widget itself has been disposed.
				 */
				if (isDisposed()) return;
				if (autoScrollDirection == ST.COLUMN_PREVIOUS) {
					if (blockSelection) {
						int x = blockXLocation - horizontalScrollOffset;
						int pixels = Math.max(-autoScrollDistance, -horizontalScrollOffset);
						if (pixels != 0) {
							setBlockSelectionLocation(x + pixels, blockYLocation - getVerticalScrollOffset(), true);
							scrollHorizontal(pixels, true);
						}
					} else {
						doVisualPrevious();
						setMouseWordSelectionAnchor();
						doMouseSelection();
					}
					display.timerExec(H_SCROLL_RATE, this);
				}
			}
		};
		autoScrollDirection = direction;
		display.timerExec(H_SCROLL_RATE, timer);
	}
}
/**
 * Deletes the previous character. Delete the selected text if any.
 * Move the caret in front of the deleted text.
 */
void doBackspace() {
	Event event = new Event();
	event.text = "";
	if (Arrays.stream(selection).anyMatch(p -> p.x != p.y)) { // at least one range to delete
		for (int i = selection.length - 1; i >= 0; i--) { // from bottom to top to avoid moving ranges
			Point sel = selection[i];
			event.start = sel.x;
			event.end = sel.y;
			sendKeyEvent(event);
		}
	} else {
		for (int i = caretOffsets.length - 1; i >= 0; i--) { // reverse loop, process from bottom to top
			int caretOffset = caretOffsets[i];
			if (caretOffset > 0) {
				int lineIndex = content.getLineAtOffset(caretOffset);
				int lineOffset = content.getOffsetAtLine(lineIndex);
				if (caretOffset == lineOffset) {
					lineOffset = content.getOffsetAtLine(lineIndex - 1);
					event.start = lineOffset + content.getLine(lineIndex - 1).length();
					event.end = caretOffset;
				} else {
					boolean isSurrogate = false;
					String lineText = content.getLine(lineIndex);
					char ch = lineText.charAt(caretOffset - lineOffset - 1);
					if (0xDC00 <= ch && ch <= 0xDFFF) {
						if (caretOffset - lineOffset - 2 >= 0) {
							ch = lineText.charAt(caretOffset - lineOffset - 2);
							isSurrogate = 0xD800 <= ch && ch <= 0xDBFF;
						}
					}
					TextLayout layout = renderer.getTextLayout(lineIndex);
					int start = layout.getPreviousOffset(caretOffset - lineOffset, isSurrogate ? SWT.MOVEMENT_CLUSTER : SWT.MOVEMENT_CHAR);
					renderer.disposeTextLayout(layout);
					event.start = start + lineOffset;
					event.end = caretOffset;
				}
				sendKeyEvent(event);
			}
		}
	}
}
void doBlockColumn(boolean next) {
	if (blockXLocation == -1) setBlockSelectionOffset(caretOffsets[0], false);
	int x = blockXLocation - horizontalScrollOffset;
	int y = blockYLocation - getVerticalScrollOffset();
	int[] trailing = new int[1];
	int offset = getOffsetAtPoint(x, y, trailing, true);
	if (offset != -1) {
		offset += trailing[0];
		int lineIndex = content.getLineAtOffset(offset);
		int newOffset;
		if (next) {
			newOffset = getClusterNext(offset, lineIndex);
		} else {
			newOffset = getClusterPrevious(offset, lineIndex);
		}
		offset = newOffset != offset ? newOffset : -1;
	}
	if (offset != -1) {
		setBlockSelectionOffset(offset, true);
		showCaret();
	} else {
		int width = next ? renderer.averageCharWidth : -renderer.averageCharWidth;
		int maxWidth = Math.max(clientAreaWidth - rightMargin - leftMargin, renderer.getWidth());
		x = Math.max(0, Math.min(blockXLocation + width, maxWidth)) - horizontalScrollOffset;
		setBlockSelectionLocation(x, y, true);
		Rectangle rect = new Rectangle(x, y, 0, 0);
		showLocation(rect, true);
	}
}
void doBlockContentStartEnd(boolean end) {
	if (blockXLocation == -1) setBlockSelectionOffset(caretOffsets[0], false);
	int offset = end ? content.getCharCount() : 0;
	setBlockSelectionOffset(offset, true);
	showCaret();
}
void doBlockWord(boolean next) {
	if (blockXLocation == -1) setBlockSelectionOffset(caretOffsets[0], false);
	int x = blockXLocation - horizontalScrollOffset;
	int y = blockYLocation - getVerticalScrollOffset();
	int[] trailing = new int[1];
	int offset = getOffsetAtPoint(x, y, trailing, true);
	if (offset != -1) {
		offset += trailing[0];
		int lineIndex = content.getLineAtOffset(offset);
		int lineOffset = content.getOffsetAtLine(lineIndex);
		String lineText = content.getLine(lineIndex);
		int lineLength = lineText.length();
		int newOffset = offset;
		if (next) {
			if (offset < lineOffset + lineLength) {
				newOffset = getWordNext(offset, SWT.MOVEMENT_WORD);
			}
		} else {
			if (offset > lineOffset) {
				newOffset = getWordPrevious(offset, SWT.MOVEMENT_WORD);
			}
		}
		offset = newOffset != offset ? newOffset : -1;
	}
	if (offset != -1) {
		setBlockSelectionOffset(offset, true);
		showCaret();
	} else {
		int width = (next ? renderer.averageCharWidth : -renderer.averageCharWidth) * 6;
		int maxWidth = Math.max(clientAreaWidth - rightMargin - leftMargin, renderer.getWidth());
		x = Math.max(0, Math.min(blockXLocation + width, maxWidth)) - horizontalScrollOffset;
		setBlockSelectionLocation(x, y, true);
		Rectangle rect = new Rectangle(x, y, 0, 0);
		showLocation(rect, true);
	}
}
void doBlockLineVertical(boolean up) {
	if (blockXLocation == -1) setBlockSelectionOffset(caretOffsets[0], false);
	int y = blockYLocation - getVerticalScrollOffset();
	int lineIndex = getLineIndex(y);
	if (up) {
		if (lineIndex > 0) {
			y = getLinePixel(lineIndex - 1);
			setBlockSelectionLocation(blockXLocation - horizontalScrollOffset, y, true);
			if (y < topMargin) {
				scrollVertical(y - topMargin, true);
			}
		}
	} else {
		int lineCount = content.getLineCount();
		if (lineIndex + 1 < lineCount) {
			y = getLinePixel(lineIndex + 2) - 1;
			setBlockSelectionLocation(blockXLocation - horizontalScrollOffset, y, true);
			int bottom = clientAreaHeight - bottomMargin;
			if (y > bottom) {
				scrollVertical(y - bottom, true);
			}
		}
	}
}
void doBlockLineHorizontal(boolean end) {
	if (blockXLocation == -1) setBlockSelectionOffset(caretOffsets[0], false);
	int x = blockXLocation - horizontalScrollOffset;
	int y = blockYLocation - getVerticalScrollOffset();
	int lineIndex = getLineIndex(y);
	int lineOffset = content.getOffsetAtLine(lineIndex);
	String lineText = content.getLine(lineIndex);
	int lineLength = lineText.length();
	int[] trailing = new int[1];
	int offset = getOffsetAtPoint(x, y, trailing, true);
	if (offset != -1) {
		offset += trailing[0];
		int newOffset = offset;
		if (end) {
			if (offset < lineOffset + lineLength) {
				newOffset = lineOffset + lineLength;
			}
		} else {
			if (offset > lineOffset) {
				newOffset = lineOffset;
			}
		}
		offset = newOffset != offset ? newOffset : -1;
	} else {
		if (!end) offset = lineOffset + lineLength;
	}
	if (offset != -1) {
		setBlockSelectionOffset(offset, true);
		showCaret();
	} else {
		int maxWidth = Math.max(clientAreaWidth - rightMargin - leftMargin, renderer.getWidth());
		x = (end ? maxWidth : 0) - horizontalScrollOffset;
		setBlockSelectionLocation(x, y, true);
		Rectangle rect = new Rectangle(x, y, 0, 0);
		showLocation(rect, true);
	}
}
void doBlockSelection(boolean sendEvent) {
	if (caretOffsets[0] > selectionAnchors[0]) {
		selection[0].x = selectionAnchors[0];
		selection[0].y = caretOffsets[0];
	} else {
		selection[0].x = caretOffsets[0];
		selection[0].y = selectionAnchors[0];
	}
	updateCaretVisibility();
	setCaretLocations();
	super.redraw();
	if (sendEvent) {
		sendSelectionEvent();
	}
	sendAccessibleTextCaretMoved();
}
/**
 * Replaces the selection with the character or insert the character at the
 * current caret position if no selection exists.
 * <p>
 * If a carriage return was typed replace it with the line break character
 * used by the widget on this platform.
 * </p>
 *
 * @param key the character typed by the user
 */
void doContent(final char key) {
	if (blockSelection && blockXLocation != -1) {
		insertBlockSelectionText(key, SWT.NULL);
		return;
	}

	for (int i = selection.length - 1; i >= 0; i--) {
		Point sel = selection[i];
		Event event = new Event();
		event.start = sel.x;
		event.end = sel.y;
		// replace a CR line break with the widget line break
		// CR does not make sense on Windows since most (all?) applications
		// don't recognize CR as a line break.
		if (key == SWT.CR || key == SWT.LF) {
			if (!isSingleLine()) {
				event.text = getLineDelimiter();
			}
		} else if (sel.x == sel.y && overwrite && key != TAB) {
			// no selection and overwrite mode is on and the typed key is not a
			// tab character (tabs are always inserted without overwriting)?
			int lineIndex = content.getLineAtOffset(event.end);
			int lineOffset = content.getOffsetAtLine(lineIndex);
			String line = content.getLine(lineIndex);
			// replace character at caret offset if the caret is not at the
			// end of the line
			if (event.end < lineOffset + line.length()) {
				event.end++;
			}
			event.text = new String(new char[] {key});
		} else {
			event.text = new String(new char[] {key});
		}
		if (event.text != null) {
			if (textLimit > 0 && content.getCharCount() - (event.end - event.start) >= textLimit) {
				return;
			}
			sendKeyEvent(event);
		}
	}
}
/**
 * Moves the caret after the last character of the widget content.
 */
void doContentEnd() {
	// place caret at end of first line if receiver is in single
	// line mode. fixes 4820.
	if (isSingleLine()) {
		doLineEnd();
	} else {
		int length = content.getCharCount();
		setCaretOffsets(new int[] {length}, SWT.DEFAULT);
		showCaret();
	}
}
/**
 * Moves the caret in front of the first character of the widget content.
 */
void doContentStart() {
	setCaretOffsets(new int[] {0}, SWT.DEFAULT);
	showCaret();
}
/**
 * Moves the caret to the start of the selection if a selection exists.
 * Otherwise, if no selection exists move the cursor according to the
 * cursor selection rules.
 *
 * @see #doSelectionCursorPrevious
 */
void doCursorPrevious() {
	if (Arrays.stream(selection).anyMatch(p -> p.x != p.y)) {
		setCaretOffsets(Arrays.stream(selection).mapToInt(sel -> sel.x).toArray(), OFFSET_LEADING);
		showCaret();
	} else {
		doSelectionCursorPrevious();
	}
}
/**
 * Moves the caret to the end of the selection if a selection exists.
 * Otherwise, if no selection exists move the cursor according to the
 * cursor selection rules.
 *
 * @see #doSelectionCursorNext
 */
void doCursorNext() {
	if (Arrays.stream(selection).anyMatch(p -> p.x != p.y)) {
		setCaretOffsets(Arrays.stream(selection).mapToInt(sel -> sel.y).toArray(), PREVIOUS_OFFSET_TRAILING);
		showCaret();
	} else {
		doSelectionCursorNext();
	}
}
/**
 * Deletes the next character. Delete the selected text if any.
 */
void doDelete() {
	Event event = new Event();
	event.text = "";
	if (Arrays.stream(selection).anyMatch(sel -> sel.x != sel.y)) {
		for (Point sel : selection) {
			event.start = sel.x;
			event.end = sel.y;
			sendKeyEvent(event);
		}
	} else {
		for (int i = caretOffsets.length - 1; i >= 0; i--) {
			int caretOffset = caretOffsets[i];
			if (caretOffset < content.getCharCount()) {
				int line = content.getLineAtOffset(caretOffset);
				int lineOffset = content.getOffsetAtLine(line);
				int lineLength = content.getLine(line).length();
				if (caretOffset == lineOffset + lineLength) {
					event.start = caretOffset;
					event.end = content.getOffsetAtLine(line + 1);
				} else {
					event.start = caretOffset;
					event.end = getClusterNext(caretOffset, line);
				}
				sendKeyEvent(event);
			}
		}
	}
}
/**
 * Deletes the next word.
 */
void doDeleteWordNext() {
	if (Arrays.stream(selection).anyMatch(sel -> sel.x != sel.y)) {
		// if a selection exists, treat the as if
		// only the delete key was pressed
		doDelete();
	} else {
		for (int i = caretOffsets.length - 1; i >= 0; i--) {
			int caretOffset = caretOffsets[i];
			Event event = new Event();
			event.text = "";
			event.start = caretOffset;
			event.end = getWordNext(caretOffset, SWT.MOVEMENT_WORD);
			sendKeyEvent(event);
		}
	}
}
/**
 * Deletes the previous word.
 */
void doDeleteWordPrevious() {
	if (Arrays.stream(selection).anyMatch(sel -> sel.x != sel.y)) {
		// if a selection exists, treat as if
		// only the backspace key was pressed
		doBackspace();
	} else {
		for (int i = caretOffsets.length - 1; i >= 0; i--) {
			int caretOffset = caretOffsets[i];
			Event event = new Event();
			event.text = "";
			event.start = getWordPrevious(caretOffset, SWT.MOVEMENT_WORD);
			event.end = caretOffset;
			sendKeyEvent(event);
		}
	}
}
/**
 * Moves the caret one line down and to the same character offset relative
 * to the beginning of the line. Move the caret to the end of the new line
 * if the new line is shorter than the character offset. Moves the caret to
 * the end of the text if the caret already is on the last line.
 */
void doLineDown(boolean select) {
	int[] newCaretOffsets = new int[caretOffsets.length];
	int lineCount = content.getLineCount();
	int[] alignment = new int[1];
	for (int i = 0; i < caretOffsets.length; i++) {
		int caretOffset = caretOffsets[i];
		int caretLine = content.getLineAtOffset(caretOffset);
		int x = caretOffsets.length == 1 ? columnX : getPointAtOffset(caretOffset).x;
		int y = 0;
		boolean lastLine = false;
		if (isWordWrap()) {
			int lineOffset = content.getOffsetAtLine(caretLine);
			int offsetInLine = caretOffset - lineOffset;
			TextLayout layout = renderer.getTextLayout(caretLine);
			int lineIndex = getVisualLineIndex(layout, offsetInLine);
			int layoutLineCount = layout.getLineCount();
			if (lineIndex == layoutLineCount - 1) {
				lastLine = caretLine == lineCount - 1;
				caretLine++;
			} else {
				y = layout.getLineBounds(lineIndex + 1).y;
				y++; // bug 485722: workaround for fractional line heights
			}
			renderer.disposeTextLayout(layout);
		} else {
			lastLine = caretLine == lineCount - 1;
			caretLine++;
		}
		if (lastLine) {
			newCaretOffsets[i] = content.getCharCount();
		} else {
			newCaretOffsets[i] = getOffsetAtPoint(x, y, caretLine, alignment);
		}
	}
	boolean hitLastLine = content.getLineAtOffset(newCaretOffsets[newCaretOffsets.length - 1]) == lineCount - 1;
	setCaretOffsets(newCaretOffsets, hitLastLine ? SWT.DEFAULT : alignment[0]);
	int oldColumnX = columnX;
	int oldHScrollOffset = horizontalScrollOffset;
	if (select) {
		setMouseWordSelectionAnchor();
		// select first and then scroll to reduce flash when key
		// repeat scrolls lots of lines
		doSelection(ST.COLUMN_NEXT);
	}
	showCaret();
	int hScrollChange = oldHScrollOffset - horizontalScrollOffset;
	columnX = oldColumnX + hScrollChange;
}
/**
 * Moves the caret to the end of the line.
 */
void doLineEnd() {
	int[] newOffsets = new int[caretOffsets.length];
	for (int i = 0; i < caretOffsets.length; i++) {
		int caretOffset = caretOffsets[i];
		int caretLine = content.getLineAtOffset(caretOffset);
		int lineOffset = content.getOffsetAtLine(caretLine);
		int lineEndOffset;
		if (isWordWrap()) {
			TextLayout layout = renderer.getTextLayout(caretLine);
			int offsetInLine = caretOffset - lineOffset;
			int lineIndex = getVisualLineIndex(layout, offsetInLine);
			int[] offsets = layout.getLineOffsets();
			lineEndOffset = lineOffset + offsets[lineIndex + 1];
			renderer.disposeTextLayout(layout);
		} else {
			int lineLength = content.getLine(caretLine).length();
			lineEndOffset = lineOffset + lineLength;
		}
		newOffsets[i] = lineEndOffset;
	}
	setCaretOffsets(newOffsets, PREVIOUS_OFFSET_TRAILING);
	showCaret();
}
/**
 * Moves the caret to the beginning of the line.
 */
void doLineStart() {
	int[] newCaretOffsets = new int[caretOffsets.length];
	for (int i = 0; i < caretOffsets.length; i++) {
		int caretOffset = caretOffsets[i];
		int caretLine = content.getLineAtOffset(caretOffset);
		int lineOffset = content.getOffsetAtLine(caretLine);
		if (isWordWrap()) {
			TextLayout layout = renderer.getTextLayout(caretLine);
			int offsetInLine = caretOffset - lineOffset;
			int lineIndex = getVisualLineIndex(layout, offsetInLine);
			int[] offsets = layout.getLineOffsets();
			lineOffset += offsets[lineIndex];
			renderer.disposeTextLayout(layout);
		}
		newCaretOffsets[i] = lineOffset;
	}
	setCaretOffsets(newCaretOffsets, OFFSET_LEADING);
	showCaret();
}
/**
 * Moves the caret one line up and to the same character offset relative
 * to the beginning of the line. Move the caret to the end of the new line
 * if the new line is shorter than the character offset. Moves the caret to
 * the beginning of the document if it is already on the first line.
 */
void doLineUp(boolean select) {
	int[] newCaretOffsets = new int[caretOffsets.length];
	int[] alignment = new int[1];
	for (int i = 0; i < caretOffsets.length; i++) {
		int caretOffset = caretOffsets[i];
		int caretLine = content.getLineAtOffset(caretOffset);
		int x = caretOffsets.length == 1 ? columnX : getPointAtOffset(caretOffset).x;
		int y = 0;
		boolean firstLine = false;
		if (isWordWrap()) {
			int lineOffset = content.getOffsetAtLine(caretLine);
			int offsetInLine = caretOffset - lineOffset;
			TextLayout layout = renderer.getTextLayout(caretLine);
			int lineIndex = getVisualLineIndex(layout, offsetInLine);
			if (lineIndex == 0) {
				firstLine = caretLine == 0;
				if (!firstLine) {
					caretLine--;
					y = renderer.getLineHeight(caretLine) - 1;
					y--; // bug 485722: workaround for fractional line heights
				}
			} else {
				y = layout.getLineBounds(lineIndex - 1).y;
				y++; // bug 485722: workaround for fractional line heights
			}
			renderer.disposeTextLayout(layout);
		} else {
			firstLine = caretLine == 0;
			caretLine--;
		}
		if (firstLine) {
			newCaretOffsets[i] = 0;
		} else {
			newCaretOffsets[i] = getOffsetAtPoint(x, y, caretLine, alignment);
		}
	}
	setCaretOffsets(newCaretOffsets, newCaretOffsets[0] == 0 ? SWT.DEFAULT : alignment[0]);
	int oldColumnX = columnX;
	int oldHScrollOffset = horizontalScrollOffset;
	if (select) setMouseWordSelectionAnchor();
	showCaret();
	if (select) doSelection(ST.COLUMN_PREVIOUS);
	int hScrollChange = oldHScrollOffset - horizontalScrollOffset;
	columnX = oldColumnX + hScrollChange;
}
void doMouseLinkCursor() {
	Display display = getDisplay();
	Point point = display.getCursorLocation();
	point = display.map(null, this, point);
	doMouseLinkCursor(point.x, point.y);
}
void doMouseLinkCursor(int x, int y) {
	int offset = getOffsetAtPoint(x, y, null, true);
	Display display = getDisplay();
	Cursor newCursor = cursor;
	if (renderer.hasLink(offset)) {
		newCursor = display.getSystemCursor(SWT.CURSOR_HAND);
	} else {
		if (cursor == null) {
			int type = blockSelection ? SWT.CURSOR_CROSS : SWT.CURSOR_IBEAM;
			newCursor = display.getSystemCursor(type);
		}
	}
	if (newCursor != getCursor()) super.setCursor(newCursor);
}
/**
 * Moves the caret to the specified location.
 *
 * @param x x location of the new caret position
 * @param y y location of the new caret position
 * @param select the location change is a selection operation.
 * 	include the line delimiter in the selection
 */
void doMouseLocationChange(int x, int y, boolean select) {
	int line = getLineIndex(y);

	updateCaretDirection = true;

	if (blockSelection) {
		x = Math.max(leftMargin, Math.min(x, clientAreaWidth - rightMargin));
		y = Math.max(topMargin, Math.min(y, clientAreaHeight - bottomMargin));
		if (doubleClickEnabled && clickCount > 1) {
			boolean wordSelect = (clickCount & 1) == 0;
			if (wordSelect) {
				Point left = getPointAtOffset(doubleClickSelection.x);
				int[] trailing = new int[1];
				int offset = getOffsetAtPoint(x, y, trailing, true);
				if (offset != -1) {
					if (x > left.x) {
						offset = getWordNext(offset + trailing[0], SWT.MOVEMENT_WORD_END);
						setBlockSelectionOffset(doubleClickSelection.x, offset, true);
					} else {
						offset = getWordPrevious(offset + trailing[0], SWT.MOVEMENT_WORD_START);
						setBlockSelectionOffset(doubleClickSelection.y, offset, true);
					}
				} else {
					if (x > left.x) {
						setBlockSelectionLocation(left.x, left.y, x, y, true);
					} else {
						Point right = getPointAtOffset(doubleClickSelection.y);
						setBlockSelectionLocation(right.x, right.y, x, y, true);
					}
				}
			} else {
				setBlockSelectionLocation(blockXLocation, y, true);
			}
			return;
		} else {
			if (select) {
				if (blockXLocation == -1) {
					setBlockSelectionOffset(caretOffsets[0], false);
				}
			} else {
				clearBlockSelection(true, false);
			}
			int[] trailing = new int[1];
			int offset = getOffsetAtPoint(x, y, trailing, true);
			if (offset != -1) {
				if (select) {
					setBlockSelectionOffset(offset + trailing[0], true);
					return;
				}
			} else {
				if (isFixedLineHeight() && renderer.fixedPitch) {
					int avg = renderer.averageCharWidth;
					x = ((x + avg / 2 - leftMargin + horizontalScrollOffset) / avg * avg) + leftMargin - horizontalScrollOffset;
				}
				setBlockSelectionLocation(x, y, true);
				return;
			}
		}
	}

	// allow caret to be placed below first line only if receiver is
	// not in single line mode. fixes 4820.
	if (line < 0 || (isSingleLine() && line > 0)) {
		return;
	}
	int[] alignment = new int[1];
	int newCaretOffset = getOffsetAtPoint(x, y, alignment);
	int newCaretAlignemnt = alignment[0];

	if (doubleClickEnabled && clickCount > 1) {
		newCaretOffset = doMouseWordSelect(x, newCaretOffset, line);
	}

	int newCaretLine = content.getLineAtOffset(newCaretOffset);

	// Is the mouse within the left client area border or on
	// a different line? If not the autoscroll selection
	// could be incorrectly reset. Fixes 1GKM3XS
	boolean vchange = 0 <= y && y < clientAreaHeight || newCaretLine == 0 || newCaretLine == content.getLineCount() - 1;
	boolean hchange = 0 <= x && x < clientAreaWidth || wordWrap || newCaretLine != content.getLineAtOffset(caretOffsets[0]);
	if (vchange && hchange && (newCaretOffset != caretOffsets[0] || newCaretAlignemnt != caretAlignment)) {
		setCaretOffsets(new int[] {newCaretOffset}, newCaretAlignemnt);
		if (select) doMouseSelection();
		showCaret();
	}
	if (!select) {
		setCaretOffsets(new int[] {newCaretOffset}, newCaretAlignemnt);
		clearSelection(true);
	}
}
/**
 * Updates the selection based on the caret position
 */
void doMouseSelection() {
	if (caretOffsets[0] <= selection[0].x ||
		(caretOffsets[0] > selection[0].x &&
		 caretOffsets[0] < selection[0].y && selectionAnchors[0] == selection[0].x)) {
		doSelection(ST.COLUMN_PREVIOUS);
	} else {
		doSelection(ST.COLUMN_NEXT);
	}
}
/**
 * Returns the offset of the word at the specified offset.
 * If the current selection extends from high index to low index
 * (i.e., right to left, or caret is at left border of selection on
 * non-bidi platforms) the start offset of the word preceding the
 * selection is returned. If the current selection extends from
 * low index to high index the end offset of the word following
 * the selection is returned.
 *
 * @param x mouse x location
 * @param newCaretOffset caret offset of the mouse cursor location
 * @param line line index of the mouse cursor location
 */
int doMouseWordSelect(int x, int newCaretOffset, int line) {
	// flip selection anchor based on word selection direction from
	// base double click. Always do this here (and don't rely on doAutoScroll)
	// because auto scroll only does not cover all possible mouse selections
	// (e.g., mouse x < 0 && mouse y > caret line y)
	if (newCaretOffset < selectionAnchors[0] && selectionAnchors[0] == selection[0].x) {
		selectionAnchors[0] = doubleClickSelection.y;
	} else if (newCaretOffset > selectionAnchors[0] && selectionAnchors[0] == selection[0].y) {
		selectionAnchors[0] = doubleClickSelection.x;
	}
	if (0 <= x && x < clientAreaWidth) {
		boolean wordSelect = (clickCount & 1) == 0;
		if (caretOffsets[0] == selection[0].x) {
			if (wordSelect) {
				newCaretOffset = getWordPrevious(newCaretOffset, SWT.MOVEMENT_WORD_START);
			} else {
				newCaretOffset = content.getOffsetAtLine(line);
			}
		} else {
			if (wordSelect) {
				newCaretOffset = getWordNext(newCaretOffset, SWT.MOVEMENT_WORD_END);
			} else {
				int lineEnd = content.getCharCount();
				if (line + 1 < content.getLineCount()) {
					lineEnd = content.getOffsetAtLine(line + 1);
				}
				newCaretOffset = lineEnd;
			}
		}
	}
	return newCaretOffset;
}
/**
 * Scrolls one page down so that the last line (truncated or whole)
 * of the current page becomes the fully visible top line.
 * <p>
 * The caret is scrolled the same number of lines so that its location
 * relative to the top line remains the same. The exception is the end
 * of the text where a full page scroll is not possible. In this case
 * the caret is moved after the last character.
 * </p>
 *
 * @param select whether or not to select the page
 */
void doPageDown(boolean select, int height) {
	if (isSingleLine()) return;
	int oldColumnX = columnX;
	int oldHScrollOffset = horizontalScrollOffset;
	if (isFixedLineHeight()) {
		int lineCount = content.getLineCount();
		int caretLine = getFirstCaretLine();
		if (caretLine < lineCount - 1) {
			int lineHeight = renderer.getLineHeight();
			int lines = (height == -1 ? clientAreaHeight : height) / lineHeight;
			int scrollLines = Math.min(lineCount - caretLine - 1, lines);
			// ensure that scrollLines never gets negative and at least one
			// line is scrolled. fixes bug 5602.
			scrollLines = Math.max(1, scrollLines);
			int[] alignment = new int[1];
			int offset = getOffsetAtPoint(columnX, getLinePixel(caretLine + scrollLines), alignment);
			setCaretOffsets(new int[] {offset}, alignment[0]);
			if (select) {
				doSelection(ST.COLUMN_NEXT);
			}
			// scroll one page down or to the bottom
			int verticalMaximum = lineCount * getVerticalIncrement();
			int pageSize = clientAreaHeight;
			int verticalScrollOffset = getVerticalScrollOffset();
			int scrollOffset = verticalScrollOffset + scrollLines * getVerticalIncrement();
			if (scrollOffset + pageSize > verticalMaximum) {
				scrollOffset = verticalMaximum - pageSize;
			}
			if (scrollOffset > verticalScrollOffset) {
				scrollVertical(scrollOffset - verticalScrollOffset, true);
			}
		}
	} else {
		int lineCount = content.getLineCount();
		int lineIndex, lineHeight;
		if (height == -1) {
			lineIndex = getPartialBottomIndex();
			int topY = getLinePixel(lineIndex);
			lineHeight = renderer.getLineHeight(lineIndex);
			height = topY;
			if (topY + lineHeight <= clientAreaHeight) {
				height += lineHeight;
			} else {
				if (isWordWrap()) {
					TextLayout layout = renderer.getTextLayout(lineIndex);
					int y = clientAreaHeight - topY;
					for (int i = 0; i < layout.getLineCount(); i++) {
						Rectangle bounds = layout.getLineBounds(i);
						if (bounds.contains(bounds.x, y)) {
							height += bounds.y;
							break;
						}
					}
					renderer.disposeTextLayout(layout);
				}
			}
		} else {
			lineIndex = getLineIndex(height);
			int topLineY = getLinePixel(lineIndex);
			if (isWordWrap()) {
				TextLayout layout = renderer.getTextLayout(lineIndex);
				int y = height - topLineY;
				for (int i = 0; i < layout.getLineCount(); i++) {
					Rectangle bounds = layout.getLineBounds(i);
					if (bounds.contains(bounds.x, y)) {
						height = topLineY + bounds.y + bounds.height;
						break;
					}
				}
				renderer.disposeTextLayout(layout);
			} else {
				height = topLineY + renderer.getLineHeight(lineIndex);
			}
		}
		int caretHeight = height;
		if (isWordWrap()) {
			for (int caretOffset : caretOffsets) {
				int caretLine = content.getLineAtOffset(caretOffset);
				TextLayout layout = renderer.getTextLayout(caretLine);
				int offsetInLine = caretOffset - content.getOffsetAtLine(caretLine);
				lineIndex = getVisualLineIndex(layout, offsetInLine);
				caretHeight += layout.getLineBounds(lineIndex).y;
				renderer.disposeTextLayout(layout);
			}
		}
		lineIndex = getFirstCaretLine();
		lineHeight = renderer.getLineHeight(lineIndex);
		while (caretHeight - lineHeight >= 0 && lineIndex < lineCount - 1) {
			caretHeight -= lineHeight;
			lineHeight = renderer.getLineHeight(++lineIndex);
		}
		int[] alignment = new int[1];
		int offset = getOffsetAtPoint(columnX, caretHeight, lineIndex, alignment);
		setCaretOffsets(new int[] {offset}, alignment[0]);
		if (select) doSelection(ST.COLUMN_NEXT);
		height = getAvailableHeightBellow(height);
		scrollVertical(height, true);
		if (height == 0) setCaretLocations();
	}
	showCaret();
	int hScrollChange = oldHScrollOffset - horizontalScrollOffset;
	columnX = oldColumnX + hScrollChange;
}
/**
 * Moves the cursor to the end of the last fully visible line.
 */
void doPageEnd() {
	// go to end of line if in single line mode. fixes 5673
	if (isSingleLine()) {
		doLineEnd();
	} else if (caretOffsets.length == 1) { // pageEnd doesn't make sense with multi-carets
		int bottomOffset;
		if (isWordWrap()) {
			int lineIndex = getPartialBottomIndex();
			TextLayout layout = renderer.getTextLayout(lineIndex);
			int y = (clientAreaHeight - bottomMargin) - getLinePixel(lineIndex);
			int index = layout.getLineCount() - 1;
			while (index >= 0) {
				Rectangle bounds = layout.getLineBounds(index);
				if (y >= bounds.y + bounds.height) break;
				index--;
			}
			if (index == -1 && lineIndex > 0) {
				bottomOffset = content.getOffsetAtLine(lineIndex - 1) + content.getLine(lineIndex - 1).length();
			} else {
				bottomOffset = content.getOffsetAtLine(lineIndex) + Math.max(0, layout.getLineOffsets()[index + 1] - 1);
			}
			renderer.disposeTextLayout(layout);
		} else {
			int lineIndex = getBottomIndex();
			bottomOffset = content.getOffsetAtLine(lineIndex) + content.getLine(lineIndex).length();
		}
		if (caretOffsets[0] < bottomOffset) {
			setCaretOffsets(new int[] {bottomOffset}, OFFSET_LEADING);
			showCaret();
		}
	}
}
/**
 * Moves the cursor to the beginning of the first fully visible line.
 */
void doPageStart() {
	int topOffset;
	if (isWordWrap()) {
		int y, lineIndex;
		if (topIndexY > 0) {
			lineIndex = topIndex - 1;
			y = renderer.getLineHeight(lineIndex) - topIndexY;
		} else {
			lineIndex = topIndex;
			y = -topIndexY;
		}
		TextLayout layout = renderer.getTextLayout(lineIndex);
		int index = 0;
		int lineCount = layout.getLineCount();
		while (index < lineCount) {
			Rectangle bounds = layout.getLineBounds(index);
			if (y <= bounds.y) break;
			index++;
		}
		if (index == lineCount) {
			topOffset = content.getOffsetAtLine(lineIndex + 1);
		} else {
			topOffset = content.getOffsetAtLine(lineIndex) + layout.getLineOffsets()[index];
		}
		renderer.disposeTextLayout(layout);
	} else {
		topOffset = content.getOffsetAtLine(topIndex);
	}
	if (caretOffsets[0] > topOffset) {
		setCaretOffsets(new int[] {topOffset}, OFFSET_LEADING);
		showCaret();
	}
}
/**
 * Scrolls one page up so that the first line (truncated or whole)
 * of the current page becomes the fully visible last line.
 * The caret is scrolled the same number of lines so that its location
 * relative to the top line remains the same. The exception is the beginning
 * of the text where a full page scroll is not possible. In this case the
 * caret is moved in front of the first character.
 */
void doPageUp(boolean select, int height) {
	if (isSingleLine()) return;
	int oldHScrollOffset = horizontalScrollOffset;
	int oldColumnX = columnX;
	if (isFixedLineHeight()) {
		int caretLine = getFirstCaretLine();
		if (caretLine > 0) {
			int lineHeight = renderer.getLineHeight();
			int lines = (height == -1 ? clientAreaHeight : height) / lineHeight;
			int scrollLines = Math.max(1, Math.min(caretLine, lines));
			caretLine -= scrollLines;
			int[] alignment = new int[1];
			int offset = getOffsetAtPoint(columnX, getLinePixel(caretLine), alignment);
			setCaretOffsets(new int[] {offset}, alignment[0]);
			if (select) {
				doSelection(ST.COLUMN_PREVIOUS);
			}
			int verticalScrollOffset = getVerticalScrollOffset();
			int scrollOffset = Math.max(0, verticalScrollOffset - scrollLines * getVerticalIncrement());
			if (scrollOffset < verticalScrollOffset) {
				scrollVertical(scrollOffset - verticalScrollOffset, true);
			}
		}
	} else {
		int lineHeight, lineIndex;
		if (height == -1) {
			if (topIndexY == 0) {
				height = clientAreaHeight;
			} else {
				int y;
				if (topIndex > 0) {
					lineIndex = topIndex - 1;
					lineHeight = renderer.getLineHeight(lineIndex);
					height = clientAreaHeight - topIndexY;
					y = lineHeight - topIndexY;
				} else {
					lineIndex = topIndex;
					lineHeight = renderer.getLineHeight(lineIndex);
					height = clientAreaHeight - (lineHeight + topIndexY);
					y = -topIndexY;
				}
				if (isWordWrap()) {
					TextLayout layout = renderer.getTextLayout(lineIndex);
					for (int i = 0; i < layout.getLineCount(); i++) {
						Rectangle bounds = layout.getLineBounds(i);
						if (bounds.contains(bounds.x, y)) {
							height += lineHeight - (bounds.y + bounds.height);
							break;
						}
					}
					renderer.disposeTextLayout(layout);
				}
			}
		} else {
			lineIndex = getLineIndex(clientAreaHeight - height);
			int topLineY = getLinePixel(lineIndex);
			if (isWordWrap()) {
				TextLayout layout = renderer.getTextLayout(lineIndex);
				int y = topLineY;
				for (int i = 0; i < layout.getLineCount(); i++) {
					Rectangle bounds = layout.getLineBounds(i);
					if (bounds.contains(bounds.x, y)) {
						height = clientAreaHeight - (topLineY + bounds.y);
						break;
					}
				}
				renderer.disposeTextLayout(layout);
			} else {
				height = clientAreaHeight - topLineY;
			}
		}
		int caretHeight = height;
		if (isWordWrap()) {
			for (int caretOffset : caretOffsets) {
				int caretLine = content.getLineAtOffset(caretOffset);
				TextLayout layout = renderer.getTextLayout(caretLine);
				int offsetInLine = caretOffset - content.getOffsetAtLine(caretLine);
				lineIndex = getVisualLineIndex(layout, offsetInLine);
				caretHeight += layout.getBounds().height - layout.getLineBounds(lineIndex).y;
				renderer.disposeTextLayout(layout);
			}
		}
		lineIndex = getFirstCaretLine();
		lineHeight = renderer.getLineHeight(lineIndex);
		while (caretHeight - lineHeight >= 0 && lineIndex > 0) {
			caretHeight -= lineHeight;
			lineHeight = renderer.getLineHeight(--lineIndex);
		}
		lineHeight = renderer.getLineHeight(lineIndex);
		int[] alignment = new int[1];
		int offset = getOffsetAtPoint(columnX, lineHeight - caretHeight, lineIndex, alignment);
		setCaretOffsets(new int[] {offset}, alignment[0]);
		if (select) doSelection(ST.COLUMN_PREVIOUS);
		height = getAvailableHeightAbove(height);
		scrollVertical(-height, true);
		if (height == 0) setCaretLocations();
	}
	showCaret();
	int hScrollChange = oldHScrollOffset - horizontalScrollOffset;
	columnX = oldColumnX + hScrollChange;
}
/**
 * Updates the selection to extend to the current caret position.
 */
void doSelection(int direction) {
	if (caretOffsets.length != selection.length) {
		return;
	}
	if (selectionAnchors.length != selection.length) {
		selectionAnchors = new int[selection.length];
		Arrays.fill(selectionAnchors, -1);
	}
	boolean selectionChanged = false;
	Point[] newSelection = Arrays.stream(selection).map(p -> new Point(p.x, p.y)).toArray(Point[]::new);
	boolean[] caretAtBeginning = new boolean[newSelection.length];
	for (int i = 0; i < caretOffsets.length; i++) {
		int caretOffset = caretOffsets[i];
		Point currentSelection = newSelection[i];
		int selectionAnchor = selectionAnchors[i];
		if (selectionAnchor == -1) {
			selectionAnchor = selectionAnchors[i] = currentSelection.x;
		}
		int redrawStart = -1;
		int redrawEnd = -1;
		if (direction == ST.COLUMN_PREVIOUS) {
			if (caretOffset < currentSelection.x) {
				caretAtBeginning[i] = true;
				// grow selection
				redrawEnd = currentSelection.x;
				redrawStart = currentSelection.x = caretOffset;
				// check if selection has reversed direction
				if (currentSelection.y != selectionAnchor) {
					redrawEnd = currentSelection.y;
					currentSelection.y = selectionAnchor;
				}
			// test whether selection actually changed. Fixes 1G71EO1
			} else if (selectionAnchor == currentSelection.x && caretOffset < currentSelection.y) {
				// caret moved towards selection anchor (left side of selection).
				// shrink selection
				redrawEnd = currentSelection.y;
				redrawStart = currentSelection.y = caretOffset;
			}
		} else {
			if (caretOffset > currentSelection.y) {
				// grow selection
				redrawStart = currentSelection.y;
				redrawEnd = currentSelection.y = caretOffset;
				// check if selection has reversed direction
				if (currentSelection.x != selectionAnchor) {
					redrawStart = currentSelection.x;
					currentSelection.x = selectionAnchor;
				}
			// test whether selection actually changed. Fixes 1G71EO1
			} else if (selectionAnchor == currentSelection.y && caretOffset > currentSelection.x) {
				// caret moved towards selection anchor (right side of selection).
				// shrink selection
				caretAtBeginning[i] = true;
				redrawStart = currentSelection.x;
				redrawEnd = currentSelection.x = caretOffset;
			}
		}
		if (redrawStart != -1 && redrawEnd != -1) {
			internalRedrawRange(redrawStart, redrawEnd - redrawStart);
			selectionChanged = true;
		}
	}
	if (selectionChanged) {
		int[] regions = new int[newSelection.length * 2];
		for (int i = 0; i < newSelection.length; i++) {
			Point p = newSelection[i];
			if (caretAtBeginning[i]) {
				regions[2 * i] = p.y;
				regions[2 * i + 1] = p.x - p.y;
			} else {
				regions[2 * i] = p.x;
				regions[2 * i + 1] = p.y - p.x;
			}
		}
		setSelection(regions, false, blockSelection);
		sendSelectionEvent();
	}
	sendAccessibleTextCaretMoved();
}
/**
 * Moves the caret to the next character or to the beginning of the
 * next line if the cursor is at the end of a line.
 */
void doSelectionCursorNext() {
	int[] newCarets = Arrays.copyOf(caretOffsets, caretOffsets.length);
	int newAlignment = Integer.MIN_VALUE;
	for (int i = 0; i < caretOffsets.length; i++) {
		int caretOffset = caretOffsets[i];
		int caretLine = content.getLineAtOffset(caretOffset);
		int lineOffset = content.getOffsetAtLine(caretLine);
		int offsetInLine = caretOffset - lineOffset;
		int offset;
		if (offsetInLine < content.getLine(caretLine).length()) {
			TextLayout layout = renderer.getTextLayout(caretLine);
			offsetInLine = layout.getNextOffset(offsetInLine, SWT.MOVEMENT_CLUSTER);
			int lineStart = layout.getLineOffsets()[layout.getLineIndex(offsetInLine)];
			renderer.disposeTextLayout(layout);
			offset = offsetInLine + lineOffset;
			newAlignment = offsetInLine == lineStart ? OFFSET_LEADING : PREVIOUS_OFFSET_TRAILING;
			newCarets[i] = offset;
		} else if (caretLine < content.getLineCount() - 1 && !isSingleLine()) {
			caretLine++;
			offset = content.getOffsetAtLine(caretLine);
			newAlignment = PREVIOUS_OFFSET_TRAILING;
			newCarets[i] = offset;
		}
	}
	if (newAlignment > Integer.MIN_VALUE) {
		setCaretOffsets(newCarets, newAlignment);
		showCaret();
	}
}
/**
 * Moves the caret to the previous character or to the end of the previous
 * line if the cursor is at the beginning of a line.
 */
void doSelectionCursorPrevious() {
	int[] newCarets = Arrays.copyOf(caretOffsets, caretOffsets.length);
	for (int i = 0; i < caretOffsets.length; i++) {
		int caretOffset = caretOffsets[i];
		int caretLine = content.getLineAtOffset(caretOffset);
		int lineOffset = content.getOffsetAtLine(caretLine);
		int offsetInLine = caretOffset - lineOffset;
		if (offsetInLine > 0) {
			newCarets[i] = getClusterPrevious(caretOffset, caretLine);
		} else if (caretLine > 0) {
			caretLine--;
			lineOffset = content.getOffsetAtLine(caretLine);
			newCarets[i] = lineOffset + content.getLine(caretLine).length();
		}
	}
	if (!Arrays.equals(caretOffsets, newCarets)) {
		setCaretOffsets(newCarets, OFFSET_LEADING);
		showCaret();
	}
}
/**
 * Moves the caret one line down and to the same character offset relative
 * to the beginning of the line. Moves the caret to the end of the new line
 * if the new line is shorter than the character offset.
 * Moves the caret to the end of the text if the caret already is on the
 * last line.
 * Adjusts the selection according to the caret change. This can either add
 * to or subtract from the old selection, depending on the previous selection
 * direction.
 */
void doSelectionLineDown() {
	int oldColumnX = columnX = getPointAtOffset(caretOffsets[0]).x;
	doLineDown(true);
	columnX = oldColumnX;
}
/**
 * Moves the caret one line up and to the same character offset relative
 * to the beginning of the line. Moves the caret to the end of the new line
 * if the new line is shorter than the character offset.
 * Moves the caret to the beginning of the document if it is already on the
 * first line.
 * Adjusts the selection according to the caret change. This can either add
 * to or subtract from the old selection, depending on the previous selection
 * direction.
 */
void doSelectionLineUp() {
	int oldColumnX = columnX = getPointAtOffset(caretOffsets[0]).x;
	doLineUp(true);
	columnX = oldColumnX;
}
/**
 * Scrolls one page down so that the last line (truncated or whole)
 * of the current page becomes the fully visible top line.
 * <p>
 * The caret is scrolled the same number of lines so that its location
 * relative to the top line remains the same. The exception is the end
 * of the text where a full page scroll is not possible. In this case
 * the caret is moved after the last character.
 * </p><p>
 * Adjusts the selection according to the caret change. This can either add
 * to or subtract from the old selection, depending on the previous selection
 * direction.
 * </p>
 */
void doSelectionPageDown(int pixels) {
	int oldColumnX = columnX = getPointAtOffset(caretOffsets[0]).x;
	doPageDown(true, pixels);
	columnX = oldColumnX;
}
/**
 * Scrolls one page up so that the first line (truncated or whole)
 * of the current page becomes the fully visible last line.
 * <p>
 * The caret is scrolled the same number of lines so that its location
 * relative to the top line remains the same. The exception is the beginning
 * of the text where a full page scroll is not possible. In this case the
 * caret is moved in front of the first character.
 * </p><p>
 * Adjusts the selection according to the caret change. This can either add
 * to or subtract from the old selection, depending on the previous selection
 * direction.
 * </p>
 */
void doSelectionPageUp(int pixels) {
	if (caretOffsets.length > 1) { // operation doesn't make sense for multi-carets
		return;
	}
	int oldColumnX = columnX = getPointAtOffset(caretOffsets[0]).x;
	doPageUp(true, pixels);
	columnX = oldColumnX;
}
/**
 * Moves the caret to the end of the next word .
 */
void doSelectionWordNext() {
	int[] offsets = Arrays.stream(caretOffsets).map(offset -> getWordNext(offset, SWT.MOVEMENT_WORD)).toArray();
	// don't change caret position if in single line mode and the cursor
	// would be on a different line. fixes 5673
	if (!isSingleLine()) {
		// Force symmetrical movement for word next and previous. Fixes 14536
		setCaretOffsets(offsets, OFFSET_LEADING);
		showCaret();
	} else {
		int[] linesForCurrentCarets = Arrays.stream(caretOffsets).map(offset -> content.getLineAtOffset(offset)).toArray();
		int[] linesForNewCarets = Arrays.stream(offsets).map(offset -> content.getLineAtOffset(offset)).toArray();
		if (Arrays.equals(linesForCurrentCarets, linesForNewCarets)) {
			// Force symmetrical movement for word next and previous. Fixes 14536
			setCaretOffsets(offsets, OFFSET_LEADING);
			showCaret();
		}
	}
}
/**
 * Moves the caret to the start of the previous word.
 */
void doSelectionWordPrevious() {
	setCaretOffsets(Arrays.stream(caretOffsets).map(offset -> getWordPrevious(offset, SWT.MOVEMENT_WORD)).toArray(), OFFSET_LEADING);
	showCaret();
}
/**
 * Moves the caret one character to the left.  Do not go to the previous line.
 * When in a bidi locale and at a R2L character the caret is moved to the
 * beginning of the R2L segment (visually right) and then one character to the
 * left (visually left because it's now in a L2R segment).
 */
void doVisualPrevious() {
	setCaretOffsets(Arrays.stream(caretOffsets).map(offset -> getClusterPrevious(offset, content.getLineAtOffset(offset))).toArray(), SWT.DEFAULT);
	showCaret();
}
/**
 * Moves the caret one character to the right.  Do not go to the next line.
 * When in a bidi locale and at a R2L character the caret is moved to the
 * end of the R2L segment (visually left) and then one character to the
 * right (visually right because it's now in a L2R segment).
 */
void doVisualNext() {
	setCaretOffsets(Arrays.stream(caretOffsets).map(offset -> getClusterNext(offset, content.getLineAtOffset(offset))).toArray(), SWT.DEFAULT);
	showCaret();
}
/**
 * Moves the caret to the end of the next word.
 * If a selection exists, move the caret to the end of the selection
 * and remove the selection.
 */
void doWordNext() {
	if (Arrays.stream(selection).anyMatch(p -> p.x != p.y)) {
		setCaretOffsets(Arrays.stream(selection).mapToInt(sel -> sel.y).toArray(), SWT.DEFAULT);
		showCaret();
	} else {
		doSelectionWordNext();
	}
}
/**
 * Moves the caret to the start of the previous word.
 * If a selection exists, move the caret to the start of the selection
 * and remove the selection.
 */
void doWordPrevious() {
	if (Arrays.stream(selection).anyMatch(p -> p.x != p.y)) {
		setCaretOffsets(Arrays.stream(selection).mapToInt(sel -> sel.x).toArray(), SWT.DEFAULT);
		showCaret();
	} else {
		doSelectionWordPrevious();
	}
}
/**
 * Ends the autoscroll process.
 */
void endAutoScroll() {
	autoScrollDirection = SWT.NULL;
}
@Override
public Color getBackground() {
	checkWidget();
	if (background == null) {
		return getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
	}
	return background;
}
/**
 * Returns the baseline, in points.
 *
 * Note: this API should not be used if a StyleRange attribute causes lines to
 * have different heights (i.e. different fonts, rise, etc).
 *
 * @return baseline the baseline
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.0
 *
 * @see #getBaseline(int)
 */
public int getBaseline() {
	checkWidget();
	return renderer.getBaseline();
}
/**
 * Returns the baseline at the given offset, in points.
 *
 * @param offset the offset
 *
 * @return baseline the baseline
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_RANGE when the offset is outside the valid range (&lt; 0 or &gt; getCharCount())</li>
 * </ul>
 *
 * @since 3.2
 */
public int getBaseline(int offset) {
	checkWidget();
	if (!(0 <= offset && offset <= content.getCharCount())) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	if (isFixedLineHeight()) {
		return renderer.getBaseline();
	}
	int lineIndex = content.getLineAtOffset(offset);
	int lineOffset = content.getOffsetAtLine(lineIndex);
	TextLayout layout = renderer.getTextLayout(lineIndex);
	int lineInParagraph = layout.getLineIndex(Math.min(offset - lineOffset, layout.getText().length()));
	FontMetrics metrics = layout.getLineMetrics(lineInParagraph);
	renderer.disposeTextLayout(layout);
	return metrics.getAscent() + metrics.getLeading();
}
/**
 * Gets the BIDI coloring mode.  When true the BIDI text display
 * algorithm is applied to segments of text that are the same
 * color.
 *
 * @return the current coloring mode
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @deprecated use BidiSegmentListener instead.
 */
@Deprecated
public boolean getBidiColoring() {
	checkWidget();
	return bidiColoring;
}
/**
 * Returns whether the widget is in block selection mode.
 *
 * @return true if widget is in block selection mode, false otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.5
 */
public boolean getBlockSelection() {
	checkWidget();
	return blockSelection;
}
Rectangle getBlockSelectionPosition() {
	int firstLine = getLineIndex(blockYAnchor - getVerticalScrollOffset());
	int lastLine = getLineIndex(blockYLocation - getVerticalScrollOffset());
	if (firstLine > lastLine) {
		int temp = firstLine;
		firstLine = lastLine;
		lastLine = temp;
	}
	int left = blockXAnchor;
	int right = blockXLocation;
	if (left > right) {
		left = blockXLocation;
		right = blockXAnchor;
	}
	return new Rectangle (left - horizontalScrollOffset, firstLine, right - horizontalScrollOffset, lastLine);
}
/**
 * Returns the block selection bounds. The bounds is
 * relative to the upper left corner of the document.
 *
 * @return the block selection bounds
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.5
 */
public Rectangle getBlockSelectionBounds() {
	Rectangle rect;
	if (blockSelection && blockXLocation != -1) {
		rect = getBlockSelectionRectangle();
	} else {
		Point startPoint = getPointAtOffset(selection[0].x);
		Point endPoint = getPointAtOffset(selection[0].y);
		int height = getLineHeight(selection[0].y);
		rect = new Rectangle(startPoint.x, startPoint.y, endPoint.x - startPoint.x, endPoint.y + height - startPoint.y);
		if (selection[0].x == selection[0].y) {
			rect.width = getCaretWidth();
		}
	}
	rect.x += horizontalScrollOffset;
	rect.y += getVerticalScrollOffset();
	return rect;
}
Rectangle getBlockSelectionRectangle() {
	Rectangle rect = getBlockSelectionPosition();
	rect.y = getLinePixel(rect.y);
	rect.width = rect.width - rect.x;
	rect.height =  getLinePixel(rect.height + 1) - rect.y;
	return rect;
}
String getBlockSelectionText(String delimiter) {
	Rectangle rect = getBlockSelectionPosition();
	int firstLine = rect.y;
	int lastLine = rect.height;
	int left = rect.x;
	int right = rect.width;
	StringBuilder buffer = new StringBuilder();
	for (int lineIndex = firstLine; lineIndex <= lastLine; lineIndex++) {
		int start = getOffsetAtPoint(left, 0, lineIndex, null);
		int end = getOffsetAtPoint(right, 0, lineIndex, null);
		if (start > end) {
			int temp = start;
			start = end;
			end = temp;
		}
		String text = content.getTextRange(start, end - start);
		buffer.append(text);
		if (lineIndex < lastLine) buffer.append(delimiter);
	}
	return buffer.toString();
}
/**
 * Returns the index of the last fully visible line.
 *
 * @return index of the last fully visible line.
 */
int getBottomIndex() {
	int bottomIndex;
	if (isFixedLineHeight()) {
		int lineCount = 1;
		int lineHeight = renderer.getLineHeight();
		if (lineHeight != 0) {
			// calculate the number of lines that are fully visible
			int partialTopLineHeight = topIndex * lineHeight - getVerticalScrollOffset();
			lineCount = (clientAreaHeight - partialTopLineHeight) / lineHeight;
		}
		bottomIndex = Math.min(content.getLineCount() - 1, topIndex + Math.max(0, lineCount - 1));
	} else {
		int clientAreaHeight = this.clientAreaHeight - bottomMargin;
		bottomIndex = getLineIndex(clientAreaHeight);
		if (bottomIndex > 0) {
			int linePixel = getLinePixel(bottomIndex);
			int lineHeight = renderer.getLineHeight(bottomIndex);
			if (linePixel + lineHeight > clientAreaHeight) {
				if (getLinePixel(bottomIndex - 1) >= topMargin) {
					bottomIndex--;
				}
			}
		}
	}
	return bottomIndex;
}
/**
 * Returns the bottom margin.
 *
 * @return the bottom margin.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.5
 */
public int getBottomMargin() {
	checkWidget();
	return bottomMargin;
}
Rectangle getBoundsAtOffset(int offset) {
	int lineIndex = content.getLineAtOffset(offset);
	int lineOffset = content.getOffsetAtLine(lineIndex);
	String line = content.getLine(lineIndex);
	Rectangle bounds;
	if (line.length() != 0) {
		TextLayout layout = renderer.getTextLayout(lineIndex);
		int offsetInLine = Math.min (layout.getText().length(), Math.max (0, offset - lineOffset));
		bounds = layout.getBounds(offsetInLine, offsetInLine);
		if (getListeners(ST.LineGetSegments).length > 0 && caretAlignment == PREVIOUS_OFFSET_TRAILING && offsetInLine != 0) {
			offsetInLine = layout.getPreviousOffset(offsetInLine, SWT.MOVEMENT_CLUSTER);
			Point point = layout.getLocation(offsetInLine, true);
			bounds = new Rectangle (point.x, point.y, 0, bounds.height);
		}
		renderer.disposeTextLayout(layout);
	} else {
		bounds = new Rectangle (0, 0, 0, renderer.getLineHeight());
	}
	if (Arrays.binarySearch(caretOffsets, offset) >= 0 && !isWordWrap()) {
		int lineEnd = lineOffset + line.length();
		if (offset == lineEnd) {
			bounds.width += getCaretWidth();
		}
	}
	bounds.x += leftMargin - horizontalScrollOffset;
	bounds.y += getLinePixel(lineIndex);
	return bounds;
}
/**
 * Returns the caret position relative to the start of the text.
 *
 * @return the caret position relative to the start of the text.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCaretOffset() {
	checkWidget();
	return caretOffsets[0];
}
/**
 * Returns the caret width.
 *
 * @return the caret width, 0 if caret is null.
 */
int getCaretWidth() {
	Caret caret = getCaret();
	if (caret == null) return 0;
	return caret.getSize().x;
}
Object getClipboardContent(int clipboardType) {
	TextTransfer plainTextTransfer = TextTransfer.getInstance();
	return clipboard.getContents(plainTextTransfer, clipboardType);
}
int getClusterNext(int offset, int lineIndex) {
	int lineOffset = content.getOffsetAtLine(lineIndex);
	TextLayout layout = renderer.getTextLayout(lineIndex);
	offset -= lineOffset;
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_CLUSTER);
	offset += lineOffset;
	renderer.disposeTextLayout(layout);
	return offset;
}
int getClusterPrevious(int offset, int lineIndex) {
	int lineOffset = content.getOffsetAtLine(lineIndex);
	TextLayout layout = renderer.getTextLayout(lineIndex);
	offset -= lineOffset;
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_CLUSTER);
	offset += lineOffset;
	renderer.disposeTextLayout(layout);
	return offset;
}
/**
 * Returns the content implementation that is used for text storage.
 *
 * @return content the user defined content implementation that is used for
 * text storage or the default content implementation if no user defined
 * content implementation has been set.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public StyledTextContent getContent() {
	checkWidget();
	return content;
}
@Override
public boolean getDragDetect () {
	checkWidget ();
	return dragDetect;
}
/**
 * Returns whether the widget implements double click mouse behavior.
 *
 * @return true if double clicking a word selects the word, false if double clicks
 * have the same effect as regular mouse clicks
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getDoubleClickEnabled() {
	checkWidget();
	return doubleClickEnabled;
}
/**
 * Returns whether the widget content can be edited.
 *
 * @return true if content can be edited, false otherwise
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getEditable() {
	checkWidget();
	return editable;
}
@Override
public Color getForeground() {
	checkWidget();
	if (foreground == null) {
		return getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND);
	}
	return foreground;
}
/**
 * Returns the horizontal scroll increment.
 *
 * @return horizontal scroll increment.
 */
int getHorizontalIncrement() {
	return renderer.averageCharWidth;
}
/**
 * Returns the horizontal scroll offset relative to the start of the line.
 *
 * @return horizontal scroll offset relative to the start of the line,
 * measured in character increments starting at 0, if &gt; 0 the content is scrolled
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getHorizontalIndex() {
	checkWidget();
	return horizontalScrollOffset / getHorizontalIncrement();
}
/**
 * Returns the horizontal scroll offset relative to the start of the line.
 *
 * @return the horizontal scroll offset relative to the start of the line,
 * measured in SWT logical point starting at 0, if &gt; 0 the content is scrolled.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getHorizontalPixel() {
	checkWidget();
	return horizontalScrollOffset;
}
/**
 * Returns the line indentation of the widget.
 *
 * @return the line indentation
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getLineIndent(int)
 *
 * @since 3.2
 */
public int getIndent() {
	checkWidget();
	return indent;
}
/**
 * Returns whether the widget justifies lines.
 *
 * @return whether lines are justified
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getLineJustify(int)
 *
 * @since 3.2
 */
public boolean getJustify() {
	checkWidget();
	return justify;
}
/**
 * Returns the action assigned to the key.
 * Returns SWT.NULL if there is no action associated with the key.
 *
 * @param key a key code defined in SWT.java or a character.
 * 	Optionally ORd with a state mask.  Preferred state masks are one or more of
 *  SWT.MOD1, SWT.MOD2, SWT.MOD3, since these masks account for modifier platform
 *  differences.  However, there may be cases where using the specific state masks
 *  (i.e., SWT.CTRL, SWT.SHIFT, SWT.ALT, SWT.COMMAND) makes sense.
 * @return one of the predefined actions defined in ST.java or SWT.NULL
 * 	if there is no action associated with the key.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getKeyBinding(int key) {
	checkWidget();
	Integer action = keyActionMap.get(key);
	return action == null ? SWT.NULL : action.intValue();
}
/**
 * Gets the number of characters.
 *
 * @return number of characters in the widget
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCharCount() {
	checkWidget();
	return content.getCharCount();
}
/**
 * Returns the line at the given line index without delimiters.
 * Index 0 is the first line of the content. When there are not
 * any lines, getLine(0) is a valid call that answers an empty string.
 * <p>
 *
 * @param lineIndex index of the line to return.
 * @return the line text without delimiters
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_RANGE when the line index is outside the valid range (&lt; 0 or &gt;= getLineCount())</li>
 * </ul>
 * @since 3.4
 */
public String getLine(int lineIndex) {
	checkWidget();
	if (lineIndex < 0 ||
		(lineIndex > 0 && lineIndex >= content.getLineCount())) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	return content.getLine(lineIndex);
}
/**
 * Returns the alignment of the line at the given index.
 *
 * @param index the index of the line
 *
 * @return the line alignment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT when the index is invalid</li>
 * </ul>
 *
 * @see #getAlignment()
 *
 * @since 3.2
 */
public int getLineAlignment(int index) {
	checkWidget();
	if (index < 0 || index > content.getLineCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	return renderer.getLineAlignment(index, alignment);
}
/**
 * Returns the line at the specified offset in the text
 * where 0 &lt; offset &lt; getCharCount() so that getLineAtOffset(getCharCount())
 * returns the line of the insert location.
 *
 * @param offset offset relative to the start of the content.
 * 	0 &lt;= offset &lt;= getCharCount()
 * @return line at the specified offset in the text
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_RANGE when the offset is outside the valid range (&lt; 0 or &gt; getCharCount())</li>
 * </ul>
 */
public int getLineAtOffset(int offset) {
	checkWidget();
	if (offset < 0 || offset > getCharCount()) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	return content.getLineAtOffset(offset);
}
/**
 * Returns the background color of the line at the given index.
 * Returns null if a LineBackgroundListener has been set or if no background
 * color has been specified for the line. Should not be called if a
 * LineBackgroundListener has been set since the listener maintains the
 * line background colors.
 *
 * @param index the index of the line
 * @return the background color of the line at the given index.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT when the index is invalid</li>
 * </ul>
 */
public Color getLineBackground(int index) {
	checkWidget();
	if (index < 0 || index > content.getLineCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	return isListening(ST.LineGetBackground) ? null : renderer.getLineBackground(index, null);
}
/**
 * Returns the bullet of the line at the given index.
 *
 * @param index the index of the line
 *
 * @return the line bullet
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT when the index is invalid</li>
 * </ul>
 *
 * @since 3.2
 */
public Bullet getLineBullet(int index) {
	checkWidget();
	if (index < 0 || index > content.getLineCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	return isListening(ST.LineGetStyle) ? null : renderer.getLineBullet(index, null);
}
/**
 * Returns the line background data for the given line or null if
 * there is none.
 *
 * @param lineOffset offset of the line start relative to the start
 * 	of the content.
 * @param line line to get line background data for
 * @return line background data for the given line.
 */
StyledTextEvent getLineBackgroundData(int lineOffset, String line) {
	return sendLineEvent(ST.LineGetBackground, lineOffset, line);
}
/**
 * Gets the number of text lines.
 *
 * @return the number of lines in the widget
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getLineCount() {
	checkWidget();
	return content.getLineCount();
}
/**
 * Returns the number of lines that can be completely displayed in the
 * widget client area.
 *
 * @return number of lines that can be completely displayed in the widget
 * 	client area.
 */
int getLineCountWhole() {
	if (isFixedLineHeight()) {
		int lineHeight = renderer.getLineHeight();
		return lineHeight != 0 ? clientAreaHeight / lineHeight : 1;
	}
	return getBottomIndex() - topIndex + 1;
}
/**
 * Returns the line delimiter used for entering new lines by key down
 * or paste operation.
 *
 * @return line delimiter used for entering new lines by key down
 * or paste operation.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getLineDelimiter() {
	checkWidget();
	return content.getLineDelimiter();
}
/**
 * Returns the line height.
 * <p>
 * Note: this API should not be used if a StyleRange attribute causes lines to
 * have different heights (i.e. different fonts, rise, etc).
 * </p>
 *
 * @return line height in points.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @see #getLineHeight(int)
 */
public int getLineHeight() {
	checkWidget();
	return renderer.getLineHeight();
}
/**
 * Returns the line height at the given offset.
 *
 * @param offset the offset
 *
 * @return line height in points
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_RANGE when the offset is outside the valid range (&lt; 0 or &gt; getCharCount())</li>
 * </ul>
 *
 * @since 3.2
 */
public int getLineHeight(int offset) {
	checkWidget();
	if (!(0 <= offset && offset <= content.getCharCount())) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	if (isFixedLineHeight()) {
		return renderer.getLineHeight();
	}
	int lineIndex = content.getLineAtOffset(offset);
	int lineOffset = content.getOffsetAtLine(lineIndex);
	TextLayout layout = renderer.getTextLayout(lineIndex);
	int lineInParagraph = layout.getLineIndex(Math.min(offset - lineOffset, layout.getText().length()));
	int height = layout.getLineBounds(lineInParagraph).height;
	renderer.disposeTextLayout(layout);
	return height;
}
/**
 * Returns the indentation of the line at the given index.
 *
 * @param index the index of the line
 *
 * @return the line indentation
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT when the index is invalid</li>
 * </ul>
 *
 * @see #getIndent()
 *
 * @since 3.2
 */
public int getLineIndent(int index) {
	checkWidget();
	if (index < 0 || index > content.getLineCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	return isListening(ST.LineGetStyle) ? 0 : renderer.getLineIndent(index, indent);
}
/**
 * Returns the vertical indentation of the line at the given index.
 *
 * @param index the index of the line
 *
 * @return the line vertical indentation
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT when the index is invalid</li>
 * </ul>
 *
 * @since 3.109
 */
public int getLineVerticalIndent(int index) {
	checkWidget();
	if (index < 0 || index >= content.getLineCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	return isListening(ST.LineGetStyle) ? 0 : renderer.getLineVerticalIndent(index);
}
/**
 * Returns whether the line at the given index is justified.
 *
 * @param index the index of the line
 *
 * @return whether the line is justified
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT when the index is invalid</li>
 * </ul>
 *
 * @see #getJustify()
 *
 * @since 3.2
 */
public boolean getLineJustify(int index) {
	checkWidget();
	if (index < 0 || index > content.getLineCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	return isListening(ST.LineGetStyle) ? false : renderer.getLineJustify(index, justify);
}
/**
 * Returns the line spacing of the widget.
 *
 * @return the line spacing
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public int getLineSpacing() {
	checkWidget();
	return lineSpacing;
}
/**
 * Returns the line style data for the given line or null if there is
 * none.
 * <p>
 * If there is a LineStyleListener but it does not set any styles,
 * the StyledTextEvent.styles field will be initialized to an empty
 * array.
 * </p>
 *
 * @param lineOffset offset of the line start relative to the start of
 * 	the content.
 * @param line line to get line styles for
 * @return line style data for the given line. Styles may start before
 * 	line start and end after line end
 */
StyledTextEvent getLineStyleData(int lineOffset, String line) {
	return sendLineEvent(ST.LineGetStyle, lineOffset, line);
}
/**
 * Returns the top SWT logical point, relative to the client area, of a given line.
 * Clamps out of ranges index.
 *
 * @param lineIndex the line index, the max value is lineCount. If
 * lineIndex == lineCount it returns the bottom SWT logical point of the last line.
 * It means this function can be used to retrieve the bottom SWT logical point of any line.
 *
 * @return the top SWT logical point of a given line index
 *
 * @since 3.2
 */
public int getLinePixel(int lineIndex) {
	checkWidget();
	int lineCount = content.getLineCount();
	lineIndex = Math.max(0, Math.min(lineCount, lineIndex));
	if (isFixedLineHeight()) {
		int lineHeight = renderer.getLineHeight();
		return lineIndex * lineHeight - getVerticalScrollOffset() + topMargin;
	}
	if (lineIndex == topIndex)
		return topIndexY + topMargin;
	int height = topIndexY;
	if (lineIndex > topIndex) {
		for (int i = topIndex; i < lineIndex; i++) {
			height += renderer.getLineHeight(i);
		}
	} else {
		for (int i = topIndex - 1; i >= lineIndex; i--) {
			height -= renderer.getLineHeight(i);
		}
	}
	return height + topMargin;
}
/**
 * Returns the line index for a y, relative to the client area.
 * The line index returned is always in the range 0..lineCount - 1.
 *
 * @param y the y-coordinate point
 *
 * @return the line index for a given y-coordinate point
 *
 * @since 3.2
 */
public int getLineIndex(int y) {
	checkWidget();
	y -= topMargin;
	if (isFixedLineHeight()) {
		int lineHeight = renderer.getLineHeight();
		int lineIndex = (y + getVerticalScrollOffset()) / lineHeight;
		int lineCount = content.getLineCount();
		lineIndex = Math.max(0, Math.min(lineCount - 1, lineIndex));
		return lineIndex;
	}
	if (y == topIndexY) return topIndex;
	int line = topIndex;
	if (y < topIndexY) {
		while (y < topIndexY && line > 0) {
			y += renderer.getLineHeight(--line);
		}
	} else {
		int lineCount = content.getLineCount();
		int lineHeight = renderer.getLineHeight(line);
		while (y - lineHeight >= topIndexY && line < lineCount - 1) {
			y -= lineHeight;
			lineHeight = renderer.getLineHeight(++line);
		}
	}
	return line;
}
/**
 * Returns the tab stops of the line at the given <code>index</code>.
 *
 * @param index the index of the line
 *
 * @return the tab stops for the line
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT when the index is invalid</li>
 * </ul>
 *
 * @see #getTabStops()
 *
 * @since 3.6
 */
public int[] getLineTabStops(int index) {
	checkWidget();
	if (index < 0 || index > content.getLineCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (isListening(ST.LineGetStyle)) return null;
	int[] tabs = renderer.getLineTabStops(index, null);
	if (tabs == null) tabs = this.tabs;
	if (tabs == null) return new int [] {renderer.tabWidth};
	int[] result = new int[tabs.length];
	System.arraycopy(tabs, 0, result, 0, tabs.length);
	return result;
}
/**
 * Returns the wrap indentation of the line at the given <code>index</code>.
 *
 * @param index the index of the line
 *
 * @return the wrap indentation
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT when the index is invalid</li>
 * </ul>
 *
 * @see #getWrapIndent()
 *
 * @since 3.6
 */
public int getLineWrapIndent(int index) {
	checkWidget();
	if (index < 0 || index > content.getLineCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	return isListening(ST.LineGetStyle) ? 0 : renderer.getLineWrapIndent(index, wrapIndent);
}
/**
 * Returns the left margin.
 *
 * @return the left margin.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.5
 */
public int getLeftMargin() {
	checkWidget();
	return leftMargin - alignmentMargin;
}
/**
 * Returns the x, y location of the upper left corner of the character
 * bounding box at the specified offset in the text. The point is
 * relative to the upper left corner of the widget client area.
 *
 * @param offset offset relative to the start of the content.
 * 	0 &lt;= offset &lt;= getCharCount()
 * @return x, y location of the upper left corner of the character
 * 	bounding box at the specified offset in the text.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_RANGE when the offset is outside the valid range (&lt; 0 or &gt; getCharCount())</li>
 * </ul>
 */
public Point getLocationAtOffset(int offset) {
	checkWidget();
	if (offset < 0 || offset > getCharCount()) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	return getPointAtOffset(offset);
}
/**
 * Returns <code>true</code> if the mouse navigator is enabled.
 * When mouse navigator is enabled, the user can navigate through the widget by pressing the middle button and moving the cursor
 *
 * @return the mouse navigator's enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getEnabled
 * @since 3.110
 */
public boolean getMouseNavigatorEnabled () {
	checkWidget ();
	return mouseNavigator != null;
}
/**
 * Returns the character offset of the first character of the given line.
 *
 * @param lineIndex index of the line, 0 based relative to the first
 * 	line in the content. 0 &lt;= lineIndex &lt; getLineCount(), except
 * 	lineIndex may always be 0
 * @return offset offset of the first character of the line, relative to
 * 	the beginning of the document. The first character of the document is
 *	at offset 0.
 *  When there are not any lines, getOffsetAtLine(0) is a valid call that
 * 	answers 0.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_RANGE when the line index is outside the valid range (&lt; 0 or &gt;= getLineCount())</li>
 * </ul>
 * @since 2.0
 */
public int getOffsetAtLine(int lineIndex) {
	checkWidget();
	if (lineIndex < 0 ||
		(lineIndex > 0 && lineIndex >= content.getLineCount())) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	return content.getOffsetAtLine(lineIndex);
}
/**
 * Returns the offset of the character at the given location relative
 * to the first character in the document.
 * <p>
 * The return value reflects the character offset that the caret will
 * be placed at if a mouse click occurred at the specified location.
 * If the x coordinate of the location is beyond the center of a character
 * the returned offset will be behind the character.
 * </p>
 *
 * @param point the origin of character bounding box relative to
 *  the origin of the widget client area.
 * @return offset of the character at the given location relative
 *  to the first character in the document.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_NULL_ARGUMENT when point is null</li>
 *   <li>ERROR_INVALID_ARGUMENT when there is no character at the specified location</li>
 * </ul>
 *
 * @deprecated Use {@link #getOffsetAtPoint(Point)} instead for better performance
 */
@Deprecated
public int getOffsetAtLocation(Point point) {
	checkWidget();
	if (point == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	int[] trailing = new int[1];
	int offset = getOffsetAtPoint(point.x, point.y, trailing, true);
	if (offset == -1) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	return offset + trailing[0];
}

/**
 * Returns the offset of the character at the given point relative
 * to the first character in the document.
 * <p>
 * The return value reflects the character offset that the caret will
 * be placed at if a mouse click occurred at the specified point.
 * If the x coordinate of the point is beyond the center of a character
 * the returned offset will be behind the character.
 * </p>
 * Note: This method is functionally similar to {@link #getOffsetAtLocation(Point)} except that
 * it does not throw an exception when no character is found and thus performs faster.
 *
 * @param point the origin of character bounding box relative to
 *  the origin of the widget client area.
 * @return offset of the character at the given point relative
 *  to the first character in the document.
 * -1 when there is no character at the specified location.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_NULL_ARGUMENT when point is <code>null</code></li>
 * </ul>
 *
 * @since 3.107
 */
public int getOffsetAtPoint(Point point) {
	checkWidget();
	if (point == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	int[] trailing = new int[1];
	int offset = getOffsetAtPoint(point.x, point.y, trailing, true);
	return offset != -1 ? offset + trailing[0] : -1;
}

int getOffsetAtPoint(int x, int y, int[] alignment) {
	int lineIndex = getLineIndex(y);
	y -= getLinePixel(lineIndex);
	return getOffsetAtPoint(x, y, lineIndex, alignment);
}
int getOffsetAtPoint(int x, int y, int lineIndex, int[] alignment) {
	TextLayout layout = renderer.getTextLayout(lineIndex);
	x += horizontalScrollOffset - leftMargin;
	int[] trailing = new int[1];
	int offsetInLine = layout.getOffset(x, y, trailing);
	if (alignment != null) alignment[0] = OFFSET_LEADING;
	if (trailing[0] != 0) {
		int lineInParagraph = layout.getLineIndex(offsetInLine + trailing[0]);
		int lineStart = layout.getLineOffsets()[lineInParagraph];
		if (offsetInLine + trailing[0] == lineStart) {
			offsetInLine += trailing[0];
			if (alignment != null) alignment[0] = PREVIOUS_OFFSET_TRAILING;
		} else {
			String line = content.getLine(lineIndex);
			int level = 0;
			if (alignment != null) {
				int offset = offsetInLine;
				while (offset > 0 && Character.isDigit(line.charAt(offset))) offset--;
				if (offset == 0 && Character.isDigit(line.charAt(offset))) {
					level = isMirrored() ? 1 : 0;
				} else {
					level = layout.getLevel(offset) & 0x1;
				}
			}
			offsetInLine += trailing[0];
			if (alignment != null) {
				int trailingLevel = layout.getLevel(offsetInLine) & 0x1;
				if (level != trailingLevel) {
					alignment[0] = PREVIOUS_OFFSET_TRAILING;
				} else {
					alignment[0] = OFFSET_LEADING;
				}
			}
		}
	}
	renderer.disposeTextLayout(layout);
	return offsetInLine + content.getOffsetAtLine(lineIndex);
}
int getOffsetAtPoint(int x, int y, int[] trailing, boolean inTextOnly) {
	if (inTextOnly && y + getVerticalScrollOffset() < 0 || x + horizontalScrollOffset < 0) {
		return -1;
	}
	int bottomIndex = getPartialBottomIndex();
	int height = getLinePixel(bottomIndex + 1);
	if (inTextOnly && y > height) {
		return -1;
	}
	int lineIndex = getLineIndex(y);
	int lineOffset = content.getOffsetAtLine(lineIndex);
	TextLayout layout = renderer.getTextLayout(lineIndex);
	x += horizontalScrollOffset - leftMargin;
	y -= getLinePixel(lineIndex);
	int offset = layout.getOffset(x, y, trailing);
	Rectangle rect = layout.getLineBounds(layout.getLineIndex(offset));
	renderer.disposeTextLayout(layout);
	if (inTextOnly && !(rect.x  <= x && x <=  rect.x + rect.width)) {
		return -1;
	}
	return offset + lineOffset;
}
/**
 * Returns the orientation of the receiver.
 *
 * @return the orientation style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 2.1.2
 */
@Override
public int getOrientation () {
	return super.getOrientation ();
}
/**
 * Returns the index of the last partially visible line.
 *
 * @return index of the last partially visible line.
 */
int getPartialBottomIndex() {
	if (isFixedLineHeight()) {
		int lineHeight = renderer.getLineHeight();
		int partialLineCount = Compatibility.ceil(clientAreaHeight, lineHeight);
		return Math.max(0, Math.min(content.getLineCount(), topIndex + partialLineCount) - 1);
	}
	return getLineIndex(clientAreaHeight - bottomMargin);
}
/**
 * Returns the index of the first partially visible line.
 *
 * @return index of the first partially visible line.
 */
int getPartialTopIndex() {
	if (isFixedLineHeight()) {
		int lineHeight = renderer.getLineHeight();
		return getVerticalScrollOffset() / lineHeight;
	}
	return topIndexY <= 0 ? topIndex : topIndex - 1;
}
/**
 * Returns the content in the specified range using the platform line
 * delimiter to separate lines.
 *
 * @param writer the TextWriter to write line text into
 * @return the content in the specified range using the platform line
 * 	delimiter to separate lines as written by the specified TextWriter.
 */
String getPlatformDelimitedText(TextWriter writer) {
	int end = writer.getStart() + writer.getCharCount();
	int startLine = content.getLineAtOffset(writer.getStart());
	int endLine = content.getLineAtOffset(end);
	String endLineText = content.getLine(endLine);
	int endLineOffset = content.getOffsetAtLine(endLine);

	for (int i = startLine; i <= endLine; i++) {
		writer.writeLine(content.getLine(i), content.getOffsetAtLine(i));
		if (i < endLine) {
			writer.writeLineDelimiter(PlatformLineDelimiter);
		}
	}
	if (end > endLineOffset + endLineText.length()) {
		writer.writeLineDelimiter(PlatformLineDelimiter);
	}
	writer.close();
	return writer.toString();
}
/**
 * Returns all the ranges of text that have an associated StyleRange.
 * Returns an empty array if a LineStyleListener has been set.
 * Should not be called if a LineStyleListener has been set since the
 * listener maintains the styles.
 * <p>
 * The ranges array contains start and length pairs.  Each pair refers to
 * the corresponding style in the styles array.  For example, the pair
 * that starts at ranges[n] with length ranges[n+1] uses the style
 * at styles[n/2] returned by <code>getStyleRanges(int, int, boolean)</code>.
 * </p>
 *
 * @return the ranges or an empty array if a LineStyleListener has been set.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 *
 * @see #getStyleRanges(boolean)
 */
public int[] getRanges() {
	checkWidget();
	if (!isListening(ST.LineGetStyle)) {
		int[] ranges = renderer.getRanges(0, content.getCharCount());
		if (ranges != null) return ranges;
	}
	return new int[0];
}
/**
 * Returns the ranges of text that have an associated StyleRange.
 * Returns an empty array if a LineStyleListener has been set.
 * Should not be called if a LineStyleListener has been set since the
 * listener maintains the styles.
 * <p>
 * The ranges array contains start and length pairs.  Each pair refers to
 * the corresponding style in the styles array.  For example, the pair
 * that starts at ranges[n] with length ranges[n+1] uses the style
 * at styles[n/2] returned by <code>getStyleRanges(int, int, boolean)</code>.
 * </p>
 *
 * @param start the start offset of the style ranges to return
 * @param length the number of style ranges to return
 *
 * @return the ranges or an empty array if a LineStyleListener has been set.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_RANGE if start or length are outside the widget content</li>
 * </ul>
 *
 * @since 3.2
 *
 * @see #getStyleRanges(int, int, boolean)
 */
public int[] getRanges(int start, int length) {
	checkWidget();
	int contentLength = getCharCount();
	int end = start + length;
	if (start > end || start < 0 || end > contentLength) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	if (!isListening(ST.LineGetStyle)) {
		int[] ranges = renderer.getRanges(start, length);
		if (ranges != null) return ranges;
	}
	return new int[0];
}
/**
 * Returns the right margin.
 *
 * @return the right margin.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.5
 */
public int getRightMargin() {
	checkWidget();
	return rightMargin;
}
/**
 * Returns the selection.
 * <p>
 * Text selections are specified in terms of caret positions.  In a text
 * widget that contains N characters, there are N+1 caret positions,
 * ranging from 0..N
 * </p>
 * <p>
 * It is usually better to use {@link #getSelectionRanges()} which better
 * support multiple selection and carets and block selection.
 * </p>
 *
 * @return start and end of the selection, x is the offset of the first
 * 	selected character, y is the offset after the last selected character.
 *  The selection values returned are visual (i.e., x will always always be
 *  &lt;= y).  To determine if a selection is right-to-left (RtoL) vs. left-to-right
 *  (LtoR), compare the caretOffset to the start and end of the selection
 *  (e.g., caretOffset == start of selection implies that the selection is RtoL).
 * @see #getSelectionRanges
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSelection() {
	checkWidget();
	return new Point(selection[0].x, selection[0].y);
}
/**
 * Returns the selection.
 * <p>
 * It is usually better to use {@link #getSelectionRanges()} which better
 * support multiple selection and carets and block selection.
 * </p>
 *
 * @return start and length of the selection, x is the offset of the
 * 	first selected character, relative to the first character of the
 * 	widget content. y is the length of the selection.
 *  The selection values returned are visual (i.e., length will always always be
 *  positive).  To determine if a selection is right-to-left (RtoL) vs. left-to-right
 *  (LtoR), compare the caretOffset to the start and end of the selection
 *  (e.g., caretOffset == start of selection implies that the selection is RtoL).
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @see #getSelectionRanges
 */
public Point getSelectionRange() {
	checkWidget();
	return new Point(selection[0].x, selection[0].y - selection[0].x);
}
/**
 * Returns the selected ranges of text.
 * If block is enabled, return the ranges that are inside the block selection rectangle.
 * <p>
 * The ranges array contains start and length pairs.
 * <p>
 * When the receiver is not
 * in block selection mode the return arrays contains the start and length of
 * the regular selections.
 *
 * @return the ranges array
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.5
 * @see #setSelectionRanges(int[])
 */
public int[] getSelectionRanges() {
	checkWidget();
	if (blockSelection && blockXLocation != -1) {
		Rectangle rect = getBlockSelectionPosition();
		int firstLine = rect.y;
		int lastLine = rect.height;
		int left = rect.x;
		int right = rect.width;
		int[] ranges = new int[(lastLine - firstLine + 1) * 2];
		int index = 0;
		for (int lineIndex = firstLine; lineIndex <= lastLine; lineIndex++) {
			int start = getOffsetAtPoint(left, 0, lineIndex, null);
			int end = getOffsetAtPoint(right, 0, lineIndex, null);
			if (start > end) {
				int temp = start;
				start = end;
				end = temp;
			}
			ranges[index++] = start;
			ranges[index++] = end - start;
		}
		return ranges;
	}
	int[] res = new int[2 * selection.length];
	int index = 0;
	for (Point p : selection) {
		res[index++] = p.x;
		res[index++] = p.y - p.x;
	}
	return res;
}
/**
 * Returns the receiver's selection background color.
 *
 * @return the selection background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 2.1
 */
public Color getSelectionBackground() {
	checkWidget();
	if (selectionBackground == null) {
		return getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION);
	}
	return selectionBackground;
}
/**
 * Gets the number of selected characters.
 *
 * @return the number of selected characters.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionCount() {
	checkWidget();
	if (blockSelection && blockXLocation != -1) {
		return getBlockSelectionText(content.getLineDelimiter()).length();
	}
	return Arrays.stream(selection).collect(Collectors.summingInt(sel -> sel.y - sel.x));
}
/**
 * Returns the receiver's selection foreground color.
 *
 * @return the selection foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 2.1
 */
public Color getSelectionForeground() {
	checkWidget();
	if (selectionForeground == null) {
		return getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);
	}
	return selectionForeground;
}
/**
 * Returns the selected text.
 *
 * @return selected text, or an empty String if there is no selection.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getSelectionText() {
	checkWidget();
	if (blockSelection && blockXLocation != -1) {
		return getBlockSelectionText(content.getLineDelimiter());
	}
	return Arrays.stream(selection).map(sel -> content.getTextRange(sel.x, sel.y - sel.x)).collect(Collectors.joining());
}
StyledTextEvent getBidiSegments(int lineOffset, String line) {
	if (!isListening(ST.LineGetSegments)) {
		if (!bidiColoring) return null;
		StyledTextEvent event = new StyledTextEvent(content);
		event.segments = getBidiSegmentsCompatibility(line, lineOffset);
		return event;
	}
	StyledTextEvent event = sendLineEvent(ST.LineGetSegments, lineOffset, line);
	if (event == null || event.segments == null || event.segments.length == 0) return null;
	int lineLength = line.length();
	int[] segments = event.segments;
	if (segments[0] > lineLength) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	char[] segmentsChars = event.segmentsChars;
	boolean hasSegmentsChars = segmentsChars != null;
	for (int i = 1; i < segments.length; i++) {
		if ((hasSegmentsChars ? segments[i] < segments[i - 1] : segments[i] <= segments[i - 1]) || segments[i] > lineLength) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	if (hasSegmentsChars && !visualWrap) {
		for (char segmentsChar : segmentsChars) {
			if (segmentsChar == '\n' || segmentsChar == '\r') {
				visualWrap = true;
				break;
			}
		}
	}
	return event;
}
/**
 * @see #getBidiSegments
 * Supports deprecated setBidiColoring API. Remove when API is removed.
 */
int [] getBidiSegmentsCompatibility(String line, int lineOffset) {
	int lineLength = line.length();
	StyleRange [] styles = null;
	StyledTextEvent event = getLineStyleData(lineOffset, line);
	if (event != null) {
		styles = event.styles;
	} else {
		styles = renderer.getStyleRanges(lineOffset, lineLength, true);
	}
	if (styles == null || styles.length == 0) {
		return new int[] {0, lineLength};
	}
	int k=0, count = 1;
	while (k < styles.length && styles[k].start == 0 && styles[k].length == lineLength) {
		k++;
	}
	int[] offsets = new int[(styles.length - k) * 2 + 2];
	for (int i = k; i < styles.length; i++) {
		StyleRange style = styles[i];
		int styleLineStart = Math.max(style.start - lineOffset, 0);
		int styleLineEnd = Math.max(style.start + style.length - lineOffset, styleLineStart);
		styleLineEnd = Math.min (styleLineEnd, line.length ());
		if (i > 0 && count > 1 &&
			((styleLineStart >= offsets[count-2] && styleLineStart <= offsets[count-1]) ||
			 (styleLineEnd >= offsets[count-2] && styleLineEnd <= offsets[count-1])) &&
			 style.similarTo(styles[i-1])) {
			offsets[count-2] = Math.min(offsets[count-2], styleLineStart);
			offsets[count-1] = Math.max(offsets[count-1], styleLineEnd);
		} else {
			if (styleLineStart > offsets[count - 1]) {
				offsets[count] = styleLineStart;
				count++;
			}
			offsets[count] = styleLineEnd;
			count++;
		}
	}
	// add offset for last non-colored segment in line, if any
	if (lineLength > offsets[count-1]) {
		offsets [count] = lineLength;
		count++;
	}
	if (count == offsets.length) {
		return offsets;
	}
	int [] result = new int [count];
	System.arraycopy (offsets, 0, result, 0, count);
	return result;
}
/**
 * Returns the style range at the given offset.
 * <p>
 * Returns null if a LineStyleListener has been set or if a style is not set
 * for the offset.
 * Should not be called if a LineStyleListener has been set since the
 * listener maintains the styles.
 * </p>
 *
 * @param offset the offset to return the style for.
 * 	0 &lt;= offset &lt; getCharCount() must be true.
 * @return a StyleRange with start == offset and length == 1, indicating
 * 	the style at the given offset. null if a LineStyleListener has been set
 * 	or if a style is not set for the given offset.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_ARGUMENT when the offset is invalid</li>
 * </ul>
 */
public StyleRange getStyleRangeAtOffset(int offset) {
	checkWidget();
	if (offset < 0 || offset >= getCharCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (!isListening(ST.LineGetStyle)) {
		StyleRange[] ranges = renderer.getStyleRanges(offset, 1, true);
		if (ranges != null) return ranges[0];
	}
	return null;
}
/**
 * Returns the styles.
 * <p>
 * Returns an empty array if a LineStyleListener has been set.
 * Should not be called if a LineStyleListener has been set since the
 * listener maintains the styles.
 * </p><p>
 * Note: Because a StyleRange includes the start and length, the
 * same instance cannot occur multiple times in the array of styles.
 * If the same style attributes, such as font and color, occur in
 * multiple StyleRanges, <code>getStyleRanges(boolean)</code>
 * can be used to get the styles without the ranges.
 * </p>
 *
 * @return the styles or an empty array if a LineStyleListener has been set.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getStyleRanges(boolean)
 */
public StyleRange[] getStyleRanges() {
	checkWidget();
	return getStyleRanges(0, content.getCharCount(), true);
}
/**
 * Returns the styles.
 * <p>
 * Returns an empty array if a LineStyleListener has been set.
 * Should not be called if a LineStyleListener has been set since the
 * listener maintains the styles.
 * </p><p>
 * Note: When <code>includeRanges</code> is true, the start and length
 * fields of each StyleRange will be valid, however the StyleRange
 * objects may need to be cloned. When <code>includeRanges</code> is
 * false, <code>getRanges(int, int)</code> can be used to get the
 * associated ranges.
 * </p>
 *
 * @param includeRanges whether the start and length field of the StyleRanges should be set.
 *
 * @return the styles or an empty array if a LineStyleListener has been set.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 *
 * @see #getRanges(int, int)
 * @see #setStyleRanges(int[], StyleRange[])
 */
public StyleRange[] getStyleRanges(boolean includeRanges) {
	checkWidget();
	return getStyleRanges(0, content.getCharCount(), includeRanges);
}
/**
 * Returns the styles for the given text range.
 * <p>
 * Returns an empty array if a LineStyleListener has been set.
 * Should not be called if a LineStyleListener has been set since the
 * listener maintains the styles.
 * </p><p>
 * Note: Because the StyleRange includes the start and length, the
 * same instance cannot occur multiple times in the array of styles.
 * If the same style attributes, such as font and color, occur in
 * multiple StyleRanges, <code>getStyleRanges(int, int, boolean)</code>
 * can be used to get the styles without the ranges.
 * </p>
 * @param start the start offset of the style ranges to return
 * @param length the number of style ranges to return
 *
 * @return the styles or an empty array if a LineStyleListener has
 *  been set.  The returned styles will reflect the given range.  The first
 *  returned <code>StyleRange</code> will have a starting offset &gt;= start
 *  and the last returned <code>StyleRange</code> will have an ending
 *  offset &lt;= start + length - 1
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_RANGE when start and/or end are outside the widget content</li>
 * </ul>
 *
 * @see #getStyleRanges(int, int, boolean)
 *
 * @since 3.0
 */
public StyleRange[] getStyleRanges(int start, int length) {
	checkWidget();
	return getStyleRanges(start, length, true);
}
/**
 * Returns the styles for the given text range.
 * <p>
 * Returns an empty array if a LineStyleListener has been set.
 * Should not be called if a LineStyleListener has been set since the
 * listener maintains the styles.
 * </p><p>
 * Note: When <code>includeRanges</code> is true, the start and length
 * fields of each StyleRange will be valid, however the StyleRange
 * objects may need to be cloned. When <code>includeRanges</code> is
 * false, <code>getRanges(int, int)</code> can be used to get the
 * associated ranges.
 * </p>
 *
 * @param start the start offset of the style ranges to return
 * @param length the number of style ranges to return
 * @param includeRanges whether the start and length field of the StyleRanges should be set.
 *
 * @return the styles or an empty array if a LineStyleListener has
 *  been set.  The returned styles will reflect the given range.  The first
 *  returned <code>StyleRange</code> will have a starting offset &gt;= start
 *  and the last returned <code>StyleRange</code> will have an ending
 *  offset &gt;= start + length - 1
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_RANGE when start and/or end are outside the widget content</li>
 * </ul>
 *
 * @since 3.2
 *
 * @see #getRanges(int, int)
 * @see #setStyleRanges(int[], StyleRange[])
 */
public StyleRange[] getStyleRanges(int start, int length, boolean includeRanges) {
	checkWidget();
	int contentLength = getCharCount();
	int end = start + length;
	if (start > end || start < 0 || end > contentLength) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	if (!isListening(ST.LineGetStyle)) {
		StyleRange[] ranges = renderer.getStyleRanges(start, length, includeRanges);
		if (ranges != null) return ranges;
	}
	return new StyleRange[0];
}
/**
 * Returns the tab width measured in characters.
 *
 * @return tab width measured in characters
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getTabStops()
 */
public int getTabs() {
	checkWidget();
	return tabLength;
}

/**
 * Returns the tab list of the receiver.
 *
 * @return the tab list
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.6
 */
public int[] getTabStops() {
	checkWidget();
	if (tabs == null) return new int [] {renderer.tabWidth};
	int[] result = new int[tabs.length];
	System.arraycopy(tabs, 0, result, 0, tabs.length);
	return result;
}

/**
 * Returns a copy of the widget content.
 *
 * @return copy of the widget content
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText() {
	checkWidget();
	return content.getTextRange(0, getCharCount());
}
/**
 * Returns the widget content between the two offsets.
 *
 * @param start offset of the first character in the returned String
 * @param end offset of the last character in the returned String
 * @return widget content starting at start and ending at end
 * @see #getTextRange(int,int)
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_RANGE when start and/or end are outside the widget content</li>
 * </ul>
 */
public String getText(int start, int end) {
	checkWidget();
	int contentLength = getCharCount();
	if (start < 0 || start >= contentLength || end < 0 || end >= contentLength || start > end) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	return content.getTextRange(start, end - start + 1);
}
/**
 * Returns the smallest bounding rectangle that includes the characters between two offsets.
 *
 * @param start offset of the first character included in the bounding box
 * @param end offset of the last character included in the bounding box
 * @return bounding box of the text between start and end
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_RANGE when start and/or end are outside the widget content</li>
 * </ul>
 * @since 3.1
 */
public Rectangle getTextBounds(int start, int end) {
	checkWidget();
	int contentLength = getCharCount();
	if (start < 0 || start >= contentLength || end < 0 || end >= contentLength || start > end) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	int lineStart = content.getLineAtOffset(start);
	int lineEnd = content.getLineAtOffset(end);
	Rectangle rect;
	int y = getLinePixel(lineStart);
	int height = 0;
	int left = 0x7fffffff, right = 0;
	for (int i = lineStart; i <= lineEnd; i++) {
		int lineOffset = content.getOffsetAtLine(i);
		TextLayout layout = renderer.getTextLayout(i);
		int length = layout.getText().length();
		if (length > 0) {
			if (i == lineStart) {
				if (i == lineEnd) {
					rect = layout.getBounds(start - lineOffset, end - lineOffset);
				} else {
					rect = layout.getBounds(start - lineOffset, length);
				}
				y += rect.y;
			} else if (i == lineEnd) {
				rect = layout.getBounds(0, end - lineOffset);
			} else {
				rect = layout.getBounds();
			}
			left = Math.min(left, rect.x);
			right = Math.max(right, rect.x + rect.width);
			height += rect.height;
		} else {
			height += renderer.getLineHeight();
		}
		renderer.disposeTextLayout(layout);
	}
	rect = new Rectangle (left, y, right-left, height);
	rect.x += leftMargin - horizontalScrollOffset;
	return rect;
}
/**
 * Returns the widget content starting at start for length characters.
 *
 * @param start offset of the first character in the returned String
 * @param length number of characters to return
 * @return widget content starting at start and extending length characters.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_RANGE when start and/or length are outside the widget content</li>
 * </ul>
 */
public String getTextRange(int start, int length) {
	checkWidget();
	int contentLength = getCharCount();
	int end = start + length;
	if (start > end || start < 0 || end > contentLength) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	return content.getTextRange(start, length);
}
/**
 * Returns the maximum number of characters that the receiver is capable of holding.
 *
 * @return the text limit
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTextLimit() {
	checkWidget();
	return textLimit;
}
/**
 * Gets the top index.
 * <p>
 * The top index is the index of the fully visible line that is currently
 * at the top of the widget or the topmost partially visible line if no line is fully visible.
 * The top index changes when the widget is scrolled. Indexing is zero based.
 * </p>
 *
 * @return the index of the top line
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopIndex() {
	checkWidget();
	return topIndex;
}
/**
 * Returns the top margin.
 *
 * @return the top margin.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.5
 */
public int getTopMargin() {
	checkWidget();
	return topMargin;
}
/**
 * Gets the top SWT logical point.
 * <p>
 * The top point is the SWT logical point position of the line that is
 * currently at the top of the widget. The text widget can be scrolled by points
 * by dragging the scroll thumb so that a partial line may be displayed at the top
 * the widget.  The top point changes when the widget is scrolled.  The top point
 * does not include the widget trimming.
 * </p>
 *
 * @return SWT logical point position of the top line
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopPixel() {
	checkWidget();
	return getVerticalScrollOffset();
}
/**
 * Returns the vertical scroll increment.
 *
 * @return vertical scroll increment.
 */
int getVerticalIncrement() {
	return renderer.getLineHeight();
}
int getVerticalScrollOffset() {
	if (verticalScrollOffset == -1) {
		renderer.calculate(0, topIndex);
		int height = 0;
		for (int i = 0; i < topIndex; i++) {
			height += renderer.getCachedLineHeight(i);
		}
		height -= topIndexY;
		verticalScrollOffset = height;
	}
	return verticalScrollOffset;
}
int getVisualLineIndex(TextLayout layout, int offsetInLine) {
	int lineIndex = layout.getLineIndex(offsetInLine);
	int[] offsets = layout.getLineOffsets();
	Caret caret = getCaret();
	if (caret != null && lineIndex != 0 && offsetInLine == offsets[lineIndex]) {
		int lineY = layout.getLineBounds(lineIndex).y;
		int caretY = caret.getLocation().y - getLinePixel(getFirstCaretLine());
		if (lineY > caretY) lineIndex--;
		caretAlignment = OFFSET_LEADING;
	}
	return lineIndex;
}
int getCaretDirection() {
	if (!isBidiCaret()) return SWT.DEFAULT;
	if (ime.getCompositionOffset() != -1) return SWT.DEFAULT;
	if (!updateCaretDirection && caretDirection != SWT.NULL) return caretDirection;
	updateCaretDirection = false;
	int caretLine = getFirstCaretLine();
	int lineOffset = content.getOffsetAtLine(caretLine);
	String line = content.getLine(caretLine);
	int offset = caretOffsets[0] - lineOffset;
	int lineLength = line.length();
	if (lineLength == 0) return isMirrored() ? SWT.RIGHT : SWT.LEFT;
	if (caretAlignment == PREVIOUS_OFFSET_TRAILING && offset > 0) offset--;
	if (offset == lineLength && offset > 0) offset--;
	while (offset > 0 && Character.isDigit(line.charAt(offset))) offset--;
	if (offset == 0 && Character.isDigit(line.charAt(offset))) {
		return isMirrored() ? SWT.RIGHT : SWT.LEFT;
	}
	TextLayout layout = renderer.getTextLayout(caretLine);
	int level = layout.getLevel(offset);
	renderer.disposeTextLayout(layout);
	return ((level & 1) != 0) ? SWT.RIGHT : SWT.LEFT;
}
/*
 * Returns the index of the line the first/top caret is on.
 */
int getFirstCaretLine() {
	return content.getLineAtOffset(caretOffsets[0]);
}
int getWrapWidth () {
	if (wordWrap && !isSingleLine()) {
		int width = clientAreaWidth - leftMargin - rightMargin;
		return width > 0 ? width : 1;
	}
	return -1;
}
int getWordNext (int offset, int movement) {
	return getWordNext(offset, movement, false);
}
int getWordNext (int offset, int movement, boolean ignoreListener) {
	int newOffset, lineOffset;
	String lineText;
	if (offset >= getCharCount()) {
		newOffset = offset;
		int lineIndex = content.getLineCount() - 1;
		lineOffset = content.getOffsetAtLine(lineIndex);
		lineText = content.getLine(lineIndex);
	} else {
		int lineIndex = content.getLineAtOffset(offset);
		lineOffset = content.getOffsetAtLine(lineIndex);
		lineText = content.getLine(lineIndex);
		int lineLength = lineText.length();
		if (offset >= lineOffset + lineLength) {
			newOffset = content.getOffsetAtLine(lineIndex + 1);
		} else {
			TextLayout layout = renderer.getTextLayout(lineIndex);
			newOffset = lineOffset + layout.getNextOffset(offset - lineOffset, movement);
			renderer.disposeTextLayout(layout);
		}
	}
	if (ignoreListener) return newOffset;
	return sendWordBoundaryEvent(ST.WordNext, movement, offset, newOffset, lineText, lineOffset);
}
int getWordPrevious(int offset, int movement) {
	return getWordPrevious(offset, movement, false);
}
int getWordPrevious(int offset, int movement, boolean ignoreListener) {
	int newOffset, lineOffset;
	String lineText;
	if (offset <= 0) {
		newOffset = 0;
		int lineIndex = content.getLineAtOffset(newOffset);
		lineOffset = content.getOffsetAtLine(lineIndex);
		lineText = content.getLine(lineIndex);
	} else {
		int lineIndex = content.getLineAtOffset(offset);
		lineOffset = content.getOffsetAtLine(lineIndex);
		lineText = content.getLine(lineIndex);
		if (offset == lineOffset) {
			String nextLineText = content.getLine(lineIndex - 1);
			int nextLineOffset = content.getOffsetAtLine(lineIndex - 1);
			newOffset = nextLineOffset + nextLineText.length();
		} else {
			int layoutOffset = Math.min(offset - lineOffset, lineText.length());
			TextLayout layout = renderer.getTextLayout(lineIndex);
			newOffset = lineOffset + layout.getPreviousOffset(layoutOffset, movement);
			renderer.disposeTextLayout(layout);
		}
	}
	if (ignoreListener) return newOffset;
	return sendWordBoundaryEvent(ST.WordPrevious, movement, offset, newOffset, lineText, lineOffset);
}
/**
 * Returns whether the widget wraps lines.
 *
 * @return true if widget wraps lines, false otherwise
 * @since 2.0
 */
public boolean getWordWrap() {
	checkWidget();
	return wordWrap;
}
/**
 * Returns the wrap indentation of the widget.
 *
 * @return the wrap indentation
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getLineWrapIndent(int)
 *
 * @since 3.6
 */
public int getWrapIndent() {
	checkWidget();
	return wrapIndent;
}
/**
 * Returns the location of the given offset.
 * <p>
 * <b>NOTE:</b> Does not return correct values for true italic fonts (vs. slanted fonts).
 * </p>
 *
 * @return location of the character at the given offset in the line.
 */
Point getPointAtOffset(int offset) {
	int lineIndex = content.getLineAtOffset(offset);
	String line = content.getLine(lineIndex);
	int lineOffset = content.getOffsetAtLine(lineIndex);
	int offsetInLine = Math.max (0, offset - lineOffset);
	int lineLength = line.length();
	if (lineIndex < content.getLineCount() - 1) {
		int endLineOffset = content.getOffsetAtLine(lineIndex + 1) - 1;
		if (lineLength < offsetInLine && offsetInLine <= endLineOffset) {
			offsetInLine = lineLength;
		}
	}
	Point point;
	TextLayout layout = renderer.getTextLayout(lineIndex);
	if (lineLength != 0  && offsetInLine <= lineLength) {
		if (offsetInLine == lineLength) {
			offsetInLine = layout.getPreviousOffset(offsetInLine, SWT.MOVEMENT_CLUSTER);
			point = layout.getLocation(offsetInLine, true);
		} else {
			switch (caretAlignment) {
				case OFFSET_LEADING:
					point = layout.getLocation(offsetInLine, false);
					break;
				case PREVIOUS_OFFSET_TRAILING:
				default:
					boolean lineBegin = offsetInLine == 0;
					// If word wrap is enabled, we should also consider offsets
					// of wrapped line parts as line begin and do NOT go back.
					// This prevents clients to jump one line higher than
					// expected, see bug 488172.
					// Respect caretAlignment at the caretOffset, unless there's
					// a non-empty selection, see bug 488172 comment 6.
					if (wordWrap && !lineBegin && (Arrays.binarySearch(caretOffsets, offset) < 0 || Arrays.stream(selection).allMatch(p -> p.x == p.y))) {
						int[] offsets = layout.getLineOffsets();
						for (int i : offsets) {
							if (i == offsetInLine) {
								lineBegin = true;
								break;
							}
						}
					}
					if (lineBegin) {
						point = layout.getLocation(offsetInLine, false);
					} else {
						offsetInLine = layout.getPreviousOffset(offsetInLine, SWT.MOVEMENT_CLUSTER);
						point = layout.getLocation(offsetInLine, true);
					}
					break;
			}
		}
	} else {
		point = new Point(layout.getIndent(), layout.getVerticalIndent());
	}
	renderer.disposeTextLayout(layout);
	point.x += leftMargin - horizontalScrollOffset;
	point.y += getLinePixel(lineIndex);
	return point;
}
/**
 * Inserts a string.  The old selection is replaced with the new text.
 *
 * @param string the string
 * @see #replaceTextRange(int,int,String)
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when string is null</li>
 * </ul>
 */
public void insert(String string) {
	checkWidget();
	if (string == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	if (blockSelection) {
		insertBlockSelectionText(string, false);
	} else {
		Point sel = getSelectionRange();
		replaceTextRange(sel.x, sel.y, string);
	}
}
int insertBlockSelectionText(String text, boolean fillWithSpaces) {
	int lineCount = 1;
	for (int i = 0; i < text.length(); i++) {
		char ch = text.charAt(i);
		if (ch == '\n' || ch == '\r') {
			lineCount++;
			if (ch == '\r' && i + 1 < text.length() && text.charAt(i + 1) == '\n') {
				i++;
			}
		}
	}
	String[] lines = new String[lineCount];
	int start = 0;
	lineCount = 0;
	for (int i = 0; i < text.length(); i++) {
		char ch = text.charAt(i);
		if (ch == '\n' || ch == '\r') {
			lines[lineCount++] = text.substring(start, i);
			if (ch == '\r' && i + 1 < text.length() && text.charAt(i + 1) == '\n') {
				i++;
			}
			start = i + 1;
		}
	}
	lines[lineCount++] = text.substring(start);
	if (fillWithSpaces) {
		int maxLength = 0;
		for (String line : lines) {
			int length = line.length();
			maxLength = Math.max(maxLength, length);
		}
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			int length = line.length();
			if (length < maxLength) {
				int numSpaces = maxLength - length;
				StringBuilder buffer = new StringBuilder(length + numSpaces);
				buffer.append(line);
				for (int j = 0; j < numSpaces; j++) buffer.append(' ');
				lines[i] = buffer.toString();
			}
		}
	}
	int firstLine, lastLine, left, right;
	if (blockXLocation != -1) {
		Rectangle rect = getBlockSelectionPosition();
		firstLine = rect.y;
		lastLine = rect.height;
		left = rect.x;
		right = rect.width;
	} else {
		firstLine = lastLine = getFirstCaretLine();
		left = right = getPointAtOffset(caretOffsets[0]).x;
	}
	start = caretOffsets[0];
	int caretLine = getFirstCaretLine();
	int index = 0, lineIndex = firstLine;
	while (lineIndex <= lastLine) {
		String string = index < lineCount ? lines[index++] : "";
		int lineStart = sendTextEvent(left, right, lineIndex, string, fillWithSpaces);
		if (lineIndex == caretLine) start = lineStart;
		lineIndex++;
	}
	while (index < lineCount) {
		int lineStart = sendTextEvent(left, left, lineIndex, lines[index++], fillWithSpaces);
		if (lineIndex == caretLine) start = lineStart;
		lineIndex++;
	}
	return start;
}
void insertBlockSelectionText(char key, int action) {
	if (key == SWT.CR || key == SWT.LF) return;
	Rectangle rect = getBlockSelectionPosition();
	int firstLine = rect.y;
	int lastLine = rect.height;
	int left = rect.x;
	int right = rect.width;
	int[] trailing = new int[1];
	int offset = 0, delta = 0;
	String text = key != 0 ? new String(new char[] {key}) : "";
	int length = text.length();
	for (int lineIndex = firstLine; lineIndex <= lastLine; lineIndex++) {
		String line = content.getLine(lineIndex);
		int lineOffset = content.getOffsetAtLine(lineIndex);
		int lineEndOffset = lineOffset + line.length();
		int linePixel = getLinePixel(lineIndex);
		int start = getOffsetAtPoint(left, linePixel, trailing, true);
		boolean outOfLine = start == -1;
		if (outOfLine) {
			start = left < leftMargin ? lineOffset : lineEndOffset;
		} else {
			start += trailing[0];
		}
		int end = getOffsetAtPoint(right, linePixel, trailing, true);
		if (end == -1) {
			end = right < leftMargin ? lineOffset : lineEndOffset;
		} else {
			end += trailing[0];
		}
		if (start > end) {
			int temp = start;
			start = end;
			end = temp;
		}
		if (start == end && !outOfLine) {
			switch (action) {
				case ST.DELETE_PREVIOUS:
					if (start > lineOffset) start = getClusterPrevious(start, lineIndex);
					break;
				case ST.DELETE_NEXT:
					if (end < lineEndOffset) end = getClusterNext(end, lineIndex);
					break;
			}
		}
		if (outOfLine) {
			if (line.length() >= delta) {
				delta = line.length();
				offset = lineEndOffset + length;
			}
		} else {
			offset = start + length;
			delta = content.getCharCount();
		}
		Event event = new Event();
		event.text = text;
		event.start = start;
		event.end = end;
		sendKeyEvent(event);
	}
	int x = getPointAtOffset(offset).x;
	int verticalScrollOffset = getVerticalScrollOffset();
	setBlockSelectionLocation(x, blockYAnchor - verticalScrollOffset, x, blockYLocation - verticalScrollOffset, false);
}
/**
 * Creates content change listeners and set the default content model.
 */
void installDefaultContent() {
	textChangeListener = new TextChangeListener() {
		@Override
		public void textChanging(TextChangingEvent event) {
			handleTextChanging(event);
		}
		@Override
		public void textChanged(TextChangedEvent event) {
			handleTextChanged(event);
		}
		@Override
		public void textSet(TextChangedEvent event) {
			handleTextSet(event);
		}
	};
	content = new DefaultContent();
	content.addTextChangeListener(textChangeListener);
}
/**
 * Adds event listeners
 */
void installListeners() {
	ScrollBar verticalBar = getVerticalBar();
	ScrollBar horizontalBar = getHorizontalBar();

	listener = event -> {
		switch (event.type) {
			case SWT.Dispose: handleDispose(event); break;
			case SWT.KeyDown: handleKeyDown(event); break;
			case SWT.KeyUp: handleKeyUp(event); break;
			case SWT.MenuDetect: handleMenuDetect(event); break;
			case SWT.MouseDown: handleMouseDown(event); break;
			case SWT.MouseUp: handleMouseUp(event); break;
			case SWT.MouseMove: handleMouseMove(event); break;
			case SWT.Paint: handlePaint(event); break;
			case SWT.Resize: handleResize(event); break;
			case SWT.Traverse: handleTraverse(event); break;
		}
	};
	addListener(SWT.Dispose, listener);
	addListener(SWT.KeyDown, listener);
	addListener(SWT.KeyUp, listener);
	addListener(SWT.MenuDetect, listener);
	addListener(SWT.MouseDown, listener);
	addListener(SWT.MouseUp, listener);
	addListener(SWT.MouseMove, listener);
	addListener(SWT.Paint, listener);
	addListener(SWT.Resize, listener);
	addListener(SWT.Traverse, listener);
	ime.addListener(SWT.ImeComposition, event -> {
		if (!editable) {
			event.doit = false;
			event.start = 0;
			event.end = 0;
			event.text = "";
			return;
		}

		switch (event.detail) {
			case SWT.COMPOSITION_SELECTION: handleCompositionSelection(event); break;
			case SWT.COMPOSITION_CHANGED: handleCompositionChanged(event); break;
			case SWT.COMPOSITION_OFFSET: handleCompositionOffset(event); break;
		}
	});
	if (verticalBar != null) {
		verticalBar.addListener(SWT.Selection, this::handleVerticalScroll);
	}
	if (horizontalBar != null) {
		horizontalBar.addListener(SWT.Selection, this::handleHorizontalScroll);
	}
}
void internalRedrawRange(int start, int length) {
	if (length <= 0) return;
	int end = start + length;
	int startLine = content.getLineAtOffset(start);
	int endLine = content.getLineAtOffset(end);
	int partialBottomIndex = getPartialBottomIndex();
	int partialTopIndex = getPartialTopIndex();
	if (startLine > partialBottomIndex || endLine < partialTopIndex) {
		return;
	}
	if (partialTopIndex > startLine) {
		startLine = partialTopIndex;
		start = 0;
	} else {
		start -= content.getOffsetAtLine(startLine);
	}
	if (partialBottomIndex < endLine) {
		endLine = partialBottomIndex + 1;
		end = 0;
	} else {
		end -= content.getOffsetAtLine(endLine);
	}

	TextLayout layout = renderer.getTextLayout(startLine);
	int lineX = leftMargin - horizontalScrollOffset, startLineY = getLinePixel(startLine);
	int[] offsets = layout.getLineOffsets();
	int startIndex = layout.getLineIndex(Math.min(start, layout.getText().length()));

	/* Redraw end of line before start line if wrapped and start offset is first char */
	if (isWordWrap() && startIndex > 0 && offsets[startIndex] == start) {
		Rectangle rect = layout.getLineBounds(startIndex - 1);
		rect.x = rect.width;
		rect.width = clientAreaWidth - rightMargin - rect.x;
		rect.x += lineX;
		rect.y += startLineY;
		super.redraw(rect.x, rect.y, rect.width, rect.height, false);
	}

	if (startLine == endLine) {
		int endIndex = layout.getLineIndex(Math.min(end, layout.getText().length()));
		if (startIndex == endIndex) {
			/* Redraw rect between start and end offset if start and end offsets are in same wrapped line */
			Rectangle rect = layout.getBounds(start, end - 1);
			rect.x += lineX;
			rect.y += startLineY;
			super.redraw(rect.x, rect.y, rect.width, rect.height, false);
			renderer.disposeTextLayout(layout);
			return;
		}
	}

	/* Redraw start line from the start offset to the end of client area */
	Rectangle startRect = layout.getBounds(start, offsets[startIndex + 1] - 1);
	if (startRect.height == 0) {
		Rectangle bounds = layout.getLineBounds(startIndex);
		startRect.x = bounds.width;
		startRect.y = bounds.y;
		startRect.height = bounds.height;
	}
	startRect.x += lineX;
	startRect.y += startLineY;
	startRect.width = clientAreaWidth - rightMargin - startRect.x;
	super.redraw(startRect.x, startRect.y, startRect.width, startRect.height, false);

	/* Redraw end line from the beginning of the line to the end offset */
	if (startLine != endLine) {
		renderer.disposeTextLayout(layout);
		layout = renderer.getTextLayout(endLine);
		offsets = layout.getLineOffsets();
	}
	int endIndex = layout.getLineIndex(Math.min(end, layout.getText().length()));
	Rectangle endRect = layout.getBounds(offsets[endIndex], end - 1);
	if (endRect.height == 0) {
		Rectangle bounds = layout.getLineBounds(endIndex);
		endRect.y = bounds.y;
		endRect.height = bounds.height;
	}
	endRect.x += lineX;
	endRect.y += getLinePixel(endLine);
	super.redraw(endRect.x, endRect.y, endRect.width, endRect.height, false);
	renderer.disposeTextLayout(layout);

	/* Redraw all lines in between start and end line */
	int y = startRect.y + startRect.height;
	if (endRect.y > y) {
		super.redraw(leftMargin, y, clientAreaWidth - rightMargin - leftMargin, endRect.y - y, false);
	}
}
void handleCompositionOffset (Event event) {
	int[] trailing = new int [1];
	event.index = getOffsetAtPoint(event.x, event.y, trailing, true);
	event.count = trailing[0];
}
void handleCompositionSelection (Event event) {
	if (event.start != event.end) {
		int charCount = getCharCount();
		event.start = Math.max(0, Math.min(event.start, charCount));
		event.end = Math.max(0, Math.min(event.end, charCount));
		if (event.text != null) {
			setSelection(event.start, event.end);
		} else {
			event.text = getTextRange(event.start, event.end - event.start);
		}
	} else {
		event.start = selection[0].x;
		event.end = selection[0].y;
		event.text = getSelectionText();
	}
}
void handleCompositionChanged(Event event) {
	String text = event.text;
	int start = event.start;
	int end = event.end;
	int charCount = content.getCharCount();
	start = Math.min(start, charCount);
	end = Math.min(end, charCount);
	int length = text.length();
	if (length == ime.getCommitCount()) {
		content.replaceTextRange(start, end - start, "");
		setCaretOffsets(new int[] {ime.getCompositionOffset()}, SWT.DEFAULT);
		caretWidth = 0;
		caretDirection = SWT.NULL;
	} else {
		content.replaceTextRange(start, end - start, text);
		int alignment = SWT.DEFAULT;
		if (ime.getWideCaret()) {
			start = ime.getCompositionOffset();
			for (int caretOffset : caretOffsets) {
				int lineIndex = content.getLineAtOffset(caretOffset);
				int lineOffset = content.getOffsetAtLine(lineIndex);
				TextLayout layout = renderer.getTextLayout(lineIndex);
				caretWidth = layout.getBounds(start - lineOffset, start + length - 1 - lineOffset).width;
				renderer.disposeTextLayout(layout);
			}
			alignment = OFFSET_LEADING;
		}
		setCaretOffsets(new int[] {ime.getCaretOffset()}, alignment);
	}
	resetSelection();
	showCaret();
}
/**
 * Frees resources.
 */
void handleDispose(Event event) {
	removeListener(SWT.Dispose, listener);
	notifyListeners(SWT.Dispose, event);
	event.type = SWT.None;

	clipboard.dispose();
	if (renderer != null) {
		renderer.dispose();
		renderer = null;
	}
	if (content != null) {
		content.removeTextChangeListener(textChangeListener);
		content = null;
	}
	if (defaultCaret != null) {
		defaultCaret.dispose();
		defaultCaret = null;
	}
	if (leftCaretBitmap != null) {
		leftCaretBitmap.dispose();
		leftCaretBitmap = null;
	}
	if (rightCaretBitmap != null) {
		rightCaretBitmap.dispose();
		rightCaretBitmap = null;
	}
	if (carets != null) {
		for (Caret caret : carets) {
			if (caret != null) {
				caret.dispose();
			}
		}
		carets = null;
	}
	if (isBidiCaret()) {
		BidiUtil.removeLanguageListener(this);
	}
	selectionBackground = null;
	selectionForeground = null;
	marginColor = null;
	textChangeListener = null;
	selection = null;
	doubleClickSelection = null;
	keyActionMap = null;
	background = null;
	foreground = null;
	clipboard = null;
	tabs = null;
}
/**
 * Scrolls the widget horizontally.
 */
void handleHorizontalScroll(Event event) {
	int scrollPixel = getHorizontalBar().getSelection() - horizontalScrollOffset;
	scrollHorizontal(scrollPixel, false);
}
/**
 * If an action has been registered for the key stroke execute the action.
 * Otherwise, if a character has been entered treat it as new content.
 *
 * @param event keyboard event
 */
void handleKey(Event event) {
	int action;
	caretAlignment = PREVIOUS_OFFSET_TRAILING;
	if (event.keyCode != 0) {
		// special key pressed (e.g., F1)
		action = getKeyBinding(event.keyCode | event.stateMask);
	} else {
		// character key pressed
		action = getKeyBinding(event.character | event.stateMask);
		if (action == SWT.NULL) {
			// see if we have a control character
			if ((event.stateMask & SWT.CTRL) != 0 && event.character <= 31) {
				// get the character from the CTRL+char sequence, the control
				// key subtracts 64 from the value of the key that it modifies
				int c = event.character + 64;
				action = getKeyBinding(c | event.stateMask);
			}
		}
	}
	if (action == SWT.NULL) {
		boolean ignore = false;

		if (IS_MAC) {
			// Ignore accelerator key combinations (we do not want to
			// insert a character in the text in this instance).
			ignore = (event.stateMask & (SWT.COMMAND | SWT.CTRL)) != 0;
		} else {
			// Ignore accelerator key combinations (we do not want to
			// insert a character in the text in this instance). Don't
			// ignore CTRL+ALT combinations since that is the Alt Gr
			// key on some keyboards.  See bug 20953.
			ignore = event.stateMask == SWT.ALT ||
					event.stateMask == SWT.CTRL ||
					event.stateMask == (SWT.ALT | SWT.SHIFT) ||
					event.stateMask == (SWT.CTRL | SWT.SHIFT);
		}
		// -ignore anything below SPACE except for line delimiter keys and tab.
		// -ignore DEL
		if (!ignore && event.character > 31 && event.character != SWT.DEL ||
			event.character == SWT.CR || event.character == SWT.LF ||
			event.character == TAB) {
			doContent(event.character);
			update();
		}
	} else {
		invokeAction(action);
	}
}
/**
 * If a VerifyKey listener exists, verify that the key that was entered
 * should be processed.
 *
 * @param event keyboard event
 */
void handleKeyDown(Event event) {
	if (clipboardSelection == null) {
		clipboardSelection = new Point(selection[0].x, selection[0].y);
	}
	newOrientation = SWT.NONE;
	event.stateMask &= SWT.MODIFIER_MASK;

	Event verifyEvent = new Event();
	verifyEvent.character = event.character;
	verifyEvent.keyCode = event.keyCode;
	verifyEvent.keyLocation = event.keyLocation;
	verifyEvent.stateMask = event.stateMask;
	verifyEvent.doit = event.doit;
	notifyListeners(ST.VerifyKey, verifyEvent);
	if (verifyEvent.doit) {
		if ((event.stateMask & SWT.MODIFIER_MASK) == SWT.CTRL && event.keyCode == SWT.SHIFT && isBidiCaret()) {
			newOrientation = event.keyLocation == SWT.LEFT ? SWT.LEFT_TO_RIGHT : SWT.RIGHT_TO_LEFT;
		}
		handleKey(event);
	}
}
/**
 * Update the Selection Clipboard.
 *
 * @param event keyboard event
 */
void handleKeyUp(Event event) {
	if (clipboardSelection != null) {
		if (clipboardSelection.x != selection[0].x || clipboardSelection.y != selection[0].y) {
			copySelection(DND.SELECTION_CLIPBOARD);
		}
	}
	clipboardSelection = null;

	if (newOrientation != SWT.NONE) {
		if (newOrientation != getOrientation()) {
			Event e = new Event();
			e.doit = true;
			notifyListeners(SWT.OrientationChange, e);
			if (e.doit) {
				setOrientation(newOrientation);
			}
		}
		newOrientation = SWT.NONE;
	}
}
/**
 * Update the event location for focus-based context menu triggers.
 *
 * @param event menu detect event
 */
void handleMenuDetect(Event event) {
	if (event.detail == SWT.MENU_KEYBOARD) {
		Point point = getDisplay().map(this, null, getPointAtOffset(caretOffsets[0]));
		event.x = point.x;
		event.y = point.y + getLineHeight(caretOffsets[0]);
	}
}
/**
 * Updates the caret location and selection if mouse button 1 has been
 * pressed.
 */
void handleMouseDown(Event event) {
	//force focus (object support)
	forceFocus();

	//drag detect
	if (dragDetect && checkDragDetect(event)) return;

	//paste clipboard selection
	if (event.button == 2) {
		// On GTK, if mouseNavigator is enabled we have to distinguish a short middle-click (to paste content) from
		// a long middle-click (mouse navigation started)
		if (IS_GTK && mouseNavigator != null) {
			middleClickPressed = true;
			getDisplay().timerExec(200, ()->{
				boolean click = middleClickPressed;
				middleClickPressed = false;
				if (click && mouseNavigator !=null) {
					mouseNavigator.onMouseDown(event);
				} else {
					pasteOnMiddleClick(event);
				}
			});
			return;
		} else {
			pasteOnMiddleClick(event);
		}
	}

	//set selection
	if ((event.button != 1) || (IS_MAC && (event.stateMask & SWT.MOD4) != 0)) {
		return;
	}
	clickCount = event.count;
	boolean addSelection = (event.stateMask & SWT.MOD3) != 0;
	if (clickCount == 1) {
		if (addSelection && !blockSelection) {
			int offset = getOffsetAtPoint(event.x, event.y, null);
			addSelection(offset, 0);
			sendSelectionEvent();
		} else {
			boolean expandSelection = (event.stateMask & SWT.MOD2) != 0;
			doMouseLocationChange(event.x, event.y, expandSelection);
		}
	} else {
		if (doubleClickEnabled) {
			boolean wordSelect = (clickCount & 1) == 0;
			int offset = getOffsetAtPoint(event.x, event.y, null);
			int lineIndex = content.getLineAtOffset(offset);
			int lineOffset = content.getOffsetAtLine(lineIndex);
			if (wordSelect) {
				String line = content.getLine(lineIndex);
				int lineLength = line.length();
				int min = blockSelection ? lineOffset : 0;
				int max = blockSelection ? lineOffset + lineLength : content.getCharCount();
				final Point offsetPoint = getPointAtOffset(offset);
				if (event.x > offsetPoint.x
						&& offset < Math.min(max, lineOffset + lineLength) // Not beyond EOL
						&& !Character.isWhitespace(line.charAt(offset - lineOffset))) { // Not on whitespace
					offset++;
				}
				int start = Math.max(min, getWordPrevious(offset, SWT.MOVEMENT_WORD_START));
				int end = Math.min(max, getWordNext(start, SWT.MOVEMENT_WORD_END));
				int[] regions = new int[2];
				if (addSelection) {
					int[] current = getSelectionRanges();
					regions = Arrays.copyOf(current, current.length + 2);
				}
				regions[regions.length - 2] = start;
				regions[regions.length - 1] = end - start;
				setSelection(regions, false, true);
				sendSelectionEvent();
			} else {
				if (blockSelection) {
					setBlockSelectionLocation(leftMargin, event.y, clientAreaWidth - rightMargin, event.y, true);
				} else {
					int lineEnd = content.getCharCount();
					if (lineIndex + 1 < content.getLineCount()) {
						lineEnd = content.getOffsetAtLine(lineIndex + 1);
					}
					int[] regions = new int[2];
					if (addSelection) {
						int[] current = getSelectionRanges();
						regions = Arrays.copyOf(current, current.length + 2);
					}
					regions[regions.length - 2] = lineOffset;
					regions[regions.length - 1] = lineEnd - lineOffset;
					setSelection(regions, false, false);
					sendSelectionEvent();
				}
			}
			doubleClickSelection = new Point(selection[0].x, selection[0].y);
			showCaret();
		}
	}
}

void addSelection(int offset, int length) {
	int[] ranges = getSelectionRanges();
	ranges = Arrays.copyOf(ranges, ranges.length + 2);
	ranges[ranges.length - 2] = offset;
	ranges[ranges.length - 1] = length;
	setSelection(ranges, true, true);
}

/**
 * Updates the caret location and selection if mouse button 1 is pressed
 * during the mouse move.
 */
void handleMouseMove(Event event) {
	if (clickCount > 0) {
		update();
		doAutoScroll(event);
		doMouseLocationChange(event.x, event.y, true);
	}
	if (renderer.hasLinks) {
		doMouseLinkCursor(event.x, event.y);
	}
}
/**
 * Autoscrolling ends when the mouse button is released.
 */
void handleMouseUp(Event event) {
	middleClickPressed = false;
	clickCount = 0;
	endAutoScroll();
	if (event.button == 1) {
		copySelection(DND.SELECTION_CLIPBOARD);
	}
}
/**
 * Renders the invalidated area specified in the paint event.
 *
 * @param event paint event
 */
void handlePaint(Event event) {
	if (event.width == 0 || event.height == 0) return;
	if (clientAreaWidth == 0 || clientAreaHeight == 0) return;

	int startLine = getLineIndex(event.y);
	int y = getLinePixel(startLine);
	int endY = event.y + event.height;
	GC gc = event.gc;
	Color background = getBackground();
	Color foreground = getForeground();
	if (endY > 0) {
		int lineCount = isSingleLine() ? 1 : content.getLineCount();
		int x = leftMargin - horizontalScrollOffset;
		for (int i = startLine; y < endY && i < lineCount; i++) {
			y += renderer.drawLine(i, x, y, gc, background, foreground);
		}
		if (y < endY) {
			gc.setBackground(background);
			drawBackground(gc, 0, y, clientAreaWidth, endY - y);
		}
	}
	if (blockSelection && blockXLocation != -1) {
		gc.setBackground(getSelectionBackground());
		Rectangle rect = getBlockSelectionRectangle();
		gc.drawRectangle(rect.x, rect.y, Math.max(1, rect.width - 1), Math.max(1, rect.height - 1));
		gc.setAdvanced(true);
		if (gc.getAdvanced()) {
			gc.setAlpha(100);
			gc.fillRectangle(rect);
			gc.setAdvanced(false);
		}
	}
	if(carets != null) {
		for (int i = 1; i < carets.length; i++) { //skip 1st caret that's already drawn
			Caret caret = carets[i];
			if (caret.isVisible()) {
				if (caret.getImage() != null) {
					gc.drawImage(caret.getImage(), caret.getBounds().x, caret.getBounds().y);
				} else {
					gc.drawRectangle(caret.getBounds().x, caret.getBounds().y, caret.getBounds().width, getLineHeight(caretOffsets[i]));
				}
			}
		}
	}

	// fill the margin background
	gc.setBackground(marginColor != null ? marginColor : background);
	if (topMargin > 0) {
		drawBackground(gc, 0, 0, clientAreaWidth, topMargin);
	}
	if (bottomMargin > 0) {
		drawBackground(gc, 0, clientAreaHeight - bottomMargin, clientAreaWidth, bottomMargin);
	}
	if (leftMargin - alignmentMargin > 0) {
		drawBackground(gc, 0, 0, leftMargin - alignmentMargin, clientAreaHeight);
	}
	if (rightMargin > 0) {
		drawBackground(gc, clientAreaWidth - rightMargin, 0, rightMargin, clientAreaHeight);
	}
}
/**
 * Recalculates the scroll bars. Rewraps all lines when in word
 * wrap mode.
 *
 * @param event resize event
 */
void handleResize(Event event) {
	int oldHeight = clientAreaHeight;
	int oldWidth = clientAreaWidth;
	Rectangle clientArea = getClientArea();
	clientAreaHeight = clientArea.height;
	clientAreaWidth = clientArea.width;
	if (!alwaysShowScroll && ignoreResize != 0) return;

	redrawMargins(oldHeight, oldWidth);
	if (wordWrap) {
		if (oldWidth != clientAreaWidth) {
			renderer.reset(0, content.getLineCount());
			verticalScrollOffset = -1;
			renderer.calculateIdle();
			super.redraw();
		}
		if (oldHeight != clientAreaHeight) {
			if (oldHeight == 0) topIndexY = 0;
			setScrollBars(true);
		}
		setCaretLocations();
	} else  {
		renderer.calculateClientArea();
		setScrollBars(true);
		claimRightFreeSpace();
		// StyledText allows any value for horizontalScrollOffset when clientArea is zero
		// in setHorizontalPixel() and setHorisontalOffset(). Fixes bug 168429.
		if (clientAreaWidth != 0) {
			ScrollBar horizontalBar = getHorizontalBar();
			if (horizontalBar != null && horizontalBar.getVisible()) {
				if (horizontalScrollOffset != horizontalBar.getSelection()) {
					horizontalBar.setSelection(horizontalScrollOffset);
					horizontalScrollOffset = horizontalBar.getSelection();
				}
			}
		}
	}
	updateCaretVisibility();
	claimBottomFreeSpace();
	setAlignment();
	//TODO FIX TOP INDEX DURING RESIZE
//	if (oldHeight != clientAreaHeight || wordWrap) {
//		calculateTopIndex(0);
//	}
}
/**
 * Updates the caret position and selection and the scroll bars to reflect
 * the content change.
 */
void handleTextChanged(TextChangedEvent event) {
	int offset = ime.getCompositionOffset();
	if (offset != -1 && lastTextChangeStart < offset) {
		ime.setCompositionOffset(offset + lastTextChangeNewCharCount - lastTextChangeReplaceCharCount);
	}
	int firstLine = content.getLineAtOffset(lastTextChangeStart);
	resetCache(firstLine, 0);
	if (!isFixedLineHeight() && topIndex > firstLine) {
		topIndex = firstLine;
		if (topIndex < 0) {
			// TODO: This logging is in place to determine why topIndex is getting set to negative values.
			// It should be deleted once we fix the root cause of this issue. See bug 487254 for details.
			System.err.println("StyledText: topIndex was " + topIndex
					+ ", lastTextChangeStart = " + lastTextChangeStart
					+ ", content.getClass() = " + content.getClass()
					);
			topIndex = 0;
		}
		topIndexY = 0;
		super.redraw();
	} else {
		int lastLine = firstLine + lastTextChangeNewLineCount;
		int firstLineTop = getLinePixel(firstLine);
		int newLastLineBottom = getLinePixel(lastLine + 1);
		if (lastLineBottom != newLastLineBottom) {
			super.redraw();
		} else {
			super.redraw(0, firstLineTop, clientAreaWidth, newLastLineBottom - firstLineTop, false);
			redrawLinesBullet(renderer.redrawLines);
		}
	}
	renderer.redrawLines = null;
	// update selection/caret location after styles have been changed.
	// otherwise any text measuring could be incorrect
	//
	// also, this needs to be done after all scrolling. Otherwise,
	// selection redraw would be flushed during scroll which is wrong.
	// in some cases new text would be drawn in scroll source area even
	// though the intent is to scroll it.
	if (!(blockSelection && blockXLocation != -1)) {
		updateSelection(lastTextChangeStart, lastTextChangeReplaceCharCount, lastTextChangeNewCharCount);
	}
	if (lastTextChangeReplaceLineCount > 0 || wordWrap || visualWrap) {
		claimBottomFreeSpace();
	}
	if (lastTextChangeReplaceCharCount > 0) {
		claimRightFreeSpace();
	}

	sendAccessibleTextChanged(lastTextChangeStart, lastTextChangeNewCharCount, 0);
	lastCharCount += lastTextChangeNewCharCount;
	lastCharCount -= lastTextChangeReplaceCharCount;
	setAlignment();
}
/**
 * Updates the screen to reflect a pending content change.
 *
 * @param event .start the start offset of the change
 * @param event .newText text that is going to be inserted or empty String
 *	if no text will be inserted
 * @param event .replaceCharCount length of text that is going to be replaced
 * @param event .newCharCount length of text that is going to be inserted
 * @param event .replaceLineCount number of lines that are going to be replaced
 * @param event .newLineCount number of new lines that are going to be inserted
 */
void handleTextChanging(TextChangingEvent event) {
	if (event.replaceCharCount < 0) {
		event.start += event.replaceCharCount;
		event.replaceCharCount *= -1;
	}
	lastTextChangeStart = event.start;
	lastTextChangeNewLineCount = event.newLineCount;
	lastTextChangeNewCharCount = event.newCharCount;
	lastTextChangeReplaceLineCount = event.replaceLineCount;
	lastTextChangeReplaceCharCount = event.replaceCharCount;
	int lineIndex = content.getLineAtOffset(event.start);
	int srcY = getLinePixel(lineIndex + event.replaceLineCount + 1);
	int destY = getLinePixel(lineIndex + 1) + event.newLineCount * renderer.getLineHeight();
	lastLineBottom = destY;
	if (srcY < 0 && destY < 0) {
		lastLineBottom += srcY - destY;
		verticalScrollOffset += destY - srcY;
		calculateTopIndex(destY - srcY);
		setScrollBars(true);
	} else {
		scrollText(srcY, destY);
	}
	sendAccessibleTextChanged(lastTextChangeStart, 0, lastTextChangeReplaceCharCount);
	renderer.textChanging(event);

	// Update the caret offset if it is greater than the length of the content.
	// This is necessary since style range API may be called between the
	// handleTextChanging and handleTextChanged events and this API sets the
	// caretOffset.
	int newEndOfText = content.getCharCount() - event.replaceCharCount + event.newCharCount;
	int tooBigOffsets = 0;
	while (tooBigOffsets < caretOffsets.length && caretOffsets[caretOffsets.length - 1 - tooBigOffsets] > newEndOfText) {
		tooBigOffsets++;
	}
	if (tooBigOffsets != 0) {
		int[] newCaretOffsets = Arrays.copyOf(caretOffsets, caretOffsets.length - tooBigOffsets + 1);
		newCaretOffsets[newCaretOffsets.length - 1] = newEndOfText;
		setCaretOffsets(newCaretOffsets, SWT.DEFAULT);
	}
}
/**
 * Called when the widget content is set programmatically, overwriting
 * the old content. Resets the caret position, selection and scroll offsets.
 * Recalculates the content width and scroll bars. Redraws the widget.
 *
 * @param event text change event.
 */
void handleTextSet(TextChangedEvent event) {
	reset();
	int newCharCount = getCharCount();
	sendAccessibleTextChanged(0, newCharCount, lastCharCount);
	lastCharCount = newCharCount;
	setAlignment();
}
/**
 * Called when a traversal key is pressed.
 * Allow tab next traversal to occur when the widget is in single
 * line mode or in multi line and non-editable mode .
 * When in editable multi line mode we want to prevent the tab
 * traversal and receive the tab key event instead.
 *
 * @param event the event
 */
void handleTraverse(Event event) {
	switch (event.detail) {
		case SWT.TRAVERSE_ESCAPE:
		case SWT.TRAVERSE_PAGE_NEXT:
		case SWT.TRAVERSE_PAGE_PREVIOUS:
			event.doit = true;
			break;
		case SWT.TRAVERSE_RETURN:
		case SWT.TRAVERSE_TAB_NEXT:
		case SWT.TRAVERSE_TAB_PREVIOUS:
			if ((getStyle() & SWT.SINGLE) != 0) {
				event.doit = true;
			} else {
				if (!editable || (event.stateMask & SWT.MODIFIER_MASK) != 0) {
					event.doit = true;
				}
			}
			break;
	}
}
/**
 * Scrolls the widget vertically.
 */
void handleVerticalScroll(Event event) {
	int scrollPixel = getVerticalBar().getSelection() - getVerticalScrollOffset();
	scrollVertical(scrollPixel, false);
}
/**
 * Add accessibility support for the widget.
 */
void initializeAccessible() {
	acc = getAccessible();

	accAdapter = new AccessibleAdapter() {
		@Override
		public void getName (AccessibleEvent e) {
			String name = null;
			String text = getAssociatedLabel ();
			if (text != null) {
				name = stripMnemonic (text);
			}
			e.result = name;
		}
		@Override
		public void getHelp(AccessibleEvent e) {
			e.result = getToolTipText();
		}
		@Override
		public void getKeyboardShortcut(AccessibleEvent e) {
			String shortcut = null;
			String text = getAssociatedLabel ();
			if (text != null) {
				char mnemonic = _findMnemonic (text);
				if (mnemonic != '\0') {
					shortcut = "Alt+"+mnemonic; //$NON-NLS-1$
				}
			}
			e.result = shortcut;
		}
	};
	acc.addAccessibleListener(accAdapter);

	accTextExtendedAdapter = new AccessibleTextExtendedAdapter() {
		@Override
		public void getCaretOffset(AccessibleTextEvent e) {
			e.offset = StyledText.this.getCaretOffset();
		}
		@Override
		public void setCaretOffset(AccessibleTextEvent e) {
			StyledText.this.setCaretOffset(e.offset);
			e.result = ACC.OK;
		}
		@Override
		public void getSelectionRange(AccessibleTextEvent e) {
			Point selection = StyledText.this.getSelectionRange();
			e.offset = selection.x;
			e.length = selection.y;
		}
		@Override
		public void addSelection(AccessibleTextEvent e) {
			StyledText st = StyledText.this;
			Point point = st.getSelection();
			if (point.x == point.y) {
				int end = e.end;
				if (end == -1) end = st.getCharCount();
				st.setSelection(e.start, end);
				e.result = ACC.OK;
			}
		}
		@Override
		public void getSelection(AccessibleTextEvent e) {
			StyledText st = StyledText.this;
			if (st.blockSelection && st.blockXLocation != -1) {
				Rectangle rect = st.getBlockSelectionPosition();
				int lineIndex = rect.y + e.index;
				int linePixel = st.getLinePixel(lineIndex);
				e.ranges = getRanges(rect.x, linePixel, rect.width, linePixel);
				if (e.ranges.length > 0) {
					e.start = e.ranges[0];
					e.end = e.ranges[e.ranges.length - 1];
				}
			} else {
				if (e.index == 0) {
					Point point = st.getSelection();
					e.start = point.x;
					e.end = point.y;
					if (e.start > e.end) {
						int temp = e.start;
						e.start = e.end;
						e.end = temp;
					}
				}
			}
		}
		@Override
		public void getSelectionCount(AccessibleTextEvent e) {
			StyledText st = StyledText.this;
			if (st.blockSelection && st.blockXLocation != -1) {
				Rectangle rect = st.getBlockSelectionPosition();
				e.count = rect.height - rect.y + 1;
			} else {
				Point point = st.getSelection();
				e.count = point.x == point.y ? 0 : 1;
			}
		}
		@Override
		public void removeSelection(AccessibleTextEvent e) {
			StyledText st = StyledText.this;
			if (e.index == 0) {
				if (st.blockSelection) {
					st.clearBlockSelection(true, false);
				} else {
					st.clearSelection(false);
				}
				e.result = ACC.OK;
			}
		}
		@Override
		public void setSelection(AccessibleTextEvent e) {
			if (e.index != 0) return;
			StyledText st = StyledText.this;
			Point point = st.getSelection();
			if (point.x == point.y) return;
			int end = e.end;
			if (end == -1) end = st.getCharCount();
			st.setSelection(e.start, end);
			e.result = ACC.OK;
		}
		@Override
		public void getCharacterCount(AccessibleTextEvent e) {
			e.count = StyledText.this.getCharCount();
		}
		@Override
		public void getOffsetAtPoint(AccessibleTextEvent e) {
			StyledText st = StyledText.this;
			Point point = new Point (e.x, e.y);
			Display display = st.getDisplay();
			point = display.map(null, st, point);
			e.offset = st.getOffsetAtPoint(point.x, point.y, null, true);
		}
		@Override
		public void getTextBounds(AccessibleTextEvent e) {
			StyledText st = StyledText.this;
			int start = e.start;
			int end = e.end;
			int contentLength = st.getCharCount();
			start = Math.max(0, Math.min(start, contentLength));
			end = Math.max(0, Math.min(end, contentLength));
			if (start > end) {
				int temp = start;
				start = end;
				end = temp;
			}
			int startLine = st.getLineAtOffset(start);
			int endLine = st.getLineAtOffset(end);
			Rectangle[] rects = new Rectangle[endLine - startLine + 1];
			Rectangle bounds = null;
			int index = 0;
			Display display = st.getDisplay();
			for (int lineIndex = startLine; lineIndex <= endLine; lineIndex++) {
				Rectangle rect = new Rectangle(0, 0, 0, 0);
				rect.y = st.getLinePixel(lineIndex);
				rect.height = st.renderer.getLineHeight(lineIndex);
				if (lineIndex == startLine) {
					rect.x = st.getPointAtOffset(start).x;
				} else {
					rect.x = st.leftMargin - st.horizontalScrollOffset;
				}
				if (lineIndex == endLine) {
					rect.width = st.getPointAtOffset(end).x - rect.x;
				} else {
					TextLayout layout = st.renderer.getTextLayout(lineIndex);
					rect.width = layout.getBounds().width - rect.x;
					st.renderer.disposeTextLayout(layout);
				}
				rects [index++] = rect = display.map(st, null, rect);
				if (bounds == null) {
					bounds = new Rectangle(rect.x, rect.y, rect.width, rect.height);
				} else {
					bounds.add(rect);
				}
			}
			e.rectangles = rects;
			if (bounds != null) {
				e.x = bounds.x;
				e.y = bounds.y;
				e.width = bounds.width;
				e.height = bounds.height;
			}
		}
		int[] getRanges(int left, int top, int right, int bottom) {
			StyledText st = StyledText.this;
			int lineStart = st.getLineIndex(top);
			int lineEnd = st.getLineIndex(bottom);
			int count = lineEnd - lineStart + 1;
			int[] ranges = new int [count * 2];
			int index = 0;
			for (int lineIndex = lineStart; lineIndex <= lineEnd; lineIndex++) {
				String line = st.content.getLine(lineIndex);
				int lineOffset = st.content.getOffsetAtLine(lineIndex);
				int lineEndOffset = lineOffset + line.length();
				int linePixel = st.getLinePixel(lineIndex);
				int start = st.getOffsetAtPoint(left, linePixel, null, true);
				if (start == -1) {
					start = left < st.leftMargin ? lineOffset : lineEndOffset;
				}
				int[] trailing = new int[1];
				int end = st.getOffsetAtPoint(right, linePixel, trailing, true);
				if (end == -1) {
					end = right < st.leftMargin ? lineOffset : lineEndOffset;
				} else {
					end += trailing[0];
				}
				if (start > end) {
					int temp = start;
					start = end;
					end = temp;
				}
				ranges[index++] = start;
				ranges[index++] = end;
			}
			return ranges;
		}
		@Override
		public void getRanges(AccessibleTextEvent e) {
			StyledText st = StyledText.this;
			Point point = new Point (e.x, e.y);
			Display display = st.getDisplay();
			point = display.map(null, st, point);
			e.ranges = getRanges(point.x, point.y, point.x + e.width, point.y + e.height);
			if (e.ranges.length > 0) {
				e.start = e.ranges[0];
				e.end = e.ranges[e.ranges.length - 1];
			}
		}
		@Override
		public void getText(AccessibleTextEvent e) {
			StyledText st = StyledText.this;
			int start = e.start;
			int end = e.end;
			int contentLength = st.getCharCount();
			if (end == -1) end = contentLength;
			start = Math.max(0, Math.min(start, contentLength));
			end = Math.max(0, Math.min(end, contentLength));
			if (start > end) {
				int temp = start;
				start = end;
				end = temp;
			}
			int count = e.count;
			switch (e.type) {
				case ACC.TEXT_BOUNDARY_ALL:
					//nothing to do
					break;
				case ACC.TEXT_BOUNDARY_CHAR: {
					int newCount = 0;
					if (count > 0) {
						while (count-- > 0) {
							int newEnd = st.getWordNext(end, SWT.MOVEMENT_CLUSTER);
							if (newEnd == contentLength) break;
							if (newEnd == end) break;
							end = newEnd;
							newCount++;
						}
						start = end;
						end = st.getWordNext(end, SWT.MOVEMENT_CLUSTER);
					} else {
						while (count++ < 0) {
							int newStart = st.getWordPrevious(start, SWT.MOVEMENT_CLUSTER);
							if (newStart == start) break;
							start = newStart;
							newCount--;
						}
						end = st.getWordNext(start, SWT.MOVEMENT_CLUSTER);
					}
					count = newCount;
					break;
				}
				case ACC.TEXT_BOUNDARY_WORD: {
					int newCount = 0;
					if (count > 0) {
						while (count-- > 0) {
							int newEnd = st.getWordNext(end, SWT.MOVEMENT_WORD_START, true);
							if (newEnd == end) break;
							newCount++;
							end = newEnd;
						}
						start = end;
						end = st.getWordNext(start, SWT.MOVEMENT_WORD_END, true);
					} else {
						if (st.getWordPrevious(Math.min(start + 1, contentLength), SWT.MOVEMENT_WORD_START, true) == start) {
							//start is a word start already
							count++;
						}
						while (count <= 0) {
							int newStart = st.getWordPrevious(start, SWT.MOVEMENT_WORD_START, true);
							if (newStart == start) break;
							count++;
							start = newStart;
							if (count != 0) newCount--;
						}
						if (count <= 0 && start == 0) {
							end = start;
						} else {
							end = st.getWordNext(start, SWT.MOVEMENT_WORD_END, true);
						}
					}
					count = newCount;
					break;
				}
				case ACC.TEXT_BOUNDARY_LINE:
					//TODO implement line
				case ACC.TEXT_BOUNDARY_PARAGRAPH:
				case ACC.TEXT_BOUNDARY_SENTENCE: {
					int offset = count > 0 ? end : start;
					int lineIndex = st.getLineAtOffset(offset) + count;
					lineIndex = Math.max(0, Math.min(lineIndex, st.getLineCount() - 1));
					start = st.getOffsetAtLine(lineIndex);
					String line = st.getLine(lineIndex);
					end = start + line.length();
					count = lineIndex - st.getLineAtOffset(offset);
					break;
				}
			}
			e.start = start;
			e.end = end;
			e.count = count;
			e.result = st.content.getTextRange(start, end - start);
		}
		@Override
		public void getVisibleRanges(AccessibleTextEvent e) {
			e.ranges = getRanges(leftMargin, topMargin, clientAreaWidth - rightMargin, clientAreaHeight - bottomMargin);
			if (e.ranges.length > 0) {
				e.start = e.ranges[0];
				e.end = e.ranges[e.ranges.length - 1];
			}
		}
		@Override
		public void scrollText(AccessibleTextEvent e) {
			StyledText st = StyledText.this;
			int topPixel = getTopPixel(), horizontalPixel = st.getHorizontalPixel();
			switch (e.type) {
				case ACC.SCROLL_TYPE_ANYWHERE:
				case ACC.SCROLL_TYPE_TOP_LEFT:
				case ACC.SCROLL_TYPE_LEFT_EDGE:
				case ACC.SCROLL_TYPE_TOP_EDGE: {
					Rectangle rect = st.getBoundsAtOffset(e.start);
					if (e.type != ACC.SCROLL_TYPE_TOP_EDGE) {
						horizontalPixel = horizontalPixel + rect.x - st.leftMargin;
					}
					if (e.type != ACC.SCROLL_TYPE_LEFT_EDGE) {
						topPixel = topPixel + rect.y - st.topMargin;
					}
					break;
				}
				case ACC.SCROLL_TYPE_BOTTOM_RIGHT:
				case ACC.SCROLL_TYPE_BOTTOM_EDGE:
				case ACC.SCROLL_TYPE_RIGHT_EDGE: {
					Rectangle rect = st.getBoundsAtOffset(e.end - 1);
					if (e.type != ACC.SCROLL_TYPE_BOTTOM_EDGE) {
						horizontalPixel = horizontalPixel - st.clientAreaWidth + rect.x + rect.width + st.rightMargin;
					}
					if (e.type != ACC.SCROLL_TYPE_RIGHT_EDGE) {
						topPixel = topPixel - st.clientAreaHeight + rect.y +rect.height + st.bottomMargin;
					}
					break;
				}
				case ACC.SCROLL_TYPE_POINT: {
					Point point = new Point(e.x, e.y);
					Display display = st.getDisplay();
					point = display.map(null, st, point);
					Rectangle rect = st.getBoundsAtOffset(e.start);
					topPixel = topPixel - point.y + rect.y;
					horizontalPixel = horizontalPixel - point.x + rect.x;
					break;
				}
			}
			st.setTopPixel(topPixel);
			st.setHorizontalPixel(horizontalPixel);
			e.result = ACC.OK;
		}
	};
	acc.addAccessibleTextListener(accTextExtendedAdapter);

	accEditableTextListener = new AccessibleEditableTextListener() {
		@Override
		public void setTextAttributes(AccessibleTextAttributeEvent e) {
			// This method must be implemented by the application
			e.result = ACC.OK;
		}
		@Override
		public void replaceText(AccessibleEditableTextEvent e) {
			StyledText st = StyledText.this;
			st.replaceTextRange(e.start, e.end - e.start, e.string);
			e.result = ACC.OK;
		}
		@Override
		public void pasteText(AccessibleEditableTextEvent e) {
			StyledText st = StyledText.this;
			st.setSelection(e.start);
			st.paste();
			e.result = ACC.OK;
		}
		@Override
		public void cutText(AccessibleEditableTextEvent e) {
			StyledText st = StyledText.this;
			st.setSelection(e.start, e.end);
			st.cut();
			e.result = ACC.OK;
		}
		@Override
		public void copyText(AccessibleEditableTextEvent e) {
			StyledText st = StyledText.this;
			st.setSelection(e.start, e.end);
			st.copy();
			e.result = ACC.OK;
		}
	};
	acc.addAccessibleEditableTextListener(accEditableTextListener);

	accAttributeAdapter = new AccessibleAttributeAdapter() {
		@Override
		public void getAttributes(AccessibleAttributeEvent e) {
			StyledText st = StyledText.this;
			e.leftMargin = st.getLeftMargin();
			e.topMargin = st.getTopMargin();
			e.rightMargin = st.getRightMargin();
			e.bottomMargin = st.getBottomMargin();
			e.tabStops = st.getTabStops();
			e.justify = st.getJustify();
			e.alignment = st.getAlignment();
			e.indent = st.getIndent();
		}
		@Override
		public void getTextAttributes(AccessibleTextAttributeEvent e) {
			StyledText st = StyledText.this;
			int contentLength = st.getCharCount();
			if (!isListening(ST.LineGetStyle) && st.renderer.styleCount == 0) {
				e.start = 0;
				e.end = contentLength;
				e.textStyle = new TextStyle(st.getFont(), st.foreground, st.background);
				return;
			}
			int offset = Math.max(0, Math.min(e.offset, contentLength - 1));
			int lineIndex = st.getLineAtOffset(offset);
			int lineOffset = st.getOffsetAtLine(lineIndex);
			int lineCount = st.getLineCount();
			offset = offset - lineOffset;

			TextLayout layout = st.renderer.getTextLayout(lineIndex);
			int lineLength = layout.getText().length();
			if (lineLength > 0) {
				e.textStyle = layout.getStyle(Math.max(0, Math.min(offset, lineLength - 1)));
			}

			// If no override info available, use defaults. Don't supply default colors, though.
			if (e.textStyle == null) {
				e.textStyle = new TextStyle(st.getFont(), st.foreground, st.background);
			} else {
				if (e.textStyle.foreground == null || e.textStyle.background == null || e.textStyle.font == null) {
					TextStyle textStyle = new TextStyle(e.textStyle);
					if (textStyle.foreground == null) textStyle.foreground = st.foreground;
					if (textStyle.background == null) textStyle.background = st.background;
					if (textStyle.font == null) textStyle.font = st.getFont();
					e.textStyle = textStyle;
				}
			}

			//offset at line delimiter case
			if (offset >= lineLength) {
				e.start = lineOffset + lineLength;
				if (lineIndex + 1 < lineCount) {
					e.end = st.getOffsetAtLine(lineIndex + 1);
				} else  {
					e.end = contentLength;
				}
				return;
			}

			int[] ranges = layout.getRanges();
			st.renderer.disposeTextLayout(layout);
			int index = 0;
			int end = 0;
			while (index < ranges.length) {
				int styleStart = ranges[index++];
				int styleEnd = ranges[index++];
				if (styleStart <= offset && offset <= styleEnd) {
					e.start = lineOffset + styleStart;
					e.end = lineOffset + styleEnd + 1;
					return;
				}
				if (styleStart > offset) {
					e.start = lineOffset + end;
					e.end = lineOffset + styleStart;
					return;
				}
				end = styleEnd + 1;
			}
			if (index == ranges.length) {
				e.start = lineOffset + end;
				if (lineIndex + 1 < lineCount) {
					e.end = st.getOffsetAtLine(lineIndex + 1);
				} else  {
					e.end = contentLength;
				}
			}
		}
	};
	acc.addAccessibleAttributeListener(accAttributeAdapter);

	accControlAdapter = new AccessibleControlAdapter() {
		@Override
		public void getRole(AccessibleControlEvent e) {
			e.detail = ACC.ROLE_TEXT;
		}
		@Override
		public void getState(AccessibleControlEvent e) {
			int state = 0;
			if (isEnabled()) state |= ACC.STATE_FOCUSABLE;
			if (isFocusControl()) state |= ACC.STATE_FOCUSED;
			if (!isVisible()) state |= ACC.STATE_INVISIBLE;
			if (!getEditable()) state |= ACC.STATE_READONLY;
			if (isSingleLine()) state |= ACC.STATE_SINGLELINE;
			else state |= ACC.STATE_MULTILINE;
			e.detail = state;
		}
		@Override
		public void getValue(AccessibleControlEvent e) {
			e.result = StyledText.this.getText();
		}
	};
	acc.addAccessibleControlListener(accControlAdapter);

	addListener(SWT.FocusIn, event -> acc.setFocus(ACC.CHILDID_SELF));
}

@Override
public void dispose() {
	/*
	 * Note: It is valid to attempt to dispose a widget more than once.
	 * Added check for this.
	 */
	if (!isDisposed()) {
		acc.removeAccessibleControlListener(accControlAdapter);
		acc.removeAccessibleAttributeListener(accAttributeAdapter);
		acc.removeAccessibleEditableTextListener(accEditableTextListener);
		acc.removeAccessibleTextListener(accTextExtendedAdapter);
		acc.removeAccessibleListener(accAdapter);
	}
	super.dispose();
}

/*
 * Return the Label immediately preceding the receiver in the z-order,
 * or null if none.
 */
String getAssociatedLabel () {
	Control[] siblings = getParent ().getChildren ();
	for (int i = 0; i < siblings.length; i++) {
		if (siblings [i] == StyledText.this) {
			if (i > 0) {
				Control sibling = siblings [i-1];
				if (sibling instanceof Label) return ((Label) sibling).getText();
				if (sibling instanceof CLabel) return ((CLabel) sibling).getText();
			}
			break;
		}
	}
	return null;
}
String stripMnemonic (String string) {
	int index = 0;
	int length = string.length ();
	do {
		while ((index < length) && (string.charAt (index) != '&')) index++;
		if (++index >= length) return string;
		if (string.charAt (index) != '&') {
			return string.substring(0, index-1) + string.substring(index, length);
		}
		index++;
	} while (index < length);
	return string;
}
/*
 * Return the lowercase of the first non-'&' character following
 * an '&' character in the given string. If there are no '&'
 * characters in the given string, return '\0'.
 */
char _findMnemonic (String string) {
	if (string == null) return '\0';
	int index = 0;
	int length = string.length ();
	do {
		while (index < length && string.charAt (index) != '&') index++;
		if (++index >= length) return '\0';
		if (string.charAt (index) != '&') return Character.toLowerCase (string.charAt (index));
		index++;
	} while (index < length);
	return '\0';
}
/**
 * Executes the action.
 *
 * @param action one of the actions defined in ST.java
 */
public void invokeAction(int action) {
	checkWidget();
	if (blockSelection && invokeBlockAction(action)) return;
	updateCaretDirection = true;
	switch (action) {
		// Navigation
		case ST.LINE_UP:
			doLineUp(false);
			clearSelection(true);
			break;
		case ST.LINE_DOWN:
			doLineDown(false);
			clearSelection(true);
			break;
		case ST.LINE_START:
			doLineStart();
			clearSelection(true);
			break;
		case ST.LINE_END:
			doLineEnd();
			clearSelection(true);
			break;
		case ST.COLUMN_PREVIOUS:
			doCursorPrevious();
			clearSelection(true);
			break;
		case ST.COLUMN_NEXT:
			doCursorNext();
			clearSelection(true);
			break;
		case ST.PAGE_UP:
			doPageUp(false, -1);
			clearSelection(true);
			break;
		case ST.PAGE_DOWN:
			doPageDown(false, -1);
			clearSelection(true);
			break;
		case ST.WORD_PREVIOUS:
			doWordPrevious();
			clearSelection(true);
			break;
		case ST.WORD_NEXT:
			doWordNext();
			clearSelection(true);
			break;
		case ST.TEXT_START:
			doContentStart();
			clearSelection(true);
			break;
		case ST.TEXT_END:
			doContentEnd();
			clearSelection(true);
			break;
		case ST.WINDOW_START:
			doPageStart();
			clearSelection(true);
			break;
		case ST.WINDOW_END:
			doPageEnd();
			clearSelection(true);
			break;
		// Selection
		case ST.SELECT_LINE_UP:
			doSelectionLineUp();
			break;
		case ST.SELECT_ALL:
			selectAll();
			break;
		case ST.SELECT_LINE_DOWN:
			doSelectionLineDown();
			break;
		case ST.SELECT_LINE_START:
			doLineStart();
			doSelection(ST.COLUMN_PREVIOUS);
			break;
		case ST.SELECT_LINE_END:
			doLineEnd();
			doSelection(ST.COLUMN_NEXT);
			break;
		case ST.SELECT_COLUMN_PREVIOUS:
			doSelectionCursorPrevious();
			doSelection(ST.COLUMN_PREVIOUS);
			break;
		case ST.SELECT_COLUMN_NEXT:
			doSelectionCursorNext();
			doSelection(ST.COLUMN_NEXT);
			break;
		case ST.SELECT_PAGE_UP:
			doSelectionPageUp(-1);
			break;
		case ST.SELECT_PAGE_DOWN:
			doSelectionPageDown(-1);
			break;
		case ST.SELECT_WORD_PREVIOUS:
			doSelectionWordPrevious();
			doSelection(ST.COLUMN_PREVIOUS);
			break;
		case ST.SELECT_WORD_NEXT:
			doSelectionWordNext();
			doSelection(ST.COLUMN_NEXT);
			break;
		case ST.SELECT_TEXT_START:
			doContentStart();
			doSelection(ST.COLUMN_PREVIOUS);
			break;
		case ST.SELECT_TEXT_END:
			doContentEnd();
			doSelection(ST.COLUMN_NEXT);
			break;
		case ST.SELECT_WINDOW_START:
			doPageStart();
			doSelection(ST.COLUMN_PREVIOUS);
			break;
		case ST.SELECT_WINDOW_END:
			doPageEnd();
			doSelection(ST.COLUMN_NEXT);
			break;
		// Modification
		case ST.CUT:
			cut();
			break;
		case ST.COPY:
			copy();
			break;
		case ST.PASTE:
			paste();
			break;
		case ST.DELETE_PREVIOUS:
			doBackspace();
			break;
		case ST.DELETE_NEXT:
			doDelete();
			break;
		case ST.DELETE_WORD_PREVIOUS:
			doDeleteWordPrevious();
			break;
		case ST.DELETE_WORD_NEXT:
			doDeleteWordNext();
			break;
		// Miscellaneous
		case ST.TOGGLE_OVERWRITE:
			overwrite = !overwrite;		// toggle insert/overwrite mode
			break;
		case ST.TOGGLE_BLOCKSELECTION:
			setBlockSelection(!blockSelection);
			break;
	}
}
/**
* Returns true if an action should not be performed when block
* selection in active
*/
boolean invokeBlockAction(int action) {
	switch (action) {
		// Navigation
		case ST.LINE_UP:
		case ST.LINE_DOWN:
		case ST.LINE_START:
		case ST.LINE_END:
		case ST.COLUMN_PREVIOUS:
		case ST.COLUMN_NEXT:
		case ST.PAGE_UP:
		case ST.PAGE_DOWN:
		case ST.WORD_PREVIOUS:
		case ST.WORD_NEXT:
		case ST.TEXT_START:
		case ST.TEXT_END:
		case ST.WINDOW_START:
		case ST.WINDOW_END:
			clearBlockSelection(false, false);
			return false;
		// Selection
		case ST.SELECT_LINE_UP:
			doBlockLineVertical(true);
			return true;
		case ST.SELECT_LINE_DOWN:
			doBlockLineVertical(false);
			return true;
		case ST.SELECT_LINE_START:
			doBlockLineHorizontal(false);
			return true;
		case ST.SELECT_LINE_END:
			doBlockLineHorizontal(true);
			return false;
		case ST.SELECT_COLUMN_PREVIOUS:
			doBlockColumn(false);
			return true;
		case ST.SELECT_COLUMN_NEXT:
			doBlockColumn(true);
			return true;
		case ST.SELECT_WORD_PREVIOUS:
			doBlockWord(false);
			return true;
		case ST.SELECT_WORD_NEXT:
			doBlockWord(true);
			return true;
		case ST.SELECT_ALL:
			return false;
		case ST.SELECT_TEXT_START:
			doBlockContentStartEnd(false);
			break;
		case ST.SELECT_TEXT_END:
			doBlockContentStartEnd(true);
			break;
		case ST.SELECT_PAGE_UP:
		case ST.SELECT_PAGE_DOWN:
		case ST.SELECT_WINDOW_START:
		case ST.SELECT_WINDOW_END:
			//blocked actions
			return true;
		// Modification
		case ST.CUT:
		case ST.COPY:
		case ST.PASTE:
			return false;
		case ST.DELETE_PREVIOUS:
		case ST.DELETE_NEXT:
			if (blockXLocation != -1) {
				insertBlockSelectionText((char)0, action);
				return true;
			}
			return false;
		case ST.DELETE_WORD_PREVIOUS:
		case ST.DELETE_WORD_NEXT:
			//blocked actions
			return blockXLocation != -1;
	}
	return false;
}
boolean isBidiCaret() {
	return BidiUtil.isBidiPlatform();
}
boolean isFixedLineHeight() {
	return !isWordWrap() && lineSpacing == 0 && renderer.lineSpacingProvider == null && !hasStyleWithVariableHeight && !hasVerticalIndent;
}
/**
 * Returns whether the given offset is inside a multi byte line delimiter.
 * Example:
 * "Line1\r\n" isLineDelimiter(5) == false but isLineDelimiter(6) == true
 *
 * @return true if the given offset is inside a multi byte line delimiter.
 * false if the given offset is before or after a line delimiter.
 */
boolean isLineDelimiter(int offset) {
	int line = content.getLineAtOffset(offset);
	int lineOffset = content.getOffsetAtLine(line);
	int offsetInLine = offset - lineOffset;
	// offsetInLine will be greater than line length if the line
	// delimiter is longer than one character and the offset is set
	// in between parts of the line delimiter.
	return offsetInLine > content.getLine(line).length();
}
/**
 * Returns whether the widget is mirrored (right oriented/right to left
 * writing order).
 *
 * @return isMirrored true=the widget is right oriented, false=the widget
 * 	is left oriented
 */
boolean isMirrored() {
	return (getStyle() & SWT.MIRRORED) != 0;
}
/**
 * Returns <code>true</code> if any text in the widget is selected,
 * and <code>false</code> otherwise.
 *
 * @return the text selection state
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.103
 */
public boolean isTextSelected() {
	checkWidget();
	if (blockSelection && blockXLocation != -1) {
		Rectangle rect = getBlockSelectionPosition();
		return !rect.isEmpty();
	}
	return Arrays.stream(selection).anyMatch(range -> range.x != range.y);
}
/**
 * Returns whether the widget can have only one line.
 *
 * @return true if widget can have only one line, false if widget can have
 * 	multiple lines
 */
boolean isSingleLine() {
	return (getStyle() & SWT.SINGLE) != 0;
}

/**
 * Sends the specified verify event, replace/insert text as defined by
 * the event and send a modify event.
 *
 * @param event	the text change event.
 *	<ul>
 *	<li>event.start - the replace start offset</li>
 * 	<li>event.end - the replace end offset</li>
 * 	<li>event.text - the new text</li>
 *	</ul>
 * @param updateCaret whether or not he caret should be set behind
 *	the new text
 */
void modifyContent(Event event, boolean updateCaret) {
	event.doit = true;
	notifyListeners(SWT.Verify, event);
	if (event.doit) {
		StyledTextEvent styledTextEvent = null;
		int replacedLength = event.end - event.start;
		if (isListening(ST.ExtendedModify)) {
			styledTextEvent = new StyledTextEvent(content);
			styledTextEvent.start = event.start;
			styledTextEvent.end = event.start + event.text.length();
			styledTextEvent.text = content.getTextRange(event.start, replacedLength);
		}
		if (updateCaret) {
			//Fix advancing flag for delete/backspace key on direction boundary
			if (event.text.length() == 0) {
				int lineIndex = content.getLineAtOffset(event.start);
				int lineOffset = content.getOffsetAtLine(lineIndex);
				TextLayout layout = renderer.getTextLayout(lineIndex);
				int levelStart = layout.getLevel(event.start - lineOffset);
				int lineIndexEnd = content.getLineAtOffset(event.end);
				if (lineIndex != lineIndexEnd) {
					renderer.disposeTextLayout(layout);
					lineOffset = content.getOffsetAtLine(lineIndexEnd);
					layout = renderer.getTextLayout(lineIndexEnd);
				}
				int levelEnd = layout.getLevel(event.end - lineOffset);
				renderer.disposeTextLayout(layout);
				if (levelStart != levelEnd) {
					caretAlignment = PREVIOUS_OFFSET_TRAILING;
				} else {
					caretAlignment = OFFSET_LEADING;
				}
			}
		}
		content.replaceTextRange(event.start, replacedLength, event.text);
		// set the caret position prior to sending the modify event.
		// fixes 1GBB8NJ
		if (updateCaret && !(blockSelection && blockXLocation != -1)) {
			// always update the caret location. fixes 1G8FODP
			setSelection(Arrays.stream(selection).map(sel -> {
				if (sel.y < event.start || sel.x > event.end) {
					return sel;
				} else { // current range edited
					return new Point(event.start + event.text.length(), event.start + event.text.length());
				}
			}).flatMapToInt(p -> IntStream.of(p.x, p.y - p.x)).toArray(), true, false);
			showCaret();
		}
		notifyListeners(SWT.Modify, event);
		if (isListening(ST.ExtendedModify)) {
			notifyListeners(ST.ExtendedModify, styledTextEvent);
		}
	}
}
void paintObject(GC gc, int x, int y, int ascent, int descent, StyleRange style, Bullet bullet, int bulletIndex) {
	if (isListening(ST.PaintObject)) {
		StyledTextEvent event = new StyledTextEvent (content) ;
		event.gc = gc;
		event.x = x;
		event.y = y;
		event.ascent = ascent;
		event.descent = descent;
		event.style = style;
		event.bullet = bullet;
		event.bulletIndex = bulletIndex;
		notifyListeners(ST.PaintObject, event);
	}
}
/**
 * Replaces the selection with the text on the <code>DND.CLIPBOARD</code>
 * clipboard  or, if there is no selection,  inserts the text at the current
 * caret offset.   If the widget has the SWT.SINGLE style and the
 * clipboard text contains more than one line, only the first line without
 * line delimiters is  inserted in the widget.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void paste(){
	checkWidget();
	String text = (String) getClipboardContent(DND.CLIPBOARD);
	if (text != null && text.length() > 0) {
		if (blockSelection) {
			boolean fillWithSpaces = isFixedLineHeight() && renderer.fixedPitch;
			int offset = insertBlockSelectionText(text, fillWithSpaces);
			setCaretOffsets(new int[] {offset}, SWT.DEFAULT);
			clearBlockSelection(true, true);
			setCaretLocations();
			return;
		} else if (getSelectionRanges().length / 2 > 1) { // multi selection
			insertMultiSelectionText(text);
			setCaretLocations();
			return;
		}
		Event event = new Event();
		event.start = selection[0].x;
		event.end = selection[0].y;
		String delimitedText = getModelDelimitedText(text);
		if (textLimit > 0) {
			int uneditedTextLength = getCharCount() - (selection[0].y - selection[0].x);
			if ((uneditedTextLength + delimitedText.length()) > textLimit) {
				int endIndex = textLimit - uneditedTextLength;
				delimitedText = delimitedText.substring(0, Math.max(endIndex, 0));
			}
		}
		event.text = delimitedText;
		sendKeyEvent(event);
	}
}

private void insertMultiSelectionText(String text) {
	String[] blocks = text.split(PlatformLineDelimiter);
	int[] ranges = getSelectionRanges();
	for (int i = ranges.length / 2 - 1; i >= 0; i --) {
		int offset = ranges[2 * i];
		int length = ranges[2 * i + 1];
		String toPaste = blocks.length > i ? blocks[i] : blocks[blocks.length - 1];
		Event event = new Event();
		event.start = offset;
		event.end = offset + length;
		event.text = toPaste;
		sendKeyEvent(event);
	}
}

private void pasteOnMiddleClick(Event event) {
	String text = (String)getClipboardContent(DND.SELECTION_CLIPBOARD);
	if (text != null && text.length() > 0) {
		// position cursor
		doMouseLocationChange(event.x, event.y, false);
		// insert text
		Event e = new Event();
		e.start = selection[0].x;
		e.end = selection[0].y;
		e.text = getModelDelimitedText(text);
		sendKeyEvent(e);
	}
}
/**
 * Prints the widget's text to the default printer.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void print() {
	checkWidget();
	Printer printer = new Printer();
	StyledTextPrintOptions options = new StyledTextPrintOptions();
	options.printTextForeground = true;
	options.printTextBackground = true;
	options.printTextFontStyle = true;
	options.printLineBackground = true;
	new Printing(this, printer, options).run();
	printer.dispose();
}
/**
 * Returns a runnable that will print the widget's text
 * to the specified printer.
 * <p>
 * The runnable may be run in a non-UI thread.
 * </p>
 *
 * @param printer the printer to print to
 *
 * @return a <code>Runnable</code> for printing the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when printer is null</li>
 * </ul>
 */
public Runnable print(Printer printer) {
	checkWidget();
	if (printer == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	StyledTextPrintOptions options = new StyledTextPrintOptions();
	options.printTextForeground = true;
	options.printTextBackground = true;
	options.printTextFontStyle = true;
	options.printLineBackground = true;
	return print(printer, options);
}
/**
 * Returns a runnable that will print the widget's text
 * to the specified printer.
 * <p>
 * The runnable may be run in a non-UI thread.
 * </p>
 *
 * @param printer the printer to print to
 * @param options print options to use during printing
 *
 * @return a <code>Runnable</code> for printing the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when printer or options is null</li>
 * </ul>
 * @since 2.1
 */
public Runnable print(Printer printer, StyledTextPrintOptions options) {
	checkWidget();
	if (printer == null || options == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	return new Printing(this, printer, options);
}
/**
 * Causes the entire bounds of the receiver to be marked
 * as needing to be redrawn. The next time a paint request
 * is processed, the control will be completely painted.
 * <p>
 * Recalculates the content width for all lines in the bounds.
 * When a <code>LineStyleListener</code> is used a redraw call
 * is the only notification to the widget that styles have changed
 * and that the content width may have changed.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Control#update()
 */
@Override
public void redraw() {
	super.redraw();
	int itemCount = getPartialBottomIndex() - topIndex + 1;
	renderer.reset(topIndex, itemCount);
	renderer.calculate(topIndex, itemCount);
	setScrollBars(false);
	doMouseLinkCursor();
}
/**
 * Causes the rectangular area of the receiver specified by
 * the arguments to be marked as needing to be redrawn.
 * The next time a paint request is processed, that area of
 * the receiver will be painted. If the <code>all</code> flag
 * is <code>true</code>, any children of the receiver which
 * intersect with the specified area will also paint their
 * intersecting areas. If the <code>all</code> flag is
 * <code>false</code>, the children will not be painted.
 * <p>
 * Marks the content width of all lines in the specified rectangle
 * as unknown. Recalculates the content width of all visible lines.
 * When a <code>LineStyleListener</code> is used a redraw call
 * is the only notification to the widget that styles have changed
 * and that the content width may have changed.
 * </p>
 *
 * @param x the x coordinate of the area to draw
 * @param y the y coordinate of the area to draw
 * @param width the width of the area to draw
 * @param height the height of the area to draw
 * @param all <code>true</code> if children should redraw, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Control#update()
 */
@Override
public void redraw(int x, int y, int width, int height, boolean all) {
	super.redraw(x, y, width, height, all);
	if (height > 0) {
		int firstLine = getLineIndex(y);
		int lastLine = getLineIndex(y + height);
		resetCache(firstLine, lastLine - firstLine + 1);
		doMouseLinkCursor();
	}
}
void redrawLines(int startLine, int lineCount, boolean bottomChanged) {
	// do nothing if redraw range is completely invisible
	int endLine = startLine + lineCount - 1;
	int partialBottomIndex = getPartialBottomIndex();
	int partialTopIndex = getPartialTopIndex();
	if (startLine > partialBottomIndex || endLine < partialTopIndex) {
		return;
	}
	// only redraw visible lines
	if (startLine < partialTopIndex) {
		startLine = partialTopIndex;
	}
	if (endLine > partialBottomIndex) {
		endLine = partialBottomIndex;
	}
	int redrawTop = getLinePixel(startLine);
	int redrawBottom = getLinePixel(endLine + 1);
	if (bottomChanged) redrawBottom = clientAreaHeight - bottomMargin;
	int redrawWidth = clientAreaWidth - leftMargin - rightMargin;
	super.redraw(leftMargin, redrawTop, redrawWidth, redrawBottom - redrawTop, true);
}
void redrawLinesBullet (int[] redrawLines) {
	if (redrawLines == null) return;
	int topIndex = getPartialTopIndex();
	int bottomIndex = getPartialBottomIndex();
	for (int redrawLine : redrawLines) {
		int lineIndex = redrawLine;
		if (!(topIndex <= lineIndex && lineIndex <= bottomIndex)) continue;
		int width = -1;
		Bullet bullet = renderer.getLineBullet(lineIndex, null);
		if (bullet != null) {
			StyleRange style = bullet.style;
			GlyphMetrics metrics = style.metrics;
			width = metrics.width;
		}
		if (width == -1) width = getClientArea().width;
		int height = renderer.getLineHeight(lineIndex);
		int y = getLinePixel(lineIndex);
		super.redraw(0, y, width, height, false);
	}
}
void redrawMargins(int oldHeight, int oldWidth) {
	/* Redraw the old or new right/bottom margin if needed */
	if (oldWidth != clientAreaWidth) {
		if (rightMargin > 0) {
			int x = (oldWidth < clientAreaWidth ? oldWidth : clientAreaWidth) - rightMargin;
			super.redraw(x, 0, rightMargin, oldHeight, false);
		}
	}
	if (oldHeight != clientAreaHeight) {
		if (bottomMargin > 0) {
			int y = (oldHeight < clientAreaHeight ? oldHeight : clientAreaHeight) - bottomMargin;
			super.redraw(0, y, oldWidth, bottomMargin, false);
		}
	}
}
/**
 * Redraws the specified text range.
 *
 * @param start offset of the first character to redraw
 * @param length number of characters to redraw
 * @param clearBackground true if the background should be cleared as
 *  part of the redraw operation.  If true, the entire redraw range will
 *  be cleared before anything is redrawn.  If the redraw range includes
 *	the last character of a line (i.e., the entire line is redrawn) the
 * 	line is cleared all the way to the right border of the widget.
 * 	The redraw operation will be faster and smoother if clearBackground
 * 	is set to false.  Whether or not the flag can be set to false depends
 * 	on the type of change that has taken place.  If font styles or
 * 	background colors for the redraw range have changed, clearBackground
 * 	should be set to true.  If only foreground colors have changed for
 * 	the redraw range, clearBackground can be set to false.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_RANGE when start and/or end are outside the widget content</li>
 * </ul>
 */
public void redrawRange(int start, int length, boolean clearBackground) {
	checkWidget();
	int end = start + length;
	int contentLength = content.getCharCount();
	if (start > end || start < 0 || end > contentLength) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	int firstLine = content.getLineAtOffset(start);
	int lastLine = content.getLineAtOffset(end);
	resetCache(firstLine, lastLine - firstLine + 1);
	internalRedrawRange(start, length);
	doMouseLinkCursor();
}
/**
 * Removes the specified bidirectional segment listener.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 *
 * @since 2.0
 */
public void removeBidiSegmentListener(BidiSegmentListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	removeListener(ST.LineGetSegments, listener);
	resetCache(0, content.getLineCount());
	setCaretLocations();
	super.redraw();
}
/**
 * Removes the specified caret listener.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 *
 * @since 3.5
 */
public void removeCaretListener(CaretListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	removeListener(ST.CaretMoved, listener);
}
/**
 * Removes the specified extended modify listener.
 *
 * @param extendedModifyListener the listener which should no longer be notified
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void removeExtendedModifyListener(ExtendedModifyListener extendedModifyListener) {
	checkWidget();
	if (extendedModifyListener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	removeListener(ST.ExtendedModify, extendedModifyListener);
}
/**
 * Removes the specified line background listener.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void removeLineBackgroundListener(LineBackgroundListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	removeListener(ST.LineGetBackground, listener);
}
/**
 * Removes the specified line style listener.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void removeLineStyleListener(LineStyleListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	removeListener(ST.LineGetStyle, listener);
	setCaretLocations();
}
/**
 * Removes the specified modify listener.
 *
 * @param modifyListener the listener which should no longer be notified
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void removeModifyListener(ModifyListener modifyListener) {
	checkWidget();
	if (modifyListener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	removeListener(SWT.Modify, modifyListener);
}
/**
 * Removes the specified listener.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 * @since 3.2
 */
public void removePaintObjectListener(PaintObjectListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	removeListener(ST.PaintObject, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the user changes the receiver's selection.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	removeListener(SWT.Selection, listener);
}
/**
 * Removes the specified verify listener.
 *
 * @param verifyListener the listener which should no longer be notified
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void removeVerifyListener(VerifyListener verifyListener) {
	checkWidget();
	if (verifyListener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	removeListener(SWT.Verify, verifyListener);
}
/**
 * Removes the specified key verify listener.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void removeVerifyKeyListener(VerifyKeyListener listener) {
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	removeListener(ST.VerifyKey, listener);
}
/**
 * Removes the specified word movement listener.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 *
 * @see MovementEvent
 * @see MovementListener
 * @see #addWordMovementListener
 *
 * @since 3.3
 */

public void removeWordMovementListener(MovementListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	removeListener(ST.WordNext, listener);
	removeListener(ST.WordPrevious, listener);
}
/**
 * Replaces the styles in the given range with new styles.  This method
 * effectively deletes the styles in the given range and then adds the
 * the new styles.
 * <p>
 * Note: Because a StyleRange includes the start and length, the
 * same instance cannot occur multiple times in the array of styles.
 * If the same style attributes, such as font and color, occur in
 * multiple StyleRanges, <code>setStyleRanges(int, int, int[], StyleRange[])</code>
 * can be used to share styles and reduce memory usage.
 * </p><p>
 * Should not be called if a LineStyleListener has been set since the
 * listener maintains the styles.
 * </p>
 *
 * @param start offset of first character where styles will be deleted
 * @param length length of the range to delete styles in
 * @param ranges StyleRange objects containing the new style information.
 * The ranges should not overlap and should be within the specified start
 * and length. The style rendering is undefined if the ranges do overlap
 * or are ill-defined. Must not be null.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_RANGE when either start or end is outside the valid range (0 &lt;= offset &lt;= getCharCount())</li>
 *   <li>ERROR_NULL_ARGUMENT when ranges is null</li>
 * </ul>
 *
 * @since 2.0
 *
 * @see #setStyleRanges(int, int, int[], StyleRange[])
 */
public void replaceStyleRanges(int start, int length, StyleRange[] ranges) {
	checkWidget();
	if (isListening(ST.LineGetStyle)) return;
	if (ranges == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	setStyleRanges(start, length, null, ranges, false);
}
/**
 * Replaces the given text range with new text.
 * If the widget has the SWT.SINGLE style and "text" contains more than
 * one line, only the first line is rendered but the text is stored
 * unchanged. A subsequent call to getText will return the same text
 * that was set. Note that only a single line of text should be set when
 * the SWT.SINGLE style is used.
 * <p>
 * <b>NOTE:</b> During the replace operation the current selection is
 * changed as follows:
 * </p>
 * <ul>
 * <li>selection before replaced text: selection unchanged
 * <li>selection after replaced text: adjust the selection so that same text
 * remains selected
 * <li>selection intersects replaced text: selection is cleared and caret
 * is placed after inserted text
 * </ul>
 *
 * @param start offset of first character to replace
 * @param length number of characters to replace. Use 0 to insert text
 * @param text new text. May be empty to delete text.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_RANGE when either start or end is outside the valid range (0 &lt;= offset &lt;= getCharCount())</li>
 *   <li>ERROR_INVALID_ARGUMENT when either start or end is inside a multi byte line delimiter.
 * 		Splitting a line delimiter for example by inserting text in between the CR and LF and deleting part of a line delimiter is not supported</li>
 *   <li>ERROR_NULL_ARGUMENT when string is null</li>
 * </ul>
 */
public void replaceTextRange(int start, int length, String text) {
	checkWidget();
	if (text == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	int contentLength = getCharCount();
	int end = start + length;
	if (start > end || start < 0 || end > contentLength) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	Event event = new Event();
	event.start = start;
	event.end = end;
	event.text = text;
	modifyContent(event, false);
}
/**
 * Resets the caret position, selection and scroll offsets. Recalculate
 * the content width and scroll bars. Redraw the widget.
 */
void reset() {
	ScrollBar verticalBar = getVerticalBar();
	ScrollBar horizontalBar = getHorizontalBar();
	setCaretOffsets(new int[] {0}, SWT.DEFAULT);
	topIndex = 0;
	topIndexY = 0;
	verticalScrollOffset = 0;
	horizontalScrollOffset = 0;
	resetSelection();
	renderer.setContent(content);
	if (verticalBar != null) {
		verticalBar.setSelection(0);
	}
	if (horizontalBar != null) {
		horizontalBar.setSelection(0);
	}
	resetCache(0, 0);
	setCaretLocations();
	super.redraw();
}
void resetBidiData() {
	caretDirection = SWT.NULL;
	resetCache(0, content.getLineCount());
	setCaretLocations();
	keyActionMap.clear();
	createKeyBindings();
	super.redraw();
}
void resetCache(SortedSet<Integer> lines) {
	if (lines == null || lines.isEmpty()) return;
	int maxLineIndex = renderer.maxWidthLineIndex;
	renderer.reset(lines);
	renderer.calculateClientArea();
	if (0 <= maxLineIndex && maxLineIndex < content.getLineCount()) {
		renderer.calculate(maxLineIndex, 1);
	}
	setScrollBars(true);
	if (!isFixedLineHeight()) {
		if (topIndex > lines.iterator().next()) {
			verticalScrollOffset = -1;
		}
		renderer.calculateIdle();
	}
}
void resetCache(int firstLine, int count) {
	int maxLineIndex = renderer.maxWidthLineIndex;
	renderer.reset(firstLine, count);
	renderer.calculateClientArea();
	if (0 <= maxLineIndex && maxLineIndex < content.getLineCount()) {
		renderer.calculate(maxLineIndex, 1);
	}
	setScrollBars(true);
	if (!isFixedLineHeight()) {
		if (topIndex > firstLine) {
			verticalScrollOffset = -1;
		}
		renderer.calculateIdle();
	}
}
/**
 * Resets the selection.
 */
void resetSelection() {
	selection = Arrays.stream(caretOffsets).mapToObj(offset -> new Point(offset, offset)).toArray(Point[]::new);
	selectionAnchors = Arrays.copyOf(caretOffsets, caretOffsets.length);
	sendAccessibleTextCaretMoved();
}

@Override
public void scroll(int destX, int destY, int x, int y, int width, int height, boolean all) {
	super.scroll(destX, destY, x, y, width, height, false);
	if (all) {
		int deltaX = destX - x, deltaY = destY - y;
		for (Control child : getChildren()) {
			Rectangle rect = child.getBounds();
			child.setLocation(rect.x + deltaX, rect.y + deltaY);
		}
	}
}

/**
 * Scrolls the widget horizontally.
 *
 * @param pixels number of SWT logical points to scroll, &gt; 0 = scroll left,
 * 	&lt; 0 scroll right
 * @param adjustScrollBar
 * 	true= the scroll thumb will be moved to reflect the new scroll offset.
 * 	false = the scroll thumb will not be moved
 * @return
 *	true=the widget was scrolled
 *	false=the widget was not scrolled, the given offset is not valid.
 */
boolean scrollHorizontal(int pixels, boolean adjustScrollBar) {
	if (pixels == 0) return false;
	if (wordWrap) return false;
	ScrollBar horizontalBar = getHorizontalBar();
	if (horizontalBar != null && adjustScrollBar) {
		horizontalBar.setSelection(horizontalScrollOffset + pixels);
	}
	int scrollHeight = clientAreaHeight - topMargin - bottomMargin;
	if (pixels > 0) {
		int sourceX = leftMargin + pixels;
		int scrollWidth = clientAreaWidth - sourceX - rightMargin;
		if (scrollWidth > 0) {
			scroll(leftMargin, topMargin, sourceX, topMargin, scrollWidth, scrollHeight, true);
		}
		if (sourceX > scrollWidth) {
			super.redraw(leftMargin + scrollWidth, topMargin, pixels - scrollWidth, scrollHeight, true);
		}
	} else {
		int destinationX = leftMargin - pixels;
		int scrollWidth = clientAreaWidth - destinationX - rightMargin;
		if (scrollWidth > 0) {
			scroll(destinationX, topMargin, leftMargin, topMargin, scrollWidth, scrollHeight, true);
		}
		if (destinationX > scrollWidth) {
			super.redraw(leftMargin + scrollWidth, topMargin, -pixels - scrollWidth, scrollHeight, true);
		}
	}
	horizontalScrollOffset += pixels;
	setCaretLocations();
	return true;
}
/**
 * Scrolls the widget vertically.
 *
 * @param pixel the new vertical scroll offset
 * @param adjustScrollBar
 * 	true= the scroll thumb will be moved to reflect the new scroll offset.
 * 	false = the scroll thumb will not be moved
 * @return
 *	true=the widget was scrolled
 *	false=the widget was not scrolled
 */
boolean scrollVertical(int pixels, boolean adjustScrollBar) {
	if (pixels == 0) {
		return false;
	}
	if (verticalScrollOffset != -1) {
		ScrollBar verticalBar = getVerticalBar();
		if (verticalBar != null && adjustScrollBar) {
			verticalBar.setSelection(verticalScrollOffset + pixels);
		}
		int deltaY = 0;
		if (pixels > 0) {
			int sourceY = topMargin + pixels;
			int scrollHeight = clientAreaHeight - sourceY - bottomMargin;
			if (scrollHeight > 0) {
				deltaY = -pixels;
			}
		} else {
			int destinationY = topMargin - pixels;
			int scrollHeight = clientAreaHeight - destinationY - bottomMargin;
			if (scrollHeight > 0) {
				deltaY = -pixels;
			}
		}
		Control[] children = getChildren();
		for (Control child : children) {
			Rectangle rect = child.getBounds();
			child.setLocation(rect.x, rect.y + deltaY);
		}
		verticalScrollOffset += pixels;
		calculateTopIndex(pixels);
		super.redraw();
	} else {
		calculateTopIndex(pixels);
		super.redraw();
	}
	setCaretLocations();
	return true;
}
void scrollText(int srcY, int destY) {
	if (srcY == destY) return;
	int deltaY = destY - srcY;
	int scrollWidth = clientAreaWidth - leftMargin - rightMargin, scrollHeight;
	if (deltaY > 0) {
		scrollHeight = clientAreaHeight - srcY - bottomMargin;
	} else {
		scrollHeight = clientAreaHeight - destY - bottomMargin;
	}
	scroll(leftMargin, destY, leftMargin, srcY, scrollWidth, scrollHeight, true);
	if ((0 < srcY + scrollHeight) && (topMargin > srcY)) {
		super.redraw(leftMargin, deltaY, scrollWidth, topMargin, false);
	}
	if ((0 < destY + scrollHeight) && (topMargin > destY)) {
		super.redraw(leftMargin, 0, scrollWidth, topMargin, false);
	}
	if ((clientAreaHeight - bottomMargin < srcY + scrollHeight) && (clientAreaHeight > srcY)) {
		super.redraw(leftMargin, clientAreaHeight - bottomMargin + deltaY, scrollWidth, bottomMargin, false);
	}
	if ((clientAreaHeight - bottomMargin < destY + scrollHeight) && (clientAreaHeight > destY)) {
		super.redraw(leftMargin, clientAreaHeight - bottomMargin, scrollWidth, bottomMargin, false);
	}
}
void sendAccessibleTextCaretMoved() {
	if (Arrays.stream(caretOffsets).noneMatch(caretOffset -> caretOffset == accCaretOffset)) {
		accCaretOffset = caretOffsets[0];
		getAccessible().textCaretMoved(caretOffsets[0]);
	}
}
void sendAccessibleTextChanged(int start, int newCharCount, int replaceCharCount) {
	Accessible accessible = getAccessible();
	if (replaceCharCount != 0) {
		accessible.textChanged(ACC.TEXT_DELETE, start, replaceCharCount);
	}
	if (newCharCount != 0) {
		accessible.textChanged(ACC.TEXT_INSERT, start, newCharCount);
	}
}
/**
 * Selects all the text.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void selectAll() {
	checkWidget();
	if (blockSelection) {
		renderer.calculate(0, content.getLineCount());
		setScrollBars(false);
		int verticalScrollOffset = getVerticalScrollOffset();
		int left = leftMargin - horizontalScrollOffset;
		int top = topMargin - verticalScrollOffset;
		int right = renderer.getWidth() - rightMargin - horizontalScrollOffset;
		int bottom = renderer.getHeight() - bottomMargin - verticalScrollOffset;
		setBlockSelectionLocation(left, top, right, bottom, false);
		return;
	}
	setSelection(0, Math.max(getCharCount(),0));
}
/**
 * Replaces/inserts text as defined by the event.
 *
 * @param event the text change event.
 *	<ul>
 *	<li>event.start - the replace start offset</li>
 * 	<li>event.end - the replace end offset</li>
 * 	<li>event.text - the new text</li>
 *	</ul>
 */
void sendKeyEvent(Event event) {
	if (editable) {
		modifyContent(event, true);
	}
}
/**
 * Returns a StyledTextEvent that can be used to request data such
 * as styles and background color for a line.
 * <p>
 * The specified line may be a visual (wrapped) line if in word
 * wrap mode. The returned object will always be for a logical
 * (unwrapped) line.
 * </p>
 *
 * @param lineOffset offset of the line. This may be the offset of
 * 	a visual line if the widget is in word wrap mode.
 * @param line line text. This may be the text of a visual line if
 * 	the widget is in word wrap mode.
 * @return StyledTextEvent that can be used to request line data
 * 	for the given line.
 */
StyledTextEvent sendLineEvent(int eventType, int lineOffset, String line) {
	StyledTextEvent event = null;
	if (isListening(eventType)) {
		event = new StyledTextEvent(content);
		event.detail = lineOffset;
		event.text = line;
		event.alignment = alignment;
		event.indent = indent;
		event.wrapIndent = wrapIndent;
		event.justify = justify;
		notifyListeners(eventType, event);
	}
	return event;
}
/**
 * Sends the specified selection event.
 */
void sendSelectionEvent() {
	getAccessible().textSelectionChanged();
	Event event = new Event();
	event.x = selection[0].x;
	event.y = selection[selection.length - 1].y;
	notifyListeners(SWT.Selection, event);
}
int sendTextEvent(int left, int right, int lineIndex, String text, boolean fillWithSpaces) {
	int lineWidth = 0, start, end;
	StringBuilder buffer = new StringBuilder();
	if (lineIndex < content.getLineCount()) {
		int[] trailing = new int[1];
		start = getOffsetAtPoint(left, getLinePixel(lineIndex), trailing, true);
		if (start == -1) {
			int lineOffset = content.getOffsetAtLine(lineIndex);
			int lineLegth = content.getLine(lineIndex).length();
			start = end = lineOffset + lineLegth;
			if (fillWithSpaces) {
				TextLayout layout = renderer.getTextLayout(lineIndex);
				lineWidth = layout.getBounds().width;
				renderer.disposeTextLayout(layout);
			}
		} else {
			start += trailing[0];
			end = left == right ? start : getOffsetAtPoint(right, 0, lineIndex, null);
			fillWithSpaces = false;
		}
	} else {
		start = end = content.getCharCount();
		buffer.append(content.getLineDelimiter());
	}
	if (start > end) {
		int temp = start;
		start = end;
		end = temp;
	}
	if (fillWithSpaces) {
		int spacesWidth = left - lineWidth + horizontalScrollOffset - leftMargin;
		int spacesCount = spacesWidth / renderer.averageCharWidth;
		for (int i = 0; i < spacesCount; i++) {
			buffer.append(' ');
		}
	}
	buffer.append(text);
	Event event = new Event();
	event.start = start;
	event.end = end;
	event.text = buffer.toString();
	sendKeyEvent(event);
	return event.start + event.text.length();
}
int sendWordBoundaryEvent(int eventType, int movement, int offset, int newOffset, String lineText, int lineOffset) {
	if (isListening(eventType)) {
		StyledTextEvent event = new StyledTextEvent(content);
		event.detail = lineOffset;
		event.text = lineText;
		event.count = movement;
		event.start = offset;
		event.end = newOffset;
		notifyListeners(eventType, event);
		offset = event.end;
		if (offset != newOffset) {
			int length = getCharCount();
			if (offset < 0) {
				offset = 0;
			} else if (offset > length) {
				offset = length;
			} else {
				if (isLineDelimiter(offset)) {
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				}
			}
		}
		return offset;
	}
	return newOffset;
}
void setAlignment() {
	if ((getStyle() & SWT.SINGLE) == 0) return;
	int alignment = renderer.getLineAlignment(0, this.alignment);
	int newAlignmentMargin = 0;
	if (alignment != SWT.LEFT) {
		renderer.calculate(0, 1);
		int width = renderer.getWidth() - alignmentMargin;
		newAlignmentMargin = clientAreaWidth - width;
		if (newAlignmentMargin < 0) newAlignmentMargin = 0;
		if (alignment == SWT.CENTER) newAlignmentMargin /= 2;
	}
	if (alignmentMargin != newAlignmentMargin) {
		leftMargin -= alignmentMargin;
		leftMargin += newAlignmentMargin;
		alignmentMargin = newAlignmentMargin;
		resetCache(0, 1);
		setCaretLocations();
		super.redraw();
	}
}
/**
 * Sets the alignment of the widget. The argument should be one of <code>SWT.LEFT</code>,
 * <code>SWT.CENTER</code> or <code>SWT.RIGHT</code>. The alignment applies for all lines.
 * <p>
 * Note that if <code>SWT.MULTI</code> is set, then <code>SWT.WRAP</code> must also be set
 * in order to stabilize the right edge before setting alignment.
 * </p>
 *
 * @param alignment the new alignment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setLineAlignment(int, int, int)
 *
 * @since 3.2
 */
public void setAlignment(int alignment) {
	checkWidget();
	alignment &= (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	if (alignment == 0 || this.alignment == alignment) return;
	this.alignment = alignment;
	resetCache(0, content.getLineCount());
	setCaretLocations();
	setAlignment();
	super.redraw();
}
/**
 * Set the Always Show Scrollbars flag.  True if the scrollbars are
 * always shown even if they are not required (default value).  False if the scrollbars are only
 * visible when some part of the content needs to be scrolled to be seen.
 * The H_SCROLL and V_SCROLL style bits are also required to enable scrollbars in the
 * horizontal and vertical directions.
 *
 * @param show true to show the scrollbars even when not required, false to show scrollbars only when required
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.8
 */
public void setAlwaysShowScrollBars(boolean show) {
	checkWidget();
	if (show == alwaysShowScroll) return;
	alwaysShowScroll = show;
	setScrollBars(true);
}
/**
 * @see Control#setBackground(Color)
 */
@Override
public void setBackground(Color color) {
	checkWidget();
	boolean backgroundDisabled = false;
	if (!this.enabled && color == null) {
		if (background != null) {
			Color disabledBg = getDisplay().getSystemColor(SWT.COLOR_TEXT_DISABLED_BACKGROUND);
			if (background.equals(disabledBg)) {
				return;
			} else {
				color = new Color (disabledBg.getRGBA());
				backgroundDisabled = true;
			}
		}
	}
	customBackground = color != null && !this.insideSetEnableCall && !backgroundDisabled;
	background = color;
	super.setBackground(color);
	resetCache(0, content.getLineCount());
	setCaretLocations();
	super.redraw();
}
/**
 * Sets the block selection mode.
 *
 * @param blockSelection true=enable block selection, false=disable block selection
 *
 * @since 3.5
 */
public void setBlockSelection(boolean blockSelection) {
	checkWidget();
	if ((getStyle() & SWT.SINGLE) != 0) return;
	if (blockSelection == this.blockSelection) return;
	if (wordWrap) return;
	this.blockSelection = blockSelection;
	if (cursor == null) {
		Display display = getDisplay();
		int type = blockSelection ? SWT.CURSOR_CROSS : SWT.CURSOR_IBEAM;
		super.setCursor(display.getSystemCursor(type));
	}
	if (blockSelection) {
		int start = selection[0].x;
		int end = selection[0].y;
		if (start != end) {
			setBlockSelectionOffset(start, end, false);
		}
	} else {
		clearBlockSelection(false, false);
	}
}
/**
 * Sets the block selection bounds. The bounds is
 * relative to the upper left corner of the document.
 *
 * @param rect the new bounds for the block selection
 *
 * @see #setBlockSelectionBounds(int, int, int, int)
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_NULL_ARGUMENT when point is null</li>
 * </ul>
 *
 * @since 3.5
 */
public void setBlockSelectionBounds(Rectangle rect) {
	checkWidget();
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	setBlockSelectionBounds(rect.x, rect.y, rect.width, rect.height);
}
/**
 * Sets the block selection bounds. The bounds is
 * relative to the upper left corner of the document.
 *
 * @param x the new x coordinate for the block selection
 * @param y the new y coordinate for the block selection
 * @param width the new width for the block selection
 * @param height the new height for the block selection
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.5
 */
public void setBlockSelectionBounds(int x, int y, int width, int height) {
	checkWidget();
	int verticalScrollOffset = getVerticalScrollOffset();
	if (!blockSelection) {
		x -= horizontalScrollOffset;
		y -= verticalScrollOffset;
		int start = getOffsetAtPoint(x, y, null);
		int end = getOffsetAtPoint(x+width-1, y+height-1, null);
		setSelection(new int[] {start, end - start}, false, false);
		setCaretLocations();
		return;
	}
	int minY = topMargin;
	int minX = leftMargin;
	int maxY = renderer.getHeight() - bottomMargin;
	int maxX = Math.max(clientAreaWidth, renderer.getWidth()) - rightMargin;
	int anchorX = Math.max(minX, Math.min(maxX, x)) - horizontalScrollOffset;
	int anchorY = Math.max(minY, Math.min(maxY, y)) - verticalScrollOffset;
	int locationX = Math.max(minX, Math.min(maxX, x + width)) - horizontalScrollOffset;
	int locationY = Math.max(minY, Math.min(maxY, y + height - 1)) - verticalScrollOffset;
	if (isFixedLineHeight() && renderer.fixedPitch) {
		int avg = renderer.averageCharWidth;
		anchorX = ((anchorX - leftMargin + horizontalScrollOffset) / avg * avg) + leftMargin - horizontalScrollOffset;
		locationX = ((locationX + avg / 2 - leftMargin + horizontalScrollOffset) / avg * avg) + leftMargin - horizontalScrollOffset;
	}
	setBlockSelectionLocation(anchorX, anchorY, locationX, locationY, false);
}
void setBlockSelectionLocation (int x, int y, boolean sendEvent) {
	int verticalScrollOffset = getVerticalScrollOffset();
	blockXLocation = x + horizontalScrollOffset;
	blockYLocation = y + verticalScrollOffset;
	int[] alignment = new int[1];
	int offset = getOffsetAtPoint(x, y, alignment);
	setCaretOffsets(new int[] {offset}, alignment[0]);
	if (blockXAnchor == -1) {
		blockXAnchor = blockXLocation;
		blockYAnchor = blockYLocation;
		selectionAnchors[0] = caretOffsets[0];
	}
	doBlockSelection(sendEvent);
}
void setBlockSelectionLocation (int anchorX, int anchorY, int x, int y, boolean sendEvent) {
	int verticalScrollOffset = getVerticalScrollOffset();
	blockXAnchor = anchorX + horizontalScrollOffset;
	blockYAnchor = anchorY + verticalScrollOffset;
	selectionAnchors[0] = getOffsetAtPoint(anchorX, anchorY, null);
	setBlockSelectionLocation(x, y, sendEvent);
}
void setBlockSelectionOffset (int offset, boolean sendEvent) {
	Point point = getPointAtOffset(offset);
	int verticalScrollOffset = getVerticalScrollOffset();
	blockXLocation = point.x + horizontalScrollOffset;
	blockYLocation = point.y + verticalScrollOffset;
	setCaretOffsets(new int[] {offset}, SWT.DEFAULT);
	if (blockXAnchor == -1) {
		blockXAnchor = blockXLocation;
		blockYAnchor = blockYLocation;
		selectionAnchors[0] = caretOffsets[0];
	}
	doBlockSelection(sendEvent);
}
void setBlockSelectionOffset (int anchorOffset, int offset, boolean sendEvent) {
	int verticalScrollOffset = getVerticalScrollOffset();
	Point anchorPoint = getPointAtOffset(anchorOffset);
	blockXAnchor = anchorPoint.x + horizontalScrollOffset;
	blockYAnchor = anchorPoint.y + verticalScrollOffset;
	selectionAnchors[0] = anchorOffset;
	setBlockSelectionOffset(offset, sendEvent);
}

/**
 * Sets the receiver's caret.  Set the caret's height and location.
 *
 * @param caret the new caret for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void setCaret(Caret caret) {
	checkWidget ();
	super.setCaret(caret);
	caretDirection = SWT.NULL;
	if (caret != null) {
		setCaretLocations();
		if(carets != null) {
			for (int i = 1; i < carets.length; i++) {
				carets[i].dispose();
			}
		}
		carets = new Caret[] {caret};
	} else {
		carets = null;
	}
}
/**
 * Sets the BIDI coloring mode.  When true the BIDI text display
 * algorithm is applied to segments of text that are the same
 * color.
 *
 * @param mode the new coloring mode
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @deprecated use BidiSegmentListener instead.
 */
@Deprecated
public void setBidiColoring(boolean mode) {
	checkWidget();
	bidiColoring = mode;
}
/**
 * Sets the bottom margin.
 *
 * @param bottomMargin the bottom margin.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.5
 */
public void setBottomMargin (int bottomMargin) {
	checkWidget();
	setMargins(getLeftMargin(), topMargin, rightMargin, bottomMargin);
}
/**
 * Moves the Caret to the current caret offset.
 */
void setCaretLocations() {
	Point[] newCaretPos = Arrays.stream(caretOffsets).mapToObj(this::getPointAtOffset).toArray(Point[]::new);
	setCaretLocations(newCaretPos, getCaretDirection());
}
void setCaretLocations(final Point[] locations, int direction) {
	Caret firstCaret = getCaret();
	if (firstCaret != null) {
		if (carets == null || carets.length == 0) {
			carets = new Caret[] { firstCaret };
		}
		final boolean isDefaultCaret = firstCaret == defaultCaret;
		if (locations.length > carets.length) {
			int formerCaretCount = carets.length;
			carets = Arrays.copyOf(carets, locations.length);
			for (int i = formerCaretCount; i < carets.length; i++) {
				carets[i] = new Caret(this, firstCaret.getStyle());
				carets[i].setImage(firstCaret.getImage());
				carets[i].setFont(firstCaret.getFont());
				carets[i].setSize(firstCaret.getSize());
			}
		} else if (locations.length < carets.length) {
			for (int i = locations.length; i < carets.length; i++) {
				carets[i].dispose();
			}
			carets = Arrays.copyOf(carets, locations.length);
		}
		for (int i = Math.min(caretOffsets.length, locations.length)-1; i>=0; i--) { // reverse order, seee bug 579028#c7
			final Caret caret = carets[i];
			final int caretOffset = caretOffsets[i];
			final Point location = locations[i];
			final StyleRange styleAtOffset = content.getCharCount() > 0 ?
				(caretOffset < content.getCharCount() ?
					getStyleRangeAtOffset(caretOffset) :
					getStyleRangeAtOffset(content.getCharCount() - 1)) : // caret after last char: use last char style
				null;
			final int caretLine = content.getLineAtOffset(caretOffset);

			int graphicalLineHeight = getLineHeight();
			final int lineStartOffset = getOffsetAtLine(caretLine);
			int graphicalLineFirstOffset = lineStartOffset;
			final int lineEndOffset = lineStartOffset + getLine(caretLine).length();
			int graphicalLineLastOffset = lineEndOffset;
			if (caretLine < getLineCount() && renderer.getLineHeight(caretLine) != getLineHeight()) { // word wrap, metrics, styles...
				graphicalLineHeight = getLineHeight(caretOffset);
				final Rectangle characterBounds = getBoundsAtOffset(caretOffset);
				graphicalLineFirstOffset = getOffsetAtPoint(new Point(leftMargin, characterBounds.y));
				graphicalLineLastOffset = getOffsetAtPoint(new Point(leftMargin, characterBounds.y + graphicalLineHeight)) - 1;
				if (graphicalLineLastOffset < graphicalLineFirstOffset) {
					graphicalLineLastOffset = getCharCount();
				}
			}

			int caretHeight = getLineHeight();
			boolean isTextAlignedAtBottom = true;
			if (graphicalLineFirstOffset >= 0) {
				for (StyleRange style : getStyleRanges(graphicalLineFirstOffset, graphicalLineLastOffset - graphicalLineFirstOffset)) {
					isTextAlignedAtBottom &= (
						(style.font == null || Objects.equals(style.font, getFont())) &&
						style.rise >= 0 &&
						(style.metrics == null || style.metrics.descent <= 0)
					);
				}
			}
			if (!isTextAlignedAtBottom || (styleAtOffset != null && styleAtOffset.isVariableHeight())) {
				if (isDefaultCaret) {
					direction = SWT.DEFAULT;
					caretHeight = graphicalLineHeight;
				} else {
					caretHeight = caret.getSize().y;
				}
			}
			if (isTextAlignedAtBottom && caretHeight < graphicalLineHeight) {
				location.y += (graphicalLineHeight - caretHeight);
			}

			int imageDirection = direction;
			if (isMirrored()) {
				if (imageDirection == SWT.LEFT) {
					imageDirection = SWT.RIGHT;
				} else if (imageDirection == SWT.RIGHT) {
					imageDirection = SWT.LEFT;
				}
			}
			if (isDefaultCaret && imageDirection == SWT.RIGHT) {
				location.x -= (caret.getSize().x - 1);
			}
			if (isDefaultCaret) {
				caret.setBounds(location.x, location.y, caretWidth, caretHeight);
			} else {
				caret.setLocation(location);
			}
			if (direction != caretDirection) {
				caretDirection = direction;
				if (isDefaultCaret) {
					if (imageDirection == SWT.DEFAULT) {
						defaultCaret.setImage(null);
					} else if (imageDirection == SWT.LEFT) {
						defaultCaret.setImage(leftCaretBitmap);
					} else if (imageDirection == SWT.RIGHT) {
						defaultCaret.setImage(rightCaretBitmap);
					}
				}
				if (caretDirection == SWT.LEFT) {
					BidiUtil.setKeyboardLanguage(BidiUtil.KEYBOARD_NON_BIDI);
				} else if (caretDirection == SWT.RIGHT) {
					BidiUtil.setKeyboardLanguage(BidiUtil.KEYBOARD_BIDI);
				}
			}
		}
		updateCaretVisibility();
		super.redraw();
	}
	columnX = locations[0].x;
}
/**
 * Sets the caret offset.
 *
 * @param offset caret offset, relative to the first character in the text.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_ARGUMENT when the offset is inside a multi byte line
 *   delimiter (and thus neither clearly in front of or after the line delimiter)
 * </ul>
 */
public void setCaretOffset(int offset) {
	checkWidget();
	int length = getCharCount();
	if (length > 0 && !Arrays.equals(caretOffsets, new int[] {offset})) {
		if (offset < 0) {
			offset = 0;
		} else if (offset > length) {
			offset = length;
		} else {
			if (isLineDelimiter(offset)) {
				// offset is inside a multi byte line delimiter. This is an
				// illegal operation and an exception is thrown. Fixes 1GDKK3R
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
		}
		setCaretOffsets(new int[] {offset}, PREVIOUS_OFFSET_TRAILING);
		// clear the selection if the caret is moved.
		// don't notify listeners about the selection change.
		if (blockSelection) {
			clearBlockSelection(true, false);
		} else {
			clearSelection(false);
		}
	}
	setCaretLocations();
}
void setCaretOffsets(int[] newOffsets, int alignment) {
	if (newOffsets.length > 1) {
		newOffsets = Arrays.stream(newOffsets).distinct().sorted().toArray();
	}
	if (!Arrays.equals(caretOffsets, newOffsets)) {
		caretOffsets = newOffsets;
		if (isListening(ST.CaretMoved)) {
			StyledTextEvent event = new StyledTextEvent(content);
			event.end = caretOffsets[caretOffsets.length - 1];
			notifyListeners(ST.CaretMoved, event);
		}
	}
	if (alignment != SWT.DEFAULT) {
		caretAlignment = alignment;
	}
}
/**
 * Copies the specified text range to the clipboard.  The text will be placed
 * in the clipboard in plain text format and RTF format.
 *
 * @param start start index of the text
 * @param length length of text to place in clipboard
 *
 * @exception SWTError
 * @see org.eclipse.swt.dnd.Clipboard#setContents
 */
void setClipboardContent(int start, int length, int clipboardType) throws SWTError {
	if (clipboardType == DND.SELECTION_CLIPBOARD && !IS_GTK) return;
	TextTransfer plainTextTransfer = TextTransfer.getInstance();
	TextWriter plainTextWriter = new TextWriter(start, length);
	String plainText = getPlatformDelimitedText(plainTextWriter);
	Object[] data;
	Transfer[] types;
	if (clipboardType == DND.SELECTION_CLIPBOARD) {
		data = new Object[]{plainText};
		types = new Transfer[]{plainTextTransfer};
	} else {
		RTFTransfer rtfTransfer = RTFTransfer.getInstance();
		RTFWriter rtfWriter = new RTFWriter(start, length);
		String rtfText = getPlatformDelimitedText(rtfWriter);
		data = new Object[]{rtfText, plainText};
		types = new Transfer[]{rtfTransfer, plainTextTransfer};
	}
	clipboard.setContents(data, types, clipboardType);
}
/**
 * Sets the content implementation to use for text storage.
 *
 * @param newContent StyledTextContent implementation to use for text storage.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when listener is null</li>
 * </ul>
 */
public void setContent(StyledTextContent newContent) {
	checkWidget();
	if (newContent == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	if (content != null) {
		content.removeTextChangeListener(textChangeListener);
	}
	content = newContent;
	content.addTextChangeListener(textChangeListener);
	reset();
}
/**
 * Sets the receiver's cursor to the cursor specified by the
 * argument.  Overridden to handle the null case since the
 * StyledText widget uses an ibeam as its default cursor.
 *
 * @see Control#setCursor(Cursor)
 */
@Override
public void setCursor (Cursor cursor) {
	checkWidget();
	if (cursor != null && cursor.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.cursor = cursor;
	if (cursor == null) {
		Display display = getDisplay();
		int type = blockSelection ? SWT.CURSOR_CROSS : SWT.CURSOR_IBEAM;
		super.setCursor(display.getSystemCursor(type));
	} else {
		super.setCursor(cursor);
	}
}
/**
 * Sets whether the widget implements double click mouse behavior.
 *
 * @param enable if true double clicking a word selects the word, if false
 * 	double clicks have the same effect as regular mouse clicks.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDoubleClickEnabled(boolean enable) {
	checkWidget();
	doubleClickEnabled = enable;
}
@Override
public void setDragDetect (boolean dragDetect) {
	checkWidget ();
	this.dragDetect = dragDetect;
}
/**
 * Sets whether the widget content can be edited.
 *
 * @param editable if true content can be edited, if false content can not be
 * 	edited
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEditable(boolean editable) {
	checkWidget();
	this.editable = editable;
}
@Override
public void setEnabled(boolean enabled) {
	super.setEnabled(enabled);
	Display display = getDisplay();
	this.enabled = enabled;
	this.insideSetEnableCall = true;
	try {
		if (enabled && editable) {
			if (!customBackground) setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
			if (!customForeground) setForeground(display.getSystemColor(SWT.COLOR_LIST_FOREGROUND));
		} else if(!enabled) {
			if (!customBackground) setBackground(display.getSystemColor(SWT.COLOR_TEXT_DISABLED_BACKGROUND));
			if (!customForeground) setForeground(display.getSystemColor(SWT.COLOR_WIDGET_DISABLED_FOREGROUND));
		} else if(!editable) {
			if (!customBackground) setBackground(display.getSystemColor(SWT.COLOR_TEXT_DISABLED_BACKGROUND));
			if (!customForeground) setForeground(display.getSystemColor(SWT.COLOR_LIST_FOREGROUND));
		}
	}
	finally {
		this.insideSetEnableCall = false;
	}
}

@Override
public boolean setFocus() {
	boolean focusGained = super.setFocus();
	if (focusGained && hasMultipleCarets()) {
		// Multiple carets need to update their drawing. See bug 579179
		setCaretLocations();
	}
	return focusGained;
}

private boolean hasMultipleCarets() {
	return carets != null && carets.length > 1;
}

/**
 * Sets a new font to render text with.
 * <p>
 * <b>NOTE:</b> Italic fonts are not supported unless they have no overhang
 * and the same baseline as regular fonts.
 * </p>
 *
 * @param font new font
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void setFont(Font font) {
	checkWidget();
	int oldLineHeight = renderer.getLineHeight();
	super.setFont(font);
	renderer.setFont(getFont(), tabLength);
	// keep the same top line visible. fixes 5815
	if (isFixedLineHeight()) {
		int lineHeight = renderer.getLineHeight();
		if (lineHeight != oldLineHeight) {
			int vscroll = (getVerticalScrollOffset() * lineHeight / oldLineHeight) - getVerticalScrollOffset();
			scrollVertical(vscroll, true);
		}
	}
	resetCache(0, content.getLineCount());
	claimBottomFreeSpace();
	calculateScrollBars();
	if (isBidiCaret()) createCaretBitmaps();
	caretDirection = SWT.NULL;
	setCaretLocations();
	super.redraw();
}
@Override
public void setForeground(Color color) {
	checkWidget();
	boolean foregroundDisabled = false;
	if (!this.enabled && color == null) {
		if (foreground != null) {
			Color disabledFg = getDisplay().getSystemColor(SWT.COLOR_WIDGET_DISABLED_FOREGROUND);
			if (foreground.equals(disabledFg)) {
				return;
			} else {
				color = new Color (disabledFg.getRGBA());
				foregroundDisabled = true;
			}
		}
	}
	customForeground = color != null && !this.insideSetEnableCall && !foregroundDisabled;
	foreground = color;
	super.setForeground(color);
	resetCache(0, content.getLineCount());
	setCaretLocations();
	super.redraw();
}
/**
 * Sets the horizontal scroll offset relative to the start of the line.
 * Do nothing if there is no text set.
 * <p>
 * <b>NOTE:</b> The horizontal index is reset to 0 when new text is set in the
 * widget.
 * </p>
 *
 * @param offset horizontal scroll offset relative to the start
 * 	of the line, measured in character increments starting at 0, if
 * 	equal to 0 the content is not scrolled, if &gt; 0 = the content is scrolled.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHorizontalIndex(int offset) {
	checkWidget();
	if (getCharCount() == 0) {
		return;
	}
	if (offset < 0) {
		offset = 0;
	}
	offset *= getHorizontalIncrement();
	// allow any value if client area width is unknown or 0.
	// offset will be checked in resize handler.
	// don't use isVisible since width is known even if widget
	// is temporarily invisible
	if (clientAreaWidth > 0) {
		int width = renderer.getWidth();
		// prevent scrolling if the content fits in the client area.
		// align end of longest line with right border of client area
		// if offset is out of range.
		if (offset > width - clientAreaWidth) {
			offset = Math.max(0, width - clientAreaWidth);
		}
	}
	scrollHorizontal(offset - horizontalScrollOffset, true);
}
/**
 * Sets the horizontal SWT logical point offset relative to the start of the line.
 * Do nothing if there is no text set.
 * <p>
 * <b>NOTE:</b> The horizontal SWT logical point offset is reset to 0 when new text
 * is set in the widget.
 * </p>
 *
 * @param pixel horizontal SWT logical point offset relative to the start
 * 	of the line.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 2.0
 */
public void setHorizontalPixel(int pixel) {
	checkWidget();
	if (getCharCount() == 0) {
		return;
	}
	if (pixel < 0) {
		pixel = 0;
	}
	// allow any value if client area width is unknown or 0.
	// offset will be checked in resize handler.
	// don't use isVisible since width is known even if widget
	// is temporarily invisible
	if (clientAreaWidth > 0) {
		int width = renderer.getWidth();
		// prevent scrolling if the content fits in the client area.
		// align end of longest line with right border of client area
		// if offset is out of range.
		if (pixel > width - clientAreaWidth) {
			pixel = Math.max(0, width - clientAreaWidth);
		}
	}
	scrollHorizontal(pixel - horizontalScrollOffset, true);
}
/**
 * Sets the line indentation of the widget.
 * <p>
 * It is the amount of blank space, in points, at the beginning of each line.
 * When a line wraps in several lines only the first one is indented.
 * </p>
 *
 * @param indent the new indent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setLineIndent(int, int, int)
 *
 * @since 3.2
 */
public void setIndent(int indent) {
	checkWidget();
	if (this.indent == indent || indent < 0) return;
	this.indent = indent;
	resetCache(0, content.getLineCount());
	setCaretLocations();
	super.redraw();
}
/**
 * Sets whether the widget should justify lines.
 *
 * @param justify whether lines should be justified
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setLineJustify(int, int, boolean)
 *
 * @since 3.2
 */
public void setJustify(boolean justify) {
	checkWidget();
	if (this.justify == justify) return;
	this.justify = justify;
	resetCache(0, content.getLineCount());
	setCaretLocations();
	super.redraw();
}
/**
 * Maps a key to an action.
 * <p>
 * One action can be associated with N keys. However, each key can only
 * have one action (key:action is N:1 relation).
 * </p>
 *
 * @param key a key code defined in SWT.java or a character.
 * 	Optionally ORd with a state mask.  Preferred state masks are one or more of
 *  SWT.MOD1, SWT.MOD2, SWT.MOD3, since these masks account for modifier platform
 *  differences.  However, there may be cases where using the specific state masks
 *  (i.e., SWT.CTRL, SWT.SHIFT, SWT.ALT, SWT.COMMAND) makes sense.
 * @param action one of the predefined actions defined in ST.java.
 * 	Use SWT.NULL to remove a key binding.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setKeyBinding(int key, int action) {
	checkWidget();
	int modifierValue = key & SWT.MODIFIER_MASK;
	int keyInt = key & SWT.KEY_MASK;
	char keyChar = (char)keyInt;
	/**
	 * Bug 440535: Make sure the key getting mapped to letter is in defiened
	 * character range and filter out incorrect int to char typecasting. For
	 * Example: SWT.KEYPAD_CR int gets wrongly type-cast to char letter 'p'
	 */
	if (Character.isDefined(keyInt) && Character.isLetter(keyChar)) {
		// make the keybinding case insensitive by adding it
		// in its upper and lower case form
		char ch = Character.toUpperCase(keyChar);
		int newKey = ch | modifierValue;
		if (action == SWT.NULL) {
			keyActionMap.remove(newKey);
		} else {
			keyActionMap.put(newKey, action);
		}
		ch = Character.toLowerCase(keyChar);
		newKey = ch | modifierValue;
		if (action == SWT.NULL) {
			keyActionMap.remove(newKey);
		} else {
			keyActionMap.put(newKey, action);
		}
	} else {
		if (action == SWT.NULL) {
			keyActionMap.remove(key);
		} else {
			keyActionMap.put(key, action);
		}
	}
}
/**
 * Sets the left margin.
 *
 * @param leftMargin the left margin.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.5
 */
public void setLeftMargin (int leftMargin) {
	checkWidget();
	setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
}
/**
 * Sets the alignment of the specified lines. The argument should be one of <code>SWT.LEFT</code>,
 * <code>SWT.CENTER</code> or <code>SWT.RIGHT</code>.
 * <p>
 * Note that if <code>SWT.MULTI</code> is set, then <code>SWT.WRAP</code> must also be set
 * in order to stabilize the right edge before setting alignment.
 * </p><p>
 * Should not be called if a LineStyleListener has been set since the listener
 * maintains the line attributes.
 * </p><p>
 * All line attributes are maintained relative to the line text, not the
 * line index that is specified in this method call.
 * During text changes, when entire lines are inserted or removed, the line
 * attributes that are associated with the lines after the change
 * will "move" with their respective text. An entire line is defined as
 * extending from the first character on a line to the last and including the
 * line delimiter.
 * </p>
 * When two lines are joined by deleting a line delimiter, the top line
 * attributes take precedence and the attributes of the bottom line are deleted.
 * For all other text changes line attributes will remain unchanged.
 *
 * @param startLine first line the alignment is applied to, 0 based
 * @param lineCount number of lines the alignment applies to.
 * @param alignment line alignment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_ARGUMENT when the specified line range is invalid</li>
 * </ul>
 * @see #setAlignment(int)
 * @since 3.2
 */
public void setLineAlignment(int startLine, int lineCount, int alignment) {
	checkWidget();
	if (isListening(ST.LineGetStyle)) return;
	if (startLine < 0 || startLine + lineCount > content.getLineCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	renderer.setLineAlignment(startLine, lineCount, alignment);
	resetCache(startLine, lineCount);
	redrawLines(startLine, lineCount, false);
	if (Arrays.stream(caretOffsets).map(content::getLineAtOffset).anyMatch(caretLine -> startLine <= caretLine && caretLine < startLine + lineCount)) {
		setCaretLocations();
	}
	setAlignment();
}
/**
 * Sets the background color of the specified lines.
 * <p>
 * The background color is drawn for the width of the widget. All
 * line background colors are discarded when setText is called.
 * The text background color if defined in a StyleRange overlays the
 * line background color.
 * </p><p>
 * Should not be called if a LineBackgroundListener has been set since the
 * listener maintains the line backgrounds.
 * </p><p>
 * All line attributes are maintained relative to the line text, not the
 * line index that is specified in this method call.
 * During text changes, when entire lines are inserted or removed, the line
 * attributes that are associated with the lines after the change
 * will "move" with their respective text. An entire line is defined as
 * extending from the first character on a line to the last and including the
 * line delimiter.
 * </p><p>
 * When two lines are joined by deleting a line delimiter, the top line
 * attributes take precedence and the attributes of the bottom line are deleted.
 * For all other text changes line attributes will remain unchanged.
 * </p>
 *
 * @param startLine first line the color is applied to, 0 based
 * @param lineCount number of lines the color applies to.
 * @param background line background color
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_ARGUMENT when the specified line range is invalid</li>
 * </ul>
 */
public void setLineBackground(int startLine, int lineCount, Color background) {
	checkWidget();
	if (isListening(ST.LineGetBackground)) return;
	if (startLine < 0 || startLine + lineCount > content.getLineCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (background != null) {
		renderer.setLineBackground(startLine, lineCount, background);
	} else {
		renderer.clearLineBackground(startLine, lineCount);
	}
	redrawLines(startLine, lineCount, false);
}
/**
 * Sets the bullet of the specified lines.
 * <p>
 * Should not be called if a LineStyleListener has been set since the listener
 * maintains the line attributes.
 * </p><p>
 * All line attributes are maintained relative to the line text, not the
 * line index that is specified in this method call.
 * During text changes, when entire lines are inserted or removed, the line
 * attributes that are associated with the lines after the change
 * will "move" with their respective text. An entire line is defined as
 * extending from the first character on a line to the last and including the
 * line delimiter.
 * </p><p>
 * When two lines are joined by deleting a line delimiter, the top line
 * attributes take precedence and the attributes of the bottom line are deleted.
 * For all other text changes line attributes will remain unchanged.
 * </p>
 *
 * @param startLine first line the bullet is applied to, 0 based
 * @param lineCount number of lines the bullet applies to.
 * @param bullet line bullet
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_ARGUMENT when the specified line range is invalid</li>
 * </ul>
 * @since 3.2
 */
public void setLineBullet(int startLine, int lineCount, Bullet bullet) {
	checkWidget();
	if (isListening(ST.LineGetStyle)) return;
	if (startLine < 0 || startLine + lineCount > content.getLineCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	int oldBottom = getLinePixel(startLine + lineCount);
	renderer.setLineBullet(startLine, lineCount, bullet);
	resetCache(startLine, lineCount);
	int newBottom = getLinePixel(startLine + lineCount);
	redrawLines(startLine, lineCount, oldBottom != newBottom);
	if (Arrays.stream(caretOffsets).map(content::getLineAtOffset).anyMatch(caretLine -> startLine <= caretLine && caretLine < startLine + lineCount)) {
		setCaretLocations();
	}
}
/**
 * Returns true if StyledText is in word wrap mode and false otherwise.
 *
 * @return true if StyledText is in word wrap mode and false otherwise.
 */
boolean isWordWrap() {
	return wordWrap || visualWrap;
}
/**
 * Sets the indent of the specified lines.
 * <p>
 * Should not be called if a LineStyleListener has been set since the listener
 * maintains the line attributes.
 * </p><p>
 * All line attributes are maintained relative to the line text, not the
 * line index that is specified in this method call.
 * During text changes, when entire lines are inserted or removed, the line
 * attributes that are associated with the lines after the change
 * will "move" with their respective text. An entire line is defined as
 * extending from the first character on a line to the last and including the
 * line delimiter.
 * </p><p>
 * When two lines are joined by deleting a line delimiter, the top line
 * attributes take precedence and the attributes of the bottom line are deleted.
 * For all other text changes line attributes will remain unchanged.
 * </p>
 *
 * @param startLine first line the indent is applied to, 0 based
 * @param lineCount number of lines the indent applies to.
 * @param indent line indent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_ARGUMENT when the specified line range is invalid</li>
 * </ul>
 * @see #setIndent(int)
 * @since 3.2
 */
public void setLineIndent(int startLine, int lineCount, int indent) {
	checkWidget();
	if (isListening(ST.LineGetStyle)) return;
	if (startLine < 0 || startLine + lineCount > content.getLineCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	int oldBottom = getLinePixel(startLine + lineCount);
	renderer.setLineIndent(startLine, lineCount, indent);
	resetCache(startLine, lineCount);
	int newBottom = getLinePixel(startLine + lineCount);
	redrawLines(startLine, lineCount, oldBottom != newBottom);
	if (Arrays.stream(caretOffsets).map(content::getLineAtOffset).anyMatch(caretLine -> startLine <= caretLine && caretLine < startLine + lineCount)) {
		setCaretLocations();
	}
}

/**
 * Sets the vertical indent of the specified lines.
 * <p>
 * Should not be called if a LineStyleListener has been set since the listener
 * maintains the line attributes.
 * </p><p>
 * All line attributes are maintained relative to the line text, not the
 * line index that is specified in this method call.
 * During text changes, when entire lines are inserted or removed, the line
 * attributes that are associated with the lines after the change
 * will "move" with their respective text. An entire line is defined as
 * extending from the first character on a line to the last and including the
 * line delimiter.
 * </p><p>
 * When two lines are joined by deleting a line delimiter, the top line
 * attributes take precedence and the attributes of the bottom line are deleted.
 * For all other text changes line attributes will remain unchanged.
 * </p><p>
 * Setting both line spacing and vertical indent on a line would result in the
 * spacing and indent add up for the line.
 * </p>
 *
 * @param lineIndex line index the vertical indent is applied to, 0 based
 * @param verticalLineIndent vertical line indent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_ARGUMENT when the specified line index is invalid</li>
 * </ul>
 * @since 3.109
 */
public void setLineVerticalIndent(int lineIndex, int verticalLineIndent) {
	checkWidget();
	if (isListening(ST.LineGetStyle)) return;
	if (lineIndex < 0 || lineIndex >= content.getLineCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	int previousVerticalIndent = renderer.getLineVerticalIndent(lineIndex);
	if (verticalLineIndent == previousVerticalIndent) {
		return;
	}
	int initialTopPixel = getTopPixel();
	int initialTopIndex = getPartialTopIndex();
	int initialBottomIndex = getPartialBottomIndex();
	int verticalIndentDiff = verticalLineIndent - previousVerticalIndent;
	renderer.setLineVerticalIndent(lineIndex, verticalLineIndent);
	this.hasVerticalIndent = verticalLineIndent != 0 || renderer.hasVerticalIndent();
	ScrollBar verticalScrollbar = getVerticalBar();
	if (lineIndex < initialTopIndex) {
		verticalScrollOffset += verticalIndentDiff; // just change value, don't actually scroll/redraw
		if (verticalScrollbar != null) {
			verticalScrollbar.setSelection(verticalScrollOffset);
			verticalScrollbar.setMaximum(verticalScrollbar.getMaximum() + verticalIndentDiff);
		}
	} else if (lineIndex > initialBottomIndex) {
		if (verticalScrollbar != null) {
			verticalScrollbar.setMaximum(verticalScrollbar.getMaximum() + verticalIndentDiff);
		}
	} else {
		resetCache(lineIndex, 1);
		if((initialTopIndex == 0) && (initialBottomIndex == (content.getLineCount() - 1))) { // not scrollable editor
		    setCaretLocations();
			redrawLines(lineIndex, getBottomIndex() - lineIndex + 1, true);
		} else if (getFirstCaretLine() >= initialTopIndex && getFirstCaretLine() <= initialBottomIndex) { // caret line with caret mustn't move
			if (getFirstCaretLine() < lineIndex) {
				redrawLines(lineIndex, getPartialBottomIndex() - lineIndex + 1, true);
			} else {
				setTopPixel(initialTopPixel + verticalIndentDiff);
			}
		} else { // move as few lines as possible
			if (lineIndex - getTopIndex() < getBottomIndex() - lineIndex) {
				setTopPixel(initialTopPixel + verticalIndentDiff);
			} else {
				// redraw below
				redrawLines(lineIndex, getPartialBottomIndex() - lineIndex + 1, true);
			}
		}
		setScrollBars(true);
	}
}

/**
 * Sets the justify of the specified lines.
 * <p>
 * Should not be called if a LineStyleListener has been set since the listener
 * maintains the line attributes.
 * </p><p>
 * All line attributes are maintained relative to the line text, not the
 * line index that is specified in this method call.
 * During text changes, when entire lines are inserted or removed, the line
 * attributes that are associated with the lines after the change
 * will "move" with their respective text. An entire line is defined as
 * extending from the first character on a line to the last and including the
 * line delimiter.
 * </p><p>
 * When two lines are joined by deleting a line delimiter, the top line
 * attributes take precedence and the attributes of the bottom line are deleted.
 * For all other text changes line attributes will remain unchanged.
 * </p>
 *
 * @param startLine first line the justify is applied to, 0 based
 * @param lineCount number of lines the justify applies to.
 * @param justify true if lines should be justified
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_ARGUMENT when the specified line range is invalid</li>
 * </ul>
 * @see #setJustify(boolean)
 * @since 3.2
 */
public void setLineJustify(int startLine, int lineCount, boolean justify) {
	checkWidget();
	if (isListening(ST.LineGetStyle)) return;
	if (startLine < 0 || startLine + lineCount > content.getLineCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	renderer.setLineJustify(startLine, lineCount, justify);
	resetCache(startLine, lineCount);
	redrawLines(startLine, lineCount, false);
	if (Arrays.stream(caretOffsets).map(content::getLineAtOffset).anyMatch(caretLine -> startLine <= caretLine && caretLine < startLine + lineCount)) {
		setCaretLocations();
	}
}
/**
 * Sets the line spacing of the widget. The line spacing applies for all lines.
 * In the case of #setLineSpacingProvider(StyledTextLineSpacingProvider) is customized,
 * the line spacing are applied only for the lines which are not managed by {@link StyledTextLineSpacingProvider}.
 *
 * @param lineSpacing the line spacing
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @see #setLineSpacingProvider(StyledTextLineSpacingProvider)
 * @since 3.2
 */
public void setLineSpacing(int lineSpacing) {
	checkWidget();
	if (this.lineSpacing == lineSpacing || lineSpacing < 0) return;
	this.lineSpacing = lineSpacing;
	resetCache(0, content.getLineCount());
	setCaretLocations();
	super.redraw();
}
/**
 * Sets the line spacing provider of the widget. The line spacing applies for some lines with customized spacing
 * or reset the customized spacing if the argument is null.
 *
 * @param lineSpacingProvider the line spacing provider (or null)
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @see #setLineSpacingProvider(StyledTextLineSpacingProvider)
 * @since 3.107
 */
public void setLineSpacingProvider(StyledTextLineSpacingProvider lineSpacingProvider) {
	checkWidget();
	boolean wasFixedLineHeight = isFixedLineHeight();
	if (renderer.getLineSpacingProvider() == null && lineSpacingProvider == null
			|| (renderer.getLineSpacingProvider() != null
					&& renderer.getLineSpacingProvider().equals(lineSpacingProvider)))
		return;
	renderer.setLineSpacingProvider(lineSpacingProvider);
	// reset lines cache if needed
	if (lineSpacingProvider == null) {
		if (!wasFixedLineHeight) {
			resetCache(0, content.getLineCount());
		}
	} else {
		if (wasFixedLineHeight) {
			int firstLine = -1;
			for (int i = 0; i < content.getLineCount(); i++) {
				Integer lineSpacing = lineSpacingProvider.getLineSpacing(i);
				if (lineSpacing != null && lineSpacing > 0) {
					// there is a custom line spacing, set StyledText as variable line height mode
					// reset only the line size
					renderer.reset(i, 1);
					if (firstLine == -1) {
						firstLine = i;
					}
				}
			}
			if (firstLine != -1) {
				// call reset cache for the first line which have changed to recompute scrollbars
				resetCache(firstLine, 0);
			}
		}
	}
	setCaretLocations();
	super.redraw();
}
/**
 * Sets the tab stops of the specified lines.
 * <p>
 * Should not be called if a <code>LineStyleListener</code> has been set since the listener
 * maintains the line attributes.
 * </p><p>
 * All line attributes are maintained relative to the line text, not the
 * line index that is specified in this method call.
 * During text changes, when entire lines are inserted or removed, the line
 * attributes that are associated with the lines after the change
 * will "move" with their respective text. An entire line is defined as
 * extending from the first character on a line to the last and including the
 * line delimiter.
 * </p><p>
 * When two lines are joined by deleting a line delimiter, the top line
 * attributes take precedence and the attributes of the bottom line are deleted.
 * For all other text changes line attributes will remain unchanged.
 * </p>
 *
 * @param startLine first line the justify is applied to, 0 based
 * @param lineCount number of lines the justify applies to.
 * @param tabStops tab stops
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_ARGUMENT when the specified line range is invalid</li>
 * </ul>
 * @see #setTabStops(int[])
 * @since 3.6
 */
public void setLineTabStops(int startLine, int lineCount, int[] tabStops) {
	checkWidget();
	if (isListening(ST.LineGetStyle)) return;
	if (startLine < 0 || startLine + lineCount > content.getLineCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (tabStops != null) {
		int pos = 0;
		int[] newTabs = new int[tabStops.length];
		for (int i = 0; i < tabStops.length; i++) {
			if (tabStops[i] < pos) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			newTabs[i] = pos = tabStops[i];
		}
		renderer.setLineTabStops(startLine, lineCount, newTabs);
	} else {
		renderer.setLineTabStops(startLine, lineCount, null);
	}
	resetCache(startLine, lineCount);
	redrawLines(startLine, lineCount, false);
	if (Arrays.stream(caretOffsets).map(content::getLineAtOffset).anyMatch(caretLine -> startLine <= caretLine && caretLine < startLine + lineCount)) {
		setCaretLocations();
	}
}
/**
 * Sets the wrap indent of the specified lines.
 * <p>
 * Should not be called if a <code>LineStyleListener</code> has been set since the listener
 * maintains the line attributes.
 * </p><p>
 * All line attributes are maintained relative to the line text, not the
 * line index that is specified in this method call.
 * During text changes, when entire lines are inserted or removed, the line
 * attributes that are associated with the lines after the change
 * will "move" with their respective text. An entire line is defined as
 * extending from the first character on a line to the last and including the
 * line delimiter.
 * </p><p>
 * When two lines are joined by deleting a line delimiter, the top line
 * attributes take precedence and the attributes of the bottom line are deleted.
 * For all other text changes line attributes will remain unchanged.
 * </p>
 *
 * @param startLine first line the wrap indent is applied to, 0 based
 * @param lineCount number of lines the wrap indent applies to.
 * @param wrapIndent line wrap indent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_ARGUMENT when the specified line range is invalid</li>
 * </ul>
 * @see #setWrapIndent(int)
 * @since 3.6
 */
public void setLineWrapIndent(int startLine, int lineCount, int wrapIndent) {
	checkWidget();
	if (isListening(ST.LineGetStyle)) return;
	if (startLine < 0 || startLine + lineCount > content.getLineCount()) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	int oldBottom = getLinePixel(startLine + lineCount);
	renderer.setLineWrapIndent(startLine, lineCount, wrapIndent);
	resetCache(startLine, lineCount);
	int newBottom = getLinePixel(startLine + lineCount);
	redrawLines(startLine, lineCount, oldBottom != newBottom);
	if (Arrays.stream(caretOffsets).map(content::getLineAtOffset).anyMatch(caretLine -> startLine <= caretLine && caretLine < startLine + lineCount)) {
		setCaretLocations();
	}
}

/**
 * Sets the color of the margins.
 *
 * @param color the new color (or null)
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.5
 */
public void setMarginColor(Color color) {
	checkWidget();
	if (color != null && color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	marginColor = color;
	super.redraw();
}
/**
 * Sets the margins.
 *
 * @param leftMargin the left margin.
 * @param topMargin the top margin.
 * @param rightMargin the right margin.
 * @param bottomMargin the bottom margin.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.5
 */
public void setMargins (int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
	checkWidget();
	this.leftMargin = Math.max(0, leftMargin) + alignmentMargin;
	this.topMargin = Math.max(0, topMargin);
	this.rightMargin = Math.max(0, rightMargin);
	this.bottomMargin = Math.max(0, bottomMargin);
	resetCache(0, content.getLineCount());
	setScrollBars(true);
	setCaretLocations();
	setAlignment();
	super.redraw();
}
/**
 * Sets the enabled state of the mouse navigator. When the mouse navigator is enabled, the user can navigate through the widget
 * by pressing the middle button and moving the cursor.
 *
 * @param enabled if true, the mouse navigator is enabled, if false the mouse navigator is deactivated
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.110
 */
public void setMouseNavigatorEnabled(boolean enabled) {
	checkWidget();
	if ((enabled && mouseNavigator != null) || (!enabled && mouseNavigator == null)) {
		return;
	}
	if (enabled) {
		mouseNavigator = new MouseNavigator(this);
	} else {
		mouseNavigator.dispose();
		mouseNavigator = null;
	}
}
/**
 * Flips selection anchor based on word selection direction.
 */
void setMouseWordSelectionAnchor() {
	if (doubleClickEnabled && clickCount > 1) {
		if (caretOffsets[0] < doubleClickSelection.x) {
			selectionAnchors[0] = doubleClickSelection.y;
		} else if (caretOffsets[0] > doubleClickSelection.y) {
			selectionAnchors[0] = doubleClickSelection.x;
		}
	}
}
/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 *
 * @param orientation new orientation style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 2.1.2
 */
@Override
public void setOrientation(int orientation) {
	int oldOrientation = getOrientation();
	super.setOrientation(orientation);
	int newOrientation = getOrientation();
	if (oldOrientation != newOrientation) {
		resetBidiData();
	}
}
/**
 * Sets the right margin.
 *
 * @param rightMargin the right margin.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.5
 */
public void setRightMargin (int rightMargin) {
	checkWidget();
	setMargins(getLeftMargin(), topMargin, rightMargin, bottomMargin);
}
void setScrollBar(ScrollBar bar, int clientArea, int maximum, int margin) {
	int inactive = 1;
	if (clientArea < maximum) {
		bar.setMaximum(maximum - margin);
		bar.setThumb(clientArea - margin);
		bar.setPageIncrement(clientArea - margin);
		if (!alwaysShowScroll) bar.setVisible(true);
	} else if (bar.getThumb() != inactive || bar.getMaximum() != inactive) {
		bar.setValues(bar.getSelection(), bar.getMinimum(), inactive, inactive, bar.getIncrement(), inactive);
	}
}
/**
 * Adjusts the maximum and the page size of the scroll bars to
 * reflect content width/length changes.
 *
 * @param vertical indicates if the vertical scrollbar also needs to be set
 */
void setScrollBars(boolean vertical) {
	ignoreResize++;
	if (!isFixedLineHeight() || !alwaysShowScroll) vertical = true;
	ScrollBar verticalBar = vertical ? getVerticalBar() : null;
	ScrollBar horizontalBar = getHorizontalBar();
	int oldHeight = clientAreaHeight;
	int oldWidth = clientAreaWidth;
	if (!alwaysShowScroll) {
		if (verticalBar != null) verticalBar.setVisible(false);
		if (horizontalBar != null) horizontalBar.setVisible(false);
	}
	if (verticalBar != null) {
		setScrollBar(verticalBar, clientAreaHeight, renderer.getHeight(), topMargin + bottomMargin);
	}
	if (horizontalBar != null && !wordWrap) {
		setScrollBar(horizontalBar, clientAreaWidth, renderer.getWidth(), leftMargin + rightMargin);
		if (!alwaysShowScroll && horizontalBar.getVisible() && verticalBar != null) {
			setScrollBar(verticalBar, clientAreaHeight, renderer.getHeight(), topMargin + bottomMargin);
			if (verticalBar.getVisible()) {
				setScrollBar(horizontalBar, clientAreaWidth, renderer.getWidth(), leftMargin + rightMargin);
			}
		}
	}
	if (!alwaysShowScroll) {
		redrawMargins(oldHeight, oldWidth);
	}
	ignoreResize--;
}
/**
 * Sets the selection to the given position and scrolls it into view.  Equivalent to setSelection(start,start).
 *
 * @param start new caret position
 * @see #setSelection(int,int)
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_ARGUMENT when either the start or the end of the selection range is inside a
 * multi byte line delimiter (and thus neither clearly in front of or after the line delimiter)
 * </ul>
 */
public void setSelection(int start) {
	// checkWidget test done in setSelectionRange
	setSelection(start, start);
}
/**
 * Sets the selection and scrolls it into view.
 * <p>
 * Indexing is zero based.  Text selections are specified in terms of
 * caret positions.  In a text widget that contains N characters, there are
 * N+1 caret positions, ranging from 0..N
 * </p>
 *
 * @param point x=selection start offset, y=selection end offset
 * 	The caret will be placed at the selection start when x &gt; y.
 * @see #setSelection(int,int)
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_NULL_ARGUMENT when point is null</li>
 *   <li>ERROR_INVALID_ARGUMENT when either the start or the end of the selection range is inside a
 * multi byte line delimiter (and thus neither clearly in front of or after the line delimiter)
 * </ul>
 */
public void setSelection(Point point) {
	checkWidget();
	if (point == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	setSelection(point.x, point.y);
}
/**
 * Sets the receiver's selection background color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 2.1
 */
public void setSelectionBackground (Color color) {
	checkWidget ();
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	selectionBackground = color;
	resetCache(0, content.getLineCount());
	setCaretLocations();
	super.redraw();
}
/**
 * Sets the receiver's selection foreground color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 * <p>
 * Note that this is a <em>HINT</em>. Some platforms do not allow the application
 * to change the selection foreground color.
 * </p>
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 2.1
 */
public void setSelectionForeground (Color color) {
	checkWidget ();
	if (color != null) {
		if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	selectionForeground = color;
	resetCache(0, content.getLineCount());
	setCaretLocations();
	super.redraw();
}
/**
 * Sets the selection and scrolls it into view.
 * <p>
 * Indexing is zero based.  Text selections are specified in terms of
 * caret positions.  In a text widget that contains N characters, there are
 * N+1 caret positions, ranging from 0..N
 * </p>
 *
 * @param start selection start offset. The caret will be placed at the
 * 	selection start when start &gt; end.
 * @param end selection end offset
 * @see #setSelectionRange(int,int)
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_ARGUMENT when either the start or the end of the selection range is inside a
 * multi byte line delimiter (and thus neither clearly in front of or after the line delimiter)
 * </ul>
 */
public void setSelection(int start, int end) {
	setSelectionRange(start, end - start);
	showSelection();
}
/**
 * Sets the selection.
 * <p>
 * The new selection may not be visible. Call showSelection to scroll
 * the selection into view.
 * </p>
 *
 * @param start offset of the first selected character, start &gt;= 0 must be true.
 * @param length number of characters to select, 0 &lt;= start + length
 * 	&lt;= getCharCount() must be true.
 * 	A negative length places the caret at the selection start.
 * @param sendEvent a Selection event is sent when set to true and when
 * 	the selection is reset.
 */
void setSelection(int[] regions, boolean sendEvent, boolean doBlock) {
	if (regions.length == 2 && selection.length == 1) { // single range before/after
		int start = regions[0];
		int length = regions[1];
		int end = start + length;
		if (start > end) {
			int temp = end;
			end = start;
			start = temp;
		}
		int selectionAnchor = selectionAnchors[0];
		// is the selection range different or is the selection direction
		// different?
		if (selection[0].x != start || selection[0].y != end ||
			(length > 0 && selectionAnchor != selection[0].x) ||
			(length < 0 && selectionAnchor != selection[0].y)) {
			if (blockSelection && doBlock) {
				if (length < 0) {
					setBlockSelectionOffset(end, start, sendEvent);
				} else {
					setBlockSelectionOffset(start, end, sendEvent);
				}
			} else {
				int oldStart = selection[0].x;
				int oldLength = selection[0].y - selection[0].x;
				int charCount = content.getCharCount();
				// called internally to remove selection after text is removed
				// therefore make sure redraw range is valid.
				int redrawX = Math.min(selection[0].x, charCount);
				int redrawY = Math.min(selection[0].y, charCount);
				if (length < 0) {
					selectionAnchors[0] = selectionAnchor = selection[0].y = end;
					selection[0].x = start;
					setCaretOffsets(new int[] {start}, PREVIOUS_OFFSET_TRAILING);
				} else {
					selectionAnchors[0] = selectionAnchor = selection[0].x = start;
					selection[0].y = end;
					setCaretOffsets(new int[] {end}, PREVIOUS_OFFSET_TRAILING);
				}
				redrawX = Math.min(redrawX, selection[0].x);
				redrawY = Math.max(redrawY, selection[0].y);
				if (redrawY - redrawX > 0) {
					internalRedrawRange(redrawX, redrawY - redrawX);
				}
				if (sendEvent && (oldLength != end - start || (oldLength != 0 && oldStart != start))) {
					sendSelectionEvent();
				}
				sendAccessibleTextCaretMoved();
			}
		}
	} else if (!blockSelection || !doBlock) {
		boolean caretAtEndOfSelection = regions[1] > 0;
		int charCount = content.getCharCount();
		Point[] newRanges = new Point[regions.length / 2];
		for (int i = 0; i < regions.length; i += 2) {
			int start = regions[i];
			int length = regions[i + 1];
			int end = start + length;
			if (start > end) {
				int temp = end;
				end = start;
				start = temp;
			}
			newRanges[i / 2] = new Point(start, end);
		}
		Arrays.sort(newRanges, SELECTION_COMPARATOR);
		// merge contiguous ranges
		int newRangeIndex = 0;
		for (Point range : newRanges) {
			if (newRangeIndex > 0) {
				Point previousRange = newRanges[newRangeIndex - 1];
				if (previousRange.y >= range.x) {
					previousRange.y = Math.max(previousRange.y, range.y);
				} else {
					newRanges[newRangeIndex] = range;
					newRangeIndex++;
				}
			} else {
				newRanges[newRangeIndex] = range;
				newRangeIndex++;
			}
		}
		Point[] toRedraw = new Point[newRangeIndex + selection.length];
		System.arraycopy(newRanges, 0, toRedraw, 0, newRangeIndex);
		System.arraycopy(selection, 0, toRedraw, newRangeIndex, selection.length);
		Arrays.sort(toRedraw, SELECTION_COMPARATOR);
		Point[] formerSelection = selection;
		selection = Arrays.copyOf(newRanges, newRangeIndex);
		Point currentToRedraw = null;
		for (Point p : toRedraw) {
			if (currentToRedraw == null) {
				currentToRedraw = new Point(p.x, p.y);
			} else if (currentToRedraw.y >= p.x - 1) {
				// expand if necessary
				currentToRedraw = new Point(currentToRedraw.x, Math.max(p.y, currentToRedraw.y));
			} else {
				currentToRedraw = new Point(Math.max(0, currentToRedraw.x), Math.min(charCount, currentToRedraw.y));
				internalRedrawRange(currentToRedraw.x, currentToRedraw.y - currentToRedraw.x);
				currentToRedraw = null;
			}
		}
		if (currentToRedraw != null) {
			currentToRedraw = new Point(Math.max(0, currentToRedraw.x), Math.min(charCount, currentToRedraw.y));
			internalRedrawRange(currentToRedraw.x, currentToRedraw.y - currentToRedraw.x);
		}
		if (!caretAtEndOfSelection) {
			selectionAnchors = Arrays.stream(selection).mapToInt(p -> p.y).toArray();
			setCaretOffsets(Arrays.stream(selection).mapToInt(p -> p.x).toArray(), PREVIOUS_OFFSET_TRAILING);
		} else {
			selectionAnchors = Arrays.stream(selection).mapToInt(p -> p.x).toArray();
			setCaretOffsets(Arrays.stream(selection).mapToInt(p -> p.y).toArray(), PREVIOUS_OFFSET_TRAILING);
		}
		setCaretLocations();
		if (sendEvent && !Arrays.equals(formerSelection, selection)) {
			sendSelectionEvent();
		}
		sendAccessibleTextCaretMoved();
	}
}
/**
 * Sets the selection.
 * <p>
 * The new selection may not be visible. Call showSelection to scroll the selection
 * into view. A negative length places the caret at the visual start of the selection.
 * </p>
 *
 * @param start offset of the first selected character
 * @param length number of characters to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_ARGUMENT when either the start or the end of the selection range is inside a
 * multi byte line delimiter (and thus neither clearly in front of or after the line delimiter)
 * </ul>
 */
public void setSelectionRange(int start, int length) {
	setSelectionRanges(new int[] { start, length });
}

/**
 * Sets the selected locations/ranges.
 * <p>
 * The new selection may not be visible. Call showSelection to scroll the selection
 * into view. A negative length places the caret at the visual start of the selection.
 * </p>
 *
 * @param ranges an array of offset/length pairs.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_ARGUMENT when either the start or the end of one selection range is inside a
 * multi byte line delimiter (and thus neither clearly in front of or after the line delimiter)</li>
 *   <li>ERROR_INVALID_ARGUMENT when ranges are null or size isn't valid (not pair)</li>
 * </ul>
 * @see #getSelectionRanges()
 * @since 3.117
 */
public void setSelectionRanges(int[] ranges) {
	checkWidget();
	int contentLength = getCharCount();
	if (ranges.length % 2 != 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	int[] fixedRanges = Arrays.copyOf(ranges, ranges.length);
	for (int i = 0; i < ranges.length; i+=2) {
		int start = ranges[i];
		start = Math.max(0, Math.min(start, contentLength));
		int length = ranges[i + 1];
		int end = start + length;
		if (end < 0) {
			length = -start;
		} else if (end > contentLength) {
			length = contentLength - start;
		}
		if (isLineDelimiter(start) || isLineDelimiter(start + length)) {
			// the start offset or end offset of the selection range is inside a
			// multi byte line delimiter. This is an illegal operation and an exception
			// is thrown. Fixes 1GDKK3R
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		fixedRanges[i] = start;
		fixedRanges[i + 1] = length;
	}
	setSelection(fixedRanges, false, true);
	setCaretLocations();
}

/**
 * Adds the specified style.
 * <p>
 * The new style overwrites existing styles for the specified range.
 * Existing style ranges are adjusted if they partially overlap with
 * the new style. To clear an individual style, call setStyleRange
 * with a StyleRange that has null attributes.
 * </p><p>
 * Should not be called if a LineStyleListener has been set since the
 * listener maintains the styles.
 * </p>
 *
 * @param range StyleRange object containing the style information.
 * Overwrites the old style in the given range. May be null to delete
 * all styles.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_INVALID_RANGE when the style range is outside the valid range (&gt; getCharCount())</li>
 * </ul>
 */
public void setStyleRange(StyleRange range) {
	checkWidget();
	if (isListening(ST.LineGetStyle)) return;
	if (range != null) {
		if (range.isUnstyled()) {
			setStyleRanges(range.start, range.length, null, null, false);
		} else {
			setStyleRanges(range.start, 0, null, new StyleRange[]{range}, false);
		}
	} else {
		setStyleRanges(0, 0, null, null, true);
	}
}
/**
 * Clears the styles in the range specified by <code>start</code> and
 * <code>length</code> and adds the new styles.
 * <p>
 * The ranges array contains start and length pairs.  Each pair refers to
 * the corresponding style in the styles array.  For example, the pair
 * that starts at ranges[n] with length ranges[n+1] uses the style
 * at styles[n/2].  The range fields within each StyleRange are ignored.
 * If ranges or styles is null, the specified range is cleared.
 * </p><p>
 * Note: It is expected that the same instance of a StyleRange will occur
 * multiple times within the styles array, reducing memory usage.
 * </p><p>
 * Should not be called if a LineStyleListener has been set since the
 * listener maintains the styles.
 * </p>
 *
 * @param start offset of first character where styles will be deleted
 * @param length length of the range to delete styles in
 * @param ranges the array of ranges.  The ranges must not overlap and must be in order.
 * @param styles the array of StyleRanges.  The range fields within the StyleRange are unused.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when an element in the styles array is null</li>
 *    <li>ERROR_INVALID_RANGE when the number of ranges and style do not match (ranges.length * 2 == styles.length)</li>
 *    <li>ERROR_INVALID_RANGE when a range is outside the valid range (&gt; getCharCount() or less than zero)</li>
 *    <li>ERROR_INVALID_RANGE when a range overlaps</li>
 * </ul>
 *
 * @since 3.2
 */
public void setStyleRanges(int start, int length, int[] ranges, StyleRange[] styles) {
	checkWidget();
	if (isListening(ST.LineGetStyle)) return;
	if (ranges == null || styles == null) {
		setStyleRanges(start, length, null, null, false);
	} else {
		setStyleRanges(start, length, ranges, styles, false);
	}
}
/**
 * Sets styles to be used for rendering the widget content.
 * <p>
 * All styles in the widget will be replaced with the given set of ranges and styles.
 * The ranges array contains start and length pairs.  Each pair refers to
 * the corresponding style in the styles array.  For example, the pair
 * that starts at ranges[n] with length ranges[n+1] uses the style
 * at styles[n/2].  The range fields within each StyleRange are ignored.
 * If either argument is null, the styles are cleared.
 * </p><p>
 * Note: It is expected that the same instance of a StyleRange will occur
 * multiple times within the styles array, reducing memory usage.
 * </p><p>
 * Should not be called if a LineStyleListener has been set since the
 * listener maintains the styles.
 * </p>
 *
 * @param ranges the array of ranges.  The ranges must not overlap and must be in order.
 * @param styles the array of StyleRanges.  The range fields within the StyleRange are unused.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when an element in the styles array is null</li>
 *    <li>ERROR_INVALID_RANGE when the number of ranges and style do not match (ranges.length * 2 == styles.length)</li>
 *    <li>ERROR_INVALID_RANGE when a range is outside the valid range (&gt; getCharCount() or less than zero)</li>
 *    <li>ERROR_INVALID_RANGE when a range overlaps</li>
 * </ul>
 *
 * @since 3.2
 */
public void setStyleRanges(int[] ranges, StyleRange[] styles) {
	checkWidget();
	if (isListening(ST.LineGetStyle)) return;
	if (ranges == null || styles == null) {
		setStyleRanges(0, 0, null, null, true);
	} else {
		setStyleRanges(0, 0, ranges, styles, true);
	}
}
void setStyleRanges(int start, int length, int[] ranges, StyleRange[] styles, boolean reset) {
	int charCount = content.getCharCount();
	if (reset) {
		start = 0;
		length = charCount;
	}
	int[] formerRanges = getRanges(start, length);
	StyleRange[] formerStyles = getStyleRanges(start, length);
	int end = start + length;
	final boolean wasFixedLineHeight = isFixedLineHeight();
	if (start > end || start < 0) {
		SWT.error(SWT.ERROR_INVALID_RANGE);
	}
	if (styles != null) {
		if (end > charCount) {
			SWT.error(SWT.ERROR_INVALID_RANGE);
		}
		if (ranges != null) {
			if (ranges.length != styles.length << 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		int lastOffset = 0;
		for (int i = 0; i < styles.length; i ++) {
			if (styles[i] == null) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			int rangeStart, rangeLength;
			if (ranges != null) {
				rangeStart = ranges[i << 1];
				rangeLength = ranges[(i << 1) + 1];
			} else {
				rangeStart = styles[i].start;
				rangeLength = styles[i].length;
			}
			if (rangeLength < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			if (!(0 <= rangeStart && rangeStart + rangeLength <= charCount)) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			if (lastOffset > rangeStart) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			hasStyleWithVariableHeight |= styles[i].isVariableHeight();
			lastOffset = rangeStart + rangeLength;
		}
	}
	int rangeStart = start, rangeEnd = end;
	if (styles != null && styles.length > 0) {
		if (ranges != null) {
			rangeStart = ranges[0];
			rangeEnd = ranges[ranges.length - 2] + ranges[ranges.length - 1];
		} else {
			rangeStart = styles[0].start;
			rangeEnd = styles[styles.length - 1].start + styles[styles.length - 1].length;
		}
	}

	// This needs to happen before new styles are applied
	int expectedBottom = 0;
	if (!isFixedLineHeight() && !reset) {
		int lineEnd = content.getLineAtOffset(Math.max(end, rangeEnd));
		int partialTopIndex = getPartialTopIndex();
		int partialBottomIndex = getPartialBottomIndex();
		if (partialTopIndex <= lineEnd && lineEnd <= partialBottomIndex) {
			expectedBottom = getLinePixel(lineEnd + 1);
		}
	}
	if (reset) {
		renderer.setStyleRanges(null, null);
	} else {
		renderer.updateRanges(start, length, length);
	}
	if (styles != null && styles.length > 0) {
		renderer.setStyleRanges(ranges, styles);
	}

	// re-evaluate variable height with all styles (including new ones)
	hasStyleWithVariableHeight = false;
	for (StyleRange style : getStyleRanges(false)) {
		hasStyleWithVariableHeight = style.isVariableHeight();
		if (hasStyleWithVariableHeight) break;
	}

	SortedSet<Integer> modifiedLines = computeModifiedLines(formerRanges, formerStyles, ranges, styles);
	resetCache(modifiedLines);
	if (reset) {
		super.redraw();
	} else {
		int lineStart = content.getLineAtOffset(Math.min(start, rangeStart));
		int lineEnd = content.getLineAtOffset(Math.max(end, rangeEnd));
		int partialTopIndex = getPartialTopIndex();
		int partialBottomIndex = getPartialBottomIndex();
		if (!(lineStart > partialBottomIndex || lineEnd < partialTopIndex)) {
			int top = 0;
			int bottom = clientAreaHeight;
			if (partialTopIndex <= lineStart && lineStart <= partialBottomIndex) {
				top = Math.max(0, getLinePixel(lineStart));
			}
			if (partialTopIndex <= lineEnd && lineEnd <= partialBottomIndex) {
				bottom = getLinePixel(lineEnd + 1);
			}
			if (!(wasFixedLineHeight && isFixedLineHeight()) && bottom != expectedBottom) {
				bottom = clientAreaHeight;
			}
			super.redraw(0, top, clientAreaWidth, bottom - top, false);
		}
	}
	int oldColumnX = columnX;
	setCaretLocations();
	columnX = oldColumnX;
	doMouseLinkCursor();
}

/**
 *
 * @param referenceRanges former ranges, sorted by order and without overlapping, typically returned {@link #getRanges(int, int)}
 * @param referenceStyles
 * @param newRanges former ranges, sorted by order and without overlapping
 * @param newStyles
 * @return
 */
private SortedSet<Integer> computeModifiedLines(int[] referenceRanges, StyleRange[] referenceStyles, int[] newRanges, StyleRange[] newStyles) {
	if (referenceStyles == null) {
		referenceStyles = new StyleRange[0];
	}
	if (referenceRanges == null) {
		referenceRanges = createRanges(referenceStyles);
	}
	if (newStyles == null) {
		newStyles = new StyleRange[0];
	}
	if (newRanges == null) {
		newRanges = createRanges(newStyles);
	}
	if (referenceRanges.length != 2 * referenceStyles.length) {
		throw new IllegalArgumentException();
	}
	if (newRanges.length != 2 * newStyles.length) {
		throw new IllegalArgumentException();
	}
	SortedSet<Integer> res = new TreeSet<>();
	int referenceRangeIndex = 0;
	int newRangeIndex = 0;
	StyleRange defaultStyle = new StyleRange();
	defaultStyle.foreground = this.foreground;
	defaultStyle.background = this.background;
	defaultStyle.font = getFont();
	int currentOffset = referenceRanges.length > 0 ? referenceRanges[0] : Integer.MAX_VALUE;
	if (newRanges.length > 0) {
		currentOffset = Math.min(currentOffset, newRanges[0]);
	}
	while (currentOffset < content.getCharCount() && (referenceRangeIndex < referenceStyles.length || newRangeIndex < newRanges.length)) {
		int nextMilestoneOffset = Integer.MAX_VALUE; // next new range start/end after current offset

		while (referenceRangeIndex < referenceStyles.length && endRangeOffset(referenceRanges, referenceRangeIndex) <= currentOffset) {
			referenceRangeIndex++;
		}
		StyleRange referenceStyleAtCurrentOffset = defaultStyle;
		if (isInRange(referenceRanges, referenceRangeIndex, currentOffset)) { // has styling
			referenceStyleAtCurrentOffset = referenceStyles[referenceRangeIndex];
			nextMilestoneOffset =  endRangeOffset(referenceRanges, referenceRangeIndex);
		} else if (referenceRangeIndex < referenceStyles.length) { // no range, default styling
			nextMilestoneOffset = referenceRanges[2 * referenceRangeIndex]; // beginning of next range
		}

		while (newRangeIndex < newStyles.length && endRangeOffset(newRanges, newRangeIndex) <= currentOffset) {
			newRangeIndex++;
		}
		StyleRange newStyleAtCurrentOffset = defaultStyle;
		if (isInRange(newRanges, newRangeIndex, currentOffset)) {
			newStyleAtCurrentOffset = newStyles[newRangeIndex];
			nextMilestoneOffset = Math.min(nextMilestoneOffset, endRangeOffset(newRanges, newRangeIndex));
		} else if (newRangeIndex < newStyles.length) {
			nextMilestoneOffset = Math.min(nextMilestoneOffset, newRanges[2 * newRangeIndex]);
		}

		if (!referenceStyleAtCurrentOffset.similarTo(newStyleAtCurrentOffset)) {
			int fromLine = getLineAtOffset(currentOffset);
			int toLine = getLineAtOffset(nextMilestoneOffset - 1);
			for (int line = fromLine; line <= toLine; line++) {
				res.add(line);
			}
			currentOffset = toLine + 1 < getLineCount() ? getOffsetAtLine(toLine + 1) : content.getCharCount();
		} else {
			currentOffset = nextMilestoneOffset;
		}
	}
	return res;
}
private int[] createRanges(StyleRange[] referenceStyles) {
	int[] referenceRanges;
	referenceRanges = new int[2 * referenceStyles.length];
	for (int i = 0; i < referenceStyles.length; i++) {
		referenceRanges[2 * i] = referenceStyles[i].start;
		referenceRanges[2 * i + 1] = referenceStyles[i].length;
	}
	return referenceRanges;
}

private boolean isInRange(int[] ranges, int styleIndex, int offset) {
	if (ranges == null || ranges.length == 0 || styleIndex < 0 || 2 * styleIndex + 1 > ranges.length) {
		return false;
	}
	int start = ranges[2 * styleIndex];
	int length = ranges[2 * styleIndex + 1];
	return offset >= start && offset < start + length;
}

/**
 * The offset on which the range ends (excluded)
 * @param ranges
 * @param styleIndex
 * @return
 */
private int endRangeOffset(int[] ranges, int styleIndex) {
	if (styleIndex < 0 || 2 * styleIndex > ranges.length) {
		throw new IllegalArgumentException();
	}
	int start = ranges[2 * styleIndex];
	int length = ranges[2 * styleIndex + 1];
	return start + length;
}

/**
 * Sets styles to be used for rendering the widget content. All styles
 * in the widget will be replaced with the given set of styles.
 * <p>
 * Note: Because a StyleRange includes the start and length, the
 * same instance cannot occur multiple times in the array of styles.
 * If the same style attributes, such as font and color, occur in
 * multiple StyleRanges, <code>setStyleRanges(int[], StyleRange[])</code>
 * can be used to share styles and reduce memory usage.
 * </p><p>
 * Should not be called if a LineStyleListener has been set since the
 * listener maintains the styles.
 * </p>
 *
 * @param ranges StyleRange objects containing the style information.
 * The ranges should not overlap. The style rendering is undefined if
 * the ranges do overlap. Must not be null. The styles need to be in order.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when the list of ranges is null</li>
 *    <li>ERROR_INVALID_RANGE when the last of the style ranges is outside the valid range (&gt; getCharCount())</li>
 * </ul>
 *
 * @see #setStyleRanges(int[], StyleRange[])
 */
public void setStyleRanges(StyleRange[] ranges) {
	checkWidget();
	if (isListening(ST.LineGetStyle)) return;
	if (ranges == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	setStyleRanges(0, 0, null, ranges, true);
}
/**
 * Sets the tab width.
 *
 * @param tabs tab width measured in characters.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setTabStops(int[])
 */
public void setTabs(int tabs) {
	checkWidget();
	tabLength = tabs;
	renderer.setFont(null, tabs);
	resetCache(0, content.getLineCount());
	setCaretLocations();
	super.redraw();
}

/**
 * Sets the receiver's tab list. Each value in the tab list specifies
 * the space in points from the origin of the document to the respective
 * tab stop.  The last tab stop width is repeated continuously.
 *
 * @param tabs the new tab list (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if a tab stop is negative or less than the previous stop in the list</li>
 * </ul>
 *
 * @see StyledText#getTabStops()
 *
 * @since 3.6
 */
public void setTabStops(int [] tabs) {
	checkWidget();
	if (tabs != null) {
		int pos = 0;
		int[] newTabs = new int[tabs.length];
		for (int i = 0; i < tabs.length; i++) {
			if (tabs[i] < pos) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			newTabs[i] = pos = tabs[i];
		}
		this.tabs = newTabs;
	} else {
		this.tabs = null;
	}
	resetCache(0, content.getLineCount());
	setCaretLocations();
	super.redraw();
}

/**
 * Sets the widget content.
 * If the widget has the SWT.SINGLE style and "text" contains more than
 * one line, only the first line is rendered but the text is stored
 * unchanged. A subsequent call to getText will return the same text
 * that was set.
 * <p>
 * <b>Note:</b> Only a single line of text should be set when the SWT.SINGLE
 * style is used.
 * </p>
 *
 * @param text new widget content. Replaces existing content. Line styles
 * 	that were set using StyledText API are discarded.  The
 * 	current selection is also discarded.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT when string is null</li>
 * </ul>
 */
public void setText(String text) {
	checkWidget();
	if (text == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	Event event = new Event();
	event.start = 0;
	event.end = getCharCount();
	event.text = text;
	event.doit = true;
	notifyListeners(SWT.Verify, event);
	if (event.doit) {
		StyledTextEvent styledTextEvent = null;
		if (isListening(ST.ExtendedModify)) {
			styledTextEvent = new StyledTextEvent(content);
			styledTextEvent.start = event.start;
			styledTextEvent.end = event.start + event.text.length();
			styledTextEvent.text = content.getTextRange(event.start, event.end - event.start);
		}
		content.setText(event.text);
		notifyListeners(SWT.Modify, event);
		if (styledTextEvent != null) {
			notifyListeners(ST.ExtendedModify, styledTextEvent);
		}
	}
}

/**
 * Sets the base text direction (a.k.a. "paragraph direction") of the receiver,
 * which must be one of the constants <code>SWT.LEFT_TO_RIGHT</code> or
 * <code>SWT.RIGHT_TO_LEFT</code>.
 * <p>
 * <code>setOrientation</code> would override this value with the text direction
 * that is consistent with the new orientation.
 * </p>
 * <p>
 * <b>Warning</b>: This API is currently only implemented on Windows.
 * It doesn't set the base text direction on GTK and Cocoa.
 * </p>
 *
 * @param textDirection the base text direction style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#FLIP_TEXT_DIRECTION
 */
@Override
public void setTextDirection(int textDirection) {
	checkWidget();
	int oldStyle = getStyle();
	super.setTextDirection(textDirection);
	if (isAutoDirection () || oldStyle != getStyle()) {
		resetBidiData();
	}
}

/**
 * Sets the text limit to the specified number of characters.
 * <p>
 * The text limit specifies the amount of text that
 * the user can type into the widget.
 * </p>
 *
 * @param limit the new text limit.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *   <li>ERROR_CANNOT_BE_ZERO when limit is 0</li>
 * </ul>
 */
public void setTextLimit(int limit) {
	checkWidget();
	if (limit == 0) {
		SWT.error(SWT.ERROR_CANNOT_BE_ZERO);
	}
	textLimit = limit;
}
/**
 * Sets the top index. Do nothing if there is no text set.
 * <p>
 * The top index is the index of the line that is currently at the top
 * of the widget. The top index changes when the widget is scrolled.
 * Indexing starts from zero.
 * Note: The top index is reset to 0 when new text is set in the widget.
 * </p>
 *
 * @param topIndex new top index. Must be between 0 and
 * 	getLineCount() - fully visible lines per page. If no lines are fully
 * 	visible the maximum value is getLineCount() - 1. An out of range
 * 	index will be adjusted accordingly.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTopIndex(int topIndex) {
	checkWidget();
	if (getCharCount() == 0) {
		return;
	}
	int lineCount = content.getLineCount(), pixel;
	if (isFixedLineHeight()) {
		int pageSize = Math.max(1, Math.min(lineCount, getLineCountWhole()));
		if (topIndex < 0) {
			topIndex = 0;
		} else if (topIndex > lineCount - pageSize) {
			topIndex = lineCount - pageSize;
		}
		pixel = getLinePixel(topIndex);
	} else {
		topIndex = Math.max(0, Math.min(lineCount - 1, topIndex));
		pixel = getLinePixel(topIndex);
		if (pixel > 0) {
			pixel = getAvailableHeightBellow(pixel);
		} else {
			pixel = getAvailableHeightAbove(pixel);
		}
	}
	scrollVertical(pixel, true);
}
/**
 * Sets the top margin.
 *
 * @param topMargin the top margin.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.5
 */
public void setTopMargin (int topMargin) {
	checkWidget();
	setMargins(getLeftMargin(), topMargin, rightMargin, bottomMargin);
}
/**
 * Sets the top SWT logical point offset. Do nothing if there is no text set.
 * <p>
 * The top point offset is the vertical SWT logical point offset of the widget. The
 * widget is scrolled so that the given SWT logical point position is at the top.
 * The top index is adjusted to the corresponding top line.
 * Note: The top point is reset to 0 when new text is set in the widget.
 * </p>
 *
 * @param pixel new top point offset. Must be between 0 and
 * 	(getLineCount() - visible lines per page) / getLineHeight()). An out
 * 	of range offset will be adjusted accordingly.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 2.0
 */
public void setTopPixel(int pixel) {
	checkWidget();
	if (getCharCount() == 0) {
		return;
	}
	if (pixel < 0) pixel = 0;
	int lineCount = content.getLineCount();
	int height = clientAreaHeight - topMargin - bottomMargin;
	int verticalOffset = getVerticalScrollOffset();
	if (isFixedLineHeight()) {
		int maxTopPixel = Math.max(0, lineCount * getVerticalIncrement() - height);
		if (pixel > maxTopPixel) pixel = maxTopPixel;
		pixel -= verticalOffset;
	} else {
		pixel -= verticalOffset;
		if (pixel > 0) {
			pixel = getAvailableHeightBellow(pixel);
		}
	}
	scrollVertical(pixel, true);
}
/**
 * Sets whether the widget wraps lines.
 * <p>
 * This overrides the creation style bit SWT.WRAP.
 * </p>
 *
 * @param wrap true=widget wraps lines, false=widget does not wrap lines
 * @since 2.0
 */
public void setWordWrap(boolean wrap) {
	checkWidget();
	if ((getStyle() & SWT.SINGLE) != 0) return;
	if (wordWrap == wrap) return;
	if (wordWrap && blockSelection) setBlockSelection(false);
	wordWrap = wrap;
	resetCache(0, content.getLineCount());
	horizontalScrollOffset = 0;
	ScrollBar horizontalBar = getHorizontalBar();
	if (horizontalBar != null) {
		horizontalBar.setVisible(!wordWrap);
	}
	setScrollBars(true);
	setCaretLocations();
	super.redraw();
}
/**
 * Sets the wrap line indentation of the widget.
 * <p>
 * It is the amount of blank space, in points, at the beginning of each wrapped line.
 * When a line wraps in several lines all the lines but the first one is indented
 * by this amount.
 * </p>
 *
 * @param wrapIndent the new wrap indent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setLineWrapIndent(int, int, int)
 *
 * @since 3.6
 */
public void setWrapIndent(int wrapIndent) {
	checkWidget();
	if (this.wrapIndent == wrapIndent || wrapIndent < 0) return;
	this.wrapIndent = wrapIndent;
	resetCache(0, content.getLineCount());
	setCaretLocations();
	super.redraw();
}
boolean showLocation(Rectangle rect, boolean scrollPage) {
	boolean scrolled = false;
	if (rect.y < topMargin) {
		scrolled = scrollVertical(rect.y - topMargin, true);
	} else if (rect.y + rect.height > clientAreaHeight - bottomMargin) {
		if (clientAreaHeight - topMargin - bottomMargin <= 0) {
			scrolled = scrollVertical(rect.y - topMargin, true);
		} else {
			scrolled = scrollVertical(rect.y + rect.height - (clientAreaHeight - bottomMargin), true);
		}
	}
	int width = clientAreaWidth - rightMargin - leftMargin;
	if (width > 0) {
		int minScroll = scrollPage ? width / 4 : 0;
		if (rect.x < leftMargin) {
			int scrollWidth = Math.max(leftMargin - rect.x, minScroll);
			int maxScroll = horizontalScrollOffset;
			scrolled = scrollHorizontal(-Math.min(maxScroll, scrollWidth), true);
		} else if (rect.x + rect.width > (clientAreaWidth - rightMargin)) {
			int scrollWidth =  Math.max(rect.x + rect.width - (clientAreaWidth - rightMargin), minScroll);
			int maxScroll = renderer.getWidth() - horizontalScrollOffset - clientAreaWidth;
			scrolled = scrollHorizontal(Math.min(maxScroll, scrollWidth), true);
		}
	}
	return scrolled;
}
/**
 * Sets the caret location and scrolls the caret offset into view.
 */
void showCaret() {
	Rectangle bounds = getBoundsAtOffset(caretOffsets[0]);
	if (!showLocation(bounds, true) || (carets != null && caretOffsets.length != carets.length)) {
		setCaretLocations();
	}
}
/**
 * Scrolls the selection into view.
 * <p>
 * The end of the selection will be scrolled into view.
 * Note that if a right-to-left selection exists, the end of the selection is
 * the visual beginning of the selection (i.e., where the caret is located).
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void showSelection() {
	checkWidget();
	// is selection from right-to-left?
	boolean rightToLeft = caretOffsets[0] == selection[0].x;
	int startOffset, endOffset;
	if (rightToLeft) {
		startOffset = selection[0].y;
		endOffset = selection[0].x;
	} else {
		startOffset = selection[0].x;
		endOffset = selection[0].y;
	}

	Rectangle startBounds = getBoundsAtOffset(startOffset);
	Rectangle endBounds = getBoundsAtOffset(endOffset);

	// can the selection be fully displayed within the widget's visible width?
	int w = clientAreaWidth - leftMargin - rightMargin;
	boolean selectionFits = rightToLeft ? startBounds.x - endBounds.x <= w : endBounds.x - startBounds.x <= w;
	if (selectionFits) {
		// show as much of the selection as possible by first showing
		// the start of the selection
		if (showLocation(startBounds, false)) {
			// endX value could change if showing startX caused a scroll to occur
			endBounds = getBoundsAtOffset(endOffset);
		}
		// the character at endOffset is not part of the selection
		endBounds.width = endOffset == caretOffsets[0] ? getCaretWidth() : 0;
		showLocation(endBounds, false);
	} else {
		// just show the end of the selection since the selection start
		// will not be visible
		showLocation(endBounds, true);
	}
}
void updateCaretVisibility() {
	Caret caret = getCaret();
	if (caret != null) {
		if (carets == null || carets.length == 0) {
			carets = new Caret[] { caret };
		}
		if (blockSelection && blockXLocation != -1) {
			Arrays.stream(carets).forEach(c -> c.setVisible(false));
		} else {
			Arrays.stream(carets).forEach(c -> {
				Point location = c.getLocation();
				Point size = c.getSize();
				boolean visible =
					topMargin <= location.y + size.y && location.y <= clientAreaHeight - bottomMargin &&
					leftMargin <= location.x + size.x && location.x <= clientAreaWidth - rightMargin;
				c.setVisible(visible);
			});
		}
	}
}
/**
 * Updates the selection and caret position depending on the text change.
 * <p>
 * If the selection intersects with the replaced text, the selection is
 * reset and the caret moved to the end of the new text.
 * If the selection is behind the replaced text it is moved so that the
 * same text remains selected.  If the selection is before the replaced text
 * it is left unchanged.
 * </p>
 *
 * @param startOffset offset of the text change
 * @param replacedLength length of text being replaced
 * @param newLength length of new text
 */
void updateSelection(int startOffset, int replacedLength, int newLength) {
	if (selection[selection.length - 1].y <= startOffset) {
		// selection ends before text change
		if (isWordWrap()) setCaretLocations();
		return;
	}
	// clear selection fragment before text change
	Arrays.stream(selection)
		.filter(sel -> sel.y > startOffset)
		.filter(sel -> sel.x < startOffset)
		.forEach(sel -> internalRedrawRange(sel.x, startOffset - sel.x));
	Arrays.stream(selection)
		.filter(sel -> sel.y > startOffset)
		.filter(sel -> sel.y > startOffset + replacedLength && sel.x < startOffset + replacedLength)
		.forEach(sel -> {
			// clear selection fragment after text change.
			// do this only when the selection is actually affected by the
			// change. Selection is only affected if it intersects the change (1GDY217).
			int netNewLength = newLength - replacedLength;
			int redrawStart = startOffset + newLength;
			internalRedrawRange(redrawStart, sel.y + netNewLength - redrawStart);
		});
	setSelection(Arrays.stream(selection).map(sel -> {
			if (sel.y <= startOffset) {
				return sel;
			} else if (sel.x == startOffset && sel.y == startOffset + replacedLength) {
				return new Point(startOffset + newLength, startOffset + newLength);
			} else if (sel.y > startOffset && sel.x < startOffset + replacedLength) {
				// selection intersects replaced text. set caret behind text change
				return new Point(startOffset + newLength, startOffset + newLength);
			} else {
				// move selection to keep same text selected
				int x = sel.x + newLength - replacedLength;
				int y = sel.x + newLength - replacedLength + (sel.y - sel.x);

				return new Point(x < 0 ? 0 : x, y < 0 ? 0 : y);
			}
		}).flatMapToInt(p -> IntStream.of(p.x, p.y - p.x))
		.toArray(), true, false);
	setCaretLocations();
}

}
