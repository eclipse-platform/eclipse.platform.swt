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

 
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.DataBrowserCallbacks;
import org.eclipse.swt.internal.carbon.DataBrowserCustomCallbacks;
import org.eclipse.swt.internal.carbon.DataBrowserListViewColumnDesc;
import org.eclipse.swt.internal.carbon.Rect;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class provide a selectable user interface object
 * that displays a hierarchy of items and issue notificiation when an
 * item in the hierarchy is selected.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TreeItem</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, Collapse, Expand</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles SINGLE and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Tree extends Composite {
	TreeItem [] items;
	GC paintGC;
	int anchorFirst, anchorLast, lastHittest;
	boolean ignoreSelect, ignoreExpand, wasSelected, wasExpanded;
	TreeItem showItem;
	static final int CHECK_COLUMN_ID = 1024;
	static final int COLUMN_ID = 1025;
	static final int EXTRA_WIDTH = 30;
	static final int CHECK_COLUMN_WIDTH = 25;

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

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * If the reciever has <code>SWT.CHECK</code> style set and the check selection changes,
 * the event object detail field contains the value <code>SWT.CHECK</code>.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
 * The item field of the event object is valid for default selection, but the detail field is not used.
 * </p>
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

int calculateWidth (TreeItem [] items, GC gc) {
	int width = 0;
	for (int i = 0; i < items.length; i++) {
		TreeItem item = items [i];
		width = Math.max (width, item.calculateWidth (gc));
		if (item.getExpanded ()) {			
			width = Math.max (width, calculateWidth (item.getItems (), gc));
		}
	}
	return width;
}

static int checkStyle (int style) {
	/*
	* Feature in Windows.  It is not possible to create
	* a tree that scrolls and does not have scroll bars.
	* The TVS_NOSCROLL style will remove the scroll bars
	* but the tree will never scroll.  Therefore, no matter
	* what style bits are specified, set the H_SCROLL and
	* V_SCROLL bits so that the SWT style will match the
	* widget that Windows creates.
	*/
	style |= SWT.H_SCROLL | SWT.V_SCROLL;
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0;
	if (wHint == SWT.DEFAULT) {
		TreeItem [] items = getItems ();
		GC gc = new GC (this);
		for (int i=0; i<items.length; i++) {
			TreeItem item = items [i];
			width = Math.max (width, item.calculateWidth (gc));
		}
		gc.dispose ();
		width += EXTRA_WIDTH;
		if ((style & SWT.CHECK) != 0) width += CHECK_COLUMN_WIDTH;
	} else {
		width = wHint;
	}
	if (width <= 0) width = DEFAULT_WIDTH;
	int height = 0;
	if (hHint == SWT.DEFAULT) {
		height = getItemCount () * getItemHeight ();
	} else {
		height = hHint;
	}
	if (height <= 0) height = DEFAULT_HEIGHT;
	Rectangle rect = computeTrim (0, 0, width, height);
	return new Point (rect.width, rect.height);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	int border = 0;
	int [] outMetric = new int [1];
	OS.GetThemeMetric (OS.kThemeMetricFocusRectOutset, outMetric);
	border += outMetric [0];
	OS.GetThemeMetric (OS.kThemeMetricEditTextFrameOutset, outMetric);
	border += outMetric [0];
	Rect rect = new Rect ();
	OS.GetDataBrowserScrollBarInset (handle, rect);
	x -= rect.left + border;
	y -= rect.top + border;
	width += rect.left + rect.right + border + border;
	height += rect.top + rect.bottom + border + border;
	return new Rectangle (x, y, width, height);
}

void createHandle () {
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
	OS.CreateDataBrowserControl (window, null, OS.kDataBrowserListView, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
	int selectionFlags = (style & SWT.SINGLE) != 0 ? OS.kDataBrowserSelectOnlyOne | OS.kDataBrowserNeverEmptySelectionSet : OS.kDataBrowserCmdTogglesSelection;
	OS.SetDataBrowserSelectionFlags (handle, selectionFlags);
	OS.SetDataBrowserListViewHeaderBtnHeight (handle, (short) 0);
	OS.SetDataBrowserHasScrollBars (handle, (style & SWT.H_SCROLL) != 0, (style & SWT.V_SCROLL) != 0);
	int position = 0;
	if ((style & SWT.CHECK) != 0) {
		DataBrowserListViewColumnDesc checkColumn = new DataBrowserListViewColumnDesc ();
		checkColumn.headerBtnDesc_version = OS.kDataBrowserListViewLatestHeaderDesc;
		checkColumn.propertyDesc_propertyID = CHECK_COLUMN_ID;
		checkColumn.propertyDesc_propertyType = OS.kDataBrowserCheckboxType;
		checkColumn.propertyDesc_propertyFlags = OS.kDataBrowserPropertyIsMutable;
		//TODO - check column size
		checkColumn.headerBtnDesc_minimumWidth = CHECK_COLUMN_WIDTH;
		checkColumn.headerBtnDesc_maximumWidth = CHECK_COLUMN_WIDTH;
		checkColumn.headerBtnDesc_initialOrder = OS.kDataBrowserOrderIncreasing;
		OS.AddDataBrowserListViewColumn (handle, checkColumn, position++);
	}
	DataBrowserListViewColumnDesc column = new DataBrowserListViewColumnDesc ();
	column.headerBtnDesc_version = OS.kDataBrowserListViewLatestHeaderDesc;
	column.propertyDesc_propertyID = COLUMN_ID;
	column.propertyDesc_propertyType = OS.kDataBrowserCustomType;
	column.propertyDesc_propertyFlags = OS.kDataBrowserListViewSelectionColumn | OS.kDataBrowserDefaultPropertyFlags;
	column.headerBtnDesc_initialOrder = OS.kDataBrowserOrderIncreasing;
	OS.AddDataBrowserListViewColumn (handle, column, position);
	OS.SetDataBrowserListViewDisclosureColumn (handle, COLUMN_ID, true);
	OS.SetDataBrowserTableViewNamedColumnWidth (handle, COLUMN_ID, (short) 0);

	/*
	* Feature in the Macintosh.  Scroll bars are not created until
	* the data browser needs to draw them.  The fix is to force the scroll
	* bars to be created by temporarily giving the widget a size, drawing
	* it on a offscreen buffer to avoid flashes and then restoring it to
	* size zero.
	*/
	int size = 50;
	Rect rect = new Rect ();
	rect.right = rect.bottom = (short) size;
	OS.SetControlBounds (handle, rect);
	int bpl = size * 4;
	int [] gWorld = new int [1];
	int data = OS.NewPtr (bpl * size);
	OS.NewGWorldFromPtr (gWorld, OS.k3ARGBPixelFormat, rect, 0, 0, 0, data, bpl);
	int [] curPort = new int [1];
	int [] curGWorld = new int [1];
	OS.GetGWorld (curPort, curGWorld);	
	OS.SetGWorld (gWorld [0], curGWorld [0]);
	OS.DrawControlInCurrentPort (handle);
	OS.SetGWorld (curPort [0], curGWorld [0]);
	OS.DisposeGWorld (gWorld [0]);
	OS.DisposePtr (data);
	rect.right = rect.bottom = (short) 0;
	OS.SetControlBounds (handle, rect);
}

void createItem (TreeItem item, TreeItem parentItem, int index) {
	int count = 0;
	for (int i=0; i<items.length; i++) {
		if (items [i] != null && items [i].parentItem == parentItem) count++;
	}
	if (index == -1) index = count;
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	item.index = index;
	for (int i=0; i<items.length; i++) {
		if (items [i] != null && items [i].parentItem == parentItem) {
			if (items [i].index >= item.index) items [i].index++;
		}
	}
	int id = 0;
	while (id < items.length && items [id] != null) id++;
	if (id == items.length) {
		TreeItem [] newItems = new TreeItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	items [id] = item;
	item.id = id + 1;
	int parentID = OS.kDataBrowserNoItem;
	boolean expanded = true;
	if (parentItem != null) {
		parentID = parentItem.id;
		expanded = parentItem.getExpanded ();
	}
	if (expanded) {
		if (OS.AddDataBrowserItems (handle, parentID, 1, new int[] {item.id}, 0) != OS.noErr) {
			items [id] = null;
			error (SWT.ERROR_ITEM_NOT_ADDED);
		}
	} else {
		if (count == 0 && parentItem != null) parentItem.redraw ();
	}
}

ScrollBar createScrollBar (int style) {
	return createStandardBar (style);
}

void createWidget () {
	super.createWidget ();
	items = new TreeItem [4];
}

Color defaultBackground () {
	return display.getSystemColor (SWT.COLOR_LIST_BACKGROUND);
}

Color defaultForeground () {
	return display.getSystemColor (SWT.COLOR_LIST_FOREGROUND);
}

int defaultThemeFont () {	
	return OS.kThemeViewsFont;
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
	ignoreSelect = true;
	int [] selectionFlags = null;
	if ((style & SWT.SINGLE) != 0) {
		selectionFlags = new int [1];
		OS.GetDataBrowserSelectionFlags (handle, selectionFlags);
		OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0] & ~OS.kDataBrowserNeverEmptySelectionSet);
	}
	OS.SetDataBrowserSelectedItems (handle, 0, null, OS.kDataBrowserItemsRemove);
	if ((style & SWT.SINGLE) != 0) {
		OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0]);
	}
	ignoreSelect = false;
}

