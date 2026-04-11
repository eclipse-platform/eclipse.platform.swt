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
 *     Josh83 - Original Version
 *     Laurent Caron (laurent.caron at gmail dot com) - Conversion to SWT
 *     IBM Corporation - adaptation to GraphicsExample
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/**
 * This tab displays an animated flat text perspective distortion effect
 * using sine/cosine rotation on a tiled texture.
 */
public class FlatTextTab extends AnimatedGraphicsTab {

	private static final int HAUTEUR = 100;

	private int[] tex;
	private int[] sine;
	private int[] cose;
	private double ang;
	private int yd;
	private ImageData imageData;
	private ImageData textureData;
	private Image outputImage;
	private boolean initialized;

	public FlatTextTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("FlatText"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("FlatTextDescription"); //$NON-NLS-1$
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
		if (!initialized) {
			return;
		}

		int a = (int) (HAUTEUR * 0.625);
		int b = HAUTEUR * 100;

		ang += 2f;
		yd += 5;

		int angnew = (int) (ang - ((int) (ang / 256) << 8));
		int halfH = height / 2;
		int halfW = width / 2;

		for (int y = 0; y < halfH; y++) {
			for (int x = 0; x < width; x++) {
				int u = a * (x - halfW) / (y - halfH);
				int v = b / (y - halfH);

				int unew = (u * cose[angnew] - v * sine[angnew] >> 8);
				int vnew = (u * sine[angnew] + v * cose[angnew] >> 8) + yd;

				int index = unew + (vnew << 8) & 65535;
				imageData.setPixel(x, height - y - 1, tex[index]);
			}
		}
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;

		if (!initialized) {
			Image sourceImage = example.loadImage(gc.getDevice(), "TEXFLAT2.png"); //$NON-NLS-1$
			if (sourceImage == null) return;
			textureData = sourceImage.getImageData();

			tex = new int[65536];
			textureData.getPixels(0, 0, 65536, tex, 0);

			sine = new int[256];
			cose = new int[256];
			for (int i = 0; i < 256; i++) {
				sine[i] = (int) (Math.sin(i * 0.02454) * 256);
				cose[i] = (int) (Math.cos(i * 0.02454) * 256);
			}

			yd = 0;
			ang = 0;

			imageData = new ImageData(width, height, textureData.depth, textureData.palette);
			initialized = true;
		}

		if (imageData.width != width || imageData.height != height) {
			imageData = new ImageData(width, height, textureData.depth, textureData.palette);
		}

		if (outputImage != null) {
			outputImage.dispose();
		}
		outputImage = new Image(gc.getDevice(), imageData);
		gc.drawImage(outputImage, 0, 0);
	}
}
