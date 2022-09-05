/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
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
 *     Conrad Groth - Bug 401015 - [CSS] Add support for styling hyperlinks in Links
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class represent a selectable
 * user interface object that displays a text with
 * links.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#link">Link snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.1
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Link extends Control {
	String text;
	int linkForeground = -1;
	String [] ids;
	char [] mnemonics;
	int nextFocusItem = -1;
	static final long LinkProc;
	static final TCHAR LinkClass = new TCHAR (0, OS.WC_LINK, true);
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, LinkClass, lpWndClass);
		LinkProc = lpWndClass.lpfnWndProc;
		/*
		* Feature in Windows.  The SysLink window class
		* does not include CS_DBLCLKS.  This means that these
		* controls will not get double click messages such as
		* WM_LBUTTONDBLCLK.  The fix is to register a new
		* window class with CS_DBLCLKS.
		*
		* NOTE:  Screen readers look for the exact class name
		* of the control in order to provide the correct kind
		* of assistance.  Therefore, it is critical that the
		* new window class have the same name.  It is possible
		* to register a local window class with the same name
		* as a global class.  Since bits that affect the class
		* are being changed, it is possible that other native
		* code, other than SWT, could create a control with
		* this class name, and fail unexpectedly.
		*/
		lpWndClass.hInstance = OS.GetModuleHandle (null);
		lpWndClass.style &= ~OS.CS_GLOBALCLASS;
		lpWndClass.style |= OS.CS_DBLCLKS;
		OS.RegisterClass (LinkClass, lpWndClass);
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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Link (Composite parent, int style) {
	super (parent, style);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the control is selected by the user.
 * <code>widgetDefaultSelected</code> is not called.
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
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}

@Override
long callWindowProc (long hwnd, int msg, long wParam, long lParam) {
	if (handle == 0) return 0;
	/*
	* Feature in Windows.  By convention, native Windows controls
	* check for a non-NULL wParam, assume that it is an HDC and
	* paint using that device.  The SysLink control does not.
	* The fix is to check for an HDC and use WM_PRINTCLIENT.
	*/
	switch (msg) {
		case OS.WM_PAINT:
			if (wParam != 0) {
				OS.SendMessage (hwnd, OS.WM_PRINTCLIENT, wParam, 0);
				return 0;
			}
			break;
	}
	return OS.CallWindowProc (LinkProc, hwnd, msg, wParam, lParam);
}

@Override
Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width, height;
	/*
	 * When the text is empty, LM_GETIDEALSIZE returns zero width and height,
	 * but SWT convention is to return zero width and line height.
	 */
	if (text.isEmpty()) {
		long hDC = OS.GetDC (handle);
		long newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
		long oldFont = OS.SelectObject (hDC, newFont);
		TEXTMETRIC lptm = new TEXTMETRIC ();
		OS.GetTextMetrics (hDC, lptm);
		width = 0;
		height = lptm.tmHeight;
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (handle, hDC);
	} else {
		SIZE size = new SIZE ();
		int maxWidth = (wHint == SWT.DEFAULT) ? 0x7fffffff : wHint;
		OS.SendMessage (handle, OS.LM_GETIDEALSIZE, maxWidth, size);
		width = size.cx;
		height = size.cy;
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int border = getBorderWidthInPixels ();
	width += border * 2;
	height += border * 2;
	return new Point (width, height);
}

@Override
void createHandle () {
	super.createHandle ();
	state |= THEME_BACKGROUND;
}

@Override
void createWidget () {
	super.createWidget ();
	text = "";
	ids = new String[0];
	mnemonics = new char[0];
	/*
	 * Accessible tool like JAWS by default only reads the hypertext link text and
	 * not the non-link text. To make JAWS read the full text we need to tweak the
	 * default behavior and explicitly return the full link text as below.
	 */
	this.getAccessible().addAccessibleListener(new AccessibleAdapter() {
		@Override
		public void getName(AccessibleEvent e) {
			e.result = text;
		}
	});
}

@Override
void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	/*
	 * Feature in Windows.  SysLink32 control doesn't natively
	 * provide disabled state. Emulate it with custom draw.
	 */
	OS.InvalidateRect (handle, null, true);
}

int getFocusItem () {
	LITEM item = new LITEM ();
	item.mask = OS.LIF_ITEMINDEX | OS.LIF_STATE;
	item.stateMask = OS.LIS_FOCUSED;
	while (OS.SendMessage (handle, OS.LM_GETITEM, 0, item) != 0) {
		if ((item.state & OS.LIS_FOCUSED) != 0) {
			return item.iLink;
		}
		item.iLink++;
	}
	return -1;
}

