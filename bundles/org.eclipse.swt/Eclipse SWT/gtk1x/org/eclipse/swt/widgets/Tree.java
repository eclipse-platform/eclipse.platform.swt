package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Tree extends Composite {
	TreeItem [] items;
	TreeItem selectedItem;

	boolean ignoreExpand, ignoreCollapse;
	TreeItem itemInInsertion;

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
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
 * @see SWT
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

void createHandle (int index) {
	state |= HANDLE;
	
	eventBoxHandle = OS.gtk_fixed_new(); // false for homogeneous; doesn't really matter
	if (eventBoxHandle == 0) error (SWT.ERROR_NO_HANDLES);
	scrolledHandle = OS.gtk_scrolled_window_new (0, 0);
	if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
	handle = OS.gtk_ctree_new (1, 0);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

void setHandleStyle() {
	setSelectionPolicy();
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
	}
	setScrollingPolicy();
}

void setSelectionPolicy() {
	int selectionPolicy = ((style & SWT.MULTI) != 0)?
		OS.GTK_SELECTION_EXTENDED :
		OS.GTK_SELECTION_BROWSE;
	OS.gtk_clist_set_selection_mode (handle, selectionPolicy);
}

void configure() {
	_connectParent();
	OS.gtk_container_add (eventBoxHandle, scrolledHandle);
	OS.gtk_container_add (scrolledHandle, handle);	
}

void showHandle() {
	OS.gtk_widget_show (eventBoxHandle);
	OS.gtk_widget_show (scrolledHandle);
	OS.gtk_widget_show (handle);
	OS.gtk_widget_realize (handle);
}

void hookEvents () {
	//TO DO - get rid of enter/exit for mouse crossing border
	super.hookEvents ();
	signal_connect (handle, "tree_select_row", SWT.Selection, 4);
	signal_connect (handle, "tree_expand", SWT.Expand, 3);
	signal_connect (handle, "tree_collapse", SWT.Collapse, 3);
}

void createWidget (int index) {
	super.createWidget (index);
	items = new TreeItem [4];
}

int topHandle() { return eventBoxHandle; }
int parentingHandle() { return eventBoxHandle; }  // FIXME - Temporary placeholder
boolean isMyHandle(int h) {
	if (h==eventBoxHandle) return true;
	if (h==scrolledHandle) return true;
	if (h==handle) return true;
	return false;
}
void _connectChild() {
	error(SWT.ERROR_UNSPECIFIED);
}
Control[] _getChildren() {
	return new Control[0];
}


/*
 *   ===  GEOMETRY  ===
 */


protected Point _getClientAreaSize () {
	return UtilFuncs.getSize(handle);
}

boolean _setSize(int width, int height) {
	boolean differentExtent = UtilFuncs.setSize (eventBoxHandle, width,height);
	UtilFuncs.setSize (scrolledHandle, width, height);	
	return differentExtent;	
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint == SWT.DEFAULT) wHint = 200;
	//scrolledHandle
	return _computeSize (wHint, hHint, changed);
}


/*
 *   ===  ADD/REMOVE LISTENERS  ===
 */
 
 
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Expand, typedListener);
	addListener (SWT.Collapse, typedListener);
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Expand, listener);
	eventTable.unhook (SWT.Collapse, listener);
}

/*
 *   ===  WIDGET SPECIFIC LOGIC  ===
 */

void createItem (TreeItem item, int node, int index) {
	int id = 0;
	while (id < items.length && items [id] != null) id++;
	if (id == items.length) {
		TreeItem [] newItems = new TreeItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}

	/* Feature in GTK.
	 * When the selection policy is BROWSE (which is what we use for SINGLE),
	 * the first item added gets automatically selected.  This leads to some
	 * nontrivial complications which one better avoid.  The hack is to
	 * temporarily put a value other than GTK_SELECTION_BROWSE into the
	 * selectionMode field just for the insertion.  Do not use the policy
	 * changing API because this will cause a selection callback.
	 */
	int[] sm = new int[1];
	OS.memmove(sm, handle+148, 1);
	int selectionMode = sm[0];
	sm[0] = OS.GTK_SELECTION_MULTIPLE;
	OS.memmove(handle+148, sm, 1);
	
	itemInInsertion = item;
	item.handle = OS.gtk_ctree_insert_node (handle,
		node,  // parent
		0,  // sibling
		null,  // text
		(byte) 2,  // extra space between icon and text
		0, 0, 0, 0,  //  open/close pixmap/mask
		false,  // isLeaf
		false);  // expanded
	itemInInsertion = null;
	OS.gtk_ctree_node_set_row_data (handle, item.handle, id + 1);

	sm[0] = selectionMode;
	OS.memmove(handle+148, sm, 1);

	items [id] = item;
}

