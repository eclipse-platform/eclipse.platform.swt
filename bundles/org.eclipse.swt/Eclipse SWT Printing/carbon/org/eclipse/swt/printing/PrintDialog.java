/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.carbon.OS;

/**
 * Instances of this class allow the user to select
 * a printer and various print-related parameters
 * prior to starting a print job.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#printing">Printing snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class PrintDialog extends Dialog {
	PrinterData printerData = new PrinterData();

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

/**
 * Sets the printer data that will be used when the dialog
 * is opened.
 * 
 * @param data the data that will be used when the dialog is opened
 * 
 * @since 3.4
 */
public void setPrinterData(PrinterData data) {
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
	int[] buffer = new int[1];
	if (OS.PMCreateSession(buffer) == OS.noErr) {
		int printSession = buffer[0];
		if (OS.PMCreatePrintSettings(buffer) == OS.noErr) {
			int printSettings = buffer[0];
			OS.PMSessionDefaultPrintSettings(printSession, printSettings);
			if (OS.PMCreatePageFormat(buffer) == OS.noErr) {
				int pageFormat = buffer[0];
				OS.PMSessionDefaultPageFormat(printSession, pageFormat);
				OS.PMSessionSetDestination(printSession, printSettings, (short) (printerData.printToFile ? OS.kPMDestinationFile : OS.kPMDestinationPrinter), 0, 0);
				if (printerData.scope == PrinterData.PAGE_RANGE) {
					OS.PMSetFirstPage(printSettings, printerData.startPage, false);
					OS.PMSetLastPage(printSettings, printerData.endPage, false);
					OS.PMSetPageRange(printSettings, printerData.startPage, printerData.endPage);
				} else {
					OS.PMSetPageRange(printSettings, 1, OS.kPMPrintAllPages);
				}
				OS.PMSetCopies(printSettings, printerData.copyCount, false);
				OS.PMSetCollate(printSettings, printerData.collate);
				OS.PMSetOrientation(pageFormat, printerData.orientation == PrinterData.LANDSCAPE ? OS.kPMLandscape : OS.kPMPortrait, false);
				boolean[] accepted = new boolean [1];
				if (OS.VERSION >= 0x1050) {
					int printDialogOptions = OS.kPMShowDefaultInlineItems | OS.kPMShowPageAttributesPDE;
					OS.PMShowPrintDialogWithOptions(printSession, printSettings, pageFormat, printDialogOptions, accepted);
				} else {
					OS.PMSessionPageSetupDialog(printSession, pageFormat, accepted);	
					if (accepted[0]) OS.PMSessionPrintDialog(printSession, printSettings, pageFormat, accepted);
				}
				if (accepted[0]) {
					short[] destType = new short[1];
					OS.PMSessionGetDestinationType(printSession, printSettings, destType);
					String name = Printer.getCurrentPrinterName(printSession);
					String driver = Printer.DRIVER;
					switch (destType[0]) {
						case OS.kPMDestinationFax: driver = Printer.FAX_DRIVER; break;
						case OS.kPMDestinationFile: driver = Printer.FILE_DRIVER; break;
						case OS.kPMDestinationPreview: driver = Printer.PREVIEW_DRIVER; break;
						case OS.kPMDestinationPrinter: driver = Printer.PRINTER_DRIVER; break;
					}
					PrinterData data = new PrinterData(driver, name);
					if (destType[0] == OS.kPMDestinationFile) {
						data.printToFile = true;
						OS.PMSessionCopyDestinationLocation(printSession, printSettings, buffer);
						int fileName = OS.CFURLCopyFileSystemPath(buffer[0],OS.kCFURLPOSIXPathStyle);
						OS.CFRelease(buffer[0]);
						data.fileName = Printer.getString(fileName);
						OS.CFRelease(fileName);
					}
					OS.PMGetCopies(printSettings, buffer);
					data.copyCount = buffer[0];
					OS.PMGetFirstPage(printSettings, buffer);
					data.startPage = buffer[0];
					OS.PMGetLastPage(printSettings, buffer);
					data.endPage = buffer[0];
					//OS.PMGetPageRange(printSettings, null, buffer);
					if (data.startPage == 1 && data.endPage == OS.kPMPrintAllPages) {
						data.scope = PrinterData.ALL_PAGES;
					} else {
						data.scope = PrinterData.PAGE_RANGE;
					}
					boolean[] collate = new boolean[1];
					OS.PMGetCollate(printSettings, collate);
					data.collate = collate[0];
					short[] orientation = new short[1];
					OS.PMGetOrientation(pageFormat, orientation);
					data.orientation = orientation[0] == OS.kPMLandscape ? PrinterData.LANDSCAPE : PrinterData.PORTRAIT;
					
					/* Serialize settings */
					int[] flatSettings = new int[1];
					OS.PMFlattenPrintSettings(printSettings, flatSettings);
					int[] flatFormat = new int[1];
					OS.PMFlattenPageFormat(pageFormat, flatFormat);
					int settingsLength = OS.GetHandleSize (flatSettings[0]);
					int formatLength = OS.GetHandleSize (flatFormat[0]);
					byte[] otherData = data.otherData = new byte[settingsLength + formatLength + 8];
					int offset = 0;
					offset = Printer.packData(flatSettings[0], otherData, offset);
					offset = Printer.packData(flatFormat[0], otherData, offset);
					OS.DisposeHandle(flatSettings[0]);
					OS.DisposeHandle(flatFormat[0]);
					printerData = data;
					return data;
				}
				OS.PMRelease(pageFormat);
			}
			OS.PMRelease(printSettings);
		}
		OS.PMRelease(printSession);
	}
	return null;
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

protected void checkSubclass() {
	String name = getClass().getName();
	String validName = PrintDialog.class.getName();
	if (!validName.equals(name)) {
		SWT.error(SWT.ERROR_INVALID_SUBCLASS);
	}
}
}
