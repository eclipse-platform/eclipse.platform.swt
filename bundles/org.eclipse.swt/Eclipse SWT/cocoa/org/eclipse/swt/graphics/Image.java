/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
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
package org.eclipse.swt.graphics;


import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.internal.graphics.*;

/**
 * Instances of this class are graphics which have been prepared
 * for display on a specific device. That is, they are ready
 * to paint using methods such as <code>GC.drawImage()</code>
 * and display on widgets with, for example, <code>Button.setImage()</code>.
 * <p>
 * If loaded from a file format that supports it, an
 * <code>Image</code> may have transparency, meaning that certain
 * pixels are specified as being transparent when drawn. Examples
 * of file formats that support transparency are GIF and PNG.
 * </p><p>
 * There are two primary ways to use <code>Images</code>.
 * The first is to load a graphic file from disk and create an
 * <code>Image</code> from it. This is done using an <code>Image</code>
 * constructor, for example:
 * <pre>
 *    Image i = new Image(device, "C:\\graphic.bmp");
 * </pre>
 * A graphic file may contain a color table specifying which
 * colors the image was intended to possess. In the above example,
 * these colors will be mapped to the closest available color in
 * SWT. It is possible to get more control over the mapping of
 * colors as the image is being created, using code of the form:
 * <pre>
 *    ImageData data = new ImageData("C:\\graphic.bmp");
 *    RGB[] rgbs = data.getRGBs();
 *    // At this point, rgbs contains specifications of all
 *    // the colors contained within this image. You may
 *    // allocate as many of these colors as you wish by
 *    // using the Color constructor Color(RGB), then
 *    // create the image:
 *    Image i = new Image(device, data);
 * </pre>
 * <p>
 * Applications which require even greater control over the image
 * loading process should use the support provided in class
 * <code>ImageLoader</code>.
 * </p><p>
 * Application code must explicitly invoke the <code>Image.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 *
 * @see Color
 * @see ImageData
 * @see ImageLoader
 * @see <a href="http://www.eclipse.org/swt/snippets/#image">Image snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Examples: GraphicsExample, ImageAnalyzer</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class Image extends Resource implements Drawable {

	/**
	 * specifies whether the receiver is a bitmap or an icon
	 * (one of <code>SWT.BITMAP</code>, <code>SWT.ICON</code>)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 *
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public int type;

	/**
	 * the handle to the OS image resource
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 *
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public NSImage handle;

	/**
	 * The GC the image is currently selected in.
	 */
	GC memGC;

	/**
	 * The global alpha value to be used for every pixel.
	 */

	/**
	 * The width of the image.
	 */
	int width = -1;

	/**
	 * The height of the image.
	 */
	int height = -1;

	/**
	 * Specifies the default scanline padding.
	 */
	static final int DEFAULT_SCANLINE_PAD = 4;

	/**
	 * ImageFileNameProvider to provide file names at various Zoom levels
	 */
	private ImageFileNameProvider imageFileNameProvider;

	/**
	 * ImageDataProvider to provide ImageData at various Zoom levels
	 */
	private ImageDataProvider imageDataProvider;

	/**
	 * Style flag used to differentiate normal, gray-scale and disabled images based
	 * on image data providers. Without this, a normal and a disabled image of the
	 * same image data provider would be considered equal.
	 */
	private int styleFlag = SWT.IMAGE_COPY;

	/**
	 * Alpha information objects for 100%, 200%
	 */
	private AlphaInfo alphaInfo_100, alphaInfo_200;

	static class AlphaInfo {
		/**
		 * The alpha data of the image.
		 */
		byte [] alphaData;
		/**
		 * The global alpha value to be used for every pixel.
		 */
		int alpha;
		/**
		 * specifies the transparent pixel
		*/
		int transparentPixel;

		AlphaInfo () {
			transparentPixel = -1;
			alpha = -1;
		}

		void init(NSImageRep nativeRep, NSBitmapImageRep rep) {
			int width = (int)nativeRep.pixelsWide();
			int height = (int)nativeRep.pixelsHigh();
			/*
			 * Intialize alphaInfo object. We may need to re intialize alpha info
			 * when a new representation is created
			 */
			transparentPixel = -1;
			alpha = -1;


			boolean hasAlpha = rep.hasAlpha();
			int bpr = width * 4;
			NSRect rect = new NSRect();
			rect.width = width;
			rect.height = height;

			/* Compute the pixels */
			long colorspace = OS.CGColorSpaceCreateDeviceRGB();
			long ctx = OS.CGBitmapContextCreate(rep.bitmapData(), width, height, 8, bpr, colorspace, OS.kCGImageAlphaNoneSkipFirst);
			OS.CGColorSpaceRelease(colorspace);
			NSGraphicsContext.static_saveGraphicsState();
			NSGraphicsContext.setCurrentContext(NSGraphicsContext.graphicsContextWithGraphicsPort(ctx, false));
			nativeRep.drawInRect(rect);
			NSGraphicsContext.static_restoreGraphicsState();
			OS.CGContextRelease(ctx);

			if (hasAlpha) {
				/* Compute the alpha values */
				long bitmapBytesPerRow = width;
				long bitmapByteCount = bitmapBytesPerRow * height;
				long alphaBitmapData = C.malloc(bitmapByteCount);
				long alphaBitmapCtx = OS.CGBitmapContextCreate(alphaBitmapData, width, height, 8, bitmapBytesPerRow, 0, OS.kCGImageAlphaOnly);
				NSGraphicsContext.static_saveGraphicsState();
				NSGraphicsContext.setCurrentContext(NSGraphicsContext.graphicsContextWithGraphicsPort(alphaBitmapCtx, false));
				nativeRep.drawInRect(rect);
				NSGraphicsContext.static_restoreGraphicsState();
				byte[] alphaData = new byte[(int)bitmapByteCount];
				C.memmove(alphaData, alphaBitmapData, bitmapByteCount);
				C.free(alphaBitmapData);
				OS.CGContextRelease(alphaBitmapCtx);

				/* Merge the alpha values with the pixels */
				byte[] srcData = new byte[height * bpr];
				C.memmove(srcData, rep.bitmapData(), srcData.length);
				for (int a = 0, p = 0; a < alphaData.length; a++, p += 4) {
					srcData[p] = alphaData[a];
					float alpha = alphaData[a] & 0xFF;
					if (alpha != 0) {
						srcData[p+1] = (byte)(((srcData[p+1] & 0xFF) / alpha) * 0xFF);
						srcData[p+2] = (byte)(((srcData[p+2] & 0xFF) / alpha) * 0xFF);
						srcData[p+3] = (byte)(((srcData[p+3] & 0xFF) / alpha) * 0xFF);
					}
				}
				C.memmove(rep.bitmapData(), srcData, srcData.length);

				// If the alpha has only 0 or 255 (-1) for alpha values, compute the transparent pixel color instead
				// of a continuous alpha range.
				int transparentOffset = -1, i = 0;
				for (i = 0; i < alphaData.length; i++) {
					int alpha = alphaData[i];
					if (transparentOffset == -1 && alpha == 0) transparentOffset = i;
					if (!(alpha == 0 || alpha == -1)) break;
				}
				this.alpha = -1;
				if (i == alphaData.length && transparentOffset != -1) {
					NSColor color = rep.colorAtX(transparentOffset % width, transparentOffset / width);
					int red = (int) (color.redComponent() * 255);
					int green = (int) (color.greenComponent() * 255);
					int blue = (int) (color.blueComponent() * 255);
					this.transparentPixel = (red << 16) | (green << 8) | blue;

					/*
					* If the image has opaque pixels that have the same color as the transparent
					* pixel, create an alpha image instead of using transparent pixel.
					*/
					for (int j = 0; j < srcData.length; j+=4) {
						if (srcData [j] != 0) {
							int pixel = ((srcData[j+1] & 0xFF) << 16) | ((srcData[j+2] & 0xFF) << 8) | (srcData[j+3] & 0xFF);
							if (pixel == this.transparentPixel){
								this.transparentPixel = -1;
								break;
							}
						}
					}
				}
				if (this.transparentPixel == -1) this.alphaData = alphaData;
			}
		}
	}

