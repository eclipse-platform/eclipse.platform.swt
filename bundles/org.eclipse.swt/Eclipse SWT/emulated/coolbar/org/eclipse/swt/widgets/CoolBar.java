package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

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
	Cursor hoverCursor, dragCursor;
	CoolItem dragging = null;
	int mouseXOffset, itemXOffset;
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
	Display display = getDisplay();
	hoverCursor = new Cursor(display, SWT.CURSOR_SIZEWE);
	dragCursor = new Cursor(display, SWT.CURSOR_SIZEALL);
	Listener listener = new Listener() {
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.Dispose:		onDispose();		break;
				case SWT.MouseDown:	onMouseDown(event);	break;
				case SWT.MouseExit:	onMouseExit();		break;
				case SWT.MouseMove:	onMouseMove(event);	break;
				case SWT.MouseUp:		onMouseUp(event);	break;
				case SWT.Paint:		onPaint(event);		break;
			}
		}
	};
	int[] events = new int[] { 
		SWT.Dispose, 
		SWT.MouseDown,
		SWT.MouseExit, 
		SWT.MouseMove, 
		SWT.MouseUp, 
		SWT.Paint 
	};
	for (int i = 0; i < events.length; i++) {
		addListener(events[i], listener);	
	}
}
private static int checkStyle (int style) {
	return (style | SWT.NO_REDRAW_RESIZE);
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
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
	int border = getBorderWidth();
	width += 2 * border;
	height += 2 * border;
	return new Point(width, height);
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
	int itemCount = 0;
	for (int row = 0; row < items.length; row++) {
		itemCount += items[row].length;
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
		if (newWidth < CoolItem.MINIMUM_WIDTH) {
			x += CoolItem.MINIMUM_WIDTH - newWidth;
			newWidth = CoolItem.MINIMUM_WIDTH;
		}
		left.setBounds(leftBounds.x, leftBounds.y, newWidth, leftBounds.height);
		left.requestedWidth = newWidth;
	}
	
	/* Set the item's bounds. */
	int width = 0, height = item.getSize().y;
	if (index < items[rowIndex].length - 1) {
		CoolItem right = items[rowIndex][index + 1];
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
	Rectangle bounds = item.getBounds();
	item.requestedWidth = bounds.width;
	redraw(bounds.x, bounds.y, CoolItem.MINIMUM_WIDTH, bounds.height, false);
}
void createItem (CoolItem item, int index) {
	int itemCount = getItemCount(), row = 0;
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	item.id = itemCount;
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
		int oldLength = items[row].length;
		CoolItem[] newRow = new CoolItem[oldLength + 1];
		System.arraycopy(items[row], 0, newRow, 0, i);
		newRow[index] = item;
		System.arraycopy(items[row], i, newRow, i + 1, oldLength - i);
		items[row] = newRow;
	}
	item.requestedWidth = CoolItem.MINIMUM_WIDTH;
	relayout();
}
void destroyItem(CoolItem item) {
	int row = findItem(item).y;
	if (row == -1) return;
	Rectangle bounds = item.getBounds();
	removeItemFromRow(item, row);
	redraw(bounds.x, bounds.y, CoolItem.MINIMUM_WIDTH, bounds.height, false);
	
	Control control = item.getControl();
	if (control != null && !control.isDisposed()) {
		control.setVisible(false);
	}
	
	/* Fix the id of each remaining item. */
	for (row = 0; row < items.length; row++) {
		for (int i = 0; i < items[row].length; i++) {
			CoolItem next = items[row][i];
			if (next.id > item.id) --next.id; 	
		}	
	}
	relayout();	
}
void moveDown(CoolItem item, int x_root) {
	int oldRowIndex = findItem(item).y;
	if (oldRowIndex == items.length - 1 && items[oldRowIndex].length == 1) {
		/* This is the only item in the bottom row, don't move it. */
		return;
	}
	int newRowIndex = (items[oldRowIndex].length == 1) ? oldRowIndex : oldRowIndex + 1;
	removeItemFromRow(item, oldRowIndex);
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
	}
	else {	
		insertItemIntoRow(item, newRowIndex, x_root);
	}
	relayout();
}
void moveLeft(CoolItem item, int pixels) {
	Point point = findItem(item);
	int row = point.y;
	int index = point.x;
	if (index == 0) return;	
	Rectangle bounds = item.getBounds();
	int min = index * CoolItem.MINIMUM_WIDTH;
	int x = Math.max(min, bounds.x - pixels);
	CoolItem left = items[row][index - 1];
	Rectangle leftBounds = left.getBounds();
	if (leftBounds.x + CoolItem.MINIMUM_WIDTH > x) {
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
	int minSpaceOnRight = (items[row].length - index) * CoolItem.MINIMUM_WIDTH;
	int max = getBounds().width - minSpaceOnRight;
	int x = Math.min(max, bounds.x + pixels);	
	int width = 0;
	if (index + 1 == items[row].length) {
		width = getBounds().width - x;
	} else {
		CoolItem right = items[row][index + 1];
		Rectangle rightBounds = right.getBounds();
		if (x + CoolItem.MINIMUM_WIDTH > rightBounds.x) {
			int shift = x + CoolItem.MINIMUM_WIDTH - rightBounds.x;
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
	if (oldRowIndex == 0 && items[oldRowIndex].length == 1) {
		/* This is the only item in the top row, don't move it. */
		return;
	}
	removeItemFromRow(item, oldRowIndex);
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
	}
	else {
		insertItemIntoRow(item, newRowIndex, x_root);
	}
	relayout();
}
void onDispose() {
	hoverCursor.dispose();
	dragCursor.dispose();
}
void onMouseDown(Event event) {
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
	dragging = null;
	if (getGrabbedItem(event.x, event.y) != null) {
		setCursor(hoverCursor);
	}
	else {
		setCursor(null);	
	}	
}
void onPaint(Event event) {
	GC gc = event.gc;
	if (items.length == 0) return;
	Display display = getDisplay();
	Color shadowColor = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	Color highlightColor = display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
	Color lightShadowColor = display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);

	int y = 0;
	int stopX = getBounds().width;
	for (int row = 0; row < items.length; row++) {
		Rectangle bounds = new Rectangle(0, 0, 0, 0);
		for (int i = 0; i < items[row].length; i++) {
			bounds = items[row][i].getBounds();
			if (!gc.getClipping().intersects(bounds)) continue;
			int grabberHeight = bounds.height - (2 * CoolItem.MARGIN_HEIGHT) - 1;
	
			/* Draw separator. */
			gc.setForeground(shadowColor);
			gc.drawLine(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height - 1);
			gc.setForeground(highlightColor);
			gc.drawLine(bounds.x + 1, bounds.y, bounds.x + 1, bounds.y + bounds.height - 1);
		
			/* Draw grabber. */
			gc.setForeground(shadowColor);
			gc.drawRectangle(
				bounds.x + CoolItem.MARGIN_WIDTH, 
				bounds.y + CoolItem.MARGIN_HEIGHT, 
				2, 
				grabberHeight);
			gc.setForeground(highlightColor);
			gc.drawLine(
				bounds.x + CoolItem.MARGIN_WIDTH, 
				bounds.y + CoolItem.MARGIN_HEIGHT + 1, 
				bounds.x + CoolItem.MARGIN_WIDTH, 
				bounds.y + CoolItem.MARGIN_HEIGHT + grabberHeight - 1);
			gc.drawLine(
				bounds.x + CoolItem.MARGIN_WIDTH, 
				bounds.y + CoolItem.MARGIN_HEIGHT, 
				bounds.x + CoolItem.MARGIN_WIDTH + 1, 
				bounds.y + CoolItem.MARGIN_HEIGHT);
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
	gc.setForeground(getForeground());
	gc.setBackground(getBackground());	
}
/**
 * Remove the item from the row. Adjust the x and width values
 * appropriately.
 */
void removeItemFromRow(CoolItem item, int rowIndex) {
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
/**
 * Return the height of the bar after it has
 * been properly layed out for the given width.
 */
int layout (int width) {
	int y = 0, maxWidth = 0;
	for (int row = 0; row < items.length; row++) {
		int count = items[row].length;
		int available = width - count * CoolItem.MINIMUM_WIDTH;
		if (available < 0) available = count * CoolItem.MINIMUM_WIDTH;
		int x = 0;

		/* determine the height of the row */
		int rowHeight = 0;
		for (int i = 0; i < items[row].length; i++) {
			CoolItem item = items[row][i];
			if (item.control != null) {
				rowHeight = Math.max(rowHeight, item.control.getSize().y);
			}
		}
		rowHeight += 2 * CoolItem.MARGIN_HEIGHT;
		if (row > 0) y += ROW_SPACING;
	
		/* lay the items out */
		for (int i = 0; i < count; i++) {
			CoolItem child = items[row][i];
			int newWidth = available + CoolItem.MINIMUM_WIDTH;
			if (i + 1 < count) {
				newWidth = Math.min(newWidth, child.requestedWidth);
				available -= (newWidth - CoolItem.MINIMUM_WIDTH);
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
					damage.x = oldBounds.x;
					damage.width = oldBounds.width;
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
	int height = layout(size.x);
	height += 2 * getBorderWidth();
	if (height != size.y) super.setSize(size.x, height);
}
public void setBounds (int x, int y, int width, int height) {
	super.setBounds (x, y, width, height);
	layout(width);
}
public void setSize (int width, int height) {
	super.setSize (width, height);
	layout (width);
}
CoolItem getChild (int id) {
	for (int row = 0; row < items.length; row++) {
		for (int i = 0; i < items[row].length; i++) {
			CoolItem child = items[row][i];
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
	int count = getItemCount();
	if (itemOrder.length != count) error(SWT.ERROR_INVALID_ARGUMENT);
	CoolItem[] row = new CoolItem[count];
	for (int i = 0; i < count; i++) {
		CoolItem child = getChild(itemOrder[i]);
		if (child == null) error(SWT.ERROR_INVALID_ARGUMENT);
		row[i] = child;
	}
	items = new CoolItem[1][count];
	items[0] = row;
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
	if (items.length == 0) return;

	CoolItem[] allItems = getItems();
	items = new CoolItem[0][];
	CoolItem[][] newItems;
	CoolItem[] row;
	int itemIndex = 0;
	for (int i = 0; i <= data.length; i++) {
		int nextWrap = (i < data.length) ? data[i] : allItems.length;
		row = new CoolItem[nextWrap - itemIndex];
		System.arraycopy(allItems, itemIndex, row, 0, row.length); 
		itemIndex += row.length;
		newItems = new CoolItem[items.length + 1][];
		System.arraycopy(items, 0, newItems, 0, items.length);
		newItems[items.length] = row;
		items = newItems;
	}
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
