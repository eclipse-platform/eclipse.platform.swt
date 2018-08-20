/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.paint;


import org.eclipse.swt.graphics.Point;

/**
 * A pencil tool.
 */
public class PencilTool extends ContinuousPaintSession implements PaintTool {
	private ToolSettings settings;

	/**
	 * Constructs a pencil tool.
	 *
	 * @param toolSettings the new tool settings
	 * @param getPaintSurface() the PaintSurface we will render on.
	 */
	public PencilTool(ToolSettings toolSettings, PaintSurface paintSurface) {
		super(paintSurface);
		set(toolSettings);
	}

	/**
	 * Sets the tool's settings.
	 *
	 * @param toolSettings the new tool settings
	 */
	@Override
	public void set(ToolSettings toolSettings) {
		settings = toolSettings;
	}

	/**
	 * Returns the name associated with this tool.
	 *
	 * @return the localized name of this tool
	 */
	@Override
	public String getDisplayName() {
		return PaintExample.getResourceString("tool.Pencil.label");
	}

	/*
	 * Template method for drawing
	 */
	@Override
	public void render(final Point point) {
		final PaintSurface ps = getPaintSurface();
		ps.drawFigure(new PointFigure(settings.commonForegroundColor, point.x, point.y));
	}
}
