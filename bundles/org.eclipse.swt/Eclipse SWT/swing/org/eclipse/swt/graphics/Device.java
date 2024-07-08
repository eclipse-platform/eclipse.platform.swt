/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

 
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.internal.swing.CGC;
import org.eclipse.swt.internal.swing.LookAndFeelUtils;

/**
 * This class is the abstract superclass of all device objects,
 * such as the Display device and the Printer device. Devices
 * can have a graphics context (GC) created for them, and they
 * can be drawn on by sending messages to the associated GC.
 */
public abstract class Device implements Drawable {
	
	/* Debugging */
	public static boolean DEBUG;
	boolean debug = DEBUG;
	boolean tracking = DEBUG;
	Error [] errors;
	Object [] objects;
	
//	/**
//	 * Palette 
//	 * (Warning: This field is platform dependent)
//	 * <p>
//	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
//	 * public API. It is marked public only so that it can be shared
//	 * within the packages provided by SWT. It is not available on all
//	 * platforms and should never be accessed from application code.
//	 * </p>
//	 */
//	public int hPalette = 0;
//	int [] colorRefCount;
	
//	/* Font Enumeration */
//	int nFonts = 256;
//	LOGFONT [] logFonts;

//	/* Scripts */
//	int [] scripts;

//	/* Advanced Graphics */
//	int [] gdipToken;

	boolean disposed;
	
  final static Object CREATE_LOCK = new Object();

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
      Class.forName ("org.eclipse.swt.widgets.Display"); //$NON-NLS-1$
		} catch (Throwable e) {}
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
  synchronized (CREATE_LOCK) {
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

//void checkGDIP() {
//	if (gdipToken != null) return;
//    int oldErrorMode = OS.SetErrorMode (OS.SEM_FAILCRITICALERRORS);
//	try {
//		int [] token = new int [1];
//		GdiplusStartupInput input = new GdiplusStartupInput ();
//		input.GdiplusVersion = 1;
//		if (Gdip.GdiplusStartup (token, input, 0) == 0) {
//			gdipToken = token;
//		}
//	} catch (Throwable t) {
//		SWT.error (SWT.ERROR_NO_GRAPHICS_LIBRARY, t);
//	} finally {
//        OS.SetErrorMode (oldErrorMode);
//    }
//}

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

//int computePixels(int height) {
//	int hDC = internal_new_GC (null);
//	int pixels = -Compatibility.round(height * OS.GetDeviceCaps(hDC, OS.LOGPIXELSY), 72);
//	internal_dispose_GC (hDC, null);
//	return pixels;
//}
//
//int computePoints(LOGFONT logFont) {
//	int hDC = internal_new_GC (null);
//	int logPixelsY = OS.GetDeviceCaps(hDC, OS.LOGPIXELSY);
//	int pixels = 0; 
//	if (logFont.lfHeight > 0) {
//		/*
//		 * Feature in Windows. If the lfHeight of the LOGFONT structure
//		 * is positive, the lfHeight measures the height of the entire
//		 * cell, including internal leading, in logical units. Since the
//		 * height of a font in points does not include the internal leading,
//		 * we must subtract the internal leading, which requires a TEXTMETRIC,
//		 * which in turn requires font creation.
//		 */
//		int hFont = OS.CreateFontIndirect(logFont);
//		int oldFont = OS.SelectObject(hDC, hFont);
//		TEXTMETRIC lptm = OS.IsUnicode ? (TEXTMETRIC)new TEXTMETRICW() : new TEXTMETRICA();
//		OS.GetTextMetrics(hDC, lptm);
//		OS.SelectObject(hDC, oldFont);
//		OS.DeleteObject(hFont);
//		pixels = logFont.lfHeight - lptm.tmInternalLeading;
//	} else {
//		pixels = -logFont.lfHeight;
//	}
//	internal_dispose_GC (hDC, null);
//
//	return Compatibility.round(pixels * 72, logPixelsY);
//}

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

//int EnumFontFamProc (int lpelfe, int lpntme, int FontType, int lParam) {
//	boolean isScalable = (FontType & OS.RASTER_FONTTYPE) == 0;
//	boolean scalable = lParam == 1;
//	if (isScalable == scalable) {
//		/* Add the log font to the list of log fonts */
//		if (nFonts == logFonts.length) {
//			LOGFONT [] newLogFonts = new LOGFONT [logFonts.length + 128];
//			System.arraycopy (logFonts, 0, newLogFonts, 0, nFonts);
//			logFonts = newLogFonts;
//		}
//		LOGFONT logFont = logFonts [nFonts];
//		if (logFont == null) logFont = OS.IsUnicode ? (LOGFONT)new LOGFONTW () : new LOGFONTA ();
//		OS.MoveMemory (logFont, lpelfe, LOGFONT.sizeof);
//		logFonts [nFonts++] = logFont;
//	}
//	return 1;
//}

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
  //TODO: implement method
  throw new IllegalStateException("Not implemented!");
//	int hDC = internal_new_GC (null);
//	int width = OS.GetDeviceCaps (hDC, OS.HORZRES);
//	int height = OS.GetDeviceCaps (hDC, OS.VERTRES);
//	internal_dispose_GC (hDC, null);
//	return new Rectangle (0, 0, width, height);
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
//	int hDC = internal_new_GC (null);
//	int bits = OS.GetDeviceCaps (hDC, OS.BITSPIXEL);
//	int planes = OS.GetDeviceCaps (hDC, OS.PLANES);
//	internal_dispose_GC (hDC, null);
//	return bits * planes;
  // TODO: is this correct?
  DisplayMode displayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
  if(displayMode == null) {
    return 32;
  }
  int bitDepth = displayMode.getBitDepth();
  return bitDepth == DisplayMode.BIT_DEPTH_MULTI? 32: bitDepth;
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
	return getDPI_();
//	int hDC = internal_new_GC (null);
//	int dpiX = OS.GetDeviceCaps (hDC, OS.LOGPIXELSX);
//	int dpiY = OS.GetDeviceCaps (hDC, OS.LOGPIXELSY);
//	internal_dispose_GC (hDC, null);
//	return new Point (dpiX, dpiY);
}

