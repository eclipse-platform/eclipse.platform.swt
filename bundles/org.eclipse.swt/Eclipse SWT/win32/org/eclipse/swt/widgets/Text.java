package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify text.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>MULTI, SINGLE, READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Verify</dd>
 * </dl>
 * </p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 */
public class Text extends Scrollable {
	int tabs, oldStart, oldEnd;
	boolean doubleClick, ignoreVerify, ignoreCharacter;
	
	public static final int LIMIT;
	public static final String DELIMITER;
	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = IsWinNT ? 0x7FFFFFFF : 0x7FFF;
		DELIMITER = "\r\n";
	}
	
	static final int EditProc;
	static final byte [] EditClass = Converter.wcsToMbcs (0, "EDIT\0");
	static {
		WNDCLASSEX lpWndClass = new WNDCLASSEX ();
		lpWndClass.cbSize = WNDCLASSEX.sizeof;
		OS.GetClassInfoEx (0, EditClass, lpWndClass);
		EditProc = lpWndClass.lpfnWndProc;
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
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Text (Composite parent, int style) {
	super (parent, checkStyle (style));
}

int callWindowProc (int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	return OS.CallWindowProc (EditProc, handle, msg, wParam, lParam);
}

void createHandle () {
	super.createHandle ();
	OS.SendMessage (handle, OS.EM_LIMITTEXT, 0, 0);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is modified, by sending
 * it one of the messages defined in the <code>ModifyListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ModifyListener
 * @see #removeModifyListener
 */
public void addModifyListener (ModifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Modify, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is not called for texts.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed in a single-line text.
 * </p>
 *
 * @param listener the listener which should be notified
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
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is verified, by sending
 * it one of the messages defined in the <code>VerifyListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see VerifyListener
 * @see #removeVerifyListener
 */
public void addVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Verify, typedListener);
}

/**
 * Appends a string.
 * <p>
 * The new text is appended to the text at
 * the end of the widget.
 * </p>
 *
 * @param string the string to be appended
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void append (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	string = Display.withCrLf (string);
	int length = OS.GetWindowTextLength (handle);
	if (hooks (SWT.Verify)) {
		string = verifyText (string, length, length);
		if (string == null) return;
	}
	OS.SendMessage (handle, OS.EM_SETSEL, length, length);
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), string, true);
	OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
	OS.SendMessage (handle, OS.EM_SCROLLCARET, 0, 0);
}

static int checkStyle (int style) {
	if ((style & SWT.SINGLE) != 0) style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
	if ((style & (SWT.SINGLE | SWT.MULTI)) != 0) return style;
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) return style | SWT.MULTI;
	return style | SWT.SINGLE;
}

/**
 * Clears the selection.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void clearSelection () {
	checkWidget ();
	OS.SendMessage (handle, OS.EM_SETSEL, -1, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int newFont, oldFont = 0;
	int hDC = OS.GetDC (handle);
	newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	TEXTMETRIC tm = new TEXTMETRIC ();
	OS.GetTextMetrics (hDC, tm);
	int count = OS.SendMessage (handle, OS.EM_GETLINECOUNT, 0, 0);
	int height = count * tm.tmHeight, width = 0;
	RECT rect = new RECT ();
	int flags = OS.DT_CALCRECT | OS.DT_EDITCONTROL | OS.DT_NOPREFIX;
	if ((style & SWT.WRAP) != 0 && wHint != SWT.DEFAULT) {
		flags |= OS.DT_WORDBREAK;
		rect.right = wHint;
	}
	String text = getText ();
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), text, false);
	OS.DrawText (hDC, buffer, buffer.length, rect, flags);
	width = rect.right - rect.left;
	if ((style & SWT.WRAP) != 0 && hHint == SWT.DEFAULT) {
		int newHeight = rect.bottom - rect.top;
		if (newHeight != 0) height = newHeight;
	}
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;

	/* Calculate the margin width */
	int margins = OS.SendMessage(handle, OS.EM_GETMARGINS, 0, 0);
	int marginWidth = (margins & 0xFFFF) + ((margins >> 16) & 0xFFFF);
	width += marginWidth;
	
	/*
	* Bug in Windows.  For some reason, despite the fact
	* that there is a 1-pixel margin height on a simple
	* text widget with a border, EM_GETRECT returns 0.
	* The fix is to detect this case and make the margin
	* height 1.
	*/
	RECT editRect = new RECT ();
	OS.SendMessage (handle, OS.EM_GETRECT, 0, rect);
	if ((style & SWT.BORDER) != 0 && editRect.top == 0) {
		editRect.top = 1;
	}

	/*
	* The preferred height of a single-line text widget
	* has been hand-crafted to be the same height as
	* the single-line text widget in an editable combo
	* box.
	*/
	width += editRect.left * 2;
	height += editRect.top * 2;
	if ((style & SWT.V_SCROLL) != 0) {
		width += OS.GetSystemMetrics (OS.SM_CXVSCROLL);
	}
	if ((style & SWT.H_SCROLL) != 0) {
		height += OS.GetSystemMetrics (OS.SM_CYHSCROLL);
		if ((style & SWT.BORDER) == 0) width++;
	}
	if ((style & SWT.BORDER) != 0) {
		int border = getBorderWidth ();
		width += (border * 2) + 3;
		height += (border * 2) + 1;
	}
	return new Point (width, height);
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
	OS.SendMessage (handle, OS.WM_COPY, 0, 0);
}

