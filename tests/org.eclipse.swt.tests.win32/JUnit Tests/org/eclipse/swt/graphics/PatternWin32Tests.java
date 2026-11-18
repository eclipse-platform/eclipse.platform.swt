/*******************************************************************************
 * Copyright (c) 2026 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Vector Informatik GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.widgets.Display;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Automated Tests for class org.eclipse.swt.graphics.Pattern
 * for Windows specific behavior
 *
 * @see org.eclipse.swt.graphics.Pattern
 */
@SuppressWarnings("restriction")
public class PatternWin32Tests {

	private static final int COLOR_TOLERANCE = 8;

	/**
	 * The zoom at which coordinates in points and pixels coincide, so that the
	 * sampled positions are independent of the zoom the machine runs at.
	 */
	private static final int UNSCALED_ZOOM = 100;

	private static final int WIDTH = 100;
	private static final int HEIGHT = 20;

	private static final int TILE_SIZE = 4;

	private static final RGB RED = new RGB(255, 0, 0);
	private static final RGB GREEN = new RGB(0, 255, 0);
	private static final RGB BLUE = new RGB(0, 0, 255);
	private static final RGB WHITE = new RGB(255, 255, 255);

	private static Display display;

	@BeforeAll
	public static void setUpClass() {
		display = Display.getDefault();
	}

	@AfterAll
	public static void tearDownClass() {
		display.dispose();
	}

	@Test
	public void testGradientPatternFillsAreaWithGradientBetweenItsColors() {
		paintWithBackgroundPattern(
				() -> new Pattern(display, 0, 0, WIDTH, 0, color(SWT.COLOR_RED), color(SWT.COLOR_BLUE)), target -> {
					ImageData imageData = target.getImageData(UNSCALED_ZOOM);
					assertColorEquals(RED, colorAt(imageData, 0, HEIGHT / 2), "start of gradient");
					assertColorEquals(new RGB(127, 0, 128), colorAt(imageData, WIDTH / 2, HEIGHT / 2),
							"middle of gradient");
					assertColorEquals(BLUE, colorAt(imageData, WIDTH - 1, HEIGHT / 2), "end of gradient");
				});
	}

	@Test
	public void testGradientPatternWithEqualStartAndEndPointFillsAreaWithItsFirstColor() {
		paintWithBackgroundPattern(
				() -> new Pattern(display, 10, 10, 10, 10, color(SWT.COLOR_RED), color(SWT.COLOR_BLUE)), target -> {
					ImageData imageData = target.getImageData(UNSCALED_ZOOM);
					assertColorEquals(RED, colorAt(imageData, 0, HEIGHT / 2), "solid fill at the start");
					assertColorEquals(RED, colorAt(imageData, WIDTH / 2, HEIGHT / 2), "solid fill in the middle");
					assertColorEquals(RED, colorAt(imageData, WIDTH - 1, HEIGHT / 2), "solid fill at the end");
				});
	}

	@Test
	public void testTranslucentGradientPatternBlendsWithTheUnderlyingContent() {
		paintWithBackgroundPattern(gc -> {
			gc.setBackground(color(SWT.COLOR_WHITE));
			gc.fillRectangle(0, 0, WIDTH, HEIGHT);
		}, () -> new Pattern(display, 0, 0, WIDTH, 0, color(SWT.COLOR_RED), 255, color(SWT.COLOR_BLUE), 0),
				(target, pattern) -> {
					ImageData imageData = target.getImageData(UNSCALED_ZOOM);
					assertColorEquals(RED, colorAt(imageData, 0, HEIGHT / 2), "opaque start of gradient");
					assertColorEquals(WHITE, colorAt(imageData, WIDTH - 1, HEIGHT / 2),
							"fully transparent end of gradient");
				});
	}

