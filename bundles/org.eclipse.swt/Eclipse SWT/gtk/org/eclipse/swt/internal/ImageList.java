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
package org.eclipse.swt.internal;


import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;

public class ImageList {
	int /*long*/ [] pixbufs;
	int width = -1, height = -1;
	Image [] images;
	
public ImageList() {
	images = new Image [4];
	pixbufs = new int /*long*/ [4];
}

public static int /*long*/ createPixbuf(Image image) {
	int /*long*/ pixbuf;
	if (OS.USE_CAIRO) {
		int /*long*/ surface = image.surface;
		int format = Cairo.cairo_image_surface_get_format(surface);
		int width = Cairo.cairo_image_surface_get_width(surface);
		int height = Cairo.cairo_image_surface_get_height(surface);
		boolean hasAlpha = format == Cairo.CAIRO_FORMAT_ARGB32;
		pixbuf = OS.gdk_pixbuf_new (OS.GDK_COLORSPACE_RGB, hasAlpha, 8, width, height);
		if (pixbuf == 0) SWT.error (SWT.ERROR_NO_HANDLES);
		int stride = OS.gdk_pixbuf_get_rowstride (pixbuf);
		int /*long*/ pixels = OS.gdk_pixbuf_get_pixels (pixbuf);
		int oa, or, og, ob;
		if (OS.BIG_ENDIAN) {
			oa = 0; or = 1; og = 2; ob = 3;
		} else {
			oa = 3; or = 2; og = 1; ob = 0;
		}
		byte[] line = new byte[stride];
		int /*long*/ surfaceData = Cairo.cairo_image_surface_get_data(surface);
		if (hasAlpha) {
			for (int y = 0; y < height; y++) {
				OS.memmove (line, surfaceData + (y * stride), stride);
				for (int x = 0, offset = 0; x < width; x++, offset += 4) {
					int a = line[offset + oa] & 0xFF;
					int r = line[offset + or] & 0xFF;
					int g = line[offset + og] & 0xFF;
					int b = line[offset + ob] & 0xFF;
					line[offset + 3] = (byte)a;
					if (a != 0) {
						//TODO write this without floating point math
						line[offset + 0] = (byte)(((r) / (float)a) * 0xFF);
						line[offset + 1] = (byte)(((g) / (float)a) * 0xFF);
						line[offset + 2] = (byte)(((b) / (float)a) * 0xFF);
					}
				}
				OS.memmove (pixels + (y * stride), line, stride);
			}
		} else {
			int cairoStride = Cairo.cairo_image_surface_get_stride(surface);
			byte[] cairoLine = new byte[cairoStride];
			for (int y = 0; y < height; y++) {
				OS.memmove (cairoLine, surfaceData + (y * cairoStride), cairoStride);
				for (int x = 0, offset = 0, cairoOffset = 0; x < width; x++, offset += 3, cairoOffset += 4) {
					byte r = cairoLine[cairoOffset + or];
					byte g = cairoLine[cairoOffset + og];
					byte b = cairoLine[cairoOffset + ob];
					line[offset + 0] = r;
					line[offset + 1] = g;
					line[offset + 2] = b;
				}
				OS.memmove (pixels + (y * stride), line, stride);
			}
		}
	} else {
		int [] w = new int [1], h = new int [1];
	 	OS.gdk_drawable_get_size (image.pixmap, w, h);
		int /*long*/ colormap = OS.gdk_colormap_get_system ();
		boolean hasMask = image.mask != 0 && OS.gdk_drawable_get_depth (image.mask) == 1;
		if (hasMask) {
			pixbuf = OS.gdk_pixbuf_new (OS.GDK_COLORSPACE_RGB, true, 8, w [0], h [0]);
			if (pixbuf == 0) SWT.error (SWT.ERROR_NO_HANDLES);
			OS.gdk_pixbuf_get_from_drawable (pixbuf, image.pixmap, colormap, 0, 0, 0, 0, w [0], h [0]);
			int /*long*/ maskPixbuf = OS.gdk_pixbuf_new(OS.GDK_COLORSPACE_RGB, false, 8, w [0], h [0]);
			if (maskPixbuf == 0) SWT.error (SWT.ERROR_NO_HANDLES);
			OS.gdk_pixbuf_get_from_drawable(maskPixbuf, image.mask, 0, 0, 0, 0, 0, w [0], h [0]);
			int stride = OS.gdk_pixbuf_get_rowstride(pixbuf);
			int /*long*/ pixels = OS.gdk_pixbuf_get_pixels(pixbuf);
			byte[] line = new byte[stride];
			int maskStride = OS.gdk_pixbuf_get_rowstride(maskPixbuf);
			int /*long*/ maskPixels = OS.gdk_pixbuf_get_pixels(maskPixbuf);
			byte[] maskLine = new byte[maskStride];
			for (int y=0; y<h[0]; y++) {
				int /*long*/ offset = pixels + (y * stride);
				OS.memmove(line, offset, stride);
				int /*long*/ maskOffset = maskPixels + (y * maskStride);
				OS.memmove(maskLine, maskOffset, maskStride);
				for (int x=0; x<w[0]; x++) {
					if (maskLine[x * 3] == 0) {
						line[x * 4 + 3] = 0;
					}
				}
				OS.memmove(offset, line, stride);
			}
			OS.g_object_unref(maskPixbuf);
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
					for (int x = 0; x < w [0]; x++) {
						line [x*4+3] = alpha [y*w [0]+x];
					}
					OS.memmove (offset, line, stride);
				}
			}
		}
	}
	return pixbuf;
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

public int /*long*/ getPixbuf (int index) {
	return pixbufs [index];
}

public int indexOf (Image image) {
	if (image == null) return -1;
	for (int index=0; index<images.length; index++) {
		if (image == images [index]) return index;
	}
	return -1;
}

public int indexOf (int /*long*/ pixbuf) {
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
	int /*long*/ pixbuf = createPixbuf (image);
	int w = OS.gdk_pixbuf_get_width(pixbuf);
	int h = OS.gdk_pixbuf_get_height(pixbuf);
	if (width == -1 || height == -1) {
		width = w;
		height = h;
	}
	if (w != width || h != height) {
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
