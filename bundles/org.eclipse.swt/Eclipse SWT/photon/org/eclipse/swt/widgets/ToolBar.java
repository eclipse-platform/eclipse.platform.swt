package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public /*final*/ class ToolBar extends Composite {
	int itemCount;
	ToolItem [] items;

public ToolBar (Composite parent, int style) {
	super (parent, checkStyle (style));

	/*
	* Ensure that either of HORIZONTAL or VERTICAL is set.
	* NOTE: HORIZONTAL and VERTICAL have the same values
	* as H_SCROLL and V_SCROLL so it is necessary to first
	* clear these bits to avoid scroll bars and then reset
	* the bits using the original style supplied by the
	* programmer.
	*/
	this.style = checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
	int [] args = {
		OS.Pt_ARG_ORIENTATION, (style & SWT.VERTICAL) == 0 ? OS.Pt_HORIZONTAL : OS.Pt_VERTICAL, 0,
	};
	OS.PtSetResources(handle, args.length / 3, args);
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

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
//	if (layout != null) return super.computeSize (wHint, hHint, changed);
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	PhDim_t dim = new PhDim_t();
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidgetFamily (handle);
	OS.PtWidgetPreferredSize(handle, dim);
	int width = dim.w, height = dim.h;
	if (wHint != SWT.DEFAULT || hHint != SWT.DEFAULT) {
		PhRect_t rect = new PhRect_t ();
		PhArea_t area = new PhArea_t ();
		rect.lr_x = (short) (wHint - 1);
		rect.lr_y = (short) (hHint - 1);
		OS.PtSetAreaFromWidgetCanvas (handle, rect, area);
		if (wHint != SWT.DEFAULT) width = area.size_w;
		if (hHint != SWT.DEFAULT) height = area.size_h;
	}
	return new Point(width, height);
}

void createHandle (int index) {
	state |= HANDLE;
	Display display = getDisplay ();
	int parentHandle = parent.handle;
	
	int [] args = {
		OS.Pt_ARG_FLAGS, hasBorder () ? OS.Pt_HIGHLIGHTED : 0, OS.Pt_HIGHLIGHTED,
		OS.Pt_ARG_TOOLBAR_FLAGS, 0, OS.Pt_TOOLBAR_DRAGGABLE | OS.Pt_TOOLBAR_END_SEPARATOR,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (OS.PtToolbar (), parentHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

void createItem (ToolItem item, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	if (itemCount == items.length) {
		ToolItem [] newItems = new ToolItem [itemCount + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	item.createWidget (index);
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
}

void createWidget (int index) {
	super.createWidget (index);
	items = new ToolItem [4];
	itemCount = 0;
}

void destroyItem (ToolItem item) {
	int index = 0;
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	if (index == itemCount) return;
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
}

public int getItemCount () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return itemCount;
}

public ToolItem [] getItems () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	ToolItem [] result = new ToolItem [itemCount];
	System.arraycopy (items, 0, result, 0, itemCount);
	return result;
}

public ToolItem getItem (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int count = itemCount;
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);	
	return items [index];
}

public ToolItem getItem (Point pt) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	ToolItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		Rectangle rect = items [i].getBounds ();
		if (rect.contains (pt)) return items [i];
	}
	return null;
}

public int getRowCount () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return 1;
}

public int indexOf (ToolItem item) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = itemCount;
	for (int i=0; i<count; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

void releaseWidget () {
	for (int i=0; i<items.length; i++) {
		ToolItem item = items [i];
		if (item != null && !item.isDisposed ()) {
			item.releaseWidget ();
			item.releaseHandle ();
		}
	}
	items = null;
	super.releaseWidget ();
}

}
