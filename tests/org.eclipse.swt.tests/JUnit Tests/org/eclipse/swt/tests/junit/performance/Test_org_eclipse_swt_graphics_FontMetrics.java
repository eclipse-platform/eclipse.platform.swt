/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit.performance;


import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.test.performance.PerformanceMeter;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.FontMetrics
 *
 * @see org.eclipse.swt.graphics.FontMetrics
 */
public class Test_org_eclipse_swt_graphics_FontMetrics extends SwtPerformanceTestCase {

public Test_org_eclipse_swt_graphics_FontMetrics(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() throws Exception {
	super.setUp();
	display = Display.getDefault();
	shell = new Shell(display);
	gc = new GC(shell);
}

protected void tearDown() throws Exception {
	super.tearDown();
	gc.dispose();
	shell.dispose();
}

public void test_equalsLjava_lang_Object() {
	final int COUNT = 50000000;
	
	FontMetrics fm1 = gc.getFontMetrics();
	
	Font oldFont = gc.getFont();
	FontData newFontData = oldFont.getFontData()[0];
	newFontData.setHeight(newFontData.getHeight() + 6);
	
	Font newFont = new Font(display, newFontData);
	gc.setFont(newFont);
	FontMetrics fm2 = gc.getFontMetrics();
	gc.setFont(oldFont);
	newFont.dispose();
	
	PerformanceMeter meter = createMeter("equals");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		fm1.equals(fm1);	// same
	}
	meter.stop();

	disposeMeter(meter);
	
	meter = createMeter("not equals");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		fm1.equals(fm2);	// different
	}
	meter.stop();

	disposeMeter(meter);
}

public void test_getAscent() {
	final int COUNT = 500000000;
	
	FontMetrics fm = gc.getFontMetrics();
	
	PerformanceMeter meter = createMeter();
	meter.start();	for (int i = 0; i < COUNT; i++) {
		fm.getAscent();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getAverageCharWidth() {
	final int COUNT = 500000000;
	
	FontMetrics fm = gc.getFontMetrics();
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		fm.getAverageCharWidth();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getDescent() {
	final int COUNT = 500000000;
	
	FontMetrics fm = gc.getFontMetrics();
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		fm.getDescent();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getHeight() {
	final int COUNT = 500000000;
	
	FontMetrics fm = gc.getFontMetrics();
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		fm.getHeight();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getLeading() {
	final int COUNT = 500000000;
	
	FontMetrics fm = gc.getFontMetrics();
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		fm.getLeading();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_hashCode() {
	final int COUNT = 70000000;
	
	FontMetrics fm = gc.getFontMetrics();
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		fm.hashCode();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_win32_newLorg_eclipse_swt_internal_win32_TEXTMETRIC() {
	// do not test - Windows only
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_FontMetrics((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_getAscent");
	methodNames.addElement("test_getAverageCharWidth");
	methodNames.addElement("test_getDescent");
	methodNames.addElement("test_getHeight");
	methodNames.addElement("test_getLeading");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_win32_newLorg_eclipse_swt_internal_win32_TEXTMETRIC");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_getAscent")) test_getAscent();
	else if (getName().equals("test_getAverageCharWidth")) test_getAverageCharWidth();
	else if (getName().equals("test_getDescent")) test_getDescent();
	else if (getName().equals("test_getHeight")) test_getHeight();
	else if (getName().equals("test_getLeading")) test_getLeading();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_win32_newLorg_eclipse_swt_internal_win32_TEXTMETRIC")) test_win32_newLorg_eclipse_swt_internal_win32_TEXTMETRIC();
}

/* custom */
	Display display;
	Shell shell;
	GC gc;
}
