/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

/** 
 * Instances of this class represent a selectable user interface
 * object that displays a list of strings and issues notification
 * when a string is selected.  A list may be single or multi select.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection</dd>
 * </dl>
 * <p>
 * Note: Only one of SINGLE and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#list">List snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class List extends Scrollable {
	NSTableColumn column;
	String [] items;
	int itemCount;
	boolean ignoreSelect, didSelect, rowsChanged, mouseIsDown;

	static int NEXT_ID;

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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public List (Composite parent, int style) {
	super (parent, checkStyle (style));
}

int /*long*/ accessibilityAttributeValue (int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {
//	if (attributeName.isEqualToString(OS.NSAccessibilityHeaderAttribute)) {
//		/*
//		* Bug in the Macintosh.  Even when the header is not visible,
//		* VoiceOver still reports each column header's role for every row.
//		* This is confusing and overly verbose.  The fix is to return
//		* "no header" when the screen reader asks for the header, by
//		* returning noErr without setting the event parameter.
//		*/
//		return 0;
//	}
	
	return super.accessibilityAttributeValue(id, sel, arg0);
}

/**
 * Adds the argument to the end of the receiver's list.
 *
 * @param string the new item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #add(String,int)
 */
public void add (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (itemCount == items.length) {
		String [] newItems = new String [itemCount + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	items [itemCount++] = string;
	updateRowCount();
	setScrollWidth(string);
}

/**
 * Adds the argument to the receiver's list at the given
 * zero-relative index.
 * <p>
 * Note: To add an item at the end of the list, use the
 * result of calling <code>getItemCount()</code> as the
 * index or use <code>add(String)</code>.
 * </p>
 *
 * @param string the new item
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #add(String)
 */
public void add (String string, int index) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	if (itemCount == items.length) {
		String [] newItems = new String [itemCount + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = string;
	updateRowCount();
	if (index != itemCount) fixSelection (index, true);
	setScrollWidth(string);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the user changes the receiver's selection, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the selection changes.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
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
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = 0;
	if (wHint == SWT.DEFAULT) {
		NSCell cell = column.dataCell ();
		Font font = this.font != null ? this.font : defaultFont ();
		cell.setFont (font.handle);
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				cell.setTitle (NSString.stringWith (items[i]));
				NSSize size = cell.cellSize ();
				width = Math.max (width, (int)Math.ceil (size.width));
			}
		}
		width += CELL_GAP;
	} else {
		width = wHint;
	}
	if (width <= 0) width = DEFAULT_WIDTH;
	int height = 0;
	if (hHint == SWT.DEFAULT) {
		int itemHeight = getItemHeight () + CELL_GAP;
		height = itemCount * itemHeight;
	} else {
		height = hHint;
	}
	if (height <= 0) height = DEFAULT_HEIGHT;
	Rectangle rect = computeTrim (0, 0, width, height);
	return new Point (rect.width, rect.height);
}

void createHandle () {
	NSScrollView scrollWidget = (NSScrollView)new SWTScrollView().alloc();
	scrollWidget.init();
	if ((style & SWT.H_SCROLL) != 0) scrollWidget.setHasHorizontalScroller(true);
	if ((style & SWT.V_SCROLL) != 0) scrollWidget.setHasVerticalScroller(true);
	scrollWidget.setAutohidesScrollers(true);
	scrollWidget.setBorderType((style & SWT.BORDER) != 0 ? OS.NSBezelBorder : OS.NSNoBorder);
	
	NSTableView widget = (NSTableView)new SWTTableView().alloc();
	widget.init();
	widget.setAllowsMultipleSelection((style & SWT.MULTI) != 0);
	widget.setDataSource(widget);
	widget.setHeaderView(null);
	widget.setDelegate(widget);
	if ((style & SWT.H_SCROLL) != 0) {
		widget.setColumnAutoresizingStyle (OS.NSTableViewNoColumnAutoresizing);
	}
	NSSize spacing = new NSSize();
	spacing.width = spacing.height = CELL_GAP;
	widget.setIntercellSpacing(spacing);
	widget.setDoubleAction(OS.sel_sendDoubleSelection);
	if (!hasBorder()) widget.setFocusRingType(OS.NSFocusRingTypeNone);
	
	column = (NSTableColumn)new NSTableColumn().alloc();
	column = column.initWithIdentifier(NSString.stringWith(String.valueOf(++NEXT_ID)));
	column.setWidth(0);
	widget.addTableColumn (column);
	
	scrollView = scrollWidget;
	view = widget;
}

void createWidget () {
	super.createWidget ();
	items = new String [4];
}

Color defaultBackground () {
	return display.getWidgetColor (SWT.COLOR_LIST_BACKGROUND);
}

NSFont defaultNSFont () {
	return display.tableViewFont;
}

Color defaultForeground () {
	return display.getWidgetColor (SWT.COLOR_LIST_FOREGROUND);
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
	checkWidget();
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
	checkWidget();
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

boolean dragDetect(int x, int y, boolean filter, boolean[] consume) {
	NSTableView widget = (NSTableView)view;
	NSPoint pt = new NSPoint();
	pt.x = x;
	pt.y = y;
	int /*long*/ row = widget.rowAtPoint(pt);
	if (row == -1) return false;
	boolean dragging = super.dragDetect(x, y, filter, consume);
	if (dragging) {
		if (!widget.isRowSelected(row)) {
			//TODO expand current selection when Shift, Command key pressed??
			NSIndexSet set = (NSIndexSet)new NSIndexSet().alloc();
			set = set.initWithIndex(row);
			widget.selectRowIndexes (set, false);
			set.release();
		}
	}
	consume[0] = dragging;
	return dragging;
}

void drawBackgroundInClipRect(int /*long*/ id, int /*long*/ sel, NSRect rect) {
	super.drawViewBackgroundInRect(id, sel, rect);
	if (id != view.id) return;
	fillBackground (view, NSGraphicsContext.currentContext(), rect, -1);
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

/**
 * Returns the zero-relative index of the item which currently
 * has the focus in the receiver, or -1 if no item has focus.
 *
 * @return the index of the selected item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getFocusIndex () {
	checkWidget();
	return (int)/*64*/((NSTableView)view).selectedRow();
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
public String getItem (int index) {
	checkWidget();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	return items [index];
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
	checkWidget();
	return itemCount;
}

/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the list.
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
	return (int)((NSTableView)view).rowHeight();
}

/**
 * Returns a (possibly empty) array of <code>String</code>s which
 * are the items in the receiver. 
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the items in the receiver's list
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String [] getItems () {
	checkWidget();
    String [] result = new String [itemCount];
	System.arraycopy (items, 0, result, 0, itemCount);
	return result;
}

/**
 * Returns an array of <code>String</code>s that are currently
 * selected in the receiver.  The order of the items is unspecified.
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
public String [] getSelection () {
	checkWidget ();
	NSTableView widget = (NSTableView)view;
	if (widget.numberOfSelectedRows() == 0) {
		return new String [0];
	}
	NSIndexSet selection = widget.selectedRowIndexes();
	int count = (int)/*64*/selection.count();
	int /*long*/ [] indexBuffer = new int /*long*/ [count];
	selection.getIndexes(indexBuffer, count, 0);
	String [] result = new String  [count];
	for (int i=0; i<count; i++) {
		result [i] = items [(int)/*64*/indexBuffer [i]];
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
	return (int)/*64*/((NSTableView)view).numberOfSelectedRows();
}

/**
 * Returns the zero-relative index of the item which is currently
 * selected in the receiver, or -1 if no item is selected.
 *
 * @return the index of the selected item or -1
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionIndex () {
	checkWidget();
	NSTableView widget = (NSTableView)view;
	if (widget.numberOfSelectedRows() == 0) {
		return -1;
	}
	NSIndexSet selection = widget.selectedRowIndexes();
	int count = (int)/*64*/selection.count();
	int /*long*/ [] result = new int /*long*/ [count];
	selection.getIndexes(result, count, 0);
	return (int)/*64*/result [0];
}

/**
 * Returns the zero-relative indices of the items which are currently
 * selected in the receiver.  The order of the indices is unspecified.
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
	int count = (int)/*64*/selection.count();
	int /*long*/ [] indices = new int /*long*/ [count];
	selection.getIndexes(indices, count, 0);
	int [] result = new int [count];
	for (int i = 0; i < result.length; i++) {
		result [i] = (int)/*64*/indices [i];
	}
	return result;
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
	checkWidget();
	//TODO - partial item at the top
	NSRect rect = scrollView.documentVisibleRect();
	NSPoint point = new NSPoint();
	point.x = rect.x;
	point.y = rect.y;
    int result = (int)/*64*/((NSTableView)view).rowAtPoint(point);
    if (result == -1) result = 0;
    return result;
}

/**
 * Gets the index of an item.
 * <p>
 * The list is searched starting at 0 until an
 * item is found that is equal to the search item.
 * If no item is found, -1 is returned.  Indexing
 * is zero based.
 *
 * @param string the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<itemCount; i++) {
		if (items [i].equals (string)) return i;
	}
	return -1;
}

/**
 * Searches the receiver's list starting at the given, 
 * zero-relative index until an item is found that is equal
 * to the argument, and returns the index of that item. If
 * no item is found or the starting index is out of range,
 * returns -1.
 *
 * @param string the search item
 * @param start the zero-relative index at which to start the search
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (String string, int start) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=start; i<itemCount; i++) {
		if (items [i].equals (string)) return i;
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
	checkWidget();
	if (!(0 <= index && index < itemCount)) return false;
	return ((NSTableView)view).isRowSelected(index);
}

/*
 * Feature in Cocoa: Table views do not change the selection when the user
 * right-clicks or control-clicks on an NSTableView or its subclasses. Fix is to select the 
 * clicked-on row ourselves.
 */
int /*long*/ menuForEvent(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	NSEvent event = new NSEvent(theEvent);
	NSTableView table = (NSTableView)view;
	
	// get the current selections for the outline view. 
	NSIndexSet selectedRowIndexes = table.selectedRowIndexes();
	
	// select the row that was clicked before showing the menu for the event
	NSPoint mousePoint = view.convertPoint_fromView_(event.locationInWindow(), null);
	int /*long*/ row = table.rowAtPoint(mousePoint);
	
	// figure out if the row that was just clicked on is currently selected
	if (selectedRowIndexes.containsIndex(row) == false) {
		NSIndexSet set = (NSIndexSet)new NSIndexSet().alloc();
		set = set.initWithIndex(row);
		table.selectRowIndexes (set, false);
		set.release();
	}
	// else that row is currently selected, so don't change anything.
	
	return super.menuForEvent(id, sel, theEvent);
}

void mouseDownSuper(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	ignoreSelect = false;
	NSTableView widget = (NSTableView)view;
	NSEvent nsEvent = new NSEvent(theEvent);
	NSPoint pt = view.convertPoint_fromView_(nsEvent.locationInWindow(), null);
	int row = (int)/*64*/widget.rowAtPoint(pt);
	if (row != -1 && (nsEvent.modifierFlags() & OS.NSDeviceIndependentModifierFlagsMask) == 0 && nsEvent.clickCount() == 1) {
		if (widget.isRowSelected(row) && widget.selectedRowIndexes().count() == 1) {
			if (0 <= row && row < itemCount) {
				sendSelection();
			}
		}
	}
	didSelect = false;
	super.mouseDownSuper(id, sel, theEvent);
	didSelect = false;
}

int /*long*/ numberOfRowsInTableView(int /*long*/ id, int /*long*/ sel, int /*long*/ aTableView) {
	return itemCount;
}

void releaseHandle () {
	super.releaseHandle ();
	if (column != null) column.release();
	column = null;
}

void releaseWidget () {	
	super.releaseWidget ();
	items = null;
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
	checkWidget();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	remove(index, true);
}

void remove (int index, boolean fixScroll) {
	if (index != itemCount - 1) fixSelection (index, false);
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	updateRowCount();
	if (fixScroll) setScrollWidth();
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
	checkWidget();
	if (start > end) return;
	if (!(0 <= start && start <= end && end < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int length = end - start + 1;
	for (int i=0; i<length; i++) remove (start, false);
	setScrollWidth();
}

/**
 * Searches the receiver's list starting at the first item
 * until an item is found that is equal to the argument, 
 * and removes that item from the list.
 *
 * @param string the item to remove
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the string is not found in the list</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int index = indexOf (string, 0);
	if (index == -1) error (SWT.ERROR_INVALID_ARGUMENT);
	remove (index);
}

/**
 * Removes the items from the receiver at the given
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
	int count = getItemCount ();
	if (!(0 <= start && start <= end && end < count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int last = -1;
	for (int i=0; i<newIndices.length; i++) {
		int index = newIndices [i];
		if (index != last) {
			remove (index, false);
			last = index;
		}
	}
	setScrollWidth();
}

/**
 * Removes all of the items from the receiver.
 * <p>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget();
	items = new String [4];
	itemCount = 0;
	updateRowCount();
	setScrollWidth();
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
 * @see #addSelectionListener
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);
}

/**
 * Selects the item at the given zero-relative index in the receiver's 
 * list.  If the item at the index was already selected, it remains
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
	checkWidget();
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
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see List#setSelection(int,int)
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
 * @see List#setSelection(int[])
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

void sendDoubleSelection() {
	if (((NSTableView)view).clickedRow () != -1) {
		sendSelectionEvent (SWT.DefaultSelection);
	}
}

boolean sendKeyEvent (NSEvent nsEvent, int type) {
	boolean result = super.sendKeyEvent (nsEvent, type);
	if (!result) return result;
	if (type != SWT.KeyDown) return result;
	short keyCode = nsEvent.keyCode ();
	switch (keyCode) {
		case 76: /* KP Enter */
		case 36: { /* Return */
			sendSelectionEvent (SWT.DefaultSelection);
			break;
		}
	}
	return result;
}

boolean sendMouseEvent (NSEvent nsEvent, int type, boolean send) {
	if (type == SWT.MouseDown) {
		mouseIsDown = true;
	} else if (type == SWT.MouseUp || type == SWT.DragDetect) {
		mouseIsDown = false;
		
		if (rowsChanged) {
			rowsChanged = false;
			((NSTableView)view).noteNumberOfRowsChanged();
		}
	}
	return super.sendMouseEvent(nsEvent, type, send);
}

void sendSelection () {
	if (ignoreSelect) return;
	sendSelectionEvent(SWT.Selection);
}

void setBackgroundColor(NSColor nsColor) {
	((NSTableView) view).setBackgroundColor (nsColor);
}

void setFont (NSFont font) {
	super.setFont (font);
	float /*double*/ ascent = font.ascender ();
	float /*double*/ descent = -font.descender () + font.leading ();
	((NSTableView)view).setRowHeight ((int)Math.ceil (ascent + descent) + 1);
	setScrollWidth();
}

/**
 * Sets the text of the item in the receiver's list at the given
 * zero-relative index to the string argument.
 *
 * @param index the index for the item
 * @param string the new text for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setItem (int index, String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	items [index] = string;
	NSTableView tableView = (NSTableView)view;
	NSRect rect = tableView.rectOfRow (index);
	tableView.setNeedsDisplayInRect (rect);
	setScrollWidth(string);
}

/**
 * Sets the receiver's items to be the given array of items.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if an item in the items array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setItems (String [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<items.length; i++) {
		if (items [i] == null) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	this.items = new String [items.length];
	System.arraycopy (items, 0, this.items, 0, items.length);
	itemCount = items.length;
	((NSTableView)view).reloadData();
	setScrollWidth();
}

boolean setScrollWidth (String item) {
	if ((style & SWT.H_SCROLL) == 0) return false;
	NSCell cell = column.dataCell ();
	Font font = this.font != null ? this.font : defaultFont ();
	cell.setFont (font.handle);
	cell.setTitle (NSString.stringWith (item));
	NSSize size = cell.cellSize ();
	float /*double*/ oldWidth = column.width ();
	if (oldWidth < size.width) {
		column.setWidth (size.width);
		return true;
	}
	return false;
}

