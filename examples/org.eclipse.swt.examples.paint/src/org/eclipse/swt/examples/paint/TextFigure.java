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
	public void draw(GC gc, Point offset) {
		gc.setFont(font);
		gc.setForeground(color);
		gc.drawText(text, x + offset.x, y + offset.y, true);
	}
	public Object drawPreview(GC gc, Point offset) {
		Color oldColor = gc.getForeground();
		gc.setFont(font);
		gc.setForeground(color);
		gc.setXORMode(false);

		Point textExtent = gc.textExtent(text);
		Image backingStore = new Image(null, textExtent.x, textExtent.y);
		gc.copyArea(backingStore, x + offset.x, y + offset.y);	

		gc.drawText(text, x + offset.x, y + offset.y, true);
		gc.setForeground(oldColor);
		gc.setXORMode(true);
		return backingStore;
	}
	public void erasePreview(GC gc, Point offset, Object rememberedData) {
		Image backingStore = (Image) rememberedData;
		gc.setXORMode(false);
		gc.drawImage(backingStore, x + offset.x, y + offset.y);
		gc.setXORMode(true);
		backingStore.dispose();
	}
}
