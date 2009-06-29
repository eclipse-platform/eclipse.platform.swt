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
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.FontDialog
 *
 * @see org.eclipse.swt.widgets.FontDialog
 */
public class Test_org_eclipse_swt_widgets_FontDialog extends Test_org_eclipse_swt_widgets_Dialog {

public Test_org_eclipse_swt_widgets_FontDialog(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	fontDialog = new FontDialog(shell, SWT.NULL);
	setDialog(fontDialog);
}

public void test_ConstructorLorg_eclipse_swt_widgets_Shell() {
	new FontDialog(shell);
	try {
		new FontDialog(null);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_ShellI() {
	try {
		new FontDialog(null, SWT.NULL);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_getFontData() {
	// tested in test_setFontDataLorg_eclipse_swt_graphics_FontData
}

public void test_getFontList() {
	warnUnimpl("Test test_getFontList not written");
}

public void test_getRGB() {
	// tested in test_setRGBLorg_eclipse_swt_graphics_RGB
}

public void test_open() {
	warnUnimpl("Test test_open not written");
}

public void test_setFontDataLorg_eclipse_swt_graphics_FontData() {
	FontData [] fontData = new FontData [1];
	fontData [0] = new FontData ();

	assertNull(fontDialog.getFontList());	
		
	fontDialog.setFontList(fontData);
	assertEquals(fontDialog.getFontList(), fontData);

	fontDialog.setFontList(null);
	assertNull(fontDialog.getFontList());	
}

public void test_setFontList$Lorg_eclipse_swt_graphics_FontData() {
	warnUnimpl("Test test_setFontList$Lorg_eclipse_swt_graphics_FontData not written");
}

public void test_setRGBLorg_eclipse_swt_graphics_RGB() {
	RGB rgb = new RGB(255, 0, 0);
	fontDialog.setRGB(rgb);
	RGB rgb2 = fontDialog.getRGB();
	assertEquals(rgb, rgb2);	
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_FontDialog((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Shell");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_ShellI");
	methodNames.addElement("test_getFontData");
	methodNames.addElement("test_getFontList");
	methodNames.addElement("test_getRGB");
	methodNames.addElement("test_open");
	methodNames.addElement("test_setFontDataLorg_eclipse_swt_graphics_FontData");
	methodNames.addElement("test_setFontList$Lorg_eclipse_swt_graphics_FontData");
	methodNames.addElement("test_setRGBLorg_eclipse_swt_graphics_RGB");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Dialog.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Shell")) test_ConstructorLorg_eclipse_swt_widgets_Shell();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_ShellI")) test_ConstructorLorg_eclipse_swt_widgets_ShellI();
	else if (getName().equals("test_getFontData")) test_getFontData();
	else if (getName().equals("test_getFontList")) test_getFontList();
	else if (getName().equals("test_getRGB")) test_getRGB();
	else if (getName().equals("test_open")) test_open();
	else if (getName().equals("test_setFontDataLorg_eclipse_swt_graphics_FontData")) test_setFontDataLorg_eclipse_swt_graphics_FontData();
	else if (getName().equals("test_setFontList$Lorg_eclipse_swt_graphics_FontData")) test_setFontList$Lorg_eclipse_swt_graphics_FontData();
	else if (getName().equals("test_setRGBLorg_eclipse_swt_graphics_RGB")) test_setRGBLorg_eclipse_swt_graphics_RGB();
	else super.runTest();
}

/* custom */
FontDialog fontDialog;
}
