/*******************************************************************************
 * Copyright (c) 2018 Laurent Caron and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Laurent Caron (laurent.caron at gmail dot com) - Conversion to SWT
 *     IBM Corporation - adaptation to GraphicsExample
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

/**
 * This tab displays an animated fire effect using a classic pixel-based
 * flame propagation algorithm with an HSL-based fire palette.
 */
public class FireTab extends AnimatedGraphicsTab {

	private ImageData imageData;
	private Image outputImage;
	private int[] palette;
	private int[][] fire;
	private int lastWidth;
	private int lastHeight;

	public FireTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Fire"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("FireDescription"); //$NON-NLS-1$
	}

	@Override
	public int getInitialAnimationTime() {
		return 10;
	}

	@Override
	public void dispose() {
		if (outputImage != null) {
			outputImage.dispose();
			outputImage = null;
		}
	}

	@Override
	public void next(int width, int height) {
		if (palette == null) {
			return;
		}
		if (width != lastWidth || height != lastHeight) {
			init(width, height);
		}

		// Randomize the bottom row
		for (int x = 0; x < width; x++) {
			fire[x][height - 1] = (int) (Math.abs(32768 + Math.random() * 100) % 256);
		}

		// Fire calculations: average neighboring pixels and decay
		for (int y = 0; y < height - 1; y++) {
			for (int x = 0; x < width; x++) {
				fire[x][y] = (fire[(x - 1 + width) % width][(y + 1) % height]
						+ fire[x % width][(y + 1) % height]
						+ fire[(x + 1) % width][(y + 1) % height]
						+ fire[x % width][(y + 2) % height]) * 32 / 129;
			}
		}

		// Map fire buffer to palette colors
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				imageData.setPixel(x, y, palette[fire[x][y]]);
			}
		}
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (palette == null) {
			init(width, height);
		}

		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		gc.fillRectangle(0, 0, width, height);

		if (imageData == null) {
			return;
		}

		if (outputImage != null) {
			outputImage.dispose();
		}
		outputImage = new Image(gc.getDevice(), imageData);
		gc.drawImage(outputImage, 0, 0);
	}

	private void init(int width, int height) {
		lastWidth = width;
		lastHeight = height;

		fire = new int[width][height];
		palette = new int[256];

		// Generate fire palette using HSL conversion
		for (int x = 0; x < 256; x++) {
			palette[x] = hslToRGB(x / 3, 255, Math.min(255, x * 2));
		}

		imageData = new ImageData(width, height, 24, new PaletteData(0xFF0000, 0xFF00, 0xFF));
	}

	private static int hslToRGB(float hue, float saturation, float lightness) {
		float r, g, b;
		float h = hue / 256.0f;
		float s = saturation / 256.0f;
		float l = lightness / 256.0f;

		if (s == 0) {
			r = g = b = l;
		} else {
			float temp2 = l < 0.5f ? l * (1 + s) : l + s - l * s;
			float temp1 = 2 * l - temp2;
			float tempr = (float) (h + 1.0 / 3.0);
			if (tempr > 1) {
				tempr--;
			}
			float tempg = h;
			float tempb = (float) (h - 1.0 / 3.0);
			if (tempb < 0) {
				tempb++;
			}

			r = hslComponent(temp1, temp2, tempr);
			g = hslComponent(temp1, temp2, tempg);
			b = hslComponent(temp1, temp2, tempb);
		}

		int ri = Math.min(255, Math.round(r * 255f));
		int gi = Math.min(255, Math.round(g * 255f));
		int bi = Math.min(255, Math.round(b * 255f));
		return ri << 16 | gi << 8 | bi;
	}

	private static float hslComponent(float temp1, float temp2, float temp) {
		if (temp < 1.0f / 6.0f) {
			return (float) (temp1 + (temp2 - temp1) * 6.0 * temp);
		} else if (temp < 0.5f) {
			return temp2;
		} else if (temp < 2.0f / 3.0f) {
			return (float) (temp1 + (temp2 - temp1) * (2.0 / 3.0 - temp) * 6.0);
		} else {
			return temp1;
		}
	}
}
