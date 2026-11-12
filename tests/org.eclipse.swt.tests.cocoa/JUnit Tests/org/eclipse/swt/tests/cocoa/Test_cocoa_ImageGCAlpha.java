/*******************************************************************************
 * Copyright (c) 2026 vogella GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.cocoa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.junit.jupiter.api.Test;

public class Test_cocoa_ImageGCAlpha {

	// probes stay far from the fill edge, so a HiDPI downscale cannot blend them
	private static final int SIZE = 16;

	@Test
	public void globalAlphaFollowsGCDrawing() {
		ImageData source = redImageData();
		source.alpha = 128;
		assertAlphaAfterHalfFill(source);
	}

	@Test
	public void alphaDataFollowsGCDrawing() {
		ImageData source = redImageData();
		source.alphaData = new byte[SIZE * SIZE];
		Arrays.fill(source.alphaData, (byte) 128);
		assertAlphaAfterHalfFill(source);
	}

	@Test
	public void transparentPixelSurvivesGCDrawing() {
		ImageData source = redImageData();
		int white = source.palette.getPixel(new RGB(255, 255, 255));
		for (int y = 0; y < SIZE; y++) {
			for (int x = SIZE / 2; x < SIZE; x++) {
				source.setPixel(x, y, white);
			}
		}
		source.transparentPixel = white;
		Image image = new Image(Display.getDefault(), source);
		try {
			fill(image, new Color(0, 0, 255), 0);
			ImageData data = image.getImageData();
			assertEquals(new RGB(0, 0, 255), data.palette.getRGB(data.getPixel(0, 1)));
			ImageData mask = data.getTransparencyMask();
			assertEquals(1, mask.getPixel(0, 1), "a pixel the GC filled");
			assertEquals(0, mask.getPixel(SIZE - 1, 1), "a transparent pixel the GC left alone");
		} finally {
			image.dispose();
		}
	}

	private static ImageData redImageData() {
		ImageData data = new ImageData(SIZE, SIZE, 24, new PaletteData(0xFF0000, 0xFF00, 0xFF));
		int red = data.palette.getPixel(new RGB(255, 0, 0));
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				data.setPixel(x, y, red);
			}
		}
		return data;
	}

	private static void assertAlphaAfterHalfFill(ImageData source) {
		Display display = Display.getDefault();
		Image image = new Image(display, source);
		try {
			fill(image, new Color(0, 0, 255), 0);
			ImageData data = image.getImageData();
			assertEquals(new RGB(0, 0, 255), data.palette.getRGB(data.getPixel(0, 1)));
			assertEquals(255, alpha(data, 0, 1), "a pixel the GC filled opaque");
			assertEquals(128, alpha(data, SIZE - 1, 1), 1, "a pixel the GC left alone");
			assertEquals(255, data.palette.getRGB(data.getPixel(SIZE - 1, 1)).red, 1, "the color of a pixel the GC left alone");

			fill(image, new Color(0, 255, 0), 1);
			data = image.getImageData();
			assertEquals(new RGB(0, 255, 0), data.palette.getRGB(data.getPixel(1, 1)));
			assertEquals(255, alpha(data, 1, 1), "a pixel a second GC filled opaque");
			assertEquals(128, alpha(data, SIZE - 1, 1), 1, "a pixel both GCs left alone");
			assertEquals(255, data.palette.getRGB(data.getPixel(SIZE - 1, 1)).red, 1, "the color of a pixel both GCs left alone");
		} finally {
			image.dispose();
		}
	}

	private static void fill(Image image, Color color, int x) {
		GC gc = new GC(image);
		try {
			gc.setBackground(color);
			gc.fillRectangle(x, 0, SIZE / 2 - x, SIZE);
		} finally {
			gc.dispose();
		}
	}

	// getAlpha ignores the global alpha
	private static int alpha(ImageData data, int x, int y) {
		return data.alphaData == null && data.alpha != -1 ? data.alpha : data.getAlpha(x, y);
	}
}
