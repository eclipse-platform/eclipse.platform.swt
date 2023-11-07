/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.swt.widgets;


import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

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
 * Note: Some text actions such as Undo are not natively supported on all platforms.
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
	long bufferHandle;
	long imContext;
	int tabs = 8, lastEventTime = 0;
	long gdkEventKey = 0;
	int fixStart = -1, fixEnd = -1;
	boolean doubleClick;
	String message = "";

	/** GTK4 only field, holds the address to the underlying GtkText widget. */
	long textHandle;

	static final char LTR_MARK = '\u200e';
	static final char RTL_MARK = '\u200f';
	int[] segments;

	static final int ITER_SIZEOF = GTK.GtkTextIter_sizeof();
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
	/* Text uses non-standard CSS to set its background color, so we need
	 * a global variable to keep track of its background color.
	 */
	GdkRGBA background, foreground;
	long indexMark = 0;
	double cachedAdjustment, currentAdjustment;

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
	if ((style & SWT.SEARCH) != 0) {
		/*
		 * Ensure that SWT.ICON_CANCEL and ICON_SEARCH are set.
		 * NOTE: ICON_CANCEL has the same value as H_SCROLL and CON_SEARCH has the same value as V_SCROLL
		 * so it is necessary to first clear these bits to avoid a scroll bar and then reset the
		 * bit using the original style supplied by the programmer.
		 *
		 * NOTE2: Default GtkSearchEntry shows both "find" icon and "clear" icon.
		 * "find" icon can be manually removed here while "clear" icon must be removed depending on text.
		 * See gtk_changed.
		 */
		this.style |= SWT.ICON_SEARCH | SWT.ICON_CANCEL;

		if (!GTK.GTK4) {
			if ((style & SWT.ICON_SEARCH) == 0) {
				this.style &= ~SWT.ICON_SEARCH;
				GTK.gtk_entry_set_icon_from_icon_name(handle, GTK.GTK_ENTRY_ICON_PRIMARY, null);
			} else {
				// Default GtkSearchEntry always shows inactive "find" icon
				// make it active and sensitive to be consistent with other platforms
				GTK.gtk_entry_set_icon_activatable(handle, GTK.GTK_ENTRY_ICON_PRIMARY, true);
				GTK.gtk_entry_set_icon_sensitive(handle, GTK.GTK_ENTRY_ICON_PRIMARY, true);
			}

			if ((style & SWT.ICON_CANCEL) == 0) {
				this.style &= ~SWT.ICON_CANCEL;
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

@Override
void createHandle (int index) {
	state |= HANDLE | MENU;
	if ((style & SWT.READ_ONLY) != 0) {
		if (applyThemeBackground() == 1) {
			state |= THEME_BACKGROUND;
		}
	}

	fixedHandle = OS.g_object_new(display.gtk_fixed_get_type (), 0);
	if (fixedHandle == 0) error(SWT.ERROR_NO_HANDLES);
	if ((style & SWT.SINGLE) != 0) {
		if ((style & SWT.SEARCH) != 0) {
			handle = GTK.gtk_search_entry_new();
		} else {
			handle = GTK.gtk_entry_new();
		}
		if (handle == 0) error(SWT.ERROR_NO_HANDLES);

		if (GTK.GTK4) {
			OS.swt_fixed_add(fixedHandle, handle);
			textHandle = GTK4.gtk_editable_get_delegate(handle);
			if ((style & SWT.SEARCH) == 0) {
				bufferHandle = GTK4.gtk_entry_get_buffer(handle);
			} else {
				bufferHandle = GTK4.gtk_text_get_buffer(textHandle);
			}
		} else {
			GTK3.gtk_widget_set_has_window(fixedHandle, true);
			GTK3.gtk_container_add(fixedHandle, handle);

			GTK3.gtk_entry_set_width_chars(handle, 1);
		}

		GTK.gtk_editable_set_editable(handle, (style & SWT.READ_ONLY) == 0);
		if (GTK.GTK4) {
			GTK4.gtk_text_set_visibility(textHandle, (style & SWT.PASSWORD) == 0);
		} else {
			GTK.gtk_entry_set_visibility(handle, (style & SWT.PASSWORD) == 0);
		}
		/*
		 * We need to handle borders differently in GTK3.20+. GtkEntry without frame will have a blank background color.
		 * So let's set border via css and override the background in this case to be COLOR_LIST_BACKGROUND.
		 */
		if ((style & SWT.BORDER) == 0) {
			GTK.gtk_entry_set_has_frame(handle, false);
			long context = GTK.gtk_widget_get_style_context(handle);
			String background = display.gtk_rgba_to_css_string(display.COLOR_LIST_BACKGROUND_RGBA);
			gtk_css_provider_load_from_css(context, "entry {border: solid; background: " + background + ";}");
			if (!GTK.GTK4) GTK3.gtk_style_context_invalidate(context);
		}

		float alignment = 0.0f;
		if ((style & SWT.CENTER) != 0) alignment = 0.5f;
		if ((style & SWT.RIGHT) != 0) alignment = 1.0f;
		if (alignment > 0.0f) {
			GTK.gtk_entry_set_alignment(handle, alignment);
		}

		if (DISABLE_EMOJI && GTK.GTK_VERSION >= OS.VERSION(3, 22, 20)) {
		    GTK.gtk_entry_set_input_hints(handle, GTK.GTK_INPUT_HINT_NO_EMOJI);
		}
	} else {
		if (GTK.GTK4) {
			scrolledHandle = GTK4.gtk_scrolled_window_new();
		} else {
			scrolledHandle = GTK3.gtk_scrolled_window_new(0, 0);
		}
		if (scrolledHandle == 0) error(SWT.ERROR_NO_HANDLES);

		handle = GTK.gtk_text_view_new();
		if (handle == 0) error(SWT.ERROR_NO_HANDLES);
		bufferHandle = GTK.gtk_text_view_get_buffer(handle);
		if (bufferHandle == 0) error(SWT.ERROR_NO_HANDLES);

		if (GTK.GTK4) {
			OS.swt_fixed_add(fixedHandle, scrolledHandle);
			GTK4.gtk_scrolled_window_set_child(scrolledHandle, handle);
		} else {
			GTK3.gtk_container_add(fixedHandle, scrolledHandle);
			GTK3.gtk_container_add(scrolledHandle, handle);
		}

		GTK.gtk_text_view_set_editable(handle, (style & SWT.READ_ONLY) == 0);
		if ((style & SWT.WRAP) != 0) GTK.gtk_text_view_set_wrap_mode(handle, GTK.GTK_WRAP_WORD_CHAR);

		int hsp = (style & SWT.H_SCROLL) != 0 ? GTK.GTK_POLICY_ALWAYS : GTK.GTK_POLICY_NEVER;
		int vsp = (style & SWT.V_SCROLL) != 0 ? GTK.GTK_POLICY_ALWAYS : GTK.GTK_POLICY_NEVER;
		GTK.gtk_scrolled_window_set_policy(scrolledHandle, hsp, vsp);

		if ((style & SWT.BORDER) != 0) {
			if (GTK.GTK4) {
				GTK4.gtk_scrolled_window_set_has_frame(scrolledHandle, true);
			} else {
				GTK3.gtk_scrolled_window_set_shadow_type(scrolledHandle, GTK.GTK_SHADOW_ETCHED_IN);
			}
		}

		int justification = GTK.GTK_JUSTIFY_LEFT;
		if ((style & SWT.CENTER) != 0) justification = GTK.GTK_JUSTIFY_CENTER;
		if ((style & SWT.RIGHT) != 0) justification = GTK.GTK_JUSTIFY_RIGHT;
		GTK.gtk_text_view_set_justification(handle, justification);
	}

	imContext = OS.imContextLast();

	// In GTK 3 font description is inherited from parent widget which is not how SWT has always worked,
	// reset to default font to get the usual behavior
	setFontDescription(defaultFont().handle);
}

@Override
int applyThemeBackground () {
	return (backgroundAlpha == 0 || (style & (SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL)) == 0) ? 1 : 0;
}

@Override
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
 * Adds a segment listener.
 * <p>
 * A <code>SegmentEvent</code> is sent whenever text content is being modified or
 * a segment listener is added or removed. You can
 * customize the appearance of text by indicating certain characters to be inserted
 * at certain text offsets. This may be used for bidi purposes, e.g. when
 * adjacent segments of right-to-left text should not be reordered relative to
 * each other.
 * E.g., multiple Java string literals in a right-to-left language
 * should generally remain in logical order to each other, that is, the
 * way they are stored.
 * </p>
 * <p>
 * <b>Warning</b>: This API is currently only implemented on Windows and GTK.
 * <code>SegmentEvent</code>s won't be sent on Cocoa.
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
 * @see SegmentEvent
 * @see SegmentListener
 * @see #removeSegmentListener
 *
 * @since 3.8
 */
public void addSegmentListener (SegmentListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	addListener (SWT.Segments, new TypedListener (listener));
	clearSegments (true);
	applySegments ();
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is not called for texts.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed in a single-line text,
 * or when ENTER is pressed in a search text. If the receiver has the <code>SWT.SEARCH | SWT.ICON_CANCEL</code> style
 * and the user cancels the search, the event object detail field contains the value <code>SWT.ICON_CANCEL</code>.
 * Likewise, if the receiver has the <code>SWT.ICON_SEARCH</code> style and the icon search is selected, the
 * event object detail field contains the value <code>SWT.ICON_SEARCH</code>.
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
	byte [] buffer = Converter.wcsToMbcs (string, false);
	clearSegments (true);
	if ((style & SWT.SINGLE) != 0) {
		GTK.gtk_editable_insert_text (handle, buffer, buffer.length, new int[]{-1});
		GTK.gtk_editable_set_position (handle, -1);
	} else {
		byte [] position =  new byte [ITER_SIZEOF];
		GTK.gtk_text_buffer_get_end_iter (bufferHandle, position);
		GTK.gtk_text_buffer_insert (bufferHandle, position, buffer, buffer.length);
		GTK.gtk_text_buffer_place_cursor (bufferHandle, position);
		long mark = GTK.gtk_text_buffer_get_insert (bufferHandle);
		GTK.gtk_text_view_scroll_to_mark (handle, mark, 0, true, 0, 0);
	}
	applySegments ();
}

void applySegments () {
	/*
	 * It is possible (but unlikely), that application code could have
	 * disposed the widget in the modify event. If this happens, return to
	 * cancel the operation.
	 */
	if (isDisposed() || (!hooks (SWT.Segments) && !filters (SWT.Segments))) return;
	Event event = new Event ();
	String string = getText ();
	event.text = string;
	event.segments = segments;
	sendEvent (SWT.Segments, event);
	segments = event.segments;
	if (segments == null) return;
	int nSegments = segments.length;
	if (nSegments == 0) return;

	for (int i = 1, length = string == null ? 0 : string.length (); i < nSegments; i++) {
		if (event.segments [i] < event.segments [i - 1] || event.segments [i] > length) {
			error (SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	char[] segmentsChars = event.segmentsChars;
	char [] separator = { getOrientation () == SWT.RIGHT_TO_LEFT ? RTL_MARK : LTR_MARK };
	if ((style & SWT.SINGLE) != 0) {
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
		int limit = GTK.gtk_entry_get_max_length (handle);
		if (limit != 0) GTK.gtk_entry_set_max_length (handle, translateOffset (limit));
		int [] pos = new int [1];
		for (int i = 0; i < nSegments; i++) {
			pos [0] = segments [i] + i;
			if (segmentsChars != null && segmentsChars.length > i) {
				separator [0] = segmentsChars [i];
			}
			byte [] buffer = Converter.wcsToMbcs (separator, false);
			long ptr;
			if (GTK.GTK4) {
				ptr = GTK.gtk_entry_buffer_get_text(bufferHandle);
			} else {
				ptr = GTK3.gtk_entry_get_text(handle);
			}
			pos [0] = (int)OS.g_utf16_offset_to_utf8_offset (ptr, pos [0]);
			GTK.gtk_editable_insert_text (handle, buffer, buffer.length, pos);
		}
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
	} else {
		OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEXT_BUFFER_INSERT_TEXT);
		byte [] pos = new byte [ITER_SIZEOF];
		byte [] startIter =  new byte [ITER_SIZEOF];
		byte [] endIter =  new byte [ITER_SIZEOF];
		for (int i = 0; i < nSegments; i++) {
			GTK.gtk_text_buffer_get_bounds (bufferHandle, startIter, endIter);
			long ptr = GTK.gtk_text_buffer_get_text (bufferHandle, startIter, endIter, true);
			GTK.gtk_text_buffer_get_iter_at_offset (bufferHandle, pos, (int)OS.g_utf16_offset_to_utf8_offset (ptr, segments[i] + i));
			OS.g_free (ptr);
			if (segmentsChars != null && segmentsChars.length > i) {
				separator [0] = segmentsChars [i];
			}
			byte [] buffer = Converter.wcsToMbcs (separator, false);
			GTK.gtk_text_buffer_insert (bufferHandle, pos, buffer, buffer.length);
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
			long ptr;
			if (GTK.GTK4) {
				ptr = GTK.gtk_entry_buffer_get_text(bufferHandle);
			} else {
				ptr = GTK3.gtk_entry_get_text(handle);
			}
			int start, end;
			for (int i = 0; i < nSegments; i++) {
				start = (int)OS.g_utf16_offset_to_utf8_offset (ptr, segments[i]);
				end = (int)OS.g_utf16_offset_to_utf8_offset (ptr, segments[i] + 1);
				GTK.gtk_editable_delete_text (handle, start, end);
			}
			OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
		}
		int limit = GTK.gtk_entry_get_max_length (handle);
		if (limit != 0) GTK.gtk_entry_set_max_length (handle, untranslateOffset (limit));
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	} else if (applyText) {
		OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_RANGE);
		byte [] start = new byte [ITER_SIZEOF], end = new byte [ITER_SIZEOF];
		byte [] startIter =  new byte [ITER_SIZEOF], endIter =  new byte [ITER_SIZEOF];
		for (int i = 0; i < nSegments; i++) {
			GTK.gtk_text_buffer_get_bounds (bufferHandle, startIter, endIter);
			long ptr = GTK.gtk_text_buffer_get_text (bufferHandle, startIter, endIter, true);
			GTK.gtk_text_buffer_get_iter_at_offset (bufferHandle, start, (int)OS.g_utf16_offset_to_utf8_offset (ptr, segments[i]));
			GTK.gtk_text_buffer_get_iter_at_offset (bufferHandle, end, (int)OS.g_utf16_offset_to_utf8_offset (ptr, segments[i] + 1));
			GTK.gtk_text_buffer_delete (bufferHandle, start, end);
			OS.g_free (ptr);
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
		int position = GTK.gtk_editable_get_position (handle);
		GTK.gtk_editable_select_region (handle, position, position);
	} else {
		byte [] position = new byte [ITER_SIZEOF];
		long insertMark = GTK.gtk_text_buffer_get_insert (bufferHandle);
		GTK.gtk_text_buffer_get_iter_at_mark (bufferHandle, position, insertMark);
		GTK.gtk_text_buffer_select_range(bufferHandle, position, position);
	}
}

@Override
Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	int[] w = new int[1], h = new int[1];
	if ((style & SWT.SINGLE) != 0) {
		long layout;
		if (GTK.GTK4) {
			long context = GTK.gtk_widget_get_pango_context(handle);
			layout = OS.pango_layout_new(context);
		} else {
			GTK.gtk_widget_realize(handle);
			layout = GTK3.gtk_entry_get_layout(handle);
		}
		OS.pango_layout_get_pixel_size(layout, w, h);
	} else {
		byte [] start =  new byte [ITER_SIZEOF], end  =  new byte [ITER_SIZEOF];
		GTK.gtk_text_buffer_get_bounds (bufferHandle, start, end);
		long text = GTK.gtk_text_buffer_get_text (bufferHandle, start, end, true);
		long layout = GTK.gtk_widget_create_pango_layout (handle, text);
		OS.g_free (text);
		OS.pango_layout_set_width (layout, wHint * OS.PANGO_SCALE);
		OS.pango_layout_get_pixel_size (layout, w, h);
		OS.g_object_unref (layout);
	}
	int width = w [0];
	int height = h [0];
	if ((style & SWT.SINGLE) != 0 && message.length () > 0) {
		byte [] buffer = Converter.wcsToMbcs (message, true);
		long layout = GTK.gtk_widget_create_pango_layout (handle, buffer);
		Arrays.fill (buffer, (byte) 0);
		OS.pango_layout_get_pixel_size (layout, w, h);
		OS.g_object_unref (layout);
		width = Math.max (width, w [0]);
	}
	if ((style & SWT.SEARCH) != 0) {
		// GtkSearchEntry have more padding than GtkEntry
		GtkBorder tmp = new GtkBorder();
		long context = GTK.gtk_widget_get_style_context (handle);
		int state_flag = GTK.gtk_widget_get_state_flags(handle);
		gtk_style_context_get_padding(context, state_flag, tmp);
		width += tmp.left + tmp.right;
	}
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	width = wHint == SWT.DEFAULT ? width : wHint;
	height = hHint == SWT.DEFAULT ? height : hHint;
	Rectangle trim = computeTrimInPixels (0, 0, width, height);
	return new Point (trim.width, trim.height);
}

@Override
Rectangle computeTrimInPixels (int x, int y, int width, int height) {
	checkWidget ();
	Rectangle trim = super.computeTrimInPixels (x, y, width, height);
	int xborder = 0, yborder = 0;
	if ((style & SWT.SINGLE) != 0) {
		GtkBorder tmp = new GtkBorder();
		long context = GTK.gtk_widget_get_style_context (handle);
		int state_flag = GTK.gtk_widget_get_state_flags(handle);
		gtk_style_context_get_padding(context, state_flag, tmp);
		trim.x -= tmp.left;
		trim.y -= tmp.top;
		trim.width += tmp.left + tmp.right;
		if (tmp.bottom == 0 && tmp.top == 0) {
			Point widthNative = computeNativeSize(handle, trim.width, SWT.DEFAULT, true);
			trim.height = widthNative.y;
		} else {
			trim.height += tmp.top + tmp.bottom;
		}
		if ((style & SWT.BORDER) != 0) {
			int state = GTK.gtk_widget_get_state_flags(handle);
			gtk_style_context_get_border(context, state, tmp);
			trim.x -= tmp.left;
			trim.y -= tmp.top;
			trim.width += tmp.left + tmp.right;
			trim.height += tmp.top + tmp.bottom;
		}
		if (!GTK.GTK4 ||  ((style & SWT.SEARCH) == 0) ) {
			GdkRectangle icon_area = new GdkRectangle();
			GTK.gtk_entry_get_icon_area(handle, GTK.GTK_ENTRY_ICON_PRIMARY, icon_area);
			trim.x -= icon_area.width;
			trim.width += icon_area.width;
			GTK.gtk_entry_get_icon_area(handle, GTK.GTK_ENTRY_ICON_SECONDARY, icon_area);
			trim.width += icon_area.width;
		}
	} else {
		int borderWidth = gtk_container_get_border_width_or_margin (handle);
		xborder += borderWidth;
		yborder += borderWidth;
	}
	/*
	 * Focus line width is done via CSS in GTK4, and does not contribute
	 * to the size of the widget.
	 */
	if (!GTK.GTK4) {
		int [] property = new int [1];
		GTK3.gtk_widget_style_get (handle, OS.interior_focus, property, 0);
		if (property [0] == 0) {
			GTK3.gtk_widget_style_get (handle, OS.focus_line_width, property, 0);
			xborder += property [0];
			yborder += property [0];
		}
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
		if (GTK.GTK4) {
			GTK4.gtk_widget_activate_action(textHandle, OS.action_copy_clipboard, null);
		} else {
			GTK3.gtk_editable_copy_clipboard(handle);
		}
	} else {
		long clipboard = GTK.GTK4 ? GTK4.gtk_widget_get_clipboard(handle) : GTK3.gtk_clipboard_get(GDK.GDK_NONE);
		clearSegments (true);
		GTK.gtk_text_buffer_copy_clipboard (bufferHandle, clipboard);
		applySegments ();
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
		if (GTK.GTK4) {
			GTK4.gtk_widget_activate_action(textHandle, OS.action_cut_clipboard, null);
		} else {
			GTK3.gtk_editable_cut_clipboard(handle);
		}
	} else {
		long clipboard = GTK.GTK4 ? GTK4.gtk_widget_get_clipboard(handle) : GTK3.gtk_clipboard_get(GDK.GDK_NONE);
		clearSegments (true);
		GTK.gtk_text_buffer_cut_clipboard (bufferHandle, clipboard, GTK.gtk_text_view_get_editable (handle));
		applySegments ();
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

@Override
GdkRGBA defaultBackground () {
	return display.getSystemColor(SWT.COLOR_LIST_BACKGROUND).handle;
}

@Override
void deregister () {
	super.deregister ();
	if (bufferHandle != 0) display.removeWidget (bufferHandle);
	if (imContext != 0) display.removeWidget (imContext);
}

@Override
boolean dragDetect (int x, int y, boolean filter, boolean dragOnTimeout, boolean [] consume) {
	return false;
}

@Override
long eventWindow () {
	if ((style & SWT.SINGLE) != 0) {
		/*
		 * Single-line Text (GtkEntry in GTK) uses a GDK_INPUT_ONLY
		 * internal window. This window can't be used for any kind
		 * of painting, but this is the window to which functions
		 * like Control.setCursor() should apply.
		 */
		long window = super.paintWindow ();
		long children = GDK.gdk_window_get_children (window);
		if (children != 0) {
			long childrenIterator = children;
			/*
			 * When search or cancel icons are added to Text, those
			 * icon window(s) are added to the beginning of the list.
			 * In order to always return the correct window for Text,
			 * browse to the end of the list.
			 */
			do {
				window = OS.g_list_data (childrenIterator);
			} while ((childrenIterator = OS.g_list_next (childrenIterator)) != 0);
		}
		OS.g_list_free (children);
		return window;
	} else {
		return paintWindow ();
	}
}

@Override
boolean filterKey (long event) {
	int time = GDK.gdk_event_get_time (event);
	if (time != lastEventTime) {
		lastEventTime = time;
		if (imContext != 0) {
			if (GTK.GTK4)
				return GTK4.gtk_im_context_filter_keypress (imContext, event);
			else
				return GTK3.gtk_im_context_filter_keypress (imContext, event);
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
		if (imContext != 0) {
			if (GTK.GTK4)
				GTK4.gtk_im_context_filter_keypress (imContext, gdkEventKey);
			else
				GTK3.gtk_im_context_filter_keypress (imContext, gdkEventKey);

			gdkEventKey = -1;
			return;
		}
	}
	gdkEventKey = 0;
}

@Override
int getBorderWidthInPixels () {
	checkWidget();
	if ((style & SWT.MULTI) != 0) return super.getBorderWidthInPixels ();
	if ((this.style & SWT.BORDER) != 0) {
		return getThickness (handle).x;
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
	long mark = GTK.gtk_text_buffer_get_insert (bufferHandle);
	GTK.gtk_text_buffer_get_iter_at_mark (bufferHandle, position, mark);
	return GTK.gtk_text_iter_get_line (position);
}

/**
 * Returns a point describing the location of the caret relative
 * to the receiver.
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
	return DPIUtil.autoScaleDown(getCaretLocationInPixels());
}

Point getCaretLocationInPixels () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) {
		int index = GTK.gtk_editable_get_position (handle);
		index = GTK3.gtk_entry_text_index_to_layout_index (handle, index);
		int [] offset_x = new int [1], offset_y = new int [1];
		GTK3.gtk_entry_get_layout_offsets (handle, offset_x, offset_y);
		long layout = GTK3.gtk_entry_get_layout (handle);
		PangoRectangle pos = new PangoRectangle ();
		OS.pango_layout_index_to_pos (layout, index, pos);
		int x = offset_x [0] + OS.PANGO_PIXELS (pos.x) - getBorderWidthInPixels ();
		int y = offset_y [0] + OS.PANGO_PIXELS (pos.y);
		return new Point (x, y);
	} else {
		byte[] position = new byte[ITER_SIZEOF];
		long mark = GTK.gtk_text_buffer_get_insert(bufferHandle);
		GTK.gtk_text_buffer_get_iter_at_mark(bufferHandle, position, mark);
		GdkRectangle rect = new GdkRectangle();
		GTK.gtk_text_view_get_iter_location(handle, position, rect);
		int[] x = new int[1];
		int[] y  = new int[1];
		GTK.gtk_text_view_buffer_to_window_coords(handle, GTK.GTK_TEXT_WINDOW_TEXT, rect.x, rect.y, x, y);
		return new Point (x[0], y[0]);
	}
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
		long ptr = GTK3.gtk_entry_get_text (handle);
		result = (int)OS.g_utf8_offset_to_utf16_offset (ptr, GTK.gtk_editable_get_position (handle));
	} else {
		byte [] position = new byte [ITER_SIZEOF];
		long mark = GTK.gtk_text_buffer_get_insert (bufferHandle);
		GTK.gtk_text_buffer_get_iter_at_mark (bufferHandle, position, mark);
		byte [] zero = new byte [ITER_SIZEOF];
		GTK.gtk_text_buffer_get_iter_at_offset(bufferHandle, zero, 0);
		long ptr = GTK.gtk_text_buffer_get_text (bufferHandle, zero, position, true);
		result = (int)OS.g_utf8_offset_to_utf16_offset (ptr, GTK.gtk_text_iter_get_offset (position));
		OS.g_free (ptr);
	}
	return untranslateOffset (result);
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
		if (GTK.GTK4) {
			result = GTK4.gtk_entry_get_text_length(handle);
		} else {
			long str = GTK3.gtk_entry_get_text(handle);
			result = (int)OS.g_utf16_strlen(str, -1);
		}
	} else {
		byte [] startIter =  new byte [ITER_SIZEOF];
		byte [] endIter =  new byte [ITER_SIZEOF];
		GTK.gtk_text_buffer_get_bounds (bufferHandle, startIter, endIter);
		long ptr = GTK.gtk_text_buffer_get_text (bufferHandle, startIter, endIter, true);
		result = (int)OS.g_utf16_strlen(ptr, -1);
		OS.g_free (ptr);
	}
	return untranslateOffset (result);
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
		if (!GTK.gtk_entry_get_visibility (handle)) {
			return GTK.gtk_entry_get_invisible_char (handle);
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
		return GTK.gtk_editable_get_editable (handle);
	}
	return GTK.gtk_text_view_get_editable (handle);
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
	return GTK.gtk_text_buffer_get_line_count (bufferHandle);
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
	long fontDesc = getFontDescription ();
	int result = fontHeight (fontDesc, handle);
	OS.pango_font_description_free (fontDesc);
	return result;
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
@Override
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
		long layout = GTK3.gtk_entry_get_layout (handle);
		OS.pango_layout_xy_to_index (layout, point.x * OS.PANGO_SCALE, point.y * OS.PANGO_SCALE, index, trailing);
		long ptr = OS.pango_layout_get_text (layout);
		position = (int)OS.g_utf16_pointer_to_offset (ptr, ptr + index[0]) + trailing[0];
	} else {
		byte [] p = new byte [ITER_SIZEOF];
		GTK.gtk_text_view_get_iter_at_location (handle, p, point.x, point.y);
		byte [] zero = new byte [ITER_SIZEOF];
		GTK.gtk_text_buffer_get_iter_at_offset(bufferHandle, zero, 0);
		long ptr = GTK.gtk_text_buffer_get_text (bufferHandle, zero, p, true);
		position = (int)OS.g_utf8_offset_to_utf16_offset (ptr, GTK.gtk_text_iter_get_offset (p));
		OS.g_free (ptr);
	}
	return untranslateOffset (position);
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
public Point getSelection() {
	checkWidget();

	Point selection;
	if ((style & SWT.SINGLE) != 0) {
		int[] start = new int[1];
		int[] end = new int[1];
		GTK.gtk_editable_get_selection_bounds(handle, start, end);
		long str;
		if (GTK.GTK4) {
			str = GTK.gtk_entry_buffer_get_text(GTK4.gtk_entry_get_buffer(handle));
		} else {
			str = GTK3.gtk_entry_get_text(handle);
		}
		start[0] = (int)OS.g_utf8_offset_to_utf16_offset(str, start[0]);
		end[0] = (int)OS.g_utf8_offset_to_utf16_offset(str, end[0]);
		selection = new Point(start[0], end[0]);
	} else {
		byte [] startIter =  new byte [ITER_SIZEOF];
		byte [] endIter =  new byte [ITER_SIZEOF];
		GTK.gtk_text_buffer_get_selection_bounds (bufferHandle, startIter, endIter);
		byte [] zero = new byte [ITER_SIZEOF];
		GTK.gtk_text_buffer_get_iter_at_offset(bufferHandle, zero, 0);
		long ptr = GTK.gtk_text_buffer_get_text (bufferHandle, zero, endIter, true);
		int start = (int)OS.g_utf8_offset_to_utf16_offset (ptr, GTK.gtk_text_iter_get_offset (startIter));
		int end = (int)OS.g_utf8_offset_to_utf16_offset (ptr, GTK.gtk_text_iter_get_offset (endIter));
		OS.g_free (ptr);
		selection = new Point (start, end);
	}

	selection.x = untranslateOffset(selection.x);
	selection.y = untranslateOffset(selection.y);
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
public String getSelectionText() {
	checkWidget();

	Point selection = getSelection();
	return getText().substring(selection.x, selection.y);
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
	byte[] buffer = Converter.wcsToMbcs(" ", true);
	long layout = GTK.gtk_widget_create_pango_layout (handle, buffer);
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
 * <p>
 * Note: Use this API to prevent the text from being written into a String
 * object whose lifecycle is outside of your control. This can help protect
 * the text, for example, when the widget is used as a password field.
 * However, the text can't be protected if an {@link SWT#Segments} or
 * {@link SWT#Verify} listener has been added to the widget.
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
	long address;

	if ((style & SWT.SINGLE) != 0) {
		if (GTK.GTK4) {
			address = GTK.gtk_entry_buffer_get_text(bufferHandle);
		} else {
			address = GTK3.gtk_entry_get_text(handle);
		}
	} else {
		byte [] start =  new byte [ITER_SIZEOF];
		byte [] end =  new byte [ITER_SIZEOF];
		GTK.gtk_text_buffer_get_bounds (bufferHandle, start, end);
		address = GTK.gtk_text_buffer_get_text (bufferHandle, start, end, true);
	}
	if (address == 0) return new char[0];
	int length = C.strlen (address);
	byte [] buffer = new byte [length];
	C.memmove (buffer, address, length);
	if ((style & SWT.MULTI) != 0) OS.g_free (address);

	char [] result = Converter.mbcsToWcs (buffer);
	Arrays.fill (buffer, (byte) 0);
	if (segments != null) {
		result = deprocessText (result, 0, -1);
	}
	return result;
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
	int limit = GTK.gtk_entry_get_max_length (handle);
	return limit == 0 ? 0xFFFF : untranslateOffset (limit);
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
	/*
	 * Feature in GTK: GtkTextView widgets are subject to line validation
	 * which happens during idle. This causes GtkTextIter to not update quickly
	 * enough when changes are added to the text buffer. The fix is to use a
	 * GtkTextMark to track the precise index, then convert it back to a
	 * GtkTextIter when getTopIndex() is called. See bug 487467.
	 *
	 * NOTE: to cover cases where getTopIndex() is called without setTopIndex()
	 * being called, we fetch the current GtkAdjustment value and cache it for
	 * comparison. In getTopIndex() we compare the current value with the cached
	 * one to see if the user has scrolled/moved the viewport using the GUI.
	 * If so, we use the old method of fetching the top index.
	 */
	long vAdjustment = GTK.gtk_scrollable_get_vadjustment (handle);
	currentAdjustment = GTK.gtk_adjustment_get_value (vAdjustment);
	if (cachedAdjustment == currentAdjustment) {
		// If indexMark is 0, fetch topIndex using the old method
		if (indexMark != 0) {
			GTK.gtk_text_buffer_get_iter_at_mark (bufferHandle, position, indexMark);
			return GTK.gtk_text_iter_get_line (position);
		}
	}
	GdkRectangle rect = new GdkRectangle ();
	GTK.gtk_text_view_get_visible_rect (handle, rect);
	GTK.gtk_text_view_get_line_at_y (handle, position, rect.y, null);
	return GTK.gtk_text_iter_get_line (position);
}

/**
 * Returns the top SWT logical point.
 * <p>
 * The top point is the SWT logical point position of the line
 * that is currently at the top of the widget.  On
 * some platforms, a text widget can be scrolled by
 * points instead of lines so that a partial line
 * is displayed at the top of the widget.
 * </p><p>
 * The top SWT logical point changes when the widget is scrolled.
 * The top SWT logical point does not include the widget trimming.
 * </p>
 *
 * @return the SWT logical point position of the top line
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopPixel () {
	checkWidget ();
	return DPIUtil.autoScaleDown(getTopPixelInPixels());
}

int getTopPixelInPixels () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return 0;
	byte [] position = new byte [ITER_SIZEOF];
	GdkRectangle rect = new GdkRectangle ();
	GTK.gtk_text_view_get_visible_rect (handle, rect);
	int [] lineTop = new int[1];
	GTK.gtk_text_view_get_line_at_y (handle, position, rect.y, lineTop);
	return lineTop [0];
}

@Override
long gtk_activate (long widget) {
	sendSelectionEvent (SWT.DefaultSelection);
	return 0;
}

@Override
long gtk_button_press_event (long widget, long event) {
	long result;
	result = super.gtk_button_press_event (widget, event);
	if (result != 0) return result;
	int eventType = GDK.gdk_event_get_event_type(event);
	if (!doubleClick) {
		switch (eventType) {
			case GDK.GDK_2BUTTON_PRESS:
			case GDK.GDK_3BUTTON_PRESS:
				return 1;
		}
	}
	return result;
}

@Override
long gtk_changed (long widget) {
	/*
	* Feature in GTK.  When the user types, GTK positions
	* the caret after sending the changed signal.  This
	* means that application code that attempts to position
	* the caret during a changed signal will fail.  The fix
	* is to post the modify event when the user is typing.
	*/
	boolean keyPress = false;
	long eventPtr = GTK.GTK4 ? 0 : GTK3.gtk_get_current_event ();
	if (eventPtr != 0) {
		int eventType = GDK.gdk_event_get_event_type(eventPtr);
		eventType = fixGdkEventTypeValues(eventType);
		switch (eventType) {
			case GDK.GDK_KEY_PRESS:
				keyPress = true;
				break;
		}
		gdk_event_free (eventPtr);
	}
	if (keyPress) {
		postEvent (SWT.Modify);
	} else {
		sendEvent (SWT.Modify);
	}
	if ((style & SWT.SEARCH) != 0 && !GTK.GTK4) {
		if ((style & SWT.ICON_CANCEL) == 0) {
			// Default GtkSearchEntry shows "clear" icon when there is text, manually revert this
			// when "cancel" icon style is not set
			GTK.gtk_entry_set_icon_from_icon_name(handle, GTK.GTK_ENTRY_ICON_SECONDARY, null);
		}
	}
	return 0;
}

@Override
long gtk_commit (long imContext, long text) {
	if (text == 0) return 0;
	if ((style & SWT.SINGLE) != 0) {
		if (!GTK.gtk_editable_get_editable (handle)) return 0;
	}
	int length = C.strlen (text);
	if (length == 0) return 0;
	byte [] buffer = new byte [length];
	C.memmove (buffer, text, length);
	char [] chars = Converter.mbcsToWcs (buffer);
	Arrays.fill (buffer, (byte) 0);
	char [] newChars = sendIMKeyEvent (SWT.KeyDown, 0, chars);
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
	int id = OS.g_signal_lookup (OS.commit, GTK.gtk_im_context_get_type ());
	int mask =  OS.G_SIGNAL_MATCH_DATA | OS.G_SIGNAL_MATCH_ID;
	OS.g_signal_handlers_unblock_matched (imContext, mask, id, 0, 0, 0, handle);
	if (newChars == chars) {
		OS.g_signal_emit_by_name (imContext, OS.commit, text);
	} else {
		buffer = Converter.wcsToMbcs (newChars, true);
		OS.g_signal_emit_by_name (imContext, OS.commit, buffer);
		Arrays.fill (buffer, (byte) 0);
	}
	OS.g_signal_handlers_unblock_matched (imContext, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, COMMIT);
	OS.g_signal_handlers_block_matched (imContext, mask, id, 0, 0, 0, handle);
	if ((style & SWT.SINGLE) != 0) {
		if (fixStart != -1 && fixEnd != -1) {
			GTK.gtk_editable_set_position (handle, fixStart);
			GTK.gtk_editable_select_region (handle, fixStart, fixEnd);
		}
	}
	fixStart = fixEnd = -1;
	return 0;
}

@Override
long gtk_delete_range (long widget, long iter1, long iter2) {
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	byte [] startIter = new byte [ITER_SIZEOF];
	byte [] endIter = new byte [ITER_SIZEOF];
	C.memmove (startIter, iter1, startIter.length);
	C.memmove (endIter, iter2, endIter.length);
	int start = GTK.gtk_text_iter_get_offset (startIter);
	int end = GTK.gtk_text_iter_get_offset (endIter);
	byte [] zero = new byte [ITER_SIZEOF];
	GTK.gtk_text_buffer_get_iter_at_offset(bufferHandle, zero, 0);
	long ptr = GTK.gtk_text_buffer_get_text (bufferHandle, zero, endIter, true);
	start = (int)OS.g_utf8_offset_to_utf16_offset (ptr, start);
	end = (int)OS.g_utf8_offset_to_utf16_offset (ptr, end);
	OS.g_free (ptr);
	String newText = verifyText ("", start, end);
	if (newText == null) {
		/* Remember the selection when the text was deleted */
		GTK.gtk_text_buffer_get_selection_bounds (bufferHandle, startIter, endIter);
		start = GTK.gtk_text_iter_get_offset (startIter);
		end = GTK.gtk_text_iter_get_offset (endIter);
		if (start != end) {
			fixStart = start;
			fixEnd = end;
		}
		OS.g_signal_stop_emission_by_name (bufferHandle, OS.delete_range);
	} else {
		if (newText.length () > 0) {
			byte [] buffer = Converter.wcsToMbcs (newText, false);
			OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_RANGE);
			GTK.gtk_text_buffer_delete (bufferHandle, startIter, endIter);
			OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_RANGE);
			OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEXT_BUFFER_INSERT_TEXT);
			GTK.gtk_text_buffer_insert (bufferHandle, startIter, buffer, buffer.length);
			OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEXT_BUFFER_INSERT_TEXT);
			OS.g_signal_stop_emission_by_name (bufferHandle, OS.delete_range);
			Arrays.fill (buffer, (byte) 0);
		}
	}
	return 0;
}

@Override
long gtk_delete_text (long widget, long start_pos, long end_pos) {
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	long ptr = GTK3.gtk_entry_get_text (handle);
	if (end_pos == -1) end_pos = OS.g_utf8_strlen (ptr, -1);
	int start = (int)OS.g_utf8_offset_to_utf16_offset (ptr, start_pos);
	int end = (int)OS.g_utf8_offset_to_utf16_offset (ptr, end_pos);
	String newText = verifyText ("", start, end);
	if (newText == null) {
		/* Remember the selection when the text was deleted */
		int [] newStart = new int [1], newEnd = new int [1];
		GTK.gtk_editable_get_selection_bounds (handle, newStart, newEnd);
		if (newStart [0] != newEnd [0]) {
			fixStart = newStart [0];
			fixEnd = newEnd [0];
		}
		OS.g_signal_stop_emission_by_name (handle, OS.delete_text);
	} else {
		if (newText.length () > 0) {
			int [] pos = new int [1];
			pos [0] = (int)end_pos;
			byte [] buffer = Converter.wcsToMbcs (newText, false);
			OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			GTK.gtk_editable_insert_text (handle, buffer, buffer.length, pos);
			OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			GTK.gtk_editable_set_position (handle, pos [0]);
			Arrays.fill (buffer, (byte) 0);
		}
	}
	return 0;
}

@Override
long gtk_event_after (long widget, long gdkEvent) {
	if (cursor != null) setCursor (cursor.handle);
	/*
	* Feature in GTK.  The gtk-entry-select-on-focus property is a global
	* setting.  Return it to its default value after the GtkEntry has done
	* its focus in processing so that other widgets (such as the combo)
	* use the correct value.
	*/
	if ((style & SWT.SINGLE) != 0 && display.entrySelectOnFocus) {
		int eventType = GDK.gdk_event_get_event_type(gdkEvent);
		eventType = fixGdkEventTypeValues(eventType);
		switch (eventType) {
			case GDK.GDK_FOCUS_CHANGE:
				boolean [] focusIn = new boolean [1];
				if (GTK.GTK4) {
					focusIn[0] = GDK.gdk_focus_event_get_in(gdkEvent);
				} else {
					GdkEventFocus gdkEventFocus = new GdkEventFocus ();
					GTK3.memmove (gdkEventFocus, gdkEvent, GdkEventFocus.sizeof);
					focusIn[0] = gdkEventFocus.in != 0;
				}
				if (focusIn[0]) {
					long settings = GTK.gtk_settings_get_default ();
					OS.g_object_set (settings, GTK.gtk_entry_select_on_focus, true, 0);
				}
				break;
		}
	}
	return super.gtk_event_after (widget, gdkEvent);
}

@Override
long gtk_draw (long widget, long cairo) {
	if ((state & OBSCURED) != 0) return 0;
	long result = super.gtk_draw (widget, cairo);
	return result;
}

@Override
boolean mustBeVisibleOnInitBounds() {
	// Bug 542940: Workaround to avoid NPE, make Text visible on initialization
	return true;
}

@Override
long gtk_focus_out_event (long widget, long event) {
	fixIM ();
	return super.gtk_focus_out_event (widget, event);
}

@Override
long gtk_grab_focus (long widget) {
	long result = super.gtk_grab_focus (widget);
	/*
	* Feature in GTK.  GtkEntry widgets select their text on focus in,
	* clearing the previous selection.  This behavior is controlled by
	* the gtk-entry-select-on-focus property.  The fix is to disable
	* this property when a GtkEntry is given focus and restore it after
	* the entry has done focus in processing.
	*/
	if ((style & SWT.SINGLE) != 0 && display.entrySelectOnFocus) {
		long settings = GTK.gtk_settings_get_default ();
		OS.g_object_set (settings, GTK.gtk_entry_select_on_focus, false, 0);
	}
	return result;
}

@Override
long gtk_icon_release (long widget, long icon_pos, long event) {
	Event e = new Event();
	if (icon_pos == GTK.GTK_ENTRY_ICON_PRIMARY) {
		e.detail = SWT.ICON_SEARCH;
	} else {
		e.detail = SWT.ICON_CANCEL;
	}
	sendSelectionEvent (SWT.DefaultSelection, e, false);
	return 0;
}

@Override
long gtk_insert_text (long widget, long new_text, long new_text_length, long position) {
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	if (new_text == 0 || new_text_length == 0) return 0;
	byte [] buffer = new byte [(int)new_text_length];
	C.memmove (buffer, new_text, buffer.length);
	String oldText = new String (Converter.mbcsToWcs (buffer));
	int [] pos = new int [1];
	C.memmove (pos, position, 4);
	long ptr = GTK3.gtk_entry_get_text (handle);
	if (pos [0] == -1) pos [0] = (int)OS.g_utf8_strlen (ptr, -1);
	/* Use the selection when the text was deleted */
	int start = pos [0], end = pos [0];
	if (fixStart != -1 && fixEnd != -1) {
		start = pos [0] = fixStart;
		end = fixEnd;
		fixStart = fixEnd = -1;
	}
	start = (int)OS.g_utf8_offset_to_utf16_offset (ptr, start);
	end = (int)OS.g_utf8_offset_to_utf16_offset (ptr, end);
	String newText = verifyText (oldText, start, end);
	if (newText != oldText && handle != 0) {
		int [] newStart = new int [1], newEnd = new int [1];
		GTK.gtk_editable_get_selection_bounds (handle, newStart, newEnd);
		if (newText != null) {
			if (newStart [0] != newEnd [0]) {
				OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
				OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				GTK.gtk_editable_delete_selection (handle);
				OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
				OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			}
			byte [] buffer3 = Converter.wcsToMbcs (newText, false);
			OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			GTK.gtk_editable_insert_text (handle, buffer3, buffer3.length, pos);
			OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			newStart [0] = newEnd [0] = pos [0];
		}
		pos [0] = newEnd [0];
		if (newStart [0] != newEnd [0]) {
			fixStart = newStart [0];
			fixEnd = newEnd [0];
		}
		C.memmove (position, pos, 4);
		OS.g_signal_stop_emission_by_name (handle, OS.insert_text);
	}
	return 0;
}

@Override
long gtk_key_press_event (long widget, long event) {
	boolean handleSegments = false, segmentsCleared = false;
	if (hooks (SWT.Segments) || filters (SWT.Segments)) {
		int length = 0;
		int [] state = new int[1];

		if (GTK.GTK4) {
			/* TODO: GTK4 no access to key event string */
			state[0] = GDK.gdk_event_get_modifier_state(event);
		} else {
			GDK.gdk_event_get_state(event, state);

			GdkEventKey gdkEvent = new GdkEventKey ();
			GTK3.memmove(gdkEvent, event, GdkEventKey.sizeof);
			length = gdkEvent.length;
		}

		if (length > 0 && (state[0] & (GDK.GDK_MOD1_MASK | GDK.GDK_CONTROL_MASK)) == 0) {
			handleSegments = true;
			if (segments != null) {
				clearSegments (true);
				segmentsCleared = true;
			}
		}
	}
	long result = super.gtk_key_press_event (widget, event);
	if (result != 0) fixIM ();
	if (gdkEventKey == -1) result = 1;
	gdkEventKey = 0;
	if (handleSegments && (result != 0 || segmentsCleared)) {
		applySegments ();
	}
	return result;
}

@Override
long gtk_populate_popup (long widget, long menu) {
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		GTK.gtk_widget_set_direction (menu, GTK.GTK_TEXT_DIR_RTL);
		GTK3.gtk_container_forall (menu, display.setDirectionProc, GTK.GTK_TEXT_DIR_RTL);
	}
	return 0;
}

@Override
long gtk_text_buffer_insert_text (long widget, long iter, long text, long length) {
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	byte [] position = new byte [ITER_SIZEOF];
	C.memmove (position, iter, position.length);
	/* Use the selection when the text was deleted */
	int start = GTK.gtk_text_iter_get_offset (position), end = start;
	if (fixStart != -1 && fixEnd != -1) {
		start = fixStart;
		end = fixEnd;
		fixStart = fixEnd = -1;
	}
	byte [] zero = new byte [ITER_SIZEOF];
	GTK.gtk_text_buffer_get_iter_at_offset(bufferHandle, zero, 0);
	long ptr = GTK.gtk_text_buffer_get_text (bufferHandle, zero, position, true);
	start = (int)OS.g_utf8_offset_to_utf16_offset (ptr, start);
	end = (int)OS.g_utf8_offset_to_utf16_offset (ptr, end);
	OS.g_free(ptr);
	byte [] buffer = new byte [(int)length];
	C.memmove (buffer, text, buffer.length);
	String oldText = new String (Converter.mbcsToWcs (buffer));
	String newText = verifyText (oldText, start, end);
	if (newText == null) {
		OS.g_signal_stop_emission_by_name (bufferHandle, OS.insert_text);
	} else {
		if (newText != oldText) {
			byte [] buffer1 = Converter.wcsToMbcs (newText, false);
			OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEXT_BUFFER_INSERT_TEXT);
			GTK.gtk_text_buffer_insert (bufferHandle, iter, buffer1, buffer1.length);
			OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEXT_BUFFER_INSERT_TEXT);
			OS.g_signal_stop_emission_by_name (bufferHandle, OS.insert_text);
		}
	}
	return 0;
}

