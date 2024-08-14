package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class CSimpleText extends Scrollable implements ICustomWidget {

	private int tabs = 8;

	public static final int LIMIT = 0x7FFFFFFF;

	public static String DELIMITER = CSimpleTextModel.DELIMITER;

	private CSimpleTextModel model;
	private String message;
	private char echoChar;

	int textLimit = LIMIT;

	private boolean mouseDown;
	private boolean doubleClick;
	private CTextCaret caret;

	public CSimpleText(Composite parent, int style) {
		super(parent, checkStyle(style));
		model = new CSimpleTextModel();
		message = "";

		setCaret(new CTextCaret(this, SWT.NONE));

		setCursor(display.getSystemCursor(SWT.CURSOR_IBEAM));
		setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		setForeground(display.getSystemColor(SWT.COLOR_LIST_FOREGROUND));

		addListeners();

	}

	static int checkStyle(int style) {
		if ((style & SWT.SEARCH) != 0) {
			style |= SWT.SINGLE | SWT.BORDER;
			style &= ~SWT.PASSWORD;
			/*
			 * NOTE: ICON_CANCEL has the same value as H_SCROLL and ICON_SEARCH has the same
			 * value as V_SCROLL so they are cleared because SWT.SINGLE is set.
			 */
		}
		if ((style & SWT.SINGLE) != 0 && (style & SWT.MULTI) != 0) {
			style &= ~SWT.MULTI;
		}
		style = checkBits(style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
		if ((style & SWT.SINGLE) != 0)
			style &= ~(SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		if ((style & SWT.WRAP) != 0) {
			style |= SWT.MULTI;
			style &= ~SWT.H_SCROLL;
		}
		if ((style & SWT.MULTI) != 0)
			style &= ~SWT.PASSWORD;
		if ((style & (SWT.SINGLE | SWT.MULTI)) != 0)
			return style;
		if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0)
			return style | SWT.MULTI;
		return style | SWT.SINGLE;
	}

	private void addListeners() {
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

		addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				CSimpleText.this.focusGained(e);
			}

			@Override
			public void focusLost(FocusEvent e) {
				CSimpleText.this.focusLost(e);
			}
		});

		model.addModelChangedListner(() -> CSimpleText.this.sendEvent(SWT.Modify));
	}

	protected void focusLost(FocusEvent e) {
		caret.setFocus();

	}

	protected void focusGained(FocusEvent e) {
		caret.killFocus();
	}

	private void onMouseDown(MouseEvent e) {
		TextLocation location = getTextLocation(e.x, e.y);
		model.setSectionStart(location);
		redraw();
		mouseDown = true;
	}

	private void onMouseMove(MouseEvent e) {
		if (mouseDown) {
			updateSelectionEnd(e);
		}
	}

	private void onMouseUp(MouseEvent e) {
		updateSelectionEnd(e);
		mouseDown = false;
	}

	private void updateSelectionEnd(MouseEvent e) {
		TextLocation location = getTextLocation(e.x, e.y);
		model.setSelectionEnd(location);
		redraw();
	}

	private void keyPressed(KeyEvent e) {
		boolean updateSelection = (e.stateMask & SWT.SHIFT) != 0;

		switch (e.keyCode) {
		case SWT.ARROW_LEFT:
			model.moveCaretLeft(updateSelection);
			break;
		case SWT.ARROW_RIGHT:
			model.moveCaretRight(updateSelection);
			break;
		case SWT.ARROW_UP:
			model.moveCaretUp(updateSelection);
			break;
		case SWT.ARROW_DOWN:
			model.moveCaretDown(updateSelection);
			break;
		case SWT.BS:
			model.removeCharacterBeforeCaret();
			break;
		case SWT.DEL:
			model.removeCharacterBeforeCaret();
			break;
		default:
			model.insert(e.character);
			break;
		}
		redraw();
	}

	public void clearSelection() {
		model.clearSelection();
		redraw();
	}

	private TextLocation getTextLocation(int selectedX, int selectedY) {
		Rectangle visibleArea = getVisibleArea();
		int x = Math.max(selectedX + visibleArea.x, 0);
		int y = Math.max(selectedY + visibleArea.y, 0);

		GC gc = new GC(this);
		String[] textLines = model.getLines();
		int clickedLine = Math.min(Math.round(y / getLineHeight(gc)), textLines.length - 1);
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
		int textLength = model.getText().length();
		int start = Math.min(Math.max(model.getSelectionStart(), 0), textLength);
		int end = Math.min(Math.max(model.getSelectionEnd(), 0), textLength);

		if (model.getSelectionStart() >= 0) {
			Point startLocationPixel = getLocationByOffset(start, gc);
			TextLocation startLocation = model.getLocation(start);
			TextLocation endLocation = model.getLocation(end);
			String[] textLines = model.getLines();

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

		int caretOffset = model.getCaretOffset();
		if (caretOffset >= 0) {
			Point caretLocation = getLocationByOffset(caretOffset, gc);
			int x = e.x + caretLocation.x - visibleArea.x;
			int y = e.y + caretLocation.y - visibleArea.y;
			getCaret().setBounds(x, y, 1, getLineHeight(gc));
		}
	}

	private void drawText(PaintEvent e, Rectangle visibleArea) {
		GC gc = e.gc;
		gc.drawText(model.getText(), e.x - visibleArea.x, e.y - visibleArea.y, true);
	}

	private void drawBackground(PaintEvent e) {
		System.out.println(getVisibleArea());
		System.out.println(getBounds());
		System.out.println(getSize());
		System.out.println(model.getLineCount() * getLineHeight());
		ScrollBar verticalBar = getVerticalBar();
		System.out.println(verticalBar.getMaximum());
		System.out.println(verticalBar.getThumb());
		GC gc = e.gc;
		gc.setBackground(getBackground());
		gc.fillRectangle(e.x, e.y, e.width - 1, e.height - 1);
	}

	@Override
	public Point getSize() {
		// TODO Auto-generated method stub
		return super.getSize();
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
		return model.getText();
	}

	public String getText(int start, int end) {
		return model.getText(start, end + 1);
	}

	public void setText(String text) {
		// TODO move verify listener to TextModel
		if (hooks(SWT.Verify) || filters(SWT.Verify)) {
			text = verifyText(text, 0, getCharCount());
			if (text == null)
				return;
		}
		model.setText(text);
		redraw();
	}

	public void append(String string) {
		if (hooks(SWT.Verify) || filters(SWT.Verify)) {
			string = verifyText(string, 0, getCharCount());
			if (string == null)
				return;
		}
		model.append(string);
		redraw();
	}

	public void setSelection(int start) {
		model.setSelection(start);
		redraw();
	}

	public void setSelection(int start, int end) {
		model.setSelection(start, end);
		redraw();
	}

	public void setSelection(Point selection) {
		if (selection == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		setSelection(selection.x, selection.y);
		redraw();
	}

	public void selectAll() {
		model.selectAll();
		redraw();
	}

	public Point getSelection() {
		if (model.isTextSelected()) {
			return new Point(model.getSelectionStart(), model.getSelectionEnd());
		} else {
			int caretOffset = model.getCaretOffset();
			return new Point(caretOffset, caretOffset);
		}
	}

	public String getSelectionText() {
		return model.getSelectedText();
	}

	public int getTopIndex() {
		if ((style & SWT.SINGLE) != 0)
			return 0;
		Rectangle visibleArea = getVisibleArea();
		return visibleArea.y / getLineHeight();
	}

	public int getTopPixel() {
		checkWidget();
		if ((style & SWT.SINGLE) != 0)
			return 0;
		return getVisibleArea().y;
	}

	public void setTopIndex(int index) {
		checkWidget();
		if ((style & SWT.SINGLE) != 0)
			return;
		TextLocation location = model.getLocation(index);
		Rectangle visibleArea = getVisibleArea();

		int y = location.line * getLineHeight();

		// TODO scroll(0, 0, visibleArea.x, y, visibleArea.width, visibleArea.height,
		// true);
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
		return getLocationByOffset(model.getCaretOffset(), new GC(this));
	}

	public Point getLocationByOffset(int offset, GC gc) {
		TextLocation selectionLocation = model.getLocation(offset);

		String beforeSelection = model.getLines()[selectionLocation.line].substring(0, selectionLocation.column);
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
		return model.getCaretOffset();
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
			String str = model.getLines()[0];
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
		String str = model.getLines()[0];
		return gc.textExtent(str).y;
	}

	/**
	 * Copies the selected text.
	 * <p>
	 * The current selection is copied to the clipboard.
	 * </p>
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void copy() {
		checkWidget();
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

	public int getSelectionCount() {
		return model.getSelectionCount();
	}

	public void showSelection() {
		checkWidget();
		setSelection(getSelection());
		redraw();
	}

	public void addVerifyListener(VerifyListener listener) {
		addTypedListener(listener, SWT.Verify);
	}

	public void removeVerifyListener(VerifyListener listener) {
		checkWidget();
		if (listener == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null)
			return;
		eventTable.unhook(SWT.Verify, listener);
	}

	public int getCaretLineNumber() {
		checkWidget();
		if ((style & SWT.SINGLE) != 0)
			return 0;
		return model.getCaretLocation().line;
	}

	public void insert(String string) {
		checkWidget();
		if (string == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		if (hooks(SWT.Verify) || filters(SWT.Verify)) {
			Point selection = getSelection();
			string = verifyText(string, selection.x, selection.y);
			if (string == null)
				return;
		}
		if (model.isTextSelected()) {
			replaceSelectedText(string);
		} else {
			insertTextAtCaret(string);
		}

		if (string.length() != 0)
			sendEvent(SWT.Modify);
		redraw();
	}

	private void insertTextAtCaret(String string) {
		model.insert(string);
	}

	private void replaceSelectedText(String string) {
		model.replaceSelectedTextWith(string);
	}

	String verifyText(String string, int start, int end) {
		Event event = new Event();
		event.text = string;
		event.start = start;
		event.end = end;
		/*
		 * It is possible (but unlikely), that application code could have disposed the
		 * widget in the verify event. If this happens, answer null to cancel the
		 * operation.
		 */
		sendEvent(SWT.Verify, event);
		if (!event.doit || isDisposed())
			return null;
		return event.text;
	}

	public int getCharCount() {
		return model.getText().length();
	}

	public boolean getDoubleClickEnabled() {
		return doubleClick;
	}

	public void setDoubleClickEnabled(boolean doubleClick) {
		this.doubleClick = doubleClick;
	}

	public char getEchoChar() {
		return echoChar;
	}

	public void setEchoChar(char echoChar) {
		this.echoChar = echoChar;
	}

	public int getLineCount() {
		checkWidget();
		if ((style & SWT.SINGLE) != 0)
			return 1;
		return model.getLineCount();
	}

	public String getLineDelimiter() {
		checkWidget();
		return CSimpleTextModel.DELIMITER;
	}

	public void addSegmentListener(SegmentListener listener) {
		addTypedListener(listener, SWT.Segments);
	}

	public void removeSegmentListener(SegmentListener listener) {
		checkWidget();
		if (listener == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		eventTable.unhook(SWT.Segments, listener);
	}

	public int getTabs() {
		return tabs;
	}

	public void setTabs(int tabs) {
		this.tabs = tabs;
	}

	public CTextCaret getCaret() {
		return caret;
	}

	public void setCaret(CTextCaret caret) {
		this.caret = caret;
	}


}
