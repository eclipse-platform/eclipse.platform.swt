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
package org.eclipse.swt.tests.junit.performance;


import org.eclipse.swt.*;
import org.eclipse.swt.tests.junit.SwtJunit;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.test.performance.PerformanceMeter;

import junit.framework.*;
import junit.textui.*;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.Font
 *
 * @see org.eclipse.swt.graphics.Font
 */
public class Test_org_eclipse_swt_graphics_Font extends SwtPerformanceTestCase {
	Display display;

public Test_org_eclipse_swt_graphics_Font(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() throws Exception {
	super.setUp();
	display = Display.getDefault();
}

public void test_ConstructorLorg_eclipse_swt_graphics_Device$Lorg_eclipse_swt_graphics_FontData() {
	final int COUNT = 160000;
	
	FontData[] data = new FontData [1];
	data[0] = new FontData (SwtJunit.testFontName, 10, SWT.BOLD | SWT.ITALIC); 
	
	PerformanceMeter meter = createMeter("Font constr.(Device,$FontData");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		/*
		* This test is not really valid since it's measuring both creation and
		* disposal of the Fonts.  This is done because attempting to pre-create
		* more than 20000 Fonts for disposal causes a No More Handles error.
		*/ 
		new Font(display, data).dispose();
	}
	meter.stop();

	disposeMeter(meter);
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_FontData() {
	final int COUNT = 170000;
	
	FontData data = new FontData (SwtJunit.testFontName, 10, SWT.BOLD | SWT.ITALIC);
	
	PerformanceMeter meter = createMeter("Font constr.(Device,FontData)");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		/*
		* This test is not really valid since it's measuring both creation and
		* disposal of the Fonts.  This is done because attempting to pre-create
		* more than 20000 Fonts for disposal causes a No More Handles error.
		*/ 
		new Font(display, data).dispose();
	}
	meter.stop();

	disposeMeter(meter);
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_StringII() {
	final int COUNT = 150000;
	
	PerformanceMeter meter = createMeter("Font constr.(Device,String,II) - no name");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		/*
		* This test is not really valid since it's measuring both creation and
		* disposal of the Fonts.  This is done because attempting to pre-create
		* more than 20000 Fonts for disposal causes a No More Handles error.
		*/ 
		// valid font with no name
		new Font(display, "", 10, SWT.NORMAL).dispose();
	}
	meter.stop();

	disposeMeter(meter);
		
	meter = createMeter("Font constr.(Device,String,II) - unknown");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		/*
		* This test is not really valid since it's measuring both creation and
		* disposal of the Fonts.  This is done because attempting to pre-create
		* more than 20000 Fonts for disposal causes a No More Handles error.
		*/ 
		// valid font with unknown name
		new Font(display, "bad-font", 10, SWT.NORMAL).dispose();
	}
	meter.stop();

	disposeMeter(meter);
	
	meter = createMeter("Font constr.(Device,String,II) - 0 height");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		/*
		* This test is not really valid since it's measuring both creation and
		* disposal of the Fonts.  This is done because attempting to pre-create
		* more than 20000 Fonts for disposal causes a No More Handles error.
		*/ 
		// valid font with 0 height
		new Font(display, SwtJunit.testFontName, 0, SWT.NORMAL).dispose();
	}
	meter.stop();

	disposeMeter(meter);

	meter = createMeter("Font constr.(Device,String,II) - 10 pt");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		/*
		* This test is not really valid since it's measuring both creation and
		* disposal of the Fonts.  This is done because attempting to pre-create
		* more than 20000 Fonts for disposal causes a No More Handles error.
		*/ 
		// valid normal 10-point font
		new Font(display, SwtJunit.testFontName, 10, SWT.NORMAL).dispose();
	}
	meter.stop();

	disposeMeter(meter);

	meter = createMeter("Font constr.(Device,String,II) - 100 pt");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		/*
		* This test is not really valid since it's measuring both creation and
		* disposal of the Fonts.  This is done because attempting to pre-create
		* more than 20000 Fonts for disposal causes a No More Handles error.
		*/ 
		// valid normal 100-point font
		new Font(display, SwtJunit.testFontName, 100, SWT.NORMAL).dispose();
	}
	meter.stop();

	disposeMeter(meter);
	
	meter = createMeter("Font constr.(Device,String,II) - BI");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		/*
		* This test is not really valid since it's measuring both creation and
		* disposal of the Fonts.  This is done because attempting to pre-create
		* more than 20000 Fonts for disposal causes a No More Handles error.
		*/ 
		// valid bold italic 10-point font
		new Font(display, SwtJunit.testFontName, 10, SWT.BOLD | SWT.ITALIC).dispose();
	}
	meter.stop();

	disposeMeter(meter);

	meter = createMeter("Font constr.(Device,String,II) - null dev");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		/*
		* This test is not really valid since it's measuring both creation and
		* disposal of the Fonts.  This is done because attempting to pre-create
		* more than 20000 Fonts for disposal causes a No More Handles error.
		*/ 
		// device == null (valid)
		new Font(null, SwtJunit.testFontName, 10, SWT.NORMAL).dispose();
	}
	meter.stop();

	disposeMeter(meter);
}

public void test_getFontData() {
	final int COUNT = 250000;
	
	Font font = new Font(display, SwtJunit.testFontName, 40, SWT.BOLD | SWT.ITALIC);
	
	PerformanceMeter meter = createMeter("Font getFontData");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		font.getFontData();
	}
	meter.stop();
	
	font.dispose();
	
	disposeMeter(meter);
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
	methodNames.addElement("test_getFontData");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_Device$Lorg_eclipse_swt_graphics_FontData")) test_ConstructorLorg_eclipse_swt_graphics_Device$Lorg_eclipse_swt_graphics_FontData();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_FontData")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_FontData();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_StringII")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_StringII();
	else if (getName().equals("test_getFontData")) test_getFontData();
}
}
