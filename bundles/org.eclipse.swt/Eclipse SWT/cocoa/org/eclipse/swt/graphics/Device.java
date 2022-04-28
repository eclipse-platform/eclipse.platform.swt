/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
import org.eclipse.swt.internal.ExceptionStash;
import org.eclipse.swt.internal.cocoa.*;

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
	volatile boolean disposed;
	boolean warnings;

	Color COLOR_BLACK, COLOR_DARK_RED, COLOR_DARK_GREEN, COLOR_DARK_YELLOW, COLOR_DARK_BLUE;
	Color COLOR_DARK_MAGENTA, COLOR_DARK_CYAN, COLOR_GRAY, COLOR_DARK_GRAY, COLOR_RED, COLOR_TRANSPARENT;
	Color COLOR_GREEN, COLOR_YELLOW, COLOR_BLUE, COLOR_MAGENTA, COLOR_CYAN, COLOR_WHITE;

	/* System Font */
	Font systemFont;

	NSMutableParagraphStyle paragraphStyle;

	/* Device DPI */
	Point dpi;

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
			startTracking();
		}
		if (NSThread.isMainThread()) {
			NSAutoreleasePool pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
			NSThread nsthread = NSThread.currentThread();
			NSMutableDictionary dictionary = nsthread.threadDictionary();
			NSString key = NSString.stringWith("SWT_NSAutoreleasePool");
			id obj = dictionary.objectForKey(key);
			if (obj == null) {
				NSNumber nsnumber = NSNumber.numberWithInteger(pool.id);
				dictionary.setObject(nsnumber, key);
			} else {
				pool.release();
			}
		}
		//check and create pool
		create (data);
		init ();
	}
}

/**
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @since 3.115
 */
public boolean isTracking() {
	checkDevice();
	return tracking;
}

/**
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @since 3.115
 */
public void setTracking(boolean tracking) {
	checkDevice();
	if (tracking == this.tracking) {
		return;
	}
	this.tracking = tracking;
	if (tracking) {
		startTracking();
	} else {
		stopTracking();
	}
}

private void startTracking() {
	errors = new Error [128];
	objects = new Object [128];
	trackingLock = new Object ();
}

