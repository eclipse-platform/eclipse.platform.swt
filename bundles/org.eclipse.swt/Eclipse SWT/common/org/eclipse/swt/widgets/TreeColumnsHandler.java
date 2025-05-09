package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

class TreeColumnsHandler {

	private Tree tree;
	private Rectangle columnsArea;
	private Point computedSize = null;
	private int columnResizePossible = -1;
	private int columnResizeActive = -1;

	TreeColumnsHandler(Tree tree) {
		this.tree = tree;
	}

	void calculateColumnsPositions() {
		boolean drawColumns = tree.columnsExist();

		int TreeColumnsHeight = 0;
		int width = 0;
		this.columnsArea = new Rectangle(0, 0, 0, 0);

		int horizontalShift = 0;
		if (tree.getHorizontalBar() != null) {
			horizontalShift = tree.getHorizontalBar().getSelection();
		}

		if (drawColumns) {
			for (TreeColumn c : tree.getColumns()) {
				width += c.getWidth();
				TreeColumnsHeight = Math.max(c.getHeight(), TreeColumnsHeight);
			}

			this.columnsArea = new Rectangle(-horizontalShift, 0, width, TreeColumnsHeight);
		}

		this.computedSize = new Point(width, TreeColumnsHeight);

		if (tree.getHeaderVisible()) {
			this.computedSize.y = Math.max(1, this.computedSize.y);
		} else {
			this.columnsArea.height = 0;
			this.computedSize.y = 0;
		}
	}

	public void paint(GC gc) {
		if (!tree.getHeaderVisible())
			return;

		var ca = tree.getClientArea();

		if (tree.FILL_AREAS) {
			var prev = gc.getBackground();
			gc.setBackground(tree.getDisplay().getSystemColor(SWT.COLOR_CYAN));
			gc.fillRectangle(columnsArea);
			gc.setBackground(prev);
		}

		var fgBef = gc.getForeground();

		for (var c : tree.getColumns()) {
			if (!c.getBounds().intersects(ca)) {
				continue;
			}

			c.paint(gc);
		}

		gc.setForeground(fgBef);
	}

	public Point getSize() {
		if (this.computedSize == null || !Tree.USE_CACHES) {
			calculateColumnsPositions();
		}

		return this.computedSize;
	}

	public Rectangle getColumnsBounds() {
		if (!Tree.USE_CACHES || columnsArea == null) {
			calculateColumnsPositions();
		}

		return columnsArea;
	}

	public void handleMouseMove(Event event) {
		if (columnsArea == null)
			return;

		if (!columnsArea.contains(event.x, event.y)) {
			if (tree.mouseHoverElement instanceof TreeColumn ti) {
				tree.mouseHoverElement = null;
				ti.redraw();
			}
			tree.setCursor(null);
			return;
		}

		if (this.columnResizeActive != -1) {
			var c = tree.getColumn(this.columnResizeActive);
			int x = c.getBounds().x;
			c.setWidth(event.x - x);
			tree.redraw();
		}

		// TODO highlight columns if mouse over...

		int i = mouseIsOnColumnSide(event.x, event.y);
		if (i >= 0) {
			tree.setCursor(tree.getDisplay().getSystemCursor(SWT.CURSOR_SIZEWE));
			this.columnResizePossible = i;
		} else {
			tree.setCursor(null);
			this.columnResizePossible = -1;
		}

	}

	private int mouseIsOnColumnSide(int x, int y) {
		final TreeColumn[] columns = tree.getColumns();
		if (columns != null) {
			for (TreeColumn c : columns) {
				if (Math.abs(c.getBounds().x + c.getBounds().width - x) < 5) {
					return tree.indexOf(c);
				}
			}
		}

		return -1;
	}

	public void handleMouseDown(Event event) {
		if (columnsArea == null)
			return;
		if (!columnsArea.contains(event.x, event.y))
			return;

		if (this.columnResizePossible != -1 && event.button == 1) {
			this.columnResizeActive = this.columnResizePossible;
		}
	}

	public void handleMouseUp(Event e) {
		this.columnResizeActive = -1;
	}

	public void clearCache() {
		columnsArea = null;
		computedSize = null;
	}
}
