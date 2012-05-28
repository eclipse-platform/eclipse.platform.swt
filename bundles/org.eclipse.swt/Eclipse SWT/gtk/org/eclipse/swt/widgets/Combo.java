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
	int /*long*/ buttonHandle, entryHandle, textRenderer, cellHandle, popupHandle, menuHandle;
	int lastEventTime, visibleCount = 10;
	int /*long*/ gdkEventKey = 0;
	int fixStart = -1, fixEnd = -1;
	String [] items = new String [0];
	boolean selectionAdded;
	int indexSelected;
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
	add (string, items.length);
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
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	OS.gtk_combo_box_insert_text (handle, index, buffer);
	if ((style & SWT.RIGHT_TO_LEFT) != 0 && popupHandle != 0) {
		OS.gtk_container_forall (popupHandle, display.setDirectionProc, OS.GTK_TEXT_DIR_RTL);    
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
		int position = OS.gtk_editable_get_position (entryHandle);
		OS.gtk_editable_select_region (entryHandle, position, position);
	}
}

void clearText () {
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if ((style & SWT.READ_ONLY) != 0) {
		int index = OS.gtk_combo_box_get_active (handle);
		if (index != -1) {
			int /*long*/ modelHandle = OS.gtk_combo_box_get_model (handle);
			int /*long*/ [] ptr = new int /*long*/ [1];
			int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
			OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
			OS.gtk_tree_model_get (modelHandle, iter, 0, ptr, -1);
			OS.g_free (iter);
			if (ptr [0] != 0 && OS.strlen (ptr [0]) > 0) postEvent (SWT.Modify);
			OS.g_free (ptr [0]);
		}
	} else {
		OS.gtk_entry_set_text (entryHandle, new byte[1]);
	}
	OS.gtk_combo_box_set_active (handle, -1);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	return computeNativeSize (handle, wHint, hHint, changed);
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
	if (entryHandle != 0) OS.gtk_editable_copy_clipboard (entryHandle);
}

