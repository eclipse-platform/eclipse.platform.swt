/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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
import org.eclipse.swt.internal.*;

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
 * it does not normally make sense to add <code>Control</code> children to
 * it, or set a layout on it, unless implementing something like a cell
 * editor.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, HIDE_SELECTION, VIRTUAL, NO_SCROLL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, SetData, MeasureItem, EraseItem, PaintItem</dd>
 * </dl>
 * </p><p>
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
	Canvas header;
	TableColumn[] columns = new TableColumn [0];
	TableColumn[] orderedColumns;
	TableItem[] items = new TableItem [0];
	TableItem[] selectedItems = new TableItem [0];
	TableItem focusItem, anchorItem, lastClickedItem;
	Color cachedBackground, cachedForeground;
	Event lastSelectionEvent;
	boolean linesVisible, ignoreKey, ignoreDispose, customHeightSet;
	int itemsCount = 0;
	int topIndex = 0, horizontalOffset = 0;
	int fontHeight = 0, imageHeight = 0, itemHeight = 0;
	int col0ImageWidth = 0;
	int headerImageHeight = 0;
	TableColumn resizeColumn;
	int resizeColumnX = -1;
	int drawCount = 0;
	TableColumn sortColumn;
	int sortDirection = SWT.NONE;

	/* column header tooltip */
	Listener toolTipListener;
	Shell toolTipShell;
	Label toolTipLabel;

	Rectangle arrowBounds, checkboxBounds, clientArea;

	static final int MARGIN_IMAGE = 3;
	static final int MARGIN_CELL = 1;
	static final int SIZE_HORIZONTALSCROLL = 5;
	static final int TOLLERANCE_COLUMNRESIZE = 2;
	static final int WIDTH_HEADER_SHADOW = 2;
	static final int WIDTH_CELL_HIGHLIGHT = 1;
	static final int [] toolTipEvents = new int[] {SWT.MouseExit, SWT.MouseHover, SWT.MouseMove, SWT.MouseDown};
	static final String ELLIPSIS = "...";						//$NON-NLS-1$
	static final String ID_UNCHECKED = "UNCHECKED";			//$NON-NLS-1$
	static final String ID_GRAYUNCHECKED = "GRAYUNCHECKED";	//$NON-NLS-1$
	static final String ID_CHECKMARK = "CHECKMARK";			//$NON-NLS-1$
	static final String ID_ARROWUP = "ARROWUP";				//$NON-NLS-1$
	static final String ID_ARROWDOWN = "ARROWDOWN";			//$NON-NLS-1$
	
