/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.ImageData
 *
 * @see org.eclipse.swt.graphics.ImageData
 */
public class Test_org_eclipse_swt_graphics_ImageData extends SwtTestCase {

public Test_org_eclipse_swt_graphics_ImageData(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 32, new PaletteData(0xFF0000, 0xFF00, 0xFF));
}

public void test_ConstructorIIILorg_eclipse_swt_graphics_PaletteData() {
	try {
		new ImageData(-1, 1, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
		fail("No exception thrown for width < 0");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(1, -1, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
		fail("No exception thrown for height < 0");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(1, 1, 1, null, 0, new byte[] {0, 0x4f, 0x4f, 0});
		fail("No exception thrown for paletteData == null");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(1, 1, 3, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
		fail("No exception thrown for unsupported depth");
	} catch (IllegalArgumentException e) {
	}
	
	int[] validDepths = {1, 2, 4, 8, 16, 24, 32};
	for (int i = 0; i < validDepths.length; i++) {
		new ImageData(1, 1, validDepths[i], new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	}
}

public void test_ConstructorIIILorg_eclipse_swt_graphics_PaletteDataI$B() {
	byte[] validData = new byte[] {0, 0x4f, 0x4f, 0};
	
	try {
		new ImageData(-1, 1, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 1, validData);
		fail("No exception thrown for width < 0");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(1, -1, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 1, validData);
		fail("No exception thrown for height < 0");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(1, 1, 1, null, 0, validData);
		fail("No exception thrown for paletteData == null");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(1, 1, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 1, null);
		fail("No exception thrown for data == null");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(1, 1, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 1, new byte[] {});
		fail("No exception thrown for data array too small");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(1, 1, 16, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 1, new byte[] {0x4f});
		fail("No exception thrown for data array too small");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(1, 1, 32, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 1, new byte[] {0x4f, 0x4f});
		fail("No exception thrown for data array too small");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(2, 2, 8, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 1, new byte[] {0x4f, 0x4f, 0x4f});
		fail("No exception thrown for data array too small");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(1, 1, 3, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 1, validData);
		fail("No exception thrown for unsupported depth");
	} catch (IllegalArgumentException e) {
	}
	
	// verify all valid depths
	int[] validDepths = {1, 2, 4, 8, 16, 24, 32};
	for (int i = 0; i < validDepths.length; i++) {
		new ImageData(1, 1, validDepths[i], new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 1, validData);
	}
	
	// verify no divide by zero exception if scanlinePad == 0
	try {
		new ImageData(1, 1, 8, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 0, validData);
		fail("No exception thrown for scanlinePad == 0");
	} catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLjava_io_InputStream() {
	InputStream stream = null;
	try {
		try {
			new ImageData(stream);
			fail("No exception thrown for InputStream == null");
		} catch (IllegalArgumentException e) {
		}
		
		stream = SwtTestCase.class.getResourceAsStream("empty.txt");
		try {
			new ImageData(stream);
			fail("No exception thrown for invalid InputStream");
		} catch (SWTException e) {
		}
	
		int numFormats = SwtTestCase.imageFormats.length;
		String fileName = SwtTestCase.imageFilenames[0];
		for (int i=0; i<numFormats; i++) {
			String format = SwtTestCase.imageFormats[i];
			stream = SwtTestCase.class.getResourceAsStream(fileName + "." + format);
			new ImageData(stream);
		}
	} finally {
		try {
			stream.close();
		} catch (Exception e) {
		}
	}
}

public void test_ConstructorLjava_lang_String() {
	String filename = null;
	try {
		new ImageData(filename);
		fail("No exception thrown for filename == null");
	} catch (IllegalArgumentException e) {
	}
	// j2se and j2me(cdc) can load from a filename but, j2me(cldc) throws an exception
}

public void test_clone() {
	InputStream stream = null;
	try {
		stream = SwtTestCase.class.getResourceAsStream(SwtTestCase.imageFilenames[0] + "." + SwtTestCase.imageFormats[0]);
		ImageData data1 = new ImageData(stream);
		ImageData data2 = (ImageData) data1.clone();
		// imageData does not implement an equals(Object) method
		assertEquals(":a:", data1.alpha, data2.alpha);
		assertEquals(":b:", data1.alphaData, data2.alphaData);
		assertEquals(":c:", data1.bytesPerLine, data2.bytesPerLine);
		assertEquals(":d:", data1.data, data2.data);
		assertEquals(":e:", data1.delayTime, data2.delayTime);
		assertEquals(":f:", data1.depth, data2.depth);
		assertEquals(":g:", data1.disposalMethod, data2.disposalMethod);
		assertEquals(":h:", data1.height, data2.height);
		assertEquals(":i:", data1.maskData, data2.maskData);
		assertEquals(":j:", data1.maskPad, data2.maskPad);
		assertEquals(":k:", data1.palette, data2.palette);
		assertEquals(":l:", data1.scanlinePad, data2.scanlinePad);
		assertEquals(":m:", data1.transparentPixel, data2.transparentPixel);
		assertEquals(":n:", data1.type, data2.type);
		assertEquals(":o:", data1.width, data2.width);
		assertEquals(":p:", data1.x, data2.x);
		assertEquals(":q:", data1.y, data2.y);
	} finally {
		try {
			stream.close();
		} catch (Exception e) {
		}
	}
}

public void test_getAlphaII() {
	int value;
	
	assertEquals(":a:", 255, imageData.getAlpha(0, 0));
	value = 0xAA;
	imageData.setAlpha(0, 0, value);
	assertEquals(":b:", value, imageData.getAlpha(0, 0));

	// exception cases
	try {
		imageData.getAlpha(-1, 1);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getAlpha(IMAGE_DIMENSION, 1);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getAlpha(0, -1);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getAlpha(0, IMAGE_DIMENSION);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
}

public void test_getAlphasIII$BI() {
	byte value;
	final int SIZE = 20; 
	final int GET_WIDTH = 10;
	final int OFFSET = 10;
	byte[] alphaData = new byte[SIZE];
	
	imageData.getAlphas(0, 1, GET_WIDTH, alphaData, OFFSET);
	for (int i = 0; i < alphaData.length; i ++) {
		if (i < OFFSET) {
			assertEquals(":a:", 0, alphaData[i]);
		} else {
			assertEquals(":b:", (byte) 255, alphaData[i]);
		}
	}

	value = (byte) 0xAA;
	byte[] values = new byte[] {value, (byte) (value+1), (byte) (value+2), (byte) (value+3), (byte) (value+4)};
	imageData.setAlphas(0, 1, values.length, values, 0);
	imageData.getAlphas(0, 1, GET_WIDTH, alphaData, OFFSET);
	for (int i = 0; i < alphaData.length; i++) {
		if (i < OFFSET) {
			assertEquals(":c:", 0, alphaData[i]);
		} else if (i < OFFSET + values.length) {
			assertEquals(":d:", (byte) values[i-OFFSET], alphaData[i]);	
		} else if (i < OFFSET+GET_WIDTH) {
			assertEquals(":e:", 0, alphaData[i]);
		}
	}
	
	// exception cases
	try {
		imageData.getAlphas(0, 1, GET_WIDTH*GET_WIDTH, alphaData, OFFSET);
		fail("No exception thrown for getWidth out of bounds");
	} catch (IndexOutOfBoundsException e) {
	}
	try {
		imageData.getAlphas(0, 1, GET_WIDTH, (byte[]) null, OFFSET);
		fail("No exception thrown for alphas == null");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for alphas == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
	try {
		imageData.getAlphas(-1, 1, GET_WIDTH, alphaData, OFFSET);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getAlphas(IMAGE_DIMENSION, 1, GET_WIDTH, alphaData, OFFSET);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getAlphas(0, -1, GET_WIDTH, alphaData, OFFSET);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getAlphas(0, IMAGE_DIMENSION, GET_WIDTH, alphaData, OFFSET);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getAlphas(0, 1, -1, alphaData, OFFSET);
		fail("No exception thrown for getWidth < 0");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for getWidth < 0", SWT.ERROR_INVALID_ARGUMENT, e);
	}	
}

public void test_getPixelII() {
	int value;
	
	assertEquals(":a:", 0, imageData.getPixel(0, 0));
	value = 0xAA;
	imageData.setPixel(0, 0, value);
	assertEquals(":b:", value, imageData.getPixel(0, 0));

	// exception cases
	try {
		imageData.getPixel(-1, 1);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getPixel(IMAGE_DIMENSION, 1);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getPixel(0, -1);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getPixel(0, IMAGE_DIMENSION);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	int width = 3;
	int height = 3;
	int depth = 4;
	byte pixelValue = 1;
	byte[] data = {(byte) ((pixelValue << 4) + pixelValue), (byte) (pixelValue << 4), (byte) ((pixelValue << 4) + pixelValue), (byte) (pixelValue << 4), (byte) ((pixelValue << 4) + pixelValue), (byte) (pixelValue << 4)}; 
	imageData = new ImageData(width, height, depth, new PaletteData(new RGB[] {new RGB(0, 0, 255), new RGB(111, 111, 111)}), 1, data);
	for (int y = 0; y < height; y++) {
		for (int x = 0; x < width; x++) {
			int pixel = imageData.getPixel(x, y);
			assertEquals("Bad pixel data", pixelValue, pixel);
		}
	}
}

public void test_getPixelsIII$BI() {
	final int SIZE = 20; 
	final int GET_WIDTH = 10;
	final int OFFSET = 10;
	byte[] pixelData = new byte[SIZE];

	// test 1 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 1, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i ++) {
		assertEquals(":a:", 0, pixelData[i]);
	}

	byte[] values = new byte[]{0x1, 0x1, 0x1, 0x1, 0x1};
	imageData.setPixels(0, 1, values.length, values, 0);
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i++) {
		if (i < OFFSET) {
			assertEquals(":b:", 0, pixelData[i]);
		} else if (i < OFFSET + values.length) {
			assertEquals(":c:", (byte) values[i-OFFSET], pixelData[i]);
		} else if (i < OFFSET+GET_WIDTH) {
			assertEquals(":d:", 0, pixelData[i]);
		}
	}

	// test 2 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 2, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i ++) {
		assertEquals(":e:", 0, pixelData[i]);
	}

	values = new byte[]{0x1, 0x2, 0x3, 0x2, 0x1};
	imageData.setPixels(0, 1, values.length, values, 0);
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i++) {
		if (i < OFFSET) {
			assertEquals(":f:", 0, pixelData[i]);
		} else if (i < OFFSET + values.length) {
			assertEquals(":g:", (byte) values[i-OFFSET], pixelData[i]);	
		} else if (i < OFFSET+GET_WIDTH) {
			assertEquals(":h:", 0, pixelData[i]);
		}
	}

	// test 4 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 4, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i ++) {
		assertEquals(":i:", 0, pixelData[i]);
	}

	values = new byte[]{0x1, 0x2, 0x3, 0x4, 0xF};
	imageData.setPixels(0, 1, values.length, values, 0);
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i++) {
		if (i < OFFSET) {
			assertEquals(":j:", 0, pixelData[i]);
		} else if (i < OFFSET + values.length) {
			assertEquals(":k:", (byte) values[i-OFFSET], pixelData[i]);	
		} else if (i < OFFSET+GET_WIDTH) {
			assertEquals(":l:", 0, pixelData[i]);
		}
	}

	// test 8 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 8, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i ++) {
		assertEquals(":m:", 0, pixelData[i]);
	}

	values = new byte[]{0x1, 0x2, 0x3, 0xF, (byte)0xFF};
	imageData.setPixels(0, 1, values.length, values, 0);
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i++) {
		if (i < OFFSET) {
			assertEquals(":n:", 0, pixelData[i]);
		} else if (i < OFFSET + values.length) {
			assertEquals(":o:", (byte) values[i-OFFSET], pixelData[i]);	
		} else if (i < OFFSET+GET_WIDTH) {
			assertEquals(":p:", 0, pixelData[i]);
		}
	}
	
	// exception cases
	try {
		imageData.getPixels(0, 1, GET_WIDTH*GET_WIDTH, pixelData, OFFSET);
		fail("No exception thrown for getWidth out of bounds");
	} catch (IndexOutOfBoundsException e) {
	}
	try {
		imageData.getPixels(0, 1, GET_WIDTH, (byte[]) null, OFFSET);
		fail("No exception thrown for pixels == null");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for pixels == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
	try {
		imageData.getPixels(-1, 1, GET_WIDTH, pixelData, OFFSET);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getPixels(IMAGE_DIMENSION, 1, GET_WIDTH, pixelData, OFFSET);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getPixels(0, -1, GET_WIDTH, pixelData, OFFSET);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getPixels(0, IMAGE_DIMENSION, GET_WIDTH, pixelData, OFFSET);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getPixels(0, 1, -1, pixelData, OFFSET);
		fail("No exception thrown for getWidth < 0");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for getWidth < 0", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 32, new PaletteData(0xFF0000, 0xFF00, 0xFF));	
	try {
		imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
		fail("No exception thrown for invalid depth");
	} catch (SWTException e) {
		assertEquals("Incorrect exception thrown for invalid depth", SWT.ERROR_UNSUPPORTED_DEPTH, e);
	}
}

public void test_getPixelsIII$II() {
	final int SIZE = 20; 
	final int GET_WIDTH = 10;
	final int OFFSET = 10;
	int[] pixelData = new int[SIZE];

	// test 1 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 1, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i ++) {
		assertEquals(":a:", 0, pixelData[i]);
	}

	int[] values = new int[]{0x1, 0x1, 0x1, 0x1, 0x1};
	imageData.setPixels(0, 1, values.length, values, 0);
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i++) {
		if (i < OFFSET) {
			assertEquals(":b:", 0, pixelData[i]);
		} else if (i < OFFSET + values.length) {
			assertEquals(":c:", values[i-OFFSET], pixelData[i]);
		} else if (i < OFFSET+GET_WIDTH) {
			assertEquals(":d:", 0, pixelData[i]);
		}
	}

	// test 2 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 2, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i ++) {
		assertEquals(":e:", 0, pixelData[i]);
	}

	values = new int[]{0x1, 0x2, 0x3, 0x2, 0x1};
	imageData.setPixels(0, 1, values.length, values, 0);
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i++) {
		if (i < OFFSET) {
			assertEquals(":f:", 0, pixelData[i]);
		} else if (i < OFFSET + values.length) {
			assertEquals(":g:", values[i-OFFSET], pixelData[i]);	
		} else if (i < OFFSET+GET_WIDTH) {
			assertEquals(":h:", 0, pixelData[i]);
		}
	}

	// test 4 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 4, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i ++) {
		assertEquals(":i:", 0, pixelData[i]);
	}

	values = new int[]{0x1, 0x2, 0x3, 0x4, 0xF};
	imageData.setPixels(0, 1, values.length, values, 0);
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i++) {
		if (i < OFFSET) {
			assertEquals(":j:", 0, pixelData[i]);
		} else if (i < OFFSET + values.length) {
			assertEquals(":k:", values[i-OFFSET], pixelData[i]);	
		} else if (i < OFFSET+GET_WIDTH) {
			assertEquals(":l:", 0, pixelData[i]);
		}
	}

	// test 8 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 8, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i ++) {
		assertEquals(":m:", 0, pixelData[i]);
	}

	values = new int[]{0x1, 0x2, 0x3, 0xF, 0xFF};
	imageData.setPixels(0, 1, values.length, values, 0);
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i++) {
		if (i < OFFSET) {
			assertEquals(":n:", 0, pixelData[i]);
		} else if (i < OFFSET + values.length) {
			assertEquals(":o:", values[i-OFFSET], pixelData[i]);	
		} else if (i < OFFSET+GET_WIDTH) {
			assertEquals(":p:", 0, pixelData[i]);
		}
	}

	// test 16 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 16, new PaletteData(0xF800, 0x7E0, 0x1F));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i ++) {
		assertEquals(":q:", 0, pixelData[i]);
	}

	values = new int[]{0, 0x2, 0xF, 0xFF, 0xFFAA};
	imageData.setPixels(0, 1, values.length, values, 0);
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i++) {
		if (i < OFFSET) {
			assertEquals(":r:", 0, pixelData[i]);
		} else if (i < OFFSET + values.length) {
			assertEquals(":s:", values[i-OFFSET], pixelData[i]);	
		} else if (i < OFFSET+GET_WIDTH) {
			assertEquals(":t:", 0, pixelData[i]);
		}
	}

	// test 24 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 24, new PaletteData(0xFF0000, 0xFF00, 0xFF));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i ++) {
		assertEquals(":u:", 0, pixelData[i]);
	}

	values = new int[]{0, 0xFF, 0xFFAA, 0xFF00AA};
	imageData.setPixels(0, 1, values.length, values, 0);
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i++) {
		if (i < OFFSET) {
			assertEquals(":v:", 0, pixelData[i]);
		} else if (i < OFFSET + values.length) {
			assertEquals(":w:", values[i-OFFSET], pixelData[i]);	
		} else if (i < OFFSET+GET_WIDTH) {
			assertEquals(":x:", 0, pixelData[i]);
		}
	}

	// test 32 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 32, new PaletteData(0xFF000000, 0xFF00, 0xFF));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i ++) {
		assertEquals(":y:", 0, pixelData[i]);
	}

	values = new int[]{0, 0xFF, 0xFFAA, 0xFF00AA00};
	imageData.setPixels(0, 1, values.length, values, 0);
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int i = 0; i < pixelData.length; i++) {
		if (i < OFFSET) {
			assertEquals(":z:", 0, pixelData[i]);
		} else if (i < OFFSET + values.length) {
			assertEquals(":aa:", values[i-OFFSET], pixelData[i]);	
		} else if (i < OFFSET+GET_WIDTH) {
			assertEquals(":ab:", 0, pixelData[i]);
		}
	}

	// exception cases
	try {
		imageData.getPixels(0, 1, GET_WIDTH*GET_WIDTH, pixelData, OFFSET);
		fail("No exception thrown for getWidth out of bounds");
	} catch (IndexOutOfBoundsException e) {
	}
	try {
		imageData.getPixels(0, 1, GET_WIDTH, (int[]) null, OFFSET);
		fail("No exception thrown for pixels == null");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for pixels == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
	try {
		imageData.getPixels(-1, 1, GET_WIDTH, pixelData, OFFSET);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getPixels(IMAGE_DIMENSION, 1, GET_WIDTH, pixelData, OFFSET);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getPixels(0, -1, GET_WIDTH, pixelData, OFFSET);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getPixels(0, IMAGE_DIMENSION, GET_WIDTH, pixelData, OFFSET);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.getPixels(0, 1, -1, pixelData, OFFSET);
		fail("No exception thrown for getWidth < 0");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for getWidth < 0", SWT.ERROR_INVALID_ARGUMENT, e);
	}
}

