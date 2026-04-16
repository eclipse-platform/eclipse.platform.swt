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
 *     W.P. van Paassen - Original Version
 *     Laurent Caron (laurent.caron at gmail dot com) - Conversion to SWT
 *     IBM Corporation - adaptation to GraphicsExample
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/**
 * This tab displays an animated sine wave distortion effect that cycles
 * through 12 different wave configurations applied to an image.
 */
public class SineWaveTab extends AnimatedGraphicsTab {

	private static final int MAX_EFFECTS = 12;

	private ImageData sourceImage;
	private ImageData imageData;
	private Image outputImage;
	private int imgWidth, imgHeight;
	private int sinIndex;
	private SinEffect[] sineEffects;
	private int currentEffect;

	public SineWaveTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("SineWave"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("SineWaveDescription"); //$NON-NLS-1$
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
		if (sourceImage == null) {
			return;
		}

		int sinBackup = sinIndex;
		SinEffect effect = sineEffects[currentEffect];

		if (effect.horizontal) {
			for (int i = 0; i < imgHeight; i++) {
				int offset = effect.sineTable[sinBackup];
				if (effect.horizontal && (i % 2 == 1)) {
					offset = -offset;
				}
				copyRow(sourceImage, i, offset, imageData, i);
				sinBackup += effect.indexAdd;
				sinBackup &= 511;
			}
		} else {
			for (int i = 0; i < imgHeight; i++) {
				int offset = effect.sineTable[sinBackup];
				copyRow(sourceImage, i, offset, imageData, i);
				sinBackup += effect.indexAdd;
				sinBackup &= 511;
			}
		}

		sinIndex += 6;
		if (sinIndex > 511) {
			sinIndex = 0;
			currentEffect++;
			currentEffect %= MAX_EFFECTS;
		}
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;

		if (sourceImage == null) {
			Image loaded = example.loadImage(gc.getDevice(), "tuxblackbg.png"); //$NON-NLS-1$
			if (loaded == null) return;
			sourceImage = loaded.getImageData();
			imgWidth = sourceImage.width;
			imgHeight = sourceImage.height;

			int[] pixels = new int[imgWidth * imgHeight];
			sourceImage.getPixels(0, 0, imgWidth * imgHeight, pixels, 0);
			imageData = new ImageData(imgWidth, imgHeight, sourceImage.depth, sourceImage.palette);
			imageData.setPixels(0, 0, imgWidth * imgHeight, pixels, 0);

			sinIndex = 0;
			currentEffect = 0;
			initEffects();
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

	private void initEffects() {
		sineEffects = new SinEffect[MAX_EFFECTS];
		for (int i = 0; i < MAX_EFFECTS; i++) {
			sineEffects[i] = new SinEffect();
		}

		double[] amplitudes = { 8, 16, 20, 32, 64, 128, 256, 128, 64, 44, 32, 8 };
		for (int i = 0; i < 512; i++) {
			float rad = (float) (i * 0.0174532 * 0.703125);
			for (int j = 0; j < MAX_EFFECTS; j++) {
				sineEffects[j].sineTable[i] = (short) (Math.sin(rad) * amplitudes[j]);
			}
		}

		int[] indexAdds = { 2, 4, 3, 5, 2, 2, 4, 1, 2, 8, 3, 2 };
		boolean[] horizontals = { false, false, true, false, false, true, false, false, true, false, true, true };
		for (int i = 0; i < MAX_EFFECTS; i++) {
			sineEffects[i].indexAdd = indexAdds[i];
			sineEffects[i].horizontal = horizontals[i];
		}
	}

	private void copyRow(ImageData src, int srcY, int offset, ImageData dest, int destY) {
		if (offset >= 0) {
			int count = Math.min(imgWidth, imgWidth - offset);
			if (count > 0) {
				int[] pixels = new int[count];
				src.getPixels(offset, srcY, count, pixels, 0);
				dest.setPixels(0, destY, count, pixels, 0);
			}
		} else {
			int srcStart = 0;
			int destStart = -offset;
			int count = Math.min(imgWidth, imgWidth + offset);
			if (count > 0 && destStart < imgWidth) {
				int[] pixels = new int[count];
				src.getPixels(srcStart, srcY, count, pixels, 0);
				dest.setPixels(destStart, destY, count, pixels, 0);
			}
		}
	}

	private static class SinEffect {
		short[] sineTable = new short[512];
		int indexAdd;
		boolean horizontal;
	}
}
