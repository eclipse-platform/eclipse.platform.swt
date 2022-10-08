/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.printing;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class allow the user to select
 * a printer and various print-related parameters
 * prior to starting a print job.
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#printing">Printing snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class PrintDialog extends Dialog {
	PrinterData printerData = new PrinterData();

	long handle;
	int index;
	byte [] settingsData;

	static final String GET_MODAL_DIALOG = "org.eclipse.swt.internal.gtk.getModalDialog";
	static final String SET_MODAL_DIALOG = "org.eclipse.swt.internal.gtk.setModalDialog";
	static final String ADD_IDLE_PROC_KEY = "org.eclipse.swt.internal.gtk.addIdleProc";
	static final String REMOVE_IDLE_PROC_KEY = "org.eclipse.swt.internal.gtk.removeIdleProc";
	static final String GET_EMISSION_PROC_KEY = "org.eclipse.swt.internal.gtk.getEmissionProc";
/**
 * Constructs a new instance of this class given only its parent.
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public PrintDialog (Shell parent) {
	this (parent, SWT.PRIMARY_MODAL);
}

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public PrintDialog (Shell parent, int style) {
	super (parent, checkStyleBit (parent, style));
	checkSubclass ();
}

/**
 * Sets the printer data that will be used when the dialog
 * is opened.
 * <p>
 * Setting the printer data to null is equivalent to
 * resetting all data fields to their default values.
 * </p>
 *
 * @param data the data that will be used when the dialog is opened or null to use default data
 *
 * @since 3.4
 */
public void setPrinterData(PrinterData data) {
	if (data == null) data = new PrinterData();
	this.printerData = data;
}

/**
 * Returns the printer data that will be used when the dialog
 * is opened.
 *
 * @return the data that will be used when the dialog is opened
 *
 * @since 3.4
 */
public PrinterData getPrinterData() {
	return printerData;
}

static int checkBits (int style, int int0, int int1, int int2, int int3, int int4, int int5) {
	int mask = int0 | int1 | int2 | int3 | int4 | int5;
	if ((style & mask) == 0) style |= int0;
	if ((style & int0) != 0) style = (style & ~mask) | int0;
	if ((style & int1) != 0) style = (style & ~mask) | int1;
	if ((style & int2) != 0) style = (style & ~mask) | int2;
	if ((style & int3) != 0) style = (style & ~mask) | int3;
	if ((style & int4) != 0) style = (style & ~mask) | int4;
	if ((style & int5) != 0) style = (style & ~mask) | int5;
	return style;
}

static int checkStyleBit (Shell parent, int style) {
	int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
	if ((style & SWT.SHEET) != 0) {
		style &= ~SWT.SHEET;
		if ((style & mask) == 0) {
			style |= parent == null ? SWT.APPLICATION_MODAL : SWT.PRIMARY_MODAL;
		}
	}
	if ((style & mask) == 0) {
		style |= SWT.APPLICATION_MODAL;
	}
	style &= ~SWT.MIRRORED;
	if ((style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT)) == 0) {
		if (parent != null) {
			if ((parent.getStyle () & SWT.LEFT_TO_RIGHT) != 0) style |= SWT.LEFT_TO_RIGHT;
			if ((parent.getStyle () & SWT.RIGHT_TO_LEFT) != 0) style |= SWT.RIGHT_TO_LEFT;
		}
	}
	return checkBits (style, SWT.LEFT_TO_RIGHT, SWT.RIGHT_TO_LEFT, 0, 0, 0, 0);
}

@Override
protected void checkSubclass() {
}

/**
 * Returns the print job scope that the user selected
 * before pressing OK in the dialog. This will be one
 * of the following values:
 * <dl>
 * <dt><code>PrinterData.ALL_PAGES</code></dt>
 * <dd>Print all pages in the current document</dd>
 * <dt><code>PrinterData.PAGE_RANGE</code></dt>
 * <dd>Print the range of pages specified by startPage and endPage</dd>
 * <dt><code>PrinterData.SELECTION</code></dt>
 * <dd>Print the current selection</dd>
 * </dl>
 *
 * @return the scope setting that the user selected
 */
public int getScope() {
	return printerData.scope;
}

