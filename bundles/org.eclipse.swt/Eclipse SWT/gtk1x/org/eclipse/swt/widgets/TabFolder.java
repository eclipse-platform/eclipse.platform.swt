package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class implement the notebook user interface
 * metaphor.  It allows the user to select a notebook page from
 * set of pages.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TabItem</code>.
 * <code>Control</code> children are created and then set into a
 * tab item using <code>TabItem#setControl</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class TabFolder extends Composite {
	int notebookHandle;
	TabItem [] items;


/*
 *   ==  CONSTRUCTORS  ==
 */

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
public TabFolder (Composite parent, int style) {
	super (parent, checkStyle (style));
}

/*
 *   ==  HANDLE DANCES, FIRST SPECIES  ==
 */

void createHandle (int index) {
	state |= HANDLE;
	eventBoxHandle = OS.gtk_event_box_new();
	fixedHandle = OS.gtk_fixed_new ();
	notebookHandle = OS.gtk_notebook_new ();
	handle = OS.gtk_fixed_new(); 
}

void configure () {
	_connectParent();
	OS.gtk_container_add(eventBoxHandle, fixedHandle);
	OS.gtk_fixed_put (fixedHandle, notebookHandle, (short)0, (short)0);
	OS.gtk_fixed_put (fixedHandle, handle, (short)2, (short)33);
}

void showHandle() {
	OS.gtk_widget_show(eventBoxHandle);
	OS.gtk_widget_show(fixedHandle);
	OS.gtk_widget_show(notebookHandle);
	OS.gtk_widget_show(handle);
	OS.gtk_widget_realize (notebookHandle);
	OS.gtk_widget_realize (handle);
}

void register () {
	super.register ();
	WidgetTable.put (notebookHandle, this);
}

void hookEvents () {
	super.hookEvents ();
	signal_connect (notebookHandle, "size_allocate", SWT.Resize, 3);
	signal_connect (notebookHandle, "switch_page", SWT.Selection, 4);
}

void createWidget (int index) {
	super.createWidget(index);
	items = new TabItem [4];
}

int topHandle () { return eventBoxHandle; }
int paintHandle () { return notebookHandle; }
int parentingHandle () { return handle; }
boolean isMyHandle(int h) {
	if (h==eventBoxHandle) return true;
	if (h==notebookHandle) return true;
	if (h==fixedHandle)  return true;
	if (h==handle)       return true;
	return false;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	//notebookHandle
	int width = _computeSize(wHint, hHint, changed).x;
	int height = 0;
	Point size;
	if (layout != null) {
		size = layout.computeSize (this, wHint, hHint, changed);
	} else {
		size = minimumSize ();
	}
	if (size.x == 0) size.x = DEFAULT_WIDTH;
	if (size.y == 0) size.y = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	width = Math.max (width, size.x);
	height = Math.max (height, size.y);
	Rectangle trim = computeTrim (0, 0, width, height);
	width = trim.width;  height = trim.height;
	return new Point (width, height);
}

/**
* Computes the widget trim.
*/
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	return new Rectangle(x-2, y-33, width+4, height+35);
}

/*
    **** Layout code ****
 */

boolean _setSize(int width, int height) {
	boolean differentExtent = UtilFuncs.setSize(eventBoxHandle, width,height);
	UtilFuncs.setSize (fixedHandle, width,height);
	UtilFuncs.setSize (notebookHandle, width,height);
	UtilFuncs.setSize (handle, width-4, height-35);
	layoutCurrent();
	return differentExtent;
}

public Rectangle _getClientArea () {
	org.eclipse.swt.graphics.Point size = _getSize();
	int x = Math.max(size.x-4, 3);
	int y = Math.max(size.y-35, 3);
	return new Rectangle(0,0, x, y);
}

void layoutCurrent() {
	int index=getSelectionIndex();
	if (index==-1) return;
	Control control = items[index].control;
	if (control==null) return;
	if (control.isDisposed()) return;
	control.setBounds(getClientArea());
}

