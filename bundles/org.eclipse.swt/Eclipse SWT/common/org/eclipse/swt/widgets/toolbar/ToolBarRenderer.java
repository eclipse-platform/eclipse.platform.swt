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

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.ToolBar.*;
import org.eclipse.swt.widgets.toolbar.ToolBarLayout.*;

import java.util.List;

/**
 * Default renderer for the ToolBar.
 */
public class ToolBarRenderer implements IToolBarRenderer {
	public static final Color COLOR_SEPARATOR = new Color(Display.getDefault(), 160, 160, 160);

	private final ToolBar bar;
	private int rowCount = SWT.DEFAULT;

	public ToolBarRenderer(ToolBar toolbar) {
		this.bar = toolbar;
	}

	@Override
	public void render(GC gc, Rectangle bounds) {
		Point size = new Point(bounds.width, bounds.height);
		ToolBarLayout layout = computeLayout(size);
		rowCount = layout.rows().size();

		render(gc, size, layout.rows());
	}

	private void render(GC gc, Point size, List<Row> rows) {
		gc.fillRectangle(0, 0, size.x, size.y);

		if (bar.isShadowOut()) {
			gc.setForeground(new Color(160, 160, 160));
			gc.drawLine(0, 0, size.x, 0);
		}

		for (int i = 0; i < rows.size(); i++) {
			Row row = rows.get(i);
			for (ItemRecord itemRecord : row.items) {
				bar.getItem(itemRecord.index()).render(gc, itemRecord.bounds());
			}
			if (row.hasRowSeparator) {
				drawHorizontalSeparator(gc, row);
			}
		}
	}

	private void drawHorizontalSeparator(GC gc, Row row) {
		int pos = row.position + row.usedSpace.y + 3;
		gc.setForeground(COLOR_SEPARATOR);
		gc.drawLine(0, pos, row.availableSpace.y, pos);
	}

	private ToolBarLayout computeLayout(Point size) {
		// Collect all item sizes
		List<ItemRecord> itemRecords = new ArrayList<>();
		for (int i = 0; i < bar.getItemCount(); i++) {
			ToolItem item = bar.getItem(i);
			Point preferedSize = item.getSize();
			Rectangle initialBounds = new Rectangle(0, 0, preferedSize.x, preferedSize.y);
			ItemRecord itemRecord = new ItemRecord(i, initialBounds, item.isSeparator());
			itemRecords.add(itemRecord);
		}

		ToolBarLayoutGenerator generator = new ToolBarLayoutGenerator(itemRecords, size, bar.isHorizontal(),
				bar.isWrap(), bar.isShadowOut(), bar.isRightToLeft(), bar.isFlat());

		return generator.computeLayout();
	}


	@Override
	public Point computeSize(Point size) {
		ToolBarLayout layout = computeLayout(size);

		Point computedSize = layout.size();
		if (bar.isBorder()) {
			computedSize.x += 4;
			computedSize.y += 4;
		}

		Point finalSize = new Point(computedSize.x, computedSize.y);
		if (size.x > 0) {
			finalSize.x = size.x;
		}

		if (size.y > 0) {
			finalSize.y = size.y;
		}

		return finalSize;
	}


	@Override
	public int rowCount() {
		if (rowCount == SWT.DEFAULT) {
			Rectangle bounds = bar.getBounds();
			Point size = new Point(bounds.x, bounds.y);
			rowCount = computeLayout(size).rows().size();
		}
		return rowCount;
	}
}
