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

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.test.performance.PerformanceMeter;

import junit.framework.*;
import junit.textui.*;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.ImageData
 *
 * @see org.eclipse.swt.graphics.ImageData
 */
public class Test_org_eclipse_swt_graphics_ImageData extends SwtPerformanceTestCase {
	static final int COUNT = 1000;

public Test_org_eclipse_swt_graphics_ImageData(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() throws Exception {
	super.setUp();
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 32, new PaletteData(0xFF0000, 0xFF00, 0xFF));
}

public void test_ConstructorIIILorg_eclipse_swt_graphics_PaletteData() {
	int[] validDepths = {1, 2, 4, 8, 16, 24, 32};
	PaletteData paletteData = new PaletteData(new RGB[] {new RGB(0, 0, 0)});
	for (int i = 0; i < validDepths.length; i++) {
		PerformanceMeter meter = createMeter("" + validDepths[i]);
		meter.start();
		for (int j = 0; j < COUNT; j++) {
			new ImageData(100, 100, validDepths[i], paletteData);
		}
		meter.stop();
		disposeMeter(meter);
	};
}

public void test_ConstructorIIILorg_eclipse_swt_graphics_PaletteDataI$B() {
	PaletteData paletteData = new PaletteData(new RGB[] {new RGB(0, 0, 0)});
	byte[] data = new byte[] {};
	int[] validDepths = {1, 2, 4, 8, 16, 24, 32};
	
	for (int i = 0; i < validDepths.length; i++) {
		PerformanceMeter meter = createMeter("" + validDepths[i]);
		meter.start();
		for (int j = 0; j < COUNT; j++) {
			new ImageData(100, 100, validDepths[i], paletteData, 1, data);
		}
		meter.stop();
		disposeMeter(meter);
	}
}

public void test_ConstructorLjava_io_InputStream() {
	// TODO
}

public void test_ConstructorLjava_lang_String() {
	// j2se and j2me(cdc) can load from a filename but, j2me(cldc) throws an exception
	if (isJ2ME()) return;

	int numFormats = imageFormats.length;
	int numFileNames = imageFilenames.length;
	for (int k = 0; k < numFileNames; k++) {
		String fileName = imageFilenames[k];		
		for (int i = 0; i < numFormats; i++) {
			String format = imageFormats[i];
			String fullName = getPath(fileName + "." + format);
			
			PerformanceMeter meter = createMeter(fileName + "." + format);
			meter.start();
			for (int j = 0; j < COUNT; j++) {
				new ImageData(fullName);
			}
			meter.stop();
			
			disposeMeter(meter);
		}
	}
}

public void test_clone() {
	String name = getPath(imageFilenames[0] + "." + imageFormats[0]); 
	FileInputStream inStream = null;
	try {
		inStream = new FileInputStream (name);
	} catch (FileNotFoundException e1) {
		e1.printStackTrace();
	}
	ImageLoader loader = new ImageLoader();
	ImageData data = loader.load(inStream)[0];
	try {
		inStream.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		data.clone();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getAlphaII() {
	imageData.setAlpha(0, 0, 0xAA);
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		imageData.getAlpha(0, 0);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getAlphasIII$BI() {
	byte value = (byte)0xAA;
	byte[] values = new byte[] {value, (byte) (value+1), (byte) (value+2), (byte) (value+3), (byte) (value+4)};
	byte[] alphaData = new byte[20];
	imageData.setAlphas(0, 1, values.length, values, 0);

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		imageData.getAlphas(0, 1, 10, alphaData, 10);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getPixelII() {
	imageData.setPixel(0, 0, 0xAA);

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		imageData.getPixel(0, 0);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getPixelsIII$BI() {
	byte[] pixelData = new byte[20];
	PaletteData paletteData = new PaletteData(0xFF0000, 0xFF00, 0xFF);
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 8, paletteData);
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		imageData.getPixels(0, 1, 10, pixelData, 10);		
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getPixelsIII$II() {
	int[] pixelData = new int[20];
	PaletteData paletteData = new PaletteData(0xFF0000, 0xFF00, 0xFF);
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 32, paletteData);
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		imageData.getPixels(0, 1, 10, pixelData, 10);		
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getRGBs() {
	RGB[] rgbs = new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)};
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 8, new PaletteData(rgbs));
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		imageData.getRGBs();		
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getTransparencyMask() {
	String name = getPath(transparentImageFilenames[0]);
	FileInputStream inStream = null;
	try {
		inStream = new FileInputStream(name);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	Image image = new Image(Display.getDefault(), inStream);
	imageData = image.getImageData();
	image.dispose();
	try {
		inStream.close();
	} catch (IOException e1) {
		e1.printStackTrace();
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		imageData.getTransparencyMask();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getTransparencyType() {
	FileInputStream inStream = null;
	String name = getPath(imageFilenames[0] + '.' + imageFormats[imageFormats.length-1]);
	try {
		inStream = new FileInputStream(name);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	Image image = new Image(Display.getDefault(), inStream);
	imageData = image.getImageData();
	image.dispose();
	try {
		inStream.close();
	} catch (IOException e1) {
		e1.printStackTrace();
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		imageData.getTransparencyType();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_internal_newIIILorg_eclipse_swt_graphics_PaletteDataI$BI$B$BIIIIIII() {
	// do not test internal API
	// javadoc states:
	// <b>IMPORTANT:</b> This method is <em>not</em> part of the public
	// API for <code>ImageData</code>. It is marked public only so that it
	// can be shared within the packages provided by SWT. It is subject
	// to change without notice, and should never be called from
	// application code.
}

public void test_scaledToII() {
	int imageDimension = 8;
	RGB[] rgbs = new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)};
	byte[] pixelData = new byte[(imageDimension*imageDimension) / 8];
	pixelData[0] = 0x4F;
	imageData = new ImageData(imageDimension, imageDimension, 1, new PaletteData(rgbs), 1, pixelData);
	int newHeight = imageDimension * 10;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		imageData.scaledTo(imageDimension, newHeight);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_setAlphaIII() {
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		imageData.setAlpha(0, 0, 0xAA);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_setAlphasIII$BI() {
	byte value = (byte) 0xAA;
	int OFFSET = 1;
	byte[] alphaData = new byte[20];
	byte[] values = new byte[] {value, (byte) (value+1), (byte) (value+2), (byte) (value+3), (byte) (value+4)};
	imageData.setAlphas(0, 1, values.length - OFFSET, values, OFFSET);
	int putWidth = values.length - OFFSET;
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		imageData.setAlphas(0, 1, putWidth, values, OFFSET);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_setPixelIII() {
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		imageData.setPixel(0, 0, 0xAA);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_setPixelsIII$BI() {
	int OFFSET = 1;
	byte[] values = new byte[]{0x1, 0x2, 0x3, 0xF, (byte)0xFF};
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 8, new PaletteData(0xFF0000, 0xFF00, 0xFF));	
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		imageData.setPixels(0, 1, values.length - OFFSET, values, OFFSET);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_setPixelsIII$II() {
	int OFFSET = 1;
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 32, new PaletteData(0xFF000000, 0xFF00, 0xFF));
	int[] values = new int[]{0, 0xFF, 0xFFAA, 0xFF00AA00};
	imageData.setPixels(0, 1, values.length - OFFSET, values, OFFSET);

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		imageData.setPixels(0, 1, values.length - OFFSET, values, OFFSET);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_ImageData((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorIIILorg_eclipse_swt_graphics_PaletteData");
	methodNames.addElement("test_ConstructorIIILorg_eclipse_swt_graphics_PaletteDataI$B");
	methodNames.addElement("test_ConstructorLjava_io_InputStream");
	methodNames.addElement("test_ConstructorLjava_lang_String");
	methodNames.addElement("test_clone");
	methodNames.addElement("test_getAlphaII");
	methodNames.addElement("test_getAlphasIII$BI");
	methodNames.addElement("test_getPixelII");
	methodNames.addElement("test_getPixelsIII$BI");
	methodNames.addElement("test_getPixelsIII$II");
	methodNames.addElement("test_getRGBs");
	methodNames.addElement("test_getTransparencyMask");
	methodNames.addElement("test_getTransparencyType");
	methodNames.addElement("test_internal_newIIILorg_eclipse_swt_graphics_PaletteDataI$BI$B$BIIIIIII");
	methodNames.addElement("test_scaledToII");
	methodNames.addElement("test_setAlphaIII");
	methodNames.addElement("test_setAlphasIII$BI");
	methodNames.addElement("test_setPixelIII");
	methodNames.addElement("test_setPixelsIII$BI");
	methodNames.addElement("test_setPixelsIII$II");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorIIILorg_eclipse_swt_graphics_PaletteData")) test_ConstructorIIILorg_eclipse_swt_graphics_PaletteData();
	else if (getName().equals("test_ConstructorIIILorg_eclipse_swt_graphics_PaletteDataI$B")) test_ConstructorIIILorg_eclipse_swt_graphics_PaletteDataI$B();
	else if (getName().equals("test_ConstructorLjava_io_InputStream")) test_ConstructorLjava_io_InputStream();
	else if (getName().equals("test_ConstructorLjava_lang_String")) test_ConstructorLjava_lang_String();
	else if (getName().equals("test_clone")) test_clone();
	else if (getName().equals("test_getAlphaII")) test_getAlphaII();
	else if (getName().equals("test_getAlphasIII$BI")) test_getAlphasIII$BI();
	else if (getName().equals("test_getPixelII")) test_getPixelII();
	else if (getName().equals("test_getPixelsIII$BI")) test_getPixelsIII$BI();
	else if (getName().equals("test_getPixelsIII$II")) test_getPixelsIII$II();
	else if (getName().equals("test_getRGBs")) test_getRGBs();
	else if (getName().equals("test_getTransparencyMask")) test_getTransparencyMask();
	else if (getName().equals("test_getTransparencyType")) test_getTransparencyType();
	else if (getName().equals("test_internal_newIIILorg_eclipse_swt_graphics_PaletteDataI$BI$B$BIIIIIII")) test_internal_newIIILorg_eclipse_swt_graphics_PaletteDataI$BI$B$BIIIIIII();
	else if (getName().equals("test_scaledToII")) test_scaledToII();
	else if (getName().equals("test_setAlphaIII")) test_setAlphaIII();
	else if (getName().equals("test_setAlphasIII$BI")) test_setAlphasIII$BI();
	else if (getName().equals("test_setPixelIII")) test_setPixelIII();
	else if (getName().equals("test_setPixelsIII$BI")) test_setPixelsIII$BI();
	else if (getName().equals("test_setPixelsIII$II")) test_setPixelsIII$II();
}
/* custom */
ImageData imageData;
int IMAGE_DIMENSION = 10;

void assertEquals(String message, byte expected[], byte actual[]) {
	if (expected == null && actual == null)
		return;
	boolean equal = false;
	if (expected != null && actual != null && expected.length == actual.length) {
		if (expected.length == 0)
			return;
		equal = true;
		for (int i = 0; i < expected.length; i++) {
			if (expected[i] != actual[i]) {
				equal = false;
			}
		}
	}
	if (!equal) {
		String formatted= "";
		if (message != null)
			formatted= message+" ";
		fail(formatted+"expected:<"+expected+"> but was:<"+actual+">");
	}
}

}
