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


import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

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
	int hAnchor;
	TreeItem [] items;
	ImageList imageList;
	boolean dragStarted, gestureCompleted;
	boolean ignoreSelect, ignoreExpand, ignoreDeselect;
	boolean customDraw;
	static final int TreeProc;
	static final TCHAR TreeClass = new TCHAR (0, OS.WC_TREEVIEW, true);
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, TreeClass, lpWndClass);
		TreeProc = lpWndClass.lpfnWndProc;
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

int callWindowProc (int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	return OS.CallWindowProc (TreeProc, handle, msg, wParam, lParam);
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
	RECT rect = new RECT ();
	int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	while (hItem != 0) {
		rect.left = hItem;
		if (OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect) != 0) {
			width = Math.max (width, rect.right);
			height += rect.bottom - rect.top;
		}
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
	}
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
	return new Point (width, height);
}

void createHandle () {
	super.createHandle ();
	state &= ~CANVAS;
	
	/* Set the checkbox image list */
	if ((style & SWT.CHECK) != 0) setCheckboxImageList ();
	
	/*
	* Feature in Windows.  When the control is created,
	* it does not use the default system font.  A new HFONT
	* is created and destroyed when the control is destroyed.
	* This means that a program that queries the font from
	* this control, uses the font in another control and then
	* destroys this control will have the font unexpectedly
	* destroyed in the other control.  The fix is to assign
	* the font ourselves each time the control is created.
	* The control will not destroy a font that it did not
	* create.
	*/
	int hFont = OS.GetStockObject (OS.SYSTEM_FONT);
	OS.SendMessage (handle, OS.WM_SETFONT, hFont, 0);
}

void createItem (TreeItem item, int hParent, int hInsertAfter) {
	item.foreground = item.background = -1;
	int id = 0;
	while (id < items.length && items [id] != null) id++;
	if (id == items.length) {
		TreeItem [] newItems = new TreeItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	TVINSERTSTRUCT tvInsert = new TVINSERTSTRUCT ();
	tvInsert.hParent = hParent;
	tvInsert.hInsertAfter = hInsertAfter;
	tvInsert.lParam = id;
	tvInsert.iImage = OS.I_IMAGENONE;
	tvInsert.iSelectedImage = tvInsert.iImage;
	tvInsert.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM | OS.TVIF_IMAGE | OS.TVIF_SELECTEDIMAGE;
	
	/* Set the initial unchecked state */
	if ((style & SWT.CHECK) != 0) {
		tvInsert.mask = tvInsert.mask | OS.TVIF_STATE;
		tvInsert.state = 1 << 12;
		tvInsert.stateMask = OS.TVIS_STATEIMAGEMASK;
	}

	/* Insert the item */
	int hItem = OS.SendMessage (handle, OS.TVM_INSERTITEM, 0, tvInsert);
	if (hItem == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	item.handle = hItem;
	items [id] = item;
	
	/*
	* This code is intentionally commented.
	*/
//	if (hParent != 0) {
//		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
//		bits |= OS.TVS_LINESATROOT;
//		OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
//	}

	/*
	* Bug in Windows.  When a child item is added to a parent item
	* that has no children outside of WM_NOTIFY with control code
	* TVN_ITEMEXPANDED, the tree widget does not redraw the +/-
	* indicator.  The fix is to detect the case when the first
	* child is added to a visible parent item and redraw the parent.
	*/
	if (!OS.IsWindowVisible (handle) || drawCount > 0) return;
	int hChild = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hParent);
	if (hChild != 0 && OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hChild) == 0) {
		RECT rect = new RECT ();
		rect.left = hParent;
		if (OS.SendMessage (handle, OS.TVM_GETITEMRECT, 0, rect) != 0) {
			OS.InvalidateRect (handle, rect, true);
		}
	}
}

void createWidget () {
	super.createWidget ();
	items = new TreeItem [4];
}

int defaultBackground () {
	return OS.GetSysColor (OS.COLOR_WINDOW);
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
		TreeItem item = items [i];
		if (item != null) {
			tvItem.hItem = item.handle;
			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
		}
	}
	OS.SetWindowLong (handle, OS.GWL_WNDPROC, oldProc);
}

