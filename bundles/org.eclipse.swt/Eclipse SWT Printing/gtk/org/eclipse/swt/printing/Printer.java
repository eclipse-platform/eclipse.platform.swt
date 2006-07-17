/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.gtk.OS;
import org.eclipse.swt.internal.cairo.Cairo;

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
	static PrinterData [] printerList;
	
	PrinterData data;
	int /*long*/ printer;
	int /*long*/ printJob;
	int /*long*/ settings;
	int /*long*/ pageSetup;
	int /*long*/ surface;
	int /*long*/ cairo;
	
	/**
	 * whether or not a GC was created for this printer
	 */
	boolean isGCCreated = false;
	Font systemFont;

	byte [] settingsData;
	int start, end;


/**
 * Returns an array of <code>PrinterData</code> objects
 * representing all available printers.
 *
 * @return the list of available printers
 */
public static PrinterData[] getPrinterList() {
	printerList = new PrinterData [0];
	Callback printerCallback = new Callback(Printer.class, "GtkPrinterFunc_List", 2); //$NON-NLS-1$
	int /*long*/ GtkPrinterFunc_List = printerCallback.getAddress();
	if (GtkPrinterFunc_List == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	OS.gtk_enumerate_printers(GtkPrinterFunc_List, 0, 0, true);
	printerCallback.dispose ();
	return printerList;
}

static int /*long*/ GtkPrinterFunc_List (int /*long*/ printer, int /*long*/ user_data) {
	int length = printerList.length;
	PrinterData [] newList = new PrinterData [length + 1];
	System.arraycopy (printerList, 0, newList, 0, length);
	printerList = newList;
	printerList [length] = printerDataFromGtkPrinter(printer);
	return 0;
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
	printerList = new PrinterData [1];
	Callback printerCallback = new Callback(Printer.class, "GtkPrinterFunc_Default", 2); //$NON-NLS-1$
	int /*long*/ GtkPrinterFunc_Default = printerCallback.getAddress();
	if (GtkPrinterFunc_Default == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	OS.gtk_enumerate_printers(GtkPrinterFunc_Default, 0, 0, true);
	printerCallback.dispose ();
	return printerList[0];
}

static int /*long*/ GtkPrinterFunc_Default (int /*long*/ printer, int /*long*/ user_data) {
	if (OS.gtk_printer_is_default(printer)) {
		printerList[0] = printerDataFromGtkPrinter(printer);
		return 1;
	}
	return 0;
}

int /*long*/ gtkPrinterFromPrinterData() {
	Callback printerCallback = new Callback(this, "GtkPrinterFunc_FindNamedPrinter", 2); //$NON-NLS-1$
	int /*long*/ GtkPrinterFunc_FindNamedPrinter = printerCallback.getAddress();
	if (GtkPrinterFunc_FindNamedPrinter == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	printer = 0;
	OS.gtk_enumerate_printers(GtkPrinterFunc_FindNamedPrinter, 0, 0, true);
	printerCallback.dispose ();
	return printer;
}

int /*long*/ GtkPrinterFunc_FindNamedPrinter (int /*long*/ printer, int /*long*/ user_data) {
	PrinterData pd = printerDataFromGtkPrinter(printer);
	if (pd.driver.equals(data.driver) && pd.name.equals(data.name)) {
		this.printer = printer;
		OS.g_object_ref(printer);
		return 1;
	}
	return 0;
}

static PrinterData printerDataFromGtkPrinter(int /*long*/ printer) {
	int /*long*/ backend = OS.gtk_printer_get_backend(printer);
	int /*long*/ address = OS.G_OBJECT_TYPE_NAME(backend);
	int length = OS.strlen (address);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, address, length);
	String backendType = new String (Converter.mbcsToWcs (null, buffer));
	
	address = OS.gtk_printer_get_name (printer);
	length = OS.strlen (address);
	buffer = new byte [length];
	OS.memmove (buffer, address, length);
	String name = new String (Converter.mbcsToWcs (null, buffer));

	return new PrinterData (backendType, name);
}

static void setScope(int /*long*/ settings, int scope, int startPage, int endPage) {
	switch (scope) {
	case PrinterData.ALL_PAGES:
		OS.gtk_print_settings_set_print_pages(settings, OS.GTK_PRINT_PAGES_ALL);
		break;
	case PrinterData.PAGE_RANGE:
		OS.gtk_print_settings_set_print_pages(settings, OS.GTK_PRINT_PAGES_RANGES);
		int [] pageRange = new int[2];
		pageRange[0] = startPage - 1;
		pageRange[1] = endPage - 1;
		OS.gtk_print_settings_set_page_ranges(settings, pageRange, 1);
		break;
	case PrinterData.SELECTION:
		//TODO: Not correctly implemented. May need new API. For now, set to ALL. (see gtk bug 344519)
		OS.gtk_print_settings_set_print_pages(settings, OS.GTK_PRINT_PAGES_ALL);
		break;
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
 * Constructs a new printer representing the default printer.
 * <p>
 * You must dispose the printer when it is no longer required. 
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
 * object representing the desired printer.
 * <p>
 * You must dispose the printer when it is no longer required. 
 * </p>
 *
 * @param data the printer data for the specified printer
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

int restoreInt(String key) {
	byte [] value = restoreBytes(key, false);
	return Integer.parseInt(new String(value));
}

double restoreDouble(String key) {
	byte [] value = restoreBytes(key, false);
	return Double.parseDouble(new String(value));
}

boolean restoreBoolean(String key) {
	byte [] value = restoreBytes(key, false);
	return Boolean.valueOf(new String(value)).booleanValue();
}

byte [] restoreBytes(String key, boolean nullTerminate) {
	//get key
	start = end;
	while (end < settingsData.length && settingsData[end] != 0) end++;
	end++;
	byte [] keyBuffer = new byte [end - start];
	System.arraycopy (settingsData, start, keyBuffer, 0, keyBuffer.length);
	
	//get value
	start = end;
	while (end < settingsData.length && settingsData[end] != 0) end++;
	int length = end - start;
	end++;
	if (nullTerminate) length++;
	byte [] valueBuffer = new byte [length];
	System.arraycopy (settingsData, start, valueBuffer, 0, length);
	
	if (DEBUG) System.out.println(new String (Converter.mbcsToWcs (null, keyBuffer))+": "+new String (Converter.mbcsToWcs (null, valueBuffer)));

	return valueBuffer;
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
 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Font getSystemFont () {
	checkDevice ();
	if (systemFont != null) return systemFont;
	int /*long*/ style = OS.gtk_widget_get_default_style();	
	int /*long*/ defaultFont = OS.pango_font_description_copy (OS.gtk_style_get_font_desc (style));
	return systemFont = Font.gtk_new (this, defaultFont);
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
public int /*long*/ internal_new_GC(GCData data) {
	int /*long*/ drawable = OS.gdk_pixmap_new(OS.GDK_ROOT_PARENT(), 1, 1, 1);
	int /*long*/ gdkGC = OS.gdk_gc_new (drawable);
	if (gdkGC == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	if (data != null) {
		if (isGCCreated) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		if ((data.style & mask) == 0) {
			data.style |= SWT.LEFT_TO_RIGHT;
		}
		data.device = this;
		data.drawable = drawable;
		data.background = getSystemColor (SWT.COLOR_WHITE).handle;
		data.foreground = getSystemColor (SWT.COLOR_BLACK).handle;
		data.font = getSystemFont ().handle;
		if (cairo == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		data.cairo = cairo;
		isGCCreated = true;
	}
	return gdkGC;
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
 */
public void internal_dispose_GC(int /*long*/ gdkGC, GCData data) {
	if (data != null) isGCCreated = false;
	OS.g_object_unref (gdkGC);
	if (data != null) {
		if (data.drawable != 0) OS.g_object_unref (data.drawable);
		data.drawable = data.cairo = 0;
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
	super.release();
	
	/* Dispose the default font */
	if (systemFont != null) systemFont.dispose ();
	systemFont = null;
}

/**
 * Starts a print job and returns true if the job started successfully
 * and false otherwise.
 * <p>
 * This must be the first method called to initiate a print job,
 * followed by any number of startPage/endPage calls, followed by
 * endJob. Calling startPage, endPage, or endJob before startJob
 * will result in undefined behavior.
 * </p><p>
 * Also, this must be called before creating any GCs on the printer.
 * Calling new GC(printer) before startJob will result in undefined
 * behavior.
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
	byte [] buffer = Converter.wcsToMbcs (null, jobName, true);
	printJob = OS.gtk_print_job_new (buffer, printer, settings, pageSetup);
	if (printJob == 0) return false;
	surface = OS.gtk_print_job_get_surface(printJob, null);
	if (surface == 0) {
		OS.g_object_unref(printJob);
		printJob = 0;
		return false;
	}
	cairo = Cairo.cairo_create(surface);
	if (cairo == 0)  {
		OS.g_object_unref(printJob);
		printJob = 0;
		return false;
	}
	return true;
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
	if (printer != 0) OS.g_object_unref (printer);
	if (settings != 0) OS.g_object_unref (settings);
	if (pageSetup != 0) OS.g_object_unref (pageSetup);
	if (cairo != 0) Cairo.cairo_destroy (cairo);
	if (printJob != 0) OS.g_object_unref (printJob);
	printer = settings = pageSetup = cairo = printJob = 0;
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
	if (printJob == 0) return;
	Cairo.cairo_surface_finish(surface);
	OS.gtk_print_job_send(printJob, 0, 0, 0);
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
	if (printJob == 0) return;
	//TODO: Need to implement (waiting on gtk bug 339323) 
	//OS.g_object_unref(printJob);
	//printJob = 0;
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
	if (printJob == 0) return false;
	double width = OS.gtk_page_setup_get_paper_width (pageSetup, OS.GTK_UNIT_POINTS);
	double height = OS.gtk_page_setup_get_paper_height (pageSetup, OS.GTK_UNIT_POINTS);
	int type = Cairo.cairo_surface_get_type (surface);
	switch (type) {
		case Cairo.CAIRO_SURFACE_TYPE_PS:
			Cairo.cairo_ps_surface_set_size (surface, width, height);
			break;
		case Cairo.CAIRO_SURFACE_TYPE_PDF:
			Cairo.cairo_pdf_surface_set_size (surface, width, height);
			break;
	}
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
	if (cairo != 0) Cairo.cairo_show_page(cairo);
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
	int resolution = OS.gtk_print_settings_get_resolution(settings);
	if (DEBUG) System.out.println("print_settings.resolution=" + resolution);
	//TODO: Return 72 (1/72 inch = 1 point) until gtk bug 346245 is fixed
	//TODO: Fix this: gtk_print_settings_get_resolution returns 0? (see gtk bug 346252)
	if (true || resolution == 0) return new Point(72, 72);
	return new Point(resolution, resolution);
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
	//TODO: We are supposed to return this in pixels, but GTK_UNIT_PIXELS is currently not implemented (gtk bug 346245)
	double width = OS.gtk_page_setup_get_paper_width (pageSetup, OS.GTK_UNIT_POINTS);
	double height = OS.gtk_page_setup_get_paper_height (pageSetup, OS.GTK_UNIT_POINTS);
	return new Rectangle(0, 0, (int) width, (int) height);
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
	//TODO: We are supposed to return this in pixels, but GTK_UNIT_PIXELS is currently not implemented (gtk bug 346245)
	double width = OS.gtk_page_setup_get_page_width(pageSetup, OS.GTK_UNIT_POINTS);
	double height = OS.gtk_page_setup_get_page_height(pageSetup, OS.GTK_UNIT_POINTS);
	return new Rectangle(0, 0, (int) width, (int) height);
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
 * @param x the desired x coordinate of the client area
 * @param y the desired y coordinate of the client area
 * @param width the desired width of the client area
 * @param height the desired height of the client area
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
	//TODO: We are supposed to return this in pixels, but GTK_UNIT_PIXELS is currently not implemented (gtk bug 346245)
	double printWidth = OS.gtk_page_setup_get_page_width(pageSetup, OS.GTK_UNIT_POINTS);
	double printHeight = OS.gtk_page_setup_get_page_height(pageSetup, OS.GTK_UNIT_POINTS);
	double paperWidth = OS.gtk_page_setup_get_paper_width (pageSetup, OS.GTK_UNIT_POINTS);
	double paperHeight = OS.gtk_page_setup_get_paper_height (pageSetup, OS.GTK_UNIT_POINTS);
	double printX = -OS.gtk_page_setup_get_left_margin(pageSetup, OS.GTK_UNIT_POINTS);
	double printY = -OS.gtk_page_setup_get_top_margin(pageSetup, OS.GTK_UNIT_POINTS);
	double hTrim = paperWidth - printWidth;
	double vTrim = paperHeight - printHeight;
	return new Rectangle(x + (int)printX, y + (int)printY, width + (int)hTrim, height + (int)vTrim);
}

/**	 
 * Creates the printer handle.
 * This method is called internally by the instance creation
 * mechanism of the <code>Device</code> class.
 * @param deviceData the device data
 */
protected void create(DeviceData deviceData) {
	this.data = (PrinterData)deviceData;
	if (OS.GTK_VERSION < OS.VERSION (2, 9, 0)) SWT.error(SWT.ERROR_NO_HANDLES);
	printer = gtkPrinterFromPrinterData();
	if (printer == 0) SWT.error(SWT.ERROR_NO_HANDLES);
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
protected void init() {
	super.init ();
	settings = OS.gtk_print_settings_new();
	pageSetup = OS.gtk_page_setup_new();
	if (data.otherData != null) {
		/* Retreive stored printer_settings data. */
		settingsData = data.otherData;
		start = end = 0;
		while (end < settingsData.length && settingsData[end] != 0) {
			start = end;
			while (end < settingsData.length && settingsData[end] != 0) end++;
			end++;
			byte [] keyBuffer = new byte [end - start];
			System.arraycopy (settingsData, start, keyBuffer, 0, keyBuffer.length);
			start = end;
			while (end < settingsData.length && settingsData[end] != 0) end++;
			end++;
			byte [] valueBuffer = new byte [end - start];
			System.arraycopy (settingsData, start, valueBuffer, 0, valueBuffer.length);
			OS.gtk_print_settings_set(settings, keyBuffer, valueBuffer);
			if (DEBUG) System.out.println(new String (Converter.mbcsToWcs (null, keyBuffer))+": "+new String (Converter.mbcsToWcs (null, valueBuffer)));
		}
		end++; // skip extra null terminator
		
		/* Retreive stored page_setup data.
		 * Note that page_setup properties must be stored (in PrintDialog) and restored (here) in the same order.
		 */
		OS.gtk_page_setup_set_orientation(pageSetup, restoreInt("orientation")); //$NON-NLS-1$
		OS.gtk_page_setup_set_top_margin(pageSetup, restoreDouble("top_margin"), OS.GTK_UNIT_MM); //$NON-NLS-1$
		OS.gtk_page_setup_set_bottom_margin(pageSetup, restoreDouble("bottom_margin"), OS.GTK_UNIT_MM); //$NON-NLS-1$
		OS.gtk_page_setup_set_left_margin(pageSetup, restoreDouble("left_margin"), OS.GTK_UNIT_MM); //$NON-NLS-1$
		OS.gtk_page_setup_set_right_margin(pageSetup, restoreDouble("right_margin"), OS.GTK_UNIT_MM); //$NON-NLS-1$
		byte [] name = restoreBytes("paper_size_name", true); //$NON-NLS-1$
		byte [] display_name = restoreBytes("paper_size_display_name", true); //$NON-NLS-1$
		byte [] ppd_name = restoreBytes("paper_size_ppd_name", true); //$NON-NLS-1$
		double width = restoreDouble("paper_size_width"); //$NON-NLS-1$
		double height = restoreDouble("paper_size_height"); //$NON-NLS-1$
		boolean custom = restoreBoolean("paper_size_is_custom"); //$NON-NLS-1$
		int /*long*/ paper_size = 0;
		if (custom) {
			if (ppd_name.length > 0) {
				paper_size = OS.gtk_paper_size_new_from_ppd(ppd_name, display_name, width, height);
			} else {
				paper_size = OS.gtk_paper_size_new_custom(name, display_name, width, height, OS.GTK_UNIT_MM);
			}
		} else {
			paper_size = OS.gtk_paper_size_new(name);
		}
		OS.gtk_page_setup_set_paper_size(pageSetup, paper_size);
		OS.g_free(paper_size);
	}
	
	/* Set values of settings from PrinterData. */
	setScope(settings, data.scope, data.startPage, data.endPage);
	//TODO: Should we look at printToFile, or driver/name for "Print to File", or both? (see gtk bug 345590)
	if (data.printToFile) {
		byte [] buffer = Converter.wcsToMbcs (null, data.fileName, true);
		OS.gtk_print_settings_set(settings, OS.GTK_PRINT_SETTINGS_OUTPUT_URI, buffer);
	}
	if (data.driver.equals("GtkPrintBackendFile") && data.name.equals("Print to File")) { //$NON-NLS-1$ //$NON-NLS-2$
		byte [] buffer = Converter.wcsToMbcs (null, data.fileName, true);
		OS.gtk_print_settings_set(settings, OS.GTK_PRINT_SETTINGS_OUTPUT_URI, buffer);
	}
	OS.gtk_print_settings_set_n_copies(settings, data.copyCount);
	OS.gtk_print_settings_set_collate(settings, data.collate);
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
