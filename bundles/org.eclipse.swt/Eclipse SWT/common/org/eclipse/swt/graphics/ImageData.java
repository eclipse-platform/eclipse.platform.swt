/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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

/**
 * Instances of this class are device-independent descriptions
 * of images. They are typically used as an intermediate format
 * between loading from or writing to streams and creating an
 * <code>Image</code>.
 * <p>
 * Note that the public fields <code>x</code>, <code>y</code>,
 * <code>disposalMethod</code> and <code>delayTime</code> are
 * typically only used when the image is in a set of images used
 * for animation.
 * </p>
 *
 * @see Image
 * @see ImageLoader
 * @see <a href="http://www.eclipse.org/swt/snippets/#image">ImageData snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ImageAnalyzer</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */

public final class ImageData implements Cloneable {

	/**
	 * The width of the image, in pixels.
	 */
	public int width;

	/**
	 * The height of the image, in pixels.
	 */
	public int height;

	/**
	 * The color depth of the image, in bits per pixel.
	 * <p>
	 * Note that a depth of 8 or less does not necessarily
	 * mean that the image is palette indexed, or
	 * conversely that a depth greater than 8 means that
	 * the image is direct color.  Check the associated
	 * PaletteData's isDirect field for such determinations.
	 */
	public int depth;

	/**
	 * The scanline padding.
	 * <p>
	 * If one scanline of the image is not a multiple of
	 * this number, it will be padded with zeros until it is.
	 * </p>
	 */
	public int scanlinePad;

	/**
	 * The number of bytes per scanline.
	 * <p>
	 * This is a multiple of the scanline padding.
	 * </p>
	 */
	public int bytesPerLine;

	/**
	 * The pixel data of the image.
	 * <p>
	 * Note that for 16 bit depth images the pixel data is stored
	 * in least significant byte order; however, for 24bit and
	 * 32bit depth images the pixel data is stored in most
	 * significant byte order.
	 * </p>
	 */
	public byte[] data;

	/**
	 * The color table for the image.
	 */
	public PaletteData palette;

	/**
	 * The transparent pixel.
	 * <p>
	 * Pixels with this value are transparent.
	 * </p><p>
	 * The default is -1 which means 'no transparent pixel'.
	 * </p>
	 */
	public int transparentPixel;

	/**
	 * An icon-specific field containing the data from the icon mask.
	 * <p>
	 * This is a 1 bit bitmap stored with the most significant
	 * bit first.  The number of bytes per scanline is
	 * '((width + 7) / 8 + (maskPad - 1)) / maskPad * maskPad'.
	 * </p><p>
	 * The default is null which means 'no transparency mask'.
	 * </p>
	 */
	public byte[] maskData;

	/**
	 * An icon-specific field containing the scanline pad of the mask.
	 * <p>
	 * If one scanline of the transparency mask is not a
	 * multiple of this number, it will be padded with zeros until
	 * it is.
	 * </p>
	 */
	public int maskPad;

	/**
	 * The alpha data of the image.
	 * <p>
	 * Every pixel can have an <em>alpha blending</em> value that
	 * varies from 0, meaning fully transparent, to 255 meaning
	 * fully opaque.  The number of bytes per scanline is
	 * 'width'.
	 * </p>
	 */
	public byte[] alphaData;

	/**
	 * The global alpha value to be used for every pixel.
	 * <p>
	 * If this value is set, the <code>alphaData</code> field
	 * is ignored and when the image is rendered each pixel
	 * will be blended with the background an amount
	 * proportional to this value.
	 * </p><p>
	 * The default is -1 which means 'no global alpha value'
	 * </p>
	 */
	public int alpha;

	/**
	 * The type of file from which the image was read.
	 *
	 * It is expressed as one of the following values:
	 * <dl>
	 * <dt><code>IMAGE_BMP</code></dt>
	 * <dd>Windows BMP file format, no compression</dd>
	 * <dt><code>IMAGE_BMP_RLE</code></dt>
	 * <dd>Windows BMP file format, RLE compression if appropriate</dd>
	 * <dt><code>IMAGE_GIF</code></dt>
	 * <dd>GIF file format</dd>
	 * <dt><code>IMAGE_ICO</code></dt>
	 * <dd>Windows ICO file format</dd>
	 * <dt><code>IMAGE_JPEG</code></dt>
	 * <dd>JPEG file format</dd>
	 * <dt><code>IMAGE_PNG</code></dt>
	 * <dd>PNG file format</dd>
	 * </dl>
	 */
	public int type;

	/**
	 * The x coordinate of the top left corner of the image
	 * within the logical screen (this field corresponds to
	 * the GIF89a Image Left Position value).
	 */
	public int x;

	/**
	 * The y coordinate of the top left corner of the image
	 * within the logical screen (this field corresponds to
	 * the GIF89a Image Top Position value).
	 */
	public int y;

	/**
	 * A description of how to dispose of the current image
	 * before displaying the next.
	 *
	 * It is expressed as one of the following values:
	 * <dl>
	 * <dt><code>DM_UNSPECIFIED</code></dt>
	 * <dd>disposal method not specified</dd>
	 * <dt><code>DM_FILL_NONE</code></dt>
	 * <dd>do nothing - leave the image in place</dd>
	 * <dt><code>DM_FILL_BACKGROUND</code></dt>
	 * <dd>fill with the background color</dd>
	 * <dt><code>DM_FILL_PREVIOUS</code></dt>
	 * <dd>restore the previous picture</dd>
	 * </dl>
	 * (this field corresponds to the GIF89a Disposal Method value)
	 */
	public int disposalMethod;

	/**
	 * The time to delay before displaying the next image
	 * in an animation (this field corresponds to the GIF89a
	 * Delay Time value).
	 */
	public int delayTime;

	/**
	 * Arbitrary channel width data to 8-bit conversion table.
	 */
	static final byte[][] ANY_TO_EIGHT = new byte[9][];
	static {
		for (int b = 0; b < 9; ++b) {
			byte[] data = ANY_TO_EIGHT[b] = new byte[1 << b];
			if (b == 0) continue;
			int inc = 0;
			for (int bit = 0x10000; (bit >>= b) != 0;) inc |= bit;
			for (int v = 0, p = 0; v < 0x10000; v+= inc) data[p++] = (byte)(v >> 8);
		}
	}

