package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;


public /*final*/ class TabFolder extends Composite {
	TabItem [] items;

public TabFolder (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}

public void addSelectionListener(SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	PhDim_t dim = new PhDim_t();
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidget (handle);
	OS.PtWidgetPreferredSize(handle, dim);
	int width = dim.w, height = dim.h;
	Point size;
	if (layout != null) {
		size = layout.computeSize (this, wHint, hHint, changed);
	} else {
		size = minimumSize ();
	}
	if (size.x == 0) size.x = DEFAULT_WIDTH;
	if (size.y == 0) size.y = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	width = Math.max (width, size.x);
	height = Math.max (height, size.y);
	Rectangle trim = computeTrim (0, 0, width, height);
	width = trim.width;  height = trim.height;
	return new Point (width, height);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	PhDim_t dim = new PhDim_t();
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidget (handle);
	OS.PtWidgetPreferredSize(handle, dim);
	int [] args = {
		OS.Pt_ARG_MARGIN_BOTTOM, 0, 0, // 1
		OS.Pt_ARG_MARGIN_TOP, 0, 0, // 4
		OS.Pt_ARG_MARGIN_RIGHT, 0, 0, // 7
		OS.Pt_ARG_MARGIN_LEFT, 0, 0, // 10
//		OS.Pt_ARG_BASIC_FLAGS, 0, 0, // 13
	};
	OS.PtGetResources(handle, args.length / 3, args);
	int trimX = x - args [10];
	int trimY = y - (dim.h - args [1]);
	int trimWidth = width + args [7] + args [10];
	int trimHeight = height + dim.h;
	return new Rectangle (trimX, trimY, trimWidth, trimHeight);
}

void createHandle (int index) {
	state |= HANDLE;
	Display display = getDisplay ();
	int clazz = display.PtPanelGroup;
	int parentHandle = parent.handle;
	int [] args = {
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (clazz, parentHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

void createWidget (int index) {
	super.createWidget (index);
	items = new TabItem [4];
}

void createItem (TabItem item, int index) {
	int [] args = {OS.Pt_ARG_PG_PANEL_TITLES, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int count = args [2];
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	if (count == items.length) {
		TabItem [] newItems = new TabItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	int oldPtr = args [1];
	int newPtr = OS.malloc ((count + 1) * 4);
	if (newPtr == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	int offset = 0;
	for (int i=0; i<count; i++) {
		if (i == index) offset = 1;
		int [] address = new int [1];
		OS.memmove (address, oldPtr + (i * 4), 4);
		int length = OS.strlen (address [0]);
		int str = OS.malloc (length + 1);
		if (str == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
		OS.memmove (str, address [0], length + 1);
		OS.memmove (newPtr + ((i + offset) * 4), new int [] {str}, 4);
	}
	int str = OS.malloc (1);
	if (str == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	OS.memmove (newPtr + ((index) * 4), new int [] {str}, 4);
	args = new int [] {OS.Pt_ARG_PG_PANEL_TITLES, newPtr, count + 1};
	OS.PtSetResources (handle, args.length / 3, args);
	for (int i=0; i<count+1; i++) {
		int [] address = new int [1];
		OS.memmove (address, newPtr + (i * 4), 4);
		OS.free (address [0]);
	}
	OS.free (newPtr);
	System.arraycopy (items, index, items, index + 1, count - index);
	items [index] = item;
}

void destroyItem (TabItem item) {
	int [] args = {OS.Pt_ARG_PG_PANEL_TITLES, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int count = args [2];
	int index = 0;
	while (index < count) {
		if (items [index] == item) break;
		index++;
	}
	int oldPtr = args [1];
	int newPtr = OS.malloc ((count - 1) * 4);
	if (newPtr == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
	int offset = 0;
	for (int i=0; i<count; i++) {
		if (i == index) {
			offset = -1;
			continue;
		}
		int [] address = new int [1];
		OS.memmove (address, oldPtr + (i * 4), 4);
		int length = OS.strlen (address [0]);
		int str = OS.malloc (length + 1);
		if (str == 0) error (SWT.ERROR_ITEM_NOT_ADDED);
		OS.memmove (str, address [0], length + 1);
		OS.memmove (newPtr + ((i + offset) * 4), new int [] {str}, 4);
	}
	args = new int [] {OS.Pt_ARG_PG_PANEL_TITLES, newPtr, count - 1};
	OS.PtSetResources (handle, args.length / 3, args);
	for (int i=0; i<count-1; i++) {
		int [] address = new int [1];
		OS.memmove (address, newPtr + (i * 4), 4);
		OS.free (address [0]);
	}
	OS.free (newPtr);
	if (index != count) {
		System.arraycopy (items, index + 1, items, index, --count - index);
	}
	items [count] = null;
}

public TabItem getItem (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_PG_PANEL_TITLES, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (!(0 <= index && index < args [2])) error (SWT.ERROR_INVALID_RANGE);
	return items [index];
}

public TabItem [] getItems () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_PG_PANEL_TITLES, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	TabItem [] result = new TabItem [args [2]];
	System.arraycopy (items, 0, result, 0, result.length);
	return result;
}

public int getItemCount () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_PG_PANEL_TITLES, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [2];
}

public TabItem [] getSelection () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int index = getSelectionIndex ();
	if (index == -1) return new TabItem [0];
	return new TabItem [] {items [index]};
}

public int getSelectionIndex () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_PG_CURRENT_INDEX, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1] == OS.Pt_PG_INVALID ? -1 : args [1];
}

void hookEvents () {
	super.hookEvents ();
	int windowProc = getDisplay ().windowProc;
	OS.PtAddCallback (handle, OS.Pt_CB_PG_PANEL_SWITCHING, windowProc, SWT.Selection);
}

public int indexOf (TabItem item) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = getItemCount ();
	for (int i=0; i<count; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

int processPaint (int damage) {
	OS.PtSuperClassDraw (OS.PtPanelGroup (), handle, damage);
	sendPaintEvent (damage);
	return OS.Pt_CONTINUE;
}

int processResize (int info) {
	int result = super.processResize (info);
	int [] args = {OS.Pt_ARG_PG_CURRENT_INDEX, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int index = args [1];
	if (index != OS.Pt_PG_INVALID) {
		TabItem item = items [index];
		Control control = item.control;
		if (control != null && !control.isDisposed ()) {
			control.setBounds (getClientArea ());
		}
	}
	return result;
}

int processSelection (int info) {
	if (info == 0) return OS.Pt_CONTINUE;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	short[] oldIndex = new short[1];
	short[] newIndex = new short[1];
	OS.memmove(oldIndex, cbinfo.cbdata + 8, 2);
	OS.memmove(newIndex, cbinfo.cbdata + 10, 2);
	int [] args = {OS.Pt_ARG_PG_PANEL_TITLES, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int count = args [2];
	Control oldControl = null;
	int index = oldIndex [0];
	TabItem oldItem = items [index];
	if (0 <= index && index < count) oldControl = oldItem.control;
	Control newControl = null;
	index = newIndex [0];
	TabItem newItem = items [index];
	if (0 <= index && index < count) newControl = newItem.control;
	if (oldControl != null) oldControl.setVisible (false);
	if (newControl != null) {
		newControl.setBounds (getClientArea ());
		newControl.setVisible (true);
	}
	Event event = new Event ();
	event.item = newItem;
	postEvent (SWT.Selection, event);
	return OS.Pt_CONTINUE;
}

void releaseWidget () {
	int [] args = {OS.Pt_ARG_PG_PANEL_TITLES, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int count = args [2];
	for (int i=0; i<count; i++) {
		TabItem item = items [i];
		if (!item.isDisposed ()) {
			item.releaseWidget ();
			item.releaseHandle ();
		}
	}
	items = null;
	super.releaseWidget ();
}

public void removeSelectionListener (SelectionListener listener) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

public void setSelection (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int [] args = {OS.Pt_ARG_PG_CURRENT_INDEX, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int oldIndex = args [1];
	if (oldIndex != OS.Pt_PG_INVALID) {
		TabItem item = items [oldIndex];
		Control control = item.control;
		if (control != null && !control.isDisposed ()) {
			control.setVisible (false);
		}
	}
	args = new int[]{OS.Pt_ARG_PG_CURRENT_INDEX, index, 0};
	OS.PtSetResources (handle, args.length / 3, args);	
	args = new int[]{OS.Pt_ARG_PG_CURRENT_INDEX, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int newIndex = args [1];
	if (newIndex != -1) {
		TabItem item = items [newIndex];
		Control control = item.control;
		if (control != null && !control.isDisposed ()) {
			control.setBounds (getClientArea ());
			control.setVisible (true);
		}
	}
}

public void setSelection (TabItem [] items) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (items.length == 0) {
		setSelection (-1);
		return;
	}
	for (int i=items.length-1; i>=0; --i) {
		int index = indexOf (items [i]);
		if (index != -1) setSelection (index);
	}
}

}
