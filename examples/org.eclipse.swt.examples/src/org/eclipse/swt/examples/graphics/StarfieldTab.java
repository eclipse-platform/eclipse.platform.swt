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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;

/**
 * This tab displays a starfield animation.
 */
public class StarfieldTab extends AnimatedGraphicsTab {

	private static final int NUMBER_OF_STARS = 1020;
	private Star[] stars;

	static class Star {
		float xpos, ypos;
		int zpos, speed;
		int color;
	}

	public StarfieldTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Starfield"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("StarfieldDescription"); //$NON-NLS-1$
	}

	@Override
	public int getInitialAnimationTime() {
		return 10;
	}

	@Override
	public void next(int width, int height) {
		if (stars == null) return;

		for (int i = 0; i < NUMBER_OF_STARS; i++) {
			stars[i].zpos -= stars[i].speed;

			if (stars[i].zpos <= 0) {
				stars[i] = initStar(i + 1);
			}

			/* compute 3D position */
			final int tempx = (int) (stars[i].xpos / stars[i].zpos + width / 2);
			final int tempy = (int) (stars[i].ypos / stars[i].zpos + height / 2);

			if (tempx < 0 || tempx > width - 1 || tempy < 0 || tempy > height - 1) /* check if a star leaves the screen */
			{
				stars[i] = initStar(i + 1);
			}
		}
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (stars == null) {
			stars = new Star[NUMBER_OF_STARS];
			for (int i = 0; i < NUMBER_OF_STARS; i++) {
				stars[i] = initStar(i + 1);
			}
		}

		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		gc.fillRectangle(0, 0, width, height);

		for (int i = 0; i < NUMBER_OF_STARS; i++) {
			/* compute 3D position */
			final int tempx = (int) (stars[i].xpos / stars[i].zpos + width / 2);
			final int tempy = (int) (stars[i].ypos / stars[i].zpos + height / 2);

			if (tempx >= 0 && tempx < width && tempy >= 0 && tempy < height) {
				Color color = new Color(gc.getDevice(), stars[i].color, stars[i].color, stars[i].color);
				gc.setForeground(color);
				gc.drawPoint(tempx, tempy);
				color.dispose();
			}
		}
	}

	private Star initStar(int i) {
		final Star star = new Star();
		star.xpos = (float) (-10.0 + 20.0 * Math.random());
		star.ypos = (float) (-10.0 + 20.0 * Math.random());

		star.xpos *= 3072.0; /* change viewpoint */
		star.ypos *= 3072.0;

		star.zpos = i;
		star.speed = 2 + (int) (2.0 * Math.random());

		star.color = i >> 2; /* the closer to the viewer the brighter */

		return star;
	}
}
