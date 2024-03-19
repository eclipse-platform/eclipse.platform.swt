/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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


import org.eclipse.swt.internal.cocoa.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

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
	TabItem [] items;
	int itemCount;
	boolean ignoreSelect;

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
	addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
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

@Override
public Point computeSize (int wHint, int hHint, boolean changed) {
	Point size = super.computeSize (wHint, hHint, changed);
	if (wHint == SWT.DEFAULT && items.length > 0) {
		NSSize minSize = ((NSTabView)view).minimumSize();
		Rectangle trim = computeTrim (0, 0, (int)Math.ceil (minSize.width), 0);
		size.x = Math.max (trim.width, size.x);
	}
	return size;
}

@Override
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	NSTabView widget = (NSTabView)view;
	NSRect rect = widget.contentRect ();
	x -= rect.x;
	y -= rect.y;
	NSRect frame = widget.frame();
	width += Math.ceil (frame.width - rect.width);
	height += Math.ceil (frame.height - rect.height);
	return super.computeTrim (x, y, width, height);
}

@Override
void createHandle () {
	NSTabView widget = (NSTabView)new SWTTabView().alloc();
	widget.init ();
	widget.setDelegate(widget);
	if ((style & SWT.BOTTOM) != 0) {
		widget.setTabViewType(OS.NSBottomTabsBezelBorder);
	}
	view = widget;
}

