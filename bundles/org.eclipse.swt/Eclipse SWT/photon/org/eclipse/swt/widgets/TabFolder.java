package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
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
	int parentingHandle;
	TabItem [] items;
	int itemCount;

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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	PhDim_t dim = new PhDim_t();
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidget (handle);
	OS.PtWidgetPreferredSize(handle, dim);
	int width = dim.w, height = dim.h;
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

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	PhDim_t dim = new PhDim_t();
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidget (handle);
	OS.PtWidgetPreferredSize(handle, dim);
	int [] args = {
		OS.Pt_ARG_MARGIN_BOTTOM, 0, 0, // 1
		OS.Pt_ARG_MARGIN_TOP, 0, 0, // 4
		OS.Pt_ARG_MARGIN_RIGHT, 0, 0, // 7
		OS.Pt_ARG_MARGIN_LEFT, 0, 0, // 10
//		OS.Pt_ARG_BASIC_FLAGS, 0, 0, // 13
	};
	OS.PtGetResources(handle, args.length / 3, args);
	int trimX = x - args [10];
	int trimY = y - (dim.h - args [1]);
	int trimWidth = width + args [7] + args [10];
	int trimHeight = height + dim.h;
	return new Rectangle (trimX, trimY, trimWidth, trimHeight);
}

