/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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

/* DO NOT USE - UNDER CONSTRUCTION */
	
/**
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify number
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, Modify</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
class Spinner extends Composite {
	static final int INNER_BORDER = 2;
	static final int MIN_ARROW_WIDTH = 6;
	int lastEventTime = 0;
	boolean ignoreOutput;
	
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
public void addVerifyListener (VerifyListener listener) {
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
	int /*long*/ layout = OS.gtk_entry_get_layout (handle);
	OS.pango_layout_get_size (layout, w, h);
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
	int /*long*/ style = OS.gtk_widget_get_style (handle);
	if ((this.style & SWT.BORDER) != 0) {
		xborder += OS.gtk_style_get_xthickness (style);
		yborder += OS.gtk_style_get_ythickness (style);
	}
	xborder += INNER_BORDER;
	yborder += INNER_BORDER;
	int [] property = new int [1];
	OS.gtk_widget_style_get (handle, OS.interior_focus, property, 0);
	if (property [0] != 0) {
		OS.gtk_widget_style_get (handle, OS.focus_line_width, property, 0);
		xborder += property [0];
		yborder += property [0];
	}
	int /*long*/ fontDesc = OS.gtk_style_get_font_desc (style);
	int fontSize = OS.pango_font_description_get_size (fontDesc);
	int arrowSize = Math.max (OS.PANGO_PIXELS (fontSize), MIN_ARROW_WIDTH);
	arrowSize = arrowSize - arrowSize % 2;	
	Rectangle trim = super.computeTrim (x, y, width, height);
	trim.x -= xborder;
	trim.y -= yborder;
	trim.width += 2 * xborder;
	trim.height += 2 * yborder;
	trim.width += arrowSize + (2 * OS.gtk_style_get_xthickness (style));
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
	OS.gtk_fixed_set_has_window (fixedHandle, true);	
	int /*long*/ adjustment = OS.gtk_adjustment_new (0, 0, 100, 1, 10, 0);
	if (adjustment == 0) error (SWT.ERROR_NO_HANDLES);
	handle = OS.gtk_spin_button_new (adjustment, 1, 0);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_container_add (fixedHandle, handle);
	OS.gtk_editable_set_editable (handle, (style & SWT.READ_ONLY) == 0);
	OS.gtk_entry_set_has_frame (handle, (style & SWT.BORDER) != 0);
	OS.gtk_spin_button_set_wrap (handle, (style & SWT.WRAP) != 0);	
	byte [] buffer = Converter.wcsToMbcs (null, "", true);
	OS.gtk_entry_set_text (handle, buffer);
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
	int /*long*/ imContext = imContext ();
	if (imContext != 0) display.removeWidget (imContext);
}

boolean filterKey (int keyval, int /*long*/ event) {
	/*
	* Bug in GTK.  On simplified Chinese, when the IM is open and the user
	* presses a tab key, focus traversal occurs and the IM stops working.
	* In order to process keys properly in SWT, if the same key event is
	* dispatched twice, it is ignored.  If the tab key is not dispatched twice,
	* the IM fails.  The fix is to dispatch it.
	*/
	boolean isTab = keyval == OS.GDK_Tab || keyval == OS.GDK_ISO_Left_Tab;
	int time = OS.gdk_event_get_time (event);
	if (time != lastEventTime ||  isTab) {
		lastEventTime = time;
		int /*long*/ imContext = imContext ();
		if (imContext != 0) {
			return OS.gtk_im_context_filter_keypress (imContext, event);
		}
	}
	return false;
}

