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

public class GtkCList {
	public short clist_flags;
	public int row_mem_chunk;
	public int cell_mem_chunk;
	public int freeze_count;
	public short internal_allocation_x;
	public short internal_allocation_y;
	public short internal_allocation_width;
	public short internal_allocation_height;
	public int rows;
	public int row_center_offset;
	public int row_height;
	public int row_list;
	public int row_list_end;
	public int columns;
	public short column_title_area_x;
	public short column_title_area_y;
	public short column_title_area_width;
	public short column_title_area_height;
	public int title_window;
	public int column;
	public int clist_window;
	public int clist_window_width;
	public int clist_window_height;
	public int hoffset;
	public int voffset;
	public int shadow_type;
	public int selection_mode;
	public int selection;
	public int selection_end;
	public int undo_selection;
	public int undo_unselection;
	public int undo_anchor;
	public byte button_actions0;
	public byte button_actions1;
	public byte button_actions2;
	public byte button_actions3;
	public byte button_actions4;
	public byte drag_button;
	public int click_cell_row;
	public int click_cell_column;
	public int hadjustment;
	public int vadjustment;
	public int xor_gc;
	public int fg_gc;
	public int bg_gc;
	public int cursor_drag;
	public int x_drag;
	public int focus_row;
	public int anchor;
	public int anchor_state;
	public int drag_pos;
	public int htimer;
	public int vtimer;
	public int sort_type;
	public int compare;
	public int sort_column;
	
	private GtkCList() {}
	public GtkCList(int ptr) {
		OS.memmove(this, ptr);
	}
}
