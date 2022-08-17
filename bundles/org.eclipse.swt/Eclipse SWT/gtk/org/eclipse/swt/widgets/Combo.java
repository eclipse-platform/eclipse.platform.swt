/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 483540
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

/**
 * Instances of this class are controls that allow the user
 * to choose an item from a list of items, or optionally
 * enter a new value by typing it into an editable text
 * field. Often, <code>Combo</code>s are used in the same place
 * where a single selection <code>List</code> widget could
 * be used but space is limited. A <code>Combo</code> takes
 * less space than a <code>List</code> widget and shows
 * similar information.
 * <p>
 * Note: Since <code>Combo</code>s can contain both a list
 * and an editable text field, it is possible to confuse methods
 * which access one versus the other (compare for example,
 * <code>clearSelection()</code> and <code>deselectAll()</code>).
 * The API documentation is careful to indicate either "the
 * receiver's list" or the "the receiver's text field" to
 * distinguish between the two cases.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>DROP_DOWN, READ_ONLY, SIMPLE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Selection, Verify, OrientationChange</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles DROP_DOWN and SIMPLE may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see List
 * @see <a href="http://www.eclipse.org/swt/snippets/#combo">Combo snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Combo extends Composite {
	long buttonHandle, entryHandle, textRenderer, cellHandle, popupHandle, menuHandle,
		buttonBoxHandle, cellBoxHandle, arrowHandle;
	int lastEventTime, visibleCount = 10;
	long imContext;
	long gdkEventKey = 0;
	int fixStart = -1, fixEnd = -1;
	String [] items = new String [0];
	int indexSelected;
	GdkRGBA background, foreground;
	long cssProvider;
	boolean firstDraw = true;
	boolean unselected = true, fitModelToggled = false;
	/**
	 * the operating system limit for the number of characters
	 * that the text field in an instance of this class can hold
	 */
	public final static int LIMIT;

	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0xFFFF;
	}

	private boolean delayedEnableWrap = false; // Bug 489640. Gtk3.

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
 * @see SWT#DROP_DOWN
 * @see SWT#READ_ONLY
 * @see SWT#SIMPLE
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Combo (Composite parent, int style) {
	super (parent, checkStyle (style));
}

/**
 * Adds the argument to the end of the receiver's list.
 * <p>
 * Note: If control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
 * </p>
 * @param string the new item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #add(String,int)
 */
public void add (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	add (string, items.length);
}

/**
 * Adds the argument to the receiver's list at the given
 * zero-relative index.
 * <p>
 * Note: To add an item at the end of the list, use the
 * result of calling <code>getItemCount()</code> as the
 * index or use <code>add(String)</code>.
 * </p><p>
 * Also note, if control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
 * </p>
 *
 * @param string the new item
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #add(String)
 */
public void add(String string, int index) {
	checkWidget();
	if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (!(0 <= index && index <= items.length)) {
		error(SWT.ERROR_INVALID_RANGE);
	}

	String [] newItems = new String [items.length + 1];
	System.arraycopy (items, 0, newItems, 0, index);
	newItems [index] = string;
	System.arraycopy (items, index, newItems, index + 1, items.length - index);
	items = newItems;

	gtk_combo_box_insert(string, index);
	if ((style & SWT.RIGHT_TO_LEFT) != 0 && popupHandle != 0) {
		GTK3.gtk_container_forall (popupHandle, display.setDirectionProc, GTK.GTK_TEXT_DIR_RTL);
	}
}

private void gtk_combo_box_insert(String string, int index) {
	byte[] buffer = Converter.wcsToMbcs(string, true);
	if (handle != 0) {
		gtk_combo_box_toggle_wrap(false);
		GTK.gtk_combo_box_text_insert (handle, index, null, buffer);
		gtk_combo_box_toggle_wrap(true);
	}
}

/**
 * <p>Bug 489640, 438992. Drop-down appearance.</p>
 *
 * <p>In Gtk3, there is a lot of white space at the top of the combo drop-down.
 *   It's meant to put the first item near the cursor (See screen shots in bug 438992), but makes eclipse look buggy.</p>
 *
 * <p>Solution (438992): gtk_combo_box_get_wrap_width(1) fixes this problem, but introduces a performance regression.
 *    When multiple items are added in a loop, then if 'wrap' is enabled then after every insertion, gtk
 *    traverses the entire list and greedily re-computes the drop-down list size.
 *    This causes O(n^2) performance regression in that the longer the list, the longer the size computation takes.
 *    E.g Adding 1000 items takes almost 30 seconds.</p>
 *
 * <p>Solution ^2 (489640): We enabled wrap in a delayed/lazy way, only when display event loop is ran.
 *   This way the insertions occur at O(n) speed and we get the benefit of a neat drop-down list when the user launches the application.
 *   Run-time insertions and combo.add(..) are also covered by this logic.</p>
 *
 * <p>Gtk4 port note:
 *  - Note, Combo was re-written in Gtk3 and it will be re-written again in Gtk4. See:
 *  https://mail.gnome.org/archives/gtk-list/2016-December/msg00036.html
 *  https://raw.githubusercontent.com/gnome-design-team/gnome-mockups/master/theming/widgets/combobox-replacements.png     // mockup screen shot.
 *  - So probably this workaround should be removed in Gtk4 port. Feel free to validate via Bug489640_SlowCombo and Bug489640_SlowComboSingleItem test snippets.</p>
 *
 * <p>CSS note: Do not use the CSS 'appears-as-list' style.
 *   It's a poorly working hack. If list has more than +-1000 entries, then we get visual cheese and jvm crashes. </p>
 */
private void gtk_combo_box_toggle_wrap (boolean wrap) {
	if (handle == 0 || GTK.GTK4) return;
	if (!wrap) {
		if (GTK3.gtk_combo_box_get_wrap_width(handle) == 1) {
			GTK3.gtk_combo_box_set_wrap_width(handle, 0);
		}
	} else  {
		if (delayedEnableWrap) {
			return;
		} else {
			delayedEnableWrap = true;
			display.asyncExec(() -> {
				if (!isDisposed() && handle != 0) {
					GTK3.gtk_combo_box_set_wrap_width(handle, 1);
					delayedEnableWrap = false;
				}
			});
		}
	}
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
	checkWidget();
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
 * <b>Warning</b>: This API is currently only implemented on Windows.
 * <code>SegmentEvent</code>s won't be sent on GTK and Cocoa.
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
 * @since 3.103
 */
public void addSegmentListener (SegmentListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	addListener (SWT.Segments, new TypedListener (listener));
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the user changes the receiver's selection, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the user changes the combo's list selection.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed the combo's text area.
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
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
 *
 * @since 3.1
 */
public void addVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Verify, typedListener);
}

static int checkStyle (int style) {
	/*
	* Feature in Windows.  It is not possible to create
	* a combo box that has a border using Windows style
	* bits.  All combo boxes draw their own border and
	* do not use the standard Windows border styles.
	* Therefore, no matter what style bits are specified,
	* clear the BORDER bits so that the SWT style will
	* match the Windows widget.
	*
	* The Windows behavior is currently implemented on
	* all platforms.
	*/
	style &= ~SWT.BORDER;

	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
	style = checkBits (style, SWT.DROP_DOWN, SWT.SIMPLE, 0, 0, 0, 0);
	if ((style & SWT.SIMPLE) != 0) return style & ~SWT.READ_ONLY;
	return style;
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

/**
 * Sets the selection in the receiver's text field to an empty
 * selection starting just before the first character. If the
 * text field is editable, this has the effect of placing the
 * i-beam at the start of the text.
 * <p>
 * Note: To clear the selected items in the receiver's list,
 * use <code>deselectAll()</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #deselectAll
 */
public void clearSelection () {
	checkWidget();
	if (entryHandle != 0) {
		int position = GTK.gtk_editable_get_position (entryHandle);
		GTK.gtk_editable_select_region (entryHandle, position, position);
	}
}

void clearText () {
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if ((style & SWT.READ_ONLY) != 0) {
		int index = GTK.gtk_combo_box_get_active (handle);
		if (index != -1) {
			long modelHandle = GTK.gtk_combo_box_get_model (handle);
			long [] ptr = new long [1];
			long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
			GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
			GTK.gtk_tree_model_get (modelHandle, iter, 0, ptr, -1);
			OS.g_free (iter);
			if (ptr [0] != 0 && C.strlen (ptr [0]) > 0) postEvent (SWT.Modify);
			OS.g_free (ptr [0]);
		}
	} else {
		if (GTK.GTK4) {
			long bufferHandle = GTK4.gtk_entry_get_buffer(entryHandle);
			GTK.gtk_entry_buffer_delete_text(bufferHandle, 0, -1);
		} else {
			GTK3.gtk_entry_set_text(entryHandle, new byte[1]);
		}
	}
	GTK.gtk_combo_box_set_active (handle, -1);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
}

@Override
Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();
	return computeNativeSize (handle, wHint, hHint, changed);
}

