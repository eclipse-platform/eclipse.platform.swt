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
 * 2D Rectangle object
 */
public class TextFigure extends Figure {
	private Color  color;
	private Font   font;
	private String text;
	private int x, y;
	/**
	 * Constructs a TextFigure
	 *
	 * @param color the color for this object
	 * @param font  the font for this object
	 * @param text  the text to draw, tab and new-line expansion is performed
	 * @param x     the virtual X coordinate of the top-left corner of the text bounding box
	 * @param y     the virtual Y coordinate of the top-left corner of the text bounding box
	 */
	public TextFigure(Color color, Font font, String text, int x, int y) {
		this.color = color; this.font = font; this.text = text; this.x = x; this.y = y;
	}
	@Override
	public void draw(FigureDrawContext fdc) {
		Point p = fdc.toClientPoint(x, y);
		fdc.gc.setFont(font);
		fdc.gc.setForeground(color);
		fdc.gc.drawText(text, p.x, p.y, true);
	}
	@Override
	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		Font oldFont = fdc.gc.getFont();
		fdc.gc.setFont(font);
		Point textExtent = fdc.gc.textExtent(text);
		fdc.gc.setFont(oldFont);
		region.add(fdc.toClientRectangle(x, y, x + textExtent.x, y + textExtent.y));
	}
}
