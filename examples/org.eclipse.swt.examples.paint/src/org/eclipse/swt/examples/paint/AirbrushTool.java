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
	private ToolSettings settings;
	private Random random;
	private int cachedRadiusSquared;
	private int cachedNumPoints;
	
	/**
	 * Constructs a Tool.
	 * 
	 * @param toolSettings the new tool settings
	 * @param paintSurface the PaintSurface we will render on.
	 */
	public AirbrushTool(ToolSettings toolSettings, PaintSurface paintSurface) {
		super(paintSurface);
		random = new Random();
		setRetriggerTimer(10);
		set(toolSettings);
	}
	
	/**
	 * Sets the tool's settings.
	 * 
	 * @param toolSettings the new tool settings
	 */
	public void set(ToolSettings toolSettings) {
		// compute things we need to know for drawing
		settings = toolSettings;
		cachedRadiusSquared = settings.airbrushRadius * settings.airbrushRadius;
		cachedNumPoints = 314 * settings.airbrushIntensity * cachedRadiusSquared / 250000;
		if (cachedNumPoints == 0 && settings.airbrushIntensity != 0)
			cachedNumPoints = 1;
	}

	/**
	 * Returns the name associated with this tool.
	 * 
	 * @return the localized name of this tool
	 */
	public String getDisplayName() {
		return PaintPlugin.getResourceString("tool.Airbrush.label");
	}

	/*
	 * Template method for drawing
	 */
	protected void render(Point point) {
		// Draws a bunch (cachedNumPoints) of random pixels within a specified circle (cachedRadiusSquared).
		ContainerFigure cfig = new ContainerFigure();

		for (int i = 0; i < cachedNumPoints; ++i) {
			int randX, randY;
			do {
				randX = (int) ((random.nextDouble() - 0.5) * settings.airbrushRadius * 2.0);
				randY = (int) ((random.nextDouble() - 0.5) * settings.airbrushRadius * 2.0);
			} while (randX * randX + randY * randY > cachedRadiusSquared);
			cfig.add(new PointFigure(settings.commonForegroundColor, point.x + randX, point.y + randY));
		}
		getPaintSurface().drawFigure(cfig);
	}
}
