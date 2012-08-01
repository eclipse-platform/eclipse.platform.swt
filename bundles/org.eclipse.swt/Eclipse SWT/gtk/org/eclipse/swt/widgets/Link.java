/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.accessibility.*;

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

void createHandle(int index) {
	state |= HANDLE | THEME_BACKGROUND;
	handle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	setHasWindow (handle, true);
	OS.GTK_WIDGET_SET_FLAGS (handle, OS.GTK_CAN_FOCUS);
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
	layout.setFont (getFont ());
	text = "";
	initAccessible ();
}

void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	if (isDisposed ()) return;
	TextStyle linkStyle = new TextStyle (null, enabled ? linkColor : disabledColor, null);
	linkStyle.underline = true;
	for (int i = 0; i < offsets.length; i++) {
		Point point = offsets [i];
		layout.setStyle (linkStyle, point.x, point.y);
	}
	redraw ();
}

void fixStyle () {
	fixStyle (handle);
}

void initAccessible () {
	Accessible accessible = getAccessible ();
	accessible.addAccessibleListener (new AccessibleAdapter () {
		public void getName (AccessibleEvent e) {
			e.result = parse (text);
		}
	});
		
	accessible.addAccessibleControlListener (new AccessibleControlAdapter () {
		public void getChildAtPoint (AccessibleControlEvent e) {
			e.childID = ACC.CHILDID_SELF;
		}
		
		public void getLocation (AccessibleControlEvent e) {
			Rectangle rect = display.map (getParent (), null, getBounds ());
			e.x = rect.x;
			e.y = rect.y;
			e.width = rect.width;
			e.height = rect.height;
		}
		
		public void getChildCount (AccessibleControlEvent e) {
			e.detail = 0;
		}
		
		public void getRole (AccessibleControlEvent e) {
			e.detail = ACC.ROLE_LINK;
		}
		
		public void getState (AccessibleControlEvent e) {
			e.detail = ACC.STATE_FOCUSABLE;
			if (hasFocus ()) e.detail |= ACC.STATE_FOCUSED;
		}
		
		public void getDefaultAction (AccessibleControlEvent e) {
			e.result = SWT.getMessage ("SWT_Press"); //$NON-NLS-1$
		}
		
		public void getSelection (AccessibleControlEvent e) {
			if (hasFocus ()) e.childID = ACC.CHILDID_SELF;
		}
		
		public void getFocus (AccessibleControlEvent e) {
			if (hasFocus ()) e.childID = ACC.CHILDID_SELF;
		}
	});
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

int getClientWidth () {
	return (state & ZERO_WIDTH) != 0 ? 0 : OS.GTK_WIDGET_WIDTH (handle);
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

int /*long*/ gtk_button_press_event (int /*long*/ widget, int /*long*/ event) {
	int /*long*/ result = super.gtk_button_press_event (widget, event);	
	if (result != 0) return result;
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, event, GdkEventButton.sizeof);
	if (gdkEvent.button == 1 && gdkEvent.type == OS.GDK_BUTTON_PRESS) {
		if (focusIndex != -1) setFocus ();
		int x = (int) gdkEvent.x;
		int y = (int) gdkEvent.y;
		if ((style & SWT.MIRRORED) != 0) x = getClientWidth () - x;
		int offset = layout.getOffset (x, y, null);
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
				if (rect.contains (x, y)) {
					focusIndex = j;						
					redraw ();
					return result;
				}
			}
		}
	}
	return result;
}

int /*long*/ gtk_button_release_event (int /*long*/ widget, int /*long*/ event) {
	int /*long*/ result = super.gtk_button_release_event (widget, event);
	if (result != 0) return result;
	if (focusIndex == -1) return result;
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, event, GdkEventButton.sizeof);
	if (gdkEvent.button == 1) {
		int x = (int) gdkEvent.x;
		int y = (int) gdkEvent.y;
		if ((style & SWT.MIRRORED) != 0) x = getClientWidth () - x;
		Rectangle [] rects = getRectangles (focusIndex);
		for (int i = 0; i < rects.length; i++) {
			Rectangle rect = rects [i];
			if (rect.contains (x, y)) {
				Event ev = new Event ();
				ev.text = ids [focusIndex];
				sendSelectionEvent (SWT.Selection, ev, true);
				return result;
			}
		}
	}
	return result;
}

int /*long*/ gtk_event_after (int /*long*/ widget, int /*long*/ gdkEvent) {
	int /*long*/ result = super.gtk_event_after (widget, gdkEvent);
	GdkEvent event = new GdkEvent ();
	OS.memmove (event, gdkEvent, GdkEvent.sizeof);
	switch (event.type) {
		case OS.GDK_FOCUS_CHANGE:
			redraw ();
			break;
	}
	return result;
}

int /*long*/ gtk_expose_event (int /*long*/ widget, int /*long*/ eventPtr) {
	if ((state & OBSCURED) != 0) return 0;
	GdkEventExpose gdkEvent = new GdkEventExpose ();
	OS.memmove (gdkEvent, eventPtr, GdkEventExpose.sizeof);
	GCData data = new GCData ();
	data.damageRgn = gdkEvent.region;
	GC gc = GC.gtk_new (this, data);
	int selStart = selection.x;
	int selEnd = selection.y;
	if (selStart > selEnd) {
		selStart = selection.y;
		selEnd = selection.x;
	}
	// temporary code to disable text selection
	selStart = selEnd = -1;
	if ((state & DISABLED) != 0) gc.setForeground (disabledColor);
	layout.draw (gc, 0, 0, selStart, selEnd, null, null);
	if (hasFocus () && focusIndex != -1) {
		Rectangle [] rects = getRectangles (focusIndex);
		for (int i = 0; i < rects.length; i++) {
			Rectangle rect = rects [i];
			gc.drawFocus (rect.x, rect.y, rect.width, rect.height);					
		}
	}
	if (hooks (SWT.Paint) || filters (SWT.Paint)) {
		Event event = new Event ();
		event.count = gdkEvent.count;
		event.x = gdkEvent.area_x;
		event.y = gdkEvent.area_y;
		event.width = gdkEvent.area_width;
		event.height = gdkEvent.area_height;
		if ((style & SWT.MIRRORED) != 0) event.x = getClientWidth () - event.width - event.x;
		event.gc = gc;
		sendEvent (SWT.Paint, event);
		event.gc = null;
	}
	gc.dispose ();
	return 0;
}