void createHandle (int index) {
	state |= HANDLE | MENU;
	fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	int /*long*/ oldList = OS.gtk_window_list_toplevels ();  
	if ((style & SWT.READ_ONLY) != 0) {
		handle = OS.gtk_combo_box_new_text ();
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		cellHandle = OS.gtk_bin_get_child (handle);
		if (cellHandle == 0) error (SWT.ERROR_NO_HANDLES);
	} else {
		handle = OS.gtk_combo_box_entry_new_text ();
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		entryHandle = OS.gtk_bin_get_child (handle);
		if (entryHandle == 0) error (SWT.ERROR_NO_HANDLES);
	}
	popupHandle = findPopupHandle (oldList);    
	OS.gtk_container_add (fixedHandle, handle);
	textRenderer = OS.gtk_cell_renderer_text_new ();
	if (textRenderer == 0) error (SWT.ERROR_NO_HANDLES);
	/*
	* Feature in GTK. In order to make a read only combo box the same
	* height as an editable combo box the ypad must be set to 0. In 
	* versions 2.4.x of GTK, a pad of 0 will clip some letters. The
	* fix is to set the pad to 1.
	*/
	int pad = 0;
	if (OS.GTK_VERSION < OS.VERSION(2, 6, 0)) pad = 1;
	OS.g_object_set (textRenderer, OS.ypad, pad, 0);
	/*
	* Feature in GTK.  In version 2.4.9 of GTK, a warning is issued
	* when a call to gtk_cell_layout_clear() is made. The fix is to hide
	* the warning.
	*/
	boolean warnings = display.getWarnings ();
	display.setWarnings (false);
	OS.gtk_cell_layout_clear (handle);
	display.setWarnings (warnings);
	OS.gtk_cell_layout_pack_start (handle, textRenderer, true);
	OS.gtk_cell_layout_set_attributes (handle, textRenderer, OS.text, 0, 0);
	/*
	* Feature in GTK. Toggle button creation differs between GTK versions. The 
	* fix is to call size_request() to force the creation of the button 
	* for those versions of GTK that defer the creation. 
	*/
	if (OS.GTK_VERSION < OS.VERSION (2, 8, 0)) {
		OS.gtk_widget_size_request(handle, new GtkRequisition());
	}
	if (popupHandle != 0) findMenuHandle ();
	findButtonHandle ();
	/*
	* Feature in GTK. By default, read only combo boxes 
	* process the RETURN key rather than allowing the 
	* default button to process the key. The fix is to
	* clear the GTK_RECEIVES_DEFAULT flag.
	*/
	if ((style & SWT.READ_ONLY) != 0 && buttonHandle != 0) {
		OS.GTK_WIDGET_UNSET_FLAGS (buttonHandle, OS.GTK_RECEIVES_DEFAULT);
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
 * 
 * @since 2.1
 */
public void cut () {
	checkWidget ();
	if (entryHandle != 0) OS.gtk_editable_cut_clipboard (entryHandle);
}

void deregister () {
	super.deregister ();
	if (buttonHandle != 0) display.removeWidget (buttonHandle);
	if (entryHandle != 0) display.removeWidget (entryHandle);
	if (popupHandle != 0) display.removeWidget (popupHandle);
	if (menuHandle != 0) display.removeWidget (menuHandle);
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

int /*long*/ findPopupHandle (int /*long*/ oldList) {
	int /*long*/ result = 0;
	int /*long*/ currentList = OS.gtk_window_list_toplevels();
	int /*long*/ oldFromList = oldList;
	int /*long*/ newFromList = OS.g_list_last(currentList);
	boolean isFound;
	while (newFromList != 0) {
		int /*long*/ newToplevel = OS.g_list_data(newFromList);
		isFound = false;
		oldFromList = oldList;
		while (oldFromList != 0) {
			int /*long*/ oldToplevel = OS.g_list_data(oldFromList);
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


void findButtonHandle() {
	/*
	* Feature in GTK.  There is no API to query the button
	* handle from a combo box although it is possible to get the
	* text field.  The button handle is needed to hook events.  The
	* fix is to walk the combo tree and find the first child that is 
	* an instance of button.
	*/
	OS.gtk_container_forall (handle, display.allChildrenProc, 0);
	if (display.allChildren != 0) {
		int /*long*/ list = display.allChildren;
		while (list != 0) {
			int /*long*/ widget = OS.g_list_data (list);
			if (OS.GTK_IS_BUTTON (widget)) {
				buttonHandle = widget;
				break;
			}
			list = OS.g_list_next (list);
		}
		OS.g_list_free (display.allChildren);
		display.allChildren = 0;
	}
}

void findMenuHandle() {
	OS.gtk_container_forall (popupHandle, display.allChildrenProc, 0);
	if (display.allChildren != 0) {
	    int /*long*/ list = display.allChildren;
		while (list != 0) {
		int /*long*/ widget = OS.g_list_data (list);
		if (OS.G_OBJECT_TYPE (widget) == OS.GTK_TYPE_MENU ()) {
			menuHandle = widget;
			break;
		}
		list = OS.g_list_next (list);
	}
	    OS.g_list_free (display.allChildren);
	    display.allChildren = 0;
	}
}
	
void fixModal (int /*long*/ group, int /*long*/ modalGroup) {
	if (popupHandle != 0) {
		if (group != 0) {
			OS.gtk_window_group_add_window (group, popupHandle);
		} else {
			if (modalGroup != 0) {
				OS.gtk_window_group_remove_window (modalGroup, popupHandle);
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
	if ((style & SWT.READ_ONLY) != 0 && buttonHandle != 0) return buttonHandle;
	if (entryHandle != 0) return entryHandle;
	return super.focusHandle ();
}

boolean hasFocus () {
	if (super.hasFocus ()) return true;
	if (entryHandle != 0 && OS.GTK_WIDGET_HAS_FOCUS (entryHandle)) return true;
	return false;
}

void hookEvents () {
	super.hookEvents ();
	OS.g_signal_connect_closure (handle, OS.changed, display.closures [CHANGED], true);

	if (entryHandle != 0) {
		OS.g_signal_connect_closure (entryHandle, OS.changed, display.closures [CHANGED], true);
		OS.g_signal_connect_closure (entryHandle, OS.insert_text, display.closures [INSERT_TEXT], false);
		OS.g_signal_connect_closure (entryHandle, OS.delete_text, display.closures [DELETE_TEXT], false);
		OS.g_signal_connect_closure (entryHandle, OS.activate, display.closures [ACTIVATE], false);
		OS.g_signal_connect_closure (entryHandle, OS.populate_popup, display.closures [POPULATE_POPUP], false);
	}
	int eventMask =	OS.GDK_POINTER_MOTION_MASK | OS.GDK_BUTTON_PRESS_MASK | 
		OS.GDK_BUTTON_RELEASE_MASK;
 	int /*long*/ [] handles = new int /*long*/ [] {buttonHandle, entryHandle, menuHandle};
	for (int i=0; i<handles.length; i++) {
		int /*long*/ eventHandle = handles [i];
		if (eventHandle != 0) {
			/* Connect the mouse signals */
			OS.gtk_widget_add_events (eventHandle, eventMask);
			OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [BUTTON_PRESS_EVENT], 0, display.closures [BUTTON_PRESS_EVENT], false);
			OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [BUTTON_RELEASE_EVENT], 0, display.closures [BUTTON_RELEASE_EVENT], false);
			OS.g_signal_connect_closure_by_id (eventHandle, display.signalIds [MOTION_NOTIFY_EVENT], 0, display.closures [MOTION_NOTIFY_EVENT], false);
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
			if (eventHandle != focusHandle ()) {
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
	
	if (menuHandle != 0) OS.g_signal_connect_closure(menuHandle, OS.selection_done, display.closures[SELECTION_DONE], true);
}

int /*long*/ imContext () {
	return entryHandle != 0 ? OS.GTK_ENTRY_IM_CONTEXT (entryHandle) : 0;
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
		if (OS.gtk_combo_box_get_active (handle) == index) {
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
	clearText ();
}

boolean dragDetect(int x, int y, boolean filter, boolean dragOnTimeout, boolean[] consume) {
	if (filter && entryHandle != 0) {
		int [] index = new int [1];
		int [] trailing = new int [1];
		int /*long*/ layout = OS.gtk_entry_get_layout (entryHandle);
		OS.pango_layout_xy_to_index (layout, x * OS.PANGO_SCALE, y * OS.PANGO_SCALE, index, trailing);
		int /*long*/ ptr = OS.pango_layout_get_text (layout);
		int position = (int)/*64*/OS.g_utf8_pointer_to_offset (ptr, ptr + index[0]) + trailing[0];
		int [] start = new int [1];
		int [] end = new int [1];
		OS.gtk_editable_get_selection_bounds (entryHandle, start, end);
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

int /*long*/ enterExitHandle () {
	return fixedHandle;
}

int /*long*/ eventWindow () {
	return paintWindow ();
}

GdkColor getBackgroundColor () {
	return getBaseColor ();
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
	if ((style & SWT.READ_ONLY) != 0) {
		return new Point (0, 0);
	}
	int index = OS.gtk_editable_get_position (entryHandle);
	if (OS.GTK_VERSION >= OS.VERSION (2, 6, 0)) {
		index = OS.gtk_entry_text_index_to_layout_index (entryHandle, index);
	}
	int [] offset_x = new int [1], offset_y = new int [1];
	OS.gtk_entry_get_layout_offsets (entryHandle, offset_x, offset_y);
	int /*long*/ layout = OS.gtk_entry_get_layout (entryHandle);
	PangoRectangle pos = new PangoRectangle ();
	OS.pango_layout_index_to_pos (layout, index, pos);
	int x = offset_x [0] + OS.PANGO_PIXELS (pos.x) - getBorderWidth ();
	int y = offset_y [0] + OS.PANGO_PIXELS (pos.y);
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
	int /*long*/ ptr = OS.gtk_entry_get_text (entryHandle);
	return (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, OS.gtk_editable_get_position (entryHandle));
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
	return fontHeight (getFontDescription (), handle);
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
	return popupHandle != 0 && OS.GTK_WIDGET_VISIBLE (popupHandle); 
}

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
		int index = OS.gtk_combo_box_get_active (handle);
		if (index != -1) length = getItem (index).length ();
		return new Point (0, length);
	}
	int [] start = new int [1];
	int [] end = new int [1];
	if (entryHandle != 0) {
		OS.gtk_editable_get_selection_bounds (entryHandle, start, end);
		int /*long*/ ptr = OS.gtk_entry_get_text (entryHandle);
		start[0] = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, start[0]);
		end[0] = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, end[0]);
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
	return OS.gtk_combo_box_get_active (handle);
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
		int /*long*/ str = OS.gtk_entry_get_text (entryHandle);
		if (str == 0) return "";
		int length = OS.strlen (str);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, str, length);
		return new String (Converter.mbcsToWcs (null, buffer));
	} else {
		int index = OS.gtk_combo_box_get_active (handle);		  
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
	GtkRequisition requisition = new GtkRequisition ();
	gtk_widget_size_request (handle, requisition);
	return OS.GTK_WIDGET_REQUISITION_HEIGHT (handle);
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
	int limit = entryHandle != 0 ? OS.gtk_entry_get_max_length (entryHandle) : 0;
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
	sendSelectionEvent (SWT.DefaultSelection);
	return 0;
}

int /*long*/ gtk_button_press_event (int /*long*/ widget, int /*long*/ event) {
	/*
	* Feature in GTK. Depending on where the user clicks, GTK prevents 
	* the left mouse button event from being propagated. The fix is to
	* send the mouse event from the event_after handler.
	*/
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, event, GdkEventButton.sizeof);
	if (gdkEvent.type == OS.GDK_BUTTON_PRESS && gdkEvent.button == 1) {
		return gtk_button_press_event(widget, event, false);
	}
	return super.gtk_button_press_event (widget, event);
}

int /*long*/ gtk_changed (int /*long*/ widget) {
	if (widget == handle) {
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
		int index = OS.gtk_combo_box_get_active (handle);
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
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;
	int /*long*/ ptr = OS.gtk_entry_get_text (entryHandle);
	if (end_pos == -1) end_pos = OS.g_utf8_strlen (ptr, -1);
	int start = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, start_pos);
	int end = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, end_pos);
	String newText = verifyText ("", start, end);
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

int /*long*/ gtk_event_after (int /*long*/ widget, int /*long*/ gdkEvent)  {
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
	GdkEvent event = new GdkEvent ();
	OS.memmove (event, gdkEvent, GdkEvent.sizeof);
	switch (event.type) {
		case OS.GDK_BUTTON_PRESS: {
			if (OS.GTK_VERSION < OS.VERSION (2, 8, 0) && !selectionAdded) {
				int /*long*/ grabHandle = OS.gtk_grab_get_current ();
				if (grabHandle != 0) {
					if (OS.G_OBJECT_TYPE (grabHandle) == OS.GTK_TYPE_MENU ()) {
						menuHandle = grabHandle;
						OS.g_signal_connect_closure_by_id (menuHandle, display.signalIds [BUTTON_RELEASE_EVENT], 0, display.closures [BUTTON_RELEASE_EVENT], false);
						OS.g_signal_connect_closure_by_id (menuHandle, display.signalIds [BUTTON_RELEASE_EVENT], 0, display.closures [BUTTON_RELEASE_EVENT_INVERSE], true);
						OS.g_signal_connect_closure (menuHandle, OS.selection_done, display.closures [SELECTION_DONE], false);
						display.addWidget (menuHandle, this);
						selectionAdded = true;
					}
				}
			}
			GdkEventButton gdkEventButton = new GdkEventButton ();
			OS.memmove (gdkEventButton, gdkEvent, GdkEventButton.sizeof);
			if (gdkEventButton.button == 1) {
				if (!sendMouseEvent (SWT.MouseDown, gdkEventButton.button, display.clickCount, 0, false, gdkEventButton.time, gdkEventButton.x_root, gdkEventButton.y_root, false, gdkEventButton.state)) {
					return 1;
				}
				if (OS.GTK_VERSION >= OS.VERSION (2, 6, 0)) {
					if ((style & SWT.READ_ONLY) == 0 && widget == buttonHandle) {
						OS.gtk_widget_grab_focus (entryHandle);
					}
				}
			}
			break;
		}
		case OS.GDK_FOCUS_CHANGE: {
			if (OS.GTK_VERSION >= OS.VERSION (2, 6, 0)) {
				if ((style & SWT.READ_ONLY) == 0) {
					GdkEventFocus gdkEventFocus = new GdkEventFocus ();
					OS.memmove (gdkEventFocus, gdkEvent, GdkEventFocus.sizeof);
					if (gdkEventFocus.in != 0) {
						OS.gtk_combo_box_set_focus_on_click (handle, false);
					} else {
						OS.gtk_combo_box_set_focus_on_click (handle, true);
					}
				}
			}
			break;
		}
	}
	return super.gtk_event_after(widget, gdkEvent);
}

int /*long*/ gtk_focus_out_event (int /*long*/ widget, int /*long*/ event) {
	fixIM ();
	return super.gtk_focus_out_event (widget, event);
}

int /*long*/ gtk_insert_text (int /*long*/ widget, int /*long*/ new_text, int /*long*/ new_text_length, int /*long*/ position) {
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return 0;	
	if (new_text == 0 || new_text_length == 0) return 0;
	byte [] buffer = new byte [(int)/*64*/new_text_length];
	OS.memmove (buffer, new_text, buffer.length);
	String oldText = new String (Converter.mbcsToWcs (null, buffer));
	int [] pos = new int [1];
	OS.memmove (pos, position, 4);
	int /*long*/ ptr = OS.gtk_entry_get_text (entryHandle);
	if (pos [0] == -1) pos [0] = (int)/*64*/OS.g_utf8_strlen (ptr, -1);
	int start = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, pos [0]);
	String newText = verifyText (oldText, start, start);
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
			newStart [0] = newEnd [0] = pos [0];
		}
		pos [0] = newEnd [0];
		if (newStart [0] != newEnd [0]) {
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
	if (result != 0) {
	    gdkEventKey = 0;
	    fixIM ();
	    return result;
	}
	if (gdkEventKey == -1) result = 1;
	gdkEventKey = 0;
	if ((style & SWT.READ_ONLY) == 0) {
		GdkEventKey keyEvent = new GdkEventKey ();
		OS.memmove (keyEvent, event, GdkEventKey.sizeof);
		int oldIndex = OS.gtk_combo_box_get_active (handle);
		int newIndex = oldIndex;
		int key = keyEvent.keyval;
		switch (key) {
			case OS.GDK_Down:
			case OS.GDK_KP_Down: 
				 if (oldIndex != (items.length - 1)) {
					newIndex = oldIndex + 1;
				 }
				 break;
			case OS.GDK_Up:
			case OS.GDK_KP_Up: 
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
			case OS.GDK_Page_Up:
		    case OS.GDK_KP_Page_Up:
		    	newIndex = 0;
		    	break;
		    case OS.GDK_Page_Down:
		    case OS.GDK_KP_Page_Down:
		    	newIndex = items.length - 1;
		    	break;  
		}
		if (newIndex != oldIndex) {
			OS.gtk_combo_box_set_active (handle, newIndex);
			return 1;
		}
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

int /*long*/ gtk_selection_done(int /*long*/ menushell) {
	int index = OS.gtk_combo_box_get_active (handle);
	if (indexSelected == -1){
		indexSelected = index;
	}
	else if (index != -1 && indexSelected == index) {
		sendSelectionEvent (SWT.Selection);
	}
	return 0;
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

boolean isFocusHandle(int widget) {
	if (buttonHandle != 0 && widget == buttonHandle) return true;
	if (entryHandle != 0 && widget == entryHandle) return true;
	return super.isFocusHandle (widget);
}

int /*long*/ paintWindow () {
	int /*long*/ childHandle =  entryHandle != 0 ? entryHandle : handle;	
	OS.gtk_widget_realize (childHandle);
	int /*long*/ window = OS.GTK_WIDGET_WINDOW (childHandle);
	if ((style & SWT.READ_ONLY) != 0) return window;
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
 * 
 * @since 2.1
 */
public void paste () {
	checkWidget ();
	if (entryHandle != 0) OS.gtk_editable_paste_clipboard (entryHandle);
}

int /*long*/ parentingHandle() {
	return fixedHandle;
}

void register () {
	super.register ();
	if (buttonHandle != 0) display.addWidget (buttonHandle, this);
	if (entryHandle != 0) display.addWidget (entryHandle, this);
	if (popupHandle != 0) display.addWidget (popupHandle, this);
	if (menuHandle != 0) display.addWidget (menuHandle, this);
	int /*long*/ imContext = imContext ();
	if (imContext != 0) display.addWidget (imContext, this);
}

void releaseHandle () {
	super.releaseHandle ();
	buttonHandle = entryHandle = 0;
}

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
	if (OS.gtk_combo_box_get_active (handle) == index) clearText ();
	OS.gtk_combo_box_remove_text (handle, index);
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
	int index = OS.gtk_combo_box_get_active (handle);
	if (start <= index && index <= end) clearText();
	for (int i = end; i >= start; i--) {
		OS.gtk_combo_box_remove_text (handle, i);
	}
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
	int count = items.length;
	items = new String[0];
	clearText ();
	for (int i = count - 1; i >= 0; i--) {
		OS.gtk_combo_box_remove_text (handle, i);
	}
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
	int selected = OS.gtk_combo_box_get_active (handle);
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.gtk_combo_box_set_active (handle, index);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if ((style & SWT.READ_ONLY) != 0 && selected != index) {
		/*
		* Feature in GTK. Read Only combo boxes do not get a chance to send out a 
		* Modify event in the gtk_changed callback. The fix is to send a Modify event 
		* here.
		*/
		sendEvent (SWT.Modify);
	}
}

void setBackgroundColor (GdkColor color) {
	super.setBackgroundColor (color);
	if (entryHandle != 0) OS.gtk_widget_modify_base (entryHandle, 0, color);
	if (cellHandle != 0) OS.g_object_set (cellHandle, OS.background_gdk, color, 0);
	OS.g_object_set (textRenderer, OS.background_gdk, color, 0);
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	int newHeight = height;
	if (resize) newHeight = Math.max (getTextHeight (), height);
	return super.setBounds (x, y, width, newHeight, move, resize);
}

void setFontDescription (int /*long*/ font) {
	super.setFontDescription (font);
	if (entryHandle != 0) OS.gtk_widget_modify_font (entryHandle, font);
	OS.g_object_set (textRenderer, OS.font_desc, font, 0);
	if ((style & SWT.READ_ONLY) != 0) {
		/*
		* Bug in GTK.  Setting the font can leave the combo box with an
		* invalid minimum size.  The fix is to temporarily change the
		* selected item to force the combo box to resize.
		*/
		int index = OS.gtk_combo_box_get_active (handle);
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.gtk_combo_box_set_active (handle, -1);
		OS.gtk_combo_box_set_active (handle, index);
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	}
}

void setForegroundColor (GdkColor color) {
	super.setForegroundColor (color);
	if (entryHandle != 0) setForegroundColor (entryHandle, color);
	OS.g_object_set (textRenderer, OS.foreground_gdk, color, 0);
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
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	OS.gtk_combo_box_remove_text (handle, index);
	OS.gtk_combo_box_insert_text (handle, index, buffer);
	if ((style & SWT.RIGHT_TO_LEFT) != 0 && popupHandle != 0) {
		OS.gtk_container_forall (popupHandle, display.setDirectionProc, OS.GTK_TEXT_DIR_RTL);    
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
public void setItems (String [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<items.length; i++) {
		if (items [i] == null) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = this.items.length;
	this.items = new String [items.length];
	System.arraycopy (items, 0, this.items, 0, items.length);
	clearText ();
	for (int i = count - 1; i >= 0; i--) {
		OS.gtk_combo_box_remove_text (handle, i);
	}
	for (int i = 0; i < items.length; i++) {
		String string = items [i];
		byte [] buffer = Converter.wcsToMbcs (null, string, true);
		OS.gtk_combo_box_insert_text (handle, i, buffer);
		if ((style & SWT.RIGHT_TO_LEFT) != 0 && popupHandle != 0) {
			OS.gtk_container_forall (popupHandle, display.setDirectionProc, OS.GTK_TEXT_DIR_RTL);    
		}
	}
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
		OS.gtk_combo_box_popup (handle);
	} else {
		OS.gtk_combo_box_popdown (handle);
	}
}

void setOrientation (boolean create) {
	super.setOrientation (create);
	if ((style & SWT.RIGHT_TO_LEFT) != 0 || !create) {
		int dir = (style & SWT.RIGHT_TO_LEFT) != 0 ? OS.GTK_TEXT_DIR_RTL : OS.GTK_TEXT_DIR_LTR;
		if (entryHandle != 0) OS.gtk_widget_set_direction (entryHandle, dir);
		if (cellHandle != 0) OS.gtk_widget_set_direction (cellHandle, dir);
		if (!create) {
			if (popupHandle != 0) OS.gtk_container_forall (popupHandle, display.setDirectionProc, dir);
		}
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
		int /*long*/ ptr = OS.gtk_entry_get_text (entryHandle);
		int start = (int)/*64*/OS.g_utf16_offset_to_utf8_offset (ptr, selection.x);
		int end = (int)/*64*/OS.g_utf16_offset_to_utf8_offset (ptr, selection.y);
		OS.gtk_editable_set_position (entryHandle, start);
		OS.gtk_editable_select_region (entryHandle, start, end);
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
		int /*long*/ ptr = OS.gtk_entry_get_text (entryHandle);
		string = verifyText (string, 0, (int)/*64*/OS.g_utf16_strlen (ptr, -1));
		if (string == null) return;
	}
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, DELETE_TEXT);
	OS.g_signal_handlers_block_matched (entryHandle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, INSERT_TEXT);
	OS.gtk_entry_set_text (entryHandle, buffer);
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
	if (entryHandle != 0) OS.gtk_entry_set_max_length (entryHandle, limit);
}

void setToolTipText (Shell shell, String newString) {
	if (entryHandle != 0) shell.setToolTipText (entryHandle, newString);
	if (buttonHandle != 0) shell.setToolTipText (buttonHandle, newString);
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

boolean checkSubwindow () {
	return false;
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
