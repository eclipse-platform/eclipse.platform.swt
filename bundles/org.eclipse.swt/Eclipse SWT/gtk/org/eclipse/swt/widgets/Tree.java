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
	TreeItem [] items;
	boolean selected, doubleSelected;
	int check, uncheck, imageHeight, timerID;
	static int CELL_SPACING = 1;

	/*
	* NOT DONE - These need to be moved to display,
	* they are not thread safe.  Consider moving the
	* methods that access them to Display.
	*/
	static TreeItem [] Items;
	static int Index, Count, Sibling;

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
	/*
	* To be compatible with Windows, force the H_SCROLL
	* and V_SCROLL style bits.  On Windows, it is not
	* possible to create a tree without scroll bars.
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
	handle = OS.gtk_ctree_new (1, 0);
	if (handle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	int parentHandle = parent.parentingHandle ();
	OS.gtk_container_add (parentHandle, fixedHandle);
	OS.gtk_container_add (fixedHandle, scrolledHandle);
	OS.gtk_container_add (scrolledHandle, handle);
	OS.gtk_widget_show (fixedHandle);
	OS.gtk_widget_show (scrolledHandle);
	OS.gtk_widget_show (handle);
	
	/* Force row_height to be computed */
	OS.gtk_clist_set_row_height (handle, 0);
		
	/* Single or Multiple Selection */
	int mode = (style & SWT.MULTI) != 0 ? OS.GTK_SELECTION_EXTENDED : OS.GTK_SELECTION_BROWSE;
	OS.gtk_clist_set_selection_mode (handle, mode);
	
	/* Scrolling policy */
	int hsp = (style & SWT.H_SCROLL) != 0 ? OS.GTK_POLICY_AUTOMATIC : OS.GTK_POLICY_NEVER;
	int vsp = (style & SWT.V_SCROLL) != 0 ? OS.GTK_POLICY_AUTOMATIC : OS.GTK_POLICY_NEVER;
	OS.gtk_scrolled_window_set_policy (scrolledHandle, hsp, vsp);
	
	if ((style & SWT.CHECK) != 0) {
		OS.gtk_widget_realize (handle);
		uncheck = createCheckPixmap (false);
		check = createCheckPixmap (true);
	}
}

void hookEvents () {
	//TO DO - get rid of enter/exit for mouse crossing border
	super.hookEvents ();
	Display display = getDisplay ();
	int windowProc3 = display.windowProc3;
	int windowProc4 = display.windowProc4;
	OS.gtk_signal_connect (handle, OS.tree_select_row, windowProc4, SWT.Selection);
	OS.gtk_signal_connect (handle, OS.tree_unselect_row, windowProc4, SWT.Selection);
	OS.gtk_signal_connect (handle, OS.tree_expand, windowProc3, SWT.Expand);
	OS.gtk_signal_connect (handle, OS.tree_collapse, windowProc3, SWT.Collapse);
	OS.gtk_signal_connect (handle, OS.event_after, windowProc3, 0);
}

int createCheckPixmap(boolean checked) {
	/*
	 * The box will occupy the whole item width.
	 */
	int check_height = OS.GTK_CLIST_ROW_HEIGHT (handle) - 2;
	int check_width = check_height;

	GdkVisual visual = new GdkVisual();
	OS.memmove(visual, OS.gdk_visual_get_system());
	int pixmap = OS.gdk_pixmap_new(0, check_width, check_height, visual.depth);
	
	int gc = OS.gdk_gc_new(pixmap);
	
	GdkColor fgcolor = new GdkColor();
	fgcolor.pixel = 0xFFFFFFFF;
	fgcolor.red = (short) 0xFFFF;
	fgcolor.green = (short) 0xFFFF;
	fgcolor.blue = (short) 0xFFFF;
	OS.gdk_gc_set_foreground(gc, fgcolor);
	OS.gdk_draw_rectangle(pixmap, gc, 1, 0,0, check_width,check_height);

	fgcolor = new GdkColor();
	fgcolor.pixel = 0;
	fgcolor.red = (short) 0;
	fgcolor.green = (short) 0;
	fgcolor.blue = (short) 0;
	OS.gdk_gc_set_foreground(gc, fgcolor);
	
	OS.gdk_draw_line(pixmap, gc, 0,0, 0,check_height-1);
	OS.gdk_draw_line(pixmap, gc, 0,check_height-1, check_width-1,check_height-1);
	OS.gdk_draw_line(pixmap, gc, check_width-1,check_height-1, check_width-1,0);
	OS.gdk_draw_line(pixmap, gc, check_width-1,0, 0,0);

	/* now the cross check */
	if (checked) {
		OS.gdk_draw_line(pixmap, gc, 0,0, check_width-1,check_height-1);
		OS.gdk_draw_line(pixmap, gc, 0,check_height-1, check_width-1,0);
	}
	
	OS.g_object_unref(gc);
	return pixmap;
}

