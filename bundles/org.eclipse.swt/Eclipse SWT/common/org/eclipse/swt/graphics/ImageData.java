package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.io.*;
import java.util.*;
import org.eclipse.swt.*;
import org.eclipse.swt.internal.image.*;

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
 *
 * @see Image
 * @see ImageLoader
 */
 
public final class ImageData implements Cloneable {
	
	/**
	 * the width of the image, in pixels
	 */
	public int width;

	/**
	 * the height of the image, in pixels
	 */
	public int height;

	/**
	 * the color depth of the image, in bits per pixel
	 */
	public int depth;

	/**
	 * the scanline padding. If one scanline of the image
	 * is not a multiple of this number, it will be padded
	 * with zeros until it is
	 */
	public int scanlinePad;

	/**
	 * the number of bytes per scanline. This is a multiple
	 * of the scanline padding
	 */
	public int bytesPerLine;

	/**
	 * the pixel data of the image
	 */
	public byte[] data;

	/**
	 * the color table for the image
	 */
	public PaletteData palette;

	/**
	 * the transparent pixel. Pixels with this value are transparent.
	 * The default is -1 which means 'no transparency'
	 */
	public int transparentPixel;

	/**
	 * icon-specific field containing the data from the icon mask
	 */
	public byte[] maskData;

	/**
	 * icon-specific field containing the scanline pad of the mask
	 */
	public int maskPad;
	
	/**
	 * the alpha data of the image.  Every pixel can have an
	 * <em>alpha blending</em> value that varies from 0, meaning
	 * fully transparent, to 255 meaning fully opaque
	 */
	public byte[] alphaData;
	
	/**
	 * the global alpha value to be used for every pixel.
	 * If this value is set the <code>alphaData</code> field
	 * is ignored. The default is -1 which means 'no global alpha
	 * value'
	 */
	public int alpha;

	/**
	 * the type of file that the image was read in from,
	 * expressed as one of the following values:
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
	 * the x coordinate of the top left corner of the image
	 * within the logical screen (this field corresponds to
	 * the GIF89a Image Left Position value)
	 */
	public int x;

	/**
	 * The y coordinate of the top left corner of the image
	 * within the logical screen (this field corresponds to
	 * the GIF89a Image Top Position value)
	 */
	public int y;

