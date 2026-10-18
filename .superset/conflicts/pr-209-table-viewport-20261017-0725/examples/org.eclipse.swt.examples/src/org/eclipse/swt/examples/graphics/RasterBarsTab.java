/*******************************************************************************
 * Copyright (c) 2019 Laurent Caron and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Calogiuri Enzo Antonio (Insolit Dust) - Original Version
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
 * This tab displays an animated raster bars effect where colored bars
 * sweep across the screen following sine wave patterns, creating a
 * classic demoscene visual.
 */
public class RasterBarsTab extends AnimatedGraphicsTab {

	private ImageData imageData;
	private Image outputImage;
	private int[] palette;
	private int offset1, offset2;
	private int lastWidth;
	private int lastHeight;

	public RasterBarsTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("RasterBars"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("RasterBarsDescription"); //$NON-NLS-1$
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

		drawBackground(width, height);
		drawBars(width, height);
		offset1 += 2;
		offset2 += 2;
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
		createColors();
		imageData = new ImageData(width, height, 24, new PaletteData(0xFF0000, 0xFF00, 0xFF));
		offset1 = 0;
		offset2 = 0;
	}

	private void createColors() {
		palette = new int[256];
		for (int i = 0; i < 80; i++) {
			palette[i] = createColor(i, i * 2, i * 3);
		}
		for (int i = 80; i < 160; i++) {
			palette[i] = createColor(i - 80, (i - 80) * 3, (i - 80) * 2);
		}
		for (int i = 160; i < 256; i++) {
			palette[i] = createColor(i - 160, i - 160, i - 160);
		}
	}

	private static int createColor(int r, int g, int b) {
		return r << 16 | g << 8 | b;
	}

	private void drawBackground(int width, int height) {
		float color = 160f;
		float inc = 96f / height;

		for (int a = 0; a < height; a++) {
			drawLine(0, a, width, a, (int) color, width, height);
			color += inc;
		}
	}

	private void drawBars(int width, int height) {
		int color = 0;
		for (int c = 0; c < height; c += 3) {
			int d = (int) (150 + 60 * Math.sin((c + offset1) * 3.14 / 180) + 50 * Math.cos((c + offset2) * 4.14 / 180));

			for (int a = d; a < d + 20; a++) {
				drawLine(a, c, a, height, color, width, height);
			}

			d = (int) (145 + 100 * Math.sin((c + offset2) * 4.14 / 180) + 50 * Math.cos((c + offset2) * 4.14 / 180));

			for (int a = d; a < d + 35; a++) {
				drawLine(a, c + 3, a, height, color + 80, width, height);
			}

			color++;
		}
	}

	private void drawLine(int x1, int y1, int x2, int y2, int color, int width, int height) {
		if (color < 0 || color >= palette.length) {
			return;
		}

		int dx = Math.abs(x1 - x2);
		int dy = Math.abs(y1 - y2);
		int cxy = 0;
		int dxy;

		if (dy > dx) {
			if (y1 > y2) {
				int temp = y2; y2 = y1; y1 = temp;
				temp = x2; x2 = x1; x1 = temp;
			}
			dxy = x1 > x2 ? -1 : 1;

			for (int y = y1; y < y2; y++) {
				cxy += dx;
				if (cxy >= dy) {
					x1 += dxy;
					cxy -= dy;
				}
				if (x1 >= 0 && x1 < width && y >= 0 && y < height) {
					imageData.setPixel(x1, y, palette[color]);
				}
			}
		} else {
			if (x1 > x2) {
				int temp = y2; y2 = y1; y1 = temp;
				temp = x2; x2 = x1; x1 = temp;
			}
			dxy = y1 > y2 ? -1 : 1;

			for (int x = x1; x < x2; x++) {
				cxy += dy;
				if (cxy >= dx) {
					y1 += dxy;
					cxy -= dx;
				}
				if (x >= 0 && x < width && y1 >= 0 && y1 < height) {
					imageData.setPixel(x, y1, palette[color]);
				}
			}
		}
	}
}