ScrollBar createScrollBar (int type) {
	return new ScrollBar (this, type);
}

void createWidget () {
	super.createWidget ();
	doubleClick = true;
	setTabStops (tabs = 8);
}

/**
 * Cuts the selected text.
 * <p>
 * The current selection is first copied to the
 * clipboard and then deleted from the widget.
 * </p>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void cut () {
	checkWidget ();
	OS.SendMessage (handle, OS.WM_CUT, 0, 0);
}

int defaultBackground () {
	return OS.GetSysColor (OS.COLOR_WINDOW);
}

/**
 * Gets the line number of the caret.
 * <p>
 * The line number of the caret is returned.
 * </p>
 *
 * @return the line number
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCaretLineNumber () {
	checkWidget ();
	return OS.SendMessage (handle, OS.EM_LINEFROMCHAR, -1, 0);
}

/**
 * Gets the location the caret.
 * <p>
 * The location of the caret is returned.
 * </p>
 *
 * @return a point, the location of the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getCaretLocation () {
	checkWidget ();
	/*
	* Bug in Windows.  For some reason, Windows is unable
	* to return the pixel coordinates of the last character
	* in the widget.  The fix is to temporarily insert a
	* space, query the coordinates and delete the space.
	* The selection is always an i-beam in this case because
	* this is the only time the start of the selection can
	* be equal to the last character position in the widget.
	* If EM_POSFROMCHAR fails for any other reason, return
	* pixel coordinates (0,0). 
	*/
	int [] start = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, (int []) null);
	int pos = OS.SendMessage (handle, OS.EM_POSFROMCHAR, start [0], 0);
	if (pos == -1) {
		pos = 0;
		if (start [0] >= OS.GetWindowTextLength (handle)) {
			OS.SendMessage (handle, OS.EM_REPLACESEL, 0, new byte [] {(byte)' ', 0});
			pos = OS.SendMessage (handle, OS.EM_POSFROMCHAR, start [0], 0);
			OS.SendMessage (handle, OS.EM_SETSEL, start [0], start [0] + 1);
			OS.SendMessage (handle, OS.EM_REPLACESEL, 0, new byte [] {0});
		}
	}
	return new Point ((short) (pos & 0xFFFF), (short) (pos >> 16));
}

/**
 * Gets the position of the caret.
 * <p>
 * The character position of the caret is returned.
 * </p>
 *
 * @return the position of the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCaretPosition () {
	checkWidget ();
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	int startLine = OS.SendMessage (handle, OS.EM_LINEFROMCHAR, start [0], 0);
	int caretPos = OS.SendMessage (handle, OS.EM_LINEINDEX, -1, 0);
	int caretLine = OS.SendMessage (handle, OS.EM_LINEFROMCHAR, caretPos, 0);
	int caret = end [0];
	if (caretLine == startLine) caret = start [0];
	if (IsDBLocale) caret = mbcsToWcsPos (caret);
	return caret;
}

/**
 * Gets the number of characters.
 *
 * @return number of characters in the widget
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCharCount () {
	checkWidget ();
	int length = OS.GetWindowTextLength (handle);
	if (IsDBLocale) length = mbcsToWcsPos (length);
	return length;
}

char [] getClipboardData () {
	char [] lpWideCharStr = null;
	if (OS.OpenClipboard (0)) {
		int hMem = OS.GetClipboardData (OS.CF_TEXT);
		if (hMem != 0) {
			int ptr = OS.GlobalLock (hMem);
			if (ptr != 0) {
				/* Use the character encoding for the default locale */
				int cp = OS.CP_ACP;
				int cchWideChar = OS.MultiByteToWideChar (cp, OS.MB_PRECOMPOSED, ptr, -1, null, 0);
				lpWideCharStr = new char [--cchWideChar];
				OS.MultiByteToWideChar (cp, OS.MB_PRECOMPOSED, ptr, -1, lpWideCharStr, cchWideChar);
			}
		}
		OS.CloseClipboard ();
	}
	return lpWideCharStr;
}

String getClipboardText () {
	char [] data = getClipboardData ();
	if (data == null) return null;
	return new String (data);
}

