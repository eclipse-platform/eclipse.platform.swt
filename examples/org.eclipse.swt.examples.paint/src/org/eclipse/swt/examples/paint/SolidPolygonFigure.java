package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;

/**
 * 2D Line object
 */
public class SolidPolygonFigure extends Figure {
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
	public void draw(FigureDrawContext fdc) {
		int[] drawPoints = new int[points.length];
		for (int i = 0; i < points.length; i += 2) {
			drawPoints[i] = points[i] * fdc.xScale - fdc.xOffset;
			drawPoints[i + 1] = points[i + 1] * fdc.yScale - fdc.yOffset;
		}
		fdc.gc.setBackground(color);
		fdc.gc.fillPolygon(drawPoints);
	}
	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		int xmin = Integer.MAX_VALUE, ymin = Integer.MAX_VALUE;
		int xmax = Integer.MIN_VALUE, ymax = Integer.MIN_VALUE;

		for (int i = 0; i < points.length; i += 2) {
			if (points[i] < xmin) xmin = points[i];
			if (points[i] > xmax) xmax = points[i];
			if (points[i+1] < ymin) ymin = points[i+1];
			if (points[i+1] > ymax) ymax = points[i+1];
		}		
		region.add(fdc.toClientRectangle(xmin, ymin, xmax, ymax));
	}
}
