/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

/**
 * Instances of this class implement a selectable user interface
 * object that displays a list of images and strings and issues
 * notification when selected.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TableItem</code>.
 * </p><p>
 * Style <code>VIRTUAL</code> is used to create a <code>Table</code> whose
 * <code>TableItem</code>s are to be populated by the client on an on-demand basis
 * instead of up-front.  This can provide significant performance improvements for
 * tables that are very large or for which <code>TableItem</code> population is
 * expensive (for example, retrieving values from an external source).
 * </p><p>
 * Here is an example of using a <code>Table</code> with style <code>VIRTUAL</code>:</p>
 * <pre><code>
 *  final Table table = new Table (parent, SWT.VIRTUAL | SWT.BORDER);
 *  table.setItemCount (1000000);
 *  table.addListener (SWT.SetData, new Listener () {
 *      public void handleEvent (Event event) {
 *          TableItem item = (TableItem) event.item;
 *          int index = table.indexOf (item);
 *          item.setText ("Item " + index);
 *          System.out.println (item.getText ());
 *      }
 *  });
 * </code></pre>
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not normally make sense to add <code>Control</code> children to
 * it, or set a layout on it, unless implementing something like a cell
 * editor.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, HIDE_SELECTION, VIRTUAL, NO_SCROLL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, SetData, MeasureItem, EraseItem, PaintItem</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles SINGLE, and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#table">Table, TableItem, TableColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Table extends Composite {
	TableItem [] items;
	TableColumn [] columns;
	TableColumn sortColumn;
	TableItem currentItem;
	NSTableHeaderView headerView;
	NSTableColumn firstColumn, checkColumn;
	NSTextFieldCell dataCell;
	NSButtonCell buttonCell;
	int columnCount, itemCount, lastIndexOf, sortDirection, selectedRowIndex = -1;
	boolean ignoreSelect, fixScrollWidth, drawExpansion, didSelect, preventSelect, dragDetected;
	Rectangle imageBounds;
	double [] headerBackground, headerForeground;

	/* Used to control drop feedback when FEEDBACK_SCROLL is set/not set */
	boolean shouldScroll = true;
	boolean keyDown;

	static int NEXT_ID;

	static final int FIRST_COLUMN_MINIMUM_WIDTH = 5;
	static final int IMAGE_GAP = 3;
	static final int TEXT_GAP = 2;
	static final int CELL_GAP = 1;

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
 * @see SWT#SINGLE
 * @see SWT#MULTI
 * @see SWT#CHECK
 * @see SWT#FULL_SELECTION
 * @see SWT#HIDE_SELECTION
 * @see SWT#VIRTUAL
 * @see SWT#NO_SCROLL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Table (Composite parent, int style) {
	super (parent, checkStyle (style));
}

@Override
boolean acceptsFirstResponder (long id, long sel) {
	return true;
}

@Override
long accessibilityAttributeValue(long id, long sel, long arg0) {
	long returnValue = 0;
	NSString attributeName = new NSString(arg0);

	// If the check column is visible, don't report it back as a column for accessibility purposes.
	// The check column is meant to appear as a part of the first column.
	if (attributeName.isEqualToString (OS.NSAccessibilityColumnsAttribute) || attributeName.isEqualToString(OS.NSAccessibilityVisibleColumnsAttribute)) {
		if ((style & SWT.CHECK) != 0) {
			long superValue = super.accessibilityAttributeValue(id, sel, arg0);
			if (superValue != 0) {
				NSArray columns = new NSArray(superValue);
				NSMutableArray columnsWithoutCheck = NSMutableArray.arrayWithCapacity(columns.count() - 1);
				columnsWithoutCheck.addObjectsFromArray(columns);
				columnsWithoutCheck.removeObjectAtIndex(0);
				returnValue = columnsWithoutCheck.id;
			}
		}
	}

	if (returnValue != 0) {
		return returnValue;
	} else {
		return super.accessibilityAttributeValue(id, sel, arg0);
	}
}

@Override
void _addListener (int eventType, Listener listener) {
	super._addListener (eventType, listener);
	clearCachedWidth(items);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the user changes the receiver's selection, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * If the receiver has the <code>SWT.CHECK</code> style and the check selection changes,
 * the event object detail field contains the value <code>SWT.CHECK</code>.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
 * The item field of the event object is valid for default selection, but the detail field is not used.
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
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}

TableItem _getItem (int index) {
	if ((style & SWT.VIRTUAL) == 0) return items [index];
	if (items [index] != null) return items [index];
	return items [index] = new TableItem (this, SWT.NULL, -1, false);
}

int calculateWidth (TableItem[] items, int index, GC gc) {
	int width = 0;
	for (int i=0; i < itemCount; i++) {
		TableItem item = items [i];
		if (item != null && item.cached) {
			width = Math.max (width, item.calculateWidth (index, gc, isSelected(index)));
		}
	}
	return width;
}

@Override
NSSize cellSize (long id, long sel) {
	NSSize size = super.cellSize(id, sel);
	NSCell cell = new NSCell(id);
	NSImage image = cell.image();
	if (image != null) size.width += imageBounds.width + IMAGE_GAP;
	if (hooks(SWT.MeasureItem)) {
		long [] outValue = new long [1];
		OS.object_getInstanceVariable(id, Display.SWT_ROW, outValue);
		long rowIndex = outValue [0];
		TableItem item = _getItem((int)rowIndex);
		OS.object_getInstanceVariable(id, Display.SWT_COLUMN, outValue);
		long tableColumn = outValue[0];
		int columnIndex = 0;
		for (int i=0; i<columnCount; i++) {
			if (columns [i].nsColumn.id == tableColumn) {
				columnIndex = i;
				break;
			}
		}
		sendMeasureItem (item, columnIndex, size, cell.isHighlighted());
	}
	return size;
}

@Override
boolean canDragRowsWithIndexes_atPoint(long id, long sel, long rowIndexes, NSPoint mouseDownPoint) {
	if (!super.canDragRowsWithIndexes_atPoint(id, sel, rowIndexes, mouseDownPoint)) return false;

	// If the current row is not selected and the user is not attempting to modify the selection, select the row first.
	NSTableView widget = (NSTableView)view;
	long row = widget.rowAtPoint(mouseDownPoint);
	long modifiers = NSApplication.sharedApplication().currentEvent().modifierFlags();

	boolean drag = (state & DRAG_DETECT) != 0 && hooks (SWT.DragDetect);
	if (drag) {
		if (!widget.isRowSelected(row) && (modifiers & (OS.NSCommandKeyMask | OS.NSShiftKeyMask | OS.NSAlternateKeyMask)) == 0) {
			NSIndexSet set = (NSIndexSet)new NSIndexSet().alloc();
			set = set.initWithIndex(row);
			widget.selectRowIndexes (set, false);
			set.release();
		}
	}

	// The clicked row must be selected to initiate a drag.
	return (widget.isRowSelected(row) && drag) || !hasFocus();
}

boolean checkData (TableItem item) {
	return checkData (item, indexOf (item));
}

boolean checkData (TableItem item, int index) {
	if (item.cached) return true;
	if ((style & SWT.VIRTUAL) != 0) {
		item.cached = true;
		Event event = new Event ();
		event.item = item;
		event.index = indexOf (item);
		currentItem = item;
		sendEvent (SWT.SetData, event);
		//widget could be disposed at this point
		currentItem = null;
		if (isDisposed () || item.isDisposed ()) return false;
		if (!setScrollWidth (item)) item.redraw (-1);
	}
	return true;
}

