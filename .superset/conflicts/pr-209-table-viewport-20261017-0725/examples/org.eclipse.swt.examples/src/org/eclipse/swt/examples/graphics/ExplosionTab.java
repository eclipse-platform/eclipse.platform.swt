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
 * This tab displays an animated explosion effect using a particle system
 * and fire propagation algorithm.
 */
public class ExplosionTab extends AnimatedGraphicsTab {

	private static final int NUMBER_OF_PARTICLES = 500;

	private ImageData imageData;
	private Image outputImage;
	private int[] fire;
	private Particle[] particles;
	private RGB[] colors;
	private int lastWidth;
	private int lastHeight;

	private static class Particle {
		int xpos, ypos, xdir, ydir;
		int colorindex;
		boolean dead;
	}

	public ExplosionTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Explosion"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("ExplosionDescription"); //$NON-NLS-1$
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
		if (fire == null) {
			return;
		}
		if (width != lastWidth || height != lastHeight) {
			init(width, height);
		}

		// Move and draw particles into fire array
		int nbDead = 0;
		for (int i = 0; i < NUMBER_OF_PARTICLES; i++) {
			if (!particles[i].dead) {
				particles[i].xpos += particles[i].xdir;
				particles[i].ypos += particles[i].ydir;

				if (particles[i].ypos >= height - 3 || particles[i].colorindex == 0
						|| particles[i].xpos <= 1 || particles[i].xpos >= width - 3) {
					particles[i].dead = true;
					continue;
				}

				particles[i].ydir++;
				particles[i].colorindex--;

				final int temp = particles[i].ypos * width + particles[i].xpos;
				fire[temp] = particles[i].colorindex;
				fire[temp - 1] = particles[i].colorindex;
				fire[temp + width] = particles[i].colorindex;
				fire[temp - width] = particles[i].colorindex;
				fire[temp + 1] = particles[i].colorindex;
			} else {
				nbDead++;
			}
		}

		// Create fire effect
		for (int i = 1; i < height - 2; i++) {
			final int index = (i - 1) * width;
			for (int j = 1; j < width - 2; j++) {
				int buf = index + j;
				int temp = fire[buf];
				temp += fire[buf + 1];
				temp += fire[buf - 1];
				buf += width;
				temp += fire[buf - 1];
				temp += fire[buf + 1];
				buf += width;
				temp += fire[buf];
				temp += fire[buf + 1];
				temp += fire[buf - 1];
				temp >>= 3;
				if (temp > 4) {
					temp -= 4;
				} else {
					temp = 0;
				}
				fire[buf - width] = temp;
			}
		}

		// Draw fire array to image from bottom to top
		int pixel = width * height - 1;
		for (int i = height - 1; i >= 0; --i) {
			final int temp = i * width;
			for (int j = width - 1; j >= 0; --j) {
				imageData.setPixel(pixel % width, pixel / width, fire[temp + j]);
				pixel--;
			}
		}

		if (nbDead == NUMBER_OF_PARTICLES) {
			initParticles(width, height);
		}
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (fire == null) {
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

		fire = new int[width * height];
		particles = new Particle[NUMBER_OF_PARTICLES];

		colors = new RGB[256];
		for (int i = 0; i < 32; ++i) {
			colors[i] = new RGB(0, 0, i << 1);
			colors[i + 32] = new RGB(i << 3, 0, 64 - (i << 1));
			colors[i + 64] = new RGB(255, i << 3, 0);
			colors[i + 96] = new RGB(255, 255, i << 2);
			colors[i + 128] = new RGB(255, 255, 64 + (i << 2));
			colors[i + 160] = new RGB(255, 255, 128 + (i << 2));
			colors[i + 192] = new RGB(255, 255, 192 + i);
			colors[i + 224] = new RGB(255, 255, 224 + i);
		}

		imageData = new ImageData(width, height, 8, new PaletteData(colors));

		for (int i = 0; i < NUMBER_OF_PARTICLES; i++) {
			particles[i] = new Particle();
		}
		initParticles(width, height);
	}

	private void initParticles(int width, int height) {
		for (int i = 0; i < NUMBER_OF_PARTICLES; i++) {
			particles[i].xpos = (width >> 1) - 20 + (int) (40.0 * Math.random());
			particles[i].ypos = (height >> 1) - 20 + (int) (40.0 * Math.random());
			particles[i].xdir = -10 + (int) (20.0 * Math.random());
			particles[i].ydir = -17 + (int) (19.0 * Math.random());
			particles[i].colorindex = 255;
			particles[i].dead = false;
		}
	}
}