public void test_getRGBs() {
	assertNull(":a:", imageData.getRGBs());
	RGB[] rgbs = new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)};
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 8, new PaletteData(rgbs));
	assertEquals(":b:", rgbs, imageData.getRGBs());
}

public void test_getTransparencyMask() {
//	Bug 71472 - transparency mask should be null	
//	assertNull(":a:", imageData.getTransparencyMask());

	InputStream stream = getClass().getResourceAsStream(transparentImageFilenames[0]);
	Image image = new Image(Display.getDefault(), stream);
	try {
		stream.close();
	} catch (IOException e) {}
	imageData = image.getImageData();
	ImageData maskData = imageData.getTransparencyMask();
	assertNotNull(":b:", maskData);

//	Bug 71472 - transparency mask should be null	
/*	image = new Image(Display.getDefault(), getClass().getResourceAsStream(imageFilenames[0] + '.' + imageFormats[imageFormats.length-1]));
	imageData = image.getImageData();
	maskData = imageData.getTransparencyMask();
	assertNull(":c:", maskData);
*/	
}

public void test_getTransparencyType() {
	assertEquals(":a:", SWT.TRANSPARENCY_NONE, imageData.getTransparencyType());

	InputStream stream = getClass().getResourceAsStream(transparentImageFilenames[0]);
	Image image = new Image(Display.getDefault(), stream);
	try {
		stream.close();
	} catch (IOException e) {}
	imageData = image.getImageData();
	assertFalse(":b:", SWT.TRANSPARENCY_NONE == imageData.getTransparencyType());
	
	stream = getClass().getResourceAsStream(imageFilenames[0] + '.' + imageFormats[imageFormats.length-1]);
	image = new Image(Display.getDefault(), stream);
	try {
		stream.close();
	} catch (IOException e) {}
	imageData = image.getImageData();
	assertEquals(":c:", SWT.TRANSPARENCY_NONE, imageData.getTransparencyType());
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
	final int imageDimension = 8;
	RGB[] rgbs = new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)};
	byte[] pixelData = new byte[(imageDimension*imageDimension) / 8];
	
	pixelData[0] = 0x4F;
	imageData = new ImageData(imageDimension, imageDimension, 1, new PaletteData(rgbs), 1, pixelData);

	ImageData scaledImageData = imageData.scaledTo(-imageDimension, -imageDimension);
	byte[] scaledPixelData = new byte[imageDimension];
	scaledImageData.getPixels(0, imageDimension - 1, scaledPixelData.length, scaledPixelData, 0);	
	byte[] expectedPixelData = new byte[] {0x1, 0x1, 0x1, 0x1, 0, 0, 0x1, 0};
	assertEquals(":a:", expectedPixelData, scaledPixelData);

	scaledImageData = imageData.scaledTo(imageDimension * 10, imageDimension);
	scaledPixelData = new byte[imageDimension * 10];
	scaledImageData.getPixels(0, 0, scaledPixelData.length, scaledPixelData, 0);	
	assertEquals(":b:", 0, scaledPixelData[0]);
	assertEquals(":c:", 0, scaledPixelData[1]);

	scaledImageData = imageData.scaledTo(imageDimension, imageDimension * 10);
	scaledPixelData = new byte[imageDimension];
	scaledImageData.getPixels(0, 0, scaledPixelData.length, scaledPixelData, 0);	
	expectedPixelData = new byte[] {0, 0x1, 0, 0, 0x1, 0x1, 0x1, 0x1};
	assertEquals(":d:", expectedPixelData, scaledPixelData);
}

