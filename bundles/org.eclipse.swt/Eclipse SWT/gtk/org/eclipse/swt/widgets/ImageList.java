/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
	if (index == images.length) {
		Image [] newImages = new Image [images.length + 4];
		System.arraycopy (images, 0, newImages, 0, images.length);
		images = newImages;
		int /*long*/ [] newPixbufs = new int /*long*/ [pixbufs.length + 4];
		System.arraycopy (pixbufs, 0, newPixbufs, 0, pixbufs.length);
		pixbufs = newPixbufs;
	}
	set (index, image);
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

public void put (int index, Image image) {
	int count = images.length;
	if (!(0 <= index && index < count)) return;
	if (image != null) {
		set (index, image);
	} else {
		images [index] = null;	
		if (pixbufs [index] != 0) OS.g_object_unref (pixbufs [index]);
		pixbufs [index] = 0;
	}
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

void set (int index, Image image) {
	int [] w = new int [1], h = new int [1];
 	OS.gdk_drawable_get_size (image.pixmap, w, h);
	int /*long*/ pixbuf = Display.createPixbuf (image);
	if (width == -1 || height == -1) {
		width = w [0];
		height = h [0];
	}
	if (w [0] != width || h [0] != height) {
		int /*long*/ scaledPixbuf = OS.gdk_pixbuf_scale_simple(pixbuf, width, height, OS.GDK_INTERP_BILINEAR);
		OS.g_object_unref (pixbuf);
		pixbuf = scaledPixbuf;
	}
	int /*long*/ oldPixbuf = pixbufs [index];
	if (oldPixbuf != 0) {
		if (images [index] == image) {
			OS.gdk_pixbuf_copy_area (pixbuf, 0, 0, width, height, oldPixbuf, 0, 0);
			OS.g_object_unref (pixbuf);
			pixbuf = oldPixbuf;
		} else {
			OS.g_object_unref (oldPixbuf);
		}
	}
	pixbufs [index] = pixbuf;
	images [index] = image;	
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
