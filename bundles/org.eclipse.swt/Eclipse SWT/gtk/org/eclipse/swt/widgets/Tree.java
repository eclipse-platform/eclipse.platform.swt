package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class provide a selectable user interface object
 * that displays a hierarchy of items and issue notificiation when an
 * item in the hierarchy is selected.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TreeItem</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, Collapse, Expand</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles SINGLE and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Tree extends Composite {
	int selSize; int[] selIndices;
	TreeItem[] items;
	int modelHandle, columnHandle;	
	
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
 * @see SWT#CHECK
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Tree (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	style |= SWT.H_SCROLL | SWT.V_SCROLL;
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * If the reciever has <code>SWT.CHECK</code> style set and the check selection changes,
 * the event object detail field contains the value <code>SWT.CHECK</code>.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
 * The item field of the event object is valid for default selection, but the detail field is not used.
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
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when an item in the receiver is expanded or collapsed
 * by sending it one of the messages defined in the <code>TreeListener</code>
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
 * @see TreeListener
 * @see #removeTreeListener
 */
public void addTreeListener(TreeListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Expand, typedListener);
	addListener (SWT.Collapse, typedListener);
}  

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint == SWT.DEFAULT) wHint = 200;
	return computeNativeSize (scrolledHandle, wHint, hHint, changed);
}

void createHandle (int index) {
	state |= HANDLE;
	fixedHandle = OS.gtk_fixed_new ();
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	scrolledHandle = OS.gtk_scrolled_window_new (0, 0);
	if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);

	/* Columns:
	 * 0 - text
	 * 1 - pixmap
	 * 2 - foreground
	 * 3 - background
	 * 4 - id
	 * 5 - checked (if needed)
	 */
	int COLUMN_NUM = (style&SWT.CHECK)!=0? 6:5;
	int[] types = new int[COLUMN_NUM];
	types[0] = OS.G_TYPE_STRING();
	types[1] = OS.gdk_pixbuf_get_type();
	types[2] = OS.GDK_TYPE_COLOR();
	types[3] = OS.GDK_TYPE_COLOR();
	types[4] = OS.G_TYPE_INT();
	if ((style&SWT.CHECK) != 0) types[5] = OS.G_TYPE_BOOLEAN();
	modelHandle = OS.gtk_tree_store_newv(COLUMN_NUM, types);
	if (modelHandle == 0) error (SWT.ERROR_NO_HANDLES);
	handle = OS.gtk_tree_view_new_with_model(modelHandle);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	
	int parentHandle = parent.parentingHandle ();
	OS.gtk_container_add (parentHandle, fixedHandle);
	OS.gtk_container_add (fixedHandle, scrolledHandle);
	OS.gtk_container_add (scrolledHandle, handle);
	int mode = (style & SWT.MULTI) != 0 ? OS.GTK_SELECTION_EXTENDED : OS.GTK_SELECTION_BROWSE;
	int selectionObject = OS.gtk_tree_view_get_selection(handle);
	OS.gtk_tree_selection_set_mode(selectionObject, mode);
	OS.gtk_tree_view_set_headers_visible(handle, false);	
	int hsp = (style & SWT.H_SCROLL) != 0 ? OS.GTK_POLICY_AUTOMATIC : OS.GTK_POLICY_NEVER;
	int vsp = (style & SWT.V_SCROLL) != 0 ? OS.GTK_POLICY_AUTOMATIC : OS.GTK_POLICY_NEVER;
	OS.gtk_scrolled_window_set_policy (scrolledHandle, hsp, vsp);
	if ((style & SWT.BORDER) != 0) OS.gtk_scrolled_window_set_shadow_type (scrolledHandle, OS.GTK_SHADOW_ETCHED_IN);
	OS.gtk_widget_show (fixedHandle);
	OS.gtk_widget_show (scrolledHandle);
	OS.gtk_widget_show (handle);
	columnHandle = OS.gtk_tree_view_column_new();
	int renderer;
	renderer = OS.gtk_cell_renderer_text_new();
	if (renderer == 0) error(SWT.ERROR_NO_HANDLES);
	OS.gtk_tree_view_column_pack_end (columnHandle, renderer, true);
	OS.gtk_tree_view_column_add_attribute (columnHandle, renderer, "text", 0);
	OS.gtk_tree_view_column_add_attribute(columnHandle, renderer, "foreground-gdk", 2);
	OS.gtk_tree_view_column_add_attribute(columnHandle, renderer, "background-gdk", 3);
	renderer = OS.gtk_cell_renderer_pixbuf_new();
	if (renderer == 0) error(SWT.ERROR_NO_HANDLES);
	OS.gtk_tree_view_column_pack_end (columnHandle, renderer, false);
	OS.gtk_tree_view_column_add_attribute (columnHandle, renderer, "pixbuf", 1);
	if ((style & SWT.CHECK) != 0) {
		renderer = OS.gtk_cell_renderer_toggle_new();
		if (renderer == 0) error(SWT.ERROR_NO_HANDLES);
		OS.gtk_tree_view_column_pack_end (columnHandle, renderer, false);
		OS.gtk_tree_view_column_add_attribute (columnHandle, renderer, "active", 5);
		OS.g_signal_connect(renderer, OS.toggled, getDisplay().toggleProc, columnHandle);
	}
	OS.gtk_tree_view_insert_column (handle, columnHandle, 0);
	OS.gtk_tree_view_set_headers_visible(handle, false);
}

