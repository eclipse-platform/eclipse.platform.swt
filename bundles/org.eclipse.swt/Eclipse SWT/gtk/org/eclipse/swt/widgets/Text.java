/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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
 * objects that allow the user to enter and modify text.
 * Text controls can be either single or multi-line.
 * When a text control is created with a border, the
 * operating system includes a platform specific inset
 * around the contents of the control.  When created
 * without a border, an effort is made to remove the
 * inset such that the preferred size of the control
 * is the same size as the contents.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CENTER, ICON_CANCEL, ICON_SEARCH, LEFT, MULTI, PASSWORD, SEARCH, SINGLE, RIGHT, READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Verify, OrientationChange</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles MULTI and SINGLE may be specified,
 * and only one of the styles LEFT, CENTER, and RIGHT may be specified.
 * </p>
 * <p>
 * Note: The styles ICON_CANCEL and ICON_SEARCH are hints used in combination with SEARCH.
 * When the platform supports the hint, the text control shows these icons.  When an icon
 * is selected, a default selection event is sent with the detail field set to one of
 * ICON_CANCEL or ICON_SEARCH.  Normally, application code does not need to check the
 * detail.  In the case of ICON_CANCEL, the text is cleared before the default selection
 * event is sent causing the application to search for an empty string.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#text">Text snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Text extends Scrollable {
	int /*long*/ bufferHandle;
	int tabs = 8, lastEventTime = 0;
	int /*long*/ gdkEventKey = 0;
	int fixStart = -1, fixEnd = -1;
	boolean doubleClick;
	String message = "";
	
	static final char LTR_MARK = '\u200e';
	static final char RTL_MARK = '\u200f';
	int[] segments;

	static final int ITER_SIZEOF = OS.GtkTextIter_sizeof();
	static final int SPACE_FOR_CURSOR = 1;
	
	/**
	* The maximum number of characters that can be entered
	* into a text widget.
	* <p>
	* Note that this value is platform dependent, based upon
	* the native widget implementation.
	* </p>
	*/
	public final static int LIMIT;
	/**
	* The delimiter used by multi-line text widgets.  When text
	* is queried and from the widget, it will be delimited using
	* this delimiter.
	*/
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
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see SWT#PASSWORD
 * @see SWT#SEARCH
 * @see SWT#ICON_SEARCH
 * @see SWT#ICON_CANCEL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Text (Composite parent, int style) {
	super (parent, checkStyle (style));
	if (OS.GTK_VERSION >= OS.VERSION (2, 16, 0)) {
		if ((style & SWT.SEARCH) != 0) {
			/*
			 * Ensure that SWT.ICON_CANCEL and ICON_SEARCH are set.
			 * NOTE: ICON_CANCEL has the same value as H_SCROLL and
			 * ICON_SEARCH has the same value as V_SCROLL so it is
			 * necessary to first clear these bits to avoid a scroll
			 * bar and then reset the bit using the original style
			 * supplied by the programmer.
			 */
			if ((style & SWT.ICON_CANCEL) != 0) {
				this.style |= SWT.ICON_CANCEL;
				OS.gtk_entry_set_icon_from_stock (handle, OS.GTK_ENTRY_ICON_SECONDARY, OS.GTK_STOCK_CLEAR);
				OS.gtk_entry_set_icon_sensitive (handle, OS.GTK_ENTRY_ICON_SECONDARY, false);
			}
			if ((style & SWT.ICON_SEARCH) != 0) {
				this.style |= SWT.ICON_SEARCH;
				OS.gtk_entry_set_icon_from_stock (handle, OS.GTK_ENTRY_ICON_PRIMARY, OS.GTK_STOCK_FIND);
			}
		}
	}
}

static int checkStyle (int style) {
	if ((style & SWT.SEARCH) != 0) {
		style |= SWT.SINGLE | SWT.BORDER;
		style &= ~SWT.PASSWORD;
		/* 
		* NOTE: ICON_CANCEL has the same value as H_SCROLL and
		* ICON_SEARCH has the same value as V_SCROLL so they are
		* cleared because SWT.SINGLE is set. 
		*/
	}
	if ((style & SWT.SINGLE) != 0 && (style & SWT.MULTI) != 0) {
		style &= ~SWT.MULTI;
	}
	style = checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
	if ((style & SWT.SINGLE) != 0) style &= ~(SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
	if ((style & SWT.WRAP) != 0) {
		style |= SWT.MULTI;
		style &= ~SWT.H_SCROLL;
	}
	if ((style & SWT.MULTI) != 0) style &= ~SWT.PASSWORD;
	if ((style & (SWT.SINGLE | SWT.MULTI)) != 0) return style;
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) return style | SWT.MULTI;
	return style | SWT.SINGLE;
}

