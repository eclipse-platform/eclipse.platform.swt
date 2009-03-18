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


import org.eclipse.swt.graphics.*;

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

public void test_Constructor$Lorg_eclipse_swt_graphics_RGB() {
	try {
		new PaletteData(null);
		fail("No exception thrown for rgb == null");
	}
	catch (IllegalArgumentException e) {
	}

	PaletteData data = new PaletteData(new RGB[] {});
	assertFalse(":a:", data.isDirect);

	new PaletteData(new RGB[] {null, null});
	assertFalse(":b:", data.isDirect);

	new PaletteData(new RGB[] {new RGB(0, 0, 0), new RGB(255, 255, 255)});
	assertFalse(":c:", data.isDirect);
}

public void test_ConstructorIII() {
	PaletteData data = new PaletteData(0, 0, 0);
	assertTrue(":a:", data.isDirect);

	data = new PaletteData(-1, -1, -1);
	assertTrue(":b:", data.isDirect);

	data = new PaletteData(0xff0000, 0x00ff00, 0x0000ff);
	assertTrue(":c:", data.isDirect);
}

public void test_getPixelLorg_eclipse_swt_graphics_RGB() {
	// indexed palette tests	
	RGB[] rgbs = {new RGB(0, 0, 0), new RGB(255, 255, 255), new RGB(50, 100, 150)};
	PaletteData data = new PaletteData(rgbs);
	
	try {
		data.getPixel(null);
		fail("No exception thrown for indexed palette with rgb == null");
	}
	catch (IllegalArgumentException e) {
	}
	
	try {
		data.getPixel(new RGB(0, 0, 1));
		fail("No exception thrown for rgb not found");
	}
	catch (IllegalArgumentException e) {
	}

	assertEquals(":a:", rgbs.length-1, data.getPixel(rgbs[rgbs.length-1]));
	
	// direct palette tests	
	RGB rgb =new RGB(0x32, 0x64, 0x96);
	data = new PaletteData(0xff0000, 0x00ff00, 0x0000ff);
	
	try {
		data.getPixel(null);
		fail("No exception thrown for direct palette with rgb == null");
	}
	catch (IllegalArgumentException e) {
	}

	assertEquals(":b:", 0x326496, data.getPixel(rgb));
}

public void test_getRGBI() {
	// indexed palette tests	
	RGB[] rgbs = {new RGB(0, 0, 0), new RGB(255, 255, 255), new RGB(50, 100, 150)};
	PaletteData data = new PaletteData(rgbs);

	try {
		data.getRGB(rgbs.length);
		fail("No exception thrown for nonexistent pixel");
	}
	catch (IllegalArgumentException e) {
	}

	assertEquals(":a:", rgbs[rgbs.length-1], data.getRGB(rgbs.length-1));

	// direct palette tests	
	RGB rgb =new RGB(0x32, 0x64, 0x96);
	data = new PaletteData(0xff0000, 0x00ff00, 0x0000ff);
	
	assertEquals(":b:", rgb, data.getRGB(0x326496));
}

public void test_getRGBs() {
	// indexed palette tests	
	RGB[] rgbs = {new RGB(0, 0, 0), new RGB(255, 255, 255)};
	PaletteData data = new PaletteData(rgbs);
	
	assertEquals(":a:", rgbs, data.getRGBs());
	
	// direct palette tests	
	data = new PaletteData(0xff0000, 0x00ff00, 0x0000ff);
	
	assertNull(":b:", data.getRGBs());
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