void destroyItem (TreeItem item) {
	int parentID = item.parentItem == null ? OS.kDataBrowserNoItem : item.parentItem.id;
	if (OS.RemoveDataBrowserItems (handle, parentID, 1, new int[] {item.id}, 0) != OS.noErr) {
		error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	releaseItems (item.getItems ());
	releaseItem (item);
	TreeItem parentItem = item.parentItem;
	for (int i=0; i<items.length; i++) {
		if (items [i] != null && items [i].parentItem == parentItem) {
			if (items [i].index >= item.index) --items [i].index;
		}
	}
	setScrollWidth ();
}

int drawItemProc (int browser, int id, int property, int itemState, int theRect, int gdDepth, int colorDevice) {
	int index = id - 1;
	if (!(0 <= index && index < items.length)) return OS.noErr;
	TreeItem item = items [index];
	Rect rect = new Rect ();
	OS.memcpy (rect, theRect, Rect.sizeof);
	int x = rect.left;
	int y = rect.top;
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	Rect controlRect = new Rect ();
	OS.GetControlBounds (handle, controlRect);
	x -= controlRect.left;
	y -= controlRect.top;
	GC gc = paintGC;
	if (gc == null) {
		GCData data = new GCData ();
		int [] port = new int [1];
		OS.GetPort (port);
		data.port = port [0];
		gc = GC.carbon_new (this, data);
	}
	int clip = OS.NewRgn ();
	OS.GetClip (clip);
	OS.OffsetRgn (clip, (short)-controlRect.left, (short)-controlRect.top);
	gc.setClipping (Region.carbon_new (display, clip));
	OS.DisposeRgn (clip);
	Color background = item.getBackground ();
	gc.setBackground (background);
	gc.fillRectangle (x, y, width, height);
	Image image = item.image;
	if (image != null) {
		Rectangle bounds = image.getBounds ();
		gc.drawImage (image, 0, 0, bounds.width, bounds.height, x, y + (height - bounds.height) / 2, bounds.width, bounds.height);
		x += bounds.width + 2;
	}
	Point extent = gc.stringExtent (item.text);
	if ((itemState & OS.kDataBrowserItemIsSelected) != 0) {
		gc.setForeground (display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT));
		gc.setBackground (display.getSystemColor (SWT.COLOR_LIST_SELECTION));
		gc.fillRectangle (x - 1, y, extent.x + 2, height);
	} else {
		Color foreground = item.getForeground ();
		gc.setForeground (foreground);
	}
	gc.drawString (item.text, x, y + (height - extent.y) / 2);
	if (gc != paintGC) gc.dispose ();
	return OS.noErr;
}

