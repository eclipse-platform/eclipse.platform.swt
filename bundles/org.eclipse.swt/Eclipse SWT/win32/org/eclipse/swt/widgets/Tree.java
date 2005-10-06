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


import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class provide a selectable user interface object
 * that displays a hierarchy of items and issue notification when an
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
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION</dd>
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
	TreeColumn [] columns;
	int hwndParent, hwndHeader, hAnchor, hInsert;
	ImageList imageList;
	boolean dragStarted, gestureCompleted, insertAfter;
	boolean ignoreSelect, ignoreExpand, ignoreDeselect, ignoreResize;
	boolean lockSelection, oldSelected, newSelected;
	boolean linesVisible, customDraw, printClient;
	static final int INSET = 3;
	static final int GRID_WIDTH = 1;
	static final int HEADER_MARGIN = 10;
	static final int TreeProc;
	static final TCHAR TreeClass = new TCHAR (0, OS.WC_TREEVIEW, true);
	static final char [] BUTTON = new char [] {'B', 'U', 'T', 'T', 'O', 'N', 0};
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

int _getBackgroundPixel () {
	if (OS.IsWinCE) return OS.GetSysColor (OS.COLOR_WINDOW);
	int pixel = OS.SendMessage (handle, OS.TVM_GETBKCOLOR, 0, 0);
	if (pixel == -1) return OS.GetSysColor (OS.COLOR_WINDOW);
	return pixel;
}

void _setBackgroundPixel (int pixel) {
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

int borderHandle () {
	return hwndParent != 0 ? hwndParent : handle;
}

int callWindowProc (int hwnd, int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	if (hwndParent != 0 && hwnd == hwndParent) {
		return OS.DefWindowProc (hwnd, msg, wParam, lParam);
	}
	switch (msg) {
		/*
		* Bug in Windows.  For some reason, when the user clicks
		* on this control, the Windows hook WH_MSGFILTER is sent
		* despite the fact that an input event from a dialog box,
		* message box, menu, or scroll bar did not seem to occur.
		* The fix is to ignore the hook.
		*/
		case OS.WM_LBUTTONDOWN:
		case OS.WM_MBUTTONDOWN:
		case OS.WM_RBUTTONDOWN:
		case OS.WM_XBUTTONDOWN: {
			Display display = this.display;
			display.ignoreMsgFilter = true;
			int code = OS.CallWindowProc (TreeProc, hwnd, msg, wParam, lParam);
			display.ignoreMsgFilter = false;
			return code;
		}
		case OS.WM_MOUSEWHEEL: {
			int code = OS.CallWindowProc (TreeProc, hwnd, msg, wParam, lParam);
			updateScrollBar ();
			return code;
		}
	}
	return OS.CallWindowProc (TreeProc, hwnd, msg, wParam, lParam);
}

boolean checkHandle (int hwnd) {
	return hwnd == handle || (hwndParent != 0 && hwnd == hwndParent);
}

boolean checkScroll (int hItem) {
	/*
	* Feature in Windows.  If redraw is turned off using WM_SETREDRAW 
	* and a tree item that is not a child of the first root is selected or
	* scrolled using TVM_SELECTITEM or TVM_ENSUREVISIBLE, then scrolling
	* does not occur.  The fix is to detect this case, and make sure
	* that redraw is temporarly enabled.  To avoid flashing, DefWindowProc()
	* is called to disable redrawing.
	* 
	* NOTE:  The code that actually works around the problem is in the
	* callers of this method.
	*/
	if (drawCount == 0) return false;
	int hRoot = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	int hParent = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PARENT, hItem);
	while (hParent != hRoot && hParent != 0) {
		hParent = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PARENT, hParent);
	}
	return hParent == 0;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	if (hwndHeader != 0) {
		HDITEM hdItem = new HDITEM ();
		hdItem.mask = OS.HDI_WIDTH;
		int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
		for (int i=0; i<count; i++) {
			OS.SendMessage (hwndHeader, OS.HDM_GETITEM, i, hdItem);
			width += hdItem.cxy;
		}
		RECT rect = new RECT ();					
		OS.GetWindowRect (hwndHeader, rect);
		height += rect.bottom - rect.top;
	}
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
	width += border * 2;
	height += border * 2;
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
	
	/*
	* Feature in Windows.  In version 5.8 of COMCTL32.DLL,
	* if the font is changed for an item, the bounds for the
	* item are not updated, causing the text to be clipped.
	* The fix is to detect the version of COMCTL32.DLL, and
	* if it is one of the versions with the problem, then
	* use version 5.00 of the control (a version that does
	* not have the problem).  This is the recomended work
	* around from the MSDN.
	*/
	if (!OS.IsWinCE) {
		if (OS.COMCTL32_MAJOR < 6) {
			OS.SendMessage (handle, OS.CCM_SETVERSION, 5, 0);
		}
	}
		
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

void createItem (TreeColumn column, int index) {
	if (hwndHeader == 0) createParent ();
	int columnCount = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (!(0 <= index && index <= columnCount)) error (SWT.ERROR_INVALID_RANGE);
	if (columnCount == columns.length) {
		TreeColumn [] newColumns = new TreeColumn [columns.length + 4];
		System.arraycopy (columns, 0, newColumns, 0, columns.length);
		columns = newColumns;
	}
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null) {
			String [] strings = item.strings;
			if (strings != null) {
				String [] temp = new String [columnCount + 1];
				System.arraycopy (strings, 0, temp, 0, index);
				System.arraycopy (strings, index, temp, index + 1, columnCount - index);
				item.strings = temp;
			}
			Image [] images = item.images;
			if (images != null) {
				Image [] temp = new Image [columnCount + 1];
				System.arraycopy (images, 0, temp, 0, index);
				System.arraycopy (images, index, temp, index + 1, columnCount - index);
				item.images = temp;
			}
			if (index == 0) {
				if (columnCount != 0) {
					if (strings == null) {
						item.strings = new String [columnCount + 1];
						item.strings [1] = item.text;
					}
					item.text = "";
					if (images == null) {
						item.images = new Image [columnCount + 1];
						item.images [1] = item.image;
					}
					item.image = null;
				}
			}
			if (item.cellBackground != null) {
				int [] cellBackground = item.cellBackground;
				int [] temp = new int [columnCount + 1];
				System.arraycopy (cellBackground, 0, temp, 0, index);
				System.arraycopy (cellBackground, index, temp, index + 1, columnCount - index);
				temp [index] = -1;
				item.cellBackground = temp;
			}
			if (item.cellForeground != null) {
				int [] cellForeground = item.cellForeground;
				int [] temp = new int [columnCount + 1];
				System.arraycopy (cellForeground, 0, temp, 0, index);
				System.arraycopy (cellForeground, index, temp, index + 1, columnCount - index);
				temp [index] = -1;
				item.cellForeground = temp;
			}
			if (item.cellFont != null) {
				int [] cellFont = item.cellFont;
				int [] temp = new int [columnCount + 1];
				System.arraycopy (cellFont, 0, temp, 0, index);
				System.arraycopy (cellFont, index, temp, index + 1, columnCount- index);
				temp [index] = -1;
				item.cellFont = temp;
			}
		}
	}
	System.arraycopy (columns, index, columns, index + 1, columnCount - index);
	columns [index] = column;
	
	/*
	* Bug in Windows.  For some reason, when HDM_INSERTITEM
	* is used to insert an item into a header without text,
	* if is not possible to set the text at a later time.
	* The fix is to insert the item with an empty string.
	*/
	int hHeap = OS.GetProcessHeap ();
	int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, TCHAR.sizeof);
	HDITEM hdItem = new HDITEM ();
	hdItem.mask = OS.HDI_TEXT | OS.HDI_FORMAT;
	hdItem.pszText = pszText;
	if ((column.style & SWT.LEFT) == SWT.LEFT) hdItem.fmt = OS.HDF_LEFT;
	if ((column.style & SWT.CENTER) == SWT.CENTER) hdItem.fmt = OS.HDF_CENTER;
	if ((column.style & SWT.RIGHT) == SWT.RIGHT) hdItem.fmt = OS.HDF_RIGHT;
	OS.SendMessage (hwndHeader, OS.HDM_INSERTITEM, index, hdItem);
	if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
	
	/* When the first column is created, hide the horizontal scroll bar */
	if (columnCount == 0) {
		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
		bits |= OS.TVS_NOHSCROLL;
		OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
	}
	setScrollWidth ();
	updateScrollBar ();
	
	/* Redraw to hide the items when the first column is created */
	if (columnCount == 0 && OS.SendMessage (handle, OS.TVM_GETCOUNT, 0, 0) != 0) {
		OS.InvalidateRect (handle, null, true);
	}
}

void createItem (TreeItem item, int hParent, int hInsertAfter) {
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
	tvInsert.pszText = OS.LPSTR_TEXTCALLBACK;
	tvInsert.iImage = tvInsert.iSelectedImage = OS.I_IMAGECALLBACK;
	tvInsert.mask = OS.TVIF_TEXT | OS.TVIF_IMAGE | OS.TVIF_SELECTEDIMAGE | OS.TVIF_HANDLE | OS.TVIF_PARAM;
	if ((style & SWT.CHECK) != 0) {
		tvInsert.mask = tvInsert.mask | OS.TVIF_STATE;
		tvInsert.state = 1 << 12;
		tvInsert.stateMask = OS.TVIS_STATEIMAGEMASK;
	}
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
	if (OS.IsWindowVisible (handle) && drawCount == 0) {
		int hChild = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hParent);
		if (hChild != 0 && OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hChild) == 0) {
			RECT rect = new RECT ();
			rect.left = hParent;
			if (OS.SendMessage (handle, OS.TVM_GETITEMRECT, 0, rect) != 0) {
				OS.InvalidateRect (handle, rect, true);
			}
		}
	}
	updateScrollBar ();
}

