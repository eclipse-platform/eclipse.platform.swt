package org.eclipse.swt.graphics;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import java.io.*;

public final class Image implements Drawable {
		
	public int type;
	public int handle;
	public int mask;
	Device device;
	int transparentPixel = -1;
	GC memGC;
	byte[] alphaData;
	int alpha = -1;
	ImageData data;
	
	static final int DEFAULT_SCANLINE_PAD = 4;

Image() {
}

public Image(Device device, int width, int height) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, width, height);
	if (device.tracking) device.new_Object(this);
}

public Image(Device device, Image srcImage, int flag) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	if (srcImage == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (srcImage.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	
	if (device.tracking) device.new_Object(this);
}

public Image(Device device, Rectangle bounds) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, bounds.width, bounds.height);
	if (device.tracking) device.new_Object(this);
}

public Image(Device device, ImageData data) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, data);
	if (device.tracking) device.new_Object(this);
}

public Image(Device device, ImageData source, ImageData mask) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (mask == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (source.width != mask.width || source.height != mask.height) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	if (device.tracking) device.new_Object(this);
}

public Image(Device device, InputStream stream) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, new ImageData(stream));
	if (device.tracking) device.new_Object(this);
}

public Image(Device device, String filename) {
		if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, new ImageData(filename));
	if (device.tracking) device.new_Object(this);
}

public void dispose () {
	if (handle == 0) return;
	if (device.isDisposed()) return;

	handle = 0;
	memGC = null;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Image)) return false;
	Image image = (Image)object;
	return device == image.device && handle == image.handle &&
			transparentPixel == image.transparentPixel &&
					mask == image.mask;
}

public Color getBackground() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (transparentPixel == -1) return null;
    return null;
}

public Rectangle getBounds () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);

	return new Rectangle(0, 0, 0, 0);
}

public ImageData getImageData() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return null;
}

public int hashCode () {
	return handle;
}

void init(Device device, int width, int height) {
	if (width <=0 || height <=0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	this.device = device;
}

void init(Device device, ImageData image) {
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
}

public int internal_new_GC (GCData data) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return 0;
}

public void internal_dispose_GC (int hDC, GCData data) {
}

public boolean isDisposed() {
	return handle == 0;
}

public void setBackground(Color color) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
}

public String toString () {
	if (isDisposed()) return "Image {*DISPOSED*}";
	return "Image {" + handle + "}";
}
}
