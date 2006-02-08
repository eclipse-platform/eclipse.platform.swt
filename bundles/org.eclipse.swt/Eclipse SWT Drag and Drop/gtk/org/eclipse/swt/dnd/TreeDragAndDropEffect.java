/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;

class TreeDragAndDropEffect extends DragAndDropEffect {
	Tree tree;
	
	int scrollIndex = -1;
	long scrollBeginTime;

	int expandIndex = -1;
	long expandBeginTime;
	
	static final int SCROLL_HYSTERESIS = 150; // milli seconds
	static final int EXPAND_HYSTERESIS = 300; // milli seconds

TreeDragAndDropEffect(Tree tree) {
	this.tree = tree;
}

int checkEffect(int effect) {
	// Some effects are mutually exclusive.  Make sure that only one of the mutually exclusive effects has been specified.
	if ((effect & DND.FEEDBACK_SELECT) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER & ~DND.FEEDBACK_INSERT_BEFORE;
	if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0) effect = effect & ~DND.FEEDBACK_INSERT_AFTER;
	return effect;
}

Widget getItem(int x, int y) {
	Point coordinates = new Point(x, y);
	coordinates = tree.toControl(coordinates);
	TreeItem item = tree.getItem(coordinates);
	if (item == null) {
		Rectangle area = tree.getClientArea();
		if (area.contains(coordinates)) {
			// Scan across the width of the tree.
			for (int x1 = area.x; x1 < area.x + area.width; x1++) {
				Point pt = new Point(x1, coordinates.y);
				item = tree.getItem(pt);
				if (item != null) {
					break;
				}
			}
		}
	}
	return item;
}

void showDropTargetEffect(int effect, int x, int y) {
	effect = checkEffect(effect);
	int /*long*/ handle = tree.handle;
	Point coordinates = new Point(x, y);
	coordinates = tree.toControl(coordinates);
	int /*long*/ [] path = new int /*long*/ [1];
	int clientX = coordinates.x - tree.getBorderWidth ();
	int clientY = coordinates.y - tree.getHeaderHeight ();
	OS.gtk_tree_view_get_path_at_pos (handle, clientX, clientY, path, null, null, null);
	int index = -1;
	if (path[0] != 0) {
		int /*long*/ indices = OS.gtk_tree_path_get_indices(path[0]);
		if (indices != 0) {	
			int depth = OS.gtk_tree_path_get_depth(path[0]);
			int[] temp = new int[depth];
			OS.memmove (temp, indices, temp.length * 4);
			index = temp[temp.length - 1];
		}
	}
	if ((effect & DND.FEEDBACK_SCROLL) == 0) {
		scrollBeginTime = 0;
		scrollIndex = -1;
	} else {
		if (index != -1 && scrollIndex == index && scrollBeginTime != 0) {
			if (System.currentTimeMillis() >= scrollBeginTime) {
				GdkRectangle cellRect = new GdkRectangle ();
				OS.gtk_tree_view_get_cell_area (handle, path[0], 0, cellRect);
				if (cellRect.y < cellRect.height) {
					int[] tx = new int[1], ty = new int[1];
					OS.gtk_tree_view_widget_to_tree_coords(handle, cellRect.x, cellRect.y - cellRect.height, tx, ty);
					OS.gtk_tree_view_scroll_to_point (handle, -1, ty[0]);
				} else {
					//scroll down
					OS.gtk_tree_view_get_path_at_pos (handle, clientX, clientY + cellRect.height, path, null, null, null);
					if (path[0] != 0) {
						OS.gtk_tree_view_scroll_to_cell(handle, path[0], 0, false, 0, 0);
						OS.gtk_tree_path_free(path[0]);
						path[0] = 0;
					}
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
	if ((effect & DND.FEEDBACK_EXPAND) == 0) {
		expandBeginTime = 0;
		expandIndex = -1;
	} else {
		if (index != -1 && expandIndex == index && expandBeginTime != 0) {
			if (System.currentTimeMillis() >= expandBeginTime) {
				OS.gtk_tree_view_expand_row (handle, path[0], false);
				expandBeginTime = 0;
				expandIndex = -1;
			}
		} else {
			expandBeginTime = System.currentTimeMillis() + EXPAND_HYSTERESIS;
			expandIndex = index;
		}
	}
	if (path[0] != 0) {
		int position = -1;
		if ((effect & DND.FEEDBACK_SELECT) != 0) position = OS.GTK_TREE_VIEW_DROP_INTO_OR_BEFORE;
		if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0) position = OS.GTK_TREE_VIEW_DROP_BEFORE;
		if ((effect & DND.FEEDBACK_INSERT_AFTER) != 0) position = OS.GTK_TREE_VIEW_DROP_AFTER;		
		if (position != -1) {
			OS.gtk_tree_view_set_drag_dest_row(handle, path[0], position);
		} else {
			OS.gtk_tree_view_unset_rows_drag_dest(handle);
		}
	} else {
		OS.gtk_tree_view_unset_rows_drag_dest(handle);
	}
	
	if (path[0] != 0) OS.gtk_tree_path_free (path [0]);
}
}