Image(Device device) {
	super(device);
}

/**
 * Constructs an empty instance of this class with the
 * specified width and height. The result may be drawn upon
 * by creating a GC and using any of its drawing operations,
 * as shown in the following example:
 * <pre>
 *    Image i = new Image(device, width, height);
 *    GC gc = new GC(i);
 *    gc.drawRectangle(0, 0, 50, 50);
 *    gc.dispose();
 * </pre>
 * <p>
 * Note: Some platforms may have a limitation on the size
 * of image that can be created (size depends on width, height,
 * and depth). For example, Windows 95, 98, and ME do not allow
 * images larger than 16M.
 * </p>
 * <p>
 * You must dispose the image when it is no longer required.
 * </p>
 *
 * @param device the device on which to create the image
 * @param width the width of the new image
 * @param height the height of the new image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_INVALID_ARGUMENT - if either the width or height is negative or zero</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 *
 * @see #dispose()
 */
public Image(Device device, int width, int height) {
	super(device);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		init(width, height);
		init();
	} finally {
		if (pool != null) pool.release();
	}
}

/**
 * Constructs a new instance of this class based on the
 * provided image, with an appearance that varies depending
 * on the value of the flag. The possible flag values are:
 * <dl>
 * <dt><b>{@link SWT#IMAGE_COPY}</b></dt>
 * <dd>the result is an identical copy of srcImage</dd>
 * <dt><b>{@link SWT#IMAGE_DISABLE}</b></dt>
 * <dd>the result is a copy of srcImage which has a <em>disabled</em> look</dd>
 * <dt><b>{@link SWT#IMAGE_GRAY}</b></dt>
 * <dd>the result is a copy of srcImage which has a <em>gray scale</em> look</dd>
 * </dl>
 * <p>
 * You must dispose the image when it is no longer required.
 * </p>
 *
 * @param device the device on which to create the image
 * @param srcImage the image to use as the source
 * @param flag the style, either <code>IMAGE_COPY</code>, <code>IMAGE_DISABLE</code> or <code>IMAGE_GRAY</code>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if srcImage is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the flag is not one of <code>IMAGE_COPY</code>, <code>IMAGE_DISABLE</code> or <code>IMAGE_GRAY</code></li>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or an icon, or is otherwise in an invalid state</li>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the depth of the image is not supported</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 *
 * @see #dispose()
 */
