/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
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


import static org.eclipse.swt.tests.junit.SwtTestUtil.assertSWTProblem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageDataProvider;
import org.eclipse.swt.graphics.ImageFileNameProvider;
import org.eclipse.swt.graphics.ImageGcDrawer;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.Image
 *
 * @see org.eclipse.swt.graphics.Image
 */
@SuppressWarnings("restriction")
public class Test_org_eclipse_swt_graphics_Image {

	@ClassRule
	public static TemporaryFolder tempFolder = new TemporaryFolder();

	private static String getPath(String fileName) {
		return SwtTestUtil.getPath(fileName, tempFolder).toString();
	}

	ImageFileNameProvider imageFileNameProvider = zoom -> {
		String fileName = switch (zoom) {
		case 100 -> "collapseall.png";
		case 150 -> "collapseall@1.5x.png";
		case 200 -> "collapseall@2x.png";
		default -> null;
		};
		return fileName != null ? getPath(fileName) : null;
	};
	ImageDataProvider imageDataProvider = zoom -> {
		String fileName = switch (zoom) {
		case 100 -> "collapseall.png";
		case 150 -> "collapseall@1.5x.png";
		case 200 -> "collapseall@2x.png";
		default -> null;
		};
		return fileName != null ? new ImageData(getPath(fileName)) : null;
	};
	ImageDataProvider imageDataProvider1xOnly = zoom -> {
		if (zoom == 100) {
			return new ImageData(getPath("collapseall.png"));
		}
		return null;
	};
ImageGcDrawer imageGcDrawer = (gc, width, height) -> {};

@Before
public void setUp() {
	display = Display.getDefault();
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceII() {
	IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> new Image(display, -1, 10));
	assertSWTProblem("Incorrect exception thrown for width < 0", SWT.ERROR_INVALID_ARGUMENT, e1);

	IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class, () -> new Image(display, 0, 10));
	assertSWTProblem("Incorrect exception thrown for width == 0", SWT.ERROR_INVALID_ARGUMENT, e2);

	IllegalArgumentException e3 = assertThrows(IllegalArgumentException.class, () -> new Image(display, 10, -20));
	assertSWTProblem("Incorrect exception thrown for height < 0", SWT.ERROR_INVALID_ARGUMENT, e3);

	IllegalArgumentException e4 = assertThrows(IllegalArgumentException.class, () -> new Image(display, 10, 0));
	assertSWTProblem("Incorrect exception thrown for height == 0", SWT.ERROR_INVALID_ARGUMENT, e4);

	Image image = new Image(null, 10, 10);
	image.dispose();