@Override
Point computeNativeSize (long h, int wHint, int hHint, boolean changed) {
	// Set fit-model property when computing size, if it was previously disabled
	if (fitModelToggled) {
		GTK.gtk_cell_view_set_fit_model(cellHandle, true);
	}
	int [] xpad = new int[1];
	if (textRenderer != 0) GTK.gtk_cell_renderer_get_padding(textRenderer, xpad, null);
	Point nativeSize = super.computeNativeSize(h, wHint, hHint, changed);
	nativeSize.x += xpad[0] * 2;

	/*
	 * Bug 545344 - [GTK3] Read only Combo text small cutoff on Linux, button.combo padding not taken into account
	 *
	 * A read only combo will use a GtkComboBoxText widget, which can be influenced by a css node:
	 *
	 * combobox button.combo
	 *
	 * This node can specify additional padding; if this padding is not taken into account,
	 * Not all of the combo items text will be displayed. Some letters will be cut off, depending on how high the padding is set.
	 */
	if ((style & SWT.READ_ONLY) != 0 && !GTK.GTK4) {
		GtkBorder buttonPadding = new GtkBorder();
		long context = GTK.gtk_widget_get_style_context (buttonHandle);
		int stateFlag = GTK.gtk_widget_get_state_flags(buttonHandle);
		gtk_style_context_get_padding(context, stateFlag, buttonPadding);
		nativeSize.x += buttonPadding.left + buttonPadding.right;
	}

	// Re-set fit-model to false as it was before
	if (fitModelToggled) {
		GTK.gtk_cell_view_set_fit_model(cellHandle, false);
	}
	return nativeSize;
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
 *
 * @since 2.1
 */
public void copy () {
	checkWidget ();
	if (entryHandle != 0) {
		if (GTK.GTK4) {
			long textHandle = GTK4.gtk_widget_get_first_child(entryHandle);
			GTK4.gtk_widget_activate_action(textHandle, OS.action_copy_clipboard, null);
		} else {
			GTK3.gtk_editable_copy_clipboard(entryHandle);
		}
	}
}

@Override
void createHandle (int index) {
	state |= HANDLE | MENU;

	fixedHandle = OS.g_object_new(display.gtk_fixed_get_type(), 0);
	if (fixedHandle == 0) error(SWT.ERROR_NO_HANDLES);
	if (!GTK.GTK4) GTK3.gtk_widget_set_has_window(fixedHandle, true);

	long oldList = GTK.gtk_window_list_toplevels();
	if ((style & SWT.READ_ONLY) != 0) {
		handle = GTK.gtk_combo_box_text_new();
		if (handle == 0) error(SWT.ERROR_NO_HANDLES);

		cellHandle = GTK.GTK4 ? GTK4.gtk_combo_box_get_child(handle) : GTK3.gtk_bin_get_child (handle);
		if (cellHandle == 0) error(SWT.ERROR_NO_HANDLES);

		gtk_combo_box_toggle_wrap(true);
	} else {
		handle = GTK.gtk_combo_box_text_new_with_entry();
		if (handle == 0) error(SWT.ERROR_NO_HANDLES);

		entryHandle = GTK.GTK4 ? GTK4.gtk_combo_box_get_child(handle) : GTK3.gtk_bin_get_child(handle);
		if (entryHandle == 0) error(SWT.ERROR_NO_HANDLES);
		if (DISABLE_EMOJI && GTK.GTK_VERSION >= OS.VERSION(3, 22, 20)) {
		    GTK.gtk_entry_set_input_hints(entryHandle, GTK.GTK_INPUT_HINT_NO_EMOJI);
		}

		imContext = OS.imContextLast();
	}

	//TODO: popupHandle is currently not mapped in GTK4, need to see if that is an issue.
	if (GTK.GTK4) {
		OS.swt_fixed_add(fixedHandle, handle);
	} else {
		popupHandle = findPopupHandle(oldList);
		GTK3.gtk_container_add (fixedHandle, handle);
	}

	textRenderer = GTK.gtk_cell_renderer_text_new();
	if (textRenderer == 0) error(SWT.ERROR_NO_HANDLES);

	GTK.gtk_cell_layout_clear (handle);
	GTK.gtk_cell_layout_pack_start (handle, textRenderer, true);
	GTK.gtk_cell_layout_set_attributes (handle, textRenderer, OS.text, 0, 0);

	/*
	* Feature in GTK. Toggle button creation differs between GTK versions. The
	* fix is to call size_request() to force the creation of the button
	* for those versions of GTK that defer the creation.
	*/
	menuHandle = findMenuHandle();
	if (menuHandle != 0) OS.g_object_ref (menuHandle);
	buttonHandle = findButtonHandle ();
	if (buttonHandle != 0) OS.g_object_ref (buttonHandle);
	if (buttonBoxHandle != 0) OS.g_object_ref (buttonBoxHandle);
	if (cellHandle != 0) cellBoxHandle = GTK.gtk_widget_get_parent(cellHandle);
	if (cellBoxHandle != 0) OS.g_object_ref(cellBoxHandle);
	/*
	* Feature in GTK. By default, read only combo boxes
	* process the RETURN key rather than allowing the
	* default button to process the key. The fix is to
	* clear the GTK_RECEIVES_DEFAULT flag.
	*/
	if ((style & SWT.READ_ONLY) != 0 && buttonHandle != 0) {
		GTK.gtk_widget_set_receives_default (buttonHandle, false);
	}
	/*
	 * Find the arrowHandle, which is the handle belonging to the GtkIcon
	 * drop down arrow. See bug 539367.
	 */
	if ((style & SWT.READ_ONLY) != 0) {
		if (cellBoxHandle != 0) arrowHandle = findArrowHandle();
	}
	// In GTK 3 font description is inherited from parent widget which is not how SWT has always worked,
	// reset to default font to get the usual behavior
	setFontDescription(defaultFont().handle);
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
 *
 * @since 2.1
 */
public void cut () {
	checkWidget ();
	if (entryHandle != 0) {
		if (GTK.GTK4) {
			long textHandle = GTK4.gtk_widget_get_first_child(entryHandle);
			GTK4.gtk_widget_activate_action(textHandle, OS.action_cut_clipboard, null);
		} else {
			GTK3.gtk_editable_cut_clipboard(entryHandle);
		}
	}
}

@Override
GdkRGBA defaultBackground () {
	return display.getSystemColor(SWT.COLOR_LIST_BACKGROUND).handle;
}

@Override
void deregister () {
	super.deregister ();
	if (buttonHandle != 0) display.removeWidget (buttonHandle);
	if (entryHandle != 0) display.removeWidget (entryHandle);
	if (popupHandle != 0) display.removeWidget (popupHandle);
	if (menuHandle != 0) display.removeWidget (menuHandle);
	long imContext = imContext ();
	if (imContext != 0) display.removeWidget (imContext);
}

@Override
boolean filterKey (long event) {
	int time = GDK.gdk_event_get_time (event);
	if (time != lastEventTime) {
		lastEventTime = time;
		long imContext = imContext ();
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

long findPopupHandle (long oldList) {
	long result = 0;
	long currentList = GTK.gtk_window_list_toplevels();
	long oldFromList = oldList;
	long newFromList = OS.g_list_last(currentList);
	boolean isFound;
	while (newFromList != 0) {
		long newToplevel = OS.g_list_data(newFromList);
		isFound = false;
		oldFromList = oldList;
		while (oldFromList != 0) {
			long oldToplevel = OS.g_list_data(oldFromList);
			if (newToplevel == oldToplevel) {
				isFound = true;
				break;
			}
			oldFromList = OS.g_list_next(oldFromList);
		}
		if (!isFound) {
			result = newToplevel;
			break;
		}
		newFromList = OS.g_list_previous(newFromList);
	}
	OS.g_list_free(oldList);
	OS.g_list_free(currentList);
	return result;
}

@Override
Point resizeCalculationsGTK3 (long widget, int width, int height) {
	/*
	 * Combo is a complex widget which has many widgets inside of it.
	 * Some of these widgets have large natural and/or minimum sizes. To prevent
	 * API breakage, just set the width and height from setBounds().
	 * See bug 537713.
	 */
	return new Point (width, height);
}

long findButtonHandle() {
	/*
	* Feature in GTK.  There is no API to query the button
	* handle from a combo box although it is possible to get the
	* text field.  The button handle is needed to hook events.  The
	* fix is to walk the combo tree and find the first child that is
	* an instance of button.
	*/
	long result = 0;

	if (GTK.GTK4) {
		for (long child = GTK4.gtk_widget_get_first_child(handle); child != 0; child = GTK4.gtk_widget_get_next_sibling(child)) {
			if (GTK.GTK_IS_BOX(child)) {
				buttonBoxHandle = child;
				break;
			}
		}

		for (long child = GTK4.gtk_widget_get_first_child(buttonBoxHandle); child != 0; child = GTK4.gtk_widget_get_next_sibling(child)) {
			if (GTK.GTK_IS_BUTTON(child)) {
				result = child;
				break;
			}
		}
	} else {
		long childHandle = handle;
		/*
		 * The only direct child of GtkComboBox since 3.20 is GtkBox and
		 * gtk_container_forall iterates over direct children only so handle for the
		 * GtkBox has to be retrieved first.
		 * As it's internal child one can't get it in other way.
		 */
		GTK3.gtk_container_forall(handle, display.allChildrenProc, 0);
		if (display.allChildren != 0) {
			long list = display.allChildren;
			while (list != 0) {
				long widget = OS.g_list_data(list);
				if (widget != 0) {
					childHandle = widget;
					break;
				}
				list = OS.g_list_next(list);
			}
			OS.g_list_free(display.allChildren);
			display.allChildren = 0;
		}
		buttonBoxHandle = childHandle;

		GTK3.gtk_container_forall (childHandle, display.allChildrenProc, 0);
		if (display.allChildren != 0) {
			long list = display.allChildren;
			while (list != 0) {
				long widget = OS.g_list_data (list);
				if (GTK.GTK_IS_BUTTON (widget)) {
					result = widget;
					break;
				}
				list = OS.g_list_next (list);
			}
			OS.g_list_free (display.allChildren);
			display.allChildren = 0;
		}
	}

	return result;
}

long findArrowHandle() {
	long result = 0;

	if (cellBoxHandle != 0) {
		if (GTK.GTK4) {
			for (long child = GTK4.gtk_widget_get_first_child(cellBoxHandle); child != 0; child = GTK4.gtk_widget_get_next_sibling(child)) {
				String name = display.gtk_widget_get_name(child);
				if (name != null && name.equals("GtkBuiltinIcon")) {
					result = child;
					break;
				}
			}
		} else {
			GTK3.gtk_container_forall (cellBoxHandle, display.allChildrenProc, 0);
			if (display.allChildren != 0) {
				long list = display.allChildren;
				while (list != 0) {
					long widget = OS.g_list_data (list);
					/*
					 * Feature in GTK: GtkIcon isn't public, so we have to do
					 * type lookups using gtk_widget_get_name(). See bug 539367.
					 */
					String name = display.gtk_widget_get_name(widget);
					if (name != null && name.contains("GtkIcon")) {
						result = widget;
					}
					list = OS.g_list_next (list);
				}
				OS.g_list_free (display.allChildren);
				display.allChildren = 0;
			}
		}
	}

	return result;
}

long findMenuHandle() {
	/*
	* Feature in GTK.  There is no API to query the menu
	* handle from a combo box. So we walk the popupHandle to
	* find the handle for the menu.
	*/
	long result = 0;

	if(GTK.GTK4) {
		for (long child = GTK4.gtk_widget_get_first_child(handle); child != 0; child = GTK4.gtk_widget_get_next_sibling(child)) {
			String name = display.gtk_widget_get_name(child);
			if (name != null && name.equals("GtkTreePopover")) {
				result = GTK4.gtk_widget_get_first_child(child);
				break;
			}
		}
	} else if(popupHandle != 0) {
		GTK3.gtk_container_forall(popupHandle, display.allChildrenProc, 0);
		if (display.allChildren != 0) {
			long list = display.allChildren;
			while (list != 0) {
				long widget = OS.g_list_data(list);
				String name = display.gtk_widget_get_name(widget);
				if (name != null && name.contains("gtk-combobox-popup-menu")) {
					result = widget;
					break;
				}
				list = OS.g_list_next(list);
			}
			OS.g_list_free(display.allChildren);
			display.allChildren = 0;
		}
	}

	return result;
}

@Override
void fixModal (long group, long modalGroup) {
	if (popupHandle != 0) {
		if (group != 0) {
			GTK.gtk_window_group_add_window (group, popupHandle);
		} else {
			if (modalGroup != 0) {
				GTK.gtk_window_group_remove_window (modalGroup, popupHandle);
			}
		}
	}
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
		long imContext = imContext ();
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
long fontHandle () {
	if (entryHandle != 0) return entryHandle;
	return super.fontHandle ();
}

@Override
long focusHandle () {
	if (entryHandle != 0) return entryHandle;
	return super.focusHandle ();
}

@Override
boolean hasFocus () {
	if (super.hasFocus ()) return true;
	if (entryHandle != 0 && GTK.gtk_widget_has_focus (entryHandle)) return true;
	return false;
}

@Override
void hookEvents () {
	super.hookEvents ();
	OS.g_signal_connect_closure (handle, OS.changed, display.getClosure (CHANGED), true);

	if (entryHandle != 0) {
		OS.g_signal_connect_closure (entryHandle, OS.changed, display.getClosure (CHANGED), true);
		OS.g_signal_connect_closure (entryHandle, OS.insert_text, display.getClosure (INSERT_TEXT), false);
		OS.g_signal_connect_closure (entryHandle, OS.delete_text, display.getClosure (DELETE_TEXT), false);
		OS.g_signal_connect_closure (entryHandle, OS.activate, display.getClosure (ACTIVATE), false);
		if (!GTK.GTK4) OS.g_signal_connect_closure (entryHandle, OS.populate_popup, display.getClosure (POPULATE_POPUP), false);
	}

	hookEvents(new long [] {buttonHandle, entryHandle, menuHandle});

	long imContext = imContext ();
	if (imContext != 0) {
		OS.g_signal_connect_closure (imContext, OS.commit, display.getClosure (COMMIT), false);
		int id = OS.g_signal_lookup (OS.commit, GTK.gtk_im_context_get_type ());
		int blockMask =  OS.G_SIGNAL_MATCH_DATA | OS.G_SIGNAL_MATCH_ID;
		OS.g_signal_handlers_block_matched (imContext, blockMask, id, 0, 0, 0, entryHandle);
	}
}

void hookEvents(long [] handles) {
	for (int i = 0; i < handles.length; i++) {
		long eventHandle = handles[i];
		if (eventHandle != 0) {
			if (GTK.GTK4) {
				long motionController = GTK4.gtk_event_controller_motion_new();
				GTK4.gtk_widget_add_controller(eventHandle, motionController);
				OS.g_signal_connect (motionController, OS.motion, display.enterMotionProc, MOTION);
				OS.g_signal_connect (motionController, OS.motion, display.enterMotionProc, MOTION_INVERSE);

				long gestureController = GTK4.gtk_gesture_click_new();
				GTK4.gtk_widget_add_controller(eventHandle, gestureController);
				OS.g_signal_connect(gestureController, OS.pressed, display.gesturePressReleaseProc, GESTURE_PRESSED);
				OS.g_signal_connect(gestureController, OS.released, display.gesturePressReleaseProc, GESTURE_RELEASED);

				//TODO: GTK4 event-after
			} else {
				int eventMask =	GDK.GDK_POINTER_MOTION_MASK | GDK.GDK_BUTTON_PRESS_MASK | GDK.GDK_BUTTON_RELEASE_MASK;
				GTK3.gtk_widget_add_events (eventHandle, eventMask);
				OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [MOTION_NOTIFY_EVENT], 0, display.getClosure (MOTION_NOTIFY_EVENT), false);
				OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [MOTION_NOTIFY_EVENT], 0, display.getClosure (MOTION_NOTIFY_EVENT_INVERSE), true);

				OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [BUTTON_PRESS_EVENT], 0, display.getClosure (BUTTON_PRESS_EVENT), false);
				OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [BUTTON_RELEASE_EVENT], 0, display.getClosure (BUTTON_RELEASE_EVENT), false);

				/*
				* Feature in GTK3.  Events such as mouse move are propagated up
				* the widget hierarchy and are seen by the parent.  This is the
				* correct GTK behavior but not correct for SWT.  The fix is to
				* hook a signal after and stop the propagation using a negative
				* event number to distinguish this case.
				*/
				OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [BUTTON_PRESS_EVENT], 0, display.getClosure (BUTTON_PRESS_EVENT_INVERSE), true);
				OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [BUTTON_RELEASE_EVENT], 0, display.getClosure (BUTTON_RELEASE_EVENT_INVERSE), true);

				/* Connect the event_after signal for both key and mouse */
				if (eventHandle != focusHandle ()) {
					OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [EVENT_AFTER], 0, display.getClosure (EVENT_AFTER), false);
				}

				if (OS.G_OBJECT_TYPE (eventHandle) == GTK3.GTK_TYPE_MENU ()) {
					OS.g_signal_connect_closure(eventHandle, OS.selection_done, display.getClosure (SELECTION_DONE), true);
				}
			}
		}
	}
}

