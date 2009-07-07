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

 
import org.eclipse.swt.internal.carbon.CFRange;
import org.eclipse.swt.internal.carbon.DataBrowserAccessibilityItemInfo;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.DataBrowserCallbacks;
import org.eclipse.swt.internal.carbon.DataBrowserCustomCallbacks;
import org.eclipse.swt.internal.carbon.DataBrowserListViewColumnDesc;
import org.eclipse.swt.internal.carbon.DataBrowserListViewHeaderDesc;
import org.eclipse.swt.internal.carbon.HMHelpContentRec;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.CGPoint;

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
	TreeItem [] items;
	TreeColumn [] columns;
	TreeColumn sortColumn;
	int [] childIds;
	GC paintGC;
	int sortDirection;
	int columnCount, column_id, idCount, anchorFirst, anchorLast, savedAnchor, headerHeight;
	boolean ignoreRedraw, ignoreSelect, wasSelected, ignoreExpand, wasExpanded, inClearAll, drawBackground;
	Rectangle imageBounds;
	TreeItem showItem;
	TreeItem insertItem;
	boolean insertBefore;
	int lastHittest, lastHittestColumn, visibleCount;
	static final int CHECK_COLUMN_ID = 1024;
	static final int COLUMN_ID = 1025;
	static final int GRID_WIDTH = 1;
	static final int ICON_AND_TEXT_GAP = 4;
	static final int CELL_CONTENT_INSET = 12;
	static final int BORDER_INSET = 1;
	static final int DISCLOSURE_COLUMN_EDGE_INSET = 8;
	static final int DISCLOSURE_COLUMN_LEVEL_INDENT = 24;
	static final int DISCLOSURE_TRIANGLE_AND_CONTENT_GAP = 8;
	static final String [] AX_ATTRIBUTES = {
		OS.kAXTitleAttribute,
	};

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
	for (int i = 0; i < items.length; i++) {
		if (items [i] != null) items [i].width = -1;		
	}
}

int _getId () {
	return _getIds (1) [0];
}

int [] _getIds (int count) {
	int [] newIds = new int [count];
	int index = 0;
	if ((style & SWT.VIRTUAL) == 0) {
		for (int i=0; i<items.length; i++) {
			if (items [i] == null) {
				newIds [index++] = i + 1;
				if (index == count) return newIds;
			}
		}
		int next = items.length;
		while (index < count) {
			newIds [index++] = next + 1;
			next++;
		}
		return newIds;
	}
	
	boolean [] reserved = new boolean [items.length];
	if (childIds != null) {
		for (int i=0; i<childIds.length; i++) {
			int usedId = childIds [i]; 
			if (usedId != 0) {
				if (usedId > reserved.length) {
					boolean [] newReserved = new boolean [usedId + 4];
					System.arraycopy (reserved, 0, newReserved, 0, reserved.length);
					reserved = newReserved;
				}
				reserved [usedId - 1] = true;
			}
		}
	}
	for (int i=0; i<items.length; i++) {
		if (items [i] != null) {
			reserved [i] = true;
			int [] ids =  items [i].childIds;
			if (ids != null) {
				for (int j=0; j<ids.length; j++) {
					int usedId = ids [j];
					if (usedId != 0) {
						if (usedId > reserved.length) {
							boolean [] newReserved = new boolean [usedId + 4];
							System.arraycopy (reserved, 0, newReserved, 0, reserved.length);
							reserved = newReserved;
						}
						reserved [usedId - 1] = true;
					}
				}
			}
		}
	}
	for (int i=0; i<reserved.length; i++) {
		if (!reserved [i]) {
			newIds [index++] = i + 1;
			if (index == count) return newIds;
		}
	}
	int next = reserved.length;
	while (index < count) {
		newIds [index++] = next + 1;
		next++;
	}
	return newIds;
}

TreeItem _getItem (int id, boolean create) {
	if (id < 1) return null;
	TreeItem item = id - 1 < items.length ? items [id - 1] : null;
	if (item != null ||  (style & SWT.VIRTUAL) == 0 || !create) return item;
	if (childIds != null) {
		for (int i=0; i<childIds.length; i++) {
			if (childIds [i] == id) {
				return _getItem (null, i);
			}
		}
	}
	for (int i=0; i<items.length; i++) {
		TreeItem parentItem = items [i];
		if (parentItem != null && parentItem.childIds != null) {
			int [] ids = parentItem.childIds;
			for (int j=0; j<ids.length; j++) {
				if (ids [j] == id) {
					return _getItem (parentItem, j);
				}
			}
		}
	}
	return null;
}

