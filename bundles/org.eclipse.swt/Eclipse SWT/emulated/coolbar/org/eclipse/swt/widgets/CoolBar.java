package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import java.util.Vector;

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
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public /*final*/ class CoolBar extends Composite {
	Vector rows;
	Cursor hoverCursor;
	Cursor dragCursor;
	static final int ROW_SPACING = 2;
		
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
public CoolBar (Composite parent, int style) {
	super (parent, style);
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	return new Point (wHint, hHint);
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public CoolItem getItem (int index) {
	checkWidget();
	if (index < 0) error (SWT.ERROR_INVALID_RANGE);
	for (int i = 0; i < rows.size(); i++) {
		Vector row = (Vector) rows.elementAt(i);
		if (row.size() > index) {
			return (CoolItem) row.elementAt(index);			
		} else {
			index -= row.size();
		}
	}
	error (SWT.ERROR_INVALID_RANGE);
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget();
	int itemCount = 0;
	for (int i = 0; i < rows.size(); i++) {
		Vector row = (Vector) rows.elementAt(i);
		itemCount += row.size();
	}
	return itemCount;
}
/**
 * Returns an array of <code>CoolItems</code>s which are the
 * items in the receiver in the order those items were added. 
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the receiver's items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public CoolItem [] getItems () {
	checkWidget();
	CoolItem [] result = new CoolItem [getItemCount()];
	int index = 0;
	for (int i = 0; i < rows.size(); i++) {
		Vector row = (Vector) rows.elementAt(i);
		for (int j = 0; j < row.size(); j++) {
			result[index] = (CoolItem) row.elementAt(j);
			index++;	
		}
	}
	return result;
}
/**
 * Searches the receiver's items, in the order they were
 * added, starting at the first item (index 0) until an item
 * is found that is equal to the argument, and returns the
 * index of that item. If no item is found, returns -1.
 *
 * @param item the search item
 * @return the index of the item
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
	checkWidget();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	int answer = 0;
	for (int i = 0; i < rows.size(); i++) {
		Vector row = (Vector) rows.elementAt(i);
		for (int j = 0; j < row.size(); j++) {
			CoolItem next = (CoolItem) row.elementAt(j);
			if (next.equals(item)) {
				return answer;		
			} else {
				answer++;
			}
		}
	}
	return -1;
}
public void setBounds (int x, int y, int width, int height) {
	super.setBounds (x, y, width, height);
}
public void setSize (int width, int height) {
	super.setSize (width, height);
}
/**
 * Returns an array of zero-relative indices which map the order
 * that the items in the receiver were added in (which is the
 * order that they are returned by <code>getItems()</code>) to
 * the order which they are currently being displayed.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the receiver's item order
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int[] getItemOrder () {
	checkWidget();
	CoolItem[] items = getItems();
	int[] ids = new int[items.length];
	for (int i = 0; i < items.length; i++) {
		ids[i] = items[i].id;
	}
	return ids;
}
/**
 * Returns an array of points whose x and y coordinates describe
 * the widths and heights (respectively) of the items in the receiver
 * in the order the items were added.
 *
 * @return the receiver's item sizes
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point[] getItemSizes () {
	checkWidget();
	CoolItem[] items = getItems();
	Point[] sizes = new Point[items.length];
	for (int i = 0; i < items.length; i++) {
		sizes[i] = items[i].getSize();
	}
	return sizes;
}
/**
 * Returns an array of ints which describe the zero-relative
 * row number of the row which each of the items in the 
 * receiver occurs in, in the order the items were added.
 *
 * @return the receiver's wrap indices
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int[] getWrapIndices () {
	checkWidget();
	int[] data = new int[rows.size() - 1];
	int itemIndex = 0;
	for (int i = 0; i < rows.size() - 1; i++) {
		Vector row = (Vector) rows.elementAt(i);
		itemIndex += row.size();
		data[i] = itemIndex;
	}
	return data;
}
/**
 * Sets the row that each of the receiver's items will be
 * displayed in to the given array of ints which describe
 * the zero-relative row number of the row for each item 
 * in the order the items were added.
 *
 * @param indices the new wrap indices
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWrapIndices (int[] data) {
	checkWidget();
	if (data == null) error(SWT.ERROR_NULL_ARGUMENT);
	CoolItem[] items = getItems();
	rows = new Vector(5);
	int itemIndex = 0;
	for (int i = 0; i < data.length; i++) {
		int nextWrap = data[i];
		Vector row = new Vector(10);
		while (itemIndex < nextWrap) {
			row.addElement(items[itemIndex]);
			itemIndex++;
		}
		rows.addElement(row);
	}
	Vector row = new Vector(10);
	while (itemIndex < items.length) {
		row.addElement(items[itemIndex]);
		itemIndex++;
	}
	rows.addElement(row);
}
/**
 * Sets the receiver's item order, wrap indices, and item
 * sizes at once. This equivalent to calling the setter
 * methods for each of these values individually.
 *
 * @param itemOrder the new item order
 * @param wrapIndices the new wrap indices
 * @param size the new item sizes
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setItemLayout (int[] itemOrder, int[] wrapIndices, Point[] sizes) {
	checkWidget();
}
}
