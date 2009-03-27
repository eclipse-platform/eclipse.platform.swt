/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;

/**
 * Class <code>GC</code> is where all of the drawing capabilities that are 
 * supported by SWT are located. Instances are used to draw on either an 
 * <code>Image</code>, a <code>Control</code>, or directly on a <code>Display</code>.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * </dl>
 * 
 * <p>
 * The SWT drawing coordinate system is the two-dimensional space with the origin
 * (0,0) at the top left corner of the drawing area and with (x,y) values increasing
 * to the right and downward respectively.
 * </p>
 * 
 * <p>
 * Application code must explicitly invoke the <code>GC.dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required. This is <em>particularly</em>
 * important on Windows95 and Windows98 where the operating system has a limited
 * number of device contexts available.
 * </p>
 * 
 * <p>
 * Note: Only one of LEFT_TO_RIGHT and RIGHT_TO_LEFT may be specified.
 * </p>
 *
 * @see org.eclipse.swt.events.PaintEvent
 * @see <a href="http://www.eclipse.org/swt/snippets/#gc">GC snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Examples: GraphicsExample, PaintExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class GC extends Resource {
	/**
	 * the handle to the OS device context
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public NSGraphicsContext handle;
	
	Drawable drawable;
	GCData data;

	CGPathElement element;
	int count, typeCount;
	byte[] types;
	float /*double*/[] points;
	float /*double*/ [] point;
	
	static final int TAB_COUNT = 32;

	final static int FOREGROUND = 1 << 0;
	final static int BACKGROUND = 1 << 1;
	final static int FONT = 1 << 2;
	final static int LINE_STYLE = 1 << 3;
	final static int LINE_CAP = 1 << 4;
	final static int LINE_JOIN = 1 << 5;
	final static int LINE_WIDTH = 1 << 6;
	final static int LINE_MITERLIMIT = 1 << 7;
	final static int FOREGROUND_FILL = 1 << 8;
	final static int DRAW_OFFSET = 1 << 9;
	final static int CLIPPING = 1 << 10;
	final static int TRANSFORM = 1 << 11;
	final static int VISIBLE_REGION = 1 << 12;
	final static int DRAW = CLIPPING | TRANSFORM | FOREGROUND | LINE_WIDTH | LINE_STYLE  | LINE_CAP  | LINE_JOIN | LINE_MITERLIMIT | DRAW_OFFSET;
	final static int FILL = CLIPPING | TRANSFORM | BACKGROUND;

	static final float[] LINE_DOT = new float[]{1, 1};
	static final float[] LINE_DASH = new float[]{3, 1};
	static final float[] LINE_DASHDOT = new float[]{3, 1, 1, 1};
	static final float[] LINE_DASHDOTDOT = new float[]{3, 1, 1, 1, 1, 1};
	static final float[] LINE_DOT_ZERO = new float[]{3, 3};
	static final float[] LINE_DASH_ZERO = new float[]{18, 6};
	static final float[] LINE_DASHDOT_ZERO = new float[]{9, 6, 3, 6};
	static final float[] LINE_DASHDOTDOT_ZERO = new float[]{9, 3, 3, 3, 3, 3};

GC() {
}

/**	 
 * Constructs a new instance of this class which has been
 * configured to draw on the specified drawable. Sets the
 * foreground color, background color and font in the GC
 * to match those in the drawable.
 * <p>
 * You must dispose the graphics context when it is no longer required. 
 * </p>
 * @param drawable the drawable to draw on
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the drawable is null</li>
 *    <li>ERROR_NULL_ARGUMENT - if there is no current device</li>
 *    <li>ERROR_INVALID_ARGUMENT
 *          - if the drawable is an image that is not a bitmap or an icon
 *          - if the drawable is an image or printer that is already selected
 *            into another graphics context</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for GC creation</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS if not called from the thread that created the drawable</li>
 * </ul>
 */
public GC(Drawable drawable) {
	this(drawable, 0);
}

/**	 
 * Constructs a new instance of this class which has been
 * configured to draw on the specified drawable. Sets the
 * foreground color, background color and font in the GC
 * to match those in the drawable.
 * <p>
 * You must dispose the graphics context when it is no longer required. 
 * </p>
 * 
 * @param drawable the drawable to draw on
 * @param style the style of GC to construct
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the drawable is null</li>
 *    <li>ERROR_NULL_ARGUMENT - if there is no current device</li>
 *    <li>ERROR_INVALID_ARGUMENT
 *          - if the drawable is an image that is not a bitmap or an icon
 *          - if the drawable is an image or printer that is already selected
 *            into another graphics context</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for GC creation</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS if not called from the thread that created the drawable</li>
 * </ul>
 *  
 * @since 2.1.2
 */
public GC(Drawable drawable, int style) {
	if (drawable == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		GCData data = new GCData();
		data.style = checkStyle(style);
		int /*long*/ contextId = drawable.internal_new_GC(data);
		Device device = data.device;
		if (device == null) device = Device.getDevice();
		if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		this.device = data.device = device;
		init(drawable, data, contextId);
		init();
	} finally {
		if (pool != null) pool.release();
	}
}

static int checkStyle (int style) {
	if ((style & SWT.LEFT_TO_RIGHT) != 0) style &= ~SWT.RIGHT_TO_LEFT;
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
}

/**	 
 * Invokes platform specific functionality to allocate a new graphics context.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>GC</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param drawable the Drawable for the receiver.
 * @param data the data for the receiver.
 *
 * @return a new <code>GC</code>
 *
 * @private
 */
public static GC cocoa_new(Drawable drawable, GCData data) {
	GC gc = new GC();
	int /*long*/ context = drawable.internal_new_GC(data);
	gc.device = data.device;
	gc.init(drawable, data, context);
	return gc;
}

int /*long*/ applierFunc(int /*long*/ info, int /*long*/ elementPtr) {
	OS.memmove(element, elementPtr, CGPathElement.sizeof);
	int type = 0, length = 1;
	switch (element.type) {
		case OS.kCGPathElementMoveToPoint: type = SWT.PATH_MOVE_TO; break;
		case OS.kCGPathElementAddLineToPoint: type = SWT.PATH_LINE_TO; break;
		case OS.kCGPathElementAddQuadCurveToPoint: type = SWT.PATH_QUAD_TO; length = 2; break;
		case OS.kCGPathElementAddCurveToPoint: type = SWT.PATH_CUBIC_TO; length = 3; break;
		case OS.kCGPathElementCloseSubpath: type = SWT.PATH_CLOSE; length = 0; break;
	}
	if (types != null) {
		types[typeCount] = (byte)type;
		if (length > 0) {
			OS.memmove(point, element.points, length * CGPoint.sizeof);
			System.arraycopy(point, 0, points, count, length * 2);
		}
	}
	typeCount++;
	count += length * 2;
	return 0;
}

NSAutoreleasePool checkGC (int mask) {
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	if (data.flippedContext != null) {
		NSGraphicsContext.static_saveGraphicsState();
		NSGraphicsContext.setCurrentContext(handle);
	}
	if ((mask & (CLIPPING | TRANSFORM)) != 0) {
		NSView view = data.view;
		if ((data.state & CLIPPING) == 0 || (data.state & TRANSFORM) == 0 || (data.state & VISIBLE_REGION) == 0) {
			boolean antialias = handle.shouldAntialias();
			handle.restoreGraphicsState();
			handle.saveGraphicsState();
			handle.setShouldAntialias(antialias);
			boolean flipped = false;
			if (view != null && (data.paintRect == null || !(flipped = view.isFlipped()))) {
				NSAffineTransform transform = NSAffineTransform.transform();
				NSRect rect = view.convertRect_toView_(view.bounds(), null);
				if (flipped) {
					transform.translateXBy(rect.x, rect.y + rect.height);
				} else {
					transform.translateXBy(0, rect.height);
				}
				transform.scaleXBy(1, -1);
				transform.concat();
				if (data.visibleRgn != 0) {
					if (data.visiblePath == null || (data.state & VISIBLE_REGION) == 0) {
						if (data.visiblePath != null) data.visiblePath.release();
						data.visiblePath = Region.cocoa_new(device, data.visibleRgn).getPath();
					}
					data.visiblePath.addClip();
					data.state |= VISIBLE_REGION;
				}
			}
			if (data.clipPath != null) data.clipPath.addClip();
			if (data.transform != null) data.transform.concat();
			mask &= ~(TRANSFORM | CLIPPING);
			data.state |= TRANSFORM | CLIPPING;
			data.state &= ~(BACKGROUND | FOREGROUND);
		}
	}

	int state = data.state;
	if ((state & mask) == mask) return pool;
	state = (state ^ mask) & mask;	
	data.state |= mask;
	
	if ((state & FOREGROUND) != 0) {
		Pattern pattern = data.foregroundPattern;
		if (pattern != null) {
			if (pattern.color != null) pattern.color.setStroke();
		} else {
			float /*double*/ [] color = data.foreground;
			if (data.fg != null) data.fg.release();
			NSColor fg = data.fg = NSColor.colorWithDeviceRed(color[0], color[1], color[2], data.alpha / 255f);
			fg.retain();
			fg.setStroke();
		}
	}
	if ((state & FOREGROUND_FILL) != 0) {
		Pattern pattern = data.foregroundPattern;
		if (pattern != null) {
			if (pattern.color != null) pattern.color.setFill();
		} else {
			float /*double*/ [] color = data.foreground;
			if (data.fg != null) data.fg.release();
			NSColor fg = data.fg = NSColor.colorWithDeviceRed(color[0], color[1], color[2], data.alpha / 255f);
			fg.retain();
			fg.setFill();
		}
		data.state &= ~BACKGROUND;
	}
	if ((state & BACKGROUND) != 0) {
		Pattern pattern = data.backgroundPattern;
		if (pattern != null) {
			if (pattern.color != null) pattern.color.setFill();
		} else {
			float /*double*/ [] color = data.background;
			if (data.bg != null) data.bg.release();
			NSColor bg = data.bg = NSColor.colorWithDeviceRed(color[0], color[1], color[2], data.alpha / 255f);
			bg.retain();
			bg.setFill();
		}
		data.state &= ~FOREGROUND_FILL;
	}
	NSBezierPath path = data.path;
	if ((state & LINE_WIDTH) != 0) {
		path.setLineWidth(data.lineWidth == 0 ?  1 : data.lineWidth);
		switch (data.lineStyle) {
			case SWT.LINE_DOT:
			case SWT.LINE_DASH:
			case SWT.LINE_DASHDOT:
			case SWT.LINE_DASHDOTDOT:
				state |= LINE_STYLE;
		}
	}
	if ((state & LINE_STYLE) != 0) {
		float[] dashes = null;
		float width = data.lineWidth;
		switch (data.lineStyle) {
			case SWT.LINE_SOLID: break;
			case SWT.LINE_DASH: dashes = width != 0 ? LINE_DASH : LINE_DASH_ZERO; break;
			case SWT.LINE_DOT: dashes = width != 0 ? LINE_DOT : LINE_DOT_ZERO; break;
			case SWT.LINE_DASHDOT: dashes = width != 0 ? LINE_DASHDOT : LINE_DASHDOT_ZERO; break;
			case SWT.LINE_DASHDOTDOT: dashes = width != 0 ? LINE_DASHDOTDOT : LINE_DASHDOTDOT_ZERO; break;
			case SWT.LINE_CUSTOM: dashes = data.lineDashes; break;
		}
		if (dashes != null) {
			float /*double*/[] lengths = new float /*double*/[dashes.length];
			for (int i = 0; i < lengths.length; i++) {
				lengths[i] = width == 0 || data.lineStyle == SWT.LINE_CUSTOM ? dashes[i] : dashes[i] * width;
			}
			path.setLineDash(lengths, lengths.length, data.lineDashesOffset);
		} else {
			path.setLineDash(null, 0, 0);
		}
	}
	if ((state & LINE_MITERLIMIT) != 0) {
		path.setMiterLimit(data.lineMiterLimit);
	}
	if ((state & LINE_JOIN) != 0) {
		int joinStyle = 0;
		switch (data.lineJoin) {
			case SWT.JOIN_MITER: joinStyle = OS.NSMiterLineJoinStyle; break;
			case SWT.JOIN_ROUND: joinStyle = OS.NSRoundLineJoinStyle; break;
			case SWT.JOIN_BEVEL: joinStyle = OS.NSBevelLineJoinStyle; break;
		}
		path.setLineJoinStyle(joinStyle);
	}
	if ((state & LINE_CAP) != 0) {
		int capStyle = 0;
		switch (data.lineCap) {
			case SWT.CAP_ROUND: capStyle = OS.NSRoundLineCapStyle; break;
			case SWT.CAP_FLAT: capStyle = OS.NSButtLineCapStyle; break;
			case SWT.CAP_SQUARE: capStyle = OS.NSSquareLineCapStyle; break;
		}
		path.setLineCapStyle(capStyle);
	}
	if ((state & DRAW_OFFSET) != 0) {
		data.drawXOffset = data.drawYOffset = 0;
		NSSize size = new NSSize();
		size.width = size.height = 1;
		if (data.transform != null) {
			size = data.transform.transformSize(size);
		}
		float /*double*/ scaling = size.width;
		if (scaling < 0) scaling = -scaling;
		float /*double*/ strokeWidth = data.lineWidth * scaling;
		if (strokeWidth == 0 || ((int)strokeWidth % 2) == 1) {
			data.drawXOffset = 0.5f / scaling;
		}
		scaling = size.height;
		if (scaling < 0) scaling = -scaling;
		strokeWidth = data.lineWidth * scaling;
		if (strokeWidth == 0 || ((int)strokeWidth % 2) == 1) {
			data.drawYOffset = 0.5f / scaling;
		}
	}
	return pool;
}

