/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

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
	TextLayout layout;
	Color linkColor, disabledColor;
	Point [] offsets;
	Point selection;
	String [] ids;
	int [] mnemonics;

	/**
	 * Index of the currently focused link in the text.
	 * Equals -1, if the text has not been set through setText
	 */
	int focusIndex;

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

@Override
Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	int width, height;
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
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int border = getBorderWidthInPixels ();
	width += border * 2;
	height += border * 2;
	return new Point (width, height);
}

@Override
void createHandle(int index) {
	state |= HANDLE | THEME_BACKGROUND;

	handle = OS.g_object_new(display.gtk_fixed_get_type(), 0);
	if (handle == 0) error(SWT.ERROR_NO_HANDLES);
	if (GTK.GTK4) {
		GTK4.gtk_widget_set_focusable(handle, true);
	} else {
		GTK3.gtk_widget_set_has_window(handle, true);
	}
	GTK.gtk_widget_set_can_focus(handle, true);

	layout = new TextLayout (display);
	disabledColor = new Color (LINK_DISABLED_FOREGROUND);
	offsets = new Point [0];
	ids = new String [0];
	mnemonics = new int [0];
	selection = new Point (-1, -1);
	focusIndex = -1;
}

@Override
void createWidget (int index) {
	super.createWidget (index);
	layout.setFont (getFont ());
	text = "";
	initAccessible ();
}

@Override
void drawWidget(GC gc) {
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
		Rectangle [] rects = getRectanglesInPixels (focusIndex);
		for (int i = 0; i < rects.length; i++) {
			Rectangle rect = DPIUtil.autoScaleDown(rects [i]);
			gc.drawFocus (rect.x, rect.y, rect.width, rect.height);
		}
	}
}

@Override
void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	if (isDisposed ()) return;
	styleLinkParts();
	redraw ();
}

@Override
void fixStyle () {
	fixStyle (handle);
}