@Override
void hookEvents() {
	super.hookEvents();
	if ((style & SWT.SINGLE) != 0) {
		OS.g_signal_connect_closure(handle, OS.changed, display.getClosure (CHANGED), true);
		OS.g_signal_connect_closure(handle, OS.insert_text, display.getClosure (INSERT_TEXT), false);
		OS.g_signal_connect_closure(handle, OS.delete_text, display.getClosure (DELETE_TEXT), false);
		OS.g_signal_connect_closure(handle, OS.activate, display.getClosure (ACTIVATE), false);

		if (!GTK.GTK4) {
			OS.g_signal_connect_closure(handle, OS.grab_focus, display.getClosure (GRAB_FOCUS), false);
		}
		if ((style & SWT.SEARCH) != 0 && !GTK.GTK4) {
			OS.g_signal_connect_closure(handle, OS.icon_release, display.getClosure (ICON_RELEASE), false);
		}
	} else {
		OS.g_signal_connect_closure(bufferHandle, OS.changed, display.getClosure (CHANGED), false);
		OS.g_signal_connect_closure(bufferHandle, OS.insert_text, display.getClosure (TEXT_BUFFER_INSERT_TEXT), false);
		OS.g_signal_connect_closure(bufferHandle, OS.delete_range, display.getClosure (DELETE_RANGE), false);
	}

	if (imContext != 0) {
		OS.g_signal_connect_closure(imContext, OS.commit, display.getClosure (COMMIT), false);
		int id = OS.g_signal_lookup(OS.commit, GTK.gtk_im_context_get_type ());
		int mask =  OS.G_SIGNAL_MATCH_DATA | OS.G_SIGNAL_MATCH_ID;
		OS.g_signal_handlers_block_matched(imContext, mask, id, 0, 0, 0, handle);
	}

	if (!GTK.GTK4) {
		OS.g_signal_connect_closure(handle, OS.populate_popup, display.getClosure (POPULATE_POPUP), false);
	}

	// In GTK4, these event signals belong to GtkText which is the only child of GtkEntry
	long eventHandle = 0;

	if (GTK.GTK4) {
		eventHandle = ((style & SWT.SINGLE) != 0) ? textHandle : handle;
	} else {
		eventHandle = handle;
	}

	OS.g_signal_connect_closure(eventHandle, OS.backspace, display.getClosure (BACKSPACE), false);
	OS.g_signal_connect_closure(eventHandle, OS.backspace, display.getClosure (BACKSPACE_INVERSE), true);
	OS.g_signal_connect_closure(eventHandle, OS.copy_clipboard, display.getClosure (COPY_CLIPBOARD), false);
	OS.g_signal_connect_closure(eventHandle, OS.copy_clipboard, display.getClosure (COPY_CLIPBOARD_INVERSE), true);
	OS.g_signal_connect_closure(eventHandle, OS.cut_clipboard, display.getClosure (CUT_CLIPBOARD), false);
	OS.g_signal_connect_closure(eventHandle, OS.cut_clipboard, display.getClosure (CUT_CLIPBOARD_INVERSE), true);
	OS.g_signal_connect_closure(eventHandle, OS.paste_clipboard, display.getClosure (PASTE_CLIPBOARD), false);
	OS.g_signal_connect_closure(eventHandle, OS.paste_clipboard, display.getClosure (PASTE_CLIPBOARD_INVERSE), true);
	OS.g_signal_connect_closure(eventHandle, OS.delete_from_cursor, display.getClosure (DELETE_FROM_CURSOR), false);
	OS.g_signal_connect_closure(eventHandle, OS.delete_from_cursor, display.getClosure (DELETE_FROM_CURSOR_INVERSE), true);
	OS.g_signal_connect_closure(eventHandle, OS.move_cursor, display.getClosure (MOVE_CURSOR), false);
	OS.g_signal_connect_closure(eventHandle, OS.move_cursor, display.getClosure (MOVE_CURSOR_INVERSE), true);
	OS.g_signal_connect_closure(eventHandle, OS.direction_changed, display.getClosure (DIRECTION_CHANGED), true);
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
	clearSegments (true);
	byte [] buffer = Converter.wcsToMbcs (string, false);
	if ((style & SWT.SINGLE) != 0) {
		int [] start = new int [1], end = new int [1];
		GTK.gtk_editable_get_selection_bounds (handle, start, end);
		GTK.gtk_editable_delete_selection (handle);
		GTK.gtk_editable_insert_text (handle, buffer, buffer.length, start);
		GTK.gtk_editable_set_position (handle, start [0]);
	} else {
		byte [] start =  new byte [ITER_SIZEOF];
		byte [] end =  new byte [ITER_SIZEOF];
		if (GTK.gtk_text_buffer_get_selection_bounds (bufferHandle, start, end)) {
			GTK.gtk_text_buffer_delete (bufferHandle, start, end);
		}
		GTK.gtk_text_buffer_insert (bufferHandle, start, buffer, buffer.length);
		GTK.gtk_text_buffer_place_cursor (bufferHandle, start);
		scrollIfNotVisible(start, null, true);
	}
	applySegments ();
}


