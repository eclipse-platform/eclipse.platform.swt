package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;

/**
 * 2D Rectangle object
 */
public class TextFigure extends StatelessXORFigureHelper {
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
	public TextFigure(Color color, int x1, int y1, int x2, int y2) {
		this.color = color; this.font = font; this.text = text; this.x = x; this.y = y;
	}
	public void draw(GC gc, Point offset) {
		gc.setForeground(color);
		gcDraw(gc, offset);
	}
	protected void gcDraw(GC gc, Point offset) {
		gc.setFont(font);
		gc.drawText(text, x, y, true);
	}			
}