GdkColor getBackgroundColor () {
	return getBaseColor ();
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
	int /*long*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment);
	return (int) adjustment.step_increment;
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
	int /*long*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment);
	return (int) adjustment.upper;
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
	int /*long*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment);
	return (int) adjustment.lower;
}

/**
 * Returns the amount that the receiver's value will be
 * modified by when the page increment/decrement areas
 * are selected.
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
	int /*long*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment);
	return (int) adjustment.page_increment;
}

/**
 * Returns the single <em>selection</em> that is the receiver's position.
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
	int /*long*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment);
	return (int) adjustment.value;
}

int gtk_activate (int widget) {
	//TODO FIND A BETTER SOLUTION FOR THIS PROBLEM
	if (OS.gtk_editable_get_editable (handle)) {
		ignoreOutput = true;
	}
	postEvent (SWT.DefaultSelection);
	return 0;
}

int /*long*/ gtk_changed (int /*long*/ widget) {
	sendEvent (SWT.Modify);
	return 0;
}

int /*long*/ gtk_commit (int /*long*/ imContext, int /*long*/ text) {
	if (text == 0) return 0;
	if (!OS.gtk_editable_get_editable (handle)) return 0;
	int length = OS.strlen (text);
	if (length == 0) return 0;
	byte [] buffer = new byte [length];
	OS.memmove (buffer, text, length);
	char [] chars = Converter.mbcsToWcs (null, buffer);
	char [] newChars = sendIMKeyEvent (SWT.KeyDown, null, chars);
	if (newChars == null) return 0;
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
	return 0;
}

int /*long*/ gtk_delete_text (int /*long*/ widget, int /*long*/ start_pos, int /*long*/ end_pos) {
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	String newText = verifyText ("", (int)/*64*/start_pos, (int)/*64*/end_pos);
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

int /*long*/ gtk_event_after (int /*long*/ widget, int /*long*/ gdkEvent) {
	if (cursor != null) setCursor (cursor.handle);
	return super.gtk_event_after (widget, gdkEvent);
}

int /*long*/ gtk_focus_out_event (int /*long*/ widget, int /*long*/ event) {
	//TODO FIND A BETTER SOLUTION FOR THIS PROBLEM
	/* Feature in gtk, GtkSpinButton internally call gtk_spin_button_update() 
	 * during focus_out events. This is the correct GTK behavior but not correct
	 * for SWT.  The fix is to ignore the output callback. 
	 */
	if (OS.gtk_editable_get_editable (handle)) {
		ignoreOutput = true;
	}
	return super.gtk_focus_out_event (widget, event);
}

int /*long*/ gtk_input (int /*long*/ widget, int /*long*/ arg1) {
	int /*long*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment);
	OS.memmove (arg1, new double [] {adjustment.value},   8);
	return 1;
}

int /*long*/ gtk_insert_text (int /*long*/ widget, int /*long*/ new_text, int /*long*/ new_text_length, int /*long*/ position) {
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	if (new_text == 0 || new_text_length == 0) return 0;
	byte [] buffer = new byte [(int)/*64*/new_text_length];
	OS.memmove (buffer, new_text, buffer.length);
	String oldText = new String (Converter.mbcsToWcs (null, buffer));
	int [] pos = new int [1];
	OS.memmove (pos, position, 4);
	if (pos [0] == -1)  {
		int /*long*/ ptr = OS.gtk_entry_get_text (handle);
		pos [0] = (int)/*64*/OS.g_utf8_strlen (ptr, -1);
	}
	String newText = verifyText (oldText, pos [0], pos [0]); //WRONG POSITION
	if (newText == null) {
		OS.g_signal_stop_emission_by_name (handle, OS.insert_text);
	} else {
		if (newText != oldText) {
			byte [] buffer2 = Converter.wcsToMbcs (null, newText, false);
			OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			OS.gtk_editable_insert_text (handle, buffer2, buffer2.length, pos);
			OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			OS.g_signal_stop_emission_by_name (handle, OS.insert_text);
			OS.memmove (position, pos, 4);
		}
	}
	return 0;
}

int /*long*/ gtk_output (int /*long*/ widget) {
	if (ignoreOutput) {
		ignoreOutput = false;
		return 1;
	}
	int /*long*/ hAdjustment = OS.gtk_spin_button_get_adjustment (handle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, hAdjustment);
	String value = String.valueOf ((int)adjustment.value);
	int ptr = OS.gtk_entry_get_text (handle);
	if (ptr != 0) {
		int length = OS.strlen (ptr);
		if (length > 0) {
			byte [] buffer = new byte [length];
			OS.memmove (buffer, ptr, length);			
			String text = new String (Converter.mbcsToWcs (null, buffer));
			if (value.equals(text)) return 1;
		}
	}
	byte [] buffer = Converter.wcsToMbcs (null, value, false);	
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.gtk_editable_delete_text (handle, 0, -1);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);	
	OS.gtk_editable_insert_text (handle, buffer, buffer.length, new int[] {0});
	return 1;
}

int /*long*/ gtk_popup_menu (int /*long*/ widget) {
	int [] x = new int [1], y = new int [1];
	OS.gdk_window_get_pointer (0, x, y, null);
	return showMenu (x [0], y [0]) ? 1 : 0;
}

int /*long*/ gtk_value_changed (int /*long*/ widget) {
	postEvent (SWT.Selection);
	return 0;
}

void hookEvents () {
	super.hookEvents();
	int /*long*/ windowProc2 = display.windowProc2;
	int /*long*/ windowProc3 = display.windowProc3;
	int /*long*/ windowProc4 = display.windowProc4;
	int /*long*/ windowProc5 = display.windowProc5;
	OS.g_signal_connect_after (handle, OS.changed, windowProc2, CHANGED);
	OS.g_signal_connect (handle, OS.insert_text, windowProc5, INSERT_TEXT);
	OS.g_signal_connect (handle, OS.delete_text, windowProc4, DELETE_TEXT);
	OS.g_signal_connect (handle, OS.value_changed, windowProc2, VALUE_CHANGED);
	OS.g_signal_connect (handle, OS.activate, windowProc2, ACTIVATE);
	OS.g_signal_connect (handle, OS.input, windowProc3, INPUT);
	OS.g_signal_connect (handle, OS.output, windowProc2, OUTPUT);
	int /*long*/ imContext = imContext ();
	if (imContext != 0) {
		OS.g_signal_connect (imContext, OS.commit, windowProc3, COMMIT);
		int id = OS.g_signal_lookup (OS.commit, OS.gtk_im_context_get_type ());
		int mask =  OS.G_SIGNAL_MATCH_DATA | OS.G_SIGNAL_MATCH_ID;
		OS.g_signal_handlers_block_matched (imContext, mask, id, 0, 0, 0, handle);
	}
}

int /*long*/ imContext () {
	return OS.GTK_ENTRY_IM_CONTEXT (handle);
}

int /*long*/ paintWindow () {
	int /*long*/ window = super.paintWindow ();
	int /*long*/ children = OS.gdk_window_get_children (window);
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
	int /*long*/ imContext = imContext ();
	if (imContext != 0) display.addWidget (imContext, this);
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
public void removeVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);	
}

void setBackgroundColor (GdkColor color) {
	super.setBackgroundColor (color);
	OS.gtk_widget_modify_base (handle, 0, color);
}

void setCursor (int /*long*/ cursor) {
	int /*long*/ defaultCursor = 0;
	if (cursor == 0) defaultCursor = OS.gdk_cursor_new (OS.GDK_XTERM);
	super.setCursor (cursor != 0 ? cursor : defaultCursor);
	if (cursor == 0) OS.gdk_cursor_destroy (defaultCursor);
}

void setFontDescription (int /*long*/ font) {
	super.setFontDescription (font);
}

void setForegroundColor (GdkColor color) {
	super.setForegroundColor (color);
	OS.gtk_widget_modify_text (handle, 0, color);
}

/**
 * Sets the amount that the receiver's value will be
 * modified by when the up/down (or right/left) arrows
 * are pressed to the argument, which must be at least 
 * one.
 *
 * @param increment the new increment (must be greater than zero)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setIncrement (int value) {
	checkWidget ();
	if (value < 1) return;
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_spin_button_set_increments (handle, value, getPageIncrement ());
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

/**
 * Sets the maximum value that the receiver will allow.  This new
 * value will be ignored if it is not greater than the receiver's current
 * minimum value.  If the new maximum is applied then the receiver's
 * selection value will be adjusted if necessary to fall within its new range.
 *
 * @param value the new maximum, which must be greater than the current minimum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMaximum (int value) {
	checkWidget ();
	int minimum = getMinimum();
	if (value <= minimum) return;
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_spin_button_set_range (handle, minimum, value);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

/**
 * Sets the minimum value that the receiver will allow.  This new
 * value will be ignored if it is negative or is not less than the receiver's
 * current maximum value.  If the new minimum is applied then the receiver's
 * selection value will be adjusted if necessary to fall within its new range.
 *
 * @param value the new minimum, which must be nonnegative and less than the current maximum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinimum (int value) {
	checkWidget ();
	int maximum = getMaximum ();
	if (value >= maximum) return;
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_spin_button_set_range (handle, value, maximum);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

/**
 * Sets the amount that the receiver's value will be
 * modified by when the page increment/decrement areas
 * are selected to the argument, which must be at least
 * one.
 *
 * @param pageIncrement the page increment (must be greater than zero)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setPageIncrement (int value) {
	checkWidget ();
	if (value < 1) return;
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_spin_button_set_increments (handle, getIncrement (), value);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

/**
 * Sets the single <em>selection</em> that is the receiver's
 * value to the argument which must be greater than or equal
 * to zero.
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
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
	OS.gtk_spin_button_set_value (handle, value);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, VALUE_CHANGED);
}

boolean translateTraversal (GdkEventKey keyEvent) {
	int key = keyEvent.keyval;
	switch (key) {
		case OS.GDK_KP_Enter:
		case OS.GDK_Return: {
			int /*long*/ imContext =  imContext ();
			if (imContext != 0) {
				int /*long*/ [] preeditString = new int /*long*/ [1];
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
	int /*long*/ eventPtr = OS.gtk_get_current_event ();
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
