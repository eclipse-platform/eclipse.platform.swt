/*******************************************************************************
 * Copyright (c) 2024 SAP SE and others.

 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SegmentListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are selectable user interface objects that allow the
 * user to enter and modify text. Text controls can be either single or
 * multi-line. When a text control is created with a border, the operating
 * system includes a platform specific inset around the contents of the control.
 * When created without a border, an effort is made to remove the inset such
 * that the preferred size of the control is the same size as the contents.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CENTER, ICON_CANCEL, ICON_SEARCH, LEFT, MULTI, PASSWORD, SEARCH, SINGLE,
 * RIGHT, READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Verify, OrientationChange</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles MULTI and SINGLE may be specified, and only one
 * of the styles LEFT, CENTER, and RIGHT may be specified.
 * </p>
 * <p>
 * Note: The styles ICON_CANCEL and ICON_SEARCH are hints used in combination
 * with SEARCH. When the platform supports the hint, the text control shows
 * these icons. When an icon is selected, a default selection event is sent with
 * the detail field set to one of ICON_CANCEL or ICON_SEARCH. Normally,
 * application code does not need to check the detail. In the case of
 * ICON_CANCEL, the text is cleared before the default selection event is sent
 * causing the application to search for an empty string.
 * </p>
 * <p>
 * Note: Some text actions such as Undo are not natively supported on all
 * platforms.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#text">Text snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Text extends NativeBasedCustomScrollable {

	public static final int LIMIT = 0x7FFFFFFF;
	public static final String DELIMITER = TextModel.DELIMITER;

	private int tabs = 8;
	private final TextModel model;
	private String message;
	private char echoChar;

	int textLimit = LIMIT;

	private boolean doubleClick = true;
	private Point doubleClickStartRange;
	private TextCaret caret;
	private int style;

	private final TextRenderer renderer;

	/**
	 * Constructs a new instance of this class given its parent and a style value
	 * describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent a composite control which will be the parent of the new
	 *               instance (cannot be null)
	 * @param style  the style of control to construct
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     <li>ERROR_INVALID_SUBCLASS - if this
	 *                                     class is not an allowed subclass</li>
	 *                                     </ul>
	 *
	 * @see SWT#SINGLE
	 * @see SWT#MULTI
	 * @see SWT#READ_ONLY
	 * @see SWT#WRAP
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#CENTER
	 * @see SWT#PASSWORD
	 * @see SWT#SEARCH
	 * @see SWT#ICON_SEARCH
	 * @see SWT#ICON_CANCEL
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Text(Composite parent, int style) {
		super(parent, checkStyle(style) & ~SWT.BORDER);
		this.style = style;
		model = new TextModel();
		message = "";

		setCaret(new TextCaret(this, SWT.NONE));

		setCursor(display.getSystemCursor(SWT.CURSOR_IBEAM));

		addListeners();

		renderer = new DefaultTextRenderer(this, model);
	}

	@Override
	protected ControlRenderer getRenderer() {
		return renderer;
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
			case SWT.Paint -> Text.this.paintControl(event);
			case SWT.MouseMove -> Text.this.onMouseMove(event);
			case SWT.MouseDown -> Text.this.onMouseDown(event);
			case SWT.MouseUp -> Text.this.onMouseUp(event);
			case SWT.MouseWheel -> Text.this.onMouseWheel(event);
			case SWT.KeyDown -> Text.this.keyPressed(event);
			case SWT.Traverse -> Text.this.onTraverse(event);
			case SWT.FocusIn -> Text.this.focusGained(event);
			case SWT.FocusOut -> Text.this.focusLost(event);
			case SWT.Gesture -> Text.this.onGesture(event);
			case SWT.Dispose -> Text.this.widgetDisposed(event);
			}
		};
		addListener(SWT.Paint, listener);
		addListener(SWT.MouseMove, listener);
		addListener(SWT.MouseDown, listener);
		addListener(SWT.MouseUp, listener);
		addListener(SWT.MouseWheel, listener);
		addListener(SWT.KeyDown, listener);
		addListener(SWT.Traverse, listener);
		addListener(SWT.FocusIn, listener);
		addListener(SWT.FocusOut, listener);
		addListener(SWT.Gesture, listener);
		addListener(SWT.Dispose, listener);

		ScrollBar horizontalBar = getHorizontalBar();
		if (horizontalBar != null) {
			horizontalBar.addListener(SWT.Selection, Text.this::scrollBarSelectionChanged);
		}
		ScrollBar verticalBar = getVerticalBar();
		if (verticalBar != null) {
			verticalBar.addListener(SWT.Selection, Text.this::scrollBarSelectionChanged);
		}

		model.addModelChangedListner(new ITextModelChangedListener() {
			@Override
			public void textModified() {
				Text.this.textModified();
			}

			@Override
			public void selectionChanged() {
				Text.this.selectionChanged();
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
		if (wHint != SWT.DEFAULT) {
			size.x = wHint;
		}
		if (hHint != SWT.DEFAULT) {
			size.y = hHint;
		}
		return size;
	}

	private Point computeTextSize() {
		return Drawing.measure(this, gc -> {
			gc.setFont(getFont());
			int width = 0, height = 0;
			if ((style & SWT.SINGLE) != 0) {
				String str = model.getLines()[0];
				Point size = gc.textExtent(str);
				if (str.length() > 0) {
					width = (int)Math.ceil(size.x);
				}
				height = (int)Math.ceil(size.y);
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
			return new Point(width, height);
		});
	}

	private void selectionChanged() {
		keepCaretInVisibleArea();
		redraw();
	}

	private void keepCaretInVisibleArea() {
		Drawing.measure(this, gc -> {
			Point caretLocation = getLocationByOffset(model.getCaretOffset(), gc);
			Rectangle visibleArea = renderer.getVisibleArea();

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
					int maxVisibleAreaHeight = visibleArea.height - renderer.getLineHeight(gc);
					if (caretLocation.y > visibleArea.y + maxVisibleAreaHeight) {
						verticalBar.setSelection(caretLocation.y - maxVisibleAreaHeight);
					}
				}
			}
			return null;
		});
	}

	protected void focusLost(Event e) {
		caret.killFocus();
		redraw();
	}

	protected void focusGained(Event e) {
		caret.setFocus();
		redraw();
	}

	private void onMouseDown(Event e) {
		setFocus();
		if (e.button != 1) {
			return;
		}
		TextLocation location = getTextLocation(e.x, e.y);
		if (e.count == 2 && doubleClick) {
			doubleClickStartRange = model.getWordRange(location);
			model.setSelection(doubleClickStartRange.x, doubleClickStartRange.y);
		}
		else {
			model.setSectionStart(location);
		}
		setCapture(true);
	}

	private void onMouseMove(Event e) {
		if ((e.stateMask & SWT.BUTTON1) != 0) {
			updateSelectionEnd(e);
		}
	}

	private void onMouseUp(Event e) {
		if (e.button != 1) {
			return;
		}

		setCapture(false);
		updateSelectionEnd(e);
		doubleClickStartRange = null;
	}

	private void updateSelectionEnd(Event e) {
		TextLocation location = getTextLocation(e.x, e.y);
		if (doubleClickStartRange != null) {
			final Point newRange = model.extendWordSelection(location, doubleClickStartRange.x, doubleClickStartRange.y);
			model.setSelection(newRange.x, newRange.y);
		}
		else {
			model.setSelectionEnd(location);
		}
	}

	private void keyPressed(Event e) {
		if (!getEditable()) return;
		final boolean mod1Pressed = (e.stateMask & SWT.MOD1) != 0;
		final boolean shiftPressed = (e.stateMask & SWT.SHIFT) != 0;
		if (mod1Pressed) {
			switch (e.keyCode) {
			case SWT.HOME -> model.moveCaretToTextStart(shiftPressed);
			case SWT.END -> model.moveCaretToTextEnd(shiftPressed);
			case SWT.ARROW_LEFT -> model.moveCaretToPreviousWord(shiftPressed);
	     	case SWT.ARROW_RIGHT -> model.moveCaretToNextWord(shiftPressed);
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
		case SWT.CR, SWT.LF -> model.handleNewLine(style, this);
		default -> {
			if (e.keyCode == 0 || e.character != '\0') {
				model.insert(e.character);
			}
		}
		}
	}

	private void onTraverse(Event e) {
		switch (e.detail) {
		case SWT.TRAVERSE_TAB_PREVIOUS -> e.doit = true;
		case SWT.TRAVERSE_TAB_NEXT -> {
			e.doit = (style & SWT.MULTI) == 0 || (e.stateMask & SWT.MODIFIER_MASK & ~SWT.SHIFT) == SWT.MOD1;
		}
		default -> e.doit = false;
		}
	}

	public void clearSelection() {
		model.clearSelection();
	}

	private TextLocation getTextLocation(int selectedX, int selectedY) {
		Rectangle visibleArea = renderer.getVisibleArea();
		int x = Math.max(selectedX + visibleArea.x, 0);
		int y = Math.max(selectedY + visibleArea.y, 0);

		return Drawing.measure(this, gc -> {
			String[] textLines = model.getLines();
			int clickedLine = Math.min(y / renderer.getLineHeight(gc), textLines.length - 1);
			int selectedLine = Math.min(clickedLine, textLines.length - 1);
			String text = textLines[selectedLine];
			if (clickedLine == selectedLine && text.length() > 0) {
				int before = 0;
				int after = text.length();
				while (true) {
					int middle = (before + after) / 2;
					final int middleX = renderer.getLocationByTextLocation(new TextLocation(selectedLine, middle), gc).x;
					if (middleX > x) {
						if (after > before + 1) {
							after = middle;
							continue;
						}
						return new TextLocation(clickedLine, 0);
					}

					if (after - middle <= 1) {
						final int afterX = renderer.getLocationByTextLocation(new TextLocation(selectedLine, after), gc).x;
						if (afterX - x < x - middleX) {
							return new TextLocation(clickedLine, after);
						}
						return new TextLocation(clickedLine, middle);
					}
					before = middle;
				}
			}
			return new TextLocation(clickedLine, text.length());
		});
	}

	protected void widgetDisposed(Event e) {
		caret.dispose();
	}

	private void paintControl(Event e) {
		Drawing.drawWithGC(this, e.gc, renderer::paint);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		redraw();
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
		Rectangle visibleArea = renderer.getVisibleArea();
		return visibleArea.y / getLineHeight();
	}

	public int getTopPixel() {
		checkWidget();
		if ((style & SWT.SINGLE) != 0) {
			return 0;
		}
		return renderer.getVisibleArea().y;
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
		return renderer.getLocationByTextLocation(textLocation, gc);
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
		int height = renderer.getLineHeight(gc);
		gc.dispose();
		return height;
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
		 if ((style & SWT.PASSWORD) != 0) return;
		    String selectedText = model.getSelectedText();
		    if (selectedText != null && !selectedText.isEmpty()) {
		        Clipboard clipboard = new Clipboard(getDisplay());
		        TextTransfer textTransfer = TextTransfer.getInstance();
		        clipboard.setContents(new Object[] { selectedText }, new Transfer[] { textTransfer });
		        clipboard.dispose();
		    }
	}

	public void cut() {
		copy();
		model.replaceSelectedTextWith("");
	}

	public void paste() {
		checkWidget();
	    Clipboard clipboard = new Clipboard(getDisplay());
	    TextTransfer textTransfer = TextTransfer.getInstance();
	    String clipboardText = (String) clipboard.getContents(textTransfer);
	    clipboard.dispose();
	    if (clipboardText != null && !clipboardText.isEmpty()) {
	        model.insert(clipboardText);
	    }
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
		return TextModel.DELIMITER;
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

	public TextCaret getCaret() {
		return caret;
	}

	public void setCaret(TextCaret caret) {
		this.caret = caret;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		caret.setVisible(visible);
	}

	@Override
	public boolean setFocus() {
		final boolean focused = super.setFocus();
		if (focused) {
			caret.setFocus();
		}
		return focused;
	}

	private boolean isPasswordMode() {
		return (style & SWT.PASSWORD) != 0;
	}

	private char getEffectiveEchoChar() {
		char echo = getEchoChar();
		return echo != 0 ? echo : '*';
	}

	private String maskText(int length, char echoChar) {
		char[] masked = new char[length];
		Arrays.fill(masked, echoChar);
		return new String(masked);
	}

	String getDisplayText() {
		if (isPasswordMode()) {
			return maskText(model.getCharCount(), getEffectiveEchoChar());
		}
		return model.getText();
	}

	String[] getDisplayLines() {
		if (isPasswordMode()) {
			char echo = getEffectiveEchoChar();
			String[] lines = model.getLines();
			String[] masked = new String[lines.length];
			for (int i = 0; i < lines.length; i++) {
				masked[i] = maskText(lines[i].length(), echo);
			}
			return masked;
		}
		return model.getLines();
	}
}
