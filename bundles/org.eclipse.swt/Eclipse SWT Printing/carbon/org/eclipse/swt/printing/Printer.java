/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.printing;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.CFRange;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.PMRect;
import org.eclipse.swt.internal.carbon.PMResolution;

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
	int printSession, printSettings, pageFormat;
	boolean inPage;
	int context;
	int colorspace;

	static final String DRIVER = "Mac";
	static final String PRINTER_DRIVER = "Printer";
	static final String FILE_DRIVER = "File";
	static final String PREVIEW_DRIVER = "Preview";
	static final String FAX_DRIVER = "Fax";

/**
 * Returns an array of <code>PrinterData</code> objects
 * representing all available printers.
 *
 * @return the list of available printers
 */
public static PrinterData[] getPrinterList() {
	PrinterData[] result = null;
	int[] printSession = new int[1];
	OS.PMCreateSession(printSession);
	if (printSession[0] != 0) {
		int[] printerList = new int[1], currentIndex = new int[1], currentPrinter = new int[1];
		OS.PMSessionCreatePrinterList(printSession[0], printerList, currentIndex, currentPrinter);
		if (printerList[0] != 0) {
			int count = OS.CFArrayGetCount(printerList[0]);
			result = new PrinterData[count];
			for (int i=0; i<count; i++) {
				String name = getString(OS.CFArrayGetValueAtIndex(printerList[0], i));
				PrinterData data = result[i] = new PrinterData(DRIVER, name);
			}
			OS.CFRelease(printerList[0]);
		}
		OS.PMRelease(printSession[0]);
	}
	return result == null ? new PrinterData[0] : result;
}

/**
 * Returns a <code>PrinterData</code> object representing
 * the default printer or <code>null</code> if there is no 
 * printer available on the System.
 *
 * @return the default printer data or null
 * 
 * @since 2.1
 */
