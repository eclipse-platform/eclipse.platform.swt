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
	ImageList imageList;
	TableItem currentItem;
	int lastIndexOf, lastWidth;
	boolean customDraw, cancelMove, dragStarted, fixScrollWidth, tipRequested;
	boolean wasSelected, ignoreActivate, ignoreSelect, ignoreShrink, ignoreResize;
	static final int INSET = 4;
	static final int GRID_WIDTH = 1;
	static final int HEADER_MARGIN = 10;
	static final int TableProc;
	static final TCHAR TableClass = new TCHAR (0, OS.WC_LISTVIEW, true);
	static final char [] BUTTON = new char [] {'B', 'U', 'T', 'T', 'O', 'N', 0};
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, TableClass, lpWndClass);
		TableProc = lpWndClass.lpfnWndProc;
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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Table (Composite parent, int style) {
	super (parent, checkStyle (style));
}

TableItem _getItem (int index) {
	if (items [index] != null) return items [index];
	return items [index] = new TableItem (this, SWT.NONE, -1, false);
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
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

int callWindowProc (int hwnd, int msg, int wParam, int lParam) {
	return callWindowProc (hwnd, msg, wParam, lParam, false);
}

int callWindowProc (int hwnd, int msg, int wParam, int lParam, boolean forceSelect) {
	if (handle == 0) return 0;
	boolean checkSelection = false, checkFilter = false, checkActivate = false;
	switch (msg) {
		case OS.WM_CHAR:
		case OS.WM_IME_CHAR:
		case OS.WM_KEYUP:
		case OS.WM_SYSCHAR:
		case OS.WM_SYSKEYDOWN:
		case OS.WM_SYSKEYUP:
		case OS.WM_LBUTTONDBLCLK:
		case OS.WM_LBUTTONUP:
		case OS.WM_MBUTTONDBLCLK:
		case OS.WM_MBUTTONUP:
		case OS.WM_MOUSEHOVER:
		case OS.WM_MOUSELEAVE:
		case OS.WM_MOUSEMOVE:
//		case OS.WM_MOUSEWHEEL:
		case OS.WM_RBUTTONDBLCLK:
		case OS.WM_RBUTTONUP:
		case OS.WM_XBUTTONDBLCLK:
		case OS.WM_XBUTTONUP:
			checkSelection = true;
			break;
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
		case OS.WM_XBUTTONDOWN:
			checkSelection = checkFilter = true;
			break;
		/*
		* Feature in Windows.  Windows sends LVN_ITEMACTIVATE from WM_KEYDOWN
		* instead of WM_CHAR.  This means that application code that expects
		* to consume the key press and therefore avoid a SWT.DefaultSelection
		* event will fail.  The fix is to ignore LVN_ITEMACTIVATE when it is
		* caused by WM_KEYDOWN and send SWT.DefaultSelection from WM_CHAR.
		*/
		case OS.WM_KEYDOWN:
			checkSelection = checkActivate = true;
			break;
	}
	boolean oldSelected = wasSelected;
	if (checkSelection) wasSelected = false;
	if (checkActivate) ignoreActivate = true;
	if (checkFilter) display.ignoreMsgFilter = true;
	int code = OS.CallWindowProc (TableProc, hwnd, msg, wParam, lParam);
	if (checkFilter) display.ignoreMsgFilter = false;
	if (checkActivate) ignoreActivate = false;
	if (checkSelection) {
		if (wasSelected || forceSelect) {
			Event event = new Event ();
			int index = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
			if (index != -1) event.item = _getItem (index);
			postEvent (SWT.Selection, event);
		}
		wasSelected = oldSelected;
	}
	return code;
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
			if (!setScrollWidth (item, false)) {
				item.redraw ();
			}
		}
	}
	return true;
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
	checkWidget ();
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
	TableItem item = items [index];
	if (item != null) {
		if (item != currentItem) item.clear ();
		/*
		* Bug in Windows.  Despite the fact that every item in the
		* table always has LPSTR_TEXTCALLBACK, Windows caches the
		* bounds for the selected items.  This means that 
		* when you change the string to be something else, Windows
		* correctly asks you for the new string but when the item
		* is selected, the selection draws using the bounds of the
		* previous item.  The fix is to reset LPSTR_TEXTCALLBACK
		* even though it has not changed, causing Windows to flush
		* cached bounds.
		*/
		if ((style & SWT.VIRTUAL) == 0 && item.cached) {
			LVITEM lvItem = new LVITEM ();
			lvItem.mask = OS.LVIF_TEXT | OS.LVIF_INDENT;
			lvItem.pszText = OS.LPSTR_TEXTCALLBACK;
			lvItem.iItem = index;
			OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
			item.cached = false;
		}
		if (currentItem == null && drawCount == 0 && OS.IsWindowVisible (handle)) {
			OS.SendMessage (handle, OS.LVM_REDRAWITEMS, index, index);
		}
		setScrollWidth (item, false);
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
	checkWidget ();
	if (start > end) return;
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	if (!(0 <= start && start <= end && end < count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (start == 0 && end == count - 1) {
		clearAll ();
	} else {
		LVITEM lvItem = null;
		boolean cleared = false;
		for (int i=start; i<=end; i++) {
			TableItem item = items [i];
			if (item != null) {
				if (item != currentItem) {
					cleared = true;
					item.clear ();
				}
				/*
				* Bug in Windows.  Despite the fact that every item in the
				* table always has LPSTR_TEXTCALLBACK, Windows caches the
				* bounds for the selected items.  This means that 
				* when you change the string to be something else, Windows
				* correctly asks you for the new string but when the item
				* is selected, the selection draws using the bounds of the
				* previous item.  The fix is to reset LPSTR_TEXTCALLBACK
				* even though it has not changed, causing Windows to flush
				* cached bounds.
				*/
				if ((style & SWT.VIRTUAL) == 0 && item.cached) {
					if (lvItem == null) {
						lvItem = new LVITEM ();
						lvItem.mask = OS.LVIF_TEXT | OS.LVIF_INDENT;
						lvItem.pszText = OS.LPSTR_TEXTCALLBACK;
					}
					lvItem.iItem = i;
					OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
					item.cached = false;
				}
			}
		}
		if (cleared) {
			if (currentItem == null && drawCount == 0 && OS.IsWindowVisible (handle)) {
				OS.SendMessage (handle, OS.LVM_REDRAWITEMS, start, end);
			}
			TableItem item = start == end ? items [start] : null; 
			setScrollWidth (item, false);
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
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (indices.length == 0) return;
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	for (int i=0; i<indices.length; i++) {
		if (!(0 <= indices [i] && indices [i] < count)) {
			error (SWT.ERROR_INVALID_RANGE);
		}
	}
	LVITEM lvItem = null;
	boolean cleared = false;
	for (int i=0; i<indices.length; i++) {
		int index = indices [i];
		TableItem item = items [index];
		if (item != null) {
			if (item != currentItem) {
				cleared = true;
				item.clear ();
			}
			/*
			* Bug in Windows.  Despite the fact that every item in the
			* table always has LPSTR_TEXTCALLBACK, Windows caches the
			* bounds for the selected items.  This means that 
			* when you change the string to be something else, Windows
			* correctly asks you for the new string but when the item
			* is selected, the selection draws using the bounds of the
			* previous item.  The fix is to reset LPSTR_TEXTCALLBACK
			* even though it has not changed, causing Windows to flush
			* cached bounds.
			*/
			if ((style & SWT.VIRTUAL) == 0 && item.cached) {
				if (lvItem == null) {
					lvItem = new LVITEM ();
					lvItem.mask = OS.LVIF_TEXT | OS.LVIF_INDENT;
					lvItem.pszText = OS.LPSTR_TEXTCALLBACK;
				}
				lvItem.iItem = i;
				OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
				item.cached = false;
			}
			if (currentItem == null && drawCount == 0 && OS.IsWindowVisible (handle)) {
				OS.SendMessage (handle, OS.LVM_REDRAWITEMS, index, index);
			}
		}
	}
	if (cleared) setScrollWidth (null, false);
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
	checkWidget ();
	LVITEM lvItem = null;
	boolean cleared = false;
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	for (int i=0; i<count; i++) {
		TableItem item = items [i];
		if (item != null) {
			if (item != currentItem) {
				cleared = true;
				item.clear ();
			}
			/*
			* Bug in Windows.  Despite the fact that every item in the
			* table always has LPSTR_TEXTCALLBACK, Windows caches the
			* bounds for the selected items.  This means that 
			* when you change the string to be something else, Windows
			* correctly asks you for the new string but when the item
			* is selected, the selection draws using the bounds of the
			* previous item.  The fix is to reset LPSTR_TEXTCALLBACK
			* even though it has not changed, causing Windows to flush
			* cached bounds.
			*/
			if ((style & SWT.VIRTUAL) == 0 && item.cached) {
				if (lvItem == null) {
					lvItem = new LVITEM ();
					lvItem.mask = OS.LVIF_TEXT | OS.LVIF_INDENT;
					lvItem.pszText = OS.LPSTR_TEXTCALLBACK;
				}
				lvItem.iItem = i;
				OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
				item.cached = false;
			}
		}
	}
	if (cleared) {
		if (currentItem == null && drawCount == 0 && OS.IsWindowVisible (handle)) {
			OS.SendMessage (handle, OS.LVM_REDRAWITEMS, 0, count - 1);
		}
		setScrollWidth (null, false);
	}
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (fixScrollWidth) setScrollWidth (null, true);
	int bits = 0;
	if (wHint != SWT.DEFAULT) {
		bits |= wHint & 0xFFFF;
	} else {
		int width = 0;
		int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
		int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
		for (int i=0; i<count; i++) {
			width += OS.SendMessage (handle, OS.LVM_GETCOLUMNWIDTH, i, 0);
		}
		bits |= width & 0xFFFF;
	}
	if (hHint != SWT.DEFAULT) bits |= hHint << 16;
	int result = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, -1, bits);
	int width = result & 0xFFFF, height = result >> 16;
	
	/*
	* Feature in Windows.  The height returned by LVM_APPROXIMATEVIEWRECT
	* includes the trim plus the height of the items plus one extra row.
	* The fix is to subtract the height of one row from the result height.
	*/
	int empty = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, 0, 0);
	int oneItem = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, 1, 0);
	height -= (oneItem >> 16) - (empty >> 16);
	
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
	
	/* 
	* This code is intentionally commented.  According to
	* the documentation, setting the default item size is
	* supposed to improve performance.  By experimentation,
	* this does not seem to have much of an effect.
	*/	
//	OS.SendMessage (handle, OS.LVM_SETITEMCOUNT, 1024 * 2, 0);

	/* Set the checkbox image list */
	if ((style & SWT.CHECK) != 0) {
		int empty = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, 0, 0);
		int oneItem = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, 1, 0);
		int width = (oneItem >> 16) - (empty >> 16), height = width;
		setCheckboxImageList (width, height);
		OS.SendMessage (handle, OS. LVM_SETCALLBACKMASK, OS.LVIS_STATEIMAGEMASK, 0);
	}

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

	/*
	* Bug in Windows.  When the first column is inserted
	* without setting the header text, Windows will never
	* allow the header text for the first column to be set.
	* The fix is to set the text to an empty string when
	* the column is inserted.
	*/
	LVCOLUMN lvColumn = new LVCOLUMN ();
	lvColumn.mask = OS.LVCF_TEXT;
	int hHeap = OS.GetProcessHeap ();
	int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, TCHAR.sizeof);
	lvColumn.pszText = pszText;
	OS.SendMessage (handle, OS.LVM_INSERTCOLUMN, 0, lvColumn);
	OS.HeapFree (hHeap, 0, pszText);

	/* Set the extended style bits */
	int bits1 = OS.LVS_EX_SUBITEMIMAGES | OS.LVS_EX_LABELTIP;
	if ((style & SWT.FULL_SELECTION) != 0) bits1 |= OS.LVS_EX_FULLROWSELECT;
	OS.SendMessage (handle, OS.LVM_SETEXTENDEDLISTVIEWSTYLE, bits1, bits1);
	
	/*
	* Feature in Windows.  Windows does not explicitly set the orientation of
	* the header.  Instead, the orientation is inherited when WS_EX_LAYOUTRTL
	* is specified for the table.  This means that when both WS_EX_LAYOUTRTL
	* and WS_EX_NOINHERITLAYOUT are specified for the table, the header will
	* not be oriented correctly.  The fix is to explicitly set the orientation
	* for the header.
	* 
	* NOTE: WS_EX_LAYOUTRTL is not supported on Windows NT.
	*/
	if (OS.WIN32_VERSION < OS.VERSION (4, 10)) return;
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
		int bits2 = OS.GetWindowLong (hwndHeader, OS.GWL_EXSTYLE);
		OS.SetWindowLong (hwndHeader, OS.GWL_EXSTYLE, bits2 | OS.WS_EX_LAYOUTRTL);
	}
}

