package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.util.Random;import org.eclipse.swt.graphics.*;

/**
 * An airbrush tool.
 */
public class AirbrushTool extends ContinuousPaintSession implements PaintTool {
	private Random random;
	private int airbrushRadius;
	private int cachedRadiusSquared;
	private int cachedNumPoints;
	private Color airbrushColor;
	
	/**
	 * Constructs a Tool.
	 * 
	 * @param toolSettings the new tool settings
	 * @param paintSurface the PaintSurface we will render on.
	 */
	public AirbrushTool(ToolSettings toolSettings, PaintSurface paintSurface) {
		super(paintSurface);
		random = new Random();
		set(toolSettings);
	}
	
	/**
	 * Sets the tool's settings.
	 * 
	 * @param toolSettings the new tool settings
	 */
	public void set(ToolSettings toolSettings) {
		airbrushRadius = toolSettings.airbrushRadius;
		airbrushColor = toolSettings.commonForegroundColor;

		// compute things we need to know for drawing
		cachedRadiusSquared = toolSettings.airbrushRadius * toolSettings.airbrushRadius;
		cachedNumPoints = 314 * cachedRadiusSquared / 10000;
	}

	/**
	 * Draws a bunch (cachedNumPoints) of random pixels within a specified
	 * circle (cachedRadiusSquared).
	 * 
	 * @param points[0] the target point
 	 * @param numPoints the number of valid points in the array (must be 1)
	 */
	public void render(final Point[] points, int numPoints) {
		Assert.assert(numPoints == 1);
		final PaintSurface ps = getPaintSurface();
		final GC    igc  = ps.getImageGC();
		final Point ioff = ps.getImageOffset();
		final int x = points[0].x + ioff.x, y = points[0].y + ioff.y;
		
		igc.setBackground(airbrushColor);
		for (int i = 0; i < cachedNumPoints; ++i) {
			int randX, randY;
			do {
				randX = (int) ((random.nextDouble() - 0.5) * airbrushRadius * 2.0);
				randY = (int) ((random.nextDouble() - 0.5) * airbrushRadius * 2.0);
			} while (randX * randX + randY * randY > cachedRadiusSquared);
			
			igc.fillRectangle(x + randX, y + randY, 1, 1);
		}
		ps.redrawArea(points[0].x - airbrushRadius, points[0].y - airbrushRadius,
			airbrushRadius * 2, airbrushRadius * 2);
	}
	
	/**
	 * Returns the name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintPlugin.getResourceString("tool.Airbrush.displayname");
	}
}
