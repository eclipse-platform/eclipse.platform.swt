package org.eclipse.swt.widgets;

import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

class TreeItemsHandler {

	private final Tree tree;
	private Point computedSize;
	private int lastVisibleElementIndex;
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
			this.computedSize = new Point(ca.width, tree.getColumnCount() * heightPerLine);
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

		this.computedSize = new Point(width, heightPerLine * tree.getItemCount());
	}

	static int getGridSize(Tree tree) {
		return tree.getLinesVisible() ? Tree.Tree_GRID_LINE_SIZE : 0;
	}

	static int getItemsHeight(TreeItem it) {
		return it.getSize().y + getGridSize(it.getParent());
	}

	public void paint(GC gc) {
		Rectangle itemsArea = getItemsClientArea();

		if (Tree.FILL_AREAS) {
			var prev = gc.getBackground();
			gc.setBackground(tree.getDisplay().getSystemColor(SWT.COLOR_GREEN));
			gc.fillRectangle(itemsArea);
			gc.setBackground(prev);
		}

		Color fgBef = gc.getForeground();

		this.lastVisibleElementIndex = -1;
		tree.synchronizeArrangements(false);
		List<TreeItem> list = tree.treeItemsArrangement;

		for (int i = tree.getTopIndex(); i < list.size(); i++) {
			TreeItem item = list.get(i);

			if (tree.isVirtual()) {
				int itemIndex = item.getParentItem() != null
						? item.getParentItem().indexOf(item)
						: item.getParent().indexOf(item);

				tree.checkData(item, itemIndex, false);
			}

			item.doPaint(gc);

			final Rectangle bounds = item.getFullBounds();
			if (bounds.y + bounds.height > itemsArea.y + itemsArea.height) {
				this.lastVisibleElementIndex = i;
				break;
			}
		}

		if (this.lastVisibleElementIndex == -1) {
			this.lastVisibleElementIndex = list.size() - 1;
		}

		gc.setForeground(fgBef);
	}

	public Point getSize() {
		if (computedSize == null
				|| this.itemsCountAtCalculation != tree.getItemCount()) {
			calculateItemsBounds();
		}

		return computedSize;
	}

	public Rectangle getItemsClientArea() {
		Rectangle ca = tree.getClientArea();
		Rectangle columns = tree.getColumnsArea();

		return new Rectangle(0, columns.y + columns.height + 1, ca.width, ca.height - columns.height);
	}

	public void handleMouseMove(Event event) {
		Rectangle ica = getItemsClientArea();
		if (ica.width == 0 || ica.height == 0 || !tree.isVisible()) {
			return;
		}

		Item mouseHoverElement = tree.mouseHoverElement;
		if (!ica.contains(event.x, event.y)) {
			if (mouseHoverElement instanceof TreeItem ti) {
				tree.mouseHoverElement = null;
				ti.redraw();
			}

			return;
		}

		Point p = new Point(event.x, event.y);

		if (mouseHoverElement instanceof TreeItem item) {
			if (!item.isDisposed() && item.getFullBounds().contains(p)) {
				return;
			}

			tree.mouseHoverElement = null;
			item.redraw();
		}

		int topIndex = tree.getTopIndex();
		for (int i = topIndex; i < Math.min(this.lastVisibleElementIndex + ITEMS_OVERLAY,
				tree.treeItemsArrangement.size()); i++) {
			TreeItem item = tree.treeItemsArrangement.get(i);
			if (item.isDisposed()) {
				continue;
			}
			if (item.getFullBounds().contains(p)) {
				tree.mouseHoverElement = item;
				item.redraw();
				return;
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
		if (ica.width == 0 || ica.height == 0 || !tree.isVisible()) {
			return;
		}

		Point p = new Point(event.x, event.y);
		var list = tree.treeItemsArrangement;
		if (ica.contains(p)) {
			for (int i = tree.getTopIndex(); i < Math.min(this.lastVisibleElementIndex + ITEMS_OVERLAY,
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
}
