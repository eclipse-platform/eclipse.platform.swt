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
import org.eclipse.test.performance.PerformanceMeter;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.Color
 *
 * @see org.eclipse.swt.graphics.Color
 */
public class Test_org_eclipse_swt_graphics_Color extends SwtPerformanceTestCase {

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
	// adding 500000 to either of these cause OOM
	final int COUNT = isGTK ? 1500000 : 3000000;
	
	Color[] colors = new Color [COUNT];

	PerformanceMeter meter = createMeter("Color constr.(Device,III)");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		colors[i] = new Color(display, 102, 255, 3);
	}
	meter.stop();
	
	for (int i = 0; i < COUNT; i++) {
		colors[i].dispose();
	}

	disposeMeter(meter);
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_RGB() {
	// adding 500000 to either of these cause OOM
	final int COUNT = isGTK ? 1500000 : 3000000;
	
	Color[] colors = new Color [COUNT];
	RGB rgb = new RGB(102, 255, 3);

	PerformanceMeter meter = createMeter("Color constr.(Device,RGB)");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		colors[i] = new Color(display, rgb);
	}
	meter.stop();
	
	for (int i = 0; i < COUNT; i++) {
		colors[i].dispose();
	}

	disposeMeter(meter);
}

public void test_dispose() {
	
	int count = 20000000; 
	
	PerformanceMeter meter = createMeter("Color dispose - typical");
	meter.start();
	for (int i = 0; i < count; i++) {
		/*
		* This test is not really valid since it's measuring both creation and
		* disposal of the Colors.  This is done because attempting to pre-create
		* more than 3500000 Colors for disposal causes an Out Of Memory error.
		*/ 
		new Color(display, 255, 0, 128).dispose();
	}
	meter.stop();
	
	disposeMeter(meter);

	count *= 3;
	
	Color disposedColor = new Color (display, 255, 0, 128);
	disposedColor.dispose();

	meter = createMeter("Color dispose - disposed");
	meter.start();
	for (int i = 0; i < count; i++) {
		disposedColor.dispose();	// dispose disposed
	}
	meter.stop();
	
    disposeMeter(meter);
}

public void test_equalsLjava_lang_Object() {
	final int COUNT = 60000000;
	
	Color color1 = new Color(display, 0, 128, 255);
	Color color2 = new Color(display, 0, 128, 255);
	
	PerformanceMeter meter = createMeter("Color equals - equal");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		color1.equals(color2);	// equal
	}
	meter.stop();
	
	color1.dispose();
	color2.dispose();
	
	disposeMeter(meter);
		
	color1 = new Color(display, 0, 128, 255);
	color2 = new Color(display, 128, 255, 0);
	
	meter = createMeter("Color equals - different");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		color1.equals(color2);	// not equal
	}
	meter.stop();
	
	color1.dispose();
	color2.dispose();
	
	disposeMeter(meter);
}

public void test_getBlue() {
	final int COUNT = 300000000;
	
	Color color = new Color(display, 128, 64, 255);
	
	PerformanceMeter meter = createMeter("Color getBlue");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		color.getBlue();
	}
	meter.stop();
	
	color.dispose();
	
	disposeMeter(meter);
}

public void test_getGreen() {
	final int COUNT = 300000000;
	
	Color color = new Color(display, 128, 64, 255);
	
	PerformanceMeter meter = createMeter("Color getGreen");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		color.getGreen();
	}
	meter.stop();
	
	color.dispose();
	
	disposeMeter(meter);
}

public void test_getRGB() {
	final int COUNT = 20000000;
	
	Color color = new Color(display, 128, 64, 255);
	
	PerformanceMeter meter = createMeter("Color getRGB");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		color.getRGB();
	}
	meter.stop();
	
	color.dispose();
	
	disposeMeter(meter);
}

public void test_getRed() {
	final int COUNT = 300000000;
	
	Color color = new Color(display, 128, 64, 255);
	
	PerformanceMeter meter = createMeter("Color getRed");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		color.getRed();
	}
	meter.stop();
	
	color.dispose();
	
	disposeMeter(meter);
}

public void test_hashCode() {
	final int COUNT = 600000000;
	
	Color color = new Color(display, 128, 64, 255);
	
	PerformanceMeter meter = createMeter("Color hashCode");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		color.hashCode();
	}
	meter.stop();
	
	color.dispose();

	disposeMeter(meter);
}

public void test_isDisposed() {
	final int COUNT = 600000000;
	
	Color color = new Color(display, 128, 128, 128);
	
	PerformanceMeter meter = createMeter("Color isDisposed - no");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		color.isDisposed();	// not disposed
	}
	meter.stop();
	
	color.dispose();
	
	disposeMeter(meter);
	
	meter = createMeter("Color isDisposed - yes");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		color.isDisposed();	// disposed
	}
	meter.stop();
	
	disposeMeter(meter);
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
}

/* custom */
Display display;
}
