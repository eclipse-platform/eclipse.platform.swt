/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;

/**
 * This class is the abstract superclass of all device objects,
 * such as the Display device and the Printer device. Devices
 * can have a graphics context (GC) created for them, and they
 * can be drawn on by sending messages to the associated GC.
 *
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public abstract class Device implements Drawable {
	/**
	 * the handle to the X Display
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public int xDisplay;
	
	/**
	 * whether the XLFD resolution should match the
	 * resolution of the device when fonts are created
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	// TEMPORARY CODE
	public boolean setDPI;
	
	/* Debugging */
	public static boolean DEBUG;
	boolean debug = DEBUG;
	boolean tracking = DEBUG;
	Error [] errors;
	Object [] objects;
	Object trackingLock;

	/* Arguments for XtOpenDisplay */
	String display_name;
	String application_name;
	String application_class;
		
	/* Colormap and reference count for this display */
	XColor [] xcolors;
	int [] colorRefCount;
	
	/* System Colors */
	Color COLOR_BLACK, COLOR_DARK_RED, COLOR_DARK_GREEN, COLOR_DARK_YELLOW, COLOR_DARK_BLUE;
	Color COLOR_DARK_MAGENTA, COLOR_DARK_CYAN, COLOR_GRAY, COLOR_DARK_GRAY, COLOR_RED;
	Color COLOR_GREEN, COLOR_YELLOW, COLOR_BLUE, COLOR_MAGENTA, COLOR_CYAN, COLOR_WHITE;
	
	/* System Font */
	Font systemFont;
	
	int shellHandle;

	boolean useXRender;

	static boolean CAIRO_LOADED;

	/* Parsing Tables */
	int tabPointer, crPointer;
	/**
	 * parse table mappings for tab and cr
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	// TEMPORARY CODE
	public int tabMapping, crMapping;

	/* Xt Warning and Error Handlers */
	boolean warnings = true;
	Callback xtWarningCallback, xtErrorCallback;
	int xtWarningProc, xtErrorProc, xtNullWarningProc, xtNullErrorProc;	
	
	/* X Warning and Error Handlers */
	static Callback XErrorCallback, XIOErrorCallback;
	static int XErrorProc, XIOErrorProc, XNullErrorProc, XNullIOErrorProc;
	static Device[] Devices = new Device[4];

	/* Initialize X and Xt */
	static {
		/*
		* This code is intentionally commented.
		*/
//		OS.XInitThreads ();
//		OS.XtToolkitThreadInitialize ();
		OS.XtToolkitInitialize ();
	}
	
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
* TEMPORARY CODE 
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
			display_name = data.display_name;
			application_name = data.application_name;
			application_class = data.application_class;
			tracking = data.tracking;
			debug = data.debug;
		}
		if (tracking) {
			errors = new Error [128];
			objects = new Object [128];
			trackingLock = new Object ();
		}
		create (data);
		init ();
		register (this);
		
		/* Initialize the system font slot */
		systemFont = getSystemFont ();
	}
}

