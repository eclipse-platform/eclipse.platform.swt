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


public abstract class BasicPaintSession implements PaintSession {
	/**
	 * The paint surface
	 */
	private PaintSurface paintSurface;

	/**
	 * Constructs a PaintSession.
	 * 
	 * @param paintSurface the drawing surface to use
	 */
	protected BasicPaintSession(PaintSurface paintSurface) {
		this.paintSurface = paintSurface;
	}

	/**
	 * Returns the paint surface associated with this paint session.
	 * 
	 * @return the associated PaintSurface
	 */
	public PaintSurface getPaintSurface() {
		return paintSurface;
	}
}
