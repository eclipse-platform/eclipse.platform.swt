package org.eclipse.swt.graphics;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.*;
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
 * Application code must explicitely invoke the <code>Image.dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 *
 * @see Color
 * @see ImageData
 * @see ImageLoader
 */
public final class Image implements Drawable {
		
	/**
	 * specifies whether the receiver is a bitmap or an icon
	 * (one of <code>SWT.BITMAP</code>, <code>SWT.ICON</code>)
	 */
	public int type;

	/**
	 * The handle to the OS pixmap resource.
	 * Warning: This field is platform dependent.
	 */
	public int pixmap;

	/**
	 * The handle to the OS mask resource.
	 * Warning: This field is platform dependent.
	 */
	public int mask;

	/**
	 * The device where this image was created.
	 */
	Device device;

	/**
	 * specifies the transparent pixel
	 * (Warning: This field is platform dependent)
	 */
	int transparentPixel = -1;

	/**
	 * The GC the image is currently selected in.
	 * Warning: This field is platform dependent.
	 */
	GC memGC;

	/**
	 * The alpha data of the image.
	 * Warning: This field is platform dependent.
	 */
	byte[] alphaData;

	/**
	 * The global alpha value to be used for every pixel.
	 * Warning: This field is platform dependent.
	 */
	int alpha = -1;