void createParent () {
	forceResize ();
	RECT rect = new RECT ();
	OS.GetWindowRect (handle, rect);
	OS.MapWindowPoints (0, parent.handle, rect, 2);
	int oldStyle = OS.GetWindowLong (handle, OS.GWL_STYLE);
	int newStyle = super.widgetStyle () & ~OS.WS_VISIBLE;
	if ((oldStyle & OS.WS_DISABLED) != 0) newStyle |= OS.WS_DISABLED;
//	if ((oldStyle & OS.WS_VISIBLE) != 0) newStyle |= OS.WS_VISIBLE;
	hwndParent = OS.CreateWindowEx (
		super.widgetExtStyle (),
		super.windowClass (),
		null,
		newStyle,
		rect.left, 
		rect.top, 
		rect.right - rect.left, 
		rect.bottom - rect.top,
		parent.handle,
		0,
		OS.GetModuleHandle (null),
		null);
	if (hwndParent == 0) error (SWT.ERROR_NO_HANDLES);
	OS.SetWindowLong (hwndParent, OS.GWL_ID, hwndParent);
	int bits = 0;
	if (OS.WIN32_VERSION >= OS.VERSION (4, 10)) {
		bits |= OS.WS_EX_NOINHERITLAYOUT;
		if ((style & SWT.RIGHT_TO_LEFT) != 0) bits |= OS.WS_EX_LAYOUTRTL;
	}
	hwndHeader = OS.CreateWindowEx (
		bits,
		new TCHAR (0, OS.WC_HEADER, true),
		null,
		OS.HDS_BUTTONS | OS.HDS_FULLDRAG | OS.HDS_HIDDEN | OS.WS_CHILD | OS.WS_CLIPSIBLINGS,
		0, 0, 0, 0,
		hwndParent,
		0,
		OS.GetModuleHandle (null),
		null);
	if (hwndHeader == 0) error (SWT.ERROR_NO_HANDLES);
	OS.SetWindowLong (hwndHeader, OS.GWL_ID, hwndHeader);
	if (OS.IsDBLocale) {
		int hIMC = OS.ImmGetContext (handle);
		OS.ImmAssociateContext (hwndParent, hIMC);
		OS.ImmAssociateContext (hwndHeader, hIMC);		
		OS.ImmReleaseContext (handle, hIMC);
	}
	if (!OS.IsPPC) {
		if ((style & SWT.BORDER) != 0) {
			int oldExStyle = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
			oldExStyle &= ~OS.WS_EX_CLIENTEDGE;
			OS.SetWindowLong (handle, OS.GWL_EXSTYLE, oldExStyle);
		}
	}
	int hFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (hFont != 0) OS.SendMessage (hwndHeader, OS.WM_SETFONT, hFont, 0);
	int hImageList = OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_NORMAL, 0);
	if (hImageList != 0) {
		OS.SendMessage (hwndHeader, OS.HDM_SETIMAGELIST, 0, hImageList);
	}
	int hwndInsertAfter = OS.GetWindow (handle, OS.GW_HWNDPREV);
	int flags = OS.SWP_NOSIZE | OS.SWP_NOMOVE | OS.SWP_NOACTIVATE;
	SetWindowPos (hwndParent, hwndInsertAfter, 0, 0, 0, 0, flags);
	SCROLLINFO info = new SCROLLINFO ();
	info.cbSize = SCROLLINFO.sizeof;
	info.fMask = OS.SIF_RANGE | OS.SIF_PAGE;
	OS.GetScrollInfo (hwndParent, OS.SB_HORZ, info);
	info.nPage = info.nMax + 1;
	OS.SetScrollInfo (hwndParent, OS.SB_HORZ, info, true);
	OS.GetScrollInfo (hwndParent, OS.SB_VERT, info);
	info.nPage = info.nMax + 1;
	OS.SetScrollInfo (hwndParent, OS.SB_VERT, info, true);
	customDraw = true;
	deregister ();
	if ((oldStyle & OS.WS_VISIBLE) != 0) {
		OS.ShowWindow (hwndParent, OS.SW_SHOW);
	}
	int hwndFocus = OS.GetFocus ();
	if (hwndFocus == handle) OS.SetFocus (hwndParent);
	OS.SetParent (handle, hwndParent);
	if (hwndFocus == handle) OS.SetFocus (handle);
	register ();
}

void createWidget () {
	super.createWidget ();
	items = new TreeItem [4];
	columns = new TreeColumn [4];
}

int defaultBackground () {
	return OS.GetSysColor (OS.COLOR_WINDOW);
}

void deregister () {
	super.deregister ();
	if (hwndParent != 0) display.removeControl (hwndParent);
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
	} else {
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
}

void destroyItem (TreeColumn column) {
	if (hwndHeader == 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
	int columnCount = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	int index = 0;
	while (index < columnCount) {
		if (columns [index] == column) break;
		index++;
	}
	if (OS.SendMessage (hwndHeader, OS.HDM_DELETEITEM, index, 0) == 0) {
		error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	System.arraycopy (columns, index + 1, columns, index, --columnCount - index);
	columns [columnCount] = null;
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null) {
			if (columnCount == 0) {
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
					String [] temp = new String [columnCount];
					System.arraycopy (strings, 0, temp, 0, index);
					System.arraycopy (strings, index + 1, temp, index, columnCount - index);
					item.strings = temp;
				} else {
					if (index == 0) item.text = "";
				}
				if (item.images != null) {
					Image [] images = item.images;
					if (index == 0) item.image = images [1];
					Image [] temp = new Image [columnCount];
					System.arraycopy (images, 0, temp, 0, index);
					System.arraycopy (images, index + 1, temp, index, columnCount - index);
					item.images = temp;
				} else {
					if (index == 0) item.image = null;
				}
				if (item.cellBackground != null) {
					int [] cellBackground = item.cellBackground;
					int [] temp = new int [columnCount];
					System.arraycopy (cellBackground, 0, temp, 0, index);
					System.arraycopy (cellBackground, index + 1, temp, index, columnCount - index);
					item.cellBackground = temp;
				}
				if (item.cellForeground != null) {
					int [] cellForeground = item.cellForeground;
					int [] temp = new int [columnCount];
					System.arraycopy (cellForeground, 0, temp, 0, index);
					System.arraycopy (cellForeground, index + 1, temp, index, columnCount - index);
					item.cellForeground = temp;
				}
				if (item.cellFont != null) {
					int [] cellFont = item.cellFont;
					int [] temp = new int [columnCount];
					System.arraycopy (cellFont, 0, temp, 0, index);
					System.arraycopy (cellFont, index + 1, temp, index, columnCount - index);
					item.cellFont = temp;
				}
			}
		}
	}

	/*
	* When the last column is deleted, show the horizontal
	* scroll bar.   Otherwise, left align the first column
	* and redraw the columns to the right.
	*/
	if (columnCount == 0) {
		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
		bits &= ~OS.TVS_NOHSCROLL;
		OS.SetWindowLong (handle, OS.GWL_STYLE, bits);
	    OS.InvalidateRect (handle, null, true);
	} else {
		if (index == 0) {
			columns [0].style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
			columns [0].style |= SWT.LEFT;
			HDITEM hdItem = new HDITEM ();
			hdItem.mask = OS.HDI_FORMAT | OS.HDI_IMAGE;
			OS.SendMessage (hwndHeader, OS.HDM_GETITEM, index, hdItem);
			hdItem.fmt &= ~OS.HDF_JUSTIFYMASK;
			hdItem.fmt |= OS.HDF_LEFT;
			OS.SendMessage (hwndHeader, OS.HDM_SETITEM, index, hdItem);
		}
		RECT rect = new RECT (), itemRect = new RECT ();
		OS.GetClientRect (handle, rect);
		OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, index - 1, itemRect);
		rect.left = itemRect.right;
	    OS.InvalidateRect (handle, rect, true);
	}
	setScrollWidth ();
	for (int i=index; i<columnCount; i++) {
		columns [i].sendEvent (SWT.Move);
	}
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
	if ((style & SWT.MULTI) != 0) {
		ignoreDeselect = ignoreSelect = lockSelection = true;
	}
	OS.SendMessage (handle, OS.TVM_DELETEITEM, 0, hItem);
	if ((style & SWT.MULTI) != 0) {
		ignoreDeselect = ignoreSelect = lockSelection = false;
	}
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
			if (hwndHeader != 0) {
				OS.SendMessage (hwndHeader, OS.HDM_SETIMAGELIST, 0, 0);
			}
			display.releaseImageList (imageList);
		}
		imageList = null;
		if (hwndParent == 0) customDraw = false;
		items = new TreeItem [4];
	}
	updateScrollBar ();
}

void enableWidget (boolean enabled) {
	super.enableWidget (enabled);
	/*
	* Feature in Windows.  When a tree is given a background color
	* using TVM_SETBKCOLOR and the tree is disabled, Windows draws
	* the tree using the background color rather than the disabled
	* colors.  This is different from the table which draws grayed.
	* The fix is to set the default background color while the tree
	* is disabled and restore it when enabled.
	*/
	if (background != -1) {
		_setBackgroundPixel (enabled ? background : -1);
	}
	if (hwndParent != 0) OS.EnableWindow (hwndParent, enabled);
}

Widget findItem (int id) {
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
	tvItem.hItem = id;
	if (OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem) != 0) {
		int lParam = tvItem.lParam;
		if (0 <= lParam && lParam < items.length) return items [lParam];
	}
	return null;
}

int getBackgroundPixel () {
	if (!OS.IsWinCE) return _getBackgroundPixel ();
	/*
	* Feature in Windows.  When a tree is given a background color
	* using TVM_SETBKCOLOR and the tree is disabled, Windows draws
	* the tree using the background color rather than the disabled
	* colors.  This is different from the table which draws grayed.
	* The fix is to set the default background color while the tree
	* is disabled and restore it when enabled.
	*/
	if (!OS.IsWindowEnabled (handle) && background != -1) {
		return background;
	}
	return _getBackgroundPixel ();
}

