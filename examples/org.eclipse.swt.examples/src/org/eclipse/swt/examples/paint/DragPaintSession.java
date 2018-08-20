/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.paint;


import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;

/**
 * The superclass for paint tools that use click-drag-release motions to
 * draw objects.
 */
public abstract class DragPaintSession extends BasicPaintSession {
	/**
	 * True if a click-drag is in progress
	 */
	private boolean dragInProgress = false;

	/**
	 * The position of the first click in a click-drag
	 */
	private Point anchorPosition = new Point(-1, -1);

	/**
	 * A temporary point
	 */
	private Point tempPosition = new Point(-1, -1);

	/**
	 * Constructs a PaintSession.
	 *
	 * @param getPaintSurface() the drawing surface to use
	 */
	protected DragPaintSession(PaintSurface paintSurface) {
		super(paintSurface);
	}

	/**
	 * Activates the tool.
	 */
	@Override
	public void beginSession() {
		getPaintSurface().
			setStatusMessage(PaintExample.getResourceString("session.DragInteractivePaint.message"));
		anchorPosition.x = -1;
		dragInProgress = false;
	}

	/**
	 * Deactivates the tool.
     */
	@Override
	public void endSession() {
	}

	/**
	 * Resets the tool.
	 * Aborts any operation in progress.
	 */
	@Override
	public void resetSession() {
		getPaintSurface().clearRubberbandSelection();
		anchorPosition.x = -1;
		dragInProgress = false;
	}

	/**
	 * Handles a mouseDown event.
	 *
	 * @param event the mouse event detail information
	 */
	@Override
	public void mouseDown(MouseEvent event) {
		if (event.button != 1) return;
		if (dragInProgress) return; // spurious event
		dragInProgress = true;

		anchorPosition.x = event.x;
		anchorPosition.y = event.y;
	}

	/**
	 * Handles a mouseDoubleClick event.
	 *
	 * @param event the mouse event detail information
	 */
	@Override
	public void mouseDoubleClick(MouseEvent event) {
	}

	/**
	 * Handles a mouseUp event.
	 *
	 * @param event the mouse event detail information
	 */
	@Override
	public void mouseUp(MouseEvent event) {
		if (event.button != 1) {
			resetSession(); // abort if right or middle mouse button pressed
			return;
		}
		if (! dragInProgress) return; // spurious event
		dragInProgress = false;
		if (anchorPosition.x == -1) return; // spurious event

		getPaintSurface().commitRubberbandSelection();
	}

	/**
	 * Handles a mouseMove event.
	 *
	 * @param event the mouse event detail information
	 */
	@Override
	public void mouseMove(MouseEvent event) {
		final PaintSurface ps = getPaintSurface();
		if (! dragInProgress) {
			ps.setStatusCoord(ps.getCurrentPosition());
			return;
		}
		ps.setStatusCoordRange(anchorPosition, ps.getCurrentPosition());
		ps.clearRubberbandSelection();
		tempPosition.x = event.x;
		tempPosition.y = event.y;
		ps.addRubberbandSelection(createFigure(anchorPosition, tempPosition));
	}

	/**
	 * Template Method: Creates a Figure for drawing rubberband entities and the final product
	 *
	 * @param anchor the anchor point
	 * @param cursor the point marking the current pointer location
	 */
	protected abstract Figure createFigure(Point anchor, Point cursor);
}