public Image(Device device, Image srcImage, int flag) {
	super(device);
	if (srcImage == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (srcImage.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	switch (flag) {
		case SWT.IMAGE_COPY:
		case SWT.IMAGE_DISABLE:
		case SWT.IMAGE_GRAY:
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		this.type = srcImage.type;
		/* Get source image size */
		NSSize size = srcImage.handle.size();
		int srcWidth = (int)size.width;
		int srcHeight = (int)size.height;

		/* Copy alpha information (transparent pixel and alpha data) for 100% & 200% image representations from source image*/
		alphaInfo_100 = new AlphaInfo();
		copyAlphaInfo(srcImage.alphaInfo_100, alphaInfo_100);
		if (srcImage.alphaInfo_200 != null) {
			alphaInfo_200 = new AlphaInfo();
			copyAlphaInfo(srcImage.alphaInfo_200, alphaInfo_200);
		}

		/* Create the image */
		handle = (NSImage)new NSImage().alloc();
		handle = handle.initWithSize(size);
		handle.setCacheMode(OS.NSImageCacheNever);

		/* Create the 100% representation for the new image from source image & apply flag */
		createRepFromSourceAndApplyFlag(srcImage.getRepresentation (100), srcWidth, srcHeight, flag);

		imageFileNameProvider = srcImage.imageFileNameProvider;
		imageDataProvider = srcImage.imageDataProvider;
		this.styleFlag = srcImage.styleFlag | flag;
		if (imageFileNameProvider != null || imageDataProvider != null) {
			/* If source image has 200% representation then create the 200% representation for the new image & apply flag */
			NSBitmapImageRep rep200 = srcImage.getRepresentation (200);
			if (rep200 != null) createRepFromSourceAndApplyFlag(rep200, srcWidth * 2, srcHeight * 2, flag);
		}
		init();
	} finally {
		if (pool != null) pool.release();
	}
}

/**
 * Copies the AlphaInfo from source to destination.
 */
private void copyAlphaInfo(AlphaInfo src_alphaInfo, AlphaInfo dest_alphaInfo) {
	dest_alphaInfo.transparentPixel = src_alphaInfo.transparentPixel;
	dest_alphaInfo.alpha = src_alphaInfo.alpha;
	if (src_alphaInfo.alphaData != null) {
		dest_alphaInfo.alphaData = new byte[src_alphaInfo.alphaData.length];
		System.arraycopy(src_alphaInfo.alphaData, 0, dest_alphaInfo.alphaData, 0, dest_alphaInfo.alphaData.length);
	}
}

/**
 * Creates a new NSBitmapImageRep image representation for the Image from the source NSBitmapImageRep representation and adds the rep to the Image.
 * Applies the flag to the newly created representation. This method is invoked from the Image copy constructor.
 */
private void createRepFromSourceAndApplyFlag(NSBitmapImageRep srcRep, int srcWidth, int srcHeight, int flag) {
	long srcData = srcRep.bitmapData();
	long srcBitmapFormat = srcRep.bitmapFormat();
	long srcBpp = srcRep.bitsPerPixel();
	long srcBpr = srcRep.bytesPerRow();

	NSBitmapImageRep rep = (NSBitmapImageRep)new NSBitmapImageRep().alloc();
	rep = rep.initWithBitmapDataPlanes(0, srcWidth, srcHeight, srcRep.bitsPerSample(), srcRep.samplesPerPixel(), srcRep.hasAlpha(), srcRep.isPlanar(), OS.NSDeviceRGBColorSpace, srcBitmapFormat, srcRep.bytesPerRow(), srcBpp);
	handle.addRepresentation(rep);
	rep.release();

	long data = rep.bitmapData();
	C.memmove(data, srcData, srcWidth * srcHeight * 4);
	if (flag != SWT.IMAGE_COPY) {
		final int redOffset, greenOffset, blueOffset;
		if (srcBpp == 32 && (srcBitmapFormat & OS.NSAlphaFirstBitmapFormat) == 0) {
			redOffset = 0;
			greenOffset = 1;
			blueOffset = 2;
		} else {
			redOffset = 1;
			greenOffset = 2;
			blueOffset = 3;
		}
		/* Apply transformation */
		switch (flag) {
		case SWT.IMAGE_DISABLE: {
			Color zeroColor = this.device.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
			RGB zeroRGB = zeroColor.getRGB();
			byte zeroRed = (byte)zeroRGB.red;
			byte zeroGreen = (byte)zeroRGB.green;
			byte zeroBlue = (byte)zeroRGB.blue;
			Color oneColor = this.device.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
			RGB oneRGB = oneColor.getRGB();
			byte oneRed = (byte)oneRGB.red;
			byte oneGreen = (byte)oneRGB.green;
			byte oneBlue = (byte)oneRGB.blue;
			byte[] line = new byte[(int)srcBpr];
			for (int y=0; y<srcHeight; y++) {
				C.memmove(line, data + (y * srcBpr), srcBpr);
				int offset = 0;
				for (int x=0; x<srcWidth; x++) {
					int red = line[offset+redOffset] & 0xFF;
					int green = line[offset+greenOffset] & 0xFF;
					int blue = line[offset+blueOffset] & 0xFF;
					int intensity = red * red + green * green + blue * blue;
					if (intensity < 98304) {
						line[offset+redOffset] = zeroRed;
						line[offset+greenOffset] = zeroGreen;
						line[offset+blueOffset] = zeroBlue;
					} else {
						line[offset+redOffset] = oneRed;
						line[offset+greenOffset] = oneGreen;
						line[offset+blueOffset] = oneBlue;
					}
					offset += 4;
				}
				C.memmove(data + (y * srcBpr), line, srcBpr);
			}
			break;
		}
		case SWT.IMAGE_GRAY: {
			byte[] line = new byte[(int)srcBpr];
			for (int y=0; y<srcHeight; y++) {
				C.memmove(line, data + (y * srcBpr), srcBpr);
				int offset = 0;
				for (int x=0; x<srcWidth; x++) {
					int red = line[offset+redOffset] & 0xFF;
					int green = line[offset+greenOffset] & 0xFF;
					int blue = line[offset+blueOffset] & 0xFF;
					byte intensity = (byte)((red+red+green+green+green+green+green+blue) >> 3);
					line[offset+redOffset] = line[offset+greenOffset] = line[offset+blueOffset] = intensity;
					offset += 4;
				}
				C.memmove(data + (y * srcBpr), line, srcBpr);
			}
			break;
		}
		}
	}
}

/**
 * Constructs an empty instance of this class with the
 * width and height of the specified rectangle. The result
 * may be drawn upon by creating a GC and using any of its
 * drawing operations, as shown in the following example:
 * <pre>
 *    Image i = new Image(device, boundsRectangle);
 *    GC gc = new GC(i);
 *    gc.drawRectangle(0, 0, 50, 50);
 *    gc.dispose();
 * </pre>
 * <p>
 * Note: Some platforms may have a limitation on the size
 * of image that can be created (size depends on width, height,
 * and depth). For example, Windows 95, 98, and ME do not allow
 * images larger than 16M.
 * </p>
 * <p>
 * You must dispose the image when it is no longer required.
 * </p>
 *
 * @param device the device on which to create the image
 * @param bounds a rectangle specifying the image's width and height (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the bounds rectangle is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if either the rectangle's width or height is negative</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 *
 * @see #dispose()
 */
public Image(Device device, Rectangle bounds) {
	super(device);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		init(bounds.width, bounds.height);
		init();
	} finally {
		if (pool != null) pool.release();
	}
}

/**
 * Constructs an instance of this class from the given
 * <code>ImageData</code>.
 * <p>
 * You must dispose the image when it is no longer required.
 * </p>
 *
 * @param device the device on which to create the image
 * @param data the image data to create the image from (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the image data is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the depth of the ImageData is not supported</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 *
 * @see #dispose()
 */
public Image(Device device, ImageData data) {
	super(device);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		init(data);
		init();
	} finally {
		if (pool != null) pool.release();
	}
}

/**
 * Constructs an instance of this class, whose type is
 * <code>SWT.ICON</code>, from the two given <code>ImageData</code>
 * objects. The two images must be the same size. Pixel transparency
 * in either image will be ignored.
 * <p>
 * The mask image should contain white wherever the icon is to be visible,
 * and black wherever the icon is to be transparent. In addition,
 * the source image should contain black wherever the icon is to be
 * transparent.
 * </p>
 * <p>
 * You must dispose the image when it is no longer required.
 * </p>
 *
 * @param device the device on which to create the icon
 * @param source the color data for the icon
 * @param mask the mask data for the icon
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if either the source or mask is null </li>
 *    <li>ERROR_INVALID_ARGUMENT - if source and mask are different sizes</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 *
 * @see #dispose()
 */
public Image(Device device, ImageData source, ImageData mask) {
	super(device);
	if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (mask == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (source.width != mask.width || source.height != mask.height) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		mask = ImageData.convertMask(mask);
		ImageData image = new ImageData(source.width, source.height, source.depth, source.palette, source.scanlinePad, source.data);
		image.maskPad = mask.scanlinePad;
		image.maskData = mask.data;
		init(image);
	} finally {
		if (pool != null) pool.release();
	}
}

/**
 * Constructs an instance of this class by loading its representation
 * from the specified input stream. Throws an error if an error
 * occurs while loading the image, or if the result is an image
 * of an unsupported type.  Application code is still responsible
 * for closing the input stream.
 * <p>
 * This constructor is provided for convenience when loading a single
 * image only. If the stream contains multiple images, only the first
 * one will be loaded. To load multiple images, use
 * <code>ImageLoader.load()</code>.
 * </p><p>
 * This constructor may be used to load a resource as follows:
 * </p>
 * <pre>
 *     static Image loadImage (Display display, Class clazz, String string) {
 *          InputStream stream = clazz.getResourceAsStream (string);
 *          if (stream == null) return null;
 *          Image image = null;
 *          try {
 *               image = new Image (display, stream);
 *          } catch (SWTException ex) {
 *          } finally {
 *               try {
 *                    stream.close ();
 *               } catch (IOException ex) {}
 *          }
 *          return image;
 *     }
 * </pre>
 * <p>
 * You must dispose the image when it is no longer required.
 * </p>
 *
 * @param device the device on which to create the image
 * @param stream the input stream to load the image from
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the stream is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_IO - if an IO error occurs while reading from the stream</li>
 *    <li>ERROR_INVALID_IMAGE - if the image stream contains invalid data </li>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the image stream describes an image with an unsupported depth</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image stream contains an unrecognized format</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 *
 * @see #dispose()
 */
