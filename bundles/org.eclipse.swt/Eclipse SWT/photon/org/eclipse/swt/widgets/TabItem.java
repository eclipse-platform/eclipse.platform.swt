package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public /*final*/ class TabItem extends Item {
	TabFolder parent;
	Control control;
	String toolTipText;

public TabItem (TabFolder parent, int style) {
	this(parent, style, parent.getItemCount ());
}

public TabItem (TabFolder parent, int style, int index) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, index);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Control getControl () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return control;
}

public Display getDisplay () {
	TabFolder parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

public TabFolder getParent () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent;
}

public String getToolTipText () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return toolTipText;
}

public boolean isDisposed () {
	//NOT DONE - should have a handle or state flag
	return parent == null;
}

void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}

void releaseWidget () {
	super.releaseWidget ();
	control = null;
	parent = null;
}

public void setControl (Control control) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (control != null && control.parent != parent) {
		error (SWT.ERROR_INVALID_PARENT);
	}
	Control oldControl = this.control, newControl = control;
	this.control = control;
	int index = parent.indexOf (this);
	if (index != parent.getSelectionIndex ()) return;
	if (newControl != null) {
		newControl.setBounds (parent.getClientArea ());
		newControl.setVisible (true);
	}
	if (oldControl != null) oldControl.setVisible (false);
}

public void setImage (Image image) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	//NOT SUPPORTED
}

public void setText (String text) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	super.setText (text);
	int index = parent.indexOf (this);
	int [] args = {OS.Pt_ARG_PG_PANEL_TITLES, 0, 0};
	OS.PtGetResources (parent.handle, args.length / 3, args);
	int count = args [2];
	int oldPtr = args [1];
	int newPtr = OS.malloc (count * 4);
	for (int i=0; i<count; i++) {
		int str;
		if (i == index) {
			byte [] buffer = Converter.wcsToMbcs (null, text);
			str = OS.malloc (buffer.length + 1);
			OS.memmove (str, buffer, buffer.length);
		} else {
			int [] address = new int [1];
			OS.memmove (address, oldPtr + (i * 4), 4);
			int length = OS.strlen (address [0]);
			str = OS.malloc (length + 1);
			OS.memmove (str, address [0], length + 1);
		}
		OS.memmove (newPtr + (i * 4), new int [] {str}, 4);
	}
	args = new int [] {OS.Pt_ARG_PG_PANEL_TITLES, newPtr, count};
	OS.PtSetResources (parent.handle, args.length / 3, args);
	for (int i=0; i<count; i++) {
		int [] address = new int [1];
		OS.memmove (address, newPtr + (i * 4), 4);
		OS.free (address [0]);
	}
	OS.free (newPtr);
}

public void setToolTipText (String string) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	toolTipText = string;
}
}