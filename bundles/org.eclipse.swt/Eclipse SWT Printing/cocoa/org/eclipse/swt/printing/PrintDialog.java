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
import org.eclipse.swt.internal.cocoa.*;

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
	PrinterData printerData;
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
	PrinterData data = null;
	NSPrintPanel panel = NSPrintPanel.printPanel();
	NSPrintInfo printInfo = new NSPrintInfo(NSPrintInfo.sharedPrintInfo().copy().id);
	NSDictionary dict = printInfo.dictionary();	
	if (printToFile) {
		dict.setValue_forKey_(new NSString(OS.NSPrintSaveJob()), new NSString(OS.NSPrintJobDisposition()));
	}
	//TODO - setting range not work. why?
	dict.setValue_forKey_(NSNumber.numberWithBool(scope == PrinterData.ALL_PAGES), new NSString(OS.NSPrintAllPages()));
	if (scope == PrinterData.PAGE_RANGE) {
		dict.setValue_forKey_(NSNumber.numberWithInt(startPage), new NSString(OS.NSPrintFirstPage()));
		dict.setValue_forKey_(NSNumber.numberWithInt(endPage), new NSString(OS.NSPrintLastPage()));
	}
	panel.setOptions(OS.NSPrintPanelShowsPageSetupAccessory | panel.options());
	if (panel.runModalWithPrintInfo(printInfo) != OS.NSCancelButton) {
		NSPrinter printer = printInfo.printer();
		NSString str = printer.name();
		char[] buffer = new char[str.length()];
		str.getCharacters_(buffer);
		data = new PrinterData(Printer.DRIVER, new String(buffer));
		data.printToFile = printInfo.jobDisposition().isEqual(new NSString(OS.NSPrintSaveJob()));
		if (data.printToFile) {
			NSString filename = new NSString(dict.objectForKey(new NSString(OS.NSPrintSavePath())).id);
			data.fileName = filename.getString();
		}
		data.scope = new NSNumber(dict.objectForKey(new NSString(OS.NSPrintAllPages())).id).intValue() != 0 ? PrinterData.ALL_PAGES : PrinterData.PAGE_RANGE;
		if (data.scope == PrinterData.PAGE_RANGE) {
			data.startPage = new NSNumber(dict.objectForKey(new NSString(OS.NSPrintFirstPage())).id).intValue();
			data.endPage = new NSNumber(dict.objectForKey(new NSString(OS.NSPrintLastPage())).id).intValue();
		}
		data.collate = new NSNumber(dict.objectForKey(new NSString(OS.NSPrintMustCollate())).id).intValue() != 0;
		data.copyCount = new NSNumber(dict.objectForKey(new NSString(OS.NSPrintCopies())).id).intValue();
		NSData nsData = NSKeyedArchiver.archivedDataWithRootObject(printInfo);
		data.otherData = new byte[nsData.length()];
		OS.memmove(data.otherData, nsData.bytes(), data.otherData.length);

		printToFile = data.printToFile;
		scope = data.scope;
		startPage = data.startPage;
		endPage = data.endPage;
	}
	printInfo.release();
	return data;
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
	return scope;
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
	this.scope = scope;
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
	return startPage;
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
	this.startPage = startPage;
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
	return endPage;
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