long imContext () {
	if (imContext != 0) return imContext;
	return 0;
}

/**
 * Deselects the item at the given zero-relative index in the receiver's
 * list.  If the item at the index was already deselected, it remains
 * deselected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to deselect
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int index) {
	checkWidget();
	if (index < 0 || index >= items.length) return;

	if (GTK.gtk_combo_box_get_active (handle) == index) {
		clearText ();
	}
}

/**
 * Deselects all selected items in the receiver's list.
 * <p>
 * Note: To clear the selection in the receiver's text field,
 * use <code>clearSelection()</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #clearSelection
 */
public void deselectAll () {
	checkWidget();
	clearText();
}

@Override
boolean dragDetect(int x, int y, boolean filter, boolean dragOnTimeout, boolean[] consume) {
	if (filter && entryHandle != 0) {
		int [] index = new int [1];
		int [] trailing = new int [1];
		long layout = GTK3.gtk_entry_get_layout (entryHandle);
		OS.pango_layout_xy_to_index (layout, x * OS.PANGO_SCALE, y * OS.PANGO_SCALE, index, trailing);
		long ptr = OS.pango_layout_get_text (layout);
		int position = (int)OS.g_utf8_pointer_to_offset (ptr, ptr + index[0]) + trailing[0];
		int [] start = new int [1];
		int [] end = new int [1];
		GTK.gtk_editable_get_selection_bounds (entryHandle, start, end);
		if (start [0] <= position && position < end [0]) {
			if (super.dragDetect (x, y, filter, dragOnTimeout, consume)) {
				if (consume != null) consume [0] = true;
				return true;
			}
		}
		return false;
	}
	return super.dragDetect (x, y, filter, dragOnTimeout, consume);
}