/**
 * Returns the link foreground color.
 *
 * @return the receiver's link foreground color.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.105
 */
public Color getLinkForeground () {
	checkWidget ();
	if (linkForeground != -1) {
		return Color.win32_new (display, linkForeground);
	}
	return Color.win32_new (display, OS.GetSysColor (OS.COLOR_HOTLIGHT));
}

@Override
String getNameText () {
	return getText ();
}

/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget ();
	return text;
}

@Override
boolean mnemonicHit (char key) {
	char uckey = Character.toUpperCase (key);
	for (int i = 0; i < mnemonics.length; i++) {
		if (uckey == mnemonics[i]) {
			nextFocusItem = i;
			return setFocus () && setFocusItem (i);
		}
	}
	return false;
}

@Override
boolean mnemonicMatch (char key) {
	char uckey = Character.toUpperCase (key);
	for (char mnemonic : mnemonics) {
		if (uckey == mnemonic) {
			return true;
		}
	}
	return false;
}

void parse (String string) {
	int length = string.length ();
	// The shortest link length is 7 characters (<a></a>).
	ids = new String [length / 7];
	mnemonics = new char [length / 7];
	int index = 0, state = 0, linkIndex = 0;
	int linkStart = 0, linkEnd = 0, refStart = 0, refEnd = 0;
	char mnemonic = 0;
	while (index < length) {
		char c = Character.toLowerCase (string.charAt (index));
		switch (state) {
			case 0:
				if (c == '<') {
					state++;
				} else if (c == '&') {
					state = 16;
				}
				break;
			case 1:
				if (c == 'a') state++;
				break;
			case 2:
				switch (c) {
					case 'h':
						state = 7;
						break;
					case '>':
						linkStart = index  + 1;
						state++;
						break;
					default:
						if (Character.isWhitespace(c)) break;
						else state = 13;
				}
				break;
			case 3:
				if (c == '<') {
					linkEnd = index;
					state++;
				}
				break;
			case 4:
				state = c == '/' ? state + 1 : 3;
				break;
			case 5:
				state = c == 'a' ? state + 1 : 3;
				break;
			case 6:
				if (c == '>') {
					if (refStart == 0) {
						refStart = linkStart;
						refEnd = linkEnd;
					}
					ids [linkIndex] = string.substring(refStart, refEnd);
					if (mnemonic != 0) {
						mnemonics [linkIndex] = mnemonic;
					}
					linkIndex++;
					linkStart = linkEnd = refStart = refEnd = mnemonic = 0;
					state = 0;
				} else {
					state = 3;
				}
				break;
			case 7:
				state = c == 'r' ? state + 1 : 0;
				break;
			case 8:
				state = c == 'e' ? state + 1 : 0;
				break;
			case 9:
				state = c == 'f' ? state + 1 : 0;
				break;
			case 10:
				state = c == '=' ? state + 1 : 0;
				break;
			case 11:
				if (c == '"') {
					state++;
					refStart = index + 1;
				} else {
					state = 0;
				}
				break;
			case 12:
				if (c == '"') {
					refEnd = index;
					state = 2;
				}
				break;
			case 13:
				if (Character.isWhitespace (c)) {
					state = 0;
				} else if (c == '='){
					state++;
				}
				break;
			case 14:
				state = c == '"' ? state + 1 : 0;
				break;
			case 15:
				if (c == '"') state = 2;
				break;
			case 16:
				if (c == '<') {
					state = 1;
				} else {
					state = 0;
					if (c != '&') mnemonic = Character.toUpperCase (c);
				}
				break;
			default:
				state = 0;
				break;
		}
		index++;
	}
	ids = Arrays.copyOf(ids, linkIndex);
	mnemonics = Arrays.copyOf(mnemonics, linkIndex);
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	ids = null;
	mnemonics = null;
	text = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected by the user.
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection, listener);
}