/**
 * Sets the scope of the print job. The user will see this
 * setting when the dialog is opened. This can have one of
 * the following values:
 * <dl>
 * <dt><code>PrinterData.ALL_PAGES</code></dt>
 * <dd>Print all pages in the current document</dd>
 * <dt><code>PrinterData.PAGE_RANGE</code></dt>
 * <dd>Print the range of pages specified by startPage and endPage</dd>
 * <dt><code>PrinterData.SELECTION</code></dt>
 * <dd>Print the current selection</dd>
 * </dl>
 *
 * @param scope the scope setting when the dialog is opened
 */
public void setScope(int scope) {
	printerData.scope = scope;
}

/**
 * Returns the start page setting that the user selected
 * before pressing OK in the dialog.
 * <p>
 * This value can be from 1 to the maximum number of pages for the platform.
 * Note that it is only valid if the scope is <code>PrinterData.PAGE_RANGE</code>.
 * </p>
 *
 * @return the start page setting that the user selected
 */
public int getStartPage() {
	return printerData.startPage;
}

/**
 * Sets the start page that the user will see when the dialog
 * is opened.
 * <p>
 * This value can be from 1 to the maximum number of pages for the platform.
 * Note that it is only valid if the scope is <code>PrinterData.PAGE_RANGE</code>.
 * </p>
 *
 * @param startPage the startPage setting when the dialog is opened
 */
public void setStartPage(int startPage) {
	printerData.startPage = startPage;
}

/**
 * Returns the end page setting that the user selected
 * before pressing OK in the dialog.
 * <p>
 * This value can be from 1 to the maximum number of pages for the platform.
 * Note that it is only valid if the scope is <code>PrinterData.PAGE_RANGE</code>.
 * </p>
 *
 * @return the end page setting that the user selected
 */
public int getEndPage() {
	return printerData.endPage;
}

/**
 * Sets the end page that the user will see when the dialog
 * is opened.
 * <p>
 * This value can be from 1 to the maximum number of pages for the platform.
 * Note that it is only valid if the scope is <code>PrinterData.PAGE_RANGE</code>.
 * </p>
 *
 * @param endPage the end page setting when the dialog is opened
 */
public void setEndPage(int endPage) {
	printerData.endPage = endPage;
}

/**
 * Returns the 'Print to file' setting that the user selected
 * before pressing OK in the dialog.
 *
 * @return the 'Print to file' setting that the user selected
 */
public boolean getPrintToFile() {
	return printerData.printToFile;
}

/**
 * Sets the 'Print to file' setting that the user will see
 * when the dialog is opened.
 *
 * @param printToFile the 'Print to file' setting when the dialog is opened
 */
public void setPrintToFile(boolean printToFile) {
	printerData.printToFile = printToFile;
}