void createHandle (int index) {
	state |= HANDLE | MENU;
	if ((style & SWT.READ_ONLY) != 0) {
		if ((style & (SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
			state |= THEME_BACKGROUND;
		}
	}
	fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	if ((style & SWT.SINGLE) != 0) {
		handle = OS.gtk_entry_new ();
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_container_add (fixedHandle, handle);
		OS.gtk_editable_set_editable (handle, (style & SWT.READ_ONLY) == 0);
		OS.gtk_entry_set_has_frame (handle, (style & SWT.BORDER) != 0);
		OS.gtk_entry_set_visibility (handle, (style & SWT.PASSWORD) == 0);
		float alignment = 0.0f;
		if ((style & SWT.CENTER) != 0) alignment = 0.5f;
		if ((style & SWT.RIGHT) != 0) alignment = 1.0f;
		if (alignment > 0.0f) {
			OS.gtk_entry_set_alignment (handle, alignment);
		}
	} else {
		scrolledHandle = OS.gtk_scrolled_window_new (0, 0);
		if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
		handle = OS.gtk_text_view_new ();
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		bufferHandle = OS.gtk_text_view_get_buffer (handle);
		if (bufferHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_container_add (fixedHandle, scrolledHandle);
		OS.gtk_container_add (scrolledHandle, handle);
		OS.gtk_text_view_set_editable (handle, (style & SWT.READ_ONLY) == 0);
		if ((style & SWT.WRAP) != 0) OS.gtk_text_view_set_wrap_mode (handle, OS.GTK_VERSION < OS.VERSION (2, 4, 0) ? OS.GTK_WRAP_WORD : OS.GTK_WRAP_WORD_CHAR);
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

public void addSegmentListener (SegmentListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	addListener (SWT.GetSegments, new TypedListener (listener));
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is not called for texts.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed in a single-line text,
 * or when ENTER is pressed in a search text. If the receiver has the <code>SWT.SEARCH | SWT.CANCEL</code> style
 * and the user cancels the search, the event object detail field contains the value <code>SWT.CANCEL</code>.
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
	if (segments != null) clearSegments (true);
	if ((style & SWT.SINGLE) != 0) {
		OS.gtk_editable_insert_text (handle, buffer, buffer.length, new int[]{-1});
		OS.gtk_editable_set_position (handle, -1);
	} else {
		byte [] position =  new byte [ITER_SIZEOF];
		OS.gtk_text_buffer_get_end_iter (bufferHandle, position);
		OS.gtk_text_buffer_insert (bufferHandle, position, buffer, buffer.length);
		OS.gtk_text_buffer_place_cursor (bufferHandle, position);
		int /*long*/ mark = OS.gtk_text_buffer_get_insert (bufferHandle);
		OS.gtk_text_view_scroll_mark_onscreen (handle, mark);
	}
	if (hooks (SWT.GetSegments) || filters (SWT.GetSegments)) {
		applySegments ();
	}
}

void applySegments () {
	if (!hooks (SWT.GetSegments) && !filters (SWT.GetSegments)) return;
	Event event = new Event ();
	String string = getText ();
	event.text = string;
	event.segments = segments;
	sendEvent (SWT.GetSegments, event);
	segments = event.segments;
	if (segments == null) return;
	int nSegments = segments.length;
	if (nSegments == 0) return;

	for (int i = 1, length = string == null ? 0 : string.length (); i < nSegments; i++) {
		if (event.segments [i] < event.segments [i - 1] || event.segments [i] > length) {
			SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	char[] segmentsChars = event.segmentsChars;
	char [] separator = { getOrientation () == SWT.RIGHT_TO_LEFT ? RTL_MARK : LTR_MARK };
	if ((style & SWT.SINGLE) != 0) {
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
		int limit = OS.gtk_entry_get_max_length (handle);
		if (limit != 0) OS.gtk_entry_set_max_length (handle, translateOffset (limit));
		int [] pos = new int [1];
		for (int i = 0; i < nSegments; i++) {
			pos [0] = segments [i] + i;
			if (segmentsChars != null && segmentsChars.length > i) {
				separator [0] = segmentsChars [i];
			}
			byte [] buffer = Converter.wcsToMbcs (null, separator, false);
			OS.gtk_editable_insert_text (handle, buffer, buffer.length, pos);
		}
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
	} else {
		OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEXT_BUFFER_INSERT_TEXT);
		byte [] pos = new byte [ITER_SIZEOF];
		for (int i = 0; i < nSegments; i++) {
			OS.gtk_text_buffer_get_iter_at_offset (bufferHandle, pos, segments[i] + i);
			if (segmentsChars != null && segmentsChars.length > i) {
				separator [0] = segmentsChars [i];
			}
			byte [] buffer = Converter.wcsToMbcs (null, separator, false);
			OS.gtk_text_buffer_insert (bufferHandle, pos, buffer, buffer.length);
		}
		OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEXT_BUFFER_INSERT_TEXT);
	}
}

void clearSegments (boolean applyText) {
	if (segments == null) return;
	int nSegments = segments.length;
	if (nSegments == 0) return;

	if ((style & SWT.SINGLE) != 0) {
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		if (applyText) {
			OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
			for (int i = 0; i < nSegments; i++) {
				OS.gtk_editable_delete_text (handle, segments[i], segments[i] + 1);
			}
			OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
		}
		int limit = OS.gtk_entry_get_max_length (handle);
		if (limit != 0) OS.gtk_entry_set_max_length (handle, untranslateOffset (limit));
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	} else if (applyText) {
		OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_RANGE);
		byte [] start = new byte [ITER_SIZEOF], end = new byte [ITER_SIZEOF];
		for (int i = 0; i < nSegments; i++) {
			OS.gtk_text_buffer_get_iter_at_offset (bufferHandle, start, segments[i]);
			OS.gtk_text_buffer_get_iter_at_offset (bufferHandle, end, segments[i] + 1);
			OS.gtk_text_buffer_delete (bufferHandle, start, end);
		}
		OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_RANGE);
		OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	}
	segments = null;
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
		int /*long*/ insertMark = OS.gtk_text_buffer_get_insert (bufferHandle);
		int /*long*/ selectionMark = OS.gtk_text_buffer_get_selection_bound (bufferHandle);
		OS.gtk_text_buffer_get_iter_at_mark (bufferHandle, position, insertMark);
		OS.gtk_text_buffer_move_mark (bufferHandle, selectionMark, position);
		OS.gtk_text_buffer_move_mark (bufferHandle, insertMark, position);
	}
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	int[] w = new int [1], h = new int [1];
	if ((style & SWT.SINGLE) != 0) {
		OS.gtk_widget_realize (handle);
		int /*long*/ layout = OS.gtk_entry_get_layout (handle);
		OS.pango_layout_get_size (layout, w, h);
	} else {
		byte [] start =  new byte [ITER_SIZEOF], end  =  new byte [ITER_SIZEOF];
		OS.gtk_text_buffer_get_bounds (bufferHandle, start, end);
		int /*long*/ text = OS.gtk_text_buffer_get_text (bufferHandle, start, end, true);
		int /*long*/ layout = OS.gtk_widget_create_pango_layout (handle, text);
		OS.g_free (text);
		OS.pango_layout_set_width (layout, wHint * OS.PANGO_SCALE);
		OS.pango_layout_get_size (layout, w, h);
		OS.g_object_unref (layout);
	}
	int width = OS.PANGO_PIXELS (w [0]);
	int height = OS.PANGO_PIXELS (h [0]);
	if ((style & SWT.SINGLE) != 0 && message.length () > 0) {
		byte [] buffer = Converter.wcsToMbcs (null, message, true);
		int /*long*/ layout = OS.gtk_widget_create_pango_layout (handle, buffer);
		OS.pango_layout_get_size (layout, w, h);
		OS.g_object_unref (layout);
		width = Math.max (width, OS.PANGO_PIXELS (w [0]));
	}
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	width = wHint == SWT.DEFAULT ? width : wHint;
	height = hHint == SWT.DEFAULT ? height : hHint;
	Rectangle trim = computeTrim (0, 0, width, height);
	return new Point (trim.width, trim.height);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	Rectangle trim = super.computeTrim (x, y, width, height);
	int xborder = 0, yborder = 0;
	if ((style & SWT.SINGLE) != 0) {
		if ((style & SWT.BORDER) != 0) {
			int /*long*/ style = OS.gtk_widget_get_style (handle);
			xborder += OS.gtk_style_get_xthickness (style);
			yborder += OS.gtk_style_get_ythickness (style);
		}
		GtkBorder innerBorder = Display.getEntryInnerBorder (handle);
		trim.x -= innerBorder.left;
		trim.y -= innerBorder.top;
		trim.width += innerBorder.left + innerBorder.right;
		trim.height += innerBorder.top + innerBorder.bottom;
	} else {
		int borderWidth = OS.gtk_container_get_border_width (handle);  
		xborder += borderWidth;
		yborder += borderWidth;
	}
	int [] property = new int [1];
	OS.gtk_widget_style_get (handle, OS.interior_focus, property, 0);
	if (property [0] == 0) {
		OS.gtk_widget_style_get (handle, OS.focus_line_width, property, 0);
		xborder += property [0];
		yborder += property [0];
	}
	trim.x -= xborder;
	trim.y -= yborder;
	trim.width += 2 * xborder;
	trim.height += 2 * yborder;
	trim.width += SPACE_FOR_CURSOR;
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
	if ((style & SWT.SINGLE) != 0) {
		OS.gtk_editable_copy_clipboard (handle);
	} else {
		int /*long*/ clipboard = OS.gtk_clipboard_get (OS.GDK_NONE);
		if (segments != null) {
			clearSegments (true);
			OS.gtk_text_buffer_copy_clipboard (bufferHandle, clipboard);
			applySegments ();
		} else {
			OS.gtk_text_buffer_copy_clipboard (bufferHandle, clipboard);
		}
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
		int /*long*/ clipboard = OS.gtk_clipboard_get (OS.GDK_NONE);
		if (hooks (SWT.GetSegments) || filters (SWT.GetSegments)) {
			if (segments != null) clearSegments (true);
			OS.gtk_text_buffer_cut_clipboard (bufferHandle, clipboard, OS.gtk_text_view_get_editable (handle));
			applySegments ();
		} else {
			OS.gtk_text_buffer_cut_clipboard (bufferHandle, clipboard, OS.gtk_text_view_get_editable (handle));
		}
	}
}

char [] deprocessText (char [] text, int start, int end) {
	if (text == null) return new char [0];
	if (start < 0) start = 0;
	int length = text.length;
	if (end == -1) end = start + length;
	if (segments != null && end > segments [0]) {
		int nSegments = segments.length;
		if (nSegments > 0 && start <= segments [nSegments - 1]) {
			int nLeadSegments = 0;
			while (start - nLeadSegments > segments [nLeadSegments]) nLeadSegments++;
			int segmentCount = nLeadSegments;
			for (int i = start; i < end; i++) {
				if (segmentCount < nSegments && i - segmentCount == segments [segmentCount]) {
					++segmentCount;
				} else {
					text [i - segmentCount + nLeadSegments - start] = text [i - start];
				}
			}
			length = end - start - segmentCount + nLeadSegments;
		}
	}
	if (start != 0 || end != start + length) {
		char [] newText = new char [length];
		System.arraycopy(text, 0, newText, 0, length);
		return newText;
	}
	return text;
}

void deregister () {
	super.deregister ();
	if (bufferHandle != 0) display.removeWidget (bufferHandle);
	int /*long*/ imContext = imContext ();
	if (imContext != 0) display.removeWidget (imContext);
}

boolean dragDetect (int x, int y, boolean filter, boolean dragOnTimeout, boolean [] consume) {
	if (filter) {
		int start = 0, end = 0;
		if ((style & SWT.SINGLE) != 0) {
			int [] s = new int [1], e = new int [1];
			OS.gtk_editable_get_selection_bounds (handle, s, e);
			start = s [0];
			end = e [0];
		} else {
			byte [] s = new byte [ITER_SIZEOF], e =  new byte [ITER_SIZEOF];
			OS.gtk_text_buffer_get_selection_bounds (bufferHandle, s, e);
			start = OS.gtk_text_iter_get_offset (s);
			end = OS.gtk_text_iter_get_offset (e);
		}
		if (start != end) {
			if (end < start) {
				int temp = end;
				end = start;
				start = temp;
			}
			int position = -1;
			if ((style & SWT.SINGLE) != 0) {
				int [] index = new int [1];
				int [] trailing = new int [1];
				int /*long*/ layout = OS.gtk_entry_get_layout (handle);
				OS.pango_layout_xy_to_index (layout, x * OS.PANGO_SCALE, y * OS.PANGO_SCALE, index, trailing);
				int /*long*/ ptr = OS.pango_layout_get_text (layout);
				position = (int)/*64*/OS.g_utf8_pointer_to_offset (ptr, ptr + index[0]) + trailing[0];
			} else {
				byte [] p = new byte [ITER_SIZEOF];
				OS.gtk_text_view_get_iter_at_location (handle, p, x, y);
				position = OS.gtk_text_iter_get_offset (p);
			}
			if (start <= position && position < end) {
				if (super.dragDetect (x, y, filter, dragOnTimeout, consume)) {
					if (consume != null) consume [0] = true;
					return true;
				}
			}
		}
		return false;
	}
	return super.dragDetect (x, y, filter, dragOnTimeout, consume);
}

int /*long*/ eventWindow () {
	return paintWindow ();
}

boolean filterKey (int keyval, int /*long*/ event) {
	int time = OS.gdk_event_get_time (event);
	if (time != lastEventTime) {
		lastEventTime = time;
		int /*long*/ imContext = imContext ();
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
		int /*long*/ imContext = imContext ();
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
	if ((style & SWT.MULTI) != 0) return super.getBorderWidth ();
	int /*long*/ style = OS.gtk_widget_get_style (handle);
	if ((this.style & SWT.BORDER) != 0) {
		 return OS.gtk_style_get_xthickness (style);
	}
	return 0;
}

/**
 * Returns the line number of the caret.
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
	if ((style & SWT.SINGLE) != 0) return 0;
	byte [] position = new byte [ITER_SIZEOF];
	int /*long*/ mark = OS.gtk_text_buffer_get_insert (bufferHandle);
	OS.gtk_text_buffer_get_iter_at_mark (bufferHandle, position, mark);
	return OS.gtk_text_iter_get_line (position);
}

/**
 * Returns a point describing the receiver's location relative
 * to its parent (or its display if its parent is null).
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
	if ((style & SWT.SINGLE) != 0) {
		int index = OS.gtk_editable_get_position (handle);
		if (OS.GTK_VERSION >= OS.VERSION (2, 6, 0)) {
			index = OS.gtk_entry_text_index_to_layout_index (handle, index);
		}
		int [] offset_x = new int [1], offset_y = new int [1];
		OS.gtk_entry_get_layout_offsets (handle, offset_x, offset_y);
		int /*long*/ layout = OS.gtk_entry_get_layout (handle);
		PangoRectangle pos = new PangoRectangle ();
		OS.pango_layout_index_to_pos (layout, index, pos);
		int x = offset_x [0] + OS.PANGO_PIXELS (pos.x) - getBorderWidth ();
		int y = offset_y [0] + OS.PANGO_PIXELS (pos.y);
		return new Point (x, y);
	}
	byte [] position = new byte [ITER_SIZEOF];
	int /*long*/ mark = OS.gtk_text_buffer_get_insert (bufferHandle);
	OS.gtk_text_buffer_get_iter_at_mark (bufferHandle, position, mark);
	GdkRectangle rect = new GdkRectangle ();
	OS.gtk_text_view_get_iter_location (handle, position, rect);
	int [] x = new int [1];
	int [] y  = new int [1];
	OS.gtk_text_view_buffer_to_window_coords (handle, OS.GTK_TEXT_WINDOW_TEXT, rect.x, rect.y, x, y);
	return new Point (x [0], y [0]);
}

