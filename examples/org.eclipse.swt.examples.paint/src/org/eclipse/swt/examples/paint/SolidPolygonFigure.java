package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;

/**
 * 2D Line object
 */
public class SolidPolygonFigure extends StatelessXORFigureHelper {
	private Color color;
	private int[] points;
	/**
	 * Constructs a SolidPolygon
	 * These objects are defined by a sequence of vertices.
	 * 
	 * @param color the color for this object
	 * @param vertices the array of vertices making up the polygon
	 * @param numPoint the number of valid points in the array (n >= 3)
	 */
	public SolidPolygonFigure(Color color, Point[] vertices, int numPoints) {
		this.color = color;
		this.points = new int[numPoints * 2];
		for (int i = 0; i < numPoints; ++i) {
			points[i * 2] = vertices[i].x;
			points[i * 2 + 1] = vertices[i].y;
		}
	}
	public void draw(GC gc, Point offset) {
		gc.setBackground(color);
		gcDraw(gc, offset);
	}
	protected void gcDraw(GC gc, Point offset) {
		gc.fillPolygon(points);
	}			
}
