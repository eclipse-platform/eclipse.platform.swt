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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

/**
 * This tab displays an animated shade bobs effect where a heated bob shape
 * moves along a precomputed path, leaving a fading heat trail.
 */
public class ShadeBobsTab extends AnimatedGraphicsTab {

	private static final int[][] HEAT = {
		{ 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 0, 0, 0 },
		{ 0, 0, 1, 1, 2, 2, 2, 3, 3, 2, 2, 2, 1, 1, 0, 0 },
		{ 0, 0, 1, 2, 2, 3, 3, 3, 3, 3, 3, 2, 2, 1, 0, 0 },
		{ 0, 1, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 1, 0 },
		{ 0, 1, 2, 2, 3, 3, 3, 4, 4, 3, 3, 3, 2, 2, 1, 0 },
		{ 1, 1, 2, 3, 3, 3, 4, 4, 4, 4, 3, 3, 3, 2, 1, 1 },
		{ 1, 1, 2, 3, 3, 3, 4, 4, 4, 4, 3, 3, 3, 2, 1, 1 },
		{ 0, 1, 2, 2, 3, 3, 3, 4, 4, 3, 3, 3, 2, 2, 1, 0 },
		{ 0, 1, 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 2, 1, 1, 0 },
		{ 0, 0, 1, 2, 2, 3, 3, 3, 3, 3, 3, 2, 2, 1, 0, 0 },
		{ 0, 0, 1, 1, 2, 2, 2, 3, 3, 2, 2, 2, 1, 1, 0, 0 },
		{ 0, 0, 0, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0 },
	};

	private ImageData imageData;
	private Image outputImage;
	private int[] xpath;
	private int[] ypath;
	private int[] pathpath;
	private int trail;
	private int lastWidth;
	private int lastHeight;

	public ShadeBobsTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("ShadeBobs"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("ShadeBobsDescription"); //$NON-NLS-1$
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
		if (xpath == null) {
			return;
		}
		if (width != lastWidth || height != lastHeight) {
			init(width, height);
		}

		// Remove heat from tail
		if (trail >= 500) {
			int tmp = trail - 500;
			int remx = getBobXLocation(tmp);
			int remy = getBobYLocation(tmp);
			for (int i = 0; i < 16; i++) {
				int base = (remy + i) * width + remx;
				for (int j = 0; j < 16; j++) {
					int px = (base + j) % width;
					int py = (base + j) / width;
					if (py >= 0 && py < height) {
						int val = imageData.getPixel(px, py);
						val -= HEAT[i][j] * 8;
						if (val < 0) {
							val = 0;
						}
						imageData.setPixel(px, py, val);
					}
				}
			}
		}

		// Add heat at new head
		int drawx = getBobXLocation(trail);
		int drawy = getBobYLocation(trail);
		for (int i = 0; i < 16; i++) {
			int base = (drawy + i) * width + drawx;
			for (int j = 0; j < 16; j++) {
				int px = (base + j) % width;
				int py = (base + j) / width;
				if (py >= 0 && py < height) {
					int val = imageData.getPixel(px, py);
					val += HEAT[i][j] * 8;
					if (val > 255) {
						val = 255;
					}
					imageData.setPixel(px, py, val);
				}
			}
		}
		trail++;
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (xpath == null) {
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
		trail = 0;

		xpath = new int[512];
		ypath = new int[512];
		pathpath = new int[1024];

		final int hw = width - 150;
		final int hh = height - 180;
		final int aw = 67;
		final int ah = 82;

		for (int i = 0; i < 512; i++) {
			final double rad = i * 0.703125 * 0.0174532;
			xpath[i] = (int) (Math.sin(rad * 2) * hw / 2 + hw / 2 + aw);
			ypath[i] = (int) (Math.sin(rad) * hh / 2 + hh / 2 + ah);
		}

		for (int i = 0; i < 1024; i++) {
			final double rad = i * 0.3515625 * 0.0174532;
			pathpath[i] = (int) (Math.sin(rad) * 15);
		}

		final RGB[] colors = new RGB[256];
		for (int i = 0; i < 64; ++i) {
			colors[i] = new RGB(0, 0, i << 1);
			colors[i + 64] = new RGB(i << 1, 0, 128 - (i << 1));
			colors[i + 128] = new RGB(128 + (i << 1), 0, 128 - (i << 1));
			colors[i + 192] = new RGB(255, i << 2, i << 2);
		}

		imageData = new ImageData(width, height, 8, new PaletteData(colors));
	}

	private int getBobXLocation(int index) {
		return xpath[index & 511] + pathpath[index & 1023];
	}

	private int getBobYLocation(int index) {
		return ypath[index & 511] + pathpath[index & 1023];
	}
}