void destroyItem (TreeItem item) {
	/*
	* Feature in Windows.  When an item is removed that is not
	* visible in the tree because it belongs to a collapsed branch,
	* Windows redraws the tree causing a flash for each item that
	* is removed.  The fix is to detect whether the item is visible,
	* force the widget to be fully painted, turn off redraw, remove
	* the item and validate the damage caused by the removing of
	* the item.
	*/
	int hItem = item.handle, hParent = 0;
	boolean fixRedraw = false;
	if (drawCount == 0 && OS.IsWindowVisible (handle)) {
		RECT rect = new RECT ();
		rect.left = hItem;
		fixRedraw = OS.SendMessage (handle, OS.TVM_GETITEMRECT, 0, rect) == 0;
	}
	if (fixRedraw) {
		hParent = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PARENT, hItem);
		OS.UpdateWindow (handle);
		OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
	}
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
	releaseItems (item.getItems (), tvItem);
	releaseItem (item, tvItem);
	OS.SendMessage (handle, OS.TVM_DELETEITEM, 0, hItem);
	if (fixRedraw) {
		OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
		OS.ValidateRect (handle, null);
		/*
		* If the item that was deleted was the last child of a tree item that
		* is visible, redraw the parent item to force the +/- to be updated.
		*/
		if (OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hParent) == 0) {
			RECT rect = new RECT ();
			rect.left = hParent;
			if (OS.SendMessage (handle, OS.TVM_GETITEMRECT, 0, rect) != 0) {
				OS.InvalidateRect (handle, rect, true);
			}
		}
	}
	int count = OS.SendMessage (handle, OS.TVM_GETCOUNT, 0, 0);
	if (count == 0) {
		if (imageList != null) {
			OS.SendMessage (handle, OS.TVM_SETIMAGELIST, 0, 0);
			display.releaseImageList (imageList);
		}
		imageList = null;
		customDraw = false;
		items = new TreeItem [4];	
	}
}

int getBackgroundPixel () {
	if (OS.IsWinCE) return OS.GetSysColor (OS.COLOR_WINDOW);
	int pixel = OS.SendMessage (handle, OS.TVM_GETBKCOLOR, 0, 0);
	if (pixel == -1) return OS.GetSysColor (OS.COLOR_WINDOW);
	return pixel;
}

int getForegroundPixel () {
	if (OS.IsWinCE) return OS.GetSysColor (OS.COLOR_WINDOWTEXT);
	int pixel = OS.SendMessage (handle, OS.TVM_GETTEXTCOLOR, 0, 0);
	if (pixel == -1) return OS.GetSysColor (OS.COLOR_WINDOWTEXT);
	return pixel;
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
	TVHITTESTINFO lpht = new TVHITTESTINFO ();
	lpht.x = point.x;
	lpht.y = point.y;
	OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
	if (lpht.hItem != 0 && (lpht.flags & OS.TVHT_ONITEM) != 0) {
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
		tvItem.hItem = lpht.hItem;
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
		return items [tvItem.lParam];	
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
	int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	if (hItem == 0) return 0;
	return getItemCount (hItem);
}

int getItemCount (int hItem) {
	int count = 0;
	while (hItem != 0) {
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
		count++;
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
	return OS.SendMessage (handle, OS.TVM_GETITEMHEIGHT, 0, 0);
}

/**
 * Returns the items contained in the receiver
 * that are direct item children of the receiver.  These
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
	int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	if (hItem == 0) return new TreeItem [0];
	return getItems (hItem);
}

TreeItem [] getItems (int hTreeItem) {
	int count = 0, hItem = hTreeItem;
	while (hItem != 0) {
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
		count++;
	}
	int index = 0;
	TreeItem [] result = new TreeItem [count];
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
	tvItem.hItem = hTreeItem;
	/*
	* Feature in Windows.  In some cases an expand or collapse message
	* can occurs from within TVM_DELETEITEM.  When this happens, the item
	* being destroyed has been removed from the list of items but has not
	* been deleted from the tree.  The fix is to check for null items and
	* remove them from the list.
	*/
	while (tvItem.hItem != 0) {
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
		TreeItem item = items [tvItem.lParam];
		if (item != null) result [index++] = item;
		tvItem.hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, tvItem.hItem);
	}
	if (index != count) {
		TreeItem [] newResult = new TreeItem [index];
		System.arraycopy (result, 0, newResult, 0, index);
		result = newResult;
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
	if ((style & SWT.SINGLE) != 0) {
		int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		if (hItem == 0) return new TreeItem [0];
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_PARAM | OS.TVIF_STATE;
		tvItem.hItem = hItem;
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
		if ((tvItem.state & OS.TVIS_SELECTED) == 0) return new TreeItem [0];
		return new TreeItem [] {items [tvItem.lParam]};
	}
	int count = 0;
	TreeItem [] guess = new TreeItem [8];
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_PARAM | OS.TVIF_STATE;
	int oldProc = OS.GetWindowLong (handle, OS.GWL_WNDPROC);
	OS.SetWindowLong (handle, OS.GWL_WNDPROC, TreeProc);
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null) {
			tvItem.hItem = item.handle;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
				if (count < guess.length) guess [count] = item;
				count++;
			}
		}
	}
	OS.SetWindowLong (handle, OS.GWL_WNDPROC, oldProc);
	if (count == 0) return new TreeItem [0];
	if (count == guess.length) return guess;
	TreeItem [] result = new TreeItem [count];
	if (count < guess.length) {
		System.arraycopy (guess, 0, result, 0, count);
		return result;
	}
	OS.SetWindowLong (handle, OS.GWL_WNDPROC, TreeProc);
	int index = 0;
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null) {
			tvItem.hItem = item.handle;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
				result [index++] = item;
			}
		}
	}
	OS.SetWindowLong (handle, OS.GWL_WNDPROC, oldProc);
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
	if ((style & SWT.SINGLE) != 0) {
		int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		if (hItem == 0) return 0;
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_STATE;
		tvItem.hItem = hItem;
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
		if ((tvItem.state & OS.TVIS_SELECTED) == 0) return 0;
		return 1;
	}
	int count = 0;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_STATE;
	int oldProc = OS.GetWindowLong (handle, OS.GWL_WNDPROC);
	OS.SetWindowLong (handle, OS.GWL_WNDPROC, TreeProc);
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null) {
			tvItem.hItem = item.handle;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			if ((tvItem.state & OS.TVIS_SELECTED) != 0) count++;
		}
	}
	OS.SetWindowLong (handle, OS.GWL_WNDPROC, oldProc);
	return count;
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
	int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_FIRSTVISIBLE, 0);
	if (hItem == 0) return null;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_PARAM;
	tvItem.hItem = hItem;
	if (OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem) == 0) return null;
	return items [tvItem.lParam];
}