void createItem (TableColumn column, int index) {
	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	int columnCount = count + 1;
	if (count == 1 && columns [0] == null) count = 0;
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	if (count == columns.length) {
		TableColumn [] newColumns = new TableColumn [columns.length + 4];
		System.arraycopy (columns, 0, newColumns, 0, columns.length);
		columns = newColumns;
	}
	int itemCount = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
		if (item != null) {
			String [] strings = item.strings;
			if (strings != null) {
				String [] temp = new String [columnCount];
				System.arraycopy (strings, 0, temp, 0, index);
				System.arraycopy (strings, index, temp, index+1, columnCount-index-1);
				item.strings = temp;
			}
			Image [] images = item.images;
			if (images != null) {
				Image [] temp = new Image [columnCount];
				System.arraycopy (images, 0, temp, 0, index);
				System.arraycopy (images, index, temp, index+1, columnCount-index-1);
				item.images = temp;
			}
			if (index == 0) {
				if (count != 0) {
					if (strings == null) {
						item.strings = new String [columnCount];
						item.strings [1] = item.text;
					}
					item.text = ""; //$NON-NLS-1$
					if (images == null) {
						item.images = new Image [columnCount];
						item.images [1] = item.image;
					}
					item.image = null;
				}
			}
			if (item.cellBackground != null) {
				int [] cellBackground = item.cellBackground;
				int [] temp = new int [columnCount];
				System.arraycopy (cellBackground, 0, temp, 0, index);
				System.arraycopy (cellBackground, index, temp, index+1, columnCount-index-1);
				temp [index] = -1;
				item.cellBackground = temp;
			}
			if (item.cellForeground != null) {
				int [] cellForeground = item.cellForeground;
				int [] temp = new int [columnCount];
				System.arraycopy (cellForeground, 0, temp, 0, index);
				System.arraycopy (cellForeground, index, temp, index+1, columnCount-index-1);
				temp [index] = -1;
				item.cellForeground = temp;
			}
			if (item.cellFont != null) {
				int [] cellFont = item.cellFont;
				int [] temp = new int [columnCount];
				System.arraycopy (cellFont, 0, temp, 0, index);
				System.arraycopy (cellFont, index, temp, index+1, columnCount-index-1);
				temp [index] = -1;
				item.cellFont = temp;
			}
		}
	}
	/*
	* Insert the column into the columns array before inserting
	* it into the widget so that the column will be present when
	* any callbacks are issued as a result of LVM_INSERTCOLUMN
	* or LVM_SETCOLUMN.
	*/
	System.arraycopy (columns, index, columns, index + 1, count - index);
	columns [index] = column;
	
	/*
	* Ensure that resize listeners for the table and for columns
	* within the table are not called.  This can happen when the
	* first column is inserted into a table or when a new column
	* is inserted in the first position. 
	*/
	ignoreResize = true;
	if (index == 0) {
		if (count > 0) {
			LVCOLUMN lvColumn = new LVCOLUMN ();
			lvColumn.mask = OS.LVCF_WIDTH;
			OS.SendMessage (handle, OS.LVM_INSERTCOLUMN, 1, lvColumn);
			OS.SendMessage (handle, OS.LVM_GETCOLUMN, 1, lvColumn);
			int width = lvColumn.cx;
			int cchTextMax = 1024;
			int hHeap = OS.GetProcessHeap ();
			int byteCount = cchTextMax * TCHAR.sizeof;
			int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
			lvColumn.mask = OS.LVCF_TEXT | OS.LVCF_IMAGE | OS.LVCF_WIDTH | OS.LVCF_FMT;
			lvColumn.pszText = pszText;
			lvColumn.cchTextMax = cchTextMax;
			OS.SendMessage (handle, OS.LVM_GETCOLUMN, 0, lvColumn);
			OS.SendMessage (handle, OS.LVM_SETCOLUMN, 1, lvColumn);
			lvColumn.fmt = OS.LVCFMT_IMAGE;
			lvColumn.cx = width;
			lvColumn.iImage = OS.I_IMAGENONE;
			lvColumn.pszText = lvColumn.cchTextMax = 0;
			OS.SendMessage (handle, OS.LVM_SETCOLUMN, 0, lvColumn);
			lvColumn.mask = OS.LVCF_FMT;
			lvColumn.fmt = OS.LVCFMT_LEFT;
			OS.SendMessage (handle, OS.LVM_SETCOLUMN, 0, lvColumn);
			if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
		} else {
			OS.SendMessage (handle, OS.LVM_SETCOLUMNWIDTH, 0, 0);
		}
		if ((parent.style & SWT.VIRTUAL) == 0) {
			LVITEM lvItem = new LVITEM ();
			lvItem.mask = OS.LVIF_TEXT | OS.LVIF_IMAGE;
			lvItem.pszText = OS.LPSTR_TEXTCALLBACK;
			lvItem.iImage = OS.I_IMAGECALLBACK;
			for (int i=0; i<itemCount; i++) {
				lvItem.iItem = i;
				OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
			}
		}
	} else {
		int fmt = OS.LVCFMT_LEFT;
		if ((column.style & SWT.CENTER) == SWT.CENTER) fmt = OS.LVCFMT_CENTER;
		if ((column.style & SWT.RIGHT) == SWT.RIGHT) fmt = OS.LVCFMT_RIGHT;
		LVCOLUMN lvColumn = new LVCOLUMN ();
		lvColumn.mask = OS.LVCF_WIDTH | OS.LVCF_FMT;
		lvColumn.fmt = fmt;
		OS.SendMessage (handle, OS.LVM_INSERTCOLUMN, index, lvColumn);
	}
	ignoreResize = false;
}

