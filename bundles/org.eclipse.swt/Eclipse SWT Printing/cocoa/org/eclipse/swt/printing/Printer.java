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
import org.eclipse.swt.internal.cocoa.*;

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
	NSPrinter printer;
	NSPrintInfo printInfo;
	NSPrintOperation operation;
	NSView view;
	NSWindow window;
	boolean isGCCreated;
	
	static final String DRIVER = "Mac";

/**
 * Returns an array of <code>PrinterData</code> objects
 * representing all available printers.  If there are no
 * printers, the array will be empty.
 *
 * @return an array of PrinterData objects representing the available printers
 */
public static PrinterData[] getPrinterList() {
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSArray printers = NSPrinter.printerNames();
		int count = (int)/*64*/printers.count();
		PrinterData[] result = new PrinterData[count];
		for (int i = 0; i < count; i++) {
			NSString str = new NSString(printers.objectAtIndex(i));
			result[i] = new PrinterData(DRIVER, str.getString());
		}
		return result;
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSPrinter printer = NSPrintInfo.defaultPrinter();
		if (printer == null) return null;
		NSString str = printer.name();
		return new PrinterData(DRIVER, str.getString());
	} finally {
		if (pool != null) pool.release();
	}
	
}

/**
 * Constructs a new printer representing the default printer.
 * <p>
 * Note: You must dispose the printer when it is no longer required. 
 * </p>
 *
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if there is no valid default printer
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
	super (checkNull(data));
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSSize paperSize = printInfo.paperSize();
		NSRect bounds = printInfo.imageablePageBounds();
		Point dpi = getDPI (), screenDPI = getIndependentDPI();
		float scaling = scalingFactor();
		x -= (bounds.x * dpi.x / screenDPI.x) / scaling;
		y -= (bounds.y * dpi.y / screenDPI.y) / scaling;
		width += ((paperSize.width - bounds.width) * dpi.x / screenDPI.x) / scaling;
		height += ((paperSize.height - bounds.height) * dpi.y / screenDPI.y) / scaling;
		return new Rectangle(x, y, width, height);
	} finally {
		if (pool != null) pool.release();
	}
}

/**	 
 * Creates the printer handle.
 * This method is called internally by the instance creation
 * mechanism of the <code>Device</code> class.
 * @param deviceData the device data
 */