boolean setScrollWidth () {
	if ((style & SWT.H_SCROLL) == 0) return false;
	if (items == null) return false;
	NSCell cell = column.dataCell ();
	Font font = this.font != null ? this.font : defaultFont ();
	cell.setFont (font.handle);
	float /*double*/ width = 0;
	for (int i = 0; i < itemCount; i++) {
		cell.setTitle (NSString.stringWith (items[i]));
		NSSize size = cell.cellSize ();
		width = Math.max (width, size.width);
	}
	column.setWidth (width);
	return true;
}

/**
 * Selects the item at the given zero-relative index in the receiver. 
 * If the item at the index was already selected, it remains selected.
 * The current selection is first cleared, then the new item is selected.
 * Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @see List#deselectAll()
 * @see List#select(int)
 */
public void setSelection (int index) {
	checkWidget();
	deselectAll ();
	if (0 <= index && index < itemCount) {
		NSIndexSet set = (NSIndexSet)new NSIndexSet().alloc();
		set = set.initWithIndex(index);
		NSTableView widget = (NSTableView)view;
		ignoreSelect = true;
		widget.selectRowIndexes(set, false);
		ignoreSelect = false;
		set.release();
		showIndex (index);
	}
}

/**
 * Selects the items in the range specified by the given zero-relative
 * indices in the receiver. The range of indices is inclusive.
 * The current selection is cleared before the new items are selected.
 * <p>
 * Indices that are out of range are ignored and no items will be selected
 * if start is greater than end.
 * If the receiver is single-select and there is more than one item in the
 * given range, then all indices are ignored.
 *
 * @param start the start index of the items to select
 * @param end the end index of the items to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see List#deselectAll()
 * @see List#select(int,int)
 */
