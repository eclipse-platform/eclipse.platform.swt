package org.eclipse.swt.graphics;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.carbon.*;

/**
 * This class is the abstract superclass of all device objects,
 * such as the Display device and the Printer device. Devices
 * can have a graphics context (GC) created for them, and they
 * can be drawn on by sending messages to the associated GC.
 */
public abstract class Device implements Drawable {
	
	/**
	 * the handle to the GDevice
	 * (Warning: This field is platform dependent)
	 */
	public int fGDeviceHandle;	

	/* Debugging */
	public static boolean DEBUG;
	boolean debug = DEBUG;
	public boolean tracking = DEBUG;
	Error [] errors;
	Object [] objects;
		
	/* System Colors */
	Color COLOR_BLACK, COLOR_DARK_RED, COLOR_DARK_GREEN, COLOR_DARK_YELLOW, COLOR_DARK_BLUE;
	Color COLOR_DARK_MAGENTA, COLOR_DARK_CYAN, COLOR_GRAY, COLOR_DARK_GRAY, COLOR_RED;
	Color COLOR_GREEN, COLOR_YELLOW, COLOR_BLUE, COLOR_MAGENTA, COLOR_CYAN, COLOR_WHITE;
	
	/* System Font */
	MacFont systemFont;

	// AW
	int fScreenDepth;
	// AW
	
	/* Warning and Error Handlers */
	boolean warnings = true;
		
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

/**
 * Constructs a new instance of this class.
 * <p>
 * You must dispose the device when it is no longer required. 
 * </p>
 *
 * @param data the DeviceData which describes the receiver
 *
 * @see #create
 * @see #init
 * @see DeviceData
 */
public Device(DeviceData data) {
	if (data != null) {
		tracking = data.tracking;
		debug = data.debug;
	}
	create (data);
	init ();
	if (tracking) {
		errors = new Error [128];
		objects = new Object [128];
	}
	
	/* Initialize the system font slot */
	Font font = getSystemFont ();
	//FontData fd = font.getFontData ()[0];
	systemFont = font.handle;
}

protected void checkDevice () {
	if (fGDeviceHandle == 0) SWT.error (SWT.ERROR_DEVICE_DISPOSED);
}

protected void create (DeviceData data) {
}

protected void destroy () {
}

/**
 * Disposes of the operating system resources associated with
 * the receiver. After this method has been invoked, the receiver
 * will answer <code>true</code> when sent the message
 * <code>isDisposed()</code>.
 *
 * @see #release
 * @see #destroy
 * @see #checkDevice
 */
public void dispose () {
	if (isDisposed()) return;
	checkDevice ();
	release ();
	destroy ();
	fGDeviceHandle= 0;
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

/**
 * Returns a rectangle describing the receiver's size and location.
 *
 * @return the bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkDevice ();
	
	MacRect bounds= new MacRect();
	if (fGDeviceHandle != 0) {
		int pm= OS.getgdPMap(fGDeviceHandle);
		if (pm != 0)
			OS.GetPixBounds(pm, bounds.getData());
	}
	return bounds.toRectangle();
}

/**
 * Returns a rectangle which describes the area of the
 * receiver which is capable of displaying data.
 * 
 * @return the client area
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getBounds
 */
public Rectangle getClientArea () {
	checkDevice ();
	
	MacRect bounds= new MacRect();
	int gdh= OS.GetMainDevice();
	OS.GetAvailableWindowPositioningBounds(gdh, bounds.getData());
	return bounds.toRectangle();
}

/**
 * Returns the bit depth of the screen, which is the number of
 * bits it takes to represent the number of unique colors that
 * the screen is currently capable of displaying. This number 
 * will typically be one of 1, 8, 15, 16, 24 or 32.
 *
 * @return the depth of the screen
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getDepth () {
	checkDevice ();
	return fScreenDepth;
}

/**
 * Returns a <code>DeviceData</code> based on the receiver.
 * Modifications made to this <code>DeviceData</code> will not
 * affect the receiver.
 *
 * @return a <code>DeviceData</code> containing the device's data and attributes
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see DeviceData
 */
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

/**
 * Returns a point whose x coordinate is the horizontal
 * dots per inch of the display, and whose y coordinate
 * is the vertical dots per inch of the display.
 *
 * @return the horizontal and vertical DPI
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point getDPI () {
	checkDevice ();
	/* AW
	int xScreenNum = OS.XDefaultScreen (xDisplay);
	int width = OS.XDisplayWidth (xDisplay, xScreenNum);
	int height = OS.XDisplayHeight (xDisplay, xScreenNum);
	int mmX = OS.XDisplayWidthMM (xDisplay, xScreenNum);
	int mmY = OS.XDisplayHeightMM (xDisplay, xScreenNum);
	*/
	/* 0.03937 mm/inch */
	/* AW
	double inchesX = mmX * 0.03937;
	double inchesY = mmY * 0.03937;
	int x = (int)((width / inchesX) + 0.5);
	int y = (int)((height / inchesY) + 0.5);
	return new Point (x, y);
	*/
	
	if (fGDeviceHandle != 0) {
		int pm= OS.getgdPMap(fGDeviceHandle);
		if (pm != 0)
			return new Point(OS.getPixHRes(pm) >> 16, OS.getPixVRes(pm) >> 16);
	}
	return new Point(72, 72);
}

/**
 * Returns <code>FontData</code> objects which describe
 * the fonts that match the given arguments. If the
 * <code>faceName</code> is null, all fonts will be returned.
 *
 * @param faceName the name of the font to look for, or null
 * @param scalable true if scalable fonts should be returned.
 * @return the matching font data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public FontData [] getFontList (String faceName, boolean scalable) {	
	checkDevice ();
	/* AW
	String xlfd;
	if (faceName == null) {
		xlfd = "-*-*-*-*-*-*-*-*-*-*-*-*-*-*";
	} else {
		int dashIndex = faceName.indexOf('-');
		if (dashIndex < 0) {
			xlfd = "-*-" + faceName + "-*-*-*-*-*-*-*-*-*-*-*-*";
		} else {
			xlfd = "-" + faceName + "-*-*-*-*-*-*-*-*-*-*-*-*";
		}
	}
	*/
	/* Use the character encoding for the default locale */
	/* AW
	byte [] buffer1 = Converter.wcsToMbcs (null, xlfd, true);
	int [] ret = new int [1];
	int listPtr = OS.XListFonts (xDisplay, buffer1, 65535, ret);
	int ptr = listPtr;
	int [] intBuf = new int [1];
	FontData [] fd = new FontData [ret [0]];
	int fdIndex = 0;
	for (int i = 0; i < ret [0]; i++) {
		OS.memmove (intBuf, ptr, 4);
		int charPtr = intBuf [0];
		int length = OS.strlen (charPtr);
		byte [] buffer2 = new byte [length];
		OS.memmove (buffer2, charPtr, length);
		// Use the character encoding for the default locale
		char [] chars = Converter.mbcsToWcs (null, buffer2);
		FontData data = FontData.motif_new (new String (chars));
		boolean isScalable = data.averageWidth == 0 && data.pixels == 0 && data.points == 0;
		if (isScalable == scalable) {
			fd [fdIndex++] = data;
		}
		ptr += 4;
	}
	OS.XFreeFontNames (listPtr);
	if (fdIndex == ret [0]) return fd;
	FontData [] result = new FontData [fdIndex];
	System.arraycopy (fd, 0, result, 0, fdIndex);
	return result;
	*/
	return new FontData [0];
}