public Image(Device device, InputStream stream) {
	super(device);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		init(new ImageData(stream));
		init();
	} finally {
		if (pool != null) pool.release();
	}
}

/**
 * Constructs an instance of this class by loading its representation
 * from the file with the specified name. Throws an error if an error
 * occurs while loading the image, or if the result is an image
 * of an unsupported type.
 * <p>
 * This constructor is provided for convenience when loading
 * a single image only. If the specified file contains
 * multiple images, only the first one will be used.
 * <p>
 * You must dispose the image when it is no longer required.
 * </p>
 *
 * @param device the device on which to create the image
 * @param filename the name of the file to load the image from
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the file name is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_IO - if an IO error occurs while reading from the file</li>
 *    <li>ERROR_INVALID_IMAGE - if the image file contains invalid data </li>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the image file describes an image with an unsupported depth</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image file contains an unrecognized format</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 *
 * @see #dispose()
 */
public Image(Device device, String filename) {
	super(device);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		if (filename == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		initNative(filename);
		if (this.handle == null) init(new ImageData(filename));
		init();
	} finally {
		if (pool != null) pool.release();
	}
}

/**
 * Constructs an instance of this class by loading its representation
 * from the file retrieved from the ImageFileNameProvider. Throws an
 * error if an error occurs while loading the image, or if the result
 * is an image of an unsupported type.
 * <p>
 * This constructor is provided for convenience for loading image as
 * per DPI level.
 *
 * @param device the device on which to create the image
 * @param imageFileNameProvider the ImageFileNameProvider object that is
 * to be used to get the file name
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the ImageFileNameProvider is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the fileName provided by ImageFileNameProvider is null at 100% zoom</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_IO - if an IO error occurs while reading from the file</li>
 *    <li>ERROR_INVALID_IMAGE - if the image file contains invalid data </li>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the image file describes an image with an unsupported depth</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image file contains an unrecognized format</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 * @since 3.104
 */
public Image(Device device, ImageFileNameProvider imageFileNameProvider) {
	super(device);
	if (imageFileNameProvider == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.imageFileNameProvider = imageFileNameProvider;
	String filename = imageFileNameProvider.getImagePath(100);
	if (filename == null) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		initNative(filename);
		if (this.handle == null) init(new ImageData(filename));
		init();
		String filename2x = imageFileNameProvider.getImagePath(200);
		if (filename2x != null) {
			alphaInfo_200 = new AlphaInfo();
			id id = NSImageRep.imageRepWithContentsOfFile(NSString.stringWith(filename2x));
			NSImageRep rep = new NSImageRep(id);
			handle.addRepresentation(rep);
		}
	} finally {
		if (pool != null) pool.release();
	}
}

/**
 * Constructs an instance of this class by loading its representation
 * from the ImageData retrieved from the ImageDataProvider. Throws an
 * error if an error occurs while loading the image, or if the result
 * is an image of an unsupported type.
 * <p>
 * This constructor is provided for convenience for loading image as
 * per DPI level.
 *
 * @param device the device on which to create the image
 * @param imageDataProvider the ImageDataProvider object that is
 * to be used to get the ImageData
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the ImageDataProvider is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the ImageData provided by ImageDataProvider is null at 100% zoom</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_IO - if an IO error occurs while reading from the file</li>
 *    <li>ERROR_INVALID_IMAGE - if the image file contains invalid data </li>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the image file describes an image with an unsupported depth</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image file contains an unrecognized format</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 * @since 3.104
 */
public Image(Device device, ImageDataProvider imageDataProvider) {
	super(device);
	if (imageDataProvider == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.imageDataProvider = imageDataProvider;
	ImageData data = imageDataProvider.getImageData (100);
	if (data == null) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		init (data);
		init ();
		ImageData data2x = imageDataProvider.getImageData (200);
		if (data2x != null) {
			alphaInfo_200 = new AlphaInfo();
			NSBitmapImageRep rep = createRepresentation (data2x, alphaInfo_200);
			handle.addRepresentation(rep);
			rep.release();
		}
	} finally {
		if (pool != null) pool.release();
	}
}

private AlphaInfo _getAlphaInfoAtCurrentZoom (NSBitmapImageRep rep) {
	int deviceZoom = DPIUtil.getDeviceZoom();
	if (deviceZoom != 100 && (imageFileNameProvider != null || imageDataProvider != null)) {
		if (alphaInfo_100.alphaData != null && alphaInfo_200 != null) {
			if (alphaInfo_200.alphaData == null) initAlpha_200(rep);
			return alphaInfo_200;
		}
	}
	return alphaInfo_100;
}

ImageData _getImageData (NSBitmapImageRep imageRep, AlphaInfo info) {
	long width = imageRep.pixelsWide();
	long height = imageRep.pixelsHigh();
	long bpr = imageRep.bytesPerRow();
	long bpp = imageRep.bitsPerPixel();
	long bitmapData = imageRep.bitmapData();
	long bitmapFormat = imageRep.bitmapFormat();
	long dataSize = height * bpr;
	byte[] srcData = new byte[(int)dataSize];
	C.memmove(srcData, bitmapData, dataSize);

	PaletteData palette;
	if (bpp == 32 && (bitmapFormat & OS.NSAlphaFirstBitmapFormat) == 0) {
		palette = new PaletteData(0xFF000000, 0xFF0000, 0xFF00);
	} else {
		palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
	}
	ImageData data = new ImageData((int)width, (int)height, (int)bpp, palette, 1, srcData);
	data.bytesPerLine = (int)bpr;
	if (imageRep.hasAlpha() && info.transparentPixel == -1 && info.alpha == -1 && info.alphaData == null) {
		byte[] alphaD = new byte[(int) (dataSize/4)];
		int offset = (bitmapFormat & OS.NSAlphaFirstBitmapFormat) != 0 ? 0 : 3, a = 0;
		for (int i = offset; i < srcData.length && a < alphaD.length; i+= 4, a++) {
			alphaD[a] = srcData[i];
		}
		data.alphaData = alphaD;
	} else {
		data.transparentPixel = info.transparentPixel;
		if (info.transparentPixel == -1 && type == SWT.ICON) {
			/* Get the icon mask data */
			int maskPad = 2;
			long maskBpl = (((width + 7) / 8) + (maskPad - 1)) / maskPad * maskPad;
			byte[] maskData = new byte[(int)(height * maskBpl)];
			int offset = 0, maskOffset = 0;
			for (int y = 0; y<height; y++) {
				for (int x = 0; x<width; x++) {
					if (srcData[offset] != 0) {
						maskData[maskOffset + (x >> 3)] |= (1 << (7 - (x & 0x7)));
					} else {
						maskData[maskOffset + (x >> 3)] &= ~(1 << (7 - (x & 0x7)));
					}
					offset += 4;
				}
				maskOffset += maskBpl;
			}
			data.maskData = maskData;
			data.maskPad = maskPad;
		}
		data.alpha = info.alpha;
		if (info.alpha == -1 && info.alphaData != null) {
			data.alphaData = new byte[info.alphaData.length];
			System.arraycopy(info.alphaData, 0, data.alphaData, 0, info.alphaData.length);
		}
	}
	if (bpp == 32) {
		int offset = (bitmapFormat & OS.NSAlphaFirstBitmapFormat) != 0 ? 0 : 3;
		for (int i = offset; i < srcData.length; i+= 4) {
			srcData[i] = 0;
		}
	}
	return data;
}

