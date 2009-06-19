/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
import org.eclipse.swt.printing.*;

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

public void test_Constructor() {
	new PrinterData();
}

public void test_ConstructorLjava_lang_StringLjava_lang_String() {
	new PrinterData("hello", "there");
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
