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
import org.eclipse.swt.graphics.*;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.ImageData
 *
 * @see org.eclipse.swt.graphics.ImageData
 */
public class Test_org_eclipse_swt_graphics_ImageData extends SwtTestCase {

public Test_org_eclipse_swt_graphics_ImageData(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
}

protected void tearDown() {
}

public void test_ConstructorIIILorg_eclipse_swt_graphics_PaletteData() {
	// Tested in test_ConstructorIIILorg_eclipse_swt_graphics_PaletteDataI$B.
}

public void test_ConstructorIIILorg_eclipse_swt_graphics_PaletteDataI$B() {
	// illegal argument, width < 0
	try {
		new ImageData(-1, 1, 8, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 4, new byte[] {});
		fail("No exception thrown for width < 0");
	} catch (IllegalArgumentException e) {
	}

	// illegal argument, height < 0
	try {
		new ImageData(1, -1, 8, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 4, new byte[] {});
		fail("No exception thrown for height < 0");
	} catch (IllegalArgumentException e) {
	}

	// illegal argument, depth != 1, 2, 4, 8, 16, 24 or 32
	try {
		new ImageData(1, 1, 7, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 4, new byte[] {});
		fail("No exception thrown for depth != 1, 2, 4, 8, 16, 24 or 32");
	} catch (IllegalArgumentException e) {
	}

	// illegal argument, palette is null
	try {
		new ImageData(1, 1, 8, null, 4, new byte[] {});
		fail("No exception thrown for null palette");
	} catch (IllegalArgumentException e) {
	}

	// illegal argument, data is null
	try {
		new ImageData(1, 1, 8, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 4, null);
		fail("No exception thrown for null data");
	} catch (IllegalArgumentException e) {
	}

	// illegal argument, scanlinePad == 0
	try {
		new ImageData(1, 1, 8, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 0, new byte[] {});
		fail("No exception thrown for scanlinePad == 0");
	} catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLjava_io_InputStream() {
	warnUnimpl("Test test_ConstructorLjava_io_InputStream not written");
}

public void test_ConstructorLjava_lang_String() {
	warnUnimpl("Test test_ConstructorLjava_lang_String not written");
}

public void test_clone() {
	warnUnimpl("Test test_clone not written");
}

public void test_getAlphaII() {
	warnUnimpl("Test test_getAlphaII not written");
}

public void test_getAlphasIII$BI() {
	warnUnimpl("Test test_getAlphasIII$BI not written");
}

public void test_getPixelII() {
	warnUnimpl("Test test_getPixelII not written");
}

public void test_getPixelsIII$BI() {
	warnUnimpl("Test test_getPixelsIII$BI not written");
}

public void test_getPixelsIII$II() {
	warnUnimpl("Test test_getPixelsIII$II not written");
}

public void test_getRGBs() {
	warnUnimpl("Test test_getRGBs not written");
}

public void test_getTransparencyMask() {
	warnUnimpl("Test test_getTransparencyMask not written");
}

public void test_getTransparencyType() {
	warnUnimpl("Test test_getTransparencyType not written");
}

public void test_internal_newIIILorg_eclipse_swt_graphics_PaletteDataI$BI$B$BIIIIIII() {
	warnUnimpl("Test test_internal_newIIILorg_eclipse_swt_graphics_PaletteDataI$BI$B$BIIIIIII not written");
}

public void test_scaledToII() {
	warnUnimpl("Test test_scaledToII not written");
}

public void test_setAlphaIII() {
	warnUnimpl("Test test_setAlphaIII not written");
}

public void test_setAlphasIII$BI() {
	warnUnimpl("Test test_setAlphasIII$BI not written");
}

public void test_setPixelIII() {
	warnUnimpl("Test test_setPixelIII not written");
}

public void test_setPixelsIII$BI() {
	warnUnimpl("Test test_setPixelsIII$BI not written");
}

public void test_setPixelsIII$II() {
	warnUnimpl("Test test_setPixelsIII$II not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_ImageData((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorIIILorg_eclipse_swt_graphics_PaletteData");
	methodNames.addElement("test_ConstructorIIILorg_eclipse_swt_graphics_PaletteDataI$B");
	methodNames.addElement("test_ConstructorLjava_io_InputStream");
	methodNames.addElement("test_ConstructorLjava_lang_String");
	methodNames.addElement("test_clone");
	methodNames.addElement("test_getAlphaII");
	methodNames.addElement("test_getAlphasIII$BI");
	methodNames.addElement("test_getPixelII");
	methodNames.addElement("test_getPixelsIII$BI");
	methodNames.addElement("test_getPixelsIII$II");
	methodNames.addElement("test_getRGBs");
	methodNames.addElement("test_getTransparencyMask");
	methodNames.addElement("test_getTransparencyType");
	methodNames.addElement("test_internal_newIIILorg_eclipse_swt_graphics_PaletteDataI$BI$B$BIIIIIII");
	methodNames.addElement("test_scaledToII");
	methodNames.addElement("test_setAlphaIII");
	methodNames.addElement("test_setAlphasIII$BI");
	methodNames.addElement("test_setPixelIII");
	methodNames.addElement("test_setPixelsIII$BI");
	methodNames.addElement("test_setPixelsIII$II");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorIIILorg_eclipse_swt_graphics_PaletteData")) test_ConstructorIIILorg_eclipse_swt_graphics_PaletteData();
	else if (getName().equals("test_ConstructorIIILorg_eclipse_swt_graphics_PaletteDataI$B")) test_ConstructorIIILorg_eclipse_swt_graphics_PaletteDataI$B();
	else if (getName().equals("test_ConstructorLjava_io_InputStream")) test_ConstructorLjava_io_InputStream();
	else if (getName().equals("test_ConstructorLjava_lang_String")) test_ConstructorLjava_lang_String();
	else if (getName().equals("test_clone")) test_clone();
	else if (getName().equals("test_getAlphaII")) test_getAlphaII();
	else if (getName().equals("test_getAlphasIII$BI")) test_getAlphasIII$BI();
	else if (getName().equals("test_getPixelII")) test_getPixelII();
	else if (getName().equals("test_getPixelsIII$BI")) test_getPixelsIII$BI();
	else if (getName().equals("test_getPixelsIII$II")) test_getPixelsIII$II();
	else if (getName().equals("test_getRGBs")) test_getRGBs();
	else if (getName().equals("test_getTransparencyMask")) test_getTransparencyMask();
	else if (getName().equals("test_getTransparencyType")) test_getTransparencyType();
	else if (getName().equals("test_internal_newIIILorg_eclipse_swt_graphics_PaletteDataI$BI$B$BIIIIIII")) test_internal_newIIILorg_eclipse_swt_graphics_PaletteDataI$BI$B$BIIIIIII();
	else if (getName().equals("test_scaledToII")) test_scaledToII();
	else if (getName().equals("test_setAlphaIII")) test_setAlphaIII();
	else if (getName().equals("test_setAlphasIII$BI")) test_setAlphasIII$BI();
	else if (getName().equals("test_setPixelIII")) test_setPixelIII();
	else if (getName().equals("test_setPixelsIII$BI")) test_setPixelsIII$BI();
	else if (getName().equals("test_setPixelsIII$II")) test_setPixelsIII$II();
}
}
