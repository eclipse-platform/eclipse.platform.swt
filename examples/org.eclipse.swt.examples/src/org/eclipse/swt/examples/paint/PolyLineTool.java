/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.paint;


import org.eclipse.swt.graphics.*;

/**
 * A polyline drawing tool.
 */
public class PolyLineTool extends SegmentedPaintSession implements PaintTool {
	private ToolSettings settings;

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
		settings = toolSettings;
	}

	/**
	 * Returns the name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintExample.getResourceString("tool.PolyLine.label");
	}

	/*
	 * Template methods for drawing
	 */
	protected Figure createFigure(Point[] points, int numPoints, boolean closed) {
		ContainerFigure container = new ContainerFigure();
		if (closed && settings.commonFillType != ToolSettings.ftNone && numPoints >= 3) {
			container.add(new SolidPolygonFigure(settings.commonBackgroundColor, points, numPoints));
		}
		if (! closed || settings.commonFillType != ToolSettings.ftSolid || numPoints < 3) {
			for (int i = 0; i < numPoints - 1; ++i) {
				final Point a = points[i];
				final Point b = points[i + 1];
				container.add(new LineFigure(settings.commonForegroundColor, settings.commonBackgroundColor, settings.commonLineStyle,
					a.x, a.y, b.x, b.y));
			}
			if (closed) {
				final Point a = points[points.length - 1];
				final Point b = points[0];
				container.add(new LineFigure(settings.commonForegroundColor, settings.commonBackgroundColor, settings.commonLineStyle,
					a.x, a.y, b.x, b.y));
			}
		}
		return container;
	}
}