int getForegroundPixel () {
	if (OS.IsWinCE) return OS.GetSysColor (OS.COLOR_WINDOWTEXT);
	int pixel = OS.SendMessage (handle, OS.TVM_GETTEXTCOLOR, 0, 0);
	if (pixel == -1) return OS.GetSysColor (OS.COLOR_WINDOWTEXT);
	return pixel;
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
	return GRID_WIDTH;
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
	if (hwndHeader == 0) return 0;
	RECT rect = new RECT ();					
	OS.GetWindowRect (hwndHeader, rect);
	return rect.bottom - rect.top;
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
	if (hwndHeader == 0) return false;
	int bits = OS.GetWindowLong (hwndHeader, OS.GWL_STYLE);
	return (bits & OS.WS_VISIBLE) != 0;
}

Point getImageSize () {
	if (imageList != null) return imageList.getImageSize ();
	return new Point (0, getItemHeight ());
}

/**
 * Returns the column at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
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
 * @since 3.1
 */
public TreeColumn getColumn (int index) {
	checkWidget ();
	if (hwndHeader == 0) error (SWT.ERROR_INVALID_RANGE);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
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
	if (hwndHeader == 0) return 0;
	return OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
}

/**
 * Returns an array of <code>TreeColumn</code>s which are the
 * columns in the receiver. If no <code>TreeColumn</code>s were
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
 * @since 3.1
 */
public TreeColumn [] getColumns () {
	checkWidget ();
	if (hwndHeader == 0) return new TreeColumn [0];
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	TreeColumn [] result = new TreeColumn [count];
	System.arraycopy (columns, 0, result, 0, count);
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
	if (index < 0) error (SWT.ERROR_INVALID_RANGE);
	int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	while (index-- > 0 && hItem != 0) {
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
	}
	if (hItem == 0) error (SWT.ERROR_INVALID_RANGE);
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
	tvItem.hItem = hItem;
	OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
	return items [tvItem.lParam];
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
	if (lpht.hItem != 0) {
		if ((style & SWT.FULL_SELECTION) != 0 || (lpht.flags & OS.TVHT_ONITEM) != 0) {
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
			tvItem.hItem = lpht.hItem;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			return items [tvItem.lParam];
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
	return linesVisible;
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
		imageList = display.getImageList (style & SWT.RIGHT_TO_LEFT, bounds.width, bounds.height);
		int index = imageList.indexOf (image);
		if (index == -1) index = imageList.add (image);
		int hImageList = imageList.getHandle ();
		OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_NORMAL, hImageList);
		if (hwndHeader != 0) {
			OS.SendMessage (hwndHeader, OS.HDM_SETIMAGELIST, 0, hImageList);
		}
		updateScrollBar ();
		return index;
	}
	int index = imageList.indexOf (image);
	if (index != -1) return index;
	return imageList.add (image);
}

int indexOf (int hItem, int hChild) {
	int index = 0;
	while (hItem != 0 && hItem != hChild) {
		hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXT, hItem);
		index++;
	}
	return hItem == hChild ? index : -1;
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
 * 
 * @since 3.1
 */
public int indexOf (TreeColumn column) {
	checkWidget ();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (column.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (hwndHeader == 0) return -1;
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	for (int i=0; i<count; i++) {
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
 *    <li>ERROR_NULL_ARGUMENT - if the tool item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the tool item has been disposed</li>
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
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	return hItem == 0 ? -1 : indexOf (hItem, item.handle);
}

void register () {
	super.register ();
	if (hwndParent != 0) display.addControl (hwndParent, this);
}

boolean releaseItem (TreeItem item, TVITEM tvItem) {
	int hItem = item.handle;
	if (hItem == hAnchor) hAnchor = 0;
	if (hItem == hInsert) hInsert = 0;
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

void releaseHandle () {
	super.releaseHandle ();
	hwndParent = hwndHeader = 0;
}

void releaseWidget () {
	int columnCount = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	for (int i=0; i<items.length; i++) {
		TreeItem item = items [i];
		if (item != null && !item.isDisposed ()) {
			item.releaseResources ();
		}
	}
	items = null;
	for (int i=0; i<columnCount; i++) {
		TreeColumn column = columns [i];
		if (!column.isDisposed ()) column.releaseResources ();
	}
	columns = null;
	/*
	* Feature in Windows.  For some reason, when TVM_GETIMAGELIST
	* or TVM_SETIMAGELIST is sent, the tree issues NM_CUSTOMDRAW
	* messages.  This behavior is unwanted when the tree is being
	* disposed.  The fix is to ingore NM_CUSTOMDRAW messages by
	* clearing the custom draw flag.
	* 
	* NOTE: This only happens on Windows XP.
	*/
	customDraw = false;
	if (imageList != null) {
		OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_NORMAL, 0);
		OS.SendMessage (hwndHeader, OS.HDM_SETIMAGELIST, 0, 0);
		display.releaseImageList (imageList);
	} else {
		int hOldList = OS.SendMessage (handle, OS.TVM_GETIMAGELIST, OS.TVSIL_NORMAL, 0);
		OS.SendMessage (handle, OS.TVM_SETIMAGELIST, OS.TVSIL_NORMAL, 0);
		OS.SendMessage (hwndHeader, OS.HDM_SETIMAGELIST, 0, 0);
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
		if (hwndHeader != 0) {
			OS.SendMessage (hwndHeader, OS.HDM_SETIMAGELIST, 0, 0);
		}
		display.releaseImageList (imageList);
	}
	imageList = null;
	if (hwndParent == 0) customDraw = false;
	items = new TreeItem [4];
	hAnchor = hInsert = 0;
	updateScrollBar ();
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
	hInsert = hItem;
	insertAfter = !before;
	OS.SendMessage (handle, OS.TVM_SETINSERTMARK, insertAfter ? 1 : 0, hInsert);
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
	if (linesVisible == show) return;
	linesVisible = show;
	OS.InvalidateRect (handle, null, true);
}

int scrolledHandle () {
	if (hwndHeader == 0) return handle;
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	return count == 0 ? handle : hwndParent;
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
	if ((style & SWT.SINGLE) != 0) return;
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
	* Feature in Windows.  When a tree is given a background color
	* using TVM_SETBKCOLOR and the tree is disabled, Windows draws
	* the tree using the background color rather than the disabled
	* colors.  This is different from the table which draws grayed.
	* The fix is to set the default background color while the tree
	* is disabled and restore it when enabled.
	*/
	if (OS.IsWindowEnabled (handle)) _setBackgroundPixel (pixel);
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
	* is sent from SendMessage(), Windows GP's in the window proc for
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
	int flags = ImageList.COLOR_FLAGS;
	if ((style & SWT.RIGHT_TO_LEFT) != 0) flags |= OS.ILC_MIRROR;	
	if (OS.COMCTL32_MAJOR < 6 || !OS.IsAppThemed ()) flags |= OS.ILC_MASK;
	int hImageList = OS.ImageList_Create (width, height, flags, count, count);
	int hDC = OS.GetDC (handle);
	int memDC = OS.CreateCompatibleDC (hDC);
	int hBitmap = OS.CreateCompatibleBitmap (hDC, width * count, height);
	int hOldBitmap = OS.SelectObject (memDC, hBitmap);
	RECT rect = new RECT ();
	OS.SetRect (rect, 0, 0, width * count, height);
	int clrBackground = _getBackgroundPixel ();
	int hBrush = OS.CreateSolidBrush (clrBackground);
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
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		int hTheme = OS.OpenThemeData (handle, BUTTON);
		OS.DrawThemeBackground (hTheme, memDC, OS.BP_CHECKBOX, OS.CBS_UNCHECKEDNORMAL, rect, null);
		rect.left += width;  rect.right += width;
		OS.DrawThemeBackground (hTheme, memDC, OS.BP_CHECKBOX, OS.CBS_CHECKEDNORMAL, rect, null);
		rect.left += width;  rect.right += width;
		OS.DrawThemeBackground (hTheme, memDC, OS.BP_CHECKBOX, OS.CBS_UNCHECKEDNORMAL, rect, null);
		rect.left += width;  rect.right += width;
		OS.DrawThemeBackground (hTheme, memDC, OS.BP_CHECKBOX, OS.CBS_MIXEDNORMAL, rect, null);
		OS.CloseThemeData (hTheme);
	} else {
		OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_FLAT);
		rect.left += width;  rect.right += width;
		OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_CHECKED | OS.DFCS_FLAT);
		rect.left += width;  rect.right += width;
		OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_INACTIVE | OS.DFCS_FLAT);
		rect.left += width;  rect.right += width;
		OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_CHECKED | OS.DFCS_INACTIVE | OS.DFCS_FLAT);
	}
	OS.SelectObject (memDC, hOldBitmap);
	OS.DeleteDC (memDC);
	OS.ReleaseDC (handle, hDC);
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
		OS.ImageList_Add (hImageList, hBitmap, 0);
	} else {
		OS.ImageList_AddMasked (hImageList, hBitmap, clrBackground);
	}
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
	if (hwndHeader == 0) {
		if (!show) return;
		createParent ();
	}
	int bits = OS.GetWindowLong (hwndHeader, OS.GWL_STYLE);
	if (show) {
		if ((bits & OS.HDS_HIDDEN) == 0) return;
		bits &= ~OS.HDS_HIDDEN;
		OS.SetWindowLong (hwndHeader, OS.GWL_STYLE, bits);
		OS.ShowWindow (hwndHeader, OS.SW_SHOW);
	} else {
		if ((bits & OS.HDS_HIDDEN) != 0) return;
		bits |= OS.HDS_HIDDEN;
		OS.SetWindowLong (hwndHeader, OS.GWL_STYLE, bits);
		OS.ShowWindow (hwndHeader, OS.SW_HIDE);
	}
	setScrollWidth ();
	updateScrollBar ();
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

