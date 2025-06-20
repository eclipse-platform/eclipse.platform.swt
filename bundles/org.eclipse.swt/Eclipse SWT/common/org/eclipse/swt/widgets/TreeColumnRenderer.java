package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class TreeColumnRenderer {

	/** Left and right margins */
	private static final int DEFAULT_MARGIN = 6;

	/** up margin */
	private static final int DEFAULT_MARGIN_UP = 3;

	/** down margin */
	private static final int DEFAULT_MARGIN_DOWN = 1;

	/** border width */
	private static final int DEFAULT_BORDER_WIDTH = 1;

	private final TreeColumn column;

	public TreeColumnRenderer(TreeColumn tableColumn) {
		this.column = tableColumn;
	}

	void doPaint(GC gc) {
		var b = column.getBounds();
		gc.drawRectangle(b);

		int xPosition = b.x + DEFAULT_MARGIN + DEFAULT_BORDER_WIDTH;
		int yPosition = b.y + DEFAULT_MARGIN_UP + DEFAULT_BORDER_WIDTH;

		if (Tree.FILL_TEXT_AREAS) {
			var pBG = gc.getBackground();
			gc.setBackground(column.getParent().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
			var textSize = column.getParent().computeTextExtent(column.getText());
			var rec = new Rectangle(xPosition, yPosition, textSize.x, textSize.y);
			gc.fillRectangle(rec);
			gc.setBackground(pBG);
		}

		if (Tree.DRAW_TEXTS) {
			gc.drawText(column.getText(), xPosition, yPosition);
		}
	}

	static int guessColumnHeight(TreeColumn column) {
		var textHeight = Tree.guessTextHeight(column.getParent());
		return textHeight + DEFAULT_MARGIN_UP + DEFAULT_MARGIN_DOWN;
	}

	Point computeSize() {
		return Drawing.measure(getParent(), gc -> {
			int colIndex = getParent().indexOf(column);
			Point fin = new Point(0, 0);
			int width = 0;
			final TreeItem[] items = getParent().getItems();
			if (items != null) {
				final boolean virtual = getParent().isVirtual();
				for (TreeItem item : items) {
					if (virtual && !item.cached) {
						continue;
					}
					Point p = item.computeCellSize(colIndex);
					width = Math.max(width, p.x);
					item.clearCache();
				}
			}

			Point headerExt = gc.textExtent(column.getText());
			fin.x = Math.max(headerExt.x + 2 * DEFAULT_MARGIN + 2 * DEFAULT_BORDER_WIDTH, width);
			fin.y = Math.max(headerExt.y + DEFAULT_MARGIN_UP + DEFAULT_MARGIN_DOWN + 2 * DEFAULT_BORDER_WIDTH, 10);
			return fin;
		});
	}

	private Tree getParent() {
		return column.getParent();
	}
}