static int checkStyle (int style) {
	/*
	* Feature in Windows.  Even when WS_HSCROLL or
	* WS_VSCROLL is not specified, Windows creates
	* trees and tables with scroll bars.  The fix
	* is to set H_SCROLL and V_SCROLL.
	*
	* NOTE: This code appears on all platforms so that
	* applications have consistent scroll bar behavior.
	*/
	if ((style & SWT.NO_SCROLL) == 0) {
		style |= SWT.H_SCROLL | SWT.V_SCROLL;
	}
	/* This platform is always FULL_SELECTION */
	style |= SWT.FULL_SELECTION;
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

/**
 * Clears the item at the given zero-relative index in the receiver.
 * The text, icon and other attributes of the item are set to the default
 * value.  If the table was created with the <code>SWT.VIRTUAL</code> style,
 * these attributes are requested again as needed.
 *
 * @param index the index of the item to clear
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 *
 * @since 3.0
 */
public void clear (int index) {
	checkWidget ();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	TableItem item = items [index];
	if (item != null) {
		if (currentItem != item) item.clear ();
		if (currentItem == null) item.redraw (-1);
		setScrollWidth (item);
	}
}
/**
 * Removes the items from the receiver which are between the given
 * zero-relative start and end indices (inclusive).  The text, icon
 * and other attributes of the items are set to their default values.
 * If the table was created with the <code>SWT.VIRTUAL</code> style,
 * these attributes are requested again as needed.
 *
 * @param start the start index of the item to clear
 * @param end the end index of the item to clear
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if either the start or end are not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 *
 * @since 3.0
 */
public void clear (int start, int end) {
	checkWidget ();
	if (start > end) return;
	if (!(0 <= start && start <= end && end < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (start == 0 && end == itemCount - 1) {
		clearAll ();
	} else {
		for (int i=start; i<=end; i++) {
			clear (i);
		}
	}
}

/**
 * Clears the items at the given zero-relative indices in the receiver.
 * The text, icon and other attributes of the items are set to their default
 * values.  If the table was created with the <code>SWT.VIRTUAL</code> style,
 * these attributes are requested again as needed.
 *
 * @param indices the array of indices of the items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 *    <li>ERROR_NULL_ARGUMENT - if the indices array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 *
 * @since 3.0
 */
public void clear (int [] indices) {
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (indices.length == 0) return;
	for (int i=0; i<indices.length; i++) {
		if (!(0 <= indices [i] && indices [i] < itemCount)) {
			error (SWT.ERROR_INVALID_RANGE);
		}
	}
	for (int i=0; i<indices.length; i++) {
		clear (indices [i]);
	}
}

/**
 * Clears all the items in the receiver. The text, icon and other
 * attributes of the items are set to their default values. If the
 * table was created with the <code>SWT.VIRTUAL</code> style, these
 * attributes are requested again as needed.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 *
 * @since 3.0
 */
public void clearAll () {
	checkWidget ();
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
		if (item != null) {
			item.clear ();
		}
	}
	if (currentItem == null && isDrawing ()) view.setNeedsDisplay(true);
	setScrollWidth (items, true);
}

void clearCachedWidth (TableItem[] items) {
	if (items == null) return;
	for (int i = 0; i < items.length; i++) {
		if (items [i] != null) items [i].width = -1;
	}
}

@Override
long columnAtPoint(long id, long sel, NSPoint point) {
	if ((style & SWT.CHECK) != 0) {
		if (point.x <= getCheckColumnWidth() && point.y < headerView.frame().height) return 1;
	}

	return super.columnAtPoint(id, sel, point);
}

@Override
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0;
	if (wHint == SWT.DEFAULT) {
		if (columnCount != 0) {
			for (int i=0; i<columnCount; i++) {
				width += columns [i].getWidth ();
			}
		} else {
			GC gc = new GC (this);
			width += calculateWidth (items, 0, gc) + CELL_GAP;
			gc.dispose ();
		}
		if ((style & SWT.CHECK) != 0) width += getCheckColumnWidth ();
	} else {
		width = wHint;
	}
	if (width <= 0) width = DEFAULT_WIDTH;
	int height = 0;
	if (hHint == SWT.DEFAULT) {
		height = itemCount * getItemHeight () + getHeaderHeight();
	} else {
		height = hHint;
	}
	if (height <= 0) height = DEFAULT_HEIGHT;
	Rectangle rect = computeTrim (0, 0, width, height);
	return new Point (rect.width, rect.height);
}

void createColumn (TableItem item, int index) {
	String [] strings = item.strings;
	if (strings != null) {
		String [] temp = new String [columnCount];
		System.arraycopy (strings, 0, temp, 0, index);
		System.arraycopy (strings, index, temp, index+1, columnCount-index-1);
		temp [index] = "";
		item.strings = temp;
	}
	if (index == 0) item.text = "";
	Image [] images = item.images;
	if (images != null) {
		Image [] temp = new Image [columnCount];
		System.arraycopy (images, 0, temp, 0, index);
		System.arraycopy (images, index, temp, index+1, columnCount-index-1);
		item.images = temp;
	}
	if (index == 0) item.image = null;
	Color [] cellBackground = item.cellBackground;
	if (cellBackground != null) {
		Color [] temp = new Color [columnCount];
		System.arraycopy (cellBackground, 0, temp, 0, index);
		System.arraycopy (cellBackground, index, temp, index+1, columnCount-index-1);
		item.cellBackground = temp;
	}
	Color [] cellForeground = item.cellForeground;
	if (cellForeground != null) {
		Color [] temp = new Color [columnCount];
		System.arraycopy (cellForeground, 0, temp, 0, index);
		System.arraycopy (cellForeground, index, temp, index+1, columnCount-index-1);
		item.cellForeground = temp;
	}
	Font [] cellFont = item.cellFont;
	if (cellFont != null) {
		Font [] temp = new Font [columnCount];
		System.arraycopy (cellFont, 0, temp, 0, index);
		System.arraycopy (cellFont, index, temp, index+1, columnCount-index-1);
		item.cellFont = temp;
	}
}

@Override
void createHandle () {
	NSScrollView scrollWidget = (NSScrollView)new SWTScrollView().alloc();
	scrollWidget.init();
	scrollWidget.setHasHorizontalScroller ((style & SWT.H_SCROLL) != 0);
	scrollWidget.setHasVerticalScroller ((style & SWT.V_SCROLL) != 0);
	scrollWidget.setAutohidesScrollers(true);
	scrollWidget.setBorderType(hasBorder() ? OS.NSBezelBorder : OS.NSNoBorder);

	NSTableView widget = (NSTableView)new SWTTableView().alloc();
	widget.init();
	widget.setAllowsMultipleSelection((style & SWT.MULTI) != 0);
	widget.setDataSource(widget);
	widget.setDelegate(widget);
	widget.setColumnAutoresizingStyle (OS.NSTableViewNoColumnAutoresizing);
	NSSize spacing = new NSSize();
	spacing.width = spacing.height = CELL_GAP;
	widget.setIntercellSpacing(spacing);
	widget.setDoubleAction(OS.sel_sendDoubleSelection);

	if (OS.isBigSurOrLater()) {
		OS.objc_msgSend(widget.id, OS.sel_setStyle, OS.NSTableViewStylePlain);
	}

	/*
	 * Table didn't have focus ring in SWT for years, because SWT didn't
	 * have layer backing, and even when it gets it, there were no focus rings
	 * due to macOS compatibility code. However, sometimes macOS misbehaves
	 * (see for example Bug 574618 snippet test 2). On the other hand, most
	 * macOS builtin apps don't have focus rings for Table, see for example
	 * Activity Monitor. So just disable it in SWT as well.
	 */
	widget.setFocusRingType(OS.NSFocusRingTypeNone);

	headerView = (NSTableHeaderView)new SWTTableHeaderView ().alloc ().init ();
	widget.setHeaderView (null);

	NSString str = NSString.string();
	if ((style & SWT.CHECK) != 0) {
		checkColumn = (NSTableColumn)new NSTableColumn().alloc();
		NSString nsstring = (NSString) new NSString().alloc();
		nsstring = nsstring.initWithString(String.valueOf(++NEXT_ID));
		checkColumn = checkColumn.initWithIdentifier(nsstring);
		nsstring.release();
		checkColumn.headerCell().setTitle(str);
		widget.addTableColumn (checkColumn);
		checkColumn.setResizingMask(OS.NSTableColumnNoResizing);
		checkColumn.setEditable(false);
		long cls = NSButton.cellClass (); /* use our custom cell class */
		buttonCell = new NSButtonCell (OS.class_createInstance (cls, 0));
		buttonCell.init ();
		checkColumn.setDataCell (buttonCell);
		buttonCell.setButtonType (OS.NSSwitchButton);
		buttonCell.setControlSize(OS.NSSmallControlSize);
		buttonCell.setImagePosition (OS.NSImageOnly);
		buttonCell.setAllowsMixedState (true);
		checkColumn.setWidth(getCheckColumnWidth());
	}

	firstColumn = (NSTableColumn)new NSTableColumn().alloc();
	NSString nsstring = (NSString) new NSString().alloc();
	nsstring = nsstring.initWithString(String.valueOf(++NEXT_ID));
	firstColumn = firstColumn.initWithIdentifier(nsstring);
	nsstring.release();
	/*
	* Feature in Cocoa.  If a column's width is too small to show any content
	* then tableView_objectValueForTableColumn_row is never invoked to
	* query for item values, which is a problem for VIRTUAL Tables.  The
	* workaround is to ensure that, for 0-column Tables, the internal first
	* column always has a minimal width that makes this call come in.
	*/
	firstColumn.setMinWidth (FIRST_COLUMN_MINIMUM_WIDTH);
	firstColumn.setWidth(0);
	firstColumn.setResizingMask (OS.NSTableColumnNoResizing);
	firstColumn.headerCell ().setTitle (str);
	widget.addTableColumn (firstColumn);
	dataCell = (NSTextFieldCell)new SWTImageTextCell ().alloc ().init ();
	dataCell.setLineBreakMode(OS.NSLineBreakByClipping);
	firstColumn.setDataCell (dataCell);
	widget.setHighlightedTableColumn(null);
	scrollView = scrollWidget;
	view = widget;
}

void createItem (TableColumn column, int index) {
	if (!(0 <= index && index <= columnCount)) error (SWT.ERROR_INVALID_RANGE);
	if (columnCount == columns.length) {
		TableColumn [] newColumns = new TableColumn [columnCount + 4];
		System.arraycopy (columns, 0, newColumns, 0, columns.length);
		columns = newColumns;
	}
	NSTableColumn nsColumn;
	if (columnCount == 0) {
		//TODO - clear attributes, alignment etc.
		nsColumn = firstColumn;
		nsColumn.setMinWidth (0);
		nsColumn.setResizingMask (OS.NSTableColumnUserResizingMask);
		firstColumn = null;
	} else {
		//TODO - set attributes, alignment etc.
		nsColumn = (NSTableColumn)new NSTableColumn().alloc();
		NSString nsstring = (NSString) new NSString().alloc();
		nsstring = nsstring.initWithString(String.valueOf(++NEXT_ID));
		nsColumn = nsColumn.initWithIdentifier(nsstring);
		nsstring.release();
		nsColumn.setMinWidth(0);
		((NSTableView)view).addTableColumn (nsColumn);
		int checkColumn = (style & SWT.CHECK) != 0 ? 1 : 0;
		((NSTableView)view).moveColumn (columnCount + checkColumn, index + checkColumn);
		nsColumn.setDataCell (dataCell);
	}
	column.createJNIRef ();
	NSTableHeaderCell headerCell = (NSTableHeaderCell)new SWTTableHeaderCell ().alloc ().init ();
	if (font != null) headerCell.setFont(font.handle);
	nsColumn.setHeaderCell (headerCell);
	display.addWidget (headerCell, column);
	column.nsColumn = nsColumn;
	nsColumn.setWidth(0);
	System.arraycopy (columns, index, columns, index + 1, columnCount++ - index);
	columns [index] = column;
	for (int i = 0; i < itemCount; i++) {
		TableItem item = items [i];
		if (item != null) {
			if (columnCount > 1) {
				createColumn (item, index);
			}
		}
	}
}

void createItem (TableItem item, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	if (itemCount == items.length) {
		/* Grow the array faster when redraw is off */
		int length = getDrawing () ? items.length + 4 : Math.max (4, items.length * 3 / 2);
		TableItem [] newItems = new TableItem [length];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
	updateRowCount();
	if (index != itemCount) fixSelection (index, true);
}

@Override
void createWidget () {
	super.createWidget ();
	items = new TableItem [4];
	columns = new TableColumn [4];
}

@Override
Color defaultBackground () {
	return display.getWidgetColor (SWT.COLOR_LIST_BACKGROUND);
}

@Override
NSFont defaultNSFont () {
	return display.tableViewFont;
}

@Override
Color defaultForeground () {
	return display.getWidgetColor (SWT.COLOR_LIST_FOREGROUND);
}

@Override
void deregister () {
	super.deregister ();
	display.removeWidget (headerView);
	display.removeWidget (dataCell);
	if (buttonCell != null) display.removeWidget (buttonCell);
}

@Override
void deselectAll(long id, long sel, long sender) {
	if (preventSelect && !ignoreSelect) return;
	if ((style & SWT.SINGLE) != 0 && !ignoreSelect) {
		if ( ((NSTableView)view).selectedRow() != -1) return;
	}
	super.deselectAll (id, sel, sender);
}

@Override
void deselectRow (long id, long sel, long index) {
	if (preventSelect && !ignoreSelect) return;
	if ((style & SWT.SINGLE) != 0 && !ignoreSelect) {
		if ( ((NSTableView)view).selectedRow() == index) return;
	}
	super.deselectRow (id, sel, index);
}

/**
 * Deselects the item at the given zero-relative index in the receiver.
 * If the item at the index was already deselected, it remains
 * deselected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to deselect
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int index) {
	checkWidget ();
	if (0 <= index && index < itemCount) {
		NSTableView widget = (NSTableView)view;
		ignoreSelect = true;
		widget.deselectRow (index);
		ignoreSelect = false;
	}
}

/**
 * Deselects the items at the given zero-relative indices in the receiver.
 * If the item at the given zero-relative index in the receiver
 * is selected, it is deselected.  If the item at the index
 * was not selected, it remains deselected.  The range of the
 * indices is inclusive. Indices that are out of range are ignored.
 *
 * @param start the start index of the items to deselect
 * @param end the end index of the items to deselect
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int start, int end) {
	checkWidget();
	if (start > end) return;
	if (end < 0 || start >= itemCount) return;
	start = Math.max (0, start);
	end = Math.min (itemCount - 1, end);
	if (start == 0 && end == itemCount - 1) {
		deselectAll ();
	} else {
		NSTableView widget = (NSTableView)view;
		ignoreSelect = true;
		for (int i=start; i<=end; i++) {
			widget.deselectRow (i);
		}
		ignoreSelect = false;
	}
}

/**
 * Deselects the items at the given zero-relative indices in the receiver.
 * If the item at the given zero-relative index in the receiver
 * is selected, it is deselected.  If the item at the index
 * was not selected, it remains deselected. Indices that are out
 * of range and duplicate indices are ignored.
 *
 * @param indices the array of indices for the items to deselect
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the set of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int [] indices) {
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	NSTableView widget = (NSTableView)view;
	ignoreSelect = true;
	for (int i=0; i<indices.length; i++) {
		widget.deselectRow (indices [i]);
	}
	ignoreSelect = false;
}

/**
 * Deselects all selected items in the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselectAll () {
	checkWidget ();
	NSTableView widget = (NSTableView)view;
	ignoreSelect = true;
	widget.deselectAll(null);
	ignoreSelect = false;
}

void destroyItem (TableColumn column) {
	int index = 0;
	while (index < columnCount) {
		if (columns [index] == column) break;
		index++;
	}
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
		if (item != null) {
			if (columnCount <= 1) {
				item.strings = null;
				item.images = null;
				item.cellBackground = null;
				item.cellForeground = null;
				item.cellFont = null;
			} else {
				if (item.strings != null) {
					String [] strings = item.strings;
					if (index == 0) {
						item.text = strings [1] != null ? strings [1] : "";
					}
					String [] temp = new String [columnCount - 1];
					System.arraycopy (strings, 0, temp, 0, index);
					System.arraycopy (strings, index + 1, temp, index, columnCount - 1 - index);
					item.strings = temp;
				} else {
					if (index == 0) item.text = "";
				}
				if (item.images != null) {
					Image [] images = item.images;
					if (index == 0) item.image = images [1];
					Image [] temp = new Image [columnCount - 1];
					System.arraycopy (images, 0, temp, 0, index);
					System.arraycopy (images, index + 1, temp, index, columnCount - 1 - index);
					item.images = temp;
				} else {
					if (index == 0) item.image = null;
				}
				if (item.cellBackground != null) {
					Color [] cellBackground = item.cellBackground;
					Color [] temp = new Color [columnCount - 1];
					System.arraycopy (cellBackground, 0, temp, 0, index);
					System.arraycopy (cellBackground, index + 1, temp, index, columnCount - 1 - index);
					item.cellBackground = temp;
				}
				if (item.cellForeground != null) {
					Color [] cellForeground = item.cellForeground;
					Color [] temp = new Color [columnCount - 1];
					System.arraycopy (cellForeground, 0, temp, 0, index);
					System.arraycopy (cellForeground, index + 1, temp, index, columnCount - 1 - index);
					item.cellForeground = temp;
				}
				if (item.cellFont != null) {
					Font [] cellFont = item.cellFont;
					Font [] temp = new Font [columnCount - 1];
					System.arraycopy (cellFont, 0, temp, 0, index);
					System.arraycopy (cellFont, index + 1, temp, index, columnCount - 1 - index);
					item.cellFont = temp;
				}
			}
		}
	}

	int oldIndex = indexOf (column.nsColumn);

	System.arraycopy (columns, index + 1, columns, index, --columnCount - index);
	columns [columnCount] = null;
	if (columnCount == 0) {
		//TODO - reset attributes
		firstColumn = column.nsColumn;
		firstColumn.retain ();
		/*
		* Feature in Cocoa.  If a column's width is too small to show any content
		* then tableView_objectValueForTableColumn_row is never invoked to
		* query for item values, which is a problem for VIRTUAL Tables.  The
		* workaround is to ensure that, for 0-column Tables, the internal first
		* column always has a minimal width that makes this call come in.
		*/
		firstColumn.setMinWidth (FIRST_COLUMN_MINIMUM_WIDTH);
		firstColumn.setResizingMask (OS.NSTableColumnNoResizing);
		setScrollWidth ();
	} else {
		((NSTableView)view).removeTableColumn(column.nsColumn);
	}

	NSArray array = ((NSTableView)view).tableColumns ();
	int arraySize = (int)array.count ();
	for (int i = oldIndex; i < arraySize; i++) {
		long columnId = array.objectAtIndex (i).id;
		for (int j = 0; j < columnCount; j++) {
			if (columns[j].nsColumn.id == columnId) {
				columns [j].sendEvent (SWT.Move);
				break;
			}
		}
	}
}