/**
 * Makes the receiver visible and brings it to the front
 * of the display.
 *
 * @return a printer data object describing the desired print job parameters,
 *         or null if the dialog was canceled, no printers were found, or an error occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public PrinterData open() {
	byte [] titleBytes = Converter.wcsToMbcs (getText(), true);
	long topHandle = getParent().handle;
	while (topHandle != 0 && !GTK.GTK_IS_WINDOW(topHandle)) {
		topHandle = GTK.gtk_widget_get_parent(topHandle);
	}
	handle = GTK.gtk_print_unix_dialog_new(titleBytes, topHandle);

	GTK.gtk_print_unix_dialog_set_support_selection(handle, true);
	GTK.gtk_print_unix_dialog_set_has_selection(handle, true);

	GTK.gtk_print_unix_dialog_set_manual_capabilities(handle,
		GTK.GTK_PRINT_CAPABILITY_COLLATE | GTK.GTK_PRINT_CAPABILITY_COPIES | GTK.GTK_PRINT_CAPABILITY_PAGE_SET);

	/* Set state into print dialog settings. */
	long settings = GTK.gtk_print_settings_new();
	long page_setup = GTK.gtk_page_setup_new();

	if (printerData.otherData != null) {
		Printer.restore(printerData.otherData, settings, page_setup);
	}

	/* Set values of print_settings and page_setup from PrinterData. */
	String printerName = printerData.name;
	if (printerName == null && printerData.printToFile) {
		/* Find the printer name corresponding to the file backend. */
		long printer = Printer.gtkPrinterFromPrinterData(printerData);
		if (printer != 0) {
			PrinterData data = Printer.printerDataFromGtkPrinter(printer);
			printerName = data.name;
			OS.g_object_unref(printer);
		}
	}
	if (printerName != null) {
		byte [] nameBytes = Converter.wcsToMbcs (printerName, true);
		GTK.gtk_print_settings_set_printer(settings, nameBytes);
	}

	switch (printerData.scope) {
		case PrinterData.ALL_PAGES:
			GTK.gtk_print_settings_set_print_pages(settings, GTK.GTK_PRINT_PAGES_ALL);
			break;
		case PrinterData.PAGE_RANGE:
			GTK.gtk_print_settings_set_print_pages(settings, GTK.GTK_PRINT_PAGES_RANGES);
			int [] pageRange = new int[2];
			pageRange[0] = printerData.startPage - 1;
			pageRange[1] = printerData.endPage - 1;
			GTK.gtk_print_settings_set_page_ranges(settings, pageRange, 1);
			break;
	}
	if ((printerData.printToFile || Printer.GTK_FILE_BACKEND.equals(printerData.driver)) && printerData.fileName != null) {
		// TODO: GTK_FILE_BACKEND is not GTK API (see gtk bug 345590)
		byte [] uri = Printer.uriFromFilename(printerData.fileName);
		if (uri != null) {
			GTK.gtk_print_settings_set(settings, GTK.GTK_PRINT_SETTINGS_OUTPUT_URI, uri);
		}
	}
	GTK.gtk_print_settings_set_n_copies(settings, printerData.copyCount);
	GTK.gtk_print_settings_set_collate(settings, printerData.collate);
	/*
	 * Bug in GTK.  The unix dialog gives priority to the value of the non-API
	 * field cups-Duplex in the print_settings (which we preserve in otherData).
	 * The fix is to manually clear cups-Duplex before setting the duplex field.
	 */
	byte [] keyBuffer = Converter.wcsToMbcs ("cups-Duplex", true);
	GTK.gtk_print_settings_set(settings, keyBuffer, (byte[]) null);
	if (printerData.duplex != SWT.DEFAULT) {
		int duplex = printerData.duplex == PrinterData.DUPLEX_LONG_EDGE ? GTK.GTK_PRINT_DUPLEX_HORIZONTAL
			: printerData.duplex == PrinterData.DUPLEX_SHORT_EDGE ? GTK.GTK_PRINT_DUPLEX_VERTICAL
			: GTK.GTK_PRINT_DUPLEX_SIMPLEX;
		GTK.gtk_print_settings_set_duplex (settings, duplex);
	}
	int orientation = printerData.orientation == PrinterData.LANDSCAPE ? GTK.GTK_PAGE_ORIENTATION_LANDSCAPE : GTK.GTK_PAGE_ORIENTATION_PORTRAIT;
	GTK.gtk_print_settings_set_orientation(settings, orientation);
	GTK.gtk_page_setup_set_orientation(page_setup, orientation);

	GTK.gtk_print_unix_dialog_set_settings(handle, settings);
	GTK.gtk_print_unix_dialog_set_page_setup(handle, page_setup);
	GTK.gtk_print_unix_dialog_set_embed_page_setup(handle, true);
	OS.g_object_unref(settings);
	OS.g_object_unref(page_setup);
	long group = GTK.gtk_window_get_group(0);
	GTK.gtk_window_group_add_window (group, handle);
	GTK.gtk_window_set_modal(handle, true);
	PrinterData data = null;
	//TODO: Handle 'Print Preview' (GTK_RESPONSE_APPLY).
	Display display = getParent() != null ? getParent().getDisplay (): Display.getCurrent ();

	int signalId = 0;
	long hookId = 0;
	if ((getStyle () & SWT.RIGHT_TO_LEFT) != 0) {
		signalId = OS.g_signal_lookup (OS.map, GTK.GTK_TYPE_WIDGET());
		hookId = OS.g_signal_add_emission_hook (signalId, 0, ((LONG) display.getData (GET_EMISSION_PROC_KEY)).value, handle, 0);
	}
	display.setData (ADD_IDLE_PROC_KEY, null);
	Object oldModal = null;
	if (GTK.gtk_window_get_modal (handle)) {
		oldModal = display.getData (GET_MODAL_DIALOG);
		display.setData (SET_MODAL_DIALOG, this);
	}
	String key = "org.eclipse.swt.internal.gtk.externalEventLoop"; //$NON-NLS-1$
	display.setData (key, Boolean.TRUE);
	display.sendPreExternalEventDispatchEvent ();
	int response = GTK3.gtk_dialog_run (handle);
	display.setData (key, Boolean.FALSE);
	display.sendPostExternalEventDispatchEvent ();
	if (GTK.gtk_window_get_modal (handle)) {
		display.setData (SET_MODAL_DIALOG, oldModal);
	}
	if ((getStyle () & SWT.RIGHT_TO_LEFT) != 0) {
		OS.g_signal_remove_emission_hook (signalId, hookId);
	}
	if (response == GTK.GTK_RESPONSE_OK) {
		long printer = GTK.gtk_print_unix_dialog_get_selected_printer(handle);
		if (printer != 0) {
			/* Get state from print dialog. */
			settings = GTK.gtk_print_unix_dialog_get_settings(handle); // must unref
			page_setup = GTK.gtk_print_unix_dialog_get_page_setup(handle); // do not unref
			data = Printer.printerDataFromGtkPrinter(printer);
			int print_pages = GTK.gtk_print_settings_get_print_pages(settings);
			switch (print_pages) {
				case GTK.GTK_PRINT_PAGES_ALL:
					data.scope = PrinterData.ALL_PAGES;
					break;
				case GTK.GTK_PRINT_PAGES_RANGES:
					data.scope = PrinterData.PAGE_RANGE;
					int[] num_ranges = new int[1];
					long page_ranges = GTK.gtk_print_settings_get_page_ranges(settings, num_ranges);
					int [] pageRange = new int[2];
					int length = num_ranges[0];
					int min = Integer.MAX_VALUE, max = 0;
					for (int i = 0; i < length; i++) {
						C.memmove(pageRange, page_ranges + i * pageRange.length * 4, pageRange.length * 4);
						min = Math.min(min, pageRange[0] + 1);
						max = Math.max(max, pageRange[1] + 1);
					}
					OS.g_free(page_ranges);
					data.startPage = min == Integer.MAX_VALUE ? 1 : min;
					data.endPage = max == 0 ? 1 : max;
					break;
				case GTK.GTK_PRINT_PAGES_CURRENT:
					data.scope = PrinterData.SELECTION;
					data.startPage = data.endPage = GTK.gtk_print_unix_dialog_get_current_page(handle);
					break;
				case GTK.GTK_PRINT_PAGES_SELECTION:
					data.scope = PrinterData.SELECTION;
					break;
			}

			data.printToFile = Printer.GTK_FILE_BACKEND.equals(data.driver); // TODO: GTK_FILE_BACKEND is not GTK API (see gtk bug 345590)
			if (data.printToFile) {
				long address = GTK.gtk_print_settings_get(settings, GTK.GTK_PRINT_SETTINGS_OUTPUT_URI);
				int length = C.strlen (address);
				byte [] buffer = new byte [length];
				C.memmove (buffer, address, length);
				data.fileName = new String (Converter.mbcsToWcs (buffer));
			}

			data.copyCount = GTK.gtk_print_settings_get_n_copies(settings);
			data.collate = GTK.gtk_print_settings_get_collate(settings);
			int duplex = GTK.gtk_print_settings_get_duplex(settings);
			data.duplex = duplex == GTK.GTK_PRINT_DUPLEX_HORIZONTAL ? PrinterData.DUPLEX_LONG_EDGE
					: duplex == GTK.GTK_PRINT_DUPLEX_VERTICAL ? PrinterData.DUPLEX_SHORT_EDGE
					: PrinterData.DUPLEX_NONE;
			data.orientation = GTK.gtk_page_setup_get_orientation(page_setup) == GTK.GTK_PAGE_ORIENTATION_LANDSCAPE ? PrinterData.LANDSCAPE : PrinterData.PORTRAIT;

			/* Save other print_settings data as key/value pairs in otherData. */
			Callback printSettingsCallback = new Callback(this, "GtkPrintSettingsFunc", 3); //$NON-NLS-1$
			long GtkPrintSettingsFunc = printSettingsCallback.getAddress();
			index = 0;
			settingsData = new byte[1024];
			GTK.gtk_print_settings_foreach (settings, GtkPrintSettingsFunc, 0);
			printSettingsCallback.dispose ();
			index++; // extra null terminator after print_settings and before page_setup

			/* Save page_setup data as key/value pairs in otherData.
			 * Note that page_setup properties must be stored and restored in the same order.
			 */
			store("orientation", GTK.gtk_page_setup_get_orientation(page_setup)); //$NON-NLS-1$
			store("top_margin", GTK.gtk_page_setup_get_top_margin(page_setup, GTK.GTK_UNIT_MM)); //$NON-NLS-1$
			store("bottom_margin", GTK.gtk_page_setup_get_bottom_margin(page_setup, GTK.GTK_UNIT_MM)); //$NON-NLS-1$
			store("left_margin", GTK.gtk_page_setup_get_left_margin(page_setup, GTK.GTK_UNIT_MM)); //$NON-NLS-1$
			store("right_margin", GTK.gtk_page_setup_get_right_margin(page_setup, GTK.GTK_UNIT_MM)); //$NON-NLS-1$
			long paper_size = GTK.gtk_page_setup_get_paper_size(page_setup); //$NON-NLS-1$
			storeBytes("paper_size_name", GTK.gtk_paper_size_get_name(paper_size)); //$NON-NLS-1$
			storeBytes("paper_size_display_name", GTK.gtk_paper_size_get_display_name(paper_size)); //$NON-NLS-1$
			storeBytes("paper_size_ppd_name", GTK.gtk_paper_size_get_ppd_name(paper_size)); //$NON-NLS-1$
			store("paper_size_width", GTK.gtk_paper_size_get_width(paper_size, GTK.GTK_UNIT_MM)); //$NON-NLS-1$
			store("paper_size_height", GTK.gtk_paper_size_get_height(paper_size, GTK.GTK_UNIT_MM)); //$NON-NLS-1$
			store("paper_size_is_custom", GTK.gtk_paper_size_is_custom(paper_size)); //$NON-NLS-1$
			data.otherData = settingsData;
			OS.g_object_unref(settings);
			printerData = data;
		}
	}
	display.setData (REMOVE_IDLE_PROC_KEY, null);
	if (GTK.GTK4) {
		GTK4.gtk_window_destroy(handle);
	} else {
		GTK3.gtk_widget_destroy(handle);
	}
	return data;
}

