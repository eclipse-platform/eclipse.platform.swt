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

 
import java.awt.Component;

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
	public java.awt.Color foreground = java.awt.Color.BLACK;
	public java.awt.Color background = java.awt.Color.BLACK;
	public int hPen;
	public int lineWidth;
	public float[] dashes;
	public int hBrush;
	public java.awt.Font hFont;
	public int hNullBitmap;
	public Component hwnd;
	public boolean layout = true;
	public int alpha = 0xFF;
  public boolean advanced;
//	public int gdipGraphics;
	public int gdipPen;
	public int gdipBrush;
  public Pattern foregroundPattern;
  public Pattern backgroundPattern;
}
