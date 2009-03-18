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


import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.printing.*;

/**
 * Automated Test Suite for class org.eclipse.swt.printing.Printer
 *
 * @see org.eclipse.swt.printing.Printer
 */
public class Test_org_eclipse_swt_printing_Printer extends Test_org_eclipse_swt_graphics_Device {

public Test_org_eclipse_swt_printing_Printer(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

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

public void test_cancelJob() {
	warnUnimpl("Test test_cancelJob not written");
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

public void test_endJob() {
	warnUnimpl("Test test_endJob not written");
}

public void test_endPage() {
	warnUnimpl("Test test_endPage not written");
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

public void test_getDefaultPrinterData() {
	// Tested in test_getPrinterData.
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

public void test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData() {
	warnUnimpl("Test test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData not written");
}

public void test_internal_new_GCLorg_eclipse_swt_graphics_GCData() {
	warnUnimpl("Test test_internal_new_GCLorg_eclipse_swt_graphics_GCData not written");
}

public void test_startJobLjava_lang_String() {
	warnUnimpl("Test test_startJobLjava_lang_String not written");
}

public void test_startPage() {
	warnUnimpl("Test test_startPage not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_printing_Printer((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
//	methodNames.addElement("test_Constructor");
//	methodNames.addElement("test_ConstructorLorg_eclipse_swt_printing_PrinterData");
//	methodNames.addElement("test_cancelJob");
//	methodNames.addElement("test_computeTrimIIII");
//	methodNames.addElement("test_endJob");
//	methodNames.addElement("test_endPage");
//	methodNames.addElement("test_getBounds");
//	methodNames.addElement("test_getClientArea");
//	methodNames.addElement("test_getDPI");
//	methodNames.addElement("test_getDefaultPrinterData");
	methodNames.addElement("test_getPrinterData");
	methodNames.addElement("test_getPrinterList");
//	methodNames.addElement("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData");
//	methodNames.addElement("test_internal_new_GCLorg_eclipse_swt_graphics_GCData");
//	methodNames.addElement("test_startJobLjava_lang_String");
//	methodNames.addElement("test_startPage");
	methodNames.addAll(Test_org_eclipse_swt_graphics_Device.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_printing_PrinterData")) test_ConstructorLorg_eclipse_swt_printing_PrinterData();
	else if (getName().equals("test_cancelJob")) test_cancelJob();
	else if (getName().equals("test_computeTrimIIII")) test_computeTrimIIII();
	else if (getName().equals("test_endJob")) test_endJob();
	else if (getName().equals("test_endPage")) test_endPage();
	else if (getName().equals("test_getBounds")) test_getBounds();
	else if (getName().equals("test_getClientArea")) test_getClientArea();
	else if (getName().equals("test_getDPI")) test_getDPI();
	else if (getName().equals("test_getDefaultPrinterData")) test_getDefaultPrinterData();
	else if (getName().equals("test_getPrinterData")) test_getPrinterData();
	else if (getName().equals("test_getPrinterList")) test_getPrinterList();
	else if (getName().equals("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData")) test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_internal_new_GCLorg_eclipse_swt_graphics_GCData")) test_internal_new_GCLorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_startJobLjava_lang_String")) test_startJobLjava_lang_String();
	else if (getName().equals("test_startPage")) test_startPage();
	else super.runTest();
}
}