	/**
	 * Scaled 8x8 Bayer dither matrix.
	 */
	static final int[][] DITHER_MATRIX = {
		{ 0xfc0000, 0x7c0000, 0xdc0000, 0x5c0000, 0xf40000, 0x740000, 0xd40000, 0x540000 },
		{ 0x3c0000, 0xbc0000, 0x1c0000, 0x9c0000, 0x340000, 0xb40000, 0x140000, 0x940000 },
		{ 0xcc0000, 0x4c0000, 0xec0000, 0x6c0000, 0xc40000, 0x440000, 0xe40000, 0x640000 },
		{ 0x0c0000, 0x8c0000, 0x2c0000, 0xac0000, 0x040000, 0x840000, 0x240000, 0xa40000 },
		{ 0xf00000, 0x700000, 0xd00000, 0x500000, 0xf80000, 0x780000, 0xd80000, 0x580000 },
		{ 0x300000, 0xb00000, 0x100000, 0x900000, 0x380000, 0xb80000, 0x180000, 0x980000 },
		{ 0xc00000, 0x400000, 0xe00000, 0x600000, 0xc80000, 0x480000, 0xe80000, 0x680000 },
		{ 0x000000, 0x800000, 0x200000, 0xa00000, 0x080000, 0x880000, 0x280000, 0xa80000 }
	};

/**
 * Constructs a new, empty ImageData with the given width, height,
 * depth and palette. The data will be initialized to an (all zero)
 * array of the appropriate size.
 *
 * @param width the width of the image
 * @param height the height of the image
 * @param depth the depth of the image
 * @param palette the palette of the image (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the width or height is zero or negative, or if the depth is not
 *        	one of 1, 2, 4, 8, 16, 24 or 32</li>
 *    <li>ERROR_NULL_ARGUMENT - if the palette is null</li>
 * </ul>
 */
public ImageData(int width, int height, int depth, PaletteData palette) {
	this(width, height, depth, palette,
		4, null, 0, null,
		null, -1, -1, SWT.IMAGE_UNDEFINED,
		0, 0, 0, 0);
}

/**
 * Constructs a new, empty ImageData with the given width, height,
 * depth, palette, scanlinePad and data.
 *
 * @param width the width of the image
 * @param height the height of the image
 * @param depth the depth of the image
 * @param palette the palette of the image
 * @param scanlinePad the padding of each line, in bytes
 * @param data the data of the image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the width or height is zero or negative, or if the depth is not
 *        	one of 1, 2, 4, 8, 16, 24 or 32, or the data array is too small to contain the image data</li>
 *    <li>ERROR_NULL_ARGUMENT - if the palette or data is null</li>
 *    <li>ERROR_CANNOT_BE_ZERO - if the scanlinePad is zero</li>
 * </ul>
 */
public ImageData(int width, int height, int depth, PaletteData palette, int scanlinePad, byte[] data) {
	this(width, height, depth, palette,
		scanlinePad, checkData(data), 0, null,
		null, -1, -1, SWT.IMAGE_UNDEFINED,
		0, 0, 0, 0);
}

/**
 * Constructs an <code>ImageData</code> loaded from the specified
 * input stream. Throws an error if an error occurs while loading
 * the image, or if the image has an unsupported type.  Application
 * code is still responsible for closing the input stream.
 * <p>
 * This constructor is provided for convenience when loading a single
 * image only. If the stream contains multiple images, only the first
 * one will be loaded. To load multiple images, use
 * <code>ImageLoader.load()</code>.
 * </p><p>
 * This constructor may be used to load a resource as follows:
 * </p>
 * <pre>
 *     static ImageData loadImageData (Class clazz, String string) {
 *          InputStream stream = clazz.getResourceAsStream (string);
 *          if (stream == null) return null;
 *          ImageData imageData = null;
 *          try {
 *               imageData = new ImageData (stream);
 *          } catch (SWTException ex) {
 *          } finally {
 *               try {
 *                    stream.close ();
 *               } catch (IOException ex) {}
 *          }
 *          return imageData;
 *     }
 * </pre>
 *
 * @param stream the input stream to load the image from (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the stream is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_IO - if an IO error occurs while reading from the stream</li>
 *    <li>ERROR_INVALID_IMAGE - if the image stream contains invalid data</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image stream contains an unrecognized format</li>
 * </ul>
 *
 * @see ImageLoader#load(InputStream)
 */
public ImageData(InputStream stream) {
	ImageData[] data = ImageDataLoader.load(stream);
	if (data.length < 1) SWT.error(SWT.ERROR_INVALID_IMAGE);
	ImageData i = data[0];
	setAllFields(
		i.width,
		i.height,
		i.depth,
		i.scanlinePad,
		i.bytesPerLine,
		i.data,
		i.palette,
		i.transparentPixel,
		i.maskData,
		i.maskPad,
		i.alphaData,
		i.alpha,
		i.type,
		i.x,
		i.y,
		i.disposalMethod,
		i.delayTime);
}

/**
 * Constructs an <code>ImageData</code> loaded from a file with the
 * specified name. Throws an error if an error occurs loading the
 * image, or if the image has an unsupported type.
 * <p>
 * This constructor is provided for convenience when loading a single
 * image only. If the file contains multiple images, only the first
 * one will be loaded. To load multiple images, use
 * <code>ImageLoader.load()</code>.
 * </p>
 *
 * @param filename the name of the file to load the image from (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the file name is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_IO - if an IO error occurs while reading from the file</li>
 *    <li>ERROR_INVALID_IMAGE - if the image file contains invalid data</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image file contains an unrecognized format</li>
 * </ul>
 */
public ImageData(String filename) {
	ImageData[] data = ImageDataLoader.load(filename);
	if (data.length < 1) SWT.error(SWT.ERROR_INVALID_IMAGE);
	ImageData i = data[0];
	setAllFields(
		i.width,
		i.height,
		i.depth,
		i.scanlinePad,
		i.bytesPerLine,
		i.data,
		i.palette,
		i.transparentPixel,
		i.maskData,
		i.maskPad,
		i.alphaData,
		i.alpha,
		i.type,
		i.x,
		i.y,
		i.disposalMethod,
		i.delayTime);
}

/**
 * Prevents uninitialized instances from being created outside the package.
 */
ImageData() {
}

/**
 * Constructs an image data by giving values for all non-computable fields.
 * <p>
 * This method is for internal use, and is not described further.
 * </p>
 */
ImageData(
	int width, int height, int depth, PaletteData palette,
	int scanlinePad, byte[] data, int maskPad, byte[] maskData,
	byte[] alphaData, int alpha, int transparentPixel, int type,
	int x, int y, int disposalMethod, int delayTime)
{

	if (palette == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (!(depth == 1 || depth == 2 || depth == 4 || depth == 8
		|| depth == 16 || depth == 24 || depth == 32)) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (width <= 0 || height <= 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (scanlinePad == 0) SWT.error (SWT.ERROR_CANNOT_BE_ZERO);

	int bytesPerLine = (((width * depth + 7) / 8) + (scanlinePad - 1))
		/ scanlinePad * scanlinePad;

	/*
	 * When the image is being loaded from a PNG, we need to use the theoretical minimum
	 * number of bytes per line to check whether there is enough data, because the actual
	 * number of bytes per line is calculated based on the given depth, which may be larger
	 * than the actual depth of the PNG.
	 */
	int minBytesPerLine = type == SWT.IMAGE_PNG ? ((((width + 7) / 8) + 3) / 4) * 4 : bytesPerLine;
	if (data != null && data.length < minBytesPerLine * height) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	setAllFields(
		width,
		height,
		depth,
		scanlinePad,
		bytesPerLine,
		data != null ? data : new byte[bytesPerLine * height],
		palette,
		transparentPixel,
		maskData,
		maskPad,
		alphaData,
		alpha,
		type,
		x,
		y,
		disposalMethod,
		delayTime);
}

/**
 * Initializes all fields in the receiver. This method must be called
 * by all public constructors to ensure that all fields are initialized
 * for a new ImageData object. If a new field is added to the class,
 * then it must be added to this method.
 * <p>
 * This method is for internal use, and is not described further.
 * </p>
 */
void setAllFields(int width, int height, int depth, int scanlinePad,
	int bytesPerLine, byte[] data, PaletteData palette, int transparentPixel,
	byte[] maskData, int maskPad, byte[] alphaData, int alpha,
	int type, int x, int y, int disposalMethod, int delayTime) {

	this.width = width;
	this.height = height;
	this.depth = depth;
	this.scanlinePad = scanlinePad;
	this.bytesPerLine = bytesPerLine;
	this.data = data;
	this.palette = palette;
	this.transparentPixel = transparentPixel;
	this.maskData = maskData;
	this.maskPad = maskPad;
	this.alphaData = alphaData;
	this.alpha = alpha;
	this.type = type;
	this.x = x;
	this.y = y;
	this.disposalMethod = disposalMethod;
	this.delayTime = delayTime;
}

/**
 * Invokes internal SWT functionality to create a new instance of
 * this class.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>ImageData</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is subject
 * to change without notice, and should never be called from
 * application code.
 * </p>
 * <p>
 * This method is for internal use, and is not described further.
 * </p>
 *
 * @noreference This method is not intended to be referenced by clients.
 */
public static ImageData internal_new(
	int width, int height, int depth, PaletteData palette,
	int scanlinePad, byte[] data, int maskPad, byte[] maskData,
	byte[] alphaData, int alpha, int transparentPixel, int type,
	int x, int y, int disposalMethod, int delayTime)
{
	return new ImageData(
		width, height, depth, palette, scanlinePad, data, maskPad, maskData,
		alphaData, alpha, transparentPixel, type, x, y, disposalMethod, delayTime);
}

ImageData colorMaskImage(int pixel) {
	ImageData mask = new ImageData(width, height, 1, bwPalette(),
		2, null, 0, null, null, -1, -1, SWT.IMAGE_UNDEFINED,
		0, 0, 0, 0);
	int[] row = new int[width];
	for (int y = 0; y < height; y++) {
		getPixels(0, y, width, row, 0);
		for (int i = 0; i < width; i++) {
			if (pixel != -1 && row[i] == pixel) {
				row[i] = 0;
			} else {
				row[i] = 1;
			}
		}
		mask.setPixels(0, y, width, row, 0);
	}
	return mask;
}

static byte[] checkData(byte [] data) {
	if (data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return data;
}

/**
 * Returns a new instance of the same class as the receiver,
 * whose slots have been filled in with <em>copies</em> of
 * the values in the slots of the receiver. That is, the
 * returned object is a <em>deep copy</em> of the receiver.
 *
 * @return a copy of the receiver.
 */
@Override
public Object clone() {
	byte[] cloneData = new byte[data.length];
	System.arraycopy(data, 0, cloneData, 0, data.length);
	byte[] cloneMaskData = null;
	if (maskData != null) {
		cloneMaskData = new byte[maskData.length];
		System.arraycopy(maskData, 0, cloneMaskData, 0, maskData.length);
	}
	byte[] cloneAlphaData = null;
	if (alphaData != null) {
		cloneAlphaData = new byte[alphaData.length];
		System.arraycopy(alphaData, 0, cloneAlphaData, 0, alphaData.length);
	}
	return new ImageData(
		width,
		height,
		depth,
		palette,
		scanlinePad,
		cloneData,
		maskPad,
		cloneMaskData,
		cloneAlphaData,
		alpha,
		transparentPixel,
		type,
		x,
		y,
		disposalMethod,
		delayTime);
}

/**
 * Returns the alpha value at offset <code>x</code> in
 * scanline <code>y</code> in the receiver's alpha data.
 * The alpha value is between 0 (transparent) and
 * 255 (opaque).
 *
 * @param x the x coordinate of the pixel to get the alpha value of
 * @param y the y coordinate of the pixel to get the alpha value of
 * @return the alpha value at the given coordinates
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if either argument is out of range</li>
 * </ul>
 */
public int getAlpha(int x, int y) {
	if (x >= width || y >= height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

	if (alphaData == null) return 255;
	return alphaData[y * width + x] & 0xFF;
}

/**
 * Returns <code>getWidth</code> alpha values starting at offset
 * <code>x</code> in scanline <code>y</code> in the receiver's alpha
 * data starting at <code>startIndex</code>. The alpha values
 * are unsigned, between <code>(byte)0</code> (transparent) and
 * <code>(byte)255</code> (opaque).
 *
 * @param x the x position of the pixel to begin getting alpha values
 * @param y the y position of the pixel to begin getting alpha values
 * @param getWidth the width of the data to get
 * @param alphas the buffer in which to put the alpha values
 * @param startIndex the offset into the image to begin getting alpha values
 *
 * @exception IndexOutOfBoundsException if getWidth is too large
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 *    <li>ERROR_INVALID_ARGUMENT - if getWidth is negative</li>
 * </ul>
 */
public void getAlphas(int x, int y, int getWidth, byte[] alphas, int startIndex) {
	if (alphas == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (getWidth < 0 || x >= width || y >= height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (getWidth == 0) return;

	if (alphaData == null) {
		int endIndex = startIndex + getWidth;
		for (int i = startIndex; i < endIndex; i++) {
			alphas[i] = (byte)255;
		}
		return;
	}
	// may throw an IndexOutOfBoundsException
	System.arraycopy(alphaData, y * width + x, alphas, startIndex, getWidth);
}

/**
 * Returns the pixel value at offset <code>x</code> in
 * scanline <code>y</code> in the receiver's data.
 *
 * @param x the x position of the pixel to get
 * @param y the y position of the pixel to get
 * @return the pixel at the given coordinates
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if either argument is out of bounds</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH if the depth is not one of 1, 2, 4, 8, 16, 24 or 32</li>
 * </ul>
 */
public int getPixel(int x, int y) {
	if (x >= width || y >= height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int index;
	int theByte;
	int mask;
	switch (depth) {
		case 32:
			index = (y * bytesPerLine) + (x * 4);
			return ((data[index] & 0xFF) << 24) + ((data[index+1] & 0xFF) << 16) +
					((data[index+2] & 0xFF) << 8) + (data[index+3] & 0xFF);
		case 24:
			index = (y * bytesPerLine) + (x * 3);
			return ((data[index] & 0xFF) << 16) + ((data[index+1] & 0xFF) << 8) +
				(data[index+2] & 0xFF);
		case 16:
			index = (y * bytesPerLine) + (x * 2);
			return ((data[index+1] & 0xFF) << 8) + (data[index] & 0xFF);
		case 8:
			index = (y * bytesPerLine) + x ;
			return data[index] & 0xFF;
		case 4:
			index = (y * bytesPerLine) + (x >> 1);
			theByte = data[index] & 0xFF;
			if ((x & 0x1) == 0) {
				return theByte >> 4;
			} else {
				return theByte & 0x0F;
			}
		case 2:
			index = (y * bytesPerLine) + (x >> 2);
			theByte = data[index] & 0xFF;
			int offset = 3 - (x % 4);
			mask = 3 << (offset * 2);
			return (theByte & mask) >> (offset * 2);
		case 1:
			index = (y * bytesPerLine) + (x >> 3);
			theByte = data[index] & 0xFF;
			mask = 1 << (7 - (x & 0x7));
			if ((theByte & mask) == 0) {
				return 0;
			} else {
				return 1;
			}
	}
	SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
	return 0;
}

/**
 * Returns <code>getWidth</code> pixel values starting at offset
 * <code>x</code> in scanline <code>y</code> in the receiver's
 * data starting at <code>startIndex</code>.
 *
 * @param x the x position of the first pixel to get
 * @param y the y position of the first pixel to get
 * @param getWidth the width of the data to get
 * @param pixels the buffer in which to put the pixels
 * @param startIndex the offset into the byte array to begin storing pixels
 *
 * @exception IndexOutOfBoundsException if getWidth is too large
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 *    <li>ERROR_INVALID_ARGUMENT - if getWidth is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the depth is not one of 1, 2, 4 or 8
 *        (For higher depths, use the int[] version of this method.)</li>
 * </ul>
 */
public void getPixels(int x, int y, int getWidth, byte[] pixels, int startIndex) {
	if (pixels == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (getWidth < 0 || x >= width || y >= height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (getWidth == 0) return;
	int index;
	int theByte;
	int mask = 0;
	int n = getWidth;
	int i = startIndex;
	int srcX = x, srcY = y;
	switch (depth) {
		case 8:
			index = (y * bytesPerLine) + x;
			for (int j = 0; j < getWidth; j++) {
				pixels[i] = data[index];
				i++;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					index++;
				}
			}
			return;
		case 4:
			index = (y * bytesPerLine) + (x >> 1);
			if ((x & 0x1) == 1) {
				theByte = data[index] & 0xFF;
				pixels[i] = (byte)(theByte & 0x0F);
				i++;
				n--;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					index++;
				}
			}
			while (n > 1) {
				theByte = data[index] & 0xFF;
				pixels[i] = (byte)(theByte >> 4);
				i++;
				n--;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					pixels[i] = (byte)(theByte & 0x0F);
					i++;
					n--;
					srcX++;
					if (srcX >= width) {
						srcY++;
						index = srcY * bytesPerLine;
						srcX = 0;
					} else {
						index++;
					}
				}
			}
			if (n > 0) {
				theByte = data[index] & 0xFF;
				pixels[i] = (byte)(theByte >> 4);
			}
			return;
		case 2:
			index = (y * bytesPerLine) + (x >> 2);
			theByte = data[index] & 0xFF;
			int offset;
			while (n > 0) {
				offset = 3 - (srcX % 4);
				mask = 3 << (offset * 2);
				pixels[i] = (byte)((theByte & mask) >> (offset * 2));
				i++;
				n--;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					if (n > 0) theByte = data[index] & 0xFF;
					srcX = 0;
				} else {
					if (offset == 0) {
						index++;
						theByte = data[index] & 0xFF;
					}
				}
			}
			return;
		case 1:
			index = (y * bytesPerLine) + (x >> 3);
			theByte = data[index] & 0xFF;
			while (n > 0) {
				mask = 1 << (7 - (srcX & 0x7));
				if ((theByte & mask) == 0) {
					pixels[i] = 0;
				} else {
					pixels[i] = 1;
				}
				i++;
				n--;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					if (n > 0) theByte = data[index] & 0xFF;
					srcX = 0;
				} else {
					if (mask == 1) {
						index++;
						if (n > 0) theByte = data[index] & 0xFF;
					}
				}
			}
			return;
	}
	SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
}

/**
 * Returns <code>getWidth</code> pixel values starting at offset
 * <code>x</code> in scanline <code>y</code> in the receiver's
 * data starting at <code>startIndex</code>.
 *
 * @param x the x position of the first pixel to get
 * @param y the y position of the first pixel to get
 * @param getWidth the width of the data to get
 * @param pixels the buffer in which to put the pixels
 * @param startIndex the offset into the buffer to begin storing pixels
 *
 * @exception IndexOutOfBoundsException if getWidth is too large
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 *    <li>ERROR_INVALID_ARGUMENT - if getWidth is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the depth is not one of 1, 2, 4, 8, 16, 24 or 32</li>
 * </ul>
 */
public void getPixels(int x, int y, int getWidth, int[] pixels, int startIndex) {
	if (pixels == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (getWidth < 0 || x >= width || y >= height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (getWidth == 0) return;
	int index;
	int theByte;
	int mask;
	int n = getWidth;
	int i = startIndex;
	int srcX = x, srcY = y;
	switch (depth) {
		case 32:
			index = (y * bytesPerLine) + (x * 4);
			i = startIndex;
			for (int j = 0; j < getWidth; j++) {
				pixels[i] = ((data[index] & 0xFF) << 24) | ((data[index+1] & 0xFF) << 16)
					| ((data[index+2] & 0xFF) << 8) | (data[index+3] & 0xFF);
				i++;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					index += 4;
				}
			}
			return;
		case 24:
			index = (y * bytesPerLine) + (x * 3);
			for (int j = 0; j < getWidth; j++) {
				pixels[i] = ((data[index] & 0xFF) << 16) | ((data[index+1] & 0xFF) << 8)
					| (data[index+2] & 0xFF);
				i++;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					index += 3;
				}
			}
			return;
		case 16:
			index = (y * bytesPerLine) + (x * 2);
			for (int j = 0; j < getWidth; j++) {
				pixels[i] = ((data[index+1] & 0xFF) << 8) + (data[index] & 0xFF);
				i++;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					index += 2;
				}
			}
			return;
		case 8:
			index = (y * bytesPerLine) + x;
			for (int j = 0; j < getWidth; j++) {
				pixels[i] = data[index] & 0xFF;
				i++;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					index++;
				}
			}
			return;
		case 4:
			index = (y * bytesPerLine) + (x >> 1);
			if ((x & 0x1) == 1) {
				theByte = data[index] & 0xFF;
				pixels[i] = theByte & 0x0F;
				i++;
				n--;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					index++;
				}
			}
			while (n > 1) {
				theByte = data[index] & 0xFF;
				pixels[i] = theByte >> 4;
				i++;
				n--;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					pixels[i] = theByte & 0x0F;
					i++;
					n--;
					srcX++;
					if (srcX >= width) {
						srcY++;
						index = srcY * bytesPerLine;
						srcX = 0;
					} else {
						index++;
					}
				}
			}
			if (n > 0) {
				theByte = data[index] & 0xFF;
				pixels[i] = theByte >> 4;
			}
			return;
		case 2:
			index = (y * bytesPerLine) + (x >> 2);
			theByte = data[index] & 0xFF;
			int offset;
			while (n > 0) {
				offset = 3 - (srcX % 4);
				mask = 3 << (offset * 2);
				pixels[i] = (byte)((theByte & mask) >> (offset * 2));
				i++;
				n--;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					if (n > 0) theByte = data[index] & 0xFF;
					srcX = 0;
				} else {
					if (offset == 0) {
						index++;
						theByte = data[index] & 0xFF;
					}
				}
			}
			return;
		case 1:
			index = (y * bytesPerLine) + (x >> 3);
			theByte = data[index] & 0xFF;
			while (n > 0) {
				mask = 1 << (7 - (srcX & 0x7));
				if ((theByte & mask) == 0) {
					pixels[i] = 0;
				} else {
					pixels[i] = 1;
				}
				i++;
				n--;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					if (n > 0) theByte = data[index] & 0xFF;
					srcX = 0;
				} else {
					if (mask == 1) {
						index++;
						if (n > 0) theByte = data[index] & 0xFF;
					}
				}
			}
			return;
	}
	SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
}

