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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.wpf.*;

/**
 * Instances of this class support the layout of selectable
 * tool bar items.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>ToolItem</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>FLAT, WRAP, RIGHT, HORIZONTAL, VERTICAL, SHADOW_OUT</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#toolbar">ToolBar, ToolItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ToolBar extends Composite {
	int parentingHandle, trayHandle;
	int itemCount;
	Control [] children;
	int childCount;
	
	//TEMPORARY CODE
	static boolean IsVertical;
	
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
 * @see SWT#FLAT
 * @see SWT#WRAP
 * @see SWT#RIGHT
 * @see SWT#HORIZONTAL
 * @see SWT#SHADOW_OUT
 * @see SWT#VERTICAL
 * @see Widget#checkSubclass()
 * @see Widget#getStyle()
 */
public ToolBar (Composite parent, int style) {
	super (parent, checkStyle (style));
	
	/*
	* Ensure that either of HORIZONTAL or VERTICAL is set.
	* NOTE: HORIZONTAL and VERTICAL have the same values
	* as H_SCROLL and V_SCROLL so it is necessary to first
	* clear these bits to avoid scroll bars and then reset
	* the bits using the original style supplied by the
	* programmer.
	*/
	if ((style & SWT.VERTICAL) != 0) {
		this.style |= SWT.VERTICAL;
	} else {
		this.style |= SWT.HORIZONTAL;
	}
}

static int checkStyle (int style) {
	IsVertical = (style & SWT.V_SCROLL) != 0;
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}

void addChild (Control widget) {
	super.addChild (widget);
	if (childCount == children.length) {
		Control [] newChildren = new Control [childCount + 4];
		System.arraycopy(children, 0, newChildren, 0, childCount);
		children = newChildren;
	}
	children [childCount++] = widget;
}

int backgroundProperty () {
	return OS.Control_BackgroundProperty ();
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	return super.computeSize (handle, wHint, hHint, changed);
}