/**
 * Invokes platform specific functionality to allocate a new image.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Image</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param device the device on which to allocate the color
 * @param type the type of the image (<code>SWT.BITMAP</code> or <code>SWT.ICON</code>)
 * @param nsImage the OS data for the image
 *
 * @noreference This method is not intended to be referenced by clients.
 */
public static Image cocoa_new(Device device, int type, NSImage nsImage) {
	Image image = new Image(device);
	image.type = type;
	image.handle = nsImage;
	image.alphaInfo_100 = new AlphaInfo();
	return image;
}

void createAlpha () {
	AlphaInfo info = alphaInfo_100;
	if (info.transparentPixel == -1 && info.alpha == -1 && info.alphaData == null) return;
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSBitmapImageRep imageRep = getRepresentation();
		long height = imageRep.pixelsHigh();
		long bpr = imageRep.bytesPerRow();
		long bitmapData = imageRep.bitmapData();
		long format = imageRep.bitmapFormat();
		long dataSize = height * bpr;
		byte[] srcData = new byte[(int)dataSize];
		C.memmove(srcData, bitmapData, dataSize);
		if (info.transparentPixel != -1) {
			if ((format & OS.NSAlphaFirstBitmapFormat) != 0) {
				for (int i=0; i<dataSize; i+=4) {
					int pixel = ((srcData[i+1] & 0xFF) << 16) | ((srcData[i+2] & 0xFF) << 8) | (srcData[i+3] & 0xFF);
					srcData[i] = (byte)(pixel == info.transparentPixel ? 0 : 0xFF);
				}
			} else {
				for (int i=0; i<dataSize; i+=4) {
					int pixel = ((srcData[i+0] & 0xFF) << 16) | ((srcData[i+1] & 0xFF) << 8) | (srcData[i+2] & 0xFF);
					srcData[i] = (byte)(pixel == info.transparentPixel ? 0 : 0xFF);
				}
			}
		} else if (info.alpha != -1) {
			byte a = (byte)info.alpha;
			for (int i=(format & OS.NSAlphaFirstBitmapFormat) != 0 ? 0 : 3; i<dataSize; i+=4) {
				srcData[i] = a;
			}
		} else {
			long width = imageRep.pixelsWide();
			int offset = 0, alphaOffset = (format & OS.NSAlphaFirstBitmapFormat) != 0 ? 0 : 3;
			for (int y = 0; y<height; y++) {
				for (int x = 0; x<width && alphaOffset<info.alphaData.length && offset<srcData.length; x++) {
					srcData[offset] = info.alphaData[alphaOffset];
					offset += 4;
					alphaOffset += 1;
				}
			}
		}

		// Since we just calculated alpha for the image rep, tell it that it now has an alpha component.
		imageRep.setAlpha(true);

		C.memmove(bitmapData, srcData, dataSize);
	} finally {
		if (pool != null) pool.release();
	}
}

private NSBitmapImageRep createRepresentation(ImageData imageData, AlphaInfo alphaInfo) {
	NSBitmapImageRep rep = (NSBitmapImageRep)new NSBitmapImageRep().alloc();

	PaletteData palette = imageData.palette;
	if (!(((imageData.depth == 1 || imageData.depth == 2 || imageData.depth == 4 || imageData.depth == 8) && !palette.isDirect) ||
			((imageData.depth == 8) || (imageData.depth == 16 || imageData.depth == 24 || imageData.depth == 32) && palette.isDirect)))
				SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);

	/* Create the image */
	int dataSize = imageData.width * imageData.height * 4;

	/* Initialize data */
	int bpr = imageData.width * 4;
	byte[] buffer = new byte[dataSize];
	if (palette.isDirect) {
		ImageData.blit(ImageData.BLIT_SRC,
			imageData.data, imageData.depth, imageData.bytesPerLine, imageData.getByteOrder(), 0, 0, imageData.width, imageData.height, palette.redMask, palette.greenMask, palette.blueMask,
			ImageData.ALPHA_OPAQUE, null, 0, 0, 0,
			buffer, 32, bpr, ImageData.MSB_FIRST, 0, 0, imageData.width, imageData.height, 0xFF0000, 0xFF00, 0xFF,
			false, false);
	} else {
		RGB[] rgbs = palette.getRGBs();
		int length = rgbs.length;
		byte[] srcReds = new byte[length];
		byte[] srcGreens = new byte[length];
		byte[] srcBlues = new byte[length];
		for (int i = 0; i < rgbs.length; i++) {
			RGB rgb = rgbs[i];
			if (rgb == null) continue;
			srcReds[i] = (byte)rgb.red;
			srcGreens[i] = (byte)rgb.green;
			srcBlues[i] = (byte)rgb.blue;
		}
		ImageData.blit(ImageData.BLIT_SRC,
			imageData.data, imageData.depth, imageData.bytesPerLine, imageData.getByteOrder(), 0, 0, imageData.width, imageData.height, srcReds, srcGreens, srcBlues,
			ImageData.ALPHA_OPAQUE, null, 0, 0, 0,
			buffer, 32, bpr, ImageData.MSB_FIRST, 0, 0, imageData.width, imageData.height, 0xFF0000, 0xFF00, 0xFF,
			false, false);
	}

	/* Initialize transparency */
	int transparency = imageData.getTransparencyType();
	boolean hasAlpha = transparency != SWT.TRANSPARENCY_NONE;
	if (transparency == SWT.TRANSPARENCY_MASK || imageData.transparentPixel != -1) {
		this.type = imageData.transparentPixel != -1 ? SWT.BITMAP : SWT.ICON;
		if (imageData.transparentPixel != -1) {
			int transRed = 0, transGreen = 0, transBlue = 0;
			if (palette.isDirect) {
				RGB rgb = palette.getRGB(imageData.transparentPixel);
				transRed = rgb.red;
				transGreen = rgb.green;
				transBlue = rgb.blue;
			} else {
				RGB[] rgbs = palette.getRGBs();
				if (imageData.transparentPixel < rgbs.length) {
					RGB rgb = rgbs[imageData.transparentPixel];
					transRed = rgb.red;
					transGreen = rgb.green;
					transBlue = rgb.blue;
				}
			}
			alphaInfo.transparentPixel = transRed << 16 | transGreen << 8 | transBlue;
		}
		ImageData maskImage = imageData.getTransparencyMask();
		byte[] maskData = maskImage.data;
		int maskBpl = maskImage.bytesPerLine;
		int offset = 0, maskOffset = 0;
		for (int y = 0; y<imageData.height; y++) {
			for (int x = 0; x<imageData.width; x++) {
				buffer[offset] = ((maskData[maskOffset + (x >> 3)]) & (1 << (7 - (x & 0x7)))) != 0 ? (byte)0xff : 0;
				offset += 4;
			}
			maskOffset += maskBpl;
		}
	} else {
		this.type = SWT.BITMAP;
		if (imageData.alpha != -1) {
			hasAlpha = true;
			alphaInfo.alpha = imageData.alpha;
			byte a = (byte)alphaInfo.alpha;
			for (int dataIndex=0; dataIndex<buffer.length; dataIndex+=4) {
				buffer[dataIndex] = a;
			}
		} else if (imageData.alphaData != null) {
			hasAlpha = true;
			alphaInfo.alphaData = new byte[imageData.alphaData.length];
			System.arraycopy(imageData.alphaData, 0, alphaInfo.alphaData, 0, alphaInfo.alphaData.length);
			int offset = 0, alphaOffset = 0;
			for (int y = 0; y<imageData.height; y++) {
				for (int x = 0; x<imageData.width; x++) {
					buffer[offset] = alphaInfo.alphaData[alphaOffset];
					offset += 4;
					alphaOffset += 1;
				}
			}
		}
	}

	rep = rep.initWithBitmapDataPlanes(0, imageData.width, imageData.height, 8, hasAlpha ? 4 : 3, hasAlpha, false, OS.NSDeviceRGBColorSpace, OS.NSAlphaFirstBitmapFormat | OS.NSAlphaNonpremultipliedBitmapFormat, bpr, 32);
	C.memmove(rep.bitmapData(), buffer, dataSize);
	return rep;
}

