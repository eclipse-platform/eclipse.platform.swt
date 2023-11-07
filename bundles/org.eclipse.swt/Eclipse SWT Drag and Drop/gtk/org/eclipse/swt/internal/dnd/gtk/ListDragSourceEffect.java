/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
package org.eclipse.swt.internal.dnd.gtk;

import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;

/**
 * This class provides default implementations to display a source image
 * when a drag is initiated from a <code>List</code>.
 *
 * <p>Classes that wish to provide their own source image for a <code>List</code> can
 * extend the <code>ListDragSourceEffect</code> class, override the
 * <code>ListDragSourceEffect.dragStart</code> method and set the field
 * <code>DragSourceEvent.image</code> with their own image.</p>
 *
 * Subclasses that override any methods of this class must call the corresponding
 * <code>super</code> method to get the default drag source effect implementation.
 *
 * @see DragSourceEffect
 * @see DragSourceEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class ListDragSourceEffect extends DragSourceEffect {
	Image dragSourceImage = null;
	private List dragList;

	/**
	 * Creates a new <code>ListDragSourceEffect</code> to handle drag effect
	 * from the specified <code>List</code>.
	 *
	 * @param list the <code>List</code> that the user clicks on to initiate the drag
	 */
	public ListDragSourceEffect(List list) {
		super(list);
		dragList = list;
	}

	/**
	 * This implementation of <code>dragFinished</code> disposes the image
	 * that was created in <code>ListDragSourceEffect.dragStart</code>.
	 *
	 * Subclasses that override this method should call <code>super.dragFinished(event)</code>
	 * to dispose the image in the default implementation.
	 *
	 * @param event the information associated with the drag finished event
	 */
	@Override
	public void dragFinished(DragSourceEvent event) {
		if (dragSourceImage != null) dragSourceImage.dispose();
		dragSourceImage = null;
	}

	/**
	 * This implementation of <code>dragStart</code> will create a default
	 * image that will be used during the drag. The image should be disposed
	 * when the drag is completed in the <code>ListDragSourceEffect.dragFinished</code>
	 * method.
	 *
	 * Subclasses that override this method should call <code>super.dragStart(event)</code>
	 * to use the image from the default implementation.
	 *
	 * @param event the information associated with the drag start event
	 */
	@Override
	public void dragStart(DragSourceEvent event) {
		event.image = getDragSourceImage(event);
	}

	Image getDragSourceImage(DragSourceEvent event) {
		if (dragSourceImage != null) dragSourceImage.dispose();
		dragSourceImage = null;

		//TEMPORARY CODE
		if (dragList.isListening(SWT.EraseItem) || dragList.isListening (SWT.PaintItem)) return null;

		long handle = dragList.handle;
		long selection = GTK.gtk_tree_view_get_selection (handle);
		long [] model = null;
		long list = GTK.gtk_tree_selection_get_selected_rows (selection, model);
		if (list == 0) return null;
		int count = Math.min(10, OS.g_list_length (list));
		long originalList = list;

		Display display = dragList.getDisplay();
		if (count == 1) {
			long path = OS.g_list_nth_data (list, 0);
			long icon = GTK.gtk_tree_view_create_row_drag_icon (handle, path);
			dragSourceImage =  Image.gtk_new (display, SWT.ICON, icon, 0);
			GTK.gtk_tree_path_free (path);
		} else {
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
			long surface = Cairo.cairo_image_surface_create(Cairo.CAIRO_FORMAT_ARGB32, width, height);
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
			dragSourceImage =  Image.gtk_new (display, SWT.ICON, surface, 0);
		}
		OS.g_list_free (originalList);
		return dragSourceImage;
	}
}
