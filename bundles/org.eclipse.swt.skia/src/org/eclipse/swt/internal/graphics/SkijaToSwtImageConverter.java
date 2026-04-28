/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors:
 *     SAP SE and others - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.graphics;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

import io.github.humbleui.skija.Bitmap;
import io.github.humbleui.skija.ColorType;

public class SkijaToSwtImageConverter {
	public static ImageData convertSkijaImageToImageData(io.github.humbleui.skija.Image image) {
		try (final Bitmap bm = Bitmap.makeFromImage(image)) {
			final var colType = bm.getColorType();
			final byte[] alphas = new byte[bm.getHeight() * bm.getWidth()];
			final var source = bm.readPixels();
			final byte[] convertedData = new byte[bm.getHeight() * bm.getWidth() * 3];
			final var colorOrder = getPixelOrder(colType);
			for (int y = 0; y < bm.getHeight(); y++) {
				for (int x = 0; x < bm.getWidth(); x++) {
					byte alpha = convertAlphaTo255Range(bm.getAlphaf(x, y));
					final int index = (x + y * bm.getWidth()) * 4;
					final byte red = source[index + colorOrder[0]];
					final byte green = source[index + colorOrder[1]];
					final byte blue = source[index + colorOrder[2]];
					alpha = source[index + colorOrder[3]];
					alphas[x + y * bm.getWidth()] = alpha;
					final int target = (x + y * bm.getWidth()) * 3;
					convertedData[target + 0] = (red);
					convertedData[target + 1] = (green);
					convertedData[target + 2] = (blue);
				}
			}
			final ImageData d = new ImageData(bm.getWidth(), bm.getHeight(), 24, getPaletteData(colType));
			d.data = convertedData;
			d.alphaData = alphas;
			d.bytesPerLine = d.width * 3;
			return d;
		}
	}

	public static byte convertAlphaTo255Range(float alphaF) {
		if (alphaF < 0.0f) {
			alphaF = 0.0f;
		}
		if (alphaF > 1.0f) {
			alphaF = 1.0f;
		}
		return (byte) Math.round(alphaF * 255);
	}

	public static int[] getPixelOrder(ColorType colorType) {
		return switch (colorType) {
		case ALPHA_8 -> new int[] { 0 };
		case RGB_565 -> new int[] { 0, 1, 2 };
		case ARGB_4444 -> new int[] { 1, 2, 3, 0 };
		case RGBA_8888 -> new int[] { 0, 1, 2, 3 };
		case RGB_888X -> new int[] { 0, 1, 2, 3 };
		case BGRA_8888 -> new int[] { 2, 1, 0, 3 };
		case RGBA_1010102 -> new int[] { 0, 1, 2, 3 };
		case BGRA_1010102 -> new int[] { 2, 1, 0, 3 };
		case RGB_101010X -> new int[] { 0, 1, 2, 3 };
		case BGR_101010X -> new int[] { 2, 1, 0, 3 };
		case RGBA_F16NORM -> new int[] { 0, 1, 2, 3 };
		case RGBA_F16 -> new int[] { 0, 1, 2, 3 };
		case RGBA_F32 -> new int[] { 0, 1, 2, 3 };
		case R8G8_UNORM -> new int[] { 0, 1 };
		case A16_FLOAT -> new int[] { 0 };
		case R16G16_FLOAT -> new int[] { 0, 1 };
		case A16_UNORM -> new int[] { 0 };
		case R16G16_UNORM -> new int[] { 0, 1 };
		case R16G16B16A16_UNORM -> new int[] { 0, 1, 2, 3 };
		default -> throw new IllegalArgumentException("Unknown Skija ColorType: " + colorType);//$NON-NLS-1$
		};
	}

	public static PaletteData getPaletteData(ColorType colorType) {
		return switch (colorType) {
		case ALPHA_8 -> new PaletteData(new RGB[] { new RGB(255, 255, 255), new RGB(0, 0, 0) });
		case RGB_565 -> new PaletteData(0xF800, 0x07E0, 0x001F);
		case ARGB_4444 -> new PaletteData(0x0F00, 0x00F0, 0x000F);
		case RGBA_8888 -> new PaletteData(0xFF000000, 0x00FF0000, 0x0000FF00);
		case BGRA_8888 -> new PaletteData(0x0000FF00, 0x00FF0000, 0xFF000000);
		case RGBA_F16 -> new PaletteData(new RGB[] { new RGB(255, 0, 0), new RGB(0, 255, 0), new RGB(0, 0, 255) });
		case RGBA_F32 ->
		new PaletteData(new RGB[] { new RGB(255, 165, 0), new RGB(0, 255, 255), new RGB(128, 0, 128) });
		default -> throw new IllegalArgumentException("Unknown Skija ColorType: " + colorType);//$NON-NLS-1$
		};
	}

	public static int getImageDepth(ColorType colorType) {
		return switch (colorType) {
		case ALPHA_8 -> 8;
		case RGB_565 -> 16;
		case ARGB_4444 -> 16;
		case RGBA_8888 -> 32;
		case BGRA_8888 -> 32;
		case RGBA_F16 -> 64;
		case RGBA_F32 -> 128;
		default -> 32;
		};
	}
}
