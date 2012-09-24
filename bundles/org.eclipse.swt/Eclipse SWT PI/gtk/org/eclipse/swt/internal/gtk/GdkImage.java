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


public class GdkImage {
	/** @field cast=(GdkImageType) */
	public int type;
	/** @field cast=(GdkVisual *) */
	public long /*int*/ visual;
	/** @field cast=(GdkByteOrder) */
	public int byte_order;
	/** @field cast=(gint) */
	public int width;
	/** @field cast=(gint) */
	public int height;
	/** @field cast=(guint16) */
	public short depth;
	/** @field cast=(guint16) */
	public short bpp;
	/** @field cast=(guint16) */
	public short bpl;
	/** @field cast=(guint16) */
	public short bits_per_pixel;
	/** @field cast=(gpointer) */
	public long /*int*/ mem;
	/** @field cast=(GdkColormap *) */
	public long /*int*/ colormap;
	/** @field cast=(gpointer) */
	public long /*int*/ windowing_data;
}
