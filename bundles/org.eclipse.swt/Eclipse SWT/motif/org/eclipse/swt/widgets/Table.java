package org.eclipse.swt.widgets;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.*;
import java.util.*;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
/**
 * This class display rows of items arranged in one or more 
 * columns. Items can be selected, columns can be resized.
 */
public /*final*/ class Table extends SelectableItemWidget {
	private static final int COLUMN_RESIZE_OFFSET = 7;	// offset from the start and end of each 
														// column at which the resize cursor is displayed 
														// if the mouse is in the column header
	static final String DOT_STRING = "...";				// used to indicate truncated item labels

	private Header tableHeader;
	private Control focusProxy;							// used as a proxy to take focus in place of the table. 
														// The latter can't get focus because it has a child 
														// (the header widget). The header widget can't be used 
														// as a focus widget because it may be hidden.
	private Vector items;
	private Vector columns;
	private boolean drawGridLines = false;
	private boolean hasColumnFocus = false;				// true if a column currently has focus
	private boolean firstColumnImage = false;			// true if any item in the first column has an image
	private int columnResizeX;							// last position of the cursor in a column resize operation
	private Cursor columnResizeCursor;					// cursor displayed when a column resize is in progress. 
														// Need to keep reference to the cursor in order to dispose it.
	private boolean isColumnResizeCursor = false;		// set to true if the column resize cursor is active														
	private TableColumn resizeColumn;					// column that is currently being resized
	private TableColumn fillColumn;						// column used to fill up space that is not used 
														// by user defined columns
	private TableColumn defaultColumn;					// Default column that is created as soon as the table is created.
														// Fix for 1FUSJY5
	private int dotsWidth = -1;							// width of the static String dots (see above)
/**
 * Create a new instance of Table.
 * @param parent - the parent window of the new instance
 * @param style - window style for the new instance
 */
public Table(Composite parent, int style) {
	// use NO_MERGE_PAINTS to avoid flashing during column and widget resize redraw
	super(parent, checkStyle(style| SWT.NO_MERGE_PAINTS));
}
/**
 * Add 'column' to the receiver.
 * @param column - new table column that should be added to 
 *	the receiver
 */
void addColumn(TableColumn column) {
	int index = column.getIndex();
	
	getColumnVector().insertElementAt(column, index);
	// has the column been inserted (vs. appended)?
	if (index < getColumnCount() - 1) {				
		reindexColumns(index + 1);
	}
	// is there more than one user created column?
	// There always is the data and visual of the default column
	// so we don't need to create those for the first user column
	if (getColumnCount() > 1) {
		insertColumnData(column);
	}
	else {								// first user created column
		setContentWidth(0);				// pretend it's ground zero for column resizings
		redraw();						// redraw the table and header. The default column 
		getHeader().redraw();			// won't be drawn anymore, because there now is a user created table.
	}
	insertColumnVisual(column);
}
/**
 * Add 'item' to the receiver.
 * @param item - new table item that should be added to 
 *	the receiver
 * @param index - position the new item should be inserted at
 */
void addItem(TableItem item, int index) {
	Vector items = getItemVector();

	if (index < 0 || index > getItemCount()) {
		error(SWT.ERROR_INVALID_RANGE);
	}	
	addingItem(item, index);
	item.setIndex(index);
	if (index < items.size()) {
		for (int i = index; i < items.size(); i++) {
			TableItem anItem = (TableItem) items.elementAt(i);
			anItem.setIndex(anItem.getIndex() + 1);
		}
		items.insertElementAt(item, index);
	}
	else {
		items.addElement(item);
	}
	addedItem(item, index);
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*/
public void addSelectionListener (SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	TypedListener typedListener;

	if (listener == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	typedListener = new TypedListener(listener);	
	addListener(SWT.Selection, typedListener);
	addListener(SWT.DefaultSelection, typedListener);
}
static int checkStyle (int style) {
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
/**
 * The width of 'column' is about to change.
 * Adjust the position of all columns behind it.
 */
void columnChange(TableColumn column, Rectangle newBounds) {
	Rectangle columnBounds = column.getBounds();
	Rectangle clientArea = getClientArea();
	int oldXPosition = columnBounds.x + columnBounds.width; 
	int newXPosition = newBounds.x + newBounds.width;
	int widthChange = newBounds.width - columnBounds.width;
	int headerHeight = getHeaderHeight();
	int columnIndex = column.getIndex();

	if (widthChange != 0) {
		if (columnIndex != TableColumn.FILL) {
			if (getLinesVisible() == true) {
				oldXPosition -= getGridLineWidth();						// include vertical grid line when scrolling resized column.
				newXPosition -= getGridLineWidth();
			}
			scroll(														// physically move all following columns
				newXPosition, headerHeight, 							// destination x, y
				oldXPosition, headerHeight, 							// source x, y
				clientArea.width, clientArea.height, true);
		}
		column.internalSetBounds(newBounds);
		if (columnIndex != TableColumn.FILL) {
			resetTableItems(columnIndex);
			moveColumns(columnIndex + 1, widthChange);					// logically move all following columns	(set their bounds)
			setContentWidth(getContentWidth() + widthChange);			// set the width of the receiver's content
			claimRightFreeSpace();
			resizeRedraw(column, columnBounds.width, newBounds.width);
		}
	}
	getHeader().widthChange(columnIndex, widthChange);
}
/**
 * The mouse pointer was double clicked on the receiver.
 * Handle the event according to the position of the mouse click
 * and the modifier key that was pressed, if any.
 * @param event - the mouse event
 */
void columnMouseDoubleClick(Event event) {
	int itemHeight = getItemHeight();
	int itemIndex;
	TableItem hitItem;
	TableColumn hitColumn = getColumnAtX(event.x);
	Event columnDblClickEvent;

	if (isFocusControl() == false) {
		setFocus();									// focus proxy gets focus here because it's the first child of the receiver
	}
	if (hitColumn != null) {
		itemIndex = (event.y - getHeaderHeight()) / itemHeight + getTopIndex();
		hitItem = (TableItem) getVisibleItem(itemIndex);
		if (hitItem != null && 
			hitColumn.getIndex() == TableColumn.FIRST) {
			if (hitItem.isSelectionHit(event.x) == true) {
				columnDblClickEvent = new Event();
				columnDblClickEvent.item = hitItem;
				notifyListeners(SWT.DefaultSelection, columnDblClickEvent);
			}
		}
		else {
			deselectAll();
		}
	}
}
/**
 * The mouse pointer was pressed down on the receiver.
 * Handle the event according to the position of the mouse click
 * and the modifier key that was pressed, if any.
 * @param event - the mouse event
 */
void columnMouseDown(Event event) {
	int itemHeight = getItemHeight();
	int itemIndex;
	TableItem hitItem;
	TableColumn hitColumn = getColumnAtX(event.x);

	if (isFocusControl() == false) {
		setFocus();									// focus proxy gets focus here because it's the first child of the receiver
	}
	if (hitColumn != null) {
		itemIndex = (event.y - getHeaderHeight()) / itemHeight + getTopIndex();
		hitItem = (TableItem) getVisibleItem(itemIndex);
		if (hitItem != null) {
			if (hitItem.isSelectionHit(event.x) == true) {
				doMouseSelect(hitItem, itemIndex, event.stateMask, event.button);
			}
			else 
			if (hitItem.isCheckHit(new Point(event.x, event.y)) == true) {
				doCheckItem(hitItem);
			}
		}
		else {
			deselectAll();
		}
	}
}
/**
 * The mouse pointer was moved over the receiver.
 * Reset the column resize cursor if it was active.
 * @param event - the mouse event
 */
void columnMouseMove(Event event) {
	if (isColumnResizeStarted() == false) {
		setColumnResizeCursor(false);
	}
}
/**
 * Answer the size of the receiver needed to display all or 
 * the first 50 items whichever is less.
 */
public Point computeSize(int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Point size = super.computeSize(wHint, hHint, changed);
	Point headerSize;
	GC gc;
	final int WidthCalculationCount = Math.min(getItemCount(), 50);		// calculate item width for the first couple of items only
	TableItem item;
	Image itemImage;
	String itemText;
	int width;
	int newItemWidth = 0;
		
	if (getHeaderVisible() == true) {
		headerSize = getHeader().computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
		size.y += headerSize.y;		
	}
	if (getContentWidth() == 0 && WidthCalculationCount > 0) {
		gc = new GC(this);
		for (int i = 0; i < WidthCalculationCount; i++) {
			item = getItem(i);
			if (item == null) {
				break;											// no more items
			}
			itemImage = item.getImage();
			itemText = item.getText();
			width = 0;
			if (itemImage != null) {
				width += itemImage.getBounds().width;
			}
			if (itemText != null) {
				width += gc.stringExtent(itemText).x;
			}
			newItemWidth = Math.max(newItemWidth, width);
		}
		if (newItemWidth > 0) {
			size.x = newItemWidth;
		}
		gc.dispose();
	}
	return size;
}
/**
 * Deselect the items identified by the indices stored 
 * in 'indices'.
 * A SWT.Selection event will not be sent.
 * @param indices - indices of the items to deselect
 */
public void deselect(int indices[]) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);	
	SelectableItem item = null;
	
	if (indices == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	for (int i = 0; i < indices.length; i++) {
		item = getVisibleItem(indices[i]);
		if (item != null) {
			deselect(item);
		}
	}
	if (item != null) {
		setLastSelection(item, false);
	}
}
/**
 * Deselect the item identified by 'index'.
 * A SWT.Selection event will not be sent.
 * @param index - index of the item to deselect
 */
public void deselect(int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	SelectableItem item = getVisibleItem(index);

	if (item != null) {
		deselect(item);
		setLastSelection(item, false);		
	}
}
/**
 * Deselect a range of items starting at index 'start' 
 * and stopping at index 'end'. Indices that are out of 
 * range are ignored. Indexing is zero based.
 * @param start - the start of the range
 * @param end - the end of the range
 */
public void deselect(int start, int end) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	SelectableItem item = null;

	for (int i=start; i<=end; i++) {
		item = getVisibleItem(i);
		if (item != null) {
			deselect(item);
		}
	}
	if (item != null) {
		setLastSelection(item, false);
	}	
}
/**
 * Deselect all items of the receiver.
 */
public void deselectAll() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	deselectAllExcept((SelectableItem) null);
}
/**
 * Free resources.
 */
void doDispose() {
	Vector items = getItemVector();
	
	super.doDispose();
	while (items.size() > 0) {								// TableItem objects are removed from vector during dispose()
		((TableItem) items.lastElement()).dispose();
	}
	items = getColumnVector();
	while (items.size() > 0) {								// TableColumn objects are removed from vector during dispose()
		((TableColumn) items.lastElement()).dispose();
	}
	resizeColumn = null;
	fillColumn = null;
	defaultColumn = null;
	if (columnResizeCursor != null) {
		columnResizeCursor.dispose();
	}
}
/**
 * Draw a line tracking the current position of a column 
 * resize operation.
 * @param xPosition - x coordinate to draw the line at
 */
void drawColumnResizeLine(int xPosition) {
	GC gc = new GC(this);
	int lineHeight = getClientArea().height;

	redraw(getColumnResizeX(), 0, 1, lineHeight, false);
	setColumnResizeX(xPosition);
	gc.drawLine(xPosition, 0, xPosition, lineHeight);
	gc.dispose();
}
/**
 * Draw the grid lines for the receiver.
 * @param event - Paint event triggering the drawing operation.
 * @param drawColumns - The table columns for which the grid 
 *	lines should be drawn.
 */
void drawGridLines(Event event, Enumeration drawColumns) {
	GC gc = event.gc;
	Color oldForeground = getForeground();
	Rectangle columnBounds;
	TableColumn column;
	int lineWidth = getGridLineWidth();
	int itemHeight = getItemHeight();
	int headerHeight = getHeaderHeight();
	int lineXPosition;
	int lineYPosition = headerHeight + ((event.y-headerHeight) / itemHeight) * itemHeight;
	int lineYStopPosition = event.y + event.height;

	gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
	// Draw the horizontal lines	
	if (itemHeight > 0) {
		while (lineYPosition < lineYStopPosition) {
			gc.drawLine(
				event.x, lineYPosition + itemHeight - lineWidth, 
				event.x + event.width, lineYPosition + itemHeight - lineWidth);
			lineYPosition += itemHeight; 		
		}
	}
	// Draw the vertical lines at the right border of each column except the fill column
	while (drawColumns.hasMoreElements() == true) {
		column = (TableColumn) drawColumns.nextElement();
		if (column.getIndex() != TableColumn.FILL) {
			columnBounds = column.getBounds();
			lineXPosition = columnBounds.x + columnBounds.width - lineWidth;
			gc.drawLine(
				lineXPosition, event.y, 
				lineXPosition, event.y + event.height);
		}
	}
	gc.setForeground(oldForeground);
}
/**
 * Draw a filled rectangle indicating the selection state of 'item'
 * If 'item' is selected the rectangle will be filled with the 
 * selection background color. Otherwise the rectangle will be filled 
 * with the background color to remove selection.
 * The selection color depends on whether the table widget has 
 * focus or not. See getSelectionBackgroundColor() for details.
 * The rectangle is drawn in either the first column or in all columns 
 * for full row select.
 * @param item - item for which the selection state should be drawn
 * @param gc - GC to draw on. 
 * @param position - position on the GC to draw at.
 * @param extent - extent of the selection rectangle.
 */
void drawSelection(TableItem item, GC gc, Point position, Point extent) {
	if (item.isSelected() == true) {
		gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
	}
	gc.fillRectangle(position.x, position.y, extent.x, extent.y);
	if (item.isSelected() == true) {
		gc.setBackground(getBackground());
	}
}
/**
 * If the receiver has input focus draw a rectangle enclosing 
 * the label of 'item' to indicate the input focus.
 * The rectangle is drawn in either the first column or in all columns 
 * for full row select. 
 * @param item - item for which the selection state should be drawn
 * @param gc - GC to draw on. 
 */
void drawSelectionFocus(TableItem item, GC gc) {
	Point extent = item.getSelectionExtent();
	Point position = new Point(
		item.getImageStopX(TableColumn.FIRST) + getHorizontalOffset(),
		getRedrawY(item));

	gc.drawFocus(position.x, position.y, extent.x, extent.y);
}

/**	Not used right now. Replace focusIn/focusOut with this method once 
 *	Display.getFocusWindow returns the new focus window on FocusOut event
 * The focus has moved in to or out of the receiver.
 * Redraw the item selection to reflect the focus change.
 * @param event - the focus change event
 */
void focusChange(Event event) {
	TableColumn focusColumn = getColumnAtX(event.x);
	Control focusWindow = getDisplay().getFocusControl();

	if (focusWindow == getFocusWindow() && 
		focusColumn != null && 
		focusColumn.getIndex() == TableColumn.FIRST) {
		hasColumnFocus = true;
	}
	else {
		hasColumnFocus = false;
	}
	super.focusChange(event);
	event.widget = this;									// the ficus event is never sent to the table itself but only to the focus widget
	notifyListeners(event.type, event);						// make sure that listeners of the table get the focus event, too
}
/**
 * The focus has moved in to or out of the receiver.
 * @param event - the focus change event
 */
void focusIn(Event event) {
	TableColumn focusColumn = getColumnAtX(event.x);

	if (focusColumn != null && 
		focusColumn.getIndex() == TableColumn.FIRST) {
		hasColumnFocus = true;
	}
	super.focusIn(event);
	event.widget = this;									// the focus event is never sent to the table itself but only to the focus widget
	notifyListeners(event.type, event);						// make sure that listeners of the table get the focus event, too
}
/**
 * The focus has moved in to or out of the receiver.
 * @param event - the focus change event
 */
void focusOut(Event event) {
	TableColumn focusColumn = getColumnAtX(event.x);

	if (focusColumn != null && 
		focusColumn.getIndex() == TableColumn.FIRST) {
		hasColumnFocus = false;
	}
	super.focusOut(event);
	event.widget = this;									// the focus event is never sent to the table itself but only to the focus widget
	notifyListeners(event.type, event);						// make sure that listeners of the table get the focus event, too
}
/**
 * Answer the TableColumn at 'index'.
 */
public TableColumn getColumn(int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Vector columns = getColumnVector();
	
	if (columns == null) error(SWT.ERROR_CANNOT_GET_ITEM);
	if (index < 0 || index >= columns.size()) {
		error(SWT.ERROR_INVALID_RANGE);
	}
	
	return (TableColumn) columns.elementAt(index);
}
/**
 * Return the column located at 'xPosition' in the widget.
 * Return null if xPosition is outside the widget.
 * @param xPosition - position of the desired column
 */
TableColumn getColumnAtX(int xPosition) {
	Enumeration columns = internalGetColumnVector().elements();
	TableColumn column;
	TableColumn hitColumn = null;
	Rectangle bounds;

	while (columns.hasMoreElements() == true && hitColumn == null) {
		column = (TableColumn) columns.nextElement();
		bounds = column.getBounds();
		if ((xPosition >= bounds.x) && 
			(xPosition <= bounds.x + bounds.width)) {
			hitColumn = column;
		}
	}
	if (hitColumn == null) {
		column = getFillColumn();
		bounds = column.getBounds();
		if ((xPosition >= bounds.x) && 
			(xPosition <= bounds.x + bounds.width)) {
			hitColumn = column;
		}
	}
	return hitColumn;
}
/**
 * Answer the number of columns in the receiver.
 */
public int getColumnCount() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Vector columns = getColumnVector();
	int count = 0;
	
	if (columns != null) {
		count = columns.size();
	}
	return count;
}
/** Replace CURSOR_SIZEWE with real column resize cursor 
 *	(no standard cursor-have to load from file)
 * Answer the cursor displayed during a column resize 
 * operation.
 * Lazy initialize the cursor since it may never be needed.
 */
