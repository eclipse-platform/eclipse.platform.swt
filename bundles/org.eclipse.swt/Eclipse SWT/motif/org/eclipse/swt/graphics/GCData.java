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
package org.eclipse.swt.graphics;


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
 */
public final class GCData {
	public Device device;
	public int style;
	public Image image;
	public int display;
	public int drawable;
	public int foreground = -1;
	public int background = -1;
	public Image backgroundImage;
	public Pattern foregroundPattern;
	public Pattern backgroundPattern;
	public Font font;
	public int colormap;
	public int clipRgn, damageRgn;
	public int lineStyle = SWT.LINE_SOLID;
	public int lineWidth;
	public int[] dashes;
	public int renderTable;
	public int alpha = 0xFF;
	public int interpolation = SWT.DEFAULT;

	public int /*long*/ cairo;

	public String string;
	public int stringWidth = -1;
	public int stringHeight = -1;
	public int xmString;

	public String text;
	public int textWidth = -1;
	public int textHeight = -1;
	public int xmText, xmMnemonic;
	public int drawFlags;
}
