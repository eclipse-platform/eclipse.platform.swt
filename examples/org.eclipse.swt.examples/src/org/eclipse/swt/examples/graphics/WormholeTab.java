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

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/**
 * This tab displays an animated wormhole effect produced by mapping a
 * precomputed spoke pattern to a scrolling texture.
 */
public class WormholeTab extends AnimatedGraphicsTab {

	private static final int RENDER_WIDTH = 640;
	private static final int RENDER_HEIGHT = 480;
	private static final int TEXTURE_WIDTH = 15;
	private static final int TEXTURE_HEIGHT = 15;
	private static final int SPOKES = 2400;
	private static final int DIVS = SPOKES;
	private static final int DIRECTION_X = -2;
	private static final int DIRECTION_Y = -1;

	private ImageData imageData;
	private Image outputImage;
	private byte[] wormImg;
	private int[] wormTexture;

	public WormholeTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Wormhole"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("WormholeDescription"); //$NON-NLS-1$
	}

	@Override
	public int getInitialAnimationTime() {
		return 30;
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
		if (wormTexture == null) {
			return;
		}

		for (int i = 0; i < RENDER_WIDTH * RENDER_HEIGHT; i++) {
			int colorIndex = wormTexture[(wormImg[i] + 256) % 256];
			imageData.setPixel(i % RENDER_WIDTH, i / RENDER_WIDTH, colorIndex);
		}

		shiftDown();
		shiftUp();
		shiftRight();
		shiftLeft();
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;

		if (wormTexture == null) {
			Image loaded = example.loadImage(gc.getDevice(), "texture.png"); //$NON-NLS-1$
			if (loaded == null) return;
			ImageData texImage = loaded.getImageData();

			imageData = new ImageData(RENDER_WIDTH, RENDER_HEIGHT, texImage.depth, texImage.palette);

			wormTexture = new int[TEXTURE_WIDTH * TEXTURE_HEIGHT];
			int index = 0;
			for (int x = 0; x < TEXTURE_WIDTH; x++) {
				for (int y = 0; y < TEXTURE_HEIGHT; y++) {
					wormTexture[index++] = texImage.getPixel(x, y);
				}
			}
			initSpokes();
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

	private void initSpokes() {
		wormImg = new byte[RENDER_WIDTH * RENDER_HEIGHT];
		float[] spokeCos = new float[SPOKES];
		float[] spokeSin = new float[SPOKES];

		for (int i = 0; i < SPOKES; i++) {
			double angle = 2.0 * Math.PI * i / SPOKES;
			spokeCos[i] = (float) Math.cos(angle);
			spokeSin[i] = (float) Math.sin(angle);
		}

		int xCenter = RENDER_WIDTH / 2;
		int yCenter = RENDER_HEIGHT / 2 - RENDER_HEIGHT / 4;

		for (int j = 1; j < DIVS + 1; j++) {
			float z = (float) (-1.0f + Math.log(2.0f * j / DIVS));
			float divX = (float) RENDER_WIDTH * j / DIVS;
			float divY = (float) RENDER_HEIGHT * j / DIVS;

			for (int i = 0; i < SPOKES; i++) {
				float x = divX * spokeCos[i];
				float y = divY * spokeSin[i];
				y -= 25f * z;
				x += xCenter;
				y += yCenter;

				if (x >= 0 && x < RENDER_WIDTH && y >= 0 && y < RENDER_HEIGHT) {
					wormImg[(int) x + (int) y * RENDER_WIDTH] =
							(byte) (i / 8 % TEXTURE_WIDTH + TEXTURE_WIDTH * (j / 7 % TEXTURE_WIDTH));
				}
			}
		}
	}

	private void shiftDown() {
		int[] reg = new int[TEXTURE_HEIGHT];
		for (int i = 0; i < DIRECTION_Y; i++) {
			for (int k = 0; k < TEXTURE_HEIGHT; k++) {
				reg[k] = wormTexture[k + 210];
			}
			for (int k = 209; k >= 0; k--) {
				wormTexture[k + 15] = wormTexture[k];
			}
			for (int k = 0; k < 15; k++) {
				wormTexture[k] = reg[k];
			}
		}
	}

	private void shiftUp() {
		int[] reg = new int[TEXTURE_HEIGHT];
		for (int i = DIRECTION_Y; i < 0; i++) {
			for (int k = 0; k < TEXTURE_HEIGHT; k++) {
				reg[k] = wormTexture[k];
			}
			for (int k = 15; k < 15 * 15; k++) {
				wormTexture[k - 15] = wormTexture[k];
			}
			for (int k = 0; k < 15; k++) {
				wormTexture[k + 210] = reg[k];
			}
		}
	}

	private void shiftRight() {
		int[] reg = new int[TEXTURE_HEIGHT];
		for (int j = 0; j < DIRECTION_X; j++) {
			for (int k = 0; k < 15; k++) {
				reg[k] = wormTexture[15 * k + 14];
				for (int i = 14; i > 0; i--) {
					wormTexture[15 * k + i] = wormTexture[15 * k + i - 1];
				}
				wormTexture[15 * k] = reg[k];
			}
		}
	}

	private void shiftLeft() {
		int[] reg = new int[TEXTURE_HEIGHT];
		for (int j = DIRECTION_X; j < 0; j++) {
			for (int k = 0; k < 15; k++) {
				reg[k] = wormTexture[15 * k];
				for (int i = 0; i < 14; i++) {
					wormTexture[15 * k + i] = wormTexture[15 * k + i + 1];
				}
				wormTexture[15 * k + 14] = reg[k];
			}
		}
	}
}