protected Point getDPI_() {
  int resolution = Toolkit.getDefaultToolkit().getScreenResolution();
  return new Point(resolution, resolution);
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
  if(!scalable) {
    return new FontData[0];
  }
  // TODO: propose all styles?
  java.awt.Font[] fonts;
  if(faceName != null) {
    java.awt.Font font = new java.awt.Font(faceName, java.awt.Font.PLAIN, 1);
    if(!"Dialog".equals(faceName) && "Dialog".equals(font.getFamily())) {
      fonts = new java.awt.Font[0];
    } else {
      fonts = new java.awt.Font[] {font};
    }
  } else {
    fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
  }
  FontData[] results = new FontData[fonts.length];
  for(int i=0; i<fonts.length; i++) {
    java.awt.Font font = fonts[i];
    results[i] = new FontData(font.getFamily(), 12, SWT.NORMAL | (font.isBold()? SWT.BOLD: 0) | (font.isItalic()? SWT.ITALIC: 0));
  }
  return results;
//  
//	/* Create the callback */
//	Callback callback = new Callback (this, "EnumFontFamProc", 4);
//	int lpEnumFontFamProc = callback.getAddress ();
//		
//	/* Initialize the instance variables */
//	logFonts = new LOGFONT [nFonts];
//	for (int i=0; i<logFonts.length; i++) {
//		logFonts [i] = OS.IsUnicode ? (LOGFONT) new LOGFONTW () : new LOGFONTA ();
//	}
//	nFonts = 0;
//
//	/* Enumerate */
//	int offset = 0;
//	int hDC = internal_new_GC (null);
//	if (faceName == null) {	
//		/* The user did not specify a face name, so they want all versions of all available face names */
//		OS.EnumFontFamilies (hDC, null, lpEnumFontFamProc, scalable ? 1 : 0);
//		
//		/**
//		 * For bitmapped fonts, EnumFontFamilies only enumerates once for each font, regardless
//		 * of how many styles are available. If the user wants bitmapped fonts, enumerate on
//		 * each face name now.
//		 */
//		offset = nFonts;
//		for (int i=0; i<offset; i++) {
//			LOGFONT lf = logFonts [i];
//			/**
//			 * Bug in Windows 98. When EnumFontFamiliesEx is called with a specified face name, it
//			 * should enumerate for each available style of that font. Instead, it only enumerates
//			 * once. The fix is to call EnumFontFamilies, which works as expected.
//			 */
//			if (OS.IsUnicode) {
//				OS.EnumFontFamiliesW (hDC, ((LOGFONTW)lf).lfFaceName, lpEnumFontFamProc, scalable ? 1 : 0);
//			} else {
//				OS.EnumFontFamiliesA (hDC, ((LOGFONTA)lf).lfFaceName, lpEnumFontFamProc, scalable ? 1 : 0);
//			}
//		}
//	} else {
//		/* Use the character encoding for the default locale */
//		TCHAR lpFaceName = new TCHAR (0, faceName, true);
//		/**
//		 * Bug in Windows 98. When EnumFontFamiliesEx is called with a specified face name, it
//		 * should enumerate for each available style of that font. Instead, it only enumerates
//		 * once. The fix is to call EnumFontFamilies, which works as expected.
//		 */
//		OS.EnumFontFamilies (hDC, lpFaceName, lpEnumFontFamProc, scalable ? 1 : 0);
//	}
//	internal_dispose_GC (hDC, null);
//
//	/* Create the fontData from the logfonts */
//	int count = nFonts - offset;
//	FontData [] result = new FontData [count];
//	for (int i=0; i<count; i++) {
//		LOGFONT logFont = logFonts [i+offset];
//		result [i] = FontData.win32_new (logFont, computePoints(logFont));
//	}
//	
//	/* Clean up */
//	callback.dispose ();
//	logFonts = null;
//	return result;
}

