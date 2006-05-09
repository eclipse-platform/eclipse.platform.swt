/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
 * <dd>DefaultSelection, Modify, Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles DROP_DOWN and SIMPLE may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see List
 */
public class Combo extends Composite {
	int /*long*/ arrowHandle, entryHandle, listHandle;
	int lastEventTime, visibleCount = 5;
	int /*long*/ gdkEventKey = 0;
	int fixStart = -1, fixEnd = -1;
	String [] items = new String [0];
	boolean ignoreSelect, lockText;

	static final int INNER_BORDER = 2;

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
 *
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
	add(string, items.length);
}

/**
 * Adds the argument to the receiver's list at the given
 * zero-relative index.
 * <p>
 * Note: To add an item at the end of the list, use the
 * result of calling <code>getItemCount()</code> as the
 * index or use <code>add(String)</code>.
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
public void add (String string, int index) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (!(0 <= index && index <= items.length)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	String [] newItems = new String [items.length + 1];
	System.arraycopy (items, 0, newItems, 0, index);
	newItems [index] = string;
	System.arraycopy (items, index, newItems, index + 1, items.length - index);
	items = newItems;
	/*
	* Feature in GTK. When the list is empty and the first item
	* is added, the combo box selects that item replacing the
	* text in the entry field.  The fix is to avoid this by
	* stopping the "delete" and "insert_text" signal emission. 
	*/
	ignoreSelect = lockText = true;
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int /*long*/ item = OS.gtk_list_item_new_with_label (buffer);
	int /*long*/ label = OS.gtk_bin_get_child (item); 
	OS.gtk_widget_modify_fg (label, OS.GTK_STATE_NORMAL, getForegroundColor ());
	OS.gtk_widget_modify_font (label, getFontDescription ());
	OS.gtk_widget_set_direction (label, OS.gtk_widget_get_direction (handle));
	OS.gtk_widget_show (item);
	int /*long*/ items = OS.g_list_append (0, item);
	OS.gtk_list_insert_items (listHandle, items, index);
	ignoreSelect = lockText = false;
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
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the combo's list selection changes.
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
	int position = OS.gtk_editable_get_position (entryHandle);
	OS.gtk_editable_select_region (entryHandle, position, position);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	int[] w = new int [1], h = new int [1];
	int /*long*/ layout = OS.gtk_entry_get_layout (entryHandle);
	OS.pango_layout_get_size (layout, w, h);
	int xborder = INNER_BORDER, yborder = INNER_BORDER;
	int /*long*/ style = OS.gtk_widget_get_style (entryHandle);
	xborder += OS.gtk_style_get_xthickness (style);
	yborder += OS.gtk_style_get_ythickness (style);
	int [] property = new int [1];
	OS.gtk_widget_style_get (entryHandle, OS.interior_focus, property, 0);
	if (property [0] == 0) {
		OS.gtk_widget_style_get (entryHandle, OS.focus_line_width, property, 0);
		xborder += property [0];
		yborder += property [0];
	}
	int width = OS.PANGO_PIXELS (w [0]) + xborder  * 2;
	int height = OS.PANGO_PIXELS (h [0]) + yborder  * 2;

	GtkRequisition arrowRequesition = new GtkRequisition ();
	OS.gtk_widget_size_request (arrowHandle, arrowRequesition);
	GtkRequisition listRequesition = new GtkRequisition ();
	int /*long*/ listParent = OS.gtk_widget_get_parent (listHandle);
	OS.gtk_widget_size_request (listParent != 0 ? listParent : listHandle, listRequesition);
	
	width = Math.max (listRequesition.width, width) + arrowRequesition.width + 4;
	width = wHint == SWT.DEFAULT ? width : wHint;
	height = hHint == SWT.DEFAULT ? height : hHint;
	return new Point (width, height);
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
	OS.gtk_editable_copy_clipboard (entryHandle);
}

