/*******************************************************************************
 * Copyright (c) 2025 Advantest Europe GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * 				Raghunandana Murthappa
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

class DefaultScrollBarRenderer extends ScrollBarRenderer {
	private static final int PREFERRED_WIDTH = 170;
	private static final int PREFERRED_HEIGHT = 18;

	static final String COLOR_TRACK_BACKGROUND = "slider.track.background"; //$NON-NLS-1$
	static final String COLOR_TRACK_BORDER = "slider.track.border"; //$NON-NLS-1$
	static final String COLOR_THUMB_BACKGROUND = "slider.thumb.background"; //$NON-NLS-1$
	static final String COLOR_THUMB_BORDER = "slider.thumb.border"; //$NON-NLS-1$
	static final String COLOR_THUMB_HOVER = "slider.thumb.hover"; //$NON-NLS-1$

	private boolean drawTrack;

	private Rectangle trackRectangle;
	private Rectangle thumbRectangle;
	private boolean isDragging;
	private boolean thumbHovered;

	protected DefaultScrollBarRenderer(ScrollBar scrollbar) {
		super(scrollbar);
	}

	@Override
	protected void paint(GC gc, int width, int height) {

		var parentBounds = scrollbar.getParent().getBounds();
		System.err.println("Scrollable paint method:" + scrollbar.getParent() + "  " + parentBounds);
		var scrollBarBounds = scrollbar.getBounds();
		System.out.println("Scrollbar bounds: " + scrollBarBounds);
		int selection = scrollbar.getSelection();
		System.out.println("Selection: " + selection);
		int min = scrollbar.getMinimum();
		int max = scrollbar.getMaximum();
		int thumb = scrollbar.getThumb();
		int range = max - min;

		// Fill background
		gc.setBackground(scrollbar.getParent().getBackground());
		gc.fillRectangle(scrollBarBounds);

		gc.setForeground(
				scrollbar.getParent().isEnabled() ? scrollbar.getParent().getForeground() : getColor(COLOR_DISABLED));
		if (scrollbar.isVertical()) {
			int trackX = scrollBarBounds.x - 4;
			int trackY = scrollBarBounds.y;
			int trackWidth = scrollBarBounds.width;
			int trackHeight = scrollBarBounds.height;
			trackRectangle = new Rectangle(trackX, trackY, trackWidth, trackHeight);
			System.err.println("trackRectangle:" + trackRectangle);
			int thumbHeight = Math.max(10, (thumb * (height - 4)) / range);
			int thumbWidth = trackWidth;

			int adjustedRange = range - thumb;
			int thumbY = trackY + ((height - thumbHeight - 4) * (selection - min)) / adjustedRange;
			if (!isDragging) {
				thumbRectangle = new Rectangle(trackX, thumbY, thumbWidth, thumbHeight);
			}
			gc.drawLine(0, 0, 0, height);
		} else {
			int trackX = scrollBarBounds.x;
			int trackY = scrollBarBounds.y;
			int trackWidth = scrollBarBounds.width;
			int trackHeight = scrollBarBounds.height;
			trackRectangle = new Rectangle(trackX, trackY, trackWidth, trackHeight);

			int thumbWidth = Math.max(10, (thumb * (width - 4)) / range);
			int thumbHeight = trackHeight;

			int adjustedRange = range - thumb;
			int thumbX = trackX + ((width - thumbWidth - 4) * (selection - min)) / adjustedRange;
			if (!isDragging) {
				thumbRectangle = new Rectangle(thumbX, trackY, thumbWidth, thumbHeight);
			}
			gc.drawLine(0, 0, width, 0);
		}

		gc.setForeground(getColor(COLOR_TRACK_BORDER));
		// Draw the track
		// if (drawTrack) {
		gc.setBackground(getColor(COLOR_TRACK_BACKGROUND));
		drawRoundRectWithBorder(gc, trackRectangle);
		// }

		// Draw the thumb
		gc.setBackground(getColor(thumbHovered || isDragging ? COLOR_THUMB_HOVER : COLOR_THUMB_BACKGROUND));
		gc.setForeground(getColor(COLOR_THUMB_BORDER));
		drawRoundRectWithBorder(gc, thumbRectangle);
	}

	private void drawRoundRectWithBorder(GC gc, Rectangle rect) {
		final int arcSize = 6;
		gc.fillRoundRectangle(rect.x, rect.y, rect.width + 1, rect.height + 1, arcSize, arcSize);
		if (scrollbar.isEnabled()) {
			gc.drawRoundRectangle(rect.x, rect.y, rect.width, rect.height, arcSize, arcSize);
		}
	}

	@Override
	public Point computeDefaultSize() {
		int width;
		int height;
		if (scrollbar.isVertical()) {
			width = PREFERRED_HEIGHT;
			height = PREFERRED_WIDTH;
		} else {
			width = PREFERRED_WIDTH;
			height = PREFERRED_HEIGHT;
		}

		return new Point(width, height);
	}

	@Override
	public void setDrawTrack(boolean drawTrack) {
		this.drawTrack = drawTrack;
	}

	@Override
	public Rectangle getThumbRectangle() {
		return thumbRectangle;
	}

	@Override
	public Rectangle getTrackRectangle() {
		return trackRectangle;
	}

	@Override
	public void setDragging(boolean dragging) {
		this.isDragging = dragging;

	}

	@Override
	public void setThumbHovered(boolean thumbHovered) {
		this.thumbHovered = thumbHovered;

	}

	@Override
	public boolean getDragging() {
		return isDragging;
	}

	@Override
	public boolean getHovered() {
		return thumbHovered;
	}

	public int getSelectionOfPosition(int x, int y) {

		if (scrollbar.isVertical()) {

			var scrollBarBounds = scrollbar.getBounds();
			var thumb = scrollbar.getThumb();

			var height = scrollBarBounds.height;
			int range = scrollbar.getMaximum() - scrollbar.getMinimum();
			var min = scrollbar.getMinimum();

			int trackX = scrollBarBounds.x - 4;
			int trackY = scrollBarBounds.y;
			int trackWidth = scrollBarBounds.width;
			int trackHeight = scrollBarBounds.height;
			trackRectangle = new Rectangle(trackX, trackY, trackWidth, trackHeight);
			System.err.println("trackRectangle:" + trackRectangle);
			int thumbHeight = Math.max(10, (thumb * (height - 4)) / range);

			int adjustedRange = range - thumb;
			int selection = (((y - trackY) * adjustedRange) / ((height - thumbHeight - 4))) + min;

			System.out.println("Selection from position on vertical mouse: " + selection);

			return selection;

		}

		var scrollBarBounds = scrollbar.getBounds();
		var thumb = scrollbar.getThumb();

		var width = scrollBarBounds.width;
		int range = scrollbar.getMaximum() - scrollbar.getMinimum();
		var min = scrollbar.getMinimum();

		int trackX = scrollBarBounds.x;
		int trackY = scrollBarBounds.y;
		int trackWidth = scrollBarBounds.width;
		int trackHeight = scrollBarBounds.height;
		trackRectangle = new Rectangle(trackX, trackY, trackWidth, trackHeight);

		int thumbWidth = Math.max(10, (thumb * (width - 4)) / range);

		int adjustedRange = range - thumb;

		int selection = ((x - trackX) * adjustedRange) / ((width - thumbWidth - 4)) + min;

		return selection;

	}
}
