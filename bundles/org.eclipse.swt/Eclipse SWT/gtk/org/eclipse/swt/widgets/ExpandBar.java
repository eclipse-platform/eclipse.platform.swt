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
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class support the layout of selectable
 * expand bar items.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>ExpandItem</code>.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>V_SCROLL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Expand, Collapse</dd>
 * </dl>
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see ExpandItem
 * @see ExpandEvent
 * @see ExpandListener
 * @see ExpandAdapter
 * @see <a href="http://www.eclipse.org/swt/snippets/#expandbar">ExpandBar snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.2
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ExpandBar extends Composite {
	ExpandItem [] items;
	ExpandItem lastFocus;
	int itemCount;
	int spacing;
	int yCurrentScroll;

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
 * @see SWT#V_SCROLL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ExpandBar (Composite parent, int style) {
	super (parent, style);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when an item in the receiver is expanded or collapsed
 * by sending it one of the messages defined in the <code>ExpandListener</code>
 * interface.
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
 * @see ExpandListener
 * @see #removeExpandListener
 */
public void addExpandListener (ExpandListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Expand, typedListener);
	addListener (SWT.Collapse, typedListener);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	if (OS.GTK_VERSION >= OS.VERSION (2, 4, 0)) {
		if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
		if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
		Point size = computeNativeSize (handle, wHint, hHint, changed);
		int border = OS.gtk_container_get_border_width (handle);
		size.x += 2 * border;
		size.y += 2 * border;
		return size;
	} else {
		int height = 0, width = 0;
		if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
			if (itemCount > 0) {
				height += spacing;
				GC gc = new GC (this);
				for (int i = 0; i < itemCount; i++) {
					ExpandItem item = items [i];
					height += item.getHeaderHeight ();
					if (item.expanded) height += item.height;
					height += spacing;
					width = Math.max (width, item.getPreferredWidth (gc));
				}
				gc.dispose ();
			}
		}
		if (width == 0) width = DEFAULT_WIDTH;
		if (height == 0) height = DEFAULT_HEIGHT;
		if (wHint != SWT.DEFAULT) width = wHint;
		if (hHint != SWT.DEFAULT) height = hHint;
		return new Point (width, height);	
	} 
}

