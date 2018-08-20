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


import org.eclipse.swt.graphics.*;

/**
 * 2D Point object
 */
public class PointFigure extends Figure {
	private Color color;
	private int x, y;
	/**
	 * Constructs a Point
	 *
	 * @param color the color for this object
	 * @param x the virtual X coordinate of the first end-point
	 * @param y the virtual Y coordinate of the first end-point
	 */
	public PointFigure(Color color, int x, int y) {
		this.color = color; this.x = x; this.y = y;
	}
	@Override
	public void draw(FigureDrawContext fdc) {
		Point p = fdc.toClientPoint(x, y);
		fdc.gc.setBackground(color);
		fdc.gc.fillRectangle(p.x, p.y, 1, 1);
	}
	@Override
	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		region.add(fdc.toClientRectangle(x, y, x, y));
	}
}