void createItem (TableItem item, int index) {
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	if (count == items.length) {
		/*
		* Grow the array faster when redraw is off or the
		* table is not visible.  When the table is painted,
		* the items array is resized to be smaller to reduce
		* memory usage.
		*/
		boolean small = drawCount == 0 && OS.IsWindowVisible (handle);
		int length = small ? items.length + 4 : Math.max (4, items.length * 3 / 2);
		TableItem [] newItems = new TableItem [length];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_TEXT | OS.LVIF_IMAGE;
	lvItem.iItem = index;
	lvItem.pszText = OS.LPSTR_TEXTCALLBACK;
	/*
	* Bug in Windows.  Despite the fact that the image list
	* index has never been set for the item, Windows always
	* assumes that the image index for the item is valid.
	* When an item is inserted, the image index is zero.
	* Therefore, when the first image is inserted and is
	* assigned image index zero, every item draws with this
	* image.  The fix is to set the image index when the
	* the item is created.
	*/
	lvItem.iImage = OS.I_IMAGECALLBACK;

	/* Insert the item */
	ignoreSelect = true;
	int result = OS.SendMessage (handle, OS.LVM_INSERTITEM, 0, lvItem);
	ignoreSelect = false;
	if (result == -1) error (SWT.ERROR_ITEM_NOT_ADDED);
	System.arraycopy (items, index, items, index + 1, count - index);
	items [index] = item;
}

void createWidget () {
	super.createWidget ();
	items = new TableItem [4];
	columns = new TableColumn [4];
	/*
	* Force virtual tables to use custom draw.  This
	* is necessary to support colors and fonts for table
	* items.  When the application is queried for data,
	* setting the custom draw flag at that time is too
	* late.  The current item is not redrawn in order
	* to avoid recursion and NM_CUSTOMDRAW has already
	* been avoided because at the time of the message,
	* there were no items that required custom drawing.
	*/
	if ((style & SWT.VIRTUAL) != 0) customDraw = true;
}

int defaultBackground () {
	return OS.GetSysColor (OS.COLOR_WINDOW);
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
	LVITEM lvItem = new LVITEM ();
	lvItem.stateMask = OS.LVIS_SELECTED;
	for (int i=0; i<indices.length; i++) {
		/*
		* An index of -1 will apply the change to all
		* items.  Ensure that indices are greater than -1.
		*/
		if (indices [i] >= 0) {
			ignoreSelect = true;
			OS.SendMessage (handle, OS.LVM_SETITEMSTATE, indices [i], lvItem);
			ignoreSelect = false;
		}
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
	/*
	* An index of -1 will apply the change to all
	* items.  Ensure that index is greater than -1.
	*/
	if (index < 0) return;
	LVITEM lvItem = new LVITEM ();
	lvItem.stateMask = OS.LVIS_SELECTED;
	ignoreSelect = true;
	OS.SendMessage (handle, OS.LVM_SETITEMSTATE, index, lvItem);
	ignoreSelect = false;
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
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	if (start == 0 && end == count - 1) {
		deselectAll ();
	} else {
		LVITEM lvItem = new LVITEM ();
		lvItem.stateMask = OS.LVIS_SELECTED;
		/*
		* An index of -1 will apply the change to all
		* items.  Ensure that indices are greater than -1.
		*/
		start = Math.max (0, start);
		for (int i=start; i<=end; i++) {
			ignoreSelect = true;
			OS.SendMessage (handle, OS.LVM_SETITEMSTATE, i, lvItem);
			ignoreSelect = false;
		}
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
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_STATE;
	lvItem.stateMask = OS.LVIS_SELECTED;
	ignoreSelect = true;
	OS.SendMessage (handle, OS.LVM_SETITEMSTATE, -1, lvItem);
	ignoreSelect = false;
}

void destroyItem (TableColumn column) {
	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int columnCount = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	int index = 0;
	while (index < columnCount) {
		if (columns [index] == column) break;
		index++;
	}
	boolean first = false;
	if (index == 0) {
		first = true;
		if (columnCount > 1) {
			index = 1;
			int cchTextMax = 1024;
			int hHeap = OS.GetProcessHeap ();
			int byteCount = cchTextMax * TCHAR.sizeof;
			int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
			LVCOLUMN lvColumn = new LVCOLUMN ();
			lvColumn.mask = OS.LVCF_TEXT | OS.LVCF_WIDTH;
			lvColumn.pszText = pszText;
			lvColumn.cchTextMax = cchTextMax;
			OS.SendMessage (handle, OS.LVM_GETCOLUMN, 1, lvColumn);
			lvColumn.mask |= OS.LVCF_FMT;
			lvColumn.fmt = OS.LVCFMT_LEFT;
			OS.SendMessage (handle, OS.LVM_SETCOLUMN, 0, lvColumn);
			if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
		} else {
			int hHeap = OS.GetProcessHeap ();
			int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, TCHAR.sizeof);
			LVCOLUMN lvColumn = new LVCOLUMN ();
			lvColumn.mask = OS.LVCF_TEXT;
			lvColumn.pszText = pszText;
			OS.SendMessage (handle, OS.LVM_SETCOLUMN, 0, lvColumn);
			if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
		}
		if ((parent.style & SWT.VIRTUAL) == 0) {
			LVITEM lvItem = new LVITEM ();
			lvItem.mask = OS.LVIF_TEXT | OS.LVIF_IMAGE;
			lvItem.pszText = OS.LPSTR_TEXTCALLBACK;
			lvItem.iImage = OS.I_IMAGECALLBACK;
			int itemCount = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
			for (int i=0; i<itemCount; i++) {
				lvItem.iItem = i;
				OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
			}
		}
	}
	if (columnCount > 1) {
		if (OS.SendMessage (handle, OS.LVM_DELETECOLUMN, index, 0) == 0) {
			error (SWT.ERROR_ITEM_NOT_REMOVED);
		}
	}
	if (first) index = 0;
	System.arraycopy (columns, index + 1, columns, index, --columnCount - index);
	columns [columnCount] = null;
	int itemCount = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
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
						item.text = strings [1] != null ? strings [1] : ""; //$NON-NLS-1$
					}
					String [] temp = new String [columnCount];
					System.arraycopy (strings, 0, temp, 0, index);
					System.arraycopy (strings, index + 1, temp, index, columnCount - index);
					item.strings = temp;
				} else {
					if (index == 0) item.text = ""; //$NON-NLS-1$
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
	if (columnCount == 0) setScrollWidth (null, true);
	updateMoveable ();
}

void destroyItem (TableItem item) {
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	int index = 0;
	while (index < count) {
		if (items [index] == item) break;
		index++;
	}
	if (index == count) return;
	ignoreSelect = ignoreShrink = true;
	int code = OS.SendMessage (handle, OS.LVM_DELETEITEM, index, 0);
	ignoreSelect = ignoreShrink = false;
	if (code == 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
	System.arraycopy (items, index + 1, items, index, --count - index);
	items [count] = null;
	if (count == 0) setTableEmpty ();
}

void fixCheckboxImageList () {
	/*
	* Bug in Windows.  When the state image list is larger than the
	* image list, Windows incorrectly positions the state images.  When
	* the table is scrolled, Windows draws garbage.  The fix is to force
	* the state image list to be the same size as the image list.
	*/
	if ((style & SWT.CHECK) == 0) return;
	int hImageList = OS.SendMessage (handle, OS.LVM_GETIMAGELIST, OS.LVSIL_SMALL, 0);
	if (hImageList == 0) return;
	int [] cx = new int [1], cy = new int [1];
	OS.ImageList_GetIconSize (hImageList, cx, cy);
	int hOldStateList = OS.SendMessage (handle, OS.LVM_GETIMAGELIST, OS.LVSIL_STATE, 0);
	if (hOldStateList == 0) return;
	int [] stateCx = new int [1], stateCy = new int [1];
	OS.ImageList_GetIconSize (hOldStateList, stateCx, stateCy);
	if (cx [0] == stateCx [0] && cy [0] == stateCy [0]) return;
	setCheckboxImageList (cx [0], cy [0]);
}

int getBackgroundPixel () {
	return OS.SendMessage (handle, OS.LVM_GETBKCOLOR, 0, 0);
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
	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (count == 1 && columns [0] == null) count = 0;
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
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
	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (count == 1 && columns [0] == null) count = 0;
	return count;
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
	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0 );
	if (count == 1 && columns [0] == null) return new int [0];
	int [] order = new int [count];
	OS.SendMessage (handle, OS.LVM_GETCOLUMNORDERARRAY, count, order);
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
	int hwndHeader =  OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (count == 1 && columns [0] == null) count = 0;
	TableColumn [] result = new TableColumn [count];
	System.arraycopy (columns, 0, result, 0, count);
	return result;
}

/*
* Not currently used.
*/
int getFocusIndex () {
//	checkWidget ();
	return OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
}

int getForegroundPixel () {
	int pixel = OS.SendMessage (handle, OS.LVM_GETTEXTCOLOR, 0, 0);
	/*
	* The Windows table control uses CLR_DEFAULT to indicate
	* that it is using the default foreground color.  This
	* is undocumented.
	*/
	if (pixel == OS.CLR_DEFAULT) return OS.GetSysColor (OS.COLOR_WINDOWTEXT);
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
 * @since 2.0 
 */
public int getHeaderHeight () {
	checkWidget ();
	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
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
 */
public boolean getHeaderVisible () {
	checkWidget ();
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	return (bits & OS.LVS_NOCOLUMNHEADER) == 0;
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
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
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
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	LVHITTESTINFO pinfo = new LVHITTESTINFO ();
	pinfo.x = point.x;  pinfo.y = point.y;
	OS.SendMessage (handle, OS.LVM_HITTEST, 0, pinfo);
	if (pinfo.iItem != -1) return _getItem (pinfo.iItem);
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
	return OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
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
	int empty = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, 0, 0);
	int oneItem = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, 1, 0);
	return (oneItem >> 16) - (empty >> 16);
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
public TableItem [] getItems () {
	checkWidget ();
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	TableItem [] result = new TableItem [count];
	if ((style & SWT.VIRTUAL) != 0) {
		for (int i=0; i<count; i++) {
			result [i] = _getItem (i);
		}
	} else {
		System.arraycopy (items, 0, result, 0, count);
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
	int bits = OS.SendMessage (handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
	return (bits & OS.LVS_EX_GRIDLINES) != 0;
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
public TableItem [] getSelection () {
	checkWidget ();
	int i = -1, j = 0, count = OS.SendMessage (handle, OS.LVM_GETSELECTEDCOUNT, 0, 0);
	TableItem [] result = new TableItem [count];
	while ((i = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, i, OS.LVNI_SELECTED)) != -1) {
		result [j++] = _getItem (i);
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
	return OS.SendMessage (handle, OS.LVM_GETSELECTEDCOUNT, 0, 0);
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
	int focusIndex = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
	int selectedIndex = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_SELECTED);
	if (focusIndex == selectedIndex) return selectedIndex;
	int i = -1;
	while ((i = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, i, OS.LVNI_SELECTED)) != -1) {
		if (i == focusIndex) return i;
	}
	return selectedIndex;
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
	int i = -1, j = 0, count = OS.SendMessage (handle, OS.LVM_GETSELECTEDCOUNT, 0, 0);
	int [] result = new int [count];
	while ((i = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, i, OS.LVNI_SELECTED)) != -1) {
		result [j++] = i;
	}
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
	checkWidget ();
	/*
	* Bug in Windows.  Under rare circumstances, LVM_GETTOPINDEX
	* can return a negative number.  When this happens, the table
	* is displaying blank lines at the top of the controls.  The
	* fix is to check for a negative number and return zero instead.
	*/
	return Math.max (0, OS.SendMessage (handle, OS.LVM_GETTOPINDEX, 0, 0));
}

int imageIndex (Image image) {
	if (image == null) return OS.I_IMAGENONE;
	if (imageList == null) {
		Rectangle bounds = image.getBounds ();
		imageList = display.getImageList (style & SWT.RIGHT_TO_LEFT, bounds.width, bounds.height);
		int index = imageList.indexOf (image);
		if (index == -1) index = imageList.add (image);
		int hImageList = imageList.getHandle ();
		/*
		* Bug in Windows.  Making any change to an item that
		* changes the item height of a table while the table
		* is scrolled can cause the lines to draw incorrectly.
		* This happens even when the lines are not currently
		* visible and are shown afterwards.  The fix is to
		* save the top index, scroll to the top of the table
		* and then restore the original top index.
		*/
		int topIndex = getTopIndex ();
		setRedraw (false);
		setTopIndex (0);
		OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, hImageList);
		setTopIndex (topIndex);
		fixCheckboxImageList ();
		setRedraw (true);
		return index;
	}
	int index = imageList.indexOf (image);
	if (index != -1) return index;
	return imageList.add (image);
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
	int hwndHeader =  OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
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
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	if (1 <= lastIndexOf && lastIndexOf < count - 1) {
		if (items [lastIndexOf] == item) return lastIndexOf;
		if (items [lastIndexOf + 1] == item) return ++lastIndexOf;
		if (items [lastIndexOf - 1] == item) return --lastIndexOf;
	}
	if (lastIndexOf < count / 2) {
		for (int i=0; i<count; i++) {
			if (items [i] == item) return lastIndexOf = i;
		}
	} else {
		for (int i=count - 1; i>=0; --i) {
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
	checkWidget ();
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_STATE;
	lvItem.stateMask = OS.LVIS_SELECTED;
	lvItem.iItem = index;
	int result = OS.SendMessage (handle, OS.LVM_GETITEM, 0, lvItem);
	return (result != 0) && ((lvItem.state & OS.LVIS_SELECTED) != 0);
}

void releaseWidget () {
	int hwndHeader =  OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int columnCount = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (columnCount == 1 && columns [0] == null) columnCount = 0;
	int itemCount = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	/*
	* Feature in Windows 98.  When there are a large number
	* of columns and items in a table (>1000) where each
	* of the subitems in the table has a string, it is much
	* faster to delete each item with LVM_DELETEITEM rather
	* than using LVM_DELETEALLITEMS.  The fix is to detect
	* this case and delete the items, one by one.  The fact
	* that the fix is only necessary on Windows 98 was
	* confirmed using version 5.81 of COMCTL32.DLL on both
	* Windows 98 and NT.
	*
	* NOTE: LVM_DELETEALLITEMS is also sent by the table
	* when the table is destroyed.
	*/	
	if (OS.IsWin95 && columnCount > 1) {
		/* Turn off redraw and leave it off */
		OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
		for (int i=itemCount-1; i>=0; --i) {
			TableItem item = items [i];
			ignoreSelect = ignoreShrink = true;
			OS.SendMessage (handle, OS.LVM_DELETEITEM, i, 0);
			ignoreSelect = ignoreShrink = false;
			if (item != null && !item.isDisposed ()) item.releaseResources ();
		}
	} else {	
		for (int i=0; i<itemCount; i++) {
			TableItem item = items [i];
			if (item != null && !item.isDisposed ()) item.releaseResources ();
		}
	}
	customDraw = false;
	currentItem = null;
	items = null;
	for (int i=0; i<columnCount; i++) {
		TableColumn column = columns [i];
		if (!column.isDisposed ()) column.releaseResources ();
	}
	columns = null;
	if (imageList != null) {
		OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, 0);
		display.releaseImageList (imageList);
	}
	imageList = null;
	int hOldList = OS.SendMessage (handle, OS.LVM_GETIMAGELIST, OS.LVSIL_STATE, 0);
	OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_STATE, 0);
	if (hOldList != 0) OS.ImageList_Destroy (hOldList);
	super.releaseWidget ();
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
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	if (!(0 <= start && start <= end && end < count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int last = -1;
	for (int i=0; i<newIndices.length; i++) {
		int index = newIndices [i];
		if (index != last) {
			TableItem item = items [index];
			ignoreSelect = ignoreShrink = true;
			int code = OS.SendMessage (handle, OS.LVM_DELETEITEM, index, 0);
			ignoreSelect = ignoreShrink = false;
			if (code == 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
			if (item != null && !item.isDisposed ()) item.releaseResources ();
			System.arraycopy (items, index + 1, items, index, --count - index);
			items [count] = null;
			last = index;
		}
	}
	if (count == 0) setTableEmpty ();
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
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
	TableItem item = items [index];
	ignoreSelect = ignoreShrink = true;
	int code = OS.SendMessage (handle, OS.LVM_DELETEITEM, index, 0);
	ignoreSelect = ignoreShrink = false;
	if (code == 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
	if (item != null && !item.isDisposed ()) item.releaseResources ();
	System.arraycopy (items, index + 1, items, index, --count - index);
	items [count] = null;
	if (count == 0) setTableEmpty ();
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
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	if (!(0 <= start && start <= end && end < count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (start == 0 && end == count - 1) {
		removeAll ();
	} else {
		int index = start;
		while (index <= end) {
			TableItem item = items [index];
			ignoreSelect = ignoreShrink = true;
			int code = OS.SendMessage (handle, OS.LVM_DELETEITEM, start, 0);
			ignoreSelect = ignoreShrink = false;
			if (code == 0) break;
			if (item != null && !item.isDisposed ()) item.releaseResources ();
			index++;
		}
		System.arraycopy (items, index, items, start, count - index);
		for (int i=count-(index-start); i<count; i++) items [i] = null;
		if (index <= end) error (SWT.ERROR_ITEM_NOT_REMOVED);
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
	checkWidget ();
	int hwndHeader =  OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int columnCount = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (columnCount == 1 && columns [0] == null) columnCount = 0;
	int itemCount = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	
	/*
	* Feature in Windows 98.  When there are a large number
	* of columns and items in a table (>1000) where each
	* of the subitems in the table has a string, it is much
	* faster to delete each item with LVM_DELETEITEM rather
	* than using LVM_DELETEALLITEMS.  The fix is to detect
	* this case and delete the items, one by one.  The fact
	* that the fix is only necessary on Windows 98 was
	* confirmed using version 5.81 of COMCTL32.DLL on both
	* Windows 98 and NT.
	*
	* NOTE: LVM_DELETEALLITEMS is also sent by the table
	* when the table is destroyed.
	*/	
	if (OS.IsWin95 && columnCount > 1) {
		boolean redraw = drawCount == 0 && OS.IsWindowVisible (handle);
		if (redraw) OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
		int index = itemCount - 1;
		while (index >= 0) {
			TableItem item = items [index];
			ignoreSelect = ignoreShrink = true;
			int code = OS.SendMessage (handle, OS.LVM_DELETEITEM, index, 0);
			ignoreSelect = ignoreShrink = false;
			if (code == 0) break;
			if (item != null && !item.isDisposed ()) item.releaseResources ();
			--index;
		}
		if (redraw) {
			OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
			/*
			* This code is intentionally commented.  The window proc
			* for the table implements WM_SETREDRAW to invalidate
			* and erase the table so it is not necessary to do this
			* again.
			*/
//			int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
//			OS.RedrawWindow (handle, null, 0, flags);
		}
		if (index != -1) error (SWT.ERROR_ITEM_NOT_REMOVED);
	} else {
		ignoreSelect = ignoreShrink = true;
		int code = OS.SendMessage (handle, OS.LVM_DELETEALLITEMS, 0, 0);
		ignoreSelect = ignoreShrink = false;
		if (code == 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
		for (int i=0; i<itemCount; i++) {
			TableItem item = items [i];
			if (item != null && !item.isDisposed ()) item.releaseResources ();
		}
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
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = indices.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	LVITEM lvItem = new LVITEM ();
	lvItem.state = OS.LVIS_SELECTED;
	lvItem.stateMask = OS.LVIS_SELECTED;
	for (int i=length-1; i>=0; --i) {
		/*
		* An index of -1 will apply the change to all
	 	* items.  Ensure that indices are greater than -1.
	 	*/
		if (indices [i] >= 0) {
			ignoreSelect = true;
			OS.SendMessage (handle, OS.LVM_SETITEMSTATE, indices [i], lvItem);
			ignoreSelect = false;
		}
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
	checkWidget ();
	/*
	* An index of -1 will apply the change to all
	* items.  Ensure that index is greater than -1.
	*/
	if (index < 0) return;
	LVITEM lvItem = new LVITEM ();
	lvItem.state = OS.LVIS_SELECTED;
	lvItem.stateMask = OS.LVIS_SELECTED;
	ignoreSelect = true;
	OS.SendMessage (handle, OS.LVM_SETITEMSTATE, index, lvItem);
	ignoreSelect = false;
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
	if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) return;
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	if (count == 0 || start >= count) return;
	start = Math.max (0, start);
	end = Math.min (end, count - 1);
	if (start == 0 && end == count - 1) {
		selectAll ();
	} else {
		/*
		* An index of -1 will apply the change to all
		* items.  Indices must be greater than -1.
		*/
		LVITEM lvItem = new LVITEM ();
		lvItem.state = OS.LVIS_SELECTED;
		lvItem.stateMask = OS.LVIS_SELECTED;
		for (int i=start; i<=end; i++) {
			ignoreSelect = true;
			OS.SendMessage (handle, OS.LVM_SETITEMSTATE, i, lvItem);
			ignoreSelect = false;
		}
	}
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
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_STATE;
	lvItem.state = OS.LVIS_SELECTED;
	lvItem.stateMask = OS.LVIS_SELECTED;
	ignoreSelect = true;
	OS.SendMessage (handle, OS.LVM_SETITEMSTATE, -1, lvItem);
	ignoreSelect = false;
}

LRESULT sendMouseDownEvent (int type, int button, int msg, int wParam, int lParam) {
	/*
	* Feature in Windows.  Inside WM_LBUTTONDOWN and WM_RBUTTONDOWN,
	* the widget starts a modal loop to determine if the user wants
	* to begin a drag/drop operation or marque select.  Unfortunately,
	* this modal loop eats the corresponding mouse up.  The fix is to
	* detect the cases when the modal loop has eaten the mouse up and
	* issue a fake mouse up.
	*
	* By observation, when the mouse is clicked anywhere but the check
	* box, the widget eats the mouse up.  When the mouse is dragged,
	* the widget does not eat the mouse up.
	*/
	LVHITTESTINFO pinfo = new LVHITTESTINFO ();
	pinfo.x = (short) (lParam & 0xFFFF);
	pinfo.y = (short) (lParam >> 16);
	OS.SendMessage (handle, OS.LVM_HITTEST, 0, pinfo);
	sendMouseEvent (type, button, handle, msg, wParam, lParam);

	/*
	* Force the table to have focus so that when the user
	* reselects the focus item, the LVIS_FOCUSED state bits
	* for the item will be set.  If the user did not click on
	* an item, then set focus to the table so that it will
	* come to the front and take focus in the work around
	* below.
	*/
	OS.SetFocus (handle);
		
	/*
	* Feature in Windows.  When the user selects outside of
	* a table item, Windows deselects all the items, even
	* when the table is multi-select.  While not strictly
	* wrong, this is unexpected.  The fix is to detect the
	* case and avoid calling the window proc.
	*/
	if (pinfo.iItem == -1) {
		if (OS.GetCapture () != handle) OS.SetCapture (handle);
		return LRESULT.ZERO;
	}
	
	/*
	* Feature in Windows.  When a table item is reselected
	* in a single-select table, Windows does not issue a
	* WM_NOTIFY because the item state has not changed.
	* This is strictly correct but is inconsistent with the
	* list widget and other widgets in Windows.  The fix is
	* to detect the case when an item is mark it as selected.
	*/
	boolean forceSelect = false;
	int count = OS.SendMessage (handle, OS.LVM_GETSELECTEDCOUNT, 0, 0);
	if (count == 1 && pinfo.iItem != -1) {
		LVITEM lvItem = new LVITEM ();
		lvItem.mask = OS.LVIF_STATE;
		lvItem.stateMask = OS.LVIS_SELECTED;
		lvItem.iItem = pinfo.iItem;
		OS.SendMessage (handle, OS.LVM_GETITEM, 0, lvItem);
		if ((lvItem.state & OS.LVIS_SELECTED) != 0) {
			forceSelect = true;
		}
	}
	dragStarted = false;
	int code = callWindowProc (handle, msg, wParam, lParam, forceSelect);
	if (dragStarted) {
		if (OS.GetCapture () != handle) OS.SetCapture (handle);
	} else {
		int flags = OS.LVHT_ONITEMLABEL | OS.LVHT_ONITEMICON;
		boolean fakeMouseUp = (pinfo.flags & flags) != 0;
		if (!fakeMouseUp && (style & SWT.MULTI) != 0) {
			fakeMouseUp = (pinfo.flags & OS.LVHT_ONITEMSTATEICON) == 0;
		}
		if (fakeMouseUp) {
			sendMouseEvent (SWT.MouseUp, button, handle, msg, wParam, lParam);
		}
	}
	dragStarted = false;
	return new LRESULT (code);
}

void setBackgroundPixel (int pixel) {
	if (background == pixel) return;
	background = pixel;
	if (pixel == -1) pixel = defaultBackground ();
	OS.SendMessage (handle, OS.LVM_SETBKCOLOR, 0, pixel);
	OS.SendMessage (handle, OS.LVM_SETTEXTBKCOLOR, 0, pixel);
	if ((style & SWT.CHECK) != 0) setCheckboxImageListColor ();
	/*
	* Feature in Windows.  When the background color is
	* changed, the table does not redraw until the next
	* WM_PAINT.  The fix is to force a redraw.
	*/
	OS.InvalidateRect (handle, null, true);
}

void setBounds (int x, int y, int width, int height, int flags) {
	/*
	* Bug in Windows.  If the table column widths are adjusted
	* in WM_SIZE or WM_POSITIONCHANGED using LVM_SETCOLUMNWIDTH
	* blank lines may be inserted at the top of the table.  A
	* call to LVM_GETTOPINDEX will return a negative number (this
	* is an impossible result).  Once the blank lines appear,
	* there seems to be no way to get rid of them, other than
	* destroying and recreating the table.  By observation, the
	* problem happens when the height of the table is less than
	* the two times the height of a line (this was tested using
	* different fonts and images).  It also seems that the bug
	* does not occur when the redraw is turned off for the table.
	* The fix is to turn off drawing when resizing a table that
	* is small enough to show the problem. 
	*/
	boolean fixResize = false;
	if ((flags & OS.SWP_NOSIZE) == 0) {
		Rectangle rect = getBounds ();
		fixResize = rect.height < getItemHeight () * 2;
	}
	if (fixResize) setRedraw (false);
	super.setBounds (x, y, width, height, flags);
	if (fixResize) setRedraw (true);
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
	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0 );
	if (count == 1 && columns [0] == null) {
		if (order.length != 0) error (SWT.ERROR_INVALID_ARGUMENT);
		return;
	}
	if (order.length != count) error (SWT.ERROR_INVALID_ARGUMENT);
	int [] oldOrder = new int [count];
	OS.SendMessage (handle, OS.LVM_GETCOLUMNORDERARRAY, count, oldOrder);
	boolean reorder = false;
	boolean [] seen = new boolean [count];
	for (int i=0; i<order.length; i++) {
		int index = order [i];
		if (index < 0 || index >= count) error (SWT.ERROR_INVALID_RANGE);
		if (seen [index]) error (SWT.ERROR_INVALID_ARGUMENT);
		seen [index] = true;
		if (index != oldOrder [i]) reorder = true;
	}
	if (reorder) {
		RECT [] oldRects = new RECT [count];
		for (int i=0; i<count; i++) {
			oldRects [i] = new RECT ();
			OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, i, oldRects [i]);
		}
		OS.SendMessage (handle, OS.LVM_SETCOLUMNORDERARRAY, order.length, order);
		/*
		* Bug in Windows.  When LVM_SETCOLUMNORDERARRAY is used to change
		* the column order, the header redraws correctly but the table does
		* not.  The fix is to force a redraw.
		*/
		OS.InvalidateRect (handle, null, true);
		TableColumn[] newColumns = new TableColumn [count];
		System.arraycopy (columns, 0, newColumns, 0, count);
		RECT newRect = new RECT ();
		for (int i=0; i<count; i++) {
			TableColumn column = newColumns [i];
			if (!column.isDisposed ()) {
				OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, i, newRect);
				if (newRect.left != oldRects [i].left) {
					column.sendEvent (SWT.Move);
				}
			}
		}
	}
}

void setCheckboxImageListColor () {
	if ((style & SWT.CHECK) == 0) return;
	int hOldStateList = OS.SendMessage (handle, OS.LVM_GETIMAGELIST, OS.LVSIL_STATE, 0);
	if (hOldStateList == 0) return;
	int [] cx = new int [1], cy = new int [1];
	OS.ImageList_GetIconSize (hOldStateList, cx, cy);
	setCheckboxImageList (cx [0], cy [0]);
}

void setCheckboxImageList (int width, int height) {
	if ((style & SWT.CHECK) == 0) return;
	int count = 4;
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
	int clrBackground = getBackgroundPixel ();
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
	OS.SetRect (rect, left, top, left + itemWidth, top + itemHeight);
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
	int hOldStateList = OS.SendMessage (handle, OS.LVM_GETIMAGELIST, OS.LVSIL_STATE, 0);
	OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_STATE, hImageList);
	if (hOldStateList != 0) OS.ImageList_Destroy (hOldStateList);
}

void setFocusIndex (int index) {
//	checkWidget ();	
	/*
	* An index of -1 will apply the change to all
	* items.  Ensure that index is greater than -1.
	*/
	if (index < 0) return;
	LVITEM lvItem = new LVITEM ();
	lvItem.state = OS.LVIS_FOCUSED;
	lvItem.stateMask = OS.LVIS_FOCUSED;
	ignoreSelect = true;
	OS.SendMessage (handle, OS.LVM_SETITEMSTATE, index, lvItem);
	ignoreSelect = false;
}

public void setFont (Font font) {
	checkWidget ();
	/*
	* Bug in Windows.  Making any change to an item that
	* changes the item height of a table while the table
	* is scrolled can cause the lines to draw incorrectly.
	* This happens even when the lines are not currently
	* visible and are shown afterwards.  The fix is to
	* save the top index, scroll to the top of the table
	* and then restore the original top index.
	*/
	int topIndex = getTopIndex ();
	setRedraw (false);
	setTopIndex (0);
	super.setFont (font);
	setTopIndex (topIndex);
	setScrollWidth (null, true);
	setRedraw (true);
	
	/*
	* Bug in Windows.  Setting the font will cause the table
	* to be redrawn but not the column headers.  The fix is
	* to force a redraw of the column headers.
	*/
	int hwndHeader =  OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);		 
	OS.InvalidateRect (hwndHeader, null, true);
	int bits = OS.SendMessage (handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
	if ((bits & OS.LVS_EX_GRIDLINES) == 0) return;
	bits = OS.GetWindowLong (handle, OS.GWL_STYLE);	
	if ((bits & OS.LVS_NOCOLUMNHEADER) != 0) return;
	setItemHeight ();
}

void setForegroundPixel (int pixel) {
	if (foreground == pixel) return;
	foreground = pixel;
	/*
	* The Windows table control uses CLR_DEFAULT to indicate
	* that it is using the default foreground color.  This
	* is undocumented.
	*/
	if (pixel == -1) pixel = OS.CLR_DEFAULT;
	OS.SendMessage (handle, OS.LVM_SETTEXTCOLOR, 0, pixel);
		
	/*
	* Feature in Windows.  When the foreground color is
	* changed, the table does not redraw until the next
	* WM_PAINT.  The fix is to force a redraw.
	*/
	OS.InvalidateRect (handle, null, true);
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
	int newBits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	newBits &= ~OS.LVS_NOCOLUMNHEADER;
	if (!show) newBits |= OS.LVS_NOCOLUMNHEADER;
	/*
	* Feature in Windows.  Setting or clearing LVS_NOCOLUMNHEADER
	* causes the table to scroll to the beginning.  The fix is to
	* save and restore the top index causing the table to scroll
	* to the new location.
	*/
	int topIndex = getTopIndex ();
	OS.SetWindowLong (handle, OS.GWL_STYLE, newBits);
	setTopIndex (topIndex);
	if (show) {
		int bits = OS.SendMessage (handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
		if ((bits & OS.LVS_EX_GRIDLINES) != 0) setItemHeight ();
	}
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
	int itemCount = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	if (count == itemCount) return;
	boolean isVirtual = (style & SWT.VIRTUAL) != 0;
	if (!isVirtual) setRedraw (false);
	int index = count;
	while (index < itemCount) {
		TableItem item = items [index];
		if (!isVirtual) {
			ignoreSelect = ignoreShrink = true;
			int code = OS.SendMessage (handle, OS.LVM_DELETEITEM, count, 0);
			ignoreSelect = ignoreShrink = false;
			if (code == 0) break;
		}
		if (item != null && !item.isDisposed ()) item.releaseResources ();
		index++;
	}
	if (index < itemCount) error (SWT.ERROR_ITEM_NOT_REMOVED);
	int length = Math.max (4, (count + 3) / 4 * 4);
	TableItem [] newItems = new TableItem [length];
	System.arraycopy (items, 0, newItems, 0, Math.min (count, itemCount));
	items = newItems;
	if (isVirtual) {
		int flags = OS.LVSICF_NOINVALIDATEALL | OS.LVSICF_NOSCROLL;
		OS.SendMessage (handle, OS.LVM_SETITEMCOUNT, count, flags);
		/*
		* Bug in Windows.  When a virutal table contains items and
		* LVM_SETITEMCOUNT is used to set the new item count to zero,
		* Windows does not redraw the table.  Note that simply not
		* specifying LVSICF_NOINVALIDATEALL or LVSICF_NOSCROLL does
		* correct the problem.  The fix is to force a redraw.
		*/
		if (count == 0 && itemCount != 0) {
			OS.InvalidateRect (handle, null, true);
		}
	} else {
		for (int i=itemCount; i<count; i++) {
			items [i] = new TableItem (this, SWT.NONE, i, true);
		}
	}
	if (!isVirtual) setRedraw (true);
}

void setItemHeight () {
	/*
	* Bug in Windows.  When both a header and grid lines are
	* displayed, the grid lines do not take into account the
	* height of the header and draw in the wrong place.  The
	* fix is to set the height of the table items to be the
	* height of the header so that the lines draw in the right
	* place.  The height of a table item is the maximum of the
	* height of the font or the height of image list.
	*
	* NOTE: In version 5.80 of COMCTL32.DLL, the bug is fixed.
	*/
	if (OS.COMCTL32_VERSION >= OS.VERSION (5, 80)) return;
	int hOldList = OS.SendMessage (handle, OS.LVM_GETIMAGELIST, OS.LVSIL_SMALL, 0);
	if (hOldList != 0) return;
	int hwndHeader =  OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	RECT rect = new RECT ();
	OS.GetWindowRect (hwndHeader, rect);
	int height = rect.bottom - rect.top - 1;
	int hImageList = OS.ImageList_Create (1, height, 0, 0, 0);
	OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, hImageList);
	fixCheckboxImageList ();
	OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, 0);
	OS.ImageList_Destroy (hImageList);
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
	int newBits = 0;
	if (show) {
		newBits = OS.LVS_EX_GRIDLINES;
		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);	
		if ((bits & OS.LVS_NOCOLUMNHEADER) == 0) setItemHeight ();
	}
	OS.SendMessage (handle, OS.LVM_SETEXTENDEDLISTVIEWSTYLE, OS.LVS_EX_GRIDLINES, newBits);
}

public void setRedraw (boolean redraw) {
	checkWidget ();
	/*
	 * Feature in Windows.  When WM_SETREDRAW is used to turn
	 * off drawing in a widget, it clears the WS_VISIBLE bits
	 * and then sets them when redraw is turned back on.  This
	 * means that WM_SETREDRAW will make a widget unexpectedly
	 * visible.  The fix is to track the visibility state while
	 * drawing is turned off and restore it when drawing is turned
	 * back on.
	 */
	if (drawCount == 0) {
		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
		if ((bits & OS.WS_VISIBLE) == 0) state |= HIDDEN;
	}
	if (redraw) {
		if (--drawCount == 0) {
			/*
			* When many items are added to a table, it is faster to
			* temporarily unsubclass the window proc so that messages
			* are dispatched directly to the table.
			*
			* NOTE: This is optimization somewhat dangerous because any
			* operation can occur when redraw is turned off, even operations
			* where the table must be subclassed in order to have the correct
			* behavior or work around a Windows bug.
			* 
			* This code is intentionally commented. 
			*/
//			subclass ();
			
			/* Set the width of the horizontal scroll bar */
			setScrollWidth (null, true);

			OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
			int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);	
			if (hwndHeader != 0) OS.SendMessage (hwndHeader, OS.WM_SETREDRAW, 1, 0);
			if ((state & HIDDEN) != 0) {
				state &= ~HIDDEN;
				OS.ShowWindow (handle, OS.SW_HIDE);
			} else {
				if (OS.IsWinCE) {
					OS.InvalidateRect (handle, null, false);
					if (hwndHeader != 0) {
						OS.InvalidateRect (hwndHeader, null, false);
					}
				} else {
					int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
					OS.RedrawWindow (handle, null, 0, flags);
				}
			}
		}
	} else {
		if (drawCount++ == 0) {
			OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
			int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
			if (hwndHeader != 0) OS.SendMessage (hwndHeader, OS.WM_SETREDRAW, 0, 0);
			
			/*
			* When many items are added to a table, it is faster to
			* temporarily unsubclass the window proc so that messages
			* are dispatched directly to the table.
			*
			* NOTE: This is optimization somewhat dangerous because any
			* operation can occur when redraw is turned off, even operations
			* where the table must be subclassed in order to have the correct
			* behavior or work around a Windows bug.
			*
			* This code is intentionally commented. 
			*/
//			unsubclass ();
		}
	}
}

boolean setScrollWidth (TableItem item, boolean force) {
	if (currentItem != null) {
		if (currentItem != item) fixScrollWidth = true;
		return false;
	}
	if (!force && (drawCount != 0 || !OS.IsWindowVisible (handle))) {
		fixScrollWidth = true;
		return false;
	}
	fixScrollWidth = false;
	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	/*
	* NOTE: It is much faster to measure the strings and compute the
	* width of the scroll bar in non-virtual table rather than using
	* LVM_SETCOLUMNWIDTH with LVSCW_AUTOSIZE.
	*/
	if (count == 1 && columns [0] == null) {
		int newWidth = 0;
		count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
		int index = 0;
		int imageIndent = 0;
		while (index < count) {
			String string = null;
			int font = -1;
			if (item != null) {
				string = item.text;
				imageIndent = Math.max (imageIndent, item.imageIndent);
				if (item.cellFont != null) font = item.cellFont [0];
				if (font == -1) font = item.font;
			} else {
				if (items [index] != null) {
					TableItem tableItem = items [index];
					string = tableItem.text;
					imageIndent = Math.max (imageIndent, tableItem.imageIndent);
					if (tableItem.cellFont != null) font = tableItem.cellFont [0];
					if (font == -1) font = tableItem.font;
				}
			}
			if (string != null && string.length () != 0) {
				if (font != -1) {
					int hDC = OS.GetDC (handle);
					int oldFont = OS.SelectObject (hDC, font);
					int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
					TCHAR buffer = new TCHAR (getCodePage (), string, false);
					RECT rect = new RECT ();
					OS.DrawText (hDC, buffer, buffer.length (), rect, flags);
					OS.SelectObject (hDC, oldFont);
					OS.ReleaseDC (handle, hDC);
					newWidth = Math.max (newWidth, rect.right - rect.left);
				} else {
					TCHAR buffer = new TCHAR (getCodePage (), string, true);
					newWidth = Math.max (newWidth, OS.SendMessage (handle, OS.LVM_GETSTRINGWIDTH, 0, buffer));
				}
			}
			if (item != null) break;
			index++;
		}
		int hStateList = OS.SendMessage (handle, OS.LVM_GETIMAGELIST, OS.LVSIL_STATE, 0);
		if (hStateList != 0) {
			int [] cx = new int [1], cy = new int [1];
			OS.ImageList_GetIconSize (hStateList, cx, cy);
			newWidth += cx [0] + INSET;
		}
		int hImageList = OS.SendMessage (handle, OS.LVM_GETIMAGELIST, OS.LVSIL_SMALL, 0);
		if (hImageList != 0) {
			int [] cx = new int [1], cy = new int [1];
			OS.ImageList_GetIconSize (hImageList, cx, cy);
			newWidth += (imageIndent + 1) * cx [0];
		} else {
			/*
			* Bug in Windows.  When LVM_SETIMAGELIST is used to remove the
			* image list by setting it to NULL, the item width and height
			* is not changed and space is reserved for icons despite the
			* fact that there are none.  The fix is to set the image list
			* to be very small before setting it to NULL.  This causes
			* Windows to reserve the smallest possible space when an image
			* list is removed.  In this case, the scroll width must be one
			* pixel larger.
			*/
			newWidth++;
		}
		newWidth += INSET * 2;
		int oldWidth = OS.SendMessage (handle, OS.LVM_GETCOLUMNWIDTH, 0, 0);
		if (newWidth > oldWidth) {
			OS.SendMessage (handle, OS.LVM_SETCOLUMNWIDTH, 0, newWidth);
			return true;
		}
	}
	return false;
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
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	deselectAll ();
	int length = indices.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	select (indices);
	int focusIndex = indices [0];
	if (focusIndex != -1) setFocusIndex (focusIndex);
	showSelection ();
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
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	deselectAll ();
	int length = items.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	int focusIndex = -1;
	for (int i=length-1; i>=0; --i) {
		int index = indexOf (items [i]);
		if (index != -1) {
			select (focusIndex = index);
		}
	}
	if (focusIndex != -1) setFocusIndex (focusIndex);
	showSelection ();
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
	select (index);
	if (index != -1) setFocusIndex (index);
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
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	if (count == 0 || start >= count) return;
	start = Math.max (0, start);
	end = Math.min (end, count - 1);
	select (start, end);
	setFocusIndex (start);
	showSelection ();
}

void setTableEmpty () {
	if (imageList != null) {
		int hwndHeader =  OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
		int columnCount = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
		if (columnCount == 1 && columns [0] == null) columnCount = 0;
		int i = 0;
		while (i < columnCount) {
			TableColumn column = columns [i];
			if (column.getImage () != null) break;
			i++;
		}
		if (i == columnCount) {
			/*
			* Bug in Windows.  When LVM_SETIMAGELIST is used to remove the
			* image list by setting it to NULL, the item width and height
			* is not changed and space is reserved for icons despite the
			* fact that there are none.  The fix is to set the image list
			* to be very small before setting it to NULL.  This causes
			* Windows to reserve the smallest possible space when an image
			* list is removed.
			*/
			int hImageList = OS.ImageList_Create (1, 1, 0, 0, 0);
			OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, hImageList);
			OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, 0);
			OS.ImageList_Destroy (hImageList);
			display.releaseImageList (imageList);
			imageList = null;
		}
	}
	if ((style & SWT.VIRTUAL) != 0) customDraw = false;
	items = new TableItem [4];
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
	int topIndex = OS.SendMessage (handle, OS.LVM_GETTOPINDEX, 0, 0);
	if (index == topIndex) return;
	
	/*
	* Bug in Windows.  For some reason, LVM_SCROLL refuses to
	* scroll a table vertically when the width and height of
	* the table is smaller than a certain size.  The values
	* that seem to cause the problem are width=68 and height=6
	* but there is no guarantee that these values cause the
	* failure on different machines or on different versions
	* of Windows.  It may depend on the font and any number
	* of other factors.  For example, setting the font to
	* anything but the default sometimes fixes the problem.
	* The fix is to use LVM_GETCOUNTPERPAGE to detect the
	* case when the number of visible items is zero and
	* use LVM_ENSUREVISIBLE to scroll the table to make the
	* index visible.
	*/

	/*
	* Bug in Windows.  When the table header is visible and
	* there is not enough space to show a single table item,
	* LVM_GETCOUNTPERPAGE can return a negative number instead
	* of zero.  The fix is to test for negative or zero.
	*/
	if (OS.SendMessage (handle, OS.LVM_GETCOUNTPERPAGE, 0, 0) <= 0) {
		/*
		* Bug in Windows.  For some reason, LVM_ENSUREVISIBLE can
		* scroll one item more or one item less when there is not
		* enough space to show a single table item.  The fix is
		* to detect the case and call LVM_ENSUREVISIBLE again with
		* the same arguments.  It seems that once LVM_ENSUREVISIBLE
		* has scrolled into the general area, it is able to scroll
		* to the exact item.
		*/
		OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 1);
		if (index != OS.SendMessage (handle, OS.LVM_GETTOPINDEX, 0, 0)) {
			OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 1);
		}
		return;
	}

	/* Use LVM_SCROLL to scroll the table */
	RECT rect = new RECT ();
	rect.left = OS.LVIR_BOUNDS;
	OS.SendMessage (handle, OS.LVM_GETITEMRECT, 0, rect);
	int dy = (index - topIndex) * (rect.bottom - rect.top);
	OS.SendMessage (handle, OS.LVM_SCROLL, 0, dy);
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
	if (column.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (column.parent != this) return;
	int index = indexOf (column);
	if (index == -1) return;
	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (count <= 1 || !(0 <= index && index < count)) return;
	/*
	* Feature in Windows.  Calling LVM_GETSUBITEMRECT with -1 for the
	* row number gives the bounds of the item that would be above the
	* first row in the table.  This is undocumented and does not work
	* for the first column. In this case, to get the bounds of the
	* first column, get the bounds of the second column and subtract
	* the width of the first. The left edge of the second column is
	* also used as the right edge of the first.
	*/
	RECT rect = new RECT ();
	rect.left = OS.LVIR_BOUNDS;
	if (index == 0) {
		rect.top = 1;
		OS.SendMessage (handle, OS.LVM_GETSUBITEMRECT, -1, rect);
		rect.right = rect.left;
		int width = OS.SendMessage (handle, OS.LVM_GETCOLUMNWIDTH, 0, 0);
		rect.left = rect.right - width;
	} else {
		rect.top = index;
		OS.SendMessage (handle, OS.LVM_GETSUBITEMRECT, -1, rect);
	}
	RECT area = new RECT ();
	OS.GetClientRect (handle, area);
	if (rect.left < area.left) {
		int dx = rect.left - area.left;
		OS.SendMessage (handle, OS.LVM_SCROLL, dx, 0);
	} else {
		int width = Math.min (area.right - area.left, rect.right - rect.left);
		if (rect.left + width > area.right) {
			int dx = rect.left + width - area.right;
			OS.SendMessage (handle, OS.LVM_SCROLL, dx, 0);
		}
	}
}

