/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


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
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class CoolBar extends Composite {
	CoolItem[][] items = new CoolItem[0][0];
	CoolItem[] originalItems = new CoolItem[0];
	Cursor hoverCursor, dragCursor;
	CoolItem dragging = null;
	int mouseXOffset, itemXOffset;
	static final int ROW_SPACING = 2;
	static final int CLICK_DISTANCE = 3;

	boolean isLocked = false;
	boolean inDispose = false;
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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public CoolBar (Composite parent, int style) {
	super (parent, checkStyle(style));
	hoverCursor = new Cursor(display, SWT.CURSOR_SIZEWE);
	dragCursor = new Cursor(display, SWT.CURSOR_SIZEALL);
	Listener listener = new Listener() {
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.Dispose:      		onDispose();        		break;
				case SWT.MouseDown:    		onMouseDown(event);			break;
				case SWT.MouseExit:    		onMouseExit();      		break;
				case SWT.MouseMove:    		onMouseMove(event); 		break;
				case SWT.MouseUp:      		onMouseUp(event);   		break;
				case SWT.MouseDoubleClick:	onMouseDoubleClick(event); 	break;
				case SWT.Paint:        		onPaint(event);     		break;
			}
		}
	};
	int[] events = new int[] { 
		SWT.Dispose, 
		SWT.MouseDown,
		SWT.MouseExit, 
		SWT.MouseMove, 
		SWT.MouseUp, 
		SWT.MouseDoubleClick,
		SWT.Paint 
	};
	for (int i = 0; i < events.length; i++) {
		addListener(events[i], listener);	
	}
}
private static int checkStyle (int style) {
	style |= SWT.NO_FOCUS;
	return (style | SWT.NO_REDRAW_RESIZE) & ~(SWT.V_SCROLL | SWT.H_SCROLL);
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = 0, height = 0;
	for (int row = 0; row < items.length; row++) {
		int rowWidth = 0, rowHeight = 0;
		for (int i = 0; i < items[row].length; i++) {
			rowWidth += items[row][i].preferredWidth;
			rowHeight = Math.max(rowHeight, items[row][i].getSize().y);
		}
		height += rowHeight;
		if (row > 0) height += ROW_SPACING;
		width = Math.max(width, rowWidth);
	}
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	Rectangle trim = computeTrim(0, 0, width, height);
	return new Point(trim.width, trim.height);
}
CoolItem getGrabbedItem(int x, int y) {
	for (int row = 0; row < items.length; row++) {
		for (int i = 0; i < items[row].length; i++) {
			CoolItem item = items[row][i];
			Rectangle bounds = item.getBounds();
			bounds.width = CoolItem.MINIMUM_WIDTH;
			if (bounds.x > x) break;
			if (bounds.y > y) return null;
			if (bounds.contains(x, y)) {
				return item;	
			}	
		}	
	}
	return null;
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public CoolItem getItem (int index) {
	checkWidget();
	if (index < 0) error (SWT.ERROR_INVALID_RANGE);
	for (int row = 0; row < items.length; row++) {
		if (items[row].length > index) {
			return items[row][index];			
		} else {
			index -= items[row].length;
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
	return originalItems.length;
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public CoolItem [] getItems () {
	checkWidget();
	CoolItem [] result = new CoolItem [getItemCount()];
	int offset = 0;
	for (int row = 0; row < items.length; row++) {
		System.arraycopy(items[row], 0, result, offset, items[row].length);
		offset += items[row].length;
	}
	return result;
}
Point findItem (CoolItem item) {
	for (int row = 0; row < items.length; row++) {
		for (int i = 0; i < items[row].length; i++) {
			if (items[row][i].equals(item)) return new Point(i, row);		
		}
	}
	return new Point(-1, -1);
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
	checkWidget();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	int answer = 0;
	for (int row = 0; row < items.length; row++) {
		for (int i = 0; i < items[row].length; i++) {
			if (items[row][i].equals(item)) {
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
void insertItemIntoRow(CoolItem item, int rowIndex, int x_root) {
	int barWidth = getSize().x;
	int rowY = items[rowIndex][0].getBounds().y;
	int x = Math.max(0, x_root - toDisplay(new Point(0, 0)).x);
	
	/* Find the insertion index and add the item. */
	int index;
	for (index = 0; index < items[rowIndex].length; index++) {
		if (x < items[rowIndex][index].getBounds().x) break;
	}
	int oldLength = items[rowIndex].length;
	CoolItem[] newRow = new CoolItem[oldLength + 1];
	System.arraycopy(items[rowIndex], 0, newRow, 0, index);
	newRow[index] = item;
	System.arraycopy(items[rowIndex], index, newRow, index + 1, oldLength - index);
	items[rowIndex] = newRow;

	/* Adjust the width of the item to the left. */
	if (index > 0) {
		CoolItem left = items[rowIndex][index - 1];
		Rectangle leftBounds = left.getBounds();
		int newWidth = x - leftBounds.x;
		if (newWidth < left.internalGetMinimumWidth()) {
			x += left.internalGetMinimumWidth() - newWidth;
			newWidth = left.internalGetMinimumWidth();
		}
		left.setBounds(leftBounds.x, leftBounds.y, newWidth, leftBounds.height);
		left.requestedWidth = newWidth;
	}
	
	/* Set the item's bounds. */
	int width = 0, height = item.getSize().y;
	if (index < items[rowIndex].length - 1) {
		CoolItem right = items[rowIndex][index + 1];
		width = right.getBounds().x - x;
		if (width < right.internalGetMinimumWidth()) {
			moveRight(right, right.internalGetMinimumWidth() - width);
			width = right.getBounds().x - x;
		}
		item.setBounds(x, rowY, width, height);
		if (width < item.internalGetMinimumWidth()) moveLeft(item, item.internalGetMinimumWidth() - width);
	} else {
		width = Math.max(item.internalGetMinimumWidth(), barWidth - x);
		item.setBounds(x, rowY, width, height);
		if (x + width > barWidth) moveLeft(item, x + width - barWidth); 
	}
	Rectangle bounds = item.getBounds();
	item.requestedWidth = bounds.width;
	redraw(bounds.x, bounds.y, item.internalGetMinimumWidth(), bounds.height, false);
}
void createItem (CoolItem item, int index) {
	int itemCount = getItemCount(), row = 0;
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	if (items.length == 0) {
		items = new CoolItem[1][1];
		items[0][0] = item;	
	}
	else {
		int i = index;
		/* find the row to insert into */
		if (index < itemCount) {
			while (i > items[row].length) {
				i -= items[row].length;
				row++;
			}   
		}
		else {
			row = items.length - 1;
			i = items[row].length;	
		}
		
		// Set the last item in the row to the preferred size 
		// and add the new one just to it's right
		int lastIndex = items[row].length - 1;
		CoolItem lastItem = items[row][lastIndex];
		if (lastItem.ideal) {
			Rectangle bounds = lastItem.getBounds();
			bounds.width = lastItem.preferredWidth;
			lastItem.requestedWidth = lastItem.preferredWidth;
			lastItem.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);  
		}
		int oldLength = items[row].length;
		CoolItem[] newRow = new CoolItem[oldLength + 1];
		System.arraycopy(items[row], 0, newRow, 0, i);
		newRow[i] = item;
		System.arraycopy(items[row], i, newRow, i + 1, oldLength - i);
		items[row] = newRow;
	}
	item.requestedWidth = CoolItem.MINIMUM_WIDTH;
	layoutItems();

	int length = originalItems.length;
	CoolItem [] newOriginals = new CoolItem [length + 1];
	System.arraycopy (originalItems, 0, newOriginals, 0, index);
	System.arraycopy (originalItems, index, newOriginals, index + 1, length - index);
	newOriginals [index] = item;
	originalItems = newOriginals;
}
void destroyItem(CoolItem item) {
	if (inDispose) return;
	int row = findItem(item).y;
	if (row == -1) return;
	Rectangle bounds = item.getBounds();
	removeItemFromRow(item, row, true);
	redraw(bounds.x, bounds.y, CoolItem.MINIMUM_WIDTH, bounds.height, false);
	relayout();

	int index = 0;
	while (index < originalItems.length) {
		if (originalItems [index] == item) break;
		index++;
	}
	int length = originalItems.length - 1;
	CoolItem [] newOriginals = new CoolItem [length];
	System.arraycopy (originalItems, 0, newOriginals, 0, index);
	System.arraycopy (originalItems, index + 1, newOriginals, index, length - index);
	originalItems = newOriginals;	
}
void moveDown(CoolItem item, int x_root) {
	int oldRowIndex = findItem(item).y;
	boolean resize = false;
	if (items[oldRowIndex].length == 1) {
		resize = true;
		/* If this is the only item in the bottom row, don't move it. */
		if (oldRowIndex == items.length - 1) return;
	}
	int newRowIndex = (items[oldRowIndex].length == 1) ? oldRowIndex : oldRowIndex + 1;
	removeItemFromRow(item, oldRowIndex, false);
	Rectangle old = item.getBounds();
	redraw(old.x, old.y, CoolItem.MINIMUM_WIDTH, old.height, false);
	if (newRowIndex == items.length) {
		/* Create a new bottom row for the item. */
		CoolItem[][] newRows = new CoolItem[items.length + 1][];
		System.arraycopy(items, 0, newRows, 0, items.length);
		int row = items.length;
		newRows[row] = new CoolItem[1];
		newRows[row][0] = item;
		items = newRows;
		resize = true;
	}
	else {	
		insertItemIntoRow(item, newRowIndex, x_root);
	}
	if (resize) relayout();
	else layoutItems();
}
void moveLeft(CoolItem item, int pixels) {
	Point point = findItem(item);
	int row = point.y;
	int index = point.x;
	if (index == 0) return;	
	Rectangle bounds = item.getBounds();
	int minSpaceOnLeft = 0;
	for (int i = 0; i < index; i++) {
		minSpaceOnLeft += items[row][i].internalGetMinimumWidth();
	}
	int x = Math.max(minSpaceOnLeft, bounds.x - pixels);
	CoolItem left = items[row][index - 1];
	Rectangle leftBounds = left.getBounds();
	if (leftBounds.x + left.internalGetMinimumWidth() > x) {
		int shift = leftBounds.x + left.internalGetMinimumWidth() - x;
		moveLeft(left, shift);
		leftBounds = left.getBounds();
	}
	int leftWidth = Math.max(left.internalGetMinimumWidth(), leftBounds.width - pixels);
	left.setBounds(leftBounds.x, leftBounds.y, leftWidth, leftBounds.height);
	left.requestedWidth = leftWidth;
	int width = bounds.width + (bounds.x - x);
	item.setBounds(x, bounds.y, width, bounds.height);
	item.requestedWidth = width;

	int damagedWidth = bounds.x - x + CoolItem.MINIMUM_WIDTH;
	if (damagedWidth > CoolItem.MINIMUM_WIDTH) {
		redraw(x, bounds.y, damagedWidth, bounds.height, false);
	}
}
void moveRight(CoolItem item, int pixels) {
	Point point = findItem(item);
	int row = point.y;
	int index = point.x;
	if (index == 0) return;	
	Rectangle bounds = item.getBounds();
	int minSpaceOnRight = 0;
	for (int i = index; i < items[row].length; i++) {
		minSpaceOnRight += items[row][i].internalGetMinimumWidth(); 
	}
	int max = getBounds().width - minSpaceOnRight;
	int x = Math.min(max, bounds.x + pixels);	
	int width = 0;
	if (index + 1 == items[row].length) {
		width = getBounds().width - x;
	} else {
		CoolItem right = items[row][index + 1];
		Rectangle rightBounds = right.getBounds();
		if (x + item.internalGetMinimumWidth() > rightBounds.x) {
			int shift = x + item.internalGetMinimumWidth() - rightBounds.x;
			moveRight(right, shift);
			rightBounds = right.getBounds();
		}
		width = rightBounds.x - x;
	}
	item.setBounds(x, bounds.y, width, bounds.height);
	item.requestedWidth = width;
	CoolItem left = items[row][index - 1];
	Rectangle leftBounds = left.getBounds();
	int leftWidth = x - leftBounds.x;
	left.setBounds(leftBounds.x, leftBounds.y, leftWidth, leftBounds.height);
	left.requestedWidth = leftWidth;
	
	int damagedWidth = x - bounds.x + CoolItem.MINIMUM_WIDTH + CoolItem.MARGIN_WIDTH;
	if (x - bounds.x > 0) {
		redraw(bounds.x - CoolItem.MARGIN_WIDTH, bounds.y, damagedWidth, bounds.height, false);
	}
}
void moveUp(CoolItem item, int x_root) {
	Point point = findItem(item);
	int oldRowIndex = point.y;
	boolean resize = false;
	if (items[oldRowIndex].length == 1) {
		resize = true;
		/* If this is the only item in the top row, don't move it. */
		if (oldRowIndex == 0) return;
	}
	removeItemFromRow(item, oldRowIndex, false);
	Rectangle old = item.getBounds();
	redraw(old.x, old.y, CoolItem.MINIMUM_WIDTH, old.height, false);
	int newRowIndex = Math.max(0, oldRowIndex - 1);
	if (oldRowIndex == 0) {
		/* Create a new top row for the item. */
		CoolItem[][] newRows = new CoolItem[items.length + 1][];
		System.arraycopy(items, 0, newRows, 1, items.length);
		newRows[0] = new CoolItem[1];
		newRows[0][0] = item;
		items = newRows;
		resize = true;
	}
	else {
		insertItemIntoRow(item, newRowIndex, x_root);
	}
	if (resize) relayout();
	else layoutItems();
}
void onDispose() {
	/*
	 * Usually when an item is disposed, destroyItem will change the size of the items array
	 * and reset the bounds of all the remaining cool items.
	 * Since the whole cool bar is being disposed, this is not necessary.  For speed
	 * the inDispose flag is used to skip over this part of the item dispose.
	 */
	inDispose = true;
	for (int i = 0; i < items.length; i++) {
		for (int j = 0; j < items[i].length; j++) {
			items[i][j].dispose();
		}
	}
	hoverCursor.dispose();
	dragCursor.dispose();
}
void onMouseDown(Event event) {
	if (isLocked || event.button != 1) return;	
	dragging = getGrabbedItem(event.x, event.y);
	if (dragging != null) {
		mouseXOffset = event.x;
		itemXOffset = mouseXOffset - dragging.getBounds().x;
		setCursor(dragCursor);
	}
}
void onMouseExit() {
	if (dragging == null) setCursor(null);
}
void onMouseMove(Event event) {
	if (isLocked) return;
	CoolItem grabbed = getGrabbedItem(event.x, event.y);
	if (dragging != null) {
		int left_root = toDisplay(new Point(event.x, event.y)).x - itemXOffset;
		Rectangle bounds = dragging.getBounds();
		if (event.y < bounds.y) {
			moveUp(dragging, left_root);
		} 
		else if (event.y > bounds.y + bounds.height){
			moveDown(dragging, left_root);
		}		
		else if (event.x < mouseXOffset) {
			int distance = Math.min(mouseXOffset, bounds.x + itemXOffset) - event.x;
			if (distance > 0) moveLeft(dragging, distance);
		}
		else if (event.x > mouseXOffset) {
			int distance = event.x - Math.max(mouseXOffset, bounds.x + itemXOffset);
			if (distance > 0) moveRight(dragging, distance);
		}
		mouseXOffset = event.x;
		return;
	}
	if (grabbed != null) {
		setCursor(hoverCursor);
	}
	else {
		setCursor(null);	
	}
}
void onMouseUp(Event event) {
	setCursor(null);
	dragging = null;
}
void onMouseDoubleClick(Event event) {
	if (isLocked) return;	
	dragging = null;
	CoolItem target = getGrabbedItem(event.x, event.y);
	if (target == null) {
		setCursor(null);
		return;	
	}

	Point location = findItem(target);
	int row = location.y;
	int index = location.x;
	if (items[row].length > 1) {
		Point size = target.getSize();
		int maxSize = getSize().x;
		for (int i = 0; i < items[row].length; i++) {
			if (i != index) {
				maxSize -= items[row][i].internalGetMinimumWidth();	
			}
		}
		if (size.x == maxSize) {
			/* The item is at its maximum width. It should be resized to its minimum width. */
			int distance = size.x - target.internalGetMinimumWidth();
			if (index + 1 < items[row].length) {
				/* There is an item to the right. Maximize it. */
				CoolItem right = items[row][index + 1];
				moveLeft(right, distance);
			}
			else {
				/* There is no item to the right. Move the item all the way right. */
				moveRight(target, distance);
			}
		}
		else if (size.x < target.preferredWidth) {
			/* The item is less than its preferredWidth. Resize to preferredWidth. */
			int distance = target.preferredWidth - size.x;
			if (index + 1 < items[row].length) {
				CoolItem right = items[row][index + 1];
				moveRight(right, distance);	
				distance = target.preferredWidth - target.getSize().x;
			}
			if (distance > 0) {
				moveLeft(target, distance);
			}
		}
		else {
			/* The item is at its minimum width. Maximize it. */
			for (int i = 0; i < items[row].length; i++) {
				if (i != index) {
					CoolItem item = items[row][i];
					item.requestedWidth = Math.max(item.internalGetMinimumWidth(), CoolItem.MINIMUM_WIDTH); 
				}
			}
			target.requestedWidth = maxSize;
			layoutItems();
		}
		setCursor(hoverCursor);
	}
}
void onPaint(Event event) {
	GC gc = event.gc;
	if (items.length == 0) return;
	Color shadowColor = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	Color highlightColor = display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);

	int stopX = getBounds().width;
	for (int row = 0; row < items.length; row++) {
		Rectangle bounds = new Rectangle(0, 0, 0, 0);
		for (int i = 0; i < items[row].length; i++) {
			bounds = items[row][i].getBounds();
			if (!gc.getClipping().intersects(bounds)) continue;

			/* Draw separator. */
			if (i != 0) {
				gc.setForeground(shadowColor);
				gc.drawLine(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height - 1);
				gc.setForeground(highlightColor);
				gc.drawLine(bounds.x + 1, bounds.y, bounds.x + 1, bounds.y + bounds.height - 1);
			}

			/* Draw grabber. */
			if (!isLocked) {
				int grabberTrim = 2; 
				int grabberHeight = bounds.height - (2 * CoolItem.MARGIN_HEIGHT) - (2 * grabberTrim) - 1;				
				gc.setForeground(shadowColor);
				gc.drawRectangle(
					bounds.x + CoolItem.MARGIN_WIDTH, 
					bounds.y + CoolItem.MARGIN_HEIGHT + grabberTrim, 
					2, 
					grabberHeight);
				gc.setForeground(highlightColor);
				gc.drawLine(
					bounds.x + CoolItem.MARGIN_WIDTH, 
					bounds.y + CoolItem.MARGIN_HEIGHT + grabberTrim + 1, 
					bounds.x + CoolItem.MARGIN_WIDTH, 
					bounds.y + CoolItem.MARGIN_HEIGHT + grabberTrim + grabberHeight - 1);
				gc.drawLine(
					bounds.x + CoolItem.MARGIN_WIDTH, 
					bounds.y + CoolItem.MARGIN_HEIGHT + grabberTrim, 
					bounds.x + CoolItem.MARGIN_WIDTH + 1, 
					bounds.y + CoolItem.MARGIN_HEIGHT + grabberTrim);
			}	
		}
		if (row + 1 < items.length) {
			/* Draw row separator. */
			int separatorY = bounds.y + bounds.height;
			gc.setForeground(shadowColor);
			gc.drawLine(0, separatorY, stopX, separatorY);	
			gc.setForeground(highlightColor);
			gc.drawLine(0, separatorY + 1, stopX, separatorY + 1);			
		}
	}
}
/**
 * Remove the item from the row. Adjust the x and width values
 * appropriately.
 */
void removeItemFromRow(CoolItem item, int rowIndex, boolean disposed) {
	int index = findItem(item).x;
	int newLength = items[rowIndex].length - 1;
	Rectangle itemBounds = item.getBounds();
	if (newLength > 0) {
		CoolItem[] newRow = new CoolItem[newLength];
		System.arraycopy(items[rowIndex], 0, newRow, 0, index);
		System.arraycopy(items[rowIndex], index + 1, newRow, index, newRow.length - index);
		items[rowIndex] = newRow;
	}
	else {
		CoolItem[][] newRows = new CoolItem[items.length - 1][];
		System.arraycopy(items, 0, newRows, 0, rowIndex);
		System.arraycopy(items, rowIndex + 1, newRows, rowIndex, newRows.length - rowIndex);
		items = newRows;
		return;
	}
	if (!disposed) {
		if (index == 0) {
			CoolItem first = items[rowIndex][0];
			Rectangle bounds = first.getBounds();
			int width = bounds.x + bounds.width;
			first.setBounds(0, bounds.y, width, bounds.height);
			first.requestedWidth = width;
			redraw(bounds.x, bounds.y, CoolItem.MINIMUM_WIDTH, bounds.height, false);
		} else {
			CoolItem previous = items[rowIndex][index - 1];
			Rectangle bounds = previous.getBounds();
			int width = bounds.width + itemBounds.width;
			previous.setBounds(bounds.x, bounds.y, width, bounds.height);
			previous.requestedWidth = width;
		}
	}
}
/**
 * Return the height of the bar after it has
 * been properly layed out for the given width.
 */
int layoutItems () {
	int y = 0, maxWidth = 0, width = getSize().x;
	for (int row = 0; row < items.length; row++) {
		int count = items[row].length;
		int x = 0;

		/* determine the height and the available width for the row */
		int rowHeight = 0;
		int available = width;
		for (int i = 0; i < items[row].length; i++) {
			CoolItem item = items[row][i];
			if (item.control != null) {
				rowHeight = Math.max(rowHeight, item.control.getSize().y);
			}
			available -= item.internalGetMinimumWidth();	
			if (available < 0 && (width > item.internalGetMinimumWidth())) {
				/* push the next items into the next row */
				available += item.internalGetMinimumWidth();
				int amount = items[row].length - i;	
				CoolItem[] nextRow;
				if (row + 1 == items.length) { /* Adding a new row */
					nextRow = new CoolItem [amount];
					CoolItem[][] newItems = new CoolItem [items.length + 1][];
					System.arraycopy(items, 0, newItems, 0, items.length);
					items = newItems;
				} else {
					nextRow = new CoolItem [amount + items [row + 1].length];
					System.arraycopy(items[row+1], 0, nextRow, amount, items [row + 1].length);
				}
				System.arraycopy(items[row], i, nextRow, 0, amount);
				items [row + 1] = nextRow;
				CoolItem[] thisRow = new CoolItem[i];
				System.arraycopy(items[row], 0, thisRow, 0, i);
				items[row] = thisRow;
				break;			
			}
		}
		rowHeight += 2 * CoolItem.MARGIN_HEIGHT;
		if (row > 0) y += ROW_SPACING;
	
	
		/* lay the items out */
		for (int i = 0; i < items[row].length; i++) {
			CoolItem child = items[row][i];
			int newWidth = available + child.internalGetMinimumWidth();
			if (i + 1 < count) {
				newWidth = Math.min(newWidth, child.requestedWidth);
				available -= (newWidth - child.internalGetMinimumWidth());
			}
			Rectangle oldBounds = child.getBounds();
			Rectangle newBounds = new Rectangle(x, y, newWidth, rowHeight);
			if (!oldBounds.equals(newBounds)) {
				child.setBounds(newBounds.x, newBounds.y, newBounds.width, newBounds.height);
				Rectangle damage = new Rectangle(0, 0, 0, 0);
				/* Cases are in descending order from most area to redraw to least. */
				if (oldBounds.y != newBounds.y) {
					damage = newBounds;
					damage.add(oldBounds);
					/* Redraw the row separator as well. */
					damage.y -= ROW_SPACING;
					damage.height += 2 * ROW_SPACING;
				}
				else if (oldBounds.height != newBounds.height) {
					/* 
					 * Draw from the bottom of the gripper to the bottom of the new area.
					 * (Bottom of the gripper is -3 from the bottom of the item).
					 */
					damage.y = newBounds.y + Math.min(oldBounds.height, newBounds.height) - 3;
					damage.height = newBounds.y + newBounds.height + ROW_SPACING;
					damage.x = oldBounds.x - CoolItem.MARGIN_WIDTH;
					damage.width = oldBounds.width + CoolItem.MARGIN_WIDTH;
				}
				else if (oldBounds.x != newBounds.x) {
					/* Redraw only the difference between the separators. */
					damage.x = Math.min(oldBounds.x, newBounds.x);
					damage.width = Math.abs(oldBounds.x - newBounds.x) + CoolItem.MINIMUM_WIDTH;
					damage.y = oldBounds.y;
					damage.height = oldBounds.height;
				}
				redraw(damage.x, damage.y, damage.width, damage.height, false);
			}
			x += newWidth;
		}
		maxWidth = Math.max(maxWidth, x);
		y += rowHeight;
	}
	return y;
}
void relayout() {
	Point size = getSize();
	int height = layoutItems();
	Rectangle trim = computeTrim (0, 0, 0, height);
	if (height != size.y) super.setSize(size.x, trim.height);
}
public void setBounds (int x, int y, int width, int height) {
	super.setBounds (x, y, width, height);
	layoutItems();
}
public void setSize (int width, int height) {
	super.setSize (width, height);
	layoutItems();
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int[] getItemOrder () {
	checkWidget ();
	int count = getItemCount ();
	int [] indices = new int [count];
	count = 0;
	for (int i = 0; i < items.length; i++) {
		for (int j = 0; j < items[i].length; j++) {
			CoolItem item = items[i][j];
			int index = 0;
			while (index<originalItems.length) {
				if (originalItems [index] == item) break;
				index++;	
			}
			if (index == originalItems.length) error (SWT.ERROR_CANNOT_GET_ITEM);
			indices [count++] = index;
		}
	}
	return indices;
}
void setItemOrder (int[] itemOrder) {
	if (itemOrder == null) error(SWT.ERROR_NULL_ARGUMENT);
	int count = originalItems.length;
	if (itemOrder.length != count) error(SWT.ERROR_INVALID_ARGUMENT);

	/* Ensure that itemOrder does not contain any duplicates. */	
	boolean [] set = new boolean [count];
	for (int i = 0; i < set.length; i++) set [i] = false;
	for (int i = 0; i < itemOrder.length; i++) {
		if (itemOrder [i] < 0 || itemOrder [i] >= count) error (SWT.ERROR_INVALID_ARGUMENT);
		if (set [itemOrder [i]]) error (SWT.ERROR_INVALID_ARGUMENT);
		set [itemOrder [i]] = true;
	}
	
	CoolItem[] row = new CoolItem[count];
	for (int i = 0; i < count; i++) {
		row[i] = originalItems[itemOrder[i]];
	}
	items = new CoolItem[1][count];
	items[0] = row;
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
		items[i].setSize(sizes[i]);
	}
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
	return isLocked;
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
public int[] getWrapIndices () {
	checkWidget();
	if (items.length <= 1) return new int[]{};
	int[] data = new int[items.length - 1];
	int i = 0, nextWrap = items[0].length;
	for (int row = 1; row < items.length; row++) {
		data[i++] = nextWrap;
		nextWrap += items[row].length;
	}
	return data;
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
	if (isLocked != locked) {
		redraw();
	}
	isLocked = locked;
	
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
public void setWrapIndices (int[] data) {
	checkWidget();
	if (data == null) data = new int[0];
	for (int i=0; i<data.length; i++) {
		if (data[i] < 0 || data[i] >= originalItems.length) error (SWT.ERROR_INVALID_ARGUMENT);	
	}	
	if (items.length == 0) return;

	CoolItem[] allItems = getItems();
	items = new CoolItem[0][];
	CoolItem[][] newItems;
	CoolItem[] row;
	int itemIndex = 0;
	for (int i = 0; i <= data.length; i++) {
		int nextWrap = (i < data.length) ? data[i] : allItems.length;
		if (nextWrap != 0) {
			row = new CoolItem[nextWrap - itemIndex];
			System.arraycopy(allItems, itemIndex, row, 0, row.length); 
			itemIndex += row.length;
			newItems = new CoolItem[items.length + 1][];
			System.arraycopy(items, 0, newItems, 0, items.length);
			newItems[items.length] = row;
			items = newItems;
		}
	}
	relayout();
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
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
