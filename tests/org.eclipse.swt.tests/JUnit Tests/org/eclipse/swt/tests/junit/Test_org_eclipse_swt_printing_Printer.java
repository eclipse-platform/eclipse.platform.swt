/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;

/**
 * Automated Test Suite for class org.eclipse.swt.printing.Printer
 *
 * @see org.eclipse.swt.printing.Printer
 */
public class Test_org_eclipse_swt_printing_Printer extends TestCase {

public void test_Constructor() {
	boolean exceptionThrown = false;
	String detail = "";
	if (Printer.getDefaultPrinterData() == null) {
		/* There aren't any printers, so verify that the
		 * constructor throws an ERROR_NO_HANDLES SWTError.
		 */
		try {
			Printer printer = new Printer();
			printer.dispose();
		} catch (SWTError ex) {
			if (ex.code == SWT.ERROR_NO_HANDLES) exceptionThrown = true;
		}
		assertTrue("ERROR_NO_HANDLES not thrown", exceptionThrown);
	} else {
		/* There is at least a default printer, so verify that
		 * the constructor does not throw any exceptions.
		 */
		try {
			Printer printer = new Printer();
			printer.dispose();
		} catch (Throwable ex) {
			exceptionThrown = true;
			detail = ex.getMessage();
		}
		assertFalse("Exception thrown: " + detail, exceptionThrown);
	}
}

public void test_ConstructorLorg_eclipse_swt_printing_PrinterData() {
	boolean exceptionThrown = false;
	String detail = "";
	PrinterData data = Printer.getDefaultPrinterData();
	if (data == null) {
		/* There aren't any printers, so verify that the
		 * constructor throws an ERROR_NO_HANDLES SWTError.
		 */
		try {
			Printer printer = new Printer(data);
			printer.dispose();
		} catch (SWTError ex) {
			if (ex.code == SWT.ERROR_NO_HANDLES) exceptionThrown = true;
		}
		assertTrue("ERROR_NO_HANDLES not thrown", exceptionThrown);
	} else {
		/* There is at least a default printer, so verify that
		 * the constructor does not throw any exceptions.
		 */
		try {
			Printer printer = new Printer(data);
			printer.dispose();
		} catch (Throwable ex) {
			exceptionThrown = true;
			detail = ex.getMessage();
		}
		assertFalse("Exception thrown: " + detail, exceptionThrown);
	}
}

public void test_computeTrimIIII() {
	PrinterData data = Printer.getDefaultPrinterData();
	// if there aren't any printers, don't do this test
	if (data == null) return;
	Printer printer = new Printer(data);
	Rectangle trim = printer.computeTrim(0, 0, 10, 10);
	assertTrue("trim width or height is incorrect", trim.width >= 10 && trim.height >= 10);
	printer.dispose();
}

public void test_getBounds() {
	PrinterData data = Printer.getDefaultPrinterData();
	// if there aren't any printers, don't do this test
	if (data == null) return;
	Printer printer = new Printer(data);
	Rectangle bounds = printer.getBounds();
	assertTrue("bounds width or height is zero", bounds.width > 0 && bounds.height > 0);
	printer.dispose();
}

public void test_getClientArea() {
	PrinterData data = Printer.getDefaultPrinterData();
	// if there aren't any printers, don't do this test
	if (data == null) return;
	Printer printer = new Printer(data);
	Rectangle clientArea = printer.getClientArea();
	assertTrue("clientArea width or height is zero", clientArea.width > 0 && clientArea.height > 0);
	printer.dispose();
}

public void test_getDPI() {
	PrinterData data = Printer.getDefaultPrinterData();
	// if there aren't any printers, don't do this test
	if (data == null) return;
	Printer printer = new Printer(data);
	Point dpi = printer.getDPI();
	assertTrue("dpi x or y is zero", dpi.x > 0 && dpi.y > 0);
	printer.dispose();
}

public void test_getPrinterData() {
	PrinterData data = Printer.getDefaultPrinterData();
	// if there aren't any printers, don't do this test
	if (data == null) return;
	Printer printer = new Printer(data);
	assertTrue("getPrinterData != data used in constructor",
			data == printer.getPrinterData());
	printer.dispose();
}

public void test_getPrinterList() {
	PrinterData data = Printer.getDefaultPrinterData();
	if (data == null) {
		/* If there aren't any printers, verify that the
		 * printer list is empty.
		 */
		PrinterData list[] = Printer.getPrinterList();
		if (list.length  == 1) {
			if (SWT.getPlatform().equals("gtk")) {
				/* Even though there is no default printer data, 
				 * on GTK it is still possible to have a print 
				 * to file backend
				 */
				assertTrue(list[0].driver.equals("GtkPrintBackendFile"));
			}
		} else {
		assertTrue("printer list contains items even though there are no printers",
				list.length == 0);
		}
	} else {
		/* If there is at least a default printer, verify
		 * that the printer list is not empty.
		 */
		PrinterData list[] = Printer.getPrinterList();
		assertTrue("printer list is empty", list.length > 0);
	}
}
}