/**
 * Returns an array of <code>RGB</code>s which comprise the
 * indexed color table of the receiver, or null if the receiver
 * has a direct color model.
 *
 * @return the RGB values for the image or null if direct color
 *
 * @see PaletteData#getRGBs()
 */
public RGB[] getRGBs() {
	return palette.getRGBs();
}

/**
 * Returns an <code>ImageData</code> which specifies the
 * transparency mask information for the receiver. If the
 * receiver has no transparency or is not an icon, returns
 * an opaque mask.
 *
 * @return the transparency mask
 */
public ImageData getTransparencyMask() {
	int transparencyType = getTransparencyType();
	switch (transparencyType) {
		case SWT.TRANSPARENCY_ALPHA: return getTransparencyMaskFromAlphaData();
		case SWT.TRANSPARENCY_MASK: return new ImageData(width, height, 1, bwPalette(), maskPad, maskData);
		case SWT.TRANSPARENCY_PIXEL: return colorMaskImage(transparentPixel);
		default: return colorMaskImage(transparentPixel);
	}
}

ImageData getTransparencyMaskFromAlphaData() {
	ImageData mask = new ImageData(width, height, 1, bwPalette(), 2, null, 0, null, null, -1, -1, SWT.IMAGE_UNDEFINED, 0, 0, 0, 0);
	int offset = 0;
	for (int y = 0; y < height; y++) {
		for (int x = 0; x < width; x++) {
			byte a = alphaData[offset++];
			if (a == 0) {
				mask.setPixel(x, y, 0);
			} else {
				mask.setPixel(x, y, 1);
			}
		}
	}
	return mask;
}

/**
 * Returns the image transparency type, which will be one of
 * <code>SWT.TRANSPARENCY_NONE</code>, <code>SWT.TRANSPARENCY_MASK</code>,
 * <code>SWT.TRANSPARENCY_PIXEL</code> or <code>SWT.TRANSPARENCY_ALPHA</code>.
 *
 * @return the receiver's transparency type
 */
public int getTransparencyType() {
	if (maskData != null) return SWT.TRANSPARENCY_MASK;
	if (transparentPixel != -1) return SWT.TRANSPARENCY_PIXEL;
	if (alphaData != null) return SWT.TRANSPARENCY_ALPHA;
	return SWT.TRANSPARENCY_NONE;
}

/**
 * Returns the byte order of the receiver.
 *
 * @return MSB_FIRST or LSB_FIRST
 */
int getByteOrder() {
	return depth != 16 ? MSB_FIRST : LSB_FIRST;
}

/**
 * Returns a copy of the receiver which has been stretched or
 * shrunk to the specified size. If either the width or height
 * is negative, the resulting image will be inverted in the
 * associated axis.
 *
 * @param width the width of the new ImageData
 * @param height the height of the new ImageData
 * @return a scaled copy of the image
 */
