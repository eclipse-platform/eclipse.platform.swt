/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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


import java.io.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

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
	 * @noreference This field is not intended to be referenced by clients.
	 * @since 3.105
	 */
	protected static final int CHANGE_SCALEFACTOR = 1;
	/* Settings callbacks */
	long gsettingsProc;
	Callback gsettingsCallback;
	boolean isConnected = false;
	long displaySettings; //gsettings Dictionary

	/**
	 * the handle to the X Display
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked protected only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 *
	 * @noreference This field is not intended to be referenced by clients.
	 */
	protected long xDisplay;
	long shellHandle;

	/* Debugging */
	public static boolean DEBUG;
	boolean debug = DEBUG;
	boolean tracking = DEBUG;
	Error [] errors;
	Object [] objects;
	Object trackingLock;

	/* Disposed flag */
	volatile boolean disposed;

	/* Warning and Error Handlers */
	long logProc;
	Callback logCallback;
	//NOT DONE - get list of valid names
	String [] log_domains = {"", "GLib-GObject", "GLib", "GObject", "Pango", "ATK", "GdkPixbuf", "Gdk", "Gtk", "GnomeVFS", "GIO"};
	int [] handler_ids = new int [log_domains.length];
	int warningLevel;

	/* X Warning and Error Handlers */
	static Callback XErrorCallback, XIOErrorCallback;
	static long XErrorProc, XIOErrorProc, XNullErrorProc, XNullIOErrorProc;
	static Device[] Devices = new Device[4];

	/*
	* The following colors are listed in the Windows
	* Programmer's Reference as the colors in the default
	* palette.
	*/
	Color COLOR_BLACK, COLOR_DARK_RED, COLOR_DARK_GREEN, COLOR_DARK_YELLOW, COLOR_DARK_BLUE;
	Color COLOR_DARK_MAGENTA, COLOR_DARK_CYAN, COLOR_GRAY, COLOR_DARK_GRAY, COLOR_RED, COLOR_TRANSPARENT;
	Color COLOR_GREEN, COLOR_YELLOW, COLOR_BLUE, COLOR_MAGENTA, COLOR_CYAN, COLOR_WHITE;

	/* System Font */
	Font systemFont;

	/* Device dpi */
	Point dpi;

	long emptyTab;

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
		create (data);
		init ();
		register (this);
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
			deregister (this);
			xDisplay = 0;
			disposed = true;
			if (tracking) {
				tracking = false;
				stopTracking ();
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

static synchronized Device findDevice (long xDisplay) {
	for (int i=0; i<Devices.length; i++) {
		Device device = Devices [i];
		if (device != null && device.xDisplay == xDisplay) {
			return device;
		}
	}
	return null;
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
	return DPIUtil.autoScaleDown (getBoundsInPixels ());
}

private Rectangle getBoundsInPixels () {
	return new Rectangle(0, 0, 0, 0);
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
	return 0;
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
	long [] family = new long [1];
	long [] face = new long [1];
	long [] families = new long [1];
	int[] n_families = new int[1];
	long [] faces = new long [1];
	int[] n_faces = new int[1];
	long context;
	if (GTK.GTK4) {
		long fontMap = OS.pango_cairo_font_map_get_default ();
		context = OS.pango_font_map_create_context (fontMap);
	} else {
		context = GDK.gdk_pango_context_get();
	}
	OS.pango_context_list_families(context, families, n_families);
	int nFds = 0;
	FontData[] fds = new FontData[faceName != null ? 4 : n_families[0]];
	for (int i=0; i<n_families[0]; i++) {
		C.memmove(family, families[0] + i * C.PTR_SIZEOF, C.PTR_SIZEOF);
		boolean match = true;
		if (faceName != null) {
			long familyName = OS.pango_font_family_get_name(family[0]);
			int length = C.strlen(familyName);
			byte[] buffer = new byte[length];
			C.memmove(buffer, familyName, length);
			String name = new String(Converter.mbcsToWcs(buffer));
			match = faceName.equalsIgnoreCase(name);
		}
		if (match) {
			OS.pango_font_family_list_faces(family[0], faces, n_faces);
			for (int j=0; j<n_faces[0]; j++) {
				C.memmove(face, faces[0] + j * C.PTR_SIZEOF, C.PTR_SIZEOF);
				long fontDesc = OS.pango_font_face_describe(face[0]);
				Font font = Font.gtk_new(this, fontDesc);
				FontData data = font.getFontData()[0];
				if (nFds == fds.length) {
					FontData[] newFds = new FontData[fds.length + n_families[0]];
					System.arraycopy(fds, 0, newFds, 0, nFds);
					fds = newFds;
				}
				fds[nFds++] = data;
				OS.pango_font_description_free(fontDesc);
			}
			OS.g_free(faces[0]);
			if (faceName != null) break;
		}
	}
	OS.g_free(families[0]);
	OS.g_object_unref(context);
	if (nFds == fds.length) return fds;
	FontData[] result = new FontData[nFds];
	System.arraycopy(fds, 0, result, 0, nFds);
	return result;
}

Point getScreenDPI () {
	Point ptDPI;

	if (GTK.GTK4) {
		ptDPI = new Point (96, 96);
	} else {
		long screen = GDK.gdk_screen_get_default();
		int dpi = (int) GDK.gdk_screen_get_resolution(screen);
		ptDPI = dpi == -1 ? new Point (96, 96) : new Point (dpi, dpi);
	}

	return ptDPI;
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
		case SWT.COLOR_TRANSPARENT:			return COLOR_TRANSPARENT;
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
	return warningLevel == 0;
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
	if (debug) {
		if (xDisplay != 0) {
			/* Create the warning and error callbacks */
			Class<?> clazz = getClass ();
			synchronized (clazz) {
				int index = 0;
				while (index < Devices.length) {
					if (Devices [index] != null) break;
					index++;
				}
				if (index == Devices.length) {
					XErrorCallback = new Callback (clazz, "XErrorProc", 2);
					XNullErrorProc = XErrorCallback.getAddress ();
					XIOErrorCallback = new Callback (clazz, "XIOErrorProc", 1);
					XNullIOErrorProc = XIOErrorCallback.getAddress ();
					XErrorProc = OS.XSetErrorHandler (XNullErrorProc);
					XIOErrorProc = OS.XSetIOErrorHandler (XNullIOErrorProc);
				}
			}
			if (debug) OS.XSynchronize (xDisplay, true);
		}
	}

	/* Create GTK warnings and error callbacks */
	if (xDisplay != 0) {
		logCallback = new Callback (this, "logProc", 4);
		logProc = logCallback.getAddress ();

		/* Set GTK warning and error handlers */
		if (debug) {
			int flags = OS.G_LOG_LEVEL_MASK | OS.G_LOG_FLAG_FATAL | OS.G_LOG_FLAG_RECURSION;
			for (int i=0; i<log_domains.length; i++) {
				byte [] log_domain = Converter.wcsToMbcs (log_domains [i], true);
				handler_ids [i] = OS.g_log_set_handler (log_domain, flags, logProc, 0);
			}
		}
	}

	/* Create the standard colors */
	COLOR_TRANSPARENT = new Color (0xFF, 0xFF,0xFF,0);
	COLOR_BLACK = new Color (0, 0,0);
	COLOR_DARK_RED = new Color (0x80, 0,0);
	COLOR_DARK_GREEN = new Color (0, 0x80,0);
	COLOR_DARK_YELLOW = new Color (0x80, 0x80,0);
	COLOR_DARK_BLUE = new Color (0, 0,0x80);
	COLOR_DARK_MAGENTA = new Color (0x80, 0,0x80);
	COLOR_DARK_CYAN = new Color (0, 0x80,0x80);
	COLOR_GRAY = new Color (0xC0, 0xC0,0xC0);
	COLOR_DARK_GRAY = new Color (0x80, 0x80,0x80);
	COLOR_RED = new Color (0xFF, 0,0);
	COLOR_GREEN = new Color (0, 0xFF,0);
	COLOR_YELLOW = new Color (0xFF, 0xFF,0);
	COLOR_BLUE = new Color (0, 0,0xFF);
	COLOR_MAGENTA = new Color (0xFF, 0,0xFF);
	COLOR_CYAN = new Color (0, 0xFF,0xFF);
	COLOR_WHITE = new Color (0xFF, 0xFF,0xFF);

	emptyTab = OS.pango_tab_array_new(1, false);
	if (emptyTab == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.pango_tab_array_set_tab(emptyTab, 0, OS.PANGO_TAB_LEFT, 1);

	if (GTK.GTK4) {
		shellHandle = GTK4.gtk_window_new();
	} else {
		shellHandle = GTK3.gtk_window_new (GTK.GTK_WINDOW_TOPLEVEL);
	}
	if (shellHandle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	GTK.gtk_widget_realize(shellHandle);

	this.dpi = getDPI();
	DPIUtil.setDeviceZoom (getDeviceZoom ());

	DPIUtil.setUseCairoAutoScale(true);

	/* Initialize the system font slot */
	long [] defaultFontArray = new long [1];
	long defaultFont = 0;
	long context = GTK.gtk_widget_get_style_context (shellHandle);
	if ("ppc64le".equals(System.getProperty("os.arch"))) {
		defaultFont = GTK3.gtk_style_context_get_font (context, GTK.GTK_STATE_FLAG_NORMAL);
	} else {
		if (GTK.GTK4) {
			long[] fontPtr = new long[1];
			long settings = GTK.gtk_settings_get_default ();
			OS.g_object_get (settings, GTK.gtk_style_property_font, fontPtr, 0);
			if (fontPtr[0] != 0) {
				int length = C.strlen(fontPtr[0]);
				if (length != 0) {
					byte[] fontString = new byte [length + 1];
					C.memmove(fontString, fontPtr[0], length);
					OS.g_free(fontPtr[0]);
					defaultFont = OS.pango_font_description_from_string(fontString);
				}
			}
		} else {
			GTK.gtk_style_context_save(context);
			GTK.gtk_style_context_set_state(context, GTK.GTK_STATE_FLAG_NORMAL);
			GTK3.gtk_style_context_get(context, GTK.GTK_STATE_FLAG_NORMAL, GTK.gtk_style_property_font, defaultFontArray, 0);
			GTK.gtk_style_context_restore(context);
			defaultFont = defaultFontArray [0];
		}
	}
	defaultFont = OS.pango_font_description_copy (defaultFont);
	Point dpi = getDPI(), screenDPI = getScreenDPI();
	if (dpi.y != screenDPI.y) {
		int size = OS.pango_font_description_get_size(defaultFont);
		OS.pango_font_description_set_size(defaultFont, size * dpi.y / screenDPI.y);
	}
	systemFont = Font.gtk_new (this, defaultFont);

	overrideThemeValues();
}

/**
 * For functionality & improved looks, we override some CSS theme values with custom values.
 *
 * Note about theme load mechanism:
 * - This method is reached early at start of SWT initialization.
 *   Later, platform.ui will call OS.setDarkThemePreferred(true), which tells Gtk to use dark theme.
 *   This has the implication that the system theme can be 'Adwaita' (light), but later be 'darkened'
 *   by platform.ui. This means that there should not be any color-specific overrides in Adwaita theming
 *   because 'Adwaita' is used for both light and dark theme.
 *
 * Note about light/dark system theme:
 * - If the System theme is Adwaita (light), eclipse can be forced to be dark with setDarkThemePreferred(true).
 *   But if the System theme is Adwaita-dark, eclipse cannot be made 'light'.
 *
 * Note that much of eclipse 'dark theme' is done by platform.ui's CSS engine, not by SWT.
 */
private void overrideThemeValues () {
	long provider = GTK.gtk_css_provider_new();

	BiFunction <String, Boolean, String> load = (path, isResource) -> {
		try  {
			BufferedReader buffer;
			if (isResource) {
				buffer = new BufferedReader(new InputStreamReader(Device.class.getResourceAsStream(path)));
			} else {
				buffer = new BufferedReader(new FileReader(new File(path)));
			}
			return buffer.lines().collect(Collectors.joining("\n"));
		} catch (IOException e) {
			System.err.println("SWT Warning: Failed to load " + (isResource ? "resource: " : "file: ") + path);
			return "";
		}
	};

	StringBuilder combinedCSS = new StringBuilder();

	if (!GTK.GTK4) {
		// Load functional CSS fixes. Such as keyboard functionality for some widgets.
		combinedCSS.append(load.apply("/org/eclipse/swt/internal/gtk/swt_functional_gtk_3_20.css", true));
	}

	// By default, load CSS theme fixes to overcome things such as excessive padding that breaks SWT otherwise.
	// Initially designed for Adwaita light/dark theme, but after investigation other themes (like Ubuntu's Ambiance + dark) seem to benefit from this also.
	// However, a few themes break with these fixes, so we allow them to be turned off by user and allow them to load their own fixes manually instead.
	// To turn on this flag, add the following vm argument:  -Dorg.eclipse.swt.internal.gtk.noThemingFixes
	// Note:
	// - Display.create() may override the theme name. See Display.create() ... OS.getThemeName(..).
	// - These fixes should not contain any color information, otherwise it might break a light/dark variant of the theme.
	//   Color fixes should be put either into the theme itself or via swt user api.
	if (System.getProperty("org.eclipse.swt.internal.gtk.noThemingFixes") == null) {
		combinedCSS.append(load.apply("/org/eclipse/swt/internal/gtk/swt_theming_fixes_gtk_3_20.css", true));
		if (GTK.GTK_VERSION >= OS.VERSION(3, 24, 5)) {
			combinedCSS.append(load.apply("/org/eclipse/swt/internal/gtk/swt_theming_fixes_gtk_3_24_5.css", true));
		}
	}

	// Load CSS from user-defined CSS file.
	String additionalCSSPath = System.getProperty("org.eclipse.swt.internal.gtk.cssFile");
	if (additionalCSSPath != null){
		// Warning:
		// - gtk css syntax changed in 3.20. If you load custom css, it could break things depending on gtk version on system.
		// - Also, a lot of custom css/themes are buggy and may result in additional console warnings.
		combinedCSS.append(load.apply(additionalCSSPath, false));
	}

	if (GTK.GTK4) {
		long display = GDK.gdk_display_get_default();
		if (display == 0 || provider == 0) {
			System.err.println("SWT Warning: Override of theme values failed. Reason: could not acquire display or provider.");
			return;
		}
		GTK4.gtk_style_context_add_provider_for_display (display, provider, GTK.GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);
	} else {
		long screen = GDK.gdk_screen_get_default();
		if (screen == 0 || provider == 0) {
			System.err.println("SWT Warning: Override of theme values failed. Reason: could not acquire screen or provider.");
			return;
		}
		GTK3.gtk_style_context_add_provider_for_screen (screen, provider, GTK.GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);
	}
	if (GTK.GTK4) {
		GTK4.gtk_css_provider_load_from_data (provider, Converter.wcsToMbcs (combinedCSS.toString(), true), -1);
	} else {
		GTK3.gtk_css_provider_load_from_data (provider, Converter.wcsToMbcs (combinedCSS.toString(), true), -1, null);
	}
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
	if (path == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (path, true);
	return OS.FcConfigAppFontAddFile (0, buffer);
}

long logProc (long log_domain, long log_level, long message, long user_data) {
	if (DEBUG) {
		new Error ().printStackTrace ();
	}
	if (warningLevel == 0) {
		if (DEBUG || debug) {
			new Error ().printStackTrace ();
		}
		OS.g_log_default_handler (log_domain, (int)log_level, message, 0);
	}
	return 0;
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
 * <p>
 * If subclasses reimplement this method, they must
 * call the <code>super</code> implementation.
 * </p>
 *
 * @see #dispose
 * @see #destroy
 */
protected void release () {
	if (shellHandle != 0) {
		if (GTK.GTK4) {
			GTK4.gtk_window_destroy(shellHandle);
		} else {
			GTK3.gtk_widget_destroy(shellHandle);
		}
	}
	shellHandle = 0;

	/* Dispose the default font */
	if (systemFont != null) systemFont.dispose ();
	systemFont = null;

	COLOR_BLACK = COLOR_DARK_RED = COLOR_DARK_GREEN = COLOR_DARK_YELLOW = COLOR_DARK_BLUE =
	COLOR_DARK_MAGENTA = COLOR_DARK_CYAN = COLOR_GRAY = COLOR_DARK_GRAY = COLOR_RED =
	COLOR_GREEN = COLOR_YELLOW = COLOR_BLUE = COLOR_MAGENTA = COLOR_CYAN = COLOR_WHITE = null;

	if (emptyTab != 0) OS.pango_tab_array_free(emptyTab);
	emptyTab = 0;

	/* Free the GTK error and warning handler */
	if (xDisplay != 0) {
		for (int i=0; i<handler_ids.length; i++) {
			if (handler_ids [i] != 0) {
				byte [] log_domain = Converter.wcsToMbcs (log_domains [i], true);
				OS.g_log_remove_handler (log_domain, handler_ids [i]);
				handler_ids [i] = 0;
			}
		}
		logCallback.dispose ();  logCallback = null;
		handler_ids = null;  log_domains = null;
		logProc = 0;
	}
	/* Dispose the settings callback */
	if (gsettingsCallback != null) {
		gsettingsCallback.dispose();
		gsettingsCallback = null;
	}
	gsettingsProc = 0;


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
	if (warnings) {
		if (--warningLevel == 0) {
			if (debug) return;
			if (logProc != 0) {
				for (int i=0; i<handler_ids.length; i++) {
					if (handler_ids [i] != 0) {
						byte [] log_domain = Converter.wcsToMbcs (log_domains [i], true);
						OS.g_log_remove_handler (log_domain, handler_ids [i]);
						handler_ids [i] = 0;
					}
				}
			}
		}
	} else {
		if (warningLevel++ == 0) {
			if (debug) return;
			if (logProc != 0) {
				int flags = OS.G_LOG_LEVEL_MASK | OS.G_LOG_FLAG_FATAL | OS.G_LOG_FLAG_RECURSION;
				for (int i=0; i<log_domains.length; i++) {
					byte [] log_domain = Converter.wcsToMbcs (log_domains [i], true);
					handler_ids [i] = OS.g_log_set_handler (log_domain, flags, logProc, 0);
				}
			}
		}
	}
}

static long XErrorProc (long xDisplay, long xErrorEvent) {
	Device device = findDevice (xDisplay);
	if (device != null) {
		if (device.warningLevel == 0) {
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

static long XIOErrorProc (long xDisplay) {
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

/**
 * Gets the scaling factor from the device and calculates the zoom level.
 * @return zoom in percentage
 *
 * @noreference This method is not intended to be referenced by clients.
 * @nooverride This method is not intended to be re-implemented or extended by clients.
 * @since 3.105
 */
protected int getDeviceZoom() {
	/*
	 * We can hard-code 96 as gdk_screen_get_resolution will always return -1
	 * if gdk_screen_set_resolution has not been called.
	 */
	int dpi = 96;
	long display = GDK.gdk_display_get_default();
	long monitor;

	if (GTK.GTK4) {
		long surface = GTK4.gtk_native_get_surface(GTK4.gtk_widget_get_native(shellHandle));
		monitor = GDK.gdk_display_get_monitor_at_surface(display, surface);
	} else {
		monitor = GDK.gdk_display_get_monitor_at_point(display, 0, 0);
	}

	int scale = GDK.gdk_monitor_get_scale_factor(monitor);
	dpi = dpi * scale;

	return DPIUtil.mapDPIToZoom (dpi);
}

}