	/**
	 * Specifies the default scanline padding.
	 * Warning: This field is platform dependent.
	 */
	static final int DEFAULT_SCANLINE_PAD = 4;

Image() {
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
	init(device, width, height);
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
 *    <li>ERROR_INVALID_IMAGE - if the image is not a bitmap or an icon, or
 *          is otherwise in an invalid state</li>
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
			
	this.type = srcImage.type;
	this.mask = 0;
	
	MacRect bounds= new MacRect();
	OS.GetPixBounds(srcImage.pixmap, bounds.getData());
 	int width = bounds.getWidth();
 	int height = bounds.getHeight();

	/* Don't create the mask here if flag is SWT.IMAGE_GRAY. See below.*/
	if (flag != SWT.IMAGE_GRAY && srcImage.mask != 0) {
		/* Generate the mask if necessary. */
		if (srcImage.transparentPixel != -1) srcImage.createMask();
		this.mask = duplicate(srcImage.mask);
		/* Destroy the image mask if the there is a GC created on the image */
		if (srcImage.transparentPixel != -1 && srcImage.memGC != null) srcImage.destroyMask();
	}
	
	switch (flag) {
		
	case SWT.IMAGE_COPY:
		this.pixmap = duplicate(srcImage.pixmap);
		transparentPixel = srcImage.transparentPixel;
		alpha = srcImage.alpha;
		if (srcImage.alphaData != null) {
			alphaData = new byte[srcImage.alphaData.length];
			System.arraycopy(srcImage.alphaData, 0, alphaData, 0, alphaData.length);
		}
		return;
		
	case SWT.IMAGE_DISABLE:
		/* Get src image data */
		int srcDepth= getDepth(srcImage.pixmap);
		int srcRowBytes= rowBytes(width, srcDepth);
		int srcBitsPerPixel= srcDepth;
		
		byte[] srcData = new byte[srcRowBytes * height];
		copyPixMapData(srcImage.pixmap, srcData);

		/* Create destination image */
		int destPixmap = createPixMap(width, height, srcDepth);
		int destBitsPerPixel= srcDepth;
		byte[] destData = new byte[srcRowBytes * height];
		
		/* Find the colors to map to */
		Color zeroColor = device.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
		Color oneColor = device.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		int zeroPixel= 0;
		int onePixel= 1;
		setColorTable(destPixmap, new Color[] { zeroColor, oneColor });

		switch (srcBitsPerPixel) {
		case 1:
			/*
			 * Nothing we can reasonably do here except copy
			 * the bitmap; we can't make it a higher color depth.
			 * Short-circuit the rest of the code and return.
			 */
			pixmap = duplicate(srcImage.pixmap);
			return;
		case 4:
			SWT.error(SWT.ERROR_NOT_IMPLEMENTED);
			break;
		case 8:
			int index = 0;
			int srcPixel, r, g, b;
			
			int[] colors= new int[256];
			for (int i= 0; i < 256; i++)
				colors[i]= -1;
				
			short[] colorTable= getColorTable(srcImage.pixmap);
			
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < srcRowBytes; x++) {
					srcPixel = srcData[index + x] & 0xFF;
					/* Get the RGB values of srcPixel */
					int color= colors[srcPixel];
					if (color == -1)			
						colors[srcPixel]= color= getRGB(colorTable, srcPixel);
					r = (color >> 16) & 0xFF;
					g = (color >> 8) & 0xFF;
					b = (color) & 0xFF;
					/* See if the rgb maps to 0 or 1 */
					if ((r * r + g * g + b * b) < 98304) {
						/* Map down to 0 */
						destData[index + x] = (byte)zeroPixel;
					} else {
						/* Map up to 1 */
						destData[index + x] = (byte)onePixel;
					}
				}
				index += srcRowBytes;
			}
			break;
		case 16:
			index = 0;
			/* Get masks */
			int redMask = getRedMask(16);
			int greenMask = getGreenMask(16);
			int blueMask = getBlueMask(16);					
			/* Calculate mask shifts */
			int[] shift = new int[1];
			getOffsetForMask(16, redMask, true, shift);
			int rShift = 24 - shift[0];
			getOffsetForMask(16, greenMask, true, shift);
			int gShift = 24 - shift[0];
			getOffsetForMask(16, blueMask, true, shift);
			int bShift = 24 - shift[0];
			byte zeroLow = (byte)(zeroPixel & 0xFF);
			byte zeroHigh = (byte)((zeroPixel >> 8) & 0xFF);
			byte oneLow = (byte)(onePixel & 0xFF);
			byte oneHigh = (byte)((onePixel >> 8) & 0xFF);
			for (int y = 0; y < height; y++) {
				int xIndex = 0;
				for (int x = 0; x < srcRowBytes; x += 2) {
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
					xIndex += srcBitsPerPixel / 8;
				}
				index += srcRowBytes;
			}
			break;
		case 24:
		case 32:
			index = 0;
			/* Get masks */
			redMask = getRedMask(srcBitsPerPixel);
			greenMask = getGreenMask(srcBitsPerPixel);
			blueMask = getBlueMask(srcBitsPerPixel);					
			/* Calculate mask shifts */
			shift = new int[1];
			getOffsetForMask(srcBitsPerPixel, redMask, true, shift);
			rShift = shift[0];
			getOffsetForMask(srcBitsPerPixel, greenMask, true, shift);
			gShift = shift[0];
			getOffsetForMask(srcBitsPerPixel, blueMask, true, shift);
			bShift = shift[0];
			byte zeroR = (byte)zeroColor.getRed();
			byte zeroG = (byte)zeroColor.getGreen();
			byte zeroB = (byte)zeroColor.getBlue();
			byte oneR = (byte)oneColor.getRed();
			byte oneG = (byte)oneColor.getGreen();
			byte oneB = (byte)oneColor.getBlue();
			for (int y = 0; y < height; y++) {
				int xIndex = 0;
				for (int x = 0; x < height; x++) {
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
					xIndex += destBitsPerPixel / 8;
				}
				index += srcRowBytes;
			}
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_IMAGE);
		}
		setPixMapData(destPixmap, destData);
		this.pixmap = destPixmap;
		return;

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
			newData.maskData = data.maskData;
			newData.maskPad = data.maskPad;
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
					int red = pixel & redMask;
					red = (redShift < 0) ? red >>> -redShift : red << redShift;
					int green = pixel & greenMask;
					green = (greenShift < 0) ? green >>> -greenShift : green << greenShift;
					int blue = pixel & blueMask;
					blue = (blueShift < 0) ? blue >>> -blueShift : blue << blueShift;
					newData.data[offset++] =
						(byte)((red+red+green+green+green+green+green+blue) >> 3);
				}
			}
		}
		init (device, newData);
		break;
	default:
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
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
	if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, bounds.width, bounds.height);
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
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 */
public Image(Device device, ImageData image) {
	init(device, image);
}
/**
 * Constructs an instance of this class, whose type is 
 * <code>SWT.ICON</code>, from the two given <code>ImageData</code>
 * objects. The two images must be the same size, and the mask image
 * must have a color depth of 1. Pixel transparency in either image
 * will be ignored. If either image is an icon to begin with, an
 * exception is thrown.
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
 *    <li>ERROR_INVALID_ARGUMENT - if source and mask are different sizes or
 *          if the mask is not monochrome, or if either the source or mask
 *          is already an icon</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 */