	@Test
	public void testGradientPatternIsAlsoAppliedAsForegroundPattern() {
		Image target = null;
		GC gc = null;
		Pattern pattern = null;
		try {
			target = new Image(display, WIDTH, HEIGHT);
			gc = new GC(target);
			pattern = new Pattern(display, 0, 0, WIDTH, 0, color(SWT.COLOR_RED), color(SWT.COLOR_BLUE));
			gc.setForegroundPattern(pattern);
			gc.setLineWidth(HEIGHT);
			gc.drawLine(0, HEIGHT / 2, WIDTH, HEIGHT / 2);

			ImageData imageData = target.getImageData(UNSCALED_ZOOM);
			assertColorEquals(new RGB(250, 0, 5), colorAt(imageData, 2, HEIGHT / 2), "start of the painted line");
			assertColorEquals(new RGB(5, 0, 250), colorAt(imageData, WIDTH - 3, HEIGHT / 2), "end of the painted line");
		} finally {
			dispose(gc, pattern, target);
		}
	}

	@Test
	public void testImagePatternTilesTheImageOverTheFilledArea() {
		Image tile = createTile();
		try {
			paintWithBackgroundPattern(() -> new Pattern(display, tile), target -> {
				ImageData imageData = target.getImageData(UNSCALED_ZOOM);
				assertTiling(imageData, tileSizeAt(UNSCALED_ZOOM), "tiled fill");
			});
		} finally {
			tile.dispose();
		}
	}

	@Test
	public void testPatternWithoutExplicitDeviceUsesTheCurrentDevice() {
		paintWithBackgroundPattern(
				() -> new Pattern(null, 0, 0, WIDTH, 0, color(SWT.COLOR_RED), color(SWT.COLOR_BLUE)), target -> {
					ImageData imageData = target.getImageData(UNSCALED_ZOOM);
					assertColorEquals(RED, colorAt(imageData, 0, HEIGHT / 2), "start of gradient");
					assertColorEquals(BLUE, colorAt(imageData, WIDTH - 1, HEIGHT / 2), "end of gradient");
				});
	}

	@Test
	public void testGradientPatternIsScaledToTheZoomItIsPaintedAt() {
		paintWithBackgroundPattern(
				() -> new Pattern(display, 0, 0, WIDTH, 0, color(SWT.COLOR_RED), color(SWT.COLOR_BLUE)), target -> {
					ImageData scaledImageData = target.getImageData(scaledZoom());
					assertEquals(WIDTH * scaledZoom() / UNSCALED_ZOOM, scaledImageData.width,
							"the image data must be scaled to the requested zoom");
					int middleY = scaledImageData.height / 2;
					assertColorEquals(RED, colorAt(scaledImageData, 0, middleY), "start of scaled gradient");
					assertColorEquals(new RGB(127, 0, 128),
							colorAt(scaledImageData, scaledImageData.width / 2, middleY), "middle of scaled gradient");
					assertColorEquals(BLUE, colorAt(scaledImageData, scaledImageData.width - 1, middleY),
							"end of scaled gradient");
				});
	}

	@Test
	public void testImagePatternIsScaledToTheZoomItIsPaintedAt() {
		Image tile = createTile();
		try {
			paintWithBackgroundPattern(() -> new Pattern(display, tile), target -> {
				ImageData scaledImageData = target.getImageData(scaledZoom());
				assertTiling(scaledImageData, tileSizeAt(scaledZoom()), "scaled tiled fill");
			});
		} finally {
			tile.dispose();
		}
	}

	@Test
	public void testGradientPatternIsStillPaintedAfterItHasBeenDisposed() {
		paintWithDisposedBackgroundPattern(
				() -> new Pattern(display, 0, 0, WIDTH, 0, color(SWT.COLOR_RED), color(SWT.COLOR_BLUE)), target -> {
					ImageData scaledImageData = target.getImageData(scaledZoom());
					int middleY = scaledImageData.height / 2;
					assertColorEquals(RED, colorAt(scaledImageData, 0, middleY), "start of gradient");
					assertColorEquals(BLUE, colorAt(scaledImageData, scaledImageData.width - 1, middleY),
							"end of gradient");
				});
	}

	@Test
	public void testImagePatternIsStillPaintedAfterItHasBeenDisposed() {
		Image tile = createTile();
		try {
			paintWithDisposedBackgroundPattern(() -> new Pattern(display, tile), target -> {
				ImageData scaledImageData = target.getImageData(scaledZoom());
				assertTiling(scaledImageData, tileSizeAt(scaledZoom()), "tiled fill");
			});
		} finally {
			tile.dispose();
		}
	}

