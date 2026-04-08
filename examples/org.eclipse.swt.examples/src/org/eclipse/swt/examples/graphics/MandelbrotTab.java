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
 * This tab displays an animated Mandelbrot fractal with a continuously
 * zooming view into the fractal boundary.
 */
public class MandelbrotTab extends AnimatedGraphicsTab {

	private static final int MAX_ITER = 570;

	private ImageData imageData;
	private Image outputImage;
	private double zoom = 150;
	private double moveX = -0.5;
	private double moveY = 0.0;
	private double zoomSpeed = 1.02;
	private int lastWidth;
	private int lastHeight;

	public MandelbrotTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Mandelbrot"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("MandelbrotDescription"); //$NON-NLS-1$
	}

	@Override
	public int getInitialAnimationTime() {
		return 50;
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
		if (imageData == null) {
			return;
		}
		if (width != lastWidth || height != lastHeight) {
			init(width, height);
		}

		zoom *= zoomSpeed;
		drawMandelbrot(width, height);
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (imageData == null) {
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
		zoom = 150;
		imageData = new ImageData(width, height, 24, new PaletteData(0xFF0000, 0xFF00, 0xFF));
	}

	private void drawMandelbrot(int width, int height) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				double zx = 0;
				double zy = 0;
				final double cX = (x - width / 2.0) / zoom + moveX;
				final double cY = (y - height / 2.0) / zoom + moveY;
				double tmp;
				int iter = MAX_ITER;
				while (zx * zx + zy * zy < 4 && iter > 0) {
					tmp = zx * zx - zy * zy + cX;
					zy = 2.0 * zx * zy + cY;
					zx = tmp;
					iter--;
				}
				imageData.setPixel(x, y, iter | iter << 8);
			}
		}
	}
}
