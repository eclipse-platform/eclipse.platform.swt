/*******************************************************************************
 * Copyright (c) 2024 Yatta Solutions
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta Solutions - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


import static org.eclipse.swt.tests.win32.SwtWin32TestUtil.assertImageDataEqualsIgnoringAlphaInData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.widgets.Display;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Automated Tests for class org.eclipse.swt.graphics.Image
 * for Windows specific behavior
 *
 * @see org.eclipse.swt.graphics.Image
 */
@SuppressWarnings("restriction")
public class ImageWin32Tests {
	private Display display;

	@BeforeEach
	public void setUp() {
		display = Display.getDefault();
	}

	@Test
	public void testImageDataForDifferentFractionalZoomsShouldBeDifferent() {
		ImageGcDrawer noOpGcDrawer = (gc, height, width) -> {};
		Image image = new Image(display, noOpGcDrawer, 10, 10);
		int zoom1 = 125;
		int zoom2 = 150;
		ImageData imageDataAtZoom1 = image.getImageData(zoom1);
		ImageData imageDataAtZoom2 = image.getImageData(zoom2);
		assertNotEquals(imageDataAtZoom1.height, imageDataAtZoom2.height,
				"ImageData::height should not be the same for imageData at different zoom levels");
		assertNotEquals(imageDataAtZoom1.width, imageDataAtZoom2.width,
				"ImageData::width should not be the same for imageData at different zoom levels");
	}

