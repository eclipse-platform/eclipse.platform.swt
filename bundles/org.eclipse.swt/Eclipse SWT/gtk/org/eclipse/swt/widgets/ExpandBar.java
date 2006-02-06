/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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

public class ExpandBar extends Composite {
	
public ExpandBar (Composite parent, int style) {
	super (parent, style);
}

public void addExpandListener (ExpandListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Expand, typedListener);
	addListener (SWT.Collapse, typedListener);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	Point size = computeNativeSize (handle, wHint, hHint, changed);
	int border = OS.gtk_container_get_border_width (handle);
	size.x += 2 * border;
	size.y += 2 * border;
	return size;
}

void createHandle (int index) {
	if (OS.GTK_VERSION < OS.VERSION (2, 4, 0)) {
		// TODO
		error (SWT.ERROR_NO_HANDLES);
	}
	state |= HANDLE;
	fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_fixed_set_has_window (fixedHandle, true);
	handle = OS.gtk_vbox_new (false, 0);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & SWT.V_SCROLL) != 0) {
		scrolledHandle = OS.gtk_scrolled_window_new (0, 0);
		if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
		int vsp = (style & SWT.V_SCROLL) != 0 ? OS.GTK_POLICY_AUTOMATIC : OS.GTK_POLICY_NEVER;
		OS.gtk_scrolled_window_set_policy (scrolledHandle, OS.GTK_POLICY_NEVER, vsp);
		OS.gtk_container_add (fixedHandle, scrolledHandle);
		OS.gtk_scrolled_window_add_with_viewport (scrolledHandle, handle);
	} else {
		OS.gtk_container_add (fixedHandle, handle);
	}
	OS.gtk_container_set_border_width (handle, 0);
}

int /*long*/ eventHandle () {
	return fixedHandle;
}

public int indexOf (ExpandItem item) {
	checkWidget();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	ExpandItem [] items = getItems ();
	for (int i = 0; i < items.length; i++) {
		if (item == items [i]) return i;
	}
	return -1;
}

public ExpandItem [] getItems () {
	checkWidget();
	int /*long*/ list = OS.gtk_container_get_children (handle);
	if (list == 0) return new ExpandItem [0];
	int count = OS.g_list_length (list);
	ExpandItem [] result = new ExpandItem [count];
	for (int i=0; i<count; i++) {
		int /*long*/ data = OS.g_list_nth_data (list, i);
		Widget widget = display.getWidget (data);
		result [i] = (ExpandItem) widget;
	}
	OS.g_list_free (list);
	return result;
}

public ExpandItem getItem (int index) {
	checkWidget();
	if (!(0 <= index && index < getItemCount ())) error (SWT.ERROR_INVALID_RANGE);
	return getItems () [index];
}

public int getItemCount () {
	checkWidget();
	int /*long*/ list = OS.gtk_container_get_children (handle);
	if (list == 0) return 0;
	int itemCount = OS.g_list_length (list);
	OS.g_list_free (list);
	return itemCount;
}

public int getSpacing () {
	checkWidget ();
	return OS.gtk_container_get_border_width (handle);
}

void relayout () {
	ExpandItem [] items = getItems ();
	int yScroll = 0;
	if (scrolledHandle != 0) {
		int adjustmentHandle = OS.gtk_scrolled_window_get_vadjustment (scrolledHandle);
		GtkAdjustment adjustment = new GtkAdjustment ();
		OS.memmove (adjustment, adjustmentHandle);
		yScroll = (int)adjustment.value;
	}
	for (int i=0; i<items.length; i++) {
		ExpandItem item = items [i];
		if (item != null) item.resizeControl (yScroll);
	}
}

public void removeExpandListener(ExpandListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Expand, listener);
	eventTable.unhook (SWT.Collapse, listener);
}

public void setSpacing (int spacing) {
	checkWidget ();
	if (spacing < 0) return;
	OS.gtk_box_set_spacing (handle, spacing);
	OS.gtk_container_set_border_width (handle, spacing);
}

void updateScrollBarValue (ScrollBar bar) {
	relayout ();
}
}