protected void create(DeviceData deviceData) {
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSApplication.sharedApplication();
		data = (PrinterData)deviceData;
		if (data.otherData != null) {
			NSData nsData = NSData.dataWithBytes(data.otherData, data.otherData.length);
			printInfo = new NSPrintInfo(NSKeyedUnarchiver.unarchiveObjectWithData(nsData).id);
		} else {
			printInfo = NSPrintInfo.sharedPrintInfo();
		}
		printInfo.retain();
		printer = NSPrinter.printerWithName(NSString.stringWith(data.name));
		if (printer != null) {
			printer.retain();
			printInfo.setPrinter(printer);
		}
		if (data.duplex != SWT.DEFAULT) {
			int /*long*/ settings = printInfo.PMPrintSettings();
			int duplex = data.duplex == PrinterData.DUPLEX_SHORT_EDGE ? OS.kPMDuplexTumble
					: data.duplex == PrinterData.DUPLEX_LONG_EDGE ? OS.kPMDuplexNoTumble
					: OS.kPMDuplexNone;
			OS.PMSetDuplex(settings, duplex);
		}
		/* Updating printInfo from PMPrintSettings overrides values in the printInfo dictionary. */
		printInfo.updateFromPMPrintSettings();
		NSMutableDictionary dict = printInfo.dictionary();	
		if (data.collate != false) dict.setValue(NSNumber.numberWithBool(data.collate), OS.NSPrintMustCollate);
		if (data.copyCount != 1) dict.setValue(NSNumber.numberWithInt(data.copyCount), OS.NSPrintCopies);
		dict.setValue(NSNumber.numberWithInt(data.orientation == PrinterData.LANDSCAPE ? OS.NSLandscapeOrientation : OS.NSPortraitOrientation), OS.NSPrintOrientation);
		if (data.printToFile) {
			dict.setValue(OS.NSPrintSaveJob, OS.NSPrintJobDisposition);
			if (data.fileName != null) dict.setValue(NSString.stringWith(data.fileName), OS.NSPrintSavePath);
		}
		/*
		* Bug in Cocoa.  For some reason, the output still goes to the printer when
		* the user chooses the preview button.  The fix is to reset the job disposition.
		*/
		NSString job = printInfo.jobDisposition();
		if (job.isEqual(new NSString(OS.NSPrintPreviewJob()))) {
			printInfo.setJobDisposition(job);
		}
		NSRect rect = new NSRect();
		window = (NSWindow)new NSWindow().alloc();
		window.initWithContentRect(rect, OS.NSBorderlessWindowMask, OS.NSBackingStoreBuffered, false);
		String className = "SWTPrinterView"; //$NON-NLS-1$
		if (OS.objc_lookUpClass(className) == 0) {
			int /*long*/ cls = OS.objc_allocateClassPair(OS.class_NSView, className, 0);
			OS.class_addMethod(cls, OS.sel_isFlipped, OS.isFlipped_CALLBACK(), "@:");
			OS.objc_registerClassPair(cls);
		}
		view = (NSView)new SWTPrinterView().alloc();
		view.initWithFrame(rect);
		window.setContentView(view);
		operation = NSPrintOperation.printOperationWithView(view, printInfo);
		operation.retain();
		operation.setShowsPrintPanel(false);
		operation.setShowsProgressPanel(false);
	} finally {
		if (pool != null) pool.release();
	}
}

/**	 
 * Destroys the printer handle.
 * This method is called internally by the dispose
 * mechanism of the <code>Device</code> class.
 */
protected void destroy() {
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		if (printer != null) printer.release();
		if (printInfo != null) printInfo.release();
		if (view != null) view.release();
		if (window != null) window.release();
		if (operation != null) operation.release();
		printer = null;
		printInfo = null;
		view = null;
		window = null;
		operation = null;
	} finally {
		if (pool != null) pool.release();
	}
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
public int /*long*/ internal_new_GC(GCData data) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		if (data != null) {
			if (isGCCreated) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			data.device = this;
			data.background = getSystemColor(SWT.COLOR_WHITE).handle;
			data.foreground = getSystemColor(SWT.COLOR_BLACK).handle;
			data.font = getSystemFont ();
			float scaling = scalingFactor();
			Point dpi = getDPI (), screenDPI = getIndependentDPI();
			NSSize size = printInfo.paperSize();
			size.width = (size.width * (dpi.x / screenDPI.x)) / scaling;
			size.height = (size.height * dpi.y / screenDPI.y) / scaling;
			data.size = size;
			isGCCreated = true;
		}
		createContext();
		return operation.context().id;
	} finally {
		if (pool != null) pool.release();
	}
}

protected void init () {
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		super.init();
	} finally {
		if (pool != null) pool.release();
	}
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
public void internal_dispose_GC(int /*long*/ context, GCData data) {
	if (data != null) isGCCreated = false;
}

/**	 
 * Releases any internal state prior to destroying this printer.
 * This method is called internally by the dispose
 * mechanism of the <code>Device</code> class.
 */
protected void release () {
	super.release();
}

float scalingFactor() {
	NSNumber scale = new NSNumber(printInfo.dictionary().objectForKey(OS.NSPrintScalingFactor));
	return OS.PTR_SIZEOF == 4 ? scale.floatValue() : (float)scale.doubleValue();
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		if (jobName != null && jobName.length() != 0) {
			operation.setJobTitle(NSString.stringWith(jobName));
		}
		if (createContext()) {
			view.beginDocument();
			return true;
		}
		return false;
	} finally {
		if (pool != null) pool.release();
	}
}

