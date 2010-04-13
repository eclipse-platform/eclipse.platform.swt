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


import org.eclipse.swt.internal.photon.*;
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
	 * specifies the default scanline padding
	 */
	static final int DEFAULT_SCANLINE_PAD = 4;

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
	if (srcImage == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (srcImage.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	switch (flag) {
		case SWT.IMAGE_COPY:
		case SWT.IMAGE_DISABLE:
			this.type = srcImage.type;
			int srcHandle = srcImage.handle;
			int newHandle = OS.PiDuplicateImage (srcHandle, 0);
			if (newHandle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
			if (flag == SWT.IMAGE_COPY) {
				/*
				* Bug in Photon.  The image returned by PiDuplicateImage might
				* have the same mask_bm/alpha as the original image.  The fix
				* is to detect this case and copy mask_bm/alpha if necessary.
				*/
				PhImage_t phImage = new PhImage_t();
				OS.memmove (phImage, srcHandle, PhImage_t.sizeof);
				PhImage_t newPhImage = new PhImage_t();
				OS.memmove(newPhImage, newHandle, PhImage_t.sizeof);
				if (newPhImage.mask_bm != 0 && phImage.mask_bm == newPhImage.mask_bm) {
					int length = newPhImage.mask_bpl * newPhImage.size_h;
					int ptr = OS.malloc(length);
					OS.memmove(ptr, newPhImage.mask_bm, length);
					newPhImage.mask_bm = ptr;
				}
				if (newPhImage.alpha != 0 && phImage.alpha == newPhImage.alpha) {
					PgAlpha_t alpha = new PgAlpha_t();
					OS.memmove(alpha, phImage.alpha, PgAlpha_t.sizeof);
					if (alpha.src_alpha_map_map != 0) {
						int length = alpha.src_alpha_map_bpl * alpha.src_alpha_map_dim_h;
						int ptr = OS.malloc(length);
						OS.memmove(ptr, alpha.src_alpha_map_map, length);
						alpha.src_alpha_map_map = ptr;
					}
					int ptr = OS.malloc(PgAlpha_t.sizeof);
					OS.memmove(ptr, alpha, PgAlpha_t.sizeof);
					newPhImage.alpha = ptr;
				}
				OS.memmove(newHandle, newPhImage, PhImage_t.sizeof);
				transparentPixel = srcImage.transparentPixel;
			} else {
				PhImage_t phImage = new PhImage_t();
				OS.PhMakeGhostBitmap(newHandle);
				OS.memmove (phImage, newHandle, PhImage_t.sizeof);
				phImage.mask_bm = phImage.ghost_bitmap;
				phImage.mask_bpl = phImage.ghost_bpl;
				phImage.ghost_bitmap = 0;
				phImage.ghost_bpl = 0;
				phImage.alpha = 0;
				OS.memmove (newHandle, phImage, PhImage_t.sizeof);
			}
			handle = newHandle;
			break;
		case SWT.IMAGE_GRAY:
			Rectangle r = srcImage.getBounds();
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
				newData = new ImageData(r.width, r.height, 8, new PaletteData(rgbs));
				newData.alpha = data.alpha;
				newData.alphaData = data.alphaData;
				newData.maskData = data.maskData;
				newData.maskPad = data.maskPad;
				if (data.transparentPixel != -1) newData.transparentPixel = 254; 

				/* Convert the pixels. */
				int[] scanline = new int[r.width];
				int redMask = palette.redMask;
				int greenMask = palette.greenMask;
				int blueMask = palette.blueMask;
				int redShift = palette.redShift;
				int greenShift = palette.greenShift;
				int blueShift = palette.blueShift;
				for (int y=0; y<r.height; y++) {
					int offset = y * newData.bytesPerLine;
					data.getPixels(0, y, r.width, scanline, 0);
					for (int x=0; x<r.width; x++) {
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
	init(new ImageData(filename));
	init();
}

void destroy() {
	if (memGC != null) memGC.dispose();
	destroyImage(handle);
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

	PhImage_t phImage = new PhImage_t();
	OS.memmove(phImage, handle, PhImage_t.sizeof);
	int color = 0;
	if ((phImage.type & OS.Pg_IMAGE_CLASS_MASK) == OS.Pg_IMAGE_CLASS_PALETTE) {
		int phPalette = phImage.palette;
		if (phPalette == 0 || transparentPixel > phImage.colors) return null;
		int[] pgColor = new int[1];
		OS.memmove(pgColor, phPalette + (transparentPixel * 4), 4);
		color = pgColor[0];
	} else {
		switch (phImage.type) {
 			case OS.Pg_IMAGE_DIRECT_888:
				color = ((transparentPixel & 0xFF) << 16) | (transparentPixel & 0xFF00) | ((transparentPixel & 0xFF0000) >> 16);
				break;
			case OS.Pg_IMAGE_DIRECT_8888:
				color = ((transparentPixel & 0xFF00) << 8) | ((transparentPixel & 0xFF0000) >> 8) | ((transparentPixel & 0xFF000000) >> 24);
				break;
			case OS.Pg_IMAGE_DIRECT_565:
				color = ((transparentPixel & 0xF800) << 8) | ((transparentPixel & 0x7E0) << 5) | ((transparentPixel & 0x1F) << 3);
				break;
			case OS.Pg_IMAGE_DIRECT_555:
				color = ((transparentPixel & 0x7C00) << 9) | ((transparentPixel & 0x3E0) << 6) | ((transparentPixel & 0x1F) << 3);
				break;
			case OS.Pg_IMAGE_DIRECT_444:
				color = ((transparentPixel & 0xF00) << 12) | ((transparentPixel & 0xF0) << 8) | ((transparentPixel & 0xF) << 4);
				break;
			default:
				return null;
		}
	}
	return Color.photon_new(device, color);
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
	PhImage_t image = new PhImage_t();
	OS.memmove(image, handle, PhImage_t.sizeof);
	return new Rectangle(0, 0, image.size_w, image.size_h);
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
	if (memGC != null) memGC.flushImage();
	PhImage_t phImage = new PhImage_t();
	OS.memmove(phImage, handle, PhImage_t.sizeof);
	int depth = 0;
	PaletteData palette = null;
	switch (phImage.type) {
		case OS.Pg_IMAGE_DIRECT_555:
			depth = 16;
			palette = new PaletteData(0x7C00,0x3E0,0x1F);
			break;
		case OS.Pg_IMAGE_DIRECT_565:
			depth = 16;
			palette = new PaletteData(0xF800,0x7E0,0x1F);
			break;
		case OS.Pg_IMAGE_DIRECT_444:     
			depth = 16;
			palette = new PaletteData(0xF00,0xF0,0xF);
			break;
		case OS.Pg_IMAGE_DIRECT_888:     
			depth = 24;
			palette = new PaletteData(0xFF,0xFF00,0xFF0000);
			break;
		case OS.Pg_IMAGE_DIRECT_8888:     
			depth = 32;
			palette = new PaletteData(0xFF00,0xFF0000,0xFF000000);
			break;
		case -1:
			depth = 1;
			palette = new PaletteData(new RGB[] {new RGB(0,0,0), new RGB(255,255,255)});
			break;			
		case OS.Pg_IMAGE_PALETTE_NIBBLE: 
		case OS.Pg_IMAGE_PALETTE_BYTE:
			depth = phImage.type == OS.Pg_IMAGE_PALETTE_BYTE ? 8 : 4;
			RGB[] rgbs = new RGB[phImage.colors];
			int[] colors = new int[phImage.colors];
			OS.memmove(colors, phImage.palette, colors.length * 4);
			for (int i = 0; i < rgbs.length; i++) {
				int rgb = colors[i];
				rgbs[i] = new RGB((rgb & 0xFF0000) >> 16, (rgb & 0xFF00) >> 8, rgb & 0xFF);
			}
			palette = new PaletteData(rgbs);
			break;
		default:
			SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
	}

	int scanLinePad, bpl = phImage.bpl;
	int width = phImage.size_w, height = phImage.size_h;
	int dataBytesPerLine = (width * depth + 7) / 8;
	for (scanLinePad = 1; scanLinePad < 128; scanLinePad++) {
		int calcBpl = (dataBytesPerLine + (scanLinePad - 1)) / scanLinePad * scanLinePad;
		if (bpl == calcBpl) break;
	}	
	byte[] data = new byte[height * bpl];
	OS.memmove(data, phImage.image, data.length);
	
	ImageData imageData = new ImageData(width, height, depth, palette, scanLinePad, data);

	if (transparentPixel != -1) {
		imageData.transparentPixel = transparentPixel;
	} else if (phImage.mask_bm != 0) {
		imageData.maskData = new byte[height * phImage.mask_bpl];
		OS.memmove(imageData.maskData, phImage.mask_bm, imageData.maskData.length);
		imageData.maskPad = 2;
	} else if (phImage.alpha != 0) {
		PgAlpha_t alpha = new PgAlpha_t();
		OS.memmove(alpha, phImage.alpha, PgAlpha_t.sizeof);
		imageData.alpha = alpha.src_global_alpha;
		if ((alpha.alpha_op & OS.Pg_ALPHA_OP_SRC_MAP) != 0 && alpha.src_alpha_map_map != 0) {
			int length = alpha.src_alpha_map_dim_w * alpha.src_alpha_map_dim_h;
			imageData.alphaData = new byte[length];
			OS.memmove(imageData.alphaData, alpha.src_alpha_map_map, length);
		}
	}
	
	return imageData;
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
	this.type = SWT.BITMAP;
	
	handle = OS.PhCreateImage(null, (short)width, (short)height, OS.Pg_IMAGE_DIRECT_888, 0, 0, 0);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
}

void init(ImageData i) {
	if (i == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	
	/*
	* Feature in Photon. Photon does not support 2-bit depth images and
	* memory contexts can not be created on 1 & 4-bit depth images.  The
	* fix is to create 8-bit depth images instead.
	*/
	if ((i.depth == 1 || i.depth == 2 || i.depth == 4) && !i.palette.isDirect) {
		ImageData img = new ImageData(i.width, i.height, 8, i.palette);
		ImageData.blit(ImageData.BLIT_SRC, 
			i.data, i.depth, i.bytesPerLine, img.getByteOrder(), 0, 0, i.width, i.height, null, null, null,
			ImageData.ALPHA_OPAQUE, null, 0, 0, 0,
			img.data, img.depth, img.bytesPerLine, img.getByteOrder(), 0, 0, img.width, img.height, null, null, null, 
			false, false);
		img.transparentPixel = i.transparentPixel;
		img.maskPad = i.maskPad;
		img.maskData = i.maskData;
		img.alpha = i.alpha;
		img.alphaData = i.alphaData;
		i = img;
	}

	int type = 0;
	int[] phPalette = null;
	if (!i.palette.isDirect) {
		switch (i.depth) {
			case 4: type = OS.Pg_IMAGE_PALETTE_NIBBLE; break;
			case 8: type = OS.Pg_IMAGE_PALETTE_BYTE; break;
			default: SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);			
		}
		RGB[] rgbs = i.palette.getRGBs();
		phPalette = new int[rgbs.length];
		for (int j=0; j<rgbs.length; j++) {
			RGB rgb = rgbs[j];
			phPalette[j] = ((rgb.red & 0xFF) << 16) | ((rgb.green & 0xFF) << 8) | (rgb.blue & 0xFF);
		}
	} else {
		final PaletteData palette = i.palette;
		final int redMask = palette.redMask;
		final int greenMask = palette.greenMask;
		final int blueMask = palette.blueMask;
		int newDepth = i.depth;
		int newOrder = ImageData.MSB_FIRST;
		PaletteData newPalette = null;

		switch (i.depth) {
			case 8:
				newDepth = 16;
				newOrder = ImageData.LSB_FIRST;
				newPalette = new PaletteData(0xF800, 0x7E0, 0x1F);
				type = OS.Pg_IMAGE_DIRECT_565;
				break;
			case 16:
				newOrder = ImageData.LSB_FIRST;
				if (redMask == 0x7C00 && greenMask == 0x3E0 && blueMask == 0x1F) {
					type = OS.Pg_IMAGE_DIRECT_555;
				} else if (redMask == 0xF800 && greenMask == 0x7E0 && blueMask == 0x1F) {
					type = OS.Pg_IMAGE_DIRECT_565;
				} else if (redMask == 0xF00 && greenMask == 0xF0 && blueMask == 0xF) {
					type = OS.Pg_IMAGE_DIRECT_444;
				} else {
					type = OS.Pg_IMAGE_DIRECT_565;
					newPalette = new PaletteData(0xF800, 0x7E0, 0x1F);
				}
				break;
			case 24:
				if (redMask == 0xFF && greenMask == 0xFF00 && blueMask == 0xFF0000) {
					type = OS.Pg_IMAGE_DIRECT_888;
				} else {
					type = OS.Pg_IMAGE_DIRECT_888;
					newPalette = new PaletteData(0xFF, 0xFF00, 0xFF0000);
				}
				break;
			case 32:
				if (redMask == 0xFF00 && greenMask == 0xFF0000 && blueMask == 0xFF000000) {
					type = OS.Pg_IMAGE_DIRECT_8888;
				} else {
					type = OS.Pg_IMAGE_DIRECT_8888;
					newPalette = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
				}
				break;			
			default: 
				SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
		}
		if (newPalette != null) {
			ImageData img = new ImageData(i.width, i.height, newDepth, newPalette);
			ImageData.blit(ImageData.BLIT_SRC, 
					i.data, i.depth, i.bytesPerLine, i.getByteOrder(), 0, 0, i.width, i.height, redMask, greenMask, blueMask,
					ImageData.ALPHA_OPAQUE, null, 0, 0, 0,
					img.data, img.depth, img.bytesPerLine, newOrder, 0, 0, img.width, img.height, newPalette.redMask, newPalette.greenMask, newPalette.blueMask,
					false, false);
			if (i.transparentPixel != -1) {
				img.transparentPixel = newPalette.getPixel(palette.getRGB(i.transparentPixel));
			}
			img.maskPad = i.maskPad;
			img.maskData = i.maskData;
			img.alpha = i.alpha;
			img.alphaData = i.alphaData;
			i = img;
		}
	}
	int handle = OS.malloc(PhImage_t.sizeof);
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	PhImage_t phImage = new PhImage_t();
	phImage.type = type;
	phImage.flags = (byte)OS.Ph_RELEASE_IMAGE_ALL;
	int size = i.data.length;
	int ptr = OS.malloc(size);
	if (ptr == 0) {
		OS.free(handle);
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	OS.memmove(ptr, i.data, size);
	phImage.image = ptr;
	phImage.size_w = (short)i.width;
	phImage.size_h = (short)i.height;
	phImage.bpl = i.bytesPerLine;
	if (phPalette != null) {
		size = phPalette.length * 4;
		ptr = OS.malloc(size);
		if (ptr == 0) {
			OS.free(phImage.image);
			OS.free(handle);
			SWT.error(SWT.ERROR_NO_HANDLES);
		}
		OS.memmove(ptr, phPalette, size);
		phImage.palette = ptr;
		phImage.colors = phPalette.length;
	}
	if (i.getTransparencyType() == SWT.TRANSPARENCY_MASK) {
		this.type = SWT.ICON;
		int maskBpl = (i.width * 1 + 7) / 8;
		maskBpl = (maskBpl + (i.maskPad - 1)) / i.maskPad * i.maskPad;
		size = maskBpl * i.height;		
		ptr = OS.malloc(size);
		if (ptr == 0) {
			if (phImage.palette != 0) OS.free(phImage.palette);
			OS.free(phImage.image);
			OS.free(handle);
			SWT.error(SWT.ERROR_NO_HANDLES);
		}
		OS.memmove(ptr, i.maskData, size); 
		phImage.mask_bm = ptr;
		phImage.mask_bpl = maskBpl;
	} else {
		this.type = SWT.BITMAP;
		if (i.transparentPixel != -1)	{
			/*
			* The PhImage_t field transparent can not used to store the
			* transparent pixel because it is overwritten when a GC is
			* created on the image.
			*/
			transparentPixel = i.transparentPixel;
		} else if (i.alpha != -1 || i.alphaData != null) {
			PgAlpha_t alpha = new PgAlpha_t();
			alpha.alpha_op = i.alpha != -1 ? OS.Pg_ALPHA_OP_SRC_GLOBAL : OS.Pg_ALPHA_OP_SRC_MAP;
			alpha.alpha_op |= OS.Pg_BLEND_SRC_SRC_ALPHA | OS.Pg_BLEND_DST_ONE_MINUS_SRC_ALPHA;
			alpha.src_global_alpha = (byte)i.alpha;
			if (i.alpha == -1 && i.alphaData != null) {
				ptr = OS.malloc(i.alphaData.length);
				if (ptr == 0) {
					if (phImage.palette != 0) OS.free(phImage.palette);
					OS.free(phImage.image);
					OS.free(handle);
					SWT.error(SWT.ERROR_NO_HANDLES);
				}
				OS.memmove(ptr, i.alphaData, i.alphaData.length);
				alpha.src_alpha_map_bpl = (short)i.width;
				alpha.src_alpha_map_dim_w = (short)i.width;
				alpha.src_alpha_map_dim_h = (short)i.height;
				alpha.src_alpha_map_map = ptr;
			}
			ptr = OS.malloc(PgAlpha_t.sizeof);
			if (ptr == 0) {
				if (alpha.src_alpha_map_map != 0) OS.free(alpha.src_alpha_map_map);
				if (phImage.palette != 0) OS.free(phImage.palette);
				OS.free(phImage.image);
				OS.free(handle);
				SWT.error(SWT.ERROR_NO_HANDLES);
			}
			OS.memmove(ptr, alpha, PgAlpha_t.sizeof);
			phImage.alpha = ptr;
		}
	}
	OS.memmove(handle, phImage, PhImage_t.sizeof);
	this.handle = handle;
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
public int internal_new_GC (GCData data) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	/*
	* Create a new GC that can draw into the image.
	* Only supported for bitmaps.
	*/
	if (type != SWT.BITMAP || memGC != null) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	PhImage_t phImage = new PhImage_t();
	OS.memmove(phImage, handle, PhImage_t.sizeof);
	PhDim_t dim = new PhDim_t();
	dim.w = phImage.size_w;
	dim.h = phImage.size_h;
	PhPoint_t trans = new PhPoint_t();
	int pmMC = OS.PmMemCreateMC(handle, dim, trans);
	if (pmMC == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
	if ((data.style & mask) == 0) {
		data.style |= SWT.LEFT_TO_RIGHT;
	}

	data.device = device;
	data.image = this;
	return pmMC;
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
public void internal_dispose_GC (int pmMC, GCData data) {
	OS.PmMemReleaseMC(pmMC);
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

	PhImage_t phImage = new PhImage_t();
	OS.memmove(phImage, handle, PhImage_t.sizeof);
	int phPalette = phImage.palette;
	if (phPalette == 0 || transparentPixel > phImage.colors) return;
	int[] pgColor = new int[]{ color.handle };
	OS.memmove(phPalette + (transparentPixel * 4), pgColor, 4);
}

static void destroyImage(int image) {
	if (image == 0) return;
	PhImage_t phImage = new PhImage_t();
	OS.memmove(phImage, image, PhImage_t.sizeof);
	phImage.flags = (byte)OS.Ph_RELEASE_IMAGE_ALL;
	OS.memmove(image, phImage, PhImage_t.sizeof);
	OS.PhReleaseImage(image);
	OS.free(image);
}

public static Image photon_new(Device device, int type, int handle) {
	Image image = new Image(device);
	image.type = type;
	image.handle = handle;
	return image;
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

}