public ImageData scaledTo(int width, int height) {
	/* Create a destination image with no data */
	final boolean flipX = (width < 0);
	if (flipX) width = - width;
	final boolean flipY = (height < 0);
	if (flipY) height = - height;

	ImageData dest = new ImageData(
		width, height, depth, palette,
		scanlinePad, null, 0, null,
		null, -1, transparentPixel, type,
		x, y, disposalMethod, delayTime);

	/* Scale the image contents */
	if (palette.isDirect) blit(
		this.data, this.depth, this.bytesPerLine, this.getByteOrder(), this.width, this.height, 0, 0, 0,
		dest.data, dest.depth, dest.bytesPerLine, dest.getByteOrder(), dest.width, dest.height, 0, 0, 0,
		flipX, flipY);
	else blit(
		this.data, this.depth, this.bytesPerLine, this.getByteOrder(), this.width, this.height,
		dest.data, dest.depth, dest.bytesPerLine, dest.getByteOrder(), dest.width, dest.height,
		flipX, flipY);

	/* Scale the image mask or alpha */
	if (maskData != null) {
		dest.maskPad = this.maskPad;
		int destBpl = (dest.width + 7) / 8;
		destBpl = (destBpl + (dest.maskPad - 1)) / dest.maskPad * dest.maskPad;
		dest.maskData = new byte[destBpl * dest.height];
		int srcBpl = (this.width + 7) / 8;
		srcBpl = (srcBpl + (this.maskPad - 1)) / this.maskPad * this.maskPad;
		blit(
			this.maskData, 1, srcBpl,  MSB_FIRST, this.width, this.height,
			dest.maskData, 1, destBpl, MSB_FIRST, dest.width, dest.height,
			flipX, flipY);
	} else if (alpha != -1) {
		dest.alpha = this.alpha;
	} else if (alphaData != null) {
		dest.alphaData = new byte[dest.width * dest.height];
		blit(
			this.alphaData, 8, this.width, MSB_FIRST, this.width, this.height,
			dest.alphaData, 8, dest.width, MSB_FIRST, dest.width, dest.height,
			flipX, flipY);
	}
	return dest;
}

/**
 * Sets the alpha value at offset <code>x</code> in
 * scanline <code>y</code> in the receiver's alpha data.
 * The alpha value must be between 0 (transparent)
 * and 255 (opaque).
 *
 * @param x the x coordinate of the alpha value to set
 * @param y the y coordinate of the alpha value to set
 * @param alpha the value to set the alpha to
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 *  </ul>
 */
public void setAlpha(int x, int y, int alpha) {
	if (x >= width || y >= height || x < 0 || y < 0 || alpha < 0 || alpha > 255)
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);

	if (alphaData == null) alphaData = new byte[width * height];
	alphaData[y * width + x] = (byte)alpha;
}

/**
 * Sets the alpha values starting at offset <code>x</code> in
 * scanline <code>y</code> in the receiver's alpha data to the
 * values from the array <code>alphas</code> starting at
 * <code>startIndex</code>. The alpha values must be between
 * <code>(byte)0</code> (transparent) and <code>(byte)255</code> (opaque)
 *
 * @param x the x coordinate of the pixel to being setting the alpha values
 * @param y the y coordinate of the pixel to being setting the alpha values
 * @param putWidth the width of the alpha values to set
 * @param alphas the alpha values to set
 * @param startIndex the index at which to begin setting
 *
 * @exception IndexOutOfBoundsException if putWidth is too large
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 *    <li>ERROR_INVALID_ARGUMENT - if putWidth is negative</li>
 * </ul>
 */
public void setAlphas(int x, int y, int putWidth, byte[] alphas, int startIndex) {
	if (alphas == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (putWidth < 0 || x >= width || y >= height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (putWidth == 0) return;

	if (alphaData == null) alphaData = new byte[width * height];
	// may throw an IndexOutOfBoundsException
	System.arraycopy(alphas, startIndex, alphaData, y * width + x, putWidth);
}

/**
 * Sets the pixel value at offset <code>x</code> in
 * scanline <code>y</code> in the receiver's data.
 *
 * @param x the x coordinate of the pixel to set
 * @param y the y coordinate of the pixel to set
 * @param pixelValue the value to set the pixel to
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH if the depth is not one of 1, 2, 4, 8, 16, 24 or 32</li>
 * </ul>
 */
public void setPixel(int x, int y, int pixelValue) {
	if (x >= width || y >= height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int index;
	byte theByte;
	int mask;
	switch (depth) {
		case 32:
			index = (y * bytesPerLine) + (x * 4);
			data[index]  = (byte)((pixelValue >> 24) & 0xFF);
			data[index + 1] = (byte)((pixelValue >> 16) & 0xFF);
			data[index + 2] = (byte)((pixelValue >> 8) & 0xFF);
			data[index + 3] = (byte)(pixelValue & 0xFF);
			return;
		case 24:
			index = (y * bytesPerLine) + (x * 3);
			data[index] = (byte)((pixelValue >> 16) & 0xFF);
			data[index + 1] = (byte)((pixelValue >> 8) & 0xFF);
			data[index + 2] = (byte)(pixelValue & 0xFF);
			return;
		case 16:
			index = (y * bytesPerLine) + (x * 2);
			data[index + 1] = (byte)((pixelValue >> 8) & 0xFF);
			data[index] = (byte)(pixelValue & 0xFF);
			return;
		case 8:
			index = (y * bytesPerLine) + x ;
			data[index] = (byte)(pixelValue & 0xFF);
			return;
		case 4:
			index = (y * bytesPerLine) + (x >> 1);
			if ((x & 0x1) == 0) {
				data[index] = (byte)((data[index] & 0x0F) | ((pixelValue & 0x0F) << 4));
			} else {
				data[index] = (byte)((data[index] & 0xF0) | (pixelValue & 0x0F));
			}
			return;
		case 2:
			index = (y * bytesPerLine) + (x >> 2);
			theByte = data[index];
			int offset = 3 - (x % 4);
			mask = 0xFF ^ (3 << (offset * 2));
			data[index] = (byte)((data[index] & mask) | (pixelValue << (offset * 2)));
			return;
		case 1:
			index = (y * bytesPerLine) + (x >> 3);
			theByte = data[index];
			mask = 1 << (7 - (x & 0x7));
			if ((pixelValue & 0x1) == 1) {
				data[index] = (byte)(theByte | mask);
			} else {
				data[index] = (byte)(theByte & (mask ^ -1));
			}
			return;
	}
	SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
}

/**
 * Sets the pixel values starting at offset <code>x</code> in
 * scanline <code>y</code> in the receiver's data to the
 * values from the array <code>pixels</code> starting at
 * <code>startIndex</code>.
 *
 * @param x the x position of the pixel to set
 * @param y the y position of the pixel to set
 * @param putWidth the width of the pixels to set
 * @param pixels the pixels to set
 * @param startIndex the index at which to begin setting
 *
 * @exception IndexOutOfBoundsException if putWidth is too large
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 *    <li>ERROR_INVALID_ARGUMENT - if putWidth is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH if the depth is not one of 1, 2, 4, 8
 *        (For higher depths, use the int[] version of this method.)</li>
 * </ul>
 */
public void setPixels(int x, int y, int putWidth, byte[] pixels, int startIndex) {
	if (pixels == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (putWidth < 0 || x >= width || y >= height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (putWidth == 0) return;
	int index;
	int theByte;
	int mask;
	int n = putWidth;
	int i = startIndex;
	int srcX = x, srcY = y;
	switch (depth) {
		case 8:
			index = (y * bytesPerLine) + x;
			for (int j = 0; j < putWidth; j++) {
				data[index] = (byte)(pixels[i] & 0xFF);
				i++;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					index++;
				}
			}
			return;
		case 4:
			index = (y * bytesPerLine) + (x >> 1);
			boolean high = (x & 0x1) == 0;
			while (n > 0) {
				theByte = pixels[i] & 0x0F;
				if (high) {
					data[index] = (byte)((data[index] & 0x0F) | (theByte << 4));
				} else {
					data[index] = (byte)((data[index] & 0xF0) | theByte);
				}
				i++;
				n--;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					high = true;
					srcX = 0;
				} else {
					if (!high) index++;
					high = !high;
				}
			}
			return;
		case 2:
			byte [] masks = { (byte)0xFC, (byte)0xF3, (byte)0xCF, (byte)0x3F };
			index = (y * bytesPerLine) + (x >> 2);
			int offset = 3 - (x % 4);
			while (n > 0) {
				theByte = pixels[i] & 0x3;
				data[index] = (byte)((data[index] & masks[offset]) | (theByte << (offset * 2)));
				i++;
				n--;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					offset = 0;
					srcX = 0;
				} else {
					if (offset == 0) {
						index++;
						offset = 3;
					} else {
						offset--;
					}
				}
			}
			return;
		case 1:
			index = (y * bytesPerLine) + (x >> 3);
			while (n > 0) {
				mask = 1 << (7 - (srcX & 0x7));
				if ((pixels[i] & 0x1) == 1) {
					data[index] = (byte)((data[index] & 0xFF) | mask);
				} else {
					data[index] = (byte)((data[index] & 0xFF) & (mask ^ -1));
				}
				i++;
				n--;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					if (mask == 1) {
						index++;
					}
				}
			}
			return;
	}
	SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
}

/**
 * Sets the pixel values starting at offset <code>x</code> in
 * scanline <code>y</code> in the receiver's data to the
 * values from the array <code>pixels</code> starting at
 * <code>startIndex</code>.
 *
 * @param x the x position of the pixel to set
 * @param y the y position of the pixel to set
 * @param putWidth the width of the pixels to set
 * @param pixels the pixels to set
 * @param startIndex the index at which to begin setting
 *
 * @exception IndexOutOfBoundsException if putWidth is too large
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 *    <li>ERROR_INVALID_ARGUMENT - if putWidth is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH if the depth is not one of 1, 2, 4, 8, 16, 24 or 32</li>
 * </ul>
 */
public void setPixels(int x, int y, int putWidth, int[] pixels, int startIndex) {
	if (pixels == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (putWidth < 0 || x >= width || y >= height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (putWidth == 0) return;
	int index;
	int theByte;
	int mask;
	int n = putWidth;
	int i = startIndex;
	int pixel;
	int srcX = x, srcY = y;
	switch (depth) {
		case 32:
			index = (y * bytesPerLine) + (x * 4);
			for (int j = 0; j < putWidth; j++) {
				pixel = pixels[i];
				data[index] = (byte)((pixel >> 24) & 0xFF);
				data[index + 1] = (byte)((pixel >> 16) & 0xFF);
				data[index + 2] = (byte)((pixel >> 8) & 0xFF);
				data[index + 3] = (byte)(pixel & 0xFF);
				i++;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					index += 4;
				}
			}
			return;
		case 24:
			index = (y * bytesPerLine) + (x * 3);
			for (int j = 0; j < putWidth; j++) {
				pixel = pixels[i];
				data[index] = (byte)((pixel >> 16) & 0xFF);
				data[index + 1] = (byte)((pixel >> 8) & 0xFF);
				data[index + 2] = (byte)(pixel & 0xFF);
				i++;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					index += 3;
				}
			}
			return;
		case 16:
			index = (y * bytesPerLine) + (x * 2);
			for (int j = 0; j < putWidth; j++) {
				pixel = pixels[i];
				data[index] = (byte)(pixel & 0xFF);
				data[index + 1] = (byte)((pixel >> 8) & 0xFF);
				i++;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					index += 2;
				}
			}
			return;
		case 8:
			index = (y * bytesPerLine) + x;
			for (int j = 0; j < putWidth; j++) {
				data[index] = (byte)(pixels[i] & 0xFF);
				i++;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					index++;
				}
			}
			return;
		case 4:
			index = (y * bytesPerLine) + (x >> 1);
			boolean high = (x & 0x1) == 0;
			while (n > 0) {
				theByte = pixels[i] & 0x0F;
				if (high) {
					data[index] = (byte)((data[index] & 0x0F) | (theByte << 4));
				} else {
					data[index] = (byte)((data[index] & 0xF0) | theByte);
				}
				i++;
				n--;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					high = true;
					srcX = 0;
				} else {
					if (!high) index++;
					high = !high;
				}
			}
			return;
		case 2:
			byte [] masks = { (byte)0xFC, (byte)0xF3, (byte)0xCF, (byte)0x3F };
			index = (y * bytesPerLine) + (x >> 2);
			int offset = 3 - (x % 4);
			while (n > 0) {
				theByte = pixels[i] & 0x3;
				data[index] = (byte)((data[index] & masks[offset]) | (theByte << (offset * 2)));
				i++;
				n--;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					offset = 3;
					srcX = 0;
				} else {
					if (offset == 0) {
						index++;
						offset = 3;
					} else {
						offset--;
					}
				}
			}
			return;
		case 1:
			index = (y * bytesPerLine) + (x >> 3);
			while (n > 0) {
				mask = 1 << (7 - (srcX & 0x7));
				if ((pixels[i] & 0x1) == 1) {
					data[index] = (byte)((data[index] & 0xFF) | mask);
				} else {
					data[index] = (byte)((data[index] & 0xFF) & (mask ^ -1));
				}
				i++;
				n--;
				srcX++;
				if (srcX >= width) {
					srcY++;
					index = srcY * bytesPerLine;
					srcX = 0;
				} else {
					if (mask == 1) {
						index++;
					}
				}
			}
			return;
	}
	SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
}