void setScrollWidth () {
	if (hwndHeader == 0 || hwndParent == 0) return;
	int width = 0;
	HDITEM hdItem = new HDITEM ();
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	for (int i=0; i<count; i++) {
		hdItem.mask = OS.HDI_WIDTH;
		OS.SendMessage (hwndHeader, OS.HDM_GETITEM, i, hdItem);
		width += hdItem.cxy;
	}
	int left = 0;
	RECT rect = new RECT ();
	SCROLLINFO info = new SCROLLINFO ();
	info.cbSize = SCROLLINFO.sizeof;
	info.fMask = OS.SIF_RANGE | OS.SIF_PAGE;
	if (count == 0) {
		OS.GetScrollInfo (hwndParent, OS.SB_HORZ, info);
		info.nPage = info.nMax + 1;
		OS.SetScrollInfo (hwndParent, OS.SB_HORZ, info, true);
		OS.GetScrollInfo (hwndParent, OS.SB_VERT, info);
		info.nPage = info.nMax + 1;
		OS.SetScrollInfo (hwndParent, OS.SB_VERT, info, true);
	} else {
		OS.GetClientRect (hwndParent, rect);
		OS.GetScrollInfo (hwndParent, OS.SB_HORZ, info);
		info.nMax = width;
		info.nPage = rect.right - rect.left;
		OS.SetScrollInfo (hwndParent, OS.SB_HORZ, info, true);
		info.fMask = OS.SIF_POS;
		OS.GetScrollInfo (hwndParent, OS.SB_HORZ, info);
		left = info.nPos;
	}
	OS.GetClientRect (hwndParent, rect);
	int hHeap = OS.GetProcessHeap ();
	HDLAYOUT playout = new HDLAYOUT ();
	playout.prc = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, RECT.sizeof);
	playout.pwpos = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, WINDOWPOS.sizeof);
	OS.MoveMemory (playout.prc, rect, RECT.sizeof);
	OS.SendMessage (hwndHeader, OS.HDM_LAYOUT, 0, playout);
	WINDOWPOS pos = new WINDOWPOS ();
	OS.MoveMemory (pos, playout.pwpos, WINDOWPOS.sizeof);
	if (playout.prc != 0) OS.HeapFree (hHeap, 0, playout.prc);
	if (playout.pwpos != 0) OS.HeapFree (hHeap, 0, playout.pwpos);
	SetWindowPos (hwndHeader, OS.HWND_TOP, pos.x - left, pos.y, pos.cx + left, pos.cy, OS.SWP_NOACTIVATE);
	int w = pos.cx + (count == 0 ? 0 : OS.GetSystemMetrics (OS.SM_CXVSCROLL));
	int h = rect.bottom - rect.top - pos.cy;
	boolean oldIgnore = ignoreResize;
	ignoreResize = true;
	SetWindowPos (handle, 0, pos.x - left, pos.y + pos.cy, w + left, h, OS.SWP_NOACTIVATE | OS.SWP_NOZORDER);
	ignoreResize = oldIgnore;
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
 * @see Tree#deselectAll()
 */