//TEMPORARY CODE
boolean hasFocus;
public boolean isFocusControl() {
	return hasFocus;
}
	
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
	setForeground (null);	/* set foreground and background to chosen default colors */
	setBackground (null);
	GC gc = new GC (this);
	fontHeight = gc.getFontMetrics ().getHeight ();
	gc.dispose ();
	itemHeight = fontHeight + (2 * getCellPadding ());
	initImages (display);
	checkboxBounds = getUncheckedImage ().getBounds ();
	arrowBounds = getArrowDownImage ().getBounds ();
	clientArea = getClientArea ();
	
	Listener listener = new Listener () {
		public void handleEvent (Event event) {
			handleEvents (event);
		}
	};
	addListener (SWT.Paint, listener);
	addListener (SWT.MouseDown, listener);
	addListener (SWT.MouseUp, listener);
	addListener (SWT.MouseDoubleClick, listener);
	addListener (SWT.Dispose, listener);	
	addListener (SWT.Resize, listener);
	addListener (SWT.KeyDown, listener);
	addListener (SWT.FocusOut, listener);
	addListener (SWT.FocusIn, listener);
	addListener (SWT.Traverse, listener);

	header = new Canvas (this, SWT.NO_REDRAW_RESIZE | SWT.NO_FOCUS);
	header.setVisible (false);
	header.setBounds (0, 0, 0, fontHeight + 2 * getHeaderPadding ());
	header.addListener (SWT.Paint, listener);
	header.addListener (SWT.MouseDown, listener);
	header.addListener (SWT.MouseUp, listener);
	header.addListener (SWT.MouseHover, listener);
	header.addListener (SWT.MouseDoubleClick, listener);
	header.addListener (SWT.MouseMove, listener);
	header.addListener (SWT.MouseExit, listener);
	header.addListener (SWT.MenuDetect, listener);

	toolTipListener = new Listener () {
		public void handleEvent (Event event) {
			switch (event.type) {
				case SWT.MouseHover:
				case SWT.MouseMove:
					if (headerUpdateToolTip (event.x)) break;
					// FALL THROUGH
				case SWT.MouseExit:
				case SWT.MouseDown:
					headerHideToolTip ();
					break;
			}
		}
	};

	ScrollBar hBar = getHorizontalBar ();
	if (hBar != null) {
		hBar.setValues (0, 0, 1, 1, 1, 1);
		hBar.setVisible (false);
		hBar.addListener (SWT.Selection, listener);
	}
	ScrollBar vBar = getVerticalBar ();
	if (vBar != null) {
		vBar.setValues (0, 0, 1, 1, 1, 1);
		vBar.setVisible (false);
		vBar.addListener (SWT.Selection, listener);
	}
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
boolean checkData (TableItem item, boolean redraw) {
	if (item.cached) return true;
	if ((style & SWT.VIRTUAL) != 0) {
		item.cached = true;
		Event event = new Event ();
		event.item = item;
		event.index = indexOf (item);
		sendEvent (SWT.SetData, event);
		if (isDisposed () || item.isDisposed ()) return false;
		if (redraw) redrawItem (item.index, false);
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
	style |= SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED;
	//TEMPORARY CODE
	style |= SWT.FULL_SELECTION;
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
	checkWidget ();
	if (!(0 <= index && index < itemsCount)) error (SWT.ERROR_INVALID_RANGE);
	Rectangle bounds = items [index].getBounds (false);
	int oldRightX = bounds.x + bounds.width;
	items [index].clear ();
	if (columns.length == 0) updateHorizontalBar (0, -oldRightX);
	redrawItem (index, false);
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
	if (!(0 <= start && start <= end && end < itemsCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	for (int i = start; i <= end; i++) {
		items [i].clear ();
	}
	updateHorizontalBar ();
	redrawItems (start, end, false);
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
	for (int i = 0; i < indices.length; i++) {
		if (!(0 <= indices [i] && indices [i] < itemsCount)) {
			error (SWT.ERROR_INVALID_RANGE);
		}
	}
	
	for (int i = 0; i < indices.length; i++) {
		items [indices [i]].clear ();
	}
	updateHorizontalBar ();
	for (int i = 0; i < indices.length; i++) {
		redrawItem (indices [i], false);
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
	clear (0, itemsCount - 1);
}
/*
 * Returns the ORDERED index of the column that the specified x falls within,
 * or -1 if the x lies to the right of the last column.
 */
int computeColumnIntersect (int x, int startColumn) {
	TableColumn[] orderedColumns = getOrderedColumns ();
	if (orderedColumns.length - 1 < startColumn) return -1;
	int rightX = orderedColumns [startColumn].getX ();
	for (int i = startColumn; i < orderedColumns.length; i++) {
		rightX += orderedColumns [i].width;
		if (x < rightX) return i;
	}
	return -1;
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	if (wHint != SWT.DEFAULT) {
		width = wHint;
	} else {
		if (columns.length == 0) {
			for (int i = 0; i < itemsCount; i++) {
				Rectangle itemBounds = items [i].getBounds (false);
				width = Math.max (width, itemBounds.x + itemBounds.width);
			}
		} else {
			TableColumn[] orderedColumns = getOrderedColumns ();
			TableColumn lastColumn = orderedColumns [orderedColumns.length - 1];
			width = lastColumn.getX () + lastColumn.width;
		}
	}
	if (hHint != SWT.DEFAULT) {
		height = hHint;
	} else {
		height = getHeaderHeight () + itemsCount * itemHeight;
	}
	Rectangle result = computeTrim (0, 0, width, height);
	return new Point (result.width, result.height);
}
void createItem (TableColumn column, int index) {
	TableColumn[] newColumns = new TableColumn [columns.length + 1];
	System.arraycopy (columns, 0, newColumns, 0, index);
	newColumns [index] = column;
	System.arraycopy (columns, index, newColumns, index + 1, columns.length - index);
	columns = newColumns;
	
	if (orderedColumns != null) {
		int insertIndex = 0;
		if (index > 0) {
			insertIndex = columns [index - 1].getOrderIndex () + 1;
		}
		TableColumn[] newOrderedColumns = new TableColumn [orderedColumns.length + 1];
		System.arraycopy (orderedColumns, 0, newOrderedColumns, 0, insertIndex);
		newOrderedColumns [insertIndex] = column;
		System.arraycopy (
			orderedColumns,
			insertIndex,
			newOrderedColumns,
			insertIndex + 1,
			orderedColumns.length - insertIndex);
		orderedColumns = newOrderedColumns;
	}

	/* allow all items to update their internal structures accordingly */
	for (int i = 0; i < itemsCount; i++) {
		items [i].addColumn (column);
	}

	/* existing items become hidden when going from 0 to 1 column (0 width) */
	if (columns.length == 1 && itemsCount > 0) {
		redrawFromItemDownwards (topIndex);
	} else {
		/* checkboxes become hidden when creating a column with index == orderedIndex == 0 (0 width) */
		if (itemsCount > 0 && (style & SWT.CHECK) != 0 && index == 0 && column.getOrderIndex () == 0) {
			redrawFromItemDownwards (topIndex);
		}
	}
}
void createItem (TableItem item) {
	int index = item.index;
	if (itemsCount == items.length) {
		int grow = drawCount <= 0 ? 4 : Math.max (4, items.length * 3 / 2);
		TableItem[] newItems = new TableItem [items.length + grow];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	if (index != itemsCount) {
		/* new item is not at end of list, so shift other items right to create space for it */
		System.arraycopy (items, index, items, index + 1, itemsCount - index);
	}
	items [index] = item;
	itemsCount++;

	/* update the index for items bumped down by this new item */
	for (int i = index + 1; i < itemsCount; i++) {
		items [i].index = i;
	}

	/* update scrollbars */
	updateVerticalBar ();
	Rectangle bounds = item.getBounds (false);
	int rightX = bounds.x + bounds.width;
	updateHorizontalBar (rightX, rightX);
	/* 
	 * If new item is above viewport then adjust topIndex and the vertical
	 * scrollbar so that the current viewport items will not change.
	 */
	if (item.index < topIndex) {
		topIndex++;
		ScrollBar vBar = getVerticalBar ();
		if (vBar != null) vBar.setSelection (topIndex);
		return;
	}
	/*
	 * If this is the first item and the receiver has focus then its boundary
	 * focus ring must be removed. 
	 */
	if (itemsCount == 1 && isFocusControl ()) {
		focusItem = item;
		redraw ();
		return;
	}
	if (item.isInViewport ()) {
		redrawFromItemDownwards (index);
	}
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
	if (!(0 <= index && index < itemsCount)) return;
	TableItem item = items [index];
	int selectIndex = getSelectionIndex (item);
	if (selectIndex == -1) return;
	
	TableItem[] newSelectedItems = new TableItem [selectedItems.length - 1];
	System.arraycopy (selectedItems, 0, newSelectedItems, 0, selectIndex);
	System.arraycopy (selectedItems, selectIndex + 1, newSelectedItems, selectIndex, newSelectedItems.length - selectIndex);
	selectedItems = newSelectedItems;
	
	if (hasFocus () || (style & SWT.HIDE_SELECTION) == 0) {
		redrawItem (item.index, false);
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
	checkWidget ();
	if (start == 0 && end == itemsCount - 1) {
		deselectAll ();
	} else {
		start = Math.max (start, 0);
		end = Math.min (end, itemsCount - 1);
		for (int i = start; i <= end; i++) {
			deselect (i);
		}
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
	if (indices.length == 0) return;
	for (int i = 0; i < indices.length; i++) {
		deselect (indices [i]);
	}
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
	TableItem[] oldSelection = selectedItems;
	selectedItems = new TableItem [0];
	if (hasFocus () || (style & SWT.HIDE_SELECTION) == 0) {
		for (int i = 0; i < oldSelection.length; i++) {
			redrawItem (oldSelection [i].index, true);
		}
	}
}
void deselectItem (TableItem item) {
	int index = getSelectionIndex (item);
	if (index == -1) return;
	TableItem[] newSelectedItems = new TableItem [selectedItems.length - 1];
	System.arraycopy (selectedItems, 0, newSelectedItems, 0, index);
	System.arraycopy (
		selectedItems,
		index + 1,
		newSelectedItems,
		index,
		newSelectedItems.length - index);
	selectedItems = newSelectedItems;
}
void destroyItem (TableColumn column) {
	headerHideToolTip ();
	int index = column.getIndex ();
	int orderedIndex = column.getOrderIndex ();

	TableColumn[] newColumns = new TableColumn [columns.length - 1];
	System.arraycopy (columns, 0, newColumns, 0, index);
	System.arraycopy (columns, index + 1, newColumns, index, newColumns.length - index);
	columns = newColumns;

	if (orderedColumns != null) {
		if (columns.length < 2) {
			orderedColumns = null;
		} else {
			int removeIndex = column.getOrderIndex ();
			TableColumn[] newOrderedColumns = new TableColumn [orderedColumns.length - 1];
			System.arraycopy (orderedColumns, 0, newOrderedColumns, 0, removeIndex);
			System.arraycopy (
				orderedColumns,
				removeIndex + 1,
				newOrderedColumns,
				removeIndex,
				newOrderedColumns.length - removeIndex);
			orderedColumns = newOrderedColumns;
		}
	}

	/* ensure that column 0 always has left-alignment */
	if (index == 0 && columns.length > 0) {
		columns [0].style |= SWT.LEFT;
		columns [0].style &= ~(SWT.CENTER | SWT.RIGHT);
	}

	/* allow all items to update their internal structures accordingly */
	for (int i = 0; i < itemsCount; i++) {
		items [i].removeColumn (column, index);
	}

	/* update horizontal scrollbar */
	int lastColumnIndex = columns.length - 1;
	if (lastColumnIndex < 0) {		/* no more columns */
		updateHorizontalBar ();
	} else {
		int newWidth = 0;
		for (int i = 0; i < columns.length; i++) {
			newWidth += columns [i].width;
		}
		ScrollBar hBar = getHorizontalBar (); 
		if (hBar != null) {
			hBar.setMaximum (newWidth);
			hBar.setVisible (clientArea.width < newWidth);
		}
		int selection = hBar.getSelection ();
		if (selection != horizontalOffset) {
			horizontalOffset = selection;
			redraw ();
			if (header.isVisible () && drawCount <= 0) header.redraw ();
		}
	}
	TableColumn[] orderedColumns = getOrderedColumns ();
	for (int i = orderedIndex; i < orderedColumns.length; i++) {
		if (!orderedColumns [i].isDisposed ()) {
			orderedColumns [i].sendEvent (SWT.Move);
		}
	}

	if (sortColumn == column) {
		sortColumn = null;
	}
}
/*
 * Allows the Table to update internal structures it has that may contain the
 * item being destroyed.
 */
void destroyItem (TableItem item) {
	if (item == focusItem) reassignFocus ();

	int index = item.index;
	Rectangle bounds = item.getBounds (false);
	int rightX = bounds.x + bounds.width;

	if (index != itemsCount - 1) {
		/* item is not at end of items list, so must shift items left to reclaim its slot */
		System.arraycopy (items, index + 1, items, index, itemsCount - index - 1);
		items [itemsCount - 1] = null;
	} else {
		items [index] = null;	/* last item, so no array copy needed */
	}
	itemsCount--;
	
	if (drawCount <= 0 && items.length - itemsCount == 4) {
		/* shrink the items array */
		TableItem[] newItems = new TableItem [itemsCount];
		System.arraycopy (items, 0, newItems, 0, newItems.length);
		items = newItems;
	}

	/* update the index on affected items */
	for (int i = index; i < itemsCount; i++) {
		items [i].index = i;
	}
	item.index = -1;

	int oldTopIndex = topIndex;
	updateVerticalBar ();
	updateHorizontalBar (0, -rightX);
	/* 
	 * If destroyed item is above viewport then adjust topIndex and the vertical
	 * scrollbar so that the current viewport items will not change. 
	 */
	if (index < topIndex) {
		topIndex = oldTopIndex - 1;
		ScrollBar vBar = getVerticalBar ();
		if (vBar != null) vBar.setSelection (topIndex);
	}

	/* selectedItems array */
	if (item.isSelected ()) {
		int selectionIndex = getSelectionIndex (item);
		TableItem[] newSelectedItems = new TableItem [selectedItems.length - 1];
		System.arraycopy (selectedItems, 0, newSelectedItems, 0, selectionIndex);
		System.arraycopy (
			selectedItems,
			selectionIndex + 1,
			newSelectedItems,
			selectionIndex,
			newSelectedItems.length - selectionIndex);
		selectedItems = newSelectedItems;
	}
	if (item == anchorItem) anchorItem = null;
	if (item == lastClickedItem) lastClickedItem = null;
	/*
	 * If this was the last item and the receiver has focus then its boundary
	 * focus ring must be redrawn.
	 */
	if (itemsCount == 0 && isFocusControl ()) {
		redraw ();
		return;
	}
}
Image getArrowDownImage () {
	return (Image) display.getData (ID_ARROWDOWN);
}
Image getArrowUpImage () {
	return (Image) display.getData (ID_ARROWUP);
}
public Color getBackground () {
	checkWidget ();
	if (cachedBackground != null) return cachedBackground;
	return super.getBackground ();
}
int getCellPadding () {
	return MARGIN_CELL + WIDTH_CELL_HIGHLIGHT; 
}
Image getCheckmarkImage () {
	return (Image) display.getData (ID_CHECKMARK);
}
public Control[] getChildren () {
	checkWidget ();
	Control[] controls = _getChildren ();
	if (header == null) return controls;
	Control[] result = new Control [controls.length - 1];
	/* remove the Header from the returned set of children */
	int index = 0;
	for (int i = 0; i < controls.length; i++) {
		 if (controls [i] != header) {
		 	result [index++] = controls [i];
		 }
	}
	return result;
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
	if (!(0 <= index && index < columns.length)) error (SWT.ERROR_INVALID_RANGE);
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
	return columns.length;
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
public int[] getColumnOrder () {
	checkWidget ();
	int[] result = new int [columns.length];
	if (orderedColumns != null) {
		for (int i = 0; i < result.length; i++) {
			result [i] = orderedColumns [i].getIndex ();
		}
	} else {
		for (int i = 0; i < columns.length; i++) {
			result [i] = i;
		}
	}
	return result;
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
public TableColumn[] getColumns () {
	checkWidget ();
	TableColumn[] result = new TableColumn [columns.length];
	System.arraycopy (columns, 0, result, 0, columns.length);
	return result;
}
public Color getForeground () {
	checkWidget ();
	if (cachedForeground != null) return cachedForeground;
	return super.getForeground ();
}
Image getGrayUncheckedImage () {
	return (Image) display.getData (ID_GRAYUNCHECKED);
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
	return 1;
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
	if (!header.getVisible ()) return 0;
	return header.getSize ().y;
}
int getHeaderPadding () {
	return MARGIN_CELL + WIDTH_HEADER_SHADOW; 
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
	return header.getVisible ();
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
	if (!(0 <= index && index < itemsCount)) error (SWT.ERROR_INVALID_RANGE);
	return items [index];
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
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	int index = (point.y - getHeaderHeight ()) / itemHeight + topIndex;
	if (!(0 <= index && index < itemsCount)) return null;		/* below the last item */
	TableItem result = items [index];
	if (!result.getHitBounds ().contains (point)) return null;	/* considers the x value */
	return result;
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
	return itemsCount;
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
	return itemHeight;
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
public TableItem[] getItems () {
	checkWidget ();
	TableItem[] result = new TableItem [itemsCount];
	System.arraycopy (items, 0, result, 0, itemsCount);
	return result;	
}
/*
 * Returns the current y-coordinate that the specified item should have. 
 */
int getItemY (TableItem item) {
	return (item.index - topIndex) * itemHeight + getHeaderHeight ();
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
	return linesVisible;
}
TableColumn[] getOrderedColumns () {
	if (orderedColumns != null) return orderedColumns;
	return columns;
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
public TableItem[] getSelection () {
	checkWidget ();
	TableItem[] result = new TableItem [selectedItems.length];
	System.arraycopy (selectedItems, 0, result, 0, selectedItems.length);
	sortAscent (result);
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
	return selectedItems.length;
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
	if (selectedItems.length == 0) return -1;
	return selectedItems [0].index;
}
/*
 * Returns the index of the argument in the receiver's array of currently-
 * selected items, or -1 if the item is not currently selected.
 */
int getSelectionIndex (TableItem item) {
	for (int i = 0; i < selectedItems.length; i++) {
		if (selectedItems [i] == item) return i;
	}
	return -1;
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
	int[] result = new int [selectedItems.length];
	for (int i = 0; i < selectedItems.length; i++) {
		result [i] = selectedItems [i].index;
	}
	sortAscent (result);
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
	return topIndex;
}
Image getUncheckedImage () {
	return (Image) display.getData (ID_UNCHECKED);
}
void handleEvents (Event event) {
	switch (event.type) {
		case SWT.Paint:
			if (event.widget == header) {
				headerOnPaint (event);
			} else {
				onPaint (event);
			}
			break;
		case SWT.MenuDetect: {
			notifyListeners (SWT.MenuDetect, event);
			break;
			}
		case SWT.MouseDown:
			if (event.widget == header) {
				headerOnMouseDown (event);
			} else {
				onMouseDown (event);
			}
			break;
		case SWT.MouseUp:
			if (event.widget == header) {
				headerOnMouseUp (event);
			} else {
				onMouseUp (event);
			}
			break;
		case SWT.MouseHover:
			headerOnMouseHover (event); break;
		case SWT.MouseMove:
			headerOnMouseMove (event); break;
		case SWT.MouseDoubleClick:
			if (event.widget == header) {
				headerOnMouseDoubleClick (event);
			} else {
				onMouseDoubleClick (event);
			}
			break;
		case SWT.MouseExit:
			headerOnMouseExit (); break;
		case SWT.Dispose:
			onDispose (event); break;		
		case SWT.KeyDown:
			onKeyDown (event); break;
		case SWT.Resize:
			onResize (event); break;
		case SWT.Selection:
			if (event.widget == getHorizontalBar ()) {
				onScrollHorizontal (event);
			}
			if (event.widget == getVerticalBar ()) {
				onScrollVertical (event);
			}
			break;
		case SWT.FocusOut:
			onFocusOut (); break;
		case SWT.FocusIn:
			onFocusIn (); break;	
		case SWT.Traverse:
			switch (event.detail) {
				case SWT.TRAVERSE_ESCAPE:
				case SWT.TRAVERSE_RETURN:
				case SWT.TRAVERSE_TAB_NEXT:
				case SWT.TRAVERSE_TAB_PREVIOUS:
				case SWT.TRAVERSE_PAGE_NEXT:
				case SWT.TRAVERSE_PAGE_PREVIOUS:
					event.doit = true;
					break;
			}
			break;			
	}
}
String headerGetToolTip (int x) {
	if (resizeColumn != null) return null;
	int orderedIndex = computeColumnIntersect (x, 0);
	if (orderedIndex == -1) return null;
	TableColumn[] orderedColumns = getOrderedColumns ();
	TableColumn column = orderedColumns [orderedIndex];
	if (column.toolTipText == null) return null;

	/* no tooltip should appear if the hover is at a column resize opportunity */
	int columnX = column.getX ();
	if (orderedIndex > 0 && orderedColumns [orderedIndex - 1].resizable) {
		/* left column bound is resizable */
		if (x - columnX <= TOLLERANCE_COLUMNRESIZE) return null;
	}
	if (column.resizable) {
		/* right column bound is resizable */
		int columnRightX = columnX + column.width;
		if (columnRightX - x <= TOLLERANCE_COLUMNRESIZE) return null;
	}
	return removeMnemonics (column.toolTipText);
}
void headerHideToolTip() {
	if (toolTipShell == null) return;
	for (int i = 0; i < toolTipEvents.length; i++) {
		header.removeListener (toolTipEvents [i], toolTipListener);
	}
	toolTipShell.dispose ();
	toolTipShell = null;
	toolTipLabel = null;
}
void headerOnMouseDoubleClick (Event event) {
	if (!isFocusControl ()) setFocus ();
	if (columns.length == 0) return;
	TableColumn[] orderedColumns = getOrderedColumns ();
	int x = -horizontalOffset;
	for (int i = 0; i < orderedColumns.length; i++) {
		TableColumn column = orderedColumns [i];
		x += column.width;
		if (event.x < x) {
			/* found the clicked column */
			TableColumn packColumn = null;
			if (x - event.x <= TOLLERANCE_COLUMNRESIZE) {
				/* clicked on column bound for this column */
				packColumn = column;
			} else {
				if (i > 0 && event.x - column.getX () <= TOLLERANCE_COLUMNRESIZE) {
					/* clicked on column bound that applies to previous column */
					packColumn = orderedColumns [i - 1];
				}
			}
			if (packColumn != null) {
				packColumn.pack ();
				resizeColumn = null;
				if (Math.abs (packColumn.getX () + packColumn.width - event.x) > TOLLERANCE_COLUMNRESIZE) {
					/* column separator has relocated away from pointer location */
					setCursor (null);
				}
				return;
			}
			/* did not click on column separator, so just fire column event */
			Event newEvent = new Event ();
			newEvent.widget = column;
			column.postEvent (SWT.DefaultSelection, newEvent);
			return;
		}
	}
}
void headerOnMouseDown (Event event) {
	if (event.button != 1) return;
	TableColumn[] orderedColumns = getOrderedColumns ();
	int x = -horizontalOffset;
	for (int i = 0; i < orderedColumns.length; i++) {
		TableColumn column = orderedColumns [i];
		x += column.width;
		/* if close to a resizable column separator line then begin column resize */
		if (column.resizable && Math.abs (x - event.x) <= TOLLERANCE_COLUMNRESIZE) {
			resizeColumn = column;
			resizeColumnX = x;
			return;
		}
		/*
		 * If within column but not near separator line then start column drag
		 * if column is moveable, or just fire column Selection otherwise.
		 */
		if (event.x < x) {
			if (column.moveable) {
				/* open tracker on the dragged column's header cell */
				int columnX = column.getX ();
				int pointerOffset = event.x - columnX;
				headerHideToolTip ();
				Tracker tracker = new Tracker (this, SWT.NONE);
				tracker.setRectangles (new Rectangle[] {
					new Rectangle (columnX, 0, column.width, getHeaderHeight ())
				});
				if (!tracker.open ()) return;	/* cancelled */
				/* determine which column was dragged onto */
				Rectangle result = tracker.getRectangles () [0];
				int pointerX = result.x + pointerOffset;
				if (pointerX < 0) return;	/* dragged too far left */
				x = -horizontalOffset;
				for (int destIndex = 0; destIndex < orderedColumns.length; destIndex++) {
					TableColumn destColumn = orderedColumns [destIndex];
					x += destColumn.width;
					if (pointerX < x) {
						int oldIndex = column.getOrderIndex ();
						if (destIndex == oldIndex) {	/* dragged onto self */
							Event newEvent = new Event ();
							newEvent.widget = column;
							column.postEvent (SWT.Selection, newEvent);
							return;
						}
						int leftmostIndex = Math.min (destIndex, oldIndex);
						int[] oldOrder = getColumnOrder ();
						int[] newOrder = new int [oldOrder.length];
						System.arraycopy (oldOrder, 0, newOrder, 0, leftmostIndex);
						if (leftmostIndex == oldIndex) {
							/* column moving to the right */
							System.arraycopy (oldOrder, oldIndex + 1, newOrder, oldIndex, destIndex - oldIndex);
						} else {
							/* column moving to the left */
							System.arraycopy (oldOrder, destIndex, newOrder, destIndex + 1, oldIndex - destIndex);
						}
						newOrder [destIndex] = oldOrder [oldIndex];
						int rightmostIndex = Math.max (destIndex, oldIndex);
						System.arraycopy (
							oldOrder,
							rightmostIndex + 1,
							newOrder,
							rightmostIndex + 1,
							newOrder.length - rightmostIndex - 1);
						setColumnOrder (newOrder);
						return;
					}
				}
				return;		/* dragged too far right */
			}
			/* column is not moveable */
			Event newEvent = new Event ();
			newEvent.widget = column;
			column.postEvent (SWT.Selection, newEvent);
			return;
		}
	}
}
void headerOnMouseExit () {
	if (resizeColumn != null) return;
	setCursor (null);	/* ensure that a column resize cursor does not escape */
}
void headerOnMouseHover (Event event) {
	headerShowToolTip (event.x);
}
void headerOnMouseMove (Event event) {
	if (resizeColumn == null) {
		/* not currently resizing a column */
		for (int i = 0; i < columns.length; i++) {
			TableColumn column = columns [i]; 
			int x = column.getX () + column.width;
			if (Math.abs (x - event.x) <= TOLLERANCE_COLUMNRESIZE) {
				if (column.resizable) {
					setCursor (display.getSystemCursor (SWT.CURSOR_SIZEWE));
				} else {
					setCursor (null);
				}
				return;
			}
		}
		setCursor (null);
		return;
	}
	
	/* currently resizing a column */
	
	/* don't allow the resize x to move left of the column's x position */
	if (event.x <= resizeColumn.getX ()) return;

	/* redraw the resizing line at its new location */
	GC gc = new GC (this);
	gc.setForeground (display.getSystemColor (SWT.COLOR_BLACK));
	int lineHeight = clientArea.height;
	redraw (resizeColumnX - 1, 0, 1, lineHeight, false);
	resizeColumnX = event.x;
	gc.drawLine (resizeColumnX - 1, 0, resizeColumnX - 1, lineHeight);
	gc.dispose ();
}
void headerOnMouseUp (Event event) {
	if (resizeColumn == null) return;	/* not resizing a column */

	/* remove the resize line */
	GC gc = new GC (this);
	redraw (resizeColumnX - 1, 0, 1, clientArea.height, false);
	gc.dispose ();

	int newWidth = resizeColumnX - resizeColumn.getX ();
	if (newWidth != resizeColumn.width) {
		setCursor (null);
		updateColumnWidth (resizeColumn, newWidth);
	}
	resizeColumnX = -1;
	resizeColumn = null;
}
void headerOnPaint (Event event) {
	TableColumn[] orderedColumns = getOrderedColumns ();
	int numColumns = orderedColumns.length;
	GC gc = event.gc;
	Rectangle clipping = gc.getClipping ();
	int startColumn = -1, endColumn = -1;
	if (numColumns > 0) {
		startColumn = computeColumnIntersect (clipping.x, 0);
		if (startColumn != -1) {	/* the clip x is within a column's bounds */
			endColumn = computeColumnIntersect (clipping.x + clipping.width, startColumn);
			if (endColumn == -1) endColumn = numColumns - 1;
		}
	} else {
		startColumn = endColumn = 0;
	}

	/* paint the column header shadow that spans the full header width */
	Point headerSize = header.getSize ();
	headerPaintHShadows (gc, 0, 0, headerSize.x, headerSize.y);

	/* if all damage is to the right of the last column then finished */
	if (startColumn == -1) return;
	
	/* paint each of the column headers */
	if (numColumns == 0) return;	/* no headers to paint */
	for (int i = startColumn; i <= endColumn; i++) {
		headerPaintVShadows (gc, orderedColumns [i].getX (), 0, orderedColumns [i].width, headerSize.y);
		orderedColumns [i].paint (gc);
	}
}
void headerPaintHShadows (GC gc, int x, int y, int width, int height) {
	gc.setClipping (x, y, width, height);
	int endX = x + width;
	gc.setForeground (display.getSystemColor (SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
	gc.drawLine (x, y, endX, y);					/* highlight shadow */
	gc.setForeground (display.getSystemColor (SWT.COLOR_WIDGET_NORMAL_SHADOW));
	gc.drawLine (x, height - 2, endX, height - 2);	/* lowlight shadow */
	gc.setForeground (display.getSystemColor (SWT.COLOR_WIDGET_DARK_SHADOW));
	gc.drawLine (x, height - 1, endX, height - 1);	/* outer shadow */
}
void headerPaintVShadows (GC gc, int x, int y, int width, int height) {
	gc.setClipping (x, y, width, height);
	int endX = x + width;
	gc.setForeground (display.getSystemColor (SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
	gc.drawLine (x, y, x, y + height - 1);					/* highlight shadow */
	gc.setForeground (display.getSystemColor (SWT.COLOR_WIDGET_NORMAL_SHADOW));
	gc.drawLine (endX - 2, y + 1, endX - 2, height - 2);	/* light inner shadow */
	gc.setForeground (display.getSystemColor (SWT.COLOR_WIDGET_DARK_SHADOW));
	gc.drawLine (endX - 1, y, endX - 1, height - 1);		/* dark outer shadow */
}
void headerShowToolTip (int x) {
	String tooltip = headerGetToolTip (x);
	if (tooltip == null || tooltip.length () == 0) return;

	if (toolTipShell == null) {
		toolTipShell = new Shell (getShell (), SWT.ON_TOP | SWT.TOOL);
		toolTipLabel = new Label (toolTipShell, SWT.CENTER);
		Display display = toolTipShell.getDisplay ();
		toolTipLabel.setForeground (display.getSystemColor (SWT.COLOR_INFO_FOREGROUND));
		toolTipLabel.setBackground (display.getSystemColor (SWT.COLOR_INFO_BACKGROUND));
		for (int i = 0; i < toolTipEvents.length; i++) {
			header.addListener (toolTipEvents [i], toolTipListener);
		}
	}
	if (headerUpdateToolTip (x)) {
		toolTipShell.setVisible (true);
	} else {
		headerHideToolTip ();
	}
}
boolean headerUpdateToolTip (int x) {
	String tooltip = headerGetToolTip (x);
	if (tooltip == null || tooltip.length () == 0) return false;
	if (tooltip.equals (toolTipLabel.getText ())) return true;

	toolTipLabel.setText (tooltip);
	TableColumn column = getOrderedColumns () [computeColumnIntersect (x, 0)];
	toolTipShell.setData (new Integer (column.getIndex ()));
	Point labelSize = toolTipLabel.computeSize (SWT.DEFAULT, SWT.DEFAULT, true);
	labelSize.x += 2; labelSize.y += 2;
	toolTipLabel.setSize (labelSize);
	toolTipShell.pack ();
	/*
	 * On some platforms, there is a minimum size for a shell  
	 * which may be greater than the label size.
	 * To avoid having the background of the tip shell showing
	 * around the label, force the label to fill the entire client area.
	 */
	Rectangle area = toolTipShell.getClientArea ();
	toolTipLabel.setSize (area.width, area.height);

	/* Position the tooltip and ensure it's not located off the screen */
	Point cursorLocation = getDisplay ().getCursorLocation ();
	int cursorHeight = 21;	/* assuming cursor is 21x21 */ 
	Point size = toolTipShell.getSize ();
	Rectangle rect = getMonitor ().getBounds ();
	Point pt = new Point (cursorLocation.x, cursorLocation.y + cursorHeight + 2);
	pt.x = Math.max (pt.x, rect.x);
	if (pt.x + size.x > rect.x + rect.width) pt.x = rect.x + rect.width - size.x;
	if (pt.y + size.y > rect.y + rect.height) pt.y = cursorLocation.y - 2 - size.y;
	toolTipShell.setLocation (pt);
	return true;
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
	if (column.parent != this) return -1;
	return column.getIndex ();
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
	if (item.parent != this) return -1;
	return item.index;
}
static void initImages (final Display display) {
	PaletteData arrowPalette = new PaletteData (new RGB[] {
		new RGB (0, 0, 0), new RGB (255, 255, 255)});
	if (display.getData (ID_ARROWDOWN) == null) {
		ImageData arrowDown = new ImageData (
			7, 4, 1,
			arrowPalette, 1,
			new byte[] {0x00, (byte)0x83, (byte)0xC7, (byte)0xEF});
		arrowDown.transparentPixel = 0x1;	/* use white for transparency */
		display.setData (ID_ARROWDOWN, new Image (display, arrowDown));
	}
	if (display.getData (ID_ARROWUP) == null) {
		ImageData arrowUp = new ImageData (
			7, 4, 1,
			arrowPalette, 1,
			new byte[] {(byte)0xEF, (byte)0xC7, (byte)0x83, 0x00});
		arrowUp.transparentPixel = 0x1;		/* use white for transparency */
		display.setData (ID_ARROWUP, new Image (display, arrowUp));
	}

	PaletteData checkMarkPalette = new PaletteData (	
		new RGB[] {new RGB (0, 0, 0), new RGB (252, 3, 251)});
	byte[] checkbox = new byte[] {0, 0, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 127, -64, 0, 0};
	ImageData checkmark = new ImageData (7, 7, 1, checkMarkPalette, 1, new byte[] {-4, -8, 112, 34, 6, -114, -34});
	checkmark.transparentPixel = 1;
	if (display.getData (ID_CHECKMARK) == null) {
		display.setData (ID_CHECKMARK, new Image (display, checkmark));
	}

	if (display.getData (ID_UNCHECKED) == null) {
		PaletteData uncheckedPalette = new PaletteData (	
			new RGB[] {new RGB (128, 128, 128), new RGB (255, 255, 255)});
		ImageData unchecked = new ImageData (11, 11, 1, uncheckedPalette, 2, checkbox);
		display.setData (ID_UNCHECKED, new Image (display, unchecked));
	}

	if (display.getData (ID_GRAYUNCHECKED) == null) {
		PaletteData grayUncheckedPalette = new PaletteData (	
			new RGB[] {new RGB (128, 128, 128), new RGB (192, 192, 192)});
		ImageData grayUnchecked = new ImageData (11, 11, 1, grayUncheckedPalette, 2, checkbox);
		display.setData (ID_GRAYUNCHECKED, new Image (display, grayUnchecked));
	}

	display.disposeExec (new Runnable () {
		public void run() {
			Image unchecked = (Image) display.getData (ID_UNCHECKED);
			if (unchecked != null) unchecked.dispose ();
			Image grayUnchecked = (Image) display.getData (ID_GRAYUNCHECKED);
			if (grayUnchecked != null) grayUnchecked.dispose ();
			Image checkmark = (Image) display.getData (ID_CHECKMARK);
			if (checkmark != null) checkmark.dispose ();
			Image arrowDown = (Image) display.getData (ID_ARROWDOWN);
			if (arrowDown != null) arrowDown.dispose ();
			Image arrowUp = (Image) display.getData (ID_ARROWUP);
			if (arrowUp != null) arrowUp.dispose ();

			display.setData (ID_UNCHECKED, null);
			display.setData (ID_GRAYUNCHECKED, null);
			display.setData (ID_CHECKMARK, null);
			display.setData (ID_ARROWDOWN, null);
			display.setData (ID_ARROWUP, null);
		}
	});
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
	if (!(0 <= index && index < itemsCount)) return false;
	return items [index].isSelected ();
}
void onArrowDown (int stateMask) {
	if ((stateMask & (SWT.SHIFT | SWT.CTRL)) == 0) {
		/* Down Arrow with no modifiers */
		int newFocusIndex = focusItem.index + 1;
		if (newFocusIndex == itemsCount) return; 	/* at bottom */
		selectItem (items [newFocusIndex], false);
		setFocusItem (items [newFocusIndex], true);
		redrawItem (newFocusIndex, true);
		showItem (items [newFocusIndex]);
		Event newEvent = new Event ();
		newEvent.item = items [newFocusIndex];
		postEvent (SWT.Selection, newEvent);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.CTRL) != 0) {
			/* CTRL+Down Arrow, CTRL+Shift+Down Arrow */
			int visibleItemCount = (clientArea.height - getHeaderHeight ()) / itemHeight;
			if (itemsCount <= topIndex + visibleItemCount) return;	/* at bottom */
			update ();
			topIndex++;
			ScrollBar vBar = getVerticalBar ();
			if (vBar != null) vBar.setSelection (topIndex);
			GC gc = new GC (this);
			gc.copyArea (
				0, 0,
				clientArea.width, clientArea.height,
				0, -itemHeight);
			gc.dispose ();
			return;
		}
		/* Shift+Down Arrow */
		int newFocusIndex = focusItem.index + 1;
		if (newFocusIndex == itemsCount) return; 	/* at bottom */
		selectItem (items [newFocusIndex], false);
		setFocusItem (items [newFocusIndex], true);
		redrawItem (newFocusIndex, true);
		showItem (items [newFocusIndex]);
		Event newEvent = new Event ();
		newEvent.item = items [newFocusIndex];
		postEvent (SWT.Selection, newEvent);
		return;
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			/* CTRL+Shift+Down Arrow */
			int visibleItemCount = (clientArea.height - getHeaderHeight ()) / itemHeight;
			if (itemsCount <= topIndex + visibleItemCount) return;	/* at bottom */
			update ();
			topIndex++;
			ScrollBar vBar = getVerticalBar ();
			if (vBar != null) vBar.setSelection (topIndex);
			GC gc = new GC (this);
			gc.copyArea (
				0, 0,
				clientArea.width, clientArea.height,
				0, -itemHeight);
			gc.dispose ();
			return;
		}
		/* CTRL+Down Arrow */
		int focusIndex = focusItem.index; 
		if (focusIndex == itemsCount - 1) return;	/* at bottom */
		TableItem newFocusItem = items [focusIndex + 1];
		setFocusItem (newFocusItem, true);
		redrawItem (newFocusItem.index, true);
		showItem (newFocusItem);
		return;
	}
	/* Shift+Down Arrow */
	int newFocusIndex = focusItem.index + 1;
	if (newFocusIndex == itemsCount) return; 	/* at bottom */
	if (anchorItem == null) anchorItem = focusItem;
	if (focusItem.index < anchorItem.index) {
		deselectItem (focusItem);
		redrawItem (focusItem.index, true);
	}
	selectItem (items [newFocusIndex], true);
	setFocusItem (items [newFocusIndex], true);
	redrawItem (newFocusIndex, true);
	showItem (items [newFocusIndex]);
	Event newEvent = new Event ();
	newEvent.item = items [newFocusIndex];
	postEvent (SWT.Selection, newEvent);
}
void onArrowLeft (int stateMask) {
	if (horizontalOffset == 0) return;
	int newSelection = Math.max (0, horizontalOffset - SIZE_HORIZONTALSCROLL);
	update ();
	GC gc = new GC (this);
	gc.copyArea (
		0, 0,
		clientArea.width, clientArea.height,
		horizontalOffset - newSelection, 0);
	gc.dispose ();
	if (header.getVisible ()) {
		header.update ();
		Rectangle headerClientArea = header.getClientArea ();
		gc = new GC (header);
		gc.copyArea (
			0, 0,
			headerClientArea.width, headerClientArea.height,
			horizontalOffset - newSelection, 0);
		gc.dispose();
	}
	horizontalOffset = newSelection;
	ScrollBar hBar = getHorizontalBar (); 
	if (hBar != null) hBar.setSelection (horizontalOffset);
}
void onArrowRight (int stateMask) {
	ScrollBar hBar = getHorizontalBar ();
	if (hBar == null) return;
	int maximum = hBar.getMaximum ();
	int clientWidth = clientArea.width;
	if ((horizontalOffset + clientArea.width) == maximum) return;
	if (maximum <= clientWidth) return;
	int newSelection = Math.min (horizontalOffset + SIZE_HORIZONTALSCROLL, maximum - clientWidth);
	update ();
	GC gc = new GC (this);
	gc.copyArea (
		0, 0,
		clientArea.width, clientArea.height,
		horizontalOffset - newSelection, 0);
	gc.dispose ();
	if (header.getVisible ()) {
		Rectangle headerClientArea = header.getClientArea ();
		header.update ();
		gc = new GC (header);
		gc.copyArea (
			0, 0,
			headerClientArea.width, headerClientArea.height,
			horizontalOffset - newSelection, 0);
		gc.dispose();
	}
	horizontalOffset = newSelection;
	hBar.setSelection (horizontalOffset);
}
void onArrowUp (int stateMask) {
	if ((stateMask & (SWT.SHIFT | SWT.CTRL)) == 0) {
		/* Up Arrow with no modifiers */
		int newFocusIndex = focusItem.index - 1;
		if (newFocusIndex < 0) return; 		/* at top */
		TableItem item = items [newFocusIndex];
		selectItem (item, false);
		setFocusItem (item, true);
		redrawItem (newFocusIndex, true);
		showItem (item);
		Event newEvent = new Event ();
		newEvent.item = item;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.CTRL) != 0) {
			/* CTRL+Up Arrow, CTRL+Shift+Up Arrow */
			if (topIndex == 0) return;	/* at top */
			update ();
			topIndex--;
			ScrollBar vBar = getVerticalBar ();
			if (vBar != null) vBar.setSelection (topIndex);
			GC gc = new GC (this);
			gc.copyArea (
				0, 0,
				clientArea.width, clientArea.height,
				0, itemHeight);
			gc.dispose ();
			return;
		}
		/* Shift+Up Arrow */
		int newFocusIndex = focusItem.index - 1;
		if (newFocusIndex < 0) return; 	/* at top */
		TableItem item = items [newFocusIndex];
		selectItem (item, false);
		setFocusItem (item, true);
		redrawItem (newFocusIndex, true);
		showItem (item);
		Event newEvent = new Event ();
		newEvent.item = item;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			/* CTRL+Shift+Up Arrow */
			if (topIndex == 0) return;	/* at top */
			update ();
			topIndex--;
			ScrollBar vBar = getVerticalBar ();
			if (vBar != null) vBar.setSelection (topIndex);
			GC gc = new GC (this);
			gc.copyArea (
				0, 0,
				clientArea.width, clientArea.height,
				0, itemHeight);
			gc.dispose ();
			return;
		}
		/* CTRL+Up Arrow */
		int focusIndex = focusItem.index; 
		if (focusIndex == 0) return;	/* at top */
		TableItem newFocusItem = items [focusIndex - 1];
		setFocusItem (newFocusItem, true);
		showItem (newFocusItem);
		redrawItem (newFocusItem.index, true);
		return;
	}
	/* Shift+Up Arrow */
	int newFocusIndex = focusItem.index - 1;
	if (newFocusIndex < 0) return; 		/* at top */
	if (anchorItem == null) anchorItem = focusItem;
	if (anchorItem.index < focusItem.index) {
		deselectItem (focusItem);
		redrawItem (focusItem.index, true);
	}
	TableItem item = items [newFocusIndex];
	selectItem (item, true);
	setFocusItem (item, true);
	redrawItem (newFocusIndex, true);
	showItem (item);
	Event newEvent = new Event ();
	newEvent.item = item;
	postEvent (SWT.Selection, newEvent);
}
void onCR () {
	if (focusItem == null) return;
	Event event = new Event ();
	event.item = focusItem;
	postEvent (SWT.DefaultSelection, event);
}
void onDispose (Event event) {
	if (isDisposed ()) return;
	if (ignoreDispose) return;
	ignoreDispose = true;
	notifyListeners(SWT.Dispose, event);
	event.type = SWT.None;
	for (int i = 0; i < itemsCount; i++) {
		items [i].dispose (false);
	}
	for (int i = 0; i < columns.length; i++) {
		columns [i].dispose (false);
	}
	if (toolTipShell != null) {
		toolTipShell.dispose ();
		toolTipShell = null;
		toolTipLabel = null;
	}
	toolTipListener = null;
	itemsCount = topIndex = horizontalOffset = 0;
	items = selectedItems = null;
	columns = orderedColumns = null;
	focusItem = anchorItem = lastClickedItem = null;
	lastSelectionEvent = null;
	header = null;
	resizeColumn = sortColumn = null;
	cachedBackground = cachedForeground = null;
}
void onEnd (int stateMask) {
	int lastAvailableIndex = itemsCount - 1;
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
		/* End with no modifiers */
		if (focusItem.index == lastAvailableIndex) return; 	/* at bottom */
		TableItem item = items [lastAvailableIndex]; 
		selectItem (item, false);
		setFocusItem (item, true);
		redrawItem (lastAvailableIndex, true);
		showItem (item);
		Event newEvent = new Event ();
		newEvent.item = item;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.CTRL) != 0) {
			/* CTRL+End, CTRL+Shift+End */
			int visibleItemCount = (clientArea.height - getHeaderHeight ()) / itemHeight;
			setTopIndex (itemsCount - visibleItemCount);
			return;
		}
		/* Shift+End */
		if (focusItem.index == lastAvailableIndex) return; /* at bottom */
		TableItem item = items [lastAvailableIndex]; 
		selectItem (item, false);
		setFocusItem (item, true);
		redrawItem (lastAvailableIndex, true);
		showItem (item);
		Event newEvent = new Event ();
		newEvent.item = item;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			/* CTRL+Shift+End */
			showItem (items [lastAvailableIndex]);
			return;
		}
		/* CTRL+End */
		if (focusItem.index == lastAvailableIndex) return; /* at bottom */
		TableItem item = items [lastAvailableIndex];
		setFocusItem (item, true);
		showItem (item);
		redrawItem (item.index, true);
		return;
	}
	/* Shift+End */
	if (anchorItem == null) anchorItem = focusItem;
	TableItem selectedItem = items [lastAvailableIndex];
	if (selectedItem == focusItem && selectedItem.isSelected ()) return;
	int anchorIndex = anchorItem.index;
	int selectIndex = selectedItem.index;
	TableItem[] newSelection = new TableItem [selectIndex - anchorIndex + 1];
	int writeIndex = 0;
	for (int i = anchorIndex; i <= selectIndex; i++) {
		newSelection [writeIndex++] = items [i];
	}
	setSelection (newSelection, false);
	setFocusItem (selectedItem, true);
	redrawItems (anchorIndex, selectIndex, true);
	showItem (selectedItem);
	Event newEvent = new Event ();
	newEvent.item = selectedItem;
	postEvent (SWT.Selection, newEvent);
}
void onFocusIn () {
	hasFocus = true;
	if (itemsCount == 0) {
		redraw ();
		return;
	}
	if ((style & (SWT.HIDE_SELECTION | SWT.MULTI)) == (SWT.HIDE_SELECTION | SWT.MULTI)) {
		for (int i = 0; i < selectedItems.length; i++) {
			redrawItem (selectedItems [i].index, true);
		}
	}
	if (focusItem != null) {
		redrawItem (focusItem.index, true);
		return;
	}
	/* an initial focus item must be selected */
	TableItem initialFocus;
	if (selectedItems.length > 0) {
		initialFocus = selectedItems [0];
	} else {
		initialFocus = items [topIndex];
	}
	setFocusItem (initialFocus, false);
	redrawItem (initialFocus.index, true);
	return;
}
void onFocusOut () {
	hasFocus = false;
	if (itemsCount == 0) {
		redraw ();
		return;
	}
	if (focusItem != null) {
		redrawItem (focusItem.index, true);
	}
	if ((style & (SWT.HIDE_SELECTION | SWT.MULTI)) == (SWT.HIDE_SELECTION | SWT.MULTI)) {
		for (int i = 0; i < selectedItems.length; i++) {
			redrawItem (selectedItems [i].index, true);
		}
	}
}
void onHome (int stateMask) {
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
		/* Home with no modifiers */
		if (focusItem.index == 0) return; 		/* at top */
		TableItem item = items [0];
		selectItem (item, false);
		setFocusItem (item, true);
		redrawItem (0, true);
		showItem (item);
		Event newEvent = new Event ();
		newEvent.item = item;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.CTRL) != 0) {
			/* CTRL+Home, CTRL+Shift+Home */
			setTopIndex (0);
			return;
		}
		/* Shift+Home */
		if (focusItem.index == 0) return; 		/* at top */
		TableItem item = items [0];
		selectItem (item, false);
		setFocusItem (item, true);
		redrawItem (0, true);
		showItem (item);
		Event newEvent = new Event ();
		newEvent.item = item;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			/* CTRL+Shift+Home */
			setTopIndex (0);
			return;
		}
		/* CTRL+Home */
		if (focusItem.index == 0) return; /* at top */
		TableItem item = items [0];
		setFocusItem (item, true);
		showItem (item);
		redrawItem (item.index, true);
		return;
	}
	/* Shift+Home */
	if (anchorItem == null) anchorItem = focusItem;
	TableItem selectedItem = items [0];
	if (selectedItem == focusItem && selectedItem.isSelected ()) return;
	int anchorIndex = anchorItem.index;
	int selectIndex = selectedItem.index;
	TableItem[] newSelection = new TableItem [anchorIndex + 1];
	int writeIndex = 0;
	for (int i = anchorIndex; i >= 0; i--) {
		newSelection [writeIndex++] = items [i];
	}
	setSelection (newSelection, false);
	setFocusItem (selectedItem, true);
	redrawItems (anchorIndex, selectIndex, true);
	showItem (selectedItem);
	Event newEvent = new Event ();
	newEvent.item = selectedItem;
	postEvent (SWT.Selection, newEvent);
}
void onKeyDown (Event event) {
	if (ignoreKey) {
		ignoreKey = false;
		return;
	}
	ignoreKey = true;
	notifyListeners (event.type, event);
	event.type = SWT.None;
	if (!event.doit) return;
	if (focusItem == null) return;
	if ((event.stateMask & SWT.SHIFT) == 0 && event.keyCode != SWT.SHIFT) {
		anchorItem = null;
	}
	switch (event.keyCode) {
		case SWT.ARROW_UP:
			onArrowUp (event.stateMask);
			return;
		case SWT.ARROW_DOWN:
			onArrowDown (event.stateMask);
			return;
		case SWT.ARROW_LEFT:
			onArrowLeft (event.stateMask);
			return;
		case SWT.ARROW_RIGHT:
			onArrowRight (event.stateMask);
			return;
		case SWT.PAGE_UP:
			onPageUp (event.stateMask);
			return;
		case SWT.PAGE_DOWN:
			onPageDown (event.stateMask);
			return;
		case SWT.HOME:
			onHome (event.stateMask);
			return;
		case SWT.END:
			onEnd (event.stateMask);
			return;
	}
	if (event.character == ' ') {
		onSpace ();
		return;
	}
	if (event.character == SWT.CR) {
		onCR ();
		return;
	}
	if ((event.stateMask & SWT.CTRL) != 0) return;
	
	int initialIndex = focusItem.index;
	char character = Character.toLowerCase (event.character);
	/* check available items from current focus item to bottom */
	for (int i = initialIndex + 1; i < itemsCount; i++) {
		TableItem item = items [i];
		String text = item.getText (0, false);
		if (text.length () > 0) {
			if (Character.toLowerCase (text.charAt (0)) == character) {
				selectItem (item, false);
				setFocusItem (item, true);
				redrawItem (i, true);
				showItem (item);
				Event newEvent = new Event ();
				newEvent.item = item;
				postEvent (SWT.Selection, newEvent);
				return;
			}
		}
	}
	/* check available items from top to current focus item */
	for (int i = 0; i < initialIndex; i++) {
		TableItem item = items [i];
		String text = item.getText (0, false);
		if (text.length () > 0) {
			if (Character.toLowerCase (text.charAt (0)) == character) {
				selectItem (item, false);
				setFocusItem (item, true);
				redrawItem (i, true);
				showItem (item);
				Event newEvent = new Event ();
				newEvent.item = item;
				postEvent (SWT.Selection, newEvent);
				return;
			}
		}
	}
}
void onMouseDoubleClick (Event event) {
	if (!isFocusControl ()) setFocus ();
	int index = (event.y - getHeaderHeight ()) / itemHeight + topIndex;
	if  (!(0 <= index && index < itemsCount)) return;	/* not on an available item */
	TableItem selectedItem = items [index];
	
	/* 
	 * If the two clicks of the double click did not occur over the same item then do not
	 * consider this to be a default selection.
	 */
	if (selectedItem != lastClickedItem) return;

	if (!selectedItem.getHitBounds ().contains (event.x, event.y)) return;	/* considers x */
	
	Event newEvent = new Event ();
	newEvent.item = selectedItem;
	postEvent (SWT.DefaultSelection, newEvent);
}
void onMouseDown (Event event) {
	if (!isFocusControl ()) forceFocus ();
	int index = (event.y - getHeaderHeight ()) / itemHeight + topIndex;
	if (!(0 <= index && index < itemsCount)) return;	/* not on an available item */
	TableItem selectedItem = items [index];
	
	/* if click was in checkbox */
	if ((style & SWT.CHECK) != 0 && selectedItem.getCheckboxBounds ().contains (event.x, event.y)) {
		if (event.button != 1) return;
		selectedItem.setChecked (!selectedItem.checked);
		Event newEvent = new Event ();
		newEvent.item = selectedItem;
		newEvent.detail = SWT.CHECK;
		postEvent (SWT.Selection, newEvent);
		return;
	}
	
	if (!selectedItem.getHitBounds ().contains (event.x, event.y)) return;

	if ((event.stateMask & SWT.SHIFT) == 0 && event.keyCode != SWT.SHIFT) anchorItem = null;

	boolean sendSelection = true;
	/* Detect when this is the second click of a DefaultSelection and don't fire Selection */
	if (lastSelectionEvent != null && lastSelectionEvent.item == selectedItem) {
		if (event.time - lastSelectionEvent.time <= display.getDoubleClickTime ()) {
			sendSelection = false;
		} else {
			lastSelectionEvent = event;
			event.item = selectedItem;
		}
	} else {
		lastSelectionEvent = event;
		event.item = selectedItem;
	}

	if ((style & SWT.SINGLE) != 0) {
		if (!selectedItem.isSelected ()) {
			if (event.button == 1) {
				selectItem (selectedItem, false);
				setFocusItem (selectedItem, true);
				redrawItem (selectedItem.index, true);
				if (sendSelection) {
					Event newEvent = new Event ();
					newEvent.item = selectedItem;
					postEvent (SWT.Selection, newEvent);
				}
				return;
			}
			if ((event.stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
				selectItem (selectedItem, false);
				setFocusItem (selectedItem, true);
				redrawItem (selectedItem.index, true);
				if (sendSelection) {
					Event newEvent = new Event ();
					newEvent.item = selectedItem;
					postEvent (SWT.Selection, newEvent);
				}
				return;
			}
		}
		/* item is selected */
		if (event.button == 1) {
			/* fire a selection event, though the selection did not change */
			if (sendSelection) {
				Event newEvent = new Event ();
				newEvent.item = selectedItem;
				postEvent (SWT.Selection, newEvent);
			}
			return;
		}
	}
	/* SWT.MULTI */
	if (!selectedItem.isSelected ()) {
		if (event.button == 1) {
			if ((event.stateMask & (SWT.CTRL | SWT.SHIFT)) == SWT.SHIFT) {
				if (anchorItem == null) anchorItem = focusItem;
				int anchorIndex = anchorItem.index;
				int selectIndex = selectedItem.index;
				TableItem[] newSelection = new TableItem [Math.abs (anchorIndex - selectIndex) + 1];
				int step = anchorIndex < selectIndex ? 1 : -1;
				int writeIndex = 0;
				for (int i = anchorIndex; i != selectIndex; i += step) {
					newSelection [writeIndex++] = items [i];
				}
				newSelection [writeIndex] = items [selectIndex];
				setSelection (newSelection, false);
				setFocusItem (selectedItem, true);
				redrawItems (
					Math.min (anchorIndex, selectIndex),
					Math.max (anchorIndex, selectIndex),
					true);
				if (sendSelection) {
					Event newEvent = new Event ();
					newEvent.item = selectedItem;
					postEvent (SWT.Selection, newEvent);
				}
				return;
			}
			selectItem (selectedItem, (event.stateMask & SWT.CTRL) != 0);
			setFocusItem (selectedItem, true);
			redrawItem (selectedItem.index, true);
			if (sendSelection) {
				Event newEvent = new Event ();
				newEvent.item = selectedItem;
				postEvent (SWT.Selection, newEvent);
			}
			return;
		}
		/* button 3 */
		if ((event.stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
			selectItem (selectedItem, false);
			setFocusItem (selectedItem, true);
			redrawItem (selectedItem.index, true);
			if (sendSelection) {
				Event newEvent = new Event ();
				newEvent.item = selectedItem;
				postEvent (SWT.Selection, newEvent);
			}
			return;
		}
	}
	/* item is selected */
	if (event.button != 1) return;
	if ((event.stateMask & SWT.CTRL) != 0) {
		removeSelectedItem (getSelectionIndex (selectedItem));
		setFocusItem (selectedItem, true);
		redrawItem (selectedItem.index, true);
		if (sendSelection) {
			Event newEvent = new Event ();
			newEvent.item = selectedItem;
			postEvent (SWT.Selection, newEvent);
		}
		return;
	}
	if ((event.stateMask & SWT.SHIFT) != 0) {
		if (anchorItem == null) anchorItem = focusItem;
		int anchorIndex = anchorItem.index;
		int selectIndex = selectedItem.index;
		TableItem[] newSelection = new TableItem [Math.abs (anchorIndex - selectIndex) + 1];
		int step = anchorIndex < selectIndex ? 1 : -1;
		int writeIndex = 0;
		for (int i = anchorIndex; i != selectIndex; i += step) {
			newSelection [writeIndex++] = items [i];
		}
		newSelection [writeIndex] = items [selectIndex];
		setSelection (newSelection, false);
		setFocusItem (selectedItem, true);
		redrawItems (
			Math.min (anchorIndex, selectIndex),
			Math.max (anchorIndex, selectIndex),
			true);
		if (sendSelection) {
			Event newEvent = new Event ();
			newEvent.item = selectedItem;
			postEvent (SWT.Selection, newEvent);
		}
		return;
	}
	selectItem (selectedItem, false);
	setFocusItem (selectedItem, true);
	redrawItem (selectedItem.index, true);
	if (sendSelection) {
		Event newEvent = new Event ();
		newEvent.item = selectedItem;
		postEvent (SWT.Selection, newEvent);
	}
}
void onMouseUp (Event event) {
	int index = (event.y - getHeaderHeight ()) / itemHeight + topIndex;
	if (!(0 <= index && index < itemsCount)) return;	/* not on an available item */
	lastClickedItem = items [index];
}
void onPageDown (int stateMask) {
	int visibleItemCount = (clientArea.height - getHeaderHeight ()) / itemHeight;
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
		/* PageDown with no modifiers */
		int newFocusIndex = focusItem.index + visibleItemCount - 1;
		newFocusIndex = Math.min (newFocusIndex, itemsCount - 1);
		if (newFocusIndex == focusItem.index) return;
		TableItem item = items [newFocusIndex];
		selectItem (item, false);
		setFocusItem (item, true);
		showItem (item);
		redrawItem (item.index, true);
		return;
	}
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == (SWT.CTRL | SWT.SHIFT)) {
		/* CTRL+Shift+PageDown */
		int newTopIndex = topIndex + visibleItemCount;
		newTopIndex = Math.min (newTopIndex, itemsCount - visibleItemCount);
		if (newTopIndex == topIndex) return;
		setTopIndex (newTopIndex);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			/* Shift+PageDown */
			int newFocusIndex = focusItem.index + visibleItemCount - 1;
			newFocusIndex = Math.min (newFocusIndex, itemsCount - 1);
			if (newFocusIndex == focusItem.index) return;
			TableItem item = items [newFocusIndex];
			selectItem (item, false);
			setFocusItem (item, true);
			showItem (item);
			redrawItem (item.index, true);
			return;
		}
		/* CTRL+PageDown */
		int newTopIndex = topIndex + visibleItemCount;
		newTopIndex = Math.min (newTopIndex, itemsCount - visibleItemCount);
		if (newTopIndex == topIndex) return;
		setTopIndex (newTopIndex);
		return;
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		/* CTRL+PageDown */
		int bottomIndex = Math.min (topIndex + visibleItemCount - 1, itemsCount - 1);
		if (focusItem.index != bottomIndex) {
			/* move focus to bottom item in viewport */
			setFocusItem (items [bottomIndex], true);
			redrawItem (bottomIndex, true);
		} else {
			/* at bottom of viewport, so set focus to bottom item one page down */
			int newFocusIndex = Math.min (itemsCount - 1, bottomIndex + visibleItemCount);
			if (newFocusIndex == focusItem.index) return;
			setFocusItem (items [newFocusIndex], true);
			showItem (items [newFocusIndex]);
			redrawItem (newFocusIndex, true);
		}
		return;
	}
	/* Shift+PageDown */
	if (anchorItem == null) anchorItem = focusItem;
	int anchorIndex = anchorItem.index;
	int bottomIndex = Math.min (topIndex + visibleItemCount - 1, itemsCount - 1);
	int selectIndex;
	if (focusItem.index != bottomIndex) {
		/* select from focus to bottom item in viewport */
		selectIndex = bottomIndex;
	} else {
		/* already at bottom of viewport, so select to bottom of one page down */
		selectIndex = Math.min (itemsCount - 1, bottomIndex + visibleItemCount);
		if (selectIndex == focusItem.index && focusItem.isSelected ()) return;
	}
	TableItem selectedItem = items [selectIndex];
	TableItem[] newSelection = new TableItem [Math.abs (anchorIndex - selectIndex) + 1];
	int step = anchorIndex < selectIndex ? 1 : -1;
	int writeIndex = 0;
	for (int i = anchorIndex; i != selectIndex; i += step) {
		newSelection [writeIndex++] = items [i];
	}
	newSelection [writeIndex] = items [selectIndex];
	setSelection (newSelection, false);
	setFocusItem (selectedItem, true);
	showItem (selectedItem);
	Event newEvent = new Event ();
	newEvent.item = selectedItem;
	postEvent (SWT.Selection, newEvent);
}
void onPageUp (int stateMask) {
	int visibleItemCount = (clientArea.height - getHeaderHeight ()) / itemHeight;
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
		/* PageUp with no modifiers */
		int newFocusIndex = Math.max (0, focusItem.index - visibleItemCount + 1);
		if (newFocusIndex == focusItem.index) return;
		TableItem item = items [newFocusIndex];
		selectItem (item, false);
		setFocusItem (item, true);
		showItem (item);
		redrawItem (item.index, true);
		return;
	}
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == (SWT.CTRL | SWT.SHIFT)) {
		/* CTRL+Shift+PageUp */
		int newTopIndex = Math.max (0, topIndex - visibleItemCount);
		if (newTopIndex == topIndex) return;
		setTopIndex (newTopIndex);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			/* Shift+PageUp */
			int newFocusIndex = Math.max (0, focusItem.index - visibleItemCount + 1);
			if (newFocusIndex == focusItem.index) return;
			TableItem item = items [newFocusIndex];
			selectItem (item, false);
			setFocusItem (item, true);
			showItem (item);
			redrawItem (item.index, true);
			return;
		}
		/* CTRL+PageUp */
		int newTopIndex = Math.max (0, topIndex - visibleItemCount);
		if (newTopIndex == topIndex) return;
		setTopIndex (newTopIndex);
		return;
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		/* CTRL+PageUp */
		if (focusItem.index != topIndex) {
			/* move focus to top item in viewport */
			setFocusItem (items [topIndex], true);
			redrawItem (topIndex, true);
		} else {
			/* at top of viewport, so set focus to top item one page up */
			int newFocusIndex = Math.max (0, focusItem.index - visibleItemCount);
			if (newFocusIndex == focusItem.index) return;
			setFocusItem (items [newFocusIndex], true);
			showItem (items [newFocusIndex]);
			redrawItem (newFocusIndex, true);
		}
		return;
	}
	/* Shift+PageUp */
	if (anchorItem == null) anchorItem = focusItem;
	int anchorIndex = anchorItem.index;
	int selectIndex;
	if (focusItem.index != topIndex) {
		/* select from focus to top item in viewport */
		selectIndex = topIndex;
	} else {
		/* already at top of viewport, so select to top of one page up */
		selectIndex = Math.max (0, topIndex - visibleItemCount);
		if (selectIndex == focusItem.index && focusItem.isSelected ()) return;
	}
	TableItem selectedItem = items [selectIndex];
	TableItem[] newSelection = new TableItem [Math.abs (anchorIndex - selectIndex) + 1];
	int step = anchorIndex < selectIndex ? 1 : -1;
	int writeIndex = 0;
	for (int i = anchorIndex; i != selectIndex; i += step) {
		newSelection [writeIndex++] = items [i];
	}
	newSelection [writeIndex] = items [selectIndex];
	setSelection (newSelection, false);
	setFocusItem (selectedItem, true);
	showItem (selectedItem);
	Event newEvent = new Event ();
	newEvent.item = selectedItem;
	postEvent (SWT.Selection, newEvent);
}
void onPaint (Event event) {
	TableColumn[] orderedColumns = getOrderedColumns ();
	GC gc = event.gc;
	Rectangle clipping = gc.getClipping ();
	int headerHeight = getHeaderHeight ();
	int numColumns = orderedColumns.length;
	int startColumn = -1, endColumn = -1;
	if (numColumns > 0) {
		startColumn = computeColumnIntersect (clipping.x, 0);
		if (startColumn != -1) {	/* the clip x is within a column's bounds */
			endColumn = computeColumnIntersect (clipping.x + clipping.width, startColumn);
			if (endColumn == -1) endColumn = numColumns - 1;
		}
	} else {
		startColumn = endColumn = 0;
	}

	/* Determine the items to be painted */
	int startIndex = (clipping.y - headerHeight) / itemHeight + topIndex;
	int endIndex = -1;
	if (startIndex < itemsCount) {
		endIndex = startIndex + Compatibility.ceil (clipping.height, itemHeight);
	}
	startIndex = Math.max (0, startIndex);
	endIndex = Math.min (endIndex, itemsCount - 1);

	/* fill background not handled by items */
	cachedBackground = getBackground ();
	gc.setBackground (cachedBackground);
	gc.setClipping (clipping);
	int bottomY = endIndex >= 0 ? getItemY (items [endIndex]) + itemHeight : 0;
	int fillHeight = Math.max (0, clientArea.height - bottomY);
	if (fillHeight > 0) {	/* space below bottom item */
		drawBackground (gc, 0, bottomY, clientArea.width, fillHeight, 0, 0);
	}
	if (columns.length > 0) {
		TableColumn column = orderedColumns [orderedColumns.length - 1];	/* last column */
		int rightX = column.getX () + column.width;
		if (rightX < clientArea.width) {
			drawBackground (gc, rightX, 0, clientArea.width - rightX, clientArea.height - fillHeight, 0, 0);
		}
	}

	/* paint the items */
	boolean noFocusDraw = false;
	int[] lineDash = gc.getLineDash ();
	int lineWidth = gc.getLineWidth ();
	cachedForeground = getForeground ();
	for (int i = startIndex; i <= Math.min (endIndex, itemsCount - 1); i++) {
		TableItem item = items [i];
		if (!item.isDisposed ()) {	/* ensure that item was not disposed in a callback */
			if (startColumn == -1) {
				/* indicates that region to paint is to the right of the last column */
				noFocusDraw = item.paint (gc, null, true) || noFocusDraw;
			} else {
				if (numColumns == 0) {
					noFocusDraw = item.paint (gc, null, false) || noFocusDraw;
				} else {
					for (int j = startColumn; j <= Math.min (endColumn, columns.length - 1); j++) {
						if (!item.isDisposed ()) {	/* ensure that item was not disposed in a callback */
							noFocusDraw = item.paint (gc, orderedColumns [j], false) || noFocusDraw;
						}
						if (isDisposed () || gc.isDisposed ()) { /* ensure that receiver was not disposed in a callback */
							cachedBackground = cachedForeground = null;
							return;
						}
					}
				}
			}
		}
		if (isDisposed () || gc.isDisposed ()) { /* ensure that receiver was not disposed in a callback */
			cachedBackground = cachedForeground = null;
			return;
		}
	}
	cachedBackground = cachedForeground = null;

	/* repaint grid lines */
	gc.setClipping(clipping);
	gc.setLineWidth (lineWidth);
	if (linesVisible) {
		gc.setForeground (display.getSystemColor (SWT.COLOR_WIDGET_LIGHT_SHADOW));
		gc.setLineDash (lineDash);
		if (numColumns > 0 && startColumn != -1) {
			/* vertical column lines */
			for (int i = startColumn; i <= endColumn; i++) {
				int x = orderedColumns [i].getX () + orderedColumns [i].width - 1;
				gc.drawLine (x, clipping.y, x, clipping.y + clipping.height);
			}
		}
		/* horizontal item lines */
		bottomY = clipping.y + clipping.height;
		int rightX = clipping.x + clipping.width;
		int y = (clipping.y - headerHeight) / itemHeight * itemHeight + headerHeight;
		while (y <= bottomY) {
			gc.drawLine (clipping.x, y, rightX, y);
			y += itemHeight;
		}
	}

	/* paint focus rectangle */
	if (!noFocusDraw && isFocusControl ()) {
		if (focusItem != null) {
			Rectangle focusBounds = focusItem.getFocusBounds ();
			if (focusBounds.width > 0) {
				gc.setForeground (display.getSystemColor (SWT.COLOR_BLACK));
				gc.setClipping (focusBounds);
				if (focusItem.isSelected ()) {
					gc.setLineDash (new int[] {2, 2});
				} else {
					gc.setLineDash (new int[] {1, 1});
				}
				gc.drawFocus (focusBounds.x, focusBounds.y, focusBounds.width, focusBounds.height);
			}
		} else {
			/* no items, so draw focus border around Table */
			int y = headerHeight + 1;
			int width = Math.max (0, clientArea.width - 2);
			int height = Math.max (0, clientArea.height - headerHeight - 2);
			gc.setForeground (display.getSystemColor (SWT.COLOR_BLACK));
			gc.setClipping (1, y, width, height);
			gc.setLineDash (new int[] {1, 1});
			gc.drawFocus (1, y, width, height);
		}
	}
}
void onResize (Event event) {
	clientArea = getClientArea ();
	/* vertical scrollbar */
	ScrollBar vBar = getVerticalBar ();
	if (vBar != null) {
		int clientHeight = (clientArea.height - getHeaderHeight ()) / itemHeight;
		int thumb = Math.min (clientHeight, itemsCount);
		vBar.setThumb (thumb);
		vBar.setPageIncrement (thumb);
		int index = vBar.getSelection ();
		if (index != topIndex) {
			topIndex = index;
			redraw ();
		}
		boolean visible = clientHeight < itemsCount;
		if (visible != vBar.getVisible ()) {
			vBar.setVisible (visible);
			clientArea = getClientArea ();
		}
	}
	
	/* horizontal scrollbar */ 
	ScrollBar hBar = getHorizontalBar (); 
	if (hBar != null) {
		int hBarMaximum = hBar.getMaximum ();
		int thumb = Math.min (clientArea.width, hBarMaximum);
		hBar.setThumb (thumb);
		hBar.setPageIncrement (thumb);
		horizontalOffset = hBar.getSelection ();
		boolean visible = clientArea.width < hBarMaximum;
		if (visible != hBar.getVisible ()) {
			hBar.setVisible (visible);
			clientArea = getClientArea ();
		}
	}
	
	/* header */
	int headerHeight = Math.max (fontHeight, headerImageHeight) + 2 * getHeaderPadding ();
	header.setSize (clientArea.width, headerHeight);
	
	/* if this is the focus control but there are no items then the boundary focus ring must be repainted */
	if (itemsCount == 0 && isFocusControl ()) redraw ();
}
void onScrollHorizontal (Event event) {
	ScrollBar hBar = getHorizontalBar (); 
	if (hBar == null) return;
	int newSelection = hBar.getSelection ();
	update ();
	if (itemsCount > 0) {
		GC gc = new GC (this);
		gc.copyArea (
			0, 0,
			clientArea.width, clientArea.height,
			horizontalOffset - newSelection, 0);
		gc.dispose ();
	} else {
		redraw ();	/* ensure that static focus rectangle updates properly */
	}

	if (drawCount <= 0 && header.isVisible ()) {
		header.update ();
		Rectangle headerClientArea = header.getClientArea ();
		GC gc = new GC (header);
		gc.copyArea (
			0, 0,
			headerClientArea.width, headerClientArea.height,
			horizontalOffset - newSelection, 0);
		gc.dispose ();
	}
	horizontalOffset = newSelection;
}
void onScrollVertical (Event event) {
	ScrollBar vBar = getVerticalBar ();
	if (vBar == null) return;
	int newSelection = vBar.getSelection ();
	update ();
	GC gc = new GC (this);
	gc.copyArea (
		0, 0,
		clientArea.width, clientArea.height,
		0, (topIndex - newSelection) * itemHeight);
	gc.dispose ();
	topIndex = newSelection;
}
void onSpace () {
	if (focusItem == null) return;
	if (!focusItem.isSelected ()) {
		selectItem (focusItem, (style & SWT.MULTI) != 0);
		redrawItem (focusItem.index, true);
	}
	if ((style & SWT.CHECK) != 0) {
		focusItem.setChecked (!focusItem.checked);
	}
	showItem (focusItem);
	Event event = new Event ();
	event.item = focusItem;
	postEvent (SWT.Selection, event);
	if ((style & SWT.CHECK) == 0) return;

	/* SWT.CHECK */
	event = new Event ();
	event.item = focusItem;
	event.detail = SWT.CHECK;
	postEvent (SWT.Selection, event);
}
/*
 * The current focus item is about to become unavailable, so reassign focus.
 */