/**
 * Returns a palette with 2 colors: black & white.
 */
static PaletteData bwPalette() {
	return new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255));
}

static ImageData convertMask(ImageData mask) {
	if (mask.depth == 1) return mask;
	PaletteData palette = new PaletteData(new RGB(0, 0, 0), new RGB(255,255,255));
	ImageData newMask = new ImageData(mask.width, mask.height, 1, palette);
	/* Find index of black in mask palette */
	int blackIndex = 0;
	RGB[] rgbs = mask.getRGBs();
	if (rgbs != null) {
		while (blackIndex < rgbs.length) {
			if (rgbs[blackIndex].equals(palette.colors[0])) break;
			blackIndex++;
		}
	}
	int[] pixels = new int[mask.width];
	for (int y = 0; y < mask.height; y++) {
		mask.getPixels(0, y, mask.width, pixels, 0);
		for (int i = 0; i < pixels.length; i++) {
			if (pixels[i] == blackIndex) {
				pixels[i] = 0;
			} else {
				pixels[i] = 1;
			}
		}
		newMask.setPixels(0, y, mask.width, pixels, 0);
	}
	return newMask;
}

static byte[] convertPad(byte[] data, int width, int height, int depth, int pad, int newPad) {
	if (pad == newPad) return data;
	int stride = (width * depth + 7) / 8;
	int bpl = (stride + (pad - 1)) / pad * pad;
	int newBpl = (stride + (newPad - 1)) / newPad * newPad;
	byte[] newData = new byte[height * newBpl];
	int srcIndex = 0, destIndex = 0;
	for (int y = 0; y < height; y++) {
		System.arraycopy(data, srcIndex, newData, destIndex, stride);
		srcIndex += bpl;
		destIndex += newBpl;
	}
	return newData;
}

/**
 * Byte and bit order constants.
 */
static final int LSB_FIRST = 0;
static final int MSB_FIRST = 1;

/**
 * Data types (internal)
 */
private static final int
	// direct / true color formats with arbitrary masks & shifts
	TYPE_GENERIC_8 = 0,
	TYPE_GENERIC_16_MSB = 1,
	TYPE_GENERIC_16_LSB = 2,
	TYPE_GENERIC_24 = 3,
	TYPE_GENERIC_32_MSB = 4,
	TYPE_GENERIC_32_LSB = 5,
	// palette indexed color formats
	TYPE_INDEX_16_LSB = 11,
	TYPE_INDEX_8 = 6,
	TYPE_INDEX_4 = 7,
	TYPE_INDEX_2 = 8,
	TYPE_INDEX_1_MSB = 9,
	TYPE_INDEX_1_LSB = 10;

/**
 * Blits a direct palette image into a direct palette image.
 * <p>
 * Note: When the source and destination depth, order and masks
 * are pairwise equal and the blitter operation is BLIT_SRC,
 * the masks are ignored.  Hence when not changing the image
 * data format, 0 may be specified for the masks.
 * </p>
 *
 * @param srcData the source byte array containing image data
 * @param srcDepth the source depth: one of 8, 16, 24, 32
 * @param srcStride the source number of bytes per line
 * @param srcOrder the source byte ordering: one of MSB_FIRST or LSB_FIRST;
 *        ignored if srcDepth is not 16 or 32
 * @param srcWidth the width of the source blit region
 * @param srcHeight the height of the source blit region
 * @param srcRedMask the source red channel mask
 * @param srcGreenMask the source green channel mask
 * @param srcBlueMask the source blue channel mask
 * @param destData the destination byte array containing image data
 * @param destDepth the destination depth: one of 8, 16, 24, 32
 * @param destStride the destination number of bytes per line
 * @param destOrder the destination byte ordering: one of MSB_FIRST or LSB_FIRST;
 *        ignored if destDepth is not 16 or 32
 * @param destWidth the width of the destination blit region
 * @param destHeight the height of the destination blit region
 * @param destRedMask the destination red channel mask
 * @param destGreenMask the destination green channel mask
 * @param destBlueMask the destination blue channel mask
 * @param flipX if true the resulting image is flipped along the vertical axis
 * @param flipY if true the resulting image is flipped along the horizontal axis
 */