/*
 *   ===  SELECTION STORY  ===
 */

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
	return _getNativeSelection();
}
private TreeItem[] _getNativeSelection() {
	GtkCList clist = new GtkCList ();
	OS.memmove(clist, handle, GtkCList.sizeof);
	switch (clist.selection_mode) {
//		case OS.GTK_SELECTION_SINGLE:   return getSelection_single();
		case OS.GTK_SELECTION_BROWSE:   return getSelection_browse();
//		case OS.GTK_SELECTION_MULTIPLE: return getSelection_multiple();
		case OS.GTK_SELECTION_EXTENDED: return getSelection_extended();
		default: error(SWT.ERROR_UNSPECIFIED);
	}
	/* can never get here */
	return null;
}

private TreeItem[] getSelection_browse () {
	GtkCList clist = new GtkCList();
	OS.memmove(clist, handle, GtkCList.sizeof);
	if (clist.selection==0) return new TreeItem[0];
	int length = OS.g_list_length (clist.selection);
	if (length == 0) return new TreeItem[0];
	int node = OS.g_list_nth_data (clist.selection, 0);
	TreeItem[] answer = new TreeItem[1];
	int index = OS.gtk_ctree_node_get_row_data(handle, node) - 1;
	if ((index<0) && (itemInInsertion!=null)) answer[0] = itemInInsertion;
	else answer[0] = items[index];
	return answer;
}
private TreeItem[] getSelection_extended () {
	GtkCList clist = new GtkCList();
	OS.memmove(clist, handle, GtkCList.sizeof);
	if (clist.selection==0) return new TreeItem[0];
	int length = OS.g_list_length (clist.selection);
	TreeItem[] result = new TreeItem[length];
	for (int i=0; i<length; i++) {
		int node = OS.g_list_nth_data (clist.selection, i);
		int index = OS.gtk_ctree_node_get_row_data(handle, node) - 1;
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
	// Native way:
	GtkCList clist = new GtkCList ();
	OS.memmove(clist, handle, GtkCList.sizeof);
	int selectionList = clist.selection;
	if (selectionList==0) return 0;
	return OS.g_list_length (clist.selection);
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
	_deselectAll();
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		OS.gtk_ctree_select (handle, item.handle);
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
	int rootNode = OS.gtk_ctree_node_nth(handle, 0);
	if (rootNode==0) return; // empty
	OS.gtk_ctree_select_recursive (handle, rootNode);	
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
	_deselectAll();
}
private void _deselectAll() {
	int rootNode = OS.gtk_ctree_node_nth(handle, 0);
	if (rootNode==0) return; // empty
	OS.gtk_ctree_unselect_recursive (handle, rootNode);
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
	if (selectedItem != null) showItem (selectedItem);
}

void destroyItem (TreeItem item) {
	int node = item.handle;
	Callback GtkCTreeFunc = new Callback (this, "GtkCTreeDispose", 3);
	int address = GtkCTreeFunc.getAddress ();
	OS.gtk_ctree_post_recursive (handle, node, address, 0);
	GtkCTreeFunc.dispose ();
	ignoreCollapse = true;
	OS.gtk_ctree_remove_node (handle, node);
	ignoreCollapse = false;
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
public TreeItem getItem(Point point) {
	int itemHeight = getItemHeight();
	int hitItemIndex = point.y / itemHeight;
	TreeItem hitItem;
	GtkCTree tree = new GtkCTree();
	OS.memmove(tree, handle, GtkCTree.sizeof);
	int [] row = new int [1];
	int [] column = new int [1];
	OS.gtk_clist_get_selection_info(handle, point.x, point.y, row, column);
	int data=OS.g_list_nth(tree.row_list, row[0]);
	for (int i = 0; i< items.length; i++)
		if (items[i].handle==data)
			return items[i];
		
	return null;
}

public boolean forceFocus () {
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	boolean result = super.forceFocus();
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
	return result;
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int root = OS.gtk_ctree_node_nth (handle, 0);
	if (root == 0) return 0;
	int data = OS.g_list_nth_data (root, 0);
	GtkCTreeRow row = new GtkCTreeRow ();
	OS.memmove (row, data, GtkCTreeRow.sizeof);
	int glist = row.sibling;
	if (glist == 0) return 0;
	return OS.g_list_length (glist);
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	GtkCList clist = new GtkCList ();
	OS.memmove (clist, handle, GtkCList.sizeof);
	return clist.row_height;
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

	// The node corresponding to the 0th row
	int root = OS.gtk_ctree_node_nth (handle, 0);
	if (root == 0) return new TreeItem [0];

	// The 0th row
	int data = OS.g_list_nth_data (root, 0);
	GtkCTreeRow row = new GtkCTreeRow ();
	OS.memmove (row, data, GtkCTreeRow.sizeof);
	
	int index = OS.gtk_ctree_node_get_row_data(handle, root) - 1;

	int glist = row.sibling;
	int count = OS.g_list_length (glist);
	TreeItem [] result = new TreeItem [count+1];
	result[0] = items[index];
	for (int i=0; i<count; i++) {
		int node = OS.g_list_nth (glist, i);
		index = OS.gtk_ctree_node_get_row_data (handle, node) - 1;
		result [i+1] = items [index];
	}
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return null;
}

int processCollapse (int int0, int int1, int int2) {
	if (ignoreCollapse) return 0;
	int index = OS.gtk_ctree_node_get_row_data (handle, int0) - 1;
	Event event = new Event ();
	event.item = items [index];
//	OS.gtk_clist_freeze (handle);
	sendEvent (SWT.Collapse, event);
//	OS.gtk_clist_thaw (handle);
	return 0;
}
int processExpand (int int0, int int1, int int2) {
	if (ignoreExpand) return 0;
	int index = OS.gtk_ctree_node_get_row_data (handle, int0) - 1;
	Event event = new Event ();
	event.item = items [index];
	OS.gtk_clist_freeze (handle);
	sendEvent (SWT.Expand, event);
	OS.gtk_clist_thaw (handle);
	boolean [] expanded = new boolean [1];
	OS.gtk_ctree_get_node_info (handle, int0, null, null, null, null, null, null, null, expanded);
	if (!expanded [0]) {
		ignoreExpand = true;
		OS.gtk_ctree_expand (handle, int0);
		ignoreExpand = false;
	}
	return 0;
}
int processSelection (int int0, int int1, int int2) {
	if (itemInInsertion != null) selectedItem = itemInInsertion;
	else {
		int index = OS.gtk_ctree_node_get_row_data (handle, int0) - 1;
		selectedItem = items [index];
	}
	Event event = new Event ();
	event.item = selectedItem;
	sendEvent (SWT.Selection, event);
	return 0;
}
int processMouseDown (int callData, int arg1, int int2) {
	OS.gtk_widget_grab_focus(handle);
	GdkEventButton gdkEvent = new GdkEventButton ();
	OS.memmove (gdkEvent, callData, GdkEventButton.sizeof);
	int eventType = SWT.MouseDown;
	if (gdkEvent.type == OS.GDK_2BUTTON_PRESS) {
		GtkCTree tree = new GtkCTree();
		OS.memmove(tree, handle, GtkCTree.sizeof);
		int row=((int)gdkEvent.y-tree.voffset)/(tree.row_height+1);
		int count = OS.g_list_length (tree.row_list);
		if (row >= count) return 1;
		int node = OS.g_list_nth(tree.row_list, row);
		int data = OS.g_list_nth_data(node, 0);
		GtkCTreeRow treerow = new GtkCTreeRow();
		OS.memmove (treerow, data, GtkCTreeRow.sizeof);
		if (OS.gtk_ctree_is_hot_spot(handle, (int)gdkEvent.x, (int)gdkEvent.y)){
			sendMouseEvent (eventType, gdkEvent.button, gdkEvent.state, gdkEvent.time, (int)gdkEvent.x, (int)gdkEvent.y);
			return 1;
		}
		if (treerow.children != 0) {
			eventType = SWT.MouseDoubleClick;
			sendMouseEvent (eventType, gdkEvent.button, gdkEvent.state, gdkEvent.time, (int)gdkEvent.x, (int)gdkEvent.y);
			return 1;
		}	 
		int index = OS.gtk_ctree_node_get_row_data (handle, node) - 1;
		selectedItem = items [index];
		Event event = new Event ();
		event.item=selectedItem;
		if (selectedItem != null) 
			sendEvent (SWT.DefaultSelection, event);
	}else{
		sendMouseEvent (eventType, gdkEvent.button, gdkEvent.state, gdkEvent.time, (int)gdkEvent.x, (int)gdkEvent.y);
	}
	if (gdkEvent.button == 3 && menu != null) {
		menu.setVisible (true);
	}
	return 1;
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	OS.gtk_ctree_remove_node (handle, 0);
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null && !item.isDisposed ()) {
			item.releaseWidget ();
			item.releaseHandle ();
		}
	}
	items = new TreeItem [4];
	selectedItem = null;
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int node = item.handle;
	if (OS.gtk_ctree_node_is_visible (handle, node) != OS.GTK_VISIBILITY_NONE) {
		return;
	}
	if (!OS.gtk_ctree_is_viewable (handle, node)) {
		ignoreExpand = true;
		int parent = node;
		GtkCTreeRow row = new GtkCTreeRow ();
		do {
			int data = OS.g_list_nth_data (parent, 0);
			OS.memmove (row, data, GtkCTreeRow.sizeof);
			if ((parent = row.parent) == 0) break;
			OS.gtk_ctree_expand (handle, parent);
		} while (true);
		ignoreExpand = false;
	}
	OS.gtk_ctree_node_moveto (handle, node, 0, 0.0f, 0.0f);
}

int GtkCTreeDispose (int ctree, int node, int data) {
	int index = OS.gtk_ctree_node_get_row_data (ctree, node) - 1;
	OS.gtk_ctree_node_set_row_data (ctree, node, 0);
	TreeItem item = items [index];
	if (item == selectedItem) selectedItem = null;
	item.releaseWidget ();
	item.releaseHandle ();
	items [index] = null;
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
	selectedItem = null;
	super.releaseWidget ();
}

}
