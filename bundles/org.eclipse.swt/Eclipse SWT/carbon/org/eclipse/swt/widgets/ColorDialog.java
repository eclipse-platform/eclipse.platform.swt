package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.graphics.RGB;

public class ColorDialog extends Dialog {
	RGB rgb;
	
public ColorDialog(Shell parent) {
	this(parent, SWT.APPLICATION_MODAL);
}

public ColorDialog(Shell parent, int style) {
	super(parent, style);
	checkSubclass ();
}

public RGB getRGB() {
	return rgb;
}

public RGB open() {	
	ColorPickerInfo info = new ColorPickerInfo ();
	if (rgb != null) {
		info.red = (short)(rgb.red * 257);
		info.green = (short)(rgb.green * 257);
		info.blue = (short)(rgb.blue * 257);
	} else {
		info.red = (short)(255 * 257);
		info.green = (short)(255 * 257);
		info.blue = (short)(255 * 257);		
	}
	info.flags = OS.kColorPickerDialogIsMoveable | OS.kColorPickerDialogIsModal;
	// NEEDS WORK - shouldn't be at mouse location
	info.placeWhere = (short)OS.kAtSpecifiedOrigin;
	org.eclipse.swt.internal.carbon.Point mp = new org.eclipse.swt.internal.carbon.Point ();
	OS.GetGlobalMouse (mp);
	info.v = mp.v;
	info.h = mp.h;
	if (title != null) {
		// NEEDS WORK - no title displayed		
		info.prompt = new byte[256];
		int length = title.length();
		if (length > 255) length = 255;
		info.prompt [0] = (byte)length;
		for (int i=0; i<length; i++) {
			info.prompt [i+1] = (byte)title.charAt (i);
		}
	}
	rgb = null;
	if (OS.PickColor (info) == OS.noErr && info.newColorChosen) {
		int red = (info.red >> 8) & 0xFF;
		int green = (info.green >> 8) & 0xFF;
		int blue =	(info.blue >> 8) & 0xFF;
		rgb = new RGB(red, green, blue);
	}
	return rgb;
}

public void setRGB(RGB rgb) {
	this.rgb = rgb;
}
}
