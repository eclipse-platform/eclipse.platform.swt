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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/**
 * This tab displays an animated sky/landscape effect using perspective-corrected
 * texture mapping with scrolling sprites and shadows on a floor and ceiling.
 */
public class SkyTab extends AnimatedGraphicsTab {

	private static final int RENDER_WIDTH = 320;
	private static final int RENDER_HEIGHT = 200;

	private ImageData sprite;
	private ImageData imageData;
	private Image outputImage;
	private int t;

	public SkyTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Sky"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("SkyDescription"); //$NON-NLS-1$
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
		if (sprite == null) {
			return;
		}

		// Clear with blue background
		Color blue = new Color(0, 0, 255);
		Image tmp = new Image(null, imageData);
		GC tmpGC = new GC(tmp);
		tmpGC.setBackground(blue);
		tmpGC.fillRectangle(0, 0, RENDER_WIDTH, RENDER_HEIGHT);
		tmpGC.dispose();
		imageData = tmp.getImageData();
		tmp.dispose();

		t += 10;

		// Floor and ceiling textures
		for (int z = 1; z < 91; z++) {
			int u = 0;
			int difx = 2 * z + 10;
			int ustep = (160 << 7) / difx;

			for (int x = 0; x < 160; x++) {
				int xnew = (u >> 7) - ((u >> 14) << 7);
				int ynew = 7000 / z + t - ((7000 / z + t >> 7) << 7);
				int xnew2 = (u >> 9) - ((u >> 16) << 7);
				int ynew2 = 2048 / z + (t >> 5) - ((2048 / z + (t >> 5) >> 7) << 7);

				safeSetPixel(160 + x, 90 - z, safeGetPixel(sprite, 255 - xnew2, ynew2));
				safeSetPixel(160 - x, 90 - z, safeGetPixel(sprite, 128 + xnew2, ynew2));
				safeSetPixel(160 + x, 91 + z, safeGetPixel(sprite, 127 - xnew, ynew));
				safeSetPixel(160 - x, 91 + z, safeGetPixel(sprite, xnew, ynew));

				u += ustep;
			}
		}

		// Sprites and shadows
		for (int b = 0; b < 200; b += 10) {
			int e = (int) (110 + 20 * Math.cos((b + t) * 0.01) + 15 * Math.sin((4 * b + t) * 0.01));
			int f = (int) (160 + 50 * Math.sin((2 * b + t) * 0.01) + 25 * Math.cos((2 * b + t) * 0.01));
			drawSprite(f, e, 127, 127, 0.1f, 0.1f);
			drawShade(f, (e >> 2) + 135, 127, 127, 0.1f, 0.04f);
		}

		// Scrolling text bar
		for (int d = 0; d < 320; d++) {
			int t2 = t / 9 + d - ((t / 9 + d >> 10) << 10);
			int ys = t2 >> 7;
			int xs = t2 - (ys << 7);

			for (int c = 0; c < 15; c++) {
				int sy = ys * 16 + 128 + c;
				int sx = xs + 128;
				if (sx >= 0 && sx < sprite.width && sy >= 0 && sy < sprite.height) {
					safeSetPixel(d, c + 184, sprite.getPixel(sx, sy));
				}
			}
		}
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;

		if (sprite == null) {
			Image loaded = example.loadImage(gc.getDevice(), "DEMO2.png"); //$NON-NLS-1$
			if (loaded == null) return;
			sprite = loaded.getImageData();

			imageData = new ImageData(RENDER_WIDTH, RENDER_HEIGHT, sprite.depth, sprite.palette);
			t = 0;
		}

		if (imageData == null) {
			return;
		}

		if (outputImage != null) {
			outputImage.dispose();
		}
		outputImage = new Image(gc.getDevice(), imageData);

		int x = (width - RENDER_WIDTH) / 2;
		int y = (height - RENDER_HEIGHT) / 2;
		gc.drawImage(outputImage, x, y);
	}

	private int safeGetPixel(ImageData img, int x, int y) {
		x = Math.max(0, Math.min(x, img.width - 1));
		y = Math.max(0, Math.min(y, img.height - 1));
		return img.getPixel(x, y);
	}

	private void safeSetPixel(int x, int y, int pixel) {
		if (x >= 0 && x < imageData.width && y >= 0 && y < imageData.height) {
			imageData.setPixel(x, y, pixel);
		}
	}

	private void drawSprite(int x, int y, int xs, int ys, float rx, float ry) {
		int xn = Math.max(2, (int) (xs * rx));
		int yn = Math.max(2, (int) (ys * ry));
		int an = (ys << 8) / yn;
		int bn = (xs << 8) / xn;
		int rn = x - (xn >> 1);
		int tn = y - (yn >> 1);
		for (int ut = 1; ut < xn; ut++) {
			for (int vt = 1; vt < yn; vt++) {
				int sx = (ut * bn) >> 8;
				int sy = ((vt * an) >> 8) + 128;
				int pixel = safeGetPixel(sprite, sx, sy);
				if (pixel != 255) {
					safeSetPixel(ut + rn, vt + tn, pixel);
				}
			}
		}
	}

	private void drawShade(int x, int y, int xs, int ys, float rx, float ry) {
		int xn = Math.max(2, (int) (xs * rx));
		int yn = Math.max(2, (int) (ys * ry));
		int an = (ys << 8) / yn;
		int bn = (xs << 8) / xn;
		int rn = x - (xn >> 1);
		int tn = y - (yn >> 1);
		for (int ut = 1; ut < xn; ut++) {
			for (int vt = 1; vt < yn; vt++) {
				int sx = (ut * bn) >> 8;
				int sy = ((vt * an) >> 8) + 128;
				int pixel = safeGetPixel(sprite, sx, sy);
				if (pixel != 255) {
					safeSetPixel(ut + rn, vt + tn, pixel);
				}
			}
		}
	}
}
