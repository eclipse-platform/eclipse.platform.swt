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
package org.eclipse.swt.printing;


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
 * @see <a href="http://www.eclipse.org/swt/snippets/#printing">Printing snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class Printer extends Device {
	PrinterData data;
	int printContext, xScreen, xDrawable, xtContext;
	Font defaultFont;
	boolean isGCCreated;

	static String APP_NAME = "SWT_Printer";
	
static DeviceData checkNull (PrinterData data) {
	if (data == null) data = new PrinterData();
	if (data.application_name == null) {
		data.application_name = APP_NAME;
	}
	if (data.application_class == null) {
		data.application_class = APP_NAME;
	}
	if (data.name == null || data.driver == null) {
		PrinterData defaultData = getDefaultPrinterData();
		if (defaultData == null) SWT.error(SWT.ERROR_NO_HANDLES);
		data.name = defaultData.name;
		data.driver = defaultData.driver;
	}
	return data;
}	

/**
 * Returns a <code>PrinterData</code> object representing
 * the default printer or <code>null</code> if there is no 
 * default printer.
 *
 * @return the default printer data or null
 * 
 * @since 2.1
 */
public static PrinterData getDefaultPrinterData() {
	PrinterData[] list = getEnvPrinterList();
	PrinterData defaultPrinter = getEnvDefaultPrinter(list);
	if (defaultPrinter != null) return defaultPrinter;
	if (list.length != 0) return list[0];	
	return null;
}

/**
 * Returns the default printer <code>PrinterData</code> specified
 * by XPRINTER.
 *
 * @return the default printer data or <code>null</code>
 */
static PrinterData getEnvDefaultPrinter(PrinterData[] serverList) {
	String[] printerNames = new String[]{"XPRINTER", "PDPRINTER", "LPDEST", "PRINTER"};
	for (int i = 0; i < printerNames.length; i++) {
		int ptr = OS.getenv(Converter.wcsToMbcs(null, printerNames[i], true));
		if (ptr != 0) {
			int length = OS.strlen(ptr);
			byte[] buffer = new byte[length];
			OS.memmove(buffer, ptr, length);
			String defaultPrinter = new String(Converter.mbcsToWcs(null, buffer));
			int index = defaultPrinter.indexOf("@");
			if (index != -1) {
				String name = defaultPrinter.substring(0, index);
				String driver = defaultPrinter.substring(index + 1);
				return new PrinterData(driver, name);
			} else {
				for (int j = 0; j < serverList.length; j++) {
					PrinterData printerData = serverList[j];
					if (defaultPrinter.equals(printerData.name)) {
						return new PrinterData(printerData.driver, defaultPrinter);
					}
				}
			}					
		}	
	}
	return null;
}

/**
 * Returns an array of <code>PrinterData</code> objects
 * representing all available printers.  If there are no
 * printers, the array will be empty.
 *
 * @return an array of PrinterData objects representing the available printers
 */
public static PrinterData[] getPrinterList() {
	PrinterData[] list = getEnvPrinterList();
	/* Ensure that default printer data occurs in the printer list */	 
	PrinterData data = getEnvDefaultPrinter(list);	 
	if (data == null) return list;
	for (int i = 0; i < list.length; i++) {
		PrinterData printerData = list[i];
		if (printerData.name.equals(data.name) && printerData.driver.equals(data.driver)) {
			return list;
		}
	}
	PrinterData[] newList = new PrinterData[list.length + 1];
	System.arraycopy(list, 0, newList, 1, list.length);
	newList[0] = data;
	return newList;
}

/**
 * Returns a array of <code> PrinterData</code> objects
 * representing all available printers for all Xprint
 * servers specified in XPSERVERLIST.
 * 
 * Note: The default printer define by XPRINTER may not be
 * included in the list.
 * 
 * @return the list of printer for all servers
 */
static PrinterData[] getEnvPrinterList() {
	String[] serverList = getXPServerList();
	PrinterData[] printerList = new PrinterData[0];
	for (int i = 0; i < serverList.length; i++) {
		PrinterData[] printers = getEnvPrinterList(serverList[i]);
		if (printers.length != 0) {
			PrinterData[] newPrinterList = new PrinterData [printerList.length + printers.length];
			System.arraycopy(printerList, 0, newPrinterList, 0, printerList.length);
			System.arraycopy(printers, 0, newPrinterList, printerList.length, printers.length);
			printerList = newPrinterList;
		}	
	} 
	return printerList;
}

/**
 * Returns a array of <code> PrinterData</code> objects
 * representing all available printers for a specific 
 * XPrint server
 * 
 * @param server the XPrint server name
 * @return the list of printers for a given XPrint server
 */
static PrinterData[] getEnvPrinterList(String server) {
	byte[] buffer = Converter.wcsToMbcs(null, server, true);
	int pdpy = OS.XOpenDisplay (buffer);
	if (pdpy == 0) return new PrinterData[0];

	/* Get the list of printers */
	int[] count = new int[1];
	int plist = OS.XpGetPrinterList(pdpy, null, count);
	int printerCount = count[0];
	if (plist == 0 || printerCount == 0) {
		OS.XCloseDisplay(pdpy);
		if (plist != 0) OS.XpFreePrinterList(plist);
		return new PrinterData[0];		
	}
    
	/* Copy the printer names into PrinterData objects */
	int[] stringPointers = new int[printerCount * 2];
	OS.memmove(stringPointers, plist, printerCount * 2 * 4);
	PrinterData printerList[] = new PrinterData[printerCount];
	for (int i = 0; i < printerCount; i++) {
		String name = "";
		int address = stringPointers[i * 2];
		if (address != 0) {
			int length = OS.strlen(address);
			buffer = new byte [length];
			OS.memmove(buffer, address, length);
			name = new String(Converter.mbcsToWcs(null, buffer));
		}
		printerList[i] = new PrinterData(server, name);
	}
	OS.XCloseDisplay(pdpy);	
	OS.XpFreePrinterList(plist);	
	return printerList;
}

/**
 * Returns the value of XPSERVERLIST.
 * 
 *  @return the value of the XPSERVERLIST variable from the environment
 */
static String[] getXPServerList() {
	byte[] name = Converter.wcsToMbcs(null, "XPSERVERLIST", true);
	int ptr = OS.getenv(name);
	String[] serversList = new String[0];
	if (ptr != 0) {
		int length = OS.strlen(ptr);
		byte[] buffer1 = new byte[length];
		OS.memmove(buffer1, ptr, length);
		char[] buffer2 = Converter.mbcsToWcs(null, buffer1);
		int i = 0;
		while (i < buffer2.length) {
			if (buffer2[i] != ' ') {
				int start = i;
				while (++i < buffer2.length && buffer2[i] != ' ') {/*empty*/}
				String server = new String(buffer2, start, i - start);
				String[] newServerList = new String[serversList.length + 1];
				System.arraycopy(serversList, 0, newServerList, 0, serversList.length);
				newServerList[serversList.length] = server;
				serversList = newServerList;
			}
			i++;
		}		
	}	
	return serversList;
}

/**
 * Constructs a new printer representing the default printer.
 * <p>
 * Note: You must dispose the printer when it is no longer required. 
 * </p>
 *
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if there are no valid printers
 * </ul>
 *
 * @see Device#dispose
 */
public Printer() {
	this(null);
}

/**
 * Constructs a new printer given a <code>PrinterData</code>
 * object representing the desired printer. If the argument
 * is null, then the default printer will be used.
 * <p>
 * Note: You must dispose the printer when it is no longer required. 
 * </p>
 *
 * @param data the printer data for the specified printer, or null to use the default printer
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the specified printer data does not represent a valid printer
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if there are no valid printers
 * </ul>
 *
 * @see Device#dispose
 */
public Printer(PrinterData data) {
	super(checkNull(data));
}

/**	 
 * Creates the printer handle.
 * This method is called internally by the instance creation
 * mechanism of the <code>Device</code> class.
 * @param deviceData the device data
 */
protected void create(DeviceData deviceData) {
	data = (PrinterData)deviceData;
	/* Use the character encoding for the default locale */
	byte [] displayName = null, appName = null, appClass = null;
	displayName = Converter.wcsToMbcs(null, data.driver, true);
	appName = Converter.wcsToMbcs(null, data.application_name, true);
	appClass = Converter.wcsToMbcs(null, data.application_class, true);

	/* Open the display for the X print server */		
	xtContext = OS.XtCreateApplicationContext();
	if (xtContext == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	xDisplay =  OS.XtOpenDisplay(xtContext, displayName, appName, appClass, 0, 0, new int[]{0}, 0);
	if (xDisplay == 0) {
		OS.XtDestroyApplicationContext(xtContext);
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	setDPI = true;
}

protected void init() {
	super.init();
	
	/* Use the character encoding for the default locale */
	byte[] buffer = Converter.wcsToMbcs(null, data.name, true);

	/*
	 * Bug in Xp. If the printer name is not valid, Xp will
	 * cause a segmentation fault. The fix is to check if the
	 * printer name is valid before calling XpCreateContext().
	 */
	int[] count = new int[1];
	int plist = OS.XpGetPrinterList(xDisplay, buffer, count);
	if (plist != 0) OS.XpFreePrinterList(plist);
	if (count[0] == 0) SWT.error(SWT.ERROR_NO_HANDLES);

	/* Create the printContext for the printer */
	printContext = OS.XpCreateContext(xDisplay, buffer);

	/* Set the printContext into the display */
	OS.XpSetContext(xDisplay, printContext);

	/* Get the printer's screen */
	xScreen = OS.XpGetScreenOfContext(xDisplay, printContext);
	if (xScreen == 0) SWT.error(SWT.ERROR_NO_HANDLES);

	/* Initialize the xDrawable */
	XRectangle rect = new XRectangle();
	short[] width = new short[1];
	short[] height = new short[1];
	OS.XpGetPageDimensions(xDisplay, printContext, width, height, rect);
	xDrawable = OS.XCreateWindow(xDisplay, OS.XRootWindowOfScreen(xScreen), 
		0, 0, rect.width, rect.height, 0,
		OS.CopyFromParent, OS.CopyFromParent, OS.CopyFromParent, 0, null);
	if (xDrawable == 0) SWT.error(SWT.ERROR_NO_HANDLES);

	/* Initialize the default font */
	/* Use the character encoding for the default locale */
	Point dpi = getDPI();
	buffer = Converter.wcsToMbcs(null, "-*-courier-medium-r-*-*-*-120-"+dpi.x+"-"+dpi.y+"-*-*-iso8859-1", true);
	int fontListEntry = OS.XmFontListEntryLoad(xDisplay, buffer, OS.XmFONT_IS_FONTSET, OS.XmFONTLIST_DEFAULT_TAG);
	if (fontListEntry == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int defaultFontList = OS.XmFontListAppendEntry(0, fontListEntry);
	OS.XmFontListEntryFree(new int[]{fontListEntry});
	defaultFont = Font.motif_new(this, defaultFontList);
}

/**	 
 * Destroys the printer handle.
 * This method is called internally by the dispose
 * mechanism of the <code>Device</code> class.
 */
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
 * @noreference This method is not intended to be referenced by clients.
 */
public int internal_new_GC(GCData data) {
	if (data != null) {
		if (isGCCreated) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		if ((data.style & mask) == 0) {
			data.style |= SWT.LEFT_TO_RIGHT;
		}
		data.device = this;
		data.display = xDisplay;
		data.drawable = xDrawable;
		data.font = defaultFont;
		data.colormap = OS.XDefaultColormapOfScreen(xScreen);
		int defaultGC = OS.XDefaultGCOfScreen(xScreen);
		if (defaultGC != 0) {
			XGCValues values = new XGCValues();
			OS.XGetGCValues(xDisplay, defaultGC, OS.GCBackground | OS.GCForeground, values);
			XColor foreground = new XColor ();
			foreground.pixel = values.foreground;
			data.foreground = foreground;
			XColor background = new XColor ();
			background.pixel = values.background;
			data.background = background;
		}
		isGCCreated = true;
	}
	int xGC = OS.XCreateGC(xDisplay, xDrawable, 0, null);
	if (xGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
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
 * @param hDC the platform specific GC handle
 * @param data the platform specific GC data 
 * 
 * @noreference This method is not intended to be referenced by clients.
 */
public void internal_dispose_GC(int xGC, GCData data) {
	OS.XFreeGC(xDisplay, xGC);
	if (data != null) isGCCreated = false;
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
 * @param jobName the name of the print job to start
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
 * <p>
 * For a printer, this is the size of the physical page, in pixels.
 * </p>
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
 * <p>
 * For a printer, this is the size of the printable area
 * of the page, in pixels.
 * </p>
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
	return new Rectangle(rect.x, rect.y, rect.width, rect.height);
}

/**
 * Given a <em>client area</em> (as described by the arguments),
 * returns a rectangle, relative to the client area's coordinates,
 * that is the client area expanded by the printer's trim (or minimum margins).
 * <p>
 * Most printers have a minimum margin on each edge of the paper where the
 * printer device is unable to print.  This margin is known as the "trim."
 * This method can be used to calculate the printer's minimum margins
 * by passing in a client area of 0, 0, 0, 0 and then using the resulting
 * x and y coordinates (which will be <= 0) to determine the minimum margins
 * for the top and left edges of the paper, and the resulting width and height
 * (offset by the resulting x and y) to determine the minimum margins for the
 * bottom and right edges of the paper, as follows:
 * <ul>
 * 		<li>The left trim width is -x pixels</li>
 * 		<li>The top trim height is -y pixels</li>
 * 		<li>The right trim width is (x + width) pixels</li>
 * 		<li>The bottom trim height is (y + height) pixels</li>
 * </ul>
 * </p>
 * 
 * @param x the x coordinate of the client area
 * @param y the y coordinate of the client area
 * @param width the width of the client area
 * @param height the height of the client area
 * @return a rectangle, relative to the client area's coordinates, that is
 * 		the client area expanded by the printer's trim (or minimum margins)
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

/**
 * Checks the validity of this device.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
protected void checkDevice() {
	if (xDisplay == 0) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
}

/**	 
 * Releases any internal state prior to destroying this printer.
 * This method is called internally by the dispose
 * mechanism of the <code>Device</code> class.
 */
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
