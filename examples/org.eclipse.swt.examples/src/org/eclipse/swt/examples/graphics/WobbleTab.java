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

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/**
 * This tab displays an animated wobble distortion effect that warps a texture
 * with rotating sine/cosine deformations.
 */
public class WobbleTab extends AnimatedGraphicsTab {

	private static final int RENDER_WIDTH = 210;
	private static final int RENDER_HEIGHT = 200;

	private ImageData imageData;
	private Image outputImage;
	private int[] baseline;
	private int[] frameBuffer;
	private int imgWidth, imgHeight;
	private int whitePixel;

	private double angle;
	private double rotDeg;
	private final double param1 = 0.6;
	private final double param2 = 2.0;
	private final double param3 = 0.5;
	private final int rotStep = 2;

	public WobbleTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Wobble"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("WobbleDescription"); //$NON-NLS-1$
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
		if (baseline == null) {
			return;
		}

		int cx = RENDER_WIDTH / 2;
		int cy = RENDER_HEIGHT / 2;
		double b = Math.sin(angle);

		for (int i = 0; i < RENDER_WIDTH; i++) {
			double a = Math.cos(angle + Math.cos(Math.PI / cx * param1 * Math.abs(cx - i)));
			for (int j = 0; j < RENDER_HEIGHT; j++) {
				frameBuffer[j * RENDER_WIDTH + i] = whitePixel;

				int ix = cx + (int) Math.round((i - cx) * a * param2 - (j - cy) * b);
				int iy = cy + (int) Math.round((i - cx) * b * param3 + (j - cy) * a);

				if (ix >= 0 && iy >= 0 && ix < RENDER_WIDTH && iy < RENDER_HEIGHT) {
					frameBuffer[RENDER_WIDTH * j + i] = baseline[RENDER_WIDTH * iy + ix];
				}
			}
		}
		imageData.setPixels(0, 0, RENDER_WIDTH * RENDER_HEIGHT, frameBuffer, 0);

		rotDeg += rotStep;
		if (rotDeg >= 360.0) {
			rotDeg = 0.0;
		}
		angle = rotDeg * Math.PI / 180.0;
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;

		if (baseline == null) {
			Image loaded = example.loadImage(gc.getDevice(), "bu.jpg"); //$NON-NLS-1$
			if (loaded == null) return;
			ImageData src = loaded.getImageData();

			imgWidth = src.width;
			imgHeight = src.height;

			imageData = new ImageData(RENDER_WIDTH, RENDER_HEIGHT, src.depth, src.palette);
			whitePixel = src.palette.getPixel(new org.eclipse.swt.graphics.RGB(255, 255, 255));

			int[] srcPixels = new int[imgWidth * imgHeight];
			src.getPixels(0, 0, imgWidth * imgHeight, srcPixels, 0);

			baseline = new int[RENDER_WIDTH * RENDER_HEIGHT];
			frameBuffer = new int[RENDER_WIDTH * RENDER_HEIGHT];

			int cx = RENDER_WIDTH / 2;
			int cy = RENDER_HEIGHT / 2;
			int icx = imgWidth / 2;
			int icy = imgHeight / 2;

			for (int i = 0; i < RENDER_WIDTH; i++) {
				for (int j = 0; j < RENDER_HEIGHT; j++) {
					if (i > cx - icx && i < cx + icx && j > cy - icy && j < cy + icy) {
						baseline[j * RENDER_WIDTH + i] = srcPixels[(i - cx + icx) + (j - cy + icy) * imgWidth];
					} else {
						baseline[j * RENDER_WIDTH + i] = whitePixel;
					}
				}
			}

			imageData.setPixels(0, 0, RENDER_WIDTH * RENDER_HEIGHT, baseline, 0);
			angle = 0;
			rotDeg = 0;
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
}