/**
 * Returns the character position of the caret.
 * <p>
 * Indexing is zero based.
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
	int result;
	if ((style & SWT.SINGLE) != 0)  {
		int /*long*/ ptr = OS.gtk_entry_get_text (handle);
		result = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, OS.gtk_editable_get_position (handle));
	} else {
		byte [] position = new byte [ITER_SIZEOF];
		int /*long*/ mark = OS.gtk_text_buffer_get_insert (bufferHandle);
		OS.gtk_text_buffer_get_iter_at_mark (bufferHandle, position, mark);
		byte [] zero = new byte [ITER_SIZEOF];
		OS.gtk_text_buffer_get_iter_at_offset(bufferHandle, zero, 0);
		int /*long*/ ptr = OS.gtk_text_buffer_get_text (bufferHandle, zero, position, true);
		result = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, OS.gtk_text_iter_get_offset (position));
		OS.g_free (ptr);
	}
	if (segments != null) result = untranslateOffset (result);
	return result;
}

/**
 * Returns the number of characters.
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
	int result;
	if ((style & SWT.SINGLE) != 0) {
		int /*long*/ ptr = OS.gtk_entry_get_text (handle);
		result = (int)/*64*/OS.g_utf16_strlen (ptr, -1);
	} else {
		byte [] startIter =  new byte [ITER_SIZEOF];
		byte [] endIter =  new byte [ITER_SIZEOF];
		OS.gtk_text_buffer_get_bounds (bufferHandle, startIter, endIter);
		int /*long*/ ptr = OS.gtk_text_buffer_get_text (bufferHandle, startIter, endIter, true);
		result = (int)/*64*/OS.g_utf16_strlen(ptr, -1);
		OS.g_free (ptr);
	}
	if (segments != null) result = untranslateOffset (result);
	return result;
}

/**
 * Returns the double click enabled flag.
 * <p>
 * The double click flag enables or disables the
 * default action of the text widget when the user
 * double clicks.
 * </p>
 * 
 * @return whether or not double click is enabled
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
 * Returns the echo character.
 * <p>
 * The echo character is the character that is
 * displayed when the user enters text or the
 * text is changed by the programmer.
 * </p>
 * 
 * @return the echo character
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setEchoChar
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
 * Returns the editable state.
 *
 * @return whether or not the receiver is editable
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
 * Returns the number of lines.
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
 * Returns the line delimiter.
 *
 * @return a string that is the line delimiter
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #DELIMITER
 */
public String getLineDelimiter () {
	checkWidget ();
	return "\n";
}