void drawWidget (int control, int damageRgn, int visibleRgn, int theEvent) {
	GC currentGC = paintGC;
	if (currentGC == null) {
		GCData data = new GCData ();
		data.paintEvent = theEvent;
		data.visibleRgn = visibleRgn;
		paintGC = GC.carbon_new (this, data);
	} 
	super.drawWidget (control, damageRgn, visibleRgn, theEvent);
	if (currentGC == null) {
		paintGC.dispose ();
		paintGC = null;
	}
}

public Rectangle getClientArea () {
	checkWidget();
	int border = 0;
	int [] outMetric = new int [1];
	OS.GetThemeMetric (OS.kThemeMetricFocusRectOutset, outMetric);
	border += outMetric [0];
	OS.GetThemeMetric (OS.kThemeMetricEditTextFrameOutset, outMetric);
	border += outMetric [0];
	Rect rect = new Rect (), inset = new Rect ();
	OS.GetControlBounds (handle, rect);
	OS.GetDataBrowserScrollBarInset (handle, inset);
	int width = Math.max (0, rect.right - rect.left - inset.right - border - border);
	int height = Math.max (0, rect.bottom - rect.top - inset.bottom - border - border);
	return new Rectangle (inset.left, inset.top, width, height);
}

/**
 * Returns the item at the given point in the receiver
 * or null if no such item exists. The point is in the
 * coordinate system of the receiver.
 *
 * @param point the point used to locate the item
 * @return the item at the given point
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
	Rect rect = new Rect ();
	OS.GetControlBounds (handle, rect);
	org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
	OS.SetPt (pt, (short) (point.x + rect.left), (short) (point.y + rect.top));
	//TODO - optimize
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null) {
			if (OS.GetDataBrowserItemPartBounds (handle, item.id, COLUMN_ID, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
				if (OS.PtInRect (pt, rect)) return item;
			}
		}
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
	return getItemCount (null);
}

int getItemCount (TreeItem item) {
	checkWidget ();
	int count = 0;
	for (int i=0; i<items.length; i++) {
		if (items [i] != null && items [i].parentItem == item) count++;
	}
	return count;
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
	short [] height = new short [1];
	if (OS.GetDataBrowserTableViewRowHeight (handle, height) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_ITEM_HEIGHT);
	}
	return height [0];
}

/**
 * Returns the number of items contained in the receiver
 * that are direct item children of the receiver.  These
 * are the roots of the tree.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem [] getItems () {
	checkWidget ();
	return getItems (null);
}

TreeItem [] getItems (TreeItem item) {
	if (items == null) return new TreeItem [0];
	int count = 0;
	for (int i=0; i<items.length; i++) {
		if (items [i] != null && items [i].parentItem == item) count++;
	}
	TreeItem [] result = new TreeItem [count];
	for (int i=0; i<items.length; i++) {
		if (items [i] != null && items [i].parentItem == item) {
			result [items [i].index] = items [i];
		}
	}
	return result;
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
 * selected in the receiver. An empty array indicates that no
 * items are selected.
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
	int ptr = OS.NewHandle (0);
	if (OS.GetDataBrowserItems (handle, OS.kDataBrowserNoItem, true, OS.kDataBrowserItemIsSelected, ptr) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_SELECTION);
	}
	int count = OS.GetHandleSize (ptr) / 4;
	TreeItem [] result = new TreeItem [count];
	OS.HLock (ptr);
	int [] start = new int [1];
	OS.memcpy (start, ptr, 4);
	int [] id = new int [1];
	for (int i=0; i<count; i++) {
		OS.memcpy (id, start [0] + (i * 4), 4);
		result [i] = items [id [0] - 1];
	}
	OS.HUnlock (ptr);
	OS.DisposeHandle (ptr);
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
	int [] count = new int [1];
	if (OS.GetDataBrowserItemCount (handle, OS.kDataBrowserNoItem, true, OS.kDataBrowserItemIsSelected, count) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_COUNT);
	}
	return count [0];
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
	//TODO - optimize
	Rect rect = new Rect ();
	OS.GetControlBounds (handle, rect);
	int offset = 0;
	int [] outMetric = new int [1];
	OS.GetThemeMetric (OS.kThemeMetricFocusRectOutset, outMetric);
	offset += outMetric [0];
	OS.GetThemeMetric (OS.kThemeMetricEditTextFrameOutset, outMetric);
	offset += outMetric [0];
	int y = rect.top + offset;
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null) {
			if (OS.GetDataBrowserItemPartBounds (handle, item.id, COLUMN_ID, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
				if (rect.top <= y && y <= rect.bottom) return item;
			}
		}
	}
	return null;
}

int hitTestProc (int browser, int id, int property, int theRect, int mouseRect) {
	lastHittest = id;
	return 1;
}

void hookEvents () {
	super.hookEvents ();
	DataBrowserCallbacks callbacks = new DataBrowserCallbacks ();
	callbacks.version = OS.kDataBrowserLatestCallbacks;
	OS.InitDataBrowserCallbacks (callbacks);
	callbacks.v1_itemCompareCallback = display.itemCompareProc;
	callbacks.v1_itemDataCallback = display.itemDataProc;
	callbacks.v1_itemNotificationCallback = display.itemNotificationProc;
	OS.SetDataBrowserCallbacks (handle, callbacks);
	DataBrowserCustomCallbacks custom = new DataBrowserCustomCallbacks ();
	custom.version = OS.kDataBrowserLatestCustomCallbacks;
	OS.InitDataBrowserCustomCallbacks (custom);
	custom.v1_drawItemCallback = display.drawItemProc;
	custom.v1_hitTestCallback = display.hitTestProc;
	custom.v1_trackingCallback = display.trackingProc;
	OS.SetDataBrowserCustomCallbacks (handle, custom);
}

int itemCompareProc (int browser, int itemOne, int itemTwo, int sortProperty) {
	int index1 = itemOne - 1;
	if (!(0 <= index1 && index1 < items.length)) return OS.noErr;
	int index2 = itemTwo - 1;
	if (!(0 <= index2 && index2 < items.length)) return OS.noErr;
	return items [index1].index < items [index2].index ? 1 : 0;
}

int itemDataProc (int browser, int id, int property, int itemData, int setValue) {
	int index = id - 1;
	if (!(0 <= index && index < items.length)) return OS.noErr;
	TreeItem item = items [index];
	switch (property) {
		case CHECK_COLUMN_ID: {
			if (setValue != 0) {
//				short [] theData = new short [1];
//				OS.GetDataBrowserItemDataButtonValue (itemData, theData);
//				item.checked = theData [0] == OS.kThemeButtonOn;
				item.checked = !item.checked;
				if (item.checked && item.grayed) {
					OS.SetDataBrowserItemDataButtonValue (itemData, (short) OS.kThemeButtonMixed);
				} else {
					int theData = item.checked ? OS.kThemeButtonOn : OS.kThemeButtonOff;
					OS.SetDataBrowserItemDataButtonValue (itemData, (short) theData);
				}
				Event event = new Event ();
				event.item = item;
				event.detail = SWT.CHECK;
				postEvent (SWT.Selection, event);
			} else {
//				short theData = (short)(item.checked ? OS.kThemeButtonOn : OS.kThemeButtonOff);
//				OS.SetDataBrowserItemDataButtonValue (itemData, theData);
				int theData = OS.kThemeButtonOff;
				if (item.checked) theData = item.grayed ? OS.kThemeButtonMixed : OS.kThemeButtonOn;
				OS.SetDataBrowserItemDataButtonValue (itemData, (short) theData);
			}
			break;
		}
//		case COLUMN_ID: {
//			String text = item.text;
//			char [] buffer = new char [text.length ()];
//			text.getChars (0, buffer.length, buffer, 0);
//			int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
//			if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
//			OS.SetDataBrowserItemDataText (itemData, ptr);
//			OS.CFRelease (ptr);
//			break;
//		}
		case OS.kDataBrowserItemIsContainerProperty: {
			for (int i=0; i<items.length; i++) {
				if (items [i] != null && items [i].parentItem == item) {
					OS.SetDataBrowserItemDataBooleanValue (itemData, true);
				}
			}
			break;
		}
	}
	return OS.noErr;
}

int itemNotificationProc (int browser, int id, int message) {
	int index = id - 1;
	if (!(0 <= index && index < items.length)) return OS.noErr;
	TreeItem item = items [index];
	switch (message) {
		case OS.kDataBrowserItemSelected:
		case OS.kDataBrowserItemDeselected: {
			wasSelected = true;
			if (ignoreSelect) break;
			int [] first = new int [1], last = new int [1];
			OS.GetDataBrowserSelectionAnchor (handle, first, last);
			boolean selected = false;
			if ((style & SWT.MULTI) != 0) {
				int modifiers = OS.GetCurrentEventKeyModifiers ();
				if ((modifiers & OS.shiftKey) != 0) {
					if (message == OS.kDataBrowserItemSelected) {
						selected = first [0] == id || last [0] == id;
					} else {
						selected = id == anchorFirst || id == anchorLast;
					}
				} else {
					if ((modifiers & OS.cmdKey) != 0) {
						selected = true;
					} else {
						selected = first [0] == last [0];
					}
				}
			} else {
				selected = message == OS.kDataBrowserItemSelected;
			}
			if (selected) {
				anchorFirst = first [0];
				anchorLast = last [0];
				Event event = new Event ();
				event.item = item;
				postEvent (SWT.Selection, event);
			}
			break;
		}	
		case OS.kDataBrowserItemDoubleClicked: {
			wasSelected = true;
			Event event = new Event ();
			event.item = item;
			postEvent (SWT.DefaultSelection, event);
			break;
		}
		case OS.kDataBrowserContainerClosing: {
			/*
			* Bug in the Macintosh.  For some reason, if the selected sub items of an item
			* get a kDataBrowserItemDeselected notificaton when the item is collapsed, a
			* call to GetDataBrowserSelectionAnchor () will cause a segment fault.  The
			* fix is to deselect these items ignoring kDataBrowserItemDeselected and them
			* issue a selection event.
			*/
			int ptr = OS.NewHandle (0);
			if (OS.GetDataBrowserItems (handle, item.id, true, OS.kDataBrowserItemIsSelected, ptr) == OS.noErr) {
				int count = OS.GetHandleSize (ptr) / 4;
				if (count > 0) {
					int [] ids = new int [count];
					OS.HLock (ptr);
					int [] start = new int [1];
					OS.memcpy (start, ptr, 4);
					OS.memcpy (ids, start [0], count * 4);
					OS.HUnlock (ptr);
					ignoreSelect = true;
					OS.SetDataBrowserSelectedItems (handle, ids.length, ids, OS.kDataBrowserItemsRemove);
					ignoreSelect = false;
					Event event = new Event ();
					event.item = item;
					if (ignoreExpand) {
						sendEvent (SWT.Selection, event);
					} else {
						postEvent (SWT.Selection, event);
					}						 
				}
			}
			OS.DisposeHandle (ptr);
			break;
		}
		case OS.kDataBrowserContainerClosed: {
			wasExpanded = true;
			if (!ignoreExpand) {
				Event event = new Event ();
				event.item = item;
				sendEvent (SWT.Collapse, event);
			}
			setScrollWidth ();
			break;
		}
		case OS.kDataBrowserContainerOpened: {
			wasExpanded = true;
			if (!ignoreExpand) {
				Event event = new Event ();
				event.item = item;
				sendEvent (SWT.Expand, event);
			}
			int count = 0;
			for (int i=0; i<items.length; i++) {
				if (items [i] != null && items [i].parentItem == item) count++;
			}
			TreeItem [] newItems = new TreeItem [count];
			int [] ids = new int [count];
			for (int i=0; i<items.length; i++) {
				if (items [i] != null && items [i].parentItem == item) {
					ids [items [i].index] = items [i].id;
					newItems [items [i].index] = items [i];
				}
			}
			OS.AddDataBrowserItems (handle, id, ids.length, ids, 0);
			setScrollWidth (newItems, false);
			break;
		}
	}
	return OS.noErr;
}