TreeItem _getItem (TreeItem parentItem, int index) {
	int count = getItemCount (parentItem);
	if (index < 0 || index >= count) return null;
	int [] ids = parentItem == null ? childIds : parentItem.childIds;
	int id = ids [index];
	if (id == 0) {
		id = _getId ();
		ids [index] = id;
	}
	if (id > items.length) {
		TreeItem [] newItems = new TreeItem [id + 4];
		System.arraycopy(items, 0, newItems, 0, items.length);
		items = newItems;
	}
	TreeItem item = items [id - 1]; 
	if (item != null || (style & SWT.VIRTUAL) == 0) return item;
	item = new TreeItem (this, parentItem, SWT.NONE, index, false);
	item.id = id;
	items [id - 1] = item;
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

int calculateWidth (int [] ids, GC gc, boolean recurse, int level, int levelIndent) {
	if (ids == null) return 0;
	int width = 0;
	for (int i=0; i<ids.length; i++) {
		TreeItem item = _getItem (ids [i], false);
		if (item != null) {
			int itemWidth = item.calculateWidth (0, gc);
			itemWidth += level * levelIndent;
			width = Math.max (width, itemWidth);
			if (recurse && item._getExpanded ()) {
				width = Math.max (width, calculateWidth (item.childIds, gc, recurse, level + 1, levelIndent));
			}
		}
	}
	return width;
}

int callPaintEventHandler (int control, int damageRgn, int visibleRgn, int theEvent, int nextHandler) {
	/*
	* Feature in the Macintosh.  The draw item proc is not called if the column width
	* is zero. This means that the SWT.MeasureItem listener is not called and the column
	* does not get wider ever.  The fix is to change the column width to one.
	*/
	if (columnCount == 0 && (hooks (SWT.MeasureItem) || hooks (SWT.EraseItem) || hooks (SWT.PaintItem))) {
		short [] width = new short [1];
		OS.GetDataBrowserTableViewNamedColumnWidth (handle, column_id, width);
		if (width [0] == 0) {
			OS.SetDataBrowserTableViewNamedColumnWidth (handle, column_id, (short) 1);
		}
	}
	GC currentGC = paintGC;
	if (currentGC == null) {
		GCData data = new GCData ();
		data.paintEvent = theEvent;
		data.visibleRgn = visibleRgn;
		paintGC = GC.carbon_new (this, data);
	}
	drawBackground = findBackgroundControl () != null;
	int result = super.callPaintEventHandler (control, damageRgn, visibleRgn, theEvent, nextHandler);
	if (getItemCount () == 0 && drawBackground) {
		drawBackground = false;
		Rectangle rect = getClientArea ();
		int headerHeight = getHeaderHeight ();
		rect.y += headerHeight;
		rect.height -= headerHeight;
		fillBackground (handle, paintGC.handle, rect);
	}
	if (insertItem != null && !insertItem.isDisposed()) {
		Rectangle itemRect = insertItem.getImageBounds(0).union(insertItem.getBounds());
		Rectangle clientRect = getClientArea();
		int x = clientRect.x + clientRect.width;
		int posY = insertBefore ? itemRect.y : itemRect.y + itemRect.height - 1;
		paintGC.drawLine(itemRect.x, posY, x, posY);
	}
	if (currentGC == null) {
		paintGC.dispose ();
		paintGC = null;
	}
	return result;
}

boolean checkData (TreeItem item, boolean redraw) {
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
		if (redraw) {
			if (!setScrollWidth (item)) item.redraw (OS.kDataBrowserNoItem);
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
	int [] ids = parentItem == null ? childIds : parentItem.childIds;
	TreeItem item = _getItem (ids [index], false);
	if (item != null) {
		item.clear();
		if (all) {
			clearAll (item, true);
		} else {
			int container = parentItem == null ? OS.kDataBrowserNoItem : parentItem.id;
			OS.UpdateDataBrowserItems (handle, container, 1, new int[] {item.id}, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
		}
	}
}

void clearAll (TreeItem parentItem, boolean all) {
	boolean update = !inClearAll;
	int count = getItemCount (parentItem);
	if (count == 0) return;
	inClearAll = true;
	int [] ids = parentItem == null ? childIds : parentItem.childIds;
	for (int i=0; i<count; i++) {
		TreeItem item = _getItem (ids [i], false);
		if (item != null) {
			item.clear ();
			if (all) clearAll (item, true);
		}
	}
	if (update) {
		OS.UpdateDataBrowserItems (handle, 0, 0, null, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
		inClearAll = false;
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
	int count = getItemCount (null);
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
	int width = 0;
	if (wHint == SWT.DEFAULT) {
		if (columnCount != 0) {
			for (int i=0; i<columnCount; i++) {
				width += columns [i].getWidth ();
			}
		} else {
			int levelIndent = DISCLOSURE_COLUMN_LEVEL_INDENT;
			if (OS.VERSION >= 0x1040) {
				float [] metric = new float [1];
				OS.DataBrowserGetMetric (handle, OS.kDataBrowserMetricDisclosureColumnPerDepthGap, null, metric);
				levelIndent = (int) metric [0];
			}
			GC gc = new GC (this);
			width = calculateWidth (childIds, gc, true, 0, levelIndent);
			gc.dispose ();
			width += getInsetWidth (column_id, true);
		}
		if ((style & SWT.CHECK) != 0) width += getCheckColumnWidth ();
	} else {
		width = wHint;
	}
	if (width <= 0) width = DEFAULT_WIDTH;
	int height = 0;
	if (hHint == SWT.DEFAULT) {
		height = visibleCount * getItemHeight () + getHeaderHeight();
	} else {
		height = hHint;
	}
	if (height <= 0) height = DEFAULT_HEIGHT;
	Rectangle rect = computeTrim (0, 0, width, height);
	return new Point (rect.width, rect.height);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	int border = getBorderWidth ();
	Rect rect = new Rect ();
	OS.GetDataBrowserScrollBarInset (handle, rect);
	x -= rect.left + border;
	y -= rect.top + border;
	width += rect.left + rect.right + border + border;
	height += rect.top + rect.bottom + border + border;
	return new Rectangle (x, y, width, height);
}

boolean contains(int shellX, int shellY) {
	CGPoint pt = new CGPoint ();
	int [] contentView = new int [1];
	OS.HIViewFindByID (OS.HIViewGetRoot (OS.GetControlOwner (handle)), OS.kHIViewWindowContentID (), contentView);
	OS.HIViewConvertPoint (pt, handle, contentView [0]);
	int x = shellX - (int) pt.x;
	int y = shellY - (int) pt.y;
	if (y < getHeaderHeight ()) return false;
	return getClientArea ().contains (x, y);
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
	if (OS.VERSION >= 0x1040) {
		int inset = 4;
		OS.DataBrowserSetMetric (handle, OS.kDataBrowserMetricCellContentInset, false, inset);
		OS.DataBrowserSetMetric (handle, OS.kDataBrowserMetricDisclosureColumnEdgeInset, false, inset);		
		OS.DataBrowserSetMetric (handle, OS.kDataBrowserMetricDisclosureTriangleAndContentGap, false, inset);
		OS.DataBrowserSetMetric (handle, OS.kDataBrowserMetricIconAndTextGap, false, inset);
	}
	int position = 0;
	if ((style & SWT.CHECK) != 0) {
		DataBrowserListViewColumnDesc checkColumn = new DataBrowserListViewColumnDesc ();
		checkColumn.headerBtnDesc_version = OS.kDataBrowserListViewLatestHeaderDesc;
		checkColumn.propertyDesc_propertyID = CHECK_COLUMN_ID;
		checkColumn.propertyDesc_propertyType = OS.kDataBrowserCheckboxType;
		checkColumn.propertyDesc_propertyFlags = OS.kDataBrowserPropertyIsMutable;
		int checkWidth = getCheckColumnWidth ();
		checkColumn.headerBtnDesc_minimumWidth = (short) checkWidth;
		checkColumn.headerBtnDesc_maximumWidth = (short) checkWidth;
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
	OS.SetDataBrowserListViewDisclosureColumn (handle, column_id, false);
	OS.SetDataBrowserTableViewNamedColumnWidth (handle, column_id, (short) 0);

	/*
	* Feature in the Macintosh.  Scroll bars are not created until
	* the data browser needs to draw them.  The fix is to force the scroll
	* bars to be created by temporarily giving the widget a size, drawing
	* it on a offscreen buffer to avoid flashes and then restoring it to
	* size zero.
	*/
	if (OS.VERSION < 0x1040) {
		OS.HIViewSetDrawingEnabled (handle, false);
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
		OS.HIViewSetDrawingEnabled (handle, true);
	}
}

void createItem (TreeColumn column, int index) {
	if (!(0 <= index && index <= columnCount)) error (SWT.ERROR_INVALID_RANGE);
	if (index == 0) {
		// first column must be left aligned
		column.style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
		column.style |= SWT.LEFT;
	}
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
		
		if (index == 0) {
			int [] disclosure = new int [1];
			boolean [] expandableRows = new boolean [1];
			OS.GetDataBrowserListViewDisclosureColumn (handle, disclosure, expandableRows);
			OS.SetDataBrowserListViewDisclosureColumn (handle, column.id, expandableRows [0]);
		}
	} 
	if (columnCount == columns.length) {
		TreeColumn [] newColumns = new TreeColumn [columnCount + 4];
		System.arraycopy (columns, 0, newColumns, 0, columns.length);
		columns = newColumns;
	}
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
	int count = getItemCount (parentItem);
	if (index == -1) index = count;
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	int id = _getId ();
	if (id > items.length) {
		TreeItem [] newItems = new TreeItem [id + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	item.id = id;
	items [id - 1] = item;
	int [] ids = parentItem == null ? childIds : parentItem.childIds;
	if (ids == null || count + 1 > ids.length) {
		int [] newIds = new int [count + 4];
		if (ids != null) System.arraycopy (ids, 0, newIds, 0, ids.length);
		ids = newIds;
		if (parentItem == null) {
			childIds = ids;
		} else {
			parentItem.childIds = ids;
		}
	}
	System.arraycopy (ids, index, ids, index + 1, ids.length - index - 1);
	ids [index] = id;
	if (parentItem != null) parentItem.itemCount++;
	if (parentItem == null || (parentItem.getExpanded ())) {
		int parentID = parentItem == null ? OS.kDataBrowserNoItem : parentItem.id;
		if (OS.AddDataBrowserItems (handle, parentID, 1, new int [] {item.id}, OS.kDataBrowserItemNoProperty) != OS.noErr) {
			items [id - 1] = null;
			System.arraycopy (ids, index+1, ids, index, ids.length - index - 1);
			error (SWT.ERROR_ITEM_NOT_ADDED);
		}
		visibleCount++;
	} else {	
		/*
		* Bug in the Macintosh.  When the first child of a tree item is
		* added and the parent is not expanded, the parent does not
		* redraw to show the expander.  The fix is to force a redraw.
		*/
		if (parentItem != null && parentItem.itemCount == 1) parentItem.redraw (OS.kDataBrowserNoItem);
	}
}

ScrollBar createScrollBar (int style) {
	return createStandardBar (style);
}

void createWidget () {
	super.createWidget ();
	items = new TreeItem [4];
	columns = new TreeColumn [4];
	if (OS.VERSION >= 0x1050) {
		OS.DataBrowserChangeAttributes (handle, OS.kDataBrowserAttributeAutoHideScrollBars, 0);
	}
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
	OS.SetDataBrowserSelectedItems (handle, 0, null, OS.kDataBrowserItemsRemove);
	if ((style & SWT.SINGLE) != 0) {
		OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0]);
	}
	ignoreSelect = false;
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
	OS.SetDataBrowserSelectedItems (handle, 1, new int [] {item.id}, OS.kDataBrowserItemsRemove);
	if ((style & SWT.SINGLE) != 0) {
		OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0]);
	}
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
		int [] disclosure = new int [1];
		boolean [] expandableRows = new boolean [1];
		OS.GetDataBrowserListViewDisclosureColumn (handle, disclosure, expandableRows);
		if (disclosure [0] == column.id) {
			TreeColumn firstColumn = columns [1];
			firstColumn.style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
			firstColumn.style |= SWT.LEFT;
			firstColumn.updateHeader();
			OS.SetDataBrowserListViewDisclosureColumn (handle, firstColumn.id, expandableRows [0]);
		}
		if (OS.RemoveDataBrowserTableViewColumn (handle, column.id) != OS.noErr) {
			error (SWT.ERROR_ITEM_NOT_REMOVED);
		}
	}
	System.arraycopy (columns, index + 1, columns, index, --columnCount - index);
	columns [columnCount] = null;
	for (int i=index; i<columnCount; i++) {
		columns [i].sendEvent (SWT.Move);
	}
}

void destroyItem (TreeItem item) {
	TreeItem parentItem = item.parentItem;
	if (parentItem == null || parentItem.getExpanded ()) {
		int oldAnchor = savedAnchor;
		int [] index = new int [1];
		if (OS.GetDataBrowserTableViewItemRow (handle, item.id, index) != OS.noErr) {
			index = null;
		}
		int parentID = parentItem == null ? OS.kDataBrowserNoItem : parentItem.id;
		ignoreExpand = true;
		if (OS.RemoveDataBrowserItems (handle, parentID, 1, new int [] {item.id}, 0) != OS.noErr) {
			error (SWT.ERROR_ITEM_NOT_REMOVED);
		}
		visibleCount--;
		ignoreExpand = false;
		if (savedAnchor != 0 && savedAnchor != oldAnchor) {
			if (index != null) {
		    	int [] itemId = new int [1];
		    	if (OS.GetDataBrowserTableViewItemID (handle, index [0], itemId) == OS.noErr) {
		    		savedAnchor = itemId [0];
		    	}
	    	}
		}
	}
	/*
	* Bug in the Macintosh.  When the last child of a tree item is
	* removed and the parent is not expanded, the parent does not
	* redraw to remove the expander.  The fix is to force a redraw.
	*/
	if (parentItem != null && !parentItem.getExpanded () && parentItem.itemCount > 0) {
		parentItem.redraw (OS.kDataBrowserNoItem);
	}
	//TEMPORARY CODE
	releaseItem (item, false);
	setScrollWidth (true);
	fixScrollBar ();
}

void destroyScrollBar (ScrollBar bar) {
	if ((bar.style & SWT.H_SCROLL) != 0) style &= ~SWT.H_SCROLL;
	if ((bar.style & SWT.V_SCROLL) != 0) style &= ~SWT.V_SCROLL;
	OS.SetDataBrowserHasScrollBars (handle, (style & SWT.H_SCROLL) != 0, (style & SWT.V_SCROLL) != 0);
}

int drawItemProc (int browser, int id, int property, int itemState, int theRect, int gdDepth, int colorDevice) {
	if (id < 0) return OS.noErr;
	int columnIndex = 0;
	if (columnCount > 0) {
		for (columnIndex=0; columnIndex<columnCount; columnIndex++) {
			if (columns [columnIndex].id == property) break;
		}
		if (columnIndex == columnCount) return OS.noErr;
	}
	Rect rect = new Rect ();
	TreeItem item = _getItem (id, true);
	if ((style & SWT.VIRTUAL) != 0) {
		if (!item.cached) {
			if (!checkData (item, false)) return OS.noErr;
			if (setScrollWidth (item)) {
				if (OS.GetDataBrowserItemPartBounds (handle, id, property, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
					int x = rect.left;
					int y = rect.top;
					int width = rect.right - rect.left;
					int height = rect.bottom - rect.top;
					redrawWidget (handle, x, y, width, height, false);
				}
				return OS.noErr;
			}
		}
	}
	OS.memmove (rect, theRect, Rect.sizeof);
	int x = rect.left;
	int y = rect.top;
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	GC gc = paintGC;
	if (gc == null) {
		GCData data = new GCData ();
		int [] port = new int [1];
		OS.GetPort (port);
		data.port = port [0];
		gc = GC.carbon_new (this, data);
	}
	OS.GetDataBrowserItemPartBounds (handle, id, property, OS.kDataBrowserPropertyEnclosingPart, rect);
	int gridWidth = getLinesVisible () ? GRID_WIDTH : 0;
	int itemX = rect.left + gridWidth;
	int itemY = rect.top;
	int itemWidth = rect.right - rect.left - gridWidth;
	int itemHeight = rect.bottom - rect.top + 1;
	if (drawBackground) {
		drawBackground = false;
		Region region = new Region (display);
		Rectangle clientArea = getClientArea ();
		int headerHeight = getHeaderHeight ();
		clientArea.y += headerHeight;
		clientArea.height -= headerHeight;
		if (clientArea.height < 0) clientArea.height = 0;
		region.add (clientArea);
		if ((style & SWT.CHECK) != 0 || gridWidth != 0) {
			int rgn = OS.NewRgn();
			if ((style & SWT.CHECK) != 0) {
				if (OS.GetDataBrowserItemPartBounds (handle, id, Table.CHECK_COLUMN_ID, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
					OS.SetRectRgn (rgn, (short)rect.left, (short)clientArea.y, (short)(rect.right + gridWidth), (short)(clientArea.y + clientArea.height));
					OS.DiffRgn (region.handle, rgn, region.handle);
				}
			}
			if (gridWidth != 0) {
				if (columnCount == 0) {
					if (OS.GetDataBrowserItemPartBounds (handle, id, Table.COLUMN_ID, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
						OS.SetRectRgn (rgn, (short)rect.right, (short)clientArea.y, (short)(rect.right + gridWidth), (short)(clientArea.y + clientArea.height));
						OS.DiffRgn (region.handle, rgn, region.handle);					
					}
				} else {
					for (int i = 0; i < columnCount; i++) {
						if (OS.GetDataBrowserItemPartBounds (handle, id, columns[i].id, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
							OS.SetRectRgn (rgn, (short)rect.right, (short)clientArea.y, (short)(rect.right + gridWidth), (short)(clientArea.y + clientArea.height));
							OS.DiffRgn (region.handle, rgn, region.handle);
						}
					}
				}
			}
			OS.DisposeRgn (rgn);
		}
		if (region != null) gc.setClipping (region);
		fillBackground (handle, gc.handle, null);
		if (region != null) {
			gc.setClipping ((Rectangle)null);
			region.dispose ();
		}
	}
	OS.CGContextSaveGState (gc.handle);
	int itemRgn = OS.NewRgn ();
	OS.SetRectRgn (itemRgn, (short) itemX, (short) itemY, (short) (itemX + itemWidth), (short) (itemY + itemHeight));
	int clip = OS.NewRgn ();
	OS.GetClip (clip);
	OS.SectRgn (clip, itemRgn, itemRgn);
	OS.DisposeRgn (clip);
	Region region = Region.carbon_new (display, itemRgn);
	Font font = item.getFont (columnIndex);
	Color background = item.getBackground (columnIndex);
	Color foreground = item.getForeground (columnIndex);
	Image image = item.getImage (columnIndex);
	String text = item.getText (columnIndex);	
	gc.setClipping (region);
	gc.setFont (font);
	Point extent = gc.stringExtent (text);
	int contentWidth = extent.x;
	Rectangle imageBounds = null;
	int gap = 0;
	if (image != null) {
		gap = getGap ();
		imageBounds = image.getBounds ();
		contentWidth += this.imageBounds.width + gap;
	}
	int paintWidth = contentWidth;
	if (hooks (SWT.MeasureItem)) {
		Event event = new Event ();
		event.item = item;
		event.index = columnIndex;
		event.gc = gc;
		event.width = contentWidth;
		event.height = itemHeight;
		sendEvent (SWT.MeasureItem, event);
		if (itemHeight < event.height) {
			itemHeight = event.height;
			OS.SetDataBrowserTableViewRowHeight (handle, (short) event.height);
			redrawWidget (handle, false);
		}
		if (setScrollWidth (item)) {
			redrawWidget (handle, false);
		}
		contentWidth = event.width;
		gc.setClipping (region);
		gc.setFont (font);
	}
	int drawState = SWT.FOREGROUND;
	if (item.background != null || (item.cellBackground != null && item.cellBackground [columnIndex] != null)) drawState |= SWT.BACKGROUND;
	if ((itemState & (OS.kDataBrowserItemIsSelected | OS.kDataBrowserItemIsDragTarget)) != 0) drawState |= SWT.SELECTED;
	boolean wasSelected = (drawState & SWT.SELECTED) != 0;
	if ((drawState & SWT.SELECTED) != 0 && ((style & SWT.FULL_SELECTION) != 0 || columnIndex == 0)) {
		gc.setBackground (display.getSystemColor (SWT.COLOR_LIST_SELECTION));
		gc.setForeground (display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT));
	} else {
		gc.setBackground (background);
		gc.setForeground (foreground);
	}
	if (hooks (SWT.EraseItem)) {
		Event event = new Event ();
		event.item = item;
		event.index = columnIndex;
		event.gc = gc;
		event.x = itemX;
		event.y = itemY;
		event.width = itemWidth;
		event.height = itemHeight;
		event.detail = drawState;
		sendEvent (SWT.EraseItem, event);
		drawState = event.doit ? event.detail : 0;
		gc.setClipping (region);
		gc.setFont (font);
		if ((drawState & SWT.SELECTED) != 0 && ((style & SWT.FULL_SELECTION) != 0 || columnIndex == 0)) {
			gc.setBackground (display.getSystemColor (SWT.COLOR_LIST_SELECTION));
			gc.setForeground (display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT));
		} else {
			gc.setBackground (background);
			if (!wasSelected) gc.setForeground (foreground);
		}
	}
	if (columnCount != 0 && columnIndex != 0) {
		TreeColumn column = columns [columnIndex];
		if ((column.style & SWT.CENTER) != 0) x += (width - contentWidth) / 2;
		if ((column.style & SWT.RIGHT) != 0) x += width - contentWidth;
	}
	int stringX = x, imageWidth = 0;
	if (image != null) stringX += imageWidth = this.imageBounds.width + gap;
	if ((drawState & SWT.SELECTED) != 0 && ((style & SWT.FULL_SELECTION) != 0 || columnIndex == 0)) {
		if ((style & SWT.HIDE_SELECTION) == 0 || hasFocus ()) {
			if ((style & SWT.FULL_SELECTION) != 0) {
				gc.fillRectangle (itemX, itemY, itemWidth, itemHeight - 1);
				drawState &= ~SWT.BACKGROUND;
			} else if (columnIndex == 0) {
				gc.fillRectangle (stringX - 1, y, (contentWidth - imageWidth) + 2, itemHeight - 1);
				drawState &= ~SWT.BACKGROUND;
			}
		 } else {
			 if ((drawState & SWT.BACKGROUND) != 0) gc.setBackground (background);
		 }
	}
	if ((drawState & SWT.BACKGROUND) != 0) {
		if (columnCount == 0) {
			gc.fillRectangle (stringX - 1, y, (contentWidth - imageWidth) + 2, itemHeight - 1);
		} else {
			gc.fillRectangle (itemX, itemY, itemWidth, itemHeight);
		}
	}
	if ((drawState & SWT.FOREGROUND) != 0) {
		if (image != null) {
			int imageX = x, imageY = y + (height - this.imageBounds.height) / 2;
			gc.drawImage (image, 0, 0, imageBounds.width, imageBounds.height, imageX, imageY, this.imageBounds.width, this.imageBounds.height);
		}
		gc.drawString (text, stringX, y + (height - extent.y) / 2, true);
	}
	if (hooks (SWT.PaintItem)) {
		Event event = new Event ();
		event.item = item;
		event.index = columnIndex;
		event.gc = gc;
		event.x = x;
		event.y = y;
		event.width = paintWidth;
		event.height = itemHeight;
		event.detail = drawState;
		sendEvent (SWT.PaintItem, event);
	}
	OS.CGContextRestoreGState (gc.handle);
	OS.DisposeRgn (itemRgn);
	if (gc != paintGC) gc.dispose ();
	return OS.noErr;
}

void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	/*
	* Bug in the Macintosh. For some reason, the data browser does not
	* redraw the checkboxes when the enable state changes. The fix is
	* to force a redraw.
	*/
	redrawWidget (handle, false);
}

void fixScrollBar () {
	/*
	* Bug in the Macintosh. For some reason, the data browser does not update
	* the vertical scrollbar when it is scrolled to the bottom and items are
	* removed.  The fix is to check if the scrollbar value is bigger the
	* maximum number of visible items and clamp it when needed.
	*/
	int [] top = new int [1], left = new int [1];
	OS.GetDataBrowserScrollPosition (handle, top, left);
	int maximum = Math.max (0, getItemHeight () * visibleCount - getClientArea ().height);
	if (top [0] > maximum) {
		OS.SetDataBrowserScrollPosition (handle, maximum, left [0]);
	}
}

String [] getAxAttributes () {
	return AX_ATTRIBUTES;
}

public int getBorderWidth () {
	checkWidget ();
	int border = 0;
	byte [] hasBorder = new byte [1];
	OS.GetControlData (handle, (short) OS.kControlEntireControl, OS.kControlDataBrowserIncludesFrameAndFocusTag, 1, hasBorder, null);
	if (hasBorder [0] != 0) {
		int [] outMetric = new int [1];
		OS.GetThemeMetric (OS.kThemeMetricFocusRectOutset, outMetric);
		border += outMetric [0] - BORDER_INSET;
	}
	return border;
}

int getCheckColumnWidth () {
	int inset = 0;
	if (OS.VERSION >= 0x1040) {
		float [] metric = new float [1];
		OS.DataBrowserGetMetric (handle, OS.kDataBrowserMetricCellContentInset, null, metric);
		inset = (int) metric [0];
	} else {
		inset = CELL_CONTENT_INSET;
	}
	int [] checkWidth = new int [1];
	OS.GetThemeMetric (OS.kThemeMetricCheckBoxWidth, checkWidth);
	return checkWidth [0] + inset * 2;
}

public Rectangle getClientArea () {
	checkWidget();
	int border = getBorderWidth ();
	Rect rect = new Rect (), inset = new Rect ();
	OS.GetControlBounds (handle, rect);
	OS.GetDataBrowserScrollBarInset (handle, inset);
	int width = Math.max (0, rect.right - rect.left - inset.right - border - border);
	int height = Math.max (0, rect.bottom - rect.top - inset.bottom - border - border);
	return new Rectangle (inset.left + border, inset.top + border, width, height);
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
		OS.GetDataBrowserTableViewColumnPosition (handle, column.id, position);
		if ((style & SWT.CHECK) != 0) position [0] -= 1;
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

int getGap () {
	if (OS.VERSION >= 0x1040) {
		float [] metric = new float [1];
		OS.DataBrowserGetMetric (handle, OS.kDataBrowserMetricIconAndTextGap, null, metric);
		return (int) metric [0];
	}
	return ICON_AND_TEXT_GAP;
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
 * 
 * @since 3.1
 */
public boolean getHeaderVisible () {
	checkWidget ();
	short [] height = new short [1];
	OS.GetDataBrowserListViewHeaderBtnHeight (handle, height);
	return height [0] != 0;
}

int getLeftDisclosureInset (int column_id) {
	int [] disclosure = new int [1];
	OS.GetDataBrowserListViewDisclosureColumn (handle, disclosure, new boolean [1]);
	if (disclosure [0] == column_id) {
		int width = 0;
		int [] metric1 = new int [1];
		OS.GetThemeMetric (OS.kThemeMetricDisclosureTriangleWidth, metric1);
		width += metric1 [0];
		if (OS.VERSION >= 0x1040) {
			float [] metric = new float [1];
			OS.DataBrowserGetMetric (handle, OS.kDataBrowserMetricDisclosureColumnEdgeInset, null, metric);
			width += (int) metric [0];
			OS.DataBrowserGetMetric (handle, OS.kDataBrowserMetricDisclosureTriangleAndContentGap, null, metric);
			width += (int) metric [0];
		} else {
			width += DISCLOSURE_COLUMN_EDGE_INSET + DISCLOSURE_TRIANGLE_AND_CONTENT_GAP;
		}
		return width;
	}
	return 0;
}

int getInsetWidth (int column_id, boolean leftInset) {
	int inset = 0;
	if (OS.VERSION >= 0x1040) {
		float [] metric = new float [1];
		OS.DataBrowserGetMetric (handle, OS.kDataBrowserMetricCellContentInset, null, metric);
		inset = (int) metric [0];
	} else {
		inset = CELL_CONTENT_INSET;
	}
	int width = 0;
	int [] disclosure = new int [1];
	OS.GetDataBrowserListViewDisclosureColumn (handle, disclosure, new boolean [1]);
	if (disclosure [0] != column_id) {
		width += inset * 2;
	} else {
		width += inset;
		if (leftInset) width += getLeftDisclosureInset (column_id);
	}
	return width;
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
	int count = getItemCount (null);
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
	int [] disclosure = new int [1];
	OS.GetDataBrowserListViewDisclosureColumn (handle, disclosure, new boolean [1]);
	short [] height = new short [1];
	if (OS.GetDataBrowserTableViewRowHeight (handle, height) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_ITEM_HEIGHT);
	}
	Rect rect = new Rect ();
	org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
	OS.SetPt (pt, (short) point.x, (short) point.y);
	if (0 < lastHittest && lastHittest <= items.length && lastHittestColumn != 0) {
		TreeItem item = _getItem (lastHittest, false);
		if (item != null) {
			int lastPosColumnId = column_id;
			for (int i=0; i<columnCount; i++) {
				TreeColumn column = columns [i];
				int [] position = new int [1];
				OS.GetDataBrowserTableViewColumnPosition (handle, column.id, position);
				if ((style & SWT.CHECK) != 0) position [0] -= 1;
				if (position [0] == columnCount - 1) {
					lastPosColumnId = column.id;
					break;
				}
			}
			if (OS.GetDataBrowserItemPartBounds (handle, item.id, lastPosColumnId, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
				if (pt.h > rect.right) return null;
			}
			if (OS.GetDataBrowserItemPartBounds (handle, item.id, disclosure[0], OS.kDataBrowserPropertyDisclosurePart, rect) == OS.noErr) {
				if (OS.PtInRect (pt, rect)) return null;
			}
			if (OS.GetDataBrowserItemPartBounds (handle, item.id, lastHittestColumn, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
				rect.bottom = (short)(rect.top + height [0]);
				if (OS.PtInRect (pt, rect)) return item;
				if (rect.top <= pt.v && pt.v < rect.bottom) {
					for (int j = 0; j < columnCount; j++) {
						if (OS.GetDataBrowserItemPartBounds (handle, item.id, columns [j].id, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
							rect.bottom = (short)(rect.top + height [0]);
							if (OS.PtInRect (pt, rect)) return item;
						}
					}
					return null;
				}
			}
		}
	}
	/* Find the item by approximating its row position */
	int[] top = new int[1];
	int[] left = new int[1];
	OS.GetDataBrowserScrollPosition (handle, top, left);		
	short [] header = new short [1];
	OS.GetDataBrowserListViewHeaderBtnHeight (handle, header);
	int [] offsets = new int [] {0, 1, -1};
	for (int i = 0; i < offsets.length; i++) {
		int row = (top[0] - header [0] + point.y) / height [0] + offsets [i];
		if (row >= 0) {
			int [] itemId = new int[1];
			int result = OS.GetDataBrowserTableViewItemID (handle, row, itemId);
			if (result != OS.noErr) return null;
			TreeItem item = _getItem(itemId[0], false);
			if (item == null) return null;
			if (OS.GetDataBrowserItemPartBounds (handle, item.id, disclosure [0], OS.kDataBrowserPropertyDisclosurePart, rect) == OS.noErr) {
				if (OS.PtInRect (pt, rect)) return null;
			}
			int columnId = columnCount == 0 ? column_id : columns [0].id;
			if (OS.GetDataBrowserItemPartBounds (handle, item.id, columnId, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
				rect.bottom = (short)(rect.top + height [0]);
				if (rect.top <= pt.v && pt.v < rect.bottom) {
					if (columnCount == 0) {
						if (OS.PtInRect (pt, rect)) return item;
					} else {
						for (int j = 0; j < columnCount; j++) {
							if (OS.GetDataBrowserItemPartBounds (handle, item.id, columns [j].id, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
								rect.bottom = (short)(rect.top + height [0]);
								if (OS.PtInRect (pt, rect)) return item;
							}
						}
					}
					return null;
				}
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

int getItemCount (TreeItem parentItem) {
	if (parentItem == null) {
		int [] count = new int [1];
		if (OS.GetDataBrowserItemCount (handle, OS.kDataBrowserNoItem, false, OS.kDataBrowserItemAnyState, count) == OS.noErr) {
			return count [0];
		}
		return 0;
	}
	return parentItem.itemCount;
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
	return getItems (null);
}

TreeItem [] getItems (TreeItem parentItem) {
	if (items == null) return new TreeItem [0];
	int count = getItemCount (parentItem);
	TreeItem [] result = new TreeItem [count];
	for (int i=0; i<count; i++) {
		result [i] = _getItem (parentItem, i);
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
 * 
 * @since 3.1
 */
public boolean getLinesVisible () {
	checkWidget ();
	if (OS.VERSION >= 0x1040) {
		int [] attrib = new int [1];
		OS.DataBrowserGetAttributes (handle, attrib);
		return (attrib [0] & (OS.kDataBrowserAttributeListViewAlternatingRowColors | OS.kDataBrowserAttributeListViewDrawColumnDividers)) != 0;
	}
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
	int [] count = new int [1];
	if (OS.GetDataBrowserItemCount (handle, OS.kDataBrowserNoItem, true, OS.kDataBrowserItemIsSelected, count) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_COUNT);
	}
	TreeItem [] result = new TreeItem [count[0]];
	if (count[0] > 0) {
		int ptr = OS.NewHandle (0);
		if (count[0] == 1) {
			if (OS.GetDataBrowserItems (handle, OS.kDataBrowserNoItem, true, OS.kDataBrowserItemIsSelected, ptr) != OS.noErr) {
				error (SWT.ERROR_CANNOT_GET_SELECTION);
			}
			OS.HLock (ptr);
			int [] id = new int [1];
			OS.memmove (id, ptr, 4);
			OS.memmove (id, id [0], 4);
			result [0] = _getItem (id [0], true);
			OS.HUnlock (ptr);
		} else {
			getSelection (result, OS.kDataBrowserNoItem, ptr, 0);
		}
		OS.DisposeHandle (ptr);
	}
	return result;
}

int getSelection(TreeItem[] result, int item, int ptr, int index) {
	OS.SetHandleSize (ptr, 0);
	if (OS.GetDataBrowserItems (handle, item, false, OS.kDataBrowserItemIsSelected, ptr) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_SELECTION);
	}
	int count = OS.GetHandleSize (ptr) / 4;
	if (count > 0) {
		OS.HLock (ptr);
		int [] id = new int [count];
		OS.memmove (id, ptr, 4);
		OS.memmove (id, id [0], count * 4);
		for (int i=0; i<count; i++) {
			result [index++] = _getItem (id [count - i - 1], true);
		}
		OS.HUnlock (ptr);
		if (index == result.length) return index;
	}
	OS.SetHandleSize (ptr, 0);
	if (OS.GetDataBrowserItems (handle, item, false, OS.kDataBrowserContainerIsOpen, ptr) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_SELECTION);
	}
	count = OS.GetHandleSize (ptr) / 4;
	if (count > 0) {
		OS.HLock (ptr);
		int [] id = new int [count];
		OS.memmove (id, ptr, 4);
		OS.memmove (id, id [0], count * 4);
		for (int i=0; i<count; i++) {
			index = getSelection(result, id [count - i - 1], ptr, index);
			if (index == result.length) return index;
		}
		OS.HUnlock (ptr);
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
	int [] count = new int [1];
	if (OS.GetDataBrowserItemCount (handle, OS.kDataBrowserNoItem, true, OS.kDataBrowserItemIsSelected, count) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_COUNT);
	}
	return count [0];
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
	/* Find the topItem by calculating its row position */
	int[] top = new int[1], left = new int[1];
	OS.GetDataBrowserScrollPosition (handle, top, left);		
	int row = top[0] / getItemHeight ();;
	int [] itemId = new int[1];
	int result = OS.GetDataBrowserTableViewItemID (handle, row, itemId);
	if (result == OS.noErr) return _getItem (itemId[0], false);
	return null;
}

int helpProc (int inControl, int inGlobalMouse, int inRequest, int outContentProvided, int ioHelpContent) {
	if (toolTipText == null) {
	    switch (inRequest) {
			case OS.kHMSupplyContent: {
				if (!(toolTipText != null && toolTipText.length () != 0)) {
					Rect rect = new Rect ();
					int window = OS.GetControlOwner (handle);
					OS.GetWindowBounds (window, (short) OS.kWindowContentRgn, rect);
					short windowLeft = rect.left, windowTop = rect.top;
					org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
					OS.memmove(pt, new int[] {inGlobalMouse}, 4);
					pt.h -= windowLeft;
					pt.v -= windowTop;
					String toolTipText = null;
					int tagSide = OS.kHMAbsoluteCenterAligned;
					CGPoint inPt = new CGPoint ();
					int [] contentView = new int [1];
					OS.HIViewFindByID (OS.HIViewGetRoot (OS.GetControlOwner (handle)), OS.kHIViewWindowContentID (), contentView);
					OS.HIViewConvertPoint (inPt, handle, contentView [0]);
					pt.h -= (int) inPt.x;
					pt.v -= (int) inPt.y;
					windowLeft += (int) inPt.x;
					windowTop += (int) inPt.y;
					int x = pt.h;
					int y = pt.v;
					int headerHeight = getHeaderHeight ();
					if (headerHeight != 0 && (0 <= y && y < headerHeight) ) {
						int startX = 0;
						for (int i = 0; i < columnCount; i++) {
							TreeColumn column = columns [i];
							int width = column.lastWidth + getLeftDisclosureInset (column.id);
							if (startX <= x && x < startX + width) {
								toolTipText = column.toolTipText;
								rect.left = (short) startX;
								rect.top = (short) 0;
								rect.right = (short) (rect.left + width);
								rect.bottom = (short) (rect.top + headerHeight);
								tagSide = OS.kHMOutsideBottomRightAligned;
								break;
							}
							startX += width;
						}
					} else {
						TreeItem item = null;
						if (0 < lastHittest && lastHittest <= items.length && lastHittestColumn != 0) {
							if (OS.GetDataBrowserItemPartBounds (handle, lastHittest, lastHittestColumn, OS.kDataBrowserPropertyContentPart, rect) == OS.noErr) {
								item = _getItem (lastHittest, false);
							}
						}
						if (item != null) {
							int columnIndex = 0;
							TreeColumn column = null;
							if (columnCount > 0) {
								for (int i = 0; i < columnCount; i++) {
									if (columns[i].id == lastHittestColumn) {
										column = columns[i];
										columnIndex = i;
										break;
									}
								}
							}
							int columnId = lastHittestColumn;
							GC gc = new GC (this);
							int inset = getInsetWidth (columnId, false);
							int width = item.calculateWidth (columnIndex, gc) + inset;
							gc.dispose ();
							int columnWidth;
							if (columnCount == 0) {
								columnWidth = getClientArea ().width;
							} else {
								short [] w = new short [1];
								OS.GetDataBrowserTableViewNamedColumnWidth (handle, columnId, w);
								columnWidth = w[0];
							}
							if (width > columnWidth) {
								toolTipText = item.getText (columnIndex);
								Image image = item.getImage (columnIndex);
								int imageWidth = image != null ? image.getBounds ().width + getGap () : 0;
								int style = column == null ? SWT.LEFT : column.style;
								if ((style & SWT.LEFT) != 0) {
									rect.left += imageWidth;
									rect.right = (short) (rect.left + width - imageWidth - inset);
								}
								if ((style & SWT.RIGHT) != 0) {
									rect.left = (short) (rect.right - width + imageWidth + inset);
								}
								if ((style & SWT.CENTER) != 0) {
									rect.left += imageWidth;
								}
							}
						}
					}
					if (toolTipText != null && toolTipText.length () != 0) {
						char [] buffer = new char [toolTipText.length ()];
						toolTipText.getChars (0, buffer.length, buffer, 0);
						int length = fixMnemonic (buffer);
						if (display.helpString != 0) OS.CFRelease (display.helpString);
						display.helpString = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, length);
						HMHelpContentRec helpContent = new HMHelpContentRec ();
						OS.memmove (helpContent, ioHelpContent, HMHelpContentRec.sizeof);
						display.helpWidget = this;
						helpContent.version = OS.kMacHelpVersion;
						helpContent.tagSide = (short) tagSide;
						helpContent.absHotRect_left = (short) (rect.left + windowLeft);
						helpContent.absHotRect_top = (short) (rect.top + windowTop);
						helpContent.absHotRect_right = (short) (rect.right + windowLeft);
						helpContent.absHotRect_bottom = (short) (rect.bottom + windowTop);	
						helpContent.content0_contentType = OS.kHMCFStringContent;
						helpContent.content0_tagCFString = display.helpString;
						helpContent.content1_contentType = OS.kHMCFStringContent;
						helpContent.content1_tagCFString = display.helpString;
						OS.memmove (ioHelpContent, helpContent, HMHelpContentRec.sizeof);
						OS.memmove (outContentProvided, new short[]{OS.kHMContentProvided}, 2);
						return OS.noErr;
					}
				}
				break;
			}
		}
	}
	return super.helpProc (inControl, inGlobalMouse, inRequest, outContentProvided, ioHelpContent);
}

int hitTestProc (int browser, int id, int property, int theRect, int mouseRect) {
	lastHittest = id;
	lastHittestColumn = property;
	return 1;
}

void hookEvents () {
	super.hookEvents ();
	DataBrowserCallbacks callbacks = new DataBrowserCallbacks ();
	callbacks.version = OS.kDataBrowserLatestCallbacks;
	OS.InitDataBrowserCallbacks (callbacks);
	callbacks.v1_itemDataCallback = display.itemDataProc;
	callbacks.v1_itemNotificationCallback = display.itemNotificationProc;
	callbacks.v1_itemCompareCallback = display.itemCompareProc;
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
	return _indexOf (null, item);
}

int _indexOf (TreeItem parentItem, TreeItem item) {
	int [] ids = parentItem == null ? childIds : parentItem.childIds;
	if (ids != null) {
		for (int i=0; i<ids.length; i++) {
			if (ids [i] == item.id) return i;
		}
	}
	return -1;
}

int itemCompareProc (int browser, int itemOne, int itemTwo, int sortProperty) {
	boolean create = (style & SWT.VIRTUAL) != 0 && sortColumn != null  
					&& !sortColumn.isDisposed () && sortDirection == SWT.DOWN; 
	TreeItem item1 = _getItem (itemOne, create);
	TreeItem item2 = _getItem (itemTwo, create);
	if (item1 == null || item2 == null) return OS.noErr;
	int index1 = _indexOf (item1.parentItem, item1);
	int index2 = _indexOf (item2.parentItem , item2);
	if (sortDirection == SWT.DOWN && sortColumn != null) {
		return index1 > index2 ? 1 : 0;
	}
	return index1 < index2 ? 1 : 0;
}

int itemDataProc (int browser, int id, int property, int itemData, int setValue) {
	switch (property) {
		case CHECK_COLUMN_ID: {
			TreeItem item = _getItem (id, true);
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
				/*
				* Bug in the Macintosh. When the height of the row is smaller than the
				* check box, the tail of the check mark draws outside of the item part
				* bounds. This means it will not be redrawn when the item is unckeched.
				* The fix is to redraw the area.
				*/
				if (!item.checked) item.redraw(Tree.CHECK_COLUMN_ID);
			} else {
				int theData = OS.kThemeButtonOff;
				if (item.checked) theData = item.grayed ? OS.kThemeButtonMixed : OS.kThemeButtonOn;
				OS.SetDataBrowserItemDataButtonValue (itemData, (short) theData);
			}
			break;
		}
		case OS.kDataBrowserItemIsContainerProperty: {
			TreeItem item = _getItem (id, true);
			if (item.itemCount > 0) {
				OS.SetDataBrowserItemDataBooleanValue (itemData, true);
			}
			break;
		}
	}
	return OS.noErr;
}

int itemNotificationProc (int browser, int id, int message) {
	if (message == OS.kDataBrowserUserStateChanged) {
		boolean resized = false;
		short [] width = new short [1];
		TreeColumn [] newColumns = getColumns ();
		for (int i = 0; i < columnCount; i++) {
			TreeColumn column = newColumns [i];
			if (!column.isDisposed ()) {
				OS.GetDataBrowserTableViewNamedColumnWidth (handle, column.id, width);
				if (width [0] != column.lastWidth) {
					column.resized (width [0]);
					resized = true;
				}
			}
			if (!column.isDisposed ()) {
				int [] position = new int[1];
				OS.GetDataBrowserTableViewColumnPosition (handle, column.id, position);
				if (position [0] != column.lastPosition) {
					column.lastPosition = position [0];
					int order = (style & SWT.CHECK) != 0 ? position [0] - 1 : position [0];
					if (order == 0) {
						int [] disclosure = new int [1];
						boolean [] expandableRows = new boolean [1];
						OS.GetDataBrowserListViewDisclosureColumn (handle, disclosure, expandableRows);
						if (disclosure [0] != column.id) {
							OS.SetDataBrowserListViewDisclosureColumn (handle, column.id, expandableRows [0]);
						}
					}
					column.sendEvent (SWT.Move);
					resized = true;
				}
			}
		}
		int [] property = new int [1];
		OS.GetDataBrowserSortProperty (handle, property);
		if (property [0] != 0) {
			if (!resized) {
				for (int i = 0; i < columnCount; i++) {
					TreeColumn column = columns [i];
					if (property [0] == column.id) {
						column.postEvent (display.clickCount == 2 ? SWT.DefaultSelection : SWT.Selection);
						break;
					}
				}
			}
			OS.SetDataBrowserSortProperty (handle, 0);
			if (sortColumn != null && !sortColumn.isDisposed () && sortDirection != SWT.NONE) {
				OS.SetDataBrowserSortProperty (handle, sortColumn.id);
				int order = sortDirection == SWT.DOWN ? OS.kDataBrowserOrderDecreasing : OS.kDataBrowserOrderIncreasing;
				OS.SetDataBrowserSortOrder (handle, (short) order);
			}
		}
		return OS.noErr;
	}
	switch (message) {
		case OS.kDataBrowserItemSelected:
			savedAnchor = 0;
			break;
		case OS.kDataBrowserItemDeselected: {
			int [] selectionCount = new int [1];
			if (OS.GetDataBrowserItemCount (handle, OS.kDataBrowserNoItem, true, OS.kDataBrowserItemIsSelected, selectionCount) == OS.noErr) {
				if (selectionCount [0] == 0) savedAnchor = id;
			}
			break;
		}
	}
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
				event.item = _getItem (id, true);
				postEvent (SWT.Selection, event);
			}
			break;
		}	
		case OS.kDataBrowserItemDoubleClicked: {
			wasSelected = true;
			if (display.clickCount == 2) {
				Event event = new Event ();
				event.item = _getItem (id, true);
				postEvent (SWT.DefaultSelection, event);
			}
			break;
		}
		case OS.kDataBrowserContainerClosing: {
			int ptr = OS.NewHandle (0);
			if (OS.GetDataBrowserItems (handle, id, false, OS.kDataBrowserItemAnyState, ptr) == OS.noErr) {
				int count = OS.GetHandleSize (ptr) / 4;
				visibleCount -= count;
			}
			OS.DisposeHandle (ptr);

			/*
			* Bug in the Macintosh.  For some reason, if the selected sub items of an item
			* get a kDataBrowserItemDeselected notificaton when the item is collapsed, a
			* call to GetDataBrowserSelectionAnchor () will cause a segment fault.  The
			* fix is to deselect these items ignoring kDataBrowserItemDeselected and then
			* issue a selection event.
			*/
			ptr = OS.NewHandle (0);
			if (OS.GetDataBrowserItems (handle, id, true, OS.kDataBrowserItemIsSelected, ptr) == OS.noErr) {
				int count = OS.GetHandleSize (ptr) / 4;
				if (count > 0) {
					int [] ids = new int [count];
					OS.HLock (ptr);
					int [] start = new int [1];
					OS.memmove (start, ptr, 4);
					OS.memmove (ids, start [0], count * 4);
					OS.HUnlock (ptr);
					boolean oldIgnore = ignoreSelect;
					ignoreSelect = true;
					/*
					* Bug in the Macintosh.  When the DataBrowser selection flags includes
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
					OS.SetDataBrowserSelectedItems (handle, ids.length, ids, OS.kDataBrowserItemsRemove);
					if ((style & SWT.SINGLE) != 0) {
						OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0]);
					}
					ignoreSelect = oldIgnore;
					if (!ignoreSelect) {
						Event event = new Event ();
						event.item = _getItem (id, true);
						sendEvent (SWT.Selection, event);
					}
				}
			}
			OS.DisposeHandle (ptr);
			break;
		}
		case OS.kDataBrowserContainerClosed: {
			TreeItem parentItem = _getItem (id, true);
			if (parentItem == null) break; // can happen when removing all items
			int [] ids = parentItem.childIds;
			if (ids != null) {
				for (int i=0; i<parentItem.itemCount; i++) {
					TreeItem item = _getItem (ids [i], false); 
					if (item == null) ids [i] = 0;
				}
			}
			wasExpanded = true;
			if (!ignoreExpand) {
				Event event = new Event ();
				event.item = parentItem;
				sendEvent (SWT.Collapse, event);
				if (isDisposed ()) break;
				setScrollWidth (true);
				fixScrollBar ();
			}
			break;
		}
		case OS.kDataBrowserContainerOpened: {
			TreeItem item = _getItem (id, true);
			wasExpanded = true;
			if (!ignoreExpand) {
				Event event = new Event ();
				event.item = item;
				try {
					item.state |= EXPANDING;
					sendEvent (SWT.Expand, event);
					if (isDisposed ()) break;
				} finally {
					item.state &= ~EXPANDING;
				}
			}
			int newIdCount = 0;
			for (int i=0; i<item.itemCount; i++) {
				if (item.childIds [i] == 0) newIdCount++;
			}
			if (newIdCount > 0) {
				int [] newIds = _getIds (newIdCount);
				int index = 0;
				for (int i=0; i<item.itemCount; i++) {
					if (item.childIds [i] == 0) item.childIds [i] = newIds [index++];   
				}
			}
			OS.AddDataBrowserItems (handle, id, item.itemCount, item.childIds, OS.kDataBrowserItemNoProperty);
			visibleCount += item.itemCount;
			if (!ignoreExpand) {
				setScrollWidth (false, item.childIds, false);
			}
			break;
		}
	}
	return OS.noErr;
}

int kEventAccessibleGetNamedAttribute (int nextHandler, int theEvent, int userData) {
	int code = OS.eventNotHandledErr;
	int [] stringRef = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamAccessibleAttributeName, OS.typeCFStringRef, null, 4, null, stringRef);
	int length = 0;
	if (stringRef [0] != 0) length = OS.CFStringGetLength (stringRef [0]);
	char [] buffer = new char [length];
	CFRange range = new CFRange ();
	range.length = length;
	OS.CFStringGetCharacters (stringRef [0], range, buffer);
	String attributeName = new String(buffer);
	if (attributeName.equals(OS.kAXHeaderAttribute)) {
		short [] height = new short [1];
		OS.GetDataBrowserListViewHeaderBtnHeight (handle, height);
		if (height [0] == 0) {
			/*
			* Bug in the Macintosh.  Even when the header is not visible,
			* VoiceOver still reports each column header's role for every row.
			* This is confusing and overly verbose.  The fix is to return
			* "no header" when the screen reader asks for the header, by
			* returning noErr without setting the event parameter.
			*/
			code = OS.noErr;
		}
	} else {
		int [] ref = new int [1];
		OS.GetEventParameter (theEvent, OS.kEventParamAccessibleObject, OS.typeCFTypeRef, null, 4, null, ref);
		int axuielementref = ref [0];
		DataBrowserAccessibilityItemInfo itemInfo = new DataBrowserAccessibilityItemInfo ();
		int err = OS.AXUIElementGetDataBrowserItemInfo (axuielementref, handle, 0, itemInfo);
		if (err == OS.noErr && itemInfo.v0_columnProperty != OS.kDataBrowserItemNoProperty && itemInfo.v0_item != OS.kDataBrowserNoItem && itemInfo.v0_propertyPart == OS.kDataBrowserPropertyEnclosingPart) {
			int columnIndex = 0;
			for (columnIndex = 0; columnIndex < columnCount; columnIndex++) {
				if (columns [columnIndex].id == itemInfo.v0_columnProperty) break;
			}
			if (columnIndex != columnCount || columnCount == 0) {
				int id = itemInfo.v0_item;
				TreeItem treeItem = _getItem (id, false);
				if (attributeName.equals (OS.kAXRoleAttribute) || attributeName.equals (OS.kAXRoleDescriptionAttribute)) {
					String roleText = OS.kAXStaticTextRole;
					buffer = new char [roleText.length ()];
					roleText.getChars (0, buffer.length, buffer, 0);
					stringRef [0] = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
					if (stringRef [0] != 0) {
						if (attributeName.equals (OS.kAXRoleAttribute)) {
							OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
						} else { // kAXRoleDescriptionAttribute
							int stringRef2 = OS.HICopyAccessibilityRoleDescription (stringRef [0], 0);
							OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, new int [] {stringRef2});
							OS.CFRelease(stringRef2);
						}
						OS.CFRelease(stringRef [0]);
						code = OS.noErr;
					}
				} else if (attributeName.equals(OS.kAXChildrenAttribute)) {
					int children = OS.CFArrayCreateMutable (OS.kCFAllocatorDefault, 0, 0);
					OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFMutableArrayRef, 4, new int [] {children});
					OS.CFRelease(children);
					code = OS.noErr;
				} else if (attributeName.equals (OS.kAXTitleAttribute) || attributeName.equals (OS.kAXDescriptionAttribute)) {
					String text = treeItem.getText (columnIndex);
					buffer = new char [text.length ()];
					text.getChars (0, buffer.length, buffer, 0);
					stringRef [0] = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
					if (stringRef [0] != 0) {
						OS.SetEventParameter (theEvent, OS.kEventParamAccessibleAttributeValue, OS.typeCFStringRef, 4, stringRef);
						OS.CFRelease(stringRef [0]);
						code = OS.noErr;
					}
				}
			}
		}
	}
	if (accessible != null) {
		code = accessible.internal_kEventAccessibleGetNamedAttribute (nextHandler, theEvent, code);
	}
	return code;
}

int kEventControlGetClickActivation (int nextHandler, int theEvent, int userData) {
	OS.SetEventParameter (theEvent, OS.kEventParamClickActivation, OS.typeClickActivationResult, 4, new int [] {OS.kActivateAndHandleClick});
	return OS.noErr;
}

int kEventControlSetCursor (int nextHandler, int theEvent, int userData) {
	if (!isEnabledCursor ()) return OS.noErr;
	if (isEnabledModal ()) {
		org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point ();
		int sizeof = org.eclipse.swt.internal.carbon.Point.sizeof;
		OS.GetEventParameter (theEvent, OS.kEventParamMouseLocation, OS.typeQDPoint, null, sizeof, null, pt);
		if (!contains (pt.h, pt.v)) return OS.eventNotHandledErr;
	}
	return super.kEventControlSetCursor (nextHandler, theEvent, userData);
}

int kEventUnicodeKeyPressed (int nextHandler, int theEvent, int userData) {
	int result = super.kEventUnicodeKeyPressed (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	int [] keyboardEvent = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamTextInputSendKeyboardEvent, OS.typeEventRef, null, keyboardEvent.length * 4, null, keyboardEvent);
	int [] keyCode = new int [1];
	OS.GetEventParameter (keyboardEvent [0], OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
	switch (keyCode [0]) {
		case 49: { /* Space */
			int [] first = new int [1], last = new int [1];
			if (OS.GetDataBrowserSelectionAnchor (handle, first, last) == OS.noErr) {
				if (first [0] != 0) {
					TreeItem item = _getItem (first [0], true);
					if ((style & SWT.CHECK) != 0) {
						item.setChecked (!item.getChecked (), true);
					}
				}
			}
			break;
		}
		case 76: /* KP Enter */
		case 36: { /* Return */
			postEvent (SWT.DefaultSelection);
			break;
		}
		/*
		* Feature in the Macintosh.  For some reason, when the user hits an
		* up or down arrow to traverse the items in a Data Browser, the item
		* scrolls to the left such that the white space that is normally
		* visible to the right of the every item is scrolled out of view.
		* The fix is to save and restore the horizontal scroll position.
		*/
		case 125: /* Down */
		case 126: /* Up*/ {
			int [] itemCount = new int [1];
			if (OS.GetDataBrowserItemCount (handle, OS.kDataBrowserNoItem, false, OS.kDataBrowserItemAnyState, itemCount) == OS.noErr) {
				if (itemCount [0] == 0) break;
			}
			int [] top = new int [1], left = new int [1];
			OS.GetDataBrowserScrollPosition (handle, top, left);
			int [] itemId = null;
			int [] selectionCount = new int [1];
			if (OS.GetDataBrowserItemCount (handle, OS.kDataBrowserNoItem, true, OS.kDataBrowserItemIsSelected, selectionCount) == OS.noErr) {
				if (savedAnchor != 0 && selectionCount [0] == 0) {
					int [] index = new int [1];
					if (OS.GetDataBrowserTableViewItemRow (handle, savedAnchor, index) == OS.noErr) {
				    	index [0] = index [0] + (keyCode [0] == 125 ? 1 : -1);
				    	itemId = new int [1];
				    	if (OS.GetDataBrowserTableViewItemID (handle, index [0], itemId) != OS.noErr) {
				    		itemId [0] = savedAnchor;
				    	}
					}
				}
			}
			if (itemId != null) {
				OS.SetDataBrowserSelectedItems (handle, 1, itemId, OS.kDataBrowserItemsAssign);
				result = OS.noErr;
			} else {
				result = OS.CallNextEventHandler (nextHandler, theEvent);
			}
			OS.GetDataBrowserScrollPosition (handle, top, null);
			OS.SetDataBrowserScrollPosition (handle, top [0], left [0]);
			redrawBackgroundImage ();
			break;
		}
	}
	return result;
}

int kEventMouseDown (int nextHandler, int theEvent, int userData) {
	int result = super.kEventMouseDown (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	wasSelected = wasExpanded = false;
	result = OS.CallNextEventHandler (nextHandler, theEvent);
	if (isDisposed ()) return OS.noErr;
	if (!wasSelected && !wasExpanded) {
		if (OS.IsDataBrowserItemSelected (handle, lastHittest)) {
			if (0 < lastHittest && lastHittest <= items.length) {
				Event event = new Event ();
				event.item = _getItem (lastHittest, true);
				postEvent (SWT.Selection, event);
			}
		}
	}
	wasSelected = wasExpanded = false;
	return result;
}

void releaseItem (TreeItem item, boolean release) {
	int id = item.id;
	if (release) item.release (false);
	if (savedAnchor == id) savedAnchor = 0;
	items [id - 1] = null;
	TreeItem parentItem = item.parentItem;
	int [] ids = parentItem == null ? childIds : parentItem.childIds;
	int index = -1;
	for (int i = 0; i < ids.length; i++) {
		if (ids [i] == id) {
			index = i; break;
		}
	}
	if (index != -1) {
		System.arraycopy(ids, 0, ids, 0, index);
		System.arraycopy(ids, index+1, ids, index, ids.length - index - 1);
		ids [ids.length - 1] = 0;
	}
	if (parentItem != null) {
		parentItem.itemCount--;
		if (parentItem.itemCount == 0) parentItem.childIds = null;
	}
}

void releaseItems (int [] ids) {
	if (ids == null) return;
	for (int i=ids.length-1; i>= 0; i--) {
		TreeItem item = _getItem (ids [i], false);
		if (item != null) {
			releaseItems (item.childIds);
			if (!isDisposed ()) {
				releaseItem (item, true);
			}
		}
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
	childIds = null;
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

void releaseWidget () {
	super.releaseWidget ();
	sortColumn = null;
	paintGC = null;
	imageBounds = null;
	showItem = null;
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
	callbacks.v1_itemCompareCallback = 0;
	OS.SetDataBrowserCallbacks (handle, callbacks);
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
	childIds = null;
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
	callbacks.v1_itemCompareCallback = 0;
	OS.SetDataBrowserCallbacks (handle, callbacks);
	ignoreExpand = ignoreSelect = true;
	int result = OS.RemoveDataBrowserItems (handle, OS.kDataBrowserNoItem, 0, null, 0);
	ignoreExpand = ignoreSelect = false;
	callbacks.v1_itemNotificationCallback = display.itemNotificationProc;
	callbacks.v1_itemCompareCallback = display.itemCompareProc;
	OS.SetDataBrowserCallbacks (handle, callbacks);
	if (result != OS.noErr) error (SWT.ERROR_ITEM_NOT_REMOVED);
	OS.SetDataBrowserScrollPosition (handle, 0, 0);
	savedAnchor = anchorFirst = anchorLast = 0;
	visibleCount = 0;
	setScrollWidth (true);
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
	if (item != null && item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	TreeItem oldMark = insertItem;
	insertItem = item;
	insertBefore = before;
	if (oldMark != null && !oldMark.isDisposed()) oldMark.redraw (OS.kDataBrowserNoItem);
	if (item != null) item.redraw (OS.kDataBrowserNoItem);
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
	ignoreSelect = true;
	OS.SetDataBrowserSelectedItems (handle, 0, null, OS.kDataBrowserItemsAssign);
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
	showItem (item, false);
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
	OS.SetDataBrowserSelectedItems (handle, 1, new int [] {item.id}, (style & SWT.MULTI) != 0 ? OS.kDataBrowserItemsAdd : OS.kDataBrowserItemsAssign);
	if ((style & SWT.SINGLE) != 0) {
		OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0]);
	}
	ignoreSelect = false;
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
	* Ensure that the selection is visible when the tree is resized
	* from a zero size to a size that can show the selection.
	*/
	int result = super.setBounds (x, y, width, height, move, resize, events);
	if (showItem != null && !showItem.isDisposed ()) {
		showItem (showItem , true);
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
		OS.GetDataBrowserListViewDisclosureColumn (handle, disclosure, expandableRows);
		TreeColumn firstColumn = columns [order [0]];
		if (disclosure [0] != firstColumn.id) {
			OS.SetDataBrowserListViewDisclosureColumn (handle, firstColumn.id, expandableRows [0]);
		}
		int x = 0;
		short [] width = new short [1];
		int [] oldX = new int [oldOrder.length];
		for (int i=0; i<oldOrder.length; i++) {
			int index = oldOrder [i];
			TreeColumn column = columns [index];
			oldX [index] =  x;
			OS.GetDataBrowserTableViewNamedColumnWidth(handle, column.id, width);
			x += width [0];
		}
		x = 0;
		int [] newX = new int [order.length];
		for (int i=0; i<order.length; i++) {
			int index = order [i];
			TreeColumn column = columns [index];
			int position = (style & SWT.CHECK) != 0 ? i + 1 : i;
			OS.SetDataBrowserTableViewColumnPosition(handle, column.id, position);
			column.lastPosition = position;
			newX [index] =  x;
			OS.GetDataBrowserTableViewNamedColumnWidth(handle, column.id, width);
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

void setFontStyle (Font font) {
	super.setFontStyle (font);
	if (items == null) return;
	for (int i = 0; i < items.length; i++) {
		TreeItem item = items [i];
		if (item != null) item.width = -1;
	}
	setScrollWidth (true);
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
	short [] height = new short [1];
	OS.GetDataBrowserListViewHeaderBtnHeight (handle, height);
	if ((height [0] != 0) != show) {
		OS.SetDataBrowserListViewHeaderBtnHeight (handle, (short) (show ? headerHeight : 0));
		invalidateVisibleRegion (handle);
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
	setItemCount (null, count);
}

void setItemCount (TreeItem parentItem, int count) {
	int itemCount = getItemCount (parentItem);
	if (count == itemCount) return;
	setRedraw (false);
	int [] top = new int [1], left = new int [1];
    OS.GetDataBrowserScrollPosition (handle, top, left);
    DataBrowserCallbacks callbacks = new DataBrowserCallbacks ();
	OS.GetDataBrowserCallbacks (handle, callbacks);
	callbacks.v1_itemNotificationCallback = 0;
	callbacks.v1_itemCompareCallback = 0;
	OS.SetDataBrowserCallbacks (handle, callbacks);
	int [] ids = parentItem == null ? childIds : parentItem.childIds;
	int removeCount = 0;
	if (count < itemCount) {
		int oldAnchor = savedAnchor;
		int selectionCount = getSelectionCount ();
		int [] removeIds = new int [itemCount - count];
		for (int index = ids.length - 1; index >= count; index--) {
			int id = ids [index];
			if (id != 0) {
				TreeItem item = _getItem (id, false);
				if (item != null && !item.isDisposed ()) {
					item.dispose ();
				} else {
					if (parentItem == null || parentItem.getExpanded ()) {
						removeIds [removeIds.length - removeCount - 1] = id;
						removeCount++;
						visibleCount--;
					}
				}
			}
		}
		if (removeCount != 0 && removeCount != removeIds.length) {
			int [] tmp = new int [removeCount];
			System.arraycopy(removeIds, removeIds.length - removeCount, tmp, 0, removeCount);
			removeIds = tmp;
		}
		if (removeCount != 0 && OS.RemoveDataBrowserItems (handle, OS.kDataBrowserNoItem, removeCount, removeIds, 0) != OS.noErr) {
			error (SWT.ERROR_ITEM_NOT_REMOVED);
		}
		boolean fixAnchor = selectionCount != 0 && getSelectionCount () == 0;
		if (fixAnchor || (savedAnchor != 0 && savedAnchor != oldAnchor)) {
			savedAnchor = 0;
			if (count == 0) {
				savedAnchor = parentItem == null ? 0 : parentItem.id;
			} else {
				int index = count - 1;
				if (0 <= index && index < ids.length) {
					savedAnchor = ids [index];
				}
			}
		}
		
		//TODO - move shrink to paint event
		// shrink items array
		int lastIndex = items.length;
		for (int i=items.length; i>0; i--) {
			if (items [i-1] != null) {
				lastIndex = i;
				break;
			}
		}
		if (lastIndex < items.length - 4) {
			int length = Math.max (4, (lastIndex + 3) / 4 * 4);
			TreeItem [] newItems = new TreeItem [length];
			System.arraycopy(items, 0, newItems, 0, Math.min(items.length, lastIndex));
			items = newItems;
		}
	}
	
	if (parentItem != null) parentItem.itemCount = count;
	int length = Math.max (4, (count + 3) / 4 * 4);
	int [] newIds = new int [length];
	if (ids != null) {
		System.arraycopy (ids, 0, newIds, 0, Math.min (count, itemCount));
	}
	ids = newIds;
	if (parentItem == null) {
		childIds = newIds;
	} else {
		parentItem.childIds = newIds;
	}
	
	if (count > itemCount) {
		if ((getStyle() & SWT.VIRTUAL) == 0) {
			int delta = Math.max (4, (count - itemCount + 3) / 4 * 4);
			TreeItem [] newItems = new TreeItem [items.length + delta];
			System.arraycopy (items, 0, newItems, 0, items.length);
			items = newItems;
			for (int i=itemCount; i<count; i++) {
				items [i] = new TreeItem (this, parentItem, SWT.NONE, i, true);
			}
		} else {
			if (parentItem == null || parentItem.getExpanded ()) {
				int parentID = parentItem == null ? OS.kDataBrowserNoItem : parentItem.id;
				int [] addIds = _getIds (count - itemCount);
				if (OS.AddDataBrowserItems (handle, parentID, addIds.length, addIds, OS.kDataBrowserItemNoProperty) != OS.noErr) {
					error (SWT.ERROR_ITEM_NOT_ADDED);
				}
				visibleCount += (count - itemCount);
				System.arraycopy (addIds, 0, ids, itemCount, addIds.length);
			}
		}
	}
	
	callbacks.v1_itemNotificationCallback = display.itemNotificationProc;
	callbacks.v1_itemCompareCallback = display.itemCompareProc;
	OS.SetDataBrowserCallbacks (handle, callbacks);
	if ((style & SWT.VIRTUAL) != 0 && sortColumn != null  
			&& !sortColumn.isDisposed () && sortDirection == SWT.DOWN) {
		OS.UpdateDataBrowserItems (handle, 0, 0, null, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
	}
	setRedraw (true);
	if (itemCount == 0 && parentItem != null) parentItem.redraw (OS.kDataBrowserNoItem);
	if (removeCount != 0) fixScrollBar ();
}

/*public*/ void setItemHeight (int itemHeight) {
	checkWidget ();
	if (itemHeight < -1) error (SWT.ERROR_INVALID_ARGUMENT);
	if (itemHeight == -1) {
		//TODO - reset item height, ensure other API's such as setFont don't do this
	} else {
		OS.SetDataBrowserTableViewRowHeight (handle, (short) itemHeight);
	}
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
public void setLinesVisible (boolean show) {
	checkWidget ();
	if (OS.VERSION >= 0x1040) {
		int attrib = OS.kDataBrowserAttributeListViewAlternatingRowColors | OS.kDataBrowserAttributeListViewDrawColumnDividers;
		OS.DataBrowserChangeAttributes (handle, show ? attrib : 0, !show ? attrib : 0);
		redraw ();
	}
}

public void setRedraw (boolean redraw) {
	checkWidget();
	super.setRedraw (redraw);
	if (redraw && drawCount == 0) {
		setScrollWidth (true);
	}
}

boolean setScrollWidth (TreeItem item) {
	if (ignoreRedraw || !getDrawing ()) return false;
	if (columnCount != 0) return false;
	TreeItem parentItem = item.parentItem;
	if (parentItem != null && !parentItem._getExpanded ()) return false;
	GC gc = new GC (this);
	int newWidth = item.calculateWidth (0, gc);
	gc.dispose ();
	newWidth += getInsetWidth (column_id, false);
	short [] width = new short [1];
	OS.GetDataBrowserTableViewNamedColumnWidth (handle, column_id, width);
	if (width [0] < newWidth) {
		OS.SetDataBrowserTableViewNamedColumnWidth (handle, column_id, (short) newWidth);
		return true;
	}
	return false;
}

boolean setScrollBarVisible (ScrollBar bar, boolean visible) {
	boolean [] horiz = new boolean [1], vert = new boolean [1];
	OS.GetDataBrowserHasScrollBars (handle, horiz, vert);
	if ((bar.style & SWT.H_SCROLL) != 0) horiz [0] = visible;
	if ((bar.style & SWT.V_SCROLL) != 0) vert [0] = visible;
	if (!visible) {
		bar.redraw ();
		bar.deregister ();
	}
	if (OS.SetDataBrowserHasScrollBars (handle, horiz [0], vert [0]) == OS.noErr) {
		if (visible) {
			bar.handle = findStandardBar (bar.style);
			bar.register ();
			bar.hookEvents ();
			bar.redraw ();
		} else {
			bar.handle = 0;
		}
		return true;
	} else {
		if (!visible) {
			bar.register ();
		}
	}
	return false;
}

boolean setScrollWidth (boolean set) {
	return setScrollWidth(set, childIds, true);
}

boolean setScrollWidth (boolean set, int[] childIds, boolean recurse) {
	if (ignoreRedraw || !getDrawing ()) return false;
	if (columnCount != 0 || childIds == null) return false;
	GC gc = new GC (this);
	int newWidth = calculateWidth (childIds, gc, recurse, 0, 0);
	gc.dispose ();
	newWidth += getInsetWidth (column_id, false);
	if (!set) {
		short [] width = new short [1];
		OS.GetDataBrowserTableViewNamedColumnWidth (handle, column_id, width);
		if (width [0] >= newWidth) return false;
	}
	OS.SetDataBrowserTableViewNamedColumnWidth (handle, column_id, (short) newWidth);
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
	int length = items.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	int count = 0;
	int[] ids = new int [length];
	for (int i=0; i<length; i++) {
		if (items [i] != null) {
			if (items [i].isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
			ids [count++] = items [i].id;
			showItem (items [i], false);
		}
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
	OS.SetDataBrowserSelectedItems (handle, count, ids, OS.kDataBrowserItemsAssign);
	if ((style & SWT.SINGLE) != 0) {
		OS.SetDataBrowserSelectionFlags (handle, selectionFlags [0]);
	}
	ignoreSelect = false;
	if (length > 0) {
		int index = -1;
		for (int i=0; i<items.length; i++) {
			if (items [i] != null) {
				index = i;
				break;
			}
		}
		if (index != -1) showItem (items [index], true);
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
	if (column == null) {
		if (sortColumn != null  && !sortColumn.isDisposed ()  && sortDirection != SWT.NONE) {
			OS.SetDataBrowserSortOrder (handle, (short) OS.kDataBrowserOrderIncreasing);
			sortColumn = null; 
			OS.SetDataBrowserSortProperty (handle, 0);
		}
	}
	sortColumn = column;
	if (sortColumn != null  && !sortColumn.isDisposed () && sortDirection != SWT.NONE) {
		OS.SetDataBrowserSortProperty (handle, sortColumn.id);
		int order = sortDirection == SWT.DOWN ? OS.kDataBrowserOrderDecreasing : OS.kDataBrowserOrderIncreasing;
		OS.SetDataBrowserSortOrder (handle, (short) order);
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
public void setSortDirection  (int direction) {
	checkWidget ();
	if (direction != SWT.UP && direction != SWT.DOWN && direction != SWT.NONE) return;
	if (direction == sortDirection) return;
	sortDirection = direction;
	if (sortColumn != null && !sortColumn.isDisposed ()) {
		if (sortDirection == SWT.NONE) {
			OS.SetDataBrowserSortOrder (handle, (short) OS.kDataBrowserOrderIncreasing);
			TreeColumn column = sortColumn;
			sortColumn = null; 
			OS.SetDataBrowserSortProperty (handle, 0);
			sortColumn = column;
		} else {
			OS.SetDataBrowserSortProperty (handle, 0);
			OS.SetDataBrowserSortProperty (handle, sortColumn.id);
			int order = sortDirection == SWT.DOWN ? OS.kDataBrowserOrderDecreasing : OS.kDataBrowserOrderIncreasing;
			OS.SetDataBrowserSortOrder (handle, (short) order);
		}
	}
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
	int itemHeight = getItemHeight ();
    int [] top = new int [1], left = new int [1];
    OS.GetDataBrowserScrollPosition (handle, top, left);
    int [] index = new int [1];
    OS.GetDataBrowserTableViewItemRow (handle, item.id, index);
    top [0] = Math.max (0, Math.min (itemHeight * visibleCount + getHeaderHeight () - getClientArea ().height, index [0] * itemHeight));
    OS.SetDataBrowserScrollPosition (handle, top [0], item.parentItem != null ? left [0] : 0);
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
	while (parentItem != null && !parentItem._getExpanded ()) {
		count++;
		parentItem = parentItem.parentItem;
	}
	int index = 0;
	parentItem = item.parentItem;
	TreeItem [] path = new TreeItem [count];
	while (parentItem != null && !parentItem._getExpanded ()) {
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
		int columnId = (style & SWT.CHECK) != 0 ? CHECK_COLUMN_ID : columnCount == 0 ? column_id : columns [0].id;
		int options = OS.kDataBrowserRevealWithoutSelecting;
		/*
		* This code is intentionally commented, since kDataBrowserRevealAndCenterInView
		* does not scroll the item to the center always (it seems to scroll to the
		* end in some cases).
		*/
		//options |= OS.kDataBrowserRevealAndCenterInView;
		OS.RevealDataBrowserItem (handle, item.id, columnId, (byte) options);
		int [] newTop = new int [1], newLeft = new int [1];
		if (columnCount == 0) {
			boolean fixScroll = false;
			Rect content = new Rect ();
			if (OS.GetDataBrowserItemPartBounds (handle, item.id, columnId, OS.kDataBrowserPropertyContentPart, content) == OS.noErr) {
				fixScroll = content.left < rect.x || content.left >= rect.x + rect.width;
				if (!fixScroll) {
					GC gc = new GC (this);
					int contentWidth = calculateWidth (new int[]{item.id}, gc, false, 0, 0);
					gc.dispose ();
					fixScroll =  content.left + contentWidth > rect.x + rect.width;
				}
			}
			if (fixScroll) {
				int leftScroll = getLeftDisclosureInset (columnId);
				int levelIndent = DISCLOSURE_COLUMN_LEVEL_INDENT;
				if (OS.VERSION >= 0x1040) {
					float [] metric = new float [1];
					OS.DataBrowserGetMetric (handle, OS.kDataBrowserMetricDisclosureColumnPerDepthGap, null, metric);
					levelIndent = (int) metric [0];
				}
				TreeItem temp = item;
				while (temp.parentItem != null) {
					leftScroll += levelIndent;
					temp = temp.parentItem;
				}
				OS.GetDataBrowserScrollPosition (handle, newTop, newLeft);
				OS.SetDataBrowserScrollPosition (handle, newTop [0], leftScroll);
			}
		}

		/*
		* Bug in the Macintosh.  For some reason, when the DataBrowser is scrolled
		* by RevealDataBrowserItem(), the scrollbars are not redrawn.  The fix is to
		* force a redraw.
		*/
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
	//checkItems (false);
	//TODO - optimize
	TreeItem [] selection = getSelection ();
	if (selection.length > 0) showItem (selection [0], true);
}

int trackingProc (int browser, int id, int property, int theRect, int startPt, int modifiers) {
	return 1;
}

}

