/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class support the layout of selectable
 * tool bar items.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>ToolItem</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>FLAT, WRAP, RIGHT, HORIZONTAL, VERTICAL, SHADOW_OUT</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#toolbar">ToolBar, ToolItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ToolBar extends Composite {
	int parentingHandle;
	int itemCount;
	ToolItem [] items;
	ToolItem lastFocus;

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
 * @see SWT#FLAT
 * @see SWT#WRAP
 * @see SWT#RIGHT
 * @see SWT#HORIZONTAL
 * @see SWT#SHADOW_OUT
 * @see SWT#VERTICAL
 * @see Widget#checkSubclass()
 * @see Widget#getStyle()
 */
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
	if ((style & SWT.VERTICAL) != 0) {
		this.style |= SWT.VERTICAL;
	} else {
		this.style |= SWT.HORIZONTAL;
	}
	int orientation = (style & SWT.VERTICAL) == 0 ? OS.Pt_HORIZONTAL : OS.Pt_VERTICAL;
	OS.PtSetResource (handle, OS.Pt_ARG_ORIENTATION, orientation, 0);
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

Control [] _getChildren () {
	Control [] children = super._getChildren ();
	int count = 0;
	for (int i=0; i<itemCount; i++) {
		if (items [i].control != null) count++;
	}
	if (count == 0) return children;
	Control [] newChildren = new Control [children.length + count];
	System.arraycopy (children, 0, newChildren, 0, children.length);
	int index = children.length;
	for (int i=0; i<itemCount; i++) {
		ToolItem item = items [i];
		if (item.control != null) newChildren [index++] = item.control;
	}
	return newChildren;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	if (layout != null) {
		return super.computeSize (wHint, hHint, changed);
	}
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
	int parentHandle = parent.parentingHandle ();
	int [] args = {
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	parentingHandle = OS.PtCreateWidget (OS.PtContainer (), parentHandle, args.length / 3, args);
	if (parentingHandle == 0) error (SWT.ERROR_NO_HANDLES);
	args = new int [] {
		OS.Pt_ARG_FLAGS, (style & SWT.NO_FOCUS) != 0 ? 0 : OS.Pt_GETS_FOCUS, OS.Pt_GETS_FOCUS,
		OS.Pt_ARG_FLAGS, hasBorder () ? OS.Pt_HIGHLIGHTED : 0, OS.Pt_HIGHLIGHTED,
		OS.Pt_ARG_TOOLBAR_FLAGS, 0, OS.Pt_TOOLBAR_DRAGGABLE | OS.Pt_TOOLBAR_END_SEPARATOR,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (display.PtToolbar, parentingHandle, args.length / 3, args);
	if ((style & SWT.FLAT) != 0) {
		OS.PtSetResource (handle, OS.Pt_ARG_BASIC_FLAGS, OS.Pt_FLAT_FILL, OS.Pt_FLAT_FILL);
	}
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

void deregister () {
	super.deregister ();
	if (parentingHandle != 0) WidgetTable.remove (parentingHandle);
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

public boolean forceFocus () {
	checkWidget();
	if (lastFocus != null && lastFocus.setFocus ()) return true;
	for (int i = 0; i < itemCount; i++) {
		ToolItem item = items [i];
		if (item.setFocus ()) return true;
	}
	return super.forceFocus ();
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
	checkWidget();
	return itemCount;
}

/**
 * Returns an array of <code>ToolItem</code>s which are the items
 * in the receiver. 
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
public ToolItem [] getItems () {
	checkWidget();
	ToolItem [] result = new ToolItem [itemCount];
	System.arraycopy (items, 0, result, 0, itemCount);
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
 */
public ToolItem getItem (int index) {
	checkWidget();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);	
	return items [index];
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
public ToolItem getItem (Point pt) {
	checkWidget();
	if (pt == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<itemCount; i++) {
		Rectangle rect = items [i].getBounds ();
		if (rect.contains (pt)) return items [i];
	}
	return null;
}

/**
 * Returns the number of rows in the receiver. When
 * the receiver has the <code>WRAP</code> style, the
 * number of rows can be greater than one.  Otherwise,
 * the number of rows is always one.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getRowCount () {
	checkWidget();
	return 1;
}

boolean hasFocus () {
	for (int i=0; i<itemCount; i++) {
		ToolItem item = items [i];
		if (item.hasFocus ()) return true;
	}
	return super.hasFocus();
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
 */
public int indexOf (ToolItem item) {
	checkWidget();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	for (int i=0; i<itemCount; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

void moveToBack (int child) {
	OS.PtWidgetInsert (child, handle, 0);
}

int parentingHandle () {
	return parentingHandle;
}

int Ph_EV_BOUNDARY (int widget, int info) {
	if (info == 0) return OS.Pt_END;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.event == 0) return OS.Pt_END;
	PhEvent_t ev = new PhEvent_t ();
	OS.memmove (ev, cbinfo.event, PhEvent_t.sizeof);
	switch ((int) ev.subtype) {
		case OS.Ph_EV_PTR_ENTER_FROM_CHILD:
		case OS.Ph_EV_PTR_LEAVE_TO_CHILD:
			return OS.Pt_CONTINUE;
	}
	return super.Ph_EV_BOUNDARY (widget, info);
}

void register () {
	super.register ();
	if (parentingHandle != 0) WidgetTable.put (parentingHandle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	parentingHandle = 0;
}

void releaseChildren (boolean destroy) {
	if (items != null) {
		for (int i=0; i<itemCount; i++) {
			ToolItem item = items [i];
			if (item != null && !item.isDisposed ()) {
				item.release (false);
			} 
		}
		items = null;
		itemCount = 0;
	}
	super.releaseChildren (destroy);
}

void removeControl (Control control) {
	super.removeControl (control);
	for (int i=0; i<itemCount; i++) {
		ToolItem item = items [i];
		if (item.control == control) item.setControl (null);
	}
}

void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	for (int i = 0; i < itemCount; i++) {
		ToolItem item = items[i];
		item.setBackgroundPixel (pixel);
	}
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	int result = super.setBounds (x, y, width, height, move, resize, events);
	if ((result & RESIZED) != 0) {
		int [] args = {OS.Pt_ARG_WIDTH, 0, 0, OS.Pt_ARG_HEIGHT, 0, 0};
		OS.PtGetResources (parentingHandle, args.length / 3, args);
		OS.PtSetResources (handle, args.length / 3, args);
	}
	return result;
}

void setFont (int font) {
	super.setFont (font);
	for (int i = 0; i < itemCount; i++) {
		ToolItem item = items[i];
		item.setFont (font);
	}
}

void setForegroundPixel (int pixel) {
	super.setForegroundPixel (pixel);
	for (int i = 0; i < itemCount; i++) {
		ToolItem item = items[i];
		item.setForegroundPixel (pixel);
	}
}

int topHandle () {
	return parentingHandle;
}

boolean translateTraversal (int key_sym, PhKeyEvent_t phEvent) {
	boolean result = super.translateTraversal (key_sym, phEvent);
	if (result) return result;
	boolean next = false;
	switch (key_sym) {
		case OS.Pk_Up:
		case OS.Pk_Left: next = false; break;
		case OS.Pk_Down:
		case OS.Pk_Right: next = true; break;
		default: return false;
	}
	int length = itemCount;
	int index = 0;
	while (index < length) {
		if (items [index].hasFocus ()) break;
		index++;
	}
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in focus in
	* or out events.  Ensure that a disposed widget is
	* not accessed.
	*/
	int start = index, offset = (next) ? 1 : -1;
	while ((index = (index + offset + length) % length) != start) {
		ToolItem item = items [index];
		if (item.setFocus ()) return false;
	}
	return false;
}

int widgetClass () {
	return OS.PtToolbar ();
}

}
