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
 * Instances of this class represent a selectable user interface
 * object that displays a list of strings and issues notificiation
 * when a string selected.  A list may be single or multi select.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection</dd>
 * </dl>
 * <p>
 * Note: Only one of SINGLE and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */

public class List extends Scrollable {
	static final int ListProc;
	static final TCHAR ListClass = new TCHAR (0, "LISTBOX", true);
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, ListClass, lpWndClass);
		ListProc = lpWndClass.lpfnWndProc;
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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public List (Composite parent, int style) {
	super (parent, checkStyle (style));
}
/**
 * Adds the argument to the end of the receiver's list.
 *
 * @param string the new item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
 * </ul>
 *
 * @see #add(String,int)
 */
public void add (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	int result = OS.SendMessage (handle, OS.LB_ADDSTRING, 0, buffer);
	if (result == OS.LB_ERR) error (SWT.ERROR_ITEM_NOT_ADDED);
	if (result == OS.LB_ERRSPACE) error (SWT.ERROR_ITEM_NOT_ADDED);
	if ((style & SWT.H_SCROLL) != 0) setScrollWidth (buffer, true);
}
/**
 * Adds the argument to the receiver's list at the given
 * zero-relative index.
 * <p>
 * Note: To add an item at the end of the list, use the
 * result of calling <code>getItemCount()</code> as the
 * index or use <code>add(String)</code>.
 * </p>
 *
 * @param string the new item
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
 * </ul>
 *
 * @see #add(String)
 */
public void add (String string, int index) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	int result = OS.SendMessage (handle, OS.LB_INSERTSTRING, index, buffer);
	if (result == OS.LB_ERRSPACE) error (SWT.ERROR_ITEM_NOT_ADDED);
	if (result == OS.LB_ERR) {
		int count = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
		if (0 <= index && index <= count) {
			error (SWT.ERROR_ITEM_NOT_ADDED);
		} else {
			error (SWT.ERROR_INVALID_RANGE);
		}
	}
	if ((style & SWT.H_SCROLL) != 0) setScrollWidth (buffer, true);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the selection changes.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
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
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