/**
 * Returns the height of a line.
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
 * Returns the widget message.  The message text is displayed
 * as a hint for the user, indicating the purpose of the field.
 * <p>
 * Typically this is used in conjunction with <code>SWT.SEARCH</code>.
 * </p>
 * 
 * @return the widget message
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
public String getMessage () {
	checkWidget ();
	return message;
}

/**
 * Returns the orientation of the receiver, which will be one of the
 * constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 *
 * @return the orientation style
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public int getOrientation () {
	return super.getOrientation ();
}

/*public*/ int getPosition (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	int position = -1;
	if ((style & SWT.SINGLE) != 0) {
		int [] index = new int [1];
		int [] trailing = new int [1];
		int /*long*/ layout = OS.gtk_entry_get_layout (handle);
		OS.pango_layout_xy_to_index (layout, point.x * OS.PANGO_SCALE, point.y * OS.PANGO_SCALE, index, trailing);
		int /*long*/ ptr = OS.pango_layout_get_text (layout);
		position = (int)/*64*/OS.g_utf16_pointer_to_offset (ptr, ptr + index[0]) + trailing[0];
	} else {
		byte [] p = new byte [ITER_SIZEOF];
		OS.gtk_text_view_get_iter_at_location (handle, p, point.x, point.y);
		byte [] zero = new byte [ITER_SIZEOF];
		OS.gtk_text_buffer_get_iter_at_offset(bufferHandle, zero, 0);
		int /*long*/ ptr = OS.gtk_text_buffer_get_text (bufferHandle, zero, p, true);
		position = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, OS.gtk_text_iter_get_offset (p));
		OS.g_free (ptr);
	}
	if (segments != null) position = untranslateOffset(position);
	return position;
}

/**
 * Returns a <code>Point</code> whose x coordinate is the
 * character position representing the start of the selected
 * text, and whose y coordinate is the character position
 * representing the end of the selection. An "empty" selection
 * is indicated by the x and y coordinates having the same value.
 * <p>
 * Indexing is zero based.  The range of a selection is from
 * 0..N where N is the number of characters in the widget.
 * </p>
 *
 * @return a point representing the selection start and end
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSelection () {
	checkWidget ();
	Point selection;
	if ((style & SWT.SINGLE) != 0) {
		int [] start = new int [1];
		int [] end = new int [1];
		OS.gtk_editable_get_selection_bounds (handle, start, end);
		int /*long*/ ptr = OS.gtk_entry_get_text (handle);
		start[0] = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, start[0]);
		end[0] = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, end[0]);
		selection = new Point (start [0], end [0]);
	} else {
		byte [] startIter =  new byte [ITER_SIZEOF];
		byte [] endIter =  new byte [ITER_SIZEOF];
		OS.gtk_text_buffer_get_selection_bounds (bufferHandle, startIter, endIter);
		byte [] zero = new byte [ITER_SIZEOF];
		OS.gtk_text_buffer_get_iter_at_offset(bufferHandle, zero, 0);
		int /*long*/ ptr = OS.gtk_text_buffer_get_text (bufferHandle, zero, endIter, true);
		int start = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, OS.gtk_text_iter_get_offset (startIter));
		int end = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, OS.gtk_text_iter_get_offset (endIter));
		OS.g_free (ptr);
		selection = new Point (start, end);
	}
	if (segments != null) {
		selection.x = untranslateOffset (selection.x);
		selection.y = untranslateOffset (selection.y);
	}
	return selection;
}

/**
 * Returns the number of selected characters.
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
 * Gets the selected text, or an empty string if there is no current selection.
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
 * Returns the number of tabs.
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
	int /*long*/ layout = OS.gtk_widget_create_pango_layout (handle, buffer);
	int [] width = new int [1];
	int [] height = new int [1];
	OS.pango_layout_get_size (layout, width, height);
	OS.g_object_unref (layout);
	return width [0] * tabs;
}

/**
 * Returns the widget text.
 * <p>
 * The text for a text widget is the characters in the widget, or
 * an empty string if this has never been set.
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
	return new String (getTextChars());
}

/**
 * Returns a range of text.  Returns an empty string if the
 * start of the range is greater than the end.
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
	String str = getText ();
	int length = str.length ();
	end = Math.min (end, length - 1);
	if (start > end) return "";
	start = Math.max (0, start);
	/*
	* NOTE: The current implementation uses substring ()
	* which can reference a potentially large character
	* array.
	*/
	return str.substring (start, end + 1);
}

/**
 * Returns the widget's text as a character array.
 * <p>
 * The text for a text widget is the characters in the widget, or
 * a zero-length array if this has never been set.
 * </p>
 *
 * @return a character array that contains the widget's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setTextChars(char[])
 *
 * @since 3.7
 */
public char [] getTextChars () {
	checkWidget ();
	int /*long*/ address;
	if ((style & SWT.SINGLE) != 0) {
		address = OS.gtk_entry_get_text (handle);
	} else {
		byte [] start =  new byte [ITER_SIZEOF];
		byte [] end =  new byte [ITER_SIZEOF];
		OS.gtk_text_buffer_get_bounds (bufferHandle, start, end);
		address = OS.gtk_text_buffer_get_text (bufferHandle, start, end, true);
	}
	if (address == 0) return new char[0];
	int length = OS.strlen (address);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, address, length);
	if ((style & SWT.MULTI) != 0) OS.g_free (address);
	if (segments != null) {
		return deprocessText (Converter.mbcsToWcs (null, buffer), 0, -1);
	}
	return Converter.mbcsToWcs (null, buffer);
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
 * 
 * @see #LIMIT
 */
public int getTextLimit () {
	checkWidget ();
	if ((style & SWT.MULTI) != 0) return LIMIT;
	int limit = OS.gtk_entry_get_max_length (handle);
	return limit == 0 ? 0xFFFF : segments != null ? untranslateOffset (limit) : limit;
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
 * Returns the top pixel.
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

int /*long*/ gtk_activate (int /*long*/ widget) {
	sendSelectionEvent (SWT.DefaultSelection);
	return 0;
}

int /*long*/ gtk_button_press_event (int /*long*/ widget, int /*long*/ event) {
	int /*long*/ result = super.gtk_button_press_event (widget, event);
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


int /*long*/ gtk_changed (int /*long*/ widget) {
	/*
	* Feature in GTK.  When the user types, GTK positions
	* the caret after sending the changed signal.  This
	* means that application code that attempts to position
	* the caret during a changed signal will fail.  The fix
	* is to post the modify event when the user is typing.
	*/
	boolean keyPress = false;
	int /*long*/ eventPtr = OS.gtk_get_current_event ();
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
	if ((style & SWT.SEARCH) != 0) {
		if ((style & SWT.ICON_CANCEL) != 0) {
			int /*long*/ ptr = OS.gtk_entry_get_text (handle);
			OS.gtk_entry_set_icon_sensitive (handle, OS.GTK_ENTRY_ICON_SECONDARY, OS.g_utf16_strlen (ptr, -1) > 0);
		}
	}
	return 0;
}

int /*long*/ gtk_commit (int /*long*/ imContext, int /*long*/ text) {
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
	if ((style & SWT.SINGLE) != 0) { 
		if (fixStart != -1 && fixEnd != -1) {
			OS.gtk_editable_set_position (handle, fixStart);
			OS.gtk_editable_select_region (handle, fixStart, fixEnd);
		}
	}
	fixStart = fixEnd = -1;
	return 0;
}

int /*long*/ gtk_delete_range (int /*long*/ widget, int /*long*/ iter1, int /*long*/ iter2) {
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	byte [] startIter = new byte [ITER_SIZEOF];
	byte [] endIter = new byte [ITER_SIZEOF];
	OS.memmove (startIter, iter1, startIter.length);
	OS.memmove (endIter, iter2, endIter.length);
	int start = OS.gtk_text_iter_get_offset (startIter);
	int end = OS.gtk_text_iter_get_offset (endIter);
	byte [] zero = new byte [ITER_SIZEOF];
	OS.gtk_text_buffer_get_iter_at_offset(bufferHandle, zero, 0);
	int /*long*/ ptr = OS.gtk_text_buffer_get_text (bufferHandle, zero, endIter, true);
	start = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, start);
	end = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, end);
	OS.g_free (ptr);
	String newText = verifyText ("", start, end);
	if (newText == null) {
		/* Remember the selection when the text was deleted */
		OS.gtk_text_buffer_get_selection_bounds (bufferHandle, startIter, endIter);
		start = OS.gtk_text_iter_get_offset (startIter);
		end = OS.gtk_text_iter_get_offset (endIter);
		if (start != end) {
			fixStart = start;
			fixEnd = end;
		}
		OS.g_signal_stop_emission_by_name (bufferHandle, OS.delete_range);
	} else {
		if (newText.length () > 0) {
			byte [] buffer = Converter.wcsToMbcs (null, newText, false);
			OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_RANGE);
			OS.gtk_text_buffer_delete (bufferHandle, startIter, endIter);
			OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_RANGE);
			OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEXT_BUFFER_INSERT_TEXT);
			OS.gtk_text_buffer_insert (bufferHandle, startIter, buffer, buffer.length);
			OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEXT_BUFFER_INSERT_TEXT);
			OS.g_signal_stop_emission_by_name (bufferHandle, OS.delete_range);
		}
	}
	return 0;
}