@Override
long enterExitHandle () {
	return fixedHandle;
}

@Override
long eventWindow () {
	if ((style & SWT.READ_ONLY) != 0) {
		GTK.gtk_widget_realize (handle);
		return gtk_widget_get_window (handle);
	}

	/*
	 * Single-line Text (GtkEntry in GTK) uses a GDK_INPUT_ONLY
	 * internal window. This window can't be used for any kind
	 * of painting, but this is the window to which functions
	 * like Control.setCursor() should apply.
	 */
	GTK.gtk_widget_realize (entryHandle);
	long window = gtk_widget_get_window (entryHandle);
	// Find the internal GDK_INPUT_ONLY window
	long children = GDK.gdk_window_get_children (window);
	if (children != 0) {
		long childrenIterator = children;
		do {
			window = OS.g_list_data (childrenIterator);
		} while ((childrenIterator = OS.g_list_next (childrenIterator)) != 0);
	}
	OS.g_list_free (children);

	return window;
}

@Override
long eventSurface () {
	return paintSurface ();
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
 *
 * @since 3.8
 */
public Point getCaretLocation () {
	checkWidget ();
	return DPIUtil.autoScaleDown(getCaretLocationInPixels());
}


Point getCaretLocationInPixels () {
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) {
		return new Point (0, 0);
	}
	int index = GTK.gtk_editable_get_position (entryHandle);
	index = GTK3.gtk_entry_text_index_to_layout_index (entryHandle, index);
	int [] offset_x = new int [1], offset_y = new int [1];
	GTK3.gtk_entry_get_layout_offsets (entryHandle, offset_x, offset_y);
	long layout = GTK3.gtk_entry_get_layout (entryHandle);
	PangoRectangle pos = new PangoRectangle ();
	OS.pango_layout_index_to_pos (layout, index, pos);
	Point thickness = getThickness (entryHandle);
	int x = offset_x [0] + OS.PANGO_PIXELS (pos.x) - getBorderWidthInPixels () - thickness.x;
	int y = offset_y [0] + OS.PANGO_PIXELS (pos.y) - thickness.y;
	return new Point (x, y);
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
 *
 * @since 3.8
 */
public int getCaretPosition () {
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) {
		return 0;
	}
	long ptr = GTK3.gtk_entry_get_text (entryHandle);
	return (int)OS.g_utf8_offset_to_utf16_offset (ptr, GTK.gtk_editable_get_position (entryHandle));
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
	}
	return display.COLOR_WIDGET_FOREGROUND_RGBA;
}

/**
 * Returns the item at the given, zero-relative index in the
 * receiver's list. Throws an exception if the index is out
 * of range.
 *
 * @param index the index of the item to return
 * @return the item at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getItem (int index) {
	checkWidget();
	if (!(0 <= index && index < items.length)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	return items [index];
}

/**
 * Returns the number of items contained in the receiver's list.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget();
	return items.length;
}

/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the receiver's list.
 *
 * @return the height of one item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemHeight () {
	checkWidget();

	long fontDesc = getFontDescription ();
	int result = fontHeight (fontDesc, handle);
	OS.pango_font_description_free(fontDesc);

	return result;
}

/**
 * Returns a (possibly empty) array of <code>String</code>s which are
 * the items in the receiver's list.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver.
 * </p>
 *
 * @return the items in the receiver's list
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String[] getItems() {
	checkWidget();
	String [] result = new String [items.length];
	System.arraycopy (items, 0, result, 0, items.length);
	return result;
}

/**
 * Returns <code>true</code> if the receiver's list is visible,
 * and <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's list's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public boolean getListVisible () {
	checkWidget ();
	return popupHandle != 0 && GTK.gtk_widget_get_visible (popupHandle);
}

@Override
String getNameText () {
	return getText ();
}

/**
 * Returns the orientation of the receiver.
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

/**
 * Returns a <code>Point</code> whose x coordinate is the
 * character position representing the start of the selection
 * in the receiver's text field, and whose y coordinate is the
 * character position representing the end of the selection.
 * An "empty" selection is indicated by the x and y coordinates
 * having the same value.
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
	if ((style & SWT.READ_ONLY) != 0) {
		int length = 0;
		int index = GTK.gtk_combo_box_get_active (handle);
		if (index != -1) length = getItem (index).length ();
		return new Point (0, length);
	}
	int [] start = new int [1];
	int [] end = new int [1];
	if (entryHandle != 0) {
		GTK.gtk_editable_get_selection_bounds (entryHandle, start, end);
		long ptr = GTK3.gtk_entry_get_text (entryHandle);
		start[0] = (int)OS.g_utf8_offset_to_utf16_offset (ptr, start[0]);
		end[0] = (int)OS.g_utf8_offset_to_utf16_offset (ptr, end[0]);
	}
	return new Point(start [0], end [0]);
}

/**
 * Returns the zero-relative index of the item which is currently
 * selected in the receiver's list, or -1 if no item is selected.
 *
 * @return the index of the selected item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionIndex () {
	checkWidget();
	return GTK.gtk_combo_box_get_active (handle);
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
 */
public String getText () {
	checkWidget();

	if (entryHandle != 0) {
		long str = 0;
		if (GTK.GTK4) {
			long bufferHandle = GTK4.gtk_entry_get_buffer(entryHandle);
			str = GTK.gtk_entry_buffer_get_text(bufferHandle);
		} else {
			str = GTK3.gtk_entry_get_text(entryHandle);
		}

		if (str == 0) {
			return "";
		} else {
			int length = C.strlen (str);
			byte[] buffer = new byte[length];
			C.memmove(buffer, str, length);
			return new String (Converter.mbcsToWcs(buffer));
		}
	} else {
		int index = GTK.gtk_combo_box_get_active (handle);
		return index != -1 ? getItem (index) : "";
	}
}

