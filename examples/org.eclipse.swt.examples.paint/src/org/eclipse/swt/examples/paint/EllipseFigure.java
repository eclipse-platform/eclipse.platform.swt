package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;

/**
 * 2D Ellipse object
 */
public class EllipseFigure extends StatelessXORFigureHelper {
	private Color color;
	private int x1, y1, x2, y2;
	/**
	 * Constructs an Ellipse
	 * These objects are defined by any two diametrically opposing corners of a box
	 * bounding the ellipse.
	 * 
	 * @param color the color for this object
	 * @param x1 the virtual X coordinate of the first corner
	 * @param y1 the virtual Y coordinate of the first corner
	 * @param x2 the virtual X coordinate of the second corner
	 * @param y2 the virtual Y coordinate of the second corner
	 */
	public EllipseFigure(Color color, int x1, int y1, int x2, int y2) {
		this.color = color; this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}
	public void draw(GC gc, Point offset) {
		gc.setForeground(color);
		gcDraw(gc, offset);
	}
	protected void gcDraw(GC gc, Point offset) {
		gc.drawOval(Math.min(x1, x2) + offset.x, Math.min(y1, y2) + offset.y,
			Math.abs(x2 - x1), Math.abs(y2 - y1));
	}			
}
