package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.internal.carbon.*;

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
public /*final*/ class List extends Scrollable {

	// AW
	private static final int COL_ID= 12345;
	private ArrayList fData= new ArrayList();
	private int fRowID= 1000;
	
	private class Pair {
		int fId;
		String fValue;

		Pair(String v) {
			fValue= v;
			fId= fRowID++;
		}
		
	}
	// AW
	
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
    Pair p= new Pair(string);
	fData.add(p);
	OS.AddDataBrowserItems(handle, OS.kDataBrowserNoItem, 1, new int[] { p.fId }, 0);
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	int size= fData.size();
	if (!(0 <= index && index <= size)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
    Pair p= new Pair(string);
	fData.add(index, p);
	if (OS.AddDataBrowserItems(handle, OS.kDataBrowserNoItem, 1, new int[] { p.fId }, 0) != OS.kNoErr)
		error (SWT.ERROR_ITEM_NOT_ADDED);
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}
static int checkStyle (int style) {
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
    /* AW
	XtWidgetGeometry result = new XtWidgetGeometry ();
	result.request_mode = OS.CWWidth;
	OS.XtQueryGeometry (handle, null, result);
	int width = result.width, height = 0;
	*/
	int width = 300, height = 0;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	if (hHint == SWT.DEFAULT || wHint == SWT.DEFAULT) {
		/* AW
		int [] argList = {OS.XmNitemCount, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		int count = argList [1];
		*/
		int count = fData.size();
		if (hHint == SWT.DEFAULT) {
			if (count == 0) {
				height = DEFAULT_HEIGHT;
			} else {
				height = getItemHeight () * count;
			}
		}
		if (wHint == SWT.DEFAULT && count == 0) {
			width = DEFAULT_WIDTH;
		}
	}
	Rectangle rect = computeTrim (0, 0, width, height);
	return new Point (rect.width, rect.height);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	int border = getBorderWidth ();
	int trimX = x - border;
	int trimY = y - border;
	int trimWidth = width + (border * 2);
	int trimHeight = height + (border * 2);
	Display display= getDisplay();
	if (horizontalBar != null) {
		trimHeight += 15;
		trimY -= display.scrolledInsetY;
		if (verticalBar != null) {
			trimX -= display.scrolledInsetX;
		}
 	}
	if (verticalBar != null) {
 		trimWidth += 15;
		trimX -= display.scrolledInsetX;
		if (horizontalBar != null) {
			trimY -= display.scrolledInsetY;
		}
	}
    /* AW
	int [] argList = {
		OS.XmNhighlightThickness, 0, // 1
		OS.XmNshadowThickness, 0, // 3
		OS.XmNlistMarginWidth, 0, // 5
		OS.XmNlistMarginHeight, 0 // 7
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int thickness = argList [1] + (argList [3] * 2);
	trimWidth += thickness + argList [5] + 1;
	trimHeight += thickness + argList [7] + 1;
	trimX -= argList [1] + argList [3] + argList [5];
	trimY -= argList [1] + argList [3] + argList [7];
	*/
	return new Rectangle (trimX, trimY, trimWidth, trimHeight);
}
void createHandle (int index) {
	state |= HANDLE;

	int parentHandle = parent.handle;
	int windowHandle= OS.GetControlOwner(parentHandle);
	handle= OS.createDataBrowserControl(windowHandle);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	MacUtil.addControl(handle, parentHandle);
	MacUtil.initLocation(handle);
	
	/* Single or Multiple Selection */
	int mode= OS.kDataBrowserSelectOnlyOne;
	if ((style & SWT.MULTI) != 0)
		mode= OS.kDataBrowserDragSelect | OS.kDataBrowserCmdTogglesSelection;
	OS.SetDataBrowserSelectionFlags(handle, mode);
	
	/* hide the neader */
	OS.SetDataBrowserListViewHeaderBtnHeight(handle, (short) 0);
	
	/* enable scrollbars */
	OS.SetDataBrowserHasScrollBars(handle, (style & SWT.H_SCROLL) != 0, (style & SWT.V_SCROLL) != 0);
	if ((style & SWT.H_SCROLL) == 0)
		OS.AutoSizeDataBrowserListViewColumns(handle);
		
	int columnDesc= OS.newColumnDesc(COL_ID, OS.kDataBrowserTextType,
					OS.kDataBrowserListViewSelectionColumn | OS.kDataBrowserDefaultPropertyFlags,
					(short)0, (short)2000);
	OS.AddDataBrowserListViewColumn(handle, columnDesc, 10000);
}
ScrollBar createScrollBar (int type) {
	return createStandardBar (type);
}
/* AW
int defaultBackground () {
	return getDisplay ().listBackground;
}
int defaultFont () {
	return getDisplay ().listFont;
}
int defaultForeground () {
	return getDisplay ().listForeground;
}
*/
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
    if (index >= 0 && index < fData.size()) {
    	Pair p= (Pair) fData.get(index);
    	if (p != null)
			OS.SetDataBrowserSelectedItems(handle, 1, new int[] { p.fId }, OS.kDataBrowserItemsRemove);
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
	if (start > end) return;
	int[] ids= getIds(start, end);
	OS.SetDataBrowserSelectedItems(handle, ids.length, ids, OS.kDataBrowserItemsRemove);
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
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int [] indices) {
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int[] ids= getIds(indices);
	OS.SetDataBrowserSelectedItems(handle, ids.length, ids, OS.kDataBrowserItemsRemove);
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
	checkWidget();
	int n= fData.size();
	if (n <= 0) return;
	int[] ids= getIds(0, n-1);
	OS.SetDataBrowserSelectedItems(handle, ids.length, ids, OS.kDataBrowserItemsRemove);
}
/**
 * Returns the zero-relative index of the item which is currently
 * has the focus in the receiver, or -1 if no item is has focus.
 *
 * @return the index of the selected item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getFocusIndex () {
	checkWidget();
    /* AW
	return OS.XmListGetKbdItemPos (handle) - 1;
    */
    return -1;
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
	checkWidget();
	int size= fData.size();
	if (!(0 <= index && index < size))
		error (SWT.ERROR_INVALID_RANGE);
	Pair p= (Pair) fData.get(index);
	return p.fValue;
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
	checkWidget();
	return fData.size();
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
	checkWidget();
    return 15;	// AW FIXME
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
	checkWidget();
    String[] result= new String[fData.size()];
    Iterator iter= fData.iterator();
    for (int i= 0; iter.hasNext(); i++) {
    	Pair p= (Pair) iter.next();
    	result[i]= p.fValue;
    }
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
	checkWidget();
	int[] ids= MacUtil.getSelectionIDs(handle, OS.kDataBrowserNoItem, false);
	String[] result= new String[ids.length];
	for (int i= 0; i < ids.length; i++)
		result[i]= get(ids[i]);
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
	checkWidget();
	int[] result= new int[1];
	if (OS.GetDataBrowserItemCount(handle, OS.kDataBrowserNoItem, false, OS.kDataBrowserItemIsSelected, result) != OS.kNoErr)
		error (SWT.ERROR_CANNOT_GET_COUNT);
	return result[0];
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
	checkWidget();
	int[] ids= MacUtil.getSelectionIDs(handle, OS.kDataBrowserNoItem, false);
	if (ids.length > 0)
		return getIndex(ids[0]);
	return -1;
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
	checkWidget();
    int[] ids= MacUtil.getSelectionIDs(handle, OS.kDataBrowserNoItem, false);
	int[] result= new int[ids.length];
	for (int i= 0; i < ids.length; i++)
		result[i]= getIndex(ids[i]);
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
    int[] top= new int[1];
    int[] left= new int[1];
    OS.GetDataBrowserScrollPosition(handle, top, left);
    return top[0] / getItemHeight();
}
void hookEvents () {
	super.hookEvents ();
	Display display= getDisplay();
	OS.setDataBrowserCallbacks(handle, display.fDataBrowserDataProc,
				display.fDataBrowserCompareProc, display.fDataBrowserItemNotificationProc);
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
	checkWidget();
	return getIndex(string, 0);
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
	checkWidget();
	return getIndex(string, start);
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
    if (index >= 0 && index < fData.size()) {
		Pair p= (Pair) fData.get(index);
		if (p != null)
			return OS.IsDataBrowserItemSelected(handle, p.fId);
    }
    return false;
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
	checkWidget();
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	int size= fData.size();
	if (!(0 <= index && index < size)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	Pair p= (Pair) fData.remove(index);
	OS.RemoveDataBrowserItems(handle, OS.kDataBrowserNoItem, 1, new int[] { p.fId }, 0);
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
	checkWidget();
	if (start > end) return;
	int n= fData.size();
	if (start < 0 || start >= n || end < 0 || end >= n)
		error (SWT.ERROR_INVALID_RANGE);
	int[] ids= getIds(start, end);
	if (OS.RemoveDataBrowserItems(handle, OS.kDataBrowserNoItem, ids.length, ids, 0) != OS.kNoErr)
		error (SWT.ERROR_ITEM_NOT_REMOVED);
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	Pair p= getPair(string);
	if (p == null) error (SWT.ERROR_INVALID_ARGUMENT);
	fData.remove(p);
	if (OS.RemoveDataBrowserItems(handle, OS.kDataBrowserNoItem, 1, new int[] { p.fId }, 0) != OS.kNoErr)
		error (SWT.ERROR_ITEM_NOT_REMOVED);
}
/**
 * Removes the items from the receiver at the given
 * zero-relative indices.
 *
 * @param indices the array of indices of the items
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
public void remove (int [] indices) {
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int[] ids= getIds(indices);
	if (OS.RemoveDataBrowserItems(handle, OS.kDataBrowserNoItem, ids.length, ids, 0) != OS.kNoErr)
		error (SWT.ERROR_ITEM_NOT_REMOVED);
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
	fData.clear();
	OS.RemoveDataBrowserItems(handle, OS.kDataBrowserNoItem, 0, null, 0);
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);
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
	checkWidget();
	if (index == -1) return;
    Pair p= (Pair) fData.get(index);
    if (p != null)
		OS.SetDataBrowserSelectedItems(handle, 1, new int[] { p.fId }, OS.kDataBrowserItemsAssign);
}
/**
 * Selects the items at the given zero-relative indices in the receiver.
 * If the item at the index was already selected, it remains
 * selected. The range of the indices is inclusive. Indices that are
 * out of range are ignored.
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
	checkWidget();
	if (start > end) return;
	if ((style & SWT.SINGLE) != 0) {
        /* AW
		int [] argList = {OS.XmNitemCount, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		int index = Math.min (argList[1] - 1, end) + 1;
		if (index != 0 && index >= start) OS.XmListSelectPos (handle, index, false);
        */
		return;
	}
    int[] ids= getIds(start, end);
	OS.SetDataBrowserSelectedItems(handle, ids.length, ids, OS.kDataBrowserItemsAssign);
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
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int [] indices) {
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SINGLE) != 0) {
		/* AW
		int [] argList = {OS.XmNitemCount, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		int count = argList [1];
		for (int i = 0; i < indices.length; i++) {
			int index = indices [i];
			if (0 <= index && index < count) {
				select (index);
				return;
			}
		}
		*/
		return;
	}
	int[] ids= getIds(indices);
	OS.SetDataBrowserSelectedItems(handle, ids.length, ids, OS.kDataBrowserItemsAssign);
}
void select (String [] items) {
	checkWidget();
	int[] ids= getIds(items);
	OS.SetDataBrowserSelectedItems(handle, ids.length, ids, OS.kDataBrowserItemsAssign);
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
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return;
	int n= fData.size();
	if (n <= 0) return;
	int[] ids= getIds(0, n-1);
	OS.SetDataBrowserSelectedItems(handle, ids.length, ids, OS.kDataBrowserItemsAssign);
}
void setFocusIndex (int index) {
    /* AW
	OS.XmListSetKbdItemPos (handle, index + 1);
    */
	System.out.println("List.setFocusIndex: nyi");
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	int size= fData.size();
	if (!(0 <= index && index < size)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
    Pair p= (Pair) fData.get(index);
    p.fValue= string;
    OS.UpdateDataBrowserItems(handle, OS.kDataBrowserNoItem, 1, new int[] { p.fId }, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
}
/**
 * Sets the receiver's items to be the given array of items.
 *
 * @param items the array of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void setItems (String [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);

	fData.clear();
	int count= items.length;
	int[] ids= new int[count];
	for (int i= 0; i < count; i++) {
		Pair p= new Pair(items[i]);
		fData.add(p);
		ids[i]= p.fId;
	}
	if (OS.AddDataBrowserItems(handle, OS.kDataBrowserNoItem, ids.length, ids, 0) != OS.kNoErr)
		error (SWT.ERROR_ITEM_NOT_ADDED);
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
	if ((style & SWT.MULTI) != 0) deselectAll ();
	select (index);
}
/**
 * Selects the items at the given zero-relative indices in the receiver. 
 * The current selected if first cleared, then the new items are selected.
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
	if ((style & SWT.MULTI) != 0) deselectAll ();
	select (start, end);
}
/**
 * Selects the items at the given zero-relative indices in the receiver. 
 * The current selection is first cleared, then the new items are selected.
 *
 * @param indices the indices of the items to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see List#deselectAll()
 * @see List#select(int[])
 */
public void setSelection(int[] indices) {
	if ((style & SWT.MULTI) != 0) deselectAll ();
	select (indices);
}
/**
 * Sets the receiver's selection to be the given array of items.
 * The current selected is first cleared, then the new items are
 * selected.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
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
	checkWidget();
	int[] ids= getIds(items);
	OS.SetDataBrowserSelectedItems(handle, ids.length, ids, OS.kDataBrowserItemsAssign);
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
    int[] top= new int[1];
    int[] left= new int[1];
    OS.GetDataBrowserScrollPosition(handle, top, left);
    top[0]= index * getItemHeight() + 4;
    OS.SetDataBrowserScrollPosition(handle, top[0], left[0]);
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
 */
public void showSelection () {
	checkWidget();	
	int[] ids= MacUtil.getSelectionIDs(handle, OS.kDataBrowserNoItem, false);
	if (ids.length > 0 && ids[0] != 0)
		OS.RevealDataBrowserItem(handle, ids[0], COL_ID, false);
}

////////////////////////////////////
// Mac stuff
////////////////////////////////////

	int processSelection (Object callData) {
		//System.out.println("List.processSelection: " + getSelectionIndex());
		return super.processSelection(callData);
	}

	int sendKeyEvent (int type, MacEvent mEvent, Event event) {
		//processEvent (type, new MacEvent(eRefHandle));
		return OS.eventNotHandledErr;
	}
			
	int handleItemCallback(int rowID, int colID, int item) {
		
		if (colID != COL_ID) {
			//System.out.println("List.handleItemCallback: wrong column id: " + colID);
			return OS.kNoErr;
		}
			
		String s= get(rowID);
		if (s == null) {
			System.out.println("List.handleItemCallback: can't find row with id: " + rowID);
			return -1;
		}
			
		int sHandle= 0;
		try {
			sHandle= OS.CFStringCreateWithCharacters(s);
			OS.SetDataBrowserItemDataText(item, sHandle);
		} finally {
			if (sHandle != 0)
				OS.CFRelease(sHandle);
		}
		return OS.kNoErr;
	}

	int handleCompareCallback(int item1ID, int item2ID, int item) {
		if (getIndex(item1ID) < getIndex(item2ID))
			return 1;
		return 0;
	}
	
	int handleItemNotificationCallback(int item, int message) {
		return OS.kNoErr;
	}
	
	/**
	 * Returns string value of row with the given ID
	 */
	private String get(int id) {
		Iterator iter= fData.iterator();
		while (iter.hasNext()) {
			Pair p= (Pair) iter.next();
    		if (p.fId == id)
    			return p.fValue;
    	}
		return null;
	}

	/**
	 * Returns the index of row with the given ID
	 */
	private int getIndex(int id) {
		Iterator iter= fData.iterator();
		for (int i= 0; iter.hasNext(); i++) {
			Pair p= (Pair) iter.next();
    		if (p.fId == id)
    			return i;			
		}
		return -1;
	}
	
	/**
	 * Returns the index of the first row that matches the given string
	 */
	private int getIndex(String s, int start) {
		if (s == null) error (SWT.ERROR_NULL_ARGUMENT);
		int n= fData.size();
		for (int i= start; i < n; i++) {
			Pair p= (Pair) fData.get(i);
			if (s.equals(p.fValue))
				return i;
		}
		return -1;
	}
	
	/**
	 * Returns the ID of the first row that matches the given string
	 */
	private Pair getPair(String s) {
		Iterator iter= fData.iterator();
		while (iter.hasNext()) {
			Pair p= (Pair) iter.next();
    		if (s.equals(p.fValue))
    			return p;
    	}
		return null;
	}
	
	/**
	 * Returns the ID of the first row that matches the given string
	 */
	private int getID(String s) {
		Iterator iter= fData.iterator();
		while (iter.hasNext()) {
			Pair p= (Pair) iter.next();
    		if (s.equals(p.fValue))
    			return p.fId;
    	}
		return 0;
	}
	
	private int[] getIds(int[] indices) {
		int count= fData.size();
		int[] ids= new int[indices.length];
		for (int i= 0; i < indices.length; i++) {
			int index= indices[i];
			if (!(0 <= index && index < count)) break;
			Pair p= (Pair) fData.get(index);
			ids[i]= p.fId;
		}
		return ids;
	}
	
	private int[] getIds(String[] items) {
		int count= items.length;
		int[] ids= new int[count];
		for (int i=0; i<count; i++) {
			int id= getID(items[i]);
			ids[i]= id;
		}
		return ids;
	}
	
	private int[] getIds(int start, int end) {
		int n= fData.size();
		if (start < 0 && start >= n)
			error (SWT.ERROR_INVALID_RANGE);
		if (end >= n)
			error (SWT.ERROR_INVALID_RANGE);
		int count= end-start+1;
		int[] ids= new int[count];
		for (int i= 0; i < count; i++) {
			Pair p= (Pair) fData.get(start+i);
			ids[i]= p.fId;
		}
		return ids;
	}
}
