/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class implement the notebook user interface
 * metaphor.  It allows the user to select a notebook page from
 * set of pages.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TabItem</code>.
 * <code>Control</code> children are created and then set into a
 * tab item using <code>TabItem#setControl</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>TOP, BOTTOM</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles TOP and BOTTOM may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class TabFolder extends Composite {
	TabItem [] items;

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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TabFolder (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	style = checkBits (style, SWT.TOP, SWT.BOTTOM, 0, 0, 0, 0);
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}


/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * <code>widgetDefaultSelected</code> is not called.
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

int clientHandle () {
	int index = OS.gtk_notebook_get_current_page (handle);
	if (index != -1 && items [index] != null) {
		return items [index].pageHandle;
	}
	return handle;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	int width = OS.GTK_WIDGET_WIDTH (fixedHandle);
	int height = OS.GTK_WIDGET_HEIGHT (fixedHandle);
	OS.gtk_widget_set_size_request (handle, wHint, hHint);
	GtkRequisition requisition = new GtkRequisition ();
	boolean scrollable = OS.gtk_notebook_get_scrollable (handle);
	OS.gtk_notebook_set_scrollable (handle, false);
	OS.gtk_widget_size_request (handle, requisition);
	OS.gtk_notebook_set_scrollable (handle, scrollable);
	OS.gtk_widget_set_size_request (handle, width, height);
	width = wHint == SWT.DEFAULT ? requisition.width : wHint;
	height = hHint == SWT.DEFAULT ? requisition.height : hHint;
	Point size;
	if (layout != null) {
		size = layout.computeSize (this, wHint, hHint, changed);
	} else {
		size = minimumSize (wHint, hHint, changed);
	}
	Rectangle trim = computeTrim (0, 0, size.x, size.y);
	size.x = trim.width;  size.y = trim.height;
	if (size.x == 0) size.x = DEFAULT_WIDTH;
	if (size.y == 0) size.y = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	width = Math.max (width, size.x);
	height = Math.max (height, size.y);
	return new Point (width, height);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget();
	int clientHandle = clientHandle ();
	int clientX = OS.GTK_WIDGET_X (clientHandle);
	int clientY = OS.GTK_WIDGET_Y (clientHandle);
	x -= clientX;
	y -= clientY;
	width +=  clientX + clientX;
	if ((style & SWT.BOTTOM) != 0) {
		int parentHeight = OS.GTK_WIDGET_HEIGHT (handle);
		int clientHeight = OS.GTK_WIDGET_HEIGHT (clientHandle);
		height += parentHeight - clientHeight;
	} else {
		height +=  clientX + clientY;
	}
	return new Rectangle (x, y, width, height);
}

void createHandle (int index) {
	state |= HANDLE;
	fixedHandle = OS.gtk_fixed_new ();
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	handle = OS.gtk_notebook_new ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	int parentHandle = parent.parentingHandle ();
	OS.gtk_container_add (parentHandle, fixedHandle);
	OS.gtk_container_add (fixedHandle, handle);
	OS.gtk_widget_show (handle);
	OS.gtk_widget_show (fixedHandle);
	OS.gtk_notebook_set_scrollable (handle, true);
	OS.gtk_notebook_set_show_tabs (handle, true);
	if ((style & SWT.BOTTOM) != 0) {
		OS.gtk_notebook_set_tab_pos (handle, OS.GTK_POS_BOTTOM);
	}
}

void createWidget (int index) {
	super.createWidget(index);
	items = new TabItem [4];
}

