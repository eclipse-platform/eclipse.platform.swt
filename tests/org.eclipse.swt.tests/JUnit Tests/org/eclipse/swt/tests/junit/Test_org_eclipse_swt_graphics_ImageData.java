/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import static org.eclipse.swt.tests.graphics.ImageDataTestHelper.LSB_FIRST;
import static org.eclipse.swt.tests.graphics.ImageDataTestHelper.MSB_FIRST;
import static org.eclipse.swt.tests.junit.SwtTestUtil.assertSWTProblem;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.tests.graphics.ImageDataTestHelper;
import org.eclipse.swt.tests.graphics.ImageDataTestHelper.BlitTestInfo;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.ImageData
 *
 * @see org.eclipse.swt.graphics.ImageData
 */
public class Test_org_eclipse_swt_graphics_ImageData {
	static int[] indexedDepths = {1, 2, 4, 8, 16};
	static int[] directDepths  = {8, 16, 24, 32};

@Before
public void setUp() {
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 32, new PaletteData(0xFF0000, 0xFF00, 0xFF));
}

/**
 * Tests {@link ImageData#blit}:
 * creates a random image and tests over all combinations of depth,format,scale
 */
@Test
public void test_blit() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	List<BlitTestInfo> tests = new ArrayList<>();

	// Compose a list of all supported formats
	for (int iByteOrder = 0; iByteOrder < 2; iByteOrder++) {
		int byteOrder = (iByteOrder == 0) ? MSB_FIRST : LSB_FIRST;

		for (int scale = 1; scale < 3; scale++) {
			for (int depth : indexedDepths) {
				tests.add(new BlitTestInfo(depth, scale, byteOrder, false));
			}

			for (int depth : directDepths) {
				tests.add(new BlitTestInfo(depth, scale, byteOrder, true));
			}
		}
	}

	// Test all combinations
	for (BlitTestInfo dstInfo : tests)
	{
		for (BlitTestInfo srcInfo : tests)
		{
			if (srcInfo.isDirect && !dstInfo.isDirect) {
				// (direct -> indexed) is not supported in SWT
				continue;
			}

			if (!srcInfo.isDirect && dstInfo.isDirect && (srcInfo.scale != dstInfo.scale)) {
				// (indexed -> direct) is only supported for equal sizes
				continue;
			}

			if (!srcInfo.isDirect && !dstInfo.isDirect && (srcInfo.depth > dstInfo.depth)) {
				// Indexed depth downgrade is not supported in SWT
				continue;
			}

			try {
				BlitTestInfo actual = ImageDataTestHelper.blit(srcInfo, dstInfo.depth, dstInfo.scale, dstInfo.byteOrder, dstInfo.isDirect);
				ImageDataTestHelper.assertImageDataEqual(srcInfo.imageData, actual.imageData, dstInfo.imageData);
			} catch (Error e) {
				String error = "ImageData.blit() error with src=" + srcInfo + " dst=" + dstInfo;
				throw new Error(error, e);
			} catch (RuntimeException e) {
				String error = "ImageData.blit() error with src=" + srcInfo + " dst=" + dstInfo;
				throw new RuntimeException(error, e);
			}
		}
	}
}

/**
 * Tests {@link ImageData#blit}:
 * Ensures that (MSB_FIRST, LSB_FIRST) round trip produces original.
 */
@Test
public void test_blit_MsbLsb() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
	List<BlitTestInfo> tests = new ArrayList<>();
	{
		for (int depth : indexedDepths) {
			tests.add(new BlitTestInfo(depth, 1, MSB_FIRST, false));
		}

		for (int depth : directDepths) {
			tests.add(new BlitTestInfo(depth, 1, MSB_FIRST, true));
		}
	}

	for (BlitTestInfo src : tests) {
		try {
			BlitTestInfo lsb = ImageDataTestHelper.blit(src, src.depth, src.scale, LSB_FIRST, src.isDirect);
			BlitTestInfo msb = ImageDataTestHelper.blit(lsb, lsb.depth, lsb.scale, MSB_FIRST, lsb.isDirect);
			ImageDataTestHelper.assertImageDataEqual(src.imageData, msb.imageData, src.imageData);
		} catch (Error e) {
			String error = "ImageData.blit() error with src=" + src;
			throw new Error(error, e);
		} catch (RuntimeException e) {
			String error = "ImageData.blit() error with src=" + src;
			throw new RuntimeException(error, e);
		}
	}
}

