package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.util.Random;import org.eclipse.swt.graphics.*;

/**
 * A pencil tool.
 */
public class PencilTool extends ContinuousPaintSession implements PaintTool {
	private Color drawColor;
	
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
	public void set(ToolSettings toolSettings) {
		drawColor = toolSettings.commonForegroundColor;		
	}

	/**
	 * Returns the name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintPlugin.getResourceString("tool.Pencil.label");
	}

	/*
	 * Template method for drawing
	 */
	public void render(final Point point) {
		final PaintSurface ps = getPaintSurface();
		final GC    igc  = ps.getImageGC();
		final Point ioff = ps.getImageOffset();
		final GC    dgc  = ps.getDisplayGC();
		final Point doff = ps.getDisplayOffset();
		igc.setBackground(drawColor);
		igc.fillRectangle(point.x + ioff.x, point.y + ioff.y, 1, 1);  
		dgc.setBackground(drawColor);
		dgc.fillRectangle(point.x + doff.x, point.y + doff.y, 1, 1);  
	}
}
