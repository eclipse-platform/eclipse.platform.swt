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


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.Font
 *
 * @see org.eclipse.swt.graphics.Font
 */
public class Test_org_eclipse_swt_graphics_Font extends SwtTestCase {

Display display;

public Test_org_eclipse_swt_graphics_Font(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	display = Display.getDefault();
	super.setUp();
}

public void test_ConstructorLorg_eclipse_swt_graphics_Device$Lorg_eclipse_swt_graphics_FontData() {
	
	// null  device argument
	
	boolean exceptionThrown = false;
	try {
		FontData[] data = new FontData[1];
		Font font = new Font(null,data);
		font.dispose();
	} catch (IllegalArgumentException e) {
		exceptionThrown = true;
	} 
	assertTrue(exceptionThrown);
	
	// null data argument
	
	exceptionThrown = false;
	try {
		FontData[] data = null;
		Font font = new Font(display,data);
		font.dispose();
	} catch (IllegalArgumentException e) {
		exceptionThrown = true;
	} 
	assertTrue(exceptionThrown);

	// zero length data array
	
	exceptionThrown = false;
	try {
		FontData[] data = new FontData[0];
		Font font = new Font(display,data);
		font.dispose();
	} catch (IllegalArgumentException e) {
		exceptionThrown = true;
	} 
	assertTrue(exceptionThrown);
	
	// null data element
	
	exceptionThrown = false;
	try {
		FontData[] data = {
			null,
			new FontData()
		};
		Font font = new Font(display,data);
		font.dispose();
	} catch (IllegalArgumentException e) {
		exceptionThrown = true;
	} 
	assertTrue("null data element 0",exceptionThrown);

	exceptionThrown = false;
	try {
		FontData[] data = {
			new FontData(),
			null
		};
		Font font = new Font(display,data);
		font.dispose();
	} catch (IllegalArgumentException e) {
		exceptionThrown = true;
	} 
	assertTrue("null data element 1",exceptionThrown);	
}

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
	font = new Font(display, new FontData(SwtJunit.testFontName, 0, SWT.NORMAL));
	font.dispose();

	// valid normal 100-point font
	font = new Font(display, new FontData(SwtJunit.testFontName, 100, SWT.NORMAL));
	font.dispose();

	// valid normal 10-point font
	font = new Font(display, new FontData(SwtJunit.testFontName, 10, SWT.NORMAL));
	font.dispose();

	// valid bold 10-point font
	font = new Font(display, new FontData(SwtJunit.testFontName, 10, SWT.BOLD));
	font.dispose();

	// valid italic 10-point font
	font = new Font(display, new FontData(SwtJunit.testFontName, 10, SWT.ITALIC));
	font.dispose();

	// valid bold italic 10-point font
	font = new Font(display, new FontData(SwtJunit.testFontName, 10, SWT.BOLD | SWT.ITALIC));
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
		font = new Font(display, new FontData(SwtJunit.testFontName, -10, SWT.NORMAL));
		font.dispose();
		fail("No exception thrown for height < 0");
	} catch (IllegalArgumentException e) {
	}
}

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
	font = new Font(display, SwtJunit.testFontName, 0, SWT.NORMAL);
	font.dispose();

	// valid normal 100-point font
	font = new Font(display, SwtJunit.testFontName, 100, SWT.NORMAL);
	font.dispose();

	// valid normal 10-point font
	font = new Font(display, SwtJunit.testFontName, 10, SWT.NORMAL);
	font.dispose();

	// valid bold 10-point font
	font = new Font(display, SwtJunit.testFontName, 10, SWT.BOLD);
	font.dispose();

	// valid italic 10-point font
	font = new Font(display, SwtJunit.testFontName, 10, SWT.ITALIC);
	font.dispose();

	// valid bold italic 10-point font
	font = new Font(display, SwtJunit.testFontName, 10, SWT.BOLD | SWT.ITALIC);
	font.dispose();

	// device == null (valid)
	font = new Font(null, SwtJunit.testFontName, 10, SWT.NORMAL);
	font.dispose();

	// illegal argument, name == null
	try {
		font = new Font(display, null, 10, SWT.NORMAL);
		font.dispose();
		fail("No exception thrown for name == null");
	} catch (IllegalArgumentException e) {
	}

	// illegal argument, height < 0
	try {
		font = new Font(display, SwtJunit.testFontName, -10, SWT.NORMAL);
		font.dispose();
		fail("No exception thrown for height < 0");
	} catch (IllegalArgumentException e) {
	}
}

public void test_dispose() {
	Font font = new Font(display, SwtJunit.testFontName, 10, SWT.NORMAL);
	assertFalse(font.isDisposed());
	font.dispose();
	assertTrue(font.isDisposed());
}

public void test_equalsLjava_lang_Object() {
	// Fonts are only equal if their handles are the same (?!)
	Font font = new Font(display, SwtJunit.testFontName, 10, SWT.NORMAL);
	Font otherFont = new Font(display, SwtJunit.testFontName, 20, SWT.NORMAL);
	try {
		// Test Font.equals(Object)
		assertTrue("!font.equals((Object)null)", !font.equals((Object)null));

		// Test Font.equals(Font)
		assertTrue("!font.equals((Font)null)", !font.equals((Font)null));
		assertTrue("font.equals(font)", font.equals(font));
		assertTrue("!font.equals(otherFont)", !font.equals(otherFont));
	} finally {
		font.dispose();
		otherFont.dispose();
	}
}

