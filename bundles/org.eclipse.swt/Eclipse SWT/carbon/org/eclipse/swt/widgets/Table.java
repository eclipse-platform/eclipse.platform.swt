/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.DataBrowserCallbacks;
import org.eclipse.swt.internal.carbon.DataBrowserCustomCallbacks;
import org.eclipse.swt.internal.carbon.DataBrowserListViewColumnDesc;
import org.eclipse.swt.internal.carbon.DataBrowserListViewHeaderDesc;
import org.eclipse.swt.internal.carbon.Rect;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/** 
 * Instances of this class implement a selectable user interface
 * object that displays a list of images and strings and issue
 * notification when selected.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TableItem</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, HIDE_SELECTION, VIRTUAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles SINGLE, and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Table extends Composite {
	TableItem [] items;
	TableColumn [] columns;
	TableItem currentItem;
	GC paintGC;
	int clickCount;
	int itemCount, columnCount, column_id, idCount, anchorFirst, anchorLast, headerHeight, lastIndexOf;
	boolean  ignoreSelect, wasSelected, fixScrollWidth;
	Rectangle imageBounds;
	int showIndex, lastHittest;
	static final int CHECK_COLUMN_ID = 1024;
	static final int COLUMN_ID = 1025;
	static final int EXTRA_WIDTH = 24;
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
 * @see SWT#FULL_SELECTION
 * @see SWT#HIDE_SELECTION
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Table (Composite parent, int style) {
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
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}

TableItem _getItem (int index) {
	if (items [index] != null) return items [index];
	return items [index] = new TableItem (this, SWT.NULL, -1, false);
}

int callPaintEventHandler (int control, int damageRgn, int visibleRgn, int theEvent, int nextHandler) {
	GC currentGC = paintGC;
	if (currentGC == null) {
		GCData data = new GCData ();
		data.paintEvent = theEvent;
		data.visibleRgn = visibleRgn;
		paintGC = GC.carbon_new (this, data);
	} 
	fixScrollWidth = false;
	int result = super.callPaintEventHandler (control, damageRgn, visibleRgn, theEvent, nextHandler);
	if (fixScrollWidth) {
		fixScrollWidth = false;
		if (setScrollWidth (items, true)) redraw ();
	}
	if (currentGC == null) {
		paintGC.dispose ();
		paintGC = null;
	}
	return result;
}

boolean checkData (TableItem item, boolean redraw) {
	if (item.cached) return true;
	if ((style & SWT.VIRTUAL) != 0) {
		item.cached = true;
		Event event = new Event ();
		event.item = item;
		currentItem = item;
		sendEvent (SWT.SetData, event);
		//widget could be disposed at this point
		currentItem = null;
		if (isDisposed () || item.isDisposed ()) return false;
		if (redraw) {
			if (!setScrollWidth (item)) item.redraw (OS.kDataBrowserNoItem);
		}
	}
	return true;
}

void checkItems (boolean setScrollWidth) {
	int [] count = new int [1];
	if (OS.GetDataBrowserItemCount (handle, OS.kDataBrowserNoItem, true, OS.kDataBrowserItemAnyState, count) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_COUNT);
	}
	if (itemCount != count [0]) {
		/*
		* Feature in the Mac. When AddDataBrowserItems() is used
		* to add items, item notification callbacks are issued with
		* the message kDataBrowserItemAdded.  When many items are
		* added, this is slow.  The fix is to temporarily remove
		* the item notification callback.
		*/
		DataBrowserCallbacks callbacks = new DataBrowserCallbacks ();
		OS.GetDataBrowserCallbacks (handle, callbacks);
		callbacks.v1_itemNotificationCallback = 0;
		OS.SetDataBrowserCallbacks (handle, callbacks);
		int delta = itemCount - count [0];
		if (delta < 1024) {
			int [] ids = new int [delta];
			for (int i=0; i<ids.length; i++) {
				ids [i] = count [0] + i + 1;
			}
			if (OS.AddDataBrowserItems (handle, OS.kDataBrowserNoItem, ids.length, ids, OS.kDataBrowserItemNoProperty) != OS.noErr) {
				error (SWT.ERROR_ITEM_NOT_ADDED);
			}
			OS.UpdateDataBrowserItems (handle, 0, 0, null, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
		} else {
			if (OS.AddDataBrowserItems (handle, 0, itemCount, null, OS.kDataBrowserItemNoProperty) != OS.noErr) {
				error (SWT.ERROR_ITEM_NOT_ADDED);
			}
		}
		callbacks.v1_itemNotificationCallback = display.itemNotificationProc;
		OS.SetDataBrowserCallbacks (handle, callbacks);
	}
	if (setScrollWidth) setScrollWidth (items, true);
}