boolean createContext () {
	if (operation.context() != null) return true;
	printInfo.setUpPrintOperationDefaultValues();
	NSPrintOperation.setCurrentOperation(operation);
	return operation.createContext() != null;
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		view.endDocument();
		operation.deliverResult();
		operation.destroyContext();
		operation.cleanUpOperation();
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		operation.destroyContext();
		operation.cleanUpOperation();
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		float scaling = scalingFactor();
		NSSize paperSize = printInfo.paperSize();
		paperSize.width /= scaling;
		paperSize.height /= scaling;
		NSRect rect = new NSRect();
		rect.width = paperSize.width;
		rect.height = paperSize.height;
		view.beginPageInRect(rect, new NSPoint());
		NSRect imageBounds = printInfo.imageablePageBounds();
		imageBounds.x /= scaling;
		imageBounds.y /= scaling;
		imageBounds.width /= scaling;
		imageBounds.height /= scaling;
		NSBezierPath.bezierPathWithRect(imageBounds).setClip();
		NSAffineTransform transform = NSAffineTransform.transform();
		transform.translateXBy(imageBounds.x, imageBounds.y);
		Point dpi = getDPI (), screenDPI = getIndependentDPI();
		transform.scaleXBy(screenDPI.x / (float)dpi.x, screenDPI.y / (float)dpi.y);
		transform.concat();
		operation.context().saveGraphicsState();
		return true;
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		operation.context().restoreGraphicsState();
		view.endPage();
	} finally {
		if (pool != null) pool.release();
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		int /*long*/ pmPrintSession = printInfo.PMPrintSession();
		int /*long*/ printer[] = new int /*long*/ [1]; 
		int /*long*/ err = OS.PMSessionGetCurrentPrinter(pmPrintSession, printer);
		
		if (err == OS.noErr) {
			int /*long*/ printSettings = printInfo.PMPrintSettings();
			short[] destType = new short[1];
			if (OS.PMSessionGetDestinationType(pmPrintSession, printSettings, destType) == OS.noErr) {
				if (destType[0] == OS.kPMDestinationPrinter) {
					PMResolution resolution =  new PMResolution();
					
					if (OS.PMPrinterGetOutputResolution(printer[0], printSettings, resolution) != OS.noErr) {
						int numberOfResolutions[] = new int[1];
						if (OS.PMPrinterGetPrinterResolutionCount(printer[0], numberOfResolutions) == OS.noErr) {
							PMResolution tempResolution = new PMResolution();
							tempResolution.hRes = tempResolution.vRes = 300.0;							
							for (int i = 1; i <= numberOfResolutions[0]; i++) {
								// PMPrinterGetIndexedPrinterResolution indexes are 1-based.
								if (OS.PMPrinterGetIndexedPrinterResolution(printer[0], i, tempResolution) == OS.noErr) {
									if (tempResolution.vRes > resolution.vRes && tempResolution.hRes > resolution.hRes) {
										resolution = tempResolution;
									}
								}
							}
						}
					}
					
					return new Point((int)resolution.hRes, (int)resolution.vRes);
				}
			}
		}
		
		return getIndependentDPI();
	} finally {
		if (pool != null) pool.release();
	}
}

Point getIndependentDPI() {
	return super.getDPI();
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSSize size = printInfo.paperSize();
		float scaling = scalingFactor();
		Point dpi = getDPI (), screenDPI = getIndependentDPI();
		return new Rectangle (0, 0, (int)((size.width * dpi.x / screenDPI.x) / scaling), (int)((size.height * dpi.y / screenDPI.y)  / scaling));
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		float scaling = scalingFactor();
		NSRect rect = printInfo.imageablePageBounds();
		Point dpi = getDPI (), screenDPI = getIndependentDPI();
		return new Rectangle(0, 0, (int)((rect.width * dpi.x / screenDPI.x) / scaling), (int)((rect.height * dpi.y / screenDPI.y) / scaling));
	} finally {
		if (pool != null) pool.release();
	}
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
}
