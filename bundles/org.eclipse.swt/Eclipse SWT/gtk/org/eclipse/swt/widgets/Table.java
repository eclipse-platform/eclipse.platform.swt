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
 * Instances of this class implement a selectable user interface
 * object that displays a list of images and strings and issue
 * notificiation when selected.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TableItem</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, HIDE_SELECTION</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles SINGLE, and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Table extends Composite {
	int modelHandle, fakeColumn, fakePixbufRenderer;
	boolean selected;
	int itemCount, columnCount, imageHeight;
	TableItem [] items;
	TableColumn [] columns;
	public static int MAX_COLUMNS = 32;
	static int INTERNAL_COLUMNS = 2*MAX_COLUMNS + 1  // check
	                                     + 2; // fg, bg
	int selSize; int[] selIndices;

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
 * @see SWT#FULL_SELECTION
 * @see SWT#HIDE_SELECTION
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Table (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	/*
	* To be compatible with Windows, force the H_SCROLL
	* and V_SCROLL style bits.  On Windows, it is not
	* possible to create a table without scroll bars.
	*/
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
public void addSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	Point size = computeNativeSize(handle, wHint, hHint, changed);
	Rectangle trim = computeTrim (0, 0, size.x, size.y);
	size.x = trim.width;
	size.y = trim.height;
	return size;
}

void createHandle (int index) {
	state |= HANDLE;
	fixedHandle = OS.gtk_fixed_new ();
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	scrolledHandle = OS.gtk_scrolled_window_new (0, 0);
	if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);

	int[] types = new int[INTERNAL_COLUMNS];
	for (int j=0; j<MAX_COLUMNS; j++) {
		types[j] = OS.G_TYPE_STRING();
		types[j+MAX_COLUMNS] = OS.gdk_pixbuf_get_type();
	}
	types[2*MAX_COLUMNS] = OS.G_TYPE_BOOLEAN();
	types[2*MAX_COLUMNS+1] = OS.GDK_TYPE_COLOR();
	types[2*MAX_COLUMNS+2] = OS.GDK_TYPE_COLOR();
	modelHandle = OS.gtk_list_store_newv(INTERNAL_COLUMNS, types);
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

	fakeColumn = _createColumnHandle(index, null);
	OS.gtk_tree_view_insert_column(handle, fakeColumn, 0);
}

int _createColumnHandle(int index, TableColumn column) {
	int columnHandle = OS.gtk_tree_view_column_new();
	if (columnHandle == 0) error(SWT.ERROR_NO_HANDLES);
	int textRenderer = OS.gtk_cell_renderer_text_new();
	if (textRenderer == 0) error(SWT.ERROR_NO_HANDLES);
	OS.gtk_tree_view_column_pack_end(columnHandle, textRenderer, true);
	OS.gtk_tree_view_column_add_attribute(columnHandle, textRenderer, "text", index);
	OS.gtk_tree_view_column_add_attribute(columnHandle, textRenderer, "foreground-gdk", 2*MAX_COLUMNS+1);
	OS.gtk_tree_view_column_add_attribute(columnHandle, textRenderer, "background-gdk", 2*MAX_COLUMNS+2);
	int pixbufRenderer = OS.gtk_cell_renderer_pixbuf_new();
	if (pixbufRenderer == 0) error(SWT.ERROR_NO_HANDLES);
	OS.gtk_tree_view_column_pack_end(columnHandle, pixbufRenderer, false);
	OS.gtk_tree_view_column_add_attribute(columnHandle, pixbufRenderer, "pixbuf", index+MAX_COLUMNS);
	if ((style & SWT.CHECK) != 0 && (index==0)) {
		int checkRenderer = OS.gtk_cell_renderer_toggle_new();
		if (checkRenderer == 0) error(SWT.ERROR_NO_HANDLES);
		OS.gtk_tree_view_column_pack_end(columnHandle, checkRenderer, false);
		OS.gtk_tree_view_column_add_attribute(columnHandle, checkRenderer, "active", 2*MAX_COLUMNS);
		OS.g_signal_connect(checkRenderer, OS.toggled, getDisplay().toggleProc, columnHandle);
	}
	OS.gtk_tree_view_column_set_resizable(columnHandle, true);
	WidgetTable.put (columnHandle, this);
	OS.gtk_tree_view_column_set_clickable(columnHandle, true);
	OS.g_signal_connect(columnHandle, OS.clicked, getDisplay().windowProc2, SWT.Activate);	
	if (column!=null) {
		column.pixbufRendererHandle = pixbufRenderer;
	} else {
		fakePixbufRenderer = pixbufRenderer;
	}
	return columnHandle;
}

