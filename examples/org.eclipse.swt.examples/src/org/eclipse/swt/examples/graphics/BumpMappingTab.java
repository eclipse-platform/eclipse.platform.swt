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
 *     Laurent Lepinay - Original Version
 *     Laurent Caron (laurent.caron at gmail dot com) - Conversion to SWT
 *     IBM Corporation - adaptation to GraphicsExample
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

/**
 * This tab displays an animated bump mapping effect with a moving light source
 * illuminating a textured surface.
 */
public class BumpMappingTab extends AnimatedGraphicsTab {

	private ImageData bumpImage;
	private int[] envMap;
	private float fTime;
	private int lightX, lightY;
	private ImageData imageData;
	private Image outputImage;
	private int imgWidth, imgHeight;

	public BumpMappingTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("BumpMapping"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("BumpMappingDescription"); //$NON-NLS-1$
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
		if (bumpImage == null) {
			return;
		}

		for (int y = 1; y < imgHeight - 1; y++) {
			for (int x = 1; x < imgWidth - 1; x++) {
				int dX = (bumpImage.getPixel(x + 1, y) >> 16) - (bumpImage.getPixel(x - 1, y) >> 16);
				int dY = (bumpImage.getPixel(x, y + 1) >> 16) - (bumpImage.getPixel(x, y - 1) >> 16);

				dX = dX - (lightX - x);
				dY = dY - (lightY - y);
				if (dX <= -128 || dX >= 128) {
					dX = dY = -128;
				}
				if (dY <= -128 || dY >= 128) {
					dX = dY = -128;
				}
				dX += 128;
				dY += 128;

				imageData.setPixel(x, y, envMap[dX + 256 * dY]);
			}
		}

		lightX = (int) (imgWidth / 2 + 80 * Math.cos(fTime += .1));
		lightY = (int) (imgHeight / 2 + 80 * Math.sin(fTime));
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;

		if (bumpImage == null) {
			Image sourceImage = example.loadImage(gc.getDevice(), "bump.png"); //$NON-NLS-1$
			if (sourceImage == null) return;
			bumpImage = sourceImage.getImageData();
			imgWidth = bumpImage.width;
			imgHeight = bumpImage.height;

			envMap = new int[256 * 256];
			for (int y = 0; y < 256; y++) {
				for (int x = 0; x < 256; x++) {
					envMap[x + 256 * y] = (int) (255 - 255 * Math.sqrt((x - 128) * (x - 128) + (y - 128) * (y - 128)) / Math.sqrt(128 * 128 + 128 * 128));
				}
			}

			RGB[] colors = new RGB[256];
			for (int i = 0; i < 64; i++) {
				colors[i] = new RGB(0, 0, 0);
			}
			for (int i = 64; i < 128; i++) {
				colors[i] = new RGB(0, 0, (i - 64) * 4);
			}
			for (int i = 128; i < 256; i++) {
				colors[i] = new RGB((i - 128) * 2, (i - 128) * 2, 255);
			}

			fTime = 0.0f;
			lightX = (int) (imgWidth / 2 + 80 * Math.cos(fTime));
			lightY = (int) (imgHeight / 2 + 80 * Math.sin(fTime));

			imageData = new ImageData(imgWidth, imgHeight, 8, new PaletteData(colors));
		}

		if (imageData == null) {
			return;
		}

		if (outputImage != null) {
			outputImage.dispose();
		}
		outputImage = new Image(gc.getDevice(), imageData);

		int x = (width - imgWidth) / 2;
		int y = (height - imgHeight) / 2;
		gc.drawImage(outputImage, x, y);
	}
}