/**
 * Copies a rectangular area of the receiver at the specified
 * position into the image, which must be of type <code>SWT.BITMAP</code>.
 *
 * @param image the image to copy into
 * @param x the x coordinate in the receiver of the area to be copied
 * @param y the y coordinate in the receiver of the area to be copied
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the image is not a bitmap or has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void copyArea(Image image, int x, int y) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image.type != SWT.BITMAP || image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	NSAutoreleasePool pool = checkGC(TRANSFORM | CLIPPING);
	try {
		if (data.image != null) {
			int srcX = x, srcY = y, destX = 0, destY = 0;
			NSSize srcSize = data.image.handle.size();
			int imgHeight = (int)srcSize.height;
			int destWidth = (int)srcSize.width - x, destHeight = (int)srcSize.height - y;
			int srcWidth = destWidth, srcHeight = destHeight;		
			NSGraphicsContext context = NSGraphicsContext.graphicsContextWithBitmapImageRep(image.getRepresentation());
			NSGraphicsContext.static_saveGraphicsState();
			NSGraphicsContext.setCurrentContext(context);
			NSAffineTransform transform = NSAffineTransform.transform();
			NSSize size = image.handle.size();
			transform.translateXBy(0, size.height-(destHeight + 2 * destY));
			transform.concat();
			NSRect srcRect = new NSRect();
			srcRect.x = srcX;
			srcRect.y = imgHeight - (srcY + srcHeight);
			srcRect.width = srcWidth;
			srcRect.height = srcHeight;
			NSRect destRect = new NSRect();
			destRect.x = destX;
			destRect.y = destY;
			destRect.width = destWidth;
			destRect.height = destHeight;
			data.image.handle.drawInRect(destRect, srcRect, OS.NSCompositeCopy, 1);
	 		NSGraphicsContext.static_restoreGraphicsState();
			return;
		}
		if (data.view != null) {
			NSPoint pt = new NSPoint();
			pt.x = x;
			pt.y = y;
			NSWindow window = data.view.window();
			pt = data.view.convertPoint_toView_(pt, window.contentView().superview());
			NSRect frame = window.frame();
			pt.y = frame.height - pt.y;
			NSSize size = image.handle.size();
			CGRect destRect = new CGRect();
			destRect.size.width = size.width;
			destRect.size.height = size.height;
			CGRect srcRect = new CGRect();
			srcRect.origin.x = pt.x;
			srcRect.origin.y = pt.y;
			srcRect.size.width = size.width;
			srcRect.size.height = size.height;
			image.handle.lockFocus();
			NSGraphicsContext context = NSGraphicsContext.currentContext();
			int /*long*/ contextID = OS.objc_msgSend(NSApplication.sharedApplication().id, OS.sel_contextID);
			OS.CGContextCopyWindowContentsToRect(context.graphicsPort(), destRect, contextID, window.windowNumber(), srcRect);
			image.handle.unlockFocus();
			return;
		}
		if (handle.isDrawingToScreen()) {
			NSImage imageHandle = image.handle;
			NSSize size = imageHandle.size();
			CGRect rect = new CGRect();
			rect.origin.x = x;
			rect.origin.y = y;
			rect.size.width = size.width;
			rect.size.height = size.height;
			int displayCount = 16;
			int /*long*/ displays = OS.malloc(4 * displayCount), countPtr = OS.malloc(4);
			if (OS.CGGetDisplaysWithRect(rect, displayCount, displays, countPtr) != 0) return;
			int[] count = new int[1], display = new int[1];
			OS.memmove(count, countPtr, OS.PTR_SIZEOF);
			for (int i = 0; i < count[0]; i++) {
				OS.memmove(display, displays + (i * 4), 4);
				OS.CGDisplayBounds(display[0], rect);
				int /*long*/ address = OS.CGDisplayBaseAddress(display[0]);
				if (address != 0) {
					int /*long*/ width = OS.CGDisplayPixelsWide(display[0]);
					int /*long*/ height = OS.CGDisplayPixelsHigh(display[0]);
					int /*long*/ bpr = OS.CGDisplayBytesPerRow(display[0]);
					int /*long*/ bpp = OS.CGDisplayBitsPerPixel(display[0]);
					int /*long*/ bps = OS.CGDisplayBitsPerSample(display[0]);
					int bitmapInfo = OS.kCGImageAlphaNoneSkipFirst;
					switch ((int)/*63*/bpp) {
						case 16: bitmapInfo |= OS.kCGBitmapByteOrder16Host; break;
						case 32: bitmapInfo |= OS.kCGBitmapByteOrder32Host; break;
					}
					int /*long*/ srcImage = 0;
					if (OS.__BIG_ENDIAN__() && OS.VERSION >= 0x1040) {
						int /*long*/ colorspace = OS.CGColorSpaceCreateDeviceRGB();
						int /*long*/ context = OS.CGBitmapContextCreate(address, width, height, bps, bpr, colorspace, bitmapInfo);
						OS.CGColorSpaceRelease(colorspace);
						srcImage = OS.CGBitmapContextCreateImage(context);
						OS.CGContextRelease(context);
					} else {
						int /*long*/ provider = OS.CGDataProviderCreateWithData(0, address, bpr * height, 0);
						int /*long*/ colorspace = OS.CGColorSpaceCreateDeviceRGB();
						srcImage = OS.CGImageCreate(width, height, bps, bpp, bpr, colorspace, bitmapInfo, provider, 0, true, 0);
						OS.CGColorSpaceRelease(colorspace);
						OS.CGDataProviderRelease(provider);
					}
					copyArea(image, x - (int)rect.origin.x, y - (int)rect.origin.y, srcImage);
					if (srcImage != 0) OS.CGImageRelease(srcImage);
				}
			}
			OS.free(displays);
			OS.free(countPtr);
		}	
	} finally {
		uncheckGC(pool);
	}
}

void copyArea (Image image, int x, int y, int /*long*/ srcImage) {
	if (srcImage == 0) return;
	NSBitmapImageRep rep = image.getRepresentation();
	int /*long*/ bpc = rep.bitsPerSample();
	int /*long*/ width = rep.pixelsWide();
	int /*long*/ height = rep.pixelsHigh();
	int /*long*/ bpr = rep.bytesPerRow();
	int alphaInfo = rep.hasAlpha() ? OS.kCGImageAlphaFirst : OS.kCGImageAlphaNoneSkipFirst;
	int /*long*/ colorspace = OS.CGColorSpaceCreateDeviceRGB();
	int /*long*/ context = OS.CGBitmapContextCreate(rep.bitmapData(), width, height, bpc, bpr, colorspace, alphaInfo);
	OS.CGColorSpaceRelease(colorspace);
	if (context != 0) {
	 	CGRect rect = new CGRect();
	 	rect.origin.x = -x;
	 	rect.origin.y = y;
	 	rect.size.width = OS.CGImageGetWidth(srcImage);
		rect.size.height = OS.CGImageGetHeight(srcImage);
		OS.CGContextTranslateCTM(context, 0, -(rect.size.height - height));
		OS.CGContextDrawImage(context, rect, srcImage);
		OS.CGContextRelease(context);
	}
}

/**
 * Copies a rectangular area of the receiver at the source
 * position onto the receiver at the destination position.
 *
 * @param srcX the x coordinate in the receiver of the area to be copied
 * @param srcY the y coordinate in the receiver of the area to be copied
 * @param width the width of the area to copy
 * @param height the height of the area to copy
 * @param destX the x coordinate in the receiver of the area to copy to
 * @param destY the y coordinate in the receiver of the area to copy to
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void copyArea(int srcX, int srcY, int width, int height, int destX, int destY) {
	copyArea(srcX, srcY, width, height, destX, destY, true);
}
/**
 * Copies a rectangular area of the receiver at the source
 * position onto the receiver at the destination position.
 *
 * @param srcX the x coordinate in the receiver of the area to be copied
 * @param srcY the y coordinate in the receiver of the area to be copied
 * @param width the width of the area to copy
 * @param height the height of the area to copy
 * @param destX the x coordinate in the receiver of the area to copy to
 * @param destY the y coordinate in the receiver of the area to copy to
 * @param paint if <code>true</code> paint events will be generated for old and obscured areas
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1 
 */
public void copyArea(int srcX, int srcY, int width, int height, int destX, int destY, boolean paint) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width <= 0 || height <= 0) return;
	int deltaX = destX - srcX, deltaY = destY - srcY;
	if (deltaX == 0 && deltaY == 0) return;
	NSAutoreleasePool pool = checkGC(TRANSFORM | CLIPPING);
	try {
		Image image = data.image;
		if (image != null) {
			NSImage imageHandle = image.handle;
			NSSize size = imageHandle.size();
		 	int imgHeight = (int)size.height;
			handle.saveGraphicsState();
			NSAffineTransform transform = NSAffineTransform.transform();
			transform.scaleXBy(1, -1);
			transform.translateXBy(0, -(height + 2 * destY));
			transform.concat();
			NSRect srcRect = new NSRect();
			srcRect.x = srcX;
			srcRect.y = imgHeight - (srcY + height);
			srcRect.width = width;
			srcRect.height = height;
			NSRect destRect = new NSRect();
			destRect.x = destX;
			destRect.y = destY;
			destRect.width = width;
			destRect.height = height;
			imageHandle.drawInRect(destRect, srcRect, OS.NSCompositeCopy, 1);
			handle.restoreGraphicsState();
	 		return;
		}
		if (data.view != null) {
			NSView view = data.view;
			NSRect visibleRect = view.visibleRect();
			if (visibleRect.width <= 0 || visibleRect.height <= 0) return;
			NSRect damage = new NSRect();
			damage.x = srcX;
			damage.y = srcY;
			damage.width = width;
			damage.height = height;
			NSPoint dest = new NSPoint();
			dest.x = destX;
			dest.y = destY;

			view.lockFocus();
			OS.NSCopyBits(0, damage , dest);
			view.unlockFocus();

			if (paint) {
				boolean disjoint = (destX + width < srcX) || (srcX + width < destX) || (destY + height < srcY) || (srcY + height < destY);
				if (disjoint) {
					view.setNeedsDisplayInRect(damage);
				} else {
					if (deltaX != 0) {
						int newX = destX - deltaX;
						if (deltaX < 0) newX = destX + width;
						damage.x = newX;
						damage.width = Math.abs(deltaX);
						view.setNeedsDisplayInRect(damage);
					}
					if (deltaY != 0) {
						int newY = destY - deltaY;
						if (deltaY < 0) newY = destY + height;
						damage.x = srcX;
						damage.y = newY;
						damage.width = width;
						damage.height =  Math.abs (deltaY);
						view.setNeedsDisplayInRect(damage);
					}
				}
	
				NSRect srcRect = new NSRect();
				srcRect.x = srcX;
				srcRect.y = srcY;
				srcRect.width = width;
				srcRect.height = height;
				OS.NSIntersectionRect(visibleRect, visibleRect, srcRect);
	
				if (!OS.NSEqualRects(visibleRect, srcRect)) {
					if (srcRect.x != visibleRect.x) {
						damage.x = srcRect.x + deltaX;
						damage.y = srcRect.y + deltaY;
						damage.width = visibleRect.x - srcRect.x;
						damage.height = srcRect.height;
						view.setNeedsDisplayInRect(damage);
					} 
					if (visibleRect.x + visibleRect.width != srcRect.x + srcRect.width) {
						damage.x = srcRect.x + visibleRect.width + deltaX;
						damage.y = srcRect.y + deltaY;
						damage.width = srcRect.width - visibleRect.width;
						damage.height = srcRect.height;
						view.setNeedsDisplayInRect(damage);
					}
					if (visibleRect.y != srcRect.y) {
						damage.x = visibleRect.x + deltaX;
						damage.y = srcRect.y + deltaY;
						damage.width = visibleRect.width;
						damage.height = visibleRect.y - srcRect.y;
						view.setNeedsDisplayInRect(damage);
					}
					if (visibleRect.y + visibleRect.height != srcRect.y + srcRect.height) {
						damage.x = visibleRect.x + deltaX;
						damage.y = visibleRect.y + visibleRect.height + deltaY;
						damage.width = visibleRect.width;
						damage.height = srcRect.y + srcRect.height - (visibleRect.y + visibleRect.height);
						view.setNeedsDisplayInRect(damage);
					}
				}
			}
			return;
		}		
	} finally {
		uncheckGC(pool);
	}
}