void checkCairo() {
	if (CAIRO_LOADED) return;
	try {
		/* Check if cairo is available on the system */
		byte[] buffer = Converter.wcsToMbcs(null, "libcairo.so.2", true);
		int /*long*/ libcairo = OS.dlopen(buffer, OS.RTLD_LAZY);
		if (libcairo != 0) {
			OS.dlclose(libcairo);
		} else {
			try {
				System.loadLibrary("cairo-swt");
			} catch (UnsatisfiedLinkError e) {
				/* Ignore problems loading the fallback library */
			}
		}
		Class.forName("org.eclipse.swt.internal.cairo.Cairo");
		CAIRO_LOADED = true;
	} catch (Throwable t) {
		SWT.error(SWT.ERROR_NO_GRAPHICS_LIBRARY, t, " [Cairo required]");
	}
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
	if (xDisplay == 0) SWT.error (SWT.ERROR_DEVICE_DISPOSED);
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

synchronized static void deregister (Device device) {
	for (int i=0; i<Devices.length; i++) {
		if (device == Devices [i]) Devices [i] = null;
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
		deregister (this);
		xDisplay = 0;
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

static synchronized Device findDevice (int xDisplay) {
	for (int i=0; i<Devices.length; i++) {
		Device device = Devices [i];
		if (device != null && device.xDisplay == xDisplay) {
			return device;
		}
	}
	return null;
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
	int screen = OS.XDefaultScreen (xDisplay);
	int width = OS.XDisplayWidth (xDisplay, screen);
	int height = OS.XDisplayHeight (xDisplay, screen);
	return new Rectangle (0, 0, width, height);
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
	return getBounds ();
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
	int xScreenPtr = OS.XDefaultScreenOfDisplay (xDisplay);
	return OS.XDefaultDepthOfScreen (xScreenPtr);
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
	data.display_name = display_name;
	data.application_name = application_name;
	data.application_class = application_class;
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
	int xScreenNum = OS.XDefaultScreen (xDisplay);
	int width = OS.XDisplayWidth (xDisplay, xScreenNum);
	int height = OS.XDisplayHeight (xDisplay, xScreenNum);
	int mmX = OS.XDisplayWidthMM (xDisplay, xScreenNum);
	int mmY = OS.XDisplayHeightMM (xDisplay, xScreenNum);
	/* 0.03937 mm/inch */
	double inchesX = mmX * 0.03937;
	double inchesY = mmY * 0.03937;
	int x = (int)((width / inchesX) + 0.5);
	int y = (int)((height / inchesY) + 0.5);
	return new Point (x, y);
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
public FontData [] getFontList (String faceName, boolean scalable) {	
	checkDevice ();
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
	/* Use the character encoding for the default locale */
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
		/* Use the character encoding for the default locale */
		char [] chars = Converter.mbcsToWcs (null, buffer2);
		try {
			FontData data = FontData.motif_new (new String (chars));
			boolean isScalable = data.averageWidth == 0 && data.pixels == 0 && data.points == 0;
			if (isScalable == scalable) {
				fd [fdIndex++] = data;
			}
		} catch (Exception e) {
			/* do not add the font to the list */
		}
		ptr += 4;
	}
	OS.XFreeFontNames (listPtr);
	if (fdIndex == ret [0]) return fd;
	FontData [] result = new FontData [fdIndex];
	System.arraycopy (fd, 0, result, 0, fdIndex);
	return result;
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
		case SWT.COLOR_DARK_YELLOW: 			return COLOR_DARK_YELLOW;
		case SWT.COLOR_DARK_BLUE: 			return COLOR_DARK_BLUE;
		case SWT.COLOR_DARK_MAGENTA: 			return COLOR_DARK_MAGENTA;
		case SWT.COLOR_DARK_CYAN: 			return COLOR_DARK_CYAN;
		case SWT.COLOR_GRAY: 				return COLOR_GRAY;
		case SWT.COLOR_DARK_GRAY: 			return COLOR_DARK_GRAY;
		case SWT.COLOR_RED: 				return COLOR_RED;
		case SWT.COLOR_GREEN: 				return COLOR_GREEN;
		case SWT.COLOR_YELLOW: 				return COLOR_YELLOW;
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
	return _getWarnings();
}

boolean _getWarnings () {
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
	if (debug) OS.XSynchronize (xDisplay, true);

	int[] event_basep = new int[1], error_basep = new int [1];
	if (OS.XRenderQueryExtension (xDisplay, event_basep, error_basep)) {
		int[] major_versionp = new int[1], minor_versionp = new int [1];
		OS.XRenderQueryVersion (xDisplay, major_versionp, minor_versionp);
		useXRender = major_versionp[0] > 0 || (major_versionp[0] == 0 && minor_versionp[0] >= 8);
	}
		
	/* Create the warning and error callbacks */
	Class clazz = getClass ();
	synchronized (clazz) {
		if (XErrorCallback == null) {
			XErrorCallback = new Callback (clazz, "XErrorProc", 2);
			XNullErrorProc = XErrorCallback.getAddress ();
			if (XNullErrorProc == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
			XErrorProc = OS.XSetErrorHandler (XNullErrorProc);
		}
		if (XIOErrorCallback == null) {
			XIOErrorCallback = new Callback (clazz, "XIOErrorProc", 1);
			XNullIOErrorProc = XIOErrorCallback.getAddress ();
			if (XNullIOErrorProc == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
			XIOErrorProc = OS.XSetIOErrorHandler (XNullIOErrorProc);
		}
	}
	xtWarningCallback = new Callback (this, "xtWarningProc", 1);
	xtNullWarningProc = xtWarningCallback.getAddress ();
	if (xtNullWarningProc == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	xtErrorCallback = new Callback (this, "xtErrorProc", 1);
	xtNullErrorProc = xtErrorCallback.getAddress ();
	if (xtNullErrorProc == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	int xtContext = OS.XtDisplayToApplicationContext (xDisplay);
	xtWarningProc = OS.XtAppSetWarningHandler (xtContext, xtNullWarningProc);
	xtErrorProc = OS.XtAppSetErrorHandler (xtContext, xtNullErrorProc);
	
	/* Only use palettes for <= 8 bpp default visual */
	int xScreenPtr = OS.XDefaultScreenOfDisplay (xDisplay);
	int defaultDepth = OS.XDefaultDepthOfScreen (xScreenPtr);
	if (defaultDepth <= 8) {
		int numColors = 1 << defaultDepth;
		colorRefCount = new int [numColors];
		xcolors = new XColor [numColors];
	}
	
	/*
	* The following colors are listed in the Windows
	* Programmer's Reference as the colors in the default
	* palette.
	*/
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

	int widgetClass = OS.topLevelShellWidgetClass ();
	shellHandle = OS.XtAppCreateShell (null, null, widgetClass, xDisplay, null, 0);
	if (shellHandle == 0) SWT.error(SWT.ERROR_NO_HANDLES);

	/* Create the parsing tables */
	byte[] tabBuffer = {(byte) '\t', 0};
	tabPointer = OS.XtMalloc (tabBuffer.length);
	OS.memmove (tabPointer, tabBuffer, tabBuffer.length);		
	int tabString = OS.XmStringComponentCreate(OS.XmSTRING_COMPONENT_TAB, 0, null);
	int [] argList = {
		OS.XmNpattern, tabPointer,
		OS.XmNsubstitute, tabString,
	};
	tabMapping = OS.XmParseMappingCreate(argList, argList.length / 2);
	OS.XmStringFree(tabString);	
	byte[] crBuffer = {(byte) '\n', 0};
	crPointer = OS.XtMalloc (crBuffer.length);		
	OS.memmove (crPointer, crBuffer, crBuffer.length);		
	int crString = OS.XmStringComponentCreate(OS.XmSTRING_COMPONENT_SEPARATOR, 0, null);
	argList = new int[] {
		OS.XmNpattern, crPointer,
		OS.XmNsubstitute, crString,
	};
	crMapping = OS.XmParseMappingCreate(argList, argList.length / 2);
	OS.XmStringFree(crString);
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
		return xDisplay == 0;
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
	//TEMPORARY CODE
	/*if (true)*/ return false;
//	int index = path.lastIndexOf ("/");
//	if (index != -1) path = path.substring (0, index);
//	int [] ndirs = new int [1];
//	int dirs = OS.XGetFontPath (xDisplay, ndirs);
//	int [] ptr = new int [1];
//	for (int i = 0; i < ndirs [0]; i++) {
//		OS.memmove (ptr, dirs + (i * 4), 4);
//		int length = OS.strlen (ptr [0]);
//		byte [] buffer = new byte [length];
//		OS.memmove (buffer, ptr [0], length);
//		if (Converter.mbcsToWcs (null, buffer).equals (path)) {
//			OS.XFreeFontPath (dirs);
//			return true;
//		}
//	}
//	int newDirs = OS.XtMalloc ((ndirs [0] + 1) * 4);
//	int[] dirsBuffer = new int [ndirs [0] + 1];
//	OS.memmove (dirsBuffer, dirs, ndirs [0] * 4);
//	byte[] buffer = Converter.wcsToMbcs (null, path, true);
//	int pathPtr = OS.XtMalloc (buffer.length);
//	OS.memmove (pathPtr, buffer, buffer.length);
//	dirsBuffer [dirsBuffer.length - 1] = pathPtr;
//	OS.memmove (newDirs, dirsBuffer, dirsBuffer.length * 4);
//	OS.XSetFontPath (xDisplay, newDirs, dirsBuffer.length);
//	OS.XFreeFontPath (dirs);
//	OS.XFree (newDirs);
//	OS.XFree (pathPtr);
//	return true;
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

static synchronized void register (Device device) {
	for (int i=0; i<Devices.length; i++) {
		if (Devices [i] == null) {
			Devices [i] = device;
			return;
		}
	}
	Device [] newDevices = new Device [Devices.length + 4];
	System.arraycopy (Devices, 0, newDevices, 0, Devices.length);
	newDevices [Devices.length] = device;
	Devices = newDevices;
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
	/* Free the parsing tables */
	OS.XtFree(tabPointer);
	OS.XtFree(crPointer);
	OS.XmParseMappingFree(tabMapping);
	OS.XmParseMappingFree(crMapping);
	tabPointer = crPointer = tabMapping = crMapping = 0;

	if (shellHandle != 0) OS.XtDestroyWidget (shellHandle);
	shellHandle = 0;

	/*
	* Free the palette.  Note that this disposes all colors on
	* the display that were allocated using the Color constructor.
	*/
	if (xcolors != null) {
		int xScreen = OS.XDefaultScreen (xDisplay);
		int xColormap = OS.XDefaultColormap (xDisplay, xScreen);
		int [] pixel = new int [1];
		for (int i = 0; i < xcolors.length; i++) {
			XColor color = xcolors [i];
			if (color != null) {
				pixel [0] = color.pixel;
				while (colorRefCount [i] > 0) {
					OS.XFreeColors (xDisplay, xColormap, pixel, 1, 0);
					--colorRefCount [i];
				}
			}
		}
	}
	xcolors = null;
	colorRefCount = null;
	
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
	COLOR_BLACK = COLOR_DARK_RED = COLOR_DARK_GREEN = COLOR_DARK_YELLOW =
	COLOR_DARK_BLUE = COLOR_DARK_MAGENTA = COLOR_DARK_CYAN = COLOR_GRAY = COLOR_DARK_GRAY = COLOR_RED =
	COLOR_GREEN = COLOR_YELLOW = COLOR_BLUE = COLOR_MAGENTA = COLOR_CYAN = COLOR_WHITE = null;
	
	/* Free the Xt error handler */
	int xtContext = OS.XtDisplayToApplicationContext (xDisplay);
	OS.XtAppSetErrorHandler (xtContext, xtErrorProc);
	xtErrorCallback.dispose (); xtErrorCallback = null;
	xtNullErrorProc = xtErrorProc = 0;
	
	/* Free the Xt Warning handler */
	OS.XtAppSetWarningHandler (xtContext, xtWarningProc);
	xtWarningCallback.dispose (); xtWarningCallback = null;
	xtNullWarningProc = xtWarningProc = 0;
	
	int count = 0;
	for (int i = 0; i < Devices.length; i++){
		if (Devices [i] != null) count++;
	}
	if (count == 1) {
		/* Free the X IO error handler */
		OS.XSetIOErrorHandler (XIOErrorProc);
		XIOErrorCallback.dispose (); XIOErrorCallback = null;
		XNullIOErrorProc = XIOErrorProc = 0;
		
		/* Free the X error handler */
		/*
		* Bug in Motif.  For some reason, when a pixmap is
		* set into a button or label, despite the fact that
		* the pixmap is cleared from the widget before it
		* is disposed, Motif still references the pixmap
		* and attempts to dispose it in XtDestroyApplicationContext().
		* The fix is to avoid warnings by leaving our handler
		* and settings warnings to false.
		*
		* NOTE: The warning callback is leaked.
		*/
		warnings = false;
//		OS.XSetErrorHandler (XErrorProc);
//		XErrorCallback.dispose (); XErrorCallback = null;
//		XNullErrorProc = XErrorProc = 0;
	}
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
	_setWarnings(warnings);
}

void _setWarnings (boolean warnings) {
	this.warnings = warnings;
}

static int XErrorProc (int xDisplay, int xErrorEvent) {
	Device device = findDevice (xDisplay);
	if (device != null) {
		if (device.warnings) {
			if (DEBUG || device.debug) {
				new SWTError ().printStackTrace ();
			}
			OS.Call (XErrorProc, xDisplay, xErrorEvent);
		}
	} else {
		if (DEBUG) new SWTError ().printStackTrace ();
		OS.Call (XErrorProc, xDisplay, xErrorEvent);
	}
	return 0;
}

static int XIOErrorProc (int xDisplay) {
	Device device = findDevice (xDisplay);
	if (device != null) {
		if (DEBUG || device.debug) {
			new SWTError ().printStackTrace ();
		}
	} else {
		if (DEBUG) new SWTError ().printStackTrace ();
	}
	OS.Call (XIOErrorProc, xDisplay, 0);
	return 0;
}

int xtErrorProc (int message) {
	if (DEBUG || debug) {
		new SWTError ().printStackTrace ();
	}
	OS.Call (xtErrorProc, message, 0);
	return 0;
}

int xtWarningProc (int message) {
	if (warnings) {
		if (DEBUG || debug) {
			new SWTError ().printStackTrace ();
		}
		OS.Call (xtWarningProc, message, 0);
	}	
	return 0;
}

}
