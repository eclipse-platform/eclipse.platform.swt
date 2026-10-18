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
import org.eclipse.swt.graphics.RGB;

/**
 * This tab displays an animated copper bars effect where colored horizontal
 * bars (red, white, blue) move up and down following sine wave patterns.
 */
public class CopperBarsTab extends AnimatedGraphicsTab {

	private int[] aSin;
	private int red, red3, red5, red7;
	private int white, white3, white5, white7;
	private int blue, blue3, blue5, blue7;
	private RGB[] colors;
	private Image bufferImage;
	private int lastWidth;
	private int lastHeight;

	public CopperBarsTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("CopperBars"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("CopperBarsDescription"); //$NON-NLS-1$
	}

	@Override
	public int getInitialAnimationTime() {
		return 10;
	}

	@Override
	public void dispose() {
		if (bufferImage != null) {
			bufferImage.dispose();
			bufferImage = null;
		}
	}

	@Override
	public void next(int width, int height) {
		if (aSin == null) {
			return;
		}
		if (width != lastWidth || height != lastHeight) {
			init(width, height);
		}

		if (bufferImage != null) {
			bufferImage.dispose();
		}
		bufferImage = new Image(null, width, height);
		GC bufferGC = new GC(bufferImage);
		bufferGC.setBackground(new Color(0, 0, 0));
		bufferGC.fillRectangle(0, 0, width, height);

		// Draw copper bars back to front
		drawCopperGroup(bufferGC, width, 31, blue7, blue5, blue3, blue);
		drawCopperGroup(bufferGC, width, 16, white7, white5, white3, white);
		drawCopperGroup(bufferGC, width, 1, red7, red5, red3, red);

		// Advance sine positions
		blue7 = (blue7 + 2) % 360;
		blue5 = (blue5 + 2) % 360;
		blue3 = (blue3 + 2) % 360;
		blue = (blue + 2) % 360;
		white7 = (white7 + 2) % 360;
		white5 = (white5 + 2) % 360;
		white3 = (white3 + 2) % 360;
		white = (white + 2) % 360;
		red7 = (red7 + 2) % 360;
		red5 = (red5 + 2) % 360;
		red3 = (red3 + 2) % 360;
		red = (red + 2) % 360;

		bufferGC.dispose();
	}

	private void drawCopperGroup(GC gc, int width, int colorOffset, int pos1, int pos2, int pos3, int pos4) {
		drawCopper(gc, width, aSin[pos1], colorOffset);
		drawCopper(gc, width, aSin[pos2], colorOffset);
		drawCopper(gc, width, aSin[pos3], colorOffset);
		drawCopper(gc, width, aSin[pos4], colorOffset);
	}

	private void drawCopper(GC gc, int width, int yPos, int colorOffset) {
		for (int i = 0; i < 15; i++) {
			gc.setBackground(new Color(colors[i + colorOffset].red, colors[i + colorOffset].green, colors[i + colorOffset].blue));
			gc.fillRectangle(0, yPos + i, width, 1);
		}
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (aSin == null) {
			init(width, height);
		}

		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		gc.fillRectangle(0, 0, width, height);

		if (bufferImage != null) {
			gc.drawImage(bufferImage, 0, 0);
		}
	}

	private void init(int width, int height) {
		lastWidth = width;
		lastHeight = height;

		int centery = height >> 1;
		aSin = new int[360];
		for (int i = 0; i < 360; i++) {
			float rad = i * 0.0174532f;
			aSin[i] = (int) (centery + Math.sin(rad) * 100.0);
		}

		colors = new RGB[46];
		for (int i = 0; i < 46; i++) {
			colors[i] = new RGB(0, 0, 0);
		}

		// Red copper bar
		int[] redVals = { 0x22, 0x44, 0x66, 0x88, 0xaa, 0xcc, 0xee, 0xff, 0xee, 0xcc, 0xaa, 0x88, 0x66, 0x44, 0x22 };
		for (int i = 0; i < 15; i++) {
			colors[1 + i].red = redVals[i];
		}

		// White copper bar
		for (int i = 0; i < 15; i++) {
			colors[16 + i].red = redVals[i];
			colors[16 + i].green = redVals[i];
			colors[16 + i].blue = redVals[i];
		}

		// Blue copper bar
		for (int i = 0; i < 15; i++) {
			colors[31 + i].blue = redVals[i];
		}

		red = 96;
		red3 = 88;
		red5 = 80;
		red7 = 72;
		white = 64;
		white3 = 56;
		white5 = 48;
		white7 = 40;
		blue = 32;
		blue3 = 24;
		blue5 = 16;
		blue7 = 8;
	}
}
