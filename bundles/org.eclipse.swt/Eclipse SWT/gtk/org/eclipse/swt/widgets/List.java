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
 * Instances of this class represent a selectable user interface
 * object that displays a list of strings and issues notificiation
 * when a string selected.  A list may be single or multi select.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection</dd>
 * </dl>
 * <p>
 * Note: Only one of SINGLE and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class List extends Scrollable {
	int modelHandle;

	static final int TEXT_COLUMN = 0;

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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public List (Composite parent, int style) {
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
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
 * </ul>
 *
 * @see #add(String,int)
 */
public void add (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	if (iter == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	OS.gtk_list_store_append (modelHandle, iter);
	OS.gtk_list_store_set (modelHandle, iter, TEXT_COLUMN, buffer, -1);
	OS.g_free (iter);
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
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
 * </ul>
 *
 * @see #add(String)
 */
public void add (String string, int index) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = OS.gtk_tree_model_iter_n_children (modelHandle, 0);
	if (!(0 <= index && index <= count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	if (iter == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	if (index == count) {
		OS.gtk_list_store_append (modelHandle, iter);
	} else {
		OS.gtk_list_store_insert (modelHandle, iter, index);
	}
	OS.gtk_list_store_set (modelHandle, iter, TEXT_COLUMN, buffer, -1);
	OS.g_free (iter);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the selection changes.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
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

static int checkStyle (int style) {
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}

void createHandle (int index) {
	state |= HANDLE;
	fixedHandle = OS.gtk_fixed_new ();
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	scrolledHandle = OS.gtk_scrolled_window_new (0, 0);
	if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
	/*
	* Columns:
	* 0 - text
	*/
	int [] types = new int [] {OS.G_TYPE_STRING ()};
	modelHandle = OS.gtk_list_store_newv (types.length, types);
	if (modelHandle == 0) error (SWT.ERROR_NO_HANDLES);
	handle = OS.gtk_tree_view_new_with_model (modelHandle);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	int textRenderer = OS.gtk_cell_renderer_text_new ();
	if (textRenderer == 0) error (SWT.ERROR_NO_HANDLES);
	int columnHandle = OS.gtk_tree_view_column_new ();
	if (columnHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_tree_view_column_pack_start (columnHandle, textRenderer, true);
	OS.gtk_tree_view_column_add_attribute (columnHandle, textRenderer, "text", TEXT_COLUMN);
	OS.gtk_tree_view_insert_column (handle, columnHandle, index);
	int parentHandle = parent.parentingHandle ();
	OS.gtk_container_add (parentHandle, fixedHandle);
	OS.gtk_container_add (fixedHandle, scrolledHandle);
	OS.gtk_container_add (scrolledHandle, handle);
	OS.gtk_widget_show (fixedHandle);
	OS.gtk_widget_show (scrolledHandle);
	OS.gtk_widget_show (handle);
	
	int mode = (style & SWT.MULTI) != 0 ? OS.GTK_SELECTION_MULTIPLE : OS.GTK_SELECTION_BROWSE;
	int selectionHandle = OS.gtk_tree_view_get_selection (handle);
	OS.gtk_tree_selection_set_mode (selectionHandle, mode);
	OS.gtk_tree_view_set_headers_visible (handle, false);	
	int hsp = (style & SWT.H_SCROLL) != 0 ? OS.GTK_POLICY_AUTOMATIC : OS.GTK_POLICY_NEVER;
	int vsp = (style & SWT.V_SCROLL) != 0 ? OS.GTK_POLICY_AUTOMATIC : OS.GTK_POLICY_NEVER;
	OS.gtk_scrolled_window_set_policy (scrolledHandle, hsp, vsp);
	if ((style & SWT.BORDER) != 0) OS.gtk_scrolled_window_set_shadow_type (scrolledHandle, OS.GTK_SHADOW_ETCHED_IN);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	Point size = computeNativeSize (handle, wHint, hHint, changed);
	Rectangle trim = computeTrim (0, 0, size.x, size.y);
	size.x = trim.width;
	size.y = trim.height;
	return size;
}

void deregister() {
	super.deregister ();
	display.removeWidget (OS.gtk_tree_view_get_selection (handle));
}

/**
 * Deselects the item at the given zero-relative index in the receiver.
 * If the item at the index was already deselected, it remains
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
	if (!(0 <= index && index < OS.gtk_tree_model_iter_n_children (modelHandle, 0)))  return;
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	int selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index); 
	OS.gtk_tree_selection_unselect_iter (selection, iter);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_free (iter);
}

/**
 * Deselects the items at the given zero-relative indices in the receiver.
 * If the item at the given zero-relative index in the receiver 
 * is selected, it is deselected.  If the item at the index
 * was not selected, it remains deselected.  The range of the
 * indices is inclusive. Indices that are out of range are ignored.
 *
 * @param start the start index of the items to deselect
 * @param end the end index of the items to deselect
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int start, int end) {
	checkWidget();
	if (start < 0 && end < 0) return;
	int count = OS.gtk_tree_model_iter_n_children (modelHandle, 0);
	if (start >= count && end >= count) return;
	start = Math.min (count - 1, Math.max (0, start));
	end = Math.min (count - 1, Math.max (0, end));
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	int selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	for (int index=start; index<=end; index++) {
		OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index); 
		OS.gtk_tree_selection_unselect_iter (selection, iter);
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_free (iter);
}

/**
 * Deselects the items at the given zero-relative indices in the receiver.
 * If the item at the given zero-relative index in the receiver 
 * is selected, it is deselected.  If the item at the index
 * was not selected, it remains deselected. Indices that are out
 * of range and duplicate indices are ignored.
 *
 * @param indices the array of indices for the items to deselect
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the set of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int [] indices) {
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	int count = OS.gtk_tree_model_iter_n_children (modelHandle, 0);
	int selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	for (int i=0; i<indices.length; i++) {
		int index = indices [i];
		if (index < 0 || index > count - 1) continue;
		OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index); 
		OS.gtk_tree_selection_unselect_iter (selection, iter);
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_free (iter);
}

/**
 * Deselects all selected items in the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselectAll () {
	checkWidget();
	int selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.gtk_tree_selection_unselect_all (selection);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
}

GdkColor getBackgroundColor () {
	return getBaseColor ();
}

/**
 * Returns the zero-relative index of the item which is currently
 * has the focus in the receiver, or -1 if no item is has focus.
 *
 * @return the index of the selected item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getFocusIndex () {
	checkWidget();
	int [] path = new int [1];
	OS.gtk_tree_view_get_cursor (handle, path, null);
	if (path [0] == 0) return -1;
	int indices = OS.gtk_tree_path_get_indices (path [0]);
	int [] index = new int []{-1};
	if (indices != 0) OS.memmove (index, indices, 4);
	OS.gtk_tree_path_free (path [0]);
	return index [0];
}

GdkColor getForegroundColor () {
	return getTextColor ();
}

/**
 * Returns the item at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public String getItem (int index) {
	checkWidget();
	if (!(0 <= index && index < OS.gtk_tree_model_iter_n_children (modelHandle, 0)))  {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int [] ptr = new int [1];
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
	OS.gtk_tree_model_get (modelHandle, iter, 0, ptr, -1);
	OS.g_free (iter);
	if (ptr [0] == 0) return null;
	int length = OS.strlen (ptr [0]);
	byte[] buffer2 = new byte [length];
	OS.memmove (buffer2, ptr [0], length);
	OS.g_free (ptr [0]);
	return new String (Converter.mbcsToWcs (null, buffer2));
}

/**
 * Returns the number of items contained in the receiver.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget();
	return OS.gtk_tree_model_iter_n_children (modelHandle, 0);
}

/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the tree.
 *
 * @return the height of one item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM_HEIGHT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getItemHeight () {
	checkWidget();
	if ( OS.gtk_tree_model_iter_n_children (modelHandle, 0) == 0) {
		int column = OS.gtk_tree_view_get_column (handle, 0);
		int list = OS.gtk_tree_view_column_get_cell_renderers (column);
		int renderer = OS.g_list_nth_data (list, 0);
		OS.g_list_free (list);
		int [] h = new int [1];
		OS.gtk_cell_renderer_get_size (renderer, handle, null, null, null, null, h);
		return h [0];
	}
	GdkRectangle rect = new GdkRectangle ();
	int path = OS.gtk_tree_path_new_first ();
	OS.gtk_widget_realize (handle);
	OS.gtk_tree_view_get_cell_area (handle, path, 0, rect);
	OS.gtk_tree_path_free (path);
	return rect.height;
}

/**
 * Returns an array of <code>String</code>s which are the items
 * in the receiver. 
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure while getting an item</li>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure while getting the item count</li>
 * </ul>
 */
public String [] getItems () {
	checkWidget();
	int count = OS.gtk_tree_model_iter_n_children (modelHandle, 0);
	int [] ptr = new int [1];
	String [] result = new String [count];
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	for (int index=0; index<count; index++) {
		OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
		OS.gtk_tree_model_get (modelHandle, iter, 0, ptr, -1);
		if (ptr [0] != 0) {
			int length = OS.strlen (ptr [0]);
			byte[] buffer = new byte [length];
			OS.memmove (buffer, ptr [0], length);
			OS.g_free (ptr [0]);
			result [index] = new String (Converter.mbcsToWcs (null, buffer));
		}
	}
	OS.g_free (iter);
	return result;
}

/**
 * Returns an array of <code>String</code>s that are currently
 * selected in the receiver. An empty array indicates that no
 * items are selected.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its selection, so modifying the array will
 * not affect the receiver. 
 * </p>
 * @return an array representing the selection
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_SELECTION - if the operation fails because of an operating system failure while getting the selection</li>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure while getting an item</li>
 * </ul>
 */
public String [] getSelection () {
	checkWidget();
	int [] indices = getSelectionIndices ();
	String [] result = new String [indices.length];
	for (int i=0; i<indices.length; i++) {
		result [i] = getItem (indices [i]);
	}
	return result;
}

/**
 * Returns the number of selected items contained in the receiver.
 *
 * @return the number of selected items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getSelectionCount () {
	checkWidget();
	display.treeSelectionLength = 0;
	display.treeSelection = null;
	int selection = OS.gtk_tree_view_get_selection (handle);
	OS.gtk_tree_selection_selected_foreach (selection, display.treeSelectionProc, handle);
	return display.treeSelectionLength;
}

/**
 * Returns the zero-relative index of the item which is currently
 * selected in the receiver, or -1 if no item is selected.
 *
 * @return the index of the selected item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_SELECTION - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getSelectionIndex () {
	checkWidget();
	int itemCount = OS.gtk_tree_model_iter_n_children (modelHandle, 0);
	display.treeSelectionLength  = 0;
	display.treeSelection = new int [itemCount];
	int selection = OS.gtk_tree_view_get_selection (handle);
	OS.gtk_tree_selection_selected_foreach (selection, display.treeSelectionProc, handle);
	if (display.treeSelectionLength == 0) return -1;
	return display.treeSelection [0];
}

/**
 * Returns the zero-relative indices of the items which are currently
 * selected in the receiver.  The array is empty if no items are selected.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its selection, so modifying the array will
 * not affect the receiver. 
 * </p>
 * @return the array of indices of the selected items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_SELECTION - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int [] getSelectionIndices () {
	checkWidget();
	int itemCount = OS.gtk_tree_model_iter_n_children (modelHandle, 0);
	display.treeSelectionLength  = 0;
	display.treeSelection = new int [itemCount];
	int selection = OS.gtk_tree_view_get_selection (handle);
	OS.gtk_tree_selection_selected_foreach (selection, display.treeSelectionProc, handle);
	if (display.treeSelectionLength == display.treeSelection.length) return display.treeSelection;
	int [] result = new int [display.treeSelectionLength];
	System.arraycopy (display.treeSelection, 0, result, 0, display.treeSelectionLength);
	return result;
}

/**
 * Returns the zero-relative index of the item which is currently
 * at the top of the receiver. This index can change when items are
 * scrolled or new items are added or removed.
 *
 * @return the index of the top item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopIndex () {
	checkWidget();
	int [] path = new int [1];
	OS.gtk_widget_realize (handle);
	if (!OS.gtk_tree_view_get_path_at_pos (handle, 1, 1, path, null, null, null)) return 0;
	if (path [0] == 0) return 0;
	int indices = OS.gtk_tree_path_get_indices (path[0]);
	int[] index = new int [1];
	if (indices != 0) OS.memmove (index, indices, 4);
	OS.gtk_tree_path_free (path [0]);
	return index [0];
}

int gtk_changed (int widget) {
	postEvent (SWT.Selection);
	return 0;
}

int gtk_button_press_event (int widget, int event) {
	int result = super.gtk_button_press_event (widget, event);
	if (result != 0) return result;
	/*
	* Feature in GTK.  In a multi-select list view, when multiple items are already
	* selected, the selection state of the item is toggled and the previous selection 
	* is cleared. This is not the desired behaviour when bringing up a popup menu.
	* Also, when an item is reselected with the right button, the tree view issues
	* an unwanted selection event. The workaround is to detect that case and not
	* run the default handler when the item is already part of the current selection.
	*/
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, event, GdkEventButton.sizeof);
	int button = gdkEvent.button;
	if (button == 3 && gdkEvent.type == OS.GDK_BUTTON_PRESS) {
		int [] path = new int [1];
		if (OS.gtk_tree_view_get_path_at_pos (handle, (int)gdkEvent.x, (int)gdkEvent.y, path, null, null, null)) {
			if (path [0] != 0) {
				int selection = OS.gtk_tree_view_get_selection (handle);
				if (OS.gtk_tree_selection_path_is_selected (selection, path [0])) result = 1;
				OS.gtk_tree_path_free (path [0]);
			}
		}
	}
	
	/*
	* Feature in GTK.  When the user clicks in a single selection GtkTreeView
	* and there are no selected items, the first item is selected automatically
	* before the click is processed, causing two selection events.  The is fix
	* is the set the cursor item to be same as the clicked item to stop the
	* widget from automatically selecting the first item.
	*/
	if ((style & SWT.SINGLE) != 0 && getSelectionCount () == 0) {
		int [] path = new int [1];
		if (OS.gtk_tree_view_get_path_at_pos (handle, (int)gdkEvent.x, (int)gdkEvent.y, path, null, null, null)) {
			if (path [0] != 0) {
				int selection = OS.gtk_tree_view_get_selection (handle);
				OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				OS.gtk_tree_view_set_cursor (handle, path [0], 0, false);
				OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				OS.gtk_tree_path_free (path [0]);
			}
		}
	}
	return result;
}

int gtk_key_press_event (int widget, int event) {
	int result = super.gtk_key_press_event (widget, event);
	if (result != 0) return result;

	/*
	* Feature in GTK.  When an item is default selected using
	* the return key, GTK does not issue notification. The fix is
	* to issue this notification when the return key is pressed.
	*/
	GdkEventKey keyEvent = new GdkEventKey ();
	OS.memmove (keyEvent, event, GdkEventKey.sizeof);
	int key = keyEvent.keyval;
	switch (key) {
		case OS.GDK_Return:
		case OS.GDK_KP_Enter: {
			postEvent (SWT.DefaultSelection);
			break;
		}
	}
	
	return result;
}

int gtk_row_activated (int tree, int path, int column) {
	postEvent (SWT.DefaultSelection);
	return 0;
}

void hookEvents () {
	super.hookEvents();
	int selection = OS.gtk_tree_view_get_selection(handle);
	OS.g_signal_connect (selection, OS.changed, display.windowProc2, CHANGED);
	OS.g_signal_connect (handle, OS.row_activated, display.windowProc4, ROW_ACTIVATED);
}

/**
 * Gets the index of an item.
 * <p>
 * The list is searched starting at 0 until an
 * item is found that is equal to the search item.
 * If no item is found, -1 is returned.  Indexing
 * is zero based.
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
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure while getting the item count</li>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure while getting an item</li>
 * </ul>
 */
public int indexOf (String string, int start) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	String [] items = getItems ();
	for (int i=start; i<items.length; i++) {
		if (items [i].equals (string)) return i;
	}
	return -1;
}

/**
 * Returns <code>true</code> if the item is selected,
 * and <code>false</code> otherwise.  Indices out of
 * range are ignored.
 *
 * @param index the index of the item
 * @return the visibility state of the item at the index
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isSelected (int index) {
	checkWidget();
	int selection = OS.gtk_tree_view_get_selection (handle);
	byte [] buffer = Converter.wcsToMbcs (null, Integer.toString (index), true);
	int path = OS.gtk_tree_path_new_from_string (buffer);
	boolean answer = OS.gtk_tree_selection_path_is_selected (selection, path);
	OS.gtk_tree_path_free (path);
	return answer;
}

int paintWindow () {
	OS.gtk_widget_realize (handle);
	return OS.gtk_tree_view_get_bin_window (handle);
}

void register () {
	super.register ();
	display.addWidget (OS.gtk_tree_view_get_selection (handle), this);
}

void releaseWidget () {
	super.releaseWidget ();
	if (modelHandle != 0) OS.g_object_unref (modelHandle);
	modelHandle = 0;
}

/**
 * Removes the item from the receiver at the given
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
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void remove (int index) {
	checkWidget();
	if (!(0 <= index && index < OS.gtk_tree_model_iter_n_children (modelHandle, 0)))  {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
	int selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.gtk_list_store_remove (modelHandle, iter);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_free (iter);
}

/**
 * Removes the items from the receiver which are
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
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void remove (int start, int end) {
	checkWidget();
	if (start > end) return;
	int count =  OS.gtk_tree_model_iter_n_children (modelHandle, 0);
	if (!(0 <= start && start <= end && end < count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	int selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	for (int index=end; index>=start; index--) {
		OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
		OS.gtk_list_store_remove (modelHandle, iter);
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_free (iter);
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
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void remove (String string) {
	checkWidget();
	int index = indexOf (string, 0);
	if (index == -1) error (SWT.ERROR_INVALID_ARGUMENT);
	remove (index);
}

/**
 * Removes the items from the receiver at the given
 * zero-relative indices.
 *
 * @param indices the array of indices of the items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 *    <li>ERROR_NULL_ARGUMENT - if the indices array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void remove (int [] indices) {
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (indices.length == 0) return;
	int [] newIndices = new int [indices.length];
	System.arraycopy (indices, 0, newIndices, 0, indices.length);
	sort (newIndices);
	int start = newIndices [newIndices.length - 1], end = newIndices [0];
	int count = getItemCount();
	if (!(0 <= start && start <= end && end < count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	int selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	int last = -1;
	for (int i=0; i<newIndices.length; i++) {
		int index = newIndices [i];
		if (index != last) {
			OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
			OS.gtk_list_store_remove (modelHandle, iter);
			last = index;
		}
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_free (iter);
}

/**
 * Removes all of the items from the receiver.
 * <p>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget();
	int selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.gtk_list_store_clear (modelHandle);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
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
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
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
	if (!(0 <= index && index < OS.gtk_tree_model_iter_n_children (modelHandle, 0)))  return;
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	int selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
	OS.gtk_tree_selection_select_iter (selection, iter); 
	if ((style & SWT.SINGLE) != 0) {
		int path = OS.gtk_tree_model_get_path (modelHandle, iter);
		OS.gtk_tree_view_set_cursor (handle, path, 0, false);
		OS.gtk_tree_path_free (path);
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_free (iter);
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * If the item at the index was already selected, it remains
 * selected. The range of the indices is inclusive. Indices that are
 * out of range are ignored and no items will be selected if start is
 * greater than end.
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int start, int end) {
	checkWidget();
	if (!(0 <= end && start <= end)) return;
	int count = OS.gtk_tree_model_iter_n_children (modelHandle, 0);
	if (count <= start || count == 0) return;
	start = Math.min (count - 1, Math.max (0, start));
	end = Math.min (count - 1, Math.max (0, end));
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	int selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	for (int index=start; index<=end; index++) {
		OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
		OS.gtk_tree_selection_select_iter (selection, iter); 
		if ((style & SWT.SINGLE) != 0) {
			int path = OS.gtk_tree_model_get_path (modelHandle, iter);
			OS.gtk_tree_view_set_cursor (handle, path, 0, false);
			OS.gtk_tree_path_free (path);
		}
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_free (iter);
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * If the item at the given zero-relative index in the receiver 
 * is not selected, it is selected.  If the item at the index
 * was selected, it remains selected. Indices that are out
 * of range and duplicate indices are ignored.
 *
 * @param indices the array of indices for the items to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the set of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int [] indices) {
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	int count = OS.gtk_tree_model_iter_n_children (modelHandle, 0);
	int selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	for (int i=0; i<indices.length; i++) {
		int index = indices [i];
		if (index < 0 || index > count - 1) continue;
		OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
		OS.gtk_tree_selection_select_iter (selection, iter); 
		if ((style & SWT.SINGLE) != 0) {
			int path = OS.gtk_tree_model_get_path (modelHandle, iter);
			OS.gtk_tree_view_set_cursor (handle, path, 0, false);
			OS.gtk_tree_path_free (path);
			break;
		}
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_free (iter);
}

/**
 * Selects all the items in the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void selectAll () {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return;
	int selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.gtk_tree_selection_select_all (selection);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
}

void selectFocusIndex (int index) {
	/*
	 * Note that this method both selects and sets the focus to the
	 * specified index, so any previous selection in the list will be lost.
	 * gtk does not provide a way to just set focus to a specified list item.
	 */
	int count = OS.gtk_tree_model_iter_n_children (modelHandle, 0);
	if (!(0 <= index && index < count))  return;
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
	int path = OS.gtk_tree_model_get_path (modelHandle, iter);
	OS.gtk_tree_view_set_cursor (handle, path, 0, false);
	OS.gtk_tree_path_free (path);
	OS.g_free (iter);
}

void setBackgroundColor (GdkColor color) {
	super.setBackgroundColor (color);
	OS.gtk_widget_modify_base (handle, 0, color);
}

boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	boolean result = super.setBounds (x, y, width, height, move, resize);
	/*
	* Bug on GTK.  The tree view sometimes does not get a paint
	* event or resizes to a one pixel square when resized in a new
	* shell that is not visible after any event loop has been run.  The
	* problem is intermittent. It doesn't seem to happen the first time
	* a new shell is created. The fix is to ensure the tree view is realized
	* after it has been resized.
	*/
	OS.gtk_widget_realize (handle);
	return result;
}

void setForegroundColor (GdkColor color) {
	super.setForegroundColor (color);
	OS.gtk_widget_modify_text (handle, 0, color);
}

/**
 * Sets the text of the item in the receiver's list at the given
 * zero-relative index to the string argument. This is equivalent
 * to <code>remove</code>'ing the old item at the index, and then
 * <code>add</code>'ing the new item at that index.
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
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the remove operation fails because of an operating system failure</li>
 *    <li>ERROR_ITEM_NOT_ADDED - if the add operation fails because of an operating system failure</li>
 * </ul>
 */
public void setItem (int index, String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (!(0 <= index && index < OS.gtk_tree_model_iter_n_children (modelHandle, 0)))  {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	OS.gtk_list_store_set (modelHandle, iter, TEXT_COLUMN, buffer, -1);
	OS.g_free (iter);
}

/**
 * Sets the receiver's items to be the given array of items.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void setItems (String [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	int selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.gtk_list_store_clear (modelHandle);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	if (iter == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	for (int i=0; i<items.length; i++) {
		String string = items [i];
		if (string == null) {
			OS.g_free (iter);
			error (SWT.ERROR_ITEM_NOT_ADDED);
		}
		byte [] buffer = Converter.wcsToMbcs (null, string, true);
		OS.gtk_list_store_append (modelHandle, iter);
		OS.gtk_list_store_set (modelHandle, iter, TEXT_COLUMN, buffer, -1);
	}
	OS.g_free (iter);
}

/**
 * Selects the item at the given zero-relative index in the receiver. 
 * If the item at the index was already selected, it remains selected.
 * The current selected is first cleared, then the new items are selected.
 * Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @see List#deselectAll()
 * @see List#select(int)
 */
public void setSelection (int index) {
	checkWidget();
	if ((style & SWT.MULTI) != 0) {
		deselectAll ();
		if (index >= 0) selectFocusIndex (index);
	} else {
		select (index);
	}
	showSelection ();
}

/**
 * Selects the items at the given zero-relative indices in the receiver. 
 * The current selection is first cleared, then the new items are selected.
 * Indices that are out of range are ignored and no items will be selected
 * if start is greater than end.
 *
 * @param start the start index of the items to select
 * @param end the end index of the items to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int,int)
 */
public void setSelection (int start, int end) {
	checkWidget();
	if ((style & SWT.MULTI) != 0) {
		deselectAll ();
		if (0 <= end && start <= end) {
			int focusIndex = Math.max (0, start);
			selectFocusIndex (focusIndex);
			start = focusIndex + 1;
		}
	}
	select (start, end);
	showSelection ();
}

/**
 * Selects the items at the given zero-relative indices in the receiver. 
 * The current selection is first cleared, then the new items are selected.
 *
 * @param indices the indices of the items to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the set of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see List#deselectAll()
 * @see List#select(int[])
 */
public void setSelection(int[] indices) {
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	deselectAll ();
	if ((style & SWT.MULTI) != 0) {
		if (indices.length > 0) {
			int focusIndex = indices [0];
			if (focusIndex >= 0) {
				selectFocusIndex (focusIndex);
				int [] temp = indices;
				indices = new int [indices.length - 1];
				System.arraycopy (temp, 1, indices, 0, indices.length);
			}
		}
	}
	select (indices);
	showSelection ();
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selected is first cleared, then the new items are
 * selected.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the set of items is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see List#deselectAll()
 * @see List#select(int)
 */
public void setSelection (String [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.MULTI) != 0) deselectAll ();
	boolean firstSelect = true;
	int singleSelectIndex = -1;
	for (int i = 0; i < items.length; i++) {
		int index = 0;
		String string = items [i];
		if (string != null) {
			while ((index = indexOf (string, index)) != -1) {
				if ((style & SWT.MULTI) != 0) {
					if (firstSelect) {
						firstSelect = false;
						selectFocusIndex (index);
					} else {
						select (index);
					}
				} else {	/* SINGLE */
					singleSelectIndex = index;
					break;
				}
				index++;
			}
		}
	}
	if ((style & SWT.SINGLE) != 0) {
		if (singleSelectIndex == -1) {
			deselectAll ();
		} else {
			select (singleSelectIndex);
		}
	}
	showSelection ();
}

/**
 * Sets the zero-relative index of the item which is currently
 * at the top of the receiver. This index can change when items
 * are scrolled or new items are added and removed.
 *
 * @param index the index of the top item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTopIndex (int index) {
	checkWidget();
	if (!(0 <= index && index < OS.gtk_tree_model_iter_n_children (modelHandle, 0))) return;
	// FIXME - For some reason, sometimes the tree scrolls to the wrong place
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
	int path = OS.gtk_tree_model_get_path (modelHandle, iter);
	OS.gtk_tree_view_scroll_to_cell (handle, path, 0, true, 0, 0);
	OS.gtk_tree_path_free (path);
	OS.g_free (iter);
}

/**
 * Shows the selection.  If the selection is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled until
 * the selection is visible.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void showSelection () {
	checkWidget();
	int index = getSelectionIndex ();
	if (index == -1) return;
	int iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
	int path = OS.gtk_tree_model_get_path (modelHandle, iter);
	OS.gtk_tree_view_scroll_to_cell (handle, path, 0, true, 0, 0);
	OS.gtk_tree_path_free (path);
	OS.g_free (iter);
}

int treeSelectionProc (int model, int path, int iter, int[] selection, int length) {
	if (selection != null) { 
		int indices = OS.gtk_tree_path_get_indices (path);
		if (indices != 0) {
			int [] index = new int [1];
			OS.memmove (index, indices, 4);
			selection [length] = index [0];
		}
	}
	return 0;
}

}
