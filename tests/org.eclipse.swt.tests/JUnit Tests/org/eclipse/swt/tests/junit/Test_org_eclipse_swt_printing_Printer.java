/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.printing.Printer
 *
 * @see org.eclipse.swt.printing.Printer
 */
public class Test_org_eclipse_swt_printing_Printer {

@Test
public void test_Constructor() {
	if (Printer.getDefaultPrinterData() == null) {
		/* There aren't any printers, so verify that the
		 * constructor throws an ERROR_NO_HANDLES SWTError.
		 */
		SWTError ex = assertThrows(SWTError.class, ()->new Printer());
		assertEquals(SWT.ERROR_NO_HANDLES, ex.code, "ERROR_NO_HANDLES not thrown");
	} else {
		/* There is at least a default printer, so verify that
		 * the constructor does not throw any exceptions.
		 */
		Printer printer = new Printer();
		assertNotNull(printer );
		printer.dispose();
	}
}

@Test
public void test_ConstructorLorg_eclipse_swt_printing_PrinterData() {
	PrinterData data = Printer.getDefaultPrinterData();
	if (data == null) {
		/* There aren't any printers, so verify that the
		 * constructor throws an ERROR_NO_HANDLES SWTError.
		 */
		SWTError ex = assertThrows(SWTError.class, ()-> new Printer(data));
		assertEquals(SWT.ERROR_NO_HANDLES, ex.code, "ERROR_NO_HANDLES not thrown");
	} else {
		/* There is at least a default printer, so verify that
		 * the constructor does not throw any exceptions.
		 */
		Printer printer = new Printer(data);
		assertNotNull(printer);
		printer.dispose();
	}
}

@Test
public void test_computeTrimIIII() {
	PrinterData data = Printer.getDefaultPrinterData();
	assumeTrue (data != null, "if there aren't any printers, don't do this test");
	Printer printer = new Printer(data);
	Rectangle trim = printer.computeTrim(0, 0, 10, 10);
	assertTrue(trim.width >= 10 && trim.height >= 10, "trim width or height is incorrect");
	printer.dispose();
}

@Test
public void test_getBounds() {
	PrinterData data = Printer.getDefaultPrinterData();
	assumeTrue (data != null, "if there aren't any printers, don't do this test");
	Printer printer = new Printer(data);
	Rectangle bounds = printer.getBounds();
	assertTrue(bounds.width > 0 && bounds.height > 0, "bounds width or height is zero");
	printer.dispose();
}

@Test
public void test_getClientArea() {
	PrinterData data = Printer.getDefaultPrinterData();
	assumeTrue (data != null, "if there aren't any printers, don't do this test");
	Printer printer = new Printer(data);
	Rectangle clientArea = printer.getClientArea();
	assertTrue(clientArea.width > 0 && clientArea.height > 0, "clientArea width or height is zero");
	printer.dispose();
}

@Test
public void test_getDPI() {
	PrinterData data = Printer.getDefaultPrinterData();
	assumeTrue (data != null, "if there aren't any printers, don't do this test");
	Printer printer = new Printer(data);
	Point dpi = printer.getDPI();
	assertTrue(dpi.x > 0 && dpi.y > 0, "dpi x or y is zero");
	printer.dispose();
}

@Test
public void test_getPrinterData() {
	PrinterData data = Printer.getDefaultPrinterData();
	assumeTrue (data != null, "if there aren't any printers, don't do this test");
	Printer printer = new Printer(data);
	assertTrue(data == printer.getPrinterData(), "getPrinterData != data used in constructor");
	printer.dispose();
}

@Tag("gtk4-todo")
@Test
public void test_getPrinterList() {
	PrinterData data = Printer.getDefaultPrinterData();
	if (data == null) {
		/* If there aren't any printers, verify that the
		 * printer list is empty.
		 */
		Stream<PrinterData> printerStream = Arrays.stream(Printer.getPrinterList());

		if (SWT.getPlatform().equals("gtk")) {
			/* Even though there is no default printer data,
			 * on GTK it is still possible to have a print
			 * to file backend
			 */
			printerStream = printerStream.filter(pd->{
				if ("GtkPrintBackendFile".equals(pd.driver)) {
					return false;
				}
				return true;
			});
		} else if (SWT.getPlatform().equals("win32")) {
			printerStream = printerStream.filter(pd->{
				if (pd.name != null) {
					/*
					 * Windows also has some default (non hardware) printer by default
					 */
					switch(pd.name.toLowerCase()) {
					case "fax":
					case "microsoft xps document writer":
					case "microsoft print to pdf":
						return false;
					}
				}
				return true;
			});
		}
		PrinterData[] list = printerStream.toArray(PrinterData[]::new);
		String printerNames = Arrays.stream(list).map(String::valueOf).collect(Collectors.joining(", "));
		assertEquals(0 , list.length, "printer list contains items even though there are no printers, printers from list are: "+printerNames);
	} else {
		/* If there is at least a default printer, verify
		 * that the printer list is not empty.
		 */
		PrinterData list[] = Printer.getPrinterList();
		assertTrue(list.length > 0, "printer list is empty");
	}
}

@Test
public void test_isAutoScalable() {
	PrinterData data = Printer.getDefaultPrinterData();
	assumeTrue (data != null, "if there aren't any printers, don't do this test");
	Printer printer = new Printer(data);
	boolean isAutoScalable = printer.isAutoScalable();
	assertFalse(isAutoScalable, "SWT doesnot auto-scale for Printer devices");
	printer.dispose();
}
}