int /*long*/ createCGPathRef(NSBezierPath nsPath) {
	int /*long*/ count = nsPath.elementCount();
	if (count > 0) {
		int /*long*/ cgPath = OS.CGPathCreateMutable();
		if (cgPath == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		int /*long*/ points = OS.malloc(NSPoint.sizeof * 3);
		if (points == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		float /*double*/ [] pt = new float /*double*/ [6];
		for (int i = 0; i < count; i++) {
			int element = (int)/*64*/nsPath.elementAtIndex(i, points);
			switch (element) {
				case OS.NSMoveToBezierPathElement:
					OS.memmove(pt, points, NSPoint.sizeof);
					OS.CGPathMoveToPoint(cgPath, 0, pt[0], pt[1]);
					break;
				case OS.NSLineToBezierPathElement:
                	OS.memmove(pt, points, NSPoint.sizeof);
                    OS.CGPathAddLineToPoint(cgPath, 0, pt[0], pt[1]);					
					break;	
				 case OS.NSCurveToBezierPathElement:
					 OS.memmove(pt, points, NSPoint.sizeof * 3);
					 OS.CGPathAddCurveToPoint(cgPath, 0, pt[0], pt[1], pt[2], pt[3], pt[4], pt[5]);
					 break;
                case OS.NSClosePathBezierPathElement:
                     OS.CGPathCloseSubpath(cgPath);
					 break;
			}
		}
		return cgPath;
	}
	return 0;
}



NSBezierPath createNSBezierPath (int /*long*/  cgPath) {
	Callback callback = new Callback(this, "applierFunc", 2);
	int /*long*/  proc = callback.getAddress();
	if (proc == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	count = typeCount = 0;
	element = new CGPathElement();
	OS.CGPathApply(cgPath, 0, proc);
	types = new byte[typeCount];
	points = new float /*double*/ [count];
	point = new float /*double*/ [6];
	count = typeCount = 0;
	OS.CGPathApply(cgPath, 0, proc);
	callback.dispose();

	NSBezierPath bezierPath = NSBezierPath.bezierPath();
	NSPoint nsPoint = new NSPoint(), nsPoint2 = new NSPoint(), nsPoint3 = new NSPoint();
	for (int i = 0, j = 0; i < types.length; i++) {
		switch (types[i]) {
			case SWT.PATH_MOVE_TO:
				nsPoint.x = points[j++];
				nsPoint.y = points[j++];
				bezierPath.moveToPoint(nsPoint);
				break;
			case SWT.PATH_LINE_TO:
				nsPoint.x = points[j++];
				nsPoint.y = points[j++];
				bezierPath.lineToPoint(nsPoint);
				break;
			case SWT.PATH_CUBIC_TO:
				nsPoint2.x = points[j++];
				nsPoint2.y = points[j++];
				nsPoint3.x = points[j++];
				nsPoint3.y = points[j++];
				nsPoint.x = points[j++];
				nsPoint.y = points[j++];
				bezierPath.curveToPoint(nsPoint, nsPoint2, nsPoint3);
				break;
			case SWT.PATH_QUAD_TO:
				float /*double*/ currentX = nsPoint.x;
				float /*double*/ currentY = nsPoint.y;
				nsPoint2.x = points[j++];
				nsPoint2.y = points[j++];
				nsPoint.x = points[j++];
				nsPoint.y = points[j++];
				float /*double*/ x0 = currentX;
				float /*double*/ y0 = currentY;
				float /*double*/ cx1 = x0 + 2 * (nsPoint2.x - x0) / 3;
				float /*double*/ cy1 = y0 + 2 * (nsPoint2.y - y0) / 3;
				float /*double*/ cx2 = cx1 + (nsPoint.x - x0) / 3;
				float /*double*/ cy2 = cy1 + (nsPoint.y - y0) / 3;
				nsPoint2.x = cx1;
				nsPoint2.y = cy1;
				nsPoint3.x = cx2;
				nsPoint3.y = cy2;
				bezierPath.curveToPoint(nsPoint, nsPoint2, nsPoint3);
				break;
			case SWT.PATH_CLOSE:
				bezierPath.closePath();
				break;
			default:
				dispose();
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	element = null;
	types = null;
	points = null;
	nsPoint = null;
	return bezierPath;	
}

NSAttributedString createString(String string, int flags, boolean draw) {
	NSMutableDictionary dict = ((NSMutableDictionary)new NSMutableDictionary().alloc()).initWithCapacity(5);
	Font font = data.font;
	dict.setObject(font.handle, OS.NSFontAttributeName);
	font.addTraits(dict);
	if (draw) {
		Pattern pattern = data.foregroundPattern;
		if (pattern != null) {
			if (pattern.color != null) dict.setObject(pattern.color, OS.NSForegroundColorAttributeName);
		} else {
			NSColor fg = data.fg;
			if (fg == null) {
				float /*double*/ [] color = data.foreground;
				fg = data.fg = NSColor.colorWithDeviceRed(color[0], color[1], color[2], data.alpha / 255f);
				fg.retain();
			}
			dict.setObject(fg, OS.NSForegroundColorAttributeName);
		}
	}
	if ((flags & SWT.DRAW_TAB) == 0) {
		dict.setObject(device.paragraphStyle, OS.NSParagraphStyleAttributeName);
	}
	int length = string.length();
	char[] chars = new char[length];
	string.getChars(0, length, chars, 0);
	int breakCount = 0;
	int[] breaks = null;
	if ((flags & SWT.DRAW_MNEMONIC) !=0 || (flags & SWT.DRAW_DELIMITER) == 0) {
		int i=0, j=0;
		while (i < chars.length) {
			char c = chars [j++] = chars [i++];
			switch (c) {
				case '&': {
					if ((flags & SWT.DRAW_MNEMONIC) != 0) {
						if (i == chars.length) {continue;}
						if (chars [i] == '&') {i++; continue;}
						j--;
					}
					break;
				}
				case '\r':
				case '\n': {
					if ((flags & SWT.DRAW_DELIMITER) == 0) {
						if (c == '\r' && i != chars.length && chars[i] == '\n') i++;
						j--;
						if (breaks == null) {
							breaks = new int[4];
						} else if (breakCount == breaks.length) {
							int[] newBreaks = new int[breaks.length + 4];
							System.arraycopy(breaks, 0, newBreaks, 0, breaks.length);
							breaks = newBreaks;
						}
						breaks[breakCount++] = j;
					}
					break;
				}
			}
		}
		length = j;
	}
	NSString str = ((NSString)new NSString().alloc()).initWithCharacters(chars, length);
	NSAttributedString attribStr = ((NSAttributedString)new NSAttributedString().alloc()).initWithString(str, dict);
	dict.release();
	str.release();
	return attribStr;
}

void destroy() {
	/* Free resources */
	Image image = data.image;
	if (image != null) {
		image.memGC = null;
		image.createAlpha();
	}
	if (data.fg != null) data.fg.release();
	if (data.bg != null) data.bg.release();
	if (data.path != null) data.path.release();
	if (data.clipPath != null) data.clipPath.release();
	if (data.visiblePath != null) data.visiblePath.release();
	if (data.transform != null) data.transform.release();
	if (data.inverseTransform != null) data.inverseTransform.release();
	data.path = data.clipPath = data.visiblePath = null;
	data.transform = data.inverseTransform = null;
	data.fg = data.bg = null;
	
	/* Dispose the GC */
	if (drawable != null) drawable.internal_dispose_GC(handle.id, data);
	handle.restoreGraphicsState();
	handle.release();

	drawable = null;
	data.image = null;
	data = null;
	handle = null;
}

/**
 * Draws the outline of a circular or elliptical arc 
 * within the specified rectangular area.
 * <p>
 * The resulting arc begins at <code>startAngle</code> and extends  
 * for <code>arcAngle</code> degrees, using the current color.
 * Angles are interpreted such that 0 degrees is at the 3 o'clock
 * position. A positive value indicates a counter-clockwise rotation
 * while a negative value indicates a clockwise rotation.
 * </p><p>
 * The center of the arc is the center of the rectangle whose origin 
 * is (<code>x</code>, <code>y</code>) and whose size is specified by the 
 * <code>width</code> and <code>height</code> arguments. 
 * </p><p>
 * The resulting arc covers an area <code>width + 1</code> pixels wide
 * by <code>height + 1</code> pixels tall.
 * </p>
 *
 * @param x the x coordinate of the upper-left corner of the arc to be drawn
 * @param y the y coordinate of the upper-left corner of the arc to be drawn
 * @param width the width of the arc to be drawn
 * @param height the height of the arc to be drawn
 * @param startAngle the beginning angle
 * @param arcAngle the angular extent of the arc, relative to the start angle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || arcAngle == 0) return;
	NSAutoreleasePool pool = checkGC(DRAW);
	try {
		handle.saveGraphicsState();
		NSAffineTransform transform = NSAffineTransform.transform();
		float /*double*/ xOffset = data.drawXOffset, yOffset = data.drawYOffset;
		transform.translateXBy(x + xOffset + width / 2f, y + yOffset + height / 2f);
		transform.scaleXBy(width / 2f, height / 2f);
		NSBezierPath path = data.path;
		NSPoint center = new NSPoint();
		float sAngle = -startAngle;
		float eAngle = -(startAngle + arcAngle);
		path.appendBezierPathWithArcWithCenter(center, 1, sAngle,  eAngle, arcAngle>0);
		path.transformUsingAffineTransform(transform);
		Pattern pattern = data.foregroundPattern;
		if (pattern != null && pattern.gradient != null) {
			strokePattern(path, pattern);
		} else {
			path.stroke();
		}
		path.removeAllPoints();
		handle.restoreGraphicsState();
	} finally {
		uncheckGC(pool);
	}
}

/** 
 * Draws a rectangle, based on the specified arguments, which has
 * the appearance of the platform's <em>focus rectangle</em> if the
 * platform supports such a notion, and otherwise draws a simple
 * rectangle in the receiver's foreground color.
 *
 * @param x the x coordinate of the rectangle
 * @param y the y coordinate of the rectangle
 * @param width the width of the rectangle
 * @param height the height of the rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRectangle(int, int, int, int)
 */
public void drawFocus(int x, int y, int width, int height) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	NSAutoreleasePool pool = checkGC(CLIPPING | TRANSFORM);
	try {
		//	int[] metric = new int[1];
		//	OS.GetThemeMetric(OS.kThemeMetricFocusRectOutset, metric);
		//	CGRect rect = new CGRect ();
		//	rect.x = x + metric[0];
		//	rect.y = y + metric[0];
		//	rect.width = width - metric[0] * 2;
		//	rect.height = height - metric[0] * 2;
		//	OS.HIThemeDrawFocusRect(rect, true, handle, OS.kHIThemeOrientationNormal);
	} finally {
		uncheckGC(pool);
	}
}

/**
 * Draws the given image in the receiver at the specified
 * coordinates.
 *
 * @param image the image to draw
 * @param x the x coordinate of where to draw
 * @param y the y coordinate of where to draw
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the given coordinates are outside the bounds of the image</li>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if no handles are available to perform the operation</li>
 * </ul>
 */
public void drawImage(Image image, int x, int y) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	drawImage(image, 0, 0, -1, -1, x, y, -1, -1, true);
}

/**
 * Copies a rectangular area from the source image into a (potentially
 * different sized) rectangular area in the receiver. If the source
 * and destination areas are of differing sizes, then the source
 * area will be stretched or shrunk to fit the destination area
 * as it is copied. The copy fails if any part of the source rectangle
 * lies outside the bounds of the source image, or if any of the width
 * or height arguments are negative.
 *
 * @param image the source image
 * @param srcX the x coordinate in the source image to copy from
 * @param srcY the y coordinate in the source image to copy from
 * @param srcWidth the width in pixels to copy from the source
 * @param srcHeight the height in pixels to copy from the source
 * @param destX the x coordinate in the destination to copy to
 * @param destY the y coordinate in the destination to copy to
 * @param destWidth the width in pixels of the destination rectangle
 * @param destHeight the height in pixels of the destination rectangle
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 *    <li>ERROR_INVALID_ARGUMENT - if any of the width or height arguments are negative.
 *    <li>ERROR_INVALID_ARGUMENT - if the source rectangle is not contained within the bounds of the source image</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if no handles are available to perform the operation</li>
 * </ul>
 */
public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (srcWidth == 0 || srcHeight == 0 || destWidth == 0 || destHeight == 0) return;
	if (srcX < 0 || srcY < 0 || srcWidth < 0 || srcHeight < 0 || destWidth < 0 || destHeight < 0) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (image.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	drawImage(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight, false);
}

void drawImage(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, boolean simple) {
	NSImage imageHandle = srcImage.handle;
	NSSize size = imageHandle.size();
 	int imgWidth = (int)size.width;
 	int imgHeight = (int)size.height;
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
	NSAutoreleasePool pool = checkGC(CLIPPING | TRANSFORM);
	try {
		if (srcImage.memGC != null) {
			srcImage.createAlpha();
		}
		handle.saveGraphicsState();
		NSAffineTransform transform = NSAffineTransform.transform();
		transform.scaleXBy(1, -1);
		transform.translateXBy(0, -(destHeight + 2 * destY));
		transform.concat();
		NSRect srcRect = new NSRect();
		srcRect.x = srcX;
		srcRect.y = imgHeight - (srcY + srcHeight);
		srcRect.width = srcWidth;
		srcRect.height = srcHeight;
		NSRect destRect = new NSRect();
		destRect.x = destX;
		destRect.y = destY;
		destRect.width = destWidth;
		destRect.height = destHeight;
		imageHandle.drawInRect(destRect, srcRect, OS.NSCompositeSourceOver, 1);
		handle.restoreGraphicsState();
	} finally {
		uncheckGC(pool);
	}
}

/** 
 * Draws a line, using the foreground color, between the points 
 * (<code>x1</code>, <code>y1</code>) and (<code>x2</code>, <code>y2</code>).
 *
 * @param x1 the first point's x coordinate
 * @param y1 the first point's y coordinate
 * @param x2 the second point's x coordinate
 * @param y2 the second point's y coordinate
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawLine(int x1, int y1, int x2, int y2) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	NSAutoreleasePool pool = checkGC(DRAW);
	try {
		NSBezierPath path = data.path;
		NSPoint pt = new NSPoint();
		pt.x = x1 + data.drawXOffset;
		pt.y = y1 + data.drawYOffset;
		path.moveToPoint(pt);
		pt.x = x2 + data.drawXOffset;
		pt.y = y2 + data.drawYOffset;
		path.lineToPoint(pt);
		Pattern pattern = data.foregroundPattern;
		if (pattern != null && pattern.gradient != null) {
			strokePattern(path, pattern);
		} else {
			path.stroke();
		}
		path.removeAllPoints();
	} finally {
		uncheckGC(pool);
	}
}

/** 
 * Draws the outline of an oval, using the foreground color,
 * within the specified rectangular area.
 * <p>
 * The result is a circle or ellipse that fits within the 
 * rectangle specified by the <code>x</code>, <code>y</code>, 
 * <code>width</code>, and <code>height</code> arguments. 
 * </p><p> 
 * The oval covers an area that is <code>width + 1</code> 
 * pixels wide and <code>height + 1</code> pixels tall.
 * </p>
 *
 * @param x the x coordinate of the upper left corner of the oval to be drawn
 * @param y the y coordinate of the upper left corner of the oval to be drawn
 * @param width the width of the oval to be drawn
 * @param height the height of the oval to be drawn
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawOval(int x, int y, int width, int height) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	NSAutoreleasePool pool = checkGC(DRAW);
	try {
		if (width < 0) {
			x = x + width;
			width = -width;
		}
		if (height < 0) {
			y = y + height;
			height = -height;
		}
		NSBezierPath path = data.path;
		NSRect rect = new NSRect();
		rect.x = x + data.drawXOffset;
		rect.y = y + data.drawXOffset;
		rect.width = width;
		rect.height = height;
		path.appendBezierPathWithOvalInRect(rect);
		Pattern pattern = data.foregroundPattern;
		if (pattern != null && pattern.gradient != null) {
			strokePattern(path, pattern);
		} else {
			path.stroke();
		}
		path.removeAllPoints();
	} finally {
		uncheckGC(pool);
	}
}

/** 
 * Draws the path described by the parameter.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * 
 * @param path the path to draw
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see Path
 * 
 * @since 3.1
 */
public void drawPath(Path path) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (path.handle == null) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	NSAutoreleasePool pool = checkGC(DRAW);
	try {
		handle.saveGraphicsState();
		NSAffineTransform transform = NSAffineTransform.transform();
		transform.translateXBy(data.drawXOffset, data.drawYOffset);
		transform.concat();
		NSBezierPath drawPath = data.path;
		drawPath.appendBezierPath(path.handle);
		Pattern pattern = data.foregroundPattern;
		if (pattern != null && pattern.gradient != null) {
			strokePattern(drawPath, pattern);
		} else {
			drawPath.stroke();
		}
		drawPath.removeAllPoints();
		handle.restoreGraphicsState();
	} finally {
		uncheckGC(pool);
	}
}