/**
 * Gets the double click enabled flag.
 * <p>
 * The double click flag enables or disables the
 * default action of the text widget when the user
 * double clicks.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getDoubleClickEnabled () {
	checkWidget ();
	return doubleClick;
}

/**
 * Gets the echo character.
 * <p>
 * The echo character is the character that is
 * displayed when the user enters text or the
 * text is changed by the programmer.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public char getEchoChar () {
	checkWidget ();
	char echo = (char) OS.SendMessage (handle, OS.EM_GETPASSWORDCHAR, 0, 0);
	if (echo != 0 && (echo = mbcsToWcs (echo, getCodePage ())) == 0) echo = '*';
	return echo;
}

/**
 * Gets the editable state.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getEditable () {
	checkWidget ();
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	return (bits & OS.ES_READONLY) == 0;
}

/**
 * Gets the number of lines.
 *
 * @return the number of lines in the widget
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getLineCount () {
	checkWidget ();
	return OS.SendMessage (handle, OS.EM_GETLINECOUNT, 0, 0);
}

/**
 * Gets the line delimiter.
 *
 * @return a string that is the line delimiter
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getLineDelimiter () {
	checkWidget ();
	return DELIMITER;
}

/**
 * Gets the height of a line.
 *
 * @return the height of a row of text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getLineHeight () {
	checkWidget ();
	int newFont, oldFont = 0;
	int hDC = OS.GetDC (handle);
	newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	TEXTMETRIC tm = new TEXTMETRIC ();
	OS.GetTextMetrics (hDC, tm);
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	return tm.tmHeight;
}

/**
 * Gets the position of the selected text.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p>
 * 
 * @return the start and end of the selection
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSelection () {
	checkWidget ();
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	if (IsDBLocale) {
		start [0] = mbcsToWcsPos (start [0]);
		end [0] = mbcsToWcsPos (end [0]);
	}
	return new Point (start [0], end [0]);
}

/**
 * Gets the number of selected characters.
 *
 * @return the number of selected characters.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionCount () {
	checkWidget ();
	Point selection = getSelection ();
	return selection.y - selection.x;
}

/**
 * Gets the selected text.
 *
 * @return the selected text
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getSelectionText () {
	checkWidget ();
	/*
	* NOTE: The current implementation uses substring ()
	* which can reference a potentially large character
	* array.
	*/
	Point selection = getSelection ();
	return getText ().substring (selection.x, selection.y);
}

/**
 * Gets the number of tabs.
 * <p>
 * Tab stop spacing is specified in terms of the
 * space (' ') character.  The width of a single
 * tab stop is the pixel width of the spaces.
 * </p>
 *
 * @return the number of tab characters
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTabs () {
	checkWidget ();
	return tabs;
}

int getTabWidth (int tabs) {
	int oldFont = 0;
	RECT rect = new RECT ();
	int hDC = OS.GetDC (handle);
	int newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
	OS.DrawText (hDC, new byte [] {(byte) ' '}, 1, rect, flags);
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	return (rect.right - rect.left) * tabs;
}

/**
 * Gets the widget text.
 * <p>
 * The text for a text widget is the characters in the widget.
 * </p>
 *
 * @return the widget text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget ();
	int length = OS.GetWindowTextLength (handle);
	if (length == 0) return "";
	byte [] buffer1 = new byte [length + 1];
	OS.GetWindowText (handle, buffer1, buffer1.length);
	char [] buffer2 = Converter.mbcsToWcs (getCodePage (), buffer1);
	return new String (buffer2, 0, buffer2.length - 1);
}

/**
 * Gets a range of text.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N-1 where N is
 * the number of characters in the widget.
 * </p>
 *
 * @param start the start of the range
 * @param end the end of the range
 * @return the range of text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText (int start, int end) {
	checkWidget ();
	/*
	* NOTE: The current implementation uses substring ()
	* which can reference a potentially large character
	* array.
	*/
	return getText ().substring (start, end + 1);
}

/**
 * Returns the maximum number of characters that the receiver is capable of holding. 
 * <p>
 * If this has not been changed by <code>setTextLimit()</code>,
 * it will be the constant <code>Text.LIMIT</code>.
 * </p>
 * 
 * @return the text limit
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTextLimit () {
	checkWidget ();
	return OS.SendMessage (handle, OS.EM_GETLIMITTEXT, 0, 0);
}

/**
 * Returns the zero-relative index of the line which is currently
 * at the top of the receiver.
 * <p>
 * This index can change when lines are scrolled or new lines are added or removed.
 * </p>
 *
 * @return the index of the top line
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopIndex () {
	checkWidget ();
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE); 
	if ((bits & OS.ES_MULTILINE) == 0) return 0;
	return OS.SendMessage (handle, OS.EM_GETFIRSTVISIBLELINE, 0, 0);
}

/**
 * Gets the top pixel.
 * <p>
 * The top pixel is the pixel position of the line
 * that is currently at the top of the widget.  On
 * some platforms, a text widget can be scrolled by
 * pixels instead of lines so that a partial line
 * is displayed at the top of the widget.
 * </p><p>
 * The top pixel changes when the widget is scrolled.
 * The top pixel does not include the widget trimming.
 * </p>
 *
 * @return the pixel position of the top line
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopPixel () {
	checkWidget ();
	/*
	* Note, EM_GETSCROLLPOS is implemented in Rich Edit 3.0
	* and greater.  The plain text widget and previous versions
	* of Rich Edit return zero.
	*/
	int [] buffer = new int [2];
	int code = OS.SendMessage (handle, OS.EM_GETSCROLLPOS, 0, buffer);
	if (code == 1) return buffer [1];
	return getTopIndex () * getLineHeight ();
}

/*
* Currently not used.
*/
boolean getWrap () {
	checkWidget ();
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE); 
	if ((bits & OS.ES_MULTILINE) == 0) return false;
	return (bits & (OS.WS_HSCROLL | OS.ES_AUTOHSCROLL)) == 0;
}