void destroyItem (TableItem item) {
	int index = 0;
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	if (index != itemCount - 1) fixSelection (index, false);
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	updateRowCount();
	if (itemCount == 0) setTableEmpty ();
}

@Override
boolean dragDetect(int x, int y, boolean filter, boolean[] consume) {
	// Let Cocoa determine if a drag is starting and fire the notification when we get the callback.
	return false;
}

@Override
void drawBackgroundInClipRect(long id, long sel, NSRect rect) {
	super.drawViewBackgroundInRect(id, sel, rect);
	if (id != view.id) return;
	fillBackground (view, NSGraphicsContext.currentContext(), rect, -1);
}

@Override
void drawInteriorWithFrame_inView (long id, long sel, NSRect rect, long view) {
	boolean hooksErase = hooks (SWT.EraseItem);
	boolean hooksPaint = hooks (SWT.PaintItem);
	boolean hooksMeasure = hooks (SWT.MeasureItem);

	NSTextFieldCell cell = new NSTextFieldCell (id);

	NSTableView widget = (NSTableView)this.view;
	long [] outValue = new long [1];
	OS.object_getInstanceVariable(id, Display.SWT_ROW, outValue);
	long rowIndex = outValue [0];
	TableItem item = _getItem((int)rowIndex);
	OS.object_getInstanceVariable(id, Display.SWT_COLUMN, outValue);
	long tableColumn = outValue[0];
	long nsColumnIndex = widget.tableColumns().indexOfObjectIdenticalTo(new id(tableColumn));
	int columnIndex = 0;
	for (int i=0; i<columnCount; i++) {
		if (columns [i].nsColumn.id == tableColumn) {
			columnIndex = i;
			break;
		}
	}

	Color background = item.cellBackground != null ? item.cellBackground [columnIndex] : null;
	if (background == null) background = item.background;
	boolean drawBackground = background != null;
	boolean drawForeground = true;
	boolean isSelected = cell.isHighlighted();
	boolean drawSelection = isSelected;
	boolean hasFocus = hasFocus ();

	Color selectionBackground = null, selectionForeground = null;
	if (isSelected && (hooksErase || hooksPaint)) {
		selectionForeground = Color.cocoa_new(display, (hasFocus || Display.APPEARANCE.Dark == display.appAppearance) ? display.alternateSelectedControlTextColor : display.selectedControlTextColor);
		selectionBackground = Color.cocoa_new(display, hasFocus ? display.getAlternateSelectedControlColor() : display.getSecondarySelectedControlColor());
	}

	NSSize contentSize = super.cellSize(id, OS.sel_cellSize);
	NSImage image = cell.image();
	if (image != null) contentSize.width += imageBounds.width + IMAGE_GAP;
	int contentWidth = (int)Math.ceil (contentSize.width);
	NSSize spacing = widget.intercellSpacing();
	int itemHeight = (int)Math.ceil (widget.rowHeight() + spacing.height);

	NSRect cellRect = widget.rectOfColumn (nsColumnIndex);
	cellRect.y = rect.y;
	cellRect.height = rect.height + spacing.height;
	if (columnCount == 0) {
		NSRect rowRect = widget.rectOfRow (rowIndex);
		cellRect.width = rowRect.width;
	}
	double offsetX = 0, offsetY = 0;
	if (hooksPaint || hooksErase) {
		NSRect frameCell = widget.frameOfCellAtColumn(nsColumnIndex, rowIndex);
		offsetX = rect.x - frameCell.x;
		offsetY = rect.y - frameCell.y;
		if (drawExpansion) {
			offsetX -= 0.5f;
			offsetY -= 0.5f;
		}
	}
	int itemX = (int)(rect.x - offsetX), itemY = (int)(rect.y - offsetY);
	NSGraphicsContext context = NSGraphicsContext.currentContext ();

	if (hooksMeasure) {
		sendMeasureItem(item, columnIndex, contentSize, isSelected);
	}

	Color userForeground = null;
	if (hooksErase) {
		context.saveGraphicsState();
		NSAffineTransform transform = NSAffineTransform.transform();
		transform.translateXBy(offsetX, offsetY);
		transform.concat();

		GCData data = new GCData ();
		data.paintRect = cellRect;
		GC gc = GC.cocoa_new (this, data);
		gc.setFont (item.getFont (columnIndex));
		Color fg;
		if (isSelected && ((style & SWT.HIDE_SELECTION) == 0 || hasFocus)) {
			fg = selectionForeground;
			gc.setBackground (selectionBackground);
		} else {
			fg = item.getForeground (columnIndex);
			gc.setBackground (item.getBackground (columnIndex));
		}
		gc.setForeground (fg);
		if (!drawExpansion) {
			gc.setClipping ((int)(cellRect.x - offsetX), (int)(cellRect.y - offsetY), (int)cellRect.width, (int)cellRect.height);
		}
		Event event = new Event ();
		event.item = item;
		event.gc = gc;
		event.index = columnIndex;
		event.detail = SWT.FOREGROUND;
		if (drawBackground) event.detail |= SWT.BACKGROUND;
		if (isSelected && ((style & SWT.HIDE_SELECTION) == 0 || hasFocus)) event.detail |= SWT.SELECTED;
		event.x = (int)cellRect.x;
		event.y = (int)cellRect.y;
		event.width = (int)cellRect.width;
		event.height = (int)cellRect.height;
		sendEvent (SWT.EraseItem, event);
		if (!event.doit) {
			drawForeground = drawBackground = drawSelection = false;
		} else {
			drawBackground = drawBackground && (event.detail & SWT.BACKGROUND) != 0;
			drawForeground = (event.detail & SWT.FOREGROUND) != 0;
			drawSelection = drawSelection && (event.detail & SWT.SELECTED) != 0;
		}
		if (!drawSelection && isSelected) {
			userForeground = Color.cocoa_new(display, gc.getForeground().handle);
		}

		if (isDisposed () || item.isDisposed ()) {
			gc.dispose ();
			context.restoreGraphicsState();
			return;
		}

		if (drawSelection) {
			cellRect.height -= spacing.height;
			/*
			 * On BigSur, calling highlightSelectionInClipRect here draws over the full row
			 * and not just the cellRect. This causes drawing over other cells content.
			 * Workaround is to draw the highlight background ourselves and not call
			 * highlightSelectionInClipRect to draw it.
			 */
			if (OS.isBigSurOrLater()) {
				gc.fillRectangle((int)cellRect.x, (int)cellRect.y, (int)cellRect.width, (int)cellRect.height);
			} else {
				callSuper (widget.id, OS.sel_highlightSelectionInClipRect_, cellRect);
			}
			cellRect.height += spacing.height;
		}
		gc.dispose ();
		context.restoreGraphicsState();
	} else {
		if (isSelected && (style & SWT.HIDE_SELECTION) != 0 && !hasFocus) {
			userForeground = item.getForeground (columnIndex);
		}
	}

	if (drawBackground && !drawSelection) {
		context.saveGraphicsState ();
		double [] colorRGB = background.handle;
		NSColor color = NSColor.colorWithDeviceRed (colorRGB[0], colorRGB[1], colorRGB[2], 1f);
		color.setFill ();
		NSBezierPath.fillRect (cellRect);
		context.restoreGraphicsState ();
	}

	if (drawForeground) {
		if ((!drawExpansion || hooksMeasure) && image != null) {
			NSRect destRect = new NSRect();
			destRect.x = rect.x + IMAGE_GAP;
			destRect.y = rect.y + (float)Math.ceil((rect.height - imageBounds.height) / 2);
			destRect.width = imageBounds.width;
			destRect.height = imageBounds.height;
			context.saveGraphicsState();
			NSBezierPath.bezierPathWithRect(rect).addClip();
			NSAffineTransform transform = NSAffineTransform.transform();
			transform.scaleXBy(1, -1);
			transform.translateXBy(0, -(destRect.height + 2 * destRect.y));
			transform.concat();
			image.drawInRect(destRect, new NSRect(), OS.NSCompositeSourceOver, 1);
			context.restoreGraphicsState();
			int imageWidth = imageBounds.width + IMAGE_GAP;
			rect.x += imageWidth;
			rect.width -= imageWidth;
		}
		cell.setHighlighted (false);
		boolean callSuper = false;
		if (userForeground != null) {
			/*
			* Bug in Cocoa.  For some reason, it is not possible to change the
			* foreground color to black when the cell is highlighted. The text
			* still draws white.  The fix is to draw the text and not call super.
			*/
			double [] color = userForeground.handle;
			if (color[0] == 0 && color[1] == 0 && color[2] == 0 && color[3] == 1) {
				NSMutableAttributedString newStr = new NSMutableAttributedString(cell.attributedStringValue().mutableCopy());
				NSRange range = new NSRange();
				range.length = newStr.length();
				newStr.removeAttribute(OS.NSForegroundColorAttributeName, range);
				int alignment = columnCount == 0 ? SWT.LEFT : columns[columnIndex].style & (SWT.LEFT | SWT.CENTER | SWT.RIGHT);
				NSSize size = newStr.size();
				NSRect newRect = new NSRect();
				newRect.x = rect.x + TEXT_GAP;
				newRect.y = rect.y;
				newRect.width = rect.width - TEXT_GAP;
				switch (alignment) {
					case SWT.CENTER: newRect.width -= TEXT_GAP / 2.0f + 1; break;
					case SWT.RIGHT: {
						if (rect.width > size.width) newRect.width -= TEXT_GAP;
						break;
					}
				}
				newRect.height = rect.height;
				if (newRect.height > size.height) {
					newRect.y += (newRect.height - size.height) / 2;
					newRect.height = size.height;
				}
				newStr.drawInRect(newRect);
				newStr.release();
			} else {
				NSColor nsColor = NSColor.colorWithDeviceRed(color[0], color[1], color[2], color[3]);
				cell.setTextColor(nsColor);
				callSuper = true;
			}
		} else {
			callSuper = true;
		}
		if (callSuper) {
			NSAttributedString attrStr = cell.attributedStringValue();
			NSSize size = attrStr.size();
			if (rect.height > size.height) {
				rect.y += (rect.height - size.height) / 2;
				rect.height = size.height;
			}
			super.drawInteriorWithFrame_inView(id, sel, rect, view);
		}
	}

	if (hooksPaint) {
		context.saveGraphicsState();
		NSAffineTransform transform = NSAffineTransform.transform();
		transform.translateXBy(offsetX, offsetY);
		transform.concat();

		GCData data = new GCData ();
		data.paintRect = cellRect;
		GC gc = GC.cocoa_new (this, data);
		gc.setFont (item.getFont (columnIndex));
		if (drawSelection) {
			gc.setForeground (selectionForeground);
			gc.setBackground (selectionBackground);
		} else {
			gc.setForeground (userForeground != null ? userForeground : item.getForeground (columnIndex));
			gc.setBackground (item.getBackground (columnIndex));
		}
		if (!drawExpansion) {
			gc.setClipping ((int)(cellRect.x - offsetX), (int)(cellRect.y - offsetY), (int)cellRect.width, (int)cellRect.height);
		}
		/*
		 * Client code can modify the item text in the paint listener, so reset the cached width.
		 */
		item.width = -1;

		Event event = new Event ();
		event.item = item;
		event.gc = gc;
		event.index = columnIndex;
		if (drawForeground) event.detail |= SWT.FOREGROUND;
		if (drawBackground) event.detail |= SWT.BACKGROUND;
		if (isSelected) event.detail |= SWT.SELECTED;
		event.x = itemX;
		event.y = itemY;
		event.width = contentWidth;
		event.height = itemHeight;
		sendEvent (SWT.PaintItem, event);
		gc.dispose ();

		context.restoreGraphicsState();
	}
}

