/*******************************************************************************
 * Copyright (c) 2025 ETAS GmbH and others, all rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ETAS GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

/**
 * Default implementation of the SashRenderer for rendering a Sash widget.
 * Provides concrete implementations for painting, size computation, and drag
 * handling.
 */
public class DefaultSashRenderer extends SashRenderer {

	private static final int DEFAULT_WIDTH = 64;
	private static final int DEFAULT_HEIGHT = 64;
	private boolean isDragging;
	private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
	private static final Color FOREGROUND_COLOR = new Color(0, 0, 0);
	private static final int SASH_MARGIN = 3;
	private Rectangle sashBounds;

	/**
	 * Constructs a DefaultSashRenderer for the specified Sash.
	 *
	 * @param sash the Sash widget to be rendered.
	 */
	public DefaultSashRenderer(Sash sash) {
		super(sash);
		sashBounds = sash.getBounds();
	}

	/**
	 * Computes the default size of the Sash based on its orientation.
	 *
	 * @return a Point representing the default width and height of the Sash.
	 */
	@Override
	public Point computeDefaultSize() {
		int width = 0;
		int height = 0;
		if (sash.isHorizontal()) {
			width += DEFAULT_WIDTH;
			height += SASH_MARGIN;
		} else {
			width += SASH_MARGIN;
			height += DEFAULT_HEIGHT;
		}
		return new Point(width, height);
	}

	/**
	 * Draws a drag band for the Sash within the specified bounds.
	 *
	 * @param gc     the graphics context used for drawing.
	 * @param bounds the bounds of the drag band to be drawn.
	 */
	@Override
	protected void drawBand(GC gc, Rectangle bounds) {
		if (isBrandRequired()) {
			drawCheckerBoardPattern(gc, bounds);
		}
	}

	/**
	 * Draws a checkerboard pattern within the specified bounds.
	 *
	 * @param gc     the graphics context used for drawing.
	 * @param bounds the bounds of the checkerboard pattern to be drawn.
	 */
	private void drawCheckerBoardPattern(GC gc, Rectangle bounds) {
		int squareSize = 1;
		for (int y1 = bounds.y; y1 < bounds.y + bounds.height; y1 += squareSize) {
			for (int x1 = bounds.x; x1 < bounds.x + bounds.width; x1 += squareSize) {
				if ((x1 / squareSize + y1 / squareSize) % 2 == 0) {
					gc.setBackground(BACKGROUND_COLOR);
				} else {
					gc.setBackground(FOREGROUND_COLOR);
				}
				gc.fillRectangle(x1, y1, squareSize, squareSize);
			}
		}
	}

	/**
	 * Sets the dragging state of the Sash.
	 *
	 * @param dragging true to indicate the Sash is being dragged; false otherwise.
	 */
	@Override
	public void setDragging(boolean dragging) {
		this.isDragging = dragging;
	}

	/**
	 * Gets the current dragging state of the Sash.
	 *
	 * @return true if the Sash is being dragged; false otherwise.
	 */
	@Override
	public boolean isDragging() {
		return isDragging;
	}

	/**
	 * Sets the bounds of the Sash.
	 *
	 * @param lastX  the x-coordinate of the Sash.
	 * @param lastY  the y-coordinate of the Sash.
	 * @param width  the width of the Sash.
	 * @param height the height of the Sash.
	 */
	@Override
	protected void setSashBounds(int lastX, int lastY, int width, int height) {
		sashBounds = new Rectangle(lastX, lastY, width, height);
	}

	/**
	 * Gets the bounds of the Sash.
	 *
	 * @return a Rectangle representing the bounds of the Sash.
	 */
	@Override
	public Rectangle getSashBounds() {
		return sashBounds;
	}

	/**
	 * Gets the default background color of the Sash.
	 *
	 * @return the default background Color.
	 */
	@Override
	public Color getDefaultBackground() {
		return BACKGROUND_COLOR;
	}

	/**
	 * Gets the default foreground color of the Sash.
	 *
	 * @return the default foreground Color.
	 */
	@Override
	public Color getDefaultForeground() {
		return FOREGROUND_COLOR;
	}

	/**
	 * Paints the Sash with the specified width and height. The method sets the
	 * background and fills the rectangle defined by the Sash bounds.
	 *
	 * @param gc     the graphics context used for painting.
	 * @param width  the width of the Sash.
	 * @param height the height of the Sash.
	 */
	@Override
	protected void paint(GC gc, int width, int height) {
		gc.setBackground(BACKGROUND_COLOR);
		if (!isBrandRequired()) {
			gc.fillRectangle(sashBounds);
		}
	}

	/**
	 * Checks if a drag overlay (brand) is required for the Sash.
	 *
	 * @return true if the drag overlay is enabled and visible, false otherwise.
	 */
	private boolean isBrandRequired() {
		return sash.dragOverlay != null && sash.dragOverlay.isEnabled() && sash.dragOverlay.isVisible();
	}
}