public void setSelection (TreeItem [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = items.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) {
		deselectAll();
		return;
	}
		
	/* Select/deselect the first item */
	TreeItem item = items [0];
	if (item != null) {
		if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		int hOldItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
		int hNewItem = hAnchor = item.handle;
		
		/*
		* Bug in Windows.  When TVM_SELECTITEM is used to select and
		* scroll an item to be visible and the client area of the tree
		* is smaller that the size of one item, TVM_SELECTITEM makes
		* the next item in the tree visible by making it the top item
		* instead of making the desired item visible.  The fix is to
		* detect the case when the client area is too small and make
		* the desired visible item be the top item in the tree.
		* 
		* Note that TVM_SELECTITEM when called with TVGN_FIRSTVISIBLE
		* also requires the work around for scrolling.
		*/
		boolean fixScroll = checkScroll (hNewItem);
		if (fixScroll) {
			OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
		}
		ignoreSelect = true;
		OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_CARET, hNewItem);
		ignoreSelect = false;
		if (OS.SendMessage (handle, OS.TVM_GETVISIBLECOUNT, 0, 0) == 0) {
			OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_FIRSTVISIBLE, hNewItem);
		}
		if (fixScroll) {
			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
			OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
		}
		
		/*
		* Feature in Windows.  When the old and new focused item
		* are the same, Windows does not check to make sure that
		* the item is actually selected, not just focused.  The
		* fix is to force the item to draw selected by setting
		* the state mask, and to ensure that it is visible.
		*/
		if (hOldItem == hNewItem) {
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_STATE;
			tvItem.state = OS.TVIS_SELECTED;
			tvItem.stateMask = OS.TVIS_SELECTED;
			tvItem.hItem = hNewItem;
			OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
			showItem (hNewItem);
		}
	}
	if ((style & SWT.SINGLE) != 0) return;

	/* Select/deselect the rest of the items */
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_STATE;
	tvItem.stateMask = OS.TVIS_SELECTED;
	int oldProc = OS.GetWindowLong (handle, OS.GWL_WNDPROC);
	OS.SetWindowLong (handle, OS.GWL_WNDPROC, TreeProc);
	for (int i=0; i<this.items.length; i++) {
		item = this.items [i];
		if (item != null) {
			int index = 0;
			while (index < length) {
				if (items [index] == item) break;
				index++;
			}
			tvItem.hItem = item.handle;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
				if (index == length) {
					tvItem.state = 0;
					OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
				}
			} else {
				if (index != length) {
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
	int hItem = item.handle;
	boolean fixScroll = checkScroll (hItem);
	if (fixScroll) {
		OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
		OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
	}
	OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_FIRSTVISIBLE, hItem);
	if (fixScroll) {
		OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
		OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
	}
	updateScrollBar ();
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
		boolean fixScroll = checkScroll (hItem);
		if (fixScroll) {
			OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
		}
		OS.SendMessage (handle, OS.TVM_SELECTITEM, OS.TVGN_FIRSTVISIBLE, hItem);
		OS.SendMessage (handle, OS.WM_HSCROLL, OS.SB_TOP, 0);
		if (fixScroll) {
			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
			OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
		}
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
		if (scroll) {
			boolean fixScroll = checkScroll (hItem);
			if (fixScroll) {
				OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
			}
			OS.SendMessage (handle, OS.TVM_ENSUREVISIBLE, 0, hItem);
			if (fixScroll) {
				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
				OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
			}
		}
	}
	if (hwndParent != 0) {
		RECT itemRect = new RECT ();
		itemRect.left = hItem;
		if (OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, itemRect) != 0) {
			forceResize ();
			RECT rect = new RECT ();
			OS.GetClientRect (hwndParent, rect);
			OS.MapWindowPoints (hwndParent, handle, rect, 2);
			POINT pt = new POINT ();
			pt.x = itemRect.left;
			pt.y = itemRect.top;
			if (!OS.PtInRect (rect, pt)) {
				pt.y = itemRect.bottom;
				if (!OS.PtInRect (rect, pt)) {
					SCROLLINFO info = new SCROLLINFO ();
					info.cbSize = SCROLLINFO.sizeof;
					info.fMask = OS.SIF_POS;
					info.nPos = Math.max (0, pt.x - Tree.INSET / 2);
					OS.SetScrollInfo (hwndParent, OS.SB_HORZ, info, true);
					setScrollWidth ();
				}
			}
		}
	}
	updateScrollBar ();
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
	int index = indexOf (column);
	if (index == -1) return;
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (0 <= index && index < count) {
		if (hwndParent != 0) {
			forceResize ();
			RECT rect = new RECT ();
			OS.GetClientRect (hwndParent, rect);
			OS.MapWindowPoints (hwndParent, handle, rect, 2);
			int x = 0;
			HDITEM hdItem = new HDITEM ();
			for (int i=0; i<index; i++) {
				hdItem.mask = OS.HDI_WIDTH;
				OS.SendMessage (hwndHeader, OS.HDM_GETITEM, i, hdItem);
				x += hdItem.cxy;
			}
			POINT pt = new POINT ();
			pt.x = x;
			pt.y = rect.top;
			if (!OS.PtInRect (rect, pt)) {
				SCROLLINFO info = new SCROLLINFO ();
				info.cbSize = SCROLLINFO.sizeof;
				info.fMask = OS.SIF_POS;
				info.nPos = Math.max (0, pt.x - Tree.INSET / 2);
				OS.SetScrollInfo (hwndParent, OS.SB_HORZ, info, true);
				setScrollWidth ();
			}
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

void showWidget (boolean visible) {
	super.showWidget (visible);
	if (hwndParent != 0) {
		OS.ShowWindow (hwndParent, visible ? OS.SW_SHOW : OS.SW_HIDE);
	}
}

String toolTipText (NMTTDISPINFO hdr) {
	int hwndToolTip = OS.SendMessage (handle, OS.TVM_GETTOOLTIPS, 0, 0);
	if (hwndToolTip == hdr.hwndFrom && toolTipText != null) return ""; //$NON-NLS-1$
	return super.toolTipText (hdr);
}

int topHandle () {
	return hwndParent != 0 ? hwndParent : handle;
}

void updateScrollBar () {
	if (hwndParent != 0) {
		int columnCount = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
		if (columnCount != 0) {
			SCROLLINFO info = new SCROLLINFO ();
			info.cbSize = SCROLLINFO.sizeof;
			info.fMask = OS.SIF_ALL;
			int itemCount = OS.SendMessage (handle, OS.TVM_GETCOUNT, 0, 0);
			if (itemCount == 0) {
				OS.GetScrollInfo (hwndParent, OS.SB_VERT, info);
				info.nPage = info.nMax + 1;
				OS.SetScrollInfo (hwndParent, OS.SB_VERT, info, true);
			} else {
				OS.GetScrollInfo (handle, OS.SB_VERT, info);
				OS.SetScrollInfo (hwndParent, OS.SB_VERT, info, true);
			}
		}
	}
}

int widgetStyle () {
	int bits = super.widgetStyle () | OS.TVS_SHOWSELALWAYS | OS.TVS_LINESATROOT | OS.TVS_HASBUTTONS;
	if ((style & SWT.FULL_SELECTION) != 0) {
		bits |= OS.TVS_FULLROWSELECT;
	} else {
		bits |= OS.TVS_HASLINES;
	}
//	bits |= OS.TVS_NOTOOLTIPS;
	return bits;
}

TCHAR windowClass () {
	return TreeClass;
}

int windowProc () {
	return TreeProc;
}

int windowProc (int hwnd, int msg, int wParam, int lParam) {
	if (hwndParent != 0 && hwnd == hwndParent) {
		switch (msg) {
			case OS.WM_MOVE: {
				sendEvent (SWT.Move);
				return 0;
			}
			case OS.WM_SIZE: {
				setScrollWidth ();
				if (ignoreResize) return 0;
				setResizeChildren (false);
				int code = callWindowProc (hwnd, OS.WM_SIZE, wParam, lParam);
				sendEvent (SWT.Resize);
				if (isDisposed ()) return 0;
				if (layout != null) {
					markLayout (false, false);
					updateLayout (false, false);
				}
				setResizeChildren (true);
				return code;
			}
			case OS.WM_COMMAND:
			case OS.WM_NOTIFY:
			case OS.WM_SYSCOLORCHANGE: {
				return OS.SendMessage (handle, msg, wParam, lParam);
			}
			case OS.WM_VSCROLL: {
				SCROLLINFO info = new SCROLLINFO ();
				info.cbSize = SCROLLINFO.sizeof;
				info.fMask = OS.SIF_ALL;
				OS.GetScrollInfo (hwndParent, OS.SB_VERT, info);
				OS.SetScrollInfo (handle, OS.SB_VERT, info, true);
				int code = OS.SendMessage (handle, OS.WM_VSCROLL, wParam, lParam);
				OS.GetScrollInfo (handle, OS.SB_VERT, info);
				OS.SetScrollInfo (hwndParent, OS.SB_VERT, info, true);
				return code;
			}
			case OS.WM_HSCROLL: {
				/*
				* Bug on WinCE.  lParam should be NULL when the message is not sent
				* by a scroll bar control, but it contains the handle to the window.
				* When the message is sent by a scroll bar control, it correctly
				* contains the handle to the scroll bar.  The fix is to check for
				* both.
				*/
				if (horizontalBar != null && (lParam == 0 || lParam == hwndParent)) {
					wmScroll (horizontalBar, true, hwndParent, OS.WM_HSCROLL, wParam, lParam);
				}
				setScrollWidth ();
				break;
			}
		}
		return callWindowProc (hwnd, msg, wParam, lParam);
	}
	int code = super.windowProc (handle, msg, wParam, lParam);
	switch (msg) {
		/* Keyboard messages */
		case OS.WM_CHAR:
		case OS.WM_IME_CHAR:
		case OS.WM_KEYDOWN:
		case OS.WM_KEYUP:
		case OS.WM_SYSCHAR:
		case OS.WM_SYSKEYDOWN:
		case OS.WM_SYSKEYUP:
			
		/* Mouse messages */
		case OS.WM_LBUTTONDBLCLK:
		case OS.WM_LBUTTONDOWN:
		case OS.WM_LBUTTONUP:
		case OS.WM_MBUTTONDBLCLK:
		case OS.WM_MBUTTONDOWN:
		case OS.WM_MBUTTONUP:
		case OS.WM_MOUSEHOVER:
		case OS.WM_MOUSELEAVE:
		case OS.WM_MOUSEMOVE:
		case OS.WM_MOUSEWHEEL:
		case OS.WM_RBUTTONDBLCLK:
		case OS.WM_RBUTTONDOWN:
		case OS.WM_RBUTTONUP:
		case OS.WM_XBUTTONDBLCLK:
		case OS.WM_XBUTTONDOWN:
		case OS.WM_XBUTTONUP:
			
		/* Other messages */
		case OS.WM_SIZE:
		case OS.WM_SETFONT:
		case OS.WM_TIMER: {
			updateScrollBar ();
		}
	}
	return code;
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
		case ' ': {
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
					if (!OS.IsWinCE) {
						int id = hItem;
						if (OS.COMCTL32_MAJOR >= 6) {
							id = OS.SendMessage (handle, OS.TVM_MAPHTREEITEMTOACCID, hItem, 0);
						}
						OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, handle, OS.OBJID_CLIENT, id);	
					}
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
			}
			return LRESULT.ZERO;
		}
		case SWT.CR: {
			/*
			* Feature in Windows.  Windows sends NM_RETURN from WM_KEYDOWN
			* instead of using WM_CHAR.  This means that application code
			* that expects to consume the key press and therefore avoid a
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
			return LRESULT.ZERO;
		}
		case SWT.ESC:
			return LRESULT.ZERO;
	}
	return result;
}

LRESULT WM_GETOBJECT (int wParam, int lParam) {
	/*
	* Ensure that there is an accessible object created for this
	* control because support for checked item and tree column
	* accessibility is temporarily implemented in the accessibility
	* package.
	*/
	if ((style & SWT.CHECK) != 0 || hwndParent != 0) {
		if (accessible == null) accessible = new_Accessible (this);
	}
	return super.WM_GETOBJECT (wParam, lParam);
}

LRESULT WM_KEYDOWN (int wParam, int lParam) {
	LRESULT result = super.WM_KEYDOWN (wParam, lParam);
	if (result != null) return result;
	switch (wParam) {
		case OS.VK_SPACE:
			/*
			* Ensure that the window proc does not process VK_SPACE
			* so that it can be handled in WM_CHAR.  This allows the
			* application to cancel an operation that is normally
			* performed in WM_KEYDOWN from WM_CHAR.
			*/
			return LRESULT.ZERO;
		case OS.VK_UP:
		case OS.VK_DOWN:
		case OS.VK_PRIOR:
		case OS.VK_NEXT:
		case OS.VK_HOME:
		case OS.VK_END: {
			OS.SendMessage (handle, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
			if ((style & SWT.SINGLE) != 0) break;
			if (OS.GetKeyState (OS.VK_SHIFT) < 0) {
				int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
				if (hItem != 0) {
					if (hAnchor == 0) hAnchor = hItem;
					ignoreSelect = ignoreDeselect = true;
					int code = callWindowProc (handle, OS.WM_KEYDOWN, wParam, lParam);
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
							int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
							int fItemRect = (bits & OS.TVS_FULLROWSELECT) != 0 ? 0 : 1;
							OS.SendMessage (handle, OS.TVM_GETITEMRECT, fItemRect, rect1);
							OS.SendMessage (handle, OS.TVM_GETITEMRECT, fItemRect, rect2);
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
			int code = callWindowProc (handle, OS.WM_KEYDOWN, wParam, lParam);
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

LRESULT WM_LBUTTONDBLCLK (int wParam, int lParam) {
	TVHITTESTINFO lpht = new TVHITTESTINFO ();
	lpht.x = (short) (lParam & 0xFFFF);
	lpht.y = (short) (lParam >> 16);
	OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
	if ((style & SWT.CHECK) != 0) {
		if ((lpht.flags & OS.TVHT_ONITEMSTATEICON) != 0) {
			sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_LBUTTONDOWN, wParam, lParam);
			sendMouseEvent (SWT.MouseDoubleClick, 1, handle, OS.WM_LBUTTONDBLCLK, wParam, lParam);
			if (OS.GetCapture () != handle) OS.SetCapture (handle);
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
			if (!OS.IsWinCE) {	
				int id = tvItem.hItem;
				if (OS.COMCTL32_MAJOR >= 6) {
					id = OS.SendMessage (handle, OS.TVM_MAPHTREEITEMTOACCID, tvItem.hItem, 0);
				}
				OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, handle, OS.OBJID_CLIENT, id);	
			}
			Event event = new Event ();
			event.item = items [tvItem.lParam];
			event.detail = SWT.CHECK;
			postEvent (SWT.Selection, event);
			return LRESULT.ZERO;
		}
	}
	LRESULT result = super.WM_LBUTTONDBLCLK (wParam, lParam);
	if (lpht.hItem != 0) {
		if ((style & SWT.FULL_SELECTION) != 0 || (lpht.flags & OS.TVHT_ONITEM) != 0) {
			Event event = new Event ();
			TVITEM tvItem = new TVITEM ();
			tvItem.hItem = lpht.hItem;
			tvItem.mask = OS.TVIF_PARAM;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			event.item = items [tvItem.lParam];
			postEvent (SWT.DefaultSelection, event);
		}
	}
	return result;
}

LRESULT WM_LBUTTONDOWN (int wParam, int lParam) {
	/*
	* In a multi-select tree, if the user is collapsing a subtree that
	* contains selected items, clear the selection from these items and
	* issue a selection event.  Only items that are selected and visible
	* are cleared.  This code also runs in the case when no item is selected.
	*/
	TVHITTESTINFO lpht = new TVHITTESTINFO ();
	lpht.x = (short) (lParam & 0xFFFF);
	lpht.y = (short) (lParam >> 16);
	OS.SendMessage (handle, OS.TVM_HITTEST, 0, lpht);
	if (lpht.hItem == 0 || (lpht.flags & OS.TVHT_ONITEMBUTTON) != 0) {
		sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_LBUTTONDOWN, wParam, lParam);
		boolean fixSelection = false, deselected = false;
		if (lpht.hItem != 0 && (style & SWT.MULTI) != 0) {
			int hSelection = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
			if (hSelection != 0) {
				TVITEM tvItem = new TVITEM ();
				tvItem.mask = OS.TVIF_STATE | OS.TVIF_PARAM;
				tvItem.hItem = lpht.hItem;
				OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
				if ((tvItem.state & OS.TVIS_EXPANDED) != 0) {
					fixSelection = true;
					tvItem.stateMask = OS.TVIS_SELECTED;
					int hParent = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PARENT, lpht.hItem);
					int hLast = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_LASTVISIBLE, lpht.hItem);
					int hNext = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, lpht.hItem);
					while (hNext != 0 && hNext != hLast) {
						tvItem.hItem = hNext;
						OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
						if ((tvItem.state & OS.TVIS_SELECTED) != 0) deselected = true;
						tvItem.state = 0;
						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
						if ((hNext = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hNext)) == 0) break;
						if (hParent == OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_PARENT, hNext)) break;
					}
				}
			}
		}
		dragStarted = gestureCompleted = false;
		if (fixSelection) ignoreDeselect = ignoreSelect = lockSelection = true;
		int code = callWindowProc (handle, OS.WM_LBUTTONDOWN, wParam, lParam);
		if (fixSelection) ignoreDeselect = ignoreSelect = lockSelection = false;
		if (dragStarted && OS.GetCapture () != handle) OS.SetCapture (handle);
		if (deselected) {
			TVITEM tvItem = new TVITEM ();
			tvItem.mask = OS.TVIF_PARAM;
			tvItem.hItem = lpht.hItem;
			OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
			Event event = new Event ();
			event.item = items [tvItem.lParam];
			postEvent (SWT.Selection, event);
		}
		return new LRESULT (code);
	}
	
	/* Look for check/uncheck */
	if ((style & SWT.CHECK) != 0) {
		if ((lpht.flags & OS.TVHT_ONITEMSTATEICON) != 0) {
			sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_LBUTTONDOWN, wParam, lParam);
			if (OS.GetCapture () != handle) OS.SetCapture (handle);
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
			if (!OS.IsWinCE) {	
				int id = tvItem.hItem;
				if (OS.COMCTL32_MAJOR >= 6) {
					id = OS.SendMessage (handle, OS.TVM_MAPHTREEITEMTOACCID, tvItem.hItem, 0);
				}
				OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, handle, OS.OBJID_CLIENT, id);	
			}
			Event event = new Event ();
			event.item = items [tvItem.lParam];
			event.detail = SWT.CHECK;
			postEvent (SWT.Selection, event);
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
	sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_LBUTTONDOWN, wParam, lParam);
	dragStarted = gestureCompleted = false;
	ignoreDeselect = ignoreSelect = true;
	int code = callWindowProc (handle, OS.WM_LBUTTONDOWN, wParam, lParam);
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
				int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
				int fItemRect = (bits & OS.TVS_FULLROWSELECT) != 0 ? 0 : 1;
				OS.SendMessage (handle, OS.TVM_GETITEMRECT, fItemRect, rect1);
				OS.SendMessage (handle, OS.TVM_GETITEMRECT, fItemRect, rect2);
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
		sendMouseEvent (SWT.MouseUp, 1, handle, OS.WM_LBUTTONUP, wParam, lParam);
	}
	dragStarted = false;
	return new LRESULT (code);
}

