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
	int /*long*/ modelHandle, columnHandle, checkRenderer, pixbufRenderer, textRenderer;
	TreeItem[] items;
	ImageList imageList;
	
	static final int TEXT_COLUMN = 0;
	static final int PIXBUF_COLUMN = 1;
	static final int FOREGROUND_COLUMN = 2;
	static final int BACKGROUND_COLUMN = 3;
	static final int FONT_COLUMN = 4;
	static final int ID_COLUMN = 5;
	static final int CHECKED_COLUMN = 6;
	static final int GRAYED_COLUMN = 7;
	
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
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	Point size = computeNativeSize (handle, wHint, hHint, changed);
	Rectangle trim = computeTrim (0, 0, size.x, size.y);
	size.x = trim.width;
	size.y = trim.height;
	return size;
}

void createHandle (int index) {
	state |= HANDLE;
	fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	scrolledHandle = OS.gtk_scrolled_window_new (0, 0);
	if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
	/*
	* Columns:
	* 0 - text
	* 1 - pixmap
	* 2 - foreground
	* 3 - background
	* 4 - font
	* 5 - id
	* 6 - checked (if needed)
	* 7 - grayed (if needed)
	*/
	int /*long*/ [] types = new int /*long*/ [(style & SWT.CHECK) !=0 ? 8 : 6];
	types [TEXT_COLUMN] = OS.G_TYPE_STRING ();
	types [PIXBUF_COLUMN] = OS.GDK_TYPE_PIXBUF ();
	types [FOREGROUND_COLUMN] = OS.GDK_TYPE_COLOR ();
	types [BACKGROUND_COLUMN] = OS.GDK_TYPE_COLOR ();
	types [FONT_COLUMN] = OS.PANGO_TYPE_FONT_DESCRIPTION ();
	types [ID_COLUMN] = OS.G_TYPE_INT ();
	if ((style & SWT.CHECK) != 0) {
		types [CHECKED_COLUMN] = OS.G_TYPE_BOOLEAN (); 
		types [GRAYED_COLUMN] = OS.G_TYPE_BOOLEAN ();
	} 
	modelHandle = OS.gtk_tree_store_newv (types.length, types);
	if (modelHandle == 0) error (SWT.ERROR_NO_HANDLES);
	handle = OS.gtk_tree_view_new_with_model (modelHandle);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	
	/*
	* Bug in ATK. For some reason, ATK segments fault if 
	* the GtkTreeView has a column and does not have items.
	* The fix is to insert the column only when an item is 
	* created.
	*/
	columnHandle = OS.gtk_tree_view_column_new ();
	if (columnHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.g_object_ref (columnHandle);
	
	if ((style & SWT.CHECK) != 0) {
		checkRenderer = OS.gtk_cell_renderer_toggle_new ();
		if (checkRenderer == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_tree_view_column_pack_start (columnHandle, checkRenderer, false);
		OS.gtk_tree_view_column_add_attribute (columnHandle, checkRenderer, OS.active, CHECKED_COLUMN);

		/*
		* Feature in GTK. The inconsistent property only exists in GTK 2.2.x.
		*/
		if (OS.GTK_VERSION >= OS.VERSION (2, 2, 0)) {
			OS.gtk_tree_view_column_add_attribute (columnHandle, checkRenderer, OS.inconsistent, GRAYED_COLUMN);
		}
	}
	pixbufRenderer = OS.gtk_cell_renderer_pixbuf_new ();
	if (pixbufRenderer == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_tree_view_column_pack_start (columnHandle, pixbufRenderer, false);
	OS.gtk_tree_view_column_add_attribute (columnHandle, pixbufRenderer, OS.pixbuf, PIXBUF_COLUMN);
	/*
	* Feature on GTK.  When a tree view column contains only one activatable
	* cell renderer such as a toggle renderer, mouse clicks anywhere in a cell
	* activate that renderer. The workaround is to set a second  cell renderer
	* to be activatable.
	*/
	if ((style & SWT.CHECK) != 0) {
		OS.g_object_set (pixbufRenderer, OS.mode, OS.GTK_CELL_RENDERER_MODE_ACTIVATABLE, 0);
	}
	textRenderer = OS.gtk_cell_renderer_text_new ();
	if (textRenderer == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_tree_view_column_pack_start (columnHandle, textRenderer, true);
	OS.gtk_tree_view_column_add_attribute (columnHandle, textRenderer, OS.text, TEXT_COLUMN);
	OS.gtk_tree_view_column_add_attribute (columnHandle, textRenderer, OS.foreground_gdk, FOREGROUND_COLUMN);
	
	/*
	 * Bug on GTK. Gtk renders the background of the text renderer on top of the pixbuf renderer.
	 * This only happens in version 2.2.1 and earlier. The fix is not to set the background.   
	 */
	if (OS.GTK_VERSION > OS.VERSION (2, 2, 1)) {
		OS.gtk_tree_view_column_add_attribute (columnHandle, textRenderer, OS.background_gdk, BACKGROUND_COLUMN);
	}
	OS.gtk_tree_view_column_add_attribute (columnHandle, textRenderer, OS.font_desc, FONT_COLUMN);
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
}

void createItem (TreeItem item, int /*long*/ iter, int index) {
	int /*long*/ column = OS.gtk_tree_view_get_column (handle, 0);
	if (column == 0) OS.gtk_tree_view_insert_column (handle, columnHandle, 0);
	int count = OS.gtk_tree_model_iter_n_children (modelHandle, iter);
	if (index == -1) index = count;
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	int id = 0;
	while (id < items.length && items [id] != null) id++;
	if (id == items.length) {
		TreeItem [] newItems = new TreeItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	item.handle = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	if (item.handle == 0) error(SWT.ERROR_NO_HANDLES);
	if (index == count) {
		OS.gtk_tree_store_append (modelHandle, item.handle, iter);
	} else {
		OS.gtk_tree_store_insert (modelHandle, item.handle, iter, index);
	}
	OS.gtk_tree_store_set (modelHandle, item.handle, ID_COLUMN, id, -1);
	items [id] = item;
}

void createWidget (int index) {
	super.createWidget (index);
	items = new TreeItem [4];
}

GdkColor defaultBackground () {
	return display.COLOR_LIST_BACKGROUND;
}

GdkColor defaultForeground () {
	return display.COLOR_LIST_FOREGROUND;
}

void deregister () {
	super.deregister ();
	display.removeWidget (OS.gtk_tree_view_get_selection (handle));
	if (checkRenderer != 0) display.removeWidget (checkRenderer);
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
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.gtk_tree_selection_unselect_all (selection);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
}

void destroyItem (TreeItem item) {
	/*
	* Bug in GTK.  GTK segment faults when a root tree item
	* is destroyed when the tree is expanded and the last leaf of
	* the root is selected.  This only happens in versions earlier
	* than 2.0.6.  The fix is to collapse the tree item being destroyed
	* when it is a root, before it is destroyed.
	*/
	if (OS.GTK_VERSION < OS.VERSION (2, 0, 6)) {
		TreeItem [] roots = getItems (0);
		for (int i = 0; i < roots.length; i++) {
			if (item == roots [i]) {
				item.setExpanded (false);
				break;
			}
		}
	}
	int [] index = new int [1];
	releaseItems (item.getItems (), index);
	releaseItem (item, index);
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.gtk_tree_store_remove (modelHandle, item.handle);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	int childCount = OS.gtk_tree_model_iter_n_children (modelHandle, 0);
	if (childCount == 0) removeColumn ();
}

GdkColor getBackgroundColor () {
	return getBaseColor ();
}

TreeItem getFocusItem () {
	int /*long*/ [] path = new int /*long*/ [1];
	OS.gtk_tree_view_get_cursor (handle, path, null);
	if (path [0] == 0) return null;
	TreeItem item = null;
	int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	if (OS.gtk_tree_model_get_iter (modelHandle, iter, path [0])) {
		int [] index = new int [1];
		OS.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, index, -1);
		item = items [index [0]];
	}
	OS.g_free (iter);
	OS.gtk_tree_path_free (path [0]);
	return item;	
} 

GdkColor getForegroundColor () {
	return getTextColor ();
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
public TreeItem getItem (Point point) {
	checkWidget ();
	int /*long*/ [] path = new int /*long*/ [1];	
	OS.gtk_widget_realize (handle);
	if (!OS.gtk_tree_view_get_path_at_pos (handle, point.x, point.y, path, null, null, null)) return null;
	if (path [0] == 0) return null;
	TreeItem item = null;
	int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	if (OS.gtk_tree_model_get_iter (modelHandle, iter, path [0])) {
		boolean overExpander = false;
		if (OS.gtk_tree_model_iter_n_children (modelHandle, iter) > 0) {
			int[] buffer = new int [1];
			GdkRectangle rect = new GdkRectangle ();
			OS.gtk_tree_view_get_cell_area (handle, path [0], columnHandle, rect);
			OS.gtk_widget_style_get (handle, OS.expander_size, buffer, 0);
			int expanderSize = buffer [0] + TreeItem.EXPANDER_EXTRA_PADDING;
			overExpander = rect.x - 1 <= point.x && point.x < rect.x + expanderSize;
		}
		if (!overExpander) {
			int [] index = new int [1];
			OS.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, index, -1);
			item = items [index [0]];
		}
	}
	OS.g_free (iter);
	OS.gtk_tree_path_free (path [0]);
	return item;
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
 */
public int getItemHeight () {
	checkWidget ();
	int itemCount = OS.gtk_tree_model_iter_n_children (modelHandle, 0);
	if (itemCount == 0) {
		int [] w = new int [1], h = new int [1];
		OS.gtk_tree_view_insert_column (handle, columnHandle, 0);
		OS.gtk_tree_view_column_cell_get_size (columnHandle, null, null, null, w, h);
		OS.gtk_tree_view_remove_column (handle, columnHandle);
		return h [0];
	} else {
		int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
		OS.gtk_tree_model_get_iter_first (modelHandle, iter);
		int /*long*/ column = OS.gtk_tree_view_get_column (handle, 0);
		OS.gtk_tree_view_column_cell_set_cell_data (column, modelHandle, iter, false, false);
		int [] w = new int [1], h = new int [1];
		OS.gtk_tree_view_column_cell_get_size (column, null, null, null, w, h);
		OS.g_free (iter);
		return h [0];
	}
}

/**
 * Returns the items contained in the receiver
 * that are direct item children of the receiver.  These
 * are the roots of the tree.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem [] getItems () {
	checkWidget();
	return getItems (0);
}

TreeItem [] getItems (int /*long*/ parent) {
	int length = OS.gtk_tree_model_iter_n_children (modelHandle, parent);
	TreeItem[] result = new TreeItem [length];
	if (length == 0) return result;
	int i = 0;
	int[] index = new int [1];
	int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	boolean valid = OS.gtk_tree_model_iter_children (modelHandle, iter, parent);
	while (valid) {
		OS.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, index, -1);
		result [i++] = items [index [0]];
		valid = OS.gtk_tree_model_iter_next (modelHandle, iter);
	}
	OS.g_free (iter);
	return result;
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
	if ((style & SWT.MULTI) != 0) {
		display.treeSelectionLength  = 0;
		display.treeSelection = new int [items.length];
		int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
		OS.gtk_tree_selection_selected_foreach (selection, display.treeSelectionProc, handle);
		TreeItem [] result = new TreeItem [display.treeSelectionLength];
		for (int i=0; i<result.length; i++) result [i] = items [display.treeSelection [i]];
		return result;
	} else {
		int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
		int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
		boolean hasSelection = OS.gtk_tree_selection_get_selected (selection, null, iter);
		TreeItem [] result;
		if (hasSelection) {
			int [] index = new int [1];
			OS.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, index, -1);
			result = new TreeItem []{items [index [0]]};
		} else {
			result = new TreeItem [0];
		}
		OS.g_free (iter);
		return result;
	}
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
 * Returns the item which is currently at the top of the receiver.
 * This item can change when items are expanded, collapsed, scrolled
 * or new items are added or removed.
 *
 * @return the item at the top of the receiver 
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public TreeItem getTopItem () {
	checkWidget ();
	int /*long*/ [] path = new int /*long*/ [1];
	OS.gtk_widget_realize (handle);
	if (!OS.gtk_tree_view_get_path_at_pos (handle, 1, 1, path, null, null, null)) return null;
	if (path [0] == 0) return null;
	TreeItem item = null;
	int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof());
	if (OS.gtk_tree_model_get_iter (modelHandle, iter, path [0])) {
		int [] index = new int [1];
		OS.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, index, -1);
		item = items [index [0]];
	}
	OS.g_free (iter);
	OS.gtk_tree_path_free (path [0]);
	return item;
}

int /*long*/ gtk_changed (int /*long*/ widget) {
	TreeItem item = getFocusItem ();
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

int /*long*/ gtk_row_activated (int /*long*/ tree, int /*long*/ path, int /*long*/ column) {
	if (path == 0) return 0;
	TreeItem item = null;
	int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	if (OS.gtk_tree_model_get_iter (modelHandle, iter, path)) {
		int [] index = new int [1];
		OS.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, index, -1);
		item = items [index [0]];
	}
	OS.g_free (iter);
	Event event = new Event ();
	event.item = item;
	postEvent (SWT.DefaultSelection, event);
	return 0;
}

int /*long*/ gtk_test_collapse_row (int /*long*/ tree, int /*long*/ iter, int /*long*/ path) {
	int [] index = new int [1];
	OS.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, index, -1);
	Event event = new Event ();
	event.item = items [index [0]];
	sendEvent (SWT.Collapse, event);
	if (isDisposed ()) return 0;
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_COLLAPSE_ROW);
	OS.gtk_tree_view_collapse_row (handle, path);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_COLLAPSE_ROW);
	return 1;
}