/**
 * Methods that insert or select text should not modify the topIndex
 * of the viewer.
 *
 * To avoid this issue we calculate the visible area, positions of the
 * topIndex, and the insertion/selection points. If the insertion/selection points
 * are outside the visible area, then scroll to them. Otherwise do nothing,
 * which preserves the topIndex.
 *
 * @param iter the GtkTextIter representing the insertion/selection point
 * @param scrollTo the GtkTextIter representing the point to be scrolled to (can be null)
 * @param insert true if insertion is being performed, false if selection
 */
private void scrollIfNotVisible(byte [] iter, byte [] scrollTo, boolean insert) {
	GdkRectangle rect = new GdkRectangle ();
	int distanceTopIndex, numLinesVisible, lineHeight;
	int[] insertionCoordinates = new int [1];
	int[] topIndexCoordinates = new int [1];
	byte [] indexIter =  new byte [ITER_SIZEOF];

	// Calculate the visible area
	GTK.gtk_text_view_get_visible_rect (handle, rect);
	lineHeight = getLineHeight ();
	numLinesVisible = rect.height / lineHeight;

	// Get the coordinates of the insertion/selection point
	GTK.gtk_text_view_get_line_yrange (handle, iter, insertionCoordinates, null);

	// If we have a topIndex, calculate whether the insertion/selection point
	// is in the visible area
	if (indexMark != 0) {
		GTK.gtk_text_buffer_get_iter_at_mark (bufferHandle, indexIter, indexMark);
		GTK.gtk_text_view_get_line_yrange (handle, indexIter, topIndexCoordinates, null);
		distanceTopIndex = (insertionCoordinates [0] - topIndexCoordinates [0]) / lineHeight;

		// If it is not in the visible area, scroll to the insertion/selection point
		if (distanceTopIndex >= numLinesVisible) {
			if (insert) {
				long mark = GTK.gtk_text_buffer_get_insert (bufferHandle);
				GTK.gtk_text_view_scroll_to_mark (handle, mark, 0, true, 0, 0);
			} else if (scrollTo != null) {
				GTK.gtk_text_view_scroll_to_iter (handle, scrollTo, 0, true, 0, 0);
			}
		}
	} else {
		// Set topIndex to 0 and perform visibility calculations based on that
		topIndexCoordinates [0] = 0;
		distanceTopIndex = (insertionCoordinates [0] - topIndexCoordinates [0]) / lineHeight;
		if (distanceTopIndex >= numLinesVisible) {
			if (scrollTo != null && !insert) {
				GTK.gtk_text_view_scroll_to_iter (handle, scrollTo, 0, true, 0, 0);
			} else if (insert) {
				long mark = GTK.gtk_text_buffer_get_insert (bufferHandle);
				GTK.gtk_text_view_scroll_to_mark (handle, mark, 0, true, 0, 0);
			}
		}
	}
}