void createItem (TabItem item, int index) {
	int count = itemCount;
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	if (count == items.length) {
		TabItem [] newItems = new TabItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	System.arraycopy (items, index, items, index + 1, count - index);
	items [index] = item;
	itemCount++;
	NSTabViewItem nsItem = (NSTabViewItem)new SWTTabViewItem().alloc().init();
	item.nsItem = nsItem;
	item.createJNIRef ();
	item.register ();
	((NSTabView)view).insertTabViewItem(nsItem, index);
}

@Override
void createWidget () {
	super.createWidget ();
	items = new TabItem [4];
}

@Override
NSFont defaultNSFont () {
	return display.tabViewFont;
}

void destroyItem (TabItem item) {
	int count = itemCount;
	int index = 0;
	while (index < count) {
		if (items [index] == item) break;
		index++;
	}
	if (index == count) return;
	--count;
	System.arraycopy (items, index + 1, items, index, count - index);
	items [count] = null;
	if (count == 0) {
		items = new TabItem [4];
	}
	itemCount = count;
	((NSTabView)view).removeTabViewItem(item.nsItem);
}

@Override
void drawBackground (long id, NSGraphicsContext context, NSRect rect) {
	if (id != view.id) return;
	fillBackground (view, context, rect, -1);
}

@Override
Widget findTooltip (NSPoint pt) {
	pt = view.convertPoint_fromView_ (pt, null);
	NSTabViewItem nsItem = ((NSTabView)view).tabViewItemAtPoint (pt);
	if (nsItem != null) {
		for (int i = 0; i < itemCount; i++) {
			TabItem item = items [i];
			if (item.nsItem.id == nsItem.id) return item;
		}
	}
	return super.findTooltip (pt);
}

@Override
public Rectangle getClientArea () {
	checkWidget ();
	NSRect rect = ((NSTabView)view).contentRect();
	int x = Math.max (0, (int)rect.x);
	int y = Math.max (0, (int)rect.y);
	int width = Math.max (0, (int)Math.ceil (rect.width));
	int height = Math.max (0, (int)Math.ceil (rect.height));
	return new Rectangle (x, y, width, height);
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
	checkWidget ();
	int count = itemCount;
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
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
public TabItem getItem (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	NSPoint nsPoint = new NSPoint ();
	nsPoint.x = point.x;
	nsPoint.y = point.y;
	NSTabView tabView = (NSTabView) view;
	NSTabViewItem tabViewItem = tabView.tabViewItemAtPoint (nsPoint);
	for (int i = 0; i < itemCount; i++) {
		NSTabViewItem item = items[i].nsItem;
		if (item.isEqual (tabViewItem)) {
			return items [i];
		}
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
	checkWidget ();
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
	checkWidget ();
	int count = itemCount;
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
	checkWidget ();
	int index = getSelectionIndex ();
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
	checkWidget ();
	NSTabViewItem selected =  ((NSTabView)view).selectedTabViewItem();
	if (selected == null) return -1;
	for (int i = 0; i < itemCount; i++) {
		if (items[i].nsItem.id == selected.id) return i;
	}
	return -1;
}

@Override
float getThemeAlpha () {
	return (background != null ? 1 : 0.25f) * parent.getThemeAlpha ();
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
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = itemCount;
	for (int i=0; i<count; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

@Override
boolean isTransparent() {
	return true;
}

@Override
Point minimumSize (int wHint, int hHint, boolean flushCache) {
	Control [] children = _getChildren ();
	int width = 0, height = 0;
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		int index = 0;
		int count = itemCount;
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
void removeControl (Control control) {
	super.removeControl (control);
	int count = itemCount;
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
		for (int i=0; i<itemCount; i++) {
			TabItem item = items [i];
			if (item != null) item.reskin (flags);
		}
	}
	super.reskinChildren (flags);
}

@Override
void setFont (NSFont font) {
	((NSTabView)view).setFont(font);
	int index = getSelectionIndex();
	for (int i = 0; i < itemCount; i++) {
		items[i].updateText(i == index);
	}
}

@Override
void setForeground (double [] color) {
	super.setForeground(color);
	int index = getSelectionIndex();
	for (int i = 0; i < itemCount; i++) {
		items[i].updateText(i == index);
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
	checkWidget ();
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
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (items.length == 0) {
		setSelection (-1, false, false);
	} else {
		for (int i=items.length - 1; i>=0; --i) {
			int index = indexOf (items [i]);
			if (index != -1) setSelection (index, false, false);
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
	int count = itemCount;
	if (!(0 <= index && index < count)) return;
	setSelection (index, false, false);
}

void setSelection (int index, boolean notify, boolean force) {
	if (!(0 <= index && index < itemCount)) return;
	int currentIndex = getSelectionIndex ();
	if (!force && currentIndex == index) return;
	if (currentIndex != -1) {
		TabItem item = items [currentIndex];
		if (item != null) {
			Control control = item.control;
			if (control != null && !control.isDisposed ()) {
				control.setVisible (false);
			}
		}
	}
	ignoreSelect = true;
	((NSTabView)view).selectTabViewItemAtIndex(index);
	ignoreSelect = false;
	index = getSelectionIndex();
	if (index != -1) {
		TabItem item = items [index];
		if (item != null) {
			Control control = item.control;
			if (control != null && !control.isDisposed ()) {
				control.setVisible (true);
			}
			if (notify) {
				Event event = new Event ();
				event.item = item;
				sendSelectionEvent (SWT.Selection, event, true);
			}
		}
	}
}

@Override
void setSmallSize () {
	((NSTabView)view).setControlSize (OS.NSSmallControlSize);
}

@Override
boolean traversePage (boolean next) {
	int count = getItemCount ();
	if (count == 0) return false;
	int index = getSelectionIndex ();
	if (index == -1) {
		index = 0;
	} else {
		int offset = (next) ? 1 : -1;
		index = (index + offset + count) % count;
	}
	setSelection (index, true, false);
	return index == getSelectionIndex ();
}

@Override
void tabView_willSelectTabViewItem(long id, long sel, long tabView, long tabViewItem) {
	if (tabViewItem == 0) return;
	for (int i = 0; i < itemCount; i++) {
		TabItem item = items [i];
		if (item.nsItem.id == tabViewItem) {
			int currentIndex = getSelectionIndex ();
			if (currentIndex != -1) {
				TabItem selected = items [currentIndex];
				if (selected != null) {
					selected.updateText(false);
					Control control = selected.control;
					if (control != null && !control.isDisposed ()) {
						control.setVisible (false);
					}
				}
			}
			Control control = item.control;
			if (control != null && !control.isDisposed ()) {
				control.setVisible (true);
			}
			item.updateText(true);
			break;
		}
	}
}

@Override
void tabView_didSelectTabViewItem(long id, long sel, long tabView, long tabViewItem) {
	if (tabViewItem == 0) return;
	for (int i = 0; i < itemCount; i++) {
		TabItem item = items [i];
		/*
		* Feature in Cocoa.  For some reason the control on a tab being
		* deselected has its parent removed natively.  The fix is to
		* re-set the control's parent.
		*/
		Control control = item.control;
		if (control != null) {
			NSView topView = control.topView ();
			if (topView.superview () == null) {
				contentView ().addSubview (topView, OS.NSWindowBelow, null);
			}
		}
		if (item.nsItem.id == tabViewItem) {
			if (!ignoreSelect) {
				Event event = new Event ();
				event.item = item;
				sendSelectionEvent (SWT.Selection, event, false);
			}
		}
	}
}

}