/** 
 * Draws a pixel, using the foreground color, at the specified
 * point (<code>x</code>, <code>y</code>).
 * <p>
 * Note that the receiver's line attributes do not affect this
 * operation.
 * </p>
 *
 * @param x the point's x coordinate
 * @param y the point's y coordinate
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *  
 * @since 3.0
 */
public void drawPoint(int x, int y) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	NSAutoreleasePool pool = checkGC(FOREGROUND_FILL | CLIPPING | TRANSFORM);
	try {
		NSRect rect = new NSRect();
		rect.x = x;
		rect.y = y;
		rect.width = 1;
		rect.height = 1;
		NSBezierPath path = data.path;
		path.appendBezierPathWithRect(rect);
		path.fill();
		path.removeAllPoints();
	} finally {
		uncheckGC(pool);
	}
}

/** 
 * Draws the closed polygon which is defined by the specified array
 * of integer coordinates, using the receiver's foreground color. The array 
 * contains alternating x and y values which are considered to represent
 * points which are the vertices of the polygon. Lines are drawn between
 * each consecutive pair, and between the first pair and last pair in the
 * array.
 *
 * @param pointArray an array of alternating x and y values which are the vertices of the polygon
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT if pointArray is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawPolygon(int[] pointArray) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (pointArray.length < 4) return;
	NSAutoreleasePool pool = checkGC(DRAW);
	try {
		float /*double*/ xOffset = data.drawXOffset, yOffset = data.drawYOffset;
		NSBezierPath path = data.path;
		NSPoint pt = new NSPoint();
		pt.x = pointArray[0] + xOffset;
		pt.y = pointArray[1] + yOffset;
		path.moveToPoint(pt);
		int end = pointArray.length / 2 * 2;
		for (int i = 2; i < end; i+=2) {
			pt.x = pointArray[i] + xOffset;
			pt.y = pointArray[i+1] + yOffset;
			path.lineToPoint(pt);
		}
		path.closePath();
		Pattern pattern = data.foregroundPattern;
		if (pattern != null && pattern.gradient != null) {
			strokePattern(path, pattern);
		} else {
			path.stroke();
		}
		path.removeAllPoints();
	} finally {
		uncheckGC(pool);
	}
}

/** 
 * Draws the polyline which is defined by the specified array
 * of integer coordinates, using the receiver's foreground color. The array 
 * contains alternating x and y values which are considered to represent
 * points which are the corners of the polyline. Lines are drawn between
 * each consecutive pair, but not between the first pair and last pair in
 * the array.
 *
 * @param pointArray an array of alternating x and y values which are the corners of the polyline
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point array is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawPolyline(int[] pointArray) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (pointArray.length < 4) return;
	NSAutoreleasePool pool = checkGC(DRAW);
	try {
		float /*double*/ xOffset = data.drawXOffset, yOffset = data.drawYOffset;
		NSBezierPath path = data.path;
		NSPoint pt = new NSPoint();
		pt.x = pointArray[0] + xOffset;
		pt.y = pointArray[1] + yOffset;
		path.moveToPoint(pt);
		int end = pointArray.length / 2 * 2;
		for (int i = 2; i < end; i+=2) {
			pt.x = pointArray[i] + xOffset;
			pt.y = pointArray[i+1] + yOffset;
			path.lineToPoint(pt);
		}
		Pattern pattern = data.foregroundPattern;
		if (pattern != null && pattern.gradient != null) {
			strokePattern(path, pattern);
		} else {
			path.stroke();
		}
		path.removeAllPoints();
	} finally {
		uncheckGC(pool);
	}
}

/** 
 * Draws the outline of the rectangle specified by the arguments,
 * using the receiver's foreground color. The left and right edges
 * of the rectangle are at <code>x</code> and <code>x + width</code>. 
 * The top and bottom edges are at <code>y</code> and <code>y + height</code>. 
 *
 * @param x the x coordinate of the rectangle to be drawn
 * @param y the y coordinate of the rectangle to be drawn
 * @param width the width of the rectangle to be drawn
 * @param height the height of the rectangle to be drawn
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawRectangle(int x, int y, int width, int height) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	NSAutoreleasePool pool = checkGC(DRAW);
	try {
		if (width < 0) {
			x = x + width;
			width = -width;
		}
		if (height < 0) {
			y = y + height;
			height = -height;
		}
		NSRect rect = new NSRect();
		rect.x = x + data.drawXOffset;
		rect.y = y + data.drawYOffset;
		rect.width = width;
		rect.height = height;
		NSBezierPath path = data.path;
		path.appendBezierPathWithRect(rect);
		Pattern pattern = data.foregroundPattern;
		if (pattern != null && pattern.gradient != null) {
			strokePattern(path, pattern);
		} else {
			path.stroke();
		}
		path.removeAllPoints();
	} finally {
		uncheckGC(pool);
	}
}

/** 
 * Draws the outline of the specified rectangle, using the receiver's
 * foreground color. The left and right edges of the rectangle are at
 * <code>rect.x</code> and <code>rect.x + rect.width</code>. The top 
 * and bottom edges are at <code>rect.y</code> and 
 * <code>rect.y + rect.height</code>. 
 *
 * @param rect the rectangle to draw
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the rectangle is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawRectangle(Rectangle rect) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	drawRectangle (rect.x, rect.y, rect.width, rect.height);
}

/** 
 * Draws the outline of the round-cornered rectangle specified by 
 * the arguments, using the receiver's foreground color. The left and
 * right edges of the rectangle are at <code>x</code> and <code>x + width</code>. 
 * The top and bottom edges are at <code>y</code> and <code>y + height</code>.
 * The <em>roundness</em> of the corners is specified by the 
 * <code>arcWidth</code> and <code>arcHeight</code> arguments, which
 * are respectively the width and height of the ellipse used to draw
 * the corners.
 *
 * @param x the x coordinate of the rectangle to be drawn
 * @param y the y coordinate of the rectangle to be drawn
 * @param width the width of the rectangle to be drawn
 * @param height the height of the rectangle to be drawn
 * @param arcWidth the width of the arc
 * @param arcHeight the height of the arc
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (arcWidth == 0 || arcHeight == 0) {
		drawRectangle(x, y, width, height);
    	return;
	}
	NSAutoreleasePool pool = checkGC(DRAW);
	try {
		NSBezierPath path = data.path;
		NSRect rect = new NSRect();
		rect.x = x + data.drawXOffset;
		rect.y = y + data.drawYOffset;
		rect.width = width;
		rect.height = height;
		path.appendBezierPathWithRoundedRect(rect, arcWidth, arcHeight);
		Pattern pattern = data.foregroundPattern;
		if (pattern != null && pattern.gradient != null) {
			strokePattern(path, pattern);
		} else {
			path.stroke();
		}
		path.removeAllPoints();
	} finally {
		uncheckGC(pool);
	}
}

/** 
 * Draws the given string, using the receiver's current font and
 * foreground color. No tab expansion or carriage return processing
 * will be performed. The background of the rectangular area where
 * the string is being drawn will be filled with the receiver's
 * background color.
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the string is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the string is to be drawn
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawString (String string, int x, int y) {
	drawString(string, x, y, false);
}

/** 
 * Draws the given string, using the receiver's current font and
 * foreground color. No tab expansion or carriage return processing
 * will be performed. If <code>isTransparent</code> is <code>true</code>,
 * then the background of the rectangular area where the string is being
 * drawn will not be modified, otherwise it will be filled with the
 * receiver's background color.
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the string is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the string is to be drawn
 * @param isTransparent if <code>true</code> the background will be transparent, otherwise it will be opaque
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawString(String string, int x, int y, boolean isTransparent) {
	drawText(string, x, y, isTransparent ? SWT.DRAW_TRANSPARENT : 0);
}

/** 
 * Draws the given string, using the receiver's current font and
 * foreground color. Tab expansion and carriage return processing
 * are performed. The background of the rectangular area where
 * the text is being drawn will be filled with the receiver's
 * background color.
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawText(String string, int x, int y) {
	drawText(string, x, y, SWT.DRAW_DELIMITER | SWT.DRAW_TAB);
}

/** 
 * Draws the given string, using the receiver's current font and
 * foreground color. Tab expansion and carriage return processing
 * are performed. If <code>isTransparent</code> is <code>true</code>,
 * then the background of the rectangular area where the text is being
 * drawn will not be modified, otherwise it will be filled with the
 * receiver's background color.
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param isTransparent if <code>true</code> the background will be transparent, otherwise it will be opaque
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawText(String string, int x, int y, boolean isTransparent) {
	int flags = SWT.DRAW_DELIMITER | SWT.DRAW_TAB;
	if (isTransparent) flags |= SWT.DRAW_TRANSPARENT;
	drawText(string, x, y, flags);
}

/** 
 * Draws the given string, using the receiver's current font and
 * foreground color. Tab expansion, line delimiter and mnemonic
 * processing are performed according to the specified flags. If
 * <code>flags</code> includes <code>DRAW_TRANSPARENT</code>,
 * then the background of the rectangular area where the text is being
 * drawn will not be modified, otherwise it will be filled with the
 * receiver's background color.
 * <p>
 * The parameter <code>flags</code> may be a combination of:
 * <dl>
 * <dt><b>DRAW_DELIMITER</b></dt>
 * <dd>draw multiple lines</dd>
 * <dt><b>DRAW_TAB</b></dt>
 * <dd>expand tabs</dd>
 * <dt><b>DRAW_MNEMONIC</b></dt>
 * <dd>underline the mnemonic character</dd>
 * <dt><b>DRAW_TRANSPARENT</b></dt>
 * <dd>transparent background</dd>
 * </dl>
 * </p>
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param flags the flags specifying how to process the text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawText (String string, int x, int y, int flags) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	NSAutoreleasePool pool = checkGC(CLIPPING | TRANSFORM | FONT);
	try {
		NSAttributedString str = createString(string, flags, true);
		if ((flags & SWT.DRAW_TRANSPARENT) == 0) {
			NSSize size = str.size();
			NSRect rect = new NSRect();
			rect.x = x;
			rect.y = y;
			rect.width = size.width;
			rect.height = size.height;
			NSColor bg = data.bg;
			if (bg == null) {
				float /*double*/ [] color = data.background;
				bg = data.bg = NSColor.colorWithDeviceRed(color[0], color[1], color[2], data.alpha / 255f);
				bg.retain();
			}
			bg.setFill();
			data.state &= ~FOREGROUND_FILL;
			NSBezierPath.fillRect(rect);
			str.drawInRect(rect);
		} else {
			NSPoint pt = new NSPoint();
			pt.x = x;
			pt.y = y;
			str.drawAtPoint(pt);
		}
		str.release();
	} finally {
		uncheckGC(pool);
	}
}

