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


import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

class ImageList {
	int [] pixbufs;
	Image [] images;
	
public ImageList() {
	images = new Image [4];
	pixbufs = new int [4];
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
	int width = w [0], height = h [0]; 	
	boolean hasMask = image.mask != 0;
	int pixbuf = OS.gdk_pixbuf_new (OS.GDK_COLORSPACE_RGB, hasMask, 8, width, height);
	if (pixbuf == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	int colormap = OS.gdk_colormap_get_system ();
	OS.gdk_pixbuf_get_from_drawable (pixbuf, image.pixmap, colormap, 0, 0, 0, 0, width, height);
	if (hasMask) {
		int gdkMaskImagePtr = OS.gdk_drawable_get_image (image.mask, 0, 0, width, height);
		if (gdkMaskImagePtr == 0) SWT.error (SWT.ERROR_NO_HANDLES);
		int stride = OS.gdk_pixbuf_get_rowstride (pixbuf);
		int pixels = OS.gdk_pixbuf_get_pixels (pixbuf);
		byte [] line = new byte [stride];
		for (int y=0; y<height; y++) {
			int offset = pixels + (y * stride);
			OS.memmove (line, offset, stride);
			for (int x=0; x<width; x++) {
				if (OS.gdk_image_get_pixel (gdkMaskImagePtr, x, y) == 0) {
					line[x*4+3] = 0;
				}
			}
			OS.memmove (offset, line, stride);
		}
		OS.g_object_unref (gdkMaskImagePtr);
	}
	if (index == images.length) {
		Image [] newImages = new Image [images.length + 4];
		System.arraycopy (images, 0, newImages, 0, images.length);
		images = newImages;
		int [] newPixbufs = new int [pixbufs.length + 4];
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

int getPixbuf (int index) {
	return pixbufs [index];
}

public int indexOf (Image image) {
	if (image == null) return -1;
	for (int index=0; index<images.length; index++) {
		if (image == images [index]) return index;
	}
	return -1;
}

int indexOf (int pixbuf) {
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
