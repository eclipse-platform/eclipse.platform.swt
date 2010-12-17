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
	ToolItem lastFocus;
	ToolItem [] tabItemList;
	ImageList imageList;

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
	int orientation = (style & SWT.VERTICAL) != 0 ? OS.GTK_ORIENTATION_VERTICAL : OS.GTK_ORIENTATION_HORIZONTAL;
	OS.gtk_toolbar_set_orientation (handle, orientation);
}

static int checkStyle (int style) {
	/*
	* Feature in GTK.  It is not possible to create
	* a toolbar that wraps.  Therefore, no matter what 
	* style bits are specified,	clear the WRAP bits so 
	* that the style matches the behavior.
	*/
	if ((style & SWT.WRAP) != 0) style &= ~SWT.WRAP;
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

void createHandle (int index) {
	state |= HANDLE | THEME_BACKGROUND;
	fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	handle = OS.gtk_toolbar_new ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_container_add (fixedHandle, handle);
	if ((style & SWT.FLAT) != 0) {
		byte [] swt_toolbar_flat = Converter.wcsToMbcs (null, "swt-toolbar-flat", true);
		OS.gtk_widget_set_name (handle, swt_toolbar_flat);
	}
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	return computeNativeSize (handle, wHint, hHint, changed);
}

Widget computeTabGroup () {
	ToolItem [] items = _getItems ();
	if (tabItemList == null) {
		int i = 0;
		while (i < items.length && items [i].control == null) i++;
		if (i == items.length) return super.computeTabGroup (); 
	}
	int index = 0;
	while (index < items.length) {
		if (items[index].hasFocus ()) break;
		index++;
	}
	while (index >= 0) {
		ToolItem item = items [index];
		if (item.isTabGroup ()) return item;
		index--;
	}
	return super.computeTabGroup ();
}

Widget [] computeTabList () {
	ToolItem [] items = _getItems ();
	if (tabItemList == null) {
		int i = 0;
		while (i < items.length && items [i].control == null) i++;
		if (i == items.length) return super.computeTabList (); 
	}
	Widget result [] = {};
	if (!isTabGroup () || !isEnabled () || !isVisible ()) return result;
	ToolItem [] list = tabList != null ? _getTabItemList () : items;
	for (int i=0; i<list.length; i++) {
		ToolItem child = list [i];
		Widget  [] childList = child.computeTabList ();
		if (childList.length != 0) {
			Widget [] newResult = new Widget [result.length + childList.length];
			System.arraycopy (result, 0, newResult, 0, result.length);
			System.arraycopy (childList, 0, newResult, result.length, childList.length);
			result = newResult;
		}
	}
	if (result.length == 0) result = new Widget [] {this}; 
	return result;
}

int /*long*/ eventHandle () {
	return fixedHandle;
}

int /*long*/ enterExitHandle() {
	return handle;
}

void fixChildren (Shell newShell, Shell oldShell, Decorations newDecorations, Decorations oldDecorations, Menu [] menus) {
	super.fixChildren (newShell, oldShell, newDecorations, oldDecorations, menus);
	ToolItem [] items = getItems ();
	if (toolTipText == null) {
		for (int i = 0; i < items.length; i++) {
			ToolItem item = items [i];
			if (item.toolTipText != null) {
				item.setToolTipText(oldShell, null);
				item.setToolTipText(newShell, item.toolTipText);
			}
		}
	}
}

boolean forceFocus (int /*long*/ focusHandle) {
	if (lastFocus != null && lastFocus.setFocus ()) return true;
	ToolItem [] items = getItems ();
	for (int i = 0; i < items.length; i++) {
		ToolItem item = items [i];
		if (item.setFocus ()) return true;
	}
	return super.forceFocus (focusHandle);
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
	if (!(0 <= index && index < getItemCount())) error (SWT.ERROR_INVALID_RANGE);
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
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ToolItem getItem (Point point) {
	checkWidget();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	ToolItem[] items = getItems();
	for (int i=0; i<items.length; i++) {
		if (items[i].getBounds().contains(point)) return items[i];
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
 */
public int getItemCount () {
	checkWidget();
	int /*long*/ list = OS.gtk_container_get_children (handle);
	if (list == 0) return 0;
	int itemCount = OS.g_list_length (list);
	OS.g_list_free (list);
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
	return _getItems ();
}

ToolItem [] _getItems () {
	int /*long*/ list = OS.gtk_container_get_children (handle);
	if (list == 0) return new ToolItem [0];
	int count = OS.g_list_length (list);
	ToolItem [] items = new ToolItem [count];
	int index = 0;
	for (int i=0; i<count; i++) {
		int /*long*/ data = OS.g_list_nth_data (list, i);
		Widget widget = display.getWidget (data);
		if (widget != null) items [index++] = (ToolItem) widget;
	}
	OS.g_list_free (list);
	if (index != items.length) {
		ToolItem [] newItems = new ToolItem [index];
		System.arraycopy (items, 0, newItems, 0, index);
		items = newItems;
	}
	return items;
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
	 /* On GTK, toolbars cannot wrap */
	return 1;
}

ToolItem [] _getTabItemList () {
	if (tabItemList == null) return tabItemList;
	int count = 0;
	for (int i=0; i<tabItemList.length; i++) {
		if (!tabItemList [i].isDisposed ()) count++;
	}
	if (count == tabItemList.length) return tabItemList;
	ToolItem [] newList = new ToolItem [count];
	int index = 0;
	for (int i=0; i<tabItemList.length; i++) {
		if (!tabItemList [i].isDisposed ()) {
			newList [index++] = tabItemList [i];
		}
	}
	tabItemList = newList;
	return tabItemList;
}

int /*long*/ gtk_key_press_event (int /*long*/ widget, int /*long*/ eventPtr) {
	if (!hasFocus ()) return 0;
	int /*long*/ result = super.gtk_key_press_event (widget, eventPtr);
	if (result != 0) return result;
	ToolItem [] items = getItems ();
	int length = items.length;
	int index = 0;
	while (index < length) {
		if (items [index].hasFocus ()) break;
		index++;
	}
	GdkEventKey gdkEvent = new GdkEventKey ();
	OS.memmove (gdkEvent, eventPtr, GdkEventKey.sizeof);
	boolean next = false;
	switch (gdkEvent.keyval) {
		case OS.GDK_Up:
		case OS.GDK_Left: next = false; break;
		case OS.GDK_Down: {
			if (0 <= index && index < length) {
				ToolItem item = items [index];
				if ((item.style & SWT.DROP_DOWN) != 0) {
					Event event = new Event ();
					event.detail = SWT.ARROW;
					int /*long*/ topHandle = item.topHandle ();
					event.x = OS.GTK_WIDGET_X (topHandle);
					event.y = OS.GTK_WIDGET_Y (topHandle) + OS.GTK_WIDGET_HEIGHT (topHandle);
					if ((style & SWT.MIRRORED) != 0) event.x = getClientWidth() - OS.GTK_WIDGET_WIDTH(topHandle) - event.x;
					item.sendSelectionEvent  (SWT.Selection, event, false);
					return result;
				}
			}
			//FALL THROUGH
		}
		case OS.GDK_Right: next = true; break;
		default: return result;
	}
	if ((style & SWT.MIRRORED) != 0) next= !next;
	int start = index, offset = next ? 1 : -1;
	while ((index = (index + offset + length) % length) != start) {
		ToolItem item = items [index];
		if (item.setFocus ()) return result;
	}
	return result;
}

boolean hasFocus () {
	ToolItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
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
	ToolItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		if (item == items[i]) return i;
	}
	return -1;
}

boolean mnemonicHit (char key) {
	ToolItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		int /*long*/ labelHandle = items [i].labelHandle;
		if (labelHandle != 0 && mnemonicHit (labelHandle, key)) return true;
	}
	return false;
}

boolean mnemonicMatch (char key) {
	ToolItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		int /*long*/ labelHandle = items [i].labelHandle;
		if (labelHandle != 0 && mnemonicMatch (labelHandle, key)) return true;
	}
	return false;
}

void relayout () {
	ToolItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		ToolItem item = items [i];
		if (item != null) item.resizeControl ();
	}
}

