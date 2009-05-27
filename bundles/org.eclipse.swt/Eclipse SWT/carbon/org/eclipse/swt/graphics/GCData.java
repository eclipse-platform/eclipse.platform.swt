/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.carbon.Rect;

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
	public float[] foreground;
	public float[] background;
	public Pattern foregroundPattern;
	public Pattern backgroundPattern;
	public Font font;
	public int alpha = 0xFF;
	public float[] transform;
	public float[] inverseTransform;
	public float[] clippingTransform;
	public int clipRgn;
	public float lineWidth;
	public int lineStyle = SWT.LINE_SOLID;
	public int lineCap = SWT.CAP_FLAT;
	public int lineJoin = SWT.JOIN_MITER;
	public float lineDashesOffset;
	public float[] lineDashes;
	public float lineMiterLimit = 10;
	public boolean xorMode;
	public int antialias = SWT.DEFAULT;
	public int textAntialias = SWT.DEFAULT;
	public int fillRule = SWT.FILL_EVEN_ODD;

	public float drawXOffset, drawYOffset;
	public int forePattern;
	public int backPattern;
	public Image image;
	public int fontAscent;
	public int fontDescent;
	public int layout;
	public int atsuiStyle;
	public int tabs;
	public String string;
	public int stringLength;
	public int stringWidth = -1;
	public int stringHeight = -1;
	public int drawFlags;
	public int stringPtr;
	public Thread thread;
	public int window;
	public int paintEvent;
	public int visibleRgn;
	public int control;
	public int port;
	public Rect portRect;
	public Rect controlRect;
	public Rect insetRect;
	public boolean updateClip;
}
