/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.Font
 *
 * @see org.eclipse.swt.graphics.Font
 */
public class Test_org_eclipse_swt_graphics_Font {

Display display;

@BeforeEach
public void setUp() {
	display = Display.getDefault();
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_Device$Lorg_eclipse_swt_graphics_FontData() {

	// null  device argument

	assertThrows(IllegalArgumentException.class, ()-> new Font(null,new FontData[1]));

	// null data argument

	assertThrows(IllegalArgumentException.class, ()-> new Font(display,(FontData[])null));

	// zero length data array

	assertThrows(IllegalArgumentException.class, ()-> new Font(display,new FontData[0]));

	// null data element

	assertThrows(IllegalArgumentException.class, () -> new Font(display, new FontData[] { null, new FontData() }),
			"null data element 0");

	assertThrows(IllegalArgumentException.class, () -> new Font(display, new FontData[] { new FontData(), null }),
			"null data element 0");
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_FontData() {
	// Test new Font(Device device, FontData fd)
	// IllegalArgumentException if the fd argument is null
	// SWTError: ERROR_NO_HANDLES, if a font could not be created from the given font data

	// valid font with no name (strange, but apparently valid)
	Font font = new Font(display, new FontData("", 10, SWT.NORMAL));
	font.dispose();

	// valid font with unknown name (strange, but apparently valid)
	font = new Font(display, new FontData("bad-font", 10, SWT.NORMAL));
	font.dispose();

	// valid font with 0 height (strange, but apparently valid)
	font = new Font(display, new FontData(SwtTestUtil.testFontName, 0, SWT.NORMAL));
	font.dispose();

	// valid normal 100-point font
	font = new Font(display, new FontData(SwtTestUtil.testFontName, 100, SWT.NORMAL));
	font.dispose();

	// valid normal 10-point font
	font = new Font(display, new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL));
	font.dispose();

	// valid bold 10-point font
	font = new Font(display, new FontData(SwtTestUtil.testFontName, 10, SWT.BOLD));
	font.dispose();

	// valid italic 10-point font
	font = new Font(display, new FontData(SwtTestUtil.testFontName, 10, SWT.ITALIC));
	font.dispose();

	// valid bold italic 10-point font
	font = new Font(display, new FontData(SwtTestUtil.testFontName, 10, SWT.BOLD | SWT.ITALIC));
	font.dispose();

	// illegal argument, fontData == null
	try {
		font = new Font(display, (FontData)null);
		font.dispose();
		fail("No exception thrown for fontData == null");
	} catch (IllegalArgumentException e) {
	}

	// illegal argument, name == null
	try {
		font = new Font(display, new FontData(null, 10, SWT.NORMAL));
		font.dispose();
		fail("No exception thrown for name == null");
	} catch (IllegalArgumentException e) {
	}

	// illegal argument, height < 0
	try {
		font = new Font(display, new FontData(SwtTestUtil.testFontName, -10, SWT.NORMAL));
		font.dispose();
		fail("No exception thrown for height < 0");
	} catch (IllegalArgumentException e) {
	}
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_StringII() {
	// Test new Font(Device device, String name, int height, int style)
	// IllegalArgumentException if the name argument is null or the height is negative
	// SWTError: ERROR_NO_HANDLES, if a font could not be created from the given arguments

	// valid font with no name (strange, but apparently valid)
	Font font = new Font(display, "", 10, SWT.NORMAL);
	font.dispose();

	// valid font with unknown name (strange, but apparently valid)
	font = new Font(display, "bad-font", 10, SWT.NORMAL);
	font.dispose();

	// valid font with 0 height (strange, but apparently valid)
	font = new Font(display, SwtTestUtil.testFontName, 0, SWT.NORMAL);
	font.dispose();

	// valid normal 100-point font
	font = new Font(display, SwtTestUtil.testFontName, 100, SWT.NORMAL);
	font.dispose();

	// valid normal 10-point font
	font = new Font(display, SwtTestUtil.testFontName, 10, SWT.NORMAL);
	font.dispose();

	// valid bold 10-point font
	font = new Font(display, SwtTestUtil.testFontName, 10, SWT.BOLD);
	font.dispose();

	// valid italic 10-point font
	font = new Font(display, SwtTestUtil.testFontName, 10, SWT.ITALIC);
	font.dispose();

	// valid bold italic 10-point font
	font = new Font(display, SwtTestUtil.testFontName, 10, SWT.BOLD | SWT.ITALIC);
	font.dispose();

	// device == null (valid)
	font = new Font(null, SwtTestUtil.testFontName, 10, SWT.NORMAL);
	font.dispose();

	// illegal argument, name == null
	assertThrows(IllegalArgumentException.class, () -> new Font(display, null, 10, SWT.NORMAL),
			"No exception thrown for name == null");

	// illegal argument, height < 0
	assertThrows(IllegalArgumentException.class, () -> new Font(display, SwtTestUtil.testFontName, -10, SWT.NORMAL),
			"No exception thrown for height < 0");
}

@Test
public void test_dispose() {
	Font font = new Font(display, SwtTestUtil.testFontName, 10, SWT.NORMAL);
	assertFalse(font.isDisposed());
	font.dispose();
	assertTrue(font.isDisposed());
}

@Test
public void test_equalsLjava_lang_Object() {
	// Fonts are only equal if their handles are the same (?!)
	Font font = new Font(display, SwtTestUtil.testFontName, 10, SWT.NORMAL);
	Font otherFont = new Font(display, SwtTestUtil.testFontName, 20, SWT.NORMAL);
	try {
		// Test Font.equals(Object)
		assertFalse(font.equals((Object)null), "!font.equals((Object)null)");

		// Test Font.equals(Font)
		assertFalse(font.equals((Font)null), "!font.equals((Font)null)");
		assertTrue(font.equals(font), "font.equals(font)");
		assertFalse(font.equals(otherFont), "!font.equals(otherFont)");
	} finally {
		font.dispose();
		otherFont.dispose();
	}
}

@Test
public void test_getFontData() {
	// Test Font.getFontData()
	// valid normal 10-point font
	Font font = new Font(display, SwtTestUtil.testFontName, 10, SWT.NORMAL);
	try {
		FontData fontData[] = font.getFontData();
		assertTrue(fontData != null && fontData.length > 0, "No font data");
		assertEquals(SwtTestUtil.testFontName, fontData[0].getName(), "Wrong font name");
		assertEquals(10, fontData[0].getHeight(), "Wrong font height");
		assertEquals(SWT.NORMAL, fontData[0].getStyle(), "Wrong font style");
	} finally {
		font.dispose();
	}

	// valid bold 20-point font
	font = new Font(display, SwtTestUtil.testFontName, 20, SWT.BOLD);
	try {
		FontData fontData[] = font.getFontData();
		assertTrue(fontData != null && fontData.length > 0, "No font data");
		assertEquals(SwtTestUtil.testFontName, fontData[0].getName(), "Wrong font name");
		assertEquals(20, fontData[0].getHeight(), "Wrong font height");
		assertEquals(SWT.BOLD, fontData[0].getStyle(), "Wrong font style");
	} finally {
		font.dispose();
	}

	// valid italic 30-point font
	font = new Font(display, SwtTestUtil.testFontName, 30, SWT.ITALIC);
	try {
		FontData fontData[] = font.getFontData();
		assertTrue(fontData != null && fontData.length > 0, "No font data");
		assertEquals(30, fontData[0].getHeight(), "Wrong font height");
		assertEquals(SWT.ITALIC, fontData[0].getStyle(), "Wrong font style");
	} finally {
		font.dispose();
	}

	// valid bold italic 40-point font
	font = new Font(display, SwtTestUtil.testFontName, 40, SWT.BOLD | SWT.ITALIC);
	try {
		FontData fontData[] = font.getFontData();
		font.dispose();
		assertTrue(fontData != null && fontData.length > 0, "No font data");
		assertEquals(40, fontData[0].getHeight(), "Wrong font height");
		assertEquals(SWT.BOLD | SWT.ITALIC, fontData[0].getStyle(), "Wrong font style");
	} finally {
		font.dispose();
	}

	// valid 10-point font with unknown name
	font = new Font(display, "bad-font", 10, SWT.NORMAL);
	try {
		FontData fontData[] = font.getFontData();
		assertTrue(fontData != null && fontData.length > 0, "No font data");
		assertEquals(10, fontData[0].getHeight(), "No font data");
		assertEquals(SWT.NORMAL, fontData[0].getStyle(), "Wrong font style");
	} finally {
		font.dispose();
	}
}

@Test
public void test_hashCode() {
	Font font = new Font(display, SwtTestUtil.testFontName, 10, SWT.NORMAL);
	assertEquals(font,font);
	assertEquals(font.hashCode(),font.hashCode());
	Font boldFont = new Font(display, SwtTestUtil.testFontName, 10, SWT.BOLD);
	assertNotEquals(boldFont.hashCode(), font.hashCode());
	boldFont.dispose();
	font.dispose();
}

@Test
public void test_isDisposed() {
	// Test Font.isDisposed() false
	Font font = new Font(display, SwtTestUtil.testFontName, 10, SWT.NORMAL);
	try {
		assertFalse(font.isDisposed(), "Font should not be disposed");
	} finally {
		// Test Font.isDisposed() true
		font.dispose();
		assertTrue(font.isDisposed(), "Font should be disposed");
	}
}

@Test
public void test_toString() {
	Font font = new Font(display, SwtTestUtil.testFontName, 10, SWT.NORMAL);
	assertNotNull(font.toString());

	font.dispose();
	assertNotNull(font.toString());
}

}