void createHandle (int index) {
	state |= HANDLE | MENU;
	fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	handle = OS.gtk_combo_new ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_container_add (fixedHandle, handle);
	GtkCombo combo = new GtkCombo ();
	OS.memmove (combo, handle);
	entryHandle = combo.entry;
	listHandle = combo.list;
	
	/*
	* Feature in GTK.  There is no API to query the arrow
	* handle from a combo box although it is possible to
	* get the list and text field.  The arrow handle is needed
	* to hook events.  The fix is to find the first child that is
	* not the entry or list and assume this is the arrow handle.
	*/
	int /*long*/ list = OS.gtk_container_get_children (handle);
	if (list != 0) {
		int i = 0, count = OS.g_list_length (list);
		while (i<count) {
			int /*long*/ childHandle = OS.g_list_nth_data (list, i);
			if (childHandle != entryHandle && childHandle != listHandle) {
				arrowHandle = childHandle;
				break;
			}
			i++;
		}
		OS.g_list_free (list);
	}
	
	boolean editable = (style & SWT.READ_ONLY) == 0;
	OS.gtk_editable_set_editable (entryHandle, editable);
	OS.gtk_combo_disable_activate (handle);
	OS.gtk_combo_set_case_sensitive (handle, true);
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
	OS.gtk_editable_cut_clipboard (entryHandle);
}

void deregister () {
	super.deregister ();
	if (arrowHandle != 0) display.removeWidget (arrowHandle);
	display.removeWidget (entryHandle);
	display.removeWidget (listHandle);
	int /*long*/ imContext = imContext ();
	if (imContext != 0) display.removeWidget (imContext);
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

int /*long*/ fontHandle () {
	if (entryHandle != 0) return entryHandle;
	return super.fontHandle ();
}

int /*long*/ focusHandle () {
	if (entryHandle != 0) return entryHandle;
	return super.focusHandle ();
}

boolean hasFocus () {
	if (super.hasFocus ()) return true;
	if (OS.GTK_WIDGET_HAS_FOCUS (entryHandle)) return true;
	if (OS.GTK_WIDGET_HAS_FOCUS (listHandle)) return true;
	return false;
}

void hookEvents () {
	//TODO - fix multiple enter/exit
	super.hookEvents ();
	OS.g_signal_connect_closure (entryHandle, OS.changed, display.closures [CHANGED], true);
	OS.g_signal_connect_closure (entryHandle, OS.insert_text, display.closures [INSERT_TEXT], false);
	OS.g_signal_connect_closure (entryHandle, OS.delete_text, display.closures [DELETE_TEXT], false);
	OS.g_signal_connect_closure (entryHandle, OS.activate, display.closures [ACTIVATE], false);
	int eventMask =	OS.GDK_POINTER_MOTION_MASK | OS.GDK_BUTTON_PRESS_MASK |
		OS.GDK_BUTTON_RELEASE_MASK | OS.GDK_ENTER_NOTIFY_MASK |
		OS.GDK_LEAVE_NOTIFY_MASK;
	int /*long*/ [] handles = new int /*long*/ [] {arrowHandle, entryHandle, listHandle};
	for (int i=0; i<handles.length; i++) {
		int /*long*/ eventHandle = handles [i];
		if (eventHandle != 0) {
			/* Connect the mouse signals */
			OS.gtk_widget_add_events (eventHandle, eventMask);
			OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [BUTTON_PRESS_EVENT], 0, display.closures [BUTTON_PRESS_EVENT], false);
			OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [BUTTON_RELEASE_EVENT], 0, display.closures [BUTTON_RELEASE_EVENT], false);
			OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [MOTION_NOTIFY_EVENT], 0, display.closures [MOTION_NOTIFY_EVENT], false);
			OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [ENTER_NOTIFY_EVENT], 0, display.closures [ENTER_NOTIFY_EVENT], false);
			OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [LEAVE_NOTIFY_EVENT], 0, display.closures [LEAVE_NOTIFY_EVENT], false);
			/*
			* Feature in GTK.  Events such as mouse move are propagated up
			* the widget hierarchy and are seen by the parent.  This is the
			* correct GTK behavior but not correct for SWT.  The fix is to
			* hook a signal after and stop the propagation using a negative
			* event number to distinguish this case.
			*/
			OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [BUTTON_PRESS_EVENT], 0, display.closures [BUTTON_PRESS_EVENT_INVERSE], true);
			OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [BUTTON_RELEASE_EVENT], 0, display.closures [BUTTON_RELEASE_EVENT_INVERSE], true);
			OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [MOTION_NOTIFY_EVENT], 0, display.closures [MOTION_NOTIFY_EVENT_INVERSE], true);

			/* Connect the event_after signal for both key and mouse */
			if (eventHandle != entryHandle) {
				OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [EVENT_AFTER], 0, display.closures [EVENT_AFTER], false);
			}
		}
	}
	int /*long*/ imContext = imContext ();
	if (imContext != 0) {
		OS.g_signal_connect_closure (imContext, OS.commit, display.closures [COMMIT], false);
		int id = OS.g_signal_lookup (OS.commit, OS.gtk_im_context_get_type ());
		int blockMask =  OS.G_SIGNAL_MATCH_DATA | OS.G_SIGNAL_MATCH_ID;
		OS.g_signal_handlers_block_matched (imContext, blockMask, id, 0, 0, 0, entryHandle);
	}	
}