void reassignFocus () {
	if (focusItem == null) return;
	
	/* 
	 * reassign to the previous root-level item if there is one, or the next
	 * root-level item otherwise
	 */
	int index = focusItem.index;
	if (index != 0) {
		index--;
	} else {
		index++;
	}
	if (index < itemsCount) {
		TableItem item = items [index];
		setFocusItem (item, false);
		showItem (item);
	} else {
		setFocusItem (null, false);		/* no items left */
	}
}
public void redraw () {
	checkWidget ();
	if (drawCount <= 0) super.redraw ();
}
public void redraw (int x, int y, int width, int height, boolean all) {
	checkWidget ();
	if (drawCount <= 0) super.redraw (x, y, width, height, all);
}
/* 
 * Redraws from the specified index down to the last available item inclusive.  Note
 * that the redraw bounds do not extend beyond the current last item, so clients
 * that reduce the number of available items should use #redrawItems(int,int) instead
 * to ensure that redrawing extends down to the previous bottom item boundary.
 */
void redrawFromItemDownwards (int index) {
	redrawItems (index, itemsCount - 1, false);
}
/*
 * Redraws the table item at the specified index.  It is valid for this index to reside
 * beyond the last available item.
 */
void redrawItem (int itemIndex, boolean focusBoundsOnly) {
	if (itemIndex < itemsCount && !items [itemIndex].isInViewport ()) return;
	redrawItems (itemIndex, itemIndex, focusBoundsOnly);
}
/*
 * Redraws the table between the start and end item indices inclusive.  It is valid
 * for the end index value to extend beyond the last available item.
 */