int /*long*/ gtk_delete_text (int /*long*/ widget, int /*long*/ start_pos, int /*long*/ end_pos) {
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	int /*long*/ ptr = OS.gtk_entry_get_text (handle);
	if (end_pos == -1) end_pos = OS.g_utf8_strlen (ptr, -1);
	int start = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, start_pos);
	int end = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, end_pos);
	String newText = verifyText ("", start, end);
	if (newText == null) {
		/* Remember the selection when the text was deleted */
		int [] newStart = new int [1], newEnd = new int [1];
		OS.gtk_editable_get_selection_bounds (handle, newStart, newEnd);
		if (newStart [0] != newEnd [0]) {
			fixStart = newStart [0];
			fixEnd = newEnd [0];
		}
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
	/*
	* Feature in GTK.  The gtk-entry-select-on-focus property is a global
	* setting.  Return it to its default value after the GtkEntry has done
	* its focus in processing so that other widgets (such as the combo)
	* use the correct value.
	*/
	if ((style & SWT.SINGLE) != 0 && display.entrySelectOnFocus) {
		GdkEvent event = new GdkEvent ();
		OS.memmove (event, gdkEvent, GdkEvent.sizeof);
		switch (event.type) {
			case OS.GDK_FOCUS_CHANGE:
				GdkEventFocus gdkEventFocus = new GdkEventFocus ();
				OS.memmove (gdkEventFocus, gdkEvent, GdkEventFocus.sizeof);
				if (gdkEventFocus.in == 0) {
					int /*long*/ settings = OS.gtk_settings_get_default ();
					OS.g_object_set (settings, OS.gtk_entry_select_on_focus, true, 0);
				}
				break;
		}
	}
	return super.gtk_event_after (widget, gdkEvent);
}

int /*long*/ gtk_expose_event (int /*long*/ widget, int /*long*/ event) {
	if ((state & OBSCURED) != 0) return 0;
	int /*long*/ result = super.gtk_expose_event (widget, event);
	if ((style & SWT.SINGLE) != 0 && message.length () > 0) {
		int /*long*/ str = OS.gtk_entry_get_text (handle);
		if (!OS.GTK_WIDGET_HAS_FOCUS (handle) && OS.strlen (str) == 0) {
			GdkEventExpose gdkEvent = new GdkEventExpose ();
			OS.memmove (gdkEvent, event, GdkEventExpose.sizeof);
			int /*long*/ window = paintWindow ();
			int [] w = new int [1], h = new int [1];
			OS.gdk_drawable_get_size (window, w, h);
			GtkBorder innerBorder = Display.getEntryInnerBorder (handle);
			int width = w [0] - innerBorder.left - innerBorder.right;
			int height = h [0] - innerBorder.top - innerBorder.bottom;
			int /*long*/ context = OS.gtk_widget_get_pango_context (handle);
			int /*long*/ lang = OS.pango_context_get_language (context);
			int /*long*/ metrics = OS.pango_context_get_metrics (context, getFontDescription (), lang);
			int ascent = OS.PANGO_PIXELS (OS.pango_font_metrics_get_ascent (metrics));
			int descent = OS.PANGO_PIXELS (OS.pango_font_metrics_get_descent (metrics));
			OS.pango_font_metrics_unref (metrics);
			byte [] buffer = Converter.wcsToMbcs (null, message, true);
			int /*long*/ layout = OS.gtk_widget_create_pango_layout (handle, buffer);
			int /*long*/ line = OS.pango_layout_get_line (layout, 0);
			PangoRectangle rect = new PangoRectangle ();
			OS.pango_layout_line_get_extents (line, null, rect);
			rect.y = OS.PANGO_PIXELS (rect.y);
			rect.height = OS.PANGO_PIXELS (rect.height);
			rect.width = OS.PANGO_PIXELS (rect.width);
			int y = (height - ascent - descent) / 2 + ascent + rect.y;
			if (rect.height > height) {
				y = (height - rect.height) / 2;
			} else if (y < 0) {
				y = 0;
			} else if (y + rect.height > height) {
				y = height - rect.height;
			}
			y += innerBorder.top;
			int x = innerBorder.left;
			boolean rtl = (style & SWT.RIGHT_TO_LEFT) != 0;
			int alignment = style & (SWT.LEFT | SWT.CENTER | SWT.RIGHT);
			switch (alignment) {
				case SWT.LEFT: x = rtl ? width - rect.width: innerBorder.left; break;
				case SWT.CENTER: x = (width - rect.width) / 2; break;
				case SWT.RIGHT: x = rtl ? innerBorder.left : width - rect.width; break;
			}
			int /*long*/ gc = OS.gdk_gc_new	(window);
			int /*long*/ style = OS.gtk_widget_get_style (handle);	
			GdkColor textColor = new GdkColor ();
			OS.gtk_style_get_text (style, OS.GTK_STATE_INSENSITIVE, textColor);
			GdkColor baseColor = new GdkColor ();
			OS.gtk_style_get_base (style, OS.GTK_STATE_NORMAL, baseColor);
			OS.gdk_draw_layout_with_colors (window, gc, x, y, layout, textColor, baseColor);
			OS.g_object_unref (gc);
			OS.g_object_unref (layout);
		}
	}
	return result;
}

int /*long*/ gtk_focus_out_event (int /*long*/ widget, int /*long*/ event) {
	fixIM ();
	return super.gtk_focus_out_event (widget, event);
}

int /*long*/ gtk_grab_focus (int /*long*/ widget) {
	int /*long*/ result = super.gtk_grab_focus (widget);
	/*
	* Feature in GTK.  GtkEntry widgets select their text on focus in,
	* clearing the previous selection.  This behavior is controlled by
	* the gtk-entry-select-on-focus property.  The fix is to disable
	* this property when a GtkEntry is given focus and restore it after
	* the entry has done focus in processing.
	*/
	if ((style & SWT.SINGLE) != 0 && display.entrySelectOnFocus) {
		int /*long*/ settings = OS.gtk_settings_get_default ();
		OS.g_object_set (settings, OS.gtk_entry_select_on_focus, false, 0);
	}
	return result;
}

