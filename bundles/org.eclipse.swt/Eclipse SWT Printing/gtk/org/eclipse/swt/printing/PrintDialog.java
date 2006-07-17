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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class allow the user to select
 * a printer and various print-related parameters
 * prior to starting a print job.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class PrintDialog extends Dialog {
	int scope = PrinterData.ALL_PAGES;
	int startPage = 1, endPage = 1;
	boolean printToFile = false;

	int /*long*/ handle;
	int index;
	byte [] settingsData;

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
	super (parent, style);
	checkSubclass ();
}

protected void checkSubclass() {
}

/**
 * Returns the print job scope that the user selected
 * before pressing OK in the dialog. This will be one
 * of the following values:
 * <dl>
 * <dt><code>ALL_PAGES</code></dt>
 * <dd>Print all pages in the current document</dd>
 * <dt><code>PAGE_RANGE</code></dt>
 * <dd>Print the range of pages specified by startPage and endPage</dd>
 * <dt><code>SELECTION</code></dt>
 * <dd>Print the current selection</dd>
 * </dl>
 *
 * @return the scope setting that the user selected
 */
public int getScope() {
	return scope;
}

/**
 * Sets the scope of the print job. The user will see this
 * setting when the dialog is opened. This can have one of
 * the following values:
 * <dl>
 * <dt><code>ALL_PAGES</code></dt>
 * <dd>Print all pages in the current document</dd>
 * <dt><code>PAGE_RANGE</code></dt>
 * <dd>Print the range of pages specified by startPage and endPage</dd>
 * <dt><code>SELECTION</code></dt>
 * <dd>Print the current selection</dd>
 * </dl>
 *
 * @param scope the scope setting when the dialog is opened
 */
public void setScope(int scope) {
	this.scope = scope;
}

/**
 * Returns the start page setting that the user selected
 * before pressing OK in the dialog.
 * <p>
 * Note that this value is one based and only valid if the scope is
 * <code>PAGE_RANGE</code>.
 * </p>
 *
 * @return the start page setting that the user selected
 */
public int getStartPage() {
	return startPage;
}

/**
 * Sets the start page that the user will see when the dialog
 * is opened.
 * <p>
 * Note that this value is one based and only valid if the scope is
 * <code>PAGE_RANGE</code>.
 * </p>
 * 
 * @param startPage the startPage setting when the dialog is opened
 */
public void setStartPage(int startPage) {
	this.startPage = startPage;
}

/**
 * Returns the end page setting that the user selected
 * before pressing OK in the dialog.
 * <p>
 * Note that this value is one based and only valid if the scope is
 * <code>PAGE_RANGE</code>.
 * </p>
 *
 * @return the end page setting that the user selected
 */
public int getEndPage() {
	return endPage;
}

/**
 * Sets the end page that the user will see when the dialog
 * is opened.
 * <p>
 * Note that this value is one based and only valid if the scope is
 * <code>PAGE_RANGE</code>.
 * </p>
 * 
 * @param endPage the end page setting when the dialog is opened
 */
public void setEndPage(int endPage) {
	this.endPage = endPage;
}

/**
 * Returns the 'Print to file' setting that the user selected
 * before pressing OK in the dialog.
 *
 * @return the 'Print to file' setting that the user selected
 */
public boolean getPrintToFile() {
	return printToFile;
}

/**
 * Sets the 'Print to file' setting that the user will see
 * when the dialog is opened.
 *
 * @param printToFile the 'Print to file' setting when the dialog is opened
 */
public void setPrintToFile(boolean printToFile) {
	this.printToFile = printToFile;
}