public static PrinterData getDefaultPrinterData() {
	PrinterData result = null;
	int[] printSession = new int[1];
	OS.PMCreateSession(printSession);
	if (printSession[0] != 0) {
		String name = getCurrentPrinterName(printSession[0]);
		if (name != null) result = new PrinterData(DRIVER, name);
		OS.PMRelease(printSession[0]);
	}
	return result;
}
static String getCurrentPrinterName(int printSession) {
	String result = null;
	int[] printerList = new int[1], currentIndex = new int[1], currentPrinter = new int[1];
	OS.PMSessionCreatePrinterList(printSession, printerList, currentIndex, currentPrinter);
	if (printerList[0] != 0) {
		int count = OS.CFArrayGetCount(printerList[0]);
		if (currentIndex[0] >= 0 && currentIndex[0] < count) {
			result = getString(OS.CFArrayGetValueAtIndex(printerList[0], currentIndex[0]));
		}
		OS.CFRelease(printerList[0]);
	}
	return result;
}
static String getString(int ptr) {
	int length = OS.CFStringGetLength(ptr);
	char [] buffer = new char[length];
	CFRange range = new CFRange();
	range.length = length;
	OS.CFStringGetCharacters(ptr, range, buffer);
	return new String(buffer);
}
static int packData(int handle, byte[] buffer, int offset) {
	int length = OS.GetHandleSize (handle);
	buffer[offset++] = (byte)((length & 0xFF) >> 0);
	buffer[offset++] = (byte)((length & 0xFF00) >> 8);
	buffer[offset++] = (byte)((length & 0xFF0000) >> 16);
	buffer[offset++] = (byte)((length & 0xFF000000) >> 24);
	int [] ptr = new int [1];
	OS.HLock(handle);
	OS.memcpy(ptr, handle, 4);
	byte[] buffer1 = new byte[length];
	OS.memcpy(buffer1, ptr [0], length);
	OS.HUnlock(handle);
	System.arraycopy(buffer1, 0, buffer, offset, length);
	return offset + length;
}
static int unpackData(int[] handle, byte[] buffer, int offset) {
	int length = 
		((buffer[offset++] & 0xFF) << 0) |
		((buffer[offset++] & 0xFF) << 8) |
		((buffer[offset++] & 0xFF) << 16) |
		((buffer[offset++] & 0xFF) << 24);
	handle[0] = OS.NewHandle(length);
	if (handle[0] == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int[] ptr = new int[1];
	OS.HLock(handle[0]);
	OS.memcpy(ptr, handle[0], 4);
	byte[] buffer1 = new byte[length];
	System.arraycopy(buffer, offset, buffer1, 0, length);
	OS.memcpy(ptr[0], buffer1, length);
	OS.HUnlock(handle[0]);
	return offset + length;
}

/**
 * Constructs a new printer representing the default printer.
 * <p>
 * You must dispose the printer when it is no longer required. 
 * </p>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NO_HANDLES - if there are no valid printers
 * </ul>
 *
 * @see #dispose
 */
public Printer() {
	this(null);
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
 *    <li>ERROR_NO_HANDLES - if there are no valid printers
 * </ul>
 *
 * @see #dispose
 */
public Printer(PrinterData data) {
	super (checkNull(data));
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
	PMRect pageRect = new PMRect();
	PMRect paperRect = new PMRect();
	OS.PMGetAdjustedPageRect(pageFormat, pageRect);
	OS.PMGetAdjustedPaperRect(pageFormat, paperRect);
	return new Rectangle(x+(int)paperRect.left, y+(int)paperRect.top, width+(int)(paperRect.right-pageRect.right), height+(int)(paperRect.bottom-pageRect.bottom));
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
protected void create(DeviceData deviceData) {
	data = (PrinterData)deviceData;
	
	int[] buffer = new int[1];
	if (OS.PMCreateSession(buffer) != OS.noErr) SWT.error(SWT.ERROR_NO_HANDLES);
	printSession = buffer[0];
	if (printSession == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		
	if (data.otherData != null) {
		/* Deserialize settings */
		int offset = 0;
		byte[] otherData = data.otherData;
		offset = unpackData(buffer, otherData, offset);
		int flatSettings = buffer[0];
		offset = unpackData(buffer, otherData, offset);
		int flatFormat = buffer[0];
		if (OS.PMUnflattenPrintSettings(flatSettings, buffer) != OS.noErr) SWT.error(SWT.ERROR_NO_HANDLES);
		printSettings = buffer[0];
		if (printSettings == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		if (OS.PMUnflattenPageFormat(flatFormat, buffer) != OS.noErr) SWT.error(SWT.ERROR_NO_HANDLES);
		pageFormat = buffer[0];
		if (pageFormat == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		OS.DisposeHandle(flatSettings);
		OS.DisposeHandle(flatFormat);
	} else {
		/* Create default settings */
		if (OS.PMCreatePrintSettings(buffer) != OS.noErr) SWT.error(SWT.ERROR_NO_HANDLES);
		printSettings = buffer[0];
		if (printSettings == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		OS.PMSessionDefaultPrintSettings(printSession, printSettings);
		if (OS.PMCreatePageFormat(buffer) != OS.noErr) SWT.error(SWT.ERROR_NO_HANDLES);
		pageFormat = buffer[0];
		if (pageFormat == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		OS.PMSessionDefaultPageFormat(printSession, pageFormat);
	}
	
	if (PREVIEW_DRIVER.equals(data.driver)) {
		OS.PMSessionSetDestination(printSession, printSettings, (short) OS.kPMDestinationPreview, 0, 0);
	}
	String name = data.name;
	char[] buffer1 = new char[name.length ()];
	name.getChars(0, buffer1.length, buffer1, 0);
	int ptr = OS.CFStringCreateWithCharacters(OS.kCFAllocatorDefault, buffer1, buffer1.length);
	if (ptr != 0) OS.PMSessionSetCurrentPrinter(printSession, ptr); 
	OS.CFRelease(ptr);
	
	OS.PMSessionValidatePrintSettings(printSession, printSettings, null);
	OS.PMSessionValidatePageFormat(printSession, pageFormat, null);	
	
	int graphicsContextsArray = OS.CFArrayCreateMutable(OS.kCFAllocatorDefault, 1, 0);
	if (graphicsContextsArray != 0) {
		OS.CFArrayAppendValue(graphicsContextsArray, OS.kPMGraphicsContextCoreGraphics());
		OS.PMSessionSetDocumentFormatGeneration(printSession, OS.kPMDocumentFormatPDF(), graphicsContextsArray, 0);
		OS.CFRelease(graphicsContextsArray);
	}
}

/**	 
 * Destroys the printer handle.
 * This method is called internally by the dispose
 * mechanism of the <code>Device</code> class.
 */
protected void destroy() {
	if (pageFormat != 0) OS.PMRelease(pageFormat);
	pageFormat = 0;
	if (printSettings != 0) OS.PMRelease(printSettings);
	printSettings = 0;
	if (printSession != 0) OS.PMRelease(printSession);
	printSession = 0;
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
 */
public int internal_new_GC(GCData data) {
	checkDevice();
	setupNewPage();
	data.device = this;
	data.background = getSystemColor(SWT.COLOR_WHITE).handle;
	data.foreground = getSystemColor(SWT.COLOR_BLACK).handle;
	return context;
}

protected void init () {
	super.init();
	colorspace = OS.CGColorSpaceCreateDeviceRGB();
	if (colorspace == 0) SWT.error(SWT.ERROR_NO_HANDLES);
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
 */
public void internal_dispose_GC(int context, GCData data) {
	checkDevice();
}

protected void release () {
	if (colorspace != 0) OS.CGColorSpaceRelease(colorspace);
	colorspace = 0;
	super.release();
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
	if (jobName != null && jobName.length() != 0) {
		char[] buffer = new char[jobName.length ()];
		jobName.getChars(0, buffer.length, buffer, 0);
		int ptr = OS.CFStringCreateWithCharacters(OS.kCFAllocatorDefault, buffer, buffer.length);
		if (ptr != 0) OS.PMSetJobNameCFString(printSettings, ptr); 
		OS.CFRelease (ptr);
	}
	return OS.PMSessionBeginDocumentNoDialog(printSession, printSettings, pageFormat) == OS.noErr;
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
	if (inPage) {
		OS.PMSessionEndPageNoDialog(printSession);
		inPage = false;
	}
	OS.PMSessionEndDocumentNoDialog(printSession);
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
	OS.PMSessionSetError(printSession, OS.kPMCancel);
}

static DeviceData checkNull (PrinterData data) {
	if (data == null) data = new PrinterData();
	if (data.driver == null || data.name == null) {
		PrinterData defaultPrinter = getDefaultPrinterData();
		if (defaultPrinter == null) SWT.error(SWT.ERROR_NO_HANDLES);
		data.driver = defaultPrinter.driver;
		data.name = defaultPrinter.name;		
	}
	return data;
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
	if (OS.PMSessionError(printSession) != OS.noErr) return false;
	setupNewPage();
	return context != 0;
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
	if (inPage) {
		OS.PMSessionEndPageNoDialog(printSession);
		inPage = false;
	}
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
	PMResolution resolution = new PMResolution();
	OS.PMGetResolution(pageFormat, resolution);
	return new Point((int)resolution.hRes, (int)resolution.vRes);
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
	PMRect paperRect = new PMRect();
	OS.PMGetAdjustedPaperRect(pageFormat, paperRect);
	return new Rectangle(0, 0, (int)(paperRect.right-paperRect.left), (int)(paperRect.bottom-paperRect.top));
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
	PMRect pageRect = new PMRect();
	OS.PMGetAdjustedPageRect(pageFormat, pageRect);
	return new Rectangle(0, 0, (int)(pageRect.right-pageRect.left), (int)(pageRect.bottom-pageRect.top));
}

/**
 * Returns a <code>PrinterData</code> object representing the
 * target printer for this print job.
 * 
 * @return a PrinterData object describing the receiver
 */
public PrinterData getPrinterData() {
	checkDevice();
	return data;
}

/**
 * On the Mac the core graphics context for printing is only valid between PMSessionBeginPage and PMSessionEndPage,
 * so printing code has to retrieve and initializes a graphic context for every page like this:
 * 
 * <pre>
 * PMSessionBeginDocument
 *    PMSessionBeginPage
 * 	     PMSessionGetGraphicsContext
 * 		 // ... use context
 *    PMSessionEndPage
 * PMSessionEndDocument
 * </pre>
 * 
 * In SWT it is OK to create a GC once between startJob / endJob and use it for all pages in between:
 * 
 * <pre>
 * startJob(...);
 * 	  GC gc= new GC(printer);
 *    startPage();
 * 		 // ... use gc
 *    endPage();
 *    gc.dispose();
 * endJob();
 * </pre>
 * 
 * The solution to resolve this difference is to rely on the fact that Mac OS X returns the same but
 * reinitialized graphics context for every page. So we only have to account for the fact that SWT assumes
 * that the graphics context keeps it settings across a page break when it actually does not.
 * So we have to copy some settings that exist in the CGC before a PMSessionEndPage to the CGC after a PMSessionBeginPage.
 * <p>
 * In addition to this we have to cope with the situation that in SWT we can create a GC before a call to
 * PMSessionBeginPage. For this we decouple the call to PMSessionBeginPage from
 * SWT's method startPage as follows: if a new GC is created before a call to startPage, internal_new_GC
 * does the PMSessionBeginPage and the next following startPage does nothing.
 * </p>
 */
void setupNewPage() {
	if (!inPage) {
		inPage= true;
		OS.PMSessionBeginPageNoDialog(printSession, pageFormat, null);
		int[] buffer = new int[1];
		OS.PMSessionGetGraphicsContext(printSession, 0, buffer);
		if (context == 0) {
			context = buffer[0];
		} else {
			if (context != buffer[0]) SWT.error(SWT.ERROR_UNSPECIFIED);
		}
		PMRect paperRect= new PMRect();
		OS.PMGetAdjustedPaperRect(pageFormat, paperRect);
		OS.CGContextScaleCTM(context, 1, -1);
		OS.CGContextTranslateCTM(context, 0, -(float)(paperRect.bottom-paperRect.top));
		OS.CGContextSetStrokeColorSpace(context, colorspace);
		OS.CGContextSetFillColorSpace(context, colorspace);
	}
}

}
