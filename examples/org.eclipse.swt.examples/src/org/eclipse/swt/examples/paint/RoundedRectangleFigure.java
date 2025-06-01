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
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;

/**
 * 2D Rectangle object
 */
public class RoundedRectangleFigure extends Figure {
	private Color foregroundColor, backgroundColor;
	private int lineStyle, x1, y1, x2, y2, diameter;
	/**
	 * Constructs a Rectangle
	 * These objects are defined by any two diametrically opposing corners.
	 *
	 * @param foregroundColor the foreground color for this object
	 * @param backgroundColor the background color for this object
	 * @param lineStyle the line style for this object
	 * @param x1 the virtual X coordinate of the first corner
	 * @param y1 the virtual Y coordinate of the first corner
	 * @param x2 the virtual X coordinate of the second corner
	 * @param y2 the virtual Y coordinate of the second corner
	 * @param diameter the diameter of curvature of all four corners
	 */
	public RoundedRectangleFigure(Color foregroundColor, Color backgroundColor, int lineStyle, int x1, int y1, int x2, int y2, int diameter) {
		this.foregroundColor = foregroundColor;
		this.backgroundColor = backgroundColor;
		this.lineStyle = lineStyle;
		this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
		this.diameter = diameter;
	}
	@Override
	public void draw(FigureDrawContext fdc) {
		Rectangle r = fdc.toClientRectangle(x1, y1, x2, y2);
		fdc.gc.setForeground(foregroundColor);
		fdc.gc.setBackground(backgroundColor);
		fdc.gc.setLineStyle(lineStyle);
		fdc.gc.drawRoundRectangle(r.x, r.y, r.width - 1, r.height - 1, diameter, diameter);
		fdc.gc.setLineStyle(SWT.LINE_SOLID);
	}
	@Override
	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		region.add(fdc.toClientRectangle(x1, y1, x2, y2));
	}
}
