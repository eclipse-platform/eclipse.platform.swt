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


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * 2D Line object
 */
public class LineFigure extends Figure {
	private Color foregroundColor, backgroundColor;
	private int lineStyle, x1, y1, x2, y2;
	/**
	 * Constructs a Line
	 * These objects are defined by their two end-points.
	 * 
	 * @param color the color for this object
	 * @param lineStyle the line style for this object
	 * @param x1 the virtual X coordinate of the first end-point
	 * @param y1 the virtual Y coordinate of the first end-point
	 * @param x2 the virtual X coordinate of the second end-point
	 * @param y2 the virtual Y coordinate of the second end-point
	 */
	public LineFigure(Color foregroundColor, Color backgroundColor, int lineStyle, int x1, int y1, int x2, int y2) {
		this.foregroundColor = foregroundColor;
		this.backgroundColor = backgroundColor;
		this.lineStyle = lineStyle;
		this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}
	public void draw(FigureDrawContext fdc) {
		Point p1 = fdc.toClientPoint(x1, y1);
		Point p2 = fdc.toClientPoint(x2, y2);
		fdc.gc.setForeground(foregroundColor);
		fdc.gc.setBackground(backgroundColor);
		fdc.gc.setLineStyle(lineStyle);
		fdc.gc.drawLine(p1.x, p1.y, p2.x, p2.y);
		fdc.gc.setLineStyle(SWT.LINE_SOLID);
	}
	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		region.add(fdc.toClientRectangle(x1, y1, x2, y2));
	}
}
