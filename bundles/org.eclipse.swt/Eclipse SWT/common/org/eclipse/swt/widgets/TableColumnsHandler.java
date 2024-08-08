package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

class TableColumnsHandler {

	private Table table;
	private Rectangle columnsArea;
	private Point computedSize = null;
	private int columnResizePossible = -1;
	private int columnResizeActive = -1;

	TableColumnsHandler(Table table) {

		this.table = table;

	}

	void calculateColumnsPositions() {

		boolean drawColumns = table.columnsExist();

		int tableColumnsHeight = 0;
		int width = 0;
		this.columnsArea = new Rectangle(0, 0, 0, 0);

		int horizontalShift = 0;
		if (table.getHorizontalBar() != null)
			horizontalShift = table.getHorizontalBar().getSelection();

		if (drawColumns) {

			for (TableColumn c : table.getColumns()) {

				width += c.getWidth();
				tableColumnsHeight = Math.max(c.getHeight(), tableColumnsHeight);

			}

			this.columnsArea = new Rectangle(-horizontalShift, 0, width, tableColumnsHeight);

		}

		this.computedSize = new Point(width, tableColumnsHeight);

		if (!table.getHeaderVisible()) {
			this.columnsArea.height = 0;
			this.computedSize.y = 0;
		} else {
			this.computedSize.y = Math.max(1, this.computedSize.y);
		}

	}

	public void paint(GC gc) {

		if (!table.getHeaderVisible())
			return;

		var ca = table.getClientArea();

		if (Table.FILL_AREAS) {
			var prev = gc.getBackground();
			gc.setBackground(table.getDisplay().getSystemColor(SWT.COLOR_CYAN));
			gc.fillRectangle(columnsArea);
			gc.setBackground(prev);
		}

		var fgBef = gc.getForeground();

		for (var c : table.getColumns()) {

			if (!c.getBounds().intersects(ca))
				continue;

			c.paint(gc);

		}

		gc.setForeground(fgBef);

	}

	public Point getSize() {

		if (this.computedSize == null || !Table.USE_CACHES)
			calculateColumnsPositions();

		return this.computedSize;

	}

	public Rectangle getColumnsBounds() {

		if (!Table.USE_CACHES || columnsArea == null)
			calculateColumnsPositions();

		return columnsArea;

	}

	public void handleMouseMove(Event event) {

		if (columnsArea == null)
			return;

		if (!columnsArea.contains(event.x, event.y)) {

			if (table.mouseHoverElement instanceof TableColumn ti) {
				table.mouseHoverElement = null;
				ti.redraw();
			}
			table.setCursor(null);
			return;
		}

		if (this.columnResizeActive != -1) {
			var c = table.getColumn(this.columnResizeActive);
			int x = c.getBounds().x;
			c.setWidth(event.x - x);
			table.redraw();
		}

		// TODO highlight columns if mouse over...

		int i = mouseIsOnColumnSide(event.x, event.y);
		if (i >= 0) {
			System.out.println("highlight Side and change cursor: " + i);
			table.setCursor(table.getDisplay().getSystemCursor(SWT.CURSOR_SIZEWE));
			this.columnResizePossible = i;
		} else {
			table.setCursor(null);
			this.columnResizePossible = -1;
		}

	}

	private int mouseIsOnColumnSide(int x, int y) {

		if (table.getColumns() != null)
			for (TableColumn c : table.getColumns()) {

				if (Math.abs(c.getBounds().x + c.getBounds().width - x) < 5) {
					return table.indexOf(c);
				}

			}

		return -1;
	}

	public void handleMouseDown(Event event) {

		if (columnsArea == null)
			return;

		if (!columnsArea.contains(event.x, event.y)) {
			return;
		}

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