Cursor getColumnResizeCursor() {
	if (columnResizeCursor == null) {
		columnResizeCursor = new Cursor(getDisplay(), SWT.CURSOR_SIZEWE);
	}
	return columnResizeCursor;
}
/**
 * Answer the current position of the mouse cursor during
 * a column resize operation.
 */
int getColumnResizeX() {
	return columnResizeX;
}
/**
 * Answer an Array containing all columns of receiver except 
 * the fill column to the right of all content columns.
 */
public TableColumn [] getColumns() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Vector columns = getColumnVector();
	TableColumn columnArray[] = new TableColumn[columns.size()];

	columns.copyInto(columnArray);
	return columnArray;
}
/**
 * Answer a Vector containing all columns of receiver except 
 * the fill column to the right of all content columns.
 */
Vector getColumnVector() {
	return columns;
}
/**
 * Return the default column that is created as soon as the table 
 * is created.
 * Fix for 1FUSJY5
 */
TableColumn getDefaultColumn() {
	return defaultColumn;
}
/**
 * Answer the width of the replacement String used to indicate 
 * truncated items.
 * Cached to speed up calculation of truncated items.
 * @param gc - GC used to measure the width of the replacement 
 *	String
 */
int getDotsWidth(GC gc) {
	if (dotsWidth == -1) {
		dotsWidth = gc.stringExtent(DOT_STRING).x;
	}
	return dotsWidth;
}
/**
 * Answer the column used to occupy any space left to the 
 * right of all the user created columns.
 */