int kEventTextInputUnicodeForKeyEvent (int nextHandler, int theEvent, int userData) {
	int result = super.kEventTextInputUnicodeForKeyEvent (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	int [] keyboardEvent = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendKeyboardEvent, OS.typeEventRef, null, keyboardEvent.length * 4, null, keyboardEvent);
	int [] keyCode = new int [1];
	OS.GetEventParameter (keyboardEvent [0], OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
	switch (keyCode [0]) {
		case 36: { /* Return */
			postEvent (SWT.DefaultSelection);
			break;
		}
	}
	return result;
}

int kEventMouseDown (int nextHandler, int theEvent, int userData) {
	int result = super.kEventMouseDown (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	Shell shell = getShell ();
    /*
     * Silenio:
     * This is nasty, but it was the best we could do. Oddly enough, 
     * it doesn't bring the shell forward on McQ's machine.
     */
	shell.bringToTop (false);
	/*
	* Feature in the Macintosh.  For some reason, when the user
	* clicks on the data browser, focus is assigned, then lost
	* and then reassigned causing kEvenControlSetFocusPart events.
	* The fix is to ignore kEvenControlSetFocusPart when the user
	* clicks and send the focus events from kEventMouseDown.
	*/
	Control oldFocus = display.getFocusControl ();
	display.ignoreFocus = true;
	wasSelected = wasExpanded = false;
	result = OS.CallNextEventHandler (nextHandler, theEvent);
	display.ignoreFocus = false;
	if (oldFocus != this) {
		if (oldFocus != null && !oldFocus.isDisposed ()) oldFocus.sendFocusEvent (false, false);
		if (!isDisposed () && isEnabled ()) sendFocusEvent (true, false);
	}
	if (!wasSelected && !wasExpanded) {
		if (OS.IsDataBrowserItemSelected (handle, lastHittest)) {
			int index = lastHittest - 1;
			if (0 <= index && index < items.length) {
				Event event = new Event ();
				event.item = items [index];
				postEvent (SWT.Selection, event);
			}
		}
	}
	wasSelected = wasExpanded = false;
	return result;
}

boolean releaseItem (TreeItem item) {
	if (item.isDisposed ()) return false;
	items [item.id - 1] = null;
	return true;
}

void releaseItems (TreeItem [] nodes) {
	for (int i=0; i<nodes.length; i++) {
		TreeItem item = nodes [i];
		TreeItem [] sons = item.getItems ();
		if (sons.length != 0) {
			releaseItems (sons);
		}
		if (releaseItem (item)) {
			item.releaseResources ();
		}
	}
}

void releaseWidget () {
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null && !item.isDisposed ()) {
			item.releaseResources ();
		}
	}
	items = null;
	super.releaseWidget ();
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
	checkWidget ();
	if (OS.RemoveDataBrowserItems (handle, OS.kDataBrowserNoItem, 0, null, 0) != OS.noErr) {
		error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	OS.SetDataBrowserScrollPosition (handle, 0, 0);
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null && !item.isDisposed ()) item.releaseResources ();
	}
	items = new TreeItem [4];
	anchorFirst = anchorLast = 0;
	setScrollWidth ();
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver's selection changes.
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
 * be notified when items in the receiver are expanded or collapsed..
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