@Override
long paintWindow () {
	if ((style & SWT.SINGLE) != 0) {
		return super.paintWindow ();
	} else {
		if (GTK.GTK4) {
			// TODO: this function has been removed on GTK4, check bug 561444
			return 0;
		} else {
			GTK.gtk_widget_realize (handle);
			return GTK3.gtk_text_view_get_window (handle, GTK.GTK_TEXT_WINDOW_TEXT);
		}
	}
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
		if (GTK.GTK4) {
			GTK4.gtk_widget_activate_action(textHandle, OS.action_paste_clipboard, null);
		} else {
			GTK3.gtk_editable_paste_clipboard (handle);
		}
	} else {
		long clipboard = GTK.GTK4 ? GTK4.gtk_widget_get_clipboard(handle) : GTK3.gtk_clipboard_get(GDK.GDK_NONE);
		clearSegments (true);
		GTK.gtk_text_buffer_paste_clipboard (bufferHandle, clipboard, null, GTK.gtk_text_view_get_editable (handle));
		applySegments ();
	}
}

@Override
void register () {
	super.register ();
	if (bufferHandle != 0) display.addWidget (bufferHandle, this);
	if (imContext != 0) display.addWidget (imContext, this);
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	fixIM ();
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
 * @see SegmentEvent
 * @see SegmentListener
 * @see #addSegmentListener
 *
 * @since 3.8
 */
public void removeSegmentListener (SegmentListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	eventTable.unhook (SWT.Segments, listener);
	clearSegments (true);
	applySegments ();
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
		GTK.gtk_editable_select_region (handle, 0, -1);
	} else {
		byte [] start =  new byte [ITER_SIZEOF];
		byte [] end =  new byte [ITER_SIZEOF];
		GTK.gtk_text_buffer_get_iter_at_offset (bufferHandle, start, 0);
		GTK.gtk_text_buffer_get_end_iter (bufferHandle, end);
		GTK.gtk_text_buffer_select_range(bufferHandle, start, end);
	}
}