/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param object the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode
 */
public boolean equals(Object object) {
	if (object == this) return true;
	if (!(object instanceof GC)) return false;
	return handle == ((GC)object).handle;
}

/**
 * Fills the interior of a circular or elliptical arc within
 * the specified rectangular area, with the receiver's background
 * color.
 * <p>
 * The resulting arc begins at <code>startAngle</code> and extends  
 * for <code>arcAngle</code> degrees, using the current color.
 * Angles are interpreted such that 0 degrees is at the 3 o'clock
 * position. A positive value indicates a counter-clockwise rotation
 * while a negative value indicates a clockwise rotation.
 * </p><p>
 * The center of the arc is the center of the rectangle whose origin 
 * is (<code>x</code>, <code>y</code>) and whose size is specified by the 
 * <code>width</code> and <code>height</code> arguments. 
 * </p><p>
 * The resulting arc covers an area <code>width + 1</code> pixels wide
 * by <code>height + 1</code> pixels tall.
 * </p>
 *
 * @param x the x coordinate of the upper-left corner of the arc to be filled
 * @param y the y coordinate of the upper-left corner of the arc to be filled
 * @param width the width of the arc to be filled
 * @param height the height of the arc to be filled
 * @param startAngle the beginning angle
 * @param arcAngle the angular extent of the arc, relative to the start angle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawArc
 */
public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || arcAngle == 0) return;
	NSAutoreleasePool pool = checkGC(FILL);
	try {
		handle.saveGraphicsState();
		NSAffineTransform transform = NSAffineTransform.transform();
		float /*double*/ xOffset = data.drawXOffset, yOffset = data.drawYOffset;
		transform.translateXBy(x + xOffset + width / 2f, y + yOffset + height / 2f);
		transform.scaleXBy(width / 2f, height / 2f);
		NSBezierPath path = data.path;
		NSPoint center = new NSPoint();
		path.moveToPoint(center);
		float sAngle = -startAngle;
		float eAngle = -(startAngle + arcAngle);
		path.appendBezierPathWithArcWithCenter(center, 1, sAngle,  eAngle, arcAngle>0);
		path.closePath();
		path.transformUsingAffineTransform(transform);
		Pattern pattern = data.backgroundPattern;
		if (pattern != null && pattern.gradient != null) {
			fillPattern(path, pattern);
		} else {
			path.fill();
		}
		path.removeAllPoints();
		handle.restoreGraphicsState();
	} finally {
		uncheckGC(pool);
	}
}

/**
 * Fills the interior of the specified rectangle with a gradient
 * sweeping from left to right or top to bottom progressing
 * from the receiver's foreground color to its background color.
 *
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled, may be negative
 *        (inverts direction of gradient if horizontal)
 * @param height the height of the rectangle to be filled, may be negative
 *        (inverts direction of gradient if vertical)
 * @param vertical if true sweeps from top to bottom, else 
 *        sweeps from left to right
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRectangle(int, int, int, int)
 */
public void fillGradientRectangle(int x, int y, int width, int height, boolean vertical) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if ((width == 0) || (height == 0)) return;
	NSAutoreleasePool pool = checkGC(CLIPPING | TRANSFORM);
	try {
		RGB backgroundRGB, foregroundRGB;
		backgroundRGB = getBackground().getRGB();
		foregroundRGB = getForeground().getRGB();

		RGB fromRGB, toRGB;
		fromRGB = foregroundRGB;
		toRGB   = backgroundRGB;
		boolean swapColors = false;
		if (width < 0) {
			x += width; width = -width;
			if (! vertical) swapColors = true;
		}
		if (height < 0) {
			y += height; height = -height;
			if (vertical) swapColors = true;
		}
		if (swapColors) {
			fromRGB = backgroundRGB;
			toRGB   = foregroundRGB;
		}
		if (fromRGB.equals(toRGB)) {
			fillRectangle(x, y, width, height);
		} else {
			NSColor startingColor = NSColor.colorWithDeviceRed(fromRGB.red / 255f, fromRGB.green / 255f, fromRGB.blue / 255f, data.alpha / 255f);
			NSColor endingColor = NSColor.colorWithDeviceRed(toRGB.red / 255f, toRGB.green / 255f, toRGB.blue / 255f, data.alpha / 255f);
			NSGradient gradient = ((NSGradient)new NSGradient().alloc()).initWithStartingColor(startingColor, endingColor);
			NSRect rect = new NSRect();
			rect.x = x;
			rect.y = y;
			rect.width = width;
			rect.height = height;
			gradient.drawInRect(rect, vertical ? 90 : 0);
			gradient.release();
		}
	} finally {
		uncheckGC(pool);
	}
}

/** 
 * Fills the interior of an oval, within the specified
 * rectangular area, with the receiver's background
 * color.
 *
 * @param x the x coordinate of the upper left corner of the oval to be filled
 * @param y the y coordinate of the upper left corner of the oval to be filled
 * @param width the width of the oval to be filled
 * @param height the height of the oval to be filled
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawOval
 */
public void fillOval(int x, int y, int width, int height) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	NSAutoreleasePool pool = checkGC(FILL);
	try {
		if (width < 0) {
			x = x + width;
			width = -width;
		}
		if (height < 0) {
			y = y + height;
			height = -height;
		}
		NSBezierPath path = data.path;
		NSRect rect = new NSRect();
		rect.x = x;
		rect.y = y;
		rect.width = width;
		rect.height = height;
		path.appendBezierPathWithOvalInRect(rect);
		Pattern pattern = data.backgroundPattern;
		if (pattern != null && pattern.gradient != null) {
			fillPattern(path, pattern);
		} else {
			path.fill();
		}
		path.removeAllPoints();
	} finally {
		uncheckGC(pool);
	}
}

void fillPattern(NSBezierPath path, Pattern pattern) {
	handle.saveGraphicsState();
	path.addClip();
	NSRect bounds = path.bounds();
	NSPoint start = new NSPoint();
	start.x = pattern.pt1.x;
	start.y = pattern.pt1.y;
	NSPoint end = new NSPoint();
	end.x = pattern.pt2.x;
	end.y = pattern.pt2.y;
	float /*double*/ difx = end.x - start.x;
	float /*double*/ dify = end.y - start.y;
	if (difx == 0 && dify == 0) {
		float /*double*/ [] color = pattern.color1;
		NSColor.colorWithDeviceRed(color[0], color[1], color[2], data.alpha / 255f).setFill();
		path.fill();
		handle.restoreGraphicsState();
		return;
	}
	float /*double*/ startx, starty, endx, endy;
	if (difx == 0 || dify == 0) {
		startx = bounds.x;
		starty = bounds.y;
		endx = bounds.x + bounds.width;
		endy = bounds.y + bounds.height;
		if (difx < 0 || dify < 0) {
			startx = endx;
			starty = endy;
			endx = bounds.x;
			endy = bounds.y;
		}
	} else {
		float /*double*/ m = (end.y-start.y)/(end.x - start.x);
		float /*double*/ b = end.y - (m * end.x);
		float /*double*/ m2 = -1/m; //perpendicular slope
		float /*double*/ b2 = bounds.y - (m2 * bounds.x);
		startx = endx = (b - b2) / (m2 - m);
		b2 = (bounds.y + bounds.height) - (m2 * bounds.x);
		float /*double*/ x2 = (b - b2) / (m2 - m);
		startx = difx > 0 ? Math.min(startx, x2) : Math.max(startx, x2);
		endx = difx < 0 ? Math.min(endx, x2) : Math.max(endx, x2);
		b2 = bounds.y - (m2 * (bounds.x + bounds.width));
		x2 = (b - b2) / (m2 - m);
		startx = difx > 0 ? Math.min(startx, x2) : Math.max(startx, x2);
		endx = difx < 0 ? Math.min(endx, x2) : Math.max(endx, x2);
		b2 = (bounds.y + bounds.height) - (m2 * (bounds.x + bounds.width));
		x2 = (b - b2) / (m2 - m);
		startx = difx > 0 ? Math.min(startx, x2) : Math.max(startx, x2);
		endx = difx < 0 ? Math.min(endx, x2) : Math.max(endx, x2);
		starty = (m * startx) + b;
		endy = (m * endx) + b;
	}
	if (difx != 0) {
		while ((difx > 0 && start.x >= startx) || (difx < 0 && start.x <= startx)) {
			start.x -= difx;
			start.y -= dify;
		}
	} else {
		while ((dify > 0 && start.y >= starty) || (dify < 0 && start.y <= starty)) {
			start.x -= difx;
			start.y -= dify;
		}
	}
	end.x = start.x;
	end.y = start.y;
	do {
		end.x += difx;
		end.y += dify;
		pattern.gradient.drawFromPoint(start, end, 0);
		start.x = end.x;
		start.y = end.y;
	} while (
				(difx > 0  && end.x <= endx) ||
				(difx < 0  && end.x >= endx) ||
				(difx == 0 && ((dify > 0  && end.y <= endy) || (dify < 0  && end.y >= endy)))
			);
	handle.restoreGraphicsState();
}

/** 
 * Fills the path described by the parameter.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 *
 * @param path the path to fill
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see Path
 * 
 * @since 3.1
 */
public void fillPath(Path path) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (path.handle == null) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	NSAutoreleasePool pool = checkGC(FILL);
	try {
		NSBezierPath drawPath = data.path;
		drawPath.appendBezierPath(path.handle);
		Pattern pattern = data.backgroundPattern;
		if (pattern != null && pattern.gradient != null) {
			fillPattern(drawPath, pattern);
		} else {
			drawPath.fill();
		}
		drawPath.removeAllPoints();
	} finally {
		uncheckGC(pool);
	}
}

/** 
 * Fills the interior of the closed polygon which is defined by the
 * specified array of integer coordinates, using the receiver's
 * background color. The array contains alternating x and y values
 * which are considered to represent points which are the vertices of
 * the polygon. Lines are drawn between each consecutive pair, and
 * between the first pair and last pair in the array.
 *
 * @param pointArray an array of alternating x and y values which are the vertices of the polygon
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT if pointArray is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawPolygon	
 */
public void fillPolygon(int[] pointArray) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pointArray == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (pointArray.length < 4) return;
	NSAutoreleasePool pool = checkGC(FILL);
	try {
		NSBezierPath path = data.path;
		NSPoint pt = new NSPoint();
		pt.x = pointArray[0];
		pt.y = pointArray[1];
		path.moveToPoint(pt);
		int end = pointArray.length / 2 * 2;
		for (int i = 2; i < end; i+=2) {
			pt.x = pointArray[i];
			pt.y = pointArray[i+1];
			path.lineToPoint(pt);
		}
		path.closePath();
		Pattern pattern = data.backgroundPattern;
		if (pattern != null && pattern.gradient != null) {
			fillPattern(path, pattern);
		} else {
			path.fill();
		}
		path.removeAllPoints();
	} finally {
		uncheckGC(pool);
	}
}

/** 
 * Fills the interior of the rectangle specified by the arguments,
 * using the receiver's background color. 
 *
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled
 * @param height the height of the rectangle to be filled
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRectangle(int, int, int, int)
 */
public void fillRectangle(int x, int y, int width, int height) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	NSAutoreleasePool pool = checkGC(FILL);
	try {
		if (width < 0) {
			x = x + width;
			width = -width;
		}
		if (height < 0) {
			y = y + height;
			height = -height;
		}
		NSRect rect = new NSRect();
		rect.x = x;
		rect.y = y;
		rect.width = width;
		rect.height = height;
		NSBezierPath path = data.path;
		path.appendBezierPathWithRect(rect);
		Pattern pattern = data.backgroundPattern;
		if (pattern != null && pattern.gradient != null) {
			fillPattern(path, pattern);
		} else {
			path.fill();
		}
		path.removeAllPoints();
	} finally {
		uncheckGC(pool);
	}
}

/** 
 * Fills the interior of the specified rectangle, using the receiver's
 * background color. 
 *
 * @param rect the rectangle to be filled
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the rectangle is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRectangle(int, int, int, int)
 */
public void fillRectangle(Rectangle rect) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	fillRectangle(rect.x, rect.y, rect.width, rect.height);
}

/** 
 * Fills the interior of the round-cornered rectangle specified by 
 * the arguments, using the receiver's background color. 
 *
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled
 * @param height the height of the rectangle to be filled
 * @param arcWidth the width of the arc
 * @param arcHeight the height of the arc
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRoundRectangle
 */
public void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (arcWidth == 0 || arcHeight == 0) {
		fillRectangle(x, y, width, height);
    	return;
	}
	NSAutoreleasePool pool = checkGC(FILL);
	try {
		NSBezierPath path = data.path;
		NSRect rect = new NSRect();
		rect.x = x;
		rect.y = y;
		rect.width = width;
		rect.height = height;
		path.appendBezierPathWithRoundedRect(rect, arcWidth, arcHeight);
		Pattern pattern = data.backgroundPattern;
		if (pattern != null && pattern.gradient != null) {
			fillPattern(path, pattern);
		} else {
			path.fill();
		}
		path.removeAllPoints();
	} finally {
		uncheckGC(pool);
	}
}

