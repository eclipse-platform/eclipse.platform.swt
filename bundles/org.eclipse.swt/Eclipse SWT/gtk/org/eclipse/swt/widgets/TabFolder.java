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
	TabItem [] items;

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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

int clientHandle () {
	if (items [0] != null) return items [0].pageHandle;
	return handle;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
//	int width = OS.GTK_WIDGET_WIDTH (fixedHandle);
//	int height = OS.GTK_WIDGET_HEIGHT (fixedHandle);
//	OS.gtk_widget_set_size_request (handle, wHint, hHint);
//	GtkRequisition requisition = new GtkRequisition ();
//	OS.gtk_widget_size_request (handle, requisition);
//	OS.gtk_widget_set_size_request (handle, width, height);
//	width = wHint == SWT.DEFAULT ? requisition.width : wHint;
//	height = hHint == SWT.DEFAULT ? requisition.height : hHint;
//	Point size;
//	if (layout != null) {
//		size = layout.computeSize (this, wHint, hHint, changed);
//	} else {
//		size = minimumSize ();
//	}
//	if (size.x == 0) size.x = DEFAULT_WIDTH;
//	if (size.y == 0) size.y = DEFAULT_HEIGHT;
//	if (wHint != SWT.DEFAULT) size.x = wHint;
//	if (hHint != SWT.DEFAULT) size.y = hHint;
//	width = Math.max (width, size.x);
//	height = Math.max (height, size.y);
////	Rectangle trim = computeTrim (0, 0, width, height);
////	width = trim.width;  height = trim.height;
//	return new Point (width, height);
	return new Point (300, 300);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	return new Rectangle(x-2, y-33, width+4, height+35);
}

void createHandle (int index) {
	state |= HANDLE;
	fixedHandle = OS.gtk_fixed_new ();
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	handle = OS.gtk_notebook_new ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	int parentHandle = parent.parentingHandle ();
	OS.gtk_container_add (parentHandle, fixedHandle);
	OS.gtk_container_add (fixedHandle, handle);
	OS.gtk_widget_show (handle);
	OS.gtk_widget_show (fixedHandle);
	OS.gtk_notebook_set_scrollable (handle, true);
	OS.gtk_notebook_set_show_tabs (handle, true);
}

void createWidget (int index) {
	super.createWidget(index);
	items = new TabItem [4];
}

void createItem (TabItem item, int index) {
	int list = OS.gtk_container_get_children (handle);
	int itemCount = 0;
	if (list != 0) {
		itemCount = OS.g_list_length (list);
		OS.g_list_free (list);
	}
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_ITEM_NOT_ADDED);
	if (itemCount == items.length) {
		TabItem [] newItems = new TabItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	int labelHandle = OS.gtk_label_new ("");
	if (labelHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int pageHandle = OS.gtk_fixed_new ();
	if (pageHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	OS.gtk_notebook_insert_page (handle, pageHandle, labelHandle, index);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
	OS.gtk_widget_show (labelHandle);
	OS.gtk_widget_show (pageHandle);
	item.state |= HANDLE;
	item.handle = labelHandle;
	item.pageHandle = pageHandle;
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
	item.setForegroundColor (getForegroundColor ());
	item.setFontDescription (getFontDescription ());
}

void destroyItem (TabItem item) {
	int index = 0;
	int itemCount = getItemCount();
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	if (index == itemCount) error (SWT.ERROR_ITEM_NOT_REMOVED);
	OS.gtk_notebook_remove_page (handle, index);
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	item.handle = 0;
}

int eventHandle () {
	return fixedHandle;
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
	checkWidget();
	int list = OS.gtk_container_get_children (handle);
	if (list == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
	int itemCount = OS.g_list_length (list);
	OS.g_list_free (list);
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
	checkWidget();
	int list = OS.gtk_container_get_children (handle);
	if (list == 0) return 0;
	int itemCount = OS.g_list_length (list);
	OS.g_list_free (list);
	return itemCount;
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
	checkWidget();
	int count = getItemCount ();
	TabItem [] result = new TabItem [count];
	System.arraycopy (items, 0, result, 0, count);
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
	checkWidget();
	int index = OS.gtk_notebook_get_current_page (handle);
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
	checkWidget();
	return OS.gtk_notebook_get_current_page (handle);
}

void hookEvents () {
	super.hookEvents ();
	signal_connect (handle, "switch_page", SWT.Selection, 4);
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
	checkWidget();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int list = OS.gtk_container_get_children (handle);
	if (list == 0) return -1;
	int count = OS.g_list_length (list);
	OS.g_list_free (list);
	for (int i=0; i<count; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

int processSelection (int int0, int int1, int int2) {
	int index = OS.gtk_notebook_get_current_page (handle);
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

void releaseWidget () {
	int count = getItemCount ();
	for (int i=0; i<count; i++) {
		TabItem item = items [i];
		if (!item.isDisposed ()) item.releaseWidget ();
	}
	items = null;
	super.releaseWidget ();
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
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	boolean changed = super.setBounds (x, y, width, height, move, resize);
	if (changed && resize) {
		int index = getSelectionIndex ();
		if (index != -1) {
			TabItem item = items [index];
			Control control = item.control;
			if (control != null && !control.isDisposed ()) {
				control.setBounds (getClientArea ());
			}
		}
	}
	return changed;
}

void setFontDescription (int font) {
	super.setFontDescription (font);
	TabItem [] items = getItems ();
	for (int i = 0; i < items.length; i++) {
		if (items[i] != null) {
			items[i].setFontDescription (font);
		}
	}
}

void setForegroundColor (GdkColor color) {
	super.setForegroundColor (color);
	TabItem [] items = getItems ();
	for (int i = 0; i < items.length; i++) {
		if (items[i] != null) {
			items[i].setForegroundColor (color);
		}
	}
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
	checkWidget ();
	setSelection (index, false);
}

void setSelection (int index, boolean notify) {
	if (index < 0) return;
	int oldIndex = OS.gtk_notebook_get_current_page (handle);
	if (oldIndex != -1) {
		TabItem item = items [oldIndex];
		Control control = item.control;
		if (control != null && !control.isDisposed ()) {
			control.setVisible (false);
		}
	}
	OS.gtk_signal_handler_block_by_data (handle, SWT.Selection);
	OS.gtk_notebook_set_page (handle, index);
	OS.gtk_signal_handler_unblock_by_data (handle, SWT.Selection);
	int newIndex = OS.gtk_notebook_get_current_page (handle);
	if (newIndex != -1) {
		TabItem item = items [newIndex];
		Control control = item.control;
		if (control != null && !control.isDisposed ()) {
			control.setBounds (getClientArea ());
			control.setVisible (true);
		}
		if (notify) {
			Event event = new Event ();
			event.item = item;
			sendEvent (SWT.Selection, event);
		}
	}
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
	checkWidget();
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

}
