package org.eclipse.swt.examples.paint;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.graphics.Color;
/** * Tool Settings objects group tool-related configuration information. */
public class ToolSettings {
	public static final int ftNone = 0, ftOutline = 1, ftSolid = 2;	/**
	 * Constructs a new ToolSettings.
	 */
	public ToolSettings() {
	}
	
	/**
	 * Disposes of any ToolSettings-related data.
	 */
	public void dispose() {
	}

	/**
	 * commonForegroundColor: current tool foreground colour
	 */
	public Color commonForegroundColor;

	/**
	 * commonBackgroundColor: current tool background colour
	 */
	public Color commonBackgroundColor;
	/**	 * commonFillType: current fill type	 * <p>	 * One of ftNone, ftOutline, ftSolid.	 * </p>	 */	public int commonFillType = ftNone;	
	/** Airbrush settings **/

	/**
	 * airbrushRadius: coverage radius in pixels
	 */
	public int airbrushRadius = 10;
	
	/**
	 * airbrushIntensity: average surface area coverage in region defined by radius per "jot"
	 */
	public int airbrushIntensity = 30;		/** RoundedRectangle settings **/		/**	 * roundedRectangleCornerDiameter: the diameter of curvature of corners in a rounded rectangle	 */	public int roundedRectangleCornerDiameter = 16;
}