boolean setFocusItem (int index) {
	int bits = 0;
	if (index > 0) {
		bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	}
	LITEM item = new LITEM ();
	item.mask = OS.LIF_ITEMINDEX | OS.LIF_STATE;
	item.stateMask = OS.LIS_FOCUSED;
	int activeIndex = getFocusItem ();
	if (activeIndex == index) return true;
	if (activeIndex >= 0) {
		/* Feature in Windows. Unfocus any element unfocus all elements.
		 * For example if item 2 is focused and we set unfocus (state = 0)
		 * for item 0 Windows will remove the focus state for item 2
		 * (getFocusItem() == -1) but fail to remove the focus border around
		 * the link. The fix is to only unfocus the element which has focus.
		 */
		item.iLink = activeIndex;
		OS.SendMessage (handle, OS.LM_SETITEM, 0, item);
	}
	item.iLink = index;
	item.state = OS.LIS_FOCUSED;
	long result = OS.SendMessage (handle, OS.LM_SETITEM, 0, item);

	if (index > 0) {
	/* Feature in Windows. For some reason, setting the focus to
	 * any item but first causes the control to clear the WS_TABSTOP
	 * bit. The fix is always to reset the bit.
	 */
	OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
	}
	return result != 0;
}

/**
 * Sets the link foreground color to the color specified
 * by the argument, or to the default system color for the link
 * if the argument is null.
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
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
 * @since 3.105
 */
public void setLinkForeground (Color color) {
	checkWidget ();
	int pixel = -1;
	if (color != null) {
		if (color.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		pixel = color.handle;
	}
	if (pixel == linkForeground) return;
	linkForeground = pixel;
	OS.InvalidateRect (handle, null, true);
}

/**
 * Sets the receiver's text.
 * <p>
 * The string can contain both regular text and hyperlinks.  A hyperlink
 * is delimited by an anchor tag, &lt;a&gt; and &lt;/a&gt;.  Within an
 * anchor, a single HREF attribute is supported.  When a hyperlink is
 * selected, the text field of the selection event contains either the
 * text of the hyperlink or the value of its HREF, if one was specified.
 * In the rare case of identical hyperlinks within the same string, the
 * HREF attribute can be used to distinguish between them.  The string may
 * include the mnemonic character and line delimiters. The only delimiter
 * the HREF attribute supports is the quotation mark ("). Text containing
 * angle-bracket characters &lt; or &gt; may be escaped using \\, however
 * this operation is a hint and varies from platform to platform.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic. The receiver can have a
 * mnemonic in the text preceding each link. When the user presses a
 * key sequence that matches the mnemonic, focus is assigned
 * to the link that follows the text. Mnemonics in links and in
 * the trailing text are ignored. On most platforms,
 * the mnemonic appears underlined but may be emphasised in a
 * platform specific manner.  The mnemonic indicator character
 * '&amp;' can be escaped by doubling it in the string, causing
 * a single '&amp;' to be displayed.
 * </p>
 *
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (string.equals (text)) return;
	text = string;
	if ((state & HAS_AUTO_DIRECTION) != 0) {
		updateTextDirection (AUTO_TEXT_DIRECTION);
	}
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	OS.SetWindowText (handle, buffer);
	parse(string);
}

@Override
int resolveTextDirection() {
	return BidiUtil.resolveTextDirection(text);
}

@Override
boolean updateTextDirection(int textDirection) {
	if (super.updateTextDirection(textDirection)) {
		int flags = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
		style &= ~SWT.MIRRORED;
		style &= ~flags;
		style |= textDirection & flags;
		updateOrientation ();
		checkMirrored ();
		return true;
	}
	return false;
}

@Override
int widgetStyle () {
	int bits = super.widgetStyle ();
	return bits | OS.WS_TABSTOP;
}

@Override
TCHAR windowClass () {
	return LinkClass;
}

@Override
long windowProc () {
	return LinkProc;
}

@Override
LRESULT WM_CHAR (long wParam, long lParam) {
	LRESULT result = super.WM_CHAR (wParam, lParam);
	if (result != null) return result;
	switch ((int)wParam) {
		case SWT.SPACE:
		case SWT.CR:
		case SWT.TAB:
			/*
			* NOTE: Call the window proc with WM_KEYDOWN rather than WM_CHAR
			* so that the key that was ignored during WM_KEYDOWN is processed.
			* This allows the application to cancel an operation that is normally
			* performed in WM_KEYDOWN from WM_CHAR.
			*/
			long code = callWindowProc (handle, OS.WM_KEYDOWN, wParam, lParam);
			return new LRESULT (code);
	}
	return result;
}

