/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.motif;

 
public class XGCValues {
	public int function;		/* logical operation */
	public int plane_mask;/* plane mask */
	public int foreground;/* foreground pixel */
	public int background;/* background pixel */
	public int line_width;		/* line width */
	public int line_style;	 	/* LineSolid, LineOnOffDash, LineDoubleDash */
	public int cap_style;	  	/* CapNotLast, CapButt, CapRound, CapProjecting */
	public int join_style;	 	/* JoinMiter, JoinRound, JoinBevel */
	public int fill_style;	 	/* FillSolid, FillTiled, FillStippled, FillOpaeueStippled */
	public int fill_rule;	  	/* EvenOddRule, WindingRule */
	public int arc_mode;		/* ArcChord, ArcPieSlice */
	public int tile;		/* tile pixmap for tiling operations */
	public int stipple;		/* stipple 1 plane pixmap for stipping */
	public int ts_x_origin;	/* offset for tile or stipple operations */
	public int ts_y_origin;
	public int font;	     /* default text font for text operations */
	public int subwindow_mode;     /* ClipByChildren, IncludeInferiors */
	public int graphics_exposures;/* boolean, should exposures be generated */
	public int clip_x_origin;	/* origin for clipping */
	public int clip_y_origin;
	public int clip_mask;	/* bitmap clipping; other calls for rects */
	public int dash_offset;	/* patterned/dashed line information */
	public byte dashes;
	public static final int sizeof = 92;
}