int imageIndex (Image image) {
	if (image == null) return OS.I_IMAGENONE;
	if (imageList == null) {
		int hOldList = OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_NORMAL, 0);
		if (hOldList != 0) OS.ImageList_Destroy (hOldList);
		Rectangle bounds = image.getBounds ();
		imageList = display.getImageList (new Point (bounds.width, bounds.height));
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

boolean releaseItem (TreeItem item, TVITEM tvItem) {
	int hItem = item.handle;
	if (hItem == hAnchor) hAnchor = 0;
	if (item.isDisposed ()) return false;
	tvItem.hItem = hItem;
	OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
	items [tvItem.lParam] = null;
	return true;
}

void releaseItems (TreeItem [] nodes, TVITEM tvItem) {
	for (int i=0; i<nodes.length; i++) {
		TreeItem item = nodes [i];
		TreeItem [] sons = item.getItems ();
		if (sons.length != 0) {
			releaseItems (sons, tvItem);
		}
		if (releaseItem (item, tvItem)) {
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
	customDraw = false;
	items = null;
	if (imageList != null) {
		OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_NORMAL, 0);
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
	ignoreDeselect = ignoreSelect = true;
	boolean redraw = drawCount == 0 && OS.IsWindowVisible (handle);
	if (redraw) {
		OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
		/*
		* This code is intentionally commented.
		*/
//		OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
	}
	int result = OS.SendMessage (handle, OS.TVM_DELETEITEM, 0, OS.TVI_ROOT);
	if (redraw) {
		OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);	
		/*
		* This code is intentionally commented.
		*/
//		OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
		OS.InvalidateRect (handle, null, true);
	}
	ignoreDeselect = ignoreSelect = false;
	if (result == 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null && !item.isDisposed ()) {
			item.releaseResources ();
		}
	}
	if (imageList != null) {
		OS.SendMessage (handle, OS.TVM_SETIMAGELIST, 0, 0);
		display.releaseImageList (imageList);
	}
	imageList = null;
	customDraw = false;
	items = new TreeItem [4];
	hAnchor = 0;
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
	int hItem = 0;
	if (item != null) {
		if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		hItem = item.handle;
	}
	OS.SendMessage (handle, OS.TVM_SETINSERTMARK, (before) ? 0 : 1, hItem);
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
	int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
	if (hItem == 0) {
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
		if (hItem != 0) {
			ignoreSelect = true;
			OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, hItem);
			ignoreSelect = false;
		}
	}
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_STATE;
	tvItem.state = OS.TVIS_SELECTED;
	tvItem.stateMask = OS.TVIS_SELECTED;
	int oldProc = OS.GetWindowLong (handle, OS.GWL_WNDPROC);
	OS.SetWindowLong (handle, OS.GWL_WNDPROC, TreeProc);	
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null) {
			tvItem.hItem = item.handle;
			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
		}
	}
	OS.SetWindowLong (handle, OS.GWL_WNDPROC, oldProc);
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
	int oldPixel = OS.SendMessage (handle, OS.TVM_GETBKCOLOR, 0, 0);
	if (oldPixel != -1) OS.SendMessage (handle, OS.TVM_SETBKCOLOR, 0, -1);
	OS.SendMessage (handle, OS.TVM_SETBKCOLOR, 0, pixel);
	if ((style & SWT.CHECK) != 0) setCheckboxImageList ();
}