TableColumn getFillColumn() {
	return fillColumn;
}
/**
 * Answer the widget that is used to hold focus for the receiver.
 * The receiver can not get focus itself because it has children.
 */
Control getFocusWindow() {
	return focusProxy;
}
/**
* Gets the width of a grid line.
* <p>
* @return the width of a grid line
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getGridLineWidth () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	return 1;
}
/**
 * Answer the table header widget.
 */
Header getHeader() {
	return tableHeader;
}
/**
 * Answer the header height or 0 if the header is not visible.
 */
int getHeaderHeight() {
	Header header = getHeader();
	int height = 0;
	
	if (header.getVisible() == true) {
		height = header.getBounds().height;
	}
	return height;
}
/**
 * Answer whether the header is visible.
 * @return 
 *	true = header is visible. false = header is not visible
 */
public boolean getHeaderVisible() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	return getHeader().getVisible();
}
/**
 * Answer the image extent of 'item'. Use the image of any column.
 */
Point getImageExtent(SelectableItem item) {
	Image image = null;
	Rectangle imageBounds;
	Point imageExtent = null;
	int columnCount = internalGetColumnCount();

	for (int i = 0; i < columnCount && image == null; i++) {
		image = ((TableItem) item).getImage(i);
	}		
	if (image != null) {
		imageBounds = image.getBounds();
		imageExtent = new Point(imageBounds.width, imageBounds.height);
	}
	return imageExtent;
}
/**
 * Answer the index of 'item' in the receiver.
 */
int getIndex(SelectableItem item) {
	int index = -1;
	
	if (item != null && item.getSelectableParent() == this) {
		index = ((TableItem) item).getIndex();
	}
	return index;
}
/**
 * Answer the TableItem located at 'index' in the receiver.
 * @param index - location of the TableItem object to return
 */
public TableItem getItem(int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	if (!(0 <= index && index < getItemCount())) {
		error(SWT.ERROR_INVALID_RANGE);
	}		
	return (TableItem) getVisibleItem(index);	
}

/**
 * Return the item at the specified position in the widget
 * Return null if the position is outside the widget or in the header widget.
 */
public TableItem getItem(Point point) {
	int headerHeight = getHeaderHeight();
	TableColumn column = getColumnAtX(point.x);
	TableItem item = null;

	if (column != null && column.getIndex() != TableColumn.FILL && point.y - headerHeight > 0) {
		int itemIndex = (point.y - headerHeight) / getItemHeight() + getTopIndex();
		item = (TableItem) getVisibleItem(itemIndex);
		if (item != null) {
			Point itemSize = item.getItemExtent(column);
			if (point.x - column.getBounds().x > itemSize.x) {
				item = null;
			}
		}
	}
	return item;
}
/**
 * Answer the number of items in the receiver.
 */
public int getItemCount() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	return getItemVector().size();
}
/**
 * Answer the number of items that can be displayed in the
 * client area of the receiver without truncating any items.
 */
int getItemCountWhole() {
	int clientAreaHeight = Math.max(0, getClientArea().height - getHeaderHeight());
	
	return clientAreaHeight / getItemHeight();
}
/**
 * Answer the height of an item in the receiver.
 * The item height is the greater of the item icon height and 
 * text height of the first item that has text or an image 
 * respectively.
 * Calculate a default item height based on the font height if
 * no item height has been calculated yet.
 */
public int getItemHeight() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	return super.getItemHeight();
}
/**
 * Answer the number of pixels that should be added to the item height.
 */
int getItemPadding() {
	return getGridLineWidth() + getDisplay().textHighlightThickness + 1;
}
/**
 * Answer all items of the receiver as an Array.
 */
public TableItem [] getItems() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Vector items = getItemVector();
	TableItem itemArray[] = new TableItem[items.size()];

	items.copyInto(itemArray);
	return itemArray;
}
/**
 * Answer all items of the receiver.
 */
Vector getItemVector() {
	return items;
}
/**
 * Answer whether the receiver is drawing grid lines.
 * @return
 *	true = receiver draws grid lines
 *	false = receiver does not draw grid lines
 */
public boolean getLinesVisible() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	return drawGridLines;
}
/** 
 * Answer a Vector containing the columns that need repainting 
 * based on the 'paintArea'.
 * @param paintArea - invalidated rectangle that needs repainting
 */
Vector getPaintColumns(Rectangle paintArea) {
	Enumeration columns = internalGetColumnVector().elements();
	Vector paintColumns = new Vector();
	TableColumn column;
	Rectangle columnBounds;
	int paintAreaRightBorder = paintArea.x + paintArea.width;

	while (columns.hasMoreElements() == true) {
		column = (TableColumn) columns.nextElement();
		columnBounds = column.getBounds();
		if ((columnBounds.x + columnBounds.width >= paintArea.x) &&	// does the paintArea overlap the current column?
			(columnBounds.x <= paintAreaRightBorder)) {
			paintColumns.addElement(column);
		}
	}
	return paintColumns;
}
/** 
 * Return the width of the widest item in the column identified by 'columnIndex'
 * @param columnIndex - index of the column whose preferred width should be
 *	calculated
 */
int getPreferredColumnWidth(int columnIndex) {
	Enumeration tableItems = getItemVector().elements();
	TableItem tableItem;
	int width = 0;
	int headerWidth;
	
	if (columnIndex != TableColumn.FILL) {
		while (tableItems.hasMoreElements() == true) {
			tableItem = (TableItem) tableItems.nextElement();
			width = Math.max(width, tableItem.getPreferredWidth(columnIndex));
		}
		headerWidth = getHeader().getPreferredWidth(columnIndex);
		if (width < headerWidth) {
			width = headerWidth;
		}
	}
	return width;
}
/**
 * Answer the position in the receiver where 'item' is drawn
 * @return the y coordinate at which 'item' is drawn.
 *	Return -1 if 'item' is not an item of the receiver 
 */
int getRedrawY(SelectableItem item) {
	int redrawY = super.getRedrawY(item);

	if (redrawY != -1) {
		redrawY += getHeaderHeight();
	}
	return redrawY;
}
/**
 * Answer the column that is being resized or null if no 
 * resize operation is in progress.
 */
TableColumn getResizeColumn() {
	return resizeColumn;
}
/**
 * Return the positions at which the column identified by 'columnIndex' 
 * must be redrawn.
 * These positions may be different for each item since each item may 
 * have a different label
 * @param columnIndex - the column index
 * @param columnWidth - width of the column
 * @return the positions at which the column must be redrawn.
 *	Each item in the widget client area is represented by a slot in 
 * 	the array. The item at position 'topIndex' is the first item in 
 *	the array.
 */