int callWindowProc (int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	return OS.CallWindowProc (ListProc, handle, msg, wParam, lParam);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	if (wHint == SWT.DEFAULT) {
		if ((style & SWT.H_SCROLL) != 0) {
			width = OS.SendMessage (handle, OS.LB_GETHORIZONTALEXTENT, 0, 0);
		} else {
			int count = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
			int newFont, oldFont = 0;
			int hDC = OS.GetDC (handle);
			newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
			if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
			RECT rect = new RECT ();
			int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
			int cp = getCodePage ();
			TCHAR buffer = new TCHAR (cp, 64 + 1);
			for (int i=0; i<count; i++) {
				int length = OS.SendMessage (handle, OS.LB_GETTEXTLEN, i, 0);
				if (length != OS.LB_ERR) {
					if (length + 1 > buffer.length ()) {
						buffer = new TCHAR (cp, length + 1);
					}
					int result = OS.SendMessage (handle, OS.LB_GETTEXT, i, buffer);
					if (result != OS.LB_ERR) {
						OS.DrawText (hDC, buffer, length, rect, flags);
						width = Math.max (width, rect.right - rect.left);
					}
				}
			}	
			if (newFont != 0) OS.SelectObject (hDC, oldFont);
			OS.ReleaseDC (handle, hDC);
		}
	}
	if (hHint == SWT.DEFAULT) {
		int count = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
		int itemHeight = OS.SendMessage (handle, OS.LB_GETITEMHEIGHT, 0, 0);
	 	height = count * itemHeight;
	}
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int border = getBorderWidth ();
	width += border * 2 + 3;
	height += border * 2;
	if ((style & SWT.V_SCROLL) != 0) {
		width += OS.GetSystemMetrics (OS.SM_CXVSCROLL);
	}
	if ((style & SWT.H_SCROLL) != 0) {
		height += OS.GetSystemMetrics (OS.SM_CYHSCROLL);
	}
	return new Point (width, height);
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
	if ((style & SWT.SINGLE) != 0) {
		int oldIndex = OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
		if (oldIndex == OS.LB_ERR) return;
		for (int i=0; i<indices.length; i++) {
			if (oldIndex == indices [i]) {
				OS.SendMessage (handle, OS.LB_SETCURSEL, -1, 0);
				return;
			}
		}
		return;
	}
	for (int i=0; i<indices.length; i++) {
		int index = indices [i];
		if (index != -1) {
			OS.SendMessage (handle, OS.LB_SETSEL, 0, index);
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
	if (index == -1) return;
	if ((style & SWT.SINGLE) != 0) {
		int oldIndex = OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
		if (oldIndex == OS.LB_ERR) return;
		if (oldIndex == index) OS.SendMessage (handle, OS.LB_SETCURSEL, -1, 0);
		return;
	} 
	OS.SendMessage (handle, OS.LB_SETSEL, 0, index);
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
	if (start > end) return;
	if ((style & SWT.SINGLE) != 0) {
		int oldIndex = OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
		if (oldIndex == OS.LB_ERR) return;
		if (start <= oldIndex && oldIndex <= end) {
			OS.SendMessage (handle, OS.LB_SETCURSEL, -1, 0);
		}
		return;
	}
	/*
	* Ensure that at least one item is contained in
	* the range from start to end.  Note that when
	* start = end, LB_SELITEMRANGEEX deselects the
	* item.
	*/
	int count = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
	if (start < 0 && end < 0) return;
	if (start >= count && end >= count) return;
	start = Math.min (count - 1, Math.max (0, start));
	end = Math.min (count - 1, Math.max (0, end));
	OS.SendMessage (handle, OS.LB_SELITEMRANGEEX, end, start);
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
	if ((style & SWT.SINGLE) != 0) {
		OS.SendMessage (handle, OS.LB_SETCURSEL, -1, 0);
	} else {
		OS.SendMessage (handle, OS.LB_SETSEL, 0, -1);
	}
}

/**
 * Returns the zero-relative index of the item which currently
 * has the focus in the receiver, or -1 if no item has focus.
 *
 * @return the index of the selected item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getFocusIndex () {
	checkWidget ();
	int result = OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0);
	if (result == 0) {
		int count = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
		if (count == 0) return -1;
	}
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public String getItem (int index) {
	checkWidget ();
	int length = OS.SendMessage (handle, OS.LB_GETTEXTLEN, index, 0);
	if (length != OS.LB_ERR) {
		TCHAR buffer = new TCHAR (getCodePage (), length + 1);
		int result = OS.SendMessage (handle, OS.LB_GETTEXT, index, buffer);
		if (result != OS.LB_ERR) return buffer.toString (0, length);
	}
	int count = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
	if (0 <= index && index < count) {
		error (SWT.ERROR_CANNOT_GET_ITEM);
	} else {
		error (SWT.ERROR_INVALID_RANGE);
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget ();
	int result = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
	if (result == OS.LB_ERR) error (SWT.ERROR_CANNOT_GET_COUNT);
	return result;
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM_HEIGHT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getItemHeight () {
	checkWidget ();
	int result = OS.SendMessage (handle, OS.LB_GETITEMHEIGHT, 0, 0);
	if (result == OS.LB_ERR) error (SWT.ERROR_CANNOT_GET_ITEM_HEIGHT);
	return result;
}

/**
 * Returns an array of <code>String</code>s which are the items
 * in the receiver. 
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the items in the receiver's list
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure while getting an item</li>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure while getting the item count</li>
 * </ul>
 */
public String [] getItems () {
	checkWidget ();
	int count = getItemCount ();
	String [] result = new String [count];
	for (int i=0; i<count; i++) result [i] = getItem (i);
	return result;
}

/**
 * Returns an array of <code>String</code>s that are currently
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_SELECTION - if the operation fails because of an operating system failure while getting the selection</li>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure while getting an item</li>
 * </ul>
 */
public String [] getSelection () {
	checkWidget ();
	int [] indices = getSelectionIndices ();
	String [] result = new String [indices.length];
	for (int i=0; i<indices.length; i++) {
		result [i] = getItem (indices [i]);
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getSelectionCount () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) {
		int result = OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
		if (result == OS.LB_ERR) return 0;
		return 1;
	}
	int result = OS.SendMessage (handle, OS.LB_GETSELCOUNT, 0, 0);
	if (result == OS.LB_ERR) error (SWT.ERROR_CANNOT_GET_COUNT);
	return result;
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_SELECTION - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getSelectionIndex () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) {
		return OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
	}
	int count = OS.SendMessage (handle, OS.LB_GETSELCOUNT, 0, 0);
	if (count == OS.LB_ERR) error (SWT.ERROR_CANNOT_GET_SELECTION);
	if (count == 0) return -1;
	int index = OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0);
	int result = OS.SendMessage (handle, OS.LB_GETSEL, index, 0);
	if (result == OS.LB_ERR) error (SWT.ERROR_CANNOT_GET_SELECTION);
	if (result != 0) return index;
	int [] buffer = new int [1];
	result = OS.SendMessage (handle, OS.LB_GETSELITEMS, 1, buffer);
	if (result != 1) error (SWT.ERROR_CANNOT_GET_SELECTION);
	return buffer [0];
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_SELECTION - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int [] getSelectionIndices () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) {
		int result = OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
		if (result == OS.LB_ERR) return new int [0];
		return new int [] {result};
	}
	int length = OS.SendMessage (handle, OS.LB_GETSELCOUNT, 0, 0);
	if (length == OS.LB_ERR) error (SWT.ERROR_CANNOT_GET_SELECTION);
	int [] indices = new int [length];
	int result = OS.SendMessage (handle, OS.LB_GETSELITEMS, length, indices);
	if (result != length) error (SWT.ERROR_CANNOT_GET_SELECTION);
	return indices;
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
	return OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
}

