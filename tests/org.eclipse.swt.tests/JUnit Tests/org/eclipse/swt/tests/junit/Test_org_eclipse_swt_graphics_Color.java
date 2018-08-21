/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.Color
 *
 * @see org.eclipse.swt.graphics.Color
 */
public class Test_org_eclipse_swt_graphics_Color {

@Before
public void setUp() {
	display = Display.getDefault();
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceIII() {
	// Test new Color(Device device, int red, int green, int blue)
	// IllegalArgumentException if the red, green or blue argument is not between 0 and 255

	// valid color (black)
	Color color = new Color(display, 0, 0, 0);
	color.dispose();

	// valid color (black with alpha)
	color = new Color(display, 0, 0, 0, 0);
	color.dispose();

	// valid color (white)
	color = new Color(display, 255, 255, 255);
	color.dispose();

	// valid color (white with alpha)
	color = new Color(display, 255, 255, 255, 0);
	color.dispose();

	// valid color (random grey)
	color = new Color(display, 20, 20, 20);
	color.dispose();

	// valid color (random grey with alpha)
	color = new Color(display, 20, 20, 20, 0);
	color.dispose();

	// valid color (random)
	color = new Color(display, 102, 255, 0);
	color.dispose();

	// valid color (random with alpha)
	color = new Color(display, 102, 255, 0, 0);
	color.dispose();

	// device == null (valid)
	color = new Color(null, 0, 0, 0);
	color.dispose();

	// device == null (valid with alpha)
	color = new Color(null, 0, 0, 0, 0);
	color.dispose();

	// illegal argument, rgb < 0
	try {
		color = new Color(display, -10, -10, -10);
		color.dispose();
		fail("No exception thrown for rgb < 0");
	} catch (IllegalArgumentException e) {
	}

	// illegal argument, alpha < 0
	try {
		color = new Color(display, 0, 0, 0, -10);
		color.dispose();
		fail("No exception thrown for rgba < 0");
	} catch (IllegalArgumentException e) {
	}

	// illegal argument, rgb > 255
	try {
		color = new Color(display, 1000, 2000, 3000);
		color.dispose();
		fail("No exception thrown for rgb > 255");
	} catch (IllegalArgumentException e) {
	}

	// illegal argument, rgba > 255
	try {
		color = new Color(display, 1000, 2000, 3000, 4000);
		color.dispose();
		fail("No exception thrown for rgba > 255");
	} catch (IllegalArgumentException e) {
	}

	// illegal argument, blue > 255
	try {
		color = new Color(display, 10, 10, 256);
		color.dispose();
		fail("No exception thrown for blue > 255");
	} catch (IllegalArgumentException e) {
	}

	// illegal argument, alpha > 255
	try {
		color = new Color(display, 10, 10, 10, 256);
		color.dispose();
		fail("No exception thrown for alpha > 255");
	} catch (IllegalArgumentException e) {
	}
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_RGB() {
	// Test new Color(Device device, RGB rgb)
	// IllegalArgumentException if the red, green or blue argument is not between 0 and 255; or rgb is null

	// valid color (black)
	Color color = new Color(display, new RGB(0, 0, 0));
	color.dispose();

	// valid color (black with alpha)
	color = new Color(display, new RGB(0, 0, 0), 0);
	color.dispose();

	// valid color (white)
	color = new Color(display, new RGB(255, 255, 255));
	color.dispose();

	// valid color (white with alpha)
	color = new Color(display, new RGB(255, 255, 255), 0);
	color.dispose();

	// valid color (random grey)
	color = new Color(display, new RGB(10, 10, 10));
	color.dispose();

	// valid color (random grey with alpha)
	color = new Color(display, new RGB(10, 10, 10), 0);
	color.dispose();

	// valid color (random)
	color = new Color(display, new RGB(102, 255, 0));
	color.dispose();

	// valid color (random with alpha)
	color = new Color(display, new RGB(102, 255, 0), 0);
	color.dispose();

	// device == null (valid)
	color = new Color(null, new RGB(0, 0, 0));
	color.dispose();

	// device == null (valid with alpha)
	color = new Color(null, new RGB(0, 0, 0), 0);
	color.dispose();

	// illegal argument, rgb < 0
	try {
		color = new Color(display, new RGB(-10, -10, -10));
		color.dispose();
		fail("No exception thrown for rgb < 0");
	}
	catch (IllegalArgumentException e) {
	}
	// illegal argument, alpha < 0
	try {
		color = new Color(display, new RGB(0, 0, 0), -10);
		color.dispose();
		fail("No exception thrown for rgba < 0");
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
	// illegal argument, rgba > 255
		try {
			color = new Color(display, new RGB(1000, 2000, 3000), 4000);
			color.dispose();
			fail("No exception thrown for rgba > 255");
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
	// illegal argument, alpha > 255
		try {
			color = new Color(display, new RGB(10, 10, 10), 256);
			color.dispose();
			fail("No exception thrown for alpha > 255");
		}
		catch (IllegalArgumentException e) {
		}
	// illegal argument, rgb == null with alpha
	try {
		color = new Color(display, null, 0);
		color.dispose();
		fail("No exception thrown for rgb == null with alpha");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_RGBA() {
	// Test new Color(Device device, RGBA rgba)
	// IllegalArgumentException if the red, green, blue or alpha argument is not between 0 and 255; or rgba is null

	// valid color (black)
	Color color = new Color(display, new RGBA(0, 0, 0, 255));
	color.dispose();

	// valid color (black with alpha)
	color = new Color(display, new RGBA(0, 0, 0, 0));
	color.dispose();

	// valid color (white)
	color = new Color(display, new RGBA(255, 255, 255, 255));
	color.dispose();

	// valid color (white with alpha)
	color = new Color(display, new RGBA(255, 255, 255, 0));
	color.dispose();

	// valid color (random grey)
	color = new Color(display, new RGBA(10, 10, 10, 10));
	color.dispose();

	// valid color (random grey with alpha)
	color = new Color(display, new RGBA(10, 10, 10, 0));
	color.dispose();

	// valid color (random)
	color = new Color(display, new RGBA(102, 255, 0, 255));
	color.dispose();

	// valid color (random with alpha)
	color = new Color(display, new RGBA(102, 255, 0, 0));
	color.dispose();

	// device == null (valid)
	color = new Color(null, new RGBA(0, 0, 0, 255));
	color.dispose();

	// device == null (valid with alpha)
	color = new Color(null, new RGBA(0, 0, 0, 0));
	color.dispose();

	// illegal argument, rgba < 0
	try {
		color = new Color(display, new RGBA(-10, -10, -10, -10));
		color.dispose();
		fail("No exception thrown for rgba < 0");
	}
	catch (IllegalArgumentException e) {
	}
	// illegal argument, alpha < 0
	try {
		color = new Color(display, new RGBA(0, 0, 0, -10));
		color.dispose();
		fail("No exception thrown for alpha < 0");
	}
	catch (IllegalArgumentException e) {
	}
	// illegal argument, rgba > 255
	try {
		color = new Color(display, new RGBA(1000, 2000, 3000, 4000));
		color.dispose();
		fail("No exception thrown for rgba > 255");
	}
	catch (IllegalArgumentException e) {
	}
	// illegal argument, blue > 255
	try {
		color = new Color(display, new RGBA(10, 10, 256, 10));
		color.dispose();
		fail("No exception thrown for blue > 255");
	}
	catch (IllegalArgumentException e) {
	}
	// illegal argument, alpha > 255
	try {
		color = new Color(display, new RGBA(10, 10, 10, 256));
		color.dispose();
		fail("No exception thrown for alpha > 255");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
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

	// With alpha
	color = new Color(display, 1, 2, 3, 0);
	sameColor = new Color(display, 1, 2, 3, 0);
	sameColor2 = new Color(display, new RGB(1, 2, 3), 0);
	otherColor = new Color(display, 5, 6, 7, 0);
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

@Test
public void test_getBlue() {
	// Test Color.getBlue()
	Color color = new Color(display, 0, 0, 255);
	try {
		assertEquals("color.getBlue()", color.getBlue(), 255);
	} finally {
		color.dispose();
	}

}

@Test
public void test_getGreen() {
	// Test Color.getGreen()
	Color color = new Color(display, 0, 255, 0);
	try {
		assertEquals("color.getGreen()", color.getGreen(), 255);
	} finally {
		color.dispose();
	}
}

@Test
public void test_getRGB() {
	Color color = new Color(display, 255, 255, 255);
	assertNotNull(color.getRGB());
	assertEquals(new RGB(255, 255, 255), color.getRGB());
	color.dispose();
}

@Test
public void test_getRed() {
	// Test Color.getRed()
	Color color = new Color(display, 255, 0, 0);
	try {
		assertEquals("color.getRed()", color.getRed(), 255);
	} finally {
		color.dispose();
	}
}

@Test
public void test_getAlpha() {
	// Test Color.getRed()
	Color color = new Color(display, 255, 0, 0, 0);
	try {
		assertEquals("color.getAlpha()", color.getAlpha(), 0);
	} finally {
		color.dispose();
	}
}

@Test
public void test_hashCode() {
	Color color = new Color(display, 12, 34, 56, 0);
	Color otherColor = new Color(display, 12, 34, 56, 0);
	if (color.equals(otherColor)) {
		assertEquals("Hash codes of equal objects should be equal", color.hashCode(), otherColor.hashCode());
	}
	color.dispose();
	otherColor.dispose();
}

@Test
public void test_isDisposed() {
	// Test Color.isDisposed() false
	Color color = new Color(display, 34, 67, 98, 0);
	try {
		assertTrue("Color should not be disposed", !color.isDisposed());
	} finally {
		// Test Color.isDisposed() true
		color.dispose();
		assertTrue("Color should be disposed", color.isDisposed());
	}
}

@Test
public void test_toString() {
	Color color = new Color(display, 0, 0, 255, 255);
	try {
		assertNotNull(color.toString());
		assertTrue(color.toString().length() > 0);
		assertEquals("Color {0, 0, 255, 255}", color.toString());
	} finally {
		color.dispose();
	}
}

/* custom */
Display display;
}
