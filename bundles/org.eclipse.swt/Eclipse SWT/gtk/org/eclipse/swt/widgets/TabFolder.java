/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

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
 * </p>
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
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tabfolder">TabFolder, TabItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TabFolder extends Composite {
	/*
	 * Implementation note (see bug 454936, bug 480794):
	 *
	 * Architecture Change on GTK3:
	 * In TabItem#setControl(Control), we reparent the child to be a child of the 'tab'
	 * rather than tabfolder's parent swtFixed container.
	 * Note, this reparenting is only on the GTK side, not on the SWT side.
	 *
	 * GTK3+:
	 * 	swtFixed
	 * 	|--	GtkNoteBook
	 * 		|-- tabLabel1
	 * 		|-- tabLabel2
	 * 		|-- pageHandle (tabItem1)
	 * 			|-- child1 //child now child of Notebook.pageHandle.
	 * 		|-- pageHandle (tabItem2)
	 * 			|-- child1
	 *
	 * This changes the hierarchy so that children are beneath gtkNotebook (as oppose to
	 * being siblings) and thus fixes DND and background color issues.
	 *
	 * Note about the reason for reparenting:
	 * Reparenting (as opposed to adding widget to a tab in the first place) is necessary
	 * because the SWT API allows situation where you create a child control before you create a TabItem.
	 */

	TabItem [] items;
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
 * @see SWT
 * @see SWT#TOP
 * @see SWT#BOTTOM
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

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the user changes the receiver's selection, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified when the user changes the receiver's selection
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

@Override
long clientHandle () {
	int index = GTK.gtk_notebook_get_current_page (handle);
	if (index != -1 && items [index] != null) {
		return items [index].pageHandle;
	}
	return handle;
}

@Override
Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();
	Point size = super.computeSizeInPixels (wHint, hHint, changed);
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	boolean scrollable = GTK.gtk_notebook_get_scrollable (handle);
	GTK.gtk_notebook_set_scrollable (handle, false);
	Point notebookSize = computeNativeSize (handle, wHint, hHint, changed);
	GTK.gtk_notebook_set_scrollable (handle, scrollable);
	int[] initialGap = new int[1];
	notebookSize.x += initialGap[0]*2;
	size.x = Math.max (notebookSize.x, size.x);
	size.y = Math.max (notebookSize.y, size.y);
	return size;
}

@Override
Rectangle computeTrimInPixels (int x, int y, int width, int height) {
	checkWidget();
	forceResize ();
	long clientHandle = clientHandle ();
	GtkAllocation allocation = new GtkAllocation ();
	GTK.gtk_widget_get_allocation (clientHandle, allocation);
	int clientX = allocation.x;
	int clientY = allocation.y;
	x -= clientX;
	y -= clientY;
	width +=  clientX + clientX;
	if ((style & SWT.BOTTOM) != 0) {
		int clientHeight = allocation.height;
		GTK.gtk_widget_get_allocation (handle, allocation);
		int parentHeight = allocation.height;
		height += parentHeight - clientHeight;
	} else {
		height +=  clientX + clientY;
	}
	return new Rectangle (x, y, width, height);
}

@Override
Rectangle getClientAreaInPixels () {
	Rectangle clientRectangle = super.getClientAreaInPixels ();

	/*
	* Bug 454936 (see also other 454936 references)
	* SWT's calls to gtk_widget_size_allocate and gtk_widget_set_allocation
	* causes GTK+ to move the clientHandle's SwtFixed down by the size of the labels.
	* These calls can come up from 'shell' and TabFolder has no control over these calls.
	*
	* This is an undesired side-effect. Client handle's x & y positions should never
	* be incremented as this is an internal sub-container.
	*
	* Note: 0 by 0 was chosen as 1 by 1 shifts controls beyond their original pos.
	* The long term fix would be to not use widget_*_allocation from higher containers,
	* but this would require removal of swtFixed.
	*
	* This is Gtk3-specific for Tabfolder as the architecture is changed in gtk3 only.
	*/
	clientRectangle.x = 0;
	clientRectangle.y = 0;
	return clientRectangle;
}


@Override
void createHandle (int index) {
	state |= HANDLE;
	fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	handle = GTK.gtk_notebook_new ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);

	if (GTK.GTK4) {
		OS.swt_fixed_add(fixedHandle, handle);
	} else {
		GTK3.gtk_widget_set_has_window(fixedHandle, true);
		GTK3.gtk_container_add (fixedHandle, handle);
	}

	GTK.gtk_notebook_set_show_tabs (handle, true);
	GTK.gtk_notebook_set_scrollable (handle, true);

	if ((style & SWT.BOTTOM) != 0) {
		GTK.gtk_notebook_set_tab_pos (handle, GTK.GTK_POS_BOTTOM);
	}
}