/**
 * Gets the index of an item.
 * <p>
 * The list is searched starting at 0 until an
 * item is found that is equal to the search item.
 * If no item is found, -1 is returned.  Indexing
 * is zero based.
 *
 * @param string the search item
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
public int indexOf (String string) {
	return indexOf (string, 0);
}

/**
 * Searches the receiver's list starting at the given, 
 * zero-relative index until an item is found that is equal
 * to the argument, and returns the index of that item. If
 * no item is found or the starting index is out of range,
 * returns -1.
 *
 * @param string the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure while getting the item count</li>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure while getting an item</li>
 * </ul>
 */
public int indexOf (String string, int start) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	
	/*
	* Bug in Windows.  For some reason, LB_FINDSTRINGEXACT
	* will not find empty strings even though it is legal
	* to insert an empty string into a list.  The fix is
	* to search the list, an item at a time.
	*/
	if (string.length () == 0) {
		int count = getItemCount ();
		for (int i=start; i<count; i++) {
			if (string.equals (getItem (i))) return i;
		}
		return -1;
	}

	/* Use LB_FINDSTRINGEXACT to search for the item */
	int count = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
	if (!(0 <= start && start < count)) return -1;
	int index = start - 1, last;
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	do {
		index = OS.SendMessage (handle, OS.LB_FINDSTRINGEXACT, last = index, buffer);
		if (index == OS.LB_ERR || index <= last) return -1;
	} while (!string.equals (getItem (index)));
	return index;
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
	int result = OS.SendMessage (handle, OS.LB_GETSEL, index, 0);
	return (result != 0) && (result != OS.LB_ERR);
}

