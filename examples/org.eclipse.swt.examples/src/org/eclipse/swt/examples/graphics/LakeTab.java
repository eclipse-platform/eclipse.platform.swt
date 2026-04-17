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
 *     David Griffiths - Original Version
 *     Laurent Caron (laurent.caron at gmail dot com) - Conversion to SWT
 *     IBM Corporation - adaptation to GraphicsExample
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;

/**
 * This tab displays an animated lake reflection effect where the bottom half
 * of the scene shows a wave-distorted mirror of the source image.
 *
 * Reimplemented with direct pixel manipulation so it renders consistently on
 * all platforms, including Linux where {@code GC.copyArea} on image-backed GCs
 * can behave differently from the Win32/Cocoa backends.
 */
public class LakeTab extends AnimatedGraphicsTab {

	private static final int FRAMES = 12;
	private static final int SEAM_SIZE = 10;

	private int imgWidth, imgHeight;
	private ImageData topData;
	private ImageData bottomData;
	private int[][] waveFrames;
	private Image topImage;
	private Image bottomImage;
	private int currFrame;
	private int transparentPixel;

	public LakeTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Lake"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("LakeDescription"); //$NON-NLS-1$
	}

	@Override
	public int getInitialAnimationTime() {
		return 100;
	}

	@Override
	public void dispose() {
		if (topImage != null) {
			topImage.dispose();
			topImage = null;
		}
		if (bottomImage != null) {
			bottomImage.dispose();
			bottomImage = null;
		}
	}

	@Override
	public void next(int width, int height) {
		if (waveFrames == null) {
			return;
		}
		currFrame = (currFrame + 1) % FRAMES;
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;

		if (waveFrames == null) {
			Image loaded = example.loadImage(gc.getDevice(), "ash.jpg"); //$NON-NLS-1$
			if (loaded == null) return;
			ImageData src = loaded.getImageData();
			imgWidth = src.width;
			imgHeight = src.height;

			int[] srcPixels = new int[imgWidth * imgHeight];
			src.getPixels(0, 0, imgWidth * imgHeight, srcPixels, 0);

			int[] mirror = new int[imgWidth * imgHeight];
			for (int y = 0; y < imgHeight; y++) {
				System.arraycopy(srcPixels, (imgHeight - 1 - y) * imgWidth, mirror, y * imgWidth, imgWidth);
			}

			// Pick a sentinel pixel value for out-of-range rows. Marked as the
			// ImageData's transparentPixel so the canvas background shows
			// through instead of a stretched/dark edge row.
			transparentPixel = src.palette.getPixel(new RGB(255, 0, 255));
			int[] transparentRow = new int[imgWidth];
			for (int i = 0; i < imgWidth; i++) {
				transparentRow[i] = transparentPixel;
			}

			waveFrames = new int[FRAMES][imgWidth * imgHeight];
			// First pass: compute the union of rows that would ever fall
			// outside the mirror. Those rows stay transparent in every frame
			// so the visible reflection has a constant height across the
			// animation (otherwise the image appears to pulse in size).
			boolean[] alwaysTransparent = new boolean[imgHeight];
			// Force a transparent band at the top of the reflection so the
			// seam where the original meets the reflection (both contain
			// near-black rows from ash.jpg) becomes transparent.
			for (int j = 0; j < SEAM_SIZE && j < imgHeight; j++) {
				alwaysTransparent[j] = true;
			}
			int[][] srcRowPerFrame = new int[FRAMES][imgHeight];
			for (int f = 0; f < FRAMES; f++) {
				double d = 2.0 * Math.PI * f / FRAMES;
				for (int j = 0; j < imgHeight; j++) {
					int k = (int) (imgHeight / 14.0 * (j + 28.0)
							* Math.sin(imgHeight / 14.0 * (imgHeight - j) / (j + 1.0) + d) / imgHeight);
					int srcRow = (j < -k) ? j : (j + k);
					srcRowPerFrame[f][j] = srcRow;
					if (srcRow < 0 || srcRow >= imgHeight) {
						alwaysTransparent[j] = true;
					}
				}
			}
			for (int f = 0; f < FRAMES; f++) {
				int[] frame = waveFrames[f];
				for (int j = 0; j < imgHeight; j++) {
					if (alwaysTransparent[j]) {
						System.arraycopy(transparentRow, 0, frame, j * imgWidth, imgWidth);
					} else {
						int srcRow = srcRowPerFrame[f][j];
						System.arraycopy(mirror, srcRow * imgWidth, frame, j * imgWidth, imgWidth);
					}
				}
			}

			// Count the transparent band width at the bottom of the reflection
			// and apply the same treatment to the top of the original so the
			// combined image has a matching transparent band at the top,
			// hiding ash.jpg's dark leading rows.
			int topTransparentRows = 0;
			for (int j = imgHeight - 1; j >= 0 && alwaysTransparent[j]; j--) {
				topTransparentRows++;
			}

			int[] topPixels = new int[imgWidth * imgHeight];
			System.arraycopy(srcPixels, 0, topPixels, 0, srcPixels.length);
			for (int j = 0; j < topTransparentRows; j++) {
				System.arraycopy(transparentRow, 0, topPixels, j * imgWidth, imgWidth);
			}
			// Match the seam band at the bottom of the original.
			for (int j = imgHeight - SEAM_SIZE; j < imgHeight; j++) {
				if (j < 0) continue;
				System.arraycopy(transparentRow, 0, topPixels, j * imgWidth, imgWidth);
			}

			topData = new ImageData(imgWidth, imgHeight, src.depth, src.palette);
			topData.setPixels(0, 0, imgWidth * imgHeight, topPixels, 0);
			topData.transparentPixel = transparentPixel;
			bottomData = new ImageData(imgWidth, imgHeight, src.depth, src.palette);
			bottomData.transparentPixel = transparentPixel;
		}

		if (topImage == null) {
			topImage = new Image(gc.getDevice(), topData);
		}

		bottomData.setPixels(0, 0, imgWidth * imgHeight, waveFrames[currFrame], 0);
		if (bottomImage != null) {
			bottomImage.dispose();
		}
		bottomImage = new Image(gc.getDevice(), bottomData);

		int x = (width - imgWidth) / 2;
		int y = (height - 2 * imgHeight) / 2;
		gc.drawImage(topImage, x, y);
		gc.drawImage(bottomImage, x, y + imgHeight);
	}
}