int [] getResizeRedrawX(int columnIndex, int columnWidth) {
	int topIndex = getTopIndex();
	int bottomIndex = getBottomIndex();
	int resizeRedrawX[];
	TableItem item;

	bottomIndex = Math.min(bottomIndex, getItemCount());
	resizeRedrawX = new int[bottomIndex-topIndex+1];
	for (int i = topIndex; i < bottomIndex; i++) {
		item = (TableItem) getVisibleItem(i);
		resizeRedrawX[i-topIndex] = item.getDotStartX(columnIndex, columnWidth);
	}
	return resizeRedrawX;
}
/**
 * Answer the selected items of the receiver.
 * @return an Array of TableItems containing the selected items.
 *	An empty Array if no items are selected.
 */
public TableItem [] getSelection() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Vector selectionVector = getSelectionVector();
	TableItem[] selectionArray = new TableItem[selectionVector.size()];

	selectionVector.copyInto(selectionArray);
	sort(selectionArray, 0, selectionArray.length);
	return selectionArray;
}

/**
 * Answer the number of selected items in the receiver.
 */
public int getSelectionCount() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	return super.getSelectionCount();
}
/**
 * Answer the size of the full row selection rectangle for 'item'.
 */
Point getFullSelectionExtent(TableItem item) {
	TableColumn lastColumn = (TableColumn) internalGetColumnVector().lastElement();
	Point selectionExtent = null;
	Rectangle columnBounds;
	int xPosition = item.getImageStopX(TableColumn.FIRST);
	int gridLineWidth = getGridLineWidth();

	if (lastColumn != null) {
		columnBounds = lastColumn.getBounds();
		selectionExtent = new Point(
			columnBounds.x - getHorizontalOffset() + columnBounds.width - xPosition - gridLineWidth, 
			getItemHeight());
		if (getLinesVisible() == true) {
			selectionExtent.y -= gridLineWidth;
		}	
	}
	return selectionExtent;
}
/**
 * Answer the index of the first selected item of the receiver.
 */
public int getSelectionIndex() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int index = -1;
	
	if (getSelectionCount() > 0) {
		index = getIndex(getSelection()[0]);
	}
	return index;
}
/**
 * Answer the indices of the selected items of the receiver.
 * @return an Array containing the indices of the selected items.
 *	An empty Array if no items are selected.
 */
public int [] getSelectionIndices() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	TableItem[] items = getSelection();
	int indices[] = new int[items.length];

	for (int i = 0; i < items.length; i++) {
		indices[i] = getIndex(items[i]);
	}	
	return indices;
}
/**
 * Answer the index of the first visible item in the receiver's 
 * client area.
 * @return 0-based index of the first visible item in the  
 * 	receiver's client area.
 */
public int getTopIndex() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	return super.getTopIndex();
}
/**
 * Answer the index of 'item' in the receiver.
 * Answer -1 if the item is not visible.
 * The returned index must refer to a visible item.
 * Note: 
 * 	Visible in this context does not neccessarily mean that the 
 * 	item is displayed on the screen. It only means that the item 
 * 	would be displayed if it is located inside the receiver's 
 * 	client area.
 *	Every item in a table widget should be visible.
 */
int getVisibleIndex(SelectableItem item) {
	return getIndex(item);
}
/**
 * Answer the SelectableItem located at 'itemIndex' in the receiver.
 * @param itemIndex - location of the SelectableItem object to return
 */
SelectableItem getVisibleItem(int itemIndex) {
	Vector items = getItemVector();
	TableItem item = null;
	
	if ((items != null) && (itemIndex >= 0) && (itemIndex < items.size())) {
		item = (TableItem) items.elementAt(itemIndex);
	}
	return item;
}
/**
 * Answer the y coordinate at which 'item' is drawn. 
 * @param item - SelectableItem for which the paint position 
 *	should be returned
 * @return the y coordinate at which 'item' is drawn.
 *	Return -1 if 'item' is null or outside the client area
 */
int getVisibleRedrawY(SelectableItem item) {
	int redrawY = -1;
	int index = getTopIndex();
	int bottomIndex = getBottomIndex();
	
	if (item == null) {
		return redrawY;
	}
	while (index < bottomIndex && item.equals(getVisibleItem(index)) == false) {
		index++;
	}
	if (index < bottomIndex) {
		redrawY = getRedrawY(item);
	}
	return redrawY;
}
/**
 * Handle the events the receiver is listening to.
 */
void handleEvents(Event event) {
	switch (event.type) {
		case SWT.MouseMove:
			if (event.widget == tableHeader) {
				headerMouseMove(event);
			}
			else {
				columnMouseMove(event);
			}
			break;
		case SWT.MouseDown:
			if (event.widget == tableHeader) {
				headerMouseDown(event);
			}
			else {
				columnMouseDown(event);
			}
			break;
		case SWT.MouseDoubleClick:
			columnMouseDoubleClick(event);
			break;
		case SWT.MouseUp:
			mouseUp(event);
			break;
		case SWT.Paint:
			paint(event);
			break;
		default:
			super.handleEvents(event);
	}		
}
/**
 * Answer true if any item in the first column has an image.
 * Answer false otherwise.
 */
boolean hasFirstColumnImage() {
	return firstColumnImage;
}
/**
 * Answer whether the receiver has the input focus.
 */
public boolean isFocusControl() {
	return hasColumnFocus;
}
/**
 * The mouse pointer was pressed down on the receiver's header
 * widget. Start a column resize operation if apropriate.
 * @param event - the mouse event that occured over the header 
 *	widget
 */
void headerMouseDown(Event event) {
	TableColumn column = getColumnAtX(event.x);

	if (isColumnResize(event) == true) {
		startColumnResize(event);
	}
	else
	if (column != null) {
		column.notifyListeners(SWT.Selection, new Event());
	}
}
/**
 * The mouse pointer was moved over the receiver's header widget.
 * If a column is currently being resized a vertical line indicating 
 * the new position of the resized column is drawn.
 * Otherwise, if no column resize operation is in progress, the 
 * column resize cursor is displayed when the mouse is near the border 
 * of a column.
 */
void headerMouseMove(Event event) {
	if (isColumnResizeStarted() == false) {				// only check whether cursor is in resize
		setColumnResizeCursor(isColumnResize(event));	// area if no resize operation is in progress
	}
	else 
	if (event.x >= getResizeColumn().getBounds().x) {
		drawColumnResizeLine(event.x);
		update();										// looks better if resize line is drawn immediately
	}
}
/**
 * Answer the index of 'column' in the receiver or -1 if 'column' 
 * does not exist in the receiver.
 */
public int indexOf(TableColumn column) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	if (column == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	return internalGetColumnVector().indexOf(column);
}
/**
 * Answer the index of 'item' in the receiver or -1 if 'item' 
 * does not exist in the receiver.
 */
public int indexOf(TableItem item) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	if (item == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}	
	return getIndex(item);
}
/**
 * Initialize the receiver. Create a header widget and an empty column.
 */
void initialize() {
	columns = new Vector();
	setItemVector(new Vector());
	focusProxy = new Button(this, SWT.NULL);
	focusProxy.setBounds(0, 0, 0, 0);				// make the focus proxy invisible
	tableHeader = new Header(this);
	tableHeader.setVisible(false);					// SWT table header is invisible by default, too
	fillColumn = TableColumn.createFillColumn(this);
	setColumnPosition(fillColumn);
	defaultColumn = TableColumn.createDefaultColumn(this);	// Create the default column. Fix for 1FUSJY5	
	super.initialize();
}
/**
 * Insert the new column 'column' into the table data at position 
 * 'index'.
 */
void insertColumnData(TableColumn column) {
	Enumeration tableItems = getItemVector().elements();
	TableItem tableItem;
	
	while (tableItems.hasMoreElements() == true ) {
		tableItem = (TableItem) tableItems.nextElement();
		tableItem.insertColumn(column);
	}
}
/**
 * Insert the new column 'column'.
 * Set the position and move the following columns to the right.
 */
void insertColumnVisual(TableColumn column) {
	Rectangle columnBounds = column.getBounds();
	Rectangle previousColumnBounds;
	int index = column.getIndex();
		
	if (index > 0) {
		previousColumnBounds = getColumn(index - 1).getBounds();
		columnBounds.x = previousColumnBounds.x + previousColumnBounds.width;
	}
	else {
		columnBounds.x = 0;
	}
	column.setBounds(columnBounds);
	setColumnPosition(column);
}
/**
 * Set event listeners for the receiver.
 */
