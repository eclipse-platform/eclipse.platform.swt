/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.gtk.*;

public class ImageList {
	long [] surfaces;
	int width = -1, height = -1;
	Image [] images;

public ImageList() {
	images = new Image [4];
	surfaces = new long [4];
}

public static long convertSurface(Image image) {
	long newSurface = image.surface;
	int type = Cairo.cairo_surface_get_type(newSurface);
	if (type != Cairo.CAIRO_SURFACE_TYPE_IMAGE) {
		Rectangle bounds;
		if (DPIUtil.useCairoAutoScale()) {
			bounds = image.getBounds();
		} else {
			bounds = image.getBoundsInPixels();
		}
		int format = Cairo.cairo_surface_get_content(newSurface) == Cairo.CAIRO_CONTENT_COLOR ? Cairo.CAIRO_FORMAT_RGB24 : Cairo.CAIRO_FORMAT_ARGB32;
		newSurface = Cairo.cairo_image_surface_create(format, bounds.width, bounds.height);
		if (newSurface == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		//retain device scale set in the original surface
		if (DPIUtil.useCairoAutoScale()) {
			double sx[] = new double[1];
			double sy[] = new double[1];
			Cairo.cairo_surface_get_device_scale(image.surface, sx, sy);
			if (sx[0] == 0 || sy[0] == 0){
				sx[0] = sy[0] = DPIUtil.getDeviceZoom() / 100f;
			}
			Cairo.cairo_surface_set_device_scale(newSurface, sx[0], sy[0]);
		}
		long cairo = Cairo.cairo_create(newSurface);
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

public static long createPixbuf(long surface) {
	int format = Cairo.cairo_image_surface_get_format(surface);
	int width = Cairo.cairo_image_surface_get_width(surface);
	int height = Cairo.cairo_image_surface_get_height(surface);
	boolean hasAlpha = format == Cairo.CAIRO_FORMAT_ARGB32;
	long pixbuf = GDK.gdk_pixbuf_new (GDK.GDK_COLORSPACE_RGB, hasAlpha, 8, width, height);
	if (pixbuf == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	int stride = GDK.gdk_pixbuf_get_rowstride (pixbuf);
	long pixels = GDK.gdk_pixbuf_get_pixels (pixbuf);
	int oa, or, og, ob;
	if (OS.BIG_ENDIAN) {
		oa = 0; or = 1; og = 2; ob = 3;
	} else {
		oa = 3; or = 2; og = 1; ob = 0;
	}
	byte[] line = new byte[stride];
	long surfaceData = Cairo.cairo_image_surface_get_data(surface);
	if (hasAlpha) {
		for (int y = 0; y < height; y++) {
			C.memmove (line, surfaceData + (y * stride), stride);
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
			C.memmove (pixels + (y * stride), line, stride);
		}
	} else {
		int cairoStride = Cairo.cairo_image_surface_get_stride(surface);
		byte[] cairoLine = new byte[cairoStride];
		for (int y = 0; y < height; y++) {
			C.memmove (cairoLine, surfaceData + (y * cairoStride), cairoStride);
			for (int x = 0, offset = 0, cairoOffset = 0; x < width; x++, offset += 3, cairoOffset += 4) {
				byte r = cairoLine[cairoOffset + or];
				byte g = cairoLine[cairoOffset + og];
				byte b = cairoLine[cairoOffset + ob];
				line[offset + 0] = r;
				line[offset + 1] = g;
				line[offset + 2] = b;
			}
			C.memmove (pixels + (y * stride), line, stride);
		}
	}
	/*
	 * At this point the new pixbuf is created with the same size as surface.
	 * if the surface has higher device scale we need to down size pixbuf accordingly
	 *
	 * We have to do this as surface has inherent auto scaling capability but pixbuf doesnot
	 */
	if (DPIUtil.useCairoAutoScale()) {
		double sx[] = new double[1];
		double sy[] = new double[1];
		Cairo.cairo_surface_get_device_scale(surface, sx, sy);
		if (sx[0] > 1 && sy[0] > 1){
			long oldPixbuf = pixbuf;
			pixbuf = GDK.gdk_pixbuf_scale_simple(pixbuf, width/(int)sx[0], height/(int)sy[0], GDK.GDK_INTERP_BILINEAR);
			OS.g_object_unref(oldPixbuf);
		}
	}
	return pixbuf;
}

public static long createPixbuf(Image image) {
	long surface = convertSurface(image);
	int format = Cairo.cairo_image_surface_get_format(surface);
	int width = Cairo.cairo_image_surface_get_width(surface);
	int height = Cairo.cairo_image_surface_get_height(surface);
	boolean hasAlpha = format == Cairo.CAIRO_FORMAT_ARGB32;
	long pixbuf = GDK.gdk_pixbuf_new (GDK.GDK_COLORSPACE_RGB, hasAlpha, 8, width, height);
	if (pixbuf == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	int stride = GDK.gdk_pixbuf_get_rowstride (pixbuf);
	long pixels = GDK.gdk_pixbuf_get_pixels (pixbuf);
	int oa, or, og, ob;
	if (OS.BIG_ENDIAN) {
		oa = 0; or = 1; og = 2; ob = 3;
	} else {
		oa = 3; or = 2; og = 1; ob = 0;
	}
	byte[] line = new byte[stride];
	long surfaceData = Cairo.cairo_image_surface_get_data(surface);
	if (hasAlpha) {
		for (int y = 0; y < height; y++) {
			C.memmove (line, surfaceData + (y * stride), stride);
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
			C.memmove (pixels + (y * stride), line, stride);
		}
	} else {
		int cairoStride = Cairo.cairo_image_surface_get_stride(surface);
		byte[] cairoLine = new byte[cairoStride];
		for (int y = 0; y < height; y++) {
			C.memmove (cairoLine, surfaceData + (y * cairoStride), cairoStride);
			for (int x = 0, offset = 0, cairoOffset = 0; x < width; x++, offset += 3, cairoOffset += 4) {
				byte r = cairoLine[cairoOffset + or];
				byte g = cairoLine[cairoOffset + og];
				byte b = cairoLine[cairoOffset + ob];
				line[offset + 0] = r;
				line[offset + 1] = g;
				line[offset + 2] = b;
			}
			C.memmove (pixels + (y * stride), line, stride);
		}
	}
	Cairo.cairo_surface_destroy(surface);
	return pixbuf;
}

public int add (Image image) {
	int index = 0;
	while (index < images.length) {
		if (images [index] != null) {
			if (images [index].isDisposed ()) {
				Cairo.cairo_surface_destroy (surfaces [index]);
				images [index] = null;
				surfaces [index] = 0;
			}
		}
		if (images [index] == null) break;
		index++;
	}
	if (index == images.length) {
		Image [] newImages = new Image [images.length + 4];
		System.arraycopy (images, 0, newImages, 0, images.length);
		images = newImages;
		long [] newPixbufs = new long [surfaces.length + 4];
		System.arraycopy (surfaces, 0, newPixbufs, 0, surfaces.length);
		surfaces = newPixbufs;
	}
	set (index, image);
	return index;
}

public void dispose () {
	if (surfaces == null) return;
	for (int index = 0; index < surfaces.length; index++) {
		long surface = surfaces[index];
		if (surface != 0) Cairo.cairo_surface_destroy(surface);
	}

	images = null;
	surfaces = null;
}

public Image get(int index) {
	return images[index];
}

public long getSurface(int index) {
	return surfaces[index];
}

public int indexOf (Image image) {
	if (image == null) return -1;
	for (int index=0; index<images.length; index++) {
		if (image == images [index]) return index;
	}
	return -1;
}

public int indexOf (long pixbuf) {
	if (pixbuf == 0) return -1;
	for (int index=0; index<images.length; index++) {
		if (pixbuf == surfaces [index]) return index;
	}
	return -1;
}

public boolean isDisposed () {
	return images == null;
}

public void put (int index, Image image) {
	int count = images.length;
	if (!(0 <= index && index < count)) return;
	if (images [index] == image) return;
	if (image != null) {
		set (index, image);
	} else {
		images [index] = null;
		if (surfaces [index] != 0) Cairo.cairo_surface_destroy (surfaces [index]);
		surfaces [index] = 0;
	}
}

public void remove(Image image) {
	if (image == null) return;
	for (int index = 0; index < images.length; index++) {
		if (image == images[index]) {
			images [index] = null;

			Cairo.cairo_surface_destroy (surfaces[index]);
			surfaces[index] = 0;
		}
	}
}

void set (int index, Image image) {
	long surface = convertSurface(image);
	int w = Cairo.cairo_image_surface_get_width(surface);
	int h = Cairo.cairo_image_surface_get_height(surface);

	/*SWT Issue 315:
	 * if device scale returns something other than 1,
	 * cairo_image_surface_get_width() and ...height() return the scaled
	 * dimensions since its treating the pixels as display pixels instead of logical pixels
	 * that surfaces use. This causes a second scaling to occur, which is incorrect.
	 * Divide w and h by the device scale to return correct dimensions.
	 */
	double sx[] = new double[1];
	double sy[] = new double[1];
	Cairo.cairo_surface_get_device_scale(image.surface, sx, sy);
	w /= (int)sx[0];
	h /= (int)sy[0];

	Rectangle bounds;
	if (DPIUtil.useCairoAutoScale()) {
		bounds = image.getBounds();
	} else {
		bounds = image.getBoundsInPixels();
	}
	if (w == 0) {
		w = bounds.width;
	}
	if (h == 0) {
		h = bounds.height;
	}
	if (width == -1 || height == -1) {
		width = w;
		height = h;
	}

	if (w != width || h != height) {
		Cairo.cairo_surface_destroy(surface);
		surface = scaleSurface(image, width, height);
	}

	long oldSurface = surfaces[index];
	if (oldSurface != 0) {
		Cairo.cairo_surface_destroy(oldSurface);
	}

	surfaces [index] = surface;
	images [index] = image;
}

long scaleSurface(Image image, int width, int height) {
	int format = Cairo.cairo_surface_get_content(image.surface) == Cairo.CAIRO_CONTENT_COLOR ? Cairo.CAIRO_FORMAT_RGB24 : Cairo.CAIRO_FORMAT_ARGB32;
	long scaledSurface = Cairo.cairo_image_surface_create(format, width, height);
	if (scaledSurface == 0) SWT.error(SWT.ERROR_NO_HANDLES);

	long cairo = Cairo.cairo_create(scaledSurface);
	if (cairo == 0) SWT.error(SWT.ERROR_NO_HANDLES);

	Rectangle bounds;
	if (DPIUtil.useCairoAutoScale()) {
		int w = Cairo.cairo_image_surface_get_width(image.surface);
		int h = Cairo.cairo_image_surface_get_height(image.surface);
		if ((w == 0) && (h == 0)) {
			bounds = image.getBounds();
		} else {
			bounds = new Rectangle(0, 0, w, h);
		}

		double sx[] = new double[1];
		double sy[] = new double[1];
		Cairo.cairo_surface_get_device_scale(image.surface, sx, sy);
		if (sx[0] == 0 || sy[0] == 0){
			sx[0] = sy[0] = DPIUtil.getDeviceZoom() / 100f;
		}
		Cairo.cairo_surface_set_device_scale(scaledSurface, sx[0], sy[0]);
	} else {
		bounds = image.getBoundsInPixels();
	}
	double scaleX = (double) width / (double) bounds.width;
	double scaleY = (double) height / (double) bounds.height;
	Cairo.cairo_scale(cairo, scaleX, scaleY);
	Cairo.cairo_set_operator(cairo, Cairo.CAIRO_OPERATOR_SOURCE);
	Cairo.cairo_set_source_surface(cairo, image.surface, 0, 0);
	Cairo.cairo_paint(cairo);
	Cairo.cairo_destroy(cairo);

	return scaledSurface;
}

}
