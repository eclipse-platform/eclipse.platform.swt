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
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.*;
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
 * @noextend This class is not intended to be subclassed by clients.
 */
public class PrintDialog extends Dialog {
	PrinterData printerData = new PrinterData();
	int returnCode;
	
	// the following Callbacks are never freed
	static Callback dialogCallback5;
	static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E', 'C', 'T', '\0'};
	static final String SET_MODAL_DIALOG = "org.eclipse.swt.internal.modalDialog"; //$NON-NLS-1$

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
	super (parent, checkStyle(parent, style));
	checkSubclass ();
}

static int checkStyle (Shell parent, int style) {
	int mask = SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL;
	if ((style & SWT.SHEET) != 0) {
		if (getSheetEnabled ()) {
			if (parent == null) {
				style &= ~SWT.SHEET;
			}
		} else {
			style &= ~SWT.SHEET;
		}
		if ((style & mask) == 0) {
			style |= parent == null ? SWT.APPLICATION_MODAL : SWT.PRIMARY_MODAL;
		}
	}
	return style;
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
	PrinterData data = null;
	NSPrintPanel panel = NSPrintPanel.printPanel();
	NSPrintInfo printInfo = new NSPrintInfo(NSPrintInfo.sharedPrintInfo().copy());
	if (printerData.duplex != SWT.DEFAULT) {
		int /*long*/ settings = printInfo.PMPrintSettings();
		int duplex = printerData.duplex == PrinterData.DUPLEX_SHORT_EDGE ? OS.kPMDuplexTumble
				: printerData.duplex == PrinterData.DUPLEX_LONG_EDGE ? OS.kPMDuplexNoTumble
				: OS.kPMDuplexNone;
		OS.PMSetDuplex(settings, duplex);
	}
	/* Updating printInfo from PMPrintSettings overrides values in the printInfo dictionary. */
	printInfo.updateFromPMPrintSettings();
	if (printerData.name != null) {
		NSPrinter printer = NSPrinter.printerWithName(NSString.stringWith(printerData.name));
		if (printer != null) {
			printer.retain();
			printInfo.setPrinter(printer);
		}
	}
	NSMutableDictionary dict = printInfo.dictionary();
	dict.setValue(NSNumber.numberWithBool(printerData.collate), OS.NSPrintMustCollate);
	dict.setValue(NSNumber.numberWithInt(printerData.copyCount), OS.NSPrintCopies);
	dict.setValue(NSNumber.numberWithInt(printerData.orientation == PrinterData.LANDSCAPE ? OS.NSLandscapeOrientation : OS.NSPortraitOrientation), OS.NSPrintOrientation);
	if (printerData.printToFile) {
		dict.setValue(OS.NSPrintSaveJob, OS.NSPrintJobDisposition);
	}
	if (printerData.fileName != null && printerData.fileName.length() > 0) {
		dict.setValue(NSString.stringWith(printerData.fileName), OS.NSPrintSavePath);
	}
	dict.setValue(NSNumber.numberWithBool(printerData.scope == PrinterData.ALL_PAGES), OS.NSPrintAllPages);
	if (printerData.scope == PrinterData.PAGE_RANGE) {
		dict.setValue(NSNumber.numberWithInt(printerData.startPage), OS.NSPrintFirstPage);
		dict.setValue(NSNumber.numberWithInt(printerData.endPage), OS.NSPrintLastPage);
	}
	panel.setOptions(OS.NSPrintPanelShowsPageSetupAccessory | panel.options());
	Shell parent = getParent();
	Display display = parent != null ? parent.getDisplay() : Display.getCurrent();
	int response;
	if ((getStyle () & SWT.SHEET) != 0) {
		initClasses();
		SWTPrintPanelDelegate delegate = (SWTPrintPanelDelegate)new SWTPrintPanelDelegate().alloc().init();
		int /*long*/ jniRef = OS.NewGlobalRef(this);
		if (jniRef == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		OS.object_setInstanceVariable(delegate.id, SWT_OBJECT, jniRef);
		returnCode = -1;
		panel.beginSheetWithPrintInfo(printInfo, parent.view.window(), delegate, OS.sel_panelDidEnd_returnCode_contextInfo_, 0);
		while (returnCode == -1) {
			if (!display.readAndDispatch()) display.sleep();
		}
		if (delegate != null) delegate.release();
		if (jniRef != 0) OS.DeleteGlobalRef(jniRef);
		response = returnCode;
	} else {
		display.setData(SET_MODAL_DIALOG, this);
		response = (int)/*64*/panel.runModalWithPrintInfo(printInfo);
	}
	display.setData(SET_MODAL_DIALOG, null);
	if (response != OS.NSCancelButton) {
		NSPrinter printer = printInfo.printer();
		NSString str = printer.name();
		data = new PrinterData(Printer.DRIVER, str.getString());
		data.printToFile = printInfo.jobDisposition().isEqual(OS.NSPrintSaveJob);
		if (data.printToFile) {
			NSString filename = new NSString(dict.objectForKey(OS.NSPrintSavePath));
			data.fileName = filename.getString();
		}
		data.scope = new NSNumber(dict.objectForKey(OS.NSPrintAllPages)).intValue() != 0 ? PrinterData.ALL_PAGES : PrinterData.PAGE_RANGE;
		if (data.scope == PrinterData.PAGE_RANGE) {
			data.startPage = new NSNumber(dict.objectForKey(OS.NSPrintFirstPage)).intValue();
			data.endPage = new NSNumber(dict.objectForKey(OS.NSPrintLastPage)).intValue();
		}
		data.collate = new NSNumber(dict.objectForKey(OS.NSPrintMustCollate)).intValue() != 0;
		data.collate = false; //TODO: Only set to false if the printer does the collate internally (most printers do)
		data.copyCount = new NSNumber(dict.objectForKey(OS.NSPrintCopies)).intValue();
		data.copyCount = 1; //TODO: Only set to 1 if the printer does the copy internally (most printers do)
		data.orientation = new NSNumber(dict.objectForKey(OS.NSPrintOrientation)).intValue() == OS.NSLandscapeOrientation ? PrinterData.LANDSCAPE : PrinterData.PORTRAIT;
		int /*long*/ settings = printInfo.PMPrintSettings();
		int outDuplexSetting[] = new int[1];
		OS.PMGetDuplex(settings, outDuplexSetting);
		data.duplex = outDuplexSetting[0] == OS.kPMDuplexTumble ? PrinterData.DUPLEX_SHORT_EDGE
				: outDuplexSetting[0] == OS.kPMDuplexNoTumble ? PrinterData.DUPLEX_LONG_EDGE
				: PrinterData.DUPLEX_NONE;
		NSData nsData = NSKeyedArchiver.archivedDataWithRootObject(printInfo);
		data.otherData = new byte[(int)/*64*/nsData.length()];
		OS.memmove(data.otherData, nsData.bytes(), data.otherData.length);
		printerData = data;
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
	return printerData.scope;
}

static boolean getSheetEnabled () {
	return !"false".equals(System.getProperty("org.eclipse.swt.sheet"));
}

static int /*long*/ dialogProc(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2) {
	int /*long*/ [] jniRef = new int /*long*/ [1];
	OS.object_getInstanceVariable(id, SWT_OBJECT, jniRef);
	if (jniRef[0] == 0) return 0;
	if (sel == OS.sel_panelDidEnd_returnCode_contextInfo_) {
		PrintDialog dialog = (PrintDialog)OS.JNIGetObject(jniRef[0]);
		if (dialog == null) return 0;
		dialog.panelDidEnd_returnCode_contextInfo(id, sel, arg0, arg1, arg2);
	}
	return 0;
}

void initClasses () {
	String className = "SWTPrintPanelDelegate";
	if (OS.objc_lookUpClass (className) != 0) return;
	
	dialogCallback5 = new Callback(getClass(), "dialogProc", 5);
	int /*long*/ dialogProc5 = dialogCallback5.getAddress();
	if (dialogProc5 == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);	
	
	byte[] types = {'*','\0'};
	int size = C.PTR_SIZEOF, align = C.PTR_SIZEOF == 4 ? 2 : 3;
	int /*long*/ cls = OS.objc_allocateClassPair(OS.class_NSObject, className, 0);
	OS.class_addIvar(cls, SWT_OBJECT, size, (byte)align, types);
	OS.class_addMethod(cls, OS.sel_panelDidEnd_returnCode_contextInfo_, dialogProc5, "@:@i@");
	OS.objc_registerClassPair(cls);
}

void panelDidEnd_returnCode_contextInfo(int /*long*/ id, int /*long*/ sel, int /*long*/ alert, int /*long*/ returnCode, int /*long*/ contextInfo) {
	this.returnCode = (int)/*64*/returnCode;
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