int /*long*/ gtk_test_expand_row (int /*long*/ tree, int /*long*/ iter, int /*long*/ path) {
	int [] index = new int [1];
	OS.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, index, -1);
	Event event = new Event ();
	event.item = items [index [0]];
	sendEvent (SWT.Expand, event);
	if (isDisposed ()) return 0;
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_EXPAND_ROW);
	OS.gtk_tree_view_expand_row (handle, path, false);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_EXPAND_ROW);
	return 1;
}

int /*long*/ gtk_toggled (int /*long*/ renderer, int /*long*/ pathStr) {
	int /*long*/ path = OS.gtk_tree_path_new_from_string (pathStr);
	if (path == 0) return 0;
	TreeItem item = null;
	int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof());
	if (OS.gtk_tree_model_get_iter (modelHandle, iter, path)) {
		int [] index = new int [1];
		OS.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, index, -1);
		item = items [index [0]];
	}
	OS.g_free (iter);
	OS.gtk_tree_path_free (path);
	item.setChecked (!item.getChecked ());
	Event event = new Event ();
	event.detail = SWT.CHECK;
	event.item = item;
	postEvent (SWT.Selection, event);
	return 0;
}

int /*long*/ gtk_button_press_event (int /*long*/ widget, int /*long*/ event) {
	int /*long*/ result = super.gtk_button_press_event (widget, event);
	if (result != 0) return result;
	/*
	* Feature in GTK.  In a multi-select tree view, when multiple items are already
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
	
void hookEvents () {
	super.hookEvents ();
	int /*long*/ selection = OS.gtk_tree_view_get_selection(handle);
	OS.g_signal_connect_after (selection, OS.changed, display.windowProc2, CHANGED);
	OS.g_signal_connect (handle, OS.row_activated, display.windowProc4, ROW_ACTIVATED);
	OS.g_signal_connect (handle, OS.test_expand_row, display.windowProc4, TEST_EXPAND_ROW);
	OS.g_signal_connect (handle, OS.test_collapse_row, display.windowProc4, TEST_COLLAPSE_ROW);
	if (checkRenderer != 0) {
		OS.g_signal_connect (checkRenderer, OS.toggled, display.windowProc3, TOGGLED);
	}
}

