package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import java.util.*;

import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class provide a selectable user interface object
 * that displays a hierarchy of items and issue notificiation when an
 * item in the hierarchy is selected.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TreeItem2</code>.
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
public class Tree2 extends Composite {
	/* AW
	int hAnchor;
	*/
	TreeItem2 [] items;
	
	// AW
	static final int CHECK_COL_ID= 12345;
	static final int COL_ID= 12346;
	private static final int FIRST_ROW_ID= 1000;
	
	TreeItem2 fRoot;
	// AW
	
	/* AW
	ImageList imageList;
	boolean dragStarted;
	boolean ignoreSelect, ignoreExpand, ignoreDeselect;
	boolean customDraw;
	*/

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
public Tree2 (Composite parent, int style) {
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
	int width = 0, height = 0;
	/* AW
	RECT rect = new RECT ();
	int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	while (hItem != 0) {
		rect.left = hItem;
		if (OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect) != 0) {
			width = Math.max (width, rect.right - rect.left);
			height += rect.bottom - rect.top;
		}
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
	}
	width = width * 2;
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int border = getBorderWidth ();
	width += border * 2;  height += border * 2;
	if ((style & SWT.V_SCROLL) != 0) {
		width += OS.GetSystemMetrics (OS.SM_CXVSCROLL);
	}
	if ((style & SWT.H_SCROLL) != 0) {
		height += OS.GetSystemMetrics (OS.SM_CYHSCROLL);
	}
	*/
	width= 200;
	height= 200;
	return new Point (width, height);
}

void createHandle (int index) {
	/* AW		FIXME!!!!
	super.createHandle ();
	*/
	state |= HANDLE;
	state &= ~CANVAS;
		
	int parentHandle = parent.handle;
	int windowHandle= OS.GetControlOwner(parentHandle);
	handle= OS.createDataBrowserControl(windowHandle);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	MacUtil.addControl(handle, parentHandle);
	MacUtil.initLocation(handle);
	
	/* Single or Multiple Selection */
	int mode= OS.kDataBrowserSelectOnlyOne | OS.kDataBrowserNeverEmptySelectionSet;
	if ((style & SWT.MULTI) != 0)
		mode= OS.kDataBrowserDragSelect | OS.kDataBrowserCmdTogglesSelection;
	OS.SetDataBrowserSelectionFlags(handle, mode);
	
	/* hide the header */
	OS.SetDataBrowserListViewHeaderBtnHeight(handle, (short) 0);
	
	/* enable scrollbars */
	OS.SetDataBrowserHasScrollBars(handle, (style & SWT.H_SCROLL) != 0, (style & SWT.V_SCROLL) != 0);
	if ((style & SWT.H_SCROLL) == 0)
		OS.AutoSizeDataBrowserListViewColumns(handle);
		
	if ((style & SWT.CHECK) != 0) {
		int checkColumnDesc= OS.newColumnDesc(CHECK_COL_ID, OS.kDataBrowserCheckboxType,
						OS.kDataBrowserPropertyIsMutable,
						(short)40, (short)40);
		OS.AddDataBrowserListViewColumn(handle, checkColumnDesc, 1999);
	}
					
	int columnDesc= OS.newColumnDesc(COL_ID, OS.kDataBrowserTextType, // OS.kDataBrowserIconAndTextType,
					OS.kDataBrowserListViewSelectionColumn | OS.kDataBrowserDefaultPropertyFlags,
					(short)0, (short)300);
	OS.AddDataBrowserListViewColumn(handle, columnDesc, 2000);
	OS.SetDataBrowserListViewDisclosureColumn(handle, COL_ID, false);
	
	/*
	Display disp= getDisplay();
	Font font= Font.carbon_new (disp, disp.getThemeFont(OS.kThemeSmallSystemFont));
	if (OS.SetControlFontStyle(handle, font.handle.fID, font.handle.fSize, font.handle.fFace) != OS.kNoErr)
		System.out.println("Tree2.setFont("+this+"): error");
	*/
}

void createItem (TreeItem2 item, TreeItem2 itemParent, int hInsertAfter) {
	item.foreground = item.background = -1;
	
	int id = 0;
	while (id < items.length && items [id] != null) id++;
	if (id == items.length) {
		TreeItem2 [] newItems = new TreeItem2 [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	items [id] = item;
	
	item.handle= id + FIRST_ROW_ID;
		
	if (itemParent != null)
		itemParent.addChild(item);
}

ScrollBar createScrollBar (int type) {
	return createStandardBar (type);
}

void createWidget (int index) {
	super.createWidget (index);
	items = new TreeItem2 [4];

	fRoot= new TreeItem2(this);
	fRoot.setText("Root");
	fRoot.fIsOpen= true;
	//OS.SetDataBrowserTarget(handle, fRoot.handle);	// opens node
}

int defaultBackground () {
	/* AW
	return OS.GetSysColor (OS.COLOR_WINDOW);
	*/
	return 0x00FFFFFF;
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
	/* AW
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_SELECTED;
	if ((style & SWT.SINGLE) != 0) {
		int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		if (hItem != 0) {
			tvItem.hItem = hItem;
			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
		}
		return;
	}
	int oldProc = OS.GetWindowLong (handle, OS.GWL_WNDPROC);
	OS.SetWindowLong (handle, OS.GWL_WNDPROC, TreeProc);	
	for (int i=0; i<items.length; i++) {
		TreeItem2 item = items [i];
		if (item != null) {
			tvItem.hItem = item.handle;
			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
		}
	}
	OS.SetWindowLong (handle, OS.GWL_WNDPROC, oldProc);
	*/
	/*
	int n= fData.size();
	if (n <= 0) return;
	int[] ids= getIds(0, n-1);
	OS.SetDataBrowserSelectedItems(handle, ids.length, ids, OS.kDataBrowserItemsRemove);
	*/
}

void destroyItem (TreeItem2 item) {
	int hItem = item.handle;
	/* AW
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
	*/
	releaseItems (new TreeItem2 [] {item} /*, tvItem */);
	/* AW
	boolean fixRedraw = false;
	if (drawCount == 0 && OS.IsWindowVisible (handle)) {
		RECT rect = new RECT ();
		rect.left = hItem;
		fixRedraw = OS.SendMessage (handle, OS.TVM_GETITEMRECT, 0, rect) == 0;
	}
	if (fixRedraw) {
		OS.UpdateWindow (handle);
		OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
	}
	OS.SendMessage (handle, OS.TVM_DELETEITEM, 0, hItem);
	*/
	TreeItem2 parent= item.getParentItem();
	if (parent != null)
		parent.removeChild(item);
	/*
	if (fixRedraw) {
		OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
		OS.ValidateRect (handle, null);
	}
	int count = OS.SendMessage (handle, OS.TVM_GETCOUNT, 0, 0);
	if (count == 0) {
		if (imageList != null) {
			OS.SendMessage (handle, OS.TVM_SETIMAGELIST, 0, 0);
			Display display = getDisplay ();
			display.releaseImageList (imageList);
		}
		imageList = null;
		customDraw = false;
		items = new TreeItem2 [4];	
	}
	*/
}

int getBackgroundPixel () {
	/* AW
	if (OS.IsWinCE) return OS.GetSysColor (OS.COLOR_WINDOW);
	int pixel = OS.SendMessage (handle, OS.TVM_GETBKCOLOR, 0, 0);
	if (pixel == -1) return OS.GetSysColor (OS.COLOR_WINDOW);
	return pixel;
	*/
	return 0x00ffffff;
}

int getForegroundPixel () {
	/* AW
	if (OS.IsWinCE) return OS.GetSysColor (OS.COLOR_WINDOWTEXT);
	int pixel = OS.SendMessage (handle, OS.TVM_GETTEXTCOLOR, 0, 0);
	if (pixel == -1) return OS.GetSysColor (OS.COLOR_WINDOWTEXT);
	return pixel;
	*/
	return 0x00000000;
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
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem2 getItem (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	/* AW
	TVHITTESTINFO lpht = new TVHITTESTINFO ();
	lpht.x = point.x;	 lpht.y = point.y;
	OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
	if (lpht.hItem != 0 && (lpht.flags & OS.TVHT_ONITEM) != 0) {
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
		tvItem.hItem = lpht.hItem;
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
		return items [tvItem.lParam];	
	}
	*/
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
	return fRoot.getItemCount();
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
	/* AW
	return OS.SendMessage (handle, OS.TVM_GETITEMHEIGHT, 0, 0);
	*/
	return 15;
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
public TreeItem2 [] getItems () {
	checkWidget ();
	return fRoot.getItems();
}

/**
 * Returns the receiver's parent item, which must be a
 * <code>TreeItem2</code> or null when the receiver is a
 * root.
 *
 * @return the receiver's parent item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem2 getParentItem () {
	checkWidget ();
	return null;
}

/**
 * Returns an array of <code>TreeItem2</code>s that are currently
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
public TreeItem2 [] getSelection () {
	checkWidget ();
	int[] ids= MacUtil.getSelectionIDs(handle, OS.kDataBrowserNoItem, true);
	TreeItem2[] result= new TreeItem2[ids.length];
	for (int i= 0; i < ids.length; i++)
		result[i]= find(ids[i]);
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
	int[] result= new int[1];
	if (OS.GetDataBrowserItemCount(handle, OS.kDataBrowserNoItem, true, OS.kDataBrowserItemIsSelected, result) != OS.kNoErr)
		error (SWT.ERROR_CANNOT_GET_COUNT);
	return result[0];
}

void hookEvents () {
	super.hookEvents ();
	Display display= getDisplay();
	OS.setDataBrowserCallbacks(handle, display.fDataBrowserDataProc,
				display.fDataBrowserCompareProc, display.fDataBrowserItemNotificationProc);
}

/* AW
int imageIndex (Image image) {
	if (image == null) return OS.I_IMAGENONE;
	if (imageList == null) {
		int hOldList = OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_NORMAL, 0);
		if (hOldList != 0) OS.ImageList_Destroy (hOldList);
		Rectangle bounds = image.getBounds ();
		imageList = getDisplay ().getImageList (new Point (bounds.width, bounds.height));
		int index = imageList.indexOf (image);
		if (index == -1) index = imageList.add (image);
		int hImageList = imageList.getHandle ();
		OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_NORMAL, hImageList);
		return index;
	}
	int index = imageList.indexOf (image);
	if (index != -1) return index;
	return imageList.add (image);
}
*/

void releaseItems (TreeItem2 [] nodes /*, TVITEM tvItem */) {
	for (int i=0; i<nodes.length; i++) {
		TreeItem2 item = nodes [i];
		TreeItem2 [] sons = item.getItems ();
		if (sons.length != 0) {
			releaseItems (sons /*, tvItem */);
		}
		int hItem = item.handle;
		/* AW
		if (hItem == hAnchor) hAnchor = 0;
		*/
		if (!item.isDisposed ()) {
			/* AW
			tvItem.hItem = hItem;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			items [tvItem.lParam] = null;
			*/
			items [hItem - FIRST_ROW_ID] = null;
			item.releaseResources ();
		}
	}
}

void releaseWidget () {
	for (int i=0; i<items.length; i++) {
		TreeItem2 item = items [i];
		if (item != null && !item.isDisposed ()) {
			item.releaseResources ();
		}
	}
	/*
	* Feature in Windows.  For some reason, when
	* TVM_GETIMAGELIST or TVM_SETIMAGELIST is sent,
	* the tree issues NM_CUSTOMDRAW messages.  This
	* behavior is unwanted when the tree is being
	* disposed.  The fix is to ingore NM_CUSTOMDRAW
	* messages by usnig the custom draw flag.
	* 
	* NOTE: This only happens on Windows XP.
	*/
	/* AW
	customDraw = false;
	items = null;
	if (imageList != null) {
		OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_NORMAL, 0);
		Display display = getDisplay ();
		display.releaseImageList (imageList);
	} else {
		int hOldList = OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_NORMAL, 0);
		OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_NORMAL, 0);
		if (hOldList != 0) OS.ImageList_Destroy (hOldList);
	}
	imageList = null;
	int hOldList = OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_STATE, 0);
	OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_STATE, 0);
	if (hOldList != 0) OS.ImageList_Destroy (hOldList);
	*/
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
	/* AW
	ignoreDeselect = ignoreSelect = true;
	int result = OS.SendMessage (handle, OS.TVM_DELETEITEM, 0, OS.TVI_ROOT);
	ignoreDeselect = ignoreSelect = false;
	if (result == 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
	*/
	for (int i=0; i<items.length; i++) {
		TreeItem2 item = items [i];
		if (item != null && !item.isDisposed ()) {
			item.releaseResources ();
		}
	}
	/* AW
	if (imageList != null) {
		OS.SendMessage (handle, OS.TVM_SETIMAGELIST, 0, 0);
		Display display = getDisplay ();
		display.releaseImageList (imageList);
	}
	imageList = null;
	customDraw = false;
	*/
	items = new TreeItem2 [4];
	/* AW
	hAnchor = 0;
	*/
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
public void setInsertMark (TreeItem2 item, boolean before) {
	checkWidget ();
	int hItem = 0;
	if (item != null) {
		if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		hItem = item.handle;
	}
	/* AW
	OS.SendMessage (handle, OS.TVM_SETINSERTMARK, (before) ? 0 : 1, hItem);
	*/
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
	int[] ids= MacUtil.getDataBrowserItems(handle, OS.kDataBrowserNoItem, 0, true);
	OS.SetDataBrowserSelectedItems(handle, ids.length, ids, OS.kDataBrowserItemsAssign);
}

void setBackgroundPixel (int pixel) {
	if (background == pixel) return;
	background = pixel;
	/*
	* Bug in Windows.  When TVM_GETBKCOLOR is used more
	* than once to set the background color of a tree,
	* the background color of the lines and the plus/minus
	* does not change to the new color.  The fix is to set
	* the background color to the default before setting
	* the new color.
	*/
	/* AW
	int oldPixel = OS.SendMessage (handle, OS.TVM_GETBKCOLOR, 0, 0);
	if (oldPixel != -1) OS.SendMessage (handle, OS.TVM_SETBKCOLOR, 0, -1);
	OS.SendMessage (handle, OS.TVM_SETBKCOLOR, 0, pixel);
	if ((style & SWT.CHECK) != 0) setCheckboxImageList ();
	*/
}

void setForegroundPixel (int pixel) {
	if (foreground == pixel) return;
	foreground = pixel;
	/* AW
	OS.SendMessage (handle, OS.TVM_SETTEXTCOLOR, 0, pixel);
	*/
}

public void setRedraw (boolean redraw) {
	checkWidget ();
	/*
	* Bug in Windows.  For some reason, when WM_SETREDRAW
	* is used to turn redraw on for a tree and the tree
	* contains no items, the last item in the tree does
	* not redraw properly.  If the tree has only one item,
	* that item is not drawn.  If another window is dragged
	* on top of the item, parts of the item are redrawn
	* and erased at random.  The fix is to ensure that this
	* case doesn't happen by inserting and deleting an item
	* when redraw is turned on and there are no items in
	* the tree.
	*/
	/* AW
	int hItem = 0;
	if (redraw) {
		int count = OS.SendMessage (handle, OS.TVM_GETCOUNT, 0, 0);
		if (count == 0) {
			TVINSERTSTRUCT tvInsert = new TVINSERTSTRUCT ();
			tvInsert.hInsertAfter = OS.TVI_FIRST;
			hItem = OS.SendMessage (handle, OS.TVM_INSERTITEM, 0, tvInsert);
		}
	}
	super.setRedraw (redraw);
	if (hItem != 0) {
		OS.SendMessage (handle, OS.TVM_DELETEITEM, 0, hItem);
	}
	*/
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
 * @see Tree2#deselectAll()
 */
public void setSelection (TreeItem2 [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	int[] ids= new int[items.length];
	for (int i= 0; i < items.length; i++)
		ids[i]= items[i].handle;
	OS.SetDataBrowserSelectedItems(handle, ids.length, ids, OS.kDataBrowserItemsAssign);
}

void showItem (int hItem) {
	if (OS.RevealDataBrowserItem(handle, hItem, COL_ID, false) != OS.kNoErr)
		System.out.println("Tree2.RevealDataBrowserItem");
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
 * @see Tree2#showSelection()
 */
public void showItem (TreeItem2 item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
	showItem (item.handle);
}

/**
 * Shows the selection.  If the selection is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled until
 * the selection is visible.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree2#showItem(TreeItem2)
 */
public void showSelection () {
	checkWidget ();
	int[] ids= MacUtil.getSelectionIDs(handle, OS.kDataBrowserNoItem, true);
	if (ids.length > 0 && ids[0] != 0)
		OS.RevealDataBrowserItem(handle, ids[0], COL_ID, false);
}

/////////////////////////////////////////////////////////////////////////////////////////////////

/* AW
LRESULT WM_KEYDOWN (int wParam, int lParam) {
	LRESULT result = super.WM_KEYDOWN (wParam, lParam);
	if (result != null) return result;
	switch (wParam) {
		case OS.VK_SPACE: {
			int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
			if (hItem != 0) {
				hAnchor = hItem;
				OS.SendMessage (handle, OS.TVM_ENSUREVISIBLE, 0, hItem);
				TVITEM tvItem = new TVITEM ();
				tvItem.mask = OS.TVIF_STATE | OS.TVIF_PARAM;
				tvItem.hItem = hItem;
				if ((style & SWT.CHECK) != 0) {
					tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
					int state = tvItem.state >> 12;
					if ((state & 0x1) != 0) {
						state++;
					} else  {
						--state;
					}
					tvItem.state = state << 12;
					OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
				}
				tvItem.stateMask = OS.TVIS_SELECTED;
				OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
				if ((style & SWT.MULTI) != 0 && OS.GetKeyState (OS.VK_CONTROL) < 0) {
					if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
						tvItem.state &= ~OS.TVIS_SELECTED;
					} else {
						tvItem.state |= OS.TVIS_SELECTED;
					}
				} else {
					tvItem.state |= OS.TVIS_SELECTED;
				}
				OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
				Event event = new Event ();
				event.item = items [tvItem.lParam];
				postEvent (SWT.Selection, event);
				if ((style & SWT.CHECK) != 0) {
					event = new Event ();
					event.item = items [tvItem.lParam];
					event.detail = SWT.CHECK;
					postEvent (SWT.Selection, event);
				}
				return LRESULT.ZERO;
			}
			break;
		}
		case OS.VK_UP:
		case OS.VK_DOWN:
		case OS.VK_PRIOR:
		case OS.VK_NEXT:
		case OS.VK_HOME:
		case OS.VK_END: {
			if ((style & SWT.SINGLE) != 0) break;
			if (OS.GetKeyState (OS.VK_SHIFT) < 0) {
				int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
				if (hItem != 0) {
					if (hAnchor == 0) hAnchor = hItem;
					ignoreSelect = ignoreDeselect = true;
					int code = callWindowProc (OS.WM_KEYDOWN, wParam, lParam);
					ignoreSelect = ignoreDeselect = false;
					int hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
					TVITEM tvItem = new TVITEM ();
					tvItem.mask = OS.TVIF_STATE;
					tvItem.stateMask = OS.TVIS_SELECTED;
					int hDeselectItem = hItem;					
					RECT rect1 = new RECT ();
					rect1.left = hAnchor;
					OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect1);
					RECT rect2 = rect2 = new RECT ();
					rect2.left = hDeselectItem;
					OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect2);
					int flags = rect1.top < rect2.top ? OS.TVGN_PREVIOUSVISIBLE : OS.TVGN_NEXTVISIBLE;
					while (hDeselectItem != hAnchor) {
						tvItem.hItem = hDeselectItem;
						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
						hDeselectItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, flags, hDeselectItem);
					}
					int hSelectItem = hAnchor;
					rect1.left = hNewItem;
					OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect1);
					rect2.left = hSelectItem;
					OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect2);
					tvItem.state = OS.TVIS_SELECTED;
					flags = rect1.top < rect2.top ? OS.TVGN_PREVIOUSVISIBLE : OS.TVGN_NEXTVISIBLE;
					while (hSelectItem != hNewItem) {
						tvItem.hItem = hSelectItem;
						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
						hSelectItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, flags, hSelectItem);
					}
					tvItem.hItem = hNewItem;
					OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
					tvItem.mask = OS.TVIF_PARAM;
					tvItem.hItem = hNewItem;
					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
					Event event = new Event ();
					event.item = items [tvItem.lParam];
					postEvent (SWT.Selection, event);
					return new LRESULT (code);
				}
			}
			if (OS.GetKeyState (OS.VK_CONTROL) < 0) {
				int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
				if (hItem != 0) {
					TVITEM tvItem = new TVITEM ();
					tvItem.mask = OS.TVIF_STATE;
					tvItem.stateMask = OS.TVIS_SELECTED;
					tvItem.hItem = hItem;
					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
					boolean oldSelected = (tvItem.state & OS.TVIS_SELECTED) != 0;
					int hNewItem = 0;
					switch (wParam) {
						case OS.VK_UP:
							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PREVIOUSVISIBLE, hItem);
							break;
						case OS.VK_DOWN:
							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hItem);
							break;
						case OS.VK_HOME:
							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
							break;
						case OS.VK_PRIOR:
							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
							if (hNewItem == hItem) {
								OS.SendMessage (handle, OS.WM_VSCROLL, OS.SB_PAGEUP, 0);
								hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
							}
							break;
						case OS.VK_NEXT:			
							RECT rect = new RECT (), clientRect = new RECT ();
							OS.GetClientRect (handle, clientRect);
							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
							do {
								int hVisible = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hNewItem);
								if (hVisible == 0) break;
								rect.left = hVisible;
								OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect);
								if (rect.bottom > clientRect.bottom) break;
								if ((hNewItem = hVisible) == hItem) {
									OS.SendMessage (handle, OS.WM_VSCROLL, OS.SB_PAGEDOWN, 0);
								}
							} while (hNewItem != 0);
							break;
						case OS.VK_END:
							hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_LASTVISIBLE, 0);
							break;
					}
					if (hNewItem != 0) {
						OS.SendMessage (handle, OS.TVM_ENSUREVISIBLE, 0, hNewItem);
						tvItem.hItem = hNewItem;
						OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
						boolean newSelected = (tvItem.state & OS.TVIS_SELECTED) != 0;
						if (!newSelected && drawCount == 0) {
							OS.UpdateWindow (handle);
							OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
							*/
							/*
							* This code is intentionally commented.
							*/
//							OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
/* AW						}
						ignoreSelect = true;
						OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, hNewItem);
						ignoreSelect = false;
						if (oldSelected) {
							tvItem.state = OS.TVIS_SELECTED;
							tvItem.hItem = hItem;
							OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
						}
						if (!newSelected) {
							tvItem.state = 0;
							tvItem.hItem = hNewItem;
							OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
						}
						if (!newSelected && drawCount == 0) {
							RECT rect1 = new RECT (), rect2 = new RECT ();
							rect1.left = hItem;  rect2.left = hNewItem;
							OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect1);
							OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect2);
							*/
							/*
							* This code is intentionally commented.
							*/
//							OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
/* AW
							OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
							if (OS.IsWinCE) {
								OS.InvalidateRect (handle, rect1, false);
								OS.InvalidateRect (handle, rect2, false);
								OS.UpdateWindow (handle);
							} else {
								int flags = OS.RDW_UPDATENOW | OS.RDW_INVALIDATE;
								OS.RedrawWindow (handle, rect1, 0, flags);
								OS.RedrawWindow (handle, rect2, 0, flags);
							}
						}
						return LRESULT.ZERO;
					}
				}
			}
			int code = callWindowProc (OS.WM_KEYDOWN, wParam, lParam);
			hAnchor = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
			return new LRESULT (code);
		}
	}
	return result;
}
*/

/* AW
LRESULT WM_LBUTTONDOWN (int wParam, int lParam) {
	*/
	/*
	* Feature in Windows.  When a tree item is
	* reselected, Windows does not issue a WM_NOTIFY.
	* This is inconsistent with the list widget and
	* other widgets in Windows.  The fix is to detect
	* the case when an item is reselected and issue
	* the notification.  The first part of this work
	* around is to ensure that the user has selected
	* an item.
	*/
	/* AW
	TVHITTESTINFO lpht = new TVHITTESTINFO ();
	lpht.x = (short) (lParam & 0xFFFF);
	lpht.y = (short) (lParam >> 16);
	OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
	if (lpht.hItem == 0 || (lpht.flags & OS.TVHT_ONITEM) == 0) {
		sendMouseEvent (SWT.MouseDown, 1, OS.WM_LBUTTONDOWN, wParam, lParam);
		int code = callWindowProc (OS.WM_LBUTTONDOWN, wParam, lParam);
		if (OS.GetCapture () != handle) OS.SetCapture (handle);
		return new LRESULT (code);
	}
	*/
	
	/* Look for check/uncheck */
	/* AW
	if ((style & SWT.CHECK) != 0) {
		if ((lpht.flags & OS.TVHT_ONITEMSTATEICON) != 0) {
			TVITEM tvItem = new TVITEM ();
			tvItem.hItem = lpht.hItem;
			tvItem.mask = OS.TVIF_PARAM | OS.TVIF_STATE;	
			tvItem.stateMask = OS.TVIS_STATEIMAGEMASK;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			int state = tvItem.state >> 12;
			if ((state & 0x1) != 0) {
				state++;
			} else  {
				--state;
			}
			tvItem.state = state << 12;
			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
			Event event = new Event ();
			event.item = items [tvItem.lParam];
			event.detail = SWT.CHECK;
			postEvent (SWT.Selection, event);
			sendMouseEvent (SWT.MouseDown, 1, OS.WM_LBUTTONDOWN, wParam, lParam);
			if (OS.GetCapture () != handle) OS.SetCapture (handle);
			return LRESULT.ZERO;
		}
	}
	*/

	/* Get the selected state of the item under the mouse */
	/* AW
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_SELECTED;
	boolean hittestSelected = false;
	if ((style & SWT.MULTI) != 0) {
		tvItem.hItem = lpht.hItem;
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
		hittestSelected = (tvItem.state & OS.TVIS_SELECTED) != 0;
	}
	*/
	
	/* Get the selected state of the last selected item */
	/* AW
	int hOldItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
	if ((style & SWT.MULTI) != 0) {
		tvItem.hItem = hOldItem;
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
		*/
		/* Check for CONTROL or drag selection */
		/* AW
		if (hittestSelected || (wParam & OS.MK_CONTROL) != 0) {
			if (drawCount == 0) {
				OS.UpdateWindow (handle);
				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
				/*
				* This code is intentionally commented.
				*/
//				OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
/* AW
			}
		} else {
			deselectAll ();
		}
	}
*/
	/* Do the selection */
	/* AW
	sendMouseEvent (SWT.MouseDown, 1, OS.WM_LBUTTONDOWN, wParam, lParam);
	dragStarted = false;
	ignoreDeselect = ignoreSelect = true;
	int code = callWindowProc (OS.WM_LBUTTONDOWN, wParam, lParam);
	ignoreDeselect = ignoreSelect = false;
	if (dragStarted && OS.GetCapture () != handle) OS.SetCapture (handle);
	int hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
	*/
	
	/*
	* Feature in Windows.  When the old and new focused item
	* are the same, Windows does not check to make sure that
	* the item is actually selected, not just focused.  The
	* fix is to force the item to draw selected by setting
	* the state mask.  This is only necessary when the tree
	* is single select.
	*/
	/* AW
	if ((style & SWT.SINGLE) != 0) {
		if (hOldItem == hNewItem) {
			tvItem.mask = OS.TVIF_STATE;
			tvItem.state = OS.TVIS_SELECTED;
			tvItem.stateMask = OS.TVIS_SELECTED;
			tvItem.hItem = hNewItem;
			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
		}
	}
	*/
	
	/* Reselect the last item that was unselected */
	/* AW
	if ((style & SWT.MULTI) != 0) {
		
		/* Check for CONTROL and reselect the last item */
		/* AW
		if (hittestSelected || (wParam & OS.MK_CONTROL) != 0) {
			if (hOldItem == hNewItem && hOldItem == lpht.hItem) {
				if ((wParam & OS.MK_CONTROL) != 0) {
					tvItem.state ^= OS.TVIS_SELECTED;
					if (dragStarted) tvItem.state = OS.TVIS_SELECTED;
					OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
				}
			} else {
				if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
					tvItem.state = OS.TVIS_SELECTED;
					OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
				}
				if ((wParam & OS.MK_CONTROL) != 0 && !dragStarted) {
					if (hittestSelected) {
						tvItem.state = 0;
						tvItem.hItem = lpht.hItem;
						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
					}
				}
			}
			if (drawCount == 0) {
				RECT rect1 = new RECT (), rect2 = new RECT ();
				rect1.left = hOldItem;  rect2.left = hNewItem;
				OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect1);
				OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect2);
				/*
				* This code is intentionally commented.
				*/
//				OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
/* AW
				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
				if (OS.IsWinCE) {
					OS.InvalidateRect (handle, rect1, false);
					OS.InvalidateRect (handle, rect2, false);
					OS.UpdateWindow (handle);
				} else {
					int flags = OS.RDW_UPDATENOW | OS.RDW_INVALIDATE;
					OS.RedrawWindow (handle, rect1, 0, flags);
					OS.RedrawWindow (handle, rect2, 0, flags);
				} 
			}
		}

		/* Check for SHIFT or normal select and delect/reselect items */
/* AW
		if ((wParam & OS.MK_CONTROL) == 0) {
			if (!hittestSelected || !dragStarted) {
				tvItem.state = 0;
				int oldProc = OS.GetWindowLong (handle, OS.GWL_WNDPROC);
				OS.SetWindowLong (handle, OS.GWL_WNDPROC, TreeProc);	
				for (int i=0; i<items.length; i++) {
					TreeItem2 item = items [i];
					if (item != null && item.handle != hNewItem) {
						tvItem.hItem = item.handle;
						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
					}
				}
				tvItem.hItem = hNewItem;
				tvItem.state = OS.TVIS_SELECTED;
				OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
				OS.SetWindowLong (handle, OS.GWL_WNDPROC, oldProc);
				if ((wParam & OS.MK_SHIFT) != 0) {
					RECT rect1 = new RECT ();
					if (hAnchor == 0) hAnchor = hNewItem;
					rect1.left = hAnchor;
					if (OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect1) != 0) {
						RECT rect2 = rect2 = new RECT ();
						rect2.left = hNewItem;
						OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect2);
						int flags = rect1.top < rect2.top ? OS.TVGN_NEXTVISIBLE : OS.TVGN_PREVIOUSVISIBLE;			
						tvItem.state = OS.TVIS_SELECTED;
						int hItem = tvItem.hItem = hAnchor;
						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
						while (hItem != hNewItem) {
							tvItem.hItem = hItem;
							OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
							hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, flags, hItem);
						}
					}
				}
			}
		}
	}
	if ((wParam & OS.MK_SHIFT) == 0) hAnchor = hNewItem;
			
	/* Issue notification */
	/* AW
	tvItem.hItem = hNewItem;
	tvItem.mask = OS.TVIF_PARAM;
	OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
	Event event = new Event ();
	event.item = items [tvItem.lParam];
	postEvent (SWT.Selection, event);
	
	/*
	* Feature in Windows.  Inside WM_LBUTTONDOWN and WM_RBUTTONDOWN,
	* the widget starts a modal loop to determine if the user wants
	* to begin a drag/drop operation or marque select.  Unfortunately,
	* this modal loop eats the corresponding mouse up.  The fix is to
	* detect the cases when the modal loop has eaten the mouse up and
	* issue a fake mouse up.
	*/
	/* AW
	if (dragStarted) {
		postEvent (SWT.DragDetect);
	} else {
		sendMouseEvent (SWT.MouseUp, 1, OS.WM_LBUTTONUP, wParam, lParam);
	}
	dragStarted = false;
	return new LRESULT (code);
}
*/


/* AW
LRESULT wmNotifyChild (int wParam, int lParam) {
	NMHDR hdr = new NMHDR ();
	OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
	int code = hdr.code;
	switch (code) {
		case OS.NM_CUSTOMDRAW: {
			if (!customDraw) break;
			NMTVCUSTOMDRAW nmcd = new NMTVCUSTOMDRAW ();
			OS.MoveMemory (nmcd, lParam, NMTVCUSTOMDRAW.sizeof);		
			switch (nmcd.dwDrawStage) {
				case OS.CDDS_PREPAINT: return new LRESULT (OS.CDRF_NOTIFYITEMDRAW);
				case OS.CDDS_ITEMPREPAINT:
					TreeItem2 item = items [nmcd.lItemlParam];
					TVITEM tvItem = new TVITEM ();
					tvItem.mask = OS.TVIF_STATE;
					tvItem.hItem = item.handle;
					OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
					if ((tvItem.state & OS.TVIS_SELECTED) != 0) break;
					int clrText = item.foreground, clrTextBk = item.background;
					if (clrText == -1 && clrTextBk == -1) break;
					nmcd.clrText = clrText == -1 ? getForegroundPixel () : clrText;
					nmcd.clrTextBk = clrTextBk == -1 ? getBackgroundPixel () : clrTextBk;
					OS.MoveMemory (lParam, nmcd, NMTVCUSTOMDRAW.sizeof);
					return new LRESULT (OS.CDRF_NEWFONT);
			}
			break;
		}
		case OS.NM_DBLCLK:
			int pos = OS.GetMessagePos ();
			TVHITTESTINFO lpht = new TVHITTESTINFO ();
			POINT pt = new POINT ();
			pt.x = (short) (pos & 0xFFFF);
			pt.y = (short) (pos >> 16);
			OS.ScreenToClient (handle, pt);
			lpht.x = pt.x;  lpht.y = pt.y;
			OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
			if ((lpht.flags & OS.TVHT_ONITEM) == 0) break;
			// fall through
		case OS.NM_RETURN:
		case OS.TVN_SELCHANGEDA:
		case OS.TVN_SELCHANGEDW:
			if (!ignoreSelect) {
				TVITEM tvItem = null;
				if (code == OS.TVN_SELCHANGED) {
					tvItem = new TVITEM ();
					int offset = NMHDR.sizeof + 4 + TVITEM.sizeof;
					OS.MoveMemory (tvItem, lParam + offset, TVITEM.sizeof);
					hAnchor = tvItem.hItem;
				} else {
					int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
					if (hItem != 0) {
						tvItem = new TVITEM ();
						tvItem.hItem = hItem;
						tvItem.mask = OS.TVIF_PARAM;
						OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
					}
				}
				Event event = new Event ();
				if (tvItem != null) {
					event.item = items [tvItem.lParam];
				}
				if (code == OS.TVN_SELCHANGED) {
					postEvent (SWT.Selection, event);
				} else {
					postEvent (SWT.DefaultSelection, event);
				}
			}
			if (code == OS.NM_DBLCLK && hooks (SWT.DefaultSelection)) {
				return LRESULT.ONE;
			}
			break;
		case OS.TVN_SELCHANGINGA:
		case OS.TVN_SELCHANGINGW:
			if (!ignoreSelect && !ignoreDeselect) {
				hAnchor = 0;
				if ((style & SWT.MULTI) != 0) deselectAll ();
			}
			break;
		case OS.TVN_ITEMEXPANDINGA:
		case OS.TVN_ITEMEXPANDINGW:
			if (!ignoreExpand) {
				TVITEM tvItem = new TVITEM ();
				int offset = NMHDR.sizeof + 4 + TVITEM.sizeof;
				OS.MoveMemory (tvItem, lParam + offset, TVITEM.sizeof);
				int [] action = new int [1];
				OS.MoveMemory (action, lParam + NMHDR.sizeof, 4);
				Event event = new Event ();
				event.item = items [tvItem.lParam];
				/*
				* It is possible (but unlikely), that application
				* code could have disposed the widget in the expand
				* or collapse event.  If this happens, end the
				* processing of the Windows message by returning
				* zero as the result of the window proc.
				*/
				/* AW
				if (action [0] == OS.TVE_EXPAND) {
					sendEvent (SWT.Expand, event);
					if (isDisposed ()) return LRESULT.ZERO;
				}
				if (action [0] == OS.TVE_COLLAPSE) {
					sendEvent (SWT.Collapse, event);
					if (isDisposed ()) return LRESULT.ZERO;
				}
			}
			break;
		case OS.TVN_BEGINDRAGA:
		case OS.TVN_BEGINDRAGW:
		case OS.TVN_BEGINRDRAGA:
		case OS.TVN_BEGINRDRAGW:
			TVITEM tvItem = new TVITEM ();
			int offset = NMHDR.sizeof + 4 + TVITEM.sizeof;
			OS.MoveMemory (tvItem, lParam + offset, TVITEM.sizeof);
			if (tvItem.hItem != 0 && (tvItem.state & OS.TVIS_SELECTED) == 0) {
				ignoreSelect = ignoreDeselect = true;
				OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, tvItem.hItem);
				ignoreSelect = ignoreDeselect = false;
			}
			dragStarted = true;
			break;
	}
	return super.wmNotifyChild (wParam, lParam);
}
*/

/////////////////////////////////////////////////////////////////////////////////////////////////
// Mac stuff
/////////////////////////////////////////////////////////////////////////////////////////////////

	int sendKeyEvent(int type, MacEvent mEvent, Event event) {
		//processEvent (type, new MacEvent(eRefHandle));
		return OS.eventNotHandledErr;
	}	

	int handleItemCallback(int item, int property, int itemData, int setValue) {
		
		TreeItem2 ti= find(item);
		if (ti == null) {
			System.out.println("handleItemCallback: can't find row with id: " + item);
			return OS.kNoErr;
		}
			
		switch (property) {

		case OS.kDataBrowserItemIsActiveProperty:
			// OS.SetDataBrowserItemDataBooleanValue(itemData, true);	// defaults to true
			break;
			
		case OS.kDataBrowserItemIsSelectableProperty:
			// OS.SetDataBrowserItemDataBooleanValue(itemData, true);	// defaults to true
			break;
			
		case OS.kDataBrowserItemIsEditableProperty:
			// OS.SetDataBrowserItemDataBooleanValue(itemData, false);	// defaults to false
			break;
			
		case OS.kDataBrowserItemIsContainerProperty:
			OS.SetDataBrowserItemDataBooleanValue(itemData, ti.isContainerProperty());
			break;

		case OS.kDataBrowserContainerIsOpenableProperty:
			// OS.SetDataBrowserItemDataBooleanValue(itemData, true);	// defaults to true
			break;
			
		case OS.kDataBrowserContainerIsClosableProperty:
			// OS.SetDataBrowserItemDataBooleanValue(itemData, true);	// defaults to true
			break;
			
		case OS.kDataBrowserContainerIsSortableProperty:
			// OS.SetDataBrowserItemDataBooleanValue(itemData, true);	// defaults to true
			break;
			
		case OS.kDataBrowserItemParentContainerProperty:	/* DataBrowserItemID (the parent of the specified item, used by ColumnView) */
			TreeItem2 parent= ti.getParentItem();
			if (parent != null)
				OS.SetDataBrowserItemDataItemID(itemData, parent.handle);
			break;
			
		case CHECK_COL_ID:	// our column
			//OS.SetDataBrowserItemDataButtonValue(itemData, OS.kThemeButtonOn);
			OS.SetDataBrowserItemDataButtonValue(itemData, (short)(item % 3));
			break;
			
		case COL_ID:	// our column
			ti.updateContent(itemData);
			break;
		
		default:
			System.out.println("handleDataCallback: wrong property: " + property);
			break;
		}
		
		return OS.kNoErr;
	}

	int handleCompareCallback(int item1ID, int item2ID, int item) {
//		if (getIndex(item1ID) < getIndex(item2ID))
//			return 1;
		return 0;
	}
	
	int handleItemNotificationCallback(int item, int message) {

		//System.out.println("handleItemNotificationCallback: message: " + message + " item: " + item);
		TreeItem2 ti= find(item);
		if (ti == null) {
			System.out.println("handleItemNotificationCallback: can't find row with id: " + item);
			return OS.kNoErr;
		}
		
		Event event= null;
		
		switch (message) {
			
		case OS.kDataBrowserContainerOpened:	/* Container is open */
			event= new Event ();
			event.item= ti;
			/*
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the expand
			* or collapse event.  If this happens, end the
			* processing of the Windows message by returning
			* zero as the result of the window proc.
			*/
			sendEvent (SWT.Expand, event);
			//if (isDisposed ()) return LRESULT.ZERO;
			ti.open();
			break;
					
		case OS.kDataBrowserContainerClosing:	/* Container is about to be closed */
			int[] ids= MacUtil.getSelectionIDs(handle, ti.handle, true);
			if (ids.length > 0 && ids[0] != 0)
				OS.SetDataBrowserSelectedItems(handle, 1, new int[] { ti.handle }, OS.kDataBrowserItemsAssign);
			break;
			
		case OS.kDataBrowserContainerClosed:	/* Container is closed */		
			ti.close();
			event= new Event ();
			event.item= ti;
			/*
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the expand
			* or collapse event.  If this happens, end the
			* processing of the Windows message by returning
			* zero as the result of the window proc.
			*/
			sendEvent (SWT.Collapse, event);
			//if (isDisposed ()) return LRESULT.ZERO;
			break;
		}
		
		return OS.kNoErr;
	}
		
	TreeItem2 find(int id) {
		int index= 0;
		if (id >= FIRST_ROW_ID) 	
			index= id - FIRST_ROW_ID;
		if (index < 0 || index >= items.length)
			return null;
		TreeItem2 ti= items[index];
		if (ti == null || ti.handle != index + FIRST_ROW_ID)
			return null;
		return ti;
	}
}
