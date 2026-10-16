package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

class TableItemsHandler {

	private final Table table;
	private Point computedSize;
	private int lastVisibleElementIndex;
	private int itemsCountAtCalculation;

	final static int ITEMS_OVERLAY = 5;

	public TableItemsHandler(Table table) {
		this.table = table;
	}

	public void calculateItemsBounds() {
		this.itemsCountAtCalculation = table.getItemCount();

		if (table.isVirtual()) {
			int gridLineSize = getGridSize(table);
			int heightPerLine = TableItemRenderer.guessItemHeight(table) + gridLineSize;

			var ca = table.getClientArea();
			this.computedSize = new Point(ca.width, itemExtent(this.itemsCountAtCalculation, heightPerLine));
			return;
		}

		var items = table.getItems();
		var columns = table.getColumnsArea();

		int gridLineSize = getGridSize(table);

		int width = 0;
		int heightPerLine = TableItemRenderer.guessItemHeight(table) + gridLineSize;

		if (table.columnsExist()) {
			width = columns.width;
		} else {
			for (int i = 0; i < items.length; i++) {
				var it = items[i];
				if (i == 0) {
					heightPerLine = getItemsHeight(it);
				}

				width = Math.max(width, it.getFullBounds().width);
			}
		}

		this.computedSize = new Point(width, itemExtent(this.itemsCountAtCalculation, heightPerLine));
	}

	/** Int-valued geometry saturates for valid extents; it is not a long-scroll model. */
	private static int itemExtent(int count, int height) {
		if (count < 0 || height < 0) return count * height;
		return (int) Math.min(Integer.MAX_VALUE, (long) count * height);
	}

	/** Same exclusive overlay boundary, without overflowing near the last logical row. */
	private static int windowLimit(int lastVisible, int count) {
		return (int) Math.min(count, (long) lastVisible + ITEMS_OVERLAY);
	}

	static int getGridSize(Table table) {
		return table.getLinesVisible() ? Table.TABLE_GRID_LINE_SIZE : 0;
	}

	static int getItemsHeight(TableItem it) {
		return it.getSize().y + getGridSize(it.getParent());
	}

	public void paint(GC gc) {
		var itemsArea = getItemsClientArea();

		if (Table.FILL_AREAS) {
			var prev = gc.getBackground();
			gc.setBackground(table.getDisplay().getSystemColor(SWT.COLOR_GREEN));
			gc.fillRectangle(itemsArea);
			gc.setBackground(prev);
		}

		var fgBef = gc.getForeground();

		Throwable failure = null;
		try {
			this.lastVisibleElementIndex = -1;

			for (int i = table.getTopIndex(); i < table.getItemCount(); i++) {
				var item = table.getItem(i);

				if (table.isVirtual()) {
					table.checkData(item, i, false);
				}

				if (table.isDisposed() || item.isDisposed() || gc.isDisposed()) return;
				item.doPaint(gc);
				if (table.isDisposed() || item.isDisposed() || gc.isDisposed()) return;

				final Rectangle bounds = item.getFullBounds();
				if ((long) bounds.y + bounds.height > (long) itemsArea.y + itemsArea.height) {
					this.lastVisibleElementIndex = i;
					break;
				}
			}

			if (this.lastVisibleElementIndex == -1) {
				this.lastVisibleElementIndex = table.getItemCount() - 1;
			}

		} catch (RuntimeException | Error primary) {
			failure = primary;
			throw primary;
		} finally {
			try {
				if (!gc.isDisposed()) gc.setForeground(fgBef);
			} catch (RuntimeException | Error cleanup) {
				if (failure == null) throw cleanup;
				if (cleanup != failure) failure.addSuppressed(cleanup);
			}
		}
	}

	public Point getSize() {
		if (computedSize == null || this.itemsCountAtCalculation != table.getItemCount()) {
			calculateItemsBounds();
		}

		return computedSize;
	}

	public Rectangle getItemsClientArea() {
		var ca = table.getClientArea();
		var columns = table.getColumnsArea();

		return new Rectangle(0, columns.y + columns.height + 1, ca.width, ca.height - columns.height);
	}

	public void handleMouseMove(Event event) {
		var ica = getItemsClientArea();
		if (ica.width == 0 || ica.height == 0 || !table.isVisible()) return;

		var mouseHoverElement = table.mouseHoverElement;
		if (!ica.contains(event.x, event.y)) {
			if (mouseHoverElement instanceof TableItem ti) {
				table.mouseHoverElement = null;
				if (!ti.isDisposed()) ti.redraw();
			}

			return;
		}

		Point p = new Point(event.x, event.y);

		if (mouseHoverElement instanceof TableItem item) {
			if (!item.isDisposed() && item.getBounds().contains(p)) {
				return;
			}

			table.mouseHoverElement = null;
			if (!item.isDisposed()) item.redraw();
		}

		// Hover must not materialize the entire logical table via getItems().
		int topIndex = table.getTopIndex();
		{
			for (int i = topIndex; i < windowLimit(this.lastVisibleElementIndex,
					table.getItemCount()); i++) {
				var item = table.getItem(i);
				if (!item.isDisposed() && item.getBounds().contains(p)) {
					table.mouseHoverElement = item;
					if (!item.isDisposed()) item.redraw();
					return;
				}

			}
		}
	}

	public int getLastVisibleElementIndex() {
		return this.lastVisibleElementIndex;
	}

	public void clearCache() {
		computedSize = null;
	}

	public void handleDoubleClick(Event event) {
		var ica = getItemsClientArea();
		if (ica.width == 0 || ica.height == 0 || !table.isVisible()) return;

		Point p = new Point(event.x, event.y);
		if (ica.contains(p)) {
			for (int i = table.getTopIndex(); i < windowLimit(this.lastVisibleElementIndex,
					table.getItemCount()); i++) {
				var it = table.getItem(i);
				if (it.getBounds().contains(p)) {
					Event e = new Event();
					e.item = it;
					e.type = SWT.DefaultSelection;
					e.count = event.count;
					e.button = event.button;
					e.doit = event.doit;
					e.stateMask = event.stateMask;
					e.time = event.time;
					e.x = event.x;
					e.y = event.y;

					table.postEvent(SWT.DefaultSelection, e);
					return;
				}
			}
		}
	}
}