public Image(Device device, ImageData source, ImageData mask) {
	if (source == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (mask == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (source.width != mask.width || source.height != mask.height) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (mask.depth != 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	ImageData image = new ImageData(source.width, source.height, source.depth, source.palette, source.scanlinePad, source.data);
	image.maskPad = mask.scanlinePad;
	image.maskData = mask.data;
	init(device, image);
}
/**
 * Constructs an instance of this class by loading its representation
 * from the specified input stream. Throws an error if an error
 * occurs while loading the image, or if the result is an image
 * of an unsupported type.
 * <p>
 * This constructor is provided for convenience when loading a single
 * image only. If the stream contains multiple images, only the first
 * one will be loaded. To load multiple images, use 
 * <code>ImageLoader.load()</code>.
 * </p><p>
 * This constructor may be used to load a resource as follows:
 * </p>
 * <pre>
 *     new Image(device, clazz.getResourceAsStream("file.gif"));
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
 *    <li>ERROR_INVALID_IMAGE - if the image file contains invalid data </li>
 *    <li>ERROR_IO - if an IO error occurs while reading data</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 */
public Image(Device device, InputStream stream) {
	init(device, new ImageData(stream));
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
 *    <li>ERROR_INVALID_IMAGE - if the image file contains invalid data </li>
 *    <li>ERROR_IO - if an IO error occurs while reading data</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for image creation</li>
 * </ul>
 */
public Image(Device device, String filename) {
	init(device, new ImageData(filename));
}
/**
 * Create the receiver's mask if necessary.
 */
void createMask() {
	if (mask != 0) return;
	mask = createMaskImage(getImageData().getTransparencyMask());
}
/**
 * Creates a Quickdraw BitMap from a device-independent image of depth 1.
 */
private static int createMaskImage(ImageData image) {
	if (image.depth != 1) return 0;
	
	int w= image.width;
	int h= image.height;
	int bitmap= createBitMap(w, h);
	int rowBytes= rowBytes(w, 1);
	byte[] data= new byte[rowBytes * h];

	ImageData.blit(ImageData.BLIT_SRC,
		image.data, 1, image.bytesPerLine, image.getByteOrder(), 0, 0, w, h, null, null, null,
		ImageData.ALPHA_OPAQUE, null, 0,
		data, 1, rowBytes, ImageData.MSB_FIRST, 0, 0, w, h, null, null, null,
		false, false);
	
	setPixMapData(bitmap, data);

	return bitmap;
}
/**
 * Disposes of the operating system resources associated with
 * the image. Applications must dispose of all images which
 * they allocate.
 */
public void dispose () {
	if (pixmap == 0) return;
	if (device.isDisposed()) return;
	if (pixmap != 0) disposeBitmapOrPixmap(pixmap);
	if (mask != 0) disposeBitmapOrPixmap(mask);
	device = null;
	memGC = null;
	pixmap = mask = 0;
}
/**
 * Destroy the receiver's mask if it exists.
 */
void destroyMask() {
	if (mask == 0) return;
	disposeBitmapOrPixmap(mask);
	mask= 0;
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
	return device == image.device && pixmap == image.pixmap &&
			transparentPixel == image.transparentPixel &&
					mask == image.mask;
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
    /* AW
	XColor xColor = new XColor();
	xColor.pixel = transparentPixel;
	int xDisplay = device.xDisplay;
	int colormap = OS.XDefaultColormap(xDisplay, OS.XDefaultScreen(xDisplay));
	OS.XQueryColor(xDisplay, colormap, xColor);
	return Color.motif_new(device, xColor);
    */
    System.out.println("Image.getBackground: nyi");
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
public Rectangle getBounds () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	MacRect bounds= new MacRect();		
	OS.GetPixBounds(pixmap, bounds.getData());
	return bounds.toRectangle();
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
    int srcDepth= getDepth(pixmap);
    int srcRowBytes= rowBytes(width, srcDepth);
    int srcBitsPerPixel= srcDepth;

	/* Calculate the palette depending on the display attributes */
	PaletteData palette = null;

	/* Get the data for the source image. */
	byte[] srcData = new byte[srcRowBytes * height];
 	copyPixMapData(pixmap, srcData);
	
	switch (srcDepth) {
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
			byte[] normPixel = new byte[ 256 ];
			for (int index = 0; index < normPixel.length; index++) {
				normPixel[ index ] = 0;
			}
			int numPixels = 1;
			int index = 0;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < srcRowBytes; x++) {
					int srcPixel = srcData[ index + x ] & 0xFF;
					if (srcPixel != 0 && normPixel[ srcPixel ] == 0) {
						normPixel[ srcPixel ] = (byte)numPixels++;
					}
					srcData[ index + x ] = normPixel[ srcPixel ];
				}
				index += srcRowBytes;
			}
			
			short[] colorTable= getColorTable(pixmap);

			/* Create a palette with only the RGB values used in the image. */
			RGB[] rgbs = new RGB[ numPixels ];
			for (int srcPixel = 0; srcPixel < normPixel.length; srcPixel++) {
				// If the pixel value was used in the image, get its RGB values.
				if (srcPixel == 0 || normPixel[ srcPixel ] != 0) {
					int packed= getRGB(colorTable, srcPixel);
					int rgbIndex = normPixel[ srcPixel ] & 0xFF;
					rgbs[ rgbIndex ] = new RGB((packed >> 16) & 0xFF, (packed >> 8) & 0xFF, (packed >> 0) & 0xFF);					
				}
			}
			palette = new PaletteData(rgbs);
			break;
		case 16:
		case 24:
		case 32:
			palette = new PaletteData(getRedMask(srcDepth), getGreenMask(srcDepth), getBlueMask(srcDepth));
			break;
		default:
			SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
	}
	
	ImageData data = new ImageData(width, height, srcDepth, palette);
	data.data = srcData;
	if (false && srcBitsPerPixel == 32) {
		/*
		 * If bits per pixel is 32, scale the data down to 24, since we do not
		 * support 32-bit images
		 */
		byte[] oldData = data.data;
		int bytesPerLine = (width * srcDepth + 7) / 8;
		bytesPerLine = (bytesPerLine + 3) / 4 * 4;
		byte[] newData = new byte[bytesPerLine * height];
		int destIndex = 0;
		int srcIndex = 0;
		int rOffset = 0, gOffset = 1, bOffset = 2;
		// MSBFirst:
		rOffset = 3; gOffset = 2; bOffset = 1;
		
		for (int y = 0; y < height; y++) {
			destIndex = y * bytesPerLine;
			srcIndex = y * srcRowBytes;
			for (int x = 0; x < width; x++) {
				newData[destIndex] = oldData[srcIndex + rOffset];
				newData[destIndex + 1] = oldData[srcIndex + gOffset];
				newData[destIndex + 2] = oldData[srcIndex + bOffset];
				srcIndex += 4;
				destIndex += 3;
			}
		}
		data.data = newData;
	}
	if (transparentPixel == -1 && type == SWT.ICON && mask != 0) {
		/* Get the icon data */
		data.maskPad = 4;
		int maskRowBytes= rowBytes(width, getDepth(mask));
		data.maskData = new byte[maskRowBytes * height];
		copyPixMapData(mask, data.maskData);
	}
	data.transparentPixel = transparentPixel;
	data.alpha = alpha;
	if (alpha == -1 && alphaData != null) {
		data.alphaData = new byte[alphaData.length];
		System.arraycopy(alphaData, 0, data.alphaData, 0, alphaData.length);
	}
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
static boolean getOffsetForMask(int bitspp, int mask, boolean msbFirst, int[] poff) {
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
	if (msbFirst) {
		poff[0] = (bitspp/8 - 1) - poff[0];
	}
	return true;
}
/**
 * Returns an integer hash code for the receiver. Any two 
 * objects which return <code>true</code> when passed to 
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
void init(Device device, int width, int height) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	/* Create the pixmap */
	if (width <= 0 | height <= 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	this.type = SWT.BITMAP;
	int pixmap = createPixMap(width, height, device.fScreenDepth);

	/* Fill the bitmap with white */
    int[] offscreenGWorld= new int[1];
	OS.NewGWorldFromPtr(offscreenGWorld, pixmap);
	int xGC = offscreenGWorld[0];
	if (xGC == 0) SWT.error (SWT.ERROR_NO_HANDLES);

	int[] savePort= new int[1];
	int[] saveGWorld= new int[1];
	OS.GetGWorld(savePort, saveGWorld);
	OS.SetGWorld(xGC, 0);
	OS.EraseRect(new short[] { 0, 0, (short)width, (short)height } );
	OS.SetGWorld(savePort[0], saveGWorld[0]);
	
	OS.DisposeGWorld(xGC);
	
	this.pixmap = pixmap;
}
void init(Device device, ImageData image) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	if (image == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);

	int screenDepth = device.fScreenDepth;
	int pixmap = createPixMap(image.width, image.height, screenDepth);

	int[] transPixel = null;
	if (image.transparentPixel != -1) transPixel = new int[]{image.transparentPixel};
	int error= putImage(image, 0, 0, image.width, image.height,
							    0, 0, image.width, image.height, screenDepth, transPixel, pixmap);
	if (error != 0) {
		disposeBitmapOrPixmap(pixmap);
		SWT.error(error);
	}
	if (image.getTransparencyType() == SWT.TRANSPARENCY_MASK || image.transparentPixel != -1) {
		if (image.transparentPixel != -1) transparentPixel = transPixel[0];
		int mask= createMaskImage(image.getTransparencyMask());
		if (mask == 0) {
			disposeBitmapOrPixmap(pixmap);
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
 * @private
 */
public int internal_new_GC (GCData data) {
	if (pixmap == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (type != SWT.BITMAP || memGC != null) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	int[] offscreenGWorld = new int[1];
	OS.NewGWorldFromPtr(offscreenGWorld, pixmap);
	int xGC = offscreenGWorld[0];
	if (xGC == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	if (data != null) {
		data.device = device;
		data.image = this;
		data.font = device.systemFont;
		data.foreground = 0x00000000;	// black
		data.background = 0x00ffffff;	// white
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
 * @param handle the platform specific GC handle
 * @param data the platform specific GC data 
 *
 * @private
 */
public void internal_dispose_GC (int gc, GCData data) {
	if (gc != 0)
		OS.DisposeGWorld(gc);
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
public static Image macosx_new(Device device, int type, int pixmap, int mask) {
	if (device == null) device = Device.getDevice();
	Image image = new Image();
	image.device = device;
	image.type = type;
	image.pixmap = pixmap;
	image.mask = mask;
	return image;
}
/**
 * Put a device-independent image of any depth into a drawable of any depth,
 * stretching if necessary.
 */
static int putImage(ImageData image, int srcX, int srcY, int srcWidth, int srcHeight,
		int destX, int destY, int destWidth, int destHeight,
		int screenDepth, int[] transparentPixel, int drawable) {
			
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
		/*
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
		*/
		screenDirect = false;
	} else {
		destRedMask = getRedMask(screenDepth);
		destGreenMask = getGreenMask(screenDepth);
		destBlueMask = getBlueMask(screenDepth);
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
	
	int destDepth= 32;
	int destBitsPerPixel= destDepth;
	
	int dest_red_mask= getRedMask(destBitsPerPixel);
	int dest_green_mask= getGreenMask(destBitsPerPixel);
	int dest_blue_mask= getBlueMask(destBitsPerPixel);
	
	int destRowBytes= rowBytes(destWidth, 32);
	int bufSize = destRowBytes * destHeight;	
	byte[] buf = new byte[bufSize];

	int srcOrder = image.getByteOrder();
	
	if (palette.isDirect) {
		if (screenDirect) {
			ImageData.blit(ImageData.BLIT_SRC,
				image.data, image.depth, image.bytesPerLine, srcOrder, srcX, srcY, srcWidth, srcHeight, palette.redMask, palette.greenMask, palette.blueMask,
				ImageData.ALPHA_OPAQUE, null, 0,
				buf, destBitsPerPixel, destRowBytes, ImageData.MSB_FIRST, 0, 0, destWidth, destHeight, dest_red_mask, dest_green_mask, dest_blue_mask,
				flipX, flipY);
		} else {
			ImageData.blit(ImageData.BLIT_SRC,
				image.data, image.depth, image.bytesPerLine, srcOrder, srcX, srcY, srcWidth, srcHeight, palette.redMask, palette.greenMask, palette.blueMask,
				ImageData.ALPHA_OPAQUE, null, 0,
				buf, destBitsPerPixel, destRowBytes, ImageData.MSB_FIRST, 0, 0, destWidth, destHeight, destReds, destGreens, destBlues,
				flipX, flipY);
		}
	} else {
		if (screenDirect) {
			ImageData.blit(ImageData.BLIT_SRC,
				image.data, image.depth, image.bytesPerLine, srcOrder, srcX, srcY, srcWidth, srcHeight, srcReds, srcGreens, srcBlues,
				ImageData.ALPHA_OPAQUE, null, 0,
				buf, destBitsPerPixel, destRowBytes, ImageData.MSB_FIRST, 0, 0, destWidth, destHeight, dest_red_mask, dest_green_mask, dest_blue_mask,
				flipX, flipY);
		} else {
			ImageData.blit(ImageData.BLIT_SRC,
				image.data, image.depth, image.bytesPerLine, srcOrder, srcX, srcY, srcWidth, srcHeight, srcReds, srcGreens, srcBlues,
				ImageData.ALPHA_OPAQUE, null, 0,
				buf, destBitsPerPixel, destRowBytes, ImageData.MSB_FIRST, 0, 0, destWidth, destHeight, destReds, destGreens, destBlues,
				flipX, flipY);
		}
	}
	setPixMapData(drawable, buf);
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
 *    image.setBackground(b.getBackground());>
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
    /* AW
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
    */
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

////////////////////////////////////////////////////////
// Mac stuff
////////////////////////////////////////////////////////

	private static int rowBytes(int width, int depth) {
		return (((width*depth-1)/(8*DEFAULT_SCANLINE_PAD))+1)*DEFAULT_SCANLINE_PAD;
	}

	private static int createBitMap(int width, int height) {
		int rowBytes= rowBytes(width, 1);
		if (rowBytes > 0x3fff) {
			System.out.println("Image.createBitMap: rowBytes >= 0x4000");
			return 0;
		}
		int bitmap= newBitMap(width, height, rowBytes);
		if (bitmap == 0)
			SWT.error(SWT.ERROR_NO_HANDLES);
			
		initPixMapData(bitmap, rowBytes * height, 0);
		
		return bitmap;
	}
	
	private static int createPixMap(int width, int height, int depth) {
		int rowBytes= rowBytes(width, depth);
		if (rowBytes > 0x3fff) {
			System.out.println("Image.createPixMap: rowBytes >= 0x4000");
			return 0;
		}
		int pixmap= NewPixMap(width, height, depth, rowBytes);
		if (pixmap == 0)
			SWT.error(SWT.ERROR_NO_HANDLES);
			
		initPixMapData(pixmap, rowBytes * height, 0);

		return pixmap;
	}
	
	private static int NewPixMap(int w, int h, int depth, int rowBytes) {
		
		int pixelType= 0, pixelSize= 0, cmpSize= 0, cmpCount= 0, pixelFormat= 0;
		
		pixelFormat= depth;
		
		switch (depth) {
		case 1:
		case 2:
		case 4:
		case 8:
			pixelType= OS.Indexed;
			pixelSize= depth;
			cmpCount= 1;
			cmpSize= depth;
			break;
		
		case 16:
		case 24:
		case 32:
			pixelType= OS.RGBDirect;
			pixelSize= depth;
			if (depth == 16)
				cmpSize= 5;
			else
				cmpSize= 8;
			cmpCount= 3;
			break;
			
		default:
			break;
		}
		
		return OS.NewPixMap((short)w, (short)h, (short)rowBytes,
				(short)pixelType, (short)pixelSize,
				(short)cmpSize, (short)cmpCount, (short)pixelFormat);
	}
	
	public static void disposeBitmapOrPixmap(int handle) {

		if (handle == 0)
			return;
		if ((OS.getRowBytes(handle) & 0x8000) != 0) {	// Pixmap
			OS.DisposePixMap(handle);
			return;
		}
		
		int baseAddr= OS.getBaseAddr(handle);
		if (baseAddr != 0) {
			OS.DisposePtr(baseAddr);
			OS.setBaseAddr(handle, 0);
		}
		
		OS.DisposeHandle(handle);
	}
	
	//private static int fgIconCount;
	
	public static int carbon_createCIcon(Image image) {
		if (image == null)
			return 0;
		
		Rectangle r= image.getBounds();
		short w= (short)r.width;
		short h= (short)r.height;

		int pm= image.pixmap;
		
		if (pm != 0 && getDepth(pm) > 8) {
			
			//System.out.println("reduce depth");
		
			ImageData id= image.getImageData();
			
			int[] values= new int[256];
			int fill= 0;
		
			short depth= 8;
			int bytesPerRow= rowBytes(w, depth);
			byte[] data= new byte[bytesPerRow*h];
		
			short[] reds= new short[256];
			short[] greens= new short[256];
			short[] blues= new short[256];
	
			for (int y= 0; y < h; y++) {
				for (int x= 0; x < w; x++) {
					int index= -1;
					int value= id.getPixel(x, y);
					int i;
					for (i= 0; i < fill; i++) {
						if (value == values[i]) {
							index= i;
							break;
						}
					}
					if (i >= fill) {
						index= fill++;
						values[index]= value;
						reds[index]= (short)((value >> 16) & 0xFF);
						greens[index]= (short)((value >> 8) & 0xFF); 
						blues[index]= (short)(value & 0xFF); 
					}
					if (index >= 0)
						data[y*bytesPerRow+x]= (byte)index;
				}
			}
			pm= NewPixMap(w, h, depth, bytesPerRow);
			setColorTable(pm, reds, greens, blues);
			setPixMapData(pm, data);
		} else {
			System.out.println("---> CIcon: can use pixmap");
		}
		
		if (pm != 0) {
			
			int mask= image.mask;
			if (mask == 0) {
				//System.out.println("---> creating mask");
				int rowBytes= rowBytes(w, 1);
				mask= newBitMap(w, h, rowBytes);
				initPixMapData(mask, rowBytes*h, 0xff);
			}
			
			int icon= OS.NewCIcon(pm, mask);
			
			if (mask != image.mask)
				disposeBitmapOrPixmap(mask);
			
			//System.out.println("CIcons: " + fgIconCount++);
			
			return icon;
		}
		return 0; 
	}
	
	public static void DisposeCIcon(int iconHandle) {
		int iconData= OS.getCIconIconData(iconHandle);
		if (iconData != 0)
			OS.DisposeHandle(iconData);
			
		int colorTable= OS.getCIconColorTable(iconHandle);
		if (colorTable != 0)
			OS.DisposeHandle(colorTable);
				
		OS.DisposeHandle(iconHandle);
		//fgIconCount--;
	}
	
	private static void setColorTable(int pixmapHandle, short[] red, short[] green, short[] blue) {
		int n= Math.max(Math.max(red.length, green.length), blue.length);
		short[] colorSpec= new short[n*4];
		int j= 0;
		for (int i= 0; i < n; i++) {
			colorSpec[j++]= (short) i;
			colorSpec[j++]= (short) (red[i]*257);
			colorSpec[j++]= (short) (green[i]*257);
			colorSpec[j++]= (short) (blue[i]*257);
		}
		OS.setColorTable(pixmapHandle, colorSpec);
	}
	
	private static void setColorTable(int pixmapHandle, Color[] table) {
		int n= table.length;
		short[] colorSpec= new short[n*4];
		int j= 0;
		for (int i= 0; i < n; i++) {
			colorSpec[j++]= (short) i;
			colorSpec[j++]= (short) (table[i].getRed() * 257);
			colorSpec[j++]= (short) (table[i].getGreen() * 257);
			colorSpec[j++]= (short) (table[i].getBlue() * 257);
		}
		OS.setColorTable(pixmapHandle, colorSpec);
	}
	
	private static int getDepth(int bitmapHandle) {
		if ((OS.getRowBytes(bitmapHandle) & 0x8000) != 0)	// Pixmap
			return OS.GetPixDepth(bitmapHandle);
		return 1;
	}
	
	private static boolean isBitMap(int handle) {
		return (OS.getRowBytes(handle) & 0x8000) == 0;
	}
	
	private static int duplicate(int handle) {
		int rowBytes= OS.getRowBytes(handle);
		if ((rowBytes & 0x8000) == 0) {
			MacRect bounds= new MacRect();
			OS.GetPixBounds(handle, bounds.getData());
			int copy= newBitMap(bounds.getWidth(), bounds.getHeight(), rowBytes);
			int baseAddr= OS.getBaseAddr(handle);
			if (baseAddr != 0) {
				int size= OS.GetPtrSize(baseAddr);
				int data= OS.NewPtr(size);
				OS.memcpy(data, baseAddr, size);
				OS.setBaseAddr(copy, data);
			}
			return copy;
		}
		return OS.duplicatePixMap(handle);
	}
	
	private static int getRedMask(int depth) {
		switch (depth) {
		case 16:
			return 0x7C00;
		case 24:
		case 32:
			return 0xff0000;
		}
		return -1;
	}
	
	private static int getGreenMask(int depth) {
		switch (depth) {
		case 16:
			return 0x03E0;
		case 24:
		case 32:
			return 0x00ff00;
		}
		return -1;
	}
	
	private static int getBlueMask(int depth) {
		switch (depth) {
		case 16:
			return 0x001F;
		case 24:
		case 32:
			return 0x0000ff;
		}
		return -1;
	}
	
	private static void setPixMapData(int destPixMap, byte[] data) {
		int addr= OS.getBaseAddr(destPixMap);
		if (addr != 0) {
			OS.DisposePtr(addr);
		}
		addr= OS.NewPtr(data.length);
		OS.memcpy(addr, data, data.length);
		OS.setBaseAddr(destPixMap, addr);
	}
	
	private static void initPixMapData(int destPixMap, int size, int value) {
		int addr= OS.getBaseAddr(destPixMap);
		if (addr != 0) {
			OS.DisposePtr(addr);
		}
		if (value != 0) {
			addr= OS.NewPtr(size);
			OS.memset(addr, value, size);
		} else {
			addr= OS.NewPtrClear(size);
		}
		OS.setBaseAddr(destPixMap, addr);
	}
	
	private static void copyPixMapData(int srcPixMap, byte[] data) {
		int baseAddr= OS.getBaseAddr(srcPixMap);
		if (baseAddr != 0) {
			int l= OS.GetPtrSize(baseAddr);
			if (l == data.length) {
  				OS.memcpy(data, baseAddr, data.length);
   			} else {
   				System.err.println("Image.copyPixmapData: wrong lengths: " + l + " " + data.length);
			}
		}
	}
	
	private static int newBitMap(int width, int height, int rowBytes) {
		int bmh= OS.NewHandleClear(/* sizeof(BitMap) */ 14);
		OS.setRowBytes(bmh, (short) rowBytes);
		OS.setPixBounds(bmh, (short)0, (short)0, (short)height, (short)width);
		return bmh;
	}
	
	private static short[] getColorTable(int pixmapHandle) {
		int n= OS.getColorTableSize(pixmapHandle);
		if (n < 1)
			return null;
		short[] data= new short[n*4];
		OS.getColorTable(pixmapHandle, data);
		return data;
	}
	
	private static int getRGB(short[] colorTable, int pixel) {
		if (colorTable == null)
			return 0;
		int base= pixel*4;
		int red= colorTable[base+1] >> 8;
		int green= colorTable[base+2] >> 8;
		int blue= colorTable[base+3] >> 8;
		return (red << 16) + (green << 8) + blue;
	}
}
