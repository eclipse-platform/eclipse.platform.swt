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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify numeric
 * values.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add children to it, or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, Modify, Verify</dd>
 * </dl>
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#spinner">Spinner snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.1
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Spinner extends Composite {
	static final int MIN_ARROW_WIDTH = 6;
	int lastEventTime = 0;
	long /*int*/ imContext;
	long /*int*/ gdkEventKey = 0;
	int fixStart = -1, fixEnd = -1;
	double climbRate = 1;
	
	/**
	 * the operating system limit for the number of characters
	 * that the text field in an instance of this class can hold
	 * 
	 * @since 3.4
	 */
	public final static int LIMIT;
	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0x7FFFFFFF;
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
 * @see SWT#READ_ONLY
 * @see SWT#WRAP
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Spinner (Composite parent, int style) {
	super (parent, checkStyle (style));
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
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is not called for texts.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed in a single-line text.
 * </p>
 *
 * @param listener the listener which should be notified when the control is selected by the user
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
public void addSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
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
void addVerifyListener (VerifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Verify, typedListener);
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
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	int[] w = new int [1], h = new int [1];
	OS.gtk_widget_realize (handle);
	long /*int*/ layout = OS.gtk_entry_get_layout (handle);
	long /*int*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	double upper = gtk_adjustment_get_upper (hAdjustment);
	int digits = OS.gtk_spin_button_get_digits (handle);
	for (int i = 0; i < digits; i++) upper *= 10; 
	String string = String.valueOf ((int) upper);
	if (digits > 0) {
		StringBuffer buffer = new StringBuffer ();
		buffer.append (string);
		buffer.append (getDecimalSeparator ());
		int count = digits - string.length ();
		while (count >= 0) {
			buffer.append ("0");
			count--;
		}
		string = buffer.toString ();
	}
	byte [] buffer1 = Converter.wcsToMbcs (null, string, false);
	long /*int*/ ptr = OS.pango_layout_get_text (layout);
	int length = OS.strlen (ptr);
	byte [] buffer2 = new byte [length];
	OS.memmove (buffer2, ptr, length);	
	OS.pango_layout_set_text (layout, buffer1, buffer1.length);
	OS.pango_layout_get_size (layout, w, h);
	OS.pango_layout_set_text (layout, buffer2, buffer2.length);
	int width = OS.PANGO_PIXELS (w [0]);
	int height = OS.PANGO_PIXELS (h [0]);
	width = wHint == SWT.DEFAULT ? width : wHint;
	height = hHint == SWT.DEFAULT ? height : hHint;
	Rectangle trim = computeTrim (0, 0, width, height);
	return new Point (trim.width, trim.height);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	int xborder = 0, yborder = 0;
	long /*int*/ style = OS.gtk_widget_get_style (handle);
	if ((this.style & SWT.BORDER) != 0) {
		xborder += OS.gtk_style_get_xthickness (style);
		yborder += OS.gtk_style_get_ythickness (style);
	}
	int [] property = new int [1];
	OS.gtk_widget_style_get (handle, OS.interior_focus, property, 0);
	if (property [0] == 0) {
		OS.gtk_widget_style_get (handle, OS.focus_line_width, property, 0);
		xborder += property [0];
		yborder += property [0];
	}
	long /*int*/ fontDesc = OS.gtk_style_get_font_desc (style);
	int fontSize = OS.pango_font_description_get_size (fontDesc);
	int arrowSize = Math.max (OS.PANGO_PIXELS (fontSize), MIN_ARROW_WIDTH);
	arrowSize = arrowSize - arrowSize % 2;	
	Rectangle trim = super.computeTrim (x, y, width, height);
	trim.x -= xborder;
	trim.y -= yborder;
	trim.width += 2 * xborder;
	trim.height += 2 * yborder;
	trim.width += arrowSize + (2 * OS.gtk_style_get_xthickness (style));
	GtkBorder innerBorder = Display.getEntryInnerBorder (handle);
	trim.x -= innerBorder.left;
	trim.y -= innerBorder.top;
	trim.width += innerBorder.left + innerBorder.right;
	trim.height += innerBorder.top + innerBorder.bottom;
	return new Rectangle (trim.x, trim.y, trim.width, trim.height);
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
	OS.gtk_editable_copy_clipboard (handle);
}

void createHandle (int index) {
	state |= HANDLE | MENU;
	fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	gtk_widget_set_has_window (fixedHandle, true);
	long /*int*/ adjustment = OS.gtk_adjustment_new (0, 0, 100, 1, 10, 0);
	if (adjustment == 0) error (SWT.ERROR_NO_HANDLES);
	handle = OS.gtk_spin_button_new (adjustment, climbRate, 0);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_container_add (fixedHandle, handle);
	OS.gtk_editable_set_editable (handle, (style & SWT.READ_ONLY) == 0);
	OS.gtk_entry_set_has_frame (handle, (style & SWT.BORDER) != 0);
	OS.gtk_spin_button_set_wrap (handle, (style & SWT.WRAP) != 0);
	if (OS.GTK_VERSION >= OS.VERSION (3, 0, 0)) {
		imContext = OS.imContextLast();
	}
}

/**
 * Cuts the selected text.
 * <p>
 * The current selection is first copied to the
 * clipboard and then deleted from the widget.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void cut () {
	checkWidget ();
	OS.gtk_editable_cut_clipboard (handle);
}

void deregister () {
	super.deregister ();
	long /*int*/ imContext = imContext ();
	if (imContext != 0) display.removeWidget (imContext);
}

