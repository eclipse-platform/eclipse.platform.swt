package org.eclipse.swt.graphics;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
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
	public int handle;
	
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
	handle = type;
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
}

public Cursor(Device device, ImageData source, ImageData mask, int hotspotX, int hotspotY) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	type = OS.Ph_CURSOR_BITMAP;
	SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
}

public void dispose () {
	if (handle == 0) return;
	if (type == OS.Ph_CURSOR_BITMAP && handle != 0) {
		/* BITMAP cursors are not implemented yet */
	}
	type = handle = 0;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Cursor)) return false;
	Cursor cursor = (Cursor) object;
	return device == cursor.device && handle == cursor.handle;
}

public int hashCode () {
	return handle ^ type;
}

public boolean isDisposed() {
	return type == 0;
}

public static Cursor photon_new(Device device, int type, int handle) {
	if (device == null) device = Device.getDevice();
	Cursor cursor = new Cursor();
	cursor.type = type;
	cursor.handle = handle;
	cursor.device = device;
	return cursor;
}

}