	@Test
	public void testImageShouldHaveDimesionAsPerZoomLevel() {
		ImageGcDrawer noOpGcDrawer = (gc, height, width) -> {};
		int zoom = DPIUtil.getDeviceZoom();
		int scalingFactor = 2;
		Image image = new Image(display, noOpGcDrawer, 10, 10);
		try {
			ImageData baseImageData = image.getImageData(zoom);
			assertEquals(10, baseImageData.width, "Width should equal the initial width on the same zoom");
			ImageData scaledImageData = image.getImageData(zoom * scalingFactor);
			assertEquals(10*2, scaledImageData.width, "Width should be doubled on doubled zoom");
		} finally {
			image.dispose();
		}
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false } )
	public void testRetrieveImageDataAtDifferentZooms(boolean concurrentlyDisposeDrawnImage) {
		Color imageColor = display.getSystemColor(SWT.COLOR_RED);
		Image image = new Image (display, 1, 1);
		GC gc = new GC(image);

		try {
			Image temporaryImage = createImageOfSizeOne(imageColor);
			gc.drawImage(temporaryImage, 0,  0);

			if (concurrentlyDisposeDrawnImage) {
				temporaryImage.dispose();
			}
			ImageData resultImageDataAtDifferentZoom = image.getImageData(200);

			assertEquals(imageColor.getRGB(), getRGBAtPixel(resultImageDataAtDifferentZoom, 0, 0));

			if (!concurrentlyDisposeDrawnImage) {
				temporaryImage.dispose();
			}
		} finally {
			gc.dispose();
			image.dispose();
		}
	}

	private Image createImageOfSizeOne(Color imageColor) {
		Image imageToDraw = new Image (display, 1, 1);
		GC imageToDrawGc = new GC(imageToDraw);
		imageToDrawGc.setBackground(imageColor);
		imageToDrawGc.fillRectangle(0, 0, 1, 1);
		imageToDrawGc.dispose();
		return imageToDraw;
	}

	private RGB getRGBAtPixel(ImageData resultImageDataAtDifferentZoom, int x, int y) {
		return resultImageDataAtDifferentZoom.palette.getRGB(resultImageDataAtDifferentZoom.getPixel(x, y));
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false } )
	public void testDrawImageAtDifferentZooms(boolean concurrentlyDisposeDrawnImage) {
		Color imageColor = display.getSystemColor(SWT.COLOR_RED);
		Image image = new Image (display, 1, 1);
		GC gc = new GC(image);
		Image temporaryImage = createImageOfSizeOne(imageColor);
		try {
			gc.drawImage(temporaryImage, 0,  0);
		} finally {
			if (concurrentlyDisposeDrawnImage) {
				temporaryImage.dispose();
			}
		}

		Image targetCanvas = new Image(display, 5, 5);
		GC targetCanvasGc = new GC(targetCanvas);
		try {
			targetCanvasGc.getGCData().nativeZoom = 200;
			targetCanvasGc.drawImage(image, 0, 0);
			targetCanvasGc.getGCData().nativeZoom = 100;
			targetCanvasGc.drawImage(image, 3, 0);
			ImageData resultImageData = targetCanvas.getImageData(100);

			assertEquals(imageColor.getRGB(), getRGBAtPixel(resultImageData, 0, 0));
			assertEquals(imageColor.getRGB(), getRGBAtPixel(resultImageData, 1, 0));
			assertNotEquals(imageColor.getRGB(), getRGBAtPixel(resultImageData, 2, 0));
			assertEquals(imageColor.getRGB(), getRGBAtPixel(resultImageData, 3, 0));
			assertNotEquals(imageColor.getRGB(), getRGBAtPixel(resultImageData, 4, 0));
		} finally {
			if (!concurrentlyDisposeDrawnImage) {
				temporaryImage.dispose();
			}
			image.dispose();
			targetCanvasGc.dispose();
			gc.dispose();
		}
	}

	@Test
	public void testDisposeDrawnImageBeforeRequestingTargetForOtherZoom() {
		Color imageColor = display.getSystemColor(SWT.COLOR_RED);
		Image imageToDraw = new Image (display, 1, 1);
		GC imageToDrawGc = new GC(imageToDraw);
		imageToDrawGc.setBackground(imageColor);
		imageToDrawGc.fillRectangle(0, 0, 1, 1);

		Image targetCanvas = new Image(display, 5, 5);
		GC targetCanvasGc = new GC(targetCanvas);
		try {
			targetCanvasGc.drawImage(imageToDraw, 0, 0);
			imageToDrawGc.dispose();
			imageToDraw.dispose();
			ImageData resultImageData = targetCanvas.getImageData(200);

			assertEquals(imageColor.getRGB(), getRGBAtPixel(resultImageData, 0, 0));
			assertEquals(imageColor.getRGB(), getRGBAtPixel(resultImageData, 1, 0));
			assertNotEquals(imageColor.getRGB(), getRGBAtPixel(resultImageData, 2, 0));
		} finally {
			targetCanvasGc.dispose();
			targetCanvas.dispose();
		}
	}

	// On Windows, the first used Image constructor creates a DDB, while the second
	// transforms it into a DIB, still pixel RGB and alpha values should be the same.
	@Test
	public void test_getImageData_fromImageForImageDataFromImage() {
		int width = 10;
		int height = 10;
		Color color = new Color(0, 0xff, 0);
		Image image = new Image(display, width, height);
		fillImage(image, color);
		ImageData imageData = image.getImageData();
		ImageData recreatedImageData = new Image(display, imageData).getImageData();
		assertImageDataEqualsIgnoringAlphaInData(imageData, recreatedImageData);
		image.dispose();
	}

	// On Windows, the first used Image constructor creates a DDB, while the second
	// transforms it into a DIB, still pixel RGB and alpha values should be the same.
	@Test
	public void test_getImageData_fromCopiedImage() {
		int width = 10;
		int height = 10;
		Color color = new Color(0, 0xff, 0);
		Image image = new Image(display, width, height);
		fillImage(image, color);
		ImageData imageData = image.getImageData();
		ImageData copiedImageData = new Image(display, image, SWT.IMAGE_COPY).getImageData();
		assertImageDataEqualsIgnoringAlphaInData(imageData, copiedImageData);
		image.dispose();
	}

	private static void fillImage(Image image, Color fillColor) {
		GC gc = new GC(image);
		gc.setBackground(fillColor);
		gc.setForeground(fillColor);
		gc.fillRectangle(image.getBounds());
		gc.dispose();
	}

}