/**
 * Makes the receiver visible and brings it to the front
 * of the display.
 *
 * @return a printer data object describing the desired print job parameters
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public PrinterData open() {
	if (OS.GTK_VERSION < OS.VERSION (2, 9, 0)) {
		return Printer.getDefaultPrinterData();
	} else {
		byte [] titleBytes = Converter.wcsToMbcs (null, getText(), true);
		int /*long*/ topHandle = getParent().handle;
		while (topHandle != 0 && !OS.GTK_IS_WINDOW(topHandle)) {
			topHandle = OS.gtk_widget_get_parent(topHandle);
		}
		handle = OS.gtk_print_unix_dialog_new(titleBytes, topHandle);
				
		//TODO: Not currently implemented. May need new API. For now, disable 'Current' in the dialog. (see gtk bug 344519)
		OS.gtk_print_unix_dialog_set_current_page(handle, -1);
		
		OS.gtk_print_unix_dialog_set_manual_capabilities(handle,
			OS.GTK_PRINT_CAPABILITY_COLLATE | OS.GTK_PRINT_CAPABILITY_COPIES | OS.GTK_PRINT_CAPABILITY_PAGE_SET);
		
		/* Set state into print dialog settings. */
		int /*long*/ settings = OS.gtk_print_settings_new();
		int /*long*/ page_setup = OS.gtk_page_setup_new();
		Printer.setScope(settings, scope, startPage, endPage);
		if (printToFile) {
			byte [] buffer = Converter.wcsToMbcs (null, "Print to File", true); //$NON-NLS-1$
			OS.gtk_print_settings_set_printer(settings, buffer);
		}
		OS.gtk_print_unix_dialog_set_settings(handle, settings);
		OS.gtk_print_unix_dialog_set_page_setup(handle, page_setup);
		OS.g_object_unref(settings);
		OS.g_object_unref(page_setup);

		PrinterData data = null;
		//TODO: Handle 'Print Preview' (GTK_RESPONSE_APPLY).
		if (OS.gtk_dialog_run (handle) == OS.GTK_RESPONSE_OK) {
			int /*long*/ printer = OS.gtk_print_unix_dialog_get_selected_printer(handle);
			if (printer != 0) {
				/* Get state from print dialog. */
				settings = OS.gtk_print_unix_dialog_get_settings(handle); // must unref
				page_setup = OS.gtk_print_unix_dialog_get_page_setup(handle); // do not unref
				data = Printer.printerDataFromGtkPrinter(printer);
				int print_pages = OS.gtk_print_settings_get_print_pages(settings);
				switch (print_pages) {
					case OS.GTK_PRINT_PAGES_ALL:
						scope = PrinterData.ALL_PAGES;
						break;
					case OS.GTK_PRINT_PAGES_RANGES:
						scope = PrinterData.PAGE_RANGE;
						int[] num_ranges = new int[1];
						int /*long*/ page_ranges = OS.gtk_print_settings_get_page_ranges(settings, num_ranges);
						int [] pageRange = new int[2];
						int length = num_ranges[0];
						int min = Integer.MAX_VALUE, max = 0;
						for (int i = 0; i < length; i++) {
							OS.memmove(pageRange, page_ranges + i * pageRange.length * 4, pageRange.length * 4);
							min = Math.min(min, pageRange[0] + 1);
							max = Math.max(max, pageRange[1] + 1);
						}
						OS.g_free(page_ranges);
						startPage = min == Integer.MAX_VALUE ? 1 : min;
						endPage = max == 0 ? 1 : max;
						break;
					case OS.GTK_PRINT_PAGES_CURRENT:
						//TODO: Disabled in dialog (see above). This code will not run. (see gtk bug 344519)
						scope = PrinterData.SELECTION;
						startPage = endPage = OS.gtk_print_unix_dialog_get_current_page(handle);
						break;
				}
				
				printToFile = data.name.equals("Print to File"); //$NON-NLS-1$
				if (printToFile) {
					int /*long*/ address = OS.gtk_print_settings_get(settings, OS.GTK_PRINT_SETTINGS_OUTPUT_URI);
					int length = OS.strlen (address);
					byte [] buffer = new byte [length];
					OS.memmove (buffer, address, length);
					data.fileName = new String (Converter.mbcsToWcs (null, buffer));
				}

				data.scope = scope;
				data.startPage = startPage;
				data.endPage = endPage;
				data.printToFile = printToFile;
				data.copyCount = OS.gtk_print_settings_get_n_copies(settings);
				data.collate = OS.gtk_print_settings_get_collate(settings);

				/* Save other print_settings data as key/value pairs in otherData. */
				Callback printSettingsCallback = new Callback(this, "GtkPrintSettingsFunc", 3); //$NON-NLS-1$
				int /*long*/ GtkPrintSettingsFunc = printSettingsCallback.getAddress();
				if (GtkPrintSettingsFunc == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
				index = 0;
				settingsData = new byte[1024];
				OS.gtk_print_settings_foreach (settings, GtkPrintSettingsFunc, 0);
				printSettingsCallback.dispose ();
				index++; // extra null terminator after print_settings and before page_setup

				/* Save page_setup data as key/value pairs in otherData.
				 * Note that page_setup properties must be stored and restored in the same order.
				 */
				store("orientation", OS.gtk_page_setup_get_orientation(page_setup)); //$NON-NLS-1$
				store("top_margin", OS.gtk_page_setup_get_top_margin(page_setup, OS.GTK_UNIT_MM)); //$NON-NLS-1$
				store("bottom_margin", OS.gtk_page_setup_get_bottom_margin(page_setup, OS.GTK_UNIT_MM)); //$NON-NLS-1$
				store("left_margin", OS.gtk_page_setup_get_left_margin(page_setup, OS.GTK_UNIT_MM)); //$NON-NLS-1$
				store("right_margin", OS.gtk_page_setup_get_right_margin(page_setup, OS.GTK_UNIT_MM)); //$NON-NLS-1$
				int /*long*/ paper_size = OS.gtk_page_setup_get_paper_size(page_setup); //$NON-NLS-1$
				storeBytes("paper_size_name", OS.gtk_paper_size_get_name(paper_size)); //$NON-NLS-1$
				storeBytes("paper_size_display_name", OS.gtk_paper_size_get_display_name(paper_size)); //$NON-NLS-1$
				storeBytes("paper_size_ppd_name", OS.gtk_paper_size_get_ppd_name(paper_size)); //$NON-NLS-1$
				store("paper_size_width", OS.gtk_paper_size_get_width(paper_size, OS.GTK_UNIT_MM)); //$NON-NLS-1$
				store("paper_size_height", OS.gtk_paper_size_get_height(paper_size, OS.GTK_UNIT_MM)); //$NON-NLS-1$
				store("paper_size_is_custom", OS.gtk_paper_size_is_custom(paper_size)); //$NON-NLS-1$
				data.otherData = settingsData;
				OS.g_object_unref(settings);
			}
		}
		OS.gtk_widget_destroy (handle);
		return data;
	}
}

int /*long*/ GtkPrintSettingsFunc (int /*long*/ key, int /*long*/ value, int /*long*/ data) {
	int length = OS.strlen (key);
	byte [] keyBuffer = new byte [length];
	OS.memmove (keyBuffer, key, length);
	length = OS.strlen (value);
	byte [] valueBuffer = new byte [length];
	OS.memmove (valueBuffer, value, length);
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

void storeBytes(String key, int /*long*/ value) {
	int length = OS.strlen (value);
	byte [] valueBuffer = new byte [length];
	OS.memmove (valueBuffer, value, length);
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
