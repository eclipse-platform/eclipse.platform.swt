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

import org.eclipse.swt.graphics.*;

public class ToolBarLayout {

	/**
	 * A record of an item and layouting relevant data.
	 */
	public static record ItemRecord(int index, Rectangle bounds, boolean isSeprator) {
	}

	/**
	 * Layout class for placeing items into a row. NOTE: While this class is called
	 * {@link Row}, it is also used for column rendering. Some of the field names
	 * make sense for a row but not for a column.
	 */
	public static class Row {
		private static final int NONE = -1;

		final List<ItemRecord> items = new ArrayList<>();
		/** The amount of pixels the row has space for items. */
		final Point availableSpace;

		final boolean horizontal;

		/** The start position of the first item. */
		Point usedSpace;

		/** The position of the row. */
		int position;

		/** Indicates if this row contains a separator. */
		boolean containsSeparator;

		/** Indicates if this row has a row separator. */
		boolean hasRowSeparator;

		public Row(Point availableSpace, int offset, boolean horizontal) {
			this.availableSpace = availableSpace;
			this.usedSpace = horizontal ? new Point(offset, 0) : new Point(0, offset);
			this.horizontal = horizontal;
		}

		/**
		 * Checks if the row has enough space for the given item.
		 */
		public boolean hasSpaceFor(ItemRecord itemRecord) {
			if (horizontal) {
				if (availableSpace.x == NONE) {
					return true;
				}
				int freeSpace = availableSpace.x - usedSpace.x;
				return freeSpace >= itemRecord.bounds.width;
			} else {
				return true;
			}
		}

		/**
		 * Adds the given item to the row layout.
		 *
		 * @param itemRecord
		 */
		public void add(ItemRecord itemRecord) {
			items.add(itemRecord);
			containsSeparator |= itemRecord.isSeprator;
			if (horizontal) {
				itemRecord.bounds.x = usedSpace.x;
				usedSpace.x += itemRecord.bounds().width + 1;
				usedSpace.y = Math.max(usedSpace.y, itemRecord.bounds.height);
			} else {
				itemRecord.bounds.y = usedSpace.y;
				usedSpace.y += itemRecord.bounds().height;
				usedSpace.x = Math.max(usedSpace.x, itemRecord.bounds.width);
			}
		}

		/**
		 * Increases the height of all items to the height of the item with the biggest
		 * height.
		 */
		void normalizeHeight() {
			int maxPossibleSize = Math.min(usedSpace.y, availableSpace.y);
			for (ItemRecord itemRecord : items) {
				itemRecord.bounds.height = maxPossibleSize;
			}
		}

		/**
		 * Increases the width of all items to the height of the item with the biggest
		 * width.
		 */
		void normalizeWidth() {
			int maxPossibleSize = Math.min(usedSpace.x, availableSpace.x);
			for (ItemRecord itemRecord : items) {
				itemRecord.bounds.width = maxPossibleSize;
			}
		}

		/**
		 * Sets the y position of all items to the given value.
		 */
		void setY(int y) {
			position = y;
			for (ItemRecord itemRecord : items) {
				itemRecord.bounds.y = y;
			}
		}

		/**
		 * Sets the y position of all items to the given value.
		 */
		void setX(int x) {
			for (ItemRecord itemRecord : items) {
				itemRecord.bounds.x = x;
			}
		}

		/**
		 * Mirrors the layout vertically.
		 */
		void mirrow() {
			for (ItemRecord itemRecord : items) {
				itemRecord.bounds.x = availableSpace.x - itemRecord.bounds.x - itemRecord.bounds.width;
			}
		}
	}

	private final List<Row> rows;
	private final Point size;

	public ToolBarLayout(List<Row> rows, Point size) {
		this.rows = rows;
		this.size = size;
	}

	public List<Row> rows() {
		return rows;
	}

	public Point size() {
		return size;
	}

}
