package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * 2D Ellipse object
 */
public class EllipseFigure extends Figure {
	private Color color;
	private int lineStyle, x1, y1, x2, y2;
	/**
	 * Constructs an Ellipse
	 * These objects are defined by any two diametrically opposing corners of a box
	 * bounding the ellipse.
	 * 
	 * @param color the color for this object
	 * @param lineStyle the line style for this object
	 * @param x1 the virtual X coordinate of the first corner
	 * @param y1 the virtual Y coordinate of the first corner
	 * @param x2 the virtual X coordinate of the second corner
	 * @param y2 the virtual Y coordinate of the second corner
	 */
	public EllipseFigure(Color color, int lineStyle, int x1, int y1, int x2, int y2) {
		this.color = color; this.lineStyle = lineStyle;
		this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}
	public void draw(FigureDrawContext fdc) {
		Rectangle r = fdc.toClientRectangle(x1, y1, x2, y2);
		fdc.gc.setForeground(color);
		fdc.gc.setLineStyle(lineStyle);
		fdc.gc.drawOval(r.x, r.y, r.width - 1, r.height - 1);
		fdc.gc.setLineStyle(SWT.LINE_SOLID);
	}
	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		region.add(fdc.toClientRectangle(x1, y1, x2, y2));
	}
}
