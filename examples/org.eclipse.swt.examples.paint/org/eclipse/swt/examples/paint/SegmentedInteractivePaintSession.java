package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.events.MouseEvent;import org.eclipse.swt.graphics.*;

/**
 * The superclass for paint tools that contruct objects from individually
 * picked segments.
 */
public abstract class SegmentedInteractivePaintSession extends BasicPaintSession 
	implements PaintRenderer {
	/**
	 * The position of the first click in a segmented selection
	 */
	private Point anchorPosition = new Point(-1, -1);
	
	/**
	 * The position of the first anchor in a segmented selection sequence
	 */
	protected Point firstAnchorPosition = new Point(-1, 0);

	/**
	 * Constructs a SegmentedInteractivePaintSession.
	 * 
	 * @param paintSurface the drawing surface to use
	 */
	protected SegmentedInteractivePaintSession(PaintSurface paintSurface) {
		super(paintSurface);
	}

	/**
	 * Activates the tool.
	 */
	public void beginSession() {
		getPaintSurface().getPaintStatus().setMessage(PaintPlugin.getResourceString(
			"session.SegmentedInteractivePaint.message.anchorMode"));

		anchorPosition.x = -1;
		firstAnchorPosition.x = -1;
	}
	
	/**
	 * Deactivates the tool.
     */
	public void endSession() {
	}
	
	/**
	 * Resets the tool.
	 * Aborts any operation in progress.
	 */
	public void resetSession() {
		getPaintSurface().getPaintStatus().setMessage(PaintPlugin.getResourceString(
			"session.SegmentedInteractivePaint.message.anchorMode"));
		getPaintSurface().clearRubberbandSelection();
		anchorPosition.x = -1;
		firstAnchorPosition.x = -1;
	}

	/**
	 * Handles a mouseDown event.
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseDown(MouseEvent event) {
		if (event.button != 1) return;

		getPaintSurface().commitRubberbandSelection();
		anchorPosition.x = event.x;
		anchorPosition.y = event.y;
		if (firstAnchorPosition.x == -1) {
			firstAnchorPosition.x = anchorPosition.x;
			firstAnchorPosition.y = anchorPosition.y;
		}
		getPaintSurface().getPaintStatus().setMessage(PaintPlugin.getResourceString(
			"session.SegmentedInteractivePaint.message.interactiveMode"));
	}

	/**
	 * Handles a mouseDoubleClick event.
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseDoubleClick(MouseEvent event) {
		if (event.button != 1) return;
		if (firstAnchorPosition.x == -1) return; // spurious event
		Assert.assert(anchorPosition.x != -1);	

		getPaintSurface().clearRubberbandSelection();
		getPaintSurface().drawFigure(createFigure(anchorPosition, firstAnchorPosition));
		anchorPosition.x = -1;
		firstAnchorPosition.x = -1;

		getPaintSurface().getPaintStatus().setMessage(PaintPlugin.getResourceString(
			"session.SegmentedInteractivePaint.message.anchorMode"));
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
		if (anchorPosition.x == -1) {
			ps.showCurrentPositionStatus();
			return; // spurious event
		} else {
			ps.showCurrentRangeStatus(anchorPosition);
		}

		ps.clearRubberbandSelection();
		ps.addRubberbandSelection(createFigure(anchorPosition, ps.getCurrentPosition()));
	}	

	/**
	 * Draws a permanent entity given 2...n points.
	 * 
	 * @param points[0] and points[1] are two points
	 * @param numPoints the number of valid points in the array (n >= 2)
	 */
	public void render(final Point[] points, int numPoints) {
		final PaintSurface ps = getPaintSurface();
		Assert.assert(numPoints >= 2);

		for (int i = 1; i < numPoints; ++i) {
			ps.drawFigure(createFigure(points[i - 1], points[i]));
		}
		ps.drawFigure(createFigure(points[numPoints - 1], points[0]));
	}
	
	/**
	 * Template Method: Creates a Figure for drawing rubberband entities and the final product
	 * 
	 * @param anchor the anchor point
	 * @param cursor the point marking the current pointer location
	 */
	protected abstract Figure createFigure(Point anchor, Point cursor);
}