void hookEvents () {
	//TO DO - get rid of enter/exit for mouse crossing border
	super.hookEvents ();
	Display display = getDisplay ();
	int selection = OS.gtk_tree_view_get_selection(handle);
	OS.g_signal_connect_after (selection, OS.changed, display.windowProc2, SWT.Selection);
	OS.g_signal_connect (handle, OS.row_activated, display.windowProc4, SWT.DefaultSelection);
	OS.g_signal_connect (handle, OS.row_expanded, display.windowProc4, SWT.Expand);
	OS.g_signal_connect (handle, OS.row_collapsed, display.windowProc4, SWT.Collapse);
	OS.g_signal_connect (handle, OS.event_after, display.windowProc3, 0);
	// FIXME - we want to get MouseUp *AFTER*
}

void createWidget(int index) {
	super.createWidget(index);
	items = new TreeItem[4];
}

void createItem (TreeItem item, int iter, int index) {
	int id = 0;
	while (id < items.length && items [id] != null) id++;
	if (id == items.length) {
		TreeItem [] newItems = new TreeItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	item.handle = OS.g_malloc(OS.GtkTreeIter_sizeof());
	if (item.handle == 0) error(SWT.ERROR_NO_HANDLES);
	if (index==-1) {
		OS.gtk_tree_store_append(modelHandle, item.handle, iter);
	} else {
		OS.gtk_tree_store_insert(modelHandle, item.handle, iter, index);
	}
	OS.gtk_tree_store_set(modelHandle, item.handle, 4, id, -1);
	items[id] = item;
}

/**
 * Deselects all selected items in the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselectAll() {
	checkWidget();
	blockSignal(handle, SWT.Selection);
	int selection = OS.gtk_tree_view_get_selection(handle);
	OS.gtk_tree_selection_unselect_all(selection);
	unblockSignal(handle, SWT.Selection);
}

void destroyItem (TreeItem item) {
	// FIXME - do I need to block Collapse?
	OS.gtk_tree_store_remove (modelHandle, item.handle);
}

/**
 * Returns the item at the given point in the receiver
 * or null if no such item exists. The point is in the
 * coordinate system of the receiver.
 *
 * @param point the point used to locate the item
 * @return the item at the given point
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem getItem (Point point) {
	checkWidget ();
	int [] path = new int [1];	
	if (!OS.gtk_tree_view_get_path_at_pos(handle, point.x, point.y, path, null, null, null)) return null;
	if (path[0]==0) return null;
	int iter = OS.g_malloc(OS.GtkTreeIter_sizeof());
	OS.gtk_tree_model_get_iter(modelHandle, iter, path[0]);
	int[] index = new int[1];
	OS.gtk_tree_model_get(modelHandle, iter, 4, index, -1);
	OS.g_free(iter);
	return items[index[0]];
}

/**
 * Returns the number of items contained in the receiver
 * that are direct item children of the receiver.  The
 * number that is returned is the number of roots in the
 * tree.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget ();
	return getItemCount (0);
}

int getItemCount (int iter) {
	return OS.gtk_tree_model_iter_n_children(modelHandle, iter);
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
 */
public int getItemHeight () {
	checkWidget ();
	// FIXME
	// I (bgs) am not sure what to do with this.
	// In GTK2, rows may have different heights, so asking
	// this question will only make sense given the item.
	if (getItemCount() == 0) return 18;
	GdkRectangle rect = new GdkRectangle();
	int path = OS.gtk_tree_path_new_from_string(Converter.wcsToMbcs(null, "0", true));
	OS.gtk_tree_view_get_cell_area(parent.handle, path, columnHandle, rect);
	return rect.height;
}

/**
 * Returns the number of items contained in the receiver
 * that are direct item children of the receiver.  These
 * are the roots of the tree.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem [] getItems () {
	TreeItem[] items;
	checkWidget();
	return getItems (0);
}

TreeItem [] getItems (int parent) {
	int len = getItemCount(parent);
	TreeItem[] answer = new TreeItem [len];
	if (len==0) return answer;
	int currentIter = OS.g_malloc(OS.GtkTreeIter_sizeof());
	if (!OS.gtk_tree_model_iter_children(modelHandle, currentIter, parent)) return answer;
	for (int i=0; i<len; i++) {
		int[] index = new int[1];
		OS.gtk_tree_model_get(modelHandle, currentIter, 4, index, -1);
		answer[i] = items[index[0]];
		OS.gtk_tree_model_iter_next(modelHandle, currentIter);
	}
	OS.g_free(currentIter);
	return answer;
}

/**
 * Returns the receiver's parent item, which must be a
 * <code>TreeItem</code> or null when the receiver is a
 * root.
 *
 * @return the receiver's parent item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem getParentItem () {
	checkWidget ();
	return null;
}

/**
 * Returns an array of <code>TreeItem</code>s that are currently
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
 */
public TreeItem[] getSelection () {
	checkWidget();
	TreeItem[] answer;
	if ((style & SWT.MULTI) != 0) {
		/*
		 * Multi-selection case
		 */
		selSize = 0;
		selIndices = new int[items.length];
		int selection = OS.gtk_tree_view_get_selection(handle);
		OS.gtk_tree_selection_selected_foreach(selection, getDisplay().selectionIterProc, handle);
		answer = new TreeItem[selSize];
		for (int i=0; i<selSize; i++) answer[i] = items[selIndices[i]];
	} else {
		/*
		 * Single-selection case
		 */
		int iter = OS.g_malloc(OS.GtkTreeIter_sizeof());
		int selection = OS.gtk_tree_view_get_selection(handle);
		boolean hasSelection = OS.gtk_tree_selection_get_selected(selection, null, iter);
		if (hasSelection) {
			int[] index = new int[1];
			OS.gtk_tree_model_get(modelHandle, iter, 4, index, -1);
			answer = new TreeItem[1];
			answer[0] = items[index[0]];
		} else {
			answer = new TreeItem[0];
		}
		OS.g_free(iter);
	}
	return answer;
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
 */
public int getSelectionCount () {
	checkWidget();
	selSize = 0;
	selIndices = new int[items.length];
	int selection = OS.gtk_tree_view_get_selection(handle);
	OS.gtk_tree_selection_selected_foreach(selection, getDisplay().selectionIterProc, handle);
	return selSize;
}

int processCollapse (int iter, int path, int data) {
	Event event = new Event ();
	int[] index = new int[1];
	OS.gtk_tree_model_get(modelHandle, iter, 4, index, -1);
	event.item = items[index[0]];
	sendEvent (SWT.Collapse, event);	
	return 0;
}

int processExpand (int iter, int path, int data) {
	Event event = new Event ();
	int[] index = new int[1];
	OS.gtk_tree_model_get(modelHandle, iter, 4, index, -1);
	event.item = items[index[0]];
	sendEvent (SWT.Expand, event);
	/* FIXME
	 * The similar code in 1.2 checks if the expand is necesary.
	 * Should we call gtk_tree_view_row_expanded() for a similar check?
	 */
	blockSignal(handle, SWT.Expand);
	OS.gtk_tree_view_expand_row(handle, path, false);
	unblockSignal(handle, SWT.Expand);
	return 0;
}

int processDefaultSelection (int int0, int int1, int int2) {
	int iter = OS.g_malloc(OS.GtkTreeIter_sizeof());
	OS.gtk_tree_model_get_iter(modelHandle, iter, int0);
	int[] index = new int[1];
	OS.gtk_tree_model_get(modelHandle, iter, 4, index, -1);
	OS.g_free(iter);
	Event event = new Event ();
	event.item = items[index[0]];
	postEvent (SWT.DefaultSelection, event);
	return 0;
}

int processSelection (int int0, int int1, int int2) {
	Event event = new Event ();
	if ((style&SWT.SINGLE)!=0) {
		event.item = getSelectionCount()>0? getSelection()[0] : null;
	} else {
		if (getSelectionCount()==1) event.item = getSelection()[0];
	}
	postEvent (SWT.Selection, event);
	return 0;	
}

int processSelectionIter(int path, int iter, int data) {
	int[] index = new int[1];
	OS.gtk_tree_model_get(modelHandle, iter, 4, index, -1);
	selIndices[selSize++] = index[0];
	return 0;
}

int processToggle(int pathString, int handle) {
	int path = OS.gtk_tree_path_new_from_string(pathString);
	int iter = OS.g_malloc(OS.GtkTreeIter_sizeof());
	OS.gtk_tree_model_get_iter(modelHandle, iter, path);
	int[] index = new int[1];
	OS.gtk_tree_model_get(modelHandle, iter, 4, index, -1);
	OS.g_free(iter);
	OS.gtk_tree_path_free(path);
	// FIXME - just use OS calls	
	boolean checked = items[index[0]].getChecked();
	checked = !checked;
	items[index[0]].setChecked(checked);
	return 0;
}

void releaseWidget () {
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null && !item.isDisposed ()) {
			item.releaseWidget ();
			item.releaseHandle ();
		}
	}
	items = null;
	modelHandle = 0;
	super.releaseWidget();
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
	checkWidget ();
	OS.gtk_tree_store_clear (modelHandle);
	for (int i=0; i<items.length; i++) {
		TreeItem item = items[i];
		if (item!=null && !item.isDisposed()) {
			item.releaseWidget();
			item.releaseHandle();
		}
	}
	items = new TreeItem[4];
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection, listener);	
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when items in the receiver are expanded or collapsed..
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
 * @see TreeListener
 * @see #addTreeListener
 */