static int checkStyle (int style) {
	/*
	* Feature in Windows.  It is not possible to create
	* a table that does not have scroll bars.  Therefore,
	* no matter what style bits are specified, set the
	* H_SCROLL and V_SCROLL bits so that the SWT style
	* will match the widget that Windows creates.
	*/
	style |= SWT.H_SCROLL | SWT.V_SCROLL;
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

/**
 * Clears the item at the given zero-relative index in the receiver.
 * The text, icon and other attributes of the item are set to the default
 * value.  If the table was created with the SWT.VIRTUAL style, these
 * attributes are requested again as needed.
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
		if (currentItem != item) {
			item.clear ();
			item.cached = false;
		}
		if (currentItem == null && drawCount == 0) {
			int [] id = new int [] {index + 1};
			OS.UpdateDataBrowserItems (handle, 0, id.length, id, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
		}
		setScrollWidth (item);
	}
}

/**
 * Removes the items from the receiver which are between the given
 * zero-relative start and end indices (inclusive).  The text, icon
 * and other attribues of the items are set to their default values.
 * If the table was created with the SWT.VIRTUAL style, these attributes
 * are requested again as needed.
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
 * The text, icon and other attribues of the items are set to their default
 * values.  If the table was created with the SWT.VIRTUAL style, these
 * attributes are requested again as needed.
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
 * attribues of the items are set to their default values. If the
 * table was created with the SWT.VIRTUAL style, these attributes
 * are requested again as needed.
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
		if (item != null) {
			item.clear ();
			item.cached = false;
		}
	}
	if (currentItem == null && drawCount == 0) {
		OS.UpdateDataBrowserItems (handle, 0, 0, null, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
	}
	setScrollWidth (items, true);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = 0;
	if (wHint == SWT.DEFAULT) {
		GC gc = new GC (this);
		int columnCount = Math.max (this.columnCount, 1);
		for (int j=0; j<columnCount; j++) {
			int columnWidth = this.columnCount != 0 ? columns [j].getWidth () : 0;
			for (int i=0; i<itemCount; i++) {
				TableItem item = items [i];
				if (item != null) {
					columnWidth = Math.max (columnWidth, item.calculateWidth (j, gc));
				}
			}
			width += columnWidth + EXTRA_WIDTH;
		}
		gc.dispose ();
		if ((style & SWT.CHECK) != 0) width += CHECK_COLUMN_WIDTH;
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
	column_id = COLUMN_ID;
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
	OS.CreateDataBrowserControl (window, null, OS.kDataBrowserListView, outControl);
	OS.SetAutomaticControlDragTrackingEnabledForWindow (window, true);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
	if (!drawFocusRing ()) {
		OS.SetControlData (handle, OS.kControlEntireControl, OS.kControlDataBrowserIncludesFrameAndFocusTag, 1, new byte[] {0});
	}
	int selectionFlags = (style & SWT.SINGLE) != 0 ? OS.kDataBrowserSelectOnlyOne | OS.kDataBrowserNeverEmptySelectionSet : OS.kDataBrowserCmdTogglesSelection;
	OS.SetDataBrowserSelectionFlags (handle, selectionFlags);
	short [] height = new short [1];
	OS.GetDataBrowserListViewHeaderBtnHeight (handle, height);
	headerHeight = height [0];
	OS.SetDataBrowserListViewHeaderBtnHeight (handle, (short) 0);
	OS.SetDataBrowserHasScrollBars (handle, (style & SWT.H_SCROLL) != 0, (style & SWT.V_SCROLL) != 0);
	int position = 0;
	if ((style & SWT.CHECK) != 0) {
		DataBrowserListViewColumnDesc checkColumn = new DataBrowserListViewColumnDesc ();
		checkColumn.headerBtnDesc_version = OS.kDataBrowserListViewLatestHeaderDesc;
		checkColumn.propertyDesc_propertyID = CHECK_COLUMN_ID;
		checkColumn.propertyDesc_propertyType = OS.kDataBrowserCheckboxType;
		checkColumn.propertyDesc_propertyFlags = OS.kDataBrowserPropertyIsMutable;
		//TODO - CHECK column size
		checkColumn.headerBtnDesc_minimumWidth = CHECK_COLUMN_WIDTH;
		checkColumn.headerBtnDesc_maximumWidth = CHECK_COLUMN_WIDTH;
		checkColumn.headerBtnDesc_initialOrder = (short) OS.kDataBrowserOrderIncreasing;
		OS.AddDataBrowserListViewColumn (handle, checkColumn, position++);
	}
	DataBrowserListViewColumnDesc column = new DataBrowserListViewColumnDesc ();
	column.headerBtnDesc_version = OS.kDataBrowserListViewLatestHeaderDesc;
	column.propertyDesc_propertyID = column_id;
	column.propertyDesc_propertyType = OS.kDataBrowserCustomType;
	column.propertyDesc_propertyFlags = OS.kDataBrowserListViewSelectionColumn | OS.kDataBrowserDefaultPropertyFlags | OS.kDataBrowserListViewSortableColumn;
	column.headerBtnDesc_maximumWidth = 0x7fff;
	column.headerBtnDesc_initialOrder = (short) OS.kDataBrowserOrderIncreasing;
	OS.AddDataBrowserListViewColumn (handle, column, position);
	OS.SetDataBrowserTableViewNamedColumnWidth (handle, column_id, (short) 0);

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
	OS.NewGWorldFromPtr (gWorld, OS.k32ARGBPixelFormat, rect, 0, 0, 0, data, bpl);
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

void createItem (TableColumn column, int index) {
	if (!(0 <= index && index <= columnCount)) error (SWT.ERROR_INVALID_RANGE);
	column.id = column_id + idCount++;
	int position = index + ((style & SWT.CHECK) != 0 ? 1 : 0);
	if (columnCount != 0) {
		DataBrowserListViewColumnDesc desc = new DataBrowserListViewColumnDesc ();
		desc.headerBtnDesc_version = OS.kDataBrowserListViewLatestHeaderDesc;
		desc.propertyDesc_propertyID = column.id;
		desc.propertyDesc_propertyType = OS.kDataBrowserCustomType;
		desc.propertyDesc_propertyFlags = OS.kDataBrowserListViewSelectionColumn | OS.kDataBrowserDefaultPropertyFlags | OS.kDataBrowserListViewSortableColumn;
		desc.headerBtnDesc_maximumWidth = 0x7fff;
		desc.headerBtnDesc_initialOrder = OS.kDataBrowserOrderIncreasing;
		desc.headerBtnDesc_btnFontStyle_just = OS.teFlushLeft;
		if ((style & SWT.CENTER) != 0) desc.headerBtnDesc_btnFontStyle_just = OS.teCenter;
		if ((style & SWT.RIGHT) != 0) desc.headerBtnDesc_btnFontStyle_just = OS.teFlushRight;
		desc.headerBtnDesc_btnFontStyle_flags |= OS.kControlUseJustMask;
		OS.AddDataBrowserListViewColumn (handle, desc, position);
		OS.SetDataBrowserTableViewNamedColumnWidth (handle, column.id, (short)0);
	} 
	if (columnCount == columns.length) {
		TableColumn [] newColumns = new TableColumn [columnCount + 4];
		System.arraycopy (columns, 0, newColumns, 0, columns.length);
		columns = newColumns;
	}
	System.arraycopy (columns, index, columns, index + 1, columnCount++ - index);
	columns [index] = column;
	if (columnCount >= 1) {
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
	int [] lastPosition = new int [1];
	for (int i=0; i<columnCount; i++) {
		TableColumn c = columns [i];
		OS.GetDataBrowserTableViewColumnPosition (handle, c.id, lastPosition);
		c.lastPosition = lastPosition [0];
	}
}

void createItem (TableItem item, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	boolean add = drawCount == 0 || index != itemCount;
	if (add) {
		int [] id = new int [] {itemCount + 1};
		if (OS.AddDataBrowserItems (handle, OS.kDataBrowserNoItem, 1, id, OS.kDataBrowserItemNoProperty) != OS.noErr) {
			error (SWT.ERROR_ITEM_NOT_ADDED);
		}
	}
	if (itemCount == items.length) {
		/* Grow the array faster when redraw is off */
		int length = drawCount == 0 ? items.length + 4 : Math.max (4, items.length * 3 / 2);
		TableItem [] newItems = new TableItem [length];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
	if (add) OS.UpdateDataBrowserItems (handle, 0, 0, null, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
}

ScrollBar createScrollBar (int style) {
	return createStandardBar (style);
}

void createWidget () {
	super.createWidget ();
	items = new TableItem [4];
	columns = new TableColumn [4];
	showIndex = -1;
}

Color defaultBackground () {
	return display.getSystemColor (SWT.COLOR_LIST_BACKGROUND);
}

Color defaultForeground () {
	return display.getSystemColor (SWT.COLOR_LIST_FOREGROUND);
}

int defaultThemeFont () {
	if (display.smallFonts) return OS.kThemeSmallSystemFont;
	return OS.kThemeViewsFont;
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
		int [] ids = new int [] {index + 1};
		deselect (ids, ids.length);
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
		if (length <= 0) return;
		int [] ids = new int [length];
		for (int i=0; i<length; i++) ids [i] = end - i + 1;
		deselect (ids, length);
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
	//TODO - check range
	int length = indices.length;
	int [] ids = new int [length];
	for (int i=0; i<length; i++) ids [i] = indices [length - i - 1] + 1;
	deselect (ids, length);
}

void deselect (int [] ids, int count) {
	ignoreSelect = true;
	int [] selectionFlags = null;
	if ((style & SWT.SINGLE) != 0) {
		selectionFlags = new int [1];
		OS.GetDataBrowserSelectionFlags (handle, selectionFlags);
		OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0] & ~OS.kDataBrowserNeverEmptySelectionSet);
	}
	OS.SetDataBrowserSelectedItems (handle, count, ids, OS.kDataBrowserItemsRemove);
	if ((style & SWT.SINGLE) != 0) {
		OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0]);
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
	deselect (null, 0);
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
		column_id = column.id; idCount = 0;
		DataBrowserListViewHeaderDesc desc = new DataBrowserListViewHeaderDesc ();
		desc.version = OS.kDataBrowserListViewLatestHeaderDesc;
		short [] width = new short [1];
		OS.GetDataBrowserTableViewNamedColumnWidth (handle, column_id, width);
		desc.minimumWidth = desc.maximumWidth = width [0];
		int str = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, null, 0);
		desc.titleString = str;
		OS.SetDataBrowserListViewHeaderDesc (handle, column_id, desc);
		OS.CFRelease (str);
	} else {
		if (OS.RemoveDataBrowserTableViewColumn (handle, column.id) != OS.noErr) {
			error (SWT.ERROR_ITEM_NOT_REMOVED);
		}
	}
	System.arraycopy (columns, index + 1, columns, index, --columnCount - index);
	columns [columnCount] = null;
}

