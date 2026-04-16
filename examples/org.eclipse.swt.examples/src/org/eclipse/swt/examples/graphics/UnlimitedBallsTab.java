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
 *     Laurent Caron (laurent.caron at gmail dot com) - Conversion to SWT
 *     IBM Corporation - adaptation to GraphicsExample
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

/**
 * This tab displays an animated unlimited balls effect where ball sprites are
 * drawn along Lissajous-like curves using multiple offscreen buffers.
 */
public class UnlimitedBallsTab extends AnimatedGraphicsTab {

	private Image[] offscreenImages;
	private Image ball;
	private int xCenter, yCenter;
	private float xRadius, yRadius;
	private int count;
	private int current;
	private int xspeed, yspeed, xrspeed, yrspeed, xstart, ystart, xrstart, yrstart;
	private int currentShape;
	private int canvasWidth, canvasHeight;

	public UnlimitedBallsTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("UnlimitedBalls"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("UnlimitedBallsDescription"); //$NON-NLS-1$
	}

	@Override
	public int getInitialAnimationTime() {
		return 10;
	}

	@Override
	public void dispose() {
		if (offscreenImages != null) {
			for (Image img : offscreenImages) {
				if (img != null) {
					img.dispose();
				}
			}
			offscreenImages = null;
		}
		if (ball != null) {
			ball.dispose();
			ball = null;
		}
	}

	@Override
	public void next(int width, int height) {
		if (offscreenImages == null || ball == null) {
			return;
		}

		if (width != canvasWidth || height != canvasHeight) {
			initBuffers(width, height);
		}

		count = (count + 1) % 8;
		for (int i = 0; i < 8; i++) {
			double xr = Math.cos((current + xrstart) * Math.PI / xrspeed) * xRadius;
			double yr = Math.cos((current + yrstart) * Math.PI / yrspeed) * yRadius;
			double x = Math.cos((current + xstart) * Math.PI / xspeed) * xr;
			double y = Math.sin((current + ystart) * Math.PI / yspeed) * yr;
			GC tempGC = new GC(offscreenImages[i]);
			tempGC.drawImage(ball, (int) (xCenter + Math.round(x)), (int) (yCenter + Math.round(y)));
			tempGC.dispose();
			current++;
		}
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;

		if (offscreenImages == null) {
			ball = example.loadImage(gc.getDevice(), "ball.gif"); //$NON-NLS-1$
			if (ball == null) return;

			currentShape = 0;
			count = 0;
			current = 0;
			canvasWidth = width;
			canvasHeight = height;
			initBuffers(width, height);
		}

		if (offscreenImages == null) {
			return;
		}

		int x = (width - canvasWidth) / 2;
		int y = (height - canvasHeight) / 2;
		gc.drawImage(offscreenImages[count], x, y);
	}

	private void initBuffers(int width, int height) {
		if (offscreenImages != null) {
			for (Image img : offscreenImages) {
				if (img != null) {
					img.dispose();
				}
			}
		}

		canvasWidth = width;
		canvasHeight = height;
		xCenter = width / 2 - 8;
		yCenter = height / 2 - 8;
		xRadius = width / 2.5f;
		yRadius = height / 2.5f;
		count = 0;
		current = 0;

		offscreenImages = new Image[8];
		for (int i = 0; i < 8; i++) {
			Image image = new Image(null, width, height);
			GC tempGC = new GC(image);
			tempGC.setBackground(new Color(255, 255, 255));
			tempGC.fillRectangle(0, 0, width, height);
			tempGC.dispose();
			offscreenImages[i] = image;
		}

		switch (currentShape) {
			case 0:
				xspeed = 100;
				yspeed = 100;
				xrspeed = 4150;
				yrspeed = 4150;
				xstart = 0;
				ystart = 0;
				xrstart = 0;
				yrstart = 4150;
				break;
			case 1:
				xspeed = 199999;
				yspeed = 100;
				xrspeed = 550;
				yrspeed = 950;
				xstart = 0;
				ystart = 50;
				xrstart = 0;
				yrstart = 0;
				break;
			default:
				xspeed = 100;
				yspeed = 150;
				xrspeed = 2200;
				yrspeed = 1100;
				xstart = 0;
				ystart = 150;
				xrstart = 0;
				yrstart = 1100;
				break;
		}

		currentShape = (currentShape + 1) % 3;
	}
}
