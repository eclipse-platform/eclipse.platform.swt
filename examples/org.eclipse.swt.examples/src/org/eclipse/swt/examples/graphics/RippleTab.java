/*******************************************************************************
 * Copyright (c) 2026 Laurent Caron and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Neil Wallis (http://www.neilwallis.com/java/water.html) - Original Version
 *     Laurent Caron (laurent.caron at gmail dot com) - Conversion to SWT
 *     IBM Corporation - adaptation to GraphicsExample
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

/**
 * This tab displays a ripple effect on an image.
 */
public class RippleTab extends AnimatedGraphicsTab {

	private static final int DISPLAY_SCALE = 2;

	private Image sourceImage;
	private ImageData offImageData;
	private int width, height;
	private int hwidth, hheight;
	private int riprad;
	private int size;
	private short[] ripplemap;
	private int[] ripple;
	private int[] texture;
	private int oldind;
	private int newind;
	private Image outputImage;
	private int frameCount;

	public RippleTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Ripple"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("RippleDescription"); //$NON-NLS-1$
	}

	@Override
	public int getInitialAnimationTime() {
		return 30;
	}

	@Override
	public void createControlPanel(Composite parent) {
		super.createControlPanel(parent);
		// Add mouse listener to the canvas to disturb the water
		example.canvas.addMouseMoveListener(e -> {
			if (example.getTab() == RippleTab.this) {
				Rectangle rect = example.canvas.getClientArea();
				disturb(e.x, e.y, rect.width, rect.height);
			}
		});
	}

	@Override
	public void dispose() {
		if (sourceImage != null) {
			sourceImage.dispose();
			sourceImage = null;
		}
		if (outputImage != null) {
			outputImage.dispose();
			outputImage = null;
		}
	}

	@Override
	public void next(int width, int height) {
		if (texture == null) return;
		// Automatic rain: drop a ripple every ~15 frames at a random location,
		// plus occasional extra drops so the effect is obvious without user
		// interaction. Mouse movement still adds further disturbances.
		frameCount++;
		if (frameCount % 15 == 0) {
			disturb((int) (Math.random() * width), (int) (Math.random() * height), width, height);
		}
		if (Math.random() > 0.9) {
			disturb((int) (Math.random() * width), (int) (Math.random() * height), width, height);
		}
		newframe();
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;
		
		if (sourceImage == null) {
			sourceImage = example.loadImage(gc.getDevice(), "ocean.jpg");
			if (sourceImage == null) return;
			
			ImageData imgData = sourceImage.getImageData();
			this.width = imgData.width;
			this.height = imgData.height;
			hwidth = this.width >> 1;
			hheight = this.height >> 1;
			riprad = 3;

			size = this.width * (this.height + 2) * 2;
			ripplemap = new short[size];
			ripple = new int[this.width * this.height];
			texture = new int[this.width * this.height];
			oldind = this.width;
			newind = this.width * (this.height + 3);

			imgData.getPixels(0, 0, this.width * this.height, texture, 0);
			offImageData = new ImageData(this.width, this.height, imgData.depth, imgData.palette);

			// Seed a handful of ripples so the effect is visible immediately.
			for (int i = 0; i < 6; i++) {
				disturb((int) (Math.random() * width), (int) (Math.random() * height), width, height);
			}
		}

		offImageData.setPixels(0, 0, this.width * this.height, ripple, 0);

		if (outputImage != null) outputImage.dispose();
		outputImage = new Image(gc.getDevice(), offImageData);

		int dw = this.width * DISPLAY_SCALE;
		int dh = this.height * DISPLAY_SCALE;
		int x = (width - dw) / 2;
		int y = (height - dh) / 2;
		gc.drawImage(outputImage, 0, 0, this.width, this.height, x, y, dw, dh);

		String hint = GraphicsExample.getResourceString("RippleHint"); //$NON-NLS-1$
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
		Point textSize = gc.textExtent(hint);
		int tx = x + (dw - textSize.x) / 2;
		int ty = y + dh - textSize.y - 8;
		gc.setAlpha(180);
		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		gc.fillRectangle(tx - 6, ty - 4, textSize.x + 12, textSize.y + 8);
		gc.setAlpha(255);
		gc.drawString(hint, tx, ty, true);
	}

	private void newframe() {
		int i, a, b;
		// Toggle maps each frame
		i = oldind;
		oldind = newind;
		newind = i;

		i = 0;
		int mapind = oldind;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				short data = (short) (ripplemap[mapind - width] + ripplemap[mapind + width] + ripplemap[mapind - 1] + ripplemap[mapind + 1] >> 1);
				data -= ripplemap[newind + i];
				data -= data >> 5;
				ripplemap[newind + i] = data;

				// where data=0 then still, where data>0 then wave
				data = (short) (1024 - data);

				// offsets
				a = (x - hwidth) * data / 1024 + hwidth;
				b = (y - hheight) * data / 1024 + hheight;

				// bounds check
				if (a >= width) {
					a = width - 1;
				}
				if (a < 0) {
					a = 0;
				}
				if (b >= height) {
					b = height - 1;
				}
				if (b < 0) {
					b = 0;
				}

				ripple[i] = texture[a + b * width];
				mapind++;
				i++;
			}
		}
	}

	private void disturb(int dx, int dy, int canvasWidth, int canvasHeight) {
		if (texture == null) return;

		// Adjust dx, dy based on center alignment and display scale in paint()
		int dw = width * DISPLAY_SCALE;
		int dh = height * DISPLAY_SCALE;
		int xOffset = (canvasWidth - dw) / 2;
		int yOffset = (canvasHeight - dh) / 2;

		int x = (dx - xOffset) / DISPLAY_SCALE;
		int y = (dy - yOffset) / DISPLAY_SCALE;

		for (int j = y - riprad; j < y + riprad; j++) {
			for (int k = x - riprad; k < x + riprad; k++) {
				if (j >= 0 && j < height && k >= 0 && k < width) {
					ripplemap[oldind + j * width + k] += 512;
				}
			}
		}
	}
}
