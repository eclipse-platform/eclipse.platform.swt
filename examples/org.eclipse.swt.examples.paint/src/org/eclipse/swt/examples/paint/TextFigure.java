package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

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
	public void draw(FigureDrawContext fdc) {
		Point p = fdc.toClientPoint(x, y);
		fdc.gc.setFont(font);
		fdc.gc.setForeground(color);
		fdc.gc.drawText(text, p.x, p.y, true);
	}
	public void addDamagedRegion(FigureDrawContext fdc, Region region) {
		Font oldFont = fdc.gc.getFont();
		fdc.gc.setFont(font);
		Point textExtent = fdc.gc.textExtent(text);
		fdc.gc.setFont(oldFont);
		region.add(fdc.toClientRectangle(x, y, x + textExtent.x, y + textExtent.y));
	}
}
