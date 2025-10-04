/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
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


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.widgets.Display;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.Color
 *
 * Within this test are tests that cover use of constructors with device
 * and those without.
 *
 * @see org.eclipse.swt.graphics.Color
 */
public class Test_org_eclipse_swt_graphics_Color {

	@BeforeEach
public void setUp() {
	display = Display.getDefault();
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceIII() {
	// Test new Color(int red, int green, int blue)
	// IllegalArgumentException if the red, green or blue argument is not between 0 and 255

	// valid color (black)
	@SuppressWarnings("unused")
	Color color = new Color(0, 0, 0);

	// valid color (black with alpha)
	color = new Color(0, 0, 0, 0);

	// valid color (white)
	color = new Color(255, 255, 255);

	// valid color (white with alpha)
	color = new Color(255, 255, 255, 0);

	// valid color (random grey)
	color = new Color(20, 20, 20);

	// valid color (random grey with alpha)
	color = new Color(20, 20, 20, 0);

	// valid color (random)
	color = new Color(102, 255, 0);

	// valid color (random with alpha)
	color = new Color(102, 255, 0, 0);

	// illegal argument, rgb < 0
	assertThrows(IllegalArgumentException.class, () -> new Color(-10, -10, -10));

	// illegal argument, alpha < 0
	assertThrows(IllegalArgumentException.class, () -> new Color(0, 0, 0, -10));

	// illegal argument, rgb > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(1000, 2000, 3000));

	// illegal argument, rgba > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(1000, 2000, 3000, 4000));

	// illegal argument, blue > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(10, 10, 256));

	// illegal argument, alpha > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(10, 10, 10, 256));
}

@Test
@SuppressWarnings("unused")
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceIII_with_device() {
	// Test new Color(Device device, int red, int green, int blue)
	// IllegalArgumentException if the red, green or blue argument is not between 0 and 255

	// valid color (black)
	Color color = new Color(display, 0, 0, 0);

	// valid color (black with alpha)
	color = new Color(display, 0, 0, 0, 0);

	// valid color (white)
	color = new Color(display, 255, 255, 255);

	// valid color (white with alpha)
	color = new Color(display, 255, 255, 255, 0);

	// valid color (random grey)
	color = new Color(display, 20, 20, 20);

	// valid color (random grey with alpha)
	color = new Color(display, 20, 20, 20, 0);

	// valid color (random)
	color = new Color(display, 102, 255, 0);

	// valid color (random with alpha)
	color = new Color(display, 102, 255, 0, 0);

	// device == null (valid)
	color = new Color(null, 0, 0, 0);

	// device == null (valid with alpha)
	color = new Color(null, 0, 0, 0, 0);

	// illegal argument, rgb < 0
	assertThrows(IllegalArgumentException.class, () -> new Color(display, -10, -10, -10));

	// illegal argument, alpha < 0
	assertThrows(IllegalArgumentException.class, () -> new Color(display, 0, 0, 0, -10));

	// illegal argument, rgb > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(display, 1000, 2000, 3000));

	// illegal argument, rgba > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(display, 1000, 2000, 3000, 4000));

	// illegal argument, blue > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(display, 10, 10, 256));

	// illegal argument, alpha > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(display, 10, 10, 10, 256));
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_RGB() {
	// Test new Color(RGB rgb)
	// IllegalArgumentException if the red, green or blue argument is not between 0 and 255; or rgb is null

	// valid color (black)
	@SuppressWarnings("unused")
	Color color = new Color(new RGB(0, 0, 0));

	// valid color (black with alpha)
	color = new Color(new RGB(0, 0, 0), 0);

	// valid color (white)
	color = new Color(new RGB(255, 255, 255));

	// valid color (white with alpha)
	color = new Color(new RGB(255, 255, 255), 0);

	// valid color (random grey)
	color = new Color(new RGB(10, 10, 10));

	// valid color (random grey with alpha)
	color = new Color(new RGB(10, 10, 10), 0);

	// valid color (random)
	color = new Color(new RGB(102, 255, 0));

	// valid color (random with alpha)
	color = new Color(new RGB(102, 255, 0), 0);

	// illegal argument, rgb < 0
	assertThrows(IllegalArgumentException.class, () -> new Color(new RGB(-10, -10, -10)));

	// illegal argument, alpha < 0
	assertThrows(IllegalArgumentException.class, () -> new Color(new RGB(0, 0, 0), -10));

	// illegal argument, rgb > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(new RGB(1000, 2000, 3000)));

	// illegal argument, rgba > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(new RGB(1000, 2000, 3000), 4000));

	// illegal argument, blue > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(new RGB(10, 10, 256)));

	// illegal argument, alpha > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(new RGB(10, 10, 10), 256));

	// illegal argument, rgb == null with alpha
	assertThrows(IllegalArgumentException.class, () -> new Color(null, 0));
}

@Test
@SuppressWarnings("unused")
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_RGB_with_device() {
	// Test new Color(Device device, RGB rgb)
	// IllegalArgumentException if the red, green or blue argument is not between 0 and 255; or rgb is null

	// valid color (black)
	Color color = new Color(display, new RGB(0, 0, 0));

	// valid color (black with alpha)
	color = new Color(display, new RGB(0, 0, 0), 0);

	// valid color (white)
	color = new Color(display, new RGB(255, 255, 255));

	// valid color (white with alpha)
	color = new Color(display, new RGB(255, 255, 255), 0);

	// valid color (random grey)
	color = new Color(display, new RGB(10, 10, 10));

	// valid color (random grey with alpha)
	color = new Color(display, new RGB(10, 10, 10), 0);

	// valid color (random)
	color = new Color(display, new RGB(102, 255, 0));

	// valid color (random with alpha)
	color = new Color(display, new RGB(102, 255, 0), 0);

	// device == null (valid)
	color = new Color(null, new RGB(0, 0, 0));

	// device == null (valid with alpha)
	color = new Color(null, new RGB(0, 0, 0), 0);

	// illegal argument, rgb < 0
	assertThrows(IllegalArgumentException.class, () -> new Color(display, new RGB(-10, -10, -10)));

	// illegal argument, alpha < 0
	assertThrows(IllegalArgumentException.class, () -> new Color(display, new RGB(0, 0, 0), -10));

	// illegal argument, rgb > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(display, new RGB(1000, 2000, 3000)));

	// illegal argument, rgba > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(display, new RGB(1000, 2000, 3000), 4000));

	// illegal argument, blue > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(display, new RGB(10, 10, 256)));

	// illegal argument, alpha > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(display, new RGB(10, 10, 10), 256));

	// illegal argument, rgb == null with alpha
	assertThrows(IllegalArgumentException.class, () -> new Color(display, null, 0));
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_RGBA() {
	// Test new Color(RGBA rgba)
	// IllegalArgumentException if the red, green, blue or alpha argument is not between 0 and 255; or rgba is null

	// valid color (black)
	@SuppressWarnings("unused")
	Color color = new Color(new RGBA(0, 0, 0, 255));

	// valid color (black with alpha)
	color = new Color(new RGBA(0, 0, 0, 0));

	// valid color (white)
	color = new Color(new RGBA(255, 255, 255, 255));

	// valid color (white with alpha)
	color = new Color(new RGBA(255, 255, 255, 0));

	// valid color (random grey)
	color = new Color(new RGBA(10, 10, 10, 10));

	// valid color (random grey with alpha)
	color = new Color(new RGBA(10, 10, 10, 0));

	// valid color (random)
	color = new Color(new RGBA(102, 255, 0, 255));

	// valid color (random with alpha)
	color = new Color(new RGBA(102, 255, 0, 0));

	// illegal argument, rgba < 0
	assertThrows(IllegalArgumentException.class, () -> new Color(new RGBA(-10, -10, -10, -10)));

	// illegal argument, alpha < 0
	assertThrows(IllegalArgumentException.class, () -> new Color(new RGBA(0, 0, 0, -10)));

	// illegal argument, rgba > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(new RGBA(1000, 2000, 3000, 4000)));

	// illegal argument, blue > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(new RGBA(10, 10, 256, 10)));

	// illegal argument, alpha > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(new RGBA(10, 10, 10, 256)));
}

@Test
@SuppressWarnings("unused")
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_RGBA_with_device() {
	// Test new Color(Device device, RGBA rgba)
	// IllegalArgumentException if the red, green, blue or alpha argument is not between 0 and 255; or rgba is null

	// valid color (black)
	Color color = new Color(display, new RGBA(0, 0, 0, 255));

	// valid color (black with alpha)
	color = new Color(display, new RGBA(0, 0, 0, 0));

	// valid color (white)
	color = new Color(display, new RGBA(255, 255, 255, 255));

	// valid color (white with alpha)
	color = new Color(display, new RGBA(255, 255, 255, 0));

	// valid color (random grey)
	color = new Color(display, new RGBA(10, 10, 10, 10));

	// valid color (random grey with alpha)
	color = new Color(display, new RGBA(10, 10, 10, 0));

	// valid color (random)
	color = new Color(display, new RGBA(102, 255, 0, 255));

	// valid color (random with alpha)
	color = new Color(display, new RGBA(102, 255, 0, 0));

	// device == null (valid)
	color = new Color(null, new RGBA(0, 0, 0, 255));

	// device == null (valid with alpha)
	color = new Color(null, new RGBA(0, 0, 0, 0));

	// illegal argument, rgba < 0
	assertThrows(IllegalArgumentException.class, () -> new Color(display, new RGBA(-10, -10, -10, -10)));

	// illegal argument, alpha < 0
	assertThrows(IllegalArgumentException.class, () -> new Color(display, new RGBA(0, 0, 0, -10)));

	// illegal argument, rgba > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(display, new RGBA(1000, 2000, 3000, 4000)));

	// illegal argument, blue > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(display, new RGBA(10, 10, 256, 10)));

	// illegal argument, alpha > 255
	assertThrows(IllegalArgumentException.class, () -> new Color(display, new RGBA(10, 10, 10, 256)));
}

@Test
public void test_equalsLjava_lang_Object() {
	Color color = new Color(1, 2, 3);
	Color sameColor = new Color(1, 2, 3);
	Color sameColor2 = new Color(new RGB(1, 2, 3));
	Color otherColor = new Color(5, 6, 7);
	Color disposedColor = new Color(1, 2, 3);
	Color disposedColor2 = new Color(5, 6, 7);

	// Test Color.equals(Object)
	assertFalse(color.equals((Object)null));

	// Test Color.equals(Color)
	assertFalse(color.equals((Color)null));
	assertTrue(color.equals(color));
	assertTrue(color.equals(sameColor));
	assertTrue(color.equals(sameColor2));
	assertFalse(color.equals(otherColor));

	// With alpha
	color = new Color(1, 2, 3, 0);
	sameColor = new Color(1, 2, 3, 0);
	sameColor2 = new Color(new RGB(1, 2, 3), 0);
	otherColor = new Color(5, 6, 7, 0);
	// Test Color.equals(Object)
	assertFalse(color.equals((Object)null));

	// Test Color.equals(Color)
	assertFalse(color.equals((Color)null));
	assertTrue(color.equals(color));
	assertTrue(color.equals(sameColor));
	assertTrue(color.equals(sameColor2));
	assertFalse(color.equals(otherColor));

	// Test Color.equals(Color)
	assertFalse(color.equals(disposedColor));
	assertFalse(disposedColor.equals(color));
	assertFalse(disposedColor.equals(disposedColor2));
	assertFalse(disposedColor2.equals(disposedColor));
}

@Test
public void test_equalsLjava_lang_Object_with_device() {
	Color color = new Color(display, 1, 2, 3);
	Color sameColor = new Color(display, 1, 2, 3);
	Color sameColor2 = new Color(display, new RGB(1, 2, 3));
	Color otherColor = new Color(display, 5, 6, 7);
	// Test Color.equals(Object)
	assertFalse(color.equals((Object)null));

	// Test Color.equals(Color)
	assertFalse(color.equals((Color)null));
	assertTrue(color.equals(color));
	assertTrue(color.equals(sameColor));
	assertTrue(color.equals(sameColor2));
	assertFalse(color.equals(otherColor));

	// With alpha
	color = new Color(display, 1, 2, 3, 0);
	sameColor = new Color(display, 1, 2, 3, 0);
	sameColor2 = new Color(display, new RGB(1, 2, 3), 0);
	otherColor = new Color(display, 5, 6, 7, 0);
	// Test Color.equals(Object)
	assertFalse(color.equals((Object)null));

	// Test Color.equals(Color)
	assertFalse(color.equals((Color)null));
	assertTrue(color.equals(color));
	assertTrue(color.equals(sameColor));
	assertTrue(color.equals(sameColor2));
	assertFalse(color.equals(otherColor));
}

@Test
public void test_equalsLjava_lang_Object_mix() {
	Color color = new Color(display, 1, 2, 3);
	Color sameColorNoDevice = new Color(1, 2, 3);
	Color otherColorNoDevice = new Color(5, 6, 7);
	// Test Color.equals(Color)
	assertTrue(color.equals(sameColorNoDevice));
	assertFalse(color.equals(otherColorNoDevice));
	assertTrue(sameColorNoDevice.equals(color));
	assertFalse(otherColorNoDevice.equals(color));

	// With alpha
	color = new Color(display, 1, 2, 3, 0);
	sameColorNoDevice = new Color(1, 2, 3, 0);
	otherColorNoDevice = new Color(5, 6, 7, 0);
	// Test Color.equals(Color)
	assertTrue(color.equals(sameColorNoDevice));
	assertFalse(color.equals(otherColorNoDevice));
	assertTrue(sameColorNoDevice.equals(color));
	assertFalse(otherColorNoDevice.equals(color));
}

@Test
public void test_getBlue() {
	// Test Color.getBlue()
	Color color = new Color(0, 0, 255);
	assertEquals(color.getBlue(), 255);
}

@Test
public void test_getBlue_with_device() {
	// Test Color.getBlue()
	Color color = new Color(display, 0, 0, 255);
	assertEquals(color.getBlue(), 255);
}

@Test
public void test_getGreen() {
	// Test Color.getGreen()
	Color color = new Color(0, 255, 0);
	assertEquals(color.getGreen(), 255);
}

@Test
public void test_getGreen_with_device() {
	// Test Color.getGreen()
	Color color = new Color(display, 0, 255, 0);
	assertEquals(color.getGreen(), 255);
}

@Test
public void test_getRGB() {
	Color color = new Color(255, 255, 255);
	assertNotNull(color.getRGB());
	assertEquals(new RGB(255, 255, 255), color.getRGB());
}

@Test
public void test_getRGB_with_device() {
	Color color = new Color(display, 255, 255, 255);
	assertNotNull(color.getRGB());
	assertEquals(new RGB(255, 255, 255), color.getRGB());
}

@Test
public void test_getRed() {
	// Test Color.getRed()
	Color color = new Color(255, 0, 0);
	assertEquals(color.getRed(), 255);
}

@Test
public void test_getRed_with_device() {
	// Test Color.getRed()
	Color color = new Color(display, 255, 0, 0);
	assertEquals(color.getRed(), 255);
}

@Test
public void test_getAlpha() {
	// Test Color.getRed()
	Color color = new Color(255, 0, 0, 0);
	assertEquals(color.getAlpha(), 0);
}

@Test
public void test_getAlpha_with_device() {
	// Test Color.getRed()
	Color color = new Color(display, 255, 0, 0, 0);
	assertEquals(color.getAlpha(), 0);
}

@Test
public void test_hashCode() {
	Color color = new Color(12, 34, 56, 0);
	Color otherColor = new Color(12, 34, 56, 0);
	if (color.equals(otherColor)) {
		assertEquals(color.hashCode(), otherColor.hashCode());
	}
}

@Test
public void test_hashCode_with_device() {
	Color color = new Color(display, 12, 34, 56, 0);
	Color otherColor = new Color(display, 12, 34, 56, 0);
	if (color.equals(otherColor)) {
		assertEquals(color.hashCode(), otherColor.hashCode());
	}
}

/**
 * While Colors do not require disposal, if they are disposed we need to
 * follow existing disposal semantics.
 */
@Test
public void test_isDisposed() {
	// Test Color.isDisposed() false
	Color color = new Color(34, 67, 98, 0);
	assertFalse(color.isDisposed());
	// Test Color.isDisposed() true
	color.dispose();
	assertTrue(color.isDisposed());
}

@Test
public void test_isDisposed_with_device() {
	// Test Color.isDisposed() false
	Color color = new Color(display, 34, 67, 98, 0);
	assertFalse(color.isDisposed());
	// Test Color.isDisposed() true
	color.dispose();
	assertTrue(color.isDisposed());
}

@Test
public void test_toString() {
	Color color = new Color(0, 0, 255, 255);
	assertNotNull(color.toString());
	assertFalse(color.toString().isEmpty());
	assertEquals("Color {0, 0, 255, 255}", color.toString());
}

@Test
public void test_toString_with_device() {
	Color color = new Color(display, 0, 0, 255, 255);
	assertNotNull(color.toString());
	assertFalse(color.toString().isEmpty());
	assertEquals("Color {0, 0, 255, 255}", color.toString());
}

@Test
public void test_getDevice() {
	Color color = new Color(0, 0, 255, 255);
	Device device = color.getDevice();
	assertEquals(display, device);


	// see test_isDisposed - we need to keep dispose semantics
	SWTException e = assertThrows(SWTException.class, () -> {
		Color color1 = new Color(0, 0, 255, 255);
		color1.dispose();
		color1.getDevice();
	});
	assertEquals(SWT.ERROR_GRAPHIC_DISPOSED, e.code);
}

@Test
public void test_getDevice_with_device() {
	Color color = new Color(display, 0, 0, 255, 255);
	assertEquals(display, color.getDevice());

	SWTException e = assertThrows(SWTException.class, () -> {
		Color color1 = new Color(0, 0, 255, 255);
		color1.dispose();
		color1.getDevice();
	});
	assertEquals(SWT.ERROR_GRAPHIC_DISPOSED, e.code);
}

/* custom */
Display display;
}
