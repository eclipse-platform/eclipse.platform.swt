package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;

/**
 * 2D Solid Ellipse object
 */
public class SolidEllipseFigure extends Figure {
	private Color color;
	private int x1, y1, x2, y2;
	/**
	 * Constructs a SolidEllipse
	 * These objects are defined by any two diametrically opposing corners of a box
	 * bounding the ellipse.
	 * 
	 * @param color the color for this object
	 * @param x1 the virtual X coordinate of the first corner
	 * @param y1 the virtual Y coordinate of the first corner
	 * @param x2 the virtual X coordinate of the second corner
	 * @param y2 the virtual Y coordinate of the second corner
	 */
	public SolidEllipseFigure(Color color, int x1, int y1, int x2, int y2) {
		this.color = color; this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}
	public void draw(FigureDrawContext fdc) {
		Rectangle r = fdc.toClientRectangle(x1, y1, x2, y2);
		fdc.gc.setBackground(color);
		fdc.gc.fillOval(r.x, r.y, r.width, r.height);
	}
	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		region.add(fdc.toClientRectangle(x1, y1, x2, y2));
	}
}
