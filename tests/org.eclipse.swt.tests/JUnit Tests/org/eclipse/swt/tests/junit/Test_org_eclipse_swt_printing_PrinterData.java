package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2002. All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.printing.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.printing.PrinterData
 *
 * @see org.eclipse.swt.printing.PrinterData
 */
public class Test_org_eclipse_swt_printing_PrinterData extends Test_org_eclipse_swt_graphics_DeviceData {

public Test_org_eclipse_swt_printing_PrinterData(String name) {
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

public void test_Constructor() {
	PrinterData data = new PrinterData();
}

public void test_ConstructorLjava_lang_StringLjava_lang_String() {
	PrinterData data = new PrinterData("hello", "there");
}

public void test_toString() {
	PrinterData data = new PrinterData();
	assertNotNull(data.toString());
	assertTrue(data.toString().length() > 0);
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_printing_PrinterData((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_ConstructorLjava_lang_StringLjava_lang_String");
	methodNames.addElement("test_toString");
	methodNames.addAll(Test_org_eclipse_swt_graphics_DeviceData.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_ConstructorLjava_lang_StringLjava_lang_String")) test_ConstructorLjava_lang_StringLjava_lang_String();
	else if (getName().equals("test_toString")) test_toString();
	else super.runTest();
}
}