int /*long*/ gtk_icon_release (int /*long*/ widget, int /*long*/ icon_pos, int /*long*/ event) {
	Event e = new Event();
	if (icon_pos == OS.GTK_ENTRY_ICON_PRIMARY) {
		e.detail = SWT.ICON_SEARCH;
	} else {
		e.detail = SWT.ICON_CANCEL;
		OS.gtk_editable_delete_text (handle, 0, -1);
	}
	sendSelectionEvent (SWT.DefaultSelection, e, false);
	return 0;
}

int /*long*/ gtk_insert_text (int /*long*/ widget, int /*long*/ new_text, int /*long*/ new_text_length, int /*long*/ position) {
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	if (new_text == 0 || new_text_length == 0) return 0;
	byte [] buffer = new byte [(int)/*64*/new_text_length];
	OS.memmove (buffer, new_text, buffer.length);
	String oldText = new String (Converter.mbcsToWcs (null, buffer));
	int [] pos = new int [1];
	OS.memmove (pos, position, 4);
	int /*long*/ ptr = OS.gtk_entry_get_text (handle);
	if (pos [0] == -1) pos [0] = (int)/*64*/OS.g_utf8_strlen (ptr, -1);
	/* Use the selection when the text was deleted */
	int start = pos [0], end = pos [0];
	if (fixStart != -1 && fixEnd != -1) {
		start = pos [0] = fixStart;
		end = fixEnd;
		fixStart = fixEnd = -1;
	}
	start = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, start);
	end = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, end);
	String newText = verifyText (oldText, start, end);
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

int /*long*/ gtk_key_press_event (int /*long*/ widget, int /*long*/ event) {
	boolean handleSegments = false, segmentsCleared = false;
	if (hooks (SWT.GetSegments) || filters (SWT.GetSegments)) {
		GdkEventKey gdkEvent = new GdkEventKey ();
		OS.memmove (gdkEvent, event, GdkEventKey.sizeof);
		if (gdkEvent.length > 0 && (gdkEvent.state & (OS.GDK_MOD1_MASK | OS.GDK_CONTROL_MASK)) == 0) {
			handleSegments = true;
			if (segments != null) {
				clearSegments (true);
				segmentsCleared = true;
			}
		}
	}
	int /*long*/ result = super.gtk_key_press_event (widget, event);
	if (result != 0) fixIM ();
	if (gdkEventKey == -1) result = 1;
	gdkEventKey = 0;
	if (handleSegments && (result != 0 || segmentsCleared)) {
		applySegments ();
	}
	return result;
}

int /*long*/ gtk_populate_popup (int /*long*/ widget, int /*long*/ menu) {
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		OS.gtk_widget_set_direction (menu, OS.GTK_TEXT_DIR_RTL);
		OS.gtk_container_forall (menu, display.setDirectionProc, OS.GTK_TEXT_DIR_RTL);
	}
	return 0;
}

int /*long*/ gtk_text_buffer_insert_text (int /*long*/ widget, int /*long*/ iter, int /*long*/ text, int /*long*/ length) {
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	byte [] position = new byte [ITER_SIZEOF];
	OS.memmove (position, iter, position.length);
	/* Use the selection when the text was deleted */
	int start = OS.gtk_text_iter_get_offset (position), end = start;
	if (fixStart != -1 && fixEnd != -1) {
		start = fixStart;
		end = fixEnd;
		fixStart = fixEnd = -1;
	}
	byte [] zero = new byte [ITER_SIZEOF];
	OS.gtk_text_buffer_get_iter_at_offset(bufferHandle, zero, 0);
	int /*long*/ ptr = OS.gtk_text_buffer_get_text (bufferHandle, zero, position, true);
	start = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, start);
	end = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, end);
	OS.g_free(ptr);
	byte [] buffer = new byte [(int)/*64*/length];
	OS.memmove (buffer, text, buffer.length);
	String oldText = new String (Converter.mbcsToWcs (null, buffer));
	String newText = verifyText (oldText, start, end);
	if (newText == null) {
		OS.g_signal_stop_emission_by_name (bufferHandle, OS.insert_text);
	} else {
		if (newText != oldText) {
			byte [] buffer1 = Converter.wcsToMbcs (null, newText, false);
			OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEXT_BUFFER_INSERT_TEXT);
			OS.gtk_text_buffer_insert (bufferHandle, iter, buffer1, buffer1.length);
			OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEXT_BUFFER_INSERT_TEXT);
			OS.g_signal_stop_emission_by_name (bufferHandle, OS.insert_text);
		}
	}
	return 0;
}

void hookEvents () {
	super.hookEvents();
	if ((style & SWT.SINGLE) != 0) {
		OS.g_signal_connect_closure (handle, OS.changed, display.closures [CHANGED], true);
		OS.g_signal_connect_closure (handle, OS.insert_text, display.closures [INSERT_TEXT], false);
		OS.g_signal_connect_closure (handle, OS.delete_text, display.closures [DELETE_TEXT], false);
		OS.g_signal_connect_closure (handle, OS.activate, display.closures [ACTIVATE], false);
		OS.g_signal_connect_closure (handle, OS.grab_focus, display.closures [GRAB_FOCUS], false);
		OS.g_signal_connect_closure (handle, OS.populate_popup, display.closures [POPULATE_POPUP], false);
		if ((style & SWT.SEARCH) != 0 && OS.GTK_VERSION >= OS.VERSION (2, 16, 0)) {
			OS.g_signal_connect_closure (handle, OS.icon_release, display.closures [ICON_RELEASE], false);
		}
	} else {
		OS.g_signal_connect_closure (bufferHandle, OS.changed, display.closures [CHANGED], false);
		OS.g_signal_connect_closure (bufferHandle, OS.insert_text, display.closures [TEXT_BUFFER_INSERT_TEXT], false);
		OS.g_signal_connect_closure (bufferHandle, OS.delete_range, display.closures [DELETE_RANGE], false);
		OS.g_signal_connect_closure (handle, OS.populate_popup, display.closures [POPULATE_POPUP], false);
	}
	int /*long*/ imContext = imContext ();
	if (imContext != 0) {
		OS.g_signal_connect_closure (imContext, OS.commit, display.closures [COMMIT], false);
		int id = OS.g_signal_lookup (OS.commit, OS.gtk_im_context_get_type ());
		int mask =  OS.G_SIGNAL_MATCH_DATA | OS.G_SIGNAL_MATCH_ID;
		OS.g_signal_handlers_block_matched (imContext, mask, id, 0, 0, 0, handle);
	}
	OS.g_signal_connect_closure (handle, OS.backspace, display.closures [BACKSPACE], false);
	OS.g_signal_connect_closure (handle, OS.backspace, display.closures [BACKSPACE_INVERSE], true);
	OS.g_signal_connect_closure (handle, OS.copy_clipboard, display.closures [COPY_CLIPBOARD], false);
	OS.g_signal_connect_closure (handle, OS.copy_clipboard, display.closures [COPY_CLIPBOARD_INVERSE], true);
	OS.g_signal_connect_closure (handle, OS.cut_clipboard, display.closures [CUT_CLIPBOARD], false);
	OS.g_signal_connect_closure (handle, OS.cut_clipboard, display.closures [CUT_CLIPBOARD_INVERSE], true);
	OS.g_signal_connect_closure (handle, OS.paste_clipboard, display.closures [PASTE_CLIPBOARD], false);
	OS.g_signal_connect_closure (handle, OS.paste_clipboard, display.closures [PASTE_CLIPBOARD_INVERSE], true);
	OS.g_signal_connect_closure (handle, OS.delete_from_cursor, display.closures [DELETE_FROM_CURSOR], false);
	OS.g_signal_connect_closure (handle, OS.delete_from_cursor, display.closures [DELETE_FROM_CURSOR_INVERSE], true);
	OS.g_signal_connect_closure (handle, OS.move_cursor, display.closures [MOVE_CURSOR], false);
	OS.g_signal_connect_closure (handle, OS.move_cursor, display.closures [MOVE_CURSOR_INVERSE], true);
	OS.g_signal_connect_closure (handle, OS.direction_changed, display.closures [DIRECTION_CHANGED], true);
}