void showItem (int index) {
	/*
	* Bug in Windows.  For some reason, when there is insufficient space
	* to show an item, LVM_ENSUREVISIBLE causes blank lines to be
	* inserted at the top of the widget.  A call to LVM_GETTOPINDEX will
	* return a negative number (this is an impossible result).  The fix 
	* is to use LVM_GETCOUNTPERPAGE to detect the case when the number 
	* of visible items is zero and use LVM_ENSUREVISIBLE with the
	* fPartialOK flag set to true to scroll the table.
	*/
	if (OS.SendMessage (handle, OS.LVM_GETCOUNTPERPAGE, 0, 0) <= 0) {
		/*
		* Bug in Windows.  For some reason, LVM_ENSUREVISIBLE can
		* scroll one item more or one item less when there is not
		* enough space to show a single table item.  The fix is
		* to detect the case and call LVM_ENSUREVISIBLE again with
		* the same arguments.  It seems that once LVM_ENSUREVISIBLE
		* has scrolled into the general area, it is able to scroll
		* to the exact item.
		*/
		OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 1);
		if (index != OS.SendMessage (handle, OS.LVM_GETTOPINDEX, 0, 0)) {
			OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 1);
		}		
	} else {
		OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 0);
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
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	int index = indexOf (item);
	if (index != -1) showItem (index);
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
	int index = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_SELECTED);
	if (index != -1) showItem (index);
}

