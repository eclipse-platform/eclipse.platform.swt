/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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
import org.eclipse.swt.internal.gtk.GdkVisual;
import org.eclipse.swt.internal.gtk.OS;
import org.eclipse.swt.internal.cairo.Cairo;
import org.eclipse.swt.printing.PrinterData;

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
	static PrinterData [] printerList;
	static int /*long*/ findPrinter;
	static PrinterData findData;
	
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

	static byte [] settingsData;
	static int start, end;

	static final String GTK_LPR_BACKEND = "GtkPrintBackendLpr"; //$NON-NLS-1$
	static final String GTK_FILE_BACKEND = "GtkPrintBackendFile"; //$NON-NLS-1$

	static boolean disablePrinting = System.getProperty("org.eclipse.swt.internal.gtk.disablePrinting") != null; //$NON-NLS-1$
	
static void gtk_init() {
	if (!OS.g_thread_supported ()) {
		OS.g_thread_init (0);
	}
	OS.gtk_set_locale();
	if (!OS.gtk_init_check (new int /*long*/ [] {0}, null)) {
		SWT.error (SWT.ERROR_NO_HANDLES, null, " [gtk_init_check() failed]");
	}
}

/**
 * Returns an array of <code>PrinterData</code> objects
 * representing all available printers.  If there are no
 * printers, the array will be empty.
 *
 * @return an array of PrinterData objects representing the available printers
 */