String getText (int start, int stop) {
	/*
	* NOTE: The current implementation uses substring ()
	* which can reference a potentially large character
	* array.
	*/
	return getText ().substring (start, stop - 1);
}

/**
 * Returns the height of the receivers's text field.
 *
 * @return the text height
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTextHeight () {
	checkWidget();
	return DPIUtil.autoScaleDown(getTextHeightInPixels());
}


int getTextHeightInPixels () {
	checkWidget();
	GtkRequisition requisition = new GtkRequisition ();
	gtk_widget_get_preferred_size (handle, requisition);
	return requisition.height;
}

/**
 * Returns the maximum number of characters that the receiver's
 * text field is capable of holding. If this has not been changed
 * by <code>setTextLimit()</code>, it will be the constant
 * <code>Combo.LIMIT</code>.
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
	checkWidget();
	int limit = entryHandle != 0 ? GTK.gtk_entry_get_max_length (entryHandle) : 0;
	return limit == 0 ? LIMIT : limit;
}

/**
 * Gets the number of items that are visible in the drop
 * down portion of the receiver's list.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 *
 * @return the number of items that are visible
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public int getVisibleItemCount () {
	checkWidget ();
	return visibleCount;
}

@Override
long gtk_activate (long widget) {
	sendSelectionEvent (SWT.DefaultSelection);
	return 0;
}

@Override
long gtk_button_press_event (long widget, long event) {
	/*
	* Feature in GTK. Depending on where the user clicks, GTK prevents
	* the left mouse button event from being propagated. The fix is to
	* send the mouse event from the event_after handler.
	*/
	int [] eventButton = new int [1];
	if (GTK.GTK4) {
		eventButton[0] = GDK.gdk_button_event_get_button(event);
	} else {
		GDK.gdk_event_get_button(event, eventButton);
	}

	int eventType = GDK.gdk_event_get_event_type(event);
	if (eventType == GDK.GDK_BUTTON_PRESS && eventButton[0] == 1) {
		return gtk_button_press_event(widget, event, false);
	}
	return super.gtk_button_press_event (widget, event);
}

@Override
long gtk_changed (long widget) {
	if (widget == handle) {
		unselected = false;
		if (entryHandle == 0) {
			sendEvent(SWT.Modify);
			if (isDisposed ()) return 0;
		}
		/*
		* Feature in GTK.  GTK emits a changed signal whenever
		* the contents of a combo box are altered by typing or
		* by selecting an item in the list, but the event should
		* only be sent when the list is selected. The fix is to
		* only send out a selection event when there is a selected
		* item.
		*
		* NOTE: This code relies on GTK clearing the selected
		* item and not matching the item as the user types.
		*/
		int index = GTK.gtk_combo_box_get_active (handle);
		if (index != -1) sendSelectionEvent (SWT.Selection);
		indexSelected = -1;
		return 0;
	}
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
	return 0;
}

@Override
long gtk_commit (long imContext, long text) {
	if (text == 0) return 0;
	if (!GTK.gtk_editable_get_editable (entryHandle)) return 0;
	int length = C.strlen (text);
	if (length == 0) return 0;
	byte [] buffer = new byte [length];
	C.memmove (buffer, text, length);
	char [] chars = Converter.mbcsToWcs (buffer);
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
	OS.g_signal_handlers_unblock_matched (imContext, mask, id, 0, 0, 0, entryHandle);
	if (newChars == chars) {
		OS.g_signal_emit_by_name (imContext, OS.commit, text);
	} else {
		buffer = Converter.wcsToMbcs (newChars, true);
		OS.g_signal_emit_by_name (imContext, OS.commit, buffer);
	}
	OS.g_signal_handlers_unblock_matched (imContext, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, COMMIT);
	OS.g_signal_handlers_block_matched (imContext, mask, id, 0, 0, 0, entryHandle);
	if (fixStart != -1 && fixEnd != -1) {
		GTK.gtk_editable_set_position (entryHandle, fixStart);
		GTK.gtk_editable_select_region (entryHandle, fixStart, fixEnd);
	}
	fixStart = fixEnd = -1;
	return 0;
}

@Override
long gtk_delete_text (long widget, long start_pos, long end_pos) {
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	long ptr;
	if(GTK.GTK4) {
		long bufferPtr = GTK4.gtk_entry_get_buffer(entryHandle);
		ptr = GTK.gtk_entry_buffer_get_text(bufferPtr);
	}
	else {
		ptr = GTK3.gtk_entry_get_text(entryHandle);
	}
	if (end_pos == -1) end_pos = OS.g_utf8_strlen (ptr, -1);
	int start = (int)OS.g_utf8_offset_to_utf16_offset (ptr, start_pos);
	int end = (int)OS.g_utf8_offset_to_utf16_offset (ptr, end_pos);
	String newText = verifyText ("", start, end);
	if (newText == null) {
		OS.g_signal_stop_emission_by_name (entryHandle, OS.delete_text);
	} else {
		if (newText.length () > 0) {
			int [] pos = new int [1];
			pos [0] = (int)end_pos;
			byte [] buffer = Converter.wcsToMbcs (newText, false);
			OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			GTK.gtk_editable_insert_text (entryHandle, buffer, buffer.length, pos);
			OS.g_signal_handlers_unblock_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			OS.g_signal_handlers_unblock_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			GTK.gtk_editable_set_position (entryHandle, pos [0]);
		}
	}
	return 0;
}

@Override
void adjustChildClipping (long widget) {
	/*
	 * When adjusting the GtkCellView's clip, take into account
	 * the position of the "arrow" icon. We set the clip of the
	 * GtkCellView to the icon's position, minus the icon's width.
	 *
	 * This ensures the text never draws longer than the Combo itself.
	 * See bug 539367.
	 */
	if (widget == cellHandle && (style & SWT.READ_ONLY) != 0 && !unselected) {
		/*
		 * Currently in GTK4, when a new combo is created it does not follow setBounds.
		 * This could be the reason gtk_cell_view_set_fit_model does not work like in GTK3.
		 * This bug will need to be revisited once setBounds is working again.
		 * See bug 567215
		 */
		if(GTK.GTK4) {
			super.adjustChildClipping(widget);
			return;
		}

		/*
		 * Set "fit-model" mode for READ_ONLY Combos on GTK3.20+ to false.
		 * This means the GtkCellView rendering the text can be set to
		 * a size other than the maximum. See bug 539367.
		 */
		if (!fitModelToggled) {
			GTK.gtk_cell_view_set_fit_model(cellHandle, false);
			fitModelToggled = true;
		}
		GtkAllocation iconAllocation = new GtkAllocation ();
		GTK.gtk_widget_get_allocation(arrowHandle, iconAllocation);
		GtkAllocation cellViewAllocation = new GtkAllocation ();
		GTK.gtk_widget_get_allocation(cellHandle, cellViewAllocation);

		cellViewAllocation.width = (iconAllocation.x - iconAllocation.width);
		GTK3.gtk_widget_set_clip(widget, cellViewAllocation);
		return;
	} else {
		super.adjustChildClipping(widget);
	}
}

@Override
long gtk_draw (long widget, long cairo) {
	/*
	 * Feature in GTK3.20+: Combos have their clip unioned
	 * with the parent container (Composite in this case). This causes
	 * overdrawing into neighbouring widgets and breaks resizing.
	 *
	 * The fix is to adjust the clip and allocation of the Combo (and its children)
	 * in the parent Composite's draw handler, as it ensures that the draw events
	 * going to the Combo have the correct geometry specifications. This has to
	 * happen in the parent Composite since neighouring widgets that share the
	 * same parent are affected. See bug 500703.
	 *
	 * An additional fix was implemented to support non-READ_ONLY Combos, as well
	 * as the case when one Composite has multiple Combos within it -- see bug 535323.
	 */
	long parentHandle = GTK.gtk_widget_get_parent(fixedHandle);
	if (parentHandle != 0) {
		if (parent.fixClipHandle == 0) parent.fixClipHandle = parentHandle;
		if (firstDraw) {
			if ((style & SWT.READ_ONLY) != 0) {
				long [] array = {fixedHandle, handle, buttonBoxHandle, buttonHandle, cellBoxHandle, cellHandle};
				parent.fixClipMap.put(this, array);
			} else {
				long [] array = {fixedHandle, handle, entryHandle, buttonBoxHandle, buttonHandle};
				parent.fixClipMap.put(this, array);
			}
			firstDraw = false;
			GTK.gtk_widget_queue_draw(parentHandle);
		}
	}
	return super.gtk_draw(widget, cairo);
}

