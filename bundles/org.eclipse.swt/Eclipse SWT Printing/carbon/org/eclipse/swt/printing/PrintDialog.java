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
 */
public class PrintDialog extends Dialog {
	int scope = PrinterData.ALL_PAGES;
	int startPage = 1, endPage = 1;
	boolean printToFile = false;

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
				OS.PMSessionSetDestination(printSession, printSettings, (short) (printToFile ? OS.kPMDestinationFile : OS.kPMDestinationPrinter), 0, 0);
				if (scope == PrinterData.PAGE_RANGE) {
					OS.PMSetFirstPage(printSettings, startPage, false);
					OS.PMSetLastPage(printSettings, endPage, false);
					OS.PMSetPageRange(printSettings, startPage, endPage);
				} else {
					OS.PMSetPageRange(printSettings, 1, OS.kPMPrintAllPages);
				}
				boolean[] accepted = new boolean [1];
				OS.PMSessionPageSetupDialog(printSession, pageFormat, accepted);	
				if (accepted[0]) {		
					OS.PMSessionPrintDialog(printSession, printSettings, pageFormat, accepted);
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
						OS.PMGetPageRange(printSettings, null, buffer);
						if (data.startPage == 1 && data.endPage == OS.kPMPrintAllPages) {
							data.scope = PrinterData.ALL_PAGES;
						} else {
							data.scope = PrinterData.PAGE_RANGE;
						}
						boolean[] collate = new boolean[1];
						OS.PMGetCollate(printSettings, collate);
						data.collate = collate[0];
						
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
						
						scope = data.scope;
						startPage = data.startPage;
						endPage = data.endPage;
						printToFile = data.printToFile;
						return data;
					}
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

protected void checkSubclass() {
	String name = getClass().getName();
	String validName = PrintDialog.class.getName();
	if (!validName.equals(name)) {
		SWT.error(SWT.ERROR_INVALID_SUBCLASS);
	}
}
}
