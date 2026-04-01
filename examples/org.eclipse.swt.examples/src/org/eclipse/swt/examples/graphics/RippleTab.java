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

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

/**
 * This tab displays a ripple effect on an image.
 */
public class RippleTab extends AnimatedGraphicsTab {

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
		if (Math.random() > 0.98) {
			disturb((int)(Math.random() * width), (int)(Math.random() * height), width, height);
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
		}

		offImageData.setPixels(0, 0, this.width * this.height, ripple, 0);
		
		if (outputImage != null) outputImage.dispose();
		outputImage = new Image(gc.getDevice(), offImageData);
		
		int x = (width - this.width) / 2;
		int y = (height - this.height) / 2;
		gc.drawImage(outputImage, x, y);
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
		
		// Adjust dx, dy based on center alignment in paint()
		int xOffset = (canvasWidth - width) / 2;
		int yOffset = (canvasHeight - height) / 2;
		
		int x = dx - xOffset;
		int y = dy - yOffset;

		for (int j = y - riprad; j < y + riprad; j++) {
			for (int k = x - riprad; k < x + riprad; k++) {
				if (j >= 0 && j < height && k >= 0 && k < width) {
					ripplemap[oldind + j * width + k] += 512;
				}
			}
		}
	}
}