@Test
public void test_ConstructorIIILorg_eclipse_swt_graphics_PaletteData() {
	assertThrows("No exception thrown for width < 0", IllegalArgumentException.class,
			() -> new ImageData(-1, 1, 1, new PaletteData(new RGB(0, 0, 0))));

	assertThrows("No exception thrown for height < 0", IllegalArgumentException.class,
			() -> new ImageData(1, -1, 1, new PaletteData(new RGB(0, 0, 0))));

	assertThrows("No exception thrown for paletteData == null", IllegalArgumentException.class,
			() -> new ImageData(1, 1, 1, null, 0, new byte[] { 0, 0x4f, 0x4f, 0 }));

	assertThrows("No exception thrown for unsupported depth", IllegalArgumentException.class,
			() -> new ImageData(1, 1, 3, new PaletteData(new RGB(0, 0, 0))));

	int[] validDepths = {1, 2, 4, 8, 16, 24, 32};
	for (int validDepth : validDepths) {
		new ImageData(1, 1, validDepth, new PaletteData(new RGB(0, 0, 0)));
	}
}

@Test
public void test_ConstructorIIILorg_eclipse_swt_graphics_PaletteDataI$B() {
	byte[] validData = {0, 0x4f, 0x4f, 0};

	assertThrows("No exception thrown for width < 0", IllegalArgumentException.class,
		() -> new ImageData(-1, 1, 1, new PaletteData(new RGB(0, 0, 0)), 1, validData));

	assertThrows("No exception thrown for height < 0", IllegalArgumentException.class,
		() -> new ImageData(1, -1, 1, new PaletteData(new RGB(0, 0, 0)), 1, validData));

	assertThrows("No exception thrown for paletteData == null", IllegalArgumentException.class,
		() -> new ImageData(1, 1, 1, null, 0, validData));

	assertThrows("No exception thrown for data == null", IllegalArgumentException.class,
		() -> new ImageData(1, 1, 1, new PaletteData(new RGB(0, 0, 0)), 1, null));

	assertThrows("No exception thrown for data array too small", IllegalArgumentException.class,
		() ->new ImageData(1, 1, 1, new PaletteData(new RGB(0, 0, 0)), 1, new byte[] {}));

	assertThrows("No exception thrown for data array too small", IllegalArgumentException.class,
		() -> new ImageData(1, 1, 16, new PaletteData(new RGB(0, 0, 0)), 1, new byte[] {0x4f}));

	assertThrows("No exception thrown for data array too small", IllegalArgumentException.class,
		() -> new ImageData(1, 1, 32, new PaletteData(new RGB(0, 0, 0)), 1, new byte[] {0x4f, 0x4f}));

	assertThrows("No exception thrown for data array too small", IllegalArgumentException.class,
		() ->new ImageData(2, 2, 8, new PaletteData(new RGB(0, 0, 0)), 1, new byte[] {0x4f, 0x4f, 0x4f}));

	assertThrows("No exception thrown for unsupported depth", IllegalArgumentException.class,
		() -> new ImageData(1, 1, 3, new PaletteData(new RGB(0, 0, 0)), 1, validData));

	// verify all valid depths
	int[] validDepths = {1, 2, 4, 8, 16, 24, 32};
	for (int validDepth : validDepths) {
		new ImageData(1, 1, validDepth, new PaletteData(new RGB(0, 0, 0)), 1, validData);
	}

	// verify no divide by zero exception if scanlinePad == 0
	assertThrows("No exception thrown for scanlinePad == 0", IllegalArgumentException.class,
		() -> new ImageData(1, 1, 8, new PaletteData(new RGB(0, 0, 0)), 0, validData));
}

