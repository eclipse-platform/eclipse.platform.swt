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
package org.eclipse.swt.graphics;


import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.swing.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.DPIUtil.*;
import org.eclipse.swt.internal.swing.*;
import org.eclipse.swt.widgets.*;

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
	public BufferedImage handle;

//	/**
//	 * specifies the transparent pixel
//	 */
//	int transparentPixel = -1;

/**
 * Prevents uninitialized instances from being created outside the package.
 */
Image () {
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
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, width, height);
	if (device.tracking) device.new_Object(this);
}

/**
 * Constructs a new instance of this class based on the
 * provided image, with an appearance that varies depending
 * on the value of the flag. The possible flag values are:
 * <dl>
 * <dt><b>IMAGE_COPY</b></dt>
 * <dd>the result is an identical copy of srcImage</dd>
 * <dt><b>IMAGE_DISABLE</b></dt>
 * <dd>the result is a copy of srcImage which has a <em>disabled</em> look</dd>
 * <dt><b>IMAGE_GRAY</b></dt>
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
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	if (srcImage == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (srcImage.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	switch (flag) {
		case SWT.IMAGE_COPY: {
		  handle = new BufferedImage(srcImage.handle.getWidth(), srcImage.handle.getHeight(), BufferedImage.TYPE_INT_ARGB);
		  Graphics g = handle.getGraphics();
		  g.drawImage(srcImage.handle, 0, 0, null);
		  g.dispose();
			if (device.tracking) device.new_Object(this);
			return;
		}
		case SWT.IMAGE_DISABLE:
    case SWT.IMAGE_GRAY: {
      // TODO: test the method below and see if results are different. If so, associate each method to a different case.
//      ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
//      ColorConvertOp op = new ColorConvertOp(cs, null);
//      bufferedImage = op.filter(bufferedImage, null);
      // ImageIcon is used to ensure that the image is loaded
      java.awt.Image image = new ImageIcon(GrayFilter.createDisabledImage(srcImage.handle)).getImage();
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GraphicsDevice gs = ge.getDefaultScreenDevice();
      GraphicsConfiguration gc = gs.getDefaultConfiguration();
      handle = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), srcImage.handle.getColorModel().getTransparency());
      Graphics g = handle.createGraphics();
      g.drawImage(image, 0, 0, null);
      g.dispose();
//      init (device, newData);
			if (device.tracking) device.new_Object(this);
			return;
		}
		default:
      SWT.error(SWT.ERROR_INVALID_IMAGE);
	}
  // TODO: Set image data?
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
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, bounds.width, bounds.height);
	if (device.tracking) device.new_Object(this);
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
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, data);
	if (device.tracking) device.new_Object(this);
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
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (mask == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (source.width != mask.width || source.height != mask.height) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	mask = ImageData.convertMask(mask);
	init(device, this, source, mask);
	if (device.tracking) device.new_Object(this);
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
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, new ImageData(stream));
	if (device.tracking) device.new_Object(this);
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
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, new ImageData(filename));
	if (device.tracking) device.new_Object(this);
}

///**
// * Create a DIB from a DDB without using GetDIBits. Note that
// * the DDB should not be selected into a HDC.
// */
//int createDIBFromDDB(int hDC, int hBitmap, int width, int height) {
//
//	/* Determine the DDB depth */
//	int bits = OS.GetDeviceCaps (hDC, OS.BITSPIXEL);
//	int planes = OS.GetDeviceCaps (hDC, OS.PLANES);
//	int depth = bits * planes;
//
//	/* Determine the DIB palette */
//	boolean isDirect = depth > 8;
//	RGB[] rgbs = null;
//	if (!isDirect) {
//		int numColors = 1 << depth;
//		byte[] logPalette = new byte[4 * numColors];
//		OS.GetPaletteEntries(device.hPalette, 0, numColors, logPalette);
//		rgbs = new RGB[numColors];
//		for (int i = 0; i < numColors; i++) {
//			rgbs[i] = new RGB(logPalette[i] & 0xFF, logPalette[i + 1] & 0xFF, logPalette[i + 2] & 0xFF);
//		}
//	}
//
//	boolean useBitfields = OS.IsWinCE && (depth == 16 || depth == 32);
//	BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
//	bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
//	bmiHeader.biWidth = width;
//	bmiHeader.biHeight = -height;
//	bmiHeader.biPlanes = 1;
//	bmiHeader.biBitCount = (short)depth;
//	if (useBitfields) bmiHeader.biCompression = OS.BI_BITFIELDS;
//	else bmiHeader.biCompression = OS.BI_RGB;
//	byte[] bmi;
//	if (isDirect) bmi = new byte[BITMAPINFOHEADER.sizeof + (useBitfields ? 12 : 0)];
//	else  bmi = new byte[BITMAPINFOHEADER.sizeof + rgbs.length * 4];
//	OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
//
//	/* Set the rgb colors into the bitmap info */
//	int offset = BITMAPINFOHEADER.sizeof;
//	if (isDirect) {
//		if (useBitfields) {
//			int redMask = 0;
//			int greenMask = 0;
//			int blueMask = 0;
//			switch (depth) {
//				case 16:
//					redMask = 0x7C00;
//					greenMask = 0x3E0;
//					blueMask = 0x1F;
//					/* little endian */
//					bmi[offset] = (byte)((redMask & 0xFF) >> 0);
//					bmi[offset + 1] = (byte)((redMask & 0xFF00) >> 8);
//					bmi[offset + 2] = (byte)((redMask & 0xFF0000) >> 16);
//					bmi[offset + 3] = (byte)((redMask & 0xFF000000) >> 24);
//					bmi[offset + 4] = (byte)((greenMask & 0xFF) >> 0);
//					bmi[offset + 5] = (byte)((greenMask & 0xFF00) >> 8);
//					bmi[offset + 6] = (byte)((greenMask & 0xFF0000) >> 16);
//					bmi[offset + 7] = (byte)((greenMask & 0xFF000000) >> 24);
//					bmi[offset + 8] = (byte)((blueMask & 0xFF) >> 0);
//					bmi[offset + 9] = (byte)((blueMask & 0xFF00) >> 8);
//					bmi[offset + 10] = (byte)((blueMask & 0xFF0000) >> 16);
//					bmi[offset + 11] = (byte)((blueMask & 0xFF000000) >> 24);
//					break;
//				case 32:
//					redMask = 0xFF00;
//					greenMask = 0xFF0000;
//					blueMask = 0xFF000000;
//					/* big endian */
//					bmi[offset] = (byte)((redMask & 0xFF000000) >> 24);
//					bmi[offset + 1] = (byte)((redMask & 0xFF0000) >> 16);
//					bmi[offset + 2] = (byte)((redMask & 0xFF00) >> 8);
//					bmi[offset + 3] = (byte)((redMask & 0xFF) >> 0);
//					bmi[offset + 4] = (byte)((greenMask & 0xFF000000) >> 24);
//					bmi[offset + 5] = (byte)((greenMask & 0xFF0000) >> 16);
//					bmi[offset + 6] = (byte)((greenMask & 0xFF00) >> 8);
//					bmi[offset + 7] = (byte)((greenMask & 0xFF) >> 0);
//					bmi[offset + 8] = (byte)((blueMask & 0xFF000000) >> 24);
//					bmi[offset + 9] = (byte)((blueMask & 0xFF0000) >> 16);
//					bmi[offset + 10] = (byte)((blueMask & 0xFF00) >> 8);
//					bmi[offset + 11] = (byte)((blueMask & 0xFF) >> 0);
//					break;
//				default:
//					SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
//			}
//		}
//	} else {
//		for (int j = 0; j < rgbs.length; j++) {
//			bmi[offset] = (byte)rgbs[j].blue;
//			bmi[offset + 1] = (byte)rgbs[j].green;
//			bmi[offset + 2] = (byte)rgbs[j].red;
//			bmi[offset + 3] = 0;
//			offset += 4;
//		}
//	}
//	int[] pBits = new int[1];
//	int hDib = OS.CreateDIBSection(0, bmi, OS.DIB_RGB_COLORS, pBits, 0, 0);
//	if (hDib == 0) SWT.error(SWT.ERROR_NO_HANDLES);
//
//	/* Bitblt DDB into DIB */
//	int hdcSource = OS.CreateCompatibleDC(hDC);
//	int hdcDest = OS.CreateCompatibleDC(hDC);
//	int hOldSrc = OS.SelectObject(hdcSource, hBitmap);
//	int hOldDest = OS.SelectObject(hdcDest, hDib);
//	OS.BitBlt(hdcDest, 0, 0, width, height, hdcSource, 0, 0, OS.SRCCOPY);
//	OS.SelectObject(hdcSource, hOldSrc);
//	OS.SelectObject(hdcDest, hOldDest);
//	OS.DeleteDC(hdcSource);
//	OS.DeleteDC(hdcDest);
//
//	return hDib;
//}

