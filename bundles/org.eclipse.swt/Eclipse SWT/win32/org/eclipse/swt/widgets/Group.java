package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class provide an etched border
 * with an optional title.
 * <p>
 * Shadow styles are hints and may not be honoured
 * by the platform.  To create a group with the
 * default shadow style for the platform, do not
 * specify a shadow style.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SHADOW_ETCHED_IN, SHADOW_ETCHED_OUT, SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */

public class Group extends Composite {
	static final int GroupProc;
	static final byte [] GroupClass = Converter.wcsToMbcs (0, "BUTTON\0");
	static {
		WNDCLASSEX lpWndClass = new WNDCLASSEX ();
		lpWndClass.cbSize = WNDCLASSEX.sizeof;
		OS.GetClassInfoEx (0, GroupClass, lpWndClass);
		GroupProc = lpWndClass.lpfnWndProc;
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
public Group (Composite parent, int style) {
	super (parent, checkStyle (style));
}

int callWindowProc (int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	return OS.CallWindowProc (GroupProc, handle, msg, wParam, lParam);
}

static int checkStyle (int style) {
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	RECT rect = new RECT ();
	int newFont, oldFont = 0;
	int hDC = OS.GetDC (handle);
	newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	int length = OS.GetWindowTextLength (handle);
	byte [] buffer1 = new byte [length + 1];
	OS.GetWindowText (handle, buffer1, length + 1);
	int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE;
	OS.DrawText (hDC, buffer1, length, rect, flags);
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	Point size;
	if (layout != null) {
		size = layout.computeSize (this, wHint, hHint, changed);
	} else {
		size = minimumSize ();
	}
	width = size.x;  height = size.y;
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	Rectangle trim = computeTrim (0, 0, width, height);
	width = Math.max (trim.width, rect.right - rect.left + 6);
	height = trim.height;
	return new Point (width, height);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	Rectangle trim = super.computeTrim (x, y, width, height);
	int newFont, oldFont = 0;
	int hDC = OS.GetDC (handle);
	newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	TEXTMETRIC tm = new TEXTMETRIC ();
	OS.GetTextMetrics (hDC, tm);
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	int inset = 3;
	trim.x -= inset;  trim.y -= tm.tmHeight;
	trim.width += (inset * 2);  trim.height += tm.tmHeight + inset;
	return trim;
}

void createHandle () {
	super.createHandle ();
	state &= ~CANVAS;
}

public Rectangle getClientArea () {
	checkWidget ();
	if (parent.hdwp != 0) {
		int oldHdwp = parent.hdwp;
		parent.hdwp = 0;
		OS.EndDeferWindowPos (oldHdwp);
		int count = parent.getChildrenCount ();
		parent.hdwp = OS.BeginDeferWindowPos (count);
	}
	RECT rect = new RECT ();
	OS.GetClientRect (handle, rect);
	int newFont, oldFont = 0;
	int hDC = OS.GetDC (handle);
	newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	TEXTMETRIC tm = new TEXTMETRIC ();
	OS.GetTextMetrics (hDC, tm);
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	int inset = 3, x = inset, y = tm.tmHeight;
	return new Rectangle (x, y, rect.right - (inset * 2), rect.bottom - y - inset);
}

String getNameText () {
	return getText ();
}

/**
 * Returns the receiver's text, which is the string that the
 * is used as the <em>title</em>. If the text has not previously
 * been set, returns an empty string.
 *
 * @return the text
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

boolean mnemonicHit () {
	return setFocus ();
}

boolean mnemonicMatch (char key) {
	char mnemonic = findMnemonic (getText ());
	if (mnemonic == '\0') return false;
	return Character.toUpperCase (key) == Character.toUpperCase (mnemonic);
}

/**
 * Sets the receiver's text, which is the string that will
 * be displayed as the receiver's <em>title</em>, to the argument,
 * which may not be null. 
 *
 * @param text the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), string, true);
	OS.SetWindowText (handle, buffer);
}

int widgetStyle () {
	/*
	* Bug in Windows.  When GetDCEx () is called with DCX_INTERSECTUPDATE,
	* the HDC that is returned does not include the current update region.
	* This was confirmed under DEBUG Windows when GetDCEx () complained about
	* invalid flags.  Therefore, it is not easily possible to get an HDC from
	* outside of WM_PAINT that includes the current damage and clips children.
	* Because the receiver has children and draws a frame and label, it is
	* necessary that the receiver always draw clipped, in the current damaged
	* area.  The fix is to force the receiver to be fully clipped by including
	* WS_CLIPCHILDREN and WS_CLIPSIBLINGS in the default style bits.
	*/
	return super.widgetStyle () | OS.BS_GROUPBOX | OS.WS_CLIPCHILDREN | OS.WS_CLIPSIBLINGS;
}

byte [] windowClass () {
	return GroupClass;
}

int windowProc () {
	return GroupProc;
}

LRESULT WM_LBUTTONDBLCLK (int wParam, int lParam) {
	/*
	* Feature in Windows.  When the user clicks on the group
	* box label, the group box takes focus.  This is unwanted.
	* The fix is to avoid calling the group box window proc.
	*/
	/*
	* Bug in Windows.  For some reason, if we allow the group
	* box to take focus, it will draw on top of pull down combo
	* box children.  The fix is to avoid calling the window proc
	* to disallow the receiver from getting focus.
	*/
	sendMouseEvent (SWT.MouseDown, 1, OS.WM_LBUTTONDOWN, wParam, lParam);
	sendMouseEvent (SWT.MouseDoubleClick, 1, OS.WM_LBUTTONDBLCLK, wParam, lParam);
	if (OS.GetCapture () != handle) OS.SetCapture (handle);
	return LRESULT.ZERO;
}

LRESULT WM_LBUTTONDOWN (int wParam, int lParam) {
	/*
	* Feature in Windows.  When the user clicks on the group
	* box label, the group box takes focus.  This is unwanted.
	* The fix is to avoid calling the group box window proc.
	*/
	/*
	* Bug in Windows.  For some reason, if we allow the group
	* box to take focus, it will draw on top of pull down combo
	* box children.  The fix is to avoid calling the window proc
	* to disallow the receiver from getting focus.
	*/
	sendMouseEvent (SWT.MouseDown, 1, OS.WM_LBUTTONDOWN, wParam, lParam);
	if (OS.GetCapture () != handle) OS.SetCapture (handle);
	if (hooks (SWT.DragDetect)) {
		POINT pt = new POINT ();
		pt.x = (short) (lParam & 0xFFFF);
		pt.y = (short) (lParam >> 16);
		if (OS.DragDetect (handle, pt)) {
			sendEvent (SWT.DragDetect);
			// widget could be disposed at this point
		}
	}
	return LRESULT.ZERO;
}

LRESULT WM_NCHITTEST (int wParam, int lParam) {
	LRESULT result = super.WM_NCHITTEST (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  The window proc for the group box
	* returns HTTRANSPARTENT indicating that mouse messages
	* should not be delivered to the receiver and any children.
	* Normally, group boxes in Windows do not have children and
	* this is the correct behavior for this case.  Because we
	* allow children, answer HTCLIENT to allow mouse messages
	* to be delivered to the children.
	*/
	return new LRESULT (OS.HTCLIENT);
}

}