/**
 * Inserts a string.
 * <p>
 * The old selection is replaced with the new text.
 * </p>
 *
 * @param string the string
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void insert (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	string = Display.withCrLf (string);
	if (hooks (SWT.Verify)) {
		int [] start = new int [1], end = new int [1];
		OS.SendMessage (handle, OS.EM_GETSEL, start, end);
		string = verifyText (string, start [0], end [0]);
		if (string == null) return;
	}
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), string, true);
	OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
}

int mbcsToWcsPos (int mbcsPos) {
	if (mbcsPos == 0) return 0;
	int cp = getCodePage ();
	int wcsTotal = 0, mbcsTotal = 0;
	byte [] buffer = new byte [128];
	String delimiter = getLineDelimiter();
	int delimiterSize = delimiter.length ();
	int count = OS.SendMessage (handle, OS.EM_GETLINECOUNT, 0, 0);
	for (int line=0; line<count; line++) {
		int wcsSize = 0;
		int linePos = OS.SendMessage (handle, OS.EM_LINEINDEX, line, 0);
		int mbcsSize = OS.SendMessage (handle, OS.EM_LINELENGTH, linePos, 0);
		if (mbcsSize != 0) {
			if (mbcsSize + delimiterSize > buffer.length) {
				buffer = new byte [mbcsSize + delimiterSize];
			}
			buffer [0] = (byte) (mbcsSize & 0xFF);
			buffer [1] = (byte) (mbcsSize >> 8);
			mbcsSize = OS.SendMessage (handle, OS.EM_GETLINE, line, buffer);
			wcsSize = OS.MultiByteToWideChar (cp, OS.MB_PRECOMPOSED, buffer, mbcsSize, null, 0);
		}
		if (line - 1 != count) {
			for (int i=0; i<delimiterSize; i++) {
				buffer [mbcsSize++] = (byte) delimiter.charAt (i);
			}
			wcsSize += delimiterSize;
		}
		if ((mbcsTotal + mbcsSize) >= mbcsPos) {
			int bufferSize = mbcsPos - mbcsTotal;
			wcsSize = OS.MultiByteToWideChar (cp, OS.MB_PRECOMPOSED, buffer, bufferSize, null, 0);
			return wcsTotal + wcsSize;
		}
		wcsTotal += wcsSize;
		mbcsTotal += mbcsSize;
	}
	return wcsTotal;
}

/**
 * Pastes text from clipboard.
 * <p>
 * The selected text is deleted from the widget
 * and new text inserted from the clipboard.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void paste () {
	checkWidget ();
	OS.SendMessage (handle, OS.WM_PASTE, 0, 0);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver's text is modified.
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
 * @see ModifyListener
 * @see #addModifyListener
 */
public void removeModifyListener (ModifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Modify, listener);	
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected.
 *
 * @param listener the listener which should be notified
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is verified.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see VerifyListener
 * @see #addVerifyListener
 */
public void removeVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);	
}

/**
 * Selects all the text in the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void selectAll () {
	checkWidget ();
	OS.SendMessage (handle, OS.EM_SETSEL, 0, -1);
}

boolean sendKeyEvent (int type, int msg, int wParam, int lParam, Event event) {
	if (!super.sendKeyEvent (type, msg, wParam, lParam, event)) {
		return false;
	}
	if (ignoreVerify) return true;
	if (type != SWT.KeyDown) return true;
	if (msg != OS.WM_CHAR && msg != OS.WM_KEYDOWN && msg != OS.WM_IME_CHAR) {
		return true;
	}
	if (event.character == 0) return true;
	if (!hooks (SWT.Verify)) return true;
	char key = event.character;
	int stateMask = event.stateMask;
	
	/* Disable all magic keys that could modify the text */
	switch (msg) {
		case OS.WM_CHAR:
			if (key != 0x08 && key != 0x7F && key != '\r' && key != '\t' && key != '\n') break;
			// FALL THROUGH
		case OS.WM_KEYDOWN:
			if ((stateMask & (SWT.CTRL | SWT.SHIFT | SWT.ALT)) != 0) return false;
			break;
	}

	/*
	* If the left button is down, the text widget
	* refuses the character.
	*/
	if (OS.GetKeyState (OS.VK_LBUTTON) < 0) {
		return true;
	}

	/* Verify the character */
	String oldText = "";
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	switch (key) {
		case 0x08:	/* Bs */
			if (start [0] == end [0]) {
				if (start [0] == 0) return true;
				int lineStart = OS.SendMessage (handle, OS.EM_LINEINDEX, -1, 0);
				if (start [0] == lineStart) {
					start [0] = start [0] - DELIMITER.length ();
				} else {
					start [0] = start [0] - 1;
					if (IsDBLocale) {
						int [] newStart = new int [1], newEnd = new int [1];
						OS.SendMessage (handle, OS.EM_SETSEL, start [0], end [0]);
						OS.SendMessage (handle, OS.EM_GETSEL, newStart, newEnd);
						if (start [0] != newStart [0]) start [0] = start [0] - 1;
					}
				}
				start [0] = Math.max (start [0], 0);
			}
			break;
		case 0x7F:	/* Del */
			if (start [0] == end [0]) {
				int length = OS.GetWindowTextLength (handle);
				if (start [0] == length) return true;
				int line = OS.SendMessage (handle, OS.EM_LINEFROMCHAR, end [0], 0);
				int lineStart = OS.SendMessage (handle, OS.EM_LINEINDEX, line + 1, 0);
				if (end [0] == lineStart - DELIMITER.length ()) {
					end [0] = end [0] + DELIMITER.length ();
				} else {
					end [0] = end [0] + 1;
					if (IsDBLocale) {
						int [] newStart = new int [1], newEnd = new int [1];
						OS.SendMessage (handle, OS.EM_SETSEL, start [0], end [0]);
						OS.SendMessage (handle, OS.EM_GETSEL, newStart, newEnd);
						if (end [0] != newEnd [0]) end [0] = end [0] + 1;
					}
				}
				end [0] = Math.min (end [0], length);
			}
			break;
		case '\r':	/* Return */
			int bits = OS.GetWindowLong (handle, OS.GWL_STYLE); 
			if ((bits & OS.ES_MULTILINE) == 0) return true;
			oldText = DELIMITER;
			break;
		default:	/* Tab and other characters */
			if (key != '\t' && key < 0x20) return true;
			oldText = new String (new char [] {key});
			break;
	}
	String newText = verifyText (oldText, start [0], end [0], event);
	if (newText == null) return false;
	if (newText == oldText) return true;
	newText = Display.withCrLf (newText);
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), newText, true);
	OS.SendMessage (handle, OS.EM_SETSEL, start [0], end [0]);
	OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
	return false;
}

