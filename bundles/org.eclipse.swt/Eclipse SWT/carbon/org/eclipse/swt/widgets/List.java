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
	/*
	* Feature in Motif.  When an index is out of range,
	* the list widget adds the item at the end.  This
	* behavior is not wrong but it is unwanted.  The
	* fix is to check the range before adding the item.
	*/
    /* AW
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	*/
	int size= fData.size();
	if (!(0 <= index && index <= size)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	/* AW
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), string, true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	OS.XmListAddItemUnselected (handle, xmString, index + 1);
	OS.XmStringFree (xmString);
    */
    Pair p= new Pair(string);
	fData.add(index, p);
	OS.AddDataBrowserItems(handle, OS.kDataBrowserNoItem, 1, new int[] { p.fId }, 0);
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
	/* AW
	int border = getBorderWidth ();
	int trimX = x - border;
	int trimY = y - border;
	int trimWidth = width + (border * 2);
	int trimHeight = height + (border * 2);
	*/
	if (horizontalBar != null) {
        /* AW
		int [] argList = {OS.XmNheight, 0};
		OS.XtGetValues (horizontalBar.handle, argList, argList.length / 2);
        */
		/**
		 * Motif adds four pixels between the bottom of the
		 * list and the horizontal scroll bar. Add those now.
		 */
        /* AW
		trimHeight += argList [1] + 4;
		trimY -= display.scrolledInsetY;
		if (verticalBar != null) {
			trimX -= display.scrolledInsetX;
		}
        */
	}
	if (verticalBar != null) {
        /* AW
		int [] argList = {OS.XmNwidth, 0};
		OS.XtGetValues (verticalBar.handle, argList, argList.length / 2);
		trimWidth += argList [1];
		trimX -= display.scrolledInsetX;
		if (horizontalBar != null) {
			trimY -= display.scrolledInsetY;
		}
        */
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
	return new Rectangle (trimX, trimY, trimWidth, trimHeight);
    */
	System.out.println("List.computeTrim: nyi");
	return new Rectangle (x, y, width, height);
}
void createHandle (int index) {
	state |= HANDLE;

	/*
	* Feature in Motif.  When items are added or removed
	* from a list, it may request and be granted, a new
	* preferred size.  This behavior is unwanted.  The fix
	* is to create a parent for the list that will disallow
	* geometry requests.
	*/
	int parentHandle = parent.handle;
    /* AW
	int [] argList1 = {OS.XmNancestorSensitive, 1};
	formHandle = OS.XmCreateForm (parentHandle, null, argList1, argList1.length / 2);
	if (formHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int selectionPolicy = OS.XmBROWSE_SELECT, listSizePolicy = OS.XmCONSTANT;
	if ((style & SWT.MULTI) != 0) {
		selectionPolicy = OS.XmEXTENDED_SELECT;
		if ((style & SWT.SIMPLE) != 0) selectionPolicy = OS.XmMULTIPLE_SELECT;
	}
	if ((style & SWT.H_SCROLL) == 0) listSizePolicy = OS.XmVARIABLE;
	int [] argList2 = {
		OS.XmNlistSizePolicy, listSizePolicy,
		OS.XmNselectionPolicy, selectionPolicy,
		OS.XmNtopAttachment, OS.XmATTACH_FORM,
		OS.XmNbottomAttachment, OS.XmATTACH_FORM,
		OS.XmNleftAttachment, OS.XmATTACH_FORM,
		OS.XmNrightAttachment, OS.XmATTACH_FORM,
		OS.XmNresizable, 0,
//		OS.XmNmatchBehavior, OS.XmNONE,
	};
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
		handle = OS.XmCreateList (formHandle, null, argList2, argList2.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	} else {
		handle = OS.XmCreateScrolledList (formHandle, null, argList2, argList2.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		scrolledHandle = OS.XtParent (handle);
	}
    */
	int windowHandle= OS.GetControlOwner(parentHandle);
	handle= OS.createDataBrowserControl(windowHandle);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	MacUtil.embedControl(handle, parentHandle);
	MacUtil.initLocation(handle);
	
	//OS.SetDataBrowserSelectionFlags(handle, OS.kDataBrowserSelectOnlyOne + OS.kDataBrowserDragSelect);
	OS.SetDataBrowserHasScrollBars(handle, (style & SWT.H_SCROLL) != 0, (style & SWT.V_SCROLL) != 0);
	OS.SetDataBrowserListViewHeaderBtnHeight(handle, (short) 0);
		
	int columnDesc= OS.newColumnDesc(COL_ID, OS.kDataBrowserTextType, OS.kDataBrowserListViewSelectionColumn, (short)30, (short)200);
	OS.AddDataBrowserListViewColumn(handle, columnDesc, 10000);
	//OS.SetDataBrowserActiveItems(handle, true);
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
	/*
	* Note:  We rely on the fact that XmListDeselectPos ()
	* fails silently when the indices are out of range.
	*/
    /* AW
	if (index != -1) OS.XmListDeselectPos (handle, index + 1);
    */
    System.out.println("List.deselect: nyi");
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
	/*
	* Note:  We rely on the fact that XmListDeselectPos ()
	* fails silently when the indices are out of range.
	*/
	/* AW
	for (int i=start; i<=end; i++) {
		int index = i + 1;
 		if (index != 0) OS.XmListDeselectPos (handle, index);
	}
	*/
    System.out.println("List.deselect(s,e): nyi");
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
	/*
	* Note:  We rely on the fact that XmListDeselectPos ()
	* fails silently when the indices are out of range.
	*/
	/* AW
	for (int i=0; i<indices.length; i++) {
		int index = indices [i] + 1;
 		if (index != 0) OS.XmListDeselectPos (handle, index);
	}
	*/
	getIds(indices);
	System.out.println("List.deselect([]): nyi");
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
    /* AW
	OS.XmListDeselectAllItems (handle);
    */
	System.out.println("List.deselectAll: nyi");
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
    /* AW
	int [] argList = {
		OS.XmNlistSpacing, 0,
		OS.XmNhighlightThickness, 0,
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int spacing = argList [1], highlight = argList [3];
    */

	/* Result is from empirical analysis on Linux and AIX */
    /* AW
	return getFontHeight () + spacing + highlight + 1;
    */
	System.out.println("List.getItemHeight: nyi");
    return 15;
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
    /* AW
	int [] argList = {OS.XmNitems, 0, OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int items = argList [1], itemCount = argList [3];
	int [] buffer1 = new int [1];
	String [] result = new String [itemCount];
	String codePage = getCodePage ();
	for (int i=0; i<itemCount; i++) {
		OS.memmove (buffer1, items, 4);
		int ptr = buffer1 [0];
		int address = OS.XmStringUnparse (
			ptr,
			null,
			OS.XmCHARSET_TEXT,
			OS.XmCHARSET_TEXT,
			null,
			0,
			OS.XmOUTPUT_ALL);
		if (address == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
		int length = OS.strlen (address);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, address, length);
		OS.XtFree (address);
		result[i] = new String (Converter.mbcsToWcs (codePage, buffer));
		items += 4;
	}
	return result;
    */
    int n= fData.size();
    String[] result= new String[n];
    for (int i= 0; i < n; i++) {
    	Pair p= (Pair) fData.get(i);
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
    /* AW
	int [] argList = {OS.XmNselectedItems, 0, OS.XmNselectedItemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int items = argList [1], itemCount = argList [3];
	int [] buffer1 = new int [1];
	String [] result = new String [itemCount];
	String codePage = getCodePage ();
	for (int i=0; i<itemCount; i++) {
		OS.memmove (buffer1, items, 4);
		int ptr = buffer1 [0];
		int address = OS.XmStringUnparse (
			ptr,
			null,
			OS.XmCHARSET_TEXT,
			OS.XmCHARSET_TEXT,
			null,
			0,
			OS.XmOUTPUT_ALL);
		if (address == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
		int length = OS.strlen (address);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, address, length);
		OS.XtFree (address);
		result[i] = new String (Converter.mbcsToWcs (codePage, buffer));
		items += 4;
	}
	return result;
    */
    
    int resultHandle= OS.NewHandle(0);
	if (OS.GetDataBrowserItems(handle, OS.kDataBrowserNoItem, false, OS.kDataBrowserItemIsSelected, resultHandle) != OS.kNoErr)
		error (SWT.ERROR_CANNOT_GET_SELECTION);
	
	int itemCount= OS.GetHandleSize(resultHandle) / 4;	// sizeof(int)
	String[] result= new String[itemCount];
	if (itemCount > 0) {	
		int resultIDs[]= new int[itemCount];
		OS.getHandleData(resultHandle, resultIDs);
		for (int i= 0; i < itemCount; i++)
			result[i]= get(resultIDs[i]);
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
	checkWidget();
	int[] result= new int[1];
	if (OS.GetDataBrowserItemCount(handle, OS.kDataBrowserNoItem, false, OS.kDataBrowserItemIsSelected, result) == OS.kNoErr)
		return result[0];
	System.out.println("List.getSelectionCount: error in GetDataBrowserItemCount");
    return 0;
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
    /* AW
	int index = OS.XmListGetKbdItemPos (handle);
	if (OS.XmListPosSelected (handle, index)) return index - 1;
	int [] count = new int [1], positions = new int [1];
	if (!OS.XmListGetSelectedPos (handle, positions, count)) return -1;
	if (count [0] == 0) return -1;
	int address = positions [0];
	int [] indices = new int [1];
	OS.memmove (indices, address, 4);
	OS.XtFree (address);
	return indices [0] - 1;
    */
    int resultHandle= OS.NewHandle(0);
	if (OS.GetDataBrowserItems(handle, OS.kDataBrowserNoItem, false, OS.kDataBrowserItemIsSelected, resultHandle) != OS.kNoErr)
		error (SWT.ERROR_CANNOT_GET_SELECTION);
	
	int itemCount= OS.GetHandleSize(resultHandle) / 4;	// sizeof(int)
	if (itemCount > 0) {	
		int resultIDs[]= new int[1];
		OS.getHandleData(resultHandle, resultIDs);
		return getIndex(resultIDs[0]);
	}
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
    /*
	int [] count = new int [1], positions = new int [1];
	OS.XmListGetSelectedPos (handle, positions, count);
	int [] result = new int [count [0]];
	OS.memmove (result, positions [0], count [0] * 4);
	if (positions [0] != 0) OS.XtFree (positions [0]);
	for (int i=0; i<result.length; i++) --result [i];
	return result;
    */
    
    int resultHandle= OS.NewHandle(0);
	if (OS.GetDataBrowserItems(handle, OS.kDataBrowserNoItem, false, OS.kDataBrowserItemIsSelected, resultHandle) != OS.kNoErr)
		error (SWT.ERROR_CANNOT_GET_SELECTION);
	
	int itemCount= OS.GetHandleSize(resultHandle) / 4;	// sizeof(int)
	int[] result= new int[itemCount];
	if (itemCount > 0) {	
		int resultIDs[]= new int[itemCount];
		OS.getHandleData(resultHandle, resultIDs);
		for (int i= 0; i < itemCount; i++)
			result[i]= getIndex(resultIDs[i]);
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
	checkWidget();
    /* AW
	int [] argList = {OS.XmNtopItemPosition, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] - 1;
    */
	System.out.println("List.getTopIndex: nyi");
    return 1;
}
void hookEvents () {
	super.hookEvents ();
    /* AW
	int windowProc = getDisplay ().windowProc;
	OS.XtAddCallback (handle, OS.XmNbrowseSelectionCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNextendedSelectionCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNdefaultActionCallback, windowProc, SWT.DefaultSelection);
    */
	Display display= getDisplay();
	OS.setDataBrowserItemDataCallback(handle, display.fDataBrowserDataProc);
	OS.setDataBrowserItemNotificationCallback(handle, display.fDataBrowserItemNotificationProc);
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
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i= 0; i < fData.size(); i++) {
		Pair p= (Pair) fData.get(i);
		if (string.equals(p.fValue))
			return i;
	}
    return -1;
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
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i= start; i < fData.size(); i++) {
		Pair p= (Pair) fData.get(i);
		if (string.equals(p.fValue))
			return i;
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
	if (index == -1) return false;
    /* AW
	return OS.XmListPosSelected (handle, index + 1);
    */
	System.out.println("List.isSelected: nyi");
    return true;
}
int processSelection (Object callData) {
	System.out.println("List.processSelection: " + getSelectionIndex());
	
	return super.processSelection(callData);
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
	/*
	* Feature in Motif.  An index out of range handled
	* correctly by the list widget but causes an unwanted
	* Xm Warning.  The fix is to check the range before
	* deleting an item.
	*/
    /* AW
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	*/
	int size= fData.size();
	if (!(0 <= index && index < size)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	/* AW
	OS.XmListDeletePos (handle, index + 1);
    */
	
	Pair p= (Pair) fData.get(index);
	fData.remove(index);
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
	int count = end - start + 1;
	/*
	* Feature in Motif.  An index out of range handled
	* correctly by the list widget but causes an unwanted
	* Xm Warning.  The fix is to check the range before
	* deleting an item.
	*/
    /* AW
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (!(0 <= start && start < argList [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	OS.XmListDeleteItemsPos (handle, count, start + 1);
	if (end >= argList [1]) error (SWT.ERROR_INVALID_RANGE);
    */
	/*
	OS.LDelRow((short)count, (short)start, OS.getListHandle(handle));
	*/
	int[] ids= new int[count];
	for (int i= 0; i < count; i++) {
		Pair p= (Pair) fData.get(start+i);
		ids[i]= p.fId;
	}
	OS.RemoveDataBrowserItems(handle, OS.kDataBrowserNoItem, count, ids, 0);
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
	for (int i= 0; i < fData.size(); i++) {
		Pair p= (Pair) fData.get(i);
		if (string.equals(p.fValue)) {
			if (OS.RemoveDataBrowserItems(handle, OS.kDataBrowserNoItem, 1, new int[] { p.fId }, 0) == OS.kNoErr)
				return;
			error (SWT.ERROR_ITEM_NOT_REMOVED);
		}
	}
	error (SWT.ERROR_INVALID_ARGUMENT);
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
	/*
	* Feature in Motif.  An index out of range handled
	* correctly by the list widget but causes an unwanted
	* Xm Warning.  The fix is to check the range before
	* deleting an item.
	*/
    /* AW
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int length = 0, count = argList [1];
	int [] newIndices = new int [indices.length];
	for (int i=0; i<indices.length; i++) {
		int index = indices [i];
		if (!(0 <= index && index < count)) break;
		newIndices [length++] = index + 1;
	}
	OS.XmListDeletePositions (handle, newIndices, length);
	if (length < indices.length) error (SWT.ERROR_INVALID_RANGE);
    */
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
    /* AW
	if (OS.XmListPosSelected (handle, index + 1)) return;
    */
	/*
	* Feature in MOTIF.  The X/MOTIF 1.2 spec says that XmListSelectPos ()
	* in a XmEXTENDED_SELECT list widget will add the index to the selected
	* indices.  The observed behavior does not match the spec.  The fix is
	* to temporarily switch the XmNselectionPolicy to XmMULTIPLE_SELECT
	* and then switch it back because XmListSelectPost () works as specified
	* for XmMULTIPLE_SELECT list widgets.
	*/
    /* AW
	int [] argList = {OS.XmNselectionPolicy, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int oldPolicy = argList [1];
	if (oldPolicy == OS.XmEXTENDED_SELECT) {
		argList [1] = OS.XmMULTIPLE_SELECT;
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
    */
	/*
	* Note:  We rely on the fact that XmListSelectPos ()
	* fails silently when the indices are out of range.
	*/
    /* AW
	OS.XmListSelectPos (handle, index + 1, false);
	if (oldPolicy == OS.XmEXTENDED_SELECT) {
		argList [1] = OS.XmEXTENDED_SELECT;
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
    */
    Pair p= (Pair) fData.get(index);
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
	/*
	* Feature in MOTIF.  The X/MOTIF 1.2 spec says that XmListSelectPos ()
	* in a XmEXTENDED_SELECT list widget will add the index to the selected
	* indices.  The observed behavior does not match the spec.  The fix is
	* to temporarily switch the XmNselectionPolicy to XmMULTIPLE_SELECT
	* and then switch it back because XmListSelectPos () works as specified
	* for XmMULTIPLE_SELECT list widgets.
	*/
    /* AW
	int [] argList = {OS.XmNselectionPolicy, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int oldPolicy = argList [1];
	if (oldPolicy == OS.XmEXTENDED_SELECT) {
		argList [1] = OS.XmMULTIPLE_SELECT;
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
    */
	/*
	* Note:  We rely on the fact that XmListSelectPos ()
	* fails silently when the indices are out of range.
	*/
	/* AW
	for (int i=start; i<=end; i++) {
		int index = i + 1;
		if ((index != 0) && !OS.XmListPosSelected (handle, index)) {
			OS.XmListSelectPos (handle, index, false);
		}
	}
	*/
    /* AW
	if (oldPolicy == OS.XmEXTENDED_SELECT) {
		argList [1] = OS.XmEXTENDED_SELECT;
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
    */
	int count= end-start+1;
	int[] ids= new int[count];
	for (int i= 0; i < count; i++) {
		Pair p= (Pair) fData.get(start+i);
		ids[i]= p.fId;
	}
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
    /* AW
	if ((style & SWT.SINGLE) != 0) {
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
		return;
	}
    */
	/*
	* Feature in MOTIF.  The X/MOTIF 1.2 spec says that XmListSelectPos ()
	* in a XmEXTENDED_SELECT list widget will add the index to the selected
	* indices.  The observed behavior does not match the spec.  The fix is
	* to temporarily switch the XmNselectionPolicy to XmMULTIPLE_SELECT
	* and then switch it back because XmListSelectPos () works as specified
	* for XmMULTIPLE_SELECT list widgets.
	*/
    /* AW
	int [] argList = {OS.XmNselectionPolicy, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int oldPolicy = argList [1];
	if (oldPolicy == OS.XmEXTENDED_SELECT) {
		argList [1] = OS.XmMULTIPLE_SELECT;
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
    */
	/*
	* Note:  We rely on the fact that XmListSelectPos ()
	* fails silently when the indices are out of range.
	*/
    /* AW
	for (int i=0; i<indices.length; i++) {
		int index = indices [i] + 1;
		if ((index != 0) && !OS.XmListPosSelected (handle, index)) {
			OS.XmListSelectPos (handle, index, false);
		}
	}
	if (oldPolicy == OS.XmEXTENDED_SELECT) {
		argList [1] = OS.XmEXTENDED_SELECT;
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
    */
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
	/*
	* Feature in MOTIF.  The X/MOTIF 1.2 spec says that XmListSelectPos ()
	* in a XmEXTENDED_SELECT list widget will add the index to the selected
	* indices.  The observed behavior does not match the spec.  The fix is
	* to temporarily switch the XmNselectionPolicy to XmMULTIPLE_SELECT
	* and then switch it back because XmListSelectPos () works as specified
	* for XmMULTIPLE_SELECT list widgets.
	*/
    /* AW
	int [] argList = {OS.XmNselectionPolicy, 0, OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int oldPolicy = argList [1];
	if (oldPolicy == OS.XmEXTENDED_SELECT) {
		argList [1] = OS.XmMULTIPLE_SELECT;
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
	for (int i=0; i<argList[3]; i++) {
		int index = i + 1;
		if (!OS.XmListPosSelected (handle, index)) {
			OS.XmListSelectPos (handle, index, false);
		}
	}
	if (oldPolicy == OS.XmEXTENDED_SELECT) {
		argList [1] = OS.XmEXTENDED_SELECT;
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
    */
	int count= fData.size();
	int[] ids= new int[count];
	for (int i= 0; i < count; i++) {
		Pair p= (Pair) fData.get(i);
		ids[i]= p.fId;
	}
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
    /* AW
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	*/
	int size= fData.size();
	if (!(0 <= index && index < size)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	/* AW
	byte [] buffer = Converter.wcsToMbcs (getCodePage (), string, true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	boolean isSelected = OS.XmListPosSelected (handle, index + 1);
	OS.XmListReplaceItemsPosUnselected (handle, new int [] {xmString}, 1, index + 1);
	if (isSelected) OS.XmListSelectPos (handle, index + 1, false);
	OS.XmStringFree (xmString);
    */
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
	/*
	* Bug in AIX.  When all list items are replaced
	* in a scrolled list that is currently showing a
	* horizontal scroll bar, the horizontal scroll bar
	* is hidden, but the list does not grow to take up
	* the space once occupied by the bar.  The fix is
	* of force the horizontal bar to be recomputed by
	* removing all items and resizing the list.
	*/
    /* AW
	OS.XmListSetPos (handle, 0);
	OS.XmListDeselectAllItems (handle);
	if ((style & SWT.H_SCROLL) != 0) {
		OS.XmListDeleteAllItems (handle);
	}
	int index = 0;
	int [] table = new int [items.length];
	String codePage = getCodePage ();
	while (index < items.length) {
		String string = items [index];
		if (string == null) break;
		byte [] buffer = Converter.wcsToMbcs (codePage, string, true);
		int xmString = OS.XmStringCreateLocalized (buffer);
		if (xmString == 0) break;
		table [index++] = xmString;
	}
	int ptr = OS.XtMalloc (index * 4);
	OS.memmove (ptr, table, index * 4);
	int [] argList = {OS.XmNitems, ptr, OS.XmNitemCount, index};
	OS.XtSetValues (handle, argList, argList.length / 2);
	for (int i=0; i<index; i++) OS.XmStringFree (table [i]);
	OS.XtFree (ptr);
    */
	/*
	* Bug in Motif.  Resize the list to work around
	* the horizontal scroll bar display bug described
	* above.
	*/
    /* AW
	if ((style & SWT.H_SCROLL) != 0) {
		OS.XtResizeWindow (handle);
	}
	if (index < items.length) error (SWT.ERROR_ITEM_NOT_ADDED);
    */
    /* AW
	int oldSize= fData.size();
	*/
	fData.clear();
	int count= items.length;
	int[] ids= new int[count];
	for (int i= 0; i < count; i++) {
		Pair p= new Pair(items[i]);
		fData.add(p);
		ids[i]= p.fId;
	}
	OS.AddDataBrowserItems(handle, OS.kDataBrowserNoItem, ids.length, ids, 0);
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
    /* AW
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	String codePage = getCodePage ();
	if ((style & SWT.SINGLE) != 0) {
		for (int i=items.length-1; i>=0; --i) {
			String string = items [i];
			if (string != null) {
				byte [] buffer = Converter.wcsToMbcs (codePage, string, true);
				int xmString = OS.XmStringCreateLocalized (buffer);
				if (xmString != 0) {
					int index = OS.XmListItemPos (handle, xmString);
					if (index != 0) OS.XmListSelectPos (handle, index, false);
					OS.XmStringFree (xmString);
					if ((index != 0) && OS.XmListPosSelected (handle, index)) return;
				}
			}
		}
		OS.XmListDeselectAllItems (handle);
		return;
	}
	OS.XmListDeselectAllItems (handle);
	int length = 0;
	int [] table = new int [items.length];
	for (int i=0; i<items.length; i++) {
		String string = items [i];
		if (string != null) {
			byte [] buffer = Converter.wcsToMbcs (codePage, string, true);
			int xmString = OS.XmStringCreateLocalized (buffer);
			if (xmString != 0) table [length++] = xmString;
		}
	}
	int ptr = OS.XtMalloc (length * 4);
	OS.memmove (ptr, table, length * 4);
	int [] argList = {OS.XmNselectedItems, ptr, OS.XmNselectedItemCount, length};
	OS.XtSetValues (handle, argList, argList.length / 2);
	for (int i=0; i<length; i++) OS.XmStringFree (table [i]);
	OS.XtFree (ptr);
	OS.XmListUpdateSelectedList (handle);
    */
	int count= items.length;
	int[] ids= new int[count];
	for (int i=0; i<count; i++) {
		int id= get(items [i]);
		ids[i]= id;
	}
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
    /* AW
	int [] argList = {OS.XmNitemCount, 0, OS.XmNvisibleItemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int newIndex = Math.max (1, Math.min (index + 1, argList [1]));
	int lastIndex = Math.max (1, argList [1] - argList [3] + 1);
	if (newIndex > lastIndex) newIndex = lastIndex;
	OS.XmListSetPos (handle, newIndex);
    */
	System.out.println("List.setTopIndex: nyi");
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
    /* AW
	int [] buffer = new int [1], positions = new int [1];
	if (!OS.XmListGetSelectedPos (handle, positions, buffer)) return;
	if (buffer [0] == 0) return;
	int address = positions [0];
	int [] indices = new int [1];
	OS.memmove (indices, address, 4);
	OS.XtFree (address);
	int index = indices [0];
	int [] argList = {
		OS.XmNtopItemPosition, 0,
		OS.XmNvisibleItemCount, 0,
		OS.XmNitemCount, 0,
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int topIndex = argList [1], visibleCount = argList [3], count = argList [5];
	int bottomIndex = Math.min (topIndex + visibleCount - 1, count);
	if ((topIndex <= index) && (index <= bottomIndex)) return;
	int lastIndex = Math.max (1, count - visibleCount + 1);
	int newTop = Math.min (Math.max (index - (visibleCount / 2), 1), lastIndex);
	OS.XmListSetPos (handle, newTop);
    */
	System.out.println("List.showSelection: nyi");
}

////////////////////////////////////
// Mac stuff
////////////////////////////////////

	int sendKeyEvent(int type, int nextHandler, int eRefHandle) {
		//processEvent (type, new MacEvent(eRefHandle));
		return OS.eventNotHandledErr;
	}
	
	int handleItemCallback(int cHandle, int colID, int rowID, int item) {
		if (colID != COL_ID) {
			System.out.println("List.handleItemCallback: wrong column ID: " + colID);
			return 0;
		}
			
		String s= get(rowID);
		if (s != null) {
			int sHandle= 0;
			try {
				sHandle= OS.CFStringCreateWithCharacters(s);
				OS.SetDataBrowserItemDataText(item, sHandle);
			} finally {
				if (sHandle != 0)
					OS.CFRelease(sHandle);
			}
		} else {
			System.out.println("List.handleItemCallback: index out of range: " + rowID);
		}
		return 0;
	}

	private String get(int id) {
		int n= fData.size();
    	String[] result= new String[n];
    	for (int i= 0; i < n; i++) {
    		Pair p= (Pair) fData.get(i);
    		if (p.fId == id)
    			return p.fValue;
    	}
		return null;
	}

	private int getIndex(int id) {
		int n= fData.size();
    	String[] result= new String[n];
    	for (int i= 0; i < n; i++) {
    		Pair p= (Pair) fData.get(i);
    		if (p.fId == id)
    			return i;
    	}
		return -1;
	}
	
	private int get(String s) {
		int n= fData.size();
    	for (int i= 0; i < n; i++) {
    		Pair p= (Pair) fData.get(i);
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
			int id= get(items[i]);
			ids[i]= id;
		}
		return ids;
	}
}
