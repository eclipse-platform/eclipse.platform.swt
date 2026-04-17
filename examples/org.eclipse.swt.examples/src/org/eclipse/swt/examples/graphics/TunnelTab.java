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
 * This tab displays an animated tunnel effect using a precomputed distance and
 * angle lookup table mapped against a scrolling stone texture.
 */
public class TunnelTab extends AnimatedGraphicsTab {

	private static final int RENDER_WIDTH = 640;
	private static final int RENDER_HEIGHT = 480;
	private static final int TEX_WIDTH = 256;
	private static final int TEX_HEIGHT = 256;

	private ImageData imageData;
	private Image outputImage;
	private int[] texture;
	private int[][] distanceTable;
	private int[][] angleTable;
	private float animation;

	public TunnelTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Tunnel"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("TunnelDescription"); //$NON-NLS-1$
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
		if (texture == null) return;

		int shiftX = (int) (TEX_WIDTH * 1.0 * animation);
		int shiftY = (int) (TEX_HEIGHT * 0.25 * animation);

		for (int x = 0; x < RENDER_WIDTH; x++) {
			for (int y = 0; y < RENDER_HEIGHT; y++) {
				int tx = Math.abs((distanceTable[x][y] + shiftX) % TEX_WIDTH);
				int ty = Math.abs((angleTable[x][y] + shiftY) % TEX_HEIGHT);
				imageData.setPixel(x, y, texture[ty * TEX_WIDTH + tx]);
			}
		}
		animation += 0.02f;
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;

		if (texture == null) {
			Image loaded = example.loadImage(gc.getDevice(), "tunnelstonetex.png"); //$NON-NLS-1$
			if (loaded == null) return;
			ImageData tex = loaded.getImageData();

			texture = new int[TEX_WIDTH * TEX_HEIGHT];
			for (int y = 0; y < TEX_HEIGHT; y++) {
				for (int x = 0; x < TEX_WIDTH; x++) {
					texture[y * TEX_WIDTH + x] = tex.getPixel(x, y);
				}
			}

			imageData = new ImageData(RENDER_WIDTH, RENDER_HEIGHT, tex.depth, tex.palette);
			distanceTable = new int[RENDER_WIDTH][RENDER_HEIGHT];
			angleTable = new int[RENDER_WIDTH][RENDER_HEIGHT];

			for (int x = 0; x < RENDER_WIDTH; x++) {
				for (int y = 0; y < RENDER_HEIGHT; y++) {
					double dx = x - RENDER_WIDTH / 2.0;
					double dy = y - RENDER_HEIGHT / 2.0;
					int distance = (int) (32.0 * TEX_HEIGHT / Math.sqrt(dx * dx + dy * dy) % TEX_HEIGHT);
					int angle = (int) (0.5 * TEX_WIDTH * Math.atan2(dy, dx) / Math.PI);
					distanceTable[x][y] = distance;
					angleTable[x][y] = angle;
				}
			}
			animation = 0f;
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
}