void destroyItem (TableItem item) {
	checkItems (true);
	int index = 0;
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	int [] id = new int [] {itemCount};
	if (OS.RemoveDataBrowserItems (handle, OS.kDataBrowserNoItem, id.length, id, 0) != OS.noErr) {
		error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	OS.UpdateDataBrowserItems (handle, 0, 0, null, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
	if (itemCount == 0) setTableEmpty ();
}

int drawItemProc (int browser, int id, int property, int itemState, int theRect, int gdDepth, int colorDevice) {
	int index = id - 1;
	if (!(0 <= index && index < itemCount)) return OS.noErr;
	int columnIndex = 0;
	if (columnCount > 0) {
		for (columnIndex=0; columnIndex<columnCount; columnIndex++) {
			if (columns [columnIndex].id == property) break;
		}
		if (columnIndex == columnCount) return OS.noErr;
	}
	lastIndexOf = index;
	TableItem item = _getItem (index);
	if ((style & SWT.VIRTUAL) != 0) {
		if (!item.cached) {
			if (!checkData (item, false)) return OS.noErr;
			if (setScrollWidth (item)) {
				Rect rect = new Rect();
				if (OS.GetDataBrowserItemPartBounds (handle, id, property, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
					redrawWidget (handle, rect.left, rect.top, rect.right, rect.bottom, false);
				}
				return OS.noErr;
			}
		}
	}
	Rect rect = new Rect ();
	OS.memcpy (rect, theRect, Rect.sizeof);
	int x = rect.left;
	int y = rect.top;
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	boolean selected = (itemState & OS.kDataBrowserItemIsSelected) != 0;
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
	Rect itemRect = new Rect();
	OS.GetDataBrowserItemPartBounds (handle, id, property, OS.kDataBrowserPropertyEnclosingPart, itemRect);
	OS.OffsetRect (itemRect, (short) -controlRect.left, (short) -controlRect.top);
	if (selected && (style & SWT.FULL_SELECTION) != 0) {
		gc.setBackground (display.getSystemColor (SWT.COLOR_LIST_SELECTION));
		gc.fillRectangle(itemRect.left, itemRect.top, itemRect.right - itemRect.left, itemRect.bottom - itemRect.top);
	} else {
		gc.setBackground (item.getBackground (columnIndex));
		gc.fillRectangle (itemRect.left, itemRect.top, itemRect.right - itemRect.left, itemRect.bottom - itemRect.top);
	}
	int rectRgn = OS.NewRgn ();
	OS.RectRgn (rectRgn, rect);
	OS.OffsetRgn (rectRgn, (short)-controlRect.left, (short)-controlRect.top);
	OS.SectRgn (rectRgn, clip, clip);
	OS.DisposeRgn (rectRgn);
	gc.setClipping (Region.carbon_new (display, clip));
	OS.DisposeRgn (clip);
	Image image = item.getImage (columnIndex);
	String text = item.getText (columnIndex);
	gc.setFont (item.getFont (columnIndex));
	Point extent = gc.stringExtent (text);
	int itemWidth = extent.x;
	Rectangle imageBounds = null;
	if (image != null) {
		imageBounds = image.getBounds ();
		itemWidth += this.imageBounds.width + 2;
	}
	if (columnCount != 0) {
		TableColumn column = columns [columnIndex];
		if ((column.style & SWT.CENTER) != 0) x += (width - itemWidth) / 2;
		if ((column.style & SWT.RIGHT) != 0) x += width - itemWidth;
	}
	if (image != null) {
		gc.drawImage (image, 0, 0, imageBounds.width, imageBounds.height, x, y + (height - this.imageBounds.height) / 2, this.imageBounds.width, this.imageBounds.height);
		x += this.imageBounds.width + 2;
	}
	if (selected) {
		gc.setForeground (display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT));
		if (columnIndex == 0 && (style & SWT.FULL_SELECTION) == 0) {
			gc.setBackground (display.getSystemColor (SWT.COLOR_LIST_SELECTION));
			gc.fillRectangle (x - 1, y, extent.x + 2, height);
		}
	} else {
		Color foreground = item.getForeground (columnIndex);
		gc.setForeground (foreground);
	}
	gc.drawString (text, x, y + (height - extent.y) / 2, true);
	if (gc != paintGC) gc.dispose ();
	return OS.noErr;
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
 * Returns the column at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
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
		OS.GetDataBrowserTableViewColumnPosition (handle, column.id, position);
		if ((style & SWT.CHECK) != 0) position [0] -= 1;
		order [position [0]] = i;
	}
	return order;
}

/**
 * Returns an array of <code>TableColumn</code>s which are the
 * columns in the receiver. If no <code>TableColumn</code>s were
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
	short [] height = new short [1];
	OS.GetDataBrowserListViewHeaderBtnHeight (handle, height);
	return height [0];
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
	short [] height = new short [1];
	OS.GetDataBrowserListViewHeaderBtnHeight (handle, height);
	return height [0] != 0;
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
public TableItem getItem (Point point) {
	checkWidget ();
	checkItems (true);
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	Rect rect = new Rect ();
	OS.GetControlBounds (handle, rect);
	org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
	OS.SetPt (pt, (short) (point.x + rect.left), (short) (point.y + rect.top));
	//TODO - optimize
	int columnId = (columnCount == 0) ? column_id : columns [0].id;
	for (int i=0; i<itemCount; i++) {
		if (OS.GetDataBrowserItemPartBounds (handle, i + 1, columnId, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
			if ((style & SWT.FULL_SELECTION) != 0) {
				if (rect.top <= pt.v && pt.v < rect.bottom) return _getItem (i);
			} else {
				if (OS.PtInRect (pt, rect)) return _getItem (i);
			}
		}
	}
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
	short [] height = new short [1];
	if (OS.GetDataBrowserTableViewRowHeight (handle, height) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_ITEM_HEIGHT);
	}
	return height [0];
}

/**
 * Returns an array of <code>TableItem</code>s which are the items
 * in the receiver. 
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
	return false;
}

/**
 * Returns an array of <code>TableItem</code>s that are currently
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
public TableItem [] getSelection () {
	checkWidget ();
	int ptr = OS.NewHandle (0);
	if (OS.GetDataBrowserItems (handle, OS.kDataBrowserNoItem, true, OS.kDataBrowserItemIsSelected, ptr) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_SELECTION);
	}
	int count = OS.GetHandleSize (ptr) / 4;
	TableItem [] result = new TableItem [count];
	OS.HLock (ptr);
	int [] start = new int [1];
	OS.memcpy (start, ptr, 4);
	int [] id = new int [1];
	for (int i=0; i<count; i++) {
		OS.memcpy (id, start [0] + (i * 4), 4);
		result [i] = _getItem (id [0] - 1);
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
	int [] first = new int [1], last = new int [1];
	if (OS.GetDataBrowserSelectionAnchor (handle, first, last) != OS.noErr) return -1;
    return first [0] - 1;
}

/**
 * Returns the zero-relative indices of the items which are currently
 * selected in the receiver.  The array is empty if no items are selected.
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
	int ptr = OS.NewHandle (0);
	if (OS.GetDataBrowserItems (handle, OS.kDataBrowserNoItem, true, OS.kDataBrowserItemIsSelected, ptr) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_SELECTION);
	}
	int count = OS.GetHandleSize (ptr) / 4;
	int [] result = new int [count];
	OS.HLock (ptr);
	int [] start = new int [1];
	OS.memcpy (start, ptr, 4);
	int [] id = new int [1];
	for (int i=0; i<count; i++) {
		OS.memcpy (id, start [0] + (i * 4), 4);
		result [i] = id [0] - 1;
	}
	OS.HUnlock (ptr);
	OS.DisposeHandle (ptr);
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
    int[] top = new int [1], left = new int [1];
    OS.GetDataBrowserScrollPosition (handle, top, left);
    return top [0] / getItemHeight ();
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
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
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
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
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
 * @return the visibility state of the item at the index
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isSelected (int index) {
	checkWidget();
	return OS.IsDataBrowserItemSelected (handle, index + 1);
}

int itemDataProc (int browser, int id, int property, int itemData, int setValue) {
	int row = id - 1;
	if (!(0 <= row && row < items.length)) return OS.noErr;
	switch (property) {
		case CHECK_COLUMN_ID: {
			TableItem item = _getItem (row);
			if (setValue != 0) {
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
				int theData = OS.kThemeButtonOff;
				if (item.checked) theData = item.grayed ? OS.kThemeButtonMixed : OS.kThemeButtonOn;
				OS.SetDataBrowserItemDataButtonValue (itemData, (short) theData);
			}
			break;
		}
	}
	return OS.noErr;
}

int itemNotificationProc (int browser, int id, int message) {
	if (message == OS.kDataBrowserUserStateChanged) {
		short [] width = new short [1];
		int [] position = new int [1];
		TableColumn [] columns = getColumns ();
		for (int i = 0; i < columnCount; i++) {
			TableColumn column = columns [i];
			if (!column.isDisposed ()) {
				OS.GetDataBrowserTableViewNamedColumnWidth (handle, column.id, width);
				if (width [0] != column.lastWidth) {
					column.resized (width [0]);
				}
			}
			if (!column.isDisposed ()) {
				OS.GetDataBrowserTableViewColumnPosition (handle, column.id, position);
				if (position [0] != column.lastPosition) {
					column.lastPosition = position [0];
					column.sendEvent (SWT.Move);
				}
			}
		}
		
		int [] property = new int [1];
		OS.GetDataBrowserSortProperty (handle, property);
		if (property [0] != 0) {
			for (int i = 0; i < columnCount; i++) {
				TableColumn column = columns [i];
				if (property [0] == column.id) {
					column.postEvent (clickCount == 2 ? SWT.DefaultSelection : SWT.Selection);
					break;
				}
			}
			OS.SetDataBrowserSortProperty (handle, 0);
		}
		return OS.noErr;
	}
	int index = id - 1;
	if (!(0 <= index && index < items.length)) return OS.noErr;
	switch (message) {
		case OS.kDataBrowserItemSelected:
		case OS.kDataBrowserItemDeselected: {
			TableItem item = _getItem (index);
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
			TableItem item = _getItem (index);
			wasSelected = true;
			Event event = new Event ();
			event.item = item;
			postEvent (SWT.DefaultSelection, event);
			break;
		}
	}
	return OS.noErr;
}

int kEventMouseDown (int nextHandler, int theEvent, int userData) {
	int [] outData = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamClickCount, OS.typeUInt32, null, 4, null, outData);
	clickCount = outData [0];
	int result = super.kEventMouseDown (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	Shell shell = getShell ();
	shell.bringToTop (true);
	/*
	* Feature in the Macintosh.  For some reason, when the user
	* clicks on the data browser, focus is assigned, then lost
	* and then reassigned causing kEvenControlSetFocusPart events.
	* The fix is to ignore kEvenControlSetFocusPart when the user
	* clicks and send the focus events from kEventMouseDown.
	*/
	Control oldFocus = display.getFocusControl ();
	display.ignoreFocus = true;
	wasSelected = false;
	result = OS.CallNextEventHandler (nextHandler, theEvent);
	display.ignoreFocus = false;
	if (oldFocus != this) {
		if (oldFocus != null && !oldFocus.isDisposed ()) oldFocus.sendFocusEvent (SWT.FocusOut, false);
		if (!isDisposed () && isEnabled ()) sendFocusEvent (SWT.FocusIn, false);
	}
	if (!wasSelected) {
		if (OS.IsDataBrowserItemSelected (handle, lastHittest)) {
			int index = lastHittest - 1;
			if (0 <= index && index < itemCount) {
				Event event = new Event ();
				event.item = _getItem (index);
				postEvent (SWT.Selection, event);
			}
		}
	}
	return result;
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
		/*
		* Feature in the Macintosh.  For some reason, when the user hits an
		* up or down arrow to traverse the items in a Data Browser, the item
		* scrolls to the left such that the white space that is normally
		* visible to the right of the every item is scrolled out of view.
		* The fix is to do the arrow traversal in Java and not call the
		* default handler.
		*/
		case 125: { /* Down */
			int index = getSelectionIndex ();
			setSelection (Math.min (itemCount - 1, index + 1), true);
			return OS.noErr;
		}
		case 126: { /* Up*/
			int index = getSelectionIndex ();
			setSelection (Math.max (0, index - 1), true);
			return OS.noErr;
		}
	}
	return result;
}

