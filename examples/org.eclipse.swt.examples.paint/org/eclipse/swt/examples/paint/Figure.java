package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;

/**
 * Superinterface for all drawing objects.
 * All drawing objects know how to render themselved to the screen and can draw a
 * temporary version of themselves for previewing the general appearance of the
 * object onscreen before it gets committed.
 */
public abstract class Figure {
	/**
	 * Draws this object.
	 * 
	 * @param fdc a parameter block specifying drawing-related information
	 */
	public abstract void draw(FigureDrawContext fdc);

	/**
	 * Computes the damaged screen region caused by drawing this object (imprecise), then
	 * appends it to the supplied region.
	 * 
	 * @param fdc a parameter block specifying drawing-related information
	 * @param region a region to which additional damage areas will be added
	 */
	public abstract void addDamagedRegion(FigureDrawContext fdc, Region region);
}
