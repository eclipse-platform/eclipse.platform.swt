package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;

/**
 * A line drawing tool
 */
public class LineTool extends DragPaintSession implements PaintTool {
	private Color temporaryColor;
	private Color drawColor;

	/**
	 * Constructs a LineTool.
	 * 
	 * @param toolSettings the new tool settings
	 * @param paintSurface the PaintSurface we will render on.
	 */
	public LineTool(ToolSettings toolSettings, PaintSurface paintSurface) {
		super(paintSurface);
		set(toolSettings);
		temporaryColor = new Color(null, 255, 255, 255);
	}
	
	/**
	 * Sets the tool's settings.
	 * 
	 * @param toolSettings the new tool settings
	 */
	public void set(ToolSettings toolSettings) {
		drawColor = toolSettings.commonForegroundColor;
	}
	
	/**
	 * Returns name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintPlugin.getResourceString("tool.Line.label");
	}

	/*
	 * Template methods for drawing
	 */
	protected Figure createFigure(Point a, Point b) {
		return new LineFigure(drawColor, a.x, a.y, b.x, b.y);
	}
}