void initAccessible () {
	if (GTK.GTK4) return;

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
			Rectangle rect = display.map (getParent (), null, getBounds ());
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
	return linkColor != null ? linkColor : display.getSystemColor(SWT.COLOR_LINK_FOREGROUND);
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
		rects [index++] = DPIUtil.autoScaleUp (layout.getBounds (point.x, point.y));
	} else {
		rects [index++] = DPIUtil.autoScaleUp (layout.getBounds (point.x, lineOffsets [lineStart]-1));
		rects [index++] = DPIUtil.autoScaleUp (layout.getBounds (lineOffsets [lineEnd-1], point.y));
		if (lineEnd - lineStart > 1) {
			for (int i = lineStart; i < lineEnd - 1; i++) {
				rects [index++] = DPIUtil.autoScaleUp (layout.getLineBounds (i));
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
long gtk_button_press_event (long widget, long event) {
	long result = super.gtk_button_press_event (widget, event);
	if (result != 0) return result;

	int eventType = GDK.gdk_event_get_event_type(event);
	eventType = fixGdkEventTypeValues(eventType);

	int [] eventButton = new int [1];
	if (GTK.GTK4) {
		eventButton[0] = GDK.gdk_button_event_get_button(event);
	} else {
		GDK.gdk_event_get_button(event, eventButton);
	}

	double [] eventX = new double [1];
	double [] eventY = new double [1];
	if (GTK.GTK4) {
		GDK.gdk_event_get_position(event, eventX, eventY);
	} else {
		GDK.gdk_event_get_coords(event, eventX, eventY);
	}

	if (eventButton[0] == 1 && eventType == GDK.GDK_BUTTON_PRESS) {
		if (focusIndex != -1) setFocus ();
		int x = (int) eventX[0];
		int y = (int) eventY[0];
		if ((style & SWT.MIRRORED) != 0) x = getClientWidth () - x;
		int offset = DPIUtil.autoScaleUp(layout.getOffset (x, y, null));
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
			Rectangle rect = DPIUtil.autoScaleUp(layout.getBounds (oldSelectionX, oldSelectionY));
			redrawInPixels (rect.x, rect.y, rect.width, rect.height, false);
		}
		for (int j = 0; j < offsets.length; j++) {
			Rectangle [] rects = getRectanglesInPixels (j);
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

@Override
long gtk_button_release_event (long widget, long event) {
	long result = super.gtk_button_release_event (widget, event);
	if (result != 0) return result;
	if (focusIndex == -1) return result;

	int [] eventButton = new int [1];
	if (GTK.GTK4) {
		eventButton[0] = GDK.gdk_button_event_get_button(event);
	} else {
		GDK.gdk_event_get_button(event, eventButton);
	}

	double [] eventX = new double [1];
	double [] eventY = new double [1];
	if (GTK.GTK4) {
		GDK.gdk_event_get_position(event, eventX, eventY);
	} else {
		GDK.gdk_event_get_coords(event, eventX, eventY);
	}

	if (eventButton[0] == 1) {
		int x = (int) eventX[0];
		int y = (int) eventY[0];
		if ((style & SWT.MIRRORED) != 0) x = getClientWidth () - x;
		Rectangle [] rects = getRectanglesInPixels (focusIndex);
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

@Override
long gtk_draw (long widget, long cairo) {
	long context = GTK.gtk_widget_get_style_context(widget);
	GtkAllocation allocation = new GtkAllocation();
	GTK.gtk_widget_get_allocation (widget, allocation);
	int width = (state & ZERO_WIDTH) != 0 ? 0 : allocation.width;
	int height = (state & ZERO_HEIGHT) != 0 ? 0 : allocation.height;
	// We specify a 0 value for x & y as we want the whole widget to be
	// colored, not some portion of it.
	GTK.gtk_render_background(context, cairo, 0, 0, width, height);
	return super.gtk_draw(widget, cairo);
}

@Override
long gtk_event_after (long widget, long gdkEvent) {
	long result = super.gtk_event_after (widget, gdkEvent);
	int eventType = GDK.gdk_event_get_event_type(gdkEvent);
	switch (eventType) {
		case GDK.GDK_FOCUS_CHANGE:
			redraw ();
			break;
	}
	return result;
}

@Override
boolean gtk4_key_press_event(long controller, int keyval, int keycode, int state, long event) {
	boolean handled = super.gtk4_key_press_event(controller, keyval, keycode, state, event);
	if (!handled && focusIndex != -1) {
		switch (keyval) {
			case GDK.GDK_Return:
			case GDK.GDK_KP_Enter:
			case GDK.GDK_space:
				Event jEvent = new Event();
				jEvent.text = ids[focusIndex];
				sendSelectionEvent(SWT.Selection, jEvent, true);
				break;
			case GDK.GDK_Tab:
				if (focusIndex < offsets.length - 1) {
					focusIndex++;
					redraw();
				}
				break;
			case GDK.GDK_ISO_Left_Tab:
				if (focusIndex > 0) {
					focusIndex--;
					redraw();
				}
				break;
		}
	}

	return handled;
}

@Override
long gtk_key_press_event (long widget, long eventPtr) {
	long result = super.gtk_key_press_event (widget, eventPtr);
	if (result != 0) return result;
	if (focusIndex == -1) return result;

	int [] key = new int [1];
	GDK.gdk_event_get_keyval(eventPtr, key);

	switch (key[0]) {
		case GDK.GDK_Return:
		case GDK.GDK_KP_Enter:
		case GDK.GDK_space:
			Event event = new Event ();
			event.text = ids [focusIndex];
			sendSelectionEvent (SWT.Selection, event, true);
			break;
		case GDK.GDK_Tab:
			if (focusIndex < offsets.length - 1) {
				focusIndex++;
				redraw ();
			}
			break;
		case GDK.GDK_ISO_Left_Tab:
			if (focusIndex > 0) {
				focusIndex--;
				redraw ();
			}
			break;
	}
	return result;
}

@Override
long gtk_motion_notify_event (long widget, long event) {
	long result = super.gtk_motion_notify_event (widget, event);
	if (result != 0) return result;

	double [] eventX = new double [1];
	double [] eventY = new double [1];

	int [] state = new int [1];
	if (GTK.GTK4) {
		GDK.gdk_event_get_position(event, eventX, eventY);
		state[0] = GDK.gdk_event_get_modifier_state(event);
	} else {
		GDK.gdk_event_get_coords(event, eventX, eventY);
		GDK.gdk_event_get_state(event, state);
	}

	int x = (int) eventX[0];
	int y = (int) eventY[0];

	if ((style & SWT.MIRRORED) != 0) x = getClientWidth () - x;
	if ((state[0] & GDK.GDK_BUTTON1_MASK) != 0) {
		int oldSelection = selection.y;
		selection.y = DPIUtil.autoScaleUp(layout.getOffset (x, y, null));
		if (selection.y != oldSelection) {
			int newSelection = selection.y;
			if (oldSelection > newSelection) {
				int temp = oldSelection;
				oldSelection = newSelection;
				newSelection = temp;
			}
			Rectangle rect = layout.getBounds (oldSelection, newSelection);
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
	return result;
}

@Override
boolean hooksPaint () {
	return true;
}

@Override
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

@Override
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


@Override
void releaseWidget () {
	super.releaseWidget ();
	if (layout != null)	layout.dispose ();
	layout = null;
	linkColor = null;
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
	StringBuilder result = new StringBuilder ();
	StringBuilder buffer = new StringBuilder(string);
	int index = 0, state = 0, linkIndex = 0;
	int start = 0, tagStart = 0, linkStart = 0, endtagStart = 0, refStart = 0;
	while (index < length) {
		char c = Character.toLowerCase (buffer.charAt(index));

		// escape < or > with \\< or \\>
		if (c == '\\' && index + 1 < length) {
			char c2 = Character.toLowerCase(buffer.charAt(index + 1));
			if (c2 == '<' || c2 == '>') {
				buffer.deleteCharAt(index);
				length--;
			}
		}

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
					mnemonics [linkIndex] = parseMnemonics (buffer.toString().toCharArray(), start, tagStart, result);
					int offset = result.length ();
					parseMnemonics (buffer.toString().toCharArray(), linkStart, endtagStart, result);
					offsets [linkIndex] = new Point (offset, result.length () - 1);
					if (ids [linkIndex] == null) {
						ids [linkIndex] = new String (buffer.toString().toCharArray(), linkStart, endtagStart - linkStart);
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
					ids[linkIndex] = new String (buffer.toString().toCharArray(), refStart, index - refStart);
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
		int tmp = parseMnemonics (buffer.toString().toCharArray(), start, tagStart, result);
		int mnemonic = parseMnemonics (buffer.toString().toCharArray(), Math.max (tagStart, linkStart), length, result);
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

int parseMnemonics (char[] buffer, int start, int end, StringBuilder result) {
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
int setBounds(int x, int y, int width, int height, boolean move, boolean resize) {
	int result = super.setBounds (x, y, width,height, move, resize);
	if ((result & RESIZED) != 0) {
		layout.setWidth (DPIUtil.autoScaleDown((width > 0 ? width : -1)));
		redraw ();
	}
	return result;
}

@Override
void setFontDescription (long font) {
	super.setFontDescription (font);
	layout.setFont (Font.gtk_new (display, font));
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
	checkWidget();
	if (color != null) {
		if (color.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (color.equals(linkColor)) return;
	} else if (linkColor == null) return;
	linkColor = color;
	if (getEnabled()) {
		styleLinkParts();
		redraw();
	}
}

@Override
void setOrientation (boolean create) {
	super.setOrientation (create);
	layout.setOrientation (style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT));
	if (!create) redraw (true);
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
	layout.setText (parse (string));
	focusIndex = offsets.length > 0 ? 0 : -1;
	selection.x = selection.y = -1;
	styleLinkParts();
	int [] bidiSegments = new int [offsets.length*2];
	for (int i = 0; i < offsets.length; i++) {
		Point point = offsets [i];
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

@Override
void showWidget () {
	super.showWidget ();
	fixStyle (handle);
}

void styleLinkParts() {
	boolean enabled = (state & DISABLED) == 0;
	TextStyle linkStyle = new TextStyle (null, enabled ? getLinkForeground() : disabledColor, null);
	linkStyle.underline = true;
	for (int i = 0; i < offsets.length; i++) {
		Point point = offsets [i];
		layout.setStyle (linkStyle, point.x, point.y);
	}
}

@Override
int traversalCode(int key, long event) {
	if (offsets.length == 0) return 0;
	int bits = super.traversalCode (key, event);
	if (key == GDK.GDK_Tab && focusIndex < offsets.length - 1) {
		return bits & ~SWT.TRAVERSE_TAB_NEXT;
	}
	if (key == GDK.GDK_ISO_Left_Tab && focusIndex > 0) {
		return bits & ~SWT.TRAVERSE_TAB_PREVIOUS;
	}
	return bits;
}
}
