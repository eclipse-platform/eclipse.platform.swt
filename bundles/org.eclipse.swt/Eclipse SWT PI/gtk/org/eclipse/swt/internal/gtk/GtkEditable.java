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

public class GtkEditable {
	public int current_pos;
	public int selection_start_pos;
	public int selection_end_pos;
	public int has_selection; // bitfield : 1
	public int editable; // bitfield : 1
	public int visible; // bitfield : 1
	public int ic;
	public int ic_attr;
	public int clipboard_text;
	
	private GtkEditable() {}
	public GtkEditable(int ptr) {
		OS.memmove(this, ptr);
	}
}
