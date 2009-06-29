/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.wpf.*;
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
	public static boolean TRACK;
	boolean debug = DEBUG;
	boolean tracking = DEBUG || TRACK;
	Error [] errors;
	Object [] objects;
	Object trackingLock;
	
	Color[] colors;
	
	/* System Font */
	Font systemFont;

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
	*/
	protected static Device CurrentDevice;
	protected static Runnable DeviceFinder;
	static {
		try {
			Class.forName ("org.eclipse.swt.widgets.Display"); //$NON-NLS-1$
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
	int width = (int) OS.SystemParameters_PrimaryScreenWidth();
	int height = (int) OS.SystemParameters_PrimaryScreenHeight();
	return new Rectangle (0, 0, width, height);
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
	//TODO - implement getDepth
	return 32;
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
	//TODO implement getDPI
	return new Point(96, 96);
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
	if (!scalable) return new FontData[0];
	int typefaces;
	if (faceName != null) {
		int length = faceName.length();
		char[] chars = new char[length + 1];
		faceName.getChars(0, length, chars, 0);
		int str = OS.gcnew_String(chars);
		int fontFamily = OS.gcnew_FontFamily(str);
		typefaces = OS.FontFamily_GetTypefaces(fontFamily);
		OS.GCHandle_Free(fontFamily);
		OS.GCHandle_Free(str);
	} else {
		typefaces = OS.Fonts_SystemTypefaces();
	}
	int count = OS.TypefaceCollection_Count(typefaces);
	int index = 0;
	FontData[] result = new FontData[count];
	int enumerator = OS.TypefaceCollection_GetEnumerator(typefaces);
	while (OS.IEnumerator_MoveNext(enumerator)) {
		int typeface = OS.TypefaceCollection_Current(enumerator);
		int fontFamily = OS.Typeface_FontFamily(typeface);
		int style = OS.Typeface_Style(typeface);
		int weight = OS.Typeface_Weight(typeface);
		int stretch = OS.Typeface_Stretch(typeface);
		int str = OS.FontFamily_Source(fontFamily);
		int charArray = OS.String_ToCharArray(str);
		char[] chars = new char[OS.String_Length(str)];
		OS.memcpy(chars, charArray, chars.length * 2);
		int fontStyle = OS.FontStyles_Normal;
		if (OS.Object_Equals(style, OS.FontStyles_Italic)) fontStyle = OS.FontStyles_Italic;
		if (OS.Object_Equals(style, OS.FontStyles_Oblique)) fontStyle = OS.FontStyles_Oblique;
		FontData data = FontData.wpf_new(new String(chars), fontStyle,
				OS.FontWeight_ToOpenTypeWeight(weight), OS.FontStretch_ToOpenTypeStretch(stretch), 0);
		OS.GCHandle_Free(charArray);
		OS.GCHandle_Free(str);
		OS.GCHandle_Free(fontFamily);
		OS.GCHandle_Free(style);
		OS.GCHandle_Free(weight);
		OS.GCHandle_Free(stretch);
		OS.GCHandle_Free(typeface);
		result[index++] = data;
	}
	OS.GCHandle_Free(enumerator);
	OS.GCHandle_Free(typefaces);
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
	if (0 <= id && id < colors.length) {
		return colors[id];
	}
	return colors[0];
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
	return false;
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
	colors = new Color[SWT.COLOR_DARK_GRAY + 1];
	colors[SWT.COLOR_BLACK] = colors[0] = Color.wpf_new(this, OS.Colors_Black());
	colors[SWT.COLOR_DARK_RED] = Color.wpf_new(this, OS.Colors_Maroon ());
	colors[SWT.COLOR_DARK_GREEN] = Color.wpf_new(this, OS.Colors_Green ());
	colors[SWT.COLOR_DARK_YELLOW] = Color.wpf_new(this, OS.Colors_Olive());
	colors[SWT.COLOR_DARK_BLUE] = Color.wpf_new(this, OS.Colors_Navy ());
	colors[SWT.COLOR_DARK_MAGENTA] = Color.wpf_new(this, OS.Colors_Purple());
	colors[SWT.COLOR_DARK_CYAN] = Color.wpf_new(this, OS.Colors_Teal ());
	colors[SWT.COLOR_GRAY] = Color.wpf_new(this, OS.Colors_Silver ());
	colors[SWT.COLOR_DARK_GRAY] = Color.wpf_new(this, OS.Colors_Silver ());
	colors[SWT.COLOR_RED] = Color.wpf_new(this, OS.Colors_Red ());
	colors[SWT.COLOR_GREEN] = Color.wpf_new(this, OS.Colors_Lime ());
	colors[SWT.COLOR_YELLOW] = Color.wpf_new(this, OS.Colors_Yellow ());
	colors[SWT.COLOR_BLUE] = Color.wpf_new(this, OS.Colors_Blue ());
	colors[SWT.COLOR_MAGENTA] = Color.wpf_new(this, OS.Colors_Magenta ());
	colors[SWT.COLOR_CYAN] = Color.wpf_new(this, OS.Colors_Cyan ());
	colors[SWT.COLOR_WHITE] = Color.wpf_new(this, OS.Colors_White ());

	/* Initialize the system font slot */
	int fontFamily = OS.SystemFonts_MessageFontFamily();
	int style = OS.SystemFonts_MessageFontStyle();
	int weight = OS.SystemFonts_MessageFontWeight();
	double size = OS.SystemFonts_MessageFontSize();
	int typeface = OS.gcnew_Typeface(fontFamily, style, weight, OS.FontStretches_Normal);
	OS.GCHandle_Free(fontFamily);
	OS.GCHandle_Free(style);
	OS.GCHandle_Free(weight);
	systemFont = Font.wpf_new(this, typeface, size);
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
public abstract void internal_dispose_GC (int hDC, GCData data);

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
	if (path == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	int length = path.length ();
	char[] buffer = new char [length + 1];
	path.getChars (0, length, buffer, 0);
	int str = OS.gcnew_String(buffer);
	int uri = OS.gcnew_Uri(str, OS.UriKind_RelativeOrAbsolute);
	int list = OS.Fonts_GetTypefaces(uri);
	int count = OS.TypefaceCollection_Count(list);
	OS.GCHandle_Free(list);
	OS.GCHandle_Free(uri);
	OS.GCHandle_Free(str);
	return count != 0;
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
	for (int i = 0; i < colors.length; i++) {
		if (colors[i] != null) colors[i].dispose();
	}
	colors = null;
	if (systemFont != null) systemFont.dispose();
	systemFont = null;
	if (tracking) {
		synchronized (trackingLock) {
			if (TRACK & objects != null) {
				for (int i = 0; i < objects.length; i++) {
					if (objects[i] != null) {
						System.err.println(objects[i]);
						errors[i].printStackTrace();
					}
				}
			}
		}
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
}

}
