package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2002.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public abstract class BasicPaintSession implements PaintSession {
	/**	 * The paint surface	 */	private PaintSurface paintSurface;	/**	 * Constructs a PaintSession.	 * 	 * @param paintSurface the drawing surface to use	 */	protected BasicPaintSession(PaintSurface paintSurface) {		this.paintSurface = paintSurface;	}	/**
	 * Returns the paint surface associated with this paint session.	 * 	 * @return the associated PaintSurface	 */	public PaintSurface getPaintSurface() {		return paintSurface;	}}