void releaseChildren (boolean destroy) {
	ToolItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		ToolItem item = items [i];
		if (item != null && !item.isDisposed ()) {
			item.release (false);
		}
	}
	super.releaseChildren (destroy);
}

void releaseWidget () {
	super.releaseWidget ();
	if (imageList != null) imageList.dispose ();
	imageList = null;
}

void removeControl (Control control) {
	super.removeControl (control);
	ToolItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		ToolItem item = items [i];
		if (item.control == control) item.setControl (null);
	}
}

void reskinChildren (int flags) {
	ToolItem[] items = _getItems();
	if (items != null) {
		for (int i=0; i<items.length; i++) {
			ToolItem item = items [i];
			if (item != null) item.reskin (flags);
		}
	}
	super.reskinChildren (flags);
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	int result = super.setBounds (x, y, width, height, move, resize);
	if ((result & RESIZED) != 0) relayout ();
	return result;
}

void setFontDescription (int /*long*/ font) {
	super.setFontDescription (font);
	ToolItem [] items = getItems ();
	for (int i = 0; i < items.length; i++) {
		items[i].setFontDescription (font);
	}
	relayout ();
}

void setForegroundColor (GdkColor color) {
	super.setForegroundColor (color);
	ToolItem [] items = getItems ();
	for (int i = 0; i < items.length; i++) {
		items[i].setForegroundColor (color);
	}
}

void setOrientation (boolean create) {
	super.setOrientation (create);
	ToolItem [] items = _getItems ();
	for (int i = 0; i < items.length; i++) {
		items[i].setOrientation (create);
	}
}

/*public*/ void setTabItemList (ToolItem [] tabList) {
	checkWidget ();
	if (tabList != null) {
		for (int i=0; i<tabList.length; i++) {
			ToolItem item = tabList [i];
			if (item == null) error (SWT.ERROR_INVALID_ARGUMENT);
			if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
			if (item.parent != this) error (SWT.ERROR_INVALID_PARENT);
		}
		ToolItem [] newList = new ToolItem [tabList.length];
		System.arraycopy (tabList, 0, newList, 0, tabList.length);
		tabList = newList;
	} 
	this.tabItemList = tabList;
}

public void setToolTipText (String string) {
	checkWidget();
	super.setToolTipText (string);
	Shell shell = _getShell ();
	ToolItem [] items = getItems ();
	for (int i = 0; i < items.length; i++) {
		String newString = string != null ? null : items [i].toolTipText;
		shell.setToolTipText (items [i].handle, newString);
	}
}

}