/**
 * Sets the double click enabled flag.
 * <p>
 * The double click flag enables or disables the
 * default action of the text widget when the user
 * double clicks.
 * </p>
 * 
 * @param doubleClick the new double click flag
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDoubleClickEnabled (boolean doubleClick) {
	checkWidget ();
	this.doubleClick = doubleClick;
}

/**
 * Sets the echo character.
 * <p>
 * The echo character is the character that is
 * displayed when the user enters text or the
 * text is changed by the programmer.
 * </p>
 *
 * @param echo the new echo character
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEchoChar (char echo) {
	checkWidget ();
	if (echo != 0) {
		if ((echo = wcsToMbcs (echo, getCodePage ())) == 0) echo = '*';
	}
	OS.SendMessage (handle, OS.EM_SETPASSWORDCHAR, echo, 0);
	/*
	* Bug in Windows.  When the password character is changed,
	* Windows does not redraw to show the new password character.
	* The fix is to force a redraw when the character is set.
	*/
	OS.InvalidateRect (handle, null, true);
}

/**
 * Sets the editable state.
 *
 * @param editable the new editable state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEditable (boolean editable) {
	checkWidget ();
	style &= ~SWT.READ_ONLY;
	if (!editable) style |= SWT.READ_ONLY; 
	OS.SendMessage (handle, OS.EM_SETREADONLY, editable ? 0 : 1, 0);
}

public void setFont (Font font) {
	checkWidget ();
	super.setFont (font);
	setTabStops (tabs);
}

/**
 * Sets the selection.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p><p>
 * Text selections are specified in terms of
 * caret positions.  In a text widget that
 * contains N characters, there are N+1 caret
 * positions, ranging from 0..N.  This differs
 * from other functions that address character
 * position such as getText () that use the
 * regular array indexing rules.
 * </p>
 *
 * @param start new caret position
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int start) {
	checkWidget ();
	if (IsDBLocale) start = wcsToMbcsPos (start);
	OS.SendMessage (handle, OS.EM_SETSEL, start, start);
}

/**
 * Sets the selection.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p><p>
 * Text selections are specified in terms of
 * caret positions.  In a text widget that
 * contains N characters, there are N+1 caret
 * positions, ranging from 0..N.  This differs
 * from other functions that address character
 * position such as getText () that use the
 * usual array indexing rules.
 * </p>
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int start, int end) {
	checkWidget ();
	if (IsDBLocale) {
		start = wcsToMbcsPos (start);
		end = wcsToMbcsPos (end);
	}
	OS.SendMessage (handle, OS.EM_SETSEL, start, end);
}

public void setRedraw (boolean redraw) {
	checkWidget ();
	super.setRedraw (redraw);
	/*
	* Feature in Windows.  When WM_SETREDRAW is used to turn
	* redraw off, the text control is not scrolled to show the
	* i-beam.  The fix is to detect that the i-beam has moved
	* while redraw is turned off and force it to be visible
	* when redraw is restored.
	*/
	if (drawCount != 0) return;
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	if (!redraw) {
		oldStart = start [0];  oldEnd = end [0];
		return;
	}
	if (oldStart == start [0] && oldEnd == end [0]) return;
	OS.SendMessage (handle, OS.EM_SCROLLCARET, 0, 0);		
}

