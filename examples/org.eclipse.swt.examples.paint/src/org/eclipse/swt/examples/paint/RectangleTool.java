package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;

/**
 * A drawing tool.
 */
public class RectangleTool extends DragPaintSession implements PaintTool {
	private Color drawFGColor;
	private Color drawBGColor;
	private int   fillType;

	/**
	 * Constructs a RectangleTool.
	 * 
	 * @param toolSettings the new tool settings
	 * @param paintSurface the PaintSurface we will render on.
	 */
	public RectangleTool(ToolSettings toolSettings, PaintSurface paintSurface) {
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
		fillType = toolSettings.commonFillType;
	}
	
	/**
	 * Returns name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintPlugin.getResourceString("tool.Rectangle.label");
	}

	/*
	 * Template method for drawing
	 */
	protected Figure createFigure(Point a, Point b) {
		switch (fillType) {
			default:
			case ToolSettings.ftNone:
				return new RectangleFigure(drawFGColor, a.x, a.y, b.x, b.y);
			case ToolSettings.ftSolid:
				return new SolidRectangleFigure(drawBGColor, a.x, a.y, b.x, b.y);
			case ToolSettings.ftOutline: {
				ContainerFigure container = new ContainerFigure();
				container.add(new RectangleFigure(drawFGColor, a.x, a.y, b.x, b.y));
				container.add(new SolidRectangleFigure(drawBGColor,
					Math.min(a.x, b.x) + 1, Math.min(a.y, b.y) + 1,
					Math.max(a.x, b.x) - 1, Math.max(a.y, b.y) - 1));
				return container;
			}
		}
	}
}
