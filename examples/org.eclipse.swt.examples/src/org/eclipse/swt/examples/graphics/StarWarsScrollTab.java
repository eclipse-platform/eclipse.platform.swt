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
 *     Laurent Caron (laurent.caron at gmail dot com) - Initial Contributor
 *     IBM Corporation - adaptation to GraphicsExample
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * This tab displays a "Star Wars" style scroller: text scrolls up the canvas
 * and a row-wise scaling transform tapers the lines toward the horizon to
 * give a perspective effect.
 */
public class StarWarsScrollTab extends AnimatedGraphicsTab {

	private static final int FONT_SIZE = 16;
	private static final int STEP = 2;
	private static final String[] LINES = {
		"SWT Graphics Example",
		"Star Wars Scroll Effect",
		"",
		"Original by Laurent Caron",
		"",
		"Lines of text scroll up",
		"the screen with a perspective",
		"that tapers toward the horizon.",
		"",
		"Enjoy the show!",
	};

	private int y = -1;
	private int lastWidth, lastHeight;
	private Image textImage;
	private Image outputImage;
	private int textHeight;

	public StarWarsScrollTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("StarWarsScroll"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("StarWarsScrollDescription"); //$NON-NLS-1$
	}

	@Override
	public int getInitialAnimationTime() {
		return 50;
	}

	@Override
	public void dispose() {
		if (textImage != null) {
			textImage.dispose();
			textImage = null;
		}
		if (outputImage != null) {
			outputImage.dispose();
			outputImage = null;
		}
	}

	@Override
	public void next(int width, int height) {
		if (y < 0 || width != lastWidth || height != lastHeight) {
			reset(width, height);
		}
		y -= STEP;
		if (y + textHeight <= 0) {
			y = height + 10;
		}
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;
		if (y < 0 || width != lastWidth || height != lastHeight) {
			reset(width, height);
		}
		if (textImage == null) {
			buildTextImage(gc, width);
		}

		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		gc.fillRectangle(0, 0, width, height);

		ImageData srcData = textImage.getImageData();
		ImageData outData = new ImageData(width, height, srcData.depth, srcData.palette);

		int vanishingY = 0;
		int baseY = height;
		for (int row = 0; row < height; row++) {
			int srcRow = row - y;
			if (srcRow < 0 || srcRow >= textHeight) continue;

			double t = (double) (row - vanishingY) / (baseY - vanishingY);
			if (t < 0.01) t = 0.01;
			int rowWidth = (int) (width * t);
			if (rowWidth <= 0) continue;
			int offsetX = (width - rowWidth) / 2;

			for (int dx = 0; dx < rowWidth; dx++) {
				int sx = (int) ((double) dx * srcData.width / rowWidth);
				if (sx < 0) sx = 0;
				if (sx >= srcData.width) sx = srcData.width - 1;
				outData.setPixel(offsetX + dx, row, srcData.getPixel(sx, srcRow));
			}
		}

		if (outputImage != null) {
			outputImage.dispose();
		}
		outputImage = new Image(gc.getDevice(), outData);
		gc.drawImage(outputImage, 0, 0);
	}

	private void reset(int width, int height) {
		lastWidth = width;
		lastHeight = height;
		y = height + 10;
		if (textImage != null) {
			textImage.dispose();
			textImage = null;
		}
	}

	private void buildTextImage(GC canvasGC, int width) {
		Font font = new Font(canvasGC.getDevice(), "Lucida Sans", FONT_SIZE, SWT.NORMAL); //$NON-NLS-1$
		GC measure = new GC(canvasGC.getDevice());
		measure.setFont(font);
		int lineHeight = (int) (measure.getFontMetrics().getHeight() * 1.5);
		measure.dispose();

		textHeight = Math.max(1, lineHeight * LINES.length);
		textImage = new Image(canvasGC.getDevice(), width, textHeight);
		GC g = new GC(textImage);
		g.setAdvanced(true);
		g.setAntialias(SWT.ON);
		g.setBackground(canvasGC.getDevice().getSystemColor(SWT.COLOR_BLACK));
		g.setForeground(canvasGC.getDevice().getSystemColor(SWT.COLOR_WHITE));
		g.fillRectangle(0, 0, width, textHeight);
		g.setFont(font);

		int cy = 0;
		for (String line : LINES) {
			Point ts = g.stringExtent(line);
			int lx = (width - ts.x) / 2;
			g.drawString(line, lx, cy, true);
			cy += lineHeight;
		}
		g.dispose();
		font.dispose();
	}
}
