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
	boolean selected;
	int itemCount, columnCount, imageHeight;
	TableItem [] items;
	TableColumn [] columns;
	int check, uncheck;
	public static int MAX_COLUMNS = 32;

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
	if (wHint == SWT.DEFAULT) wHint = 200;
	return computeNativeSize (scrolledHandle, wHint, hHint, changed);
}

int createCheckPixmap(boolean checked) {
	/*
	 * The box will occupy the whole item width.
	 */
	GtkCList clist = new GtkCList (handle);
	int check_height = clist.row_height-1;
	int check_width = check_height;

	GdkVisual visual = new GdkVisual(OS.gdk_visual_get_system());
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

void createHandle (int index) {
	state |= HANDLE;
	fixedHandle = OS.gtk_fixed_new ();
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	scrolledHandle = OS.gtk_scrolled_window_new (0, 0);
	if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
	handle = OS.gtk_clist_new (MAX_COLUMNS);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
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

	/* We fake the number of columns, because we have to know beforehand.
	 * Initially all those fake columns are invisible
	 */
	byte [] buffer = new byte [1];
	OS.gtk_clist_set_column_title (handle, 0, buffer);
	for (int i=1; i<MAX_COLUMNS; i++) {
		OS.gtk_clist_set_column_visibility (handle, i, false);
	}

	/* Scrolling policy */
	int hscrollbar_policy = (style & SWT.H_SCROLL) != 0 ? OS.GTK_POLICY_ALWAYS : OS.GTK_POLICY_AUTOMATIC;
	int vscrollbar_policy = (style & SWT.V_SCROLL) != 0 ? OS.GTK_POLICY_ALWAYS : OS.GTK_POLICY_AUTOMATIC;
	OS.gtk_scrolled_window_set_policy (scrolledHandle, hscrollbar_policy, vscrollbar_policy);		

	if ((style & SWT.CHECK) != 0) {
		OS.gtk_widget_realize (handle);
		uncheck = createCheckPixmap (false);
		check = createCheckPixmap (true);
	}
}

void createItem (TableColumn column, int index) {
	if (!(0 <= index && index <= columnCount)) error (SWT.ERROR_ITEM_NOT_ADDED);
	if (columnCount == columns.length) {
		TableColumn [] newColumns = new TableColumn [columns.length + 4];
		System.arraycopy (columns, 0, newColumns, 0, columns.length);
		columns = newColumns;
	}
	OS.gtk_clist_set_column_visibility (handle, index, true);
	OS.gtk_clist_column_titles_passive (handle);
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
//	int ptr = OS.g_malloc (1);
	int [] strings = new int [MAX_COLUMNS];
//	for (int i=0; i<strings.length; i++) strings [i] = ptr;
	for (int i=0; i<strings.length; i++) strings [i] = 0;
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	int result = OS.gtk_clist_insert (handle, index, strings);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
//	OS.g_free (ptr);
	if ((style & SWT.CHECK) != 0) {
		OS.gtk_clist_set_pixtext (handle, index, 0, new byte [1], (byte) 2, uncheck, 0);
	}
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
}

void createWidget (int index) {
	super.createWidget (index);
	items = new TableItem [4];
	columns = new TableColumn [4];
	itemCount = columnCount = 0;
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
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	OS.gtk_clist_unselect_row (handle, index, 0);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
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
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	for (int i=start; i<=end; i++) {
		OS.gtk_clist_unselect_row (handle, i, 0);
	}
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
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
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	for (int i=0; i<indices.length; i++) {
		OS.gtk_clist_unselect_row (handle, indices [i], 0);
	}
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
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
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	OS.gtk_clist_unselect_all (handle);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
}

void destroyItem (TableColumn column) {
	int index = 0;
	while (index < columnCount) {
		if (columns [index] == column) break;
		index++;
	}
	if (index == columnCount) return;
	OS.gtk_clist_set_column_visibility (handle, index, false);
	OS.gtk_clist_set_column_title (handle, index, new byte [1]);
	System.arraycopy (columns, index + 1, columns, index, --columnCount - index);
	columns [columnCount] = null;
}

void destroyItem (TableItem item) {
	int index = 0;
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	if (index == itemCount) return;
	OS.gtk_clist_remove (handle, index);
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
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
	GtkCList clist = new GtkCList (handle);
	int clientX = pt.x;
	int clientY = pt.y - clist.column_title_area_height;
	if (clientY <= 0) return null;
	int [] row = new int [1], column = new int [1];
	if (OS.gtk_clist_get_selection_info (handle, clientX, clientY, row, column) == 0) {
		return null;
	}
	return items [row [0]];
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
	GtkCList clist = new GtkCList (handle);
	return clist.row_height;
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
	GtkCList clist = new GtkCList (handle);
	int list = clist.selection;
	if (list == 0) return new TableItem [0];
	int length = OS.g_list_length (list);
	TableItem [] result = new TableItem [length];
	int [] buffer = new int [1];
	for (int i=0; i<length; i++) {
		int index = OS.g_list_nth_data (list, i);
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
	GtkCList clist = new GtkCList (handle);
	if (clist.selection == 0) return 0;
	return OS.g_list_length (clist.selection);
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
	GtkCList clist = new GtkCList (handle);
	int list = clist.selection;
	if (OS.g_list_length (list) == 0) return -1;
	return OS.g_list_nth_data (list, 0);
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
	GtkCList clist = new GtkCList (handle);
	int list = clist.selection;
	int length = OS.g_list_length (list);
	int [] indices = new int [length];
	for (int i=0; i<length; i++) {
		indices [i] = OS.g_list_nth_data (list, i);
	}
	return indices;
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
	GtkCList widget = new GtkCList (handle);
	int list = widget.selection;
	int length = OS.g_list_length (list);
	for (int i=0; i<length; i++) {
		if (index == OS.g_list_nth_data (list, i)) return true;
	}
	return false;
}

int processSelection (int int0, int int1, int int2) {
	GtkCList clist = new GtkCList (handle);
	if (int0 != clist.focus_row) return 0;
	if ((style & SWT.MULTI) != 0) selected = false;
	int eventType = SWT.Selection;
	if (int2 != 0) {
		int type = OS.GDK_EVENT_TYPE (int2);
		if (type == OS.GDK_2BUTTON_PRESS) {
			eventType = SWT.DefaultSelection;
		}
	}
	postEvent (eventType);
	return 0;
}

int paintWindow () {
	OS.gtk_widget_realize (handle);
	GtkCList clist = new GtkCList (handle);
	return clist.clist_window;
}

int processEvent (int eventNumber, int int0, int int1, int int2) {
	if (eventNumber == 0) {
		switch (OS.GDK_EVENT_TYPE (int0)) {
			case OS.GDK_BUTTON_PRESS:
			case OS.GDK_2BUTTON_PRESS: {
				if ((style & SWT.MULTI) != 0) selected = true;
				if ((style & SWT.CHECK) != 0) {
					double [] px = new double [1], py = new double [1];
					OS.gdk_event_get_coords (int0, px, py);
					GtkCList clist = new GtkCList (handle);
					int x = (int) (px [0]);
					int y = (int) (py [0]) - clist.column_title_area_height;
					if (y > 0) {
						int [] row = new int [1], column = new int [1];
						if (OS.gtk_clist_get_selection_info (handle, x, y, row, column) != 0) {	
							int nX = clist.hoffset + 4;
							int nY = clist.voffset + (clist.row_height + 1) * row [0] + 2;
							int [] check_width = new int [1], check_height = new int [1];
							OS.gdk_drawable_get_size (check, check_width, check_height);
							if (nX <= x && x <= nX + check_width [0]) {
								if (nY <= y && y <= nY + check_height [0]) {
									TableItem item = items [row [0]];
									byte [] spacing = new byte [1];
									int [] pixmap = new int [1], mask = new int [1];
									OS.gtk_clist_get_pixtext (handle, row [0], 0, null, spacing, pixmap, mask);
									byte [] text = Converter.wcsToMbcs (null, item.getText (), true);
									pixmap [0] = pixmap [0] == check ? uncheck : check;
									OS.gtk_clist_set_pixtext (handle, row [0], 0, text, spacing [0], pixmap [0], mask [0]);
									Event event = new Event ();
									event.detail = SWT.CHECK;
									event.item = item;
									postEvent (SWT.Selection, event);
								}
							}
						}
					}
				}
				break;
			}
			case OS.GDK_BUTTON_RELEASE: {
				if ((style & SWT.MULTI) != 0) {
					/*
					* Feature in GTK.  When an item is reselected, GTK
					* does not issue notification.  The fix is to detect
					* that the mouse was released over a selected item when
					* no selection signal was set and issue a fake selection
					* event.
					*/
					double[] px = new double [1], py = new double [1];
					OS.gdk_event_get_coords (int0, px, py);
					int x = (int) (px[0]), y = (int) (py[0]);
					int [] row = new int [1], column = new int [1];
					if (OS.gtk_clist_get_selection_info (handle, x, y, row, column) != 0) {
						GtkCList clist = new GtkCList (handle);
						if (selected && clist.selection != 0) {
							int list = clist.selection;
							int length = OS.g_list_length (list);
							for (int i=0; i<length; i++) {
								if (row [0] == OS.g_list_nth_data (list, i)) {
									postEvent (SWT.Selection);
								}
							}
						}
					}
					selected = false;
				}
				break;
			}
		}
//		return 1;
	}
	return super. processEvent (eventNumber, int0, int1, int2);
}

int processKeyDown (int callData, int arg1, int int2) {
	int result = super.processKeyDown (callData, arg1, int2);
	/*
	* Feature in GTK.  When an item is reselected using
	* the space bar, GTK does not issue notification.
	* The fix is to ignore the notification that is sent
	* by GTK and look for the space key.
	*/
	int length = OS.gdk_event_key_get_length (callData);
	if (length == 1) {
		int string = OS.gdk_event_key_get_string (callData);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, string, length);
		char [] unicode = Converter.mbcsToWcs (null, buffer);
		switch (unicode [0]) {
			case ' ':
				GtkCList clist = new GtkCList (handle);
				if (clist.focus_row != -1) {
					Event event = new Event ();
					event.item = items [clist.focus_row];
					postEvent (SWT.Selection, event);
				}
				break;
		}
	}
	return result;
}

int processKeyUp (int callData, int arg1, int int2) {
	int result = super.processKeyUp (callData, arg1, int2);
	/*
	* Feature in GTK.  For some reason, when the selection
	* is extended using the shift key, the notification is
	* issued when the widget loses focus.  The fix is to force
	* the notification to be issued by temporarily losing and
	* gaining focus every time the shift key is released.
	*/
	int keyval = OS.gdk_event_key_get_keyval (callData);
	switch (keyval) {
		case OS.GDK_Shift_L:
		case OS.GDK_Shift_R:
			OS.gtk_widget_grab_focus (scrolledHandle);
			OS.gtk_widget_grab_focus (handle);
	}
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
	GtkCList clist = new GtkCList (handle);
	return -clist.voffset / (clist.row_height + 1);
}

void hookEvents () {
	//TO DO - get rid of enter/exit for mouse crossing border
	super.hookEvents ();
	signal_connect (handle, "select_row", SWT.Selection, 5);
	signal_connect (handle, "unselect_row", SWT.Selection, 5);
	signal_connect (handle, "event_after", 0, 3);
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

void releaseWidget () {
	for (int i=0; i<columnCount; i++) {
		TableColumn column = columns [i];
		if (!column.isDisposed ()) column.releaseWidget ();
	}
	columns = null;
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
		if (!item.isDisposed ()) item.releaseWidget ();
	}
	items = null;
	if (check != 0) OS.g_object_unref (check);
	if (uncheck != 0) OS.g_object_unref (uncheck);
	check = uncheck = 0;
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
	OS.gtk_clist_remove (handle, index);
	TableItem item = items [index];
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	item.releaseWidget ();
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
		OS.gtk_clist_remove (handle, index);
		items [index].releaseWidget ();
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
			OS.gtk_clist_remove (handle, index);
			// BUG - disposed callback could remove an item
			items [index].releaseWidget ();
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
	OS.gtk_clist_clear (handle);
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
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	OS.gtk_clist_select_row (handle, index, 0);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
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
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	for (int i=start; i<=end; i++) {
		OS.gtk_clist_select_row (handle, i, 0);
	}
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
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
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	for (int i=0; i<indices.length; i++) {
		OS.gtk_clist_select_row (handle, indices [i], 0);
	}
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
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
	OS.gtk_clist_select_all (handle);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
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
	return (OS.GTK_WIDGET_FLAGS (handle) & OS.GTK_CLIST_SHOW_TITLES) != 0;
}

public void setFont (Font font) {
	checkWidget ();
	super.setFont (font);
	if (imageHeight != 0) {
		OS.gtk_widget_realize (handle);
		OS.gtk_clist_set_row_height (handle, 0);
		GtkCList clist = new GtkCList (handle);
		if (imageHeight > clist.row_height) {
			OS.gtk_clist_set_row_height (handle, imageHeight);
		}
	}
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
	if (show) {
		OS.gtk_clist_column_titles_show (handle);
	} else {
		OS.gtk_clist_column_titles_hide (handle);
	}
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
	if (redraw) {
		OS.gtk_clist_thaw (handle);
	} else {
		OS.gtk_clist_freeze (handle);
	}
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
	checkWidget();
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	OS.gtk_clist_moveto (handle, index, 0, 0.0f, 0.0f);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
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
//	error(SWT.ERROR_NOT_IMPLEMENTED);
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
	setTopIndex (getSelectionIndex ());
}

}
