package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2002. All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.ColorDialog
 *
 * @see org.eclipse.swt.widgets.ColorDialog
 */
public class Test_org_eclipse_swt_widgets_ColorDialog extends Test_org_eclipse_swt_widgets_Dialog {

ColorDialog colorDialog;

public Test_org_eclipse_swt_widgets_ColorDialog(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	colorDialog = new ColorDialog(shell, SWT.NULL);
	setDialog(colorDialog);
}

protected void tearDown() {
}

public void test_ConstructorLorg_eclipse_swt_widgets_Shell() {
	new ColorDialog(shell);
	
	try {
		new ColorDialog(null);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_ShellI() {
	new ColorDialog(shell, SWT.NULL);
	
	try {
		new ColorDialog(null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_getRGB() {
	// tested in test_setRGBLorg_eclipse_swt_graphics_RGB
}

public void test_open() {
	warnUnimpl("Test test_open not written");
}

public void test_setRGBLorg_eclipse_swt_graphics_RGB() {
	RGB rgb = new RGB(0, 0, 0);

	assertTrue(":a:", colorDialog.getRGB() == null);	
		
	colorDialog.setRGB(rgb);
	assertTrue(":b:", colorDialog.getRGB() == rgb);

	if (fCheckSwtNullExceptions) {
		try {
			colorDialog.setRGB(null);
			fail("No exception thrown for rgb == null");
		}
		catch (IllegalArgumentException e) {
		}
	}
	assertTrue(":c:", colorDialog.getRGB() == rgb);	
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_ColorDialog((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Shell");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_ShellI");
	methodNames.addElement("test_getRGB");
	methodNames.addElement("test_open");
	methodNames.addElement("test_setRGBLorg_eclipse_swt_graphics_RGB");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Dialog.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Shell")) test_ConstructorLorg_eclipse_swt_widgets_Shell();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_ShellI")) test_ConstructorLorg_eclipse_swt_widgets_ShellI();
	else if (getName().equals("test_getRGB")) test_getRGB();
	else if (getName().equals("test_open")) test_open();
	else if (getName().equals("test_setRGBLorg_eclipse_swt_graphics_RGB")) test_setRGBLorg_eclipse_swt_graphics_RGB();
	else super.runTest();
}
}
