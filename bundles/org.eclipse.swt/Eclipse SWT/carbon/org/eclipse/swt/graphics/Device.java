package org.eclipse.swt.graphics;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;

public abstract class Device implements Drawable {

	/* Debugging */
	public static boolean DEBUG;
	boolean debug = DEBUG;
	boolean tracking = DEBUG;
	Error [] errors;
	Object [] objects;

	boolean disposed;
	
	/*
	* TEMPORARY CODE. When a graphics object is
	* created and the device parameter is null,
	* the current Display is used. This presents
	* a problem because SWT graphics does not
	* reference classes in SWT widgets. The correct
	* fix is to remove this feature. Unfortunately,
	* too many application programs rely on this
	* feature.
	*
	* This code will be removed in the future.
	*/
	protected static Device CurrentDevice;
	protected static Runnable DeviceFinder;
	static {
		try {
			Class.forName ("org.eclipse.swt.widgets.Display");
		} catch (Throwable e) {}
	}	

/* 
* TEMPORARY CODE 
*/
static Device getDevice () {
	if (DeviceFinder != null) DeviceFinder.run();
	Device device = CurrentDevice;
	CurrentDevice = null;
	return device;
}

public Device(DeviceData data) {
	if (data != null) {
		debug = data.debug;
		tracking = data.tracking;
	}
	create (data);
	init ();
	if (tracking) {
		errors = new Error [128];
		objects = new Object [128];
	}
}

protected void checkDevice () {
	if (disposed) SWT.error (SWT.ERROR_DEVICE_DISPOSED);
}

protected void create (DeviceData data) {
}

protected void destroy () {
}

public void dispose () {
	if (isDisposed()) return;
	checkDevice ();
	release ();
	destroy ();
	disposed = true;
	if (tracking) {
		objects = null;
		errors = null;
	}
}

void dispose_Object (Object object) {
	for (int i=0; i<objects.length; i++) {
		if (objects [i] == object) {
			objects [i] = null;
			errors [i] = null;
			return;
		}
	}
}

public Rectangle getBounds () {
	checkDevice ();
	return new Rectangle(0, 0, 0, 0);
}

public Rectangle getClientArea () {
	return getBounds ();
}

public int getDepth () {
	checkDevice ();
	return 0;
}

public DeviceData getDeviceData () {
	checkDevice ();
	DeviceData data = new DeviceData ();
	data.debug = debug;
	data.tracking = tracking;
	int count = 0, length = 0;
	if (tracking) length = objects.length;
	for (int i=0; i<length; i++) {
		if (objects [i] != null) count++;
	}
	int index = 0;
	data.objects = new Object [count];
	data.errors = new Error [count];
	for (int i=0; i<length; i++) {
		if (objects [i] != null) {
			data.objects [index] = objects [i];
			data.errors [index] = errors [i];
			index++;
		}
	}
	return data;
}

public Point getDPI () {
	checkDevice ();
	return new Point(0, 0);
}

public FontData [] getFontList (String faceName, boolean scalable) {	
	checkDevice ();
	return new FontData [0];
}

public Color getSystemColor (int id) {
	checkDevice ();
//	switch (id) {
//		case SWT.COLOR_BLACK: 				return COLOR_BLACK;
//		case SWT.COLOR_DARK_RED: 			return COLOR_DARK_RED;
//		case SWT.COLOR_DARK_GREEN:	 		return COLOR_DARK_GREEN;
//		case SWT.COLOR_DARK_YELLOW: 		return COLOR_DARK_YELLOW;
//		case SWT.COLOR_DARK_BLUE: 			return COLOR_DARK_BLUE;
//		case SWT.COLOR_DARK_MAGENTA: 		return COLOR_DARK_MAGENTA;
//		case SWT.COLOR_DARK_CYAN: 			return COLOR_DARK_CYAN;
//		case SWT.COLOR_GRAY: 				return COLOR_GRAY;
//		case SWT.COLOR_DARK_GRAY: 			return COLOR_DARK_GRAY;
//		case SWT.COLOR_RED: 				return COLOR_RED;
//		case SWT.COLOR_GREEN: 				return COLOR_GREEN;
//		case SWT.COLOR_YELLOW: 			return COLOR_YELLOW;
//		case SWT.COLOR_BLUE: 				return COLOR_BLUE;
//		case SWT.COLOR_MAGENTA: 			return COLOR_MAGENTA;
//		case SWT.COLOR_CYAN: 				return COLOR_CYAN;
//		case SWT.COLOR_WHITE: 				return COLOR_WHITE;
//	}
	return null;
}

public Font getSystemFont () {
	checkDevice ();
	return null;
}

public boolean getWarnings () {
	checkDevice ();
	return false;
}

protected void init () {
}

public abstract int internal_new_GC (GCData data);

public abstract void internal_dispose_GC (int handle, GCData data);

public boolean isDisposed () {
	return disposed;
}
	
void new_Object (Object object) {
	for (int i=0; i<objects.length; i++) {
		if (objects [i] == null) {
			objects [i] = object;
			errors [i] = new Error ();
			return;
		}
	}
	Object [] newObjects = new Object [objects.length + 128];
	System.arraycopy (objects, 0, newObjects, 0, objects.length);
	newObjects [objects.length] = object;
	objects = newObjects;
	Error [] newErrors = new Error [errors.length + 128];
	System.arraycopy (errors, 0, newErrors, 0, errors.length);
	newErrors [errors.length] = new Error ();
	errors = newErrors;
}

protected void release () {	
}

public void setWarnings (boolean warnings) {
	checkDevice ();
}
}