/**
 * Sets the selection.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p><p>
 * Text selections are specified in terms of
 * caret positions.  In a text widget that
 * contains N characters, there are N+1 caret
 * positions, ranging from 0..N.  This differs
 * from other functions that address character
 * position such as getText () that use the
 * usual array indexing rules.
 * </p>
 *
 * @param selection the point
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (Point selection) {
	checkWidget ();
	if (selection == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (selection.x, selection.y);
}

 /**
 * Sets the number of tabs.
 * <p>
 * Tab stop spacing is specified in terms of the
 * space (' ') character.  The width of a single
 * tab stop is the pixel width of the spaces.
 * </p>
 *
 * @param tabs the number of tabs
 *
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
*/
public void setTabs (int tabs) {
	checkWidget ();
	if (tabs < 0) return;
	setTabStops (this.tabs = tabs);
}

void setTabStops (int tabs) {
	/*
	* Feature in Windows.  Windows expects the tab spacing in
	* dialog units so we must convert from space widths.  Due
	* to round off error, the tab spacing may not be the exact
	* number of space widths, depending on the font.
	*/
	int width = (getTabWidth (tabs) * 4) / (OS.GetDialogBaseUnits () & 0xFFFF);
	OS.SendMessage (handle, OS.EM_SETTABSTOPS, 1, new int [] {width});
}

/**
 * Sets the contents of the receiver to the given string.
 *
 * @param text the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	string = Display.withCrLf (string);
	if (hooks (SWT.Verify)) {
		int length = OS.GetWindowTextLength (handle);
		string = verifyText (string, 0, length);
		if (string == null) return;
	}
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), string, true);
	OS.SetWindowText (handle, buffer);
	/*
	* Bug in Windows.  When the widget is multi line
	* text widget, it does not send a WM_COMMAND with
	* control code EN_CHANGE from SetWindowText () to
	* notify the application that the text has changed.
	* The fix is to send the event.
	*/
	if ((style & SWT.MULTI) != 0) {
		sendEvent (SWT.Modify);
		// widget could be disposed at this point
	}
}

/**
 * Sets the maximum number of characters that the receiver
 * is capable of holding to be the argument.
 *
 * @param limit new text limit
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTextLimit (int limit) {
	checkWidget ();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	OS.SendMessage (handle, OS.EM_SETLIMITTEXT, limit, 0);
}

/**
 * Sets the zero-relative index of the line which is currently
 * at the top of the receiver. This index can change when lines
 * are scrolled or new lines are added and removed.
 *
 * @param index the index of the top item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTopIndex (int index) {
	checkWidget ();
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE); 
	if ((bits & OS.ES_MULTILINE) == 0) return;
	int count = OS.SendMessage (handle, OS.EM_GETLINECOUNT, 0, 0);
	index = Math.min (Math.max (index, 0), count - 1);
	int topIndex = OS.SendMessage (handle, OS.EM_GETFIRSTVISIBLELINE, 0, 0);
	OS.SendMessage (handle, OS.EM_LINESCROLL, 0, index - topIndex);
}

/*
* Not currently used.
*/
void setWrap (boolean wrap) {
	if (wrap == getWrap ()) return;
	style &= ~SWT.WRAP;
	if (wrap) style |= SWT.WRAP;
}

/**
 * Shows the selection.
 * <p>
 * If the selection is already showing
 * in the receiver, this method simply returns.  Otherwise,
 * lines are scrolled until the selection is visible.
 * </p>
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void showSelection () {
	checkWidget ();
	OS.SendMessage (handle, OS.EM_SCROLLCARET, 0, 0);
}

String verifyText (String string, int start, int end) {
	return verifyText (string, start, end, null);
}

String verifyText (String string, int start, int end, Event keyEvent) {
	if (ignoreVerify) return string;
	Event event = new Event ();
	event.text = string;
	event.start = start;
	event.end = end;
	if (keyEvent != null) {
		event.character = keyEvent.character;
		event.keyCode = keyEvent.keyCode;
		event.stateMask = keyEvent.stateMask;
	}
	if (IsDBLocale) {
		event.start = mbcsToWcsPos (start);
		event.end = mbcsToWcsPos (end);
	}
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the verify
	* event.  If this happens, answer null to cancel
	* the operation.
	*/
	sendEvent (SWT.Verify, event);
	if (!event.doit || isDisposed ()) return null;
	return event.text;
}

