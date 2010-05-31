/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.*;

/**
 * This class is the abstract superclass of all device objects,
 * such as the Display device and the Printer device. Devices
 * can have a graphics context (GC) created for them, and they
 * can be drawn on by sending messages to the associated GC.
 *
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public abstract class Device implements Drawable {
	
	/* Debugging */
	public static boolean DEBUG;
	boolean debug = DEBUG;
	boolean tracking = DEBUG;
	Error [] errors;
	Object [] objects;
	Object trackingLock;
	
	/* Disposed flag */
	boolean disposed, warnings;
	
	int colorspace;
	
	/*
	* The following colors are listed in the Windows
	* Programmer's Reference as the colors in the default
	* palette.
	*/
	Color COLOR_BLACK, COLOR_DARK_RED, COLOR_DARK_GREEN, COLOR_DARK_YELLOW, COLOR_DARK_BLUE;
	Color COLOR_DARK_MAGENTA, COLOR_DARK_CYAN, COLOR_GRAY, COLOR_DARK_GRAY, COLOR_RED;
	Color COLOR_GREEN, COLOR_YELLOW, COLOR_BLUE, COLOR_MAGENTA, COLOR_CYAN, COLOR_WHITE;

	/* System Font */
	Font systemFont;
	
	/* Device DPI */
	Point dpi;

	/* Callbacks */
	Callback drawPatternCallback, axialShadingCallback, releaseCallback;
	int drawPatternProc, axialShadingProc, releaseProc;

	/*
	* TEMPORARY CODE. When a graphics object is
	* created and the device parameter is null,
	* the current Display is used. This presents
	* a problem because SWT graphics does not
	* reference classes in SWT widgets. The correct
	* fix is to remove this feature. Unfortunately,
	* too many application programs rely on this
	* feature.
	*/
	protected static Device CurrentDevice;
	protected static Runnable DeviceFinder;
	static {
		try {
			Class.forName ("org.eclipse.swt.widgets.Display");
		} catch (ClassNotFoundException e) {}
	}	