/**
 * Removes the items from the receiver at the given
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
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
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
	int count = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
	if (!(0 <= start && start <= end && end < count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int topIndex = OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
	RECT rect = null;
	int hDC = 0, oldFont = 0, newFont = 0, newWidth = 0;
	if ((style & SWT.H_SCROLL) != 0) {
		rect = new RECT ();
		hDC = OS.GetDC (handle);
		newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	}
	int cp = getCodePage ();
	int i = 0, topCount = 0, last = -1;
	while (i < newIndices.length) {
		int index = newIndices [i];
		if (index != last) {
			TCHAR buffer = null;
			if ((style & SWT.H_SCROLL) != 0) {
				int length = OS.SendMessage (handle, OS.LB_GETTEXTLEN, index, 0);
				if (length == OS.LB_ERR) break;
				buffer = new TCHAR (cp, length + 1);
				int result = OS.SendMessage (handle, OS.LB_GETTEXT, index, buffer);
				if (result == OS.LB_ERR) break;
			}
			int result = OS.SendMessage (handle, OS.LB_DELETESTRING, index, 0);
			if (result == OS.LB_ERR) break;
			if ((style & SWT.H_SCROLL) != 0) {
				int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
				OS.DrawText (hDC, buffer, -1, rect, flags);
				newWidth = Math.max (newWidth, rect.right - rect.left);
			}
			if (index < topIndex) topCount++;
			last = index;
		}
		i++;
	}
	if ((style & SWT.H_SCROLL) != 0) {
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (handle, hDC);
		setScrollWidth (newWidth, false);
	}
	if (topCount > 0) {
		topIndex -= topCount;
		OS.SendMessage (handle, OS.LB_SETTOPINDEX, topIndex, 0);
	}
	if (i < newIndices.length) error (SWT.ERROR_ITEM_NOT_REMOVED);
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
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void remove (int index) {
	checkWidget ();
	TCHAR buffer = null;
	if ((style & SWT.H_SCROLL) != 0) {
		int length = OS.SendMessage (handle, OS.LB_GETTEXTLEN, index, 0);
		if (length == OS.LB_ERR) error (SWT.ERROR_ITEM_NOT_REMOVED);
		buffer = new TCHAR (getCodePage (), length + 1);
		int result = OS.SendMessage (handle, OS.LB_GETTEXT, index, buffer);
		if (result == OS.LB_ERR) error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	int topIndex = OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
	int result = OS.SendMessage (handle, OS.LB_DELETESTRING, index, 0);
	if (result == OS.LB_ERR) {
		int count = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
		if (0 <= index && index < count) error (SWT.ERROR_ITEM_NOT_REMOVED);
		error (SWT.ERROR_INVALID_RANGE);
	}
	if ((style & SWT.H_SCROLL) != 0) setScrollWidth (buffer, false);
	if (index < topIndex) {
		OS.SendMessage (handle, OS.LB_SETTOPINDEX, topIndex - 1, 0);
	}
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
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void remove (int start, int end) {
	checkWidget ();
	if (start > end) return;
	int count = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
	if (!(0 <= start && start <= end && end < count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (start == 0 && end == count - 1) {
		removeAll ();
		return;
	} 
	int topIndex = OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
	RECT rect = null;
	int hDC = 0, oldFont = 0, newFont = 0, newWidth = 0;
	if ((style & SWT.H_SCROLL) != 0) {
		rect = new RECT ();
		hDC = OS.GetDC (handle);
		newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	}
	int cp = getCodePage ();
	int index = start;
	int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
	while (index <= end) {
		TCHAR buffer = null;
		if ((style & SWT.H_SCROLL) != 0) {
			int length = OS.SendMessage (handle, OS.LB_GETTEXTLEN, start, 0);
			if (length == OS.LB_ERR) break;
			buffer = new TCHAR (cp, length + 1);
			int result = OS.SendMessage (handle, OS.LB_GETTEXT, start, buffer);
			if (result == OS.LB_ERR) break;
		}
		int result = OS.SendMessage (handle, OS.LB_DELETESTRING, start, 0);
		if (result == OS.LB_ERR) break;
		if ((style & SWT.H_SCROLL) != 0) {
			OS.DrawText (hDC, buffer, -1, rect, flags);
			newWidth = Math.max (newWidth, rect.right - rect.left);
		}
		index++;
	}
	if ((style & SWT.H_SCROLL) != 0) {
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (handle, hDC);
		setScrollWidth (newWidth, false);
	}
	if (end < topIndex) {
		topIndex -= end - start + 1;
		OS.SendMessage (handle, OS.LB_SETTOPINDEX, topIndex, 0);
	}
	if (index <= end) error (SWT.ERROR_ITEM_NOT_REMOVED);
}

/**
 * Searches the receiver's list starting at the first item
 * until an item is found that is equal to the argument, 
 * and removes that item from the list.
 *
 * @param string the item to remove
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the string is not found in the list</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void remove (String string) {
	checkWidget ();
	int index = indexOf (string, 0);
	if (index == -1) error (SWT.ERROR_INVALID_ARGUMENT);
	remove (index);
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
	OS.SendMessage (handle, OS.LB_RESETCONTENT, 0, 0);
	if ((style & SWT.H_SCROLL) != 0) {
		OS.SendMessage (handle, OS.LB_SETHORIZONTALEXTENT, 0, 0);
	}
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
public void removeSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * If the item at the given zero-relative index in the receiver 
 * is not selected, it is selected.  If the item at the index
 * was selected, it remains selected. Indices that are out
 * of range and duplicate indices are ignored.
 *
 * @param indices the array of indices for the items to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the set of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int [] indices) {
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	select (indices, false);
}

void select (int [] indices, boolean scroll) {
	int i = 0;
	while (i < indices.length) {
		int index = indices [i];
		if (index != -1) {
			select (index, false);
			if ((style & SWT.SINGLE) != 0) {
				int count = getItemCount ();
				if (0 <= index && index < count) {
					break;
				}
			}
		}
		i++;
	}
	if (scroll) showSelection ();
}

/**
 * Selects the item at the given zero-relative index in the receiver's 
 * list.  If the item at the index was already selected, it remains
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
	select (index, false);
}

void select (int index, boolean scroll) {
	if (index == -1) return;
	int count = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
	if (!(0 <= index && index < count)) return;
	if (scroll) {
		if ((style & SWT.SINGLE) != 0) {
			OS.SendMessage (handle, OS.LB_SETCURSEL, index, 0);
		} else {
			OS.SendMessage (handle, OS.LB_SETSEL, 1, index);	
		}		
		return;
	}
	int topIndex = OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
	RECT itemRect = new RECT (), selectedRect = null;
	OS.SendMessage (handle, OS.LB_GETITEMRECT, index, itemRect);
	boolean redraw = drawCount == 0 && OS.IsWindowVisible (handle);
	if (redraw) {
		OS.UpdateWindow (handle);
		OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
	}
	int focusIndex = -1;
	if ((style & SWT.SINGLE) != 0) {
		int oldIndex = OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
		if (oldIndex != -1) {
			selectedRect = new RECT ();
			OS.SendMessage (handle, OS.LB_GETITEMRECT, oldIndex, selectedRect);
		}
		OS.SendMessage (handle, OS.LB_SETCURSEL, index, 0);
	} else {
		focusIndex = OS.SendMessage (handle, OS.LB_GETCARETINDEX, 0, 0);
		OS.SendMessage (handle, OS.LB_SETSEL, 1, index);
	}
	if ((style & SWT.MULTI) != 0) {
		if (focusIndex != -1) {
			OS.SendMessage (handle, OS.LB_SETCARETINDEX, focusIndex, 0);
		}
	}
	OS.SendMessage (handle, OS.LB_SETTOPINDEX, topIndex, 0);
	if (redraw) {
		OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
		OS.ValidateRect (handle, null);
		OS.InvalidateRect (handle, itemRect, true);
		if (selectedRect != null) {
			OS.InvalidateRect (handle, selectedRect, true);
		}
	}
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * If the item at the index was already selected, it remains
 * selected. The range of the indices is inclusive. Indices that are
 * out of range are ignored and no items will be selected if start is
 * greater than end.
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int start, int end) {
	checkWidget ();
	select (start, end, false);
}

void select (int start, int end, boolean scroll) {
	if (start > end) return;
	if ((style & SWT.SINGLE) != 0) {
		int count = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
		int index = Math.min (count - 1, end);
		if (index >= start) select (index, scroll);
		return;
	}
	/*
	* Ensure that at least one item is contained in
	* the range from start to end.  Note that when
	* start = end, LB_SELITEMRANGEEX deselects the
	* item.
	*/
	int count = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
	if (start < 0 && end < 0) return;
	if (start >= count && end >= count) return;
	start = Math.min (count - 1, Math.max (0, start));
	end = Math.min (count - 1, Math.max (0, end));
	if (start == end) {
		select (start, scroll);
		return;
	}
	OS.SendMessage (handle, OS.LB_SELITEMRANGEEX, start, end);
	if (scroll) showSelection ();
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
	OS.SendMessage (handle, OS.LB_SETSEL, 1, -1);
}

