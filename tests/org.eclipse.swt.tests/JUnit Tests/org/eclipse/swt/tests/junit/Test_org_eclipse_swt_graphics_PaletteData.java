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


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.PaletteData
 *
 * @see org.eclipse.swt.graphics.PaletteData
 */
public class Test_org_eclipse_swt_graphics_PaletteData {

@Test
public void test_Constructor$Lorg_eclipse_swt_graphics_RGB() {
	try {
		new PaletteData((RGB [])null);
		fail("No exception thrown for rgb == null");
	}
	catch (IllegalArgumentException e) {
	}

	PaletteData data = new PaletteData(new RGB[] {});
	assertFalse(":a:", data.isDirect);

	new PaletteData(new RGB[] {null, null});
	assertFalse(":b:", data.isDirect);

	new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255));
	assertFalse(":c:", data.isDirect);
}

@Test
public void test_ConstructorIII() {
	PaletteData data = new PaletteData(0, 0, 0);
	assertTrue(":a:", data.isDirect);

	data = new PaletteData(-1, -1, -1);
	assertTrue(":b:", data.isDirect);

	data = new PaletteData(0xff0000, 0x00ff00, 0x0000ff);
	assertTrue(":c:", data.isDirect);
}

@Test
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

@Test
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

@Test
public void test_getRGBs() {
	// indexed palette tests
	RGB[] rgbs = {new RGB(0, 0, 0), new RGB(255, 255, 255)};
	PaletteData data = new PaletteData(rgbs);

	assertArrayEquals(":a:", rgbs, data.getRGBs());

	// direct palette tests
	data = new PaletteData(0xff0000, 0x00ff00, 0x0000ff);

	assertNull(":b:", data.getRGBs());
}

}
