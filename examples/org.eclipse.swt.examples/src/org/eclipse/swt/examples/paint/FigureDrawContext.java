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

public class FigureDrawContext {
	/*
	 * <p>
	 * The GC must be set up as follows
	 * (it will be returned to this state upon completion of drawing operations)
	 * <ul>
	 *   <li>setXORMode(false)
	 * </ul>
	 * </p>
	 */
	public GC gc = null;
	public int xOffset = 0, yOffset = 0; // substract to get GC coords
	public int xScale = 1, yScale = 1;
	
	public Rectangle toClientRectangle(int x1, int y1, int x2, int y2) {
		return new Rectangle(
			Math.min(x1, x2) * xScale - xOffset,
			Math.min(y1, y2) * yScale - yOffset,
			(Math.abs(x2 - x1) + 1) * xScale,
			(Math.abs(y2 - y1) + 1) * yScale);
	}
	public Point toClientPoint(int x, int y) {
		return new Point(x * xScale - xOffset, y * yScale - yOffset);
	}
}
