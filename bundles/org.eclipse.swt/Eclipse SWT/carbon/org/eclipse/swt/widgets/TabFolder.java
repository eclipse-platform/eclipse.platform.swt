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

 
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;

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
	int lastSelected = -1;
	
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
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
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

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	if (wHint == SWT.DEFAULT) {
		GC gc = new GC (this);
		for (int i = 0; i < items.length; i++) {
			if (items [i] != null) {
				width += items [i].calculateWidth (gc);
			}
		}
		gc.dispose ();
	}
	Point size;
	if (layout != null) {
		size = layout.computeSize (this, wHint, hHint, changed);
	} else {
		size = minimumSize (wHint, hHint, changed);
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
	checkWidget ();
	Rect oldBounds = new Rect ();
	OS.GetControlBounds (handle, oldBounds);
	Rect bounds = new Rect ();
	bounds.right = bounds.bottom = 100;
	OS.SetControlBounds (handle, bounds);
	Rect client = new Rect ();
	OS.GetTabContentRect (handle, client);
	OS.SetControlBounds (handle, oldBounds);
	x -= client.left - bounds.left;
	y -= client.top - bounds.top;
	width += (bounds.right - bounds.left) - (client.right - client.left);
	height += (bounds.bottom - bounds.top) - (client.bottom - client.top);
	Rect inset = getInset ();
	x -= inset.left;
	y -= inset.top;
	width += inset.left + inset.right;
	height += inset.top + inset.bottom;
	return new Rectangle (x, y, width, height);
}

void createHandle () {
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
	short direction = (style & SWT.BOTTOM) != 0 ? (short)OS.kControlTabDirectionSouth : (short)OS.kControlTabDirectionNorth;
	OS.CreateTabsControl (window, new Rect (), (short)OS.kControlTabSizeLarge, direction, (short) 0, 0, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
}

void createItem (TabItem item, int index) {
	int count = OS.GetControl32BitMaximum (handle);
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	OS.SetControl32BitMaximum (handle, count+1);
	if (count == items.length) {
		TabItem [] newItems = new TabItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	System.arraycopy (items, index, items, index + 1, count - index);
	items [index] = item;

	/*
	* Send a selection event when the item that is added becomes
	* the new selection.  This only happens when the first item
	* is added.
	*/
	if (count == 0) {
		OS.SetControl32BitValue (handle, 1);
		lastSelected = 0;
		Event event = new Event ();
		event.item = items [0];
		sendEvent (SWT.Selection, event);
		// the widget could be destroyed at this point
	}
}

void createWidget () {
	super.createWidget ();
	items = new TabItem [4];
}

void destroyItem (TabItem item) {
	int count = OS.GetControl32BitMaximum (handle);
	int index = 0;
	while (index < count) {
		if (items [index] == item) break;
		index++;
	}
	if (index == count) return;	// not found
	int selectionIndex = OS.GetControl32BitValue (handle) - 1;
	--count;
	OS.SetControl32BitMaximum (handle, count);
	if (count == 0) {
		items = new TabItem [4];
		return;
	}
	System.arraycopy (items, index + 1, items, index, count - index);
	items [count] = null;
	if (index == selectionIndex) {
		setSelection (Math.max (0, selectionIndex - 1));
		selectionIndex = getSelectionIndex ();
		if (selectionIndex != -1) {
			Event event = new Event ();
			event.item = items [selectionIndex];
			sendEvent (SWT.Selection, event);
			// the widget could be destroyed at this point
		}
	}
}

public Rectangle getClientArea () {
	checkWidget ();
	Rect bounds = new Rect ();
	OS.GetControlBounds (handle, bounds);
	Rect client = new Rect ();
	OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlTabContentRectTag, Rect.sizeof, client, null);
	int x = Math.max (0, client.left - bounds.left);
	int y = Math.max (0, client.top - bounds.top);
	int width = Math.max (0, client.right - client.left);
	int height = Math.max (0, client.bottom - client.top);
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
	int count = OS.GetControl32BitMaximum (handle);
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
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
	checkWidget ();
	return OS.GetControl32BitMaximum (handle);
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
	int count = OS.GetControl32BitMaximum (handle);
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
	int index = OS.GetControl32BitValue(handle) - 1;
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
	return OS.GetControl32BitValue (handle) - 1;
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
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = OS.GetControl32BitMaximum (handle);
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
		int count = OS.GetControl32BitMaximum (handle);
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

Rect getInset () {
	return (style & SWT.BOTTOM) != 0 ? display.tabFolderSouthInset : display.tabFolderNorthInset;
}

int kEventControlApplyBackground (int nextHandler, int theEvent, int userData) {
	/*
	* Feature in the Macintosh.  For some reason, the tab folder applies the
	* theme background when drawing even though a theme has not been set for
	* the window.  The fix is to avoid running the default handler. 
	*/
	return OS.noErr;
}

int kEventControlHit (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlHit (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	int index = OS.GetControl32BitValue (handle) - 1;
	if (index == lastSelected) return result;
	lastSelected = index;
	int count = OS.GetControl32BitMaximum (handle);
	for (int i = 0; i < count; i++) {
		if (i != index) {
			Control control = items [i].control;
			if (control != null && !control.isDisposed ())
				control.setVisible (false);
		}
	}
	TabItem item = null;
	if (index != -1) item = items [index];
	if (item != null) {
		Control control = item.control;
		if (control != null && !control.isDisposed ()) {
			control.setBounds (getClientArea ());
			control.setVisible (true);
		}
	}
	Event event = new Event ();
	event.item = item;
	postEvent (SWT.Selection, event);
	return OS.noErr;
}

void releaseWidget () {
	int count = OS.GetControl32BitMaximum (handle);
	for (int i=0; i<count; i++) {
		TabItem item = items [i];
		if (!item.isDisposed ()) item.releaseResources ();
	}
	items = null;
	super.releaseWidget ();
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

int setBounds (int c, int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	int result = super.setBounds(c, x, y, width, height, move, resize, events);
	if ((result & RESIZED) != 0) {
		int index = OS.GetControl32BitValue (handle) - 1;
		if (index != -1) {
			TabItem item = items [index];
			Control control = item.control;
			if (control != null && !control.isDisposed ()) {
				control.setBounds (getClientArea ());
			}
		}
	}
	return result;
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
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (items.length == 0) {
		setSelection (-1);
		return;
	}
	for (int i=items.length - 1; i>=0; --i) {
		int index = indexOf (items [i]);
		if (index != -1) setSelection (index);
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
	setSelection (index, false);
}

void setSelection (int index, boolean notify) {
	if (index >= OS.GetControl32BitMaximum (handle)) return;
	int currentIndex = OS.GetControl32BitValue (handle) - 1;
	if (currentIndex != -1) {
		TabItem item = items [currentIndex];
		if (item != null) {
			Control control = item.control;
			if (control != null && !control.isDisposed ()) {
				control.setVisible (false);
			}
		}
	}
	OS.SetControl32BitValue (handle, index+1);
	index = OS.GetControl32BitValue (handle) - 1;
	lastSelected = index;
	if (index != -1) {
		TabItem item = items [index];
		if (item != null) {
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
}

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
	setSelection (index, true);
	return index == getSelectionIndex ();
}
}
