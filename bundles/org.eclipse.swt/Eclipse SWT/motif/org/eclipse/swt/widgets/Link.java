/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

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
	TextLayout layout;
	Color linkColor, disabledColor;
	Point [] offsets;
	Point selection;
	String [] ids;
	int [] mnemonics;
	int focusIndex;
	
	static final RGB LINK_FOREGROUND = new RGB (0, 51, 153);
	static final RGB LINK_DISABLED_FOREGROUND = new RGB (172, 168, 153);
	
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
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	int width, height;
	int layoutWidth = layout.getWidth ();
	//TEMPORARY CODE
	if (wHint == 0) {
		layout.setWidth (1);
		Rectangle rect = layout.getBounds ();
		width = 0;
		height = rect.height;
	} else {
		layout.setWidth (wHint);
		Rectangle rect = layout.getBounds ();
		width = rect.width;
		height = rect.height;
	}
	layout.setWidth (layoutWidth);		
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int border = getBorderWidth ();
	width += border * 2;
	height += border * 2;
	return new Point (width, height);
}
void createHandle (int index) {
	state |= THEME_BACKGROUND;
	int [] argList = {
		OS.XmNancestorSensitive, 1,
		OS.XmNborderWidth, (style & SWT.BORDER) != 0 ? 1 : 0,
		OS.XmNmarginWidth, 0,
		OS.XmNmarginHeight, 0,
		OS.XmNresizePolicy, OS.XmRESIZE_NONE,
		OS.XmNtraversalOn, 0,
	};
	handle = OS.XmCreateDrawingArea (parent.handle, null, argList, argList.length / 2);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	layout = new TextLayout (display);
	linkColor = new Color (display, LINK_FOREGROUND);
	disabledColor = new Color (display, LINK_DISABLED_FOREGROUND);
	offsets = new Point [0];
	ids = new String [0];
	mnemonics = new int [0];
	selection = new Point (-1, -1);
	focusIndex = -1;
}
void createWidget (int index) {
	super.createWidget (index);	
	text = "";
	//TODO - accessibility
}
void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	TextStyle linkStyle = new TextStyle (null, enabled ? linkColor : disabledColor, null);
	linkStyle.underline = true;
	for (int i = 0; i < offsets.length; i++) {
		Point point = offsets [i];
		layout.setStyle (linkStyle, point.x, point.y);
	}
	redraw ();
}
String getNameText () {
	return getText ();
}
Rectangle [] getRectangles (int linkIndex) {
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
		rects [index++] = layout.getBounds (point.x, point.y);		
	} else {
		rects [index++] = layout.getBounds (point.x, lineOffsets [lineStart]-1);
		rects [index++] = layout.getBounds (lineOffsets [lineEnd-1], point.y);
		if (lineEnd - lineStart > 1) {
			for (int i = lineStart; i < lineEnd - 1; i++) {
				rects [index++] = layout.getLineBounds (i);
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
void releaseWidget () {
	super.releaseWidget ();
	if (layout != null) layout.dispose ();
	layout = null;
	if (linkColor != null) linkColor.dispose ();
	linkColor = null;
	if (disabledColor != null) disabledColor.dispose ();
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
boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	boolean changed = super.setBounds (x, y, width,height, move, resize);
	if (changed && resize) {
		layout.setWidth (width > 0 ? width : -1);
		redraw ();
	}
	return changed;
}
public void setFont (Font font) {
	super.setFont (font);
	layout.setFont (this.font);
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return;
	OS.XClearArea (xDisplay, xWindow, 0, 0, 0, 0, true);
}
void setForegroundPixel (int pixel) {
	super.setForegroundPixel (pixel);
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return;
	OS.XClearArea (xDisplay, xWindow, 0, 0, 0, 0, true);
}
/**
 * Sets the receiver's text.
 * <p>
 * The string can contain both regular text and hyperlinks.  A hyperlink
 * is delimited by an anchor tag, &lt;A&gt; and &lt;/A&gt;.  Within an
 * anchor, a single HREF attribute is supported.  When a hyperlink is
 * selected, the text field of the selection event contains either the
 * text of the hyperlink or the value of its HREF, if one was specified.
 * In the rare case of identical hyperlinks within the same string, the
 * HREF attribute can be used to distinguish between them.  The string may
 * include the mnemonic character and line delimiters. The only delimiter
 * the HREF attribute supports is the quotation mark (").
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
	layout.setText (parse (string));
	focusIndex = offsets.length > 0 ? 0 : -1;	
	selection.x = selection.y = -1;
	int [] argList = new int [] {OS.XmNtraversalOn, offsets.length > 0 ? 1 : 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
	int [] argList1 = {OS.XmNsensitive, 0};
	OS.XtGetValues (handle, argList1, argList1.length / 2);
	boolean enabled = argList1 [1] != 0;
	TextStyle linkStyle = new TextStyle (null, enabled ? linkColor : disabledColor, null);
	linkStyle.underline = true;
	for (int i = 0; i < offsets.length; i++) {
		Point point = offsets [i];
		layout.setStyle (linkStyle, point.x, point.y);
	}
	TextStyle mnemonicStyle = new TextStyle (null, null, null);
	mnemonicStyle.underline = true;
	for (int i = 0; i < mnemonics.length; i++) {
		int mnemonic  = mnemonics [i];
		if (mnemonic != -1) {
			layout.setStyle (mnemonicStyle, mnemonic, mnemonic);
		}
	}
	redraw ();
}
int traversalCode (int key, XKeyEvent event) {
	if (offsets.length == 0) return 0;
	int bits = super.traversalCode (key, event);
	if (event != null && key == OS.XK_Tab) {
		boolean next = (event.state & OS.ShiftMask) == 0;
		if (next && focusIndex < offsets.length - 1) {
			return bits & ~SWT.TRAVERSE_TAB_NEXT;
		}
		if (!next && focusIndex > 0) {
			return bits & ~SWT.TRAVERSE_TAB_PREVIOUS;
		}
	}
	return bits;
}
int XButtonPress (int w, int client_data, int call_data, int continue_to_dispatch) {
	int result = super.XButtonPress (w, client_data, call_data, continue_to_dispatch);
	if (result != 0) return result;
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, call_data, XButtonEvent.sizeof);
	if (xEvent.button == 1) {
		int offset = layout.getOffset (xEvent.x, xEvent.y, null);
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
			Rectangle rect = layout.getBounds (oldSelectionX, oldSelectionY);
			redraw (rect.x, rect.y, rect.width, rect.height, false);
		}
		for (int j = 0; j < offsets.length; j++) {
			Rectangle [] rects = getRectangles (j);
			for (int i = 0; i < rects.length; i++) {
				Rectangle rect = rects [i];
				if (rect.contains (xEvent.x, xEvent.y)) {
					focusIndex = j;						
					redraw ();
					return result;
				}
			}
		}
	}
	return result;
}
int XButtonRelease (int w, int client_data, int call_data, int continue_to_dispatch) {
	int result = super.XButtonRelease (w, client_data, call_data, continue_to_dispatch);
	if (result != 0) return result;
	if (focusIndex == -1) return result;
	XButtonEvent xEvent = new XButtonEvent ();
	OS.memmove (xEvent, call_data, XButtonEvent.sizeof);
	if (xEvent.button == 1) {
		Rectangle [] rects = getRectangles (focusIndex);
		for (int i = 0; i < rects.length; i++) {
			Rectangle rect = rects [i];
			if (rect.contains (xEvent.x, xEvent.y)) {
				Event event = new Event ();
				event.text = ids [focusIndex];
				notifyListeners (SWT.Selection, event);
				return result;
			}
		}
	}
	return result;
}
int XExposure (int w, int client_data, int call_data, int continue_to_dispatch) {
	XExposeEvent xEvent = new XExposeEvent ();
	OS.memmove (xEvent, call_data, XExposeEvent.sizeof);
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return 0;
	int damageRgn = OS.XCreateRegion ();
	OS.XtAddExposureToRegion (call_data, damageRgn);
	GCData data = new GCData ();
	data.damageRgn = damageRgn;
	GC gc = GC.motif_new (this, data);
	OS.XSetRegion (xDisplay, gc.handle, damageRgn);
	int selStart = selection.x;
	int selEnd = selection.y;
	if (selStart > selEnd) {
		selStart = selection.y;
		selEnd = selection.x;
	}
	// temporary code to disable text selection
	selStart = selEnd = -1;
	int [] argList = {OS.XmNsensitive, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (argList [1] == 0) gc.setForeground (disabledColor);	
	layout.draw (gc, 0, 0, selStart, selEnd, null, null);
	if (hasFocus () && focusIndex != -1) {
		Rectangle [] rects = getRectangles (focusIndex);
		for (int i = 0; i < rects.length; i++) {
			Rectangle rect = rects [i];
			gc.drawFocus (rect.x, rect.y, rect.width, rect.height);					
		}
	}
	gc.dispose ();
	OS.XDestroyRegion (damageRgn);
	return super.XExposure (w, client_data, call_data, continue_to_dispatch);
}
int XFocusChange (int w, int client_data, int call_data, int continue_to_dispatch) {
	int result = super.XFocusChange (w, client_data, call_data, continue_to_dispatch);
	redraw ();
	return result;
}
int XKeyPress (int w, int client_data, int call_data, int continue_to_dispatch) {
	int result = super.XKeyPress (w, client_data, call_data, continue_to_dispatch);
	if (result != 0) return result;
	if (focusIndex == -1) return result;
	XKeyEvent xEvent = new XKeyEvent ();
	OS.memmove (xEvent, call_data, XKeyEvent.sizeof);
	int [] keysym = new int [1];
	OS.XLookupString (xEvent, null, 0, keysym, null);
	keysym [0] &= 0xFFFF;
	switch (keysym [0]) {
		case OS.XK_space:
		case OS.XK_Return:
		case OS.XK_KP_Enter:
			Event event = new Event ();
			event.text = ids [focusIndex];
			sendEvent (SWT.Selection, event);
			break;
		case OS.XK_Tab:
			if (focusIndex < offsets.length - 1) {
				focusIndex++;
				redraw ();
				OS.memmove (continue_to_dispatch, new int [1], 4);
				return 1;
			}
			break;
		case OS.XK_ISO_Left_Tab:
			if (focusIndex > 0) {
				focusIndex--;
				redraw ();
				OS.memmove (continue_to_dispatch, new int [1], 4);
				return 1;
			}
			break;
	}
	return result;
}
int XPointerMotion (int w, int client_data, int call_data, int continue_to_dispatch) {
	int result = super.XPointerMotion (w, client_data, call_data, continue_to_dispatch);
	if (result != 0) return result;
	XMotionEvent xEvent = new XMotionEvent ();
	OS.memmove (xEvent, call_data, XMotionEvent.sizeof);
	if ((xEvent.state & OS.Button1Mask) != 0) {
		int oldSelection = selection.y;
		selection.y = layout.getOffset (xEvent.x, xEvent.y, null);
		if (selection.y != oldSelection) {
			int newSelection = selection.y;
			if (oldSelection > newSelection) {
				int temp = oldSelection;
				oldSelection = newSelection;
				newSelection = temp;
			}
			Rectangle rect = layout.getBounds (oldSelection, newSelection);
			redraw (rect.x, rect.y, rect.width, rect.height, false);
		}
		return result;
	}
	for (int j = 0; j < offsets.length; j++) {
		Rectangle [] rects = getRectangles (j);
		for (int i = 0; i < rects.length; i++) {
			Rectangle rect = rects [i];
			if (rect.contains (xEvent.x, xEvent.y)) {
				setCursor (display.getSystemCursor (SWT.CURSOR_HAND));
				return result;
			}
		}
	}
	setCursor (null);
	return result;
}
}