String toolTipText (NMTTDISPINFO hdr) {
	int hwndToolTip = OS.SendMessage (handle, OS.LVM_GETTOOLTIPS, 0, 0);
	if (hwndToolTip == hdr.hwndFrom && toolTipText != null) return ""; //$NON-NLS-1$
	return super.toolTipText (hdr);
}

void updateMoveable () {
	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (count == 1 && columns [0] == null) count = 0;
	int index = 0;
	while (index < count) {
		if (columns [index].moveable) break;
		index++;
	}
	int newBits = index < count ? OS.LVS_EX_HEADERDRAGDROP : 0;
	OS.SendMessage (handle, OS.LVM_SETEXTENDEDLISTVIEWSTYLE, OS.LVS_EX_HEADERDRAGDROP, newBits);
}

int widgetStyle () {
	int bits = super.widgetStyle () | OS.LVS_SHAREIMAGELISTS;
	if ((style & SWT.HIDE_SELECTION) == 0) bits |= OS.LVS_SHOWSELALWAYS;
	if ((style & SWT.SINGLE) != 0) bits |= OS.LVS_SINGLESEL;
	/*
	* This code is intentionally commented.  In the future,
	* the FLAT bit may be used to make the header flat and
	* unresponsive to mouse clicks.
	*/
//	if ((style & SWT.FLAT) != 0) bits |= OS.LVS_NOSORTHEADER;
	bits |= OS.LVS_REPORT | OS.LVS_NOCOLUMNHEADER;
	if ((style & SWT.VIRTUAL) != 0) bits |= OS.LVS_OWNERDATA;
	return bits;
}

