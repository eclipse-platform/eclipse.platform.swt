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
 * This tab displays an animated plasma effect using precomputed sine tables
 * and a cycling color palette.
 */
public class PlasmaTab extends AnimatedGraphicsTab {

	private static final int SIN_TABLE_SIZE = 1800;

	private ImageData imageData;
	private Image outputImage;
	private int[] palette;
	private final int[] sin = new int[SIN_TABLE_SIZE];
	private int plasmaIndex = 1;
	private int lastWidth;
	private int lastHeight;

	public PlasmaTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Plasma"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("PlasmaDescription"); //$NON-NLS-1$
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
		drawPlasma(width, height);
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
		precomputeSin();
		imageData = new ImageData(width, height, 24, new PaletteData(0xFF0000, 0xFF00, 0xFF));
	}

	private void createColors() {
		palette = new int[256];
		int r = 0, g = 0, b = 0;
		for (int i = 0; i < 256; i++) {
			palette[i] = 0;
		}

		for (int i = 0; i < 42; i++) {
			palette[i] = r << 18 | g << 10 | b << 2;
			r++;
		}
		for (int i = 42; i < 84; i++) {
			palette[i] = r << 18 | g << 10 | b << 2;
			g++;
		}
		for (int i = 84; i < 126; i++) {
			palette[i] = r << 18 | g << 10 | b << 2;
			b++;
		}
		for (int i = 126; i < 168; i++) {
			palette[i] = r << 18 | g << 10 | b << 2;
			r--;
		}
		for (int i = 168; i < 210; i++) {
			palette[i] = r << 18 | g << 10 | b << 2;
			g--;
		}
		for (int i = 210; i < 252; i++) {
			palette[i] = r << 18 | g << 10 | b << 2;
			b--;
		}
	}

	private void precomputeSin() {
		for (int i = 0; i < SIN_TABLE_SIZE; i++) {
			sin[i] = (int) (Math.cos(Math.PI * i / 180) * 1024);
		}
	}

	private void drawPlasma(int width, int height) {
		plasmaIndex += 2;
		if (plasmaIndex > 360) {
			plasmaIndex = 0;
		}

		for (int x = 0; x < width; x++) {
			int ix1 = Math.abs(((x << 1) + (plasmaIndex >> 1)) % SIN_TABLE_SIZE);
			int ix2 = Math.abs((x + (plasmaIndex << 1)) % SIN_TABLE_SIZE);
			int ix3 = Math.abs(((x >> 1) + plasmaIndex) % SIN_TABLE_SIZE);
			final int indexX = 75 + (sin[ix1] + sin[ix2] + (sin[ix3] << 1) >> 6);

			for (int y = 0; y < height; y++) {
				int iy1 = Math.abs((y + (plasmaIndex << 1)) % SIN_TABLE_SIZE);
				int iy2 = Math.abs(((y << 1) + (plasmaIndex >> 1)) % SIN_TABLE_SIZE);
				int iy3 = Math.abs((y + plasmaIndex) % SIN_TABLE_SIZE);
				final int indexY = 75 + ((sin[iy1] << 1) + sin[iy2] + (sin[iy3] << 1) >> 5);
				final int colorIndex = Math.abs((indexX * indexY >> 5) % 256);
				imageData.setPixel(x, y, palette[colorIndex]);
			}
		}
	}
}