void setBounds (int x, int y, int width, int height, int flags) {
	/*
	* Bug in Windows.  If the receiver is scrolled horizontally
	* and is resized, the list does not redraw properly.  The fix
	* is to redraw the control when resizing is not deferred and
	* the new size is different from the previous size.
	*/
	if (parent.lpwp != null || (flags & OS.SWP_NOSIZE) != 0) {	
		super.setBounds (x, y, width, height, flags);
		return;
	}
	RECT rect = new RECT ();
	OS.GetWindowRect (handle, rect);
	int oldWidth = rect.right - rect.left;
	int oldHeight = rect.bottom - rect.top;
	super.setBounds (x, y, width, height, flags);
	if (oldWidth == width && oldHeight == height) return;
	SCROLLINFO info = new SCROLLINFO ();
	info.cbSize = SCROLLINFO.sizeof;
	info.fMask = OS.SIF_POS;
	if (!OS.GetScrollInfo (handle, OS.SB_HORZ, info)) return;
	if (info.nPos != 0) OS.InvalidateRect (handle, null, true);
}

void setFocusIndex (int index) {
//	checkWidget ();	
	int count = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
	if (!(0 <= index && index < count)) return;
	OS.SendMessage (handle, OS.LB_SETCARETINDEX, index, 0);
}

