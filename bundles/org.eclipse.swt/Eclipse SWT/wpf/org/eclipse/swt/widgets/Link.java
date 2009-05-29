/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.internal.wpf.*;
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
	Point [] offsets;
	Point selection;
	String [] ids;
	String text;
	private int[] mnemonics;
	
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

int backgroundProperty () {
	return OS.TextBlock_BackgroundProperty ();
}

void createHandle () {
	state |= THEME_BACKGROUND;
	handle = OS.gcnew_TextBlock ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

int defaultBackground () {
	return OS.SystemColors_ControlColor;
}

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

void HandleClick (int sender, int e) {
	if (!checkEvent (e)) return;
	int source = OS.RoutedEventArgs_Source (e);
	int i = OS.FrameworkContentElement_Tag (source);
	OS.GCHandle_Free (source);
	Event event = new Event ();
	event.text = ids [i];
	sendEvent (SWT.Selection, event);	
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

int parseMnemonics (char [] buffer, int start, int end, StringBuffer result) {
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

void setFont (int font, double size) {
	if (font != 0) {
		int fontFamily = OS.Typeface_FontFamily( font);
		int style = OS.Typeface_Style (font);
		int weight = OS.Typeface_Weight (font);
		int stretch = OS.Typeface_Stretch (font);
		OS.TextBlock_FontFamily (handle, fontFamily);
		OS.TextBlock_FontStyle (handle, style);
		OS.TextBlock_FontWeight (handle, weight);
		OS.TextBlock_FontStretch (handle, stretch);
		OS.TextBlock_FontSize (handle, size);
		OS.GCHandle_Free (fontFamily);
		OS.GCHandle_Free (style);
		OS.GCHandle_Free (weight);
		OS.GCHandle_Free (stretch);
	} else {
		int property = OS.TextBlock_FontFamilyProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
		property = OS.TextBlock_FontStyleProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
		property = OS.TextBlock_FontWeightProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
		property = OS.TextBlock_FontStretchProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
		property = OS.TextBlock_FontSizeProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
	}
}

void setForegroundBrush (int brush) {
//TODO	
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
	String parsed = parse (string);
	int inlines = OS.TextBlock_Inlines (handle);
	OS.InlineCollection_Clear (inlines);
	int start = 0, end = parsed.length ();
	int offsetIndex = 0;
	while (start < end) {
		Point point = offsetIndex < offsets.length ? offsets [offsetIndex] : null;
		if (point == null) {
			String substring = parsed.substring (start, end);
			int stringPtr = createDotNetString (substring, false);
			if (stringPtr == 0) error(SWT.ERROR_NO_HANDLES);
			int run = OS.gcnew_Run ();
			if (run == 0) error(SWT.ERROR_NO_HANDLES);
			OS.Run_Text (run, stringPtr);
			OS.InlineCollection_Add (inlines, run);
			OS.GCHandle_Free (stringPtr);
			OS.GCHandle_Free (run);
			start = end;
		} else {
			if (start < point.x) {
				String substring = parsed.substring (start, point.x);
				int stringPtr = createDotNetString (substring, false);
				if (stringPtr == 0) error(SWT.ERROR_NO_HANDLES);
				int run = OS.gcnew_Run ();
				if (run == 0) error(SWT.ERROR_NO_HANDLES);
				OS.Run_Text (run, stringPtr);
				OS.InlineCollection_Add (inlines, run);
				OS.GCHandle_Free (stringPtr);
				OS.GCHandle_Free (run);
				start = point.x;
			} else {
				String substring = parsed.substring (point.x, point.y+1);
				int stringPtr = createDotNetString (substring, false);
				if (stringPtr == 0) error(SWT.ERROR_NO_HANDLES);
				int run = OS.gcnew_Run ();
				if (run == 0) error(SWT.ERROR_NO_HANDLES);
				OS.Run_Text (run, stringPtr);
				int hyperlink = OS.gcnew_Hyperlink (run);
				OS.FrameworkContentElement_Tag (hyperlink, offsetIndex);
				OS.InlineCollection_Add (inlines, hyperlink);
				int handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleClick");
				OS.Hyperlink_Click (hyperlink, handler);
				OS.GCHandle_Free (handler);
				OS.GCHandle_Free (stringPtr);
				OS.GCHandle_Free (run);
				OS.GCHandle_Free (hyperlink);
				start = point.y+1;
				offsetIndex++;
			}
		}
	}
	OS.GCHandle_Free (inlines);
}

int traversalCode (int key, int event) {
//	if (offsets.length == 0) return 0;
	int bits = super.traversalCode (key, event);
//	if (key == OS.Key_Tab && focusIndex < offsets.length - 1) {
//		return bits & ~SWT.TRAVERSE_TAB_NEXT;
//	}
//	if (key == OS.GDK_ISO_Left_Tab && focusIndex > 0) {
//		return bits & ~SWT.TRAVERSE_TAB_PREVIOUS;
//	}
	//TODO
	return bits;
}
}
