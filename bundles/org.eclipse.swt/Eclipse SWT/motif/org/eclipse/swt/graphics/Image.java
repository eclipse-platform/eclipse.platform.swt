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

 
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import java.io.*;
 
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
	 */
	public int type;
	
	/**
	 * The handle to the OS pixmap resource.
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public int pixmap;
	
	/**
	 * The handle to the OS mask resource.
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public int mask;

	int /*long*/ surface;
	
	/**
	 * specifies the transparent pixel
	 */
	int transparentPixel = -1;
	
	/**
	 * The GC the image is currently selected in.
	 */
	GC memGC;

	/**
	 * The alpha data of the image.
	 */
	byte[] alphaData;
	
	/**
	 * The global alpha value to be used for every pixel.
	 */
	int alpha = -1;
	
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
 */
public Image(Device device, int width, int height) {
	super(device);
	init(width, height);
	init();
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
 */
public Image(Device device, Image srcImage, int flag) {
	super(device);
	device = this.device;
	if (srcImage == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (srcImage.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int xDisplay = device.xDisplay;
	this.type = srcImage.type;
	this.mask = 0;
	int[] unused = new int[1];
	int[] w = new int[1];
	int[] h = new int[1];
 	OS.XGetGeometry(xDisplay, srcImage.pixmap, unused, unused, unused, w, h, unused, unused);
 	int width = w[0];
 	int height = h[0];
	int drawable = OS.XDefaultRootWindow(xDisplay);
	/* Don't create the mask here if flag is SWT.IMAGE_GRAY. See below.*/
	if (flag != SWT.IMAGE_GRAY && ((srcImage.type == SWT.ICON && srcImage.mask != 0) || srcImage.transparentPixel != -1)) {
		/* Generate the mask if necessary. */
		if (srcImage.transparentPixel != -1) srcImage.createMask();
		int mask = OS.XCreatePixmap(xDisplay, drawable, width, height, 1);
		int gc = OS.XCreateGC(xDisplay, mask, 0, null);
		OS.XCopyArea(xDisplay, srcImage.mask, mask, gc, 0, 0, width, height, 0, 0);
		OS.XFreeGC(xDisplay, gc);
		this.mask = mask;
		/* Destroy the image mask if the there is a GC created on the image */
		if (srcImage.transparentPixel != -1 && srcImage.memGC != null) srcImage.destroyMask();
	}
	switch (flag) {
		case SWT.IMAGE_COPY:
			int[] depth = new int[1];
			OS.XGetGeometry(xDisplay, srcImage.pixmap, unused, unused, unused, unused, unused, unused, depth);
			int pixmap = OS.XCreatePixmap(xDisplay, drawable, width, height, depth[0]);
			int gc = OS.XCreateGC(xDisplay, pixmap, 0, null);
			OS.XCopyArea(xDisplay, srcImage.pixmap, pixmap, gc, 0, 0, width, height, 0, 0);
			OS.XFreeGC(xDisplay, gc);
			this.pixmap = pixmap;
			transparentPixel = srcImage.transparentPixel;
			alpha = srcImage.alpha;
			if (srcImage.alphaData != null) {
				alphaData = new byte[srcImage.alphaData.length];
				System.arraycopy(srcImage.alphaData, 0, alphaData, 0, alphaData.length);
			}
			createAlphaMask(width, height);
			break;
		case SWT.IMAGE_DISABLE:
			/* Get src image data */
			XImage srcXImage = new XImage();
			int srcXImagePtr = OS.XGetImage(xDisplay, srcImage.pixmap, 0, 0, width, height, OS.AllPlanes, OS.ZPixmap);
			OS.memmove(srcXImage, srcXImagePtr, XImage.sizeof);
			byte[] srcData = new byte[srcXImage.bytes_per_line * srcXImage.height];
			OS.memmove(srcData, srcXImage.data, srcData.length);
			/* Create destination image */
			int destPixmap = OS.XCreatePixmap(xDisplay, drawable, width, height, srcXImage.depth);
			int visualPtr = OS.XDefaultVisual(xDisplay, OS.XDefaultScreen(xDisplay));
			int screenDepth = OS.XDefaultDepthOfScreen(OS.XDefaultScreenOfDisplay(xDisplay));			
			int destXImagePtr = OS.XCreateImage(xDisplay, visualPtr, screenDepth, OS.ZPixmap, 0, 0, width, height, srcXImage.bitmap_pad, 0);
			XImage destXImage = new XImage();
			OS.memmove(destXImage, destXImagePtr, XImage.sizeof);
			int bufSize = destXImage.bytes_per_line * destXImage.height;
			int bufPtr = OS.XtMalloc(bufSize);
			destXImage.data = bufPtr;
			OS.memmove(destXImagePtr, destXImage, XImage.sizeof);
			byte[] destData = new byte[bufSize];
			/* Find the colors to map to */
			Color zeroColor = device.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
			Color oneColor = device.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
			int zeroPixel = zeroColor.handle.pixel;
			int onePixel = oneColor.handle.pixel;
			switch (srcXImage.bits_per_pixel) {
				case 1:
					/*
					 * Nothing we can reasonably do here except copy
					 * the bitmap; we can't make it a higher color depth.
					 * Short-circuit the rest of the code and return.
					 */
					gc = OS.XCreateGC(xDisplay, drawable, 0, null);
					pixmap = OS.XCreatePixmap(xDisplay, drawable, width, height, 1);
					OS.XCopyArea(xDisplay, srcImage.pixmap, pixmap, gc, 0, 0, width, height, 0, 0);
					OS.XDestroyImage(srcXImagePtr);
					OS.XDestroyImage(destXImagePtr);
					OS.XFreeGC(xDisplay, gc);
					return;
				case 4:
					SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
					break;
				case 8:
					int index = 0;
					int srcPixel, r, g, b;
					XColor[] colors = new XColor[256];
					int colormap = OS.XDefaultColormap(xDisplay, OS.XDefaultScreen(xDisplay));
					for (int y = 0; y < srcXImage.height; y++) {
						for (int x = 0; x < srcXImage.bytes_per_line; x++) {
							srcPixel = srcData[index + x] & 0xFF;
							/* Get the RGB values of srcPixel */
							if (colors[srcPixel] == null) {
								XColor color = new XColor();
								color.pixel = srcPixel;
								OS.XQueryColor(xDisplay, colormap, color);
								colors[srcPixel] = color;
							}
							XColor xColor = colors[srcPixel];
							r = (xColor.red >> 8) & 0xFF;
							g = (xColor.green >> 8) & 0xFF;
							b = (xColor.blue >> 8) & 0xFF;
							/* See if the rgb maps to 0 or 1 */
							if ((r * r + g * g + b * b) < 98304) {
								/* Map down to 0 */
								destData[index + x] = (byte)zeroPixel;
							} else {
								/* Map up to 1 */
								destData[index + x] = (byte)onePixel;
							}
						}
						index += srcXImage.bytes_per_line;
					}
					break;
				case 16:
					index = 0;
					/* Get masks */
					Visual visual = new Visual();
					OS.memmove(visual, visualPtr, Visual.sizeof);
					int redMask = visual.red_mask;
					int greenMask = visual.green_mask;
					int blueMask = visual.blue_mask;
					/* Calculate mask shifts */
					int[] shift = new int[1];
					getOffsetForMask(16, redMask, srcXImage.byte_order, shift);
					int rShift = 24 - shift[0];
					getOffsetForMask(16, greenMask, srcXImage.byte_order, shift);
					int gShift = 24 - shift[0];
					getOffsetForMask(16, blueMask, srcXImage.byte_order, shift);
					int bShift = 24 - shift[0];
					byte zeroLow = (byte)(zeroPixel & 0xFF);
					byte zeroHigh = (byte)((zeroPixel >> 8) & 0xFF);
					byte oneLow = (byte)(onePixel & 0xFF);
					byte oneHigh = (byte)((onePixel >> 8) & 0xFF);
					for (int y = 0; y < srcXImage.height; y++) {
						int xIndex = 0;
						for (int x = 0; x < srcXImage.bytes_per_line; x += 2) {
							srcPixel = ((srcData[index + xIndex + 1] & 0xFF) << 8) | (srcData[index + xIndex] & 0xFF);
							r = (srcPixel & redMask) << rShift >> 16;
							g = (srcPixel & greenMask) << gShift >> 16;
							b = (srcPixel & blueMask) << bShift >> 16;
							/* See if the rgb maps to 0 or 1 */
							if ((r * r + g * g + b * b) < 98304) {
								/* Map down to 0 */
								destData[index + xIndex] = zeroLow;
								destData[index + xIndex + 1] = zeroHigh;
							} else {
								/* Map up to 1 */
								destData[index + xIndex] = oneLow;
								destData[index + xIndex + 1] = oneHigh;
							}
							xIndex += srcXImage.bits_per_pixel / 8;
						}
						index += srcXImage.bytes_per_line;
					}
					break;
				case 24:
				case 32:
					index = 0;
					/* Get masks */
					visual = new Visual();
					OS.memmove(visual, visualPtr, Visual.sizeof);
					redMask = visual.red_mask;
					greenMask = visual.green_mask;
					blueMask = visual.blue_mask;
					/* Calculate mask shifts */
					shift = new int[1];
					getOffsetForMask(srcXImage.bits_per_pixel, redMask, srcXImage.byte_order, shift);
					rShift = shift[0];
					getOffsetForMask(srcXImage.bits_per_pixel, greenMask, srcXImage.byte_order, shift);
					gShift = shift[0];
					getOffsetForMask(srcXImage.bits_per_pixel, blueMask, srcXImage.byte_order, shift);
					bShift = shift[0];
					byte zeroR = (byte)zeroColor.getRed();
					byte zeroG = (byte)zeroColor.getGreen();
					byte zeroB = (byte)zeroColor.getBlue();
					byte oneR = (byte)oneColor.getRed();
					byte oneG = (byte)oneColor.getGreen();
					byte oneB = (byte)oneColor.getBlue();
					for (int y = 0; y < srcXImage.height; y++) {
						int xIndex = 0;
						for (int x = 0; x < srcXImage.width; x++) {
							r = srcData[index + xIndex + rShift] & 0xFF;
							g = srcData[index + xIndex + gShift] & 0xFF;
							b = srcData[index + xIndex + bShift] & 0xFF;
							/* See if the rgb maps to 0 or 1 */
							if ((r * r + g * g + b * b) < 98304) {
								/* Map down to 0 */
								destData[index + xIndex + rShift] = zeroR;
								destData[index + xIndex + gShift] = zeroG;
								destData[index + xIndex + bShift] = zeroB;
							} else {
								/* Map up to 1 */
								destData[index + xIndex + rShift] = oneR;
								destData[index + xIndex + gShift] = oneG;
								destData[index + xIndex + bShift] = oneB;
							}
							xIndex += destXImage.bits_per_pixel / 8;
						}
						index += srcXImage.bytes_per_line;
					}
					break;
				default:
					SWT.error(SWT.ERROR_INVALID_IMAGE);
			}
			OS.memmove(destXImage.data, destData, destData.length);
			gc = OS.XCreateGC(xDisplay, destPixmap, 0, null);
			OS.XPutImage(xDisplay, destPixmap, gc, destXImagePtr, 0, 0, 0, 0, width, height);
			OS.XDestroyImage(destXImagePtr);
			OS.XDestroyImage(srcXImagePtr);
			OS.XFreeGC(xDisplay, gc);
			alpha = srcImage.alpha;
			if (srcImage.alphaData != null) {
				alphaData = new byte[srcImage.alphaData.length];
				System.arraycopy(srcImage.alphaData, 0, alphaData, 0, alphaData.length);
			}
			createAlphaMask(width, height);
			this.pixmap = destPixmap;
			break;
		case SWT.IMAGE_GRAY:
			ImageData data = srcImage.getImageData();
			PaletteData palette = data.palette;
			ImageData newData = data;
			if (!palette.isDirect) {
				/* Convert the palette entries to gray. */
				RGB [] rgbs = palette.getRGBs();
				for (int i=0; i<rgbs.length; i++) {
					if (data.transparentPixel != i) {
						RGB color = rgbs [i];
						int red = color.red;
						int green = color.green;
						int blue = color.blue;
						int intensity = (red+red+green+green+green+green+green+blue) >> 3;
						color.red = color.green = color.blue = intensity;
					}
				}
				newData.palette = new PaletteData(rgbs);
			} else {
				/* Create a 8 bit depth image data with a gray palette. */
				RGB[] rgbs = new RGB[256];
				for (int i=0; i<rgbs.length; i++) {
					rgbs[i] = new RGB(i, i, i);
				}
				newData = new ImageData(width, height, 8, new PaletteData(rgbs));
				newData.alpha = data.alpha;
				newData.alphaData = data.alphaData;
				newData.maskData = data.maskData;
				newData.maskPad = data.maskPad;
				if (data.transparentPixel != -1) newData.transparentPixel = 254; 

				/* Convert the pixels. */
				int[] scanline = new int[width];
				int redMask = palette.redMask;
				int greenMask = palette.greenMask;
				int blueMask = palette.blueMask;
				int redShift = palette.redShift;
				int greenShift = palette.greenShift;
				int blueShift = palette.blueShift;
				for (int y=0; y<height; y++) {
					int offset = y * newData.bytesPerLine;
					data.getPixels(0, y, width, scanline, 0);
					for (int x=0; x<width; x++) {
						int pixel = scanline[x];
						if (pixel != data.transparentPixel) {
							int red = pixel & redMask;
							red = (redShift < 0) ? red >>> -redShift : red << redShift;
							int green = pixel & greenMask;
							green = (greenShift < 0) ? green >>> -greenShift : green << greenShift;
							int blue = pixel & blueMask;
							blue = (blueShift < 0) ? blue >>> -blueShift : blue << blueShift;
							int intensity = (red+red+green+green+green+green+green+blue) >> 3;
							if (newData.transparentPixel == intensity) intensity = 255;
							newData.data[offset] = (byte)intensity;
						} else {
							newData.data[offset] = (byte)254;
						}
						offset++;
					}
				}
			}
			init (newData);
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	init();
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
 */
public Image(Device device, Rectangle bounds) {
	super(device);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(bounds.width, bounds.height);
	init();
}
/**
 * Constructs an instance of this class from the given
 * <code>ImageData</code>.
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
 */
public Image(Device device, ImageData image) {
	super(device);
	init(image);
	init();
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
 */
public Image(Device device, ImageData source, ImageData mask) {
	super(device);
	if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (mask == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (source.width != mask.width || source.height != mask.height) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	mask = ImageData.convertMask(mask);
	ImageData image = new ImageData(source.width, source.height, source.depth, source.palette, source.scanlinePad, source.data);
	image.maskPad = mask.scanlinePad;
	image.maskData = mask.data;
	init(image);
	init();
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
 */
public Image(Device device, InputStream stream) {
	super(device);
	init(new ImageData(stream));
	init();
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
 */
public Image(Device device, String filename) {
	super(device);
	init(new ImageData(filename));
	init();
}
void createAlphaMask(int width, int height) {
	if (device.useXRender && (alpha != -1 || alphaData != null)) {
		int xDisplay = device.xDisplay;
		int drawable = OS.XDefaultRootWindow(xDisplay);
		mask = OS.XCreatePixmap(xDisplay, drawable, alpha != -1 ? 1 : width, alpha != -1 ? 1 : height, 8);
		if (mask == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		int gc = OS.XCreateGC(xDisplay, mask, 0, null);
		if (alpha != -1) {
			OS.XSetForeground(xDisplay, gc, (alpha & 0xFF) << 8 | (alpha & 0xFF));
			OS.XFillRectangle(xDisplay, mask, gc, 0, 0, 1, 1);
		} else {
			int imagePtr = OS.XGetImage(xDisplay, mask, 0, 0, width, height, OS.AllPlanes, OS.ZPixmap);
			XImage xImage = new XImage();
			OS.memmove(xImage, imagePtr, XImage.sizeof);
			if (xImage.bytes_per_line == width) {
				OS.memmove(xImage.data, alphaData, alphaData.length);
			} else {
				byte[] line = new byte[xImage.bytes_per_line];
				for (int y = 0; y < height; y++) {
					System.arraycopy(alphaData, width * y, line, 0, width);
					OS.memmove(xImage.data + (xImage.bytes_per_line * y), line, xImage.bytes_per_line);
				}
			}
			OS.XPutImage(xDisplay, mask, gc, imagePtr, 0, 0, 0, 0, width, height);
			OS.XDestroyImage(imagePtr);
		}			
		OS.XFreeGC(xDisplay, gc);
	}
}
/**
 * Create the receiver's mask if necessary.
 */
void createMask() {
	if (mask != 0) return;
	int xDisplay = device.xDisplay;
	int drawable = OS.XDefaultRootWindow(xDisplay);
	int screenDepth = OS.XDefaultDepthOfScreen(OS.XDefaultScreenOfDisplay(xDisplay));
	int visual = OS.XDefaultVisual(xDisplay, OS.XDefaultScreen(xDisplay));
	ImageData maskImage = getImageData().getTransparencyMask();
	int maskPixmap = OS.XCreatePixmap(xDisplay, drawable, maskImage.width, maskImage.height, 1);
	XColor[] xcolors = device.xcolors;
	int gc = OS.XCreateGC(xDisplay, maskPixmap, 0, null);
	Image.putImage(maskImage, 0, 0, maskImage.width, maskImage.height, 0, 0, maskImage.width, maskImage.height, xDisplay, visual, screenDepth, xcolors, null, true, maskPixmap, gc);
	OS.XFreeGC(xDisplay, gc);
	this.mask = maskPixmap;
}
void createSurface() {
	if (surface != 0) return;
	int [] unused = new int [1];  int [] width = new int [1];  int [] height = new int [1];
 	OS.XGetGeometry (device.xDisplay, pixmap, unused, unused, unused, width, height, unused, unused);
	int xDisplay = device.xDisplay;
	int xDrawable = pixmap;
	int xVisual = OS.XDefaultVisual(xDisplay, OS.XDefaultScreen(xDisplay));
	surface = Cairo.cairo_xlib_surface_create(xDisplay, xDrawable, xVisual, width[0], height[0]);
}
void destroy() {
	if (memGC != null) memGC.dispose();
	int xDisplay = device.xDisplay;
	if (pixmap != 0) OS.XFreePixmap (xDisplay, pixmap);
	if (mask != 0) OS.XFreePixmap (xDisplay, mask);
	if (surface != 0) Cairo.cairo_surface_destroy(surface);
	surface = pixmap = mask = 0;
	memGC = null;
}
/**
 * Destroy the receiver's mask if it exists.
 */
void destroyMask() {
	if (mask == 0) return;
	OS.XFreePixmap (device.xDisplay, mask);
	mask = 0;
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
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Image)) return false;
	Image image = (Image)object;
	return device == image.device && pixmap == image.pixmap;
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
	if (transparentPixel == -1) return null;
	XColor xColor = new XColor();
	xColor.pixel = transparentPixel;
	int xDisplay = device.xDisplay;
	int colormap = OS.XDefaultColormap(xDisplay, OS.XDefaultScreen(xDisplay));
	OS.XQueryColor(xDisplay, colormap, xColor);
	return Color.motif_new(device, xColor);
}
/**
 * Returns the bounds of the receiver. The rectangle will always
 * have x and y values of 0, and the width and height of the
 * image.
 *
 * @return a rectangle specifying the image's bounds
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or an icon</li>
 * </ul>
 */
public Rectangle getBounds () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width != -1 && height != -1) {
		return new Rectangle(0, 0, width, height);
	}
	int [] unused = new int [1];  int [] w = new int [1];  int [] h = new int [1];
 	OS.XGetGeometry (device.xDisplay, pixmap, unused, unused, unused, w, h, unused, unused);
	return new Rectangle(0, 0, width = w [0], height = h [0]);
}
/**
 * Returns an <code>ImageData</code> based on the receiver
 * Modifications made to this <code>ImageData</code> will not
 * affect the Image.
 *
 * @return an <code>ImageData</code> containing the image's data and attributes
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or an icon</li>
 * </ul>
 *
 * @see ImageData
 */
public ImageData getImageData() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	Rectangle srcBounds = getBounds();
	int width = srcBounds.width;
	int height = srcBounds.height;
	int xDisplay = device.xDisplay;
	int xSrcImagePtr = OS.XGetImage(xDisplay, pixmap, 0, 0, width, height, OS.AllPlanes, OS.ZPixmap);
	if (xSrcImagePtr == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	XImage xSrcImage = new XImage();
	OS.memmove(xSrcImage, xSrcImagePtr, XImage.sizeof);
	
	/* Get the data and palette of the source image. */
	PaletteData palette = null;
	int length = xSrcImage.bytes_per_line * xSrcImage.height;
	byte[] srcData = new byte[length];
	OS.memmove(srcData, xSrcImage.data, length);
	switch (xSrcImage.bits_per_pixel) {
		case 1:
			palette = new PaletteData(new RGB[] {
				new RGB(0, 0, 0),
				new RGB(255, 255, 255)
			});
			break;
		case 4:
			/*
			* We currently don't run on a 4-bit server, so 4-bit images
			* should not exist.
			*/
			SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
		case 8:
			/* Normalize the pixels in the source image data (by making the 
			* pixel values sequential starting at pixel 0). Reserve normalized 
			* pixel 0 so that it maps to real pixel 0. This assumes pixel 0 is 
			* always used in the image.
			*/
			byte[] normPixel = new byte[256];
			for (int index = 0; index < normPixel.length; index++) {
				normPixel[index] = 0;
			}
			int numPixels = 1;
			int index = 0;
			for (int y = 0; y < xSrcImage.height; y++) {
				for (int x = 0; x < xSrcImage.bytes_per_line; x++) {
					int srcPixel = srcData[index + x] & 0xFF;
					if (srcPixel != 0 && normPixel[srcPixel] == 0) {
						normPixel[srcPixel] = (byte)numPixels++;
					}
					srcData[index + x] = normPixel[srcPixel];
				}
				index += xSrcImage.bytes_per_line;
			}
			
			/* Create a palette with only the RGB values used in the image. */
			int colormap = OS.XDefaultColormap(xDisplay, OS.XDefaultScreen(xDisplay));
			RGB[] rgbs = new RGB[numPixels];
			XColor color = new XColor();
			for (int srcPixel = 0; srcPixel < normPixel.length; srcPixel++) {
				/* If the pixel value was used in the image, get its RGB values. */
				if (srcPixel == 0 || normPixel[srcPixel] != 0) {
					color.pixel = srcPixel;
					OS.XQueryColor(xDisplay, colormap, color);
					int rgbIndex = normPixel[srcPixel] & 0xFF;
					rgbs[rgbIndex] = new RGB((color.red >> 8) & 0xFF, (color.green >> 8) & 0xFF, (color.blue >> 8) & 0xFF);
				}
			}
			palette = new PaletteData(rgbs);
			break;
		case 16:
			/* Byte swap the data if necessary */
			if (xSrcImage.byte_order == OS.MSBFirst) {
				for (int i = 0; i < srcData.length; i += 2) {
					byte b = srcData[i];
					srcData[i] = srcData[i+1];
					srcData[i+1] = b;
				}
			}
			break;
		case 24:
			break;
		case 32:
			/* Byte swap the data if necessary */
			if (xSrcImage.byte_order == OS.LSBFirst) {
				for (int i = 0; i < srcData.length; i += 4) {
					byte b = srcData[i];
					srcData[i] = srcData[i+3];
					srcData[i+3] = b;
					b = srcData[i+1];
					srcData[i+1] = srcData[i+2];
					srcData[i+2] = b;
				}
			}
			break;
		default:
			SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
	}
	if (palette == null) {
		/*
		* For some reason, the XImage does not have the mask information.
		* We must get it from the defualt visual.
		*/
		int visual = OS.XDefaultVisual(xDisplay, OS.XDefaultScreen(xDisplay));
		Visual v = new Visual();
		OS.memmove(v, visual, Visual.sizeof);
		palette = new PaletteData(v.red_mask, v.green_mask, v.blue_mask);
	}	
	ImageData data = new ImageData(width, height, xSrcImage.bits_per_pixel, palette, 4, srcData);
	if (transparentPixel == -1 && type == SWT.ICON && mask != 0) {
		/* Get the icon mask data */
		int xMaskPtr = OS.XGetImage(xDisplay, mask, 0, 0, width, height, OS.AllPlanes, OS.ZPixmap);
		if (xMaskPtr == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		XImage xMask = new XImage();
		OS.memmove(xMask, xMaskPtr, XImage.sizeof);
		byte[] maskData = new byte[xMask.bytes_per_line * xMask.height];
		OS.memmove(maskData, xMask.data, maskData.length);
		OS.XDestroyImage(xMaskPtr);
		int maskPad = xMask.bitmap_pad / 8;
		/* Make mask scanline pad equals to 2 */
		data.maskPad = 2;
		maskData = ImageData.convertPad(maskData, width, height, 1, maskPad, data.maskPad);
		/* Bit swap the mask data if necessary */
		if (xMask.bitmap_bit_order == OS.LSBFirst) {
			for (int i = 0; i < maskData.length; i++) {
				byte b = maskData[i];
				maskData[i] = (byte)(((b & 0x01) << 7) | ((b & 0x02) << 5) | 
					((b & 0x04) << 3) |	((b & 0x08) << 1) | ((b & 0x10) >> 1) | 
					((b & 0x20) >> 3) |	((b & 0x40) >> 5) | ((b & 0x80) >> 7));
			}
		}
		data.maskData = maskData;
	}
	data.transparentPixel = transparentPixel;
	data.alpha = alpha;
	if (alpha == -1 && alphaData != null) {
		data.alphaData = new byte[alphaData.length];
		System.arraycopy(alphaData, 0, data.alphaData, 0, alphaData.length);
	}
	OS.XDestroyImage(xSrcImagePtr);
	return data;
}
/**
 * Get the offset for the given mask.
 *
 * For 24 and 32-bit masks, the offset indicates which byte holds the
 *  data for the given mask (indexed from 0).
 *  For example, in 0x0000FF00, the byte offset is 1.
 *
 * For 16-bit masks, the offset indicates which bit holds the most significant
 *  data for the given mask (indexed from 1).
 *  For example, in 0x7E0, the bit offset is 11.
 *
 * The different semantics are necessary because 24- and 32-bit images
 * have their color components aligned on byte boundaries, and 16-bit images
 * do not.
 */
static boolean getOffsetForMask(int bitspp, int mask, int byteOrder, int[] poff) {
	if (bitspp % 8 != 0) {
		return false;
	}
	switch (mask) {
		/* 24-bit and 32-bit masks */
		case 0x000000FF:
			poff[0] = 0;
			break;
		case 0x0000FF00:
			poff[0] = 1;
			break;
		case 0x00FF0000:
			poff[0] = 2;
			break;
		case 0xFF000000:
			poff[0] = 3;
			break;
		/* 16-bit masks */
		case 0x001F:
			poff[0] = 5;
			break;
		case 0x03E0:
			poff[0] = 10;
			break;
		case 0x07E0:
			poff[0] = 11;
			break;
		case 0x7C00:
			poff[0] = 15;
			break;
		case 0xF800:
			poff[0] = 16;
			break;
		default:
			return false;
	}
	if (bitspp == 16) {
		return true;
	}
	if (poff[0] >= bitspp / 8) {
		return false;
	}
	if (byteOrder == OS.MSBFirst) {
		poff[0] = (bitspp/8 - 1) - poff[0];
	}
	return true;
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
public int hashCode () {
	return pixmap;
}
void init(int width, int height) {
	if (width <= 0 || height <= 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	/* Create the pixmap */
	this.type = SWT.BITMAP;
	int xDisplay = device.xDisplay;
	int screen = OS.XDefaultScreenOfDisplay(xDisplay);
	int depth = OS.XDefaultDepthOfScreen(screen);
	int screenNum = OS.XDefaultScreen(xDisplay);
	int drawable = OS.XDefaultRootWindow(xDisplay);
	int pixmap = OS.XCreatePixmap(xDisplay, drawable, width, height, depth);
	if (pixmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	/* Fill the bitmap with white */
	int xGC = OS.XCreateGC(xDisplay, drawable, 0, null);
	OS.XSetForeground(xDisplay, xGC, OS.XWhitePixel(xDisplay, screenNum));
	OS.XFillRectangle(xDisplay, pixmap, xGC, 0, 0, width, height);
	OS.XFreeGC(xDisplay, xGC);
	this.pixmap = pixmap;
}
void init(ImageData image) {
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int xDisplay = device.xDisplay;
	int drawable = OS.XDefaultRootWindow(xDisplay);
	int screenDepth = OS.XDefaultDepthOfScreen(OS.XDefaultScreenOfDisplay(xDisplay));
	int visual = OS.XDefaultVisual(xDisplay, OS.XDefaultScreen(xDisplay));
	int pixmap = OS.XCreatePixmap(xDisplay, drawable, image.width, image.height, screenDepth);
	if (pixmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int gc = OS.XCreateGC(xDisplay, pixmap, 0, null);
	int[] transPixel = null;
	if (image.transparentPixel != -1) transPixel = new int[]{image.transparentPixel};
	int error = putImage(image, 0, 0, image.width, image.height, 0, 0, image.width, image.height, xDisplay, visual, screenDepth, device.xcolors, transPixel, false, pixmap, gc);
	OS.XFreeGC(xDisplay, gc);
	if (error != 0) {
		OS.XFreePixmap (xDisplay, pixmap);
		SWT.error(error);
	}
	if (image.getTransparencyType() == SWT.TRANSPARENCY_MASK || image.transparentPixel != -1) {
		if (image.transparentPixel != -1) transparentPixel = transPixel[0];
		ImageData maskImage = image.getTransparencyMask();
		int mask = OS.XCreatePixmap(xDisplay, drawable, image.width, image.height, 1);
		gc = OS.XCreateGC(xDisplay, mask, 0, null);
		error = putImage(maskImage, 0, 0, maskImage.width, maskImage.height, 0, 0, maskImage.width, maskImage.height, xDisplay, visual, screenDepth, device.xcolors, null, true, mask, gc);
		OS.XFreeGC(xDisplay, gc);
		if (error != 0) {
			OS.XFreePixmap (xDisplay, pixmap);
			OS.XFreePixmap (xDisplay, mask);
			SWT.error(error);
		}
		this.mask = mask;
		if (image.getTransparencyType() == SWT.TRANSPARENCY_MASK) {
			this.type = SWT.ICON;
		} else {
			this.type = SWT.BITMAP;
		}
	} else {
		this.type = SWT.BITMAP;
		this.mask = 0;
		this.alpha = image.alpha;
		if (image.alpha == -1 && image.alphaData != null) {
			this.alphaData = new byte[image.alphaData.length];
			System.arraycopy(image.alphaData, 0, this.alphaData, 0, alphaData.length);
		}
		createAlphaMask(image.width, image.height);
	}
	this.pixmap = pixmap;
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
 */
public int internal_new_GC (GCData data) {
	if (pixmap == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (type != SWT.BITMAP || memGC != null) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	int xDisplay = device.xDisplay;
	int xGC = OS.XCreateGC (xDisplay, pixmap, 0, null);
	if (xGC == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	if (data != null) {
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		if ((data.style & mask) == 0) {
			data.style |= SWT.LEFT_TO_RIGHT; 
		}
		data.device = device;
		data.display = xDisplay;
		data.drawable = pixmap;
		data.background = device.COLOR_WHITE.handle;
		data.foreground = device.COLOR_BLACK.handle;
		data.font = device.systemFont;
		data.colormap = OS.XDefaultColormap (xDisplay, OS.XDefaultScreen (xDisplay));
		data.image = this;
	}
	return xGC;
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
 */
public void internal_dispose_GC (int gc, GCData data) {
	int xDisplay = 0;
	if (data != null) xDisplay = data.display;
	if (xDisplay == 0 && device != null) xDisplay = device.xDisplay;
	if (xDisplay == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	OS.XFreeGC(xDisplay, gc);
}
/**
 * Returns <code>true</code> if the image has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the image.
 * When an image has been disposed, it is an error to
 * invoke any other method using the image.
 *
 * @return <code>true</code> when the image is disposed and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return pixmap == 0;
}
public static Image motif_new(Device device, int type, int pixmap, int mask) {
	Image image = new Image(device);
	image.type = type;
	image.pixmap = pixmap;
	image.mask = mask;
	return image;
}
/**
 * Put a device-independent image of any depth into a drawable of any depth, 
 * stretching if necessary.
 */
static int putImage(ImageData image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight, int display, int visual, int screenDepth, XColor[] xcolors, int[] transparentPixel, boolean isMask, int drawable, int gc) {
	PaletteData palette = image.palette;
	if (!(((image.depth == 1 || image.depth == 2 || image.depth == 4 || image.depth == 8) && !palette.isDirect) ||
		((image.depth == 8) || (image.depth == 16 || image.depth == 24 || image.depth == 32) && palette.isDirect)))
			return SWT.ERROR_UNSUPPORTED_DEPTH;

	boolean flipX = destWidth < 0;
	boolean flipY = destHeight < 0;
	if (flipX) {
		destWidth = -destWidth;
		destX = destX - destWidth;
	}
	if (flipY) {
		destHeight = -destHeight;
		destY = destY - destHeight;
	}
	byte[] srcReds = null, srcGreens = null, srcBlues = null;
	if (!palette.isDirect) {
		RGB[] rgbs = palette.getRGBs();
		int length = rgbs.length;
		srcReds = new byte[length];
		srcGreens = new byte[length];
		srcBlues = new byte[length];
		for (int i = 0; i < rgbs.length; i++) {
			RGB rgb = rgbs[i];
			if (rgb == null) continue;
			srcReds[i] = (byte)rgb.red;
			srcGreens[i] = (byte)rgb.green;
			srcBlues[i] = (byte)rgb.blue;
		}
	}
	byte[] destReds = null, destGreens = null, destBlues = null;
	int destRedMask = 0, destGreenMask = 0, destBlueMask = 0;
	final boolean screenDirect;
	if (screenDepth <= 8) {
		if (xcolors == null) return SWT.ERROR_UNSUPPORTED_DEPTH;
		destReds = new byte[xcolors.length];
		destGreens = new byte[xcolors.length];
		destBlues = new byte[xcolors.length];
		for (int i = 0; i < xcolors.length; i++) {
			XColor color = xcolors[i];
			if (color == null) continue;
			destReds[i] = (byte)((color.red >> 8) & 0xFF);
			destGreens[i] = (byte)((color.green >> 8) & 0xFF);
			destBlues[i] = (byte)((color.blue >> 8) & 0xFF);
		}
		screenDirect = false;
	} else {
		Visual xVisual = new Visual();
		OS.memmove(xVisual, visual, Visual.sizeof);
		destRedMask = xVisual.red_mask;
		destGreenMask = xVisual.green_mask;
		destBlueMask = xVisual.blue_mask;
		screenDirect = true;
	}
	if (transparentPixel != null) {
		int transRed = 0, transGreen = 0, transBlue = 0;
		if (palette.isDirect) {
			RGB rgb = palette.getRGB(transparentPixel[0]);
			transRed = rgb.red;
			transGreen = rgb.green;
			transBlue = rgb.blue;
		} else {
			RGB[] rgbs = palette.getRGBs();
			if (transparentPixel[0] < rgbs.length) {
				RGB rgb = rgbs[transparentPixel[0]];
				transRed = rgb.red;
				transGreen = rgb.green;
				transBlue = rgb.blue;				
			}
		}
		transparentPixel[0] = ImageData.closestMatch(screenDepth, (byte)transRed, (byte)transGreen, (byte)transBlue,
			destRedMask, destGreenMask, destBlueMask, destReds, destGreens, destBlues);
	}

	/* Depth 1 */
	if (image.depth == 1) {
		int xImagePtr = OS.XCreateImage(display, visual, 1, OS.XYBitmap, 0, 0, destWidth, destHeight, image.scanlinePad * 8, 0);
		if (xImagePtr == 0) return SWT.ERROR_NO_HANDLES;
		XImage xImage = new XImage();
		OS.memmove(xImage, xImagePtr, XImage.sizeof);
		int bufSize = xImage.bytes_per_line * xImage.height;
		int bufPtr = OS.XtMalloc(bufSize);
		xImage.data = bufPtr;
		OS.memmove(xImagePtr, xImage, XImage.sizeof);
		byte[] buf = new byte[bufSize];
		ImageData.blit(ImageData.BLIT_SRC,
			image.data, image.depth, image.bytesPerLine, image.getByteOrder(), srcX, srcY, srcWidth, srcHeight, null, null, null,
			ImageData.ALPHA_OPAQUE, null, 0, srcX, srcY,
			buf, xImage.bits_per_pixel, xImage.bytes_per_line, xImage.bitmap_bit_order, 0, 0, destWidth, destHeight, null, null, null,
			flipX, flipY);
		OS.memmove(xImage.data, buf, bufSize);

		int foreground = 1, background = 0;
		if (!isMask) {
			foreground = 0;
			if (srcReds.length > 1) {
				foreground = ImageData.closestMatch(screenDepth, srcReds[1], srcGreens[1], srcBlues[1],
					destRedMask, destGreenMask, destBlueMask, destReds, destGreens, destBlues);
			}
			if (srcReds.length > 0) {
				background = ImageData.closestMatch(screenDepth, srcReds[0], srcGreens[0], srcBlues[0],
					destRedMask, destGreenMask, destBlueMask, destReds, destGreens, destBlues);
			}
		}
		XGCValues values = new XGCValues();
		OS.XGetGCValues(display, gc, OS.GCForeground | OS.GCBackground, values);
		OS.XSetForeground(display, gc, foreground);
		OS.XSetBackground(display, gc, background);
		OS.XPutImage(display, drawable, gc, xImagePtr, 0, 0, destX, destY, destWidth, destHeight);
		OS.XSetForeground(display, gc, values.foreground);
		OS.XSetBackground(display, gc, values.background);
		OS.XDestroyImage(xImagePtr);	
		return 0;
	}
	
	/* Depths other than 1 */
	int xImagePtr = OS.XCreateImage(display, visual, screenDepth, OS.ZPixmap, 0, 0, destWidth, destHeight, image.scanlinePad * 8, 0);
	if (xImagePtr == 0) return SWT.ERROR_NO_HANDLES;
	XImage xImage = new XImage();
	OS.memmove(xImage, xImagePtr, XImage.sizeof);
	int bufSize = xImage.bytes_per_line * xImage.height;
	int bufPtr = OS.XtMalloc(bufSize);
	xImage.data = bufPtr;
	OS.memmove(xImagePtr, xImage, XImage.sizeof);
	byte[] buf = new byte[bufSize];
	if (palette.isDirect) {
		if (screenDirect) {
			ImageData.blit(ImageData.BLIT_SRC,
				image.data, image.depth, image.bytesPerLine, image.getByteOrder(), srcX, srcY, srcWidth, srcHeight, palette.redMask, palette.greenMask, palette.blueMask,
				ImageData.ALPHA_OPAQUE, null, 0, srcX, srcY,
				buf, xImage.bits_per_pixel, xImage.bytes_per_line, xImage.byte_order, 0, 0, destWidth, destHeight, xImage.red_mask, xImage.green_mask, xImage.blue_mask,
				flipX, flipY);
		} else {
			ImageData.blit(ImageData.BLIT_SRC,
				image.data, image.depth, image.bytesPerLine, image.getByteOrder(), srcX, srcY, srcWidth, srcHeight, palette.redMask, palette.greenMask, palette.blueMask,
				ImageData.ALPHA_OPAQUE, null, 0, srcX, srcY,
				buf, xImage.bits_per_pixel, xImage.bytes_per_line, xImage.byte_order, 0, 0, destWidth, destHeight, destReds, destGreens, destBlues,
				flipX, flipY);
		}
	} else {
		if (screenDirect) {
			ImageData.blit(ImageData.BLIT_SRC,
				image.data, image.depth, image.bytesPerLine, image.getByteOrder(), srcX, srcY, srcWidth, srcHeight, srcReds, srcGreens, srcBlues,
				ImageData.ALPHA_OPAQUE, null, 0, srcX, srcY,
				buf, xImage.bits_per_pixel, xImage.bytes_per_line, xImage.byte_order, 0, 0, destWidth, destHeight, xImage.red_mask, xImage.green_mask, xImage.blue_mask,
				flipX, flipY);
		} else {
			ImageData.blit(ImageData.BLIT_SRC,
				image.data, image.depth, image.bytesPerLine, image.getByteOrder(), srcX, srcY, srcWidth, srcHeight, srcReds, srcGreens, srcBlues,
				ImageData.ALPHA_OPAQUE, null, 0, srcX, srcY,
				buf, xImage.bits_per_pixel, xImage.bytes_per_line, xImage.byte_order, 0, 0, destWidth, destHeight, destReds, destGreens, destBlues,
				flipX, flipY);
		}
	}
	OS.memmove(xImage.data, buf, bufSize);
	OS.XPutImage(display, drawable, gc, xImagePtr, 0, 0, destX, destY, destWidth, destHeight);
	OS.XDestroyImage(xImagePtr);
	return 0;
}
/**
 * Sets the color to which to map the transparent pixel.
 * <p>
 * There are certain uses of <code>Images</code> that do not support
 * transparency (for example, setting an image into a button or label).
 * In these cases, it may be desired to simulate transparency by using
 * the background color of the widget to paint the transparent pixels
 * of the image. This method specifies the color that will be used in
 * these cases. For example:
 * <pre>
 *    Button b = new Button();
 *    image.setBackground(b.getBackground());
 *    b.setImage(image);
 * </pre>
 * </p><p>
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
	if (transparentPixel == -1) return;
	/* Generate the mask if necessary. */
	if (mask == 0) createMask();
	Rectangle bounds = getBounds();
	int[] unused = new int[1];
	int[] depth = new int[1];
	int xDisplay = device.xDisplay;
 	OS.XGetGeometry(xDisplay, pixmap, unused, unused, unused, unused, unused, unused, depth);
	int drawable = OS.XDefaultRootWindow(xDisplay);
	int tempPixmap = OS.XCreatePixmap(xDisplay, drawable, bounds.width, bounds.height, depth[0]);
	int xGC = OS.XCreateGC(xDisplay, tempPixmap, 0, null);
	OS.XSetForeground(xDisplay, xGC, color.handle.pixel);
	OS.XFillRectangle(xDisplay, tempPixmap, xGC, 0, 0, bounds.width, bounds.height);
	OS.XSetClipMask(xDisplay, xGC, mask);
	OS.XCopyArea(xDisplay, pixmap, tempPixmap, xGC, 0, 0, bounds.width, bounds.height, 0, 0);
	OS.XSetClipMask(xDisplay, xGC, OS.None);
	OS.XCopyArea(xDisplay, tempPixmap, pixmap, xGC, 0, 0, bounds.width, bounds.height, 0, 0);
	OS.XFreePixmap(xDisplay, tempPixmap);
	OS.XFreeGC(xDisplay, xGC);
	/* Destroy the receiver's mask if the there is a GC created on it */
	if (memGC != null) destroyMask();
}
/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "Image {*DISPOSED*}";
	return "Image {" + pixmap + "}";
}
}
