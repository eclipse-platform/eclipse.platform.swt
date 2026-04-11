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
 *     Guillaume Bouchon (bouchon_guillaume@yahoo.fr) - Original Version
 *     Laurent Caron (laurent.caron at gmail dot com) - Conversion to SWT
 *     IBM Corporation - adaptation to GraphicsExample
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

/**
 * This tab displays an animated isometric dancing shape with sine-wave
 * deformations across a grid of polygons.
 */
public class DancingTab extends AnimatedGraphicsTab {

	private int pw, ph;
	private float[][] pointsX;
	private float[][] pointsY;
	private float[][] pointsZ;
	private double dec;
	private int cw, ch, dx;
	private int waveCx, waveCy;
	private Image offscreenImage;
	private int lastWidth, lastHeight;

	public DancingTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Dancing"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("DancingDescription"); //$NON-NLS-1$
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
		if (pointsX == null || width != lastWidth || height != lastHeight) {
			init(width, height);
		}
		dec += 0.25;
		for (int x = 0; x < pw; x++) {
			for (int y = 0; y < ph; y++) {
				pointsX[x][y] = x * cw + y * dx;
				pointsZ[x][y] = (float) (-Math.sin(dec + 1.5 * Math.sqrt((x - waveCx) * (x - waveCx) + (y - waveCy) * (y - waveCy)) + ch) * dx);
				pointsY[x][y] = y * ch - pointsZ[x][y];
			}
		}
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (pointsX == null) {
			init(width, height);
		}

		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
		gc.fillRectangle(0, 0, width, height);

		if (offscreenImage != null) {
			offscreenImage.dispose();
		}
		offscreenImage = new Image(gc.getDevice(), width, height);
		GC imageGC = new GC(offscreenImage);
		imageGC.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
		imageGC.fillRectangle(0, 0, width, height);
		imageGC.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));

		int xc = ph / 2 * dx;
		int[] p = new int[8];

		for (int x = 0; x < pw - 1; x++) {
			for (int y = 0; y < ph - 1; y++) {
				int gray = 255 / ph * y;
				Color color = new Color(gray, gray, gray);
				imageGC.setBackground(color);

				p[0] = (int) (pointsX[x][y] - xc);
				p[2] = (int) (pointsX[x + 1][y] - xc);
				p[4] = (int) (pointsX[x + 1][y + 1] - xc);
				p[6] = (int) (pointsX[x][y + 1] - xc);

				p[1] = (int) pointsY[x][y];
				p[3] = (int) pointsY[x + 1][y];
				p[5] = (int) pointsY[x + 1][y + 1];
				p[7] = (int) pointsY[x][y + 1];

				imageGC.fillPolygon(p);
			}
		}
		imageGC.dispose();

		gc.drawImage(offscreenImage, 0, 0);
	}

	private void init(int width, int height) {
		lastWidth = width;
		lastHeight = height;
		cw = 15;
		ch = 15;
		dx = 10;

		pw = width / cw;
		ph = height / ch;
		if (pw < 2) pw = 2;
		if (ph < 2) ph = 2;

		waveCx = pw / 2;
		waveCy = ph / 2;

		pointsX = new float[pw][ph];
		pointsY = new float[pw][ph];
		pointsZ = new float[pw][ph];
		dec = 0;
	}
}
