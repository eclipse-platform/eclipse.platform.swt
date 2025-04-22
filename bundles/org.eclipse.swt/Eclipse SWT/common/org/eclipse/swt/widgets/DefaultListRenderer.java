package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class DefaultListRenderer extends ControlRenderer {

	private List list;

	private final static int FLAGS = SWT.DRAW_DELIMITER | SWT.DRAW_TAB;

	private java.util.List<Integer> lineStartPositions = new ArrayList<>();

	private int lineHeight = -1;

	protected DefaultListRenderer(List list) {
		super(list);
		this.list = list;
	}

	public int getLineHeight(GC gc) {
		list.checkWidget();
		return gc.getFontMetrics().getHeight();
	}

	@Override
	protected void paint(GC gc, int width, int height) {

		Rectangle r = list.getBounds();
		if (r.width == 0 && r.height == 0) {
			return;
		}

		final Rectangle clientArea = list.getClientArea();
		drawBackground(clientArea, gc);

		if ((list.getStyle() & SWT.BORDER) != 0) {
			int borderSize = list.getBorderWidth();
			clientArea.x += borderSize;
			clientArea.y += borderSize;
		}

		this.lineHeight = getLineHeight(gc);
		int x = clientArea.x;
		int y = clientArea.y;

		lineStartPositions.clear();

		for (int i = list.getTopIndex(); i < list.getItems().length; i++) {
			lineStartPositions.add(y);
			drawTextLine(i, x, y, gc, clientArea);
			y += lineHeight;
		}

	}

	private void drawBackground(Rectangle clientArea, GC gc) {

		if ((list.getState() & Widget.PARENT_BACKGROUND) == 0) {

			if (list.getBackground() == null) {
				// white is the default color for lists
				gc.setBackground(list.getDisplay().getSystemColor(SWT.COLOR_WHITE));
			}
		}

		gc.fillRectangle(clientArea.x, clientArea.y, clientArea.width, clientArea.height - 1);

		if ((list.getStyle() & SWT.BORDER) != 0 && list.isEnabled()) {
			Color foreground = gc.getForeground();
			gc.setForeground(list.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
			gc.drawLine(clientArea.x, clientArea.y + clientArea.height - 1,
					clientArea.x + clientArea.x + clientArea.width - 1, clientArea.y + clientArea.height - 1);
			gc.setForeground(foreground);
		}
	}

	private int calculateHorizontalAlignment(int x, Point textExtent, Rectangle clientArea) {
		if ((list.getStyle() & SWT.CENTER) != 0) {
			return (clientArea.width - textExtent.x) / 2;
		} else if ((list.getStyle() & SWT.RIGHT) != 0) {
			return clientArea.width - textExtent.x;
		}
		return x;
	}

	private void drawTextLine(int lineNumber, int x, int y, GC gc, Rectangle clientArea) {
		String text = list.getItems()[lineNumber];
		Point textExtent = gc.textExtent(text);

		int _x = calculateHorizontalAlignment(x, textExtent, clientArea);
		if (list.getHorizontalBar() != null) {
			_x -= list.getHorizontalBar().getSelection();
		}

		if (list.isEnabled()) {
			if (list.isSelected(lineNumber)) {
				drawSelectedText(text, gc, _x, y, clientArea, textExtent);
			} else {
				gc.drawText(text, _x, y, true);

			}
		} else {
			Color foreground = gc.getForeground();
			gc.setForeground(list.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
			gc.drawText(text, _x, y, true);

			gc.setForeground(foreground);
		}
	}

	private void drawSelectedText(String text, GC gc, int _x, int _y, Rectangle clientArea, Point textExtent) {
		Color background = gc.getBackground();
		Color foreground = gc.getForeground();
		gc.setForeground(list.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
		gc.setBackground(list.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
		gc.fillRectangle(0, _y, clientArea.width, textExtent.y);
		gc.drawText(text, _x, _y);

		gc.setForeground(foreground);
		gc.setBackground(background);
	}

	public int getLineHeight() {
		return this.lineHeight;
	}

	public Point computeDefaultSize() {

		Point size = computeTextSize();
		if ((list.getStyle() & SWT.BORDER) != 0) {
			final int borderWidth = list.getBorderWidth();
			size.x += 2 * borderWidth + List.INSET;
			size.y += 2 * borderWidth;
		}
		return size;

	}

	private Point getTextExtent(String text) {
		return Drawing.getTextExtent(list, text, FLAGS);
	}

	Point computeTextSize() {

		int width = 0;
		Point size = null;
		for (String line : list.getItems()) {
			size = getTextExtent(line);
			width = Math.max(width, size.x);
		}
		int height = size != null ? size.y * list.getItemCount() : 0;
		if (list.getHorizontalBar() != null) {
			height += list.getHorizontalBar().getSize().y;
		}
		if (list.getVerticalBar() != null) {
			width += list.getVerticalBar().getSize().x;
		}
		return new Point(width, height);
	}

}