public void test_setAlphaIII() {
	int value;
	
	value = 0xAA;
	imageData.setAlpha(0, 0, value);
	assertEquals(":a:", value, imageData.getAlpha(0, 0));

	// exception cases
	try {
		imageData.setAlpha(-1, 1, value);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setAlpha(IMAGE_DIMENSION, 1, value);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setAlpha(0, -1, value);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setAlpha(0, IMAGE_DIMENSION, value);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
}

public void test_setAlphasIII$BI() {
	byte value;
	final int SIZE = 20; 
	final int OFFSET = 1;
	byte[] alphaData = new byte[SIZE];
	
	value = (byte) 0xAA;
	byte[] values = new byte[] {value, (byte) (value+1), (byte) (value+2), (byte) (value+3), (byte) (value+4)};
	imageData.setAlphas(0, 1, values.length - OFFSET, values, OFFSET);
	imageData.getAlphas(0, 1, IMAGE_DIMENSION, alphaData, 0);
	for (int i = 0; i < alphaData.length; i++) {
		if (i + OFFSET < values.length) {
			assertEquals(":a:", (byte) values[i + OFFSET], alphaData[i]);
		} else {
			assertEquals(":b:", 0, alphaData[i]);
		}
	}
	
	// exception cases
	try {
		imageData.setAlphas(0, 1, IMAGE_DIMENSION*IMAGE_DIMENSION, alphaData, OFFSET);
		fail("No exception thrown for putWidth out of bounds");
	} catch (IndexOutOfBoundsException e) {
	}
	try {
		imageData.setAlphas(0, 1, IMAGE_DIMENSION, (byte[]) null, OFFSET);
		fail("No exception thrown for alphas == null");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for alphas == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
	try {
		imageData.setAlphas(-1, 1, IMAGE_DIMENSION, alphaData, OFFSET);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setAlphas(IMAGE_DIMENSION, 1, IMAGE_DIMENSION, alphaData, OFFSET);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setAlphas(0, -1, IMAGE_DIMENSION, alphaData, OFFSET);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setAlphas(0, IMAGE_DIMENSION, IMAGE_DIMENSION, alphaData, OFFSET);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setAlphas(0, 1, -1, alphaData, OFFSET);
		fail("No exception thrown for putWidth < 0");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for putWidth < 0", SWT.ERROR_INVALID_ARGUMENT, e);
	}	
}

public void test_setPixelIII() {
	int value;
	
	value = 0xAA;
	imageData.setPixel(0, 0, value);
	assertEquals(":a:", value, imageData.getPixel(0, 0));

	// exception cases
	try {
		imageData.setPixel(-1, 1, value);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setPixel(IMAGE_DIMENSION, 1, value);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setPixel(0, -1, value);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setPixel(0, IMAGE_DIMENSION, value);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
}

public void test_setPixelsIII$BI() {
	final int SIZE = 20; 
	final int OFFSET = 1;
	byte[] pixelData = new byte[SIZE];

	// test 1 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 1, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
	byte[] values = new byte[]{0x1, 0x1, 0x1, 0x1, 0x1};
	imageData.setPixels(0, 1, values.length - OFFSET, values, OFFSET);
	imageData.getPixels(0, 1, IMAGE_DIMENSION, pixelData, 0);
	for (int i = 0; i < pixelData.length; i++) {
		if (i + OFFSET < values.length) {
			assertEquals(":a:", (byte) values[i + OFFSET], pixelData[i]);
		} else {
			assertEquals(":b:", 0, pixelData[i]);
		}
	}

	// test 2 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 2, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
	values = new byte[]{0x1, 0x2, 0x3, 0x2, 0x1};
	imageData.setPixels(0, 1, values.length - OFFSET, values, OFFSET);
	imageData.getPixels(0, 1, IMAGE_DIMENSION, pixelData, 0);
	for (int i = 0; i < pixelData.length; i++) {
		if (i + OFFSET < values.length) {
			assertEquals(":c:", (byte) values[i + OFFSET], pixelData[i]);
		} else {
			assertEquals(":d:", 0, pixelData[i]);
		}
	}

	// test 4 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 4, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
	values = new byte[]{0x1, 0x2, 0x3, 0x4, 0xF};
	imageData.setPixels(0, 1, values.length - OFFSET, values, OFFSET);
	imageData.getPixels(0, 1, IMAGE_DIMENSION, pixelData, 0);
	for (int i = 0; i < pixelData.length; i++) {
		if (i + OFFSET < values.length) {
			assertEquals(":e:", (byte) values[i + OFFSET], pixelData[i]);
		} else {
			assertEquals(":f:", 0, pixelData[i]);
		}
	}

	// test 8 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 8, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
	values = new byte[]{0x1, 0x2, 0x3, 0xF, (byte)0xFF};
	imageData.setPixels(0, 1, values.length - OFFSET, values, OFFSET);
	imageData.getPixels(0, 1, IMAGE_DIMENSION, pixelData, 0);
	for (int i = 0; i < pixelData.length; i++) {
		if (i + OFFSET < values.length) {
			assertEquals(":g:", (byte) values[i + OFFSET], pixelData[i]);
		} else {
			assertEquals(":h:", 0, pixelData[i]);
		}
	}
	
	// exception cases
	try {
		imageData.setPixels(0, 1, IMAGE_DIMENSION*IMAGE_DIMENSION, pixelData, OFFSET);
		fail("No exception thrown for putWidth out of bounds");
	} catch (IndexOutOfBoundsException e) {
	}
	try {
		imageData.setPixels(0, 1, IMAGE_DIMENSION, (byte[]) null, OFFSET);
		fail("No exception thrown for pixels == null");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for pixels == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
	try {
		imageData.setPixels(-1, 1, IMAGE_DIMENSION, pixelData, OFFSET);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setPixels(IMAGE_DIMENSION, 1, IMAGE_DIMENSION, pixelData, OFFSET);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setPixels(0, -1, IMAGE_DIMENSION, pixelData, OFFSET);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setPixels(0, IMAGE_DIMENSION, IMAGE_DIMENSION, pixelData, OFFSET);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setPixels(0, 1, -1, pixelData, OFFSET);
		fail("No exception thrown for putWidth < 0");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for putWidth < 0", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 32, new PaletteData(0xFF0000, 0xFF00, 0xFF));	
	try {
		imageData.setPixels(0, 1, IMAGE_DIMENSION, pixelData, OFFSET);
		fail("No exception thrown for invalid depth");
	} catch (SWTException e) {
		assertEquals("Incorrect exception thrown for invalid depth", SWT.ERROR_UNSUPPORTED_DEPTH, e);
	}
}

public void test_setPixelsIII$II() {
	final int SIZE = 20; 
	final int OFFSET = 1;
	int[] pixelData = new int[SIZE];

	// test 1 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 1, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
	int[] values = new int[]{0x1, 0x1, 0x1, 0x1, 0x1};
	imageData.setPixels(0, 1, values.length - OFFSET, values, OFFSET);
	imageData.getPixels(0, 1, IMAGE_DIMENSION, pixelData, 0);
	for (int i = 0; i < pixelData.length; i++) {
		if (i + OFFSET < values.length) {
			assertEquals(":a:", values[i + OFFSET], pixelData[i]);
		} else {
			assertEquals(":b:", 0, pixelData[i]);
		}
	}

	// test 2 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 2, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
	values = new int[]{0x1, 0x2, 0x3, 0x2, 0x1};
	imageData.setPixels(0, 1, values.length - OFFSET, values, OFFSET);
	imageData.getPixels(0, 1, IMAGE_DIMENSION, pixelData, 0);
	for (int i = 0; i < pixelData.length; i++) {
		if (i + OFFSET < values.length) {
			assertEquals(":c:", values[i + OFFSET], pixelData[i]);
		} else {
			assertEquals(":d:", 0, pixelData[i]);
		}
	}

	// test 4 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 4, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
	values = new int[]{0x1, 0x2, 0x3, 0x4, 0xF};
	imageData.setPixels(0, 1, values.length - OFFSET, values, OFFSET);
	imageData.getPixels(0, 1, IMAGE_DIMENSION, pixelData, 0);
	for (int i = 0; i < pixelData.length; i++) {
		if (i + OFFSET < values.length) {
			assertEquals(":e:", values[i + OFFSET], pixelData[i]);
		} else {
			assertEquals(":f:", 0, pixelData[i]);
		}
	}

	// test 8 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 8, new PaletteData(new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)}));
	values = new int[]{0x1, 0x2, 0x3, 0xF, 0xFF};
	imageData.setPixels(0, 1, values.length - OFFSET, values, OFFSET);
	imageData.getPixels(0, 1, IMAGE_DIMENSION, pixelData, 0);
	for (int i = 0; i < pixelData.length; i++) {
		if (i + OFFSET < values.length) {
			assertEquals(":g:", values[i + OFFSET], pixelData[i]);
		} else {
			assertEquals(":h:", 0, pixelData[i]);
		}
	}

	// test 16 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 16, new PaletteData(0xF800, 0x7E0, 0x1F));
	values = new int[]{0, 0x2, 0xF, 0xFF, 0xFFAA};
	imageData.setPixels(0, 1, values.length - OFFSET, values, OFFSET);
	imageData.getPixels(0, 1, IMAGE_DIMENSION, pixelData, 0);
	for (int i = 0; i < pixelData.length; i++) {
		if (i + OFFSET < values.length) {
			assertEquals(":i:", values[i + OFFSET], pixelData[i]);
		} else {
			assertEquals(":j:", 0, pixelData[i]);
		}
	}

	// test 24 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 24, new PaletteData(0xFF0000, 0xFF00, 0xFF));
	values = new int[]{0, 0xFF, 0xFFAA, 0xFF00AA};
	imageData.setPixels(0, 1, values.length - OFFSET, values, OFFSET);
	imageData.getPixels(0, 1, IMAGE_DIMENSION, pixelData, 0);
	for (int i = 0; i < pixelData.length; i++) {
		if (i + OFFSET < values.length) {
			assertEquals(":k:", values[i + OFFSET], pixelData[i]);
		} else {
			assertEquals(":l:", 0, pixelData[i]);
		}
	}

	// test 32 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 32, new PaletteData(0xFF000000, 0xFF00, 0xFF));
	values = new int[]{0, 0xFF, 0xFFAA, 0xFF00AA00};
	imageData.setPixels(0, 1, values.length - OFFSET, values, OFFSET);
	imageData.getPixels(0, 1, IMAGE_DIMENSION, pixelData, 0);
	for (int i = 0; i < pixelData.length; i++) {
		if (i + OFFSET < values.length) {
			assertEquals(":m:", values[i + OFFSET], pixelData[i]);
		} else {
			assertEquals(":n:", 0, pixelData[i]);
		}
	}

	// exception cases
	try {
		imageData.setPixels(0, 1, IMAGE_DIMENSION*IMAGE_DIMENSION, pixelData, OFFSET);
		fail("No exception thrown for putWidth out of bounds");
	} catch (IndexOutOfBoundsException e) {
	}
	try {
		imageData.setPixels(0, 1, IMAGE_DIMENSION, (int[]) null, OFFSET);
		fail("No exception thrown for pixels == null");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for pixels == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
	try {
		imageData.setPixels(-1, 1, IMAGE_DIMENSION, pixelData, OFFSET);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setPixels(IMAGE_DIMENSION, 1, IMAGE_DIMENSION, pixelData, OFFSET);
		fail("No exception thrown for x out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setPixels(0, -1, IMAGE_DIMENSION, pixelData, OFFSET);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setPixels(0, IMAGE_DIMENSION, IMAGE_DIMENSION, pixelData, OFFSET);
		fail("No exception thrown for y out of bounds");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, e);
	}
	try {
		imageData.setPixels(0, 1, -1, pixelData, OFFSET);
		fail("No exception thrown for putWidth < 0");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for putWidth < 0", SWT.ERROR_INVALID_ARGUMENT, e);
	}
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
final int IMAGE_DIMENSION = 10;

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
