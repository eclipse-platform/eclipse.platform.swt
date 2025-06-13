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

import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.ToolBarLayout.*;

/**
 * Default renderer for the ToolBar.
 */
class DefaultToolBarRenderer extends ToolBarRenderer {

	public static final String COLOR_SEPARATOR = "toolbar.separator"; //$NON-NLS-1$
	public static final String COLOR_SHADOW_OUT = "toolbar.shadowOut"; //$NON-NLS-1$
	public static final String COLOR_HOVER_BACKGROUND = "toolbar.background.hover"; //$NON-NLS-1$
	public static final String COLOR_HOVER_BORDER = "toolbar.border.hover"; //$NON-NLS-1$
	public static final String COLOR_SELECTION_BACKGROUND = "toolbar.background.selection"; //$NON-NLS-1$
	public static final String COLOR_SELECTION_BORDER = "toolbar.selection.border"; //$NON-NLS-1$

	private int rowCount = SWT.DEFAULT;

	public DefaultToolBarRenderer(ToolBar toolbar) {
		super(toolbar);
	}

	@Override
	public void paint(GC gc, int width, int height) {
		Point size = new Point(width, height);
		ToolBarLayout layout = computeLayout(size);
		rowCount = layout.rows().size();

		render(gc, size, layout.rows());
	}

	private void render(GC gc, Point size, List<Row> rows) {
		final ColorProvider colorProvider = toolBar.getDisplay().getColorProvider();
		gc.setBackground(toolBar.getBackground());
		gc.fillRectangle(0, 0, size.x, size.y);

		if (toolBar.isShadowOut()) {
			gc.setForeground(colorProvider.getColor(COLOR_SHADOW_OUT));
			gc.drawLine(0, 0, size.x, 0);
		}

		for (Row row : rows) {
			for (ItemRecord itemRecord : row.items) {
				final ToolItem item = toolBar.getItem(itemRecord.index());
				item.render(gc, itemRecord.bounds());
			}
			if (row.hasRowSeparator) {
				drawHorizontalSeparator(gc, row, colorProvider);
			}
		}
	}

	private void drawHorizontalSeparator(GC gc, Row row, ColorProvider colorProvider) {
		int pos = row.position + row.usedSpace.y + 3;
		gc.setForeground(colorProvider.getColor(COLOR_SEPARATOR));
		gc.drawLine(0, pos, row.availableSpace.y, pos);
	}

	private ToolBarLayout computeLayout(Point size) {
		// Collect all item sizes
		List<ItemRecord> itemRecords = new ArrayList<>();
		for (int i = 0; i < toolBar.getItemCount(); i++) {
			ToolItem item = toolBar.getItem(i);
			Point preferredSize = item.getSize();
			Rectangle initialBounds = new Rectangle(0, 0, preferredSize.x, preferredSize.y);
			ItemRecord itemRecord = new ItemRecord(i, initialBounds, item.isSeparator());
			itemRecords.add(itemRecord);
		}

		ToolBarLayoutGenerator generator = new ToolBarLayoutGenerator(itemRecords, size, toolBar.isHorizontal(),
				toolBar.isWrap(), toolBar.isShadowOut(), toolBar.isRightToLeft(), toolBar.isFlat());

		return generator.computeLayout();
	}


	@Override
	public Point computeSize(int widthHint, int heightHint) {
		ToolBarLayout layout = computeLayout(new Point(widthHint, heightHint));

		Point computedSize = layout.size();
		if (toolBar.isBorder()) {
			computedSize.x += 4;
			computedSize.y += 4;
		}

		Point finalSize = new Point(computedSize.x, computedSize.y);
		if (widthHint > 0) {
			finalSize.x = widthHint;
		}

		if (heightHint > 0) {
			finalSize.y = heightHint;
		}

		return finalSize;
	}


	@Override
	public int rowCount() {
		if (rowCount == SWT.DEFAULT) {
			Point size = toolBar.getSize();
			rowCount = computeLayout(size).rows().size();
		}
		return rowCount;
	}
}
