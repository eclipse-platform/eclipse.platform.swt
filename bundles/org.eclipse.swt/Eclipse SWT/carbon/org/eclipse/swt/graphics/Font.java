package org.eclipse.swt.graphics;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;

public final class Font {

	public int handle;
	
	Device device;

Font() {
}

public Font(Device device, FontData fd) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, fd);
	if (device.tracking) device.new_Object(this);	
}

public Font(Device device, String name, int height, int style) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (name == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, new FontData (name, height, style));
	if (device.tracking) device.new_Object(this);	
}

public void dispose() {
	if (handle == 0) return;
	if (device.isDisposed()) return;
	
	handle = 0;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public boolean equals(Object object) {
	if (object == this) return true;
	if (!(object instanceof Font)) return false;
	Font font = (Font) object;
	return device == font.device && handle == font.handle;
}

public FontData[] getFontData() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return new FontData[0];
}

public int hashCode () {
	return handle;
}

void init (Device device, FontData fd) {
	if (fd == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;

	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
}

public boolean isDisposed() {
	return handle == 0;
}

public String toString () {
	if (isDisposed()) return "Font {*DISPOSED*}";
	return "Font {" + handle + "}";
}

}
