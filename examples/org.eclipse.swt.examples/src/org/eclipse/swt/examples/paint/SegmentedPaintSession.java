/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.paint;


import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

import java.util.*;

/**
 * The superclass for paint tools that contruct objects from individually
 * picked segments.
 */
public abstract class SegmentedPaintSession extends BasicPaintSession {
	/**
	 * The set of control points making up the segmented selection
	 */
	private Vector /* of Point */ controlPoints = new Vector();

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
	public void endSession() {
		getPaintSurface().clearRubberbandSelection();
		if (previousFigure != null) getPaintSurface().drawFigure(previousFigure);
	}
	
	/**
	 * Resets the tool.
	 * Aborts any operation in progress.
	 */
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
	public void mouseDown(MouseEvent event) {
		if (event.button != 1) return;

		getPaintSurface().setStatusMessage(PaintExample.getResourceString(
			"session.SegmentedInteractivePaint.message.interactiveMode"));
		previousFigure = currentFigure;

		if (controlPoints.size() > 0) {
			final Point lastPoint = (Point) controlPoints.elementAt(controlPoints.size() - 1);
			if (lastPoint.x == event.x || lastPoint.y == event.y) return; // spurious event
		}
		controlPoints.add(new Point(event.x, event.y));
	}

	/**
	 * Handles a mouseDoubleClick event.
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseDoubleClick(MouseEvent event) {
		if (event.button != 1) return;
		if (controlPoints.size() >= 2) {
			getPaintSurface().clearRubberbandSelection();
			previousFigure = createFigure(
				(Point[]) controlPoints.toArray(new Point[controlPoints.size()]),
				controlPoints.size(), true);
		}
		resetSession();
	}

	/**
	 * Handles a mouseUp event.
	 * 
	 * @param event the mouse event detail information
	 */
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
	public void mouseMove(MouseEvent event) {
		final PaintSurface ps = getPaintSurface();
		if (controlPoints.size() == 0) {
			ps.setStatusCoord(ps.getCurrentPosition());
			return; // spurious event
		}
		ps.setStatusCoordRange((Point) controlPoints.elementAt(controlPoints.size() - 1),
			ps.getCurrentPosition());
		ps.clearRubberbandSelection();
		Point[] points = (Point[]) controlPoints.toArray(new Point[controlPoints.size() + 1]);
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
