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


import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;

/**
 * The superclass for paint tools that contruct objects from individually
 * picked segments.
 */
public abstract class SegmentedPaintSession extends BasicPaintSession {
	/**
	 * The set of control points making up the segmented selection
	 */
	private List<Point> controlPoints = new ArrayList<>();

	/**
	 * The previous figure (so that we can abort with right-button)
	 */
	private Figure previousFigure = null;

	/**
	 * The current figure (so that we can abort with right-button)
	 */
	private Figure currentFigure = null;

	/**
	 * Constructs a PaintSession.
	 *
	 * @param paintSurface the drawing surface to use
	 */
	protected SegmentedPaintSession(PaintSurface paintSurface) {
		super(paintSurface);
	}

	/**
	 * Activates the tool.
	 */
	@Override
	public void beginSession() {
		getPaintSurface().setStatusMessage(PaintExample.getResourceString(
			"session.SegmentedInteractivePaint.message.anchorMode"));
		previousFigure = null;
		currentFigure = null;
		controlPoints.clear();
	}

	/**
	 * Deactivates the tool.
     */
	@Override
	public void endSession() {
		getPaintSurface().clearRubberbandSelection();
		if (previousFigure != null) getPaintSurface().drawFigure(previousFigure);
	}

	/**
	 * Resets the tool.
	 * Aborts any operation in progress.
	 */
	@Override
	public void resetSession() {
		getPaintSurface().clearRubberbandSelection();
		if (previousFigure != null) getPaintSurface().drawFigure(previousFigure);

		getPaintSurface().setStatusMessage(PaintExample.getResourceString(
			"session.SegmentedInteractivePaint.message.anchorMode"));
		previousFigure = null;
		currentFigure = null;
		controlPoints.clear();
	}

	/**
	 * Handles a mouseDown event.
	 *
	 * @param event the mouse event detail information
	 */
	@Override
	public void mouseDown(MouseEvent event) {
		if (event.button != 1) return;

		getPaintSurface().setStatusMessage(PaintExample.getResourceString(
			"session.SegmentedInteractivePaint.message.interactiveMode"));
		previousFigure = currentFigure;

		if (controlPoints.size() > 0) {
			final Point lastPoint = controlPoints.get(controlPoints.size() - 1);
			if (lastPoint.x == event.x || lastPoint.y == event.y) return; // spurious event
		}
		controlPoints.add(new Point(event.x, event.y));
	}

	/**
	 * Handles a mouseDoubleClick event.
	 *
	 * @param event the mouse event detail information
	 */
	@Override
	public void mouseDoubleClick(MouseEvent event) {
		if (event.button != 1) return;
		if (controlPoints.size() >= 2) {
			getPaintSurface().clearRubberbandSelection();
			previousFigure = createFigure(
				controlPoints.toArray(new Point[controlPoints.size()]),
				controlPoints.size(), true);
		}
		resetSession();
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
	}

	/**
	 * Handles a mouseMove event.
	 *
	 * @param event the mouse event detail information
	 */
	@Override
	public void mouseMove(MouseEvent event) {
		final PaintSurface ps = getPaintSurface();
		if (controlPoints.size() == 0) {
			ps.setStatusCoord(ps.getCurrentPosition());
			return; // spurious event
		}
		ps.setStatusCoordRange(controlPoints.get(controlPoints.size() - 1),
			ps.getCurrentPosition());
		ps.clearRubberbandSelection();
		Point[] points = controlPoints.toArray(new Point[controlPoints.size() + 1]);
		points[controlPoints.size()] = ps.getCurrentPosition();
		currentFigure = createFigure(points, points.length, false);
		ps.addRubberbandSelection(currentFigure);
	}

	/**
	 * Template Method: Creates a Figure for drawing rubberband entities and the final product
	 *
	 * @param points the array of control points
	 * @param numPoints the number of valid points in the array (n >= 2)
	 * @param closed true if the user double-clicked on the final control point
	 */
	protected abstract Figure createFigure(Point[] points, int numPoints, boolean closed);
}
