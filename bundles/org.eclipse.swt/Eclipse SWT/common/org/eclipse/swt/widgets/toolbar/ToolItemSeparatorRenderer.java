/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
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
package org.eclipse.swt.widgets.toolbar;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.ToolItem.*;

public class ToolItemSeparatorRenderer implements ToolItemRenderer {
	public static final Color COLOR_SEPARATOR = new Color(Display.getDefault(), 160, 160, 160);

	private static final int EMPTY_WIDTH = 7;

	private final ToolBar bar;
	private int separatorWidth = -1;

	public ToolItemSeparatorRenderer(ToolBar bar) {
		this.bar = bar;
	}

	@Override
	public void setSeparatorWidth(int separatorWidth) {
		this.separatorWidth = separatorWidth;
	}

	@Override
	public void render(GC gc, Rectangle bounds) {
		if (bar.isFlat()) {
			if (bar.isHorizontal()) {
				gc.setForeground(COLOR_SEPARATOR);
				gc.drawLine(bounds.x + 2, bounds.y, bounds.x + 2, bounds.y + bounds.height);
			}else {
				gc.setForeground(COLOR_SEPARATOR);
				gc.drawLine(bounds.x, bounds.y + 2, bounds.x + bounds.width, bounds.y + 2);

			}
		}
	}

	@Override
	public Point getSize() {
		if (bar.isVertical()) {
			return new Point(-1, EMPTY_WIDTH);
		} else {
			if (separatorWidth != -1) {
				return new Point(separatorWidth, -1);
			} else {
				return new Point(EMPTY_WIDTH, -1);
			}
		}
	}
}