@Test
public void test_ConstructorLjava_io_InputStream() throws IOException {
		InputStream stream = null;
		assertThrows("No exception thrown for InputStream == null", IllegalArgumentException.class,
				() -> new ImageData(stream));

		try (InputStream stream1 = SwtTestUtil.class.getResourceAsStream("empty.txt")){
			assertThrows("No exception thrown for invalid InputStream", SWTException.class, () ->new ImageData(stream1));
		}

		int numFormats = SwtTestUtil.imageFormats.length;
		String fileName = SwtTestUtil.imageFilenames[0];
		for (int i=0; i<numFormats; i++) {
			String format = SwtTestUtil.imageFormats[i];
			try (InputStream stream2 = SwtTestUtil.class.getResourceAsStream(fileName + "." + format)) {
				new ImageData(stream2);
			}
		}
}

@Test
public void test_ConstructorLjava_lang_String() {
	String filename = null;
	assertThrows("No exception thrown for filename == null", IllegalArgumentException.class,
			() -> new ImageData(filename));
}

@Test
public void test_clone() throws IOException {
	try (InputStream stream = SwtTestUtil.class.getResourceAsStream(SwtTestUtil.imageFilenames[0] + "." + SwtTestUtil.imageFormats[0])) {
		ImageData data1 = new ImageData(stream);
		ImageData data2 = (ImageData) data1.clone();
		// imageData does not implement an equals(Object) method
		assertEquals(":a:", data1.alpha, data2.alpha);
		assertArrayEquals(":b:", data1.alphaData, data2.alphaData);
		assertEquals(":c:", data1.bytesPerLine, data2.bytesPerLine);
		assertArrayEquals(":d:", data1.data, data2.data);
		assertEquals(":e:", data1.delayTime, data2.delayTime);
		assertEquals(":f:", data1.depth, data2.depth);
		assertEquals(":g:", data1.disposalMethod, data2.disposalMethod);
		assertEquals(":h:", data1.height, data2.height);
		assertArrayEquals(":i:", data1.maskData, data2.maskData);
		assertEquals(":j:", data1.maskPad, data2.maskPad);
		assertEquals(":k:", data1.palette, data2.palette);
		assertEquals(":l:", data1.scanlinePad, data2.scanlinePad);
		assertEquals(":m:", data1.transparentPixel, data2.transparentPixel);
		assertEquals(":n:", data1.type, data2.type);
		assertEquals(":o:", data1.width, data2.width);
		assertEquals(":p:", data1.x, data2.x);
		assertEquals(":q:", data1.y, data2.y);
	}
}

@Test
public void test_getAlphaII() {
	int value;

	assertEquals(":a:", 255, imageData.getAlpha(0, 0));
	value = 0xAA;
	imageData.setAlpha(0, 0, value);
	assertEquals(":b:", value, imageData.getAlpha(0, 0));

	// exception cases
	IllegalArgumentException ex = assertThrows("No exception thrown for x out of bounds",
			IllegalArgumentException.class, () -> imageData.getAlpha(-1, 1));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
			() -> imageData.getAlpha(IMAGE_DIMENSION, 1));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds",
			IllegalArgumentException.class, () -> imageData.getAlpha(0, -1));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
			() -> imageData.getAlpha(0, IMAGE_DIMENSION));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
}

