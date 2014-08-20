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

import java.util.Locale;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.FontData
 *
 * @see org.eclipse.swt.graphics.FontData
 */
public class Test_org_eclipse_swt_graphics_FontData extends TestCase {

public void test_Constructor() {
	// Test new FontData()
	new FontData();
}

public void test_ConstructorLjava_lang_String() {
	// Test new FontData(String string)
	FontData fd = new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL);
	FontData reconstructedFontData = new FontData(fd.toString());
	assertEquals(fd, reconstructedFontData);
}

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
	try {
		new FontData(null, 10, SWT.NORMAL);
		fail("No exception thrown for name == null");
	} catch (IllegalArgumentException e) {
	}

	// illegal argument, height < 0
	try {
		new FontData(SwtTestUtil.testFontName, -10, SWT.NORMAL);
		fail("No exception thrown for height < 0");
	} catch (IllegalArgumentException e) {
	}
}

public void test_equalsLjava_lang_Object() {
	FontData fd1 = new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL);
	FontData fd2 = new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL);
	assertEquals(fd1,fd1);
	assertEquals(fd1,fd2);
}

public void test_hashCode() {
	FontData fd1 = new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL);
	FontData fd2 = new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL);
	assertEquals(fd1,fd2);
	assertEquals(fd1.hashCode(),fd2.hashCode());
	FontData fd3 = new FontData(SwtTestUtil.testFontName, 10, SWT.BOLD);
	assertFalse(fd1.hashCode() == fd3.hashCode());
}

public void test_setHeightI() {
	// Test Font.setHeight(int  height)
	// valid normal font data for various heights
	FontData fontData = new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL);	
	for (int height = 0; height < 1000; height++) {
		fontData.setHeight(height);
		assertEquals("Wrong height", fontData.getHeight(), height);
	}

	// valid bold font data for various heights
	fontData = new FontData(SwtTestUtil.testFontName, 10, SWT.BOLD);	
	for (int height = 0; height < 1000; height++) {
		fontData.setHeight(height);
		assertEquals("Wrong height", fontData.getHeight(), height);
	}

	// valid italic font data for various heights
	fontData = new FontData(SwtTestUtil.testFontName, 10, SWT.ITALIC);	
	for (int height = 0; height < 1000; height++) {
		fontData.setHeight(height);
		assertEquals("Wrong height", fontData.getHeight(), height);
	}

	// valid bold italic font data for various heights
	fontData = new FontData(SwtTestUtil.testFontName, 10, SWT.BOLD | SWT.ITALIC);	
	for (int height = 0; height < 1000; height++) {
		fontData.setHeight(height);
		assertEquals("Wrong height", fontData.getHeight(), height);
	}
}

public void test_setLocaleLjava_lang_String() {
	FontData fd = new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL);
	Locale locale = Locale.ENGLISH;
	fd.setLocale(locale.toString());
	assertEquals(Locale.ENGLISH.toString(),fd.getLocale());
}

public void test_setNameLjava_lang_String() {
	// Test Font.setName(String name)
	// valid name
	FontData fontData = new FontData(SwtTestUtil.testFontName, 10, SWT.NORMAL);
	assertEquals("Wrong name", fontData.getName(), SwtTestUtil.testFontName);

	// valid name (unknown name, but valid)
	fontData.setName("bad-font");
	assertEquals("Wrong name", fontData.getName(), "bad-font");

	// valid name (empty string, but valid)
	// only on windows
	if (SwtTestUtil.isWindows) {
		fontData.setName("");
		assertEquals("Wrong name", fontData.getName(), "");
	}

	// valid name
	fontData.setName(SwtTestUtil.testFontName);	
	assertEquals("Wrong name", fontData.getName(), SwtTestUtil.testFontName);
	// illegal argument, name == null
	try {
		fontData.setName(null);
		fail("No exception thrown for name == null");
	} catch (IllegalArgumentException e) {
	}
}

public void test_setStyleI() {
	// Test Font.setStyle(int  style)
	for (int height = 0; height < 1000; height++) {
		// valid normal font data
		FontData fontData = new FontData(SwtTestUtil.testFontName, height, SWT.NORMAL);	
		assertEquals("Wrong style", fontData.getStyle(), SWT.NORMAL);

		// valid bold font data
		fontData.setStyle(SWT.BOLD);
		assertEquals("Wrong style", fontData.getStyle(), SWT.BOLD);

		// valid italic font data
		fontData.setStyle(SWT.ITALIC);
		assertEquals("Wrong style", fontData.getStyle(), SWT.ITALIC);
		
		// valid bold italic font data
		fontData.setStyle(SWT.ITALIC | SWT.BOLD);
		assertEquals("Wrong style", fontData.getStyle(), SWT.BOLD | SWT.ITALIC);

		// valid normal font data
		fontData.setStyle(SWT.NORMAL);
		assertEquals("Wrong style", fontData.getStyle(), SWT.NORMAL);
	}
}

public void test_toString() {
	FontData data = new FontData();
	assertNotNull(data.toString());
	assertTrue(data.toString().length() > 0);
}
}
