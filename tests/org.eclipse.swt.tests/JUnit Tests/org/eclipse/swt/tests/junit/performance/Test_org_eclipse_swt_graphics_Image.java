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
import org.eclipse.test.performance.PerformanceMeter;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.Image
 *
 * @see org.eclipse.swt.graphics.Image
 */
public class Test_org_eclipse_swt_graphics_Image extends SwtPerformanceTestCase {

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
	final int COUNT = 30000;
	
	PerformanceMeter meter = createMeter("Image constr.(Device,II)");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		/*
		* This test is not really valid since it's measuring both creation and
		* disposal of the Images.  This is necessary because attempting to defer
		* the image disposal until the timer has been stopped causes a No More
		* Handles error.
		*/
		new Image(display, 100, 100).dispose(); 
	}
	meter.stop();

	disposeMeter(meter);
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageI() {
	final int COUNT = 6000;
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
	
	PerformanceMeter meter = createMeter("Image constr.(Device,Image,I) - copy");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		images[i] = new Image(display, image, SWT.IMAGE_COPY); 
	}
	meter.stop();

	for (int i = 0; i < COUNT; i++) {
		images[i].dispose(); 
	}
	
	disposeMeter(meter);
	
	meter = createMeter("Image constr.(Device,Image,I) - disable");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		images[i] = new Image(display, image, SWT.IMAGE_DISABLE); 
	}
	meter.stop();

	for (int i = 0; i < COUNT; i++) {
		images[i].dispose(); 
	}
	
	disposeMeter(meter);
	
	meter = createMeter("Image constr.(Device,Image,I) - gray");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		images[i] = new Image(display, image, SWT.IMAGE_GRAY);
	}
	meter.stop();

	for (int i = 0; i < COUNT; i++) {
		images[i].dispose(); 
	}
	
	disposeMeter(meter);	
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_Rectangle() {
	final int COUNT = 30000;
	
	Rectangle rectangle = new Rectangle(0, 0, 100, 100);
	
	PerformanceMeter meter = createMeter("Image constr.(Device,Rectangle)");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		/*
		* This test is not really valid since it's measuring both creation and
		* disposal of the Images.  This is necessary because attempting to defer
		* the image disposal until the timer has been stopped causes a No More
		* Handles error.
		*/
		new Image(display, rectangle).dispose();
	}
	meter.stop();

	disposeMeter(meter);
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageData() {
	final int COUNT = 60000;
	
	String name = getPath(imageFilenames[0] + "." + imageFormats[0]);
	FileInputStream inStream = null;
	try {
		inStream = new FileInputStream(name);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	ImageData[] imageData = new ImageLoader().load(inStream);
	try {
		inStream.close();
	} catch (IOException e1) {
		e1.printStackTrace();
	}

	PerformanceMeter meter = createMeter("Image constr.(Device,ImageData)");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		/*
		* This test is not really valid since it's measuring both creation and
		* disposal of the Images.  This is done because attempting to pre-create
		* more than 8000 Images for disposal causes a No More Handles error.
		*/ 
		new Image(display, imageData[0]).dispose();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageData() {
	final int COUNT = 4000;	// 5000 causes an error
	
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

	PerformanceMeter meter = createMeter("Image constr.(Device,ImageData,ImageData)");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		images[i] = new Image(display, imageData, imageData1);
	}
	meter.stop();
	
	for (int i = 0; i < COUNT; i++) {
		images[i].dispose();
	}
	
	disposeMeter(meter);
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_io_InputStream() {
	// TODO
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_String() {
	final int COUNT = 1000;
	
	int numFileNames = imageFilenames.length;
	int numFormats = imageFormats.length;
	for (int k = 0; k < numFileNames; k++) {
		String fileName = imageFilenames[k];
		for (int i=0; i<numFormats; i++) {
			String format = imageFormats[i];
			String pathName = getPath(fileName + "." + format);
			Image[] images = new Image[COUNT];
			
			PerformanceMeter meter = createMeter("Image constr.(Device,String) - " + fileName + "." + format);
			meter.start();
			for (int j = 0; j < COUNT; j++) {
				images[j] = new Image(display, pathName);
			}
			meter.stop();

			for (int j = 0; j < COUNT; j++) {
				images[j].dispose();
			}
			
			disposeMeter(meter);
		}
	}
}

public void test_dispose() {
	final int COUNT = 50000000;
	
	/*
	* test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageData
	* covers the base dispose case since it has to dispose of created Images within its timer block.  
	*/
	
	Image disposedImage = new Image (display, 100, 100);
	disposedImage.dispose();
	
	PerformanceMeter meter = createMeter("Image dispose - disposed");

	meter.start();
	for (int i = 0; i < COUNT; i++) {
		disposedImage.dispose();	// disposed
	}
	meter.stop();
	
    disposeMeter(meter);
}

public void test_equalsLjava_lang_Object() {
	final int COUNT = 70000000;
	
	ImageData imageData = new ImageData(10, 10, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	Image image1 = new Image(display, imageData);
	Image image2 = new Image(display, imageData);
	
	PerformanceMeter meter = createMeter("Image equals - yes");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		image1.equals(image2);	// equal
	}
	meter.stop();
	
	image1.dispose();
	image2.dispose();
	
	disposeMeter(meter);
		
	image1 = new Image(display, imageData);
	image2 = new Image(display, 8, 8);
	
	meter = createMeter("Image equals - no");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		image1.equals(image2);	// not equal
	}
	meter.stop();
	
	image1.dispose();
	image2.dispose();
	
	disposeMeter(meter);
}

public void test_getBackground() {
	final int COUNT = 60000000;
	
	Image image = new Image(display, 100, 100);
	image.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
	
	PerformanceMeter meter = createMeter("Image getBackground");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		image.getBackground();
	}
	meter.stop();
	
	image.dispose();
	
	disposeMeter(meter);
}

public void test_getBounds() {
	final int COUNT = 1200000;
	
	Image image = new Image(display, 10, 20);
	
	PerformanceMeter meter = createMeter("Image getBounds");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		image.getBounds();
	}
	meter.stop();
	
	image.dispose();
	
	disposeMeter(meter);
}

