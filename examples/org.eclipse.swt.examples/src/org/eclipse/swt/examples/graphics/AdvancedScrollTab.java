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
import org.eclipse.swt.graphics.Point;

/**
 * This tab displays a vertical text scroller with top and bottom gradient fade
 * effects so lines smoothly appear from the bottom and vanish into the top.
 */
public class AdvancedScrollTab extends AnimatedGraphicsTab {

	private static final int FONT_SIZE = 14;
	private static final int STEP = 1;
	private static final String[] LINES = {
		"SWT Graphics Example",
		"Advanced Scroll Effect",
		"",
		"Lines scroll up the screen",
		"with a gradient fade on",
		"the top and bottom edges.",
		"",
		"Enjoy the show!",
	};

	private int y = -1;
	private int lastHeight;

	public AdvancedScrollTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("AdvancedScroll"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("AdvancedScrollDescription"); //$NON-NLS-1$
	}

	@Override
	public int getInitialAnimationTime() {
		return 10;
	}

	@Override
	public void next(int width, int height) {
		if (y < 0 || height != lastHeight) {
			y = height + 10;
			lastHeight = height;
		}
		y -= STEP;
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (y < 0) {
			y = height + 10;
			lastHeight = height;
		}

		gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
		gc.fillRectangle(0, 0, width, height);

		Font font = new Font(gc.getDevice(), "Lucida Sans", FONT_SIZE, SWT.NORMAL); //$NON-NLS-1$
		Font previous = gc.getFont();
		gc.setFont(font);
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);

		int currentY = y;
		for (String line : LINES) {
			Point ts = gc.stringExtent(line);
			int x = (width - ts.x) / 2;
			gc.drawString(line, x, currentY, true);
			currentY += (int) (ts.y * 1.5);
		}

		if (currentY <= 0) {
			y = height + 10;
		}

		gc.setFont(previous);
		font.dispose();

		int maxSize = (int) (height * 0.1);
		if (maxSize > 0) {
			int step = 255 / maxSize;
			gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_BLACK));
			int alpha = 255;
			for (int i = 0; i < maxSize; i++) {
				gc.setAlpha(alpha);
				gc.drawLine(0, i, width, i);
				alpha -= step;
			}
			alpha = 255;
			for (int i = 0; i < maxSize; i++) {
				gc.setAlpha(alpha);
				gc.drawLine(0, height - 1 - i, width, height - 1 - i);
				alpha -= step;
			}
			gc.setAlpha(255);
		}
	}
}