//String getLastError () {
//	int error = OS.GetLastError();
//	if (error == 0) return ""; 
//	return " [GetLastError=0x" + Integer.toHexString(error) + "]";
//}
//
//String getLastErrorText () {
//	int error = OS.GetLastError();
//	if (error == 0) return ""; 
//	int[] buffer = new int[1];
//	int dwFlags = OS.FORMAT_MESSAGE_ALLOCATE_BUFFER | OS.FORMAT_MESSAGE_FROM_SYSTEM | OS.FORMAT_MESSAGE_IGNORE_INSERTS;
//	int length = OS.FormatMessage(dwFlags, 0, error, OS.LANG_USER_DEFAULT, buffer, 0, 0);
//	if (length == 0) return " [GetLastError=0x" + Integer.toHexString(error) + "]";
//	TCHAR buffer1 = new TCHAR(0, length);
//	OS.MoveMemory(buffer1, buffer[0], length * TCHAR.sizeof);
//	if (buffer[0] != 0) OS.LocalFree(buffer[0]);
//	return buffer1.toString(0, length);
//}

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
	java.awt.Color pixel = java.awt.Color.BLACK;
	switch (id) {
		case SWT.COLOR_WHITE:				pixel = java.awt.Color.WHITE;  break;
		case SWT.COLOR_BLACK:				pixel = java.awt.Color.BLACK;  break;
		case SWT.COLOR_RED:					pixel = java.awt.Color.RED;  break;
		case SWT.COLOR_DARK_RED:			pixel = java.awt.Color.RED.darker();  break;
		case SWT.COLOR_GREEN:				pixel = java.awt.Color.GREEN;  break;
		case SWT.COLOR_DARK_GREEN:			pixel = java.awt.Color.GREEN.darker();  break;
		case SWT.COLOR_YELLOW:				pixel = java.awt.Color.YELLOW;  break;
		case SWT.COLOR_DARK_YELLOW:			pixel = java.awt.Color.YELLOW.darker();  break;
		case SWT.COLOR_BLUE:				pixel = java.awt.Color.BLUE;  break;
		case SWT.COLOR_DARK_BLUE:			pixel = java.awt.Color.BLUE.darker();  break;
		case SWT.COLOR_MAGENTA:				pixel = java.awt.Color.MAGENTA;  break;
		case SWT.COLOR_DARK_MAGENTA:		pixel = java.awt.Color.MAGENTA.darker();  break;
		case SWT.COLOR_CYAN:				pixel = java.awt.Color.CYAN;  break;
		case SWT.COLOR_DARK_CYAN:			pixel = java.awt.Color.CYAN.darker();  break;
		case SWT.COLOR_GRAY:				pixel = java.awt.Color.GRAY;  break;
		case SWT.COLOR_DARK_GRAY:			pixel = java.awt.Color.GRAY.darker();  break;
	}
	return Color.swing_new (this, pixel);
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
	return Font.swing_new (this, LookAndFeelUtils.getSystemFont());
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
//	if (debug) {
//		if (!OS.IsWinCE) OS.GdiSetBatchLimit(1);
//	}
//
//	/* Initialize scripts list */
//	if (!OS.IsWinCE) {
//		int [] ppSp = new int [1];
//		int [] piNumScripts = new int [1];
//		OS.ScriptGetProperties (ppSp, piNumScripts);
//		scripts = new int [piNumScripts [0]];
//		OS.MoveMemory (scripts, ppSp [0], scripts.length * 4);
//	}
//	
//	/*
//	 * If we're not on a device which supports palettes,
//	 * don't create one.
//	 */
//	int hDC = internal_new_GC (null);
//	int rc = OS.GetDeviceCaps (hDC, OS.RASTERCAPS);
//	int bits = OS.GetDeviceCaps (hDC, OS.BITSPIXEL);
//	int planes = OS.GetDeviceCaps (hDC, OS.PLANES);
//	
//	bits *= planes;
//	if ((rc & OS.RC_PALETTE) == 0 || bits != 8) {
//		internal_dispose_GC (hDC, null);
//		return;
//	}
//	
//	int numReserved = OS.GetDeviceCaps (hDC, OS.NUMRESERVED);
//	int numEntries = OS.GetDeviceCaps (hDC, OS.SIZEPALETTE);
//
//	if (OS.IsWinCE) {
//		/*
//		* Feature on WinCE.  For some reason, certain 8 bit WinCE
//		* devices return 0 for the number of reserved entries in
//		* the system palette.  Their system palette correctly contains
//		* the usual 20 system colors.  The workaround is to assume
//		* there are 20 reserved system colors instead of 0.
//		*/
//		if (numReserved == 0 && numEntries >= 20) numReserved = 20;
//	}
//
//	/* Create the palette and reference counter */
//	colorRefCount = new int [numEntries];
//
//	/* 4 bytes header + 4 bytes per entry * numEntries entries */
//	byte [] logPalette = new byte [4 + 4 * numEntries];
//	
//	/* 2 bytes = special header */
//	logPalette [0] = 0x00;
//	logPalette [1] = 0x03;
//	
//	/* 2 bytes = number of colors, LSB first */
//	logPalette [2] = 0;
//	logPalette [3] = 1;
//
//	/* 
//	* Create a palette which contains the system entries
//	* as they are located in the system palette.  The
//	* MSDN article 'Memory Device Contexts' describes
//	* where system entries are located.  On an 8 bit
//	* display with 20 reserved colors, the system colors
//	* will be the first 10 entries and the last 10 ones.
//	*/
//	byte[] lppe = new byte [4 * numEntries];
//	OS.GetSystemPaletteEntries (hDC, 0, numEntries, lppe);
//	/* Copy all entries from the system palette */
//	System.arraycopy (lppe, 0, logPalette, 4, 4 * numEntries);
//	/* Lock the indices corresponding to the system entries */
//	for (int i = 0; i < numReserved / 2; i++) {
//		colorRefCount [i] = 1;
//		colorRefCount [numEntries - 1 - i] = 1;
//	}
//	internal_dispose_GC (hDC, null);
//	hPalette = OS.CreatePalette (logPalette);
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
public abstract CGC internal_new_GC (GCData data);

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
 */
public abstract void internal_dispose_GC (CGC handle, GCData data);

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
//	if (gdipToken != null) {
//		Gdip.GdiplusShutdown (gdipToken);
//	}
//	gdipToken = null;
//	scripts = null;
//	if (hPalette != 0) OS.DeleteObject (hPalette);
//	hPalette = 0;
//	colorRefCount = null;
//	logFonts = null;
//	nFonts = 0;
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
}

}