void createItem (TabItem item, int index) {
	int list = OS.gtk_container_children (notebookHandle);
	int itemCount = OS.g_list_length (list);
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_ITEM_NOT_ADDED);
	if (itemCount == items.length) {
		TabItem [] newItems = new TabItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	
	// create a new label	
	byte [] buffer = new byte [] {0};
	int labelHandle = OS.gtk_label_new (buffer);

	// create a new fake page
	int stubPage = OS.gtk_fixed_new();
	
	// put the label and the fake page inside the notebook
	OS.gtk_signal_handler_block_by_data (notebookHandle, SWT.Selection);
	OS.gtk_notebook_append_page(notebookHandle, stubPage, labelHandle);
	OS.gtk_signal_handler_unblock_by_data (notebookHandle, SWT.Selection);
	
	OS.gtk_widget_show(labelHandle);
	OS.gtk_widget_show(stubPage);

	item.state |= HANDLE;
	item.handle = labelHandle;
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
	OS.gtk_notebook_set_show_tabs (notebookHandle, true);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * <code>widgetDefaultSelected</code> is not called.
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
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

void destroyItem (TabItem item) {
	int index = 0;
	int itemCount = getItemCount();
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	if (index == itemCount) error (SWT.ERROR_ITEM_NOT_REMOVED);
	OS.gtk_notebook_remove_page (notebookHandle, index);
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	item.handle = 0;
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

public TabItem getItem (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int list = OS.gtk_container_children (notebookHandle);
	int itemCount = OS.g_list_length (list);
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_CANNOT_GET_ITEM);
	return items [index];
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	//return itemCount;
	int list = OS.gtk_container_children (notebookHandle);
	return OS.g_list_length (list);
}
/**
 * Returns an array of <code>TabItem</code>s which are the items
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
public TabItem [] getItems () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int list = OS.gtk_container_children (notebookHandle);
	int itemCount = OS.g_list_length (list);
	TabItem [] result = new TabItem [itemCount];
	System.arraycopy (items, 0, result, 0, itemCount);
	return result;
}
/**
 * Returns an array of <code>TabItem</code>s that are currently
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
public TabItem [] getSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int index = OS.gtk_notebook_get_current_page (notebookHandle);
	if (index == -1) return new TabItem [0];
	return new TabItem [] {items [index]};
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return OS.gtk_notebook_get_current_page (notebookHandle);
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
public int indexOf (TabItem item) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int list = OS.gtk_container_children (notebookHandle);
	int itemCount = OS.g_list_length (list);
	for (int i=0; i<itemCount; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

int processSelection (int int0, int int1, int int2) {
	int index = OS.gtk_notebook_get_current_page (notebookHandle);
	if (index != -1) {
		Control control = items [index].getControl ();
		if (control != null && !control.isDisposed ()) {
			control.setVisible (false);
		}
	}
	Control control = items [int1].getControl ();
	if (control != null && !control.isDisposed ()) {
		control.setBounds(getClientArea());
		control.setVisible (true);
	}
	Event event = new Event();
	event.item = items[int1];
	postEvent(SWT.Selection, event);
	return 0;
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
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}
/**
 * Selects the item at the given zero-relative index in the receiver. 
 * If the item at the index was already selected, it remains selected.
 * The current selected is first cleared, then the new items are
 * selected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int index) {
	checkWidget();
	if (index == -1) return;
	OS.gtk_signal_handler_block_by_data (notebookHandle, SWT.Selection);
	OS.gtk_notebook_set_page (notebookHandle, index);
	OS.gtk_signal_handler_unblock_by_data (notebookHandle, SWT.Selection);
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selected is first cleared, then the new items are
 * selected.
 *
 * @param items the array of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (TabItem [] items) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (items.length == 0) {
		setSelection (-1);
		return;
	}
	for (int i=items.length-1; i>=0; --i) {
		int index = indexOf (items [i]);
		if (index != -1) setSelection (index);
	}
}

/*
 *   == DESTRUCTION ===
 */

void deregister () {
	super.deregister ();
	WidgetTable.remove (notebookHandle);
}

void releaseChildren() {
	int list = OS.gtk_container_children (notebookHandle);
	int itemCount = OS.g_list_length (list);
	for (int i=0; i<itemCount; i++) {
		TabItem item = items [i];
		if (!item.isDisposed ()) {
			item.releaseWidget ();
			item.releaseHandle ();
		}
	}
	
	// Now, the non-item children
	list = OS.gtk_container_children(parentingHandle());
	int childCount = OS.g_list_length (list);
	for (int i=0; i<childCount; i++) {
		int childHandle = OS.g_list_nth_data(list, i);
		if (!isMyHandle(childHandle)) {
			Widget w = WidgetTable.get(childHandle);
			if (!(w==null)  &&  !(w.isDisposed())) {
				w.releaseWidget();
				w.releaseHandle();
			}
		}
	}
}

void releaseHandle () {
	super.releaseHandle ();
	notebookHandle = 0;
}

void releaseWidget () {
	super.releaseWidget();
	items = null;
}

/*
 *   == AS YET UNCLASSIFIED ===
 */

static int checkStyle (int style) {
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}
}