long /*int*/ eventWindow () {
	return paintWindow ();
}

long /*int*/ enterExitHandle () {
	return fixedHandle;
}

boolean filterKey (int keyval, long /*int*/ event) {
	int time = OS.gdk_event_get_time (event);
	if (time != lastEventTime) {
		lastEventTime = time;
		long /*int*/ imContext = imContext ();
		if (imContext != 0) {
			return OS.gtk_im_context_filter_keypress (imContext, event);
		}
	}
	gdkEventKey = event;
	return false;
}

void fixIM () {
	/*
	*  The IM filter has to be called one time for each key press event.
	*  When the IM is open the key events are duplicated. The first event
	*  is filtered by SWT and the second event is filtered by GTK.  In some 
	*  cases the GTK handler does not run (the widget is destroyed, the 
	*  application code consumes the event, etc), for these cases the IM
	*  filter has to be called by SWT.
	*/	
	if (gdkEventKey != 0 && gdkEventKey != -1) {
		long /*int*/ imContext = imContext ();
		if (imContext != 0) {
			OS.gtk_im_context_filter_keypress (imContext, gdkEventKey);
			gdkEventKey = -1;
			return;
		}
	}
	gdkEventKey = 0;
}

GdkColor getBackgroundColor () {
	return getBaseColor ();
}

public int getBorderWidth () {
	checkWidget();
	long /*int*/ style = OS.gtk_widget_get_style (handle);
	if ((this.style & SWT.BORDER) != 0) {
		 return OS.gtk_style_get_xthickness (style);
	}
	return 0;
}

GdkColor getForegroundColor () {
	return getTextColor ();
}

