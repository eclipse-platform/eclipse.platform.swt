/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.ImageLoaderEvent;
import org.eclipse.swt.graphics.ImageLoaderListener;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.ImageLoader
 *
 * @see org.eclipse.swt.graphics.ImageLoader
 */
public class Test_org_eclipse_swt_graphics_ImageLoader {

@Test
public void test_Constructor() {
	new ImageLoader();
}

@Test
public void test_addImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener() {
	ImageLoader loader = new ImageLoader();
	ImageLoaderListener loaderListener = e -> loaderListenerCalled = true;

	try {
		loader.addImageLoaderListener(null);
		fail("No exception thrown for addImageLoaderListener with null argument");
	} catch (IllegalArgumentException e) {
	}

	assertFalse(":a:", loader.hasListeners());
	loader.addImageLoaderListener(loaderListener);
	assertTrue(":b:", loader.hasListeners());

	loaderListenerCalled = false;
	try (InputStream stream = SwtTestUtil.class.getResourceAsStream("interlaced_target.png")) {
		loader.load(stream);
	} catch (IOException e) {}
	assertTrue(":c:", loaderListenerCalled);

	loaderListenerCalled = false;
	try (InputStream stream = SwtTestUtil.class.getResourceAsStream("target.png")) {
		loader.load(stream);
	} catch (IOException e) {}
	assertFalse(":d:", loaderListenerCalled);

	loaderListenerCalled = false;
	loader.notifyListeners(new ImageLoaderEvent(loader, loader.data[0], 0, true));
	assertTrue(":e:", loaderListenerCalled);

	loader.removeImageLoaderListener(loaderListener);
	assertFalse(":f:", loader.hasListeners());
}

@Test
public void test_loadLjava_io_InputStream() {
		ImageLoader loader = new ImageLoader();
		try (InputStream stream = null) {
			loader.load(stream);
			fail("No exception thrown for load inputStream == null");
		} catch (IllegalArgumentException | IOException e) {
		}

		try (InputStream stream = SwtTestUtil.class.getResourceAsStream("empty.txt")) {
			loader.load(stream);
			fail("No exception thrown for load from invalid inputStream");
		} catch (IOException|SWTException e) {
		}

		int numFormats = SwtTestUtil.imageFormats.length;
		String fileName = SwtTestUtil.imageFilenames[0];
		for (int i = 0; i < numFormats; i++) {
			String format = SwtTestUtil.imageFormats[i];
			try (InputStream stream = SwtTestUtil.class.getResourceAsStream(fileName + "." + format)) {
				loader.load(stream);
			} catch (IOException e) {
			}
		}
}

@Test
public void test_loadLjava_lang_String() {
	ImageLoader loader = new ImageLoader();
	String filename = null;
	try {
		loader.load(filename);
		fail("No exception thrown for load filename == null");
	} catch (IllegalArgumentException e) {
	}
}

@Test
public void test_saveLjava_io_OutputStreamI() {
	ImageLoader loader = new ImageLoader();
	ByteArrayOutputStream outStream = null;
	try {
		try {
			loader.save(outStream, 0);
			fail("No exception thrown for save outputStream == null");
		} catch (IllegalArgumentException e) {
		}

		outStream = new ByteArrayOutputStream();
		try {
			loader.save(outStream, -1);
			fail("No exception thrown for save to invalid outputStream format");
		} catch (SWTException e) {
		}
		boolean jpgSupported = false;
		for (int i=0; i<SwtTestUtil.imageFormats.length; i++) {
			if (SwtTestUtil.imageFormats[i].equals("jpg")) {
				jpgSupported = true;
				break;
			}
		}
		if (jpgSupported) {
			String filename = SwtTestUtil.imageFilenames[0];
			// must use jpg since save is not implemented yet in png format
			String filetype = "jpg";
			try (InputStream inStream = SwtTestUtil.class.getResourceAsStream(filename + "." + filetype)) {
				loader.load(inStream);
			} catch (IOException e) {}
			for (int i = 0; i < SwtTestUtil.imageFormats.length; i++) {
				if (SwtTestUtil.imageFormats[i].equals(filetype)) {
					// save using the appropriate format
					loader.save(outStream, i);
					break;
				}
			}
		}
	} finally {
		try {
			outStream.close();
		} catch (Exception e) {
		}
	}
}

@Test
public void test_saveLjava_lang_StringI() {
	ImageLoader loader = new ImageLoader();
	String filename = null;
	try {
		loader.save(filename, 0);
		fail("No exception thrown for save filename == null");
	} catch (IllegalArgumentException e) {
	}
}

/**
 * Ensure that saving and loading an image with {@link ImageLoader}
 * does not result in different {@link ImageData#data} arrays.
 */
@Test
public void test_bug547529() {
	Display display = Display.getDefault();
	try {
		int imageWidth = 8;
		int imageHeight = 8;
		int imageDepth = 24;

		int RED = 0xFF0000;
		int GREEN = 0x00FF00;
		int BLUE = 0x0000FF;
		PaletteData palette = new PaletteData(RED, GREEN, BLUE);
		ImageData imageData = new ImageData(imageWidth, imageHeight, imageDepth, palette);
		int w = imageWidth / 2;
		int h = imageHeight / 2;

		for (int y = 0; y < imageHeight; ++y) {
			for (int x = 0; x < imageWidth; ++x) {
				int color = 0x000000;
				if (x < w && y < h) {
					color = RED;
				} else if (x < w && y >= h) {
					color = GREEN;
				} else if (x >= w && y < h) {
					color = BLUE;
				}
				imageData.setPixel(x, y, color);
			}
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageLoader saver = new ImageLoader();
		saver.data = new ImageData[] { imageData };
		saver.save(outputStream, SWT.IMAGE_PNG);
		byte[] savedBytes = imageData.data;
		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		ImageLoader loader = new ImageLoader();
		loader.load(inputStream);
		ImageData[] loadedData = loader.data;
		assertEquals("ImageLoader loaded incorrect number of ImageData objects", 1, loadedData.length);
		byte[] loadedBytes = loadedData[0].data;
		assertArrayEquals(savedBytes, loadedBytes);
	} finally {
		display.dispose();
	}
}

/* custom */
boolean loaderListenerCalled;
}
