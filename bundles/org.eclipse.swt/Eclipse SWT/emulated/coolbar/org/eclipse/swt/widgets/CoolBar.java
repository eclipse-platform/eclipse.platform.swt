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
	Cursor hoverCursor, dragCursor;
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
	super (parent, checkStyle(style));
	rows = new Vector(5);
	rows.addElement(new Vector(10));
	hoverCursor = new Cursor(getDisplay(), SWT.CURSOR_SIZEWE);
	dragCursor = new Cursor(getDisplay(), SWT.CURSOR_SIZEALL);
	Listener listener = new Listener() {
		public void handleEvent(Event e) {
			switch (e.type) {
				case SWT.Paint:		
					processPaint(e);	
					break;
				case SWT.Dispose:
					rows = null;
					hoverCursor.dispose();
					dragCursor.dispose();					
					break;
			}
		}
	};
	addListener(SWT.Paint, listener);
	addListener(SWT.Dispose, listener);
}
private static int checkStyle(int style) {
	return style & SWT.BORDER;
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = wHint, height = hHint;
	if (wHint == SWT.DEFAULT) width = 0x7FFFFFFF;
	if (hHint == SWT.DEFAULT) height = 0x7FFFFFFF;
	Point extent = layout (width, false);
	if (wHint != SWT.DEFAULT) extent.x = wHint;
	if (hHint != SWT.DEFAULT) extent.y = hHint;
	return extent;
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
int getRowHeight (int rowIndex) {
	Vector row = (Vector) rows.elementAt(rowIndex);
	int height = 0;
	for (int i = 0; i < row.size(); i++) {
		CoolItem item = (CoolItem) row.elementAt(i);
		int itemHeight = item.getSize().y;
		height = Math.max(height, itemHeight);
	}
	return height;
}
int getRowIndex (CoolItem item) {
	for (int i = 0; i < rows.size(); i++) {
		Vector row = (Vector) rows.elementAt(i);
		for (int j = 0; j < row.size(); j++) {
			CoolItem next = (CoolItem) row.elementAt(j);
			if (next.equals(item)) return i;		
		}
	}
	return -1;
}
Vector getRow (CoolItem item) {
	for (int i = 0; i < rows.size(); i++) {
		Vector row = (Vector) rows.elementAt(i);
		for (int j = 0; j < row.size(); j++) {
			CoolItem next = (CoolItem) row.elementAt(j);
			if (next.equals(item)) return row;		
		}
	}
	return null;
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
/**
 * Insert the item into the row. Adjust the x and width values
 * appropriately.
 */
void insertItemIntoRow(CoolItem item, Vector row, int x_root, int rowY) {
	int barWidth = getSize().x;
	int height = item.getSize().y;
	if (row.size() == 0) {
		item.setBounds(0, rowY, barWidth, height);		
		row.addElement(item);
		return;
	}
	
	int x = Math.max(0, x_root - toDisplay(new Point(0, 0)).x);
	
	/* Find the insertion index and add the item. */
	int index;
	for (index = 0; index < row.size(); index++) {
		CoolItem next = (CoolItem) row.elementAt(index);
		if (x < next.getBounds().x) break;
	}
	row.insertElementAt(item, index);

	/* Adjust the width of the item to the left. */
	if (index > 0) {
		CoolItem left = (CoolItem) row.elementAt(index - 1);
		Rectangle leftBounds = left.getBounds();
		int newWidth = x - leftBounds.x;
		if (newWidth < CoolItem.MINIMUM_WIDTH) {
			x += CoolItem.MINIMUM_WIDTH - newWidth;
			newWidth = CoolItem.MINIMUM_WIDTH;
		}
		left.setBounds(leftBounds.x, leftBounds.y, newWidth, leftBounds.height);
		left.requestedWidth = newWidth;
	}
	
	/* Set the item's bounds. */
	int width = 0;
	if (index < row.size() - 1) {
		CoolItem right = (CoolItem) row.elementAt(index + 1);
		width = right.getBounds().x - x;
		if (width < CoolItem.MINIMUM_WIDTH) {
			moveRight(right, CoolItem.MINIMUM_WIDTH - width);
			width = right.getBounds().x - x;
		}
		item.setBounds(x, rowY, width, height);
		if (width < CoolItem.MINIMUM_WIDTH) moveLeft(item, CoolItem.MINIMUM_WIDTH - width);
	} else {
		width = Math.max(CoolItem.MINIMUM_WIDTH, barWidth - x);
		item.setBounds(x, rowY, width, height);
		if (x + width > barWidth) moveLeft(item, x + width - barWidth); 
	}
	item.requestedWidth = width;
}
void createItem (CoolItem item, int index) {
	int itemCount = getItemCount();
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	item.id = itemCount;
	if (index < itemCount) {
		for (int i = 0; i < rows.size(); i++) {
			Vector row = (Vector) rows.elementAt(i);
			if (row.size() > index) {
				row.insertElementAt(item, index);
				return;
			} else {
				index -= row.size();
			}
		}
	} else {
		Vector lastRow = (Vector) rows.lastElement();
		lastRow.addElement(item);	
	}
}
void moveDown(CoolItem item, int x_root) {
	int oldRowIndex = getRowIndex(item);
	Vector oldRowItems = (Vector) rows.elementAt(oldRowIndex);	
	if (oldRowIndex == rows.size() - 1 && oldRowItems.size() == 1) {
		/* This is the only item in the bottom row, don't move it. */
		return;
	}
	
	int newRowIndex = (oldRowItems.size() == 1) ? oldRowIndex : oldRowIndex + 1;
	removeItemFromRow(item, oldRowItems);
	
	int newRowY = ROW_SPACING;
	for (int i = 0; i < newRowIndex; i++) {
		newRowY += getRowHeight(i) + ROW_SPACING;	
	}
	
	if (newRowIndex == rows.size()) {
		/* Create a new bottom row for the item. */
		Vector newRow = new Vector(10);
		insertItemIntoRow(item, newRow, x_root, newRowY);
		rows.addElement(newRow);
		adjustItemHeights(oldRowIndex);
		return;
	}
	
	Vector newRowItems = (Vector) rows.elementAt(newRowIndex);
	insertItemIntoRow(item, newRowItems, x_root, newRowY);
	adjustItemHeights(oldRowIndex);
}
void moveLeft(CoolItem item, int pixels) {
	Vector row = getRow(item);
	int index = row.indexOf(item);
	if (index == 0) return;	
	Rectangle bounds = item.getBounds();
	int min = index * CoolItem.MINIMUM_WIDTH;
	int x = Math.max(min, bounds.x - pixels);
	CoolItem left = (CoolItem) row.elementAt(index - 1);
	Rectangle leftBounds = left.getBounds();
	if (leftBounds.x + CoolItem.MINIMUM_WIDTH >= x) {
		int shift = leftBounds.x + CoolItem.MINIMUM_WIDTH - x;
		moveLeft(left, shift);
		leftBounds = left.getBounds();
	}
	int leftWidth = Math.max(CoolItem.MINIMUM_WIDTH, leftBounds.width - pixels);
	left.setBounds(leftBounds.x, leftBounds.y, leftWidth, leftBounds.height);
	left.requestedWidth = leftWidth;
	int width = bounds.width + (bounds.x - x);
	item.setBounds(x, bounds.y, width, bounds.height);
	item.requestedWidth = width;
}
void moveRight(CoolItem item, int pixels) {
	Vector row = getRow(item);
	int index = row.indexOf(item);
	if (index == 0) return;	
	Rectangle bounds = item.getBounds();
	int minSpaceOnRight = (row.size() - index) * CoolItem.MINIMUM_WIDTH;
	int max = getBounds().width - minSpaceOnRight;
	int x = Math.min(max, bounds.x + pixels);	
	int width = 0;
	if (index + 1 == row.size()) {
		width = getBounds().width - x;
	} else {
		CoolItem right = (CoolItem) row.elementAt(index + 1);
		Rectangle rightBounds = right.getBounds();
		if (x + CoolItem.MINIMUM_WIDTH >= rightBounds.x) {
			int shift = x + CoolItem.MINIMUM_WIDTH - rightBounds.x;
			moveRight(right, shift);
			rightBounds = right.getBounds();
		}
		width = rightBounds.x - x;
	}
	item.setBounds(x, bounds.y, width, bounds.height);
	item.requestedWidth = width;
	CoolItem left = (CoolItem) row.elementAt(index - 1);
	Rectangle leftBounds = left.getBounds();
	int leftWidth = x - leftBounds.x;
	left.setBounds(leftBounds.x, leftBounds.y, leftWidth, leftBounds.height);
	left.requestedWidth = leftWidth;
}
void moveUp(CoolItem item, int x_root) {
	int oldRowIndex = getRowIndex(item);
	Vector oldRowItems = (Vector) rows.elementAt(oldRowIndex);
	if (oldRowIndex == 0 && oldRowItems.size() == 1) {
		/* This is the only item in the top row, don't move it. */
		return;
	}
	
	removeItemFromRow(item, oldRowItems);
	int newRowIndex = oldRowIndex - 1;
	int newRowY = ROW_SPACING;
	for (int i = 0; i < newRowIndex; i++) {
		newRowY += getRowHeight(i) + ROW_SPACING;	
	}
	
	if (oldRowIndex == 0) {
		/* Create a new top row for the item. */
		Vector newRow = new Vector(10);
		insertItemIntoRow(item, newRow, x_root, newRowY);
		rows.insertElementAt(newRow, 0);
		adjustItemHeights(0);
		return;
	}
	
	Vector newRowItems = (Vector) rows.elementAt(newRowIndex);
	insertItemIntoRow(item, newRowItems, x_root, newRowY);
	adjustItemHeights(newRowIndex);
}
void processPaint (Event e) {
	if (rows.size() == 0) return;
	GC gc = new GC(this);
	Display display = getDisplay();
	int y = getRowHeight(0) + ROW_SPACING;
	int stopX = getBounds().width;
	Color shadowColor = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	Color highlightColor = display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
	for (int i = 1; i < rows.size(); i++) {
		gc.setForeground(shadowColor);
		gc.drawLine(0, y, stopX, y);	
		gc.setForeground(highlightColor);
		gc.drawLine(0, y + 1, stopX, y + 1);
		y += getRowHeight(i) + ROW_SPACING;
	}
	gc.dispose();
}
/**
 * Remove the item from the row. Adjust the x and width values
 * appropriately.
 */
void removeItemFromRow(CoolItem item, Vector row) {
	int index = row.indexOf(item);
	row.removeElementAt(index);
	if (row.size() == 0) {
		rows.removeElement(row);
		return;
	}	
	if (index == 0) {
		CoolItem first = (CoolItem) row.elementAt(0);
		Rectangle bounds = first.getBounds();
		int width = bounds.x + bounds.width;
		first.setBounds(0, bounds.y, width, bounds.height);
		first.requestedWidth = width;
	} else {
		CoolItem previous = (CoolItem) row.elementAt(index - 1);
		Rectangle bounds = previous.getBounds();
		int width = bounds.width + item.getSize().x;
		previous.setBounds(bounds.x, bounds.y, width, bounds.height);
		previous.requestedWidth = width;
	}
}
/**
 * Update the children to reflect a change in y values.
 */
void adjustItemHeights(int startIndex) {
	int y = ROW_SPACING;
	for (int i = 0; i < rows.size(); i++) {
		Vector row = (Vector) rows.elementAt(i);
		int rowHeight = getRowHeight(i);
		if (i >= startIndex) {
			for (int j = 0; j < row.size(); j++) {
				CoolItem child = (CoolItem) row.elementAt(j);
				Rectangle bounds = child.getBounds();
				if (bounds.y != y || bounds.height != rowHeight) {
					child.setBounds(bounds.x, y, bounds.width, rowHeight);
				}
			}
		}
		y += ROW_SPACING + rowHeight;
	}
	Point size = getSize();
	if (size.y != y) super.setSize(size.x, y);
}
Point layout (int width, boolean resize) {
	int y = ROW_SPACING, maxWidth = 0;
	for (int i = 0; i < rows.size(); i++) {
		Vector row = (Vector) rows.elementAt(i);
		int count = row.size();
		if (count > 0) {
			int available = width - count * CoolItem.MINIMUM_WIDTH;
			if (available < 0) available = count * CoolItem.MINIMUM_WIDTH;
			int x = 0, rowHeight = getRowHeight(i);
			for (int j = 0; j < count; j++) {
				CoolItem child = (CoolItem) row.elementAt(j);
				int newWidth = available + CoolItem.MINIMUM_WIDTH;
				if (j + 1 < count) {
					newWidth = Math.min(newWidth, child.requestedWidth);
					available -= (newWidth - CoolItem.MINIMUM_WIDTH);
				}
				if (resize) child.setBounds(x, y, newWidth, rowHeight);
				x += resize ? newWidth : child.preferredWidth;
			}		
			maxWidth = Math.max(maxWidth, x);
			y += ROW_SPACING + rowHeight;
		}
	}
	return new Point(maxWidth, y);
}
void relayout() {
	int width = getSize().x;
	int height = layout(width, true).y;
	super.setSize(width, height);
}
public void setBackground (Color color) {
	super.setBackground (color);
	CoolItem[] items = getItems ();
	for (int i = 0; i < items.length; i++) {
		items[i].composite.setBackground (color);	
	}
}
public void setBounds (int x, int y, int width, int height) {
	super.setBounds (x, y, width, height);
	layout(width, true);
}
public void setSize (int width, int height) {
	super.setSize (width, height);
	layout (width, true);
}
CoolItem getChild (int id) {
	for (int i = 0; i < rows.size(); i++) {
		Vector row = (Vector) rows.elementAt(i);
		for (int j = 0; j < row.size(); j++) {
			CoolItem child = (CoolItem) row.elementAt(j);
			if (child.id == id) return child;
		}
	}
	return null;
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
void setItemOrder (int[] itemOrder) {
	if (itemOrder == null) error(SWT.ERROR_NULL_ARGUMENT);
	CoolItem[] items = getItems();
	if (itemOrder.length != items.length) error(SWT.ERROR_INVALID_ARGUMENT);
	Vector row = new Vector(items.length);
	for (int i = 0; i < itemOrder.length; i++) {
		CoolItem child = getChild(itemOrder[i]);
		if (child == null) error(SWT.ERROR_INVALID_ARGUMENT);
		row.addElement(child);
	}
	rows = new Vector(1);
	rows.addElement(row);
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
void setItemSizes (Point[] sizes) {
	if (sizes == null) error(SWT.ERROR_NULL_ARGUMENT);
	CoolItem[] items = getItems();
	if (sizes.length != items.length) error(SWT.ERROR_INVALID_ARGUMENT);
	for (int i = 0; i < items.length; i++) {
		Rectangle bounds = items[i].getBounds();
		items[i].setSize(sizes[i]);
	}
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
	relayout();
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
	setItemOrder(itemOrder);
	setWrapIndices(wrapIndices);
	setItemSizes(sizes);	
	relayout();
}
}
