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


import java.io.*;

import junit.framework.*;
import junit.textui.TestRunner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.test.performance.PerformanceMeter;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.Cursor
 *
 * @see org.eclipse.swt.graphics.Cursor
 */
public class Test_org_eclipse_swt_graphics_Cursor extends SwtPerformanceTestCase {

public Test_org_eclipse_swt_graphics_Cursor(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() throws Exception {
	super.setUp();
	display = Display.getDefault();
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceI() {
	final int COUNT = 600000;
	
	PerformanceMeter meter = createMeter("Cursor constr.(Device,I)");
	meter.start();
	Cursor[] cursors = new Cursor [COUNT];
	for (int i = 0; i < COUNT; i++) {
		cursors[i] = new Cursor(display, SWT.CURSOR_ARROW);
	}
	meter.stop();
	
	for (int i = 0; i < COUNT; i++) {
		cursors[i].dispose();
	}
	
    disposeMeter(meter);
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataII() {
	// TODO
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageDataII() {
	final int COUNT = 1000000;
	
	int numFormats = imageFormats.length;
	String fileName = imageFilenames[0];
	for (int i = 0; i < numFormats; i++) {
		String format = imageFormats[i];
		ImageLoader loader = new ImageLoader();
		FileInputStream inStream = null;
		try {
			inStream = new FileInputStream(getPath (fileName + "." + format));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		ImageData source = loader.load(inStream)[0];
		try {
			inStream.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		ImageData mask = source.getTransparencyMask();
		if (mask != null && (source.depth == 1)) {
			Cursor[] cursors = new Cursor[COUNT];
			
			PerformanceMeter meter = createMeter("Cursor constr.(5 args) - " + format);
			meter.start();
			for (int j = 0; j < COUNT; j++) {
				cursors[j] = new Cursor(display, source, mask, 0, 0);
			}
			meter.stop();
			
			for (int j = 0; j < COUNT; j++) {
				cursors[j].dispose();
			}
			
			disposeMeter(meter);
		}
	}
}
public void test_dispose() {
	final int COUNT = 2000000;
	
	Cursor[] cursors = new Cursor [COUNT];
	for (int i = 0; i < COUNT; i++) {
		cursors[i] = new Cursor(display, SWT.CURSOR_ARROW);
	}
	
	PerformanceMeter meter = createMeter("Cursor dispose - typical");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		cursors[i].dispose();	// dispose
	}
	meter.stop();
	
    disposeMeter(meter);
    
	meter = createMeter("Cursor dispose - disposed");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		cursors[i].dispose();	// dispose disposed
	}
	meter.stop();
	
    disposeMeter(meter);
}

public void test_equalsLjava_lang_Object() {
	final int COUNT = 60000000;
	
	/* 
	* Note: Two cursors are only considered equal if their handles are equal.
	* So since Windows reuses cursor handles, and other platforms do not,
	* it does not make sense to test whether cursor.equals(sameCursor).
	*/
	Cursor cursor = new Cursor(display, SWT.CURSOR_WAIT);
	Cursor otherCursor = new Cursor(display, SWT.CURSOR_CROSS);
	
	PerformanceMeter meter = createMeter("Cursor equals");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		cursor.equals(otherCursor);
	}
	meter.stop();

	cursor.dispose();
	otherCursor.dispose();
	
	disposeMeter(meter);
}

public void test_hashCode() {
	final int COUNT = 600000000;
	
	Cursor cursor = new Cursor(display, SWT.CURSOR_WAIT);
	
	PerformanceMeter meter = createMeter("Cursor hashCode");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		cursor.hashCode();
	}
	meter.stop();
	
	cursor.dispose();
	
	disposeMeter(meter);
}

public void test_isDisposed() {
	final int COUNT = 500000000;
	
	Cursor cursor = new Cursor(display, SWT.CURSOR_WAIT);
	
	PerformanceMeter meter = createMeter("Cursor isDisposed - no");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		cursor.isDisposed();	// not disposed
	}
	meter.stop();
	
	cursor.dispose();
	
	disposeMeter(meter);
	
	meter = createMeter("Cursor isDisposed - yes");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		cursor.isDisposed();	// disposed
	}
	meter.stop();
	
	disposeMeter(meter);
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_Cursor((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataII");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageDataII");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_isDisposed");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceI")) test_ConstructorLorg_eclipse_swt_graphics_DeviceI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataII")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataII();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageDataII")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageDataII();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_isDisposed")) test_isDisposed();
}

/* custom */
Display display;
}