TCHAR windowClass () {
	return TableClass;
}

int windowProc () {
	return TableProc;
}

LRESULT WM_CHAR (int wParam, int lParam) {
	LRESULT result = super.WM_CHAR (wParam, lParam);
	if (result != null) return result;
	switch (wParam) {
		case ' ':
			if ((style & SWT.CHECK) != 0) {
				int index = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
				if (index != -1) {
					TableItem item = _getItem (index);
					item.setChecked (!item.getChecked (), true);
					if (!OS.IsWinCE) {
						OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, handle, OS.OBJID_CLIENT, index + 1);
					}
				}
			}
			/*
			* NOTE: Call the window proc with WM_KEYDOWN rather than WM_CHAR
			* so that the key that was ignored during WM_KEYDOWN is processed.
			* This allows the application to cancel an operation that is normally
			* performed in WM_KEYDOWN from WM_CHAR.
			*/
			int code = callWindowProc (handle, OS.WM_KEYDOWN, wParam, lParam);
			return new LRESULT (code);
		case SWT.CR:
			/*
			* Feature in Windows.  Windows sends LVN_ITEMACTIVATE from WM_KEYDOWN
			* instead of WM_CHAR.  This means that application code that expects
			* to consume the key press and therefore avoid a SWT.DefaultSelection
			* event will fail.  The fix is to ignore LVN_ITEMACTIVATE when it is
			* caused by WM_KEYDOWN and send SWT.DefaultSelection from WM_CHAR.
			*/
			int index = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
			if (index != -1) {
				Event event = new Event ();
				event.item = _getItem (index);
				postEvent (SWT.DefaultSelection, event);
			}
			return LRESULT.ZERO;
	}
	return result;
}

LRESULT WM_ERASEBKGND (int wParam, int lParam) {
	LRESULT result = super.WM_ERASEBKGND (wParam, lParam);
	if (result != null) return result;
	/*
	* This code is intentionally commented.  When a table contains
	* images that are not in the first column, the work around causes
	* pixel corruption.
	*/
//	if (!OS.IsWindowEnabled (handle)) return result;
//	/*
//	* Feature in Windows.  When WM_ERASEBKGND is called,
//	* it clears the damaged area by filling it with the
//	* background color.  During WM_PAINT, when the table
//	* items are drawn, the background for each item is
//	* also drawn, causing flashing.  The fix is to adjust
//	* the damage by subtracting the bounds of each visible
//	* table item.
//	*/
//	int itemCount = getItemCount ();
//	if (itemCount == 0) return result;
//	GCData data = new GCData();
//	data.device = display;
//	GC gc = GC.win32_new (wParam, data);
//	Region region = new Region (display);
//	gc.getClipping (region);
//	int columnCount = Math.max (1, getColumnCount ());
//	Rectangle clientArea = getClientArea ();
//	int i = getTopIndex ();
//	int bottomIndex = i + OS.SendMessage (handle, OS.LVM_GETCOUNTPERPAGE, 0, 0);
//	bottomIndex = Math.min (itemCount, bottomIndex);
//	while (i < bottomIndex) {
//		int j = 0;
//		while (j < columnCount) {
//			if (j != 0 || (!isSelected (i) && i != getFocusIndex ())) {
//				RECT rect = new RECT ();
//				rect.top = j;
//				rect.left = OS.LVIR_LABEL;
//				OS.SendMessage (handle, OS. LVM_GETSUBITEMRECT, i, rect);
//				int width = Math.max (0, rect.right - rect.left);
//				int height = Math.max (0, rect.bottom - rect.top);
//				Rectangle rect2 = new Rectangle (rect.left, rect.top, width, height);
//				if (!rect2.intersects (clientArea)) break;
//				region.subtract (rect2);
//			}
//			j++;
//		}
//		i++;
//	}
//	gc.setClipping (region);
//	drawBackground (wParam);
//	gc.setClipping ((Region) null);
//	region.dispose ();
//	gc.dispose ();
//	return LRESULT.ONE;
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
		case OS.VK_END:
			OS.SendMessage (handle, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
			break;
	}
	return result;
}