int wcsToMbcsPos (int wcsPos) {
	if (wcsPos == 0) return 0;
	int cp = getCodePage ();
	int wcsTotal = 0, mbcsTotal = 0;
	byte [] buffer = new byte [128];
	String delimiter = getLineDelimiter ();
	int delimiterSize = delimiter.length ();
	int count = OS.SendMessage (handle, OS.EM_GETLINECOUNT, 0, 0);
	for (int line=0; line<count; line++) {
		int wcsSize = 0;
		int linePos = OS.SendMessage (handle, OS.EM_LINEINDEX, line, 0);
		int mbcsSize = OS.SendMessage (handle, OS.EM_LINELENGTH, linePos, 0);
		if (mbcsSize != 0) {
			if (mbcsSize + delimiterSize > buffer.length) {
				buffer = new byte [mbcsSize + delimiterSize];
			}
			buffer [0] = (byte) (mbcsSize & 0xFF);
			buffer [1] = (byte) (mbcsSize >> 8);
			mbcsSize = OS.SendMessage (handle, OS.EM_GETLINE, line, buffer);
			wcsSize = OS.MultiByteToWideChar (cp, OS.MB_PRECOMPOSED, buffer, mbcsSize, null, 0);
		}
		if (line - 1 != count) {
			for (int i=0; i<delimiterSize; i++) {
				buffer [mbcsSize++] = (byte) delimiter.charAt (i);
			}
			wcsSize += delimiterSize;
		}
		if ((wcsTotal + wcsSize) >= wcsPos) {
			wcsSize = 0;
			int index = 0;
			while (index < mbcsSize) {
				if ((wcsTotal + wcsSize) == wcsPos) {
					return mbcsTotal + index;
				}
				if (OS.IsDBCSLeadByte (buffer [index++])) index++;
				wcsSize++;
			}
			return mbcsTotal + mbcsSize;
		}
		wcsTotal += wcsSize;
		mbcsTotal += mbcsSize;
	}
	return mbcsTotal;
}

int widgetStyle () {
	int bits = super.widgetStyle () | OS.WS_TABSTOP;
	if ((style & SWT.READ_ONLY) != 0) bits |= OS.ES_READONLY;
	if ((style & SWT.SINGLE) != 0) return bits | OS.ES_AUTOHSCROLL;
	bits |= OS.ES_MULTILINE | OS.ES_AUTOHSCROLL | OS.ES_NOHIDESEL;	
	if ((style & SWT.WRAP) != 0) bits &= ~(OS.WS_HSCROLL | OS.ES_AUTOHSCROLL);
	return bits;
}

byte [] windowClass () {
	return EditClass;
}

int windowProc () {
	return EditProc;
}

LRESULT WM_CHAR (int wParam, int lParam) {
	if (ignoreCharacter) return null;
	LRESULT result = super.WM_CHAR (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  For some reason, when the
	* widget is a single line text widget, when the
	* user presses tab, return or escape, Windows beeps.
	* The fix is to look for these keys and not call
	* the window proc.
	*/
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE); 
	if ((bits & OS.ES_MULTILINE) == 0) {
		switch (wParam) {
			case OS.VK_RETURN:
				postEvent (SWT.DefaultSelection);
				// FALL THROUGH
			case OS.VK_TAB:
			case OS.VK_ESCAPE: return LRESULT.ZERO;
		}
	}
	return result;
}

LRESULT WM_CLEAR (int wParam, int lParam) {
	LRESULT result = super.WM_CLEAR (wParam, lParam);
	if (result != null) return result;
	if (!hooks (SWT.Verify)) return result;
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.ES_READONLY) != 0) return result;
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	if (start [0] == end [0]) return result;
	String newText = verifyText ("", start [0], end [0]);
	if (newText == null) return LRESULT.ZERO;
	if (newText.length () != 0) {
		result = new LRESULT (callWindowProc (OS.WM_CLEAR, 0, 0));	
		newText = Display.withCrLf (newText);
		byte [] buffer = Converter.wcsToMbcs (getCodePage (), newText, true);
		OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
	}
	return result;
}

LRESULT WM_CUT (int wParam, int lParam) {
	LRESULT result = super.WM_CUT (wParam, lParam);
	if (result != null) return result;
	if (!hooks (SWT.Verify)) return result;
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.ES_READONLY) != 0) return result;
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	if (start [0] == end [0]) return result;
	String newText = verifyText ("", start [0], end [0]);
	if (newText == null) return LRESULT.ZERO;
	if (newText.length () != 0) {
		result = new LRESULT (callWindowProc (OS.WM_CUT, 0, 0));	
		newText = Display.withCrLf (newText);
		byte [] buffer = Converter.wcsToMbcs (getCodePage (), newText, true);
		OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
	}
	return result;
}

LRESULT WM_IME_CHAR (int wParam, int lParam) {

	/* Process a DBCS character */
	Display display = getDisplay ();
	display.lastKey = 0;
	display.lastAscii = wParam;
	display.lastVirtual = false;
	if (!sendKeyEvent (SWT.KeyDown, OS.WM_IME_CHAR, wParam, lParam)) {
		return LRESULT.ZERO;
	}
	sendKeyEvent (SWT.KeyUp, OS.WM_IME_CHAR, wParam, lParam);
	display.lastKey = display.lastAscii = 0;

	/*
	* Feature in Windows.  The Windows text widget uses
	* two 2 WM_CHAR's to process a DBCS key instead of
	* using WM_IME_CHAR.  The fix is to allow the text
	* widget to get the WM_CHAR's but ignore sending
	* them to the application.
	*/
	ignoreCharacter = true;
	int result = callWindowProc (OS.WM_IME_CHAR, wParam, lParam);
	MSG msg = new MSG ();
	while (OS.PeekMessage (msg, handle, OS.WM_CHAR, OS.WM_CHAR, OS.PM_REMOVE)) {
		OS.TranslateMessage (msg);
		OS.DispatchMessage (msg);
	}
	ignoreCharacter = false;
	return new LRESULT (result);
}

