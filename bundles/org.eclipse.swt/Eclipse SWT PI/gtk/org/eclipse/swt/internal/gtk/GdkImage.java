package org.eclipse.swt.internal.gtk;

/*
 * Copyright (c) IBM Corp. 2000, 2001.  All rights reserved.
 *
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 */

public class GdkImage {
//	GObject parent_instance;
    //public int parent_instance_g_type_instance;
    //public int parent_instance_ref_count;
    //public int parent_instance_qdata; 
	public int type;
	public int visual;
	public int byte_order;
	public int width;
	public int height;
	public short depth;
	public short bpp;
	public short bpl;
	public short bits_per_pixel;
	public int mem;
	public int colormap;
	public int windowing_data;
	public static final int sizeof = 52;
	
	private GdkImage () {}
	public GdkImage (int ptr) {
		OS.memmove(this, ptr);
	}
}
