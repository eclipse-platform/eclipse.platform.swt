package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;

public final class GC {
	/**
	 * The handle to the OS device context.
	 * Warning: This field is platform dependent.
	 */
	public int handle;
	
	Drawable drawable;
	GCData data;
		
	static final int DefaultBack = 0xffffff;
	static final int DefaultFore = 0x000000;
	static final byte[][] DashList = {
		{ },                   // SWT.LINE_SOLID
		{ 10, 4 },             // SWT.LINE_DASH
		{ 2, 2 },              // SWT.LINE_DOT
		{ 10, 4, 2, 4 },       // SWT.LINE_DASHDOT
		{ 10, 4, 2, 4, 2, 4 }  // SWT.LINE_DASHDOTDOT
	};
	// Photon Draw Buffer Size for off screen drawing.
	static int DrawBufferSize = 48 * 1024;
	
GC() {
}

public GC(Drawable drawable) {
	int flags = OS.PtEnter(0);
	try {
		if (drawable == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		GCData data = new GCData ();
		int hDC = drawable.internal_new_GC (data);
		Device device = data.device;
		if (device == null) device = Device.getDevice();
		if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		data.device = device;
		init (drawable, data, hDC);
		if (device.tracking) device.new_Object(this);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void copyArea(Image image, int x, int y) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (image == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (image.type != SWT.BITMAP || image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int flags = OS.PtEnter(0);
	try {
		Rectangle bounds = image.getBounds();
		int memImage = 0;
		PhRect_t rect = new PhRect_t();
		rect.ul_x = (short)x; rect.ul_y = (short)y;
		rect.lr_x = (short)(x + bounds.width - 1); rect.lr_y = (short)(y + bounds.height - 1);
		boolean sharedMem = true;
		int rid = data.rid;
		int widget = data.widget;
		if (rid == OS.Ph_DEV_RID) {
			memImage = OS.PgShmemCreate(OS.PgReadScreenSize(rect), null);
			if (memImage != 0) memImage = OS.PgReadScreen(rect, memImage);
		} else if (widget != 0) {
			short [] widget_x = new short [1], widget_y = new short [1];
			OS.PtGetAbsPosition(widget, widget_x, widget_y);
			rect.ul_x += widget_x[0];
			rect.ul_y += widget_y[0];
			rect.lr_x += widget_y[0];
			rect.lr_y += widget_y[0];
			memImage = OS.PgShmemCreate(OS.PgReadScreenSize(rect), null);
			if (memImage != 0) memImage = OS.PgReadScreen(rect, memImage);
		} else if (data.image != null) {
			memImage = OS.PiCropImage(data.image.handle, rect, 0);
			sharedMem = false;
		}
		if (memImage == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		PhImage_t phImage = new PhImage_t();
		OS.memmove(phImage, memImage, PhImage_t.sizeof);
		PhPoint_t trans = new PhPoint_t();
		PhPoint_t pos = new PhPoint_t();
		PhDim_t scale = new PhDim_t();
		scale.w = (short)bounds.width;
		scale.h = (short)bounds.height;
		int mc = OS.PmMemCreateMC(image.handle, scale, trans);
		OS.PmMemStart(mc);
		OS.PgSetDrawBufferSize(DrawBufferSize);
		if (phImage.palette != 0) OS.PgSetPalette(phImage.palette, 0, (short)0, (short)phImage.colors, OS.Pg_PALSET_SOFT, 0);
		OS.PgDrawImage(phImage.image, phImage.type, pos, scale, phImage.bpl, 0);
		if (phImage.palette != 0) OS.PgSetPalette(0, 0, (short)0, (short)-1, 0, 0);
		OS.PmMemFlush(mc, image.handle);
		OS.PmMemStop(mc);
		OS.PmMemReleaseMC(mc);
		if (sharedMem) {
			OS.PgShmemDestroy(memImage);
		} else {
			phImage.flags = OS.Ph_RELEASE_IMAGE_ALL;
			OS.memmove(memImage, phImage, PhImage_t.sizeof);
			OS.PhReleaseImage(memImage);
			OS.free(memImage);	
		}
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void copyArea(int x, int y, int width, int height, int destX, int destY) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width == 0 || height == 0) return;
	int deltaX = destX - x, deltaY = destY - y;
	if (deltaX == 0 && deltaY == 0) return;

	int flags = OS.PtEnter(0);
	try {
		boolean overlaps = (destX < x + width) && (destY < y + height) &&
			(destX + width > x) && (destY + height > y);
		int widget = data.widget;
		Image image = data.image;
		if (image != null) {
			int drawImage = image.handle;
			PhImage_t phDrawImage = new PhImage_t();
			OS.memmove(phDrawImage, drawImage, PhImage_t.sizeof);
			if (overlaps) {
				PhPoint_t trans = new PhPoint_t();
				PhDim_t scale = new PhDim_t();
				scale.w = (short)width;
				scale.h = (short)height;
				PhPoint_t pos = new PhPoint_t();
				pos.x = (short)-x;
				pos.y = (short)-y;
				PhDim_t dim = new PhDim_t();
				dim.w = (short)Math.min(phDrawImage.size_w, x + width);
				dim.h = (short)Math.min(phDrawImage.size_h, y + height);
				/* Feature on Photon - It is only possible to draw on images of
				   type Pg_IMAGE_PALETTE_BYTE and Pg_IMAGE_DIRECT_888.
				*/
				int type = OS.Pg_IMAGE_PALETTE_BYTE;
				if ((phDrawImage.type & OS.Pg_IMAGE_CLASS_MASK) == OS.Pg_IMAGE_CLASS_DIRECT) {
					type = OS.Pg_IMAGE_DIRECT_888;
				}
				int memImage = OS.PhCreateImage(null, (short)width, (short)height, type, phDrawImage.palette, phDrawImage.colors, 0);
				int mc = OS.PmMemCreateMC(memImage, scale, trans);
				OS.PmMemStart(mc);
				OS.PgSetDrawBufferSize(DrawBufferSize);
				if (phDrawImage.palette != 0) OS.PgSetPalette(phDrawImage.palette, 0, (short)0, (short)phDrawImage.colors, OS.Pg_PALSET_SOFT, 0);
				OS.PgDrawImage(phDrawImage.image, phDrawImage.type, pos, dim, phDrawImage.bpl, 0);
				if (phDrawImage.palette != 0) OS.PgSetPalette(0, 0, (short)0, (short)-1, 0, 0);
				OS.PmMemFlush(mc, memImage);
				OS.PmMemStop(mc);
				OS.PmMemReleaseMC(mc);
				x = (short)0;
				y = (short)0;
				drawImage = memImage;
				OS.memmove(phDrawImage, drawImage, PhImage_t.sizeof);
				phDrawImage.flags = OS.Ph_RELEASE_IMAGE_ALL;
				OS.memmove(drawImage, phDrawImage, PhImage_t.sizeof);
			}
			PhPoint_t pos = new PhPoint_t();
			pos.x = (short)(destX - x);
			pos.y = (short)(destY - y);
			PhRect_t clip = new PhRect_t();
			clip.ul_x = (short)destX;
			clip.ul_y = (short)destY;
			clip.lr_x = (short)(destX + width - 1);
			clip.lr_y = (short)(destY + height - 1);
			PhDim_t dim = new PhDim_t();
			dim.w = (short)Math.min(phDrawImage.size_w, x + width);
			dim.h = (short)Math.min(phDrawImage.size_h, y + height);
			int prevContext = setGC();
			setGCClipping();
			OS.PgSetUserClip(clip);
			if (phDrawImage.palette != 0) OS.PgSetPalette(phDrawImage.palette, 0, (short)0, (short)phDrawImage.colors, OS.Pg_PALSET_SOFT, 0);
			OS.PgDrawImage(phDrawImage.image, phDrawImage.type, pos, dim, phDrawImage.bpl, 0);
			if (phDrawImage.palette != 0) OS.PgSetPalette(0, 0, (short)0, (short)-1, 0, 0);
			OS.PgSetUserClip(null);
			unsetGC(prevContext);
			if (drawImage != image.handle) {
				OS.PhReleaseImage(drawImage);
				OS.free(drawImage);
			}
		} else if (widget != 0) {
			int rid = OS.PtWidgetRid(widget);
			if (rid == 0) return;
			PhRect_t rect = new PhRect_t();
			rect.ul_x = (short)x;
			rect.ul_y = (short)y;
			rect.lr_x = (short)(x + width - 1);
			rect.lr_y = (short)(y + height - 1);
			PhPoint_t delta = new PhPoint_t();
			delta.x = (short)deltaX;
			delta.y = (short)deltaY;
			int clipRects = data.clipRects;
			int child_clip = getClipping(widget, data.topWidget, true, true);
			if (clipRects == 0 && child_clip == 0) {
				OS.PhBlit(rid, rect, delta);
			} else {
				int dest = OS.PhGetTile();
				OS.memmove(dest, rect, PhRect_t.sizeof);
				OS.PhTranslateTiles(dest, delta);
				short[] unused = new short[1];
				int clip = child_clip;
				if (clipRects != 0) {
					clip = OS.PhRectsToTiles(clipRects, data.clipRectsCount);
					if (child_clip != 0) {
						clip = OS.PhIntersectTilings(clip, child_clip, unused);
						OS.PhFreeTiles(child_clip);
					}
				}
				int dest_tiles = OS.PhIntersectTilings(dest, clip, unused);
				OS.PhFreeTiles(clip);
				OS.PhFreeTiles(dest);
				PhPoint_t inverseDelta = new PhPoint_t();
				inverseDelta.x = (short)(-delta.x);
				inverseDelta.y = (short)(-delta.y);
				OS.PhTranslateTiles(dest_tiles, inverseDelta);
				int[] src_rects_count = new int[1];
				int src_rects = OS.PhTilesToRects(dest_tiles, src_rects_count);
				OS.PhFreeTiles(dest_tiles);
				PhRect_t src_rect = new PhRect_t();
				for (int i = 0; i<src_rects_count[0]; i++) {
					OS.memmove(src_rect, src_rects + (i * PhRect_t.sizeof), PhRect_t.sizeof);
					OS.PhBlit(rid, src_rect, delta);
				}
				OS.free(src_rects);
			}
			if (!overlaps) {
				OS.PtDamageExtent (widget, rect);
			} else {
				int src = OS.PhGetTile();
				int dest = OS.PhGetTile();
				OS.memmove(src, rect, PhRect_t.sizeof);
				OS.memmove(dest, rect, PhRect_t.sizeof);
				OS.PhTranslateTiles(dest, delta);
				int damage_tile = OS.PhClipTilings(src, dest, null);
				int[] damage_rects_count = new int[1];
				int damage_rects = OS.PhTilesToRects(damage_tile, damage_rects_count);
				OS.PhFreeTiles(dest);
				OS.PhFreeTiles(damage_tile);
				for (int i=0; i<damage_rects_count[0]; i++) {
					OS.memmove(rect, damage_rects + (i * PhRect_t.sizeof), PhRect_t.sizeof);
					OS.PtDamageExtent (widget, rect);
				}
				OS.free(damage_rects);
			}
	}
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void dispose() {
	int flags = OS.PtEnter(0);
	try {
		if (handle == 0) return;
		
		int clipRects = data.clipRects;
		if (clipRects != 0) {
			OS.free(clipRects);
			data.clipRects = data.clipRectsCount = 0;		
		}
		Image image = data.image;
		if (image != null) image.memGC = null;
		
		/*
		* Dispose the HDC.
		*/
		Device device = data.device;
		drawable.internal_dispose_GC(handle, data);
		drawable = null;
		handle = 0;
		data.image = null;
		data.font = null;
		data.rid = data.widget = data.topWidget = 0;
		if (device.tracking) device.dispose_Object(this);
		data.device = null;
		data = null;
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void drawArc (int x, int y, int width, int height, int startAngle, int endAngle) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (startAngle > 0) {
		if (endAngle > 0) {
			//No need to modify start angle.
			endAngle += startAngle;
		} else {
			int newStartAngle;
			int newStopAngle = startAngle;
			if (startAngle > Math.abs(endAngle)) {
				newStartAngle = startAngle - Math.abs(endAngle);
			} else {
				newStartAngle = startAngle + 360 - Math.abs(endAngle);
			}
			startAngle = newStartAngle;
			endAngle = newStopAngle;
		}
	} else {
		if (endAngle > 0) {
			endAngle = endAngle + startAngle;
			startAngle = 360 - Math.abs(startAngle);
		} else {
			int newStopAngle = 360 + startAngle;
			startAngle = newStopAngle - Math.abs(endAngle);
			endAngle = newStopAngle;			
		}
	}
			
	startAngle = (int) (startAngle * 65536 / 360);
	endAngle   = (int) (endAngle * 65536 / 360);
	
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || endAngle == 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	PhPoint_t center = new PhPoint_t();
	center.x = (short)(x + (width / 2));
	center.y = (short)(y + (height / 2));
	PhPoint_t radii = new PhPoint_t();
	radii.x = (short)(width / 2);
	radii.y = (short)(height / 2);

	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCClipping();
		OS.PgDrawArc(center, radii, startAngle, endAngle, OS.Pg_ARC | OS.Pg_DRAW_STROKE);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void drawFocus (int x, int y, int width, int height) {
	width = (width < 0 ? -width : width) - 1;
	height = (height < 0 ? -height : height) - 1;
	drawRectangle(x, y, width, height);
}

public void drawImage(Image image, int x, int y) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (image == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (image.isDisposed()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	drawImage(image, 0, 0, -1, -1, x, y, -1, -1, true);
}

public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (srcWidth == 0 || srcHeight == 0 || destWidth == 0 || destHeight == 0) return;
	if (srcX < 0 || srcY < 0 || srcWidth < 0 || srcHeight < 0 || destWidth < 0 || destHeight < 0) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (image == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (image.isDisposed()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	drawImage(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, false);
}
void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple) {
	int flags = OS.PtEnter(0);
	try {
		int drawImage = image.handle;
		PhImage_t phDrawImage = new PhImage_t();
		OS.memmove(phDrawImage, drawImage, PhImage_t.sizeof);
	 	int imgWidth = phDrawImage.size_w;
	 	int imgHeight = phDrawImage.size_h;
	 	if (simple) {
	 		srcWidth = destWidth = imgWidth;
	 		srcHeight = destHeight = imgHeight;
	 	} else {
	 		simple = srcX == 0 && srcY == 0 &&
	 			srcWidth == destWidth && destWidth == imgWidth &&
	 			srcHeight == destHeight && destHeight == imgHeight;
			if (srcX + srcWidth > imgWidth || srcY + srcHeight > imgHeight) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
	 	} 	
		if (srcWidth != destWidth || srcHeight != destHeight) {
			drawImage = scaleImage(image, phDrawImage, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight);
			srcX = (short)0;
			srcY = (short)0;
			srcWidth = (short)destWidth;
			srcHeight = (short)destHeight;
			OS.memmove(phDrawImage, drawImage, PhImage_t.sizeof);
		}
		PhPoint_t pos = new PhPoint_t();
		pos.x = (short)(destX - srcX);
		pos.y = (short)(destY - srcY);
		PhDim_t dim = new PhDim_t();
		dim.w = (short)Math.min(phDrawImage.size_w, srcX + srcWidth);
		dim.h = (short)Math.min(phDrawImage.size_h, srcY + srcHeight);
		PhRect_t clip = new PhRect_t();
		clip.ul_x = (short)destX;
		clip.ul_y = (short)destY;
		clip.lr_x = (short)(destX + destWidth - 1);
		clip.lr_y = (short)(destY + destHeight - 1);
		int prevContext = setGC();
		setGCClipping();
		OS.PgSetUserClip(clip);
		if (phDrawImage.palette != 0) OS.PgSetPalette(phDrawImage.palette, 0, (short)0, (short)phDrawImage.colors, OS.Pg_PALSET_SOFT, 0);
		if (phDrawImage.alpha != 0) {
			drawImageAlpha(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, phDrawImage, drawImage, pos, dim);
		} else if (image.transparentPixel != -1) {
			drawImageTransparent(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, phDrawImage, drawImage, pos, dim);
		} else if (phDrawImage.mask_bm != 0) {
			drawImageMask(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, phDrawImage, drawImage, pos, dim);
		} else {
			drawImage(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, simple, phDrawImage, drawImage, pos, dim);
		}
		if (phDrawImage.palette != 0) OS.PgSetPalette(0, 0, (short)0, (short)-1, 0, 0);
		OS.PgSetUserClip(null);
		unsetGC(prevContext);	
		if (drawImage != image.handle) {
			phDrawImage.flags = OS.Ph_RELEASE_IMAGE_ALL;
			OS.memmove(drawImage, phDrawImage, PhImage_t.sizeof);
			OS.PhReleaseImage(drawImage);
			OS.free(drawImage);
		}
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}
void drawImageAlpha(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, PhImage_t phImage, int imgHandle, PhPoint_t pos, PhDim_t dim) {
	PgAlpha_t phAlpha = new PgAlpha_t();
	OS.memmove(phAlpha, phImage.alpha, PgAlpha_t.sizeof);
	if ((phAlpha.alpha_op & OS.Pg_ALPHA_OP_SRC_GLOBAL) != 0) {
		OS.PgSetAlpha(phAlpha.alpha_op, null, 0, phAlpha.src_global_alpha, phAlpha.dest_global_alpha);
		OS.PgAlphaOn();
		OS.PgDrawImage(phImage.image, phImage.type, pos, dim, phImage.bpl, 0);
		OS.PgAlphaOff();
		return;
	}

	/*
	* Feature/Bug in Photon - When drawing images with alpha blending
	* enabled, there is a limitation in the size of the alpha map.
	* This limitation is probably related to the draw buffer size and
	* it seems to be worse when drawing to a memory context.  The
	* fix/workaround is to draw the image line by line.
	*/
	PgMap_t imageMap = new PgMap_t();
	OS.memmove(imageMap, phImage.alpha + 4, PgMap_t.sizeof);
	PgMap_t lineMap = new PgMap_t();
	lineMap.dim_w = imageMap.dim_w;
	lineMap.dim_h = 1;
	/*
	* Feature in Photon - The alpha map set in a graphics context by
	* PgSetAlpha is freed when the graphics context is destroyed.
	*/
	lineMap.map = OS.malloc(lineMap.dim_w);
	OS.PgSetAlpha(phAlpha.alpha_op, lineMap, 0, phAlpha.src_global_alpha, phAlpha.dest_global_alpha);
	OS.PgAlphaOn();
	pos.y = (short)(destY);
	int end = dim.h;
	dim.h = (short)1;
	for (int y=srcY; y<end; y+=lineMap.dim_h) {
		OS.memmove(lineMap.map, imageMap.map + (imageMap.dim_w * y), lineMap.dim_w);
		/* 
		* Bug in Photon - When drawing an image to a memory context created by
		* PmMemCreateMC at a negative position, the alpha map is not offset.
		*/
		if (data.image != null && pos.x < 0) {
			OS.memmove(lineMap.map, lineMap.map - pos.x, lineMap.dim_w + pos.x);
		}
		OS.PgDrawImage(phImage.image + (phImage.bpl * y), phImage.type, pos, dim, phImage.bpl, 0);
		/*
		* Flushing is necessary in order to change the alpha map.
		*/
		if (data.image != null) OS.PmMemFlush(handle, data.image.handle);
		else OS.PgFlush();
		pos.y += lineMap.dim_h;
	}
	OS.PgAlphaOff();
}
void drawImageTransparent(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, PhImage_t phImage, int imgHandle, PhPoint_t pos, PhDim_t dim) {
	/* Generate the mask if necessary */
	if (phImage.mask_bm == 0) {
		createMask(imgHandle, phImage.type, image.transparentPixel);
		OS.memmove(phImage, imgHandle, PhImage_t.sizeof);
	}
	OS.PgDrawTImage(phImage.image, phImage.type, pos, dim, phImage.bpl, 0, phImage.mask_bm, phImage.mask_bpl);
	/* Destroy the mask if there is a GC created on the image */
	if (image.memGC != null && image.handle == imgHandle) {
		OS.free(phImage.mask_bm);
		phImage.mask_bm = 0;
		phImage.mask_bpl = 0;
		OS.memmove(imgHandle, phImage, PhImage_t.sizeof);
	}
}
void drawImageMask(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, PhImage_t phImage, int imgHandle, PhPoint_t pos, PhDim_t dim) {
	OS.PgDrawTImage(phImage.image, phImage.type, pos, dim, phImage.bpl, 0, phImage.mask_bm, phImage.mask_bpl);
}
void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple, PhImage_t phImage, int imgHandle, PhPoint_t pos, PhDim_t dim) {
	OS.PgDrawImage(phImage.image, phImage.type, pos, dim, phImage.bpl, 0);
}
static void createMask(int image, int type, int transparent) {
	if ((type & OS.Pg_IMAGE_CLASS_MASK) == OS.Pg_IMAGE_CLASS_PALETTE) {
		transparent = (transparent & 0xFF) | OS.Pt_INDEX_COLOR;
	} else {
 		switch (type) {
 			case OS.Pg_IMAGE_DIRECT_888:
				transparent = ((transparent & 0xFF) << 16) | (transparent & 0xFF00) | ((transparent & 0xFF0000) >> 16);
				break;
			case OS.Pg_IMAGE_DIRECT_8888:
				transparent = ((transparent & 0xFF00) << 8) | ((transparent & 0xFF0000) >> 8) | ((transparent & 0xFF000000) >> 24);
				break;
			case OS.Pg_IMAGE_DIRECT_565:
				transparent = ((transparent & 0xF800) << 8) | ((transparent & 0x7E0) << 5) | ((transparent & 0x1F) << 3);
				break;
			case OS.Pg_IMAGE_DIRECT_555:
				transparent = ((transparent & 0x7C00) << 9) | ((transparent & 0x3E0) << 6) | ((transparent & 0x1F) << 3);
				break;
			case OS.Pg_IMAGE_DIRECT_444:
				transparent = ((transparent & 0xF00) << 12) | ((transparent & 0xF0) << 8) | ((transparent & 0xF) << 4);
				break;
		}
	}
	OS.PhMakeTransBitmap(image, transparent);
}
static int scaleImage(Image image, PhImage_t phImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight) {
	PhPoint_t trans = new PhPoint_t();
	PhDim_t scale = new PhDim_t();
	scale.w = (short)srcWidth;
	scale.h = (short)srcHeight;
	PhPoint_t pos = new PhPoint_t();
	pos.x = (short)-srcX;
	pos.y = (short)-srcY;
	PhDim_t dim = new PhDim_t();
	dim.w = (short)Math.min(phImage.size_w, srcX + srcWidth);
	dim.h = (short)Math.min(phImage.size_h, srcY + srcHeight);
	/*
	* Feature on Photon - It is only possible to draw on images of
	* type Pg_IMAGE_PALETTE_BYTE and Pg_IMAGE_DIRECT_888.
	*/
	int type = OS.Pg_IMAGE_PALETTE_BYTE;
	if ((phImage.type & OS.Pg_IMAGE_CLASS_MASK) == OS.Pg_IMAGE_CLASS_DIRECT) {
		type = OS.Pg_IMAGE_DIRECT_888;
	}
	/* Scale the image */
	int memImage = OS.PhCreateImage(null, (short)destWidth, (short)destHeight, type, phImage.palette, phImage.colors, 0);
	if (memImage == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int mc = OS.PmMemCreateMC(memImage, scale, trans);
	if (mc == 0) {
		Image.destroyImage(memImage);
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	OS.PmMemStart(mc);
	OS.PgSetDrawBufferSize(DrawBufferSize);
	if (phImage.palette != 0) OS.PgSetPalette(phImage.palette, 0, (short)0, (short)phImage.colors, OS.Pg_PALSET_SOFT, 0);
	OS.PgDrawImage(phImage.image, phImage.type, pos, dim, phImage.bpl, 0);
	if (phImage.palette != 0) OS.PgSetPalette(0, 0, (short)0, (short)-1, 0, 0);
	OS.PmMemFlush(mc, memImage);
	OS.PmMemStop(mc);
	OS.PmMemReleaseMC(mc);
	
	PhImage_t phMemImage = new PhImage_t();
	OS.memmove(phMemImage, memImage, PhImage_t.sizeof);
	if (image.transparentPixel != -1) {
		/* Generate the mask if it was created originally */
		if (phImage.mask_bm != 0) {
			createMask(memImage, phImage.type, image.transparentPixel);
		}
	} else if (phImage.mask_bm != 0) {
		/* Scale the mask */
		int[] palette = new int[2];
		palette[0] = 0x000000;
		palette[1] = 0xffffff;
		int palettePtr = OS.malloc(palette.length * 4);
		OS.memmove(palettePtr, palette, palette.length * 4);
		/*
		* Feature on Photon - It is only possible to draw on images of
		* type Pg_IMAGE_PALETTE_BYTE and Pg_IMAGE_DIRECT_888.
		*/
		int maskImage = OS.PhCreateImage(null, (short)destWidth, (short)destHeight, OS.Pg_IMAGE_PALETTE_BYTE, palettePtr, palette.length, 0);
		if (maskImage == 0) {
			Image.destroyImage(memImage);
			SWT.error(SWT.ERROR_NO_HANDLES);
		}
		mc = OS.PmMemCreateMC(maskImage, scale, trans);
		if (mc == 0) {
			Image.destroyImage(maskImage);
			Image.destroyImage(memImage);
			SWT.error(SWT.ERROR_NO_HANDLES);
		}
		OS.PmMemStart(mc);
		OS.PgSetDrawBufferSize(DrawBufferSize);
		OS.PgSetFillColor(palette[0]);
		OS.PgSetTextColor(palette[1]);
		OS.PgDrawBitmap(phImage.mask_bm, OS.Pg_BACK_FILL, pos, dim, phImage.mask_bpl, 0);
		OS.PmMemFlush(mc, maskImage);
		OS.PmMemStop(mc);
		OS.PmMemReleaseMC(mc);
		OS.free(palettePtr);
		
		/* Transfer the mask to the scaled image */
		OS.PhMakeTransBitmap(maskImage, 0 | OS.Pt_INDEX_COLOR);			
		PhImage_t phMaskImage = new PhImage_t();
		OS.memmove(phMaskImage, maskImage, PhImage_t.sizeof);
		phMemImage.mask_bm = phMaskImage.mask_bm;
		phMemImage.mask_bpl = phMaskImage.mask_bpl;
		OS.memmove(memImage, phMemImage, PhImage_t.sizeof);
		
		/* Release the temporary image but not the mask data */
		phMaskImage.mask_bm = 0;
		phMaskImage.mask_bpl = 0;
		phMaskImage.flags = OS.Ph_RELEASE_IMAGE_ALL;
		OS.memmove(maskImage, phMaskImage, PhImage_t.sizeof);
		OS.PhReleaseImage(maskImage);
		OS.free(maskImage);
	} else if (phImage.alpha != 0) {
		PgAlpha_t alpha = new PgAlpha_t();
		OS.memmove(alpha, phImage.alpha, PgAlpha_t.sizeof);
		int alphaPtr = OS.malloc(PgAlpha_t.sizeof);
		if (alphaPtr == 0) {
			Image.destroyImage(memImage);
			SWT.error(SWT.ERROR_NO_HANDLES);
		}
		
		/* Scale alpha data */
		if (alpha.src_alpha_map_map != 0) {
			int[] palette = new int[256];
			for (int i = 0; i < palette.length; i++) {
				palette[i] = i;
			}
			int palettePtr = OS.malloc(palette.length * 4);
			OS.memmove(palettePtr, palette, palette.length * 4);
			/*
			* Feature on Photon - It is only possible to draw on images of
			* type Pg_IMAGE_PALETTE_BYTE and Pg_IMAGE_DIRECT_888.
			*/
			int alphaImage = OS.PhCreateImage(null, (short)destWidth, (short)destHeight, OS.Pg_IMAGE_PALETTE_BYTE, palettePtr, palette.length, 0);
			if (alphaImage == 0) {
				OS.free(palettePtr);
				OS.free(alphaPtr);
				Image.destroyImage(memImage);
				SWT.error(SWT.ERROR_NO_HANDLES);
			}
			mc = OS.PmMemCreateMC(alphaImage, scale, trans);
			if (mc == 0) {
				OS.free(palettePtr);
				OS.free(alphaPtr);
				Image.destroyImage(alphaImage);
				Image.destroyImage(memImage);
				SWT.error(SWT.ERROR_NO_HANDLES);
			}
			OS.PmMemStart(mc);
			OS.PgSetPalette(palettePtr, 0, (short)0, (short)palette.length, OS.Pg_PALSET_SOFT, 0);
			OS.PgDrawImage(alpha.src_alpha_map_map, OS.Pg_IMAGE_PALETTE_BYTE, pos, dim, alpha.src_alpha_map_bpl, 0);
			OS.PgSetPalette(0, 0, (short)0, (short)-1, 0, 0);
			OS.PmMemFlush(mc, alphaImage);
			OS.PmMemStop(mc);
			OS.PmMemReleaseMC(mc);
			OS.free(palettePtr);
				
			/* Transfer the image to the scaled image alpha data*/
			PhImage_t phAlphaImage = new PhImage_t();
			OS.memmove(phAlphaImage, alphaImage, PhImage_t.sizeof);
			alpha.src_alpha_map_bpl = (short)phAlphaImage.bpl;
			alpha.src_alpha_map_dim_w = (short)phAlphaImage.bpl;
			alpha.src_alpha_map_dim_h = (short)phAlphaImage.size_h;
			alpha.src_alpha_map_map = phAlphaImage.image;

			/* Release the temporary image but not the image data */
			phAlphaImage.image = 0;
			phAlphaImage.bpl = 0;
			phAlphaImage.flags = OS.Ph_RELEASE_IMAGE_ALL;
			OS.memmove(alphaImage, phAlphaImage, PhImage_t.sizeof);
			OS.PhReleaseImage(alphaImage);
			OS.free(alphaImage);
		}

		OS.memmove(alphaPtr, alpha, PgAlpha_t.sizeof);
		phMemImage.alpha = alphaPtr;
		OS.memmove(memImage, phMemImage, PhImage_t.sizeof);
	}
	return memImage;
}

public void drawLine (int x1, int y1, int x2, int y2) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCClipping();
		OS.PgDrawILine(x1, y1, x2, y2);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void drawOval (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	PhPoint_t center = new PhPoint_t();
	center.x = (short)x; center.y = (short)y;
	PhPoint_t radii = new PhPoint_t();
	// Don't subtract one, so that the bottom/right edges are drawn
	radii.x = (short)(x + width); radii.y = (short)(y + height);

	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCClipping();
		OS.PgDrawEllipse(center, radii,	OS.Pg_DRAW_STROKE | OS.Pg_EXTENT_BASED);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void drawPolygon(int[] pointArray) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	
	short[] points = new short[pointArray.length];
	for (int i = pointArray.length - 1; i >= 0; i--) {
		points[i] = (short)pointArray[i];
	}
	
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCClipping();
		OS.PgDrawPolygon(points, pointArray.length / 2,	new PhPoint_t(), OS.Pg_DRAW_STROKE | OS.Pg_CLOSED);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void drawPolyline(int[] pointArray) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	
	short[] points = new short[pointArray.length];
	for (int i = pointArray.length - 1; i >= 0; i--) {
		points[i] = (short)pointArray[i];
	}
	
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCClipping();
		OS.PgDrawPolygon(points, pointArray.length / 2,	new PhPoint_t(), OS.Pg_DRAW_STROKE);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void drawRectangle (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();	
		setGCClipping();
		// Don't subtract one, so that the bottom/right edges are drawn
		OS.PgDrawIRect(x, y, x + width, y + height, OS.Pg_DRAW_STROKE);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void drawRectangle (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	drawRectangle (rect.x, rect.y, rect.width, rect.height);
}

public void drawRoundRectangle (int x, int y, int width, int height, int arcWidth, int arcHeight) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	PhRect_t rect  = new PhRect_t();
	rect.ul_x = (short)x; rect.ul_y = (short)y;
	// Don't subtract one, so that the bottom/right edges are drawn
	rect.lr_x = (short)(x + width); rect.lr_y = (short)(y + height);
	PhPoint_t radii = new PhPoint_t();
	radii.x = (short)(arcWidth / 2); radii.y = (short)(arcHeight / 2);

	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCClipping();
		OS.PgDrawRoundRect(rect, radii, OS.Pg_DRAW_STROKE);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void drawString (String string, int x, int y) {
	drawString(string, x, y, false);
}

public void drawString (String string, int x, int y, boolean isTransparent) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);

	int drawFlags = OS.Pg_TEXT_LEFT | OS.Pg_TEXT_TOP;
	if (!isTransparent) drawFlags |= OS.Pg_BACK_FILL;
	byte[] buffer = Converter.wcsToMbcs(null, string, false);

	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();	
		setGCClipping();
		OS.PgDrawText(buffer, buffer.length, (short)x, (short)y, drawFlags);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void drawText (String string, int x, int y) {
	drawText(string, x, y, false);
}

public void drawText (String string, int x, int y, boolean isTransparent) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);

	int drawFlags = OS.Pg_TEXT_LEFT | OS.Pg_TEXT_TOP;
	if (!isTransparent) drawFlags |= OS.Pg_BACK_FILL;
	string = replaceTabs(string, 8);
	byte[] buffer = Converter.wcsToMbcs(null, string, false);
	PhRect_t rect = new PhRect_t();
	rect.ul_x = (short)x;
	rect.ul_y = (short)y;
	rect.lr_x = (short)0xFFFF;
	rect.lr_y = (short)0xFFFF;

	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();	
		setGCClipping();
		OS.PgDrawMultiTextArea(buffer, buffer.length, rect, drawFlags, OS.Pg_TEXT_LEFT | OS.Pg_TEXT_TOP, 0);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public boolean equals (Object object) {
	return (object == this) || ((object instanceof GC) && (handle == ((GC)object).handle));
}

public void fillArc (int x, int y, int width, int height, int startAngle, int endAngle) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (startAngle > 0) {
		if (endAngle > 0) {
			//No need to modify start angle.
			endAngle += startAngle;
		} else {
			int newStartAngle;
			int newStopAngle = startAngle;
			if (startAngle > Math.abs(endAngle)) {
				newStartAngle = startAngle - Math.abs(endAngle);
			} else {
				newStartAngle = startAngle + 360 - Math.abs(endAngle);
			}
			startAngle = newStartAngle;
			endAngle = newStopAngle;
		}
	} else {
		if (endAngle > 0) {
			endAngle = endAngle + startAngle;
			startAngle = 360 - Math.abs(startAngle);
		} else {
			int newStopAngle = 360 + startAngle;
			startAngle = newStopAngle - Math.abs(endAngle);
			endAngle = newStopAngle;			
		}
	}
			
	startAngle = (int) (startAngle * 65536 / 360);
	endAngle   = (int) (endAngle * 65536 / 360);
	
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || endAngle == 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	
	PhPoint_t center = new PhPoint_t();
	center.x = (short)(x + (width / 2));
	center.y = (short)(y + (height / 2));
	PhPoint_t radii = new PhPoint_t();
	radii.x = (short)(width / 2);
	radii.y = (short)(height / 2);
	
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();	
		setGCClipping();
		OS.PgDrawArc(center, radii, startAngle, endAngle, OS.Pg_ARC_PIE | OS.Pg_DRAW_FILL);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void fillOval (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	PhPoint_t center = new PhPoint_t();
	center.x = (short)x; center.y = (short)y;
	PhPoint_t radii = new PhPoint_t();
	radii.x = (short)(x + width);
	radii.y = (short)(y + height);

	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCClipping();
		OS.PgDrawEllipse(center, radii,	OS.Pg_DRAW_FILL | OS.Pg_EXTENT_BASED);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void fillPolygon(int[] pointArray) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	
	short[] points = new short[pointArray.length];
	for (int i = pointArray.length - 1; i >= 0; i--) {
		points[i] = (short)pointArray[i];
	}
	
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCClipping();
		OS.PgDrawPolygon(points, pointArray.length / 2,	new PhPoint_t(), OS.Pg_DRAW_FILL | OS.Pg_CLOSED);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void fillRectangle (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width == 0 || height == 0) return;
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();	
		setGCClipping();
		OS.PgDrawIRect(x, y, x + width - 1, y + height - 1, OS.Pg_DRAW_FILL);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void fillRectangle (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	fillRectangle (rect.x, rect.y, rect.width, rect.height);
}

public void fillRoundRectangle (int x, int y, int width, int height, int arcWidth, int arcHeight) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	PhRect_t rect  = new PhRect_t();
	rect.ul_x = (short)x; rect.ul_y = (short)y;
	rect.lr_x = (short)(x + width - 1); rect.lr_y = (short)(y + height - 1);
	PhPoint_t radii = new PhPoint_t();
	radii.x = (short)(arcWidth / 2); radii.y = (short)(arcHeight / 2);

	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		setGCClipping();
		OS.PgDrawRoundRect(rect, radii, OS.Pg_DRAW_FILL);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public int getAdvanceWidth(char ch) {
	return getCharWidth(ch);
}

public Color getBackground() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return Color.photon_new(data.device, data.background);
}

public int getCharWidth(char ch) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	String string = new String(new char[] {ch});
	Point point = stringExtent(string);
	return point.x;
}

public Rectangle getClipping() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int flags = OS.PtEnter(0);
	try {
		PhRect_t rect = new PhRect_t();
		int rid = data.rid;
		int widget = data.widget;
		Image image = data.image;
		if (rid == OS.Ph_DEV_RID) {
			OS.PhRegionQuery (rid, null, rect, 0, 0);
		} else if (widget != 0) {
			OS.PtWidgetCanvas(widget, rect);	
		} else if (image != null) {
			PhImage_t img = new PhImage_t();
			OS.memmove(img, image.handle, PhImage_t.sizeof);
			rect.lr_x = (short)(img.size_w - 1);
			rect.lr_y = (short)(img.size_h - 1);
		}
		int clipRects = data.clipRects;
		if (clipRects != 0) {
			int clipRectsCount = data.clipRectsCount;
			int clip_ptr = OS.malloc(PhRect_t.sizeof);
			OS.memmove(clip_ptr, clipRects, PhRect_t.sizeof);
			for (int i = 1; i < clipRectsCount; i++) {
				OS.PhRectUnion (clip_ptr, clipRects + (i * 4));
			}
			int rect_ptr = OS.malloc(PhRect_t.sizeof);
			OS.memmove(rect_ptr, rect, PhRect_t.sizeof);
			boolean intersect = OS.PhRectIntersect(rect_ptr, clip_ptr) != 0;
			OS.memmove(rect, rect_ptr, PhRect_t.sizeof);
			OS.free(rect_ptr);
			OS.free(clip_ptr);
			if (!intersect) return new Rectangle(0, 0, 0, 0);
		}
		int width = rect.lr_x - rect.ul_x + 1;
		int height = rect.lr_y - rect.ul_y + 1;
		return new Rectangle(rect.ul_x, rect.ul_y, width, height);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void getClipping (Region region) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	int flags = OS.PtEnter(0);
	try {
		if (region.handle != 0 && region.handle != Region.EMPTY_REGION) {
			OS.PhFreeTiles(region.handle);
		}
		int clipRects = data.clipRects;
		if (clipRects != 0) {
			region.handle = OS.PhRectsToTiles(clipRects, data.clipRectsCount);
		} else {
			region.handle = OS.PhGetTile();
			PhRect_t rect = new PhRect_t ();
			int rid = data.rid;
			int widget = data.widget;
			Image image = data.image;
			if (rid == OS.Ph_DEV_RID) {
				OS.PhRegionQuery (rid, null, rect, 0, 0);
			} else if (widget != 0) {
				OS.PtWidgetCanvas(widget, rect);
			} else if (image != null) {
				PhImage_t img = new PhImage_t();
				OS.memmove(img, image.handle, PhImage_t.sizeof);
				rect.lr_x = (short)(img.size_w - 1);
				rect.lr_y = (short)(img.size_h - 1);
			}
			OS.memmove(region.handle, rect, PhRect_t.sizeof);
		}
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public Font getFont () {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return Font.photon_new(data.device, data.font);
}

public FontMetrics getFontMetrics() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	FontQueryInfo info = new FontQueryInfo();
	OS.PfQueryFontInfo(data.font, info);
	return FontMetrics.photon_new(info);
}

public Color getForeground() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return Color.photon_new(data.device, data.foreground);
}

public int getLineStyle() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.lineStyle;
}

public int getLineWidth() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.lineWidth;
}

public boolean getXORMode() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.xorMode;
}

public int hashCode () {
	return handle;
}

void init(Drawable drawable, GCData data, int context) {
	int prevContext;
	Image image = data.image;
	if (image == null) {
	 	prevContext = OS.PgSetGC(context);
	} else {
		prevContext = OS.PmMemStart(context);
		OS.PgSetDrawBufferSize(DrawBufferSize);
	}

	if (data.foreground == -1) data.foreground = DefaultFore;
	OS.PgSetStrokeColor(data.foreground);
	OS.PgSetTextColor(data.foreground);
	if (data.background == -1) data.background = DefaultBack;
	OS.PgSetFillColor(data.background);
	if (data.font == null) data.font = Font.DefaultFont;
	OS.PgSetFont(data.font);

	if (image == null) {
		OS.PgSetGC(prevContext);
	} else {
		image.memGC = this;
		OS.PmMemStop(context);
		
		
		/*
		* Destroy the mask when it is generated from a transparent
		* pixel since drawing on the image might change the mask.
		*/
		if (image.transparentPixel != -1) {
			PhImage_t phImage = new PhImage_t ();
			OS.memmove(phImage, image.handle, PhImage_t.sizeof);
			if (phImage.mask_bm != 0) {
				OS.free(phImage.mask_bm);
				phImage.mask_bm = 0;
				phImage.mask_bpl = 0;
				OS.memmove(image.handle, phImage, PhImage_t.sizeof);
			}
		}
	}
	this.drawable = drawable;
	this.data = data;
	handle = context;
}

public boolean isClipped() {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.clipRects != 0;
}

public boolean isDisposed() {
	return handle == 0;
}

static String replaceTabs(String text, int spaces) {
	int length = text.length();
	int index = text.indexOf('\t', 0);
	if (index == -1) return text;

	int start = 0;
	StringBuffer result = new StringBuffer();
	StringBuffer spaceString = new StringBuffer();
	while (spaces-- > 0) {
		spaceString.append(' ');
	}
	while (index != -1 && index < length) {
		result.append(text.substring(start, index));
		result.append(spaceString);
		index = text.indexOf('\t', start = index + 1);
	}
	if (index == -1) result.append(text.substring(start, length));
	return result.toString();
}

public void setBackground (Color color) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		OS.PgSetFillColor(data.background = color.handle);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void setClipping (int x, int y, int width, int height) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int flags = OS.PtEnter(0);
	try {
		int clipRects = data.clipRects;
		if (clipRects != 0)
			OS.free(clipRects);
		clipRects = OS.malloc(PhRect_t.sizeof);
		int clipRectsCount = 1;
		PhRect_t rect = new PhRect_t();
		rect.ul_x = (short)x;
		rect.ul_y = (short)y;
		rect.lr_x = (short)(x + width - 1);
		rect.lr_y = (short)(y + height - 1);
		OS.memmove(clipRects, rect, PhRect_t.sizeof);
		int prevContext = setGC();
		OS.PgSetMultiClip(clipRectsCount, clipRects);
		data.clipRects = clipRects;
		data.clipRectsCount = clipRectsCount;
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void setClipping (Rectangle rect) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) {
		int flags = OS.PtEnter(0);
		try {
			int clipRects = data.clipRects;
			if (clipRects != 0)
				OS.free(clipRects);
			data.clipRects = data.clipRectsCount = 0;
			int prevContext = setGC();
			OS.PgSetMultiClip(0, 0);
			unsetGC(prevContext);
		} finally {
			if (flags >= 0) OS.PtLeave(flags);
		}
	} else 
		setClipping (rect.x, rect.y, rect.width, rect.height);
}

public void setClipping (Region region) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int flags = OS.PtEnter(0);
	try {
		int clipRects = data.clipRects;
		int clipRectsCount = data.clipRectsCount;
		if (clipRects != 0)
			OS.free(clipRects);
		if (region == null || region.handle == 0) {
			clipRects = clipRectsCount = 0;
		} else if (region.handle == Region.EMPTY_REGION) {
			clipRects = OS.malloc(PhRect_t.sizeof);
			clipRectsCount = 1;
		} else {
			int[] clip_rects_count = new int[1];
			clipRects = OS.PhTilesToRects(region.handle, clip_rects_count);
			clipRectsCount = clip_rects_count[0];
		}
		int prevContext = setGC();
		OS.PgSetMultiClip(clipRectsCount, clipRects);
		data.clipRects = clipRects;
		data.clipRectsCount = clipRectsCount;
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void setFont (Font font) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (font != null && font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		byte[] newFont = font == null ? Font.DefaultFont : font.handle;
		OS.PgSetFont(newFont);
		data.font = newFont;
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void setForeground (Color color) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		int foreColor = data.foreground = color.handle;
		OS.PgSetStrokeColor(foreColor);
		OS.PgSetTextColor(foreColor);
		unsetGC(prevContext);
	} finally {	
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void setLineStyle(int lineStyle) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	byte[] dashList;
	switch (lineStyle) {
		case SWT.LINE_SOLID: dashList = DashList[0]; break;
		case SWT.LINE_DASH:	dashList = DashList[1]; break;
		case SWT.LINE_DOT: dashList = DashList[2]; break;
		case SWT.LINE_DASHDOT: dashList = DashList[3]; break;
		case SWT.LINE_DASHDOTDOT: dashList = DashList[4]; break;
		default:
			SWT.error (SWT.ERROR_INVALID_ARGUMENT);
			return;
	}
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		data.lineStyle = lineStyle;
		OS.PgSetStrokeDash(dashList, dashList.length, 0x10000);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public void setLineWidth(int lineWidth) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		data.lineWidth = lineWidth;
		OS.PgSetStrokeWidth(lineWidth);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

int setGC() {
	if (data.image != null) return OS.PmMemStart(handle);
	else if (data.rid == OS.Ph_DEV_RID || data.widget != 0) return OS.PgSetGC(handle);
	else return 0;
}

void setGCClipping() {
	int rid = data.rid;
	int widget = data.widget;
	if (rid == OS.Ph_DEV_RID) OS.PgSetRegion(rid);
	else if (widget != 0) OS.PgSetRegion(OS.PtWidgetRid(widget));
	else if (data.image != null) return;
	
	/* NOTE: PgSetRegion resets the clipping rectangle */
	OS.PgSetMultiClip(data.clipRectsCount, data.clipRects);	

	if (widget == 0) return;
	
	int clip_tile = getClipping(widget, data.topWidget, true, true);
	int[] clip_rects_count = new int[1];
	int clip_rects = OS.PhTilesToRects(clip_tile, clip_rects_count);
	OS.PhFreeTiles(clip_tile);	
	if (clip_rects_count[0] == 0) {
		clip_rects_count[0] = 1;
		OS.free(clip_rects);
		clip_rects = OS.malloc(PhRect_t.sizeof);
	}
	OS.PgSetClipping((short)clip_rects_count[0], clip_rects);
	OS.free(clip_rects);
}

int getClipping(int widget, int topWidget, boolean clipChildren, boolean clipSiblings) {
	int child_tile = 0;
	int widget_tile = OS.PhGetTile(); // NOTE: PhGetTile native initializes the tile

	PhRect_t rect = new PhRect_t ();
	int args [] = {OS.Pt_ARG_FLAGS, 0, 0, OS.Pt_ARG_BASIC_FLAGS, 0, 0};
	
	/* Get the rectangle of all siblings in front of the widget */
	if (clipSiblings && OS.PtWidgetClass(topWidget) != OS.PtWindow()) {
		int temp_widget = topWidget;
		while ((temp_widget = OS.PtWidgetBrotherInFront(temp_widget)) != 0) {
			if (OS.PtWidgetIsRealized(temp_widget)) {
				int tile = OS.PhGetTile();
				if (child_tile == 0) child_tile = tile;			
				else child_tile = OS.PhAddMergeTiles(tile, child_tile, null);
				OS.PtWidgetExtent(temp_widget, tile); // NOTE: tile->rect
				args [1] = args [4] = 0;
				OS.PtGetResources(temp_widget, args.length / 3, args);
				if ((args [1] & OS.Pt_HIGHLIGHTED) != 0) {
					int basic_flags = args [4];
					OS.memmove(rect, tile, PhRect_t.sizeof);
					if ((basic_flags & OS.Pt_TOP_ETCH) != 0) rect.ul_y++;
					if ((basic_flags & OS.Pt_BOTTOM_ETCH) != 0) rect.lr_y--;
					if ((basic_flags & OS.Pt_RIGHT_ETCH) != 0) rect.ul_x++;
					if ((basic_flags & OS.Pt_LEFT_ETCH) != 0) rect.lr_x--;
					OS.memmove(tile, rect, PhRect_t.sizeof);
				}
			}
		}
		/* Translate the siblings rectangles to the widget's coordinates */
		OS.PtWidgetCanvas(topWidget, widget_tile); // NOTE: widget_tile->rect
		OS.PhDeTranslateTiles(child_tile, widget_tile); // NOTE: widget_tile->rect.ul
	}
			
	/* Get the rectangle of the widget's children */
	if (clipChildren) {
		int temp_widget = OS.PtWidgetChildBack(widget);
		while (temp_widget != 0) {
			if (OS.PtWidgetIsRealized(temp_widget)) {
				int tile = OS.PhGetTile();
				if (child_tile == 0) child_tile = tile;			
				else child_tile = OS.PhAddMergeTiles(tile, child_tile, null);
				OS.PtWidgetExtent(temp_widget, tile); // NOTE: tile->rect
				args [1] = args [4] = 0;
				OS.PtGetResources(temp_widget, args.length / 3, args);
				if ((args [1] & OS.Pt_HIGHLIGHTED) != 0) {
					int basic_flags = args [4];
					OS.memmove(rect, tile, PhRect_t.sizeof);
					if ((basic_flags & OS.Pt_TOP_ETCH) != 0) rect.ul_y++;
					if ((basic_flags & OS.Pt_BOTTOM_ETCH) != 0) rect.lr_y--;
					if ((basic_flags & OS.Pt_RIGHT_ETCH) != 0) rect.ul_x++;
					if ((basic_flags & OS.Pt_LEFT_ETCH) != 0) rect.lr_x--;
					OS.memmove(tile, rect, PhRect_t.sizeof);
				}
			}
			temp_widget = OS.PtWidgetBrotherInFront(temp_widget);
		}
	}

	/* Get the widget's rectangle */
	OS.PtWidgetCanvas(widget, widget_tile); // NOTE: widget_tile->rect
	OS.PhDeTranslateTiles(widget_tile, widget_tile); // NOTE: widget_tile->rect.ul

	/* Clip the widget's rectangle from the child/siblings rectangle's */
	if (child_tile != 0) {
		int clip_tile = OS.PhClipTilings(widget_tile, child_tile, null);
		OS.PhFreeTiles(child_tile);
		return clip_tile;
	}
	return widget_tile;
}

public void setXORMode(boolean xor) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		data.xorMode = xor;
		if (xor) OS.PgSetDrawMode(OS.Pg_DRAWMODE_XOR);
		else OS.PgSetDrawMode(OS.Pg_DRAWMODE_OPAQUE);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
}

public Point stringExtent(String string) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	PhRect_t rect = new PhRect_t();
	int size = string.length();
	char[] buffer = new char[size];
	string.getChars(0, size, buffer, 0);

	int flags = OS.PtEnter(0);
	try {
		OS.PfExtentWideText(rect, null, data.font, buffer, size * 2);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
	
	int width;
	if (size == 0) width = 0;
	else width = rect.lr_x - (rect.ul_x < 0 ? rect.ul_x : 0) + 1;
	int height = rect.lr_y - rect.ul_y + 1;
	return new Point(width, height);
}

public Point textExtent(String string) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	PhRect_t rect = new PhRect_t();
	string = replaceTabs(string, 8);
	int size = string.length();
	byte [] buffer = Converter.wcsToMbcs (null, string, false);

	int flags = OS.PtEnter(0);
	try {
		int prevContext = setGC();
		OS.PgExtentMultiText(rect, null, data.font, buffer, buffer.length, 0);
		unsetGC(prevContext);
	} finally {
		if (flags >= 0) OS.PtLeave(flags);
	}
	
	int width;
	if (size == 0) width = 0;
	else width = rect.lr_x - (rect.ul_x < 0 ? rect.ul_x : 0) + 1;
	int height = rect.lr_y - rect.ul_y + 1;
	return new Point(width, height);
}

public String toString () {
	if (isDisposed()) return "GC {*DISPOSED*}";
	return "GC {" + handle + "}";
}

void unsetGC(int prevContext) {
	Image image = data.image;
	if (image != null) {
		OS.PmMemFlush(handle, image.handle);
		OS.PmMemStop(handle);
	} else if (data.rid == OS.Ph_DEV_RID || data.widget != 0) {
		OS.PgSetGC(prevContext);
//		OS.PgFlush();
	}
}

public static GC photon_new(Drawable drawable, GCData data) {
	GC gc = new GC();
	int context = drawable.internal_new_GC(data);
	gc.init(drawable, data, context);
	return gc;
}

}
