/*******************************************************************************
 * Copyright (c) 2022 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.tests.graphics;

import static org.junit.Assert.assertArrayEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.tests.junit.SwtTestUtil;
import org.eclipse.swt.widgets.Display;

/**
 * Gives access to protected methods for JUnit test purposes
 */
public class ImageDataTestHelper {
	private static boolean TEST_BLIT_SHOW_IMAGES = false;

	public static int LSB_FIRST = 0;
	public static int MSB_FIRST = 1;

	public static class BlitTestInfo {
		public final int depth;
		public final int scale;
		public final int byteOrder;
		public final boolean isDirect;

		public final ImageData imageData;
		public final byte[] paletteR;
		public final byte[] paletteG;
		public final byte[] paletteB;

		public BlitTestInfo(int depth, int scale, int byteOrder, boolean isDirect, ImageData imageData) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			this.depth = depth;
			this.scale = scale;
			this.byteOrder = byteOrder;
			this.isDirect = isDirect;

			this.imageData = (imageData != null)
				? imageData
				: makeTestImageData(depth, scale, byteOrder, isDirect, false);

			if (!isDirect) {
				RGB[] rgbs = this.imageData.palette.getRGBs();
				paletteR = new byte[rgbs.length];
				paletteG = new byte[rgbs.length];
				paletteB = new byte[rgbs.length];
				for (int i = 0; i < rgbs.length; i++) {
					RGB rgb = rgbs[i];
					if (rgb == null) continue;
					paletteR[i] = (byte) rgb.red;
					paletteG[i] = (byte) rgb.green;
					paletteB[i] = (byte) rgb.blue;
				}
			} else {
				paletteR  = null;
				paletteG  = null;
				paletteB  = null;
			}
		}