public Image(Display display, AutoScaleImageDataProvider autoScaleImageDataProvider) {
	throw new UnsupportedOperationException("Not implemented yet");
}

public Image(Device device, ImageDataProvider imageDataProvider) {
	throw new UnsupportedOperationException("Not implemented yet");
}

/**
 * Disposes of the operating system resources associated with
 * the image. Applications must dispose of all images which
 * they allocate.
 */
@Override
public void dispose () {
	if (handle == null) return;
	if (device.isDisposed()) return;
	handle = null;
	if (device.tracking) device.dispose_Object(this);
	device = null;
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
  // TODO: see setBackground...
  return null;
//	if (transparentPixel == -1) return null;
//
//	/* Get the HDC for the device */
//	int hDC = device.internal_new_GC(null);
//
//	/* Compute the background color */
//	BITMAP bm = new BITMAP();
//	OS.GetObject(handle, BITMAP.sizeof, bm);
//	int hdcMem = OS.CreateCompatibleDC(hDC);
//	int hOldObject = OS.SelectObject(hdcMem, handle);
//	int red = 0, green = 0, blue = 0;
//	if (bm.bmBitsPixel <= 8)  {
//		if (OS.IsWinCE) {
//			byte[] pBits = new byte[1];
//			OS.MoveMemory(pBits, bm.bmBits, 1);
//			byte oldValue = pBits[0];
//			int mask = (0xFF << (8 - bm.bmBitsPixel)) & 0x00FF;
//			pBits[0] = (byte)((transparentPixel << (8 - bm.bmBitsPixel)) | (pBits[0] & ~mask));
//			OS.MoveMemory(bm.bmBits, pBits, 1);
//			int color = OS.GetPixel(hdcMem, 0, 0);
//       		pBits[0] = oldValue;
//       		OS.MoveMemory(bm.bmBits, pBits, 1);
//			blue = (color & 0xFF0000) >> 16;
//			green = (color & 0xFF00) >> 8;
//			red = color & 0xFF;
//		} else {
//			byte[] color = new byte[4];
//			OS.GetDIBColorTable(hdcMem, transparentPixel, 1, color);
//			blue = color[0] & 0xFF;
//			green = color[1] & 0xFF;
//			red = color[2] & 0xFF;
//		}
//	} else {
//		switch (bm.bmBitsPixel) {
//			case 16:
//				blue = (transparentPixel & 0x1F) << 3;
//				green = (transparentPixel & 0x3E0) >> 2;
//				red = (transparentPixel & 0x7C00) >> 7;
//				break;
//			case 24:
//				blue = (transparentPixel & 0xFF0000) >> 16;
//				green = (transparentPixel & 0xFF00) >> 8;
//				red = transparentPixel & 0xFF;
//				break;
//			case 32:
//				blue = (transparentPixel & 0xFF000000) >>> 24;
//				green = (transparentPixel & 0xFF0000) >> 16;
//				red = (transparentPixel & 0xFF00) >> 8;
//				break;
//			default:
//				return null;
//		}
//	}
//	OS.SelectObject(hdcMem, hOldObject);
//	OS.DeleteDC(hdcMem);
//
//	/* Release the HDC for the device */
//	device.internal_dispose_GC(hDC, null);
//	return Color.win32_new(device, 0x02000000 | (blue << 16) | (green << 8) | red);
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
  return new Rectangle(0, 0, handle.getWidth(), handle.getHeight());
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
  ColorModel colorModel = handle.getColorModel();
  PaletteData paletteData = new PaletteData(0xFF0000, 0xFF00, 0xFF);
  int width = handle.getWidth();
  ImageData imageData = new ImageData(width, handle.getHeight(), colorModel.getPixelSize(), paletteData);
  int height = handle.getHeight();
  byte[] maskData = new byte[(width + 7) / 8 * height];
  for(int x=width-1; x >= 0; x--) {
    for(int y=height-1; y >= 0; y--) {
      int rgb = handle.getRGB(x, y);
      int pixel = paletteData.getPixel(new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF));
      imageData.setPixel(x, y, pixel);
      int alpha = (rgb >> 24) & 0xFF;
      imageData.setAlpha(x, y, alpha);
      if(alpha != 0) {
        int index = x + y * ((width + 7) / 8) * 8;
        maskData[index / 8] |= (byte)(1 << (7 - (index % 8)));
      }
    }
  }
  imageData.maskPad = 1;
  imageData.maskData = maskData;
//  ByteArrayOutputStream baos = new ByteArrayOutputStream();
//  ImageWriter writer = (ImageWriter)ImageIO.getImageWritersBySuffix("jpg").next();
//  try {
//    writer.setOutput(ImageIO.createImageOutputStream(baos));
//    writer.write(handle);
//    return new ImageData(new ByteArrayInputStream(baos.toByteArray()));
//  } catch(Exception e) {
//    e.printStackTrace();
//  }
  return imageData;
