/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.paint;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Region;

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
	 * @param foregroundColor the foreground color for this object
	 * @param backgroundColor the background color for this object
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
	@Override
	public void draw(FigureDrawContext fdc) {
		Point p1 = fdc.toClientPoint(x1, y1);
		Point p2 = fdc.toClientPoint(x2, y2);
		fdc.gc.setForeground(foregroundColor);
		fdc.gc.setBackground(backgroundColor);
		fdc.gc.setLineStyle(lineStyle);
		fdc.gc.drawLine(p1.x, p1.y, p2.x, p2.y);
		fdc.gc.setLineStyle(SWT.LINE_SOLID);
	}
	@Override
	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		region.add(fdc.toClientRectangle(x1, y1, x2, y2));
	}
}
