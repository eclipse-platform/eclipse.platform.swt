/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
import org.eclipse.swt.widgets.*;

/**
 * The superclass for paint tools that draw continuously along the path
 * traced by the mouse's movement while the button is depressed
 */
public abstract class ContinuousPaintSession extends BasicPaintSession {
	/**
	 * True if a click-drag is in progress.
	 */
	private boolean dragInProgress = false;
	
	/**
	 * A cached Point array for drawing.
	 */
	private Point[] points = new Point[] { new Point(-1, -1), new Point(-1, -1) };

	/**
	 * The time to wait between retriggers in milliseconds.
	 */
	private int retriggerInterval = 0;
	
	/**
	 * The currently valid RetriggerHandler
	 */
	protected Runnable retriggerHandler = null;

	/**
	 * Constructs a ContinuousPaintSession.
	 * 
	 * @param paintSurface the drawing surface to use
	 */
	protected ContinuousPaintSession(PaintSurface paintSurface) {
		super(paintSurface);
	}

	/**
	 * Sets the retrigger timer.
	 * <p>
	 * After the timer elapses, if the mouse is still hovering over the same point with the
	 * drag button pressed, a new render order is issued and the timer is restarted.
	 * </p>
	 * @param interval the time in milliseconds to wait between retriggers, 0 to disable
	 */
	public void setRetriggerTimer(int interval) {
		retriggerInterval = interval;
	}

	/**
	 * Activates the tool.
	 */
	public void beginSession() {
		getPaintSurface().
			setStatusMessage(PaintExample.getResourceString("session.ContinuousPaint.message"));
		dragInProgress = false;
	}
	
	/**
	 * Deactivates the tool.
     */
	public void endSession() {
		abortRetrigger();
	}
	
	/**
	 * Aborts the current operation.
	 */
	public void resetSession() {
		abortRetrigger();
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

		points[0].x = event.x;
		points[0].y = event.y;
		render(points[0]);
		prepareRetrigger();
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
		abortRetrigger();
		mouseSegmentFinished(event);
		dragInProgress = false;
	}
	
	/**
	 * Handles a mouseMove event.
	 * 
	 * @param event the mouse event detail information
	 */
	public final void mouseMove(MouseEvent event) {
		final PaintSurface ps = getPaintSurface();
		ps.setStatusCoord(ps.getCurrentPosition());
		if (! dragInProgress) return;
		mouseSegmentFinished(event);
		prepareRetrigger();
	}
	
	/**
	 * Handle a rendering segment
	 * 
	 * @param event the mouse event detail information
	 */
	private final void mouseSegmentFinished(MouseEvent event) {
		if (points[0].x == -1) return; // spurious event
		if (points[0].x != event.x || points[0].y != event.y) {
			// draw new segment
			points[1].x = event.x;
			points[1].y = event.y;
			renderContinuousSegment();
		}
	}

	/**
	 * Draws a continuous segment from points[0] to points[1].
	 * Assumes points[0] has been drawn already.
	 * 
	 * @post points[0] will refer to the same point as points[1]
	 */
	protected void renderContinuousSegment() {
		/* A lazy but effective line drawing algorithm */
		final int dX = points[1].x - points[0].x;
		final int dY = points[1].y - points[0].y;
		int absdX = Math.abs(dX);
		int absdY = Math.abs(dY);

		if ((dX == 0) && (dY == 0)) return;
		
		if (absdY > absdX) {
			final int incfpX = (dX << 16) / absdY;
			final int incY = (dY > 0) ? 1 : -1;
			int fpX = points[0].x << 16; // X in fixedpoint format

			while (--absdY >= 0) {
				points[0].y += incY;
				points[0].x = (fpX += incfpX) >> 16;
				render(points[0]);
			}
			if (points[0].x == points[1].x) return;
			points[0].x = points[1].x;
		} else {
			final int incfpY = (dY << 16) / absdX;
			final int incX = (dX > 0) ? 1 : -1;
			int fpY = points[0].y << 16; // Y in fixedpoint format

			while (--absdX >= 0) {
				points[0].x += incX;
				points[0].y = (fpY += incfpY) >> 16;
				render(points[0]);
			}
			if (points[0].y == points[1].y) return;
			points[0].y = points[1].y;
		}
		render(points[0]);
	}		

	/**
	 * Prepare the retrigger timer
	 */
	private final void prepareRetrigger() {
		if (retriggerInterval > 0) {
			/*
			 * timerExec() provides a lightweight mechanism for running code at intervals from within
			 * the event loop when timing accuracy is not important.
			 *
			 * Since it is not possible to cancel a timerExec(), we remember the Runnable that is
			 * active in order to distinguish the valid one from the stale ones.  In practice,
			 * if the interval is 1/100th of a second, then creating a few hundred new RetriggerHandlers
			 * each second will not cause a significant performance hit.
			 */
			Display display = getPaintSurface().getDisplay();
			retriggerHandler = new Runnable() {
				public void run() {
					if (retriggerHandler == this) {
						render(points[0]);
						prepareRetrigger();
					}
				}
			};			
			display.timerExec(retriggerInterval, retriggerHandler);
		}
	}

	/**
	 * Aborts the retrigger timer
	 */
	private final void abortRetrigger() {
		retriggerHandler = null;
	}
	
	/**
	 * Template method: Renders a point.
	 * @param point, the point to render
	 */
	protected abstract void render(Point point);
}
