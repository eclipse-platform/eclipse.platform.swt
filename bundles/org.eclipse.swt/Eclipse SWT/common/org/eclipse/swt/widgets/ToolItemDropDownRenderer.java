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
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.ToolItem.*;

public class ToolItemDropDownRenderer implements ToolItemRenderer {
	private static final int ARROW_PADDING = 4;
	private static final int ARROW_WIDTH = 7;
	private static final int ARROW_HEIGHT = 4;

	private static final int ARROW_SECTION_WIDTH = ARROW_PADDING * 2 + ARROW_WIDTH;

	private final ToolBar bar;
	private final ToolItem item;
	private final ToolItemButtonRenderer buttonRenderer;

	private Rectangle button = new Rectangle(0, 0, 0, 0);
	private Rectangle arrow = new Rectangle(0, 0, 0, 0);

	public ToolItemDropDownRenderer(ToolBar bar, ToolItem item) {
		this.bar = bar;
		this.item = item;
		this.buttonRenderer = new ToolItemButtonRenderer(bar, item);
	}

	@Override
	public void render(GC gc, Rectangle bounds) {
		if (bar.isLeftToRight()) {
			button = new Rectangle(bounds.x, bounds.y, bounds.width - ARROW_SECTION_WIDTH, bounds.height);
			arrow = new Rectangle(bounds.x + button.width, bounds.y, ARROW_SECTION_WIDTH, bounds.height);
		} else {
			arrow = new Rectangle(bounds.x, bounds.y, ARROW_SECTION_WIDTH, bounds.height);
			button = new Rectangle(bounds.x + arrow.width, bounds.y, bounds.width - ARROW_SECTION_WIDTH, bounds.height);
		}

		buttonRenderer.renderHighlight(gc, arrow);

		buttonRenderer.render(gc, button);

		drawArrow(gc, arrow);
	}

	private Point drawArrow(GC gc, Rectangle bounds) {
		int arrowX = bounds.x + ARROW_PADDING;
		int arrowY = bounds.y + (bounds.height - ARROW_HEIGHT) / 2;
		Point topLeft = new Point(arrowX, arrowY);
		Point topRight = new Point(topLeft.x + ARROW_WIDTH, topLeft.y);
		Point bottom = new Point(topLeft.x + (ARROW_WIDTH / 2), topLeft.y + ARROW_HEIGHT);

		gc.setBackground(bar.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		gc.fillPolygon(new int[] { topLeft.x, topLeft.y, topRight.x, topRight.y, bottom.x, bottom.y });

		return new Point(ARROW_SECTION_WIDTH, bounds.height);
	}

	@Override
	public Point getSize() {
		Point buttonSize = buttonRenderer.getSize();
		return new Point(buttonSize.x + getArrowWidth(), buttonSize.y);
	}

	private int getArrowWidth() {
		return ARROW_PADDING * 2 + ARROW_WIDTH;
	}

	@Override
	public boolean isOnButton(Point location) {
		return button.contains(location);
	}

	@Override
	public Point getOnArrowLocation(Point location) {
		if (!arrow.contains(location)) {
			return null;
		}
		return new Point(button.x, button.y + button.height);
	}
}
