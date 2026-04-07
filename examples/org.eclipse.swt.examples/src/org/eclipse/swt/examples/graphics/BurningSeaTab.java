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
 *     Josh83 - Original Version
 *     Laurent Caron (laurent.caron at gmail dot com) - Conversion to SWT
 *     IBM Corporation - adaptation to GraphicsExample
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

/**
 * This tab displays an animated burning sea effect with fire reflections
 * on water, using sine-based wave distortion and random sparks.
 */
public class BurningSeaTab extends AnimatedGraphicsTab {

	private static final int YSCROLL = 10;

	private int[] sinp;
	private int[] ran1;
	private int[] xfall, yfall, sfall;
	private short t;
	private int r1;
	private ImageData imageData;
	private Image outputImage;
	private int lastWidth;
	private int lastHeight;

	public BurningSeaTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("BurningSea"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("BurningSeaDescription"); //$NON-NLS-1$
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
		if (sinp == null) {
			return;
		}
		if (width != lastWidth || height != lastHeight) {
			init(width, height);
		}

		t += 8;
		if (t > 30000) {
			t = 0;
		}

		// Generate fire at the horizon line
		int horizonLine = Math.min(height - 3, (int) (height * 0.65));
		for (int x = 0; x < width; x++) {
			for (int y = horizonLine; y < Math.min(horizonLine + 3, height); y++) {
				imageData.setPixel(x, y, (int) (Math.random() * 100 + 80));
			}
		}

		// Animate falling sparks
		for (int i = 0; i < 10; i++) {
			yfall[i] += sfall[i];
			if (yfall[i] > horizonLine - 5) {
				xfall[i] = (int) (Math.random() * width);
				yfall[i] = 1;
				sfall[i] = (int) (Math.random() * 4 + 1);
			}
			for (int x = 0; x < sfall[i] + 2; x++) {
				for (int y = 0; y < sfall[i] + 2; y++) {
					int px = x + xfall[i];
					int py = y + yfall[i];
					if (px >= 0 && px < width && py >= 0 && py < height) {
						imageData.setPixel(px, py, (int) (Math.random() * 50 + (sfall[i] << 5)));
					}
				}
			}
		}

		// Fire propagation with blur
		for (int ab = width - 2; ab > 0; ab--) {
			for (int bb = horizonLine; bb > 0; bb--) {
				if (bb < YSCROLL - 1 || bb > YSCROLL + 12) {
					if (imageData.getPixel(ab, bb + 1) > 0 || imageData.getPixel(ab, bb) > 0) {
						r1++;
						if (r1 > 9000) {
							r1 = 0;
						}
						int cblur = imageData.getPixel(ab - 1, bb + 1) + imageData.getPixel(ab + 1, bb + 1)
								+ ran1[r1] * imageData.getPixel(ab, bb + 1) + imageData.getPixel(ab, bb) >> 2;
						if (cblur > 190) {
							cblur = 190;
						}
						imageData.setPixel(ab, bb, cblur);
					}
				}
				if (imageData.getPixel(ab, bb) > 0) {
					imageData.setPixel(ab, bb, imageData.getPixel(ab, bb) - 1);
				}

				// Water reflection with sine distortion
				int abnew = ab + (sinp[(t + bb * 20) % 628] * (bb - (horizonLine - 20) >> 2) >> 7);
				int bbnew = (horizonLine * 2) - bb + 5;

				if (abnew > 0 && abnew < width && bbnew > 0 && bbnew < height) {
					imageData.setPixel(ab, bbnew, imageData.getPixel(Math.min(abnew, width - 1), bb) >> 1);
				}
			}
		}

		// Clear sky area
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < 30; y++) {
				imageData.setPixel(x, y, 0);
			}
		}
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (sinp == null) {
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

		sinp = new int[628];
		for (int i = 0; i < 628; i++) {
			sinp[i] = (int) (Math.sin(i * 0.01) * 128);
		}

		ran1 = new int[10000];
		for (int i = 0; i < 10000; i++) {
			ran1[i] = (int) (Math.random() * 3);
		}

		// Fire palette
		final RGB[] colors = new RGB[256];
		int r = 0, g = 0, b = 0;
		for (int i = 0; i < 256; i++) {
			colors[i] = new RGB(0, 0, 0);
		}
		for (int i = 0; i < 64; i++) {
			colors[i] = new RGB(r, 0, 0);
			r++;
		}
		for (int i = 64; i < 128; i++) {
			colors[i] = new RGB(63, g, 0);
			g++;
		}
		for (int i = 128; i < 192; i++) {
			colors[i] = new RGB(63, 63, b);
			b++;
		}
		b = 0;
		for (int i = 192; i < 256; i++) {
			colors[i] = new RGB(0, 0, b);
			b++;
		}
		// Increase brightness
		for (int i = 1; i < 256; i++) {
			final RGB color = colors[i];
			final float[] temp = color.getHSB();
			colors[i] = new RGB(temp[0], temp[1], Math.min(1.0f, temp[2] * 4f));
		}

		xfall = new int[10];
		yfall = new int[10];
		sfall = new int[10];
		for (int x = 0; x < 10; x++) {
			xfall[x] = (int) (Math.random() * width);
			yfall[x] = (int) (Math.random() * 50);
			sfall[x] = (int) (Math.random() * 4) + 1;
		}

		imageData = new ImageData(width, height, 8, new PaletteData(colors));
		t = 0;
		r1 = 0;
	}
}