//	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
//	BITMAP bm;
//	int depth, width, height;
//	switch (type) {
//		case SWT.ICON: {
//			if (OS.IsWinCE) return data;
//			ICONINFO info = new ICONINFO();
//			if (OS.IsWinCE) SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
//			OS.GetIconInfo(handle, info);
//			/* Get the basic BITMAP information */
//			int hBitmap = info.hbmColor;
//			if (hBitmap == 0) hBitmap = info.hbmMask;
//			bm = new BITMAP();
//			OS.GetObject(hBitmap, BITMAP.sizeof, bm);
//			depth = bm.bmPlanes * bm.bmBitsPixel;
//			width = bm.bmWidth;
//			if (hBitmap == info.hbmMask) bm.bmHeight /= 2;
//			height = bm.bmHeight;
//			int numColors = 0;
//			if (depth <= 8) numColors = 1 << depth;
//			/* Create the BITMAPINFO */
//			BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
//			bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
//			bmiHeader.biWidth = width;
//			bmiHeader.biHeight = -height;
//			bmiHeader.biPlanes = 1;
//			bmiHeader.biBitCount = (short)depth;
//			bmiHeader.biCompression = OS.BI_RGB;
//			byte[] bmi = new byte[BITMAPINFOHEADER.sizeof + numColors * 4];
//			OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
//
//			/* Get the HDC for the device */
//			int hDC = device.internal_new_GC(null);
//
//			/* Create the DC and select the bitmap */
//			int hBitmapDC = OS.CreateCompatibleDC(hDC);
//			int hOldBitmap = OS.SelectObject(hBitmapDC, hBitmap);
//			/* Select the palette if necessary */
//			int oldPalette = 0;
//			if (depth <= 8) {
//				int hPalette = device.hPalette;
//				if (hPalette != 0) {
//					oldPalette = OS.SelectPalette(hBitmapDC, hPalette, false);
//					OS.RealizePalette(hBitmapDC);
//				}
//			}
//			/* Find the size of the image and allocate data */
//			int imageSize;
//			/* Call with null lpBits to get the image size */
//			if (OS.IsWinCE) SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
//			OS.GetDIBits(hBitmapDC, hBitmap, 0, height, 0, bmi, OS.DIB_RGB_COLORS);
//			OS.MoveMemory(bmiHeader, bmi, BITMAPINFOHEADER.sizeof);
//			imageSize = bmiHeader.biSizeImage;
//			byte[] data = new byte[imageSize];
//			/* Get the bitmap data */
//			int hHeap = OS.GetProcessHeap();
//			int lpvBits = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, imageSize);
//			if (OS.IsWinCE) SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
//			OS.GetDIBits(hBitmapDC, hBitmap, 0, height, lpvBits, bmi, OS.DIB_RGB_COLORS);
//			OS.MoveMemory(data, lpvBits, imageSize);
//			/* Calculate the palette */
//			PaletteData palette = null;
//			if (depth <= 8) {
//				RGB[] rgbs = new RGB[numColors];
//				int srcIndex = 40;
//				for (int i = 0; i < numColors; i++) {
//					rgbs[i] = new RGB(bmi[srcIndex + 2] & 0xFF, bmi[srcIndex + 1] & 0xFF, bmi[srcIndex] & 0xFF);
//					srcIndex += 4;
//				}
//				palette = new PaletteData(rgbs);
//			} else if (depth == 16) {
//				palette = new PaletteData(0x7C00, 0x3E0, 0x1F);
//			} else if (depth == 24) {
//				palette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
//			} else if (depth == 32) {
//				palette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
//			} else {
//				SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
//			}
//
//			/* Do the mask */
//			byte [] maskData = null;
//			if (info.hbmColor == 0) {
//				/* Do the bottom half of the mask */
//				maskData = new byte[imageSize];
//				if (OS.IsWinCE) SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
//				OS.GetDIBits(hBitmapDC, hBitmap, height, height, lpvBits, bmi, OS.DIB_RGB_COLORS);
//				OS.MoveMemory(maskData, lpvBits, imageSize);
//			} else {
//				/* Do the entire mask */
//				/* Create the BITMAPINFO */
//				bmiHeader = new BITMAPINFOHEADER();
//				bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
//				bmiHeader.biWidth = width;
//				bmiHeader.biHeight = -height;
//				bmiHeader.biPlanes = 1;
//				bmiHeader.biBitCount = 1;
//				bmiHeader.biCompression = OS.BI_RGB;
//				bmi = new byte[BITMAPINFOHEADER.sizeof + 8];
//				OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
//
//				/* First color black, second color white */
//				int offset = BITMAPINFOHEADER.sizeof;
//				bmi[offset + 4] = bmi[offset + 5] = bmi[offset + 6] = (byte)0xFF;
//				bmi[offset + 7] = 0;
//				OS.SelectObject(hBitmapDC, info.hbmMask);
//				/* Call with null lpBits to get the image size */
//				if (OS.IsWinCE) SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
//				OS.GetDIBits(hBitmapDC, info.hbmMask, 0, height, 0, bmi, OS.DIB_RGB_COLORS);
//				OS.MoveMemory(bmiHeader, bmi, BITMAPINFOHEADER.sizeof);
//				imageSize = bmiHeader.biSizeImage;
//				maskData = new byte[imageSize];
//				int lpvMaskBits = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, imageSize);
//				if (OS.IsWinCE) SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
//				OS.GetDIBits(hBitmapDC, info.hbmMask, 0, height, lpvMaskBits, bmi, OS.DIB_RGB_COLORS);
//				OS.MoveMemory(maskData, lpvMaskBits, imageSize);
//				OS.HeapFree(hHeap, 0, lpvMaskBits);
//				/* Loop to invert the mask */
//				for (int i = 0; i < maskData.length; i++) {
//					maskData[i] ^= -1;
//				}
//				/* Make sure mask scanlinePad is 2 */
//				int maskPad;
//				int bpl = imageSize / height;
//				for (maskPad = 1; maskPad < 128; maskPad++) {
//					int calcBpl = (((width + 7) / 8) + (maskPad - 1)) / maskPad * maskPad;
//					if (calcBpl == bpl) break;
//				}
//				maskData = ImageData.convertPad(maskData, width, height, 1, maskPad, 2);
//			}
//			/* Clean up */
//			OS.HeapFree(hHeap, 0, lpvBits);
//			OS.SelectObject(hBitmapDC, hOldBitmap);
//			if (oldPalette != 0) {
//				OS.SelectPalette(hBitmapDC, oldPalette, false);
//				OS.RealizePalette(hBitmapDC);
//			}
//			OS.DeleteDC(hBitmapDC);
//
//			/* Release the HDC for the device */
//			device.internal_dispose_GC(hDC, null);
//
//			if (info.hbmColor != 0) OS.DeleteObject(info.hbmColor);
//			if (info.hbmMask != 0) OS.DeleteObject(info.hbmMask);
//			/* Construct and return the ImageData */
//			ImageData imageData = new ImageData(width, height, depth, palette, 4, data);
//			imageData.maskData = maskData;
//			imageData.maskPad = 2;
//			return imageData;
//		}
//		case SWT.BITMAP: {
//			/* Get the basic BITMAP information */
//			bm = new BITMAP();
//			OS.GetObject(handle, BITMAP.sizeof, bm);
//			depth = bm.bmPlanes * bm.bmBitsPixel;
//			width = bm.bmWidth;
//			height = bm.bmHeight;
//			/* Find out whether this is a DIB or a DDB. */
//			boolean isDib = (bm.bmBits != 0);
//			/* Get the HDC for the device */
//			int hDC = device.internal_new_GC(null);
//
//			/*
//			* Feature in WinCE.  GetDIBits is not available in WinCE.  The
//			* workaround is to create a temporary DIB from the DDB and use
//			* the bmBits field of DIBSECTION to retrieve the image data.
//			*/
//			int handle = this.handle;
//			if (OS.IsWinCE) {
//				if (!isDib) {
//					boolean mustRestore = false;
//					if (memGC != null && !memGC.isDisposed()) {
//						mustRestore = true;
//						GCData data = memGC.data;
//						if (data.hNullBitmap != 0) {
//							OS.SelectObject(memGC.handle, data.hNullBitmap);
//							data.hNullBitmap = 0;
//						}
//					}
//					handle = createDIBFromDDB(hDC, this.handle, width, height);
//					if (mustRestore) {
//						int hOldBitmap = OS.SelectObject(memGC.handle, this.handle);
//						memGC.data.hNullBitmap = hOldBitmap;
//					}
//					isDib = true;
//				}
//			}
//			DIBSECTION dib = null;
//			if (isDib) {
//				dib = new DIBSECTION();
//				OS.GetObject(handle, DIBSECTION.sizeof, dib);
//			}
//			/* Calculate number of colors */
//			int numColors = 0;
//			if (depth <= 8) {
//				if (isDib) {
//					numColors = dib.biClrUsed;
//				} else {
//					numColors = 1 << depth;
//				}
//			}
//			/* Create the BITMAPINFO */
//			byte[] bmi = null;
//			BITMAPINFOHEADER bmiHeader = null;
//			if (!isDib) {
//				bmiHeader = new BITMAPINFOHEADER();
//				bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
//				bmiHeader.biWidth = width;
//				bmiHeader.biHeight = -height;
//				bmiHeader.biPlanes = 1;
//				bmiHeader.biBitCount = (short)depth;
//				bmiHeader.biCompression = OS.BI_RGB;
//				bmi = new byte[BITMAPINFOHEADER.sizeof + numColors * 4];
//				OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
//			}
//
//			/* Create the DC and select the bitmap */
//			int hBitmapDC = OS.CreateCompatibleDC(hDC);
//			int hOldBitmap = OS.SelectObject(hBitmapDC, handle);
//			/* Select the palette if necessary */
//			int oldPalette = 0;
//			if (!isDib && depth <= 8) {
//				int hPalette = device.hPalette;
//				if (hPalette != 0) {
//					oldPalette = OS.SelectPalette(hBitmapDC, hPalette, false);
//					OS.RealizePalette(hBitmapDC);
//				}
//			}
//			/* Find the size of the image and allocate data */
//			int imageSize;
//			if (isDib) {
//				imageSize = dib.biSizeImage;
//			} else {
//				/* Call with null lpBits to get the image size */
//				if (OS.IsWinCE) SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
//				OS.GetDIBits(hBitmapDC, handle, 0, height, 0, bmi, OS.DIB_RGB_COLORS);
//				OS.MoveMemory(bmiHeader, bmi, BITMAPINFOHEADER.sizeof);
//				imageSize = bmiHeader.biSizeImage;
//			}
//			byte[] data = new byte[imageSize];
//			/* Get the bitmap data */
//			if (isDib) {
//				if (OS.IsWinCE && this.handle != handle) {
//					/* get image data from the temporary DIB */
//					OS.MoveMemory(data, dib.bmBits, imageSize);
//				} else {
//					OS.MoveMemory(data, bm.bmBits, imageSize);
//				}
//			} else {
//				int hHeap = OS.GetProcessHeap();
//				int lpvBits = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, imageSize);
//				if (OS.IsWinCE) SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
//				OS.GetDIBits(hBitmapDC, handle, 0, height, lpvBits, bmi, OS.DIB_RGB_COLORS);
//				OS.MoveMemory(data, lpvBits, imageSize);
//				OS.HeapFree(hHeap, 0, lpvBits);
//			}
//			/* Calculate the palette */
//			PaletteData palette = null;
//			if (depth <= 8) {
//				RGB[] rgbs = new RGB[numColors];
//				if (isDib) {
//					if (OS.IsWinCE) {
//						/*
//						* Feature on WinCE.  GetDIBColorTable is not supported.
//						* The workaround is to set a pixel to the desired
//						* palette index and use getPixel to get the corresponding
//						* RGB value.
//						*/
//						int red = 0, green = 0, blue = 0;
//						byte[] pBits = new byte[1];
//						OS.MoveMemory(pBits, bm.bmBits, 1);
//						byte oldValue = pBits[0];
//						int mask = (0xFF << (8 - bm.bmBitsPixel)) & 0x00FF;
//						for (int i = 0; i < numColors; i++) {
//							pBits[0] = (byte)((i << (8 - bm.bmBitsPixel)) | (pBits[0] & ~mask));
//							OS.MoveMemory(bm.bmBits, pBits, 1);
//							int color = OS.GetPixel(hBitmapDC, 0, 0);
//							blue = (color & 0xFF0000) >> 16;
//							green = (color & 0xFF00) >> 8;
//							red = color & 0xFF;
//							rgbs[i] = new RGB(red, green, blue);
//						}
//		       			pBits[0] = oldValue;
//			       		OS.MoveMemory(bm.bmBits, pBits, 1);
//					} else {
//						byte[] colors = new byte[numColors * 4];
//						OS.GetDIBColorTable(hBitmapDC, 0, numColors, colors);
//						int colorIndex = 0;
//						for (int i = 0; i < rgbs.length; i++) {
//							rgbs[i] = new RGB(colors[colorIndex + 2] & 0xFF, colors[colorIndex + 1] & 0xFF, colors[colorIndex] & 0xFF);
//							colorIndex += 4;
//						}
//					}
//				} else {
//					int srcIndex = BITMAPINFOHEADER.sizeof;
//					for (int i = 0; i < numColors; i++) {
//						rgbs[i] = new RGB(bmi[srcIndex + 2] & 0xFF, bmi[srcIndex + 1] & 0xFF, bmi[srcIndex] & 0xFF);
//						srcIndex += 4;
//					}
//				}
//				palette = new PaletteData(rgbs);
//			} else if (depth == 16) {
//				palette = new PaletteData(0x7C00, 0x3E0, 0x1F);
//			} else if (depth == 24) {
//				palette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
//			} else if (depth == 32) {
//				palette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
//			} else {
//				SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
//			}
//			/* Clean up */
//			OS.SelectObject(hBitmapDC, hOldBitmap);
//			if (oldPalette != 0) {
//				OS.SelectPalette(hBitmapDC, oldPalette, false);
//				OS.RealizePalette(hBitmapDC);
//			}
//			if (OS.IsWinCE) {
//				if (handle != this.handle) {
//					/* free temporary DIB */
//					OS.DeleteObject (handle);
//				}
//			}
//			OS.DeleteDC(hBitmapDC);
//
//			/* Release the HDC for the device */
//			device.internal_dispose_GC(hDC, null);
//
//			/* Construct and return the ImageData */
//			ImageData imageData = new ImageData(width, height, depth, palette, 4, data);
//			imageData.transparentPixel = this.transparentPixel;
//			imageData.alpha = alpha;
//			if (alpha == -1 && alphaData != null) {
//				imageData.alphaData = new byte[alphaData.length];
//				System.arraycopy(alphaData, 0, imageData.alphaData, 0, alphaData.length);
//			}
//			return imageData;
//		}
//		default:
//      SWT.error(SWT.ERROR_INVALID_IMAGE);
//			return null;
//	}
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
	return handle == null? 0: handle.hashCode();
}