public void setSelection (int start, int end) {
	checkWidget ();
	deselectAll ();
	if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) return;
	if (itemCount == 0 || start >= itemCount) return;
	start = Math.max (0, start);
	end = Math.min (end, itemCount - 1);
	NSRange range = new NSRange();
	range.location = start;
	range.length = end - start + 1;
	NSIndexSet set = (NSIndexSet)new NSIndexSet().alloc();
	set = set.initWithIndexesInRange(range);
	NSTableView widget = (NSTableView)view;
	ignoreSelect = true;
	widget.selectRowIndexes(set, false);
	ignoreSelect = false;
	set.release();
	showIndex(end);
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * The current selection is cleared before the new items are selected.
 * <p>
 * Indices that are out of range and duplicate indices are ignored.
 * If the receiver is single-select and multiple indices are specified,
 * then all indices are ignored.
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
 * @see List#deselectAll()
 * @see List#select(int[])
 */
public void setSelection (int [] indices) {
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	deselectAll ();
	int length = indices.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	int [] newIndices = new int [length];
	int count = 0;
	for (int i=0; i<length; i++) {
		int index = indices [length - i - 1];
		if (index >= 0 && index < itemCount) {
			newIndices [count++] = index;
		}
	}
	if (count > 0) {
		select (newIndices, count, true);
		showIndex (newIndices [0]);
	}
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selection is cleared before the new items are selected.
 * <p>
 * Items that are not in the receiver are ignored.
 * If the receiver is single-select and multiple items are specified,
 * then all items are ignored.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of items is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see List#deselectAll()
 * @see List#select(int[])
 * @see List#setSelection(int[])
 */
public void setSelection (String [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	deselectAll ();
	int length = items.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	int count = 0;
	int [] indices = new int [length];
	for (int i=0; i<length; i++) {
		String string = items [length - i - 1];
		if ((style & SWT.SINGLE) != 0) {
			int index = indexOf (string, 0);
			if (index != -1) {
				count = 1;
				indices = new int [] {index};
			}
		} else {
			int index = 0;
			while ((index = indexOf (string, index)) != -1) {
				if (count == indices.length) {
					int [] newIds = new int [indices.length + 4];
					System.arraycopy (indices, 0, newIds, 0, indices.length);
					indices = newIds;
				}
				indices [count++] = index;
				index++;
			}
		}
	}
	if (count > 0) {
		select (indices, count, true);
		showIndex (indices [0]);
	}
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
	checkWidget();
	NSTableView widget = (NSTableView) view;
	int row = Math.max(0, Math.min(index, itemCount));
	NSPoint pt = new NSPoint();
	pt.x = scrollView.contentView().bounds().x;
	pt.y = widget.frameOfCellAtColumn(0, row).y;
	view.scrollPoint(pt);
}

void showIndex (int index) {
	if (0 <= index && index < itemCount) {
		((NSTableView)view).scrollRowToVisible(index);
	}
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
 */
public void showSelection () {
	checkWidget();
	int index = getSelectionIndex ();
	if (index >= 0) showIndex (index);
}

void tableViewSelectionDidChange (int /*long*/ id, int /*long*/ sel, int /*long*/ aNotification) {
	if (didSelect) return;
	sendSelection();
}

void tableViewSelectionIsChanging (int /*long*/ id, int /*long*/ sel, int /*long*/ aNotification) {
	didSelect = true;
	sendSelection();
}

int /*long*/ tableView_objectValueForTableColumn_row(int /*long*/ id, int /*long*/ sel, int /*long*/ aTableView, int /*long*/ aTableColumn, int /*long*/ rowIndex) {
	float /*double*/ [] fg = ((NSTableView)view).isRowSelected(rowIndex) ? null : foreground;
	NSAttributedString attribStr = createString(items[(int)/*64*/rowIndex], null, fg, SWT.LEFT, false, getEnabled(), false);
	attribStr.autorelease();
	return attribStr.id;
}

int /*long*/ tableView_selectionIndexesForProposedSelection (int /*long*/ id, int /*long*/ sel, int /*long*/ aTableView, int /*long*/ indexSet) {
	if ((style & SWT.SINGLE) != 0) {
		/*
		 * Feature in Cocoa.  Calling setAllowsEmptySelection will automatically select the first row of the list. 
		 * And, single-selection NSTable/OutlineViews allow the user to de-select the selected item via command-click.
		 * This is normal platform behavior, but for compatibility with other platforms, if the SINGLE style is in use,
		 * force a selection by seeing if the proposed selection set is empty, and if so, put back the currently selected row.  
		 */
		NSIndexSet indexes = new NSIndexSet(indexSet);
		NSTableView table = new NSTableView(aTableView);			
		if (indexes.count() != 1 && table.selectedRow() != -1) {
			NSIndexSet newSelection = (NSIndexSet)new NSIndexSet().alloc();
			newSelection = newSelection.initWithIndex(table.selectedRow());
			newSelection.autorelease();
			return newSelection.id;
		}
	}
	
	return indexSet;
}

void updateRowCount() {	
	/*
	 * Feature in Cocoa. Changing the row count while the mouse is tracking will confuse the code that calculates the 
	 * current selection.  Fix is to not call noteNumberOfRowsChanged if the mouse is down.
	 */
	if (mouseIsDown) {
		rowsChanged = true;
	} else {
		NSTableView widget = (NSTableView)view;
		setRedraw(false);
		ignoreSelect = true;
		widget.noteNumberOfRowsChanged ();
		ignoreSelect = false;
		widget.tile();
		setRedraw(true);
	}
}

}
