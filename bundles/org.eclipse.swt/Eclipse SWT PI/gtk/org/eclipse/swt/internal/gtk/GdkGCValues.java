/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others. All rights reserved.
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
	/** @field accessor=foreground.pixel,cast=(guint32) */
	public int foreground_pixel;
	/** @field accessor=foreground.red,cast=(guint16) */
	public short foreground_red;
	/** @field accessor=foreground.green,cast=(guint16) */
	public short foreground_green;
	/** @field accessor=foreground.blue,cast=(guint16) */
	public short foreground_blue;
	/** @field accessor=background.pixel,cast=(guint32) */
	public int background_pixel;
	/** @field accessor=background.red,cast=(guint16) */
	public short background_red;
	/** @field accessor=background.green,cast=(guint16) */
	public short background_green;
	/** @field accessor=background.blue,cast=(guint16) */
	public short background_blue;
	/** @field cast=(GdkFont *) */
	public long /*int*/ font;
	/** @field cast=(GdkFunction) */
	public long /*int*/ function;
	/** @field cast=(GdkFill) */
	public int fill;
	/** @field cast=(GdkPixmap *) */
	public long /*int*/ tile;
	/** @field cast=(GdkPixmap *) */
	public long /*int*/ stipple;
	/** @field cast=(GdkPixmap *) */
	public long /*int*/ clip_mask;
	/** @field cast=(GdkSubwindowMode) */
	public int subwindow_mode;
	/** @field cast=(gint) */
	public int ts_x_origin;
	/** @field cast=(gint) */
	public int ts_y_origin;
	/** @field cast=(gint) */
	public int clip_x_origin;
	/** @field cast=(gint) */
	public int clip_y_origin;
	/** @field cast=(gint) */
	public int graphics_exposures;
	/** @field cast=(gint) */
	public int line_width;
	/** @field cast=(GdkLineStyle) */
	public int line_style;
	/** @field cast=(GdkCapStyle) */
	public int cap_style;
	/** @field cast=(GdkJoinStyle) */
	public int join_style;
	public static final int sizeof = OS.GdkGCValues_sizeof();
}
