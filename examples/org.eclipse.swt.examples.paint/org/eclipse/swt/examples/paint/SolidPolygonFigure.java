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
	public void draw(GC gc, Point offset) {
		gc.setBackground(color);
		gc.fillPolygon(getPoints(offset));
	}
	public Object drawPreview(GC gc, Point offset) {
		int[] points = getPoints(offset);
		gc.fillPolygon(points);
		return points;
	}
	public void erasePreview(GC gc, Point offset, Object rememberedData) {
		int[] points = (int[]) rememberedData;
		gc.fillPolygon(points);
	}
	private int[] getPoints(Point offset) {
		int[] shiftedPoints = new int[points.length];
		for (int i = 0; i < points.length; i += 2) {
			shiftedPoints[i] = points[i] + offset.x;
			shiftedPoints[i + 1] = points[i + 1] + offset.y;
		}
		return shiftedPoints;
	}
}