public void setFont (Font font) {
	checkWidget ();
	super.setFont (font);
	if ((style & SWT.H_SCROLL) != 0) setScrollWidth ();
}

/**
 * Sets the text of the item in the receiver's list at the given
 * zero-relative index to the string argument. This is equivalent
 * to <code>remove</code>'ing the old item at the index, and then
 * <code>add</code>'ing the new item at that index.
 *
 * @param index the index for the item
 * @param string the new text for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the remove operation fails because of an operating system failure</li>
 *    <li>ERROR_ITEM_NOT_ADDED - if the add operation fails because of an operating system failure</li>
 * </ul>
 */
public void setItem (int index, String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int topIndex = getTopIndex ();
	boolean isSelected = isSelected (index);
	remove (index);
	add (string, index);
	if (isSelected) select (index, false);
	setTopIndex (topIndex);
}

/**
 * Sets the receiver's items to be the given array of items.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void setItems (String [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	int oldProc = OS.GetWindowLong (handle, OS.GWL_WNDPROC);
	OS.SetWindowLong (handle, OS.GWL_WNDPROC, ListProc);
	boolean redraw = drawCount == 0 && OS.IsWindowVisible (handle);
	if (redraw) {
		OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
	}
	RECT rect = null;
	int hDC = 0, oldFont = 0, newFont = 0, newWidth = 0;
	if ((style & SWT.H_SCROLL) != 0) {
		rect = new RECT ();
		hDC = OS.GetDC (handle);
		newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
		OS.SendMessage (handle, OS.LB_SETHORIZONTALEXTENT, 0, 0);
	}
	int length = items.length;
	OS.SendMessage (handle, OS.LB_RESETCONTENT, 0, 0);
	OS.SendMessage (handle, OS.LB_INITSTORAGE, length, length * 32);
	int index = 0;
	int cp = getCodePage ();
	while (index < length) {
		String string = items [index];
		if (string == null) break;
		TCHAR buffer = new TCHAR (cp, string, true);
		int result = OS.SendMessage (handle, OS.LB_ADDSTRING, 0, buffer);
		if (result == OS.LB_ERR || result == OS.LB_ERRSPACE) break;
		if ((style & SWT.H_SCROLL) != 0) {
			int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
			OS.DrawText (hDC, buffer, buffer.length (), rect, flags);
			newWidth = Math.max (newWidth, rect.right - rect.left);
		}
		index++;
	}
	if ((style & SWT.H_SCROLL) != 0) {
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (handle, hDC);
		OS.SendMessage (handle, OS.LB_SETHORIZONTALEXTENT, newWidth + 3, 0);
	}
	if (redraw) {
		OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
		/*
		* This code is intentionally commented.  The window proc
		* for the list box implements WM_SETREDRAW to invalidate
		* and erase the widget.  This is undocumented behavior.
		* The commented code below shows what is actually happening
		* and reminds us that we are relying on this undocumented
		* behavior.
		*/
