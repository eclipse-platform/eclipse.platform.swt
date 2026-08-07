/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;

/**
 * Encodes an ImageLoader's first ImageData to a raw RGBA byte array.
 * Handles direct and indexed color images, and all SWT transparency types.
 */
public final class RGBAEncoder {

	/**
	 * Returns a byte array of RGBA pixels (width * height * 4).
	 */
	public static byte[] encode(ImageData data) {

		int colorType;

		final var width = data.width;
		final var height = data.height;
		final var transparencyType = data.getTransparencyType();
		if (data.palette.isDirect) {
			if (transparencyType == SWT.TRANSPARENCY_ALPHA) {
				colorType = 6; // RGBA
			} else {
				colorType = 2; // RGB
			}
		} else {
			colorType = 3; // Indexed
		}
		if (!(colorType == 2 || colorType == 3 || colorType == 6)) {
			SWT.error(SWT.ERROR_INVALID_IMAGE);
		}



		final byte[] rgba = new byte[width * height * 4];
		if (colorType == 3) {
			// Indexed color
			final RGB[] palette = data.palette.getRGBs();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					final int pixel = data.getPixel(x, y);
					final RGB rgb = palette[pixel];
					final int offset = (y * width + x) * 4;
					rgba[offset] = (byte) rgb.red;
					rgba[offset + 1] = (byte) rgb.green;
					rgba[offset + 2] = (byte) rgb.blue;
					int a = 255;
					if (transparencyType == SWT.TRANSPARENCY_ALPHA && data.alphaData != null) {
						a = data.alphaData[y * width + x] & 0xFF;
					} else if (transparencyType == SWT.TRANSPARENCY_PIXEL && pixel == data.transparentPixel) {
						a = 0;
					} else if (transparencyType == SWT.TRANSPARENCY_MASK && data.maskData != null) {
						final int maskOffset = y * data.width + x;
						final int maskByte = (data.maskData[maskOffset / 8] >> (7 - (maskOffset % 8))) & 0x1;
						a = maskByte == 0 ? 0 : 255;
					}
					rgba[offset + 3] = (byte) a;
				}
			}
		} else {
			// Direct color
			final int redMask = data.palette.redMask;
			final int redShift = data.palette.redShift;
			final int greenMask = data.palette.greenMask;
			final int greenShift = data.palette.greenShift;
			final int blueMask = data.palette.blueMask;
			final int blueShift = data.palette.blueShift;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					final int pixel = data.getPixel(x, y);
					final int offset = (y * width + x) * 4;
					int r = pixel & redMask;
					r = (redShift < 0) ? r >>> -redShift : r << redShift;
				int g = pixel & greenMask;
				g = (greenShift < 0) ? g >>> -greenShift : g << greenShift;
				int b = pixel & blueMask;
				b = (blueShift < 0) ? b >>> -blueShift : b << blueShift;
				rgba[offset] = (byte) r;
				rgba[offset + 1] = (byte) g;
				rgba[offset + 2] = (byte) b;
				int a = 255;
				if (transparencyType == SWT.TRANSPARENCY_ALPHA && data.alphaData != null) {
					a = data.alphaData[y * width + x] & 0xFF;
				} else if (transparencyType == SWT.TRANSPARENCY_PIXEL && pixel == data.transparentPixel) {
					a = 0;
				} else if (transparencyType == SWT.TRANSPARENCY_MASK && data.maskData != null) {
					final int maskOffset = y * data.width + x;
					final int maskByte = (data.maskData[maskOffset / 8] >> (7 - (maskOffset % 8))) & 0x1;
					a = maskByte == 0 ? 0 : 255;
				} else if (data.alpha != -1) {
					a = data.alpha & 0xFF;
				}
				rgba[offset + 3] = (byte) a;
				}
			}
		}
		return rgba;
	}
}
