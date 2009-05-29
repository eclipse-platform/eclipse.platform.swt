/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.wpf.*;

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
	int itemCount;	
	Control [] children;
	int childCount;
	boolean ignoreSelection;

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

int backgroundProperty () {
	return OS.Control_BackgroundProperty ();
}

void addChild (Control widget) {
	super.addChild (widget);
	if (childCount == children.length) {
		Control [] newChildren = new Control [childCount + 4];
		System.arraycopy (children, 0, newChildren, 0, childCount);
		children = newChildren;
	}
	children [childCount++] = widget;
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
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	/*
	* When the SWT.TOP style has not been set, force the
	* tabs to be on the bottom for tab folders on PPC.
	*/
//	if (OS.IsPPC) {
//		if ((style & SWT.TOP) == 0) style |= SWT.BOTTOM;
//	}
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

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

int clientHandle () {
	int index = OS.Selector_SelectedIndex (handle);
	if (index != -1) {
		int items = OS.ItemsControl_Items (handle);
		TabItem item = getItem (items, index);
		OS.GCHandle_Free (items);
		return item.contentHandle;
	}
	return handle;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	Point size = super.computeSize (wHint, hHint, changed); 
	Point sizeTabFolder = computeSize (handle, wHint, hHint, changed);
	return new Point (Math.max(sizeTabFolder.x + 1, size.x + 10), size.y + sizeTabFolder.y + 10);
}

void createItem (TabItem item, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	item.createWidget ();
	int items = OS.ItemsControl_Items (handle);
	OS.ItemCollection_Insert (items, index, item.topHandle ());
	int count = OS.ItemCollection_Count (items);
	OS.GCHandle_Free (items);
	if (itemCount == count) error (SWT.ERROR_ITEM_NOT_ADDED);
	itemCount++;
}

void createHandle () {
	parentingHandle = OS.gcnew_Canvas ();
	if (parentingHandle == 0) error (SWT.ERROR_NO_HANDLES);
	handle = OS.gcnew_TabControl ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & SWT.BOTTOM) != 0) OS.TabControl_TabStripPlacement (handle, OS.Dock_Bottom);
	int children = OS.Panel_Children (parentingHandle);
	OS.UIElementCollection_Add (children, handle);
	OS.GCHandle_Free (children);
	OS.Canvas_SetLeft (handle, 0);
	OS.Canvas_SetTop (handle, 0);
}

void createWidget () {
	super.createWidget ();
	children = new Control [4];
}

void deregister () {
	super.deregister ();
	display.removeWidget (parentingHandle);
}

void destroyItem (TabItem item) {
	int items = OS.ItemsControl_Items (handle);
	OS.ItemCollection_Remove (items, item.topHandle ());
	int count = OS.ItemCollection_Count (items);
	OS.GCHandle_Free (items);
	if (itemCount == count) error (SWT.ERROR_ITEM_NOT_REMOVED);
	itemCount--;
}

Control findThemeControl () {
	return this;
}

Control [] _getChildren () {
	// return children in reverse order.
	Control[] result = new Control [childCount];
	for (int i =0; i < childCount; i++) {
		result [childCount - i - 1] = children [i]; 
	}
	return result;
}

public Rectangle getClientArea () {
	checkWidget ();
	Rectangle rect = super.getClientArea ();
	int clientHandle = clientHandle ();
	int topHandle = topHandle ();
	int point = OS.gcnew_Point (0, 0);
	int result = OS.UIElement_TranslatePoint (clientHandle, point, topHandle);
	rect.x = (int) OS.Point_X (result);
	rect.y = (int) OS.Point_Y (result);
	OS.GCHandle_Free (point);
	OS.GCHandle_Free (result);
	return rect;
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
	checkWidget ();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	int items = OS.ItemsControl_Items (handle);
	TabItem item = getItem (items, index); 
	OS.GCHandle_Free (items);
	return item;
}

TabItem getItem (int items, int index) {
	int item = OS.ItemCollection_GetItemAt (items, index);
	TabItem result = (TabItem) display.getWidget (item);
	OS.GCHandle_Free (item);
	return result;
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
	int items = OS.ItemsControl_Items (handle);
	for (int index = 0; index < itemCount; index++) {
		TabItem item = getItem (items, index); 
		Rectangle bounds = item.getBounds();
		if (bounds.contains(point)) {
			OS.GCHandle_Free (items);
			return item;
		}
	}
	OS.GCHandle_Free (items);
	return null;
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
	checkWidget ();
	TabItem [] result = new TabItem [itemCount];
	int items = OS.ItemsControl_Items (handle);
	for (int i=0; i<itemCount; i++) {
		result [i] = getItem (items, i);
	}
	OS.GCHandle_Free (items);
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
	checkWidget ();
	int index = OS.Selector_SelectedIndex (handle);
	if (index == -1) return new TabItem [0];
	int items = OS.ItemsControl_Items (handle);
	TabItem item = getItem (items, index);
	OS.GCHandle_Free (items);
	return new TabItem [] {item};
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
	checkWidget ();
	return OS.Selector_SelectedIndex (handle);
}

boolean hasItems () {
	return true;
}

void HandleSelectionChanged (int sender, int e) {
	if (!checkEvent (e)) return;
	int removed = OS.SelectionChangedEventArgs_RemovedItems (e);
	if (OS.ICollection_Count (removed) > 0) {
		int oldSelection = OS.IList_default (removed, 0);
		TabItem item = (TabItem) display.getWidget (oldSelection);
		OS.GCHandle_Free (oldSelection);
		Control control = item.getControl(); 
		if (control != null && !control.isDisposed ()) {
			control.setVisible (false);
		}
	}
	OS.GCHandle_Free (removed);
	int selectedItem = OS.Selector_SelectedItem (handle);
	if (selectedItem == 0) return;
	TabItem item = (TabItem) display.getWidget (selectedItem);
	OS.GCHandle_Free (selectedItem);
	
	Control control = item.getControl(); 
	if (control != null && !control.isDisposed ()) {
		control.setVisible (true);
	}
	if (ignoreSelection) return;
	Event event = new Event ();
	event.item = item;
	postEvent (SWT.Selection, event);
}

