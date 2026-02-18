/*******************************************************************************
 * Copyright (c) 2025 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.*;

/**
 * Headless implementation of Device for SWT.
 */
public abstract class Device implements Drawable {
	
	protected static final int CHANGE_SCALEFACTOR = 1;
	
	public static boolean DEBUG;
	boolean debug = DEBUG;
	boolean tracking = DEBUG;
	Error[] errors;
	Object[] objects;
	Object trackingLock;
	
	volatile boolean disposed;
	
	int warningLevel;
	
	static Device[] Devices = new Device[4];
	
	static final Color COLOR_BLACK, COLOR_DARK_RED, COLOR_DARK_GREEN, COLOR_DARK_YELLOW, COLOR_DARK_BLUE;
	static final Color COLOR_DARK_MAGENTA, COLOR_DARK_CYAN, COLOR_GRAY, COLOR_DARK_GRAY, COLOR_RED, COLOR_TRANSPARENT;
	static final Color COLOR_GREEN, COLOR_YELLOW, COLOR_BLUE, COLOR_MAGENTA, COLOR_CYAN, COLOR_WHITE;
	
	static {
		COLOR_TRANSPARENT = new Color(0xFF, 0xFF, 0xFF, 0);
		COLOR_BLACK = new Color(0, 0, 0);
		COLOR_DARK_RED = new Color(0x80, 0, 0);
		COLOR_DARK_GREEN = new Color(0, 0x80, 0);
		COLOR_DARK_YELLOW = new Color(0x80, 0x80, 0);
		COLOR_DARK_BLUE = new Color(0, 0, 0x80);
		COLOR_DARK_MAGENTA = new Color(0x80, 0, 0x80);
		COLOR_DARK_CYAN = new Color(0, 0x80, 0x80);
		COLOR_GRAY = new Color(0xC0, 0xC0, 0xC0);
		COLOR_DARK_GRAY = new Color(0x80, 0x80, 0x80);
		COLOR_RED = new Color(0xFF, 0, 0);
		COLOR_GREEN = new Color(0, 0xFF, 0);
		COLOR_YELLOW = new Color(0xFF, 0xFF, 0);
		COLOR_BLUE = new Color(0, 0, 0xFF);
		COLOR_MAGENTA = new Color(0xFF, 0, 0xFF);
		COLOR_CYAN = new Color(0, 0xFF, 0xFF);
		COLOR_WHITE = new Color(0xFF, 0xFF, 0xFF);
	}
	
	Font systemFont;
	Point dpi;
	
	protected static Device CurrentDevice;
	protected static Runnable DeviceFinder;
	static {
		try {
			Class.forName("org.eclipse.swt.widgets.Display");
		} catch (ClassNotFoundException e) {}
	}
	
	static synchronized Device getDevice() {
		if (DeviceFinder != null) DeviceFinder.run();
		Device device = CurrentDevice;
		CurrentDevice = null;
		return device;
	}
	
	public Device() {
		this(null);
	}
	
	public Device(DeviceData data) {
		synchronized (Device.class) {
			if (data != null) {
				debug = data.debug;
				tracking = data.tracking;
			}
			if (tracking) {
				startTracking();
			}
			create(data);
			init();
			register(this);
		}
	}
	
	protected void checkDevice() {
		if (disposed) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
	}
	
	protected void create(DeviceData data) {
	}
	
	protected void destroy() {
	}
	
	public void dispose() {
		synchronized (Device.class) {
			if (isDisposed()) return;
			checkDevice();
			release();
			destroy();
			deregister(this);
			disposed = true;
			if (tracking) {
				stopTracking();
			}
		}
	}
	