int /*long*/ imContext () {
	if ((style & SWT.SINGLE) != 0) {
		return OS.gtk_editable_get_editable (handle) ? OS.GTK_ENTRY_IM_CONTEXT (handle) : 0;
	} 
	return OS.GTK_TEXTVIEW_IM_CONTEXT (handle);
}

/**
 * Inserts a string.
 * <p>
 * The old selection is replaced with the new text.
 * </p>
 *
 * @param string the string
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is <code>null</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void insert (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (segments != null) clearSegments (true);
	byte [] buffer = Converter.wcsToMbcs (null, string, false);
	if ((style & SWT.SINGLE) != 0) {
		int [] start = new int [1], end = new int [1];
		OS.gtk_editable_get_selection_bounds (handle, start, end);
		OS.gtk_editable_delete_selection (handle);
		OS.gtk_editable_insert_text (handle, buffer, buffer.length, start);
		OS.gtk_editable_set_position (handle, start [0]);
	} else {
		byte [] start =  new byte [ITER_SIZEOF];
		byte [] end =  new byte [ITER_SIZEOF];
		if (OS.gtk_text_buffer_get_selection_bounds (bufferHandle, start, end)) {
			OS.gtk_text_buffer_delete (bufferHandle, start, end);
		}
		OS.gtk_text_buffer_insert (bufferHandle, start, buffer, buffer.length);
		OS.gtk_text_buffer_place_cursor (bufferHandle, start);
		int /*long*/ mark = OS.gtk_text_buffer_get_insert (bufferHandle);
		OS.gtk_text_view_scroll_mark_onscreen (handle, mark);
	}
	if (hooks (SWT.GetSegments) || filters (SWT.GetSegments)) {
		applySegments ();
	}
}

int /*long*/ paintWindow () {
	if ((style & SWT.SINGLE) != 0) {
		int /*long*/ window = super.paintWindow ();
		int /*long*/ children = OS.gdk_window_get_children (window);
		if (children != 0) {
			/*
			* When search or cancel icons are added to Text, those
			* icon window(s) are added to the beginning of the list. 
			* In order to always return the correct window for Text,
			* browse to the end of the list. 
			*/
			do {
				window = OS.g_list_data (children);
			} while ((children = OS.g_list_next (children)) != 0);
		}
		OS.g_list_free (children);
		return window;
	}
	OS.gtk_widget_realize (handle);
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
		int /*long*/ clipboard = OS.gtk_clipboard_get (OS.GDK_NONE);
		if (hooks (SWT.GetSegments) || filters (SWT.GetSegments)) {
			if (segments != null) clearSegments (true);
			OS.gtk_text_buffer_paste_clipboard (bufferHandle, clipboard, null, OS.gtk_text_view_get_editable (handle));
			applySegments ();
		} else {
			OS.gtk_text_buffer_paste_clipboard (bufferHandle, clipboard, null, OS.gtk_text_view_get_editable (handle));
		}
	}
}

void register () {
	super.register ();
	if (bufferHandle != 0) display.addWidget (bufferHandle, this);
	int /*long*/ imContext = imContext ();
	if (imContext != 0) display.addWidget (imContext, this);
}

void releaseWidget () {
	super.releaseWidget ();
	fixIM ();	
	if (OS.GTK_VERSION < OS.VERSION (2, 6, 0)) {		 
		/*
		* Bug in GTK.  Any text copied into the clipboard will be lost when
		* the GtkTextView is destroyed.  The fix is to paste the contents as
		* the widget is being destroyed to reference the text buffer, keeping
		* it around until ownership of the clipboard is lost.
		*/
		if ((style & SWT.MULTI) != 0) {
			int /*long*/ clipboard = OS.gtk_clipboard_get (OS.GDK_NONE);
			OS.gtk_text_buffer_paste_clipboard (bufferHandle, clipboard, null, OS.gtk_text_view_get_editable (handle));
		}
	}
	message = null;
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

public void removeSegmentListener (SegmentListener listener) {
	checkWidget ();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	eventTable.unhook (SWT.GetSegments, listener);
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
		int /*long*/ insertMark = OS.gtk_text_buffer_get_insert (bufferHandle);
		int /*long*/ selectionMark = OS.gtk_text_buffer_get_selection_bound (bufferHandle);
		OS.gtk_text_buffer_move_mark (bufferHandle, selectionMark, start);
		OS.gtk_text_buffer_move_mark (bufferHandle, insertMark, end);
	}
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

/**
 * Sets the double click enabled flag.
 * <p>
 * The double click flag enables or disables the
 * default action of the text widget when the user
 * double clicks.
 * </p><p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
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
 * or if the platform does not allow modification
 * of the echo character, the default echo character
 * for the platform is used.
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

void setFontDescription (int /*long*/ font) {
	super.setFontDescription (font);
	setTabStops (tabs);
}

/**
 * Sets the widget message. The message text is displayed
 * as a hint for the user, indicating the purpose of the field.
 * <p>
 * Typically this is used in conjunction with <code>SWT.SEARCH</code>.
 * </p>
 * 
 * @param message the new message
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the message is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
public void setMessage (String message) {
	checkWidget ();
	if (message == null) error (SWT.ERROR_NULL_ARGUMENT);
	this.message = message;
	redraw (false);
}

/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 *
 * @param orientation new orientation style
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
	if (segments != null) start = translateOffset (start);
	if ((style & SWT.SINGLE) != 0) {
		int /*long*/ ptr = OS.gtk_entry_get_text (handle);
		start = (int)/*64*/OS.g_utf16_offset_to_utf8_offset (ptr, start);
		OS.gtk_editable_set_position (handle, start);
	} else {
		byte [] startIter =  new byte [ITER_SIZEOF];
		byte [] endIter =  new byte [ITER_SIZEOF];
		OS.gtk_text_buffer_get_bounds (bufferHandle, startIter, endIter);
		int /*long*/ ptr = OS.gtk_text_buffer_get_text (bufferHandle, startIter, endIter, true);
		start = (int)/*64*/OS.g_utf16_offset_to_utf8_offset (ptr, start);
		OS.g_free (ptr);
		OS.gtk_text_buffer_get_iter_at_offset (bufferHandle, startIter, start);
		OS.gtk_text_buffer_place_cursor (bufferHandle, startIter);
		int /*long*/ mark = OS.gtk_text_buffer_get_insert (bufferHandle);
		OS.gtk_text_view_scroll_mark_onscreen (handle, mark);
	}
}

/**
 * Sets the selection to the range specified
 * by the given start and end indices.
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
	if (segments != null) {
		start = translateOffset (start);
		end = translateOffset (end);
	}
	if ((style & SWT.SINGLE) != 0) {
		int /*long*/ ptr = OS.gtk_entry_get_text (handle);
		start = (int)/*64*/OS.g_utf16_offset_to_utf8_offset (ptr, start);
		end = (int)/*64*/OS.g_utf16_offset_to_utf8_offset (ptr, end);
		OS.gtk_editable_set_position (handle, start);
		OS.gtk_editable_select_region (handle, start, end);
	} else {
		byte [] startIter =  new byte [ITER_SIZEOF];
		byte [] endIter =  new byte [ITER_SIZEOF];
		OS.gtk_text_buffer_get_bounds (bufferHandle, startIter, endIter);
		int /*long*/ ptr = OS.gtk_text_buffer_get_text (bufferHandle, startIter, endIter, true);
		start = (int)/*64*/OS.g_utf16_offset_to_utf8_offset (ptr, start);
		end = (int)/*64*/OS.g_utf16_offset_to_utf8_offset (ptr, end);
		OS.g_free (ptr);
		OS.gtk_text_buffer_get_iter_at_offset (bufferHandle, startIter, start);
		OS.gtk_text_buffer_get_iter_at_offset (bufferHandle, endIter, end);
		int /*long*/ insertMark = OS.gtk_text_buffer_get_insert (bufferHandle);
		int /*long*/ selectionMark = OS.gtk_text_buffer_get_selection_bound (bufferHandle);
		OS.gtk_text_buffer_move_mark (bufferHandle, selectionMark, startIter);
		OS.gtk_text_buffer_move_mark (bufferHandle, insertMark, endIter);
	}
}

