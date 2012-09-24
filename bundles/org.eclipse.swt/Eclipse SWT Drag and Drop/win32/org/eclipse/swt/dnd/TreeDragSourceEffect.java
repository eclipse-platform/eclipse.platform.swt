/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
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
 * when a drag is initiated from a <code>Tree</code>.
 * 
 * <p>Classes that wish to provide their own source image for a <code>Tree</code> can
 * extend <code>TreeDragSourceEffect</code> class and override the <code>TreeDragSourceEffect.dragStart</code>
 * method and set the field <code>DragSourceEvent.image</code> with their own image.</p>
 *
 * Subclasses that override any methods of this class must call the corresponding
 * <code>super</code> method to get the default drag under effect implementation.
 *
 * @see DragSourceEffect
 * @see DragSourceEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.3
 */
public class TreeDragSourceEffect extends DragSourceEffect {
	Image dragSourceImage = null;

	/**
	 * Creates a new <code>TreeDragSourceEffect</code> to handle drag effect 
	 * from the specified <code>Tree</code>.
	 *
	 * @param tree the <code>Tree</code> that the user clicks on to initiate the drag
	 */
	public TreeDragSourceEffect(Tree tree) {
		super(tree);
	}

	/**
	 * This implementation of <code>dragFinished</code> disposes the image
	 * that was created in <code>TreeDragSourceEffect.dragStart</code>.
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
	 * when the drag is completed in the <code>TreeDragSourceEffect.dragFinished</code>
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
		if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1)) {
			SHDRAGIMAGE shdi = new SHDRAGIMAGE();
			int DI_GETDRAGIMAGE = OS.RegisterWindowMessage (new TCHAR (0, "ShellGetDragImage", true)); //$NON-NLS-1$
			if (OS.SendMessage (control.handle, DI_GETDRAGIMAGE, 0, shdi) != 0) {
				if ((control.getStyle() & SWT.MIRRORED) != 0) {
					event.offsetX = shdi.sizeDragImage.cx - shdi.ptOffset.x;
				} else {
					event.offsetX = shdi.ptOffset.x;
				}
				event.offsetY = shdi.ptOffset.y;
				long /*int*/ hImage = shdi.hbmpDragImage;
				if (hImage != 0) {
					BITMAP bm = new BITMAP ();
					OS.GetObject (hImage, BITMAP.sizeof, bm);
					int srcWidth = bm.bmWidth;
					int srcHeight = bm.bmHeight;
					
					/* Create resources */
					long /*int*/ hdc = OS.GetDC (0);
					long /*int*/ srcHdc = OS.CreateCompatibleDC (hdc);
					long /*int*/ oldSrcBitmap = OS.SelectObject (srcHdc, hImage);
					long /*int*/ memHdc = OS.CreateCompatibleDC (hdc);
					BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER ();
					bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
					bmiHeader.biWidth = srcWidth;
					bmiHeader.biHeight = -srcHeight;
					bmiHeader.biPlanes = 1;
					bmiHeader.biBitCount = 32;
					bmiHeader.biCompression = OS.BI_RGB;
					byte []	bmi = new byte[BITMAPINFOHEADER.sizeof];
					OS.MoveMemory (bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
					long /*int*/ [] pBits = new long /*int*/ [1];
					long /*int*/ memDib = OS.CreateDIBSection (0, bmi, OS.DIB_RGB_COLORS, pBits, 0, 0);
					if (memDib == 0) SWT.error (SWT.ERROR_NO_HANDLES);
					long /*int*/ oldMemBitmap = OS.SelectObject (memHdc, memDib);

					BITMAP dibBM = new BITMAP ();
					OS.GetObject (memDib, BITMAP.sizeof, dibBM);
					int sizeInBytes = dibBM.bmWidthBytes * dibBM.bmHeight;

				 	/* Get the foreground pixels */
				 	OS.BitBlt (memHdc, 0, 0, srcWidth, srcHeight, srcHdc, 0, 0, OS.SRCCOPY);
				 	byte[] srcData = new byte [sizeInBytes];
					OS.MoveMemory (srcData, dibBM.bmBits, sizeInBytes);
					
					PaletteData palette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
					ImageData data = new ImageData(srcWidth, srcHeight, bm.bmBitsPixel, palette, bm.bmWidthBytes, srcData);
					if (shdi.crColorKey == -1) {
						byte[] alphaData = new byte[srcWidth * srcHeight];
						int spinc = dibBM.bmWidthBytes - srcWidth * 4;
						int ap = 0, sp = 3;
						for (int y = 0; y < srcHeight; ++y) {
							for (int x = 0; x < srcWidth; ++x) {
								alphaData [ap++] = srcData [sp];
								sp += 4;
							}
							sp += spinc;
						}
						data.alphaData = alphaData;
					} else {
						data.transparentPixel = shdi.crColorKey << 8;
					}
					dragSourceImage = new Image (control.getDisplay (), data);
					OS.SelectObject (memHdc, oldMemBitmap);
					OS.DeleteDC (memHdc);
					OS.DeleteObject (memDib);
					OS.SelectObject (srcHdc, oldSrcBitmap);
					OS.DeleteDC (srcHdc);
					OS.ReleaseDC (0, hdc);
					OS.DeleteObject (hImage);
					return dragSourceImage;
				}
			}
			return null;
		}
		
		Tree tree = (Tree) control;
		//TEMPORARY CODE
		if (tree.isListening (SWT.EraseItem) || tree.isListening (SWT.PaintItem)) return null;
		TreeItem[] selection = tree.getSelection();
		if (selection.length == 0) return null;
		long /*int*/ treeImageList = OS.SendMessage (tree.handle, OS.TVM_GETIMAGELIST, OS.TVSIL_NORMAL, 0);
		if (treeImageList != 0) {
			int count = Math.min(selection.length, 10);
			Rectangle bounds = selection[0].getBounds(0);
			for (int i = 1; i < count; i++) {
				bounds = bounds.union(selection[i].getBounds(0));
			}
			long /*int*/ hDC = OS.GetDC(tree.handle);
			long /*int*/ hDC1 = OS.CreateCompatibleDC(hDC);
			long /*int*/ bitmap = OS.CreateCompatibleBitmap(hDC, bounds.width, bounds.height);
			long /*int*/ hOldBitmap = OS.SelectObject(hDC1, bitmap);
			RECT rect = new RECT();
			rect.right = bounds.width;
			rect.bottom = bounds.height;
			long /*int*/ hBrush = OS.GetStockObject(OS.WHITE_BRUSH);
			OS.FillRect(hDC1, rect, hBrush);
			for (int i = 0; i < count; i++) {
				TreeItem selected = selection[i];
				Rectangle cell = selected.getBounds(0);
				long /*int*/ imageList = OS.SendMessage(tree.handle, OS.TVM_CREATEDRAGIMAGE, 0, selected.handle);
				OS.ImageList_Draw(imageList, 0, hDC1, cell.x - bounds.x, cell.y - bounds.y, OS.ILD_SELECTED);
				OS.ImageList_Destroy(imageList);
			}
			OS.SelectObject(hDC1, hOldBitmap);
			OS.DeleteDC (hDC1);
			OS.ReleaseDC (tree.handle, hDC);
			Display display = tree.getDisplay();
			dragSourceImage = Image.win32_new(display, SWT.BITMAP, bitmap);
			return dragSourceImage;
		}
		return null;
	}
}
