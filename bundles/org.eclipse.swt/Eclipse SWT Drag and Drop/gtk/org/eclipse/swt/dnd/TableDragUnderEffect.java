/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;


import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.gtk.OS;

class TableDragUnderEffect extends DragUnderEffect {
	private Table table;
	private int scrollIndex = -1;
	private long scrollBeginTime;
	private static final int SCROLL_HYSTERESIS = 500; // milli seconds

TableDragUnderEffect(Table table) {
	this.table = table;
}
private int checkEffect(int effect) {
	// Some effects are mutually exclusive.  Make sure that only one of the mutually exclusive effects has been specified.
	if ((effect & DND.FEEDBACK_SELECT) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER & ~DND.FEEDBACK_INSERT_BEFORE;
	if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER;
	return effect;
}

void show(int effect, int x, int y) {
	effect = checkEffect(effect);
	int handle = table.handle;
	Point coordinates = new Point(x, y);
	coordinates = table.toControl(coordinates);
	int /*long*/ [] path = new int /*long*/ [1];
	int clientX = coordinates.x - table.getBorderWidth ();
	int clientY = coordinates.y - table.getHeaderHeight ();
	OS.gtk_tree_view_get_path_at_pos (handle, clientX, clientY, path, null, null, null);
	int index = -1;
	if (path[0] != 0) {
		int /*long*/ indices = OS.gtk_tree_path_get_indices (path[0]);
		if (indices != 0) {
			int[] temp = new int[1];
			OS.memmove (temp, indices, 4);
			index = temp[0];
		}
	}
	if ((effect & DND.FEEDBACK_SCROLL) == 0) {
		scrollBeginTime = 0;
		scrollIndex = -1;
	} else {
		if (index != -1 && scrollIndex == index && scrollBeginTime != 0) {
			if (System.currentTimeMillis() >= scrollBeginTime) {
				if (clientY < table.getItemHeight()) {
					OS.gtk_tree_path_prev(path[0]);
				} else {
					OS.gtk_tree_path_next(path[0]);
				}
				if (path[0] != 0) {
					OS.gtk_tree_view_scroll_to_cell(handle, path[0], 0, false, 0, 0);
					OS.gtk_tree_path_free(path[0]);
					path[0] = 0;
					OS.gtk_tree_view_get_path_at_pos (handle, clientX, clientY, path, null, null, null);
				}
				scrollBeginTime = 0;
				scrollIndex = -1;
			}
		} else {
			scrollBeginTime = System.currentTimeMillis() + SCROLL_HYSTERESIS;
			scrollIndex = index;
		}
	}
	if (path[0] != 0) {
		int position = 0;
		if ((effect & DND.FEEDBACK_SELECT) != 0) position = OS.GTK_TREE_VIEW_DROP_INTO_OR_BEFORE;
		//if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0) position = OS.GTK_TREE_VIEW_DROP_BEFORE;
		//if ((effect & DND.FEEDBACK_INSERT_AFTER) != 0) position = OS.GTK_TREE_VIEW_DROP_AFTER;
		if (position != 0) {
			OS.gtk_tree_view_set_drag_dest_row(handle, path[0], OS.GTK_TREE_VIEW_DROP_INTO_OR_BEFORE);
		} else {
			OS.gtk_tree_view_unset_rows_drag_dest(handle);
		}
	} else {
		OS.gtk_tree_view_unset_rows_drag_dest(handle);
	}
	if (path[0] != 0) OS.gtk_tree_path_free (path [0]);
}
}
