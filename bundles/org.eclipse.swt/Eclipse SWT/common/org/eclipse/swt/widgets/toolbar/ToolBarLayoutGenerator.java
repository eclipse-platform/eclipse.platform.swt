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
import org.eclipse.swt.widgets.toolbar.ToolBarLayout.*;

public class ToolBarLayoutGenerator {
	private final List<ItemRecord> itemRecords;
	private final Point availableSize;
	private final boolean horizontal;
	private final boolean wrap;
	private final boolean shadowOut;
	private final boolean rtl;
	private final boolean flat;

	public ToolBarLayoutGenerator(List<ItemRecord> itemRecords, Point availableSize, boolean horizontal, boolean wrap,
			boolean shadowOut, boolean rlt, boolean flat) {
		this.itemRecords = itemRecords;
		this.availableSize = availableSize;
		this.horizontal = horizontal;
		this.wrap = wrap;
		this.shadowOut = shadowOut;
		this.rtl = rlt;
		this.flat = flat;
	}

	public ToolBarLayout computeLayout() {
		Point offset = new Point(0, 0);

		if (shadowOut) {
			offset.y++;
		}

		ToolBarLayout layout;
		if (horizontal) {
			if (wrap) {
				layout = computeMultipleHorizontalRows(offset);
			} else {
				layout = computeSingleHorizontalRow(offset);
			}
		} else {
			layout = computeSingleVerticalRow(offset);
		}

		return layout;
	}

	private ToolBarLayout computeSingleHorizontalRow(Point offset) {
		Row row = new Row(availableSize, offset.x, true);

		for (ItemRecord itemRecord : itemRecords) {
			row.add(itemRecord);
		}

		row.normalizeHeight();
		row.setY(offset.y);

		if (rtl) {
			row.mirrow();
		}

		int width = offset.x + row.usedSpace.x;
		int height = offset.y + row.usedSpace.y;

		return new ToolBarLayout(List.of(row), new Point(width, height));
	}

	private ToolBarLayout computeSingleVerticalRow(Point offset) {
		Row row = new Row(availableSize, offset.y, false);

		for (ItemRecord itemRecord : itemRecords) {
			row.add(itemRecord);
		}

		row.normalizeWidth();
		row.setX(offset.x);

		int width = offset.x + row.usedSpace.x;
		int height = offset.y + row.usedSpace.y;

		return new ToolBarLayout(List.of(row), new Point(width, height));
	}

	private ToolBarLayout computeMultipleHorizontalRows(Point offset) {
		List<Row> rows = sortItemsIntoRows(offset);

		int yOffset = offset.y;
		Iterator<Row> iter = rows.iterator();
		while (iter.hasNext()) {
			Row row = iter.next();
			row.normalizeHeight();
			row.setY(yOffset);

			yOffset += row.usedSpace.y;

			if (requiresRowSeparator(row, !iter.hasNext())) {
				row.hasRowSeparator = true;
				yOffset += 3;
			}

			if (rtl) {
				row.mirrow();
			}
		}

		int maxWidth = offset.x;
		int maxHeight = offset.y;
		for (Row row : rows) {
			maxWidth = Math.max(maxWidth, row.usedSpace.x);
			maxHeight += row.usedSpace.y;
		}

		if (availableSize.x != SWT.DEFAULT) {
			maxWidth = availableSize.x;
		}

		if (availableSize.y != SWT.DEFAULT) {
			maxHeight = availableSize.y;
		}

		return new ToolBarLayout(rows, new Point(maxWidth, maxHeight));
	}

	private boolean requiresRowSeparator(Row row, boolean isLastRow) {
		if (isLastRow) {
			return false;
		}
		if (row.containsSeparator && flat && wrap && horizontal) {
			return true;
		}
		return false;
	}

	private List<Row> sortItemsIntoRows(Point offset) {
		List<Row> rows = new ArrayList<>();
		Row row = new Row(availableSize, offset.x, true);
		rows.add(row);

		for (ItemRecord itemRecord : itemRecords) {
			if (row.items.isEmpty() || row.hasSpaceFor(itemRecord)) {
				row.add(itemRecord);
			} else {
				row = new Row(availableSize, offset.x, true);
				rows.add(row);
				row.add(itemRecord);
			}
		}
		return rows;
	}
}