void installListeners() {
	Header tableHeader = getHeader();
	Control focusWindow = getFocusWindow();
	Listener listener = getListener();

	super.installListeners();	
	tableHeader.addListener(SWT.MouseMove, listener);
	tableHeader.addListener(SWT.MouseDown, listener);
	tableHeader.addListener(SWT.MouseUp, listener);
	
	// HACK: All we're really interested in is focus change and key down
	// for the table itself. Doesn't work that way because setFocus sets 
	// focus to the first child of the receiver (which is our focus window)
	removeListener(SWT.FocusOut, listener);
	removeListener(SWT.FocusIn, listener);	
	focusWindow.addListener(SWT.FocusOut, listener);
	focusWindow.addListener(SWT.FocusIn, listener);
	focusWindow.addListener(SWT.KeyDown, listener);			
	
	addListener(SWT.MouseMove, listener);
	addListener(SWT.MouseDown, listener);
	addListener(SWT.MouseDoubleClick, listener);
	addListener(SWT.MouseUp, listener);
	addListener(SWT.Paint, listener);
}
/**
 * Answer the TableColumn at 'index'.
 * If the user has not created any columns the default column is 
 * returned if index is 0.
 * Fix for 1FUSJY5 
 */
TableColumn internalGetColumn(int index) {
	Vector columns = internalGetColumnVector();
	
	if (columns == null) error(SWT.ERROR_CANNOT_GET_ITEM);
	if (index < 0 || index >= columns.size()) {
		error(SWT.ERROR_INVALID_RANGE);
	}
	
	return (TableColumn) columns.elementAt(index);

}
/**
 * Answer the number of columns in the receiver.
 * If the user has not created any columns, 1 is returned since there 
 * always is a default column.
 * Fix for 1FUSJY5
 */
int internalGetColumnCount() {
	Vector columns = internalGetColumnVector();
	int count = 0;
	
	if (columns != null) {
		count = columns.size();
	}
	return count;
}
/**
 * Return a Vector containing all columns of the receiver except 
 * the fill column to the right of all content columns.
 * Return a Vector containing the default column if the user has
 * not created any columns.
 * Fix for 1FUSJY5 
 */
Vector internalGetColumnVector() {
	Vector internalColumnVector;
	TableColumn defaultColumn;
	
	if (columns.isEmpty() == false) {
		internalColumnVector = columns;
	}
	else {
		internalColumnVector = new Vector(1);
		defaultColumn = getDefaultColumn();		
		if (defaultColumn != null) {
			internalColumnVector.addElement(defaultColumn);
		}
	}
	return internalColumnVector;
}
/**
 * Answer whether the mouse pointer is at a position that can
 * start a column resize operation. A column resize can be 
 * started if the mouse pointer is at either the left or right 
 * border of a column.
 * @param event - mouse event specifying the location of the 
 *	mouse pointer.
 */
boolean isColumnResize(Event event) {
	TableColumn hotColumn = getColumnAtX(event.x);
	Rectangle bounds = hotColumn.getBounds();
	int hotColumnIndex = hotColumn.getIndex();
	int columnX = event.x - bounds.x;
	boolean isColumnResize = false;

	if (columnX <= COLUMN_RESIZE_OFFSET && 									// mouse over left side of column? and
		hotColumnIndex != TableColumn.FIRST) {								// it's not the first column)
		if (hotColumnIndex == TableColumn.FILL) {
			hotColumn = (TableColumn) internalGetColumnVector().lastElement();
		}
		else {
			hotColumn = internalGetColumn(hotColumnIndex - 1);
		}
		isColumnResize = hotColumn.getResizable();							// check whether left column can be resized
	}
	else
	if (columnX >= bounds.width - COLUMN_RESIZE_OFFSET && 					// mouse over right side of column and
		hotColumn != getFillColumn()) {										// column is a real one (not the right hand fill column)?
		isColumnResize = hotColumn.getResizable();							// check whether column under cursor can be resized
	}
	return isColumnResize;
}
/**
 * Answer whether a column of the receiver is being resized.
 */
boolean isColumnResizeStarted() {
	return (getResizeColumn() != null);
}
/**
 * Answer whether the item identified by 'index' is selected.
 * @return 
 *	true=item identified by 'index' is selected
 *	false=item identified by 'index' is not selected.
 */
public boolean isSelected(int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	TableItem item = getItem(index);

	return (item != null && item.isSelected() == true);
}
/**
 * 'changedItem' has changed. Update the default column width.
 * @param changedItem - the item that has changed
 */
void itemChanged(SelectableItem changedItem, int repaintStartX, int repaintWidth) {
	// call super.itemChanged first to make sure that table image size is 
	// calculated if necessary. Fixes 1FYPBBG.
	super.itemChanged(changedItem, repaintStartX, repaintWidth);
	// remember if any item ever had an image in the first column.
	if (firstColumnImage == false && changedItem.getImage() != null) {
		firstColumnImage = true;
	}
	setFirstColumnWidth((TableItem) changedItem);	
}
/**
 * A mouse button was released. 
 * Update the display if a column has been resized.
 * @param event - the mouse event for the button up action
 */
void mouseUp(Event event) {
	TableColumn resizeColumn = getResizeColumn();
	Rectangle oldColumnBounds;
	int resizeXPosition;
	int widthChange;
	
	if (isColumnResizeStarted() == true) {
		oldColumnBounds = resizeColumn.getBounds();
		resizeXPosition = getColumnResizeX();	
		widthChange = resizeXPosition - (oldColumnBounds.x + oldColumnBounds.width);
		if (widthChange != 0) {
			if (widthChange > 0) {
				redraw(resizeXPosition, 0, 1, getClientArea().height, false);		// remove resize line
				update();															// to avoid cheese caused by scrolling the resize line
			}
			resizeColumn.setWidth(oldColumnBounds.width + widthChange);
		}
		setResizeColumn(null);
	}
}
/**
 * Adjust the position of all columns starting at 'startIndex'.
 * @param startIndex - index at which the column move should begin
 *	If this is the index of the fill column all columns are moved,
 * 	including the fill column
 * @param moveDistance - distance that the columns should be moved.
 *	< 0 = columns are going to be moved left.
 *	> 0 = columns are going to be moved right.
 */
void moveColumns(int startIndex, int moveDistance) {
	Vector columns = internalGetColumnVector();
	TableColumn moveColumn;
	Rectangle columnBounds;

	if (startIndex == TableColumn.FILL) {
		moveColumn = getFillColumn();
		columnBounds = moveColumn.getBounds();
		columnBounds.x += moveDistance;
		moveColumn.setBounds(columnBounds);
		startIndex = 0;					// continue with first data column
	}
	for (int i = startIndex; i < columns.size(); i++) {
		moveColumn = (TableColumn) columns.elementAt(i);
		columnBounds = moveColumn.getBounds();
		columnBounds.x += moveDistance;
		moveColumn.setBounds(columnBounds);
	}
}
/**
 * Adjust the y position of all columns including the fill column.
 */
void moveColumnsVertical() {
	Enumeration columns = internalGetColumnVector().elements();
	TableColumn column;

	setColumnPosition(getFillColumn());
	while (columns.hasMoreElements() == true) {
		column = (TableColumn) columns.nextElement();
		setColumnPosition(column);
	}
}
/** 
 * A paint event has occurred. Paint the invalidated items.
 * @param event - paint event specifying the invalidated area.
 */
void paint(Event event) {
	int visibleRange[];
	int headerHeight = getHeaderHeight();
	Vector paintColumns = getPaintColumns(event.getBounds());
	TableItem focusItem = null;
	
	if (paintColumns.size() > 0) {
		event.y -= headerHeight;
		visibleRange = getIndexRange(event.getBounds());
		event.y += headerHeight;
		// When the top index is > 0 and the receiver is resized 
		// higher so that the top index becomes 0 the invalidated 
		// rectangle doesn't start below the header widget but at 
		// y position 0. Subtraction of the header height (it is 
		// not above the receiver but on top) causes event.y and 
		// subsequently visibleRange[0] to be negative.
		// Hack to prevent visibleRange[0] from becoming negative.
		// Need to find out why the invalidated area starts at 0
		// in the first place.
		if (visibleRange[0] < 0) {
			visibleRange[0] = 0;
		}
		// 
		visibleRange[1] = Math.min(visibleRange[1], getItemCount()-1-getTopIndex());
		focusItem = paintItems(event, visibleRange[0], visibleRange[1], paintColumns);
	}
	if (getLinesVisible() == true) {
		drawGridLines(event, paintColumns.elements());
	}
	if (focusItem != null) {
		// draw focus on top of drawing grid lines so that focus rectangle 
		// is not obscured by grid. Fixes 1G5X20B
		drawSelectionFocus(focusItem, event.gc);	
	}
}

