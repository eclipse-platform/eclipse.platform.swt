package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.OS;

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
		if (isImage && image != null) {
			Rectangle r = image.getBounds();
			width = r.width;
			height = r.height;
		} else {
			int [] ptr = new int [1];
			int [] actualSize = new int [1];
			OS.GetControlData (handle, (short)0 , OS.kControlStaticTextCFStringTag, 4, ptr, actualSize);
			if (ptr [0] != 0) {
				org.eclipse.swt.internal.carbon.Point bounds = new org.eclipse.swt.internal.carbon.Point ();
				short [] baseLine = new short [1];
				boolean wrap = false;
				if ((style & SWT.WRAP) != 0 && wHint != SWT.DEFAULT) {
					wrap = true;
					bounds.h = (short) wHint;
				}
				// NEEDS work - only works for default font
				OS.GetThemeTextDimensions (ptr [0], (short)OS.kThemeSystemFont, OS.kThemeStateActive, wrap, bounds, baseLine);
				width = bounds.h;
				height = bounds.v;
				OS.CFRelease (ptr [0]);
			} else {
				width = DEFAULT_WIDTH;
				height = DEFAULT_HEIGHT;
			}
		}
	}
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width, height);
}

void createHandle () {
	state |= GRAB;
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

void drawWidget (int control, int damageRgn, int visibleRgn, int theEvent) {
	if (isImage && image != null) {
		GCData data = new GCData ();
		data.paintEvent = theEvent;
		GC gc = GC.carbon_new (this, data);
		gc.drawImage (image, 0, 0);
		gc.dispose ();
	}
	super.drawWidget (control, damageRgn, visibleRgn, theEvent);
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

public void setAlignment (int alignment) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
}

public void setImage (Image image) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	this.image = image;
	isImage = true;
	redraw ();
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
	redraw ();
}

}