void strokePattern(NSBezierPath path, Pattern pattern) {
	handle.saveGraphicsState();
	int /*long*/ cgPath = createCGPathRef(path);
	int /*long*/ cgContext = handle.graphicsPort();
	OS.CGContextSaveGState(cgContext);
	initCGContext(cgContext);
	OS.CGContextAddPath(cgContext, cgPath);
	OS.CGContextReplacePathWithStrokedPath(cgContext);
	OS.CGPathRelease(cgPath);
	cgPath = 0;
	cgPath = OS.CGContextCopyPath(cgContext);
	if (cgPath == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.CGContextRestoreGState(cgContext);
	NSBezierPath strokePath = createNSBezierPath(cgPath);
	OS.CGPathRelease(cgPath);
	fillPattern(strokePath, pattern);
	handle.restoreGraphicsState();
}

void flush () {
	handle.flushGraphics();
}

/**
 * Returns the <em>advance width</em> of the specified character in
 * the font which is currently selected into the receiver.
 * <p>
 * The advance width is defined as the horizontal distance the cursor
 * should move after printing the character in the selected font.
 * </p>
 *
 * @param ch the character to measure
 * @return the distance in the x direction to move past the character before painting the next
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getAdvanceWidth(char ch) {	
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	//NOT DONE
	return stringExtent(new String(new char[]{ch})).x;
}

/** 
 * Returns the background color.
 *
 * @return the receiver's background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Color getBackground() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return Color.cocoa_new (data.device, data.background);
}

/** 
 * Returns the background pattern. The default value is
 * <code>null</code>.
 *
 * @return the receiver's background pattern
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see Pattern
 * 
 * @since 3.1
 */
public Pattern getBackgroundPattern() {	
	if (handle == null) SWT.error(SWT.ERROR_WIDGET_DISPOSED);
	return data.backgroundPattern;	
}

/**
 * Returns <code>true</code> if receiver is using the operating system's
 * advanced graphics subsystem.  Otherwise, <code>false</code> is returned
 * to indicate that normal graphics are in use.
 * <p>
 * Advanced graphics may not be installed for the operating system.  In this
 * case, <code>false</code> is always returned.  Some operating system have
 * only one graphics subsystem.  If this subsystem supports advanced graphics,
 * then <code>true</code> is always returned.  If any graphics operation such
 * as alpha, antialias, patterns, interpolation, paths, clipping or transformation
 * has caused the receiver to switch from regular to advanced graphics mode,
 * <code>true</code> is returned.  If the receiver has been explicitly switched
 * to advanced mode and this mode is supported, <code>true</code> is returned.
 * </p>
 *
 * @return the advanced value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #setAdvanced
 * 
 * @since 3.1
 */
public boolean getAdvanced() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return true;
}

/**
 * Returns the receiver's alpha value. The alpha value
 * is between 0 (transparent) and 255 (opaque).
 *
 * @return the alpha value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public int getAlpha() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.alpha;
}

/**
 * Returns the receiver's anti-aliasing setting value, which will be
 * one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code> or
 * <code>SWT.ON</code>. Note that this controls anti-aliasing for all
 * <em>non-text drawing</em> operations.
 *
 * @return the anti-aliasing setting
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #getTextAntialias
 * 
 * @since 3.1
 */
public int getAntialias() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.antialias;
}

/**
 * Returns the width of the specified character in the font
 * selected into the receiver. 
 * <p>
 * The width is defined as the space taken up by the actual
 * character, not including the leading and tailing whitespace
 * or overhang.
 * </p>
 *
 * @param ch the character to measure
 * @return the width of the character
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getCharWidth(char ch) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	//NOT DONE
	return stringExtent(new String(new char[]{ch})).x;
}

/** 
 * Returns the bounding rectangle of the receiver's clipping
 * region. If no clipping region is set, the return value
 * will be a rectangle which covers the entire bounds of the
 * object the receiver is drawing on.
 *
 * @return the bounding rectangle of the clipping region
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Rectangle getClipping() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSRect rect = null;
		if (data.view != null) {
			rect = data.view.visibleRect();
		} else {
			rect = new NSRect();
			if (data.image != null) {
				NSSize size = data.image.handle.size();
				rect.width = size.width;
				rect.height = size.height;
			} else if (data.size != null) {
				rect.width = data.size.width;
				rect.height = data.size.height;
			}
		}
		if (data.paintRect != null || data.clipPath != null || data.inverseTransform != null) {
			if (data.paintRect != null) {
				OS.NSIntersectionRect(rect, rect, data.paintRect);
			}
			if (data.clipPath != null) {
				NSRect clip = data.clipPath.bounds();
				OS.NSIntersectionRect(rect, rect, clip);
			}
			if (data.inverseTransform != null && rect.width > 0 && rect.height > 0) {
				NSPoint pt = new NSPoint();
				pt.x = rect.x;
				pt.y = rect.y;
				NSSize size = new NSSize();
				size.width = rect.width;
				size.height = rect.height;
				pt = data.inverseTransform.transformPoint(pt);
				size =  data.inverseTransform.transformSize(size);
				rect.x = pt.x;
				rect.y = pt.y;
				rect.width = size.width;
				rect.height = size.height;
			}
		}
		return new Rectangle((int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
	} finally {
		if (pool != null) pool.release();
	}
}

/** 
 * Sets the region managed by the argument to the current
 * clipping region of the receiver.
 *
 * @param region the region to fill with the clipping region
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the region is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the region is disposed</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void getClipping(Region region) {	
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		region.subtract(region);
		NSRect rect = null;
		if (data.view != null) {
			rect = data.view.visibleRect();
		} else {
			rect = new NSRect();
			if (data.image != null) {
				NSSize size = data.image.handle.size();
				rect.width = size.width;
				rect.height = size.height;
			} else if (data.size != null) {
				rect.width = data.size.width;
				rect.height = data.size.height;
			}
		}
		region.add((int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
		NSRect paintRect = data.paintRect;
		if (paintRect != null) {
			region.intersect((int)paintRect.x, (int)paintRect.y, (int)paintRect.width, (int)paintRect.height);
		}
		if (data.clipPath != null) {
			NSBezierPath clip = data.clipPath.bezierPathByFlatteningPath();
			int count = (int)/*64*/clip.elementCount();
			int pointCount = 0;
			Region clipRgn = new Region(device);
			int[] pointArray = new int[count * 2];
			int /*long*/ points = OS.malloc(NSPoint.sizeof);
			if (points == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			NSPoint pt = new NSPoint();
			for (int i = 0; i < count; i++) {
				int element = (int)/*64*/clip.elementAtIndex(i, points);
				switch (element) {
					case OS.NSMoveToBezierPathElement:
						if (pointCount != 0) clipRgn.add(pointArray, pointCount);
						pointCount = 0;
						OS.memmove(pt, points, NSPoint.sizeof);
						pointArray[pointCount++] = (int)pt.x;
						pointArray[pointCount++] = (int)pt.y;
						break;
					case OS.NSLineToBezierPathElement:
						OS.memmove(pt, points, NSPoint.sizeof);
						pointArray[pointCount++] = (int)pt.x;
						pointArray[pointCount++] = (int)pt.y;
						break;
					case OS.NSClosePathBezierPathElement:
						if (pointCount != 0) clipRgn.add(pointArray, pointCount);
						pointCount = 0;
						break;
				}
			}
			if (pointCount != 0) clipRgn.add(pointArray, pointCount);
			OS.free(points);
			region.intersect(clipRgn);
			clipRgn.dispose();
		}
		if (data.inverseTransform != null) {
			region.convertRgn(data.inverseTransform);
		}
	} finally {
		if (pool != null) pool.release();
	}
}

/** 
 * Returns the receiver's fill rule, which will be one of
 * <code>SWT.FILL_EVEN_ODD</code> or <code>SWT.FILL_WINDING</code>.
 *
 * @return the receiver's fill rule
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public int getFillRule() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.fillRule;
}

/** 
 * Returns the font currently being used by the receiver
 * to draw and measure text.
 *
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Font getFont() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.font;
}

/**
 * Returns a FontMetrics which contains information
 * about the font currently being used by the receiver
 * to draw and measure text.
 *
 * @return font metrics for the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public FontMetrics getFontMetrics() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	NSAutoreleasePool pool = checkGC(FONT);
	try {
		NSFont font = data.font.handle;
		int ascent = (int)(0.5f + font.ascender());
		int descent = (int)(0.5f + (-font.descender() + font.leading()));	
		String s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; 
		int averageCharWidth = stringExtent(s).x / s.length();
		return FontMetrics.cocoa_new(ascent, descent, averageCharWidth, 0, ascent + descent);
	} finally {
		uncheckGC(pool);
	}
}

/** 
 * Returns the receiver's foreground color.
 *
 * @return the color used for drawing foreground things
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Color getForeground() {	
	if (handle == null) SWT.error(SWT.ERROR_WIDGET_DISPOSED);
	return Color.cocoa_new(data.device, data.foreground);	
}

/** 
 * Returns the foreground pattern. The default value is
 * <code>null</code>.
 *
 * @return the receiver's foreground pattern
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see Pattern
 * 
 * @since 3.1
 */
public Pattern getForegroundPattern() {	
	if (handle == null) SWT.error(SWT.ERROR_WIDGET_DISPOSED);
	return data.foregroundPattern;	
}

/** 
 * Returns the GCData.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>GC</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @return the receiver's GCData
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see GCData
 * 
 * @since 3.2
 */
public GCData getGCData() {	
	if (handle == null) SWT.error(SWT.ERROR_WIDGET_DISPOSED);
	return data;	
}

/** 
 * Returns the receiver's interpolation setting, which will be one of
 * <code>SWT.DEFAULT</code>, <code>SWT.NONE</code>, 
 * <code>SWT.LOW</code> or <code>SWT.HIGH</code>.
 *
 * @return the receiver's interpolation setting
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public int getInterpolation() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int interpolation = (int)/*64*/handle.imageInterpolation();
	switch (interpolation) {
		case OS.NSImageInterpolationDefault: return SWT.DEFAULT;
		case OS.NSImageInterpolationNone: return SWT.NONE;
		case OS.NSImageInterpolationLow: return SWT.LOW;
		case OS.NSImageInterpolationHigh: return SWT.HIGH;
	}
	return SWT.DEFAULT;
}

/** 
 * Returns the receiver's line attributes.
 *
 * @return the line attributes used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.3 
 */
public LineAttributes getLineAttributes() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	float[] dashes = null;
	if (data.lineDashes != null) {
		dashes = new float[data.lineDashes.length];
		System.arraycopy(data.lineDashes, 0, dashes, 0, dashes.length);
	}
	return new LineAttributes(data.lineWidth, data.lineCap, data.lineJoin, data.lineStyle, dashes, data.lineDashesOffset, data.lineMiterLimit);
}

/** 
 * Returns the receiver's line cap style, which will be one
 * of the constants <code>SWT.CAP_FLAT</code>, <code>SWT.CAP_ROUND</code>,
 * or <code>SWT.CAP_SQUARE</code>.
 *
 * @return the cap style used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1 
 */
public int getLineCap() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.lineCap;
}

/** 
 * Returns the receiver's line dash style. The default value is
 * <code>null</code>.
 *
 * @return the line dash style used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1 
 */
public int[] getLineDash() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.lineDashes == null) return null;
	int[] lineDashes = new int[data.lineDashes.length];
	for (int i = 0; i < lineDashes.length; i++) {
		lineDashes[i] = (int)data.lineDashes[i];
	}
	return lineDashes;	
}

/** 
 * Returns the receiver's line join style, which will be one
 * of the constants <code>SWT.JOIN_MITER</code>, <code>SWT.JOIN_ROUND</code>,
 * or <code>SWT.JOIN_BEVEL</code>.
 *
 * @return the join style used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1 
 */
public int getLineJoin() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.lineJoin;
}

/** 
 * Returns the receiver's line style, which will be one
 * of the constants <code>SWT.LINE_SOLID</code>, <code>SWT.LINE_DASH</code>,
 * <code>SWT.LINE_DOT</code>, <code>SWT.LINE_DASHDOT</code> or
 * <code>SWT.LINE_DASHDOTDOT</code>.
 *
 * @return the style used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getLineStyle() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.lineStyle;
}

/** 
 * Returns the width that will be used when drawing lines
 * for all of the figure drawing operations (that is,
 * <code>drawLine</code>, <code>drawRectangle</code>, 
 * <code>drawPolyline</code>, and so forth.
 *
 * @return the receiver's line width 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getLineWidth() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return (int)data.lineWidth;
}

/**
 * Returns the receiver's style information.
 * <p>
 * Note that the value which is returned by this method <em>may
 * not match</em> the value which was provided to the constructor
 * when the receiver was created. This can occur when the underlying
 * operating system does not support a particular combination of
 * requested styles. 
 * </p>
 *
 * @return the style bits
 *  
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *   
 * @since 2.1.2
 */
public int getStyle () {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.style;
}

/**
 * Returns the receiver's text drawing anti-aliasing setting value,
 * which will be one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code> or
 * <code>SWT.ON</code>. Note that this controls anti-aliasing
 * <em>only</em> for text drawing operations.
 *
 * @return the anti-aliasing setting
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #getAntialias
 * 
 * @since 3.1
 */
public int getTextAntialias() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.textAntialias;
}

/** 
 * Sets the parameter to the transform that is currently being
 * used by the receiver.
 *
 * @param transform the destination to copy the transform into
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see Transform
 * 
 * @since 3.1
 */