void setBounds (int x, int y, int width, int height, int flags) {
	/*
	* Ensure that the selection is visible when the tree is resized
	* from a zero size to a size that can show the selection.
	*/
	boolean fixSelection = false;
	if ((flags & OS.SWP_NOSIZE) == 0 && (width != 0 || height != 0)) {
		if (OS.SendMessage (handle, OS.TVM_GETVISIBLECOUNT, 0, 0) == 0) {
			fixSelection = true;
		}
	}
	super.setBounds (x, y, width, height, flags);
	if (fixSelection) {
		int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		if (hItem != 0) showItem (hItem);
	}
}

void setCursor () {
	/*
	* Bug in Windows.  Under certain circumstances, when WM_SETCURSOR
	* is sent using SendMessage(), Windows GP's in the window proc for
	* the tree.  The fix is to avoid calling the tree window proc and
	* set the cursor for the tree outside of WM_SETCURSOR.
	* 
	* NOTE:  This code assumes that the default cursor for the tree
	* is IDC_ARROW.
	*/
	Cursor cursor = findCursor ();
	int hCursor = cursor == null ? OS.LoadCursor (0, OS.IDC_ARROW) : cursor.handle;
	OS.SetCursor (hCursor);
}

void setCheckboxImageList () {
	if ((style & SWT.CHECK) == 0) return;
	int count = 5;
	int height = OS.SendMessage (handle, OS.TVM_GETITEMHEIGHT, 0, 0), width = height;
	int hImageList = OS.ImageList_Create (width, height, OS.ILC_COLOR, count, count);
	int hDC = OS.GetDC (handle);
	if ((OS.WIN32_MAJOR << 16 | OS.WIN32_MINOR) >= (4 << 16 | 10)) {
		OS.SetLayout (hDC, 0);
	}
	int memDC = OS.CreateCompatibleDC (hDC);
	int hBitmap = OS.CreateCompatibleBitmap (hDC, width * count, height);
	int hOldBitmap = OS.SelectObject (memDC, hBitmap);
	RECT rect = new RECT ();
	OS.SetRect (rect, 0, 0, width * count, height);
	int hBrush = OS.CreateSolidBrush (getBackgroundPixel ());
	OS.FillRect (memDC, rect, hBrush);
	OS.DeleteObject (hBrush);
	int oldFont = OS.SelectObject (hDC, defaultFont ());
	TEXTMETRIC tm = OS.IsUnicode ? (TEXTMETRIC) new TEXTMETRICW () : new TEXTMETRICA ();
	OS.GetTextMetrics (hDC, tm);
	OS.SelectObject (hDC, oldFont);
	int itemWidth = Math.min (tm.tmHeight, width);
	int itemHeight = Math.min (tm.tmHeight, height);
	int left = (width - itemWidth) / 2, top = (height - itemHeight) / 2 + 1;
	OS.SetRect (rect, left + width, top, left + width + itemWidth, top + itemHeight);
	OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_FLAT);
	rect.left += width;  rect.right += width;
	OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_CHECKED | OS.DFCS_FLAT);
	rect.left += width;  rect.right += width;
	OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_INACTIVE | OS.DFCS_FLAT);
	rect.left += width;  rect.right += width;
	OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_CHECKED | OS.DFCS_INACTIVE | OS.DFCS_FLAT);
	OS.SelectObject (memDC, hOldBitmap);
	OS.DeleteDC (memDC);
	OS.ReleaseDC (handle, hDC);
	OS.ImageList_AddMasked (hImageList, hBitmap, 0);
	OS.DeleteObject (hBitmap);
	int hOldList = OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_STATE, 0);
	OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_STATE, hImageList);
	if (hOldList != 0) OS.ImageList_Destroy (hOldList);
}