/**
 * Paint items of the receiver starting at index 'topPaintIndex' and 
 * ending at 'bottomPaintIndex'.
 * @param event - holds the GC to draw on and the clipping rectangle
 * @param topPaintIndex - index of the first item to draw
 * @param bottomPaintIndex - index of the last item to draw
 * @param paintColumns - the table columns that should be painted
 * @return the item that has focus if it was among the rendered items.
 *	null if the focus item was not rendered or if no item has focus (ie. 
 *	because the widget does not have focus)
 */
TableItem paintItems(Event event, int topPaintIndex, int bottomPaintIndex, Vector paintColumns) {
	Enumeration columns;
	TableColumn column;
	TableItem paintItem;
	TableItem focusItem = null;
	Point selectionExtent;
	Point selectionPosition;
	int itemHeight = getItemHeight();

	topPaintIndex += getTopIndex();
	bottomPaintIndex += getTopIndex();
	for (int i = topPaintIndex; i <= bottomPaintIndex; i++) {
		paintItem = (TableItem) getVisibleItem(i);
		selectionExtent = paintItem.getSelectionExtent();
		if (selectionExtent != null) {
			selectionPosition = new Point(paintItem.getSelectionX(), getRedrawY(paintItem));
			drawSelection(paintItem, event.gc, selectionPosition, selectionExtent);
		}
		columns = paintColumns.elements();
		while (columns.hasMoreElements() == true) {
			column = (TableColumn) columns.nextElement();
			paintSubItem(event, paintItem, column, i * itemHeight);
		}
		if (hasFocus(paintItem)) {
			focusItem = paintItem;
		}
	}
	return focusItem;
}

/**
 * Paint the table item 'paintItem' in 'column' at y position 
 * 'paintYPosition' of the receiver.
 * @param event - holds the GC to draw on and the clipping 
 *	rectangle.
 * @param paintItem - the item to draw
 * @param column - column to draw 'paintItem' in
 * @param paintYPosition - y position in the receiver to draw 
 *	'paintItem' at.
 */
void paintSubItem(Event event, TableItem paintItem, TableColumn column, int paintYPosition) {
	Rectangle columnBounds = column.getBounds();
	int gridLineWidth = getGridLineWidth();
	int itemDrawStopX = columnBounds.x + columnBounds.width - gridLineWidth;
	int clipX;
	
	if (event.x + event.width > itemDrawStopX) {	// does the invalidated area stretch past the current column's right border?
		clipX = Math.max(columnBounds.x, event.x);
		event.gc.setClipping(											// clip the drawing area
			clipX, event.y, 
			itemDrawStopX - clipX, event.height);		
	}
	column.paint(paintItem, event.gc, paintYPosition);
	if (event.x + event.width > itemDrawStopX) {
		event.gc.setClipping(event.x, event.y, event.width, event.height); // restore original clip rectangle
	}
}
/**
 * Reindex all columns starting at 'startIndex'.
 * Reindexing is necessary when a new column has been inserted.
 */
void reindexColumns(int startIndex) {
	Vector columns = getColumnVector();
	TableColumn column;
	
	for (int i = startIndex; i < getColumnCount(); i++) {
		column = (TableColumn) columns.elementAt(i);
		column.setIndex(i);
	}
}
/**
 * Remove the items identified by the indices stored in 
 * 'indices' from the receiver.
 * @param indices - indices of the items to dispose
 */
public void remove(int indices[]) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	SelectableItem item;
	int [] sortedIndices;
	int last = -1;
	
	if (indices == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	sortedIndices = new int[indices.length];	
	System.arraycopy (indices, 0, sortedIndices, 0, indices.length);
	sort(sortedIndices);								// sort indices in descending order
	for (int i = 0; i < sortedIndices.length; i++) {
		if (sortedIndices[i] != last) {
			item = getVisibleItem(sortedIndices[i]);
			if (item != null) {
				item.dispose();
			}
			else {
				error(SWT.ERROR_ITEM_NOT_REMOVED);
			}
			last = sortedIndices[i];			
		}
	}
}
/**
 * Remove the item identified by 'index'.
 * @param index - index of the item to dispose
 */
public void remove(int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	SelectableItem item = getVisibleItem(index);

	if (item != null) {
		item.dispose();
	}
	else {
		error(SWT.ERROR_ITEM_NOT_REMOVED);
	}
}
/**
 * Remove a range of items starting at index 'start' and 
 * stopping at index 'end'. 
 * This operation will fail when the index is out of range 
 * Indexing is zero based.
 * @param start - the start of the range
 * @param end - the end of the range
 */
public void remove(int start, int end) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	SelectableItem item;
	
	for (int i = end; i >= start; i--) {
		item = getVisibleItem(i);
		if (item != null) {
			item.dispose();
		}
		else {
			error(SWT.ERROR_ITEM_NOT_REMOVED);
		}
	}
}
/**
 * Remove all items of the receiver.
 */
public void removeAll() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Vector items = getItemVector();

	setRedraw(false);
	setRemovingAll(true);
	for (int i = 0; i < items.size(); i++) {
		((TableItem) items.elementAt(i)).dispose();
	}
	setItemVector(new Vector());
	reset();
	calculateVerticalScrollbar();
	setRemovingAll(false);
	setRedraw(true);	
}
/**
 * Remove 'column' from the receiver.
 */
void removeColumn(TableColumn column) {
	TableColumn lastColumn;
	int index = column.getIndex();
	int columnWidth = column.getWidth();
	int columnCount;

	if (isRemovingAll() == true) {
		getColumnVector().removeElementAt(index);
	}
	else {		
		getColumnVector().removeElementAt(index);
		columnCount = getColumnCount();
		// Never remove the data of the last user created column. 
		// SWT for Windows does the same.
		if (columnCount > 0) {
			removeColumnData(column);
		}
		removeColumnVisual(column);		
		if (index < columnCount) {					// is there a column after the removed one?
			reindexColumns(index);
		}
		// last user created column is about to be removed.
		if (columnCount == 0) {		
			TableColumn defaultColumn = getDefaultColumn();
			defaultColumn.pack();						// make sure the default column has the right size...
			setColumnPosition(defaultColumn);			// ...and is at the right position
		}
		// Fixes for 1G1L0UT
		// Reduce the content width by the width of the removed column
		setContentWidth(getContentWidth() - columnWidth);
		// claim free space
		claimRightFreeSpace();		
		//
	}
}
/**
 * Remove the column 'column' from the table data.
 */
void removeColumnData(TableColumn column) {
	Enumeration tableItems = getItemVector().elements();
	TableItem tableItem;

	while (tableItems.hasMoreElements() == true ) {
		tableItem = (TableItem) tableItems.nextElement();
		tableItem.removeColumn(column);
	}
}
/**
 * Remove the column 'column'.
 * Set the position of the following columns.
 */
void removeColumnVisual(TableColumn column) {
	int columnWidth = column.getWidth();
		
	// move following columns to the left
	moveColumns(column.getIndex() + 1, columnWidth * -1);
	redraw();
	getHeader().redraw();
}
/** 
 * Remove 'item' from the receiver. 
 * @param item - item that should be removed from the receiver
 */
void removeItem(TableItem item) {
	Vector items = getItemVector();
	int index = items.indexOf(item);

	if (index != -1) {
		if (isRemovingAll() == false) {
			removingItem(item);	
		}			
		items.removeElementAt(index);
		for (int i = index; i < items.size(); i++) {
			TableItem anItem = (TableItem) items.elementAt(i);
			anItem.setIndex(anItem.getIndex() - 1);
		}		
		if (isRemovingAll() == false) {
			removedItem(item);
		}			
	}
}
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
*/
public void removeSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	removeListener(SWT.Selection, listener);
	removeListener(SWT.DefaultSelection, listener);
}
/** 
 * Reset cached data of column at 'columnIndex' for the items of the receiver. 
 * @param columnIndex - index of the column for which the item data should be 
 *	reset.
 */
void resetTableItems(int columnIndex) {
	Enumeration tableItems = getItemVector().elements();
	TableItem tableItem;

	while (tableItems.hasMoreElements() == true ) {
		tableItem = (TableItem) tableItems.nextElement();
		tableItem.reset(columnIndex);
	}
}
/**
 * The receiver has been resized. Resize the fill column 
 * and the header widget.
 */
void resize(Event event) {
	TableColumn fillColumn = getFillColumn();
	Rectangle fillColumnBounds;

	super.resize(event);
	// the x position may change in super.resize.
	// get the column bounds after calling super.resize. Fixes 1G7ALGG
	fillColumnBounds = fillColumn.getBounds();
	fillColumnBounds.width = Math.max(0, getClientArea().width - getContentWidth());
	fillColumn.setBounds(fillColumnBounds);
	resizeHeader();
}
/**
 * Resize the header widget to occupy the whole width of the
 * receiver.
 */
