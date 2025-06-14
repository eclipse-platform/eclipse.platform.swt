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
 * Abstract class for rendering a Sash widget in the SWT framework. This class
 * provides a base for implementing custom rendering logic for a Sash, including
 * painting, size computation, and drag handling.
 */
public abstract class SashRenderer extends ControlRenderer {

	/**
	 * Computes the default size of the Sash.
	 *
	 * @return a {@link Point} representing the default width and height of the
	 *         Sash.
	 */
	public abstract Point computeDefaultSize();

	/**
	 * The Sash widget associated with this renderer.
	 */
	protected final Sash sash;

	/**
	 * Constructs a new SashRenderer for the specified Sash.
	 *
	 * @param sash the Sash widget to be rendered.
	 */
	protected SashRenderer(Sash sash) {
		super(sash);
		this.sash = sash;
	}

	/**
	 * Sets the dragging state of the Sash.
	 *
	 * @param dragging true to indicate the Sash is being dragged, false otherwise.
	 */
	protected abstract void setDragging(boolean dragging);

	/**
	 * Gets the current dragging state of the Sash.
	 *
	 * @return true if the Sash is being dragged, false otherwise.
	 */
	protected abstract boolean isDragging();

	/**
	 * Gets the bounds of the Sash.
	 *
	 * @return a {@link Rectangle} representing the bounds of the Sash.
	 */
	protected abstract Rectangle getSashBounds();

	/**
	 * Sets the bounds of the Sash.
	 *
	 * @param x      the x-coordinate of the Sash.
	 * @param y      the y-coordinate of the Sash.
	 * @param width  the width of the Sash.
	 * @param height the height of the Sash.
	 */
	protected abstract void setSashBounds(int x, int y, int width, int height);

	/**
	 * Draws a drag band for the Sash within the specified bounds.
	 *
	 * @param gc     the graphics context used for drawing.
	 * @param bounds the bounds of the drag band to be drawn.
	 */
	protected abstract void drawBand(GC gc, Rectangle bounds);
}