void releaseWidget () {
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
		if (item != null && !item.isDisposed ()) item.releaseResources ();
	}
	items = null;
	for (int i=0; i<columnCount; i++) {
		TableColumn column = columns [i];
		if (!column.isDisposed ()) column.releaseResources ();
	}
	columns = null;
	super.releaseWidget ();
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
	checkItems (true);
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	int [] id = new int [] {itemCount};
	if (OS.RemoveDataBrowserItems (handle, OS.kDataBrowserNoItem, id.length, id, 0) != OS.noErr) {
		error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	TableItem item = items [index];
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	if (item != null) item.releaseResources ();
	OS.UpdateDataBrowserItems (handle, 0, 0, null, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
	if (itemCount == 0) setTableEmpty ();
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
 * <p>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget();
	/*
	* Feature in the Mac. When RemoveDataBrowserItems() is used
	* to remove items, item notification callbacks are issued with
	* the message kDataBrowserItemRemoved  When many items are
	* removed, this is slow.  The fix is to temporarily remove
	* the item notification callback.
	*/
	DataBrowserCallbacks callbacks = new DataBrowserCallbacks ();
	OS.GetDataBrowserCallbacks (handle, callbacks);
	callbacks.v1_itemNotificationCallback = 0;
	OS.SetDataBrowserCallbacks (handle, callbacks);
	OS.RemoveDataBrowserItems (handle, OS.kDataBrowserNoItem, 0, null, 0);
	callbacks.v1_itemNotificationCallback = display.itemNotificationProc;
	OS.SetDataBrowserCallbacks (handle, callbacks);
	OS.SetDataBrowserScrollPosition (handle, 0, 0);
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
		if (item != null && !item.isDisposed ()) item.releaseResources ();
	}
	setTableEmpty ();
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
 * @see #addSelectionListener(SelectionListener)
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

void resetVisibleRegion (int control) {
	super.resetVisibleRegion (control);
	if (showIndex != -1) {
		showIndex (showIndex);
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
	checkWidget();
	checkItems (false);
	if (0 <= index && index < itemCount) {
		int [] ids = new int [] {index + 1};
		select (ids, ids.length, false);
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
 * @see Table#setSelection(int,int)
 */
public void select (int start, int end) {
	checkWidget ();
	checkItems (false);
	if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) return;
	if (itemCount == 0 || start >= itemCount) return;
	if (start == 0 && end == itemCount - 1) {
		selectAll ();
	} else {
		start = Math.max (0, start);
		end = Math.min (end, itemCount - 1);
		int length = end - start + 1;
		int [] ids = new int [length];
		for (int i=0; i<length; i++) ids [i] = end - i + 1;
		select (ids, length, false);
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
 * @see Table#setSelection(int[])
 */
public void select (int [] indices) {
	checkWidget ();
	checkItems (false);
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = indices.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	int [] ids = new int [length];
	int count = 0;
	for (int i=0; i<length; i++) {
		int index = indices [length - i - 1];
		if (index >= 0 && index < itemCount) {
			ids [count++] = index + 1;
		}
	}
	if (count > 0) select (ids, count, false);
}

void select (int [] ids, int count, boolean clear) {
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
	int operation = OS.kDataBrowserItemsAssign;
	if ((style & SWT.MULTI) != 0 && !clear) operation = OS.kDataBrowserItemsAdd;
	OS.SetDataBrowserSelectedItems (handle, count, ids, operation);
	if ((style & SWT.SINGLE) != 0) {
		OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0]);
	}
	ignoreSelect = false;
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
	checkItems (false);
	if ((style & SWT.SINGLE) != 0) return;
	select (null, 0, false);
}

void setBackground (float [] color) {
	/*
	* Bug in the Macintosh.  The default background of a window changes when
	* the background of a data browser is set using SetControlFontStyle().  This
	* also affects the background of any TNXObject created on that window.  The
	* fix is to avoid calling SetControlFontStyle() which has no effect
	* in a data browser anyways.
	*/
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	/*
	* Ensure that the top item is visible when the tree is resized
	* from a zero size to a size that can show the selection.
	*/
	int result = super.setBounds (x, y, width, height, move, resize, events);
	if (showIndex != -1) {
		showIndex (showIndex);
	}
	return result;
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
			OS.GetDataBrowserTableViewNamedColumnWidth(handle, column.id, width);
			x += width [0];
		}
		x = 0;
		int [] newX = new int [order.length];
		for (int i=0; i<order.length; i++) {
			int index = order [i];
			TableColumn column = columns [index];
			int position = (style & SWT.CHECK) != 0 ? i + 1 : i;
			OS.SetDataBrowserTableViewColumnPosition(handle, column.id, position);
			column.lastPosition = position;
			newX [index] =  x;
			OS.GetDataBrowserTableViewNamedColumnWidth(handle, column.id, width);
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

void setFontStyle (Font font) {
	super.setFontStyle (font);
	if (items == null) return;
	for (int i = 0; i < items.length; i++) {
		TableItem item = items [i];
		if (item != null) item.width = -1;
	}
	setScrollWidth (items, true);
	setItemHeight (null);
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
	int height = show ? headerHeight : 0;
	OS.SetDataBrowserListViewHeaderBtnHeight (handle, (short)height);
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
	checkItems (true);
	count = Math.max (0, count);
	if (count == itemCount) return;
	setRedraw (false);
    int[] top = new int [1], left = new int [1];
    OS.GetDataBrowserScrollPosition (handle, top, left);
    DataBrowserCallbacks callbacks = new DataBrowserCallbacks ();
	OS.GetDataBrowserCallbacks (handle, callbacks);
	callbacks.v1_itemNotificationCallback = 0;
	OS.SetDataBrowserCallbacks (handle, callbacks);
	if (count < itemCount) {
		int index = count;
		while (index < itemCount) {
			int [] id = new int [] {index + 1};
			if (OS.RemoveDataBrowserItems (handle, OS.kDataBrowserNoItem, id.length, id, 0) != OS.noErr) {
				break;
			}
			TableItem item = items [index];
			if (item != null) item.releaseResources ();
			index++;
		}
		if (index < itemCount) error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	int length = Math.max (4, (count + 3) / 4 * 4);
	TableItem [] newItems = new TableItem [length];
	System.arraycopy (items, 0, newItems, 0, Math.min (count, itemCount));
	items = newItems;
	if ((style & SWT.VIRTUAL) == 0) {
		for (int i=count; i<itemCount; i++) {
			items [i] = new TableItem (this, SWT.NONE, i, false);
		}
	}
	itemCount = count;
	OS.AddDataBrowserItems (handle, 0, itemCount, null, OS.kDataBrowserItemNoProperty);
	callbacks.v1_itemNotificationCallback = display.itemNotificationProc;
	OS.SetDataBrowserCallbacks (handle, callbacks);
	setRedraw (true);
}

void setItemHeight (Image image) {
	Rectangle bounds = image != null ? image.getBounds () : imageBounds;
	if (bounds == null) return;
	imageBounds = bounds;
	short [] height = new short [1];
	if (OS.GetDataBrowserTableViewRowHeight (handle, height) == OS.noErr) {
		if (height [0] < bounds.height) {
			OS.SetDataBrowserTableViewRowHeight (handle, (short) bounds.height);
		}
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
}

public void setRedraw (boolean redraw) {
	checkWidget();
	super.setRedraw (redraw);
	if (redraw && drawCount == 0) {
	 	/* Resize the item array to match the item count */
		if (items.length > 4 && items.length - itemCount > 3) {
			int length = Math.max (4, (itemCount + 3) / 4 * 4);
			TableItem [] newItems = new TableItem [length];
			System.arraycopy (items, 0, newItems, 0, itemCount);
			items = newItems;
		}		
	 	checkItems (true);
	}
}

boolean setScrollWidth (TableItem item) {
	if (columnCount != 0) return false;
	if (currentItem != null) {
		if (currentItem != item) fixScrollWidth = true;
		return false;
	}
	if (drawCount != 0) return false;
	GC gc = new GC (this);
	int newWidth = item.calculateWidth (0, gc);
	gc.dispose ();
	newWidth += EXTRA_WIDTH;
	short [] width = new short [1];
	OS.GetDataBrowserTableViewNamedColumnWidth (handle, column_id, width);
	if (width [0] < newWidth) {
		OS.SetDataBrowserTableViewNamedColumnWidth (handle, column_id, (short) newWidth);
		return true;
	}
	return false;
}

boolean setScrollWidth (TableItem [] items, boolean set) {
	if (columnCount != 0) return false;
	if (currentItem != null) {
		fixScrollWidth = true;
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
	newWidth += EXTRA_WIDTH;
	if (!set) {
		short [] width = new short [1];
		OS.GetDataBrowserTableViewNamedColumnWidth (handle, column_id, width);
		if (width [0] >= newWidth) return false;
	}
	OS.SetDataBrowserTableViewNamedColumnWidth (handle, column_id, (short) newWidth);
	return true;
}

/**
 * Selects the item at the given zero-relative index in the receiver. 
 * The current selected is first cleared, then the new item is selected.
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
	checkItems (false);
	deselectAll ();
	setSelection (index, false);
}

void setSelection (int index, boolean notify) {
//	checkWidget();
	if (0 <= index && index < itemCount) {
		int [] ids = new int [] {index + 1};
		select (ids, ids.length, true);
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
	checkItems (false);
	deselectAll ();
	if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) return;
	if (itemCount == 0 || start >= itemCount) return;
	start = Math.max (0, start);
	end = Math.min (end, itemCount - 1);
	int length = end - start + 1;
	int [] ids = new int [length];
	for (int i=0; i<length; i++) ids [i] = end - i + 1;
	select (ids, length, true);
	showIndex (ids [0] - 1);
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
 * @see Table#deselectAll()
 * @see Table#select(int[])
 */
public void setSelection (int [] indices) {
	checkWidget ();
	checkItems (false);
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	deselectAll ();
	int length = indices.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	int [] ids = new int [length];
	int count = 0;
	for (int i=0; i<length; i++) {
		int index = indices [length - i - 1];
		if (index >= 0 && index < itemCount) {
			ids [count++] = index + 1;
		}
	}
	if (count > 0) {
		select (ids, count, true);
		showIndex (ids [0] - 1);
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
	checkItems (false);
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	deselectAll ();
	int length = items.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	int [] ids = new int [length];
	int count = 0;
	for (int i=0; i<length; i++) {
		int index = indexOf (items [length - i - 1]);
		if (index != -1) {
			ids [count++] = index + 1;
		}
	}
	if (count > 0) {
		select (ids, count, true);
		showIndex (ids [0] - 1);
	}
}

void setTableEmpty () {
	itemCount = anchorFirst = anchorLast = 0;
	items = new TableItem [4];
	if (imageBounds != null) {
		/* Reset the item height to the font height */
		imageBounds = null;
		setFontStyle (font);
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
	checkItems (false);
    int [] top = new int [1], left = new int [1];
    OS.GetDataBrowserScrollPosition (handle, top, left);
    top [0] = index * getItemHeight ();
    OS.SetDataBrowserScrollPosition (handle, top [0], left [0]);
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
 * @since 3.0
 */
public void showColumn (TableColumn column) {
	checkWidget ();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (column.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (column.parent != this) return;
	int index = indexOf (column);
	if (columnCount <= 1 || !(0 <= index && index < columnCount)) return;
	// Get width and horizontal position of column
	short [] w = new short [1];
	OS.GetDataBrowserTableViewNamedColumnWidth (handle, column.id, w);
	int width = w [0];
	int x = 0;
	for (int i = 0; i < index; i++) {
		w = new short [1];
		OS.GetDataBrowserTableViewNamedColumnWidth (handle, columns[i].id, w);
		x += w [0];
	}
	// Get current scroll position
	int [] top = new int [1], left = new int [1];
	OS.GetDataBrowserScrollPosition (handle, top, left);
	// Scroll column into view
	if (x < left[0]) {
		OS.SetDataBrowserScrollPosition(handle, top [0], x);
	} else {
		Rectangle rect = getClientArea ();
		int maxWidth = rect.width;
		width = Math.min(width, maxWidth);
		if (x + width > left [0] + maxWidth) {
			left [0] = x + width - maxWidth;
			OS.SetDataBrowserScrollPosition(handle, top [0], left [0]);
		}
	}
}

void showIndex (int index) {
	if (0 <= index && index < itemCount) {
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
			showIndex = index;
			return;
		}
		showIndex = -1;
		TableItem item = _getItem (index);
		Rectangle itemRect = item.getBounds (0);
		if (!itemRect.isEmpty()) {
			if (rect.contains (itemRect.x, itemRect.y)
				&& rect.contains (itemRect.x, itemRect.y + itemRect.height)) return;
		}
		int [] top = new int [1], left = new int [1];
		OS.GetDataBrowserScrollPosition (handle, top, left);
		OS.RevealDataBrowserItem (handle, index + 1, OS.kDataBrowserNoItem, (byte) OS.kDataBrowserRevealWithoutSelecting);

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
	checkItems (false);
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
	checkItems (false);
	int index = getSelectionIndex ();
	if (index >= 0) showIndex (index);
}

int trackingProc (int browser, int id, int property, int theRect, int startPt, int modifiers) {
	return 1;
}

}