@Override
void drawWithExpansionFrame_inView (long id, long sel, NSRect cellFrame, long view) {
	drawExpansion = true;
	super.drawWithExpansionFrame_inView(id, sel, cellFrame, view);
	drawExpansion = false;
}

@Override
void drawRect(long id, long sel, NSRect rect) {
	fixScrollWidth = false;
	super.drawRect(id, sel, rect);
	if (isDisposed ()) return;
	if (fixScrollWidth) {
		fixScrollWidth = false;
		if (setScrollWidth (items, true)) view.setNeedsDisplay(true);
	}
}

@Override
NSRect expansionFrameWithFrame_inView(long id, long sel, NSRect cellRect, long view) {
	if (toolTipText == null) {
		NSRect rect = super.expansionFrameWithFrame_inView(id, sel, cellRect, view);
		NSCell cell = new NSCell(id);
		NSAttributedString str = cell.attributedStringValue();
		NSSize textSize = str.size();
		NSRect expansionRect;
		if (rect.width != 0 && rect.height != 0) {
			if (hooks(SWT.MeasureItem)) {
				expansionRect = cellRect;
				NSSize cellSize = cell.cellSize();
				expansionRect.width = cellSize.width;
			} else {
				expansionRect = rect;
			}
			if (textSize.height > expansionRect.height) {
				expansionRect.height = textSize.height;
			}
		} else {
			if (hooks(SWT.MeasureItem)) {
				expansionRect = cellRect;
				NSSize cellSize = cell.cellSize();
				expansionRect.width = cellSize.width;
			} else {
				expansionRect = cell.titleRectForBounds(cellRect);
				NSSize cellSize = super.cellSize(id, OS.sel_cellSize);
				expansionRect.width = cellSize.width;
			}
			if (textSize.height > expansionRect.height) {
				expansionRect.height = textSize.height;
			} else {
				NSRect contentRect = scrollView.contentView().bounds();
				OS.NSIntersectionRect(contentRect, expansionRect, contentRect);
				if (OS.NSEqualRects(expansionRect, contentRect)) {
					return new NSRect();
				}
			}
		}
		return expansionRect;
	}
	return new NSRect();
}

@Override
Widget findTooltip (NSPoint pt) {
	NSTableView widget = (NSTableView)view;
	NSTableHeaderView headerView = widget.headerView();
	if (headerView != null) {
		pt = headerView.convertPoint_fromView_ (pt, null);
		long index = headerView.columnAtPoint (pt);
		if (index != -1) {
			NSArray nsColumns = widget.tableColumns ();
			id nsColumn = nsColumns.objectAtIndex (index);
			for (int i = 0; i < columnCount; i++) {
				TableColumn column = columns [i];
				if (column.nsColumn.id == nsColumn.id) {
					return column;
				}
			}
		}
	}
	return super.findTooltip (pt);
}

void fixSelection (int index, boolean add) {
	int [] selection = getSelectionIndices ();
	if (selection.length == 0) return;
	int newCount = 0;
	boolean fix = false;
	for (int i = 0; i < selection.length; i++) {
		if (!add && selection [i] == index) {
			fix = true;
		} else {
			int newIndex = newCount++;
			selection [newIndex] = selection [i];
			if (selection [newIndex] >= index) {
				selection [newIndex] += add ? 1 : -1;
				fix = true;
			}
		}
	}
	if (fix) select (selection, newCount, true);
}

int getCheckColumnWidth () {
	return (int)checkColumn.dataCell().cellSize().width;
}

@Override
public Rectangle getClientArea () {
	checkWidget ();
	Rectangle rect = super.getClientArea ();
	/*
	 * OSX version < 10.11 - The origin of the table is the top-left of the rows
	 * of the table, not the header. We adjust the y value and height of the rect
	 * accordingly, to include the header.
	 *
	 * OSX 10.11 - The origin of the table is the header and the header's
	 * height is already included in the rect. Hence, we return the rect as is.
	 */
	if (OS.VERSION < OS.VERSION (10, 11, 0)) {
		NSTableHeaderView headerView = ((NSTableView) view).headerView ();
		if (headerView != null) {
			int height =  (int) headerView.bounds ().height;
			rect.y -= height;
			rect.height += height;
		}
	}
	return rect;
}

TableColumn getColumn (id id) {
	for (int i = 0; i < columnCount; i++) {
		if (columns[i].nsColumn.id == id.id) {
			return columns[i];
		}
	}
	return null;
}

/**
 * Returns the column at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
 * Columns are returned in the order that they were created.
 * If no <code>TableColumn</code>s were created by the programmer,
 * this method will throw <code>ERROR_INVALID_RANGE</code> despite
 * the fact that a single column of data may be visible in the table.
 * This occurs when the programmer uses the table like a list, adding
 * items but never creating a column.
 *
 * @param index the index of the column to return
 * @return the column at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#getColumnOrder()
 * @see Table#setColumnOrder(int[])
 * @see TableColumn#getMoveable()
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 */
public TableColumn getColumn (int index) {
	checkWidget ();
	if (!(0 <=index && index < columnCount)) error (SWT.ERROR_INVALID_RANGE);
	return columns [index];
}

/**
 * Returns the number of columns contained in the receiver.
 * If no <code>TableColumn</code>s were created by the programmer,
 * this value is zero, despite the fact that visually, one column
 * of items may be visible. This occurs when the programmer uses
 * the table like a list, adding items but never creating a column.
 *
 * @return the number of columns
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getColumnCount () {
	checkWidget ();
	return columnCount;
}

/**
 * Returns an array of zero-relative integers that map
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
 *
 * @see Table#setColumnOrder(int[])
 * @see TableColumn#getMoveable()
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 *
 * @since 3.1
 */
public int [] getColumnOrder () {
	checkWidget ();
	int [] order = new int [columnCount];
	for (int i = 0; i < columnCount; i++) {
		TableColumn column = columns [i];
		int index = indexOf (column.nsColumn);
		if ((style & SWT.CHECK) != 0) index -= 1;
		order [index] = i;
	}
	return order;
}

/**
 * Returns an array of <code>TableColumn</code>s which are the
 * columns in the receiver.  Columns are returned in the order
 * that they were created.  If no <code>TableColumn</code>s were
 * created by the programmer, the array is empty, despite the fact
 * that visually, one column of items may be visible. This occurs
 * when the programmer uses the table like a list, adding items but
 * never creating a column.
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
 *
 * @see Table#getColumnOrder()
 * @see Table#setColumnOrder(int[])
 * @see TableColumn#getMoveable()
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 */
public TableColumn [] getColumns () {
	checkWidget ();
	TableColumn [] result = new TableColumn [columnCount];
	System.arraycopy (columns, 0, result, 0, columnCount);
	return result;
}

/**
 * Returns the width in points of a grid line.
 *
 * @return the width of a grid line in points
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getGridLineWidth () {
	checkWidget ();
	return 0;
}

/**
 * Returns the header background color.
 *
 * @return the receiver's header background color.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.106
 */
public Color getHeaderBackground () {
	checkWidget ();
	return getHeaderBackgroundColor ();
}

private Color getHeaderBackgroundColor () {
	return headerBackground != null ? Color.cocoa_new (display, headerBackground) : defaultBackground ();
}

/**
 * Returns the header foreground color.
 *
 * @return the receiver's header foreground color.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.106
 */
public Color getHeaderForeground () {
	checkWidget ();
	return getHeaderForegroundColor ();
}

Color getHeaderForegroundColor () {
	return headerForeground != null ? Color.cocoa_new (display, headerForeground) : defaultForeground ();
}

/**
 * Returns the height of the receiver's header
 *
 * @return the height of the header or zero if the header is not visible
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 2.0
 */
public int getHeaderHeight () {
	checkWidget ();
	NSTableHeaderView headerView = ((NSTableView)view).headerView();
	if (headerView == null) return 0;
	return (int)headerView.bounds().height;
}

/**
 * Returns <code>true</code> if the receiver's header is visible,
 * and <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's header's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getHeaderVisible () {
	checkWidget ();
	return ((NSTableView)view).headerView() != null;
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
public TableItem getItem (int index) {
	checkWidget ();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	return _getItem (index);
}

/**
 * Returns the item at the given point in the receiver
 * or null if no such item exists. The point is in the
 * coordinate system of the receiver.
 * <p>
 * The item that is returned represents an item that could be selected by the user.
 * For example, if selection only occurs in items in the first column, then null is
 * returned if the point is outside of the item.
 * Note that the SWT.FULL_SELECTION style hint, which specifies the selection policy,
 * determines the extent of the selection.
 * </p>
 *
 * @param point the point used to locate the item
 * @return the item at the given point, or null if the point is not in a selectable item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TableItem getItem (Point point) {
	checkWidget ();
	NSTableView widget = (NSTableView)view;
	NSPoint pt = new NSPoint();
	pt.x = point.x;
	pt.y = point.y;
	int row = (int)widget.rowAtPoint(pt);
	if (row == -1) return null;
	return items[row];
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
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the receiver.
 *
 * @return the height of one item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemHeight () {
	checkWidget ();
	return (int)((NSTableView)view).rowHeight() + CELL_GAP;
}

/**
 * Returns a (possibly empty) array of <code>TableItem</code>s which
 * are the items in the receiver.
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
public TableItem [] getItems () {
	checkWidget ();
	TableItem [] result = new TableItem [itemCount];
	if ((style & SWT.VIRTUAL) != 0) {
		for (int i=0; i<itemCount; i++) {
			result [i] = _getItem (i);
		}
	} else {
		System.arraycopy (items, 0, result, 0, itemCount);
	}
	return result;
}

/**
 * Returns <code>true</code> if the receiver's lines are visible,
 * and <code>false</code> otherwise. Note that some platforms draw
 * grid lines while others may draw alternating row colors.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the visibility state of the lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getLinesVisible () {
	checkWidget ();
	return ((NSTableView)view).usesAlternatingRowBackgroundColors();
}

/**
 * Returns an array of <code>TableItem</code>s that are currently
 * selected in the receiver. The order of the items is unspecified.
 * An empty array indicates that no items are selected.
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
public TableItem [] getSelection () {
	checkWidget ();
	NSTableView widget = (NSTableView)view;
	if (widget.numberOfSelectedRows() == 0) {
		return new TableItem [0];
	}
	NSIndexSet selection = widget.selectedRowIndexes();
	int count = (int)selection.count();
	long [] indexBuffer = new long [count];
	selection.getIndexes(indexBuffer, count, 0);
	TableItem [] result = new TableItem  [count];
	for (int i=0; i<count; i++) {
		result [i] = _getItem ((int)indexBuffer [i]);
	}
	return result;
}

/**
 * Returns the number of selected items contained in the receiver.
 *
 * @return the number of selected items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionCount () {
	checkWidget ();
	return (int)((NSTableView)view).numberOfSelectedRows();
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
	NSTableView widget = (NSTableView)view;
	if (widget.numberOfSelectedRows() == 0) {
		return -1;
	}
	NSIndexSet selection = widget.selectedRowIndexes();
	int count = (int)selection.count();
	long [] result = new long [count];
	selection.getIndexes(result, count, 0);
	return (int)result [0];
}

/**
 * Returns the zero-relative indices of the items which are currently
 * selected in the receiver. The order of the indices is unspecified.
 * The array is empty if no items are selected.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its selection, so modifying the array will
 * not affect the receiver.
 * </p>
 * @return the array of indices of the selected items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int [] getSelectionIndices () {
	checkWidget ();
	NSTableView widget = (NSTableView)view;
	if (widget.numberOfSelectedRows() == 0) {
		return new int [0];
	}
	NSIndexSet selection = widget.selectedRowIndexes();
	int count = (int)selection.count();
	long [] indices = new long [count];
	selection.getIndexes(indices, count, 0);
	int [] result = new int [count];
	for (int i = 0; i < indices.length; i++) {
		result [i] = (int)indices [i];
	}
	return result;
}

/**
 * Returns the column which shows the sort indicator for
 * the receiver. The value may be null if no column shows
 * the sort indicator.
 *
 * @return the sort indicator
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setSortColumn(TableColumn)
 *
 * @since 3.2
 */
public TableColumn getSortColumn () {
	checkWidget ();
	return sortColumn;
}