LRESULT WM_KILLFOCUS (int wParam, int lParam) {
	LRESULT result = super.WM_KILLFOCUS (wParam, lParam);
	/*
	* Bug in Windows.  When LVS_SHOWSELALWAYS is not specified,
	* Windows hides the selection when focus is lost but does
	* not redraw anything other than the text, leaving the image
	* and check box appearing selected.  The fix is to redraw
	* the table.
	*/
	if ((style & SWT.HIDE_SELECTION) != 0) {
		if (imageList != null || (style & SWT.CHECK) != 0) {
			OS.InvalidateRect (handle, null, false);
		}
	}
	return result;
}

LRESULT WM_LBUTTONDBLCLK (int wParam, int lParam) {

	/*
	* Feature in Windows.  When the user selects outside of
	* a table item, Windows deselects all the items, even
	* when the table is multi-select.  While not strictly
	* wrong, this is unexpected.  The fix is to detect the
	* case and avoid calling the window proc.
	*/
	LVHITTESTINFO pinfo = new LVHITTESTINFO ();
	pinfo.x = (short) (lParam & 0xFFFF);
	pinfo.y = (short) (lParam >> 16);
	int index = OS.SendMessage (handle, OS.LVM_HITTEST, 0, pinfo);
	sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_LBUTTONDOWN, wParam, lParam);
	sendMouseEvent (SWT.MouseDoubleClick, 1, handle, OS.WM_LBUTTONDBLCLK, wParam, lParam);
	if (pinfo.iItem != -1) callWindowProc (handle, OS.WM_LBUTTONDBLCLK, wParam, lParam);
	if (OS.GetCapture () != handle) OS.SetCapture (handle);
	
	/* Look for check/uncheck */
	if ((style & SWT.CHECK) != 0) {
		/*
		* Note that when the table has LVS_EX_FULLROWSELECT and the
		* user clicks anywhere on a row except on the check box, all
		* of the bits are set.  The hit test flags are LVHT_ONITEM.
		* This means that a bit test for LVHT_ONITEMSTATEICON is not
		* the correct way to determine that the user has selected
		* the check box, equality is needed.
		*/
		if (index != -1 && pinfo.flags == OS.LVHT_ONITEMSTATEICON) {
			TableItem item = _getItem (index);
			item.setChecked (!item.getChecked (), true);
			if (!OS.IsWinCE) {
				OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, handle, OS.OBJID_CLIENT, index + 1);
			}
		}	
	}
	return LRESULT.ZERO;
}

LRESULT WM_LBUTTONDOWN (int wParam, int lParam) {
	
	/*
	* Feature in Windows.  For some reason, capturing
	* the mouse after processing the mouse event for the
	* widget interferes with the normal mouse processing
	* for the widget.  The fix is to avoid the automatic
	* mouse capture.
	*/
	LRESULT result = sendMouseDownEvent (SWT.MouseDown, 1, OS.WM_LBUTTONDOWN, wParam, lParam);

	/* Look for check/uncheck */
	if ((style & SWT.CHECK) != 0) {
		LVHITTESTINFO pinfo = new LVHITTESTINFO ();
		pinfo.x = (short) (lParam & 0xFFFF);
		pinfo.y = (short) (lParam >> 16);
		/*
		* Note that when the table has LVS_EX_FULLROWSELECT and the
		* user clicks anywhere on a row except on the check box, all
		* of the bits are set.  The hit test flags are LVHT_ONITEM.
		* This means that a bit test for LVHT_ONITEMSTATEICON is not
		* the correct way to determine that the user has selected
		* the check box, equality is needed.
		*/
		int index = OS.SendMessage (handle, OS.LVM_HITTEST, 0, pinfo);
		if (index != -1 && pinfo.flags == OS.LVHT_ONITEMSTATEICON) {
			TableItem item = _getItem (index);
			item.setChecked (!item.getChecked (), true);
			if (!OS.IsWinCE) {
				OS.NotifyWinEvent (OS.EVENT_OBJECT_FOCUS, handle, OS.OBJID_CLIENT, index + 1);
			}
		}	
	}
	
	return result;
}

