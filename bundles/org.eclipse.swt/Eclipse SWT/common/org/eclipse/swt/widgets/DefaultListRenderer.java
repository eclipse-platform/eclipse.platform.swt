package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;

public class DefaultListRenderer extends ListRenderer {

	private final static int FLAGS = SWT.DRAW_DELIMITER | SWT.DRAW_TAB;

	private int lineHeight = -1;
	/* Scrollbar measurement cache: scalar, parent-stamped, never a per-row width array. */
	private int scrollMaxWidth, scrollLastHeight, scrollMeasuredCount;
	private long scrollMeasuredVersion = Long.MIN_VALUE;

	protected DefaultListRenderer(List list) {
		super(list);
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

		this.lineHeight = Math.max(1, gc.getFontMetrics().getHeight());
		if (clientArea.width <= 0 || clientArea.height <= 0) return;
		int start = Math.max(0, list.getTopIndex());
		int count = list.getItemCount();
		if (start >= count) return;
		// The LIST itself owns the logical model and scroll origin; no whole-list copy.
		// A partially visible last row must still be painted, respecting the GC clip.
		long visible = ((long) clientArea.height + lineHeight - 1L) / lineHeight;
		int end = (int) Math.min((long) count, (long) start + visible);
		long y = clientArea.y;
		for (int row = start; row < end; row++) {
			if (y > Integer.MAX_VALUE) break;
			drawTextLine(row, clientArea.x, (int) y, gc, clientArea);
			y += lineHeight;
		}
	}

	private void drawBackground(Rectangle clientArea, GC gc) {
		if ((list.getState() & Widget.PARENT_BACKGROUND) == 0) {
			gc.setBackground(list.getBackground());
		}

		gc.fillRectangle(clientArea.x, clientArea.y, clientArea.width, clientArea.height - 1);

		if ((list.getStyle() & SWT.BORDER) != 0 && list.isEnabled()) {
			Color foreground = gc.getForeground();
			gc.setForeground(getColor(COLOR_BORDER));
			gc.drawLine(clientArea.x, clientArea.y + clientArea.height - 1,
					clientArea.x + clientArea.x + clientArea.width - 1, clientArea.y + clientArea.height - 1);
			gc.setForeground(foreground);
		}
	}

	private int calculateHorizontalAlignment(int x, Point textExtent, Rectangle clientArea) {
		if ((list.getStyle() & SWT.CENTER) != 0) {
			return (clientArea.width - textExtent.x) / 2;
		}
		if ((list.getStyle() & SWT.RIGHT) != 0) {
			return clientArea.width - textExtent.x;
		}
		return x;
	}

	private void drawTextLine(int lineNumber, int x, int y, GC gc, Rectangle clientArea) {
		String text = list.getItem(lineNumber);
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
			gc.setForeground(getColor(COLOR_DISABLED));
			gc.drawText(text, _x, y, true);

			gc.setForeground(foreground);
		}
	}

	private void drawSelectedText(String text, GC gc, int _x, int _y, Rectangle clientArea, Point textExtent) {
		Color background = gc.getBackground();
		Color foreground = gc.getForeground();
		gc.setForeground(getColor(COLOR_SELECTION_FOREGROUND));
		gc.setBackground(getColor(COLOR_SELECTION_BACKGROUND));
		gc.fillRectangle(0, _y, clientArea.width, textExtent.y);
		gc.drawText(text, _x, _y);

		gc.setForeground(foreground);
		gc.setBackground(background);
	}

	@Override
	public int getLineHeight() {
		if (lineHeight <= 0) lineHeight = Math.max(1, measure(gc -> gc.getFontMetrics().getHeight()));
		return lineHeight;
	}

	/** An SWT font change invalidates geometry without creating another renderer. */
	void invalidateMetrics() {
		lineHeight = -1;
		scrollMeasuredVersion = Long.MIN_VALUE;
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
		return getTextExtent(text, FLAGS);
	}

	/**
	 * Recompute the same exact text extent for scrollbar updates. A tail append
	 * adds just one measured text; anything else falls back to a full scan.
	 * Explicit computeTextSize() continues to measure every row by contract.
	 * Cache data are only two primitive measurements and source epoch/count.
	 */
	Point computeScrollSize() {
		int count = list.getItemCount();
		long epoch = list.contentGeneration();
		if (scrollMeasuredVersion != epoch || scrollMeasuredCount != count) {
			boolean append = scrollMeasuredVersion != Long.MIN_VALUE
					&& scrollMeasuredVersion != Long.MAX_VALUE
					&& epoch == scrollMeasuredVersion + 1L
					&& list.lastAppendGeneration() == epoch
					&& scrollMeasuredCount < Integer.MAX_VALUE
					&& count == scrollMeasuredCount + 1;
			if (append) {
				Point measured = getTextExtent(list.getItem(count - 1));
				scrollMaxWidth = Math.max(scrollMaxWidth, measured.x);
				scrollLastHeight = measured.y;
			} else {
				int max = 0, lastHeight = 0;
				for (int i = 0; i < count; i++) {
					Point measured = getTextExtent(list.getItem(i));
					max = Math.max(max, measured.x);
					lastHeight = measured.y;
				}
				scrollMaxWidth = max;
				scrollLastHeight = lastHeight;
			}
			scrollMeasuredVersion = epoch;
			scrollMeasuredCount = count;
		}
		// Deliberately retain the SWT integer-height arithmetic and bar additions.
		int width = scrollMaxWidth;
		int height = count == 0 ? 0 : scrollLastHeight * count;
		if (list.getHorizontalBar() != null) height += list.getHorizontalBar().getSize().y;
		if (list.getVerticalBar() != null) width += list.getVerticalBar().getSize().x;
		return new Point(width, height);
	}

	public Point computeTextSize() {
		int width = 0;
		Point size = null;
		for (int i = 0, count = list.getItemCount(); i < count; i++) {
			size = getTextExtent(list.getItem(i));
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
