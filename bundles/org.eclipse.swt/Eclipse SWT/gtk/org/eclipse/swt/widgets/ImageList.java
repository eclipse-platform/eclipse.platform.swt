/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

class ImageList {
	int /*long*/ [] pixbufs;
	int width = -1, height = -1;
	Image [] images;
	
public ImageList() {
	images = new Image [4];
	pixbufs = new int /*long*/ [4];
}

public int add (Image image) {
	int index = 0;
	while (index < images.length) {
		if (images [index] != null) {
			if (images [index].isDisposed ()) {
				OS.g_object_unref (pixbufs [index]);
				images [index] = null;
				pixbufs [index] = 0;
			}
		}
		if (images [index] == null) break;
		index++;
	}
	int [] w = new int [1], h = new int [1];
 	OS.gdk_drawable_get_size (image.pixmap, w, h);
	int /*long*/ colormap = OS.gdk_colormap_get_system ();
	int /*long*/ pixbuf;
	boolean hasMask = image.mask != 0;
	if (hasMask) {
		pixbuf = OS.gdk_pixbuf_new (OS.GDK_COLORSPACE_RGB, true, 8, w [0], h [0]);
		if (pixbuf == 0) SWT.error (SWT.ERROR_NO_HANDLES);
		OS.gdk_pixbuf_get_from_drawable (pixbuf, image.pixmap, colormap, 0, 0, 0, 0, w [0], h [0]);
		int /*long*/ gdkMaskImagePtr = OS.gdk_drawable_get_image (image.mask, 0, 0, w [0], h [0]);
		if (gdkMaskImagePtr == 0) SWT.error (SWT.ERROR_NO_HANDLES);
		int stride = OS.gdk_pixbuf_get_rowstride (pixbuf);
		int /*long*/ pixels = OS.gdk_pixbuf_get_pixels (pixbuf);
		byte [] line = new byte [stride];
		for (int y = 0; y < h [0]; y++) {
			int /*long*/ offset = pixels + (y * stride);
			OS.memmove (line, offset, stride);
			for (int x = 0; x < w [0]; x++) {
				if (OS.gdk_image_get_pixel (gdkMaskImagePtr, x, y) == 0) {
					line [x*4+3] = 0;
				}
			}
			OS.memmove (offset, line, stride);
		}
		OS.g_object_unref (gdkMaskImagePtr);
	} else {
		ImageData data = image.getImageData ();
		boolean hasAlpha = data.getTransparencyType () == SWT.TRANSPARENCY_ALPHA;
		pixbuf = OS.gdk_pixbuf_new (OS.GDK_COLORSPACE_RGB, hasAlpha, 8, w [0], h [0]);
		if (pixbuf == 0) SWT.error (SWT.ERROR_NO_HANDLES);
		OS.gdk_pixbuf_get_from_drawable (pixbuf, image.pixmap, colormap, 0, 0, 0, 0, w [0], h [0]);
		if (hasAlpha) {
			byte [] alpha = data.alphaData;
			int stride = OS.gdk_pixbuf_get_rowstride (pixbuf);
			int /*long*/ pixels = OS.gdk_pixbuf_get_pixels (pixbuf);
			byte [] line = new byte [stride];
			for (int y = 0; y < h [0]; y++) {
				int /*long*/ offset = pixels + (y * stride);
				OS.memmove (line, offset, stride);
				for (int x = 0; x<w [0]; x++) {
					line [x*4+3] = alpha [y*h [0]+x];
				}
				OS.memmove (offset, line, stride);
			}
		}
	}
	if (width == -1 || height == -1) {
		width = w [0];
		height = h [0];
	}
	if (w [0] != width || h [0] != height) {
		int /*long*/ scaledPixbuf = OS.gdk_pixbuf_scale_simple(pixbuf, width, height, OS.GDK_INTERP_BILINEAR);
		OS.g_object_unref (pixbuf);
		pixbuf = scaledPixbuf;
	}
	if (index == images.length) {
		Image [] newImages = new Image [images.length + 4];
		System.arraycopy (images, 0, newImages, 0, images.length);
		images = newImages;
		int /*long*/ [] newPixbufs = new int /*long*/ [pixbufs.length + 4];
		System.arraycopy (pixbufs, 0, newPixbufs, 0, pixbufs.length);
		pixbufs = newPixbufs;
	}
	pixbufs [index] = pixbuf;
	images [index] = image;
	return index;
}

public void dispose () {
	if (pixbufs == null) return;
	for (int index=0; index<pixbufs.length; index++) {
		if (pixbufs [index] != 0) OS.g_object_unref (pixbufs [index]);
	}
	images = null;
	pixbufs = null;
}

public Image get (int index) {
	return images [index];
}

int /*long*/ getPixbuf (int index) {
	return pixbufs [index];
}

public int indexOf (Image image) {
	if (image == null) return -1;
	for (int index=0; index<images.length; index++) {
		if (image == images [index]) return index;
	}
	return -1;
}

int indexOf (int /*long*/ pixbuf) {
	if (pixbuf == 0) return -1;
	for (int index=0; index<images.length; index++) {
		if (pixbuf == pixbufs [index]) return index;
	}
	return -1;
}

public boolean isDisposed () {
	return images == null;
}

public void remove (Image image) {
	if (image == null) return;
	for (int index=0; index<images.length; index++) {
		if (image == images [index]){
			OS.g_object_unref (pixbufs [index]);
			images [index] = null;
			pixbufs [index] = 0;
		}
	}
}

public int size () {
	int result = 0;
	for (int index=0; index<images.length; index++) {
		if (images [index] != null) {
			if (images [index].isDisposed ()) {
				OS.g_object_unref (pixbufs [index]);
				images [index] = null;
				pixbufs [index] = 0;
			}
			if (images [index] != null) result++;
		}
	}
	return result;
}

}