void createItem (TableColumn column, int index) {
	if (!(0 <= index && index <= columnCount)) error (SWT.ERROR_ITEM_NOT_ADDED);
	if (columnCount == columns.length) {
		TableColumn [] newColumns = new TableColumn [columns.length + 4];
		System.arraycopy (columns, 0, newColumns, 0, columns.length);
		columns = newColumns;
	}
	if (fakeColumn != 0) {
		column.handle = fakeColumn;
		fakeColumn = 0;
	} else {
		column.handle = _createColumnHandle(index, column);
		OS.gtk_tree_view_insert_column(handle, column.handle, index);
	}
	System.arraycopy (columns, index, columns, index + 1, columnCount++ - index);
	columns [index] = column;
}

void createItem (TableItem item, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_ITEM_NOT_ADDED);
	if (itemCount == items.length) {
		TableItem [] newItems = new TableItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	item.handle = OS.g_malloc(OS.GtkTreeIter_sizeof());
	if (item.handle==0) error(SWT.ERROR_NO_HANDLES);
	OS.gtk_list_store_insert (modelHandle, item.handle, index);
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
}

void createWidget (int index) {
	super.createWidget (index);
	items = new TableItem [4];
	columns = new TableColumn [4];
	itemCount = columnCount = 0;
}

GdkColor defaultBackground () {
	Display display = getDisplay ();
	return display.COLOR_LIST_BACKGROUND;
}

GdkColor defaultForeground () {
	Display display = getDisplay ();
	return display.COLOR_LIST_FOREGROUND;
}

void deregister() {
	super.deregister ();
	WidgetTable.remove(OS.gtk_tree_view_get_selection(handle));
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
	if ((index<0) || (index>=items.length)) return;
	blockSignal (handle, SWT.Selection);
	int selection = OS.gtk_tree_view_get_selection(handle);
	OS.gtk_tree_selection_unselect_iter(selection, items[index].handle);
	unblockSignal (handle, SWT.Selection);
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
	// FIXME: use the native facility
	int selection = OS.gtk_tree_view_get_selection(handle);
	blockSignal(handle, SWT.Selection);
	for (int i=start; i<=end; i++) {
		if ((i<0) || (i>=items.length)) continue;
		OS.gtk_tree_selection_unselect_iter(selection, items[i].handle);
	}
	unblockSignal(handle, SWT.Selection);
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
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int [] indices) {
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int selection = OS.gtk_tree_view_get_selection(handle);
	blockSignal(handle, SWT.Selection);
	for (int i=0; i<indices.length; i++) {
		if ((i<0) || (i>=items.length)) continue;
		OS.gtk_tree_selection_unselect_iter(selection, items[i].handle);
	}
	unblockSignal(handle, SWT.Selection);
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
	blockSignal(handle, SWT.Selection);
	int selection = OS.gtk_tree_view_get_selection(handle);
	OS.gtk_tree_selection_unselect_all(selection);
	unblockSignal(handle, SWT.Selection);
}

