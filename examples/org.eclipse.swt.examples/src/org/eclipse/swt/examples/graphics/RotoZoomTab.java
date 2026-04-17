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
 * This tab displays an animated rotozoom effect where a tiling texture is
 * rotated and zoomed using precomputed sine tables.
 */
public class RotoZoomTab extends AnimatedGraphicsTab {

	private static final int RENDER_WIDTH = 480;
	private static final int RENDER_HEIGHT = 360;

	private ImageData imageData;
	private Image outputImage;
	private int[] tilePixels;
	private int[] roto;
	private int[] roto2;
	private int path, zpath;

	public RotoZoomTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("RotoZoom"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("RotoZoomDescription"); //$NON-NLS-1$
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
		if (tilePixels == null) return;
		drawTile(roto[path], roto[(path + 128) & 255], roto2[zpath]);
		path = (path - 1) & 255;
		zpath = (zpath + 1) & 255;
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;

		if (tilePixels == null) {
			Image loaded = example.loadImage(gc.getDevice(), "tux256256.png"); //$NON-NLS-1$
			if (loaded == null) return;
			ImageData tile = loaded.getImageData();

			tilePixels = new int[256 * 256];
			for (int y = 0; y < 256; y++) {
				for (int x = 0; x < 256; x++) {
					tilePixels[y * 256 + x] = tile.getPixel(x, y);
				}
			}

			imageData = new ImageData(RENDER_WIDTH, RENDER_HEIGHT, tile.depth, tile.palette);
			roto = new int[256];
			roto2 = new int[256];
			for (int i = 0; i < 256; i++) {
				double rad = i * 1.41176 * 0.0174532;
				double c = Math.sin(rad);
				roto[i] = (int) ((c + 0.8) * 4096.0);
				roto2[i] = (int) (2.0 * c * 4096.0);
			}
			path = 0;
			zpath = 0;
			drawTile(roto[path], roto[(path + 128) & 255], roto2[zpath]);
		}

		if (imageData == null) return;

		if (outputImage != null) {
			outputImage.dispose();
		}
		outputImage = new Image(gc.getDevice(), imageData);

		int x = (width - RENDER_WIDTH) / 2;
		int y = (height - RENDER_HEIGHT) / 2;
		gc.drawImage(outputImage, x, y);
	}

	private void drawTile(int stepx, int stepy, int zoom) {
		int sx = 0, sy = 0;
		int xd = stepx * zoom >> 12;
		int yd = stepy * zoom >> 12;

		for (int j = 0; j < RENDER_HEIGHT; j++) {
			int x = sx;
			int y = sy;
			for (int i = 0; i < RENDER_WIDTH; i++) {
				int a = (x >> 12) & 255;
				int b = (y >> 12) & 255;
				imageData.setPixel(i, j, tilePixels[b * 256 + a]);
				x += xd;
				y += yd;
			}
			sx -= yd;
			sy += xd;
		}
	}
}