void redrawItems (int startIndex, int endIndex, boolean focusBoundsOnly) {
	if (drawCount > 0) return;

	int startY = (startIndex - topIndex) * itemHeight + getHeaderHeight ();
	int height = (endIndex - startIndex + 1) * itemHeight;
	if (focusBoundsOnly) {
		boolean custom = hooks (SWT.EraseItem) || hooks (SWT.PaintItem);
		if (!custom && columns.length > 0) {
			TableColumn lastColumn;
			if ((style & SWT.FULL_SELECTION) != 0) {
				TableColumn[] orderedColumns = getOrderedColumns ();
				lastColumn = orderedColumns [orderedColumns.length - 1];
			} else {
				lastColumn = columns [0];
			}
			int rightX = lastColumn.getX () + lastColumn.getWidth ();
			if (rightX <= 0) return;	/* focus column(s) not visible */
		}
		endIndex = Math.min (endIndex, itemsCount - 1);
		for (int i = startIndex; i <= endIndex; i++) {
			TableItem item = items [i];
			if (item.isInViewport ()) {
				/* if custom painting is being done then repaint the full item */
				if (custom) {
					redraw (0, getItemY (item), clientArea.width, itemHeight, false);
				} else {
					/* repaint the item's focus bounds */
					Rectangle bounds = item.getFocusBounds ();
					redraw (bounds.x, startY, bounds.width, height, false);
				}
			}
		}
	} else {
		redraw (0, startY, clientArea.width, height, false);
	}
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
	if (!(0 <= index && index < itemsCount)) error (SWT.ERROR_INVALID_RANGE);
	items [index].dispose ();
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
	if (!(0 <= start && start <= end && end < itemsCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (start == 0 && end == itemsCount - 1) {
		removeAll ();
	} else {
		for (int i = end; i >= start; i--) {
			items [i].dispose ();
		}
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
	sortDescent (newIndices);
	int start = newIndices [newIndices.length - 1], end = newIndices [0];
	if (!(0 <= start && start <= end && end < itemsCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int lastRemovedIndex = -1;
	for (int i = 0; i < newIndices.length; i++) {
		if (newIndices [i] != lastRemovedIndex) {
			items [newIndices [i]].dispose ();
			lastRemovedIndex = newIndices [i];
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
	checkWidget ();
	if (itemsCount == 0) return;
	setRedraw (false);

	setFocusItem (null, false);
	for (int i = 0; i < itemsCount; i++) {
		items [i].dispose (false);
	}
	items = new TableItem [0];
	selectedItems = new TableItem [0];
	itemsCount = topIndex = 0;
	anchorItem = lastClickedItem = null;
	lastSelectionEvent = null;
	ScrollBar vBar = getVerticalBar ();
	if (vBar != null) {
		vBar.setMaximum (1);
		vBar.setVisible (false);
	}
	if (columns.length == 0) {
		horizontalOffset = 0;
		ScrollBar hBar = getHorizontalBar (); 
		if (hBar != null) {
			hBar.setMaximum (1);
			hBar.setVisible (false);
		}
	}

	setRedraw (true);
}
String removeMnemonics (String string) {
	/* removes single ampersands and preserves double-ampersands */
	char [] chars = new char [string.length ()];
	string.getChars (0, chars.length, chars, 0);
	int i = 0, j = 0;
	for ( ; i < chars.length; i++, j++) {
		if (chars[i] == '&') {
			if (++i == chars.length) break;
			if (chars[i] == '&') {
				chars[j++] = chars[i - 1];
			}
		}
		chars[j] = chars[i];
	}
	if (i == j) return string;
	return new String (chars, 0, j);
}
void removeSelectedItem (int index) {
	TableItem[] newSelectedItems = new TableItem [selectedItems.length - 1];
	System.arraycopy (selectedItems, 0, newSelectedItems, 0, index);
	System.arraycopy (selectedItems, index + 1, newSelectedItems, index, newSelectedItems.length - index);
	selectedItems = newSelectedItems;
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	removeListener (SWT.Selection, listener);
	removeListener (SWT.DefaultSelection, listener);	
}
void reskinChildren (int flags) {
	if (items != null) {
		for (int i=0; i<itemsCount; i++) {
			TableItem item = items [i];
			if (item != null) item.reskin (flags);
		}
	}
	if (columns != null) {
		for (int i=0; i<columns.length; i++) {
			TableColumn column = columns [i];
			if (!column.isDisposed ()) column.reskin (flags);
		}
	}
	super.reskinChildren (flags);
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
	if (!(0 <= index && index < itemsCount)) return;
	selectItem (items [index], (style & SWT.MULTI) != 0);
	if (hasFocus () || (style & SWT.HIDE_SELECTION) == 0) {
		redrawItem (index, false);
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
	if (itemsCount == 0 || start >= itemsCount) return;
	start = Math.max (start, 0);
	end = Math.min (end, itemsCount - 1);
	for (int i = start; i <= end; i++) {
		selectItem (items [i], (style & SWT.MULTI) != 0);
	}
	if (hasFocus () || (style & SWT.HIDE_SELECTION) == 0) {
		redrawItems (start, end, false);
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
	if (indices.length == 0 || ((style & SWT.SINGLE) != 0 && indices.length > 1)) return;

	for (int i = 0; i < indices.length; i++) {
		if (0 <= indices [i] && indices [i] < itemsCount) {
			selectItem (items [indices [i]], (style & SWT.MULTI) != 0);
		}
	}
	if (hasFocus () || (style & SWT.HIDE_SELECTION) == 0) {
		for (int i = 0; i < indices.length; i++) {
			if (0 <= indices [i] && indices [i] < itemsCount) {
				redrawItem (indices [i], false);
			}
		}
	}
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
	selectedItems = new TableItem [itemsCount];
	System.arraycopy (items, 0, selectedItems, 0, itemsCount);
	if (hasFocus () || (style & SWT.HIDE_SELECTION) == 0) {
		redraw ();
	}
}
void selectItem (TableItem item, boolean addToSelection) {
	TableItem[] oldSelectedItems = selectedItems;
	if (!addToSelection || (style & SWT.SINGLE) != 0) {
		selectedItems = new TableItem[] {item};
		if (hasFocus () || (style & SWT.HIDE_SELECTION) == 0) {
			for (int i = 0; i < oldSelectedItems.length; i++) {
				if (oldSelectedItems [i] != item) {
					redrawItem (oldSelectedItems [i].index, true);
				}
			}
		}
	} else {
		if (item.isSelected ()) return;
		selectedItems = new TableItem [selectedItems.length + 1];
		System.arraycopy (oldSelectedItems, 0, selectedItems, 0, oldSelectedItems.length);
		selectedItems [selectedItems.length - 1] = item;
	}
}
public void setBackground (Color color) {
	checkWidget ();
	if (color == null) color = display.getSystemColor (SWT.COLOR_LIST_BACKGROUND); 
	super.setBackground (color);
}
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	cachedBackground = null;
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
	if (columns.length == 0) {
		if (order.length != 0) error (SWT.ERROR_INVALID_ARGUMENT);
		return;
	}
	if (order.length != columns.length) error (SWT.ERROR_INVALID_ARGUMENT);
	boolean reorder = false;
	boolean [] seen = new boolean [columns.length];
	int[] oldOrder = getColumnOrder ();
	for (int i = 0; i < order.length; i++) {
		int index = order [i];
		if (index < 0 || index >= columns.length) error (SWT.ERROR_INVALID_RANGE);
		if (seen [index]) error (SWT.ERROR_INVALID_ARGUMENT);
		seen [index] = true;
		if (index != oldOrder [i]) reorder = true;
	}
	if (!reorder) return;

	headerHideToolTip ();
	int[] oldX = new int [columns.length];
	for (int i = 0; i < columns.length; i++) {
		oldX [i] = columns [i].getX ();
	}
	orderedColumns = new TableColumn [order.length];
	for (int i = 0; i < order.length; i++) {
		orderedColumns [i] = columns [order [i]];
	}
	for (int i = 0; i < orderedColumns.length; i++) {
		TableColumn column = orderedColumns [i];
		if (!column.isDisposed () && column.getX () != oldX [column.getIndex ()]) {
			column.sendEvent (SWT.Move);
		}
	}

	redraw ();
	if (drawCount <= 0 && header.isVisible ()) header.redraw ();
}
void setFocusItem (TableItem item, boolean redrawOldFocus) {
	if (item == focusItem) return;
	TableItem oldFocusItem = focusItem;
	focusItem = item;
	if (redrawOldFocus && oldFocusItem != null) {
		redrawItem (oldFocusItem.index, true);
	}
}
public void setFont (Font value) {
	checkWidget ();
	Font oldFont = getFont ();
	super.setFont (value);
	Font font = getFont ();
	if (font.equals (oldFont)) return;
		
	GC gc = new GC (this);
	
	/* recompute the receiver's cached font height and item height values */
	fontHeight = gc.getFontMetrics ().getHeight ();
	setItemHeight (Math.max (fontHeight, imageHeight) + 2 * getCellPadding ());
	Point headerSize = header.getSize ();
	int newHeaderHeight = Math.max (fontHeight, headerImageHeight) + 2 * getHeaderPadding ();
	if (headerSize.y != newHeaderHeight) {
		header.setSize (headerSize.x, newHeaderHeight);
	}
	header.setFont (font);

	/* 
	 * Notify all columns and items of the font change so that elements that
	 * use the receiver's font can recompute their cached string widths.
	 */
	for (int i = 0; i < columns.length; i++) {
		columns [i].updateFont (gc);
	}
	for (int i = 0; i < itemsCount; i++) {
		items [i].updateFont (gc);
	}
	
	gc.dispose ();
	
	if (drawCount <= 0 && header.isVisible ()) header.redraw ();
	
	/* update scrollbars */
	if (columns.length == 0) updateHorizontalBar ();
	ScrollBar vBar = getVerticalBar ();
	if (vBar != null) {
		int thumb = (clientArea.height - getHeaderHeight ()) / itemHeight;
		vBar.setThumb (thumb);
		vBar.setPageIncrement (thumb);
		topIndex = vBar.getSelection ();
		vBar.setVisible (thumb < vBar.getMaximum ());
	}
	redraw ();
}
public void setForeground (Color color) {
	checkWidget ();
	if (color == null) color = display.getSystemColor (SWT.COLOR_LIST_FOREGROUND); 
	super.setForeground (color);
}
void setForegroundPixel (int pixel) {
	super.setForegroundPixel (pixel);
	cachedForeground = null;
}
void setHeaderImageHeight (int value) {
	headerImageHeight = value;
	Point headerSize = header.getSize ();
	int newHeaderHeight = Math.max (fontHeight, headerImageHeight) + 2 * getHeaderPadding ();
	if (headerSize.y != newHeaderHeight) {
		header.setSize (headerSize.x, newHeaderHeight);
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
public void setHeaderVisible (boolean value) {
	checkWidget ();
	if (header.getVisible () == value) return;		/* no change */
	headerHideToolTip ();
	header.setVisible (value);
	updateVerticalBar ();
	redraw ();
}
void setImageHeight (int value) {
	imageHeight = value;
	setItemHeight (Math.max (fontHeight, imageHeight) + 2 * getCellPadding ());
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
	if (count == itemsCount) return;
	int oldCount = itemsCount;
	int redrawStart, redrawEnd;
	
	/* if the new item count is less than the current count then remove all excess items from the end */
	if (count < itemsCount) {
		redrawStart = count;
		redrawEnd = itemsCount - 1;
		for (int i = count; i < itemsCount; i++) {
			items [i].dispose (false);
		}

		int newSelectedCount = 0;
		for (int i = 0; i < selectedItems.length; i++) {
			if (!selectedItems [i].isDisposed ()) newSelectedCount++;
		}
		if (newSelectedCount != selectedItems.length) {
			/* one or more selected items have been disposed */
			TableItem[] newSelectedItems = new TableItem [newSelectedCount];
			int pos = 0;
			for (int i = 0; i < selectedItems.length; i++) {
				TableItem item = selectedItems [i];
				if (!item.isDisposed ()) {
					newSelectedItems [pos++] = item;
				}
			}
			selectedItems = newSelectedItems;
		}

		if (anchorItem != null && anchorItem.isDisposed ()) anchorItem = null;
		if (lastClickedItem != null && lastClickedItem.isDisposed ()) lastClickedItem = null;
		if (focusItem != null && focusItem.isDisposed ()) {
			TableItem newFocusItem = count > 0 ? items [count - 1] : null; 
			setFocusItem (newFocusItem, false);
		}
		itemsCount = count;
		if (columns.length == 0) updateHorizontalBar ();
	} else {
		redrawStart = itemsCount;
		redrawEnd = count - 1;
		TableItem[] newItems = new TableItem [count];
		System.arraycopy (items, 0, newItems, 0, itemsCount);
		items = newItems;
		for (int i = itemsCount; i < count; i++) {
			items [i] = new TableItem (this, SWT.NONE, i, false);
			itemsCount++;
		}
		if (oldCount == 0) focusItem = items [0];
	}

	updateVerticalBar ();
	/*
	 * If this is the focus control and the item count is going from 0->!0 or !0->0 then the
	 * receiver must be redrawn to ensure that its boundary focus ring is updated.
	 */
	if ((oldCount == 0 || itemsCount == 0) && isFocusControl ()) {
		redraw ();
		return;
	}
	redrawItems (redrawStart, redrawEnd, false);
}
boolean setItemHeight (int value) {
	boolean update = !customHeightSet || itemHeight < value; 
	if (update) itemHeight = value;
	return update;
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
public void setLinesVisible (boolean value) {
	checkWidget ();
	if (linesVisible == value) return;		/* no change */
	linesVisible = value;
	redraw ();
}
public void setMenu (Menu menu) {
	super.setMenu (menu);
	header.setMenu (menu);
}
public void setRedraw (boolean value) {
	checkWidget();
	if (value) {
		if (--drawCount == 0) {
			if (items.length - itemsCount > 3) {
				TableItem[] newItems = new TableItem [itemsCount];
				System.arraycopy (items, 0, newItems, 0, itemsCount);
				items = newItems;
			}
			updateVerticalBar ();
			updateHorizontalBar ();
		}
	} else {
		drawCount++;
	}
	super.setRedraw (value);
	header.setRedraw (value);
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
public void setSelection (TableItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (new TableItem[] {item}, true);
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
public void setSelection (TableItem[] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (items, true);
}
void setSelection (TableItem[] items, boolean updateViewport) {
	if (items.length == 0 || ((style & SWT.SINGLE) != 0 && items.length > 1)) {
		deselectAll ();
		return;
	}
	TableItem[] oldSelection = selectedItems;
	
	/* remove null and duplicate items */
	int index = 0;
	selectedItems = new TableItem [items.length];	/* assume all valid items */
	for (int i = 0; i < items.length; i++) {
		TableItem item = items [i];
		if (item != null && item.parent == this && !item.isSelected ()) {
			selectedItems [index++] = item;
		}
	}
	if (index != items.length) {
		/* an invalid item was provided so resize the array accordingly */
		TableItem[] temp = new TableItem [index];
		System.arraycopy (selectedItems, 0, temp, 0, index);
		selectedItems = temp;
	}
	if (selectedItems.length == 0) {	/* no valid items */
		deselectAll ();
		return;
	}

	if (hasFocus () || (style & SWT.HIDE_SELECTION) == 0) {
		for (int i = 0; i < oldSelection.length; i++) {
			if (!oldSelection [i].isSelected ()) {
				redrawItem (oldSelection [i].index, true);
			}
		}
		for (int i = 0; i < selectedItems.length; i++) {
			redrawItem (selectedItems [i].index, true);
		}
	}
	if (updateViewport) {
		showItem (selectedItems [0]);
		setFocusItem (selectedItems [0], true);
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
	if (sortColumn != null && !sortColumn.isDisposed ()) {
		sortColumn.setSortDirection (SWT.NONE);
	}
	sortColumn = column;
	if (sortColumn != null) {
		sortColumn.setSortDirection (sortDirection);
	}
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
public void setSortDirection (int direction) {
	checkWidget ();
	if (direction != SWT.UP && direction != SWT.DOWN && direction != SWT.NONE) return;
	sortDirection = direction;
	if (sortColumn == null || sortColumn.isDisposed ()) return;
	sortColumn.setSortDirection (sortDirection);
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
	checkWidget ();
	deselectAll ();
	if (!(0 <= index && index < itemsCount)) return;
	selectItem (items [index], false);
	setFocusItem (items [index], true);
	redrawItem (index, true);
	showSelection ();
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
	deselectAll ();
	if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) return;
	if (itemsCount == 0 || start >= itemsCount) return;
	start = Math.max (0, start);
	end = Math.min (end, itemsCount - 1);
	select (start, end);
	setFocusItem (items [start], true);
	showSelection ();
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
	deselectAll ();
	int length = indices.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	select (indices);
	int focusIndex = -1;
	for (int i = 0; i < indices.length && focusIndex == -1; i++) {
		if (0 <= indices [i] && indices [i] < itemsCount) {
			focusIndex = indices [i];
		}
	}
	if (focusIndex != -1) setFocusItem (items [focusIndex], true);
	showSelection ();
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
	if (!(0 <= index && index < itemsCount)) return;
	int visibleItemCount = (clientArea.height - getHeaderHeight ()) / itemHeight;
	if (itemsCount <= visibleItemCount) return;
	index = Math.min (index, itemsCount - visibleItemCount);
	if (index == topIndex) return;

	update ();
	int change = topIndex - index;
	topIndex = index;
	ScrollBar vBar = getVerticalBar ();
	if (vBar != null) vBar.setSelection (topIndex);
	if (drawCount <= 0) {
		GC gc = new GC (this);
		gc.copyArea (0, 0, clientArea.width, clientArea.height, 0, change * itemHeight);
		gc.dispose ();
	}
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
	if (column.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (column.parent != this) return;

	int x = column.getX ();
	int rightX = x + column.width;
	if (0 <= x && rightX <= clientArea.width) return;	 /* column is fully visible */

	headerHideToolTip ();
	int absX = 0;	/* the X of the column irrespective of the horizontal scroll */
	TableColumn[] orderedColumns = getOrderedColumns ();
	for (int i = 0; i < column.getOrderIndex (); i++) {
		absX += orderedColumns [i].width;
	}
	if (x < clientArea.x) { 	/* column is to left of viewport */
		horizontalOffset = absX;
	} else {
		horizontalOffset = absX + column.width - clientArea.width;
	}
	ScrollBar hBar = getHorizontalBar (); 
	if (hBar != null) hBar.setSelection (horizontalOffset);
	redraw ();
	if (drawCount <= 0 && header.isVisible ()) header.redraw ();
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
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (item.parent != this) return;
	
	int index = item.index;
	int visibleItemCount = (clientArea.height - getHeaderHeight ()) / itemHeight;
	/* nothing to do if item is already in viewport */
	if (topIndex <= index && index < topIndex + visibleItemCount) return;
	
	if (index <= topIndex) {
		/* item is above current viewport, so show on top */
		setTopIndex (item.index);
	} else {
		/* item is below current viewport, so show on bottom */
		visibleItemCount = Math.max (visibleItemCount, 1);	/* item to show should be top item */
		setTopIndex (Math.min (index - visibleItemCount + 1, itemsCount - 1));
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
 *
 * @see Table#showItem(TableItem)
 */
public void showSelection () {
	checkWidget ();
	if (selectedItems.length == 0) return;
	showItem (selectedItems [0]);
}
void sortDescent (int [] items) {
	/* Shell Sort from K&R, pg 108 */
	int length = items.length;
	for (int gap = length / 2; gap > 0; gap /= 2) {
		for (int i = gap; i < length; i++) {
			for (int j = i - gap; j >= 0; j -= gap) {
				if (items [j] <= items [j + gap]) {
					int swap = items [j];
					items [j] = items [j + gap];
					items [j + gap] = swap;
				}
			}
		}
	}
}
void sortAscent (int [] items) {
	/* Shell Sort from K&R, pg 108 */
	int length = items.length;
	for (int gap = length / 2; gap > 0; gap /= 2) {
		for (int i = gap; i < length; i++) {
			for (int j = i - gap; j >= 0; j -= gap) {
				if (items [j] >= items [j + gap]) {
					int swap = items [j];
					items [j] = items [j + gap];
					items [j + gap] = swap;
				}
			}
		}
	}
}
void sortAscent (TableItem [] items) {
	/* Shell Sort from K&R, pg 108 */
	int length = items.length;
	for (int gap = length / 2; gap > 0; gap /= 2) {
		for (int i = gap; i < length; i++) {
			for (int j = i - gap; j >= 0; j -= gap) {
				if (items [j].index >= items [j + gap].index) {
					TableItem swap = items [j];
					items [j] = items [j + gap];
					items [j + gap] = swap;
				}
			}
		}
	}
}
void updateColumnWidth (TableColumn column, int width) {
	headerHideToolTip ();
	int oldWidth = column.width;
	int columnX = column.getX ();
	int x = columnX + oldWidth - 1;	/* -1 ensures that grid line is included */

	update ();
	GC gc = new GC (this);
	gc.copyArea (x, 0, clientArea.width - x, clientArea.height, columnX + width - 1, 0);	/* dest x -1 offsets x's -1 above */
	if (width > oldWidth) {
		/* column width grew */
		int change = width - oldWidth + 1;	/* +1 offsets x's -1 above */
		/* -1/+1 below ensure that right bound of selection redraws correctly in column */ 
		redraw (x - 1, 0, change + 1, clientArea.height, false);
	} else {
		int change = oldWidth - width + 1;	/* +1 offsets x's -1 above */
		redraw (clientArea.width - change, 0, change, clientArea.height, false);
	}
	/* the focus box must be repainted because its stipple may become shifted as a result of its new width */
	if (focusItem != null) redrawItem (focusItem.index, true);

	GC headerGC = new GC (header);
	if (drawCount <= 0 && header.getVisible ()) {
		Rectangle headerBounds = header.getClientArea ();
		header.update ();
		x -= 1;	/* -1 ensures that full header column separator is included */
		headerGC.copyArea (x, 0, headerBounds.width - x, headerBounds.height, columnX + width - 2, 0);	/* dest x -2 offsets x's -1s above */
		if (width > oldWidth) {
			/* column width grew */
			int change = width - oldWidth + 2;	/* +2 offsets x's -1s above */
			header.redraw (x, 0, change, headerBounds.height, false);
		} else {
			int change = oldWidth - width + 2;	/* +2 offsets x's -1s above */
			header.redraw (headerBounds.width - change, 0, change, headerBounds.height, false);
		}
	}

	column.width = width;

	/*
	 * Notify column and all items of column width change so that display labels
	 * can be recomputed if needed.
	 */
	column.updateWidth (headerGC);
	headerGC.dispose ();
	for (int i = 0; i < itemsCount; i++) {
		items [i].updateColumnWidth (column, gc);
	}
	gc.dispose ();

	int maximum = 0;
	for (int i = 0; i < columns.length; i++) {
		maximum += columns [i].width;
	}
	ScrollBar hBar = getHorizontalBar ();
	if (hBar != null) {
		hBar.setMaximum (Math.max (1, maximum));	/* setting a value of 0 here is ignored */
		if (hBar.getThumb () != clientArea.width) {
			hBar.setThumb (clientArea.width);
			hBar.setPageIncrement (clientArea.width);
		}
		int oldHorizontalOffset = horizontalOffset;	/* hBar.setVisible() can modify horizontalOffset */
		hBar.setVisible (clientArea.width < maximum);
		int selection = hBar.getSelection ();
		if (selection != oldHorizontalOffset) {
			horizontalOffset = selection;
			redraw ();
			if (drawCount <= 0 && header.getVisible ()) header.redraw ();
		}
	}

	column.sendEvent (SWT.Resize);
	TableColumn[] orderedColumns = getOrderedColumns ();
	for (int i = column.getOrderIndex () + 1; i < orderedColumns.length; i++) {
		if (!orderedColumns [i].isDisposed ()) {
			orderedColumns [i].sendEvent (SWT.Move);
		}
	}

	if (itemsCount == 0) redraw ();	/* ensure that static focus rectangle updates properly */
}
/*
 * This is a naive implementation that computes the value from scratch.
 */
void updateHorizontalBar () {
	if (drawCount > 0) return;
	ScrollBar hBar = getHorizontalBar ();
	if (hBar == null) return;
	
	int maxX = 0;
	if (columns.length > 0) {
		for (int i = 0; i < columns.length; i++) {
			maxX += columns [i].width;
		}
	} else {
		for (int i = 0; i < itemsCount; i++) {
			Rectangle itemBounds = items [i].getCellBounds (0);
			maxX = Math.max (maxX, itemBounds.x + itemBounds.width + horizontalOffset);
		}
	}
	
	int clientWidth = clientArea.width;
	if (maxX != hBar.getMaximum ()) {
		hBar.setMaximum (Math.max (1, maxX));	/* setting a value of 0 here is ignored */
	}
	int thumb = Math.min (clientWidth, maxX);
	if (thumb != hBar.getThumb ()) {
		hBar.setThumb (thumb);
		hBar.setPageIncrement (thumb);
	}
	hBar.setVisible (clientWidth < maxX);
	
	/* reclaim any space now left on the right */
	if (maxX < horizontalOffset + thumb) {
		horizontalOffset = maxX - thumb;
		hBar.setSelection (horizontalOffset);
		redraw ();
	} else {
		int selection = hBar.getSelection ();
		if (selection != horizontalOffset) {
			horizontalOffset = selection;
			redraw ();
		}
	}
}
/*
 * Update the horizontal bar, if needed, in response to an item change (eg.- created,
 * disposed, expanded, etc.).  newRightX is the new rightmost X value of the item,
 * and rightXchange is the change that led to the item's rightmost X value becoming
 * newRightX (so oldRightX + rightXchange = newRightX)
 */
void updateHorizontalBar (int newRightX, int rightXchange) {
	if (drawCount > 0) return;
	ScrollBar hBar = getHorizontalBar ();
	if (hBar == null) return;

	newRightX += horizontalOffset;
	int barMaximum = hBar.getMaximum ();
	if (newRightX > barMaximum) {	/* item has extended beyond previous maximum */
		hBar.setMaximum (newRightX);
		int clientAreaWidth = clientArea.width;
		int thumb = Math.min (newRightX, clientAreaWidth);
		hBar.setThumb (thumb);
		hBar.setPageIncrement (thumb);
		hBar.setVisible (clientAreaWidth <= newRightX);
		return;
	}

	int previousRightX = newRightX - rightXchange;
	if (previousRightX != barMaximum) {
		/* this was not the rightmost item, so just check for client width change */
		int clientAreaWidth = clientArea.width;
		int thumb = Math.min (barMaximum, clientAreaWidth);
		hBar.setThumb (thumb);
		hBar.setPageIncrement (thumb);
		hBar.setVisible (clientAreaWidth <= barMaximum);
		return;
	}
	updateHorizontalBar ();		/* must search for the new rightmost item */
}
void updateVerticalBar () {
	if (drawCount > 0) return;
	ScrollBar vBar = getVerticalBar ();
	if (vBar == null) return;

	int pageSize = (clientArea.height - getHeaderHeight ()) / itemHeight;
	int maximum = Math.max (1, itemsCount);	/* setting a value of 0 here is ignored */
	if (maximum != vBar.getMaximum ()) {
		vBar.setMaximum (maximum);
	}
	int thumb = Math.min (pageSize, maximum);
	if (thumb != vBar.getThumb ()) {
		vBar.setThumb (thumb);
		vBar.setPageIncrement (thumb);
	}
	vBar.setVisible (pageSize < maximum);

	/* reclaim any space now left on the bottom */
	if (maximum < topIndex + thumb) {
		topIndex = maximum - thumb;
		vBar.setSelection (topIndex);
		redraw ();
	} else {
		int selection = vBar.getSelection ();
		if (selection != topIndex) {
			topIndex = selection;
			redraw ();
		}
	}
}
}