void setForegroundPixel (int pixel) {
	if (foreground == pixel) return;
	foreground = pixel;
	OS.SendMessage (handle, OS.TVM_SETTEXTCOLOR, 0, pixel);
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
	int hItem = 0;
	if (redraw && drawCount == 1) {
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
		
	/* Select/deselect the first item */
	int hOldItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
	if (items.length == 0) {
		if (hOldItem != 0) {
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_STATE;
			tvItem.stateMask = OS.TVIS_SELECTED;
			tvItem.hItem = hOldItem;
			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
		}
	} else {
		int hNewItem = 0;
		TreeItem item = items [0];
		if (item != null) {
			if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
			hAnchor = hNewItem = item.handle;
		}
		ignoreSelect = true;
		OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, hNewItem);
		ignoreSelect = false;
		/*
		* Feature in Windows.  When the old and new focused item
		* are the same, Windows does not check to make sure that
		* the item is actually selected, not just focused.  The
		* fix is to force the item to draw selected by setting
		* the state mask.
		*/
		if (hOldItem == hNewItem) {
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_STATE;
			tvItem.state = OS.TVIS_SELECTED;
			tvItem.stateMask = OS.TVIS_SELECTED;
			tvItem.hItem = hNewItem;
			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
		}
		showItem (hNewItem);
	}
	if ((style & SWT.SINGLE) != 0) return;

	/* Select/deselect the rest of the items */
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_SELECTED;
	int oldProc = OS.GetWindowLong (handle, OS.GWL_WNDPROC);
	OS.SetWindowLong (handle, OS.GWL_WNDPROC, TreeProc);
	for (int i=0; i<this.items.length; i++) {
		TreeItem item = this.items [i];
		if (item != null) {
			int index = 0;
			while (index < items.length) {
				if (items [index] == item) break;
				index++;
			}
			tvItem.hItem = item.handle;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
				if (index == items.length) {
					tvItem.state = 0;
					OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
				}
			} else {
				if (index != items.length) {
					tvItem.state = OS.TVIS_SELECTED;
					OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
				}
			}
		}
	}
	OS.SetWindowLong (handle, OS.GWL_WNDPROC, oldProc);
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
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_FIRSTVISIBLE, item.handle);
}

void showItem (int hItem) {
	/*
	* Bug in Windows.  When TVM_ENSUREVISIBLE is used to ensure
	* that an item is visible and the client area of the tree is
	* smaller that the size of one item, TVM_ENSUREVISIBLE makes
	* the next item in the tree visible by making it the top item
	* instead of making the desired item visible.  The fix is to
	* detect the case when the client area is too small and make
	* the desired visible item be the top item in the tree.
	*/
	if (OS.SendMessage (handle, OS.TVM_GETVISIBLECOUNT, 0, 0) == 0) {
		OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_FIRSTVISIBLE, hItem);
		OS.SendMessage (handle, OS.WM_HSCROLL, OS.SB_TOP ,0);
	} else {
		boolean scroll = true;
		RECT itemRect = new RECT ();
		itemRect.left = hItem;
		if (OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, itemRect) != 0) {
			forceResize ();
			RECT rect = new RECT ();
			OS.GetClientRect (handle, rect);
			POINT pt = new POINT ();
			pt.x = itemRect.left;
			pt.y = itemRect.top;
			if (OS.PtInRect (rect, pt)) {
				pt.y = itemRect.bottom;
				if (OS.PtInRect (rect, pt)) scroll = false;
			}
		}
		if (scroll) OS.SendMessage (handle, OS.TVM_ENSUREVISIBLE, 0, hItem);
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
	if (item.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
	showItem (item.handle);
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
	int hItem = 0;
	if ((style & SWT.SINGLE) != 0) {	
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		if (hItem == 0) return;
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_STATE;
		tvItem.hItem = hItem;
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
		if ((tvItem.state & OS.TVIS_SELECTED) == 0) return;
	} else {
		TVITEM tvItem = new TVITEM ();
		tvItem.mask = OS.TVIF_STATE;
		int oldProc = OS.GetWindowLong (handle, OS.GWL_WNDPROC);
		OS.SetWindowLong (handle, OS.GWL_WNDPROC, TreeProc);
		int index = 0;
		while (index <items.length) {
			TreeItem item = items [index];
			if (item != null) {
				tvItem.hItem = item.handle;
				OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
				if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
					hItem = tvItem.hItem;
					break;
				}
			}
			index++;
		}
		OS.SetWindowLong (handle, OS.GWL_WNDPROC, oldProc);
	}
	if (hItem != 0) showItem (hItem);
}

