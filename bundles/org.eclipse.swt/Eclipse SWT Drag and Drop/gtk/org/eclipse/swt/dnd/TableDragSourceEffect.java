/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;

/**
 * This class provides default implementations to display a source image
 * when a drag is initiated from a <code>Table</code>.
 * 
 * <p>Classes that wish to provide their own source image for a <code>Table</code> can
 * extend the <code>TableDragSourceEffect</code> class, override the 
 * <code>TableDragSourceEffect.dragStart</code> method and set the field 
 * <code>DragSourceEvent.image</code> with their own image.</p>
 *
 * Subclasses that override any methods of this class must call the corresponding
 * <code>super</code> method to get the default drag source effect implementation.
 *
 * @see DragSourceEffect
 * @see DragSourceEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.3
 */
public class TableDragSourceEffect extends DragSourceEffect {
	Image dragSourceImage = null;

	/**
	 * Creates a new <code>TableDragSourceEffect</code> to handle drag effect 
	 * from the specified <code>Table</code>.
	 *
	 * @param table the <code>Table</code> that the user clicks on to initiate the drag
	 */
	public TableDragSourceEffect(Table table) {
		super(table);
	}

	/**
	 * This implementation of <code>dragFinished</code> disposes the image
	 * that was created in <code>TableDragSourceEffect.dragStart</code>.
	 * 
	 * Subclasses that override this method should call <code>super.dragFinished(event)</code>
	 * to dispose the image in the default implementation.
	 * 
	 * @param event the information associated with the drag finished event
	 */
	public void dragFinished(DragSourceEvent event) {
		if (dragSourceImage != null) dragSourceImage.dispose();
		dragSourceImage = null;		
	}

	/**
	 * This implementation of <code>dragStart</code> will create a default
	 * image that will be used during the drag. The image should be disposed
	 * when the drag is completed in the <code>TableDragSourceEffect.dragFinished</code>
	 * method.
	 * 
	 * Subclasses that override this method should call <code>super.dragStart(event)</code>
	 * to use the image from the default implementation.
	 * 
	 * @param event the information associated with the drag start event
	 */
	public void dragStart(DragSourceEvent event) {
		event.image = getDragSourceImage(event);
	}
	
	Image getDragSourceImage(DragSourceEvent event) {
		if (dragSourceImage != null) dragSourceImage.dispose();
		dragSourceImage = null;		
		
		Table table = (Table) control;
		if (OS.GTK_VERSION < OS.VERSION (2, 2, 0)) return null;
		//TEMPORARY CODE
		if (table.isListening(SWT.EraseItem) || table.isListening (SWT.PaintItem)) return null;
		/*
		* Bug in GTK.  gtk_tree_selection_get_selected_rows() segmentation faults
		* in versions smaller than 2.2.4 if the model is NULL.  The fix is
		* to give a valid pointer instead.
		*/
		int /*long*/ handle = table.handle;
		int /*long*/ selection = OS.gtk_tree_view_get_selection (handle);
		int /*long*/ [] model = OS.GTK_VERSION < OS.VERSION (2, 2, 4) ? new int /*long*/ [1] : null;
		int /*long*/ list = OS.gtk_tree_selection_get_selected_rows (selection, model);
		if (list == 0) return null;
		int count = Math.min(10, OS.g_list_length (list));

		Display display = table.getDisplay();
		if (count == 1) {
			int /*long*/ path = OS.g_list_nth_data (list, 0);
			int /*long*/ pixmap = OS.gtk_tree_view_create_row_drag_icon(handle, path);
			dragSourceImage =  Image.gtk_new(display, SWT.ICON, pixmap, 0); 
		} else {
			int width = 0, height = 0;
			int[] w = new int[1], h = new int[1];
			int[] yy = new int[count], hh = new int[count];
			int /*long*/ [] pixmaps = new int /*long*/ [count];
			GdkRectangle rect = new GdkRectangle ();
			for (int i=0; i<count; i++) {
				int /*long*/ path = OS.g_list_nth_data (list, i);
				OS.gtk_tree_view_get_cell_area (handle, path, 0, rect);
				pixmaps[i] = OS.gtk_tree_view_create_row_drag_icon(handle, path);
				OS.gdk_drawable_get_size(pixmaps[i], w, h);
				width = Math.max(width, w[0]);
				height = rect.y + h[0] - yy[0];
				yy[i] = rect.y;
				hh[i] = h[0];
			}
			int /*long*/ source = OS.gdk_pixmap_new(OS.GDK_ROOT_PARENT(), width, height, -1);
			int /*long*/ gcSource = OS.gdk_gc_new(source);
			int /*long*/ mask = OS.gdk_pixmap_new(OS.GDK_ROOT_PARENT(), width, height, 1);
			int /*long*/ gcMask = OS.gdk_gc_new(mask);
			GdkColor color = new GdkColor();
			color.pixel = 0;
			OS.gdk_gc_set_foreground(gcMask, color);
			OS.gdk_draw_rectangle(mask, gcMask, 1, 0, 0, width, height);
			color.pixel = 1;
			OS.gdk_gc_set_foreground(gcMask, color);
			for (int i=0; i<count; i++) {
				OS.gdk_draw_drawable(source, gcSource, pixmaps[i], 0, 0, 0, yy[i] - yy[0], -1, -1);
				OS.gdk_draw_rectangle(mask, gcMask, 1, 0, yy[i] - yy[0], width, hh[i]);
				OS.g_object_unref(pixmaps[i]);
			}
			OS.g_object_unref(gcSource);
			OS.g_object_unref(gcMask);
			dragSourceImage  = Image.gtk_new(display, SWT.ICON, source, mask);
		}
		OS.g_list_free (list);
		
		return dragSourceImage;
}
}
