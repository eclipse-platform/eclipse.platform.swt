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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.PaletteData
 *
 * @see org.eclipse.swt.graphics.PaletteData
 */
public class Test_org_eclipse_swt_graphics_PaletteData {

@Test
public void test_Constructor$Lorg_eclipse_swt_graphics_RGB() {
	assertThrows(IllegalArgumentException.class, ()->
		new PaletteData((RGB [])null),"No exception thrown for rgb == null");

	PaletteData data = new PaletteData(new RGB[] {});
	assertFalse(data.isDirect);

	new PaletteData(new RGB[] {null, null});
	assertFalse(data.isDirect);

	new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255));
	assertFalse(data.isDirect);
}

@Test
public void test_ConstructorIII() {
	PaletteData data = new PaletteData(0, 0, 0);
	assertTrue(data.isDirect);

	data = new PaletteData(-1, -1, -1);
	assertTrue(data.isDirect);

	data = new PaletteData(0xff0000, 0x00ff00, 0x0000ff);
	assertTrue(data.isDirect);
}

@Test
public void test_getPixelLorg_eclipse_swt_graphics_RGB() {
	// indexed palette tests
	RGB[] rgbs = { new RGB(0, 0, 0), new RGB(255, 255, 255), new RGB(50, 100, 150) };
	PaletteData data = new PaletteData(rgbs);

	assertThrows(IllegalArgumentException.class, () -> data.getPixel(null),
			"No exception thrown for indexed palette with rgb == null");

	assertThrows(IllegalArgumentException.class, () -> data.getPixel(new RGB(0, 0, 1)),
			"No exception thrown for rgb not found");

	assertEquals(rgbs.length - 1, data.getPixel(rgbs[rgbs.length - 1]));

	// direct palette tests
	RGB rgb = new RGB(0x32, 0x64, 0x96);
	PaletteData data1 = new PaletteData(0xff0000, 0x00ff00, 0x0000ff);

	assertThrows(IllegalArgumentException.class, () -> data.getPixel(null),
			"No exception thrown for direct palette with rgb == null");

	assertEquals(0x326496, data1.getPixel(rgb));
}

@Test
public void test_getRGBI() {
	// indexed palette tests
	RGB[] rgbs = { new RGB(0, 0, 0), new RGB(255, 255, 255), new RGB(50, 100, 150) };
	PaletteData data = new PaletteData(rgbs);

	assertThrows(IllegalArgumentException.class, () -> data.getRGB(rgbs.length),
			"No exception thrown for nonexistent pixel");

	assertEquals(rgbs[rgbs.length - 1], data.getRGB(rgbs.length - 1));

	// direct palette tests
	RGB rgb = new RGB(0x32, 0x64, 0x96);
	PaletteData data1 = new PaletteData(0xff0000, 0x00ff00, 0x0000ff);

	assertEquals(rgb, data1.getRGB(0x326496));
}

@Test
public void test_getRGBs() {
	// indexed palette tests
	RGB[] rgbs = {new RGB(0, 0, 0), new RGB(255, 255, 255)};
	PaletteData data = new PaletteData(rgbs);

	assertArrayEquals(rgbs, data.getRGBs());

	// direct palette tests
	data = new PaletteData(0xff0000, 0x00ff00, 0x0000ff);

	assertNull(data.getRGBs());
}

}
