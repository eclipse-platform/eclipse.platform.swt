package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class TableItemRenderer {

	/** Gap between icon and text */
	private static final int GAP = 3;
	/** Left and right margins */
	private static final int DEFAULT_MARGIN = 3;
	/** Left and right margins */
	private static final int DEFAULT_MARGIN_UP_DOWN = 2;
	/** a string inserted in the middle of text that has been shortened */

	private static final int leftMargin = 3;
	private static final int rightMargin = DEFAULT_MARGIN;
	private static final int topMargin = DEFAULT_MARGIN_UP_DOWN;
	private static final int bottomMargin = DEFAULT_MARGIN_UP_DOWN;

	private final TableItem item;
	private boolean selected;
	private boolean hovered;
	Rectangle checkboxBounds;

	private final Map<Integer, Point> computedCellSizes = new HashMap<>();
	private Point computedSize;

	public TableItemRenderer(TableItem tableItem) {
		this.item = tableItem;
	}

	void doPaint(GC gc) {
		Rectangle b = item.getBounds();

		Color bgBefore = gc.getBackground();

		if (getParent().selectedTableItems.contains(item)) {
			this.selected = true;

			gc.setBackground(Table.SELECTION_COLOR);
			gc.fillRectangle(b);
			gc.drawRectangle(new Rectangle(b.x, b.y, b.width - 1, b.height - 1));
			gc.drawRectangle(b);
		} else if (getParent().mouseHoverElement == item) {
			this.hovered = true;
			gc.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_YELLOW));
			gc.fillRectangle(b);
		} else {
			this.selected = false;
			this.hovered = false;
		}

		drawCheckbox(gc);

		Table parent = getParent();
		if (parent.columnsExist()) {
			for (int i = 0; i < parent.getColumnCount(); i++) {
				drawItemCell(gc, i);
			}
		} else {
			drawItem(gc);
		}

		gc.setBackground(bgBefore);
	}

	private Table getParent() {
		return item.getParent();
	}

	private void drawCheckbox(GC gc) {
		if ((getParent().getStyle() & SWT.CHECK) == 0) return;

		var itemBounds = item.getFullBounds();

		this.checkboxBounds = new Rectangle(itemBounds.x + 5, itemBounds.y + 3, 20, 20);

		gc.drawRectangle(this.checkboxBounds);
		if (item.getChecked()) {
			gc.drawLine(this.checkboxBounds.x, this.checkboxBounds.y, this.checkboxBounds.x + this.checkboxBounds.width,
					this.checkboxBounds.y + this.checkboxBounds.height);
			gc.drawLine(this.checkboxBounds.x + this.checkboxBounds.width, this.checkboxBounds.y, this.checkboxBounds.x,
					this.checkboxBounds.y + this.checkboxBounds.height);
		}
	}

	private void drawItemCell(GC gc, int columnIndex) {
		var b = getBounds(columnIndex);
		gc.setClipping(b);

		var prevBG = gc.getBackground();
		var bgCell = item.getBackground(columnIndex);

		if (bgCell != null && !this.selected && !this.hovered) {
			gc.setBackground(bgCell);
			gc.fillRectangle(b);
		}

		int currentWidthPosition = b.x + leftMargin;

		int xPosition = currentWidthPosition;
		int yPosition = b.y + topMargin;

		var image = item.getImage(columnIndex);
		if (image != null) {
			if (Table.FILL_IMAGE_AREAS) {
				var pBG = gc.getBackground();

				gc.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_RED));
				var imgB = image.getBounds();
				var rec = new Rectangle(imgB.x + xPosition, imgB.y + yPosition, imgB.width, imgB.height);
				gc.fillRectangle(rec);

				gc.setBackground(pBG);
			}

			if (Table.DRAW_IMAGES) {
				gc.drawImage(image, xPosition, yPosition);
			}
			currentWidthPosition += image.getBounds().width + GAP;
		}

		var prevFG = gc.getForeground();
		var fgCol = item.getForeground(columnIndex);
		if (fgCol != null && !this.selected && !this.hovered) {
			gc.setForeground(fgCol);
		}

		xPosition = currentWidthPosition;
		yPosition = b.y + topMargin;

		if (Table.FILL_TEXT_AREAS) {
			var pBG = gc.getBackground();
			gc.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
			var textSize = getParent().computeTextExtent(item.getText(columnIndex));
			var rec = new Rectangle(xPosition, yPosition, textSize.x, textSize.y);
			gc.fillRectangle(rec);
			gc.setBackground(pBG);
		}

		if (Table.DRAW_TEXTS) {
			gc.drawText(item.getText(columnIndex), currentWidthPosition, b.y + topMargin);
		}

		gc.setForeground(prevFG);
		gc.setBackground(prevBG);
		gc.setClipping((Rectangle) null);
	}

	private Rectangle getBounds(int columnIndex) {
		return item.getBounds(columnIndex);
	}

	private void drawItem(GC gc) {
		var b = item.getBounds();

		var prevBG = gc.getBackground();
		var bgCol = item.getBackground();
		if (bgCol != null && !this.selected && !this.hovered) {
			gc.setBackground(bgCol);
			gc.fillRectangle(b);
		}

		int currentWidthPosition = b.x + leftMargin;

		int xPosition = currentWidthPosition;
		int yPosition = b.y + topMargin;

		var image = item.getImage();
		if (image != null) {
			if (Table.FILL_IMAGE_AREAS) {
				var pBG = gc.getBackground();
				gc.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_RED));
				var imgB = item.getImage().getBounds();
				var rec = new Rectangle(imgB.x + xPosition, imgB.y + yPosition, imgB.width, imgB.height);
				gc.fillRectangle(rec);
				gc.setBackground(pBG);
			}

			if (Table.DRAW_IMAGES) {
				gc.drawImage(image, xPosition, yPosition);
			}
			currentWidthPosition += image.getBounds().width + GAP;
		}

		var prevFG = gc.getForeground();
		var fgCol = item.getForeground();
		if (fgCol != null && !this.selected && !this.hovered) {
			gc.setForeground(fgCol);
		}

		xPosition = currentWidthPosition;
		yPosition = b.y + topMargin;

		if (Table.FILL_TEXT_AREAS) {
			var pBG = gc.getBackground();
			gc.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
			var textSize = getParent().computeTextExtent(item.getText());
			var rec = new Rectangle(xPosition, yPosition, textSize.x, textSize.y);
			gc.fillRectangle(rec);
			gc.setBackground(pBG);
		}

		if (Table.DRAW_TEXTS) {
			gc.drawText(item.getText(), currentWidthPosition, b.y + topMargin);
		}

		gc.setForeground(prevFG);
		gc.setBackground(prevBG);
	}

	public Point computeCellSize(int colIndex) {
		if (computedCellSizes.get(colIndex) != null) {
			return computedCellSizes.get(colIndex);
		}

		var image = item.getImage(colIndex);

		int height = topMargin + bottomMargin;
		int width = leftMargin + rightMargin;

		if (image != null) {
			height += image.getBounds().height;
			width += image.getBounds().width;
		}

		var text = item.getText(colIndex);
		if (text != null) {
			var size = getParent().computeTextExtent(text);

			width += size.x;
			height += size.y;
		}

		if (image != null && text != null) {
			width += GAP;
		}

		var p = new Point(width, height);

		computedCellSizes.put(colIndex, p);

		return p;
	}

	/**
	 *
	 * TODO: The text extent calls cause da drastic performance decrease with skija.
	 * This should be improved: no height calculations, just use the default height
	 * from textMetrics. And check for multiple lines of text.
	 *
	 */
	Point computeSize(boolean changed) {
		if (!changed && this.computedSize != null) {
			return computedSize;
		}

		int lineHeight = 0;
		int imageHeight = 0;

		int width = leftMargin + rightMargin;

		// guess the line height for the text. Currently only support for one line

		lineHeight = guessItemHeight(getParent());

//		if (text != null && !text.isEmpty()) {
//			Point textExtent = computeTextExtent();
//			lineHeight = textExtent.y;
//			width += textExtent.x;
//		}
//
//		if (getParent().getColumnCount() > 0) {
//			for (int c = 0; c < getParent().getColumnCount(); c++) {
//				Point textExtent = computeTextExtent(c);
//				lineHeight = Math.max(lineHeight, textExtent.y);
//			}
//		}

		if (item.images != null) {
			for (var i : item.images) {
				if (i == null) {
					continue;
				}
				Rectangle imageBounds = i.getBounds();
				imageHeight = Math.max(imageBounds.height, imageHeight);
//				width += imageBounds.width;
//
//				if (text != null)
//					width += GAP;

			}
		} else if (item.image != null) {
			Rectangle imageBounds = item.image.getBounds();
			imageHeight = Math.max(imageBounds.height, imageHeight);
			width += imageBounds.width;
			if (item.text != null) {
				width += GAP;
			}
		}

		int height = topMargin + Math.max(lineHeight, imageHeight) + bottomMargin;

		if (getParent().getColumnCount() > 0) {
			width = getParent().getTotalColumnWidth();
		} else {
			Point textExtent = getParent().computeTextExtent(item.getText());
			lineHeight = textExtent.y;
			width += textExtent.x;
		}

		this.computedSize = new Point(width, height);

		return this.computedSize;
	}

	public void clearCache() {
		this.computedCellSizes.clear();
		computedSize = null;
	}

	public static int guessItemHeight(Table table) {
		int textHeight = Table.guessTextHeight(table);
		return textHeight + topMargin + bottomMargin;
	}
}