/*
* TEMPORARY CODE.
*/
static synchronized Device getDevice () {
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
 * @see #create
 * @see #init
 * 
 * @since 3.1
 */
public Device() {
	this(null);
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
	synchronized (Device.class) {
		if (data != null) {
			debug = data.debug;
			tracking = data.tracking;
		}
		if (tracking) {
			errors = new Error [128];
			objects = new Object [128];
			trackingLock = new Object ();
		}
		create (data);
		init ();
	}
}

int axialShadingProc (int ref, int in, int out) {
	Object object = OS.JNIGetObject (ref);
	if (object instanceof Pattern) {
		return ((Pattern) object).axialShadingProc (ref, in, out);
	}
	return 0;
}

/**
 * Throws an <code>SWTException</code> if the receiver can not
 * be accessed by the caller. This may include both checks on
 * the state of the receiver and more generally on the entire
 * execution context. This method <em>should</em> be called by
 * device implementors to enforce the standard SWT invariants.
 * <p>
 * Currently, it is an error to invoke any method (other than
 * <code>isDisposed()</code> and <code>dispose()</code>) on a
 * device that has had its <code>dispose()</code> method called.
 * </p><p>
 * In future releases of SWT, there may be more or fewer error
 * checks and exceptions may be thrown for different reasons.
 * <p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
protected void checkDevice () {
	if (disposed) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
}

/**
 * Creates the device in the operating system.  If the device
 * does not have a handle, this method may do nothing depending
 * on the device.
 * <p>
 * This method is called before <code>init</code>.
 * </p><p>
 * Subclasses are supposed to reimplement this method and not
 * call the <code>super</code> implementation.
 * </p>
 *
 * @param data the DeviceData which describes the receiver
 *
 * @see #init
 */
protected void create (DeviceData data) {
}

void createPatternCallbacks () {
	if (drawPatternCallback == null) {
		drawPatternCallback = new Callback(this, "drawPatternProc", 2);
		drawPatternProc = drawPatternCallback.getAddress();
		if (drawPatternProc == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	}
	if (axialShadingCallback == null) {
		axialShadingCallback = new Callback(this, "axialShadingProc", 3);
		axialShadingProc = axialShadingCallback.getAddress();
		if (axialShadingProc == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	}
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
	synchronized (Device.class) {
		if (isDisposed()) return;
		checkDevice ();
		release ();
		destroy ();
		disposed = true;
		if (tracking) {
			synchronized (trackingLock) {
				objects = null;
				errors = null;
				trackingLock = null;
			}
		}
	}
}

void dispose_Object (Object object) {
	synchronized (trackingLock) {
		for (int i=0; i<objects.length; i++) {
			if (objects [i] == object) {
				objects [i] = null;
				errors [i] = null;
				return;
			}
		}
	}
}

/**
 * Destroys the device in the operating system and releases
 * the device's handle.  If the device does not have a handle,
 * this method may do nothing depending on the device.
 * <p>
 * This method is called after <code>release</code>.
 * </p><p>
 * Subclasses are supposed to reimplement this method and not
 * call the <code>super</code> implementation.
 * </p>
 *
 * @see #dispose
 * @see #release
 */
protected void destroy () {
}

int drawPatternProc (int ref, int context) {
	Object object = OS.JNIGetObject (ref);
	if (object instanceof Pattern) {
		return ((Pattern) object).drawPatternProc (ref, context);
	}
	return 0;
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
	int gdevice = OS.GetMainDevice();
	int[] ptr = new int[1];
	OS.memmove(ptr, gdevice, 4);
	GDevice device = new GDevice();
	OS.memmove(device, ptr[0], GDevice.sizeof);	
	return new Rectangle(device.left, device.top, device.right - device.left, device.bottom - device.top);
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
	checkDevice();
	DeviceData data = new DeviceData ();
	data.debug = debug;
	data.tracking = tracking;
	if (tracking) {
		synchronized (trackingLock) {
			int count = 0, length = objects.length;
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
		}
	} else {
		data.objects = new Object [0];
		data.errors = new Error [0];
	}
	return data;
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
	int gdevice = OS.GetMainDevice();
	Rect rect = new Rect();
	OS.GetAvailableWindowPositioningBounds(gdevice, rect);
	return new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
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
	int gdevice = OS.GetMainDevice();
	int[] ptr = new int[1];
	OS.memmove(ptr, gdevice, 4);
	GDevice device = new GDevice();
	OS.memmove(device, ptr[0], GDevice.sizeof);
	return OS.GetPixDepth(device.gdPMap);
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
	int gdevice = OS.GetMainDevice();
	int[] ptr = new int[1];
	OS.memmove(ptr, gdevice, 4);
	GDevice device = new GDevice();
	OS.memmove(device, ptr[0], GDevice.sizeof);
	OS.memmove(ptr, device.gdPMap, 4);
	PixMap pixmap = new PixMap();
	OS.memmove(pixmap, ptr[0], PixMap.sizeof);
	return new Point (OS.Fix2Long (pixmap.hRes), OS.Fix2Long (pixmap.vRes));
}

/**
 * Returns <code>FontData</code> objects which describe
 * the fonts that match the given arguments. If the
 * <code>faceName</code> is null, all fonts will be returned.
 *
 * @param faceName the name of the font to look for, or null
 * @param scalable if true only scalable fonts are returned, otherwise only non-scalable fonts are returned.
 * @return the matching font data
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public FontData[] getFontList (String faceName, boolean scalable) {
	checkDevice ();
	if (!scalable) return new FontData[0];	
	int count = 0;
	int[] buffer = new int[1];
	CFRange range = new CFRange ();
	OS.ATSUGetFontIDs(null, 0, buffer);
	FontData[] fds = new FontData[buffer[0]];
	int status = OS.ATSFontIteratorCreate (OS.kATSFontContextLocal, 0, 0, OS.kATSOptionFlagsDefaultScope, buffer);
	int iter = buffer[0];
	while (status == OS.noErr) {
		status = OS.ATSFontIteratorNext(iter, buffer);
		if (status == OS.noErr) {
			int font = buffer[0];
			if (OS.ATSFontGetName(font, 0, buffer) == OS.noErr) {
				range.length = OS.CFStringGetLength(buffer[0]);
				char [] chars = new char[range.length];
				OS.CFStringGetCharacters(buffer[0], range, chars);
				OS.CFRelease(buffer[0]);
				String atsName = new String(chars);
				int platformCode = OS.kFontUnicodePlatform, encoding = OS.kCFStringEncodingUnicode;
				if (OS.ATSUFindFontName(font, OS.kFontFamilyName, platformCode, OS.kFontNoScriptCode, OS.kFontNoLanguageCode, 0, null, buffer, null) != OS.noErr) {
					platformCode = OS.kFontNoPlatformCode;
					encoding = OS.kCFStringEncodingMacRoman;
					if (OS.ATSUFindFontName(font, OS.kFontFamilyName, platformCode, OS.kFontNoScriptCode, OS.kFontNoLanguageCode, 0, null, buffer, null) != OS.noErr) {
						continue;
					}
				}
				byte[] bytes = new byte[buffer[0]];
				OS.ATSUFindFontName(font, OS.kFontFamilyName, platformCode, OS.kFontNoScriptCode, OS.kFontNoLanguageCode, bytes.length, bytes, buffer, null);
				int ptr = OS.CFStringCreateWithBytes(0, bytes, bytes.length, encoding, false);
				if (ptr != 0) {
					range.length = OS.CFStringGetLength(ptr);
					if (range.length != 0) {
						chars = new char[range.length];
						OS.CFStringGetCharacters(ptr, range, chars);
						String name = new String(chars);
						if (!name.startsWith(".")) {
							if (faceName == null || Compatibility.equalsIgnoreCase(faceName, name)) {
								int s = SWT.NORMAL;
								if (atsName.indexOf("Italic") != -1) s |= SWT.ITALIC;
								if (atsName.indexOf("Bold") != -1) s |= SWT.BOLD;
								FontData data = new FontData(name, 0, s);
								data.atsName = atsName;
								if (count == fds.length) {
									FontData[] newFDs = new FontData[count + 4];
									System.arraycopy(fds, 0, newFDs, 0, count);
									fds = newFDs;
								}
								fds[count++] = data;
							}
						}
					}
					OS.CFRelease(ptr);
				}
			}
		}
	}
	if (iter != 0) {
		buffer [0] = iter;
		OS.ATSFontIteratorRelease (buffer);
	}
	if (count == fds.length) return fds;
	FontData[] result = new FontData[count];
	System.arraycopy(fds, 0, result, 0, count);
	return result;
}

Point getScreenDPI() {
	int gdevice = OS.GetMainDevice();
	int[] ptr = new int[1];
	OS.memmove(ptr, gdevice, 4);
	GDevice device = new GDevice();
	OS.memmove(device, ptr[0], GDevice.sizeof);
	OS.memmove(ptr, device.gdPMap, 4);
	PixMap pixmap = new PixMap();
	OS.memmove(pixmap, ptr[0], PixMap.sizeof);
	return new Point (OS.Fix2Long (pixmap.hRes), OS.Fix2Long (pixmap.vRes));
}

/**
 * Returns the matching standard color for the given
 * constant, which should be one of the color constants
 * specified in class <code>SWT</code>. Any value other
 * than one of the SWT color constants which is passed
 * in will result in the color black. This color should
 * not be freed because it was allocated by the system,
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
	return COLOR_BLACK;
}

/**
 * Returns a reasonable font for applications to use.
 * On some platforms, this will match the "default font"
 * or "system font" if such can be found.  This font
 * should not be freed because it was allocated by the
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
	return systemFont;
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

/**
 * Initializes any internal resources needed by the
 * device.
 * <p>
 * This method is called after <code>create</code>.
 * </p><p>
 * If subclasses reimplement this method, they must
 * call the <code>super</code> implementation.
 * </p>
 * 
 * @see #create
 */
protected void init () {
	colorspace = OS.CGColorSpaceCreateDeviceRGB();
	if (colorspace == 0) SWT.error(SWT.ERROR_NO_HANDLES);

	releaseCallback = new Callback (this, "releaseProc", 3);
	releaseProc = releaseCallback.getAddress ();
	if (releaseProc == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	
	/* Create the standard colors */
	COLOR_BLACK = new Color (this, 0,0,0);
	COLOR_DARK_RED = new Color (this, 0x80,0,0);
	COLOR_DARK_GREEN = new Color (this, 0,0x80,0);
	COLOR_DARK_YELLOW = new Color (this, 0x80,0x80,0);
	COLOR_DARK_BLUE = new Color (this, 0,0,0x80);
	COLOR_DARK_MAGENTA = new Color (this, 0x80,0,0x80);
	COLOR_DARK_CYAN = new Color (this, 0,0x80,0x80);
	COLOR_GRAY = new Color (this, 0xC0,0xC0,0xC0);
	COLOR_DARK_GRAY = new Color (this, 0x80,0x80,0x80);
	COLOR_RED = new Color (this, 0xFF,0,0);
	COLOR_GREEN = new Color (this, 0,0xFF,0);
	COLOR_YELLOW = new Color (this, 0xFF,0xFF,0);
	COLOR_BLUE = new Color (this, 0,0,0xFF);
	COLOR_MAGENTA = new Color (this, 0xFF,0,0xFF);
	COLOR_CYAN = new Color (this, 0,0xFF,0xFF);
	COLOR_WHITE = new Color (this, 0xFF,0xFF,0xFF);
	
	/* Initialize the system font slot */
	//TEMPORARY CODE
	boolean smallFonts = System.getProperty("org.eclipse.swt.internal.carbon.smallFonts") != null;
	byte [] family = new byte [256];
	short [] size = new short [1];
	byte [] style = new byte [1];
	int themeFont = smallFonts ? OS.kThemeSmallSystemFont : OS.kThemeSystemFont;
	OS.GetThemeFont ((short) themeFont, (short) OS.smSystemScript, family, size, style);
	short id = OS.FMGetFontFamilyFromName (family);
	int [] font = new int [1]; 
	OS.FMGetFontFromFontFamilyInstance (id, style [0], font, null);
	Point dpi = this.dpi = getDPI(), screenDPI = getScreenDPI();
	systemFont = Font.carbon_new (this, OS.FMGetATSFontRefFromFont (font [0]), style [0], size [0] * dpi.y / screenDPI.y);
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
 * @noreference This method is not intended to be referenced by clients.
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
 * @param hDC the platform specific GC handle
 * @param data the platform specific GC data 
 * 
 * @noreference This method is not intended to be referenced by clients.
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
	synchronized (Device.class) {
		return disposed;
	}
}

/**
 * Loads the font specified by a file.  The font will be
 * present in the list of fonts available to the application.
 *
 * @param path the font file path
 * @return whether the font was successfully loaded
 *
 * @exception SWTException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if path is null</li>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see Font
 * 
 * @since 3.3
 */
public boolean loadFont (String path) {
	checkDevice();
	if (path == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	boolean result = false;
	char [] chars = new char [path.length ()];
	path.getChars (0, chars.length, chars, 0);
	int str = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, chars, chars.length);
	if (str != 0) {
		int url = OS.CFURLCreateWithFileSystemPath (OS.kCFAllocatorDefault, str, OS.kCFURLPOSIXPathStyle, false);
		if (url != 0) {
			byte [] fsRef = new byte [80];
			if (OS.CFURLGetFSRef (url, fsRef)) {
				byte [] fsSpec = new byte [70];
				if (OS.FSGetCatalogInfo (fsRef, 0, null, null, fsSpec, null) == OS.noErr) {
					int [] container = new int [1];
					result = OS.ATSFontActivateFromFileSpecification (fsSpec, OS.kATSFontContextLocal, OS.kATSFontFormatUnspecified, 0, OS.kATSOptionFlagsDefault, container) == OS.noErr;
				}
			}
			OS.CFRelease (url);
		}
		OS.CFRelease (str);
	}
	return result;
}

void new_Object (Object object) {
	synchronized (trackingLock) {
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
}

/**
 * Releases any internal resources back to the operating
 * system and clears all fields except the device handle.
 * <p>
 * When a device is destroyed, resources that were acquired
 * on behalf of the programmer need to be returned to the
 * operating system.  For example, if the device allocated a
 * font to be used as the system font, this font would be
 * freed in <code>release</code>.  Also,to assist the garbage
 * collector and minimize the amount of memory that is not
 * reclaimed when the programmer keeps a reference to a
 * disposed device, all fields except the handle are zero'd.
 * The handle is needed by <code>destroy</code>.
 * </p>
 * This method is called before <code>destroy</code>.
 * </p><p>
 * If subclasses reimplement this method, they must
 * call the <code>super</code> implementation.
 * </p>
 *
 * @see #dispose
 * @see #destroy
 */
protected void release () {
	if (releaseCallback != null) releaseCallback.dispose ();
	if (drawPatternCallback != null) drawPatternCallback.dispose();
	if (axialShadingCallback != null) axialShadingCallback.dispose();
	releaseCallback = axialShadingCallback = drawPatternCallback = null;
	releaseProc = axialShadingProc = drawPatternProc = 0;

	OS.CGColorSpaceRelease(colorspace);
	colorspace = 0;

	if (COLOR_BLACK != null) COLOR_BLACK.dispose();
	if (COLOR_DARK_RED != null) COLOR_DARK_RED.dispose();
	if (COLOR_DARK_GREEN != null) COLOR_DARK_GREEN.dispose();
	if (COLOR_DARK_YELLOW != null) COLOR_DARK_YELLOW.dispose();
	if (COLOR_DARK_BLUE != null) COLOR_DARK_BLUE.dispose();
	if (COLOR_DARK_MAGENTA != null) COLOR_DARK_MAGENTA.dispose();
	if (COLOR_DARK_CYAN != null) COLOR_DARK_CYAN.dispose();
	if (COLOR_GRAY != null) COLOR_GRAY.dispose();
	if (COLOR_DARK_GRAY != null) COLOR_DARK_GRAY.dispose();
	if (COLOR_RED != null) COLOR_RED.dispose();
	if (COLOR_GREEN != null) COLOR_GREEN.dispose();
	if (COLOR_YELLOW != null) COLOR_YELLOW.dispose();
	if (COLOR_BLUE != null) COLOR_BLUE.dispose();
	if (COLOR_MAGENTA != null) COLOR_MAGENTA.dispose();
	if (COLOR_CYAN != null) COLOR_CYAN.dispose();
	if (COLOR_WHITE != null) COLOR_WHITE.dispose();
	COLOR_BLACK = COLOR_DARK_RED = COLOR_DARK_GREEN = COLOR_DARK_YELLOW = COLOR_DARK_BLUE =
	COLOR_DARK_MAGENTA = COLOR_DARK_CYAN = COLOR_GRAY = COLOR_DARK_GRAY = COLOR_RED =
	COLOR_GREEN = COLOR_YELLOW = COLOR_BLUE = COLOR_MAGENTA = COLOR_CYAN = COLOR_WHITE = null;
}

int releaseProc (int info, int data, int size) {
	OS.DisposePtr(data);
	return 0;
}

/**
 * If the underlying window system supports printing warning messages
 * to the console, setting warnings to <code>false</code> prevents these
 * messages from being printed. If the argument is <code>true</code> then
 * message printing is not blocked.
 *
 * @param warnings <code>true</code>if warnings should be printed, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setWarnings (boolean warnings) {
	checkDevice ();
	this.warnings = warnings;
}

}
