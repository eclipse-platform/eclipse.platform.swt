package org.eclipse.swt.widgets;



import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class CSimpleText extends Canvas {


	public static final int LIMIT = 0x7FFFFFFF;

	public static final String DELIMITER = "\n";

	private TextContent content;
	private String message;

	private int selectionStart, selectionEnd;
	private int caretOffset;

	int textLimit = LIMIT;

	private boolean mouseDown;


	public CSimpleText(Composite parent, int style) {
		super(parent, style);
		content = new TextContent();
		message = "";

		caret = new Caret(this, SWT.NONE);

		setCursor(display.getSystemCursor(SWT.CURSOR_IBEAM));
		setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		setForeground(display.getSystemColor(SWT.COLOR_LIST_FOREGROUND));

		addDisposeListener(e -> CSimpleText.this.widgetDisposed(e));
		addPaintListener(e -> CSimpleText.this.paintControl(e));
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				CSimpleText.this.keyPressed(e);
			}
		});

		ScrollBar horizontalBar = getHorizontalBar();
		if (horizontalBar != null) {
			horizontalBar.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					super.widgetSelected(e);
					CSimpleText.this.scrollBarSelectionChanged(e);
				}
			});
		}
		ScrollBar verticalBar = getVerticalBar();
		if (verticalBar != null) {
			verticalBar.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					super.widgetSelected(e);
					CSimpleText.this.scrollBarSelectionChanged(e);
				}
			});
		}
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent e) {
				super.mouseDown(e);
				CSimpleText.this.onMouseDown(e);
			}

			@Override
			public void mouseUp(MouseEvent e) {
				super.mouseUp(e);
				CSimpleText.this.onMouseUp(e);
			}

		});

		addMouseMoveListener(e -> {
			CSimpleText.this.onMouseMove(e);
		});


	}

	private void onMouseMove(MouseEvent e) {
		if (mouseDown) {
			TextLocation location = getTextLocation(e.x, e.y);
			selectionEnd = content.getOffset(location);
			redraw();
		}
	}

	private void onMouseUp(MouseEvent e) {
		TextLocation location = getTextLocation(e.x, e.y);
		selectionEnd = content.getOffset(location);
		if (isTextSelected()) {
			caretOffset = selectionEnd;
		} else {
			clearSelection();
		}
		redraw();
		mouseDown = false;
	}

	private void onMouseDown(MouseEvent e) {
		TextLocation location = getTextLocation(e.x, e.y);
		caretOffset = selectionStart = selectionEnd = content.getOffset(location);
		mouseDown = true;
		redraw();
	}

	private TextLocation getTextLocation(int selectedX, int selectedY) {
		Rectangle visibleArea = getVisibleArea();
		int x = selectedX + visibleArea.x;
		int y = selectedY + visibleArea.y;

		GC gc = new GC(this);
		String[] textLines = content.getLines();
		int clickedLine = Math.round(y / getLineHeight(gc));
		int selectedLine = Math.min(clickedLine, textLines.length - 1);
		String text = "";
		if (clickedLine > selectedLine) {
			text = textLines[selectedLine];
		} else {
			text = textLines[selectedLine];
			for (int i = 0; i < text.length(); i++) {
				int textExtend = gc.textExtent(text.substring(0, i)).x;
				if (textExtend >= x + 5) {
					if (i > 0) {
						text = text.substring(0, i - 1);
					} else {
						text = "";
					}
					break;
				}
			}
		}
		gc.dispose();

		TextLocation location = new TextLocation(clickedLine, text.length());
		return location;
	}

	private void keyPressed(KeyEvent e) {
		TextLocation caretLocation;

		boolean updateSelection = (e.stateMask & SWT.SHIFT) != 0;

		switch (e.keyCode) {
		case SWT.ARROW_LEFT:
			moveCaretTo(caretOffset - 1, updateSelection);
			break;
		case SWT.ARROW_RIGHT:
			moveCaretTo(caretOffset + 1, updateSelection);
			break;
		case SWT.ARROW_UP:
			caretLocation = content.getLocation(caretOffset);
			if (caretLocation.line <= 0)
				break;
			caretLocation.line--;
			moveCaretTo(content.getOffset(caretLocation), updateSelection);
			break;
		case SWT.ARROW_DOWN:
			caretLocation = content.getLocation(caretOffset);
			if (caretLocation.line > content.getLines().length - 1)
				break;
			caretLocation.line++;
			moveCaretTo(content.getOffset(caretLocation), updateSelection);
			break;
		case SWT.BS:
			content.removeCharacter(caretOffset - 1);
			moveCaretTo(caretOffset - 1, false);
			clearSelection();
			break;
		case SWT.DEL:
			content.removeCharacter(caretOffset);
			clearSelection();
			break;
		default:
			if (isTextSelected()) {
				content.replaceWith(e.character, getSelectionStart(), getSelectionEnd());
				moveCaretTo(getSelectionStart() + 1, false);
			} else {
				content.append(e.character);
				moveCaretTo(caretOffset + 1, false);
			}
			clearSelection();
		}
		redraw();
	}

	private boolean isTextSelected() {
		if (selectionStart >= 0 && selectionEnd > 0 && selectionStart != selectionEnd) {
			return true;
		}
		return false;
	}

	public void clearSelection() {
		selectionStart = selectionEnd = -1;
	}

	private void moveCaretTo(int newOffset, boolean updateSelection) {
		if (newOffset < 0 || newOffset > content.getText().length())
			return;

		if (updateSelection) {
			if (caretOffset == getSelectionEnd()) {
				selectionEnd = newOffset;
			} else if (selectionStart < 0 && selectionEnd < 0) {
				selectionStart = caretOffset;
				selectionEnd = newOffset;
			}
		} else {
			clearSelection();
		}
		caretOffset = newOffset;
	}

	protected void widgetDisposed(DisposeEvent e) {
		// TODO Auto-generated method stub
	}

	private void paintControl(PaintEvent e) {
		drawBackground(e);
		Rectangle visibleArea = getVisibleArea();
		drawText(e, visibleArea);
		drawSelection(e, visibleArea);
		drawCaret(e, visibleArea);
	}

	private void drawSelection(PaintEvent e, Rectangle visibleArea) {
		GC gc = e.gc;
		int textLength = content.getText().length();
		int start = Math.min(Math.max(getSelectionStart(), 0), textLength);
		int end = Math.min(Math.max(getSelectionEnd(), 0), textLength);

		if (getSelectionStart() >= 0) {
			Point startLocationPixel = getLocationByOffset(start, gc);
			TextLocation startLocation = content.getLocation(start);
			TextLocation endLocation = content.getLocation(end);
			String[] textLines = content.getLines();

			Color oldBackground = gc.getBackground();
			gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));

			int y = startLocationPixel.y;
			int lineHeight = getLineHeight(gc);
			for (int i = startLocation.line; i <= endLocation.line; i++) {
				int x = 0;
				String text = textLines[i];
				if (i == endLocation.line) {
					text = text.substring(0, endLocation.column);
				}
				if (i == startLocation.line) {
					x = startLocationPixel.x;
					text = text.substring(startLocation.column);
				}
				gc.drawText(text, x - visibleArea.x, y - visibleArea.y, false);
				y += lineHeight;
			}

			gc.setBackground(oldBackground);

		}
	}

	private void drawCaret(PaintEvent e, Rectangle visibleArea) {
		GC gc = e.gc;

		if (caretOffset >= 0) {
			Point caretLocation = getLocationByOffset(caretOffset, gc);
			int x = e.x + caretLocation.x - visibleArea.x;
			int y = e.y + caretLocation.y - visibleArea.y;
			caret.setBounds(x, y, 1, getLineHeight(gc));
		}
	}

	private void drawText(PaintEvent e, Rectangle visibleArea) {
		GC gc = e.gc;
		gc.drawText(content.getText(), e.x - visibleArea.x, e.y - visibleArea.y, true);
	}

	private void drawBackground(PaintEvent e) {
		GC gc = e.gc;
		gc.setBackground(getBackgroundColor());
		gc.fillRectangle(e.x, e.y, e.width - 1, e.height - 1);
	}

	private Color getBackgroundColor() {
		return super.getBackground();
	}

	private Rectangle getVisibleArea() {
		Rectangle clientArea = getClientArea();

		ScrollBar horizontalBar = getHorizontalBar();
		ScrollBar verticalBar = getVerticalBar();

		int hOffset = (horizontalBar != null) ? horizontalBar.getSelection() : 0;
		int vOffset = (verticalBar != null) ? verticalBar.getSelection() : 0;

		clientArea.x += hOffset;
		clientArea.y += vOffset;

		return clientArea;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		redraw();
	}

	public String getText() {
		return content.getText();
	}

	public void setText(String text) {
		content.setText(text);
		redraw();
	}

	public void append(String string) {
		content.append(string);
		redraw();
	}

	public void setSelection(int start) {
		selectionStart = start;
		redraw();
	}

	public void setSelection(int start, int end) {
		selectionStart = start;
		selectionEnd = end;
		caretOffset = end;
		redraw();
	}

	public void setSelection(Point selection) {
		if (selection == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		setSelection(selection.x, selection.y);
	}

	public void selectAll() {
		setSelection(0, content.getText().length());
	}

	public Point getSelection() {
		return new Point(0, 0);
	}

	public String getSelectionText() {
		return content.getText().substring(getSelectionStart(), getSelectionEnd());
	}

	public int getTopIndex() {
		if ((style & SWT.SINGLE) != 0)
			return 0;
		Rectangle visibleArea = getVisibleArea();
		return visibleArea.x / getLineHeight();
	}

	public void setTopIndex(int index) {
		checkWidget();
		if ((style & SWT.SINGLE) != 0)
			return;
		Rectangle visibleArea = getVisibleArea();

		int y = index * getLineHeight();
		scroll(0, 0, visibleArea.x, y, visibleArea.width, visibleArea.height, true);
	}

	/**
	 * Returns a point describing the location of the caret relative to the
	 * receiver.
	 *
	 * @return a point, the location of the caret
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Point getCaretLocation() {
		return getLocationByOffset(caretOffset, new GC(this));
	}

	public Point getLocationByOffset(int offset, GC gc) {
		TextLocation selectionLocation = content.getLocation(offset);

		String beforeSelection = content.getLines()[selectionLocation.line].substring(0, selectionLocation.column);
		int x = gc.textExtent(beforeSelection).x;
		int y = selectionLocation.line * getLineHeight(gc);
		return new Point(x, y);
	}

	/**
	 * Returns the character position of the caret.
	 * <p>
	 * Indexing is zero based.
	 * </p>
	 *
	 * @return the position of the caret
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getCaretPosition() {
		return caretOffset;
	}

	class TextLocation {
		int line;
		int column;

		public TextLocation(int line, int column) {
			this.line = line;
			this.column = column;
		}

		@Override
		public String toString() {
			return "(" + line + ", " + column + ")";
		}
	}

	class TextContent {
		private String text = "##";

		public String getText() {
			return text;
		}

		public void replaceWith(char character, int from, int to) {
			int start = Math.min(from, to);
			int end = Math.min(Math.max(from, to), text.length());

			StringBuilder sb = new StringBuilder(text.substring(0, start));
			sb.append(character);
			sb.append(text.substring(end));

			text = sb.toString();
		}

		public void removeCharacter(int offset) {
			if (offset > text.length()) return;
			StringBuilder sb = new StringBuilder(text.substring(0, offset));
			if (offset + 1 < text.length()) {
				sb.append(text.substring(offset + 1, text.length()));
			}
			text = sb.toString();

		}

		public void append(char character) {
			StringBuilder sb = new StringBuilder(text);
			sb.insert(caretOffset, character);
			text = sb.toString();
		}

		public int getOffset(TextLocation location) {
			String[] lines = getLines();
			int offset = 0;
			for (int i = 0; i < location.line; i++) {
				offset += lines[i].length() + 1; // add 1 for line break
			}
			offset += Math.min(location.column, lines[location.line].length());
			return offset;
		}

		public void setText(String text) {
			if (text == null)
				SWT.error(SWT.ERROR_NULL_ARGUMENT);
			this.text = "##" + text;
		}

		public void append(String string) {
			if (string == null)
				SWT.error(SWT.ERROR_NULL_ARGUMENT);
			text = text + string;
		}

		public String[] getLines() {
			return getLinesOf(text);
		}

		public int getLineCount() {
			return getLines().length;
		}

		public TextLocation getLocation(int offset) {
			if (offset < 0 || offset > text.length())
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);

			int line = 0;
			int column = 0;

			for (int i = 0; i < offset; i++) {
				char c = text.charAt(i);
				if (c == '\n') {
					line++;
					column = 0; // Reset column number after a new line
				} else if (c == '\r') {
					if (i + 1 < text.length() && text.charAt(i + 1) == '\n') {
						i++; // Skip the '\n' in '\r\n' sequence
					}
					line++;
					column = 0; // Reset column number after a new line
				} else {
					column++;
				}
			}
			return new TextLocation(line, column);
		}

		private String[] getLinesOf(String string) {
			return string.split(DELIMITER);
		}
	}

	private void scrollBarSelectionChanged(SelectionEvent e) {
		redraw();
	}

	public void addSelectionListener(SelectionListener listener) {
		addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
	}

	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null)
			return;
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
	}

	public void addModifyListener(ModifyListener listener) {
		addTypedListener(listener, SWT.Modify);
	}

	public void removeModifyListener(ModifyListener listener) {
		checkWidget();
		if (listener == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null)
			return;
		eventTable.unhook(SWT.Modify, listener);
	}

	public void setEditable(boolean editable) {
		checkWidget();
		if (editable) {
			style &= ~SWT.READ_ONLY;
		} else {
			style |= SWT.READ_ONLY;
		}
		redraw();
	}

	public boolean getEditable() {
		checkWidget();
		return (style & SWT.READ_ONLY) == 0;
	}

	public int getTextLimit() {
		checkWidget();
		return textLimit;
	}

	public void setTextLimit(int limit) {
		checkWidget();
		if (limit == 0)
			error(SWT.ERROR_CANNOT_BE_ZERO);
		if (limit < 0)
			return;
		textLimit = limit;
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		int width = 0, height = 0;
		if ((style & SWT.SINGLE) != 0) {
			GC gc = new GC(this);
			String str = content.getLines()[0];
			Point size = gc.textExtent(str);
			gc.dispose();
			if (str.length() > 0) {
				width = (int) Math.ceil(size.x);
			}
			height = (int) Math.ceil(size.y);

			if ((style & SWT.BORDER) != 0) {
				width += 2;
			}

			return new Point(width, height);
		} else {
			return super.computeSize(wHint, hHint, changed);
		}
	}

	public int getLineHeight() {
		checkWidget();
		GC gc = new GC(this);
		int height = getLineHeight(gc);
		gc.dispose();
		return height;
	}

	private int getLineHeight(GC gc) {
		checkWidget();
		String str = content.getLines()[0];
		return gc.textExtent(str).y;
	}


	/**
	 * Copies the selected text.
	 * <p>
	 * The current selection is copied to the clipboard.
	 * </p>
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 * </ul>
	 */
	public void copy () {
		checkWidget ();
		// if ((style & SWT.PASSWORD) != 0 || echoCharacter != '\0') return;
		// if ((style & SWT.SINGLE) != 0) {
		// Point selection = getSelection ();
		// if (selection.x == selection.y) return;
		// copyToClipboard(content.getText().substring(getSelectionStart(),
		// getSelectionEnd()).toCharArray());
		// }
		// else {
		// NSText text = (NSText) view;
		// if (text.selectedRange ().length == 0) return;
		// text.copy (null);
		// }
	}

	public void cut() {
		// TODO
	}

	public void paste() {
		// TODO
	}

	public int getSelectionStart() {
		return Math.min(selectionStart, selectionEnd);
	}

	public int getSelectionEnd() {
		return Math.max(selectionStart, selectionEnd);
	}


}
