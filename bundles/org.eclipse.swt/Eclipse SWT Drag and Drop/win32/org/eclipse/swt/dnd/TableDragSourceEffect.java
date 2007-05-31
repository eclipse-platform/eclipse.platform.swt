/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
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
import org.eclipse.swt.internal.win32.*;
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
		TableItem[] selection = table.getSelection();
		if (selection.length == 0) return null;
		int tableImageList = OS.SendMessage (table.handle, OS.LVM_GETIMAGELIST, OS.LVSIL_SMALL, 0);
		if (tableImageList != 0) {
			int count = Math.min(selection.length, 10);
			Rectangle bounds = selection[0].getBounds(0);
			for (int i = 1; i < count; i++) {
				bounds = bounds.union(selection[i].getBounds(0));
			}
			int hDC = OS.GetDC(0);
			int hDC1 = OS.CreateCompatibleDC(hDC);
			int bitmap = OS.CreateCompatibleBitmap(hDC, bounds.width, bounds.height);
			int hOldBitmap = OS.SelectObject(hDC1, bitmap);
			RECT rect = new RECT();
			rect.right = bounds.width;
			rect.bottom = bounds.height;
			int hBrush = OS.GetStockObject(OS.WHITE_BRUSH);
			OS.FillRect(hDC1, rect, hBrush);
			for (int i = 0; i < count; i++) {
				TableItem selected = selection[i];
				Rectangle cell = selected.getBounds(0);
				POINT pt = new POINT();
				int imageList = OS.SendMessage (table.handle, OS.LVM_CREATEDRAGIMAGE, table.indexOf(selected), pt);
				OS.ImageList_Draw(imageList, 0, hDC1, cell.x - bounds.x, cell.y - bounds.y, OS.ILD_SELECTED);
				OS.ImageList_Destroy(imageList);
			}
			OS.SelectObject(hDC1, hOldBitmap);
			OS.DeleteDC (hDC1);
			OS.ReleaseDC (0, hDC);
			Display display = table.getDisplay();
			dragSourceImage = Image.win32_new(display, SWT.BITMAP, bitmap);
			return dragSourceImage;
		}
		return null;
	}
}
