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


import java.io.*;

import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.Image
 *
 * @see org.eclipse.swt.graphics.Image
 */
public class Test_org_eclipse_swt_graphics_Image extends SwtTestCase {

public Test_org_eclipse_swt_graphics_Image(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	display = Display.getDefault();
}

protected void tearDown() {
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceII() {
	Image image;
	try {
		image = new Image(display, -1, 10);
		image.dispose();
		fail("No exception thrown for width <= 0");
	} catch (IllegalArgumentException e) {
	}

	try {
		image = new Image(display, 10, 0);
		image.dispose();
		fail("No exception thrown for height <= 0");
	} catch (IllegalArgumentException e) {
	}

	image = new Image(null, 10, 10);
	image.dispose();

	image = new Image(display, 10, 10);
	image.dispose();
		
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageI not written");
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_Rectangle() {
	Image image;
	Rectangle bounds = null;

	try {
		image = new Image(display, bounds);
		image.dispose();
		fail("No exception thrown for rectangle == null");
	} catch (IllegalArgumentException e) {
	}

	bounds = new Rectangle(0, 0, -1, 10);
	try {
		image = new Image(display, bounds);
		image.dispose();
		fail("No exception thrown for width <= 0");
	} catch (IllegalArgumentException e) {
	}

	bounds = new Rectangle(0, 0, 10, -1);
	try {
		image = new Image(display, bounds);
		image.dispose();
		fail("No exception thrown for height <= 0");
	} catch (IllegalArgumentException e) {
	}

	// valid images
	bounds = new Rectangle(0, 0, 10, 10);
	image = new Image(null, bounds);
	image.dispose();
	
	image = new Image(display, bounds);
	image.dispose();
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageData() {
	ImageData data = null;
	Image image = null;
	
	try {
		image = new Image(display, data);
		image.dispose();
		fail("No exception thrown for ImageData == null");
	} catch (IllegalArgumentException e) {
	}

//	Platform-specific test.  
//	data = new ImageData(10, 10, 1, new PaletteData(0xff0000, 0x00ff00, 0x0000ff));
//	try {
//		image = new Image(display, data);
//		image.dispose();
//		fail("Unsupported color depth");
//	} catch (SWTException e) {
//	}

	data = new ImageData(10, 10, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	image = new Image(null, data);
	image.dispose();

	data = new ImageData(10, 10, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	image = new Image(display, data);
	image.dispose();
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageData() {
	ImageData data = null;
	ImageData data1 = new ImageData(10, 10, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	Image image = null;
	
	try {
		image = new Image(display, data, data1);
		image.dispose();
		fail("No exception thrown for ImageData source == null");
	} catch (IllegalArgumentException e) {
	}

	data = new ImageData(10, 10, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	data1 = null;
	try {
		image = new Image(display, data, data1);
		image.dispose();
		fail("No exception thrown for ImageData mask == null");
	} catch (IllegalArgumentException e) {
	}

	data = new ImageData(10, 10, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	data1 = new ImageData(1, 10, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	try {
		image = new Image(display, data, data1);
		image.dispose();
		fail("No exception thrown for ImageData source width != ImageData mask width");
	} catch (IllegalArgumentException e) {
	}

	data = new ImageData(10, 1, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	data1 = new ImageData(10, 10, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	try {
		image = new Image(display, data, data1);
		image.dispose();
		fail("No exception thrown for ImageData source height != ImageData mask height");
	} catch (IllegalArgumentException e) {
	}

	data = new ImageData(10, 10, 8, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	data1 = new ImageData(10, 10, 8, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	try {
		image = new Image(display, data, data1);
		image.dispose();
		fail("No exception thrown for ImageData mask color depth != 1");
	} catch (IllegalArgumentException e) {
	}

	// This test isn't finished yet, don't remove until it is!  Should test mask support.
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageData not written");
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_io_InputStream() {
	InputStream stream = null;
	Image image = null;
	try {
		try {
			image = new Image(display, stream);
			image.dispose();
			fail("No exception thrown for InputStream == null");
		} catch (IllegalArgumentException e) {
		}
		
		stream = SwtTestCase.class.getResourceAsStream("empty.txt");
		try {
			image = new Image(display, stream);
			image.dispose();
			try {
				stream.close();
			} catch (IOException e) {}
			fail("No exception thrown for invalid InputStream");
		} catch (SWTException e) {
		}
	
		// create valid images
		int numFormats = SwtTestCase.imageFormats.length;
		String fileName = SwtTestCase.imageFilenames[0];
		Display[] displays = {display, null};
		for (int j = 0; j < displays.length; j++) {
			Display tempDisplay = displays[j];
			for (int i=0; i<numFormats; i++) {
				String format = SwtTestCase.imageFormats[i];
				stream = SwtTestCase.class.getResourceAsStream(fileName + "." + format);
				image = new Image(tempDisplay, stream);
				image.dispose();
				try {
					stream.close();
				} catch (IOException e) {}
			}
		}
	} finally {
		try {
			stream.close();
		} catch (Exception e) {
		}
	}
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_String() {
	String filename = null;
	try {
		Image image = new Image(display, filename);
		image.dispose();
		fail("No exception thrown for filename == null");
	} catch (IllegalArgumentException e) {
	}
	// j2se and j2me(cdc) can load from a filename but, j2me(cldc) throws an exception
}

public void test_dispose() {
	// tested in isDisposed() method
}

public void test_equalsLjava_lang_Object() {
	Image image = null;
	Image image1 = null;;

	try {
		image = new Image(display, 10, 10);
		image1 = image;
	
		assertFalse(":a:", image.equals(null));
		
		assertTrue(":b:", image.equals(image1));
		
		ImageData imageData = new ImageData(10, 10, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
		image.dispose();
		image = new Image(display, imageData);
		image1 = new Image(display, imageData);
		assertFalse(":c:", image.equals(image1));
	} finally {
		image.dispose();
		image1.dispose();
	}
}

public void test_getBackground() {
	Image image = new Image(display, 10, 10);
	image.dispose();
	try {
		image.getBackground();
		fail("No exception thrown for disposed image");
	} catch (SWTException e) {
	}
	// remainder tested in setBackground method
}

public void test_getBounds() {
	Rectangle bounds = new Rectangle(0, 0, 10, 20);
	Image image = new Image(display, bounds.width, bounds.height);
	image.dispose();
	try {
		image.getBounds();
		fail("No exception thrown for disposed image");
	} catch (SWTException e) {
		image.dispose();
	}
		
	// creates bitmap image
	image = new Image(display, bounds.width, bounds.height);
	Rectangle bounds1 = image.getBounds();
	image.dispose();
	assertEquals(":a:", bounds, bounds1);
	
	// create icon image
	ImageData imageData = new ImageData(bounds.width, bounds.height, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	image = new Image(display, imageData);
	bounds1 = image.getBounds();
	image.dispose();
	assertEquals(":b:", bounds, bounds1);
}

public void test_getImageData() {	
	getImageData1();
	getImageData2(24, new PaletteData(0xff0000, 0xff00, 0xff));		
	getImageData2(32, new PaletteData(0xff0000, 0xff00, 0xff));
}

public void test_hashCode() {
	Image image = null;
	Image image1 = null;;

	try {
		image = new Image(display, 10, 10);
		image1 = image;
	
		assertEquals(":a:", image1.hashCode(), image.hashCode());
		
		ImageData imageData = new ImageData(10, 10, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
		image.dispose();
		image = new Image(display, imageData);
		image1 = new Image(display, imageData);
		boolean equals = (image1.hashCode() == image.hashCode());
		assertFalse(":b:", equals);
	} finally {
		image.dispose();
		image1.dispose();
	}
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
	assertFalse(":a:", image.isDisposed());
	image.dispose();
	assertTrue(":b:", image.isDisposed());
}

public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	Image image = new Image(display, 10, 10);

	try {
		image.setBackground(null);
		fail("No exception thrown for color == null");
	} catch (IllegalArgumentException e) {
	} finally {
		image.dispose();
	}

	image = new Image(display, 10, 10);
	Color color = new Color(display, 255, 255, 255);
	color.dispose();
	try {
		image.setBackground(color);
		fail("No exception thrown for disposed color");
	} catch (IllegalArgumentException e) {
	} finally {
		image.dispose();
	}

	image = new Image(display, 10, 10);
	image.dispose();
	color = new Color(display, 255, 255, 255);
	try {
		image.setBackground(color);
		fail("No exception thrown for disposed image");
	} catch (SWTException e) {
	} finally {
		color.dispose();
	}
	
	// this image does not have a transparent pixel by default so setBackground has no effect
	image = new Image(display, 10, 10);
	image.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
	color = image.getBackground();
	assertNull("background color should be null for non-transparent image", color);
	image.dispose();
	
	// create an image with transparency and then set the background color
	ImageData imageData = new ImageData(10, 10, 2, new PaletteData(new RGB[] {new RGB(0, 0, 0), new RGB(255, 255, 255), new RGB(50, 100, 150)}));
	imageData.transparentPixel = 0; // transparent pixel is currently black
	image = new Image(display, imageData);
	image.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
	color = image.getBackground();
	assertEquals("background color should have been set to green", display.getSystemColor(SWT.COLOR_GREEN), color);
	image.dispose();
}

public void test_toString() {
	Image image = new Image(display, 10, 10);
	try {
		assertNotNull(image.toString());
		assertTrue(image.toString().length() > 0);
	} finally {
		image.dispose();
	}
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
	methodNames.addElement("test_toString");
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
	else if (getName().equals("test_toString")) test_toString();
	else if (getName().equals("test_win32_newLorg_eclipse_swt_graphics_DeviceII")) test_win32_newLorg_eclipse_swt_graphics_DeviceII();
}
/* custom */
Display display;

/** Test implementation **/

void getImageData1() {
	int numFormats = SwtTestCase.imageFormats.length;
	String fileName = SwtTestCase.imageFilenames[0];
	for (int i=0; i<numFormats; i++) {
		String format = SwtTestCase.imageFormats[i];
		ImageLoader loader = new ImageLoader();
		InputStream stream = SwtTestCase.class.getResourceAsStream(fileName + "." + format);
		ImageData data1 = loader.load(stream)[0];
		Image image = new Image(display, data1);
		ImageData data2 = image.getImageData();
		image.dispose();
		assertEquals("Image width should be the same", data1.width, data2.width);
		assertEquals("Image height should be the same", data1.height, data2.height);
		try {
			stream.close();
		} catch (IOException e) {
			// continue;
		}
	}
}

/*
 * Verify Image.getImageData returns pixels with the same RGB value as the
 * source image. This test only makes sense with depth of 24 and 32 bits.
 */
void getImageData2(int depth, PaletteData palette) {
	int width = 10;
	int height = 10;
	Color color = new Color(display, 0, 0xff, 0);
	RGB colorRGB = color.getRGB();

	ImageData imageData = new ImageData(width, height, depth, palette);
	Image image = new Image(display, imageData);
		
	GC gc = new GC(image);
	gc.setBackground(color);
	gc.setForeground(color);
	gc.fillRectangle(0, 0, 10, 10);
		
	ImageData newData = image.getImageData();
	PaletteData newPalette = newData.palette;
	for (int i = 0; i < width; i++) {
		for (int j = 0; j < height; j++) {
			int pixel = newData.getPixel(i, j);
			RGB rgb = newPalette.getRGB(pixel);
			assertTrue("rgb.equals(colorRGB)", rgb.equals(colorRGB));
		}
	}
	color.dispose();
	gc.dispose();
	image.dispose();
}
}