/**
 * Returns the amount that the receiver's value will be
 * modified by when the up/down arrows are pressed.
 *
 * @return the increment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getIncrement () {
	checkWidget ();
	long /*int*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	int digits = OS.gtk_spin_button_get_digits (handle);
	double value = gtk_adjustment_get_step_increment (hAdjustment);
	for (int i = 0; i < digits; i++) value *= 10;
	return (int) (value > 0 ? value + 0.5 : value - 0.5);
}

/**
 * Returns the maximum value which the receiver will allow.
 *
 * @return the maximum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMaximum () {
	checkWidget ();
	long /*int*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	int digits = OS.gtk_spin_button_get_digits (handle);
	double value = gtk_adjustment_get_upper (hAdjustment);
	for (int i = 0; i < digits; i++) value *= 10;
	return (int) (value > 0 ? value + 0.5 : value - 0.5);
}

/**
 * Returns the minimum value which the receiver will allow.
 *
 * @return the minimum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMinimum () {
	checkWidget ();
	long /*int*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	int digits = OS.gtk_spin_button_get_digits (handle);
	double value = gtk_adjustment_get_lower (hAdjustment);
	for (int i = 0; i < digits; i++) value *= 10;
	return (int) (value > 0 ? value + 0.5 : value - 0.5);
}

/**
 * Returns the amount that the receiver's position will be
 * modified by when the page up/down keys are pressed.
 *
 * @return the page increment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getPageIncrement () {
	checkWidget ();
	long /*int*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	int digits = OS.gtk_spin_button_get_digits (handle);
	double value = gtk_adjustment_get_page_increment (hAdjustment);
	for (int i = 0; i < digits; i++) value *= 10;
	return (int) (value > 0 ? value + 0.5 : value - 0.5);
}

/**
 * Returns the <em>selection</em>, which is the receiver's position.
 *
 * @return the selection 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelection () {
	checkWidget ();	
	long /*int*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	int digits = OS.gtk_spin_button_get_digits (handle);
	double value = gtk_adjustment_get_value(hAdjustment);
	for (int i = 0; i < digits; i++) value *= 10;
	return (int) (value > 0 ? value + 0.5 : value - 0.5);
}

/**
 * Returns a string containing a copy of the contents of the
 * receiver's text field, or an empty string if there are no
 * contents.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public String getText () {
	checkWidget ();
	long /*int*/ str = OS.gtk_entry_get_text (handle);
	if (str == 0) return "";
	int length = OS.strlen (str);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, str, length);
	return new String (Converter.mbcsToWcs (null, buffer));
}

/**
 * Returns the maximum number of characters that the receiver's
 * text field is capable of holding. If this has not been changed
 * by <code>setTextLimit()</code>, it will be the constant
 * <code>Spinner.LIMIT</code>.
 * 
 * @return the text limit
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #LIMIT
 * 
 * @since 3.4
 */
public int getTextLimit () {
	checkWidget ();
	int limit = OS.gtk_entry_get_max_length (handle);
	return limit == 0 ? 0xFFFF : limit;
}

/**
 * Returns the number of decimal places used by the receiver.
 *
 * @return the digits
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getDigits () {
	checkWidget ();
	return OS.gtk_spin_button_get_digits (handle);
}

String getDecimalSeparator () {
	long /*int*/ ptr = OS.localeconv_decimal_point ();
	int length = OS.strlen (ptr);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, ptr, length);	
	return new String (Converter.mbcsToWcs (null, buffer));
}

long /*int*/ gtk_activate (long /*int*/ widget) {
	sendSelectionEvent (SWT.DefaultSelection);
	return 0;
}

