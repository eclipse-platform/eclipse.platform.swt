package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;

/**
 * The superclass for single-click paint tools.
 */
public abstract class SimplePaintSession extends BasicPaintSession implements PaintRenderer {
	/**
	 * A cached Point array
	 */
	protected Point[] cachedPointArray = new Point[] { new Point(-1, -1) };

	/**
	 * Constructs a SimplePaintSession.
	 * 
	 * @param paintSurface the drawing surface to use
	 */
	protected SimplePaintSession(PaintSurface paintSurface) {
		super(paintSurface);
	}

	/**
	 * Activates the tool.
	 */
	public void beginSession() {
		getPaintSurface().getPaintStatus().setMessage(PaintPlugin.getResourceString(
			"session.SimplePaint.message"));
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
	public void mouseDown(MouseEvent event) {
		if (event.button != 1) return; // catch only left mouse actions
		
		cachedPointArray[0].x = event.x;
		cachedPointArray[0].y = event.y;
		render(cachedPointArray, 1);
	}

	/**
	 * Handles a mouseDoubleClick event.
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseDoubleClick(MouseEvent event) {
		mouseDown(event);
	}

	/**
	 * Handles a mouseUp event.
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseUp(MouseEvent event) {
	}
	
	/**
	 * Handles a mouseMove event.
	 * 
	 * @param event the mouse event detail information
	 */
	public void mouseMove(MouseEvent event) {
		getPaintSurface().showCurrentPositionStatus();
	}
}
