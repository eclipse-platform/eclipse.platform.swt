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
import org.eclipse.swt.graphics.*;

public class Label extends Control {
	String text = "";
	Image image;
	boolean isImage;

public Label (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	if ((style & SWT.SEPARATOR) != 0) return style;
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = 0, height = 0;
	if ((style & SWT.SEPARATOR) != 0) {
		if ((style & SWT.HORIZONTAL) != 0) {
			width = DEFAULT_WIDTH;
			height = 3;
		} else {
			width = 3;
			height = DEFAULT_HEIGHT;
		}
	} else {
//		Rect saveRect = new Rect ();
//		OS.GetControlBounds (handle, saveRect);
		Rect rect = new Rect ();
		//FIX ME - always returns the current width
//		OS.SetRect (rect, (short)0, (short)0, (short) 0x1FFF, (short) 0x1FFF);
//		OS.SetControlBounds (handle, rect);
		short [] base = new short [1];
		OS.GetBestControlRect (handle, rect, base);
		width = 100;//rect.right - rect.left;
		height = 30;//rect.bottom - rect.top;
//		OS.SetControlBounds (handle, saveRect);
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width, height);
}

void createHandle () {
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
	if ((style & SWT.SEPARATOR) != 0) {
		OS.CreateSeparatorControl (window, null, outControl);
	} else {
		OS.CreateStaticTextControl (window, null, 0, null, outControl);
	}
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
}

public int getAlignment () {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

public int getBorderWidth () {
	checkWidget();
	return (style & SWT.BORDER) != 0 ? 1 : 0;
}

public Image getImage () {
	checkWidget();
	return image;
}

String getNameText () {
	return getText ();
}

public String getText () {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return "";
	return text;
}

int kEventControlDraw (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlDraw (nextHandler, theEvent, userData);
//	if (result == OS.noErr) return result;
	if (isImage) {
		if (image != null) {
			GCData data = new GCData ();
			data.paintEvent = theEvent;
			GC gc = GC.carbon_new (this, data);
			gc.drawImage (image, 0, 0);
			gc.dispose ();
		}
		return OS.noErr;
	}
	return result;
}

public void setAlignment (int alignment) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
}

public void setImage (Image image) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	this.image = image;
	isImage = true;
}

public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	isImage = false;
	text = string;
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
	int ptr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, j);
	if (ptr == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
	OS.SetControlData (handle, 0 , OS.kControlStaticTextCFStringTag, 4, new int[]{ptr});
	OS.CFRelease (ptr);
	OS.HIViewSetNeedsDisplay (handle, true);
}

}
