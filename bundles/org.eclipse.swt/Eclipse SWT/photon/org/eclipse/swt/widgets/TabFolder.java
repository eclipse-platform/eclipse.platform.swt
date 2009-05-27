/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.widgets.TabItem;
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
 * <dd>TOP, BOTTOM</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles TOP and BOTTOM may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tabfolder">TabFolder, TabItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TabFolder extends Composite {
	int parentingHandle;
	TabItem [] items;
	int itemCount, currentIndex = OS.Pt_PG_INVALID;

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
 * @see SWT
 * @see SWT#TOP
 * @see SWT#BOTTOM
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TabFolder (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	style = checkBits (style, SWT.TOP, SWT.BOTTOM, 0, 0, 0, 0);
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
 * be notified when the user changes the receiver's selection, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified when the user changes the receiver's selection
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
		changed |= (state & LAYOUT_CHANGED) != 0;
		size = layout.computeSize (this, wHint, hHint, changed);
		state &= ~LAYOUT_CHANGED;
	} else {
		size = minimumSize (wHint, hHint, changed);
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
	int trimY;
	if ((style & SWT.BOTTOM) != 0) {
		trimY = y - args [4];
	} else {
		trimY = y - (dim.h - args [1]);
	}
	int trimWidth = width + args [7] + args [10];
	int trimHeight = height + dim.h;
	return new Rectangle (trimX, trimY, trimWidth, trimHeight);
}

