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
	private ToolSettings settings;

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
		settings = toolSettings;
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
		switch (settings.commonFillType) {
			default:
			case ToolSettings.ftNone:
				return new RectangleFigure(settings.commonForegroundColor, settings.commonLineStyle,
					a.x, a.y, b.x, b.y);
			case ToolSettings.ftSolid:
				return new SolidRectangleFigure(settings.commonBackgroundColor, a.x, a.y, b.x, b.y);
			case ToolSettings.ftOutline: {
				ContainerFigure container = new ContainerFigure();
				container.add(new SolidRectangleFigure(settings.commonBackgroundColor, a.x, a.y, b.x, b.y));
				container.add(new RectangleFigure(settings.commonForegroundColor, settings.commonLineStyle,
					a.x, a.y, b.x, b.y));
				return container;
			}
		}
	}
}