public void removeTreeListener(TreeListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Expand, listener);
	eventTable.unhook (SWT.Collapse, listener);
}

public void setInsertMark(TreeItem item, boolean set) {
	// FIXME - not yet implemented
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
	blockSignal(handle, SWT.Selection);
	OS.gtk_tree_selection_select_all (OS.gtk_tree_view_get_selection(handle));
	unblockSignal(handle, SWT.Selection);
}

void setBackgroundColor (GdkColor color) {
	super.setBackgroundColor (color);
	OS.gtk_widget_modify_base (handle, 0, color);
}

void setForegroundColor (GdkColor color) {
	super.setForegroundColor (color);
	OS.gtk_widget_modify_text (handle, 0, color);
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selected is first cleared, then the new items are
 * selected.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of items is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if one of the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree#deselectAll()
 */
public void setSelection (TreeItem [] items) {
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	checkWidget();
	int selection = OS.gtk_tree_view_get_selection(handle);
	blockSignal(handle, SWT.Selection);
	OS.gtk_tree_selection_unselect_all(selection);
	for (int i = 0; i < items.length; i++) {
		if (items[i].isDisposed ()) break;
		OS.gtk_tree_selection_select_iter (selection, items[i].handle);
	}
	unblockSignal(handle, SWT.Selection);
}

/**
 * Shows the selection.  If the selection is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled until
 * the selection is visible.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree#showItem(TreeItem)
 */
public void showSelection () {
	checkWidget();
	if (getSelectionCount()==0) return;
	TreeItem item = getSelection()[0];
	int path = OS.gtk_tree_model_get_path(modelHandle, item.handle);
	OS.gtk_tree_view_scroll_to_cell(handle, path, 0, false, 0, 0);
	OS.gtk_tree_path_free(path);
}

/**
 * Shows the item.  If the item is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled
 * and expanded until the item is visible.
 *
 * @param item the item to be shown
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree#showSelection()
 */
public void showItem (TreeItem item) {
	checkWidget ();
	int path = OS.gtk_tree_model_get_path(modelHandle, item.handle);
	OS.gtk_tree_view_scroll_to_cell(handle, path, 0, false, 0, 0);
	OS.gtk_tree_path_free(path);
}

void deregister () {
	super.deregister ();
	WidgetTable.remove(OS.gtk_tree_view_get_selection(handle));
	WidgetTable.remove(columnHandle);
}

void register () {
	super.register ();
	WidgetTable.put (OS.gtk_tree_view_get_selection(handle), this);
	WidgetTable.put (columnHandle, this);
}
}

