package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

class ImageList {
	int handle, background;
	Image [] images;
	static final int CREATE_FLAGS;
	static {
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
	
public ImageList () {
	handle = OS.ImageList_Create (32, 32, CREATE_FLAGS, 16, 16);
	images = new Image [4];
	background = 0x00FFFFFF;
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
	int hImage = image.handle;
	int [] cx = new int [1], cy = new int [1];
	OS.ImageList_GetIconSize (handle, cx, cy);
	switch (image.type) {
		case SWT.BITMAP: {
			if (count == 0) {
				BITMAP bm = new BITMAP ();
				OS.GetObject (hImage, BITMAP.sizeof, bm);
				cx [0] = bm.bmWidth; cy [0] = bm.bmHeight;
				OS.ImageList_SetIconSize (handle, cx [0], cy [0]);
			}
			int hBitmap = copyBitmap (hImage, cx [0], cy [0]);
			if (index == count) {
				OS.ImageList_AddMasked (handle, hBitmap, background);
			} else {
				int hMask = createMask (hBitmap, cx [0], cy [0]);
				OS.ImageList_Replace (handle, index, hBitmap, hMask);
				OS.DeleteObject (hMask);
			}
			OS.DeleteObject (hBitmap);
			break;
		}
		case SWT.ICON: {
			if (count == 0) {
				BITMAP bm = new BITMAP ();
				ICONINFO info = new ICONINFO ();
				OS.GetIconInfo (hImage, info);
				int hBitmap = info.hbmColor;
				if (hBitmap == 0) hBitmap = info.hbmMask;
				OS.GetObject (hBitmap, BITMAP.sizeof, bm);
				if (hBitmap == info.hbmMask) bm.bmHeight /= 2;
				if (info.hbmColor != 0) OS.DeleteObject (info.hbmColor);
				if (info.hbmMask != 0) OS.DeleteObject (info.hbmMask);
				cx [0] = bm.bmWidth;  cy [0] = bm.bmHeight;
				OS.ImageList_SetIconSize (handle, cx [0], cy [0]);
			}
			int hIcon = copyIcon (hImage, cx [0], cy [0]);
			OS.ImageList_ReplaceIcon (handle, index == count ? -1 : index, hIcon);
			OS.DestroyIcon (hIcon);
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

int copyBitmap (int hImage, int width, int height) {
	BITMAP bm = new BITMAP ();
	OS.GetObject (hImage, BITMAP.sizeof, bm);
	int hDC = OS.GetDC (0);
	int hdc1 = OS.CreateCompatibleDC (hDC);
	OS.SelectObject (hdc1, hImage);
	int hdc2 = OS.CreateCompatibleDC (hDC);
	int hBitmap = OS.CreateCompatibleBitmap (hDC, width, height);
	OS.SelectObject (hdc2, hBitmap);
	OS.SetStretchBltMode(hdc2, OS.COLORONCOLOR);
	OS.StretchBlt (hdc2, 0, 0, width, height, hdc1, 0, 0, bm.bmWidth, bm.bmHeight, OS.SRCCOPY);
	OS.DeleteDC (hdc1);
	OS.DeleteDC (hdc2);
	OS.ReleaseDC (0, hDC);
	return hBitmap;
}

int copyIcon (int hImage, int width, int height) {
	int hIcon = OS.CopyImage (hImage, OS.IMAGE_ICON, width, height, OS.LR_DEFAULTCOLOR);
	return hIcon;
}

int createMask (int hBitmap, int width, int height) {
	int hDC = OS.GetDC (0);
	int hdc1 = OS.CreateCompatibleDC (hDC);
	OS.SelectObject (hdc1, hBitmap);
	int hdc2 = OS.CreateCompatibleDC (hDC);
	int hMask = OS.CreateBitmap (width, height, 1, 1, null);
	OS.SelectObject (hdc2, hMask);
	OS.SetBkColor (hdc1, background);
	OS.BitBlt (hdc2, 0, 0, width, height, hdc1, 0, 0, OS.SRCCOPY);
	OS.ReleaseDC (0, hDC);
	OS.DeleteDC (hdc1);
	OS.DeleteDC (hdc2);
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

public int getBackground () {
	return background;
}

public int getHandle () {
	return handle;
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
			case SWT.BITMAP:
				int hBitmap = copyBitmap (hImage, cx [0], cy [0]);
				int hMask = createMask (hBitmap, cx [0], cy [0]);
				OS.ImageList_Replace (handle, index, hBitmap, hMask);
				OS.DeleteObject (hBitmap);
				OS.DeleteObject (hMask);
				break;
			case SWT.ICON:
				int hIcon = copyIcon (hImage, cx [0], cy [0]);
				OS.ImageList_ReplaceIcon (handle, index, hIcon);
				OS.DestroyIcon (hIcon);
				break;
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

public void setBackground (int color) {
	if (background == color) return;
	background = color;
	/*
	* This code is intentionally commented.  When the background
	* color of the control changes, it is necessary to recompute
	* the masks for all bitmaps in the image list.  This can look
	* really bad when the application program assumes the original
	* color of the control and makes bitmaps accordingly.  Typically
	* this happens when the colors are change using the control panel.
	* Therefore, this code remains commented even though it is correct.
	*/
//	int length = OS.ImageList_GetImageCount (handle);
//	if (length == 0) return;
//	int [] cx = new int [1], cy = new int [1];
//	OS.ImageList_GetIconSize (handle, cx, cy);
//	int width = cx [0], height = cy [0];
//	OS.ImageList_Destroy (handle);
//	handle = OS.ImageList_Create (width, height, CREATE_FLAGS, length, 16);
//	for (int i=0; i<length; i++) {
//		Image image = images [i];
//		if (image == null || image.isDisposed ()) {
//			images [i] = null;
//			int hBitmap = OS.CreateBitmap (width, height, 1, 1, null);
//			OS.ImageList_AddMasked (handle, hBitmap, background);
//			OS.DeleteObject (hBitmap);
//		} else {
//			int hImage = image.handle;
//			switch (image.type) {
//				case SWT.BITMAP:
//					int hBitmap = copyBitmap (hImage, width, height);
//					OS.ImageList_AddMasked (handle, hBitmap, background);
//					OS.DeleteObject (hBitmap);
//					break;
//				case SWT.ICON:
//					int hIcon = copyIcon (hImage, width, height);
//					OS.ImageList_ReplaceIcon (handle, -1, hIcon);
//					OS.DestroyIcon (hIcon);
//					break;
//				default:
//					SWT.error (SWT.ERROR_NOT_IMPLEMENTED);
//					break;
//			}
//		}
//	}
}

public int size () {
	int result = 0;
	int count = OS.ImageList_GetImageCount (handle);
	for (int i=0; i<count; i++) {
		if (images [i] != null) {
			if (images [i].isDisposed ()) images [i] = null;
			if (images [i] == null) result++;
		}
	}
	return result;
}

}
