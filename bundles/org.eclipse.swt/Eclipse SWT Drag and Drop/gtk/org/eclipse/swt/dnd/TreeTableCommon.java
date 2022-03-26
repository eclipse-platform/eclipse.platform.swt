/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;

class TreeTableCommon {
	static Image getDragSourceImage(Control control) {
		Image dragSourceImage;

		/*
		 * Bug in GTK.  gtk_tree_selection_get_selected_rows() segmentation faults
		 * in versions smaller than 2.2.4 if the model is NULL.  The fix is
		 * to give a valid pointer instead.
		 */
		long handle = control.handle;
		long selection = GTK.gtk_tree_view_get_selection (handle);
		long [] model =  null;
		long list = GTK.gtk_tree_selection_get_selected_rows (selection, model);
		if (list == 0) return null;
		int count = Math.min(10, OS.g_list_length (list));
		long originalList = list;

		Display display = control.getDisplay();
		int width = 0, height = 0;
		int[] w = new int[1], h = new int[1];
		int[] yy = new int[count], hh = new int[count];
		long [] icons = new long [count];
		GdkRectangle rect = new GdkRectangle ();
		for (int i=0; i<count; i++) {
			long path = OS.g_list_data (list);
			GTK.gtk_tree_view_get_cell_area (handle, path, 0, rect);
			icons[i] = GTK.gtk_tree_view_create_row_drag_icon(handle, path);
			switch (Cairo.cairo_surface_get_type(icons[i])) {
				case Cairo.CAIRO_SURFACE_TYPE_IMAGE:
					w[0] = Cairo.cairo_image_surface_get_width(icons[i]);
					h[0] = Cairo.cairo_image_surface_get_height(icons[i]);
					break;
				case Cairo.CAIRO_SURFACE_TYPE_XLIB:
					w[0] = Cairo.cairo_xlib_surface_get_width(icons[i]);
					h[0] = Cairo.cairo_xlib_surface_get_height(icons[i]);
					break;
			}
			width = Math.max(width, w[0]);
			height = rect.y + h[0] - yy[0];
			yy[i] = rect.y;
			hh[i] = h[0];
			list = OS.g_list_next (list);
			GTK.gtk_tree_path_free (path);
		}
		OS.g_list_free (originalList);
		list = originalList = 0;

		// Limit image size to Control's size. This is to avoid
		// images that are too wide when wide columns are present.
		// Windows and macOS also limit width to Control's width.
		GtkAllocation controlSize = new GtkAllocation();
		GTK.gtk_widget_get_allocation(handle, controlSize);
		int sourceWidth = width;
		width = Math.min(width, controlSize.width);

		long surface;
		if ((count == 1) && (sourceWidth == width)) {
			surface = icons[0];
		} else {
			surface = Cairo.cairo_image_surface_create(Cairo.CAIRO_FORMAT_ARGB32, width, height);
			if (surface == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			long cairo = Cairo.cairo_create(surface);
			if (cairo == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			Cairo.cairo_set_operator(cairo, Cairo.CAIRO_OPERATOR_SOURCE);
			for (int i=0; i<count; i++) {
				Cairo.cairo_set_source_surface (cairo, icons[i], 2, yy[i] - yy[0] + 2);
				Cairo.cairo_rectangle(cairo, 0, yy[i] - yy[0], width, hh[i]);
				Cairo.cairo_fill(cairo);
				Cairo.cairo_surface_destroy(icons[i]);
			}
			Cairo.cairo_destroy(cairo);
		}

		dragSourceImage = Image.gtk_new (display, SWT.ICON, surface, 0);
		return dragSourceImage;
	}
}
