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
 *     Laurent Caron (laurent.caron at gmail dot com) - Original Version
 *     IBM Corporation - adaptation to GraphicsExample
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;

/**
 * This tab displays an animated moiré pattern effect created by overlaying
 * two sets of concentric circles, one fixed and one moving in a
 * Lissajous-like path.
 */
public class MoireTab extends AnimatedGraphicsTab {

	private static final int STEP = 12;

	private Image bufferImage;
	private Image circleImage;
	private Image bigCircleImage;
	private float t;
	private int lastWidth;
	private int lastHeight;

	public MoireTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Moire"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("MoireDescription"); //$NON-NLS-1$
	}

	@Override
	public int getInitialAnimationTime() {
		return 10;
	}

	@Override
	public void dispose() {
		disposeImages();
	}

	private void disposeImages() {
		if (bufferImage != null) {
			bufferImage.dispose();
			bufferImage = null;
		}
		if (circleImage != null) {
			circleImage.dispose();
			circleImage = null;
		}
		if (bigCircleImage != null) {
			bigCircleImage.dispose();
			bigCircleImage = null;
		}
	}

	@Override
	public void next(int width, int height) {
		if (circleImage == null) {
			return;
		}
		if (width != lastWidth || height != lastHeight) {
			init(width, height);
		}

		t += 0.02f;
		double posX = 100 + 50 * Math.cos(t) + 50 * Math.cos(3 * t) - width;
		double posY = 100 + 50 * Math.sin(t) + 40 * Math.sin(Math.sqrt(2) * t) - height;

		if (bufferImage != null) {
			bufferImage.dispose();
		}
		bufferImage = new Image(null, width, height);
		GC bufferGC = new GC(bufferImage);
		bufferGC.setBackground(new org.eclipse.swt.graphics.Color(255, 255, 255));
		bufferGC.fillRectangle(0, 0, width, height);
		bufferGC.drawImage(circleImage, 0, 0);
		bufferGC.drawImage(bigCircleImage, (int) posX, (int) posY);
		bufferGC.dispose();
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (circleImage == null) {
			init(width, height);
		}

		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
		gc.fillRectangle(0, 0, width, height);

		if (bufferImage != null) {
			gc.drawImage(bufferImage, 0, 0);
		}
	}

	private void init(int width, int height) {
		lastWidth = width;
		lastHeight = height;
		disposeImages();

		circleImage = createCircles(null, width / 2, height / 2, width, height);

		Image temp = createCircles(null, width, height, width * 4, height * 3);
		ImageData data = temp.getImageData();
		data.transparentPixel = data.palette.getPixel(new RGB(255, 255, 255));
		bigCircleImage = new Image(null, data);
		temp.dispose();

		t = 0;
	}

	private Image createCircles(Object unused, int centerX, int centerY, int width, int height) {
		Image img = new Image(null, width, height);
		GC tempGC = new GC(img);
		tempGC.setBackground(new org.eclipse.swt.graphics.Color(255, 255, 255));
		tempGC.fillRectangle(0, 0, width, height);
		tempGC.setForeground(new org.eclipse.swt.graphics.Color(0, 0, 0));
		tempGC.setLineWidth(3);
		tempGC.setAntialias(SWT.ON);

		for (int i = 0; i < Math.max(width, height) * 2; i += STEP) {
			tempGC.drawOval(centerX - i / 2, centerY - i / 2, i, i);
		}

		tempGC.dispose();
		return img;
	}
}
