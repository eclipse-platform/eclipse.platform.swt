/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.Color
 *
 * @see org.eclipse.swt.graphics.Color
 */
public class Test_org_eclipse_swt_graphics_Color extends SwtTestCase {

public Test_org_eclipse_swt_graphics_Color(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	display = Display.getDefault();
	super.setUp();
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceIII() {
	// Test new Color(Device device, int red, int green, int blue)
	// IllegalArgumentException if the red, green or blue argument is not between 0 and 255

	// valid color (black)
	Color color = new Color(display, 0, 0, 0);
	color.dispose();
	
	// valid color (white)
	color = new Color(display, 255, 255, 255);
	color.dispose();
	
	// valid color (random grey)
	color = new Color(display, 20, 20, 20);
	color.dispose();

	// valid color (random)
	color = new Color(display, 102, 255, 0);
	color.dispose();
	
	// device == null (valid)
	color = new Color(null, 0, 0, 0);
	color.dispose();
	
	// illegal argument, rgb < 0
	try {
		color = new Color(display, -10, -10, -10);
		color.dispose();
		fail("No exception thrown for rgb < 0");
	} catch (IllegalArgumentException e) {
	}

	// illegal argument, rgb > 255
	try {
		color = new Color(display, 1000, 2000, 3000);
		color.dispose();
		fail("No exception thrown for rgb > 255");
	} catch (IllegalArgumentException e) {
	}
	// illegal argument, blue > 255
	try {
		color = new Color(display, 10, 10, 256);
		color.dispose();
		fail("No exception thrown for blue > 255");
	} catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_RGB() {
	// Test new Color(Device device, RGB rgb)
	// IllegalArgumentException if the red, green or blue argument is not between 0 and 255; or rgb is null
	
	// valid color (black)
	Color color = new Color(display, new RGB(0, 0, 0));
	color.dispose();
	
	// valid color (white)
	color = new Color(display, new RGB(255, 255, 255));
	color.dispose();
	
	// valid color (random grey)
	color = new Color(display, new RGB(10, 10, 10));
	color.dispose();
	
	// valid color (random)
	color = new Color(display, new RGB(102, 255, 0));
	color.dispose();
	
	// device == null (valid)
	color = new Color(null, new RGB(0, 0, 0));
	color.dispose();
	
	// illegal argument, rgb < 0
	try {
		color = new Color(display, new RGB(-10, -10, -10));
		color.dispose();
		fail("No exception thrown for rgb < 0");
	}
	catch (IllegalArgumentException e) {
	}
	// illegal argument, rgb > 255
	try {
		color = new Color(display, new RGB(1000, 2000, 3000));
		color.dispose();
		fail("No exception thrown for rgb > 255");
	}
	catch (IllegalArgumentException e) {
	}
	// illegal argument, blue > 255
	try {
		color = new Color(display, new RGB(10, 10, 256));
		color.dispose();
		fail("No exception thrown for blue > 255");
	}
	catch (IllegalArgumentException e) {
	}

	// illegal argument, rgb == null
	try {
		color = new Color(display, null);
		color.dispose();
		fail("No exception thrown for rgb == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_dispose() {
	// tested in test_isDisposed
}

public void test_equalsLjava_lang_Object() {
	Color color = new Color(display, 1, 2, 3);
	Color sameColor = new Color(display, 1, 2, 3);
	Color sameColor2 = new Color(display, new RGB(1, 2, 3));
	Color otherColor = new Color(display, 5, 6, 7);
	try {
		// Test Color.equals(Object)
		assertTrue("!color.equals((Object)null)", !color.equals((Object)null));

		// Test Color.equals(Color)
		assertTrue("!color.equals((Color)null)", !color.equals((Color)null));
		assertTrue("color.equals(color)", color.equals(color));
		assertTrue("color.equals(sameColor)", color.equals(sameColor));
		assertTrue("color.equals(sameColor2)", color.equals(sameColor2));
		assertTrue("!color.equals(otherColor)", !color.equals(otherColor));
	} finally {
		color.dispose();
		sameColor.dispose();
		sameColor2.dispose();
		otherColor.dispose();
	}
}

public void test_getBlue() {
	// Test Color.getBlue()
	Color color = new Color(display, 0, 0, 255);
	try {
		assertEquals("color.getBlue()", color.getBlue(), 255);
	} finally {
		color.dispose();
	}
	
}

public void test_getGreen() {
	// Test Color.getGreen()
	Color color = new Color(display, 0, 255, 0);
	try {
		assertEquals("color.getGreen()", color.getGreen(), 255);
	} finally {
		color.dispose();
	}
}

public void test_getRGB() {
	Color color = new Color(display, 255, 255, 255);
	assertNotNull(color.getRGB());
	assertEquals(new RGB(255, 255, 255), color.getRGB());
	color.dispose();
}

public void test_getRed() {
	// Test Color.getRed()
	Color color = new Color(display, 255, 0, 0);
	try {
		assertEquals("color.getRed()", color.getRed(), 255);
	} finally {
		color.dispose();
	}
}

public void test_hashCode() {
	Color color = new Color(display, 12, 34, 56);
	Color otherColor = new Color(display, 12, 34, 56);
	if (color.equals(otherColor)) {
		assertEquals("Hash codes of equal objects should be equal", color.hashCode(), otherColor.hashCode());
	}
	color.dispose();
	otherColor.dispose();
}

public void test_isDisposed() {
	// Test Color.isDisposed() false
	Color color = new Color(display, 34, 67, 98);
	try {
		assertTrue("Color should not be disposed", !color.isDisposed());
	} finally {
		// Test Color.isDisposed() true
		color.dispose();
		assertTrue("Color should be disposed", color.isDisposed());
	}
}

public void test_toString() {
	Color color = new Color(display, 0, 0, 255);
	try {
		assertNotNull(color.toString());
		assertTrue(color.toString().length() > 0);
		assertEquals("Color {0, 0, 255}", color.toString());
	} finally {
		color.dispose();
	}
}

public void test_win32_newLorg_eclipse_swt_graphics_DeviceI() {
	// do not test - Windows platform only
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_Color((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceIII");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_RGB");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_getBlue");
	methodNames.addElement("test_getGreen");
	methodNames.addElement("test_getRGB");
	methodNames.addElement("test_getRed");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_isDisposed");
	methodNames.addElement("test_toString");
	methodNames.addElement("test_win32_newLorg_eclipse_swt_graphics_DeviceI");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceIII")) test_ConstructorLorg_eclipse_swt_graphics_DeviceIII();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_RGB")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_RGB();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_getBlue")) test_getBlue();
	else if (getName().equals("test_getGreen")) test_getGreen();
	else if (getName().equals("test_getRGB")) test_getRGB();
	else if (getName().equals("test_getRed")) test_getRed();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_isDisposed")) test_isDisposed();
	else if (getName().equals("test_toString")) test_toString();
	else if (getName().equals("test_win32_newLorg_eclipse_swt_graphics_DeviceI")) test_win32_newLorg_eclipse_swt_graphics_DeviceI();
}

/* custom */
Display display;
}