/**
 * Sets the selection to the range specified
 * by the given point, where the x coordinate
 * represents the start index and the y coordinate
 * represents the end index.
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
	int /*long*/ tabArray = OS.pango_tab_array_new (1, false);
	OS.pango_tab_array_set_tab (tabArray, 0, OS.PANGO_TAB_LEFT, tabWidth);
	OS.gtk_text_view_set_tabs (handle, tabArray);
	OS.pango_tab_array_free (tabArray);
}

/**
 * Sets the contents of the receiver to the given string. If the receiver has style
 * SINGLE and the argument contains multiple lines of text, the result of this
 * operation is undefined and may vary from platform to platform.
 *
 * @param string the new text
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
	char [] text = new char [string.length()];
	string.getChars(0, text.length, text, 0);
	setText (text);
}

/**
 * Sets the contents of the receiver to the characters in the array. If the receiver
 * has style <code>SWT.SINGLE</code> and the argument contains multiple lines of text
 * then the result of this operation is undefined and may vary between platforms.
 *
 * @param text a character array that contains the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getTextChars()
 *
 * @since 3.7
 */
public void setTextChars (char [] text) {
	checkWidget ();
	if (text == null) error (SWT.ERROR_NULL_ARGUMENT);
	/*
	* Feature in gtk.  When text is set in gtk, separate events are fired for the deletion and 
	* insertion of the text.  This is not wrong, but is inconsistent with other platforms.  The
	* fix is to block the firing of these events and fire them ourselves in a consistent manner. 
	*/
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		String string = verifyText (new String(text), 0, getCharCount ());
		if (string == null) return;
		text = new char [string.length()];
		string.getChars (0, text.length, text, 0);
	}
	setText (text);
}

void setText (char [] text) {
	if ((style & SWT.SINGLE) != 0) {
		byte [] buffer = Converter.wcsToMbcs (null, text, true);
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
		if (segments != null) clearSegments (false);
		OS.gtk_entry_set_text (handle, buffer);
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
	} else {
		byte [] buffer = Converter.wcsToMbcs (null, text, false);
		byte [] position =  new byte [ITER_SIZEOF];
		if (segments != null) clearSegments (false);
		OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_RANGE);
		OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEXT_BUFFER_INSERT_TEXT);
		OS.gtk_text_buffer_set_text (bufferHandle, buffer, buffer.length);
		OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_RANGE);
		OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEXT_BUFFER_INSERT_TEXT);
		OS.gtk_text_buffer_get_iter_at_offset (bufferHandle, position, 0);
		OS.gtk_text_buffer_place_cursor (bufferHandle, position);
		int /*long*/ mark = OS.gtk_text_buffer_get_insert (bufferHandle);
		OS.gtk_text_view_scroll_mark_onscreen (handle, mark);
	}
	sendEvent (SWT.Modify);
	if ((style & SWT.SEARCH) != 0) {
		if ((style & SWT.ICON_CANCEL) != 0) {
			OS.gtk_entry_set_icon_sensitive (handle, OS.GTK_ENTRY_ICON_SECONDARY, true);
		}
	}
	if (hooks (SWT.GetSegments) || filters (SWT.GetSegments)) {
		applySegments ();
	}
}

/**
 * Sets the maximum number of characters that the receiver
 * is capable of holding to be the argument.
 * <p>
 * Instead of trying to set the text limit to zero, consider
 * creating a read-only text widget.
 * </p><p>
 * To reset this value to the default, use <code>setTextLimit(Text.LIMIT)</code>.
 * Specifying a limit value larger than <code>Text.LIMIT</code> sets the
 * receiver's limit to <code>Text.LIMIT</code>.
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
 * 
 * @see #LIMIT
 */
public void setTextLimit (int limit) {
	checkWidget ();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	if ((style & SWT.SINGLE) != 0) {
		OS.gtk_entry_set_max_length (handle, segments != null ? Math.min (LIMIT, translateOffset (limit)) : limit);
	}
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
	int /*long*/ mark = OS.gtk_text_buffer_get_selection_bound (bufferHandle);
	OS.gtk_text_view_scroll_mark_onscreen (handle, mark);
	mark = OS.gtk_text_buffer_get_insert (bufferHandle);
	OS.gtk_text_view_scroll_mark_onscreen (handle, mark);
}

int translateOffset (int offset) {
	if (segments == null) return offset;
	for (int i = 0, nSegments = segments.length; i < nSegments && offset - i >= segments[i]; i++) {
		offset++;
	}	
	return offset;
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

int untranslateOffset (int offset) {
	if (segments == null) return offset;
	for (int i = 0, nSegments = segments.length; i < nSegments && offset > segments[i]; i++) {
		offset--;
	}
	return offset;
}

String verifyText (String string, int start, int end) {
	if (string != null && string.length () == 0 && start == end) return null;
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

int /*long*/ windowProc (int /*long*/ handle, int /*long*/ user_data) {
	if (hooks (SWT.GetSegments) || filters (SWT.GetSegments)) {
		switch ((int)/*64*/user_data) {
			case BACKSPACE:
			case COPY_CLIPBOARD:
			case CUT_CLIPBOARD:
			case PASTE_CLIPBOARD: {
				if (segments != null) clearSegments (true);
				break;
			}
			case BACKSPACE_INVERSE:
			case COPY_CLIPBOARD_INVERSE:
			case CUT_CLIPBOARD_INVERSE:
			case PASTE_CLIPBOARD_INVERSE: {
				applySegments ();
				return 0;
			}
		}
	}
	return super.windowProc (handle, user_data);
}

int /*long*/ windowProc (int /*long*/ handle, int /*long*/ arg0, int /*long*/ user_data) {
	if (hooks (SWT.GetSegments) || filters (SWT.GetSegments)) {
		switch ((int)/*64*/user_data) {
			case DIRECTION_CHANGED: {
				if (segments != null) clearSegments (true);
				applySegments ();
				return 0;
			}
		}
	}
	return super.windowProc (handle, arg0, user_data);
}

int /*long*/ windowProc (int /*long*/ handle, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ user_data) {
	if (hooks (SWT.GetSegments) || filters (SWT.GetSegments)) {
		switch ((int)/*64*/user_data) {
			case DELETE_FROM_CURSOR: {
				if (segments != null) clearSegments (true);
				break;
			}
			case DELETE_FROM_CURSOR_INVERSE: {
				applySegments ();
				return 0;
			}
		}
	}
	return super.windowProc (handle, arg0, arg1, user_data);
}

int /*long*/ windowProc (int /*long*/ handle, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ user_data) {
	if (hooks (SWT.GetSegments) || filters (SWT.GetSegments)) {
		switch ((int)/*64*/user_data) {
			case MOVE_CURSOR: {
				if (arg0 == OS.GTK_MOVEMENT_VISUAL_POSITIONS && segments != null) clearSegments (true);
				break;
			}
			case MOVE_CURSOR_INVERSE: {
				if (arg0 == OS.GTK_MOVEMENT_VISUAL_POSITIONS) applySegments ();
				return 0;
			}
		}
	}
	return super.windowProc (handle, arg0, arg1, arg2, user_data);
}

}
