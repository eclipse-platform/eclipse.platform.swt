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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

/**
 * This tab displays an animated metaball/blob effect where multiple blobs
 * move randomly and blend together when overlapping.
 */
public class BlobTab extends AnimatedGraphicsTab {

	private static final int BLOB_RADIUS = 44;
	private static final int BLOB_DRADIUS = BLOB_RADIUS * 2;
	private static final int BLOB_SRADIUS = BLOB_RADIUS * BLOB_RADIUS;
	private static final int NUMBER_OF_BLOBS = 20;

	private int[][] blob;
	private int[] blobX;
	private int[] blobY;
	private ImageData imageData;
	private Image outputImage;
	private int lastWidth;
	private int lastHeight;

	public BlobTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Blob"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("BlobDescription"); //$NON-NLS-1$
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
		if (blobX == null) {
			return;
		}
		if (width != lastWidth || height != lastHeight) {
			init(width, height);
		}

		// Clear pixel data
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				imageData.setPixel(x, y, 0);
			}
		}

		// Move and draw blobs
		for (int i = 0; i < NUMBER_OF_BLOBS; i++) {
			blobX[i] += -2 + (int) (5.0 * Math.random());
			blobY[i] += -2 + (int) (5.0 * Math.random());
		}

		for (int k = 0; k < NUMBER_OF_BLOBS; ++k) {
			if (blobX[k] > 0 && blobX[k] < width - BLOB_DRADIUS && blobY[k] > 0 && blobY[k] < height - BLOB_DRADIUS) {
				int start = blobX[k] + blobY[k] * width;
				for (int i = 0; i < BLOB_DRADIUS; ++i) {
					for (int j = 0; j < BLOB_DRADIUS; ++j) {
						int px = (start + j) % width;
						int py = (start + j) / width;
						if (py >= 0 && py < height) {
							int current = imageData.getPixel(px, py);
							int sum = current + blob[i][j];
							imageData.setPixel(px, py, Math.min(sum, 255));
						}
					}
					start += width;
				}
			} else {
				blobX[k] = (width >> 1) - BLOB_RADIUS;
				blobY[k] = (height >> 1) - BLOB_RADIUS;
			}
		}
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (blobX == null) {
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

		blob = new int[BLOB_DRADIUS][BLOB_DRADIUS];
		blobX = new int[NUMBER_OF_BLOBS];
		blobY = new int[NUMBER_OF_BLOBS];

		// Create blob shape
		for (int i = -BLOB_RADIUS; i < BLOB_RADIUS; ++i) {
			for (int j = -BLOB_RADIUS; j < BLOB_RADIUS; ++j) {
				final int distanceSquared = i * i + j * j;
				if (distanceSquared <= BLOB_SRADIUS) {
					final float fraction = (float) distanceSquared / (float) BLOB_SRADIUS;
					blob[i + BLOB_RADIUS][j + BLOB_RADIUS] = (int) (Math.pow(1.0 - fraction * fraction, 4.0) * 255.0);
				} else {
					blob[i + BLOB_RADIUS][j + BLOB_RADIUS] = 0;
				}
			}
		}

		// Initialize blob positions at center
		for (int i = 0; i < NUMBER_OF_BLOBS; i++) {
			blobX[i] = (width >> 1) - BLOB_RADIUS;
			blobY[i] = (height >> 1) - BLOB_RADIUS;
		}

		// Create grayscale palette image data
		final RGB[] colors = new RGB[256];
		for (int i = 0; i < 256; ++i) {
			colors[i] = new RGB(i, i, i);
		}
		imageData = new ImageData(width, height, 8, new PaletteData(colors));
	}
}