@Override
void destroy() {
	if (memGC != null) memGC.dispose();
	handle.release();
	handle = null;
	memGC = null;
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
@Override
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Image)) return false;
	Image image = (Image)object;
	if (device != image.device || alphaInfo_100.transparentPixel != image.alphaInfo_100.transparentPixel) return false;
	if (imageDataProvider != null && image.imageDataProvider != null) {
		return styleFlag == image.styleFlag && imageDataProvider.equals (image.imageDataProvider);
	} else if (imageFileNameProvider != null && image.imageFileNameProvider != null) {
		return styleFlag == image.styleFlag && imageFileNameProvider.equals (image.imageFileNameProvider);
	} else {
		return handle == image.handle;
	}
}

/**
 * Returns the image representation at scale factor as percentage. Creates the representation if necessary.
 */
NSBitmapImageRep getRepresentation (int scaleFactor) {
	NSArray reps = handle.representations();
	NSSize size = handle.size();
	long count = reps.count();
	NSSize targetSize = new NSSize();
	targetSize.width = (int)size.width * scaleFactor / 100;
	targetSize.height = (int)size.height * scaleFactor / 100;
	NSBitmapImageRep rep;
	for (int i = 0; i < count; i++) {
		rep = new NSBitmapImageRep(reps.objectAtIndex(i));
		if ((targetSize.width == rep.pixelsWide() && targetSize.height == rep.pixelsHigh())) {
			if (rep.isKindOfClass(OS.class_NSBitmapImageRep)) {
				return rep;
			}
		}
	}
	NSBitmapImageRep newRep = createImageRep(targetSize);
	for (int i = 0; i < count; i++) {
		handle.removeRepresentation(new NSImageRep(handle.representations().objectAtIndex(0)));
	}
	handle.addRepresentation(newRep);
	return newRep;
}


/**
 * Returns the color to which to map the transparent pixel, or null if
 * the receiver has no transparent pixel.
 * <p>
 * There are certain uses of Images that do not support transparency
 * (for example, setting an image into a button or label). In these cases,
 * it may be desired to simulate transparency by using the background
 * color of the widget to paint the transparent pixels of the image.
 * Use this method to check which color will be used in these cases
 * in place of transparency. This value may be set with setBackground().
 * <p>
 *
 * @return the background color of the image, or null if there is no transparency in the image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Color getBackground() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	NSBitmapImageRep imageRep = getRepresentation();
	AlphaInfo alphaInfo = _getAlphaInfoAtCurrentZoom(imageRep);
	if (alphaInfo.transparentPixel == -1) return null;
	int red = (alphaInfo.transparentPixel >> 16) & 0xFF;
	int green = (alphaInfo.transparentPixel >> 8) & 0xFF;
	int blue = (alphaInfo.transparentPixel >> 0) & 0xFF;
	return Color.cocoa_new(device, new double []{red / 255f, green / 255f, blue / 255f, 1});
}

/**
 * Returns the bounds of the receiver. The rectangle will always
 * have x and y values of 0, and the width and height of the
 * image.
 *
 * @return a rectangle specifying the image's bounds in points.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or an icon</li>
 * </ul>
 */
public Rectangle getBounds() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		if (width != -1 && height != -1) {
			return new Rectangle(0, 0, width, height);
		}
		NSSize size = handle.size();
		return new Rectangle(0, 0, width = (int)size.width, height = (int)size.height);
	} finally {
		if (pool != null) pool.release();
	}
}

/**
 * Returns the bounds of the receiver. The rectangle will always
 * have x and y values of 0, and the width and height of the
 * image in pixels.
 *
 * @return a rectangle specifying the image's bounds in pixels.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or an icon</li>
 * </ul>
 * @since 3.105
 * @deprecated This API doesn't serve the purpose in an environment having
 *             multiple monitors with different DPIs, hence deprecated.
 */
@Deprecated
public Rectangle getBoundsInPixels() {
	Rectangle bounds = getBounds();
	int scaleFactor = (int) NSScreen.mainScreen().backingScaleFactor();
	bounds.width *= scaleFactor;
	bounds.height *= scaleFactor;
	return bounds;
}

/**
 * Returns an <code>ImageData</code> based on the receiver.
 * Modifications made to this <code>ImageData</code> will not
 * affect the Image.
 *
 * @return an <code>ImageData</code> containing the image's data and
 *         attributes at 100% zoom level.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or an icon</li>
 * </ul>
 *
 * @see ImageData
 */
public ImageData getImageData() {
	return getImageData(100);
}

/**
 * Returns an <code>ImageData</code> based on the receiver.
 * Modifications made to this <code>ImageData</code> will not
 * affect the Image.
 *
 * @return an <code>ImageData</code> containing the image's data
 * and attributes at the current zoom level.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or an icon</li>
 * </ul>
 *
 * @see ImageData
 * @since 3.105
 * @deprecated This API doesn't serve the purpose in an environment having
 *             multiple monitors with different DPIs, hence deprecated. Use
 *             {@link #getImageData(int)} instead.
 */
@Deprecated
public ImageData getImageDataAtCurrentZoom() {
	return getImageData(DPIUtil.getDeviceZoom());
}

