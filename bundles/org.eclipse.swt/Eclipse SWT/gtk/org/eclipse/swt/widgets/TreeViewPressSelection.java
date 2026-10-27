/*******************************************************************************
 * Copyright (c) 2026 Aleksandar Kurtakov and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.gtk.*;

/**
 * GTK4: GtkTreeView selects only the pressed row already on the press, so a drag of a
 * multi selection would carry the pressed row only. Like the GTK3 workaround for Bug
 * 503431, keep the selection when a selected row is pressed without modifiers. Select
 * the row alone on release, unless the application changed the selection meanwhile or a
 * drag took over the click.
 *
 * Used by the GtkTreeView based Tree, Table and List. They forward the press, release
 * and cancel of their click gesture, GtkTreeView::cursor-changed, GtkTreeSelection::changed
 * (SELECTION_CHANGED) and their disposal. Only one press can be pending, there is one
 * pointer.
 */
final class TreeViewPressSelection {
	private static long treeView, row;
	private static boolean blocked;

	private TreeViewPressSelection() {
	}

	/**
	 * Called from gtk_gesture_press_event of the tree view's click gesture, after the
	 * MouseDown was posted and before GtkTreeView's own click gesture handles the press.
	 */
	static void press(Display display, long handle, long model, int style, int n_press, int button, int state, double x, double y) {
		end();
		if (n_press != 1 || button != 1) return;
		if ((state & (GDK.GDK_CONTROL_MASK | GDK.GDK_SHIFT_MASK)) != 0) return;
		long selection = GTK.gtk_tree_view_get_selection(handle);
		if (GTK.gtk_tree_selection_count_selected_rows(selection) < 2) return;
		// The gesture coordinates are relative to the widget, the path lookup needs bin window ones
		int[] binX = new int[1], binY = new int[1];
		GTK.gtk_tree_view_convert_bin_window_to_widget_coords(handle, 0, 0, binX, binY);
		long[] path = new long[1];
		if (!GTK.gtk_tree_view_get_path_at_pos(handle, (int) x - binX[0], (int) y - binY[0], path, null, null, null)
				|| path[0] == 0) return;
		if (GTK.gtk_tree_selection_path_is_selected(selection, path[0]) && !overExpander(handle, model, path[0], style, (int) x - binX[0])) {
			/*
			 * Block selection changes only until GtkTreeView handled the press: it emits
			 * cursor-changed right after it tried to select the pressed row alone. The
			 * MouseDown is posted, so its listeners can change the selection again.
			 */
			GTK.gtk_tree_selection_set_select_function(selection, display.rejectSelectionProc, 0, 0);
			blocked = true;
			// Unlike a path, a row reference follows the row when the model changes
			row = GTK.gtk_tree_row_reference_new(model, path[0]);
			treeView = handle;
		}
		GTK.gtk_tree_path_free(path[0]);
	}

	/**
	 * Called from gtk_gesture_release_event: selects the pressed row alone if the selection
	 * did not change since the press and the row is still there.
	 */
	static void release(long handle) {
		if (treeView != handle) return;
		// 0 if the row was removed
		long path = GTK.gtk_tree_row_reference_get_path(row);
		end();
		if (path == 0) return;
		if (GTK.gtk_tree_selection_path_is_selected(GTK.gtk_tree_view_get_selection(handle), path)) {
			GTK.gtk_tree_view_set_cursor(handle, path, 0, false);
		}
		GTK.gtk_tree_path_free(path);
	}

	/**
	 * Called from gtk_gesture_cancel_event: another gesture took over the press, e.g. a
	 * GtkDragSource after it prepared the drag, or a grab reset the controllers. Unlike
	 * GtkGestureClick::stopped, which is also emitted when the double-click time passes
	 * while the button is still down.
	 */
	static void cancel(long handle) {
		if (treeView == handle) end();
	}

	/** Called from gtk_cursor_changed: GtkTreeView handled the press. */
	static void cursorChanged(long handle) {
		if (treeView == handle && blocked) unblock();
	}

	/**
	 * Called from gtk_selection_changed. After GtkTreeView handled the press, it does not
	 * change the selection until the release, so the application did: keep its selection.
	 */
	static void selectionChanged(long handle) {
		if (treeView == handle && !blocked) end();
	}

	/*
	 * A press on the expander of a parent row only expands or collapses it: GtkTreeView
	 * claims the press, so neither the release nor cursor-changed would end it. Like
	 * Tree.getItem(Point), the expander is before the cell area of the expander column.
	 */
	private static boolean overExpander(long handle, long model, long path, int style, int x) {
		long column = GTK.gtk_tree_view_get_expander_column(handle);
		if (column == 0) return false;
		long iter = OS.g_malloc(GTK.GtkTreeIter_sizeof());
		boolean parent = GTK.gtk_tree_model_get_iter(model, iter, path) && GTK.gtk_tree_model_iter_n_children(model, iter) > 0;
		OS.g_free(iter);
		if (!parent) return false;
		GdkRectangle rect = new GdkRectangle();
		GTK.gtk_tree_view_get_cell_area(handle, path, column, rect);
		return (style & SWT.MIRRORED) != 0 ? x >= rect.x + rect.width : x < rect.x;
	}

	/** Called from releaseWidget. */
	static void dispose(long handle) {
		if (treeView == handle) end();
	}

	private static void unblock() {
		blocked = false;
		GTK.gtk_tree_selection_set_select_function(GTK.gtk_tree_view_get_selection(treeView), 0, 0, 0);
	}

	private static void end() {
		if (treeView == 0) return;
		if (blocked) unblock();
		GTK.gtk_tree_row_reference_free(row);
		treeView = row = 0;
	}
}