@Override
GdkRGBA getContextBackgroundGdkRGBA () {
	if (background != null && (state & BACKGROUND) != 0) {
		return background;
	}
	return defaultBackground();
}

@Override
GdkRGBA getContextColorGdkRGBA () {
	if (foreground != null) {
		return foreground;
	} else {
		return display.COLOR_WIDGET_FOREGROUND_RGBA;
	}
}

@Override
void setBackgroundGdkRGBA (long context, long handle, GdkRGBA rgba) {
	/* Setting the background color overrides the selected background color.
	 * To prevent this, we need to re-set the default. This can be done with CSS
	 * on GTK3.16+, or by using GtkStateFlags as an argument to
	 * gtk_widget_override_background_color() on versions of GTK3 less than 3.16.
	 */
	if (rgba == null) {
		background = defaultBackground();
	} else {
		background = rgba;
	}
	GdkRGBA selectedBackground = display.getSystemColor(SWT.COLOR_LIST_SELECTION).handle;
	GdkRGBA selectedForeground = display.getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT).handle;
	String css;
	String properties;
	if ((style & SWT.SINGLE) != 0) {
		properties = "entry {background: " + display.gtk_rgba_to_css_string(background) + ";}\n"
				+ "entry:selected {background-color: " + display.gtk_rgba_to_css_string(selectedBackground) + ";}\n"
				+ "entry selection {color: " + display.gtk_rgba_to_css_string(selectedForeground) + ";}";
	} else {
		properties = "textview text {background-color: " + display.gtk_rgba_to_css_string(background) + ";}\n"
				+ "textview text:selected {background-color: " + display.gtk_rgba_to_css_string(selectedBackground) + ";}\n"
				+ "textview text selection {color: " + display.gtk_rgba_to_css_string(selectedForeground) + ";}";
	}
	css = properties;

	// Cache background color
	cssBackground = css;

	// Apply background color and any foreground color
	String finalCss = display.gtk_css_create_css_color_string (cssBackground, cssForeground, SWT.BACKGROUND);
	gtk_css_provider_load_from_css(context, finalCss);
}