@Override
long gtk_event_after (long widget, long gdkEvent)  {
	/*
	* Feature in GTK. Depending on where the user clicks, GTK prevents
	* the left mouse button event from being propagated. The fix is to
	* send the mouse event from the event_after handler.
	*
	* Feature in GTK. When the user clicks anywhere in an editable
	* combo box, a single focus event should be issued, despite the
	* fact that focus might switch between the drop down button and
	* the text field. The fix is to use gtk_combo_box_set_focus_on_click ()
	* to eat all focus events while focus is in the combo box. When the
	* user clicks on the drop down button focus is assigned to the text
	* field.
	*/
	int eventType = GDK.gdk_event_get_event_type(gdkEvent);
	eventType = fixGdkEventTypeValues(eventType);
	switch (eventType) {
		case GDK.GDK_BUTTON_PRESS: {
			int [] eventButton = new int [1];
			int [] eventState = new int [1];
			if (GTK.GTK4) {
				eventButton[0] = GDK.gdk_button_event_get_button(gdkEvent);
				eventState[0] = GDK.gdk_event_get_modifier_state(gdkEvent);
			} else {
				GDK.gdk_event_get_button(gdkEvent, eventButton);
				GDK.gdk_event_get_state(gdkEvent, eventState);
			}

			int eventTime = GDK.gdk_event_get_time(gdkEvent);

			double [] eventRX = new double [1];
			double [] eventRY = new double [1];
			GDK.gdk_event_get_root_coords(gdkEvent, eventRX, eventRY);

			if (eventButton[0] == 1) {
				if (!sendMouseEvent (SWT.MouseDown, eventButton[0], display.clickCount, 0, false, eventTime, eventRX[0], eventRY[0], false, eventState[0])) {
					return 1;
				}
				if ((style & SWT.READ_ONLY) == 0 && widget == buttonHandle) {
					GTK.gtk_widget_grab_focus (entryHandle);
				}
			}
			break;
		}
		case GDK.GDK_FOCUS_CHANGE: {
			if ((style & SWT.READ_ONLY) == 0) {
				boolean [] focusIn = new boolean [1];
				if (GTK.GTK4) {
					focusIn[0] = GDK.gdk_focus_event_get_in(gdkEvent);
				} else {
					GdkEventFocus gdkEventFocus = new GdkEventFocus ();
					GTK3.memmove (gdkEventFocus, gdkEvent, GdkEventFocus.sizeof);
					focusIn[0] = gdkEventFocus.in != 0;
				}
				if (focusIn[0]) {
					GTK.gtk_widget_set_focus_on_click(handle, false);
				} else {
					GTK.gtk_widget_set_focus_on_click(handle, true);
				}
			}
			break;
		}
	}
	return super.gtk_event_after(widget, gdkEvent);
}

@Override
long gtk_focus_out_event (long widget, long event) {
	fixIM ();
	return super.gtk_focus_out_event (widget, event);
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
	long ptr;
	if(GTK.GTK4) {
		long bufferPtr = GTK4.gtk_entry_get_buffer(entryHandle);
		ptr = GTK.gtk_entry_buffer_get_text(bufferPtr);
	}
	else {
		ptr = GTK3.gtk_entry_get_text (entryHandle);
	}

	if (pos [0] == -1) pos [0] = (int)OS.g_utf8_strlen (ptr, -1);
	int start = (int)OS.g_utf8_offset_to_utf16_offset (ptr, pos [0]);
	String newText = verifyText (oldText, start, start);
	if (newText != oldText) {
		int [] newStart = new int [1], newEnd = new int [1];
		GTK.gtk_editable_get_selection_bounds (entryHandle, newStart, newEnd);
		if (newText != null) {
			if (newStart [0] != newEnd [0]) {
				OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
				OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				GTK.gtk_editable_delete_selection (entryHandle);
				OS.g_signal_handlers_unblock_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
				OS.g_signal_handlers_unblock_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			}
			byte [] buffer3 = Converter.wcsToMbcs (newText, false);
			OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			GTK.gtk_editable_insert_text (entryHandle, buffer3, buffer3.length, pos);
			OS.g_signal_handlers_unblock_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			newStart [0] = newEnd [0] = pos [0];
		}
		pos [0] = newEnd [0];
		if (newStart [0] != newEnd [0]) {
			fixStart = newStart [0];
			fixEnd = newEnd [0];
		}
		C.memmove (position, pos, 4);
		OS.g_signal_stop_emission_by_name (entryHandle, OS.insert_text);
	}
	return 0;
}

