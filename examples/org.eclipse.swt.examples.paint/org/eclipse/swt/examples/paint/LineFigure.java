package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;

/**
 * 2D Line object
 */
public class LineFigure extends StatelessXORFigureHelper {
	private Color color;
	private int x1, y1, x2, y2;
	/**
	 * Constructs a Line
	 * These objects are defined by their two end-points.
	 * 
	 * @param color the color for this object
	 * @param x1 the virtual X coordinate of the first end-point
	 * @param y1 the virtual Y coordinate of the first end-point
	 * @param x2 the virtual X coordinate of the second end-point
	 * @param y2 the virtual Y coordinate of the second end-point
	 */
	public LineFigure(Color color, int x1, int y1, int x2, int y2) {
		this.color = color; this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}
	public void draw(GC gc, Point offset) {
		gc.setForeground(color);
		gcDraw(gc, offset);
	}
	protected void gcDraw(GC gc, Point offset) {
		gc.drawLine(x1 + offset.x, y1 + offset.y, x2 + offset.x, y2 + offset.y);
	}			
}
