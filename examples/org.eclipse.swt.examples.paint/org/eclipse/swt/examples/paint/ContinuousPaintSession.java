package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;

/**
 * The superclass for paint tools that draw continuously along the path
 * traced by the mouse's movement while the button is depressed
 */
public abstract class ContinuousPaintSession extends BasicPaintSession implements PaintRenderer {
	/**
	 * True if a click-drag is in progress
	 */
	protected boolean dragInProgress;
	
	/**
	 * A cached Point array for drawing
	 */
	protected Point[] cachedPointArray = new Point[] { new Point(-1, -1), new Point(-1, -1) };

	/**
	 * Constructs a ContinuousPaintSession.
	 * 
	 * @param paintSurface the drawing surface to use
	 */
	protected ContinuousPaintSession(PaintSurface paintSurface) {
		super(paintSurface);
	}

	/**
	 * Activates the tool.
	 */
	public void beginSession() {
		getPaintSurface().getPaintStatus().
			setMessage(PaintPlugin.getResourceString("session.ContinuousPaint.message"));
		dragInProgress = false;
	}
	
	/**
	 * Deactivates the tool.
     */
	public void endSession() {
	}
	
	/**
	 * Aborts the current operation.
	 */
	public void resetSession() {
	}
	
	/**
	 * Handles a mouseDown event.
	 * 
	 * @param event the mouse event detail information
	 */
	public final void mouseDown(MouseEvent event) {
		if (event.button != 1) return;
		if (dragInProgress) return; // spurious event
		dragInProgress = true;

		cachedPointArray[0].x = event.x;
		cachedPointArray[0].y = event.y;
		render(cachedPointArray, 1);
	}

	/**
	 * Handles a mouseDoubleClick event.
	 * 
	 * @param event the mouse event detail information
	 */
	public final void mouseDoubleClick(MouseEvent event) {
	}

	/**
	 * Handles a mouseUp event.
	 * 
	 * @param event the mouse event detail information
	 */
	public final void mouseUp(MouseEvent event) {
		if (event.button != 1) return;
		if (! dragInProgress) return; // spurious event
		mouseMove(event);
		dragInProgress = false;
	}
	
	/**
	 * Handles a mouseMove event.
	 * 
	 * @param event the mouse event detail information
	 */
	public final void mouseMove(MouseEvent event) {
		getPaintSurface().showCurrentPositionStatus();

		if (! dragInProgress) return;
		if (cachedPointArray[0].x == -1) return; // spurious event
		cachedPointArray[1].x = event.x;
		cachedPointArray[1].y = event.y;
		renderContinuousSegment();
	}
	
	/**
	 * Draws a continuous segment from cachedPointArray[0] to cachedPointArray[1].
	 * Assumes cachedPointArray[0] has been drawn already.
	 * 
	 * @post cachedPointArray[0] will refer to the same point as cachedPointArray[1]
	 */
	protected void renderContinuousSegment() {
		/* A lazy but effective line drawing algorithm */
		final int dX = cachedPointArray[1].x - cachedPointArray[0].x;
		final int dY = cachedPointArray[1].y - cachedPointArray[0].y;
		int absdX = Math.abs(dX);
		int absdY = Math.abs(dY);

		if ((dX == 0) && (dY == 0)) return;
		
		if (absdY > absdX) {
			final int incfpX = (dX << 16) / absdY;
			final int incY = (dY > 0) ? 1 : -1;
			int fpX = cachedPointArray[0].x << 16; // X in fixedpoint format

			while (--absdY >= 0) {
				cachedPointArray[0].y += incY;
				cachedPointArray[0].x = (fpX += incfpX) >> 16;
				render(cachedPointArray, 1);
			}
			if (cachedPointArray[0].x == cachedPointArray[1].x) return;
			cachedPointArray[0].x = cachedPointArray[1].x;
		} else {
			final int incfpY = (dY << 16) / absdX;
			final int incX = (dX > 0) ? 1 : -1;
			int fpY = cachedPointArray[0].y << 16; // Y in fixedpoint format

			while (--absdX >= 0) {
				cachedPointArray[0].x += incX;
				cachedPointArray[0].y = (fpY += incfpY) >> 16;
				render(cachedPointArray, 1);
			}
			if (cachedPointArray[0].y == cachedPointArray[1].y) return;
			cachedPointArray[0].y = cachedPointArray[1].y;
		}
		render(cachedPointArray, 1);
	}		
}