void createHandle (int index) {
	state |= HANDLE;
	if (OS.GTK_VERSION >= OS.VERSION (2, 4, 0)) {
		fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
		if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_fixed_set_has_window (fixedHandle, true);
		handle = OS.gtk_vbox_new (false, 0);
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		if ((style & SWT.V_SCROLL) != 0) {
			scrolledHandle = OS.gtk_scrolled_window_new (0, 0);
			if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
			OS.gtk_scrolled_window_set_policy (scrolledHandle, OS.GTK_POLICY_NEVER, OS.GTK_POLICY_AUTOMATIC);
			OS.gtk_container_add (fixedHandle, scrolledHandle);
			OS.gtk_scrolled_window_add_with_viewport (scrolledHandle, handle);
			int /*long*/ viewport = OS.gtk_bin_get_child (scrolledHandle);
			OS.gtk_viewport_set_shadow_type (viewport, OS.GTK_SHADOW_NONE);
		} else {
			OS.gtk_container_add (fixedHandle, handle);
		}
		OS.gtk_container_set_border_width (handle, 0);
	} else {
		int /*long*/ topHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
		if (topHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_fixed_set_has_window (topHandle, true);
		if ((style & SWT.V_SCROLL) != 0) {
			fixedHandle = topHandle;
			scrolledHandle = OS.gtk_scrolled_window_new (0, 0);
			if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
			handle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			OS.gtk_fixed_set_has_window (handle, true);
			OS.gtk_container_add (fixedHandle, scrolledHandle);
				
			/*
			* Force the scrolledWindow to have a single child that is
			* not scrolled automatically.  Calling gtk_container_add()
			* seems to add the child correctly but cause a warning.
			*/
			boolean warnings = display.getWarnings ();
			display.setWarnings (false);
			OS.gtk_container_add (scrolledHandle, handle);
			display.setWarnings (warnings);
		} else {
			handle = topHandle;
		}
		OS.GTK_WIDGET_SET_FLAGS (handle, OS.GTK_CAN_FOCUS);
	}
}

void createItem (ExpandItem item, int style, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	if (itemCount == items.length) {
		ExpandItem [] newItems = new ExpandItem [itemCount + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	System.arraycopy (items, index, items, index + 1, itemCount - index);
	items [index] = item;
	itemCount++;
	if (OS.GTK_VERSION < OS.VERSION (2, 4, 0)) {
		if (lastFocus == null) lastFocus = item;
	}
	item.width = Math.max (0, getClientArea ().width - spacing * 2);
	layoutItems (index, true);
}

void createWidget (int index) {
	super.createWidget (index);
	items = new ExpandItem [4];	
}

void destroyItem (ExpandItem item) {
	int index = 0;
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	if (index == itemCount) return;
	if (OS.GTK_VERSION < OS.VERSION (2, 4, 0)) {
		if (item == lastFocus) {
			int focusIndex = index > 0 ? index - 1 : 1;
			if (focusIndex < itemCount) {
				lastFocus = items [focusIndex];
				lastFocus.redraw ();
			} else {
				lastFocus = null;
			}
		}
	}
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	item.redraw ();
	layoutItems (index, true);
}

int /*long*/ eventHandle () {
	return OS.GTK_VERSION >= OS.VERSION (2, 4, 0) ? fixedHandle : handle;
}

boolean forceFocus (int /*long*/ focusHandle) {
	if (OS.GTK_VERSION >= OS.VERSION (2, 4, 0)) {
		if (lastFocus != null && lastFocus.setFocus ()) return true;
		for (int i = 0; i < itemCount; i++) {
			ExpandItem item = items [i];
			if (item.setFocus ()) return true;
		}
	}
	return super.forceFocus (focusHandle);
}

boolean hasFocus () {
	if (OS.GTK_VERSION >= OS.VERSION (2, 4, 0)) {
		for (int i=0; i<itemCount; i++) {
			ExpandItem item = items [i];
			if (item.hasFocus ()) return true;
		}
	}
	return super.hasFocus();
}

void hookEvents () {
	super.hookEvents ();
	if (OS.GTK_VERSION >= OS.VERSION (2, 4, 0)) {
		if (scrolledHandle != 0) {
			OS.g_signal_connect_closure (scrolledHandle, OS.size_allocate, display.closures [SIZE_ALLOCATE], true);
		}
	}
}

int getBandHeight () {
	if (font == null) return ExpandItem.CHEVRON_SIZE;
	GC gc = new GC (this);
	FontMetrics metrics = gc.getFontMetrics ();
	gc.dispose ();
	return Math.max (ExpandItem.CHEVRON_SIZE, metrics.getHeight ());
}

GdkColor getForegroundColor () {
	if (OS.GTK_VERSION < OS.VERSION (2, 4, 0)) {
		if ((state & FOREGROUND) == 0) {
			return display.getSystemColor (SWT.COLOR_TITLE_FOREGROUND).handle;
		}
	}
	return super.getForegroundColor ();
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
public ExpandItem getItem (int index) {
	checkWidget();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
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
	return itemCount;
}

/**
 * Returns an array of <code>ExpandItem</code>s which are the items
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
public ExpandItem [] getItems () {
	checkWidget ();
	ExpandItem [] result = new ExpandItem [itemCount];
	System.arraycopy (items, 0, result, 0, itemCount);
	return result;
}

/**
 * Returns the receiver's spacing.
 *
 * @return the spacing
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSpacing () {
	checkWidget ();
	return spacing;
}

int /*long*/ gtk_button_press_event (int /*long*/ widget, int /*long*/ event) {
	if (OS.GTK_VERSION < OS.VERSION (2, 4, 0)) {
		GdkEventButton gdkEvent = new GdkEventButton ();
		OS.memmove (gdkEvent, event, GdkEventButton.sizeof);
		int x = (int)gdkEvent.x;
		int y = (int)gdkEvent.y;
		for (int i = 0; i < itemCount; i++) {
			ExpandItem item = items[i];
			boolean hover = item.x <= x && x < (item.x + item.width) && item.y <= y && y < (item.y + getBandHeight ());
			if (hover && item != lastFocus) {
				lastFocus.redraw ();
				lastFocus = item;
				lastFocus.redraw ();
				forceFocus ();
				break;
			}
		}
	}
	return super.gtk_button_press_event (widget, event);
}

int /*long*/ gtk_button_release_event (int /*long*/ widget, int /*long*/ event) {
	if (OS.GTK_VERSION < OS.VERSION (2, 4, 0)) {
		if (lastFocus != null) {
			GdkEventButton gdkEvent = new GdkEventButton ();
			OS.memmove (gdkEvent, event, GdkEventButton.sizeof);
			int x = (int)gdkEvent.x;
			int y = (int)gdkEvent.y;
			boolean hover = lastFocus.x <= x && x < (lastFocus.x + lastFocus.width) && lastFocus.y <= y && y < (lastFocus.y + getBandHeight ()); 
			if (hover) {
				Event ev = new Event ();
				ev.item = lastFocus;
				notifyListeners (lastFocus.expanded ? SWT.Collapse : SWT.Expand, ev);
				lastFocus.expanded = !lastFocus.expanded;
				showItem (lastFocus);
			}
		}
	}
	return super.gtk_button_release_event (widget, event);
}

int /*long*/ gtk_expose_event (int /*long*/ widget, int /*long*/ eventPtr) {
	if ((state & OBSCURED) != 0) return 0;
	if (OS.GTK_VERSION < OS.VERSION (2, 4, 0)) {
		GdkEventExpose gdkEvent = new GdkEventExpose ();
		OS.memmove(gdkEvent, eventPtr, GdkEventExpose.sizeof);
		GCData data = new GCData ();
		data.damageRgn = gdkEvent.region;
		GC gc = GC.gtk_new (this, data);
		OS.gdk_gc_set_clip_region (gc.handle, gdkEvent.region);
		boolean hasFocus = isFocusControl ();
		for (int i = 0; i < itemCount; i++) {
			ExpandItem item = items [i];
			item.drawItem (gc, hasFocus && item == lastFocus);
		}
		gc.dispose ();
	}
	return super.gtk_expose_event (widget, eventPtr);
}

int /*long*/ gtk_focus_in_event (int /*long*/ widget, int /*long*/ event) {
	if (OS.GTK_VERSION < OS.VERSION (2, 4, 0)) {
		if (lastFocus != null) lastFocus.redraw ();
	}
	return super.gtk_focus_in_event(widget, event);
}

int /*long*/ gtk_focus_out_event (int /*long*/ widget, int /*long*/ event) {
	if (OS.GTK_VERSION < OS.VERSION (2, 4, 0)) {
		if (lastFocus != null) lastFocus.redraw ();
	}
	return super.gtk_focus_out_event (widget, event);
}

int /*long*/ gtk_key_press_event (int /*long*/ widget, int /*long*/ event) {
	if (OS.GTK_VERSION >= OS.VERSION (2, 4, 0)) {
		if (!hasFocus ()) return 0;
		int /*long*/ result = super.gtk_key_press_event (widget, event);
		if (result != 0) return result;
		int index = 0;
		while (index < itemCount) {
			if (items [index].hasFocus ()) break;
			index++;
		}
		GdkEventKey gdkEvent = new GdkEventKey ();
		OS.memmove (gdkEvent, event, GdkEventKey.sizeof);
		boolean next = false;
		switch (gdkEvent.keyval) {
			case OS.GDK_Up:
			case OS.GDK_Left: next = false; break;
			case OS.GDK_Down: 
			case OS.GDK_Right: next = true; break;
			default: return result;
		}
		int start = index, offset = next ? 1 : -1;
		while ((index = (index + offset + itemCount) % itemCount) != start) {
			ExpandItem item = items [index];
			if (item.setFocus ()) return result;
		}
		return result;
	} else {
		if (lastFocus != null) {
			GdkEventKey keyEvent = new GdkEventKey ();
			OS.memmove (keyEvent, event, GdkEventKey.sizeof);
			switch (keyEvent.keyval) {
				case OS.GDK_Return:
				case OS.GDK_space:
					Event ev = new Event ();
					ev.item = lastFocus;
					sendEvent (lastFocus.expanded ? SWT.Collapse :SWT.Expand, ev);
					lastFocus.expanded = !lastFocus.expanded;
					showItem (lastFocus);
					break;
				case OS.GDK_Up:
				case OS.GDK_KP_Up: {
					int focusIndex = indexOf (lastFocus);
					if (focusIndex > 0) {
						lastFocus.redraw ();
						lastFocus = items [focusIndex - 1];
						lastFocus.redraw ();
					}
					break;
				}
				case OS.GDK_Down:
				case OS.GDK_KP_Down: {
					int focusIndex = indexOf (lastFocus);
					if (focusIndex < itemCount - 1) {
						lastFocus.redraw ();
						lastFocus = items [focusIndex + 1];
						lastFocus.redraw ();
					}
					break;
				}
			}
		}
	}
	return super.gtk_key_press_event (widget, event);
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
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (ExpandItem item) {
	checkWidget();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i = 0; i < itemCount; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

void layoutItems (int index, boolean setScrollbar) {
	if (OS.GTK_VERSION >= OS.VERSION (2, 4, 0)) {
		for (int i = 0; i < itemCount; i++) {
			ExpandItem item = items [i];
			if (item != null) item.resizeControl (yCurrentScroll);
		}
	} else {
		if (index < itemCount) {
			int y = spacing - yCurrentScroll;
			for (int i = 0; i < index; i++) {
				ExpandItem item = items [i];
				if (item.expanded) y += item.height;
				y += item.getHeaderHeight() + spacing;
			}
			for (int i = index; i < itemCount; i++) {
				ExpandItem item = items [i];
				item.setBounds (spacing, y, 0, 0, true, false);
				if (item.expanded) y += item.height;
				y += item.getHeaderHeight() + spacing;
			}
		}
		if (setScrollbar) setScrollbar ();
	}
}

int /*long*/ gtk_size_allocate (int /*long*/ widget, int /*long*/ allocation) {
	int /*long*/ result = super.gtk_size_allocate (widget, allocation);
	layoutItems (0, false);
	return result;
}

int /*long*/ parentingHandle () {
	return OS.GTK_VERSION >= OS.VERSION (2, 4, 0) ? fixedHandle : handle;
}

void releaseChildren (boolean destroy) {
	for (int i = 0; i < itemCount; i++) {
		ExpandItem item = items [i];
		if (item != null && !item.isDisposed ()) {
			item.release (false);
		}
	}
	super.releaseChildren (destroy);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when items in the receiver are expanded or collapsed.
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
 * @see ExpandListener
 * @see #addExpandListener
 */
public void removeExpandListener (ExpandListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Expand, listener);
	eventTable.unhook (SWT.Collapse, listener);
}

void reskinChildren (int flags) {
	if (items != null) {
		for (int i=0; i<items.length; i++) {
			ExpandItem item = items [i];
			if (item != null ) item.reskin (flags);
		}
	}
	super.reskinChildren (flags);
}

int setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	int result = super.setBounds (x, y, width, height, move, resize);
	if (OS.GTK_VERSION < OS.VERSION (2, 4, 0)) {
		if (resize) {
			if ((style & SWT.V_SCROLL) != 0) {
				setScrollbar ();
			} else {
				for (int i = 0; i < itemCount; i++) {
					ExpandItem item = items [i];
					int newWidth = Math.max (0, getClientArea ().width - spacing * 2);
					if (item.width != newWidth) {
						item.setBounds (0, 0, newWidth, item.height, false, true);
					}
				}
			}
		}
	}
	return result;
}

void setFontDescription (int /*long*/ font) {
	super.setFontDescription (font);
	if (OS.GTK_VERSION >= OS.VERSION (2, 4, 0)) {
		for (int i = 0; i < itemCount; i++) {
			items[i].setFontDescription (font);
		}
		layoutItems (0, true);
	}
}

void setForegroundColor (GdkColor color) {
	super.setForegroundColor (color);
	if (OS.GTK_VERSION >= OS.VERSION (2, 4, 0)) {
		for (int i = 0; i < itemCount; i++) {
			items[i].setForegroundColor (color);
		}
	}
}

void setOrientation (boolean create) {
	super.setOrientation (create);
	if (items != null) {
		for (int i=0; i<items.length; i++) {
			if (items[i] != null) items[i].setOrientation (create);
		}
	}
}

void setScrollbar () {
	if (itemCount == 0) return;
	if ((style & SWT.V_SCROLL) == 0) return;
	int height = getClientArea ().height;
	ExpandItem item = items [itemCount - 1];
	int maxHeight = item.y + getBandHeight () + spacing;
	if (item.expanded) maxHeight += item.height;
	int /*long*/ adjustmentHandle = OS.gtk_scrolled_window_get_vadjustment (scrolledHandle);
	GtkAdjustment adjustment = new GtkAdjustment ();
	OS.memmove (adjustment, adjustmentHandle);
	yCurrentScroll = (int)adjustment.value;

	//claim bottom free space
	if (yCurrentScroll > 0 && height > maxHeight) {
		yCurrentScroll = Math.max (0, yCurrentScroll + maxHeight - height);
		layoutItems (0, false);
	}
	maxHeight += yCurrentScroll;	
	adjustment.value = Math.min (yCurrentScroll, maxHeight);
	adjustment.upper = maxHeight;
	adjustment.page_size = height;
	OS.memmove (adjustmentHandle, adjustment);
	OS.gtk_adjustment_changed (adjustmentHandle);
	int policy = maxHeight > height ? OS.GTK_POLICY_ALWAYS : OS.GTK_POLICY_NEVER;
	OS.gtk_scrolled_window_set_policy (scrolledHandle, OS.GTK_POLICY_NEVER, policy);
	int width = OS.GTK_WIDGET_WIDTH (fixedHandle) - spacing * 2;
	if (policy == OS.GTK_POLICY_ALWAYS) {
		int /*long*/ vHandle = OS.GTK_SCROLLED_WINDOW_VSCROLLBAR (scrolledHandle);
		GtkRequisition requisition = new GtkRequisition ();
		OS.gtk_widget_size_request (vHandle, requisition);
		width -= requisition.width;
	}
	width = Math.max (0,  width);
	for (int i = 0; i < itemCount; i++) {
		ExpandItem item2 = items[i];
		item2.setBounds (0, 0, width, item2.height, false, true);
	}
}

/**
 * Sets the receiver's spacing. Spacing specifies the number of pixels allocated around 
 * each item.
 * 
 * @param spacing the spacing around each item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSpacing (int spacing) {
	checkWidget ();
	if (spacing < 0) return;
	if (spacing == this.spacing) return;
	this.spacing = spacing;
	if (OS.GTK_VERSION >= OS.VERSION (2, 4, 0)) {
		OS.gtk_box_set_spacing (handle, spacing);
		OS.gtk_container_set_border_width (handle, spacing);
	} else {
		if ((style & SWT.V_SCROLL) == 0) {
			int width = Math.max (0, getClientArea ().width - spacing * 2);
			for (int i = 0; i < itemCount; i++) {
				ExpandItem item = items [i];
				if (item.width != width) item.setBounds (0, 0, width, item.height, false, true);
			}
		}
		layoutItems (0, true);
		redraw ();
	}
}

void showItem (ExpandItem item) {
	Control control = item.control;
	if (control != null && !control.isDisposed ()) {
		control.setVisible (item.expanded);
	}
	item.redraw ();
	int index = indexOf (item);
	layoutItems (index + 1, true);
}

void updateScrollBarValue (ScrollBar bar) {
	yCurrentScroll = bar.getSelection();
	layoutItems (0, false);
}
}