void createHandle (int index) {
	state |= HANDLE;
	Display display = getDisplay ();
	int parentHandle = parent.parentingHandle ();
	int [] args = {
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	parentingHandle = OS.PtCreateWidget (OS.PtContainer (), parentHandle, args.length / 3, args);
	if (parentingHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int clazz = display.PtPanelGroup;
	args = new int []{
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (clazz, parentingHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

void createWidget (int index) {
	super.createWidget (index);
	items = new TabItem [4];
}

void createItem (TabItem item, int index) {
	int count = itemCount;
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	if (count == items.length) {
		TabItem [] newItems = new TabItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	int [] args = {OS.Pt_ARG_PG_PANEL_TITLES, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int oldPtr = args [1];
	int newPtr = OS.malloc ((count + 1) * 4);
	if (newPtr == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	int offset = 0;
	for (int i=0; i<count; i++) {
		if (i == index) offset = 1;
		int [] address = new int [1];
		OS.memmove (address, oldPtr + (i * 4), 4);
		int length = OS.strlen (address [0]);
		int str = OS.malloc (length + 1);
		if (str == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
		OS.memmove (str, address [0], length + 1);
		OS.memmove (newPtr + ((i + offset) * 4), new int [] {str}, 4);
	}
	int str = OS.malloc (1);
	if (str == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	OS.memmove (newPtr + (index * 4), new int [] {str}, 4);
	OS.PtSetResource (handle, OS.Pt_ARG_PG_PANEL_TITLES, newPtr, count + 1);
	for (int i=0; i<count+1; i++) {
		int [] address = new int [1];
		OS.memmove (address, newPtr + (i * 4), 4);
		OS.free (address [0]);
	}
	OS.free (newPtr);
	System.arraycopy (items, index, items, index + 1, count - index);
	items [index] = item;
	itemCount++;
}

void deregister () {
	super.deregister ();
	if (parentingHandle != 0) WidgetTable.remove (parentingHandle);
}

void destroyItem (TabItem item) {
	int count = itemCount;
	int index = 0;
	while (index < count) {
		if (items [index] == item) break;
		index++;
	}
	int [] args = {OS.Pt_ARG_PG_PANEL_TITLES, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int oldPtr = args [1];
	int newPtr = OS.malloc ((count - 1) * 4);
	if (newPtr == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	int offset = 0;
	for (int i=0; i<count; i++) {
		if (i == index) {
			offset = -1;
			continue;
		}
		int [] address = new int [1];
		OS.memmove (address, oldPtr + (i * 4), 4);
		int length = OS.strlen (address [0]);
		int str = OS.malloc (length + 1);
		if (str == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
		OS.memmove (str, address [0], length + 1);
		OS.memmove (newPtr + ((i + offset) * 4), new int [] {str}, 4);
	}
	OS.PtSetResource (handle, OS.Pt_ARG_PG_PANEL_TITLES, newPtr, count - 1);
	for (int i=0; i<count-1; i++) {
		int [] address = new int [1];
		OS.memmove (address, newPtr + (i * 4), 4);
		OS.free (address [0]);
	}
	OS.free (newPtr);
	if (index != count) {
		System.arraycopy (items, index + 1, items, index, --count - index);
	}
	items [count] = null;
	itemCount--;
}

public Rectangle getClientArea () {
	checkWidget();
	PhArea_t area = new PhArea_t ();
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidgetFamily (handle);
	int clientHandle = OS.PtWidgetChildBack (handle);
	OS.PtWidgetArea (clientHandle, area);
	return new Rectangle (area.pos_x, area.pos_y, area.size_w, area.size_h);
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
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	return items [index];
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
	TabItem [] result = new TabItem [itemCount];
	System.arraycopy (items, 0, result, 0, result.length);
	return result;
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
	return itemCount;
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
	int index = getSelectionIndex ();
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
	int [] args = {OS.Pt_ARG_PG_CURRENT_INDEX, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1] == OS.Pt_PG_INVALID ? -1 : args [1];
}

void hookEvents () {
	super.hookEvents ();
	int windowProc = getDisplay ().windowProc;
	OS.PtAddCallback (handle, OS.Pt_CB_PG_PANEL_SWITCHING, windowProc, SWT.Selection);
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
	for (int i=0; i<itemCount; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

void moveToBack (int child) {
	OS.PtWidgetInsert (child, handle, 0);
}

int parentingHandle () {
	return parentingHandle;
}

int processPaint (int damage) {
	OS.PtSuperClassDraw (OS.PtPanelGroup (), handle, damage);
	sendPaintEvent (damage);
	return OS.Pt_CONTINUE;
}

int processSelection (int info) {
	if (info == 0) return OS.Pt_CONTINUE;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	short[] oldIndex = new short[1];
	short[] newIndex = new short[1];
	OS.memmove(oldIndex, cbinfo.cbdata + 8, 2);
	OS.memmove(newIndex, cbinfo.cbdata + 10, 2);
	Control oldControl = null;
	int index = oldIndex [0];
	TabItem oldItem = items [index];
	if (0 <= index && index < itemCount) oldControl = oldItem.control;
	Control newControl = null;
	index = newIndex [0];
	TabItem newItem = items [index];
	if (0 <= index && index < itemCount) newControl = newItem.control;
	if (oldControl != null && !oldControl.isDisposed()) oldControl.setVisible (false);
	if (newControl != null && !newControl.isDisposed()) {
		newControl.setBounds (getClientArea ());
		newControl.setVisible (true);
	}
	Event event = new Event ();
	event.item = newItem;
	postEvent (SWT.Selection, event);
	return OS.Pt_CONTINUE;
}

void register () {
	super.register ();
	if (parentingHandle != 0) WidgetTable.put (parentingHandle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	parentingHandle = 0;
}

void releaseWidget () {
	for (int i=0; i<itemCount; i++) {
		TabItem item = items [i];
		if (!item.isDisposed ()) {
			item.releaseWidget ();
			item.releaseHandle ();
		}
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	boolean changed = super.setBounds (x, y, width, height, move, resize);
	if (changed && resize) {
		int [] args = {OS.Pt_ARG_WIDTH, 0, 0, OS.Pt_ARG_HEIGHT, 0, 0};
		OS.PtGetResources (parentingHandle, args.length / 3, args);
		OS.PtSetResources (handle, args.length / 3, args);
		args = new int [] {OS.Pt_ARG_PG_CURRENT_INDEX, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		int index = args [1];
		if (index != OS.Pt_PG_INVALID) {
			TabItem item = items [index];
			Control control = item.control;
			if (control != null && !control.isDisposed ()) {
				control.setBounds (getClientArea ());
			}
		}
	}
	return changed;
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
	setSelection (index, false);
}

void setSelection (int index, boolean notify) {
	int [] args = {OS.Pt_ARG_PG_CURRENT_INDEX, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int oldIndex = args [1];
	if (oldIndex != OS.Pt_PG_INVALID) {
		TabItem item = items [oldIndex];
		Control control = item.control;
		if (control != null && !control.isDisposed ()) {
			control.setVisible (false);
		}
	}
	OS.PtSetResource (handle, OS.Pt_ARG_PG_CURRENT_INDEX, index, 0);	
	args [1] = 0;
	OS.PtGetResources (handle, args.length / 3, args);
	int newIndex = args [1];
	if (newIndex != OS.Pt_PG_INVALID) {
		TabItem item = items [newIndex];
		Control control = item.control;
		if (control != null && !control.isDisposed ()) {
			control.setBounds (getClientArea ());
			control.setVisible (true);
		}
		if (notify) {
			Event event = new Event ();
			event.item = item;
			postEvent (SWT.Selection, event);
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

boolean traversePage (boolean next) {
	int count = getItemCount ();
	if (count == 0) return false;
	int index = getSelectionIndex ();
	if (index == -1) {
		index = 0;
	} else {
		int offset = next ? 1 : -1;
		index = (index + offset + count) % count;
	}
	setSelection (index, true);
	return true;
}

int topHandle () {
	return parentingHandle;
}

}