long /*int*/ gtk_changed (long /*int*/ widget) {
	long /*int*/ str = OS.gtk_entry_get_text (handle);
	int length = OS.strlen (str);
	if (length > 0) {
		long /*int*/ [] endptr = new long /*int*/ [1];
		double value = OS.g_strtod (str, endptr);
		if (endptr [0] == str + length) {
			long /*int*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
			GtkAdjustment adjustment = new GtkAdjustment ();
			gtk_adjustment_get (hAdjustment, adjustment);
			if (value != adjustment.value && adjustment.lower <= value && value <= adjustment.upper) {
				OS.gtk_spin_button_update (handle);
			}
		}
	}
	
	/*
	* Feature in GTK.  When the user types, GTK positions
	* the caret after sending the changed signal.  This
	* means that application code that attempts to position
	* the caret during a changed signal will fail.  The fix
	* is to post the modify event when the user is typing.
	*/
	boolean keyPress = false;
	long /*int*/ eventPtr = OS.gtk_get_current_event ();
	if (eventPtr != 0) {
		GdkEventKey gdkEvent = new GdkEventKey ();
		OS.memmove (gdkEvent, eventPtr, GdkEventKey.sizeof);
		switch (gdkEvent.type) {
			case OS.GDK_KEY_PRESS:
				keyPress = true;
				break;
		}
		OS.gdk_event_free (eventPtr);
	}
	if (keyPress) {
		postEvent (SWT.Modify);
	} else {
		sendEvent (SWT.Modify);
	}
	return 0;
}

long /*int*/ gtk_commit (long /*int*/ imContext, long /*int*/ text) {
	if (text == 0) return 0;
	if (!OS.gtk_editable_get_editable (handle)) return 0;
	int length = OS.strlen (text);
	if (length == 0) return 0;
	byte [] buffer = new byte [length];
	OS.memmove (buffer, text, length);
	char [] chars = Converter.mbcsToWcs (null, buffer);
	char [] newChars = sendIMKeyEvent (SWT.KeyDown, null, chars);
	if (newChars == null) return 0;
	/*
	* Feature in GTK.  For a GtkEntry, during the insert-text signal,
	* GTK allows the programmer to change only the caret location,
	* not the selection.  If the programmer changes the selection,
	* the new selection is lost.  The fix is to detect a selection
	* change and set it after the insert-text signal has completed.
	*/
	fixStart = fixEnd = -1;
	OS.g_signal_handlers_block_matched (imContext, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, COMMIT);
	int id = OS.g_signal_lookup (OS.commit, OS.gtk_im_context_get_type ());
	int mask =  OS.G_SIGNAL_MATCH_DATA | OS.G_SIGNAL_MATCH_ID;
	OS.g_signal_handlers_unblock_matched (imContext, mask, id, 0, 0, 0, handle);
	if (newChars == chars) {
		OS.g_signal_emit_by_name (imContext, OS.commit, text);
	} else {
		buffer = Converter.wcsToMbcs (null, newChars, true);
		OS.g_signal_emit_by_name (imContext, OS.commit, buffer);
	}
	OS.g_signal_handlers_unblock_matched (imContext, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, COMMIT);
	OS.g_signal_handlers_block_matched (imContext, mask, id, 0, 0, 0, handle);
	if (fixStart != -1 && fixEnd != -1) {
		OS.gtk_editable_set_position (handle, fixStart);
		OS.gtk_editable_select_region (handle, fixStart, fixEnd);
	}
	fixStart = fixEnd = -1;
	return 0;
}

long /*int*/ gtk_delete_text (long /*int*/ widget, long /*int*/ start_pos, long /*int*/ end_pos) {
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	long /*int*/ ptr = OS.gtk_entry_get_text (handle);
	if (end_pos == -1) end_pos = OS.g_utf8_strlen (ptr, -1);
	int start = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, start_pos);
	int end = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, end_pos);
	String newText = verifyText ("", start, end);
	if (newText == null) {
		OS.g_signal_stop_emission_by_name (handle, OS.delete_text);
	} else {
		if (newText.length () > 0) {
			int [] pos = new int [1];
			pos [0] = (int)/*64*/end_pos;
			byte [] buffer = Converter.wcsToMbcs (null, newText, false);
			OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			OS.gtk_editable_insert_text (handle, buffer, buffer.length, pos);
			OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			OS.gtk_editable_set_position (handle, pos [0]);
		}
	}
	return 0;
}

long /*int*/ gtk_event_after (long /*int*/ widget, long /*int*/ gdkEvent) {
	if (cursor != null) setCursor (cursor.handle);
	return super.gtk_event_after (widget, gdkEvent);
}

long /*int*/ gtk_focus_out_event (long /*int*/ widget, long /*int*/ event) {
	fixIM ();
	return super.gtk_focus_out_event (widget, event);
}

long /*int*/ gtk_insert_text (long /*int*/ widget, long /*int*/ new_text, long /*int*/ new_text_length, long /*int*/ position) {
//	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	if (new_text == 0 || new_text_length == 0) return 0;
	byte [] buffer = new byte [(int)/*64*/new_text_length];
	OS.memmove (buffer, new_text, buffer.length);
	String oldText = new String (Converter.mbcsToWcs (null, buffer));
	int [] pos = new int [1];
	OS.memmove (pos, position, 4);
	long /*int*/ ptr = OS.gtk_entry_get_text (handle);
	if (pos [0] == -1) pos [0] = (int)/*64*/OS.g_utf8_strlen (ptr, -1);
	int start = (int)/*64*/OS.g_utf16_pointer_to_offset (ptr, pos [0]);
	String newText = verifyText (oldText, start, start);
	if (newText != oldText) {
		int [] newStart = new int [1], newEnd = new int [1];
		OS.gtk_editable_get_selection_bounds (handle, newStart, newEnd);
		if (newText != null) {
			if (newStart [0] != newEnd [0]) {
				OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
				OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				OS.gtk_editable_delete_selection (handle);
				OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
				OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			}
			byte [] buffer3 = Converter.wcsToMbcs (null, newText, false);
			OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			OS.gtk_editable_insert_text (handle, buffer3, buffer3.length, pos);
			OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			newStart [0] = newEnd [0] = pos [0];
		}
		pos [0] = newEnd [0];
		if (newStart [0] != newEnd [0]) {
			fixStart = newStart [0];
			fixEnd = newEnd [0];
		}
		OS.memmove (position, pos, 4);
		OS.g_signal_stop_emission_by_name (handle, OS.insert_text);
	}
	return 0;
}

