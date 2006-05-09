/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.gtk;


public class GdkGCValues {
	public int foreground_pixel;
	public short foreground_red;
	public short foreground_green;
	public short foreground_blue;
	public int background_pixel;
	public short background_red;
	public short background_green;
	public short background_blue;
	public int /*long*/ font;
	public int /*long*/ function;
	public int fill;
	public int /*long*/ tile;
	public int /*long*/ stipple;
	public int /*long*/ clip_mask;
	public int subwindow_mode;
	public int ts_x_origin;
	public int ts_y_origin;
	public int clip_x_origin;
	public int clip_y_origin;
	public int graphics_exposures;
	public int line_width;
	public int line_style;
	public int cap_style;
	public int join_style;
	public static final int sizeof = OS.GdkGCValues_sizeof();
}