void createItem (TreeItem item, int node, int index) {
	int id = 0;
	while (id < items.length && items [id] != null) id++;
	if (id == items.length) {
		TreeItem [] newItems = new TreeItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	int sibling = index == -1 ? 0 : findSibling (node, index);
	if (OS.GTK_WIDGET_MAPPED (handle) && timerID == 0) {
		OS.gtk_clist_freeze (handle);
		Display display = getDisplay ();
		timerID = OS.gtk_timeout_add (0, display.windowTimerProc, handle);
	}
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	item.handle = OS.gtk_ctree_insert_node (handle, node, sibling, null, (byte) 2, uncheck, 0, uncheck, 0, false, false);
	if ((style & SWT.SINGLE) != 0 && OS.GTK_CLIST_ROWS (handle) == 1) {
		OS.gtk_clist_unselect_row (handle, 0, 0);
	}
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
	if (item.handle == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	OS.gtk_ctree_node_set_row_data (handle, item.handle, id + 1);
	items [id] = item;
}

void createWidget (int index) {
	super.createWidget (index);
	items = new TreeItem [4];
}

GdkColor defaultBackground () {
	Display display = getDisplay ();
	return display.COLOR_LIST_BACKGROUND;
}

GdkColor defaultForeground () {
	Display display = getDisplay ();
	return display.COLOR_LIST_FOREGROUND;
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
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	if ((style & SWT.SINGLE) != 0) {
		int selection = OS.GTK_CLIST_SELECTION (handle);
		if (selection != 0 && OS.g_list_length (selection) > 0) {
			int index = OS.GTK_CLIST_FOCUS_ROW (handle);
			if (index == -1) index = 0;
			OS.gtk_clist_select_row (handle, index, 0);
			OS.gtk_clist_unselect_row (handle, index, 0);
		}
	} else {
		OS.gtk_ctree_unselect_recursive (handle, 0);
	}
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
}

void destroyItem (TreeItem item) {
	int node = item.handle;
	Callback GtkCTreeFunc = new Callback (this, "GtkCTreeDispose", 3);
	int address = GtkCTreeFunc.getAddress ();
	OS.gtk_ctree_post_recursive (handle, node, address, 0);
	GtkCTreeFunc.dispose ();
	OS.gtk_signal_handler_block_by_data (handle, SWT.Collapse);
	boolean fixSelection = false;
	if ((style & SWT.SINGLE) != 0) {
		int selection = OS.GTK_CLIST_SELECTION (handle);
		fixSelection = selection == 0 || OS.g_list_length (selection) == 0;
	}
	if (fixSelection) OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	OS.gtk_ctree_remove_node (handle, node);
	if (fixSelection) {
		OS.gtk_clist_unselect_row (handle, 0, 0);
		OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
	}
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Collapse);
}

int findSibling (int node, int index) {
	int depth = 1;
	if (node != 0) {
		int data = OS.g_list_nth_data (node, 0);
		GtkCTreeRow row = new GtkCTreeRow ();
		OS.memmove (row, data, GtkCTreeRow.sizeof);
		depth = row.level + 1;
	}
	Index = 0;
	Sibling = 0;
	Callback GtkCTreeCountItems = new Callback (this, "GtkCTreeFindSibling", 3);
	int address0 = GtkCTreeCountItems.getAddress ();
	OS.gtk_ctree_post_recursive_to_depth (handle, node, depth, address0, index);
	GtkCTreeCountItems.dispose ();
	if (Sibling == node) Sibling = 0; 
	return Sibling;
}

GdkColor getBackgroundColor () {
	return getBaseColor ();
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
	int [] row = new int [1], column = new int [1];
	if (OS.gtk_clist_get_selection_info (handle, point.x, point.y, row, column) == 0) {
		return null;
	}
	int node = OS.gtk_ctree_node_nth (handle, row [0]);
	int index = OS.gtk_ctree_node_get_row_data (handle, node) - 1;
	return items [index];
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

int getItemCount (int node) {
	int depth = 1;
	if (node != 0) {
		int data = OS.g_list_nth_data (node, 0);
		GtkCTreeRow row = new GtkCTreeRow ();
		OS.memmove (row, data, GtkCTreeRow.sizeof);
		depth = row.level + 1;
	}
	Count = 0;
	Callback GtkCTreeCountItems = new Callback (this, "GtkCTreeCountItems", 3);
	int address0 = GtkCTreeCountItems.getAddress ();
	OS.gtk_ctree_post_recursive_to_depth (handle, node, depth, address0, node);
	GtkCTreeCountItems.dispose ();
	return Count;
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
	return OS.GTK_CLIST_ROW_HEIGHT (handle) + CELL_SPACING;
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
	checkWidget();
	return getItems (0);
}

TreeItem [] getItems (int node) {
	int depth = 1;
	if (node != 0) {
		int data = OS.g_list_nth_data (node, 0);
		GtkCTreeRow row = new GtkCTreeRow ();
		OS.memmove (row, data, GtkCTreeRow.sizeof);
		depth = row.level + 1;
	}
	Count = 0;
	Callback GtkCTreeCountItems = new Callback (this, "GtkCTreeCountItems", 3);
	int address0 = GtkCTreeCountItems.getAddress ();
	OS.gtk_ctree_post_recursive_to_depth (handle, node, depth, address0, node);
	GtkCTreeCountItems.dispose ();
	Items = new TreeItem [Count];
	Count = 0;
	Callback GtkCTreeGetItems = new Callback (this, "GtkCTreeGetItems", 3);
	int address1 = GtkCTreeGetItems.getAddress ();
	OS.gtk_ctree_post_recursive_to_depth (handle, node, depth, address1, node);
	GtkCTreeGetItems.dispose ();
	TreeItem [] result = Items;
	Items = null;
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
	int selection = OS.GTK_CLIST_SELECTION (handle);
	if (selection == 0) return new TreeItem [0];
	int length = OS.g_list_length (selection);
	TreeItem [] result = new TreeItem [length];
	for (int i=0; i<length; i++) {
		int node = OS.g_list_nth_data (selection, i);
		int index = OS.gtk_ctree_node_get_row_data (handle, node) - 1;
		result [i] = items [index];
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
 */
public int getSelectionCount () {
	checkWidget();
	int selection = OS.GTK_CLIST_SELECTION (handle);
	if (selection == 0) return 0;
	return OS.g_list_length (selection);
}

int GtkCTreeCountItems (int ctree, int node, int data) {
	if (data != node) Count++;
	return 0;
}

int GtkCTreeFindSibling (int ctree, int node, int data) {
	if (Index == -1) return 0;
	if (Index == data) {
		Sibling = node;
		Index = -1;
	} else {
		Index++;
	}
	return 0;
}

int GtkCTreeGetItems (int ctree, int node, int data) {
	if (data != node) {
		int index = OS.gtk_ctree_node_get_row_data (ctree, node) - 1;
		Items [Count++] = items [index];
	}
	return 0;
}

int GtkCTreeDispose (int ctree, int node, int data) {
	int index = OS.gtk_ctree_node_get_row_data (ctree, node) - 1;
	OS.gtk_ctree_node_set_row_data (ctree, node, 0);
	TreeItem item = items [index];
	item.releaseWidget ();
	item.releaseHandle ();
	items [index] = null;
	return 0;
}

int paintWindow () {
	OS.gtk_widget_realize (handle);
	return OS.GTK_CLIST_CLIST_WINDOW (handle);
}

int processCollapse (int int0, int int1, int int2) {
	int index = OS.gtk_ctree_node_get_row_data (handle, int0) - 1;
	Event event = new Event ();
	event.item = items [index];
	/*
	* This code is intentionally commented.
	* Not yet sure if freezing improves the
	* look when a tree is collapsed.
	*/
//	OS.gtk_clist_freeze (handle);
	sendEvent (SWT.Collapse, event);
//	OS.gtk_clist_thaw (handle);
	return 0;
}

int processKeyDown (int callData, int arg1, int int2) {
	int result = super.processKeyDown (callData, arg1, int2);
	/*
	* Feature in GTK.  When an item is reselected using
	* the space bar, GTK does not issue notification.
	* The fix is to ignore the notification that is sent
	* by GTK and look for the space key.
	*/
	GdkEventKey keyEvent = new GdkEventKey ();
	OS.memmove (keyEvent, callData, GdkEventKey.sizeof);
	int length = keyEvent.length;
	if (length == 1) {
		int string = keyEvent.string;
		byte [] buffer = new byte [length];
		OS.memmove (buffer, string, length);
		char [] unicode = Converter.mbcsToWcs (null, buffer);
		switch (unicode [0]) {
			case ' ':
				int focus_row = OS.GTK_CLIST_FOCUS_ROW (handle);
				if (focus_row != -1) {
					Event event = new Event ();
					int focus = OS.gtk_ctree_node_nth (handle, focus_row);
					if (focus != 0) {
						int index = OS.gtk_ctree_node_get_row_data (handle, focus) - 1;
						event.item = items [index];
					}
					postEvent (SWT.Selection, event);
				}
				break;
		}
	}
	return result;
}

int processEvent (int eventNumber, int int0, int int1, int int2) {
	if (eventNumber == 0) {
		GdkEvent gdkEvent = new GdkEvent ();
		OS.memmove (gdkEvent, int0, GdkEvent.sizeof);
		int type = gdkEvent.type;
		switch (type) {
			case OS.GDK_BUTTON_PRESS:
			case OS.GDK_2BUTTON_PRESS: {
				OS.GTK_CLIST_RESYNC_SELECTION (handle);
				
				/*
				* Feature in GTK.  Double selection can only be implemented
				* in a mouse down handler for a tree unlike the list, the event
				* that caused the select signal is not included when the select
				* signal is issued.
				*/
				if (type == OS.GDK_2BUTTON_PRESS) {
					doubleSelected = true;
					double[] px = new double [1], py = new double [1];
					OS.gdk_event_get_coords (int0, px, py);
					int x = (int)(px[0]), y = (int)(py[0]);	
					if (!OS.gtk_ctree_is_hot_spot (handle, x, y)) {
						int [] row = new int [1], column = new int [1];
						int code = OS.gtk_clist_get_selection_info (handle, x, y, row, column);
						if (code != 0) {
							int node = OS.gtk_ctree_node_nth (handle, row [0]);
							int index = OS.gtk_ctree_node_get_row_data (handle, node) - 1;
							Event event = new Event ();
							event.item = items [index];
							postEvent (SWT.DefaultSelection, event);
						}
					}
				}
				break;
			}
			case OS.GDK_BUTTON_RELEASE: {
				/*
				* Feature in GTK.  When an item is reselected in a
				* multi-select tree, GTK does not issue notification.
				* The fix is to detect that the mouse was released over
				* a selected item when no selection signal was set and
				* issue a fake selection event.
				*/
				if ((style & SWT.MULTI) != 0) {
					if (selected && !doubleSelected) {
						double[] px = new double [1], py = new double [1];
						OS.gdk_event_get_coords (int0, px, py);
						int x = (int)(px[0]), y = (int)(py[0]);	
						if (!OS.gtk_ctree_is_hot_spot (handle, x, y)) {
							int [] row = new int [1], column = new int [1];
							int code = OS.gtk_clist_get_selection_info (handle, x, y, row, column);
							if (code != 0) {
								int focus = OS.gtk_ctree_node_nth (handle, row [0]);
								int selection = OS.GTK_CLIST_SELECTION (handle);
								if (selection != 0) {
									int length = OS.g_list_length (selection);
									for (int i=0; i<length; i++) {
										int node = OS.g_list_nth_data (selection, i);
										if (node == focus) {
											int index = OS.gtk_ctree_node_get_row_data (handle, node) - 1;
											Event event = new Event ();
											event.item = items [index];
											postEvent (SWT.Selection, event);
										}
									}
								}
							}
						}
					}
					selected = false;
				}
				doubleSelected = false;
				break;
			}
			case OS.GDK_FOCUS_CHANGE: {
				/*
				* Bug in GTK.  When an application opens a new modal top level
				* shell from inside the "select_row" signal, the GtkCList does not get the
				* mouse up and does not release grabs.  The fix is to release the grabs
				* when focus is lost.
				*/
				GdkEventFocus focusEvent = new GdkEventFocus ();
				OS.memmove (focusEvent, int0, GdkEventFocus.sizeof);
				if (focusEvent.in == 0) {
					OS.gtk_grab_remove (handle);
					OS.gdk_pointer_ungrab (OS.GDK_CURRENT_TIME);
				}
				break;
			}
		}
		return 1;
	}
	return super.processEvent (eventNumber, int0, int1, int2);
}

int processExpand (int int0, int int1, int int2) {
	int index = OS.gtk_ctree_node_get_row_data (handle, int0) - 1;
	Event event = new Event ();
	event.item = items [index];
	OS.gtk_clist_freeze (handle);
	sendEvent (SWT.Expand, event);
	OS.gtk_clist_thaw (handle);
	boolean [] expanded = new boolean [1];
	OS.gtk_ctree_get_node_info (handle, int0, null, null, null, null, null, null, null, expanded);
	if (!expanded [0]) {
		OS.gtk_signal_handler_block_by_data (handle, SWT.Expand);
		OS.gtk_ctree_expand (handle, int0);
		OS.gtk_signal_handler_unblock_by_data (handle, SWT.Expand);
	}
	return 0;
}

int processMouseDown (int int0, int int1, int int2) {
	int result = super.processMouseDown (int0, int1, int2);
	if ((style & SWT.MULTI) != 0) selected = true;
	double [] px = new double [1], py = new double [1];
	OS.gdk_event_get_coords (int0, px, py);
	int x = (int)(px[0]), y = (int)(py[0]);	
	if ((style & SWT.CHECK) != 0) {
		if (!OS.gtk_ctree_is_hot_spot (handle, x, y)) {
			int [] row = new int [1], column = new int [1];
			if (OS.gtk_clist_get_selection_info (handle, x, y, row, column) != 0) {
				int node = OS.gtk_ctree_node_nth (handle, row [0]);
				int crow = OS.g_list_nth_data (node, 0);
				GtkCTreeRow row_data = new GtkCTreeRow ();
				OS.memmove (row_data, crow, GtkCTreeRow.sizeof);
				int nX = OS.GTK_CLIST_HOFFSET (handle) + OS.GTK_CTREE_TREE_INDENT (handle) * row_data.level - 2;
				int nY = OS.GTK_CLIST_VOFFSET (handle) + (OS.GTK_CLIST_ROW_HEIGHT (handle) + 1) * row [0] + 2;
				int [] check_width = new int [1], check_height = new int [1];
				OS.gdk_drawable_get_size (check, check_width, check_height);
				if (nX <= x && x <= nX + check_width [0]) {
					if (nY <= y && y <= nY + check_height [0]) {
						byte [] spacing = new byte [1];
						boolean [] is_leaf = new boolean [1], expanded = new boolean [1];
						int [] pixmap = new int [1], mask = new int [1];
						int index = OS.gtk_ctree_node_get_row_data (handle, node) - 1;
						byte [] text = Converter.wcsToMbcs (null, items [index].getText (), true);
						OS.gtk_ctree_get_node_info (handle, node, null, spacing, pixmap, mask, pixmap, mask, is_leaf, expanded);
						pixmap [0] = pixmap [0] == check ? uncheck : check;
						OS.gtk_ctree_set_node_info (handle, node, text, spacing [0], pixmap [0], mask [0], pixmap [0], mask [0], is_leaf [0], expanded [0]);
						Event event = new Event ();
						event.detail = SWT.CHECK;
						event.item = items [index];
						postEvent (SWT.Selection, event);
					}
				}
			}
		}
	}
	GdkEvent gdkEvent = new GdkEvent ();
	OS.memmove (gdkEvent, int0, GdkEvent.sizeof);
	if (gdkEvent.type == OS.GDK_2BUTTON_PRESS) {
		if (hooks (SWT.DefaultSelection)) return 1;
	}
	return result;
}

int processSelection (int int0, int int1, int int2) {
	int focus_row = OS.GTK_CLIST_FOCUS_ROW (handle);
	int focus = OS.gtk_ctree_node_nth (handle, focus_row);
	if (focus != int0) return 0;
	if ((style & SWT.MULTI) != 0) selected = false;
	int ptr = OS.gtk_get_current_event ();
	if (ptr != 0) {
		GdkEvent gdkEvent = new GdkEvent ();
		OS.memmove (gdkEvent, ptr, GdkEvent.sizeof);
		OS.gdk_event_free (ptr);
		/*
		* Feature in GTK.  When a leaf node is double clicked, the
		* GtkCTree issues two tree_row_select signals.  Non-leaf
		* nodes only get one tree_row_select.  Avoid issueing
		* a selection event for the double clicked leaf node.
		*/ 
		if (gdkEvent.type == OS.GDK_2BUTTON_PRESS) return 0;
	}
	int index = OS.gtk_ctree_node_get_row_data (handle, int0) - 1;
	Event event = new Event ();
	event.item = items [index];
	postEvent (SWT.Selection, event);
	return 0;
}

int processTimer (int id) {
	if (timerID != 0) OS.gtk_clist_thaw (handle);
	timerID = 0;
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
	if (check != 0) OS.g_object_unref (check);
	if (uncheck != 0) OS.g_object_unref (uncheck);
	check = uncheck = 0;
	if (timerID != 0) OS.gtk_timeout_remove (timerID);
	super.releaseWidget ();
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
	OS.gtk_ctree_remove_node (handle, 0);
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null && !item.isDisposed ()) {
			item.releaseWidget ();
			item.releaseHandle ();
		}
	}
	items = new TreeItem [4];
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

void setFontDescription (int font) {
	super.setFontDescription (font);
	if (imageHeight != 0) {
		OS.gtk_widget_realize (handle);
		OS.gtk_clist_set_row_height (handle, 0);
		if (imageHeight > OS.GTK_CLIST_ROW_HEIGHT (handle)) {
			OS.gtk_clist_set_row_height (handle, imageHeight);
		}
	}
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
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	OS.gtk_ctree_select_recursive (handle, 0);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);	
}

void setBackgroundColor (GdkColor color) {
	super.setBackgroundColor (color);
	OS.gtk_widget_modify_base (handle, 0, color);
}

public void setRedraw (boolean redraw) {
	checkWidget ();
	if (redraw) {
		OS.gtk_clist_thaw (handle);
	} else {
		OS.gtk_clist_freeze (handle);
	}
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
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	if ((style & SWT.MULTI) != 0) {
		OS.gtk_ctree_unselect_recursive (handle, 0);
	}
	int index = 0, length = items.length;
	while (index < length) {
		TreeItem item = items [index];
		if (item != null) {
			if (item.isDisposed ()) break;
			OS.gtk_ctree_select (handle, item.handle);
		}
		index++;
	}
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
	index = 0;
	while (index < length) {
		TreeItem item = items [index];
		if (item != null && !item.isDisposed ()) {
			showItem (item);
			return;
		}
		index++;
	}
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
	int selection = OS.GTK_CLIST_SELECTION (handle);
	if (selection == 0) return;
	if (OS.g_list_length (selection) == 0) return;
	int node = OS.g_list_nth_data (selection, 0);
	int index = OS.gtk_ctree_node_get_row_data (handle, node) - 1;
	showItem (items [index]);
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
	int node = item.handle;
	int visibility = OS.gtk_ctree_node_is_visible (handle, node);
	if (visibility != OS.GTK_VISIBILITY_NONE) return;
	if (!OS.gtk_ctree_is_viewable (handle, node)) {
		int parent = node;
		GtkCTreeRow row;
		OS.gtk_signal_handler_block_by_data (handle, SWT.Expand);
		do {
			int data = OS.g_list_nth_data (parent, 0);
			row = new GtkCTreeRow ();
			OS.memmove (row, data, GtkCTreeRow.sizeof);
			if ((parent = row.parent) == 0) break;
			OS.gtk_ctree_expand (handle, parent);
		} while (true);
		OS.gtk_signal_handler_unblock_by_data (handle, SWT.Expand);
	}
	OS.gtk_ctree_node_moveto (handle, node, 0, 0.0f, 0.0f);
}

public void update () {
	checkWidget ();
	if (timerID != 0) {
		OS.gtk_clist_thaw (handle);
		OS.gtk_timeout_remove (timerID);
		timerID = 0;
	}
	super.update ();
}

}
