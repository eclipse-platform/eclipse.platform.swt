/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
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
import org.eclipse.swt.internal.gdip.*;
import org.eclipse.swt.internal.win32.*;

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
	public long handle;

	/**
	 * specifies the transparent pixel
	 */
	int transparentPixel = -1, transparentColor = -1;

	/**
	 * the GC which is drawing on the image
	 */
	GC memGC;

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
	 * Attribute to cache current device zoom level
	 */
	private int currentDeviceZoom = 100;

	/**
	 * width of the image
	 */
	int width = -1;

	/**
	 * height of the image
	 */
	int height = -1;

	/**
	 * specifies the default scanline padding
	 */
	static final int DEFAULT_SCANLINE_PAD = 4;

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Image (Device device) {
	super(device);
	currentDeviceZoom = DPIUtil.getDeviceZoom ();
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
	currentDeviceZoom = DPIUtil.getDeviceZoom ();
	width = DPIUtil.autoScaleUp (width);
	height = DPIUtil.autoScaleUp (height);
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
	device = this.device;
	if (srcImage == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (srcImage.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	Rectangle rect = srcImage.getBoundsInPixels();
	this.type = srcImage.type;
	this.imageDataProvider = srcImage.imageDataProvider;
	this.imageFileNameProvider = srcImage.imageFileNameProvider;
	this.styleFlag = srcImage.styleFlag | flag;
	this.currentDeviceZoom = srcImage.currentDeviceZoom;
	switch (flag) {
		case SWT.IMAGE_COPY: {
			switch (type) {
				case SWT.BITMAP:
					/* Get the HDC for the device */
					long hDC = device.internal_new_GC(null);

					/* Copy the bitmap */
					long hdcSource = OS.CreateCompatibleDC(hDC);
					long hdcDest = OS.CreateCompatibleDC(hDC);
					long hOldSrc = OS.SelectObject(hdcSource, srcImage.handle);
					BITMAP bm = new BITMAP();
					OS.GetObject(srcImage.handle, BITMAP.sizeof, bm);
					handle = OS.CreateCompatibleBitmap(hdcSource, rect.width, bm.bmBits != 0 ? -rect.height : rect.height);
					if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
					long hOldDest = OS.SelectObject(hdcDest, handle);
					OS.BitBlt(hdcDest, 0, 0, rect.width, rect.height, hdcSource, 0, 0, OS.SRCCOPY);
					OS.SelectObject(hdcSource, hOldSrc);
					OS.SelectObject(hdcDest, hOldDest);
					OS.DeleteDC(hdcSource);
					OS.DeleteDC(hdcDest);

					/* Release the HDC for the device */
					device.internal_dispose_GC(hDC, null);

					transparentPixel = srcImage.transparentPixel;
					break;
				case SWT.ICON:
					handle = OS.CopyImage(srcImage.handle, OS.IMAGE_ICON, rect.width, rect.height, 0);
					if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
					break;
				default:
					SWT.error(SWT.ERROR_INVALID_IMAGE);
			}
			break;
		}
		case SWT.IMAGE_DISABLE: {
			ImageData data = srcImage.getImageDataAtCurrentZoom();
			PaletteData palette = data.palette;
			RGB[] rgbs = new RGB[3];
			rgbs[0] = device.getSystemColor(SWT.COLOR_BLACK).getRGB();
			rgbs[1] = device.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW).getRGB();
			rgbs[2] = device.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND).getRGB();
			ImageData newData = new ImageData(rect.width, rect.height, 8, new PaletteData(rgbs));
			newData.alpha = data.alpha;
			newData.alphaData = data.alphaData;
			newData.maskData = data.maskData;
			newData.maskPad = data.maskPad;
			if (data.transparentPixel != -1) newData.transparentPixel = 0;

			/* Convert the pixels. */
			int[] scanline = new int[rect.width];
			int[] maskScanline = null;
			ImageData mask = null;
			if (data.maskData != null) mask = data.getTransparencyMask();
			if (mask != null) maskScanline = new int[rect.width];
			int redMask = palette.redMask;
			int greenMask = palette.greenMask;
			int blueMask = palette.blueMask;
			int redShift = palette.redShift;
			int greenShift = palette.greenShift;
			int blueShift = palette.blueShift;
			for (int y=0; y<rect.height; y++) {
				int offset = y * newData.bytesPerLine;
				data.getPixels(0, y, rect.width, scanline, 0);
				if (mask != null) mask.getPixels(0, y, rect.width, maskScanline, 0);
				for (int x=0; x<rect.width; x++) {
					int pixel = scanline[x];
					if (!((data.transparentPixel != -1 && pixel == data.transparentPixel) || (mask != null && maskScanline[x] == 0))) {
						int red, green, blue;
						if (palette.isDirect) {
							red = pixel & redMask;
							red = (redShift < 0) ? red >>> -redShift : red << redShift;
							green = pixel & greenMask;
							green = (greenShift < 0) ? green >>> -greenShift : green << greenShift;
							blue = pixel & blueMask;
							blue = (blueShift < 0) ? blue >>> -blueShift : blue << blueShift;
						} else {
							red = palette.colors[pixel].red;
							green = palette.colors[pixel].green;
							blue = palette.colors[pixel].blue;
						}
						int intensity = red * red + green * green + blue * blue;
						if (intensity < 98304) {
							newData.data[offset] = (byte)1;
						} else {
							newData.data[offset] = (byte)2;
						}
					}
					offset++;
				}
			}
			init (newData);
			break;
		}
		case SWT.IMAGE_GRAY: {
			ImageData data = srcImage.getImageDataAtCurrentZoom();
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
				newData = new ImageData(rect.width, rect.height, 8, new PaletteData(rgbs));
				newData.alpha = data.alpha;
				newData.alphaData = data.alphaData;
				newData.maskData = data.maskData;
				newData.maskPad = data.maskPad;
				if (data.transparentPixel != -1) newData.transparentPixel = 254;

				/* Convert the pixels. */
				int[] scanline = new int[rect.width];
				int redMask = palette.redMask;
				int greenMask = palette.greenMask;
				int blueMask = palette.blueMask;
				int redShift = palette.redShift;
				int greenShift = palette.greenShift;
				int blueShift = palette.blueShift;
				for (int y=0; y<rect.height; y++) {
					int offset = y * newData.bytesPerLine;
					data.getPixels(0, y, rect.width, scanline, 0);
					for (int x=0; x<rect.width; x++) {
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
		}
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
	currentDeviceZoom = DPIUtil.getDeviceZoom ();
	bounds = DPIUtil.autoScaleUp (bounds);
	init(bounds.width, bounds.height);
	init();
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
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	currentDeviceZoom = DPIUtil.getDeviceZoom ();
	data = DPIUtil.autoScaleUp (device, data);
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
	currentDeviceZoom = DPIUtil.getDeviceZoom ();
	source = DPIUtil.autoScaleUp(device, source);
	mask = DPIUtil.autoScaleUp(device, mask);
	mask = ImageData.convertMask(mask);
	init(this.device, this, source, mask);
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
public Image (Device device, InputStream stream) {
	super(device);
	currentDeviceZoom = DPIUtil.getDeviceZoom ();
	ImageData data = DPIUtil.autoScaleUp(device, new ImageData(stream));
	init(data);
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
public Image (Device device, String filename) {
	super(device);
	if (filename == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	currentDeviceZoom = DPIUtil.getDeviceZoom ();
	ImageData data = DPIUtil.autoScaleUp(device, new ImageData(filename));
	init(data);
	init();
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
	this.imageFileNameProvider = imageFileNameProvider;
	currentDeviceZoom = DPIUtil.getDeviceZoom ();
	boolean[] found = new boolean[1];
	String fileName = DPIUtil.validateAndGetImagePathAtZoom (imageFileNameProvider, currentDeviceZoom, found);
	if (found[0]) {
		initNative (fileName);
		if (this.handle == 0) init(new ImageData (fileName));
	} else {
		ImageData resizedData = DPIUtil.autoScaleUp (device, new ImageData (fileName));
		init(resizedData);
	}
	init();
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
	this.imageDataProvider = imageDataProvider;
	currentDeviceZoom = DPIUtil.getDeviceZoom ();
	boolean[] found = new boolean[1];
	ImageData data =  DPIUtil.validateAndGetImageDataAtZoom(imageDataProvider, currentDeviceZoom, found);
	if (found[0]) {
		init(data);
	} else {
		ImageData resizedData = DPIUtil.autoScaleUp(device, data);
		init (resizedData);
	}
	init();
}

/**
 * Refresh the Image based on the zoom level, if required.
 *
 * @return true if image is refreshed
 */
boolean refreshImageForZoom () {
	boolean refreshed = false;
	int deviceZoomLevel = DPIUtil.getDeviceZoom();
	if (imageFileNameProvider != null) {
		if (deviceZoomLevel != currentDeviceZoom) {
			boolean[] found = new boolean[1];
			String filename = DPIUtil.validateAndGetImagePathAtZoom (imageFileNameProvider, deviceZoomLevel, found);
			/* Avoid re-creating the fall-back image, when current zoom is already 100% */
			if (found[0] || currentDeviceZoom != 100) {
				/* Release current native resources */
				destroy ();
				initNative(filename);
				if (this.handle == 0) init(new ImageData (filename));
				init();
				refreshed = true;
			}
			if (!found[0]) {
				/* Release current native resources */
				destroy ();
				ImageData resizedData = DPIUtil.autoScaleUp (device, new ImageData (filename));
				init(resizedData);
				init ();
				refreshed = true;
			}
			currentDeviceZoom = deviceZoomLevel;
		}
	} else if (imageDataProvider != null) {
		if (deviceZoomLevel != currentDeviceZoom) {
			boolean[] found = new boolean[1];
			ImageData data = DPIUtil.validateAndGetImageDataAtZoom (imageDataProvider, deviceZoomLevel, found);
			/* Avoid re-creating the fall-back image, when current zoom is already 100% */
			if (found[0] || currentDeviceZoom != 100) {
				/* Release current native resources */
				destroy ();
				init(data);
				init();
				refreshed = true;
			}
			if (!found[0]) {
				/* Release current native resources */
				destroy ();
				ImageData resizedData = DPIUtil.autoScaleUp (device, data);
				init(resizedData);
				init();
				refreshed = true;
			}
			currentDeviceZoom = deviceZoomLevel;
		}
	} else {
		if (deviceZoomLevel != currentDeviceZoom) {
			ImageData data = getImageDataAtCurrentZoom();
			destroy ();
			ImageData resizedData = DPIUtil.autoScaleImageData(device, data, deviceZoomLevel, currentDeviceZoom);
			init(resizedData);
			init();
			refreshed = true;
			currentDeviceZoom = deviceZoomLevel;
		}
	}
	return refreshed;
}

void initNative(String filename) {
	device.checkGDIP();
	boolean gdip = true;
	/*
	* Bug in GDI+.  For some reason, Bitmap.LockBits() segment faults
	* when loading GIF files in 64-bit Windows.  The fix is to not use
	* GDI+ image loading in this case.
	*/
	if (gdip && C.PTR_SIZEOF == 8 && filename.toLowerCase().endsWith(".gif")) gdip = false;
	/*
	* Bug in GDI+. Bitmap.LockBits() fails to load GIF files in
	* Windows 7 when the image has a position offset in the first frame.
	* The fix is to not use GDI+ image loading in this case.
	*/
	if (filename.toLowerCase().endsWith(".gif")) gdip = false;
	if (gdip) {
		int length = filename.length();
		char[] chars = new char[length+1];
		filename.getChars(0, length, chars, 0);
		long bitmap = Gdip.Bitmap_new(chars, false);
		if (bitmap != 0) {
			int error = SWT.ERROR_NO_HANDLES;
			int status = Gdip.Image_GetLastStatus(bitmap);
			if (status == 0) {
				if (filename.toLowerCase().endsWith(".ico")) {
					this.type = SWT.ICON;
					long[] hicon = new long[1];
					status = Gdip.Bitmap_GetHICON(bitmap, hicon);
					this.handle = hicon[0];
				} else {
					this.type = SWT.BITMAP;
					int width = Gdip.Image_GetWidth(bitmap);
					int height = Gdip.Image_GetHeight(bitmap);
					int pixelFormat = Gdip.Image_GetPixelFormat(bitmap);
					switch (pixelFormat) {
						case Gdip.PixelFormat16bppRGB555:
						case Gdip.PixelFormat16bppRGB565:
							this.handle = createDIB(width, height, 16);
							break;
						case Gdip.PixelFormat24bppRGB:
						case Gdip.PixelFormat32bppCMYK:
							this.handle = createDIB(width, height, 24);
							break;
						case Gdip.PixelFormat32bppRGB:
						// These will lose either precision or transparency
						case Gdip.PixelFormat16bppGrayScale:
						case Gdip.PixelFormat48bppRGB:
						case Gdip.PixelFormat32bppPARGB:
						case Gdip.PixelFormat64bppARGB:
						case Gdip.PixelFormat64bppPARGB:
							this.handle = createDIB(width, height, 32);
							break;
					}
					if (this.handle != 0) {
						/*
						* This performs better than getting the bits with Bitmap.LockBits(),
						* but it cannot be used when there is transparency.
						*/
						long hDC = device.internal_new_GC(null);
						long srcHDC = OS.CreateCompatibleDC(hDC);
						long oldSrcBitmap = OS.SelectObject(srcHDC, this.handle);
						long graphics = Gdip.Graphics_new(srcHDC);
						if (graphics != 0) {
							Rect rect = new Rect();
							rect.Width = width;
							rect.Height = height;
							status = Gdip.Graphics_DrawImage(graphics, bitmap, rect, 0, 0, width, height, Gdip.UnitPixel, 0, 0, 0);
							if (status != 0) {
								error = SWT.ERROR_INVALID_IMAGE;
								OS.DeleteObject(handle);
								this.handle = 0;
							}
							Gdip.Graphics_delete(graphics);
						}
						OS.SelectObject(srcHDC, oldSrcBitmap);
						OS.DeleteDC(srcHDC);
						device.internal_dispose_GC(hDC, null);
					} else {
						long lockedBitmapData = Gdip.BitmapData_new();
						if (lockedBitmapData != 0) {
							status = Gdip.Bitmap_LockBits(bitmap, 0, 0, pixelFormat, lockedBitmapData);
							if (status == 0) {
								BitmapData bitmapData = new BitmapData();
								Gdip.MoveMemory(bitmapData, lockedBitmapData);
								int stride = bitmapData.Stride;
								long pixels = bitmapData.Scan0;
								int depth = 0, scanlinePad = 4, transparentPixel = -1;
								switch (bitmapData.PixelFormat) {
									case Gdip.PixelFormat1bppIndexed: depth = 1; break;
									case Gdip.PixelFormat4bppIndexed: depth = 4; break;
									case Gdip.PixelFormat8bppIndexed: depth = 8; break;
									case Gdip.PixelFormat16bppARGB1555:
									case Gdip.PixelFormat16bppRGB555:
									case Gdip.PixelFormat16bppRGB565: depth = 16; break;
									case Gdip.PixelFormat24bppRGB: depth = 24; break;
									case Gdip.PixelFormat32bppRGB:
									case Gdip.PixelFormat32bppARGB: depth = 32; break;
								}
								if (depth != 0) {
									PaletteData paletteData = null;
									switch (bitmapData.PixelFormat) {
										case Gdip.PixelFormat1bppIndexed:
										case Gdip.PixelFormat4bppIndexed:
										case Gdip.PixelFormat8bppIndexed:
											int paletteSize = Gdip.Image_GetPaletteSize(bitmap);
											long hHeap = OS.GetProcessHeap();
											long palette = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, paletteSize);
											if (palette == 0) SWT.error(SWT.ERROR_NO_HANDLES);
											Gdip.Image_GetPalette(bitmap, palette, paletteSize);
											ColorPalette colorPalette = new ColorPalette();
											Gdip.MoveMemory(colorPalette, palette, ColorPalette.sizeof);
											int[] entries = new int[colorPalette.Count];
											OS.MoveMemory(entries, palette + 8, entries.length * 4);
											OS.HeapFree(hHeap, 0, palette);
											RGB[] rgbs = new RGB[colorPalette.Count];
											paletteData = new PaletteData(rgbs);
											for (int i = 0; i < entries.length; i++) {
												if (((entries[i] >> 24) & 0xFF) == 0 && (colorPalette.Flags & Gdip.PaletteFlagsHasAlpha) != 0) {
													transparentPixel = i;
												}
												rgbs[i] = new RGB(((entries[i] & 0xFF0000) >> 16), ((entries[i] & 0xFF00) >> 8), ((entries[i] & 0xFF) >> 0));
											}
											break;
										case Gdip.PixelFormat16bppARGB1555:
										case Gdip.PixelFormat16bppRGB555: paletteData = new PaletteData(0x7C00, 0x3E0, 0x1F); break;
										case Gdip.PixelFormat16bppRGB565: paletteData = new PaletteData(0xF800, 0x7E0, 0x1F); break;
										case Gdip.PixelFormat24bppRGB: paletteData = new PaletteData(0xFF, 0xFF00, 0xFF0000); break;
										case Gdip.PixelFormat32bppRGB:
										case Gdip.PixelFormat32bppARGB: paletteData = new PaletteData(0xFF00, 0xFF0000, 0xFF000000); break;
									}
									byte[] data = new byte[stride * height], alphaData = null;
									OS.MoveMemory(data, pixels, data.length);
									switch (bitmapData.PixelFormat) {
										case Gdip.PixelFormat16bppARGB1555:
											alphaData = new byte[width * height];
											for (int i = 1, j = 0; i < data.length; i += 2, j++) {
												alphaData[j] = (byte)((data[i] & 0x80) != 0 ? 255 : 0);
											}
											break;
										case Gdip.PixelFormat32bppARGB:
											alphaData = new byte[width * height];
											for (int i = 3, j = 0; i < data.length; i += 4, j++) {
												alphaData[j] = data[i];
											}
											break;
									}
									ImageData img = new ImageData(width, height, depth, paletteData, scanlinePad, data);
									img.transparentPixel = transparentPixel;
									img.alphaData = alphaData;
									init(img);
								}
								Gdip.Bitmap_UnlockBits(bitmap, lockedBitmapData);
							} else {
								error = SWT.ERROR_INVALID_IMAGE;
							}
							Gdip.BitmapData_delete(lockedBitmapData);
						}
					}
				}
			}
			Gdip.Bitmap_delete(bitmap);
			if (status == 0) {
				if (this.handle == 0) SWT.error(error);
			}
		}
	}
}

long [] createGdipImage() {
	switch (type) {
		case SWT.BITMAP: {
			BITMAP bm = new BITMAP();
			OS.GetObject(handle, BITMAP.sizeof, bm);
			int depth = bm.bmPlanes * bm.bmBitsPixel;
			boolean isDib = bm.bmBits != 0;
			boolean hasAlpha = isDib && depth == 32;
			if (hasAlpha || transparentPixel != -1) {
				int imgWidth = bm.bmWidth;
				int imgHeight = bm.bmHeight;
				long srcHdc;
				long memHdc;
				{
					long hDC = device.internal_new_GC(null);
					if (hDC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
					srcHdc = OS.CreateCompatibleDC(hDC);
					memHdc = OS.CreateCompatibleDC(hDC);
					device.internal_dispose_GC(hDC, null);
				}
				if (srcHdc == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				if (memHdc == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				long oldSrcBitmap = OS.SelectObject(srcHdc, handle);
				long memDib = createDIB(imgWidth, imgHeight, 32);
				if (memDib == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				long oldMemBitmap = OS.SelectObject(memHdc, memDib);
				BITMAP dibBM = new BITMAP();
				OS.GetObject(memDib, BITMAP.sizeof, dibBM);
				int sizeInBytes = dibBM.bmWidthBytes * dibBM.bmHeight;
				OS.BitBlt(memHdc, 0, 0, imgWidth, imgHeight, srcHdc, 0, 0, OS.SRCCOPY);
				long hHeap = OS.GetProcessHeap();
				long pixels = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, sizeInBytes);
				if (pixels == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				byte red = 0, green = 0, blue = 0;
				if (hasAlpha) {
					OS.MoveMemory(pixels, bm.bmBits, sizeInBytes);
				}
				else {
					if (bm.bmBitsPixel <= 8)  {
						byte[] color = new byte[4];
						OS.GetDIBColorTable(srcHdc, transparentPixel, 1, color);
						blue = color[0];
						green = color[1];
						red = color[2];
					} else {
						switch (bm.bmBitsPixel) {
							case 16:
								int blueMask = 0x1F;
								int blueShift = ImageData.getChannelShift(blueMask);
								byte[] blues = ImageData.ANY_TO_EIGHT[ImageData.getChannelWidth(blueMask, blueShift)];
								blue = blues[(transparentPixel & blueMask) >> blueShift];
								int greenMask = 0x3E0;
								int greenShift = ImageData.getChannelShift(greenMask);
								byte[] greens = ImageData.ANY_TO_EIGHT[ImageData.getChannelWidth(greenMask, greenShift)];
								green = greens[(transparentPixel & greenMask) >> greenShift];
								int redMask = 0x7C00;
								int redShift = ImageData.getChannelShift(redMask);
								byte[] reds = ImageData.ANY_TO_EIGHT[ImageData.getChannelWidth(redMask, redShift)];
								red = reds[(transparentPixel & redMask) >> redShift];
								break;
							case 24:
								blue = (byte)((transparentPixel & 0xFF0000) >> 16);
								green = (byte)((transparentPixel & 0xFF00) >> 8);
								red = (byte)(transparentPixel & 0xFF);
								break;
							case 32:
								blue = (byte)((transparentPixel & 0xFF000000) >>> 24);
								green = (byte)((transparentPixel & 0xFF0000) >> 16);
								red = (byte)((transparentPixel & 0xFF00) >> 8);
								break;
						}
					}
					byte[] srcData = new byte[sizeInBytes];
					OS.MoveMemory(srcData, dibBM.bmBits, sizeInBytes);
					for (int y = 0, dp = 0; y < imgHeight; ++y) {
						for (int x = 0; x < imgWidth; ++x) {
							if (srcData[dp] == blue && srcData[dp + 1] == green && srcData[dp + 2] == red) {
								srcData[dp + 3] = (byte)0;
							} else {
								srcData[dp + 3] = (byte)0xFF;
							}
							dp += 4;
						}
					}
					OS.MoveMemory(pixels, srcData, sizeInBytes);
				}
				OS.SelectObject(srcHdc, oldSrcBitmap);
				OS.SelectObject(memHdc, oldMemBitmap);
				OS.DeleteObject(srcHdc);
				OS.DeleteObject(memHdc);
				OS.DeleteObject(memDib);
				int pixelFormat = hasAlpha ? Gdip.PixelFormat32bppPARGB : Gdip.PixelFormat32bppARGB;
				return new long []{Gdip.Bitmap_new(imgWidth, imgHeight, dibBM.bmWidthBytes, pixelFormat, pixels), pixels};
			}
			return new long []{Gdip.Bitmap_new(handle, 0), 0};
		}
		case SWT.ICON: {
			/*
			* Bug in GDI+. Creating a new GDI+ Bitmap from a HICON segment faults
			* when the icon width is bigger than the icon height.  The fix is to
			* detect this and create a PixelFormat32bppARGB image instead.
			*/
			ICONINFO iconInfo = new ICONINFO();
			OS.GetIconInfo(handle, iconInfo);
			long hBitmap = iconInfo.hbmColor;
			if (hBitmap == 0) hBitmap = iconInfo.hbmMask;
			BITMAP bm = new BITMAP();
			OS.GetObject(hBitmap, BITMAP.sizeof, bm);
			int imgWidth = bm.bmWidth;
			int imgHeight = hBitmap == iconInfo.hbmMask ? bm.bmHeight / 2 : bm.bmHeight;
			long img = 0, pixels = 0;
			/*
			* Bug in GDI+.  Bitmap_new() segments fault if the image width
			* is greater than the image height.
			*
			* Note that it also fails to generated an appropriate alpha
			* channel when the icon depth is 32.
			*/
			if (imgWidth > imgHeight || bm.bmBitsPixel == 32) {
				long hDC = device.internal_new_GC(null);
				long srcHdc = OS.CreateCompatibleDC(hDC);
				long oldSrcBitmap = OS.SelectObject(srcHdc, hBitmap);
				long memHdc = OS.CreateCompatibleDC(hDC);
				long memDib = createDIB(imgWidth, imgHeight, 32);
				if (memDib == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				long oldMemBitmap = OS.SelectObject(memHdc, memDib);
				BITMAP dibBM = new BITMAP();
				OS.GetObject(memDib, BITMAP.sizeof, dibBM);
				OS.BitBlt(memHdc, 0, 0, imgWidth, imgHeight, srcHdc, 0, hBitmap == iconInfo.hbmMask ? imgHeight : 0, OS.SRCCOPY);
				OS.SelectObject(memHdc, oldMemBitmap);
				OS.DeleteObject(memHdc);
				byte[] srcData = new byte[dibBM.bmWidthBytes * dibBM.bmHeight];
				OS.MoveMemory(srcData, dibBM.bmBits, srcData.length);
				OS.DeleteObject(memDib);
				OS.SelectObject(srcHdc, iconInfo.hbmMask);
				for (int y = 0, dp = 3; y < imgHeight; ++y) {
					for (int x = 0; x < imgWidth; ++x) {
						if (srcData[dp] == 0) {
							if (OS.GetPixel(srcHdc, x, y) != 0) {
								srcData[dp] = (byte)0;
							} else {
								srcData[dp] = (byte)0xFF;
							}
						}
						dp += 4;
					}
				}
				OS.SelectObject(srcHdc, oldSrcBitmap);
				OS.DeleteObject(srcHdc);
				device.internal_dispose_GC(hDC, null);
				long hHeap = OS.GetProcessHeap();
				pixels = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, srcData.length);
				if (pixels == 0) SWT.error(SWT.ERROR_NO_HANDLES);
				OS.MoveMemory(pixels, srcData, srcData.length);
				img = Gdip.Bitmap_new(imgWidth, imgHeight, dibBM.bmWidthBytes, Gdip.PixelFormat32bppARGB, pixels);
			} else {
				img = Gdip.Bitmap_new(handle);
			}
			if (iconInfo.hbmColor != 0) OS.DeleteObject(iconInfo.hbmColor);
			if (iconInfo.hbmMask != 0) OS.DeleteObject(iconInfo.hbmMask);
			return new long []{img, pixels};
		}
		default: SWT.error(SWT.ERROR_INVALID_IMAGE);
	}
	return null;
}

@Override
void destroy () {
	if (memGC != null) memGC.dispose();
	if (type == SWT.ICON) {
		OS.DestroyIcon (handle);
	} else {
		OS.DeleteObject (handle);
	}
	handle = 0;
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
	Image image = (Image) object;
	if (device != image.device || transparentPixel != image.transparentPixel) return false;
	if (imageDataProvider != null && image.imageDataProvider != null) {
		return (styleFlag == image.styleFlag) && imageDataProvider.equals (image.imageDataProvider);
	} else if (imageFileNameProvider != null && image.imageFileNameProvider != null) {
		return (styleFlag == image.styleFlag) && imageFileNameProvider.equals (image.imageFileNameProvider);
	} else {
		return handle == image.handle;
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
 * </p>
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

	/* Get the HDC for the device */
	long hDC = device.internal_new_GC(null);

	/* Compute the background color */
	BITMAP bm = new BITMAP();
	OS.GetObject(handle, BITMAP.sizeof, bm);
	long hdcMem = OS.CreateCompatibleDC(hDC);
	long hOldObject = OS.SelectObject(hdcMem, handle);
	int red = 0, green = 0, blue = 0;
	if (bm.bmBitsPixel <= 8)  {
		byte[] color = new byte[4];
		OS.GetDIBColorTable(hdcMem, transparentPixel, 1, color);
		blue = color[0] & 0xFF;
		green = color[1] & 0xFF;
		red = color[2] & 0xFF;
	} else {
		switch (bm.bmBitsPixel) {
			case 16:
				blue = (transparentPixel & 0x1F) << 3;
				green = (transparentPixel & 0x3E0) >> 2;
				red = (transparentPixel & 0x7C00) >> 7;
				break;
			case 24:
				blue = (transparentPixel & 0xFF0000) >> 16;
				green = (transparentPixel & 0xFF00) >> 8;
				red = transparentPixel & 0xFF;
				break;
			case 32:
				blue = (transparentPixel & 0xFF000000) >>> 24;
				green = (transparentPixel & 0xFF0000) >> 16;
				red = (transparentPixel & 0xFF00) >> 8;
				break;
			default:
				return null;
		}
	}
	OS.SelectObject(hdcMem, hOldObject);
	OS.DeleteDC(hdcMem);

	/* Release the HDC for the device */
	device.internal_dispose_GC(hDC, null);
	return Color.win32_new(device, (blue << 16) | (green << 8) | red);
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
	return getBounds (100);
}

Rectangle getBounds(int zoom) {
	Rectangle bounds = getBoundsInPixels();
	if (bounds != null && zoom != currentDeviceZoom) {
		bounds = DPIUtil.autoScaleBounds(bounds, zoom, currentDeviceZoom);
	}
	return bounds;
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
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width != -1 && height != -1) {
		return new Rectangle(0, 0, width, height);
	}
	switch (type) {
		case SWT.BITMAP:
			BITMAP bm = new BITMAP();
			OS.GetObject(handle, BITMAP.sizeof, bm);
			return new Rectangle(0, 0, width = bm.bmWidth, height = bm.bmHeight);
		case SWT.ICON:
			ICONINFO info = new ICONINFO();
			OS.GetIconInfo(handle, info);
			long hBitmap = info.hbmColor;
			if (hBitmap == 0) hBitmap = info.hbmMask;
			bm = new BITMAP();
			OS.GetObject(hBitmap, BITMAP.sizeof, bm);
			if (hBitmap == info.hbmMask) bm.bmHeight /= 2;
			if (info.hbmColor != 0) OS.DeleteObject(info.hbmColor);
			if (info.hbmMask != 0) OS.DeleteObject(info.hbmMask);
			return new Rectangle(0, 0, width = bm.bmWidth, height = bm.bmHeight);
		default:
			SWT.error(SWT.ERROR_INVALID_IMAGE);
			return null;
	}
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
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return getImageData(100);
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
public ImageData getImageData (int zoom) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);

	if (zoom == currentDeviceZoom) {
		return getImageDataAtCurrentZoom();
	} else if (imageDataProvider != null) {
		boolean[] found = new boolean[1];
		ImageData data = DPIUtil.validateAndGetImageDataAtZoom (imageDataProvider, zoom, found);
		// exact image found
		if (found[0]) {
			return data;
		}
		// AutoScale the image at 100% zoom
		return DPIUtil.autoScaleImageData (device, data, zoom, 100);
	} else if (imageFileNameProvider != null) {
		boolean[] found = new boolean[1];
		String fileName = DPIUtil.validateAndGetImagePathAtZoom (imageFileNameProvider, zoom, found);
		// exact image found
		if (found[0]) {
			return new ImageData (fileName);
		}
		// AutoScale the image at 100% zoom
		return DPIUtil.autoScaleImageData (device, new ImageData (fileName), zoom, 100);
	} else {
		return DPIUtil.autoScaleImageData (device, getImageDataAtCurrentZoom (), zoom, currentDeviceZoom);
	}
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
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	BITMAP bm;
	int depth, width, height;
	switch (type) {
		case SWT.ICON: {
			ICONINFO info = new ICONINFO();
			OS.GetIconInfo(handle, info);
			/* Get the basic BITMAP information */
			long hBitmap = info.hbmColor;
			if (hBitmap == 0) hBitmap = info.hbmMask;
			bm = new BITMAP();
			OS.GetObject(hBitmap, BITMAP.sizeof, bm);
			depth = bm.bmPlanes * bm.bmBitsPixel;
			width = bm.bmWidth;
			if (hBitmap == info.hbmMask) bm.bmHeight /= 2;
			height = bm.bmHeight;
			int numColors = 0;
			if (depth <= 8) numColors = 1 << depth;
			/* Create the BITMAPINFO */
			BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
			bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
			bmiHeader.biWidth = width;
			bmiHeader.biHeight = -height;
			bmiHeader.biPlanes = 1;
			bmiHeader.biBitCount = (short)depth;
			bmiHeader.biCompression = OS.BI_RGB;
			byte[] bmi = new byte[BITMAPINFOHEADER.sizeof + numColors * 4];
			OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);

			/* Get the HDC for the device */
			long hDC = device.internal_new_GC(null);

			/* Create the DC and select the bitmap */
			long hBitmapDC = OS.CreateCompatibleDC(hDC);
			long hOldBitmap = OS.SelectObject(hBitmapDC, hBitmap);
			/* Find the size of the image and allocate data */
			int imageSize;
			/* Call with null lpBits to get the image size */
			OS.GetDIBits(hBitmapDC, hBitmap, 0, height, null, bmi, OS.DIB_RGB_COLORS);
			OS.MoveMemory(bmiHeader, bmi, BITMAPINFOHEADER.sizeof);
			imageSize = bmiHeader.biSizeImage;
			byte[] data = new byte[imageSize];
			/* Get the bitmap data */
			OS.GetDIBits(hBitmapDC, hBitmap, 0, height, data, bmi, OS.DIB_RGB_COLORS);
			/* Calculate the palette */
			PaletteData palette = null;
			if (depth <= 8) {
				RGB[] rgbs = new RGB[numColors];
				int srcIndex = 40;
				for (int i = 0; i < numColors; i++) {
					rgbs[i] = new RGB(bmi[srcIndex + 2] & 0xFF, bmi[srcIndex + 1] & 0xFF, bmi[srcIndex] & 0xFF);
					srcIndex += 4;
				}
				palette = new PaletteData(rgbs);
			} else if (depth == 16) {
				palette = new PaletteData(0x7C00, 0x3E0, 0x1F);
			} else if (depth == 24) {
				palette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
			} else if (depth == 32) {
				palette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
			} else {
				SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
			}

			/* Do the mask */
			byte [] maskData = null;
			if (info.hbmColor == 0) {
				/* Do the bottom half of the mask */
				maskData = new byte[imageSize];
				OS.GetDIBits(hBitmapDC, hBitmap, height, height, maskData, bmi, OS.DIB_RGB_COLORS);
			} else {
				/* Do the entire mask */
				/* Create the BITMAPINFO */
				bmiHeader = new BITMAPINFOHEADER();
				bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
				bmiHeader.biWidth = width;
				bmiHeader.biHeight = -height;
				bmiHeader.biPlanes = 1;
				bmiHeader.biBitCount = 1;
				bmiHeader.biCompression = OS.BI_RGB;
				bmi = new byte[BITMAPINFOHEADER.sizeof + 8];
				OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);

				/* First color black, second color white */
				int offset = BITMAPINFOHEADER.sizeof;
				bmi[offset + 4] = bmi[offset + 5] = bmi[offset + 6] = (byte)0xFF;
				bmi[offset + 7] = 0;
				OS.SelectObject(hBitmapDC, info.hbmMask);
				/* Call with null lpBits to get the image size */
				OS.GetDIBits(hBitmapDC, info.hbmMask, 0, height, null, bmi, OS.DIB_RGB_COLORS);
				OS.MoveMemory(bmiHeader, bmi, BITMAPINFOHEADER.sizeof);
				imageSize = bmiHeader.biSizeImage;
				maskData = new byte[imageSize];
				OS.GetDIBits(hBitmapDC, info.hbmMask, 0, height, maskData, bmi, OS.DIB_RGB_COLORS);
				/* Loop to invert the mask */
				for (int i = 0; i < maskData.length; i++) {
					maskData[i] ^= -1;
				}
				/* Make sure mask scanlinePad is 2 */
				int maskPad;
				int bpl = imageSize / height;
				for (maskPad = 1; maskPad < 128; maskPad++) {
					int calcBpl = (((width + 7) / 8) + (maskPad - 1)) / maskPad * maskPad;
					if (calcBpl == bpl) break;
				}
				maskData = ImageData.convertPad(maskData, width, height, 1, maskPad, 2);
			}
			/* Clean up */
			OS.SelectObject(hBitmapDC, hOldBitmap);
			OS.DeleteDC(hBitmapDC);

			/* Release the HDC for the device */
			device.internal_dispose_GC(hDC, null);

			if (info.hbmColor != 0) OS.DeleteObject(info.hbmColor);
			if (info.hbmMask != 0) OS.DeleteObject(info.hbmMask);
			/* Construct and return the ImageData */
			ImageData imageData = new ImageData(width, height, depth, palette, 4, data);
			imageData.maskData = maskData;
			imageData.maskPad = 2;
			return imageData;
		}
		case SWT.BITMAP: {
			/* Get the basic BITMAP information */
			bm = new BITMAP();
			OS.GetObject(handle, BITMAP.sizeof, bm);
			depth = bm.bmPlanes * bm.bmBitsPixel;
			width = bm.bmWidth;
			height = bm.bmHeight;
			/* Find out whether this is a DIB or a DDB. */
			boolean isDib = (bm.bmBits != 0);
			/* Get the HDC for the device */
			long hDC = device.internal_new_GC(null);
			DIBSECTION dib = null;
			if (isDib) {
				dib = new DIBSECTION();
				OS.GetObject(handle, DIBSECTION.sizeof, dib);
			}
			/* Calculate number of colors */
			int numColors = 0;
			if (depth <= 8) {
				if (isDib) {
					numColors = dib.biClrUsed;
				} else {
					numColors = 1 << depth;
				}
			}
			/* Create the BITMAPINFO */
			byte[] bmi = null;
			BITMAPINFOHEADER bmiHeader = null;
			if (!isDib) {
				bmiHeader = new BITMAPINFOHEADER();
				bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
				bmiHeader.biWidth = width;
				bmiHeader.biHeight = -height;
				bmiHeader.biPlanes = 1;
				bmiHeader.biBitCount = (short)depth;
				bmiHeader.biCompression = OS.BI_RGB;
				bmi = new byte[BITMAPINFOHEADER.sizeof + numColors * 4];
				OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
			}

			/* Create the DC and select the bitmap */
			long hBitmapDC = OS.CreateCompatibleDC(hDC);
			long hOldBitmap = OS.SelectObject(hBitmapDC, handle);
			/* Find the size of the image and allocate data */
			int imageSize;
			if (isDib) {
				imageSize = dib.biSizeImage;
			} else {
				/* Call with null lpBits to get the image size */
				OS.GetDIBits(hBitmapDC, handle, 0, height, null, bmi, OS.DIB_RGB_COLORS);
				OS.MoveMemory(bmiHeader, bmi, BITMAPINFOHEADER.sizeof);
				imageSize = bmiHeader.biSizeImage;
			}
			byte[] data = new byte[imageSize];
			/* Get the bitmap data */
			if (isDib) {
				OS.MoveMemory(data, bm.bmBits, imageSize);
			} else {
				OS.GetDIBits(hBitmapDC, handle, 0, height, data, bmi, OS.DIB_RGB_COLORS);
			}
			/* Calculate the palette */
			PaletteData palette = null;
			if (depth <= 8) {
				RGB[] rgbs = new RGB[numColors];
				if (isDib) {
					byte[] colors = new byte[numColors * 4];
					OS.GetDIBColorTable(hBitmapDC, 0, numColors, colors);
					int colorIndex = 0;
					for (int i = 0; i < rgbs.length; i++) {
						rgbs[i] = new RGB(colors[colorIndex + 2] & 0xFF, colors[colorIndex + 1] & 0xFF, colors[colorIndex] & 0xFF);
						colorIndex += 4;
					}
				} else {
					int srcIndex = BITMAPINFOHEADER.sizeof;
					for (int i = 0; i < numColors; i++) {
						rgbs[i] = new RGB(bmi[srcIndex + 2] & 0xFF, bmi[srcIndex + 1] & 0xFF, bmi[srcIndex] & 0xFF);
						srcIndex += 4;
					}
				}
				palette = new PaletteData(rgbs);
			} else if (depth == 16) {
				palette = new PaletteData(0x7C00, 0x3E0, 0x1F);
			} else if (depth == 24) {
				palette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
			} else if (depth == 32) {
				palette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
			} else {
				SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
			}
			/* Clean up */
			OS.SelectObject(hBitmapDC, hOldBitmap);
			OS.DeleteDC(hBitmapDC);

			/* Release the HDC for the device */
			device.internal_dispose_GC(hDC, null);

			/* Construct and return the ImageData */
			ImageData imageData = new ImageData(width, height, depth, palette, 4, data);
			imageData.transparentPixel = this.transparentPixel;
			if (depth == 32) {
				byte straightData[] = new byte[imageSize];
				byte alphaData[] = new byte[width * height];
				boolean validAlpha = isDib;
				for (int ap = 0, dp = 0; validAlpha && ap < alphaData.length; ap++, dp += 4) {
					int b = data[dp    ] & 0xFF;
					int g = data[dp + 1] & 0xFF;
					int r = data[dp + 2] & 0xFF;
					int a = data[dp + 3] & 0xFF;
					alphaData[ap] = (byte) a;
					validAlpha = validAlpha && b <= a && g <= a && r <= a;
					if (a != 0) {
						straightData[dp    ] = (byte) (((b * 0xFF) + a / 2) / a);
						straightData[dp + 1] = (byte) (((g * 0xFF) + a / 2) / a);
						straightData[dp + 2] = (byte) (((r * 0xFF) + a / 2) / a);
					}
				}
				if (validAlpha) {
					imageData.data = straightData;
					imageData.alphaData = alphaData;
				}
				else {
					for (int dp = 3; dp < imageSize; dp += 4) {
						data[dp] = (byte) 0xFF;
					}
				}
			}
			return imageData;
		}
		default:
			SWT.error(SWT.ERROR_INVALID_IMAGE);
			return null;
	}
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
		return (int)handle;
	}
}

void init(int width, int height) {
	if (width <= 0 || height <= 0) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	type = SWT.BITMAP;
	long hDC = device.internal_new_GC(null);
	handle = OS.CreateCompatibleBitmap(hDC, width, height);
	/*
	* Feature in Windows.  CreateCompatibleBitmap() may fail
	* for large images.  The fix is to create a DIB section
	* in that case.
	*/
	if (handle == 0) {
		int bits = OS.GetDeviceCaps(hDC, OS.BITSPIXEL);
		int planes = OS.GetDeviceCaps(hDC, OS.PLANES);
		int depth = bits * planes;
		if (depth < 16) depth = 16;
		if (depth > 24) depth = 24;
		handle = createDIB(width, height, depth);
	}
	if (handle != 0) {
		long memDC = OS.CreateCompatibleDC(hDC);
		long hOldBitmap = OS.SelectObject(memDC, handle);
		OS.PatBlt(memDC, 0, 0, width, height, OS.PATCOPY);
		OS.SelectObject(memDC, hOldBitmap);
		OS.DeleteDC(memDC);
	}
	device.internal_dispose_GC(hDC, null);
	if (handle == 0) {
		SWT.error(SWT.ERROR_NO_HANDLES, null, device.getLastError());
	}
}

static long createDIB(int width, int height, int depth) {
	BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
	bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
	bmiHeader.biWidth = width;
	bmiHeader.biHeight = -height;
	bmiHeader.biPlanes = 1;
	bmiHeader.biBitCount = (short)depth;
	bmiHeader.biCompression = OS.BI_RGB;
	byte[] bmi = new byte[BITMAPINFOHEADER.sizeof];
	OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
	long[] pBits = new long[1];
	return OS.CreateDIBSection(0, bmi, OS.DIB_RGB_COLORS, pBits, 0, 0);
}

static ImageData indexToIndex(ImageData src, int newDepth) {
	ImageData img = new ImageData(src.width, src.height, newDepth, src.palette);

	ImageData.blit(
		src.data, src.depth, src.bytesPerLine, src.getByteOrder(), src.width, src.height,
		img.data, img.depth, img.bytesPerLine, src.getByteOrder(), img.width, img.height,
		false, false);

	img.transparentPixel = src.transparentPixel;
	img.maskPad   = src.maskPad;
	img.maskData  = src.maskData;
	img.alpha     = src.alpha;
	img.alphaData = src.alphaData;

	return img;
}

static ImageData indexToDirect(ImageData src, int newDepth, PaletteData newPalette, int newByteOrder) {
	ImageData img = new ImageData(src.width, src.height, newDepth, newPalette);

	RGB[] rgbs = src.palette.getRGBs();
	byte[] srcReds   = new byte[rgbs.length];
	byte[] srcGreens = new byte[rgbs.length];
	byte[] srcBlues  = new byte[rgbs.length];
	for (int j = 0; j < rgbs.length; j++) {
		RGB rgb = rgbs[j];
		if (rgb == null) continue;
		srcReds[j] = (byte)rgb.red;
		srcGreens[j] = (byte)rgb.green;
		srcBlues[j] = (byte)rgb.blue;
	}

	ImageData.blit(
		src.width, src.height,
		src.data, src.depth, src.bytesPerLine, src.getByteOrder(), srcReds, srcGreens, srcBlues,
		img.data, img.depth, img.bytesPerLine,       newByteOrder, newPalette.redMask, newPalette.greenMask, newPalette.blueMask);

	if (src.transparentPixel != -1) {
		img.transparentPixel = newPalette.getPixel(src.palette.getRGB(src.transparentPixel));
	}

	img.maskPad   = src.maskPad;
	img.maskData  = src.maskData;
	img.alpha     = src.alpha;
	img.alphaData = src.alphaData;

	return img;
}

static ImageData directToDirect(ImageData src, int newDepth, PaletteData newPalette, int newByteOrder) {
	ImageData img = new ImageData(src.width, src.height, newDepth, newPalette);

	ImageData.blit(
		src.data, src.depth, src.bytesPerLine, src.getByteOrder(), src.width, src.height, src.palette.redMask, src.palette.greenMask, src.palette.blueMask,
		img.data, img.depth, img.bytesPerLine,       newByteOrder, img.width, img.height, img.palette.redMask, img.palette.greenMask, img.palette.blueMask,
		false, false);

	if (src.transparentPixel != -1) {
		img.transparentPixel = img.palette.getPixel(src.palette.getRGB(src.transparentPixel));
	}

	img.maskPad   = src.maskPad;
	img.maskData  = src.maskData;
	img.alpha     = src.alpha;
	img.alphaData = src.alphaData;

	return img;
}

static long [] init(Device device, Image image, ImageData i) {
	/* Windows does not support 2-bit images. Convert to 4-bit image. */
	if (i.depth == 2) {
		i = indexToIndex(i, 4);
	}

	/* Windows does not support 16-bit palette images. Convert to RGB. */
	if ((i.depth == 16) && !i.palette.isDirect) {
		PaletteData newPalette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
		i = indexToDirect(i, 24, newPalette, ImageData.MSB_FIRST);
	}

	boolean hasAlpha = i.alpha != -1 || i.alphaData != null;

	/*
	 * Windows supports 16-bit mask of (0x7C00, 0x3E0, 0x1F),
	 * 24-bit mask of (0xFF0000, 0xFF00, 0xFF) and 32-bit mask
	 * (0x00FF0000, 0x0000FF00, 0x000000FF) as documented in
	 * MSDN BITMAPINFOHEADER.  Make sure the image is
	 * Windows-supported.
	 */
	if (i.palette.isDirect) {
		final PaletteData palette = i.palette;
		final int redMask = palette.redMask;
		final int greenMask = palette.greenMask;
		final int blueMask = palette.blueMask;
		int newDepth = i.depth;
		int newOrder = ImageData.MSB_FIRST;
		PaletteData newPalette = null;

		if (hasAlpha) {
			newDepth = 32;
			newPalette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
		}
		else {
			switch (i.depth) {
				case 8:
					/*
					 * Bug 566545. Usually each color mask selects a different part of the pixel
					 * value to encode the according color. In this common case it is rather trivial
					 * to convert an 8-bit direct color image to the Windows supported 16-bit image.
					 * However there is no enforcement for the color masks to be disjunct. For
					 * example an 8-bit image where all color masks select the same 8-bit of pixel
					 * value (mask = 0xFF and shift = 0 for all colors) results in a very efficient
					 * 8-bit gray-scale image without the need of defining a color table.
					 *
					 * That's why we need to calculate the actual required depth if all colors are
					 * stored non-overlapping which might require 24-bit instead of the usual
					 * expected 16-bit.
					 */
					int minDepth = ImageData.getChannelWidth(redMask, palette.redShift)
							+ ImageData.getChannelWidth(greenMask, palette.greenShift)
							+ ImageData.getChannelWidth(blueMask, palette.blueShift);
					if (minDepth <= 16) {
						newDepth = 16;
						newOrder = ImageData.LSB_FIRST;
						newPalette = new PaletteData(0x7C00, 0x3E0, 0x1F);
					} else {
						newDepth = 24;
						newPalette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
					}
					break;
				case 16:
					newOrder = ImageData.LSB_FIRST;
					if (!(redMask == 0x7C00 && greenMask == 0x3E0 && blueMask == 0x1F)) {
						newPalette = new PaletteData(0x7C00, 0x3E0, 0x1F);
					}
					break;
				case 24:
					if (!(redMask == 0xFF && greenMask == 0xFF00 && blueMask == 0xFF0000)) {
						newPalette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
					}
					break;
				case 32:
					if (i.getTransparencyType() != SWT.TRANSPARENCY_MASK) {
						newDepth = 24;
						newPalette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
					}
					else if (!(redMask == 0xFF00 && greenMask == 0xFF0000 && blueMask == 0xFF000000)) {
						newPalette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
					}
					break;
				default:
					SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
			}
		}
		if (newPalette != null) {
			i = directToDirect(i, newDepth, newPalette, newOrder);
		}
	}
	else if (hasAlpha) {
		PaletteData newPalette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
		i = indexToDirect(i, 32, newPalette, ImageData.MSB_FIRST);
	}
	if (i.alpha != -1) {
		int alpha = i.alpha & 0xFF;
		byte[] data = i.data;
		for (int dp = 0; dp < i.data.length; dp += 4) {
			/* pre-multiplied alpha */
			int r = ((data[dp    ] & 0xFF) * alpha) + 128;
			r = (r + (r >> 8)) >> 8;
			int g = ((data[dp + 1] & 0xFF) * alpha) + 128;
			g = (g + (g >> 8)) >> 8;
			int b = ((data[dp + 2] & 0xFF) * alpha) + 128;
			b = (b + (b >> 8)) >> 8;
			data[dp    ] = (byte) b;
			data[dp + 1] = (byte) g;
			data[dp + 2] = (byte) r;
			data[dp + 3] = (byte) alpha;
		}
	}
	else if (i.alphaData != null) {
		byte[] data = i.data;
		for (int ap = 0, dp = 0; dp < i.data.length; ap++, dp += 4) {
			/* pre-multiplied alpha */
			int a = i.alphaData[ap] & 0xFF;
			int r = ((data[dp    ] & 0xFF) * a) + 128;
			r = (r + (r >> 8)) >> 8;
			int g = ((data[dp + 1] & 0xFF) * a) + 128;
			g = (g + (g >> 8)) >> 8;
			int b = ((data[dp + 2] & 0xFF) * a) + 128;
			b = (b + (b >> 8)) >> 8;
			data[dp    ] = (byte) r;
			data[dp + 1] = (byte) g;
			data[dp + 2] = (byte) b;
			data[dp + 3] = (byte) a;
		}
	}

	/* Construct bitmap info header by hand */
	RGB[] rgbs = i.palette.getRGBs();
	BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
	bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
	bmiHeader.biWidth = i.width;
	bmiHeader.biHeight = -i.height;
	bmiHeader.biPlanes = 1;
	bmiHeader.biBitCount = (short)i.depth;
	bmiHeader.biCompression = OS.BI_RGB;
	bmiHeader.biClrUsed = rgbs == null ? 0 : rgbs.length;
	byte[] bmi;
	if (i.palette.isDirect)
		bmi = new byte[BITMAPINFOHEADER.sizeof];
	else
		bmi = new byte[BITMAPINFOHEADER.sizeof + rgbs.length * 4];
	OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
	/* Set the rgb colors into the bitmap info */
	int offset = BITMAPINFOHEADER.sizeof;
	if (!i.palette.isDirect) {
		for (RGB rgb : rgbs) {
			bmi[offset] = (byte)rgb.blue;
			bmi[offset + 1] = (byte)rgb.green;
			bmi[offset + 2] = (byte)rgb.red;
			bmi[offset + 3] = 0;
			offset += 4;
		}
	}
	long[] pBits = new long[1];
	long hDib = OS.CreateDIBSection(0, bmi, OS.DIB_RGB_COLORS, pBits, 0, 0);
	if (hDib == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	/* In case of a scanline pad other than 4, do the work to convert it */
	byte[] data = i.data;
	if (i.scanlinePad != 4 && (i.bytesPerLine % 4 != 0)) {
		data = ImageData.convertPad(data, i.width, i.height, i.depth, i.scanlinePad, 4);
	}
	OS.MoveMemory(pBits[0], data, data.length);

	long [] result = null;
	if (i.getTransparencyType() == SWT.TRANSPARENCY_MASK) {
		/* Get the HDC for the device */
		long hDC = device.internal_new_GC(null);

		/* Create the color bitmap */
		long hdcSrc = OS.CreateCompatibleDC(hDC);
		OS.SelectObject(hdcSrc, hDib);
		long hBitmap = OS.CreateCompatibleBitmap(hDC, i.width, i.height);
		if (hBitmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		long hdcDest = OS.CreateCompatibleDC(hDC);
		OS.SelectObject(hdcDest, hBitmap);
		OS.BitBlt(hdcDest, 0, 0, i.width, i.height, hdcSrc, 0, 0, OS.SRCCOPY);

		/* Release the HDC for the device */
		device.internal_dispose_GC(hDC, null);

		/* Create the mask. Windows requires icon masks to have a scanline pad of 2. */
		byte[] maskData = ImageData.convertPad(i.maskData, i.width, i.height, 1, i.maskPad, 2);
		long hMask = OS.CreateBitmap(i.width, i.height, 1, 1, maskData);
		if (hMask == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		OS.SelectObject(hdcSrc, hMask);
		OS.PatBlt(hdcSrc, 0, 0, i.width, i.height, OS.DSTINVERT);
		OS.DeleteDC(hdcSrc);
		OS.DeleteDC(hdcDest);
		OS.DeleteObject(hDib);

		if (image == null) {
			result = new long []{hBitmap, hMask};
		} else {
			/* Create the icon */
			ICONINFO info = new ICONINFO();
			info.fIcon = true;
			info.hbmColor = hBitmap;
			info.hbmMask = hMask;
			long hIcon = OS.CreateIconIndirect(info);
			if (hIcon == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			OS.DeleteObject(hBitmap);
			OS.DeleteObject(hMask);
			image.handle = hIcon;
			image.type = SWT.ICON;
		}
	} else {
		if (image == null) {
			result = new long []{hDib};
		} else {
			image.handle = hDib;
			image.type = SWT.BITMAP;
			image.transparentPixel = i.transparentPixel;
		}
	}
	return result;
}

static long [] init(Device device, Image image, ImageData source, ImageData mask) {
	/* Create a temporary image and locate the black pixel */
	ImageData imageData;
	int blackIndex = 0;
	if (source.palette.isDirect) {
		imageData = new ImageData(source.width, source.height, source.depth, source.palette);
	} else {
		RGB black = new RGB(0, 0, 0);
		RGB[] rgbs = source.getRGBs();
		if (source.transparentPixel != -1) {
			/*
			 * The source had transparency, so we can use the transparent pixel
			 * for black.
			 */
			RGB[] newRGBs = new RGB[rgbs.length];
			System.arraycopy(rgbs, 0, newRGBs, 0, rgbs.length);
			if (source.transparentPixel >= newRGBs.length) {
				/* Grow the palette with black */
				rgbs = new RGB[source.transparentPixel + 1];
				System.arraycopy(newRGBs, 0, rgbs, 0, newRGBs.length);
				for (int i = newRGBs.length; i <= source.transparentPixel; i++) {
					rgbs[i] = new RGB(0, 0, 0);
				}
			} else {
				newRGBs[source.transparentPixel] = black;
				rgbs = newRGBs;
			}
			blackIndex = source.transparentPixel;
			imageData = new ImageData(source.width, source.height, source.depth, new PaletteData(rgbs));
		} else {
			while (blackIndex < rgbs.length) {
				if (rgbs[blackIndex].equals(black)) break;
				blackIndex++;
			}
			if (blackIndex == rgbs.length) {
				/*
				 * We didn't find black in the palette, and there is no transparent
				 * pixel we can use.
				 */
				if ((1 << source.depth) > rgbs.length) {
					/* We can grow the palette and add black */
					RGB[] newRGBs = new RGB[rgbs.length + 1];
					System.arraycopy(rgbs, 0, newRGBs, 0, rgbs.length);
					newRGBs[rgbs.length] = black;
					rgbs = newRGBs;
				} else {
					/* No room to grow the palette */
					blackIndex = -1;
				}
			}
			imageData = new ImageData(source.width, source.height, source.depth, new PaletteData(rgbs));
		}
	}
	if (blackIndex == -1) {
		/* There was no black in the palette, so just copy the data over */
		System.arraycopy(source.data, 0, imageData.data, 0, imageData.data.length);
	} else {
		/* Modify the source image to contain black wherever the mask is 0 */
		int[] imagePixels = new int[imageData.width];
		int[] maskPixels = new int[mask.width];
		for (int y = 0; y < imageData.height; y++) {
			source.getPixels(0, y, imageData.width, imagePixels, 0);
			mask.getPixels(0, y, mask.width, maskPixels, 0);
			for (int i = 0; i < imagePixels.length; i++) {
				if (maskPixels[i] == 0) imagePixels[i] = blackIndex;
			}
			imageData.setPixels(0, y, source.width, imagePixels, 0);
		}
	}
	imageData.maskPad = mask.scanlinePad;
	imageData.maskData = mask.data;
	return init(device, image, imageData);
}
void init(ImageData i) {
	if (i == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, this, i);
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
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	/*
	* Create a new GC that can draw into the image.
	* Only supported for bitmaps.
	*/
	if (type != SWT.BITMAP || memGC != null) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	/* Create a compatible HDC for the device */
	long hDC = device.internal_new_GC(null);
	long imageDC = OS.CreateCompatibleDC(hDC);
	device.internal_dispose_GC(hDC, null);
	if (imageDC == 0) SWT.error(SWT.ERROR_NO_HANDLES);

	if (data != null) {
		/* Set the GCData fields */
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		if ((data.style & mask) != 0) {
			data.layout = (data.style & SWT.RIGHT_TO_LEFT) != 0 ? OS.LAYOUT_RTL : 0;
		} else {
			data.style |= SWT.LEFT_TO_RIGHT;
		}
		data.device = device;
		data.image = this;
		data.font = device.systemFont;
	}
	return imageDC;
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
	OS.DeleteDC(hDC);
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
	return handle == 0;
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
	if (transparentPixel == -1) return;
	transparentColor = -1;

	/* Get the HDC for the device */
	long hDC = device.internal_new_GC(null);

	/* Change the background color in the image */
	BITMAP bm = new BITMAP();
	OS.GetObject(handle, BITMAP.sizeof, bm);
	long hdcMem = OS.CreateCompatibleDC(hDC);
	OS.SelectObject(hdcMem, handle);
	int maxColors = 1 << bm.bmBitsPixel;
	byte[] colors = new byte[maxColors * 4];
	int numColors = OS.GetDIBColorTable(hdcMem, 0, maxColors, colors);
	int offset = transparentPixel * 4;
	colors[offset] = (byte)color.getBlue();
	colors[offset + 1] = (byte)color.getGreen();
	colors[offset + 2] = (byte)color.getRed();
	OS.SetDIBColorTable(hdcMem, 0, numColors, colors);
	OS.DeleteDC(hdcMem);

	/* Release the HDC for the device */
	device.internal_dispose_GC(hDC, null);
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
 * @param handle the OS handle for the image
 * @return a new image object containing the specified device, type and handle
 *
 * @noreference This method is not intended to be referenced by clients.
 */
public static Image win32_new(Device device, int type, long handle) {
	Image image = new Image(device);
	image.type = type;
	image.handle = handle;
	return image;
}

}
