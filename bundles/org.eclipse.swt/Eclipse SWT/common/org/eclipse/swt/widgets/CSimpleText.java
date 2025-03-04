package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class CSimpleText extends NativeBasedCustomScrollable {

	private static final Color DISABLED_COLOR = new Color(160, 160, 160);
	private static final Color TEXT_COLOR = new Color(0, 0, 0);
	private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
	private static final Color BORDER_COLOR = new Color(128, 128, 128);
	private static final Color READONLY_BACKGROUND_COLOR = new Color(227, 227, 227);
	private static final Color SELECTION_BACKGROUND_COLOR = new Color(0, 120, 215);
	private static final Color SELECTION_TEXT_COLOR = new Color(255, 255, 255);

	public static final int LIMIT = 0x7FFFFFFF;
	public static final String DELIMITER = CSimpleTextModel.DELIMITER;

	private int tabs = 8;
	private CSimpleTextModel model;
	private String message;
	private char echoChar;

	int textLimit = LIMIT;

	private boolean mouseDown;
	private boolean doubleClick;
	private CTextCaret caret;
	private int style;
	private Color backgroundColor;
	private Color foregroundColor;

	public CSimpleText(Composite parent, int style) {
		super(parent, checkStyle(style) & ~SWT.BORDER);
		this.style = style;
		model = new CSimpleTextModel();
		message = "";

		setCaret(new CTextCaret(this, SWT.NONE));

		setCursor(display.getSystemCursor(SWT.CURSOR_IBEAM));

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
		if ((style & SWT.SINGLE) != 0) {
			style &= ~(SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		}
		if ((style & SWT.WRAP) != 0) {
			style |= SWT.MULTI;
			style &= ~SWT.H_SCROLL;
		}
		if ((style & SWT.MULTI) != 0) {
			style &= ~SWT.PASSWORD;
		}
		if ((style & (SWT.SINGLE | SWT.MULTI)) != 0) {
			return style;
		}
		if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
			return style | SWT.MULTI;
		}
		return style | SWT.SINGLE;
	}

	@Override
	public int getStyle() {
		checkWidget();
		return style;
	}

	private void addListeners() {
		final Listener listener = event -> {
			switch (event.type) {
			case SWT.Paint -> CSimpleText.this.paintControl(event);
			case SWT.MouseMove -> CSimpleText.this.onMouseMove(event);
			case SWT.MouseDown -> CSimpleText.this.onMouseDown(event);
			case SWT.MouseUp -> CSimpleText.this.onMouseUp(event);
			case SWT.MouseWheel -> CSimpleText.this.onMouseWheel(event);
			case SWT.KeyDown -> CSimpleText.this.keyPressed(event);
			case SWT.FocusIn -> CSimpleText.this.focusGained(event);
			case SWT.FocusOut -> CSimpleText.this.focusLost(event);
			case SWT.Gesture -> CSimpleText.this.onGesture(event);
			case SWT.Dispose -> CSimpleText.this.widgetDisposed(event);
			}
		};
		addListener(SWT.Paint, listener);
		addListener(SWT.MouseMove, listener);
		addListener(SWT.MouseDown, listener);
		addListener(SWT.MouseUp, listener);
		addListener(SWT.MouseWheel, listener);
		addListener(SWT.KeyDown, listener);
		addListener(SWT.FocusIn, listener);
		addListener(SWT.FocusOut, listener);
		addListener(SWT.Gesture, listener);
		addListener(SWT.Dispose, listener);

		ScrollBar horizontalBar = getHorizontalBar();
		if (horizontalBar != null) {
			horizontalBar.addListener(SWT.Selection, CSimpleText.this::scrollBarSelectionChanged);
		}
		ScrollBar verticalBar = getVerticalBar();
		if (verticalBar != null) {
			verticalBar.addListener(SWT.Selection, CSimpleText.this::scrollBarSelectionChanged);
		}

		model.addModelChangedListner(new ITextModelChangedListener() {
			@Override
			public void textModified() {
				CSimpleText.this.textModified();
			}

			@Override
			public void selectionChanged() {
				CSimpleText.this.selectionChanged();
			}
		});
	}

	protected void onGesture(Event e) {
		if (e.yDirection != 0) {
			verticalScroll(e.yDirection);
		}
		if (e.xDirection != 0) {
			if (horizontalBar != null) {
				horizontalScroll(e.xDirection);
			}
		}

		redraw();
	}

	private void horizontalScroll(int diff) {
		int selection = horizontalBar.getSelection();
		horizontalBar.setSelection(selection - diff * 16);
	}

	private void verticalScroll(int diff) {
		if (verticalBar != null) {
			int selection = verticalBar.getSelection();
			verticalBar.setSelection(selection - diff * 16);
		}
	}

	private void onMouseWheel(Event e) {
		verticalScroll(e.count);
		redraw();
	}

	private void textModified() {
		updateScrollBarWithTextSize();
		sendEvent(SWT.Modify);
		redraw();
	}

	private void updateScrollBarWithTextSize() {
		Point size = computeTextSize();
		Rectangle bounds = getBounds();
		if (verticalBar != null) {
			verticalBar.setMaximum(size.y);
			verticalBar.setThumb(bounds.height);
			verticalBar.setPageIncrement(bounds.height);
		}
		if (horizontalBar != null) {
			horizontalBar.setMaximum(size.y);
			horizontalBar.setThumb(bounds.width);
			horizontalBar.setPageIncrement(bounds.height);
		}
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		Point size = computeTextSize();
		if ((style & SWT.BORDER) != 0) {
			size.x += 2 * getBorderWidth();
			size.y += 2 * getBorderWidth();
		}
		return size;
	}

	private Point computeTextSize() {
		GC gc = new GC(this);
		gc.setFont(getFont());
		int width = 0, height = 0;
		if ((style & SWT.SINGLE) != 0) {
			String str = model.getLines()[0];
			Point size = gc.textExtent(str);
			if (str.length() > 0) {
				width = (int) Math.ceil(size.x);
			}
			height = (int) Math.ceil(size.y);
		} else {
			Point size = null;
			for (String line : model.getLines()) {
				size = gc.textExtent(line);
				width = Math.max(width, size.x);
			}
			height = size.y * model.getLineCount();
			if (horizontalBar != null) {
				height += horizontalBar.getSize().y;
			}
			if (verticalBar != null) {
				width += verticalBar.getSize().x;
			}
		}
		gc.dispose();

		return new Point(width, height);
	}

	private void selectionChanged() {
		keepCaretInVisibleArea();
		redraw();
	}

	private void keepCaretInVisibleArea() {
		GC gc = new GC(this);
		Point caretLocation = getLocationByOffset(model.getCaretOffset(), gc);
		Rectangle visibleArea = getVisibleArea();

		if (horizontalBar != null) {
			if (caretLocation.x < visibleArea.x) {
				horizontalBar.setSelection(caretLocation.x);
			} else {
				int maxVisibleAreaWidth = visibleArea.width - 5;
				if (caretLocation.x > visibleArea.x + maxVisibleAreaWidth) {
					horizontalBar.setSelection(caretLocation.x - maxVisibleAreaWidth);
				}
			}
		}

		if (verticalBar != null) {
			if (caretLocation.y < visibleArea.y) {
				verticalBar.setSelection(caretLocation.y);
			} else {
				int maxVisibleAreaHeight = visibleArea.height - getLineHeight(gc);
				if (caretLocation.y > visibleArea.y + maxVisibleAreaHeight) {
					verticalBar.setSelection(caretLocation.y - maxVisibleAreaHeight);
				}
			}
		}

		gc.dispose();
	}

	protected void focusLost(Event e) {
		caret.killFocus();
	}

	protected void focusGained(Event e) {
		caret.setFocus();
	}

	private void onMouseDown(Event e) {
		setFocus();
		TextLocation location = getTextLocation(e.x, e.y);
		model.setSectionStart(location);
		mouseDown = true;
	}

	private void onMouseMove(Event e) {
		if (mouseDown) {
			updateSelectionEnd(e);
		}
	}

	private void onMouseUp(Event e) {
		updateSelectionEnd(e);
		mouseDown = false;
	}

	private void updateSelectionEnd(Event e) {
		TextLocation location = getTextLocation(e.x, e.y);
		model.setSelectionEnd(location);
	}

	private void keyPressed(Event e) {
		final boolean mod1Pressed = (e.stateMask & SWT.MOD1) != 0;
		final boolean shiftPressed = (e.stateMask & SWT.SHIFT) != 0;
		if (mod1Pressed) {
			switch (e.keyCode) {
			case SWT.HOME -> model.moveCaretToTextStart(shiftPressed);
			case SWT.END -> model.moveCaretToTextEnd(shiftPressed);
			}
			if (!shiftPressed) {
				switch (e.keyCode) {
				case 'a' -> model.selectAll();
				case 'c' -> copy();
				case 'x' -> cut();
				case 'v' -> paste();
				}
			}
			return;
		}

		boolean updateSelection = shiftPressed;
		switch (e.keyCode) {
		case SWT.ARROW_LEFT -> model.moveCaretLeft(updateSelection);
		case SWT.ARROW_RIGHT -> model.moveCaretRight(updateSelection);
		case SWT.ARROW_UP -> model.moveCaretUp(updateSelection);
		case SWT.ARROW_DOWN -> model.moveCaretDown(updateSelection);
		case SWT.HOME -> model.moveCaretToLineStart(shiftPressed);
		case SWT.END -> model.moveCaretToLineEnd(shiftPressed);
		case SWT.BS -> model.removeCharacterBeforeCaret();
		case SWT.DEL -> model.removeCharacterAfterCaret();
		default -> {
			if (e.keyCode == 0 || e.character != '\0') {
				model.insert(e.character);
			}
		}
		}
	}

	public void clearSelection() {
		model.clearSelection();
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
				Point textLocationPixel = getLocationByTextLocation(new TextLocation(selectedLine, i), gc);

				if (textLocationPixel.x >= x + 5) {
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

	protected void widgetDisposed(Event e) {
		caret.dispose();
	}

	public Color getBackground() {
		return backgroundColor != null ? backgroundColor : BACKGROUND_COLOR;
	}

	@Override
	public void setBackground(Color color) {
		backgroundColor = color;
		super.setBackground(color);
	}

	@Override
	public Color getForeground() {
		return foregroundColor != null ? foregroundColor : TEXT_COLOR;
	}

	@Override
	public void setForeground(Color color) {
		foregroundColor = color;
		super.setForeground(color);
	}

	private void paintControl(Event e) {
		Rectangle visibleArea = getVisibleArea();
		e.gc.setFont(getFont());
		e.gc.setForeground(getForeground());
		e.gc.setBackground(getBackground());
		if (!isEnabled()) {
			e.gc.setForeground(DISABLED_COLOR);
		}
		if (backgroundColor != null) {
			if (!isEnabled() || ((style & SWT.BORDER) == 1 && !getEditable())) {
				e.gc.setBackground(READONLY_BACKGROUND_COLOR);
			}
			if ((style & SWT.BORDER) == 0 && !getEditable()) {
				e.gc.setBackground(getParent().getBackground());
			}
		}

		drawBackground(e, getClientArea());
		drawText(e, visibleArea);
		drawSelection(e, visibleArea);
		drawCaret(e, visibleArea);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		redraw();
	}

	private void drawSelection(Event e, Rectangle visibleArea) {
		GC gc = e.gc;
		int textLength = model.getText().length();
		int start = Math.min(Math.max(model.getSelectionStart(), 0), textLength);
		int end = Math.min(Math.max(model.getSelectionEnd(), 0), textLength);

		if (model.getSelectionStart() >= 0) {
			TextLocation startLocation = model.getLocation(start);
			TextLocation endLocation = model.getLocation(end);
			String[] textLines = model.getLines();

			Color oldForeground = gc.getForeground();
			Color oldBackground = gc.getBackground();
			gc.setForeground(SELECTION_TEXT_COLOR);
			gc.setBackground(SELECTION_BACKGROUND_COLOR);
			for (int i = startLocation.line; i <= endLocation.line; i++) {
				TextLocation location = new TextLocation(i, 0);
				String text = textLines[i];
				if (i == endLocation.line) {
					text = text.substring(0, endLocation.column);
				}
				if (i == startLocation.line) {
					location.column = startLocation.column;
					text = text.substring(startLocation.column);
				}
				Point locationPixel = getLocationByTextLocation(location, gc);
				gc.drawText(text, locationPixel.x - visibleArea.x, locationPixel.y - visibleArea.y, false);
			}
			gc.setForeground(oldForeground);
			gc.setBackground(oldBackground);
		}
	}

	private void drawCaret(Event e, Rectangle visibleArea) {
		GC gc = e.gc;

		int caretOffset = model.getCaretOffset();

		if (caretOffset >= 0) {
			Point caretLocation = getLocationByOffset(model.getCaretOffset(), e.gc);
			int x = e.x + caretLocation.x - visibleArea.x;
			int y = e.y + caretLocation.y - visibleArea.y;
			getCaret().setBounds(x, y, 1, getLineHeight(gc));
		}

		caret.paint(e);
	}

	private void drawText(Event e, Rectangle visibleArea) {
		String[] lines = model.getLines();
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			drawTextLine(line, i, e.x, e.y, visibleArea, e.gc);
		}
	}


	private void drawTextLine(String text, int lineNumber, int x, int y, Rectangle visibleArea,
			GC gc) {
		Point completeTextExtent = gc.textExtent(text);
		Rectangle clientArea = getClientArea();
		int _x;
		if ((style & SWT.CENTER) != 0) {
			_x = (clientArea.width - completeTextExtent.x) / 2;
		} else if ((style & SWT.RIGHT) != 0) {
			_x = clientArea.width - completeTextExtent.x;
		} else { // ((style & SWT.LEFT) != 0)
			_x = x;
		}
		_x -= visibleArea.x;
		int _y = y + lineNumber * completeTextExtent.y - visibleArea.y;
		if ((style & SWT.BORDER) != 0) {
			_x += getBorderWidth();
			_y += getBorderWidth();
		}
		gc.drawText(text, _x, _y, true);
	}

	private void drawBackground(Event e, Rectangle clientArea) {
		GC gc = e.gc;
		int height = clientArea.height;
		final boolean drawLine = (style & SWT.BORDER) != 0 && getEditable() && isEnabled();
		if (drawLine) {
			height--;
		}
		gc.fillRectangle(clientArea.x, clientArea.y, clientArea.width, height);
		if (drawLine) {
			Color prevBackground = gc.getBackground();
			gc.setBackground(BORDER_COLOR);
			gc.fillRectangle(clientArea.x, clientArea.y + height, clientArea.width, 1);
			gc.setBackground(prevBackground);
		}
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
			if (text == null) {
				return;
			}
		}
		model.setText(text);
	}

	public void setTextChars(char[] cs) {
		model.setTextChars(cs);
	}

	public void append(String string) {
		if (hooks(SWT.Verify) || filters(SWT.Verify)) {
			string = verifyText(string, 0, getCharCount());
			if (string == null) {
				return;
			}
		}
		model.append(string);
	}

	public void setSelection(int start) {
		model.setSelection(start);
	}

	public void setSelection(int start, int end) {
		model.setSelection(start, end);
	}

	public void setSelection(Point selection) {
		if (selection == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		setSelection(selection.x, selection.y);
	}

	public void selectAll() {
		model.selectAll();
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
		if ((style & SWT.SINGLE) != 0) {
			return 0;
		}
		Rectangle visibleArea = getVisibleArea();
		return visibleArea.y / getLineHeight();
	}

	public int getTopPixel() {
		checkWidget();
		if ((style & SWT.SINGLE) != 0) {
			return 0;
		}
		return getVisibleArea().y;
	}

	public void setTopIndex(int index) {
		checkWidget();
		if ((style & SWT.SINGLE) != 0) {
			return;
		}
		TextLocation location = model.getLocation(index);
		int y = location.line * getLineHeight();
		if (verticalBar != null) {
			verticalBar.setSelection(y);
		}
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

	private Point getLocationByOffset(int offset, GC gc) {
		TextLocation textLocation = model.getLocation(offset);
		return getLocationByTextLocation(textLocation, gc);
	}

	private Point getLocationByTextLocation(TextLocation textLocation, GC gc) {
		String completeText = model.getLines()[textLocation.line];
		String beforeSelection = completeText.substring(0, textLocation.column);
		gc.setFont(getFont());
		Point completeTextExtent = gc.textExtent(completeText);
		Point textExtent = gc.textExtent(beforeSelection);
		int x;
		Rectangle clientArea = getClientArea();
		if ((style & SWT.CENTER) != 0) {
			x = (clientArea.width - completeTextExtent.x) / 2;
		} else if ((style & SWT.RIGHT) != 0) {
			x = clientArea.width - completeTextExtent.x;
		} else { // ((style & SWT.LEFT) != 0)
			x = 0;
		}
		x += textExtent.x;
		int y = textLocation.line * textExtent.y;
		if ((style & SWT.BORDER) != 0) {
			x += getBorderWidth();
			y += getBorderWidth();
		}
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

	private void scrollBarSelectionChanged(Event e) {
		redraw();
	}

	public void addSelectionListener(SelectionListener listener) {
		addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
	}

	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
	}

	public void addModifyListener(ModifyListener listener) {
		addTypedListener(listener, SWT.Modify);
	}

	public void removeModifyListener(ModifyListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
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
		if (limit == 0) {
			error(SWT.ERROR_CANNOT_BE_ZERO);
		}
		if (limit < 0) {
			return;
		}
		textLimit = limit;
	}

	@Override
	public int getBorderWidth() {
		if ((style & SWT.BORDER) != 0) {
			return 2;
		}
		return super.getBorderWidth();
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
		if ((style & SWT.PASSWORD) != 0)// || echoCharacter != '\0')
			return;
//		copyToClipboard(model.getSelectedText().toCharArray());
	}

	public void cut() {
		copy();
		model.replaceSelectedTextWith("");
	}

	public void paste() {
		checkWidget();
		String clipboardText = ""; // getClipboardText();
		model.insert(clipboardText);
	}

	public int getSelectionCount() {
		return model.getSelectionCount();
	}

	public void showSelection() {
		checkWidget();
		setSelection(getSelection());
	}

	public void addVerifyListener(VerifyListener listener) {
		addTypedListener(listener, SWT.Verify);
	}

	public void removeVerifyListener(VerifyListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Verify, listener);
	}

	public int getCaretLineNumber() {
		checkWidget();
		if ((style & SWT.SINGLE) != 0) {
			return 0;
		}
		return model.getCaretLocation().line;
	}

	public void insert(String string) {
		checkWidget();
		if (string == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (hooks(SWT.Verify) || filters(SWT.Verify)) {
			Point selection = getSelection();
			string = verifyText(string, selection.x, selection.y);
			if (string == null) {
				return;
			}
		}
		model.insert(string);
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
		if (!event.doit || isDisposed()) {
			return null;
		}
		return event.text;
	}

	public int getCharCount() {
		return model.getCharCount();
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
		if ((style & SWT.SINGLE) != 0) {
			return 1;
		}
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
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
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

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		caret.setVisible(visible);
	}

	@Override
	public boolean setFocus() {
		caret.setFocus();
		return super.setFocus();
	}
}