void createHandle () {
	parentingHandle = OS.gcnew_Canvas ();
	if (parentingHandle == 0) error (SWT.ERROR_NO_HANDLES);
	handle = OS.gcnew_ToolBar ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	trayHandle = OS.gcnew_ToolBarTray ();
	if (trayHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int brush = OS.Brushes_Transparent ();
	OS.ToolBarTray_Background (trayHandle, brush);
	OS.GCHandle_Free (brush);
	int toolbars = OS.ToolBarTray_ToolBars (trayHandle);
	OS.IList_Add (toolbars, handle);
	OS.GCHandle_Free (toolbars);
	if (IsVertical) OS.ToolBarTray_Orientation (trayHandle, OS.Orientation_Vertical);
	int children = OS.Panel_Children (parentingHandle);
	OS.UIElementCollection_Add (children, trayHandle);
	OS.GCHandle_Free (children);
	//FIXME: FLAT, WRAP, RIGHT, SHADOW_OUT
}

void createItem (ToolItem item, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	item.createWidget ();
	int items = OS.ItemsControl_Items (handle);
	OS.ItemCollection_Insert (items, index, item.topHandle ());
	int count = OS.ItemCollection_Count (items);
	OS.GCHandle_Free (items);
	if (itemCount == count) error (SWT.ERROR_ITEM_NOT_ADDED);
	itemCount++;
}

void createWidget () {
	super.createWidget();
	children = new Control [4];
	updateLayout (parentingHandle);
	setThumbVisibility (OS.Visibility_Collapsed);
	setButtonVisibility (OS.Visibility_Collapsed);
}

void deregister () {
	super.deregister ();
	display.removeWidget (parentingHandle);
}

int defaultBackground () {
	if ((style & SWT.FLAT) != 0) {
		return OS.Colors_Transparent;
	}
	return 0;
}

void destroyItem (ToolItem item) {
	int items = OS.ItemsControl_Items (handle);
	OS.ItemCollection_Remove (items,  item.topHandle ());
	int count = OS.ItemCollection_Count (items);
	OS.GCHandle_Free (items);
	if (itemCount == count) error (SWT.ERROR_ITEM_NOT_REMOVED);
	itemCount--;
}

Control [] _getChildren () {
	// return children in reverse order.
	Control[] result = new Control [childCount];
	for (int i =0; i < childCount; i++) {
		result [childCount - i - 1] = children [i]; 
	}
	return result;
}

ToolItem getItem (int items, int index) {
	int item = OS.ItemCollection_GetItemAt (items, index);
	ToolItem result = (ToolItem) display.getWidget (item);
	OS.GCHandle_Free (item);
	return result;
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
public ToolItem getItem (int index) {
	checkWidget ();
	if (index < 0 || index >= itemCount) error (SWT.ERROR_INVALID_RANGE);
	int items = OS.ItemsControl_Items (handle);
	ToolItem result = getItem (items, index); 
	OS.GCHandle_Free (items);
	return result;
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
public ToolItem getItem (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	ToolItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		Rectangle rect = items [i].getBounds ();
		if (rect.contains (point)) return items [i];
	}
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
	checkWidget ();
	return itemCount;
}

/**
 * Returns an array of <code>ToolItem</code>s which are the items
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
public ToolItem [] getItems () {
	checkWidget ();
	ToolItem [] result = new ToolItem [itemCount];
	int items = OS.ItemsControl_Items (handle);
	for (int i = 0; i < itemCount; i++) {
		result[i] = getItem (items, i);
	}
	OS.GCHandle_Free (items);
	return result;
}

/**
 * Returns the number of rows in the receiver. When
 * the receiver has the <code>WRAP</code> style, the
 * number of rows can be greater than one.  Otherwise,
 * the number of rows is always one.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getRowCount () {
	checkWidget ();
	//FIXME: when WRAP implemented
	return 1;
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
 *    <li>ERROR_NULL_ARGUMENT - if the tool item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the tool item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (ToolItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	int items = OS.ItemsControl_Items (handle);
	int index = OS.ItemCollection_IndexOf (items, item.topHandle ());
	OS.GCHandle_Free (items);
	return index;
}

boolean mnemonicHit (char key) {
	//TODO
	return false;
}

boolean mnemonicMatch (char key) {
//	for (int i=0; i<itemCount; i++) {
//		if (mnemonicMatch (items [i].textHandle, key)) return true;
//	}
	return false;
}

int parentingHandle () {
	return parentingHandle;
}

void register () {
	super.register ();
	display.addWidget (parentingHandle, this);
}

void releaseChildren (boolean destroy) {
	int items = OS.ItemsControl_Items (handle);
	for (int i=0; i<itemCount; i++) {
		ToolItem item = getItem (items, i);
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

void releaseHandle () {
	super.releaseHandle ();
	if (parentingHandle != 0) OS.GCHandle_Free (parentingHandle);
	parentingHandle = 0;
	if (trayHandle != 0) OS.GCHandle_Free (trayHandle);
	trayHandle = 0;
}

void removeControl (Control control) {
	super.removeControl (control);
	int items = OS.ItemsControl_Items (handle);
	for (int i=0; i<itemCount; i++) {
		ToolItem item = getItem (items, i);
		if (item.control == control) {
			item.setControl (null);
			break;
		}
	}
	OS.GCHandle_Free (items);
}

void setButtonVisibility (byte visibility) {
	int template = OS.Control_Template (handle);
	int overFlowName = createDotNetString ("OverflowGrid", false);
	int overFlowGrid = OS.FrameworkTemplate_FindName (template, overFlowName, handle);
	if (overFlowGrid != 0) {
		OS.UIElement_Visibility (overFlowGrid, visibility);
		OS.GCHandle_Free (overFlowGrid);
	}
	OS.GCHandle_Free (overFlowName);
	int borderName = createDotNetString ("MainPanelBorder", false);
	int border = OS.FrameworkTemplate_FindName (template, borderName, handle);
	if (border != 0) {
		int left = visibility == OS.Visibility_Visible ? 11 : 0;
		int margin = OS.gcnew_Thickness (0, 0, left, 0);
		OS.FrameworkElement_Margin (border, margin);
		OS.GCHandle_Free (border);
		OS.GCHandle_Free (margin);
	}
	OS.GCHandle_Free (borderName);
	OS.GCHandle_Free (template);	
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

void setThumbVisibility (byte visibility) {
	int template = OS.Control_Template (handle);
	int thumbName = createDotNetString ("ToolBarThumb", false);
	int thumb = OS.FrameworkTemplate_FindName (template, thumbName, handle);
	if (thumb != 0) {
		OS.UIElement_Visibility (thumb, visibility);
		OS.GCHandle_Free (thumb);
	}
	OS.GCHandle_Free (thumbName);
	OS.GCHandle_Free (template);	
}

int topHandle() {
	return parentingHandle;
}
}
