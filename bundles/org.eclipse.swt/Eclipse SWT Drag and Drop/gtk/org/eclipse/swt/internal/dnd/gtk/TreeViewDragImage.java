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
package org.eclipse.swt.internal.dnd.gtk;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk4.*;
import org.eclipse.swt.widgets.*;

/**
 * GTK4: the drag image of the selected rows of a GtkTreeView based Tree, Table or
 * List. GtkTreeView gives each row as a GdkPaintable instead of the cairo surface of
 * GTK3, so draw the paintables into one image surface.
 */
public class TreeViewDragImage {

	/**
	 * Returns an image of up to 10 visible selected rows of the control at their
	 * positions, or null if no selected row is visible. The caller disposes the image.
	 */
	public static Image create(Control control) {
		if (control.isDisposed()) return null;
		long handle = control.handle;
		long list = GTK.gtk_tree_selection_get_selected_rows(GTK.gtk_tree_view_get_selection(handle), null);
		/*
		 * Only rows in view: the image spans from the first to the last row, and rows far
		 * apart would exceed the size limit of a cairo surface. The row areas are relative
		 * to the visible rows, below the header.
		 */
		GdkRectangle visible = new GdkRectangle();
		GTK.gtk_tree_view_get_visible_rect(handle, visible);
		long[] icons = new long[10];
		int[] y = new int[10];
		int count = 0;
		// The selected rows come in display order, so the rows after one below the view are too
		boolean belowView = false;
		GdkRectangle rect = new GdkRectangle();
		for (long item = list; item != 0; item = OS.g_list_next(item)) {
			long path = OS.g_list_data(item);
			if (count < icons.length && !belowView) {
				GTK.gtk_tree_view_get_background_area(handle, path, 0, rect);
				if (rect.y >= visible.height) {
					belowView = true;
				} else if (rect.y + rect.height > 0) {
					icons[count] = GTK.gtk_tree_view_create_row_drag_icon(handle, path);
					y[count] = rect.y;
					count++;
				}
			}
			GTK.gtk_tree_path_free(path);
		}
		OS.g_list_free(list);
		if (count == 0) return null;

		/*
		 * Draw each row at its own size, it has all the columns and a frame 2 pixels wider
		 * than the control. Like on GTK3, the image is at most as wide as the control, so
		 * wide columns and the right line of the frame are cut off.
		 */
		int[] rowWidth = new int[count], rowHeight = new int[count];
		int width = 0;
		for (int i = 0; i < count; i++) {
			if (icons[i] == 0) continue;
			rowWidth[i] = GTK4.gdk_paintable_get_intrinsic_width(icons[i]);
			rowHeight[i] = GTK4.gdk_paintable_get_intrinsic_height(icons[i]);
			width = Math.max(width, rowWidth[i]);
		}
		width = Math.max(1, Math.min(width, GTK4.gtk_widget_get_width(handle)));
		int height = Math.max(1, y[count - 1] + rowHeight[count - 1] - y[0]);

		// Render at the scale of the widget, so that the image is sharp on HiDPI
		int scale = GTK.gtk_widget_get_scale_factor(handle);
		long surface = Cairo.cairo_image_surface_create(Cairo.CAIRO_FORMAT_ARGB32, width * scale, height * scale);
		if (surface == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		Cairo.cairo_surface_set_device_scale(surface, scale, scale);
		long cairo = Cairo.cairo_create(surface);
		if (cairo == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		for (int i = 0; i < count; i++) {
			if (icons[i] == 0) continue;
			long snapshot = GTK4.gtk_snapshot_new();
			GTK4.gdk_paintable_snapshot(icons[i], snapshot, rowWidth[i], rowHeight[i]);
			long node = GTK4.gtk_snapshot_free_to_node(snapshot);
			if (node != 0) {
				Cairo.cairo_save(cairo);
				Cairo.cairo_translate(cairo, 0, y[i] - y[0]);
				GTK4.gsk_render_node_draw(node, cairo);
				Cairo.cairo_restore(cairo);
				GTK4.gsk_render_node_unref(node);
			}
			OS.g_object_unref(icons[i]);
		}
		Cairo.cairo_destroy(cairo);
		return Image.gtk_new(control.getDisplay(), SWT.ICON, surface, 0);
	}
}
