package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */

/* Imports */
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import java.util.*;

public /*final*/ class CoolBar extends Composite {
	Vector rows;
	Cursor hoverCursor;
	Cursor dragCursor;
	static final int ROW_SPACING = 2;
		
public CoolBar (Composite parent, int style) {
	super (parent, style);
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int width = wHint, height = hHint;
	if (wHint == SWT.DEFAULT) width = 0x7FFFFFFF;
	if (hHint == SWT.DEFAULT) height = 0x7FFFFFFF;
	Point extent = layout (width, false);
	if (wHint != SWT.DEFAULT) extent.x = wHint;
	if (hHint != SWT.DEFAULT) extent.y = hHint;
	return extent;
}
public CoolItem getItem (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
public int getItemCount () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int itemCount = 0;
	for (int i = 0; i < rows.size(); i++) {
		Vector row = (Vector) rows.elementAt(i);
		itemCount += row.size();
	}
	return itemCount;
}
public CoolItem [] getItems () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
		int itemHeight = item.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
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
void hookEvents () {
	int windowProc = getDisplay ().windowProc;
	OS.XtAddEventHandler (handle, OS.ExposureMask, false, windowProc, SWT.Paint);
}
public int indexOf (CoolItem item) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
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
	if (row.size() == 0) {
		Point size = item.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		item.setBounds(0, rowY, getSize().x, size.y);		
		row.addElement(item);
		return;
	}
	
	int x = x_root - toDisplay(new Point(0, 0)).x;
	
	/* Find the insertion index and add the item. */
	int index = 0;
	for (int i = 0; i < row.size(); i++) {
		CoolItem next = (CoolItem) row.elementAt(i);
		if (x < next.getBounds().x) break;	
		index++;
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
	}
	
	/* Set the item's bounds. */
	int width = 0;
	if (index < row.size() - 1) {
		CoolItem right = (CoolItem) row.elementAt(index + 1);
		width = right.getBounds().x - x;
		if (width < CoolItem.MINIMUM_WIDTH) {
			moveRight(right, CoolItem.MINIMUM_WIDTH - width);
			width = CoolItem.MINIMUM_WIDTH;
		}
	} else {
		width = getBounds().width - x;
	}
	item.setBounds(x, rowY, width, item.getBounds().height);
}
void createItem (CoolItem item, int index) {
	int itemCount = getItemCount();
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	item.createWidget (index);
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
void createWidget (int index) {
	rows = new Vector(5);
	super.createWidget (index);
	rows.addElement(new Vector(10));
	hoverCursor = new Cursor(getDisplay(), SWT.CURSOR_SIZEWE);
	dragCursor = new Cursor(getDisplay(), SWT.CURSOR_SIZEALL);	
}
void releaseWidget () {
	for (int i = 0; i < rows.size(); i++) {
		Vector row = (Vector) rows.elementAt(i);
		for (int j = 0; j < row.size(); j++) {
			CoolItem item = (CoolItem) row.elementAt(j);
			if (!item.isDisposed ()) {
				item.releaseWidget ();
				item.releaseHandle ();
			}
		}
	}
	rows = null;
	if (hoverCursor != null) hoverCursor.dispose();	
	if (dragCursor != null) dragCursor.dispose();
	super.releaseWidget ();
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
		rows.add(newRow);
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
	int width = bounds.width + (bounds.x - x);
	item.setBounds(x, bounds.y, width, bounds.height); 
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
	CoolItem left = (CoolItem) row.elementAt(index - 1);
	Rectangle leftBounds = left.getBounds();
	int leftWidth = x - leftBounds.x;
	left.setBounds(leftBounds.x, leftBounds.y, leftWidth, leftBounds.height);	
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
		rows.add(0, newRow);
		adjustItemHeights(0);
		return;
	}
	
	Vector newRowItems = (Vector) rows.elementAt(newRowIndex);
	insertItemIntoRow(item, newRowItems, x_root, newRowY);
	adjustItemHeights(newRowIndex);
}
int processPaint (int callData) {
	if (rows.size() == 0) return 0;
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
	return 0;
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
	} else {
		CoolItem previous = (CoolItem) row.elementAt(index - 1);
		Rectangle bounds = previous.getBounds();
		int width = bounds.width + item.getSize().x;
		previous.setBounds(bounds.x, bounds.y, width, bounds.height);
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
					if (bounds.height != rowHeight) child.redraw();
				}
			}
		}
		y += ROW_SPACING + rowHeight;
	}
	Point size = getSize();
	if (size.y != y) super.setSize(size.x, y);
}
/**
 * The row has been assigned a new width. Grow or
 * shrink the row's items as necessary.
 */
void adjustItemWidths (Vector row, int width) {
	CoolItem last = (CoolItem) row.lastElement();
	Rectangle bounds = last.getBounds();
	int rowWidth = bounds.x + bounds.width;
	if (width == rowWidth) return;
	if (width > rowWidth || width > bounds.x + CoolItem.MINIMUM_WIDTH) {
		last.setBounds(bounds.x, bounds.y, width - bounds.x, bounds.height);
		return;
	}
	/* Shifting the last item ensures that all hidden items
	   to its left are made visible as well.*/
	last.setBounds(bounds.x, bounds.y, CoolItem.MINIMUM_WIDTH, bounds.height);
	moveLeft(last, bounds.x - width + CoolItem.MINIMUM_WIDTH); 
}
Point layout (int width, boolean resize) {
	int y = ROW_SPACING, maxWidth = 0;
	for (int i = 0; i < rows.size(); i++) {
		Vector row = (Vector) rows.elementAt(i);
		int x = 0, rowHeight = getRowHeight(i);
		for (int j = 0; j < row.size(); j++) {
			CoolItem child = (CoolItem) row.elementAt(j);
			int childWidth = child.getSize().x;
			if (resize) child.setBounds(x, y, childWidth, rowHeight);
			x += childWidth;
		}		
		maxWidth = Math.max(maxWidth, x);
		if (resize) adjustItemWidths(row, width);
		y += ROW_SPACING + rowHeight;	
	}
	return new Point(maxWidth, y);
}
void realizeChildren () {
	super.realizeChildren ();
	CoolItem[] items = getItems();
	for (int i = 0; i < items.length; i++) {
		int window = OS.XtWindow (items[i].handle);
		if (window != 0) {
			int display = OS.XtDisplay (items[i].handle);
			if (display != 0) OS.XLowerWindow (display, window);
		}
	}
	layout(getSize().x, true);
}
void relayout() {
	Point size = layout(getSize().x, true);
	super.setSize(size.x, size.y);
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
public int[] getItemOrder () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
public Point[] getItemSizes () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
		items[i].setBounds(bounds.x, bounds.y, sizes[i].x, sizes[i].y);
	}
}
public int[] getWrapIndices () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int[] data = new int[rows.size() - 1];
	int itemIndex = 0;
	for (int i = 0; i < rows.size() - 1; i++) {
		Vector row = (Vector) rows.elementAt(i);
		itemIndex += row.size();
		data[i] = itemIndex;
	}
	return data;
}
public void setWrapIndices (int[] data) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
public void setItemLayout (int[] itemOrder, int[] wrapIndices, Point[] sizes) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	setItemOrder(itemOrder);
	setWrapIndices(wrapIndices);
	setItemSizes(sizes);	
	relayout();
}
}