/**
 * Returns an {@link ImageData} for the given zoom level based on the
 * receiver.
 * <p>
 * Note that this method is mainly intended to be used by custom
 * implementations of {@link ImageDataProvider} that draw a composite image
 * at the requested zoom level based on other images. For custom zoom
 * levels, the image data may be an auto-scaled version of the native image
 * and may look more blurred or mangled than expected.
 * </p>
 * <p>
 * Modifications made to the returned {@code ImageData} will not affect this
 * {@code Image}.
 * </p>
 *
 * @param zoom
 *            The zoom level in % of the standard resolution (which is 1
 *            physical monitor pixel == 1 SWT logical point). Typically 100,
 *            150, or 200.
 * @return an <code>ImageData</code> containing the image's data and
 *         attributes at the given zoom level
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or an icon</li>
 * </ul>
 *
 * @since 3.106
 */
public ImageData getImageData(int zoom) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		if (zoom == 100) {
			NSBitmapImageRep imageRep;
			imageRep = getRepresentation (100);
			return _getImageData(imageRep, alphaInfo_100);
		}
		if (zoom == 200) {
			NSBitmapImageRep imageRep200 = getRepresentation (200);
			if (alphaInfo_100.alphaData != null && alphaInfo_200 != null) {
				if (alphaInfo_200.alphaData == null) initAlpha_200(imageRep200);
			}
			if (alphaInfo_200 == null) {
				initAlpha_200(imageRep200);
			}
			return _getImageData(imageRep200, alphaInfo_200);
		}
	} finally {
		if (pool != null) pool.release();
	}
	return DPIUtil.autoScaleImageData (device, getImageData(100), zoom, 100);
}

/** Returns the best available representation. May be 100% or 200% iff there is an image provider. */
NSBitmapImageRep getRepresentation () {
	return getRepresentation (DPIUtil.getDeviceZoom ());
}

NSBitmapImageRep createImageRep(NSSize targetSize) {
	return ImageUtil.createImageRep(this, targetSize);
}

/**
 * Returns an integer hash code for the receiver. Any two
 * objects that return <code>true</code> when passed to
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals
 */
@Override
public int hashCode () {
	if (imageDataProvider != null) {
		return imageDataProvider.hashCode();
	} else if (imageFileNameProvider != null) {
		return imageFileNameProvider.hashCode();
	} else {
		return handle != null ? (int)handle.id : 0;
	}
}

void init(int width, int height) {
	if (width <= 0 || height <= 0) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	this.type = SWT.BITMAP;
	this.width = width;
	this.height = height;

	handle = (NSImage)new NSImage().alloc();
	NSSize size = new NSSize();
	size.width = width;
	size.height = height;
	handle = handle.initWithSize(size);
	NSBitmapImageRep rep = (NSBitmapImageRep)new NSBitmapImageRep().alloc();
	rep = rep.initWithBitmapDataPlanes(0, width, height, 8, 3, false, false, OS.NSDeviceRGBColorSpace, OS.NSAlphaFirstBitmapFormat | OS.NSAlphaNonpremultipliedBitmapFormat, width * 4, 32);
	C.memset(rep.bitmapData(), 0xFF, width * height * 4);
	handle.addRepresentation(rep);
	rep.release();
	handle.setCacheMode(OS.NSImageCacheNever);
	if (alphaInfo_100 == null) alphaInfo_100 = new AlphaInfo();
}

void init(ImageData image) {
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);

	if (handle != null) handle.release();

	handle = (NSImage)new NSImage().alloc();
	NSSize size = new NSSize();
	size.width = width;
	size.height = height;
	handle = handle.initWithSize(size);
	this.width = image.width;
	this.height = image.height;
	if (alphaInfo_100 == null) alphaInfo_100 = new AlphaInfo();
	NSBitmapImageRep rep = createRepresentation(image, alphaInfo_100);
	handle.addRepresentation(rep);
	rep.release();
	handle.setCacheMode(OS.NSImageCacheNever);
}

void initAlpha_200(NSBitmapImageRep nativeRep) {
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		int width = (int)nativeRep.pixelsWide();
		int height = (int)nativeRep.pixelsHigh();

		boolean hasAlpha = nativeRep.hasAlpha();
		int bpr = width * 4;
		NSBitmapImageRep rep = (NSBitmapImageRep)new NSBitmapImageRep().alloc();
		rep = rep.initWithBitmapDataPlanes(0, width, height, 8, hasAlpha ? 4 : 3, hasAlpha, false, OS.NSDeviceRGBColorSpace, OS.NSAlphaFirstBitmapFormat | OS.NSAlphaNonpremultipliedBitmapFormat, bpr, 32);

		if (alphaInfo_200 == null) alphaInfo_200 = new AlphaInfo();
		alphaInfo_200.init(nativeRep, rep);
		rep.release();
	} finally {
		if (pool != null) pool.release();
	}

}

void initAlpha_100(NSBitmapImageRep nativeRep) {
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		int width = (int)nativeRep.pixelsWide();
		int height = (int)nativeRep.pixelsHigh();

		boolean hasAlpha = nativeRep.hasAlpha();
		int bpr = width * 4;
		NSBitmapImageRep rep = (NSBitmapImageRep)new NSBitmapImageRep().alloc();
		rep = rep.initWithBitmapDataPlanes(0, width, height, 8, hasAlpha ? 4 : 3, hasAlpha, false, OS.NSDeviceRGBColorSpace, OS.NSAlphaFirstBitmapFormat | OS.NSAlphaNonpremultipliedBitmapFormat, bpr, 32);

		if (alphaInfo_100 == null) alphaInfo_100 = new AlphaInfo();
		alphaInfo_100.init(nativeRep, rep);
		rep.release();
	} finally {
		if (pool != null) pool.release();
	}

}

void initNative(String filename) {
	NSAutoreleasePool pool = null;
	NSImage nativeImage = null;

	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		nativeImage = new NSImage();
		nativeImage.alloc();

		// initByReferencingFile returns null if the file can't be found or is
		// not an image.
		nativeImage = nativeImage.initWithContentsOfFile(NSString.stringWith(filename));
		if (nativeImage == null) {
			// In order to get the same kind of exception, let the file format try to load and throw
			// the appropriate exception. It is possible file format supports some image formats
			// that is not natively supported as well.
			return;
		}

		NSImageRep nativeRep = nativeImage.bestRepresentationForDevice(null);
		if (!nativeRep.isKindOfClass(OS.class_NSBitmapImageRep)) {
			return;
		}
		width = (int)nativeRep.pixelsWide();
		height = (int)nativeRep.pixelsHigh();

		boolean hasAlpha = nativeRep.hasAlpha();
		int bpr = width * 4;
		handle = (NSImage)new NSImage().alloc();
		NSSize size = new NSSize();
		size.width = width;
		size.height = height;
		handle = handle.initWithSize(size);
		NSBitmapImageRep rep = (NSBitmapImageRep)new NSBitmapImageRep().alloc();
		rep = rep.initWithBitmapDataPlanes(0, width, height, 8, hasAlpha ? 4 : 3, hasAlpha, false, OS.NSDeviceRGBColorSpace, OS.NSAlphaFirstBitmapFormat | OS.NSAlphaNonpremultipliedBitmapFormat, bpr, 32);
		handle.addRepresentation(rep);
		rep.release();
		handle.setCacheMode(OS.NSImageCacheNever);

		if (alphaInfo_100 == null) alphaInfo_100 = new AlphaInfo();
		alphaInfo_100.init(nativeRep, rep);

		// For compatibility, images created from .ico files are treated as SWT.ICON format, even though
		// they are no different than other bitmaps in Cocoa.
		if (filename.toLowerCase().endsWith(".ico")) {
			this.type = SWT.ICON;
		} else {
			this.type = SWT.BITMAP;
		}
	} finally {
		if (nativeImage != null) nativeImage.release();
		if (pool != null) pool.release();
	}
}