//		int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
//		OS.RedrawWindow (handle, null, 0, flags);
	}
	OS.SetWindowLong (handle, OS.GWL_WNDPROC, oldProc);
	if (index < items.length) error (SWT.ERROR_ITEM_NOT_ADDED);
}

void setScrollWidth () {
	int newWidth = 0;
	RECT rect = new RECT ();
	int newFont, oldFont = 0;
	int hDC = OS.GetDC (handle);
	newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	int cp = getCodePage ();
	int count = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
	int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
	for (int i=0; i<count; i++) {
		int length = OS.SendMessage (handle, OS.LB_GETTEXTLEN, i, 0);
		if (length != OS.LB_ERR) {
			TCHAR buffer = new TCHAR (cp, length + 1);
			int result = OS.SendMessage (handle, OS.LB_GETTEXT, i, buffer);
			if (result != OS.LB_ERR) {
				OS.DrawText (hDC, buffer, -1, rect, flags);
				newWidth = Math.max (newWidth, rect.right - rect.left);
			}
		}
	}
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	OS.SendMessage (handle, OS.LB_SETHORIZONTALEXTENT, newWidth + 3, 0);
}

void setScrollWidth (TCHAR buffer, boolean grow) {
	RECT rect = new RECT ();
	int newFont, oldFont = 0;
	int hDC = OS.GetDC (handle);
	newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
	OS.DrawText (hDC, buffer, -1, rect, flags);
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	setScrollWidth (rect.right - rect.left, grow);
}

void setScrollWidth (int newWidth, boolean grow) {
	int width = OS.SendMessage (handle, OS.LB_GETHORIZONTALEXTENT, 0, 0);
	if (grow) {
		if (newWidth <= width) return;
		OS.SendMessage (handle, OS.LB_SETHORIZONTALEXTENT, newWidth + 3, 0);
	} else {
		if (newWidth < width) return;
		setScrollWidth ();
	}
}

/**
 * Selects the items at the given zero-relative indices in the receiver. 
 * The current selection is first cleared, then the new items are selected.
 *
 * @param indices the indices of the items to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the set of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see List#deselectAll()
 * @see List#select(int[])
 */