static void blit(
	byte[] srcData, int srcDepth, int srcStride, int srcOrder,
	int srcWidth, int srcHeight,
	int srcRedMask, int srcGreenMask, int srcBlueMask,
	byte[] destData, int destDepth, int destStride, int destOrder,
	int destWidth, int destHeight,
	int destRedMask, int destGreenMask, int destBlueMask,
	boolean flipX, boolean flipY) {
	if ((destWidth <= 0) || (destHeight <= 0)) return;

	/*** Prepare scaling data ***/
	final int dwm1 = destWidth - 1;
	final int sfxi = (dwm1 != 0) ? (int)((((long)srcWidth << 16) - 1) / dwm1) : 0;
	final int dhm1 = destHeight - 1;
	final int sfyi = (dhm1 != 0) ? (int)((((long)srcHeight << 16) - 1) / dhm1) : 0;

	/*** Prepare source-related data ***/
	final int sbpp, stype;
	switch (srcDepth) {
		case 8:
			sbpp = 1;
			stype = TYPE_GENERIC_8;
			break;
		case 16:
			sbpp = 2;
			stype = (srcOrder == MSB_FIRST) ? TYPE_GENERIC_16_MSB : TYPE_GENERIC_16_LSB;
			break;
		case 24:
			sbpp = 3;
			stype = TYPE_GENERIC_24;
			break;
		case 32:
			sbpp = 4;
			stype = (srcOrder == MSB_FIRST) ? TYPE_GENERIC_32_MSB : TYPE_GENERIC_32_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid source type");
			return;
	}
	int spr = 0;

	/*** Prepare destination-related data ***/
	final int dbpp, dtype;
	switch (destDepth) {
		case 8:
			dbpp = 1;
			dtype = TYPE_GENERIC_8;
			break;
		case 16:
			dbpp = 2;
			dtype = (destOrder == MSB_FIRST) ? TYPE_GENERIC_16_MSB : TYPE_GENERIC_16_LSB;
			break;
		case 24:
			dbpp = 3;
			dtype = TYPE_GENERIC_24;
			break;
		case 32:
			dbpp = 4;
			dtype = (destOrder == MSB_FIRST) ? TYPE_GENERIC_32_MSB : TYPE_GENERIC_32_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid destination type");
			return;
	}
	int dpr = ((flipY) ? dhm1 : 0) * destStride + ((flipX) ? dwm1 : 0) * dbpp;
	final int dprxi = (flipX) ? -dbpp : dbpp;
	final int dpryi = (flipY) ? -destStride : destStride;

	/*** Blit ***/
	int dp = dpr;
	int sp = spr;
	if ((stype == dtype) &&
		(srcRedMask == destRedMask) &&
		(srcGreenMask == destGreenMask) &&
		(srcBlueMask == destBlueMask)) {
		/*** Fast blit (straight copy) ***/
		switch (sbpp) {
			case 1:
				for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
					for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
						destData[dp] = srcData[sp];
						sp += (sfx >>> 16);
					}
				}
				break;
			case 2:
				for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
					for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
						destData[dp] = srcData[sp];
						destData[dp + 1] = srcData[sp + 1];
						sp += (sfx >>> 16) * 2;
					}
				}
				break;
			case 3:
				for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
					for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
						destData[dp] = srcData[sp];
						destData[dp + 1] = srcData[sp + 1];
						destData[dp + 2] = srcData[sp + 2];
						sp += (sfx >>> 16) * 3;
					}
				}
				break;
			case 4:
				for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
					for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
						destData[dp] = srcData[sp];
						destData[dp + 1] = srcData[sp + 1];
						destData[dp + 2] = srcData[sp + 2];
						destData[dp + 3] = srcData[sp + 3];
						sp += (sfx >>> 16) * 4;
					}
				}
				break;
		}
		return;
	}
	/*Fast 32 to 32 blit */
	if (stype == TYPE_GENERIC_32_MSB && dtype == TYPE_GENERIC_32_MSB) {
		if (srcRedMask == 0xFF00 && srcGreenMask == 0xff0000 && srcBlueMask == 0xff000000 && destRedMask == 0xFF0000 && destGreenMask == 0xff00 && destBlueMask == 0xff) {
			for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
				for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
					destData[dp] = srcData[sp + 3];
					destData[dp + 1] = srcData[sp + 2];
					destData[dp + 2] = srcData[sp + 1];
					destData[dp + 3] = srcData[sp];
					sp += (sfx >>> 16) * 4;
				}
			}
			return;
		}
	}
	/*Fast 24 to 32 blit */
	if (stype == TYPE_GENERIC_24 && dtype == TYPE_GENERIC_32_MSB) {
		if (srcRedMask == 0xFF && srcGreenMask == 0xff00 && srcBlueMask == 0xff0000 && destRedMask == 0xFF0000 && destGreenMask == 0xff00 && destBlueMask == 0xff) {
			for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
				for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
					destData[dp] = 0;
					destData[dp + 1] = srcData[sp + 2];
					destData[dp + 2] = srcData[sp + 1];
					destData[dp + 3] = srcData[sp];
					sp += (sfx >>> 16) * 3;
				}
			}
			return;
		}
	}

	/*** Comprehensive blit (apply transformations) ***/
	final int srcRedShift = getChannelShift(srcRedMask);
	final byte[] srcReds = ANY_TO_EIGHT[getChannelWidth(srcRedMask, srcRedShift)];
	final int srcGreenShift = getChannelShift(srcGreenMask);
	final byte[] srcGreens = ANY_TO_EIGHT[getChannelWidth(srcGreenMask, srcGreenShift)];
	final int srcBlueShift = getChannelShift(srcBlueMask);
	final byte[] srcBlues = ANY_TO_EIGHT[getChannelWidth(srcBlueMask, srcBlueShift)];

	final int destRedShift = getChannelShift(destRedMask);
	final int destRedWidth = getChannelWidth(destRedMask, destRedShift);
	final int destRedPreShift = 8 - destRedWidth;
	final int destGreenShift = getChannelShift(destGreenMask);
	final int destGreenWidth = getChannelWidth(destGreenMask, destGreenShift);
	final int destGreenPreShift = 8 - destGreenWidth;
	final int destBlueShift = getChannelShift(destBlueMask);
	final int destBlueWidth = getChannelWidth(destBlueMask, destBlueShift);
	final int destBluePreShift = 8 - destBlueWidth;

	int r = 0, g = 0, b = 0;
	for (int dy = destHeight, sfy = sfyi; dy > 0; --dy,
			sp = spr += (sfy >>> 16) * srcStride,
			sfy = (sfy & 0xffff) + sfyi,
			dp = dpr += dpryi) {
		for (int dx = destWidth, sfx = sfxi; dx > 0; --dx,
				dp += dprxi,
				sfx = (sfx & 0xffff) + sfxi) {
			/*** READ NEXT PIXEL ***/
			switch (stype) {
				case TYPE_GENERIC_8: {
					final int data = srcData[sp] & 0xff;
					sp += (sfx >>> 16);
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
				} break;
				case TYPE_GENERIC_16_MSB: {
					final int data = ((srcData[sp] & 0xff) << 8) | (srcData[sp + 1] & 0xff);
					sp += (sfx >>> 16) * 2;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
				} break;
				case TYPE_GENERIC_16_LSB: {
					final int data = ((srcData[sp + 1] & 0xff) << 8) | (srcData[sp] & 0xff);
					sp += (sfx >>> 16) * 2;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
				} break;
				case TYPE_GENERIC_24: {
					final int data = (( ((srcData[sp] & 0xff) << 8) |
						(srcData[sp + 1] & 0xff)) << 8) |
						(srcData[sp + 2] & 0xff);
					sp += (sfx >>> 16) * 3;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
				} break;
				case TYPE_GENERIC_32_MSB: {
					final int data = (( (( ((srcData[sp] & 0xff) << 8) |
						(srcData[sp + 1] & 0xff)) << 8) |
						(srcData[sp + 2] & 0xff)) << 8) |
						(srcData[sp + 3] & 0xff);
					sp += (sfx >>> 16) * 4;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
				} break;
				case TYPE_GENERIC_32_LSB: {
					final int data = (( (( ((srcData[sp + 3] & 0xff) << 8) |
						(srcData[sp + 2] & 0xff)) << 8) |
						(srcData[sp + 1] & 0xff)) << 8) |
						(srcData[sp] & 0xff);
					sp += (sfx >>> 16) * 4;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
				} break;
			}

			/*** WRITE NEXT PIXEL ***/
			final int data =
				(r >>> destRedPreShift << destRedShift) |
				(g >>> destGreenPreShift << destGreenShift) |
				(b >>> destBluePreShift << destBlueShift);
			switch (dtype) {
				case TYPE_GENERIC_8: {
					destData[dp] = (byte) data;
				} break;
				case TYPE_GENERIC_16_MSB: {
					destData[dp] = (byte) (data >>> 8);
					destData[dp + 1] = (byte) (data & 0xff);
				} break;
				case TYPE_GENERIC_16_LSB: {
					destData[dp] = (byte) (data & 0xff);
					destData[dp + 1] = (byte) (data >>> 8);
				} break;
				case TYPE_GENERIC_24: {
					destData[dp] = (byte) (data >>> 16);
					destData[dp + 1] = (byte) (data >>> 8);
					destData[dp + 2] = (byte) (data & 0xff);
				} break;
				case TYPE_GENERIC_32_MSB: {
					destData[dp] = (byte) (data >>> 24);
					destData[dp + 1] = (byte) (data >>> 16);
					destData[dp + 2] = (byte) (data >>> 8);
					destData[dp + 3] = (byte) (data & 0xff);
				} break;
				case TYPE_GENERIC_32_LSB: {
					destData[dp] = (byte) (data & 0xff);
					destData[dp + 1] = (byte) (data >>> 8);
					destData[dp + 2] = (byte) (data >>> 16);
					destData[dp + 3] = (byte) (data >>> 24);
				} break;
			}
		}
	}
}

/**
 * Blits an index palette image into an index palette image.
 * <p>
 * Note: The source and destination red, green, and blue
 * arrays may be null if no alpha blending or dither is to be
 * performed.
 * </p>
 *
 * @param srcData the source byte array containing image data
 * @param srcDepth the source depth: one of 1, 2, 4, 8
 * @param srcStride the source number of bytes per line
 * @param srcOrder the source byte ordering: one of MSB_FIRST or LSB_FIRST;
 *        ignored if srcDepth is not 1
 * @param srcWidth the width of the source blit region
 * @param srcHeight the height of the source blit region
 * @param destData the destination byte array containing image data
 * @param destDepth the destination depth: one of 1, 2, 4, 8
 * @param destStride the destination number of bytes per line
 * @param destOrder the destination byte ordering: one of MSB_FIRST or LSB_FIRST;
 *        ignored if destDepth is not 1
 * @param destWidth the width of the destination blit region
 * @param destHeight the height of the destination blit region
 * @param flipX if true the resulting image is flipped along the vertical axis
 * @param flipY if true the resulting image is flipped along the horizontal axis
 */
static void blit(
	byte[] srcData, int srcDepth, int srcStride, int srcOrder,
	int srcWidth, int srcHeight,
	byte[] destData, int destDepth, int destStride, int destOrder,
	int destWidth, int destHeight,
	boolean flipX, boolean flipY) {
	if ((destWidth <= 0) || (destHeight <= 0)) return;

	if (srcDepth > destDepth) {
		// This case doesn't really make sense - what to do when source palette index
		// doesn't fit into new bits-per-pixel? Therefore, not supported.
		return;
	}

	/*** Prepare scaling data ***/
	final int dwm1 = destWidth - 1;
	final int sfxi = (dwm1 != 0) ? (int)((((long)srcWidth << 16) - 1) / dwm1) : 0;
	final int dhm1 = destHeight - 1;
	final int sfyi = (dhm1 != 0) ? (int)((((long)srcHeight << 16) - 1) / dhm1) : 0;

	/*** Prepare source-related data ***/
	final int stype;
	switch (srcDepth) {
		case 16:
			stype = TYPE_INDEX_16_LSB;
			if ((srcStride % 2) != 0) {
				// Such strides don't really make sense and are not expected to occur.
				// Not supported at the moment.
				return;
			}
			break;
		case 8:
			stype = TYPE_INDEX_8;
			break;
		case 4:
			stype = TYPE_INDEX_4;
			break;
		case 2:
			stype = TYPE_INDEX_2;
			break;
		case 1:
			stype = (srcOrder == MSB_FIRST) ? TYPE_INDEX_1_MSB : TYPE_INDEX_1_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid source type");
			return;
	}
	int spr = 0;

	/*** Prepare destination-related data ***/
	final int dtype;
	switch (destDepth) {
		case 16:
			dtype = TYPE_INDEX_16_LSB;
			if ((destStride % 2) != 0) {
				// Such strides don't really make sense and are not expected to occur.
				// Not supported at the moment.
				return;
			}
			break;
		case 8:
			dtype = TYPE_INDEX_8;
			break;
		case 4:
			dtype = TYPE_INDEX_4;
			break;
		case 2:
			dtype = TYPE_INDEX_2;
			break;
		case 1:
			dtype = (destOrder == MSB_FIRST) ? TYPE_INDEX_1_MSB : TYPE_INDEX_1_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid source type");
			return;
	}

	final int srcPixelsPerStride = srcStride * 8 / srcDepth;
	final int dstPixelsPerStride = destStride * 8 / destDepth;

	int dpr = ((flipY) ? dhm1 : 0) * dstPixelsPerStride + ((flipX) ? dwm1 : 0);
	final int dprxi = (flipX) ? -1 : 1;
	final int dpryi = (flipY) ? -dstPixelsPerStride : dstPixelsPerStride;

	/*** Blit ***/
	int dp = dpr;
	int sp = spr;

	if (stype == dtype) {
		/*** Fast blit (copy w/ mapping) ***/
		switch (stype) {
			case TYPE_INDEX_16_LSB:
				for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcPixelsPerStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
					for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
						destData[2*dp] = srcData[2*sp];
						destData[2*dp+1] = srcData[2*sp+1];
						sp += (sfx >>> 16);
					}
				}
				break;
			case TYPE_INDEX_8:
				for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcPixelsPerStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
					for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
						destData[dp] = srcData[sp];
						sp += (sfx >>> 16);
					}
				}
				break;
			case TYPE_INDEX_4:
				for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcPixelsPerStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
					for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
						final int v;
						if ((sp & 1) != 0) v = srcData[sp >> 1] & 0x0f;
						else v = (srcData[sp >> 1] >>> 4) & 0x0f;
						sp += (sfx >>> 16);
						if ((dp & 1) != 0) destData[dp >> 1] = (byte)((destData[dp >> 1] & 0xf0) | v);
						else destData[dp >> 1] = (byte)((destData[dp >> 1] & 0x0f) | (v << 4));
					}
				}
				break;
			case TYPE_INDEX_2:
				for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcPixelsPerStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
					for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
						final int index = (srcData[sp >> 2] >>> (6 - (sp & 3) * 2)) & 0x03;
						sp += (sfx >>> 16);
						final int shift = 6 - (dp & 3) * 2;
						destData[dp >> 2] = (byte)(destData[dp >> 2] & ~(0x03 << shift) | (index << shift));
					}
				}
				break;
			case TYPE_INDEX_1_MSB:
				for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcPixelsPerStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
					for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
						final int index = (srcData[sp >> 3] >>> (7 - (sp & 7))) & 0x01;
						sp += (sfx >>> 16);
						final int shift = 7 - (dp & 7);
						destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (index << shift));
					}
				}
				break;
			case TYPE_INDEX_1_LSB:
				for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcPixelsPerStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
					for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
						final int index = (srcData[sp >> 3] >>> (sp & 7)) & 0x01;
						sp += (sfx >>> 16);
						final int shift = dp & 7;
						destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (index << shift));
					}
				}
				break;
		}
	} else {
		/*** Convert between indexed modes using mapping and mask ***/
		for (int dy = destHeight, sfy = sfyi; dy > 0; --dy,
				sp = spr += (sfy >>> 16) * srcPixelsPerStride,
				sfy = (sfy & 0xffff) + sfyi,
				dp = dpr += dpryi) {
			for (int dx = destWidth, sfx = sfxi; dx > 0; --dx,
					dp += dprxi,
					sp += (sfx >>> 16),
					sfx = (sfx & 0xffff) + sfxi) {
				int index;
				/*** READ NEXT PIXEL ***/
				switch (stype) {
					case TYPE_INDEX_16_LSB:
						index = (((srcData[2*sp+1] & 0xff) << 8) | (srcData[2*sp] & 0xff)) & 0xffff;
						break;
					case TYPE_INDEX_8:
						index = srcData[sp] & 0xff;
						break;
					case TYPE_INDEX_4:
						if ((sp & 1) != 0) index = srcData[sp >> 1] & 0x0f;
						else index = (srcData[sp >> 1] >>> 4) & 0x0f;
						break;
					case TYPE_INDEX_2:
						index = (srcData[sp >> 2] >>> (6 - (sp & 3) * 2)) & 0x03;
						break;
					case TYPE_INDEX_1_MSB:
						index = (srcData[sp >> 3] >>> (7 - (sp & 7))) & 0x01;
						break;
					case TYPE_INDEX_1_LSB:
						index = (srcData[sp >> 3] >>> (sp & 7)) & 0x01;
						break;
					default:
						return;
				}

				/*** WRITE NEXT PIXEL ***/
				switch (dtype) {
					case TYPE_INDEX_16_LSB:
						destData[2*dp]   = (byte) (index & 0xff);
						destData[2*dp+1] = (byte) (index >>> 8);
						break;
					case TYPE_INDEX_8:
						destData[dp] = (byte) index;
						break;
					case TYPE_INDEX_4:
						if ((dp & 1) != 0) destData[dp >> 1] = (byte)((destData[dp >> 1] & 0xf0) | index);
						else destData[dp >> 1] = (byte)((destData[dp >> 1] & 0x0f) | (index << 4));
						break;
					case TYPE_INDEX_2: {
						final int shift = 6 - (dp & 3) * 2;
						destData[dp >> 2] = (byte)(destData[dp >> 2] & ~(0x03 << shift) | (index << shift));
					} break;
					case TYPE_INDEX_1_MSB: {
						final int shift = 7 - (dp & 7);
						destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (index << shift));
					} break;
					case TYPE_INDEX_1_LSB: {
						final int shift = dp & 7;
						destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (index << shift));
					} break;
				}
			}
		}
	}
}