	/**
	 * a description of how to dispose of the current image
	 * before displaying the next, expressed as one of the
	 * following values:
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
	 * the time to delay before displaying the next image
	 * in an animation (this field corresponds to the GIF89a
	 * Delay Time value)
	 */
	public int delayTime;
	
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
 *    <li>ERROR_INVALID_ARGUMENT - if the width or height is negative</li>
 *    <li>ERROR_NULL_ARGUMENT - if the palette is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the depth argument is not one of 1, 2, 4, 8, 16, 24 or 32</li>
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
 *    <li>ERROR_INVALID_ARGUMENT - if the width or height is negative</li>
 *    <li>ERROR_NULL_ARGUMENT - if the palette or data is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the depth argument is not one of 1, 2, 4, 8, 16, 24 or 32</li>
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
 * the image, or if the image has an unsupported type.
 * <p>
 * This constructor is provided for convenience when loading a single
 * image only. If the stream contains multiple images, only the first
 * one will be loaded. To load multiple images, use 
 * <code>ImageLoader.load()</code>.
 * </p>
 *
 * @param stream the input stream to load the image from (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the stream is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_INVALID_IMAGE - if the image file contains invalid data</li>
 *    <li>ERROR_IO - if an IO error occurs while reading data</li>
 * </ul>
 *
 * @see ImageLoader#load
 */
public ImageData(InputStream stream) {
	ImageData[] data = new ImageLoader().load(stream);
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
 *    <li>ERROR_INVALID_IMAGE - if the image file contains invalid data</li>
 *    <li>ERROR_IO if an IO error occurs while reading data</li>
 * </ul>
 */
public ImageData(String filename) {
	ImageData[] data = new ImageLoader().load(filename);
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
	int bytesPerLine = (((width * depth + 7) / 8) + (scanlinePad - 1))
		/ scanlinePad * scanlinePad;
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
 * @private
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
		Image.DEFAULT_SCANLINE_PAD, null, 0, null,
		null, -1, -1, SWT.IMAGE_UNDEFINED,
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
 *
 * @param x the x coodinate of the pixel to get the alpha value of
 * @param y the y coordinate of the pixel to get the alpha value of
 * @return the alpha value at the given coordinates
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if either argument is out of range</li>
 * </ul>
 */
public int getAlpha(int x, int y) {
	if (x > width || y > height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

	if (alphaData == null) return 255;
	return alphaData[y * width + x] & 0xFF;
}

/**
 * Returns <code>getWidth</code> alpha values starting at offset
 * <code>x</code> in scanline <code>y</code> in the receiver's alpha
 * data starting at <code>startIndex</code>.
 *
 * @param x the x position of the pixel to begin getting alpha values
 * @param y the y position of the pixel to begin getting alpha values
 * @param getWidth the width of the data to get
 * @param alphas the buffer in which to put the alpha values
 * @param startIndex the offset into the image to begin getting alpha values
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if <code>x</code> or <code>y</code> is out of bounds</li>
 * </ul>
 */
public void getAlphas(int x, int y, int getWidth, byte[] alphas, int startIndex) {
	if (alphas == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (getWidth <= 0) return;
	if (x >= width || y >= height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

	if (alphaData == null) {
		int endIndex = startIndex + getWidth;
		for (int i = startIndex; i < endIndex; i++) {
			alphas[i] = (byte)255;
		}
		return;
	}
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
	if (x > width || y > height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int index;
	int theByte;
	int mask;
	if (depth == 1) {
		index = (y * bytesPerLine) + (x >> 3);
		theByte = data[index] & 0xFF;
		mask = 1 << (7 - (x & 0x7));
		if ((theByte & mask) == 0) {
			return 0;
		} else {
			return 1;
		}
	}
	if (depth == 2) {
		index = (y * bytesPerLine) + (x >> 2);
		theByte = data[index] & 0xFF;
		int offset = 3 - (x % 4);
		mask = 3 << (offset * 2);
		return (theByte & mask) >> (offset * 2);
	}
	if (depth == 4) {
		index = (y * bytesPerLine) + (x >> 1);
		theByte = data[index] & 0xFF;
		if ((x & 0x1) == 0) {
			return theByte >> 4;
		} else {
			return theByte & 0x0F;
		}
	}
	if (depth == 8) {
		index = (y * bytesPerLine) + x ;
		return data[index] & 0xFF;
	}
	if (depth == 16) {
		index = (y * bytesPerLine) + (x * 2);
		return ((data[index+1] & 0xFF) << 8) + (data[index] & 0xFF);
	}
	if (depth == 24) {
		index = (y * bytesPerLine) + (x * 3);
		return ((data[index] & 0xFF) << 16) + ((data[index+1] & 0xFF) << 8) +
			(data[index+2] & 0xFF);
	}
	if (depth == 32) {
		index = (y * bytesPerLine) + (x * 4);
		return ((data[index] & 0xFF) << 24) + ((data[index+1] & 0xFF) << 16) +
				((data[index+2] & 0xFF) << 8) + (data[index+3] & 0xFF);
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
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the depth is not one of 1, 2, 4 or 8
 *        (For higher depths, use the int[] version of this method.)</li>
 * </ul>
 */
public void getPixels(int x, int y, int getWidth, byte[] pixels, int startIndex) {
	if (pixels == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (getWidth <= 0) return;
	if (x >= width || y >= height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int index;
	int theByte;
	int mask = 0;
	int n = getWidth;
	int i = startIndex;
	int srcX = x, srcY = y;
	if (depth == 1) {
		index = (y * bytesPerLine) + (x >> 3);
		theByte = data[index] & 0xFF;
		while (n > 1) {
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
	if (depth == 2) {
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
	}
	if (depth == 4) {
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
	}
	if (depth == 8) {
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
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH - if the depth is not one of 1, 2, 4, 8, 16, 24 or 32</li>
 * </ul>
 */
public void getPixels(int x, int y, int getWidth, int[] pixels, int startIndex) {
	if (pixels == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (getWidth <= 0) return;
	if (x > width || y > height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int index;
	int theByte;
	int mask;
	int n = getWidth;
	int i = startIndex;
	int srcX = x, srcY = y;
	if (depth == 1) {
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
	if (depth == 2) {
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
	}
	if (depth == 4) {
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
				srcX++;;
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
	}
	if (depth == 8) {
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
	}
	if (depth == 16) {
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
	}
	if (depth == 24) {
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
	}
	if (depth == 32) {
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
 * @see PaletteData#getRGBs
 */
public RGB[] getRGBs() {
	return palette.getRGBs();
}

/**
 * Returns an <code>ImageData</code> which specifies the
 * transparency mask information for the receiver, or null if the
 * receiver has no transparency and is not an icon.
 *
 * @return the transparency mask or null if none exists
 */
public ImageData getTransparencyMask() {
	if (getTransparencyType() == SWT.TRANSPARENCY_MASK) {
		return new ImageData(width, height, 1, bwPalette(), maskPad, maskData);
	} else {
		return colorMaskImage(transparentPixel);
	}
}

/**
 * Returns the image transparency type.
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
	/* Create a destination image with no data,
	 * and then scale all of the data. */
	ImageData destImage = new ImageData(
		width, height, depth, palette,
		scanlinePad, null, 0, null,
		null, -1, transparentPixel, type,
		x, y, disposalMethod, delayTime);
	scaleImage(destImage, 0, 0, this.width, this.height, 0, 0, width, height);
	return destImage;
}

void scaleImage(ImageData destImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight) {
	boolean flipX, flipY;
	/* Flip rects if necessary so that src width and height are positive. */
	if (srcWidth < 0) {
		srcWidth = -srcWidth;
		srcX = srcX - srcWidth;
		destWidth = -destWidth;
		destX = destX - destWidth;
	}
	if (srcHeight < 0) {
		srcHeight = -srcHeight;
		srcY = srcY - srcHeight;
		destHeight = -destHeight;
		destY = destY - destHeight;
	}
	/* Ensure dest width and height are positive, remembering whether to flip. */
	flipX = destWidth < 0;
	if (flipX) {
		destWidth = -destWidth;
		destX = destX - destWidth;
	}
	flipY = destHeight < 0;
	if (flipY) {
		destHeight = -destHeight;
		destY = destY - destHeight;
	}
	/* Check source rect bounds. Succeed with 0 if out of bounds, rather
	 * than failing.
	 */
	if (srcX < 0 || srcY < 0 || (srcX + srcWidth > width) ||
		(srcY + srcHeight > height)) {
		return;
	}
	if (destX < 0 || destY < 0 || (destX + destWidth > destImage.width) ||
		(destY + destHeight > destImage.height)) {
		return;
	}
	/* If dest rect is 0, there is nothing to do. */
	if (destWidth == 0 || destHeight == 0) {
		return;
	}
	switch (depth) {
		case 1:
			stretch1(data, bytesPerLine, MSB_FIRST, srcX, srcY, srcWidth, srcHeight, destImage.data, destImage.bytesPerLine, MSB_FIRST, destX, destY, destWidth, destHeight, flipX, flipY);
			break;
		case 2:
			stretch2(data, bytesPerLine, srcX, srcY, srcWidth, srcHeight, destImage.data, destImage.bytesPerLine, destX, destY, destWidth, destHeight, null, flipX, flipY);
			break;
		case 4:
			stretch4(data, bytesPerLine, srcX, srcY, srcWidth, srcHeight, destImage.data, destImage.bytesPerLine, destX, destY, destWidth, destHeight, null, flipX, flipY);
			break;
		case 8:
			stretch8(data, bytesPerLine, srcX, srcY, srcWidth, srcHeight, destImage.data, destImage.bytesPerLine, destX, destY, destWidth, destHeight, null, flipX, flipY);
			break;
		case 16:
			stretch16(data, bytesPerLine, srcX, srcY, srcWidth, srcHeight, destImage.data, destImage.bytesPerLine, destX, destY, destWidth, destHeight, flipX, flipY);
			break;
		case 24:		
			stretch24(data, bytesPerLine, srcX, srcY, srcWidth, srcHeight, destImage.data, destImage.bytesPerLine, destX, destY, destWidth, destHeight, flipX, flipY);
			break;
		case 32:
			stretch32(data, bytesPerLine, srcX, srcY, srcWidth, srcHeight, destImage.data, destImage.bytesPerLine, destX, destY, destWidth, destHeight, flipX, flipY);
			break;
		default:
			SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
	}
	if (getTransparencyType() == SWT.TRANSPARENCY_MASK) {
		destImage.maskPad = maskPad;
		int destBpl = (destWidth + 7) / 8;
		destBpl = (destBpl + (maskPad - 1)) / maskPad * maskPad;
		destImage.maskData = new byte[destBpl * destHeight];
		int srcBpl = (srcWidth + 7) / 8;
		srcBpl = (srcBpl + (maskPad - 1)) / maskPad * maskPad;
		stretch1(maskData, srcBpl, MSB_FIRST, srcX, srcY, srcWidth, srcHeight, destImage.maskData, destBpl, MSB_FIRST, destX, destY, destWidth, destHeight, flipX, flipY);
	} else if (alpha != -1) {
		destImage.alpha = alpha;
	} else if (alphaData != null) {
		destImage.alphaData = new byte[destImage.width * destImage.height];
		stretch8(alphaData, width, srcX, srcY, srcWidth, srcHeight, destImage.alphaData, destImage.width, destX, destY, destWidth, destHeight, null, flipX, flipY);
	}
}

/**
 * Sets the alpha value at offset <code>x</code> in
 * scanline <code>y</code> in the receiver's alpha data.
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
	if (x > width || y > height || x < 0 || y < 0 || alpha < 0 || alpha > 255)
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	
	if (alphaData == null) alphaData = new byte[width * height];
	alphaData[y * width + x] = (byte)alpha;	
}

/**
 * Sets the alpha values starting at offset <code>x</code> in
 * scanline <code>y</code> in the receiver's alpha data to the
 * values from the array <code>alphas</code> starting at
 * <code>startIndex</code>.
 *
 * @param x the x coordinate of the pixel to being setting the alpha values
 * @param y the y coordinate of the pixel to being setting the alpha values
 * @param putWidth the width of the alpha values to set
 * @param alphas the alpha values to set
 * @param startIndex the index at which to begin setting
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 * </ul>
 */
public void setAlphas(int x, int y, int putWidth, byte[] alphas, int startIndex) {
	if (alphas == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (putWidth <= 0) return;
	if (x > width || y > height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	
	if (alphaData == null) alphaData = new byte[width * height];
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
	if (x > width || y > height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int index;
	byte theByte;
	int mask;
	if (depth == 1) {
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
	if (depth == 2) {
		index = (y * bytesPerLine) + (x >> 2);
		theByte = data[index];
		int offset = 3 - (x % 4);
		mask = 0xFF ^ (3 << (offset * 2));
		data[index] = (byte)((data[index] & mask) | (pixelValue << (offset * 2)));
		return;
	}
	if (depth == 4) {
		index = (y * bytesPerLine) + (x >> 1);
		if ((x & 0x1) == 0) {
			data[index] = (byte)((data[index] & 0x0F) | ((pixelValue & 0x0F) << 4));
		} else {
			data[index] = (byte)((data[index] & 0xF0) | (pixelValue & 0x0F));
		}
		return;
	}
	if (depth == 8) {
		index = (y * bytesPerLine) + x ;
		data[index] = (byte)(pixelValue & 0xFF);
		return;
	}
	if (depth == 16) {
		index = (y * bytesPerLine) + (x * 2);
		data[index + 1] = (byte)((pixelValue >> 8) & 0xFF);
		data[index] = (byte)(pixelValue & 0xFF);
		return;
	}
	if (depth == 24) {
		index = (y * bytesPerLine) + (x * 3);
		data[index] = (byte)((pixelValue >> 16) & 0xFF);
		data[index + 1] = (byte)((pixelValue >> 8) & 0xFF);
		data[index + 2] = (byte)(pixelValue & 0xFF);
		return;
	}
	if (depth == 32) {
		index = (y * bytesPerLine) + (x * 4);
		data[index]  = (byte)((pixelValue >> 24) & 0xFF);
		data[index + 1] = (byte)((pixelValue >> 16) & 0xFF);
		data[index + 2] = (byte)((pixelValue >> 8) & 0xFF);
		data[index + 3] = (byte)(pixelValue & 0xFF);
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
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH if the depth is not one of 1, 2, 4, 8
 *        (For higher depths, use the int[] version of this method.)</li>
 * </ul>
 */
public void setPixels(int x, int y, int putWidth, byte[] pixels, int startIndex) {
	if (pixels == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (putWidth <= 0) return;
	if (x > width || y > height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int index;
	int theByte;
	int mask;
	int n = putWidth;
	int i = startIndex;
	int pixel;
	int srcX = x, srcY = y;
	if (depth == 1) {
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
	if (depth == 2) {
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
	}
	if (depth == 4) {
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
	}
	if (depth == 8) {
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
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if pixels is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if x or y is out of bounds</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_UNSUPPORTED_DEPTH if the depth is not one of 1, 2, 4, 8, 16, 24 or 32</li>
 * </ul>
 */
public void setPixels(int x, int y, int putWidth, int[] pixels, int startIndex) {
	if (pixels == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (putWidth <= 0) return;
	if (x > width || y > height || x < 0 || y < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int index;
	int theByte;
	int mask;
	int n = putWidth;
	int i = startIndex;
	int pixel;
	int srcX = x, srcY = y;
	if (depth == 1) {
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
	if (depth == 2) {
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
	}
	if (depth == 4) {
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
	}
	if (depth == 8) {
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

	}
	if (depth == 16) {
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
	}
	if (depth == 24) {
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
	}
	if (depth == 32) {
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
	}
	SWT.error(SWT.ERROR_UNSUPPORTED_DEPTH);
}

/**
 * Returns a palette with 2 colors: black & white.
 */
static PaletteData bwPalette() {
	return new PaletteData(new RGB[] {new RGB(0, 0, 0), new RGB(255, 255, 255)});
}

/**
 * Blit operations.
 */
static final int BLIT_SRC = 1;
static final int BLIT_ALPHA = 2;
/**
 * Byte and bit order constants.
 */
static final int MSB_FIRST = 1;
static final int LSB_FIRST = 2;

/**
 * Masks for blitting.
 */
static final byte[] msbMasks1 = {
	(byte)0x80, (byte)0x40, (byte)0x20, (byte)0x10, 
	(byte)0x08, (byte)0x04, (byte)0x02, (byte)0x01
};
static final byte[] lsbMasks1 = {
	(byte)0x01, (byte)0x02, (byte)0x04, (byte)0x08,
	(byte)0x10, (byte)0x20, (byte)0x40, (byte)0x80
};
static final byte[] msbInverseMasks1 = {
	(byte)0x7F, (byte)0xBF, (byte)0xDF, (byte)0xEF,
	(byte)0xF7, (byte)0xFB, (byte)0xFD, (byte)0xFE
};
static final byte[] lsbInverseMasks1 = {
	(byte)0xFE, (byte)0xFD, (byte)0xFB, (byte)0xF7,
	(byte)0xEF, (byte)0xDF, (byte)0xBF, (byte)0x7F
};
static final byte[] masks2 = {
	(byte)0x03, (byte)0x0C, (byte)0x30, (byte)0xC0
};
static final byte[] inverseMasks2 = {
	(byte)0xFC, (byte)0xF3, (byte)0xCF, (byte)0x3F
};

/**
 * Gets the offset of the most significant bit for
 * the given mask.
 */
static int getMSBOffset(int mask) {
	for (int i = 31; i >= 0; i--) {
		if (((mask >> i) & 0x1) != 0) return i + 1;
	}
	return 0;
}

/**
 * Finds the closest match.
 */
static int closestMatch(int depth, byte red, byte green, byte blue, int redMask, int greenMask, int blueMask, byte[] reds, byte[] greens, byte[] blues) {
	if (depth > 8) {
		int rshift = 32 - getMSBOffset(redMask);
		int gshift = 32 - getMSBOffset(greenMask);
		int bshift = 32 - getMSBOffset(blueMask);
		return (((red << 24) >>> rshift) & redMask) |
			(((green << 24) >>> gshift) & greenMask) |
			(((blue << 24) >>> bshift) & blueMask);
	}
	int r, g, b;
	int minDistance = 0x7fffffff;
	int nearestPixel = 0;
	int n = reds.length;
	for (int j = 0; j < n; j++) {
		r = (reds[j] & 0xFF) - (red & 0xFF);
		g = (greens[j] & 0xFF) - (green & 0xFF);
		b = (blues[j] & 0xFF) - (blue & 0xFF);
		int distance = r*r + g*g + b*b;
		if (distance < minDistance) {
			nearestPixel = j;
			if (distance == 0) break;
			minDistance = distance;
		}
	}
	return nearestPixel;
}

/**
 * Blits a direct palette image into a direct palette image.
 */
static void blit(int op, byte[] srcData, int srcDepth, int srcStride, int srcOrder, int srcX, int srcY, int srcWidth, int srcHeight, int srcRedMask, int srcGreenMask, int srcBlueMask, int srcGlobalAlpha, byte[] srcAlphaData, int srcAlphaStride, byte[] destData, int destDepth, int destStride, int destOrder, int destX, int destY, int destWidth, int destHeight, int destRedMask, int destGreenMask, int destBlueMask, boolean flipX, boolean flipY) {
	int xs1, ys1, xs2, ys2, xd1, yd1, xd2, yd2;
	int dyd, dys, ey, dy, dyd2, dys2, ys, yd;
	short syd, sys;
	int dxd, dxs, ex, dx, dxd2, dxs2;
	short sxd, sxs, sas;
	int sp, dp, sap = 0;
	int sr = 0, sg = 0, sb = 0, sa = 0, dr = 0, dg = 0, db = 0, da = 0;
	int srcPixel = 0, destPixel = 0;
	short so0 = 0, so1 = 1, so2 = 2, so3 = 3;
	short do0 = 0, do1 = 1, do2 = 2, do3 = 3;
	int srcRedShift, srcGreenShift, srcBlueShift;
	int destRedShift, destGreenShift, destBlueShift;
	
	if (op == BLIT_SRC && srcDepth == destDepth &&
		srcRedMask == destRedMask &&
		srcGreenMask == destGreenMask &&
		srcBlueMask == destBlueMask)
	{
		switch (srcDepth) {
			case 16:
				stretch16(srcData, srcStride, srcX, srcY, srcWidth, srcHeight, destData, destStride, destX, destY, destWidth, destHeight, flipX, flipY);
				break;
			case 24:
				stretch24(srcData, srcStride, srcX, srcY, srcWidth, srcHeight, destData, destStride, destX, destY, destWidth, destHeight, flipX, flipY);
				break;
			case 32:
				stretch32(srcData, srcStride, srcX, srcY, srcWidth, srcHeight, destData, destStride, destX, destY, destWidth, destHeight, flipX, flipY);
				break;
		}
		return;
	}	
	
	srcRedShift = 32 - getMSBOffset(srcRedMask);
	srcGreenShift = 32 - getMSBOffset(srcGreenMask);
	srcBlueShift = 32 - getMSBOffset(srcBlueMask);
	destRedShift = 32 - getMSBOffset(destRedMask);
	destGreenShift = 32 - getMSBOffset(destGreenMask);
	destBlueShift = 32 - getMSBOffset(destBlueMask);
	if (srcOrder == LSB_FIRST) {
		switch (srcDepth) {
			case 16: so0 = 1; so1 = 0; break;
			case 24: so0 = 2; so1 = 1; so2 = 0; break;
			case 32: so0 = 3; so1 = 2; so2 = 1; so3 = 0; break;
		}
	}
	if (destOrder == LSB_FIRST) {
		switch (destDepth) {
			case 16: do0 = 1; do1 = 0; break;
			case 24: do0 = 2; do1 = 1; do2 = 0; break;
			case 32: do0 = 3; do1 = 2; do2 = 1; do3 = 0; break;
		}
	}

	xs1 = srcX; xs2 = srcX + srcWidth - 1;
	ys1 = srcY; ys2 = srcY + srcHeight - 1;
	if (flipX) {
		xd1 = destX + destWidth - 1;
		xd2 = destX;
	} else {
		xd1 = destX;
		xd2 = destX + destWidth - 1;
	}
	if (flipY) {
		yd1 = destY + destHeight - 1;
		yd2 = destY;
	} else {
		yd1 = destY;
		yd2 = destY + destHeight - 1;
	}

	/* Y preliminary calculations */
	dyd = yd2 - yd1;
	if (dyd < 0) dyd = -dyd;
	dys = ys2 - ys1;
	if (dys < 0) dys = -dys;
	dyd2 = dyd << 1;
	dys2 = dys << 1;
	syd = (short)((yd2 - yd1) > 0 ? 1 : -1);
	sys = (short)((ys2 - ys1) > 0 ? 1 : -1);
	ey = dys2 - dyd;
	ys = ys1;
	yd = yd1;
	/* X preliminary calculations */
	dxd = xd2 - xd1;
	if (dxd < 0) dxd = -dxd;
	dxs = xs2 - xs1;
	if (dxs < 0) dxs = -dxs;
	dxs2 = dxs << 1;
	dxd2 = dxd << 1;
	sxs = sas = (short)((xs2 - xs1) > 0 ? 1 : -1);
	sxd = (short)((xd2 - xd1) > 0 ? 1 : -1);

	sxs *= srcDepth / 8;
	xs1 *= srcDepth / 8;
	sxd *= destDepth / 8;
	xd1 *= destDepth / 8;
	
	if (srcGlobalAlpha != -1) srcAlphaData = null;
	sa = srcGlobalAlpha;
	for (dy = 0; dy <= dyd; dy++) {
		/* X stretch starts here */
		ex = dxs2 - dxd;
		sp = ys * srcStride + xs1;
		dp = yd * destStride + xd1;
		if (srcAlphaData != null) sap = ys * srcAlphaStride + xs1;
		for (dx = 0; dx < dxd; dx++) {
			if (srcAlphaData != null) sa = srcAlphaData[sap] & 0xFF;
			switch (srcDepth) {
				case 16:
					srcPixel = ((srcData[sp+so0] & 0xFF) << 8) | (srcData[sp+so1] & 0xFF);
					break;
				case 24:
					srcPixel = ((srcData[sp+so0] & 0xFF) << 16) | ((srcData[sp+so1] & 0xFF) << 8) | 
						(srcData[sp+so2] & 0xFF);
					break;
				case 32:
					srcPixel = ((srcData[sp+so0] & 0xFF) << 24) | ((srcData[sp+so1] & 0xFF) << 16) |
						((srcData[sp+so2] & 0xFF) << 8) | (srcData[sp+so3] & 0xFF);
					break;
			}
			dr = sr = ((srcPixel & srcRedMask) << srcRedShift) >>> 24;
			dg = sg = ((srcPixel & srcGreenMask) << srcGreenShift) >>> 24;
			db = sb = ((srcPixel & srcBlueMask) << srcBlueShift) >>> 24;
			if (op != BLIT_SRC) {
				switch (destDepth) {
					case 16:
						destPixel = ((destData[dp+do0] & 0xFF) << 8) | (destData[dp+do1] & 0xFF);
						break;
					case 24:
						destPixel = ((destData[dp+do0] & 0xFF) << 16) | ((destData[dp+do1] & 0xFF) << 8) | 
							(destData[dp+do2] & 0xFF);
						break;
					case 32:
						destPixel = ((destData[dp+do0] & 0xFF) << 24) | ((destData[dp+do1] & 0xFF) << 16) |
							((destData[dp+do2] & 0xFF) << 8) | (destData[dp+do3] & 0xFF);
						break;
				}
				dr = ((destPixel & destRedMask) << destRedShift) >>> 24;
				dg = ((destPixel & destGreenMask) << destGreenShift) >>> 24;
				db = ((destPixel & destBlueMask) << destBlueShift) >>> 24;
				switch (op) {
					case BLIT_ALPHA:
						dr += (sr - dr) * sa / 255;
						dg += (sg - dg) * sa / 255;
						db += (sb - db) * sa / 255;
					break;
				}
			}
			destPixel =
				(((dr << 24) >> destRedShift) & destRedMask) |
				(((dg << 24) >> destGreenShift) & destGreenMask) |
				(((db << 24) >> destBlueShift) & destBlueMask);
			switch (destDepth) {
				case 16:
					destData[dp + do0] = (byte)((destPixel >> 8) & 0xFF);
					destData[dp + do1] = (byte)(destPixel & 0xFF);
					break;
				case 24:
					destData[dp + do0] = (byte)((destPixel >> 16) & 0xFF);
					destData[dp + do1] = (byte)((destPixel >> 8) & 0xFF);
					destData[dp + do2] = (byte)(destPixel & 0xFF);
					break;
				case 32:
					destData[dp + do0]  = (byte)((destPixel >> 24) & 0xFF);
					destData[dp + do1] = (byte)((destPixel >> 16) & 0xFF);
					destData[dp + do2] = (byte)((destPixel >> 8) & 0xFF);
					destData[dp + do3] = (byte)(destPixel & 0xFF);
					break;
			}
			while (ex >= 0) {
				sp += sxs;
				ex -= dxd2;
				if (srcAlphaData != null) sap += sas;
			}
			dp += sxd;
			ex += dxs2;
		}
		
		if (srcAlphaData != null) sa = srcAlphaData[sap] & 0xFF;
		switch (srcDepth) {
			case 16:
				srcPixel = ((srcData[sp+so0] & 0xFF) << 8) | (srcData[sp+so1] & 0xFF);
				break;
			case 24:
				srcPixel = ((srcData[sp+so0] & 0xFF) << 16) | ((srcData[sp+so1] & 0xFF) << 8) | 
					(srcData[sp+so2] & 0xFF);
				break;
			case 32:
				srcPixel = ((srcData[sp+so0] & 0xFF) << 24) | ((srcData[sp+so1] & 0xFF) << 16) |
					((srcData[sp+so2] & 0xFF) << 8) | (srcData[sp+so3] & 0xFF);
				break;
		}
		dr = sr = ((srcPixel & srcRedMask) << srcRedShift) >>> 24;
		dg = sg = ((srcPixel & srcGreenMask) << srcGreenShift) >>> 24;
		db = sb = ((srcPixel & srcBlueMask) << srcBlueShift) >>> 24;
		if (op != BLIT_SRC) {
			switch (destDepth) {
				case 16:
					destPixel = ((destData[dp+do0] & 0xFF) << 8) | (destData[dp+do1] & 0xFF);
					break;
				case 24:
					destPixel = ((destData[dp+do0] & 0xFF) << 16) | ((destData[dp+do1] & 0xFF) << 8) | 
						(destData[dp+do2] & 0xFF);
					break;
				case 32:
					destPixel = ((destData[dp+do0] & 0xFF) << 24) | ((destData[dp+do1] & 0xFF) << 16) |
						((destData[dp+do2] & 0xFF) << 8) | (destData[dp+do3] & 0xFF);
					break;
			}
			dr = ((destPixel & destRedMask) << destRedShift) >>> 24;
			dg = ((destPixel & destGreenMask) << destGreenShift) >>> 24;
			db = ((destPixel & destBlueMask) << destBlueShift) >>> 24;
			switch (op) {
				case BLIT_ALPHA:
					dr += (sr - dr) * sa / 255;
					dg += (sg - dg) * sa / 255;
					db += (sb - db) * sa / 255;
				break;
			}
		}
		destPixel =
			(((dr << 24) >> destRedShift) & destRedMask) |
			(((dg << 24) >> destGreenShift) & destGreenMask) |
			(((db << 24) >> destBlueShift) & destBlueMask);
		switch (destDepth) {
			case 16:
				destData[dp + do0] = (byte)((destPixel >> 8) & 0xFF);
				destData[dp + do1] = (byte)(destPixel & 0xFF);
				break;
			case 24:
				destData[dp + do0] = (byte)((destPixel >> 16) & 0xFF);
				destData[dp + do1] = (byte)((destPixel >> 8) & 0xFF);
				destData[dp + do2] = (byte)(destPixel & 0xFF);
				break;
			case 32:
				destData[dp + do0]  = (byte)((destPixel >> 24) & 0xFF);
				destData[dp + do1] = (byte)((destPixel >> 16) & 0xFF);
				destData[dp + do2] = (byte)((destPixel >> 8) & 0xFF);
				destData[dp + do3] = (byte)(destPixel & 0xFF);
				break;
		}
		/* X stretch ends here */
		if (dy == dyd)
			break;
		while (ey >= 0) {
			ys += sys;
			ey -= dyd2;
		}
		yd += syd;
		ey += dys2;
	}
}

/**
 * Blits an index palette image into a direct palette image.
 */
static void blit(int op, byte[] srcData, int srcDepth, int srcStride, int srcOrder, int srcX, int srcY, int srcWidth, int srcHeight, byte[] srcReds, byte[] srcGreens, byte[] srcBlues, int srcGlobalAlpha, byte[] srcAlphaData, int srcAlphaStride, byte[] destData, int destDepth, int destStride, int destOrder, int destX, int destY, int destWidth, int destHeight, int destRedMask, int destGreenMask, int destBlueMask, boolean flipX, boolean flipY) {
	int xs1, ys1, xs2, ys2, xd1, yd1, xd2, yd2;
	int dyd, dys, ey, dy, dyd2, dys2, ys, yd;
	short syd, sys;
	int dxd, dxs, ex, dx, dxd2, dxs2, xs;
	short sxd, sxs, sas;
	int sp, dp, sap = 0;
	int sr = 0, sg = 0, sb = 0, sa = 0, dr = 0, dg = 0, db = 0, da = 0;
	int srcPixel = 0, destPixel = 0;
	short do0 = 0, do1 = 1, do2 = 2, do3 = 3;
	int destRedShift, destGreenShift, destBlueShift;
	int i, offset = 0;
	byte[] srcMasks = null;
	int srcN = 1 << srcDepth;
	int[] mapping = null;
	
	destRedShift = 32 - getMSBOffset(destRedMask);
	destGreenShift = 32 - getMSBOffset(destGreenMask);
	destBlueShift = 32 - getMSBOffset(destBlueMask);
	if (srcReds != null && srcN > srcReds.length) srcN = srcReds.length;
	if (op == BLIT_SRC) {
		mapping = new int[srcN];		
		for (i = 0; i < srcN; i++) {
			dr = srcReds[i] & 0xFF;
			dg = srcGreens[i] & 0xFF;
			db = srcBlues[i] & 0xFF;
			mapping[i] = 
				(((dr << 24) >>> destRedShift) & destRedMask) | 
				(((dg << 24) >>> destGreenShift) & destGreenMask) |
				(((db << 24) >>> destBlueShift) & destBlueMask);
		}
	}
	switch (srcDepth) {
		case 1: srcMasks = msbMasks1; break;
		case 2: srcMasks = masks2; break;
	}
	if (srcOrder == LSB_FIRST) {
		switch (srcDepth) {
			case 1: srcMasks = lsbMasks1; break;
		}
	}
	if (destOrder == LSB_FIRST) {
		switch (destDepth) {
			case 16: do0 = 1; do1 = 0; break;
			case 24: do0 = 2; do1 = 1; do2 = 0; break;
			case 32: do0 = 3; do1 = 2; do2 = 1; do3 = 0; break;
		}
	}

	xs1 = srcX; xs2 = srcX + srcWidth - 1;
	ys1 = srcY; ys2 = srcY + srcHeight - 1;
	if (flipX) {
		xd1 = destX + destWidth - 1;
		xd2 = destX;
	} else {
		xd1 = destX;
		xd2 = destX + destWidth - 1;
	}
	if (flipY) {
		yd1 = destY + destHeight - 1;
		yd2 = destY;
	} else {
		yd1 = destY;
		yd2 = destY + destHeight - 1;
	}

	/* Y preliminary calculations */
	dyd = yd2 - yd1;
	if (dyd < 0) dyd = -dyd;
	dys = ys2 - ys1;
	if (dys < 0) dys = -dys;
	dyd2 = dyd << 1;
	dys2 = dys << 1;
	syd = (short)((yd2 - yd1) > 0 ? 1 : -1);
	sys = (short)((ys2 - ys1) > 0 ? 1 : -1);
	ey = dys2 - dyd;
	ys = ys1;
	yd = yd1;
	/* X preliminary calculations */
	dxd = xd2 - xd1;
	if (dxd < 0) dxd = -dxd;
	dxs = xs2 - xs1;
	if (dxs < 0) dxs = -dxs;
	dxs2 = dxs << 1;
	dxd2 = dxd << 1;
	sxs = sas = (short)((xs2 - xs1) > 0 ? 1 : -1);
	sxd = (short)((xd2 - xd1) > 0 ? 1 : -1);

	sxd *= destDepth / 8;
	xd1 *= destDepth / 8;
	
	if (srcGlobalAlpha != -1) srcAlphaData = null;
	sa = srcGlobalAlpha;
	for (dy = 0; dy <= dyd; dy++) {
		/* X stretch starts here */
		offset = 3 - (srcX % 4);
		ex = dxs2 - dxd;
		xs = xs1;
		sp = ys * srcStride;
		dp = yd * destStride + xd1;
		if (srcAlphaData != null) sap = ys * srcAlphaStride + xs1;
		for (dx = 0; dx < dxd; dx++) {
			if (srcAlphaData != null) sa = srcAlphaData[sap] & 0xFF;
			switch (srcDepth) {
				case  1:
					srcPixel = (srcData[sp + (xs >> 3)] & srcMasks[xs & 7]) == 0 ? 0 : 1;
					break;
				case  2:
					srcPixel = ((srcData[sp + (xs >> 2)] & srcMasks[offset]) & 0xFF) >> (offset * 2);
					break;
				case  4:
					srcPixel = srcData[sp + (xs >> 1)] & 0xFF;
					if ((xs & 0x1) == 0) {
						srcPixel = srcPixel >> 4;
					} else {
						srcPixel = srcPixel & 0x0F;
					}
					break;
				case  8:
					srcPixel = srcData[sp + xs] & 0xFF;
					break;
			}
			if (mapping != null) {
				destPixel = mapping[srcPixel];
			} else {
				dr = sr = srcReds[srcPixel] & 0xFF;
				dg = sg = srcGreens[srcPixel] & 0xFF;
				db = sb = srcBlues[srcPixel] & 0xFF;
				if (op != BLIT_SRC) {
					switch (destDepth) {
						case 16:
							destPixel = ((destData[dp+do0] & 0xFF) << 8) | (destData[dp+do1] & 0xFF);
							break;
						case 24:
							destPixel = ((destData[dp+do0] & 0xFF) << 16) | ((destData[dp+do1] & 0xFF) << 8) | 
								(destData[dp+do2] & 0xFF);
							break;
						case 32:
							destPixel = ((destData[dp+do0] & 0xFF) << 24) | ((destData[dp+do1] & 0xFF) << 16) |
								((destData[dp+do2] & 0xFF) << 8) | (destData[dp+do3] & 0xFF);
							break;
					}
					dr = ((destPixel & destRedMask) << destRedShift) >>> 24;
					dg = ((destPixel & destGreenMask) << destGreenShift) >>> 24;
					db = ((destPixel & destBlueMask) << destBlueShift) >>> 24;
					switch (op) {
						case BLIT_ALPHA:
							dr += (sr - dr) * sa / 255;
							dg += (sg - dg) * sa / 255;
							db += (sb - db) * sa / 255;
						break;
					}
				}
				destPixel =
					(((dr << 24) >>> destRedShift) & destRedMask) |
					(((dg << 24) >>> destGreenShift) & destGreenMask) |
					(((db << 24) >>> destBlueShift) & destBlueMask);
			}
			switch (destDepth) {
				case 16:
					destData[dp + do0] = (byte)((destPixel >> 8) & 0xFF);
					destData[dp + do1] = (byte)(destPixel & 0xFF);
					break;
				case 24:
					destData[dp + do0] = (byte)((destPixel >> 16) & 0xFF);
					destData[dp + do1] = (byte)((destPixel >> 8) & 0xFF);
					destData[dp + do2] = (byte)(destPixel & 0xFF);
					break;
				case 32:
					destData[dp + do0]  = (byte)((destPixel >> 24) & 0xFF);
					destData[dp + do1] = (byte)((destPixel >> 16) & 0xFF);
					destData[dp + do2] = (byte)((destPixel >> 8) & 0xFF);
					destData[dp + do3] = (byte)(destPixel & 0xFF);
					break;
			}
			while (ex >= 0) {
				xs += sxs;
				ex -= dxd2;
				if (srcAlphaData != null) sap += sas;
			}
			dp += sxd;
			ex += dxs2;
			if (offset == 0) {
				offset = 3;
			} else {
				offset--;
			}
		}
		
		if (srcAlphaData != null) sa = srcAlphaData[sap] & 0xFF;
		switch (srcDepth) {
			case  1:
				srcPixel = (srcData[sp + (xs >> 3)] & srcMasks[xs & 7]) == 0 ? 0 : 1;
				break;
			case  2:
				srcPixel = ((srcData[sp + (xs >> 2)] & srcMasks[offset]) & 0xFF) >> (offset * 2);
				break;
			case  4:
				srcPixel = srcData[sp + (xs >> 1)] & 0xFF;
				if ((xs & 0x1) == 0) {
					srcPixel = srcPixel >> 4;
				} else {
					srcPixel = srcPixel & 0x0F;
				}
				break;
			case  8:
				srcPixel = srcData[sp + xs] & 0xFF;
				break;
		}
		if (mapping != null) {
			destPixel = mapping[srcPixel];
		} else {
			dr = sr = srcReds[srcPixel] & 0xFF;
			dg = sg = srcGreens[srcPixel] & 0xFF;
			db = sb = srcBlues[srcPixel] & 0xFF;
			if (op != BLIT_SRC) {
				switch (destDepth) {
					case 16:
						destPixel = ((destData[dp+do0] & 0xFF) << 8) | (destData[dp+do1] & 0xFF);
						break;
					case 24:
						destPixel = ((destData[dp+do0] & 0xFF) << 16) | ((destData[dp+do1] & 0xFF) << 8) | 
							(destData[dp+do2] & 0xFF);
						break;
					case 32:
						destPixel = ((destData[dp+do0] & 0xFF) << 24) | ((destData[dp+do1] & 0xFF) << 16) |
							((destData[dp+do2] & 0xFF) << 8) | (destData[dp+do3] & 0xFF);
						break;
				}
				dr = ((destPixel & destRedMask) << destRedShift) >>> 24;
				dg = ((destPixel & destGreenMask) << destGreenShift) >>> 24;
				db = ((destPixel & destBlueMask) << destBlueShift) >>> 24;
				switch (op) {
					case BLIT_ALPHA:
						dr += (sr - dr) * sa / 255;
						dg += (sg - dg) * sa / 255;
						db += (sb - db) * sa / 255;
					break;
				}
			}
			destPixel =
				(((dr << 24) >>> destRedShift) & destRedMask) |
				(((dg << 24) >>> destGreenShift) & destGreenMask) |
				(((db << 24) >>> destBlueShift) & destBlueMask);
		}
		switch (destDepth) {
			case 16:
				destData[dp + do0] = (byte)((destPixel >> 8) & 0xFF);
				destData[dp + do1] = (byte)(destPixel & 0xFF);
				break;
			case 24:
				destData[dp + do0] = (byte)((destPixel >> 16) & 0xFF);
				destData[dp + do1] = (byte)((destPixel >> 8) & 0xFF);
				destData[dp + do2] = (byte)(destPixel & 0xFF);
				break;
			case 32:
				destData[dp + do0]  = (byte)((destPixel >> 24) & 0xFF);
				destData[dp + do1] = (byte)((destPixel >> 16) & 0xFF);
				destData[dp + do2] = (byte)((destPixel >> 8) & 0xFF);
				destData[dp + do3] = (byte)(destPixel & 0xFF);
				break;
		}
		/* X stretch ends here */
		if (dy == dyd)
			break;
		while (ey >= 0) {
			ys += sys;
			ey -= dyd2;
		}
		yd += syd;
		ey += dys2;
	}
}

/**
 * Blits an index palette image into an index palette image.
 */
static void blit(int op, byte[] srcData, int srcDepth, int srcStride, int srcOrder, int srcX, int srcY, int srcWidth, int srcHeight, byte[] srcReds, byte[] srcGreens, byte[] srcBlues, int srcGlobalAlpha, byte[] srcAlphaData, int srcAlphaStride, byte[] destData, int destDepth, int destStride, int destOrder, int destX, int destY, int destWidth, int destHeight, byte[] destReds, byte[] destGreens, byte[] destBlues, boolean flipX, boolean flipY) {
	int xs1, ys1, xs2, ys2, xd1, yd1, xd2, yd2;
	int dyd, dys, ey, dy, dyd2, dys2, ys, yd;
	short syd, sys;
	int dxd, dxs, ex, dx, dxd2, dxs2, xs, xd;
	short sxd, sxs, sas;
	int sp, dp, sap = 0;
	int sr = 0, sg = 0, sb = 0, sa = 0, dr = 0, dg = 0, db = 0, da = 0;
	int srcPixel = 0, destPixel = 0;
	int i, j, offset = 0;
	byte[] srcMasks = null, destMasks = null, destInverseMasks = null;
	int destN = 1 << destDepth, srcN = 1 << srcDepth;
	int r, g, b, nearestPixel = 0, lastPixel = -1;
	int distance, minDistance;
	int[] mapping = null;
	boolean samePalette = srcReds == destReds && srcGreens == destGreens && srcBlues == srcBlues;
	boolean sameAsSrc = op == BLIT_SRC && samePalette && srcDepth <= destDepth;

	if (srcReds != null && srcN > srcReds.length) srcN = srcReds.length;
	if (destReds != null && destN > destReds.length) destN = destReds.length;
	if (op == BLIT_SRC && srcReds != null) {
		mapping = new int[srcN];		
		for (i = 0; i < srcN; i++) {
			minDistance = 0x7FFFFFFF;
			nearestPixel = 0;
			for (j = 0; j < destN; j++) {
				r = (destReds[j] & 0xFF) - (srcReds[i] & 0xFF);
				g = (destGreens[j] & 0xFF) - (srcGreens[i] & 0xFF);
				b = (destBlues[j] & 0xFF) - (srcBlues[i] & 0xFF);
				distance = r*r + g*g + b*b;
				if (distance < minDistance) {
					nearestPixel = j;
					if (distance == 0) break;
					minDistance = distance;
				}
			}
			mapping[i] = nearestPixel;
		}
	}
	if (op == BLIT_SRC && srcDepth == destDepth && (mapping != null || samePalette))
	{
		switch (srcDepth) {
			case 1:
				stretch1(srcData, srcStride, srcOrder, srcX, srcY, srcWidth, srcHeight, destData, destStride, destOrder, destX, destY, destWidth, destHeight, flipX, flipY);
				break;
			case 2:
				stretch2(srcData, srcStride, srcX, srcY, srcWidth, srcHeight, destData, destStride, destX, destY, destWidth, destHeight, mapping, flipX, flipY);
				break;
			case 4:
				stretch4(srcData, srcStride, srcX, srcY, srcWidth, srcHeight, destData, destStride, destX, destY, destWidth, destHeight, mapping, flipX, flipY);
				break;
			case 8:
				stretch8(srcData, srcStride, srcX, srcY, srcWidth, srcHeight, destData, destStride, destX, destY, destWidth, destHeight, mapping, flipX, flipY);
				break;
		}
		return;
	}
	switch (srcDepth) {
		case 1: srcMasks = msbMasks1; break;
		case 2: srcMasks = masks2; break;
	}
	switch (destDepth) {
		case 1: destMasks = msbMasks1; destInverseMasks = msbInverseMasks1; break;
		case 2: destMasks = masks2; destInverseMasks = inverseMasks2; break;
	}
	if (srcOrder == LSB_FIRST) {
		switch (srcDepth) {
			case 1: srcMasks = lsbMasks1; break;
		}
	}
	if (destOrder == LSB_FIRST) {
		switch (destDepth) {
			case 1: destMasks = lsbMasks1; destInverseMasks = lsbInverseMasks1; break;
		}
	}

	xs1 = srcX; xs2 = srcX + srcWidth - 1;
	ys1 = srcY; ys2 = srcY + srcHeight - 1;
	if (flipX) {
		xd1 = destX + destWidth - 1;
		xd2 = destX;
	} else {
		xd1 = destX;
		xd2 = destX + destWidth - 1;
	}
	if (flipY) {
		yd1 = destY + destHeight - 1;
		yd2 = destY;
	} else {
		yd1 = destY;
		yd2 = destY + destHeight - 1;
	}

	/* Y preliminary calculations */
	dyd = yd2 - yd1;
	if (dyd < 0) dyd = -dyd;
	dys = ys2 - ys1;
	if (dys < 0) dys = -dys;
	dyd2 = dyd << 1;
	dys2 = dys << 1;
	syd = (short)((yd2 - yd1) > 0 ? 1 : -1);
	sys = (short)((ys2 - ys1) > 0 ? 1 : -1);
	ey = dys2 - dyd;
	ys = ys1;
	yd = yd1;
	/* X preliminary calculations */
	dxd = xd2 - xd1;
	if (dxd < 0) dxd = -dxd;
	dxs = xs2 - xs1;
	if (dxs < 0) dxs = -dxs;
	dxs2 = dxs << 1;
	dxd2 = dxd << 1;
	sxs = sas = (short)((xs2 - xs1) > 0 ? 1 : -1);
	sxd = (short)((xd2 - xd1) > 0 ? 1 : -1);
	
	if (srcGlobalAlpha != -1) srcAlphaData = null;
	sa = srcGlobalAlpha;
	for (dy = 0; dy <= dyd; dy++) {
		/* X stretch starts here */
		offset = 3 - (srcX % 4);
		ex = dxs2 - dxd;
		xs = xs1;
		xd = xd1;
		sp = ys * srcStride;
		dp = yd * destStride;
		if (srcAlphaData != null) sap = ys * srcAlphaStride;
		for (dx = 0; dx < dxd; dx++) {
			if (srcAlphaData != null) sa = srcAlphaData[sap] & 0xFF;
			switch (srcDepth) {
				case  1:
					srcPixel = (srcData[sp + (xs >> 3)] & srcMasks[xs & 7]) == 0 ? 0 : 1;
					break;
				case  2:
					srcPixel = ((srcData[sp + (xs >> 2)] & srcMasks[offset]) & 0xFF) >> (offset * 2);
					break;
				case  4:
					srcPixel = srcData[sp + (xs >> 1)] & 0xFF;
					if ((xs & 0x1) == 0) {
						srcPixel = srcPixel >> 4;
					} else {
						srcPixel = srcPixel & 0x0F;
					}
					break;
				case  8:
					srcPixel = srcData[sp + xs] & 0xFF;
					break;
			}
			if (mapping != null) {
				destPixel = mapping[srcPixel];
			} else if (sameAsSrc) {
				destPixel = srcPixel;
			} else {
				dr = sr = srcReds[srcPixel] & 0xFF;
				dg = sg = srcGreens[srcPixel] & 0xFF;
				db = sb = srcBlues[srcPixel] & 0xFF;
				if (op != BLIT_SRC) {
					switch (destDepth) {
						case  1:
							destPixel = (destData[dp + (xd >> 3)] & destMasks[xd & 7]) == 0 ? 0 : 1;
							break;
						case  2:
							destPixel = ((destData[dp + (xd >> 2)] & destMasks[offset]) & 0xFF) >> (offset * 2);
							break;
						case  4:
							destPixel = destData[dp + (xd >> 1)] & 0xFF;
							if ((xs & 0x1) == 0) {
								destPixel = destPixel >> 4;
							} else {
								destPixel = destPixel & 0x0F;
							}
							break;
						case  8:
							destPixel = destData[dp + xd] & 0xFF;
							break;
					}
					dr = destReds[destPixel] & 0xFF;
					dg = destGreens[destPixel] & 0xFF;
					db = destBlues[destPixel] & 0xFF;
					switch (op) {
						case BLIT_ALPHA:
							dr += (sr - dr) * sa / 255;
							dg += (sg - dg) * sa / 255;
							db += (sb - db) * sa / 255;
						break;
					}
				}
				if (lastPixel == -1 || lastPixel != srcPixel) {
					minDistance = 0x7FFFFFFF;
					nearestPixel = 0;
					for (j = 0; j < destN; j++) {
						r = (destReds[j] & 0xFF) - dr;
						g = (destGreens[j] & 0xFF) - dg;
						b = (destBlues[j] & 0xFF) - db;
						distance = r*r + g*g + b*b;
						if (distance < minDistance) {
							nearestPixel = j;
							if (distance == 0) break;
							minDistance = distance;
						}
					}
					lastPixel = srcPixel;
				}
				destPixel = nearestPixel;
			}
			switch (destDepth) {
				case  1:
					if ((destPixel & 0x1) == 1) {
						destData[dp + (xd >> 3)] |= destInverseMasks[xd & 7];
					} else {
						destData[dp + (xd >> 3)] &= destInverseMasks[xd & 7] ^ -1;
					}
					break;
				case  2:
					destData[dp + (xd >> 2)] = (byte)((destData[dp + (xd >> 2)] & destInverseMasks[offset]) | (destPixel << (offset * 2)));
					break;
				case  4:
					if ((xd & 0x1) == 0) {
						destData[dp + (xd >> 1)] = (byte)((destData[dp + (xd >> 1)] & 0x0F) | ((destPixel & 0x0F) << 4));
					} else {
						destData[dp + (xd >> 1)] = (byte)((destData[dp + (xd >> 1)] & 0xF0) | (destPixel & 0x0F));
					}
					break;
				case  8:
					destData[dp + xd] = (byte)(destPixel & 0xFF);
					break;
			}
			while (ex >= 0) {
				xs += sxs;
				ex -= dxd2;
				if (srcAlphaData != null) sap += sas;
			}
			xd += sxd;
			ex += dxs2;
			if (offset == 0) {
				offset = 3;
			} else {
				offset--;
			}
		}
		
		if (srcAlphaData != null) sa = srcAlphaData[sap] & 0xFF;
		switch (srcDepth) {
			case  1:
				srcPixel = (srcData[sp + (xs >> 3)] & srcMasks[xs & 7]) == 0 ? 0 : 1;
				break;
			case  2:
				srcPixel = ((srcData[sp + (xs >> 2)] & srcMasks[offset]) & 0xFF) >> (offset * 2);
				break;
			case  4:
				srcPixel = srcData[sp + (xs >> 1)] & 0xFF;
				if ((xs & 0x1) == 0) {
					srcPixel = srcPixel >> 4;
				} else {
					srcPixel = srcPixel & 0x0F;
				}
				break;
			case  8:
				srcPixel = srcData[sp + xs] & 0xFF;
				break;
		}
		if (mapping != null) {
			destPixel = mapping[srcPixel];
		} else if (sameAsSrc) {
			destPixel = srcPixel;
		} else {
			dr = sr = srcReds[srcPixel] & 0xFF;
			dg = sg = srcGreens[srcPixel] & 0xFF;
			db = sb = srcBlues[srcPixel] & 0xFF;
			if (op != BLIT_SRC) {
				switch (destDepth) {
					case  1:
						destPixel = (destData[dp + (xd >> 3)] & destMasks[xd & 7]) == 0 ? 0 : 1;
						break;
					case  2:
						destPixel = ((destData[dp + (xd >> 2)] & destMasks[offset]) & 0xFF) >> (offset * 2);
						break;
					case  4:
						destPixel = destData[dp + (xd >> 1)] & 0xFF;
						if ((xs & 0x1) == 0) {
							destPixel = destPixel >> 4;
						} else {
							destPixel = destPixel & 0x0F;
						}
						break;
					case  8:
						destPixel = destData[dp + xd] & 0xFF;
						break;
				}
				dr = destReds[destPixel] & 0xFF;
				dg = destGreens[destPixel] & 0xFF;
				db = destBlues[destPixel] & 0xFF;
				switch (op) {
					case BLIT_ALPHA:
						dr += (sr - dr) * sa / 255;
						dg += (sg - dg) * sa / 255;
						db += (sb - db) * sa / 255;
					break;
				}
			}
			if (lastPixel == -1 || lastPixel != srcPixel) {
				minDistance = 0x7FFFFFFF;
				nearestPixel = 0;
				for (j = 0; j < destN; j++) {
					r = (destReds[j] & 0xFF) - dr;
					g = (destGreens[j] & 0xFF) - dg;
					b = (destBlues[j] & 0xFF) - db;
					distance = r*r + g*g + b*b;
					if (distance < minDistance) {
						nearestPixel = j;
						if (distance == 0) break;
						minDistance = distance;
					}
				}
				lastPixel = srcPixel;
			}
			destPixel = nearestPixel;
		}
		switch (destDepth) {
			case  1:
				if ((destPixel & 0x1) == 1) {
					destData[dp + (xd >> 3)] |= destInverseMasks[xd & 7];
				} else {
					destData[dp + (xd >> 3)] &= destInverseMasks[xd & 7] ^ -1;
				}
				break;
			case  2:
				destData[dp + (xd >> 2)] = (byte)((destData[dp + (xd >> 2)] & destInverseMasks[offset]) | (destPixel << (offset * 2)));
				break;
			case  4:
				if ((xd & 0x1) == 0) {
					destData[dp + (xd >> 1)] = (byte)((destData[dp + (xd >> 1)] & 0x0F) | ((destPixel & 0x0F) << 4));
				} else {
					destData[dp + (xd >> 1)] = (byte)((destData[dp + (xd >> 1)] & 0xF0) | (destPixel & 0x0F));
				}
				break;
			case  8:
				destData[dp + xd] = (byte)(destPixel & 0xFF);
				break;
		}
		/* X stretch ends here */
		if (dy == dyd)
			break;
		while (ey >= 0) {
			ys += sys;
			ey -= dyd2;
		}
		yd += syd;
		ey += dys2;
	}
}

/**
 * Blits a direct palette image into an index palette image.
 */
static void blit(int op, byte[] srcData, int srcDepth, int srcStride, int srcOrder, int srcX, int srcY, int srcWidth, int srcHeight, int srcRedMask, int srcGreenMask, int srcBlueMask, int srcGlobalAlpha, byte[] srcAlphaData, int srcAlphaStride, byte[] destData, int destDepth, int destStride, int destOrder, int destX, int destY, int destWidth, int destHeight, byte[] destReds, byte[] destGreens, byte[] destBlues, boolean flipX, boolean flipY) {
	int xs1, ys1, xs2, ys2, xd1, yd1, xd2, yd2;
	int dyd, dys, ey, dy, dyd2, dys2, ys, yd;
	short syd, sys;
	int dxd, dxs, ex, dx, dxd2, dxs2, xd;
	short sxd, sxs, sas;
	int sp, dp, sap = 0;
	int sr = 0, sg = 0, sb = 0, sa = 0, dr = 0, dg = 0, db = 0, da = 0;
	int srcPixel = 0, destPixel = 0;
	short so0 = 0, so1 = 1, so2 = 2, so3 = 3;
	int srcRedShift, srcGreenShift, srcBlueShift;
	int j, offset = 0;
	byte[] destMasks = null, destInverseMasks = null;
	int destN = 1 << destDepth;
	int r, g, b, nearestPixel = 0, lastPixel = -1;
	int distance, minDistance;
	
	srcRedShift = 32 - getMSBOffset(srcRedMask);
	srcGreenShift = 32 - getMSBOffset(srcGreenMask);
	srcBlueShift = 32 - getMSBOffset(srcBlueMask);
	if (destReds != null && destN > destReds.length) destN = destReds.length;
	switch (destDepth) {
		case 1: destMasks = msbMasks1; destInverseMasks = msbInverseMasks1; break;
		case 2: destMasks = masks2; destInverseMasks = inverseMasks2; break;
	}
	if (srcOrder == LSB_FIRST) {
		switch (srcDepth) {
			case 16: so0 = 1; so1 = 0; break;
			case 24: so0 = 2; so1 = 1; so2 = 0; break;
			case 32: so0 = 3; so1 = 2; so2 = 1; so3 = 0; break;
		}
	}
	if (destOrder == LSB_FIRST) {
		switch (destDepth) {
			case 1: destMasks = lsbMasks1; destInverseMasks = lsbInverseMasks1; break;
		}
	}

	xs1 = srcX; xs2 = srcX + srcWidth - 1;
	ys1 = srcY; ys2 = srcY + srcHeight - 1;
	if (flipX) {
		xd1 = destX + destWidth - 1;
		xd2 = destX;
	} else {
		xd1 = destX;
		xd2 = destX + destWidth - 1;
	}
	if (flipY) {
		yd1 = destY + destHeight - 1;
		yd2 = destY;
	} else {
		yd1 = destY;
		yd2 = destY + destHeight - 1;
	}

	/* Y preliminary calculations */
	dyd = yd2 - yd1;
	if (dyd < 0) dyd = -dyd;
	dys = ys2 - ys1;
	if (dys < 0) dys = -dys;
	dyd2 = dyd << 1;
	dys2 = dys << 1;
	syd = (short)((yd2 - yd1) > 0 ? 1 : -1);
	sys = (short)((ys2 - ys1) > 0 ? 1 : -1);
	ey = dys2 - dyd;
	ys = ys1;
	yd = yd1;
	/* X preliminary calculations */
	dxd = xd2 - xd1;
	if (dxd < 0) dxd = -dxd;
	dxs = xs2 - xs1;
	if (dxs < 0) dxs = -dxs;
	dxs2 = dxs << 1;
	dxd2 = dxd << 1;
	sxs = sas = (short)((xs2 - xs1) > 0 ? 1 : -1);
	sxd = (short)((xd2 - xd1) > 0 ? 1 : -1);

	sxs *= srcDepth / 8;
	xs1 *= srcDepth / 8;
	
	if (srcGlobalAlpha != -1) srcAlphaData = null;
	sa = srcGlobalAlpha;
	for (dy = 0; dy <= dyd; dy++) {
		/* X stretch starts here */
		offset = 3 - (srcX % 4);
		ex = dxs2 - dxd;
		xd = xd1;
		sp = ys * srcStride + xs1;
		dp = yd * destStride;
		if (srcAlphaData != null) sap = ys * srcAlphaStride + xs1;
		for (dx = 0; dx < dxd; dx++) {
			if (srcAlphaData != null) sa = srcAlphaData[sap] & 0xFF;
			switch (srcDepth) {
				case 16:
					srcPixel = ((srcData[sp+so0] & 0xFF) << 8) | (srcData[sp+so1] & 0xFF);
					break;
				case 24:
					srcPixel = ((srcData[sp+so0] & 0xFF) << 16) | ((srcData[sp+so1] & 0xFF) << 8) | 
						(srcData[sp+so2] & 0xFF);
					break;
				case 32:
					srcPixel = ((srcData[sp+so0] & 0xFF) << 24) | ((srcData[sp+so1] & 0xFF) << 16) |
						((srcData[sp+so2] & 0xFF) << 8) | (srcData[sp+so3] & 0xFF);
					break;
			}
			dr = sr = ((srcPixel & srcRedMask) << srcRedShift) >>> 24;
			dg = sg = ((srcPixel & srcGreenMask) << srcGreenShift) >>> 24;
			db = sb = ((srcPixel & srcBlueMask) << srcBlueShift) >>> 24;
			if (op != BLIT_SRC) {
				switch (destDepth) {
					case  1:
						destPixel = (destData[dp + (xd >> 3)] & destMasks[xd & 7]) == 0 ? 0 : 1;
						break;
					case  2:
						destPixel = ((destData[dp + (xd >> 2)] & destMasks[offset]) & 0xFF) >> (offset * 2);
						break;
					case  4:
						destPixel = destData[dp + (xd >> 1)] & 0xFF;
						if ((xd & 0x1) == 0) {
							destPixel = destPixel >> 4;
						} else {
							destPixel = destPixel & 0x0F;
						}
						break;
					case  8:
						destPixel = destData[dp + xd] & 0xFF;
						break;
				}
				dr = destReds[destPixel] & 0xFF;
				dg = destGreens[destPixel] & 0xFF;
				db = destBlues[destPixel] & 0xFF;
				switch (op) {
					case BLIT_ALPHA:
						dr += (sr - dr) * sa / 255;
						dg += (sg - dg) * sa / 255;
						db += (sb - db) * sa / 255;
					break;
				}
			}
			if (lastPixel == -1 || lastPixel != srcPixel) {
				minDistance = 0x7FFFFFFF;
				nearestPixel = 0;
				for (j = 0; j < destN; j++) {
					r = (destReds[j] & 0xFF) - dr;
					g = (destGreens[j] & 0xFF) - dg;
					b = (destBlues[j] & 0xFF) - db;
					distance = r*r + g*g + b*b;
					if (distance < minDistance) {
						nearestPixel = j;
						if (distance == 0) break;
						minDistance = distance;
					}
				}
				lastPixel = srcPixel;
			}
			destPixel = nearestPixel;
			switch (destDepth) {
				case  1:
					if ((destPixel & 0x1) == 1) {
						destData[dp + (xd >> 3)] |= destInverseMasks[xd & 7];
					} else {
						destData[dp + (xd >> 3)] &= destInverseMasks[xd & 7] ^ -1;
					}
					break;
				case  2:
					destData[dp + (xd >> 2)] = (byte)((destData[dp + (xd >> 2)] & destInverseMasks[offset]) | (destPixel << (offset * 2)));
					break;
				case  4:
					if ((xd & 0x1) == 0) {
						destData[dp + (xd >> 1)] = (byte)((destData[dp + (xd >> 1)] & 0x0F) | ((destPixel & 0x0F) << 4));
					} else {
						destData[dp + (xd >> 1)] = (byte)((destData[dp + (xd >> 1)] & 0xF0) | (destPixel & 0x0F));
					}
					break;
				case  8:
					destData[dp + xd] = (byte)(destPixel & 0xFF);
					break;
			}
			while (ex >= 0) {
				sp += sxs;
				ex -= dxd2;
				if (srcAlphaData != null) sap += sas;
			}
			xd += sxd;
			ex += dxs2;
			if (offset == 0) {
				offset = 3;
			} else {
				offset--;
			}
		}
		
		if (srcAlphaData != null) sa = srcAlphaData[sap] & 0xFF;
		switch (srcDepth) {
			case 16:
				srcPixel = ((srcData[sp+so0] & 0xFF) << 8) | (srcData[sp+so1] & 0xFF);
				break;
			case 24:
				srcPixel = ((srcData[sp+so0] & 0xFF) << 16) | ((srcData[sp+so1] & 0xFF) << 8) | 
					(srcData[sp+so2] & 0xFF);
				break;
			case 32:
				srcPixel = ((srcData[sp+so0] & 0xFF) << 24) | ((srcData[sp+so1] & 0xFF) << 16) |
					((srcData[sp+so2] & 0xFF) << 8) | (srcData[sp+so3] & 0xFF);
				break;
		}
		dr = sr = ((srcPixel & srcRedMask) << srcRedShift) >>> 24;
		dg = sg = ((srcPixel & srcGreenMask) << srcGreenShift) >>> 24;
		db = sb = ((srcPixel & srcBlueMask) << srcBlueShift) >>> 24;
		if (op != BLIT_SRC) {
			switch (destDepth) {
				case  1:
					destPixel = (destData[dp + (xd >> 3)] & destMasks[xd & 7]) == 0 ? 0 : 1;
					break;
				case  2:
					destPixel = ((destData[dp + (xd >> 2)] & destMasks[offset]) & 0xFF) >> (offset * 2);
					break;
				case  4:
					destPixel = destData[dp + (xd >> 1)] & 0xFF;
					if ((xd & 0x1) == 0) {
						destPixel = destPixel >> 4;
					} else {
						destPixel = destPixel & 0x0F;
					}
					break;
				case  8:
					destPixel = destData[dp + xd] & 0xFF;
					break;
			}
			dr = destReds[destPixel] & 0xFF;
			dg = destGreens[destPixel] & 0xFF;
			db = destBlues[destPixel] & 0xFF;
			switch (op) {
				case BLIT_ALPHA:
					dr += (sr - dr) * sa / 255;
					dg += (sg - dg) * sa / 255;
					db += (sb - db) * sa / 255;
				break;
			}
		}
		if (lastPixel == -1 || lastPixel != srcPixel) {
			minDistance = 0x7FFFFFFF;
			nearestPixel = 0;
			for (j = 0; j < destN; j++) {
				r = (destReds[j] & 0xFF) - dr;
				g = (destGreens[j] & 0xFF) - dg;
				b = (destBlues[j] & 0xFF) - db;
				distance = r*r + g*g + b*b;
				if (distance < minDistance) {
					nearestPixel = j;
					if (distance == 0) break;
					minDistance = distance;
				}
			}
			lastPixel = srcPixel;
		}
		destPixel = nearestPixel;
		switch (destDepth) {
			case  1:
				if ((destPixel & 0x1) == 1) {
					destData[dp + (xd >> 3)] |= destInverseMasks[xd & 7];
				} else {
					destData[dp + (xd >> 3)] &= destInverseMasks[xd & 7] ^ -1;
				}
				break;
			case  2:
				destData[dp + (xd >> 2)] = (byte)((destData[dp + (xd >> 2)] & destInverseMasks[offset]) | (destPixel << (offset * 2)));
				break;
			case  4:
				if ((xd & 0x1) == 0) {
					destData[dp + (xd >> 1)] = (byte)((destData[dp + (xd >> 1)] & 0x0F) | ((destPixel & 0x0F) << 4));
				} else {
					destData[dp + (xd >> 1)] = (byte)((destData[dp + (xd >> 1)] & 0xF0) | (destPixel & 0x0F));
				}
				break;
			case  8:
				destData[dp + xd] = (byte)(destPixel & 0xFF);
				break;
		}
		/* X stretch ends here */
		if (dy == dyd)
			break;
		while (ey >= 0) {
			ys += sys;
			ey -= dyd2;
		}
		yd += syd;
		ey += dys2;
	}
}

/**
 * Stretches the source, a 1-bit image, into the destination, a 1-bit image.
 */
static void stretch1(byte[] srcData, int srcStride, int srcOrder, int srcX, int srcY, int srcWidth, int srcHeight, byte[] destData, int destStride, int destOrder, int destX, int destY, int destWidth, int destHeight, boolean flipX, boolean flipY) {
	int xs1, ys1, xs2, ys2, xd1, yd1, xd2, yd2;
	int dyd, dys, ey, dy, dyd2, dys2, ys, yd;
	short syd, sys;
	int dxd, dxs, ex, dx, dxd2, dxs2, xs, xd;
	short sxd, sxs;
	byte pixel;
	int sp, dp;
	byte[] masks, inverseMasks;
	
	if (srcOrder == LSB_FIRST) {
		masks = lsbMasks1;
	} else {
		masks = msbMasks1;
	}
	if (destOrder == LSB_FIRST) {
		inverseMasks = lsbInverseMasks1;
	} else {
		inverseMasks = msbInverseMasks1;
	}
	
	xs1 = srcX; xs2 = srcX + srcWidth - 1;
	ys1 = srcY; ys2 = srcY + srcHeight - 1;
	if (flipX) {
		xd1 = destX + destWidth - 1;
		xd2 = destX;
	} else {
		xd1 = destX;
		xd2 = destX + destWidth - 1;
	}
	if (flipY) {
		yd1 = destY + destHeight - 1;
		yd2 = destY;
	} else {
		yd1 = destY;
		yd2 = destY + destHeight - 1;
	}

	/* Y preliminary calculations */
	dyd = yd2 - yd1;
	if (dyd < 0) dyd = -dyd;
	dys = ys2 - ys1;
	if (dys < 0) dys = -dys;
	dyd2 = dyd << 1;
	dys2 = dys << 1;
	syd = (short)((yd2 - yd1) > 0 ? 1 : -1);
	sys = (short)((ys2 - ys1) > 0 ? 1 : -1);
	ey = dys2 - dyd;
	ys = ys1;
	yd = yd1;
	/* X preliminary calculations */
	dxd = xd2 - xd1;
	if (dxd < 0) dxd = -dxd;
	dxs = xs2 - xs1;
	if (dxs < 0) dxs = -dxs;
	dxd2 = dxd << 1;
	dxs2 = dxs << 1;
	sxd = (short)((xd2 - xd1) > 0 ? 1 : -1);
	sxs = (short)((xs2 - xs1) > 0 ? 1 : -1);
	
	for (dy = 0; dy <= dyd; dy++) {
		/* X stretch starts here */
		ex = dxs2 - dxd;
		xs = xs1;
		xd = xd1;
		sp = ys * srcStride;
		dp = yd * destStride;
		pixel = (byte)(srcData[sp + (xs >> 3)] & masks[xs & 7]);
		for (dx = 0; dx < dxd; dx++) {
			if (pixel != 0)
				destData[dp + (xd >> 3)] |= masks[xd & 7];
			else
				destData[dp + (xd >> 3)] &= inverseMasks[xd & 7];
			if (ex >= 0) {
				do {
					xs += sxs;
					ex -= dxd2;
				} while (ex >= 0);
				pixel = (byte)(srcData[sp + (xs >> 3)] & masks[xs & 7]);
			}
			xd += sxd;
			ex += dxs2;
		}
		if (pixel != 0)
			destData[dp + (xd >> 3)] |= masks[xd & 7];
		else
			destData[dp + (xd >> 3)] &= inverseMasks[xd & 7];
		/* X stretch ends here */
		if (dy == dyd)
			break;
		while (ey >= 0) {
			ys += sys;
			ey -= dyd2;
		}
		yd += syd;
		ey += dys2;
	}
}

/**
 * Stretches the source, a 16-bit image, into the destination, a 16-bit image.
 * The images are assumed to have the same red, green, and blue masks.
 *
 * Untested - would need an X server with the same red, green and blue
 * masks as the file has, namely, 0x7C00, 0x3E0, 0x1F. Most 16-bit X servers
 * have masks of 0xF800, 0x7E0, 0x1F.
 */
static void stretch16(byte[] srcData, int srcStride, int srcX, int srcY, int srcWidth, int srcHeight, byte[] destData, int destStride, int destX, int destY, int destWidth, int destHeight, boolean flipX, boolean flipY) {
	int xs1, ys1, xs2, ys2, xd1, yd1, xd2, yd2;
	int dyd, dys, ey, dy, dyd2, dys2, yd, ys;
	short syd, sys;
	int dxd, dxs, ex, dx, dxd2, dxs2, xs, xd;
	short sxd, sxs;
	byte pixel0, pixel1;
	int sp, dp;
	
	xs1 = srcX; xs2 = srcX + srcWidth - 1;
	ys1 = srcY; ys2 = srcY + srcHeight - 1;
	if (flipX) {
		xd1 = destX + destWidth - 1;
		xd2 = destX;
	} else {
		xd1 = destX;
		xd2 = destX + destWidth - 1;
	}
	if (flipY) {
		yd1 = destY + destHeight - 1;
		yd2 = destY;
	} else {
		yd1 = destY;
		yd2 = destY + destHeight - 1;
	}

	/* Y preliminary calculations */
	dyd = yd2 - yd1;
	if (dyd < 0) dyd = -dyd;
	dys = ys2 - ys1;
	if (dys < 0) dys = -dys;
	dyd2 = dyd << 1;
	dys2 = dys << 1;
	syd = (short)((yd2 - yd1) > 0 ? 1 : -1);
	sys = (short)((ys2 - ys1) > 0 ? 1 : -1);
	ey = dys2 - dyd;
	ys = ys1;
	yd = yd1;
	/* X preliminary calculations */
	dxd = xd2 - xd1;
	if (dxd < 0) dxd = -dxd;
	dxs = xs2 - xs1;
	if (dxs < 0) dxs = -dxs;
	dxd2 = dxd << 1;
	dxs2 = dxs << 1;
	sxd = (short)((xd2 - xd1) > 0 ? 1 : -1);
	sxs = (short)((xs2 - xs1) > 0 ? 1 : -1);
	
	for (dy = 0; dy <= dyd; dy++) {
		/* X stretch starts here */
		ex = dxs2 - dxd;
		xs = xs1;
		xd = xd1;
		sp = ys * srcStride;
		dp = yd * destStride;
		pixel0 = srcData[sp + xs];
		pixel1 = srcData[sp + xs + 1];
		for (dx = 0; dx < dxd; dx++) {
			destData[dp + xd] = pixel0;
			destData[dp + xd + 1] = pixel1;
			if (ex >= 0) {
				do {
					xs += (sxs << 1);
					ex -= dxd2;
				} while (ex >= 0);
				pixel0 = srcData[sp + xs];
				pixel1 = srcData[sp + xs + 1];
			}
			xd += (sxd << 1);
			ex += dxs2;
		}
		destData[dp + xd] = pixel0;
		destData[dp + xd + 1] = pixel1;
		/* X stretch ends here */
		if (dy == dyd)
			break;
		while (ey >= 0) {
			ys += sys;
			ey -= dyd2;
		}
		yd += syd;
		ey += dys2;
	}
}

/**
 * Stretches the source, a 2-bit image, into the destination, a 2-bit image.
 */
static void stretch2(byte[] srcData, int srcStride, int srcX, int srcY, int srcWidth, int srcHeight, byte[] destData, int destStride, int destX, int destY, int destWidth, int destHeight, int[] mapping, boolean flipX, boolean flipY) {
	int xs1, ys1, xs2, ys2, xd1, yd1, xd2, yd2;
	int dyd, dys, ey, dy, dyd2, dys2, yd, ys;
	short syd, sys;
	int dxd, dxs, ex, dx, dxd2, dxs2, xs, xd;
	short sxd, sxs;
	byte pixel;
	int sp, dp, x;
	byte [] masks = { (byte)0x03, (byte)0x0C, (byte)0x30, (byte)0xC0 };
	byte [] inverseMasks = { (byte)0xFC, (byte)0xF3, (byte)0xCF, (byte)0x3F };
	
	xs1 = srcX; xs2 = srcX + srcWidth - 1;
	ys1 = srcY; ys2 = srcY + srcHeight - 1;
	if (flipX) {
		xd1 = destX + destWidth - 1;
		xd2 = destX;
	} else {
		xd1 = destX;
		xd2 = destX + destWidth - 1;
	}
	if (flipY) {
		yd1 = destY + destHeight - 1;
		yd2 = destY;
	} else {
		yd1 = destY;
		yd2 = destY + destHeight - 1;
	}

	/* Y preliminary calculations */
	dyd = yd2 - yd1;
	if (dyd < 0) dyd = -dyd;
	dys = ys2 - ys1;
	if (dys < 0) dys = -dys;
	dyd2 = dyd << 1;
	dys2 = dys << 1;
	syd = (short)((yd2 - yd1) > 0 ? 1 : -1);
	sys = (short)((ys2 - ys1) > 0 ? 1 : -1);
	ey = dys2 - dyd;
	ys = ys1;
	yd = yd1;
	/* X preliminary calculations */
	dxd = xd2 - xd1;
	if (dxd < 0) dxd = -dxd;
	dxs = xs2 - xs1;
	if (dxs < 0) dxs = -dxs;
	dxd2 = dxd << 1;
	dxs2 = dxs << 1;
	sxd = (short)((xd2 - xd1) > 0 ? 1 : -1);
	sxs = (short)((xs2 - xs1) > 0 ? 1 : -1);
	
	for (dy = 0; dy <= dyd; dy++) {
		/* X stretch starts here */
		int offset = 3 - (srcX % 4);
		ex = dxs2 - dxd;
		xs = xs1;
		xd = xd1;
		sp = ys * srcStride;
		dp = yd * destStride;
		x = (byte)(((srcData[sp + (xs >> 2)] & masks[offset]) & 0xFF) >> (offset * 2));
		pixel = (byte)(mapping == null ? x : mapping[x]);
		for (dx = 0; dx < dxd; dx++) {
			destData[dp + (xd >> 2)] = (byte)((destData[dp + (xd >> 2)] & inverseMasks[offset]) | (pixel << (offset * 2)));
			if (ex >= 0) {
				do {
					xs += sxs;
					ex -= dxd2;
				} while (ex >= 0);
				x = (byte)(((srcData[sp + (xs >> 2)] & masks[offset]) & 0xFF) >> (offset * 2));
				pixel = (byte)(mapping == null ? x : mapping[x]);
			}
			xd += sxd;
			ex += dxs2;
			if (offset == 0) {
				offset = 3;
			} else {
				offset--;
			}
		}
		destData[dp + (xd >> 2)] = (byte)((destData[dp + (xd >> 2)] & inverseMasks[offset]) | (pixel << (offset * 2)));
		/* X stretch ends here */
		if (dy == dyd)
			break;
		while (ey >= 0) {
			ys += sys;
			ey -= dyd2;
		}
		yd += syd;
		ey += dys2;
	}
}

/**
 * Stretches the source, a 24-bit image, into the destination, a 24-bit image.
 * The images must have the same red, green, and blue masks.
 * The image are assumed to have 24 bits per pixel; many 24-bit images
 * use 32 bits per pixel.
 *
 * Untested. Would require an X server with a depth of 24 which has 24 bits per
 * pixel. Most X servers with depth 24 actually have 32 bits per pixel.
 */
static void stretch24(byte[] srcData, int srcStride, int srcX, int srcY, int srcWidth, int srcHeight, byte[] destData, int destStride, int destX, int destY, int destWidth, int destHeight, boolean flipX, boolean flipY) {
	int xs1, ys1, xs2, ys2, xd1, yd1, xd2, yd2;
	int dyd, dys, ey, dy, dyd2, dys2, ys, yd;
	short syd, sys;
	int dxd, dxs, ex, dx, dxd2, dxs2;
	short sxd, sxs;
	byte r, g, b;
	int sp, dp;
	
	xs1 = srcX; xs2 = srcX + srcWidth - 1;
	ys1 = srcY; ys2 = srcY + srcHeight - 1;
	if (flipX) {
		xd1 = destX + destWidth - 1;
		xd2 = destX;
	} else {
		xd1 = destX;
		xd2 = destX + destWidth - 1;
	}
	if (flipY) {
		yd1 = destY + destHeight - 1;
		yd2 = destY;
	} else {
		yd1 = destY;
		yd2 = destY + destHeight - 1;
	}

	/* Y preliminary calculations */
	dyd = yd2 - yd1;
	if (dyd < 0) dyd = -dyd;
	dys = ys2 - ys1;
	if (dys < 0) dys = -dys;
	dyd2 = dyd << 1;
	dys2 = dys << 1;
	syd = (short)((yd2 - yd1) > 0 ? 1 : -1);
	sys = (short)((ys2 - ys1) > 0 ? 1 : -1);
	ey = dys2 - dyd;
	ys = ys1;
	yd = yd1;
	/* X preliminary calculations */
	dxd = xd2 - xd1;
	if (dxd < 0) dxd = -dxd;
	dxs = xs2 - xs1;
	if (dxs < 0) dxs = -dxs;
	dxd2 = dxd << 1;
	dxs2 = dxs << 1;
	sxd = (short)((xd2 - xd1) > 0 ? 3 : -3);
	sxs = (short)((xs2 - xs1) > 0 ? 3 : -3);
	xs1 *= 3;
	xd1 *= 3;
	
	for (dy = 0; dy <= dyd; dy++) {
		/* X stretch starts here */
		ex = dxs2 - dxd;
		sp = ys * srcStride + xs1;
		dp = yd * destStride + xd1;
		r = srcData[sp];
		g = srcData[sp + 1];
		b = srcData[sp + 2];
		for (dx = 0; dx < dxd; dx++) {
			destData[dp] = r;
			destData[dp + 1] = g;
			destData[dp + 2] = b;
			if (ex >= 0) {
				while (ex >= 0) {
					sp += sxs;
					ex -= dxd2;
				}
				r = srcData[sp];
				g = srcData[sp + 1];
				b = srcData[sp + 2];
			}
			dp += sxd;
			ex += dxs2;
		}
		destData[dp] = r;
		destData[dp + 1] = g;
		destData[dp + 2] = b;
		/* X stretch ends here */
		if (dy == dyd)
			break;
		while (ey >= 0) {
			ys += sys;
			ey -= dyd2;
		}
		yd += syd;
		ey += dys2;
	}
}

/**
 * Stretches the source, a 32-bit image, into the destination, a 32-bit image.
 * The images must have the same red, green, and blue masks.
 */
static void stretch32(byte[] srcData, int srcStride, int srcX, int srcY, int srcWidth, int srcHeight, byte[] destData, int destStride, int destX, int destY, int destWidth, int destHeight, boolean flipX, boolean flipY) {
	int xs1, ys1, xs2, ys2, xd1, yd1, xd2, yd2;
	int dyd, dys, ey, dy, dyd2, dys2, ys, yd;
	short syd, sys;
	int dxd, dxs, ex, dx, dxd2, dxs2;
	short sxd, sxs;
	byte r, g, b, a;
	int sp, dp;
	
	xs1 = srcX; xs2 = srcX + srcWidth - 1;
	ys1 = srcY; ys2 = srcY + srcHeight - 1;
	if (flipX) {
		xd1 = destX + destWidth - 1;
		xd2 = destX;
	} else {
		xd1 = destX;
		xd2 = destX + destWidth - 1;
	}
	if (flipY) {
		yd1 = destY + destHeight - 1;
		yd2 = destY;
	} else {
		yd1 = destY;
		yd2 = destY + destHeight - 1;
	}

	/* Y preliminary calculations */
	dyd = yd2 - yd1;
	if (dyd < 0) dyd = -dyd;
	dys = ys2 - ys1;
	if (dys < 0) dys = -dys;
	dyd2 = dyd << 1;
	dys2 = dys << 1;
	syd = (short)((yd2 - yd1) > 0 ? 1 : -1);
	sys = (short)((ys2 - ys1) > 0 ? 1 : -1);
	ey = dys2 - dyd;
	ys = ys1;
	yd = yd1;
	/* X preliminary calculations */
	dxd = xd2 - xd1;
	if (dxd < 0) dxd = -dxd;
	dxs = xs2 - xs1;
	if (dxs < 0) dxs = -dxs;
	dxd2 = dxd << 1;
	dxs2 = dxs << 1;
	sxd = (short)((xd2 - xd1) > 0 ? 4 : -4);
	sxs = (short)((xs2 - xs1) > 0 ? 4 : -4);
	xs1 *= 4;
	xd1 *= 4;
	
	for (dy = 0; dy <= dyd; dy++) {
		/* X stretch starts here */
		ex = dxs2 - dxd;
		sp = ys * srcStride + xs1;
		dp = yd * destStride + xd1;
		r = srcData[sp];
		g = srcData[sp + 1];
		b = srcData[sp + 2];
		a = srcData[sp + 3];
		for (dx = 0; dx < dxd; dx++) {
			destData[dp] = r;
			destData[dp + 1] = g;
			destData[dp + 2] = b;
			destData[dp + 3] = a;
			if (ex >= 0) {
				while (ex >= 0) {
					sp += sxs;
					ex -= dxd2;
				}
				r = srcData[sp];
				g = srcData[sp + 1];
				b = srcData[sp + 2];
				a = srcData[sp + 3];
			}
			dp += sxd;
			ex += dxs2;
		}
		destData[dp] = r;
		destData[dp + 1] = g;
		destData[dp + 2] = b;
		destData[dp + 3] = a;
		/* X stretch ends here */
		if (dy == dyd)
			break;
		while (ey >= 0) {
			ys += sys;
			ey -= dyd2;
		}
		yd += syd;
		ey += dys2;
	}
}

/**
 * Stretches the source, a 4-bit image, into the destination, a 4-bit image.
 */
static void stretch4(byte[] srcData, int srcStride, int srcX, int srcY, int srcWidth, int srcHeight, byte[] destData, int destStride, int destX, int destY, int destWidth, int destHeight, int[] mapping, boolean flipX, boolean flipY) {
	int xs1, ys1, xs2, ys2, xd1, yd1, xd2, yd2;
	int dyd, dys, ey, dy, dyd2, dys2, yd, ys;
	short syd, sys;
	int dxd, dxs, ex, dx, dxd2, dxs2, xs, xd;
	short sxd, sxs;
	byte pixel;
	int sp, dp;
	
	xs1 = srcX; xs2 = srcX + srcWidth - 1;
	ys1 = srcY; ys2 = srcY + srcHeight - 1;
	if (flipX) {
		xd1 = destX + destWidth - 1;
		xd2 = destX;
	} else {
		xd1 = destX;
		xd2 = destX + destWidth - 1;
	}
	if (flipY) {
		yd1 = destY + destHeight - 1;
		yd2 = destY;
	} else {
		yd1 = destY;
		yd2 = destY + destHeight - 1;
	}

	/* Y preliminary calculations */
	dyd = yd2 - yd1;
	if (dyd < 0) dyd = -dyd;
	dys = ys2 - ys1;
	if (dys < 0) dys = -dys;
	dyd2 = dyd << 1;
	dys2 = dys << 1;
	syd = (short)((yd2 - yd1) > 0 ? 1 : -1);
	sys = (short)((ys2 - ys1) > 0 ? 1 : -1);
	ey = dys2 - dyd;
	ys = ys1;
	yd = yd1;
	/* X preliminary calculations */
	dxd = xd2 - xd1;
	if (dxd < 0) dxd = -dxd;
	dxs = xs2 - xs1;
	if (dxs < 0) dxs = -dxs;
	dxd2 = dxd << 1;
	dxs2 = dxs << 1;
	sxd = (short)((xd2 - xd1) > 0 ? 1 : -1);
	sxs = (short)((xs2 - xs1) > 0 ? 1 : -1);
	
	for (dy = 0; dy <= dyd; dy++) {
		/* X stretch starts here */
		ex = dxs2 - dxd;
		xs = xs1;
		xd = xd1;
		sp = ys * srcStride;
		dp = yd * destStride;
		int x = srcData[sp + (xs >> 1)];
		if ((xs & 1) != 0) {
			pixel = (byte)((mapping == null) ? (x & 0x0F) : (mapping[x & 0x0F] & 0x0F));
		} else {
			pixel = (byte)((mapping == null) ? (x >> 4) : (mapping[x >> 4] & 0x0F));
		}
		for (dx = 0; dx < dxd; dx++) {
			if ((xd & 1) != 0)
				destData[dp + (xd >> 1)] = (byte)((destData[dp + (xd >> 1)] & 0xF0) | pixel);
			else
				destData[dp + (xd >> 1)] = (byte)((destData[dp + (xd >> 1)] & 0x0F) | (pixel << 4));
			if (ex >= 0) {
				do {
					xs += sxs;
					ex -= dxd2;
				} while (ex >= 0);
				x = srcData[sp + (xs >> 1)];
				if ((xd & 1) != 0) {
					pixel = (byte)((mapping == null) ? (x & 0x0F) : (mapping[x & 0x0F] & 0x0F));
				} else {
					pixel = (byte)((mapping == null) ? ((x >> 4) & 0x0F) : (mapping[x >> 4] & 0x0F));
				}
			}
			xd += sxd;
			ex += dxs2;
		}
		if ((xd & 1) != 0)
			destData[dp + (xd >> 1)] = (byte)((destData[dp + (xd >> 1)] & 0xF0) | pixel);
		else
			destData[dp + (xd >> 1)] = (byte)((destData[dp + (xd >> 1)] & 0x0F) | (pixel << 4));
		/* X stretch ends here */
		if (dy == dyd)
			break;
		while (ey >= 0) {
			ys += sys;
			ey -= dyd2;
		}
		yd += syd;
		ey += dys2;
	}
}

/**
 * Stretches the source, an 8-bit image, into the destination, an 8-bit image.
 */
static void stretch8(byte[] srcData, int srcStride, int srcX, int srcY, int srcWidth, int srcHeight, byte[] destData, int destStride, int destX, int destY, int destWidth, int destHeight, int[] mapping, boolean flipX, boolean flipY) {
	int xs1, ys1, xs2, ys2, xd1, yd1, xd2, yd2;
	int dyd, dys, ey, dy, dyd2, dys2, yd, ys;
	short syd, sys;
	int dxd, dxs, ex, dx, dxd2, dxs2, xs, xd;
	short sxd, sxs;
	byte pixel;
	int sp, dp, x;
	
	xs1 = srcX; xs2 = srcX + srcWidth - 1;
	ys1 = srcY; ys2 = srcY + srcHeight - 1;
	if (flipX) {
		xd1 = destX + destWidth - 1;
		xd2 = destX;
	} else {
		xd1 = destX;
		xd2 = destX + destWidth - 1;
	}
	if (flipY) {
		yd1 = destY + destHeight - 1;
		yd2 = destY;
	} else {
		yd1 = destY;
		yd2 = destY + destHeight - 1;
	}

	/* Y preliminary calculations */
	dyd = yd2 - yd1;
	if (dyd < 0) dyd = -dyd;
	dys = ys2 - ys1;
	if (dys < 0) dys = -dys;
	dyd2 = dyd << 1;
	dys2 = dys << 1;
	syd = (short)((yd2 - yd1) > 0 ? 1 : -1);
	sys = (short)((ys2 - ys1) > 0 ? 1 : -1);
	ey = dys2 - dyd;
	ys = ys1;
	yd = yd1;
	/* X preliminary calculations */
	dxd = xd2 - xd1;
	if (dxd < 0) dxd = -dxd;
	dxs = xs2 - xs1;
	if (dxs < 0) dxs = -dxs;
	dxd2 = dxd << 1;
	dxs2 = dxs << 1;
	sxd = (short)((xd2 - xd1) > 0 ? 1 : -1);
	sxs = (short)((xs2 - xs1) > 0 ? 1 : -1);
	
	for (dy = 0; dy <= dyd; dy++) {
		/* X stretch starts here */
		ex = dxs2 - dxd;
		xs = xs1;
		xd = xd1;
		sp = ys * srcStride;
		dp = yd * destStride;
		x = srcData[sp + xs] & 0xFF;
		pixel = (byte)(mapping == null ? x : mapping[x]);
		for (dx = 0; dx < dxd; dx++) {
			destData[dp + xd] = pixel;
			if (ex >= 0) {
				do {
					xs += sxs;
					ex -= dxd2;
				} while (ex >= 0);
				x = srcData[sp + xs] & 0xFF;
				pixel = (byte)(mapping == null ? x : mapping[x]);
			}
			xd += sxd;
			ex += dxs2;
		}
		destData[dp + xd] = pixel;
		/* X stretch ends here */
		if (dy == dyd)
			break;
		while (ey >= 0) {
			ys += sys;
			ey -= dyd2;
		}
		yd += syd;
		ey += dys2;
	}
}

}

