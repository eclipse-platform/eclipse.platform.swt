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
	int boxHandle, tempHandle;

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

void createHandle (int index) {
	state |= HANDLE;
	
	/* FIXME
	 * We do not need an event box here, as event boxes
	 * have real X windows.
	 */
	boxHandle = OS.gtk_event_box_new ();
	if (boxHandle == 0) error (SWT.ERROR_NO_HANDLES);
	
	int orientation = ((style&SWT.VERTICAL)!=0)?
		OS.GTK_ORIENTATION_VERTICAL : OS.GTK_ORIENTATION_HORIZONTAL;
	handle = OS.gtk_toolbar_new (orientation, OS.GTK_TOOLBAR_BOTH);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	
	tempHandle = OS.gtk_fixed_new();
	if (tempHandle == 0) error (SWT.ERROR_NO_HANDLES);
}	

void setHandleStyle() {
	int relief = ((style&SWT.FLAT)!=0)? OS.GTK_RELIEF_NONE : OS.GTK_RELIEF_NORMAL;
	OS.gtk_toolbar_set_button_relief(handle, relief);
}

void configure() {
	_connectParent();
	OS.gtk_container_add (boxHandle, handle);
	// invisible handle to temporarily hold control (non-item) items
	OS.gtk_toolbar_insert_widget (handle,tempHandle,new byte[1], new byte[1],0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (layout != null) super.computeSize(wHint, hHint, changed);
	return _computeSize (wHint, hHint, changed);
}

int eventHandle () {
	return boxHandle;
}

void showHandle() {
	OS.gtk_widget_show (boxHandle);
	OS.gtk_widget_show (handle);
	OS.gtk_widget_realize (handle);
	// don't show the temp fixed
}

void register() {
	super.register ();
	WidgetTable.put (boxHandle, this);
}

void deregister() {
	super.deregister ();
	WidgetTable.remove (boxHandle);
}

int topHandle() { return boxHandle; }
int parentingHandle() { return tempHandle; } 

/**
 * Returns whether the argument points to an OS widget that is
 * implementing the receiver, i.e., one of my own handles
 */
boolean isMyHandle(int h) {
	if (h==handle)       return true;
	if (h==tempHandle)  return true;
	if (h==boxHandle)       return true;
	return false;
}
void _connectChild (int h) {
	// When we put a widget as a tool item, we don't know which item it is, yet.
	OS.gtk_fixed_put(tempHandle, h, (short)0, (short)0);
}


/*
 *   ===  GEOMETRY  ===
 */

boolean _setSize (int width, int height) { UtilFuncs.setSize(boxHandle, width, height); return true; }



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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	/* FIXME
	 * This code will return the wrong count for items,
	 * as list includes Window children
	 */
//	int list = OS.gtk_container_children (handle);
//	return OS.g_list_length (list);
	// TEMPORARY CODE
	return getItems ().length;
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int count = 0;
	int list = OS.gtk_container_children (handle);
	int length = OS.g_list_length (list);
	ToolItem [] result = new ToolItem [length];
	for (int i=0; i<length; i++) {
		int data = OS.g_list_nth_data (list, i);
		Widget widget = WidgetTable.get (data);
		if (widget instanceof ToolItem) {
			result [count++] = (ToolItem) widget;
		}
	}
	if (length == count) return result;
	ToolItem [] newResult = new ToolItem [count];
	System.arraycopy (result, 0, newResult, 0, count);
	return newResult;
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return 1;
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
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);

	// TEMPORARY CODE
	ToolItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		if (item == items[i]) return i;
	}
	return -1;
}
int processResize (int int0, int int1, int int2) {
	ToolItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		Control control = items [i].control;
		if (control != null && !control.isDisposed ()) {
			control.setBounds (items [i].getBounds ());
		}
	}
	return 0;
}

void releaseHandle () {
	super.releaseHandle ();
	boxHandle = tempHandle = 0;
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
	return style;   // & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}

}
