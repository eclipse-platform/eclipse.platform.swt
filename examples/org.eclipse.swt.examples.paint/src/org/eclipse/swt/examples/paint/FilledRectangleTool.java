package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;

/**
 * A rectangle drawing tool
 */
public class FilledRectangleTool extends DragInteractivePaintSession implements PaintTool {
	private Color temporaryFGColor;
	private Color temporaryBGColor;
	private Color drawFGColor;
	private Color drawBGColor;

	/**
	 * Create a RectangleTool.
	 * 
	 * @param toolSettings the new tool settings
	 * @param paintSurface the PaintSurface we will render on.
	 */
	public FilledRectangleTool(ToolSettings toolSettings, PaintSurface paintSurface) {
		super(paintSurface);
		set(toolSettings);
		temporaryFGColor = new Color(null, 255, 255, 255);
		temporaryBGColor = new Color(null, 127, 127, 127);
	}
	
	/**
	 * Set the tool's settings
	 * 
	 * @param toolSettings the new tool settings
	 */
	public void set(ToolSettings toolSettings) {
		drawFGColor = toolSettings.commonForegroundColor;
		drawBGColor = toolSettings.commonBackgroundColor;
	}
	
	/**
	 * Get name associated with this tool
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintPlugin.getResourceString("tool.FilledRectangle.displayname");
	}

	/*
	 * Template methods for drawing
	 */
	protected void drawPermanent(GC gc, Point a, Point b) {
		gc.setForeground(drawFGColor);
		gc.setBackground(drawBGColor);
		fillRectangle(gc, a, b);
	}		

	protected void drawTemporary(GC gc, Point a, Point b) {
		gc.setForeground(temporaryFGColor);
		gc.setBackground(temporaryBGColor);
		gc.setXORMode(true);
		fillRectangle(gc, a, b);
		gc.setXORMode(false);
	}		

	protected void eraseTemporary(GC gc, Point a, Point b) {
		drawTemporary(gc, a, b);
	}

	/**
	 * Draw a filled rectangle
	 */
	protected void fillRectangle(GC gc, Point a, Point b) {
		final int left = Math.min(a.x, b.x);
		final int top = Math.min(a.y, b.y);
		final int width = Math.abs(a.x - b.x) + 1;
		final int height = Math.abs(a.y - b.y) + 1;
		
		gc.drawRectangle(left, top, width, height);
		if ((width > 2) && (height > 2))
			gc.fillRectangle(left + 1, top + 1,
				width - 1/*- 2*/, height - 1/*- 2*/); // BUG in GC
	}
}
