package org.eclipse.swt.printing;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*; 

/**
 * Instances of this class are used to print to a printer.
 * Applications create a GC on a printer using <code>new GC(printer)</code>
 * and then draw on the printer GC using the usual graphics calls.
 * <p>
 * A <code>Printer</code> object may be constructed by providing
 * a <code>PrinterData</code> object which identifies the printer.
 * A <code>PrintDialog</code> presents a print dialog to the user
 * and returns an initialized instance of <code>PrinterData</code>.
 * Alternatively, calling <code>new Printer()</code> will construct a
 * printer object for the user's default printer.
 * </p><p>
 * Application code must explicitly invoke the <code>Printer.dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 *
 * @see PrinterData
 * @see PrintDialog
 */
public final class Printer extends Device {
	PrinterData data;
	int printContext, xScreen, xDrawable, xtContext;
	Font defaultFont;

	static String APP_NAME = "SWT_Printer";
	
	public static String XDefaultPrintServer = ":1";
//	static {
//		/* Read the default print server name from
//		 * the XPRINTER environment variable.
//		 */
//		XDefaultPrintServer = ":1";
//	}
/**
 * Returns an array of <code>PrinterData</code> objects
 * representing all available printers.
 *
 * @return the list of available printers
 */
public static PrinterData[] getPrinterList() {
	// TEMPORARY CODE
	if (true) return new PrinterData[0];
	
	/* Connect to the default X print server */
	byte [] buffer = Converter.wcsToMbcs(null, XDefaultPrintServer, true);
	int xtContext = OS.XtCreateApplicationContext ();
	int pdpy =  OS.XtOpenDisplay (xtContext, buffer, null, null, 0, 0, new int [] {0}, 0);
	if (pdpy == 0) {
		/* no print server */
		return new PrinterData[0];
	}

	/* Get the list of printers */
	int [] listCount = new int[1];
	int plist = OS.XpGetPrinterList(pdpy, null, listCount);
	int printerCount = listCount[0];
	if (plist == 0 || printerCount == 0) {
		/* no printers */
		//OS.XCloseDisplay(pdpy);
		return new PrinterData[0];
	}
    
	/* Copy the printer names into PrinterData objects */
	int [] stringPointers = new int [printerCount * 2];
	OS.memmove(stringPointers, plist, printerCount * 2 * 4);
	PrinterData printerList[] = new PrinterData[printerCount];
	for (int i = 0; i < printerCount; i++) {
		String name = "";
		int address = stringPointers[i * 2];
		if (address != 0) {
			int length = OS.strlen(address);
			buffer = new byte [length];
			OS.memmove(buffer, address, length);
			/* Use the character encoding for the default locale */
			name = new String(Converter.mbcsToWcs(null, buffer));
		}
		printerList[i] = new PrinterData(XDefaultPrintServer, name);
	}
	OS.XpFreePrinterList(plist);
	OS.XtDestroyApplicationContext (xtContext);
	return printerList;
}

/*
 * Returns a <code>PrinterData</code> object representing
 * the default printer.
 *
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if an error occurred constructing the default printer data</li>
 * </ul>
 *
 * @return the default printer data
 */
static PrinterData getDefaultPrinterData() {
	/* Use the first printer in the list as the default */
	PrinterData[] list = getPrinterList();
	if (list.length == 0) {
		/* no printers */
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	return list[0];
}

/**
 * Constructs a new printer representing the default printer.
 * <p>
 * You must dispose the printer when it is no longer required. 
 * </p>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_UNSPECIFIED - if there are no valid printers
 * </ul>
 *
 * @see #dispose
 */
public Printer() {
	this(getDefaultPrinterData());
}

/**
 * Constructs a new printer given a <code>PrinterData</code>
 * object representing the desired printer.
 * <p>
 * You must dispose the printer when it is no longer required. 
 * </p>
 *
 * @param data the printer data for the specified printer
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the specified printer data does not represent a valid printer
 *    <li>ERROR_UNSPECIFIED - if there are no valid printers
 * </ul>
 *
 * @see #dispose
 */
public Printer(PrinterData data) {
	super(data);
}

protected void create(DeviceData deviceData) {
	data = (PrinterData)deviceData;

	/* Open the display for the X print server */	
	String display_name = null;
	String application_name = APP_NAME;
	String application_class = APP_NAME;
	if (data != null) {
//		if (data.display_name != null) display_name = data.display_name;
		if (data.driver != null) display_name = data.driver;
		if (data.application_name != null) application_name = data.application_name;
		if (data.application_class != null) application_class = data.application_class;
	}
	/* Use the character encoding for the default locale */
	byte [] displayName = null, appName = null, appClass = null;
	if (display_name != null) displayName = Converter.wcsToMbcs (null, display_name, true);
	if (application_name != null) appName = Converter.wcsToMbcs (null, application_name, true);
	if (application_class != null) appClass = Converter.wcsToMbcs (null, application_class, true);
	
	xtContext = OS.XtCreateApplicationContext ();
	xDisplay =  OS.XtOpenDisplay (xtContext, displayName, appName, appClass, 0, 0, new int [] {0}, 0);
	if (xDisplay == 0) {
		/* no print server */
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
}

protected void init() {
	super.init();
	
	/* Create the printContext for the printer */
	/* Use the character encoding for the default locale */
	byte[] name = Converter.wcsToMbcs(null, data.name, true);
	printContext = OS.XpCreateContext(xDisplay, name);
	if (printContext == OS.None) {
		/* can't create print context */
		//OS.XCloseDisplay(xDisplay);
		SWT.error(SWT.ERROR_NO_HANDLES);
	}

	/* Set the printContext into the display */
	OS.XpSetContext(xDisplay, printContext); 

	/* Get the printer's screen */
	xScreen = OS.XpGetScreenOfContext(xDisplay, printContext);
	
	/* Initialize Motif */
	int widgetClass = OS.TopLevelShellWidgetClass();
	int shellHandle = OS.XtAppCreateShell(null, null, widgetClass, xDisplay, null, 0);
	OS.XtDestroyWidget(shellHandle);
	
	/* Initialize the default font */
	/* Use the character encoding for the default locale */
	byte [] buffer = Converter.wcsToMbcs(null, "-*-courier-medium-r-*-*-*-120-*-*-*-*-*-*", true);
	int fontListEntry = OS.XmFontListEntryLoad(xDisplay, buffer, OS.XmFONT_IS_FONTSET, OS.XmFONTLIST_DEFAULT_TAG);
	if (fontListEntry == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int defaultFontList = OS.XmFontListAppendEntry(0, fontListEntry);
	OS.XmFontListEntryFree(new int[]{fontListEntry});
	defaultFont = Font.motif_new(this, defaultFontList);
}

protected void destroy() {
	if (xtContext != 0) OS.XtDestroyApplicationContext (xtContext);
}

/**	 
 * Invokes platform specific functionality to allocate a new GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Printer</code>. It is marked public only so that it
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
public int internal_new_GC(GCData data) {
	if (xDrawable == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int xGC = OS.XCreateGC(xDisplay, xDrawable, 0, null);
	if (xGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (data != null) {
		data.device = this;
		data.display = xDisplay;
		data.drawable = xDrawable; // not valid until after startJob
		data.fontList = defaultFont.handle;
		data.codePage = defaultFont.codePage;
		data.colormap = OS.XDefaultColormapOfScreen(xScreen);
	}
	return xGC;
}

/**	 
 * Invokes platform specific functionality to dispose a GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Printer</code>. It is marked public only so that it
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
public void internal_dispose_GC(int xGC, GCData data) {
	OS.XFreeGC(xDisplay, xGC);
}

/**
 * Starts a print job and returns true if the job started successfully
 * and false otherwise.
 * <p>
 * This must be the first method called to initiate a print job,
 * followed by any number of startPage/endPage calls, followed by
 * endJob. Calling startPage, endPage, or endJob before startJob
 * will result in undefined behavior.
 * </p>
 * 
 * @return true if the job started successfully and false otherwise.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #startPage
 * @see #endPage
 * @see #endJob
 */
public boolean startJob(String jobName) {
	checkDevice();
	/* Use the character encoding for the default locale */
	byte [] buffer = Converter.wcsToMbcs(null, "*job-name: " + jobName, true);
	OS.XpSetAttributes(xDisplay, printContext, OS.XPJobAttr, buffer, OS.XPAttrMerge);
	OS.XpStartJob(xDisplay, OS.XPSpool);

	/* Create the xDrawable */
	XRectangle rect = new XRectangle();
	short [] width = new short [1];
	short [] height = new short [1];
	OS.XpGetPageDimensions(xDisplay, printContext, width, height, rect);
	xDrawable = OS.XCreateWindow(xDisplay, OS.XRootWindowOfScreen(xScreen), 
		0, 0, rect.width, rect.height, 0,
		OS.CopyFromParent, OS.CopyFromParent, OS.CopyFromParent, 0, 0);
	return true;
}

/**
 * Ends the current print job.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #startJob
 * @see #startPage
 * @see #endPage
 */
public void endJob() {
	checkDevice();
	OS.XpEndJob(xDisplay);
	OS.XFlush(xDisplay);
}

/**
 * Cancels a print job in progress. 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void cancelJob() {
	checkDevice();
	OS.XpCancelJob(xDisplay, true);
}

/**
 * Starts a page and returns true if the page started successfully
 * and false otherwise.
 * <p>
 * After calling startJob, this method may be called any number of times
 * along with a matching endPage.
 * </p>
 * 
 * @return true if the page started successfully and false otherwise.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #endPage
 * @see #startJob
 * @see #endJob
 */
public boolean startPage() {
	checkDevice();
	OS.XpStartPage(xDisplay, xDrawable);
	return true;
}

/**
 * Ends the current page.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #startPage
 * @see #startJob
 * @see #endJob
 */
public void endPage() {
	checkDevice();
	OS.XpEndPage(xDisplay);
}

/**
 * Returns a point whose x coordinate is the horizontal
 * dots per inch of the printer, and whose y coordinate
 * is the vertical dots per inch of the printer.
 *
 * @return the horizontal and vertical DPI
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point getDPI() {
	checkDevice();
	/* Use the character encoding for the default locale */
	byte [] buffer = Converter.wcsToMbcs(null, "default-printer-resolution", true);
	int pool = OS.XpGetOneAttribute(xDisplay, printContext, OS.XPDocAttr, buffer);
    int length = OS.strlen(pool);
	buffer = new byte[length];
	OS.memmove(buffer, pool, length);
	OS.XtFree(pool);
	String resolution = new String(buffer, 0, buffer.length);
	int res = 300; // default
	if (resolution.length() == 0) {
		/* If we can't get the info from the DocAttrs, ask the printer. */
		/* Use the character encoding for the default locale */
		buffer = Converter.wcsToMbcs(null, "printer-resolutions-supported", true);
		pool = OS.XpGetOneAttribute(xDisplay, printContext, OS.XPPrinterAttr, buffer);
    		length = OS.strlen(pool);
		buffer = new byte[length];
		OS.memmove(buffer, pool, length);
		OS.XtFree(pool);
		int n = 0;
		while (!Compatibility.isWhitespace((char)buffer[n]) && n < buffer.length) n++;
		resolution = new String(buffer, 0, n);
	}
	if (resolution.length() != 0) {
		try {
			res = Integer.parseInt(resolution);
		} catch (NumberFormatException ex) {}
	}
	return new Point(res, res);
}

/**
 * Returns a rectangle describing the receiver's size and location.
 * For a printer, this is the size of a page, in pixels.
 *
 * @return the bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getClientArea
 * @see #computeTrim
 */
public Rectangle getBounds() {
	checkDevice();
	XRectangle rect = new XRectangle();
	short [] width = new short [1];
	short [] height = new short [1];
	OS.XpGetPageDimensions(xDisplay, printContext, width, height, rect);
	return new Rectangle(0, 0, width[0], height[0]);
}

/**
 * Returns a rectangle which describes the area of the
 * receiver which is capable of displaying data.
 * For a printer, this is the size of the printable area
 * of a page, in pixels.
 * 
 * @return the client area
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getBounds
 * @see #computeTrim
 */
public Rectangle getClientArea() {
	checkDevice();
	XRectangle rect = new XRectangle();
	OS.XpGetPageDimensions(xDisplay, printContext, new short [1], new short [1], rect);
	return new Rectangle(0, 0, rect.width, rect.height);
}

/**
 * Given a desired <em>client area</em> for the receiver
 * (as described by the arguments), returns the bounding
 * rectangle which would be required to produce that client
 * area.
 * <p>
 * In other words, it returns a rectangle such that, if the
 * receiver's bounds were set to that rectangle, the area
 * of the receiver which is capable of displaying data
 * (that is, not covered by the "trimmings") would be the
 * rectangle described by the arguments (relative to the
 * receiver's parent).
 * </p>
 * Note that there is no setBounds for a printer. This method
 * is usually used by passing in the client area (the 'printable
 * area') of the printer. It can also be useful to pass in 0, 0, 0, 0.
 * 
 * @return the required bounds to produce the given client area
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getBounds
 * @see #getClientArea
 */
public Rectangle computeTrim(int x, int y, int width, int height) {
	checkDevice();
	XRectangle rect = new XRectangle();
	short [] paperWidth = new short [1];
	short [] paperHeight = new short [1];
	OS.XpGetPageDimensions(xDisplay, printContext, paperWidth, paperHeight, rect);
	int hTrim = paperWidth[0] - rect.width;
	int vTrim = paperHeight[0] - rect.height;
	return new Rectangle(x - rect.x, y - rect.y, width + hTrim, height + vTrim);
}

/**
 * Returns a <code>PrinterData</code> object representing the
 * target printer for this print job.
 * 
 * @return a PrinterData object describing the receiver
 */
public PrinterData getPrinterData() {
	return data;
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
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Font getSystemFont () {
	checkDevice ();
	return defaultFont;
}

protected void checkDevice() {
	if (xDisplay == 0) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
}

protected void release() {
	super.release();
	if (defaultFont != null) {
		OS.XmFontListFree(defaultFont.handle);
		defaultFont.handle = 0;
		defaultFont = null;
	}
	if (printContext != 0) {
		OS.XpDestroyContext(xDisplay, printContext);
		printContext = 0;
	}
	if (xDrawable != 0) {
		OS.XDestroyWindow(xDisplay, xDrawable);
		xDrawable = 0;
	}
	xScreen = 0;
	data = null;
}

}