/**
 * Returns the direction of the sort indicator for the receiver.
 * The value will be one of <code>UP</code>, <code>DOWN</code>
 * or <code>NONE</code>.
 *
 * @return the sort direction
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setSortDirection(int)
 *
 * @since 3.2
 */
public int getSortDirection () {
	checkWidget ();
	return sortDirection;
}

/**
 * Returns the zero-relative index of the item which is currently
 * at the top of the receiver. This index can change when items are
 * scrolled or new items are added or removed.
 *
 * @return the index of the top item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopIndex () {
	checkWidget ();
	//TODO - partial item at the top
	NSRect rect = scrollView.documentVisibleRect();
	NSPoint point = new NSPoint();
	point.x = rect.x;
	point.y = rect.y;

	/*
	 * In OSX 10.11, the origin of the table is the header, not the top-left of the rows.
	 * Offset the point's y coordinate accordingly.
	 */
	if (OS.VERSION >= OS.VERSION (10, 11, 0)) {
		NSTableHeaderView headerView = ((NSTableView) view).headerView ();
		if (headerView != null) {
			int height =  (int) headerView.bounds ().height;
			point.y += height;
		}
	}
	int rowAtPoint = (int)((NSTableView)view).rowAtPoint(point);
	if (rowAtPoint == -1) return 0; /* Empty table */
	return rowAtPoint;
}

@Override
NSRect headerRectOfColumn (long id, long sel, long column) {
	if ((style & SWT.CHECK) == 0) return callSuperRect(id, sel, column);

	if (column == 0) {
		NSRect returnValue = callSuperRect(id, sel, column);
		returnValue.width = 0;
		return returnValue;
	}
	if (column == 1) {
		NSRect returnValue = callSuperRect(id, sel, column);
		// Save a call to [NSTableView intercellSpacing] by using our constant.
		returnValue.width += (checkColumn.width() + CELL_GAP);
		returnValue.x -= (checkColumn.width() + CELL_GAP);
		return returnValue;
	}
	return callSuperRect(id, sel, column);
}

@Override
void highlightSelectionInClipRect(long id, long sel, long rect) {
	if (hooks (SWT.EraseItem)) return;
	if ((style & SWT.HIDE_SELECTION) != 0 && !hasFocus()) return;
	NSRect clipRect = new NSRect ();
	OS.memmove (clipRect, rect, NSRect.sizeof);
	callSuper (id, sel, clipRect);
}

@Override
long hitTestForEvent (long id, long sel, long event, NSRect rect, long controlView) {
	/*
	* For some reason, the cell class needs to implement hitTestForEvent:inRect:ofView:,
	* otherwise the double action selector is not called properly.
	*/
	return callSuper(id, sel, event, rect, controlView);
}

@Override
long image (long id, long sel) {
	long [] image = new long [1];
	OS.object_getInstanceVariable(id, Display.SWT_IMAGE, image);
	return image[0];
}

@Override
NSRect imageRectForBounds (long id, long sel, NSRect cellFrame) {
	NSImage image = new NSCell(id).image();
	if (image != null) {
		cellFrame.x += IMAGE_GAP;
		cellFrame.width = imageBounds.width;
		cellFrame.height = imageBounds.height;
	}
	return cellFrame;
}

int indexOf (NSTableColumn column) {
	return (int)((NSTableView)view).tableColumns().indexOfObjectIdenticalTo(column);
}

/**
 * Searches the receiver's list starting at the first column
 * (index 0) until a column is found that is equal to the
 * argument, and returns the index of that column. If no column
 * is found, returns -1.
 *
 * @param column the search column
 * @return the index of the column
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the column is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (TableColumn column) {
	checkWidget ();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<columnCount; i++) {
		if (columns [i] == column) return i;
	}
	return -1;
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
public int indexOf (TableItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (1 <= lastIndexOf && lastIndexOf < itemCount - 1) {
		if (items [lastIndexOf] == item) return lastIndexOf;
		if (items [lastIndexOf + 1] == item) return ++lastIndexOf;
		if (items [lastIndexOf - 1] == item) return --lastIndexOf;
	}
	if (lastIndexOf < itemCount / 2) {
		for (int i=0; i<itemCount; i++) {
			if (items [i] == item) return lastIndexOf = i;
		}
	} else {
		for (int i=itemCount - 1; i>=0; --i) {
			if (items [i] == item) return lastIndexOf = i;
		}
	}
	return -1;
}

/**
 * Returns <code>true</code> if the item is selected,
 * and <code>false</code> otherwise.  Indices out of
 * range are ignored.
 *
 * @param index the index of the item
 * @return the selection state of the item at the index
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isSelected (int index) {
	checkWidget ();
	if (!(0 <= index && index < itemCount)) return false;
	return ((NSTableView)view).isRowSelected(index);
}

@Override
boolean isTransparent() {
	return true;
}

@Override
void keyDown(long id, long sel, long theEvent) {
	ignoreSelect = preventSelect = false;
	keyDown = true;
	super.keyDown(id, sel, theEvent);
	keyDown = false;
}

@Override
boolean isTrim (NSView view) {
	if (super.isTrim (view)) return true;
	return view.id == headerView.id;
}

@Override
long menuForEvent(long id, long sel, long theEvent) {
	if (display.lastHandledMenuForEventId == theEvent) return 0;
	if (id != headerView.id) {
		/*
		 * Feature in Cocoa: Table views do not change the selection when the user
		 * right-clicks or control-clicks on an NSTableView or its subclasses. Fix is to select the
		 * clicked-on row ourselves.
		 */
		NSEvent event = new NSEvent(theEvent);
		NSTableView table = (NSTableView)view;

		// get the current selections for the table view.
		NSIndexSet selectedRowIndexes = table.selectedRowIndexes();

		// select the row that was clicked before showing the menu for the event
		NSPoint mousePoint = view.convertPoint_fromView_(event.locationInWindow(), null);
		long row = table.rowAtPoint(mousePoint);

		// figure out if the row that was just clicked on is currently selected
		if (selectedRowIndexes.containsIndex(row) == false) {
			NSIndexSet set = (NSIndexSet)new NSIndexSet().alloc();
			set = set.initWithIndex(row);
			table.selectRowIndexes (set, false);
			set.release();
		}
		// else that row is currently selected, so don't change anything.
	}
	return super.menuForEvent(id, sel, theEvent);
}

@Override
void mouseDown (long id, long sel, long theEvent) {
	if (id == view.id) {
		// Bug/feature in Cocoa:  If the table has a context menu we just set it visible instead of returning
		// it from menuForEvent:.  This has the side effect, however, of sending control-click to the NSTableView,
		// which is interpreted as a single click that clears the selection.  Fix is to ignore control-click if the
		// view has a context menu.
		NSEvent event = new NSEvent(theEvent);
		if ((event.modifierFlags() & OS.NSControlKeyMask) != 0) return;
	}
	super.mouseDown(id, sel, theEvent);
}

@Override
void mouseDownSuper(long id, long sel, long theEvent) {
	ignoreSelect = preventSelect = false;
	boolean check = false;
	NSEvent nsEvent = new NSEvent(theEvent);
	NSTableView widget = (NSTableView)view;
	NSPoint pt = view.convertPoint_fromView_(nsEvent.locationInWindow(), null);
	int row = (int)widget.rowAtPoint(pt);
	if (row != -1 && (style & SWT.CHECK) != 0) {
		int column = (int)widget.columnAtPoint(pt);
		NSCell cell = widget.preparedCellAtColumn(column, row);
		if (cell != null && cell.isKindOfClass(OS.class_NSButtonCell) && cell.isEnabled()) {
			NSRect checkRect = cell.imageRectForBounds(widget.frameOfCellAtColumn(column, row));
			if (OS.NSPointInRect(pt, checkRect)) {
				check = preventSelect = true;
			}
		}
	}
	if (!check && row != -1 && (nsEvent.modifierFlags() & OS.NSDeviceIndependentModifierFlagsMask) == 0 && nsEvent.clickCount() == 1) {
		if (widget.isRowSelected(row)) {
			if (0 <= row && row < itemCount) {
				selectedRowIndex = row;
			}
		}
	}
	didSelect = false;
	super.mouseDownSuper(id, sel, theEvent);
	didSelect = false;
}

@Override
boolean needsPanelToBecomeKey (long id, long sel) {
	return false;
}

/*
 * Feature in Cocoa.  If a checkbox is in multi-state mode, nextState cycles
 * from off to mixed to on and back to off again.  This will cause the on state
 * to momentarily appear while clicking on the checkbox.  To avoid this,
 * override [NSCell nextState] to go directly to the desired state.
 */
@Override
long nextState (long id, long sel) {
	NSTableView tableView = (NSTableView)view;
	int index = (int)tableView.clickedRow();
	if (index == -1) index = (int)tableView.selectedRow ();
	TableItem item = items[index];
	if (item.grayed) {
		return item.checked ? OS.NSOffState : OS.NSMixedState;
	}
	return item.checked ? OS.NSOffState : OS.NSOnState;
}

@Override
long numberOfRowsInTableView(long id, long sel, long aTableView) {
	return itemCount;
}

@Override
void register () {
	super.register ();
	display.addWidget (headerView, this);
	display.addWidget (dataCell, this);
	if (buttonCell != null) display.addWidget (buttonCell, this);
}

@Override
void releaseChildren (boolean destroy) {
	if (items != null) {
		for (int i=0; i<itemCount; i++) {
			TableItem item = items [i];
			if (item != null && !item.isDisposed ()) {
				item.release (false);
			}
		}
		items = null;
	}
	if (columns != null) {
		for (int i=0; i<columnCount; i++) {
			TableColumn column = columns [i];
			if (column != null && !column.isDisposed ()) {
				column.release (false);
			}
		}
		columns = null;
	}
	super.releaseChildren (destroy);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	if (headerView != null) headerView.release();
	headerView = null;
	if (firstColumn != null) firstColumn.release();
	firstColumn = null;
	if (checkColumn != null) checkColumn.release();
	checkColumn = null;
	if (dataCell != null) dataCell.release();
	dataCell = null;
	if (buttonCell != null) buttonCell.release();
	buttonCell = null;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	currentItem = null;
	sortColumn = null;
}

/**
 * Removes the item from the receiver at the given
 * zero-relative index.
 *
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int index) {
	checkWidget ();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	TableItem item = items [index];
	if (item != null) item.release (false);
	if (index != itemCount - 1) fixSelection (index, false);
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	updateRowCount();
	if (itemCount == 0) {
		setTableEmpty ();
	}
}

/**
 * Removes the items from the receiver which are
 * between the given zero-relative start and end
 * indices (inclusive).
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if either the start or end are not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int start, int end) {
	checkWidget ();
	if (start > end) return;
	if (!(0 <= start && start <= end && end < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (start == 0 && end == itemCount - 1) {
		removeAll ();
	} else {
		int numOfItemsRemoved = end - start + 1;
		for (int i=start; i<=end; i++) {
			TableItem item = items [i];
			if (item != null) item.release (false);
		}
		//fix selection
		int [] selection = getSelectionIndices ();
		if (selection.length != 0) {
			int newCount = 0;
			boolean fix = false;
			for (int i = 0; i < selection.length; i++) {
				if (selection [i] >= start && selection[i] <= end) {
					fix = true;
				} else {
					int newIndex = newCount++;
					selection [newIndex] = selection [i];
					if (selection [newIndex] > end) {
						selection [newIndex] -= numOfItemsRemoved;
						fix = true;
					}
				}
			}
			if (fix) select (selection, newCount, true);
		}
		//fix items array
		System.arraycopy (items, start + numOfItemsRemoved, items, start, itemCount - (start + numOfItemsRemoved));
		for (int i = itemCount - numOfItemsRemoved; i < itemCount; i++) {
			items [i] = null;
		}
		itemCount -= numOfItemsRemoved;
		updateRowCount();
	}
	if (itemCount == 0) {
		setTableEmpty ();
	}
}

/**
 * Removes the items from the receiver's list at the given
 * zero-relative indices.
 *
 * @param indices the array of indices of the items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 *    <li>ERROR_NULL_ARGUMENT - if the indices array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int [] indices) {
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (indices.length == 0) return;
	int [] newIndices = new int [indices.length];
	System.arraycopy (indices, 0, newIndices, 0, indices.length);
	sort (newIndices);
	int start = newIndices [newIndices.length - 1], end = newIndices [0];
	if (!(0 <= start && start <= end && end < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int last = -1;
	for (int i=0; i<newIndices.length; i++) {
		int index = newIndices [i];
		if (index != last) {
			TableItem item = items [index];
			if (item != null) item.release (false);
			if (index != itemCount - 1) fixSelection (index, false);
			System.arraycopy (items, index + 1, items, index, --itemCount - index);
			items [itemCount] = null;
			last = index;
		}
	}
	updateRowCount();
	if (itemCount == 0) {
		setTableEmpty ();
	}
}

/**
 * Removes all of the items from the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget ();
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
		if (item != null && !item.isDisposed ()) item.release (false);
	}
	setTableEmpty ();
	updateRowCount();
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
 * @see #addSelectionListener(SelectionListener)
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);
}

@Override
void reskinChildren (int flags) {
	if (items != null) {
		for (int i=0; i<itemCount; i++) {
			TableItem item = items [i];
			if (item != null) item.reskin (flags);
		}
	}
	if (columns != null) {
		for (int i=0; i<columnCount; i++) {
			TableColumn column = columns [i];
			if (!column.isDisposed ()) column.reskin (flags);
		}
	}
	super.reskinChildren (flags);
}

@Override
void scrollClipViewToPoint(long id, long sel, long clipView, NSPoint point) {
	if (shouldScroll) {
		super.scrollClipViewToPoint(id, sel, clipView, point);
		if ((style & SWT.CHECK) != 0 && columnCount > 0 && ((NSTableView)view).headerView() != null) {
			if (point.x <= getCheckColumnWidth()) {
				/*
				 * Header of first column is extended as header of the checkbox column.
				 * So, redraw header of first column when check column is scrolled to be visible.
				 */
				headerView.setNeedsDisplayInRect(headerView.headerRectOfColumn(1));
			}
		}
	}
}