/**
 * Blits an index palette image into a direct palette image.
 * <p>
 * Note: The source and destination masks and palettes must
 * always be fully specified.
 * </p>
 *
 * @param srcData the source byte array containing image data
 * @param srcDepth the source depth: one of 1, 2, 4, 8
 * @param srcStride the source number of bytes per line
 * @param srcOrder the source byte ordering: one of MSB_FIRST or LSB_FIRST;
 *        ignored if srcDepth is not 1
 * @param srcWidth the width of the source blit region
 * @param srcHeight the height of the source blit region
 * @param srcReds the source palette red component intensities
 * @param srcGreens the source palette green component intensities
 * @param srcBlues the source palette blue component intensities
 * @param destData the destination byte array containing image data
 * @param destDepth the destination depth: one of 8, 16, 24, 32
 * @param destStride the destination number of bytes per line
 * @param destOrder the destination byte ordering: one of MSB_FIRST or LSB_FIRST;
 *        ignored if destDepth is not 16 or 32
 * @param destRedMask the destination red channel mask
 * @param destGreenMask the destination green channel mask
 * @param destBlueMask the destination blue channel mask
 */
static void blit(
	int srcWidth, int srcHeight,
	byte[] srcData, int srcDepth, int srcStride, int srcOrder,
	byte[] srcReds, byte[] srcGreens, byte[] srcBlues,
	byte[] destData, int destDepth, int destStride, int destOrder,
	int destRedMask, int destGreenMask, int destBlueMask) {

	/*** Fast blit (straight copy) ***/
	if (destDepth == 24 && srcDepth == 8 && destRedMask == 0xFF0000 && destGreenMask == 0xFF00 && destBlueMask == 0xFF) {
		for (int y = 0, sp = 0, dp = 0, spad = srcStride - srcWidth, dpad = destStride - (srcWidth * 3); y < srcHeight; y++, sp += spad, dp += dpad) {
			for (int x = 0; x < srcWidth; x++) {
				int index = srcData[sp++] & 0xff;
				destData[dp++] = srcReds[index];
				destData[dp++] = srcGreens[index];
				destData[dp++] = srcBlues[index];
			}
		}
		return;
	}
	if (destDepth == 32 && destOrder == MSB_FIRST && srcDepth == 8 && destRedMask == 0xFF0000 && destGreenMask == 0xFF00 && destBlueMask == 0xFF) {
		for (int y = 0, sp = 0, dp = 0, spad = srcStride - srcWidth, dpad = destStride - (srcWidth * 4); y < srcHeight; y++, sp += spad, dp += dpad) {
			for (int x = 0; x < srcWidth; x++) {
				int index = srcData[sp++] & 0xff;
				dp++;
				destData[dp++] = srcReds[index];
				destData[dp++] = srcGreens[index];
				destData[dp++] = srcBlues[index];
			}
		}
		return;
	}

	/*** Prepare source-related data ***/
	final int stype;
	switch (srcDepth) {
		case 16:
			stype = TYPE_INDEX_16_LSB;
			if ((srcStride % 2) != 0) {
				// Such strides don't really make sense and are not expected to occur.
				// Not supported at the moment.
				return;
			}
			break;
		case 8:
			stype = TYPE_INDEX_8;
			break;
		case 4:
			stype = TYPE_INDEX_4;
			break;
		case 2:
			stype = TYPE_INDEX_2;
			break;
		case 1:
			stype = (srcOrder == MSB_FIRST) ? TYPE_INDEX_1_MSB : TYPE_INDEX_1_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid source type");
			return;
	}

	/*** Prepare destination-related data ***/
	final int dbpp, dtype;
	switch (destDepth) {
		case 8:
			dbpp = 1;
			dtype = TYPE_GENERIC_8;
			break;
		case 16:
			dbpp = 2;
			dtype = (destOrder == MSB_FIRST) ? TYPE_GENERIC_16_MSB : TYPE_GENERIC_16_LSB;
			break;
		case 24:
			dbpp = 3;
			dtype = TYPE_GENERIC_24;
			break;
		case 32:
			dbpp = 4;
			dtype = (destOrder == MSB_FIRST) ? TYPE_GENERIC_32_MSB : TYPE_GENERIC_32_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid destination type");
			return;
	}

	/*** Comprehensive blit (apply transformations) ***/
	final int destRedShift = getChannelShift(destRedMask);
	final int destRedWidth = getChannelWidth(destRedMask, destRedShift);
	final int destRedPreShift = 8 - destRedWidth;
	final int destGreenShift = getChannelShift(destGreenMask);
	final int destGreenWidth = getChannelWidth(destGreenMask, destGreenShift);
	final int destGreenPreShift = 8 - destGreenWidth;
	final int destBlueShift = getChannelShift(destBlueMask);
	final int destBlueWidth = getChannelWidth(destBlueMask, destBlueShift);
	final int destBluePreShift = 8 - destBlueWidth;

	final int srcPixelsPerStride = srcStride * 8 / srcDepth;
	int spr = 0;
	int dpr = 0;
	int dp = 0;
	int sp = 0;
	int r = 0, g = 0, b = 0, index = 0;
	for (int dy = srcHeight; dy > 0; --dy, sp = spr += srcPixelsPerStride, dp = dpr += destStride) {
		for (int dx = srcWidth; dx > 0; --dx, sp++, dp += dbpp) {
			/*** READ NEXT PIXEL ***/
			switch (stype) {
				case TYPE_INDEX_16_LSB:
					index = (((srcData[2*sp+1] & 0xff) << 8) | (srcData[2*sp] & 0xff)) & 0xffff;
					break;
				case TYPE_INDEX_8:
					index = srcData[sp] & 0xff;
					break;
				case TYPE_INDEX_4:
					if ((sp & 1) != 0) index = srcData[sp >> 1] & 0x0f;
					else index = (srcData[sp >> 1] >>> 4) & 0x0f;
					break;
				case TYPE_INDEX_2:
					index = (srcData[sp >> 2] >>> (6 - (sp & 3) * 2)) & 0x03;
					break;
				case TYPE_INDEX_1_MSB:
					index = (srcData[sp >> 3] >>> (7 - (sp & 7))) & 0x01;
					break;
				case TYPE_INDEX_1_LSB:
					index = (srcData[sp >> 3] >>> (sp & 7)) & 0x01;
					break;
			}

			/*** DO SPECIAL PROCESSING IF REQUIRED ***/
			r = srcReds[index] & 0xff;
			g = srcGreens[index] & 0xff;
			b = srcBlues[index] & 0xff;

			/*** WRITE NEXT PIXEL ***/
			final int data =
				(r >>> destRedPreShift << destRedShift) |
				(g >>> destGreenPreShift << destGreenShift) |
				(b >>> destBluePreShift << destBlueShift);
			switch (dtype) {
				case TYPE_GENERIC_8: {
					destData[dp] = (byte) data;
				} break;
				case TYPE_GENERIC_16_MSB: {
					destData[dp] = (byte) (data >>> 8);
					destData[dp + 1] = (byte) (data & 0xff);
				} break;
				case TYPE_GENERIC_16_LSB: {
					destData[dp] = (byte) (data & 0xff);
					destData[dp + 1] = (byte) (data >>> 8);
				} break;
				case TYPE_GENERIC_24: {
					destData[dp] = (byte) (data >>> 16);
					destData[dp + 1] = (byte) (data >>> 8);
					destData[dp + 2] = (byte) (data & 0xff);
				} break;
				case TYPE_GENERIC_32_MSB: {
					destData[dp] = (byte) (data >>> 24);
					destData[dp + 1] = (byte) (data >>> 16);
					destData[dp + 2] = (byte) (data >>> 8);
					destData[dp + 3] = (byte) (data & 0xff);
				} break;
				case TYPE_GENERIC_32_LSB: {
					destData[dp] = (byte) (data & 0xff);
					destData[dp + 1] = (byte) (data >>> 8);
					destData[dp + 2] = (byte) (data >>> 16);
					destData[dp + 3] = (byte) (data >>> 24);
				} break;
			}
		}
	}
}