void hookEvents () {
	super.hookEvents ();
	int handler = OS.gcnew_SelectionChangedEventHandler (jniRef, "HandleSelectionChanged");
	OS.Selector_SelectionChanged (handle, handler);
	OS.GCHandle_Free (handler);
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
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int items = OS.ItemsControl_Items (handle);
	int index = OS.ItemCollection_IndexOf (items, item.topHandle ());
	OS.GCHandle_Free (items);
	return index;
}

Point minimumSize (int wHint, int hHint, boolean flushCache) {
	Control [] children = _getChildren ();
	int width = 0, height = 0;
	int items = OS.ItemsControl_Items (handle);
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		int index = 0;	
		while (index < itemCount) {
			TabItem item = getItem (items, index); 
			if (item.control == child) break;
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
	OS.GCHandle_Free (items);
	return new Point (width, height);
}

boolean mnemonicHit (char key) {
	//TODO
	return false;
}

boolean mnemonicMatch (char key) {
	boolean found = false;
	int items = OS.ItemsControl_Items (handle);
	for (int i=0; i<itemCount; i++) {
		TabItem item = getItem (items, i);
		if (mnemonicMatch (item.textHandle, key)) {
			found = true;
			break;
		}
	}
	OS.GCHandle_Free (items);
	return found;
}

int parentingHandle() {
	return parentingHandle;
}

void register () {
	super.register ();
	display.addWidget (parentingHandle, this);
}

void releaseChildren (boolean destroy) {
	int items = OS.ItemsControl_Items (handle);
	for (int i=0; i<itemCount; i++) {
		TabItem item = getItem (items, i);
		if (item != null && !item.isDisposed ()) item.release (false);
	}
	OS.GCHandle_Free (items);
	super.releaseChildren (destroy);
}

void removeChild (Control control) {
	super.removeChild (control);
	int index = 0;
	while (index < childCount) {
		if (children [index] == control) break;
		index++;
	}
	if (index == childCount) return;
	System.arraycopy (children, index+1, children, index, --childCount - index);
	children [childCount] = null;
}

void releaseHandle() {
	super.releaseHandle ();
	if (parentingHandle != 0) OS.GCHandle_Free (parentingHandle);
	parentingHandle = 0;
}

void removeControl (Control control) {
	super.removeControl (control);
	int items = OS.ItemsControl_Items (handle);
	for (int i=0; i<itemCount; i++) {
		TabItem item = getItem (items, i);
		if (item.control == control) { 
			item.setControl (null);
			break;
		}
	}
	OS.GCHandle_Free (items);	
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

int setBounds (int x, int y, int width, int height, int flags) {
	int result = super.setBounds (x, y, width, height, flags);
	if ((result & RESIZED) != 0) {
		OS.FrameworkElement_Height (handle, height);
		OS.FrameworkElement_Width (handle, width);
		int selectedItem = OS.Selector_SelectedItem (handle);
		if (selectedItem != 0) {
			TabItem item = (TabItem) display.getWidget (selectedItem);
			OS.GCHandle_Free (selectedItem);
			Control control = item.control;
			if (control != null && !control.isDisposed ()) {
				control.setBounds (getClientArea ());
			}
		}
	}
	return result;
}

void setForegroundBrush (int brush) {
	if (brush != 0) {
		OS.Control_Foreground (handle, brush);
	} else {
		int property = OS.Control_ForegroundProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
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
	checkWidget ();
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
	checkWidget ();
	if (!(0 <= index && index < itemCount)) return;
	setSelection (index, false);
}

void setSelection (int index, boolean notify) {
	int oldIndex = OS.Selector_SelectedIndex (handle);
	if (oldIndex != -1) {
		int items = OS.ItemsControl_Items (handle);
		TabItem item = getItem (items, oldIndex);
		OS.GCHandle_Free (items);
		Control control = item.control;
		if (control != null && !control.isDisposed ()) {
			control.setVisible (false);
		}
	}
	ignoreSelection = true;
	OS.Selector_SelectedIndex (handle, index);
	ignoreSelection = false;
	int newIndex = OS.Selector_SelectedIndex (handle);
	if (newIndex != -1) {
		int items = OS.ItemsControl_Items (handle);
		TabItem item = getItem (items, newIndex);
		OS.GCHandle_Free (items);
		Control control = item.control;
		if (control != null && !control.isDisposed ()) {
//			control.setBounds (getClientArea ());
			control.setVisible (true);
		}
		if (notify) {
			Event event = new Event ();
			event.item = item;
			sendEvent (SWT.Selection, event);
		}
	}
}

int topHandle() {
	return parentingHandle;
}

boolean traversePage (boolean next) {
//	GTK: OS.g_signal_emit_by_name (handle, OS.change_current_page, next ? 1 : -1);
	int count = getItemCount ();
	if (count <= 1) return false;
	int index = getSelectionIndex ();
	if (index == -1) {
		index = 0;
	} else {
		int offset = (next) ? 1 : -1;
		index = (index + offset + count) % count;
	}
	setSelection (index, true);
	if (index == getSelectionIndex ()) {
//		OS.SendMessage (handle, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
		return true;
	}
	return false;
}
}
