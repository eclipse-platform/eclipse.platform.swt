package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;

/**
 * A rectangle drawing tool.
 */
public class FilledRectangleTool extends DragInteractivePaintSession implements PaintTool {
	private Color drawFGColor;
	private Color drawBGColor;

	/**
	 * Constructs a RectangleTool.
	 * 
	 * @param toolSettings the new tool settings
	 * @param paintSurface the PaintSurface we will render on.
	 */
	public FilledRectangleTool(ToolSettings toolSettings, PaintSurface paintSurface) {
		super(paintSurface);
		set(toolSettings);
	}
	
	/**
	 * Sets the tool's settings.
	 * 
	 * @param toolSettings the new tool settings
	 */
	public void set(ToolSettings toolSettings) {
		drawFGColor = toolSettings.commonForegroundColor;
		drawBGColor = toolSettings.commonBackgroundColor;
	}
	
	/**
	 * Returns name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintPlugin.getResourceString("tool.FilledRectangle.displayname");
	}

	/*
	 * Template methods for drawing
	 */
	protected Meta createMeta(Point a, Point b) {
		MetaContainer container = new MetaContainer();
		container.add(new MetaRectangle(drawFGColor, a.x, a.y, b.x, b.y));
		container.add(new MetaSolidRectangle(drawBGColor,
			Math.min(a.x, b.x) + 1, Math.min(a.y, b.y) + 1,
			Math.max(a.x, b.x) - 1, Math.max(a.y, b.y) - 1));
		return container;
	}
}
