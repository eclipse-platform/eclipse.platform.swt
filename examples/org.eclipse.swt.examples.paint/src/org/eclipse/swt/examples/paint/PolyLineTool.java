package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;

/**
 * A polyline drawing tool.
 */
public class PolyLineTool extends SegmentedPaintSession implements PaintTool {
	private Color drawFGColor;
	private Color drawBGColor;
	private int fillType;

	/**
	 * Constructs a PolyLineTool.
	 * 
	 * @param toolSettings the new tool settings
	 * @param paintSurface the PaintSurface we will render on.
	 */
	public PolyLineTool(ToolSettings toolSettings, PaintSurface paintSurface) {
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
	 * Returns the name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintPlugin.getResourceString("tool.PolyLine.displayname");
	}

	/*
	 * Template methods for drawing
	 */
	protected Figure createFigure(Point[] points, int numPoints, boolean closed) {
		ContainerFigure container = new ContainerFigure();
		if (closed && fillType != ToolSettings.ftNone && numPoints >= 3) {
			container.add(new SolidPolygonFigure(drawBGColor, points, numPoints));
		}
		if (! closed || fillType != ToolSettings.ftSolid || numPoints < 3) {
			for (int i = 0; i < numPoints - 1; ++i) {
				final Point a = points[i];
				final Point b = points[i + 1];
				container.add(new LineFigure(drawFGColor, a.x, a.y, b.x, b.y));
			}
			if (closed) {
				final Point a = points[points.length - 1];
				final Point b = points[0];
				container.add(new LineFigure(drawFGColor, a.x, a.y, b.x, b.y));
			}
		}
		return container;
	}
}
