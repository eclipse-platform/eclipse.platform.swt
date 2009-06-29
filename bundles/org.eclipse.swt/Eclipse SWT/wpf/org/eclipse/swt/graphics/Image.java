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


import org.eclipse.swt.internal.wpf.*;
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
	 * the handle to the OS image resource
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public int handle;
		
	/**
	 * specifies the transparent pixel
	 */
	int transparentPixel = -1;
	
	/**
	 * the GC which is drawing on the image
	 */
	GC memGC;
	
	/**
	 * the alpha data for the image
	 */
	byte[] alphaData;
	
	/**
	 * the global alpha value to be used for every pixel
	 */
	int alpha = -1;
	
	/**
	 * the image data used to create this image if it is a
	 * icon. Used only in WinCE
	 */
	ImageData data;
	
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
	Rectangle rect = srcImage.getBounds();
	switch (flag) {
		case SWT.IMAGE_COPY: {
			type = srcImage.type;
			handle = OS.BitmapSource_Clone(srcImage.handle);
			transparentPixel = srcImage.transparentPixel;
			alpha = srcImage.alpha;
			if (srcImage.alphaData != null) {
				alphaData = new byte[srcImage.alphaData.length];
				System.arraycopy(srcImage.alphaData, 0, alphaData, 0, alphaData.length);
			}
			break;
		}
		case SWT.IMAGE_DISABLE: {
			ImageData data = srcImage.getImageData();
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
					if (!(pixel == data.transparentPixel || (mask != null && maskScanline[x] == 0))) {
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
public Image (Device device, InputStream stream) {
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
public Image (Device device, String filename) {
	super(device);
	if (filename == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	try {
		handle = OS.gcnew_BitmapImage();
		if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		int length = filename.length();
		char[] chars = new char[length + 1];
		filename.getChars(0, length, chars, 0);
		int str = OS.gcnew_String(chars);
		if (str == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		if (!OS.File_Exists(str)) SWT.error(SWT.ERROR_IO);
		int uri = OS.gcnew_Uri(str, OS.UriKind_RelativeOrAbsolute);
		if (uri == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		OS.BitmapImage_BeginInit(handle);
		OS.BitmapImage_CreateOptions(handle, OS.BitmapCreateOptions_PreservePixelFormat);
		OS.BitmapImage_UriSource(handle, uri);
		OS.BitmapImage_EndInit(handle);
		if (OS.Freezable_CanFreeze(handle)) OS.Freezable_Freeze(handle);
		OS.GCHandle_Free(uri);
		OS.GCHandle_Free(str);
		return;
	} catch (SWTException e) {}
	init(new ImageData(filename));
	init();
}

void destroy() {
	if (memGC != null) memGC.dispose();
	OS.GCHandle_Free(handle);
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
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Image)) return false;
	Image image = (Image) object;
	return device == image.device && handle == image.handle;
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
	//TODO implement Image.getBackground()
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
	if (width == -1 || height == -1) {
		width = OS.BitmapSource_PixelWidth(handle);
		height = OS.BitmapSource_PixelHeight(handle);
	}
	return new Rectangle(0, 0, width, height);
}

Point getDPI () {
	//TODO
	return new Point (96, 96);
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
	int format = OS.BitmapSource_Format(handle);
	int depth = OS.PixelFormat_BitsPerPixel(format);
	int width = OS.BitmapSource_PixelWidth(handle);
	int height = OS.BitmapSource_PixelHeight(handle);
	int scanlinePad = DEFAULT_SCANLINE_PAD;
	int bytesPerLine = (((width * depth + 7) / 8) + (scanlinePad - 1))	/ scanlinePad * scanlinePad;
	byte[] buffer = new byte[bytesPerLine * height];
	int rect = OS.Int32Rect_Empty();
	OS.BitmapSource_CopyPixels(handle, rect, buffer, buffer.length, bytesPerLine);
	OS.GCHandle_Free(rect);
	PaletteData paletteData = null;
	int palette = OS.BitmapSource_Palette(handle);
	if (palette != 0) {
		int colors = OS.BitmapPalette_Colors(palette);
		int count = OS.ColorList_Count(colors);
		RGB[] rgbs = new RGB[count];
		paletteData = new PaletteData(rgbs);
		if (count != 0) {
			int index = 0;
			int enumerator = OS.ColorList_GetEnumerator(colors);
			while (OS.IEnumerator_MoveNext(enumerator)) {
				int color = OS.ColorList_Current(enumerator);
				rgbs[index++] = new RGB(OS.Color_R(color) & 0xFF, OS.Color_G(color) & 0xFF, OS.Color_B(color) & 0xFF);
				OS.GCHandle_Free(color);
			}
			OS.GCHandle_Free(enumerator);
		}
		OS.GCHandle_Free(colors);
		OS.GCHandle_Free(palette);
	} else {
		int[] formats = {
			OS.PixelFormats_Bgr555(),
			OS.PixelFormats_Bgr565(),
			OS.PixelFormats_Bgr24(),
			OS.PixelFormats_Rgb24(),
			OS.PixelFormats_Bgr32(),
			OS.PixelFormats_Bgra32(),
			OS.PixelFormats_Pbgra32(),
			OS.PixelFormats_Bgr101010(),
		};
		for (int i = 0; i < formats.length; i++) {
			if (OS.Object_Equals(format, formats[i])) {
				switch (i) {
					case 0: paletteData = new PaletteData(0x7C00, 0x3E0, 0x1F); break;
					case 1: paletteData = new PaletteData(0xF800, 0x7E0, 0x1F); break;
					case 2: paletteData = new PaletteData(0xFF, 0xFF00, 0xFF0000); break;
					case 3: paletteData = new PaletteData(0xFF0000, 0xFF00, 0xFF); break;
					case 4:
					case 5:
					case 6: paletteData = new PaletteData(0xFF00, 0xFF0000, 0xFF000000); break;
					case 7: paletteData = new PaletteData(0x3FF, 0xFFC00, 0x3FF0000); break;
				}
			}
			OS.GCHandle_Free(formats[i]);
		}
	}
	OS.GCHandle_Free(format);
	ImageData data = new ImageData(width, height, depth, paletteData, scanlinePad, buffer);
	data.transparentPixel = transparentPixel;
	if (transparentPixel == -1 && type == SWT.ICON) {
		/* Get the icon mask data */
		int maskPad = 2;
		int maskBpl = (((width + 7) / 8) + (maskPad - 1)) / maskPad * maskPad;
		byte[] maskData = new byte[height * maskBpl];
		int offset = 3, maskOffset = 0;
		for (int y = 0; y<height; y++) {
			for (int x = 0; x<width; x++) {
				if (buffer[offset] != 0) {
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
	data.alpha = alpha;
	if (alpha == -1 && alphaData != null) {
		data.alphaData = new byte[alphaData.length];
		System.arraycopy(alphaData, 0, data.alphaData, 0, alphaData.length);
	}
	return data;
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
	return handle;
}

void init(int width, int height) {
	if (width <= 0 || height <= 0) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	type = SWT.BITMAP;
	Point dpi = getDPI();
	int pixelFormat = OS.PixelFormats_Bgr24();
	int stride = width * 3;
	byte[] buffer = new byte[stride * height];
	for (int i = 0; i < buffer.length; i++) {
		buffer[i] = (byte)0xFF;
	}
	handle = OS.BitmapSource_Create(width, height, dpi.x, dpi.y, pixelFormat, 0, buffer, buffer.length, stride);	
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.GCHandle_Free(pixelFormat);
	if (OS.Freezable_CanFreeze(handle)) OS.Freezable_Freeze(handle);
}

void init(ImageData data) {
	PaletteData palette = data.palette;
	if (!(((data.depth == 1 || data.depth == 2 || data.depth == 4 || data.depth == 8) && !palette.isDirect) ||
		((data.depth == 8) || (data.depth == 16 || data.depth == 24 || data.depth == 32) && palette.isDirect)))
			SWT.error (SWT.ERROR_UNSUPPORTED_DEPTH);
	int width = data.width;
	int height = data.height;
	int redMask = palette.redMask;
	int greenMask = palette.greenMask;
	int blueMask = palette.blueMask;
	ImageData newData = null;
	int pixelFormat = 0;
	boolean transparent = false;
	if (data.maskData != null) {
		transparent= true;
	} else {
		if (data.transparentPixel != -1) {
			transparent = palette.isDirect;
		} else {
			if (data.alpha != -1) {
				transparent = palette.isDirect;
			} else {
				transparent = data.alphaData != null;
			}
		}
	}
	if (transparent) {
		pixelFormat = OS.PixelFormats_Bgra32();
		if (!(palette.isDirect && data.depth == 32 && redMask == 0xFF00 && greenMask == 0xFF0000 && blueMask == 0xFF000000)) {
			newData = new ImageData(width, height, 32, new PaletteData(0xFF00, 0xFF0000, 0xFF000000));
		}
	} else {
		switch (data.depth) {
			case 1: pixelFormat = OS.PixelFormats_Indexed1(); break;
			case 2: pixelFormat = OS.PixelFormats_Indexed2(); break;
			case 4: pixelFormat = OS.PixelFormats_Indexed4(); break;
			case 8:
				if (!palette.isDirect) {
					pixelFormat = OS.PixelFormats_Indexed8();
				} else {
					pixelFormat = OS.PixelFormats_Bgr32();
					newData = new ImageData(data.width, data.height, 32, new PaletteData(0xFF00, 0xFF0000, 0xFF000000));
				}
				break;
			case 16:
				if (redMask == 0x7C00 && greenMask == 0x3E0 && blueMask == 0x1F) {
					pixelFormat = OS.PixelFormats_Bgr555();
				} else if (redMask == 0xF800 && greenMask == 0x7E0 && blueMask == 0x1F) {
					pixelFormat = OS.PixelFormats_Bgr565();
				} else {
					pixelFormat = OS.PixelFormats_Bgr555();
					newData = new ImageData(data.width, data.height, 16, new PaletteData(0x7C00, 0x3E0, 0x1F));
				}
				break;
			case 24:
				if (redMask == 0xFF && greenMask == 0xFF00 && blueMask == 0xFF0000) {
					pixelFormat = OS.PixelFormats_Bgr24();
				} else if (redMask == 0xFF0000 && greenMask == 0xFF00 && blueMask == 0xFF) {
					pixelFormat = OS.PixelFormats_Rgb24();
				} else {
					pixelFormat = OS.PixelFormats_Bgr24();
					newData = new ImageData(data.width, data.height, 24, new PaletteData(0xFF, 0xFF00, 0xFF0000));
				}
				break;
			case 32:
				if (redMask == 0x3FF && greenMask == 0xFFC00 && blueMask == 0x3FF0000) {
					pixelFormat = OS.PixelFormats_Bgr101010();
				} else if (redMask == 0xFF00 && greenMask == 0xFF0000 && blueMask == 0xFF000000) {
					pixelFormat = OS.PixelFormats_Bgr32();
				} else {
					pixelFormat = OS.PixelFormats_Bgr32();
					newData = new ImageData(data.width, data.height, 32, new PaletteData(0xFF00, 0xFF0000, 0xFF000000));
				}
				break;
		}
	}
	if (newData != null) {
		PaletteData newPalette = newData.palette;
		if (palette.isDirect) {
			ImageData.blit(ImageData.BLIT_SRC, 
					data.data, data.depth, data.bytesPerLine, data.getByteOrder(), 0, 0, width, height, redMask, greenMask, blueMask,
					ImageData.ALPHA_OPAQUE, null, 0, 0, 0,
					newData.data, newData.depth, newData.bytesPerLine, newData.getByteOrder(), 0, 0, width, height, newPalette.redMask, newPalette.greenMask, newPalette.blueMask,
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
				data.data, data.depth, data.bytesPerLine, data.getByteOrder(), 0, 0, width, height, srcReds, srcGreens, srcBlues,
				ImageData.ALPHA_OPAQUE, null, 0, 0, 0,
				newData.data, newData.depth, newData.bytesPerLine, newData.getByteOrder(), 0, 0, width, height, newPalette.redMask, newPalette.greenMask, newPalette.blueMask,
				false, false);
		}
		if (data.transparentPixel != -1) {
			newData.transparentPixel = newPalette.getPixel(palette.getRGB(data.transparentPixel));
		}
		newData.maskPad = data.maskPad;
		newData.maskData = data.maskData;
		newData.alpha = data.alpha;
		newData.alphaData = data.alphaData;
		data = newData;
		palette = data.palette;
	}
	int bitmapPalette = 0;
	if (!palette.isDirect) {
		if (data.transparentPixel != -1) {
			transparentPixel = data.transparentPixel;
		} else {
			alpha = data.alpha;
		}
		RGB[] rgbs = palette.colors;
		int list = OS.gcnew_ColorList(rgbs.length);
		for (int i = 0; i < rgbs.length; i++) {
			RGB rgb = rgbs[i];
			byte alpha;
			if (data.transparentPixel != -1) {
				alpha = (byte)(i == data.transparentPixel ? 0 : 0xFF);
			} else {
				alpha = (byte)(data.alpha & 0xFF);
			}
			int color = OS.Color_FromArgb(alpha, (byte)rgb.red, (byte)rgb.green, (byte)rgb.blue);
			OS.ColorList_Add(list, color);
			OS.GCHandle_Free(color);
		}
		bitmapPalette = OS.gcnew_BitmapPalette(list);
		OS.GCHandle_Free(list);
	}
	type = SWT.BITMAP;
	if (transparent) {
		if (data.maskData != null || data.transparentPixel != -1) {
			this.type = data.transparentPixel != -1 ? SWT.BITMAP : SWT.ICON;
			transparentPixel = data.transparentPixel;
			ImageData maskImage = data.getTransparencyMask();
			byte[] maskData = maskImage.data;
			int maskBpl = maskImage.bytesPerLine;
			int offset = 3, maskOffset = 0;
			for (int y = 0; y<height; y++) {
				for (int x = 0; x<width; x++) {
					data.data[offset] = ((maskData[maskOffset + (x >> 3)]) & (1 << (7 - (x & 0x7)))) != 0 ? (byte)0xff : 0;
					offset += 4;
				}
				maskOffset += maskBpl;
			}
		} else if (data.alpha != -1) {
			alpha = data.alpha;
			for (int i = 3, j = 0; i < data.data.length; i+=4, j++) {
				data.data[i] = (byte)alpha;
			}
		} else {
			int length = data.alphaData.length;
			alphaData = new byte[length];
			System.arraycopy(data.alphaData, 0, alphaData, 0, length);
			for (int i = 3, j = 0; i < data.data.length; i+=4, j++) {
				data.data[i] = alphaData[j];
			}
		}
	}
	Point dpi = getDPI();
	handle = OS.BitmapSource_Create(width, height, dpi.x, dpi.y, pixelFormat, bitmapPalette, data.data, data.data.length, data.bytesPerLine);
	if (OS.Freezable_CanFreeze(handle)) OS.Freezable_Freeze(handle);
	OS.GCHandle_Free(pixelFormat);
	if (bitmapPalette != 0) OS.GCHandle_Free(bitmapPalette);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
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
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	/*
	* Create a new GC that can draw into the image.
	* Only supported for bitmaps.
	*/
	if (type != SWT.BITMAP || memGC != null) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (width == -1 || height == -1) {
		width = OS.BitmapSource_PixelWidth(handle);
		height = OS.BitmapSource_PixelHeight(handle);
	}
	
	int visual = OS.gcnew_DrawingVisual();
	if (visual == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int rect = OS.gcnew_Rect(0, 0, width, height);
	int geometry = OS.gcnew_RectangleGeometry(rect);
	OS.ContainerVisual_Clip (visual, geometry);
	int dc = OS.DrawingVisual_RenderOpen(visual);
	if (dc == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.DrawingContext_DrawImage(dc, handle, rect);
	OS.GCHandle_Free(rect);
	OS.GCHandle_Free(geometry);
	if (data != null) {
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		if ((data.style & mask) == 0) {
			data.style |= SWT.LEFT_TO_RIGHT;
		}
		data.device = device;
		data.image = this;
		data.background = OS.Colors_White;
		data.foreground = OS.Colors_Black;
		data.font = device.systemFont;
		data.visual = visual;
	}
	return dc;
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
public void internal_dispose_GC (int dc, GCData data) {
	OS.DrawingContext_Close(dc);
	Point dpi = getDPI();
	int pixelFormat = OS.PixelFormats_Pbgra32();
	int	renderHandle = OS.gcnew_RenderTargetBitmap(width, height, dpi.x, dpi.y, pixelFormat);
	OS.GCHandle_Free(pixelFormat);
	if (renderHandle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.RenderTargetBitmap_Render(renderHandle, data.visual);
	OS.GCHandle_Free(data.visual);
	OS.GCHandle_Free(dc);
	int format = OS.BitmapSource_Format(handle);
	int palette = OS.BitmapSource_Palette(handle);
	OS.GCHandle_Free(handle);
	handle = OS.gcnew_FormatConvertedBitmap(renderHandle, format, palette, 100);
	OS.GCHandle_Free(palette);
	OS.GCHandle_Free(format);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.GCHandle_Free(renderHandle);
	if (OS.Freezable_CanFreeze(handle)) OS.Freezable_Freeze(handle);
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
	//TODO implement Image.setBackground()
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
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
 */
public static Image wpf_new(Device device, int type, int handle) {
	Image image = new Image(device);
	image.type = type;
	image.handle = handle;
	return image;
}

}
