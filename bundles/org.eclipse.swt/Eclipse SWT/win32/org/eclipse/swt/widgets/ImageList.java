/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

class ImageList {
	int handle, refCount;
	Image [] images;
	static final int CREATE_FLAGS;
	static {
		if (OS.IsWinCE) {
			CREATE_FLAGS = OS.ILC_MASK | OS.ILC_COLOR;
		} else {
			int flags = OS.ILC_MASK;
			int hDC = OS.GetDC (0);
			int bits = OS.GetDeviceCaps (hDC, OS.BITSPIXEL);
			int planes = OS.GetDeviceCaps (hDC, OS.PLANES);
			OS.ReleaseDC (0, hDC);
			int depth = bits * planes;
			switch (depth) {
				case 4:
					flags |= OS.ILC_COLOR4;
					break;
				case 8:
					flags |= OS.ILC_COLOR8;
					break;
				case 16:
					flags |= OS.ILC_COLOR16;
					break;
				case 24:
					flags |= OS.ILC_COLOR24;
					break;
				case 32:
					flags |= OS.ILC_COLOR32;
					break;
				default:
					flags |= OS.ILC_COLOR;
			}
			CREATE_FLAGS = flags;
		}
	}
	
public ImageList () {
	handle = OS.ImageList_Create (32, 32, CREATE_FLAGS, 16, 16);
	images = new Image [4];
}

public int add (Image image) {
	int count = OS.ImageList_GetImageCount (handle);
	int index = 0;
	while (index < count) {
		if (images [index] != null) {
			if (images [index].isDisposed ()) images [index] = null;
		}
		if (images [index] == null) break;
		index++;
	}
	int [] cx = new int [1], cy = new int [1];
	if (count == 0) {
		Rectangle rect = image.getBounds();
		cx [0] = rect.width;
		cy [0] = rect.height;
		OS.ImageList_SetIconSize (handle, cx [0], cy [0]);
	}
	int hImage = image.handle;
	OS.ImageList_GetIconSize (handle, cx, cy);
	switch (image.type) {
		case SWT.BITMAP: {
			int hBitmap = copyBitmap (hImage, cx [0], cy [0]);
			int background = -1;
			Color color = image.getBackground ();
			if (color != null) background = color.handle;
			if (index == count) {
				if (background != -1) {
					OS.ImageList_AddMasked (handle, hBitmap, background);
				} else {
					int hMask = createMask (hBitmap, cx [0], cy [0], background);
					OS.ImageList_Add (handle, hBitmap, hMask);
					OS.DeleteObject (hMask);
				}
			} else {
				int hMask = createMask (hBitmap, cx [0], cy [0], background);
				OS.ImageList_Replace (handle, index, hBitmap, hMask);
				OS.DeleteObject (hMask);
			}
			OS.DeleteObject (hBitmap);
			break;
		}
		case SWT.ICON: {
			if (OS.IsWinCE) {	
				OS.ImageList_ReplaceIcon (handle, index == count ? -1 : index, hImage);
			} else {
				int hIcon = copyIcon (hImage, cx [0], cy [0]);
				OS.ImageList_ReplaceIcon (handle, index == count ? -1 : index, hIcon);
				OS.DestroyIcon (hIcon);
			}
			break;
		}
	}
	if (index == images.length) {
		Image [] newImages = new Image [images.length + 4];
		System.arraycopy (images, 0, newImages, 0, images.length);
		images = newImages;
	}
	images [index] = image;
	return index;
}

int addRef() {
	return ++refCount;
}

int copyBitmap (int hImage, int width, int height) {
	BITMAP bm = new BITMAP ();
	OS.GetObject (hImage, BITMAP.sizeof, bm);
	int hDC = OS.GetDC (0);
	int hdc1 = OS.CreateCompatibleDC (hDC);
	OS.SelectObject (hdc1, hImage);
	int hdc2 = OS.CreateCompatibleDC (hDC);
	int hBitmap = OS.CreateCompatibleBitmap (hDC, width, height);
	OS.SelectObject (hdc2, hBitmap);
	if (width != bm.bmWidth || height != bm.bmHeight) {
		if (!OS.IsWinCE) OS.SetStretchBltMode(hdc2, OS.COLORONCOLOR);
		OS.StretchBlt (hdc2, 0, 0, width, height, hdc1, 0, 0, bm.bmWidth, bm.bmHeight, OS.SRCCOPY);
	} else {
		OS.BitBlt (hdc2, 0, 0, width, height, hdc1, 0, 0, OS.SRCCOPY);
	}
	OS.DeleteDC (hdc1);
	OS.DeleteDC (hdc2);
	OS.ReleaseDC (0, hDC);
	return hBitmap;
}

int copyIcon (int hImage, int width, int height) {
	if (OS.IsWinCE) SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
	int hIcon = OS.CopyImage (hImage, OS.IMAGE_ICON, width, height, OS.LR_DEFAULTCOLOR);
	return hIcon != 0 ? hIcon : hImage;
}

int createMask (int hBitmap, int width, int height, int background) {
	int hMask = OS.CreateBitmap (width, height, 1, 1, null);
	int hDC = OS.GetDC (0);
	int hdc1 = OS.CreateCompatibleDC (hDC);
	if (background != -1) {
		OS.SelectObject (hdc1, hBitmap);
		int hdc2 = OS.CreateCompatibleDC (hDC);
		OS.SelectObject (hdc2, hMask);
		OS.SetBkColor (hdc1, background);
		OS.BitBlt (hdc2, 0, 0, width, height, hdc1, 0, 0, OS.SRCCOPY);
		OS.DeleteDC (hdc2);
	} else {
		int hOldBitmap = OS.SelectObject (hdc1, hMask);
		OS.PatBlt (hdc1, 0, 0, width, height, OS.BLACKNESS);
		OS.SelectObject (hdc1, hOldBitmap);
	}
	OS.ReleaseDC (0, hDC);
	OS.DeleteDC (hdc1);
	return hMask;
}

public void dispose () {
	if (handle != 0) OS.ImageList_Destroy (handle);
	handle = 0;
	images = null;
}

public Image get (int index) {
	return images [index];
}

public int getHandle () {
	return handle;
}

public Point getImageSize() {
	int [] cx = new int [1], cy = new int [1];
	OS.ImageList_GetIconSize (handle, cx, cy);
	return new Point (cx [0], cy [0]);
}

public int indexOf (Image image) {
	int count = OS.ImageList_GetImageCount (handle);
	for (int i=0; i<count; i++) {
		if (images [i] != null) {
			if (images [i].isDisposed ()) images [i] = null;
			if (images [i] != null && images [i].equals (image)) return i;
		}
	}
	return -1;
}

public void put (int index, Image image) {
	int count = OS.ImageList_GetImageCount (handle);
	if (!(0 <= index && index < count)) return;
	if (image != null) {
		int [] cx = new int [1], cy = new int [1];
		OS.ImageList_GetIconSize (handle, cx, cy);
		int hImage = image.handle;
		switch (image.type) {
			case SWT.BITMAP: {
				int background = -1;
				Color color = image.getBackground ();
				if (color != null) background = color.handle;
				int hBitmap = copyBitmap (hImage, cx [0], cy [0]);
				int hMask = createMask (hBitmap, cx [0], cy [0], background);
				OS.ImageList_Replace (handle, index, hBitmap, hMask);
				OS.DeleteObject (hBitmap);
				OS.DeleteObject (hMask);
				break;
			}
			case SWT.ICON: {
				if (OS.IsWinCE) {
					OS.ImageList_ReplaceIcon (handle, index, hImage);
				} else {
					int hIcon = copyIcon (hImage, cx [0], cy [0]);
					OS.ImageList_ReplaceIcon (handle, index, hIcon);
					OS.DestroyIcon (hIcon);
				}
				break;
			}
		}
	}
	images [index] = image;
}

public void remove (int index) {
	int count = OS.ImageList_GetImageCount (handle);
	if (!(0 <= index && index < count)) return;
	OS.ImageList_Remove (handle, index);
	System.arraycopy (images, index + 1, images, index, --count - index);
	images [index] = null;
}

int removeRef() {
	return --refCount;
}

public int size () {
	int result = 0;
	int count = OS.ImageList_GetImageCount (handle);
	for (int i=0; i<count; i++) {
		if (images [i] != null) {
			if (images [i].isDisposed ()) images [i] = null;
			if (images [i] != null) result++;
		}
	}
	return result;
}

}
