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
package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.*;

public class TextStyle {
	Device device;
	Font font;
	Color foreground;
	Color background;
	
	int style;

public TextStyle (Device device, Font font, Color foreground, Color background) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (font != null && font.isDisposed()) SWT.error (SWT.ERROR_GRAPHIC_DISPOSED);
	if (foreground != null && foreground.isDisposed()) SWT.error (SWT.ERROR_GRAPHIC_DISPOSED);
	if (background != null && background.isDisposed()) SWT.error (SWT.ERROR_GRAPHIC_DISPOSED);
	this.device = device;
	this.font = font;
	this.foreground = foreground;
	this.background = background;
}

void createStyle() {
	if (style != 0) return;
	int[] buffer = new int[1];
	OS.ATSUCreateStyle(buffer);
	style = buffer[0];
	if (style == 0) SWT.error(SWT.ERROR_NO_HANDLES);	
	int length = 0, ptrLength = 0, index = 0;
	if (font != null) {
		length += 2;
		ptrLength += 8;
	}
	if (foreground != null) {
		length += 1;
		ptrLength += RGBColor.sizeof;
	}
	int[] tags = new int[length];
	int[] sizes = new int[length];
	int[] values = new int[length];
	int ptr = OS.NewPtr(ptrLength), ptr1 = ptr;
	if (font != null) {
		buffer[0] = font.handle;
		tags[index] = OS.kATSUFontTag;
		sizes[index] = 4;
		values[index] = ptr1;
		OS.memcpy(values[index], buffer, sizes[index]);
		ptr1 += sizes[index];
		index++;

		buffer[0] = OS.X2Fix(font.size);
		tags[index] = OS.kATSUSizeTag;
		sizes[index] = 4;
		values[index] = ptr1;
		OS.memcpy(values[index], buffer, sizes[index]);
		ptr1 += sizes[index];
		index++;
	}
	if (foreground != null) {
		RGBColor rgb = new RGBColor ();
		float[] color = foreground.handle;
		rgb.red = (short) (color [0] * 0xffff);
		rgb.green = (short) (color [1] * 0xffff);
		rgb.blue = (short) (color [2] * 0xffff);		
		tags[index] = OS.kATSUColorTag;
		sizes[index] = RGBColor.sizeof;
		values[index] = ptr1;
		OS.memcpy(values[index], rgb, sizes[index]);
		ptr1 += sizes[index];
		index++;
	}
	OS.ATSUSetAttributes(style, tags.length, tags, sizes, values);
	OS.DisposePtr(ptr);	
}

public void dispose() {
	if (style == 0) return;
	OS.ATSUDisposeStyle(style);
	style = 0;
	font = null;
	foreground = null;
	background = null;
	device = null;
}

public boolean isDisposed() {
	return device == null;
}
}