void resizeHeader() {
	Header tableHeader = getHeader();
	Point size = tableHeader.getSize();

	size.x = Math.max(getContentWidth(), getClientArea().width);
	tableHeader.setSize(size);
}
/**
 * Redraw 'column' after its width has been changed.
 * @param column - column whose width has changed.
 * @param oldColumnWidth - column width before resize
 * @param oldColumnWidth - column width after resize 
 */
void resizeRedraw(TableColumn column, int oldColumnWidth, int newColumnWidth) {
	Rectangle columnBounds = column.getBounds();
	int columnIndex = column.getIndex();
	int oldRedrawStartX[] = getResizeRedrawX(columnIndex, oldColumnWidth);
	int newRedrawStartX[] = getResizeRedrawX(columnIndex, newColumnWidth);
	int itemHeight = getItemHeight();
	int widthChange = newColumnWidth - oldColumnWidth;
	int topIndex = getTopIndex();

	for (int i = 0; i < newRedrawStartX.length; i++) {
		if (newRedrawStartX[i] != oldRedrawStartX[i]) {
			if (widthChange > 0) {
				newRedrawStartX[i] = oldRedrawStartX[i];
			}
			redraw(
				columnBounds.x + newRedrawStartX[i], columnBounds.y + itemHeight * (i + topIndex), 
				columnBounds.width - newRedrawStartX[i], itemHeight, false);
		}
	}
}
/**
 * Scroll horizontally by 'numPixel' pixel.
 * @param numPixel - the number of pixel to scroll
 *	< 0 = columns are going to be moved left.
 *	> 0 = columns are going to be moved right.
 */
void scrollHorizontal(int numPixel) {
	Rectangle clientArea = getClientArea();	

	scroll(
		numPixel, 0, 								// destination x, y
		0, 0, 										// source x, y
		clientArea.width, clientArea.height, true);
	getHeader().scroll(
		numPixel, 0, 								// destination x, y
		0, 0, 										// source x, y
		clientArea.width, clientArea.height, true);
	moveColumns(TableColumn.FILL, numPixel);
}
/**
 * Scroll vertically by 'scrollIndexCount' items.
 * @param scrollIndexCount - the number of items to scroll.
 *	scrollIndexCount > 0 = scroll up. scrollIndexCount < 0 = scroll down
 */
void scrollVertical(int scrollIndexCount) {
	int scrollAmount = scrollIndexCount * getItemHeight();
	int headerHeight = getHeaderHeight();
	int destY;
	int sourceY;
	boolean scrollUp = scrollIndexCount < 0;
	Rectangle clientArea = getClientArea();

	if (scrollIndexCount == 0) {
		return;
	}
	if (scrollUp == true) {
		destY = headerHeight - scrollAmount;
		sourceY = headerHeight;
	}
	else {
		destY = headerHeight;
		sourceY = destY + scrollAmount;
	}
	scroll(
		0, destY, 									// destination x, y
		0, sourceY,									// source x, y
		clientArea.width, clientArea.height, true);
}
/**
 * Scroll items down to make space for a new item added to 
 * the receiver at position 'index'.
 * @param index - position at which space for one new item
 *	should be made. This index is relative to the first item 
 *	of the receiver.
 */
void scrollVerticalAddingItem(int index) {
	int itemHeight = getItemHeight();
	int sourceY = getHeaderHeight();
	Rectangle clientArea = getClientArea();	

	if (index >= getTopIndex()) {
		sourceY += (index-getTopIndex()) * itemHeight;
	}
	scroll(
		0, sourceY + itemHeight, 				// destination x, y
		0, sourceY,								// source x, y
		clientArea.width, clientArea.height, true);
}
/**
 * Scroll the items below the item at position 'index' up 
 * so that they cover the removed item.
 * @param index - index of the removed item
 */
void scrollVerticalRemovedItem(int index) {
	int itemHeight = getItemHeight();
	int headerHeight = getHeaderHeight();
	int destY;
	Rectangle clientArea = getClientArea();		

	destY = Math.max(headerHeight, headerHeight + (index - getTopIndex()) * itemHeight);
	scroll(
		0, destY, 								// destination x, y
		0, destY + itemHeight,					// source x, y
		clientArea.width, clientArea.height, true);
}
/**
 * Select the items identified by the indices stored in 
 * 'indices'.
 * A SWT.Selection event will not be sent.
 * @param indices - indices of the items to select
 */
public void select(int indices[]) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	SelectableItem item = null;
	int selectionCount;

	if (indices == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	selectionCount = indices.length;
	if (isMultiSelect() == false && selectionCount > 1) {
		selectionCount = 1;
		deselectAllExcept(getVisibleItem(indices[0]));
	}
	for (int i = selectionCount - 1; i >= 0; --i) {
		item = getVisibleItem(indices[i]);
		if (item != null) {
			select(item);
		}
	}
	if (item != null) {
		setLastSelection(item, false);
	}
}
/**
 * Select the item identified by 'index'.
 * A SWT.Selection event will not be sent.
 * @param index - index of the item to select
 */
public void select(int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	SelectableItem item = getVisibleItem(index);
	
	if (isMultiSelect() == false) {
		deselectAllExcept(getVisibleItem(index));
	}
	if (item != null) {
		select(item);
		setLastSelection(item, false);
	}
}
/**
 * Select a range of items starting at index 'start' and 
 * stopping at index 'end'. Indices that are out of range 
 * are ignored. Indexing is zero based.
 * @param start - the start of the range
 * @param end - the end of the range
 */
public void select(int start, int end) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	SelectableItem item = null;
	int selectionCount = 1;
	
	if (isMultiSelect() == false) {
		if (start < 0 && end >= 0) {
			start = 0;
		}
		end = start;
		deselectAllExcept(getVisibleItem(end));
	}
	// select in the same order as all the other selection and deslection methods.
	// Otherwise setLastSelection repeatedly changes the lastSelectedItem for repeated 
	// selections of the items, causing flash.
	for (int i = end; i >= start; i--) {
		item = getVisibleItem(i);
		if (item != null) {
			select(item);
		}
	}
	if (item != null) {
		setLastSelection(item, false);
	}
}
/**
 * Select all items of the receiver if it is in multiple 
 * selection mode.
 * A SWT.Selection event will not be sent.
 * Do nothing if the receiver is in single selection mode.
 */
public void selectAll() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Enumeration items = getItemVector().elements();
	TableItem item = null;

	if (isMultiSelect() == false) {
		return;
	}
	while (items.hasMoreElements() == true) {
		item = (TableItem) items.nextElement();
		select(item);
	}
	if (item != null) {
		setLastSelection(item, false);
	}
}
/**
 * Set the y position of 'column'.
 * @param column - the TableColumn that should be set to 
 *	a new y position.
 */
void setColumnPosition(TableColumn column) {
	Rectangle bounds = column.getBounds();

	bounds.y = getHeaderHeight() - getTopIndex() * getItemHeight();
	column.setBounds(bounds);	
}
/**
 * Change the cursor of the receiver.
 * @param isColumnResizeCursor - indicates whether the column 
 *	resize cursor or the regular cursor should be set.
 */
void setColumnResizeCursor(boolean isColumnResizeCursor) {
	if (isColumnResizeCursor != this.isColumnResizeCursor) {
		this.isColumnResizeCursor = isColumnResizeCursor;
		if (isColumnResizeCursor == true) {
			setCursor(getColumnResizeCursor());
		}
		else {
			setCursor(null);
		}
	}
}
/**
 * Set the current position of the resized column to 'xPosition'.
 * @param xPosition - the current position of the resized column
 */
void setColumnResizeX(int xPosition) {
	columnResizeX = xPosition;
}
/**
 * Set the width of the receiver's contents to 'newWidth'.
 * Content width is used to calculate the horizontal scrollbar.
 */
void setContentWidth(int newWidth) {
	TableColumn fillColumn = getFillColumn();
	Rectangle fillColumnBounds;
	int widthDiff = newWidth - getContentWidth();

	super.setContentWidth(newWidth);
	if (fillColumn != null) {
		fillColumnBounds = fillColumn.getBounds();
		fillColumnBounds.x += widthDiff;
		fillColumnBounds.width = Math.max(0, getClientArea().width - newWidth);
		fillColumn.setBounds(fillColumnBounds);
	}
}
/**
 * Set the width of the first column to fit 'item' if it is longer than 
 * the current column width.
 * Do nothing if the user has already set a width.
 */