int /*long*/ paintWindow () {
	OS.gtk_widget_realize (handle);
	return OS.gtk_tree_view_get_bin_window (handle);
}

void register () {
	super.register ();
	display.addWidget (OS.gtk_tree_view_get_selection (handle), this);
	if (checkRenderer != 0) display.addWidget (checkRenderer, this);
}

boolean releaseItem (TreeItem item, int [] index) {
	if (item.isDisposed ()) return false;
	OS.gtk_tree_model_get (modelHandle, item.handle, ID_COLUMN, index, -1);
	items [index [0]] = null;
	return true;
}

void releaseItems (TreeItem [] nodes, int [] index) {
	for (int i=0; i<nodes.length; i++) {
		TreeItem item = nodes [i];
		TreeItem [] sons = item.getItems ();
		if (sons.length != 0) {
			releaseItems (sons, index);
		}
		if (releaseItem (item, index)) {
			item.releaseResources ();
		}
	}
}

void releaseWidget () {
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null && !item.isDisposed ()) item.releaseResources();
	}
	items = null;
	super.releaseWidget();
	if (modelHandle != 0) OS.g_object_unref (modelHandle);
	modelHandle = 0;
	if (columnHandle != 0) OS.g_object_unref (columnHandle);
	columnHandle = 0;
	if (imageList != null) {
		imageList.dispose ();
		imageList = null;
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
	checkWidget ();
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null && !item.isDisposed ()) item.releaseResources ();
	}
	items = new TreeItem[4];
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	OS.gtk_tree_store_clear (modelHandle);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	removeColumn ();
}

