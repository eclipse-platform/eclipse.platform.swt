///*******************************************************************************
// * Copyright (c) 2000, 2004 IBM Corporation and others.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// *
// * Contributors:
// *     IBM Corporation - initial API and implementation
// *******************************************************************************/
//package org.eclipse.swt.printing;
//
//
//import javax.print.attribute.HashPrintRequestAttributeSet;
//
//import org.eclipse.swt.graphics.DeviceData;
//
///**
// * Instances of this class are descriptions of a print job
// * in terms of the printer, and the scope and type of printing
// * that is desired. For example, the number of pages and copies
// * can be specified, as well as whether or not the print job
// * should go to a file.
// * <p>
// * Application code does <em>not</em> need to explicitly release the
// * resources managed by each instance when those instances are no longer
// * required, and thus no <code>dispose()</code> method is provided.
// * </p>
// *
// * @see Printer
// * @see Printer#getPrinterList
// * @see PrintDialog#open
// */
//
//public final class PrinterData extends DeviceData {
//
//	/**
//	 * the printer driver
//	 * On Windows systems, this is the name of the driver (often "winspool").
//	 * On X/Window systems, this is the name of a display connection to the
//	 * Xprt server (the default is ":1").
//	 */
//	public String driver;
//
//	/**
//	 * the name of the printer
//	 * On Windows systems, this is the name of the 'device'.
//	 * On X/Window systems, this is the printer's 'name'.
//	 */
//	public String name;
//
//	/**
//	 * the scope of the print job, expressed as one of the following values:
//	 * <dl>
//	 * <dt><code>ALL_PAGES</code></dt>
//	 * <dd>Print all pages in the current document</dd>
//	 * <dt><code>PAGE_RANGE</code></dt>
//	 * <dd>Print the range of pages specified by startPage and endPage</dd>
//	 * <dt><code>SELECTION</code></dt>
//	 * <dd>Print the current selection</dd>
//	 * </dl>
//	 */
//	public int scope = ALL_PAGES;
//
//	/**
//	 * the start page of a page range, used when scope is PAGE_RANGE
//	 */
//	public int startPage = 0;
//
//	/**
//	 * the end page of a page range, used when scope is PAGE_RANGE
//	 */
//	public int endPage = 0;
//
//	/**
//	 * whether or not the print job should go to a file
//	 */
//	public boolean printToFile = false;
//
//	/**
//	 * the name of the file to print to if printToFile is true.
//	 * Note that this field is ignored if printToFile is false.
//	 */
//	public String fileName;
//
//	/**
//	 * the number of copies to print.
//	 * Note that this field may be controlled by the printer driver
//	 * In other words, the printer itself may be capable of printing
//	 * multiple copies, and if so, the value of this field will always be 1.
//	 */
//	public int copyCount = 1;
//
//	/**
//	 * whether or not the printer should collate the printed paper
//	 * Note that this field may be controlled by the printer driver.
//	 * In other words, the printer itself may be capable of doing the
//	 * collation, and if so, the value of this field will always be false.
//	 */
//	public boolean collate = false;
//
//	/**
//	 * <code>scope</code> field value indicating that
//	 * all pages should be printed
//	 */
//	public static final int ALL_PAGES = 0;
//
//	/**
//	 * <code>scope</code> field value indicating that
//	 * the range of pages specified by startPage and endPage
//	 * should be printed
//	 */
//	public static final int PAGE_RANGE = 1;
//
//	/**
//	 * <code>scope</code> field value indicating that
//	 * the current selection should be printed
//	 */
//	public static final int SELECTION = 2;
//
//	/**
//	 * private, platform-specific data
//	 * On Windows, this contains a copy of the DEVMODE struct
//	 * returned from the <code>PrintDialog</code>.
//	 * This field is not currently used on the X/Window System.
//	 */
//	HashPrintRequestAttributeSet otherData;
//
//	/**
//	 * Constructs an instance of this class that can be
//	 * used to print to the default printer.
//	 *
//	 * @see Printer#getDefaultPrinterData
//	 */
//	public PrinterData() {
//	}
//
//	/**
//	 * Constructs an instance of this class with the given
//	 * printer driver and printer name.
//	 *
//	 * @param driver the printer driver for the printer
//	 * @param name the name of the printer
//	 *
//	 * @see #driver
//	 * @see #name
//	 */
//	public PrinterData(String driver, String name) {
//		this.driver = driver;
//		this.name = name;
//	}
//
//	/**
//	 * Returns a string containing a concise, human-readable
//	 * description of the receiver.
//	 *
//	 * @return a string representation of the receiver
//	 */
//	public String toString() {
//		return "PrinterData {" + "driver = " + driver + ", name = " + name + "}";  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
//	}
//}


