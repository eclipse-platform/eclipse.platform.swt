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
 *     Jerry Huxtable - Original Version
 *     Laurent Caron (laurent.caron at gmail dot com) - Conversion to SWT
 *     IBM Corporation - adaptation to GraphicsExample
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/**
 * This tab displays an animated block pixelation effect that oscillates block
 * size from single pixels to large blocks and back on an image.
 */
public class BlockEffectTab extends AnimatedGraphicsTab {

	private ImageData sourceImage;
	private ImageData imageData;
	private Image outputImage;
	private int imgWidth, imgHeight;
	private int blockSize;
	private int direction;

	public BlockEffectTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("BlockEffect"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("BlockEffectDescription"); //$NON-NLS-1$
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

		imageData = filter(sourceImage, blockSize);
		blockSize += direction;

		if (blockSize <= 1) {
			direction = 1;
			blockSize = 1;
		}

		if (blockSize >= imgWidth / 4) {
			direction = -1;
		}
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;

		if (sourceImage == null) {
			Image loaded = example.loadImage(gc.getDevice(), "flower.jpg"); //$NON-NLS-1$
			if (loaded == null) return;
			sourceImage = loaded.getImageData();
			imgWidth = sourceImage.width;
			imgHeight = sourceImage.height;

			int[] pixels = new int[imgWidth * imgHeight];
			sourceImage.getPixels(0, 0, imgWidth * imgHeight, pixels, 0);
			imageData = new ImageData(imgWidth, imgHeight, sourceImage.depth, sourceImage.palette);
			imageData.setPixels(0, 0, imgWidth * imgHeight, pixels, 0);

			blockSize = 1;
			direction = 1;
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

	private ImageData filter(ImageData src, int size) {
		if (size <= 1) {
			int[] pixels = new int[imgWidth * imgHeight];
			src.getPixels(0, 0, imgWidth * imgHeight, pixels, 0);
			ImageData copy = new ImageData(imgWidth, imgHeight, src.depth, src.palette);
			copy.setPixels(0, 0, imgWidth * imgHeight, pixels, 0);
			return copy;
		}

		ImageData dst = new ImageData(imgWidth, imgHeight, src.depth, src.palette);
		int[] pixels = new int[size * size];

		for (int y = 0; y < imgHeight; y += size) {
			for (int x = 0; x < imgWidth; x += size) {
				int w = Math.min(size, imgWidth - x);
				int h = Math.min(size, imgHeight - y);
				int t = w * h;
				getRGB(src, x, y, w, h, pixels, 0, w);
				int r = 0, g = 0, b = 0;
				int i = 0;
				for (int by = 0; by < h; by++) {
					for (int bx = 0; bx < w; bx++) {
						int argb = pixels[i];
						r += (argb >> 16) & 0xff;
						g += (argb >> 8) & 0xff;
						b += argb & 0xff;
						i++;
					}
				}
				int argb = ((r / t) << 16) | ((g / t) << 8) | (b / t);
				i = 0;
				for (int by = 0; by < h; by++) {
					for (int bx = 0; bx < w; bx++) {
						pixels[i] = (pixels[i] & 0xff000000) | argb;
						i++;
					}
				}
				setRGB(dst, x, y, w, h, pixels, 0, w);
			}
		}
		return dst;
	}

	private static void getRGB(ImageData image, int startX, int startY, int w, int h, int[] pixels, int offset, int scansize) {
		int yoff = offset;
		for (int y = startY; y < startY + h; y++, yoff += scansize) {
			int off = yoff;
			for (int x = startX; x < startX + w; x++) {
				pixels[off++] = image.getPixel(x, y);
			}
		}
	}

	private static void setRGB(ImageData image, int startX, int startY, int w, int h, int[] pixels, int offset, int scansize) {
		int yoff = offset;
		for (int y = startY; y < startY + h; y++, yoff += scansize) {
			int off = yoff;
			for (int x = startX; x < startX + w; x++) {
				image.setPixel(x, y, pixels[off++]);
			}
		}
	}
}
