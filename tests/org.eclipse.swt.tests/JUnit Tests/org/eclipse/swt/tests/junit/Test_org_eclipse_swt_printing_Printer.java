/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import junit.framework.*;
import junit.textui.*;

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

protected void setUp() {
	super.setUp();
}

protected void tearDown() {
	super.tearDown();
}

public void test_getPrinterList() {
	warnUnimpl("Test test_getPrinterList not written");
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public void test_ConstructorLorg_eclipse_swt_printing_PrinterData() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_printing_PrinterData not written");
}

public void test_createLorg_eclipse_swt_graphics_DeviceData() {
	warnUnimpl("Test test_createLorg_eclipse_swt_graphics_DeviceData not written");
}

public void test_internal_new_GCLorg_eclipse_swt_graphics_GCData() {
	warnUnimpl("Test test_internal_new_GCLorg_eclipse_swt_graphics_GCData not written");
}

public void test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData() {
	warnUnimpl("Test test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData not written");
}

public void test_startJobLjava_lang_String() {
	warnUnimpl("Test test_startJobLjava_lang_String not written");
}

public void test_endJob() {
	warnUnimpl("Test test_endJob not written");
}

public void test_cancelJob() {
	warnUnimpl("Test test_cancelJob not written");
}

public void test_startPage() {
	warnUnimpl("Test test_startPage not written");
}

public void test_endPage() {
	warnUnimpl("Test test_endPage not written");
}

public void test_getDPI() {
	warnUnimpl("Test test_getDPI not written");
}

public void test_getBounds() {
	warnUnimpl("Test test_getBounds not written");
}

public void test_getClientArea() {
	warnUnimpl("Test test_getClientArea not written");
}

public void test_computeTrimIIII() {
	warnUnimpl("Test test_computeTrimIIII not written");
}

public void test_getPrinterData() {
	warnUnimpl("Test test_getPrinterData not written");
}

public void test_checkDevice() {
	warnUnimpl("Test test_checkDevice not written");
}

public void test_release() {
	warnUnimpl("Test test_release not written");
}

public void test_destroy() {
	warnUnimpl("Test test_destroy not written");
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
	methodNames.addElement("test_getPrinterList");
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_printing_PrinterData");
	methodNames.addElement("test_createLorg_eclipse_swt_graphics_DeviceData");
	methodNames.addElement("test_internal_new_GCLorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_startJobLjava_lang_String");
	methodNames.addElement("test_endJob");
	methodNames.addElement("test_cancelJob");
	methodNames.addElement("test_startPage");
	methodNames.addElement("test_endPage");
	methodNames.addElement("test_getDPI");
	methodNames.addElement("test_getBounds");
	methodNames.addElement("test_getClientArea");
	methodNames.addElement("test_computeTrimIIII");
	methodNames.addElement("test_getPrinterData");
	methodNames.addElement("test_checkDevice");
	methodNames.addElement("test_release");
	methodNames.addElement("test_destroy");
	methodNames.addAll(Test_org_eclipse_swt_graphics_Device.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_getPrinterList")) test_getPrinterList();
	else if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_printing_PrinterData")) test_ConstructorLorg_eclipse_swt_printing_PrinterData();
	else if (getName().equals("test_createLorg_eclipse_swt_graphics_DeviceData")) test_createLorg_eclipse_swt_graphics_DeviceData();
	else if (getName().equals("test_internal_new_GCLorg_eclipse_swt_graphics_GCData")) test_internal_new_GCLorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData")) test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_startJobLjava_lang_String")) test_startJobLjava_lang_String();
	else if (getName().equals("test_endJob")) test_endJob();
	else if (getName().equals("test_cancelJob")) test_cancelJob();
	else if (getName().equals("test_startPage")) test_startPage();
	else if (getName().equals("test_endPage")) test_endPage();
	else if (getName().equals("test_getDPI")) test_getDPI();
	else if (getName().equals("test_getBounds")) test_getBounds();
	else if (getName().equals("test_getClientArea")) test_getClientArea();
	else if (getName().equals("test_computeTrimIIII")) test_computeTrimIIII();
	else if (getName().equals("test_getPrinterData")) test_getPrinterData();
	else if (getName().equals("test_checkDevice")) test_checkDevice();
	else if (getName().equals("test_release")) test_release();
	else if (getName().equals("test_destroy")) test_destroy();
	else super.runTest();
}
}