@Override
void setForegroundGdkRGBA (GdkRGBA rgba) {
	foreground = rgba;
	GdkRGBA toSet = rgba == null ? display.COLOR_WIDGET_FOREGROUND_RGBA : rgba;
	setForegroundGdkRGBA (handle, toSet);
}

@Override
void setCursor (long cursor) {
	long defaultCursor = 0;
	if (cursor == 0) {
		if (GTK.GTK4) {
			defaultCursor = GDK.gdk_cursor_new_from_name("xterm", 0);
		} else {
			defaultCursor = GDK.gdk_cursor_new_from_name (GDK.gdk_display_get_default(), "xterm");
		}
	}
	super.setCursor (cursor != 0 ? cursor : defaultCursor);
	if (cursor == 0) OS.g_object_unref (defaultCursor);
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
public void setEchoChar(char echo) {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) {
		GTK.gtk_entry_set_visibility(handle, echo == '\0');
		GTK.gtk_entry_set_invisible_char(handle, echo);
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
		GTK.gtk_editable_set_editable (handle, editable);
	} else {
		GTK.gtk_text_view_set_editable (handle, editable);
	}
}

@Override
void setFontDescription (long font) {
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
	if ((style & SWT.SINGLE) != 0) {
		byte [] buffer = Converter.wcsToMbcs (message, true);
		if (GTK.GTK4) {
			GTK4.gtk_text_set_placeholder_text (textHandle, buffer);
		} else {
			GTK.gtk_entry_set_placeholder_text (handle, buffer);
		}
		return;
	}
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
@Override
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
public void setSelection(int start) {
	checkWidget();

	start = translateOffset(start);
	if ((style & SWT.SINGLE) != 0) {
		long str;
		if (GTK.GTK4) {
			str = GTK.gtk_entry_buffer_get_text(GTK4.gtk_entry_get_buffer(handle));
		} else {
			str = GTK3.gtk_entry_get_text(handle);
		}
		start = (int)OS.g_utf16_offset_to_utf8_offset(str, start);
		GTK.gtk_editable_set_position(handle, start);
	} else {
		byte [] startIter =  new byte [ITER_SIZEOF];
		byte [] endIter =  new byte [ITER_SIZEOF];
		GTK.gtk_text_buffer_get_bounds (bufferHandle, startIter, endIter);
		long ptr = GTK.gtk_text_buffer_get_text (bufferHandle, startIter, endIter, true);
		start = (int)OS.g_utf16_offset_to_utf8_offset (ptr, start);
		OS.g_free (ptr);
		GTK.gtk_text_buffer_get_iter_at_offset (bufferHandle, startIter, start);
		GTK.gtk_text_buffer_place_cursor (bufferHandle, startIter);
		scrollIfNotVisible(startIter, startIter, false);
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
	start = translateOffset (start);
	end = translateOffset (end);
	if ((style & SWT.SINGLE) != 0) {
		long ptr;
		if (GTK.GTK4) {
			ptr = GTK.gtk_entry_buffer_get_text(bufferHandle);
		} else {
			ptr = GTK3.gtk_entry_get_text (handle);
		}
		start = (int)OS.g_utf16_offset_to_utf8_offset (ptr, start);
		end = (int)OS.g_utf16_offset_to_utf8_offset (ptr, end);
		GTK.gtk_editable_set_position (handle, start);
		GTK.gtk_editable_select_region (handle, start, end);
	} else {
		byte [] startIter =  new byte [ITER_SIZEOF];
		byte [] endIter =  new byte [ITER_SIZEOF];
		GTK.gtk_text_buffer_get_bounds (bufferHandle, startIter, endIter);
		long ptr = GTK.gtk_text_buffer_get_text (bufferHandle, startIter, endIter, true);
		start = (int)OS.g_utf16_offset_to_utf8_offset (ptr, start);
		end = (int)OS.g_utf16_offset_to_utf8_offset (ptr, end);
		OS.g_free (ptr);
		GTK.gtk_text_buffer_get_iter_at_offset (bufferHandle, startIter, start);
		GTK.gtk_text_buffer_get_iter_at_offset (bufferHandle, endIter, end);
		scrollIfNotVisible(startIter, startIter, false);
		GTK.gtk_text_buffer_select_range(bufferHandle, startIter, endIter);
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
	int tabWidth = getTabWidth (tabs);
	long tabArray = OS.pango_tab_array_new (1, false);
	OS.pango_tab_array_set_tab (tabArray, 0, OS.PANGO_TAB_LEFT, tabWidth);
	if ((style & SWT.SINGLE) != 0) {
		if (GTK.GTK4) {
			GTK4.gtk_text_set_tabs (textHandle, tabArray);
		} else {
			GTK.gtk_entry_set_tabs (handle, tabArray);
		}
	} else {
		GTK.gtk_text_view_set_tabs (handle, tabArray);
	}
	OS.pango_tab_array_free (tabArray);
}

/**
 * Sets the contents of the receiver to the given string. If the receiver has style
 * SINGLE and the argument contains multiple lines of text, the result of this
 * operation is undefined and may vary from platform to platform.
 * <p>
 * Note: If control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
 * </p>
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
 * <p>
 * Note: Use this API to prevent the text from being written into a String
 * object whose lifecycle is outside of your control. This can help protect
 * the text, for example, when the widget is used as a password field.
 * However, the text can't be protected if an {@link SWT#Segments} or
 * {@link SWT#Verify} listener has been added to the widget.
 * </p>
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
	clearSegments (false);
	if ((style & SWT.SINGLE) != 0) {
		byte[] buffer = Converter.wcsToMbcs(text, true);
		OS.g_signal_handlers_block_matched(handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_block_matched(handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
		OS.g_signal_handlers_block_matched(handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
		if (GTK.GTK4) {
			GTK.gtk_entry_buffer_set_text(bufferHandle, buffer, -1);
		} else {
			GTK3.gtk_entry_set_text(handle, buffer);
		}
		OS.g_signal_handlers_unblock_matched(handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_unblock_matched(handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
		OS.g_signal_handlers_unblock_matched(handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
		Arrays.fill(buffer, (byte) 0);
	} else {
		byte [] buffer = Converter.wcsToMbcs (text, false);
		byte [] position =  new byte [ITER_SIZEOF];
		OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_RANGE);
		OS.g_signal_handlers_block_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEXT_BUFFER_INSERT_TEXT);
		GTK.gtk_text_buffer_set_text (bufferHandle, buffer, buffer.length);
		OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_RANGE);
		OS.g_signal_handlers_unblock_matched (bufferHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEXT_BUFFER_INSERT_TEXT);
		GTK.gtk_text_buffer_get_iter_at_offset (bufferHandle, position, 0);
		GTK.gtk_text_buffer_place_cursor (bufferHandle, position);
		long mark = GTK.gtk_text_buffer_get_insert (bufferHandle);
		GTK.gtk_text_view_scroll_to_mark (handle, mark, 0, true, 0, 0);
		Arrays.fill (buffer, (byte) 0);
	}
	sendEvent (SWT.Modify);
	if ((style & SWT.SEARCH) != 0 && !GTK.GTK4) {
		if ((style & SWT.ICON_CANCEL) == 0) {
			GTK.gtk_entry_set_icon_from_icon_name(handle, GTK.GTK_ENTRY_ICON_SECONDARY, null);
		}
	}
	applySegments ();
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
		GTK.gtk_entry_set_max_length (handle, segments != null ? Math.min (LIMIT, translateOffset (limit)) : limit);
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
	GTK.gtk_text_buffer_get_iter_at_line (bufferHandle, position, index);
	/*
	 * Feature in GTK: create a new GtkTextMark for the purposes of keeping track of the top index. In getTopIndex() we
	 * can use this without worrying about line validation. See bug 487467.
	 *
	 * We also cache the current GtkAdjustment value for future comparison in getTopIndex().
	 */
	byte [] buffer = Converter.wcsToMbcs ("index_mark", true);
	indexMark = GTK.gtk_text_buffer_create_mark (bufferHandle, buffer, position, true);
	GTK.gtk_text_view_scroll_to_mark (handle, indexMark, 0, true, 0, 0);
	long vAdjustment = GTK.gtk_scrollable_get_vadjustment (handle);
	cachedAdjustment = GTK.gtk_adjustment_get_value (vAdjustment);
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
	long mark = GTK.gtk_text_buffer_get_selection_bound (bufferHandle);
	GTK.gtk_text_view_scroll_to_mark (handle, mark, 0, true, 0, 0);
	mark = GTK.gtk_text_buffer_get_insert (bufferHandle);
	GTK.gtk_text_view_scroll_to_mark (handle, mark, 0, true, 0, 0);
}

int translateOffset (int offset) {
	if (segments == null) return offset;
	for (int i = 0, nSegments = segments.length; i < nSegments && offset - i >= segments[i]; i++) {
		offset++;
	}
	return offset;
}

@Override
boolean translateTraversal (long event) {
	int [] key = new int[1];
	if (GTK.GTK4) {
		key[0] = GDK.gdk_key_event_get_keyval(event);
	} else {
		GDK.gdk_event_get_keyval(event, key);
	}

	switch (key[0]) {
		case GDK.GDK_KP_Enter:
		case GDK.GDK_Return: {
			if (imContext != 0) {
				long [] preeditString = new long [1];
				GTK.gtk_im_context_get_preedit_string (imContext, preeditString, null, null);
				if (preeditString [0] != 0) {
					int length = C.strlen (preeditString [0]);
					OS.g_free (preeditString [0]);
					if (length != 0) return false;
				}
			}
		}
	}
	return super.translateTraversal (event);
}

@Override
int traversalCode (int key, long event) {
	int bits = super.traversalCode (key, event);
	if ((style & SWT.READ_ONLY) != 0)  return bits;
	if ((style & SWT.MULTI) != 0) {
		bits &= ~SWT.TRAVERSE_RETURN;
		if (key == GDK.GDK_Tab && event != 0) {
			int [] eventState = new int [1];
			if (GTK.GTK4) {
				eventState[0] = GDK.gdk_event_get_modifier_state(event);
			} else {
				GDK.gdk_event_get_state(event, eventState);
			}

			boolean next = (eventState[0] & GDK.GDK_SHIFT_MASK) == 0;
			if (next && (eventState[0] & GDK.GDK_CONTROL_MASK) == 0) {
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
	long eventPtr = GTK.GTK4 ? 0 : GTK3.gtk_get_current_event();
	if (eventPtr != 0) {
		int type = GDK.gdk_event_get_event_type(eventPtr);
		type = fixGdkEventTypeValues(type);
		switch (type) {
			case GDK.GDK_KEY_PRESS:
				setKeyState (event, eventPtr);
				break;
		}
		gdk_event_free (eventPtr);
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

@Override
long windowProc (long handle, long user_data) {
	if (hooks (SWT.Segments) || filters (SWT.Segments) || segments != null) {
		switch ((int)user_data) {
			case BACKSPACE:
			case COPY_CLIPBOARD:
			case CUT_CLIPBOARD:
			case PASTE_CLIPBOARD: {
				clearSegments (true);
				break;
			}
			case BACKSPACE_INVERSE:
			case COPY_CLIPBOARD_INVERSE:
			case CUT_CLIPBOARD_INVERSE:
			case PASTE_CLIPBOARD_INVERSE: {
				applySegments ();
				break;
			}
		}
	}
	return super.windowProc (handle, user_data);
}

@Override
long windowProc (long handle, long arg0, long user_data) {
	if (hooks (SWT.Segments) || filters (SWT.Segments) || segments != null) {
		switch ((int)user_data) {
			case DIRECTION_CHANGED: {
				clearSegments (true);
				applySegments ();
				break;
			}
		}
	}
	return super.windowProc (handle, arg0, user_data);
}

@Override
long windowProc (long handle, long arg0, long arg1, long user_data) {
	if (hooks (SWT.Segments) || filters (SWT.Segments) || segments != null) {
		switch ((int)user_data) {
			case DELETE_FROM_CURSOR: {
				clearSegments (true);
				break;
			}
			case DELETE_FROM_CURSOR_INVERSE: {
				applySegments ();
				break;
			}
		}
	}
	return super.windowProc (handle, arg0, arg1, user_data);
}

@Override
long windowProc (long handle, long arg0, long arg1, long arg2, long user_data) {
	if (hooks (SWT.Segments) || filters (SWT.Segments) || segments != null) {
		switch ((int)user_data) {
			case MOVE_CURSOR: {
				if (arg0 == GTK.GTK_MOVEMENT_VISUAL_POSITIONS) {
					clearSegments (true);
				}
				break;
			}
			case MOVE_CURSOR_INVERSE: {
				if (arg0 == GTK.GTK_MOVEMENT_VISUAL_POSITIONS) {
					applySegments ();
				}
				break;
			}
		}
	}
	return super.windowProc (handle, arg0, arg1, arg2, user_data);
}

}