@Override
void createWidget (int index) {
	super.createWidget(index);
	items = new TabItem [4];
}

void createItem (TabItem item, int index) {
	int itemCount = 0;
	if (GTK.GTK4) {
		itemCount = GTK.gtk_notebook_get_n_pages(handle);
	} else {
		long list = GTK3.gtk_container_get_children (handle);
		if (list != 0) {
			itemCount = OS.g_list_length (list);
			OS.g_list_free (list);
		}
	}

	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	if (itemCount == items.length) {
		TabItem [] newItems = new TabItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	long boxHandle = gtk_box_new (GTK.GTK_ORIENTATION_HORIZONTAL, false, 0);
	if (boxHandle == 0) error (SWT.ERROR_NO_HANDLES);
	long labelHandle = GTK.gtk_label_new_with_mnemonic (null);
	if (labelHandle == 0) error (SWT.ERROR_NO_HANDLES);
	long imageHandle = GTK.gtk_image_new ();
	if (imageHandle == 0) error (SWT.ERROR_NO_HANDLES);

	if (GTK.GTK4) {
		GTK4.gtk_box_append(boxHandle, imageHandle);
		GTK4.gtk_box_append(boxHandle, labelHandle);
	} else {
		GTK3.gtk_container_add(boxHandle, imageHandle);
		GTK3.gtk_container_add(boxHandle, labelHandle);
	}

	long pageHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (pageHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, SWITCH_PAGE);
	GTK.gtk_notebook_insert_page (handle, pageHandle, boxHandle, index);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, SWITCH_PAGE);

	if (GTK.GTK4) {
		GTK.gtk_widget_hide(imageHandle);
	} else {
		GTK.gtk_widget_show(boxHandle);
		GTK.gtk_widget_show(labelHandle);
		GTK.gtk_widget_show(pageHandle);
	}

	item.state |= HANDLE;
	item.handle = boxHandle;
	item.labelHandle = labelHandle;
	item.imageHandle = imageHandle;
	item.pageHandle = pageHandle;
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
	if ((state & FOREGROUND) != 0) {
		item.setForegroundGdkRGBA (item.handle, getForegroundGdkRGBA());
	}
	if ((state & FONT) != 0) {
		long fontDesc = getFontDescription ();
		item.setFontDescription (fontDesc);
		OS.pango_font_description_free (fontDesc);
	}
	if (itemCount == 1) {
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, SWITCH_PAGE);
		GTK.gtk_notebook_set_current_page (handle, 0);
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, SWITCH_PAGE);
		Event event = new Event();
		event.item = items[0];
		sendSelectionEvent (SWT.Selection, event, false);
		// the widget could be destroyed at this point
	}
}

void destroyItem (TabItem item) {
	int index = 0;
	int itemCount = getItemCount();
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	if (index == itemCount) error (SWT.ERROR_ITEM_NOT_REMOVED);
	int oldIndex = GTK.gtk_notebook_get_current_page (handle);
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, SWITCH_PAGE);
	GTK.gtk_notebook_remove_page (handle, index);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, SWITCH_PAGE);
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	if (index == oldIndex) {
		int newIndex = GTK.gtk_notebook_get_current_page (handle);
		if (newIndex != -1) {
			Control control = items [newIndex].getControl ();
			if (control != null && !control.isDisposed ()) {
				control.setBoundsInPixels (getClientAreaInPixels());
				control.setVisible (true);
			}
			Event event = new Event ();
			event.item = items [newIndex];
			sendSelectionEvent (SWT.Selection, event, true);
			// the widget could be destroyed at this point
		}
	}
}

@Override
long eventHandle () {
	return handle;
}