		public BlitTestInfo(int depth, int scale, int byteOrder, boolean isDirect) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			this(depth, scale, byteOrder, isDirect, null);
		}

		@Override
		public String toString () {
			return "{depth=" + depth + " scale=" + scale + " byteOrder=" + byteOrder + " isDirect=" + isDirect + "}";
		}
	}

	public static PaletteData makeTestPalette(int depth, boolean isDirect) {
		if (isDirect) {
			// For test purposes, 8bpp is RGB222 (with 2 spare bits) and
			// 16bpp is RGB444 (with 4 spare bits). This allows to convert
			// colors between all formats without any data loss.
			switch (depth) {
				case 8:
					return new PaletteData(0x03, 0x0C, 0x30);
				case 16:
					return new PaletteData(0xF000, 0x0F00, 0x00F0);
				case 24:
					return new PaletteData(0xFF, 0xFF00, 0xFF0000);
				case 32:
					return new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
			}

			throw new IllegalArgumentException();
		} else {
			// Only use 2 colors to be able to fit into 1bpp indexed format.
			// Use color components that are supported in 2-bits-per-component
			// direct palette (0x00, 0x55, 0xAA, 0xFF). Arrange color components
			// in such a way that accidental match when reading image data at
			// wrong offset is the least possible.
			RGB[] rgbs = new RGB[2];
			rgbs[0] = new RGB(0x55, 0xAA, 0xFF);
			rgbs[1] = new RGB(0x00, 0xFF, 0xAA);
			return new PaletteData(rgbs);
		}
	}

	public static ImageData makeTestImageData(int depth, int scale, int byteOrder, boolean isDirect, boolean isClean) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Use fixed seed to make test results reproducible
		Random random = new Random(0);

		final int width = 8;
		final int height = 4;
		final int bytesPad = random.nextInt(8) * 2; // To also test handling of padded lines
		final int bytesPerLine = ((width * depth / 8) * scale) + bytesPad;
		final PaletteData palette = makeTestPalette(depth, isDirect);
		final byte[] data = new byte[bytesPerLine * (height * scale)];
		ImageData imageData = new ImageData(width*scale, height*scale, depth, palette, bytesPerLine, data);

		if (isClean) {
			return imageData;
		}

		// Cache possible pixel values for direct palette
		int[] pixelValues = null;
		PaletteData indexedPalette = makeTestPalette(1, false);
		if (isDirect) {
			pixelValues = new int[indexedPalette.colors.length];
			for (int iColor = 0; iColor < pixelValues.length; iColor++) {
				pixelValues[iColor] = palette.getPixel(indexedPalette.colors[iColor]);
			}
		}

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int colorIndex = random.nextInt(indexedPalette.colors.length);
				int pixel = isDirect ? pixelValues[colorIndex] : colorIndex;

				for (int yStep = 0; yStep < scale; yStep++) {
					for (int xStep = 0; xStep < scale; xStep++) {
						imageData.setPixel(x*scale + xStep, y*scale + yStep, pixel);
					}
				}
			}
		}

		// setPixel() only supports one of {MSB_FIRST, LSB_FIRST} for each format.
		// If it's the other, convert.
		Method method = ImageData.class.getDeclaredMethod("getByteOrder");
		method.setAccessible(true);
		int defaultByteOrderForDepth = (int) method.invoke(imageData);
		if (defaultByteOrderForDepth != byteOrder) {
			BlitTestInfo actualInfo = new BlitTestInfo(depth, scale, defaultByteOrderForDepth, isDirect, imageData);
			imageData = blit(actualInfo, depth, scale, byteOrder, isDirect).imageData;
		}

		return imageData;
	}

	public static BlitTestInfo blit(BlitTestInfo srcInfo, int dstInfo_depth, int dstInfo_scale, int dstInfo_byteOrder, boolean dstInfo_isDirect) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		ImageData src = srcInfo.imageData;
		ImageData dst = makeTestImageData(dstInfo_depth, dstInfo_scale, dstInfo_byteOrder, dstInfo_isDirect, true);


		if (srcInfo.isDirect) {
			if (dstInfo_isDirect) {
				Method blitMethod = ImageData.class.getDeclaredMethod("blit", byte[].class, int.class,
						int.class, int.class, int.class, int.class, int.class, int.class, int.class, byte[].class,
						int.class, int.class, int.class, int.class, int.class, int.class, int.class, int.class,
						boolean.class, boolean.class);
				blitMethod.setAccessible(true);
				blitMethod.invoke(null,
					src.data, src.depth, src.bytesPerLine, srcInfo.byteOrder, src.width, src.height, src.palette.redMask, src.palette.greenMask, src.palette.blueMask,
					dst.data, dst.depth, dst.bytesPerLine, dstInfo_byteOrder, dst.width, dst.height, dst.palette.redMask, dst.palette.greenMask, dst.palette.blueMask,
					false, false);
			} else {
				throw new UnsupportedOperationException();
			}
		} else {
			if (dstInfo_isDirect) {
				Method blitMethod = ImageData.class.getDeclaredMethod("blit", int.class, int.class, byte[].class,
						int.class, int.class, int.class, byte[].class, byte[].class, byte[].class, byte[].class,
						int.class, int.class, int.class, int.class, int.class, int.class);
				blitMethod.setAccessible(true);
				blitMethod.invoke(null,
					src.width, src.height,
					src.data, src.depth, src.bytesPerLine, srcInfo.byteOrder, srcInfo.paletteR, srcInfo.paletteG, srcInfo.paletteB,
					dst.data, dst.depth, dst.bytesPerLine, dstInfo_byteOrder, dst.palette.redMask, dst.palette.greenMask, dst.palette.blueMask);
			} else {
				Method blitMethod = ImageData.class.getDeclaredMethod("blit", byte[].class, int.class, int.class,
						int.class, int.class, int.class, byte[].class, int.class, int.class, int.class, int.class,
						int.class, boolean.class, boolean.class);
				blitMethod.setAccessible(true);
				blitMethod.invoke(null,
					src.data, src.depth, src.bytesPerLine, srcInfo.byteOrder, src.width, src.height,
					dst.data, dst.depth, dst.bytesPerLine, dstInfo_byteOrder, dst.width, dst.height,
					false, false);
			}
		}

		return new BlitTestInfo(dstInfo_depth, dstInfo_scale, dstInfo_byteOrder, dstInfo_isDirect, dst);
	}

	public static void assertImageDataEqual(ImageData source, ImageData actual, ImageData expected) {
		if (TEST_BLIT_SHOW_IMAGES) {
			Image[] images = new Image[3];
			images[0] = new Image(Display.getCurrent(), source);
			images[1] = new Image(Display.getCurrent(), actual);
			images[2] = new Image(Display.getCurrent(), expected);
			SwtTestUtil.debugDisplayImages(images, 3);
			images[0].dispose();
			images[1].dispose();
			images[2].dispose();
		}

		assertArrayEquals(actual.data, expected.data);
	}
}