	void dispose_Object(Object object) {
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] == object) {
				objects[i] = null;
				errors[i] = null;
				return;
			}
		}
	}
	
	static void deregister(Device device) {
		synchronized (Device.class) {
			for (int i = 0; i < Devices.length; i++) {
				if (device == Devices[i]) Devices[i] = null;
			}
		}
	}
	
	public Rectangle getBounds() {
		checkDevice();
		return new Rectangle(0, 0, 1024, 768);
	}
	
	public Rectangle getClientArea() {
		return getBounds();
	}
	
	public int getDepth() {
		return 24;
	}
	
	public Point getDPI() {
		checkDevice();
		if (dpi == null) {
			dpi = new Point(96, 96);
		}
		return new Point(dpi.x, dpi.y);
	}
	
	public FontData[] getFontList(String faceName, boolean scalable) {
		checkDevice();
		return new FontData[0];
	}
	
	public Color getSystemColor(int id) {
		checkDevice();
		switch (id) {
			case SWT.COLOR_BLACK:                return COLOR_BLACK;
			case SWT.COLOR_DARK_RED:             return COLOR_DARK_RED;
			case SWT.COLOR_DARK_GREEN:           return COLOR_DARK_GREEN;
			case SWT.COLOR_DARK_YELLOW:          return COLOR_DARK_YELLOW;
			case SWT.COLOR_DARK_BLUE:            return COLOR_DARK_BLUE;
			case SWT.COLOR_DARK_MAGENTA:         return COLOR_DARK_MAGENTA;
			case SWT.COLOR_DARK_CYAN:            return COLOR_DARK_CYAN;
			case SWT.COLOR_GRAY:                 return COLOR_GRAY;
			case SWT.COLOR_DARK_GRAY:            return COLOR_DARK_GRAY;
			case SWT.COLOR_RED:                  return COLOR_RED;
			case SWT.COLOR_GREEN:                return COLOR_GREEN;
			case SWT.COLOR_YELLOW:               return COLOR_YELLOW;
			case SWT.COLOR_BLUE:                 return COLOR_BLUE;
			case SWT.COLOR_MAGENTA:              return COLOR_MAGENTA;
			case SWT.COLOR_CYAN:                 return COLOR_CYAN;
			case SWT.COLOR_WHITE:                return COLOR_WHITE;
			case SWT.COLOR_TRANSPARENT:          return COLOR_TRANSPARENT;
		}
		return COLOR_BLACK;
	}
	
	public Font getSystemFont() {
		checkDevice();
		if (systemFont == null) {
			systemFont = new Font(this, new FontData("Sans", 10, SWT.NORMAL));
		}
		return systemFont;
	}
	
	public int getWarnings() {
		checkDevice();
		return warningLevel;
	}
	
	protected void init() {
	}
	
	@Override
	public abstract boolean isAutoScalable();
	
	public boolean isDisposed() {
		synchronized (Device.class) {
			return disposed;
		}
	}
	
	Object[] new_Object(int size) {
		return new Object[size];
	}
	
	protected void release() {
		if (tracking) {
			synchronized (trackingLock) {
				for (int i = 0; i < objects.length; i++) {
					if (objects[i] != null) {
						System.err.println("WARNING: Resource leaked: " + objects[i]);
					}
				}
				objects = null;
				errors = null;
			}
		}
	}
	
	static synchronized void register(Device device) {
		for (int i = 0; i < Devices.length; i++) {
			if (Devices[i] == null) {
				Devices[i] = device;
				return;
			}
		}
		Device[] newDevices = new Device[Devices.length + 4];
		System.arraycopy(Devices, 0, newDevices, 0, Devices.length);
		newDevices[Devices.length] = device;
		Devices = newDevices;
	}
	
	public void setWarnings(boolean warnings) {
		checkDevice();
		warningLevel = warnings ? 1 : 0;
	}
	
	void startTracking() {
		synchronized (Device.class) {
			if (trackingLock == null) {
				trackingLock = new Object();
				objects = new Object[128];
				errors = new Error[128];
			}
		}
	}
	
	void stopTracking() {
		synchronized (Device.class) {
			objects = null;
			errors = null;
			trackingLock = null;
		}
	}
	
}
