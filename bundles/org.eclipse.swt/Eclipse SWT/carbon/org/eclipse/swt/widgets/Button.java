package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

public  class Button extends Control {
	String text;

public Button (Composite parent, int style) {
	super (parent, checkStyle (style));
}

public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	style = checkBits (style, SWT.PUSH, SWT.ARROW, SWT.CHECK, SWT.RADIO, SWT.TOGGLE, 0);
	if ((style & SWT.PUSH) != 0) {
		return checkBits (style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0, 0, 0);
	}
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) != 0) {
		return checkBits (style, SWT.LEFT, SWT.RIGHT, SWT.CENTER, 0, 0, 0);
	}
	if ((style & SWT.ARROW) != 0) {
		return checkBits (style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT, 0, 0);
	}
	return style;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	Rect rect = new Rect();
	short [] base= new short [1];
	OS.GetBestControlRect (handle, rect, base);
	return new Point (rect.right - rect.left, rect.bottom - rect.top);
}

void createHandle () {
	int [] outControl = new int [1];
//	int window = OS.GetControlOwner (parent.handle);
	int window = getShell ().shellHandle;
	OS.CreatePushButtonControl (window, null, 0, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
	OS.HIViewAddSubview (parent.handle, handle);
	OS.HIViewSetZOrder (handle, OS.kHIViewZOrderBelow, 0);
}

public int getAlignment () {
	checkWidget();
    return 0;
}

public Image getImage () {
	checkWidget();
	return null;
}
String getNameText () {
	return getText ();
}

public boolean getSelection () {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
    return false;
}

public String getText () {
	checkWidget();
	return "text";
}

public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);
}

public void setAlignment (int alignment) {
	checkWidget();
}

public void setImage (Image image) {
	checkWidget();
}

public void setSelection (boolean selected) {
	checkWidget();
}

public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.ARROW) != 0) return;
	text = string;
	char [] buffer = new char [text.length ()];
	int i=0, j=0;
	while (i < buffer.length) {
		if ((buffer [j++] = buffer [i++]) == Mnemonic) {
			if (i == buffer.length) {continue;}
			if (buffer [i] == Mnemonic) {i++; continue;}
			j--;
		}
	}
	text.getChars (0, buffer.length, buffer, 0);
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, j);
	if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	OS.SetControlTitleWithCFString (handle, ptr);
	OS.CFRelease (ptr);
}

}