@Override
Control[] _getChildren() {
	Control[] directChildren = super._getChildren();
	int directCount = directChildren.length;
	int itemCount = items == null ? 0 : items.length;
	Control[] children = new Control[itemCount + directCount];

	int childrenCount = 0;
	for (int itemIndex = 0; itemIndex < itemCount; itemIndex++) {
		TabItem tabItem = items[itemIndex];
		if (tabItem != null && !tabItem.isDisposed()) {
			long parentHandle = tabItem.pageHandle;

			if (GTK.GTK4) {
				for (long child = GTK4.gtk_widget_get_first_child(parentHandle); child != 0; child = GTK4.gtk_widget_get_next_sibling(child)) {
					Widget childWidget = display.getWidget(child);
					if (childWidget != null && childWidget instanceof Control && childWidget != this) {
						children[childrenCount] = (Control)childWidget;
						childrenCount++;
					}
				}
			} else {
				long list = GTK3.gtk_container_get_children (parentHandle);
				if (list != 0) {
					long handle = OS.g_list_data (list);
					if (handle != 0) {
						Widget widget = display.getWidget (handle);
						if (widget != null && widget != this) {
							if (widget instanceof Control) {
								children [childrenCount++] = (Control) widget;
							}
						}
					}
					OS.g_list_free (list);
				}
			}
		}
	}

	if (childrenCount == itemCount + directCount) {
		return children;
	} else {
		Control[] newChildren;
		if (childrenCount == itemCount) {
			newChildren = children;
		} else {
			newChildren = new Control [childrenCount + directCount];
			System.arraycopy (children, 0, newChildren, 0, childrenCount);
		}
		System.arraycopy (directChildren, 0, newChildren, childrenCount, directCount);
		return newChildren;
	}
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

	if (GTK.GTK4) {
		long child = GTK4.gtk_widget_get_first_child(handle);
		if (child == 0) error(SWT.ERROR_CANNOT_GET_ITEM);
	} else {
		long list = GTK3.gtk_container_get_children (handle);
		if (list == 0) error (SWT.ERROR_CANNOT_GET_ITEM);
		int itemCount = OS.g_list_length (list);
		OS.g_list_free (list);
		if (!(0 <= index && index < itemCount)) error (SWT.ERROR_CANNOT_GET_ITEM);
	}

	return items [index];
}

/**
 * Returns the tab item at the given point in the receiver
 * or null if no such item exists. The point is in the
 * coordinate system of the receiver.
 *
 * @param point the point used to locate the item
 * @return the tab item at the given point, or null if the point is not in a tab item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public TabItem getItem(Point point) {
	checkWidget();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	int itemCount = getItemCount();
	for (int i = 0; i < itemCount; i++) {
		TabItem item = items[i];
		Rectangle rect = item.getBounds();
		if (rect.contains(point)) return item;
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

	int itemCount = 0;
	if (GTK.GTK4) {
		itemCount = GTK.gtk_notebook_get_n_pages(handle);
	} else {
		long list = GTK3.gtk_container_get_children (handle);
		if (list == 0) return 0;
		itemCount = OS.g_list_length (list);
		OS.g_list_free (list);
	}

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
	int index = GTK.gtk_notebook_get_current_page (handle);
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
	return GTK.gtk_notebook_get_current_page (handle);
}

@Override
long gtk_focus (long widget, long directionType) {
	return 0;
}

@Override
long gtk_switch_page(long notebook, long page, int page_num) {
	TabItem item = items[page_num];

	if (GTK.GTK4) {
		Control control = item.getControl();
		control.setBoundsInPixels(getClientAreaInPixels());
	} else {
		int index = GTK.gtk_notebook_get_current_page(handle);
		if (index != -1) {
			Control control = items [index].getControl();
			if (control != null && !control.isDisposed()) {
				control.setVisible(false);
			}
		} else {
			return 0;
		}

		Control control = item.getControl();
		if (control != null && !control.isDisposed()) {
			control.setBoundsInPixels(getClientAreaInPixels());
			control.setVisible(true);
		}
	}

	Event event = new Event();
	event.item = item;
	sendSelectionEvent(SWT.Selection, event, false);

	return 0;
}

@Override
void hookEvents () {
	super.hookEvents ();
	OS.g_signal_connect_closure (handle, OS.switch_page, display.getClosure (SWITCH_PAGE), false);
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
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (TabItem item) {
	checkWidget();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);

	int index = -1;
	int count = getItemCount();
	for (int i = 0; i < count; i++) {
		if (items [i] == item) {
			index = i;
			break;
		}
	}
	return index;
}

@Override
Point minimumSize (int wHint, int hHint, boolean flushCache) {
	Control [] children = _getChildren ();
	int width = 0, height = 0;
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		int index = 0;
		int count = getItemCount();
		while (index < count) {
			if (items [index].control == child) break;
			index++;
		}
		if (index == count) {
			Rectangle rect = DPIUtil.autoScaleUp(child.getBounds ());
			width = Math.max (width, rect.x + rect.width);
			height = Math.max (height, rect.y + rect.height);
		} else {
			/*
			 * Since computeSize can be overridden by subclasses, we cannot
			 * call computeSizeInPixels directly.
			 */
			Point size = DPIUtil.autoScaleUp(child.computeSize (DPIUtil.autoScaleDown(wHint), DPIUtil.autoScaleDown(hHint), flushCache));
			width = Math.max (width, size.x);
			height = Math.max (height, size.y);
		}
	}
	return new Point (width, height);
}