/**
 * Selects the item at the given zero-relative index in the receiver.
 * If the item at the index was already selected, it remains
 * selected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int index) {
	checkWidget ();
	if (0 <= index && index < itemCount) {
		NSIndexSet set = (NSIndexSet)new NSIndexSet().alloc();
		set = set.initWithIndex(index);
		NSTableView widget = (NSTableView)view;
		ignoreSelect = true;
		widget.selectRowIndexes(set, (style & SWT.MULTI) != 0);
		ignoreSelect = false;
		set.release();
	}
}

/**
 * Selects the items in the range specified by the given zero-relative
 * indices in the receiver. The range of indices is inclusive.
 * The current selection is not cleared before the new items are selected.
 * <p>
 * If an item in the given range is not selected, it is selected.
 * If an item in the given range was already selected, it remains selected.
 * Indices that are out of range are ignored and no items will be selected
 * if start is greater than end.
 * If the receiver is single-select and there is more than one item in the
 * given range, then all indices are ignored.
 * </p>
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#setSelection(int,int)
 */
public void select (int start, int end) {
	checkWidget ();
	if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) return;
	if (itemCount == 0 || start >= itemCount) return;
	if (start == 0 && end == itemCount - 1) {
		selectAll ();
	} else {
		start = Math.max (0, start);
		end = Math.min (end, itemCount - 1);
		NSRange range = new NSRange();
		range.location = start;
		range.length = end - start + 1;
		NSIndexSet set = (NSIndexSet)new NSIndexSet().alloc();
		set = set.initWithIndexesInRange(range);
		NSTableView widget = (NSTableView)view;
		ignoreSelect = true;
		widget.selectRowIndexes(set, (style & SWT.MULTI) != 0);
		ignoreSelect = false;
		set.release();
	}
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * The current selection is not cleared before the new items are selected.
 * <p>
 * If the item at a given index is not selected, it is selected.
 * If the item at a given index was already selected, it remains selected.
 * Indices that are out of range and duplicate indices are ignored.
 * If the receiver is single-select and multiple indices are specified,
 * then all indices are ignored.
 * </p>
 *
 * @param indices the array of indices for the items to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#setSelection(int[])
 */
public void select (int [] indices) {
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = indices.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	int count = 0;
	NSMutableIndexSet set = (NSMutableIndexSet)new NSMutableIndexSet().alloc().init();
	for (int i=0; i<length; i++) {
		int index = indices [i];
		if (index >= 0 && index < itemCount) {
			set.addIndex (indices [i]);
			count++;
		}
	}
	if (count > 0) {
		NSTableView widget = (NSTableView)view;
		ignoreSelect = true;
		widget.selectRowIndexes(set, (style & SWT.MULTI) != 0);
		ignoreSelect = false;
	}
	set.release();
}

void select (int [] indices, int count, boolean clear) {
	NSMutableIndexSet set = (NSMutableIndexSet)new NSMutableIndexSet().alloc().init();
	for (int i=0; i<count; i++) set.addIndex (indices [i]);
	NSTableView widget = (NSTableView)view;
	ignoreSelect = true;
	widget.selectRowIndexes(set, !clear);
	ignoreSelect = false;
	set.release();
}

/**
 * Selects all of the items in the receiver.
 * <p>
 * If the receiver is single-select, do nothing.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void selectAll () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return;
	NSTableView widget = (NSTableView)view;
	ignoreSelect = true;
	widget.selectAll(null);
	ignoreSelect = false;
}

@Override
void setBackgroundColor(NSColor nsColor) {
	((NSTableView) view).setBackgroundColor (nsColor);
}

/**
 * Sets the order that the items in the receiver should
 * be displayed in to the given argument which is described
 * in terms of the zero-relative ordering of when the items
 * were added.
 *
 * @param order the new order to display the items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item order is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item order is not the same length as the number of items</li>
 * </ul>
 *
 * @see Table#getColumnOrder()
 * @see TableColumn#getMoveable()
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 *
 * @since 3.1
 */