LRESULT WM_MOUSEHOVER (int wParam, int lParam) {
	/*
	* Feature in Windows.  Despite the fact that hot
	* tracking is not enabled, the hot tracking code
	* in WM_MOUSEHOVER is executed causing the item
	* under the cursor to be selected.  The fix is to
	* avoid calling the window proc.
	*/
	LRESULT result = super.WM_MOUSEHOVER (wParam, lParam);
	int bits = OS.SendMessage (handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
	int mask = OS.LVS_EX_ONECLICKACTIVATE | OS.LVS_EX_TRACKSELECT | OS.LVS_EX_TWOCLICKACTIVATE;
	if ((bits & mask) != 0) return result;
	return LRESULT.ZERO;
}

LRESULT WM_PAINT (int wParam, int lParam) {
	if (!ignoreShrink) {
		/* Resize the item array to match the item count */
		int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
		if (items.length > 4 && items.length - count > 3) {
			int length = Math.max (4, (count + 3) / 4 * 4);
			TableItem [] newItems = new TableItem [length];
			System.arraycopy (items, 0, newItems, 0, count);
			items = newItems;
		}
	}
	if (fixScrollWidth) setScrollWidth (null, true);
	return super.WM_PAINT (wParam, lParam);
}

LRESULT WM_NOTIFY (int wParam, int lParam) {
	NMHDR hdr = new NMHDR ();
	OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	if (hdr.hwndFrom == hwndHeader) {
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
				int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
				if (count == 1 && columns [0] == null) return LRESULT.ONE;
				NMHEADER phdn = new NMHEADER ();
				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
				TableColumn column = columns [phdn.iItem];
				if (column != null && !column.getResizable ()) {
					return LRESULT.ONE;
				}
				break;
			}
			case OS.NM_RELEASEDCAPTURE:
				cancelMove = false;
				break;
			case OS.HDN_BEGINDRAG: {
				if (cancelMove) return LRESULT.ONE;
				int bits = OS.SendMessage (handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
				if ((bits & OS.LVS_EX_HEADERDRAGDROP) == 0) break; 
				int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
				if (count == 1 && columns [0] == null) return LRESULT.ONE;
				NMHEADER phdn = new NMHEADER ();
				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
				if (phdn.iItem != -1) {
					TableColumn column = columns [phdn.iItem];
					if (column != null && !column.getMoveable ()) {
						cancelMove = true;
						return LRESULT.ONE;
					}
				}
				break;
			}
			case OS.HDN_ENDDRAG: {
				cancelMove = false;
				int bits = OS.SendMessage (handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
				if ((bits & OS.LVS_EX_HEADERDRAGDROP) == 0) break;
				NMHEADER phdn = new NMHEADER ();
				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
				if (phdn.iItem != -1 && phdn.pitem != 0) {
					HDITEM pitem = new HDITEM ();
					OS.MoveMemory (pitem, phdn.pitem, HDITEM.sizeof);
					if ((pitem.mask & OS.HDI_ORDER) != 0 && pitem.iOrder != -1) {
						int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
						if (count == 1 && columns [0] == null) break;
						int [] order = new int [count];
						OS.SendMessage (handle, OS.LVM_GETCOLUMNORDERARRAY, count, order);
						int index = 0;
						while (index < order.length) {
						 	if (order [index] == phdn.iItem) break;
							index++;
						}
						if (index == order.length) index = 0;
						if (index == pitem.iOrder) break;
						int start = Math.min (index, pitem.iOrder);
						int end = Math.max (index, pitem.iOrder);
						for (int i=start; i<=end; i++) {
							TableColumn column = columns [order [i]];
							if (!column.isDisposed ()) {
								column.postEvent (SWT.Move);
							}
						}
					}
				}
				break;
			}
			case OS.HDN_ITEMCHANGEDW:
			case OS.HDN_ITEMCHANGEDA: {
				/*
				* Bug in Windows.  When a table has the LVS_EX_GRIDLINES extended
				* style and the user drags any column over the first column in the
				* table, making the size become zero, when the user drags a column
				* such that the size of the first column becomes non-zero, the grid
				* lines are not redrawn.  The fix is to detect the case and force
				* a redraw of the first column.
				*/
				int width = OS.SendMessage (handle, OS.LVM_GETCOLUMNWIDTH, 0, 0);
				if (lastWidth == 0 && width > 0) {
					int bits = OS.SendMessage (handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
					if ((bits & OS.LVS_EX_GRIDLINES) != 0) {
						RECT rect = new RECT ();
						OS.GetClientRect (handle, rect);
						rect.right = rect.left + width;
						OS.InvalidateRect (handle, rect, true);
					}
				}
				lastWidth = width;
				if (!ignoreResize) {
					NMHEADER phdn = new NMHEADER ();
					OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
					if (phdn.pitem != 0) {
						HDITEM pitem = new HDITEM ();
						OS.MoveMemory (pitem, phdn.pitem, HDITEM.sizeof);
						if ((pitem.mask & OS.HDI_WIDTH) != 0) {
							TableColumn column = columns [phdn.iItem];
							if (column != null) {
								column.sendEvent (SWT.Resize);
								/*
								* It is possible (but unlikely), that application
								* code could have disposed the widget in the resize
								* event.  If this happens, end the processing of the
								* Windows message by returning zero as the result of
								* the window proc.
								*/
								if (isDisposed ()) return LRESULT.ZERO;	
								int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
								if (count == 1 && columns [0] == null) count = 0;
								/*
								* It is possible (but unlikely), that application
								* code could have disposed the column in the move
								* event.  If this happens, process the move event
								* for those columns that have not been destroyed.
								*/
								TableColumn [] newColumns = new TableColumn [count];
								System.arraycopy (columns, 0, newColumns, 0, count);
								int [] order = new int [count];
								OS.SendMessage (handle, OS.LVM_GETCOLUMNORDERARRAY, count, order);
								boolean moved = false;
								for (int i=0; i<count; i++) {
									TableColumn nextColumn = newColumns [order [i]];
									if (moved && !nextColumn.isDisposed ()) {
										nextColumn.sendEvent (SWT.Move);
									}
									if (nextColumn == column) moved = true;
								}
							}
						}
					}
				}
				break;
			}
			case OS.HDN_ITEMDBLCLICKW:      
			case OS.HDN_ITEMDBLCLICKA: {
				NMHEADER phdn = new NMHEADER ();
				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
				TableColumn column = columns [phdn.iItem];
				if (column != null) {
					column.postEvent (SWT.DefaultSelection);
				}
				break;
			}
		}
	}
	LRESULT result = super.WM_NOTIFY (wParam, lParam);
	if (result != null) return result;
	switch (hdr.code) {
		case OS.TTN_GETDISPINFOA:
		case OS.TTN_GETDISPINFOW: {
			tipRequested = true;
			int code = callWindowProc (handle, OS.WM_NOTIFY, wParam, lParam);
			tipRequested = false;
			return new LRESULT (code);
		}
	}
	return result;
}

LRESULT WM_RBUTTONDBLCLK (int wParam, int lParam) {
	/*
	* Feature in Windows.  When the user selects outside of
	* a table item, Windows deselects all the items, even
	* when the table is multi-select.  While not strictly
	* wrong, this is unexpected.  The fix is to detect the
	* case and avoid calling the window proc.
	*/
	LVHITTESTINFO pinfo = new LVHITTESTINFO ();
	pinfo.x = (short) (lParam & 0xFFFF);
	pinfo.y = (short) (lParam >> 16);
	OS.SendMessage (handle, OS.LVM_HITTEST, 0, pinfo);
	sendMouseEvent (SWT.MouseDown, 1, handle, OS.WM_RBUTTONDOWN, wParam, lParam);
	sendMouseEvent (SWT.MouseDoubleClick, 1, handle, OS.WM_RBUTTONDBLCLK, wParam, lParam);
	if (pinfo.iItem != -1) callWindowProc (handle, OS.WM_RBUTTONDBLCLK, wParam, lParam);
	if (OS.GetCapture () != handle) OS.SetCapture (handle);
	return LRESULT.ZERO;
}

LRESULT WM_RBUTTONDOWN (int wParam, int lParam) {
	/*
	* Feature in Windows.  For some reason, capturing
	* the mouse after processing the mouse event for the
	* widget interferes with the normal mouse processing
	* for the widget.  The fix is to avoid the automatic
	* mouse capture.
	*/
	return sendMouseDownEvent (SWT.MouseDown, 3, OS.WM_RBUTTONDOWN, wParam, lParam);
}

LRESULT WM_SETFOCUS (int wParam, int lParam) {
	LRESULT result = super.WM_SETFOCUS (wParam, lParam);
	/*
	* Bug in Windows.  For some reason, the table does
	* not set the default focus rectangle to be the first
	* item in the table when it gets focus and there is
	* no selected item.  The fix to make the first item
	* be the focus item.
	*/
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	if (count == 0) return result;
	int index = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
	if (index == -1) {
		LVITEM lvItem = new LVITEM ();
		lvItem.state = OS.LVIS_FOCUSED;
		lvItem.stateMask = OS.LVIS_FOCUSED;
		ignoreSelect = true;
		OS.SendMessage (handle, OS.LVM_SETITEMSTATE, 0, lvItem);
		ignoreSelect = false;
	}
	return result;
}

LRESULT WM_SYSCOLORCHANGE (int wParam, int lParam) {
	LRESULT result = super.WM_SYSCOLORCHANGE (wParam, lParam);
	if (result != null) return result;
	if (background == -1) {
		int pixel = defaultBackground ();
		OS.SendMessage (handle, OS.LVM_SETBKCOLOR, 0, pixel);
		OS.SendMessage (handle, OS.LVM_SETTEXTBKCOLOR, 0, pixel);
	}
	if ((style & SWT.CHECK) != 0) setCheckboxImageListColor ();
	return result;
}

LRESULT WM_VSCROLL (int wParam, int lParam) {
	LRESULT result = super.WM_VSCROLL (wParam, lParam);
	/*
	* Bug in Windows.  When a table is drawing grid lines and the
	* user scrolls vertically up or down by a line or a page, the
	* table does not redraw the grid lines for newly exposed items.
	* The fix is to invalidate the items.
	*/
	int bits = OS.SendMessage (handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
	if ((bits & OS.LVS_EX_GRIDLINES) != 0) {
		int code = wParam & 0xFFFF;
		switch (code) {
			case OS.SB_ENDSCROLL:
			case OS.SB_THUMBPOSITION:
			case OS.SB_THUMBTRACK:
			case OS.SB_TOP:
			case OS.SB_BOTTOM:
				break;
			case OS.SB_LINEDOWN:
			case OS.SB_LINEUP:
				int headerHeight = 0;
				int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
				if (hwndHeader != 0) {
					RECT rect = new RECT ();					
					OS.GetWindowRect (hwndHeader, rect);
					headerHeight = rect.bottom - rect.top;
				}
				RECT rect = new RECT ();
				OS.GetClientRect (handle, rect);
				rect.top += headerHeight;
				int empty = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, 0, 0);
				int oneItem = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, 1, 0);
				int itemHeight = (oneItem >> 16) - (empty >> 16);
				if (code == OS.SB_LINEDOWN) {
					rect.top = rect.bottom - itemHeight - GRID_WIDTH;
				} else {
					rect.bottom = rect.top + itemHeight + GRID_WIDTH;
				}
				OS.InvalidateRect (handle, rect, true);
				break;
			case OS.SB_PAGEDOWN:
			case OS.SB_PAGEUP:
				OS.InvalidateRect (handle, null, true);
				break;
		}
	}
	return result;
}
	
LRESULT wmNotifyChild (int wParam, int lParam) {
	NMHDR hdr = new NMHDR ();
	OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
	switch (hdr.code) {
		case OS.LVN_ODFINDITEMA:
		case OS.LVN_ODFINDITEMW: {
			if ((style & SWT.VIRTUAL) != 0) {
				NMLVFINDITEM pnmfi = new NMLVFINDITEM ();
				OS.MoveMemory (pnmfi, lParam, NMLVFINDITEM.sizeof);
				int index = Math.max (0, pnmfi.iStart - 1);
				return new LRESULT (index);
			}
			break;
		}
		case OS.LVN_GETDISPINFOA:
		case OS.LVN_GETDISPINFOW: {
//			if (drawCount != 0 || !OS.IsWindowVisible (handle)) break;
			NMLVDISPINFO plvfi = new NMLVDISPINFO ();
			OS.MoveMemory (plvfi, lParam, NMLVDISPINFO.sizeof);
			TableItem item = _getItem (plvfi.iItem);
			/*
			* The cached flag is used by both virtual and non-virtual
			* tables to indicate that Windows has asked at least once
			* for a table item.
			*/
			if (!item.cached) {
				if ((style & SWT.VIRTUAL) != 0) {
					lastIndexOf = plvfi.iItem;
					if (!checkData (item, false)) break;
					TableItem newItem = fixScrollWidth ? null : item;
					if (setScrollWidth (newItem, true)) {
						OS.InvalidateRect (handle, null, true);
					}
				}
				item.cached = true;
			}
			if ((plvfi.mask & OS.LVIF_TEXT) != 0) {
				String string = null;
				if (plvfi.iSubItem == 0) {
					string = item.text;
				} else {
					String [] strings  = item.strings;
					if (strings != null) string = strings [plvfi.iSubItem];
				}
				if (string != null) {
					/*
					* Bug in Windows.  When pszText points to a zero length
					* NULL terminated string, Windows correctly draws the
					* empty string but the cache of the bounds for the item
					* is not reset.  This means that when the text for the
					* item is set and then reset to an empty string, the
					* selection draws using the bounds of the previous text.
					* The fix is to use a space rather than an empty string
					* when anything but a tool tip is requested (to avoid
					* a tool tip that is a single space).
					* 
					* NOTE: This is only a problem for items in the first
					* column.  Assigning NULL to other columns stops Windows
					* from drawing the selection when LVS_EX_FULLROWSELECT
					* is set.
					*/
					if (!tipRequested && string.length () == 0 && plvfi.iSubItem == 0) {
						string = " "; //$NON-NLS-1$
					}
					TCHAR buffer = new TCHAR (getCodePage (), string, false);
					int byteCount = Math.min (buffer.length (), plvfi.cchTextMax - 1) * TCHAR.sizeof;
					OS.MoveMemory (plvfi.pszText, buffer, byteCount);
					OS.MoveMemory (plvfi.pszText + byteCount, new byte [TCHAR.sizeof], TCHAR.sizeof);
					plvfi.cchTextMax = Math.min (plvfi.cchTextMax, string.length () + 1);
				}
			}
			if ((plvfi.mask & OS.LVIF_IMAGE) != 0) {
				Image image = null;
				if (plvfi.iSubItem == 0) {
					image = item.image;
				} else {
					Image [] images = item.images;
					if (images != null) image = images [plvfi.iSubItem];
				}
				if (image != null) plvfi.iImage = imageIndex (image);
			}
			if ((plvfi.mask & OS.LVIF_STATE) != 0) {
				if (plvfi.iSubItem == 0) {
					int state = 1;
					if (item.checked) state++;
					if (item.grayed) state +=2;
					plvfi.state = state << 12;
					plvfi.stateMask = OS.LVIS_STATEIMAGEMASK;
				}
			}
			if ((plvfi.mask & OS.LVIF_INDENT) != 0) {
				if (plvfi.iSubItem == 0) plvfi.iIndent = item.imageIndent;
			}
			OS.MoveMemory (lParam, plvfi, NMLVDISPINFO.sizeof);
			break;
		}
		case OS.NM_CUSTOMDRAW: {
			if (!customDraw) break;
			NMLVCUSTOMDRAW nmcd = new NMLVCUSTOMDRAW ();
			OS.MoveMemory (nmcd, lParam, NMLVCUSTOMDRAW.sizeof);
			switch (nmcd.dwDrawStage) {
				case OS.CDDS_PREPAINT: return new LRESULT (OS.CDRF_NOTIFYITEMDRAW);
				case OS.CDDS_ITEMPREPAINT: return new LRESULT (OS.CDRF_NOTIFYSUBITEMDRAW);
				case OS.CDDS_ITEMPREPAINT | OS.CDDS_SUBITEM: {
					TableItem item = _getItem (nmcd.dwItemSpec);
					int hFont = item.cellFont != null ? item.cellFont [nmcd.iSubItem] : -1;
					if (hFont == -1) hFont = item.font;
					int clrText = item.cellForeground != null ? item.cellForeground [nmcd.iSubItem] : -1;
					if (clrText == -1) clrText = item.foreground;
					int clrTextBk = item.cellBackground != null ? item.cellBackground [nmcd.iSubItem] : -1;
					if (clrTextBk == -1) clrTextBk = item.background;
					/*
					* Feature in Windows.  When the font is set for one cell in a table,
					* Windows does not reset the font for the next cell.  As a result,
					* all subsequent cells are drawn using the new font.  The fix is to
					* reset the font to the default.
					* 
					* NOTE: This does not happen for foreground and background.
					*/
					if (hFont == -1 && clrText == -1 && clrTextBk == -1) {
						if (item.cellForeground == null && item.cellBackground == null && item.cellFont == null) {
							int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
							int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
							if (count == 1) break;
						}
					}
					if (hFont == -1) hFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
					OS.SelectObject (nmcd.hdc, hFont);
					if (OS.IsWindowEnabled (handle)) {
						nmcd.clrText = clrText == -1 ? getForegroundPixel () : clrText;
						nmcd.clrTextBk = clrTextBk == -1 ? getBackgroundPixel () : clrTextBk;
						OS.MoveMemory (lParam, nmcd, NMLVCUSTOMDRAW.sizeof);
					}
					return new LRESULT (OS.CDRF_NEWFONT);
				}
			}
			break;
		}
		case OS.LVN_MARQUEEBEGIN: return LRESULT.ONE;
		case OS.LVN_BEGINDRAG:
		case OS.LVN_BEGINRDRAG: {
			dragStarted = true;
			if (hdr.code == OS.LVN_BEGINDRAG) {
				int pos = OS.GetMessagePos ();
				POINT pt = new POINT ();
				pt.x = (short) (pos & 0xFFFF);
				pt.y = (short) (pos >> 16); 
				OS.ScreenToClient (handle, pt);
				Event event = new Event ();
				event.x = pt.x;
				event.y = pt.y;
				postEvent (SWT.DragDetect, event);
			}
			break;
		}
		case OS.LVN_COLUMNCLICK: {
			NMLISTVIEW pnmlv = new NMLISTVIEW ();
			OS.MoveMemory(pnmlv, lParam, NMLISTVIEW.sizeof);
			TableColumn column = columns [pnmlv.iSubItem];
			if (column != null) {
				column.postEvent (SWT.Selection);
			}
			break;
		}
		case OS.LVN_ITEMACTIVATE: {
			if (ignoreActivate) break;
			NMLISTVIEW pnmlv = new NMLISTVIEW ();
			OS.MoveMemory(pnmlv, lParam, NMLISTVIEW.sizeof);
			if (pnmlv.iItem != -1) {
				Event event = new Event ();
				event.item = _getItem (pnmlv.iItem);
				postEvent (SWT.DefaultSelection, event);
			}
			break;
		}
		case OS.LVN_ITEMCHANGED: {
			if (!ignoreSelect) {
				NMLISTVIEW pnmlv = new NMLISTVIEW ();
				OS.MoveMemory (pnmlv, lParam, NMLISTVIEW.sizeof);
				if ((pnmlv.uChanged & OS.LVIF_STATE) != 0) {
					if (pnmlv.iItem == -1) {
						wasSelected = true;
					} else {
						boolean oldSelected = (pnmlv.uNewState & OS.LVIS_SELECTED) != 0;
						boolean newSelected = (pnmlv.uOldState & OS.LVIS_SELECTED) != 0;
						if (oldSelected != newSelected) wasSelected = true;
					}
				}
			}
			break;
		}
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
					return LRESULT.ONE;
				}
			}
			break;
	}
	return super.wmNotifyChild (wParam, lParam);
}

}
