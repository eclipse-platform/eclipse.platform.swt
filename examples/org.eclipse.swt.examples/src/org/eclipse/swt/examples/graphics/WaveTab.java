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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

/**
 * This tab displays an animated wave effect with multiple motion patterns.
 * The effect cycles through four different wave algorithms automatically.
 */
public class WaveTab extends AnimatedGraphicsTab {

	private static final double STEP = 0.07;
	private static final int COLS = 30;
	private static final int LINES = 20;

	private Image offscreenImage;
	private int[][] xPos;
	private int[][] yPos;
	private double position;
	private int kind;
	private int patternWidth;
	private int patternHeight;
	private int centerX;
	private int centerY;
	private int lastWidth;
	private int lastHeight;
	private int frameCount;

	public WaveTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Wave"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("WaveDescription"); //$NON-NLS-1$
	}

	@Override
	public int getInitialAnimationTime() {
		return 10;
	}

	@Override
	public void dispose() {
		if (offscreenImage != null) {
			offscreenImage.dispose();
			offscreenImage = null;
		}
	}

	@Override
	public void next(int width, int height) {
		if (xPos == null) {
			return;
		}
		if (width != lastWidth || height != lastHeight) {
			init(width, height);
		}

		// Auto-cycle through effects
		frameCount++;
		if (frameCount % 300 == 0) {
			kind = (kind + 1) % 4;
		}

		computePositions(width, height);
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (xPos == null) {
			init(width, height);
		}

		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		gc.fillRectangle(0, 0, width, height);

		if (offscreenImage != null) {
			offscreenImage.dispose();
		}
		offscreenImage = new Image(gc.getDevice(), width, height);
		GC imageGC = new GC(offscreenImage);
		imageGC.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		imageGC.fillRectangle(0, 0, width, height);

		Color white = new Color(255, 255, 255);
		imageGC.setForeground(white);

		for (int i = 0; i < COLS; ++i) {
			for (int j = 0; j < LINES; ++j) {
				int px = xPos[i][j];
				int py = yPos[i][j];
				if (px >= 0 && px < width - 1 && py >= 0 && py < height - 1) {
					imageGC.drawPoint(px, py);
					imageGC.drawPoint(px + 1, py);
					imageGC.drawPoint(px, py + 1);
					imageGC.drawPoint(px + 1, py + 1);
				}
			}
		}

		imageGC.dispose();
		gc.drawImage(offscreenImage, 0, 0);
	}

	private void init(int width, int height) {
		lastWidth = width;
		lastHeight = height;
		position = 0.0;
		kind = 0;
		frameCount = 0;
		xPos = new int[COLS][LINES];
		yPos = new int[COLS][LINES];

		patternWidth = (int) (0.5 * (width - (COLS - 1) * 15)) - 3;
		patternHeight = (int) (0.5 * (height - (LINES - 1) * 15)) - 3;
		centerX = (int) (0.5 * width);
		centerY = (int) (0.5 * height);
	}

	private void computePositions(int width, int height) {
		position += STEP;

		for (int i = 0; i < COLS; ++i) {
			for (int j = 0; j < LINES; ++j) {
				switch (kind) {
				case 0:
					xPos[i][j] = (int) (patternWidth + 15 * i
							+ 10.0 * Math.cos(position * (1.0 + 0.01 * i + 0.015 * j)));
					yPos[i][j] = (int) (patternHeight + 15 * j
							- 10.0 * Math.sin(position * (1.0 + 0.0123 * j + 0.012 * i)));
					break;
				case 1:
					xPos[i][j] = (int) (patternWidth + 15 * i
							+ 20.0 * Math.sin(position * (1.0 + 0.0059 * j + 0.00639 * i))
									* Math.cos(position + 0.3 * i + 0.3 * j));
					yPos[i][j] = (int) (patternHeight + 15 * j
							- 20.0 * Math.cos(position * (1.0 - 0.073 * j + 0.00849 * i))
									* Math.sin(position + 0.23 * j + 0.389 * i));
					break;
				case 2:
					xPos[i][j] = (int) (centerX + 14.0 * i * Math.cos(0.01 * (40.0 - i) * position + j));
					yPos[i][j] = (int) (centerY - 14.0 * i * Math.sin(0.01 * (40.0 - i) * position + j));
					break;
				case 3:
					double d1 = 1.0;
					int k = 0;
					if (i >= 2 && i < 5) { d1 = -1.0; k = 1; }
					else if (i >= 5 && i < 8) { d1 = 1.0; k = 2; }
					else if (i >= 8 && i < 12) { d1 = -1.0; k = 3; }
					else if (i >= 12 && i < 17) { d1 = 1.0; k = 4; }
					else if (i >= 17 && i < 23) { d1 = -1.0; k = 5; }
					else if (i >= 23 && i < 31) { d1 = 1.0; k = 6; }
					xPos[i][j] = (int) (centerX
							+ 20.0 * (k + 4) * Math.cos(0.1 * position * d1 + j + 0.786 * i));
					yPos[i][j] = (int) (centerY
							- 20.0 * (k + 4) * Math.sin(0.1 * position * d1 + j + 0.786 * i));
					break;
				}
			}
		}
	}
}