String toolTipText (NMTTDISPINFO hdr) {
	int hwndToolTip = OS.SendMessage (handle, OS.TVM_GETTOOLTIPS, 0, 0);
	if (hwndToolTip == hdr.hwndFrom && toolTipText != null) return ""; //$NON-NLS-1$
	return super.toolTipText (hdr);
}

int widgetStyle () {
	int bits = super.widgetStyle () | OS.TVS_SHOWSELALWAYS;
	bits |= OS.TVS_LINESATROOT | OS.TVS_HASLINES | OS.TVS_HASBUTTONS;
	/*
	* This code is intentionally commented.  In future,
	* FULL_SELECTION may be implemented for trees.
	*/
//	if ((style & SWT.FULL_SELECTION) != 0) {
//		bits |= OS.TVS_FULLROWSELECT;
//	} else {
//		bits |= OS.TVS_HASLINES | OS.TVS_HASBUTTONS;
//	}
//	bits |= OS.TVS_NOTOOLTIPS;
	return bits;
}

TCHAR windowClass () {
	return TreeClass;
}

int windowProc () {
	return TreeProc;
}

LRESULT WM_CHAR (int wParam, int lParam) {
	LRESULT result = super.WM_CHAR (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  The tree control beeps
	* in WM_CHAR when the search for the item that
	* matches the key stroke fails.  This is the
	* standard tree behavior but is unexpected when
	* the key that was typed was ESC, CR or SPACE.
	* The fix is to avoid calling the tree window
	* proc in these cases.
	*/
	switch (wParam) {
		case OS.VK_RETURN:
			/*
			* Feature in Windows.  Windows sends NM_RETURN from WM_KEYDOWN
			* instead of using WM_CHAR.  This means that application code
			* that expects to consume the key press and therefore avoid the
			* SWT.DefaultSelection event from WM_CHAR will fail.  The fix
			* is to implement SWT.DefaultSelection in WM_CHAR instead of
			* using NM_RETURN.
			*/
			Event event = new Event ();
			int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
			if (hItem != 0) {
				TVITEM tvItem = new TVITEM ();
				tvItem.hItem = hItem;
				tvItem.mask = OS.TVIF_PARAM;
				OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
				event.item = items [tvItem.lParam];
			}
			postEvent (SWT.DefaultSelection, event);
			//FALL THROUGH
		case OS.VK_ESCAPE:
		case OS.VK_SPACE: return LRESULT.ZERO;
	}
	return result;
}

LRESULT WM_GETOBJECT (int wParam, int lParam) {
	/*
	* Ensure that there is an accessible object created for this
	* control because support for checked item accessibility is
	* temporarily implemented in the accessibility package.
	*/
	if ((style & SWT.CHECK) != 0) {
		if (accessible == null) accessible = new_Accessible (this);
	}
	return super.WM_GETOBJECT (wParam, lParam);
}

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
							/*
							* This code is intentionally commented.
							*/
//							OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
						}
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
							/*
							* This code is intentionally commented.
							*/
//							OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
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

LRESULT WM_KILLFOCUS (int wParam, int lParam) {
	LRESULT result = super.WM_KILLFOCUS (wParam, lParam);
	if ((style & SWT.SINGLE) != 0) return result;
	/*
	* Feature in Windows.  When multiple item have
	* the TVIS_SELECTED state, Windows redraws only
	* the focused item in the color used to show the
	* selection when the tree loses or gains focus.
	* The fix is to force Windows to redraw all the
	* visible items when focus is gained or lost.
	*/
	OS.InvalidateRect (handle, null, false);
	return result;
}