	image = new Image(display, 10, 10);
	image.dispose();
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_Rectangle() {
	Image image;
	IllegalArgumentException e;
	Rectangle bounds1 = null;
	e = assertThrows(IllegalArgumentException.class, () -> new Image(display, bounds1));
	assertSWTProblem("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);

	Rectangle bounds2 = new Rectangle(0, 0, -1, 10);
	e = assertThrows(IllegalArgumentException.class, () -> new Image(display, bounds2));
	assertSWTProblem("Incorrect exception thrown for width < 0", SWT.ERROR_INVALID_ARGUMENT, e);

	Rectangle bounds3 = new Rectangle(0, 0, 0, 10);
	e = assertThrows(IllegalArgumentException.class, () -> new Image(display, bounds3));
	assertSWTProblem("Incorrect exception thrown for width == 0", SWT.ERROR_INVALID_ARGUMENT, e);

	Rectangle bounds4 = new Rectangle(0, 0, 10, -1);
	e = assertThrows(IllegalArgumentException.class, () -> new Image(display, bounds4));
	assertSWTProblem("Incorrect exception thrown for height < 0", SWT.ERROR_INVALID_ARGUMENT, e);

	Rectangle bounds5 = new Rectangle(0, 0, 10, 0);
	e = assertThrows(IllegalArgumentException.class, () -> new Image(display, bounds5));
	assertSWTProblem("Incorrect exception thrown for height == 0", SWT.ERROR_INVALID_ARGUMENT, e);

	// valid images
	Rectangle bounds = new Rectangle(-1, -10, 10, 10);
	image = new Image(display, bounds);
	image.dispose();

	bounds = new Rectangle(0, 0, 10, 10);
	image = new Image(null, bounds);
	image.dispose();

	image = new Image(display, bounds);
	image.dispose();
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageData() {
	IllegalArgumentException e;

	ImageData data1 = null;
	e = assertThrows(IllegalArgumentException.class, () -> new Image(display, data1));
	assertSWTProblem("Incorrect exception thrown for ImageData == null", SWT.ERROR_NULL_ARGUMENT, e);

//	Platform-specific test.
//	ImageData data2 = new ImageData(10, 10, 1, new PaletteData(0xff0000, 0x00ff00, 0x0000ff));
//	e = assertThrows(IllegalArgumentException.class, () -> new Image(display, data2));

	ImageData data;
	Image image;
	data = new ImageData(10, 10, 1, new PaletteData(new RGB(0, 0, 0)));
	image = new Image(null, data);
	image.dispose();

	data = new ImageData(10, 10, 1, new PaletteData(new RGB(0, 0, 0)));
	image = new Image(display, data);
	image.dispose();

	data = new ImageData(10, 10, 8, new PaletteData(0x30, 0x0C, 0x03));
	// set red pixel at x=9, y=9
	data.setPixel(9, 9, 0x30);
	image = new Image(display, data);
	Image gcImage = new Image(display, 10, 10);
	GC gc = new GC(gcImage);
	gc.drawImage(image, 0, 0);
	ImageData gcImageData = gcImage.getImageData();
	int redPixel = gcImageData.getPixel(9, 9);
	assertEquals(getRealRGB(display.getSystemColor(SWT.COLOR_RED)), gcImageData.palette.getRGB(redPixel));
	gc.dispose();
	gcImage.dispose();
	image.dispose();
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageData() {
	IllegalArgumentException e;

	ImageData data1 = new ImageData(10, 10, 1, new PaletteData(new RGB(0, 0, 0)));
	e = assertThrows(IllegalArgumentException.class, () -> new Image(display, null, data1));
	assertSWTProblem("Incorrect exception thrown for ImageData source == null", SWT.ERROR_NULL_ARGUMENT, e);

	e = assertThrows(IllegalArgumentException.class, () -> new Image(display, data1, null));
	assertSWTProblem("Incorrect exception thrown for ImageData mask == null", SWT.ERROR_NULL_ARGUMENT, e);

	ImageData data2 = new ImageData(10, 10, 1, new PaletteData(new RGB(0, 0, 0)));
	ImageData data3 = new ImageData(1, 10, 1, new PaletteData(new RGB(0, 0, 0)));
	e = assertThrows(IllegalArgumentException.class, () -> new Image(display, data2, data3));
	assertSWTProblem("Incorrect exception thrown for ImageData source width != ImageData mask width", SWT.ERROR_INVALID_ARGUMENT, e);

	ImageData data4 = new ImageData(10, 1, 1, new PaletteData(new RGB(0, 0, 0)));
	ImageData data5 = new ImageData(10, 10, 1, new PaletteData(new RGB(0, 0, 0)));
	e = assertThrows(IllegalArgumentException.class, () -> new Image(display, data4, data5));
	assertSWTProblem("Incorrect exception thrown for ImageData source height != ImageData mask height", SWT.ERROR_INVALID_ARGUMENT, e);

	ImageData data6 = new ImageData(10, 10, 8, new PaletteData(new RGB(0, 0, 0)));
	ImageData data7 = new ImageData(10, 10, 8, new PaletteData(new RGB(0, 0, 0)));
	Image image = new Image(display, data6, data7); // Image now accepts masks where depth != 1
	image.dispose();

	data6 = new ImageData(10, 10, 8, new PaletteData(0x30, 0x0C, 0x03));
	// set opaque red pixel at x=9, y=9
	data6.setPixel(9, 9, 0x30);
	data7 = new ImageData(10, 10, 1, new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
	data7.setPixel(9, 9, 1);
	image = new Image(display, data6, data7);
	Image gcImage = new Image(display, 10, 10);
	GC gc = new GC(gcImage);
	Color backgroundColor = display.getSystemColor(SWT.COLOR_BLUE);
	gc.setBackground(backgroundColor);
	gc.fillRectangle(0, 0, 10, 10);
	gc.drawImage(image, 0, 0);
	ImageData gcImageData = gcImage.getImageData();
	int redPixel = gcImageData.getPixel(9, 9);
	assertEquals(getRealRGB(display.getSystemColor(SWT.COLOR_RED)), gcImageData.palette.getRGB(redPixel));
	int bluePixel = gcImageData.getPixel(0, 0);
	assertEquals(getRealRGB(backgroundColor), gcImageData.palette.getRGB(bluePixel));
	gc.dispose();
	gcImage.dispose();
	image.dispose();
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_io_InputStream() throws IOException {
	Exception e;

	InputStream stream1 = null;
	e = assertThrows(IllegalArgumentException.class, () -> new Image(display, stream1));
	assertSWTProblem("Incorrect exception thrown for InputStream == null", SWT.ERROR_NULL_ARGUMENT, e);

	try (InputStream stream2 = SwtTestUtil.class.getResourceAsStream("empty.txt")) {
		e = assertThrows(SWTException.class, () -> new Image(display, stream2));
	}
	assertSWTProblem("Incorrect exception thrown for invalid InputStream", SWT.ERROR_UNSUPPORTED_FORMAT, e);

	String firstFile = SwtTestUtil.invalidImageFilenames[0];
	Display[] displays = { display, null };
	for (int j = 0; j < displays.length; j++) {
		for (String format : SwtTestUtil.imageFormats) {
			try (InputStream stream = SwtTestUtil.class.getResourceAsStream(firstFile + "." + format)) {
				e = assertThrows(SWTException.class, () -> new Image(display, stream));
			}
			assertSWTProblem("Incorrect exception thrown for invalid image InputStream", SWT.ERROR_INVALID_IMAGE, e);
		}
	}

	try (InputStream stream = SwtTestUtil.class.getResourceAsStream(SwtTestUtil.invalidImageFilenames[1])) {
		e = assertThrows(SWTException.class, () -> new Image(display, stream));
	}
	assertSWTProblem("Incorrect exception thrown for invalid image InputStream", SWT.ERROR_INVALID_IMAGE, e);

	// create valid images
	for (Display tempDisplay : displays) {
		for (String fileName : SwtTestUtil.imageFilenames) {
			for (String format : SwtTestUtil.imageFormats) {
				try (InputStream stream = SwtTestUtil.class.getResourceAsStream(fileName + "." + format);) {
					Image image = new Image(tempDisplay, stream);
					image.dispose();
				}
			}
		}
	}
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_String() {
	Exception e;

	String fileName1 = null;
	e = assertThrows(IllegalArgumentException.class, () -> new Image(display, fileName1));
	assertSWTProblem("Incorrect exception thrown for file name == null", SWT.ERROR_NULL_ARGUMENT, e);

	String pathName2 = "nonexistent.txt";
	e = assertThrows(SWTException.class, () -> new Image(display, pathName2));
	assertSWTProblem("Incorrect exception thrown for non-existent file name", SWT.ERROR_IO, e);

	String pathName3 = getPath("empty.txt").toString();
	e = assertThrows(SWTException.class, () -> new Image(display, pathName3));
	assertSWTProblem("Incorrect exception thrown for invalid file name", SWT.ERROR_UNSUPPORTED_FORMAT, e);

	String firstFile = SwtTestUtil.invalidImageFilenames[0];
	Display[] displays = { display, null };
	for (Display display : displays) {
		for (String format : SwtTestUtil.imageFormats) {
			String pathName = getPath(firstFile + "." + format).toString();
			e = assertThrows(SWTException.class, () -> new Image(display, pathName));
			assertSWTProblem("Incorrect exception thrown for invalid image file name", SWT.ERROR_INVALID_IMAGE, e);
		}
	}

	String pathName4 = getPath(SwtTestUtil.invalidImageFilenames[1]).toString();
	e = assertThrows(SWTException.class, () -> new Image(display, pathName4));
	assertSWTProblem("Incorrect exception thrown for invalid image file name", SWT.ERROR_INVALID_IMAGE, e);

	// create valid images
	for (Display display : displays) {
		for (String fileName : SwtTestUtil.imageFilenames) {
			for (int i = 0; i < SwtTestUtil.imageFormats.length; i++) {
				String format = SwtTestUtil.imageFormats[i];
				String pathName = getPath(fileName + "." + format).toString();
				Image image = new Image(display, pathName);
				image.dispose();
			}
		}
	}
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_Device_ImageFileNameProvider() {
	Exception e;

	// Null provider
	ImageFileNameProvider provider1 = null;
	e = assertThrows(IllegalArgumentException.class, ()->new Image(display, provider1));
	assertSWTProblem("Incorrect exception thrown for provider == null", SWT.ERROR_NULL_ARGUMENT, e);

	// Invalid provider
	ImageFileNameProvider	provider2 = zoom -> null;
	e = assertThrows(IllegalArgumentException.class, ()->new Image(display, provider2));
	assertSWTProblem("Incorrect exception thrown for provider == null", SWT.ERROR_INVALID_ARGUMENT, e);

	// Valid provider
	Image image = new Image(display, imageFileNameProvider);
	image.dispose();
	// Corrupt Image provider
	ImageFileNameProvider provider3 = zoom -> {
		String fileName = switch (zoom) {
		case 100, 150, 200 -> "corrupt.png";
		default -> null;
		};
		return fileName != null ? getPath(fileName) : null;
	};
	e = assertThrows(SWTException.class, ()->new Image(display, provider3));
	assertSWTProblem("Incorrect exception thrown for provider with corrupt images", SWT.ERROR_INVALID_IMAGE, e);

	// Valid provider only 100% zoom
	ImageFileNameProvider provider4 = zoom -> {
		if (zoom == 100) {
			return getPath("collapseall.png");
		}
		return null;
	};
	image = new Image(display, provider4);
	image.dispose();
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_Device_ImageDataProvider() {
	Exception e;
	// Null provider
	ImageDataProvider provider1 = null;
	e = assertThrows(IllegalArgumentException.class, () -> new Image(display, provider1));
	assertSWTProblem("Incorrect exception thrown for provider == null", SWT.ERROR_NULL_ARGUMENT, e);

	// Invalid provider
	ImageDataProvider provider2 = zoom -> null;
	e = assertThrows(IllegalArgumentException.class, () -> new Image(display, provider2));
	assertSWTProblem("Incorrect exception thrown for provider == null", SWT.ERROR_INVALID_ARGUMENT, e);
	// Valid provider
	Image image = new Image(display, imageDataProvider);
	image.dispose();
	// Corrupt Image provider
	ImageDataProvider provider3 = zoom -> {
		return switch (zoom) {
		case 100, 150, 200 -> new ImageData(getPath("corrupt.png"));
		default -> null;
		};
	};
	e = assertThrows(SWTException.class, () -> new Image(display, provider3));
	assertSWTProblem("Incorrect exception thrown for provider with corrupt images", SWT.ERROR_INVALID_IMAGE, e);
	// Valid provider only 100% zoom
	ImageDataProvider provider4 = zoom -> {
		if (zoom == 100) {
			return new ImageData(getPath("collapseall.png"));
		}
		return null;
	};
	image = new Image(display, provider4);
	image.dispose();
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_Device_ImageGcDrawer() {
	// Null provider
	ImageGcDrawer drawer = null;
	IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Image(display, drawer, 20, 20));
	assertSWTProblem("Incorrect exception thrown for ImageGcDrawer == null", SWT.ERROR_NULL_ARGUMENT, e);

	// Valid provider
	Image image = new Image(display, imageGcDrawer, 20, 20);
	image.dispose();
}

@Test
public void test_equalsLjava_lang_Object() {
	Image image = null;
	Image image1 = null;

	try {
		image = new Image(display, 10, 10);
		image1 = image;

		assertFalse(image.equals(null));

		assertTrue(image.equals(image1));

		ImageData imageData = new ImageData(10, 10, 1, new PaletteData(new RGB(0, 0, 0)));
		image.dispose();
		image = new Image(display, imageData);
		image1 = new Image(display, imageData);
		assertFalse(image.equals(image1));
	} finally {
		image.dispose();
		image1.dispose();
	}

	// ImageFileNameProvider
	try {
		image = new Image(display, imageFileNameProvider);
		image1 = image;

		assertFalse(image.equals(null));

		assertTrue(image.equals(image1));

		image1 = new Image(display, imageFileNameProvider);
		assertTrue(image.equals(image1));
	} finally {
		image.dispose();
		image1.dispose();
	}
	try {
		image = new Image(display, imageFileNameProvider);
		image1 = image;

		assertFalse(image.equals(null));

		assertTrue(image.equals(image1));

		image1 = new Image(display, imageFileNameProvider);
		assertTrue(image.equals(image1));
	} finally {
		image.dispose();
		image1.dispose();
	}

	// ImageDataProvider
	try {
		image = new Image(display, imageDataProvider);
		image1 = image;

		assertFalse(image.equals(null));

		assertTrue(image.equals(image1));

		image1 = new Image(display, imageDataProvider);
		assertTrue(image.equals(image1));
	} finally {
		image.dispose();
		image1.dispose();
	}

	// ImageGcDrawer
	try {
		image = new Image(display, imageGcDrawer, 10, 10);
		image1 = image;

		assertFalse(image.equals(null));

		assertTrue(image.equals(image1));

		image1 = new Image(display, imageGcDrawer, 10, 10);
		assertTrue(image.equals(image1));
	} finally {
		image.dispose();
		image1.dispose();
	}
}

@Test
public void test_getBackground() {
	Image image = new Image(display, 10, 10);
	image.dispose();
	SWTException e = assertThrows(SWTException.class, () -> image.getBackground());
	assertSWTProblem("Incorrect exception thrown for disposed image", SWT.ERROR_GRAPHIC_DISPOSED, e);
	// remainder tested in setBackground method
}

@Test
public void test_getBounds() {
	Rectangle bounds = new Rectangle(0, 0, 10, 20);
	Image image1 = new Image(display, bounds.width, bounds.height);
	image1.dispose();
	SWTException e = assertThrows(SWTException.class, () -> image1.getBounds());
	assertSWTProblem("Incorrect exception thrown for disposed image", SWT.ERROR_GRAPHIC_DISPOSED, e);

	Image image;
	// creates bitmap image
	image = new Image(display, bounds.width, bounds.height);
	Rectangle bounds1 = image.getBounds();
	image.dispose();
	assertEquals(bounds, bounds1);

	image = new Image(display, bounds);
	bounds1 = image.getBounds();
	image.dispose();
	assertEquals(bounds, bounds1);

	// create icon image
	ImageData imageData = new ImageData(bounds.width, bounds.height, 1, new PaletteData(new RGB(0, 0, 0)));
	image = new Image(display, imageData);
	bounds1 = image.getBounds();
	image.dispose();
	assertEquals(bounds, bounds1);
}

@SuppressWarnings("deprecation")
@Test
public void test_getBoundsInPixels() {
	Rectangle initialBounds = new Rectangle(0, 0, 10, 20);
	Image image1 = new Image(display, initialBounds.width, initialBounds.height);
	image1.dispose();
	SWTException e = assertThrows(SWTException.class, () -> image1.getBoundsInPixels());
	assertSWTProblem("Incorrect exception thrown for disposed image", SWT.ERROR_GRAPHIC_DISPOSED, e);

	Image image;
	// creates bitmap image
	image = new Image(display, initialBounds.width, initialBounds.height);
	Rectangle boundsInPixels = image.getBoundsInPixels();
	Rectangle bounds = image.getBounds();
	image.dispose();
	assertEquals("Image.getBounds method doesn't return original bounds.", initialBounds, bounds);
	assertEquals("Image.getBoundsInPixels method doesn't return bounds in Pixel values.", DPIUtil.autoScaleUp(initialBounds), boundsInPixels);

	// create icon image
	ImageData imageData = new ImageData(initialBounds.width, initialBounds.height, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	image = new Image(display, imageData);
	boundsInPixels = image.getBoundsInPixels();
	bounds = image.getBounds();
	image.dispose();
	assertEquals("Image.getBounds method doesn't return original bounds.", initialBounds, bounds);
	assertEquals("Image.getBoundsInPixels method doesn't return bounds in Pixel values.", DPIUtil.autoScaleUp(initialBounds), boundsInPixels);

	// create image with FileNameProvider
	image = new Image(display, imageFileNameProvider);
	boundsInPixels = image.getBoundsInPixels();
	bounds = image.getBounds();
	image.dispose();
	assertEquals("Image.getBoundsInPixels method doesn't return bounds in Pixel values.", DPIUtil.autoScaleUp(bounds), boundsInPixels);

	// create image with ImageDataProvider
	image = new Image(display, imageDataProvider);
	boundsInPixels = image.getBoundsInPixels();
	bounds = image.getBounds();
	image.dispose();
	assertEquals("Image.getBoundsInPixels method doesn't return bounds in Pixel values.", DPIUtil.autoScaleUp(bounds), boundsInPixels);

	// create image with ImageGcDrawer
	image = new Image(display, imageGcDrawer, initialBounds.width, initialBounds.height);
	boundsInPixels = image.getBoundsInPixels();
	bounds = image.getBounds();
	image.dispose();
	assertEquals("Image.getBounds method doesn't return original bounds.", initialBounds, bounds);
	assertEquals("Image.getBoundsInPixels method doesn't return bounds in Pixel values for ImageGcDrawer.", DPIUtil.autoScaleUp(initialBounds), boundsInPixels);
}

@SuppressWarnings("deprecation")
@Test
public void test_getImageDataCurrentZoom() {
	Rectangle bounds = new Rectangle(0, 0, 10, 20);
	Image image1 = new Image(display, bounds.width, bounds.height);
	image1.dispose();
	SWTException e = assertThrows(SWTException.class, () -> image1.getImageDataAtCurrentZoom());
	assertSWTProblem("Incorrect exception thrown for disposed image", SWT.ERROR_GRAPHIC_DISPOSED, e);

	Image image;
	// creates bitmap image and compare size of imageData
	image = new Image(display, bounds.width, bounds.height);
	ImageData imageDataAtCurrentZoom = image.getImageDataAtCurrentZoom();
	image.dispose();
	Rectangle boundsAtCurrentZoom = new Rectangle(0, 0, imageDataAtCurrentZoom.width, imageDataAtCurrentZoom.height);
	assertEquals(":a: Size of ImageData returned from Image.getImageDataAtCurrentZoom method doesn't return matches with bounds in Pixel values.", boundsAtCurrentZoom, DPIUtil.autoScaleUp(bounds));

	// create icon image and compare size of imageData
	ImageData imageData = new ImageData(bounds.width, bounds.height, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	image = new Image(display, imageData);
	imageDataAtCurrentZoom = image.getImageDataAtCurrentZoom();
	image.dispose();
	boundsAtCurrentZoom = new Rectangle(0, 0, imageDataAtCurrentZoom.width, imageDataAtCurrentZoom.height);
	assertEquals(":b: Size of ImageData returned from Image.getImageDataAtCurrentZoom method doesn't return matches with bounds in Pixel values.", boundsAtCurrentZoom, DPIUtil.autoScaleUp(bounds));

	// create image with FileNameProvider
	image = new Image(display, imageFileNameProvider);
	imageDataAtCurrentZoom = image.getImageDataAtCurrentZoom();
	boundsAtCurrentZoom = new Rectangle(0, 0, imageDataAtCurrentZoom.width, imageDataAtCurrentZoom.height);
	bounds = image.getBounds();
	image.dispose();
	assertEquals(":c: Size of ImageData returned from Image.getImageDataAtCurrentZoom method doesn't return matches with bounds in Pixel values.", boundsAtCurrentZoom, DPIUtil.autoScaleUp(bounds));

	// create image with ImageDataProvider
	image = new Image(display, imageDataProvider);
	imageDataAtCurrentZoom = image.getImageDataAtCurrentZoom();
	boundsAtCurrentZoom = new Rectangle(0, 0, imageDataAtCurrentZoom.width, imageDataAtCurrentZoom.height);
	bounds = image.getBounds();
	image.dispose();
	assertEquals(":d: Size of ImageData returned from Image.getImageDataAtCurrentZoom method doesn't return matches with bounds in Pixel values.", boundsAtCurrentZoom, DPIUtil.autoScaleUp(bounds));
}

@Test
public void test_getImageData() {
	getImageData1();
	getImageData2(24, new PaletteData(0xff0000, 0xff00, 0xff));
	getImageData2(32, new PaletteData(0xff0000, 0xff00, 0xff));
}

@Test
public void test_getImageData_100() {
	int zoom = DPIUtil.getDeviceZoom();
	try {
		DPIUtil.setDeviceZoom(100);
		getImageData_int(100);
	} finally {
		DPIUtil.setDeviceZoom(zoom);
	}
}

@Test
public void test_getImageData_125() {
	int zoom = DPIUtil.getDeviceZoom();
	try {
		DPIUtil.setDeviceZoom(125);
		getImageData_int(125);
	} finally {
		DPIUtil.setDeviceZoom(zoom);
	}
}

@Test
public void test_getImageData_150() {
	int zoom = DPIUtil.getDeviceZoom();
	try {
		DPIUtil.setDeviceZoom(150);
		getImageData_int(150);
	} finally {
		DPIUtil.setDeviceZoom(zoom);
	}
}

@Test
public void test_getImageData_200() {
	int zoom = DPIUtil.getDeviceZoom();
	try {
		DPIUtil.setDeviceZoom(200);
		getImageData_int(200);
	} finally {
		DPIUtil.setDeviceZoom(zoom);
	}
}

void getImageData_int(int zoom) {
	Rectangle bounds = new Rectangle(0, 0, 10, 20);
	Image image1 = new Image(display, bounds.width, bounds.height);
	image1.dispose();
	SWTException e = assertThrows(SWTException.class, () -> image1.getImageData(zoom));
	assertSWTProblem("Incorrect exception thrown for disposed image", SWT.ERROR_GRAPHIC_DISPOSED, e);

	Image image;
	// creates bitmap image and compare size of imageData
	image = new Image(display, bounds.width, bounds.height);
	ImageData imageDataAtZoom = image.getImageData(zoom);
	image.dispose();
	Rectangle boundsAtZoom = new Rectangle(0, 0, imageDataAtZoom.width, imageDataAtZoom.height);
	assertEquals(":a: Size of ImageData returned from Image.getImageData(int) method doesn't return matches with bounds in Pixel values.", scaleBounds(bounds, zoom, 100), boundsAtZoom);

	// creates second bitmap image and compare size of imageData
	image = new Image(display, bounds);
	imageDataAtZoom = image.getImageData(zoom);
	boundsAtZoom = new Rectangle(0, 0, imageDataAtZoom.width, imageDataAtZoom.height);
	bounds = image.getBounds();
	image.dispose();
	assertEquals(":a: Size of ImageData returned from Image.getImageData(int) method doesn't return matches with bounds in Pixel values.", scaleBounds(bounds, zoom, 100), boundsAtZoom);

	// create icon image and compare size of imageData
	ImageData imageData = new ImageData(bounds.width, bounds.height, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	image = new Image(display, imageData);
	imageDataAtZoom = image.getImageData(zoom);
	image.dispose();
	boundsAtZoom = new Rectangle(0, 0, imageDataAtZoom.width, imageDataAtZoom.height);
	assertEquals(":b: Size of ImageData returned from Image.getImageData(int) method doesn't return matches with bounds in Pixel values.", scaleBounds(bounds, zoom, 100), boundsAtZoom);

	// create image with FileNameProvider
	image = new Image(display, imageFileNameProvider);
	imageDataAtZoom = image.getImageData(zoom);
	boundsAtZoom = new Rectangle(0, 0, imageDataAtZoom.width, imageDataAtZoom.height);
	bounds = image.getBounds();
	image.dispose();
	assertEquals(":c: Size of ImageData returned from Image.getImageData(int) method doesn't return matches with bounds in Pixel values.", scaleBounds(bounds, zoom, 100), boundsAtZoom);

	// create image with ImageDataProvider
	image = new Image(display, imageDataProvider);
	imageDataAtZoom = image.getImageData(zoom);
	boundsAtZoom = new Rectangle(0, 0, imageDataAtZoom.width, imageDataAtZoom.height);
	bounds = image.getBounds();
	image.dispose();
	assertEquals(":d: Size of ImageData returned from Image.getImageData(int) method doesn't return matches with bounds in Pixel values.", scaleBounds(bounds, zoom, 100), boundsAtZoom);

	// create image with ImageDataProvider that only has 1x data
	image = new Image(display, imageDataProvider1xOnly);
	imageDataAtZoom = image.getImageData(zoom);
	boundsAtZoom = new Rectangle(0, 0, imageDataAtZoom.width, imageDataAtZoom.height);
	bounds = image.getBounds();
	image.dispose();
	assertEquals(":d: Size of ImageData returned from Image.getImageData(int) method doesn't return matches with bounds in Pixel values.", scaleBounds(bounds, zoom, 100), boundsAtZoom);
}

public static Rectangle scaleBounds (Rectangle rect, int targetZoom, int currentZoom) {
	float scaleFactor = ((float)targetZoom) / (float)currentZoom;
	Rectangle returnRect = new Rectangle (0,0,0,0);
	returnRect.x = Math.round (rect.x * scaleFactor);
	returnRect.y = Math.round (rect.y * scaleFactor);
	returnRect.width = Math.round (rect.width * scaleFactor);
	returnRect.height = Math.round (rect.height * scaleFactor);
	return returnRect;
}

@Test
public void test_hashCode() {
	Image image = null;
	Image image1 = null;

	try {
		image = new Image(display, 10, 10);
		image1 = image;

		assertEquals(image1.hashCode(), image.hashCode());

		ImageData imageData = new ImageData(10, 10, 1, new PaletteData(new RGB(0, 0, 0)));
		image.dispose();
		image = new Image(display, imageData);
		image1 = new Image(display, imageData);
		boolean equals = (image1.hashCode() == image.hashCode());
		assertFalse(equals);
	} finally {
		image.dispose();
		image1.dispose();
	}

	// ImageFileNameProvider
	try {
		image = new Image(display, imageFileNameProvider);
		image1 = new Image(display, imageFileNameProvider);
		assertEquals(image1.hashCode(), image.hashCode());
	} finally {
		image.dispose();
		image1.dispose();
	}

	// ImageDataProvider
	try {
		image = new Image(display, imageDataProvider);
		image1 = new Image(display, imageDataProvider);
		assertEquals(image1.hashCode(), image.hashCode());
	} finally {
		image.dispose();
		image1.dispose();
	}

	// ImageGcDrawer
	try {
		image = new Image(display, imageGcDrawer, 10, 10);
		image1 = new Image(display, imageGcDrawer, 10, 10);
		assertEquals(image1.hashCode(), image.hashCode());
	} finally {
		image.dispose();
		image1.dispose();
	}
}

@Test
public void test_isDisposed() {
	Image image = new Image(display, 10, 10);
	assertFalse(image.isDisposed());
	image.dispose();
	assertTrue(image.isDisposed());
}

@Test
public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	assumeFalse(
			"Excluded test_setBackgroundLorg_eclipse_swt_graphics_Color(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_Image)",
			SwtTestUtil.isGTK);
	// TODO Fix GTK failure.
	Image image1 = new Image(display, 10, 10);
	try {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> image1.setBackground(null));
		assertSWTProblem("Incorrect exception thrown for color == null", SWT.ERROR_NULL_ARGUMENT, e);
	} finally {
		image1.dispose();
	}
	Image image2 = new Image(display, 10, 10);
	Color color2 = new Color(255, 255, 255);
	color2.dispose();
	try {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> image2.setBackground(color2));
		assertSWTProblem("Incorrect exception thrown for disposed color", SWT.ERROR_INVALID_ARGUMENT, e);
	} finally {
		image2.dispose();
	}
	Image image3 = new Image(display, 10, 10);
	image3.dispose();
	Color color3 = new Color(255, 255, 255);
	SWTException e = assertThrows(SWTException.class, () -> image3.setBackground(color3));
	assertSWTProblem("Incorrect exception thrown for disposed image", SWT.ERROR_GRAPHIC_DISPOSED, e);

	// this image does not have a transparent pixel by default so setBackground has no effect
	Image image4 = new Image(display, 10, 10);
	image4.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
	Color color4 = image4.getBackground();
	assertNull("background color should be null for non-transparent image", color4);
	image4.dispose();

	// create an image with transparency and then set the background color
	ImageData imageData = new ImageData(10, 10, 2,
			new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255), new RGB(50, 100, 150)));
	imageData.transparentPixel = 0; // transparent pixel is currently black
	Image image5 = new Image(display, imageData);
	image5.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
	Color color5 = image5.getBackground();
	assertEquals("background color should have been set to green", display.getSystemColor(SWT.COLOR_GREEN), color5);
	image5.dispose();
}

@Test
public void test_toString() {
	Image image = new Image(display, 10, 10);
	try {
		assertNotNull(image.toString());
		assertTrue(image.toString().length() > 0);
	} finally {
		image.dispose();
	}
}

/* custom */
Display display;

/** Test implementation **/

void getImageData1() {
	int numFormats = SwtTestUtil.imageFormats.length;
	String fileName = SwtTestUtil.imageFilenames[0];
	for (int i=0; i<numFormats; i++) {
		String format = SwtTestUtil.imageFormats[i];
		try (InputStream stream = SwtTestUtil.class.getResourceAsStream(fileName + "." + format)) {
			ImageData data1 = new ImageData(stream);
			Image image = new Image(display, data1);
			ImageData data2 = image.getImageData();
			image.dispose();
			assertEquals("Image width should be the same", data1.width, data2.width);
			assertEquals("Image height should be the same", data1.height, data2.height);
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
	Color color = new Color(0, 0xff, 0);
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
	gc.dispose();
	image.dispose();
}

RGB getRealRGB(Color color) {
	Image colorImage = new Image(display, 10, 10);
	GC imageGc = new GC(colorImage);
	ImageData imageData;
	PaletteData palette;
	int pixel;

	imageGc.setBackground(color);
	imageGc.setForeground(color);
	imageGc.fillRectangle(0, 0, 10, 10);
	imageData = colorImage.getImageData();
	palette = imageData.palette;
	imageGc.dispose();
	colorImage.dispose();
	pixel = imageData.getPixel(0, 0);
	return palette.getRGB(pixel);
}

/**
 * Create two types of gray-scale image. Same content but one encoded with color
 * table and one with an efficient use of color masks.
 */
@Test
public void test_bug566545_efficientGrayscaleImage() {
	RGB[] grayscale = new RGB[256];
	for (int i = 0; i < grayscale.length; i++)
		grayscale[i] = new RGB(i, i, i);
	int width = 128;
	int height = 128;
	ImageData imageDataIndexed = new ImageData(width, height, 8, new PaletteData(grayscale));
	ImageData imageDataDirect = new ImageData(width, height, 8, new PaletteData(0xFF, 0xFF, 0xFF));

	Consumer<ImageData> fillImage = imageData -> {
		for (int y = 0; y < imageData.height; y++)
			for (int x = 0; x < imageData.width; x++)
				imageData.setPixel(x, y, (x + y) % 256);
	};
	fillImage.accept(imageDataIndexed);
	fillImage.accept(imageDataDirect);

	Image imageIndexed = new Image(display, imageDataIndexed);
	Image imageDirect = new Image(display, imageDataDirect);
	Image outImageIndexed = new Image(display, width, height);
	Image outImageDirect = new Image(display, width, height);

	GC gc = new GC(outImageIndexed);
	gc.drawImage(imageIndexed, 0, 0);
	gc.dispose();
	gc = new GC(outImageDirect);
	gc.drawImage(imageDirect, 0, 0);
	gc.dispose();

	ImageTestUtil.assertImagesEqual(imageDataIndexed, imageDataDirect);
	ImageTestUtil.assertImagesEqual(imageIndexed.getImageData(), imageDirect.getImageData());
	ImageTestUtil.assertImagesEqual(outImageIndexed.getImageData(), outImageDirect.getImageData());

	imageIndexed.dispose();
	imageDirect.dispose();
	outImageIndexed.dispose();
	outImageDirect.dispose();
}

@Test
public void test_updateWidthHeightAfterDPIChange() {
	int deviceZoom = DPIUtil.getDeviceZoom();
	try {
		Rectangle imageSize = new Rectangle(0, 0, 16, 16);
		Image baseImage = new Image(display, imageSize.width, imageSize.height);
		GC gc = new GC(display);
		gc.drawImage(baseImage, 10, 10);
		assertEquals("Base image size differs unexpectedly", imageSize, baseImage.getBounds());

		DPIUtil.setDeviceZoom(deviceZoom * 2);
		gc.drawImage(baseImage, 10, 10);
		assertEquals("Image size at 100% must always stay the same despite the zoom factor", imageSize, baseImage.getBounds());
		gc.dispose();
		baseImage.dispose();
	} finally {
		DPIUtil.setDeviceZoom(deviceZoom);
	}
}

@Test
public void test_imageDataIsCached() {
	assumeTrue("On-demand image creation only implemented for Windows", SwtTestUtil.isWindows);
	String imagePath = getPath("collapseall.png");
	ImageFileNameProvider imageFileNameProvider = __ -> {
		return imagePath;
	};
	Image fileNameProviderImage = new Image(display, imageFileNameProvider);
	assertSame(fileNameProviderImage.getImageData(100), fileNameProviderImage.getImageData(100));
}

@Test
public void test_imageDataSameViaDifferentProviders() {
	assumeFalse("Cocoa generates inconsistent image data", SwtTestUtil.isCocoa);
	String imagePath = getPath("collapseall.png");
	ImageFileNameProvider imageFileNameProvider = __ -> {
		return imagePath;
	};
	ImageDataProvider dataProvider = __ -> {
		try (InputStream imageStream = Files.newInputStream(Path.of(imagePath))) {
			return new ImageData(imageStream);
		} catch (IOException e) {
		}
		return null;
	};
	Image fileNameProviderImage = new Image(display, imageFileNameProvider);
	Image dataProviderImage = new Image(display, dataProvider);
	ImageData dataFromFileNameProviderImage = fileNameProviderImage.getImageData(100);
	ImageData dataFromImageDescriptorImage = dataProviderImage.getImageData(100);
	assertEquals(0, imageDataComparator().compare(dataFromFileNameProviderImage, dataFromImageDescriptorImage));

	fileNameProviderImage.dispose();
	dataProviderImage.dispose();
}

@Test
public void test_imageDataSameViaProviderAndSimpleData() {
	assumeFalse("Cocoa generates inconsistent image data", SwtTestUtil.isCocoa);
	String imagePath = getPath("collapseall.png");
	ImageFileNameProvider imageFileNameProvider = __ -> {
		return imagePath;
	};
	ImageDataProvider dataProvider = __ -> {
		try (InputStream imageStream = Files.newInputStream(Path.of(imagePath))) {
			return new ImageData(imageStream);
		} catch (IOException e) {
		}
		return null;
	};
	Image fileNameProviderImage = new Image(display, imageFileNameProvider);
	Image dataImage = new Image(display, dataProvider.getImageData(100));
	ImageData dataFromFileNameProviderImage = fileNameProviderImage.getImageData(100);
	ImageData dataFromImageWithSimpleData = dataImage.getImageData(100);
	assertEquals(0, imageDataComparator().compare(dataFromFileNameProviderImage, dataFromImageWithSimpleData));

	fileNameProviderImage.dispose();
	dataImage.dispose();
}


private Comparator<ImageData> imageDataComparator() {
	return Comparator.<ImageData>comparingInt(d -> d.width) //
			.thenComparing(d -> d.height) //
			.thenComparing((ImageData firstData, ImageData secondData) -> {
				for (int x = 0; x < firstData.width; x++) {
					for (int y = 0; y < firstData.height; y++) {
						if (firstData.getPixel(x, y) != secondData.getPixel(x, y)) {
							return -1;
						}
						if (firstData.getAlpha(x, y) != secondData.getAlpha(x, y)) {
							return -1;
						}
					}
				}
				return 0;
			});
}

}
