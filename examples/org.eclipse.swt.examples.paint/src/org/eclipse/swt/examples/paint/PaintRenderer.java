package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;

public interface PaintRenderer {
	/**
	 * Renders an object on a given point/region.
	 * 
	 * @param points the array of points
	 * @param numPoints the number of valid points in the array
	 */
	public void render(Point[] points, int numPoints);
}
