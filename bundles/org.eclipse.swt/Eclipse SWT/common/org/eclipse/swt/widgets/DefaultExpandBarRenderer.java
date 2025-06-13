/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class DefaultExpandBarRenderer extends ExpandBarRenderer {

	protected DefaultExpandBarRenderer(ExpandBar expandBar) {
		super(expandBar);
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		boolean hasFocus = expandBar.isFocusControl ();
		for (int i = 0; i < expandBar.itemCount; i++) {
			ExpandItem item = expandBar.items[i];
			drawItem(gc, item, hasFocus && item == expandBar.focusItem);
		}

	}

	void drawItem(GC gc, ExpandItem item, boolean drawFocus) {
		int headerHeight = expandBar.getBandHeight();
		Display display = expandBar.getDisplay();
		gc.setForeground(display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
		gc.setBackground(display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		int x = item.x;
		int y = item.y;
		int width = item.width;
		int height = item.height;
		gc.fillGradientRectangle(x, y, width, headerHeight, true);
		if (item.expanded) {
			gc.setForeground(display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
			gc.drawLine(x, y + headerHeight, x, y + headerHeight + height - 1);
			gc.drawLine(x, y + headerHeight + height - 1, x + width - 1, y + headerHeight + height - 1);
			gc.drawLine(x + width - 1, y + headerHeight + height - 1, x + width - 1, y + headerHeight);
		}
		int drawX = x;
		Image image = item.image;
		int imageHeight = item.imageHeight;
		int imageWidth = item.imageWidth;
		if (image != null) {
			drawX += ExpandItem.TEXT_INSET;
			if (imageHeight > headerHeight) {
				gc.drawImage(image, drawX, y + headerHeight - imageHeight);
			} else {
				gc.drawImage(image, drawX, y + (headerHeight - imageHeight) / 2);
			}
			drawX += imageWidth;
		}
		String text = item.text;
		if (text.length() > 0) {
			drawX += ExpandItem.TEXT_INSET;
			Point size = gc.stringExtent(text);
			gc.setForeground(expandBar.getForeground());
			gc.drawString(text, drawX, y + (headerHeight - size.y) / 2, true);
		}
		int chevronSize = ExpandItem.CHEVRON_SIZE;
		drawChevron(gc, x + width - chevronSize, y + (headerHeight - chevronSize) / 2, item.expanded);
		if (drawFocus) {
			gc.drawFocus(x + 1, y + 1, width - 2, headerHeight - 2);
		}
	}

	void drawChevron(GC gc, int x, int y, boolean expanded) {
		int[] polyline1, polyline2;
		if (expanded) {
			int px = x + 4 + 5;
			int py = y + 4 + 7;
			polyline1 = new int[] { px, py, px + 1, py, px + 1, py - 1, px + 2, py - 1, px + 2, py - 2, px + 3, py - 2,
					px + 3, py - 3, px + 3, py - 2, px + 4, py - 2, px + 4, py - 1, px + 5, py - 1, px + 5, py, px + 6,
					py };
			py += 4;
			polyline2 = new int[] { px, py, px + 1, py, px + 1, py - 1, px + 2, py - 1, px + 2, py - 2, px + 3, py - 2,
					px + 3, py - 3, px + 3, py - 2, px + 4, py - 2, px + 4, py - 1, px + 5, py - 1, px + 5, py, px + 6,
					py };
		} else {
			int px = x + 4 + 5;
			int py = y + 4 + 4;
			polyline1 = new int[] { px, py, px + 1, py, px + 1, py + 1, px + 2, py + 1, px + 2, py + 2, px + 3, py + 2,
					px + 3, py + 3, px + 3, py + 2, px + 4, py + 2, px + 4, py + 1, px + 5, py + 1, px + 5, py, px + 6,
					py };
			py += 4;
			polyline2 = new int[] { px, py, px + 1, py, px + 1, py + 1, px + 2, py + 1, px + 2, py + 2, px + 3, py + 2,
					px + 3, py + 3, px + 3, py + 2, px + 4, py + 2, px + 4, py + 1, px + 5, py + 1, px + 5, py, px + 6,
					py };
		}
		gc.setForeground(expandBar.display.getSystemColor(SWT.COLOR_TITLE_FOREGROUND));
		gc.drawPolyline(polyline1);
		gc.drawPolyline(polyline2);
	}

}
