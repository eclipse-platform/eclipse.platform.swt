/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
 * Instances of this class provide a selectable user interface object
 * that displays a hierarchy of items and issues notification when an
 * item in the hierarchy is selected.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TreeItem</code>.
 * </p><p>
 * Style <code>VIRTUAL</code> is used to create a <code>Tree</code> whose
 * <code>TreeItem</code>s are to be populated by the client on an on-demand basis
 * instead of up-front.  This can provide significant performance improvements for
 * trees that are very large or for which <code>TreeItem</code> population is
 * expensive (for example, retrieving values from an external source).
 * </p><p>
 * Here is an example of using a <code>Tree</code> with style <code>VIRTUAL</code>:
 * <code><pre>
 *  final Tree tree = new Tree(parent, SWT.VIRTUAL | SWT.BORDER);
 *  tree.setItemCount(20);
 *  tree.addListener(SWT.SetData, new Listener() {
 *      public void handleEvent(Event event) {
 *          TreeItem item = (TreeItem)event.item;
 *          TreeItem parentItem = item.getParentItem();
 *          String text = null;
 *          if (parentItem == null) {
 *              text = "node " + tree.indexOf(item);
 *          } else {
 *              text = parentItem.getText() + " - " + parentItem.indexOf(item);
 *          }
 *          item.setText(text);
 *          System.out.println(text);
 *          item.setItemCount(10);
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
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, VIRTUAL, NO_SCROLL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, Collapse, Expand, SetData, MeasureItem, EraseItem, PaintItem</dd>
 * </dl>
 * </p><p>
 * Note: Only one of the styles SINGLE and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tree">Tree, TreeItem, TreeColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Tree extends Composite {
	Canvas header;
	TreeColumn[] columns = new TreeColumn [0];
	TreeColumn[] orderedColumns;
	TreeItem[] items = NO_ITEMS;
	TreeItem[] availableItems = NO_ITEMS;
	TreeItem[] selectedItems = NO_ITEMS;
	TreeItem focusItem, anchorItem, insertMarkItem;
	TreeItem lastClickedItem;
	Event lastSelectionEvent;
	int availableItemsCount = 0;
	boolean insertMarkPrecedes = false;
	boolean linesVisible, ignoreKey, ignoreDispose, customHeightSet;
	int topIndex = 0, horizontalOffset = 0;
	int fontHeight = 0, imageHeight = 0, itemHeight = 0;
	int headerImageHeight = 0, orderedCol0imageWidth = 0;
	TreeColumn resizeColumn;
	int resizeColumnX = -1;
	int drawCount = 0;
	boolean inExpand = false;	/* for item creation within Expand callback */
	TreeColumn sortColumn;
	int sortDirection = SWT.NONE;

	/* column header tooltip */
	Listener toolTipListener;
	Shell toolTipShell;
	Label toolTipLabel;

	Rectangle arrowBounds, expanderBounds, checkboxBounds, clientArea;

	static final TreeItem[] NO_ITEMS = new TreeItem [0];

	static final int MARGIN_IMAGE = 3;
	static final int MARGIN_CELL = 1;
	static final int SIZE_HORIZONTALSCROLL = 5;
	static final int TOLLERANCE_COLUMNRESIZE = 2;
	static final int WIDTH_HEADER_SHADOW = 2;
	static final int WIDTH_CELL_HIGHLIGHT = 1;
	static final int [] toolTipEvents = new int[] {SWT.MouseExit, SWT.MouseHover, SWT.MouseMove, SWT.MouseDown};
	static final String ELLIPSIS = "...";						//$NON-NLS-1$
	static final String ID_EXPANDED = "EXPANDED";				//$NON-NLS-1$
	static final String ID_COLLAPSED = "COLLAPSED";			//$NON-NLS-1$
	static final String ID_UNCHECKED = "UNCHECKED";			//$NON-NLS-1$
	static final String ID_GRAYUNCHECKED = "GRAYUNCHECKED";	//$NON-NLS-1$
	static final String ID_CHECKMARK = "CHECKMARK";			//$NON-NLS-1$
	static final String ID_CONNECTOR_COLOR = "CONNECTOR_COLOR";	//$NON-NLS-1$
	static final String ID_ARROWUP = "ARROWUP";				//$NON-NLS-1$
	static final String ID_ARROWDOWN = "ARROWDOWN";			//$NON-NLS-1$

//	TEMPORARY CODE
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
 * @see SWT#VIRTUAL
 * @see SWT#NO_SCROLL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Tree (Composite parent, int style) {
	super (parent, checkStyle (style));
	setForeground (null);	/* set foreground and background to chosen default colors */
	setBackground (null);
	GC gc = new GC (this);
	fontHeight = gc.getFontMetrics ().getHeight ();
	gc.dispose ();
	itemHeight = fontHeight + (2 * getCellPadding ());
	initImages (display);
	expanderBounds = getExpandedImage ().getBounds ();
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
/**
 * Adds the listener to the collection of listeners who will
 * be notified when an item in the receiver is expanded or collapsed
 * by sending it one of the messages defined in the <code>TreeListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see TreeListener
 * @see #removeTreeListener
 */
public void addTreeListener (TreeListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);	
	addListener (SWT.Expand, typedListener);
	addListener (SWT.Collapse, typedListener);
}
boolean checkData (TreeItem item, boolean redraw) {
	if (item.cached) return true;
	if ((style & SWT.VIRTUAL) != 0) {
		item.cached = true;
		Event event = new Event ();
		TreeItem parentItem = item.getParentItem ();
		event.item = item;
		event.index = parentItem == null ? indexOf (item) : parentItem.indexOf (item);
		sendEvent (SWT.SetData, event);
		if (isDisposed () || item.isDisposed ()) return false;
		if (redraw) redrawItem (item.availableIndex, false);
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
 * value.  If the tree was created with the <code>SWT.VIRTUAL</code> style,
 * these attributes are requested again as needed.
 *
 * @param index the index of the item to clear
 * @param all <code>true</code> if all child items of the indexed item should be
 * cleared recursively, and <code>false</code> otherwise
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
 * @since 3.2
 */
public void clear (int index, boolean recursive) {
	checkWidget ();
	if (!(0 <= index && index < items.length)) error (SWT.ERROR_INVALID_RANGE);
	TreeItem item = items [index];
	
	/* if there are no columns then the horizontal scrollbar may need adjusting */
	TreeItem[] availableDescendents = null;
	int oldRightX = 0;
	if (columns.length == 0) {
		if (recursive) {
			availableDescendents = item.computeAvailableDescendents ();
			for (int i = 0; i < availableDescendents.length; i++) {
				Rectangle bounds = availableDescendents [i].getBounds (false);
				oldRightX = Math.max (oldRightX, bounds.x + bounds.width);
			}
		} else {
			Rectangle bounds = item.getBounds (false);
			oldRightX = bounds.x + bounds.width;
		}
	}
	
	/* clear the item(s) */
	item.clear ();
	if (recursive) {
		item.clearAll (true, false);
	}

	/* adjust the horizontal scrollbar if needed */
	if (columns.length == 0) {
		int newRightX = 0;
		if (recursive) {
			for (int i = 0; i < availableDescendents.length; i++) {
				Rectangle bounds = availableDescendents [i].getBounds (false);
				newRightX = Math.max (newRightX, bounds.x + bounds.width);
			}
		} else {
			Rectangle bounds = item.getBounds (false);
			newRightX = bounds.x + bounds.width;
		}
		updateHorizontalBar (newRightX, newRightX - oldRightX);
	}
	
	/* redraw the item(s) */
	if (recursive && item.expanded) {
		int descendentCount = availableDescendents == null ?
			item.computeAvailableDescendentCount () :
			availableDescendents.length;
		redrawItems (item.availableIndex, item.availableIndex + descendentCount - 1, false);
	} else {
		redrawItem (item.availableIndex, false);
	}
}
/**
 * Clears all the items in the receiver. The text, icon and other
 * attributes of the items are set to their default values. If the
 * tree was created with the <code>SWT.VIRTUAL</code> style, these
 * attributes are requested again as needed.
 * 
 * @param all <code>true</code> if all child items should be cleared
 * recursively, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 * 
 * @since 3.2
 */
public void clearAll (boolean recursive) {
	checkWidget ();
	if (items.length == 0) return;
	
	/* if there are no columns then the horizontal scrollbar may need adjusting */
	int oldRightX = 0;
	if (columns.length == 0 && !recursive) {
		for (int i = 0; i < items.length; i++) {
			Rectangle bounds = items [i].getBounds (false);
			oldRightX = Math.max (oldRightX, bounds.x + bounds.width);
		}
	}

	/* clear the item(s) */
	for (int i = 0; i < items.length; i++) {
		items [i].clear ();
		if (recursive) items [i].clearAll (true, false);
	}

	/* adjust the horizontal scrollbar if needed */
	if (columns.length == 0) {
		if (recursive) {
			updateHorizontalBar ();		/* recompute from scratch */
		} else {
			/*
			 * All cleared root items will have the same x and width values now,
			 * so just measure the first one as a sample.
			 */
			Rectangle bounds = items [0].getBounds (false);
			int newRightX = bounds.x + bounds.width;
			updateHorizontalBar (newRightX, newRightX - oldRightX);
		}
	}
	
	/* redraw the item(s) */
	if (recursive) {
		redrawItems (0, availableItemsCount - 1, false);
	} else {
		for (int i = 0; i < items.length; i++) {
			redrawItem (items [i].availableIndex, false);
		}
	}
}
/*
 * Returns the ORDERED index of the column that the specified x falls within,
 * or -1 if the x lies to the right of the last column.
 */
int computeColumnIntersect (int x, int startColumn) {
	TreeColumn[] orderedColumns = getOrderedColumns ();
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
			for (int i = 0; i < availableItemsCount; i++) {
				Rectangle itemBounds = availableItems [i].getBounds (false);
				width = Math.max (width, itemBounds.x + itemBounds.width);
			}
		} else {
			TreeColumn[] orderedColumns = getOrderedColumns ();
			TreeColumn lastColumn = orderedColumns [orderedColumns.length - 1];
			width = lastColumn.getX () + lastColumn.width;
		}
	}
	if (hHint != SWT.DEFAULT) {
		height = hHint;
	} else {
		height = getHeaderHeight () + availableItemsCount * itemHeight;
	}
	Rectangle result = computeTrim (0, 0, width, height);
	return new Point (result.width, result.height);
}
void createItem (TreeColumn column, int index) {
	TreeColumn[] newColumns = new TreeColumn [columns.length + 1];
	System.arraycopy (columns, 0, newColumns, 0, index);
	newColumns [index] = column;
	System.arraycopy (columns, index, newColumns, index + 1, columns.length - index);
	columns = newColumns;
	
	if (orderedColumns != null) {
		int insertIndex = 0;
		if (index > 0) {
			insertIndex = columns [index - 1].getOrderIndex () + 1;
		}
		TreeColumn[] newOrderedColumns = new TreeColumn [orderedColumns.length + 1];
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

	if (columns.length == 1) {
		column.itemImageWidth = orderedCol0imageWidth;
	} else {
		if (column.getOrderIndex () == 0) orderedCol0imageWidth = 0;
	}

	/* allow all items to update their internal structures accordingly */
	for (int i = 0; i < items.length; i++) {
		items [i].addColumn (column);
	}

	/* existing items become hidden when going from 0 to 1 column (0 width) */
	if (columns.length == 1 && availableItemsCount > 0) {
		redrawFromItemDownwards (topIndex);
	} else {
		/* checkboxes become hidden when creating a column with index == ordered index == 0 (0 width) */
		if (availableItemsCount > 0 && (style & SWT.CHECK) != 0 && index == 0 && column.getOrderIndex () == 0) {
			redrawFromItemDownwards (topIndex);
		}
	}
}
void createItem (TreeItem item, int index) {
	TreeItem[] newItems = new TreeItem [items.length + 1];
	System.arraycopy (items, 0, newItems, 0, index);
	newItems [index] = item;
	System.arraycopy (items, index, newItems, index + 1, items.length - index);
	items = newItems;

	/* determine the item's availability index */
	int startIndex;
	if (index == items.length - 1) {
		startIndex = availableItemsCount;		/* last item */
	} else {
		startIndex = items [index + 1].availableIndex;
	}

	if (availableItemsCount == availableItems.length) {
		int grow = drawCount <= 0 ? 4 : Math.max (4, availableItems.length * 3 / 2);
		TreeItem[] newAvailableItems = new TreeItem [availableItems.length + grow];
		System.arraycopy (availableItems, 0, newAvailableItems, 0, availableItems.length);
		availableItems = newAvailableItems;
	}
	if (startIndex != availableItemsCount) {
		/* new item is not at end of list, so shift other items right to create space for it */
		System.arraycopy (
			availableItems,
			startIndex,
			availableItems,
			startIndex + 1,
			availableItemsCount - startIndex);
	}
	availableItems [startIndex] = item;
	availableItemsCount++;

	/* update the availableIndex for items bumped down by this new item */
	for (int i = startIndex; i < availableItemsCount; i++) {
		availableItems [i].availableIndex = i;
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
	if (item.availableIndex < topIndex) {
		topIndex++;
		ScrollBar vBar = getVerticalBar ();
		if (vBar != null) vBar.setSelection (topIndex);
		return;
	}
	/*
	 * If this is the first item and the receiver has focus then its boundary
	 * focus ring must be removed. 
	 */
	if (availableItemsCount == 1 && isFocusControl ()) {
		focusItem = item;
		redraw ();
		return;
	}
	int redrawIndex = index;
	if (redrawIndex > 0 && item.isLastChild ()) redrawIndex--;
	redrawFromItemDownwards (items [redrawIndex].availableIndex);
}
/**
 * Deselects an item in the receiver.  If the item was already
 * deselected, it remains deselected.
 *
 * @param item the item to be deselected
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
 * @since 3.4
 */
public void deselect (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	deselectItem (item);
	redrawItem (item.availableIndex, true);
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
	TreeItem[] oldSelection = selectedItems;
	selectedItems = NO_ITEMS;
	for (int i = 0; i < oldSelection.length; i++) {
		redrawItem (oldSelection [i].availableIndex, true);
	}
}
void deselectItem (TreeItem item) {
	int index = getSelectionIndex (item);
	if (index == -1) return;
	TreeItem[] newSelectedItems = new TreeItem [selectedItems.length - 1];
	System.arraycopy (selectedItems, 0, newSelectedItems, 0, index);
	System.arraycopy (
		selectedItems,
		index + 1,
		newSelectedItems,
		index,
		newSelectedItems.length - index);
	selectedItems = newSelectedItems;
}
void destroyItem (TreeColumn column) {
	headerHideToolTip ();
	int index = column.getIndex ();
	int orderedIndex = column.getOrderIndex ();

	TreeColumn[] newColumns = new TreeColumn [columns.length - 1];
	System.arraycopy (columns, 0, newColumns, 0, index);
	System.arraycopy (columns, index + 1, newColumns, index, newColumns.length - index);
	columns = newColumns;

	if (orderedColumns != null) {
		if (columns.length < 2) {
			orderedColumns = null;
		} else {
			int removeIndex = column.getOrderIndex ();
			TreeColumn[] newOrderedColumns = new TreeColumn [orderedColumns.length - 1];
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

	if (orderedIndex == 0 && columns.length > 0) {
		orderedCol0imageWidth = columns [getColumnOrder ()[0]].itemImageWidth;
	}

	/* allow all items to update their internal structures accordingly */
	for (int i = 0; i < items.length; i++) {
		items [i].removeColumn (column, index, orderedIndex);
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
			int selection = hBar.getSelection ();
			if (selection != horizontalOffset) {
				horizontalOffset = selection;
				redraw ();
				if (header.isVisible () && drawCount <= 0) header.redraw ();
			}
		}
	}
	TreeColumn[] orderedColumns = getOrderedColumns ();
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
 * Allows the Tree to update internal structures it has that may contain the
 * item being destroyed.  The argument is not necessarily a root-level item.
 */
void destroyItem (TreeItem item) {
	if (item == focusItem) reassignFocus ();

	/* availableItems array */
	int availableIndex = item.availableIndex; 
	if (availableIndex != -1) {
		Rectangle bounds = item.getBounds (false);
		int rightX = bounds.x + bounds.width;

		if (availableIndex != availableItemsCount - 1) {
			/* item is not at end of available items list, so must shift items left to reclaim its slot */
			System.arraycopy (
				availableItems,
				availableIndex + 1,
				availableItems,
				availableIndex,
				availableItemsCount - availableIndex - 1);
			availableItems [availableItemsCount - 1] = null;
		} else {
			availableItems [availableIndex] = null;	/* last item, so no array copy needed */
		}
		availableItemsCount--;

		if (drawCount <= 0 && availableItems.length - availableItemsCount == 4) {
			/* shrink the available items array */
			TreeItem[] newAvailableItems = new TreeItem [availableItemsCount];
			System.arraycopy (availableItems, 0, newAvailableItems, 0, newAvailableItems.length);
			availableItems = newAvailableItems;
		}

		/* update the availableIndex on affected items */
		for (int i = availableIndex; i < availableItemsCount; i++) {
			availableItems [i].availableIndex = i;
		}
		item.availableIndex = -1;
		int oldTopIndex = topIndex;
		updateVerticalBar ();
		updateHorizontalBar (0, -rightX);
		/* 
		 * If destroyed item is above viewport then adjust topIndex and the vertical
		 * scrollbar so that the current viewport items will not change. 
		 */
		if (availableIndex < topIndex) {
			topIndex = oldTopIndex - 1;
			ScrollBar vBar = getVerticalBar ();
			if (vBar != null) vBar.setSelection (topIndex);
		}
	}
	/* selectedItems array */
	if (item.isSelected ()) {
		int selectionIndex = getSelectionIndex (item);
		TreeItem[] newSelectedItems = new TreeItem [selectedItems.length - 1];
		System.arraycopy (selectedItems, 0, newSelectedItems, 0, selectionIndex);
		System.arraycopy (
			selectedItems,
			selectionIndex + 1,
			newSelectedItems,
			selectionIndex,
			newSelectedItems.length - selectionIndex);
		selectedItems = newSelectedItems;
	}
	/* root-level items array */
	if (item.depth == 0) {
		int index = item.getIndex ();
		TreeItem[] newItems = new TreeItem [items.length - 1];
		System.arraycopy (items, 0, newItems, 0, index);
		System.arraycopy (items, index + 1, newItems, index, newItems.length - index);
		items = newItems;
	}
	if (item == anchorItem) anchorItem = null;
	if (item == insertMarkItem) insertMarkItem = null;
	if (item == lastClickedItem) lastClickedItem = null;
	/*
	 * If this was the last item and the receiver has focus then its boundary
	 * focus ring must be redrawn.
	 */
	if (availableItemsCount == 0 && isFocusControl ()) {
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
Image getCollapsedImage () {
	return (Image) display.getData (ID_COLLAPSED);
}
/**
 * Returns the column at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
 * Columns are returned in the order that they were created.
 * If no <code>TreeColumn</code>s were created by the programmer,
 * this method will throw <code>ERROR_INVALID_RANGE</code> despite
 * the fact that a single column of data may be visible in the tree.
 * This occurs when the programmer uses the tree like a list, adding
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
 * @see Tree#getColumnOrder()
 * @see Tree#setColumnOrder(int[])
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 * 
 * @since 3.1
 */
public TreeColumn getColumn (int index) {
	checkWidget ();
	if (!(0 <= index && index < columns.length)) error (SWT.ERROR_INVALID_RANGE);
	return columns [index];
}
/**
 * Returns the number of columns contained in the receiver.
 * If no <code>TreeColumn</code>s were created by the programmer,
 * this value is zero, despite the fact that visually, one column
 * of items may be visible. This occurs when the programmer uses
 * the tree like a list, adding items but never creating a column.
 *
 * @return the number of columns
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
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
 * @see Tree#setColumnOrder(int[])
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 * 
 * @since 3.2
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
 * Returns an array of <code>TreeColumn</code>s which are the
 * columns in the receiver. Columns are returned in the order
 * that they were created.  If no <code>TreeColumn</code>s were
 * created by the programmer, the array is empty, despite the fact
 * that visually, one column of items may be visible. This occurs
 * when the programmer uses the tree like a list, adding items but
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
 * @see Tree#getColumnOrder()
 * @see Tree#setColumnOrder(int[])
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 * 
 * @since 3.1
 */
public TreeColumn[] getColumns () {
	checkWidget ();
	TreeColumn[] result = new TreeColumn [columns.length];
	System.arraycopy (columns, 0, result, 0, columns.length);
	return result;
}
Color getConnectorColor () {
	return (Color) display.getData (ID_CONNECTOR_COLOR);
}
Image getExpandedImage () {
	return (Image) display.getData (ID_EXPANDED);
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
 * 
 * @since 3.1
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
 * @since 3.1 
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
 * 
 * @since 3.1
 */
public boolean getHeaderVisible () {
	checkWidget ();
	return header.getVisible ();
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
public TreeItem getItem (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	int index = (point.y - getHeaderHeight ()) / itemHeight + topIndex;
	if (!(0 <= index && index < availableItemsCount)) return null;		/* below the last item */
	TreeItem result = availableItems [index];
	if (!result.getHitBounds ().contains (point)) return null;	/* considers the x value */
	return result;
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
 * 
 * @since 3.1
 */
public TreeItem getItem (int index) {
	checkWidget ();
	if (!(0 <= index && index < items.length)) error (SWT.ERROR_INVALID_RANGE);
	return items [index];
}
/**
 * Returns the number of items contained in the receiver
 * that are direct item children of the receiver.  The
 * number that is returned is the number of roots in the
 * tree.
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
	return items.length;
}
/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the tree.
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
 * Returns a (possibly empty) array of items contained in the
 * receiver that are direct item children of the receiver.  These
 * are the roots of the tree.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem[] getItems () {
	checkWidget ();
	TreeItem[] result = new TreeItem [items.length];
	System.arraycopy (items, 0, result, 0, items.length);
	return result;	
}
/*
 * Returns the current y-coordinate that the specified item should have. 
 */
int getItemY (TreeItem item) {
	int index = item.availableIndex;
	if (index == -1) return -1;
	return (index - topIndex) * itemHeight + getHeaderHeight ();
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
 * 
 * @since 3.1
 */
public boolean getLinesVisible () {
	checkWidget ();
	return linesVisible;
}
TreeColumn[] getOrderedColumns () {
	if (orderedColumns != null) return orderedColumns;
	return columns;
}
/**
 * Returns the receiver's parent item, which must be a
 * <code>TreeItem</code> or null when the receiver is a
 * root.
 *
 * @return the receiver's parent item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem getParentItem () {
	checkWidget ();
	return null;
}
/**
 * Returns an array of <code>TreeItem</code>s that are currently
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
public TreeItem[] getSelection () {
	checkWidget ();
	int count = selectedItems.length;
	TreeItem[] result = new TreeItem [count];
	if (count > 0) {
		if (count == 1) {
			System.arraycopy (selectedItems, 0, result, 0, count);
		} else {
			getSelection (result, items, 0);
		}
	}
	return result;
}
int getSelection (TreeItem[] result, TreeItem[] items, int index) {
	for (int i = 0; i < items.length; i++) {
		TreeItem item = items [i];
		if (item.isSelected ()) result [index++] = item;
		if (index == result.length) break;
		index = getSelection (result, items [i].items, index);
		if (index == result.length) break;
	}
	return index;
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
/*
 * Returns the index of the argument in the receiver's array of currently-
 * selected items, or -1 if the item is not currently selected.
 */
int getSelectionIndex (TreeItem item) {
	for (int i = 0; i < selectedItems.length; i++) {
		if (selectedItems [i] == item) return i;
	}
	return -1;
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
 * @see #setSortColumn(TreeColumn)
 * 
 * @since 3.2
 */
public TreeColumn getSortColumn () {
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
 * Returns the item which is currently at the top of the receiver.
 * This item can change when items are expanded, collapsed, scrolled
 * or new items are added or removed.
 *
 * @return the item at the top of the receiver 
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public TreeItem getTopItem () {
	checkWidget ();
	if (availableItemsCount == 0) return null;
	return availableItems [topIndex];
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
	TreeColumn[] orderedColumns = getOrderedColumns ();
	TreeColumn column = orderedColumns [orderedIndex];
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
	TreeColumn[] orderedColumns = getOrderedColumns ();
	int x = -horizontalOffset;
	for (int i = 0; i < orderedColumns.length; i++) {
		TreeColumn column = orderedColumns [i];
		x += column.width;
		if (event.x < x) {
			/* found the clicked column */
			TreeColumn packColumn = null;
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
	TreeColumn[] orderedColumns = getOrderedColumns ();
	int x = -horizontalOffset;
	for (int i = 0; i < orderedColumns.length; i++) {
		TreeColumn column = orderedColumns [i]; 
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
					TreeColumn destColumn = orderedColumns [destIndex];
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
			TreeColumn column = columns [i]; 
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
	TreeColumn[] orderedColumns = getOrderedColumns ();
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
	TreeColumn column = getOrderedColumns () [computeColumnIntersect (x, 0)];
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
 * 
 * @since 3.1
 */
public int indexOf (TreeColumn column) {
	checkWidget ();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (column.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
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
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public int indexOf (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (item.parentItem != null || item.parent != this) return -1;
	return item.getIndex ();
}
static void initImages (final Display display) {
	PaletteData fourBit = new PaletteData (new RGB[] {
		new RGB (0, 0, 0), new RGB (128, 0, 0), new RGB (0, 128, 0), new RGB (128, 128, 0),
		new RGB (0, 0, 128), new RGB (128, 0, 128), new RGB (0, 128, 128), new RGB (128, 128, 128),
		new RGB (192, 192, 192), new RGB (255, 0, 0), new RGB (0, 255, 0), new RGB (255, 255, 0),
		new RGB (0, 0, 255), new RGB (255, 0, 255), new RGB (0, 255, 255), new RGB (255, 255, 255)});

	if (display.getData (ID_EXPANDED) == null) {
		ImageData expanded = new ImageData (
			9, 9, 4, 										/* width, height, depth */
			fourBit, 4,
			new byte[] {
				119, 119, 119, 119, 112, 0, 0, 0,
				127, -1, -1, -1, 112, 0, 0, 0,
				127, -1, -1, -1, 112, 0, 0, 0,
				127, -1, -1, -1, 112, 0, 0, 0,
				127, 0, 0, 15, 112, 0, 0, 0,
				127, -1, -1, -1, 112, 0, 0, 0,
				127, -1, -1, -1, 112, 0, 0, 0,
				127, -1, -1, -1, 112, 0, 0, 0,
				119, 119, 119, 119, 112, 0, 0, 0});
		expanded.transparentPixel = 15;		/* use white for transparency */
		display.setData (ID_EXPANDED, new Image (display, expanded));
	}

	if (display.getData (ID_COLLAPSED) == null) {
		ImageData collapsed = new ImageData (
			9, 9, 4, 										/* width, height, depth */
			fourBit, 4,
			new byte[] {
				119, 119, 119, 119, 112, 0, 0, 0,
				127, -1, -1, -1, 112, 0, 0, 0,
				127, -1, 15, -1, 112, 0, 0, 0,
				127, -1, 15, -1, 112, 0, 0, 0,
				127, 0, 0, 15, 112, 0, 0, 0,
				127, -1, 15, -1, 112, 0, 0, 0,
				127, -1, 15, -1, 112, 0, 0, 0,
				127, -1, -1, -1, 112, 0, 0, 0,
				119, 119, 119, 119, 112, 0, 0, 0});
		collapsed.transparentPixel = 15;	/* use white for transparency */
		display.setData (ID_COLLAPSED, new Image (display, collapsed));
	}

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
	
	if (display.getData (ID_CONNECTOR_COLOR) == null) {
		display.setData (ID_CONNECTOR_COLOR, new Color (display, 170, 170, 170));
	}

	display.disposeExec (new Runnable () {
		public void run() {
			Image expanded = (Image) display.getData (ID_EXPANDED);
			if (expanded != null) expanded.dispose ();
			Image collapsed = (Image) display.getData (ID_COLLAPSED);
			if (collapsed != null) collapsed.dispose ();
			Color connectorColor = (Color) display.getData (ID_CONNECTOR_COLOR);
			if (connectorColor != null) connectorColor.dispose ();
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

			display.setData (ID_EXPANDED, null);
			display.setData (ID_COLLAPSED, null);
			display.setData (ID_CONNECTOR_COLOR, null);
			display.setData (ID_UNCHECKED, null);
			display.setData (ID_GRAYUNCHECKED, null);
			display.setData (ID_CHECKMARK, null);
			display.setData (ID_ARROWDOWN, null);
			display.setData (ID_ARROWUP, null);
		}
	});
}
/*
 * Important: Assumes that item just became available (ie.- was either created
 * or the parent item was expanded) and the parent is available.
 */
void makeAvailable (TreeItem item) {
	int parentItemCount = item.parentItem.items.length; 
	int index = 0;
	if (parentItemCount == 1) {		/* this is the only child of parentItem */
		index = item.parentItem.availableIndex + 1;
	} else {
		/* determine this item's index in its parent */
		int itemIndex = 0;
		TreeItem[] items = item.parentItem.items;
		for (int i = 0; i < items.length; i++) {
			if (items [i] == item) {
				itemIndex = i;
				break;
			}
		}
		if (itemIndex != parentItemCount - 1) {	/* this is not the last child */
			index = items [itemIndex + 1].availableIndex;
		} else {	/* this is the last child */
			TreeItem previousItem = items [itemIndex - 1];
			index = previousItem.availableIndex + previousItem.computeAvailableDescendentCount ();
		}
	}

	if (availableItemsCount == availableItems.length) {
		int grow = drawCount <= 0 ? 4 : Math.max (4, availableItems.length * 3 / 2);
		TreeItem[] newAvailableItems = new TreeItem [availableItems.length + grow];
		System.arraycopy (availableItems, 0, newAvailableItems, 0, availableItems.length);
		availableItems = newAvailableItems;
	}
	if (index != availableItemsCount) {
		/* new item is not at end of list, so shift other items right to create space for it */
		System.arraycopy (availableItems, index, availableItems, index + 1, availableItemsCount - index);
	}
	availableItems [index] = item;
	availableItemsCount++;

	/* update availableIndex as needed */
	for (int i = index; i < availableItemsCount; i++) {
		availableItems [i].availableIndex = i;
	}
}

/*
 * Important: Assumes that item is available and its descendents have just become
 * available (ie.- they were either created or the item was expanded).
 */
void makeDescendentsAvailable (TreeItem item, TreeItem[] descendents) {
	int itemAvailableIndex = item.availableIndex;
	TreeItem[] newAvailableItems = new TreeItem [availableItemsCount + descendents.length - 1];
	
	System.arraycopy (availableItems, 0, newAvailableItems, 0, itemAvailableIndex);
	System.arraycopy (descendents, 0, newAvailableItems, itemAvailableIndex, descendents.length);
	int startIndex = itemAvailableIndex + 1;
	System.arraycopy (
		availableItems,
		startIndex,
		newAvailableItems,
		itemAvailableIndex + descendents.length,
		availableItemsCount - startIndex);
	availableItems = newAvailableItems;
	availableItemsCount = availableItems.length;
	
	/* update availableIndex as needed */
	for (int i = itemAvailableIndex; i < availableItemsCount; i++) {
		availableItems [i].availableIndex = i;
	}
}

/*
 * Important: Assumes that item is available and its descendents have just become
 * unavailable (ie.- they were either disposed or the item was collapsed).
 */
void makeDescendentsUnavailable (TreeItem item, TreeItem[] descendents) {
	int descendentsLength = descendents.length;
	TreeItem[] newAvailableItems = new TreeItem [availableItemsCount - descendentsLength + 1];
	
	System.arraycopy (availableItems, 0, newAvailableItems, 0, item.availableIndex + 1);
	int startIndex = item.availableIndex + descendentsLength;
	System.arraycopy (
		availableItems,
		startIndex,
		newAvailableItems,
		item.availableIndex + 1,
		availableItemsCount - startIndex);
	availableItems = newAvailableItems;
	availableItemsCount = availableItems.length;
	
	/* update availableIndexes */
	for (int i = 1; i < descendents.length; i++) {
		/* skip the first descendent since this is the item being collapsed */
		descendents [i].availableIndex = -1;
	}
	for (int i = item.availableIndex; i < availableItemsCount; i++) {
		availableItems [i].availableIndex = i;
	}
	
	/* remove the selection from all descendents */
	for (int i = selectedItems.length - 1; i >= 0; i--) {
		if (selectedItems [i] != item && selectedItems [i].hasAncestor (item)) {
			removeSelectedItem (i);
		}
	}
	
	/* if the anchorItem is being hidden then clear it */
	if (anchorItem != null && anchorItem != item && anchorItem.hasAncestor (item)) {
		anchorItem = null;
	}
}
void onArrowDown (int stateMask) {
	if ((stateMask & (SWT.SHIFT | SWT.CTRL)) == 0) {
		/* Down Arrow with no modifiers */
		int newFocusIndex = focusItem.availableIndex + 1;
		if (newFocusIndex == availableItemsCount) return; 	/* at bottom */
		selectItem (availableItems [newFocusIndex], false);
		setFocusItem (availableItems [newFocusIndex], true);
		redrawItem (newFocusIndex, true);
		showItem (availableItems [newFocusIndex]);
		Event newEvent = new Event ();
		newEvent.item = availableItems [newFocusIndex];
		postEvent (SWT.Selection, newEvent);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.CTRL) != 0) {
			/* CTRL+Down Arrow, CTRL+Shift+Down Arrow */
			int visibleItemCount = (clientArea.height - getHeaderHeight ()) / itemHeight;
			if (availableItemsCount <= topIndex + visibleItemCount) return;	/* at bottom */
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
		int newFocusIndex = focusItem.availableIndex + 1;
		if (newFocusIndex == availableItemsCount) return; 	/* at bottom */
		selectItem (availableItems [newFocusIndex], false);
		setFocusItem (availableItems [newFocusIndex], true);
		redrawItem (newFocusIndex, true);
		showItem (availableItems [newFocusIndex]);
		Event newEvent = new Event ();
		newEvent.item = availableItems [newFocusIndex];
		postEvent (SWT.Selection, newEvent);
		return;
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			/* CTRL+Shift+Down Arrow */
			int visibleItemCount = (clientArea.height - getHeaderHeight ()) / itemHeight;
			if (availableItemsCount <= topIndex + visibleItemCount) return;	/* at bottom */
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
		int focusIndex = focusItem.availableIndex; 
		if (focusIndex == availableItemsCount - 1) return;	/* at bottom */
		TreeItem newFocusItem = availableItems [focusIndex + 1];
		setFocusItem (newFocusItem, true);
		redrawItem (newFocusItem.availableIndex, true);
		showItem (newFocusItem);
		return;
	}
	/* Shift+Down Arrow */
	int newFocusIndex = focusItem.availableIndex + 1;
	if (newFocusIndex == availableItemsCount) return; 	/* at bottom */
	if (anchorItem == null) anchorItem = focusItem;
	if (focusItem.availableIndex < anchorItem.availableIndex) {
		deselectItem (focusItem);
		redrawItem (focusItem.availableIndex, true);
	}
	selectItem (availableItems [newFocusIndex], true);
	setFocusItem (availableItems [newFocusIndex], true);
	redrawItem (newFocusIndex, true);
	showItem (availableItems [newFocusIndex]);
	Event newEvent = new Event ();
	newEvent.item = availableItems [newFocusIndex];
	postEvent (SWT.Selection, newEvent);
}
void onArrowLeft (int stateMask) {
	if ((stateMask & SWT.CTRL) != 0) {
		/* CTRL+Left Arrow, CTRL+Shift+Left Arrow */
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
		return;
	}
	/* Left Arrow with no modifiers, Shift+Left Arrow */
	if (focusItem.expanded) {
		focusItem.setExpanded (false);
		Event newEvent = new Event ();
		newEvent.item = focusItem;
		sendEvent (SWT.Collapse, newEvent);
		return;
	}
	TreeItem parentItem = focusItem.parentItem;
	if (parentItem == null) return;
	
	selectItem (parentItem, false);
	setFocusItem (parentItem, true);
	redrawItem (parentItem.availableIndex, true);
	showItem (parentItem);
	Event newEvent = new Event ();
	newEvent.item = parentItem;
	postEvent (SWT.Selection, newEvent);
}
void onArrowRight (int stateMask) {
	if ((stateMask & SWT.CTRL) != 0) {
		/* CTRL+Right Arrow, CTRL+Shift+Right Arrow */
		ScrollBar hBar = getHorizontalBar ();
		if (hBar != null) {
			int maximum = hBar.getMaximum ();
			int clientWidth = clientArea.width;
			if ((horizontalOffset + clientWidth) == maximum) return;
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
		return;
	}
	/* Right Arrow with no modifiers, Shift+Right Arrow */
	TreeItem[] children = focusItem.items;
	if (children.length == 0) return;
	if (!focusItem.expanded) {
		focusItem.setExpanded (true);
		Event newEvent = new Event ();
		newEvent.item = focusItem;
		inExpand = true;
		sendEvent (SWT.Expand, newEvent);
		inExpand = false;
		if (isDisposed ()) return;
		if (focusItem.items.length == 0) {
			focusItem.expanded = false;
		}
		return;
	}
	selectItem (children [0], false);
	setFocusItem (children [0], true);
	redrawItem (children [0].availableIndex, true);
	showItem (children [0]);
	Event newEvent = new Event ();
	newEvent.item = children [0];
	postEvent (SWT.Selection, newEvent);
}
void onArrowUp (int stateMask) {
	if ((stateMask & (SWT.SHIFT | SWT.CTRL)) == 0) {
		/* Up Arrow with no modifiers */
		int newFocusIndex = focusItem.availableIndex - 1;
		if (newFocusIndex < 0) return; 		/* at top */
		TreeItem item = availableItems [newFocusIndex];
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
		int newFocusIndex = focusItem.availableIndex - 1;
		if (newFocusIndex < 0) return; 	/* at top */
		TreeItem item = availableItems [newFocusIndex];
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
		int focusIndex = focusItem.availableIndex; 
		if (focusIndex == 0) return;	/* at top */
		TreeItem newFocusItem = availableItems [focusIndex - 1];
		setFocusItem (newFocusItem, true);
		showItem (newFocusItem);
		redrawItem (newFocusItem.availableIndex, true);
		return;
	}
	/* Shift+Up Arrow */
	int newFocusIndex = focusItem.availableIndex - 1;
	if (newFocusIndex < 0) return; 		/* at top */
	if (anchorItem == null) anchorItem = focusItem;
	if (anchorItem.availableIndex < focusItem.availableIndex) {
		deselectItem (focusItem);
		redrawItem (focusItem.availableIndex, true);
	}
	TreeItem item = availableItems [newFocusIndex];
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
	for (int i = 0; i < items.length; i++) {
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
	topIndex = availableItemsCount = horizontalOffset = 0;
	availableItems = items = selectedItems = null;
	columns = orderedColumns = null;
	focusItem = anchorItem = insertMarkItem = lastClickedItem = null;
	lastSelectionEvent = null;
	header = null;
	resizeColumn = sortColumn = null;
	expanderBounds = null;
}
void onEnd (int stateMask) {
	int lastAvailableIndex = availableItemsCount - 1;
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
		/* End with no modifiers */
		if (focusItem.availableIndex == lastAvailableIndex) return; 	/* at bottom */
		TreeItem item = availableItems [lastAvailableIndex]; 
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
			setTopItem (availableItems [availableItemsCount - visibleItemCount]);
			return;
		}
		/* Shift+End */
		if (focusItem.availableIndex == lastAvailableIndex) return; /* at bottom */
		TreeItem item = availableItems [lastAvailableIndex]; 
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
			showItem (availableItems [lastAvailableIndex]);
			return;
		}
		/* CTRL+End */
		if (focusItem.availableIndex == lastAvailableIndex) return; /* at bottom */
		TreeItem item = availableItems [lastAvailableIndex];
		setFocusItem (item, true);
		showItem (item);
		redrawItem (item.availableIndex, true);
		return;
	}
	/* Shift+End */
	if (anchorItem == null) anchorItem = focusItem;
	TreeItem selectedItem = availableItems [lastAvailableIndex];
	if (selectedItem == focusItem && selectedItem.isSelected ()) return;
	int anchorIndex = anchorItem.availableIndex;
	int selectIndex = selectedItem.availableIndex;
	TreeItem[] newSelection = new TreeItem [selectIndex - anchorIndex + 1];
	int writeIndex = 0;
	for (int i = anchorIndex; i <= selectIndex; i++) {
		newSelection [writeIndex++] = availableItems [i];
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

	if (items.length == 0) {
		redraw ();
		return;
	}
	if (focusItem != null) {
		redrawItem (focusItem.availableIndex, true);
		return;
	}
	/* an initial focus item must be selected */
	TreeItem initialFocus = null;
	if (selectedItems.length > 0) {
		for (int i = 0; i < selectedItems.length && initialFocus == null; i++) {
			if (selectedItems [i].isAvailable ()) {
				initialFocus = selectedItems [i];
			}
		}
		if (initialFocus == null) {
			/* none of the selected items are available */
			initialFocus = availableItems [topIndex];
		}
	} else {
		initialFocus = availableItems [topIndex];
	}
	setFocusItem (initialFocus, false);
	redrawItem (initialFocus.availableIndex, true);
	return;
}
void onFocusOut () {
	hasFocus = false;

	if (items.length == 0) {
		redraw ();
		return;
	}	

	if (focusItem != null) {
		redrawItem (focusItem.availableIndex, true);
	}
}
void onHome (int stateMask) {
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
		/* Home with no modifiers */
		if (focusItem.availableIndex == 0) return; 		/* at top */
		TreeItem item = availableItems [0];
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
			setTopItem (availableItems [0]);
			return;
		}
		/* Shift+Home */
		if (focusItem.availableIndex == 0) return; 		/* at top */
		TreeItem item = availableItems [0];
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
			setTopItem (availableItems [0]);
			return;
		}
		/* CTRL+Home */
		if (focusItem.availableIndex == 0) return; /* at top */
		TreeItem item = availableItems [0];
		setFocusItem (item, true);
		showItem (item);
		redrawItem (item.availableIndex, true);
		return;
	}
	/* Shift+Home */
	if (anchorItem == null) anchorItem = focusItem;
	TreeItem selectedItem = availableItems [0];
	if (selectedItem == focusItem && selectedItem.isSelected ()) return;
	int anchorIndex = anchorItem.availableIndex;
	int selectIndex = selectedItem.availableIndex;
	TreeItem[] newSelection = new TreeItem [anchorIndex + 1];
	int writeIndex = 0;
	for (int i = anchorIndex; i >= 0; i--) {
		newSelection [writeIndex++] = availableItems [i];
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

	int initialIndex = focusItem.availableIndex;
	char character = Character.toLowerCase (event.character);
	/* check available items from current focus item to bottom */
	for (int i = initialIndex + 1; i < availableItemsCount; i++) {
		TreeItem item = availableItems [i];
		String text = item.getText (0, false);
		if (text.length() > 0) {
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
		TreeItem item = availableItems [i];
		String text = item.getText (0, false);
		if (text.length() > 0) {
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
	if  (!(0 <= index && index < availableItemsCount)) return;	/* not on an available item */
	TreeItem selectedItem = availableItems [index];
	
	/* 
	 * If the two clicks of the double click did not occur over the same item then do not
	 * consider this to be a default selection.
	 */
	if (selectedItem != lastClickedItem) return;

	/* if click was in expander box then don't fire event */
	if (selectedItem.items.length > 0 && selectedItem.getExpanderBounds ().contains (event.x, event.y)) {
		return;
	}
	
	if (!selectedItem.getHitBounds ().contains (event.x, event.y)) return;	/* considers x */
	
	Event newEvent = new Event ();
	newEvent.item = selectedItem;
	postEvent (SWT.DefaultSelection, newEvent);
}
void onMouseDown (Event event) {
	if (!isFocusControl ()) forceFocus ();
	int index = (event.y - getHeaderHeight ()) / itemHeight + topIndex;
	if (!(0 <= index && index < availableItemsCount)) return;	/* not on an available item */
	TreeItem selectedItem = availableItems [index];
	
	/* if click was in expander box */
	if (selectedItem.items.length > 0 && selectedItem.getExpanderBounds ().contains (event.x, event.y)) {
		if (event.button != 1) return;
		boolean expand = !selectedItem.expanded;
		selectedItem.setExpanded (expand);
		Event newEvent = new Event ();
		newEvent.item = selectedItem;
		if (expand) {
			inExpand = true;
			sendEvent (SWT.Expand, newEvent);
			inExpand = false;
			if (isDisposed ()) return;
			if (selectedItem.items.length == 0) {
				selectedItem.expanded = false;
			}
		} else {
			sendEvent (SWT.Collapse, newEvent);
		}
		return;
	}
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
				redrawItem (selectedItem.availableIndex, true);
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
				redrawItem (selectedItem.availableIndex, true);
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
				int anchorIndex = anchorItem.availableIndex;
				int selectIndex = selectedItem.availableIndex;
				TreeItem[] newSelection = new TreeItem [Math.abs (anchorIndex - selectIndex) + 1];
				int step = anchorIndex < selectIndex ? 1 : -1;
				int writeIndex = 0;
				for (int i = anchorIndex; i != selectIndex; i += step) {
					newSelection [writeIndex++] = availableItems [i];
				}
				newSelection [writeIndex] = availableItems [selectIndex];
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
			redrawItem (selectedItem.availableIndex, true);
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
			redrawItem (selectedItem.availableIndex, true);
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
		redrawItem (selectedItem.availableIndex, true);
		if (sendSelection) {
			Event newEvent = new Event ();
			newEvent.item = selectedItem;
			postEvent (SWT.Selection, newEvent);
		}
		return;
	}
	if ((event.stateMask & SWT.SHIFT) != 0) {
		if (anchorItem == null) anchorItem = focusItem;
		int anchorIndex = anchorItem.availableIndex;
		int selectIndex = selectedItem.availableIndex;
		TreeItem[] newSelection = new TreeItem [Math.abs (anchorIndex - selectIndex) + 1];
		int step = anchorIndex < selectIndex ? 1 : -1;
		int writeIndex = 0;
		for (int i = anchorIndex; i != selectIndex; i += step) {
			newSelection [writeIndex++] = availableItems [i];
		}
		newSelection [writeIndex] = availableItems [selectIndex];
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
	redrawItem (selectedItem.availableIndex, true);
	if (sendSelection) {
		Event newEvent = new Event ();
		newEvent.item = selectedItem;
		postEvent (SWT.Selection, newEvent);
	}
}
void onMouseUp (Event event) {
	int index = (event.y - getHeaderHeight ()) / itemHeight + topIndex;
	if (!(0 <= index && index < availableItemsCount)) return;	/* not on an available item */
	lastClickedItem = availableItems [index];
}
void onPageDown (int stateMask) {
	int visibleItemCount = (clientArea.height - getHeaderHeight ()) / itemHeight;
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
		/* PageDown with no modifiers */
		int newFocusIndex = focusItem.availableIndex + visibleItemCount - 1;
		newFocusIndex = Math.min (newFocusIndex, availableItemsCount - 1);
		if (newFocusIndex == focusItem.availableIndex) return;
		TreeItem item = availableItems [newFocusIndex];
		selectItem (item, false);
		setFocusItem (item, true);
		showItem (item);
		redrawItem (item.availableIndex, true);
		return;
	}
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == (SWT.CTRL | SWT.SHIFT)) {
		/* CTRL+Shift+PageDown */
		int newTopIndex = topIndex + visibleItemCount;
		newTopIndex = Math.min (newTopIndex, availableItemsCount - visibleItemCount);
		if (newTopIndex == topIndex) return;
		setTopItem (availableItems [newTopIndex]);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			/* Shift+PageDown */
			int newFocusIndex = focusItem.availableIndex + visibleItemCount - 1;
			newFocusIndex = Math.min (newFocusIndex, availableItemsCount - 1);
			if (newFocusIndex == focusItem.availableIndex) return;
			TreeItem item = availableItems [newFocusIndex];
			selectItem (item, false);
			setFocusItem (item, true);
			showItem (item);
			redrawItem (item.availableIndex, true);
			return;
		}
		/* CTRL+PageDown */
		int newTopIndex = topIndex + visibleItemCount;
		newTopIndex = Math.min (newTopIndex, availableItemsCount - visibleItemCount);
		if (newTopIndex == topIndex) return;
		setTopItem (availableItems [newTopIndex]);
		return;
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		/* CTRL+PageDown */
		int bottomIndex = Math.min (topIndex + visibleItemCount - 1, availableItemsCount - 1);
		if (focusItem.availableIndex != bottomIndex) {
			/* move focus to bottom item in viewport */
			setFocusItem (availableItems [bottomIndex], true);
			redrawItem (bottomIndex, true);
		} else {
			/* at bottom of viewport, so set focus to bottom item one page down */
			int newFocusIndex = Math.min (availableItemsCount - 1, bottomIndex + visibleItemCount);
			if (newFocusIndex == focusItem.availableIndex) return;
			setFocusItem (availableItems [newFocusIndex], true);
			showItem (availableItems [newFocusIndex]);
			redrawItem (newFocusIndex, true);
		}
		return;
	}
	/* Shift+PageDown */
	if (anchorItem == null) anchorItem = focusItem;
	int anchorIndex = anchorItem.availableIndex;
	int bottomIndex = Math.min (topIndex + visibleItemCount - 1, availableItemsCount - 1);
	int selectIndex;
	if (focusItem.availableIndex != bottomIndex) {
		/* select from focus to bottom item in viewport */
		selectIndex = bottomIndex;
	} else {
		/* already at bottom of viewport, so select to bottom of one page down */
		selectIndex = Math.min (availableItemsCount - 1, bottomIndex + visibleItemCount);
		if (selectIndex == focusItem.availableIndex && focusItem.isSelected ()) return;
	}
	TreeItem selectedItem = availableItems [selectIndex];
	TreeItem[] newSelection = new TreeItem [Math.abs (anchorIndex - selectIndex) + 1];
	int step = anchorIndex < selectIndex ? 1 : -1;
	int writeIndex = 0;
	for (int i = anchorIndex; i != selectIndex; i += step) {
		newSelection [writeIndex++] = availableItems [i];
	}
	newSelection [writeIndex] = availableItems [selectIndex];
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
		int newFocusIndex = Math.max (0, focusItem.availableIndex - visibleItemCount + 1);
		if (newFocusIndex == focusItem.availableIndex) return;
		TreeItem item = availableItems [newFocusIndex];
		selectItem (item, false);
		setFocusItem (item, true);
		showItem (item);
		redrawItem (item.availableIndex, true);
		return;
	}
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == (SWT.CTRL | SWT.SHIFT)) {
		/* CTRL+Shift+PageUp */
		int newTopIndex = Math.max (0, topIndex - visibleItemCount);
		if (newTopIndex == topIndex) return;
		setTopItem (availableItems [newTopIndex]);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			/* Shift+PageUp */
			int newFocusIndex = Math.max (0, focusItem.availableIndex - visibleItemCount + 1);
			if (newFocusIndex == focusItem.availableIndex) return;
			TreeItem item = availableItems [newFocusIndex];
			selectItem (item, false);
			setFocusItem (item, true);
			showItem (item);
			redrawItem (item.availableIndex, true);
			return;
		}
		/* CTRL+PageUp */
		int newTopIndex = Math.max (0, topIndex - visibleItemCount);
		if (newTopIndex == topIndex) return;
		setTopItem (availableItems [newTopIndex]);
		return;
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		/* CTRL+PageUp */
		if (focusItem.availableIndex != topIndex) {
			/* move focus to top item in viewport */
			setFocusItem (availableItems [topIndex], true);
			redrawItem (topIndex, true);
		} else {
			/* at top of viewport, so set focus to top item one page up */
			int newFocusIndex = Math.max (0, focusItem.availableIndex - visibleItemCount);
			if (newFocusIndex == focusItem.availableIndex) return;
			setFocusItem (availableItems [newFocusIndex], true);
			showItem (availableItems [newFocusIndex]);
			redrawItem (newFocusIndex, true);
		}
		return;
	}
	/* Shift+PageUp */
	if (anchorItem == null) anchorItem = focusItem;
	int anchorIndex = anchorItem.availableIndex;
	int selectIndex;
	if (focusItem.availableIndex != topIndex) {
		/* select from focus to top item in viewport */
		selectIndex = topIndex;
	} else {
		/* already at top of viewport, so select to top of one page up */
		selectIndex = Math.max (0, topIndex - visibleItemCount);
		if (selectIndex == focusItem.availableIndex && focusItem.isSelected ()) return;
	}
	TreeItem selectedItem = availableItems [selectIndex];
	TreeItem[] newSelection = new TreeItem [Math.abs (anchorIndex - selectIndex) + 1];
	int step = anchorIndex < selectIndex ? 1 : -1;
	int writeIndex = 0;
	for (int i = anchorIndex; i != selectIndex; i += step) {
		newSelection [writeIndex++] = availableItems [i];
	}
	newSelection [writeIndex] = availableItems [selectIndex];
	setSelection (newSelection, false);
	setFocusItem (selectedItem, true);
	showItem (selectedItem);
	Event newEvent = new Event ();
	newEvent.item = selectedItem;
	postEvent (SWT.Selection, newEvent);
}
void onPaint (Event event) {
	TreeColumn[] orderedColumns = getOrderedColumns ();
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
	if (startIndex < availableItemsCount) {
		endIndex = startIndex + Compatibility.ceil (clipping.height, itemHeight);
	}
	startIndex = Math.max (0, startIndex);
	endIndex = Math.min (endIndex, availableItemsCount - 1);

	/* fill background not handled by items */
	gc.setBackground (getBackground ());
	gc.setClipping (clipping);
	int bottomY = endIndex >= 0 ? getItemY (availableItems [endIndex]) + itemHeight : 0;
	int fillHeight = Math.max (0, clientArea.height - bottomY);
	if (fillHeight > 0) {	/* space below bottom item */
		drawBackground (gc, 0, bottomY, clientArea.width, fillHeight);
	}
	if (columns.length > 0) {
		TreeColumn column = orderedColumns [orderedColumns.length - 1];	/* last column */
		int rightX = column.getX () + column.width;
		if (rightX < clientArea.width) {
			drawBackground (gc, rightX, 0, clientArea.width - rightX, clientArea.height - fillHeight);
		}
	}

	/* paint the items */
	boolean noFocusDraw = false;
	int[] lineDash = gc.getLineDash ();
	int lineWidth = gc.getLineWidth ();
	for (int i = startIndex; i <= Math.min (endIndex, availableItemsCount - 1); i++) {
		TreeItem item = availableItems [i];
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
						if (isDisposed () || gc.isDisposed ()) return;	/* ensure that receiver was not disposed in a callback */
					}
				}
			}
		}
		if (isDisposed () || gc.isDisposed ()) return;	/* ensure that receiver was not disposed in a callback */
	}

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

	/* draw focus rectangle */
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
			/* no items, so draw focus border around Tree */
			int y = headerHeight + 1;
			int width = Math.max (0, clientArea.width - 2);
			int height = Math.max (0, clientArea.height - headerHeight - 2);
			gc.setForeground (display.getSystemColor (SWT.COLOR_BLACK));
			gc.setClipping (1, y, width, height);
			gc.setLineDash (new int[] {1, 1});
			gc.drawFocus (1, y, width, height);
		}
	}

	/* draw insert mark */
	if (insertMarkItem != null) {
		Rectangle focusBounds = insertMarkItem.getFocusBounds ();
		gc.setForeground (display.getSystemColor (SWT.COLOR_BLACK));
		gc.setClipping (focusBounds);
		gc.setLineDash (lineDash);
		if (insertMarkPrecedes) {
			gc.drawLine (focusBounds.x, focusBounds.y, focusBounds.x + focusBounds.width, focusBounds.y);
		} else {
			int y = focusBounds.y + focusBounds.height - 1;
			gc.drawLine (focusBounds.x, y, focusBounds.x + focusBounds.width, y);
		}
	}
}
void onResize (Event event) {
	clientArea = getClientArea ();
	/* vertical scrollbar */
	ScrollBar vBar = getVerticalBar ();
	if (vBar != null) {
		int clientHeight = (clientArea.height - getHeaderHeight ()) / itemHeight;
		int thumb = Math.min (clientHeight, availableItemsCount);
		vBar.setThumb (thumb);
		vBar.setPageIncrement (thumb);
		int index = vBar.getSelection ();
		if (index != topIndex) {
			topIndex = index;
			redraw ();
		}
		boolean visible = clientHeight < availableItemsCount;
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
	if (availableItemsCount == 0 && isFocusControl ()) redraw ();
}
void onScrollHorizontal (Event event) {
	ScrollBar hBar = getHorizontalBar (); 
	if (hBar == null) return;
	int newSelection = hBar.getSelection ();
	update ();
	if (availableItemsCount > 0) {
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
	if (vBar != null) {
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
}
void onSpace () {
	if (focusItem == null) return;
	if (!focusItem.isSelected ()) {
		selectItem (focusItem, (style & SWT.MULTI) != 0);
		redrawItem (focusItem.availableIndex, true);
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
	
	/* reassign to current focus' parent item if it has one */
	if (focusItem.parentItem != null) {
		TreeItem item = focusItem.parentItem;
		setFocusItem (item, false);
		showItem (item);
		return;
	}
	
	/* 
	 * reassign to the previous root-level item if there is one, or the next
	 * root-level item otherwise
	 */
	int index = focusItem.getIndex ();
	if (index != 0) {
		index--;
	} else {
		index++;
	}
	if (index < items.length) {
		TreeItem item = items [index];
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
	redrawItems (index, availableItemsCount - 1, false);
}
/*
 * Redraws the tree item at the specified index.  It is valid for this index to reside
 * beyond the last available item.
 */
void redrawItem (int itemIndex, boolean focusBoundsOnly) {
	if (itemIndex == -1) return;
	if (itemIndex < availableItemsCount && !availableItems [itemIndex].isInViewport ()) return;
	redrawItems (itemIndex, itemIndex, focusBoundsOnly);
}
/*
 * Redraws the tree between the start and end item indices inclusive.  It is valid
 * for the end index value to extend beyond the last available item.
 */
void redrawItems (int startIndex, int endIndex, boolean focusBoundsOnly) {
	if (drawCount > 0) return;

	int startY = (startIndex - topIndex) * itemHeight + getHeaderHeight ();
	int height = (endIndex - startIndex + 1) * itemHeight;
	if (focusBoundsOnly) {
		boolean custom = hooks (SWT.EraseItem) || hooks (SWT.PaintItem);
		if (!custom && columns.length > 0) {
			TreeColumn lastColumn;
			if ((style & SWT.FULL_SELECTION) != 0) {
				TreeColumn[] orderedColumns = getOrderedColumns ();
				lastColumn = orderedColumns [orderedColumns.length - 1];
			} else {
				lastColumn = columns [0];
			}
			int rightX = lastColumn.getX () + lastColumn.getWidth ();
			if (rightX <= 0) return;	/* focus column(s) not visible */
		}
		endIndex = Math.min (endIndex, availableItemsCount - 1);
		for (int i = startIndex; i <= endIndex; i++) {
			TreeItem item = availableItems [i];
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
 * Removes all of the items from the receiver.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget ();
	if (items.length == 0) return;
	setRedraw (false);

	setFocusItem (null, false);
	for (int i = 0; i < items.length; i++) {
		items [i].dispose (false);
	}
	items = availableItems = selectedItems = NO_ITEMS;
	availableItemsCount = topIndex = 0;
	anchorItem = lastClickedItem = insertMarkItem = null;
	lastSelectionEvent = null;
	inExpand = false;
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
	TreeItem[] newSelectedItems = new TreeItem [selectedItems.length - 1];
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
 * @see #addSelectionListener
 */
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	removeListener (SWT.Selection, listener);
	removeListener (SWT.DefaultSelection, listener);	
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when items in the receiver are expanded or collapsed.
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
 * @see TreeListener
 * @see #addTreeListener
 */
public void removeTreeListener (TreeListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	removeListener (SWT.Expand, listener);
	removeListener (SWT.Collapse, listener);
}
void reskinChildren (int flags) {
	if (items != null) {
		for (int i=0; i<items.length; i++) {
			TreeItem item = items [i];
			if (item != null) item.reskinChildren (flags);
		}
	}
	if (columns != null) {
		for (int i=0; i<columns.length; i++) {
			TreeColumn column = columns [i];
			if (column != null) column.reskinChildren (flags);
		}
	}
	super.reskinChildren (flags);
}
/**
 * Selects an item in the receiver.  If the item was already
 * selected, it remains selected.
 *
 * @param item the item to be selected
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
 * @since 3.4
 */
public void select (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	selectItem (item, (style & SWT.MULTI) != 0);
	redrawItem (item.availableIndex, true);
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
	selectedItems = new TreeItem [availableItemsCount];
	System.arraycopy (availableItems, 0, selectedItems, 0, availableItemsCount);
	redraw ();
}
void selectItem (TreeItem item, boolean addToSelection) {
	TreeItem[] oldSelectedItems = selectedItems;
	if (!addToSelection || (style & SWT.SINGLE) != 0) {
		selectedItems = new TreeItem[] {item};
		for (int i = 0; i < oldSelectedItems.length; i++) {
			if (oldSelectedItems [i] != item) {
				redrawItem (oldSelectedItems [i].availableIndex, true);
			}
		}
	} else {
		if (item.isSelected ()) return;
		selectedItems = new TreeItem [selectedItems.length + 1];
		System.arraycopy (oldSelectedItems, 0, selectedItems, 0, oldSelectedItems.length);
		selectedItems [selectedItems.length - 1] = item;
	}
}
public void setBackground (Color color) {
	checkWidget ();
	if (color == null) color = display.getSystemColor (SWT.COLOR_LIST_BACKGROUND); 
	super.setBackground (color);
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
 * @see Tree#getColumnOrder()
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 * 
 * @since 3.2
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
	orderedColumns = new TreeColumn [order.length];
	for (int i = 0; i < order.length; i++) {
		orderedColumns [i] = columns [order [i]];
	}
	/*
	 * If the first ordered column has changed then the old and new ordered column 0's
	 * have to recompute their display texts since they will now have just gained/lost
	 * space as a result of the hierarchy decorations that appear in ordered column 0.
	 */
	if (oldOrder [0] != order [0]) {
		orderedCol0imageWidth = columns [order [0]].itemImageWidth;
		GC gc = new GC (this);
		for (int i = 0; i < items.length; i++) {
			items [i].updateColumnWidth (columns [oldOrder [0]], gc);
			items [i].updateColumnWidth (columns [order [0]], gc);
		}
		gc.dispose();
	}
	for (int i = 0; i < orderedColumns.length; i++) {
		TreeColumn column = orderedColumns [i];
		if (!column.isDisposed () && column.getX () != oldX [column.getIndex ()]) {
			column.sendEvent (SWT.Move);
		}
	}

	redraw ();
	if (drawCount <= 0 && header.isVisible ()) header.redraw ();
}
void setFocusItem (TreeItem item, boolean redrawOldFocus) {
	if (item == focusItem) return;
	TreeItem oldFocusItem = focusItem;
	focusItem = item;
	if (redrawOldFocus && oldFocusItem != null) {
		redrawItem (oldFocusItem.availableIndex, true);
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
	for (int i = 0; i < items.length; i++) {
		items [i].updateFont (gc);
	}
	
	gc.dispose ();
	
	if (drawCount <= 0 && header.isVisible ()) header.redraw ();
	
	/* update scrollbars */
	if (columns.length == 0) updateHorizontalBar ();
	ScrollBar vBar = getVerticalBar ();
	if (vBar != null) {
		int thumb = (clientArea.height - getHeaderHeight ()) / itemHeight;
		if (vBar.getThumb () != thumb) {
			vBar.setThumb (thumb);
			vBar.setPageIncrement (thumb);
		}
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
 * 
 * @since 3.1
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
 * Display a mark indicating the point at which an item will be inserted.
 * The drop insert item has a visual hint to show where a dragged item 
 * will be inserted when dropped on the tree.
 * 
 * @param item the insert item.  Null will clear the insertion mark.
 * @param before true places the insert mark above 'item'. false places 
 *	the insert mark below 'item'.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setInsertMark (TreeItem item, boolean before) {
	checkWidget ();
	if (item != null && item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (item != null && item.parent != this) return;
	if (item == insertMarkItem && before == insertMarkPrecedes) return;	/* no change */
	
	TreeItem oldInsertItem = insertMarkItem;
	insertMarkItem = item;
	insertMarkPrecedes = before;
	if (oldInsertItem != null && oldInsertItem.availableIndex != -1) {
		redrawItem (oldInsertItem.availableIndex, true);
	}
	if (item != null && item != oldInsertItem && item.availableIndex != -1) {
		redrawItem (item.availableIndex, true);
	}
}
/**
 * Sets the number of root-level items contained in the receiver.
 *
 * @param count the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setItemCount (int count) {
	checkWidget ();
	count = Math.max (0, count);
	if (count == items.length) return;
	int oldCount = availableItemsCount;
	int redrawStart, redrawEnd;

	/* if the new item count is less than the current count then remove all excess items from the end */
	if (count < items.length) {
		redrawStart = count > 0 ? items [count - 1].availableIndex : 0;
		redrawEnd = availableItemsCount - 1;
		availableItemsCount = items [count].availableIndex;
		for (int i = count; i < items.length; i++) {
			items [i].dispose (false);
		}
		if (count == 0) {
			items = Tree.NO_ITEMS;
		} else {
			TreeItem[] newItems = new TreeItem [count];
			System.arraycopy (items, 0, newItems, 0, count);
			items = newItems;
		}

		int newSelectedCount = 0;
		for (int i = 0; i < selectedItems.length; i++) {
			if (!selectedItems [i].isDisposed ()) newSelectedCount++;
		}
		if (newSelectedCount != selectedItems.length) {
			/* one or more selected items have been disposed */
			TreeItem[] newSelectedItems = new TreeItem [newSelectedCount];
			int pos = 0;
			for (int i = 0; i < selectedItems.length; i++) {
				TreeItem item = selectedItems [i];
				if (!item.isDisposed ()) {
					newSelectedItems [pos++] = item;
				}
			}
			selectedItems = newSelectedItems;
		}

		if (insertMarkItem != null && insertMarkItem.isDisposed ()) insertMarkItem = null;
		if (lastClickedItem != null && lastClickedItem.isDisposed ()) lastClickedItem = null;
		if (anchorItem != null && anchorItem.isDisposed ()) anchorItem = null;
		if (focusItem != null && focusItem.isDisposed ()) {
			TreeItem newFocusItem = count > 0 ? items [count - 1] : null; 
			setFocusItem (newFocusItem, false);
		}
		if (columns.length == 0) updateHorizontalBar ();
	} else {
		int grow = count - items.length;
		redrawStart = items.length == 0 ? 0 : items [items.length - 1].availableIndex;
		redrawEnd = availableItemsCount + grow - 1;
		TreeItem[] newItems = new TreeItem [count];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
		if (availableItems.length < availableItemsCount + grow) {
			TreeItem[] newAvailableItems = new TreeItem [availableItemsCount + grow];
			System.arraycopy (availableItems, 0, newAvailableItems, 0, availableItemsCount);
			availableItems = newAvailableItems;
		}
		for (int i = items.length - grow; i < count; i++) {
			TreeItem newItem = new TreeItem (this, SWT.NONE, i, false);
			items [i] = newItem;
			items [i].availableIndex = availableItemsCount;
			availableItems [availableItemsCount++] = newItem;
		}
		if (oldCount == 0) focusItem = availableItems [0];
	}

	updateVerticalBar ();
	/*
	 * If this is the focus control and the available item count is going from 0->!0 or !0->0
	 * then the receiver must be redrawn to ensure that its boundary focus ring is updated.
	 */
	if ((oldCount == 0 || availableItemsCount == 0) && isFocusControl ()) {
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
 * and marks it invisible otherwise. Note that some platforms draw 
 * grid lines while others may draw alternating row colors.
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
 * 
 * @since 3.1
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
			if (availableItems.length - availableItemsCount > 3) {
				TreeItem[] newAvailableItems = new TreeItem [availableItemsCount];
				System.arraycopy (availableItems, 0, newAvailableItems, 0, availableItemsCount);
				availableItems = newAvailableItems;
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
public void setSelection (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (new TreeItem [] {item}, true);
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
 * @see Tree#deselectAll()
 */
public void setSelection (TreeItem[] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (items, true);
}

void setSelection (TreeItem[] items, boolean updateViewport) {
	if (items.length == 0 || ((style & SWT.SINGLE) != 0 && items.length > 1)) {
		deselectAll ();
		return;
	}
	TreeItem[] oldSelection = selectedItems;
	
	/* remove null and duplicate items */
	int index = 0;
	selectedItems = new TreeItem [items.length];	/* initially assume all valid items */
	for (int i = 0; i < items.length; i++) {
		TreeItem item = items [i];
		if (item != null && item.parent == this && !item.isSelected ()) {
			selectedItems [index++] = item;
		}
	}
	if (index != items.length) {
		/* an invalid item was provided so resize the array accordingly */
		TreeItem[] temp = new TreeItem [index];
		System.arraycopy (selectedItems, 0, temp, 0, index);
		selectedItems = temp;
	}
	if (selectedItems.length == 0) {	/* no valid items */
		deselectAll ();
		return;
	}

	for (int i = 0; i < oldSelection.length; i++) {
		if (!oldSelection [i].isSelected ()) {
			int availableIndex = oldSelection [i].availableIndex;
			if (availableIndex != -1) {
				redrawItem (availableIndex, true);
			}
		}
	}
	if (updateViewport) {
		showItem (selectedItems [0]);
		setFocusItem (selectedItems [0], true);
	}
	for (int i = 0; i < selectedItems.length; i++) {
		int availableIndex = selectedItems [i].availableIndex;
		if (availableIndex != -1) {
			redrawItem (availableIndex, true);
		}
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
public void setSortColumn (TreeColumn column) {
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
 * Sets the item which is currently at the top of the receiver.
 * This item can change when items are expanded, collapsed, scrolled
 * or new items are added or removed.
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
 * @see Tree#getTopItem()
 * 
 * @since 2.1
 */
public void setTopItem (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (item.parent != this) return;

	/* item must be available */
	if (!item.isAvailable ()) item.parentItem.expandAncestors ();

	int visibleItemCount = (clientArea.height - getHeaderHeight ()) / itemHeight;
	if (availableItemsCount < visibleItemCount) return;
	int index = Math.min (item.availableIndex, availableItemsCount - visibleItemCount);
	if (topIndex == index) return;

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
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void showColumn (TreeColumn column) {
	checkWidget ();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (column.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (column.parent != this) return;

	int x = column.getX ();
	int rightX = x + column.width;
	if (0 <= x && rightX <= clientArea.width) return;	 /* column is fully visible */

	headerHideToolTip ();
	int absX = 0;	/* the X of the column irrespective of the horizontal scroll */
	TreeColumn[] orderedColumns = getOrderedColumns ();
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
 * this method simply returns.  Otherwise, the items are scrolled
 * and expanded until the item is visible.
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
 * @see Tree#showSelection()
 */
public void showItem (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (item.parent != this) return;
	
	/* item must be available */
	if (!item.isAvailable ()) item.parentItem.expandAncestors ();
	
	int index = item.availableIndex;
	int visibleItemCount = (clientArea.height - getHeaderHeight ()) / itemHeight;
	/* nothing to do if item is already in viewport */
	if (topIndex <= index && index < topIndex + visibleItemCount) return;
	
	if (index <= topIndex) {
		/* item is above current viewport, so show on top */
		setTopItem (item);
	} else {
		/* item is below current viewport, so show on bottom */
		visibleItemCount = Math.max (visibleItemCount, 1);	/* item to show should be top item */
		setTopItem (availableItems [Math.min (index - visibleItemCount + 1, availableItemsCount - 1)]);
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
 * @see Tree#showItem(TreeItem)
 */
public void showSelection () {
	checkWidget ();
	if (selectedItems.length == 0) return;
	showItem (selectedItems [0]);
}
void updateColumnWidth (TreeColumn column, int width) {
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
	if (focusItem != null) redrawItem (focusItem.availableIndex, true);

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
	for (int i = 0; i < items.length; i++) {
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
	TreeColumn[] orderedColumns = getOrderedColumns ();
	for (int i = column.getOrderIndex () + 1; i < orderedColumns.length; i++) {
		if (!orderedColumns [i].isDisposed ()) {
			orderedColumns [i].sendEvent (SWT.Move);
		}
	}

	if (availableItemsCount == 0) redraw ();	/* ensure that static focus rectangle updates properly */
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
		for (int i = 0; i < availableItemsCount; i++) {
			Rectangle itemBounds = availableItems [i].getCellBounds (0);
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
		if (hBar.getThumb () != thumb) {
			hBar.setThumb (thumb);
			hBar.setPageIncrement (thumb);
		}
		hBar.setVisible (clientAreaWidth <= newRightX);
		return;
	}

	int previousRightX = newRightX - rightXchange;
	if (previousRightX != barMaximum) {
		/* this was not the rightmost item, so just check for client width change */
		int clientAreaWidth = clientArea.width;
		int thumb = Math.min (barMaximum, clientAreaWidth);
		if (hBar.getThumb () != thumb) {
			hBar.setThumb (thumb);
			hBar.setPageIncrement (thumb);
		}
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
	int maximum = Math.max (1, availableItemsCount);
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