int /*long*/ imContext () {
	return OS.GTK_ENTRY_IM_CONTEXT (entryHandle);
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
	ignoreSelect = true;
	int /*long*/ children = OS.gtk_container_get_children (listHandle);
	int /*long*/ item = OS.g_list_nth_data (children, index);
	boolean selected = OS.GTK_WIDGET_STATE (item) == OS.GTK_STATE_SELECTED;
	if (selected) {
		OS.gtk_list_unselect_all (listHandle);
		OS.gtk_entry_set_text (entryHandle, new byte[1]);
	}
	OS.g_list_free (children);
	ignoreSelect = false;
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
	ignoreSelect = true;
	OS.gtk_list_unselect_all (listHandle);
	OS.gtk_entry_set_text (entryHandle, new byte[1]);
	ignoreSelect = false;
}

GdkColor getBackgroundColor () {
	return getBaseColor ();
}

GdkColor getForegroundColor () {
	return getTextColor ();
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
	return fontHeight (getFontDescription (), listHandle != 0 ? listHandle : handle);
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
public String [] getItems () {
	checkWidget();
	String [] result = new String [items.length];
	System.arraycopy (items, 0, result, 0, items.length);
	return result;
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
public int getOrientation () {
	checkWidget();
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
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
	int [] start = new int [1];
	int [] end = new int [1];
	OS.gtk_editable_get_selection_bounds (entryHandle, start, end);
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
	int index = 0, result = -1;
	int /*long*/ children = OS.gtk_container_get_children (listHandle);
	int /*long*/ temp = children;
	while (temp != 0) {
		int /*long*/ item = OS.g_list_data (temp);
		if (OS.GTK_WIDGET_STATE (item) == OS.GTK_STATE_SELECTED) {
			result = index;
			break;
		}
		index++;
		temp = OS.g_list_next (temp);
	}	
	OS.g_list_free (children);
	return result;
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
	int /*long*/ address = OS.gtk_entry_get_text (entryHandle);
	int length = OS.strlen (address);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, address, length);
	return new String (Converter.mbcsToWcs (null, buffer));
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
	return fontHeight (getFontDescription (), entryHandle != 0 ? entryHandle : handle) + 8;
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
	int limit = OS.gtk_entry_get_max_length (entryHandle);
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

int /*long*/ gtk_activate (int /*long*/ widget) {
	postEvent (SWT.DefaultSelection);
	return 0;
}

int /*long*/ gtk_changed (int /*long*/ widget) {
	if (!ignoreSelect) {
		int /*long*/ ptr = OS.gtk_entry_get_text (entryHandle);
		int length = OS.strlen (ptr);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, ptr, length);
		String text = new String (Converter.mbcsToWcs (null, buffer));
		for (int i = 0; i < items.length; i++) {
			if (items [i].equals (text)) {
				postEvent (SWT.Selection);
				break;
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
	return 0;
}

int /*long*/ gtk_commit (int /*long*/ imContext, int /*long*/ text) {
	if (text == 0) return 0;
	if (!OS.gtk_editable_get_editable (entryHandle)) return 0;
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
	OS.g_signal_handlers_unblock_matched (imContext, mask, id, 0, 0, 0, entryHandle);
	if (newChars == chars) {
		OS.g_signal_emit_by_name (imContext, OS.commit, text);
	} else {
		buffer = Converter.wcsToMbcs (null, newChars, true);
		OS.g_signal_emit_by_name (imContext, OS.commit, buffer);
	}
	OS.g_signal_handlers_unblock_matched (imContext, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, COMMIT);
	OS.g_signal_handlers_block_matched (imContext, mask, id, 0, 0, 0, entryHandle);
	if (fixStart != -1 && fixEnd != -1) {
		OS.gtk_editable_set_position (entryHandle, fixStart);
		OS.gtk_editable_select_region (entryHandle, fixStart, fixEnd);
	}
	fixStart = fixEnd = -1;
	return 0;
}

int /*long*/ gtk_delete_text (int /*long*/ widget, int /*long*/ start_pos, int /*long*/ end_pos) {
	if (lockText) {
		OS.gtk_list_unselect_item (listHandle, 0);
		OS.g_signal_stop_emission_by_name (entryHandle, OS.delete_text);
		return 0;
	}
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	String newText = verifyText ("", (int)/*64*/start_pos, (int)/*64*/end_pos);
	if (newText == null) {
		OS.g_signal_stop_emission_by_name (entryHandle, OS.delete_text);
	} else {
		if (newText.length () > 0) {
			int [] pos = new int [1];
			pos [0] = (int)/*64*/end_pos;
			byte [] buffer = Converter.wcsToMbcs (null, newText, false);
			OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			OS.gtk_editable_insert_text (entryHandle, buffer, buffer.length, pos);
			OS.g_signal_handlers_unblock_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			OS.g_signal_handlers_unblock_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			OS.gtk_editable_set_position (entryHandle, pos [0]);
		}
	}
	return 0;
}

int /*long*/ gtk_focus_out_event (int /*long*/ widget, int /*long*/ event) {
	fixIM ();
	return super.gtk_focus_out_event (widget, event);
}

int /*long*/ gtk_insert_text (int /*long*/ widget, int /*long*/ new_text, int /*long*/ new_text_length, int /*long*/ position) {
	if (lockText) {
		OS.gtk_list_unselect_item (listHandle, 0);
		OS.g_signal_stop_emission_by_name (entryHandle, OS.insert_text);
		return 0;
	}
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;	
	if (new_text == 0 || new_text_length == 0) return 0;
	byte [] buffer = new byte [(int)/*64*/new_text_length];
	OS.memmove (buffer, new_text, buffer.length);
	String oldText = new String (Converter.mbcsToWcs (null, buffer));
	int [] pos = new int [1];
	OS.memmove (pos, position, 4);
	if (pos [0] == -1) {
		int /*long*/ ptr = OS.gtk_entry_get_text (entryHandle);
		pos [0] = (int)/*64*/OS.g_utf8_strlen (ptr, -1);
	}
	String newText = verifyText (oldText, pos [0], pos [0]);
	if (newText != oldText) {
		int [] newStart = new int [1], newEnd = new int [1];
		OS.gtk_editable_get_selection_bounds (entryHandle, newStart, newEnd);
		if (newText != null) {
			if (newStart [0] != newEnd [0]) {
				OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
				OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				OS.gtk_editable_delete_selection (entryHandle);
				OS.g_signal_handlers_unblock_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
				OS.g_signal_handlers_unblock_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			}
			byte [] buffer3 = Converter.wcsToMbcs (null, newText, false);
			OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			OS.gtk_editable_insert_text (entryHandle, buffer3, buffer3.length, pos);
			OS.g_signal_handlers_unblock_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
			newEnd [0] = pos [0];
		}
		pos [0] = newEnd [0];
		if (newStart [0] != newEnd [0] && newEnd [0] != pos [0]) {
			fixStart = newStart [0];
			fixEnd = newEnd [0];
		}
		OS.memmove (position, pos, 4);
		OS.g_signal_stop_emission_by_name (entryHandle, OS.insert_text);
	}
	return 0;
}

int /*long*/ gtk_key_press_event (int /*long*/ widget, int /*long*/ event) {
	int /*long*/ result = super.gtk_key_press_event (widget, event);
	if (result != 0) fixIM ();
	if (gdkEventKey == -1) result = 1;
	gdkEventKey = 0;
	return result;
}

int /*long*/ gtk_popup_menu (int /*long*/ widget) {
	int [] x = new int [1], y = new int [1];
	OS.gdk_window_get_pointer (0, x, y, null);
	return showMenu (x [0], y [0]) ? 1 : 0;
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
	return indexOf (string, 0);
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
	OS.gtk_editable_paste_clipboard (entryHandle);
}

int /*long*/ parentingHandle() {
	return fixedHandle;
}

void register () {
	super.register ();
	if (arrowHandle != 0) display.addWidget (arrowHandle, this);
	display.addWidget (entryHandle, this);
	display.addWidget (listHandle, this);
	int /*long*/ imContext = imContext ();
	if (imContext != 0) display.addWidget (imContext, this);
}

void releaseHandle () {
	super.releaseHandle ();
	entryHandle = listHandle = 0;
}

void releaseWidget () {
	super.releaseWidget ();
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
	ignoreSelect = true;
	int /*long*/ children = OS.gtk_container_get_children (listHandle);
	int /*long*/ item = OS.g_list_nth_data (children, index);
	boolean selected = OS.GTK_WIDGET_STATE (item) == OS.GTK_STATE_SELECTED;
	int /*long*/ items = OS.g_list_append (0, item);
	OS.gtk_list_remove_items (listHandle, items);
	OS.g_list_free (items);
	OS.g_list_free (children);
	if (selected) {
		OS.gtk_entry_set_text (entryHandle, new byte[1]);
	}
	ignoreSelect = false;
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
	boolean selected = false;
	ignoreSelect = true;
	int /*long*/ items = 0;
	int /*long*/ children = OS.gtk_container_get_children (listHandle);
	for (int i = start; i <= end; i++) {
		int /*long*/ item = OS.g_list_nth_data (children, i);
		selected |= OS.GTK_WIDGET_STATE (item) == OS.GTK_STATE_SELECTED;
		items = OS.g_list_append (items, item);
	}
	OS.gtk_list_remove_items (listHandle, items);
	OS.g_list_free (items);
	OS.g_list_free (children);
	if (selected) {
		OS.gtk_entry_set_text (entryHandle, new byte[1]);
	}
	ignoreSelect = false;
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
 * <p>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget();
	ignoreSelect = true;
	OS.gtk_list_clear_items (listHandle, 0, -1);
	OS.gtk_entry_set_text (entryHandle, new byte[1]);
	items = new String[0];
	ignoreSelect = false;
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
 * be notified when the receiver's selection changes.
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
	ignoreSelect = true;
	OS.gtk_list_select_item (listHandle, index);
	ignoreSelect = false;
}

void setBackgroundColor (GdkColor color) {
	super.setBackgroundColor (color);
	if (entryHandle != 0) OS.gtk_widget_modify_base (entryHandle, 0, color);
	if (listHandle != 0) OS.gtk_widget_modify_base (listHandle, 0, color);
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	int newHeight = resize ? getTextHeight () : height;
	return super.setBounds (x, y, width, newHeight, move, resize);
}

void setFontDescription (int /*long*/ font) {
	super.setFontDescription (font);
	if (entryHandle != 0) OS.gtk_widget_modify_font (entryHandle, font);
	if (listHandle != 0) {
		OS.gtk_widget_modify_font (listHandle, font);
		int /*long*/ itemsList = OS.gtk_container_get_children (listHandle);
		if (itemsList != 0) {
			int count = OS.g_list_length (itemsList);
			for (int i=count - 1; i>=0; i--) {
				int /*long*/ widget = OS.gtk_bin_get_child (OS.g_list_nth_data (itemsList, i));
				OS.gtk_widget_modify_font (widget, font);
			}
			OS.g_list_free (itemsList);
		}
	}
}

void setForegroundColor (GdkColor color) {
	super.setForegroundColor (color);
	if (entryHandle != 0) OS.gtk_widget_modify_text (entryHandle, 0, color);
	if (listHandle != 0) {
		OS.gtk_widget_modify_text (listHandle, 0, color);
		int /*long*/ itemsList = OS.gtk_container_get_children (listHandle);
		if (itemsList != 0) {
			int count = OS.g_list_length (itemsList);
			for (int i=count - 1; i>=0; i--) {
				int /*long*/ widget = OS.gtk_bin_get_child (OS.g_list_nth_data (itemsList, i));
				OS.gtk_widget_modify_fg (widget,  OS.GTK_STATE_NORMAL, color);
			}
			OS.g_list_free (itemsList);
		}
	}
}

/**
 * Sets the text of the item in the receiver's list at the given
 * zero-relative index to the string argument. This is equivalent
 * to removing the old item at the index, and then adding the new
 * item at that index.
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
	ignoreSelect = true;
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int /*long*/ children = OS.gtk_container_get_children (listHandle);
	int /*long*/ item = OS.g_list_nth_data (children, index);
	int /*long*/ label = OS.gtk_bin_get_child (item);
	OS.gtk_label_set_text (label, buffer);
	OS.g_list_free (children);
	ignoreSelect = false;
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
public void setItems (String [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<items.length; i++) {
		if (items [i] == null) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	lockText = ignoreSelect = true;
	OS.gtk_list_clear_items (listHandle, 0, -1);
	int /*long*/ font = getFontDescription ();
	GdkColor color = getForegroundColor ();
	int direction = OS.gtk_widget_get_direction (handle);
	int i = 0;
	while (i < items.length) {
		String string = items [i];
		byte [] buffer = Converter.wcsToMbcs (null, string, true);
		int /*long*/ item = OS.gtk_list_item_new_with_label (buffer);
		int /*long*/ label = OS.gtk_bin_get_child (item); 
		OS.gtk_widget_modify_fg (label, OS.GTK_STATE_NORMAL, color);
		OS.gtk_widget_modify_font (label, font);
		OS.gtk_widget_set_direction (label, direction);
		OS.gtk_container_add (listHandle, item);
		OS.gtk_widget_show (item);
		i++;
	}
	this.items = new String [items.length];
	System.arraycopy (items, 0, this.items, 0, i);
	lockText = ignoreSelect = false;
	OS.gtk_entry_set_text (entryHandle, new byte[1]);
}

void setOrientation() {
	super.setOrientation();
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		OS.gtk_widget_set_direction (listHandle, OS.GTK_TEXT_DIR_RTL);
		OS.gtk_widget_set_direction (entryHandle, OS.GTK_TEXT_DIR_RTL);
	}
}
/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 * <p>
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
	int flags = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
	if ((orientation & flags) == 0 || (orientation & flags) == flags) return;
	style &= ~flags;
	style |= orientation & flags;
	int dir = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? OS.GTK_TEXT_DIR_RTL : OS.GTK_TEXT_DIR_LTR;
	OS.gtk_widget_set_direction (fixedHandle, dir);
	OS.gtk_widget_set_direction (handle, dir);
	OS.gtk_widget_set_direction (listHandle, dir);
	OS.gtk_widget_set_direction (entryHandle, dir);
	int /*long*/ itemsList = OS.gtk_container_get_children (listHandle);
	if (itemsList != 0) {
		int count = OS.g_list_length (itemsList);
		for (int i=count - 1; i>=0; i--) {
			int /*long*/ widget = OS.gtk_bin_get_child (OS.g_list_nth_data (itemsList, i));
			OS.gtk_widget_set_direction (widget, dir);
		}
		OS.g_list_free (itemsList);
	}
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
	OS.gtk_editable_set_position (entryHandle, selection.x);
	OS.gtk_editable_select_region (entryHandle, selection.x, selection.y);
}

/**
 * Sets the contents of the receiver's text field to the
 * given string.
 * <p>
 * Note: The text field in a <code>Combo</code> is typically
 * only capable of displaying a single line of text. Thus,
 * setting the text to a string containing line breaks or
 * other special characters will probably cause it to 
 * display incorrectly.
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
	}
	/*
	* Feature in gtk.  When text is set in gtk, separate events are fired for the deletion and 
	* insertion of the text.  This is not wrong, but is inconsistent with other platforms.  The
	* fix is to block the firing of these events and fire them ourselves in a consistent manner. 
	*/
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		int /*long*/ ptr = OS.gtk_entry_get_text (entryHandle);
		string = verifyText (string, 0, (int)/*64*/OS.g_utf8_strlen (ptr, -1));
		if (string == null) return;
	}
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
	OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
	OS.gtk_entry_set_text (entryHandle, buffer);
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
	OS.gtk_entry_set_max_length (entryHandle, limit);
}

void setToolTipText (Shell shell, String newString, String oldString) {
	shell.setToolTipText (entryHandle, newString, oldString);
	shell.setToolTipText (arrowHandle, newString, oldString);
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

boolean translateTraversal (GdkEventKey keyEvent) {
	int key = keyEvent.keyval;
	switch (key) {
		case OS.GDK_KP_Enter:
		case OS.GDK_Return: {
			int /*long*/ imContext = imContext (); 
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
