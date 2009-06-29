/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.theme;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

public class Theme {
	Device device;
	
	int /*long*/ shellHandle, fixedHandle, buttonHandle, arrowHandle,
		frameHandle, entryHandle, checkButtonHandle, radioButtonHandle, 
		notebookHandle, treeHandle, progressHandle, toolbarHandle,
		labelHandle, separatorHandle;
	
public Theme(Device device) {
	this.device = device;
	shellHandle = OS.gtk_window_new (OS.GTK_WINDOW_TOPLEVEL);
	fixedHandle = OS.gtk_fixed_new();
	buttonHandle = OS.gtk_button_new();
	arrowHandle = OS.gtk_arrow_new(OS.GTK_ARROW_DOWN, OS.GTK_SHADOW_NONE);
	checkButtonHandle = OS.gtk_check_button_new();
	frameHandle = OS.gtk_check_button_new();
	entryHandle = OS.gtk_entry_new();
	radioButtonHandle = OS.gtk_radio_button_new(0);
	notebookHandle = OS.gtk_notebook_new();
	progressHandle = OS.gtk_progress_bar_new();
	toolbarHandle = OS.gtk_toolbar_new();
	treeHandle = OS.gtk_tree_view_new_with_model(0);
	separatorHandle = OS.gtk_vseparator_new();
	labelHandle = OS.gtk_label_new(null);
	OS.gtk_container_add (fixedHandle, labelHandle);
	OS.gtk_container_add (fixedHandle, frameHandle);
	OS.gtk_container_add (fixedHandle, entryHandle);
	OS.gtk_container_add (fixedHandle, separatorHandle);
	OS.gtk_container_add (fixedHandle, arrowHandle);
	OS.gtk_container_add (fixedHandle, toolbarHandle);
	OS.gtk_container_add (fixedHandle, progressHandle);
	OS.gtk_container_add (fixedHandle, checkButtonHandle);
	OS.gtk_container_add (fixedHandle, radioButtonHandle);
	OS.gtk_container_add (fixedHandle, buttonHandle);
	OS.gtk_container_add (fixedHandle, treeHandle);
	OS.gtk_container_add (fixedHandle, notebookHandle);
	OS.gtk_container_add (shellHandle, fixedHandle);
	OS.gtk_widget_realize (separatorHandle);
	OS.gtk_widget_realize (labelHandle);
	OS.gtk_widget_realize (frameHandle);
	OS.gtk_widget_realize (entryHandle);
	OS.gtk_widget_realize (arrowHandle);
	OS.gtk_widget_realize (buttonHandle);
	OS.gtk_widget_realize (treeHandle);
	OS.gtk_widget_realize (notebookHandle);
	OS.gtk_widget_realize (checkButtonHandle);
	OS.gtk_widget_realize (radioButtonHandle);
	OS.gtk_widget_realize (progressHandle);
	OS.gtk_widget_realize (toolbarHandle);
	OS.gtk_widget_realize (shellHandle);
}

//TODO - share this code 
static int /*long*/ createPixbuf(Image image) {
	int [] w = new int [1], h = new int [1];
 	OS.gdk_drawable_get_size (image.pixmap, w, h);
	int /*long*/ colormap = OS.gdk_colormap_get_system ();
	int /*long*/ pixbuf;
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
	return pixbuf;
}

void checkTheme() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

public Rectangle computeTrim(GC gc, DrawData data) {
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	return data.computeTrim(this, gc);
}

public void dispose () {
	if (shellHandle == 0) return;
	OS.gtk_widget_destroy(shellHandle);
	shellHandle = fixedHandle = buttonHandle = arrowHandle =
	frameHandle = entryHandle = checkButtonHandle = radioButtonHandle = 
	notebookHandle = treeHandle = progressHandle = toolbarHandle = 
	labelHandle = separatorHandle = 0;
}
	
public void drawBackground(GC gc, Rectangle bounds, DrawData data) {
	checkTheme();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.draw(this, gc, bounds);
}

public void drawFocus(GC gc, Rectangle bounds, DrawData data) {
	checkTheme();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	gc.drawFocus(bounds.x, bounds.y, bounds.width, bounds.height);
}

public void drawImage(GC gc, Rectangle bounds, DrawData data, Image image, int flags) {
	checkTheme();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.drawImage(this, image, gc, bounds);
}

public void drawText(GC gc, Rectangle bounds, DrawData data, String text, int flags) {
	checkTheme();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (text == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.drawText(this, text, flags, gc, bounds);
}

public Rectangle getBounds(int part, Rectangle bounds, DrawData data) {
	checkTheme();
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return data.getBounds(part, bounds);
}

public int getSelection(Point offset, Rectangle bounds, RangeDrawData data) {
	checkTheme();
	if (offset == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return data.getSelection(offset, bounds);
}

public int hitBackground(Point position, Rectangle bounds, DrawData data) {
	checkTheme();
	if (position == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return data.hit(this, position, bounds);
}

public boolean isDisposed() {
	return device == null;
}

public Rectangle measureText(GC gc, Rectangle bounds, DrawData data, String text, int flags) {
	checkTheme();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (text == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	return data.measureText(this, text, flags, gc, bounds);
}

int getWidgetProperty(int /*long*/ handle, String name) {
	byte[] propertyName = Converter.wcsToMbcs(null, name, true);
	int[] result = new int[1];
	OS.gtk_widget_style_get(handle, propertyName, result, 0);
	return result[0];
}

void transferClipping(GC gc, int /*long*/ style) {
	GCData data = gc.getGCData();
	int /*long*/ clipRgn = data.clipRgn;
	int /*long*/ damageRgn = data.damageRgn;
	int /*long*/ clipping = clipRgn;
	if (damageRgn != 0) {
		if (clipping != 0) {
			clipping = OS.gdk_region_new();
			OS.gdk_region_union(clipping, clipRgn);
			OS.gdk_region_intersect(clipping, damageRgn);
		} else {
			clipping = damageRgn;
		}
	}
	int /*long*/ [] curGC = new int /*long*/ [1];
	for (int i = 0; i < 5; i++) {
		OS.gtk_style_get_fg_gc (style, i, curGC);
		if (curGC[0] != 0) OS.gdk_gc_set_clip_region (curGC[0], clipping);
		OS.gtk_style_get_bg_gc (style, i, curGC);
		if (curGC[0] != 0) OS.gdk_gc_set_clip_region (curGC[0], clipping);
		OS.gtk_style_get_light_gc (style, i, curGC);
		if (curGC[0] != 0) OS.gdk_gc_set_clip_region (curGC[0], clipping);
		OS.gtk_style_get_dark_gc (style, i, curGC);
		if (curGC[0] != 0) OS.gdk_gc_set_clip_region (curGC[0], clipping);
		OS.gtk_style_get_mid_gc (style, i, curGC);
		if (curGC[0] != 0) OS.gdk_gc_set_clip_region (curGC[0], clipping);
		OS.gtk_style_get_text_gc (style, i, curGC);
		if (curGC[0] != 0) OS.gdk_gc_set_clip_region (curGC[0], clipping);
		OS.gtk_style_get_text_aa_gc (style, i, curGC);
		if (curGC[0] != 0) OS.gdk_gc_set_clip_region (curGC[0], clipping);
	}
	OS.gtk_style_get_black_gc (style, curGC);
	if (curGC[0] != 0) OS.gdk_gc_set_clip_region (curGC[0], clipping);
	OS.gtk_style_get_white_gc (style, curGC);
	if (curGC[0] != 0) OS.gdk_gc_set_clip_region (curGC[0], clipping);
	if (clipping != clipRgn && clipping != damageRgn) {
		OS.gdk_region_destroy(clipping);
	}
}
}