void removeColumn () {
	int /*long*/ column = OS.gtk_tree_view_get_column (handle, 0);
	if (column == 0) return;
	OS.gtk_tree_view_remove_column (handle, column);
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

/**
 * Display a mark indicating the point at which an item will be inserted.
 * The drop insert item has a visual hint to show where a dragged item 
 * will be inserted when dropped on the tree.
 * 
 * @param item the insert item.  Null will clear the insertion mark.
 * @param before true places the insert mark above 'item'. false places 
 *	the insert mark below 'item'.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setInsertMark (TreeItem item, boolean before) {
	checkWidget ();
	if (item == null) {
		OS.gtk_tree_view_unset_rows_drag_dest(handle);
		return;
	}
	if (item.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (item.parent != this) return;
	Rectangle rect = item.getBounds();
	int /*long*/ [] path = new int /*long*/ [1];
	OS.gtk_widget_realize (handle);
	if (!OS.gtk_tree_view_get_path_at_pos(handle, rect.x, rect.y, path, null, null, null)) return;
	if (path [0] == 0) return;
	int position = before ? OS.GTK_TREE_VIEW_DROP_BEFORE : OS.GTK_TREE_VIEW_DROP_AFTER;
	OS.gtk_tree_view_set_drag_dest_row(handle, path[0], position);
	OS.gtk_tree_path_free (path [0]);
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
	OS.gtk_tree_selection_select_all (OS.gtk_tree_view_get_selection (handle));
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
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

void setForegroundColor (GdkColor color) {
	super.setForegroundColor (color);
	OS.gtk_widget_modify_text (handle, 0, color);
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
 * @see Tree#deselectAll()
 */
public void setSelection (TreeItem [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	deselectAll ();
	int length = items.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	boolean first = true;
	for (int i = 0; i < length; i++) {
		TreeItem item = items [i];
		if (item == null) continue;
		if (item.isDisposed ()) break;
		if (item.parent != this) continue;
		int /*long*/ path = OS.gtk_tree_model_get_path (modelHandle, item.handle);
		showItem (path, first);
		if (first) {
			OS.gtk_tree_view_set_cursor (handle, path, 0, false);
		}
		OS.gtk_tree_selection_select_iter (selection, item.handle);
		OS.gtk_tree_path_free (path);
		first = false;
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
}

/**
 * Sets the item which is currently at the top of the receiver.
 * This item can change when items are expanded, collapsed, scrolled
 * or new items are added or removed.
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
 * @see Tree#getTopItem()
 * 
 * @since 2.1
 */
public void setTopItem (TreeItem item) {
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (item.parent != this) return;
	int /*long*/ path = OS.gtk_tree_model_get_path (modelHandle, item.handle);
	showItem (path, false);
	OS.gtk_tree_view_scroll_to_cell (handle, path, 0, true, 0, 0);
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
 * @see Tree#showItem(TreeItem)
 */
public void showSelection () {
	checkWidget();
	TreeItem [] items = getSelection ();
	if (items.length != 0 && items [0] != null) showItem (items [0]);
}

void showItem (int /*long*/ path, boolean scroll) {
	int depth = OS.gtk_tree_path_get_depth (path);
	if (depth > 1) {
		int [] indices = new int [depth - 1];
		int /*long*/ indicesPtr = OS.gtk_tree_path_get_indices (path);
		OS.memmove (indices, indicesPtr, indices.length * 4);
		int /*long*/ tempPath = OS.gtk_tree_path_new ();
		for (int i=0; i<indices.length; i++) {
			OS.gtk_tree_path_append_index (tempPath, indices [i]);
			OS.gtk_tree_view_expand_row (handle, tempPath, false);
		}
		OS.gtk_tree_path_free (tempPath);		
	}
	if (scroll) {
		GdkRectangle rect = new GdkRectangle ();
		OS.gtk_widget_realize (handle);
		OS.gtk_tree_view_get_cell_area (handle, path, 0, rect);
		boolean isHidden = rect.y == 0 && rect.height == 0;
		if (!isHidden) {
			int [] tx = new int [1], ty = new int [1];
			OS.gtk_tree_view_widget_to_tree_coords (handle, rect.x, rect.y, tx, ty);
			rect.y = ty[0];
			GdkRectangle visRect = new GdkRectangle ();
			OS.gtk_tree_view_get_visible_rect (handle, visRect);
			if (rect.y < visRect.y || rect.y + rect.height > visRect.y + visRect.height) {
				isHidden = true;
			} 
		}
		if (isHidden) OS.gtk_tree_view_scroll_to_cell (handle, path, 0, depth != 1, 0.5f, 0.0f);	
	}
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
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (item.parent != this) return;
	int /*long*/ path = OS.gtk_tree_model_get_path (modelHandle, item.handle);
	showItem (path, true);
	OS.gtk_tree_path_free (path);
}

int /*long*/ treeSelectionProc (int /*long*/ model, int /*long*/ path, int /*long*/ iter, int[] selection, int length) {
	if (selection != null) {
		int [] index = new int [1];
		OS.gtk_tree_model_get (modelHandle, iter, ID_COLUMN, index, -1);
		selection [(int)/*64*/length] = index [0];
	}
	return 0;
}

}
