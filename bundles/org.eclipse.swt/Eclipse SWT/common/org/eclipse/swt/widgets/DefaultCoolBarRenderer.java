/*******************************************************************************
 * Copyright (c) 2025 Advantest Europe GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * 				Raghunandana Murthappa (Advantest)
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class DefaultCoolBarRenderer extends CoolBarRenderer {

	protected static final String COLOR_SHADOW_NORMAL = "coolBar.shadow.normal"; //$NON-NLS-1$
	protected static final String COLOR_SHADOW_HIGHLIGHT = "coolBar.shadow.highlight"; //$NON-NLS-1$

	protected DefaultCoolBarRenderer(CoolBar coolBar) {
		super(coolBar);
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		CoolItem[][] items = coolbar.items;
		if (items.length == 0) {
			return;
		}

		final Color normalShadow = getColor(COLOR_SHADOW_NORMAL);
		final Color highlightShadow = getColor(COLOR_SHADOW_HIGHLIGHT);

		int style = coolbar.getStyle();
		boolean vertical = (style & SWT.VERTICAL) != 0;
		boolean flat = (style & SWT.FLAT) != 0;
		int stopX = width;
		Rectangle rect;
		Rectangle clipping = new Rectangle(0, 0, width, height);
		for (int row = 0; row < items.length; row++) {
			Rectangle bounds = new Rectangle(0, 0, 0, 0);
			for (int i = 0; i < items[row].length; i++) {
				bounds = items[row][i].internalGetBounds();
				rect = coolbar.fixRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
				if (!clipping.intersects(rect)) {
					continue;
				}
				boolean nativeGripper = false;

				/* Draw gripper. */
				if (!coolbar.isLocked) {
					rect = coolbar.fixRectangle(bounds.x, bounds.y, CoolItem.MINIMUM_WIDTH, bounds.height);
					if (flat) {
						int grabberTrim = 2;
						int grabberHeight = bounds.height - (2 * grabberTrim) - 1;
						gc.setForeground(normalShadow);
						rect = coolbar.fixRectangle(bounds.x + CoolItem.MARGIN_WIDTH, bounds.y + grabberTrim, 2,
								grabberHeight);
						gc.drawRectangle(rect);
						gc.setForeground(highlightShadow);
						rect = coolbar.fixRectangle(bounds.x + CoolItem.MARGIN_WIDTH, bounds.y + grabberTrim + 1,
								bounds.x + CoolItem.MARGIN_WIDTH, bounds.y + grabberTrim + grabberHeight - 1);
						gc.drawLine(rect.x, rect.y, rect.width, rect.height);
						rect = coolbar.fixRectangle(bounds.x + CoolItem.MARGIN_WIDTH, bounds.y + grabberTrim,
								bounds.x + CoolItem.MARGIN_WIDTH + 1, bounds.y + grabberTrim);
						gc.drawLine(rect.x, rect.y, rect.width, rect.height);
					} else {
						nativeGripper = drawGripper(gc, rect.x, rect.y, rect.width, rect.height, vertical);
					}
				}

				/* Draw separator. */
				if (!flat && !nativeGripper && i != 0) {
					gc.setForeground(normalShadow);
					rect = coolbar.fixRectangle(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height - 1);
					gc.drawLine(rect.x, rect.y, rect.width, rect.height);
					gc.setForeground(highlightShadow);
					rect = coolbar.fixRectangle(bounds.x + 1, bounds.y, bounds.x + 1, bounds.y + bounds.height - 1);
					gc.drawLine(rect.x, rect.y, rect.width, rect.height);
				}
			}
			if (!flat && row + 1 < items.length) {
				/* Draw row separator. */
				int separatorY = bounds.y + bounds.height;
				gc.setForeground(normalShadow);
				rect = coolbar.fixRectangle(0, separatorY, stopX, separatorY);
				gc.drawLine(rect.x, rect.y, rect.width, rect.height);
				gc.setForeground(highlightShadow);
				rect = coolbar.fixRectangle(0, separatorY + 1, stopX, separatorY + 1);
				gc.drawLine(rect.x, rect.y, rect.width, rect.height);
			}
		}
	}

	boolean drawGripper(GC gc, int x, int y, int width, int height, boolean vertical) {
		final Color normalShadow = getColor(COLOR_SHADOW_NORMAL);
		final Color highlightShadow = getColor(COLOR_SHADOW_HIGHLIGHT);
		int dotSpacing = 3;
		int dotLength = 1;
		if (vertical) {
			int centerY = y + height / 2;
			for (int i = x + 2; i < x + width - 2; i += dotSpacing) {
				gc.setForeground(normalShadow);
				gc.drawLine(i, centerY, i + dotLength, centerY);
				gc.setForeground(highlightShadow);
				gc.drawLine(i, centerY + 1, i + dotLength, centerY + 1);
			}
		} else {
			int centerX = x + width / 2;
			for (int i = y + 2; i < y + height - 2; i += dotSpacing) {
				gc.setForeground(normalShadow);
				gc.drawLine(centerX, i, centerX, i + dotLength);
				gc.setForeground(highlightShadow);
				gc.drawLine(centerX + 1, i, centerX + 1, i + dotLength);
			}
		}
		return true;
	}
}
