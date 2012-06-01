/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.*;

/**
 * Instances of this class are descriptions of GCs in terms
 * of unallocated platform-specific data fields.
 * <p>
 * <b>IMPORTANT:</b> This class is <em>not</em> part of the public
 * API for SWT. It is marked public only so that it can be shared
 * within the packages provided by SWT. It is not available on all
 * platforms, and should never be called from application code.
 * </p>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public final class GCData {
	public Device device;
	public int style, state = -1;
	public GdkColor foreground;
	public GdkColor background;
	public Font font;
	public Pattern foregroundPattern;
	public Pattern backgroundPattern;
	public int /*long*/ clipRgn;
	public float lineWidth;
	public int lineStyle = SWT.LINE_SOLID;
	public float[] lineDashes;
	public float lineDashesOffset;
	public float lineMiterLimit = 10;
	public int lineCap = SWT.CAP_FLAT;
	public int lineJoin = SWT.JOIN_MITER;
	public boolean xorMode;
	public int alpha = 0xFF;
	public int interpolation = SWT.DEFAULT;

	public int /*long*/ context;
	public int /*long*/ layout;
	public int /*long*/ damageRgn;
	public Image image;
	public int /*long*/ drawable;	
	public int /*long*/ cairo;
	public double cairoXoffset, cairoYoffset;
	public boolean disposeCairo;
	public double[] identity, clippingTransform;
	public String string;
	public int stringWidth = -1;
	public int stringHeight = -1;
	public int drawFlags;
	public boolean realDrawable;
	public int width = -1, height = -1;
}