LRESULT WM_MOVE (int wParam, int lParam) {
	if (ignoreResize) return null;
	return super.WM_MOVE (wParam, lParam);
}

LRESULT WM_NOTIFY (int wParam, int lParam) {
	NMHDR hdr = new NMHDR ();
	OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
	if (hwndHeader != 0 && hdr.hwndFrom == hwndHeader) {
		/*
		* Feature in Windows.  On NT, the automatically created
		* header control is created as a UNICODE window, not an
		* ANSI window despite the fact that the parent is created
		* as an ANSI window.  This means that it sends UNICODE
		* notification messages to the parent window on NT for
		* no good reason.  The data and size in the NMHEADER and
		* HDITEM structs is identical between the platforms so no
		* different message is actually necessary.  Despite this,
		* Windows sends different messages.  The fix is to look
		* for both messages, despite the platform.  This works
		* because only one will be sent on either platform, never
		* both.
		*/
		switch (hdr.code) {
			case OS.HDN_BEGINTRACKW:
			case OS.HDN_BEGINTRACKA:
			case OS.HDN_DIVIDERDBLCLICKW:
			case OS.HDN_DIVIDERDBLCLICKA: {
				NMHEADER phdn = new NMHEADER ();
				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
				TreeColumn column = columns [phdn.iItem];
				if (column != null && !column.getResizable ()) {
					return LRESULT.ONE;
				}
				break;
			}
			case OS.HDN_ITEMCHANGINGW:
			case OS.HDN_ITEMCHANGINGA: {
				NMHEADER phdn = new NMHEADER ();
				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
				if (phdn.pitem != 0) {
					HDITEM newItem = new HDITEM ();
					OS.MoveMemory (newItem, phdn.pitem, HDITEM.sizeof);
					if ((newItem.mask & OS.HDI_WIDTH) != 0) {
						HDITEM oldItem = new HDITEM ();
						oldItem.mask = OS.HDI_WIDTH;
						OS.SendMessage (hwndHeader, OS.HDM_GETITEM, phdn.iItem, oldItem);
						int deltaX = newItem.cxy - oldItem.cxy;
						RECT rect = new RECT (), itemRect = new RECT ();
						OS.GetClientRect (handle, rect);
						OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, phdn.iItem, itemRect);
						int gridWidth = getLinesVisible () ? GRID_WIDTH : 0;
						rect.left = itemRect.right - gridWidth;
						int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
						if (phdn.iItem < count - 1) {
							for (int i=phdn.iItem; i<count; i++) {
								OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, i, itemRect);
							}
							rect.right = itemRect.right;
						}
						int flags = OS.SW_INVALIDATE | OS.SW_ERASE;
						OS.ScrollWindowEx (handle, deltaX, 0, rect, null, 0, null, flags);
						//TODO - column flashes when resized
						if (phdn.iItem != 0) {
							OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, phdn.iItem, itemRect);
							rect.left = itemRect.left;
							rect.right = itemRect.right;
							OS.InvalidateRect (handle, rect, true);
						}
						setScrollWidth ();
					}
				}
				break;
			}
			case OS.HDN_ITEMCHANGEDW:
			case OS.HDN_ITEMCHANGEDA: {
				NMHEADER phdn = new NMHEADER ();
				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
				if (phdn.pitem != 0) {
					HDITEM pitem = new HDITEM ();
					OS.MoveMemory (pitem, phdn.pitem, HDITEM.sizeof);
					if ((pitem.mask & OS.HDI_WIDTH) != 0) {
						TreeColumn column = columns [phdn.iItem];
						if (column != null) {
							column.sendEvent (SWT.Resize);
							if (isDisposed ()) return LRESULT.ZERO;	
							int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
							TreeColumn [] newColumns = new TreeColumn [count];
							System.arraycopy (columns, 0, newColumns, 0, count);
							for (int i=phdn.iItem+1; i<count; i++) {
								if (!newColumns [i].isDisposed ()) {
									newColumns [i].sendEvent (SWT.Move);
								}
							}
						}
					}
					setScrollWidth ();
				}
				break;
			}
			case OS.HDN_ITEMCLICKW:
			case OS.HDN_ITEMCLICKA: {
				NMHEADER phdn = new NMHEADER ();
				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
				TreeColumn column = columns [phdn.iItem];
				if (column != null) {
					column.postEvent (SWT.Selection);
				}
				break;
			}
			case OS.HDN_ITEMDBLCLICKW:      
			case OS.HDN_ITEMDBLCLICKA: {
				NMHEADER phdn = new NMHEADER ();
				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
				TreeColumn column = columns [phdn.iItem];
				if (column != null) {
					column.postEvent (SWT.DefaultSelection);
				}
				break;
			}
		}
	}
	return super.WM_NOTIFY (wParam, lParam);
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
	sendMouseEvent (SWT.MouseDown, 3, handle, OS.WM_RBUTTONDOWN, wParam, lParam);
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

