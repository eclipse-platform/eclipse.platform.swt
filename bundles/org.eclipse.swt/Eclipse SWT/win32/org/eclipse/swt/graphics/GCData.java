/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
package org.eclipse.swt.graphics;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.win32.*;

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
 * @noreference This class is not intended to be referenced by clients
 */
public final class GCData {
	public Device device;
	public int style, state = -1;
	public int foreground = -1;
	public int background = -1;
	public Font font;
	public Pattern foregroundPattern;
	public long gdipFgPatternBrushAlpha;
	public Pattern backgroundPattern;
	public long gdipBgPatternBrushAlpha;
	public int lineStyle = SWT.LINE_SOLID;
	public float lineWidth;
	public int lineCap = SWT.CAP_FLAT;
	public int lineJoin = SWT.JOIN_MITER;
	public float lineDashesOffset;
	public float[] lineDashes;
	public float lineMiterLimit = 10;
	public int alpha = 0xFF;

	public Image image;
	public PAINTSTRUCT ps;
	public int layout = -1;
	public long hPen, hOldPen, hBrush, hOldBrush, hNullBitmap,
		hwnd, gdipGraphics, gdipPen, gdipBrush, gdipFgBrush, gdipBgBrush,
		gdipFont, hGDIFont;
	public float gdipXOffset, gdipYOffset;
	public int uiState = 0;
	public boolean focusDrawn;
}
