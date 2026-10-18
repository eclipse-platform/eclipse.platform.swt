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
 * This tab displays an animated twister effect using precomputed sine tables
 * and a torsion deformation on a checkered pattern.
 */
public class TwisterTab extends AnimatedGraphicsTab {

	private static final int SIN_TABLE_SIZE = 256;

	private ImageData imageData;
	private Image outputImage;
	private final int[] guim1 = new int[SIN_TABLE_SIZE];
	private final int[] guim2 = new int[SIN_TABLE_SIZE];
	private final int[] guim3 = new int[SIN_TABLE_SIZE];
	private final float[] pas = new float[SIN_TABLE_SIZE];
	private int[] palette;
	private int roll;
	private boolean initialized;
	private int lastWidth;
	private int lastHeight;

	public TwisterTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Twister"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("TwisterDescription"); //$NON-NLS-1$
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
		if (!initialized) {
			return;
		}
		if (width != lastWidth || height != lastHeight) {
			init(width, height);
		}
		imageData = new ImageData(width, height, 24, new PaletteData(0xFF0000, 0xFF00, 0xFF));
		doTwister(width, height);
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!initialized) {
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
		roll = 0;
		createColors();
		initSinTables(width);
		initialized = true;
	}

	private void createColors() {
		palette = new int[256];
		for (int i = 0; i < 256; i++) {
			palette[i] = (i << 16) | (i / 2 << 8) | (i / 4);
		}
	}

	private void initSinTables(int width) {
		for (int i = 0; i < SIN_TABLE_SIZE; i++) {
			int min = width;
			int numMin = 0;
			final int[] tmp = new int[4];

			for (int k = 0; k < 4; k++) {
				tmp[k] = width / 2
						- (int) ((width / 2 - 20) * Math.cos(k * Math.PI / 2 + i * (3 * Math.PI / SIN_TABLE_SIZE)));
				if (tmp[k] < min) {
					min = tmp[k];
					numMin = k;
				}
			}

			guim1[i] = tmp[numMin];
			guim2[i] = tmp[(numMin + 1) & 3];
			guim3[i] = tmp[(numMin + 2) & 3];

			pas[i] = (float) (1.2 * Math.sin(i * 14f * Math.PI / SIN_TABLE_SIZE)
					* Math.cos(i * 2f * Math.PI / SIN_TABLE_SIZE));
		}
	}

	private void doTwister(int width, int height) {
		roll = (roll + 1) % (SIN_TABLE_SIZE - 1);

		for (int j = 0; j < height; j++) {
			int wouaf = (int) (roll + j * pas[roll]);
			wouaf &= SIN_TABLE_SIZE - 1;

			final int tmp1 = guim1[wouaf];
			final int tmp2 = guim2[wouaf];
			final int tmp3 = guim3[wouaf];

			zoom(tmp1, tmp2 - tmp1, j, width, height);
			zoom(tmp2, tmp3 - tmp2, j, width, height);
		}
	}

	private void zoom(int begin, int size, int row, int width, int height) {
		if (size == 0 || row >= height) {
			return;
		}
		int screenIndex = begin + row * width;
		final int j = row & 63;
		final float rap = 64.0f / size;

		for (float k = 0f; k < 64f; k += rap) {
			final int i = (int) k;
			int color = (i ^ j) << 2;
			if ((color & 64) == 0) {
				color ^= 64;
			}
			int posX = screenIndex % width;
			int posY = screenIndex / width;
			if (posX >= 0 && posX < width && posY >= 0 && posY < height) {
				imageData.setPixel(posX, posY, palette[color]);
			}
			screenIndex++;
		}
	}
}
