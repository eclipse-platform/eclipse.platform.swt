package org.eclipse.swt.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;

public abstract class Device implements Drawable {
	/**
	* the handle to the X Display
	* (Warning: This field is platform dependent)
	*/
	public int xDisplay;
	
	/* Arguments for XtOpenDisplay */
	String display_name;
	String application_name;
	String application_class;
	
	/* Debugging */
	public static boolean DEBUG;
	boolean debug = DEBUG;
	public boolean tracking = DEBUG;
	Error [] errors;
	Object [] objects;
	
	/* Colormap and reference count for this display */
	XColor [] xcolors;
	int [] colorRefCount;
	
	/* System Colors */
	Color COLOR_BLACK, COLOR_DARK_RED, COLOR_DARK_GREEN, COLOR_DARK_YELLOW, COLOR_DARK_BLUE;
	Color COLOR_DARK_MAGENTA, COLOR_DARK_CYAN, COLOR_GRAY, COLOR_DARK_GRAY, COLOR_RED;
	Color COLOR_GREEN, COLOR_YELLOW, COLOR_BLUE, COLOR_MAGENTA, COLOR_CYAN, COLOR_WHITE;

	/* Warning and Error Handlers */
	boolean warnings = true;
	Callback xErrorCallback, xtWarningCallback, xIOErrorCallback, xtErrorCallback;
	int xErrorProc, xtWarningProc, xIOErrorProc, xtErrorProc;
	int xNullErrorProc, xtNullWarningProc, xNullIOErrorProc, xtNullErrorProc;
	
	public static String XDefaultPrintServer = ":1";
	static {
		/* Read the default print server name from
		 * the XPRINTER environment variable.
		 */
		XDefaultPrintServer = ":1";
	}
	protected static int xPrinter;

public Device(DeviceData data) {
	create (data);
	init ();
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
	}
}

/**
 * Temporary code.
 */	
static Device getDevice () {
	Device device = null;
	try {
		Class clazz = Class.forName ("org.eclipse.swt.widgets.Display");
		java.lang.reflect.Method method = clazz.getMethod("getCurrent", new Class[0]);
		device = (Device) method.invoke(clazz, new Object[0]);
		if (device == null) {
			method = clazz.getMethod("getDefault", new Class[0]);
			device = (Device)method.invoke(clazz, new Object[0]);
		}
	} catch (Throwable e) {};
	return device;
}

protected void checkDevice () {
	if (xDisplay == 0) SWT.error (SWT.ERROR_DEVICE_DISPOSED);
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
	xDisplay = 0;
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
	int screen = OS.XDefaultScreen (xDisplay);
	int width = OS.XDisplayWidth (xDisplay, screen);
	int height = OS.XDisplayHeight (xDisplay, screen);
	return new Rectangle (0, 0, width, height);
}

public Rectangle getClientArea () {
	return getBounds ();
}

public int getDepth () {
	checkDevice ();
	int xScreenPtr = OS.XDefaultScreenOfDisplay (xDisplay);
	return OS.XDefaultDepthOfScreen (xScreenPtr);
}

public DeviceData getDeviceData () {
	checkDevice ();
	DeviceData data = new DeviceData ();
	data.display_name = display_name;
	data.application_name = application_name;
	data.application_class = application_class;
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
}

public Color getSystemColor (int id) {
	checkDevice ();
	XColor xColor = null;
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
	if (xColor == null) return COLOR_BLACK;
	return Color.motif_new (this, xColor);
}

public Font getSystemFont () {
	checkDevice ();
	int shellHandle, widgetHandle;
	int widgetClass = OS.TopLevelShellWidgetClass ();
	shellHandle = OS.XtAppCreateShell (null, null, widgetClass, xDisplay, null, 0);
	widgetHandle = OS.XmCreateLabel (shellHandle, null, null, 0);
	int [] argList = {OS.XmNfontList, 0};
	OS.XtGetValues (widgetHandle, argList, argList.length / 2);
	/**
	 * Feature in Motif. Querying the font list from the widget and
	 * then destroying the shell (and the widget) could cause the
	 * font list to be freed as well. The fix is to make a copy of
	 * the font list, then to free it when the display is disposed.
	 */ 
	int labelFont = OS.XmFontListCopy (argList [1]);
	OS.XtDestroyWidget (shellHandle);
	return Font.motif_new (this, labelFont);
}

