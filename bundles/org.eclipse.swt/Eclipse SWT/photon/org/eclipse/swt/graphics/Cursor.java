package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;

public final class Cursor {

	/**
	 * the type to the OS cursor resource
	 * (Warning: This field is platform dependent)
	 */
	 public int type;

	/**
	 * the handle to the OS cursor resource
	 * (Warning: This field is platform dependent)
	 */
	public int bitmap;
	
	/**
	 * the device where this cursor was created
	 */
	Device device;

Cursor() {
}

public Cursor(Device device, int style) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	switch (style) {
		case SWT.CURSOR_ARROW: 		type = OS.Ph_CURSOR_POINTER; break;
		case SWT.CURSOR_WAIT: 		type = OS.Ph_CURSOR_CLOCK; break;
		case SWT.CURSOR_HAND: 		type = OS.Ph_CURSOR_FINGER; break;
		case SWT.CURSOR_CROSS: 		type = OS.Ph_CURSOR_CROSSHAIR; break;
		case SWT.CURSOR_APPSTARTING:	type = OS.Ph_CURSOR_POINT_WAIT; break;
		case SWT.CURSOR_HELP: 		type = OS.Ph_CURSOR_QUESTION_POINT; break;
		case SWT.CURSOR_SIZEALL: 	type = OS.Ph_CURSOR_MOVE; break;
		case SWT.CURSOR_SIZENESW: 	type = OS.Ph_CURSOR_MOVE; break;
		case SWT.CURSOR_SIZENS: 	type = OS.Ph_CURSOR_DRAG_VERTICAL; break;
		case SWT.CURSOR_SIZENWSE:	type = OS.Ph_CURSOR_MOVE; break;
		case SWT.CURSOR_SIZEWE: 	type = OS.Ph_CURSOR_DRAG_HORIZONTAL; break;
		case SWT.CURSOR_SIZEN: 		type = OS.Ph_CURSOR_DRAG_TOP; break;
		case SWT.CURSOR_SIZES: 		type = OS.Ph_CURSOR_DRAG_BOTTOM; break;
		case SWT.CURSOR_SIZEE: 		type = OS.Ph_CURSOR_DRAG_RIGHT; break;
		case SWT.CURSOR_SIZEW: 		type = OS.Ph_CURSOR_DRAG_LEFT; break;
		case SWT.CURSOR_SIZENE: 	type = OS.Ph_CURSOR_DRAG_TR; break;
		case SWT.CURSOR_SIZESE: 	type = OS.Ph_CURSOR_DRAG_BR; break;
		case SWT.CURSOR_SIZESW:		type = OS.Ph_CURSOR_DRAG_BL; break;
		case SWT.CURSOR_SIZENW: 	type = OS.Ph_CURSOR_DRAG_TL; break;
		case SWT.CURSOR_UPARROW: 	type = OS.Ph_CURSOR_FINGER; break;
		case SWT.CURSOR_IBEAM: 		type = OS.Ph_CURSOR_INSERT; break;
		case SWT.CURSOR_NO: 		type = OS.Ph_CURSOR_DONT; break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (type == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
}

public Cursor(Device device, ImageData source, ImageData mask, int hotspotX, int hotspotY) {
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
	/* Check color depths */
	if (mask.depth != 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (source.depth != 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	/* Check the hotspots */
	if (hotspotX >= source.width || hotspotX < 0 ||
		hotspotY >= source.height || hotspotY < 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	type = OS.Ph_CURSOR_BITMAP;
	
	short w = (short)source.width;
	short h = (short)source.height;
	ImageData mask1 = new ImageData(w, h, 1, source.palette);
	ImageData mask2 = new ImageData(w, h, 1, mask.palette);
	for (int y=0; y<h; y++) {
		for (int x=0; x<w; x++) {
			int mask1_pixel, src_pixel = source.getPixel(x, y);
			int mask2_pixel, mask_pixel = mask.getPixel(x, y);
			if (src_pixel == 0 && mask_pixel == 0) {
				// BLACK
				mask1_pixel = 0;
				mask2_pixel = 1;
			} else if (src_pixel == 0 && mask_pixel == 1) {
				// WHITE - cursor color
				mask1_pixel = 1;
				mask2_pixel = 0;
			} else if (src_pixel == 1 && mask_pixel == 0) {
				// SCREEN
				mask1_pixel = 0;
				mask2_pixel = 0;
			} else {
				/*
				* Feature in Photon. It is not possible to have
				* the reverse screen case using the Photon support.
				* Reverse screen will be the same as screen.
				*/
				// REVERSE SCREEN -> SCREEN
				mask1_pixel = 0;
				mask2_pixel = 0;
			}
			mask1.setPixel(x, y, mask1_pixel);
			mask2.setPixel(x, y, mask2_pixel);
		}
	}
	
	PhCursorDef_t cursor = new PhCursorDef_t();
	cursor.size1_x = w;
	cursor.size1_y = h;
	cursor.offset1_x = (short)-hotspotX;
	cursor.offset1_y = (short)-hotspotY;
	cursor.bytesperline1 = (byte)mask1.bytesPerLine;
	cursor.color1 = OS.Ph_CURSOR_DEFAULT_COLOR;
	cursor.size2_x = w;
	cursor.size2_y = h;
	cursor.offset2_x = (short)-hotspotX;
	cursor.offset2_y = (short)-hotspotY;
	cursor.bytesperline2 = (byte)mask2.bytesPerLine;
	cursor.color2 = 0x000000;
	int mask1Size = cursor.bytesperline1 * cursor.size1_y;
	int mask2Size = cursor.bytesperline2 * cursor.size2_y;	
	bitmap = OS.malloc(PhCursorDef_t.sizeof + mask1Size + mask2Size);
	if (bitmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.memmove(bitmap, cursor, PhCursorDef_t.sizeof);
	OS.memmove(bitmap + PhCursorDef_t.sizeof, mask1.data, mask1Size);
	OS.memmove(bitmap + PhCursorDef_t.sizeof + mask1Size, mask2.data, mask2Size);
	if (device.tracking) device.new_Object(this);
}

public void dispose () {
	if (type == 0) return;
	if (type == OS.Ph_CURSOR_BITMAP && bitmap != 0) {
		OS.free(bitmap);
	}
	type = bitmap = 0;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Cursor)) return false;
	Cursor cursor = (Cursor) object;
	return device == cursor.device && type == cursor.type &&
		bitmap == cursor.bitmap;
}

public int hashCode () {
	return bitmap ^ type;
}

public boolean isDisposed() {
	return type == 0;
}

public static Cursor photon_new(Device device, int type, int bitmap) {
	if (device == null) device = Device.getDevice();
	Cursor cursor = new Cursor();
	cursor.type = type;
	cursor.bitmap = bitmap;
	cursor.device = device;
	return cursor;
}

public String toString () {
	if (isDisposed()) return "Cursor {*DISPOSED*}";
	return "Cursor {" + type + "," + bitmap + "}";
}

}
