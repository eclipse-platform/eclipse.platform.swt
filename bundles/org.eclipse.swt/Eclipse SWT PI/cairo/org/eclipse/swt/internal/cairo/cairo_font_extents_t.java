/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *******************************************************************************/
package org.eclipse.swt.internal.cairo;


public class cairo_font_extents_t {
	public float ascent;
	public float descent;
	public float height;
	public float max_x_advance;
	public float max_y_advance;
	public static final int sizeof = Cairo.cairo_font_extents_t_sizeof();
}
