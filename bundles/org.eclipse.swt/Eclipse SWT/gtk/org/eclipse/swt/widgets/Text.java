/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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

/**
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify text.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CENTER, LEFT, MULTI, SINGLE, RIGHT, READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Verify</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles MULTI and SINGLE may be specified. 
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Text extends Scrollable {
	int bufferHandle, tabs = 8, lastEventTime = 0;
	boolean doubleClick;
	
	static final int INNER_BORDER = 2;
	static final int ITER_SIZEOF = 56;
	
	public final static int LIMIT;
	public final static String DELIMITER;
	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0x7FFFFFFF;
		DELIMITER = "\n";
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
 * @see SWT#SINGLE
 * @see SWT#MULTI
 * @see SWT#READ_ONLY
 * @see SWT#WRAP
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Text (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	style = checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
	if ((style & SWT.SINGLE) != 0) style &= ~(SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
	if ((style & SWT.WRAP) != 0) style |= SWT.MULTI;
	if ((style & SWT.MULTI) != 0) style &= ~SWT.PASSWORD;
	if ((style & (SWT.SINGLE | SWT.MULTI)) != 0) return style;
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
		return style | SWT.MULTI;
	}
	return style | SWT.SINGLE;
}

void createHandle (int index) {
	state |= HANDLE | MENU;
	int parentHandle = parent.parentingHandle ();
	if ((style & SWT.SINGLE) != 0) {
		handle = OS.gtk_entry_new ();
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_container_add (parentHandle, handle);
		OS.gtk_editable_set_editable (handle, (style & SWT.READ_ONLY) == 0);
		OS.gtk_entry_set_has_frame (handle, (style & SWT.BORDER) != 0);
		OS.gtk_entry_set_visibility (handle, (style & SWT.PASSWORD) == 0);
	} else {
		fixedHandle = OS.gtk_fixed_new ();
		if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_fixed_set_has_window (fixedHandle, true);
		scrolledHandle = OS.gtk_scrolled_window_new (0, 0);
		if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
		handle = OS.gtk_text_view_new ();
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		bufferHandle = OS.gtk_text_view_get_buffer (handle);
		if (bufferHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_container_add (parentHandle, fixedHandle);
		OS.gtk_container_add (fixedHandle, scrolledHandle);
		OS.gtk_container_add (scrolledHandle, handle);
		OS.gtk_widget_show (fixedHandle);
		OS.gtk_widget_show (scrolledHandle);
		OS.gtk_text_view_set_editable (handle, (style & SWT.READ_ONLY) == 0);
		if ((style & SWT.WRAP) != 0) OS.gtk_text_view_set_wrap_mode (handle, OS.GTK_WRAP_WORD);
		int hsp = (style & SWT.H_SCROLL) != 0 ? OS.GTK_POLICY_ALWAYS : OS.GTK_POLICY_NEVER;
		int vsp = (style & SWT.V_SCROLL) != 0 ? OS.GTK_POLICY_ALWAYS : OS.GTK_POLICY_NEVER;
		OS.gtk_scrolled_window_set_policy (scrolledHandle, hsp, vsp);
		if ((style & SWT.BORDER) != 0) {
			OS.gtk_scrolled_window_set_shadow_type (scrolledHandle, OS.GTK_SHADOW_ETCHED_IN);
		}
		int just = OS.GTK_JUSTIFY_LEFT;
		if ((style & SWT.CENTER) != 0) just = OS.GTK_JUSTIFY_CENTER; 
		if ((style & SWT.RIGHT) != 0) just = OS.GTK_JUSTIFY_RIGHT;
		OS.gtk_text_view_set_justification (handle, just);
	}
	OS.gtk_widget_show (handle);
}

void createWidget (int index) {
	super.createWidget (index);
	doubleClick = true;
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
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void append (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (null, string, false);
	if ((style & SWT.SINGLE) != 0) {
		OS.gtk_editable_insert_text (handle, buffer, buffer.length, new int[]{-1});
		OS.gtk_editable_set_position (handle, -1);
	} else {
		byte [] position =  new byte [ITER_SIZEOF];
		OS.gtk_text_buffer_get_end_iter (bufferHandle, position);
		OS.gtk_text_buffer_insert (bufferHandle, position, buffer, buffer.length);
		OS.gtk_text_buffer_place_cursor (bufferHandle, position);
		int mark = OS.gtk_text_buffer_get_insert (bufferHandle);
		OS.gtk_text_view_scroll_mark_onscreen (handle, mark);
	}
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
	if ((style & SWT.SINGLE) != 0) {
		int position = OS.gtk_editable_get_position (handle);
		OS.gtk_editable_select_region (handle, position, position);
	} else {
		byte [] position = new byte [ITER_SIZEOF];
		int insertMark = OS.gtk_text_buffer_get_insert (bufferHandle);
		int selectionMark = OS.gtk_text_buffer_get_selection_bound (bufferHandle);
		OS.gtk_text_buffer_get_iter_at_mark (bufferHandle, position, insertMark);
		OS.gtk_text_buffer_move_mark (bufferHandle, selectionMark, position);
		OS.gtk_text_buffer_move_mark (bufferHandle, insertMark, position);
	}
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	int xborder = 0, yborder = 0;
	int[] w = new int [1], h = new int [1];
	if ((style & SWT.SINGLE) != 0) {
		int layout = OS.gtk_entry_get_layout (handle);
		OS.pango_layout_get_size (layout, w, h);
		if ((style & SWT.BORDER) != 0) {
			int style = OS.gtk_widget_get_style (handle);
			xborder += OS.gtk_style_get_xthickness (style);
			yborder += OS.gtk_style_get_ythickness (style);
		}
		xborder += INNER_BORDER;
		yborder += INNER_BORDER;
	} else {
		byte [] start =  new byte [ITER_SIZEOF], end  =  new byte [ITER_SIZEOF];
		OS.gtk_text_buffer_get_bounds (bufferHandle, start, end);
		int text = OS.gtk_text_buffer_get_text (bufferHandle, start, end, true);
		int layout = OS.gtk_widget_create_pango_layout (handle, text);
		OS.g_free (text);
		OS.pango_layout_set_width (layout, wHint * OS.PANGO_SCALE);
		OS.pango_layout_get_size (layout, w, h);
		OS.g_object_unref (layout);
		int borderWidth = OS.gtk_container_get_border_width (handle);  
		xborder += borderWidth;
		yborder += borderWidth;
	}
	int [] property = new int [1];
	OS.gtk_widget_style_get (handle, OS.interior_focus, property, 0);
	if (property [0] != 0) {
		OS.gtk_widget_style_get (handle, OS.focus_line_width, property, 0);
		xborder += property [0];
		yborder += property [0];
	}
	int width = OS.PANGO_PIXELS (w [0]);
	int height = OS.PANGO_PIXELS (h [0]);
	width = wHint == SWT.DEFAULT ? width : wHint;
	height = hHint == SWT.DEFAULT ? height : hHint;
	width += 2 * xborder;
	height += 2 * yborder;
	Rectangle trim = computeTrim (0, 0, width, height);
	return new Point (trim.width, trim.height);
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
	if ((style & SWT.SINGLE) != 0) {
		OS.gtk_editable_copy_clipboard (handle);
	} else {
		int clipboard = OS.gtk_clipboard_get (OS.GDK_NONE);
		OS.gtk_text_buffer_copy_clipboard (bufferHandle, clipboard);
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
	if ((style & SWT.SINGLE) != 0) {
		OS.gtk_editable_cut_clipboard (handle);
	} else {
		int clipboard = OS.gtk_clipboard_get (OS.GDK_NONE);
		OS.gtk_text_buffer_cut_clipboard (bufferHandle, clipboard, OS.gtk_text_view_get_editable (handle));
	}
}

void deregister () {
	super.deregister ();
	if (bufferHandle != 0) display.removeWidget (bufferHandle);
	int imContext = imContext ();
	if (imContext != 0) display.removeWidget (imContext);
}

GdkColor getBackgroundColor () {
	return getBaseColor ();
}

/**
 * Gets the line number of the caret.
 * <p>
 * The line number of the caret is returned.
 * </p>
 *
 * @return the line number
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCaretLineNumber () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return 1;
	byte [] position = new byte [ITER_SIZEOF];
	int mark = OS.gtk_text_buffer_get_insert (bufferHandle);
	OS.gtk_text_buffer_get_iter_at_mark (bufferHandle, position, mark);
	return OS.gtk_text_iter_get_line (position);
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
	if ((style & SWT.SINGLE) != 0) return new Point (0, 0);
	byte [] position = new byte [ITER_SIZEOF];
	int mark = OS.gtk_text_buffer_get_insert (bufferHandle);
	OS.gtk_text_buffer_get_iter_at_mark (bufferHandle, position, mark);
	GdkRectangle rect = new GdkRectangle ();
	OS.gtk_text_view_get_iter_location (handle, position, rect);
	int [] x = new int [1];
	int [] y  = new int [1];
	OS.gtk_text_view_buffer_to_window_coords (handle, OS.GTK_TEXT_WINDOW_TEXT, rect.x, rect.y, x, y);
	return new Point (x [0], y [0]);
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
	if ((style & SWT.SINGLE) != 0)  {
		return OS.gtk_editable_get_position (handle);
	}
	byte [] position = new byte [ITER_SIZEOF];
	int mark = OS.gtk_text_buffer_get_insert (bufferHandle);
	OS.gtk_text_buffer_get_iter_at_mark (bufferHandle, position, mark);
	return OS.gtk_text_iter_get_offset (position);
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
	if ((style & SWT.SINGLE) != 0) return getText ().length ();
	return OS.gtk_text_buffer_get_char_count (bufferHandle);
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
	if ((style & SWT.SINGLE) != 0) {
		if (!OS.gtk_entry_get_visibility (handle)) {
			return OS.gtk_entry_get_invisible_char (handle);
		}
	}
	return '\0';
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
	if ((style & SWT.SINGLE) != 0) {
		return OS.gtk_editable_get_editable (handle);
	}
	return OS.gtk_text_view_get_editable (handle);
}

GdkColor getForegroundColor () {
	return getTextColor ();
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
	if ((style & SWT.SINGLE) != 0) return 1;
	return OS.gtk_text_buffer_get_line_count (bufferHandle);
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
	return "\n";
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
	return fontHeight (getFontDescription (), handle);
}

/**
 * Returns the orientation of the receiver.
 *
 * @return the orientation bit.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public int getOrientation () {
	checkWidget();
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
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
	if ((style & SWT.SINGLE) != 0) {
		int [] start = new int [1];
		int [] end = new int [1];
		OS.gtk_editable_get_selection_bounds (handle, start, end);
		return new Point (start [0], end [0]);
	}
	byte [] start =  new byte [ITER_SIZEOF];
	byte [] end =  new byte [ITER_SIZEOF];
	OS.gtk_text_buffer_get_selection_bounds (bufferHandle, start, end);
	return new Point (OS.gtk_text_iter_get_offset (start), OS.gtk_text_iter_get_offset (end));
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
	return Math.abs (selection.y - selection.x);
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
	Point selection = getSelection ();
	return getText ().substring(selection.x, selection.y);
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
	byte[] buffer = Converter.wcsToMbcs(null, " ", true);
	int layout = OS.gtk_widget_create_pango_layout (handle, buffer);
	int [] width = new int [1];
	int [] height = new int [1];
	OS.pango_layout_get_size (layout, width, height);
	OS.g_object_unref (layout);
	return width [0] * tabs;
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
	int address;
	if ((style & SWT.SINGLE) != 0) {
		address = OS.gtk_entry_get_text (handle);
	} else {
		byte [] start =  new byte [ITER_SIZEOF];
		byte [] end =  new byte [ITER_SIZEOF];
		OS.gtk_text_buffer_get_bounds (bufferHandle, start, end);
		address = OS.gtk_text_buffer_get_text (bufferHandle, start, end, true);
	}
	if (address == 0) return "";
	int length = OS.strlen (address);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, address, length);
	if ((style & SWT.MULTI) != 0) OS.g_free (address);
	return new String (Converter.mbcsToWcs (null, buffer));
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
	if (!(start <= end && 0 <= end)) return "";
	start = Math.max (0, start);
	int address;
	if ((style & SWT.SINGLE) != 0) {
		address = OS.gtk_editable_get_chars (handle, start, end + 1);
	} else {
		int length = OS.gtk_text_buffer_get_char_count (bufferHandle);
		end = Math.min (end, length - 1);
		byte [] startIter =  new byte [ITER_SIZEOF];
		byte [] endIter =  new byte [ITER_SIZEOF];
		OS.gtk_text_buffer_get_iter_at_offset (bufferHandle, startIter, start);
		OS.gtk_text_buffer_get_iter_at_offset (bufferHandle, endIter, end + 1);
		address = OS.gtk_text_buffer_get_text (bufferHandle, startIter, endIter, true);
	}
	if (address == 0) error (SWT.ERROR_CANNOT_GET_TEXT);
	int length = OS.strlen (address);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, address, length);
	OS.g_free (address);
	return new String (Converter.mbcsToWcs (null, buffer));
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
	if ((style & SWT.MULTI) != 0) return LIMIT;
	int limit = OS.gtk_entry_get_max_length (handle);
	return limit == 0 ? 0xFFFF : limit;
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
	if ((style & SWT.SINGLE) != 0) return 0;
	byte [] position = new byte [ITER_SIZEOF];
	GdkRectangle rect = new GdkRectangle ();
	OS.gtk_text_view_get_visible_rect (handle, rect);
	OS.gtk_text_view_get_line_at_y (handle, position, rect.y, null);
	return OS.gtk_text_iter_get_line (position);
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
	if ((style & SWT.SINGLE) != 0) return 0;
	byte [] position = new byte [ITER_SIZEOF];
	GdkRectangle rect = new GdkRectangle ();
	OS.gtk_text_view_get_visible_rect (handle, rect);
	int [] lineTop = new int[1];
	OS.gtk_text_view_get_line_at_y (handle, position, rect.y, lineTop);
	return lineTop [0];
}

int gtk_activate (int widget) {
	postEvent (SWT.DefaultSelection);
	return 0;
}

int gtk_button_press_event (int widget, int event) {
	int result =  super.gtk_button_press_event (widget, event);
	if (result != 0) return result;
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, event, GdkEventButton.sizeof);
	if (!doubleClick) {
		switch (gdkEvent.type) {
			case OS.GDK_2BUTTON_PRESS:
			case OS.GDK_3BUTTON_PRESS:
				return 1;
		}
	}
	return result;
}

int gtk_changed (int widget) {
	sendEvent (SWT.Modify);
	return 0;
}

int gtk_commit (int imContext, int text) {
	if (text == 0) return 0;
	if ((style & SWT.SINGLE) != 0) {
		if (!OS.gtk_editable_get_editable (handle)) return 0;
	}
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

int gtk_delete_range (int widget, int iter1, int iter2) {
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	byte [] startIter =  new byte [ITER_SIZEOF];
	byte [] endIter =  new byte [ITER_SIZEOF];
	OS.memmove (startIter, iter1, startIter.length);
	OS.memmove (endIter, iter2, endIter.length);
	int start = OS.gtk_text_iter_get_offset (startIter);
	int end = OS.gtk_text_iter_get_offset (endIter);
	String newText = verifyText ("", start, end);
	if (newText == null) {
		OS.g_signal_stop_emission_by_name (bufferHandle, OS.delete_range);
	}
	return 0;
}

int gtk_delete_text (int widget, int start_pos, int end_pos) {
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	String newText = verifyText ("", start_pos, end_pos);
	if (newText == null) {
		OS.g_signal_stop_emission_by_name (handle, OS.delete_text);
	}
	return 0;
}

int gtk_insert_text (int widget, int int0, int int1, int int2) {
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	if ((style & SWT.SINGLE) != 0) {
		if (int0 == 0 || int1==0) return 0;
		byte [] buffer = new byte [int1];
		OS.memmove (buffer, int0, buffer.length);
		String oldText = new String (Converter.mbcsToWcs (null, buffer));
		int [] position = new int [1];
		OS.memmove (position, int2, 4);
		if (position [0] == -1) position [0] = getCharCount ();
		String newText = verifyText (oldText, position [0], position [0]); //WRONG POSITION
		if (newText == null) {
			OS.g_signal_stop_emission_by_name (handle, OS.insert_text);
			return 0;
		}
		if (newText != oldText) {
			byte [] buffer3 = Converter.wcsToMbcs (null, newText, false);
			OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			OS.gtk_editable_insert_text (handle, buffer3, buffer3.length, position);
			OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			OS.g_signal_stop_emission_by_name (handle, OS.insert_text);
			return 0;
		}
	} else {
		byte [] iter = new byte [ITER_SIZEOF];
		OS.memmove (iter, int0, iter.length);
		int start = OS.gtk_text_iter_get_offset (iter);
		byte [] buffer = new byte [int2];
		OS.memmove (buffer, int1, buffer.length);
		String oldText = new String (Converter.mbcsToWcs (null, buffer));
		String newText = verifyText (oldText, start, start);
		if (newText == null) {
			OS.g_signal_stop_emission_by_name (bufferHandle, OS.insert_text);
			return 0;
		}
		if (newText != oldText) {
			byte [] buffer1 = Converter.wcsToMbcs (null, newText, false);
			OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			OS.gtk_text_buffer_insert (bufferHandle, int0, buffer1, buffer1.length);
			OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			OS.g_signal_stop_emission_by_name (bufferHandle, OS.insert_text);
			return 0;
		}
	}
	return 0;
}

int gtk_key_press_event (int widget, int event) {
	if (!hasFocus ()) return 0;
	GdkEventKey gdkEvent = new GdkEventKey ();
	OS.memmove (gdkEvent, event, GdkEventKey.sizeof);
	if (gdkEvent.time != lastEventTime) {
		lastEventTime = gdkEvent.time;
		int imContext = imContext ();
		if (imContext != 0) {
			if (OS.gtk_im_context_filter_keypress (imContext, event)) return 1;
		}
	}
	return super.gtk_key_press_event (widget, event);
}

int gtk_popup_menu (int widget) {
	int [] x = new int [1], y = new int [1];
	OS.gdk_window_get_pointer (0, x, y, null);
	return showMenu (x [0], y [0]) ? 1 : 0;
}

void hookEvents () {
	super.hookEvents();
	int windowProc2 = display.windowProc2;
	int windowProc3 = display.windowProc3;
	int windowProc4 = display.windowProc4;
	int windowProc5 = display.windowProc5;
	if ((style & SWT.SINGLE) != 0) {
		OS.g_signal_connect_after (handle, OS.changed, windowProc2, CHANGED);
		OS.g_signal_connect (handle, OS.insert_text, windowProc5, INSERT_TEXT);
		OS.g_signal_connect (handle, OS.delete_text, windowProc4, DELETE_TEXT);
		OS.g_signal_connect (handle, OS.activate, windowProc2, ACTIVATE);
	} else {
		OS.g_signal_connect (bufferHandle, OS.changed, windowProc2, CHANGED);
		OS.g_signal_connect (bufferHandle, OS.insert_text, windowProc5, INSERT_TEXT);
		OS.g_signal_connect (bufferHandle, OS.delete_range, windowProc4, DELETE_RANGE);
	}
	int imContext = imContext ();
	if (imContext != 0) {
		OS.g_signal_connect (imContext, OS.commit, windowProc3, COMMIT);
		int id = OS.g_signal_lookup (OS.commit, OS.gtk_im_context_get_type ());
		int mask =  OS.G_SIGNAL_MATCH_DATA | OS.G_SIGNAL_MATCH_ID;
		OS.g_signal_handlers_block_matched (imContext, mask, id, 0, 0, 0, handle);
	}
}

int imContext () {
	return (style & SWT.SINGLE) != 0 ? OS.GTK_ENTRY_IM_CONTEXT (handle) : OS.GTK_TEXTVIEW_IM_CONTEXT (handle);
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
	byte [] buffer = Converter.wcsToMbcs (null, string, false);
	if ((style & SWT.SINGLE) != 0) {
		int [] start = new int [1], end = new int [1];
		OS.gtk_editable_get_selection_bounds (handle, start, end);
		OS.gtk_editable_delete_selection (handle);
		OS.gtk_editable_insert_text (handle, buffer, buffer.length, start);
	} else {
		byte [] start =  new byte [ITER_SIZEOF];
		byte [] end =  new byte [ITER_SIZEOF];
		if (OS.gtk_text_buffer_get_selection_bounds (bufferHandle, start, end)) {
			OS.gtk_text_buffer_delete (bufferHandle, start, end);
		}
		OS.gtk_text_buffer_insert (bufferHandle, start, buffer, buffer.length);
		OS.gtk_text_buffer_place_cursor (bufferHandle, start);
		int mark = OS.gtk_text_buffer_get_insert (bufferHandle);
		OS.gtk_text_view_scroll_mark_onscreen (handle, mark);
	}
}

int paintWindow () {
	OS.gtk_widget_realize (handle);
	if ((style & SWT.SINGLE) != 0)  return OS.GTK_WIDGET_WINDOW (handle);
	return OS.gtk_text_view_get_window (handle, OS.GTK_TEXT_WINDOW_TEXT);
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
	if ((style & SWT.SINGLE) != 0) {
		OS.gtk_editable_paste_clipboard (handle);
	} else {
		int clipboard = OS.gtk_clipboard_get (OS.GDK_NONE);
		OS.gtk_text_buffer_paste_clipboard (bufferHandle, clipboard, null, OS.gtk_text_view_get_editable (handle));
	}
}

void register () {
	super.register ();
	if (bufferHandle != 0) display.addWidget (bufferHandle, this);
	int imContext = imContext ();
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
	if ((style & SWT.SINGLE) != 0) {
		OS.gtk_editable_select_region (handle, 0, -1);
	} else {
		byte [] start =  new byte [ITER_SIZEOF];
		byte [] end =  new byte [ITER_SIZEOF];
		OS.gtk_text_buffer_get_iter_at_offset (bufferHandle, start, 0);
		OS.gtk_text_buffer_get_end_iter (bufferHandle, end);
		int insertMark = OS.gtk_text_buffer_get_insert (bufferHandle);
		int selectionMark = OS.gtk_text_buffer_get_selection_bound (bufferHandle);
		OS.gtk_text_buffer_move_mark (bufferHandle, selectionMark, start);
		OS.gtk_text_buffer_move_mark (bufferHandle, insertMark, end);
	}
}

void setBackgroundColor (GdkColor color) {
	super.setBackgroundColor (color);
	OS.gtk_widget_modify_base (handle, 0, color);
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
 * text is changed by the programmer. Setting
 * the echo character to '\0' clears the echo
 * character and redraws the original text.
 * If for any reason the echo character is invalid,
 * the default echo character for the platform
 * is used.
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
	if ((style & SWT.SINGLE) != 0) {
		OS.gtk_entry_set_visibility (handle, echo == '\0');
		OS.gtk_entry_set_invisible_char (handle, echo);
	}
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
	if ((style & SWT.SINGLE) != 0) {
		OS.gtk_editable_set_editable (handle, editable);
	} else {
		OS.gtk_text_view_set_editable (handle, editable);
	}
}

void setFontDescription (int font) {
	super.setFontDescription (font);
	setTabStops (tabs);
}

void setForegroundColor (GdkColor color) {
	super.setForegroundColor (color);
	OS.gtk_widget_modify_text (handle, 0, color);
}

/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.LEFT_TO_RIGHT</code>.
 * <p>
 *
 * @param orientation new orientation bit
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public void setOrientation (int orientation) {
	checkWidget();
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
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int start) {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) {
		OS.gtk_editable_set_position (handle, start);
	} else {
		byte [] position =  new byte [ITER_SIZEOF];
		OS.gtk_text_buffer_get_iter_at_offset (bufferHandle, position, start);
		OS.gtk_text_buffer_place_cursor (bufferHandle, position);
		int mark = OS.gtk_text_buffer_get_insert (bufferHandle);
		OS.gtk_text_view_scroll_mark_onscreen (handle, mark);
	}
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
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int start, int end) {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) { 
		OS.gtk_editable_set_position (handle, start);
		OS.gtk_editable_select_region (handle, start, end);
	} else {
		byte [] startIter =  new byte [ITER_SIZEOF];
		byte [] endIter =  new byte [ITER_SIZEOF];
		OS.gtk_text_buffer_get_iter_at_offset (bufferHandle, startIter, start);
		OS.gtk_text_buffer_get_iter_at_offset (bufferHandle, endIter, end);
		int insertMark = OS.gtk_text_buffer_get_insert (bufferHandle);
		int selectionMark = OS.gtk_text_buffer_get_selection_bound (bufferHandle);
		OS.gtk_text_buffer_move_mark (bufferHandle, selectionMark, startIter);
		OS.gtk_text_buffer_move_mark (bufferHandle, insertMark, endIter);
	}
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
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
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
	if ((style & SWT.SINGLE) != 0) return;
	int tabWidth = getTabWidth (tabs);
	int tabArray = OS.pango_tab_array_new (1, false);
	OS.pango_tab_array_set_tab (tabArray, 0, OS.PANGO_TAB_LEFT, tabWidth);
	OS.gtk_text_view_set_tabs (handle, tabArray);
	OS.pango_tab_array_free (tabArray);
}

/**
 * Sets the contents of the receiver to the given string. If the receiver has style
 * SINGLE and the argument contains multiple lines of text, the result of this
 * operation is undefined and may vary from platform to platform.
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
	/*
	* Feature in gtk.  When text is set in gtk, separate events are fired for the deletion and 
	* insertion of the text.  This is not wrong, but is inconsistent with other platforms.  The
	* fix is to block the firing of these events and fire them ourselves in a consistent manner. 
	*/
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		string = verifyText (string, 0, getCharCount ());
		if (string == null) return;
	}
	byte [] buffer = Converter.wcsToMbcs (null, string, false);
	if ((style & SWT.SINGLE) != 0) {
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
		OS.gtk_editable_delete_text (handle, 0, -1);
		int [] position = new int [1];
		OS.gtk_editable_insert_text (handle, buffer, buffer.length, position);
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
		OS.gtk_editable_set_position (handle, 0);
	} else {
		byte [] position =  new byte [ITER_SIZEOF];
		OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_RANGE);
		OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
		OS.gtk_text_buffer_set_text (bufferHandle, buffer, buffer.length);
		OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_RANGE);
		OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
		OS.gtk_text_buffer_get_iter_at_offset (bufferHandle, position, 0);
		OS.gtk_text_buffer_place_cursor (bufferHandle, position);
		int mark = OS.gtk_text_buffer_get_insert (bufferHandle);
		OS.gtk_text_view_scroll_mark_onscreen (handle, mark);
	}
	sendEvent (SWT.Modify);
}