public static PrinterData[] getPrinterList() {
	printerList = new PrinterData [0];
	if (OS.GTK_VERSION < OS.VERSION (2, 10, 0) || disablePrinting) {
		return printerList;
	}
	gtk_init();
	Callback printerCallback = new Callback(Printer.class, "GtkPrinterFunc_List", 2); //$NON-NLS-1$
	int /*long*/ GtkPrinterFunc_List = printerCallback.getAddress();
	if (GtkPrinterFunc_List == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	OS.gtk_enumerate_printers(GtkPrinterFunc_List, 0, 0, true);
	/*
	* This call to gdk_threads_leave() is a temporary work around
	* to avoid deadlocks when gdk_threads_init() is called by native
	* code outside of SWT (i.e AWT, etc). It ensures that the current
	* thread leaves the GTK lock acquired by the function above. 
	*/
	OS.gdk_threads_leave();
	printerCallback.dispose ();
	return printerList;
}

static int /*long*/ GtkPrinterFunc_List (int /*long*/ printer, int /*long*/ user_data) {
	int length = printerList.length;
	PrinterData [] newList = new PrinterData [length + 1];
	System.arraycopy (printerList, 0, newList, 0, length);
	printerList = newList;
	printerList [length] = printerDataFromGtkPrinter(printer);
	/*
	* Bug in GTK. While performing a gtk_enumerate_printers(), GTK finds all of the 
	* available printers from each backend and can hang. If a backend requires more 
	* time to gather printer info, GTK will start an event loop waiting for a done 
    * signal before continuing. For the Lpr backend, GTK does not send a done signal
    * which means the event loop never ends. The fix is to check to see if the driver
    * is of type Lpr, and stop the enumeration, which exits the event loop.
	*/
	if (printerList[length].driver.equals (GTK_LPR_BACKEND)) return 1;
	return 0;
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
	findData = null;
	if (OS.GTK_VERSION < OS.VERSION (2, 10, 0) || disablePrinting) {
		return null;
	}
	gtk_init();
	Callback printerCallback = new Callback(Printer.class, "GtkPrinterFunc_Default", 2); //$NON-NLS-1$
	int /*long*/ GtkPrinterFunc_Default = printerCallback.getAddress();
	if (GtkPrinterFunc_Default == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	OS.gtk_enumerate_printers(GtkPrinterFunc_Default, 0, 0, true);
	/*
	* This call to gdk_threads_leave() is a temporary work around
	* to avoid deadlocks when gdk_threads_init() is called by native
	* code outside of SWT (i.e AWT, etc). It ensures that the current
	* thread leaves the GTK lock acquired by the function above. 
	*/
	OS.gdk_threads_leave();
	printerCallback.dispose ();
	return findData;
}

static int /*long*/ GtkPrinterFunc_Default (int /*long*/ printer, int /*long*/ user_data) {
	if (OS.gtk_printer_is_default(printer)) {
		findData = printerDataFromGtkPrinter(printer);
		return 1;
	} else if (OS.GTK_VERSION < OS.VERSION(2, 10, 12) && printerDataFromGtkPrinter(printer).driver.equals (GTK_LPR_BACKEND)) { 
		return 1;
	}
	return 0;
}

static int /*long*/ gtkPrinterFromPrinterData(PrinterData data) {
	gtk_init();
	Callback printerCallback = new Callback(Printer.class, "GtkPrinterFunc_FindNamedPrinter", 2); //$NON-NLS-1$
	int /*long*/ GtkPrinterFunc_FindNamedPrinter = printerCallback.getAddress();
	if (GtkPrinterFunc_FindNamedPrinter == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	findPrinter = 0;
	findData = data;
	OS.gtk_enumerate_printers(GtkPrinterFunc_FindNamedPrinter, 0, 0, true);
	/*
	* This call to gdk_threads_leave() is a temporary work around
	* to avoid deadlocks when gdk_threads_init() is called by native
	* code outside of SWT (i.e AWT, etc). It ensures that the current
	* thread leaves the GTK lock acquired by the function above. 
	*/
	OS.gdk_threads_leave();
	printerCallback.dispose ();
	return findPrinter;
}

static int /*long*/ GtkPrinterFunc_FindNamedPrinter (int /*long*/ printer, int /*long*/ user_data) {
	PrinterData pd = printerDataFromGtkPrinter(printer);
	if ((pd.driver.equals(findData.driver) && pd.name.equals(findData.name))
			|| (pd.driver.equals(GTK_FILE_BACKEND)) && findData.printToFile && findData.driver == null && findData.name == null) {
			// TODO: GTK_FILE_BACKEND is not GTK API (see gtk bug 345590)
		findPrinter = printer;
		OS.g_object_ref(printer);
		return 1;
	} else if (OS.GTK_VERSION < OS.VERSION (2, 10, 12) && pd.driver.equals(GTK_LPR_BACKEND)) {
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

/* 
* Restore printer settings and page_setup data from data.
*/
static void restore(byte [] data, int /*long*/ settings, int /*long*/ page_setup) {
	settingsData = data;
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
	
	/* Retrieve stored page_setup data.
	 * Note that page_setup properties must be stored (in PrintDialog) and restored (here) in the same order.
	 */
	OS.gtk_page_setup_set_orientation(page_setup, restoreInt("orientation")); //$NON-NLS-1$
	OS.gtk_page_setup_set_top_margin(page_setup, restoreDouble("top_margin"), OS.GTK_UNIT_MM); //$NON-NLS-1$
	OS.gtk_page_setup_set_bottom_margin(page_setup, restoreDouble("bottom_margin"), OS.GTK_UNIT_MM); //$NON-NLS-1$
	OS.gtk_page_setup_set_left_margin(page_setup, restoreDouble("left_margin"), OS.GTK_UNIT_MM); //$NON-NLS-1$
	OS.gtk_page_setup_set_right_margin(page_setup, restoreDouble("right_margin"), OS.GTK_UNIT_MM); //$NON-NLS-1$
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
	OS.gtk_page_setup_set_paper_size(page_setup, paper_size);
	OS.gtk_paper_size_free(paper_size);
}

static byte [] uriFromFilename(String filename) {
	if (filename == null) return null;
	int length = filename.length();
	if (length == 0) return null;
	char[] chars = new char[length];
	filename.getChars(0, length, chars, 0);		
	int /*long*/[] error = new int /*long*/[1];
	int /*long*/ utf8Ptr = OS.g_utf16_to_utf8(chars, chars.length, null, null, error);
	if (error[0] != 0 || utf8Ptr == 0) return null;
	int /*long*/ localePtr = OS.g_filename_from_utf8(utf8Ptr, -1, null, null, error);
	OS.g_free(utf8Ptr);
	if (error[0] != 0 || localePtr == 0) return null;
	int /*long*/ uriPtr = OS.g_filename_to_uri(localePtr, 0, error);
	OS.g_free(localePtr);
	if (error[0] != 0 || uriPtr == 0) return null;
	length = OS.strlen(uriPtr);
	byte[] uri = new byte[length + 1];
	OS.memmove (uri, uriPtr, length);
	OS.g_free(uriPtr);
	return uri;
}

static DeviceData checkNull (PrinterData data) {
	if (data == null) data = new PrinterData();
	if (data.driver == null || data.name == null) {
		PrinterData defaultData = null;
		if (data.printToFile) {
			int /*long*/ filePrinter = gtkPrinterFromPrinterData(data);
			if (filePrinter != 0) {
				defaultData = printerDataFromGtkPrinter(filePrinter);
				OS.g_object_unref(filePrinter);
			}
		}
		if (defaultData == null) {
			defaultData = getDefaultPrinterData();
			if (defaultData == null) SWT.error(SWT.ERROR_NO_HANDLES);
		}
		data.driver = defaultData.driver;
		data.name = defaultData.name;		
	}
	return data;
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
	super(checkNull(data));
}

static int restoreInt(String key) {
	byte [] value = restoreBytes(key, false);
	return Integer.parseInt(new String(value));
}

static double restoreDouble(String key) {
	byte [] value = restoreBytes(key, false);
	return Double.parseDouble(new String(value));
}

static boolean restoreBoolean(String key) {
	byte [] value = restoreBytes(key, false);
	return Boolean.valueOf(new String(value)).booleanValue();
}

static byte [] restoreBytes(String key, boolean nullTerminate) {
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
	int size = OS.pango_font_description_get_size(defaultFont);
	Point dpi = getDPI(), screenDPI = super.getDPI();
	OS.pango_font_description_set_size(defaultFont, size * dpi.y / screenDPI.y);
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
 * 
 * @noreference This method is not intended to be referenced by clients.
 */
public int /*long*/ internal_new_GC(GCData data) {
	int /*long*/ gc, drawable = 0;
	if (OS.USE_CAIRO) {
		gc = cairo;
	} else {
		GdkVisual visual = new GdkVisual ();
		OS.memmove (visual, OS.gdk_visual_get_system());
		drawable = OS.gdk_pixmap_new(OS.GDK_ROOT_PARENT(), 1, 1, visual.depth);
		gc = OS.gdk_gc_new (drawable);
	}
	if (gc == 0) SWT.error (SWT.ERROR_NO_HANDLES);
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
		data.font = getSystemFont ();
		Point dpi = getDPI(), screenDPI = getIndependentDPI();
		data.width = (int)(OS.gtk_page_setup_get_paper_width (pageSetup, OS.GTK_UNIT_POINTS) * dpi.x / screenDPI.x);
		data.height = (int)(OS.gtk_page_setup_get_paper_height (pageSetup, OS.GTK_UNIT_POINTS) * dpi.y / screenDPI.y);
		if (cairo == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		Cairo.cairo_identity_matrix(cairo);
		double printX = OS.gtk_page_setup_get_left_margin(pageSetup, OS.GTK_UNIT_POINTS);
		double printY = OS.gtk_page_setup_get_top_margin(pageSetup, OS.GTK_UNIT_POINTS);
		Cairo.cairo_translate(cairo, printX, printY);
		Cairo.cairo_scale(cairo, screenDPI.x / (float)dpi.x, screenDPI.y / (float)dpi.y);
		double[] matrix = new double[6];
		Cairo.cairo_get_matrix(cairo, matrix);
		data.identity = matrix;
		data.cairo = cairo;
		isGCCreated = true;
	}
	return gc;
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
public void internal_dispose_GC(int /*long*/ gc, GCData data) {
	if (data != null) isGCCreated = false;
	if (OS.USE_CAIRO) return;
	OS.g_object_unref (gc);
	if (data != null) {
		if (data.drawable != 0) OS.g_object_unref (data.drawable);
		data.drawable = data.cairo = 0;
	}
}

/**	 
 * Releases any internal state prior to destroying this printer.
 * This method is called internally by the dispose
 * mechanism of the <code>Device</code> class.
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
 * Destroys the printer handle.
 * This method is called internally by the dispose
 * mechanism of the <code>Device</code> class.
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
	OS.g_object_unref(printJob);
	printJob = 0;
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
	Cairo.cairo_surface_finish(surface);
	OS.g_object_unref(printJob);
	printJob = 0;
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
	//TODO: use new api for get x resolution and get y resolution
	if (resolution == 0) return new Point(72, 72);
	return new Point(resolution, resolution);
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
	Point dpi = getDPI(), screenDPI = getIndependentDPI();
	double width = OS.gtk_page_setup_get_paper_width (pageSetup, OS.GTK_UNIT_POINTS) * dpi.x / screenDPI.x;
	double height = OS.gtk_page_setup_get_paper_height (pageSetup, OS.GTK_UNIT_POINTS) * dpi.y / screenDPI.y;
	return new Rectangle(0, 0, (int) width, (int) height);
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
	Point dpi = getDPI(), screenDPI = getIndependentDPI();
	double width = OS.gtk_page_setup_get_page_width(pageSetup, OS.GTK_UNIT_POINTS) * dpi.x / screenDPI.x;
	double height = OS.gtk_page_setup_get_page_height(pageSetup, OS.GTK_UNIT_POINTS) * dpi.y / screenDPI.y;
	return new Rectangle(0, 0, (int) width, (int) height);
}

Point getIndependentDPI () {
	return new Point(72, 72);
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
	Point dpi = getDPI(), screenDPI = getIndependentDPI();
	double printWidth = OS.gtk_page_setup_get_page_width(pageSetup, OS.GTK_UNIT_POINTS) * dpi.x / screenDPI.x;
	double printHeight = OS.gtk_page_setup_get_page_height(pageSetup, OS.GTK_UNIT_POINTS) * dpi.y / screenDPI.y;
	double paperWidth = OS.gtk_page_setup_get_paper_width (pageSetup, OS.GTK_UNIT_POINTS) * dpi.x / screenDPI.x;
	double paperHeight = OS.gtk_page_setup_get_paper_height (pageSetup, OS.GTK_UNIT_POINTS) * dpi.y / screenDPI.y;
	double printX = -OS.gtk_page_setup_get_left_margin(pageSetup, OS.GTK_UNIT_POINTS) * dpi.x / screenDPI.x;
	double printY = -OS.gtk_page_setup_get_top_margin(pageSetup, OS.GTK_UNIT_POINTS) * dpi.y / screenDPI.y;
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
	if (OS.GTK_VERSION < OS.VERSION (2, 10, 0) || disablePrinting) SWT.error(SWT.ERROR_NO_HANDLES);
	printer = gtkPrinterFromPrinterData(data);
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
	settings = OS.gtk_print_settings_new();
	pageSetup = OS.gtk_page_setup_new();
	if (data.otherData != null) {
		restore(data.otherData, settings, pageSetup);
	}
	
	/* Set values of print_settings and page_setup from PrinterData. */
	if (data.printToFile && data.fileName != null) {
		byte [] uri = uriFromFilename(data.fileName);
		if (uri != null) {
			OS.gtk_print_settings_set(settings, OS.GTK_PRINT_SETTINGS_OUTPUT_URI, uri);
		}
	}
	OS.gtk_print_settings_set_n_copies(settings, data.copyCount);
	OS.gtk_print_settings_set_collate(settings, data.collate);
	if (data.duplex != SWT.DEFAULT) {
		int duplex = data.duplex == PrinterData.DUPLEX_LONG_EDGE ? OS.GTK_PRINT_DUPLEX_HORIZONTAL
			: data.duplex == PrinterData.DUPLEX_SHORT_EDGE ? OS.GTK_PRINT_DUPLEX_VERTICAL
			: OS.GTK_PRINT_DUPLEX_SIMPLEX;
		OS.gtk_print_settings_set_duplex (settings, duplex);
		/* 
		 * Bug in GTK.  The cups backend only looks at the value
		 * of the non-API field cups-Duplex in the print_settings.
		 * The fix is to manually set cups-Duplex to Tumble or NoTumble.
		 */
		String cupsDuplexType = null;
		if (duplex == OS.GTK_PRINT_DUPLEX_HORIZONTAL) cupsDuplexType = "DuplexNoTumble";
		else if (duplex == OS.GTK_PRINT_DUPLEX_VERTICAL) cupsDuplexType = "DuplexTumble";
		if (cupsDuplexType != null) {
			byte [] keyBuffer = Converter.wcsToMbcs (null, "cups-Duplex", true);
			byte [] valueBuffer = Converter.wcsToMbcs (null, cupsDuplexType, true);
			OS.gtk_print_settings_set(settings, keyBuffer, valueBuffer);
		}
	}
	int orientation = data.orientation == PrinterData.LANDSCAPE ? OS.GTK_PAGE_ORIENTATION_LANDSCAPE : OS.GTK_PAGE_ORIENTATION_PORTRAIT;
	OS.gtk_page_setup_set_orientation(pageSetup, orientation);
	OS.gtk_print_settings_set_orientation(settings, orientation);
	super.init ();
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
