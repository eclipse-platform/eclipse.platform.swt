/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Conrad Groth - Bug 401015 - [CSS] Add support for styling hyperlinks in Links
 *******************************************************************************/
package org.eclipse.swt.widgets;

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
 * <p>
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
	TextLayout layout; // always track the current layout, even if we don't need it.
	Color disabledColor;
	int linkForeground = -1;
	Point [] offsets;
	Point selection;
	String [] ids;
	int [] mnemonics;
	int focusIndex, mouseDownIndex;
	long /*int*/ font;
	static final RGB LAST_FALLBACK_LINK_FOREGROUND = new RGB (0, 51, 153);
	static final long /*int*/ LinkProc;
	static final TCHAR LinkClass = new TCHAR (0, OS.WC_LINK, true);
	static {
		if (isCommonControlAvailable()) {
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
			long /*int*/ hInstance = OS.GetModuleHandle (null);
			long /*int*/ hHeap = OS.GetProcessHeap ();
			lpWndClass.hInstance = hInstance;
			lpWndClass.style &= ~OS.CS_GLOBALCLASS;
			lpWndClass.style |= OS.CS_DBLCLKS;
			int byteCount = LinkClass.length () * TCHAR.sizeof;
			long /*int*/ lpszClassName = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
			OS.MoveMemory (lpszClassName, LinkClass, byteCount);
			lpWndClass.lpszClassName = lpszClassName;
			OS.RegisterClass (lpWndClass);
			OS.HeapFree (hHeap, 0, lpszClassName);
		} else {
			LinkProc = 0;
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
long /*int*/ callWindowProc (long /*int*/ hwnd, int msg, long /*int*/ wParam, long /*int*/ lParam) {
	if (handle == 0) return 0;
	if (LinkProc != 0) {
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
	return OS.DefWindowProc (hwnd, msg, wParam, lParam);
}

@Override Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	int width, height;
	if (useCommonControl()) {
		long /*int*/ hDC = OS.GetDC (handle);
		long /*int*/ newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
		long /*int*/ oldFont = OS.SelectObject (hDC, newFont);
		if (text.length () > 0) {
			TCHAR buffer = new TCHAR (getCodePage (), parse (text), false);
			RECT rect = new RECT ();
			int flags = OS.DT_CALCRECT | OS.DT_NOPREFIX;
			if (wHint != SWT.DEFAULT) {
				flags |= OS.DT_WORDBREAK;
				rect.right = wHint;
			}
			OS.DrawText (hDC, buffer, buffer.length (), rect, flags);
			width = rect.right - rect.left;
			height = rect.bottom;
		} else {
			TEXTMETRIC lptm = OS.IsUnicode ? (TEXTMETRIC)new TEXTMETRICW () : new TEXTMETRICA ();
			OS.GetTextMetrics (hDC, lptm);
			width = 0;
			height = lptm.tmHeight;
		}
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (handle, hDC);
	} else {
		int layoutWidth = layout.getWidth ();
		//TEMPORARY CODE
		if (wHint == 0) {
			layout.setWidth (1);
			Rectangle rect = DPIUtil.autoScaleUp(layout.getBounds ());
			width = 0;
			height = rect.height;
		} else {
			layout.setWidth (DPIUtil.autoScaleDown(wHint));
			Rectangle rect = DPIUtil.autoScaleUp(layout.getBounds ());
			width = rect.width;
			height = rect.height;
		}
		layout.setWidth (layoutWidth);
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
	layout = new TextLayout (display);
	disabledColor = Color.win32_new (display, OS.GetSysColor (OS.COLOR_GRAYTEXT));
	offsets = new Point [0];
	ids = new String [0];
	mnemonics = new int [0];
	selection = new Point (-1, -1);
	focusIndex = mouseDownIndex = -1;
}

@Override
void createWidget () {
	super.createWidget ();
	text = "";
	if ((style & SWT.MIRRORED) != 0) {
		layout.setOrientation (SWT.RIGHT_TO_LEFT);
	}
	initAccessible ();
}

void drawWidget (GC gc, RECT rect) {
	drawBackground (gc.handle, rect);
	int selStart = selection.x;
	int selEnd = selection.y;
	if (selStart > selEnd) {
		selStart = selection.y;
		selEnd = selection.x;
	}
	// temporary code to disable text selection
	selStart = selEnd = -1;
	if (!OS.IsWindowEnabled (handle)) gc.setForeground (disabledColor);
	layout.draw (gc, 0, 0, selStart, selEnd, null, null);
	if (hasFocus () && focusIndex != -1) {
		Rectangle [] rects = getRectanglesInPixels (focusIndex);
		for (int i = 0; i < rects.length; i++) {
			Rectangle rectangle = DPIUtil.autoScaleDown(rects [i]);
			gc.drawFocus (rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		}
	}
	if (hooks (SWT.Paint) || filters (SWT.Paint)) {
		Event event = new Event ();
		event.gc = gc;
		event.setBoundsInPixels(new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top));
		sendEvent (SWT.Paint, event);
		event.gc = null;
	}
}

@Override
void enableWidget (boolean enabled) {
	if (useCommonControl(enabled)) {
		LITEM item = new LITEM ();
		item.mask = OS.LIF_ITEMINDEX | OS.LIF_STATE;
		item.stateMask = OS.LIS_ENABLED;
		item.state = enabled ? OS.LIS_ENABLED : 0;
		while (OS.SendMessage (handle, OS.LM_SETITEM, 0, item) != 0) {
			item.iLink++;
		}
	}
	styleLinkParts(enabled);
	redraw ();
	/*
	* Feature in Windows.  For some reason, setting
	* LIS_ENABLED state using LM_SETITEM causes the
	* SysLink to become enabled.  To be specific,
	* calling IsWindowEnabled() returns true.  The
	* fix is disable the SysLink after LM_SETITEM.
	*/
	super.enableWidget (enabled);
}

void initAccessible () {
	Accessible accessible = getAccessible ();
	accessible.addAccessibleListener (new AccessibleAdapter () {
		@Override
		public void getName (AccessibleEvent e) {
			e.result = parse (text);
		}
	});

	accessible.addAccessibleControlListener (new AccessibleControlAdapter () {
		@Override
		public void getChildAtPoint (AccessibleControlEvent e) {
			e.childID = ACC.CHILDID_SELF;
		}

		@Override
		public void getLocation (AccessibleControlEvent e) {
			Rectangle rect = display.mapInPixels (getParent (), null, getBoundsInPixels ());
			e.x = rect.x;
			e.y = rect.y;
			e.width = rect.width;
			e.height = rect.height;
		}

		@Override
		public void getChildCount (AccessibleControlEvent e) {
			e.detail = 0;
		}

		@Override
		public void getRole (AccessibleControlEvent e) {
			e.detail = ACC.ROLE_LINK;
		}

		@Override
		public void getState (AccessibleControlEvent e) {
			e.detail = ACC.STATE_FOCUSABLE;
			if (hasFocus ()) e.detail |= ACC.STATE_FOCUSED;
		}

		@Override
		public void getDefaultAction (AccessibleControlEvent e) {
			e.result = SWT.getMessage ("SWT_Press"); //$NON-NLS-1$
		}

		@Override
		public void getSelection (AccessibleControlEvent e) {
			if (hasFocus ()) e.childID = ACC.CHILDID_SELF;
		}

		@Override
		public void getFocus (AccessibleControlEvent e) {
			if (hasFocus ()) e.childID = ACC.CHILDID_SELF;
		}
	});
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
	return internalGetLinkForeground();
}

Color internalGetLinkForeground() {
	if (linkForeground != -1) {
		return Color.win32_new (display, linkForeground);
	}
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (4, 10)) {
		return Color.win32_new (display, OS.GetSysColor (OS.COLOR_HOTLIGHT));
	}
	// Create and cache the linkForeground Color native handle for re-use.
	Color color = new Color (display, LAST_FALLBACK_LINK_FOREGROUND);
	linkForeground = color.handle;
	return color;
}

@Override
String getNameText () {
	return getText ();
}

Rectangle [] getRectanglesInPixels (int linkIndex) {
	int lineCount = layout.getLineCount ();
	Rectangle [] rects = new Rectangle [lineCount];
	int [] lineOffsets = layout.getLineOffsets ();
	Point point = offsets [linkIndex];
	int lineStart = 1;
	while (point.x > lineOffsets [lineStart]) lineStart++;
	int lineEnd = 1;
	while (point.y > lineOffsets [lineEnd]) lineEnd++;
	int index = 0;
	if (lineStart == lineEnd) {
		rects [index++] = DPIUtil.autoScaleUp(layout.getBounds (point.x, point.y));
	} else {
		rects [index++] = DPIUtil.autoScaleUp(layout.getBounds (point.x, lineOffsets [lineStart]-1));
		rects [index++] = DPIUtil.autoScaleUp(layout.getBounds (lineOffsets [lineEnd-1], point.y));
		if (lineEnd - lineStart > 1) {
			for (int i = lineStart; i < lineEnd - 1; i++) {
				rects [index++] = DPIUtil.autoScaleUp(layout.getLineBounds (i));
			}
		}
	}
	if (rects.length != index) {
		Rectangle [] tmp = new Rectangle [index];
		System.arraycopy (rects, 0, tmp, 0, index);
		rects = tmp;
	}
	return rects;
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
	if (mnemonics != null) {
		char uckey = Character.toUpperCase (key);
		String parsedText = parse(text);
		for (int i = 0; i < mnemonics.length - 1; i++) {
			if (mnemonics[i] != -1) {
				char mnemonic = parsedText.charAt(mnemonics[i]);
				if (uckey == Character.toUpperCase (mnemonic)) {
					if (!setFocus ()) return false;
					if (useCommonControl()) {
						int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
						LITEM item = new LITEM ();
						item.mask = OS.LIF_ITEMINDEX | OS.LIF_STATE;
						item.stateMask = OS.LIS_FOCUSED;
						while (item.iLink < mnemonics.length) {
							if (item.iLink != i) OS.SendMessage (handle, OS.LM_SETITEM, 0, item);
							item.iLink++;
						}
						item.iLink = i;
						item.state = OS.LIS_FOCUSED;
						OS.SendMessage (handle, OS.LM_SETITEM, 0, item);

						/* Feature in Windows. For some reason, setting the focus to
						 * any item but first causes the control to clear the WS_TABSTOP
						 * bit. The fix is always to reset the bit.
						 */
						OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
					} else {
						focusIndex = i;
						redraw ();
					}
					return  true;
				}
			}
		}
	}
	return false;
}

@Override
boolean mnemonicMatch (char key) {
	if (mnemonics != null) {
		char uckey = Character.toUpperCase (key);
		String parsedText = parse(text);
		for (int i = 0; i < mnemonics.length - 1; i++) {
			if (mnemonics[i] != -1) {
				char mnemonic = parsedText.charAt(mnemonics[i]);
				if (uckey == Character.toUpperCase (mnemonic)) {
					return true;
				}
			}
		}
	}
	return false;
}

String parse (String string) {
	int length = string.length ();
	offsets = new Point [length / 4];
	ids = new String [length / 4];
	mnemonics = new int [length / 4 + 1];
	StringBuffer result = new StringBuffer ();
	char [] buffer = new char [length];
	string.getChars (0, string.length (), buffer, 0);
	int index = 0, state = 0, linkIndex = 0;
	int start = 0, tagStart = 0, linkStart = 0, endtagStart = 0, refStart = 0;
	while (index < length) {
		char c = Character.toLowerCase (buffer [index]);
		switch (state) {
			case 0:
				if (c == '<') {
					tagStart = index;
					state++;
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
					endtagStart = index;
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
					mnemonics [linkIndex] = parseMnemonics (buffer, start, tagStart, result);
					int offset = result.length ();
					parseMnemonics (buffer, linkStart, endtagStart, result);
					offsets [linkIndex] = new Point (offset, result.length () - 1);
					if (ids [linkIndex] == null) {
						ids [linkIndex] = new String (buffer, linkStart, endtagStart - linkStart);
					}
					linkIndex++;
					start = tagStart = linkStart = endtagStart = refStart = index + 1;
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
					ids[linkIndex] = new String (buffer, refStart, index - refStart);
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
			default:
				state = 0;
				break;
		}
		index++;
	}
	if (start < length) {
		int tmp = parseMnemonics (buffer, start, tagStart, result);
		int mnemonic = parseMnemonics (buffer, Math.max (tagStart, linkStart), length, result);
		if (mnemonic == -1) mnemonic = tmp;
		mnemonics [linkIndex] = mnemonic;
	} else {
		mnemonics [linkIndex] = -1;
	}
	if (offsets.length != linkIndex) {
		Point [] newOffsets = new Point [linkIndex];
		System.arraycopy (offsets, 0, newOffsets, 0, linkIndex);
		offsets = newOffsets;
		String [] newIDs = new String [linkIndex];
		System.arraycopy (ids, 0, newIDs, 0, linkIndex);
		ids = newIDs;
		int [] newMnemonics = new int [linkIndex + 1];
		System.arraycopy (mnemonics, 0, newMnemonics, 0, linkIndex + 1);
		mnemonics = newMnemonics;
	}
	return result.toString ();
}

int parseMnemonics (char[] buffer, int start, int end, StringBuffer result) {
	int mnemonic = -1, index = start;
	while (index < end) {
		if (buffer [index] == '&') {
			if (index + 1 < end && buffer [index + 1] == '&') {
				result.append (buffer [index]);
				index++;
			} else {
				mnemonic = result.length();
			}
		} else {
			result.append (buffer [index]);
		}
		index++;
	}
	return mnemonic;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	if (layout != null) layout.dispose ();
	layout = null;
	disabledColor = null;
	offsets = null;
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
	if (OS.IsWindowEnabled (handle)) {
		styleLinkParts(true);
	}
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
 * the HREF attribute supports is the quotation mark (").
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

	boolean enabled = OS.IsWindowEnabled (handle);
	String parsedText = parse (text);
	if (isCommonControlAvailable()) {
		/*
		* Bug in Windows.  For some reason, when SetWindowText()
		* is used to set the text of a link control to the empty
		* string, the old text remains.  The fix is to set the
		* text to a space instead.
		*/
		if (string.length () == 0) string = " ";  //$NON-NLS-1$
		TCHAR buffer = new TCHAR (getCodePage (), string, true);
		OS.SetWindowText (handle, buffer);
	}

	layout.setText (parsedText);
	focusIndex = offsets.length > 0 ? 0 : -1;
	selection.x = selection.y = -1;
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if (offsets.length > 0) {
		bits |= OS.WS_TABSTOP;
	} else {
		bits &= ~OS.WS_TABSTOP;
	}
	OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
	styleLinkParts(enabled);
	TextStyle mnemonicStyle = new TextStyle (null, null, null);
	mnemonicStyle.underline = true;
	for (int i = 0; i < mnemonics.length; i++) {
		int mnemonic  = mnemonics [i];
		if (mnemonic != -1) {
			layout.setStyle (mnemonicStyle, mnemonic, mnemonic);
		}
	}

	if (useCommonControl()) {
		enableWidget (enabled);
	} else {
		redraw ();
	}
}

@Override
int resolveTextDirection() {
	return BidiUtil.resolveTextDirection(text);
}

void styleLinkParts(boolean enabled) {
	TextStyle linkStyle = new TextStyle (null, enabled ? internalGetLinkForeground() : disabledColor, null);
	linkStyle.underline = true;
	for (int i = 0; i < offsets.length; i++) {
		Point point = offsets [i];
		layout.setStyle (linkStyle, point.x, point.y);
	}
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

boolean useCommonControl() {
	return useCommonControl(OS.IsWindowEnabled(handle));
}

boolean useCommonControl(boolean enabled) {
	return linkForeground == -1 && !enabled && isCommonControlAvailable();
}

static boolean isCommonControlAvailable() {
	return OS.COMCTL32_MAJOR >= 6;
}

@Override
int widgetStyle () {
	int bits = super.widgetStyle ();
	return bits | OS.WS_TABSTOP;
}

@Override
TCHAR windowClass () {
	return isCommonControlAvailable() ? LinkClass : display.windowClass;
}

@Override
long /*int*/ windowProc () {
	return LinkProc != 0 ? LinkProc : display.windowProc;
}

@Override
LRESULT WM_CHAR (long /*int*/ wParam, long /*int*/ lParam) {
	LRESULT result = super.WM_CHAR (wParam, lParam);
	if (result != null) return result;
	if (!useCommonControl()) {
		if (focusIndex == -1) return result;
		switch ((int)/*64*/wParam) {
			case ' ':
			case SWT.CR:
				Event event = new Event ();
				event.text = ids [focusIndex];
				sendSelectionEvent (SWT.Selection, event, true);
				break;
			case SWT.TAB:
				boolean next = OS.GetKeyState (OS.VK_SHIFT) >= 0;
				if (next) {
					if (focusIndex < offsets.length - 1) {
						focusIndex++;
						redraw ();
					}
				} else {
					if (focusIndex > 0) {
						focusIndex--;
						redraw ();
					}
				}
				break;
		}
	} else {
		switch ((int)/*64*/wParam) {
			case ' ':
			case SWT.CR:
			case SWT.TAB:
				/*
				* NOTE: Call the window proc with WM_KEYDOWN rather than WM_CHAR
				* so that the key that was ignored during WM_KEYDOWN is processed.
				* This allows the application to cancel an operation that is normally
				* performed in WM_KEYDOWN from WM_CHAR.
				*/
				long /*int*/ code = callWindowProc (handle, OS.WM_KEYDOWN, wParam, lParam);
				return new LRESULT (code);
		}

	}
	return result;
}

@Override
LRESULT WM_GETDLGCODE (long /*int*/ wParam, long /*int*/ lParam) {
	LRESULT result = super.WM_GETDLGCODE (wParam, lParam);
	if (result != null) return result;
	int index, count;
	long /*int*/ code = 0;
	if (useCommonControl()) {
		LITEM item = new LITEM ();
		item.mask = OS.LIF_ITEMINDEX | OS.LIF_STATE;
		item.stateMask = OS.LIS_FOCUSED;
		index = 0;
		while (OS.SendMessage (handle, OS.LM_GETITEM, 0, item) != 0) {
			if ((item.state & OS.LIS_FOCUSED) != 0) {
				index = item.iLink;
			}
			item.iLink++;
		}
		count = item.iLink;
		code = callWindowProc (handle, OS.WM_GETDLGCODE, wParam, lParam);
	} else {
		index = focusIndex;
		count = offsets.length;
	}
	if (count == 0) {
		return new LRESULT (code | OS.DLGC_STATIC);
	}
	boolean next = OS.GetKeyState (OS.VK_SHIFT) >= 0;
	if (next && index < count - 1) {
		return new LRESULT (code | OS.DLGC_WANTTAB);
	}
	if (!next && index > 0) {
		return new LRESULT (code | OS.DLGC_WANTTAB);
	}
	return result;
}

@Override
LRESULT WM_GETFONT (long /*int*/ wParam, long /*int*/ lParam) {
	LRESULT result = super.WM_GETFONT (wParam, lParam);
	if (result != null) return result;
	long /*int*/ code = callWindowProc (handle, OS.WM_GETFONT, wParam, lParam);
	if (code != 0) return new LRESULT (code);
	if (font == 0) font = defaultFont ();
	return new LRESULT (font);
}

@Override
LRESULT WM_KEYDOWN (long /*int*/ wParam, long /*int*/ lParam) {
	LRESULT result = super.WM_KEYDOWN (wParam, lParam);
	if (result != null) return result;
	if (useCommonControl()) {
		switch ((int)/*64*/wParam) {
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
	}
	return result;
}

@Override
LRESULT WM_KILLFOCUS (long /*int*/ wParam, long /*int*/ lParam) {
	LRESULT result = super.WM_KILLFOCUS (wParam, lParam);
	if (!useCommonControl()) redraw ();
	return result;
}

@Override
LRESULT WM_LBUTTONDOWN (long /*int*/ wParam, long /*int*/ lParam) {
	LRESULT result = super.WM_LBUTTONDOWN (wParam, lParam);
	if (result == LRESULT.ZERO) return result;
	if (!useCommonControl()) {
		if (focusIndex != -1) setFocus ();
		int x = OS.GET_X_LPARAM (lParam);
		int y = OS.GET_Y_LPARAM (lParam);
		int offset = layout.getOffset (DPIUtil.autoScaleDown(x), DPIUtil.autoScaleDown(y), null);
		int oldSelectionX = selection.x;
		int oldSelectionY = selection.y;
		selection.x = offset;
		selection.y = -1;
		if (oldSelectionX != -1 && oldSelectionY != -1) {
			if (oldSelectionX > oldSelectionY) {
				int temp = oldSelectionX;
				oldSelectionX = oldSelectionY;
				oldSelectionY = temp;
			}
			Rectangle rect = DPIUtil.autoScaleUp(layout.getBounds (oldSelectionX, oldSelectionY)); // To Pixels
			redrawInPixels (rect.x, rect.y, rect.width, rect.height, false);
		}
		for (int j = 0; j < offsets.length; j++) {
			Rectangle [] rects = getRectanglesInPixels (j);
			for (int i = 0; i < rects.length; i++) {
				Rectangle rect = rects [i];
				if (rect.contains (x, y)) {
					if (j != focusIndex) {
						redraw ();
					}
					focusIndex = mouseDownIndex = j;
					return result;
				}
			}
		}
	}
	return result;
}

@Override
LRESULT WM_LBUTTONUP (long /*int*/ wParam, long /*int*/ lParam) {
	LRESULT result = super.WM_LBUTTONUP (wParam, lParam);
	if (result == LRESULT.ZERO) return result;
	if (!useCommonControl()) {
		if (mouseDownIndex == -1) return result;
		int x = OS.GET_X_LPARAM (lParam);
		int y = OS.GET_Y_LPARAM (lParam);
		Rectangle [] rects = getRectanglesInPixels (mouseDownIndex);
		for (int i = 0; i < rects.length; i++) {
			Rectangle rect = rects [i];
			if (rect.contains (x, y)) {
				Event event = new Event ();
				event.text = ids [mouseDownIndex];
				sendSelectionEvent (SWT.Selection, event, true);
				break;
			}
		}
	}
	mouseDownIndex = -1;
	return result;
}

@Override
LRESULT WM_NCHITTEST (long /*int*/ wParam, long /*int*/ lParam) {
	LRESULT result = super.WM_NCHITTEST (wParam, lParam);
	if (result != null) return result;

	/*
	* Feature in Windows. For WM_NCHITTEST, the Syslink window proc
	* returns HTTRANSPARENT when mouse is over plain text. The fix is
	* to always return HTCLIENT.
	*/
	return new LRESULT (OS.HTCLIENT);
}

@Override
LRESULT WM_MOUSEMOVE (long /*int*/ wParam, long /*int*/ lParam) {
	LRESULT result = super.WM_MOUSEMOVE (wParam, lParam);
	if (!useCommonControl()) {
		int x = OS.GET_X_LPARAM (lParam);
		int y = OS.GET_Y_LPARAM (lParam);
		if (OS.GetKeyState (OS.VK_LBUTTON) < 0) {
			int oldSelection = selection.y;
			selection.y = layout.getOffset (DPIUtil.autoScaleDown(x), DPIUtil.autoScaleDown(y), null);
			if (selection.y != oldSelection) {
				int newSelection = selection.y;
				if (oldSelection > newSelection) {
					int temp = oldSelection;
					oldSelection = newSelection;
					newSelection = temp;
				}
				Rectangle rect = DPIUtil.autoScaleUp(layout.getBounds (oldSelection, newSelection));// To Pixels
				redrawInPixels (rect.x, rect.y, rect.width, rect.height, false);
			}
		} else {
			for (int j = 0; j < offsets.length; j++) {
				Rectangle [] rects = getRectanglesInPixels (j);
				for (int i = 0; i < rects.length; i++) {
					Rectangle rect = rects [i];
					if (rect.contains (x, y)) {
						setCursor (display.getSystemCursor (SWT.CURSOR_HAND));
						return result;
					}
				}
			}
			setCursor (null);
		}
	}
	return result;
}

@Override
LRESULT WM_PAINT (long /*int*/ wParam, long /*int*/ lParam) {
	if ((state & DISPOSE_SENT) != 0) return LRESULT.ZERO;
	if (useCommonControl()) {
		return super.WM_PAINT (wParam, lParam);
	}

	PAINTSTRUCT ps = new PAINTSTRUCT ();
	GCData data = new GCData ();
	data.ps = ps;
	data.hwnd = handle;
	GC gc = new_GC (data);
	if (gc != null) {
		int width = ps.right - ps.left;
		int height = ps.bottom - ps.top;
		if (width != 0 && height != 0) {
			RECT rect = new RECT ();
			OS.SetRect (rect, ps.left, ps.top, ps.right, ps.bottom);
			drawWidget (gc, rect);
		}
		gc.dispose ();
	}
	return LRESULT.ZERO;
}

@Override
LRESULT WM_PRINTCLIENT (long /*int*/ wParam, long /*int*/ lParam) {
	LRESULT result = super.WM_PRINTCLIENT (wParam, lParam);
	if (!useCommonControl()) {
		RECT rect = new RECT ();
		OS.GetClientRect (handle, rect);
		GCData data = new GCData ();
		data.device = display;
		data.foreground = getForegroundPixel ();
		GC gc = GC.win32_new (wParam, data);
		drawWidget (gc, rect);
		gc.dispose ();
	}
	return result;
}

@Override
LRESULT WM_SETFOCUS (long /*int*/ wParam, long /*int*/ lParam) {
	LRESULT result = super.WM_SETFOCUS (wParam, lParam);
	if (!useCommonControl()) redraw ();
	return result;
}

@Override
LRESULT WM_SETFONT (long /*int*/ wParam, long /*int*/ lParam) {
	layout.setFont (Font.win32_new (display, wParam));
	if (lParam != 0) OS.InvalidateRect (handle, null, true);
	return super.WM_SETFONT (font = wParam, lParam);
}

@Override
LRESULT WM_SIZE (long /*int*/ wParam, long /*int*/ lParam) {
	LRESULT result = super.WM_SIZE (wParam, lParam);
	RECT rect = new RECT ();
	OS.GetClientRect (handle, rect);
	layout.setWidth (DPIUtil.autoScaleDown(rect.right > 0 ? rect.right : -1));
	if (!useCommonControl()) {
		redraw ();
	}
	return result;
}

@Override
LRESULT wmColorChild (long /*int*/ wParam, long /*int*/ lParam) {
	LRESULT result = super.wmColorChild (wParam, lParam);
	/*
	* Feature in Windows.  When a SysLink is disabled, it does
	* not gray out the non-link portion of the text.  The fix
	* is to set the text color to the system gray color.
	*/
	if (useCommonControl()) {
		if (!OS.IsWindowEnabled (handle)) {
			OS.SetTextColor (wParam, OS.GetSysColor (OS.COLOR_GRAYTEXT));
			if (result == null) {
				int backPixel = getBackgroundPixel ();
				OS.SetBkColor (wParam, backPixel);
				long /*int*/ hBrush = findBrush (backPixel, OS.BS_SOLID);
				return new LRESULT (hBrush);
			}
		}
	}
	return result;
}

@Override
LRESULT wmNotifyChild (NMHDR hdr, long /*int*/ wParam, long /*int*/ lParam) {
	if (useCommonControl()) {
		switch (hdr.code) {
			case OS.NM_RETURN:
			case OS.NM_CLICK:
				NMLINK item = new NMLINK ();
				OS.MoveMemory (item, lParam, NMLINK.sizeof);
				Event event = new Event ();
				event.text = ids [item.iLink];
				sendSelectionEvent (SWT.Selection, event, true);
				break;
		}
	}
	return super.wmNotifyChild (hdr, wParam, lParam);
}
}