void createHandle (int index) {
	state |= HANDLE;
	int parentHandle = parent.parentingHandle ();
	int [] args = {
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	parentingHandle = OS.PtCreateWidget (OS.PtContainer (), parentHandle, args.length / 3, args);
	if (parentingHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int clazz = display.PtPanelGroup;
	args = new int []{
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
		OS.Pt_ARG_PG_FLAGS,  (style & SWT.BOTTOM) != 0 ? OS.Pt_PG_SELECTOR_ON_BOTTOM : 0, OS.Pt_PG_SELECTOR_ON_BOTTOM,  
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
	int j = 0;
	int [] str = new int [1];
	int [] address = new int [1];
	for (int i=0; i<=count; i++) {
		if (i == index) {
			str [0] = OS.malloc (1);
			OS.memset(str [0], 0, 1);
		} else {
			OS.memmove (address, oldPtr + (j++ * 4), 4);
			str [0] = OS.strdup (address [0]);
		}
		if (str [0] == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
		OS.memmove (newPtr + (i * 4), str, 4);
	}
	OS.PtSetResource (handle, OS.Pt_ARG_PG_PANEL_TITLES, newPtr, count + 1);
	for (int i=0; i<=count; i++) {
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
	int j = 0;
	int [] str = new int [1];
	int [] address = new int [1];
	for (int i=0; i<count; i++) {
		if (i == index) continue;
		OS.memmove (address, oldPtr + (i * 4), 4);
		str [0] = OS.strdup (address [0]);
		if (str [0] == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
		OS.memmove (newPtr + (j++ * 4), str, 4);
	}
	OS.PtSetResource (handle, OS.Pt_ARG_PG_PANEL_TITLES, newPtr, count - 1);
	for (int i=0; i<count-1; i++) {
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
	
	int args[] = {
		OS.Pt_ARG_MARGIN_RIGHT, 0, 0,
		OS.Pt_ARG_MARGIN_BOTTOM, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	
	PhArea_t parentArea = new PhArea_t();
	OS.PtWidgetArea(handle, parentArea);
	int deltaX = area.pos_x - parentArea.pos_x; 
    int deltaY = area.pos_y - parentArea.pos_y;
    area.size_w = (short) (parentArea.size_w - ( deltaX + args[1]));
    area.size_h = (short) (parentArea.size_h - ( deltaY + args [4]));
    
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
 * Returns the tab item at the given point in the receiver
 * or null if no such item exists. The point is in the
 * coordinate system of the receiver.
 *
 * @param point the point used to locate the item
 * @return the tab item at the given point, or null if the point is not in a tab item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public TabItem getItem (Point point) {
	checkWidget();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int index = 0; index < itemCount; index++) {
		TabItem item = items[index];
		Rectangle bounds = item.getBounds();
		if (bounds.contains(point)) return item;
	}
	return null;
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
	int index;
	if (currentIndex != OS.Pt_PG_INVALID && !OS.PtWidgetIsRealized (handle)) {
		index = currentIndex;
	} else {
		int [] args = {OS.Pt_ARG_PG_CURRENT_INDEX, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		index = args [1];
	}
	return index == OS.Pt_PG_INVALID ? -1 : index;
}

void hookEvents () {
	super.hookEvents ();
	int windowProc = display.windowProc;
	OS.PtAddCallback (handle, OS.Pt_CB_PG_PANEL_SWITCHING, windowProc, OS.Pt_CB_PG_PANEL_SWITCHING);
	OS.PtAddCallback (handle, OS.Pt_CB_REALIZED, windowProc, OS.Pt_CB_REALIZED);
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
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
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

Point minimumSize (int wHint, int hHint, boolean flushCache) {
	Control [] children = _getChildren ();
	int width = 0, height = 0;
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		int index = 0;
		while (index < itemCount) {
			if (items [index].control == child) break;
			index++;
		}
		if (index == itemCount) {
			Rectangle rect = child.getBounds ();
			width = Math.max (width, rect.x + rect.width);
			height = Math.max (height, rect.y + rect.height);
		} else {
			Point size = child.computeSize (wHint, hHint, flushCache);
			width = Math.max (width, size.x);
			height = Math.max (height, size.y);
		}
	}
	return new Point (width, height);
}

void moveToBack (int child) {
	OS.PtWidgetInsert (child, handle, 0);
}

int parentingHandle () {
	return parentingHandle;
}

int Pt_CB_PG_PANEL_SWITCHING (int widget, int info) {
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

int Pt_CB_REALIZED (int widget, int info) {
	int result = super.Pt_CB_REALIZED (widget, info);
	if (currentIndex != OS.Pt_PG_INVALID) {
		if (info == 0) return OS.Pt_END;
		PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
		OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
		if (cbinfo.reason == OS.Pt_CB_REALIZED) {
			setSelection (currentIndex, false);
			currentIndex = OS.Pt_PG_INVALID;
		}
	}
	return result;
}

void register () {
	super.register ();
	if (parentingHandle != 0) WidgetTable.put (parentingHandle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	parentingHandle = 0;
}

void releaseChildren (boolean destroy) {
	if (items != null) {
		for (int i=0; i<itemCount; i++) {
			TabItem item = items [i];
			if (item != null && !item.isDisposed ()) {
				item.release (false);
			} 
		}
		itemCount = 0;
		items = null;
	}
	super.releaseChildren (destroy);
}

void removeControl (Control control) {
	super.removeControl (control);
	for (int i=0; i<itemCount; i++) {
		TabItem item = items [i];
		if (item.control == control) item.setControl (null);
	}
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the user changes the receiver's selection.
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

int setBounds (int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	int result = super.setBounds (x, y, width, height, move, resize, events);
	if ((result & RESIZED) != 0) {
		int [] args = {OS.Pt_ARG_WIDTH, 0, 0, OS.Pt_ARG_HEIGHT, 0, 0};
		OS.PtGetResources (parentingHandle, args.length / 3, args);
		OS.PtSetResources (handle, args.length / 3, args);
		int index = getSelectionIndex ();
		if (index != -1) {
			TabItem item = items [index];
			Control control = item.control;
			if (control != null && !control.isDisposed ()) {
				control.setBounds (getClientArea ());
			}
		}
	}
	return result;
}

/**
 * Selects the item at the given zero-relative index in the receiver. 
 * If the item at the index was already selected, it remains selected.
 * The current selection is first cleared, then the new items are
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
	if (!(0 <= index && index < itemCount)) return;
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
	if (index == -1) index = OS.Pt_PG_INVALID;
	OS.PtSetResource (handle, OS.Pt_ARG_PG_CURRENT_INDEX, index, 0);	
	args [1] = 0;
	OS.PtGetResources (handle, args.length / 3, args);
	int newIndex = args [1];
	/*
	* Bug in Photon.  Pt_ARG_PG_CURRENT_INDEX cannot be set if
	* the widget is not realized.  The fix is to remember the current
	* index and reset it when the widget is realized. 
	*/
	if (!OS.PtWidgetIsRealized (handle)) {
		newIndex = currentIndex = index;
	}
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
 * Sets the receiver's selection to the given item.
 * The current selected is first cleared, then the new item is
 * selected.
 *
 * @param item the item to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setSelection (TabItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (new TabItem [] {item});
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selected is first cleared, then the new items are
 * selected.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (TabItem [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (items.length == 0) {
		setSelection (-1, false);
	} else {
		for (int i=items.length-1; i>=0; --i) {
			int index = indexOf (items [i]);
			if (index != -1) setSelection (index, false);
		}
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

int widgetClass () {
	return OS.PtPanelGroup ();
}

}