long /*int*/ gtk_key_press_event (long /*int*/ widget, long /*int*/ event) {
	long /*int*/ result = super.gtk_key_press_event (widget, event);
	if (result != 0) fixIM ();
	if (gdkEventKey == -1) result = 1;
	gdkEventKey = 0;
	return result;
}

long /*int*/ gtk_populate_popup (long /*int*/ widget, long /*int*/ menu) {
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		OS.gtk_widget_set_direction (menu, OS.GTK_TEXT_DIR_RTL);
		OS.gtk_container_forall (menu, display.setDirectionProc, OS.GTK_TEXT_DIR_RTL);
	}
	return 0;
}

long /*int*/ gtk_value_changed (long /*int*/ widget) {
	sendSelectionEvent (SWT.Selection);
	return 0;
}

void hookEvents () {
	super.hookEvents();
	OS.g_signal_connect_closure (handle, OS.changed, display.closures [CHANGED], true);
	OS.g_signal_connect_closure (handle, OS.insert_text, display.closures [INSERT_TEXT], false);
	OS.g_signal_connect_closure (handle, OS.delete_text, display.closures [DELETE_TEXT], false);
	OS.g_signal_connect_closure (handle, OS.value_changed, display.closures [VALUE_CHANGED], false);
	OS.g_signal_connect_closure (handle, OS.activate, display.closures [ACTIVATE], false);
	OS.g_signal_connect_closure (handle, OS.populate_popup, display.closures [POPULATE_POPUP], false);
	long /*int*/ imContext = imContext ();
	if (imContext != 0) {
		OS.g_signal_connect_closure (imContext, OS.commit, display.closures [COMMIT], false);
		int id = OS.g_signal_lookup (OS.commit, OS.gtk_im_context_get_type ());
		int mask =  OS.G_SIGNAL_MATCH_DATA | OS.G_SIGNAL_MATCH_ID;
		OS.g_signal_handlers_block_matched (imContext, mask, id, 0, 0, 0, handle);
	}
}

long /*int*/ imContext () {
	if (imContext != 0) return imContext; 
	return OS.GTK_ENTRY_IM_CONTEXT (handle);
}

long /*int*/ paintWindow () {
	long /*int*/ window = super.paintWindow ();
	long /*int*/ children = OS.gdk_window_get_children (window);
	if (children != 0) window = OS.g_list_data (children);
	OS.g_list_free (children);
	return window;
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
	OS.gtk_editable_paste_clipboard (handle);
}

void register () {
	super.register ();
	long /*int*/ imContext = imContext ();
	if (imContext != 0) display.addWidget (imContext, this);
}

void releaseWidget () {
	super.releaseWidget ();
	fixIM ();
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
public void removeSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);	
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
void removeVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);	
}

void setBackgroundColor (GdkColor color) {
	super.setBackgroundColor (color);
	OS.gtk_widget_modify_base (handle, 0, color);
}

void setCursor (long /*int*/ cursor) {
	long /*int*/ defaultCursor = 0;
	if (cursor == 0) defaultCursor = OS.gdk_cursor_new (OS.GDK_XTERM);
	super.setCursor (cursor != 0 ? cursor : defaultCursor);
	if (cursor == 0) gdk_cursor_unref (defaultCursor);
}

void setFontDescription (long /*int*/ font) {
	super.setFontDescription (font);
}

void setForegroundColor (GdkColor color) {
	setForegroundColor (handle, color, false);
}

