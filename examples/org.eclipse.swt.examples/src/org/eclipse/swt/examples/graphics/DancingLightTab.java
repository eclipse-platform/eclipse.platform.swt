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
 * This tab displays an animated isometric dancing shape illuminated by a
 * moving point light source.
 */
public class DancingLightTab extends AnimatedGraphicsTab {

	private static final float LIGHT_R = 255f;
	private static final float LIGHT_G = 255f;
	private static final float LIGHT_B = 0f;
	private static final double DIST_ATTENUATION = 45.0;
	private static final double WAVE_FREQ = 1.0;

	private int pw, ph;
	private float[][] pointsX;
	private float[][] pointsY;
	private float[][] pointsZ;
	private double dec;
	private double decl;
	private int cw, ch, dx;
	private int waveCx, waveCy;
	private int centerX, centerY;
	private float lightX, lightY, lightZ = 10;
	private Image offscreenImage;
	private int lastWidth, lastHeight;

	public DancingLightTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("DancingLight"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("DancingLightDescription"); //$NON-NLS-1$
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
		decl += 0.05;

		lightY = (float) (centerY + Math.cos(decl) * (height / 2.0));
		lightX = (float) (centerX + Math.sin(decl) * (width / 2.0));

		for (int x = 0; x < pw; x++) {
			for (int y = 0; y < ph; y++) {
				pointsX[x][y] = x * cw + y * dx;
				pointsZ[x][y] = (float) (-Math.sin(dec + WAVE_FREQ
						* Math.sqrt((x - waveCx) * (x - waveCx) + (y - waveCy) * (y - waveCy)) + ch) * dx);
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
				float cx = (pointsX[x][y] + pointsX[x + 1][y] + pointsX[x + 1][y + 1] + pointsX[x][y + 1]) / 4f;
				float cy = (pointsY[x][y] + pointsY[x + 1][y] + pointsY[x + 1][y + 1] + pointsY[x][y + 1]) / 4f;
				float cz = (pointsZ[x][y] + pointsZ[x + 1][y] + pointsZ[x + 1][y + 1] + pointsZ[x][y + 1]) / 4f;

				float vx = lightX - cx;
				float vy = lightY - cy;
				float vz = lightZ - cz;
				float dist = (float) Math.sqrt(vx * vx + vy * vy + vz * vz);

				float ux = pointsX[x][y + 1] - pointsX[x][y];
				float uy = pointsY[x][y + 1] - pointsY[x][y];
				float uz = pointsZ[x][y + 1] - pointsZ[x][y];
				float uNorm = (float) Math.sqrt(ux * ux + uy * uy + uz * uz);

				double a;
				if (uNorm == 0 || dist == 0) {
					a = Math.PI / 2;
				} else {
					ux /= uNorm; uy /= uNorm; uz /= uNorm;
					float nvx = vx / dist;
					float nvy = vy / dist;
					float nvz = vz / dist;
					double dot = ux * nvx + uy * nvy + uz * nvz;
					if (dot < -1) dot = -1;
					if (dot > 1) dot = 1;
					a = Math.abs(Math.acos(dot));
				}

				double attenuation = dist / DIST_ATTENUATION;
				if (attenuation < 0.001) attenuation = 0.001;
				int colr = clamp((int) ((Math.PI - a) * LIGHT_R / attenuation));
				int colg = clamp((int) ((Math.PI - a) * LIGHT_G / attenuation));
				int colb = clamp((int) ((Math.PI - a) * LIGHT_B / attenuation));

				Color color = new Color(colr, colg, colb);
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

	private static int clamp(int v) {
		if (v < 0) return 0;
		if (v > 255) return 255;
		return v;
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
		decl = 0;

		int xc = ph / 2 * dx;
		centerX = cw * pw / 2 + xc;
		centerY = ch * ph / 2;
		lightX = centerX;
		lightY = centerY;
	}
}