public void getTransform (Transform transform) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (transform == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (transform.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	NSAffineTransform cmt = data.transform;
	if (cmt != null) {
		NSAffineTransformStruct struct = cmt.transformStruct();
		transform.handle.setTransformStruct(struct);
	} else {
		transform.setElements(1, 0, 0, 1, 0, 0);
	}
}

/** 
 * Returns <code>true</code> if this GC is drawing in the mode
 * where the resulting color in the destination is the
 * <em>exclusive or</em> of the color values in the source
 * and the destination, and <code>false</code> if it is
 * drawing in the mode where the destination color is being
 * replaced with the source color value.
 *
 * @return <code>true</code> true if the receiver is in XOR mode, and false otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public boolean getXORMode() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.xorMode;
}

/**
 * Returns an integer hash code for the receiver. Any two 
 * objects that return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #equals
 */
public int hashCode() {
	return handle != null ? (int)/*64*/handle.id : 0;
}

void init(Drawable drawable, GCData data, int /*long*/ context) {
	if (data.foreground != null) data.state &= ~(FOREGROUND | FOREGROUND_FILL);
	if (data.background != null)  data.state &= ~BACKGROUND;
	if (data.font != null) data.state &= ~FONT;
	data.state &= ~DRAW_OFFSET;

	Image image = data.image;
	if (image != null) image.memGC = this;
	this.drawable = drawable;
	this.data = data;
	handle = new NSGraphicsContext(context);
	handle.retain();
	handle.saveGraphicsState();
	data.path = NSBezierPath.bezierPath();
	data.path.setWindingRule(data.fillRule == SWT.FILL_WINDING ? OS.NSNonZeroWindingRule : OS.NSEvenOddWindingRule);
	data.path.retain();
}

void initCGContext(int /*long*/ cgContext) {
	int state = data.state;
	if ((state & LINE_WIDTH) != 0) {
		OS.CGContextSetLineWidth(cgContext, data.lineWidth == 0 ?  1 : data.lineWidth);
		switch (data.lineStyle) {
			case SWT.LINE_DOT:
			case SWT.LINE_DASH:
			case SWT.LINE_DASHDOT:
			case SWT.LINE_DASHDOTDOT:
				state |= LINE_STYLE;
		}
	}
	if ((state & LINE_STYLE) != 0) {
		float[] dashes = null;
		float width = data.lineWidth;
		switch (data.lineStyle) {
			case SWT.LINE_SOLID: break;
			case SWT.LINE_DASH: dashes = width != 0 ? LINE_DASH : LINE_DASH_ZERO; break;
			case SWT.LINE_DOT: dashes = width != 0 ? LINE_DOT : LINE_DOT_ZERO; break;
			case SWT.LINE_DASHDOT: dashes = width != 0 ? LINE_DASHDOT : LINE_DASHDOT_ZERO; break;
			case SWT.LINE_DASHDOTDOT: dashes = width != 0 ? LINE_DASHDOTDOT : LINE_DASHDOTDOT_ZERO; break;
			case SWT.LINE_CUSTOM: dashes = data.lineDashes; break;
		}
		if (dashes != null) {
			float[] lengths = new float[dashes.length];
			for (int i = 0; i < lengths.length; i++) {
				lengths[i] = width == 0 || data.lineStyle == SWT.LINE_CUSTOM ? dashes[i] : dashes[i] * width;
			}
			OS.CGContextSetLineDash(cgContext, data.lineDashesOffset, lengths, lengths.length);
		} else {
			OS.CGContextSetLineDash(cgContext, 0, null, 0);
		}
	}
	if ((state & LINE_MITERLIMIT) != 0) {
		OS.CGContextSetMiterLimit(cgContext, data.lineMiterLimit);
	}
	if ((state & LINE_JOIN) != 0) {
		int joinStyle = 0;
		switch (data.lineJoin) {
			case SWT.JOIN_MITER: joinStyle = OS.kCGLineJoinMiter; break;
			case SWT.JOIN_ROUND: joinStyle = OS.kCGLineJoinRound; break;
			case SWT.JOIN_BEVEL: joinStyle = OS.kCGLineJoinBevel; break;
		}
		OS.CGContextSetLineJoin(cgContext, joinStyle);
	}
	if ((state & LINE_CAP) != 0) {
		int capStyle = 0;
		switch (data.lineCap) {
			case SWT.CAP_ROUND: capStyle = OS.kCGLineCapRound; break;
			case SWT.CAP_FLAT: capStyle = OS.kCGLineCapButt; break;
			case SWT.CAP_SQUARE: capStyle = OS.kCGLineCapSquare; break;
		}
		OS.CGContextSetLineCap(cgContext, capStyle);
	}
}

/**
 * Returns <code>true</code> if the receiver has a clipping
 * region set into it, and <code>false</code> otherwise.
 * If this method returns false, the receiver will draw on all
 * available space in the destination. If it returns true, 
 * it will draw only in the area that is covered by the region
 * that can be accessed with <code>getClipping(region)</code>.
 *
 * @return <code>true</code> if the GC has a clipping region, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public boolean isClipped() {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return data.clipPath != null;
}

/**
 * Returns <code>true</code> if the GC has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the GC.
 * When a GC has been disposed, it is an error to
 * invoke any other method using the GC.
 *
 * @return <code>true</code> when the GC is disposed and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return handle == null;
}

boolean isIdentity(float[] transform) {
	return transform[0] == 1 && transform[1] == 0 && transform[2] == 0
	 	&& transform[3] == 1 && transform[4] == 0 && transform[5] == 0;
}

/**
 * Sets the receiver to always use the operating system's advanced graphics
 * subsystem for all graphics operations if the argument is <code>true</code>.
 * If the argument is <code>false</code>, the advanced graphics subsystem is 
 * no longer used, advanced graphics state is cleared and the normal graphics
 * subsystem is used from now on.
 * <p>
 * Normally, the advanced graphics subsystem is invoked automatically when
 * any one of the alpha, antialias, patterns, interpolation, paths, clipping
 * or transformation operations in the receiver is requested.  When the receiver
 * is switched into advanced mode, the advanced graphics subsystem performs both
 * advanced and normal graphics operations.  Because the two subsystems are
 * different, their output may differ.  Switching to advanced graphics before
 * any graphics operations are performed ensures that the output is consistent.
 * </p><p>
 * Advanced graphics may not be installed for the operating system.  In this
 * case, this operation does nothing.  Some operating system have only one
 * graphics subsystem, so switching from normal to advanced graphics does
 * nothing.  However, switching from advanced to normal graphics will always
 * clear the advanced graphics state, even for operating systems that have
 * only one graphics subsystem.
 * </p>
 *
 * @param advanced the new advanced graphics state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see #setAlpha
 * @see #setAntialias
 * @see #setBackgroundPattern
 * @see #setClipping(Path)
 * @see #setForegroundPattern
 * @see #setLineAttributes
 * @see #setInterpolation
 * @see #setTextAntialias
 * @see #setTransform
 * @see #getAdvanced
 * 
 * @since 3.1
 */
public void setAdvanced(boolean advanced) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (!advanced) {
		setAlpha(0xFF);
		setAntialias(SWT.DEFAULT);
		setBackgroundPattern(null);
		setClipping((Rectangle)null);
		setForegroundPattern(null);
		setInterpolation(SWT.DEFAULT);
		setTextAntialias(SWT.DEFAULT);
		setTransform(null);
	}
}

/**
 * Sets the receiver's alpha value which must be
 * between 0 (transparent) and 255 (opaque).
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * @param alpha the alpha value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see #getAdvanced
 * @see #setAdvanced
 * 
 * @since 3.1
 */
public void setAlpha(int alpha) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	data.alpha = alpha & 0xFF;
	data.state &= ~(BACKGROUND | FOREGROUND | FOREGROUND_FILL);
	
}

/**
 * Sets the receiver's anti-aliasing value to the parameter, 
 * which must be one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code>
 * or <code>SWT.ON</code>. Note that this controls anti-aliasing for all
 * <em>non-text drawing</em> operations.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 *
 * @param antialias the anti-aliasing setting
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter is not one of <code>SWT.DEFAULT</code>,
 *                                 <code>SWT.OFF</code> or <code>SWT.ON</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see #getAdvanced
 * @see #setAdvanced
 * @see #setTextAntialias
 * 
 * @since 3.1
 */
public void setAntialias(int antialias) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	boolean mode = true;
	switch (antialias) {
		case SWT.DEFAULT:
			/* Printer is off by default */
			if (!handle.isDrawingToScreen()) mode = false;
			break;
		case SWT.OFF: mode = false; break;
		case SWT.ON: mode = true; break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.antialias = antialias;
	handle.setShouldAntialias(mode);
}

/**
 * Sets the background color. The background color is used
 * for fill operations and as the background color when text
 * is drawn.
 *
 * @param color the new background color for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the color is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the color has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setBackground(Color color) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.background = color.handle;
	data.backgroundPattern = null;
	if (data.bg != null) data.bg.release();
	data.bg = null;
	data.state &= ~BACKGROUND;
}

/** 
 * Sets the background pattern. The default value is <code>null</code>.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * 
 * @param pattern the new background pattern
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see Pattern
 * @see #getAdvanced
 * @see #setAdvanced
 * 
 * @since 3.1
 */
public void setBackgroundPattern(Pattern pattern) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pattern != null && pattern.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (data.backgroundPattern == pattern) return;
	data.backgroundPattern = pattern;
	data.state &= ~BACKGROUND;
}

/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the rectangular area specified
 * by the arguments.
 *
 * @param x the x coordinate of the clipping rectangle
 * @param y the y coordinate of the clipping rectangle
 * @param width the width of the clipping rectangle
 * @param height the height of the clipping rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setClipping(int x, int y, int width, int height) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		if (width < 0) {
			x = x + width;
			width = -width;
		}
		if (height < 0) {
			y = y + height;
			height = -height;
		}
		NSRect rect = new NSRect();
		rect.x = x;
		rect.y = y;
		rect.width = width;
		rect.height = height;
		NSBezierPath path = NSBezierPath.bezierPathWithRect(rect);
		path.retain();
		setClipping(path);
	} finally {
		if (pool != null) pool.release();
	}
}

/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the path specified
 * by the argument.  
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * 
 * @param path the clipping path.
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the path has been disposed</li>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see Path
 * @see #getAdvanced
 * @see #setAdvanced
 * 
 * @since 3.1
 */
public void setClipping(Path path) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path != null && path.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		setClipping(new NSBezierPath(path.handle.copy().id));
	} finally {
		if (pool != null) pool.release();
	}
}

/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the rectangular area specified
 * by the argument.  Specifying <code>null</code> for the
 * rectangle reverts the receiver's clipping area to its
 * original value.
 *
 * @param rect the clipping rectangle or <code>null</code>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setClipping(Rectangle rect) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) {
		setClipping((NSBezierPath)null);
	} else {
		setClipping(rect.x, rect.y, rect.width, rect.height);
	}
}

/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the region specified
 * by the argument.  Specifying <code>null</code> for the
 * region reverts the receiver's clipping area to its
 * original value.
 *
 * @param region the clipping region or <code>null</code>
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the region has been disposed</li>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setClipping(Region region) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region != null && region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		setClipping(region != null ? region.getPath() : null);
	} finally {
		if (pool != null) pool.release();
	}
}

void setClipping(NSBezierPath path) {
	if (data.clipPath != null) {
		data.clipPath.release();
		data.clipPath = null;
	}
	if (path != null) {
		data.clipPath = path;
		if (data.transform != null) {
			data.clipPath.transformUsingAffineTransform(data.transform);
		}
	}
	data.state &= ~CLIPPING;
}

/** 
 * Sets the receiver's fill rule to the parameter, which must be one of
 * <code>SWT.FILL_EVEN_ODD</code> or <code>SWT.FILL_WINDING</code>.
 *
 * @param rule the new fill rule
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the rule is not one of <code>SWT.FILL_EVEN_ODD</code>
 *                                 or <code>SWT.FILL_WINDING</code></li>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setFillRule(int rule) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	switch (rule) {
		case SWT.FILL_WINDING:
		case SWT.FILL_EVEN_ODD: break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.fillRule = rule;
	data.path.setWindingRule(rule == SWT.FILL_WINDING ? OS.NSNonZeroWindingRule : OS.NSEvenOddWindingRule);
}

/** 
 * Sets the font which will be used by the receiver
 * to draw and measure text to the argument. If the
 * argument is null, then a default font appropriate
 * for the platform will be used instead.
 *
 * @param font the new font for the receiver, or null to indicate a default font
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the font has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setFont(Font font) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (font != null && font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.font = font != null ? font : data.device.systemFont;
	data.state &= ~FONT;
}

/**
 * Sets the foreground color. The foreground color is used
 * for drawing operations including when text is drawn.
 *
 * @param color the new foreground color for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the color is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the color has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setForeground(Color color) {	
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	data.foreground = color.handle;
	data.foregroundPattern = null;
	if (data.fg != null) data.fg.release();
	data.fg = null;
	data.state &= ~(FOREGROUND | FOREGROUND_FILL);
}

/** 
 * Sets the foreground pattern. The default value is <code>null</code>.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * @param pattern the new foreground pattern
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see Pattern
 * @see #getAdvanced
 * @see #setAdvanced
 * 
 * @since 3.1
 */
public void setForegroundPattern(Pattern pattern) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (pattern != null && pattern.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (data.foregroundPattern == pattern) return;
	data.foregroundPattern = pattern;
	data.state &= ~(FOREGROUND | FOREGROUND_FILL);
}