/**
 * Invokes platform specific functionality to allocate a new GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Image</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param data the platform specific GC data
 * @return the platform specific GC handle
 *
 * @noreference This method is not intended to be referenced by clients.
 */
@Override
public long internal_new_GC (GCData data) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (type != SWT.BITMAP || memGC != null) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		int scaleFactor = DPIUtil.getDeviceZoom() / 100;
		NSBitmapImageRep imageRep = getRepresentation();

		NSGraphicsContext context = NSGraphicsContext.graphicsContextWithBitmapImageRep(imageRep);
		if (context == null) {
			imageRep.setAlpha(false);
			context = NSGraphicsContext.graphicsContextWithBitmapImageRep(imageRep);
		}
		NSGraphicsContext flippedContext = NSGraphicsContext.graphicsContextWithGraphicsPort(context.graphicsPort(), true);
		context = flippedContext;
		context.retain();
		if (data != null) data.flippedContext = flippedContext;
		NSGraphicsContext.static_saveGraphicsState();
		NSGraphicsContext.setCurrentContext(context);
		NSAffineTransform transform = NSAffineTransform.transform();
		NSSize size = handle.size();
		transform.translateXBy(0, size.height * scaleFactor);
		transform.scaleXBy(scaleFactor, -scaleFactor);
		transform.set();
		NSGraphicsContext.static_restoreGraphicsState();
		if (data != null) {
			int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
			if ((data.style & mask) == 0) {
				data.style |= SWT.LEFT_TO_RIGHT;
			}
			data.device = device;
			data.background = device.COLOR_WHITE.handle;
			data.foreground = device.COLOR_BLACK.handle;
			data.font = device.systemFont;
			data.image = this;
		}
		return context.id;
	} finally {
		if (pool != null) pool.release();
	}
}

/**
 * Invokes platform specific functionality to dispose a GC handle.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Image</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param hDC the platform specific GC handle
 * @param data the platform specific GC data
 *
 * @noreference This method is not intended to be referenced by clients.
 */
@Override
public void internal_dispose_GC (long hDC, GCData data) {
	long context = hDC;
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		if (context != 0) {
			/*
			* Bug in cocoa.  For some reason, there cases when the image pixels
			* are not transfered from the underlining CGImage into the bitmap
			* representation.   This only happens when bitmapData() is called.
			*/
			NSBitmapImageRep imageRep = getRepresentation();
			imageRep.bitmapData();

			NSGraphicsContext contextObj = new NSGraphicsContext(context);
			contextObj.release();
		}
//		handle.setCacheMode(OS.NSImageCacheDefault);
	} finally {
		if (pool != null) pool.release();
	}
}

/**
 * Returns <code>true</code> if the image has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the image.
 * When an image has been disposed, it is an error to
 * invoke any other method (except {@link #dispose()}) using the image.
 *
 * @return <code>true</code> when the image is disposed and <code>false</code> otherwise
 */
@Override
public boolean isDisposed() {
	return handle == null;
}

/**
 * Sets the color to which to map the transparent pixel.
 * <p>
 * There are certain uses of <code>Images</code> that do not support
 * transparency (for example, setting an image into a button or label).
 * In these cases, it may be desired to simulate transparency by using
 * the background color of the widget to paint the transparent pixels
 * of the image. This method specifies the color that will be used in
 * these cases. For example:</p>
 * <pre>
 *    Button b = new Button();
 *    image.setBackground(b.getBackground());
 *    b.setImage(image);
 * </pre>
 * <p>
 * The image may be modified by this operation (in effect, the
 * transparent regions may be filled with the supplied color).  Hence
 * this operation is not reversible and it is not legal to call
 * this function twice or with a null argument.
 * </p><p>
 * This method has no effect if the receiver does not have a transparent
 * pixel value.
 * </p>
 *
 * @param color the color to use when a transparent pixel is specified
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
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSBitmapImageRep imageRep = getRepresentation();
		AlphaInfo alphaInfo = _getAlphaInfoAtCurrentZoom(imageRep);
		if (alphaInfo.transparentPixel == -1) return;
		byte red = (byte)((alphaInfo.transparentPixel >> 16) & 0xFF);
		byte green = (byte)((alphaInfo.transparentPixel >> 8) & 0xFF);
		byte blue = (byte)((alphaInfo.transparentPixel >> 0) & 0xFF);
		byte newRed = (byte)((int)(color.handle[0] * 255) & 0xFF);
		byte newGreen = (byte)((int)(color.handle[1] * 255) & 0xFF);
		byte newBlue = (byte)((int)(color.handle[2] * 255) & 0xFF);
		long height = imageRep.pixelsHigh();
		long bpr = imageRep.bytesPerRow();
		long data = imageRep.bitmapData();
		long format = imageRep.bitmapFormat();
		long bpp = imageRep.bitsPerPixel();
		final int redOffset, greenOffset, blueOffset;
		if (bpp == 32 && (format & OS.NSAlphaFirstBitmapFormat) == 0) {
			redOffset = 0;
			greenOffset = 1;
			blueOffset = 2;
		} else {
			redOffset = 1;
			greenOffset = 2;
			blueOffset = 3;
		}
		byte[] line = new byte[(int)bpr];
		for (int i = 0, offset = 0; i < height; i++, offset += bpr) {
			C.memmove(line, data + offset, bpr);
			for (int j = 0; j  < line.length; j += 4) {
				if (line[j + redOffset] == red && line[j + greenOffset] == green && line[j + blueOffset] == blue) {
					line[j + redOffset] = newRed;
					line[j + greenOffset] = newGreen;
					line[j + blueOffset] = newBlue;
				}
			}
			C.memmove(data + offset, line, bpr);
		}
		alphaInfo.transparentPixel = (newRed & 0xFF) << 16 | (newGreen & 0xFF) << 8 | (newBlue & 0xFF);
	} finally {
		if (pool != null) pool.release();
	}
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
@Override
public String toString () {
	if (isDisposed()) return "Image {*DISPOSED*}";
	return "Image {" + handle + "}";
}

}