void resetVisibleRegion (int control) {
	super.resetVisibleRegion (control);
	if (showItem != null && !showItem.isDisposed ()) {
		showItem (showItem , true);
	}	
}

/**
 * Display a mark indicating the point at which an item will be inserted.
 * The drop insert item has a visual hint to show where a dragged item 
 * will be inserted when dropped on the tree.
 * 
 * @param item the insert item.  Null will clear the insertion mark.
 * @param after true places the insert mark above 'item'. false places 
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
 * Selects all the items in the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void selectAll () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return;
	ignoreSelect = true;
	OS.SetDataBrowserSelectedItems (handle, 0, null, OS.kDataBrowserItemsAssign);
	ignoreSelect = false;
}

int setBounds (int control, int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	/*
	* Ensure that the selection is visible when the tree is resized
	* from a zero size to a size that can show the selection.
	*/
	int result = super.setBounds (control, x, y, width, height, move, resize, events);
	if (showItem != null && !showItem.isDisposed ()) {
		showItem (showItem , true);
	}		 
	return result;
}

void setFontStyle (Font font) {
	super.setFontStyle (font);
	for (int i = 0; i < items.length; i++) {
		TreeItem item = items [i];
		if (item != null) item.width = -1;
	}	
}

void setScrollWidth () {
	setScrollWidth (getItems (), true);
}