/** 
 * Sets the receiver's interpolation setting to the parameter, which
 * must be one of <code>SWT.DEFAULT</code>, <code>SWT.NONE</code>, 
 * <code>SWT.LOW</code> or <code>SWT.HIGH</code>.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * 
 * @param interpolation the new interpolation setting
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the rule is not one of <code>SWT.DEFAULT</code>, 
 *                                 <code>SWT.NONE</code>, <code>SWT.LOW</code> or <code>SWT.HIGH</code>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see #getAdvanced
 * @see #setAdvanced
 * 
 * @since 3.1
 */
public void setInterpolation(int interpolation) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int quality = 0;
	switch (interpolation) {
		case SWT.DEFAULT: quality = OS.NSImageInterpolationDefault; break;
		case SWT.NONE: quality = OS.NSImageInterpolationNone; break;
		case SWT.LOW: quality = OS.NSImageInterpolationLow; break;
		case SWT.HIGH: quality = OS.NSImageInterpolationHigh; break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	handle.setImageInterpolation(quality);
}

/**
 * Sets the receiver's line attributes.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * @param attributes the line attributes
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the attributes is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if any of the line attributes is not valid</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see LineAttributes
 * @see #getAdvanced
 * @see #setAdvanced
 * 
 * @since 3.3
 */
public void setLineAttributes(LineAttributes attributes) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (attributes == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int mask = 0;
	float lineWidth = attributes.width;
	if (lineWidth != data.lineWidth) {
		mask |= LINE_WIDTH | DRAW_OFFSET;
	}
	int lineStyle = attributes.style;
	if (lineStyle != data.lineStyle) {
		mask |= LINE_STYLE;
		switch (lineStyle) {
			case SWT.LINE_SOLID:
			case SWT.LINE_DASH:
			case SWT.LINE_DOT:
			case SWT.LINE_DASHDOT:
			case SWT.LINE_DASHDOTDOT:
				break;
			case SWT.LINE_CUSTOM:
				if (attributes.dash == null) lineStyle = SWT.LINE_SOLID;
				break;
			default:
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	int join = attributes.join;
	if (join != data.lineJoin) {
		mask |= LINE_JOIN;
		switch (join) {
			case SWT.CAP_ROUND:
			case SWT.CAP_FLAT:
			case SWT.CAP_SQUARE:
				break;
			default:
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	int cap = attributes.cap;
	if (cap != data.lineCap) {
		mask |= LINE_CAP;
		switch (cap) {
			case SWT.JOIN_MITER:
			case SWT.JOIN_ROUND:
			case SWT.JOIN_BEVEL:
				break;
			default:
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	float[] dashes = attributes.dash;
	float[] lineDashes = data.lineDashes;
	if (dashes != null && dashes.length > 0) {
		boolean changed = lineDashes == null || lineDashes.length != dashes.length;
		for (int i = 0; i < dashes.length; i++) {
			float dash = dashes[i];
			if (dash <= 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			if (!changed && lineDashes[i] != dash) changed = true;
		}
		if (changed) {
			float[] newDashes = new float[dashes.length];
			System.arraycopy(dashes, 0, newDashes, 0, dashes.length);
			dashes = newDashes;
			mask |= LINE_STYLE;
		} else {
			dashes = lineDashes;
		}
	} else {
		if (lineDashes != null && lineDashes.length > 0) {
			mask |= LINE_STYLE;
		} else {
			dashes = lineDashes;
		}
	}
	float dashOffset = attributes.dashOffset;
	if (dashOffset != data.lineDashesOffset) {
		mask |= LINE_STYLE;		
	}
	float miterLimit = attributes.miterLimit;
	if (miterLimit != data.lineMiterLimit) {
		mask |= LINE_MITERLIMIT;		
	}
	if (mask == 0) return;
	data.lineWidth = lineWidth;
	data.lineStyle = lineStyle;
	data.lineCap = cap;
	data.lineJoin = join;
	data.lineDashes = dashes;
	data.lineDashesOffset = dashOffset;
	data.lineMiterLimit = miterLimit;
	data.state &= ~mask;
}

/** 
 * Sets the receiver's line cap style to the argument, which must be one
 * of the constants <code>SWT.CAP_FLAT</code>, <code>SWT.CAP_ROUND</code>,
 * or <code>SWT.CAP_SQUARE</code>.
 *
 * @param cap the cap style to be used for drawing lines
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the style is not valid</li>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1 
 */
public void setLineCap(int cap) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.lineCap == cap) return;
	switch (cap) {
		case SWT.CAP_ROUND:
		case SWT.CAP_FLAT:
		case SWT.CAP_SQUARE:
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.lineCap = cap;
	data.state &= ~LINE_CAP;
}

/** 
 * Sets the receiver's line dash style to the argument. The default
 * value is <code>null</code>. If the argument is not <code>null</code>,
 * the receiver's line style is set to <code>SWT.LINE_CUSTOM</code>, otherwise
 * it is set to <code>SWT.LINE_SOLID</code>.
 *
 * @param dashes the dash style to be used for drawing lines
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if any of the values in the array is less than or equal 0</li>
 * </ul> 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1 
 */
public void setLineDash(int[] dashes) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	float[] lineDashes = data.lineDashes;
	if (dashes != null && dashes.length > 0) {
		boolean changed = data.lineStyle != SWT.LINE_CUSTOM || lineDashes == null || lineDashes.length != dashes.length;
		for (int i = 0; i < dashes.length; i++) {
			int dash = dashes[i];
			if (dash <= 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			if (!changed && lineDashes[i] != dash) changed = true;
		}
		if (!changed) return;
		data.lineDashes = new float[dashes.length];
		for (int i = 0; i < dashes.length; i++) {
			data.lineDashes[i] = dashes[i];
		}
		data.lineStyle = SWT.LINE_CUSTOM;
	} else {
		if (data.lineStyle == SWT.LINE_SOLID && (lineDashes == null || lineDashes.length == 0)) return;
		data.lineDashes = null;
		data.lineStyle = SWT.LINE_SOLID;
	}
	data.state &= ~LINE_STYLE;
}

/** 
 * Sets the receiver's line join style to the argument, which must be one
 * of the constants <code>SWT.JOIN_MITER</code>, <code>SWT.JOIN_ROUND</code>,
 * or <code>SWT.JOIN_BEVEL</code>.
 *
 * @param join the join style to be used for drawing lines
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the style is not valid</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.1 
 */
public void setLineJoin(int join) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.lineJoin == join) return;
	switch (join) {
		case SWT.JOIN_MITER:
		case SWT.JOIN_ROUND:
		case SWT.JOIN_BEVEL:
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.lineJoin = join;
	data.state &= ~LINE_JOIN;
}

/** 
 * Sets the receiver's line style to the argument, which must be one
 * of the constants <code>SWT.LINE_SOLID</code>, <code>SWT.LINE_DASH</code>,
 * <code>SWT.LINE_DOT</code>, <code>SWT.LINE_DASHDOT</code> or
 * <code>SWT.LINE_DASHDOTDOT</code>.
 *
 * @param lineStyle the style to be used for drawing lines
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the style is not valid</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setLineStyle(int lineStyle) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.lineStyle == lineStyle) return;
	switch (lineStyle) {
		case SWT.LINE_SOLID:
		case SWT.LINE_DASH:
		case SWT.LINE_DOT:
		case SWT.LINE_DASHDOT:
		case SWT.LINE_DASHDOTDOT:
			break;
		case SWT.LINE_CUSTOM:
			if (data.lineDashes == null) lineStyle = SWT.LINE_SOLID;
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.lineStyle = lineStyle;
	data.state &= ~LINE_STYLE;
}

/** 
 * Sets the width that will be used when drawing lines
 * for all of the figure drawing operations (that is,
 * <code>drawLine</code>, <code>drawRectangle</code>, 
 * <code>drawPolyline</code>, and so forth.
 * <p>
 * Note that line width of zero is used as a hint to
 * indicate that the fastest possible line drawing
 * algorithms should be used. This means that the
 * output may be different from line width one.
 * </p>
 *
 * @param lineWidth the width of a line
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setLineWidth(int lineWidth) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (data.lineWidth == lineWidth) return;
	data.lineWidth = lineWidth;
	data.state &= ~(LINE_WIDTH | DRAW_OFFSET);	
}

/** 
 * If the argument is <code>true</code>, puts the receiver
 * in a drawing mode where the resulting color in the destination
 * is the <em>exclusive or</em> of the color values in the source
 * and the destination, and if the argument is <code>false</code>,
 * puts the receiver in a drawing mode where the destination color
 * is replaced with the source color value.
 * <p>
 * Note that this mode in fundamentally unsupportable on certain
 * platforms, notably Carbon (Mac OS X). Clients that want their
 * code to run on all platforms need to avoid this method.
 * </p>
 *
 * @param xor if <code>true</code>, then <em>xor</em> mode is used, otherwise <em>source copy</em> mode is used
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @deprecated this functionality is not supported on some platforms
 */
public void setXORMode(boolean xor) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	data.xorMode = xor;
}

/**
 * Sets the receiver's text anti-aliasing value to the parameter, 
 * which must be one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code>
 * or <code>SWT.ON</code>. Note that this controls anti-aliasing only
 * for all <em>text drawing</em> operations.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * 
 * @param antialias the anti-aliasing setting
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter is not one of <code>SWT.DEFAULT</code>,
 *                                 <code>SWT.OFF</code> or <code>SWT.ON</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see #getAdvanced
 * @see #setAdvanced
 * @see #setAntialias
 * 
 * @since 3.1
 */
public void setTextAntialias(int antialias) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	switch (antialias) {
		case SWT.DEFAULT:
		case SWT.OFF:
		case SWT.ON:
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	data.textAntialias = antialias;
}

/**
 * Sets the transform that is currently being used by the receiver. If
 * the argument is <code>null</code>, the current transform is set to
 * the identity transform.
 * <p>
 * This operation requires the operating system's advanced
 * graphics subsystem which may not be available on some
 * platforms.
 * </p>
 * 
 * @param transform the transform to set
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are not available</li>
 * </ul>
 * 
 * @see Transform
 * @see #getAdvanced
 * @see #setAdvanced
 * 
 * @since 3.1
 */
public void setTransform(Transform transform) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (transform != null && transform.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (transform != null) {
		if (data.transform != null) data.transform.release();
		if (data.inverseTransform != null) data.inverseTransform.release();
		data.transform = ((NSAffineTransform)new NSAffineTransform().alloc()).initWithTransform(transform.handle);
		data.inverseTransform = ((NSAffineTransform)new NSAffineTransform().alloc()).initWithTransform(transform.handle);
		NSAffineTransformStruct struct = data.inverseTransform.transformStruct();
		if ((struct.m11 * struct.m22 - struct.m12 * struct.m21) != 0) {
			data.inverseTransform.invert();
		}
	} else {
		data.transform = data.inverseTransform = null;
	}
	data.state &= ~(TRANSFORM | DRAW_OFFSET);
}

/**
 * Returns the extent of the given string. No tab
 * expansion or carriage return processing will be performed.
 * <p>
 * The <em>extent</em> of a string is the width and height of
 * the rectangular area it would cover if drawn in a particular
 * font (in this case, the current font in the receiver).
 * </p>
 *
 * @param string the string to measure
 * @return a point containing the extent of the string
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point stringExtent(String string) {
	return textExtent(string, 0);
}

/**
 * Returns the extent of the given string. Tab expansion and
 * carriage return processing are performed.
 * <p>
 * The <em>extent</em> of a string is the width and height of
 * the rectangular area it would cover if drawn in a particular
 * font (in this case, the current font in the receiver).
 * </p>
 *
 * @param string the string to measure
 * @return a point containing the extent of the string
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point textExtent(String string) {
	return textExtent(string, SWT.DRAW_DELIMITER | SWT.DRAW_TAB);
}

/**
 * Returns the extent of the given string. Tab expansion, line
 * delimiter and mnemonic processing are performed according to
 * the specified flags, which can be a combination of:
 * <dl>
 * <dt><b>DRAW_DELIMITER</b></dt>
 * <dd>draw multiple lines</dd>
 * <dt><b>DRAW_TAB</b></dt>
 * <dd>expand tabs</dd>
 * <dt><b>DRAW_MNEMONIC</b></dt>
 * <dd>underline the mnemonic character</dd>
 * <dt><b>DRAW_TRANSPARENT</b></dt>
 * <dd>transparent background</dd>
 * </dl>
 * <p>
 * The <em>extent</em> of a string is the width and height of
 * the rectangular area it would cover if drawn in a particular
 * font (in this case, the current font in the receiver).
 * </p>
 *
 * @param string the string to measure
 * @param flags the flags specifying how to process the text
 * @return a point containing the extent of the string
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point textExtent(String string, int flags) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	NSAutoreleasePool pool = checkGC(FONT);
	try {
		NSAttributedString str = createString(string, flags, false);
		NSSize size = str.size();
		str.release();
		return new Point((int)size.width, (int)size.height);
	} finally {
		uncheckGC(pool);
	}
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "GC {*DISPOSED*}";
	return "GC {" + handle + "}";
}

void uncheckGC(NSAutoreleasePool pool) {
	if (data.flippedContext != null) {
		NSGraphicsContext.static_restoreGraphicsState();
	}
	NSView view = data.view;
	if (view != null && data.paintRect == null) {
		if (data.thread != Thread.currentThread()) flush();
	}
	if (pool != null) pool.release();
}

}
