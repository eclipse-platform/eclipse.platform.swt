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
		return PaintExample.getResourceString("tool.Rectangle.label");
	}

	/*
	 * Template method for drawing
	 */
	protected Figure createFigure(Point a, Point b) {
		switch (settings.commonFillType) {
			default:
			case ToolSettings.ftNone:
				return new RectangleFigure(settings.commonForegroundColor, settings.commonBackgroundColor, settings.commonLineStyle,
					a.x, a.y, b.x, b.y);
			case ToolSettings.ftSolid:
				return new SolidRectangleFigure(settings.commonBackgroundColor, a.x, a.y, b.x, b.y);
			case ToolSettings.ftOutline: {
				ContainerFigure container = new ContainerFigure();
				container.add(new SolidRectangleFigure(settings.commonBackgroundColor, a.x, a.y, b.x, b.y));
				container.add(new RectangleFigure(settings.commonForegroundColor, settings.commonBackgroundColor, settings.commonLineStyle,
					a.x, a.y, b.x, b.y));
				return container;
			}
		}
	}
}
