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
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, HIDE_SELECTION, VIRTUAL</dd>
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
	int /*long*/ modelHandle, checkRenderer;
	int itemCount, columnCount, lastIndexOf, lastDataIndex = -1;
	int /*long*/ ignoreTextCell, ignorePixbufCell;
	TableItem [] items;
	TableColumn [] columns;
	ImageList imageList;
	boolean firstCustomDraw;
	
	static final int CHECKED_COLUMN = 0;
	static final int GRAYED_COLUMN = 1;
	static final int FOREGROUND_COLUMN = 2;
	static final int BACKGROUND_COLUMN = 3;
	static final int FONT_COLUMN = 4;
	static final int FIRST_COLUMN = FONT_COLUMN + 1;
	static final int CELL_PIXBUF = 0;
	static final int CELL_TEXT = 1;
	static final int CELL_FOREGROUND = 2;
	static final int CELL_BACKGROUND = 3;
	static final int CELL_FONT = 4;
	static final int CELL_TYPES = CELL_FONT + 1;

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

TableItem _getItem (int index) {
	if (items [index] != null) return items [index];
	return items [index] = new TableItem (this, SWT.NONE, index, false);
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

boolean checkData (TableItem item) {
	if (item.cached) return true;
	if ((style & SWT.VIRTUAL) != 0) {
		item.cached = true;
		Event event = new Event ();
		event.item = item;
		int mask = OS.G_SIGNAL_MATCH_DATA | OS.G_SIGNAL_MATCH_ID;
		int signal_id = OS.g_signal_lookup (OS.row_changed, OS.gtk_tree_model_get_type ());
		OS.g_signal_handlers_block_matched (modelHandle, mask, signal_id, 0, 0, 0, handle);
		sendEvent (SWT.SetData, event);
		//widget could be disposed at this point
		if (isDisposed ()) return false;
		OS.g_signal_handlers_unblock_matched (modelHandle, mask, signal_id, 0, 0, 0, handle);
		if (item.isDisposed ()) return false;
	}
	return true;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
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

int calculateWidth (int /*long*/ column, int /*long*/ iter) {
	OS.gtk_tree_view_column_cell_set_cell_data (column, modelHandle, iter, false, false);
	int [] width = new int [1];
	OS.gtk_tree_view_column_cell_get_size (column, null, null, null, width, null);
	return width [0];
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

/**
 * Clears the item at the given zero-relative index in the receiver.
 * The text, icon and other attribues of the item are set to the default
 * value.  If the table was created with the SWT.VIRTUAL style, these
 * attributes are requested again as needed.
 *
 * @param index the index of the item to clear
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 * 
 * @since 3.0
 */
public void clear (int index) {
	checkWidget ();
	if (!(0 <= index && index < itemCount)) {
		error(SWT.ERROR_INVALID_RANGE);
	}
	TableItem item = items [index];
	if (item != null) item.clear();
}

/**
 * Removes the items from the receiver which are between the given
 * zero-relative start and end indices (inclusive).  The text, icon
 * and other attribues of the items are set to their default values.
 * If the table was created with the SWT.VIRTUAL style, these attributes
 * are requested again as needed.
 *
 * @param start the start index of the item to clear
 * @param end the end index of the item to clear
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if either the start or end are not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see SWT#VIRTUAL
 * @see SWT.SetData
 * 
 * @since 3.0
 */
public void clear (int start, int end) {
	checkWidget ();
	if (start > end) return;
	if (!(0 <= start && start <= end && end < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (start == 0 && end == itemCount - 1) {
		clearAll();
	} else {
		for (int i=start; i<=end; i++) {
			TableItem item = items [i];
			if (item != null) item.clear();
		}
	}
}

/**
 * Clears the items at the given zero-relative indices in the receiver.
 * The text, icon and other attribues of the items are set to their default
 * values.  If the table was created with the SWT.VIRTUAL style, these
 * attributes are requested again as needed.
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
 * 
 * @see SWT#VIRTUAL
 * @see SWT.SetData
 * 
 * @since 3.0
 */
public void clear (int [] indices) {
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (indices.length == 0) return;
	for (int i=0; i<indices.length; i++) {
		if (!(0 <= indices [i] && indices [i] < itemCount)) {
			error (SWT.ERROR_INVALID_RANGE);
		}
	}
	for (int i=0; i<indices.length; i++) {
		TableItem item = items [indices [i]];
		if (item != null) item.clear();
	}
}

/**
 * Clears all the items in the receiver. The text, icon and other
 * attribues of the items are set to their default values. If the
 * table was created with the SWT.VIRTUAL style, these attributes
 * are requested again as needed.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see SWT#VIRTUAL
 * @see SWT.SetData
 * 
 * @since 3.0
 */
public void clearAll () {
	checkWidget ();
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
		if (item != null) item.clear();
	}
}

void createColumn (TableColumn column, int index) {
	int modelIndex = FIRST_COLUMN;
	if (columnCount != 0) {
		int modelLength = OS.gtk_tree_model_get_n_columns (modelHandle);
		boolean [] usedColumns = new boolean [modelLength];
		for (int i=0; i<columnCount; i++) {
			int columnIndex = columns [i].modelIndex;
			for (int j = 0; j < CELL_TYPES; j++) {
				usedColumns [columnIndex + j] = true;
			}
		}
		while (modelIndex < modelLength) {
			if (!usedColumns [modelIndex]) break;
			modelIndex++;
		}
		if (modelIndex == modelLength) {
			int /*long*/ oldModel = modelHandle;
			int /*long*/[] types = getColumnTypes (columnCount + 4); // grow by 4 rows at a time
			int /*long*/ newModel = OS.gtk_list_store_newv (types.length, types);
			if (newModel == 0) error (SWT.ERROR_NO_HANDLES);
			int /*long*/ [] ptr = new int /*long*/ [1];
			for (int i=0; i<itemCount; i++) {
				int /*long*/ newItem = OS.g_malloc (OS.GtkTreeIter_sizeof ());
				if (newItem == 0) error (SWT.ERROR_NO_HANDLES);
				OS.gtk_list_store_append (newModel, newItem);
				TableItem item = items [i];
				if (item != null) {
					int /*long*/ oldItem = item.handle;
					for (int j=0; j<modelLength; j++) {
						OS.gtk_tree_model_get (oldModel, oldItem, j, ptr, -1);
						OS.gtk_list_store_set (newModel, newItem, j, ptr [0], -1);
						if (types [j] == OS.G_TYPE_STRING ()) OS.g_free ((ptr [0]));
					}
					OS.gtk_list_store_remove (oldModel, oldItem);
					OS.g_free (oldItem);
					item.handle = newItem;
				} else {
					OS.g_free (newItem);
				}
			}
			OS.gtk_tree_view_set_model (handle, newModel);
			OS.g_object_unref (oldModel);
			modelHandle = newModel;
		}
	}
	int /*long*/ columnHandle = OS.gtk_tree_view_column_new ();
	if (columnHandle == 0) error (SWT.ERROR_NO_HANDLES);
	if (index == 0 && columnCount > 0) {
		TableColumn checkColumn = columns [0];
		createRenderers (checkColumn.handle, checkColumn.modelIndex, false, checkColumn.style);
	}
	createRenderers (columnHandle, modelIndex, index == 0, column == null ? 0 : column.style);
	if ((style & SWT.VIRTUAL) == 0 && columnCount == 0) {
		OS.gtk_tree_view_column_set_sizing (columnHandle, OS.GTK_TREE_VIEW_COLUMN_AUTOSIZE);
	} else {
		OS.gtk_tree_view_column_set_sizing (columnHandle, OS.GTK_TREE_VIEW_COLUMN_FIXED);
		OS.gtk_tree_view_column_set_fixed_width (columnHandle, 10);
	}
	OS.gtk_tree_view_column_set_resizable (columnHandle, true);
	OS.gtk_tree_view_column_set_clickable (columnHandle, true);
	OS.gtk_tree_view_insert_column (handle, columnHandle, index);
	if (column != null) {
		column.handle = columnHandle;
		column.modelIndex = modelIndex;
	}
}

void createHandle (int index) {
	state |= HANDLE;
	fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	scrolledHandle = OS.gtk_scrolled_window_new (0, 0);
	if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int /*long*/ [] types = getColumnTypes (1);
	modelHandle = OS.gtk_list_store_newv (types.length, types);
	if (modelHandle == 0) error (SWT.ERROR_NO_HANDLES);
	handle = OS.gtk_tree_view_new_with_model (modelHandle);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & SWT.CHECK) != 0) {
		checkRenderer = OS.gtk_cell_renderer_toggle_new ();
		if (checkRenderer == 0) error (SWT.ERROR_NO_HANDLES);
		OS.g_object_ref (checkRenderer);
	}
	createColumn (null, 0);
	OS.gtk_container_add (fixedHandle, scrolledHandle);
	OS.gtk_container_add (scrolledHandle, handle);
	
	int mode = (style & SWT.MULTI) != 0 ? OS.GTK_SELECTION_MULTIPLE : OS.GTK_SELECTION_BROWSE;
	int /*long*/ selectionHandle = OS.gtk_tree_view_get_selection (handle);
	OS.gtk_tree_selection_set_mode (selectionHandle, mode);
	OS.gtk_tree_view_set_headers_visible (handle, false);	
	int hsp = (style & SWT.H_SCROLL) != 0 ? OS.GTK_POLICY_AUTOMATIC : OS.GTK_POLICY_NEVER;
	int vsp = (style & SWT.V_SCROLL) != 0 ? OS.GTK_POLICY_AUTOMATIC : OS.GTK_POLICY_NEVER;
	OS.gtk_scrolled_window_set_policy (scrolledHandle, hsp, vsp);
	if ((style & SWT.BORDER) != 0) OS.gtk_scrolled_window_set_shadow_type (scrolledHandle, OS.GTK_SHADOW_ETCHED_IN);
	if ((style & SWT.VIRTUAL) != 0) {
		/*
		* Feature in GTK. The fixed_height_mode property only exists in GTK 2.3.2 and greater.
		*/
		if (OS.GTK_VERSION >= OS.VERSION (2, 3, 2)) {
			OS.g_object_set (handle, OS.fixed_height_mode, true, 0);
		}
	}
}

void createItem (TableColumn column, int index) {
	if (!(0 <= index && index <= columnCount)) error (SWT.ERROR_INVALID_RANGE);
	if (columnCount == 0) {
		column.handle = OS.gtk_tree_view_get_column (handle, 0);
		OS.gtk_tree_view_column_set_sizing (column.handle, OS.GTK_TREE_VIEW_COLUMN_FIXED);
		OS.gtk_tree_view_column_set_fixed_width (column.handle, 10);
		column.modelIndex = FIRST_COLUMN;
		createRenderers (column.handle, column.modelIndex, true, column.style);
		column.customDraw = firstCustomDraw;
		firstCustomDraw = false;
	} else {
		createColumn (column, index);
	}
	int /*long*/ boxHandle = OS.gtk_hbox_new (false, 3);
	if (boxHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int /*long*/ labelHandle = OS.gtk_label_new_with_mnemonic (null);
	if (labelHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int /*long*/ imageHandle = OS.gtk_image_new ();
	if (imageHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_container_add (boxHandle, imageHandle);
	OS.gtk_container_add (boxHandle, labelHandle);
	OS.gtk_widget_show (boxHandle);
	OS.gtk_widget_show (labelHandle);
	column.labelHandle = labelHandle;
	column.imageHandle = imageHandle;	
	OS.gtk_tree_view_column_set_widget (column.handle, boxHandle);
	int /*long*/ widget = OS.gtk_widget_get_parent (boxHandle);
	while (widget != handle) {
		if (OS.GTK_IS_BUTTON (widget)) {
			column.buttonHandle = widget;
			break;
		}
		widget = OS.gtk_widget_get_parent (widget);
	}
	if (columnCount == columns.length) {
		TableColumn [] newColumns = new TableColumn [columns.length + 4];
		System.arraycopy (columns, 0, newColumns, 0, columns.length);
		columns = newColumns;
	}
	System.arraycopy (columns, index, columns, index + 1, columnCount++ - index);
	columns [index] = column;
	column.setFontDescription (getFontDescription ());
	if (columnCount >= 1) {
		for (int i=0; i<itemCount; i++) {
			TableItem item = items [i];
			if (item != null) {
				Font [] cellFont = item.cellFont;
				if (cellFont != null) {
					Font [] temp = new Font [columnCount];
					System.arraycopy (cellFont, 0, temp, 0, index);
					System.arraycopy (cellFont, index, temp, index+1, columnCount-index-1);
					item.cellFont = temp;
				}
			}
		}
	}
}

void createItem (TableItem item, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	if (itemCount == items.length) {
		int length = drawCount == 0 ? items.length + 4 : Math.max (4, items.length * 3 / 2);
		TableItem [] newItems = new TableItem [length];	
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	item.handle = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	if (item.handle == 0) error (SWT.ERROR_NO_HANDLES);
	if (index == itemCount) {
		OS.gtk_list_store_append (modelHandle, item.handle);
	} else {
		OS.gtk_list_store_insert (modelHandle, item.handle, index);
	}
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
}

void createRenderers (int /*long*/ columnHandle, int modelIndex, boolean check, int columnStyle) {
	OS.gtk_tree_view_column_clear (columnHandle);
	if ((style & SWT.CHECK) != 0 && check) {
		OS.gtk_tree_view_column_pack_start (columnHandle, checkRenderer, false);
		OS.gtk_tree_view_column_add_attribute (columnHandle, checkRenderer, OS.active, CHECKED_COLUMN);
		/*
		* Feature in GTK. The inconsistent property only exists in GTK 2.2.x.
		*/
		if (OS.GTK_VERSION >= OS.VERSION (2, 2, 0)) {
			OS.gtk_tree_view_column_add_attribute (columnHandle, checkRenderer, OS.inconsistent, GRAYED_COLUMN);
		}
		/*
		* Bug in GTK. GTK renders the background on top of the checkbox.
		* This only happens in version 2.2.1 and earlier. The fix is not to set the background.   
		*/
		if (OS.GTK_VERSION > OS.VERSION (2, 2, 1)) {
			OS.gtk_tree_view_column_add_attribute (columnHandle, checkRenderer, OS.cell_background_gdk, BACKGROUND_COLUMN);
		}
	}
	int /*long*/ pixbufRenderer = OS.gtk_cell_renderer_pixbuf_new ();
	if (pixbufRenderer == 0) error (SWT.ERROR_NO_HANDLES);
	int /*long*/ textRenderer = OS.gtk_cell_renderer_text_new ();
	if (textRenderer == 0) error (SWT.ERROR_NO_HANDLES);
	
	/*
	* Feature in GTK.  When a tree view column contains only one activatable
	* cell renderer such as a toggle renderer, mouse clicks anywhere in a cell
	* activate that renderer. The workaround is to set a second  cell renderer
	* to be activatable.
	*/
	if ((style & SWT.CHECK) != 0 && check) {
		OS.g_object_set (pixbufRenderer, OS.mode, OS.GTK_CELL_RENDERER_MODE_ACTIVATABLE, 0);
	}

	/* Set alignment */
	if ((columnStyle & SWT.RIGHT) != 0) {
		OS.g_object_set (textRenderer, OS.xalign, 1f, 0);
		OS.gtk_tree_view_column_pack_start (columnHandle, pixbufRenderer, false);
		OS.gtk_tree_view_column_pack_start (columnHandle, textRenderer, true);
		OS.gtk_tree_view_column_set_alignment (columnHandle, 1f);
	} else if ((columnStyle & SWT.CENTER) != 0) {
		OS.g_object_set (textRenderer, OS.xalign, 0.5f, 0);
		OS.gtk_tree_view_column_pack_start (columnHandle, pixbufRenderer, false);
		OS.gtk_tree_view_column_pack_end (columnHandle, textRenderer, true);
		OS.gtk_tree_view_column_set_alignment (columnHandle, 0.5f);
	} else {
		OS.gtk_tree_view_column_pack_start (columnHandle, pixbufRenderer, false);
		OS.gtk_tree_view_column_pack_start (columnHandle, textRenderer, true);
		OS.gtk_tree_view_column_set_alignment (columnHandle, 0f);
	}

	/* Add attributes */
	OS.gtk_tree_view_column_add_attribute (columnHandle, pixbufRenderer, OS.pixbuf, modelIndex + CELL_PIXBUF);
	/*
	 * Bug on GTK. Gtk renders the background on top of the pixbuf.
	 * This only happens in version 2.2.1 and earlier. The fix is not to set the background.   
	 */
	if (OS.GTK_VERSION > OS.VERSION (2, 2, 1)) {
		OS.gtk_tree_view_column_add_attribute (columnHandle, pixbufRenderer, OS.cell_background_gdk, BACKGROUND_COLUMN);
		OS.gtk_tree_view_column_add_attribute (columnHandle, textRenderer, OS.background_gdk, BACKGROUND_COLUMN);
	}
	OS.gtk_tree_view_column_add_attribute (columnHandle, textRenderer, OS.text, modelIndex + CELL_TEXT);
	OS.gtk_tree_view_column_add_attribute (columnHandle, textRenderer, OS.foreground_gdk, FOREGROUND_COLUMN);
	OS.gtk_tree_view_column_add_attribute (columnHandle, textRenderer, OS.font_desc, FONT_COLUMN);
	
	boolean customDraw = firstCustomDraw;
	if (columnCount != 0) {
		for (int i=0; i<columnCount; i++) {
			if (columns [i].handle == columnHandle) {
				customDraw = columns [i].customDraw;
				break;
			}
		}
	}
	if ((style & SWT.VIRTUAL) != 0 || customDraw) {
		OS.gtk_tree_view_column_set_cell_data_func (columnHandle, textRenderer, display.textCellDataProc, handle, 0);
		OS.gtk_tree_view_column_set_cell_data_func (columnHandle, pixbufRenderer, display.pixbufCellDataProc, handle, 0);
	}
}

void createWidget (int index) {
	super.createWidget (index);
	items = new TableItem [4];
	columns = new TableColumn [4];
	itemCount = columnCount = 0;
}

GdkColor defaultBackground () {
	return display.COLOR_LIST_BACKGROUND;
}

GdkColor defaultForeground () {
	return display.COLOR_LIST_FOREGROUND;
}

void deregister() {
	super.deregister ();
	display.removeWidget (OS.gtk_tree_view_get_selection (handle));
	if (checkRenderer != 0) display.removeWidget (checkRenderer);
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
	if (index < 0 || index >= itemCount) return;
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.gtk_tree_selection_unselect_iter (selection, _getItem (index).handle);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
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
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	for (int index=start; index<=end; index++) {
		if (index < 0 || index >= itemCount) continue;
		OS.gtk_tree_selection_unselect_iter (selection, _getItem (index).handle);
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
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
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	for (int i=0; i<indices.length; i++) {
		int index = indices[i];
		if (index < 0 || index >= itemCount) continue;
		OS.gtk_tree_selection_unselect_iter (selection, _getItem (index).handle);
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
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
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.gtk_tree_selection_unselect_all (selection);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
}

void destroyItem (TableColumn column) {
	int index = 0;
	while (index < columnCount) {
		if (columns [index] == column) break;
		index++;
	}
	if (index == columnCount) return;
	int /*long*/ columnHandle = column.handle;
	if (columnCount == 1) {
		firstCustomDraw = column.customDraw;
	}
	System.arraycopy (columns, index + 1, columns, index, --columnCount - index);
	columns [columnCount] = null;
	column.deregister ();
	OS.gtk_tree_view_remove_column (handle, columnHandle);
	if (columnCount == 0) {
		int /*long*/ oldModel = modelHandle;
		int /*long*/[] types = getColumnTypes (1);
		int /*long*/ newModel = OS.gtk_list_store_newv (types.length, types);
		if (newModel == 0) error (SWT.ERROR_NO_HANDLES);
		int /*long*/ [] ptr = new int /*long*/ [1];
		for (int i=0; i<itemCount; i++) {
			int /*long*/ newItem = OS.g_malloc (OS.GtkTreeIter_sizeof ());
			if (newItem == 0) error (SWT.ERROR_NO_HANDLES);
			OS.gtk_list_store_append (newModel, newItem);
			TableItem item = items [i];
			if (item != null) {
				int /*long*/ oldItem = item.handle;
				for (int j=0; j<FIRST_COLUMN; j++) {
					OS.gtk_tree_model_get (oldModel, oldItem, j, ptr, -1);
					OS.gtk_list_store_set (newModel, newItem, j, ptr [0], -1);
				}
				OS.gtk_tree_model_get (oldModel, oldItem, column.modelIndex + CELL_PIXBUF, ptr, -1);
				OS.gtk_list_store_set (newModel, newItem, FIRST_COLUMN + CELL_PIXBUF, ptr [0], -1);
				OS.gtk_tree_model_get (oldModel, oldItem, column.modelIndex + CELL_TEXT, ptr, -1);
				OS.gtk_list_store_set (newModel, newItem, FIRST_COLUMN + CELL_TEXT, ptr [0], -1);
				OS.g_free (ptr [0]);
				OS.gtk_tree_model_get (oldModel, oldItem, column.modelIndex + CELL_FOREGROUND, ptr, -1);
				OS.gtk_list_store_set (newModel, newItem, FIRST_COLUMN + CELL_FOREGROUND, ptr [0], -1);
				OS.gtk_tree_model_get (oldModel, oldItem, column.modelIndex + CELL_BACKGROUND, ptr, -1);
				OS.gtk_list_store_set (newModel, newItem, FIRST_COLUMN + CELL_BACKGROUND, ptr [0], -1);
				OS.gtk_tree_model_get (oldModel, oldItem, column.modelIndex + CELL_FONT, ptr, -1);
				OS.gtk_list_store_set (newModel, newItem, FIRST_COLUMN + CELL_FONT, ptr [0], -1);
				OS.gtk_list_store_remove (oldModel, oldItem);
				OS.g_free (oldItem);
				item.handle = newItem;
			} else {
				OS.g_free (newItem);
			}
		}
		OS.gtk_tree_view_set_model (handle, newModel);
		OS.g_object_unref (oldModel);
		modelHandle = newModel;
		createColumn (null, 0);
	} else {
		for (int i=0; i<itemCount; i++) {
			TableItem item = items [i];
			if (item != null) {
				int /*long*/ iter = item.handle;
				int modelIndex = column.modelIndex;
				OS.gtk_list_store_set (modelHandle, iter, modelIndex + CELL_PIXBUF, 0, -1);
				OS.gtk_list_store_set (modelHandle, iter, modelIndex + CELL_TEXT, 0, -1);
				OS.gtk_list_store_set (modelHandle, iter, modelIndex + CELL_FOREGROUND, 0, -1);
				OS.gtk_list_store_set (modelHandle, iter, modelIndex + CELL_BACKGROUND, 0, -1);
				OS.gtk_list_store_set (modelHandle, iter, modelIndex + CELL_FONT, 0, -1);
				
				Font [] cellFont = item.cellFont;
				if (cellFont != null) {
					if (columnCount == 1) {
						item.cellFont = null;
					} else {
						Font [] temp = new Font [columnCount - 1];
						System.arraycopy (cellFont, 0, temp, 0, index);
						System.arraycopy (cellFont, index + 1, temp, index, columnCount - 1 - index);
						item.cellFont = temp;
					}
				}
			}
		}
		if (index == 0) {
			TableColumn checkColumn = columns [0];
			createRenderers (checkColumn.handle, checkColumn.modelIndex, true, checkColumn.style);
		}
	}
	column.handle = column.buttonHandle = column.labelHandle = 0;
	column.imageHandle = 0;
}

void destroyItem (TableItem item) {
	int index = 0;
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	if (index == itemCount) return;
	int /*long*/ itemHandle = item.handle;
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.gtk_list_store_remove (modelHandle, itemHandle);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.g_free (itemHandle);
	item.handle = 0;
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	lastDataIndex = -1;
	if (itemCount == 0) resetCustomDraw ();
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

int /*long*/[] getColumnTypes (int columnCount) {
	int /*long*/[] types = new int /*long*/ [FIRST_COLUMN + (columnCount * CELL_TYPES)];
	// per row data
	types [CHECKED_COLUMN] = OS.G_TYPE_BOOLEAN ();
	types [GRAYED_COLUMN] = OS.G_TYPE_BOOLEAN ();
	types [FOREGROUND_COLUMN] = OS.GDK_TYPE_COLOR ();
	types [BACKGROUND_COLUMN] = OS.GDK_TYPE_COLOR ();
	types [FONT_COLUMN] = OS.PANGO_TYPE_FONT_DESCRIPTION ();
	// per cell data
	for (int i=FIRST_COLUMN; i<types.length; i+=CELL_TYPES) {
		types [i + CELL_PIXBUF] = OS.GDK_TYPE_PIXBUF ();
		types [i + CELL_TEXT] = OS.G_TYPE_STRING ();
		types [i + CELL_FOREGROUND] = OS.GDK_TYPE_COLOR ();
		types [i + CELL_BACKGROUND] = OS.GDK_TYPE_COLOR ();
		types [i + CELL_FONT] = OS.PANGO_TYPE_FONT_DESCRIPTION ();
	}
	return types;
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

TableItem getFocusItem () {
	int /*long*/ [] path = new int /*long*/ [1];
	OS.gtk_tree_view_get_cursor (handle, path, null);
	if (path [0] == 0) return null;
	TableItem item = null;
	int /*long*/ indices = OS.gtk_tree_path_get_indices (path [0]);
	if (indices != 0) {
		int [] index = new int []{-1};
		OS.memmove (index, indices, 4);
		item = _getItem (index [0]); 
	}
	OS.gtk_tree_path_free (path [0]);
	return item;
} 

GdkColor getForegroundColor () {
	return getTextColor ();
}

/**
 * Returns the width in pixels of a grid line.
 *
 * @return the width of a grid line in pixels
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
	if (!OS.gtk_tree_view_get_headers_visible (handle)) return 0;
	OS.gtk_widget_realize (handle);
	int /*long*/ fixedWindow = OS.GTK_WIDGET_WINDOW (fixedHandle);
	int /*long*/ binWindow = OS.gtk_tree_view_get_bin_window (handle);
	int [] binY = new int [1];
	OS.gdk_window_get_origin (binWindow, null, binY);
	int [] fixedY = new int [1];
	OS.gdk_window_get_origin (fixedWindow, null, fixedY);
	return binY [0] - fixedY [0];
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
	return OS.gtk_tree_view_get_headers_visible (handle);
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
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	return _getItem (index);
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
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TableItem getItem (Point point) {
	checkWidget();
	int /*long*/ [] path = new int /*long*/ [1];
	int clientX = point.x - getBorderWidth ();
	int clientY = point.y - getHeaderHeight ();
	OS.gtk_widget_realize (handle);
	if (!OS.gtk_tree_view_get_path_at_pos (handle, clientX, clientY, path, null, null, null)) return null;
	if (path [0] == 0) return null;
	int /*long*/ indices = OS.gtk_tree_path_get_indices (path [0]);
	TableItem item = null;
	if (indices != 0) {
		int [] index = new int [1];
		OS.memmove (index, indices, 4);
		item = _getItem (index [0]);
	}
	OS.gtk_tree_path_free (path [0]);
	return item;
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
	if (itemCount == 0) {
		int /*long*/ column = OS.gtk_tree_view_get_column (handle, 0);
		int [] w = new int [1], h = new int [1];
		OS.gtk_tree_view_column_cell_get_size (column, null, null, null, w, h);
		return h [0];
	} else {
		int height = 0;
		int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
		OS.gtk_tree_model_get_iter_first (modelHandle, iter);
		int columnCount = Math.max (1, this.columnCount);
		for (int i=0; i<columnCount; i++) {
			int /*long*/ column = OS.gtk_tree_view_get_column (handle, i);
			OS.gtk_tree_view_column_cell_set_cell_data (column, modelHandle, iter, false, false);
			int [] w = new int [1], h = new int [1];
			OS.gtk_tree_view_column_cell_get_size (column, null, null, null, w, h);
			height = Math.max (height, h [0]);
		}
		OS.g_free (iter);
		return height;
	}
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
	if ((style & SWT.VIRTUAL) != 0) {
		for (int i=0; i<itemCount; i++) {
			result [i] = _getItem (i);
		}
	} else {
		System.arraycopy (items, 0, result, 0, itemCount);
	}
	return result;
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
	return OS.gtk_tree_view_get_rules_hint (handle);
}

int /*long*/ getPixbufRenderer (int /*long*/ column) {
	int /*long*/ list = OS.gtk_tree_view_column_get_cell_renderers (column);
	if (list == 0) return 0;
	int count = OS.g_list_length (list);
	int /*long*/ pixbufRenderer = 0;
	int i = 0;
	while (i < count) {
		int /*long*/ renderer = OS.g_list_nth_data (list, i);
		 if (OS.GTK_IS_CELL_RENDERER_PIXBUF (renderer)) {
			pixbufRenderer = renderer;
			break;
		}
		i++;
	}
	OS.g_list_free (list);
	return pixbufRenderer;
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
	display.treeSelectionLength  = 0;
	display.treeSelection = new int [itemCount];
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	OS.gtk_tree_selection_selected_foreach (selection, display.treeSelectionProc, handle);
	TableItem [] result = new TableItem [display.treeSelectionLength];
	for (int i=0; i<result.length; i++) result [i] = _getItem (display.treeSelection [i]);
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
 */
public int getSelectionCount () {
	checkWidget();
	display.treeSelectionLength = 0;
	display.treeSelection = null;
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
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
 */
public int getSelectionIndex () {
	checkWidget();
	display.treeSelectionLength  = 0;
	display.treeSelection = new int [itemCount];
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
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
 */
public int [] getSelectionIndices () {
	checkWidget();
	display.treeSelectionLength  = 0;
	display.treeSelection = new int [itemCount];
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	OS.gtk_tree_selection_selected_foreach (selection, display.treeSelectionProc, handle);
	if (display.treeSelectionLength == display.treeSelection.length) return display.treeSelection;
	int [] result = new int [display.treeSelectionLength];
	System.arraycopy (display.treeSelection, 0, result, 0, display.treeSelectionLength);
	return result;
}

int /*long*/ getTextRenderer (int /*long*/ column) {
	int /*long*/ list = OS.gtk_tree_view_column_get_cell_renderers (column);
	if (list == 0) return 0;
	int count = OS.g_list_length (list);
	int /*long*/ textRenderer = 0;
	int i = 0;
	while (i < count) {
		int /*long*/ renderer = OS.g_list_nth_data (list, i);
		 if (OS.GTK_IS_CELL_RENDERER_TEXT (renderer)) {
			textRenderer = renderer;
			break;
		}
		i++;
	}
	OS.g_list_free (list);
	return textRenderer;
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
	int /*long*/ [] path = new int /*long*/ [1];
	OS.gtk_widget_realize (handle);
	if (!OS.gtk_tree_view_get_path_at_pos (handle, 1, 1, path, null, null, null)) return 0;
	if (path [0] == 0) return 0;
	int /*long*/ indices = OS.gtk_tree_path_get_indices (path[0]);
	int[] index = new int [1];
	if (indices != 0) OS.memmove (index, indices, 4);
	OS.gtk_tree_path_free (path [0]);
	return index [0];
}

int /*long*/ gtk_button_press_event (int /*long*/ widget, int /*long*/ event) {
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, event, GdkEventButton.sizeof);
	if (gdkEvent.window != OS.gtk_tree_view_get_bin_window (handle)) return 0;
	int border = getBorderWidth ();
	int headerHeight = getHeaderHeight ();
	gdkEvent.x += border;
	gdkEvent.y += headerHeight;
	OS.memmove (event, gdkEvent, GdkEventButton.sizeof);
	int /*long*/ result = super.gtk_button_press_event (widget, event);
	gdkEvent.x -= border;
	gdkEvent.y -= headerHeight;
	OS.memmove (event, gdkEvent, GdkEventButton.sizeof);
	if (result != 0) return result;
	/*
	* Feature in GTK.  In a multi-select table view, when multiple items are already
	* selected, the selection state of the item is toggled and the previous selection 
	* is cleared. This is not the desired behaviour when bringing up a popup menu.
	* Also, when an item is reselected with the right button, the tree view issues
	* an unwanted selection event. The workaround is to detect that case and not
	* run the default handler when the item is already part of the current selection.
	*/
	int button = gdkEvent.button;
	if (button == 3 && gdkEvent.type == OS.GDK_BUTTON_PRESS) {
		int /*long*/ [] path = new int /*long*/ [1];
		if (OS.gtk_tree_view_get_path_at_pos (handle, (int)gdkEvent.x, (int)gdkEvent.y, path, null, null, null)) {
			if (path [0] != 0) {
				int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
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
		int /*long*/ [] path = new int /*long*/ [1];
		if (OS.gtk_tree_view_get_path_at_pos (handle, (int)gdkEvent.x, (int)gdkEvent.y, path, null, null, null)) {
			if (path [0] != 0) {
				int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
				OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				OS.gtk_tree_view_set_cursor (handle, path [0], 0, false);
				OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				OS.gtk_tree_path_free (path [0]);
			}
		}
	}
	/*
	* Bug in GTK. GTK segments fault, if the GtkTreeView widget is
	* not in focus and all items in the widget are disposed before
	* it finishes processing a button press.  The fix is to give
	* focus to the widget before it starts processing the event.
	*/
	if (!OS.GTK_WIDGET_HAS_FOCUS (handle)) {
		OS.gtk_widget_grab_focus (handle);
	}
	return result;
}

int /*long*/ gtk_button_release_event (int /*long*/ widget, int /*long*/ event) {
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, event, GdkEventButton.sizeof);
	if (gdkEvent.window != OS.gtk_tree_view_get_bin_window (handle)) return 0;
	int border = getBorderWidth ();
	int headerHeight = getHeaderHeight ();
	gdkEvent.x += border;
	gdkEvent.y += headerHeight;
	OS.memmove (event, gdkEvent, GdkEventButton.sizeof);
	int /*long*/ result = super.gtk_button_release_event (widget, event);
	gdkEvent.x -= border;
	gdkEvent.y -= headerHeight;
	OS.memmove (event, gdkEvent, GdkEventButton.sizeof);
	return result;
}

int /*long*/ gtk_changed (int /*long*/ widget) {
	TableItem item = getFocusItem ();
	if (item != null) {
		Event event = new Event ();
		event.item = item; 
		postEvent (SWT.Selection, event);
	}
	return 0;
}

int /*long*/ gtk_key_press_event (int /*long*/ widget, int /*long*/ eventPtr) {
	int /*long*/ result = super.gtk_key_press_event (widget, eventPtr);
	if (result != 0) return result;
	if (OS.GTK_VERSION < OS.VERSION (2, 2 ,0)) {
		/*
		* Feature in GTK 2.0.x.  When an item is default selected using
		* the return key, GTK does not issue notification. The fix is
		* to issue this notification when the return key is pressed.
		*/
		GdkEventKey keyEvent = new GdkEventKey ();
		OS.memmove (keyEvent, eventPtr, GdkEventKey.sizeof);
		int key = keyEvent.keyval;
		switch (key) {
			case OS.GDK_Return:
			case OS.GDK_KP_Enter: {
				Event event = new Event ();
				event.item = getFocusItem (); 
				postEvent (SWT.DefaultSelection, event);
				break;
			}
		}
	}
	return result;
}

int /*long*/ gtk_motion_notify_event (int /*long*/ widget, int /*long*/ event) {
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, event, GdkEventButton.sizeof);
	if (gdkEvent.window != OS.gtk_tree_view_get_bin_window (handle)) return 0;
	int border = getBorderWidth ();
	int headerHeight = getHeaderHeight ();
	gdkEvent.x += border;
	gdkEvent.y += headerHeight;
	OS.memmove (event, gdkEvent, GdkEventButton.sizeof);
	int /*long*/ result = super.gtk_motion_notify_event (widget, event);
	gdkEvent.x -= border;
	gdkEvent.y -= headerHeight;
	OS.memmove (event, gdkEvent, GdkEventButton.sizeof);
	return result;
}

int /*long*/ gtk_row_activated (int /*long*/ tree, int /*long*/ path, int /*long*/ column) {
	TableItem item = null;
	int /*long*/ indices = OS.gtk_tree_path_get_indices (path);
	if (indices != 0) {
		int [] index = new int []{-1};
		OS.memmove (index, indices, 4);
		item = _getItem (index [0]); 
	}
	Event event = new Event ();
	event.item = item;
	postEvent (SWT.DefaultSelection, event);
	return 0;
}

int /*long*/ gtk_toggled (int /*long*/ renderer, int /*long*/ pathStr) {
	int /*long*/ path = OS.gtk_tree_path_new_from_string (pathStr);
	if (path == 0) return 0;
	int /*long*/ indices = OS.gtk_tree_path_get_indices (path);
	if (indices != 0) {
		int [] index = new int [1];
		OS.memmove (index, indices, 4);
		TableItem item = _getItem (index [0]);
		item.setChecked (!item.getChecked ());
		Event event = new Event ();
		event.detail = SWT.CHECK;
		event.item = item;
		postEvent (SWT.Selection, event);
	}
	OS.gtk_tree_path_free (path);
	return 0;
}

void hookEvents () {
	super.hookEvents ();
	int /*long*/ selection = OS.gtk_tree_view_get_selection(handle);
	OS.g_signal_connect (selection, OS.changed, display.windowProc2, CHANGED);
	OS.g_signal_connect (handle, OS.row_activated, display.windowProc4, ROW_ACTIVATED);
	if (checkRenderer != 0) {
		OS.g_signal_connect (checkRenderer, OS.toggled, display.windowProc3, TOGGLED);
	}
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
	if (1 <= lastIndexOf && lastIndexOf < itemCount - 1) {
		if (items [lastIndexOf] == item) return lastIndexOf;
		if (items [lastIndexOf + 1] == item) return ++lastIndexOf;
		if (items [lastIndexOf - 1] == item) return --lastIndexOf;
	}
	if (lastIndexOf < itemCount / 2) {
		for (int i=0; i<itemCount; i++) {
			if (items [i] == item) return lastIndexOf = i;
		}
	} else {
		for (int i=itemCount - 1; i>=0; --i) {
			if (items [i] == item) return lastIndexOf = i;
		}
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
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	byte [] buffer = Converter.wcsToMbcs (null, Integer.toString (index), true);
	int /*long*/ path = OS.gtk_tree_path_new_from_string (buffer);
	boolean answer = OS.gtk_tree_selection_path_is_selected (selection, path);
	OS.gtk_tree_path_free (path);
	return answer;
}

boolean mnemonicHit (char key) {
	for (int i=0; i<columnCount; i++) {
		int /*long*/ labelHandle = columns [i].labelHandle;
		if (labelHandle != 0 && mnemonicHit (labelHandle, key)) return true;
	}
	return false;
}

boolean mnemonicMatch (char key) {
	for (int i=0; i<columnCount; i++) {
		int /*long*/ labelHandle = columns [i].labelHandle;
		if (labelHandle != 0 && mnemonicMatch (labelHandle, key)) return true;
	}
	return false;
}

int /*long*/ paintWindow () {
	OS.gtk_widget_realize (handle);
	return OS.gtk_tree_view_get_bin_window (handle);
}

int /*long*/ pixbufCellDataProc (int /*long*/ tree_column, int /*long*/ cell, int /*long*/ tree_model, int /*long*/ iter, int /*long*/ data) {
	if (cell == ignorePixbufCell) return 0;
	int modelIndex = -1;
	boolean customDraw = false;
	if (columnCount == 0) {
		modelIndex = Table.FIRST_COLUMN;
		customDraw = firstCustomDraw;
	} else {
		for (int i = 0; i < columns.length; i++) {
			if (columns [i] != null && columns [i].handle == tree_column) {
				modelIndex = columns [i].modelIndex;
				customDraw = columns [i].customDraw;
				break;
			}
		}
	}
	if (modelIndex == -1) return 0;
	boolean setData = setCellData (tree_model, iter);
	int /*long*/ [] ptr = new int /*long*/ [1];
	if (setData) {
		OS.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_PIXBUF, ptr, -1);
		OS.g_object_set(cell, OS.pixbuf, ptr[0], 0);
		ptr = new int /*long*/ [1];
	}
	if (customDraw) {
		/*
		* Bug on GTK. Gtk renders the background on top of the checkbox and pixbuf.
		* This only happens in version 2.2.1 and earlier. The fix is not to set the background.   
		*/
		if (OS.GTK_VERSION > OS.VERSION (2, 2, 1)) {
			OS.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_BACKGROUND, ptr, -1);
			if (ptr [0] != 0) {
				OS.g_object_set(cell, OS.cell_background_gdk, ptr[0], 0);
			}
		}
	}
	if (setData) {
		ignorePixbufCell = cell;
		setScrollWidth (tree_column, iter);
		ignorePixbufCell = 0;
	}
	return 0;
}

void register () {
	super.register ();
	display.addWidget (OS.gtk_tree_view_get_selection (handle), this);
	if (checkRenderer != 0) display.addWidget (checkRenderer, this);
}

void releaseWidget () {
	for (int i=0; i<columnCount; i++) {
		TableColumn column = columns [i];
		if (!column.isDisposed ()) column.releaseResources ();
	}
	columns = null;
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
		if (item != null && !item.isDisposed ()) item.releaseResources ();
	}
	items = null;
	super.releaseWidget ();
	if (modelHandle != 0) OS.g_object_unref (modelHandle);
	modelHandle = 0;
	if (checkRenderer != 0) OS.g_object_unref (checkRenderer);
	checkRenderer = 0;
	if (imageList != null) {
		imageList.dispose ();
		imageList = null;
	}
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
	TableItem item = items [index];
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	if (item != null && !item.isDisposed ()) {
		OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.gtk_list_store_remove (modelHandle, item.handle);
		OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		item.releaseResources ();
	} else {
		int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
		OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
		OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.gtk_list_store_remove (modelHandle, iter);
		OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.g_free (iter);
	}
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
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
	if (!(0 <= start && start <= end && end < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, start);
	int index = start;
	while (index <= end) {
		TableItem item = items [index];
		OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		OS.gtk_list_store_remove (modelHandle, iter);
		OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		if (item != null && !item.isDisposed ()) item.releaseResources ();
		index++;
	}
	OS.g_free (iter);
	System.arraycopy (items, index, items, start, itemCount - index);
	for (int i=itemCount-(index-start); i<itemCount; i++) items [i] = null;
	itemCount = itemCount - (index - start);
}

/**
 * Removes the items from the receiver's list at the given
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
	if (!(0 <= start && start <= end && end < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	int last = -1;
	int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	for (int i=0; i<newIndices.length; i++) {
		int index = newIndices [i];
		if (index != last) {
			TableItem item = items [index];
			if (item != null && !item.isDisposed ()) {
				OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				OS.gtk_list_store_remove (modelHandle, item.handle);
				OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				item.releaseResources ();
			} else {
				OS.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
				OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				OS.gtk_list_store_remove (modelHandle, iter);
				OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
			} 
			System.arraycopy (items, index + 1, items, index, --itemCount - index);
			items [itemCount] = null;
			last = index;
		}
	}
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

	/*
	* Bug in GTK.  In version 2.3.2, when the property fixed-height-mode
	* is set and there are items in the list, OS.gtk_list_store_clear()
	* segment faults.  The fix is to create a new empty model instead.
	*/
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
//	OS.gtk_list_store_clear (modelHandle);
	int /*long*/ oldModel = modelHandle;
	int /*long*/[] types = getColumnTypes (Math.max (1,columnCount));
	int /*long*/ newModel = OS.gtk_list_store_newv (types.length, types);
	if (newModel == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_tree_view_set_model (handle, newModel);
	OS.g_object_unref (oldModel);
	modelHandle = newModel;
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);

	int index = itemCount - 1;
	while (index >= 0) {
		TableItem item = items [index];
		if (item != null && !item.isDisposed ()) item.releaseResources ();
		--index;
	}
	items = new TableItem [4];
	itemCount = 0;
	resetCustomDraw ();
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
 * @see #addSelectionListener(SelectionListener)
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

void resetCustomDraw () {
	if ((style & SWT.VIRTUAL) != 0) return;
	int end = Math.max (1, columnCount);
	for (int i=0; i<end; i++) {
		boolean customDraw = columnCount != 0 ? columns [i].customDraw : firstCustomDraw;
		if (customDraw) {
			int /*long*/ column = OS.gtk_tree_view_get_column (handle, i);
			int /*long*/ textRenderer = getTextRenderer (column);
			OS.gtk_tree_view_column_set_cell_data_func (column, textRenderer, 0, 0, 0);
			if (columnCount != 0) columns [i].customDraw = false;
		}
	}
	firstCustomDraw = false;
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
	if (!(0 <= index && index < itemCount))  return;
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	TableItem item = _getItem (index);
	OS.gtk_tree_selection_select_iter (selection, item.handle);
	if ((style & SWT.SINGLE) != 0) {
		int /*long*/ path = OS.gtk_tree_model_get_path (modelHandle, item.handle);
		OS.gtk_tree_view_set_cursor (handle, path, 0, false);
		OS.gtk_tree_path_free (path);
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
}

/**
 * Selects the items in the range specified by the given zero-relative
 * indices in the receiver. The range of indices is inclusive.
 * The current selection is not cleared before the new items are selected.
 * <p>
 * If an item in the given range is not selected, it is selected.
 * If an item in the given range was already selected, it remains selected.
 * Indices that are out of range are ignored and no items will be selected
 * if start is greater than end.
 * If the receiver is single-select and there is more than one item in the
 * given range, then all indices are ignored.
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Table#setSelection(int,int)
 */
public void select (int start, int end) {
	checkWidget ();
	if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) return;
	if (itemCount == 0 || start >= itemCount) return;
	start = Math.max (0, start);
	end = Math.min (end, itemCount - 1);
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	for (int index=start; index<=end; index++) {
		TableItem item = _getItem (index);
		OS.gtk_tree_selection_select_iter (selection, item.handle);
		if ((style & SWT.SINGLE) != 0) {
			int /*long*/ path = OS.gtk_tree_model_get_path (modelHandle, item.handle);
			OS.gtk_tree_view_set_cursor (handle, path, 0, false);
			OS.gtk_tree_path_free (path);
		}
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * The current selection is not cleared before the new items are selected.
 * <p>
 * If the item at a given index is not selected, it is selected.
 * If the item at a given index was already selected, it remains selected.
 * Indices that are out of range and duplicate indices are ignored.
 * If the receiver is single-select and multiple indices are specified,
 * then all indices are ignored.
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
 * 
 * @see Table#setSelection(int[])
 */
public void select (int [] indices) {
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = indices.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	for (int i=0; i<length; i++) {
		int index = indices [i];
		if (!(0 <= index && index < itemCount)) continue;
		TableItem item = _getItem (index);
		OS.gtk_tree_selection_select_iter (selection, item.handle);
		if ((style & SWT.SINGLE) != 0) {
			int /*long*/ path = OS.gtk_tree_model_get_path (modelHandle, item.handle);
			OS.gtk_tree_view_set_cursor (handle, path, 0, false);
			OS.gtk_tree_path_free (path);
		}
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
}

/**
 * Selects all of the items in the receiver.
 * <p>
 * If the receiver is single-select, do nothing.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void selectAll () {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return;
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
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
	if (!(0 <= index && index < itemCount))  return;
	TableItem item = _getItem (index);
	int /*long*/ path = OS.gtk_tree_model_get_path (modelHandle, item.handle);
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.gtk_tree_view_set_cursor (handle, path, 0, false);
	/*
	* Bug in GTK. For some reason, when an event loop is run from
	* within a key pressed handler and a dialog is displayed that
	* contains a GtkTreeView,  gtk_tree_view_set_cursor() does
	* not set the cursor or select the item.  The fix is to select the
	* item with gtk_tree_selection_select_iter() as well.
	* 
	* NOTE: This happens in GTK 2.2.1 and is fixed in GTK 2.2.4.
	*/
	OS.gtk_tree_selection_select_iter (selection, item.handle);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.gtk_tree_path_free (path);
}

void setBackgroundColor (GdkColor color) {
	super.setBackgroundColor (color);
	OS.gtk_widget_modify_base (handle, 0, color);
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	int result = super.setBounds (x, y, width, height, move, resize);
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

boolean setCellData(int /*long*/ tree_model, int /*long*/ iter) {
	if ((style & SWT.VIRTUAL) != 0) {
		int [] index = new int [1];
		int /*long*/ path = OS.gtk_tree_model_get_path (tree_model, iter);
		OS.memmove (index, OS.gtk_tree_path_get_indices (path), 4);
		OS.gtk_tree_path_free (path);
		if (lastDataIndex != index [0]) {
			lastDataIndex = lastIndexOf = index [0];
			TableItem item = _getItem (index [0]);
			if (!item.cached) return checkData (item);
		}
	}
	return false;
}

void setFontDescription (int /*long*/ font) {
	super.setFontDescription (font);
	TableColumn[] columns = getColumns ();
	for (int i = 0; i < columns.length; i++) {
		if (columns[i] != null) {
			columns[i].setFontDescription (font);
		}
	}
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
 * @param show the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHeaderVisible (boolean show) {
	checkWidget ();
	OS.gtk_tree_view_set_headers_visible (handle, show);
}

/**
 * Sets the number of items contained in the receiver.
 *
 * @param count the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public void setItemCount (int count) {
	checkWidget ();
	count = Math.max (0, count);
	if (count == itemCount) return;
	boolean isVirtual = (style & SWT.VIRTUAL) != 0;
	if (!isVirtual) setRedraw (false);
	remove (count, itemCount - 1);
	int length = Math.max (4, (count + 3) / 4 * 4);
	TableItem [] newItems = new TableItem [length];
	System.arraycopy (items, 0, newItems, 0, itemCount);
	items = newItems;
	if (isVirtual) {
		int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
		if (iter == 0) error (SWT.ERROR_NO_HANDLES);
		for (int i=itemCount; i<count; i++) {
			OS.gtk_list_store_append (modelHandle, iter);
		}
		OS.g_free (iter);
		itemCount = count;
	} else {
		for (int i=itemCount; i<count; i++) {
			items [i] = new TableItem (this, SWT.NONE, i, true);
		}
	}
	if (!isVirtual) setRedraw (true);
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
 * @param show the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLinesVisible (boolean show) {
	checkWidget();
	OS.gtk_tree_view_set_rules_hint (handle, show);
}

public void setRedraw (boolean redraw) {
	checkWidget();
	super.setRedraw (redraw);
	if (redraw && drawCount == 0) {
		/* Resize the item array to match the item count */
		if (items.length > 4 && items.length - itemCount > 3) {
			int length = Math.max (4, (itemCount + 3) / 4 * 4);
			TableItem [] newItems = new TableItem [length];
			System.arraycopy (items, 0, newItems, 0, itemCount);
			items = newItems;
		}
	}
}

void setScrollWidth (int /*long*/ column, int /*long*/ iter) {
	if (columnCount != 0) return;
	int width = OS.gtk_tree_view_column_get_width (column);
	int itemWidth = calculateWidth (column, iter);
	if (width < itemWidth) {
		OS.gtk_tree_view_column_set_fixed_width (column, itemWidth);
	}
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
	checkWidget ();
	deselectAll ();
	selectFocusIndex (index);
	showSelection ();
}

/**
 * Selects the items in the range specified by the given zero-relative
 * indices in the receiver. The range of indices is inclusive.
 * The current selection is cleared before the new items are selected.
 * <p>
 * Indices that are out of range are ignored and no items will be selected
 * if start is greater than end.
 * If the receiver is single-select and there is more than one item in the
 * given range, then all indices are ignored.
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
	checkWidget ();
	deselectAll();
	if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) return;
	if (itemCount == 0 || start >= itemCount) return;
	start = Math.max (0, start);
	end = Math.min (end, itemCount - 1);
	selectFocusIndex (start);
	if ((style & SWT.MULTI) != 0) {
		select (start, end);
	}
	showSelection ();
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * The current selection is cleared before the new items are selected.
 * <p>
 * Indices that are out of range and duplicate indices are ignored.
 * If the receiver is single-select and multiple indices are specified,
 * then all indices are ignored.
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
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	deselectAll ();
	int length = indices.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	selectFocusIndex (indices [0]);
	if ((style & SWT.MULTI) != 0) {
		select (indices);
	}
	showSelection ();
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selection is cleared before the new items are selected.
 * <p>
 * Items that are not in the receiver are ignored.
 * If the receiver is single-select and multiple items are specified,
 * then all items are ignored.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of items is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if one of the items has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int[])
 * @see Table#setSelection(int[])
 */
public void setSelection (TableItem [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	deselectAll ();
	int length = items.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	boolean first = true;
	for (int i = 0; i < length; i++) {
		int index = indexOf (items [i]);
		if (index != -1) {
			if (first) {
				first = false;
				selectFocusIndex (index);
			} else {
				select (index);
			}
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
	if (!(0 <= index && index < itemCount)) return;
	// FIXME - For some reason, sometimes the tree scrolls to the wrong place
	int /*long*/ path = OS.gtk_tree_model_get_path (modelHandle, _getItem (index).handle);
	OS.gtk_tree_view_scroll_to_cell (handle, path, 0, true, 0, 0);
	OS.gtk_tree_path_free (path);
}

/**
 * Shows the column.  If the column is already showing in the receiver,
 * this method simply returns.  Otherwise, the columns are scrolled until
 * the column is visible.
 *
 * @param column the column to be shown
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
 * @since 3.0
 */
public void showColumn (TableColumn column) {
	checkWidget ();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (column.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (column.parent != this) return;
	/*
	* This code is intentionally commented. According to the
	* documentation, gtk_tree_view_scroll_to_cell should scroll the
	* minimum amount to show the column but instead it scrolls strangely.
	*/
	//OS.gtk_tree_view_scroll_to_cell (handle, 0, column.handle, false, 0, 0);
	OS.gtk_widget_realize (handle);
	GdkRectangle cellRect = new GdkRectangle ();
	OS.gtk_tree_view_get_cell_area (handle, 0, column.handle, cellRect);
	GdkRectangle visibleRect = new GdkRectangle ();
	OS.gtk_tree_view_get_visible_rect (handle, visibleRect);
	if (cellRect.x < visibleRect.x) {
		OS.gtk_tree_view_scroll_to_point (handle, cellRect.x, -1);
	} else {
		int width = Math.min (visibleRect.width, cellRect.width);
		if (cellRect.x + width > visibleRect.x + visibleRect.width) {
			int tree_x = cellRect.x + width - visibleRect.width;
			OS.gtk_tree_view_scroll_to_point (handle, tree_x, -1);
		}
	}
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
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (item.parent != this) return;
	showItem (item.handle);
}

void showItem (int /*long*/ iter) {
	int /*long*/ path = OS.gtk_tree_model_get_path (modelHandle, iter);
	OS.gtk_tree_view_scroll_to_cell (handle, path, 0, false, 0, 0);
	OS.gtk_tree_path_free (path);
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
 *
 * @see Table#showItem(TableItem)
 */
public void showSelection () {
	checkWidget();
	TableItem [] selection = getSelection ();
	if (selection.length == 0) return;
	TableItem item = selection [0];
	showItem (item.handle);
}

int /*long*/ textCellDataProc (int /*long*/ tree_column, int /*long*/ cell, int /*long*/ tree_model, int /*long*/ iter, int /*long*/ data) {
	if (cell == ignoreTextCell) return 0;
	int modelIndex = -1;
	boolean customDraw = false;
	if (columnCount == 0) {
		modelIndex = Table.FIRST_COLUMN;
		customDraw = firstCustomDraw;
	} else {
		for (int i = 0; i < columns.length; i++) {
			if (columns [i] != null && columns [i].handle == tree_column) {
				modelIndex = columns [i].modelIndex;
				customDraw = columns [i].customDraw;
				break;
			}
		}
	}
	if (modelIndex == -1) return 0;
	boolean setData = setCellData (tree_model, iter);
	int /*long*/ [] ptr = new int /*long*/ [1];
	if (setData) {
		OS.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_TEXT, ptr, -1); 
		if (ptr [0] != 0) {
			OS.g_object_set(cell, OS.text, ptr[0], 0);
			OS.g_free (ptr[0]);
		}
		ptr = new int /*long*/ [1];
	}
	if (customDraw) {
		OS.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_FOREGROUND, ptr, -1);
		if (ptr [0] != 0) {
			OS.g_object_set(cell, OS.foreground_gdk, ptr[0], 0);
		}
		/*
		 * Bug on GTK. Gtk renders the background of the text renderer on top of the pixbuf renderer.
		 * This only happens in version 2.2.1 and earlier. The fix is not to set the background.   
		 */
		if (OS.GTK_VERSION > OS.VERSION (2, 2, 1)) {
			ptr = new int /*long*/ [1];
			OS.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_BACKGROUND, ptr, -1);
			if (ptr [0] != 0) {
				OS.g_object_set(cell, OS.background_gdk, ptr[0], 0);
			}
		}
		ptr = new int /*long*/ [1];
		OS.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_FONT, ptr, -1);
		if (ptr [0] != 0) {
			OS.g_object_set(cell, OS.font_desc, ptr[0], 0);
		}
	}
	if (setData) {
		ignoreTextCell = cell;
		setScrollWidth (tree_column, iter);
		ignoreTextCell = 0;
	}
	return 0;
}

int /*long*/ treeSelectionProc (int /*long*/ model, int /*long*/ path, int /*long*/ iter, int[] selection, int length) {
	if (selection != null) { 
		int /*long*/ indices = OS.gtk_tree_path_get_indices (path);
		if (indices != 0) {
			int [] index = new int [1];
			OS.memmove (index, indices, 4);
			selection [(int)/*64*/length] = index [0];
		}
	}
	return 0;
}

}
