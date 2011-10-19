/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.gtk.*;
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
	 * 
	 * @noreference This field is not intended to be referenced by clients.
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
	 * 
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public int /*long*/ pixmap;
	
	/**
	 * The handle to the OS mask resource.
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
	public int /*long*/ mask;

	public int /*long*/ surface, surfaceData;
	
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
	device = this.device;
	this.type = srcImage.type;

	/* Get source image size */
	int[] w = new int[1], h = new int[1];
 	OS.gdk_drawable_get_size(srcImage.pixmap, w, h);
 	int width = w[0];
 	int height = h[0];
 	
 	/* Copy the mask */
	if ((srcImage.type == SWT.ICON && srcImage.mask != 0) || srcImage.transparentPixel != -1) {
		/* Generate the mask if necessary. */
		if (srcImage.transparentPixel != -1) srcImage.createMask();
		int /*long*/ mask = OS.gdk_pixmap_new(0, width, height, 1);
		if (mask == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		int /*long*/ gdkGC = OS.gdk_gc_new(mask);
		if (gdkGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		OS.gdk_draw_drawable(mask, gdkGC, srcImage.mask, 0, 0, 0, 0, width, height);
		OS.g_object_unref(gdkGC);
		this.mask = mask;
		/* Destroy the image mask if the there is a GC created on the image */
		if (srcImage.transparentPixel != -1 && srcImage.memGC != null) srcImage.destroyMask();
	}

	/* Copy transparent pixel and alpha data */
	if (flag != SWT.IMAGE_DISABLE) transparentPixel = srcImage.transparentPixel;
	alpha = srcImage.alpha;
	if (srcImage.alphaData != null) {
		alphaData = new byte[srcImage.alphaData.length];
		System.arraycopy(srcImage.alphaData, 0, alphaData, 0, alphaData.length);
	}
	createAlphaMask(width, height);

	/* Create the new pixmap */
	int /*long*/ pixmap = OS.gdk_pixmap_new (OS.GDK_ROOT_PARENT(), width, height, -1);
	if (pixmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int /*long*/ gdkGC = OS.gdk_gc_new(pixmap);
	if (gdkGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	this.pixmap = pixmap;
	
	if (flag == SWT.IMAGE_COPY) {
		OS.gdk_draw_drawable(pixmap, gdkGC, srcImage.pixmap, 0, 0, 0, 0, width, height);
		OS.g_object_unref(gdkGC);
	} else {
		
		/* Retrieve the source pixmap data */
		int /*long*/ pixbuf = OS.gdk_pixbuf_new(OS.GDK_COLORSPACE_RGB, false, 8, width, height);
		if (pixbuf == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		int /*long*/ colormap = OS.gdk_colormap_get_system();
		OS.gdk_pixbuf_get_from_drawable(pixbuf, srcImage.pixmap, colormap, 0, 0, 0, 0, width, height);
		int stride = OS.gdk_pixbuf_get_rowstride(pixbuf);
		int /*long*/ pixels = OS.gdk_pixbuf_get_pixels(pixbuf);
	
		/* Apply transformation */
		switch (flag) {
			case SWT.IMAGE_DISABLE: {
				Color zeroColor = device.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
				RGB zeroRGB = zeroColor.getRGB();
				byte zeroRed = (byte)zeroRGB.red;
				byte zeroGreen = (byte)zeroRGB.green;
				byte zeroBlue = (byte)zeroRGB.blue;
				Color oneColor = device.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
				RGB oneRGB = oneColor.getRGB();
				byte oneRed = (byte)oneRGB.red;
				byte oneGreen = (byte)oneRGB.green;
				byte oneBlue = (byte)oneRGB.blue;
				byte[] line = new byte[stride];
				for (int y=0; y<height; y++) {
					OS.memmove(line, pixels + (y * stride), stride);
					for (int x=0; x<width; x++) {
						int offset = x*3;
						int red = line[offset] & 0xFF;
						int green = line[offset+1] & 0xFF;
						int blue = line[offset+2] & 0xFF;
						int intensity = red * red + green * green + blue * blue;
						if (intensity < 98304) {
							line[offset] = zeroRed;
							line[offset+1] = zeroGreen;
							line[offset+2] = zeroBlue;
						} else {
							line[offset] = oneRed;
							line[offset+1] = oneGreen;
							line[offset+2] = oneBlue;
						}
					}
					OS.memmove(pixels + (y * stride), line, stride);
				}
				break;
			}
			case SWT.IMAGE_GRAY: {			
				byte[] line = new byte[stride];
				for (int y=0; y<height; y++) {
					OS.memmove(line, pixels + (y * stride), stride);
					for (int x=0; x<width; x++) {
						int offset = x*3;
						int red = line[offset] & 0xFF;
						int green = line[offset+1] & 0xFF;
						int blue = line[offset+2] & 0xFF;
						byte intensity = (byte)((red+red+green+green+green+green+green+blue) >> 3);
						line[offset] = line[offset+1] = line[offset+2] = intensity;
					}
					OS.memmove(pixels + (y * stride), line, stride);
				}
				break;
			}
		}
	
		/* Copy data back to destination pixmap */
		OS.gdk_pixbuf_render_to_drawable(pixbuf, pixmap, gdkGC, 0, 0, 0, 0, width, height, OS.GDK_RGB_DITHER_NORMAL, 0, 0);
		
		/* Free resources */
		OS.g_object_unref(pixbuf);
		OS.g_object_unref(gdkGC);
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
public Image(Device device, ImageData data) {
	super(device);
	init(data);
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
	mask = ImageData.convertMask (mask);
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
	if (filename == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	initNative(filename);
	if (this.pixmap == 0 && this.surface == 0) init(new ImageData(filename));
	init();
}

void initNative(String filename) {
	try {
		int length = filename.length ();
		char [] chars = new char [length];
		filename.getChars (0, length, chars, 0);
		byte [] buffer = Converter.wcsToMbcs(null, chars, true);
		int /*long*/ pixbuf = OS.gdk_pixbuf_new_from_file(buffer, null);
		if (pixbuf != 0) {
			try {
				createFromPixbuf (SWT.BITMAP, pixbuf);
			} finally {
				if (pixbuf != 0) OS.g_object_unref (pixbuf);
			}
		}
	} catch (SWTException e) {}
}

void createAlphaMask (int width, int height) {
	if (device.useXRender && (alpha != -1 || alphaData != null)) {
		mask = OS.gdk_pixmap_new(0, alpha != -1 ? 1 : width, alpha != -1 ? 1 : height, 8);
		if (mask == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		int /*long*/ gc = OS.gdk_gc_new(mask);
		if (alpha != -1) {
			GdkColor color = new GdkColor();
			color.pixel = (alpha & 0xFF) << 8 | (alpha & 0xFF);
			OS.gdk_gc_set_foreground(gc, color);
			OS.gdk_draw_rectangle(mask, gc, 1, 0, 0, 1, 1);
		} else {
			int /*long*/ imagePtr = OS.gdk_drawable_get_image(mask, 0, 0, width, height);
			if (imagePtr == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			GdkImage gdkImage = new GdkImage();
			OS.memmove(gdkImage, imagePtr);
			if (gdkImage.bpl == width) {
				OS.memmove(gdkImage.mem, alphaData, alphaData.length);
			} else {
				byte[] line = new byte[gdkImage.bpl];
				for (int y = 0; y < height; y++) {
					System.arraycopy(alphaData, width * y, line, 0, width);
					OS.memmove(gdkImage.mem + (gdkImage.bpl * y), line, gdkImage.bpl);
				}
			}
			OS.gdk_draw_image(mask, gc, imagePtr, 0, 0, 0, 0, width, height);
			OS.g_object_unref(imagePtr);
		}		
		OS.g_object_unref(gc);
	}
}

void createFromPixbuf(int type, int /*long*/ pixbuf) {
	this.type = type;
	boolean hasAlpha = OS.gdk_pixbuf_get_has_alpha(pixbuf);
	if (OS.USE_CAIRO_SURFACE) {
		int width = this.width = OS.gdk_pixbuf_get_width(pixbuf);
		int height = this.height = OS.gdk_pixbuf_get_height(pixbuf);
		int stride = OS.gdk_pixbuf_get_rowstride(pixbuf);
		int /*long*/ pixels = OS.gdk_pixbuf_get_pixels(pixbuf);
		int format = hasAlpha ? Cairo.CAIRO_FORMAT_ARGB32 : Cairo.CAIRO_FORMAT_RGB24;
		int cairoStride = Cairo.cairo_format_stride_for_width(format, width);
		int /*long*/ data = surfaceData = OS.g_malloc(cairoStride * height);
		if (surfaceData == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		surface = Cairo.cairo_image_surface_create_for_data(surfaceData, format, width, height, cairoStride);
		if (surface == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		byte[] line = new byte[stride];
		int oa = 0, or = 0, og = 0, ob = 0;
		if (OS.BIG_ENDIAN) {
			oa = 0; or = 1; og = 2; ob = 3;
		} else {
			oa = 3; or = 2; og = 1; ob = 0;
		}
		if (hasAlpha) {
			byte[] cairoLine = new byte[cairoStride];
			alphaData = new byte[width * height];
			for (int y = 0, alphaOffset = 0; y < height; y++) {
				OS.memmove(line, pixels + (y * stride), stride);
				for (int x = 0, offset = 0; x < width; x++, offset += 4) {
					int a = line[offset + 3] & 0xFF;
					int r = ((line[offset + 0] & 0xFF) * a) + 128;
					r = (r + (r >> 8)) >> 8;
					int g = ((line[offset + 1] & 0xFF) * a) + 128;
					g = (g + (g >> 8)) >> 8;
					int b = ((line[offset + 2] & 0xFF) * a) + 128;
					b = (b + (b >> 8)) >> 8;
					cairoLine[offset + oa] = (byte)a;
					cairoLine[offset + or] = (byte)r;
					cairoLine[offset + og] = (byte)g;
					cairoLine[offset + ob] = (byte)b;
					alphaData[alphaOffset++] = (byte)a;
				}
				OS.memmove(data + (y * cairoStride), cairoLine, cairoStride);
			}
		} else {
			byte[] cairoLine = new byte[cairoStride];
			for (int y = 0; y < height; y++) {
				OS.memmove(line, pixels + (y * stride), stride);
				for (int x = 0, offset = 0, cairoOffset = 0; x < width; x++, offset += 3, cairoOffset += 4) {
					int r = line[offset + 0] & 0xFF;
					int g = line[offset + 1] & 0xFF;
					int b = line[offset + 2] & 0xFF;
					cairoLine[cairoOffset + oa] = (byte)0xFF;
					cairoLine[cairoOffset + or] = (byte)r;
					cairoLine[cairoOffset + og] = (byte)g;
					cairoLine[cairoOffset + ob] = (byte)b;
				}
				OS.memmove(data + (y * cairoStride), cairoLine, cairoStride);
			}
		}
	} else {
		if (hasAlpha) {
			/*
			* Bug in GTK. Depending on the image (seems to affect images that have
			* some degree of transparency all over the image), gdk_pixbuff_render_pixmap_and_mask()
			* will return a corrupt pixmap. To avoid this, read in and store the alpha channel data
			* for the image and then set it to 0xFF to prevent any possible corruption from 
			* gdk_pixbuff_render_pixmap_and_mask(). 
			*/
			int width = OS.gdk_pixbuf_get_width(pixbuf);
			int height = OS.gdk_pixbuf_get_height(pixbuf);
			int stride = OS.gdk_pixbuf_get_rowstride(pixbuf);
			int /*long*/ pixels = OS.gdk_pixbuf_get_pixels(pixbuf);
			byte[] line = new byte[stride];
			alphaData = new byte[width * height];
			for (int y = 0; y < height; y++) {
				OS.memmove(line, pixels + (y * stride), stride);
				for (int x = 0; x < width; x++) {
					alphaData[y*width+x] = line[x*4 + 3];
					line[x*4 + 3] = (byte) 0xFF;
				}
				OS.memmove(pixels + (y * stride), line, stride);
			}
			createAlphaMask(width, height);
		}
		int /*long*/ [] pixmap_return = new int /*long*/ [1];
		OS.gdk_pixbuf_render_pixmap_and_mask(pixbuf, pixmap_return, null, 0);
		this.pixmap = pixmap_return[0];
		if (pixmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	}
}

/**
 * Create the receiver's mask if necessary.
 */
void createMask() {
	if (OS.USE_CAIRO_SURFACE) {
		int width = this.width;
		int height = this.height;
		int stride = Cairo.cairo_format_stride_for_width(Cairo.CAIRO_FORMAT_ARGB32, width);
		byte[] srcData = new byte[stride * height];
		int oa, or, og, ob, tr, tg, tb;
		if (OS.BIG_ENDIAN) {
			oa = 0; or = 1; og = 2; ob = 3;
			tr = (transparentPixel >> 24) & 0xFF;
			tg = (transparentPixel >> 16) & 0xFF;
			tb = (transparentPixel >> 8) & 0xFF;
		} else {
			oa = 3; or = 2; og = 1; ob = 0;
			tr = (transparentPixel >> 16) & 0xFF;
			tg = (transparentPixel >> 8) & 0xFF;
			tb = (transparentPixel >> 0) & 0xFF;
		}
		OS.memmove(srcData, this.surfaceData, srcData.length);
		int offset = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++, offset += 4) {
				int a = srcData[offset + oa] & 0xFF;
				int r = srcData[offset + or] & 0xFF;
				int g = srcData[offset + og] & 0xFF;
				int b = srcData[offset + ob] & 0xFF;
				if (r == tr && g == tg && b == tb) {
					a = r = g = b = 0;
				} else {
					a = 0xff;
				}
				srcData[offset + oa] = (byte)a;
				srcData[offset + or] = (byte)r;
				srcData[offset + og] = (byte)g;
				srcData[offset + ob] = (byte)b;
			}
		}
		OS.memmove(this.surfaceData, srcData, srcData.length);
		return;
	}
	if (mask != 0) return;
	mask = createMask(getImageData(), false);
	if (mask == 0) SWT.error(SWT.ERROR_NO_HANDLES);
}

int /*long*/ createMask(ImageData image, boolean copy) {
	ImageData mask = image.getTransparencyMask();
	byte[] data = mask.data;
	byte[] maskData = copy ? new byte[data.length] : data;
	for (int i = 0; i < maskData.length; i++) {
		byte s = data[i];
		maskData[i] = (byte)(((s & 0x80) >> 7) | ((s & 0x40) >> 5) |
			((s & 0x20) >> 3) | ((s & 0x10) >> 1) | ((s & 0x08) << 1) |
			((s & 0x04) << 3) | ((s & 0x02) << 5) |	((s & 0x01) << 7));
	}
	maskData = ImageData.convertPad(maskData, mask.width, mask.height, mask.depth, mask.scanlinePad, 1);
	return OS.gdk_bitmap_create_from_data(0, maskData, mask.width, mask.height);
}

void createSurface() {
	if (surface != 0) return;
	/* Generate the mask if necessary. */
	if (transparentPixel != -1) createMask();
	int[] w = new int[1], h = new int[1];
	OS.gdk_drawable_get_size(pixmap, w, h);
	int width = w[0], height = h[0];
	if (mask != 0 || alpha != -1 || alphaData != null) {
	 	int /*long*/ pixbuf = OS.gdk_pixbuf_new(OS.GDK_COLORSPACE_RGB, true, 8, width, height);
		if (pixbuf == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		int /*long*/ colormap = OS.gdk_colormap_get_system();
		OS.gdk_pixbuf_get_from_drawable(pixbuf, pixmap, colormap, 0, 0, 0, 0, width, height);
		int stride = OS.gdk_pixbuf_get_rowstride(pixbuf);
		int /*long*/ pixels = OS.gdk_pixbuf_get_pixels(pixbuf);		
		byte[] line = new byte[stride];
		int oa, or, og, ob;
		if (OS.BIG_ENDIAN) {
			oa = 0; or = 1; og = 2; ob = 3;
		} else {
			oa = 3; or = 2; og = 1; ob = 0;
		}
		if (mask != 0 && OS.gdk_drawable_get_depth(mask) == 1) {
			int /*long*/ maskPixbuf = OS.gdk_pixbuf_new(OS.GDK_COLORSPACE_RGB, false, 8, width, height);
			if (maskPixbuf == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			OS.gdk_pixbuf_get_from_drawable(maskPixbuf, mask, 0, 0, 0, 0, 0, width, height);
			int maskStride = OS.gdk_pixbuf_get_rowstride(maskPixbuf);
			int /*long*/ maskPixels = OS.gdk_pixbuf_get_pixels(maskPixbuf);
			byte[] maskLine = new byte[maskStride];
			int /*long*/ offset = pixels, maskOffset = maskPixels;
			for (int y=0; y<height; y++) {
				OS.memmove(line, offset, stride);
				OS.memmove(maskLine, maskOffset, maskStride);
				for (int x=0, offset1=0; x<width; x++, offset1 += 4) {
					if (maskLine[x * 3] == 0) {
						line[offset1 + 0] = line[offset1 + 1] = line[offset1 + 2] = line[offset1 + 3] = 0;
					} else {
						byte r = line[offset1 + 0];
						byte g = line[offset1 + 1];
						byte b = line[offset1 + 2];
						line[offset1 + oa] = (byte)0xFF;
						line[offset1 + or] = r;
						line[offset1 + og] = g;
						line[offset1 + ob] = b;
					}
				}
				OS.memmove(offset, line, stride);
				offset += stride;
				maskOffset += maskStride;
			}
			OS.g_object_unref(maskPixbuf);
		} else if (alpha != -1) {
			int /*long*/ offset = pixels;
			for (int y=0; y<height; y++) {
				OS.memmove(line, offset, stride);
				for (int x=0, offset1=0; x<width; x++, offset1 += 4) {
					/* pre-multiplied alpha */
					int r = ((line[offset1 + 0] & 0xFF) * alpha) + 128;
					r = (r + (r >> 8)) >> 8;
					int g = ((line[offset1 + 1] & 0xFF) * alpha) + 128;
					g = (g + (g >> 8)) >> 8;
					int b = ((line[offset1 + 2] & 0xFF) * alpha) + 128;
					b = (b + (b >> 8)) >> 8;
					line[offset1 + oa] = (byte)alpha;
					line[offset1 + or] = (byte)r;
					line[offset1 + og] = (byte)g;
					line[offset1 + ob] = (byte)b;
				}
				OS.memmove(offset, line, stride);
				offset += stride;
			}
		} else if (alphaData != null) {
			int /*long*/ offset = pixels;
			for (int y = 0; y < h [0]; y++) {
				OS.memmove (line, offset, stride);
				for (int x=0, offset1=0; x<width; x++, offset1 += 4) {
					int alpha = alphaData [y*w [0]+x] & 0xFF;
					/* pre-multiplied alpha */
					int r = ((line[offset1 + 0] & 0xFF) * alpha) + 128;
					r = (r + (r >> 8)) >> 8;
					int g = ((line[offset1 + 1] & 0xFF) * alpha) + 128;
					g = (g + (g >> 8)) >> 8;
					int b = ((line[offset1 + 2] & 0xFF) * alpha) + 128;
					b = (b + (b >> 8)) >> 8;
					line[offset1 + oa] = (byte)alpha;
					line[offset1 + or] = (byte)r;
					line[offset1 + og] = (byte)g;
					line[offset1 + ob] = (byte)b;
				}
				OS.memmove (offset, line, stride);
				offset += stride;
			}
		} else {
			int /*long*/ offset = pixels;
			for (int y = 0; y < h [0]; y++) {
				OS.memmove (line, offset, stride);
				for (int x=0, offset1=0; x<width; x++, offset1 += 4) {
					byte r = line[offset1 + 0];
					byte g = line[offset1 + 1];
					byte b = line[offset1 + 2];
					line[offset1 + oa] = (byte)0xFF;
					line[offset1 + or] = r;
					line[offset1 + og] = g;
					line[offset1 + ob] = b;
				}
				OS.memmove (offset, line, stride);
				offset += stride;
			}
		}
		surfaceData = OS.g_malloc(stride * height);
		OS.memmove(surfaceData, pixels, stride * height);
		surface = Cairo.cairo_image_surface_create_for_data(surfaceData, Cairo.CAIRO_FORMAT_ARGB32, width, height, stride);
		OS.g_object_unref(pixbuf);
	} else {
		int /*long*/ xDisplay = OS.GDK_DISPLAY();
		int /*long*/ xDrawable = OS.GDK_PIXMAP_XID(pixmap);
		int /*long*/ xVisual = OS.gdk_x11_visual_get_xvisual(OS.gdk_visual_get_system());
		surface = Cairo.cairo_xlib_surface_create(xDisplay, xDrawable, xVisual, width, height);
	}
	/* Destroy the image mask if the there is a GC created on the image */
	if (transparentPixel != -1 && memGC != null) destroyMask();
}

/**
 * Destroy the receiver's mask if it exists.
 */
void destroyMask() {
	if (mask == 0) return;
	OS.g_object_unref(mask);
	mask = 0;
}

void destroy() {
	if (memGC != null) memGC.dispose();
	if (pixmap != 0) OS.g_object_unref(pixmap);
	if (mask != 0) OS.g_object_unref(mask);
	if (surface != 0) Cairo.cairo_surface_destroy(surface);
	if (surfaceData != 0) OS.g_free(surfaceData);
	surfaceData = surface = pixmap = mask = 0;
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
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Image)) return false;
	Image image = (Image)object;
	if (OS.USE_CAIRO_SURFACE) {
		return device == image.device && surface == image.surface;
	} else {
		return device == image.device && pixmap == image.pixmap;
	}
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
	//NOT DONE
	return null;
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
public Rectangle getBounds() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width != -1 && height != -1) {
		return new Rectangle(0, 0, width, height);
	}
	int[] w = new int[1]; int[] h = new int[1];
	OS.gdk_drawable_get_size(pixmap, w, h);
	return new Rectangle(0, 0, width = w[0], height = h[0]);
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
	ImageData data;
	if (OS.USE_CAIRO_SURFACE) {
		int width = this.width;
		int height = this.height;
		int stride = Cairo.cairo_format_stride_for_width(Cairo.CAIRO_FORMAT_ARGB32, width);
		byte[] srcData = new byte[stride * height];
		int oa, or, og, ob;
		if (OS.BIG_ENDIAN) {
			oa = 0; or = 1; og = 2; ob = 3;
		} else {
			oa = 3; or = 2; og = 1; ob = 0;
		}
		OS.memmove(srcData, this.surfaceData, srcData.length);
		//TODO make palette more sensible
		PaletteData palette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
		data = new ImageData(width, height, 32, palette, 4, srcData);
		for (int y = 0, offset = 0; y < height; y++) {
			for (int x = 0; x < width; x++, offset += 4) {
				int a = srcData[offset + oa] & 0xFF;
				int r = srcData[offset + or] & 0xFF;
				int g = srcData[offset + og] & 0xFF;
				int b = srcData[offset + ob] & 0xFF;
				srcData[offset + 3] = (byte)a;
				if (a != 0) {
					srcData[offset + 2] = (byte)(((r) / (float)a) * 0xFF);
					srcData[offset + 1] = (byte)(((g) / (float)a) * 0xFF);
					srcData[offset + 0] = (byte)(((b) / (float)a) * 0xFF);
				}
			}
		}
		
		/*
		* TODO is it impossible to retrieve the RGB values when alpha is zero? If this is true
		* then this code is necessary because the transparent pixel needs the RGB values to work. 
		*/
		if (transparentPixel != -1) {
			byte[] alphaData = data.alphaData = new byte[width * height];
			for (int y = 0, offset = 3, alphaOffset = 0; y < height; y++) {
				for (int x = 0; x < width; x++, offset += 4) {
					alphaData[alphaOffset++] = srcData[offset];
				}
			}
		}
		
		for (int i = 3; i < srcData.length; i+= 4) {
			srcData[i] = 0;
		}
	} else {
		int[] w = new int[1], h = new int[1];
	 	OS.gdk_drawable_get_size(pixmap, w, h);
	 	int width = w[0], height = h[0]; 	
	 	int /*long*/ pixbuf = OS.gdk_pixbuf_new(OS.GDK_COLORSPACE_RGB, false, 8, width, height);
		if (pixbuf == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		int /*long*/ colormap = OS.gdk_colormap_get_system();
		OS.gdk_pixbuf_get_from_drawable(pixbuf, pixmap, colormap, 0, 0, 0, 0, width, height);
		int stride = OS.gdk_pixbuf_get_rowstride(pixbuf);
		int /*long*/ pixels = OS.gdk_pixbuf_get_pixels(pixbuf);
		byte[] srcData = new byte[stride * height];
		OS.memmove(srcData, pixels, srcData.length);
		OS.g_object_unref(pixbuf);
	
		PaletteData palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
		data = new ImageData(width, height, 24, palette, 4, srcData);
		data.bytesPerLine = stride;
	
		if (transparentPixel == -1 && type == SWT.ICON && mask != 0) {
			/* Get the icon mask data */
			int /*long*/ gdkImagePtr = OS.gdk_drawable_get_image(mask, 0, 0, width, height);
			if (gdkImagePtr == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			GdkImage gdkImage = new GdkImage();
			OS.memmove(gdkImage, gdkImagePtr);
			byte[] maskData = new byte[gdkImage.bpl * gdkImage.height];
			OS.memmove(maskData, gdkImage.mem, maskData.length);
			OS.g_object_unref(gdkImagePtr);
			int maskPad;
			for (maskPad = 1; maskPad < 128; maskPad++) {
				int bpl = (((width + 7) / 8) + (maskPad - 1)) / maskPad * maskPad;
				if (gdkImage.bpl == bpl) break;
			}
			/* Make mask scanline pad equals to 2 */
			data.maskPad = 2;
			maskData = ImageData.convertPad(maskData, width, height, 1, maskPad, data.maskPad);
			/* Bit swap the mask data if necessary */
			if (gdkImage.byte_order == OS.GDK_LSB_FIRST) {
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
	}
	data.alpha = alpha;
	if (alpha == -1 && alphaData != null) {
		data.alphaData = new byte[alphaData.length];
		System.arraycopy(alphaData, 0, data.alphaData, 0, alphaData.length);
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
 * @param pixmap the OS handle for the image
 * @param mask the OS handle for the image mask
 *
 * @noreference This method is not intended to be referenced by clients.
 */
public static Image gtk_new(Device device, int type, int /*long*/ pixmap, int /*long*/ mask) {
	Image image = new Image(device);
	image.type = type;
	image.pixmap = pixmap;
	image.mask = mask;
	return image;
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
 * @param pixbuf an GdkPixbuf
 *
 * @noreference This method is not intended to be referenced by clients.
 */
public static Image gtk_new_from_pixbuf(Device device, int type, int /*long*/ pixbuf) {
	Image image = new Image(device);
	image.createFromPixbuf(type, pixbuf);
	image.type = type;
	return image;
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
	if (OS.USE_CAIRO_SURFACE) {
		return (int)/*64*/surface;
	} else {
		return (int)/*64*/pixmap;
	}
}

void init(int width, int height) {
	if (width <= 0 || height <= 0) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	this.type = SWT.BITMAP;

	/* Create the pixmap */
	if (OS.USE_CAIRO_SURFACE) {
		int stride = Cairo.cairo_format_stride_for_width(Cairo.CAIRO_FORMAT_RGB24, width);
		int /*long*/ data = surfaceData = OS.g_malloc(stride * height);
		if (surfaceData == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		surface = Cairo.cairo_image_surface_create_for_data(surfaceData, Cairo.CAIRO_FORMAT_RGB24, width, height, stride);
		if (surface == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		OS.memset(data, 0xff, stride * height);
		this.width = width;
		this.height = height;
	} else {
		this.pixmap = OS.gdk_pixmap_new(OS.GDK_ROOT_PARENT(), width, height, -1);
		if (pixmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		/* Fill the bitmap with white */
		GdkColor white = new GdkColor();
		white.red = (short)0xFFFF;
		white.green = (short)0xFFFF;
		white.blue = (short)0xFFFF;
		int /*long*/ colormap = OS.gdk_colormap_get_system();
		OS.gdk_colormap_alloc_color(colormap, white, true, true);
		int /*long*/ gdkGC = OS.gdk_gc_new(pixmap);
		OS.gdk_gc_set_foreground(gdkGC, white);
		OS.gdk_draw_rectangle(pixmap, gdkGC, 1, 0, 0, width, height);
		OS.g_object_unref(gdkGC);
		OS.gdk_colormap_free_colors(colormap, white, 1);
	}
}

void init(ImageData image) {
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int width = this.width = image.width;
	int height = this.height = image.height;
	PaletteData palette = image.palette;
	if (!(((image.depth == 1 || image.depth == 2 || image.depth == 4 || image.depth == 8) && !palette.isDirect) ||
		((image.depth == 8) || (image.depth == 16 || image.depth == 24 || image.depth == 32) && palette.isDirect)))
			SWT.error (SWT.ERROR_UNSUPPORTED_DEPTH);
	if (OS.USE_CAIRO_SURFACE) {
		int stride = Cairo.cairo_format_stride_for_width(Cairo.CAIRO_FORMAT_ARGB32, width);
		int /*long*/ data = surfaceData = OS.g_malloc(stride * height);
		if (surfaceData == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		surface = Cairo.cairo_image_surface_create_for_data(surfaceData, Cairo.CAIRO_FORMAT_ARGB32, width, height, stride);
		if (surface == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		int oa = 0, or = 0, og = 0, ob = 0;
		int redMask, greenMask, blueMask, destDepth = 32, destOrder;
		if (OS.BIG_ENDIAN) {
			oa = 0; or = 1; og = 2; ob = 3;
			redMask = 0xFF00;
			greenMask = 0xFF0000;
			blueMask = 0xFF000000;
			destOrder = ImageData.MSB_FIRST;
		} else {
			oa = 3; or = 2; og = 1; ob = 0;
			redMask = 0xFF0000;
			greenMask = 0xFF00;
			blueMask = 0xFF;
			destOrder = ImageData.LSB_FIRST;
		}
		byte[] buffer = image.data;
		if (!palette.isDirect || image.depth != destDepth || stride != image.bytesPerLine || palette.redMask != redMask || palette.greenMask != greenMask || palette.blueMask != blueMask || destOrder != image.getByteOrder()) {
			buffer = new byte[stride * height];
			if (palette.isDirect) {
				ImageData.blit(ImageData.BLIT_SRC,
					image.data, image.depth, image.bytesPerLine, image.getByteOrder(), 0, 0, width, height, palette.redMask, palette.greenMask, palette.blueMask,
					ImageData.ALPHA_OPAQUE, null, 0, 0, 0, 
					buffer, destDepth, stride, destOrder, 0, 0, width, height, redMask, greenMask, blueMask,
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
					image.data, image.depth, image.bytesPerLine, image.getByteOrder(), 0, 0, width, height, srcReds, srcGreens, srcBlues,
					ImageData.ALPHA_OPAQUE, null, 0, 0, 0,
					buffer, destDepth, stride, destOrder, 0, 0, width, height, redMask, greenMask, blueMask,
					false, false);
			}
		}
		boolean isIcon = image.getTransparencyType() == SWT.TRANSPARENCY_MASK;
		this.type = isIcon ? SWT.ICON : SWT.BITMAP;
		if (isIcon || image.transparentPixel != -1) {
			if (image.transparentPixel != -1) {
				RGB rgb = null;
				if (palette.isDirect) {
					rgb = palette.getRGB(image.transparentPixel);
				} else {
					if (image.transparentPixel < palette.colors.length) {
						rgb = palette.getRGB(image.transparentPixel);
					}
				}
				if (rgb != null) {
					transparentPixel = rgb.red << 16 | rgb.green << 8 | rgb.blue;
				}
			}
			ImageData mask = image.getTransparencyMask();
			for (int y = 0, offset = 0; y < height; y++) {
				for (int x=0; x<width; x++, offset += 4) {
					int alpha = mask.getPixel(x, y) == 0 ? 0 : 0xff;
					/* pre-multiplied alpha */
					int r = ((buffer[offset + or] & 0xFF) * alpha) + 128;
					r = (r + (r >> 8)) >> 8;
					int g = ((buffer[offset + og] & 0xFF) * alpha) + 128;
					g = (g + (g >> 8)) >> 8;
					int b = ((buffer[offset + ob] & 0xFF) * alpha) + 128;
					b = (b + (b >> 8)) >> 8;
					buffer[offset + oa] = (byte)alpha;
					buffer[offset + or] = (byte)r;
					buffer[offset + og] = (byte)g;
					buffer[offset + ob] = (byte)b;
				}
			}
		} else {
			this.alpha = image.alpha;
			if (image.alpha == -1 && image.alphaData != null) {
				this.alphaData = new byte[image.alphaData.length];
				System.arraycopy(image.alphaData, 0, this.alphaData, 0, alphaData.length);
			}
			if (this.alpha != -1) {
				for (int y = 0, offset = 0; y < height; y++) {
					for (int x=0; x<width; x++, offset += 4) {
						int alpha = this.alpha;
						/* pre-multiplied alpha */
						int r = ((buffer[offset + or] & 0xFF) * alpha) + 128;
						r = (r + (r >> 8)) >> 8;
						int g = ((buffer[offset + og] & 0xFF) * alpha) + 128;
						g = (g + (g >> 8)) >> 8;
						int b = ((buffer[offset + ob] & 0xFF) * alpha) + 128;
						b = (b + (b >> 8)) >> 8;
						buffer[offset + oa] = (byte)alpha;
						buffer[offset + or] = (byte)r;
						buffer[offset + og] = (byte)g;
						buffer[offset + ob] = (byte)b;
					}
				}
			} else if (this.alphaData != null) {
				for (int y = 0, offset = 0; y < height; y++) {
					for (int x=0; x<width; x++, offset += 4) {
						int alpha = alphaData [y*width+x] & 0xFF;
						/* pre-multiplied alpha */
						int r = ((buffer[offset + or] & 0xFF) * alpha) + 128;
						r = (r + (r >> 8)) >> 8;
						int g = ((buffer[offset + og] & 0xFF) * alpha) + 128;
						g = (g + (g >> 8)) >> 8;
						int b = ((buffer[offset + ob] & 0xFF) * alpha) + 128;
						b = (b + (b >> 8)) >> 8;
						buffer[offset + oa] = (byte)alpha;
						buffer[offset + or] = (byte)r;
						buffer[offset + og] = (byte)g;
						buffer[offset + ob] = (byte)b;
					}
				}
			} else {
				for (int y = 0, offset = 0; y < height; y++) {
					for (int x=0; x<width; x++, offset += 4) {
						buffer[offset + oa] = (byte)0xFF;
					}
				}
			}
		}
		OS.memmove(data, buffer, stride * height);
		return;
	}
	int /*long*/ pixbuf = OS.gdk_pixbuf_new(OS.GDK_COLORSPACE_RGB, false, 8, width, height);
	if (pixbuf == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int stride = OS.gdk_pixbuf_get_rowstride(pixbuf);
	int /*long*/ data = OS.gdk_pixbuf_get_pixels(pixbuf);
	byte[] buffer = image.data;
	if (!palette.isDirect || image.depth != 24 || stride != image.bytesPerLine || palette.redMask != 0xFF0000 || palette.greenMask != 0xFF00 || palette.blueMask != 0xFF) {
		buffer = new byte[stride * height];
		if (palette.isDirect) {
			ImageData.blit(ImageData.BLIT_SRC,
				image.data, image.depth, image.bytesPerLine, image.getByteOrder(), 0, 0, width, height, palette.redMask, palette.greenMask, palette.blueMask,
				ImageData.ALPHA_OPAQUE, null, 0, 0, 0, 
				buffer, 24, stride, ImageData.MSB_FIRST, 0, 0, width, height, 0xFF0000, 0xFF00, 0xFF,
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
				image.data, image.depth, image.bytesPerLine, image.getByteOrder(), 0, 0, width, height, srcReds, srcGreens, srcBlues,
				ImageData.ALPHA_OPAQUE, null, 0, 0, 0,
				buffer, 24, stride, ImageData.MSB_FIRST, 0, 0, width, height, 0xFF0000, 0xFF00, 0xFF,
				false, false);
		}
	}
	OS.memmove(data, buffer, stride * height);
	int /*long*/ pixmap = OS.gdk_pixmap_new (OS.GDK_ROOT_PARENT(), width, height, -1);
	if (pixmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int /*long*/ gdkGC = OS.gdk_gc_new(pixmap);
	if (gdkGC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.gdk_pixbuf_render_to_drawable(pixbuf, pixmap, gdkGC, 0, 0, 0, 0, width, height, OS.GDK_RGB_DITHER_NORMAL, 0, 0);
	OS.g_object_unref(gdkGC);
	OS.g_object_unref(pixbuf);
	
	boolean isIcon = image.getTransparencyType() == SWT.TRANSPARENCY_MASK;
	if (isIcon || image.transparentPixel != -1) {
		if (image.transparentPixel != -1) {
			RGB rgb = null;
			if (palette.isDirect) {
				rgb = palette.getRGB(image.transparentPixel);
			} else {
				if (image.transparentPixel < palette.colors.length) {
					rgb = palette.getRGB(image.transparentPixel);
				}
			}
			if (rgb != null) {
				transparentPixel = rgb.red << 16 | rgb.green << 8 | rgb.blue;
			}
		}
		int /*long*/ mask = createMask(image, isIcon);
		if (mask == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		this.mask = mask;
		if (isIcon) {
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
		createAlphaMask(width, height);
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
 * 
 * @noreference This method is not intended to be referenced by clients.
 */
public int /*long*/ internal_new_GC (GCData data) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (type != SWT.BITMAP || memGC != null) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	//TODO the handle of the GC should be a cairo object instead of GdkGC.
	int /*long*/ gdkGC = OS.gdk_gc_new(OS.USE_CAIRO_SURFACE ? OS.GDK_ROOT_PARENT() : pixmap);
	if (data != null) {
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		if ((data.style & mask) == 0) {
			data.style |= SWT.LEFT_TO_RIGHT;
		} else {
			if ((data.style & SWT.RIGHT_TO_LEFT) != 0) {
				data.style |= SWT.MIRRORED;
			}
		}
		data.device = device;
		data.drawable = pixmap;
		data.background = device.COLOR_WHITE.handle;
		data.foreground = device.COLOR_BLACK.handle;
		data.font = device.systemFont;
		data.image = this;
	}
	return gdkGC;
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
public void internal_dispose_GC (int /*long*/ gdkGC, GCData data) {
	OS.g_object_unref(gdkGC);
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
public boolean isDisposed() {
	if (OS.USE_CAIRO_SURFACE) {
		return surface == 0;
	} else {
		return pixmap == 0;
	}
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
	//NOT DONE
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "Image {*DISPOSED*}";
	if (OS.USE_CAIRO_SURFACE) {
		return "Image {" + surface + "}";
	} else {
		return "Image {" + pixmap + "}";
	}
}

}