@Test
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
			assertEquals(":d:", values[i-OFFSET], alphaData[i]);
		} else if (i < OFFSET+GET_WIDTH) {
			assertEquals(":e:", 0, alphaData[i]);
		}
	}

	// exception cases
	assertThrows("No exception thrown for getWidth out of bounds", IndexOutOfBoundsException.class,
		() -> imageData.getAlphas(0, 1, GET_WIDTH*GET_WIDTH, alphaData, OFFSET));
	IllegalArgumentException ex = assertThrows("No exception thrown for alphas == null", IllegalArgumentException.class,
			() -> imageData.getAlphas(0, 1, GET_WIDTH, (byte[]) null, OFFSET));
	assertSWTProblem("Incorrect exception thrown for alphas == null", SWT.ERROR_NULL_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() -> imageData.getAlphas(-1, 1, GET_WIDTH, alphaData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() ->imageData.getAlphas(IMAGE_DIMENSION, 1, GET_WIDTH, alphaData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
		() -> imageData.getAlphas(0, -1, GET_WIDTH, alphaData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
		() -> imageData.getAlphas(0, IMAGE_DIMENSION, GET_WIDTH, alphaData, OFFSET));
		assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for getWidth < 0", IllegalArgumentException.class,
			() -> imageData.getAlphas(0, 1, -1, alphaData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for getWidth < 0", SWT.ERROR_INVALID_ARGUMENT, ex);
}

@Test
public void test_getPixelII() {
	int value;

	assertEquals(":a:", 0, imageData.getPixel(0, 0));
	value = 0xAA;
	imageData.setPixel(0, 0, value);
	assertEquals(":b:", value, imageData.getPixel(0, 0));

	// exception cases
	IllegalArgumentException ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() -> imageData.getPixel(-1, 1));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() ->imageData.getPixel(IMAGE_DIMENSION, 1));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
		() -> imageData.getPixel(0, -1));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
		() -> imageData.getPixel(0, IMAGE_DIMENSION));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	int width = 3;
	int height = 3;
	int depth = 4;
	byte pixelValue = 1;
	byte[] data = {(byte) ((pixelValue << 4) + pixelValue), (byte) (pixelValue << 4), (byte) ((pixelValue << 4) + pixelValue), (byte) (pixelValue << 4), (byte) ((pixelValue << 4) + pixelValue), (byte) (pixelValue << 4)};
	imageData = new ImageData(width, height, depth, new PaletteData(new RGB(0, 0, 255), new RGB(111, 111, 111)), 1, data);
	for (int y = 0; y < height; y++) {
		for (int x = 0; x < width; x++) {
			int pixel = imageData.getPixel(x, y);
			assertEquals("Bad pixel data", pixelValue, pixel);
		}
	}
}

