/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
package org.eclipse.swt.internal;

public class ImageList {
//	long handle;
//	int style, refCount;
//	Image [] images;
//	private final int zoom;
//
//public ImageList (int style, int zoom) {
//	this (style, 32, 32, zoom);
//}
//
//public ImageList (int style, int width, int height, int zoom) {
//	this.style = style;
//	int flags = OS.ILC_MASK | OS.ILC_COLOR32;
//	if ((style & SWT.RIGHT_TO_LEFT) != 0) flags |= OS.ILC_MIRROR;
//	handle = OS.ImageList_Create (width, height, flags, 16, 16);
//	images = new Image [4];
//	this.zoom = zoom;
//}
//
//public int add (Image image) {
//	int count = OS.ImageList_GetImageCount (handle);
//	int index = 0;
//	while (index < count) {
//		if (images [index] != null) {
//			if (images [index].isDisposed ()) images [index] = null;
//		}
//		if (images [index] == null) break;
//		index++;
//	}
//	if (count == 0) {
//		Rectangle rect = DPIUtil.autoScaleBounds(image.getBounds(), zoom, 100);
//		OS.ImageList_SetIconSize (handle, rect.width, rect.height);
//	}
//	set (index, image, count);
//	if (index == images.length) {
//		Image [] newImages = new Image [images.length + 4];
//		System.arraycopy (images, 0, newImages, 0, images.length);
//		images = newImages;
//	}
//	images [index] = image;
//	return index;
//}
//
//public int addRef() {
//	return ++refCount;
//}
//
//long copyBitmap (long hImage, int width, int height) {
//	BITMAP bm = new BITMAP ();
//	OS.GetObject (hImage, BITMAP.sizeof, bm);
//	long hDC = OS.GetDC (0);
//	long hdc1 = OS.CreateCompatibleDC (hDC);
//	OS.SelectObject (hdc1, hImage);
//	long hdc2 = OS.CreateCompatibleDC (hDC);
//	/*
//	* Feature in Windows.  If a bitmap has a 32-bit depth and any
//	* pixel has an alpha value different than zero, common controls
//	* version 6.0 assumes that the bitmap should be alpha blended.
//	* AlphaBlend() composes the alpha channel of a destination 32-bit
//	* depth image with the alpha channel of the source image. This
//	* may cause opaque images to draw transparently.  The fix is
//	* remove the alpha channel of opaque images by down sampling
//	* it to 24-bit depth.
//	*/
//	long hBitmap;
//	if (bm.bmBitsPixel == 32) {
//		BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
//		bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
//		bmiHeader.biWidth = width;
//		bmiHeader.biHeight = -height;
//		bmiHeader.biPlanes = 1;
//		bmiHeader.biBitCount = (short)24;
//		bmiHeader.biCompression = OS.BI_RGB;
//		byte[] bmi = new byte[BITMAPINFOHEADER.sizeof];
//		OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
//		long[] pBits = new long[1];
//		hBitmap = OS.CreateDIBSection(0, bmi, OS.DIB_RGB_COLORS, pBits, 0, 0);
//	} else {
//		hBitmap = OS.CreateCompatibleBitmap (hDC, width, height);
//	}
//	OS.SelectObject (hdc2, hBitmap);
//	if (width != bm.bmWidth || height != bm.bmHeight) {
//		OS.SetStretchBltMode(hdc2, OS.COLORONCOLOR);
//		OS.StretchBlt (hdc2, 0, 0, width, height, hdc1, 0, 0, bm.bmWidth, bm.bmHeight, OS.SRCCOPY);
//	} else {
//		OS.BitBlt (hdc2, 0, 0, width, height, hdc1, 0, 0, OS.SRCCOPY);
//	}
//	OS.DeleteDC (hdc1);
//	OS.DeleteDC (hdc2);
//	OS.ReleaseDC (0, hDC);
//	return hBitmap;
//}
//
//long copyIcon (long hImage, int width, int height) {
//	long hIcon = OS.CopyImage (hImage, OS.IMAGE_ICON, width, height, 0);
//	return hIcon != 0 ? hIcon : hImage;
//}
//
//long copyWithAlpha (long hBitmap, int background, byte[] alphaData, int destWidth, int destHeight) {
//	BITMAP bm = new BITMAP ();
//	OS.GetObject (hBitmap, BITMAP.sizeof, bm);
//	int srcWidth = bm.bmWidth;
//	int srcHeight = bm.bmHeight;
//
//	/* Create resources */
//	long hdc = OS.GetDC (0);
//	long srcHdc = OS.CreateCompatibleDC (hdc);
//	long oldSrcBitmap = OS.SelectObject (srcHdc, hBitmap);
//	long memHdc = OS.CreateCompatibleDC (hdc);
//	BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER ();
//	bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
//	bmiHeader.biWidth = srcWidth;
//	bmiHeader.biHeight = -srcHeight;
//	bmiHeader.biPlanes = 1;
//	bmiHeader.biBitCount = 32;
//	bmiHeader.biCompression = OS.BI_RGB;
//	byte []	bmi = new byte[BITMAPINFOHEADER.sizeof];
//	OS.MoveMemory (bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
//	long [] pBits = new long [1];
//	long memDib = OS.CreateDIBSection (0, bmi, OS.DIB_RGB_COLORS, pBits, 0, 0);
//	if (memDib == 0) SWT.error (SWT.ERROR_NO_HANDLES);
//	long oldMemBitmap = OS.SelectObject (memHdc, memDib);
//
//	BITMAP dibBM = new BITMAP ();
//	OS.GetObject (memDib, BITMAP.sizeof, dibBM);
//	int sizeInBytes = dibBM.bmWidthBytes * dibBM.bmHeight;
//
//	/* Get the foreground pixels */
//	OS.BitBlt (memHdc, 0, 0, srcWidth, srcHeight, srcHdc, 0, 0, OS.SRCCOPY);
//	byte[] srcData = new byte [sizeInBytes];
//	OS.MoveMemory (srcData, dibBM.bmBits, sizeInBytes);
//
//	/* Merge the alpha channel in place */
//	if (alphaData != null) {
//		int spinc = dibBM.bmWidthBytes - srcWidth * 4;
//		int ap = 0, sp = 0;
//		for (int y = 0; y < srcHeight; ++y) {
//			for (int x = 0; x < srcWidth; ++x) {
//				int a = alphaData [ap++] & 0xFF;
//				if (a != 0) {
//					srcData [sp    ] = (byte)((((srcData [sp    ] & 0xFF) * 0xFF) + a / 2) / a);
//					srcData [sp + 1] = (byte)((((srcData [sp + 1] & 0xFF) * 0xFF) + a / 2) / a);
//					srcData [sp + 2] = (byte)((((srcData [sp + 2] & 0xFF) * 0xFF) + a / 2) / a);
//				}
//				srcData [sp + 3] = (byte)a;
//				sp += 4;
//			}
//			sp += spinc;
//		}
//	} else {
//		byte transRed = (byte)(background & 0xFF);
//		byte transGreen = (byte)((background >> 8) & 0xFF);
//		byte transBlue = (byte)((background >> 16) & 0xFF);
//		final int spinc = dibBM.bmWidthBytes - srcWidth * 4;
//		int sp = 3;
//		for (int y = 0; y < srcHeight; ++y) {
//			for (int x = 0; x < srcWidth; ++x) {
//				srcData [sp] = (srcData[sp-1] == transRed && srcData[sp-2] == transGreen && srcData[sp-3] == transBlue) ? 0 : (byte)255;
//				sp += 4;
//			}
//			sp += spinc;
//		}
//	}
//	OS.MoveMemory (dibBM.bmBits, srcData, sizeInBytes);
//
//	/* Stretch and free resources */
//	if (srcWidth != destWidth || srcHeight != destHeight) {
//		BITMAPINFOHEADER bmiHeader2 = new BITMAPINFOHEADER ();
//		bmiHeader2.biSize = BITMAPINFOHEADER.sizeof;
//		bmiHeader2.biWidth = destWidth;
//		bmiHeader2.biHeight = -destHeight;
//		bmiHeader2.biPlanes = 1;
//		bmiHeader2.biBitCount = 32;
//		bmiHeader2.biCompression = OS.BI_RGB;
//		byte []	bmi2 = new byte[BITMAPINFOHEADER.sizeof];
//		OS.MoveMemory (bmi2, bmiHeader2, BITMAPINFOHEADER.sizeof);
//		long [] pBits2 = new long [1];
//		long memDib2 = OS.CreateDIBSection (0, bmi2, OS.DIB_RGB_COLORS, pBits2, 0, 0);
//		long memHdc2 = OS.CreateCompatibleDC (hdc);
//		long oldMemBitmap2 = OS.SelectObject (memHdc2, memDib2);
//		OS.SetStretchBltMode(memHdc2, OS.COLORONCOLOR);
//		OS.StretchBlt (memHdc2, 0, 0, destWidth, destHeight, memHdc, 0, 0, srcWidth, srcHeight, OS.SRCCOPY);
//		OS.SelectObject (memHdc2, oldMemBitmap2);
//		OS.DeleteDC (memHdc2);
//		OS.SelectObject (memHdc, oldMemBitmap);
//		OS.DeleteDC (memHdc);
//		OS.DeleteObject (memDib);
//		memDib = memDib2;
//	} else {
//		OS.SelectObject (memHdc, oldMemBitmap);
//		OS.DeleteDC (memHdc);
//	}
//	OS.SelectObject (srcHdc, oldSrcBitmap);
//	OS.DeleteDC (srcHdc);
//	OS.ReleaseDC (0, hdc);
//	return memDib;
//}
//
//long createMaskFromAlpha (ImageData data, int destWidth, int destHeight) {
//	int srcWidth = data.width;
//	int srcHeight = data.height;
//	ImageData mask = ImageData.internal_new (srcWidth, srcHeight, 1,
//			new PaletteData(new RGB [] {new RGB (0, 0, 0), new RGB (0xff, 0xff, 0xff)}),
//			2, null, 1, null, null, -1, -1, -1, 0, 0, 0, 0);
//	int ap = 0;
//	for (int y = 0; y < mask.height; y++) {
//		for (int x = 0; x < mask.width; x++) {
//			mask.setPixel (x, y, (data.alphaData [ap++] & 0xff) <= 127 ? 1 : 0);
//		}
//	}
//	long hMask = OS.CreateBitmap (srcWidth, srcHeight, 1, 1, mask.data);
//	if (srcWidth != destWidth || srcHeight != destHeight) {
//		long hdc = OS.GetDC (0);
//		long hdc1 = OS.CreateCompatibleDC (hdc);
//		OS.SelectObject (hdc1, hMask);
//		long hdc2 = OS.CreateCompatibleDC (hdc);
//		long hMask2 = OS.CreateBitmap (destWidth, destHeight, 1, 1, null);
//		OS.SelectObject (hdc2, hMask2);
//		OS.SetStretchBltMode(hdc2, OS.COLORONCOLOR);
//		OS.StretchBlt (hdc2, 0, 0, destWidth, destHeight, hdc1, 0, 0, srcWidth, srcHeight, OS.SRCCOPY);
//		OS.DeleteDC (hdc1);
//		OS.DeleteDC (hdc2);
//		OS.ReleaseDC (0, hdc);
//		OS.DeleteObject(hMask);
//		hMask = hMask2;
//	}
//	return hMask;
//}
//
//long createMask (long hBitmap, int destWidth, int destHeight, int background, int transparentPixel) {
//	BITMAP bm = new BITMAP ();
//	OS.GetObject (hBitmap, BITMAP.sizeof, bm);
//	int srcWidth = bm.bmWidth;
//	int srcHeight = bm.bmHeight;
//	long hMask = OS.CreateBitmap (destWidth, destHeight, 1, 1, null);
//	long hDC = OS.GetDC (0);
//	long hdc1 = OS.CreateCompatibleDC (hDC);
//	if (background != -1) {
//		OS.SelectObject (hdc1, hBitmap);
//
//		/*
//		* If the image has a palette with multiple entries having
//		* the same color and one of those entries is the transparentPixel,
//		* only the first entry becomes transparent. To avoid this
//		* problem, temporarily change the image palette to a palette
//		* where the transparentPixel is white and everything else is
//		* black.
//		*/
//		boolean isDib = bm.bmBits != 0;
//		byte[] originalColors = null;
//		if (transparentPixel != -1 && isDib && bm.bmBitsPixel <= 8) {
//			int maxColors = 1 << bm.bmBitsPixel;
//			byte[] oldColors = new byte[maxColors * 4];
//			OS.GetDIBColorTable(hdc1, 0, maxColors, oldColors);
//			int offset = transparentPixel * 4;
//			byte[] newColors = new byte[oldColors.length];
//			newColors[offset] = (byte)0xFF;
//			newColors[offset+1] = (byte)0xFF;
//			newColors[offset+2] = (byte)0xFF;
//			OS.SetDIBColorTable(hdc1, 0, maxColors, newColors);
//			originalColors = oldColors;
//			OS.SetBkColor (hdc1, 0xFFFFFF);
//		} else {
//			OS.SetBkColor (hdc1, background);
//		}
//
//		long hdc2 = OS.CreateCompatibleDC (hDC);
//		OS.SelectObject (hdc2, hMask);
//		if (destWidth != srcWidth || destHeight != srcHeight) {
//			OS.SetStretchBltMode (hdc2, OS.COLORONCOLOR);
//			OS.StretchBlt (hdc2, 0, 0, destWidth, destHeight, hdc1, 0, 0, srcWidth, srcHeight, OS.SRCCOPY);
//		} else {
//			OS.BitBlt (hdc2, 0, 0, destWidth, destHeight, hdc1, 0, 0, OS.SRCCOPY);
//		}
//		OS.DeleteDC (hdc2);
//
//		/* Put back the original palette */
//		if (originalColors != null) OS.SetDIBColorTable(hdc1, 0, 1 << bm.bmBitsPixel, originalColors);
//	} else {
//		long hOldBitmap = OS.SelectObject (hdc1, hMask);
//		OS.PatBlt (hdc1, 0, 0, destWidth, destHeight, OS.BLACKNESS);
//		OS.SelectObject (hdc1, hOldBitmap);
//	}
//	OS.ReleaseDC (0, hDC);
//	OS.DeleteDC (hdc1);
//	return hMask;
//}
//
public void dispose () {
//	if (handle != 0) OS.ImageList_Destroy (handle);
//	handle = 0;
//	images = null;
}
//
//public Image get (int index) {
//	return images [index];
//}
//
//public int getStyle () {
//	return style;
//}
//
//public long getHandle () {
//	return handle;
//}
//
//public Point getImageSize() {
//	int [] cx = new int [1], cy = new int [1];
//	OS.ImageList_GetIconSize (handle, cx, cy);
//	return new Point (cx [0], cy [0]);
//}
//
//public int indexOf (Image image) {
//	int count = OS.ImageList_GetImageCount (handle);
//	for (int i=0; i<count; i++) {
//		if (images [i] != null) {
//			if (images [i].isDisposed ()) images [i] = null;
//			if (images [i] != null && images [i].equals (image)) return i;
//		}
//	}
//	return -1;
//}
//
//public void put (int index, Image image) {
//	if ((0 <= index && index < images.length) && (images [index] == image)) return;
//	int count = OS.ImageList_GetImageCount (handle);
//	if (!(0 <= index && index < count)) return;
//	if (image != null) set(index, image, count);
//	images [index] = image;
//}
//
//public void remove (int index) {
//	int count = OS.ImageList_GetImageCount (handle);
//	if (!(0 <= index && index < count)) return;
//	OS.ImageList_Remove (handle, index);
//	System.arraycopy (images, index + 1, images, index, --count - index);
//	images [index] = null;
//}
//
//public int removeRef() {
//	return --refCount;
//}
//
//void set (int index, Image image, int count) {
//	long hImage = Image.win32_getHandle(image, zoom);
//	int [] cx = new int [1], cy = new int [1];
//	OS.ImageList_GetIconSize (handle, cx, cy);
//	switch (image.type) {
//		case SWT.BITMAP: {
//			/*
//			* Note that the image size has to match the image list icon size.
//			*/
//			long hBitmap = 0, hMask = 0;
//			ImageData data = image.getImageData(zoom);
//			switch (data.getTransparencyType ()) {
//				case SWT.TRANSPARENCY_ALPHA:
//					/*
//					 * Fully transparent image is rendered as a black image, so such
//					 * image needs to be rendered using ImageData mask approach.
//					 * Refer bug 426247
//					 *
//					 * TODO: Explore using createMaskFromAlpha() method even
//					 * for newer versions of the COMCTL32 library.
//					 */
//					boolean fullyTransparent = true;
//					if (data.alphaData == null) {
//						fullyTransparent = false;
//					}
//					else {
//						for (byte alphaData : data.alphaData) {
//							if (alphaData != 0) {
//								fullyTransparent = false;
//								break;
//							}
//						}
//					}
//					if (!fullyTransparent) {
//						hBitmap = copyWithAlpha (hImage, -1, data.alphaData, cx [0], cy [0]);
//					} else {
//						hBitmap = copyBitmap (hImage, cx [0], cy [0]);
//						hMask = createMaskFromAlpha (data, cx [0], cy [0]);
//					}
//					break;
//				case SWT.TRANSPARENCY_PIXEL:
//					int background = -1;
//					Color color = image.getBackground ();
//					if (color != null) background = color.handle;
//					hBitmap = copyBitmap (hImage, cx [0], cy [0]);
//					hMask = createMask (hImage, cx [0], cy [0], background, data.transparentPixel);
//					break;
//				case SWT.TRANSPARENCY_NONE:
//				default:
//					hBitmap = copyBitmap (hImage, cx [0], cy [0]);
//					if (index != count) hMask = createMask (hImage, cx [0], cy [0], -1, -1);
//					break;
//			}
//			if (index == count) {
//				OS.ImageList_Add (handle, hBitmap, hMask);
//			} else {
//				/* Note that the mask must always be replaced even for TRANSPARENCY_NONE */
//				OS.ImageList_Replace (handle, index, hBitmap, hMask);
//			}
//			if (hMask != 0) OS.DeleteObject (hMask);
//			if (hBitmap != hImage) OS.DeleteObject (hBitmap);
//			break;
//		}
//		case SWT.ICON: {
//			long hIcon = copyIcon (hImage, cx [0], cy [0]);
//			OS.ImageList_ReplaceIcon (handle, index == count ? -1 : index, hIcon);
//			OS.DestroyIcon (hIcon);
//			break;
//		}
//	}
//}
//
//public int size () {
//	int result = 0;
//	int count = OS.ImageList_GetImageCount (handle);
//	for (int i=0; i<count; i++) {
//		if (images [i] != null) {
//			if (images [i].isDisposed ()) images [i] = null;
//			if (images [i] != null) result++;
//		}
//	}
//	return result;
//}
//
}