public boolean getWarnings () {
	checkDevice ();
	return warnings;
}

protected void init () {
		
	/* Create the warning and error callbacks */
	xErrorCallback = new Callback (this, "xErrorProc", 2);
	xNullErrorProc = xErrorCallback.getAddress ();
	if (xNullErrorProc == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	xtWarningCallback = new Callback (this, "xtWarningProc", 1);
	xtNullWarningProc = xtWarningCallback.getAddress ();
	if (xtNullWarningProc == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	xIOErrorCallback = new Callback (this, "xIOErrorProc", 1);
	xNullIOErrorProc = xIOErrorCallback.getAddress ();
	if (xNullIOErrorProc == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	xtErrorCallback = new Callback (this, "xtErrorProc", 1);
	xtNullErrorProc = xtErrorCallback.getAddress ();
	if (xtNullErrorProc == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	
	/* Set the warning and error handlers */
	if (debug) OS.XSynchronize (xDisplay, true);
	int xtContext = OS.XtDisplayToApplicationContext (xDisplay);
	xErrorProc = OS.XSetErrorHandler (xNullErrorProc);
	if (!debug) OS.XSetErrorHandler (xErrorProc);
	xtWarningProc = OS.XtAppSetWarningHandler (xtContext, xtNullWarningProc);
	if (!debug) OS.XtAppSetWarningHandler (xtContext, xtWarningProc);
	xIOErrorProc = OS.XSetIOErrorHandler (xNullIOErrorProc);
	if (!debug) OS.XSetIOErrorHandler (xIOErrorProc);
	xtErrorProc = OS.XtAppSetErrorHandler (xtContext, xtNullErrorProc);
	if (!debug) OS.XtAppSetErrorHandler (xtContext, xtErrorProc);
	
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
}

public abstract int internal_new_GC (GCData data);

public abstract void internal_dispose_GC (int handle, GCData data);

public boolean isDisposed () {
	return xDisplay == 0;
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
	
	COLOR_BLACK = COLOR_DARK_RED = COLOR_DARK_GREEN = COLOR_DARK_YELLOW =
	COLOR_DARK_BLUE = COLOR_DARK_MAGENTA = COLOR_DARK_CYAN = COLOR_GRAY = COLOR_DARK_GRAY = COLOR_RED =
	COLOR_GREEN = COLOR_YELLOW = COLOR_BLUE = COLOR_MAGENTA = COLOR_CYAN = COLOR_WHITE = null;
	
	/* Release the warning and error callbacks */
	xtWarningCallback.dispose (); xtWarningCallback = null;
	xtWarningProc = 0;
	xErrorCallback.dispose (); xErrorCallback = null;
	xErrorProc = 0;
	xtErrorCallback.dispose (); xtErrorCallback = null;
	xtErrorProc = 0;
	xIOErrorCallback.dispose (); xIOErrorCallback = null;
	xIOErrorProc = 0;
}

public void setWarnings (boolean warnings) {
	checkDevice ();
	this.warnings = warnings;
	if (debug) return;
	int xtContext = OS.XtDisplayToApplicationContext (xDisplay);
	if (warnings) {
		OS.XSetErrorHandler (xErrorProc);
		OS.XtAppSetWarningHandler (xtContext, xtWarningProc);
	} else {
		OS.XSetErrorHandler (xNullErrorProc);
		OS.XtAppSetWarningHandler (xtContext, xtNullWarningProc);
	}
}

int xErrorProc (int xDisplay, int xErrorEvent) {
	if (debug) {
		new SWTError ().printStackTrace ();
		OS.Call (xErrorProc, xDisplay, xErrorEvent);
	}
	return 0;
}

int xIOErrorProc (int xDisplay) {
	if (debug) {
		new SWTError ().printStackTrace ();
		OS.Call (xIOErrorProc, xDisplay, 0);
	}
	return 0;
}

int xtErrorProc (int message) {
	if (debug) {
		new SWTError ().printStackTrace ();
		OS.Call (xtErrorProc, message, 0);
	}
	return 0;
}

int xtWarningProc (int message) {
	if (debug) {
		new SWTError ().printStackTrace ();
		OS.Call (xtWarningProc, message, 0);
	}
	return 0;
}

}