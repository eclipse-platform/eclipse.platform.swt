package org.eclipse.swt.graphics;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.widgets.Display;

public final class Cursor {

	public int handle;
	Device device;
		
Cursor () {
}

public Cursor (Device device, int style) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	
	if (device.tracking) device.new_Object(this);
}

public Cursor (Device device, ImageData source, ImageData mask, int hotspotX, int hotspotY) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (mask == null) {
		if (source.getTransparencyType() != SWT.TRANSPARENCY_MASK) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		mask = source.getTransparencyMask();
	}
	
	if (device.tracking) device.new_Object(this);
}

public void dispose () {
	if (handle == 0) return;
	if (device.isDisposed()) return;


	handle = 0;
	if (device.tracking) device.dispose_Object(this);
	device = null;
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

public String toString () {
	if (isDisposed()) return "Cursor {*DISPOSED*}";
	return "Cursor {" + handle + "}";
}

}