/**
 * Returns the matching standard color for the given
 * constant, which should be one of the color constants
 * specified in class <code>SWT</code>. Any value other
 * than one of the SWT color constants which is passed
 * in will result in the color black. This color should
 * not be free'd because it was allocated by the system,
 * not the application.
 *
 * @param id the color constant
 * @return the matching color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see SWT
 */
public Color getSystemColor (int id) {
	checkDevice ();
	/* AW
	XColor xColor = null;
	*/
	switch (id) {
		case SWT.COLOR_BLACK: 				return COLOR_BLACK;
		case SWT.COLOR_DARK_RED: 			return COLOR_DARK_RED;
		case SWT.COLOR_DARK_GREEN:	 		return COLOR_DARK_GREEN;
		case SWT.COLOR_DARK_YELLOW: 		return COLOR_DARK_YELLOW;
		case SWT.COLOR_DARK_BLUE: 			return COLOR_DARK_BLUE;
		case SWT.COLOR_DARK_MAGENTA: 		return COLOR_DARK_MAGENTA;
		case SWT.COLOR_DARK_CYAN: 			return COLOR_DARK_CYAN;
		case SWT.COLOR_GRAY: 				return COLOR_GRAY;
		case SWT.COLOR_DARK_GRAY: 			return COLOR_DARK_GRAY;
		case SWT.COLOR_RED: 				return COLOR_RED;
		case SWT.COLOR_GREEN: 				return COLOR_GREEN;
		case SWT.COLOR_YELLOW: 			return COLOR_YELLOW;
		case SWT.COLOR_BLUE: 				return COLOR_BLUE;
		case SWT.COLOR_MAGENTA: 			return COLOR_MAGENTA;
		case SWT.COLOR_CYAN: 				return COLOR_CYAN;
		case SWT.COLOR_WHITE: 				return COLOR_WHITE;
	}
	/* AW
	if (xColor == null) return COLOR_BLACK;
	return Color.motif_new (this, xColor);
	*/
	return Color.carbon_new(this, 0x000000, false);
}

