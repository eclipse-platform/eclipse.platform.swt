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
 * This tab displays an animated warp distortion effect that uses a precomputed
 * distortion lookup table to render a texture with animated warping.
 */
public class WarpTab extends AnimatedGraphicsTab {

	private static final int RENDER_WIDTH = 640;
	private static final int RENDER_HEIGHT = 480;
	private static final int TEXTURE_SIZE = 256;

	private ImageData imageData;
	private Image outputImage;
	private int[] texture;
	private int[][] distortionX;
	private int[][] distortionY;
	private float alpha, beta, dz, dw;

	public WarpTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Warp"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("WarpDescription"); //$NON-NLS-1$
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
		if (texture == null) {
			return;
		}

		alpha += 0.02f;
		beta += 0.044f;
		dz += (float) (Math.sin(alpha + beta) * 2 + Math.cos(beta) + 0.4);
		dw += (float) (Math.cos(beta - alpha) * 3 + Math.sin(alpha) + 0.2);
		int decz = (int) dz;
		int decw = (int) dw;

		int halfW = RENDER_WIDTH / 2;
		int halfH = RENDER_HEIGHT / 2;

		for (int j = 0; j < halfH; j++) {
			int p1 = halfW + RENDER_WIDTH * (halfH - j);
			int p2 = p1;
			int p3 = halfW + RENDER_WIDTH * (halfH + j);
			int p4 = p3;

			for (int i = 0; i < halfW; i++) {
				int ddx = distortionX[j][i];
				int ddy = distortionY[j][i];

				drawPixel(p1, (-ddx + decz) & 255, (-ddy + decw) & 255);
				p1--;
				drawPixel(p2, (-ddx + decz) & 255, (ddy + decw) & 255);
				p2++;
				drawPixel(p3, (ddx + decz) & 255, (-ddy + decw) & 255);
				p3--;
				drawPixel(p4, (ddx + decz) & 255, (ddy + decw) & 255);
				p4++;
			}
		}
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;

		if (texture == null) {
			Image loaded = example.loadImage(gc.getDevice(), "texture.png"); //$NON-NLS-1$
			if (loaded == null) return;
			ImageData texImage = loaded.getImageData();

			imageData = new ImageData(RENDER_WIDTH, RENDER_HEIGHT, texImage.depth, texImage.palette);

			int index = 0;
			texture = new int[TEXTURE_SIZE * TEXTURE_SIZE];
			for (int x = 0; x < TEXTURE_SIZE; x++) {
				for (int y = 0; y < TEXTURE_SIZE; y++) {
					texture[index++] = texImage.getPixel(x, y);
				}
			}

			initDistortionTable();
			alpha = 0;
			beta = 0;
			dz = 0;
			dw = 0;
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

	private void initDistortionTable() {
		int halfW = RENDER_WIDTH / 2;
		int halfH = RENDER_HEIGHT / 2;
		distortionX = new int[halfH][halfW];
		distortionY = new int[halfH][halfW];

		for (int i = 0; i < halfH; i++) {
			for (int j = 0; j < halfW; j++) {
				double f = Math.pow(j * 1.2 / RENDER_WIDTH, 2);
				double d = Math.log(1 + i / (RENDER_HEIGHT / 3.0)) / (3 * f + 1) * halfH;
				distortionX[i][j] = (short) d;

				f = Math.pow(i * 1.5 / RENDER_WIDTH, 2);
				d = Math.log(1 + j / (RENDER_WIDTH / 3.0)) / (3 * f + 1) * halfW;
				distortionY[i][j] = (short) d;
			}
		}
	}

	private void drawPixel(int index, int x, int y) {
		int colorIndex = texture[(y << 8) + x];
		int posX = index % RENDER_WIDTH;
		int posY = index / RENDER_WIDTH;
		if (posX >= 0 && posX < RENDER_WIDTH && posY >= 0 && posY < RENDER_HEIGHT) {
			imageData.setPixel(posX, posY, colorIndex);
		}
	}
}
