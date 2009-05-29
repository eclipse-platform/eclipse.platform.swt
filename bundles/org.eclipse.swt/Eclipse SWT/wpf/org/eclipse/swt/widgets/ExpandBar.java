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

import org.eclipse.swt.internal.wpf.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class support the layout of selectable
 * expand bar items.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>ExpandItem</code>.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>V_SCROLL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Expand, Collapse</dd>
 * </dl>
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see ExpandItem
 * @see ExpandEvent
 * @see ExpandListener
 * @see ExpandAdapter
 * @see <a href="http://www.eclipse.org/swt/snippets/#expandbar">ExpandBar snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.2
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ExpandBar extends Composite {
	int parentingHandle;
	Control [] children;
	int itemCount, childCount;
	int spacing;
	
	
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
 * @see SWT#V_SCROLL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ExpandBar (Composite parent, int style) {
	super (parent, checkStyle (style));
}

Control [] _getChildren () {
	// return children in reverse order.
	Control[] result = new Control [childCount];
	for (int i = 0; i < childCount; i++) {
		result [childCount - i - 1] = children [i]; 
	}
	return result;
}

void addChild (Control control) {
	super.addChild (control);
	if (childCount == children.length) {
		Control [] newChildren = new Control [childCount + 4];
		System.arraycopy (children, 0, newChildren, 0, childCount);
		children = newChildren;
	}
	children [childCount++] = control;
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when an item in the receiver is expanded or collapsed
 * by sending it one of the messages defined in the <code>ExpandListener</code>
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
 * @see ExpandListener
 * @see #removeExpandListener
 */
public void addExpandListener (ExpandListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Expand, typedListener);
	addListener (SWT.Collapse, typedListener);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

static int checkStyle (int style) {
	style &= ~SWT.H_SCROLL;
	return style;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	Point size = computeSize (handle, wHint, hHint, changed);
	Rectangle trim = computeTrim (0, 0, size.x, size.y);
	return new Point (trim.width, trim.height);
}

void createHandle () {
	state |= THEME_BACKGROUND;
	parentingHandle = OS.gcnew_Canvas ();
	if (parentingHandle == 0) error (SWT.ERROR_NO_HANDLES);
	scrolledHandle = OS.gcnew_ScrollViewer ();
	if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
	handle = OS.gcnew_StackPanel ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	int children = OS.Panel_Children (parentingHandle);
	OS.UIElementCollection_Add (children, scrolledHandle);
	OS.GCHandle_Free (children);
	OS.ContentControl_Content (scrolledHandle, handle);
}

void createItem (ExpandItem item, int style, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	item.createWidget ();
	int items = OS.Panel_Children (handle);
	OS.UIElementCollection_Insert (items, index, item.topHandle ());
	int count = OS.UIElementCollection_Count (items);
	OS.GCHandle_Free (items);
	if (itemCount == count) error (SWT.ERROR_ITEM_NOT_ADDED);
	itemCount++;
}

void createWidget () {
	super.createWidget ();
	children = new Control [4];
}

int defaultBackground () {
	return OS.SystemColors_ControlColor;
}

void deregister () {
	super.deregister ();
	display.removeWidget (parentingHandle);
}

void destroyItem (ExpandItem item) {
	int items = OS.Panel_Children (handle);
	OS.UIElementCollection_Remove (items, item.topHandle ());
	int count = OS.UIElementCollection_Count (items);
	OS.GCHandle_Free (items);
	if (itemCount == count) error (SWT.ERROR_ITEM_NOT_REMOVED);
	itemCount--;
}

void fixScrollbarVisibility () {
	OS.ScrollViewer_SetHorizontalScrollBarVisibility (scrolledHandle, OS.ScrollBarVisibility_Hidden);
	if ((style & SWT.V_SCROLL) == 0) {
		OS.ScrollViewer_SetVerticalScrollBarVisibility (scrolledHandle, OS.ScrollBarVisibility_Hidden);
	} else {
		OS.ScrollViewer_SetVerticalScrollBarVisibility (scrolledHandle, OS.ScrollBarVisibility_Auto);
	}
}

Point getLocation (Control child) {
	int topHandle = child.topHandle ();
	int point = OS.gcnew_Point (0, 0);
	if (point == 0) error (SWT.ERROR_NO_HANDLES);
	int location = OS.UIElement_TranslatePoint (topHandle, point, handle);
	int x = (int) OS.Point_X (location);
	int y = (int) OS.Point_Y (location);
	OS.GCHandle_Free (point);
	OS.GCHandle_Free (location);
	return new Point (x, y);
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
public ExpandItem getItem (int index) {
	checkWidget ();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	int items = OS.Panel_Children (handle);
	ExpandItem item = getItem (items, index);
	OS.GCHandle_Free (items);
	return item;
}

ExpandItem getItem (int items, int index) {
	int item = OS.UIElementCollection_default (items, index);
	ExpandItem result = (ExpandItem) display.getWidget (item);
	OS.GCHandle_Free (item);
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
	checkWidget ();
	return itemCount;
}

/**
 * Returns an array of <code>ExpandItem</code>s which are the items
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
public ExpandItem [] getItems () {
	checkWidget ();
	ExpandItem [] result = new ExpandItem [itemCount];
	int items = OS.Panel_Children (handle);
	for (int i=0; i<itemCount; i++) {
		result [i] = getItem (items, i);
	}
	OS.GCHandle_Free (items);
	return result;
}

int getScrollBarHandle (int style) {
	if ((style & SWT.H_SCROLL) != 0) return 0;
	updateLayout (handle);
	int template = OS.Control_Template (scrolledHandle);
	int part = createDotNetString ("PART_VerticalScrollBar", false);
	int scrollbar = OS.FrameworkTemplate_FindName (template, part, scrolledHandle);
	OS.GCHandle_Free (part);
	OS.GCHandle_Free (template);
	return scrollbar;
}

/**
 * Returns the receiver's spacing.
 *
 * @return the spacing
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSpacing () {
	checkWidget ();
	return spacing;
}

boolean hasItems () {
	return true;
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
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (ExpandItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int items = OS.Panel_Children (handle);
	int index = OS.UIElementCollection_IndexOf (items, item.topHandle ());
	OS.GCHandle_Free (items);
	return index;
}

int parentingHandle() {
	return parentingHandle;
}

void register () {
	super.register ();
	display.addWidget (parentingHandle, this);
}

void releaseChildren (boolean destroy) {
	int items = OS.Panel_Children (handle);
	for (int i=0; i<itemCount; i++) {
		ExpandItem item = getItem (items, i);
		if (item != null && !item.isDisposed ()) item.release (false);
	}
	OS.GCHandle_Free (items);
	super.releaseChildren (destroy);
}

void releaseHandle () {
	super.releaseHandle ();
	if (parentingHandle != 0) OS.GCHandle_Free (parentingHandle);
	parentingHandle = 0;
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

void removeControl (Control control) {
	super.removeControl (control);
	int items = OS.Panel_Children (handle);
	for (int i=0; i<itemCount; i++) {
		ExpandItem item = getItem (items, i);
		if (item.control == control) { 
			item.setControl (null);
			break;
		}
	}
	OS.GCHandle_Free (items);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when items in the receiver are expanded or collapsed.
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
 * @see ExpandListener
 * @see #addExpandListener
 */
public void removeExpandListener (ExpandListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Expand, listener);
	eventTable.unhook (SWT.Collapse, listener);	
}

int setBounds (int x, int y, int width, int height, int flags) {
	int result = super.setBounds (x, y, width, height, flags);
	if ((result & RESIZED) != 0) {
		OS.FrameworkElement_Height (scrolledHandle, height);
		OS.FrameworkElement_Width (scrolledHandle, width);
	}
	return result;
}

/**
 * Sets the receiver's spacing. Spacing specifies the number of pixels allocated around 
 * each item.
 * 
 * @param spacing the spacing around each item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSpacing (int spacing) {
	checkWidget ();
	if (spacing < 0) return;
	if (spacing == this.spacing) return;
	this.spacing = spacing;
	int thickness = OS.gcnew_Thickness (spacing, spacing, spacing, spacing);
	int items = OS.Panel_Children (handle);
	for (int i = 0; i < itemCount; i++) {
		ExpandItem item = getItem (items, i);
		OS.FrameworkElement_Margin (item.handle, thickness);	
	}
	OS.GCHandle_Free (items);
	OS.GCHandle_Free (thickness);	
}

int topHandle () {
	return parentingHandle;
}
}