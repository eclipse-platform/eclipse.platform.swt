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
 * Automated Test Suite for class org.eclipse.swt.graphics.Device
 *
 * @see org.eclipse.swt.graphics.Device
 */
public class Test_org_eclipse_swt_graphics_Device extends SwtTestCase {

public Test_org_eclipse_swt_graphics_Device(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
}

protected void tearDown() {
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceData() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_graphics_DeviceData not written");
}

public void test_dispose() {
	warnUnimpl("Test test_dispose not written");
}

public void test_getBounds() {
	warnUnimpl("Test test_getBounds not written");
}

public void test_getClientArea() {
	warnUnimpl("Test test_getClientArea not written");
}

public void test_getDPI() {
	warnUnimpl("Test test_getDPI not written");
}

public void test_getDepth() {
	warnUnimpl("Test test_getDepth not written");
}

public void test_getDeviceData() {
	warnUnimpl("Test test_getDeviceData not written");
}

public void test_getFontListLjava_lang_StringZ() {
	warnUnimpl("Test test_getFontListLjava_lang_StringZ not written");
}

public void test_getSystemColorI() {
	warnUnimpl("Test test_getSystemColorI not written");
}

public void test_getSystemFont() {
	warnUnimpl("Test test_getSystemFont not written");
}

public void test_getWarnings() {
	warnUnimpl("Test test_getWarnings not written");
}

public void test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData() {
	warnUnimpl("Test test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData not written");
}

public void test_internal_new_GCLorg_eclipse_swt_graphics_GCData() {
	warnUnimpl("Test test_internal_new_GCLorg_eclipse_swt_graphics_GCData not written");
}


public void test_isDisposed() {
	warnUnimpl("Test test_isDisposed not written");
}

public void test_setWarningsZ() {
	warnUnimpl("Test test_setWarningsZ not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_Device((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceData");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_getBounds");
	methodNames.addElement("test_getClientArea");
	methodNames.addElement("test_getDPI");
	methodNames.addElement("test_getDepth");
	methodNames.addElement("test_getDeviceData");
	methodNames.addElement("test_getFontListLjava_lang_StringZ");
	methodNames.addElement("test_getSystemColorI");
	methodNames.addElement("test_getSystemFont");
	methodNames.addElement("test_getWarnings");
	methodNames.addElement("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_internal_new_GCLorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_isDisposed");
	methodNames.addElement("test_setWarningsZ");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceData")) test_ConstructorLorg_eclipse_swt_graphics_DeviceData();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_getBounds")) test_getBounds();
	else if (getName().equals("test_getClientArea")) test_getClientArea();
	else if (getName().equals("test_getDPI")) test_getDPI();
	else if (getName().equals("test_getDepth")) test_getDepth();
	else if (getName().equals("test_getDeviceData")) test_getDeviceData();
	else if (getName().equals("test_getFontListLjava_lang_StringZ")) test_getFontListLjava_lang_StringZ();
	else if (getName().equals("test_getSystemColorI")) test_getSystemColorI();
	else if (getName().equals("test_getSystemFont")) test_getSystemFont();
	else if (getName().equals("test_getWarnings")) test_getWarnings();
	else if (getName().equals("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData")) test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_internal_new_GCLorg_eclipse_swt_graphics_GCData")) test_internal_new_GCLorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_isDisposed")) test_isDisposed();
	else if (getName().equals("test_setWarningsZ")) test_setWarningsZ();
}
}