void createItem (TabItem item, int index) {
	int list = OS.gtk_container_get_children (handle);
	int itemCount = 0;
	if (list != 0) {
		itemCount = OS.g_list_length (list);
		OS.g_list_free (list);
	}
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	if (itemCount == items.length) {
		TabItem [] newItems = new TabItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	int boxHandle = OS.gtk_hbox_new (false, 0);
	if (boxHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int labelHandle = OS.gtk_label_new_with_mnemonic (null);
	if (labelHandle == 0) error (SWT.ERROR_NO_HANDLES);
	int imageHandle = OS.gtk_image_new ();
	if (imageHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_container_add (boxHandle, imageHandle);
	OS.gtk_container_add (boxHandle, labelHandle);
	int pageHandle = OS.gtk_fixed_new ();
	if (pageHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, SWITCH_PAGE);
	OS.gtk_notebook_insert_page (handle, pageHandle, boxHandle, index);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, SWITCH_PAGE);
	OS.gtk_widget_show (boxHandle);
	OS.gtk_widget_show (labelHandle);
	OS.gtk_widget_show (pageHandle);
	item.state |= HANDLE;
	item.handle = boxHandle;
	item.labelHandle = labelHandle;
	item.imageHandle = imageHandle;
	item.pageHandle = pageHandle;
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
	item.setForegroundColor (getForegroundColor ());
	item.setFontDescription (getFontDescription ());
	if (itemCount == 1) {
		fixPage ();
		Event event = new Event();
		event.item = items[0];
		sendEvent (SWT.Selection, event);
		// the widget could be destroyed at this point
	}
}

void fixPage () {
	/*
	* Feature in GTK.  For some reason, the positioning of
	* tab labels and pages become corrupted when when there
	* is no current page.  The fix is to force the notebook
	* to resize which causes the current page to be set.
	*/
//	int index = OS.gtk_notebook_get_current_page (handle);
//	if (index != -1) return;
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, SWITCH_PAGE);
	int flags = OS.GTK_WIDGET_FLAGS (handle);
	OS.GTK_WIDGET_SET_FLAGS(handle, OS.GTK_VISIBLE);
	GtkRequisition requisition = new GtkRequisition ();
	OS.gtk_widget_size_request (handle, requisition);
	OS.gtk_container_resize_children (handle);
	if ((flags & OS.GTK_VISIBLE) == 0) {
		OS.GTK_WIDGET_UNSET_FLAGS(handle, OS.GTK_VISIBLE);	
	}
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, SWITCH_PAGE);
}

void destroyItem (TabItem item) {
	int index = 0;
	int itemCount = getItemCount();
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	if (index == itemCount) error (SWT.ERROR_ITEM_NOT_REMOVED);
	int oldIndex = OS.gtk_notebook_get_current_page (handle);
	item.deregister ();
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, SWITCH_PAGE);
	OS.gtk_notebook_remove_page (handle, index);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, SWITCH_PAGE);
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	item.handle = item.pageHandle = item.imageHandle = item.labelHandle = 0;
	if (index == oldIndex) {
		fixPage ();
		int newIndex = OS.gtk_notebook_get_current_page (handle);
		if (newIndex != -1) {
			Control control = items [newIndex].getControl ();
			if (control != null && !control.isDisposed ()) {
				control.setBounds (getClientArea());
				control.setVisible (true);
			}
			Event event = new Event ();
			event.item = items [newIndex];
			sendEvent (SWT.Selection, event);	
			// the widget could be destroyed at this point
		}
	}
}

void enableWidget (boolean enabled) {
	OS.gtk_widget_set_sensitive (handle, enabled);
}

