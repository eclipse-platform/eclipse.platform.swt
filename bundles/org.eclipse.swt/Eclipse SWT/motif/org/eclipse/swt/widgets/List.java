package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/* Imports */
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;


/** 
*	The list class represents a selectable user interface object
* that display a list of items and issues notificiation when an
* item is selected from the list.  A list may be single or multi
* select.
* <p>
* <b>Styles</b><br>
* <dd>SINGLE, MULTI,<br>
* <dd>H_SCROLL, V_SCROLL<br>
* <br>
* <b>Events</b><br>
* <dd>Selection<br>
* <dd>DoubleSelection<br>
*/

/* Class Definition */
public /*final*/ class List extends Scrollable {
	int rows, columns;
/**
* Creates a new instance of the widget.
*/
public List (Composite parent, int style) {
	/**
	 * Feature in Motif.  It is not possible to create
	 * scrolled list that will never show the vertical
	 * scroll bar.  Therefore, not matter what style
	 * bits are specified, set the V_SCROLL bits to
	 * match the widget Motif creates.
	 */
	super (parent, checkStyle (style | SWT.V_SCROLL));
}
/**
* Adds an item.
* <p>
* The item is placed at the end of the list.
* Indexing is zero based.
* 
* This operation can fail when the item cannot
* be added in the OS.
*
* @param string the new item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when string is null
* @exception SWTError(ERROR_ITEM_NOT_ADDED)
*	when the operation fails in the OS
*/
public void add (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	OS.XmListAddItemUnselected (handle, xmString, 0);
	OS.XmStringFree (xmString);
}
/**
* Adds an item at an index.
* <p>
* The item is placed at an index in the list.
* Indexing is zero based.
*
* This operation will fail when the index is
* out of range or the item cannot be added in
* the OS.
*
* @param string the new item
* @param index the index for the item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when the string is null
* @exception SWTError(ERROR_ITEM_NOT_ADDED)
*	when the item cannot be added
*/
public void add (String string, int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	/*
	* Feature in Motif.  When an index is out of range,
	* the list widget adds the item at the end.  This
	* behavior is not wrong but it is unwanted.  The
	* fix is to check the range before adding the item.
	*/
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (!(0 <= index && index <= argList [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	OS.XmListAddItemUnselected (handle, xmString, index + 1);
	OS.XmStringFree (xmString);
}
/**	 
* Adds the listener to receive events.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void addSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}
static int checkStyle (int style) {
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}
/**
* Computes the preferred size.
*/
public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	XtWidgetGeometry result = new XtWidgetGeometry ();
	result.request_mode = OS.CWWidth;
	OS.XtQueryGeometry (handle, null, result);
	int width = result.width, height = 0;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	if (hHint == SWT.DEFAULT || wHint == SWT.DEFAULT) {
		int count = rows;
		if (count == 0) {
			int [] argList = {OS.XmNitemCount, 0};
			OS.XtGetValues (handle, argList, argList.length / 2);
			count = argList [1];
		}
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
/**
* Computes the trim.
*/
public Rectangle computeTrim (int x, int y, int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Display display = getDisplay ();
	int border = getBorderWidth ();
	int trimX = x - border;
	int trimY = y - border;
	int trimWidth = width + (border * 2);
	int trimHeight = height + (border * 2);
	if (horizontalBar != null) {
		int [] argList = {OS.XmNheight, 0};
		OS.XtGetValues (horizontalBar.handle, argList, argList.length / 2);
		/**
		 * Motif adds four pixels between the bottom of the
		 * list and the horizontal scroll bar. Add those now.
		 */
		trimHeight += argList [1] + 4;
		trimY -= display.scrolledInsetY;
		if (verticalBar != null) {
			trimX -= display.scrolledInsetX;
		}
	}
	if (verticalBar != null) {
		int [] argList = {OS.XmNwidth, 0};
		OS.XtGetValues (verticalBar.handle, argList, argList.length / 2);
		trimWidth += argList [1];
		trimX -= display.scrolledInsetX;
		if (horizontalBar != null) {
			trimY -= display.scrolledInsetY;
		}
	}
	int [] argList = {
		OS.XmNhighlightThickness, 0, /* 1 */
		OS.XmNshadowThickness, 0, /* 3 */
		OS.XmNlistMarginWidth, 0, /* 5 */
		OS.XmNlistMarginHeight, 0 /* 7 */
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int thickness = argList [1] + (argList [3] * 2);
	trimWidth += thickness + argList [5] + 1;
	trimHeight += thickness + argList [7] + 1;
	trimX -= argList [1] + argList [3] + argList [5];
	trimY -= argList [1] + argList [3] + argList [7];
	return new Rectangle (trimX, trimY, trimWidth, trimHeight);
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
	formHandle = OS.XmCreateForm (parent.handle, null, null, 0);
	if (formHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int selectionPolicy = OS.XmBROWSE_SELECT, listSizePolicy = OS.XmCONSTANT;
	if ((style & SWT.MULTI) != 0) {
		selectionPolicy = OS.XmEXTENDED_SELECT;
		if ((style & SWT.SIMPLE) != 0) selectionPolicy = OS.XmMULTIPLE_SELECT;
	}
	if ((style & SWT.H_SCROLL) == 0) listSizePolicy = OS.XmVARIABLE;
	int [] argList = {
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
		handle = OS.XmCreateList (formHandle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	} else {
		handle = OS.XmCreateScrolledList (formHandle, null, argList, argList.length / 2);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		scrolledHandle = OS.XtParent (handle);
	}
}
ScrollBar createScrollBar (int type) {
	return createStandardBar (type);
}
int defaultBackground () {
	return getDisplay ().listBackground;
}
int defaultFont () {
	return getDisplay ().listFont;
}
int defaultForeground () {
	return getDisplay ().listForeground;
}
/**
* Deselects an item.
* <p>
* If the item at an index is selected, it is
* deselected.  If the item at an index is not
* selected, it remains deselected.  Indices
* that are out of range are ignored.  Indexing
* is zero based.
*
* @param index the index of the item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void deselect (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	/*
	* Note:  We rely on the fact that XmListDeselectPos ()
	* fails silently when the indices are out of range.
	*/
	if (index != -1) OS.XmListDeselectPos (handle, index + 1);
}
/**
* Deselects a range of items.
* <p>
* If the item at an index is selected, it is
* deselected.  If the item at an index is not
* selected, it remains deselected.  Indices
* that are out of range are ignored.
*
* Indexing is zero based.  The range of items
* is from the start index up to and including
* the end index.

* @param start the start of the range
* @param end the end of the range
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void deselect (int start, int end) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (start > end) return;
	/*
	* Note:  We rely on the fact that XmListDeselectPos ()
	* fails silently when the indices are out of range.
	*/
	for (int i=start; i<=end; i++) {
		int index = i + 1;
		if (index != 0) OS.XmListDeselectPos (handle, index);
	}
}
/**
* Deselects items.
* <p>
* If the item at an index is selected, it is
* deselected.  If the item at an index is not
* selected, it remains deselected.  Indices
* that are out of range are ignored.  Indexing
* is zero based.
*
* @param indices the indices of the items
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when the indices are null
*/
public void deselect (int [] indices) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	/*
	* Note:  We rely on the fact that XmListDeselectPos ()
	* fails silently when the indices are out of range.
	*/
	for (int i=0; i<indices.length; i++) {
		int index = indices [i] + 1;
		if (index != 0) OS.XmListDeselectPos (handle, index);
	}
}
/**
* Deselects all items.
* <p>
*
* If an item is selected, it is deselected.
* If an item is not selected, it remains unselected.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void deselectAll () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	OS.XmListDeselectAllItems (handle);
}
public int getFocusIndex () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return OS.XmListGetKbdItemPos (handle) - 1;
}
/**
* Gets an item at an index.
* <p>
* Indexing is zero based.
*
* This operation will fail when the index is out
* of range or an item could not be queried from
* the OS.
*
* @param index the index of the item
* @return the item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_CANNOT_GET_ITEM)
*	when the operation fails
*/
public String getItem (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNitemCount, 0, OS.XmNitems, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (!(0 <= index && index < argList [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (argList [3] == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
	int ptr = argList [3] + (index * 4);
	int [] buffer1 = new int [1]; 
	OS.memmove (buffer1, ptr, 4);
	ptr = buffer1 [0];
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
	return new String (Converter.mbcsToWcs (null, buffer));
}
/**
* Gets the number of items.
* <p>
* This operation will fail if the number of
* items could not be queried from the OS.
*
* @return the number of items in the widget
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_CANNOT_GET_COUNT)
*	when the operation fails
*/
public int getItemCount () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
/**
* Gets the height of one item.
* <p>
* This operation will fail if the height of
* one item could not be queried from the OS.
*
* @return the height of one item in the widget
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_CANNOT_GET_ITEM_HEIGHT)
*	when the operation fails
*/
public int getItemHeight () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {
		OS.XmNlistSpacing, 0,
		OS.XmNhighlightThickness, 0,
	};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int spacing = argList [1], highlight = argList [3];

	/* Result is from empirical analysis on Linux and AIX */
	return getFontHeight () + spacing + highlight + 1;
}
/**
* Gets the items.
* <p>
* This operation will fail if the items cannot
* be queried from the OS.
*
* @return the items in the widget
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_CANNOT_GET_ITEM)
*	when the operation fails
*/
public String [] getItems () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNitems, 0, OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int items = argList [1], itemCount = argList [3];
	int [] buffer1 = new int [1];
	String [] result = new String [itemCount];
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
		result[i] = new String (Converter.mbcsToWcs (null, buffer));
		items += 4;
	}
	return result;
}
/**
* Gets the selected items.
* <p>
* This operation will fail if the selected
* items cannot be queried from the OS.
*
* @return the selected items in the widget
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_CANNOT_GET_ITEM)
*	when the operation fails
* @exception SWTError(ERROR_CANNOT_GET_SELECTION)
*	when the operation fails
*/
public String [] getSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNselectedItems, 0, OS.XmNselectedItemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int items = argList [1], itemCount = argList [3];
	int [] buffer1 = new int [1];
	String [] result = new String [itemCount];
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
		result[i] = new String (Converter.mbcsToWcs (null, buffer));
		items += 4;
	}
	return result;
}
/**
* Gets the number of selected items.
* <p>
* This operation will fail if the number of selected
* items cannot be queried from the OS.
*
* @return the number of selected items in the widget
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_CANNOT_GET_COUNT)
*	when the operation fails
*/
public int getSelectionCount () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNselectedItemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1];
}
/**
* Gets the index of the selected item.
* <p>
* Indexing is zero based.
*
* When the list is single-select, the index of
* the selected item is returned or -1 if no item
* is selected.
*
* When the list is multi-select, the index of
* a selected item that contains the focus
* rectangle or the index of the first selected
* item is return.  If no item is selected, -1
* is returned.
*
* This operation will fail if the selected
* index cannot be queried from the OS.
*
* @return the index of the selected item.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_CANNOT_GET_SELECTION)
*	when the operation fails
*/
public int getSelectionIndex () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
}
/**
* Gets the indices of the selected items.
* <p>
* Indexing is zero based.
*
* If no item is selected, an array is returned.
*
* This operation will fail if the selected
* indices cannot be queried from the OS.
*
* @return the indices of the selected items.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_CANNOT_GET_SELECTION)
*	when the operation fails
*/
public int [] getSelectionIndices () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] count = new int [1], positions = new int [1];
	OS.XmListGetSelectedPos (handle, positions, count);
	int [] result = new int [count [0]];
	OS.memmove (result, positions [0], count [0] * 4);
	if (positions [0] != 0) OS.XtFree (positions [0]);
	for (int i=0; i<result.length; i++) --result [i];
	return result;
}
/**
* Gets the top index.
* <p>
* The top index is the index of the item that
* is currently at the top of the widget.  The
* top index changes when the widget is scrolled.
* Indexing is zero based.
*
* @return the index of the top item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public int getTopIndex () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNtopItemPosition, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	return argList [1] - 1;
}
void hookEvents () {
	super.hookEvents ();
	int windowProc = getDisplay ().windowProc;
	OS.XtAddCallback (handle, OS.XmNbrowseSelectionCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNextendedSelectionCallback, windowProc, SWT.Selection);
	OS.XtAddCallback (handle, OS.XmNdefaultActionCallback, windowProc, SWT.DefaultSelection);
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
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when string is null
*/
public int indexOf (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) return -1;
	int index = OS.XmListItemPos (handle, xmString);
	OS.XmStringFree (xmString);
	return index - 1;
}
/**
* Gets the index of an item.
* <p>
* The list is searched starting at and including
* the start position until an item is found that
* is equal to the search itenm.  If no item is
* found, -1 is returned.  Indexing is zero based.
*
* @param string the search item
* @param index the starting position
* @return the index of the item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when string is null
*/
public int indexOf (String string, int start) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int [] argList = {OS.XmNitems, 0, OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int items = argList [1], itemCount = argList [3];
	if (!((0 <= start) && (start < itemCount))) return -1;
	byte [] buffer1 = Converter.wcsToMbcs (null, string, true);
	int xmString = OS.XmStringCreateLocalized (buffer1);
	if (xmString == 0) return -1;
	int index = start;
	items += start * 4;
	int [] buffer2 = new int [1];
	while (index < itemCount) {
		OS.memmove (buffer2, items, 4);
		if (OS.XmStringCompare (buffer2 [0], xmString)) break;
		items += 4;  index++;
	}
	OS.XmStringFree (xmString);
	if (index == itemCount) return -1;
	return index;
}
/**
* Determines if an item is selected. 
* <p>
* Indexing is zero based.
*
* @param index the index of the item
* @return the selection state
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public boolean isSelected (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index == -1) return false;
	return OS.XmListPosSelected (handle, index + 1);
}
/**
* Removes an item at an index.
* <p>
* Indexing is zero based.
*
* This operation will fail when the index is out
* of range or an item could not be removed from
* the OS.
*
* @param index the index of the item
* @return the selection state
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_ITEM_NOT_REMOVED)
*	when the operation fails
*/
public void remove (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	/*
	* Feature in Motif.  An index out of range handled
	* correctly by the list widget but causes an unwanted
	* Xm Warning.  The fix is to check the range before
	* deleting an item.
	*/
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (!(0 <= index && index < argList [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	OS.XmListDeletePos (handle, index + 1);
}
/**
* Removes a range of items.
* <p>
* Indexing is zero based.  The range of items
* is from the start index up to and including
* the end index.
*
* This operation will fail when the index is out
* of range or an item could not be removed from
* the OS.
*
* @param start the start of the range
* @param end the end of the range
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_ITEM_NOT_REMOVED)
*	when the operation fails
*/
public void remove (int start, int end) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (start > end) return;
	int count = end - start + 1;
	/*
	* Feature in Motif.  An index out of range handled
	* correctly by the list widget but causes an unwanted
	* Xm Warning.  The fix is to check the range before
	* deleting an item.
	*/
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (!(0 <= start && start < argList [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	OS.XmListDeleteItemsPos (handle, count, start + 1);
	if (end >= argList [1]) error (SWT.ERROR_INVALID_RANGE);
}
/**
* Removes an item.
* <p>
* This operation will fail when the item
* could not be removed from the OS.
*
* @param string the search item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when string is null
* @exception SWTError(ERROR_ITEM_NOT_REMOVED)
*	when the operation fails
*/
public void remove (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
	int index = OS.XmListItemPos (handle, xmString);
	OS.XmStringFree (xmString);
	if (index == 0) error (SWT.ERROR_INVALID_ARGUMENT);
	OS.XmListDeletePos (handle, index);
}
/**
* Removes items.
* <p>
* Indexing is zero based.
*
* This operation will fail when the index is out
* of range or an item could not be removed from
* the OS.
*
* @param indices the indices of the items
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when the indices are null
* @exception SWTError(ERROR_ITEM_NOT_REMOVED)
*	when the operation fails
*/
public void remove (int [] indices) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	/*
	* Feature in Motif.  An index out of range handled
	* correctly by the list widget but causes an unwanted
	* Xm Warning.  The fix is to check the range before
	* deleting an item.
	*/
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
}
/**
* Removes all items.
* <p>
* This operation will fail when an item
* could not be removed in the OS.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_ITEM_NOT_REMOVED)
*	when the operation fails
*/
public void removeAll () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	OS.XmListDeselectAllItems (handle);
	OS.XmListDeleteAllItems (handle);
	/*
	* Bug in AIX.  When all list items are deleted
	* from a scrolled list that is currently showing a
	* horizontal scroll bar, the horizontal scroll bar
	* is hidden, but the list does not grow to take up
	* the space once occupied by the bar.  The fix is
	* of force a resize of the list.
	*/
	if ((style & SWT.H_SCROLL) != 0) OS.XtResizeWindow (handle);
}
/**	 
* Removes the listener.
* <p>
*
* @param listener the listener
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when listener is null
*/
public void removeSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);	
}
/**
* Selects an item.
* <p>
* If the item at an index is not selected, it is
* selected.  If the item at an index is selected,
* it remains selected.  Indices that are out of
* range are ignored.  Indexing is zero based.
*
* @param index the index of the item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void select (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index == -1) return; 
	if (OS.XmListPosSelected (handle, index + 1)) return;
	/*
	* Feature in MOTIF.  The X/MOTIF 1.2 spec says that XmListSelectPos ()
	* in a XmEXTENDED_SELECT list widget will add the index to the selected
	* indices.  The observed behavior does not match the spec.  The fix is
	* to temporarily switch the XmNselectionPolicy to XmMULTIPLE_SELECT
	* and then switch it back because XmListSelectPost () works as specified
	* for XmMULTIPLE_SELECT list widgets.
	*/
	int [] argList = {OS.XmNselectionPolicy, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int oldPolicy = argList [1];
	if (oldPolicy == OS.XmEXTENDED_SELECT) {
		argList [1] = OS.XmMULTIPLE_SELECT;
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
	/*
	* Note:  We rely on the fact that XmListSelectPos ()
	* fails silently when the indices are out of range.
	*/
	OS.XmListSelectPos (handle, index + 1, false);
	if (oldPolicy == OS.XmEXTENDED_SELECT) {
		argList [1] = OS.XmEXTENDED_SELECT;
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
}
/**
* Selects a range of items.
* <p>
* If the item at an index is not selected, it is
* selected.  If the item at an index is selected,
* it remains selected.  Indices that are out of
* range are ignored.
*
* Indexing is zero based.  The range of items
* is from the start index up to and including
* the end index.
*
* @param start the start of the range
* @param end the end of the range
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void select (int start, int end) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (start > end) return;
	if ((style & SWT.SINGLE) != 0) {
		int [] argList = {OS.XmNitemCount, 0};
		OS.XtGetValues (handle, argList, argList.length / 2);
		int index = Math.min (argList[1] - 1, end) + 1;
		if (index != 0 && index >= start) OS.XmListSelectPos (handle, index, false);
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
	int [] argList = {OS.XmNselectionPolicy, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int oldPolicy = argList [1];
	if (oldPolicy == OS.XmEXTENDED_SELECT) {
		argList [1] = OS.XmMULTIPLE_SELECT;
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
	/*
	* Note:  We rely on the fact that XmListSelectPos ()
	* fails silently when the indices are out of range.
	*/
	for (int i=start; i<=end; i++) {
		int index = i + 1;
		if ((index != 0) && !OS.XmListPosSelected (handle, index)) {
			OS.XmListSelectPos (handle, index, false);
		}
	}
	if (oldPolicy == OS.XmEXTENDED_SELECT) {
		argList [1] = OS.XmEXTENDED_SELECT;
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
}
/**
* Selects items.
* <p>
* If the item at an index is not selected, it is
* selected.  If the item at an index is selected,
* it remains selected.  Indices that are out of
* range are ignored.  Indexing is zero based.
*
* @param indices the indices of the items
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when the indices are null
*/
public void select (int [] indices) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
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
	/*
	* Feature in MOTIF.  The X/MOTIF 1.2 spec says that XmListSelectPos ()
	* in a XmEXTENDED_SELECT list widget will add the index to the selected
	* indices.  The observed behavior does not match the spec.  The fix is
	* to temporarily switch the XmNselectionPolicy to XmMULTIPLE_SELECT
	* and then switch it back because XmListSelectPos () works as specified
	* for XmMULTIPLE_SELECT list widgets.
	*/
	int [] argList = {OS.XmNselectionPolicy, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int oldPolicy = argList [1];
	if (oldPolicy == OS.XmEXTENDED_SELECT) {
		argList [1] = OS.XmMULTIPLE_SELECT;
		OS.XtSetValues (handle, argList, argList.length / 2);
	}
	/*
	* Note:  We rely on the fact that XmListSelectPos ()
	* fails silently when the indices are out of range.
	*/
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
}
void select (String [] items) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] table = new int [items.length];
	for (int i=0; i<items.length; i++) {
		String string = items [i];
		byte [] buffer = Converter.wcsToMbcs (null, string, true);
		int xmString = OS.XmStringCreateLocalized (buffer);
		table [i] = xmString;
	}
	int ptr = OS.XtMalloc (items.length * 4);
	OS.memmove (ptr, table, items.length * 4);
	int [] argList = {OS.XmNselectedItems, ptr, OS.XmNselectedItemCount, table.length};
	OS.XtSetValues (handle, argList, argList.length / 2);
	for (int i=0; i<table.length; i++) OS.XmStringFree (table [i]);
	OS.XtFree (ptr);
	OS.XmListUpdateSelectedList (handle);
}
/**
* Selects all items.
* <p>
* If an item is not selected, it is selected.
* If an item is selected, it remains selected.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void selectAll () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((style & SWT.SINGLE) != 0) return;
	/*
	* Feature in MOTIF.  The X/MOTIF 1.2 spec says that XmListSelectPos ()
	* in a XmEXTENDED_SELECT list widget will add the index to the selected
	* indices.  The observed behavior does not match the spec.  The fix is
	* to temporarily switch the XmNselectionPolicy to XmMULTIPLE_SELECT
	* and then switch it back because XmListSelectPos () works as specified
	* for XmMULTIPLE_SELECT list widgets.
	*/
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
}
/**
* Sets the bounds.
*/
public void setBounds (int x, int y, int width, int height) {
	super.setBounds (x, y, width, height);
	/*
	* Bug in AIX.  When the receiver has a vertical scroll bar
	* that is currently not visible and no horizontal scroll bar
	* and is resized to be smaller in both the width and height
	* and goes from the state where the width of the longest item
	* is smaller than the width of the list to the state where the
	* width of the longest item is longer than the width of the
	* list, the list hides the vertical scroll bar and leaves a
	* blank space where it should be.  This often happens when a
	* shell containing a list that matches the above criteria is
	* maximized and then restored.  This is just one of a number
	* of repeatable cases where the scrolled window hides the
	* scroll bars but does not resize the list.  The fix is to
	* detect these cases and force the scroll bars to be layed
	* out properly by growing and then shrinking the scrolled
	* window.
	*/
//	fixHScroll := hScroll ~~ nil and: [
//		hScroll isVisible not and: [
//			height ~~ (self dimensionAt: XmNheight)]].
//	fixVScroll := vScroll ~~ nil and: [
//		vScroll isVisible not and: [
//			width ~~ (self dimensionAt: XmNwidth)]].
//	(fixHScroll or: [fixVScroll]) ifFalse: [^self].
	
	/* Grow and shrink the scrolled window by one pixel */
	if (scrolledHandle == 0) return;
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
	OS.XtResizeWidget (scrolledHandle, argList [1] + 1, argList [3], argList [5]);
	OS.XtResizeWidget (scrolledHandle, argList [1], argList [3], argList [5]);
}
void setFocusIndex (int index) {
	OS.XmListSetKbdItemPos (handle, index + 1);
}
/**
* Sets the text of an item.
* <p>
* Indexing is zero based.
*
* This operation will fail when the index is out
* of range or an item could not be changed in
* the OS.
*
* @param index the index for the item
* @param string the item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when items is null
* @exception SWTError(ERROR_ITEM_NOT_MODIFIED)
*	when the operation fails
*/
public void setItem (int index, String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	int [] argList = {OS.XmNitemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	if (!(0 <= index && index < argList [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int xmString = OS.XmStringCreateLocalized (buffer);
	if (xmString == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	boolean isSelected = OS.XmListPosSelected (handle, index + 1);
	OS.XmListReplaceItemsPosUnselected (handle, new int [] {xmString}, 1, index + 1);
	if (isSelected) OS.XmListSelectPos (handle, index + 1, false);
	OS.XmStringFree (xmString);
}
/**
* Sets all items.
* <p>
* The previous selection is cleared.
* The previous items are deleted.
* The new items are added.
* The top index is set to 0.
*
* @param items the array of items
*
* This operation will fail when an item is null
* or could not be added in the OS.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when items is null
*/
public void setItems (String [] items) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
	OS.XmListSetPos (handle, 0);
	OS.XmListDeselectAllItems (handle);
	if ((style & SWT.H_SCROLL) != 0) {
		OS.XmListDeleteAllItems (handle);
	}
	int index = 0;
	int [] table = new int [items.length];
	while (index < items.length) {
		String string = items [index];
		if (string == null) break; 
		byte [] buffer = Converter.wcsToMbcs (null, string, true);
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
	/*
	* Bug in Motif.  Resize the list to work around
	* the horizontal scroll bar display bug described
	* above.
	*/
	if ((style & SWT.H_SCROLL) != 0) {
		OS.XtResizeWindow (handle);
	}
	if (index < items.length) error (SWT.ERROR_ITEM_NOT_ADDED);
}
/**
* Sets the selection.
* <p>
* The previous selection is cleared
* before new items are selected.
*
* @see List#deselectAll()
* @see List#select(int)
*
* @param index the index of the item
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setSelection (int index) {
	if ((style & SWT.MULTI) != 0) deselectAll ();
	select (index);
}
/**
* Sets the selection.
* <p>
* The previous selection is cleared
* before new items are selected.
*
* @see List#deselectAll()
* @see List#select(int, int)
*
* @param start the start of the range
* @param end the end of the range
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setSelection (int start, int end) {
	if ((style & SWT.MULTI) != 0) deselectAll ();
	select (start, end);
}
/**
* Sets the selection.
* <p>
* The previous selection is cleared
* before new items are selected.
*
* @see List#deselectAll()
* @see List#select(int [])
*
* @param indices the indices of the items
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setSelection(int[] indices) {
	if ((style & SWT.MULTI) != 0) deselectAll ();
	select (indices);
}
/**
* Sets the selection.
* <p>
* The previous selection is cleared
* before new items are selected.
*
* @see List#deselectAll()
* @see List#select(int [])
*
* @param items the array of items
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
* @exception SWTError(ERROR_NULL_ARGUMENT)
*	when items is null
*/
public void setSelection (String [] items) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SINGLE) != 0) {
		for (int i=items.length-1; i>=0; --i) {
			String string = items [i];
			if (string != null) {
				byte [] buffer = Converter.wcsToMbcs (null, string, true);
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
			byte [] buffer = Converter.wcsToMbcs (null, string, true);
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
}
/**
* Sets the size.
*/
public void setSize (int width, int height) {
	super.setSize (width, height);
	/*
	* Bug in AIX.  When the receiver has a vertical scroll bar
	* that is currently not visible and no horizontal scroll bar
	* and is resized to be smaller in both the width and height
	* and goes from the state where the width of the longest item
	* is smaller than the width of the list to the state where the
	* width of the longest item is longer than the width of the
	* list, the list hides the vertical scroll bar and leaves a
	* blank space where it should be.  This often happens when a
	* shell containing a list that matches the above criteria is
	* maximized and then restored.  This is just one of a number
	* of repeatable cases where the scrolled window hides the
	* scroll bars but does not resize the list.  The fix is to
	* detect these cases and force the scroll bars to be layed
	* out properly by growing and then shrinking the scrolled
	* window.
	*/
//	fixHScroll := hScroll ~~ nil and: [
//		hScroll isVisible not and: [
//			height ~~ (self dimensionAt: XmNheight)]].
//	fixVScroll := vScroll ~~ nil and: [
//		vScroll isVisible not and: [
//			width ~~ (self dimensionAt: XmNwidth)]].
//	(fixHScroll or: [fixVScroll]) ifFalse: [^self].

	/* Grow and shrink the scrolled window by one pixel */
	if (scrolledHandle == 0) return;
	int [] argList = {OS.XmNwidth, 0, OS.XmNheight, 0, OS.XmNborderWidth, 0};
	OS.XtGetValues (scrolledHandle, argList, argList.length / 2);
	OS.XtResizeWidget (scrolledHandle, argList [1] + 1, argList [3], argList [5]);
	OS.XtResizeWidget (scrolledHandle, argList [1], argList [3], argList [5]);
}
/**
* Sets the top index.
* <p>
* The top index is the index of the item that
* is currently at the top of the widget.  The
* top index changes when the widget is scrolled.
* Indexing starts from zero.
*
* @param index the new top index
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void setTopIndex (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] argList = {OS.XmNitemCount, 0, OS.XmNvisibleItemCount, 0};
	OS.XtGetValues (handle, argList, argList.length / 2);
	int newIndex = Math.max (1, Math.min (index + 1, argList [1]));
	int lastIndex = Math.max (1, argList [1] - argList [3] + 1);
	if (newIndex > lastIndex) newIndex = lastIndex;
	OS.XmListSetPos (handle, newIndex);
}
/**
* Shows the selection.
* <p>
* If there is no selection or the selection
* is already visible, this method does nothing.
* If the selection is scrolled out of view,
* the top index of the widget is changed such
* that selection becomes visible.
*
* @exception SWTError(ERROR_THREAD_INVALID_ACCESS)
*	when called from the wrong thread
* @exception SWTError(ERROR_WIDGET_DISPOSED)
*	when the widget has been disposed
*/
public void showSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
}
int topHandle () {
	/*
	* Normally, when a composite has both a form handle
	* and a scrolled handle, the scrolled handle is the
	* parent of the form handle.  In the case of a list,
	* both handles are present but the form handle is
	* parent of the scrolled handle and therefore is
	* the top handle of the list.
	*/
	return formHandle;
}
}