/**
 * Computes the required channel shift from a mask.
 */
static int getChannelShift(int mask) {
	if (mask == 0) return 0;
	int i;
	for (i = 0; ((mask & 1) == 0) && (i < 32); ++i) {
		mask >>>= 1;
	}
	return i;
}

/**
 * Computes the required channel width (depth) from a mask.
 */
static int getChannelWidth(int mask, int shift) {
	if (mask == 0) return 0;
	int i;
	mask >>>= shift;
	for (i = shift; ((mask & 1) != 0) && (i < 32); ++i) {
		mask >>>= 1;
	}
	return i - shift;
}

/**
 * Creates an ImageData containing one band's worth of a gradient filled
 * block.  If <code>vertical</code> is true, the band must be tiled
 * horizontally to fill a region, otherwise it must be tiled vertically.
 *
 * @param width the width of the region to be filled
 * @param height the height of the region to be filled
 * @param vertical if true sweeps from top to bottom, else
 *        sweeps from left to right
 * @param fromRGB the color to start with
 * @param toRGB the color to end with
 * @param redBits the number of significant red bits, 0 for palette modes
 * @param greenBits the number of significant green bits, 0 for palette modes
 * @param blueBits the number of significant blue bits, 0 for palette modes
 * @return the new ImageData
 */
static ImageData createGradientBand(
	int width, int height, boolean vertical,
	RGB fromRGB, RGB toRGB,
	int redBits, int greenBits, int blueBits) {
	/* Gradients are drawn as tiled bands */
	final int bandWidth, bandHeight, bitmapDepth;
	final byte[] bitmapData;
	final PaletteData paletteData;
	/* Select an algorithm depending on the depth of the screen */
	if (redBits != 0 && greenBits != 0 && blueBits != 0) {
		paletteData = new PaletteData(0x0000ff00, 0x00ff0000, 0xff000000);
		bitmapDepth = 32;
		if (redBits >= 8 && greenBits >= 8 && blueBits >= 8) {
			/* Precise color */
			final int steps;
			if (vertical) {
				bandWidth = 1;
				bandHeight = height;
				steps = bandHeight > 1 ? bandHeight - 1 : 1;
			} else {
				bandWidth = width;
				bandHeight = 1;
				steps = bandWidth > 1 ? bandWidth - 1 : 1;
			}
			final int bytesPerLine = bandWidth * 4;
			bitmapData = new byte[bandHeight * bytesPerLine];
			buildPreciseGradientChannel(fromRGB.blue, toRGB.blue, steps, bandWidth, bandHeight, vertical, bitmapData, 0, bytesPerLine);
			buildPreciseGradientChannel(fromRGB.green, toRGB.green, steps, bandWidth, bandHeight, vertical, bitmapData, 1, bytesPerLine);
			buildPreciseGradientChannel(fromRGB.red, toRGB.red, steps, bandWidth, bandHeight, vertical, bitmapData, 2, bytesPerLine);
		} else {
			/* Dithered color */
			final int steps;
			if (vertical) {
				bandWidth = (width < 8) ? width : 8;
				bandHeight = height;
				steps = bandHeight > 1 ? bandHeight - 1 : 1;
			} else {
				bandWidth = width;
				bandHeight = (height < 8) ? height : 8;
				steps = bandWidth > 1 ? bandWidth - 1 : 1;
			}
			final int bytesPerLine = bandWidth * 4;
			bitmapData = new byte[bandHeight * bytesPerLine];
			buildDitheredGradientChannel(fromRGB.blue, toRGB.blue, steps, bandWidth, bandHeight, vertical, bitmapData, 0, bytesPerLine, blueBits);
			buildDitheredGradientChannel(fromRGB.green, toRGB.green, steps, bandWidth, bandHeight, vertical, bitmapData, 1, bytesPerLine, greenBits);
			buildDitheredGradientChannel(fromRGB.red, toRGB.red, steps, bandWidth, bandHeight, vertical, bitmapData, 2, bytesPerLine, redBits);
		}
	} else {
		/* Dithered two tone */
		paletteData = new PaletteData(fromRGB, toRGB);
		bitmapDepth = 8;
		final int blendi;
		if (vertical) {
			bandWidth = (width < 8) ? width : 8;
			bandHeight = height;
			blendi = (bandHeight > 1) ? 0x1040000 / (bandHeight - 1) + 1 : 1;
		} else {
			bandWidth = width;
			bandHeight = (height < 8) ? height : 8;
			blendi = (bandWidth > 1) ? 0x1040000 / (bandWidth - 1) + 1 : 1;
		}
		final int bytesPerLine = (bandWidth + 3) & -4;
		bitmapData = new byte[bandHeight * bytesPerLine];
		if (vertical) {
			for (int dy = 0, blend = 0, dp = 0; dy < bandHeight;
				++dy, blend += blendi, dp += bytesPerLine) {
				for (int dx = 0; dx < bandWidth; ++dx) {
					bitmapData[dp + dx] = (blend + DITHER_MATRIX[dy & 7][dx]) <
						0x1000000 ? (byte)0 : (byte)1;
				}
			}
		} else {
			for (int dx = 0, blend = 0; dx < bandWidth; ++dx, blend += blendi) {
				for (int dy = 0, dptr = dx; dy < bandHeight; ++dy, dptr += bytesPerLine) {
					bitmapData[dptr] = (blend + DITHER_MATRIX[dy][dx & 7]) <
						0x1000000 ? (byte)0 : (byte)1;
				}
			}
		}
	}
	return new ImageData(bandWidth, bandHeight, bitmapDepth, paletteData, 4, bitmapData);
}

/*
 * Fill in gradated values for a color channel
 */
static void buildPreciseGradientChannel(int from, int to, int steps,
	int bandWidth, int bandHeight, boolean vertical,
	byte[] bitmapData, int dp, int bytesPerLine) {
	int val = from << 16;
	final int inc = ((to << 16) - val) / steps + 1;
	if (vertical) {
		for (int dy = 0; dy < bandHeight; ++dy, dp += bytesPerLine) {
			bitmapData[dp] = (byte)(val >>> 16);
			val += inc;
		}
	} else {
		for (int dx = 0; dx < bandWidth; ++dx, dp += 4) {
			bitmapData[dp] = (byte)(val >>> 16);
			val += inc;
		}
	}
}

/*
 * Fill in dithered gradated values for a color channel
 */
static void buildDitheredGradientChannel(int from, int to, int steps,
	int bandWidth, int bandHeight, boolean vertical,
	byte[] bitmapData, int dp, int bytesPerLine, int bits) {
	final int mask = 0xff00 >>> bits;
	int val = from << 16;
	final int inc = ((to << 16) - val) / steps + 1;
	if (vertical) {
		for (int dy = 0; dy < bandHeight; ++dy, dp += bytesPerLine) {
			for (int dx = 0, dptr = dp; dx < bandWidth; ++dx, dptr += 4) {
				final int thresh = DITHER_MATRIX[dy & 7][dx] >>> bits;
				int temp = val + thresh;
				if (temp > 0xffffff) bitmapData[dptr] = -1;
				else bitmapData[dptr] = (byte)((temp >>> 16) & mask);
			}
			val += inc;
		}
	} else {
		for (int dx = 0; dx < bandWidth; ++dx, dp += 4) {
			for (int dy = 0, dptr = dp; dy < bandHeight; ++dy, dptr += bytesPerLine) {
				final int thresh = DITHER_MATRIX[dy][dx & 7] >>> bits;
				int temp = val + thresh;
				if (temp > 0xffffff) bitmapData[dptr] = -1;
				else bitmapData[dptr] = (byte)((temp >>> 16) & mask);
			}
			val += inc;
		}
	}
}

/**
 * Renders a gradient onto a GC.
 * <p>
 * This is a GC helper.
 * </p>
 *
 * @param gc the GC to render the gradient onto
 * @param device the device the GC belongs to
 * @param x the top-left x coordinate of the region to be filled
 * @param y the top-left y coordinate of the region to be filled
 * @param width the width of the region to be filled
 * @param height the height of the region to be filled
 * @param vertical if true sweeps from top to bottom, else
 *        sweeps from left to right
 * @param fromRGB the color to start with
 * @param toRGB the color to end with
 * @param redBits the number of significant red bits, 0 for palette modes
 * @param greenBits the number of significant green bits, 0 for palette modes
 * @param blueBits the number of significant blue bits, 0 for palette modes
 */
static void fillGradientRectangle(GC gc, Device device,
	int x, int y, int width, int height, boolean vertical,
	RGB fromRGB, RGB toRGB,
	int redBits, int greenBits, int blueBits) {
	/* Create the bitmap and tile it */
	ImageData band = createGradientBand(width, height, vertical,
		fromRGB, toRGB, redBits, greenBits, blueBits);
	Image image = new Image(device, band);
	if ((band.width == 1) || (band.height == 1)) {
			gc.drawImage(image, 0, 0, DPIUtil.autoScaleDown(band.width), DPIUtil.autoScaleDown(band.height),
					DPIUtil.autoScaleDown(x), DPIUtil.autoScaleDown(y), DPIUtil.autoScaleDown(width),
					DPIUtil.autoScaleDown(height));
		} else {
		if (vertical) {
			for (int dx = 0; dx < width; dx += band.width) {
				int blitWidth = width - dx;
				if (blitWidth > band.width) blitWidth = band.width;
					gc.drawImage(image, 0, 0, DPIUtil.autoScaleDown(blitWidth), DPIUtil.autoScaleDown(band.height),
							DPIUtil.autoScaleDown(dx + x), DPIUtil.autoScaleDown(y), DPIUtil.autoScaleDown(blitWidth),
							DPIUtil.autoScaleDown(band.height));
				}
		} else {
			for (int dy = 0; dy < height; dy += band.height) {
				int blitHeight = height - dy;
				if (blitHeight > band.height) blitHeight = band.height;
					gc.drawImage(image, 0, 0, DPIUtil.autoScaleDown(band.width), DPIUtil.autoScaleDown(blitHeight),
							DPIUtil.autoScaleDown(x), DPIUtil.autoScaleDown(dy + y), DPIUtil.autoScaleDown(band.width),
							DPIUtil.autoScaleDown(blitHeight));
				}
		}
	}
	image.dispose();
}

}