int /*long*/ gtk_key_press_event (int /*long*/ widget, int /*long*/ eventPtr) {
	int /*long*/ result = super.gtk_key_press_event (widget, eventPtr);
	if (result != 0) return result;
	if (focusIndex == -1) return result;
	GdkEventKey gdkEvent = new GdkEventKey ();
	OS.memmove (gdkEvent, eventPtr, GdkEventKey.sizeof);
	switch (gdkEvent.keyval) {
		case OS.GDK_Return:
		case OS.GDK_KP_Enter:
		case OS.GDK_space:
			Event event = new Event ();
			event.text = ids [focusIndex];
			sendSelectionEvent (SWT.Selection, event, true);
			break;
		case OS.GDK_Tab:
			if (focusIndex < offsets.length - 1) {
				focusIndex++;
				redraw ();
			}
			break;
		case OS.GDK_ISO_Left_Tab:
			if (focusIndex > 0) {
				focusIndex--;
				redraw ();
			}
			break;
	}
	return result;
}

int /*long*/ gtk_motion_notify_event (int /*long*/ widget, int /*long*/ event) {
	int /*long*/ result = super.gtk_motion_notify_event (widget, event);
	if (result != 0) return result;
	GdkEventMotion gdkEvent = new GdkEventMotion ();
	OS.memmove (gdkEvent, event, GdkEventMotion.sizeof);
	int x = (int) gdkEvent.x;
	int y = (int) gdkEvent.y;	
	if ((style & SWT.MIRRORED) != 0) x = getClientWidth () - x;
	if ((gdkEvent.state & OS.GDK_BUTTON1_MASK) != 0) {
		int oldSelection = selection.y;
		selection.y = layout.getOffset (x, y, null);
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
	} else {
		for (int j = 0; j < offsets.length; j++) {
			Rectangle [] rects = getRectangles (j);
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
	return result;
}

boolean mnemonicHit (char key) {
	char uckey = Character.toUpperCase (key);
	String parsedText = layout.getText();
	for (int i = 0; i < mnemonics.length - 1; i++) {
		if (mnemonics[i] != -1) {
			char mnemonic = parsedText.charAt(mnemonics[i]);
			if (uckey == Character.toUpperCase (mnemonic)) {
				if (!setFocus ()) return false;
				focusIndex = i;
				redraw ();
				return  true;
			}
		}
	}
	return false;
}

boolean mnemonicMatch (char key) {
	char uckey = Character.toUpperCase (key);
	String parsedText = layout.getText();
	for (int i = 0; i < mnemonics.length - 1; i++) {
		if (mnemonics[i] != -1) {
			char mnemonic = parsedText.charAt(mnemonics[i]);
			if (uckey == Character.toUpperCase (mnemonic)) { 
				return true;
			}
		}
	}
	return false;
}


void releaseWidget () {
	super.releaseWidget ();
	if (layout != null)	layout.dispose ();
	layout = null;
	if (linkColor != null)	linkColor.dispose ();
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

int setBounds(int x, int y, int width, int height, boolean move, boolean resize) {
	int result = super.setBounds (x, y, width,height, move, resize);
	if ((result & RESIZED) != 0) {		
		layout.setWidth (width > 0 ? width : -1);
		redraw ();
	}
	return result;
}

void setFontDescription (int /*long*/ font) {
	super.setFontDescription (font);
	layout.setFont (Font.gtk_new (display, font));
}

void setOrientation (boolean create) {
    super.setOrientation (create);
    layout.setOrientation (style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT));
    if (!create) redraw (true);
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
	layout.setText (parse (string));
	focusIndex = offsets.length > 0 ? 0 : -1;
	selection.x = selection.y = -1;
	boolean enabled = (state & DISABLED) == 0;
	TextStyle linkStyle = new TextStyle (null, enabled ? linkColor : disabledColor, null);
	linkStyle.underline = true;
	int [] bidiSegments = new int [offsets.length*2];
	for (int i = 0; i < offsets.length; i++) {
		Point point = offsets [i];
		layout.setStyle (linkStyle, point.x, point.y);
		bidiSegments[i*2] = point.x;
		bidiSegments[i*2+1] = point.y+1;
	}
	layout.setSegments (bidiSegments);
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

void showWidget () {
	super.showWidget ();
	fixStyle (handle);
}

int traversalCode (int key, GdkEventKey event) {
	if (offsets.length == 0) return 0;
	int bits = super.traversalCode (key, event);
	if (key == OS.GDK_Tab && focusIndex < offsets.length - 1) {
		return bits & ~SWT.TRAVERSE_TAB_NEXT;
	}
	if (key == OS.GDK_ISO_Left_Tab && focusIndex > 0) {
		return bits & ~SWT.TRAVERSE_TAB_PREVIOUS;
	}
	return bits;
}
}