LRESULT WM_LBUTTONDBLCLK (int wParam, int lParam) {
	/*
	* Prevent Windows from processing WM_LBUTTONDBLCLK
	* when double clicking behavior is disabled by not
	* calling the window proc.
	*/
	sendMouseEvent (SWT.MouseDown, 1, OS.WM_LBUTTONDOWN, wParam, lParam);
	sendMouseEvent (SWT.MouseDoubleClick, 1, OS.WM_LBUTTONDBLCLK, wParam, lParam);
	if (OS.GetCapture () != handle) OS.SetCapture (handle);
	if (!doubleClick) return LRESULT.ZERO;
		
	/*
	* Bug in Windows.  When the last line of text in the
	* widget is double clicked and the line is empty, Windows
	* hides the i-beam then moves it to the first line in
	* the widget but does not scroll to show the user.
	* If the user types without clicking the mouse, invalid
	* characters are displayed at the end of each line of
	* text in the widget.  The fix is to detect this case
	* and avoid calling the window proc.
	*/
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	if (start [0] == end [0]) {
		int length = OS.GetWindowTextLength (handle);
		if (length == start [0]) {
			int result = OS.SendMessage (handle, OS.EM_LINELENGTH, length, 0);
			if (result == 0) return LRESULT.ZERO;
		}
	}
	return null;
}

LRESULT WM_PASTE (int wParam, int lParam) {
	LRESULT result = super.WM_PASTE (wParam, lParam);
	if (result != null) return result;
	if (!hooks (SWT.Verify)) return result;
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.ES_READONLY) != 0) return result;
	String oldText = getClipboardText ();
	if (oldText == null) return result;
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	String newText = verifyText (oldText, start [0], end [0]);
	if (newText == null) return LRESULT.ZERO;
	if (newText != oldText) {
		newText = Display.withCrLf (newText);
		byte [] buffer = Converter.wcsToMbcs (getCodePage (), newText, true);
		OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
		return LRESULT.ZERO;
	}
	return result;
}

LRESULT WM_SIZE (int wParam, int lParam) {
	LRESULT result = super.WM_SIZE (wParam, lParam);
	// widget may be disposed at this point
	if (handle == 0) return result;
	
	/*
	* Feature in Windows.  When the caret is moved,
	* the text widget scrolls to show the new location.
	* This means that the text widget may be scrolled
	* to the left in order to show the caret when the
	* widget is not large enough to show both the caret
	* location and all the text.  Unfortunately, when
	* the text widget is resized such that all the text
	* and the caret could be visible, Windows does not
	* scroll the widget back.  The fix is to save the
	* current selection, set the selection to the start
	* of the text and then restore the selection.  This
	* will cause the text widget recompute the left
	* scroll position.
	*
	* NOTE: Currently, this work around is only applied
	* to single line text widgets that are not visible.
	* If the widget is resized when it is visible, this
	* is fine because the user has already seen that the
	* text is scrolled.
	*/
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE); 
	if ((bits & OS.ES_MULTILINE) != 0) return result;
	if (OS.IsWindowVisible (handle)) return result;
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	if (start [0] == 0 && end [0] == 0) return result;
	OS.SendMessage (handle, OS.EM_SETSEL, 0, 0);
	OS.SendMessage (handle, OS.EM_SETSEL, start [0], end [0]);
	return result;
}

LRESULT WM_UNDO (int wParam, int lParam) {
	LRESULT result = super.WM_UNDO (wParam, lParam);
	if (result != null) return result;
	if (!hooks (SWT.Verify)) return result;

	/* Undo and then Redo to get the Undo text */
	if (OS.SendMessage (handle, OS.EM_CANUNDO, 0, 0) == 0) {
		return result;
	}
	ignoreVerify = true;
	callWindowProc (OS.WM_UNDO, wParam, lParam);
	String oldText = getSelectionText ();
	callWindowProc (OS.WM_UNDO, wParam, lParam);
	ignoreVerify = false;

	/* Verify the Undo operation */
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	String newText = verifyText (oldText, start [0], end [0]);
	if (newText == null) return LRESULT.ZERO;
	if (newText != oldText) {
		newText = Display.withCrLf (newText);
		byte [] buffer = Converter.wcsToMbcs (getCodePage (), newText, true);
		OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
		return LRESULT.ZERO;
	}
	
	/* Do the original Undo */
	ignoreVerify = true;
	callWindowProc (OS.WM_UNDO, wParam, lParam);
	ignoreVerify = false;
	return LRESULT.ONE;
}

LRESULT wmCommandChild (int wParam, int lParam) {
	int code = wParam >> 16;
	switch (code) {
		case OS.EN_CHANGE: 
			/*
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the modify
			* event.  If this happens, end the processing of the
			* Windows message by returning zero as the result of
			* the window proc.
			*/
			sendEvent (SWT.Modify);
			if (isDisposed ()) return LRESULT.ZERO;
			break;
	}
	return super.wmCommandChild (wParam, lParam);
}

LRESULT wmScroll (int msg, int wParam, int lParam) {
	int code = callWindowProc (msg, wParam, lParam);
	if (code == 0) return LRESULT.ZERO;
	return new LRESULT (code);
}

}
