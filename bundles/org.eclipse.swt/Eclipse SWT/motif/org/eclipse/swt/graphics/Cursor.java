package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
public final class Cursor {
	/**
	 * The handle to the OS cursor resource.
	 * Warning: This field is platform dependent.
	 */
	public int handle;

	/**
	 * The device where this image was created.
	 */
	Device device;
	
Cursor () {
}
public Cursor (Device device, int style) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	int shape = 0;
	switch (style) {
		case SWT.CURSOR_ARROW: shape = OS.XC_left_ptr; break;
		case SWT.CURSOR_WAIT: shape = OS.XC_watch; break;
		case SWT.CURSOR_HAND: shape = OS.XC_hand2; break;
		case SWT.CURSOR_CROSS: shape = OS.XC_cross; break;
		case SWT.CURSOR_APPSTARTING: shape = OS.XC_watch; break;
		case SWT.CURSOR_HELP: shape = OS.XC_question_arrow; break;
		case SWT.CURSOR_SIZEALL: shape = OS.XC_diamond_cross; break;
		case SWT.CURSOR_SIZENESW: shape = OS.XC_sizing; break;
		case SWT.CURSOR_SIZENS: shape = OS.XC_double_arrow; break;
		case SWT.CURSOR_SIZENWSE: shape = OS.XC_sizing; break;
		case SWT.CURSOR_SIZEWE: shape = OS.XC_sb_h_double_arrow; break;
		case SWT.CURSOR_SIZEN: shape = OS.XC_top_side; break;
		case SWT.CURSOR_SIZES: shape = OS.XC_bottom_side; break;
		case SWT.CURSOR_SIZEE: shape = OS.XC_right_side; break;
		case SWT.CURSOR_SIZEW: shape = OS.XC_left_side; break;
		case SWT.CURSOR_SIZENE: shape = OS.XC_top_right_corner; break;
		case SWT.CURSOR_SIZESE: shape = OS.XC_bottom_right_corner; break;
		case SWT.CURSOR_SIZESW: shape = OS.XC_bottom_left_corner; break;
		case SWT.CURSOR_SIZENW: shape = OS.XC_top_left_corner; break;
		case SWT.CURSOR_UPARROW: shape = OS.XC_sb_up_arrow; break;
		case SWT.CURSOR_IBEAM: shape = OS.XC_xterm; break;
		case SWT.CURSOR_NO: shape = OS.XC_X_cursor; break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	this.handle = OS.XCreateFontCursor(device.xDisplay, shape);
}
public Cursor (Device device, ImageData source, ImageData mask, int hotspotX, int hotspotY) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (mask == null) {
		if (source.getTransparencyType() != SWT.TRANSPARENCY_MASK) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		mask = source.getTransparencyMask();
	}
	/* Check the bounds. Mask must be the same size as source */
	if (mask.width != source.width || mask.height != source.height) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	/* Check depths */
	if (mask.depth != 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (source.depth != 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	/* Check the hotspots */
	if (hotspotX >= source.width || hotspotX < 0 ||
		hotspotY >= source.height || hotspotY < 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	/* Swap the bits if necessary */
	byte[] sourceData = new byte[source.data.length];
	byte[] maskData = new byte[mask.data.length];
	/* Swap the bits in each byte */
	byte[] data = source.data;
	for (int i = 0; i < data.length; i++) {
		byte s = data[i];
		sourceData[i] = (byte)(((s & 0x80) >> 7) |
			((s & 0x40) >> 5) |
			((s & 0x20) >> 3) |
			((s & 0x10) >> 1) |
			((s & 0x08) << 1) |
			((s & 0x04) << 3) |
			((s & 0x02) << 5) |
			((s & 0x01) << 7));
		sourceData[i] = (byte) ~sourceData[i];
	}
	data = mask.data;
	for (int i = 0; i < data.length; i++) {
		byte s = data[i];
		maskData[i] = (byte)(((s & 0x80) >> 7) |
			((s & 0x40) >> 5) |
			((s & 0x20) >> 3) |
			((s & 0x10) >> 1) |
			((s & 0x08) << 1) |
			((s & 0x04) << 3) |
			((s & 0x02) << 5) |
			((s & 0x01) << 7));
		maskData[i] = (byte) ~maskData[i];
	}
	int xDisplay = device.xDisplay;
	int drawable = OS.XDefaultRootWindow(xDisplay);
	int sourcePixmap = OS.XCreateBitmapFromData(xDisplay, drawable, sourceData, source.width, source.height);
	int maskPixmap = OS.XCreateBitmapFromData(xDisplay, drawable, maskData, source.width, source.height);
	/* Get the colors */
	int screenNum = OS.XDefaultScreen(xDisplay);
	XColor foreground = new XColor();
	foreground.pixel = OS.XBlackPixel(xDisplay, screenNum);
	foreground.red = foreground.green = foreground.blue = 0;
	XColor background = new XColor();
	background.pixel = OS.XWhitePixel(xDisplay, screenNum);
	background.red = background.green = background.blue = (short)0xFFFF;
	/* Create the cursor */
	handle = OS.XCreatePixmapCursor(xDisplay, maskPixmap, sourcePixmap, foreground, background, hotspotX, hotspotY);
	/* Dispose the pixmaps */
	OS.XFreePixmap(xDisplay, sourcePixmap);
	OS.XFreePixmap(xDisplay, maskPixmap);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
}
public void dispose () {
	if (handle != 0) OS.XFreeCursor(device.xDisplay, handle);
	device = null;
	handle = 0;
}
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Cursor)) return false;
	Cursor cursor = (Cursor)object;
	return device == cursor.device && handle == cursor.handle;
}
public int hashCode () {
	return handle;
}
public boolean isDisposed() {
	return handle == 0;
}
public static Cursor motif_new(Device device, int handle) {
	if (device == null) device = Device.getDevice();
	Cursor cursor = new Cursor();
	cursor.device = device;
	cursor.handle = handle;
	return cursor;
}
}