long GtkPrintSettingsFunc (long key, long value, long data) {
	int length = C.strlen (key);
	byte [] keyBuffer = new byte [length];
	C.memmove (keyBuffer, key, length);
	length = C.strlen (value);
	byte [] valueBuffer = new byte [length];
	C.memmove (valueBuffer, value, length);
	store(keyBuffer, valueBuffer);
	return 0;
}

void store(String key, int value) {
	store(key, String.valueOf(value));
}

void store(String key, double value) {
	store(key, String.valueOf(value));
}

void store(String key, boolean value) {
	store(key, String.valueOf(value));
}

void storeBytes(String key, long value) {
	int length = C.strlen (value);
	byte [] valueBuffer = new byte [length];
	C.memmove (valueBuffer, value, length);
	store(key.getBytes(), valueBuffer);
}

void store(String key, String value) {
	store(key.getBytes(), value.getBytes());
}

void store(byte [] key, byte [] value) {
	int length = key.length + 1 + value.length + 1;
	if (index + length + 1 > settingsData.length) {
		byte [] newData = new byte[settingsData.length + Math.max(length + 1, 1024)];
		System.arraycopy (settingsData, 0, newData, 0, settingsData.length);
		settingsData = newData;
	}
	System.arraycopy (key, 0, settingsData, index, key.length);
	index += key.length + 1; // null terminated
	System.arraycopy (value, 0, settingsData, index, value.length);
	index += value.length + 1; // null terminated
}
}