LRESULT WM_LBUTTONDOWN (int wParam, int lParam) {
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
	
	/* Look for check/uncheck */
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

	/* Get the selected state of the item under the mouse */
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_SELECTED;
	boolean hittestSelected = false;
	if ((style & SWT.MULTI) != 0) {
		tvItem.hItem = lpht.hItem;
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
		hittestSelected = (tvItem.state & OS.TVIS_SELECTED) != 0;
	}
	
	/* Get the selected state of the last selected item */
	int hOldItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
	if ((style & SWT.MULTI) != 0) {
		tvItem.hItem = hOldItem;
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);

		/* Check for CONTROL or drag selection */
		if (hittestSelected || (wParam & OS.MK_CONTROL) != 0) {
			if (drawCount == 0) {
				OS.UpdateWindow (handle);
				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
				/*
				* This code is intentionally commented.
				*/
//				OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
			}
		} else {
			deselectAll ();
		}
	}

	/* Do the selection */
	sendMouseEvent (SWT.MouseDown, 1, OS.WM_LBUTTONDOWN, wParam, lParam);
	dragStarted = gestureCompleted = false;
	ignoreDeselect = ignoreSelect = true;
	int code = callWindowProc (OS.WM_LBUTTONDOWN, wParam, lParam);
	ignoreDeselect = ignoreSelect = false;
	if (dragStarted && OS.GetCapture () != handle) OS.SetCapture (handle);
	int hNewItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);

	/*
	* Feature in Windows.  When the old and new focused item
	* are the same, Windows does not check to make sure that
	* the item is actually selected, not just focused.  The
	* fix is to force the item to draw selected by setting
	* the state mask.  This is only necessary when the tree
	* is single select.
	*/
	if ((style & SWT.SINGLE) != 0) {
		if (hOldItem == hNewItem) {
			tvItem.mask = OS.TVIF_STATE;
			tvItem.state = OS.TVIS_SELECTED;
			tvItem.stateMask = OS.TVIS_SELECTED;
			tvItem.hItem = hNewItem;
			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
		}
	}
	
	/* Reselect the last item that was unselected */
	if ((style & SWT.MULTI) != 0) {
		
		/* Check for CONTROL and reselect the last item */
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
		if ((wParam & OS.MK_CONTROL) == 0) {
			if (!hittestSelected || !dragStarted) {
				tvItem.state = 0;
				int oldProc = OS.GetWindowLong (handle, OS.GWL_WNDPROC);
				OS.SetWindowLong (handle, OS.GWL_WNDPROC, TreeProc);	
				for (int i=0; i<items.length; i++) {
					TreeItem item = items [i];
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
	if (!gestureCompleted) {
		tvItem.hItem = hNewItem;
		tvItem.mask = OS.TVIF_PARAM;
		OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
		Event event = new Event ();
		event.item = items [tvItem.lParam];
		postEvent (SWT.Selection, event);
	}
	gestureCompleted = false;
	
	/*
	* Feature in Windows.  Inside WM_LBUTTONDOWN and WM_RBUTTONDOWN,
	* the widget starts a modal loop to determine if the user wants
	* to begin a drag/drop operation or marque select.  Unfortunately,
	* this modal loop eats the corresponding mouse up.  The fix is to
	* detect the cases when the modal loop has eaten the mouse up and
	* issue a fake mouse up.
	*/
	if (dragStarted) {
		Event event = new Event ();
		event.x = (short) (lParam & 0xFFFF);
		event.y = (short) (lParam >> 16);
		postEvent (SWT.DragDetect, event);
	} else {
		sendMouseEvent (SWT.MouseUp, 1, OS.WM_LBUTTONUP, wParam, lParam);
	}
	dragStarted = false;
	return new LRESULT (code);
}

LRESULT WM_RBUTTONDOWN (int wParam, int lParam) {
	/*
	* Feature in Windows.  The receiver uses WM_RBUTTONDOWN
	* to initiate a drag/drop operation depending on how the
	* user moves the mouse.  If the user clicks the right button,
	* without moving the mouse, the tree consumes the corresponding
	* WM_RBUTTONUP.  The fix is to avoid calling the window proc for
	* the tree.
	*/
	sendMouseEvent (SWT.MouseDown, 3, OS.WM_RBUTTONDOWN, wParam, lParam);
	/*
	* This code is intentionally commented.
	*/
//	if (OS.GetCapture () != handle) OS.SetCapture (handle);
	setFocus ();
	
	/*
	* Feature in Windows.  When the user selects a tree item
	* with the right mouse button, the item remains selected
	* only as long as the user does not release or move the
	* mouse.  As soon as this happens, the selection snaps
	* back to the previous selection.  This behavior can be
	* observed in the Explorer but is not instantly apparent
	* because the Explorer explicity sets the selection when
	* the user chooses a menu item.  If the user cancels the
	* menu, the selection snaps back.  The fix is to avoid
	* calling the window proc and do the selection ourselves.
	* This behavior is consistent with the table.
	*/
	TVHITTESTINFO lpht = new TVHITTESTINFO ();
	lpht.x = (short) (lParam & 0xFFFF);
	lpht.y = (short) (lParam >> 16);
	OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
	if (lpht.hItem != 0 && (lpht.flags & (OS.TVHT_ONITEMICON | OS.TVHT_ONITEMLABEL)) != 0) {
		if ((wParam & (OS.MK_CONTROL | OS.MK_SHIFT)) == 0) {
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_STATE;
			tvItem.stateMask = OS.TVIS_SELECTED;
			tvItem.hItem = lpht.hItem;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			if ((tvItem.state & OS.TVIS_SELECTED) == 0) {
				ignoreSelect = true;
				OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, 0);
				ignoreSelect = false;
				OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, lpht.hItem);
			}
		}
	}
	return LRESULT.ZERO;
}

LRESULT WM_SETFOCUS (int wParam, int lParam) {
	LRESULT result = super.WM_SETFOCUS (wParam, lParam);
	if ((style & SWT.SINGLE) != 0) return result;
	/*
	* Feature in Windows.  When multiple item have
	* the TVIS_SELECTED state, Windows redraws only
	* the focused item in the color used to show the
	* selection when the tree loses or gains focus.
	* The fix is to force Windows to redraw all the
	* visible items when focus is gained or lost.
	*/
	OS.InvalidateRect (handle, null, false);
	return result;
}

LRESULT WM_SYSCOLORCHANGE (int wParam, int lParam) {
	LRESULT result = super.WM_SYSCOLORCHANGE (wParam, lParam);
	if (result != null) return result;
	if ((style & SWT.CHECK) != 0) setCheckboxImageList ();
	return result;
}

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
					/*
					* Feature on Windows.  When a new tree item is inserted
					* using TVM_INSERTITEM and the tree is using custom draw,
					* a NM_CUSTOMDRAW is sent before TVM_INSERTITEM returns
					* and before the item is added to the items array.  The
					* fix is to check for null.
					* 
					* NOTE: This only happens on XP with the version 6.00 of
					* COMCTL32.DLL,
					*/
					TreeItem item = items [nmcd.lItemlParam];
					if (item == null) break;
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
			POINT pt = new POINT ();
			pt.x = (short) (pos & 0xFFFF);
			pt.y = (short) (pos >> 16);
			OS.ScreenToClient (handle, pt);
			TVHITTESTINFO lpht = new TVHITTESTINFO ();
			lpht.x = pt.x;
			lpht.y = pt.y;
			OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
			if ((lpht.flags & OS.TVHT_ONITEM) == 0) break;
			// FALL THROUGH
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
				/*
				* Feature on Windows.  In some cases, TVM_ITEMEXPANDING
				* is sent from within TVM_DELETEITEM for the tree item
				* being destroyed.  By the time the message is sent,
				* the item has already been removed from the list of
				* items.  The fix is to check for null. 
				*/
				TreeItem item = items [tvItem.lParam];
				if (item == null) break;
				Event event = new Event ();
				event.item = item;
				/*
				* It is possible (but unlikely), that application
				* code could have disposed the widget in the expand
				* or collapse event.  If this happens, end the
				* processing of the Windows message by returning
				* zero as the result of the window proc.
				*/
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
		case OS.NM_RECOGNIZEGESTURE:
			/* 
			* Feature on Pocket PC.  The tree and table controls detect the tap
			* and hold gesture by default. They send a GN_CONTEXTMENU message to show
			* the popup menu.  This default behaviour is unwanted on Pocket PC 2002
			* when no menu has been set, as it still draws a red circle.  The fix
			* is to disable this default behaviour when no menu is set by returning
			* TRUE when receiving the Pocket PC 2002 specific NM_RECOGNIZEGESTURE
			* message.
			*/
			if (OS.IsPPC) {
				boolean hasMenu = menu != null && !menu.isDisposed ();
				if (!hasMenu && !hooks (SWT.MenuDetect)) return LRESULT.ONE;
			}
			break;
		case OS.GN_CONTEXTMENU:
			if (OS.IsPPC) {
				boolean hasMenu = menu != null && !menu.isDisposed ();
				if (hasMenu || hooks (SWT.MenuDetect)) {
					NMRGINFO nmrg = new NMRGINFO ();
					OS.MoveMemory (nmrg, lParam, NMRGINFO.sizeof);
					showMenu (nmrg.x, nmrg.y);
					gestureCompleted = true;
					return LRESULT.ONE;
				}
			}
			break;
	}
	return super.wmNotifyChild (wParam, lParam);
}

}
