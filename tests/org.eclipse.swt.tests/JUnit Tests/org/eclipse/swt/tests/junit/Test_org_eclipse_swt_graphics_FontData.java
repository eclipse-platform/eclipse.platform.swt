/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import java.util.*;
import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.FontData
 *
 * @see org.eclipse.swt.graphics.FontData
 */
public class Test_org_eclipse_swt_graphics_FontData extends SwtTestCase {

public Test_org_eclipse_swt_graphics_FontData(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
}

protected void tearDown() {
}

public void test_Constructor() {
	// Test new FontData()
	FontData fd = new FontData();
	testPerformance (new Runnable() {
		public void run() {
			new FontData();
		}
	});
}

public void test_ConstructorLjava_lang_String() {
	// Test new FontData(String string)
	final FontData fd = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	FontData reconstructedFontData = new FontData(fd.toString());
	assertEquals(fd, reconstructedFontData);
	testPerformance(new Runnable() {
		public void run() {
			new FontData(fd.toString());
		}
	});
}

public void test_ConstructorLjava_lang_StringII() {
	// Test new FontData(String name, int height, int style)
	// valid font data with no name (strange, but apparently valid)
	FontData fd = new FontData("", 10, SWT.NORMAL);

	// valid font data with unknown name (strange, but apparently valid)
	fd = new FontData("bad-font", 10, SWT.NORMAL);

	// valid font data with 0 height (strange, but apparently valid)
	fd = new FontData(SwtJunit.testFontName, 0, SWT.NORMAL);

	// valid font data with 1000 height (pretty big, but apparently valid)
	fd = new FontData(SwtJunit.testFontName, 1000, SWT.NORMAL);

	// valid normal 10-point font data
	fd = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);

	// valid bold 10-point font data
	fd = new FontData(SwtJunit.testFontName, 10, SWT.BOLD);

	// valid italic 10-point font data
	fd = new FontData(SwtJunit.testFontName, 10, SWT.ITALIC);

	// valid bold italic 10-point font data
	fd = new FontData(SwtJunit.testFontName, 10, SWT.BOLD | SWT.ITALIC);

	// illegal argument, name == null
	try {
		fd = new FontData(null, 10, SWT.NORMAL);
		fail("No exception thrown for name == null");
	} catch (IllegalArgumentException e) {
	}

	// illegal argument, height < 0
	try {
		fd = new FontData(SwtJunit.testFontName, -10, SWT.NORMAL);
		fail("No exception thrown for height < 0");
	} catch (IllegalArgumentException e) {
	}

	testPerformance(new Runnable() {
		public void run() {
			// valid normal 10-point font data
			new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
		}
	});
}

public void test_equalsLjava_lang_Object() {
	final FontData fd1 = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	final FontData fd2 = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	assertEquals(fd1,fd1);
	assertEquals(fd1,fd2);
	testPerformance("equals same", new Runnable() {
		public void run() {
			assertEquals(fd1,fd1);
		}
	});
	testPerformance("equals different", new Runnable() {
		public void run() {
			assertEquals(fd1,fd2);
		}
	});
}

public void test_getHeight() {
	// Font.getHeight() tested in test_setHeightI
	final FontData data = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	testPerformance(new Runnable() {
		public void run() {
			data.getHeight();
		}
	});
}

public void test_getLocale() {
	// FontData.getLocale() tested in test_setLocaleLjava_lang_String
	final FontData data = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	testPerformance(new Runnable() {
		public void run() {
			data.getLocale();
		}
	});
}

public void test_getName() {
	// Font.getName() tested in test_setNameLjava_lang_String
	final FontData data = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	testPerformance(new Runnable() {
		public void run() {
			data.getName();
		}
	});
}

public void test_getStyle() {
	// Font.getStyle() tested in test_setStyleI
	final FontData data = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	testPerformance(new Runnable() {
		public void run() {
			data.getStyle();
		}
	});
}

public void test_hashCode() {
	final FontData fd1 = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	FontData fd2 = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	assertEquals(fd1,fd2);
	assertEquals(fd1.hashCode(),fd2.hashCode());
	FontData fd3 = new FontData(SwtJunit.testFontName, 10, SWT.BOLD);
	assertFalse(fd1.hashCode() == fd3.hashCode());
	testPerformance(new Runnable() {
		public void run() {
			fd1.hashCode();
		}
	});
}

