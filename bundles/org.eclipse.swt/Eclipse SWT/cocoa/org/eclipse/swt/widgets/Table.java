/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.cocoa.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

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
 * Here is an example of using a <code>Table</code> with style <code>VIRTUAL</code>:
 * <code><pre>
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
 * </pre></code>
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, HIDE_SELECTION, VIRTUAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, SetData, MeasureItem, EraseItem, PaintItem</dd>
 * </dl>
 * </p><p>
 * Note: Only one of the styles SINGLE, and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Table extends Composite {
	TableItem [] items;
	TableColumn [] columns;
	TableColumn sortColumn;
	TableItem currentItem;
	NSTableHeaderView headerView;
	NSTableColumn firstColumn, checkColumn;
	int columnCount, itemCount, lastIndexOf, sortDirection;
	boolean ignoreSelect;

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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Table (Composite parent, int style) {
	super (parent, checkStyle (style));
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

boolean checkData (TableItem item, boolean redraw) {
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
		if (redraw) {
//			if (!setScrollWidth (item)) item.redraw (OS.kDataBrowserNoItem);
		}
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
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}

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
	checkWidget();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	TableItem item = items [index];
	if (item != null) {
		if (currentItem != item) item.clear ();
		if (currentItem == null && drawCount == 0) {
			int [] id = new int [] {index + 1};
//			OS.UpdateDataBrowserItems (handle, 0, id.length, id, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
		}
//		setScrollWidth (item);
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
	checkWidget();
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
	checkWidget();
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
	checkWidget();
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
		if (item != null) item.clear ();
	}
	if (currentItem == null && drawCount == 0) {
//		OS.UpdateDataBrowserItems (handle, 0, 0, null, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
	}
//	setScrollWidth (items, true);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = 0;
	if (wHint == SWT.DEFAULT) {
		if (columnCount != 0) {
			for (int i=0; i<columnCount; i++) {
				width += columns [i].getWidth ();
			}
		} else {
			int columnWidth = 0;
			GC gc = new GC (this);
			for (int i=0; i<itemCount; i++) {
				TableItem item = items [i];
				if (item != null) {
					columnWidth = Math.max (columnWidth, item.calculateWidth (0, gc));
				}
			}
			gc.dispose ();
			width += columnWidth + getInsetWidth ();
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

void createHandle () {
	//TODO - SWT.CHECK	
	SWTScrollView scrollWidget = (SWTScrollView)new SWTScrollView().alloc();
	scrollWidget.initWithFrame(new NSRect ());
	scrollWidget.setHasHorizontalScroller(true);
	scrollWidget.setHasVerticalScroller(true);
	scrollWidget.setAutohidesScrollers(true);
	scrollWidget.setBorderType(hasBorder() ? OS.NSBezelBorder : OS.NSNoBorder);
	scrollWidget.setTag(jniRef);
	
	NSTableView widget = (NSTableView)new SWTTableView().alloc();
	widget.initWithFrame(new NSRect());
	widget.setAllowsMultipleSelection((style & SWT.MULTI) != 0);
	widget.setDataSource(widget);
	widget.setDelegate(widget);
	widget.setDoubleAction(OS.sel_sendDoubleSelection);
	if (!hasBorder()) widget.setFocusRingType(OS.NSFocusRingTypeNone);
	widget.setTag(jniRef);
	
	headerView = widget.headerView();
	headerView.retain();
	widget.setHeaderView(null);
	
	NSString str = NSString.stringWith("");
	if ((style & SWT.CHECK) != 0) {
		checkColumn = (NSTableColumn)new NSTableColumn().alloc();
		checkColumn.initWithIdentifier(str);
		checkColumn.headerCell().setTitle(str);
		widget.addTableColumn (checkColumn);
		NSButtonCell cell = (NSButtonCell)new NSButtonCell().alloc().init();
		checkColumn.setDataCell(cell);
		cell.setButtonType(OS.NSSwitchButton);
		cell.setImagePosition(OS.NSImageOnly);
		cell.setAllowsMixedState(true);
		cell.release();
		checkColumn.setWidth(getCheckColumnWidth());
		checkColumn.setResizingMask(OS.NSTableColumnNoResizing);
		checkColumn.setEditable(false);
	}

	firstColumn = (NSTableColumn)new NSTableColumn().alloc();
	firstColumn.initWithIdentifier(str);
	//column.setResizingMask(OS.NSTableColumnAutoresizingMask);
	NSCell cell = (NSBrowserCell)new NSBrowserCell().alloc().init();
	firstColumn.setDataCell(cell);
	cell.release();
	widget.addTableColumn (firstColumn);

	scrollView = scrollWidget;
	view = widget;
	scrollView.setDocumentView(widget);
	parent.contentView().addSubview_(scrollView);
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
		firstColumn = null;
	} else {
		//TODO - set attributes, alignment etc.
		nsColumn = (NSTableColumn)new NSTableColumn().alloc();
		nsColumn.initWithIdentifier(NSString.stringWith(""));
		((NSTableView)view).addTableColumn (nsColumn);
		int checkColumn = (style & SWT.CHECK) != 0 ? 1 : 0;
		((NSTableView)view).moveColumn (columnCount + checkColumn, index + checkColumn);
		NSCell cell = (NSBrowserCell)new NSBrowserCell().alloc().init();
		nsColumn.setDataCell(cell);
		cell.release();
	}
	column.nsColumn = nsColumn;
	nsColumn.headerCell().setTitle(NSString.stringWith(""));
	nsColumn.setWidth(0);
	System.arraycopy (columns, index, columns, index + 1, columnCount++ - index);
	columns [index] = column;
	if (columnCount > 1) {
		for (int i=0; i<itemCount; i++) {
			TableItem item = items [i];
			if (item != null) {
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
		}
	}
}

void createItem (TableItem item, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	if (itemCount == items.length) {
		/* Grow the array faster when redraw is off */
		int length = drawCount == 0 ? items.length + 4 : Math.max (4, items.length * 3 / 2);
		TableItem [] newItems = new TableItem [length];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
	//TODO - use noteNumberOfRowsChanged?
	((NSTableView)view).reloadData();
}

void createWidget () {
	super.createWidget ();
	items = new TableItem [4];
	columns = new TableColumn [4];
}

Color defaultBackground () {
	return display.getSystemColor (SWT.COLOR_LIST_BACKGROUND);
}

Color defaultForeground () {
	return display.getSystemColor (SWT.COLOR_LIST_FOREGROUND);
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
	//TODO - check range
	if (start == 0 && end == itemCount - 1) {
		deselectAll ();
	} else {
		int length = end - start + 1;
		NSTableView widget = (NSTableView)view;
		ignoreSelect = true;
		for (int i=0; i<length; i++) {
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
	if (columnCount == 1) {
		//TODO - reset attributes
		firstColumn = column.nsColumn;
		firstColumn.setWidth (0);
	} else {
		((NSTableView)view).removeTableColumn(column.nsColumn);
	}
	System.arraycopy (columns, index + 1, columns, index, --columnCount - index);
	columns [columnCount] = null;
	for (int i=index; i<columnCount; i++) {
		columns [i].sendEvent (SWT.Move);
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
	((NSTableView)view).noteNumberOfRowsChanged();
	if (itemCount == 0) {
		setTableEmpty ();
	} else {
//		fixScrollBar ();
	}
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
			selection [newIndex] = selection [i] + 1;
			if (selection [newIndex] - 1 >= index) {
				selection [newIndex] += add ? 1 : -1;
				fix = true;
			}
		}
	}
	if (fix) select (selection, newCount, true);
}

int getCheckColumnWidth () {
	return 20; //TODO - compute width
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
	int [] position = new int [1];
	for (int i=0; i<columnCount; i++) {
		TableColumn column = columns [i];
//		OS.GetDataBrowserTableViewColumnPosition (handle, column.id, position);
//		if ((style & SWT.CHECK) != 0) position [0] -= 1;
		order [position [0]] = i;
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
 * Returns the width in pixels of a grid line.
 *
 * @return the width of a grid line in pixels
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

int getInsetWidth () {
	//TODO - wrong
	return 20;
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
//	checkItems (true);
//	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
//	Rect rect = new Rect ();
//	org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
//	OS.SetPt (pt, (short) point.x, (short) point.y);
//	if (0 < lastHittest && lastHittest <= itemCount && lastHittestColumn != 0) {
//		if (OS.GetDataBrowserItemPartBounds (handle, lastHittest, lastHittestColumn, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
//			if (rect.top <= pt.v && pt.v <= rect.bottom) {
//				if ((style & SWT.FULL_SELECTION) != 0) {
//					return _getItem (lastHittest - 1);
//				} else {
//					return OS.PtInRect (pt, rect) ? _getItem (lastHittest - 1) : null;
//				}
//			}
//		}
//			
//	}
//	int [] top = new int [1], left = new int [1];
//    OS.GetDataBrowserScrollPosition(handle, top, left);
//	short [] height = new short [1];
//	OS.GetDataBrowserTableViewRowHeight (handle, height);
//	short [] header = new short [1];
//	OS.GetDataBrowserListViewHeaderBtnHeight (handle, header);
//	int [] offsets = new int [] {0, 1, -1};
//	for (int i = 0; i < offsets.length; i++) {
//		int index = (top[0] - header [0] + point.y) / height [0] + offsets [i];
//		if (0 <= index && index < itemCount) {
//			if (columnCount == 0) {
//				if (OS.GetDataBrowserItemPartBounds (handle, index + 1, column_id, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
//					if (rect.top <= pt.v && pt.v <= rect.bottom) {
//						if ((style & SWT.FULL_SELECTION) != 0) {
//							return _getItem (index);
//						} else {
//							return OS.PtInRect (pt, rect) ? _getItem (index) : null;
//						}
//					}
//				}
//			} else {
//				for (int j = 0; j < columnCount; j++) {
//					if (OS.GetDataBrowserItemPartBounds (handle, index + 1, columns [j].id, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
//						if (rect.top <= pt.v && pt.v <= rect.bottom) {
//							if ((style & SWT.FULL_SELECTION) != 0) {
//								return _getItem (index);
//							} else {
//								return OS.PtInRect (pt, rect) ? _getItem (index) : null;
//							}
//						}
//					}
//				}
//			}
//		}
//	}
//	//TODO - optimize
//	for (int i=0; i<itemCount; i++) {
//		if (columnCount == 0) {
//			if (OS.GetDataBrowserItemPartBounds (handle, i + 1, column_id, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
//				if (rect.top <= pt.v && pt.v <= rect.bottom) {
//					if ((style & SWT.FULL_SELECTION) != 0) {
//						return _getItem (i);
//					} else {
//						return OS.PtInRect (pt, rect) ? _getItem (i) : null;
//					}
//				}
//			}
//		} else {
//			for (int j = 0; j < columnCount; j++) {
//				if (OS.GetDataBrowserItemPartBounds (handle, i + 1, columns [j].id, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
//					if (rect.top <= pt.v && pt.v <= rect.bottom) {
//						if ((style & SWT.FULL_SELECTION) != 0) {
//							return _getItem (i);
//						} else {
//							return OS.PtInRect (pt, rect) ? _getItem (i) : null;
//						}
//					}
//				}
//			}
//		}
//	}
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
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the receiver's.
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
 * and <code>false</code> otherwise.
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
//	if (OS.VERSION >= 0x1040) {
//		int [] attrib = new int [1];
//		OS.DataBrowserGetAttributes (handle, attrib);
//		return (attrib [0] & (OS.kDataBrowserAttributeListViewAlternatingRowColors | OS.kDataBrowserAttributeListViewDrawColumnDividers)) != 0;
//	}
	return false;
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
	int count = selection.count();
	int [] indexBuffer = new int [count];
	selection.getIndexes(indexBuffer, count, 0);
	TableItem [] result = new TableItem  [count];
	for (int i=0; i<count; i++) {
		result [i] = _getItem (indexBuffer [i]);
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
	return ((NSTableView)view).numberOfSelectedRows();
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
	checkWidget();
	//TODO - check empty selection case
	return ((NSTableView)view).selectedRow();
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
	int count = selection.count();
	int [] result = new int [count];
	selection.getIndexes(result, count, 0);
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
	checkWidget();
	//TODO - partial item at the top
	NSRect rect = scrollView.documentVisibleRect();
	NSPoint point = new NSPoint();
	point.x = rect.x;
	point.y = rect.y;
    return ((NSTableView)view).rowAtPoint(point);
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
	checkWidget();
	//TODO - range check
	return ((NSTableView)view).isRowSelected(index);
}

int numberOfRowsInTableView(int aTableView) {
	return itemCount;
}

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

void releaseHandle () {
	super.releaseHandle ();
	if (headerView != null) headerView.release();
	headerView = null;
	if (firstColumn != null) firstColumn.release();
	firstColumn = null;
	if (checkColumn != null) checkColumn.release();
	checkColumn = null;
}

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
	checkWidget();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	TableItem item = items [index];
	if (item != null) item.release (false);
	if (index != itemCount - 1) fixSelection (index, false);
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	((NSTableView)view).noteNumberOfRowsChanged();
	if (itemCount == 0) {
		setTableEmpty ();
	} else {
//		fixScrollBar ();
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
	checkWidget();
	if (start > end) return;
	if (!(0 <= start && start <= end && end < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (start == 0 && end == itemCount - 1) {
		removeAll ();
	} else {
		int length = end - start + 1;
		for (int i=0; i<length; i++) remove (start);
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
			remove (index);
			last = index;
		}
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
	checkWidget();
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
		if (item != null && !item.isDisposed ()) item.release (false);
	}
	setTableEmpty ();
	((NSTableView)view).noteNumberOfRowsChanged();
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
	checkWidget();
	if (0 <= index && index < itemCount) {
		NSIndexSet indexes = (NSIndexSet)new NSIndexSet().alloc();
		indexes.initWithIndex(index);
		NSTableView widget = (NSTableView)view;
		ignoreSelect = true;
		((NSTableView)view).selectRowIndexes(indexes, true);
		ignoreSelect = false;
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
		int length = end - start + 1;
		NSIndexSet indexes = (NSIndexSet)new NSIndexSet().alloc();
		NSRange range = new NSRange();
		range.location = start;
		range.length = length;
		indexes.initWithIndexesInRange(range);
		NSTableView widget = (NSTableView)view;
		ignoreSelect = true;
		widget.selectRowIndexes(indexes, true);
		ignoreSelect = false;
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
	NSMutableIndexSet indexes = (NSMutableIndexSet)new NSMutableIndexSet().alloc().init();
	for (int i=0; i<length; i++) {
		int index = indices [length - i - 1];
		if (index >= 0 && index < itemCount) {
			indexes.addIndex (indices [i]);
			count++;
		}
	}
	if (count > 0) {
		NSTableView widget = (NSTableView)view;
		ignoreSelect = true;
		widget.selectRowIndexes(indexes, true);
		ignoreSelect = false;
	}
}

void select (int [] ids, int count, boolean clear) {
	NSMutableIndexSet indexes = (NSMutableIndexSet)new NSMutableIndexSet().alloc().init();
	for (int i=0; i<count; i++) indexes.addIndex (ids [i] - 1); //WRONG -1
	NSTableView widget = (NSTableView)view;
	ignoreSelect = true;
	widget.selectRowIndexes(indexes, !clear);
	ignoreSelect = false;
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
		int x = 0;
		short [] width = new short [1];
		int [] oldX = new int [oldOrder.length];
		for (int i=0; i<oldOrder.length; i++) {
			int index = oldOrder [i];
			TableColumn column = columns [index];
			oldX [index] =  x;
//			OS.GetDataBrowserTableViewNamedColumnWidth(handle, column.id, width);
			x += width [0];
		}
		x = 0;
		int [] newX = new int [order.length];
		for (int i=0; i<order.length; i++) {
			int index = order [i];
			TableColumn column = columns [index];
			int position = (style & SWT.CHECK) != 0 ? i + 1 : i;
//			OS.SetDataBrowserTableViewColumnPosition(handle, column.id, position);
//			column.lastPosition = position;
			newX [index] =  x;
//			OS.GetDataBrowserTableViewNamedColumnWidth(handle, column.id, width);
			x += width [0];
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
//	checkItems (true);
//	count = Math.max (0, count);
//	if (count == itemCount) return;
//	setRedraw (false);
//    int[] top = new int [1], left = new int [1];
//    OS.GetDataBrowserScrollPosition (handle, top, left);
//    DataBrowserCallbacks callbacks = new DataBrowserCallbacks ();
//	OS.GetDataBrowserCallbacks (handle, callbacks);
//	callbacks.v1_itemNotificationCallback = 0;
//	callbacks.v1_itemCompareCallback = 0;
//	OS.SetDataBrowserCallbacks (handle, callbacks);
//	if (count < itemCount) {
//		int index = count;
//		int[] id = new int [itemCount - count];
//		while (index < itemCount) {
//			TableItem item = items [index];
//			if (item != null) item.release (false);
//			id [index-count] = index + 1;
//			index++;
//		}
//		OS.RemoveDataBrowserItems (handle, OS.kDataBrowserNoItem, id.length, id, 0);
//		int [] newItemCount = new int [1];
//		if (OS.GetDataBrowserItemCount (handle, OS.kDataBrowserNoItem, true, OS.kDataBrowserItemAnyState, newItemCount) != OS.noErr) {
//			error (SWT.ERROR_CANNOT_GET_COUNT);
//		}
//		if (count != newItemCount[0]) error (SWT.ERROR_ITEM_NOT_REMOVED);
//	}
//	int length = Math.max (4, (count + 3) / 4 * 4);
//	TableItem [] newItems = new TableItem [length];
//	System.arraycopy (items, 0, newItems, 0, Math.min (count, itemCount));
//	items = newItems;
//	if ((style & SWT.VIRTUAL) == 0) {
//		for (int i=itemCount; i<count; i++) {
//			items [i] = new TableItem (this, SWT.NONE, i, false);
//		}
//	}
//	itemCount = count;
//	OS.AddDataBrowserItems (handle, 0, itemCount, null, OS.kDataBrowserItemNoProperty);
//	callbacks.v1_itemNotificationCallback = display.itemNotificationProc;
//	callbacks.v1_itemCompareCallback = itemCompareProc ();
//	OS.SetDataBrowserCallbacks (handle, callbacks);
//	fixScrollBar ();
//	setRedraw (true);
}

/*public*/ void setItemHeight (int itemHeight) {
	checkWidget ();
	if (itemHeight < -1) error (SWT.ERROR_INVALID_ARGUMENT);
	if (itemHeight == -1) {
		//TODO - reset item height, ensure other API's such as setFont don't do this
	} else {
//		OS.SetDataBrowserTableViewRowHeight (handle, (short) itemHeight);
	}
}

/**
 * Marks the receiver's lines as visible if the argument is <code>true</code>,
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
public void setLinesVisible (boolean show) {
	checkWidget ();
	((NSTableView)view).setUsesAlternatingRowBackgroundColors(show);
}

boolean setScrollWidth (TableItem item) {
	if (columnCount != 0) return false;
	if (currentItem != null) {
//		if (currentItem != item) fixScrollWidth = true;
		return false;
	}
	if (drawCount != 0) return false;
	GC gc = new GC (this);
	int newWidth = item.calculateWidth (0, gc);
	gc.dispose ();
	newWidth += getInsetWidth ();
//	short [] width = new short [1];
//	OS.GetDataBrowserTableViewNamedColumnWidth (handle, column_id, width);
//	if (width [0] < newWidth) {
//		OS.SetDataBrowserTableViewNamedColumnWidth (handle, column_id, (short) newWidth);
//		return true;
//	}
	if (firstColumn.width() < newWidth) {
		firstColumn.setWidth (newWidth);
	}
	return false;
}

boolean setScrollWidth (TableItem [] items, boolean set) {
	if (columnCount != 0) return false;
	if (currentItem != null) {
//		fixScrollWidth = true;
		return false;
	}
	if (drawCount != 0) return false;
	GC gc = new GC (this);
	int newWidth = 0;
	for (int i = 0; i < items.length; i++) {
		TableItem item = items [i];
		if (item != null) {
			newWidth = Math.max (newWidth, item.calculateWidth (0, gc));
		}
	}
	gc.dispose ();
	newWidth += getInsetWidth ();
//	if (!set) {
//		short [] width = new short [1];
//		OS.GetDataBrowserTableViewNamedColumnWidth (handle, column_id, width);
//		if (width [0] >= newWidth) return false;
//	}
//	OS.SetDataBrowserTableViewNamedColumnWidth (handle, column_id, (short) newWidth);
	if (!set) {
		if (firstColumn.width() > newWidth) return false;
	}
	firstColumn.setWidth (newWidth);
	return true;
}

/**
 * Selects the item at the given zero-relative index in the receiver. 
 * The current selection is first cleared, then the new item is selected.
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
	checkWidget();
	//TODO - optimize to use expand flag
	deselectAll ();
	setSelection (index, false);
}

void setSelection (int index, boolean notify) {
//	checkWidget();
	if (0 <= index && index < itemCount) {
		select (index);
		showIndex (index);
		if (notify) {
			Event event = new Event ();
			event.item = _getItem (index);
			postEvent (SWT.Selection, event);
		}
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
 * The current selection is cleared before the new items are selected.
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
 * The current selection is cleared before the new item is selected.
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
 * The current selection is cleared before the new items are selected.
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
		showIndex (indices [0] - 1);
	}
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
//	DataBrowserCallbacks callbacks = new DataBrowserCallbacks ();
//	OS.GetDataBrowserCallbacks (handle, callbacks);
//	callbacks.v1_itemCompareCallback = display.itemCompareProc;
//	OS.SetDataBrowserCallbacks (handle, callbacks);
//	if (column == null) {
//		if (sortColumn != null  && !sortColumn.isDisposed ()  && sortDirection != SWT.NONE) {
//			OS.SetDataBrowserSortOrder (handle, (short) OS.kDataBrowserOrderIncreasing);
//			sortColumn = null; 
//			OS.SetDataBrowserSortProperty (handle, 0);
//		}
//	}
//	sortColumn = column;
//	if (sortColumn != null  && !sortColumn.isDisposed () && sortDirection != SWT.NONE) {
//		OS.SetDataBrowserSortProperty (handle, sortColumn.id);
//		int order = sortDirection == SWT.DOWN ? OS.kDataBrowserOrderDecreasing : OS.kDataBrowserOrderIncreasing;
//		OS.SetDataBrowserSortOrder (handle, (short) order);
//	}
//	callbacks.v1_itemCompareCallback = itemCompareProc ();
//	OS.SetDataBrowserCallbacks (handle, callbacks);
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
	sortDirection = direction;
//	DataBrowserCallbacks callbacks = new DataBrowserCallbacks ();
//	OS.GetDataBrowserCallbacks (handle, callbacks);
//	callbacks.v1_itemCompareCallback = display.itemCompareProc;
//	OS.SetDataBrowserCallbacks (handle, callbacks);
//	if (sortColumn != null && !sortColumn.isDisposed ()) {
//		if (sortDirection == SWT.NONE) {
//			OS.SetDataBrowserSortOrder (handle, (short) OS.kDataBrowserOrderIncreasing);
//			TableColumn column = sortColumn;
//			sortColumn = null; 
//			OS.SetDataBrowserSortProperty (handle, 0);
//			sortColumn = column;
//		} else {
//			OS.SetDataBrowserSortProperty (handle, 0);
//			OS.SetDataBrowserSortProperty (handle, sortColumn.id);
//			int order = sortDirection == SWT.DOWN ? OS.kDataBrowserOrderDecreasing : OS.kDataBrowserOrderIncreasing;
//			OS.SetDataBrowserSortOrder (handle, (short) order);
//		}
//	}
//	callbacks.v1_itemCompareCallback = itemCompareProc ();
//	OS.SetDataBrowserCallbacks (handle, callbacks);
}

void setTableEmpty () {
	itemCount = 0;
	items = new TableItem [4];
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
	NSRect rect = ((NSTableView)view).rectOfRow(index);
	((NSTableView)view).scrollRectToVisible(rect);
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
	int index = indexOf (column);
	if (columnCount <= 1 || !(0 <= index && index < columnCount)) return;
	((NSTableView)view).scrollColumnToVisible(index + ((style & SWT.CHECK) != 0 ? 1 : 0));
}

void showIndex (int index) {
	if (0 <= index && index < itemCount) {
		((NSTableView)view).scrollRowToVisible(index);
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
	checkWidget();
	int index = getSelectionIndex ();
	if (index >= 0) showIndex (index);
}

void sendDoubleSelection() {
	postEvent (SWT.DefaultSelection);
}

void tableViewSelectionDidChange (int aNotification) {
	if (ignoreSelect) return;
	NSTableView widget = (NSTableView)view;
	int row = widget.selectedRow();
	if(row == -1)
		postEvent(SWT.Selection);
	else {
		TableItem item = _getItem(row);
		Event event = new Event();
		event.item = item;
		event.index = row;
		postEvent(SWT.Selection, event);
	}
}

int tableView_objectValueForTableColumn_row(int aTableView, int aTableColumn, int rowIndex) {
	TableItem item = items [rowIndex];
	if (checkColumn != null && aTableColumn == checkColumn.id) {
		NSNumber value;
		if (item.checked && item.grayed) {
			value = NSNumber.numberWithInt(OS.NSMixedState);
		} else {
			value = NSNumber.numberWithInt(item.checked ? OS.NSOnState : OS.NSOffState);
		}
		return value.id;
	}
	for (int i=0; i<columnCount; i++) {
		if (columns [i].nsColumn.id == aTableColumn) {
			return item.createString(i).id;
		}
	}
	return item.createString(0).id;
}

void tableView_setObjectValue_forTableColumn_row(int aTableView, int anObject, int aTableColumn, int rowIndex) {
	TableItem item = items [rowIndex];
	if (checkColumn != null && aTableColumn == checkColumn.id)  {
		item.checked = !item.checked;
		Event event = new Event();
		event.detail = SWT.CHECK;
		event.item = item;
		event.index = rowIndex;
		postEvent(SWT.Selection, event);
	}
}

boolean tableView_shouldEditTableColumn_row(int aTableView, int aTableColumn, int rowIndex) {
	return false;
}

void tableView_willDisplayCell_forTableColumn_row(int aTableView, int aCell, int aTableColumn, int rowIndex) {
	if (checkColumn != null && aTableColumn == checkColumn.id) return;
	TableItem item = items [rowIndex];
	Image image = item.image;
	for (int i=0; i<columnCount; i++) {
		if (columns [i].nsColumn.id == aTableColumn) {
			image = item.getImage(i);
		}
	}
	NSBrowserCell cell = new NSBrowserCell(aCell);
	cell.setImage(image != null ? image.handle : null);
	cell.setLeaf(true);
}
}
