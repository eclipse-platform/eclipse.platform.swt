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

public class GtkCTreeRow {
	public int parent;
	public int sibling;
	public int children;
	public int pixmap_closed;
	public int mask_closed;
	public int pixmap_opened;
	public int mask_opened;
	public short level;
	public int is_leaf; // bitfield: 1
	public int expanded; // bitfield: 1

	
	private GtkCTreeRow() {}
	public GtkCTreeRow(int ptr) {
		OS.memmove(this, ptr);
	}
}