/**
 * Returns a reasonable font for applications to use.
 * On some platforms, this will match the "default font"
 * or "system font" if such can be found.  This font
 * should not be free'd because it was allocated by the
 * system, not the application.
 * <p>
 * Typically, applications which want the default look
 * should simply not set the font on the widgets they
 * create. Widgets are always created with the correct
 * default font for the class of user-interface component
 * they represent.
 * </p>
 *
 * @return a font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Font getSystemFont () {
	checkDevice ();
	return Font.carbon_new (this, systemFont);
}

/**
 * Returns <code>true</code> if the underlying window system prints out
 * warning messages on the console, and <code>setWarnings</code>
 * had previously been called with <code>true</code>.
 *
 * @return <code>true</code>if warnings are being handled, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public boolean getWarnings () {
	checkDevice ();
	return warnings;
}

protected void init () {

	if (fGDeviceHandle != 0) {
		int pm= OS.getgdPMap(fGDeviceHandle);
		if (pm != 0)
			fScreenDepth= OS.GetPixDepth(pm);
	}
	if (fScreenDepth == 0)
		fScreenDepth= 32;	// a guess
	
	/*
	* The following colors are listed in the Windows
	* Programmer's Reference as the colors in the default
	* palette.
	*/
	COLOR_BLACK = 		Color.carbon_new(this, 0x000000, true);
	COLOR_DARK_RED = 	Color.carbon_new(this, 0x800000, true);
	COLOR_DARK_GREEN = 	Color.carbon_new(this, 0x008000, true);
	COLOR_DARK_YELLOW = Color.carbon_new(this, 0x808000, true);
	COLOR_DARK_BLUE = 	Color.carbon_new(this, 0x000080, true);
	COLOR_DARK_MAGENTA =Color.carbon_new(this, 0x800080, true);
	COLOR_DARK_CYAN = 	Color.carbon_new(this, 0x008080, true);
	COLOR_GRAY = 		Color.carbon_new(this, 0xC0C0C0, true);
	COLOR_DARK_GRAY = 	Color.carbon_new(this, 0x808080, true);
	COLOR_RED = 		Color.carbon_new(this, 0xFF0000, true);
	COLOR_GREEN = 		Color.carbon_new(this, 0x00FF00, true);
	COLOR_YELLOW = 		Color.carbon_new(this, 0xFFFF00, true);
	COLOR_BLUE = 		Color.carbon_new(this, 0x0000FF, true);
	COLOR_MAGENTA = 	Color.carbon_new(this, 0xFF00FF, true);
	COLOR_CYAN = 		Color.carbon_new(this, 0x00FFFF, true);
	COLOR_WHITE = 		Color.carbon_new(this, 0xFFFFFF, true);
}

/**	 
 * Invokes platform specific functionality to allocate a new GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Device</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param data the platform specific GC data 
 * @return the platform specific GC handle
 *
 * @private
 */
public abstract int internal_new_GC (GCData data);

/**	 
 * Invokes platform specific functionality to dispose a GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Device</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param handle the platform specific GC handle
 * @param data the platform specific GC data 
 *
 * @private
 */
public abstract void internal_dispose_GC (int handle, GCData data);

/**
 * Returns <code>true</code> if the device has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the device.
 * When a device has been disposed, it is an error to
 * invoke any other method using the device.
 *
 * @return <code>true</code> when the device is disposed and <code>false</code> otherwise
 */
public boolean isDisposed () {
	return fGDeviceHandle == 0;
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
	COLOR_BLACK = COLOR_DARK_RED = COLOR_DARK_GREEN = COLOR_DARK_YELLOW =
	COLOR_DARK_BLUE = COLOR_DARK_MAGENTA = COLOR_DARK_CYAN = COLOR_GRAY = COLOR_DARK_GRAY = COLOR_RED =
	COLOR_GREEN = COLOR_YELLOW = COLOR_BLUE = COLOR_MAGENTA = COLOR_CYAN = COLOR_WHITE = null;
}

/**
 * If the underlying window system supports printing warning messages
 * to the console, setting warnings to <code>true</code> prevents these
 * messages from being printed. If the argument is <code>false</code>
 * message printing is not blocked.
 *
 * @param warnings <code>true</code>if warnings should be handled, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setWarnings (boolean warnings) {
	checkDevice ();
	this.warnings = warnings;
	if (debug) return;
}
}