/**
 * Sets the maximum number of characters that the receiver
 * is capable of holding to be the argument.
 * <p>
 * Instead of trying to set the text limit to zero, consider
 * creating a read-only text widget.
 * </p><p>
 * To reset this value to the default, use <code>setTextLimit(Text.LIMIT)</code>.
 * </p>
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
	if ((style & SWT.SINGLE) != 0) OS.gtk_entry_set_max_length (handle, limit);
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
	if ((style & SWT.SINGLE) != 0) return;
	byte [] position = new byte [ITER_SIZEOF];
	OS.gtk_text_buffer_get_iter_at_line (bufferHandle, position, index);
	OS.gtk_text_view_scroll_to_iter (handle, position, 0, true, 0, 0);
}

/**
 * Shows the selection.
 * <p>
 * If the selection is already showing
 * in the receiver, this method simply returns.  Otherwise,
 * lines are scrolled until the selection is visible.
 * </p>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void showSelection () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return;
	int mark = OS.gtk_text_buffer_get_selection_bound (bufferHandle);
	OS.gtk_text_view_scroll_mark_onscreen (handle, mark);
	mark = OS.gtk_text_buffer_get_insert (bufferHandle);
	OS.gtk_text_view_scroll_mark_onscreen (handle, mark);
}

boolean translateTraversal (GdkEventKey keyEvent) {
	int key = keyEvent.keyval;
	switch (key) {
		case OS.GDK_KP_Enter:
		case OS.GDK_Return: {
			int imContext =  imContext ();
			if (imContext != 0) {
				int [] preeditString = new int [1];
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

int traversalCode (int key, GdkEventKey event) {
	int bits = super.traversalCode (key, event);
	if ((style & SWT.READ_ONLY) != 0)  return bits;
	if ((style & SWT.MULTI) != 0) {
		bits &= ~SWT.TRAVERSE_RETURN;
		if (key == OS.GDK_Tab && event != null) {
			boolean next = (event.state & OS.GDK_SHIFT_MASK) == 0;
			if (next && (event.state & OS.GDK_CONTROL_MASK) == 0) {
				bits &= ~(SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS);
			}
		}
	}
	return bits;
}

String verifyText (String string, int start, int end) {
	if (string.length () == 0 && start == end) return null;
	Event event = new Event ();
	event.text = string;
	event.start = start;
	event.end = end;
	int eventPtr = OS.gtk_get_current_event ();
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
