package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.DataBrowserListViewHeaderDesc;
import org.eclipse.swt.internal.carbon.OS;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

public class TableColumn extends Item {
	Table parent;
	int id;
	boolean resizable;
	static final int EXTRA_WIDTH = 20;

public TableColumn (Table parent, int style) {
	super (parent, checkStyle (style));
	resizable = true;
	this.parent = parent;
	parent.createItem (this, parent.getColumnCount ());
}

public TableColumn (Table parent, int style, int index) {
	super (parent, checkStyle (style));
	resizable = true;
	this.parent = parent;
	parent.createItem (this, index);
}

public void addControlListener(ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize,typedListener);
	addListener (SWT.Move,typedListener);
}

public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public int getAlignment () {
	checkWidget ();
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

public Display getDisplay () {
	Table parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

String getNameText () {
	return getText ();
}

public Table getParent () {
	checkWidget ();
	return parent;
}

public boolean getResizable () {
	checkWidget ();
	return resizable;
}

public int getWidth () {
	checkWidget ();
	short [] width = new short [1];
	OS.GetDataBrowserTableViewNamedColumnWidth (parent.handle, id, width);
	return Math.max (0, width [0] - EXTRA_WIDTH);
}

public void pack () {
	checkWidget ();
	GC gc = new GC (parent);
	int width = 0;
	int index = parent.indexOf (this);
	for (int i=0; i<parent.itemCount; i++) {
		TableItem item = parent.items [i];
		Image image = item.getImage (index);
		String text = item.getText (index);
		int itemWidth = 0;
		if (image != null) itemWidth = image.getBounds ().width + 2;
		if (text != null && text.length () > 0) itemWidth += gc.stringExtent (text).x;
		width = Math.max (width, itemWidth);
	}
	gc.dispose ();
	width += EXTRA_WIDTH;
	OS.SetDataBrowserTableViewNamedColumnWidth (parent.handle, id, (short) width);
}

void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}

void releaseWidget () {
	super.releaseWidget ();
	parent = null;
}

public void removeControlListener (ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
}

public void removeSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

public void setAlignment (int alignment) {
	checkWidget ();
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	int index = parent.indexOf (this);
	if (index == -1 || index == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
}

public void setImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int index = parent.indexOf (this);
	if (index == -1) return;
	super.setImage (image);
}

public void setResizable (boolean resizable) {
	checkWidget ();
	this.resizable = resizable;
	updateHeader ();
}

public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText (string);
	updateHeader ();
}

public void setWidth (int width) {
	checkWidget ();
	OS.SetDataBrowserTableViewNamedColumnWidth (parent.handle, id, (short) (width + EXTRA_WIDTH));
	updateHeader ();
}

void updateHeader () {
	char [] buffer = new char [text.length ()];
	text.getChars (0, buffer.length, buffer, 0);
	int i=0, j=0;
	while (i < buffer.length) {
		if ((buffer [j++] = buffer [i++]) == Mnemonic) {
			if (i == buffer.length) {continue;}
			if (buffer [i] == Mnemonic) {i++; continue;}
			j--;
		}
	}
	int str = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, j);
	if (str == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	DataBrowserListViewHeaderDesc desc = new DataBrowserListViewHeaderDesc ();
	desc.version = OS.kDataBrowserListViewLatestHeaderDesc;
	if (resizable) {
		desc.minimumWidth = 0;
		desc.maximumWidth = 0x7FFF;
	} else {
		short [] width = new short [1];
		OS.GetDataBrowserTableViewNamedColumnWidth (parent.handle, id, width);
		desc.minimumWidth = desc.maximumWidth = width [0];
	}
	desc.titleString = str;
	OS.SetDataBrowserListViewHeaderDesc (parent.handle, id, desc);
	OS.CFRelease (str);
}

}