private void stopTracking() {
	synchronized (trackingLock) {
		objects = null;
		errors = null;
		trackingLock = null;
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
 * </p>
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
		try (ExceptionStash exceptions = new ExceptionStash ()) {
			if (isDisposed ()) return;
			checkDevice ();

			try {
				release ();
			} catch (Error | RuntimeException ex) {
				exceptions.stash (ex);
			}

			destroy ();
			disposed = true;			
			if (tracking) {
				synchronized (trackingLock) {
					printErrors ();
					objects = null;
					errors = null;
					trackingLock = null;
				}
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
	NSScreen primaryScreen = getPrimaryScreen();
	if (primaryScreen == null) return new Rectangle(0, 0, 0, 0);
	NSRect frame = primaryScreen.frame();
	return new Rectangle((int)frame.x, (int)frame.y, (int)frame.width, (int)frame.height);
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
	NSScreen primaryScreen = getPrimaryScreen();
	if (primaryScreen == null) return 0;
	return (int)OS.NSBitsPerPixelFromDepth(primaryScreen.depth());
}

/**
 * Returns a point whose x coordinate is the logical horizontal
 * dots per inch of the display, and whose y coordinate
 * is the logical vertical dots per inch of the display.
 *
 * @return the horizontal and vertical DPI
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point getDPI () {
	checkDevice ();
	return getScreenDPI();
}

NSScreen getPrimaryScreen () {
	NSArray screens = NSScreen.screens();
	return (screens != null) ? new NSScreen(screens.objectAtIndex(0)) : null;
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
	String systemFontName = systemFont.getFontData()[0].getName();
	boolean systemFontIncluded = false;
	int count = 0;
	NSArray families = NSFontManager.sharedFontManager().availableFontFamilies();
	FontData[] fds = new FontData[100];
	if (families != null) {
		long familyCount = families.count();
		for (int i = 0; i < familyCount; i++) {
			NSString nsFamily = new NSString(families.objectAtIndex(i));
			String name = nsFamily.getString();
			NSArray fonts = NSFontManager.sharedFontManager().availableMembersOfFontFamily(nsFamily);

			if (fonts != null) {
				int fontCount = (int)fonts.count();
				for (int j = 0; j < fontCount; j++) {
					NSArray fontDetails = new NSArray(fonts.objectAtIndex(j));
					String nsName = new NSString(fontDetails.objectAtIndex(0)).getString();
					long weight = new NSNumber(fontDetails.objectAtIndex(2)).integerValue();
					long traits = new NSNumber(fontDetails.objectAtIndex(3)).integerValue();
					int style = SWT.NORMAL;
					if ((traits & OS.NSItalicFontMask) != 0) style |= SWT.ITALIC;
					if (weight == 9) style |= SWT.BOLD;
					if (faceName == null || faceName.equalsIgnoreCase(name)) {
						FontData data = new FontData(name, 0, style);
						data.nsName = nsName;
						if (systemFontName.equalsIgnoreCase(name)) {
							systemFontIncluded = true;
						}
						if (count == fds.length) {
							FontData[] newFds = new FontData[fds.length + 100];
							System.arraycopy(fds, 0, newFds, 0, fds.length);
							fds = newFds;
						}
						fds[count++] = data;
					}
				}
			}
		}
	}
	if (!systemFontIncluded && (faceName == null || faceName.equalsIgnoreCase(systemFontName))) {
		/*
		 * Feature in Mac OS X 10.10: The default system font ".Helvetica Neue DeskInterface"
		 * is not available from the NSFontManager. The fix is to include it manually if necessary.
		 */
		if (count == fds.length) {
			FontData[] newFds = new FontData[fds.length + 1];
			System.arraycopy(fds, 0, newFds, 0, fds.length);
			fds = newFds;
		}
		fds[count++] = systemFont.getFontData()[0];
	}
	if (count == fds.length) return fds;
	FontData[] result = new FontData[count];
	System.arraycopy(fds, 0, result, 0, count);
	return result;
}

Point getScreenDPI () {
	NSScreen screen = getPrimaryScreen();
	if (screen == null) return new Point(0, 0);

	NSDictionary dictionary = screen.deviceDescription();
	NSValue value = new NSValue(dictionary.objectForKey(new id(OS.NSDeviceResolution())).id);
	NSSize size = value.sizeValue();
	double scaling = screen.backingScaleFactor();
	return new Point((int)(size.width / scaling), (int)(size.height / scaling));
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
		case SWT.COLOR_TRANSPARENT: 		return COLOR_TRANSPARENT;
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
	/* Create the standard colors */
	COLOR_TRANSPARENT = new Color (this, 0xFF,0xFF,0xFF,0);
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

	paragraphStyle = (NSMutableParagraphStyle)new NSMutableParagraphStyle().alloc().init();
	paragraphStyle.setAlignment(OS.NSTextAlignmentLeft);
	paragraphStyle.setLineBreakMode(OS.NSLineBreakByClipping);
	NSArray tabs = new NSArray(new NSArray().alloc().init());
	paragraphStyle.setTabStops(tabs);
	tabs.release();

	/* Initialize the system font slot */
	boolean smallFonts = System.getProperty("org.eclipse.swt.internal.carbon.smallFonts") != null;
	double systemFontSize = smallFonts ? NSFont.smallSystemFontSize() : NSFont.systemFontSize();
	Point dpi = this.dpi = getDPI(), screenDPI = getScreenDPI();
	NSFont font = NSFont.systemFontOfSize(systemFontSize * dpi.y / screenDPI.y);
	font.retain();
	systemFont = Font.cocoa_new(this, font);
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
@Override
public abstract long internal_new_GC (GCData data);

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
@Override
public abstract void internal_dispose_GC (long hDC, GCData data);

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
	return disposed;
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
	NSString nsPath = NSString.stringWith(path);
	NSURL nsUrl = NSURL.fileURLWithPath(nsPath);
	if (nsUrl == null) return false;
	return OS.CTFontManagerRegisterFontsForURL(nsUrl.id, OS.kCTFontManagerScopeProcess, 0);
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

void printErrors () {
	if (!DEBUG) return;
	if (tracking) {
		synchronized (trackingLock) {
			if (objects == null || errors == null) return;
			int objectCount = 0;
			int colors = 0, cursors = 0, fonts = 0, gcs = 0, images = 0;
			int paths = 0, patterns = 0, regions = 0, textLayouts = 0, transforms = 0;
			for (int i=0; i<objects.length; i++) {
				Object object = objects [i];
				if (object != null) {
					objectCount++;
					if (object instanceof Color) colors++;
					if (object instanceof Cursor) cursors++;
					if (object instanceof Font) fonts++;
					if (object instanceof GC) gcs++;
					if (object instanceof Image) images++;
					if (object instanceof Path) paths++;
					if (object instanceof Pattern) patterns++;
					if (object instanceof Region) regions++;
					if (object instanceof TextLayout) textLayouts++;
					if (object instanceof Transform) transforms++;
				}
			}
			if (objectCount != 0) {
				String string = "Summary: ";
				if (colors != 0) string += colors + " Color(s), ";
				if (cursors != 0) string += cursors + " Cursor(s), ";
				if (fonts != 0) string += fonts + " Font(s), ";
				if (gcs != 0) string += gcs + " GC(s), ";
				if (images != 0) string += images + " Image(s), ";
				if (paths != 0) string += paths + " Path(s), ";
				if (patterns != 0) string += patterns + " Pattern(s), ";
				if (regions != 0) string += regions + " Region(s), ";
				if (textLayouts != 0) string += textLayouts + " TextLayout(s), ";
				if (transforms != 0) string += transforms + " Transforms(s), ";
				if (string.length () != 0) {
					string = string.substring (0, string.length () - 2);
					System.out.println (string);
				}
				for (int i=0; i<errors.length; i++) {
					if (errors [i] != null) errors [i].printStackTrace (System.out);
				}
			}
		}
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
 * <p>
 * If subclasses reimplement this method, they must
 * call the <code>super</code> implementation.
 * </p>
 *
 * @see #dispose
 * @see #destroy
 */
protected void release () {
	if (paragraphStyle != null) paragraphStyle.release();
	paragraphStyle = null;

	if (systemFont != null) systemFont.dispose();
	systemFont = null;

	COLOR_BLACK = COLOR_DARK_RED = COLOR_DARK_GREEN = COLOR_DARK_YELLOW = COLOR_DARK_BLUE =
	COLOR_DARK_MAGENTA = COLOR_DARK_CYAN = COLOR_GRAY = COLOR_DARK_GRAY = COLOR_RED =
	COLOR_GREEN = COLOR_YELLOW = COLOR_BLUE = COLOR_MAGENTA = COLOR_CYAN = COLOR_WHITE = null;
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

/**
 * Gets the scaling factor from the device and calculates the zoom level.
 * @return zoom in percentage
 *
 * @noreference This method is not intended to be referenced by clients.
 * @nooverride This method is not intended to be re-implemented or extended by clients.
 * @since 3.105
 */
protected int getDeviceZoom () {
	NSScreen mainScreen = NSScreen.mainScreen();
	int scaleFactor = mainScreen != null ? (int) mainScreen.backingScaleFactor() : 1;
	return scaleFactor * 100;
}

}
