package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

public abstract class BasicPaintSession implements PaintSession {
	/**	 * The paint surface	 */	private PaintSurface paintSurface;	/**	 * Constructs a PaintSession.	 * 	 * @param paintSurface the drawing surface to use	 */	protected BasicPaintSession(PaintSurface paintSurface) {		this.paintSurface = paintSurface;	}	/**
	 * Returns the paint surface associated with this paint session.	 * 	 * @return the associated PaintSurface	 */	public PaintSurface getPaintSurface() {		return paintSurface;	}}