void init(Device device, int width, int height) {
	if (width <= 0 || height <= 0) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	this.device = device;
	type = SWT.BITMAP;
  handle = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  if(!(device instanceof Display)) {
    Graphics g = handle.getGraphics();
    g.setColor(java.awt.Color.WHITE);
    ((Graphics2D)g).setBackground(java.awt.Color.WHITE);
    g.fillRect(0, 0, width, height);
  }
}

static void init(Device device, Image image, ImageData data) {
//  BufferedImage bufferedImage;
//  if (data.palette.isDirect) {
//    bufferedImage = createBufferedImageDirectPalette(data, data.palette);
//  } else {
//    bufferedImage = createBufferedImageIndexPalette (data, data.palette);
//  }
//	image.handle = bufferedImage;
//	image.device = null;
////  SImageData.serialize(image, data);
//	if(true) return;
  image.device = device;
  if(image.handle == null) {
    BufferedImage bufferedImage = new BufferedImage(data.width, data.height, BufferedImage.TYPE_INT_ARGB);
    image.handle = bufferedImage;
    if(!(device instanceof Display)) {
      Graphics g = bufferedImage.getGraphics();
      g.setColor(java.awt.Color.WHITE);
      ((Graphics2D)g).setBackground(java.awt.Color.WHITE);
      g.fillRect(0, 0, data.width, data.height);
    }
  }
  ImageData transparencyMask = data.getTransparencyMask();
  for(int x=image.handle.getWidth()-1; x >= 0; x--) {
    for(int y=image.handle.getHeight()-1; y >= 0; y--) {
      RGB rgb = data.palette.getRGB(data.getPixel(x, y));
      int pixel = rgb.red << 16 | rgb.green << 8 | rgb.blue;
      rgb = transparencyMask.palette.getRGB(transparencyMask.getPixel(x, y));
      int mask = rgb.red << 16 | rgb.green << 8 | rgb.blue;
      if(mask != 0) {
        int alpha = data.getAlpha(x, y);
        if(alpha > 0) {
          pixel = pixel & 0x00FFFFFF | alpha << 24;
          image.handle.setRGB(x, y, pixel);
        }
      }
    }
  }
//  SImageData.serialize(image, data);
//	/*
//	 * BUG in Windows 98:
//	 * A monochrome DIBSection will display as solid black
//	 * on Windows 98 machines, even though it contains the
//	 * correct data. The fix is to convert 1-bit ImageData
//	 * into 4-bit ImageData before creating the image.
//	 */
//	/* Windows does not support 2-bit images. Convert to 4-bit image. */
//	if ((i.depth == 1 && i.getTransparencyType() != SWT.TRANSPARENCY_MASK) || i.depth == 2) {
//		ImageData img = new ImageData(i.width, i.height, 4, i.palette);
//		ImageData.blit(ImageData.BLIT_SRC,
//			i.data, i.depth, i.bytesPerLine, i.getByteOrder(), 0, 0, i.width, i.height, null, null, null,
//			ImageData.ALPHA_OPAQUE, null, 0, 0, 0,
//			img.data, img.depth, img.bytesPerLine, i.getByteOrder(), 0, 0, img.width, img.height, null, null, null,
//			false, false);
//		img.transparentPixel = i.transparentPixel;
//		img.maskPad = i.maskPad;
//		img.maskData = i.maskData;
//		img.alpha = i.alpha;
//		img.alphaData = i.alphaData;
//		i = img;
//	}
//	/*
//	 * Windows supports 16-bit mask of (0x7C00, 0x3E0, 0x1F),
//	 * 24-bit mask of (0xFF0000, 0xFF00, 0xFF) and 32-bit mask
//	 * (0x00FF0000, 0x0000FF00, 0x000000FF) as documented in
//	 * MSDN BITMAPINFOHEADER.  Make sure the image is
//	 * Windows-supported.
//	 */
//	/*
//	* Note on WinCE.  CreateDIBSection requires the biCompression
//	* field of the BITMAPINFOHEADER to be set to BI_BITFIELDS for
//	* 16 and 32 bit direct images (see MSDN for CreateDIBSection).
//	* In this case, the color mask can be set to any value.  For
//	* consistency, it is set to the same mask used by non WinCE
//	* platforms in BI_RGB mode.
//	*/
//	if (i.palette.isDirect) {
//		final PaletteData palette = i.palette;
//		final int redMask = palette.redMask;
//		final int greenMask = palette.greenMask;
//		final int blueMask = palette.blueMask;
//		int newDepth = i.depth;
//		int newOrder = ImageData.MSB_FIRST;
//		PaletteData newPalette = null;
//
//		switch (i.depth) {
//			case 8:
//				newDepth = 16;
//				newOrder = ImageData.LSB_FIRST;
//				newPalette = new PaletteData(0x7C00, 0x3E0, 0x1F);
//				break;
//			case 16:
//				newOrder = ImageData.LSB_FIRST;
//				if (!(redMask == 0x7C00 && greenMask == 0x3E0 && blueMask == 0x1F)) {
//					newPalette = new PaletteData(0x7C00, 0x3E0, 0x1F);
//				}
//				break;
//			case 24:
//				if (!(redMask == 0xFF && greenMask == 0xFF00 && blueMask == 0xFF0000)) {
//					newPalette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
//				}
//				break;
//			case 32:
//				if (!(redMask == 0xFF00 && greenMask == 0xFF0000 && blueMask == 0xFF000000)) {
//					newPalette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
//				}
//				break;
//			default:
//				SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
//		}
//		if (newPalette != null) {
//			ImageData img = new ImageData(i.width, i.height, newDepth, newPalette);
//			ImageData.blit(ImageData.BLIT_SRC,
//					i.data, i.depth, i.bytesPerLine, i.getByteOrder(), 0, 0, i.width, i.height, redMask, greenMask, blueMask,
//					ImageData.ALPHA_OPAQUE, null, 0, 0, 0,
//					img.data, img.depth, img.bytesPerLine, newOrder, 0, 0, img.width, img.height, newPalette.redMask, newPalette.greenMask, newPalette.blueMask,
//					false, false);
//			if (i.transparentPixel != -1) {
//				img.transparentPixel = newPalette.getPixel(palette.getRGB(i.transparentPixel));
//			}
//			img.maskPad = i.maskPad;
//			img.maskData = i.maskData;
//			img.alpha = i.alpha;
//			img.alphaData = i.alphaData;
//			i = img;
//		}
//	}
//	/* Construct bitmap info header by hand */
//	RGB[] rgbs = i.palette.getRGBs();
//	boolean useBitfields = OS.IsWinCE && (i.depth == 16 || i.depth == 32);
//	BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER();
//	bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
//	bmiHeader.biWidth = i.width;
//	bmiHeader.biHeight = -i.height;
//	bmiHeader.biPlanes = 1;
//	bmiHeader.biBitCount = (short)i.depth;
//	if (useBitfields) bmiHeader.biCompression = OS.BI_BITFIELDS;
//	else bmiHeader.biCompression = OS.BI_RGB;
//	bmiHeader.biClrUsed = rgbs == null ? 0 : rgbs.length;
//	byte[] bmi;
//	if (i.palette.isDirect)
//		bmi = new byte[BITMAPINFOHEADER.sizeof + (useBitfields ? 12 : 0)];
//	else
//		bmi = new byte[BITMAPINFOHEADER.sizeof + rgbs.length * 4];
//	OS.MoveMemory(bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
//	/* Set the rgb colors into the bitmap info */
//	int offset = BITMAPINFOHEADER.sizeof;
//	if (i.palette.isDirect) {
//		if (useBitfields) {
//			PaletteData palette = i.palette;
//			int redMask = palette.redMask;
//			int greenMask = palette.greenMask;
//			int blueMask = palette.blueMask;
//			/*
//			 * The color masks must be written based on the
//			 * endianness of the ImageData.
//			 */
//			if (i.getByteOrder() == ImageData.LSB_FIRST) {
//				bmi[offset] = (byte)((redMask & 0xFF) >> 0);
//				bmi[offset + 1] = (byte)((redMask & 0xFF00) >> 8);
//				bmi[offset + 2] = (byte)((redMask & 0xFF0000) >> 16);
//				bmi[offset + 3] = (byte)((redMask & 0xFF000000) >> 24);
//				bmi[offset + 4] = (byte)((greenMask & 0xFF) >> 0);
//				bmi[offset + 5] = (byte)((greenMask & 0xFF00) >> 8);
//				bmi[offset + 6] = (byte)((greenMask & 0xFF0000) >> 16);
//				bmi[offset + 7] = (byte)((greenMask & 0xFF000000) >> 24);
//				bmi[offset + 8] = (byte)((blueMask & 0xFF) >> 0);
//				bmi[offset + 9] = (byte)((blueMask & 0xFF00) >> 8);
//				bmi[offset + 10] = (byte)((blueMask & 0xFF0000) >> 16);
//				bmi[offset + 11] = (byte)((blueMask & 0xFF000000) >> 24);
//			} else {
//				bmi[offset] = (byte)((redMask & 0xFF000000) >> 24);
//				bmi[offset + 1] = (byte)((redMask & 0xFF0000) >> 16);
//				bmi[offset + 2] = (byte)((redMask & 0xFF00) >> 8);
//				bmi[offset + 3] = (byte)((redMask & 0xFF) >> 0);
//				bmi[offset + 4] = (byte)((greenMask & 0xFF000000) >> 24);
//				bmi[offset + 5] = (byte)((greenMask & 0xFF0000) >> 16);
//				bmi[offset + 6] = (byte)((greenMask & 0xFF00) >> 8);
//				bmi[offset + 7] = (byte)((greenMask & 0xFF) >> 0);
//				bmi[offset + 8] = (byte)((blueMask & 0xFF000000) >> 24);
//				bmi[offset + 9] = (byte)((blueMask & 0xFF0000) >> 16);
//				bmi[offset + 10] = (byte)((blueMask & 0xFF00) >> 8);
//				bmi[offset + 11] = (byte)((blueMask & 0xFF) >> 0);
//			}
//		}
//	} else {
//		for (int j = 0; j < rgbs.length; j++) {
//			bmi[offset] = (byte)rgbs[j].blue;
//			bmi[offset + 1] = (byte)rgbs[j].green;
//			bmi[offset + 2] = (byte)rgbs[j].red;
//			bmi[offset + 3] = 0;
//			offset += 4;
//		}
//	}
//	int[] pBits = new int[1];
//	int hDib = OS.CreateDIBSection(0, bmi, OS.DIB_RGB_COLORS, pBits, 0, 0);
//	if (hDib == 0) SWT.error(SWT.ERROR_NO_HANDLES);
//	/* In case of a scanline pad other than 4, do the work to convert it */
//	byte[] data = i.data;
//	if (i.scanlinePad != 4 && (i.bytesPerLine % 4 != 0)) {
//		data = ImageData.convertPad(data, i.width, i.height, i.depth, i.scanlinePad, 4);
//	}
//	OS.MoveMemory(pBits[0], data, data.length);
//
//	int[] result = null;
//	if (i.getTransparencyType() == SWT.TRANSPARENCY_MASK) {
//		/* Get the HDC for the device */
//		int hDC = device.internal_new_GC(null);
//
//		/* Create the color bitmap */
//		int hdcSrc = OS.CreateCompatibleDC(hDC);
//		OS.SelectObject(hdcSrc, hDib);
//		int hBitmap = OS.CreateCompatibleBitmap(hDC, i.width, i.height);
//		if (hBitmap == 0) SWT.error(SWT.ERROR_NO_HANDLES);
//		int hdcDest = OS.CreateCompatibleDC(hDC);
//		OS.SelectObject(hdcDest, hBitmap);
//		OS.BitBlt(hdcDest, 0, 0, i.width, i.height, hdcSrc, 0, 0, OS.SRCCOPY);
//
//		/* Release the HDC for the device */
//		device.internal_dispose_GC(hDC, null);
//
//		/* Create the mask. Windows requires icon masks to have a scanline pad of 2. */
//		byte[] maskData = ImageData.convertPad(i.maskData, i.width, i.height, 1, i.maskPad, 2);
//		int hMask = OS.CreateBitmap(i.width, i.height, 1, 1, maskData);
//		if (hMask == 0) SWT.error(SWT.ERROR_NO_HANDLES);
//		OS.SelectObject(hdcSrc, hMask);
//		OS.PatBlt(hdcSrc, 0, 0, i.width, i.height, OS.DSTINVERT);
//		OS.DeleteDC(hdcSrc);
//		OS.DeleteDC(hdcDest);
//		OS.DeleteObject(hDib);
//
//		if (image == null) {
//			result = new int[]{hBitmap, hMask};
//		} else {
//			/* Create the icon */
//			ICONINFO info = new ICONINFO();
//			info.fIcon = true;
//			info.hbmColor = hBitmap;
//			info.hbmMask = hMask;
//			int hIcon = OS.CreateIconIndirect(info);
//			if (hIcon == 0) SWT.error(SWT.ERROR_NO_HANDLES);
//			OS.DeleteObject(hBitmap);
//			OS.DeleteObject(hMask);
//			image.handle = hIcon;
//			image.type = SWT.ICON;
//			if (OS.IsWinCE) image.data = i;
//		}
//	} else {
//		if (image == null) {
//			result = new int[]{hDib};
//		} else {
//			image.handle = hDib;
//			image.type = SWT.BITMAP;
//			image.transparentPixel = i.transparentPixel;
//			if (image.transparentPixel == -1) {
//				image.alpha = i.alpha;
//				if (i.alpha == -1 && i.alphaData != null) {
//					int length = i.alphaData.length;
//					image.alphaData = new byte[length];
//					System.arraycopy(i.alphaData, 0, image.alphaData, 0, length);
//				}
//			}
//		}
//	}
//	return result;
}