@Override
long gtk_key_press_event (long widget, long event) {
	long result = super.gtk_key_press_event (widget, event);
	if (result != 0) {
		gdkEventKey = 0;
		fixIM ();
		return result;
	}
	if (gdkEventKey == -1) result = 1;
	gdkEventKey = 0;
	if ((style & SWT.READ_ONLY) == 0) {
		int oldIndex = GTK.gtk_combo_box_get_active (handle);
		int newIndex = oldIndex;
		int [] eventKeyval = new int [1];
		if (GTK.GTK4) {
			eventKeyval[0] = GDK.gdk_key_event_get_keyval(event);
		} else {
			GDK.gdk_event_get_keyval(event, eventKeyval);
		}

		switch (eventKeyval[0]) {
			case GDK.GDK_Down:
			case GDK.GDK_KP_Down:
				if (oldIndex != (items.length - 1)) {
					newIndex = oldIndex + 1;
				}
				break;
			case GDK.GDK_Up:
			case GDK.GDK_KP_Up:
				if (oldIndex != -1 && oldIndex != 0) {
					newIndex = oldIndex - 1;
				}
				break;
			/*
			* Feature in GTK. In gtk_combo_box, the PageUp and PageDown keys
			* go the first and last items in the list rather than scrolling
			* a page at a time. The fix is to emulate this behavior for
			* gtk_combo_box_entry.
			*/
			case GDK.GDK_Page_Up:
			case GDK.GDK_KP_Page_Up:
				newIndex = 0;
				break;
			case GDK.GDK_Page_Down:
			case GDK.GDK_KP_Page_Down:
				newIndex = items.length - 1;
				break;
		}
		if (newIndex != oldIndex) {
			GTK.gtk_combo_box_set_active (handle, newIndex);
			return 1;
		}
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
long gtk_selection_done(long menushell) {
	int index = GTK.gtk_combo_box_get_active (handle);
	unselected = false;
	if (indexSelected == -1){
		indexSelected = index;
	}
	else if (index != -1 && indexSelected == index) {
		sendSelectionEvent (SWT.Selection);
	}
	return 0;
}

@Override
long gtk_style_updated (long widget) {
	/*
	 * Legacy code from GTK2, probably it can already be removed.
	 * It seems to deal with the case when Combo has 'appears-as-list' style,
	 * which causes it to re-create (once) child controls in GTK's
	 * 'gtk_combo_box_check_appearance()'. However, 'appears-as-list' style is
	 * very broken, causing various crashes etc. To my understanding, no GTK3
	 * themes currently use it (search for '-GtkComboBox-appears-as-list:').
	 * Also, it was completely removed in GTK4 in commit fdc0c642 (2016-11-11).
	 */
	setButtonHandle (findButtonHandle ());
	setMenuHandle (findMenuHandle ());
	return super.gtk_style_updated (widget);
}

/**
 * Searches the receiver's list starting at the first item
 * (index 0) until an item is found that is equal to the
 * argument, and returns the index of that item. If no item
 * is found, returns -1.
 *
 * @param string the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (String string) {
	checkWidget();
	return indexOf(string, 0);
}

/**
 * Searches the receiver's list starting at the given,
 * zero-relative index until an item is found that is equal
 * to the argument, and returns the index of that item. If
 * no item is found or the starting index is out of range,
 * returns -1.
 *
 * @param string the search item
 * @param start the zero-relative index at which to begin the search
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (String string, int start) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (!(0 <= start && start < items.length)) return -1;
	for (int i=start; i<items.length; i++) {
		if (string.equals(items [i])) return i;
	}
	return -1;
}

@Override
boolean isFocusHandle(long widget) {
	if (buttonHandle != 0 && widget == buttonHandle) return true;
	if (entryHandle != 0 && widget == entryHandle) return true;
	return super.isFocusHandle (widget);
}

@Override
long paintSurface () {
	long childHandle =  entryHandle != 0 ? entryHandle : handle;
	GTK.gtk_widget_realize (childHandle);
	long surface = gtk_widget_get_surface (childHandle);
	if ((style & SWT.READ_ONLY) != 0) return surface;
	/*
	 * TODO: GTK4 no access to children of the surface
	 * for combobox may need to use gtk_combo_box_get_child ().
	 * See also Bug 570331, maybe entire function just needs to be removed
	 */

	return surface;
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
 *
 * @since 2.1
 */
public void paste () {
	checkWidget ();
	if (entryHandle != 0) GTK3.gtk_editable_paste_clipboard (entryHandle);
}

@Override
long parentingHandle() {
	return fixedHandle;
}

@Override
void register () {
	super.register ();
	if (buttonHandle != 0) display.addWidget (buttonHandle, this);
	if (entryHandle != 0) display.addWidget (entryHandle, this);
	if (popupHandle != 0) display.addWidget (popupHandle, this);
	if (menuHandle != 0) display.addWidget (menuHandle, this);
	long imContext = imContext ();
	if (imContext != 0) display.addWidget (imContext, this);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	if (menuHandle != 0) {
		OS.g_object_unref (menuHandle);
		menuHandle = 0;
	}
	if (buttonHandle != 0) {
		OS.g_object_unref (buttonHandle);
		buttonHandle = 0;
	}
	if (buttonBoxHandle != 0) {
		OS.g_object_unref (buttonBoxHandle);
		buttonBoxHandle = 0;
	}
	if (cellBoxHandle != 0) {
		OS.g_object_unref (cellBoxHandle);
		cellBoxHandle = 0;
	}
	entryHandle = 0;

	if (cssProvider != 0) {
		OS.g_object_unref(cssProvider);
		cssProvider = 0;
	}
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	textRenderer = 0;
	fixIM ();
}

/**
 * Removes the item from the receiver's list at the given
 * zero-relative index.
 *
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int index) {
	checkWidget();
	if (!(0 <= index && index < items.length)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	String [] oldItems = items;
	String [] newItems = new String [oldItems.length - 1];
	System.arraycopy (oldItems, 0, newItems, 0, index);
	System.arraycopy (oldItems, index + 1, newItems, index, oldItems.length - index - 1);
	items = newItems;
	if (GTK.gtk_combo_box_get_active (handle) == index) clearText ();
	if (handle != 0) GTK.gtk_combo_box_text_remove(handle, index);
}

/**
 * Removes the items from the receiver's list which are
 * between the given zero-relative start and end
 * indices (inclusive).
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if either the start or end are not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int start, int end) {
	checkWidget();
	if (start > end) return;
	if (!(0 <= start && start <= end && end < items.length)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	String [] oldItems = items;
	String [] newItems = new String [oldItems.length - (end - start + 1)];
	System.arraycopy (oldItems, 0, newItems, 0, start);
	System.arraycopy (oldItems, end + 1, newItems, start, oldItems.length - end - 1);
	items = newItems;
	int index = GTK.gtk_combo_box_get_active (handle);
	if (start <= index && index <= end) clearText();

	gtk_combo_box_toggle_wrap(false);
	for (int i = end; i >= start; i--) {
		if (handle != 0) GTK.gtk_combo_box_text_remove(handle, i);
	}
	gtk_combo_box_toggle_wrap(true);
}

/**
 * Searches the receiver's list starting at the first item
 * until an item is found that is equal to the argument,
 * and removes that item from the list.
 *
 * @param string the item to remove
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the string is not found in the list</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int index = indexOf (string, 0);
	if (index == -1) error (SWT.ERROR_INVALID_ARGUMENT);
	remove (index);
}

/**
 * Removes all of the items from the receiver's list and clear the
 * contents of receiver's text field.
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget();

	items = new String[0];
	clearText();
	gtk_combo_box_text_remove_all();
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
	checkWidget();
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
 * @since 3.103
 */
public void removeSegmentListener (SegmentListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	eventTable.unhook (SWT.Segments, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the user changes the receiver's selection.
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);
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
 *
 * @since 3.1
 */
public void removeVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);
}

/**
 * Selects the item at the given zero-relative index in the receiver's
 * list.  If the item at the index was already selected, it remains
 * selected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int index) {
	checkWidget();
	if (index < 0 || index >= items.length) return;
	int selected = GTK.gtk_combo_box_get_active (handle);
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_combo_box_set_active (handle, index);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if ((style & SWT.READ_ONLY) != 0 && selected != index) {
		/*
		* Feature in GTK. Read Only combo boxes do not get a chance to send out a
		* Modify event in the gtk_changed callback. The fix is to send a Modify event
		* here.
		*/
		sendEvent (SWT.Modify);
	}
	unselected = false;
}

@Override
void setBackgroundGdkRGBA (long context, long handle, GdkRGBA rgba) {
	background = rgba;
	updateCss();
}

@Override
int setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	int newHeight = height;
	if (resize) newHeight = Math.max (getTextHeightInPixels (), height);
	return super.setBounds (x, y, width, newHeight, move, resize);
}

void setButtonHandle (long widget) {
	if (buttonHandle == widget) return;
	if (buttonHandle != 0) {
		display.removeWidget (buttonHandle);
		OS.g_object_unref (buttonHandle);
	}
	buttonHandle = widget;
	if (buttonHandle != 0) {
		OS.g_object_ref (buttonHandle);
		display.addWidget (buttonHandle, this);
		hookEvents (new long []{buttonHandle});
	}
}

void setMenuHandle (long widget) {
	if (menuHandle == widget) return;
	if (menuHandle != 0) {
		display.removeWidget (menuHandle);
		OS.g_object_unref (menuHandle);
	}
	menuHandle = widget;
	if (menuHandle != 0) {
		OS.g_object_ref (menuHandle);
		display.addWidget (menuHandle, this);
		hookEvents (new long []{menuHandle});
	}
}

@Override
void setFontDescription (long font) {
	super.setFontDescription (font);
	if (entryHandle != 0) setFontDescription (entryHandle, font);
	OS.g_object_set (textRenderer, OS.font_desc, font, 0);
	if ((style & SWT.READ_ONLY) != 0) {
		/*
		* Bug in GTK.  Setting the font can leave the combo box with an
		* invalid minimum size.  The fix is to temporarily change the
		* selected item to force the combo box to resize.
		*/
		int index = GTK.gtk_combo_box_get_active (handle);
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		GTK.gtk_combo_box_set_active (handle, -1);
		GTK.gtk_combo_box_set_active (handle, index);
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	}
}

@Override
void setForegroundGdkRGBA (long handle, GdkRGBA rgba) {
	OS.g_object_set(textRenderer, OS.foreground_rgba, rgba, 0);

	foreground = rgba;
	updateCss();
}

@Override
void setInitialBounds () {
	if ((state & ZERO_WIDTH) != 0 && (state & ZERO_HEIGHT) != 0) {
		/*
		* Feature in GTK.  On creation, each widget's allocation is
		* initialized to a position of (-1, -1) until the widget is
		* first sized.  The fix is to set the value to (0, 0) as
		* expected by SWT.
		*/
		long topHandle = topHandle ();
		GtkAllocation allocation = new GtkAllocation();
		if ((parent.style & SWT.MIRRORED) != 0) {
			allocation.x = parent.getClientWidth ();
		} else {
			allocation.x = 0;
		}
		allocation.y = 0;
		GTK.gtk_widget_set_visible(topHandle, true);
		if (GTK.GTK4) {
			GTK4.gtk_widget_size_allocate (topHandle, allocation, -1);
		} else {
			// Prevent GTK+ allocation warnings, preferred size should be retrieved before setting allocation size.
			GTK.gtk_widget_get_preferred_size(topHandle, null, null);
			GTK3.gtk_widget_set_allocation(topHandle, allocation);
		}
	} else {
		super.setInitialBounds();
	}
}