public void setColumnOrder (int [] order) {
	checkWidget ();
	if (order == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (columnCount == 0) {
		if (order.length != 0) error (SWT.ERROR_INVALID_ARGUMENT);
		return;
	}
	if (order.length != columnCount) error (SWT.ERROR_INVALID_ARGUMENT);
	int [] oldOrder = getColumnOrder ();
	boolean reorder = false;
	boolean [] seen = new boolean [columnCount];
	for (int i=0; i<order.length; i++) {
		int index = order [i];
		if (index < 0 || index >= columnCount) error (SWT.ERROR_INVALID_ARGUMENT);
		if (seen [index]) error (SWT.ERROR_INVALID_ARGUMENT);
		seen [index] = true;
		if (order [i] != oldOrder [i]) reorder = true;
	}
	if (reorder) {
		NSTableView tableView = (NSTableView)view;
		int [] oldX = new int [oldOrder.length];
		int check = (style & SWT.CHECK) != 0 ? 1 : 0;
		for (int i=0; i<oldOrder.length; i++) {
			int index = oldOrder[i];
			oldX [index] = (int)tableView.rectOfColumn (i + check).x;
		}
		int [] newX = new int [order.length];
		for (int i=0; i<order.length; i++) {
			int index = order [i];
			TableColumn column = columns[index];
			int oldIndex = indexOf (column.nsColumn);
			int newIndex = i + check;
			tableView.moveColumn (oldIndex, newIndex);
			newX [index] = (int)tableView.rectOfColumn (newIndex).x;
		}
		TableColumn[] newColumns = new TableColumn [columnCount];
		System.arraycopy (columns, 0, newColumns, 0, columnCount);
		for (int i=0; i<columnCount; i++) {
			TableColumn column = newColumns [i];
			if (!column.isDisposed ()) {
				if (newX [i] != oldX [i]) {
					column.sendEvent (SWT.Move);
				}
			}
		}
	}
}

@Override
void setFont (NSFont font) {
	super.setFont (font);
	for (int i = 0; i < columnCount; i++) {
		columns[i].nsColumn.headerCell().setFont(font);
	}
	setItemHeight (null, font, !hooks (SWT.MeasureItem));
	view.setNeedsDisplay (true);
	clearCachedWidth (items);
	setScrollWidth (items, true);
}

@Override
void setFrameSize (long id, long sel, NSSize size) {
	super.setFrameSize(id, sel, size);

	/*
	 * Bug 577767: Since macOS 10.15, NSTableView has 'autoresizingMask'
	 * set to follow resizes of its NSClipView. This sometimes causes
	 * Table/Tree to have wrong scroll range (note that size of NSClipView
	 * is what you see and size of NSTableView is the size of entire
	 * content, this defines scroll range). The workaround is to recalc
	 * layout after resizing.
	 */
	if ((scrollView != null) && (id == scrollView.id))
		((NSTableView)view).tile();
}

/**
 * Sets the header background color to the color specified
 * by the argument, or to the default system color if the argument is null.
 * <p>
 * Note: This operation is a <em>HINT</em> and is not supported on all platforms. If
 * the native header has a 3D look and feel (e.g. Windows 7), this method
 * will cause the header to look FLAT irrespective of the state of the table style.
 * </p>
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.106
 */
public void setHeaderBackground (Color color) {
	checkWidget ();
	if (color != null) {
		if (color.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	double [] headerBackground = color != null ? color.handle : null;
	if (equals (headerBackground, this.headerBackground)) return;
	this.headerBackground = headerBackground;
	if (getHeaderVisible()) {
		redrawWidget (view, false);
	}
}

/**
 * Sets the header foreground color to the color specified
 * by the argument, or to the default system color if the argument is null.
 * <p>
 * Note: This operation is a <em>HINT</em> and is not supported on all platforms. If
 * the native header has a 3D look and feel (e.g. Windows 7), this method
 * will cause the header to look FLAT irrespective of the state of the table style.
 * </p>
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.106
 */
public void setHeaderForeground (Color color) {
	checkWidget ();
	if (color != null) {
		if (color.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	double [] headerForeground = color != null ? color.handle : null;
	if (equals (headerForeground, this.headerForeground)) return;
	this.headerForeground = headerForeground;
	if (getHeaderVisible()) {
		redrawWidget (view, false);
	}
}

/**
 * Marks the receiver's header as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param show the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHeaderVisible (boolean show) {
	checkWidget ();
	((NSTableView)view).setHeaderView (show ? headerView : null);
	scrollView.tile();
}

@Override
void setImage (long id, long sel, long arg0) {
	OS.object_setInstanceVariable(id, Display.SWT_IMAGE, arg0);
}

/**
 * Sets the number of items contained in the receiver.
 *
 * @param count the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public void setItemCount (int count) {
	checkWidget ();
	count = Math.max (0, count);
	if (count == itemCount) return;
	TableItem [] children = items;
	if (count < itemCount) {
		for (int index = count; index < itemCount; index ++) {
			TableItem item = children [index];
			if (item != null && !item.isDisposed()) item.release (false);
		}
	}
	if (count > itemCount) {
		if ((getStyle() & SWT.VIRTUAL) == 0) {
			for (int i=itemCount; i<count; i++) {
				new TableItem (this, SWT.NONE, i, true);
			}
			return;
		}
	}
	int length = Math.max (4, (count + 3) / 4 * 4);
	TableItem [] newItems = new TableItem [length];
	if (children != null) {
		System.arraycopy (items, 0, newItems, 0, Math.min (count, itemCount));
	}
	children = newItems;
	this.items = newItems;
	this.itemCount = count;
	updateRowCount();
}

/*public*/ void setItemHeight (int itemHeight) {
	checkWidget ();
	if (itemHeight < -1) error (SWT.ERROR_INVALID_ARGUMENT);
	if (itemHeight == -1) {
		//TODO - reset item height, ensure other API's such as setFont don't do this
	} else {
		((NSTableView)view).setRowHeight (itemHeight);
	}
}

void setItemHeight (Image image, NSFont font, boolean set) {
	if (font == null) font = getFont ().handle;
	double ascent = font.ascender ();
	double descent = -font.descender () + font.leading ();
	int height = (int)Math.ceil (ascent + descent) + 1;
	Rectangle bounds = image != null ? image.getBounds () : imageBounds;
	if (bounds != null) {
		imageBounds = bounds;
		height = Math.max (height, bounds.height);
	}
	NSTableView widget = (NSTableView)view;
	if (set || widget.rowHeight () < height) {
		widget.setRowHeight (height);
		if (headerView != null) {
			NSRect frame = headerView.frame();
			if (frame.height < height) {
				frame.height = height;
				headerView.setFrame(frame);
			}
		}
	}
}

@Override
public void setRedraw (boolean redraw) {
	checkWidget ();
	super.setRedraw (redraw);
	if (redraw && drawCount == 0) {
		/* Resize the item array to match the item count */
		if (items.length > 4 && items.length - itemCount > 3) {
			int length = Math.max (4, (itemCount + 3) / 4 * 4);
			TableItem [] newItems = new TableItem [length];
			System.arraycopy (items, 0, newItems, 0, itemCount);
			items = newItems;
		}
		setScrollWidth ();
	}
}

/**
 * Marks the receiver's lines as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. Note that some platforms draw grid lines
 * while others may draw alternating row colors.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param show the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLinesVisible (boolean show) {
	checkWidget ();
	((NSTableView)view).setUsesAlternatingRowBackgroundColors(show);
	((NSTableView)view).setGridStyleMask(show ? OS.NSTableViewSolidVerticalGridLineMask : OS.NSTableViewGridNone);
}

boolean setScrollWidth () {
	return setScrollWidth (items, true);
}

boolean setScrollWidth (TableItem item) {
	if (columnCount != 0) return false;
	if (!getDrawing()) return false;
	if (currentItem != null) {
		if (currentItem != item) fixScrollWidth = true;
		return false;
	}
	GC gc = new GC (this);
	int newWidth = item.calculateWidth (0, gc, isSelected(indexOf(item)));
	gc.dispose ();
	int oldWidth = (int)firstColumn.width ();
	if (oldWidth < newWidth) {
		firstColumn.setWidth (newWidth);
		if (horizontalBar != null && horizontalBar.view != null) redrawWidget (horizontalBar.view, false);
		return true;
	}
	return false;
}

boolean setScrollWidth (TableItem [] items, boolean set) {
	if (items == null) return false;
	if (columnCount != 0) return false;
	if (!getDrawing()) return false;
	if (currentItem != null) {
		fixScrollWidth = true;
		return false;
	}
	GC gc = new GC (this);
	int newWidth = 0;
	for (int i = 0; i < items.length; i++) {
		TableItem item = items [i];
		if (item != null) {
			newWidth = Math.max (newWidth, item.calculateWidth (0, gc, isSelected(indexOf(item))));
		}
	}
	gc.dispose ();
	if (!set) {
		int oldWidth = (int)firstColumn.width ();
		if (oldWidth >= newWidth) return false;
	}
	firstColumn.setWidth (newWidth);
	if (horizontalBar != null && horizontalBar.view != null) redrawWidget (horizontalBar.view, false);
	return true;
}

/**
 * Selects the item at the given zero-relative index in the receiver.
 * The current selection is first cleared, then the new item is selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int)
 */
public void setSelection (int index) {
	checkWidget ();
	//TODO - optimize to use expand flag
	deselectAll ();
	if (0 <= index && index < itemCount) {
		select (index);
		showIndex (index);
	}
}

/**
 * Selects the items in the range specified by the given zero-relative
 * indices in the receiver. The range of indices is inclusive.
 * The current selection is cleared before the new items are selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 * <p>
 * Indices that are out of range are ignored and no items will be selected
 * if start is greater than end.
 * If the receiver is single-select and there is more than one item in the
 * given range, then all indices are ignored.
 * </p>
 *
 * @param start the start index of the items to select
 * @param end the end index of the items to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int,int)
 */
public void setSelection (int start, int end) {
	checkWidget ();
	//TODO - optimize to use expand flag
	deselectAll ();
	if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) return;
	if (itemCount == 0 || start >= itemCount) return;
	start = Math.max (0, start);
	end = Math.min (end, itemCount - 1);
	select (start, end);
	showIndex (start);
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * The current selection is cleared before the new items are selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 * <p>
 * Indices that are out of range and duplicate indices are ignored.
 * If the receiver is single-select and multiple indices are specified,
 * then all indices are ignored.
 * </p>
 *
 * @param indices the indices of the items to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int[])
 */
public void setSelection (int [] indices) {
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	//TODO - optimize to use expand flag
	deselectAll ();
	int length = indices.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	select (indices);
	showIndex (indices [0]);
}

/**
 * Sets the receiver's selection to the given item.
 * The current selection is cleared before the new item is selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 * <p>
 * If the item is not in the receiver, then it is ignored.
 * </p>
 *
 * @param item the item to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setSelection (TableItem  item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (new TableItem [] {item});
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selection is cleared before the new items are selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 * <p>
 * Items that are not in the receiver are ignored.
 * If the receiver is single-select and multiple items are specified,
 * then all items are ignored.
 * </p>
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of items is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if one of the items has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int[])
 * @see Table#setSelection(int[])
 */
public void setSelection (TableItem [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	//TODO - optimize to use expand flag
	deselectAll ();
	int length = items.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	int [] indices = new int [length];
	int count = 0;
	for (int i=0; i<length; i++) {
		int index = indexOf (items [length - i - 1]);
		if (index != -1) {
			indices [count++] = index;
		}
	}
	if (count > 0) {
		select (indices);
		showIndex (indices [0]);
	}
}

@Override
void setShouldScrollClipView(long id, long sel, boolean shouldScroll) {
	this.shouldScroll = shouldScroll;
}

/**
 * Sets the column used by the sort indicator for the receiver. A null
 * value will clear the sort indicator.  The current sort column is cleared
 * before the new column is set.
 *
 * @param column the column used by the sort indicator or <code>null</code>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the column is disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setSortColumn (TableColumn column) {
	checkWidget ();
	if (column != null && column.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (column == sortColumn) return;
	setSort(column, sortDirection);
}

/**
 * Sets the direction of the sort indicator for the receiver. The value
 * can be one of <code>UP</code>, <code>DOWN</code> or <code>NONE</code>.
 *
 * @param direction the direction of the sort indicator
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setSortDirection  (int direction) {
	checkWidget ();
	if (direction != SWT.UP && direction != SWT.DOWN && direction != SWT.NONE) return;
	if (direction == sortDirection) return;
	setSort(sortColumn, direction);
}

void setSort (TableColumn column, int direction) {
	NSImage image = null;
	NSTableColumn nsColumn = null;
	if (column != null) {
		nsColumn = column.nsColumn;
		if (direction == SWT.DOWN) image = NSImage.imageNamed(NSString.stringWith("NSDescendingSortIndicator"));
		if (direction == SWT.UP) image = NSImage.imageNamed(NSString.stringWith("NSAscendingSortIndicator"));
	}
	NSTableView widget = (NSTableView)view;
	if (sortColumn != null && sortColumn != column) {
		widget.setIndicatorImage(null, sortColumn.nsColumn);
	}
	widget.setHighlightedTableColumn(nsColumn);
	widget.setIndicatorImage(image, nsColumn);
	sortDirection = direction;
	sortColumn = column;
}

void setTableEmpty () {
	itemCount = 0;
	items = new TableItem [4];
	imageBounds = null;
}

/**
 * Sets the zero-relative index of the item which is currently
 * at the top of the receiver. This index can change when items
 * are scrolled or new items are added and removed.
 *
 * @param index the index of the top item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTopIndex (int index) {
	checkWidget ();
	NSTableView widget = (NSTableView) view;
	int row = Math.max(0, Math.min(index, itemCount));
	NSPoint pt = new NSPoint();
	pt.x = scrollView.contentView().bounds().x;
	pt.y = widget.frameOfCellAtColumn(0, row).y;
	/*
	 * In OSX 10.11, the origin of the table is the header, not the top-left of the rows.
	 * Offset the point's y coordinate accordingly.
	 */
	if (OS.VERSION >= OS.VERSION(10, 11, 0)) {
		if (widget.headerView() != null) {
			NSRect headerRect = headerView.frame();
			pt.y -= headerRect.y + headerRect.height;
		}
	}
	view.scrollPoint(pt);
}

/**
 * Shows the column.  If the column is already showing in the receiver,
 * this method simply returns.  Otherwise, the columns are scrolled until
 * the column is visible.
 *
 * @param column the column to be shown
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the column is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the column has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public void showColumn (TableColumn column) {
	checkWidget ();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (column.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (column.parent != this) return;
	if (columnCount <= 1) return;
	int index = indexOf (column.nsColumn);
	if (!(0 <= index && index < columnCount + ((style & SWT.CHECK) != 0 ? 1 : 0))) return;
	((NSTableView)view).scrollColumnToVisible (index);
}

void showIndex (int index) {
	if (0 <= index && index < itemCount) {
		NSTableView tableView = (NSTableView)view;
		if (OS.VERSION >= OS.VERSION (10, 15, 0)) {
			if (tableView.headerView () == null) {
				/**
				 * On macOS 10.15, scrollRowToVisible doesn't work correctly if
				 * contentView's bounds is not set (i.e, width or height is 0).
				 *
				 * The contentView's bounds is set when the Table's header view is set.
				 * So don't call this code if Table has a header already.
				 */
				NSClipView contentView = scrollView.contentView ();
				if (contentView != null) {
					NSRect contentViewBounds = contentView.bounds ();
					if (contentViewBounds.height == 0 || contentViewBounds.width == 0) {
						NSView documentView = scrollView.documentView ();
						if (documentView != null) {
							NSRect documentViewBounds = documentView.bounds ();
							NSSize size = new NSSize ();
							size.width = contentViewBounds.width == 0 ? documentViewBounds.width : contentViewBounds.width;
							size.height = contentViewBounds.height == 0 ? documentViewBounds.height : contentViewBounds.height;
							contentView.setBoundsSize (size);
						}
					}
				}
			}
		}
		tableView.scrollRowToVisible(index);
	}
}

/**
 * Shows the item.  If the item is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled until
 * the item is visible.
 *
 * @param item the item to be shown
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#showSelection()
 */
public void showItem (TableItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	int index = indexOf (item);
	if (index != -1) showIndex (index);
}

/**
 * Shows the selection.  If the selection is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled until
 * the selection is visible.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#showItem(TableItem)
 */
public void showSelection () {
	checkWidget ();
	int index = getSelectionIndex ();
	if (index >= 0) {
		checkData(_getItem(index));
		showIndex (index);
	}
}

@Override
void selectRowIndexes_byExtendingSelection (long id, long sel, long indexes, boolean extend) {
	if (preventSelect && !ignoreSelect) return;
	if ((style & SWT.SINGLE) != 0 && !ignoreSelect) {
		NSIndexSet set = new NSIndexSet(indexes);
		if (set.count() == 0) return;
	}
	super.selectRowIndexes_byExtendingSelection (id, sel, indexes, extend);
}

@Override
void sendDoubleSelection() {
	NSTableView tableView = (NSTableView)view;
	int rowIndex = (int)tableView.clickedRow ();
	if (rowIndex == -1) rowIndex = (int)tableView.selectedRow();
	if (rowIndex != -1) {
		if ((style & SWT.CHECK) != 0) {
			NSArray columns = tableView.tableColumns ();
			int columnIndex = (int)tableView.clickedColumn ();
			if (columnIndex != -1) {
				id column = columns.objectAtIndex (columnIndex);
				if (column.id == checkColumn.id) return;
			}
		}
		Event event = new Event ();
		event.item = _getItem (rowIndex);
		sendSelectionEvent (SWT.DefaultSelection, event, false);
	}
}

@Override
boolean sendKeyEvent (NSEvent nsEvent, int type) {
	boolean result = super.sendKeyEvent (nsEvent, type);
	if (!result) return result;
	if (type != SWT.KeyDown) return result;
	short keyCode = nsEvent.keyCode ();
	switch (keyCode) {
		case 76: /* KP Enter */
		case 36: { /* Return */
			sendDoubleSelection();
			break;
		}
	}
	return result;
}

void sendMeasureItem (TableItem item, int columnIndex, NSSize size, boolean isSelected) {
	NSTableView widget = (NSTableView)this.view;
	int contentWidth = (int)Math.ceil (size.width);
	NSSize spacing = widget.intercellSpacing();
	int itemHeight = (int)Math.ceil (widget.rowHeight() + spacing.height);
	GCData data = new GCData ();
	data.paintRect = widget.frame ();
	GC gc = GC.cocoa_new (this, data);
	gc.setFont (item.getFont (columnIndex));
	Event event = new Event ();
	event.item = item;
	event.gc = gc;
	event.index = columnIndex;
	event.width = contentWidth;
	event.height = itemHeight;
	if (isSelected && ((style & SWT.HIDE_SELECTION) == 0 || hasFocus())) event.detail |= SWT.SELECTED;
	sendEvent (SWT.MeasureItem, event);
	gc.dispose ();
	if (!isDisposed () && !item.isDisposed ()) {
		size.width = event.width;
		size.height = event.height;
		if (itemHeight < event.height) {
			widget.setRowHeight (event.height);
		}
		if (contentWidth != event.width) {
			if (columnCount == 0 && columnIndex == 0) {
				item.width = event.width;
				if (setScrollWidth (item)) {
					widget.setNeedsDisplay(true);
				}
			}
		}
	}
}