void setFirstColumnWidth(TableItem item) {
	int newWidth;
	TableColumn column;

	if (internalGetColumnCount() > 0) {
		column = internalGetColumn(TableColumn.FIRST);		
		if (column.isDefaultWidth() == true) {
			newWidth = Math.max(column.getWidth(), item.getPreferredWidth(TableColumn.FIRST));
			column.setWidth(newWidth);
			column.setDefaultWidth(true);					// reset to true so that we know when the user has set 
															// the width instead of us setting a default width.
		}
	}
}
/**
 * The font is changing. Trigger a recalculation of the item 
 * height using all items of the receiver.
 */
public void setFont(Font font) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	SelectableItem item;
	int itemCount = getItemCount();

	if (font == null || font.equals(getFont()) == true) {
		return;
	}
	setRedraw(false);						// disable redraw because itemChanged() triggers undesired redraw	
	resetItemData();	
	super.setFont(font);
	for (int i = 0; i < itemCount; i++) {
		itemChanged(getItem(i), 0, getClientArea().width);
	}
	setRedraw(true);						// re-enable redraw
	getHeader().setFont(font);
}
/**
 * Set whether or not the header is visible.
 * @param headerVisible - 
 *	true = header is visible. false = header is not visible
 */
public void setHeaderVisible(boolean headerVisible) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	if (headerVisible != getHeaderVisible()) {
		getHeader().setLocation(0, 0);
		getHeader().setVisible(headerVisible);
		// Windows resets scrolling so do we
		setTopIndex(0, true);
		moveColumnsVertical();
		resizeVerticalScrollbar();
		redraw();
	}
}
/**
 * Set the vector that stores the items of the receiver 
 * to 'newVector'.
 * @param newVector - Vector to use for storing the items of 
 *	the receiver.
 */
void setItemVector(Vector newVector) {
	items = newVector;
}
/**
 * Set whether the receiver is drawing grid lines to 
 * 'drawGridLines'.
 * @param drawGridLines - 
 *	true = receiver draws grid lines
 *	false = receiver does not draw grid lines
 */
public void setLinesVisible(boolean drawGridLines) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	if (this.drawGridLines != drawGridLines) {
		this.drawGridLines = drawGridLines;
		redraw();
	}
}
/**
 * Set whether the receiver and it's children should be
 * drawn or not.
 * @param redraw -
 *	true = redraw the receiver and its children
 *	false = do not draw the receiver or its children
 */
public void setRedraw(boolean redraw) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	super.setRedraw(redraw);
	getHeader().setRedraw(redraw);
}
/**
 * Set the column that is being resized to 'column'. 
 * @param column - the TableColumn that is being resized. 
 * 	A null value indicates that no column resize operation is 
 *	in progress.
 */
void setResizeColumn(TableColumn column) {
	resizeColumn = column;
}
/**
* Sets the selection.
* <p>
* The previous selection is cleared
* before new items are selected.
*
* @see Table#deselectAll()
* @see Table#select(int [])
*
* @param indices the indices of the items
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setSelection(int [] indices) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Vector keepSelected;
	
	if (indices == null)  {
		error(SWT.ERROR_NULL_ARGUMENT);
	}	
	keepSelected = new Vector(indices.length);
	for (int i = 0; i < indices.length; i++) {
		SelectableItem item = getVisibleItem(indices[i]);
		if (item != null) {
			keepSelected.addElement(item);
		}
	}
	deselectAllExcept(keepSelected);
	select(indices);
}
/**
 * Select the items stored in 'items'. 
 * A SWT.Selection event is not going to be sent.
 * @param selectionItems - Array containing the items that should 
 *	be selected
 */
public void setSelection(TableItem selectionItems[]) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	if (selectionItems == null)  {
		error(SWT.ERROR_NULL_ARGUMENT);
	}	
	setSelectableSelection(selectionItems);
}
/**
 * Set the selection to the item identified by 'index'.
 * SWT.Selection events are not going to be sent.
 * @param index - index of the item that should be selected
 */
public void setSelection(int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	deselectAllExcept(getVisibleItem(index));
	select(index);
}
/**
 * Set the selection to a range of items starting at index 
 * 'start' and stopping at index 'end'. Indices that are out 
 * of range are ignored. Indexing is zero based.
 * @param start - the start of the range
 * @param end - the end of the range
 */
public void setSelection(int start, int end) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Vector keepSelected = new Vector(end - start + 1);
	
	for (int i = start; i <= end; i++) {
		SelectableItem item = getVisibleItem(i);
		if (item != null) {
			keepSelected.addElement(item);
		}
	}	
	deselectAllExcept(keepSelected);
	select(start, end);
}
/**
 * Scroll the item at position 'index' to the top of the receiver.
 * If the scroll operation would result in empty space being 
 * displayed below the last item of the receiver, the last item is 
 * scrolled into view. This results in the specified item not being 
 * displayed at the top of the receiver but after the top item.
 * @param index - 0-based index of the item that should be 
 *	displayed at the top of the receiver's client area.
 */
public void setTopIndex(int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int itemCount = getItemCount();
	int itemCountWhole = getItemCountWhole();
	
	if (index < 0 || itemCount == 0) {
		return;
	}
	if (index >= itemCount) {
		index = itemCount - itemCountWhole;
	}
	super.setTopIndex(index, true);
}
/**
 * Set the index of the first visible item in the receiver's client 
 * area to 'index'.
 * @param index - 0-based index of the first visible item in the 
 *	receiver's client area.
 * @param adjustScrollbar - true=set the position of the vertical 
 *	scroll bar to the new top index. 
 *	false=don't adjust the vertical scroll bar
 */
void setTopIndexNoScroll(int index, boolean adjustScrollbar) {
	super.setTopIndexNoScroll(index, adjustScrollbar);
	moveColumnsVertical();
}
/**
 * Make 'item' visible by scrolling it into the receiver's 
 * client area if necessary.
 * @param item - the item that should be made visible to the
 *	user.
 */
public void showItem(TableItem item) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);

	if (item == null)  {
		error(SWT.ERROR_NULL_ARGUMENT);
	}	
	showSelectableItem(item);
}
/**
 * Show the selection. If there is no selection or the 
 * selection is already visible, this method does nothing. 
 * If the selection is not visible, the top index of the 
 * widget is changed such that the selection becomes visible.
 */
public void showSelection() {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	
	super.showSelection();
}
void sort (int [] items) {
	/* Shell Sort from K&R, pg 108 */
	int length = items.length;
	for (int gap=length/2; gap>0; gap/=2) {
		for (int i=gap; i<length; i++) {
			for (int j=i-gap; j>=0; j-=gap) {
		   		if (items [j] <= items [j + gap]) {
					int swap = items [j];
					items [j] = items [j + gap];
					items [j + gap] = swap;
		   		}
	    	}
	    }
	}
}
/**
 * Start a column resize operation.
 * @param event - the mouse event that occured over the header 
 * 	widget
 */
void startColumnResize(Event event) {
	Vector columns = internalGetColumnVector();
	TableColumn hitColumn = getColumnAtX(event.x);
	Rectangle hitColumnBounds;
	int hitIndex = hitColumn.getIndex();

	if (hitColumn == getFillColumn()) {										// clicked on the fill column?
		hitColumn = (TableColumn) columns.lastElement();					// resize the last real column
	}
	else 
	if ((event.x - hitColumn.getBounds().x <= COLUMN_RESIZE_OFFSET) && 		// check if left side of a column was clicked
		(hitIndex > 0)) {													
		hitColumn = (TableColumn) columns.elementAt(hitIndex - 1);			// resize the preceding column
	}
	hitColumnBounds = hitColumn.getBounds();
	setColumnResizeX(hitColumnBounds.x + hitColumnBounds.width);
	setResizeColumn(hitColumn);
}
/**
 * Return 'text' after it has been checked to be no longer than 'maxWidth' 
 * when drawn on 'gc'.
 * If it is too long it will be truncated up to the last character.
 * @param text - the String that should be checked for length
 * @param maxWidth - maximum width of 'text'
 * @param gc - GC to use for String measurement
 */
String trimItemText(String text, int maxWidth, GC gc) {
	int textWidth;
	int dotsWidth;

	if (text != null && text.length() > 1) {
		textWidth = gc.stringExtent(text).x;
		if (textWidth >= maxWidth) {
			dotsWidth = getDotsWidth(gc);
			while (textWidth + dotsWidth >= maxWidth && text.length() > 1) {
				text = text.substring(0, text.length() - 1);		// chop off one character at the end
				textWidth = gc.stringExtent(text).x;
			}
			text = text.concat(Table.DOT_STRING);
		}
	}
	return text;
}

}