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


import org.eclipse.swt.graphics.*;
import org.eclipse.test.performance.PerformanceMeter;

import junit.framework.*;
import junit.textui.*;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.PaletteData
 *
 * @see org.eclipse.swt.graphics.PaletteData
 */
public class Test_org_eclipse_swt_graphics_PaletteData extends SwtPerformanceTestCase {

public Test_org_eclipse_swt_graphics_PaletteData(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_Constructor$Lorg_eclipse_swt_graphics_RGB() {
	final int COUNT = 25000000;
	
	RGB[] rgb = new RGB[] {new RGB(0, 0, 0), new RGB(255, 255, 255)};
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		new PaletteData(rgb);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_ConstructorIII() {
	final int COUNT = 6000000;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		new PaletteData(0xff0000, 0x00ff00, 0x0000ff);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getPixelLorg_eclipse_swt_graphics_RGB() {
	final int COUNT = 50000000;
	
	RGB rgb = new RGB(0x32, 0x64, 0x96);
	PaletteData data = new PaletteData(0xff0000, 0x00ff00, 0x0000ff);
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		data.getPixel(rgb);		
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getRGBI() {
	final int COUNT = 15000000;
	
	RGB rgb = new RGB(0x32, 0x64, 0x96);
	PaletteData data = new PaletteData(0xff0000, 0x00ff00, 0x0000ff);
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		data.getRGB(0x326496);		
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getRGBs() {
	final int COUNT = 700000000;
	
	RGB rgb = new RGB(0x32, 0x64, 0x96);
	PaletteData data = new PaletteData(0xff0000, 0x00ff00, 0x0000ff);
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		data.getRGBs();		
	}
	meter.stop();
	
	disposeMeter(meter);
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_PaletteData((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor$Lorg_eclipse_swt_graphics_RGB");
	methodNames.addElement("test_ConstructorIII");
	methodNames.addElement("test_getPixelLorg_eclipse_swt_graphics_RGB");
	methodNames.addElement("test_getRGBI");
	methodNames.addElement("test_getRGBs");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor$Lorg_eclipse_swt_graphics_RGB")) test_Constructor$Lorg_eclipse_swt_graphics_RGB();
	else if (getName().equals("test_ConstructorIII")) test_ConstructorIII();
	else if (getName().equals("test_getPixelLorg_eclipse_swt_graphics_RGB")) test_getPixelLorg_eclipse_swt_graphics_RGB();
	else if (getName().equals("test_getRGBI")) test_getRGBI();
	else if (getName().equals("test_getRGBs")) test_getRGBs();
}
}
