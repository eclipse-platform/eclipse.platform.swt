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

/**
 * Instances of this class provide an area for dynamically
 * positioning the items they contain.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>CoolItem</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>FLAT, HORIZONTAL, VERTICAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * </p><p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#coolbar">CoolBar snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class CoolBar extends Composite {
	Control [] children;
	CoolItem [] items;
	int parentingHandle;
	int itemCount, childCount;
	
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
 * @see SWT
 * @see SWT#FLAT
 * @see SWT#HORIZONTAL
 * @see SWT#VERTICAL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public CoolBar (Composite parent, int style) {
	super (parent, checkStyle (style));
	if ((style & SWT.VERTICAL) != 0) {
		this.style |= SWT.VERTICAL;
	} else {
		this.style |= SWT.HORIZONTAL;
	}
}

static int checkStyle (int style) {
	style |= SWT.NO_FOCUS;
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

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
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

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	return computeSize (handle, wHint, hHint, changed);
}

void createHandle () {
	parentingHandle = OS.gcnew_Canvas ();
	if (parentingHandle == 0) error (SWT.ERROR_NO_HANDLES);
	handle = OS.gcnew_ToolBarTray ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	int children = OS.Panel_Children (parentingHandle);
	OS.UIElementCollection_Add (children, handle);
	OS.GCHandle_Free (children);
	if (IsVertical) OS.ToolBarTray_Orientation (handle, OS.Orientation_Vertical);
}

void createItem (CoolItem item, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	item.createWidget ();
	int toolbars = OS.ToolBarTray_ToolBars (handle);
	int itemHandle = item.topHandle ();
	OS.IList_Insert (toolbars, index, item.topHandle ());
	int count = OS.ICollection_Count (toolbars);
	if (itemCount == count) error (SWT.ERROR_ITEM_NOT_ADDED);
	int band = 0;
	int bandIndex = 0;
	if (index != itemCount) {
		band = OS.ToolBar_Band (items [index].topHandle ());
		bandIndex = OS.ToolBar_BandIndex (items [index].topHandle ());
		for (int i=0; i<count; i++) {
			int current = OS.IList_default (toolbars, i);
			int currentBand = OS.ToolBar_Band (current);
			int currentIndex = OS.ToolBar_BandIndex (current);
			if (currentBand == band && currentIndex >= bandIndex) {
				OS.ToolBar_BandIndex (current, currentIndex + 1);
			}
			OS.GCHandle_Free (current);
		}
	} else {
		if (itemCount > 0) {
			int [] log2vis = logicalToVisualIndices ();
			int lastItem = items [log2vis [itemCount - 1]].topHandle ();
			band = OS.ToolBar_Band (lastItem);
			bandIndex = OS.ToolBar_BandIndex (lastItem) + 1;
		}
	}
	OS.ToolBar_Band (itemHandle, band);
	OS.ToolBar_BandIndex (itemHandle, bandIndex);
	OS.GCHandle_Free (toolbars);
	if (itemCount == items.length) {
		CoolItem [] newItems = new CoolItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	System.arraycopy (items, index, items, index + 1, itemCount - index);
	items [index] = item;	
	itemCount++;
}

void createWidget () {
	super.createWidget ();
	items = new CoolItem [4];
	children = new Control [4];
}

int defaultBackground () {
	return OS.Colors_Transparent;
}

void deregister () {
	super.deregister ();
	display.removeWidget (parentingHandle);
}

void destroyItem (CoolItem item) {
	int toolbars = OS.ToolBarTray_ToolBars (handle);
	int itemHandle = item.topHandle ();
	int band =  OS.ToolBar_Band (itemHandle);
	int bandIndex = OS.ToolBar_BandIndex (itemHandle);
	OS.IList_Remove (toolbars, itemHandle);
	int count = OS.ICollection_Count (toolbars);
	if (itemCount == count) error (SWT.ERROR_ITEM_NOT_REMOVED);
	itemCount--;
	for (int i=0; i<itemCount; i++) {
		int current = OS.IList_default (toolbars, i);
		int currentBand = OS.ToolBar_Band (current);
		int currentIndex = OS.ToolBar_BandIndex (current);
		if (currentBand == band && currentIndex >= bandIndex) {
			OS.ToolBar_BandIndex (current, currentIndex - 1);
		}
		OS.GCHandle_Free (current);
	}
	OS.GCHandle_Free (toolbars);
	int index = 0;
	while (index < items.length) {
		if (items [index] == item) break;
		index++;
	}
	System.arraycopy (items, index + 1, items, index, itemCount - index);
	items [itemCount] = null;
}

Control [] _getChildren () {
	// return children in reverse order.
	Control[] result = new Control [childCount];
	for (int i =0; i < childCount; i++) {
		result [childCount - i - 1] = children [i]; 
	}
	return result;
}

int [] logicalToVisualIndices () {
	int [] bandLengths = new int [4];
	for (int i = 0; i < itemCount; i++) {
		int topHandle = items [i].topHandle ();
		int band = OS.ToolBar_Band (topHandle);
		int bandIndex = OS.ToolBar_BandIndex (topHandle);
		if (band >= bandLengths.length) {
			int [] newLengths = new int [band + 4];
			System.arraycopy (bandLengths, 0, newLengths, 0, bandLengths.length);
			bandLengths = newLengths;
		}
		if (bandIndex + 1 > bandLengths [band]) bandLengths [band] = bandIndex + 1;
	}
	int [] result = new int [itemCount];
	for (int i = 0; i < itemCount; i++) {
		int topHandle = items [i].topHandle ();
		int band = OS.ToolBar_Band (topHandle);
		int bandIndex = OS.ToolBar_BandIndex (topHandle);
		int index = bandIndex;
		for (int j=0; j<band; j++) index += bandLengths [j];
		result [index] = i;
	}
	return result;
}

/**
 * Returns the item that is currently displayed at the given,
 * zero-relative index. Throws an exception if the index is
 * out of range.
 *
 * @param index the visual index of the item to return
 * @return the item at the given visual index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public CoolItem getItem (int index) {
	checkWidget ();
	if (index < 0 || index >= itemCount) error (SWT.ERROR_INVALID_RANGE);
	int [] log2vis = logicalToVisualIndices ();
	return items [log2vis [index]];
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
 * Returns an array of zero-relative ints that map
 * the creation order of the receiver's items to the
 * order in which they are currently being displayed.
 * <p>
 * Specifically, the indices of the returned array represent
 * the current visual order of the items, and the contents
 * of the array represent the creation order of the items.
 * </p><p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the current visual order of the receiver's items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int [] getItemOrder () {
	checkWidget ();
	return logicalToVisualIndices ();
}

/**
 * Returns an array of <code>CoolItem</code>s in the order
 * in which they are currently being displayed.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the receiver's items in their current visual order
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public CoolItem [] getItems () {
	checkWidget ();
	CoolItem [] result = new CoolItem [itemCount];
	int [] log2vis = logicalToVisualIndices ();
	for (int i = 0; i < itemCount; i++) {
		result [i] = items [log2vis [i]];
	}
	return result;
}

/**
 * Returns an array of points whose x and y coordinates describe
 * the widths and heights (respectively) of the items in the receiver
 * in the order in which they are currently being displayed.
 *
 * @return the receiver's item sizes in their current visual order
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point [] getItemSizes () {
	checkWidget ();	
	int [] log2vis = logicalToVisualIndices ();
	Point [] result = new Point [itemCount];
	for (int i = 0; i < itemCount; i++) {
		result [i] = items [log2vis [i]].getSize ();
	}
	return result;
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
 * Returns whether or not the receiver is 'locked'. When a coolbar
 * is locked, its items cannot be repositioned.
 *
 * @return true if the coolbar is locked, false otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public boolean getLocked () {
	checkWidget ();
	return OS.ToolBarTray_IsLocked (handle);
}

/**
 * Returns an array of ints that describe the zero-relative
 * indices of any item(s) in the receiver that will begin on
 * a new row. The 0th visible item always begins the first row,
 * therefore it does not count as a wrap index.
 *
 * @return an array containing the receiver's wrap indices, or an empty array if all items are in one row
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int [] getWrapIndices () {
	checkWidget ();
	int bands = 1;
	int [] bandLengths = new int [4];
	for (int i = 0; i < itemCount; i++) {
		int topHandle = items [i].topHandle ();
		int band = OS.ToolBar_Band (topHandle);
		int bandIndex = OS.ToolBar_BandIndex (topHandle);
		if (band >= bandLengths.length) {
			int [] newLengths = new int [band + 4];
			System.arraycopy (bandLengths, 0, newLengths, 0, bandLengths.length);
			bandLengths = newLengths;
		}
		if (band != 0 && bandLengths [band] == 0) bands ++;
		if (bandIndex + 1 > bandLengths [band]) bandLengths [band] = bandIndex + 1;
	}
	if (bands == 1) return new int [0];
	int [] result = new int [bands-1];
	int sum = 0;
	for (int i = 0; i < result.length; i++) {
		sum += bandLengths [i];
		result [i] = sum;
	}
	return result;
}

void HandleSizeChanged (int sender, int e) {
	postEvent (SWT.Resize);
}

void hookEvents () {
	super.hookEvents ();
	int handler = OS.gcnew_SizeChangedEventHandler (jniRef, "HandleSizeChanged");
	OS.FrameworkElement_SizeChanged (handle, handler);
	OS.GCHandle_Free (handler);
}

/**
 * Searches the receiver's items in the order they are currently
 * being displayed, starting at the first item (index 0), until
 * an item is found that is equal to the argument, and returns
 * the index of that item. If no item is found, returns -1.
 *
 * @param item the search item
 * @return the visual order index of the search item, or -1 if the item is not found
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item is disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (CoolItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	for (int i = 0; i < itemCount; i++) {
		if (item.equals (items [i])) {
			int [] log2vis = logicalToVisualIndices ();
			return log2vis [i];
		}
	}
	return -1;
}

int parentingHandle () {
	return parentingHandle;
}

void register () {
	super.register ();
	display.addWidget (parentingHandle, this);
}

void releaseChildren (boolean destroy) {
	for (int i=0; i<itemCount; i++) {
		CoolItem item = items [i];
		if (item != null && !item.isDisposed ()) item.release (false);
	}
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
	for (int i=0; i<itemCount; i++) {
		CoolItem item = items [i];
		if (item.control == control) {
			item.setControl (null);
			break;
		}
	}
}

void reskinChildren (int flags) {
	if (items != null) {
		for (int i=0; i<items.length; i++) {
			CoolItem item = items [i];
			if (item != null) item.reskin (flags);
		}
	}
	super.reskinChildren (flags);
}

int setBounds (int x, int y, int width, int height, int flags) {
	int result = super.setBounds (x, y, width, height, flags);
	if ((result & RESIZED) != 0) {
		if ((style & SWT.VERTICAL) != 0) {
			OS.FrameworkElement_Height (handle, height);
		} else {
			OS.FrameworkElement_Width (handle, width);
		}
	}
	return result;
}

/**
 * Sets the receiver's item order, wrap indices, and item sizes
 * all at once. This method is typically used to restore the
 * displayed state of the receiver to a previously stored state.
 * <p>
 * The item order is the order in which the items in the receiver
 * should be displayed, given in terms of the zero-relative ordering
 * of when the items were added.
 * </p><p>
 * The wrap indices are the indices of all item(s) in the receiver
 * that will begin on a new row. The indices are given in the order
 * specified by the item order. The 0th item always begins the first
 * row, therefore it does not count as a wrap index. If wrap indices
 * is null or empty, the items will be placed on one line.
 * </p><p>
 * The sizes are specified in an array of points whose x and y
 * coordinates describe the new widths and heights (respectively)
 * of the receiver's items in the order specified by the item order.
 * </p>
 *
 * @param itemOrder an array of indices that describe the new order to display the items in
 * @param wrapIndices an array of wrap indices, or null
 * @param sizes an array containing the new sizes for each of the receiver's items in visual order
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if item order or sizes is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if item order or sizes is not the same length as the number of items</li>
 * </ul>
 */
