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


public class GdkVisual {
	/** @field cast=(GdkVisualType) */
	public int type;
	/** @field cast=(gint) */
	public int depth;
	/** @field cast=(GdkByteOrder) */
	public int byte_order;
	/** @field cast=(gint) */
	public int colormap_size;
	/** @field cast=(gint) */
	public int bits_per_rgb;
	/** @field cast=(guint32) */
	public int red_mask;
	/** @field cast=(gint) */
	public int red_shift;
	/** @field cast=(gint) */
	public int red_prec;
	/** @field cast=(guint32) */
	public int green_mask;
	/** @field cast=(gint) */
	public int green_shift;
	/** @field cast=(gint) */
	public int green_prec;
	/** @field cast=(guint32) */
	public int blue_mask;
	/** @field cast=(gint) */
	public int blue_shift;
	/** @field cast=(gint) */
	public int blue_prec;
}
