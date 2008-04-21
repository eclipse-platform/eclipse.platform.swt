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
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, VIRTUAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, Collapse, Expand, SetData, MeasureItem, EraseItem, PaintItem</dd>
 * </dl>
 * </p><p>
 * Note: Only one of the styles SINGLE and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Tree extends Composite {
	NSTableColumn firstColumn, checkColumn;
	NSTableHeaderView headerView;
	TreeItem [] items;
	int itemCount;
	TreeColumn [] columns;
	TreeColumn sortColumn;
	int columnCount;
	int sortDirection;
	boolean ignoreExpand, ignoreSelect;

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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Tree (Composite parent, int style) {
	super (parent, checkStyle (style));
}

void _addListener (int eventType, Listener listener) {
	super._addListener (eventType, listener);
	for (int i = 0; i < items.length; i++) {
		if (items [i] != null) items [i].width = -1;		
	}
}

TreeItem _getItem (TreeItem parentItem, int index) {
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
	if (item != null || (style & SWT.VIRTUAL) == 0) return item;
	item = new TreeItem (this, parentItem, SWT.NONE, index, false);
	items [index] = item;
	return item;
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

boolean checkData (TreeItem item, boolean redraw) {
	if (item.cached) return true;
	if ((style & SWT.VIRTUAL) != 0) {
		item.cached = true;
		Event event = new Event ();
		TreeItem parentItem = item.getParentItem ();
		event.item = item;
		event.index = parentItem == null ? indexOf (item) : parentItem.indexOf (item);
//		ignoreRedraw = true;
		sendEvent (SWT.SetData, event);
		//widget could be disposed at this point
//		ignoreRedraw = false;
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

void clear (TreeItem parentItem, int index, boolean all) {
//	int [] ids = parentItem == null ? childIds : parentItem.childIds;
//	TreeItem item = _getItem (ids [index], false);
//	if (item != null) {
//		item.clear();
//		if (all) {
//			clearAll (item, true);
//		} else {
//			int container = parentItem == null ? OS.kDataBrowserNoItem : parentItem.id;
//			OS.UpdateDataBrowserItems (handle, container, 1, new int[] {item.id}, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
//		}
//	}
}

void clearAll (TreeItem parentItem, boolean all) {
//	boolean update = !inClearAll;
//	int count = getItemCount (parentItem);
//	if (count == 0) return;
//	inClearAll = true;
//	int [] ids = parentItem == null ? childIds : parentItem.childIds;
//	for (int i=0; i<count; i++) {
//		TreeItem item = _getItem (ids [i], false);
//		if (item != null) {
//			item.clear ();
//			if (all) clearAll (item, true);
//		}
//	}
//	if (update) {
//		OS.UpdateDataBrowserItems (handle, 0, 0, null, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
//		inClearAll = false;
//	}
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

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	if (wHint == SWT.DEFAULT) {
		if (columnCount != 0) {
			for (int i=0; i<columnCount; i++) {
				width += columns [i].getWidth ();
			}
		} else {
//			int levelIndent = DISCLOSURE_COLUMN_LEVEL_INDENT;
//			if (OS.VERSION >= 0x1040) {
//				float [] metric = new float [1];
//				OS.DataBrowserGetMetric (handle, OS.kDataBrowserMetricDisclosureColumnPerDepthGap, null, metric);
//				levelIndent = (int) metric [0];
//			}
//			GC gc = new GC (this);
//			width = calculateWidth (childIds, gc, true, 0, levelIndent);
//			gc.dispose ();
//			width += getInsetWidth (column_id, true);
		}
		if ((style & SWT.CHECK) != 0) width += getCheckColumnWidth ();
	} else {
		width = wHint;
	}
	if (hHint == SWT.DEFAULT) {
		height = ((NSTableView)view).numberOfRows() * getItemHeight () + getHeaderHeight();
	} else {
		height = hHint;
	}
	if (width <= 0) width = DEFAULT_WIDTH;
	if (height <= 0) height = DEFAULT_HEIGHT;
	Rectangle rect = computeTrim (0, 0, width, height);
	return new Point (rect.width, rect.height);
}

void createHandle () {
	SWTScrollView scrollWidget = (SWTScrollView)new SWTScrollView().alloc();
	scrollWidget.initWithFrame(new NSRect ());
	scrollWidget.setHasHorizontalScroller(true);
	scrollWidget.setHasVerticalScroller(true);
	scrollWidget.setAutohidesScrollers(true);
	scrollWidget.setBorderType(hasBorder() ? OS.NSBezelBorder : OS.NSNoBorder);
	scrollWidget.setTag(jniRef);
	
	NSOutlineView widget = (NSOutlineView)new SWTOutlineView().alloc();
	widget.initWithFrame(new NSRect());
	widget.setAllowsMultipleSelection((style & SWT.MULTI) != 0);
	widget.setAutoresizesOutlineColumn(false);
	widget.setAutosaveExpandedItems(true);
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
		widget.setOutlineTableColumn(checkColumn);
		NSButtonCell cell = (NSButtonCell)new NSButtonCell().alloc().init();
		checkColumn.setDataCell(cell);
		cell.setButtonType(OS.NSSwitchButton);
		cell.setImagePosition(OS.NSImageOnly);
		cell.setAllowsMixedState(true);
		checkColumn.setWidth(getCheckColumnWidth());
		checkColumn.setResizingMask(OS.NSTableColumnNoResizing);
		checkColumn.setEditable(false);
		cell.release();
	}
	
	firstColumn = (NSTableColumn)new NSTableColumn().alloc();
	firstColumn.initWithIdentifier(str);
	firstColumn.headerCell().setTitle(str);
	widget.addTableColumn (firstColumn);
	widget.setOutlineTableColumn(firstColumn);
	NSBrowserCell cell = (NSBrowserCell)new NSBrowserCell().alloc().init();
	cell.setLeaf(true);
	firstColumn.setDataCell(cell);
	cell.release();
	
	scrollView = scrollWidget;
	view = widget;
	scrollView.setDocumentView(widget);
	parent.contentView().addSubview_(scrollView);
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
		firstColumn = null;
	} else {
		//TODO - set attributes, alignment etc.
		NSString str = NSString.stringWith("");
		nsColumn = (NSTableColumn)new NSTableColumn().alloc();
		nsColumn.initWithIdentifier(str);
		nsColumn.headerCell().setTitle(str);
		((NSTableView)view).addTableColumn (nsColumn);
		int checkColumn = (style & SWT.CHECK) != 0 ? 1 : 0;
		((NSTableView)view).moveColumn (columnCount + checkColumn, index + checkColumn);
		NSBrowserCell cell = (NSBrowserCell)new NSBrowserCell().alloc().init();
		cell.setLeaf(true);
		nsColumn.setDataCell(cell);
		cell.release();
	}
	column.nsColumn = nsColumn;
	nsColumn.headerCell().setTitle(NSString.stringWith(""));
	nsColumn.setWidth(0);
	System.arraycopy (columns, index, columns, index + 1, columnCount++ - index);
	columns [index] = column;
	if (columnCount > 1) {
		for (int i=0; i<items.length; i++) {
			TreeItem item = items [i];
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

void createItem (TreeItem item, TreeItem parentItem, int index) {
	int count;
	TreeItem[] items;
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
	item.items = new TreeItem[4];
	item.createJNIRef();
	SWTTreeItem handle = (SWTTreeItem)new SWTTreeItem().alloc().init();
	handle.setTag(item.jniRef);
	item.handle = handle;
	if (parentItem != null) {
		parentItem.itemCount = count;
	} else {
		this.itemCount = count;
	}
	//TODO ?
	((NSTableView)view).reloadData();
}

void createWidget () {
	super.createWidget ();
	items = new TreeItem [4];
	columns = new TreeColumn [4];
}

Color defaultBackground () {
	return display.getSystemColor (SWT.COLOR_LIST_BACKGROUND);
}

Color defaultForeground () {
	return display.getSystemColor (SWT.COLOR_LIST_FOREGROUND);
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

public void deselect (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
//	ignoreSelect = true;
//	/*
//	* Bug in the Macintosh.  When the DataBroswer selection flags includes
//	* both kDataBrowserNeverEmptySelectionSet and kDataBrowserSelectOnlyOne,
//	* two items are selected when SetDataBrowserSelectedItems() is called
//	* with kDataBrowserItemsAssign to assign a new seletion despite the fact
//	* that kDataBrowserSelectOnlyOne was specified.  The fix is to save and
//	* restore kDataBrowserNeverEmptySelectionSet around each call to
//	* SetDataBrowserSelectedItems().
//	*/
//	int [] selectionFlags = null;
//	if ((style & SWT.SINGLE) != 0) {
//		selectionFlags = new int [1];
//		OS.GetDataBrowserSelectionFlags (handle, selectionFlags);
//		OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0] & ~OS.kDataBrowserNeverEmptySelectionSet);
//	}
//	OS.SetDataBrowserSelectedItems (handle, 1, new int [] {item.id}, OS.kDataBrowserItemsRemove);
//	if ((style & SWT.SINGLE) != 0) {
//		OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0]);
//	}
//	ignoreSelect = false;
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
//	if (columnCount == 1) {
//		column_id = column.id; idCount = 0;
//		DataBrowserListViewHeaderDesc desc = new DataBrowserListViewHeaderDesc ();
//		desc.version = OS.kDataBrowserListViewLatestHeaderDesc;
//		short [] width = new short [1];
//		OS.GetDataBrowserTableViewNamedColumnWidth (handle, column_id, width);
//		desc.minimumWidth = desc.maximumWidth = width [0];
//		int str = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, null, 0);
//		desc.titleString = str;
//		OS.SetDataBrowserListViewHeaderDesc (handle, column_id, desc);
//		OS.CFRelease (str);
//	} else {
//		int [] disclosure = new int [1];
//		boolean [] expandableRows = new boolean [1];
//		OS.GetDataBrowserListViewDisclosureColumn (handle, disclosure, expandableRows);
//		if (disclosure [0] == column.id) {
//			TreeColumn firstColumn = columns [1];
//			firstColumn.style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
//			firstColumn.style |= SWT.LEFT;
//			firstColumn.updateHeader();
//			OS.SetDataBrowserListViewDisclosureColumn (handle, firstColumn.id, expandableRows [0]);
//		}
//		if (OS.RemoveDataBrowserTableViewColumn (handle, column.id) != OS.noErr) {
//			error (SWT.ERROR_ITEM_NOT_REMOVED);
//		}
//	}
	System.arraycopy (columns, index + 1, columns, index, --columnCount - index);
	columns [columnCount] = null;
	for (int i=index; i<columnCount; i++) {
		columns [i].sendEvent (SWT.Move);
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
//	if (index != itemCount - 1) fixSelection (index, false); 
	System.arraycopy (items, index + 1, items, index, --count - index);
	items [count] = null;
	if (parentItem != null) {
		parentItem.itemCount = count;
		((NSOutlineView)view).reloadItem_reloadChildren_(parentItem.handle, true);
	} else {
		this.itemCount = count;
		((NSOutlineView)view).reloadItem_(null);
	}
	
	//noteNumberOfRowsChanged was causing crashes whenever
	//a TreeItem was disposed. 
	//Using reloadItem avoids the crashes.
	//Not sure that this NSTableView function 
	//makes sense in an NSOutlineView.
	
	//((NSTableView)view).noteNumberOfRowsChanged();
	
//	setScrollWidth (true);
//	fixScrollBar ();
}


void fixScrollBar () {
	/*
	* Bug in the Macintosh. For some reason, the data browser does not update
	* the vertical scrollbar when it is scrolled to the bottom and items are
	* removed.  The fix is to check if the scrollbar value is bigger the
	* maximum number of visible items and clamp it when needed.
	*/
//	int [] top = new int [1], left = new int [1];
//	OS.GetDataBrowserScrollPosition (handle, top, left);
//	int maximum = Math.max (0, getItemHeight () * visibleCount - getClientArea ().height);
//	if (top [0] > maximum) {
//		OS.SetDataBrowserScrollPosition (handle, maximum, left [0]);
//	}
}

int getCheckColumnWidth () {
	return 20; //TODO - compute width
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
	int [] position = new int [1];
	for (int i=0; i<columnCount; i++) {
		TreeColumn column = columns [i];
//		OS.GetDataBrowserTableViewColumnPosition (handle, column.id, position);
//		if ((style & SWT.CHECK) != 0) position [0] -= 1;
		order [position [0]] = i;
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
 * 
 * @since 3.1
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
 * 
 * @since 3.1
 */
public TreeItem getItem (int index) {
	checkWidget ();
	int count = getItemCount ();
	if (index < 0 || index >= count) error (SWT.ERROR_INVALID_RANGE);
	return _getItem (null, index);
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
//	Rect rect = new Rect ();
//	org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
//	OS.SetPt (pt, (short) point.x, (short) point.y);
//	if (0 < lastHittest && lastHittest <= items.length && lastHittestColumn != 0) {
//		TreeItem item = _getItem (lastHittest, false);
//		if (item != null) {
//			if (OS.GetDataBrowserItemPartBounds (handle, item.id, lastHittestColumn, OS.kDataBrowserPropertyDisclosurePart, rect) == OS.noErr) {
//				if (OS.PtInRect (pt, rect)) return null;
//			}
//			if (OS.GetDataBrowserItemPartBounds (handle, item.id, lastHittestColumn, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
//				if (rect.top <= pt.v && pt.v <= rect.bottom) {
//					if ((style & SWT.FULL_SELECTION) != 0) {
//						return item;
//					} else {
//						return OS.PtInRect (pt, rect) ? item : null;
//					}
//				}
//			}
//		}
//	}
//	//TODO - optimize
//	for (int i=0; i<items.length; i++) {
//		TreeItem item = items [i];
//		if (item != null) {
//			if (OS.GetDataBrowserItemPartBounds (handle, item.id, column_id, OS.kDataBrowserPropertyDisclosurePart, rect) == OS.noErr) {
//				if (OS.PtInRect (pt, rect)) return null;
//			}
//			if (columnCount == 0) {
//				if (OS.GetDataBrowserItemPartBounds (handle, item.id, column_id, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
//					if (rect.top <= pt.v && pt.v <= rect.bottom) {
//						if ((style & SWT.FULL_SELECTION) != 0) {
//							return item;
//						} else {
//							return OS.PtInRect (pt, rect) ? item : null;
//						}
//					}
//				}
//			} else {
//				for (int j = 0; j < columnCount; j++) {
//					if (OS.GetDataBrowserItemPartBounds (handle, item.id, columns [j].id, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
//						if (rect.top <= pt.v && pt.v <= rect.bottom) {
//							if ((style & SWT.FULL_SELECTION) != 0) {
//								return item;
//							} else {
//								return OS.PtInRect (pt, rect) ? item : null;
//							}
//						}
//					}
//				}
//			}
//		}
//	}
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
	return (int)((NSTableView)view).rowHeight();
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
		result [i] = _getItem (null, i);
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
//	if (OS.VERSION >= 0x1040) {
//		int [] attrib = new int [1];
//		OS.DataBrowserGetAttributes (handle, attrib);
//		return (attrib [0] & (OS.kDataBrowserAttributeListViewAlternatingRowColors | OS.kDataBrowserAttributeListViewDrawColumnDividers)) != 0;
//	}
	return false;
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
	NSOutlineView widget = (NSOutlineView)view;
	if (widget.numberOfSelectedRows() == 0) {
		return new TreeItem [0];
	}
	NSIndexSet selection = widget.selectedRowIndexes();
	int count = selection.count();
	int [] indexBuffer = new int [count];
	selection.getIndexes(indexBuffer, count, 0);
	TreeItem [] result = new TreeItem  [count];
	for (int i=0; i<count; i++) {
		id item = widget.itemAtRow(indexBuffer [i]);
		int jniRef = OS.objc_msgSend(item.id, OS.sel_tag);
		if (jniRef != -1 && jniRef != 0) {
			//TODO virtual
			result[i] = (TreeItem)OS.JNIGetObject(jniRef);
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
	return ((NSTableView)view).numberOfSelectedRows();
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
	checkWidget();
//	//TODO - optimize
//	Rect rect = new Rect ();
//	int y = getBorder () + getHeaderHeight ();
//	for (int i=0; i<items.length; i++) {
//		TreeItem item = items [i];
//		if (item != null) {
//			int columnId = (columnCount == 0) ? column_id : columns [0].id;
//			if (OS.GetDataBrowserItemPartBounds (handle, item.id, columnId, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
//				if (rect.top <= y && y <= rect.bottom) return item;
//			}
//		}
//	}
	return null;
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

int outlineView_child_ofItem(int outlineView, int index, int ref) {
	TreeItem parent = null;
	if (ref != 0) parent = (TreeItem)OS.JNIGetObject(OS.objc_msgSend(ref, OS.sel_tag));
	TreeItem item = _getItem(parent, index);
	return item.handle.id;
}

int outlineView_objectValueForTableColumn_byItem(int outlineView, int tableColumn, int ref) {
	TreeItem item = (TreeItem)OS.JNIGetObject(OS.objc_msgSend(ref, OS.sel_tag));
	if (checkColumn != null && tableColumn == checkColumn.id) {
		NSNumber value;
		if (item.checked && item.grayed) {
			value = NSNumber.numberWithInt(OS.NSMixedState);
		} else {
			value = NSNumber.numberWithInt(item.checked ? OS.NSOnState : OS.NSOffState);
		}
		return value.id;
	}
	for (int i=0; i<columnCount; i++) {
		if (columns [i].nsColumn.id == tableColumn) {
			return item.createString(i).id;
		}
	}
	return item.createString(0).id;
}

boolean outlineView_isItemExpandable(int outlineView, int ref) {
	if (ref == 0) return true;
	return ((TreeItem)OS.JNIGetObject(OS.objc_msgSend(ref, OS.sel_tag))).itemCount != 0;
}

int outlineView_numberOfChildrenOfItem(int outlineView, int ref) {
	if (ref == 0) return itemCount;
	return ((TreeItem)OS.JNIGetObject(OS.objc_msgSend(ref, OS.sel_tag))).itemCount;
}

void outlineView_willDisplayCell_forTableColumn_item(int outlineView, int cell, int tableColumn, int ref) {
	if (checkColumn != null && tableColumn == checkColumn.id) return;
	TreeItem item = (TreeItem)OS.JNIGetObject(OS.objc_msgSend(ref, OS.sel_tag));
	Image image = item.image;
	for (int i=0; i<columnCount; i++) {
		if (columns [i].nsColumn.id == tableColumn) {
			image = item.getImage(i);
		}
	}
	NSBrowserCell browserCell = new NSBrowserCell(cell);
	browserCell.setImage(image != null ? image.handle : null);
}

void outlineViewSelectionDidChange(int notification) {
	if (ignoreSelect) return;
	NSOutlineView widget = (NSOutlineView)view;
	int row = widget.selectedRow();
	if(row == -1)
		postEvent(SWT.Selection);
	else {
		id _id = widget.itemAtRow(row);
		TreeItem item = (TreeItem)OS.JNIGetObject(OS.objc_msgSend(_id.id, OS.sel_tag));
		Event event = new Event();
		event.item = item;
		event.index = row;
		postEvent(SWT.Selection, event);
	}
}

boolean outlineView_shouldCollapseItem(int outlineView, int ref) {
	if (!ignoreExpand) {
		TreeItem item = (TreeItem)OS.JNIGetObject(OS.objc_msgSend(ref, OS.sel_tag));
		Event event = new Event();
		event.item = item;
		sendEvent(SWT.Collapse, event);
		item.expanded = false;
	}
	return true;
}

boolean outlineView_shouldExpandItem(int outlineView, int ref) {
	if (!ignoreExpand) {
		TreeItem item = (TreeItem)OS.JNIGetObject(OS.objc_msgSend(ref, OS.sel_tag));
		Event event = new Event();
		event.item = item;
		sendEvent(SWT.Expand, event);
		item.expanded = true;
	}
	return true;
}

void outlineView_setObjectValue_forTableColumn_byItem(int outlineView, int object, int tableColumn, int ref) {
	TreeItem item = (TreeItem)OS.JNIGetObject(OS.objc_msgSend(ref, OS.sel_tag));
	if (checkColumn != null && tableColumn == checkColumn.id)  {
		item.checked = !item.checked;
		Event event = new Event();
		event.detail = SWT.CHECK;
		event.item = item;
		postEvent(SWT.Selection, event);
	}
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
	if (headerView != null) headerView.release();
	headerView = null;
	if (firstColumn != null) firstColumn.release();
	firstColumn = null;
	if (checkColumn != null) checkColumn.release();
	checkColumn = null;
}

void releaseWidget () {
	super.releaseWidget ();
	//release handle
	sortColumn = null;
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
	((NSOutlineView)view).reloadItem_(null);
	//((NSTableView)view).noteNumberOfRowsChanged();
//	setScrollWidth (true);
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
public void removeTreeListener(TreeListener listener) {
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
		if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
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
	NSTableView widget = (NSTableView)view;
	ignoreSelect = true;
	widget.selectAll(null);
	ignoreSelect = false;
}

public void select (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
//	showItem (item, false);
//	ignoreSelect = true;
//	/*
//	* Bug in the Macintosh.  When the DataBroswer selection flags includes
//	* both kDataBrowserNeverEmptySelectionSet and kDataBrowserSelectOnlyOne,
//    * two items are selected when SetDataBrowserSelectedItems() is called
//    * with kDataBrowserItemsAssign to assign a new seletion despite the fact
//	* that kDataBrowserSelectOnlyOne was specified.  The fix is to save and
//	* restore kDataBrowserNeverEmptySelectionSet around each call to
//	* SetDataBrowserSelectedItems().
//	*/
//	int [] selectionFlags = null;
//	if ((style & SWT.SINGLE) != 0) {
//		selectionFlags = new int [1];
//		OS.GetDataBrowserSelectionFlags (handle, selectionFlags);
//		OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0] & ~OS.kDataBrowserNeverEmptySelectionSet);
//	}
//	OS.SetDataBrowserSelectedItems (handle, 1, new int [] {item.id}, OS.kDataBrowserItemsAssign);
//	if ((style & SWT.SINGLE) != 0) {
//		OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0]);
//	}
//	ignoreSelect = false;
}

void sendDoubleSelection() {
	postEvent (SWT.DefaultSelection);
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
		int [] disclosure = new int [1];
		boolean [] expandableRows = new boolean [1];
//		OS.GetDataBrowserListViewDisclosureColumn (handle, disclosure, expandableRows);
		TreeColumn firstColumn = columns [order [0]];
//		if (disclosure [0] != firstColumn.id) {
//			OS.SetDataBrowserListViewDisclosureColumn (handle, firstColumn.id, expandableRows [0]);
//		}
		int x = 0;
		short [] width = new short [1];
		int [] oldX = new int [oldOrder.length];
		for (int i=0; i<oldOrder.length; i++) {
			int index = oldOrder [i];
			TreeColumn column = columns [index];
			oldX [index] =  x;
//			OS.GetDataBrowserTableViewNamedColumnWidth(handle, column.id, width);
			x += width [0];
		}
		x = 0;
		int [] newX = new int [order.length];
		for (int i=0; i<order.length; i++) {
			int index = order [i];
			TreeColumn column = columns [index];
			int position = (style & SWT.CHECK) != 0 ? i + 1 : i;
//			OS.SetDataBrowserTableViewColumnPosition(handle, column.id, position);
//			column.lastPosition = position;
			newX [index] =  x;
//			OS.GetDataBrowserTableViewNamedColumnWidth(handle, column.id, width);
			x += width [0];
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
	((NSTableView)view).setHeaderView (show ? headerView : null);
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
	setItemCount (null, count);
}

void setItemCount (TreeItem parentItem, int count) {
//	int itemCount = getItemCount (parentItem);
//	if (count == itemCount) return;
//	setRedraw (false);
//	int [] top = new int [1], left = new int [1];
//    OS.GetDataBrowserScrollPosition (handle, top, left);
//    DataBrowserCallbacks callbacks = new DataBrowserCallbacks ();
//	OS.GetDataBrowserCallbacks (handle, callbacks);
//	callbacks.v1_itemNotificationCallback = 0;
//	callbacks.v1_itemCompareCallback = 0;
//	OS.SetDataBrowserCallbacks (handle, callbacks);
//	int[] ids = parentItem == null ? childIds : parentItem.childIds;
//	if (count < itemCount) {
//		for (int index = ids.length - 1; index >= count; index--) {
//			int id = ids [index];
//			if (id != 0) {
//				TreeItem item = _getItem (id, false);
//				if (item != null && !item.isDisposed ()) {
//					item.dispose ();
//				} else {
//					if (parentItem == null || parentItem.getExpanded ()) {
//						if (OS.RemoveDataBrowserItems (handle, OS.kDataBrowserNoItem, 1, new int [] {id}, 0) != OS.noErr) {
//							error (SWT.ERROR_ITEM_NOT_REMOVED);
//							break;
//						}
//						visibleCount--;
//					}
//				}
//			}
//		}
//		//TODO - move shrink to paint event
//		// shrink items array
//		int lastIndex = items.length;
//		for (int i=items.length; i>0; i--) {
//			if (items [i-1] != null) {
//				lastIndex = i;
//				break;
//			}
//		}
//		if (lastIndex < items.length - 4) {
//			int length = Math.max (4, (lastIndex + 3) / 4 * 4);
//			TreeItem [] newItems = new TreeItem [length];
//			System.arraycopy(items, 0, newItems, 0, Math.min(items.length, lastIndex));
//		}
//	}
//	
//	if (parentItem != null) parentItem.itemCount = count;
//	int length = Math.max (4, (count + 3) / 4 * 4);
//	int [] newIds = new int [length];
//	if (ids != null) {
//		System.arraycopy (ids, 0, newIds, 0, Math.min (count, itemCount));
//	}
//	ids = newIds;
//	if (parentItem == null) {
//		childIds = newIds;
//	} else {
//		parentItem.childIds = newIds;
//	}
//	
//	if (count > itemCount) {
//		if ((getStyle() & SWT.VIRTUAL) == 0) {
//			int delta = Math.max (4, (count - itemCount + 3) / 4 * 4);
//			TreeItem [] newItems = new TreeItem [items.length + delta];
//			System.arraycopy (items, 0, newItems, 0, items.length);
//			items = newItems;
//			for (int i=itemCount; i<count; i++) {
//				items [i] = new TreeItem (this, parentItem, SWT.NONE, i, true);
//			}
//		} else {
//			if (parentItem == null || parentItem.getExpanded ()) {
//				int parentID = parentItem == null ? OS.kDataBrowserNoItem : parentItem.id;
//				int [] addIds = _getIds (count - itemCount);
//				if (OS.AddDataBrowserItems (handle, parentID, addIds.length, addIds, OS.kDataBrowserItemNoProperty) != OS.noErr) {
//					error (SWT.ERROR_ITEM_NOT_ADDED);
//				}
//				visibleCount += (count - itemCount);
//				System.arraycopy (addIds, 0, ids, itemCount, addIds.length);
//			}
//		}
//	}
//	
//	callbacks.v1_itemNotificationCallback = display.itemNotificationProc;
//	callbacks.v1_itemCompareCallback = display.itemCompareProc;
//	OS.SetDataBrowserCallbacks (handle, callbacks);
//	setRedraw (true);
//	if (itemCount == 0 && parentItem != null) parentItem.redraw (OS.kDataBrowserNoItem);
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

void setItemHeight (Image image) {
//	Rectangle bounds = image != null ? image.getBounds () : imageBounds;
//	if (bounds == null) return;
//	imageBounds = bounds;
//	short [] height = new short [1];
//	if (OS.GetDataBrowserTableViewRowHeight (handle, height) == OS.noErr) {
//		if (height [0] < bounds.height) {
//			OS.SetDataBrowserTableViewRowHeight (handle, (short) bounds.height);
//		}
//	}
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
	((NSTableView)view).setUsesAlternatingRowBackgroundColors(show);
}

public void setRedraw (boolean redraw) {
	checkWidget();
	super.setRedraw (redraw);
	if (redraw && drawCount == 0) {
		setScrollWidth (true);
	}
}

boolean setScrollWidth (TreeItem item) {
//	if (ignoreRedraw || drawCount != 0) return false;
	if (columnCount != 0) return false;
//	TreeItem parentItem = item.parentItem;
//	if (parentItem != null && !parentItem._getExpanded ()) return false;
//	GC gc = new GC (this);
//	int newWidth = item.calculateWidth (0, gc);
//	gc.dispose ();
//	newWidth += getInsetWidth (column_id, false);
//	short [] width = new short [1];
//	OS.GetDataBrowserTableViewNamedColumnWidth (handle, column_id, width);
//	if (width [0] < newWidth) {
//		OS.SetDataBrowserTableViewNamedColumnWidth (handle, column_id, (short) newWidth);
//		return true;
//	}
//	firstColumn.setWidth(400);
	return false;
}

boolean setScrollWidth (boolean set) {
//	return setScrollWidth(set, childIds, true);
	return false;
}

boolean setScrollWidth (boolean set, int[] childIds, boolean recurse) {
//	if (ignoreRedraw || drawCount != 0) return false;
//	if (columnCount != 0 || childIds == null) return false;
//	GC gc = new GC (this);
//	int newWidth = calculateWidth (childIds, gc, recurse, 0, 0);
//	gc.dispose ();
//	newWidth += getInsetWidth (column_id, false);
//	if (!set) {
//		short [] width = new short [1];
//		OS.GetDataBrowserTableViewNamedColumnWidth (handle, column_id, width);
//		if (width [0] >= newWidth) return false;
//	}
//	OS.SetDataBrowserTableViewNamedColumnWidth (handle, column_id, (short) newWidth);
	return true;
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
	deselectAll ();
//	int length = items.length;
//	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
//	int count = 0;
//	int[] ids = new int [length];
//	for (int i=0; i<length; i++) {
//		if (items [i] != null) {
//			if (items [i].isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
//			ids [count++] = items [i].id;
//			showItem (items [i], false);
//		}
//	}
//	ignoreSelect = true;
//	/*
//	* Bug in the Macintosh.  When the DataBroswer selection flags includes
//	* both kDataBrowserNeverEmptySelectionSet and kDataBrowserSelectOnlyOne,
//    * two items are selected when SetDataBrowserSelectedItems() is called
//    * with kDataBrowserItemsAssign to assign a new seletion despite the fact
//	* that kDataBrowserSelectOnlyOne was specified.  The fix is to save and
//	* restore kDataBrowserNeverEmptySelectionSet around each call to
//	* SetDataBrowserSelectedItems().
//	*/
//	int [] selectionFlags = null;
//	if ((style & SWT.SINGLE) != 0) {
//		selectionFlags = new int [1];
//		OS.GetDataBrowserSelectionFlags (handle, selectionFlags);
//		OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0] & ~OS.kDataBrowserNeverEmptySelectionSet);
//	}
//	OS.SetDataBrowserSelectedItems (handle, count, ids, OS.kDataBrowserItemsAssign);
//	if ((style & SWT.SINGLE) != 0) {
//		OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0]);
//	}
//	ignoreSelect = false;
//	if (length > 0) {
//		int index = -1;
//		for (int i=0; i<items.length; i++) {
//			if (items [i] != null) {
//				index = i;
//				break;
//			}
//		}
//		if (index != -1) showItem (items [index], true);
//	}
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
//	if (direction == sortDirection) return;
//	sortDirection = direction;
//	if (sortColumn != null && !sortColumn.isDisposed ()) {
//		if (sortDirection == SWT.NONE) {
//			OS.SetDataBrowserSortOrder (handle, (short) OS.kDataBrowserOrderIncreasing);
//			TreeColumn column = sortColumn;
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
//	showItem (item, false);
//	int columnId = (columnCount == 0) ? column_id : columns [0].id;
//	OS.RevealDataBrowserItem (handle, item.id, columnId, (byte) OS.kDataBrowserRevealWithoutSelecting);
//	Rect rect = new Rect ();
//	if (OS.GetDataBrowserItemPartBounds (handle, item.id, column_id, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
//		int border = getBorder ();
//		int [] top = new int [1], left = new int [1];
//		OS.GetDataBrowserScrollPosition (handle, top, left);
//		OS.SetDataBrowserScrollPosition (handle, Math.max (0, top [0] + rect.top - border - getHeaderHeight ()), left [0]);
//	}
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
	if (column.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (column.parent != this) return;
	int index = indexOf (column);
	if (columnCount <= 1 || !(0 <= index && index < columnCount)) return;
	((NSTableView)view).scrollColumnToVisible(index + ((style & SWT.CHECK) != 0 ? 1 : 0));
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
	showItem (item, true);
}

void showItem (TreeItem item, boolean scroll) {
	int count = 0;
//	TreeItem parentItem = item.parentItem;
//	while (parentItem != null && !parentItem._getExpanded ()) {
//		count++;
//		parentItem = parentItem.parentItem;
//	}
//	int index = 0;
//	parentItem = item.parentItem;
//	TreeItem [] path = new TreeItem [count];
//	while (parentItem != null && !parentItem._getExpanded ()) {
//		path [index++] = parentItem;
//		parentItem = parentItem.parentItem;
//	}
//	for (int i=path.length-1; i>=0; --i) {
//		path [i].setExpanded (true);
//	}
//	if (scroll) {
//		/*
//		* Bug in the Macintosh.  When there is not room to show a
//		* single item in the data browser, RevealDataBrowserItem()
//		* scrolls the item such that it is above the top of the data
//		* browser.  The fix is to remember the index and scroll when
//		* the data browser is resized.
//		* 
//		* Bug in the Macintosh.  When items are added to the data
//		* browser after is has been hidden, RevealDataBrowserItem()
//		* when called before the controls behind the data browser
//		* are repainted causes a redraw.  This redraw happens right
//		* away causing pixel corruption.  The fix is to remember the
//		* index and scroll when the data browser is shown.
//		*/
//		Rectangle rect = getClientArea ();
//		if (rect.height < getItemHeight () || !OS.IsControlVisible (handle)) {
//			showItem = item;
//			return;
//		}
//		showItem = null;
//		Rectangle itemRect = item.getBounds ();
//		if (!itemRect.isEmpty()) {
//			if (rect.contains (itemRect.x, itemRect.y)
//				&& rect.contains (itemRect.x, itemRect.y + itemRect.height)) return;
//		}
//		int [] top = new int [1], left = new int [1];
//		OS.GetDataBrowserScrollPosition (handle, top, left);
//		int columnId = (columnCount == 0) ? column_id : columns [0].id;
//		int options = OS.kDataBrowserRevealWithoutSelecting;
//		/*
//		* This code is intentionally commented, since kDataBrowserRevealAndCenterInView
//		* does not scroll the item to the center always (it seems to scroll to the
//		* end in some cases).
//		*/
//		//options |= OS.kDataBrowserRevealAndCenterInView;
//		OS.RevealDataBrowserItem (handle, item.id, columnId, (byte) options);
//		int [] newTop = new int [1], newLeft = new int [1];
//		if (columnCount == 0) {
//			boolean fixScroll = false;
//			Rect content = new Rect ();
//			if (OS.GetDataBrowserItemPartBounds (handle, item.id, columnId, OS.kDataBrowserPropertyContentPart, content) == OS.noErr) {
//				fixScroll = content.left < rect.x || content.left >= rect.x + rect.width;
//				if (!fixScroll) {
//					GC gc = new GC (this);
//					int contentWidth = calculateWidth (new int[]{item.id}, gc, false, 0, 0);
//					gc.dispose ();
//					fixScroll =  content.left + contentWidth > rect.x + rect.width;
//				}
//			}
//			if (fixScroll) {
//				int leftScroll = getLeftDisclosureInset (columnId);
//				int levelIndent = DISCLOSURE_COLUMN_LEVEL_INDENT;
//				if (OS.VERSION >= 0x1040) {
//					float [] metric = new float [1];
//					OS.DataBrowserGetMetric (handle, OS.kDataBrowserMetricDisclosureColumnPerDepthGap, null, metric);
//					levelIndent = (int) metric [0];
//				}
//				TreeItem temp = item;
//				while (temp.parentItem != null) {
//					leftScroll += levelIndent;
//					temp = temp.parentItem;
//				}
//				OS.GetDataBrowserScrollPosition (handle, newTop, newLeft);
//				OS.SetDataBrowserScrollPosition (handle, newTop [0], leftScroll);
//			}
//		}
//
//		/*
//		* Bug in the Macintosh.  For some reason, when the DataBrowser is scrolled
//		* by RevealDataBrowserItem(), the scrollbars are not redrawn.  The fix is to
//		* force a redraw.
//		*/
//		OS.GetDataBrowserScrollPosition (handle, newTop, newLeft);
//		if (horizontalBar != null && newLeft [0] != left [0]) horizontalBar.redraw ();
//		if (verticalBar != null && newTop [0] != top [0]) verticalBar.redraw ();
//	}
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
	//checkItems (false);
	//TODO - optimize
	TreeItem [] selection = getSelection ();
	if (selection.length > 0) showItem (selection [0], true);
}

}
