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

import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.*;

public class TextStyle {

	Device device;
	Font font;
	Color foreground;
	Color background;

public TextStyle (Device device, Font font, Color foreground, Color background) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (font != null && font.isDisposed()) SWT.error (SWT.ERROR_GRAPHIC_DISPOSED);
	if (foreground != null && foreground.isDisposed()) SWT.error (SWT.ERROR_GRAPHIC_DISPOSED);
	if (background != null && background.isDisposed()) SWT.error (SWT.ERROR_GRAPHIC_DISPOSED);
	this.font = font;
	this.foreground = foreground;
	this.background = background;
	if (device.tracking) device.new_Object(this);
}

int /*long*/[] createAttributes () {
	int /*long*/[] attributes = new int /*long*/[6];
	int count = 0;
	if (font != null && !font.isDisposed()) {
		attributes[count++] = OS.pango_attr_font_desc_new (font.handle);
	}
	if (foreground != null && !foreground.isDisposed()) {
		GdkColor fg = foreground.handle;
		attributes[count++] = OS.pango_attr_foreground_new(fg.red, fg.green, fg.blue);
	}
	if (background != null && !background.isDisposed()) {
		GdkColor bg = background.handle;
		attributes[count++] = OS.pango_attr_background_new(bg.red, bg.green, bg.blue);
	}
//	if ((style & UNDERLINE) != 0) {
//		attributes[count++] = OS.pango_attr_underline_new(OS.PANGO_UNDERLINE_SINGLE);
//	}
//	if ((style & STRIKETHROUGH) != 0) {
//		attributes[count++] = OS.pango_attr_strikethrough_new(true);
//	}
//	if ((style & BOLD) != 0) {
//		attributes[count++] = OS.pango_attr_weight_new(OS.PANGO_WEIGHT_BOLD);
//	}
	return attributes;
}

public void dispose() {
	if (device == null) return;
	font = null;
	foreground = null;
	background = null;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public boolean isDisposed() {
	return device == null;
}
}
