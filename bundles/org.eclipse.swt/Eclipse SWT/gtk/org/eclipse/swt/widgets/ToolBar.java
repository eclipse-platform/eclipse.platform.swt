package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
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
 * <dd>FLAT, WRAP, RIGHT, HORIZONTAL, VERTICAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class ToolBar extends Composite {

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolBar (Composite parent, int style) {
	super (parent, checkStyle (style));
}

/*
 *  === Handle code ===
 */
void createHandle (int index) {
	state |= HANDLE;
	
	boxHandle = OS.gtk_event_box_new();
	if (boxHandle == 0) error (SWT.ERROR_NO_HANDLES);
	
	fixedHandle = OS.eclipse_fixed_new();
	if (fixedHandle == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	
	int orientation = ((style&SWT.VERTICAL)!=0)?
		OS.GTK_ORIENTATION_VERTICAL : OS.GTK_ORIENTATION_HORIZONTAL;
	handle = OS.gtk_toolbar_new ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_toolbar_set_orientation(handle, orientation);
}	

void setHandleStyle() {
/*	int relief = ((style&SWT.FLAT)!=0)? OS.GTK_RELIEF_NONE : OS.GTK_RELIEF_NORMAL;
	OS.gtk_toolbar_set_button_relief(handle, relief);*/
}

void configure() {
	parent._connectChild(boxHandle);
	OS.gtk_container_add(boxHandle, fixedHandle);	
	OS.gtk_container_add(fixedHandle, handle);
}

void showHandle() {
	OS.gtk_widget_show (boxHandle);
	OS.gtk_widget_show (fixedHandle);
	OS.gtk_widget_show (handle);
	OS.gtk_widget_realize (handle);
}

void register() {
	super.register ();
}

void deregister() {
	super.deregister ();
}

void releaseHandle () {
	super.releaseHandle ();
}

int topHandle() { return boxHandle; }
int parentingHandle() { return fixedHandle; } 
boolean isMyHandle(int h) {
	return super.isMyHandle(h);
}


/*
 *   ===  GEOMETRY  ===
 */

void _setSize (int width, int height) {
	OS.eclipse_fixed_set_size(parent.parentingHandle(), boxHandle, width, height);
	OS.eclipse_fixed_set_size(fixedHandle, handle, width, height);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (layout != null) super.computeSize(wHint, hHint, changed);
	return new Point(300,30);
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
	return getItems()[index];
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
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ToolItem getItem (Point point) {
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
	checkWidget();
	int list = OS.gtk_container_children (handle);
	return OS.g_list_length (list);
}

/**
 * Returns an array of <code>TabItem</code>s which are the items
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
	int count = 0;
	int list = OS.gtk_container_children (handle);
	int length = OS.g_list_length (list);
	ToolItem [] result = new ToolItem [length];
	for (int i=0; i<length; i++) {
		int data = OS.g_list_nth_data (list, i);
		Widget widget = WidgetTable.get (data);
		result [count++] = (ToolItem) widget;
	}
	return result;
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
	return 1; /* On GTK, toolbars never wrap */
}

Control _childFromHandle(int h) {
	Widget child = WidgetTable.get(h);
	if (child==null) return null;
	if (child instanceof ToolItem) return null; // ToolItems are not our children
	return (Control)child;
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

	// TEMPORARY CODE
	ToolItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		if (item == items[i]) return i;
	}
	return -1;
}

void releaseWidget () {
	ToolItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		ToolItem item = items [i];
		if (!item.isDisposed ()) {
			item.releaseWidget ();
			item.releaseHandle ();
		}
	}
	items = null;
	super.releaseWidget ();
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

}