/**
 * Sets the amount that the receiver's value will be
 * modified by when the up/down arrows are pressed to
 * the argument, which must be at least one.
 *
 * @param value the new increment (must be greater than zero)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setIncrement (int value) {
	checkWidget ();
	if (value < 1) return;
	long /*int*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	double page_increment = gtk_adjustment_get_page_increment (hAdjustment);
	double newValue = value;
	int digits = OS.gtk_spin_button_get_digits (handle);
	for (int i = 0; i < digits; i++) newValue /= 10;
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_spin_button_set_increments (handle, newValue, page_increment);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

/**
 * Sets the maximum value that the receiver will allow.  This new
 * value will be ignored if it is less than the receiver's current
 * minimum value.  If the new maximum is applied then the receiver's
 * selection value will be adjusted if necessary to fall within its new range.
 *
 * @param value the new maximum, which must be greater than or equal to the current minimum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMaximum (int value) {
	checkWidget ();
	long /*int*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	double lower = gtk_adjustment_get_lower (hAdjustment);
	double newValue = value;
	int digits = OS.gtk_spin_button_get_digits (handle);
	for (int i = 0; i < digits; i++) newValue /= 10;
	if (newValue < lower) return;
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_spin_button_set_range (handle, lower, newValue);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

/**
 * Sets the minimum value that the receiver will allow.  This new
 * value will be ignored if it is greater than the receiver's
 * current maximum value.  If the new minimum is applied then the receiver's
 * selection value will be adjusted if necessary to fall within its new range.
 *
 * @param value the new minimum, which must be less than or equal to the current maximum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinimum (int value) {
	checkWidget ();
	long /*int*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	double upper = gtk_adjustment_get_upper (hAdjustment);
	double newValue = value;
	int digits = OS.gtk_spin_button_get_digits (handle);
	for (int i = 0; i < digits; i++) newValue /= 10;
	if (newValue > upper) return;
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_spin_button_set_range (handle, newValue, upper);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

/**
 * Sets the amount that the receiver's position will be
 * modified by when the page up/down keys are pressed
 * to the argument, which must be at least one.
 *
 * @param value the page increment (must be greater than zero)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setPageIncrement (int value) {
	checkWidget ();
	if (value < 1) return;
	long /*int*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	double step_increment = gtk_adjustment_get_step_increment(hAdjustment);
	double newValue = value;
	int digits = OS.gtk_spin_button_get_digits (handle);
	for (int i = 0; i < digits; i++) newValue /= 10;
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_spin_button_set_increments (handle, step_increment, newValue);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

/**
 * Sets the <em>selection</em>, which is the receiver's
 * position, to the argument. If the argument is not within
 * the range specified by minimum and maximum, it will be
 * adjusted to fall within this range.
 *
 * @param value the new selection (must be zero or greater)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int value) {
	checkWidget ();
	double newValue = value;
	int digits = OS.gtk_spin_button_get_digits (handle);
	for (int i = 0; i < digits; i++) newValue /= 10;
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_spin_button_set_value (handle, newValue);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

/**
 * Sets the maximum number of characters that the receiver's
 * text field is capable of holding to be the argument.
 * <p>
 * To reset this value to the default, use <code>setTextLimit(Spinner.LIMIT)</code>.
 * Specifying a limit value larger than <code>Spinner.LIMIT</code> sets the
 * receiver's limit to <code>Spinner.LIMIT</code>.
 * </p>
 * @param limit new text limit
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #LIMIT
 * 
 * @since 3.4
 */
public void setTextLimit (int limit) {
	checkWidget ();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	OS.gtk_entry_set_max_length (handle, limit);
}