public void test_setHeightI() {
	// Test Font.setHeight(int  height)
	// valid normal font data for various heights
	FontData fontData = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);	
	for (int height = 0; height < 1000; height++) {
		fontData.setHeight(height);
		assertEquals("Wrong height", fontData.getHeight(), height);
	}

	// valid bold font data for various heights
	fontData = new FontData(SwtJunit.testFontName, 10, SWT.BOLD);	
	for (int height = 0; height < 1000; height++) {
		fontData.setHeight(height);
		assertEquals("Wrong height", fontData.getHeight(), height);
	}

	// valid italic font data for various heights
	fontData = new FontData(SwtJunit.testFontName, 10, SWT.ITALIC);	
	for (int height = 0; height < 1000; height++) {
		fontData.setHeight(height);
		assertEquals("Wrong height", fontData.getHeight(), height);
	}

	// valid bold italic font data for various heights
	fontData = new FontData(SwtJunit.testFontName, 10, SWT.BOLD | SWT.ITALIC);	
	for (int height = 0; height < 1000; height++) {
		fontData.setHeight(height);
		assertEquals("Wrong height", fontData.getHeight(), height);
	}
	
	final FontData data = new FontData();
	testPerformance(new Runnable() {
		public void run() {
			data.setHeight(10);
		}
	});
}

public void test_setLocaleLjava_lang_String() {
	FontData fd = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	Locale locale = Locale.ENGLISH;
	fd.setLocale(locale.toString());
	assertEquals(Locale.ENGLISH.toString(),fd.getLocale());
	final FontData data = new FontData();
	testPerformance(new Runnable() {
		public void run() {
			data.setLocale(Locale.ENGLISH.toString());
		}
	});
}

public void test_setNameLjava_lang_String() {
	// Test Font.setName(String name)
	// valid name
	FontData fontData = new FontData(SwtJunit.testFontName, 10, SWT.NORMAL);
	assertEquals("Wrong name", fontData.getName(), SwtJunit.testFontName);

	// valid name (unknown name, but valid)
	fontData.setName("bad-font");
	assertEquals("Wrong name", fontData.getName(), "bad-font");

	// valid name (empty string, but valid)
	// only on windows since motif supports separate font foundries
	if (SwtJunit.isWindows) {
		fontData.setName("");
		assertEquals("Wrong name", fontData.getName(), "");
	}

	// valid name
	fontData.setName(SwtJunit.testFontName);	
	assertEquals("Wrong name", fontData.getName(), SwtJunit.testFontName);
	// illegal argument, name == null
	try {
		fontData.setName(null);
		fail("No exception thrown for name == null");
	} catch (IllegalArgumentException e) {
	}
	
	final FontData data = new FontData();
	testPerformance(new Runnable() {
		public void run() {
			data.setName("name");
		}
	});
}

public void test_setStyleI() {
	// Test Font.setStyle(int  style)
	for (int height = 0; height < 1000; height++) {
		// valid normal font data
		FontData fontData = new FontData(SwtJunit.testFontName, height, SWT.NORMAL);	
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
	final FontData data = new FontData();
	testPerformance(new Runnable() {
		public void run() {
			data.setStyle(SWT.ITALIC);
		}
	});
}

public void test_toString() {
	FontData data = new FontData();
	assertNotNull(data.toString());
	assertTrue(data.toString().length() > 0);
}

public void test_win32_newLorg_eclipse_swt_internal_win32_LOGFONTI() {
	// do not test - Windows only
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_FontData((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_ConstructorLjava_lang_String");
	methodNames.addElement("test_ConstructorLjava_lang_StringII");
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_getHeight");
	methodNames.addElement("test_getLocale");
	methodNames.addElement("test_getName");
	methodNames.addElement("test_getStyle");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_setHeightI");
	methodNames.addElement("test_setLocaleLjava_lang_String");
	methodNames.addElement("test_setNameLjava_lang_String");
	methodNames.addElement("test_setStyleI");
	methodNames.addElement("test_toString");
	methodNames.addElement("test_win32_newLorg_eclipse_swt_internal_win32_LOGFONTI");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_ConstructorLjava_lang_String")) test_ConstructorLjava_lang_String();
	else if (getName().equals("test_ConstructorLjava_lang_StringII")) test_ConstructorLjava_lang_StringII();
	else if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_getHeight")) test_getHeight();
	else if (getName().equals("test_getLocale")) test_getLocale();
	else if (getName().equals("test_getName")) test_getName();
	else if (getName().equals("test_getStyle")) test_getStyle();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_setHeightI")) test_setHeightI();
	else if (getName().equals("test_setLocaleLjava_lang_String")) test_setLocaleLjava_lang_String();
	else if (getName().equals("test_setNameLjava_lang_String")) test_setNameLjava_lang_String();
	else if (getName().equals("test_setStyleI")) test_setStyleI();
	else if (getName().equals("test_toString")) test_toString();
	else if (getName().equals("test_win32_newLorg_eclipse_swt_internal_win32_LOGFONTI")) test_win32_newLorg_eclipse_swt_internal_win32_LOGFONTI();
}
}
