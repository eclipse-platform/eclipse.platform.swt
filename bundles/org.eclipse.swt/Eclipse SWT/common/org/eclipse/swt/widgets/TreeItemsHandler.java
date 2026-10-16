package org.eclipse.swt.widgets;

import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

class TreeItemsHandler {

	private final Tree tree;
	private Point computedSize;
	private long lastVisibleElementIndex;
	private int itemsCountAtCalculation;

	final static int ITEMS_OVERLAY = 5;

	public TreeItemsHandler(Tree tree) {
		this.tree = tree;
	}

	public void calculateItemsBounds() {
		this.itemsCountAtCalculation = tree.getOpenedItemCount();

		if (tree.isVirtual()) {
			int gridLineSize = getGridSize(tree);
			int heightPerLine = TreeItemRenderer.guessItemHeight(tree) + gridLineSize;

			Rectangle ca = tree.getClientArea();
			this.computedSize = new Point(ca.width, itemExtent(this.itemsCountAtCalculation, heightPerLine));
			return;
		}

		var items = tree.treeItemsArrangement;
		var columns = tree.getColumnsArea();

		int gridLineSize = getGridSize(tree);

		int width = 0;
		int heightPerLine = TreeItemRenderer.guessItemHeight(tree) + gridLineSize;

		if (tree.columnsExist()) {
			width = columns.width;
		} else {
			for (int i = 0; i < items.size(); i++) {
				var it = items.get(i);
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
	private static int windowLimit(long lastVisible, int count) {
		return (int) Math.min(count, lastVisible + ITEMS_OVERLAY);
	}

	static int getGridSize(Tree tree) {
		return tree.getLinesVisible() ? Tree.Tree_GRID_LINE_SIZE : 0;
	}

	static int getItemsHeight(TreeItem it) {
		return it.getSize().y + getGridSize(it.getParent());
	}

	public void paint(GC gc) {
        if (tree.isVirtual()) { paintVirtual(gc); return; }
		Rectangle itemsArea = getItemsClientArea();

		if (Tree.FILL_AREAS) {
			var prev = gc.getBackground();
			gc.setBackground(tree.getDisplay().getSystemColor(SWT.COLOR_GREEN));
			gc.fillRectangle(itemsArea);
			gc.setBackground(prev);
		}

		Color fgBef = gc.getForeground();

		Throwable failure = null;
		try {
			this.lastVisibleElementIndex = -1;
			tree.synchronizeArrangements(false);
			List<TreeItem> list = tree.treeItemsArrangement;

			for (int i = Math.toIntExact(tree.getTopIndex()); i < list.size(); i++) {
				TreeItem item = list.get(i);

				if (tree.isVirtual()) {
					int itemIndex = item.getParentItem() != null
							? item.getParentItem().indexOf(item)
							: item.getParent().indexOf(item);

					tree.checkData(item, itemIndex, false);
				}

				if (tree.isDisposed() || item.isDisposed() || gc.isDisposed()) return;
				item.doPaint(gc);
				if (tree.isDisposed() || item.isDisposed() || gc.isDisposed()) return;

				final Rectangle bounds = item.getFullBounds();
				if ((long) bounds.y + bounds.height > (long) itemsArea.y + itemsArea.height) {
					this.lastVisibleElementIndex = i;
					break;
				}
			}

			if (this.lastVisibleElementIndex == -1) {
				this.lastVisibleElementIndex = list.size() - 1;
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
		if (computedSize == null
				|| this.itemsCountAtCalculation != tree.getOpenedItemCount()) {
			calculateItemsBounds();
		}

		return computedSize;
	}

	public Rectangle getItemsClientArea() {
        Rectangle ca = tree.getClientArea();
        Rectangle columns = tree.getColumnsArea();
        int top = columns.y + columns.height + 1;
        return new Rectangle(0, top, ca.width, Math.max(0, ca.y + ca.height - top));
    }

	public void handleMouseMove(Event event) {
        if (tree.isVirtual()) {
            Item previous = tree.mouseHoverElement;
            TreeItem hit = tree.getItemInPixels(new Point(event.x, event.y));
            if (previous == hit) return;
            tree.mouseHoverElement = hit;
            if (previous instanceof TreeItem oldItem && !oldItem.isDisposed()) oldItem.redraw();
            if (hit != null && !hit.isDisposed()) hit.redraw();
            return;
        }

		Rectangle ica = getItemsClientArea();
		if (ica.width == 0 || ica.height == 0 || !tree.isVisible()) {
			return;
		}

		Item mouseHoverElement = tree.mouseHoverElement;
		if (!ica.contains(event.x, event.y)) {
			if (mouseHoverElement instanceof TreeItem ti) {
				tree.mouseHoverElement = null;
				if (!ti.isDisposed()) ti.redraw();
			}

			return;
		}

		Point p = new Point(event.x, event.y);

		if (mouseHoverElement instanceof TreeItem item) {
			if (!item.isDisposed() && item.getFullBounds().contains(p)) {
				return;
			}

			tree.mouseHoverElement = null;
			if (!item.isDisposed()) item.redraw();
		}

		int topIndex = Math.toIntExact(tree.getTopIndex());
		for (int i = topIndex; i < windowLimit(this.lastVisibleElementIndex,
				tree.treeItemsArrangement.size()); i++) {
			TreeItem item = tree.treeItemsArrangement.get(i);
			if (item.isDisposed()) {
				continue;
			}
			if (item.getFullBounds().contains(p)) {
				tree.mouseHoverElement = item;
				if (!item.isDisposed()) item.redraw();
				return;
			}
		}
	}

	public long getLastVisibleElementIndex() {
        return tree.isVirtual() ? tree.lastVisibleVirtualIndex() : lastVisibleElementIndex;
    }

	public void clearCache() {
		computedSize = null;
	}

	public void handleDoubleClick(Event event) {
        if (tree.isVirtual()) {
            TreeItem hit = tree.getItemInPixels(new Point(event.x, event.y));
            if (hit == null || hit.isDisposed()) return;
            Event selection = new Event();
            selection.item = hit;
            selection.type = SWT.DefaultSelection;
            selection.count = event.count; selection.button = event.button;
            selection.doit = event.doit; selection.stateMask = event.stateMask;
            selection.time = event.time; selection.x = event.x; selection.y = event.y;
            tree.postEvent(SWT.DefaultSelection, selection);
            return;
        }

		var ica = getItemsClientArea();
		if (ica.width == 0 || ica.height == 0 || !tree.isVisible()) {
			return;
		}

		Point p = new Point(event.x, event.y);
		var list = tree.treeItemsArrangement;
		if (ica.contains(p)) {
			for (int i = Math.toIntExact(tree.getTopIndex()); i < windowLimit(this.lastVisibleElementIndex,
					list.size()); i++) {
				TreeItem it = list.get(i);
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

					tree.postEvent(SWT.DefaultSelection, e);

					return;
				}
			}
		}
	}

    private void paintVirtual(GC gc) {
        tree.synchronizeArrangements(false);
        List<TreeItem> expected = tree.treeItemsArrangement;
        if (!tree.isVirtualWindowCurrent(expected)) { if (!tree.isDisposed()) tree.redraw(); return; }
        Rectangle body = getItemsClientArea();
        Rectangle incomingClip = gc.getClipping();
        Color foreground = gc.getForeground();
        Color background = gc.getBackground();
        this.lastVisibleElementIndex = tree.getTopIndex() - 1L;
        try {
            gc.setClipping(incomingClip.intersection(body));
            long bottom = (long) body.y + body.height;
            for (int local = 0; local < expected.size(); local++) {
                TreeItem item = expected.get(local);
                long rank = tree.getTopIndex() + local;
                if (!tree.isVirtualRowCurrent(expected, item, rank)) { if (!tree.isDisposed()) tree.redraw(); return; }
                long y = tree.virtualItemY(item);
                if (!tree.isVirtualRowCurrent(expected, item, rank)) { if (!tree.isDisposed()) tree.redraw(); return; }
                if (y >= bottom) break;
                if (!tree.checkData(item, false) || !tree.isVirtualRowCurrent(expected, item, rank)) { if (!tree.isDisposed()) tree.redraw(); return; }
                Rectangle bounds = item.getFullBounds();
                if (!tree.isVirtualRowCurrent(expected, item, rank)) { if (!tree.isDisposed()) tree.redraw(); return; }
                long end = y + bounds.height;
                if (end > body.y && y < bottom && bounds.intersects(incomingClip)) {
                    item.doPaint(gc);
                    if (!tree.isVirtualRowCurrent(expected, item, rank)) { if (!tree.isDisposed()) tree.redraw(); return; }
                }
                this.lastVisibleElementIndex = rank;
                if (end >= bottom) break;
            }
        } finally {
            gc.setForeground(foreground);
            gc.setBackground(background);
            gc.setClipping(incomingClip);
        }
        tree.completeVirtualMeasurements(expected);
    }

}
