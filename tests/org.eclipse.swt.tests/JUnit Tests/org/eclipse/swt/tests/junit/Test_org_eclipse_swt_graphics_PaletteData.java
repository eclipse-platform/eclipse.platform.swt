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
 * Automated Test Suite for class org.eclipse.swt.graphics.PaletteData
 *
 * @see org.eclipse.swt.graphics.PaletteData
 */
public class Test_org_eclipse_swt_graphics_PaletteData extends SwtTestCase {

public Test_org_eclipse_swt_graphics_PaletteData(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
}

protected void tearDown() {
}

public void test_Constructor$Lorg_eclipse_swt_graphics_RGB() {
	warnUnimpl("Test test_Constructor$Lorg_eclipse_swt_graphics_RGB not written");
}

public void test_ConstructorIII() {
	warnUnimpl("Test test_ConstructorIII not written");
}

public void test_getPixelLorg_eclipse_swt_graphics_RGB() {
	warnUnimpl("Test test_getPixelLorg_eclipse_swt_graphics_RGB not written");
}

public void test_getRGBI() {
	warnUnimpl("Test test_getRGBI not written");
}

public void test_getRGBs() {
	warnUnimpl("Test test_getRGBs not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_PaletteData((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor$Lorg_eclipse_swt_graphics_RGB");
	methodNames.addElement("test_ConstructorIII");
	methodNames.addElement("test_getPixelLorg_eclipse_swt_graphics_RGB");
	methodNames.addElement("test_getRGBI");
	methodNames.addElement("test_getRGBs");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor$Lorg_eclipse_swt_graphics_RGB")) test_Constructor$Lorg_eclipse_swt_graphics_RGB();
	else if (getName().equals("test_ConstructorIII")) test_ConstructorIII();
	else if (getName().equals("test_getPixelLorg_eclipse_swt_graphics_RGB")) test_getPixelLorg_eclipse_swt_graphics_RGB();
	else if (getName().equals("test_getRGBI")) test_getRGBI();
	else if (getName().equals("test_getRGBs")) test_getRGBs();
}
}