@Override
LRESULT WM_GETDLGCODE (long wParam, long lParam) {
	long code = callWindowProc (handle, OS.WM_GETDLGCODE, wParam, lParam);
	int count = ids.length;
	if (count == 0) {
		code |= OS.DLGC_STATIC;
	} else if (count > 1) {
		int limit = (OS.GetKeyState (OS.VK_SHIFT) < 0) ? 0 : count - 1;
		if (getFocusItem() != limit) {
			code |= OS.DLGC_WANTTAB;
		}
	}
	return new LRESULT (code);
}

@Override
LRESULT WM_KEYDOWN (long wParam, long lParam) {
	LRESULT result = super.WM_KEYDOWN (wParam, lParam);
	if (result != null) return result;
	switch ((int)wParam) {
		case OS.VK_SPACE:
		case OS.VK_RETURN:
		case OS.VK_TAB:
			/*
			* Ensure that the window proc does not process VK_SPACE,
			* VK_RETURN or VK_TAB so that it can be handled in WM_CHAR.
			* This allows the application to cancel an operation that
			* is normally performed in WM_KEYDOWN from WM_CHAR.
			*/
			return LRESULT.ZERO;
	}
	return result;
}

@Override
LRESULT WM_KILLFOCUS (long wParam, long lParam) {
	nextFocusItem = getFocusItem();
	return super.WM_KILLFOCUS(wParam, lParam);
}

@Override
LRESULT WM_NCHITTEST (long wParam, long lParam) {
	LRESULT result = super.WM_NCHITTEST (wParam, lParam);
	if (result != null) return result;

	/*
	* Feature in Windows. For WM_NCHITTEST, the Syslink window proc
	* returns HTTRANSPARENT when mouse is over plain text. As a result,
	* mouse events are not delivered. The fix is to always return HTCLIENT.
	*/
	return new LRESULT (OS.HTCLIENT);
}

@Override
LRESULT WM_SETCURSOR(long wParam, long lParam) {
	LRESULT result = super.WM_SETCURSOR (wParam, lParam);
	if (result != null) return result;
	long fDone = callWindowProc (handle, OS.WM_SETCURSOR, wParam, lParam);
	/* Take responsibility for cursor over plain text after overriding WM_NCHITTEST. */
	if (fDone == 0) OS.DefWindowProc (handle, OS.WM_SETCURSOR, wParam, lParam);
	return LRESULT.ONE;
}

@Override
LRESULT WM_SETFOCUS (long wParam, long lParam) {
	/*
	* Feature in Windows. Upon receiving focus, SysLink control
	* always activates the first link. This leads to surprising
	* behavior in multi-link controls.
	*/
	if (ids.length > 1) {
		if (OS.GetKeyState (OS.VK_TAB) < 0) {
			if (OS.GetKeyState (OS.VK_SHIFT) < 0) {
				// reverse tab; focus on last item
				setFocusItem(ids.length - 1);
			}
		} else if (nextFocusItem > 0) {
			setFocusItem(nextFocusItem);
		}
	}
	return super.WM_SETFOCUS (wParam, lParam);
}

@Override
LRESULT wmNotifyChild (NMHDR hdr, long wParam, long lParam) {
	switch (hdr.code) {
		case OS.NM_RETURN:
		case OS.NM_CLICK:
			NMLINK item = new NMLINK ();
			OS.MoveMemory (item, lParam, NMLINK.sizeof);
			Event event = new Event ();
			event.text = ids [item.iLink];
			sendSelectionEvent (SWT.Selection, event, true);
			break;
		case OS.NM_CUSTOMDRAW:
			NMCUSTOMDRAW nmcd = new NMCUSTOMDRAW ();
			OS.MoveMemory (nmcd, lParam, NMCUSTOMDRAW.sizeof);
			switch (nmcd.dwDrawStage) {
				case OS.CDDS_PREPAINT:
					if (!OS.IsWindowEnabled (handle) || linkForeground != -1) {
						return new LRESULT(OS.CDRF_NOTIFYITEMDRAW);
					}
					break;
				case OS.CDDS_ITEMPREPAINT:
					/*
					 * Feature in Windows.  SysLink32 control doesn't natively
					 * provide disabled state. Emulate it with custom draw.
					 */
					if (!OS.IsWindowEnabled (handle)) {
						OS.SetTextColor (nmcd.hdc, OS.GetSysColor (OS.COLOR_GRAYTEXT));
					}
					else if (linkForeground != -1 && nmcd.dwItemSpec != -1) {
						OS.SetTextColor(nmcd.hdc, linkForeground);
					}
					break;
			}
			break;
	}
	return super.wmNotifyChild (hdr, wParam, lParam);
}
}
