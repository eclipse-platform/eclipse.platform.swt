/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
	long /*int*/ [] pixbufs;
	int width = -1, height = -1;
	Image [] images;
	
public ImageList() {
	images = new Image [4];
	pixbufs = new long /*int*/ [4];
}

public static long /*int*/ convertSurface(Image image) {
	long /*int*/ newSurface = image.surface;
	int type = Cairo.cairo_surface_get_type(newSurface);
	if (type != Cairo.CAIRO_SURFACE_TYPE_IMAGE) {
		Rectangle bounds = image.getBounds();
		int format = Cairo.cairo_surface_get_content(newSurface) == Cairo.CAIRO_CONTENT_COLOR ? Cairo.CAIRO_FORMAT_RGB24 : Cairo.CAIRO_FORMAT_ARGB32;
		newSurface = Cairo.cairo_image_surface_create(format, bounds.width, bounds.height);
		if (newSurface == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		long /*int*/ cairo = Cairo.cairo_create(newSurface);
		if (cairo == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		Cairo.cairo_set_operator(cairo, Cairo.CAIRO_OPERATOR_SOURCE);
		Cairo.cairo_set_source_surface (cairo, image.surface, 0, 0);
		Cairo.cairo_paint (cairo);
		Cairo.cairo_destroy(cairo);
	} else {
		Cairo.cairo_surface_reference(newSurface);
	}
	return newSurface;
}

public static long /*int*/ createPixbuf(Image image) {
	long /*int*/ pixbuf;
	if (OS.USE_CAIRO) {
		long /*int*/ surface = convertSurface(image);
		int format = Cairo.cairo_image_surface_get_format(surface);
		int width = Cairo.cairo_image_surface_get_width(surface);
		int height = Cairo.cairo_image_surface_get_height(surface);
		boolean hasAlpha = format == Cairo.CAIRO_FORMAT_ARGB32;
		pixbuf = OS.gdk_pixbuf_new (OS.GDK_COLORSPACE_RGB, hasAlpha, 8, width, height);
		if (pixbuf == 0) SWT.error (SWT.ERROR_NO_HANDLES);
		int stride = OS.gdk_pixbuf_get_rowstride (pixbuf);
		long /*int*/ pixels = OS.gdk_pixbuf_get_pixels (pixbuf);
		int oa, or, og, ob;
		if (OS.BIG_ENDIAN) {
			oa = 0; or = 1; og = 2; ob = 3;
		} else {
			oa = 3; or = 2; og = 1; ob = 0;
		}
		byte[] line = new byte[stride];
		long /*int*/ surfaceData = Cairo.cairo_image_surface_get_data(surface);
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
						line[offset + 0] = (byte)(((r * 0xFF) + a / 2) / a);
						line[offset + 1] = (byte)(((g * 0xFF) + a / 2) / a);
						line[offset + 2] = (byte)(((b * 0xFF) + a / 2) / a);
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
		Cairo.cairo_surface_destroy(surface);
	} else {
		int [] w = new int [1], h = new int [1];
		if (OS.GTK_VERSION >= OS.VERSION(2, 24, 0)) {
			OS.gdk_pixmap_get_size(image.pixmap, w, h);
		} else {
			OS.gdk_drawable_get_size (image.pixmap, w, h);
		}
		long /*int*/ colormap = OS.gdk_colormap_get_system ();
		boolean hasMask = image.mask != 0 && OS.gdk_drawable_get_depth (image.mask) == 1;
		if (hasMask) {
			pixbuf = OS.gdk_pixbuf_new (OS.GDK_COLORSPACE_RGB, true, 8, w [0], h [0]);
			if (pixbuf == 0) SWT.error (SWT.ERROR_NO_HANDLES);
			gdk_pixbuf_get_from_window (pixbuf, image.pixmap, colormap, 0, 0, 0, 0, w [0], h [0]);
			long /*int*/ maskPixbuf = OS.gdk_pixbuf_new(OS.GDK_COLORSPACE_RGB, false, 8, w [0], h [0]);
			if (maskPixbuf == 0) SWT.error (SWT.ERROR_NO_HANDLES);
			gdk_pixbuf_get_from_window (maskPixbuf, image.mask, 0, 0, 0, 0, 0, w [0], h [0]);
			int stride = OS.gdk_pixbuf_get_rowstride(pixbuf);
			long /*int*/ pixels = OS.gdk_pixbuf_get_pixels(pixbuf);
			byte[] line = new byte[stride];
			int maskStride = OS.gdk_pixbuf_get_rowstride(maskPixbuf);
			long /*int*/ maskPixels = OS.gdk_pixbuf_get_pixels(maskPixbuf);
			byte[] maskLine = new byte[maskStride];
			for (int y=0; y<h[0]; y++) {
				long /*int*/ offset = pixels + (y * stride);
				OS.memmove(line, offset, stride);
				long /*int*/ maskOffset = maskPixels + (y * maskStride);
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
			gdk_pixbuf_get_from_window (pixbuf, image.pixmap, colormap, 0, 0, 0, 0, w [0], h [0]);
			if (hasAlpha) {
				byte [] alpha = data.alphaData;
				int stride = OS.gdk_pixbuf_get_rowstride (pixbuf);
				long /*int*/ pixels = OS.gdk_pixbuf_get_pixels (pixbuf);
				byte [] line = new byte [stride];
				for (int y = 0; y < h [0]; y++) {
					long /*int*/ offset = pixels + (y * stride);
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
		long /*int*/ [] newPixbufs = new long /*int*/ [pixbufs.length + 4];
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

public long /*int*/ getPixbuf (int index) {
	return pixbufs [index];
}

public int indexOf (Image image) {
	if (image == null) return -1;
	for (int index=0; index<images.length; index++) {
		if (image == images [index]) return index;
	}
	return -1;
}

public int indexOf (long /*int*/ pixbuf) {
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
	long /*int*/ pixbuf = createPixbuf (image);
	int w = OS.gdk_pixbuf_get_width(pixbuf);
	int h = OS.gdk_pixbuf_get_height(pixbuf);
	if (width == -1 || height == -1) {
		width = w;
		height = h;
	}
	if (w != width || h != height) {
		long /*int*/ scaledPixbuf = OS.gdk_pixbuf_scale_simple(pixbuf, width, height, OS.GDK_INTERP_BILINEAR);
		OS.g_object_unref (pixbuf);
		pixbuf = scaledPixbuf;
	}
	long /*int*/ oldPixbuf = pixbufs [index];
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

static long /*int*/ gdk_pixbuf_get_from_window (long /*int*/ dest, long /*int*/ src, long /*int*/ cmap, int src_x, int src_y, int dest_x, int dest_y, int width, int height) {
	if (OS.GTK_VERSION >= OS.VERSION(3, 0, 0)) {
		return OS.gdk_pixbuf_get_from_window (dest, src_x, src_y, width, height);
	} else {
		return OS.gdk_pixbuf_get_from_drawable (dest, src, cmap, src_x, src_y, dest_x, dest_y, width, height);
	}
}
}
