package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.events.MouseEvent;import org.eclipse.swt.graphics.GC;import org.eclipse.swt.graphics.Point;

/**
 * The superclass for paint tools that use click-drag-release motions to
 * draw objects
 */
public abstract class DragInteractivePaintSession extends BasicPaintSession 
	implements PaintRenderer {
	/**
	 * True iff a click-drag is in progress
	 */
	private boolean dragInProgress;
	
	/**
	 * The position of the first click in a click-drag
	 */
	private Point anchorPosition = new Point(-1, -1);
	
	/**
	 * The position of the last drawn temporary point in a click-drag
	 */
	private Point tempPosition = new Point(-1, -1);
	
	/**
	 * Create a DragInteractivePaintSession
	 * 
	 * @param getPaintSurface() the drawing surface to use
	 */
	protected DragInteractivePaintSession(PaintSurface paintSurface) {
		super(paintSurface);
	}

	/**
	 * Activate the tool.
	 */
	public void beginSession() {
		getPaintSurface().getPaintStatus().
			setMessage(PaintPlugin.getResourceString("session.DragInteractivePaint.message"));

		anchorPosition.x = -1;
		tempPosition.x = -1;
		dragInProgress = false;
	}
	
	/**
	 * Deactivate the tool.
     */
	public void endSession() {
	}
	
	/**
	 * Reset the tool.
	 * Aborts any operation in progress.
	 */
	public void resetSession() {
		if (tempPosition.x != -1) { // restore old image
			final GC gc = getPaintSurface().getGC();	
			eraseTemporary(gc, anchorPosition, tempPosition);
		}
		anchorPosition.x = -1;
		tempPosition.x = -1;
		dragInProgress = false;
	}

	/**
	 * Handle a mouseDown event
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseDown(MouseEvent event) {
		if (event.button != 1) return;
		if (dragInProgress) return; // spurious event
		dragInProgress = true;
		
		anchorPosition.x = event.x;
		anchorPosition.y = event.y;
		tempPosition.x = -1;
	}

	/**
	 * Handle a mouseDoubleClick event
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseDoubleClick(MouseEvent event) {
	}

	/**
	 * Handle a mouseUp event
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseUp(MouseEvent event) {
		if (event.button != 1) {
			resetSession(); // abort if right or middle mouse button pressed
			return;
		}
		if (! dragInProgress) return; // spurious event
		dragInProgress = false;
		if (anchorPosition.x == -1) return; // spurious event
		
		final GC gc = getPaintSurface().getGC();		
		if (tempPosition.x != -1) { // restore old image
			eraseTemporary(gc, anchorPosition, tempPosition);
		}

		// draw permanent entiry
		tempPosition.x = event.x;
		tempPosition.y = event.y;
		drawPermanent(gc, anchorPosition, tempPosition);
		tempPosition.x = -1;
	}
	
	/**
	 * Handle a mouseMove event
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseMove(MouseEvent event) {
		if (! dragInProgress) {
			getPaintSurface().showCurrentPositionStatus();
			return;
		}
		getPaintSurface().showCurrentRangeStatus(anchorPosition);

		final GC gc = getPaintSurface().getGC();
		
		if (tempPosition.x != -1) { // restore old image
			eraseTemporary(gc, anchorPosition, tempPosition);
		}
		// draw temporary entity
		tempPosition.x = event.x;
		tempPosition.y = event.y;
		drawTemporary(gc, anchorPosition, tempPosition);
	}
	
	/**
	 * Draw a permanent entity given 2 points
	 * 
	 * @param points[0] and points[1] are two points
	 * @param numPoints the number of valid points in the array (must be 2)
	 */
	public void render(final Point[] points, int numPoints) {
		Assert.assert(numPoints == 2);
		final GC gc = getPaintSurface().getGC();
		drawPermanent(gc, points[0], points[1]);
	}
	
	/**
	 * Draw a permanent entity
	 */
	protected abstract void drawPermanent(GC gc, Point a, Point b);

	/**
	 * Draw a temporary entity
	 */
	protected abstract void drawTemporary(GC gc, Point a, Point b);

	/**
	 * Erase a temporary entity
	 */
	protected abstract void eraseTemporary(GC gc, Point a, Point b);	
}
