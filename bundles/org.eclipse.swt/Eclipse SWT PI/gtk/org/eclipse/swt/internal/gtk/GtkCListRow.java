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

public class GtkCListRow {
	public int cell;
	public int state;
	public short foreground_red;
	public short foreground_green;
	public short foreground_blue;
	public int foreground_pixel;
	public short background_red;
	public short background_green;
	public short background_blue;
	public int background_pixel;
	public int style;
	public int data;
	public int destroy; // bitfield: 1
	public int fg_set; // bitfield: 1
	public int bg_set; // bitfield: 1
	public int selectable; // bitfield: 1
	
	private GtkCListRow() {}
	public GtkCListRow(int ptr) {
		OS.memmove(this, ptr);
	}
}