public void test_getFontData() {
	// Test Font.getFontData()
	// valid normal 10-point font
	Font font = new Font(display, SwtJunit.testFontName, 10, SWT.NORMAL);
	try {
		FontData fontData[] = font.getFontData();
		assertTrue("No font data", fontData != null && fontData.length > 0);
		assertEquals("Wrong font name", SwtJunit.testFontName, fontData[0].getName());
		assertEquals("Wrong font height", 10, fontData[0].getHeight());
		assertEquals("Wrong font style", SWT.NORMAL, fontData[0].getStyle());
	} finally {
		font.dispose();
	}

	// valid bold 20-point font
	font = new Font(display, SwtJunit.testFontName, 20, SWT.BOLD);
	try {
		FontData fontData[] = font.getFontData();
		assertTrue("No font data", fontData != null && fontData.length > 0);
		assertEquals("Wrong font name", SwtJunit.testFontName, fontData[0].getName());
		assertEquals("Wrong font height", 20, fontData[0].getHeight());
		assertEquals("Wrong font style", SWT.BOLD, fontData[0].getStyle());
	} finally {
		font.dispose();
	}

	// valid italic 30-point font
	font = new Font(display, SwtJunit.testFontName, 30, SWT.ITALIC);
	try {
		FontData fontData[] = font.getFontData();
		assertTrue("No font data", fontData != null && fontData.length > 0);
//		assertEquals("Wrong font name", SwtJunit.testFontName, fontData[0].getName());
		assertEquals("Wrong font height", 30, fontData[0].getHeight());
		assertEquals("Wrong font style", SWT.ITALIC, fontData[0].getStyle());
	} finally {
		font.dispose();
	}

	// valid bold italic 40-point font
	font = new Font(display, SwtJunit.testFontName, 40, SWT.BOLD | SWT.ITALIC);
	try {
		FontData fontData[] = font.getFontData();
		font.dispose();
		assertTrue("No font data", fontData != null && fontData.length > 0);
//		assertEquals("Wrong font name", SwtJunit.testFontName, fontData[0].getName());
		assertEquals("Wrong font height", 40, fontData[0].getHeight());
		assertEquals("Wrong font style", SWT.BOLD | SWT.ITALIC, fontData[0].getStyle());
	} finally {
		font.dispose();
	}

	// valid 10-point font with unknown name
	font = new Font(display, "bad-font", 10, SWT.NORMAL);
	try {
		FontData fontData[] = font.getFontData();
		assertTrue("No font data", fontData != null && fontData.length > 0);
//		assertEquals("Wrong font name", "bad-font", fontData[0].getName());
		assertEquals("Wrong font height", 10, fontData[0].getHeight());
		assertEquals("Wrong font style", SWT.NORMAL, fontData[0].getStyle());
	} finally {
		font.dispose();
	}
}

public void test_hashCode() {
	Font font = new Font(display, SwtJunit.testFontName, 10, SWT.NORMAL);
	assertEquals(font,font);
	assertEquals(font.hashCode(),font.hashCode());
	Font boldFont = new Font(display, SwtJunit.testFontName, 10, SWT.BOLD);
	assertFalse(font.hashCode() == boldFont.hashCode());
	boldFont.dispose();
	font.dispose();
}

public void test_isDisposed() {
	// Test Font.isDisposed() false
	Font font = new Font(display, SwtJunit.testFontName, 10, SWT.NORMAL);
	try {
		assertTrue("Font should not be disposed", !font.isDisposed());
	} finally {
		// Test Font.isDisposed() true
		font.dispose();
		assertTrue("Font should be disposed", font.isDisposed());
	}
}

public void test_toString() {
	Font font = new Font(display, SwtJunit.testFontName, 10, SWT.NORMAL);
	assertNotNull(font.toString());
	
	font.dispose();
	assertNotNull(font.toString());
}

public void test_win32_newLorg_eclipse_swt_graphics_DeviceI() {
	// do not test - Windows only
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_Font((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_Device$Lorg_eclipse_swt_graphics_FontData");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_FontData");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_StringII");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_getFontData");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_isDisposed");
	methodNames.addElement("test_toString");
	methodNames.addElement("test_win32_newLorg_eclipse_swt_graphics_DeviceI");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_Device$Lorg_eclipse_swt_graphics_FontData")) test_ConstructorLorg_eclipse_swt_graphics_Device$Lorg_eclipse_swt_graphics_FontData();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_FontData")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_FontData();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_StringII")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_StringII();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_getFontData")) test_getFontData();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_isDisposed")) test_isDisposed();
	else if (getName().equals("test_toString")) test_toString();
	else if (getName().equals("test_win32_newLorg_eclipse_swt_graphics_DeviceI")) test_win32_newLorg_eclipse_swt_graphics_DeviceI();
}
}
