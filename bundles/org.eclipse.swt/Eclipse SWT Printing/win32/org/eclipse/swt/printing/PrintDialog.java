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
import org.eclipse.swt.internal.win32.*;

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
	PRINTDLG pd = new PRINTDLG();
	pd.lStructSize = PRINTDLG.sizeof;
	Control parent = getParent();
	if (parent != null) pd.hwndOwner = parent.handle;
	pd.Flags = OS.PD_USEDEVMODECOPIESANDCOLLATE;
	if (printToFile) pd.Flags |= OS.PD_PRINTTOFILE;
	switch (scope) {
		case PrinterData.PAGE_RANGE: pd.Flags |= OS.PD_PAGENUMS; break;
		case PrinterData.SELECTION: pd.Flags |= OS.PD_SELECTION; break;
		default: pd.Flags |= OS.PD_ALLPAGES;
	}
	pd.nMinPage = 1;
	pd.nMaxPage = -1;
	pd.nFromPage = (short) Math.min (0xFFFF, Math.max (1, startPage));
	pd.nToPage = (short) Math.min (0xFFFF, Math.max (1, endPage));
	
	Display display = parent.getDisplay();
	Shell [] shells = display.getShells();
	if ((getStyle() & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
		for (int i=0; i<shells.length; i++) {
			if (shells[i].isEnabled() && shells[i] != parent) {
				shells[i].setEnabled(false);
			} else {
				shells[i] = null;
			}
		}
	}
	PrinterData data = null;
	boolean success = OS.PrintDlg(pd);
	if ((getStyle() & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
		for (int i=0; i<shells.length; i++) {
			if (shells[i] != null && !shells[i].isDisposed ()) {
				shells[i].setEnabled(true);
			}
		}
	}
	
	if (success) {
		/* Get driver and device from the DEVNAMES struct */
		int hMem = pd.hDevNames;
		/* Ensure size is a multiple of 2 bytes on UNICODE platforms */
		int size = OS.GlobalSize(hMem) / TCHAR.sizeof * TCHAR.sizeof;
		int ptr = OS.GlobalLock(hMem);
		short[] offsets = new short[4];
		OS.MoveMemory(offsets, ptr, 2 * offsets.length);
		TCHAR buffer = new TCHAR(0, size);
		OS.MoveMemory(buffer, ptr, size);	
		OS.GlobalUnlock(hMem);

		int driverOffset = offsets[0];
		int i = 0;
		while (driverOffset + i < size) {
			if (buffer.tcharAt(driverOffset + i) == 0) break;
			i++;
		}
		String driver = buffer.toString(driverOffset, i);

		int deviceOffset = offsets[1];
		i = 0;
		while (deviceOffset + i < size) {
			if (buffer.tcharAt(deviceOffset + i) == 0) break;
			i++;
		}
		String device = buffer.toString(deviceOffset, i);	

		int outputOffset = offsets[2];
		i = 0;
		while (outputOffset + i < size) {
			if (buffer.tcharAt(outputOffset + i) == 0) break;
			i++;
		}
		String output = buffer.toString(outputOffset, i);
		
		/* Create PrinterData object and set fields from PRINTDLG */
		data = new PrinterData(driver, device);
		if ((pd.Flags & OS.PD_PAGENUMS) != 0) {
			data.scope = PrinterData.PAGE_RANGE;
			data.startPage = pd.nFromPage & 0xFFFF;
			data.endPage = pd.nToPage & 0xFFFF;
		} else if ((pd.Flags & OS.PD_SELECTION) != 0) {
			data.scope = PrinterData.SELECTION;
		}
		data.printToFile = (pd.Flags & OS.PD_PRINTTOFILE) != 0;
		if (data.printToFile) data.fileName = output;
		data.copyCount = pd.nCopies;
		data.collate = (pd.Flags & OS.PD_COLLATE) != 0;

		/* Bulk-save the printer-specific settings in the DEVMODE struct */
		hMem = pd.hDevMode;
		size = OS.GlobalSize(hMem);
		ptr = OS.GlobalLock(hMem);
		data.otherData = new byte[size];
		OS.MoveMemory(data.otherData, ptr, size);
		OS.GlobalUnlock(hMem);

		endPage = data.endPage;
		printToFile = data.printToFile;
		scope = data.scope;
		startPage = data.startPage;
	}
	return data;
}
}
