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
 * This tab displays an animated twirl distortion effect that rotates pixels
 * around the center of an image with increasing and decreasing twirl angle.
 */
public class TwirlEffectTab extends AnimatedGraphicsTab {

	private static final float STEP = 0.1f;
	private static final int DISPLAY_SCALE = 3;

	private ImageData sourceImage;
	private ImageData imageData;
	private Image outputImage;
	private int imgWidth, imgHeight;
	private float angle;
	private float icentreX, icentreY;
	private float radius, radius2;
	private float direction;

	public TwirlEffectTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("TwirlEffect"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("TwirlEffectDescription"); //$NON-NLS-1$
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

		imageData = filter(sourceImage);
		angle += direction;

		if (angle < -15.7f) {
			direction = STEP;
		}
		if (angle > 15.7f) {
			direction = -STEP;
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

			angle = 0;
			direction = STEP;
			icentreX = imgWidth * 0.5f;
			icentreY = imgHeight * 0.5f;
			radius = 100;
			radius2 = radius * radius;
		}

		if (imageData == null) {
			return;
		}

		if (outputImage != null) {
			outputImage.dispose();
		}
		outputImage = new Image(gc.getDevice(), imageData);

		int dw = imgWidth * DISPLAY_SCALE;
		int dh = imgHeight * DISPLAY_SCALE;
		int x = (width - dw) / 2;
		int y = (height - dh) / 2;
		gc.drawImage(outputImage, 0, 0, imgWidth, imgHeight, x, y, dw, dh);
	}

	private ImageData filter(ImageData src) {
		ImageData dst = new ImageData(imgWidth, imgHeight, src.depth, src.palette);
		int[] inPixels = new int[imgWidth * imgHeight];
		src.getPixels(0, 0, imgWidth * imgHeight, inPixels, 0);

		int[] outPixels = new int[imgWidth];
		float[] out = new float[2];

		for (int y = 0; y < imgHeight; y++) {
			for (int x = 0; x < imgWidth; x++) {
				transformInverse(x, y, out);
				int srcX = (int) Math.floor(out[0]);
				int srcY = (int) Math.floor(out[1]);
				float xWeight = out[0] - srcX;
				float yWeight = out[1] - srcY;
				int nw, ne, sw, se;

				if (srcX >= 0 && srcX < imgWidth - 1 && srcY >= 0 && srcY < imgHeight - 1) {
					int i = imgWidth * srcY + srcX;
					nw = inPixels[i];
					ne = inPixels[i + 1];
					sw = inPixels[i + imgWidth];
					se = inPixels[i + imgWidth + 1];
				} else {
					nw = getPixel(inPixels, srcX, srcY);
					ne = getPixel(inPixels, srcX + 1, srcY);
					sw = getPixel(inPixels, srcX, srcY + 1);
					se = getPixel(inPixels, srcX + 1, srcY + 1);
				}
				outPixels[x] = bilinearInterpolate(xWeight, yWeight, nw, ne, sw, se);
			}
			setRGB(dst, 0, y, imgWidth, 1, outPixels, 0, imgWidth);
		}
		return dst;
	}

	private int getPixel(int[] pixels, int x, int y) {
		return pixels[clamp(y, 0, imgHeight - 1) * imgWidth + clamp(x, 0, imgWidth - 1)];
	}

	private void transformInverse(int x, int y, float[] out) {
		float dx = x - icentreX;
		float dy = y - icentreY;
		float distance = dx * dx + dy * dy;
		if (distance > radius2) {
			out[0] = x;
			out[1] = y;
		} else {
			distance = (float) Math.sqrt(distance);
			float a = (float) Math.atan2(dy, dx) + angle * (radius - distance) / radius;
			out[0] = icentreX + distance * (float) Math.cos(a);
			out[1] = icentreY + distance * (float) Math.sin(a);
		}
	}

	private static int bilinearInterpolate(float x, float y, int nw, int ne, int sw, int se) {
		float cx = 1.0f - x;
		float cy = 1.0f - y;

		int r = (int) (cy * (cx * ((nw >> 16) & 0xff) + x * ((ne >> 16) & 0xff))
				+ y * (cx * ((sw >> 16) & 0xff) + x * ((se >> 16) & 0xff)));
		int g = (int) (cy * (cx * ((nw >> 8) & 0xff) + x * ((ne >> 8) & 0xff))
				+ y * (cx * ((sw >> 8) & 0xff) + x * ((se >> 8) & 0xff)));
		int b = (int) (cy * (cx * (nw & 0xff) + x * (ne & 0xff))
				+ y * (cx * (sw & 0xff) + x * (se & 0xff)));
		int a = (int) (cy * (cx * ((nw >> 24) & 0xff) + x * ((ne >> 24) & 0xff))
				+ y * (cx * ((sw >> 24) & 0xff) + x * ((se >> 24) & 0xff)));

		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	private static int clamp(int x, int a, int b) {
		return x < a ? a : x > b ? b : x;
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