public void test_getImageData() {
	final int COUNT = 50000;
	
	ImageData imageData = new ImageData(10, 10, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	Image image = new Image(display, imageData);
	
	PerformanceMeter meter = createMeter("Image getImageData");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		image.getImageData();
	}
	meter.stop();
	
	image.dispose();
	
	disposeMeter(meter);
}

public void test_hashCode() {
	final int COUNT = 700000000;
	
	ImageData imageData = new ImageData(10, 10, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	Image image = new Image(display, imageData);
	
	PerformanceMeter meter = createMeter("Image hashCode");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		image.hashCode();
	}
	meter.stop();
	
	image.dispose();
	
	disposeMeter(meter);
}

public void test_isDisposed() {
	final int COUNT = 500000000;
	
	Image image = new Image(display, 10, 10);
	
	PerformanceMeter meter = createMeter("Image isDisposed - no");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		image.isDisposed();	// not disposed
	}
	meter.stop();
	
	image.dispose();
	
	disposeMeter(meter);
	
	meter = createMeter("Image isDisposed - yes");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		image.isDisposed();	// disposed
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	final int COUNT = 60000000;
	
	Image image = new Image(display, 100, 100);
	Color color1 = display.getSystemColor(SWT.COLOR_GREEN);
	Color color2 = display.getSystemColor(SWT.COLOR_RED);
	
	PerformanceMeter meter = createMeter("Image setBackground");
	meter.start();
	for (int i = 0; i < COUNT / 2; i++) {
		image.setBackground(color1);
		image.setBackground(color2);
	}
	meter.stop();
	
	image.dispose();
	
	disposeMeter(meter);
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
	methodNames.addElement("test_isDisposed");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");
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
	else if (getName().equals("test_isDisposed")) test_isDisposed();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Color")) test_setBackgroundLorg_eclipse_swt_graphics_Color();
}
/* custom */
Display display;

}
