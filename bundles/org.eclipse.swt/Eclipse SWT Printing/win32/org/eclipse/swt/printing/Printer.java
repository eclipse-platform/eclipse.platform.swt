package org.eclipse.swt.printing;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

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
	/**
	 * the handle to the printer DC
	 * (Warning: This field is platform dependent)
	 */
	public int handle;

	/**
	 * the printer data describing this printer
	 */
	PrinterData data;

	/**
	 * whether or not a GC was created for this printer
	 */
	boolean isGCCreated = false;

	/**
	 * strings used to access the Windows registry
	 * (Warning: These fields are platform dependent)
	 */
	static byte[] profile;
	static byte[] appName;
	static byte[] keyName;
	static {
		profile = Converter.wcsToMbcs(0, "PrinterPorts", true);
		appName = Converter.wcsToMbcs(0, "windows", true);
		keyName = Converter.wcsToMbcs(0, "device", true);
	}
	
/**
 * Returns an array of <code>PrinterData</code> objects
 * representing all available printers.
 *
 * @return the list of available printers
 */
public static PrinterData[] getPrinterList() {
	byte[] buf = new byte[1024];
	int n = OS.GetProfileStringA(profile, null, new byte[] {0}, buf, buf.length);
	if (n == 0) return new PrinterData[0];
	byte[][] deviceNames = new byte[5][];
	int nameCount = 0;
	int index = 0;
	for (int i = 0; i < n; i++) {
		if (buf[i] == 0) {
			if (nameCount == deviceNames.length) {
				byte[][] newNames = new byte[deviceNames.length + 5][];
				System.arraycopy(deviceNames, 0, newNames, 0, deviceNames.length);
				deviceNames = newNames;
			}
			deviceNames[nameCount] = new byte[i - index + 1];
			System.arraycopy(buf, index, deviceNames[nameCount], 0, i - index);
			nameCount++;
			index = i + 1;
		}
	}
	PrinterData printerList[] = new PrinterData[nameCount];
	for (int p = 0; p < nameCount; p++) {
		String device = new String(deviceNames[p], 0, deviceNames[p].length - 1);
		String driver = "";
		if (OS.GetProfileStringA(profile, deviceNames[p], new byte [] {0}, buf, buf.length) > 0) {
			int commaIndex = 0;
			while (buf[commaIndex] != ',' && commaIndex < buf.length) commaIndex++;
			if (commaIndex < buf.length) {
				byte[] driverName = new byte[commaIndex + 1];
				System.arraycopy(buf, 0, driverName, 0, commaIndex);
				driver = new String(driverName, 0, driverName.length - 1);
			}
		}
		printerList[p] = new PrinterData(driver, device);
	}
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
	byte [] deviceName = null;
	byte[] buf = new byte[1024];
	int n = OS.GetProfileStringA(appName, keyName, new byte[] {0}, buf, buf.length);
	if (n == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int commaIndex = 0;
	while(buf[commaIndex] != ',' && commaIndex < buf.length) commaIndex++;
	if (commaIndex < buf.length) {
		deviceName = new byte[commaIndex + 1];
		System.arraycopy(buf, 0, deviceName, 0, commaIndex);
	}
	String device = new String(deviceName, 0, deviceName.length - 1);
	String driver = "";
	if (OS.GetProfileStringA(profile, deviceName, new byte [] {0}, buf, buf.length) > 0) {
		commaIndex = 0;
		while (buf[commaIndex] != ',' && commaIndex < buf.length) commaIndex++;
		if (commaIndex < buf.length) {
			byte[] driverName = new byte[commaIndex + 1];
			System.arraycopy(buf, 0, driverName, 0, commaIndex);
			driver = new String(driverName, 0, driverName.length - 1);
		}
	}
	return new PrinterData(driver, device);
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

/**	 
 * Creates the printer handle.
 * This method is called internally by the instance creation
 * mechanism of the <code>Device</code> class.
 */
protected void create(DeviceData deviceData) {
	data = (PrinterData)deviceData;
	/* Use the character encoding for the default locale */
	byte[] driver = Converter.wcsToMbcs(0, data.driver, true);
	byte[] device = Converter.wcsToMbcs(0, data.name, true);
	int lpInitData = 0;
	byte buffer [] = data.otherData;
	int hHeap = OS.GetProcessHeap();
	if (buffer != null && buffer.length != 0) {
		lpInitData = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, buffer.length);
		OS.MoveMemory(lpInitData, buffer, buffer.length);
	}
	handle = OS.CreateDCA(driver, device, 0, lpInitData);
	if (lpInitData != 0) OS.HeapFree(hHeap, 0, lpInitData);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
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
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	if (data != null) {
		if (isGCCreated) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		data.device = this;
		data.hFont = OS.GetCurrentObject(handle, OS.OBJ_FONT);
		isGCCreated = true;
	}
	return handle;
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
public void internal_dispose_GC(int hDC, GCData data) {
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
	DOCINFO di = new DOCINFO();
	di.cbSize = DOCINFO.sizeof;
	int hHeap = OS.GetProcessHeap();
	int lpszDocName = 0;
	if (jobName != null && jobName.length() != 0) {
		/* Use the character encoding for the default locale */
		byte [] buffer = Converter.wcsToMbcs(0, jobName, true);
		lpszDocName = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, buffer.length);
		OS.MoveMemory(lpszDocName, buffer, buffer.length);
		di.lpszDocName = lpszDocName;
	}
	int lpszOutput = 0;
	if (data.printToFile && data.fileName != null) {
		/* Use the character encoding for the default locale */
		byte [] buffer = Converter.wcsToMbcs(0, data.fileName, true);
		lpszOutput = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, buffer.length);
		OS.MoveMemory(lpszOutput, buffer, buffer.length);
		di.lpszOutput = lpszOutput;
	}
	int rc = OS.StartDocA(handle, di);
	if (lpszDocName != 0) OS.HeapFree(hHeap, 0, lpszDocName);
	if (lpszOutput != 0) OS.HeapFree(hHeap, 0, lpszOutput);
	return rc > 0;
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
	OS.EndDoc(handle);
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
	OS.AbortDoc(handle);
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
	int rc = OS.StartPage(handle);
	if (rc <= 0) OS.AbortDoc(handle);
	return rc > 0;
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
	OS.EndPage(handle);
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
	int dpiX = OS.GetDeviceCaps(handle, OS.LOGPIXELSX);
	int dpiY = OS.GetDeviceCaps(handle, OS.LOGPIXELSY);
	return new Point(dpiX, dpiY);
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
	int width = OS.GetDeviceCaps(handle, OS.PHYSICALWIDTH);
	int height = OS.GetDeviceCaps(handle, OS.PHYSICALHEIGHT);
	return new Rectangle(0, 0, width, height);
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
	int width = OS.GetDeviceCaps(handle, OS.HORZRES);
	int height = OS.GetDeviceCaps(handle, OS.VERTRES);
	return new Rectangle(0, 0, width, height);
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
	int printX = -OS.GetDeviceCaps(handle, OS.PHYSICALOFFSETX);
	int printY = -OS.GetDeviceCaps(handle, OS.PHYSICALOFFSETY);
	int printWidth = OS.GetDeviceCaps(handle, OS.HORZRES);
	int printHeight = OS.GetDeviceCaps(handle, OS.VERTRES);
	int paperWidth = OS.GetDeviceCaps(handle, OS.PHYSICALWIDTH);
	int paperHeight = OS.GetDeviceCaps(handle, OS.PHYSICALHEIGHT);
	int hTrim = paperWidth - printWidth;
	int vTrim = paperHeight - printHeight;
	return new Rectangle(x + printX, y + printY, width + hTrim, height + vTrim);
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
 * Checks the validity of this device.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
protected void checkDevice() {
	if (handle == 0) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
}

/**	 
 * Releases any internal state prior to destroying this printer.
 * This method is called internally by the dispose
 * mechanism of the <code>Device</code> class.
 */
protected void release() {
	super.release();
	data = null;
}

/**	 
 * Destroys the printer handle.
 * This method is called internally by the dispose
 * mechanism of the <code>Device</code> class.
 */
protected void destroy() {
	if (handle != 0) OS.DeleteDC(handle);
	handle = 0;
}

}