@Override
void tableViewColumnDidMove (long id, long sel, long aNotification) {
	NSNotification notification = new NSNotification (aNotification);
	NSDictionary userInfo = notification.userInfo ();
	NSString nsstring = (NSString) new NSString().alloc();
	nsstring = nsstring.initWithString("NSOldColumn"); //$NON-NLS-1$
	id nsOldIndex = userInfo.valueForKey (nsstring);
	nsstring.release();
	nsstring = (NSString) new NSString().alloc();
	nsstring = nsstring.initWithString("NSNewColumn"); //$NON-NLS-1$
	id nsNewIndex = userInfo.valueForKey (nsstring);
	nsstring.release();
	int oldIndex = new NSNumber (nsOldIndex).intValue ();
	int newIndex = new NSNumber (nsNewIndex).intValue ();
	NSTableView tableView = (NSTableView)view;
	int startIndex = Math.min (oldIndex, newIndex);
	int endIndex = Math.max (oldIndex, newIndex);
	NSArray nsColumns = tableView.tableColumns ();
	for (int i = startIndex; i <= endIndex; i++) {
		id columnId = nsColumns.objectAtIndex (i);
		TableColumn column = getColumn (columnId);
		if (column != null) {
			column.sendEvent (SWT.Move);
			if (isDisposed ()) return;
		}
	}
	headerView.setNeedsDisplay(true);
}

@Override
void tableViewColumnDidResize (long id, long sel, long aNotification) {
	NSNotification notification = new NSNotification (aNotification);
	NSDictionary userInfo = notification.userInfo ();
	NSString nsstring = (NSString) new NSString().alloc();
	nsstring = nsstring.initWithString("NSTableColumn"); //$NON-NLS-1$
	id columnId = userInfo.valueForKey (nsstring);
	nsstring.release();
	TableColumn column = getColumn (columnId);
	if (column == null) return; /* either CHECK column or firstColumn in 0-column Table */

	column.sendEvent (SWT.Resize);
	if (isDisposed ()) return;

	NSTableView tableView = (NSTableView)view;
	int index = indexOf (column.nsColumn);
	if (index == -1) return; /* column was disposed in Resize callback */

	NSArray nsColumns = tableView.tableColumns ();
	int columnCount = (int)tableView.numberOfColumns ();
	for (int i = index + 1; i < columnCount; i++) {
		columnId = nsColumns.objectAtIndex (i);
		column = getColumn (columnId);
		if (column != null) {
			column.sendEvent (SWT.Move);
			if (isDisposed ()) return;
		}
	}
}

@Override
void sendSelection () {
	if (ignoreSelect) return;
	NSTableView widget = (NSTableView) view;
	int row = (int)widget.selectedRow ();
	if(row == -1)
		sendSelectionEvent (SWT.Selection);
	else {
		TableItem item = _getItem (row);
		Event event = new Event ();
		event.item = item;
		event.index = row;
		sendSelectionEvent (SWT.Selection, event, false);
	}
}

@Override
void tableViewSelectionDidChange (long id, long sel, long aNotification) {
	if (didSelect) return;
	sendSelection();
}

@Override
void tableViewSelectionIsChanging (long id, long sel, long aNotification) {
	didSelect = true;
	sendSelection();
}

@Override
void tableView_didClickTableColumn (long id, long sel, long tableView, long tableColumn) {
	TableColumn column = getColumn (new id (tableColumn));
	if (column == null) return; /* either CHECK column or firstColumn in 0-column Table */
	column.sendSelectionEvent (SWT.Selection);
}

@Override
long tableView_objectValueForTableColumn_row (long id, long sel, long aTableView, long aTableColumn, long rowIndex) {
	int index = (int)rowIndex;
	TableItem item = _getItem (index);
	checkData (item, index);
	if (checkColumn != null && aTableColumn == checkColumn.id) {
		NSNumber value;
		if (item.checked && item.grayed) {
			value = NSNumber.numberWithInt (OS.NSMixedState);
		} else {
			value = NSNumber.numberWithInt (item.checked ? OS.NSOnState : OS.NSOffState);
		}
		return value.id;
	}
	for (int i=0; i<columnCount; i++) {
		if (columns [i].nsColumn.id == aTableColumn) {
			return item.createString (i).id;
		}
	}
	return item.createString (0).id;
}

@Override
boolean tableView_shouldReorderColumn_toColumn(long id, long sel, long aTableView, long currentColIndex, long newColIndex) {
	// Check column should never move and no column can be dragged to the left of it, if present.
	if ((style & SWT.CHECK) != 0) {
		if (currentColIndex == 0) return false;
		if (newColIndex == 0) return false;
	}

	NSTableView widget = new NSTableView(aTableView);
	id nsColumn = widget.tableColumns().objectAtIndex(currentColIndex);
	for (int i = 0; i < columnCount; i++) {
		if (columns[i].nsColumn.id == nsColumn.id) {
			return columns[i].movable;
		}
	}

	return true;
}

@Override
boolean tableView_shouldTrackCell_forTableColumn_row(long id, long sel, long table, long cell, /*long*/ long tableColumn, long rowIndex) {
	if ((style & SWT.CHECK) != 0) {
		if (new NSCell(cell).isKindOfClass(OS.class_NSButtonCell)) return true;
	}
	NSTableView widget = (NSTableView)view;
	return widget.isRowSelected(rowIndex);
}

@Override
void tableView_setObjectValue_forTableColumn_row (long id, long sel, long aTableView, long anObject, long aTableColumn, long rowIndex) {
	if (checkColumn != null && aTableColumn == checkColumn.id) {
		if (keyDown && (style & SWT.MULTI) != 0) {
			NSTableView widget = (NSTableView)view;
			NSIndexSet selection = widget.selectedRowIndexes ();
			int count = (int)selection.count ();
			long [] indices = new long [count];
			selection.getIndexes (indices, count, 0);
			for (int i = 0; i < indices.length; i++) {
				int index = (int)indices [i];
				TableItem item = items [index];
				toggleCheckedItem (item, index);
			}
		} else {
			TableItem item = items [(int)rowIndex];
			toggleCheckedItem (item, rowIndex);
		}
	}
}

private void toggleCheckedItem (TableItem item, long rowIndex) {
	item.checked = !item.checked;
	Event event = new Event ();
	event.detail = SWT.CHECK;
	event.item = item;
	event.index = (int)rowIndex;
	sendSelectionEvent (SWT.Selection, event, false);
	item.redraw (-1);
}

@Override
void tableView_willDisplayCell_forTableColumn_row (long id, long sel, long aTableView, long cell, long tableColumn, long rowIndex) {
	if (checkColumn != null && tableColumn == checkColumn.id) return;
	TableItem item = items [(int)rowIndex];
	int index = 0;
	for (int i=0; i<columnCount; i++) {
		if (columns [i].nsColumn.id == tableColumn) {
			index = i;
			break;
		}
	}
	NSTextFieldCell textCell = new NSTextFieldCell (cell);
	OS.object_setInstanceVariable(cell, Display.SWT_ROW, rowIndex);
	OS.object_setInstanceVariable(cell, Display.SWT_COLUMN, tableColumn);
	Image image = index == 0 ? item.image : (item.images == null ? null : item.images [index]);
	textCell.setImage (image != null ? image.handle : null);
	NSColor color;
	if (textCell.isEnabled()) {
		if (textCell.isHighlighted()) {
			color = NSColor.selectedControlTextColor();
		} else {
			Color foreground = item.cellForeground != null ? item.cellForeground [index] : null;
			if (foreground == null) foreground = item.foreground;
			if (foreground == null) foreground = getForegroundColor();
			color = NSColor.colorWithDeviceRed (foreground.handle [0], foreground.handle [1], foreground.handle [2], 1);
		}
	} else {
		color = NSColor.disabledControlTextColor();
	}
	int direction = (style & SWT.RIGHT_TO_LEFT) != 0 ? OS.NSWritingDirectionRightToLeft : OS.NSWritingDirectionLeftToRight;
	int alignment = OS.NSTextAlignmentLeft;
	if (columnCount > 0) {
		int style = columns [index].style;
		if ((style & SWT.CENTER) != 0) {
			alignment = OS.NSTextAlignmentCenter;
		} else if ((style & SWT.RIGHT) != 0) {
			alignment = OS.NSTextAlignmentRight;
		}
	}
	Font font = item.cellFont != null ? item.cellFont [index] : null;
	if (font == null) font = item.font;
	if (font == null) font = this.font;
	if (font == null) font = defaultFont ();
	if (font.extraTraits != 0) {
		NSMutableDictionary dict = ((NSMutableDictionary)new NSMutableDictionary().alloc()).initWithCapacity(5);
		dict.setObject (color, OS.NSForegroundColorAttributeName);
		dict.setObject (font.handle, OS.NSFontAttributeName);
		addTraits(dict, font);
		NSMutableParagraphStyle paragraphStyle = (NSMutableParagraphStyle)new NSMutableParagraphStyle ().alloc ().init ();
		paragraphStyle.setLineBreakMode (OS.NSLineBreakByClipping);
		paragraphStyle.setAlignment (alignment);
		paragraphStyle.setBaseWritingDirection(direction);
		dict.setObject (paragraphStyle, OS.NSParagraphStyleAttributeName);
		paragraphStyle.release ();
		NSAttributedString attribStr = ((NSAttributedString) new NSAttributedString ().alloc ()).initWithString (textCell.title(), dict);
		textCell.setAttributedStringValue(attribStr);
		attribStr.release();
		dict.release();
	} else {
		textCell.setFont(font.handle);
		textCell.setTextColor(color);
		textCell.setAlignment (alignment);
		textCell.setBaseWritingDirection(direction);
	}
}

@Override
boolean tableView_writeRowsWithIndexes_toPasteboard(long id, long sel, long arg0, long arg1, long arg2) {
	return sendMouseEvent(NSApplication.sharedApplication().currentEvent(), SWT.DragDetect, true);
}

void handleClickSelected() {
	/*
	 * When there are multiple selected items and one of them is clicked
	 * without modifiers, macOS supports two cases:
	 * 1) For single click, all other items are deselected
	 * 2) For double-click, selection stays as is, allowing to create
	 *    double-click event with multiple items.
	 * In order to distinguish between the two, macOS delays (1) by
	 * [NSEvent doubleClickInterval] in order to see if it's case (2).
	 * This causes SWT.Selection to occur after SWT.MouseUp.
	 *
	 * Bug 289483: For consistent cross-platform behavior, we want
	 * SWT.Selection to occur before SWT.MouseUp. The workaround is to
	 * implement (1) in SWT code and ignore the delayed selection event.
	 */

	int clickedRow = selectedRowIndex;
	selectedRowIndex = -1;

	if (clickedRow == -1) return;
	if (dragDetected) return;

	// Deselect all items except the clicked one
	NSTableView widget = (NSTableView)view;
	NSIndexSet selectedRows = widget.selectedRowIndexes ();
	int count = (int)selectedRows.count();
	long [] indexBuffer = new long [count];
	selectedRows.getIndexes(indexBuffer, count, 0);
	for (int i = 0; i < count; i++) {
		if (indexBuffer[i] == clickedRow) continue;
		ignoreSelect = true;
		widget.deselectRow (indexBuffer[i]);
		ignoreSelect = false;
	}

	// Bug 456602: It's possible that item is removed between mouse
	// down (where 'selectedRowIndex' was cached) and mouse up (current
	// code). In such case, all other items are still deselected, because
	// 1) without workaround, selection should have happened in mouse down,
	//    where item still existed
	// 2) clicking empty space deselects all items on macOS
	// If item is deleted, then pending selection is canceled by macOS, so
	// there's no need to ignore the next selection event.
	if (clickedRow >= itemCount) return;

	// Emulate SWT.Selection
	Event event = new Event ();
	event.item = _getItem(clickedRow);
	sendSelectionEvent (SWT.Selection, event, false);

	// Ignore real SWT.Selection that will arrive later
	ignoreSelect = true;
}

@Override
boolean sendMouseEvent(NSEvent nsEvent, int type, boolean send) {
	if (type == SWT.DragDetect) {
		dragDetected = true;
	} else if (type == SWT.MouseUp) {
		handleClickSelected();
		dragDetected = false;
	}

	return super.sendMouseEvent (nsEvent, type, send);
}

@Override
NSRect titleRectForBounds (long id, long sel, NSRect cellFrame) {
	NSImage image = new NSCell(id).image();
	if (image != null) {
		int imageWidth = imageBounds.width + IMAGE_GAP;
		cellFrame.x += imageWidth;
		cellFrame.width -= imageWidth;
	}
	return cellFrame;
}

@Override
void updateCursorRects (boolean enabled) {
	super.updateCursorRects (enabled);
	if (headerView == null) return;
	updateCursorRects (enabled, headerView);
}

void updateRowCount() {
	NSTableView widget = (NSTableView)view;
	setRedraw(false);
	ignoreSelect = true;
	widget.noteNumberOfRowsChanged ();
	ignoreSelect = false;
	widget.tile();
	setRedraw(true);
}

}