@Test
public void test_getPixelsIII$BI() {
	final int SIZE = 20;
	final int GET_WIDTH = 10;
	final int OFFSET = 10;
	byte[] pixelData = new byte[SIZE];

	// test 1 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 1, new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (byte b : pixelData) {
		assertEquals(":a:", 0, b);
	}

	byte[] values = new byte[]{0x1, 0x1, 0x1, 0x1, 0x1};
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
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 2, new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (byte b : pixelData) {
		assertEquals(":e:", 0, b);
	}

	values = new byte[]{0x1, 0x2, 0x3, 0x2, 0x1};
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
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 4, new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (byte b : pixelData) {
		assertEquals(":i:", 0, b);
	}

	values = new byte[]{0x1, 0x2, 0x3, 0x4, 0xF};
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
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 8, new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (byte b : pixelData) {
		assertEquals(":m:", 0, b);
	}

	values = new byte[]{0x1, 0x2, 0x3, 0xF, (byte)0xFF};
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

	// exception cases
	assertThrows("No exception thrown for getWidth out of bounds", IndexOutOfBoundsException.class,
		() -> imageData.getPixels(0, 1, GET_WIDTH*GET_WIDTH, pixelData, OFFSET));
	IllegalArgumentException ex = assertThrows("No exception thrown for pixels == null", IllegalArgumentException.class,
		() -> imageData.getPixels(0, 1, GET_WIDTH, (byte[]) null, OFFSET));
	assertSWTProblem("Incorrect exception thrown for pixels == null", SWT.ERROR_NULL_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		()->imageData.getPixels(-1, 1, GET_WIDTH, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() -> imageData.getPixels(IMAGE_DIMENSION, 1, GET_WIDTH, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
		() -> imageData.getPixels(0, -1, GET_WIDTH, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
		() -> imageData.getPixels(0, IMAGE_DIMENSION, GET_WIDTH, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for getWidth < 0", IllegalArgumentException.class,
		() -> imageData.getPixels(0, 1, -1, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for getWidth < 0", SWT.ERROR_INVALID_ARGUMENT, ex);
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 32, new PaletteData(0xFF0000, 0xFF00, 0xFF));
	SWTException swtEx = assertThrows("No exception thrown for invalid depth", SWTException.class,
		() -> imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for invalid depth", SWT.ERROR_UNSUPPORTED_DEPTH, swtEx);
}

@Test
public void test_getPixelsIII$II() {
	final int SIZE = 20;
	final int GET_WIDTH = 10;
	final int OFFSET = 10;
	int[] pixelData = new int[SIZE];

	// test 1 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 1, new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int data : pixelData) {
		assertEquals(":a:", 0, data);
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
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 2, new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int data : pixelData) {
		assertEquals(":e:", 0, data);
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
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 4, new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int data : pixelData) {
		assertEquals(":i:", 0, data);
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
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 8, new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
	imageData.getPixels(0, 1, GET_WIDTH, pixelData, OFFSET);
	for (int data : pixelData) {
		assertEquals(":m:", 0, data);
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
	for (int data : pixelData) {
		assertEquals(":q:", 0, data);
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
	for (int data : pixelData) {
		assertEquals(":u:", 0, data);
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
	for (int data : pixelData) {
		assertEquals(":y:", 0, data);
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
	assertThrows("No exception thrown for getWidth out of bounds", IndexOutOfBoundsException.class,
		() -> imageData.getPixels(0, 1, GET_WIDTH*GET_WIDTH, pixelData, OFFSET));
	IllegalArgumentException ex = assertThrows("No exception thrown for pixels == null", IllegalArgumentException.class,
		() -> imageData.getPixels(0, 1, GET_WIDTH, (int[]) null, OFFSET));
	assertSWTProblem("Incorrect exception thrown for pixels == null", SWT.ERROR_NULL_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() -> imageData.getPixels(-1, 1, GET_WIDTH, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() -> imageData.getPixels(IMAGE_DIMENSION, 1, GET_WIDTH, pixelData, OFFSET));
		assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
		()-> imageData.getPixels(0, -1, GET_WIDTH, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
		() -> imageData.getPixels(0, IMAGE_DIMENSION, GET_WIDTH, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for getWidth < 0", IllegalArgumentException.class,
		() -> imageData.getPixels(0, 1, -1, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for getWidth < 0", SWT.ERROR_INVALID_ARGUMENT, ex);
}

@Test
public void test_getRGBs() {
	assertNull(":a:", imageData.getRGBs());
	RGB[] rgbs = new RGB[]{new RGB(0, 0, 0), new RGB(255, 255, 255)};
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 8, new PaletteData(rgbs));
	assertArrayEquals(":b:", rgbs, imageData.getRGBs());
}

@Test
public void test_getTransparencyMask() throws IOException {
//	Bug 71472 - transparency mask should be null
//	assertNull(":a:", imageData.getTransparencyMask());

	try (InputStream stream = getClass().getResourceAsStream(SwtTestUtil.transparentImageFilenames[0])) {
		Image image = new Image(Display.getDefault(), stream);
		imageData = image.getImageData();
		ImageData maskData = imageData.getTransparencyMask();
		assertNotNull(":b:", maskData);
		image.dispose();
	}

//	Bug 71472 - transparency mask should be null
/*	image = new Image(Display.getDefault(), getClass().getResourceAsStream(imageFilenames[0] + '.' + imageFormats[imageFormats.length-1]));
	imageData = image.getImageData();
	maskData = imageData.getTransparencyMask();
	assertNull(":c:", maskData);
*/
}

@Test
public void test_getTransparencyType() throws IOException {
	assertEquals(":a:", SWT.TRANSPARENCY_NONE, imageData.getTransparencyType());

	try (InputStream stream = getClass().getResourceAsStream(SwtTestUtil.transparentImageFilenames[0])) {
		Image image = new Image(Display.getDefault(), stream);
		imageData = image.getImageData();
		assertNotEquals(":b:", SWT.TRANSPARENCY_NONE, imageData.getTransparencyType());
		image.dispose();
	}

	try (InputStream stream = getClass().getResourceAsStream(SwtTestUtil.imageFilenames[0] + '.' + SwtTestUtil.imageFormats[SwtTestUtil.imageFormats.length-1])) {
		Image image = new Image(Display.getDefault(), stream);
		imageData = image.getImageData();
		assertEquals(":c:", SWT.TRANSPARENCY_NONE, imageData.getTransparencyType());
		image.dispose();
	}
}

@Test
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
	assertArrayEquals(":a:", expectedPixelData, scaledPixelData);

	scaledImageData = imageData.scaledTo(imageDimension * 10, imageDimension);
	scaledPixelData = new byte[imageDimension * 10];
	scaledImageData.getPixels(0, 0, scaledPixelData.length, scaledPixelData, 0);
	assertEquals(":b:", 0, scaledPixelData[0]);
	assertEquals(":c:", 0, scaledPixelData[1]);

	scaledImageData = imageData.scaledTo(imageDimension, imageDimension * 10);
	scaledPixelData = new byte[imageDimension];
	scaledImageData.getPixels(0, 0, scaledPixelData.length, scaledPixelData, 0);
	expectedPixelData = new byte[] {0, 0x1, 0, 0, 0x1, 0x1, 0x1, 0x1};
	assertArrayEquals(":d:", expectedPixelData, scaledPixelData);
}

@Test
public void test_setAlphaIII() {
	int value;

	value = 0xAA;
	imageData.setAlpha(0, 0, value);
	assertEquals(":a:", value, imageData.getAlpha(0, 0));

	// exception cases
	IllegalArgumentException ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() -> imageData.setAlpha(-1, 1, value));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() -> imageData.setAlpha(IMAGE_DIMENSION, 1, value));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
		() -> imageData.setAlpha(0, -1, value));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
		() -> imageData.setAlpha(0, IMAGE_DIMENSION, value));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
}

@Test
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
			assertEquals(":a:", values[i + OFFSET], alphaData[i]);
		} else {
			assertEquals(":b:", 0, alphaData[i]);
		}
	}

	// exception cases
	assertThrows("No exception thrown for putWidth out of bounds", IndexOutOfBoundsException.class,
		() -> imageData.setAlphas(0, 1, IMAGE_DIMENSION*IMAGE_DIMENSION, alphaData, OFFSET));
	IllegalArgumentException ex = assertThrows("No exception thrown for alphas == null", IllegalArgumentException.class,
		() -> imageData.setAlphas(0, 1, IMAGE_DIMENSION, (byte[]) null, OFFSET));
	assertSWTProblem("Incorrect exception thrown for alphas == null", SWT.ERROR_NULL_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() -> imageData.setAlphas(-1, 1, IMAGE_DIMENSION, alphaData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() -> imageData.setAlphas(IMAGE_DIMENSION, 1, IMAGE_DIMENSION, alphaData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
		() -> imageData.setAlphas(0, -1, IMAGE_DIMENSION, alphaData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
		() -> imageData.setAlphas(0, IMAGE_DIMENSION, IMAGE_DIMENSION, alphaData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for putWidth < 0", IllegalArgumentException.class,
		() -> imageData.setAlphas(0, 1, -1, alphaData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for putWidth < 0", SWT.ERROR_INVALID_ARGUMENT, ex);
}

@Test
public void test_setPixelIII() {
	int value;

	value = 0xAA;
	imageData.setPixel(0, 0, value);
	assertEquals(":a:", value, imageData.getPixel(0, 0));

	// exception cases
	IllegalArgumentException ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() -> imageData.setPixel(-1, 1, value));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() -> imageData.setPixel(IMAGE_DIMENSION, 1, value));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
		() -> imageData.setPixel(0, -1, value));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() -> imageData.setPixel(0, IMAGE_DIMENSION, value));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
}

@Test
public void test_setPixelsIII$BI() {
	final int SIZE = 20;
	final int OFFSET = 1;
	byte[] pixelData = new byte[SIZE];

	// test 1 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 1, new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
	byte[] values = new byte[]{0x1, 0x1, 0x1, 0x1, 0x1};
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
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 2, new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
	values = new byte[]{0x1, 0x2, 0x3, 0x2, 0x1};
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
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 4, new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
	values = new byte[]{0x1, 0x2, 0x3, 0x4, 0xF};
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
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 8, new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
	values = new byte[]{0x1, 0x2, 0x3, 0xF, (byte)0xFF};
	imageData.setPixels(0, 1, values.length - OFFSET, values, OFFSET);
	imageData.getPixels(0, 1, IMAGE_DIMENSION, pixelData, 0);
	for (int i = 0; i < pixelData.length; i++) {
		if (i + OFFSET < values.length) {
			assertEquals(":g:", values[i + OFFSET], pixelData[i]);
		} else {
			assertEquals(":h:", 0, pixelData[i]);
		}
	}

	// exception cases
	assertThrows("No exception thrown for putWidth out of bounds", IndexOutOfBoundsException.class,
		() -> imageData.setPixels(0, 1, IMAGE_DIMENSION*IMAGE_DIMENSION, pixelData, OFFSET));
	IllegalArgumentException ex = assertThrows("No exception thrown for pixels == null", IllegalArgumentException.class,
		() -> imageData.setPixels(0, 1, IMAGE_DIMENSION, (byte[]) null, OFFSET));
	assertSWTProblem("Incorrect exception thrown for pixels == null", SWT.ERROR_NULL_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() -> imageData.setPixels(-1, 1, IMAGE_DIMENSION, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() -> imageData.setPixels(IMAGE_DIMENSION, 1, IMAGE_DIMENSION, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
		() -> imageData.setPixels(0, -1, IMAGE_DIMENSION, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
		() -> imageData.setPixels(0, IMAGE_DIMENSION, IMAGE_DIMENSION, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for putWidth < 0", IllegalArgumentException.class,
		() -> imageData.setPixels(0, 1, -1, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for putWidth < 0", SWT.ERROR_INVALID_ARGUMENT, ex);
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 32, new PaletteData(0xFF0000, 0xFF00, 0xFF));
	SWTException swtEx = assertThrows("No exception thrown for invalid depth", SWTException.class,
		() -> imageData.setPixels(0, 1, IMAGE_DIMENSION, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for invalid depth", SWT.ERROR_UNSUPPORTED_DEPTH, swtEx);
}

@Test
public void test_setPixelsIII$II() {
	final int SIZE = 20;
	final int OFFSET = 1;
	int[] pixelData = new int[SIZE];

	// test 1 bit
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 1, new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
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
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 2, new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
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
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 4, new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
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
	imageData = new ImageData(IMAGE_DIMENSION, IMAGE_DIMENSION, 8, new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
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
	assertThrows("No exception thrown for putWidth out of bounds", IndexOutOfBoundsException.class,
		() -> imageData.setPixels(0, 1, IMAGE_DIMENSION*IMAGE_DIMENSION, pixelData, OFFSET));
	IllegalArgumentException ex = assertThrows("No exception thrown for pixels == null", IllegalArgumentException.class,
		() -> imageData.setPixels(0, 1, IMAGE_DIMENSION, (int[]) null, OFFSET));
	assertSWTProblem("Incorrect exception thrown for pixels == null", SWT.ERROR_NULL_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() -> imageData.setPixels(-1, 1, IMAGE_DIMENSION, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for x out of bounds", IllegalArgumentException.class,
		() -> imageData.setPixels(IMAGE_DIMENSION, 1, IMAGE_DIMENSION, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for x out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
		() -> imageData.setPixels(0, -1, IMAGE_DIMENSION, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for y out of bounds", IllegalArgumentException.class,
		() -> imageData.setPixels(0, IMAGE_DIMENSION, IMAGE_DIMENSION, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for y out of bounds", SWT.ERROR_INVALID_ARGUMENT, ex);
	ex = assertThrows("No exception thrown for putWidth < 0", IllegalArgumentException.class,
		() -> imageData.setPixels(0, 1, -1, pixelData, OFFSET));
	assertSWTProblem("Incorrect exception thrown for putWidth < 0", SWT.ERROR_INVALID_ARGUMENT, ex);
}
/* custom */
ImageData imageData;
final int IMAGE_DIMENSION = 10;

}