void destroyItem (TableColumn column) {
/*	int index = 0;
	while (index < columnCount) {
		if (columns [index] == column) break;
		index++;
	}
	if (index == columnCount) return;
	WidgetTable.remove(column.handle);
	System.arraycopy (columns, index + 1, columns, index, --columnCount - index);
	columns [columnCount] = null;
	if (columnCount==0) {
		fakeColumn = column.handle;
	} else {
		for (int j=index+1; j<=columnCount; j++) {
			
		}
		OS.gtk_tree_view_remove_column(handle, column.handle);
		OS.g_object_unref(column.handle);
	}*/
}

void destroyItem (TableItem item) {
	int index = 0;
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	if (index == itemCount) return;
	blockSignal(handle, SWT.Selection);
	OS.gtk_list_store_remove(modelHandle, item.handle);
	OS.g_free(item.handle);
	unblockSignal(handle, SWT.Selection);
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
}

void destroyWidget() {
	int model = modelHandle;
	super.destroyWidget();
	OS.g_object_unref(model);
}

GdkColor getBackgroundColor () {
	return getBaseColor ();
}

/**
 * Returns the column at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
 * If no <code>TableColumn</code>s were created by the programmer,
 * this method will throw <code>ERROR_INVALID_RANGE</code> despite
 * the fact that a single column of data may be visible in the table.
 * This occurs when the programmer uses the table like a list, adding
 * items but never creating a column.
 *
 * @param index the index of the column to return
 * @return the column at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TableColumn getColumn (int index) {
	checkWidget();
	if (!(0 <= index && index < columnCount)) error (SWT.ERROR_CANNOT_GET_ITEM);
	return columns [index];
}

/**
 * Returns the number of columns contained in the receiver.
 * If no <code>TableColumn</code>s were created by the programmer,
 * this value is zero, despite the fact that visually, one column
 * of items is may be visible. This occurs when the programmer uses
 * the table like a list, adding items but never creating a column.
 *
 * @return the number of columns
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getColumnCount () {
	checkWidget();
	return columnCount;
}

/**
 * Returns an array of <code>TableColumn</code>s which are the
 * columns in the receiver. If no <code>TableColumn</code>s were
 * created by the programmer, the array is empty, despite the fact
 * that visually, one column of items may be visible. This occurs
 * when the programmer uses the table like a list, adding items but
 * never creating a column.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the items in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TableColumn [] getColumns () {
	checkWidget();
	TableColumn [] result = new TableColumn [columnCount];
	System.arraycopy (columns, 0, result, 0, columnCount);
	return result;
}

/**
 * Returns the width in pixels of a grid line.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getGridLineWidth () {
	checkWidget();
	return 0;
}

/**
 * Returns the height of the receiver's header 
 *
 * @return the height of the header or zero if the header is not visible
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0 
 */