void setScrollWidth (TreeItem item) {
	TreeItem parentItem = item.parentItem;
	if (parentItem != null && !parentItem.getExpanded ()) return;
	GC gc = new GC (this);
	int newWidth = item.calculateWidth (gc);
	gc.dispose ();
	short [] width = new short [1];
	OS.GetDataBrowserTableViewNamedColumnWidth (handle, COLUMN_ID, width);
	if (width [0] < newWidth) {
		OS.SetDataBrowserTableViewNamedColumnWidth (handle, COLUMN_ID, (short) newWidth);
	}
}

void setScrollWidth (TreeItem [] items, boolean set) {
	GC gc = new GC (this);
	int newWidth = calculateWidth (items, gc);
	gc.dispose ();
	if (!set) {
		short [] width = new short [1];
		OS.GetDataBrowserTableViewNamedColumnWidth (handle, COLUMN_ID, width);
		if (width [0] >= newWidth) return;
	}
	OS.SetDataBrowserTableViewNamedColumnWidth (handle, COLUMN_ID, (short) newWidth);
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selected is first cleared, then the new items are
 * selected.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of items is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if one of the item has been disposed</li>
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
	int[] ids = new int [items.length];
	for (int i=0; i<items.length; i++) {
		if (items [i] == null) error (SWT.ERROR_INVALID_ARGUMENT);
		if (items [i].isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		ids [i] = items [i].id;
		showItem (items [i], false);
	}
	ignoreSelect = true;
	/*
	* Bug in the Macintosh.  When the DataBroswer selection flags includes
	* both kDataBrowserNeverEmptySelectionSet and kDataBrowserSelectOnlyOne,
    * two items are selected when SetDataBrowserSelectedItems() is called
    * with kDataBrowserItemsAssign to assign a new seletion despite the fact
	* that kDataBrowserSelectOnlyOne was specified.  The fix is to save and
	* restore kDataBrowserNeverEmptySelectionSet around each call to
	* SetDataBrowserSelectedItems().
	*/
	int [] selectionFlags = null;
	if ((style & SWT.SINGLE) != 0) {
		selectionFlags = new int [1];
		OS.GetDataBrowserSelectionFlags (handle, selectionFlags);
		OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0] & ~OS.kDataBrowserNeverEmptySelectionSet);
	}
	OS.SetDataBrowserSelectedItems (handle, ids.length, ids, OS.kDataBrowserItemsAssign);
	if ((style & SWT.SINGLE) != 0) {
		OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0]);
	}
	ignoreSelect = false;
	if (items.length > 0) showItem (items [0], true);
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
	showItem (item, false);	
	OS.RevealDataBrowserItem (handle, item.id, COLUMN_ID, (byte) OS.kDataBrowserRevealWithoutSelecting);