int eventHandle () {
	return fixedHandle;
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
public TabItem getItem (int index) {
	checkWidget();
	if (!(0 <= index && index < getItemCount())) error (SWT.ERROR_INVALID_RANGE);	
	int list = OS.gtk_container_get_children (handle);
	if (list == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
	int itemCount = OS.g_list_length (list);
	OS.g_list_free (list);
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_CANNOT_GET_ITEM);
	return items [index];
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
	int list = OS.gtk_container_get_children (handle);
	if (list == 0) return 0;
	int itemCount = OS.g_list_length (list);
	OS.g_list_free (list);
	return itemCount;
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
public TabItem [] getItems () {
	checkWidget();
	int count = getItemCount ();
	TabItem [] result = new TabItem [count];
	System.arraycopy (items, 0, result, 0, count);
	return result;
}

/**
 * Returns an array of <code>TabItem</code>s that are currently
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
 */
public TabItem [] getSelection () {
	checkWidget();
	int index = OS.gtk_notebook_get_current_page (handle);
	if (index == -1) return new TabItem [0];
	return new TabItem [] {items [index]};
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
 */
public int getSelectionIndex () {
	checkWidget();
	return OS.gtk_notebook_get_current_page (handle);
}

int gtk_switch_page (int widget, int page, int page_num) {
	int index = OS.gtk_notebook_get_current_page (handle);
	if (index != -1) {
		Control control = items [index].getControl ();
		if (control != null && !control.isDisposed ()) {
			control.setVisible (false);
		}
	}
	Control control = items [page_num].getControl ();
	if (control != null && !control.isDisposed ()) {
		control.setBounds(getClientArea());
		control.setVisible (true);
	}
	Event event = new Event();
	event.item = items[page_num];
	postEvent(SWT.Selection, event);
	return 0;
}

void hookEvents () {
	super.hookEvents ();
	OS.g_signal_connect (handle, OS.switch_page, display.windowProc4, SWITCH_PAGE);
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
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (TabItem item) {
	checkWidget();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int list = OS.gtk_container_get_children (handle);
	if (list == 0) return -1;
	int count = OS.g_list_length (list);
	OS.g_list_free (list);
	for (int i=0; i<count; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

Point minimumSize (int wHint, int hHint, boolean flushCache) {
	Control [] children = _getChildren ();
	int width = 0, height = 0;
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		int index = 0;
		int count = 0;
		int list = OS.gtk_container_get_children (handle);
		if (list != 0) {
			count = OS.g_list_length (list);
			OS.g_list_free (list);
		}
		while (index < count) {
			if (items [index].control == child) break;
			index++;
		}
		if (index == count) {
			Rectangle rect = child.getBounds ();
			width = Math.max (width, rect.x + rect.width);
			height = Math.max (height, rect.y + rect.height);
		} else {
			Point size = child.computeSize (wHint, hHint, flushCache);
			width = Math.max (width, size.x);
			height = Math.max (height, size.y);
		}
	}
	return new Point (width, height);
}

boolean mnemonicHit (char key) {
	int itemCount = getItemCount ();
	for (int i=0; i<itemCount; i++) {
		int labelHandle = items [i].labelHandle;
		if (labelHandle != 0 && mnemonicHit (labelHandle, key)) return true;
	}
	return false;
}

boolean mnemonicMatch (char key) {
	int itemCount = getItemCount ();
	for (int i=0; i<itemCount; i++) {
		int labelHandle = items [i].labelHandle;
		if (labelHandle != 0 && mnemonicMatch (labelHandle, key)) return true;
	}
	return false;
}

void releaseWidget () {
	int count = getItemCount ();
	for (int i=0; i<count; i++) {
		TabItem item = items [i];
		if (!item.isDisposed ()) item.releaseResources ();
	}
	items = null;
	super.releaseWidget ();
}

void removeControl (Control control) {
	super.removeControl (control);
	int count = getItemCount ();
	for (int i=0; i<count; i++) {
		TabItem item = items [i];
		if (item.control == control) item.setControl (null);
	}
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
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	boolean changed = super.setBounds (x, y, width, height, move, resize);
	if (changed && resize) {
		int index = getSelectionIndex ();
		if (index != -1) {
			TabItem item = items [index];
			Control control = item.control;
			if (control != null && !control.isDisposed ()) {
				control.setBounds (getClientArea ());
			}
		}
	}
	return changed;
}

void setFontDescription (int font) {
	super.setFontDescription (font);
	TabItem [] items = getItems ();
	for (int i = 0; i < items.length; i++) {
		if (items[i] != null) {
			items[i].setFontDescription (font);
		}
	}
}

void setForegroundColor (GdkColor color) {
	super.setForegroundColor (color);
	TabItem [] items = getItems ();
	for (int i = 0; i < items.length; i++) {
		if (items[i] != null) {
			items[i].setForegroundColor (color);
		}
	}
}

/**
 * Selects the item at the given zero-relative index in the receiver. 
 * If the item at the index was already selected, it remains selected.
 * The current selected is first cleared, then the new items are
 * selected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int index) {
	checkWidget ();
	if (!(0 <= index && index < getItemCount ())) return;
	setSelection (index, false);
}

void setSelection (int index, boolean notify) {
	if (index < 0) return;
	int oldIndex = OS.gtk_notebook_get_current_page (handle);
	if (oldIndex != -1) {
		TabItem item = items [oldIndex];
		Control control = item.control;
		if (control != null && !control.isDisposed ()) {
			control.setVisible (false);
		}
	}
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, SWITCH_PAGE);
	OS.gtk_notebook_set_current_page (handle, index);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, SWITCH_PAGE);
	int newIndex = OS.gtk_notebook_get_current_page (handle);
	if (newIndex != -1) {
		TabItem item = items [newIndex];
		Control control = item.control;
		if (control != null && !control.isDisposed ()) {
			control.setBounds (getClientArea ());
			control.setVisible (true);
		}
		if (notify) {
			Event event = new Event ();
			event.item = item;
			sendEvent (SWT.Selection, event);
		}
	}
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selected is first cleared, then the new items are
 * selected.
 *
 * @param items the array of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (TabItem [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (items.length == 0) {
		setSelection (-1, false);
	} else {
		for (int i=items.length-1; i>=0; --i) {
			int index = indexOf (items [i]);
			if (index != -1) setSelection (index, false);
		}
	}
}

}