/**
 * Sets the text of the item in the receiver's list at the given
 * zero-relative index to the string argument.
 *
 * @param index the index for the item
 * @param string the new text for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setItem (int index, String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (!(0 <= index && index < items.length)) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	items [index] = string;
	if (handle != 0) {
		GTK.gtk_combo_box_text_remove (handle, index);
	}
	gtk_combo_box_insert(string, index);

	if ((style & SWT.RIGHT_TO_LEFT) != 0 && popupHandle != 0) {
		GTK3.gtk_container_forall (popupHandle, display.setDirectionProc, GTK.GTK_TEXT_DIR_RTL);
	}
}

/**
 * Sets the receiver's list to be the given array of items.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if an item in the items array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setItems (String... items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<items.length; i++) {
		if (items [i] == null) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	this.items = new String [items.length];
	System.arraycopy (items, 0, this.items, 0, items.length);
	clearText ();

	gtk_combo_box_text_remove_all();
	for (int i = 0; i < items.length; i++) {
		String string = items [i];
		gtk_combo_box_insert(string, i);
		if ((style & SWT.RIGHT_TO_LEFT) != 0 && popupHandle != 0) {
			GTK3.gtk_container_forall (popupHandle, display.setDirectionProc, GTK.GTK_TEXT_DIR_RTL);
		}
	}
}

private void gtk_combo_box_text_remove_all() {
	gtk_combo_box_toggle_wrap(false);
	if (handle != 0) GTK.gtk_combo_box_text_remove_all(handle);
	gtk_combo_box_toggle_wrap(true);
}

/**
 * Marks the receiver's list as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setListVisible (boolean visible) {
	checkWidget ();
	if (visible) {
		GTK.gtk_combo_box_popup (handle);
	} else {
		GTK.gtk_combo_box_popdown (handle);
	}
}

@Override
void setOrientation (boolean create) {
	super.setOrientation (create);
	if ((style & SWT.RIGHT_TO_LEFT) != 0 || !create) {
		int dir = (style & SWT.RIGHT_TO_LEFT) != 0 ? GTK.GTK_TEXT_DIR_RTL : GTK.GTK_TEXT_DIR_LTR;
		if (entryHandle != 0) GTK.gtk_widget_set_direction (entryHandle, dir);
		if (cellHandle != 0) GTK.gtk_widget_set_direction (cellHandle, dir);
		if (!create) {
			if (popupHandle != 0) GTK3.gtk_container_forall (popupHandle, display.setDirectionProc, dir);
		}
	}
}

/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
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
	super.setOrientation (orientation);
}

/**
 * Sets the selection in the receiver's text field to the
 * range specified by the argument whose x coordinate is the
 * start of the selection and whose y coordinate is the end
 * of the selection.
 *
 * @param selection a point representing the new selection start and end
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
	checkWidget();
	if (selection == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.READ_ONLY) != 0) return;

	if (entryHandle != 0) {
		long textPtr = 0;
		if (GTK.GTK4) {
			long bufferHandle = GTK4.gtk_entry_get_buffer(entryHandle);
			textPtr = GTK.gtk_entry_buffer_get_text(bufferHandle);
		} else {
			textPtr = GTK3.gtk_entry_get_text(entryHandle);
		}

		int start = (int)OS.g_utf16_offset_to_utf8_offset(textPtr, selection.x);
		int end = (int)OS.g_utf16_offset_to_utf8_offset(textPtr, selection.y);
		GTK.gtk_editable_set_position(entryHandle, start);
		GTK.gtk_editable_select_region(entryHandle, start, end);
	}
}

/**
 * Sets the contents of the receiver's text field to the
 * given string.
 * <p>
 * This call is ignored when the receiver is read only and
 * the given string is not in the receiver's list.
 * </p>
 * <p>
 * Note: The text field in a <code>Combo</code> is typically
 * only capable of displaying a single line of text. Thus,
 * setting the text to a string containing line breaks or
 * other special characters will probably cause it to
 * display incorrectly.
 * </p><p>
 * Also note, if control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
 * </p>
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.READ_ONLY) != 0) {
		int index = indexOf (string);
		if (index == -1) return;
		select (index);
		return;
	}
	/*
	* Feature in gtk.  When text is set in gtk, separate events are fired for the deletion and
	* insertion of the text.  This is not wrong, but is inconsistent with other platforms.  The
	* fix is to block the firing of these events and fire them ourselves in a consistent manner.
	*/
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		long ptr = GTK3.gtk_entry_get_text (entryHandle);
		string = verifyText (string, 0, (int)OS.g_utf16_strlen (ptr, -1));
		if (string == null) return;
	}
	byte [] buffer = Converter.wcsToMbcs (string, true);
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
	OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
	if (GTK.GTK4) {
		long bufferHandle = GTK4.gtk_entry_get_buffer(entryHandle);
		GTK.gtk_entry_buffer_set_text(bufferHandle, buffer, string.length());
	} else {
		GTK3.gtk_entry_set_text (entryHandle, buffer);
	}
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_signal_handlers_unblock_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_signal_handlers_unblock_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
	OS.g_signal_handlers_unblock_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
	sendEvent (SWT.Modify);
}

/**
 * Sets the maximum number of characters that the receiver's
 * text field is capable of holding to be the argument.
 * <p>
 * To reset this value to the default, use <code>setTextLimit(Combo.LIMIT)</code>.
 * Specifying a limit value larger than <code>Combo.LIMIT</code> sets the
 * receiver's limit to <code>Combo.LIMIT</code>.
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
 */
public void setTextLimit (int limit) {
	checkWidget();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	if (entryHandle != 0) GTK.gtk_entry_set_max_length (entryHandle, limit);
}

@Override
void setToolTipText(Shell shell, String newString) {
	if (entryHandle != 0) setToolTipText(entryHandle, newString);
	if (buttonHandle != 0) setToolTipText(buttonHandle, newString);
}

/**
 * Sets the number of items that are visible in the drop
 * down portion of the receiver's list.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 *
 * @param count the new number of items to be visible
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public void setVisibleItemCount (int count) {
	checkWidget ();
	if (count < 0) return;
	visibleCount = count;
}

@Override
boolean checkSubwindow () {
	return false;
}

@Override
boolean translateTraversal (long event) {
	int [] key = new int [1];
	if (GTK.GTK4) {
		key[0] = GDK.gdk_key_event_get_keyval(event);
	} else {
		GDK.gdk_event_get_keyval(event, key);
	}

	switch (key[0]) {
		case GDK.GDK_KP_Enter:
		case GDK.GDK_Return: {
			long imContext = imContext ();
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

void updateCss() {
	if (cssProvider == 0) {
		cssProvider = GTK.gtk_css_provider_new();

		if (menuHandle != 0) {
			long context = GTK.gtk_widget_get_style_context(menuHandle);
			GTK.gtk_style_context_add_provider(context, cssProvider, GTK.GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);
		}

		if (buttonHandle != 0) {
			long context = GTK.gtk_widget_get_style_context(buttonHandle);
			GTK.gtk_style_context_add_provider(context, cssProvider, GTK.GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);
		}

		if (entryHandle != 0) {
			long context = GTK.gtk_widget_get_style_context(entryHandle);
			GTK.gtk_style_context_add_provider(context, cssProvider, GTK.GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);
		}
	}

	StringBuilder css = new StringBuilder();

	// Deal with background
	if (background != null) {
		final String colorString = display.gtk_rgba_to_css_string(background);

		/*
		 * Use 'background:' instead of 'background-color:' to also override
		 * any 'background-image:'. For example, Ubuntu's Yaru theme has
		 * 'background-image:' for 'GtkToggleButton' used in READ_ONLY combo.
		 */
		css.append("* {background: " + colorString + ";}\n");
		css.append("menu {background: " + colorString + ";}\n");

		/*
		 * Setting background color for '*' also affects selection background,
		 * making it hard to see selected text. Fix this by forcing selection
		 * colors to reasonable ones. A better fix would be to list affected
		 * classes explicitly instead of using '*'. If you're doing this,
		 * please also compare screenshots of snippet from Bug 570502.
		 */
		final String clrSelectionBack = display.gtk_rgba_to_css_string(display.COLOR_LIST_SELECTION_RGBA);
		final String clrSelectionFore = display.gtk_rgba_to_css_string(display.COLOR_LIST_SELECTION_TEXT_RGBA);
		css.append("entry selection {background-color: " + clrSelectionBack + ";}\n");
		css.append("entry selection {color: " + clrSelectionFore + ";}\n");
	}

	// Deal with foreground
	if (foreground != null) {
		final String colorString = display.gtk_rgba_to_css_string(foreground);

		css.append("* {color: " + colorString + ";}\n");
	}

	// Update CSS provider
	if (GTK.GTK4) {
		GTK4.gtk_css_provider_load_from_data (cssProvider, Converter.wcsToMbcs (css.toString(), true), -1);
	} else {
		GTK3.gtk_css_provider_load_from_data (cssProvider, Converter.wcsToMbcs (css.toString(), true), -1, null);
	}
}

String verifyText (String string, int start, int end) {
	if (string.length () == 0 && start == end) return null;
	Event event = new Event ();
	event.text = string;
	event.start = start;
	event.end = end;
	long eventPtr = GTK.GTK4 ? 0 : GTK3.gtk_get_current_event ();
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

}