//	Rect rect = new Rect ();
//	OS.GetControlBounds (handle, rect);
//	int x = rect.left, y = rect.top;
//	if (OS.GetDataBrowserItemPartBounds (handle, item.id, COLUMN_ID, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
//		OS.SetDataBrowserScrollPosition (handle, rect.top - y - 3, 0);
//	}
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
	TreeItem parentItem = item.parentItem;
	while (parentItem != null && !parentItem.getExpanded ()) {
		count++;
		parentItem = parentItem.parentItem;
	}
	int index = 0;
	parentItem = item.parentItem;
	TreeItem [] path = new TreeItem [count];
	while (parentItem != null && !parentItem.getExpanded ()) {
		path [index++] = parentItem;
		parentItem = parentItem.parentItem;
	}
	for (int i=path.length-1; i>=0; --i) {
		path [i].setExpanded (true);
	}
	if (scroll) {
		/*
		* Bug in the Macintosh.  When there is not room to show a
		* single item in the data browser, RevealDataBrowserItem()
		* scrolls the item such that it is above the top of the data
		* browser.  The fix is to remember the index and scroll when
		* the data browser is resized.
		* 
		* Bug in the Macintosh.  When items are added to the data
		* browser after is has been hidden, RevealDataBrowserItem()
		* when called before the controls behind the data browser
		* are repainted causes a redraw.  This redraw happens right
		* away causing pixel corruption.  The fix is to remember the
		* index and scroll when the data browser is shown.
		*/
		Rectangle rect = getClientArea ();
		if (rect.height < getItemHeight () || !OS.IsControlVisible (handle)) {
			showItem = item;
			return;
		}
		showItem = null;
		Rectangle itemRect = item.getBounds ();
		if (!itemRect.isEmpty()) {
			if (rect.contains (itemRect.x, itemRect.y)
				&& rect.contains (itemRect.x, itemRect.y + itemRect.height)) return;
		}
		int [] top = new int [1], left = new int [1];
		OS.GetDataBrowserScrollPosition (handle, top, left);
		OS.RevealDataBrowserItem (handle, item.id, COLUMN_ID, (byte) OS.kDataBrowserRevealWithoutSelecting);

		/*
		* Bug in the Macintosh.  For some reason, when the DataBrowser is scrolled
		* by RevealDataBrowserItem(), the scrollbars are not redrawn.  The fix is to
		* force a redraw.
		*/
		int [] newTop = new int [1], newLeft = new int [1];
		OS.GetDataBrowserScrollPosition (handle, newTop, newLeft);
		if (horizontalBar != null && newLeft [0] != left [0]) horizontalBar.redraw ();
		if (verticalBar != null && newTop [0] != top [0]) verticalBar.redraw ();
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
	//TODO - optimize
	TreeItem [] selection = getSelection ();
	if (selection.length > 0) showItem (selection [0], true);
}

int trackingProc (int browser, int id, int property, int theRect, int startPt, int modifiers) {
	return 1;
}

}
