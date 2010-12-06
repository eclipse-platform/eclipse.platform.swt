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
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#printing">Printing snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class PrintDialog extends Dialog {
	static final TCHAR DialogClass = new TCHAR (0, OS.IsWinCE ? "Dialog" : "#32770", true);
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
	super (parent, checkStyle(parent, style));
	checkSubclass ();
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

static int checkStyle (Shell parent, int style) {
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
	/* Get the owner HWND for the dialog */
	Control parent = getParent();
	int style = getStyle();
	int /*long*/ hwndOwner = parent.handle;
	int /*long*/ hwndParent = parent.handle;

	/*
	* Feature in Windows.  There is no API to set the orientation of a
	* file dialog.  It is always inherited from the parent.  The fix is
	* to create a hidden parent and set the orientation in the hidden
	* parent for the dialog to inherit.
	*/
	boolean enabled = false;
	if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION(4, 10)) {
		int dialogOrientation = style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
		int parentOrientation = parent.getStyle() & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
		if (dialogOrientation != parentOrientation) {
			int exStyle = OS.WS_EX_NOINHERITLAYOUT;
			if (dialogOrientation == SWT.RIGHT_TO_LEFT) exStyle |= OS.WS_EX_LAYOUTRTL;
			hwndOwner = OS.CreateWindowEx (
				exStyle,
				DialogClass,
				null,
				0,
				OS.CW_USEDEFAULT, 0, OS.CW_USEDEFAULT, 0,
				hwndParent,
				0,
				OS.GetModuleHandle (null),
				null);
			enabled = OS.IsWindowEnabled (hwndParent);
			if (enabled) OS.EnableWindow (hwndParent, false);
		}
	}

	PrinterData data = null;
	PRINTDLG pd = new PRINTDLG();
	pd.lStructSize = PRINTDLG.sizeof;
	pd.hwndOwner = hwndOwner;
	
	/* Initialize PRINTDLG fields, including DEVMODE. */
	pd.Flags = OS.PD_RETURNDEFAULT;
	if (OS.PrintDlg(pd)) {
		if (pd.hDevNames != 0) {
			OS.GlobalFree(pd.hDevNames);
			pd.hDevNames = 0;
		}

		/*
		 * If user setup info from a previous print dialog was specified,
		 * then restore the previous DEVMODE struct.
		 */
		byte devmodeData [] = printerData.otherData;
		if (devmodeData != null && devmodeData.length != 0) {
			int /*long*/ lpInitData = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, devmodeData.length);
			OS.MoveMemory(lpInitData, devmodeData, devmodeData.length);
			if (pd.hDevMode != 0) OS.GlobalFree(pd.hDevMode);
			pd.hDevMode = lpInitData;
		}
		
		/* Initialize the DEVMODE struct's fields from the printerData. */
		int /*long*/ hMem = pd.hDevMode;
		int /*long*/ ptr = OS.GlobalLock(hMem);
		DEVMODE devmode = OS.IsUnicode ? (DEVMODE)new DEVMODEW () : new DEVMODEA ();
		OS.MoveMemory(devmode, ptr, OS.IsUnicode ? OS.DEVMODEW_sizeof() : OS.DEVMODEA_sizeof());
		devmode.dmFields |= OS.DM_ORIENTATION;
		devmode.dmOrientation = printerData.orientation == PrinterData.PORTRAIT ? OS.DMORIENT_PORTRAIT : OS.DMORIENT_LANDSCAPE;
		if (printerData.copyCount != 1) {
			devmode.dmFields |= OS.DM_COPIES;
			devmode.dmCopies = (short)printerData.copyCount;
		}
		if (printerData.collate != false) {
			devmode.dmFields |= OS.DM_COLLATE;
			devmode.dmCollate = OS.DMCOLLATE_TRUE;
		}
		if (printerData.duplex != SWT.DEFAULT) {
			devmode.dmFields |= OS.DM_DUPLEX;
			switch (printerData.duplex) {
				case PrinterData.DUPLEX_SHORT_EDGE: devmode.dmDuplex = OS.DMDUP_HORIZONTAL; break;
				case PrinterData.DUPLEX_LONG_EDGE: devmode.dmDuplex = OS.DMDUP_VERTICAL; break;
				default: devmode.dmDuplex = OS.DMDUP_SIMPLEX;
			}
		}
		OS.MoveMemory(ptr, devmode, OS.IsUnicode ? OS.DEVMODEW_sizeof() : OS.DEVMODEA_sizeof());
		OS.GlobalUnlock(hMem);
	
		pd.Flags = OS.PD_USEDEVMODECOPIESANDCOLLATE;
		if (printerData.printToFile) pd.Flags |= OS.PD_PRINTTOFILE;
		switch (printerData.scope) {
			case PrinterData.PAGE_RANGE: pd.Flags |= OS.PD_PAGENUMS; break;
			case PrinterData.SELECTION: pd.Flags |= OS.PD_SELECTION; break;
			default: pd.Flags |= OS.PD_ALLPAGES;
		}
		pd.nMinPage = 1;
		pd.nMaxPage = -1;
		pd.nFromPage = (short) Math.min (0xFFFF, Math.max (1, printerData.startPage));
		pd.nToPage = (short) Math.min (0xFFFF, Math.max (1, printerData.endPage));
	
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
		String key = "org.eclipse.swt.internal.win32.runMessagesInIdle"; //$NON-NLS-1$
		Object oldValue = display.getData(key);
		display.setData(key, new Boolean(true));
		boolean success = OS.PrintDlg(pd);
		display.setData(key, oldValue);
		if ((getStyle() & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
			for (int i=0; i<shells.length; i++) {
				if (shells[i] != null && !shells[i].isDisposed ()) {
					shells[i].setEnabled(true);
				}
			}
		}
		
		if (success) {
			/* Get driver and device from the DEVNAMES struct */
			hMem = pd.hDevNames;
			/* Ensure size is a multiple of 2 bytes on UNICODE platforms */
			int size = OS.GlobalSize(hMem) / TCHAR.sizeof * TCHAR.sizeof;
			ptr = OS.GlobalLock(hMem);
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
			devmode = OS.IsUnicode ? (DEVMODE)new DEVMODEW () : new DEVMODEA ();
			OS.MoveMemory(devmode, ptr, OS.IsUnicode ? OS.DEVMODEW_sizeof() : OS.DEVMODEA_sizeof());
			if ((devmode.dmFields & OS.DM_ORIENTATION) != 0) {
				int dmOrientation = devmode.dmOrientation;
				data.orientation = dmOrientation == OS.DMORIENT_LANDSCAPE ? PrinterData.LANDSCAPE : PrinterData.PORTRAIT;
			}
			if ((devmode.dmFields & OS.DM_DUPLEX) != 0) {
				short dmDuplex = devmode.dmDuplex;
				data.duplex = dmDuplex == OS.DMDUP_SIMPLEX ? PrinterData.DUPLEX_NONE : dmDuplex == OS.DMDUP_HORIZONTAL ? PrinterData.DUPLEX_SHORT_EDGE : PrinterData.DUPLEX_LONG_EDGE;
			}

			OS.GlobalUnlock(hMem);
			printerData = data;
		}
	}
	if (pd.hDevNames != 0) {
		OS.GlobalFree(pd.hDevNames);
		pd.hDevNames = 0;
	}
	if (pd.hDevMode != 0) {
		OS.GlobalFree(pd.hDevMode);
		pd.hDevMode = 0;
	}
	/* Destroy the BIDI orientation window */
	if (hwndParent != hwndOwner) {
		if (enabled) OS.EnableWindow (hwndParent, true);
		OS.SetActiveWindow (hwndParent);
		OS.DestroyWindow (hwndOwner);
	}
	return data;
}
}
