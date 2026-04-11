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
 *     W.P. van Paassen and Byron Ellacott - Original Version
 *     Laurent Caron (laurent.caron at gmail dot com) - Conversion to SWT
 *     IBM Corporation - adaptation to GraphicsExample
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/**
 * This tab displays an animated spherical lens distortion effect that bounces
 * across an image, magnifying the area beneath the lens.
 */
public class LensTab extends AnimatedGraphicsTab {

	private static final int LENS_WIDTH = 150;
	private static final int LENS_ZOOM = 40;

	private ImageData backing;
	private int[][] lens;
	private int lensX = 16, lensY = 16;
	private int xd = 1, yd = 1;
	private ImageData imageData;
	private Image outputImage;
	private int imgWidth, imgHeight;

	public LensTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Lens"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("LensDescription"); //$NON-NLS-1$
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
		if (backing == null) {
			return;
		}

		applyLens(lensX, lensY);

		lensX += xd;
		lensY += yd;
		if (lensX > imgWidth - LENS_WIDTH - 15 || lensX < 15) {
			xd = -xd;
		}
		if (lensY > imgHeight - LENS_WIDTH - 15 || lensY < 15) {
			yd = -yd;
		}
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;

		if (backing == null) {
			Image sourceImage = example.loadImage(gc.getDevice(), "tuxblackbg.png"); //$NON-NLS-1$
			if (sourceImage == null) return;
			backing = sourceImage.getImageData();
			imgWidth = backing.width;
			imgHeight = backing.height;

			initLens();

			imageData = new ImageData(imgWidth, imgHeight, backing.depth, backing.palette);
			int[] pixels = new int[imgWidth * imgHeight];
			backing.getPixels(0, 0, imgWidth * imgHeight, pixels, 0);
			imageData.setPixels(0, 0, imgWidth * imgHeight, pixels, 0);
		}

		if (imageData == null) {
			return;
		}

		if (outputImage != null) {
			outputImage.dispose();
		}
		outputImage = new Image(gc.getDevice(), imageData);

		int x = (width - imgWidth) / 2;
		int y = (height - imgHeight) / 2;
		gc.drawImage(outputImage, x, y);
	}

	private void initLens() {
		lens = new int[LENS_WIDTH][LENS_WIDTH];
		int r = LENS_WIDTH / 2;
		int d = LENS_ZOOM;

		for (int y = 0; y < LENS_WIDTH >> 1; y++) {
			for (int x = 0; x < LENS_WIDTH >> 1; x++) {
				int ix, iy, offset;
				if (x * x + y * y < r * r) {
					float shift = (float) (d / Math.sqrt(d * d - (x * x + y * y - r * r)));
					ix = (int) (x * shift - x);
					iy = (int) (y * shift - y);
				} else {
					ix = 0;
					iy = 0;
				}
				offset = iy * imgWidth + ix;
				lens[LENS_WIDTH / 2 - y][LENS_WIDTH / 2 - x] = -offset;
				lens[LENS_WIDTH / 2 + y][LENS_WIDTH / 2 + x] = offset;
				offset = -iy * imgWidth + ix;
				lens[LENS_WIDTH / 2 + y][LENS_WIDTH / 2 - x] = -offset;
				lens[LENS_WIDTH / 2 - y][LENS_WIDTH / 2 + x] = offset;
			}
		}
	}

	private void applyLens(int ox, int oy) {
		for (int y = 0; y < LENS_WIDTH; y++) {
			int temp = (y + oy) * imgWidth + ox;
			for (int x = 0; x < LENS_WIDTH; x++) {
				int pos = temp + x;
				int backPos = pos + lens[y][x];
				imageData.setPixel(pos % imgWidth, pos / imgWidth, backing.getPixel(backPos % imgWidth, backPos / imgWidth));
			}
		}
	}
}
