/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

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
 */
public class Tree extends Composite {
	NSTableColumn firstColumn, checkColumn;
	NSBrowserCell dataCell;
	NSTableHeaderView headerView;
	TreeItem [] items;
	int itemCount;
	TreeColumn [] columns;
	TreeColumn sortColumn;
	int columnCount;
	int sortDirection;
	boolean ignoreExpand, ignoreSelect, ignoreRedraw, reloadPending;
	Rectangle imageBounds;

	static final int FIRST_COLUMN_MINIMUM_WIDTH = 5;

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
}

void _addListener (int eventType, Listener listener) {
	super._addListener (eventType, listener);
	clearCachedWidth (items);
}

TreeItem _getItem (TreeItem parentItem, int index, boolean create) {
	int count;
	TreeItem[] items;
	if (parentItem != null) {
		count = parentItem.itemCount;
		items = parentItem.items;
	} else {
		count = this.itemCount;
		items = this.items;
	}
	if (index < 0 || index >= count) return null;
	TreeItem item = items [index]; 
	if (item != null || (style & SWT.VIRTUAL) == 0 || !create) return item;
	item = new TreeItem (this, parentItem, SWT.NONE, index, false);
	items [index] = item;
	return item;
}

int /*long*/ accessibilityAttributeValue (int /*long*/ id, int /*long*/ sel, int /*long*/ arg0) {

	if (accessible != null) {
		NSString attribute = new NSString(arg0);
		id returnValue = accessible.internal_accessibilityAttributeValue(attribute, ACC.CHILDID_SELF);
		if (returnValue != null) return returnValue.id;
	}
	
	NSString attributeName = new NSString(arg0);
	
	// Accessibility Verifier queries for a title or description.  NSOutlineView doesn't
	// seem to return either, so we return a default description value here.
	if (attributeName.isEqualToString (OS.NSAccessibilityDescriptionAttribute)) {
		return NSString.stringWith("").id;
	}
	
	return super.accessibilityAttributeValue(id, sel, arg0);
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
public void addSelectionListener(SelectionListener listener) {
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
public void addTreeListener(TreeListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Expand, typedListener);
	addListener (SWT.Collapse, typedListener);
}

int calculateWidth (TreeItem[] items, int index, GC gc, boolean recurse) {
	if (items == null) return 0;
	int width = 0;
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null) {
			int itemWidth = item.calculateWidth (index, gc);
			width = Math.max (width, itemWidth);
			if (recurse && item.getExpanded ()) {
				width = Math.max (width, calculateWidth (item.items, index, gc, recurse));
			}
		}
	}
	return width;
}

