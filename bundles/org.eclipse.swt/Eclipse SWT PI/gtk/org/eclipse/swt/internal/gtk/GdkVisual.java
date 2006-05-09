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


public class GdkVisual {
	public int type;
	public int depth;
	public int byte_order;
	public int colormap_size;
	public int bits_per_rgb;
	public int red_mask;
	public int red_shift;
	public int red_prec;
	public int green_mask;
	public int green_shift;
	public int green_prec;
	public int blue_mask;
	public int blue_shift;
	public int blue_prec;
}