@Override
boolean mnemonicHit (char key) {
	int itemCount = getItemCount ();
	for (int i=0; i<itemCount; i++) {
		long labelHandle = items [i].labelHandle;
		if (labelHandle != 0 && mnemonicHit (labelHandle, key)) return true;
	}
	return false;
}

@Override
boolean mnemonicMatch (char key) {
	int itemCount = getItemCount ();
	for (int i=0; i<itemCount; i++) {
		long labelHandle = items [i].labelHandle;
		if (labelHandle != 0 && mnemonicHit (labelHandle, key)) return true;
	}
	return false;
}

@Override
void releaseChildren (boolean destroy) {
	if (items != null) {
		for (int i=0; i<items.length; i++) {
			TabItem item = items [i];
			if (item != null && !item.isDisposed ()) {
				item.release (false);
			}
		}
		items = null;
	}
	super.releaseChildren (destroy);
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	if (imageList != null) imageList.dispose ();
	imageList = null;
}

@Override
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
 * be notified when the user changes the receiver's selection.
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

@Override
void reskinChildren (int flags) {
	if (items != null) {
		int count = getItemCount();

		for (int i = 0; i < count; i++) {
			TabItem item = items [i];
			if (item != null) item.reskin(flags);
		}
	}
	super.reskinChildren (flags);
}

@Override
void setBackgroundGdkRGBA (long context, long handle, GdkRGBA rgba) {
	// Form background string
	String css = "notebook header {background-color: " + display.gtk_rgba_to_css_string (rgba) + ";}";

		// Cache background
		cssBackground = css;

		// Apply background color and any cached foreground color
	String finalCss = display.gtk_css_create_css_color_string (cssBackground, cssForeground, SWT.BACKGROUND);
	gtk_css_provider_load_from_css (context, finalCss);
}

@Override
int setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	int result = super.setBounds (x, y, width, height, move, resize);
	if ((result & RESIZED) != 0) {
		int index = getSelectionIndex ();
		if (index != -1) {
			TabItem item = items [index];
			Control control = item.control;
			if (control != null && !control.isDisposed ()) {
				control.setBoundsInPixels (getClientAreaInPixels ());
			}
		}
	}
	return result;
}

@Override
void setFontDescription (long font) {
	super.setFontDescription (font);
	TabItem [] items = getItems ();
	for (int i = 0; i < items.length; i++) {
		if (items[i] != null) {
			items[i].setFontDescription (font);
		}
	}
}

@Override
void setForegroundGdkRGBA (GdkRGBA rgba) {
	super.setForegroundGdkRGBA(rgba);
	TabItem [] items = getItems ();
	for (int i = 0; i < items.length; i++) {
		if (items[i] != null) {
			items[i].setForegroundRGBA (rgba);
		}
	}
}

@Override
void setOrientation (boolean create) {
	super.setOrientation (create);
	if (items != null) {
		for (int i=0; i<items.length; i++) {
			if (items[i] != null) items[i].setOrientation (create);
		}
	}
}

/**
 * Selects the item at the given zero-relative index in the receiver.
 * If the item at the index was already selected, it remains selected.
 * The current selection is first cleared, then the new items are
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
	int oldIndex = GTK.gtk_notebook_get_current_page (handle);
	if (oldIndex == index) return;
	if (oldIndex != -1) {
		TabItem item = items [oldIndex];
		Control control = item.control;
		if (control != null && !control.isDisposed ()) {
			control.setVisible (false);
		}
	}
	OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, SWITCH_PAGE);
	GTK.gtk_notebook_set_current_page (handle, index);
	OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, SWITCH_PAGE);
	int newIndex = GTK.gtk_notebook_get_current_page (handle);
	if (newIndex != -1) {
		TabItem item = items [newIndex];
		Control control = item.control;
		if (control != null && !control.isDisposed ()) {
			control.setBoundsInPixels (getClientAreaInPixels ());
			control.setVisible (true);
		}
		if (notify) {
			Event event = new Event ();
			event.item = item;
			sendSelectionEvent (SWT.Selection, event, true);
		}
	}
}

/**
 * Sets the receiver's selection to the given item.
 * The current selected is first cleared, then the new item is
 * selected.
 *
 * @param item the item to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setSelection (TabItem item) {
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (new TabItem [] {item});
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selected is first cleared, then the new items are
 * selected.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
 * </ul>
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

@Override
boolean traversePage (final boolean next) {
	if (next) {
		GTK.gtk_notebook_next_page (handle);
	} else {
		GTK.gtk_notebook_prev_page (handle);
	}
	return true;
}

}