public void setSelection(int [] indices) {
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	deselectAll ();
	select (indices, true);
	if ((style & SWT.MULTI) != 0) {
		if (indices.length != 0) {
			int focusIndex = indices [0];
			if (focusIndex >= 0) setFocusIndex (focusIndex);
		}
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
 *    <li>ERROR_NULL_ARGUMENT - if the set of items is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see List#deselectAll()
 * @see List#select(int)
 */
public void setSelection (String [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.MULTI) != 0) deselectAll ();
	int focusIndex = -1;
	for (int i=items.length-1; i>=0; --i) {
		String string = items [i];
		int index = 0;
		if (string != null) {
			int localFocus = -1;
			while ((index = indexOf (string, index)) != -1) {
				if (localFocus == -1) localFocus = index;
				select (index, false);
				if ((style & SWT.SINGLE) != 0 && isSelected (index)) {
					/*
					* Return and rely on the fact that select ()
					* for single-select lists clears the previous
					* selection.
					*/
					showSelection ();
					return;
				}
				index++;
			}
			if (localFocus != -1) focusIndex = localFocus;
		}
	}
	if ((style & SWT.SINGLE) != 0) deselectAll ();
	if ((style & SWT.MULTI) != 0) {
		if (focusIndex >= 0) setFocusIndex (focusIndex);
	}
}

/**
 * Selects the item at the given zero-relative index in the receiver. 
 * If the item at the index was already selected, it remains selected.
 * The current selected is first cleared, then the new items are selected.
 * Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @see List#deselectAll()
 * @see List#select(int)
 */
public void setSelection (int index) {
	checkWidget ();
	if ((style & SWT.MULTI) != 0) deselectAll ();
	select (index, true);
	if ((style & SWT.MULTI) != 0) {
		if (index >= 0) setFocusIndex (index);
	}
}

/**
 * Selects the items at the given zero-relative indices in the receiver. 
 * The current selection is first cleared, then the new items are selected.
 * Indices that are out of range are ignored and no items will be selected
 * if start is greater than end.
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
	if ((style & SWT.MULTI) != 0) deselectAll ();
	select (start, end, true);
	if ((style & SWT.MULTI) != 0) {
		if (0 <= start && start <= end) setFocusIndex (start);
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
	checkWidget ();
	int result = OS.SendMessage (handle, OS.LB_SETTOPINDEX, index, 0);
	if (result == OS.LB_ERR) {
		int count = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
		index = Math.min (count - 1, Math.max (0, index));
		OS.SendMessage (handle, OS.LB_SETTOPINDEX, index, 0);
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
 */
public void showSelection () {
	checkWidget ();
	int index;
	if ((style & SWT.SINGLE) != 0) {
		index = OS.SendMessage (handle, OS.LB_GETCURSEL, 0, 0);
	} else {
		int [] indices = new int [1];
		int result = OS.SendMessage (handle, OS.LB_GETSELITEMS, 1, indices);
		index = indices [0];
		if (result != 1) index = -1;
	}
	if (index == -1) return;
	int count = OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);
	if (count == 0) return;
	int height = OS.SendMessage (handle, OS.LB_GETITEMHEIGHT, 0, 0);
	forceResize ();
	RECT rect = new RECT ();
	OS.GetClientRect (handle, rect);
	int topIndex = OS.SendMessage (handle, OS.LB_GETTOPINDEX, 0, 0);
	int visibleCount = Math.max (rect.bottom / height, 1);
	int bottomIndex = Math.min (topIndex + visibleCount + 1, count - 1);
	if ((topIndex <= index) && (index <= bottomIndex)) return;
	int newTop = Math.min (Math.max (index - (visibleCount / 2), 0), count - 1);
	OS.SendMessage (handle, OS.LB_SETTOPINDEX, newTop, 0);
}

int widgetStyle () {
	int bits = super.widgetStyle () | OS.LBS_NOTIFY | OS.LBS_NOINTEGRALHEIGHT;
	if ((style & SWT.SINGLE) != 0) return bits;
	if ((style & SWT.MULTI) != 0) {
		if ((style & SWT.SIMPLE) != 0) return bits | OS.LBS_MULTIPLESEL;
		return bits | OS.LBS_EXTENDEDSEL;
	}
	return bits;
}

TCHAR windowClass () {
	return ListClass;
}

int windowProc () {
	return ListProc;
}

LRESULT wmCommandChild (int wParam, int lParam) {
	int code = wParam >> 16;
	switch (code) {
		case OS.LBN_SELCHANGE:
			postEvent (SWT.Selection);
			break;
		case OS.LBN_DBLCLK:
			postEvent (SWT.DefaultSelection);
			break;
	}
	return super.wmCommandChild (wParam, lParam);
}



}
