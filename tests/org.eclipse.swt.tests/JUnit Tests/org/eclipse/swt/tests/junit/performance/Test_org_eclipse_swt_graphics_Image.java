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


import java.io.*;

import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.Image
 *
 * @see org.eclipse.swt.graphics.Image
 */
public class Test_org_eclipse_swt_graphics_Image extends SwtPerformanceTestCase {
	static final int COUNT = 1000;

public Test_org_eclipse_swt_graphics_Image(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() throws Exception {
	super.setUp();
	display = Display.getDefault();
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceII() {
	Image[] images = new Image[COUNT];
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		images[i] = new Image(display, 100, 100); 
	}
	stopMeasuring();

	for (int i = 0; i < COUNT; i++) {
		images[i].dispose(); 
	}
	
	commitMeasurements();
	assertPerformance();
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageI() {
	String name = getPath(imageFilenames[0] + "." + imageFormats[0]);
	FileInputStream inStream = null;
	try {
		inStream = new FileInputStream(name);
	} catch (FileNotFoundException e1) {
		e1.printStackTrace();
	}
	ImageData[] data = new ImageLoader().load(inStream);
	try {
		inStream.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	Image image = new Image (display, data[0]);
	image.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
	
	Image[] images = new Image[COUNT];
	
	startMeasuring();	// COPY
	for (int i = 0; i < COUNT; i++) {
		images[i] = new Image(display, image, SWT.IMAGE_COPY); 
	}
	stopMeasuring();

	for (int i = 0; i < COUNT; i++) {
		images[i].dispose(); 
	}
	
	commitMeasurements();
	assertPerformance();
	
	startMeasuring();	// DISABLE
	for (int i = 0; i < COUNT; i++) {
		images[i] = new Image(display, image, SWT.IMAGE_DISABLE); 
	}
	stopMeasuring();

	for (int i = 0; i < COUNT; i++) {
		images[i].dispose(); 
	}
	
	commitMeasurements();
	assertPerformance();
	
	startMeasuring();	// GRAY
	for (int i = 0; i < COUNT; i++) {
		images[i] = new Image(display, image, SWT.IMAGE_GRAY);
	}
	stopMeasuring();

	for (int i = 0; i < COUNT; i++) {
		images[i].dispose(); 
	}
	
	commitMeasurements();
	assertPerformance();	
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_Rectangle() {
	Image[] images = new Image[COUNT];
	Rectangle rectangle = new Rectangle(0, 0, 100, 100);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		images[i] = new Image(display, rectangle);
	}
	stopMeasuring();

	for (int i = 0; i < COUNT; i++) {
		images[i].dispose();
	}

	commitMeasurements();
	assertPerformance();
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageData() {
	String name = getPath(imageFilenames[0] + "." + imageFormats[0]);
	FileInputStream inStream = null;
	try {
		inStream = new FileInputStream(name);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	ImageData[] imageData = new ImageLoader().load(inStream);
	Image[] images = new Image[COUNT];
	try {
		inStream.close();
	} catch (IOException e1) {
		e1.printStackTrace();
	}

	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		images[i] = new Image(display, imageData[0]);
	}
	stopMeasuring();
	
	for (int i = 0; i < COUNT; i++) {
		images[i].dispose();
	}
	
	commitMeasurements();
	assertPerformance();
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageData() {
	Image[] images = new Image[COUNT];
	String name = getPath(imageFilenames[0] + "." + imageFormats[0]);
	FileInputStream inStream = null;
	try {
		inStream = new FileInputStream(name);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	ImageData imageData = new ImageLoader().load(inStream)[0];
	ImageData imageData1 = new ImageData(imageData.width, imageData.height, 8, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	try {
		inStream.close();
	} catch (IOException e1) {
		e1.printStackTrace();
	}

	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		images[i] = new Image(display, imageData, imageData1);
	}
	stopMeasuring();
	
	for (int i = 0; i < COUNT; i++) {
		images[i].dispose();
	}
	
	commitMeasurements();
	assertPerformance();
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_io_InputStream() {
	// TODO
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_String() {
	int numFileNames = imageFilenames.length;
	int numFormats = imageFormats.length;
	for (int k=0; k<numFileNames; k++) {
		String fileName = imageFilenames[k];
		for (int i=0; i<numFormats; i++) {
			String format = imageFormats[i];
			String pathName = getPath(fileName + "." + format);
			Image[] images = new Image[COUNT];
			
			startMeasuring();
			for (int j = 0; j < COUNT; j++) {
				images[j] = new Image(display, pathName);
			}
			stopMeasuring();

			for (int j = 0; j < COUNT; j++) {
				images[j].dispose();
			}
			
			commitMeasurements();
			assertPerformance();
		}
	}
}

public void test_dispose() {
	Image[] images = new Image [COUNT];
	for (int i = 0; i < COUNT; i++) {
		images[i] = new Image(display, 100, 100);
	}
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		images[i].dispose();	// dispose
	}
	stopMeasuring();
	
    commitMeasurements();
    assertPerformance();
    
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		images[i].dispose();	// dispose disposed
	}
	stopMeasuring();
	
    commitMeasurements();
    assertPerformance();
}

public void test_equalsLjava_lang_Object() {
	ImageData imageData = new ImageData(10, 10, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	Image image1 = new Image(display, imageData);
	Image image2 = new Image(display, imageData);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		image1.equals(image2);	// equal
	}
	stopMeasuring();
	
	image1.dispose();
	image2.dispose();
	
	commitMeasurements();
	assertPerformance();
		
	image1 = new Image(display, imageData);
	image2 = new Image(display, 8, 8);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		image1.equals(image2);	// not equal
	}
	stopMeasuring();
	
	image1.dispose();
	image2.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_getBackground() {
	Image image = new Image(display, 100, 100);
	image.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		image.getBackground();
	}
	stopMeasuring();
	
	image.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_getBounds() {
	Image image = new Image(display, 10, 20);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		image.getBounds();
	}
	stopMeasuring();
	
	image.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_getImageData() {
	ImageData imageData = new ImageData(10, 10, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	Image image = new Image(display, imageData);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		image.getImageData();
	}
	stopMeasuring();
	
	image.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_hashCode() {
	ImageData imageData = new ImageData(10, 10, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	Image image = new Image(display, imageData);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		image.hashCode();
	}
	stopMeasuring();
	
	image.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_internal_new_GCLorg_eclipse_swt_graphics_GCData() {
	// javadoc states:
	// <b>IMPORTANT:</b> This method is <em>not</em> part of the public
	// API for <code>Image</code>
}

public void test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData() {
	// javadoc states:
	// <b>IMPORTANT:</b> This method is <em>not</em> part of the public
	// API for <code>Image</code>
}

public void test_isDisposed() {
	Image image = new Image(display, 10, 10);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		image.isDisposed();	// not disposed
	}
	stopMeasuring();
	
	image.dispose();
	
	commitMeasurements();
	assertPerformance();
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		image.isDisposed();	// disposed
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	Image image = new Image(display, 100, 100);
	Color color1 = display.getSystemColor(SWT.COLOR_GREEN);
	Color color2 = display.getSystemColor(SWT.COLOR_RED);
	
	startMeasuring();
	for (int i = 0; i < COUNT / 2; i++) {
		image.setBackground(color1);
		image.setBackground(color2);
	}
	stopMeasuring();
	
	image.dispose();
	
	commitMeasurements();
	assertPerformance();
}

public void test_win32_newLorg_eclipse_swt_graphics_DeviceII() {
	// do not test - Windows only
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_Image((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceII");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_io_InputStream");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_String");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageData");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageData");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_getBackground");
	methodNames.addElement("test_getBounds");
	methodNames.addElement("test_getImageData");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_internal_new_GCLorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_isDisposed");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_win32_newLorg_eclipse_swt_graphics_DeviceII");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceII")) test_ConstructorLorg_eclipse_swt_graphics_DeviceII();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_io_InputStream")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_io_InputStream();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_String")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_String();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageData")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageData();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageData")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageData();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageI")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_Rectangle")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_getBackground")) test_getBackground();
	else if (getName().equals("test_getBounds")) test_getBounds();
	else if (getName().equals("test_getImageData")) test_getImageData();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData")) test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_internal_new_GCLorg_eclipse_swt_graphics_GCData")) test_internal_new_GCLorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_isDisposed")) test_isDisposed();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Color")) test_setBackgroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_win32_newLorg_eclipse_swt_graphics_DeviceII")) test_win32_newLorg_eclipse_swt_graphics_DeviceII();
}
/* custom */
Display display;

}