static void init(Device device, Image image, ImageData source, ImageData mask) {
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
	init(device, image, imageData);
}

void init(Device device, ImageData i) {
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
 */
@Override
public CGC internal_new_GC (GCData data) {
	if (handle == null) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  if(data != null) {
    int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
    if ((data.style & mask) != 0) {
      data.layout = (data.style & SWT.RIGHT_TO_LEFT) != 0 ? false : true;
    } else {
      data.style |= SWT.LEFT_TO_RIGHT;
    }
    data.device = device;
    data.image = this;
    data.hFont = LookAndFeelUtils.getSystemFont();
  }
  final Graphics2D g = (Graphics2D)handle.getGraphics();
  if(g == null) {
    return null;
  }
  return new CGC.CGCGraphics2D() {
    @Override
	public Graphics2D getGraphics() {
      return g;
    }
    @Override
	public Dimension getDeviceSize() {
      return new Dimension(handle.getWidth(), handle.getHeight());
    }
  };
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
public void internal_dispose_GC (CGC handle, GCData data) {
	handle.dispose();
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
  // TODO: change all pixels with alpha channel == 1.0 to the defined value?
  // if(colorModel instanceof IndexColorModel) {return colorModel.getTransparentPixel();}
  return;
//	/*
//	* Note.  Not implemented on WinCE.
//	*/
//	if (OS.IsWinCE) return;
//	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
//	if (color == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
//	if (color.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
//	if (transparentPixel == -1) return;
//
//	/* Get the HDC for the device */
//	int hDC = device.internal_new_GC(null);
//
//	/* Change the background color in the image */
//	BITMAP bm = new BITMAP();
//	OS.GetObject(handle, BITMAP.sizeof, bm);
//	int hdcMem = OS.CreateCompatibleDC(hDC);
//	OS.SelectObject(hdcMem, handle);
//	int maxColors = 1 << bm.bmBitsPixel;
//	byte[] colors = new byte[maxColors * 4];
//	if (OS.IsWinCE) SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
//	int numColors = OS.GetDIBColorTable(hdcMem, 0, maxColors, colors);
//	int offset = transparentPixel * 4;
//	colors[offset] = (byte)color.getBlue();
//	colors[offset + 1] = (byte)color.getGreen();
//	colors[offset + 2] = (byte)color.getRed();
//	if (OS.IsWinCE) SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
//	OS.SetDIBColorTable(hdcMem, 0, numColors, colors);
//	OS.DeleteDC(hdcMem);
//
//	/* Release the HDC for the device */
//	device.internal_dispose_GC(hDC, null);
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
 */
public static Image swing_new(Device device, int type, java.awt.Image handle) {
	if (device == null) device = Device.getDevice();
	Image image = new Image();
	image.type = type;
	image.handle = duplicateImage(handle);
	image.device = device;
	return image;
}

static BufferedImage duplicateImage(java.awt.Image handle) {
  GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
  GraphicsDevice gs = ge.getDefaultScreenDevice();
  GraphicsConfiguration gc = gs.getDefaultConfiguration();
  int transparency;
  if(handle instanceof BufferedImage) {
    transparency = ((BufferedImage)handle).getColorModel().getTransparency();
  } else {
    PixelGrabber pg = new PixelGrabber(handle, 0, 0, 1, 1, false);
    try {
      pg.grabPixels();
    } catch (InterruptedException e) {}
    transparency = pg.getColorModel().getTransparency();
  }
  BufferedImage bHandle = gc.createCompatibleImage(handle.getWidth(null), handle.getHeight(null), transparency);
  Graphics g = bHandle.createGraphics();
  g.drawImage(handle, 0, 0, null);
  g.dispose();
  return bHandle;
}

public ImageData getImageData(int deviceZoom) {
	throw new UnsupportedOperationException("Not implemented yet");
}

//private static BufferedImage createBufferedImageIndexPalette(ImageData data, PaletteData p) {
//  RGB[] rgbs = p.getRGBs();
//  byte[] red = new byte[rgbs.length];
//  byte[] green = new byte[rgbs.length];
//  byte[] blue = new byte[rgbs.length];
//  for(int i = 0; i < rgbs.length; i++) {
//    RGB rgb = rgbs[i];
//    red[i] = (byte) rgb.red;
//    green[i] = (byte) rgb.green;
//    blue[i] = (byte) rgb.blue;
//  }
//  ColorModel cM;
//  if(data.transparentPixel != -1) {
//    cM = new IndexColorModel(data.depth, rgbs.length, red, green, blue, data.transparentPixel);
//  } else {
//    cM = new IndexColorModel(data.depth, rgbs.length, red, green, blue);
//  }
//  BufferedImage bi = new BufferedImage(cM, cM.createCompatibleWritableRaster(data.width, data.height), false, null);
//  WritableRaster r = bi.getRaster();
//  int[] pA = new int[1];
//  for (int y = 0; y < data.height; y++) {
//    for (int x = 0; x < data.width; x++) {
//      int pixel = data.getPixel(x, y);
//      pA[0] = pixel;
//      r.setPixel(x, y, pA);
//    }
//  }
//  // System.out.println("data.transparentPixel: "
//  // + data.transparentPixel);
//  // System.out.println(colorModel);
//  // System.out.println(bufferedImage);
//  // System.out.println();
//  return bi;
//}
//
//private static BufferedImage createBufferedImageDirectPalette(ImageData data, PaletteData p) {
//  // Added ColorModel.TRANSLUCENT to create a ColorModel with
//  // transparency and resulting in a ARGB BufferedImage
//  // TODO: Check if true: Still Transparency-Gradients may be
//  // impossible with standard JavaIO & BufferedImages since
//  // ColorModel.TRANSLUCENT is either opaque or transparent.
//  ColorModel cM = new DirectColorModel(data.depth, p.redMask, p.greenMask, p.blueMask, 0);
//  BufferedImage bi = new BufferedImage(cM, cM.createCompatibleWritableRaster(data.width, data.height), false, null);
//  WritableRaster r = bi.getRaster();
//  int[] pA = new int[4];
//  for (int y = 0; y < data.height; y++) {
//    for (int x = 0; x < data.width; x++) {
//      int pixel = data.getPixel(x, y);
//      RGB rgb = p.getRGB(pixel);
//      pA[0] = rgb.red;
//      pA[1] = rgb.green;
//      pA[2] = rgb.blue;
//      pA[3] = data.getAlpha(x, y);
//      // Line is a bugfix which is actually for Problems with
//      // detecting Transparency in BufferedImages. Also using the
//      // normal ImageIO.read(...) method results in this bug and
//      // can be tested using comment.png from Azureus
//      if (data.transparentPixel != -1 && pixel == data.transparentPixel) {
//        pA[3] = 0;
//      }
//      // System.out.println(pixelArray[3]);
//      r.setPixels(x, y, 1, 1, pA);
//    }
//  }
//  // System.out.println("data.transparentPixel: "
//  // + data.transparentPixel);
//  // System.out.println(colorModel);
//  // System.out.println(bufferedImage);
//  // System.out.println();
//  return bi;
//}

//public static class SImageData implements java.io.Serializable {
//  public static class SPaletteData implements java.io.Serializable {
//    public boolean isDirect;
//    public RGB[] colors;
//    public int redMask;
//    public int greenMask;
//    public int blueMask;
//    public int redShift;
//    public int greenShift;
//    public int blueShift;
//    SPaletteData(PaletteData data) {
//      this.colors = data.colors;
//      this.isDirect = data.isDirect;
//      this.redMask = data.redMask;
//      this.greenMask = data.greenMask;
//      this.blueMask = data.blueMask;
//      this.redShift = data.redShift;
//      this.greenShift = data.greenShift;
//      this.blueShift = data.blueShift;
//    }
//    public PaletteData createPaletteData() {
//      PaletteData paletteData = new PaletteData(new RGB[0]);
//      paletteData.colors = colors;
//      paletteData.isDirect = isDirect;
//      paletteData.redMask = redMask;
//      paletteData.greenMask = greenMask;
//      paletteData.blueMask = blueMask;
//      paletteData.redShift = redShift;
//      paletteData.greenShift = greenShift;
//      paletteData.blueShift = blueShift;
//      return paletteData;
//    }
//    public String toString() {
//      StringBuilder sb = new StringBuilder("PaletteData:");
//      try {
//        java.lang.reflect.Field[] fields = getClass().getDeclaredFields();
//        for(int i=0; i<fields.length; i++) {
//          java.lang.reflect.Field field = fields[i];
//          Object value = field.get(this);
//          if(value instanceof Object[]) {
//            value = java.util.Arrays.toString((Object[])value);
//          } else if(value instanceof byte[]) {
//            value = java.util.Arrays.toString((byte[])value);
//          }
//          sb.append("\r\n").append("  ").append(field.getName()).append(": ").append(value);
//        }
//      } catch(Exception e) {
//        e.printStackTrace();
//      }
//      return sb.toString();
//    }
//  }
//  public int width;
//  public int height;
//  public int depth;
//  public int scanlinePad;
//  public int bytesPerLine;
//  public byte[] data;
//  public SPaletteData palette;
//  public int transparentPixel;
//  public byte[] maskData;
//  public int maskPad;
//  public byte[] alphaData;
//  public int alpha;
//  public int type;
//  public int x;
//  public int y;
//  public int disposalMethod;
//  public int delayTime;
//  SImageData(ImageData data) {
//    this.width = data.width;
//    this.height = data.height;
//    this.depth = data.depth;
//    this.scanlinePad = data.scanlinePad;
//    this.bytesPerLine = data.bytesPerLine;
//    this.data = data.data;
//    if(data.palette != null) {
//      this.palette = new SPaletteData(data.palette);
//    }
//    this.transparentPixel = data.transparentPixel;
//    this.maskData = data.maskData;
//    this.maskPad = data.maskPad;
//    this.alphaData = data.alphaData;
//    this.alpha = data.alpha;
//    this.type = data.type;
//    this.x = data.x;
//    this.y = data.y;
//    this.disposalMethod = data.disposalMethod;
//    this.delayTime = data.delayTime;
//  }
//  public ImageData createImageData() {
//    ImageData imageData = new ImageData();
//    imageData.width = width;
//    imageData.height = height;
//    imageData.depth = depth;
//    imageData.scanlinePad = scanlinePad;
//    imageData.bytesPerLine = bytesPerLine;
//    imageData.data = data;
//    if(palette != null) {
//      imageData.palette = palette.createPaletteData();
//    }
//    imageData.transparentPixel = transparentPixel;
//    imageData.maskData = maskData;
//    imageData.maskPad = maskPad;
//    imageData.alphaData = alphaData;
//    imageData.alpha = alpha;
//    imageData.type = type;
//    imageData.x = x;
//    imageData.y = y;
//    imageData.disposalMethod = disposalMethod;
//    imageData.delayTime = delayTime;
//    return imageData;
//  }
//  public String toString() {
//    StringBuilder sb = new StringBuilder("ImageData: ");
//    try {
//      java.lang.reflect.Field[] fields = getClass().getDeclaredFields();
//      for(int i=0; i<fields.length; i++) {
//        java.lang.reflect.Field field = fields[i];
//        Object value = field.get(this);
//        if(value instanceof Object[]) {
//          value = java.util.Arrays.toString((Object[])value);
//        } else if(value instanceof byte[]) {
//          value = java.util.Arrays.toString((byte[])value);
//        }
//        sb.append("\r\n").append("  ").append(field.getName()).append(": ").append(value);
//      }
//    } catch(Exception e) {
//      e.printStackTrace();
//    }
//    return sb.toString();
//  }
//  public static void serialize(Image image, ImageData data) {
//    try {
//      java.io.File tmpDir = new java.io.File(System.getProperty("java.io.tmpdir"), ".swtswing_img");
//      tmpDir.mkdirs();
//      long now = System.currentTimeMillis();
//      javax.imageio.ImageIO.write(image.handle, "png", new java.io.File(tmpDir, now + ".png"));
//      java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(new java.io.BufferedOutputStream(new java.io.FileOutputStream(new java.io.File(tmpDir, now + ".bin"))));
//      SImageData sImageData = new SImageData(data);
//      out.writeObject(sImageData);
//      out.close();
//      java.io.OutputStream out2 = new java.io.BufferedOutputStream(new java.io.FileOutputStream(new java.io.File(tmpDir, now + ".txt")));
//      out2.write(sImageData.toString().getBytes("UTF-8"));
//      out2.close();
//    } catch(Exception e) {
//      e.printStackTrace();
//    }
//  }
//  public static ImageData deserialize(java.io.File file) {
//    try {
//      java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(file)));
//      SImageData sImageData = (SImageData)in.readObject();
//      in.close();
//      return sImageData.createImageData();
//    } catch(Exception e) {
//      e.printStackTrace();
//    }
//    return null;
//  }
//}

}