public void setItemLayout (int [] itemOrder, int [] wrapIndices, Point [] sizes) {
	checkWidget ();
	setItemOrder (itemOrder);
	setWrapIndices (wrapIndices);
	setItemSizes (sizes);
}

/*
 * Sets the order that the items in the receiver should 
 * be displayed in to the given argument which is described
 * in terms of the zero-relative ordering of when the items
 * were added.
 *
 * @param itemOrder the new order to display the items in
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item order is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item order is not the same length as the number of items</li>
 * </ul>
 */
void setItemOrder (int [] itemOrder) {
	checkWidget ();
	if (itemOrder == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (itemOrder.length != itemCount) error (SWT.ERROR_INVALID_ARGUMENT);
	/* Ensure that itemOrder does not contain any duplicates. */
	boolean [] set = new boolean [itemCount];
	for (int i=0; i<itemOrder.length; i++) {
		int index = itemOrder [i];
		if (index < 0 || index >= itemCount) error (SWT.ERROR_INVALID_RANGE);
		if (set [index]) error (SWT.ERROR_INVALID_ARGUMENT);
		set [index] = true;
	}
	for (int i=0; i<itemCount; i++) {
		CoolItem item = items [itemOrder [i]];
		int itemHandle = item.topHandle (); 
		OS.ToolBar_Band (itemHandle, 0);
		OS.ToolBar_BandIndex (itemHandle, i);
	}
}

/*
 * Sets the width and height of the receiver's items to the ones
 * specified by the argument, which is an array of points whose x
 * and y coordinates describe the widths and heights (respectively)
 * in the order in which the items are currently being displayed.
 *
 * @param sizes an array containing the new sizes for each of the receiver's items in visual order
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of sizes is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the array of sizes is not the same length as the number of items</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
void setItemSizes (Point [] sizes) {
	if (sizes == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (sizes.length != itemCount) error (SWT.ERROR_INVALID_ARGUMENT);
	int [] log2vis = logicalToVisualIndices ();
	for (int i=0; i<itemCount; i++) {
		items [log2vis [i]].setSize (sizes [i].x, sizes [i].y);
	}
}

/**
 * Sets whether or not the receiver is 'locked'. When a coolbar
 * is locked, its items cannot be repositioned.
 *
 * @param locked lock the coolbar if true, otherwise unlock the coolbar
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public void setLocked (boolean locked) {
	checkWidget ();
	OS.ToolBarTray_IsLocked (handle, locked);
}

/**
 * Sets the indices of all item(s) in the receiver that will
 * begin on a new row. The indices are given in the order in
 * which they are currently being displayed. The 0th item
 * always begins the first row, therefore it does not count
 * as a wrap index. If indices is null or empty, the items
 * will be placed on one line.
 *
 * @param indices an array of wrap indices, or null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWrapIndices (int [] indices) {
	checkWidget ();
	if (indices == null) indices = new int [0];
	int count = getItemCount ();
	for (int i=0; i<indices.length; i++) {
		if (indices [i] < 0 || indices [i] >= count) {
			error (SWT.ERROR_INVALID_RANGE);
		}	
	}
	sortAscending (indices);
	int [] log2vis = logicalToVisualIndices ();
	int band = 0;
	int bandIndex = 0;
	int wrapIndex = 0;
	for (int i = 0; i < itemCount; i++) {
		int wrap = indices.length > wrapIndex ? indices [wrapIndex] : itemCount;
		if (i == wrap) {
			if (wrap != 0) {
				band ++;
				bandIndex = 0;
			}
			wrapIndex ++;
		}
		int topHandle = items [log2vis [i]].topHandle ();
		OS.ToolBar_Band (topHandle, band);
		OS.ToolBar_BandIndex (topHandle, bandIndex);
		bandIndex ++;
	}
}

int topHandle () {
	return parentingHandle;
}
}