boolean checkData (TreeItem item) {
	if (item.cached) return true;
	if ((style & SWT.VIRTUAL) != 0) {
		item.cached = true;
		Event event = new Event ();
		TreeItem parentItem = item.getParentItem ();
		event.item = item;
		event.index = parentItem == null ? indexOf (item) : parentItem.indexOf (item);
		ignoreRedraw = true;
		sendEvent (SWT.SetData, event);
		//widget could be disposed at this point
		ignoreRedraw = false;
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

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void checkItems () {
	if (!reloadPending) return;
	reloadPending = false;
	TreeItem[] selectedItems = getSelection ();
	((NSOutlineView)view).reloadData ();
	selectItems (selectedItems, true);
}

void clear (TreeItem parentItem, int index, boolean all) {
	TreeItem item = _getItem (parentItem, index, false);
	if (item != null) {
		item.clear();
		item.redraw (-1);
		if (all) {
			clearAll (item, true);
		}
	}
}

void clearAll (TreeItem parentItem, boolean all) {
	int count = getItemCount (parentItem);
	if (count == 0) return;
	TreeItem [] children = parentItem == null ? items : parentItem.items; 
	for (int i=0; i<count; i++) {
		TreeItem item = children [i];
		if (item != null) {
			item.clear ();
			item.redraw (-1);
			if (all) clearAll (item, true);
		}
	}
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
public void clear (int index, boolean all) {
	checkWidget ();
	int count = getItemCount ();
	if (index < 0 || index >= count) error (SWT.ERROR_INVALID_RANGE);
	clear (null, index, all);
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
public void clearAll (boolean all) {
	checkWidget ();
	clearAll (null, all);
}

void clearCachedWidth (TreeItem[] items) {
	if (items == null) return;
	for (int i = 0; i < items.length; i++) {
		TreeItem item = items [i];
		if (item == null) break;
		item.width = -1;
		clearCachedWidth (item.items);
	}
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	if (wHint == SWT.DEFAULT) {
		if (columnCount != 0) {
			for (int i=0; i<columnCount; i++) {
				width += columns [i].getWidth ();
			}
		} else {
			GC gc = new GC (this);
			width = calculateWidth (items, 0, gc, true);
			gc.dispose ();
		}
		if ((style & SWT.CHECK) != 0) width += getCheckColumnWidth ();
	} else {
		width = wHint;
	}
	if (hHint == SWT.DEFAULT) {
		height = (int)/*64*/((NSOutlineView) view).numberOfRows () * getItemHeight () + getHeaderHeight ();
	} else {
		height = hHint;
	}
	if (width <= 0) width = DEFAULT_WIDTH;
	if (height <= 0) height = DEFAULT_HEIGHT;
	Rectangle rect = computeTrim (0, 0, width, height);
	return new Point (rect.width, rect.height);
}

void createColumn (TreeItem item, int index) {
	if (item.items != null) {
		for (int i = 0; i < item.items.length; i++) {
			if (item.items[i] != null) createColumn (item.items[i], index);
		}
	}
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

void createHandle () {
	NSScrollView scrollWidget = (NSScrollView) new SWTScrollView ().alloc ();
	scrollWidget.init ();
	scrollWidget.setHasHorizontalScroller ((style & SWT.H_SCROLL) != 0);
	scrollWidget.setHasVerticalScroller ((style & SWT.V_SCROLL) != 0);
	scrollWidget.setAutohidesScrollers (true);
	scrollWidget.setBorderType(hasBorder () ? OS.NSBezelBorder : OS.NSNoBorder);
	
	NSOutlineView widget = (NSOutlineView) new SWTOutlineView ().alloc ();
	/*
	* Bug in Cocoa.  Calling init, instead of initWithFrame on an NSOutlineView
	* cause the NSOutlineView to leak some memory.  The work around is to call 
	* initWithFrame and pass an empty NSRect instead of calling init. 
	*/
	widget.initWithFrame(new NSRect());
	widget.setAllowsMultipleSelection ((style & SWT.MULTI) != 0);
	widget.setAllowsColumnReordering (false);
	widget.setAutoresizesOutlineColumn (false);
	widget.setAutosaveExpandedItems (true);
	widget.setDataSource (widget);
	widget.setDelegate (widget);
	widget.setColumnAutoresizingStyle (OS.NSTableViewNoColumnAutoresizing);
	NSSize spacing = new NSSize();
	spacing.width = spacing.height = 1;
	widget.setIntercellSpacing(spacing);
	widget.setDoubleAction (OS.sel_sendDoubleSelection);
	if (!hasBorder ()) widget.setFocusRingType (OS.NSFocusRingTypeNone);
	
	headerView = (NSTableHeaderView)new SWTTableHeaderView ().alloc ().init ();
	widget.setHeaderView (null);
	
	NSString str = NSString.stringWith ("");  //$NON-NLS-1$
	if ((style & SWT.CHECK) != 0) {
		checkColumn = (NSTableColumn) new NSTableColumn ().alloc ();
		checkColumn.initWithIdentifier (checkColumn);
		checkColumn.headerCell ().setTitle (str);
		widget.addTableColumn (checkColumn);
		widget.setOutlineTableColumn (checkColumn);
		NSButtonCell cell = (NSButtonCell) new NSButtonCell ().alloc ().init ();
		checkColumn.setDataCell (cell);
		cell.setButtonType (OS.NSSwitchButton);
		cell.setImagePosition (OS.NSImageOnly);
		cell.setAllowsMixedState (true);
		checkColumn.setWidth (getCheckColumnWidth ());
		checkColumn.setResizingMask (OS.NSTableColumnNoResizing);
		checkColumn.setEditable (false);
		cell.release ();
	}
	
	firstColumn = (NSTableColumn) new NSTableColumn ().alloc ();
	firstColumn.initWithIdentifier (firstColumn);
	/*
	* Feature in Cocoa.  If a column's width is too small to show any content
	* then outlineView_objectValueForTableColumn_byItem is never invoked to
	* query for item values, which is a problem for VIRTUAL Trees.  The
	* workaround is to ensure that, for 0-column Trees, the internal first
	* column always has a minimal width that makes this call come in.
	*/
	firstColumn.setMinWidth (FIRST_COLUMN_MINIMUM_WIDTH);
	firstColumn.headerCell ().setTitle (str);
	widget.addTableColumn (firstColumn);
	widget.setOutlineTableColumn (firstColumn);
	dataCell = (NSBrowserCell)new SWTBrowserCell ().alloc ().init ();
	dataCell.setLeaf (true);
	firstColumn.setDataCell (dataCell);
	
	scrollView = scrollWidget;
	view = widget;
}

void createItem (TreeColumn column, int index) {
	if (!(0 <= index && index <= columnCount)) error (SWT.ERROR_INVALID_RANGE);
	if (index == 0) {
		// first column must be left aligned
		column.style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
		column.style |= SWT.LEFT;
	}
	if (columnCount == columns.length) {
		TreeColumn [] newColumns = new TreeColumn [columnCount + 4];
		System.arraycopy (columns, 0, newColumns, 0, columns.length);
		columns = newColumns;
	}
	NSTableColumn nsColumn;
	if (columnCount == 0) {
		//TODO - clear attributes, alignment etc.
		nsColumn = firstColumn;
		nsColumn.setMinWidth (0);
		firstColumn = null;
	} else {
		//TODO - set attributes, alignment etc.
		NSOutlineView outlineView = (NSOutlineView)view;
		NSString str = NSString.stringWith ("");
		nsColumn = (NSTableColumn) new NSTableColumn ().alloc ();
		nsColumn.initWithIdentifier (nsColumn);
		nsColumn.setMinWidth(0);
		nsColumn.headerCell ().setTitle (str);
		outlineView.addTableColumn (nsColumn);
		int checkColumn = (style & SWT.CHECK) != 0 ? 1 : 0;
		outlineView.moveColumn (columnCount + checkColumn, index + checkColumn);
		nsColumn.setDataCell (dataCell);
		if (index == 0) {
			outlineView.setOutlineTableColumn (nsColumn);
		}
	}
	column.createJNIRef ();
	NSTableHeaderCell headerCell = (NSTableHeaderCell)new SWTTableHeaderCell ().alloc ().init ();
	nsColumn.setHeaderCell (headerCell);
	display.addWidget (headerCell, column);
	column.nsColumn = nsColumn;
	nsColumn.setWidth (0);
	System.arraycopy (columns, index, columns, index + 1, columnCount++ - index);
	columns [index] = column;
	for (int i = 0; i < itemCount; i++) {
		TreeItem item = items [i];
		if (item != null) {
			if (columnCount > 1) {
				createColumn (item, index);
			}
		}
	}
}

void createItem (TreeItem item, TreeItem parentItem, int index) {
	int count;
	TreeItem [] items;
	if (parentItem != null) {
		count = parentItem.itemCount;
		items = parentItem.items;
	} else {
		count = this.itemCount;
		items = this.items;
	}
	if (index == -1) index = count;
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	if (count == items.length) {
		TreeItem [] newItems = new TreeItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
		if (parentItem != null) {
			parentItem.items = items;
		} else {
			this.items = items;
		}
	}
	System.arraycopy (items, index, items, index + 1, count++ - index);
	items [index] = item;
	item.items = new TreeItem [4];
	SWTTreeItem handle = (SWTTreeItem) new SWTTreeItem ().alloc ().init ();
	item.handle = handle;
	item.createJNIRef ();
	item.register ();
	if (parentItem != null) {
		parentItem.itemCount = count;
	} else {
		this.itemCount = count;
	}
	ignoreExpand = true;
	reloadItem (parentItem, true);
	ignoreExpand = false;
}

void createWidget () {
	super.createWidget ();
	items = new TreeItem [4];
	columns = new TreeColumn [4];
}

Color defaultBackground () {
	return display.getWidgetColor (SWT.COLOR_LIST_BACKGROUND);
}

NSFont defaultNSFont () {
	return display.outlineViewFont;
}

Color defaultForeground () {
	return display.getWidgetColor (SWT.COLOR_LIST_FOREGROUND);
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
	NSTableView widget = (NSOutlineView) view;
	ignoreSelect = true;
	widget.deselectAll (null);
	ignoreSelect = false;
}

void deregister () {
	super.deregister ();
	display.removeWidget (headerView);
	display.removeWidget (dataCell);
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
	NSOutlineView widget = (NSOutlineView)view;
	int /*long*/ row = widget.rowForItem(item.handle);
	ignoreSelect = true;
	widget.deselectRow (row);
	ignoreSelect = false;
}

void destroyItem (TreeColumn column) {
	int index = 0;
	while (index < columnCount) {
		if (columns [index] == column) break;
		index++;
	}
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
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

	int oldIndex = (int)/*64*/((NSOutlineView)view).columnWithIdentifier (column.nsColumn);

	System.arraycopy (columns, index + 1, columns, index, --columnCount - index);
	columns [columnCount] = null;
	if (columnCount == 0) {
		//TODO - reset attributes
		firstColumn = column.nsColumn;
		firstColumn.retain ();
		/*
		* Feature in Cocoa.  If a column's width is too small to show any content
		* then outlineView_objectValueForTableColumn_byItem is never invoked to
		* query for item values, which is a problem for VIRTUAL Trees.  The
		* workaround is to ensure that, for 0-column Trees, the internal first
		* column always has a minimal width that makes this call come in.
		*/
		firstColumn.setMinWidth (FIRST_COLUMN_MINIMUM_WIDTH);
		setScrollWidth ();
	} else {
		if (index == 0) {
			((NSOutlineView)view).setOutlineTableColumn(columns[0].nsColumn);
		}
		((NSOutlineView)view).removeTableColumn(column.nsColumn);
	}

	NSArray array = ((NSOutlineView)view).tableColumns ();
	int arraySize = (int)/*64*/array.count ();
	for (int i = oldIndex; i < arraySize; i++) {
		int /*long*/ columnId = array.objectAtIndex (i).id;
		for (int j = 0; j < columnCount; j++) {
			if (columns[j].nsColumn.id == columnId) {
				columns [j].sendEvent (SWT.Move);
				break;
			}
		}
	}
}

void destroyItem (TreeItem item) {
	int count;
	TreeItem[] items;
	TreeItem parentItem = item.parentItem;
	if (parentItem != null) {
		count = parentItem.itemCount;
		items = parentItem.items;
	} else {
		count = this.itemCount;
		items = this.items;
	}
	int index = 0;
	while (index < count) {
		if (items [index] == item) break;
		index++;
	}
	System.arraycopy (items, index + 1, items, index, --count - index);
	items [count] = null;
	if (parentItem != null) {
		parentItem.itemCount = count;
	} else {
		this.itemCount = count;
	}
	reloadItem (parentItem, true);
	setScrollWidth ();
}

boolean dragDetect(int x, int y, boolean filter, boolean[] consume) {
	boolean dragging = super.dragDetect(x, y, filter, consume);
	if (dragging) {
		NSTableView widget = (NSTableView)view;
		NSPoint pt = new NSPoint();
		pt.x = x;
		pt.y = y;
		int row = (int)/*64*/widget.rowAtPoint(pt);
		if (!widget.isRowSelected(row)) {
			//TODO expand current selection when Shift, Command key pressed??
			NSIndexSet indexes = new NSIndexSet (NSIndexSet.indexSetWithIndex (row));
			widget.selectRowIndexes (indexes, false);
		}
	}
	consume[0] = dragging;
	return dragging;
}

void drawInteriorWithFrame_inView (int /*long*/ id, int /*long*/ sel, int /*long*/ cellFrame, int /*long*/ view) {
	NSRect rect = new NSRect ();
	OS.memmove (rect, cellFrame, NSRect.sizeof);
	boolean hooksErase = hooks (SWT.EraseItem);
	boolean hooksPaint = hooks (SWT.PaintItem);
	boolean hooksMeasure = hooks (SWT.MeasureItem);

	NSOutlineView outlineView = (NSOutlineView)this.view;
	NSBrowserCell cell = new NSBrowserCell (id);
	NSPoint pt = new NSPoint();
	pt.x = rect.x + rect.width / 2;
	pt.y = rect.y + rect.height / 2;
	int rowIndex = (int)outlineView.rowAtPoint(pt);
	TreeItem item = (TreeItem) display.getWidget (outlineView.itemAtRow (rowIndex).id);
	int nsColumnIndex = 0;
	int columnIndex = 0;
	if (columnCount == 0) {
		nsColumnIndex = (style & SWT.CHECK) != 0 ? 1 : 0;
	} else {
		nsColumnIndex = (int)outlineView.columnAtPoint (pt);
		NSArray nsColumns = outlineView.tableColumns ();
		id nsColumn = nsColumns.objectAtIndex (nsColumnIndex);
		for (int i = 0; i < columnCount; i++) {
			if (columns[i].nsColumn.id == nsColumn.id) {
				columnIndex = i;
				break;
			}
		}
	}

	Color background = item.cellBackground != null ? item.cellBackground [columnIndex] : null;
	if (background == null) background = item.background;
	boolean drawBackground = background != null;
	boolean drawForeground = true;
	boolean isSelected = outlineView.isRowSelected (rowIndex);
	boolean drawSelection = isSelected;

	Color selectionBackground = null;
	Color selectionForeground = null;
	if (isSelected && (hooksErase || hooksPaint)) {
		NSColor nsSelectionForeground, nsSelectionBackground;
		if (isFocusControl ()) {
			nsSelectionForeground = NSColor.alternateSelectedControlTextColor ();
		} else {
			nsSelectionForeground = NSColor.selectedControlTextColor ();
		}
		nsSelectionForeground = nsSelectionForeground.colorUsingColorSpace (NSColorSpace.deviceRGBColorSpace ());
		nsSelectionBackground = cell.highlightColorInView (outlineView);
		nsSelectionBackground = nsSelectionBackground.colorUsingColorSpace (NSColorSpace.deviceRGBColorSpace ());
		float /*double*/[] components = new float /*double*/[(int)/*64*/nsSelectionForeground.numberOfComponents ()];
		nsSelectionForeground.getComponents (components);	
		selectionForeground = Color.cocoa_new (display, components);
		components = new float /*double*/[(int)/*64*/nsSelectionBackground.numberOfComponents ()];
		nsSelectionBackground.getComponents (components);	
		selectionBackground = Color.cocoa_new (display, components);
	}
	
	NSSize contentSize = cell.cellSize();
	NSSize spacing = outlineView.intercellSpacing();
	int contentWidth = (int)Math.ceil (contentSize.width);
	int itemHeight = (int)Math.ceil (outlineView.rowHeight() + spacing.height);
	
	NSRect cellRect = outlineView.rectOfColumn (nsColumnIndex);
	cellRect.y = rect.y;
	cellRect.height = rect.height + spacing.height;
	if (columnCount == 0) {
		NSRect rowRect = outlineView.rectOfRow (rowIndex);
		cellRect.width = rowRect.width;
	}
	
	if (hooksMeasure) {
		GCData data = new GCData ();
		data.paintRect = outlineView.frame ();
		GC gc = GC.cocoa_new (this, data);
		gc.setFont (item.getFont (columnIndex));
		Event event = new Event ();
		event.item = item;
		event.gc = gc;
		event.index = columnIndex;
		event.width = contentWidth;
		event.height = itemHeight;
		sendEvent (SWT.MeasureItem, event);
		gc.dispose ();
		if (isDisposed ()) return;
		if (itemHeight < event.height) {
			outlineView.setRowHeight (event.height);
		}
		if (columnCount == 0 && columnIndex == 0 && contentWidth != event.width) {
			item.width = event.width;
			item.width += outlineView.indentationPerLevel () * (1 + outlineView.levelForItem (item.handle));
			if (setScrollWidth (item)) {
				outlineView.setNeedsDisplay(true);
			}
		}
	}	

	if (hooksErase) {
		GCData data = new GCData ();
		data.paintRect = cellRect;
		GC gc = GC.cocoa_new (this, data);
		gc.setFont (item.getFont (columnIndex));
		if (isSelected) {
			gc.setForeground (selectionForeground);
			gc.setBackground (selectionBackground);
		} else {
			gc.setForeground (item.getForeground (columnIndex));
			gc.setBackground (item.getBackground (columnIndex));
		}
		gc.setClipping ((int)cellRect.x, (int)cellRect.y, (int)cellRect.width, (int)cellRect.height);
		Event event = new Event ();
		event.item = item;
		event.gc = gc;
		event.index = columnIndex;
		event.detail = SWT.FOREGROUND;
		if (drawBackground) event.detail |= SWT.BACKGROUND;
		if (isSelected) event.detail |= SWT.SELECTED;
		event.x = (int)cellRect.x;
		event.y = (int)cellRect.y;
		event.width = (int)cellRect.width;
		event.height = (int)cellRect.height;
		sendEvent (SWT.EraseItem, event);
		gc.dispose ();
		if (item.isDisposed ()) return;
		if (!event.doit) {
			drawForeground = drawBackground = drawSelection = false; 
		} else {
			drawBackground = drawBackground && (event.detail & SWT.BACKGROUND) != 0;
			drawForeground = (event.detail & SWT.FOREGROUND) != 0;
			drawSelection = drawSelection && (event.detail & SWT.SELECTED) != 0;			
		}
		if (drawSelection) {
			cellRect.height -= spacing.height;
			callSuper (outlineView.id, OS.sel_highlightSelectionInClipRect_, cellRect);
			cellRect.height += spacing.height;
		}
	}

	if (drawBackground && !drawSelection) {
		NSGraphicsContext context = NSGraphicsContext.currentContext ();
		context.saveGraphicsState ();
		float /*double*/ [] colorRGB = background.handle;
		NSColor color = NSColor.colorWithDeviceRed (colorRGB[0], colorRGB[1], colorRGB[2], 1f);
		color.setFill ();
		NSBezierPath.fillRect (cellRect);
		context.restoreGraphicsState ();
	}

	if (drawForeground) {
		cell.setHighlighted (false);
		callSuper (id, sel, rect, view);
	}

	if (hooksPaint) {
		NSRect contentRect = cell.titleRectForBounds (rect);
		GCData data = new GCData ();
		data.paintRect = cellRect;
		GC gc = GC.cocoa_new (this, data);
		gc.setFont (item.getFont (columnIndex));
		if (isSelected) {
			gc.setForeground (selectionForeground);
			gc.setBackground (selectionBackground);
		} else {
			gc.setForeground (item.getForeground (columnIndex));
			gc.setBackground (item.getBackground (columnIndex));
		}
		gc.setClipping ((int)cellRect.x, (int)cellRect.y, (int)cellRect.width, (int)cellRect.height);
		Event event = new Event ();
		event.item = item;
		event.gc = gc;
		event.index = columnIndex;
		if (isSelected) event.detail |= SWT.SELECTED;
		event.x = (int)contentRect.x;
		event.y = (int)contentRect.y;
		event.width = contentWidth;
		event.height = itemHeight;
		sendEvent (SWT.PaintItem, event);
		gc.dispose ();
	}
}

Widget findTooltip (NSPoint pt) {
	NSTableView widget = (NSTableView)view;
	NSTableHeaderView headerView = widget.headerView();
	if (headerView != null) {
		pt = headerView.convertPoint_fromView_ (pt, null);
		int /*long*/ index = headerView.columnAtPoint (pt);
		if (index != -1) {
			NSArray nsColumns = widget.tableColumns ();
			id nsColumn = nsColumns.objectAtIndex (index);
			for (int i = 0; i < columnCount; i++) {
				TreeColumn column = columns [i];
				if (column.nsColumn.id == nsColumn.id) {
					return column;
				}
			}
		}
	}
	return super.findTooltip (pt);
}

int getCheckColumnWidth () {
	return (int)checkColumn.dataCell().cellSize().width;
}

public Rectangle getClientArea () {
	checkWidget ();
	Rectangle rect = super.getClientArea ();
	NSTableHeaderView headerView = ((NSTableView) view).headerView ();
	if (headerView != null) {
		int height =  (int) headerView.bounds ().height;
		rect.y -= height;
		rect.height += height;
	}
	return rect;
}

TreeColumn getColumn (id id) {
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
	if (!(0 <=index && index < columnCount)) error (SWT.ERROR_INVALID_RANGE);
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
 * @see Tree#setColumnOrder(int[])
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 * 
 * @since 3.2
 */
public int [] getColumnOrder () {
	checkWidget ();
	int [] order = new int [columnCount];
	for (int i = 0; i < columnCount; i++) {
		TreeColumn column = columns [i];
		int index = (int)/*64*/((NSOutlineView)view).columnWithIdentifier (column.nsColumn);
		if ((style & SWT.CHECK) != 0) index -= 1;
		order [index] = i;
	}
	return order;
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
public TreeColumn [] getColumns () {
	checkWidget ();
	TreeColumn [] result = new TreeColumn [columnCount];
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
 * 
 * @since 3.1
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
 * @since 3.1 
 */
public int getHeaderHeight () {
	checkWidget ();
	NSTableHeaderView headerView = ((NSOutlineView) view).headerView ();
	if (headerView == null) return 0;
	return (int) headerView.bounds ().height;
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
	return ((NSOutlineView) view).headerView () != null;
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
	int count = getItemCount ();
	if (index < 0 || index >= count) error (SWT.ERROR_INVALID_RANGE);
	return _getItem (null, index, true);
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
	checkItems ();
	NSOutlineView widget = (NSOutlineView)view;
	NSPoint pt = new NSPoint();
	pt.x = point.x;
	pt.y = point.y;
	int row = (int)/*64*/widget.rowAtPoint(pt);
	if (row == -1) return null;
	id id = widget.itemAtRow(row);
	Widget item = display.getWidget (id.id);
	if (item != null && item instanceof TreeItem) {
		return (TreeItem)item;
	}
	return null;
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
	return itemCount;
}

int getItemCount (TreeItem item) {
	return item == null ? itemCount : item.itemCount;
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
	return (int)((NSOutlineView) view).rowHeight ();
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
public TreeItem [] getItems () {
	checkWidget ();
	TreeItem [] result = new TreeItem [itemCount];
	for (int i=0; i<itemCount; i++) {
		result [i] = _getItem (null, i, true);
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
 * 
 * @since 3.1
 */
public boolean getLinesVisible () {
	checkWidget ();
	return ((NSOutlineView) view).usesAlternatingRowBackgroundColors ();
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
public TreeItem [] getSelection () {
	checkWidget ();
	NSOutlineView widget = (NSOutlineView) view;
	if (widget.numberOfSelectedRows () == 0) {
		return new TreeItem [0];
	}
	NSIndexSet selection = widget.selectedRowIndexes ();
	int count = (int)/*64*/selection.count ();
	int /*long*/ [] indexBuffer = new int /*long*/ [count];
	selection.getIndexes (indexBuffer, count, 0);
	TreeItem [] result = new TreeItem [count];
	for (int i=0; i<count; i++) {
		id id = widget.itemAtRow (indexBuffer [i]);
		Widget item = display.getWidget (id.id);
		if (item != null && item instanceof TreeItem) {
			result[i] = (TreeItem) item;
		}
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
	return (int)/*64*/((NSOutlineView) view).numberOfSelectedRows ();
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
	//TODO - partial item at the top
	NSRect rect = scrollView.documentVisibleRect ();
	NSPoint point = new NSPoint ();
	point.x = rect.x;
	point.y = rect.y;
	NSOutlineView outlineView = (NSOutlineView)view;
	int /*long*/ index = outlineView.rowAtPoint (point);
	if (index == -1) return null; /* empty */
	id item = outlineView.itemAtRow (index);
	return (TreeItem)display.getWidget (item.id);
}

void highlightSelectionInClipRect(int /*long*/ id, int /*long*/ sel, int /*long*/ rect) {
	if (!hooks (SWT.EraseItem)) {
		NSRect clipRect = new NSRect ();
		OS.memmove (clipRect, rect, NSRect.sizeof);
		callSuper (id, sel, clipRect);
	}
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
	if (item.parentItem != null) return -1;
	for (int i = 0; i < itemCount; i++) {
		if (item == items[i]) return i;
	}
	return -1;
}

boolean isTrim (NSView view) {
	if (super.isTrim (view)) return true;
	return view.id == headerView.id;
}

int /*long*/ menuForEvent(int /*long*/ id, int /*long*/ sel, int /*long*/ theEvent) {
	if (id != headerView.id) {
		/*
		 * Feature in Cocoa: Table views do not change the selection when the user
		 * right-clicks or control-clicks on an NSTableView or its subclasses. Fix is to select the 
		 * clicked-on row ourselves.
		 */
		NSEvent event = new NSEvent(theEvent);
		NSOutlineView tree = (NSOutlineView)view;
		
		// get the current selections for the outline view. 
		NSIndexSet selectedRowIndexes = tree.selectedRowIndexes();
		
		// select the row that was clicked before showing the menu for the event
		NSPoint mousePoint = view.convertPoint_fromView_(event.locationInWindow(), null);
		int /*long*/ row = tree.rowAtPoint(mousePoint);
		
		// figure out if the row that was just clicked on is currently selected
		if (selectedRowIndexes.containsIndex(row) == false) {
			NSIndexSet indexes = new NSIndexSet (NSIndexSet.indexSetWithIndex (row));
			tree.selectRowIndexes (indexes, false);
		}
		// else that row is currently selected, so don't change anything.
	}
	
	return super.menuForEvent(id, sel, theEvent);
}

int /*long*/ outlineView_child_ofItem (int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ index, int /*long*/ itemID) {
	TreeItem parent = (TreeItem) display.getWidget (itemID);
	TreeItem item = _getItem (parent, (int)/*64*/index, true);
	return item.handle.id;
}

void outlineView_didClickTableColumn (int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ tableColumn) {
	TreeColumn column = getColumn (new id (tableColumn));
	if (column == null) return; /* either CHECK column or firstColumn in 0-column Tree */
	column.postEvent (SWT.Selection);
}

int /*long*/ outlineView_objectValueForTableColumn_byItem (int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ tableColumn, int /*long*/ itemID) {
	TreeItem item = (TreeItem) display.getWidget (itemID);
	checkData (item);
	if (checkColumn != null && tableColumn == checkColumn.id) {
		NSNumber value;
		if (item.checked && item.grayed) {
			value = NSNumber.numberWithInt (OS.NSMixedState);
		} else {
			value = NSNumber.numberWithInt (item.checked ? OS.NSOnState : OS.NSOffState);
		}
		return value.id;
	}
	for (int i=0; i<columnCount; i++) {
		if (columns [i].nsColumn.id == tableColumn) {
			return item.createString (i).id;
		}
	}
	return item.createString (0).id;
}

boolean outlineView_isItemExpandable (int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ item) {
	if (item == 0) return true;
	return ((TreeItem) display.getWidget (item)).itemCount != 0;
}

int /*long*/ outlineView_numberOfChildrenOfItem (int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ item) {
	if (item == 0) return itemCount;
	return ((TreeItem) display.getWidget (item)).itemCount;
}

void outlineView_willDisplayCell_forTableColumn_item (int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ cell, int /*long*/ tableColumn, int /*long*/ itemID) {
	if (checkColumn != null && tableColumn == checkColumn.id) return;
	TreeItem item = (TreeItem) display.getWidget(itemID);
	int columnIndex = 0;
	for (int i=0; i<columnCount; i++) {
		if (columns [i].nsColumn.id == tableColumn) {
			columnIndex = i;
			break;
		}
	}
	Image image = item.getImage (columnIndex);
	NSBrowserCell browserCell = new NSBrowserCell (cell);
	browserCell.setImage (image != null ? image.handle : null);
}

void outlineViewColumnDidMove (int /*long*/ id, int /*long*/ sel, int /*long*/ aNotification) {
	NSNotification notification = new NSNotification (aNotification);
	NSDictionary userInfo = notification.userInfo ();
	id nsOldIndex = userInfo.valueForKey (NSString.stringWith ("NSOldColumn")); //$NON-NLS-1$
	id nsNewIndex = userInfo.valueForKey (NSString.stringWith ("NSNewColumn")); //$NON-NLS-1$
	int oldIndex = new NSNumber (nsOldIndex).intValue ();
	int newIndex = new NSNumber (nsNewIndex).intValue ();
	int startIndex = Math.min (oldIndex, newIndex);
	int endIndex = Math.max (oldIndex, newIndex);
	NSOutlineView outlineView = (NSOutlineView)view;
	NSArray nsColumns = outlineView.tableColumns ();
	for (int i = startIndex; i <= endIndex; i++) {
		id columnId = nsColumns.objectAtIndex (i);
		TreeColumn column = getColumn (columnId);
		if (column != null) {
			column.sendEvent (SWT.Move);
			if (isDisposed ()) return;
		}
	}
}

void outlineViewColumnDidResize (int /*long*/ id, int /*long*/ sel, int /*long*/ aNotification) {
	NSNotification notification = new NSNotification (aNotification);
	NSDictionary userInfo = notification.userInfo ();
	id columnId = userInfo.valueForKey (NSString.stringWith ("NSTableColumn")); //$NON-NLS-1$
	TreeColumn column = getColumn (columnId);
	if (column == null) return; /* either CHECK column or firstColumn in 0-column Tree */

	column.sendEvent (SWT.Resize);
	if (isDisposed ()) return;

	NSOutlineView outlineView = (NSOutlineView)view;
	int index = (int)/*64*/outlineView.columnWithIdentifier (columnId);
	if (index == -1) return; /* column was disposed in Resize callback */

	NSArray nsColumns = outlineView.tableColumns ();
	int columnCount = (int)/*64*/outlineView.numberOfColumns ();
	for (int i = index + 1; i < columnCount; i++) {
		columnId = nsColumns.objectAtIndex (i);
		column = getColumn (columnId);
		if (column != null) {
			column.sendEvent (SWT.Move);
			if (isDisposed ()) return;
		}
	}
}

void outlineViewItemDidExpand (int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
	NSNotification nsNotification = new NSNotification (notification);
	NSDictionary info = nsNotification.userInfo ();
	NSString key = NSString.stringWith ("NSObject"); //$NON-NLS-1$
	int /*long*/ itemHandle = info.objectForKey (key).id;
	TreeItem item = (TreeItem)display.getWidget (itemHandle);
	setScrollWidth (false, item.items, false);
}

void outlineViewSelectionDidChange (int /*long*/ id, int /*long*/ sel, int /*long*/ notification) {
	if (ignoreSelect) return;
	NSOutlineView widget = (NSOutlineView) view;
	int row = (int)/*64*/widget.selectedRow ();
	if (row == -1)
		postEvent (SWT.Selection);
	else {
		id _id = widget.itemAtRow (row);
		TreeItem item = (TreeItem) display.getWidget (_id.id);
		Event event = new Event ();
		event.item = item;
		event.index = row;
		postEvent (SWT.Selection, event);
	}
}

boolean outlineView_shouldCollapseItem (int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ itemID) {
	TreeItem item = (TreeItem) display.getWidget (itemID);
	if (!ignoreExpand) {
		Event event = new Event ();
		event.item = item;
		sendEvent (SWT.Collapse, event);
		if (isDisposed ()) return false;
		ignoreExpand = true;
		((NSOutlineView) view).collapseItem (item.handle);
		ignoreExpand = false;
		setScrollWidth ();
		return false;
	}
	return true;
}

boolean outlineView_shouldExpandItem (int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ itemID) {
	final TreeItem item = (TreeItem) display.getWidget (itemID);
	if (!ignoreExpand) {
		Event event = new Event ();
		event.item = item;
		sendEvent (SWT.Expand, event);
		if (isDisposed ()) return false;
		ignoreExpand = true;
		((NSOutlineView) view).expandItem (item.handle);
		ignoreExpand = false;
		return false;
	}
	return true;
}

void outlineView_setObjectValue_forTableColumn_byItem (int /*long*/ id, int /*long*/ sel, int /*long*/ outlineView, int /*long*/ object, int /*long*/ tableColumn, int /*long*/ itemID) {
	if (checkColumn != null && tableColumn == checkColumn.id)  {
		TreeItem item = (TreeItem) display.getWidget (itemID);
		item.checked = !item.checked;
		Event event = new Event ();
		event.detail = SWT.CHECK;
		event.item = item;
		postEvent (SWT.Selection, event);
		item.redraw (-1);
	}
}

void register () {
	super.register ();
	display.addWidget (headerView, this);
	display.addWidget (dataCell, this);
}

void releaseChildren (boolean destroy) {
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null && !item.isDisposed ()) {
			item.release (false);
		}
	}
	items = null;
	if (columns != null) {
		for (int i=0; i<columnCount; i++) {
			TreeColumn column = columns [i];
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
	if (headerView != null) headerView.release ();
	headerView = null;
	if (firstColumn != null) firstColumn.release ();
	firstColumn = null;
	if (checkColumn != null) checkColumn.release ();
	checkColumn = null;
	if (dataCell != null) dataCell.release ();
	dataCell = null;
}

void releaseWidget () {
	super.releaseWidget ();
	//release handle
	sortColumn = null;
}

void reloadItem (TreeItem item, boolean recurse) {
	if (drawCount == 0) {
		NSOutlineView widget = (NSOutlineView)view;
		TreeItem[] selectedItems = getSelection ();
		if (item != null) {
			widget.reloadItem (item.handle, recurse);
		} else {
			widget.reloadData ();
		}
		selectItems (selectedItems, true);
	} else {
		reloadPending = true;
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
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null && !item.isDisposed ()) item.release (false);
	}
	items = new TreeItem [4];
	itemCount = 0;
	((NSOutlineView) view).reloadData ();
	setScrollWidth ();
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
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection, listener);	
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
	if (eventTable == null) return;
	eventTable.unhook (SWT.Expand, listener);
	eventTable.unhook (SWT.Collapse, listener);
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
	if (item != null) {
		if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
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
	checkItems ();
	NSOutlineView widget = (NSOutlineView) view;
	ignoreSelect = true;
	widget.selectAll (null);
	ignoreSelect = false;
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
	checkItems ();
	showItem (item);
	NSOutlineView outlineView = (NSOutlineView) view;
	int /*long*/ row = outlineView.rowForItem (item.handle);
	NSIndexSet indexes = new NSIndexSet (NSIndexSet.indexSetWithIndex (row));
	outlineView.selectRowIndexes (indexes, false);
}

void sendDoubleSelection() {
	NSOutlineView outlineView = (NSOutlineView)view;
	int rowIndex = (int)/*64*/outlineView.clickedRow (); 
	if (rowIndex != -1) {
		if ((style & SWT.CHECK) != 0) {
			NSArray columns = outlineView.tableColumns ();
			int columnIndex = (int)/*64*/outlineView.clickedColumn ();
			id column = columns.objectAtIndex (columnIndex);
			if (column.id == checkColumn.id) return;
		}
		TreeItem item = (TreeItem) display.getWidget (outlineView.itemAtRow (rowIndex).id);
		Event event = new Event ();
		event.item = item;
		postEvent (SWT.DefaultSelection, event);
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
			postEvent (SWT.DefaultSelection);
			break;
		}
	}
	return result;
}

void selectItems (TreeItem[] items, boolean ignoreDisposed) {
	NSOutlineView outlineView = (NSOutlineView) view;
	NSMutableIndexSet rows = (NSMutableIndexSet) new NSMutableIndexSet ().alloc ().init ();
	rows.autorelease ();
	int length = items.length;
	for (int i=0; i<length; i++) {
		if (items [i] != null) {
			if (items [i].isDisposed ()) {
				if (ignoreDisposed) continue;
				error (SWT.ERROR_INVALID_ARGUMENT);
			}
			TreeItem item = items [i];
			if (!ignoreDisposed) showItem (items [i], false);
			rows.addIndex (outlineView.rowForItem (item.handle));
		}
	}
	ignoreSelect = true;
	outlineView.selectRowIndexes (rows, false);
	ignoreSelect = false;
}

void setBackground (float /*double*/ [] color) {
	super.setBackground (color);
	NSColor nsColor;
	if (color == null) {
		nsColor = null;
	} else {
		nsColor = NSColor.colorWithDeviceRed (color [0], color [1], color [2], 1);
	}
	((NSOutlineView) view).setBackgroundColor (nsColor);
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
		NSOutlineView outlineView = (NSOutlineView)view;
		int [] oldX = new int [oldOrder.length];
		int check = (style & SWT.CHECK) != 0 ? 1 : 0;
		for (int i=0; i<oldOrder.length; i++) {
			int index = oldOrder[i];
			oldX [index] = (int)outlineView.rectOfColumn (i + check).x;
		}
		int [] newX = new int [order.length];
		for (int i=0; i<order.length; i++) {
			int index = order [i];
			TreeColumn column = columns[index];
			int /*long*/ oldIndex = outlineView.columnWithIdentifier (column.nsColumn);
			int newIndex = i + check;
			outlineView.moveColumn (oldIndex, newIndex);
			newX [index] = (int)outlineView.rectOfColumn (newIndex).x;
		}

		TreeColumn[] newColumns = new TreeColumn [columnCount];
		System.arraycopy (columns, 0, newColumns, 0, columnCount);
		for (int i=0; i<columnCount; i++) {
			TreeColumn column = newColumns [i];
			if (!column.isDisposed ()) {
				if (newX [i] != oldX [i]) {
					column.sendEvent (SWT.Move);
				}
			}
		}
	}
}

void setFont (NSFont font) {
	super.setFont (font);
	setItemHeight (null, font, !hooks (SWT.MeasureItem));
	view.setNeedsDisplay (true);
	clearCachedWidth (items);
	setScrollWidth ();
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
public void setHeaderVisible (boolean show) {
	checkWidget ();
	((NSOutlineView) view).setHeaderView (show ? headerView : null);
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
	checkItems ();
	count = Math.max (0, count);
	setItemCount (null, count);
}

void setItemCount (TreeItem parentItem, int count) {
	int itemCount = getItemCount (parentItem);
	if (count == itemCount) return;
	NSOutlineView widget = (NSOutlineView) view;
	int length = Math.max (4, (count + 3) / 4 * 4);
	TreeItem [] children = parentItem == null ? items : parentItem.items;
	boolean expanded = parentItem == null || parentItem.getExpanded();
	if (count < itemCount) {
		/*
		* Note that the item count has to be updated before the call to reloadItem(), but
		* the items have to be released after.
		*/
		if (parentItem == null) {
			this.itemCount = count;
		} else {
			parentItem.itemCount = count;
		}
		/*
		* Bug in Cocoa.  When removing selected items from an NSOutlineView, the selection
		* is not properly updated.  The fix is to ensure that the item and its subitems
		* are deselected before the item is removed by the reloadItem call. 
		*/
		if (expanded) {
			for (int index = count; index < itemCount; index ++) {
				TreeItem item = children [index];
				if (item != null && !item.isDisposed ()) item.clearSelection ();
			}
		}
		TreeItem[] selectedItems = getSelection ();
		widget.reloadItem (parentItem != null ? parentItem.handle : null, expanded);
		selectItems (selectedItems, true);
		for (int index = count; index < itemCount; index ++) {
			TreeItem item = children [index];
			if (item != null && !item.isDisposed()) item.release (false);
		}
		TreeItem [] newItems = new TreeItem [length];
		if (children != null) {
			System.arraycopy (children, 0, newItems, 0, count);
		}
		children = newItems;
		if (parentItem == null) {
			this.items = newItems;
		} else {
			parentItem.items = newItems;
		}
	} else {
		if ((style & SWT.VIRTUAL) == 0) {
			for (int i=itemCount; i<count; i++) {
				new TreeItem (this, parentItem, SWT.NONE, i, true);
			}
		} else {
			TreeItem [] newItems = new TreeItem [length];
			if (children != null) {
				System.arraycopy (children, 0, newItems, 0, itemCount);
			}
			children = newItems;
			if (parentItem == null) {
				this.items = newItems;
				this.itemCount = count;
			} else {
				parentItem.items = newItems;
				parentItem.itemCount = count;
			}
			TreeItem[] selectedItems = getSelection ();
			widget.reloadItem (parentItem != null ? parentItem.handle : null, expanded);
			selectItems (selectedItems, true);
		}
	}
}

/*public*/ void setItemHeight (int itemHeight) {
	checkWidget ();
	if (itemHeight < -1) error (SWT.ERROR_INVALID_ARGUMENT);
	if (itemHeight == -1) {
		setItemHeight (null, null, true);
	} else {
		((NSOutlineView)view).setRowHeight (itemHeight);
	}
}

void setItemHeight (Image image, NSFont font, boolean set) {
	if (font == null) font = getFont ().handle;
	float /*double*/ ascent = font.ascender ();
	float /*double*/ descent = -font.descender () + font.leading ();
	int height = (int)Math.ceil (ascent + descent) + 1;
	Rectangle bounds = image != null ? image.getBounds () : imageBounds;
	if (bounds != null) {
		imageBounds = bounds;
		height = Math.max (height, bounds.height);
	}
	NSTableView widget = (NSTableView)view;
	if (set || widget.rowHeight () < height) {
		widget.setRowHeight (height);
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
 * 
 * @since 3.1
 */
public void setLinesVisible (boolean show) {
	checkWidget ();
	((NSOutlineView) view).setUsesAlternatingRowBackgroundColors (show);
}

public void setRedraw (boolean redraw) {
	checkWidget ();
	super.setRedraw (redraw);
	if (redraw && drawCount == 0) {
		checkItems ();
		setScrollWidth ();
	}
}

boolean setScrollWidth () {
	return setScrollWidth (true, items, true);
}

boolean setScrollWidth (boolean set, TreeItem[] items, boolean recurse) {
	if (items == null) return false;
	if (ignoreRedraw || drawCount != 0) return false;
	if (columnCount != 0) return false;
	GC gc = new GC (this);
	int newWidth = calculateWidth (items, 0, gc, recurse);
	gc.dispose ();
	if (!set) {
		int oldWidth = (int)firstColumn.width ();
		if (oldWidth >= newWidth) return false;
	}
	firstColumn.setWidth (newWidth);
	return true;
}

boolean setScrollWidth (TreeItem item) {
	if (ignoreRedraw || drawCount != 0) return false;
	if (columnCount != 0) return false;
	TreeItem parentItem = item.parentItem;
	if (parentItem != null && !parentItem.getExpanded ()) return false;
	GC gc = new GC (this);
	int newWidth = item.calculateWidth (0, gc);
	gc.dispose ();
	int oldWidth = (int)firstColumn.width ();
	if (oldWidth < newWidth) {
		firstColumn.setWidth (newWidth);
		return true;
	}
	return false;
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
	setSelection (new TreeItem [] {item});
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
public void setSelection (TreeItem [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	checkItems ();
	deselectAll ();
	int length = items.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	selectItems (items, false);
	if (items.length > 0) {
		for (int i = 0; i < items.length; i++) {
			TreeItem item = items[i];
			if (item != null) {
				showItem(item, true);			
				break;
			}
		}
	}
}

void setSmallSize () {
	if (checkColumn == null) return;
	checkColumn.dataCell ().setControlSize (OS.NSSmallControlSize);
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
	sortColumn = column;
	((NSOutlineView)view).setHighlightedTableColumn (column == null ? null : column.nsColumn);
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
	if (sortColumn == null) return;
	NSTableHeaderView headerView = ((NSOutlineView)view).headerView ();
	if (headerView == null) return;
	int /*long*/ index = ((NSOutlineView)view).columnWithIdentifier (sortColumn.nsColumn);
	NSRect rect = headerView.headerRectOfColumn (index);
	headerView.setNeedsDisplayInRect (rect);
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
	checkWidget();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	checkItems ();
	showItem (item, false);
	NSOutlineView outlineView = (NSOutlineView) view;
	//FIXME
	((NSOutlineView) view).scrollRowToVisible (outlineView.rowForItem (item.handle));
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
	if (column.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (column.parent != this) return;
	if (columnCount <= 1) return;
	int index = (int)/*64*/((NSOutlineView)view).columnWithIdentifier (column.nsColumn);
	if (!(0 <= index && index < columnCount + ((style & SWT.CHECK) != 0 ? 1 : 0))) return;
	((NSOutlineView)view).scrollColumnToVisible (index);
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
	checkItems ();
	showItem (item, true);
}

void showItem (TreeItem item, boolean scroll) {
	TreeItem parentItem = item.parentItem;
	if (parentItem != null) {
		showItem (parentItem, false);
		parentItem.setExpanded (true);
	}
	if (scroll) {
		NSOutlineView outlineView = (NSOutlineView) view;
		outlineView.scrollRowToVisible (outlineView.rowForItem (item.handle));
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
	checkItems ();
	//TODO - optimize
	TreeItem [] selection = getSelection ();
	if (selection.length > 0) showItem (selection [0], true);
}

void updateCursorRects (boolean enabled) {
	super.updateCursorRects (enabled);
	if (headerView == null) return;
	updateCursorRects (enabled, headerView);
}

}