public int getHeaderHeight () {
	checkWidget ();
	if ( !OS.gtk_tree_view_get_headers_visible(handle) ) return 0;
	OS.gtk_widget_realize(handle);
	int fixedWindow = OS.GTK_WIDGET_WINDOW (fixedHandle);
	int binWindow = OS.gtk_tree_view_get_bin_window(handle);
	int[] binY = new int[1];
	OS.gdk_window_get_origin(binWindow, null, binY);
	int[] fixedY = new int[1];
	OS.gdk_window_get_origin(fixedWindow, null, fixedY);
	return binY[0]-fixedY[0];
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
 */
public TableItem getItem (int index) {
	checkWidget();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_CANNOT_GET_ITEM);
	return items [index];
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
public TableItem getItem (Point pt) {
	checkWidget();
	int[] path = new int[1];
	int[] column = new int[1];
	int clientY = pt.y - getHeaderHeight();
	if (!OS.gtk_tree_view_get_path_at_pos(handle, pt.x, clientY, path, column, null, null)) return null;
	if (path[0]==0) return null;
	int indexPtr = OS.gtk_tree_path_get_indices(path[0]);
	OS.gtk_tree_path_free(path[0]);
	if (indexPtr==0) return null;
	int[] indices = new int[1];
	OS.memmove(indices, indexPtr, 4);
	return items [indices[0]];
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
 */
public int getItemCount () {
	checkWidget ();
	return itemCount;
}

/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the receiver's.
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
	// FIXME
	// I (bgs) am not sure what to do with this.
	// In GTK2, rows may have different heights, so asking
	// this question will only make sense given the item.
	if (itemCount == 0) return 15;
	OS.gtk_widget_realize(handle);
	GdkRectangle rect = new GdkRectangle();
	int path = OS.gtk_tree_path_new_from_string(Converter.wcsToMbcs(null, "0", true));
	int columnPtr;
	if (fakeColumn != 0) {
		columnPtr = fakeColumn;
	} else {
		columnPtr = columns[0].handle;
	}
	OS.gtk_tree_view_get_cell_area(handle, path, columnPtr, rect);
	return rect.height;
}

/**
 * Returns an array of <code>TableItem</code>s which are the items
 * in the receiver. 
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the items in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TableItem [] getItems () {
	checkWidget();
	TableItem [] result = new TableItem [itemCount];
	System.arraycopy (items, 0, result, 0, itemCount);
	return result;
}

/**
 * Returns an array of <code>TableItem</code>s that are currently
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
public TableItem [] getSelection () {
	checkWidget();
	selSize = 0;
	selIndices = new int[itemCount];
	int selection = OS.gtk_tree_view_get_selection(handle);
	OS.gtk_tree_selection_selected_foreach(selection, getDisplay().selectionIterProc, handle);
	TableItem[] answer = new TableItem[selSize];
	for (int i=0; i<selSize; i++) answer[i] = items[selIndices[i]];
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
	selIndices = new int[itemCount];
	int selection = OS.gtk_tree_view_get_selection(handle);
	OS.gtk_tree_selection_selected_foreach(selection, getDisplay().selectionIterProc, handle);
	return selSize;
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
 */
public int getSelectionIndex () {
	checkWidget();
	selSize = 0;
	selIndices = new int[itemCount];
	int selection = OS.gtk_tree_view_get_selection(handle);
	OS.gtk_tree_selection_selected_foreach(selection, getDisplay().selectionIterProc, handle);
	if (selSize==0) return -1;
	return selIndices[0];
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
 */
public int [] getSelectionIndices () {
	checkWidget();
	selSize = 0;
	selIndices = new int[itemCount];
	int selection = OS.gtk_tree_view_get_selection(handle);
	OS.gtk_tree_selection_selected_foreach(selection, getDisplay().selectionIterProc, handle);
	int[] answer = new int[selSize];
	for (int i=0; i<selSize; i++) answer[i] = selIndices[i];
	return answer;
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
	int selection = OS.gtk_tree_view_get_selection(handle);
	String pathString = Integer.toString(index);
	int path = OS.gtk_tree_path_new_from_string(Converter.wcsToMbcs(null, pathString, true));
	boolean answer = OS.gtk_tree_selection_path_is_selected(selection, path);
	OS.gtk_tree_path_free(path);
	return answer;
}

int processSelection (int int0, int int1, int int2) {
/*
	int focus_row = OS.GTK_CLIST_FOCUS_ROW (handle);
	if (int0 != focus_row) return 0;
	if ((style & SWT.MULTI) != 0) selected = false;
*/
	int eventType = SWT.Selection;
	Event event = new Event ();
	event.item = items [int0];
	postEvent (eventType, event);
	return 0;
}

int paintWindow () {
	OS.gtk_widget_realize (handle);
	return OS.gtk_tree_view_get_bin_window (handle);
}

int processActivate (int columnIndex, int arg1, int int2) {
	if (fakeColumn != 0) return 0;
	TableColumn column = columns [columnIndex];
	if (column != null) column.postEvent (SWT.Selection);
	return 0;
}

int processMouseDown (int int0, int int1, int int2) {
	GdkEventButton e = new GdkEventButton ();
	int indexPtr;
	int[] path = new int[1];
	int[] column = new int[1];
	OS.memmove (e, int0, GdkEventButton.sizeof);
	if ((e.type == OS.GDK_2BUTTON_PRESS) &&
	     OS.gtk_tree_view_get_path_at_pos(handle, (int)e.x, (int)e.y, path, column, null, null) &&
	     ((indexPtr = OS.gtk_tree_path_get_indices(path[0])) != 0)) {
		int[] indices = new int[1];
		OS.memmove(indices, indexPtr, 4);
		Event event = new Event ();
		event.item = items [indices[0]];
		postEvent (SWT.DefaultSelection, event);
		OS.gtk_tree_path_free(path[0]);
	}
	int headerHeight = getHeaderHeight();
	e.y += headerHeight;
	OS.memmove(int0, e, GdkEventButton.sizeof);
	int result = super.processMouseDown(int0, int1, int2);
	e.y -= headerHeight;
	OS.memmove(int0, e, GdkEventButton.sizeof);
	return result;
}

int processMouseUp (int int0, int int1, int int2) {
	GdkEventButton e = new GdkEventButton ();
	OS.memmove (e, int0, GdkEventButton.sizeof);
	int headerHeight = getHeaderHeight();
	e.y += headerHeight;
	OS.memmove(int0, e, GdkEventButton.sizeof);
	int result = super.processMouseUp(int0, int1, int2);
	e.y -= headerHeight;
	OS.memmove(int0, e, GdkEventButton.sizeof);
	return result;
}

int processMouseMove (int int0, int int1, int int2) {
	GdkEventButton e = new GdkEventButton ();
	OS.memmove (e, int0, GdkEventButton.sizeof);
	int headerHeight = getHeaderHeight();
	e.y += headerHeight;
	OS.memmove(int0, e, GdkEventButton.sizeof);
	int result = super.processMouseMove(int0, int1, int2);
	e.y -= headerHeight;
	OS.memmove(int0, e, GdkEventButton.sizeof);
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
	int[] path = new int[1];
	int[] column = new int[1];
	if (!OS.gtk_tree_view_get_path_at_pos(handle, 1, 1, path, column, null, null)) return 0;
	if (path[0]==0) return -1;
	int indexPtr = OS.gtk_tree_path_get_indices(path[0]);
	OS.gtk_tree_path_free(path[0]);
	if (indexPtr==0) return -1;
	int[] indices = new int[1];
	OS.memmove(indices, indexPtr, 4);
	return indices[0];
}

void hookEvents () {
	//TO DO - get rid of enter/exit for mouse crossing border
	super.hookEvents ();
	Display display = getDisplay ();
	int selection = OS.gtk_tree_view_get_selection(handle);
	OS.g_signal_connect(selection, OS.changed, display.windowProc2, SWT.Selection);
	OS.g_signal_connect (handle, OS.event_after, display.windowProc3, 0);
}

/**
 * Searches the receiver's list starting at the first column
 * (index 0) until a column is found that is equal to the 
 * argument, and returns the index of that column. If no column
 * is found, returns -1.
 *
 * @param column the search column
 * @return the index of the column
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (TableColumn column) {
	checkWidget();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<columnCount; i++) {
		if (columns [i] == column) return i;
	}
	return -1;
}

/**
 * Searches the receiver's list starting at the first item
 * (index 0) until an item is found that is equal to the 
 * argument, and returns the index of that item. If no item
 * is found, returns -1.
 *
 * @param item the search item
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
public int indexOf (TableItem item) {
	checkWidget();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<itemCount; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

void register () {
	super.register ();
	WidgetTable.put (OS.gtk_tree_view_get_selection(handle), this);
}

void releaseWidget () {
	for (int i=0; i<columnCount; i++) {
		TableColumn column = columns [i];
		if (!column.isDisposed ()) column.releaseResources ();
	}
	columns = null;
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
		if (!item.isDisposed ()) item.releaseResources ();
	}
	items = null;
	super.releaseWidget ();
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
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_ITEM_NOT_REMOVED);
	TableItem item = items[index];
	OS.gtk_list_store_remove(modelHandle, item.handle);
	OS.g_free(item.handle);
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	item.releaseResources ();
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
	int index = end;
	while (index >= start) {
		OS.gtk_list_store_remove(modelHandle, items[index].handle);
		items [index].releaseResources ();
		--index;
	}
	int first = index + 1, last = end + 1;
	System.arraycopy (items, last, items, first, itemCount - last);
	for (int i=itemCount-(last-first); i<itemCount; i++) items [i] = null;
	itemCount = itemCount - (last - first);
	if (first > start) error (SWT.ERROR_ITEM_NOT_REMOVED);
}

/**
 * Removes the items from the receiver's list at the given
 * zero-relative indices.
 *
 * @param indices the array of indices of the items
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
public void remove (int [] indices) {
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int [] newIndices = new int [indices.length];
	System.arraycopy (indices, 0, newIndices, 0, indices.length);
	sort (newIndices);
	int last = -1;
	for (int i=0; i<newIndices.length; i++) {
		int index = newIndices [i];
		if (index != last || i == 0) {
			OS.gtk_list_store_remove(modelHandle, items[index].handle);
			// BUG - disposed callback could remove an item
			items [index].releaseResources ();
			System.arraycopy (items, index + 1, items, index, --itemCount - index);
			items [itemCount] = null;
			last = index;
		}
	}
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
	OS.gtk_list_store_clear (modelHandle);
	int index = itemCount - 1;
	while (index >= 0) {
		items [index].releaseResources ();
		--index;
	}
	items = new TableItem [4];
	itemCount = 0;
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
 * Selects the item at the given zero-relative index in the receiver. 
 * If the item at the index was already selected, it remains
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
	if ((index<0) || (index>=items.length)) return;
	blockSignal(handle, SWT.Selection);
	int selection = OS.gtk_tree_view_get_selection(handle);
	OS.gtk_tree_selection_select_iter(selection, items[index].handle);
	unblockSignal(handle, SWT.Selection);
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * If the item at the index was already selected, it remains
 * selected. The range of the indices is inclusive. Indices that are
 * out of range are ignored.
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
	// FIXME, use the native mechanism
	blockSignal(handle, SWT.Selection);
	int selection = OS.gtk_tree_view_get_selection(handle);
	for (int i=start; i<=end; i++) {
		if ((i<0) || (i>=items.length)) continue;
		OS.gtk_tree_selection_select_iter(selection, items[i].handle);
	}
	unblockSignal(handle, SWT.Selection);
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
 *    <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int [] indices) {
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	blockSignal(handle, SWT.Selection);
	int selection = OS.gtk_tree_view_get_selection(handle);
	for (int i=0; i<indices.length; i++) {
		if ((i<0) || (i>=items.length)) continue;
		OS.gtk_tree_selection_select_iter(selection, items[i].handle);
	}
	unblockSignal(handle, SWT.Selection);
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
	int selection = OS.gtk_tree_view_get_selection(handle);
	OS.gtk_tree_selection_select_all(selection);
	unblockSignal(handle, SWT.Selection);
}

int processSelectionIter(int path, int iter, int data) {
	int indexPtr = OS.gtk_tree_path_get_indices(path);
	if (indexPtr!=0) {
		int[] indices = new int[1];
		OS.memmove(indices, indexPtr, 4);
		selIndices[selSize++] = indices[0];
	}
	return 0;
}

int processToggle(int path, int handle) {
	int length = OS.strlen(path);
	byte[] pathBytes = new byte[length+1];
	OS.memmove(pathBytes, path, length);
	char[] pathChars = Converter.mbcsToWcs(null, pathBytes);
	int itemIndex = Integer.parseInt(new String(pathChars));
	boolean checked = items[itemIndex].getChecked();
	checked = !checked;
	items[itemIndex].setChecked(checked);
	Event event = new Event();
	event.detail = SWT.CHECK;
	event.item = items[itemIndex];
	postEvent(SWT.Selection, event);
	return 0;
}

void setBackgroundColor (GdkColor color) {
	super.setBackgroundColor (color);
	OS.gtk_widget_modify_base (handle, 0, color);
}


/**
 * Returns <code>true</code> if the receiver's header is visible,
 * and <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's header's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getHeaderVisible () {
	checkWidget();
	return OS.gtk_tree_view_get_headers_visible(handle);
}

void setForegroundColor (GdkColor color) {
	super.setForegroundColor (color);
	OS.gtk_widget_modify_text (handle, 0, color);
}

/**
 * Marks the receiver's header as visible if the argument is <code>true</code>,
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
 */
public void setHeaderVisible (boolean show) {
	checkWidget ();
	OS.gtk_tree_view_set_headers_visible(handle, show);
}

/**
 * Returns <code>true</code> if the receiver's lines are visible,
 * and <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the visibility state of the lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getLinesVisible() {
	checkWidget();
	return false;
}

/**
 * Marks the receiver's lines as visible if the argument is <code>true</code>,
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
 */
public void setLinesVisible (boolean show) {
	checkWidget();
}

/**
 * Selects the item at the given zero-relative index in the receiver. 
 * The current selected is first cleared, then the new item is selected.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int)
 */
public void setSelection (int index) {
	deselectAll ();
	select (index);
}

public void setRedraw (boolean redraw) {
	checkWidget ();
	// FIXME
/*	if (redraw) {
		OS.gtk_clist_thaw (handle);
	} else {
		OS.gtk_clist_freeze (handle);
	}
*/
}

/**
 * Selects the items at the given zero-relative indices in the receiver. 
 * The current selected if first cleared, then the new items are selected.
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
	deselectAll ();
	select (start, end);
}

/**
 * Selects the items at the given zero-relative indices in the receiver. 
 * The current selected is first cleared, then the new items are selected.
 *
 * @param indices the indices of the items to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int[])
 */
public void setSelection (int [] indices) {
	deselectAll ();
	select (indices);
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
 * @see Table#deselectAll()
 * @see Table#select(int)
 */
public void setSelection (TableItem [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	deselectAll ();
	int length = items.length;
	if (length == 0) return;
	if ((style & SWT.SINGLE) != 0) length = 1;
	for (int i=length-1; i>=0; --i) {
		int index = indexOf (items [i]);
		if (index != -1) select (index);
	}
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
	// FIXME - For some reason, sometimes the tree scrolls to the wrong place
	checkWidget();
	if (!(0 <= index && index < itemCount)) return;
	int path = OS.gtk_tree_model_get_path(modelHandle, items[index].handle);
	OS.gtk_tree_view_scroll_to_cell(handle,
	                                path,
	                                0,    // no h-scroll, please
	                                true, // align at the top
	                                0, 0);
	OS.gtk_tree_path_free(path);
}

/**
 * Shows the item.  If the item is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled until
 * the item is visible.
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
 * @see Table#showSelection()
 */
public void showItem (TableItem item) {
	int path = OS.gtk_tree_model_get_path(modelHandle, item.handle);
	OS.gtk_tree_view_scroll_to_cell(handle, path, 0, false, 0, 0);
	OS.gtk_tree_path_free(path);
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
 * @see Table#showItem(TableItem)
 */
public void showSelection () {
	checkWidget();
	if (getSelectionCount()==0) return;
	TableItem item = getSelection()[0];
	int path = OS.gtk_tree_model_get_path(modelHandle, item.handle);
	OS.gtk_tree_view_scroll_to_cell(handle, path, 0, false, 0, 0);
	OS.gtk_tree_path_free(path);
}
}
