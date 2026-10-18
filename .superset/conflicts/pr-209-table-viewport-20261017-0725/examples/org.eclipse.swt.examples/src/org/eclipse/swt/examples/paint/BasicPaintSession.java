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
	@Override
	public PaintSurface getPaintSurface() {
		return paintSurface;
	}
}
