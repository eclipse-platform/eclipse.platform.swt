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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.FontData
 *
 * @see org.eclipse.swt.graphics.FontData
 */
public class Test_org_eclipse_swt_graphics_FontData {

@Test
public void test_Constructor() {
	// Test new FontData()
	new FontData();
}

@Test
public void test_ConstructorLjava_lang_String() {
	// Test new FontData(String string)
	FontData fd = new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL);
	FontData reconstructedFontData = new FontData(fd.toString());
	assertEquals(fd, reconstructedFontData);
	assertEquals(fd.hashCode(), reconstructedFontData.hashCode());
}

@Test
public void test_ConstructorLjava_lang_StringII() {
	// Test new FontData(String name, int height, int style)
	// valid font data with no name (strange, but apparently valid)
	new FontData("", 10, SWT.NORMAL);

	// valid font data with unknown name (strange, but apparently valid)
	new FontData("bad-font", 10, SWT.NORMAL);

	// valid font data with 0 height (strange, but apparently valid)
	new FontData(SwtTestUtil.testFontName, 0, SWT.NORMAL);

	// valid font data with 1000 height (pretty big, but apparently valid)


	// valid normal 10-point font data
	new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL);

	// valid bold 10-point font data
	new FontData(SwtTestUtil.testFontName, 10, SWT.BOLD);

	// valid italic 10-point font data
	new FontData(SwtTestUtil.testFontName, 10, SWT.ITALIC);

	// valid bold italic 10-point font data
	new FontData(SwtTestUtil.testFontName, 10, SWT.BOLD | SWT.ITALIC);

	// illegal argument, name == null
	assertThrows(IllegalArgumentException.class, () -> new FontData(null, 10, SWT.NORMAL),
			"No exception thrown for name == null");

	// illegal argument, height < 0
	assertThrows(IllegalArgumentException.class,
			() -> new FontData(SwtTestUtil.testFontName, -10, SWT.NORMAL),"No exception thrown for height < 0");
}

@Test
public void test_ConstructorLjava_lang_FontData() {
	// Test new FontData(FontData fontData)
	FontData fd = new FontData(SwtTestUtil.testFontName, 30, SWT.ITALIC);
	FontData reconstructedFontDataFromCopyConstructor = new FontData(fd);
	assertEquals(fd, reconstructedFontDataFromCopyConstructor);
	assertEquals(fd.hashCode(), reconstructedFontDataFromCopyConstructor.hashCode());
}

@Test
public void test_equalsLjava_lang_Object() {
	FontData fd1 = new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL);
	FontData fd2 = new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL);
	assertEquals(fd1,fd1);
	assertEquals(fd1,fd2);
}

@Test
public void test_hashCode() {
	FontData fd1 = new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL);
	FontData fd2 = new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL);
	assertEquals(fd1,fd2);
	assertEquals(fd1.hashCode(),fd2.hashCode());
	FontData fd3 = new FontData(SwtTestUtil.testFontName, 10, SWT.BOLD);
	assertNotEquals(fd3.hashCode(), fd1.hashCode());
}

@Test
public void test_setHeightI() {
	// Test Font.setHeight(int  height)
	// valid normal font data for various heights
	FontData fontData = new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL);
	for (int height = 0; height < 1000; height++) {
		fontData.setHeight(height);
		assertEquals(fontData.getHeight(), height, "Wrong height");
	}

	// valid bold font data for various heights
	fontData = new FontData(SwtTestUtil.testFontName, 10, SWT.BOLD);
	for (int height = 0; height < 1000; height++) {
		fontData.setHeight(height);
		assertEquals(fontData.getHeight(), height, "Wrong height");
	}

	// valid italic font data for various heights
	fontData = new FontData(SwtTestUtil.testFontName, 10, SWT.ITALIC);
	for (int height = 0; height < 1000; height++) {
		fontData.setHeight(height);
		assertEquals(fontData.getHeight(), height, "Wrong height");
	}

	// valid bold italic font data for various heights
	fontData = new FontData(SwtTestUtil.testFontName, 10, SWT.BOLD | SWT.ITALIC);
	for (int height = 0; height < 1000; height++) {
		fontData.setHeight(height);
		assertEquals(fontData.getHeight(), height, "Wrong height");
	}
}

@Test
public void test_setLocaleLjava_lang_String() {
	FontData fd = new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL);
	Locale locale = Locale.ENGLISH;
	fd.setLocale(locale.toString());
	assertEquals(Locale.ENGLISH.toString(),fd.getLocale());
}

@Test
public void test_setNameLjava_lang_String() {
	// Test Font.setName(String name)
	// valid name
	FontData fontData = new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL);
	assertEquals(fontData.getName(), SwtTestUtil.testFontName);

	// valid name (unknown name, but valid)
	fontData.setName("bad-font");
	assertEquals(fontData.getName(), "bad-font");

	// valid name (empty string, but valid)
	// only on windows
	if (SwtTestUtil.isWindows) {
		fontData.setName("");
		assertEquals(fontData.getName(), "");
	}

	// valid name
	fontData.setName(SwtTestUtil.testFontName);
	assertEquals(fontData.getName(), SwtTestUtil.testFontName);
	// illegal argument, name == null
	assertThrows(IllegalArgumentException.class, ()->
		fontData.setName(null),"No exception thrown for name == null");
}

@Test
public void test_setStyleI() {
	// Test Font.setStyle(int  style)
	for (int height = 0; height < 1000; height++) {
		// valid normal font data
		FontData fontData = new FontData(SwtTestUtil.testFontName, height, SWT.NORMAL);
		assertEquals(fontData.getStyle(), SWT.NORMAL, "Wrong style");

		// valid bold font data
		fontData.setStyle(SWT.BOLD);
		assertEquals(fontData.getStyle(), SWT.BOLD, "Wrong style");

		// valid italic font data
		fontData.setStyle(SWT.ITALIC);
		assertEquals(fontData.getStyle(), SWT.ITALIC, "Wrong style");

		// valid bold italic font data
		fontData.setStyle(SWT.ITALIC | SWT.BOLD);
		assertEquals(fontData.getStyle(), SWT.BOLD | SWT.ITALIC, "Wrong style");

		// valid normal font data
		fontData.setStyle(SWT.NORMAL);
		assertEquals(fontData.getStyle(), SWT.NORMAL, "Wrong style");
	}
}

@Test
public void test_toString() {
	FontData data = new FontData();
	assertNotNull(data.toString());
	assertTrue(data.toString().length() > 0);
}
}