LRESULT WM_PRINTCLIENT (int wParam, int lParam) {
	LRESULT result = super.WM_PRINTCLIENT (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  For some reason, when WM_PRINT is used
	* to capture an image of a hierarchy that contains a tree with
	* columns, the clipping that is used to stop the first column
	* from drawing on top of subsequent columns stops the first
	* column and the tree lines from drawing.  This does not happen
	* during WM_PAINT.  The fix is to draw without clipping and
	* then draw the rest of the columns on top.  Since the drawing
	* is happening in WM_PRINTCLIENT, the redrawing is not visible.
	*/
	printClient = true;
	int code = callWindowProc (handle, OS.WM_PRINTCLIENT, wParam, lParam);
	printClient = false;
	return new LRESULT (code);
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

LRESULT WM_SETFONT (int wParam, int lParam) {
	LRESULT result = super.WM_SETFONT (wParam, lParam);
	if (result != null) return result;
	if (hwndHeader != 0) OS.SendMessage (hwndHeader, OS.WM_SETFONT, wParam, lParam);
	return result;
}

LRESULT WM_SIZE (int wParam, int lParam) {
	if (ignoreResize) return null;
	return super.WM_SIZE (wParam, lParam);
}

LRESULT WM_SYSCOLORCHANGE (int wParam, int lParam) {
	LRESULT result = super.WM_SYSCOLORCHANGE (wParam, lParam);
	if (result != null) return result;
	if ((style & SWT.CHECK) != 0) setCheckboxImageList ();
	return result;
}

LRESULT wmColorChild (int wParam, int lParam) {
	/*
	* Feature in Windows.  Tree controls send WM_CTLCOLOREDIT
	* to allow application code to change the default colors.
	* This is undocumented and conflicts with TVM_SETTEXTCOLOR
	* and TVM_SETBKCOLOR, the documented way to do this.  The
	* fix is to ignore WM_CTLCOLOREDIT messages from trees.
	*/
	return null;
}

LRESULT wmNotifyChild (int wParam, int lParam) {
	NMHDR hdr = new NMHDR ();
	OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
	switch (hdr.code) {
		case OS.TVN_GETDISPINFOA:
		case OS.TVN_GETDISPINFOW: {
			NMTVDISPINFO lptvdi = new NMTVDISPINFO ();
			OS.MoveMemory (lptvdi, lParam, NMTVDISPINFO.sizeof);
			/*
			* Feature in Windows.  When a new tree item is inserted
			* using TVM_INSERTITEM, a TVN_GETDISPINFO is sent before
			* TVM_INSERTITEM returns and before the item is added to
			* the items array.  The fix is to check for null.
			* 
			* NOTE: This only happens on XP with the version 6.00 of
			* COMCTL32.DLL,
			*/
			if (items == null) break;
			TreeItem item = items [lptvdi.lParam];
			if (item == null) break;
			if ((lptvdi.mask & OS.TVIF_TEXT) != 0) {
				String string = item.text;
				TCHAR buffer = new TCHAR (getCodePage (), string, false);
				int byteCount = Math.min (buffer.length (), lptvdi.cchTextMax - 1) * TCHAR.sizeof;
				OS.MoveMemory (lptvdi.pszText, buffer, byteCount);
				OS.MoveMemory (lptvdi.pszText + byteCount, new byte [TCHAR.sizeof], TCHAR.sizeof);
				lptvdi.cchTextMax = Math.min (lptvdi.cchTextMax, string.length () + 1);
			}
			if ((lptvdi.mask & (OS.TVIF_IMAGE | OS.TVIF_SELECTEDIMAGE)) != 0) {
				Image image = item.image;
				lptvdi.iImage = lptvdi.iSelectedImage = OS.I_IMAGENONE;
				if (image != null) {
					lptvdi.iImage = lptvdi.iSelectedImage = imageIndex (image);
				}
			}
			OS.MoveMemory (lParam, lptvdi, NMTVDISPINFO.sizeof);
			break;
		}
		case OS.NM_CUSTOMDRAW: {
			if (!customDraw) break;
			NMTVCUSTOMDRAW nmcd = new NMTVCUSTOMDRAW ();
			OS.MoveMemory (nmcd, lParam, NMTVCUSTOMDRAW.sizeof);		
			switch (nmcd.dwDrawStage) {
				case OS.CDDS_PREPAINT: {
					return new LRESULT (OS.CDRF_NOTIFYITEMDRAW | OS.CDRF_NOTIFYPOSTPAINT);
				}
				case OS.CDDS_POSTPAINT: {
					if (linesVisible) {
						int hDC = nmcd.hdc;
						if (hwndHeader != 0) {
							int x = 0;
							RECT rect = new RECT ();
							HDITEM hdItem = new HDITEM ();
							hdItem.mask = OS.HDI_WIDTH;
							int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
							for (int i=0; i<count; i++) {
								OS.SendMessage (hwndHeader, OS.HDM_GETITEM, i, hdItem);
								OS.SetRect (rect, x, nmcd.top, x + hdItem.cxy, nmcd.bottom);
								OS.DrawEdge (hDC, rect, OS.BDR_SUNKENINNER, OS.BF_RIGHT);
								x += hdItem.cxy;
							}
						}
						RECT rect = new RECT ();
						int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_LASTVISIBLE, 0);
						rect.left = hItem;
						if (OS.SendMessage (handle, OS.TVM_GETITEMRECT, 0, rect) != 0) {
							int height = rect.bottom - rect.top;
							while (rect.bottom < nmcd.bottom) {
								int top = rect.top + height;
								OS.SetRect (rect, rect.left, top, rect.right, top + height);
								OS.DrawEdge (hDC, rect, OS.BDR_SUNKENINNER, OS.BF_BOTTOM);
							}
						}
					}
					return new LRESULT (OS.CDRF_DODEFAULT);
				}
				case OS.CDDS_ITEMPREPAINT: {
					/*
					* Feature in Windows.  When a new tree item is inserted
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
					if (nmcd.left >= nmcd.right || nmcd.top >= nmcd.bottom) {
						break;
					}
					int hDC = nmcd.hdc;
					OS.SaveDC (hDC);
					if (linesVisible) {
						RECT rect = new RECT ();
						OS.SetRect (rect, nmcd.left, nmcd.top, nmcd.right, nmcd.bottom);
						OS.DrawEdge (hDC, rect, OS.BDR_SUNKENINNER, OS.BF_BOTTOM);
					}
					if (!printClient && (style & SWT.FULL_SELECTION) == 0) {
						if (hwndHeader != 0) {
							int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
							if (count != 0) {
								HDITEM hdItem = new HDITEM ();
								hdItem.mask = OS.HDI_WIDTH;
								OS.SendMessage (hwndHeader, OS.HDM_GETITEM, 0, hdItem);
								int hRgn = OS.CreateRectRgn (nmcd.left, nmcd.top, nmcd.left + hdItem.cxy, nmcd.bottom);
								OS.SelectClipRgn (hDC, hRgn);
								OS.DeleteObject (hRgn);
							}
						}
					}
					if (item.font == -1 && item.foreground == -1 && item.background == -1) {
						if (item.cellForeground == null && item.cellBackground == null && item.cellFont == null) {
							return new LRESULT (OS.CDRF_DODEFAULT | OS.CDRF_NOTIFYPOSTPAINT);
						}
					}
					int hFont = item.cellFont != null ? item.cellFont [0] : item.font;
					if (hFont != -1) OS.SelectObject (hDC, hFont);
					if (OS.IsWindowEnabled (handle)) {
						TVITEM tvItem = new TVITEM ();
						tvItem.mask = OS.TVIF_STATE;
						tvItem.hItem = item.handle;
						OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
						if ((tvItem.state & (OS.TVIS_SELECTED | OS.TVIS_DROPHILITED)) == 0) {
							int clrText = item.cellForeground != null ? item.cellForeground [0] : item.foreground;
							nmcd.clrText = clrText == -1 ? getForegroundPixel () : clrText;
							int clrTextBk = item.cellBackground != null ? item.cellBackground [0] : item.background;
							nmcd.clrTextBk = clrTextBk == -1 ? getBackgroundPixel () : clrTextBk;
							OS.MoveMemory (lParam, nmcd, NMTVCUSTOMDRAW.sizeof);
						}
					}
					return new LRESULT (OS.CDRF_NEWFONT | OS.CDRF_NOTIFYPOSTPAINT);
				}
				case OS.CDDS_ITEMPOSTPAINT: {
					TreeItem item = items [nmcd.lItemlParam];
					if (item == null) break;
					/*
					* Feature in Windows.  Under certain circumstances, Windows
					* sends CDDS_ITEMPOSTPAINT for an empty rectangle.  This is
					* not a problem providing that graphics do not occur outside
					* the rectangle.  The fix is to test for the rectangle and
					* draw nothing.
					* 
					* NOTE:  This seems to happen when both I_IMAGECALLBACK
					* and LPSTR_TEXTCALLBACK are used at the same time with
					* TVM_SETITEM.
					*/
					if (nmcd.left >= nmcd.right || nmcd.top >= nmcd.bottom) {
						break;
					}
					int hDC = nmcd.hdc;
					OS.RestoreDC (hDC, -1);
					OS.SetBkMode (hDC, OS.TRANSPARENT);
					boolean useColor = OS.IsWindowEnabled (handle);
					if (useColor) {
						if ((style & SWT.FULL_SELECTION) != 0) {
							TVITEM tvItem = new TVITEM ();
							tvItem.mask = OS.TVIF_STATE;
							tvItem.hItem = item.handle;
							OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
							if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
								useColor = false;
							} else {
								/*
								* Feature in Windows.  When the mouse is pressed and the
								* selection is first drawn for a tree, the item is drawn
								* selected, but the TVIS_SELECTED bits for the item are
								* not set.  When the user moves the mouse slightly and
								* a drag and drop operation is not started, the item is
								* drawn again and this time TVIS_SELECTED is set.  This
								* means that an item that is in a tree that has the style
								* TVS_FULLROWSELECT and that also contains colored cells
								* will not draw the entire row selected until the user
								* moves the mouse.  The fix is to test for the selection
								* colors and guess that the item is selected.
								* 
								* NOTE: This code doesn't work when the foreground and
								* background of the tree are set to the selection colors
								* but this does not happen in a regular application.
								*/
								int clrForeground = OS.GetTextColor (hDC);
								if (clrForeground == OS.GetSysColor (OS.COLOR_HIGHLIGHTTEXT)) {
									int clrBackground = OS.GetBkColor (hDC);
									if (clrBackground == OS.GetSysColor (OS.COLOR_HIGHLIGHT)) {
										useColor = false;
									}
								}
							}
						} else {
							OS.SetTextColor (hDC, getForegroundPixel ());
						}
					}
					if (hwndHeader != 0) {
						GCData data = new GCData();
						data.device = display;
						GC gc = GC.win32_new (hDC, data);
						int x = 0, gridWidth = linesVisible ? GRID_WIDTH : 0;
						Point size = null;
						RECT rect = new RECT ();
						HDITEM hdItem = new HDITEM ();
						hdItem.mask = OS.HDI_WIDTH;
						int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
						for (int i=0; i<count; i++) {
							OS.SendMessage (hwndHeader, OS.HDM_GETITEM, i, hdItem);
							if (i > 0) {
								OS.SetRect (rect, x, nmcd.top, x + hdItem.cxy, nmcd.bottom - gridWidth);
								if (printClient || (style & SWT.FULL_SELECTION) != 0) {
									drawBackground (hDC, OS.GetBkColor (hDC), rect);
								}
								if (useColor) {
									int clrTextBk = item.cellBackground != null ? item.cellBackground [i] : item.background;
									if (clrTextBk != -1) drawBackground (hDC, clrTextBk, rect);
								}
								Image image = item.images != null ? item.images [i] : null;
								if (image != null) {
									Rectangle bounds = image.getBounds ();
									if (size == null) size = getImageSize ();
									gc.drawImage (image, 0, 0, bounds.width, bounds.height, rect.left, rect.top, size.x, size.y);
									OS.SetRect (rect, rect.left + size.x + INSET, rect.top, rect.right - INSET, rect.bottom);
								} else {
									OS.SetRect (rect, rect.left + INSET, rect.top, rect.right - INSET, rect.bottom);
								}
								/*
								* Bug in Windows.  When DrawText() is used with DT_VCENTER
								* and DT_ENDELLIPSIS, the ellipsis can draw outside of the
								* rectangle when the rectangle is empty.  The fix is avoid
								* all text drawing for empty rectangles.
								*/
								if (rect.left < rect.right) {
									if (item.strings != null && item.strings [i] != null) {
										int hFont = item.cellFont != null ? item.cellFont [i] : item.font;
										hFont = hFont != -1 ? OS.SelectObject (hDC, hFont) : -1;
										int clrText = -1;
										if (useColor) {
											clrText = item.cellForeground != null ? item.cellForeground [i] : item.foreground;
											clrText = clrText != -1? OS.SetTextColor (hDC, clrText) : -1;
										}
										int flags = OS.DT_NOPREFIX | OS.DT_SINGLELINE | OS.DT_VCENTER | OS.DT_ENDELLIPSIS;
										TreeColumn column = columns [i];
										if ((column.style & SWT.LEFT) != 0) flags |= OS.DT_LEFT;
										if ((column.style & SWT.CENTER) != 0) flags |= OS.DT_CENTER;
										if ((column.style & SWT.RIGHT) != 0) flags |= OS.DT_RIGHT;
										TCHAR buffer = new TCHAR (getCodePage (), item.strings [i], false);
										OS.DrawText (hDC, buffer, buffer.length (), rect, flags);
										if (hFont != -1) OS.SelectObject (hDC, hFont);
										if (clrText != -1) OS.SetTextColor (hDC, clrText);
									}
								}
							}
							x += hdItem.cxy;
						}
						if (count > 0) {
							if (printClient || (style & SWT.FULL_SELECTION) != 0) {
								OS.SetRect (rect, x, nmcd.top, nmcd.right, nmcd.bottom - gridWidth);
								drawBackground (hDC, OS.GetBkColor (hDC), rect);
							}
						}
						gc.dispose ();
					}
					if (linesVisible) {
						if (printClient && (style & SWT.FULL_SELECTION) == 0) {
							if (hwndHeader != 0) {
								int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
								if (count != 0 && printClient) {
									HDITEM hdItem = new HDITEM ();
									hdItem.mask = OS.HDI_WIDTH;
									OS.SendMessage (hwndHeader, OS.HDM_GETITEM, 0, hdItem);
									RECT rect = new RECT ();
									OS.SetRect (rect, nmcd.left + hdItem.cxy, nmcd.top, nmcd.right, nmcd.bottom);
									OS.DrawEdge (hDC, rect, OS.BDR_SUNKENINNER, OS.BF_BOTTOM);
								}
							}
						}
						RECT rect = new RECT ();
						if (OS.COMCTL32_MAJOR < 6 || (style & SWT.FULL_SELECTION) != 0) {
							OS.SetRect (rect, nmcd.left, nmcd.top, nmcd.right, nmcd.bottom);
						} else {
							rect.left = item.handle;
							if (OS.SendMessage (handle, OS.TVM_GETITEMRECT, 1, rect) != 0) {
								int hItem = OS.SendMessage (handle, OS.TVM_GETNEXTITEM, OS.TVGN_CARET, 0);
								if (hItem == item.handle) {
									OS.SetRect (rect, rect.right, nmcd.top, nmcd.right, nmcd.bottom);
								} else {
									TVITEM tvItem = new TVITEM ();
									tvItem.mask = OS.TVIF_STATE;
									tvItem.hItem = item.handle;
									OS.SendMessage (handle, OS.TVM_GETITEM, 0, tvItem);
									if ((tvItem.state & OS.TVIS_SELECTED) != 0) {
										OS.SetRect (rect, rect.right, nmcd.top, nmcd.right, nmcd.bottom);
									} else {
										OS.SetRect (rect, rect.left, nmcd.top, nmcd.right, nmcd.bottom);
									}
								}
							} else {
								rect.left = 0;
							}
						}
						OS.DrawEdge (hDC, rect, OS.BDR_SUNKENINNER, OS.BF_BOTTOM);
					}
					return new LRESULT (OS.CDRF_DODEFAULT);
				}
			}
			break;
		}
		case OS.NM_DBLCLK: {
			if (hooks (SWT.DefaultSelection)) return LRESULT.ONE;
			break;
		}
		case OS.TVN_SELCHANGEDA:
		case OS.TVN_SELCHANGEDW: {
			if ((style & SWT.MULTI) != 0) {
				if (lockSelection) {
					/* Restore the old selection state of both items */
					if (oldSelected) {
						TVITEM tvItem = new TVITEM ();
						int offset = NMHDR.sizeof + 4;
						OS.MoveMemory (tvItem, lParam + offset, TVITEM.sizeof);
						tvItem.mask = OS.TVIF_STATE;
						tvItem.stateMask = OS.TVIS_SELECTED;
						tvItem.state = OS.TVIS_SELECTED;
						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
					}
					if (!newSelected && ignoreSelect) {
						TVITEM tvItem = new TVITEM ();
						int offset = NMHDR.sizeof + 4 + TVITEM.sizeof;
						OS.MoveMemory (tvItem, lParam + offset, TVITEM.sizeof);
						tvItem.mask = OS.TVIF_STATE;
						tvItem.stateMask = OS.TVIS_SELECTED;
						tvItem.state = 0;
						OS.SendMessage (handle, OS.TVM_SETITEM, 0, tvItem);
					}
				}
			}
			if (!ignoreSelect) {
				TVITEM tvItem = new TVITEM ();
				int offset = NMHDR.sizeof + 4 + TVITEM.sizeof;
				OS.MoveMemory (tvItem, lParam + offset, TVITEM.sizeof);
				hAnchor = tvItem.hItem;
				Event event = new Event ();
				event.item = items [tvItem.lParam];
				postEvent (SWT.Selection, event);
			}
			updateScrollBar ();
			break;
		}
		case OS.TVN_SELCHANGINGA:
		case OS.TVN_SELCHANGINGW: {
			if ((style & SWT.MULTI) != 0) {
				if (lockSelection) {
					/* Save the old selection state for both items */
					TVITEM tvItem = new TVITEM ();
					int offset1 = NMHDR.sizeof + 4;
					OS.MoveMemory (tvItem, lParam + offset1, TVITEM.sizeof);
					oldSelected = (tvItem.state & OS.TVIS_SELECTED) != 0;
					int offset2 = NMHDR.sizeof + 4 + TVITEM.sizeof;
					OS.MoveMemory (tvItem, lParam + offset2, TVITEM.sizeof);
					newSelected = (tvItem.state & OS.TVIS_SELECTED) != 0;
				}
			}
			if (!ignoreSelect && !ignoreDeselect) {
				hAnchor = 0;
				if ((style & SWT.MULTI) != 0) deselectAll ();
			}
			break;
		}
		case OS.TVN_ITEMEXPANDEDA:
		case OS.TVN_ITEMEXPANDEDW: {
			/*
			* Bug in Windows.  When TVM_SETINSERTMARK is used to set
			* an insert mark for a tree and an item is expanded or
			* collapsed near the insert mark, the tree does not redraw
			* the insert mark properly.  The fix is to hide and show
			* the insert mark whenever an item is expanded or collapsed.
			*/
			if (hInsert != 0) {
				OS.SendMessage (handle, OS.TVM_SETINSERTMARK, insertAfter ? 1 : 0, hInsert);
			}
			updateScrollBar ();
			break;
		}
		case OS.TVN_ITEMEXPANDINGA:
		case OS.TVN_ITEMEXPANDINGW: {
			/*
			* Bug in Windows.  When TVM_SETINSERTMARK is used to set
			* an insert mark for a tree and an item is expanded or
			* collapsed near the insert mark, the tree does not redraw
			* the insert mark properly.  The fix is to hide and show
			* the insert mark whenever an item is expanded or collapsed.
			*/
			if (hInsert != 0) {
				OS.SendMessage (handle, OS.TVM_SETINSERTMARK, 0, 0);
			}
			if (!ignoreExpand) {
				TVITEM tvItem = new TVITEM ();
				int offset = NMHDR.sizeof + 4 + TVITEM.sizeof;
				OS.MoveMemory (tvItem, lParam + offset, TVITEM.sizeof);
				/*
				* Feature in Windows.  In some cases, TVM_ITEMEXPANDING
				* is sent from within TVM_DELETEITEM for the tree item
				* being destroyed.  By the time the message is sent,
				* the item has already been removed from the list of
				* items.  The fix is to check for null. 
				*/
				if (items == null) break;
				TreeItem item = items [tvItem.lParam];
				if (item == null) break;
				Event event = new Event ();
				event.item = item;
				int [] action = new int [1];
				OS.MoveMemory (action, lParam + NMHDR.sizeof, 4);
				switch (action [0]) {
					case OS.TVE_EXPAND:
						/*
						* Bug in Windows.  When the numeric keypad asterisk
						* key is used to expand every item in the tree, Windows
						* sends TVN_ITEMEXPANDING to items in the tree that
						* have already been expanded.  The fix is to detect
						* that the item is already expanded and ignore the
						* notification.
						*/
						if ((tvItem.state & OS.TVIS_EXPANDED) == 0) {
							sendEvent (SWT.Expand, event);
							if (isDisposed ()) return LRESULT.ZERO;
						}
						break;
					case OS.TVE_COLLAPSE:
						sendEvent (SWT.Collapse, event);
						if (isDisposed ()) return LRESULT.ZERO;
						break;
				}
			}
			break;
		}
		case OS.TVN_BEGINDRAGA:
		case OS.TVN_BEGINDRAGW:
		case OS.TVN_BEGINRDRAGA:
		case OS.TVN_BEGINRDRAGW: {
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
		case OS.NM_RECOGNIZEGESTURE: {
			/* 
			* Feature in Pocket PC.  The tree and table controls detect the tap
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
		}
		case OS.GN_CONTEXTMENU: {
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
	}
	return super.wmNotifyChild (wParam, lParam);
}

}
