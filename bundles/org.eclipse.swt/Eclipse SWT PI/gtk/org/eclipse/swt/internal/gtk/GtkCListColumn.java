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

public class GtkCListColumn {
	public int title;
	public short area_x;
	public short area_y;
	public short area_width;
	public short area_height;
	public int button;
	public int window;
	public int width;
	public int min_width;
	public int max_width;
	public int justification;
	public int visible;
	public int width_set;
	public int resizeable;
	public int auto_resize;
	public int button_passive;
	public static final int sizeof = 40;
	
	private GtkCListColumn() {}
	public GtkCListColumn(int ptr) {
		OS.memmove(this, ptr);
	}
}
