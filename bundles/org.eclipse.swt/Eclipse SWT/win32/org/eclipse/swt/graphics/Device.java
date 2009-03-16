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

 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gdip.*;
import org.eclipse.swt.internal.win32.*;
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
	
	/**
	 * Palette 
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public int /*long*/ hPalette = 0;
	int [] colorRefCount;
	
	/* System Font */
	Font systemFont;

	/* Font Enumeration */
	int nFonts = 256;
	LOGFONT [] logFonts;
	TEXTMETRIC metrics;
	int[] pixels;

	/* Scripts */
	int /*long*/ [] scripts;

	/* Advanced Graphics */
	int /*long*/ [] gdipToken;
	int /*long*/ fontCollection;
	String[] loadedFonts;

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

void addFont (String font) {
	if (loadedFonts == null) loadedFonts = new String [4];
	int length = loadedFonts.length;
	for (int i=0; i<length; i++) {
		if (font.equals(loadedFonts [i])) return;
	}
	int index = 0;
	while (index < length) {
		if (loadedFonts [index] == null) break;
		index++;
	}
	if (index == length) {
		String [] temp = new String [length + 4];
		System.arraycopy (loadedFonts, 0, temp, 0, length);
		loadedFonts = temp;
	}
	loadedFonts [index] = font;
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

void checkGDIP() {
	if (gdipToken != null) return;
    int oldErrorMode = 0;
    if (!OS.IsWinCE) oldErrorMode = OS.SetErrorMode (OS.SEM_FAILCRITICALERRORS);
	try {
		int /*long*/ [] token = new int /*long*/ [1];
		GdiplusStartupInput input = new GdiplusStartupInput ();
		input.GdiplusVersion = 1;
		if (Gdip.GdiplusStartup (token, input, 0) == 0) {
			gdipToken = token;
			if (loadedFonts != null) {
				fontCollection = Gdip.PrivateFontCollection_new();
				if (fontCollection == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				for (int i = 0; i < loadedFonts.length; i++) {
					String path = loadedFonts[i];
					if (path == null) break;
					int length = path.length();
					char [] buffer = new char [length + 1];
					path.getChars(0, length, buffer, 0);
					Gdip.PrivateFontCollection_AddFontFile(fontCollection, buffer);
				}
				loadedFonts = null;
			}
		}
	} catch (Throwable t) {
		SWT.error (SWT.ERROR_NO_GRAPHICS_LIBRARY, t, " [GDI+ is required]"); //$NON-NLS-1$
	} finally {
		if (!OS.IsWinCE) OS.SetErrorMode (oldErrorMode);
    }
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

int computePixels(float height) {
	int /*long*/ hDC = internal_new_GC (null);
	int pixels = -(int)(0.5f + (height * OS.GetDeviceCaps(hDC, OS.LOGPIXELSY) / 72f));
	internal_dispose_GC (hDC, null);
	return pixels;
}

float computePoints(LOGFONT logFont, int /*long*/ hFont) {
	int /*long*/ hDC = internal_new_GC (null);
	int logPixelsY = OS.GetDeviceCaps(hDC, OS.LOGPIXELSY);
	int pixels = 0; 
	if (logFont.lfHeight > 0) {
		/*
		 * Feature in Windows. If the lfHeight of the LOGFONT structure
		 * is positive, the lfHeight measures the height of the entire
		 * cell, including internal leading, in logical units. Since the
		 * height of a font in points does not include the internal leading,
		 * we must subtract the internal leading, which requires a TEXTMETRIC.
		 */
		int /*long*/ oldFont = OS.SelectObject(hDC, hFont);
		TEXTMETRIC lptm = OS.IsUnicode ? (TEXTMETRIC)new TEXTMETRICW() : new TEXTMETRICA();
		OS.GetTextMetrics(hDC, lptm);
		OS.SelectObject(hDC, oldFont);
		pixels = logFont.lfHeight - lptm.tmInternalLeading;
	} else {
		pixels = -logFont.lfHeight;
	}
	internal_dispose_GC (hDC, null);
	return pixels * 72f / logPixelsY;
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
				printErrors ();
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

int /*long*/ EnumFontFamProc (int /*long*/ lpelfe, int /*long*/ lpntme, int /*long*/ FontType, int /*long*/ lParam) {
	boolean isScalable = ((int)/*64*/FontType & OS.RASTER_FONTTYPE) == 0;
	boolean scalable = lParam == 1;
	if (isScalable == scalable) {
		/* Add the log font to the list of log fonts */
		if (nFonts == logFonts.length) {
			LOGFONT [] newLogFonts = new LOGFONT [logFonts.length + 128];
			System.arraycopy (logFonts, 0, newLogFonts, 0, nFonts);
			logFonts = newLogFonts;
			int[] newPixels = new int[newLogFonts.length];
			System.arraycopy (pixels, 0, newPixels, 0, nFonts);
			pixels = newPixels;
		}
		LOGFONT logFont = logFonts [nFonts];
		if (logFont == null) logFont = OS.IsUnicode ? (LOGFONT)new LOGFONTW () : new LOGFONTA ();
		OS.MoveMemory (logFont, lpelfe, LOGFONT.sizeof);
		logFonts [nFonts] = logFont;
		if (logFont.lfHeight > 0) {
			/*
			 * Feature in Windows. If the lfHeight of the LOGFONT structure
			 * is positive, the lfHeight measures the height of the entire
			 * cell, including internal leading, in logical units. Since the
			 * height of a font in points does not include the internal leading,
			 * we must subtract the internal leading, which requires a TEXTMETRIC,
			 * which in turn requires font creation.
			 */
			OS.MoveMemory(metrics, lpntme, TEXTMETRIC.sizeof);
			pixels[nFonts] = logFont.lfHeight - metrics.tmInternalLeading;
		} else {
			pixels[nFonts] = -logFont.lfHeight;
		}
		nFonts++;
	}
	return 1;
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
	int /*long*/ hDC = internal_new_GC (null);
	int width = OS.GetDeviceCaps (hDC, OS.HORZRES);
	int height = OS.GetDeviceCaps (hDC, OS.VERTRES);
	internal_dispose_GC (hDC, null);
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
	int /*long*/ hDC = internal_new_GC (null);
	int bits = OS.GetDeviceCaps (hDC, OS.BITSPIXEL);
	int planes = OS.GetDeviceCaps (hDC, OS.PLANES);
	internal_dispose_GC (hDC, null);
	return bits * planes;
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
	int /*long*/ hDC = internal_new_GC (null);
	int dpiX = OS.GetDeviceCaps (hDC, OS.LOGPIXELSX);
	int dpiY = OS.GetDeviceCaps (hDC, OS.LOGPIXELSY);
	internal_dispose_GC (hDC, null);
	return new Point (dpiX, dpiY);
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
	
	/* Create the callback */
	Callback callback = new Callback (this, "EnumFontFamProc", 4); //$NON-NLS-1$
	int /*long*/ lpEnumFontFamProc = callback.getAddress ();
	if (lpEnumFontFamProc == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	
	/* Initialize the instance variables */
	metrics = OS.IsUnicode ? (TEXTMETRIC)new TEXTMETRICW() : new TEXTMETRICA();
	pixels = new int[nFonts];
	logFonts = new LOGFONT [nFonts];
	for (int i=0; i<logFonts.length; i++) {
		logFonts [i] = OS.IsUnicode ? (LOGFONT) new LOGFONTW () : new LOGFONTA ();
	}
	nFonts = 0;

	/* Enumerate */
	int offset = 0;
	int /*long*/ hDC = internal_new_GC (null);
	if (faceName == null) {	
		/* The user did not specify a face name, so they want all versions of all available face names */
		OS.EnumFontFamilies (hDC, null, lpEnumFontFamProc, scalable ? 1 : 0);
		
		/**
		 * For bitmapped fonts, EnumFontFamilies only enumerates once for each font, regardless
		 * of how many styles are available. If the user wants bitmapped fonts, enumerate on
		 * each face name now.
		 */
		offset = nFonts;
		for (int i=0; i<offset; i++) {
			LOGFONT lf = logFonts [i];
			/**
			 * Bug in Windows 98. When EnumFontFamiliesEx is called with a specified face name, it
			 * should enumerate for each available style of that font. Instead, it only enumerates
			 * once. The fix is to call EnumFontFamilies, which works as expected.
			 */
			if (OS.IsUnicode) {
				OS.EnumFontFamiliesW (hDC, ((LOGFONTW)lf).lfFaceName, lpEnumFontFamProc, scalable ? 1 : 0);
			} else {
				OS.EnumFontFamiliesA (hDC, ((LOGFONTA)lf).lfFaceName, lpEnumFontFamProc, scalable ? 1 : 0);
			}
		}
	} else {
		/* Use the character encoding for the default locale */
		TCHAR lpFaceName = new TCHAR (0, faceName, true);
		/**
		 * Bug in Windows 98. When EnumFontFamiliesEx is called with a specified face name, it
		 * should enumerate for each available style of that font. Instead, it only enumerates
		 * once. The fix is to call EnumFontFamilies, which works as expected.
		 */
		OS.EnumFontFamilies (hDC, lpFaceName, lpEnumFontFamProc, scalable ? 1 : 0);
	}
	int logPixelsY = OS.GetDeviceCaps(hDC, OS.LOGPIXELSY);
	internal_dispose_GC (hDC, null);

	/* Create the fontData from the logfonts */
	int count = 0;
	FontData [] result = new FontData [nFonts - offset];
	for (int i=offset; i<nFonts; i++) {
		FontData fd = FontData.win32_new (logFonts [i], pixels [i] * 72f / logPixelsY);
		int j;
		for (j = 0; j < count; j++) {
			if (fd.equals (result [j])) break;
		}
		if (j == count) result [count++] = fd;
	}
	if (count != result.length) {
		FontData [] newResult = new FontData [count];
		System.arraycopy (result, 0, newResult, 0, count);
		result = newResult;
	}
	
	/* Clean up */
	callback.dispose ();
	logFonts = null;
	pixels = null;
	metrics = null;
	return result;
}

String getLastError () {
	int error = OS.GetLastError();
	if (error == 0) return ""; //$NON-NLS-1$
	return " [GetLastError=0x" + Integer.toHexString(error) + "]"; //$NON-NLS-1$ //$NON-NLS-2$
}

String getLastErrorText () {
	int error = OS.GetLastError();
	if (error == 0) return ""; //$NON-NLS-1$
	int /*long*/ [] buffer = new int /*long*/ [1];
	int dwFlags = OS.FORMAT_MESSAGE_ALLOCATE_BUFFER | OS.FORMAT_MESSAGE_FROM_SYSTEM | OS.FORMAT_MESSAGE_IGNORE_INSERTS;
	int length = OS.FormatMessage(dwFlags, 0, error, OS.LANG_USER_DEFAULT, buffer, 0, 0);
	if (length == 0) return " [GetLastError=0x" + Integer.toHexString(error) + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	TCHAR buffer1 = new TCHAR(0, length);
	OS.MoveMemory(buffer1, buffer[0], length * TCHAR.sizeof);
	if (buffer[0] != 0) OS.LocalFree(buffer[0]);
	return buffer1.toString(0, length);
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
	int pixel = 0x00000000;
	switch (id) {
		case SWT.COLOR_WHITE:				pixel = 0x00FFFFFF;  break;
		case SWT.COLOR_BLACK:				pixel = 0x00000000;  break;
		case SWT.COLOR_RED:					pixel = 0x000000FF;  break;
		case SWT.COLOR_DARK_RED:			pixel = 0x00000080;  break;
		case SWT.COLOR_GREEN:				pixel = 0x0000FF00;  break;
		case SWT.COLOR_DARK_GREEN:			pixel = 0x00008000;  break;
		case SWT.COLOR_YELLOW:				pixel = 0x0000FFFF;  break;
		case SWT.COLOR_DARK_YELLOW:			pixel = 0x00008080;  break;
		case SWT.COLOR_BLUE:				pixel = 0x00FF0000;  break;
		case SWT.COLOR_DARK_BLUE:			pixel = 0x00800000;  break;
		case SWT.COLOR_MAGENTA:				pixel = 0x00FF00FF;  break;
		case SWT.COLOR_DARK_MAGENTA:		pixel = 0x00800080;  break;
		case SWT.COLOR_CYAN:				pixel = 0x00FFFF00;  break;
		case SWT.COLOR_DARK_CYAN:			pixel = 0x00808000;  break;
		case SWT.COLOR_GRAY:				pixel = 0x00C0C0C0;  break;
		case SWT.COLOR_DARK_GRAY:			pixel = 0x00808080;  break;
	}
	return Color.win32_new (this, pixel);
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
	int /*long*/ hFont = OS.GetStockObject (OS.SYSTEM_FONT);
	return Font.win32_new (this, hFont);
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
	if (debug) {
		if (!OS.IsWinCE) OS.GdiSetBatchLimit(1);
	}

	/* Initialize the system font slot */
	systemFont = getSystemFont();

	/* Initialize scripts list */
	if (!OS.IsWinCE) {
		int /*long*/ [] ppSp = new int /*long*/ [1];
		int [] piNumScripts = new int [1];
		OS.ScriptGetProperties (ppSp, piNumScripts);
		scripts = new int /*long*/ [piNumScripts [0]];
		OS.MoveMemory (scripts, ppSp [0], scripts.length * OS.PTR_SIZEOF);
	}
	
	/*
	 * If we're not on a device which supports palettes,
	 * don't create one.
	 */
	int /*long*/ hDC = internal_new_GC (null);
	int rc = OS.GetDeviceCaps (hDC, OS.RASTERCAPS);
	int bits = OS.GetDeviceCaps (hDC, OS.BITSPIXEL);
	int planes = OS.GetDeviceCaps (hDC, OS.PLANES);
	
	bits *= planes;
	if ((rc & OS.RC_PALETTE) == 0 || bits != 8) {
		internal_dispose_GC (hDC, null);
		return;
	}
	
	int numReserved = OS.GetDeviceCaps (hDC, OS.NUMRESERVED);
	int numEntries = OS.GetDeviceCaps (hDC, OS.SIZEPALETTE);

	if (OS.IsWinCE) {
		/*
		* Feature on WinCE.  For some reason, certain 8 bit WinCE
		* devices return 0 for the number of reserved entries in
		* the system palette.  Their system palette correctly contains
		* the usual 20 system colors.  The workaround is to assume
		* there are 20 reserved system colors instead of 0.
		*/
		if (numReserved == 0 && numEntries >= 20) numReserved = 20;
	}

	/* Create the palette and reference counter */
	colorRefCount = new int [numEntries];

	/* 4 bytes header + 4 bytes per entry * numEntries entries */
	byte [] logPalette = new byte [4 + 4 * numEntries];
	
	/* 2 bytes = special header */
	logPalette [0] = 0x00;
	logPalette [1] = 0x03;
	
	/* 2 bytes = number of colors, LSB first */
	logPalette [2] = 0;
	logPalette [3] = 1;

	/* 
	* Create a palette which contains the system entries
	* as they are located in the system palette.  The
	* MSDN article 'Memory Device Contexts' describes
	* where system entries are located.  On an 8 bit
	* display with 20 reserved colors, the system colors
	* will be the first 10 entries and the last 10 ones.
	*/
	byte[] lppe = new byte [4 * numEntries];
	OS.GetSystemPaletteEntries (hDC, 0, numEntries, lppe);
	/* Copy all entries from the system palette */
	System.arraycopy (lppe, 0, logPalette, 4, 4 * numEntries);
	/* Lock the indices corresponding to the system entries */
	for (int i = 0; i < numReserved / 2; i++) {
		colorRefCount [i] = 1;
		colorRefCount [numEntries - 1 - i] = 1;
	}
	internal_dispose_GC (hDC, null);
	hPalette = OS.CreatePalette (logPalette);
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
public abstract int /*long*/ internal_new_GC (GCData data);

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
public abstract void /*long*/ internal_dispose_GC (int /*long*/ hDC, GCData data);

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
	if (OS.IsWinNT && OS.WIN32_VERSION >= OS.VERSION (4, 10)) {
		TCHAR lpszFilename = new TCHAR (0, path, true);
		boolean loaded = OS.AddFontResourceEx (lpszFilename, OS.FR_PRIVATE, 0) != 0;
		if (loaded) {
			if (gdipToken != null) {
				if (fontCollection == 0) {
					fontCollection = Gdip.PrivateFontCollection_new();
					if (fontCollection == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				}
				int length = path.length();
				char [] buffer = new char [length + 1];
				path.getChars(0, length, buffer, 0);
				Gdip.PrivateFontCollection_AddFontFile(fontCollection, buffer);
			} else {
				addFont(path);
			}
		}
		return loaded;
	}
	return false;
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
					System.err.println (string);
				}
				for (int i=0; i<errors.length; i++) {
					if (errors [i] != null) errors [i].printStackTrace (System.err);
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
 * </p><p>
 * If subclasses reimplement this method, they must
 * call the <code>super</code> implementation.
 * </p>
 *
 * @see #dispose
 * @see #destroy
 */
protected void release () {
	if (gdipToken != null) {
		if (fontCollection != 0) {
			Gdip.PrivateFontCollection_delete(fontCollection);
		}
		fontCollection = 0;
		Gdip.GdiplusShutdown (gdipToken[0]);
	}
	gdipToken = null;
	scripts = null;
	if (hPalette != 0) OS.DeleteObject (hPalette);
	hPalette = 0;
	colorRefCount = null;
	logFonts = null;
	nFonts = 0;
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
