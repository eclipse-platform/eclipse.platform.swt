package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.DataBrowserCallbacks;
import org.eclipse.swt.internal.carbon.DataBrowserListViewColumnDesc;
import org.eclipse.swt.internal.carbon.Rect;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public class List extends Scrollable {
	String [] items;
	int itemCount, anchorFirst, anchorLast;
	boolean ignoreSelect;
	static final int COLUMN_ID = 1024;

public List (Composite parent, int style) {
	super (parent, checkStyle (style));
}

public void add (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int [] id = new int [] {itemCount + 1};
	if (OS.AddDataBrowserItems (handle, OS.kDataBrowserNoItem, 1, id, 0) != OS.noErr) {
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	if (itemCount == items.length) {
		String [] newItems = new String [itemCount + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	items [itemCount++] = string;
}

public void add (String string, int index) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	int [] id = new int [] {itemCount + 1};
	if (OS.AddDataBrowserItems (handle, OS.kDataBrowserNoItem, 1, id, 0) != OS.noErr) {
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	if (itemCount == items.length) {
		String [] newItems = new String [itemCount + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = string;
	OS.UpdateDataBrowserItems (handle, 0, 0, null, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
}

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
	int width = 0;
	if (wHint == SWT.DEFAULT) {
		GC gc = new GC (this);
		for (int i=0; i<itemCount; i++) {
			Point extent = gc.stringExtent (items [i]);
			width = Math.max (width, extent.x);
		}
		gc.dispose ();
	} else {
		width = wHint;
	}
	if (width <= 0) width = DEFAULT_WIDTH;
	int height = 0;
	if (hHint == SWT.DEFAULT) {
		height = itemCount * getItemHeight ();
	} else {
		height = hHint;
	}
	if (height <= 0) height = DEFAULT_HEIGHT;
	Rectangle rect = computeTrim (0, 0, width, height);
	return new Point (rect.width, rect.height);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	Rect rect = new Rect ();
	OS.GetDataBrowserScrollBarInset (handle, rect);
	x -= rect.left;
	y -= rect.top;
	width += (rect.left + rect.right) * 3;
	height += rect.top + rect.bottom;
	return new Rectangle (x, y, width, height);
}

void createHandle () {
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
	OS.CreateDataBrowserControl (window, null, OS.kDataBrowserListView, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
	int selectionFlags = (style & SWT.SINGLE) != 0 ? OS.kDataBrowserSelectOnlyOne : OS.kDataBrowserCmdTogglesSelection;
	OS.SetDataBrowserSelectionFlags (handle, selectionFlags);
	OS.SetDataBrowserListViewHeaderBtnHeight (handle, (short) 0);
	OS.SetDataBrowserHasScrollBars (handle, (style & SWT.H_SCROLL) != 0, (style & SWT.V_SCROLL) != 0);
	//NOT DONE
	if ((style & SWT.H_SCROLL) == 0) OS.AutoSizeDataBrowserListViewColumns (handle);
	DataBrowserListViewColumnDesc column = new DataBrowserListViewColumnDesc ();
	column.headerBtnDesc_version = OS.kDataBrowserListViewLatestHeaderDesc;
	column.propertyDesc_propertyID = COLUMN_ID;
	column.propertyDesc_propertyType = OS.kDataBrowserTextType;
	column.propertyDesc_propertyFlags = OS.kDataBrowserListViewSelectionColumn | OS.kDataBrowserDefaultPropertyFlags;
	//NOT DONE
	column.headerBtnDesc_maximumWidth= 0x7FFF;
	column.headerBtnDesc_initialOrder= OS.kDataBrowserOrderIncreasing;
	OS.AddDataBrowserListViewColumn (handle, column, 0);
	//NOT DONE
	OS.SetDataBrowserTableViewNamedColumnWidth (handle, COLUMN_ID, (short)800);

	/*
	* Feature in the Macintosh.  Scroll bars are not created until
	* the widget has a minimum size.  The fix is to force the scroll
	* bars to be created by temporarily giving the widget a size and
	* then restoring it to zero.
	* 
	* NOTE: The widget must be visible and SizeControl() must be used
	* to resize the widget to a minimim size or the widget will not
	* create the scroll bars.  This work around currently flashes.
	*/
	OS.SizeControl (handle, (short) 0xFF, (short) 0xFF);
	OS.SizeControl (handle, (short) 0, (short) 0);
}

void createWidget () {
	super.createWidget ();
	items = new String [4];
}

ScrollBar createScrollBar (int style) {
	return createStandardBar (style);
}

int defaultThemeFont () {	
	return OS.kThemeViewsFont;
}

public void deselect (int index) {
	checkWidget();
	if (0 < index && index < itemCount) {
		ignoreSelect = true;
		int [] id = new int [] {index + 1};
		OS.SetDataBrowserSelectedItems (handle, id.length, id, OS.kDataBrowserItemsRemove);
		ignoreSelect = false;
	}
}

public void deselect (int start, int end) {
	checkWidget();
	//NOT DONE - range check
	int length = end - start + 1;
	if (length <= 0) return;
	int [] ids = new int [length];
	for (int i=0; i<length; i++) ids [i] = end - i + 1;
	ignoreSelect = true;
	OS.SetDataBrowserSelectedItems (handle, length, ids, OS.kDataBrowserItemsRemove);
	ignoreSelect = false;
}

public void deselect (int [] indices) {
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	//NOT DONE - range check
	int length = indices.length;
	int [] ids = new int [length];
	for (int i=0; i<length; i++) ids [i] = indices [length - i - 1] + 1;
	ignoreSelect = true;
	OS.SetDataBrowserSelectedItems (handle, length, ids, OS.kDataBrowserItemsRemove);
	ignoreSelect = false;
}

public void deselectAll () {
	checkWidget ();
	ignoreSelect = true;
	OS.SetDataBrowserSelectedItems (handle, 0, null, OS.kDataBrowserItemsRemove);
	ignoreSelect = false;
}

public Rectangle getClientArea () {
	checkWidget();
	Rect rect = new Rect (), inset = new Rect ();
	OS.GetControlBounds (handle, rect);
	OS.GetDataBrowserScrollBarInset (handle, inset);
	return new Rectangle (inset.left, inset.top, rect.right - rect.left + inset.right, rect.bottom - rect.top + inset.bottom);
}

public int getFocusIndex () {
	checkWidget();
	int [] first = new int [1], last = new int [1];
	if (OS.GetDataBrowserSelectionAnchor (handle, first, last) != OS.noErr) return -1;
    return first [0] - 1;
}

public String getItem (int index) {
	checkWidget();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	return items [index];
}

public int getItemCount () {
	checkWidget();
	return itemCount;
}

public int getItemHeight () {
	checkWidget ();
	short [] height = new short [1];
	if (OS.GetDataBrowserTableViewRowHeight (handle, height) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_ITEM_HEIGHT);
	}
	return height [0];
}

public String [] getItems () {
	checkWidget();
    String [] result = new String [itemCount];
	System.arraycopy (items, 0, result, 0, itemCount);
	return result;
}

public String [] getSelection () {
	checkWidget ();
	int ptr = OS.NewHandle (0);
	if (OS.GetDataBrowserItems (handle, OS.kDataBrowserNoItem, true, OS.kDataBrowserItemIsSelected, ptr) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_SELECTION);
	}
	int count = OS.GetHandleSize (ptr) / 4;
	String [] result = new String [count];
	OS.HLock (ptr);
	int [] start = new int [1];
	OS.memcpy (start, ptr, 4);
	int [] id = new int [1];
	for (int i=0; i<count; i++) {
		OS.memcpy (id, start [0] + (i * 4), 4);
		result [i] = items [id [0] - 1];
	}
	OS.HUnlock (ptr);
	OS.DisposeHandle (ptr);
	return result;
}

public int getSelectionCount () {
	checkWidget ();
	int [] count = new int [1];
	if (OS.GetDataBrowserItemCount (handle, OS.kDataBrowserNoItem, true, OS.kDataBrowserItemIsSelected, count) != OS.noErr) {
		error (SWT.ERROR_CANNOT_GET_COUNT);
	}
	return count [0];
}

public int getSelectionIndex () {
	checkWidget();
	int [] first = new int [1], last = new int [1];
	if (OS.GetDataBrowserSelectionAnchor (handle, first, last) != OS.noErr) return -1;
    return first [0] - 1;
}

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

public int getTopIndex () {
	checkWidget();
    int[] top = new int [1], left = new int [1];
    OS.GetDataBrowserScrollPosition (handle, top, left);
    return top [0] / getItemHeight ();
}

void hookEvents () {
	super.hookEvents ();
	Display display= getDisplay();
	DataBrowserCallbacks callbacks = new DataBrowserCallbacks ();
	callbacks.version = OS.kDataBrowserLatestCallbacks;
	OS.InitDataBrowserCallbacks (callbacks);
	callbacks.v1_itemDataCallback = display.itemDataProc;
	callbacks.v1_itemNotificationCallback = display.itemNotificationProc;
	OS.SetDataBrowserCallbacks (handle, callbacks);
}

int itemDataProc (int browser, int id, int property, int itemData, int setValue) {
	int index = id - 1;
	switch (property) {
		case COLUMN_ID: {
			String text = items [index];
			char [] buffer = new char [text.length ()];
			text.getChars (0, buffer.length, buffer, 0);
			int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
			if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
			OS.SetDataBrowserItemDataText (itemData, ptr);
			OS.CFRelease (ptr);
			break;
		}
	}
	return OS.noErr;
}

int kEventMouseDown (int nextHandler, int theEvent, int userData) {
	int result = super.kEventMouseDown (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	/*
	* Feature in the Macintosh.  For some reason, when the user
	* clicks on the data browser, focus is assigned, then lost
	* and then reassigned causing kEvenControlSetFocusPart events.
	* The fix is to ignore kEvenControlSetFocusPart when the user
	* clicks and send the focus events from kEventMouseDown.
	*/
	Display display = getDisplay ();
	Control oldFocus = display.getFocusControl ();
	display.ignoreFocus = true;
	result = OS.CallNextEventHandler (nextHandler, theEvent);
	display.ignoreFocus = false;
	if (oldFocus != this) {
		if (oldFocus != null && !oldFocus.isDisposed ()) oldFocus.sendFocusEvent (false);
		if (!isDisposed () && isEnabled ()) sendFocusEvent (true);
	}
	return result;
}

int kEventRawKeyDown (int nextHandler, int theEvent, int userData) {
	int result = super.kEventRawKeyDown (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	/*
	* Feature in the Macintosh.  For some reason, when the user hits an
	* up or down arrow to traverse the items in a Data Browser, the item
	* scrolls to the left such that the white space that is normally
	* visible to the right of the every item is scrolled out of view.
	* The fix is to do the arrow traversal in Java and not call the
	* default handler.
	*/
	int [] keyCode = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
	switch (keyCode [0]) {
		case 125: { /* Down */
			int index = getSelectionIndex ();
			setSelection (Math.min (itemCount - 1, index + 1));
			return OS.noErr;
		}
		case 126: { /* Up*/
			int index = getSelectionIndex ();
			setSelection (Math.max (0, index - 1));
			return OS.noErr;
		}
	}
	return OS.eventNotHandledErr;
}

int kEventRawKeyRepeat (int nextHandler, int theEvent, int userData) {
	int result = super.kEventRawKeyRepeat (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	/*
	* Feature in the Macintosh.  For some reason, when the user hits an
	* up or down arrow to traverse the items in a Data Browser, the item
	* scrolls to the left such that the white space that is normally
	* visible to the right of the every item is scrolled out of view.
	* The fix is to do the arrow traversal in Java and not call the
	* default handler.
	*/
	int [] keyCode = new int [1];
	OS.GetEventParameter (theEvent, OS.kEventParamKeyCode, OS.typeUInt32, null, keyCode.length * 4, null, keyCode);
	switch (keyCode [0]) {
		case 125: { /* Down */
			int index = getSelectionIndex ();
			setSelection (Math.min (itemCount - 1, index + 1));
			return OS.noErr;
		}
		case 126: { /* Up*/
			int index = getSelectionIndex ();
			setSelection (Math.max (0, index - 1));
			return OS.noErr;
		}
	}
	return OS.eventNotHandledErr;
}

int itemNotificationProc (int browser, int id, int message) {
	switch (message) {
		case OS.kDataBrowserItemSelected:
		case OS.kDataBrowserItemDeselected: {
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
				postEvent (SWT.Selection);
			}
			break;
		}	
		case OS.kDataBrowserItemDoubleClicked: {
			postEvent (SWT.DefaultSelection);
			break;
		}
	}
	return OS.noErr;
}

public int indexOf (String item) {
	checkWidget();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<itemCount; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

public int indexOf (String string, int start) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=start; i<itemCount; i++) {
		if (items [i] == string) return i;
	}
	return -1;
}

public boolean isSelected (int index) {
	checkWidget();
	return OS.IsDataBrowserItemSelected (handle, index + 1);
}

public void remove (int index) {
	checkWidget();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	int [] id = new int [] {itemCount};
	if (OS.RemoveDataBrowserItems (handle, OS.kDataBrowserNoItem, id.length, id, 0) != OS.noErr) {
		error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	OS.UpdateDataBrowserItems (handle, 0, 0, null, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
}

public void remove (int start, int end) {
	checkWidget();
	if (!(0 <= start && start <= end && end < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int length = end - start + 1;
	for (int i=0; i<length; i++) remove (start);
}

public void remove (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int index = indexOf (string, 0);
	if (index == -1) error (SWT.ERROR_INVALID_ARGUMENT);
	remove (index);
}

public void remove (int [] indices) {
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int [] newIndices = new int [indices.length];
	System.arraycopy (indices, 0, newIndices, 0, indices.length);
	sort (newIndices);
	int last = -1;
	for (int i=0; i<newIndices.length; i++) {
		int index = newIndices [i];
		if (index != last || i == 0) remove (index);
		last = index;
	}
}

public void removeAll () {
	checkWidget();
	OS.RemoveDataBrowserItems (handle, OS.kDataBrowserNoItem, 0, null, 0);
	items = new String [4];
	itemCount = anchorFirst = anchorLast = 0;
}

public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);
}

public void select (int index) {
	checkWidget();
	if (0 <= index && index < itemCount) {
		int [] id = new int [] {index + 1};
		ignoreSelect = true;
		int operation = (style & SWT.SINGLE) != 0 ? OS.kDataBrowserItemsAssign: OS.kDataBrowserItemsAdd;
		OS.SetDataBrowserSelectedItems (handle, id.length, id, operation);
		ignoreSelect = false;
	}
}

public void select (int start, int end) {
	checkWidget();
	//NOT DONE - range check
	int length = end - start + 1;
	if (length <= 0) return;
	int [] ids = new int [length];
	for (int i=0; i<length; i++) ids [i] = end - i + 1;
	ignoreSelect = true;
	int operation = (style & SWT.SINGLE) != 0 ? OS.kDataBrowserItemsAssign: OS.kDataBrowserItemsAdd;
	OS.SetDataBrowserSelectedItems (handle, length, ids, operation);
	ignoreSelect = false;
}

public void select (int [] indices) {
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	//NOT DONE - range check
	int length = indices.length;
	int [] ids = new int [length];
	for (int i=0; i<length; i++) ids [i] = indices [length - i - 1] + 1;
	ignoreSelect = true;
	int operation = (style & SWT.SINGLE) != 0 ? OS.kDataBrowserItemsAssign: OS.kDataBrowserItemsAdd;
	OS.SetDataBrowserSelectedItems (handle, ids.length, ids, operation);
	ignoreSelect = false;
}

void select (String [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	//NOT DONE - range check
	int length = items.length;
	int [] ids = new int [length];
	for (int i=0; i<length; i++) ids [i] = indexOf (items [length - i - 1]) + 1;
	ignoreSelect = true;
	int operation = (style & SWT.SINGLE) != 0 ? OS.kDataBrowserItemsAssign: OS.kDataBrowserItemsAdd;
	OS.SetDataBrowserSelectedItems (handle, length, ids, operation);
	ignoreSelect = false;
}

public void selectAll () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return;
	ignoreSelect = true;
	OS.SetDataBrowserSelectedItems (handle, 0, null, OS.kDataBrowserItemsAssign);
	ignoreSelect = false;
}

public void setItem (int index, String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	int [] id = new int [] {index + 1};
	items [index] = string;
    OS.UpdateDataBrowserItems (handle, OS.kDataBrowserNoItem, id.length, id, OS.kDataBrowserItemNoProperty, OS.kDataBrowserNoItem);
}

public void setItems (String [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	OS.RemoveDataBrowserItems (handle, OS.kDataBrowserNoItem, 0, null, 0);
	if (OS.AddDataBrowserItems(handle, OS.kDataBrowserNoItem, items.length, null, 0) != OS.noErr) {
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	this.items = new String [items.length];
	System.arraycopy (items, 0, this.items, 0, items.length);
	itemCount = items.length;
}

public void setSelection (int index) {
	checkWidget();
	if (0 <= index && index < itemCount) {
		int [] id = new int [] {index + 1};
		ignoreSelect = true;
		OS.SetDataBrowserSelectedItems (handle, id.length, id, OS.kDataBrowserItemsAssign);
		ignoreSelect = false;
		showIndex (index);
	}
}

public void setSelection (int start, int end) {
	checkWidget ();
	int length = end - start + 1;
	if (length <= 0) return;
	int count = length;
	int [] ids = new int [length];
	for (int i=start; i<=end; i++) {
		if (0 <= i && i < itemCount) ids [--count] = i + 1;
	}
	if (count != 0) return;
	ignoreSelect = true;
	OS.SetDataBrowserSelectedItems (handle, count, ids, OS.kDataBrowserItemsAssign);
	ignoreSelect = false;
	if (ids.length > 0) showIndex (ids [0] - 1);
}

public void setSelection (int [] indices) {
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = indices.length;
	int count = length;
	int [] ids = new int [length];
	for (int i=0; i<length; i++) {
		int index = indices [i];
		if (0 <= index && index < itemCount) ids [--count] = index + 1;
	}
	if (count != 0) return;
	ignoreSelect = true;
	OS.SetDataBrowserSelectedItems (handle, count, ids, OS.kDataBrowserItemsAssign);
	ignoreSelect = false;
	if (ids.length > 0) showIndex (ids [0] - 1);
}

public void setSelection (String [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = items.length;
	int count = length;
	int [] ids = new int [length];
	for (int i=0; i<length; i++) {
		int index = indexOf (items [i]);
		if (0 <= index && index < itemCount) ids [--count] = index + 1;
	}
	if (count != 0) return;
	ignoreSelect = true;
	OS.SetDataBrowserSelectedItems (handle, count, ids, OS.kDataBrowserItemsAssign);
	ignoreSelect = false;
	if (ids.length > 0) showIndex (ids [0] - 1);
}

public void setTopIndex (int index) {
	checkWidget();
    int [] top = new int [1], left = new int [1];
    OS.GetDataBrowserScrollPosition (handle, top, left);
    top [0] = index * getItemHeight ();
    OS.SetDataBrowserScrollPosition (handle, top [0], left [0]);
}

void showIndex (int index) {
	if (0 <= index && index < itemCount) {
		short [] width = new short [1];
		OS.GetDataBrowserTableViewNamedColumnWidth (handle, COLUMN_ID, width);
		Rect rect = new Rect (), inset = new Rect ();
		OS.GetControlBounds (handle, rect);
		OS.GetDataBrowserScrollBarInset (handle, inset);
		OS.SetDataBrowserTableViewNamedColumnWidth (handle, COLUMN_ID, (short)(rect.right - rect.left - inset.left - inset.right));
		OS.RevealDataBrowserItem (handle, index + 1, COLUMN_ID, (byte) OS.kDataBrowserRevealWithoutSelecting);
		OS.SetDataBrowserTableViewNamedColumnWidth (handle, COLUMN_ID, (short)width [0]);
	}
}

public void showSelection () {
	checkWidget();
	int index = getSelectionIndex ();
	if (index >= 0) showIndex (index);
}

}