/**
 * Sets the number of decimal places used by the receiver.
 * <p>
 * The digit setting is used to allow for floating point values in the receiver.
 * For example, to set the selection to a floating point value of 1.37 call setDigits() with 
 * a value of 2 and setSelection() with a value of 137. Similarly, if getDigits() has a value
 * of 2 and getSelection() returns 137 this should be interpreted as 1.37. This applies to all
 * numeric APIs. 
 * </p>
 * 
 * @param value the new digits (must be greater than or equal to zero)
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the value is less than zero</li>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDigits (int value) {
	checkWidget ();
	if (value < 0) error (SWT.ERROR_INVALID_ARGUMENT);
	int digits = OS.gtk_spin_button_get_digits (handle);
	if (value == digits) return;
	long /*int*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	gtk_adjustment_get (hAdjustment, adjustment);
	int diff = Math.abs (value - digits);
	int factor = 1;
	for (int i = 0; i < diff; i++) factor *= 10;
	if (digits > value) {
		adjustment.value *= factor;
		adjustment.upper *= factor;
		adjustment.lower *= factor;
		adjustment.step_increment *= factor;
		adjustment.page_increment *= factor;
		climbRate *= factor;
	} else {
		adjustment.value /= factor;
		adjustment.upper /= factor;
		adjustment.lower /= factor;
		adjustment.step_increment /= factor;
		adjustment.page_increment /= factor;
		climbRate /= factor;
	}
	gtk_adjustment_configure (hAdjustment, adjustment);
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_spin_button_configure (handle, hAdjustment, climbRate, value);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

/**
 * Sets the receiver's selection, minimum value, maximum
 * value, digits, increment and page increment all at once.
 * <p>
 * Note: This is similar to setting the values individually
 * using the appropriate methods, but may be implemented in a 
 * more efficient fashion on some platforms.
 * </p>
 *
 * @param selection the new selection value
 * @param minimum the new minimum value
 * @param maximum the new maximum value
 * @param digits the new digits value
 * @param increment the new increment value
 * @param pageIncrement the new pageIncrement value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setValues (int selection, int minimum, int maximum, int digits, int increment, int pageIncrement) {
	checkWidget ();
	if (maximum < minimum) return;
	if (digits < 0) return;
	if (increment < 1) return;
	if (pageIncrement < 1) return;
	selection = Math.min (Math.max (minimum, selection), maximum);
	double factor = 1;
	for (int i = 0; i < digits; i++) factor *= 10;	
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_spin_button_set_range (handle, minimum / factor, maximum / factor);
	OS.gtk_spin_button_set_increments (handle, increment / factor, pageIncrement / factor);
	OS.gtk_spin_button_set_value (handle, selection / factor);
	/*
	* The value of climb-rate indicates the acceleration rate 
	* to spin the value when the button is pressed and hold 
	* on the arrow button. This value should be varied 
	* depending upon the value of digits.
	*/
	climbRate = 1.0 / factor;
	long /*int*/ adjustment = OS.gtk_spin_button_get_adjustment(handle);
	OS.gtk_spin_button_configure (handle, adjustment, climbRate, digits);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

boolean checkSubwindow () {
	return false;
}

boolean translateTraversal (GdkEventKey keyEvent) {
	int key = keyEvent.keyval;
	switch (key) {
		case OS.GDK_KP_Enter:
		case OS.GDK_Return: {
			long /*int*/ imContext =  imContext ();
			if (imContext != 0) {
				long /*int*/ [] preeditString = new long /*int*/ [1];
				OS.gtk_im_context_get_preedit_string (imContext, preeditString, null, null);
				if (preeditString [0] != 0) {
					int length = OS.strlen (preeditString [0]);
					OS.g_free (preeditString [0]);
					if (length != 0) return false;
				}
			}
		}
	}
	return super.translateTraversal (keyEvent);
}

String verifyText (String string, int start, int end) {
	if (string.length () == 0 && start == end) return null;
	Event event = new Event ();
	event.text = string;
	event.start = start;
	event.end = end;
	long /*int*/ eventPtr = OS.gtk_get_current_event ();
	if (eventPtr != 0) {
		GdkEventKey gdkEvent = new GdkEventKey ();
		OS.memmove (gdkEvent, eventPtr, GdkEventKey.sizeof);
		switch (gdkEvent.type) {
			case OS.GDK_KEY_PRESS:
				setKeyState (event, gdkEvent);
				break;
		}
		OS.gdk_event_free (eventPtr);
	}
	int index = 0;
	if (OS.gtk_spin_button_get_digits (handle) > 0) {
		String decimalSeparator = getDecimalSeparator ();
		index = string.indexOf (decimalSeparator);
		if (index != -1) {
			string = string.substring (0, index) + string.substring (index + 1);
		}
		index = 0;
	}
	if (string.length () > 0) {
		long /*int*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
		double lower = gtk_adjustment_get_lower (hAdjustment);
		if (lower < 0 && string.charAt (0) == '-') index++;
	}
	while (index < string.length ()) {
		if (!Character.isDigit (string.charAt (index))) break;
		index++;
	}
	event.doit = index == string.length ();
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

}