	/**
	 * A zoom the target image has no handle for yet, so that reading image data at
	 * it makes the GC repaint, and with it recreate the pattern, at that zoom. A
	 * painted image tolerates only one such read, which is why no test reads image
	 * data at two zooms other than the device zoom.
	 */
	private static int scaledZoom() {
		return 2 * DPIUtil.getDeviceZoom();
	}

	private static int tileSizeAt(int zoom) {
		return TILE_SIZE * zoom / UNSCALED_ZOOM;
	}

	private static Color color(int systemColorId) {
		return display.getSystemColor(systemColorId);
	}

	/**
	 * Creates a tile whose left half is green and whose right half is blue, so that
	 * the period and the phase of the tiling are visible in the painted result.
	 */
	private static Image createTile() {
		Image image = null;
		GC gc = null;
		boolean created = false;
		try {
			image = new Image(display, TILE_SIZE, TILE_SIZE);
			gc = new GC(image);
			gc.setBackground(color(SWT.COLOR_GREEN));
			gc.fillRectangle(0, 0, TILE_SIZE / 2, TILE_SIZE);
			gc.setBackground(color(SWT.COLOR_BLUE));
			gc.fillRectangle(TILE_SIZE / 2, 0, TILE_SIZE / 2, TILE_SIZE);
			created = true;
		} finally {
			dispose(gc);
			if (!created) {
				dispose(image);
			}
		}
		return image;
	}

	private static void assertTiling(ImageData imageData, int tileSize, String message) {
		int middleY = imageData.height / 2;
		assertColorEquals(GREEN, colorAt(imageData, 0, middleY), message + ": first half of the first tile");
		assertColorEquals(BLUE, colorAt(imageData, tileSize - 1, middleY), message + ": second half of the first tile");
		assertColorEquals(GREEN, colorAt(imageData, tileSize, middleY), message + ": first half of the second tile");
		assertColorEquals(BLUE, colorAt(imageData, imageData.width - 1, middleY),
				message + ": second half of the last tile");
	}

	private static void paintWithBackgroundPattern(Supplier<Pattern> patternFactory, Consumer<Image> inspection) {
		paintWithBackgroundPattern(gc -> {}, patternFactory, (target, pattern) -> inspection.accept(target));
	}

	private static void paintWithDisposedBackgroundPattern(Supplier<Pattern> patternFactory,
			Consumer<Image> inspection) {
		paintWithBackgroundPattern(gc -> {}, patternFactory, (target, pattern) -> {
			pattern.dispose();
			inspection.accept(target);
		});
	}

	/**
	 * Paints the whole area of an image with the given pattern and hands the image
	 * to the inspection, with the GC still alive so that image data can be read at
	 * a zoom the image has no handle for yet.
	 */
	private static void paintWithBackgroundPattern(Consumer<GC> underlyingContent, Supplier<Pattern> patternFactory,
			BiConsumer<Image, Pattern> inspection) {
		Image target = null;
		GC gc = null;
		Pattern pattern = null;
		try {
			target = new Image(display, WIDTH, HEIGHT);
			gc = new GC(target);
			underlyingContent.accept(gc);
			pattern = patternFactory.get();
			gc.setBackgroundPattern(pattern);
			gc.fillRectangle(0, 0, WIDTH, HEIGHT);
			inspection.accept(target, pattern);
		} finally {
			dispose(gc, pattern, target);
		}
	}

	private static void dispose(Resource... resources) {
		for (Resource resource : resources) {
			if (resource != null) {
				resource.dispose();
			}
		}
	}

	private static RGB colorAt(ImageData imageData, int x, int y) {
		return imageData.palette.getRGB(imageData.getPixel(x, y));
	}

	private static void assertColorEquals(RGB expected, RGB actual, String message) {
		assertTrue(isWithinTolerance(expected, actual), () -> message + ": expected " + expected + " but was " + actual
				+ " (tolerance " + COLOR_TOLERANCE + " per channel)");
	}

	private static boolean isWithinTolerance(RGB expected, RGB actual) {
		return Math.abs(expected.red - actual.red) <= COLOR_TOLERANCE
				&& Math.abs(expected.green - actual.green) <= COLOR_TOLERANCE
				&& Math.abs(expected.blue - actual.blue) <= COLOR_TOLERANCE;
	}

}
