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
package org.eclipse.swt.tests.junit.performance;

import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.Color
 *
 * @see org.eclipse.swt.graphics.Color
 */
public class Test_org_eclipse_swt_graphics_Color extends SwtPerformanceTestCase {
	static final int COUNT = 10000;

public Test_org_eclipse_swt_graphics_Color(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() throws Exception {
	super.setUp();
	display = Display.getDefault();
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceIII() {
	Color[] colors = new Color [COUNT];

	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		colors[i] = new Color(display, 102, 255, 3);
	}
	stopMeasuring();
	
	for (int i = 0; i < COUNT; i++) {
		colors[i].dispose();
	}

	commitMeasurements();
	assertPerformance();
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_RGB() {
	Color[] colors = new Color [COUNT];
	RGB rgb = new RGB(102, 255, 3);

	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		colors[i] = new Color(display, rgb);
	}
	stopMeasuring();
	
	for (int i = 0; i < COUNT; i++) {
		colors[i].dispose();
	}

	commitMeasurements();
	assertPerformance();
}

public void test_dispose() {
	Color[] colors = new Color [COUNT];
	for (int i = 0; i < COUNT; i++) {
		colors[i] = new Color(display, 255,0,128);
	}
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		colors[i].dispose();	// dispose
	}
	stopMeasuring();
	
    commitMeasurements();
    assertPerformance();
    
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		colors[i].dispose();	// dispose disposed
	}
	stopMeasuring();
	
    commitMeasurements();
    assertPerformance();
}

public void test_equalsLjava_lang_Object() {
	Color color1 = new Color(display, 0, 128, 255);
	Color color2 = new Color(display, 0, 128, 255);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		color1.equals(color2);	// equal
	}
	stopMeasuring();
	
	color1.dispose();
	color2.dispose();
	
	commitMeasurements();
	assertPerformance();
		
	color1 = new Color(display, 0, 128, 255);
	color2 = new Color(display, 128, 255, 0);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		color1.equals(color2);	// not equal
	}
	stopMeasuring();
	
	color1.dispose();
	color2.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_getBlue() {
	Color color = new Color(display, 128, 64, 255);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		color.getBlue();
	}
	stopMeasuring();
	
	color.dispose();
	commitMeasurements();
	assertPerformance();
}

public void test_getGreen() {
	Color color = new Color(display, 128, 64, 255);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		color.getGreen();
	}
	stopMeasuring();
	
	color.dispose();
	commitMeasurements();
	assertPerformance();
}

public void test_getRGB() {
	Color color = new Color(display, 128, 64, 255);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		color.getRGB();
	}
	stopMeasuring();
	
	color.dispose();
	commitMeasurements();
	assertPerformance();
}

public void test_getRed() {
	Color color = new Color(display, 128, 64, 255);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		color.getRed();
	}
	stopMeasuring();
	
	color.dispose();
	commitMeasurements();
	assertPerformance();
}

public void test_hashCode() {
	Color color = new Color(display, 128, 64, 255);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		color.hashCode();
	}
	stopMeasuring();
	
	color.dispose();
	commitMeasurements();
	assertPerformance();
}

public void test_isDisposed() {
	Color color = new Color(display, 128, 128, 128);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		color.isDisposed();	// not disposed
	}
	stopMeasuring();
	
	color.dispose();
	
	commitMeasurements();
	assertPerformance();
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		color.isDisposed();	// disposed
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_win32_newLorg_eclipse_swt_graphics_DeviceI() {
	// do not test - Windows platform only
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_Color((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceIII");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_RGB");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_getBlue");
	methodNames.addElement("test_getGreen");
	methodNames.addElement("test_getRGB");
	methodNames.addElement("test_getRed");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_isDisposed");
	methodNames.addElement("test_win32_newLorg_eclipse_swt_graphics_DeviceI");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceIII")) test_ConstructorLorg_eclipse_swt_graphics_DeviceIII();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_RGB")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_RGB();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_getBlue")) test_getBlue();
	else if (getName().equals("test_getGreen")) test_getGreen();
	else if (getName().equals("test_getRGB")) test_getRGB();
	else if (getName().equals("test_getRed")) test_getRed();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_isDisposed")) test_isDisposed();
	else if (getName().equals("test_win32_newLorg_eclipse_swt_graphics_DeviceI")) test_win32_newLorg_eclipse_swt_graphics_DeviceI();
}

/* custom */
Display display;
}
