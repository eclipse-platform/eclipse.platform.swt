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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;

public class ExpandItem extends Item {
	ExpandBar parent;
	Control control;
	ImageList imageList;
	int /*long*/ clientHandle, boxHandle, labelHandle, imageHandle;
	
public ExpandItem (ExpandBar parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget (parent.getItemCount ());
}

public ExpandItem (ExpandBar parent, int style, int index) {
	super (parent, style);
	this.parent = parent;
	int count = parent.getItemCount ();
	if (!(0 <= index && index <= count)) {
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	createWidget (index);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void createHandle (int index) {
	state |= HANDLE;
	handle = OS.gtk_expander_new (null);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	clientHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (clientHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_container_add (handle, clientHandle);	
	boxHandle = OS.gtk_hbox_new (false, 4);
	if (boxHandle == 0) error (SWT.ERROR_NO_HANDLES);
	labelHandle = OS.gtk_label_new (null);
	if (labelHandle == 0) error (SWT.ERROR_NO_HANDLES);
	imageHandle = OS.gtk_image_new ();
	if (imageHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.gtk_container_add (boxHandle, imageHandle);
	OS.gtk_container_add (boxHandle, labelHandle);
	OS.gtk_expander_set_label_widget (handle, boxHandle);
}

void createWidget (int index) {
	super.createWidget (index);
	showWidget (index);
	parent.relayout ();
}

void deregister() {
	super.deregister();
	display.removeWidget (clientHandle);
	display.removeWidget (boxHandle);
	display.removeWidget (labelHandle);
	display.removeWidget (imageHandle);
}

public void dispose () {
	if (isDisposed ()) return;
	ExpandBar parent = this.parent;
	super.dispose ();
	parent.relayout ();
}

public Control getControl () {
	checkWidget ();
	return control;
}

public boolean getExpanded () {
	checkWidget ();
	return OS.gtk_expander_get_expanded (handle);
}

public int getHeight () {
	checkWidget ();
	GtkRequisition requisition = new GtkRequisition ();
	OS.gtk_widget_size_request (clientHandle, requisition);
	return requisition.height;
}

public ExpandBar getParent () {
	checkWidget();
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent;
}

int /*long*/ gtk_activate (int /*long*/ widget) {
	Event event = new Event ();
	event.item = this;
	int type = OS.gtk_expander_get_expanded (handle) ? SWT.Collapse : SWT.Expand;
	parent.sendEvent (type, event);
	return 0;
}

int gtk_size_allocate (int widget, int allocation) {
	parent.relayout ();
	return 0;
}

void hookEvents () {
	super.hookEvents();
	OS.g_signal_connect_closure (handle, OS.activate, display.closures [ACTIVATE], false);
	OS.g_signal_connect_closure (handle, OS.activate, display.closures [ACTIVATE_INVERSE], true);
	OS.g_signal_connect_closure (clientHandle, OS.size_allocate, display.closures [SIZE_ALLOCATE], true);
}

void register() {
	super.register();
	display.addWidget (clientHandle, this);
	display.addWidget (boxHandle, this);
	display.addWidget (labelHandle, this);
	display.addWidget (imageHandle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	clientHandle = boxHandle = labelHandle = imageHandle = 0;
}

void releaseWidget () {
	super.releaseWidget ();
	if (imageList != null) imageList.dispose ();
	imageList = null;
	parent = null;
	control = null;
}

void resizeControl (int yScroll) {
	if (control != null && !control.isDisposed ()) {
		boolean visible = OS.gtk_expander_get_expanded (handle);
		if (visible) {
			int x = OS.GTK_WIDGET_X (clientHandle);
			int y = OS.GTK_WIDGET_Y (clientHandle);
			if (x != -1 && y != -1) {
				int width = OS.GTK_WIDGET_WIDTH (clientHandle);
				int height = OS.GTK_WIDGET_HEIGHT (clientHandle);
				control.setBounds (x, y - yScroll, width, height, true, true);
			}
		}
		control.setVisible (visible);
	}
}

public void setControl (Control control) {
	checkWidget ();
	if (control != null) {
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	if (this.control == control) return;
	this.control = control;
	if (control != null) {
		control.setVisible (OS.gtk_expander_get_expanded (handle));
	}
	parent.relayout ();
}

public void setExpanded (boolean expanded) {
	checkWidget ();
	OS.gtk_expander_set_expanded (handle, expanded);
	parent.relayout ();
}

public void setImage (Image image) {
	super.setImage (image);
	if (imageList != null) imageList.dispose ();
	imageList = null;
	if (image != null) {
		if (image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		imageList = new ImageList ();
		int imageIndex = imageList.add (image);
		int /*long*/ pixbuf = imageList.getPixbuf (imageIndex);
		OS.gtk_image_set_from_pixbuf (imageHandle, pixbuf);
		if (text.length () == 0) OS.gtk_widget_hide (labelHandle);
		OS.gtk_widget_show (imageHandle);
	} else {
		OS.gtk_image_set_from_pixbuf (imageHandle, 0);
		OS.gtk_widget_show (labelHandle);
		OS.gtk_widget_hide (imageHandle);
	}
}

public void setHeight (int height) {
	checkWidget ();
	if (height < 0) return;
	OS.gtk_widget_set_size_request (clientHandle, -1, height);
	parent.relayout ();
}

public void setText (String string) {
	super.setText (string);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	OS.gtk_label_set_text (labelHandle, buffer);
}

void showWidget (int index) {
	OS.gtk_widget_show (handle);
	OS.gtk_widget_show (clientHandle);
	OS.gtk_container_add (parent.handle, handle);
	OS.gtk_box_set_child_packing (parent.handle, handle, false, false, 0, OS.GTK_PACK_START);	
	if (boxHandle != 0) OS.gtk_widget_show (boxHandle);
	if (labelHandle != 0) OS.gtk_widget_show (labelHandle);
}

int /*long*/ windowProc (int /*long*/ handle, int /*long*/ user_data) {
	switch ((int)/*64*/user_data) {
		case ACTIVATE_INVERSE: {
			parent.relayout ();
			return 0;
		}
	}
	return super.windowProc (handle, user_data);
}
}