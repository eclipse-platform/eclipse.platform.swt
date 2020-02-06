/*******************************************************************************
 * Copyright (c) 2020 Paul Pazderski and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Paul Pazderski - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.function.BiFunction;

import org.eclipse.swt.graphics.ImageData;

public class ImageTestUtil {

	/**
	 * Check if two image data represent the same image. This method try its best to
	 * ignore how the data is organized and only compare the effective image. E.g.
	 * 8bit and 32bit image data can be equal. A direct and indirect palette image
	 * can be equal. Images with and without alpha can be equal as long as the alpha
	 * image has full opacity for each pixel.
	 */
	public static void assertImagesEqual(ImageData expected, ImageData actual) {
		assertImagesEqual(new ImageData[] { expected }, new ImageData[] { actual });
	}

	/**
	 * Inversion of {@link #assertImagesEqual(ImageData, ImageData)}.
	 */
	public static void assertImagesNotEqual(ImageData expected, ImageData actual) {
		assertImagesNotEqual(new ImageData[] { expected }, new ImageData[] { actual });
	}

	/**
	 * Inversion of {@link #assertImagesEqual(ImageData[], ImageData[])}.
	 */
	public static void assertImagesNotEqual(ImageData[] expected, ImageData[] actual) {
		try {
			assertImagesEqual(expected, actual);
		} catch (AssertionError e) {
			return;
		}
		fail("Images are equal.");
	}

	/**
	 * Check if two image data represent the same image. This method try its best to
	 * ignore how the data is organized and only compare the effective image. E.g.
	 * 8bit and 32bit image data can be equal. A direct and indirect palette image
	 * can be equal. Images with and without alpha can be equal as long as the alpha
	 * image has full opacity for each pixel.
	 */
	public static void assertImagesEqual(ImageData[] expected, ImageData[] actual) {
		assertNotNull(expected);
		assertNotNull(actual);
		assertEquals("Different number of frames.", expected.length, actual.length);
		BiFunction<String, Integer, String> formatMsg = (msg, i) -> expected.length == 1 ? msg + "."
				: msg + " in frame " + i + ".";
		for (int i = 0; i < expected.length; i++) {
			assertEquals(formatMsg.apply("Different width", i), expected[i].width, actual[i].width);
			assertEquals(formatMsg.apply("Different height", i), expected[i].height, actual[i].height);
			if (expected[i].transparentPixel == -1 || (actual[i].palette != null && actual[i].palette.isDirect)) {
				assertEquals("Wrong transparent pixel.", expected[i].transparentPixel, actual[i].transparentPixel);
			} else {
				assertEquals("Wrong transparent pixel.", expected[i].palette.getRGBs()[expected[i].transparentPixel],
						actual[i].palette.getRGBs()[actual[i].transparentPixel]);
			}
			// improve performance in case the frame has a global fixed alpha value
			int expectedFixAlpha = getEffectiveAlpha(expected[i], -1, -1);
			int actualFixAlpha = getEffectiveAlpha(actual[i], -1, -1);
			int[] expectedLine = new int[expected[i].width];
			int[] actualLine = new int[actual[i].width];
			for (int y = 0; y < expected[i].height; y++) {
				expected[i].getPixels(0, y, expected[i].width, expectedLine, 0);
				actual[i].getPixels(0, y, actual[i].width, actualLine, 0);
				for (int x = 0; x < expected[i].width; x++) {
					assertEquals(formatMsg.apply("Different color at x=" + x + ", y=" + y, i),
							expected[i].palette.getRGB(expectedLine[x]), actual[i].palette.getRGB(actualLine[x]));
					int expectedAlpha = expectedFixAlpha < 0 ? getEffectiveAlpha(expected[i], x, y) : expectedFixAlpha;
					int actualAlpha = actualFixAlpha < 0 ? getEffectiveAlpha(actual[i], x, y) : actualFixAlpha;
					if (expectedAlpha != actualAlpha)
						assertEquals(formatMsg.apply("Different alpha at x=" + x + ", y=" + y, i), expectedAlpha,
								actualAlpha);
				}
			}
		}
	}

	/**
	 * Get the effective alpha value for specific pixel. Considers all possible
	 * sources of alpha information. Set coordinates to negative value to check if
	 * data has a single global alpha value for all pixel.
	 *
	 * @param data the image data
	 * @param x    image x coordinate
	 * @param y    image y coordinate
	 * @return the alpha value for the requested pixel. <code>255</code> if data has
	 *         no alpha. If x or y are negative returns the global alpha value valid
	 *         for all pixel or <code>-1</code> if no global alpha exist.
	 */
	private static int getEffectiveAlpha(ImageData data, int x, int y) {
		if (data.alpha >= 0) { // global alpha value
			return data.alpha;
		} else if (data.alphaData != null) { // separate alpha data
			if (x < 0 || y < 0) {
				return -1;
			}
			return data.getAlpha(x, y);
		} else { // build from data
			int alphaMask = ~(data.palette.redMask | data.palette.greenMask | data.palette.blueMask);
			if (data.depth < 32) {
				alphaMask &= (1 << data.depth) - 1;
			}
			if (alphaMask == 0) {
				return 255; // no alpha in pixel value; return default alpha
			}
			if (x < 0 || y < 0) {
				return -1;
			}
			int alpha = data.getPixel(x, y) & alphaMask;
			int mask = ~1;
			while (alphaMask != 0) {
				if ((alphaMask & 1) == 0) {
					alpha = (alpha & ~mask) | (alpha & mask) >>> 1;
					mask >>>= 1;
				}
				alphaMask >>>= 1;
			}
			return 0;
		}
	}
}
