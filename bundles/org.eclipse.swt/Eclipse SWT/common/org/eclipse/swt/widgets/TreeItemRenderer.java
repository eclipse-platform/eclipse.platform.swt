package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class TreeItemRenderer {

	static final String COLOR_SELECTION = "button.background.selection"; //$NON-NLS-1$
	static final String COLOR_BOX = "button.box"; //$NON-NLS-1$
	static final String COLOR_BOX_DISABLED = "button.box.disabled"; //$NON-NLS-1$
	static final String COLOR_GRAYED = "button.gray"; //$NON-NLS-1$
	protected static final String COLOR_DISABLED = "disabled"; //$NON-NLS-1$

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
	public static final int INDENT_WIDTH = 15;
	private static final int ARROW_SIDE_LENGTH = 20;
	private static final int BOX_SIZE = 12;

	private final TreeItem item;
	private boolean selected;
	private boolean hovered;

	private final Map<Integer, Point> computedCellSizes = new HashMap<>();
	private final Map<Integer, Rectangle> internalComputedCellTextBounds = new HashMap<>();
	private final Map<Integer, Rectangle> internalComputedCellImage = new HashMap<>();

	private Point computedSize;

	public TreeItemRenderer(TreeItem tableItem) {
		this.item = tableItem;
	}

	void doPaint(GC gc) {
		Rectangle b = item.getFullBounds();

		Color bgBefore = gc.getBackground();
		Tree parent = getParent();
		final boolean paintItemEvent = parent.hooks(SWT.PaintItem);

		if (parent.selectedTreeItems.contains(item)) {
			this.selected = true;

			gc.setBackground(Table.SELECTION_COLOR);
			gc.fillRectangle(b);
			if (parent.isFocusControl()) {
				gc.drawFocus(b.x, b.y, b.width - 1, b.height - 1);
			}
		} else if (getParent().mouseHoverElement == item) {
			this.hovered = true;
			gc.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_YELLOW));
			gc.fillRectangle(b);
		} else {
			this.selected = false;
			this.hovered = false;
		}
		drawArrowArea(gc);
		drawCheckbox(gc);

		if (parent.columnsExist()) {
			for (int i = 0; i < parent.getColumnCount(); i++) {
				drawItemCell(gc, i, paintItemEvent);
			}
		} else {
			drawItem(gc, paintItemEvent);
		}

		gc.setBackground(bgBefore);
	}

	Point getArrowRectangleSize() {
		return new Point(ARROW_SIDE_LENGTH, ARROW_SIDE_LENGTH);
	}

	Point getCheckBoxSize() {
		if ((getParent().getStyle() & SWT.CHECK) == 0) {
			return null;
		}

		return new Point(BOX_SIZE, BOX_SIZE);
	}

	Rectangle getArrowRectangle() {
		var itemLocation = item.getLocation();
		var size = getArrowRectangleSize();

		int indent = item.getIndent() * TreeItemRenderer.INDENT_WIDTH;

		return new Rectangle(itemLocation.x + leftMargin + indent, itemLocation.y + topMargin, size.x, size.y);
	}

	Rectangle getCheckboxRectangle() {
		if ((getParent().getStyle() & SWT.CHECK) == 0) {
			return null;
		}

		var itemLocation = item.getLocation();
		var arrowBounds = getArrowRectangle();
		var size = getCheckBoxSize();

		return new Rectangle(arrowBounds.x + arrowBounds.width + GAP, itemLocation.y + topMargin, size.x, size.y);
	}

	private void drawArrowArea(GC gc) {
		Rectangle arrowBounds = getArrowRectangle();

		Color prev = gc.getBackground();
		try {
			if (item.getItemCount() == 0) {
				return;
			}

			drawArrow(gc, arrowBounds, item.getExpanded() ? SWT.DOWN : SWT.RIGHT);
		} finally {
			gc.setBackground(prev);
		}
	}

	private void drawArrow(GC gc, Rectangle arrowBounds2, int style) {
		gc.setBackground(item.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));

		int width = arrowBounds2.width;
		int height = arrowBounds2.height;

		int centerHeight = height / 2;
		int centerWidth = width / 2;

		// TODO: in the next version use a bezier path

		int[] curve;
		if ((style & SWT.DOWN) != 0) {
			curve = new int[] { centerWidth, centerHeight + 5, centerWidth - 5, centerHeight - 5, centerWidth + 5,
					centerHeight - 5 };

		} else if ((style & SWT.LEFT) != 0) {
			curve = new int[] { centerWidth - 5, centerHeight, centerWidth + 5, centerHeight + 5, centerWidth + 5,
					centerHeight - 5 };

		} else if ((style & SWT.RIGHT) != 0) {
			curve = new int[] { centerWidth + 5, centerHeight, centerWidth - 5, centerHeight - 5, centerWidth - 5,
					centerHeight + 5 };

		} else {
			curve = new int[] { centerWidth, centerHeight - 5, centerWidth - 5, centerHeight + 5, centerWidth + 5,
					centerHeight + 5 };
		}

		curve = translateCurve(arrowBounds2.x, arrowBounds2.y, curve);

		gc.fillPolygon(curve);
	}

	private int[] translateCurve(int x, int y, int[] curve) {
		int[] newCurve = new int[curve.length];

		for (int i = 0; i < curve.length / 2; i++) {
			int px = curve[2 * i];
			int py = curve[2 * i + 1];

			newCurve[2 * i] = px + x;
			newCurve[2 * i + 1] = py + y;
		}

		return newCurve;
	}

	private Tree getParent() {
		return item.getParent();
	}

	private void drawCheckbox(GC gc) {
		if ((getParent().getStyle() & SWT.CHECK) == 0) {
			return;
		}

		var prevF = gc.getForeground();
		var prevB = gc.getBackground();

		var checkboxBounds = getCheckboxRectangle();
		drawCheckbox(gc, checkboxBounds.x, checkboxBounds.y);

		gc.setForeground(prevF);
		gc.setBackground(prevB);
	}

	private void drawCheckbox(GC gc, int x, int y) {
		final boolean enabled = item.getParent().isEnabled();
		final boolean selection = item.getChecked();
		if (selection) {
			gc.setBackground(
					getColor(enabled ? item.getGrayed() ? COLOR_GRAYED : COLOR_SELECTION : COLOR_DISABLED));
			int partialBoxBorder = 2;
			gc.fillRoundRectangle(x + partialBoxBorder, y + partialBoxBorder, BOX_SIZE - 2 * partialBoxBorder + 1,
					BOX_SIZE - 2 * partialBoxBorder + 1, BOX_SIZE / 4 - partialBoxBorder / 2,
					BOX_SIZE / 4 - partialBoxBorder / 2);
		}

		gc.setForeground(getColor(enabled ? COLOR_BOX : COLOR_BOX_DISABLED));
		gc.drawRoundRectangle(x, y, BOX_SIZE, BOX_SIZE, 4, 4);
	}

	private Color getColor(String colorString) {
		return item.getParent().getRenderer().getColor(colorString);
	}

	private void drawItemCell(GC gc, int columnIndex, boolean paintItemEvent) {
		if (paintItemEvent) {
			var parent = item.getParent();
			var b = item.getTextBounds(columnIndex);
			Event event = new Event();
			event.item = item;
			event.index = columnIndex;
			event.gc = gc;
			event.x = b.x;
			event.y = b.y;
			// TODO MeasureItem should happen in the bounds calculation
			// logic...
			parent.sendEvent(SWT.MeasureItem, event);
			parent.sendEvent(SWT.EraseItem, event);

			if (this.selected) {
				gc.setBackground(Table.SELECTION_COLOR);
				gc.fillRectangle(b);
			} else if (this.hovered) {
				gc.setBackground(getParent().getDisplay().getSystemColor(SWT.COLOR_YELLOW));
				gc.fillRectangle(b);
			}

			parent.sendEvent(SWT.PaintItem, event);
		}

		Color prevBG = gc.getBackground();
		Color bgCell = item.getBackground(columnIndex);

		Color prevFG = gc.getForeground();
		Color fgCol = item.getForeground(columnIndex);
		if (fgCol != null && !this.selected && !this.hovered) {
			gc.setForeground(fgCol);
		}

		if (this.hovered)
			gc.setBackground(item.getParent().getDisplay().getSystemColor(SWT.COLOR_YELLOW));
		if (this.selected) {
			gc.setBackground(Table.SELECTION_COLOR);
		} else {
			gc.setBackground(bgCell);
		}

		Image image = item.getImage(columnIndex);
		if (image != null) {
			var rec = getImageBounds(columnIndex);
			drawImage(image, rec, gc);
		}
		Rectangle textRec = getTextBounds(columnIndex);

		drawText(item.getText(columnIndex), textRec, gc);

		gc.setForeground(prevFG);
		gc.setBackground(prevBG);
	}

	private void drawText(String text, Rectangle textRec, GC gc) {
		if (Tree.FILL_TEXT_AREAS) {
			Color pBG = gc.getBackground();
			gc.setBackground(item.getParent().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));

			gc.fillRectangle(textRec);
			gc.setBackground(pBG);
		}

		if (Tree.DRAW_TEXTS) {
			gc.drawText(text, textRec.x, textRec.y);
		}
	}

	private void drawImage(Image image, Rectangle rec, GC gc) {
		if (image != null) {
			if (Tree.FILL_IMAGE_AREAS) {
				var pBG = gc.getBackground();

				gc.setBackground(item.getParent().getDisplay().getSystemColor(SWT.COLOR_RED));
				gc.fillRectangle(rec);
				gc.setBackground(pBG);
			}

			if (Tree.DRAW_IMAGES) {
				gc.drawImage(image, rec.x, rec.y);
			}
		}
	}

	private Rectangle getBounds(int columnIndex) {
		return item.getBounds(columnIndex);
	}

	private void drawItem(GC gc, boolean paintItemEvent) {
		Rectangle b = item.getFullBounds();
		gc.setClipping(b);

		Color prevBG = gc.getBackground();
		Color prevFG = gc.getForeground();

		try {
			final Tree parent = getParent();
			if (paintItemEvent) {
				Event event = new Event();
				Rectangle rec = item.getTextBounds(0);

				event.item = item;
				event.index = 0;
				event.gc = gc;
				event.x = rec.x;
				event.y = b.y;
				event.width = rec.width;
				event.height = b.height;
				event.doit = true;
				parent.sendEvent(SWT.MeasureItem, event);

				gc.setClipping((Rectangle) null);

				parent.sendEvent(SWT.EraseItem, event);

				drawBackground(event, gc);

				gc.setClipping(new Rectangle(event.x, event.y, event.width, event.height));

				event.item = item;
				event.index = 0;
				event.gc = gc;
				event.x = rec.x;
				event.y = b.y;
				event.width = rec.width;
				event.height = b.height;
				event.doit = true;

				parent.sendEvent(SWT.PaintItem, event);

				gc.setClipping((Rectangle) null);
				gc.setClipping(b);

				if (event.doit) {
					drawImage(item.getImage(), getImageBounds(0), gc);
					drawText(item.getText(), getTextBounds(0), gc);
				}
			}
			else {
				drawBackground(null, gc);
				drawImage(item.getImage(), getImageBounds(0), gc);
				drawText(item.getText(), getTextBounds(0), gc);
			}
		} finally {
			gc.setForeground(prevFG);
			gc.setBackground(prevBG);
			gc.setClipping((Rectangle) null);
		}
	}

	private void drawBackground(Event event, GC gc) {
		// TODO handle event details for background

		var bgCol = item.getBackground();
		if (bgCol != null && !this.selected && !this.hovered) {
			gc.setBackground(bgCol);
		}

		var fgCol = item.getForeground();
		if (fgCol != null && !this.selected && !this.hovered) {
			gc.setForeground(fgCol);
		}
		if (this.hovered) {
			gc.setBackground(item.getParent().getDisplay().getSystemColor(SWT.COLOR_YELLOW));
		}
		if (this.selected) {
			gc.setBackground(Table.SELECTION_COLOR);
		}
	}

	public Point computeCellSize(int colIndex) {
		final Point cellSize = computedCellSizes.get(colIndex);
		if (cellSize != null) {
			return cellSize;
		}

		if (!item.getParent().columnsExist()) {
			return computeSize(false);
		}

		Image image = item.getImage(colIndex);

		int height = topMargin + bottomMargin;
		int width = leftMargin + rightMargin;

		if (image != null) {
			final Rectangle bounds = image.getBounds();
			var rec = new Rectangle(width, topMargin, bounds.width, bounds.height);
			internalComputedCellImage.put(colIndex, rec);
			height += bounds.height;
			width += bounds.width;
		}

		String text = item.getText(colIndex);
		if (text != null) {
			Point size = getParent().computeTextExtent(text);

			Rectangle rec = new Rectangle(width, topMargin, size.x, size.y);
			internalComputedCellTextBounds.put(colIndex, rec);

			width += size.x;
			height += size.y;
		} else {
			internalComputedCellTextBounds.put(colIndex, new Rectangle(width, topMargin, 0, 0));
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

		// guess the line height for the text. Currently only support for one line
		int lineHeight = guessItemHeight(getParent());

		int currentWidth = 0;

		// 1.) Indent

		currentWidth += item.getIndent() * TreeItemRenderer.INDENT_WIDTH;
		// 2.) Expand Arrow

		Point expand = getArrowRectangleSize();

		// Bad design: two calculations for arrow rectangle: here and in
		// getRectangleSize
		lineHeight = Math.max(expand.y + topMargin + bottomMargin, lineHeight);

		currentWidth += leftMargin + expand.x + GAP;

		// 3.) Checkbox

		Point checkbox = getCheckBoxSize();

		if (checkbox != null) {
			currentWidth += checkbox.x + GAP;
			lineHeight = Math.max(checkbox.y + topMargin + bottomMargin, lineHeight);
		}

		// 4.) Image

		if (item.getImage() != null) {
			Rectangle imgB = item.getImage().getBounds();

			Rectangle rec = new Rectangle(currentWidth, topMargin, imgB.width, imgB.height);
			internalComputedCellImage.put(0, rec);

			lineHeight = Math.max(imgB.height + topMargin + bottomMargin, lineHeight);

			currentWidth += imgB.width + GAP;
		}

		// 5.) Text

		String text = item.getText();
		if (text == null) {
			text = "T"; // dummy text for minimal size calculation
		}

		Point textExtent = getParent().computeTextExtent(text);

		Rectangle rec = new Rectangle(currentWidth, topMargin, textExtent.x, textExtent.y);
		internalComputedCellTextBounds.put(0, rec);

		currentWidth += textExtent.x;

		currentWidth += rightMargin;
		lineHeight = Math.max(textExtent.y + topMargin + bottomMargin, lineHeight);

		if (getParent().columnsExist()) {
			currentWidth = getParent().getTotalColumnWidth();
		}

		this.computedSize = new Point(currentWidth, lineHeight);

		return this.computedSize;
	}

	void clearCache() {
		this.computedCellSizes.clear();
		this.internalComputedCellTextBounds.clear();
		this.internalComputedCellImage.clear();
		computedSize = null;
	}

	public static int guessItemHeight(Tree table) {
		int textHeight = Tree.guessTextHeight(table);
		return textHeight + topMargin + bottomMargin;
	}

	public Rectangle getTextBounds(int index) {
		if (internalComputedCellTextBounds.get(index) == null) {
			computeCellSize(index);
		}

		Rectangle internal = internalComputedCellTextBounds.get(index);

		Rectangle outer = item.getParent().columnsExist() && index != 0
				? getBounds(index)
				: item.getFullBounds();

		return new Rectangle(outer.x + internal.x, outer.y + internal.y, internal.width, internal.height);
	}

	public Rectangle getImageBounds(int index) {
		if (item.getImage(index) == null) {
			return new Rectangle(0, 0, 0, 0);
		}

		if (internalComputedCellImage.get(index) == null) {
			computeCellSize(index);
		}

		Rectangle internal = internalComputedCellImage.get(index);

		Rectangle outer = item.getParent().columnsExist() && index != 0
				? getBounds(index)
				: item.getFullBounds();

		return new Rectangle(outer.x + internal.x, outer.y + internal.y, internal.width, internal.height);
	}
}
