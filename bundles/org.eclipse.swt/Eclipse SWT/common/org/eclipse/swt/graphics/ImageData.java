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
	if (palette.isDirect) blit(BLIT_SRC,
		this.data, this.depth, this.bytesPerLine, MSB_FIRST, 0, 0, this.width, this.height, 0, 0, 0,
		ALPHA_OPAQUE, null, 0,
		dest.data, dest.depth, dest.bytesPerLine, MSB_FIRST, 0, 0, dest.width, dest.height, 0, 0, 0,
		flipX, flipY);
	else blit(BLIT_SRC,
		this.data, this.depth, this.bytesPerLine, MSB_FIRST, 0, 0, this.width, this.height, null, null, null,
		ALPHA_OPAQUE, null, 0,
		dest.data, dest.depth, dest.bytesPerLine, MSB_FIRST, 0, 0, dest.width, dest.height, null, null, null,
		flipX, flipY);
	
	/* Scale the image mask or alpha */
	if (maskData != null) {
		dest.maskPad = this.maskPad;
		int destBpl = (dest.width + 7) / 8;
		destBpl = (destBpl + (dest.maskPad - 1)) / dest.maskPad * dest.maskPad;
		dest.maskData = new byte[destBpl * dest.height];
		int srcBpl = (this.width + 7) / 8;
		srcBpl = (srcBpl + (this.maskPad - 1)) / this.maskPad * this.maskPad;
		blit(BLIT_SRC,
			this.maskData, 1, srcBpl, MSB_FIRST, 0, 0, this.width, this.height, null, null, null,
			ALPHA_OPAQUE, null, 0,
			dest.maskData, 1, destBpl, MSB_FIRST, 0, 0, dest.width, dest.height, null, null, null,
			flipX, flipY);
	} else if (alpha != -1) {
		dest.alpha = this.alpha;
	} else if (alphaData != null) {
		dest.alphaData = new byte[dest.width * dest.height];
		blit(BLIT_SRC,
			this.alphaData, 8, this.width, MSB_FIRST, 0, 0, this.width, this.height, null, null, null,
			ALPHA_OPAQUE, null, 0,
			dest.alphaData, 8, dest.width, MSB_FIRST, 0, 0, dest.width, dest.height, null, null, null,
			flipX, flipY);
	}
	return dest;
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
 * Blit operation bits to be OR'ed together to specify the desired operation.
 */
static final int
	BLIT_SRC = 1,     // copy source directly, else applies logic operations
	BLIT_ALPHA = 2,   // enable alpha blending
	BLIT_DITHER = 4;  // enable dithering in low color modes

/**
 * Alpha mode, values 0 - 255 specify global alpha level
 */
static final int
	ALPHA_OPAQUE = 255,           // Fully opaque (ignores any alpha data)
	ALPHA_TRANSPARENT = 0,        // Fully transparent (ignores any alpha data)
	ALPHA_CHANNEL_SEPARATE = -1,  // Use alpha channel from separate alphaData
	ALPHA_CHANNEL_SOURCE = -2,    // Use alpha channel embedded in sourceData
	ALPHA_MASK_UNPACKED = -3,     // Use transparency mask formed by bytes in alphaData (non-zero is opaque)
	ALPHA_MASK_PACKED = -4,       // Use transparency mask formed by packed bits in alphaData
	ALPHA_MASK_INDEX = -5,        // Consider source palette indices transparent if in alphaData array
	ALPHA_MASK_RGB = -6;          // Consider source RGBs transparent if in RGB888 format alphaData array

/**
 * Byte and bit order constants.
 */
static final int MSB_FIRST = 1;
static final int LSB_FIRST = 2;

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
	TYPE_INDEX_8 = 6,
	TYPE_INDEX_4 = 7,
	TYPE_INDEX_2 = 8,
	TYPE_INDEX_1_MSB = 9,
	TYPE_INDEX_1_LSB = 10;

/**
 * Blits a direct palette image into a direct palette image.
 */
static final void blit(int op,
	byte[] srcData, int srcDepth, int srcStride, int srcOrder,
	int srcX, int srcY, int srcWidth, int srcHeight,
	int srcRedMask, int srcGreenMask, int srcBlueMask,
	int alphaMode, byte[] alphaData, int alphaStride,
	byte[] destData, int destDepth, int destStride, int destOrder,
	int destX, int destY, int destWidth, int destHeight,
	int destRedMask, int destGreenMask, int destBlueMask,
	boolean flipX, boolean flipY) {
	if ((destWidth <= 0) || (destHeight <= 0) || (alphaMode == ALPHA_TRANSPARENT)) return;

	// these should be supplied as params later
	final int srcAlphaMask = 0, destAlphaMask = 0;

	/*** Prepare scaling data ***/
	final int dwm1 = destWidth - 1;
	final int sfxi = (dwm1 != 0) ? ((srcWidth << 16) - 1) / dwm1 : 0;
	final int dhm1 = destHeight - 1;
	final int sfyi = (dhm1 != 0) ? ((srcHeight << 16) - 1) / dhm1 : 0;

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
	int spr = srcY * srcStride + srcX * sbpp;

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
	int dpr = ((flipY) ? destY + dhm1 : destY) * destStride + ((flipX) ? destX + dwm1 : destX) * dbpp;
	final int dprxi = (flipX) ? -dbpp : dbpp;
	final int dpryi = (flipY) ? -destStride : destStride;

	/*** Prepare special processing data ***/
	int apr;
	if ((op & BLIT_ALPHA) != 0) {
		switch (alphaMode) {
			case ALPHA_MASK_UNPACKED:
			case ALPHA_CHANNEL_SEPARATE:
				apr = srcY * alphaStride + srcX;
				break;
			case ALPHA_MASK_PACKED:
				alphaStride <<= 3;
				apr = srcY * alphaStride + srcX;
				break;
			case ALPHA_MASK_INDEX:
				//throw new IllegalArgumentException("Invalid alpha type");
				return;
			default:
				alphaMode = (alphaMode << 16) / 255; // prescale
			case ALPHA_CHANNEL_SOURCE:
			case ALPHA_MASK_RGB:
				apr = 0;
				break;
		}
	} else {
		alphaMode = 0x10000;
		apr = 0;
	}

	/*** Blit ***/
	int dp = dpr;
	int sp = spr;
	if ((alphaMode == 0x10000) && (stype == dtype) &&
		(srcRedMask == destRedMask) && (srcGreenMask == destGreenMask) &&
		(srcBlueMask == destBlueMask) && (srcAlphaMask == destAlphaMask)) {
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
	/*** Comprehensive blit (apply transformations) ***/
	final int srcRedShift = getChannelShift(srcRedMask);
	final byte[] srcReds = anyToEight[getChannelWidth(srcRedMask, srcRedShift)];
	final int srcGreenShift = getChannelShift(srcGreenMask);
	final byte[] srcGreens = anyToEight[getChannelWidth(srcGreenMask, srcGreenShift)];
	final int srcBlueShift = getChannelShift(srcBlueMask);
	final byte[] srcBlues = anyToEight[getChannelWidth(srcBlueMask, srcBlueShift)];
	final int srcAlphaShift = getChannelShift(srcAlphaMask);
	final byte[] srcAlphas = anyToEight[getChannelWidth(srcAlphaMask, srcAlphaShift)];

	final int destRedShift = getChannelShift(destRedMask);
	final int destRedWidth = getChannelWidth(destRedMask, destRedShift);
	final byte[] destReds = anyToEight[destRedWidth];
	final int destRedPreShift = 8 - destRedWidth;
	final int destGreenShift = getChannelShift(destGreenMask);
	final int destGreenWidth = getChannelWidth(destGreenMask, destGreenShift);
	final byte[] destGreens = anyToEight[destGreenWidth];
	final int destGreenPreShift = 8 - destGreenWidth;
	final int destBlueShift = getChannelShift(destBlueMask);
	final int destBlueWidth = getChannelWidth(destBlueMask, destBlueShift);
	final byte[] destBlues = anyToEight[destBlueWidth];
	final int destBluePreShift = 8 - destBlueWidth;
	final int destAlphaShift = getChannelShift(destAlphaMask);
	final int destAlphaWidth = getChannelWidth(destAlphaMask, destAlphaShift);
	final byte[] destAlphas = anyToEight[destAlphaWidth];
	final int destAlphaPreShift = 8 - destAlphaWidth;

	int ap = apr, alpha = alphaMode;
	int r = 0, g = 0, b = 0, a = 0;
	int rq = 0, gq = 0, bq = 0, aq = 0;
	for (int dy = destHeight, sfy = sfyi; dy > 0; --dy,
			sp = spr += (sfy >>> 16) * srcStride,
			ap = apr += (sfy >>> 16) * alphaStride,
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
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
				case TYPE_GENERIC_16_MSB: {
					final int data = ((srcData[sp] & 0xff) << 8) | (srcData[sp + 1] & 0xff);
					sp += (sfx >>> 16) * 2;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
				case TYPE_GENERIC_16_LSB: {
					final int data = ((srcData[sp + 1] & 0xff) << 8) | (srcData[sp] & 0xff);
					sp += (sfx >>> 16) * 2;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
				case TYPE_GENERIC_24: {
					final int data = (( ((srcData[sp] & 0xff) << 8) |
						(srcData[sp + 1] & 0xff)) << 8) |
						(srcData[sp + 2] & 0xff);
					sp += (sfx >>> 16) * 3;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
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
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
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
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
			}

			/*** DO SPECIAL PROCESSING IF REQUIRED ***/
			switch (alphaMode) {
				case ALPHA_CHANNEL_SEPARATE:
					alpha = ((alphaData[ap] & 0xff) << 16) / 255;
					ap += (sfx >> 16);
					break;
				case ALPHA_CHANNEL_SOURCE:
					alpha = (a << 16) / 255;
					break;
				case ALPHA_MASK_UNPACKED:
					alpha = (alphaData[ap] != 0) ? 0x10000 : 0;
					ap += (sfx >> 16);
					break;						
				case ALPHA_MASK_PACKED:
					alpha = (alphaData[ap >> 3] << ((ap & 7) + 9)) & 0x10000;
					ap += (sfx >> 16);
					break;
				case ALPHA_MASK_RGB:
					alpha = 0x10000;
					for (int i = 0; i < alphaData.length; i += 3) {
						if ((r == alphaData[i]) && (g == alphaData[i + 1]) && (b == alphaData[i + 2])) {
							alpha = 0x0000;
							break;
						}
					}
					break;
			}
			if (alpha != 0x10000) {
				if (alpha == 0x0000) continue;
				switch (dtype) {
					case TYPE_GENERIC_8: {
						final int data = destData[dp] & 0xff;
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_16_MSB: {
						final int data = ((destData[dp] & 0xff) << 8) | (destData[dp + 1] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_16_LSB: {
						final int data = ((destData[dp + 1] & 0xff) << 8) | (destData[dp] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_24: {
						final int data = (( ((destData[dp] & 0xff) << 8) |
							(destData[dp + 1] & 0xff)) << 8) |
							(destData[dp + 2] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_32_MSB: {
						final int data = (( (( ((destData[dp] & 0xff) << 8) |
							(destData[dp + 1] & 0xff)) << 8) |
							(destData[dp + 2] & 0xff)) << 8) |
							(destData[dp + 3] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_32_LSB: {
						final int data = (( (( ((destData[dp + 3] & 0xff) << 8) |
							(destData[dp + 2] & 0xff)) << 8) |
							(destData[dp + 1] & 0xff)) << 8) |
							(destData[dp] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
				}
				// Perform alpha blending
				a = aq + ((a - aq) * alpha >> 16);
				r = rq + ((r - rq) * alpha >> 16);
				g = gq + ((g - gq) * alpha >> 16);
				b = bq + ((b - bq) * alpha >> 16);
			}

			/*** WRITE NEXT PIXEL ***/
			final int data = 
				(r >>> destRedPreShift << destRedShift) |
				(g >>> destGreenPreShift << destGreenShift) |
				(b >>> destBluePreShift << destBlueShift) |
				(a >>> destAlphaPreShift << destAlphaShift);
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
 */
static final void blit(int op,
	byte[] srcData, int srcDepth, int srcStride, int srcOrder,
	int srcX, int srcY, int srcWidth, int srcHeight,
	byte[] srcReds, byte[] srcGreens, byte[] srcBlues,
	int alphaMode, byte[] alphaData, int alphaStride,
	byte[] destData, int destDepth, int destStride, int destOrder,
	int destX, int destY, int destWidth, int destHeight,
	byte[] destReds, byte[] destGreens, byte[] destBlues,
	boolean flipX, boolean flipY) {
	if ((destWidth <= 0) || (destHeight <= 0) || (alphaMode == ALPHA_TRANSPARENT)) return;

	/*** Prepare scaling data ***/
	final int dwm1 = destWidth - 1;
	final int sfxi = (dwm1 != 0) ? ((srcWidth << 16) - 1) / dwm1 : 0;
	final int dhm1 = destHeight - 1;
	final int sfyi = (dhm1 != 0) ? ((srcHeight << 16) - 1) / dhm1 : 0;

	/*** Prepare source-related data ***/
	final int stype;
	switch (srcDepth) {
		case 8:
			stype = TYPE_INDEX_8;
			break;
		case 4:
			srcStride <<= 1;
			stype = TYPE_INDEX_4;
			break;
		case 2:
			srcStride <<= 2;
			stype = TYPE_INDEX_2;
			break;
		case 1:
			srcStride <<= 3;
			stype = (srcOrder == MSB_FIRST) ? TYPE_INDEX_1_MSB : TYPE_INDEX_1_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid source type");
			return;		
	}			
	int spr = srcY * srcStride + srcX;

	/*** Prepare destination-related data ***/
	final int dtype;
	switch (destDepth) {
		case 8:
			dtype = TYPE_INDEX_8;
			break;
		case 4:
			destStride <<= 1;
			dtype = TYPE_INDEX_4;
			break;
		case 2:
			destStride <<= 2;
			dtype = TYPE_INDEX_2;
			break;
		case 1:
			destStride <<= 3;
			dtype = (destOrder == MSB_FIRST) ? TYPE_INDEX_1_MSB : TYPE_INDEX_1_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid source type");
			return;
	}			
	int dpr = ((flipY) ? destY + dhm1 : destY) * destStride + ((flipX) ? destX + dwm1 : destX);
	final int dprxi = (flipX) ? -1 : 1;
	final int dpryi = (flipY) ? -destStride : destStride;

	/*** Prepare special processing data ***/
	int apr;
	if ((op & BLIT_ALPHA) != 0) {
		switch (alphaMode) {
			case ALPHA_MASK_UNPACKED:
			case ALPHA_CHANNEL_SEPARATE:
				apr = srcY * alphaStride + srcX;
				break;
			case ALPHA_MASK_PACKED:
				alphaStride <<= 3;
				apr = srcY * alphaStride + srcX;
				break;
			case ALPHA_CHANNEL_SOURCE:
				alphaMode = 0x10000;
				apr = 0;
				break;
			default:
				alphaMode = (alphaMode << 16) / 255; // prescale
			case ALPHA_MASK_RGB:
			case ALPHA_MASK_INDEX:
				apr = 0;
				break;
		}
	} else {
		alphaMode = 0x10000;
		apr = 0;
	}
	final boolean ditherEnabled = (op & BLIT_DITHER) != 0;

	/*** Blit ***/
	int dp = dpr;
	int sp = spr;
	int ap = apr;
	int destPaletteSize = 1 << destDepth;
	if ((destReds != null) && (destReds.length < destPaletteSize)) destPaletteSize = destReds.length;
	byte[] paletteMapping = null;
	boolean isExactPaletteMapping = true;
	switch (alphaMode) {
		case 0x10000:
			/*** If the palettes and formats are equivalent use a one-to-one mapping ***/
			if ((stype == dtype) &&
				(srcReds == destReds) && (srcGreens == destGreens) && (srcBlues == destBlues)) {
				paletteMapping = oneToOneMapping;
				break;
			}
		case ALPHA_MASK_UNPACKED:
		case ALPHA_MASK_PACKED:
		case ALPHA_MASK_INDEX:
		case ALPHA_MASK_RGB:
			/*** Generate a palette mapping ***/
			int srcPaletteSize = 1 << srcDepth;
			paletteMapping = new byte[srcPaletteSize];
			if ((srcReds != null) && (srcReds.length < srcPaletteSize)) srcPaletteSize = srcReds.length;
			for (int i = 0, r, g, b, index; i < srcPaletteSize; ++i) {
				r = srcReds[i] & 0xff;
				g = srcGreens[i] & 0xff;
				b = srcBlues[i] & 0xff;
				index = 0;
				int minDistance = 0x7fffffff;
				for (int j = 0, dr, dg, db, distance; j < destPaletteSize; ++j) {
					dr = (destReds[j] & 0xff) - r;
					dg = (destGreens[j] & 0xff) - g;
					db = (destBlues[j] & 0xff) - b;
					distance = dr * dr + dg * dg + db * db;
					if (distance < minDistance) {
						index = j;
						if (distance == 0) break;
						minDistance = distance;
					}
				}
				paletteMapping[i] = (byte)index;
				if (minDistance != 0) isExactPaletteMapping = false;
			}
			break;
	}
	if ((paletteMapping != null) && (isExactPaletteMapping || ! ditherEnabled)) {
		if ((stype == dtype) && (alphaMode == 0x10000)) {
			/*** Fast blit (copy w/ mapping) ***/
			switch (stype) {
				case TYPE_INDEX_8:
					for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
						for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
							destData[dp] = paletteMapping[srcData[sp] & 0xff];
							sp += (sfx >>> 16);
						}
					}
					break;					
				case TYPE_INDEX_4:
					for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
						for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
							final int v;
							if ((sp & 1) != 0) v = paletteMapping[srcData[sp >> 1] & 0x0f];
							else v = (srcData[sp >> 1] >>> 4) & 0x0f;
							sp += (sfx >>> 16);
							if ((dp & 1) != 0) destData[dp >> 1] = (byte)((destData[dp >> 1] & 0xf0) | v);
							else destData[dp >> 1] = (byte)((destData[dp >> 1] & 0x0f) | (v << 4));
						}
					}
					break;
				case TYPE_INDEX_2:
					for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
						for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
							final int index = paletteMapping[(srcData[sp >> 2] >>> (6 - (sp & 3) * 2)) & 0x03];
							sp += (sfx >>> 16);
							final int shift = 6 - (dp & 3) * 2;
							destData[dp >> 2] = (byte)(destData[dp >> 2] & ~(0x03 << shift) | (index << shift));
						}
					}
					break;					
				case TYPE_INDEX_1_MSB:
					for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
						for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
							final int index = paletteMapping[(srcData[sp >> 3] >>> (7 - (sp & 7))) & 0x01];
							sp += (sfx >>> 16);
							final int shift = 7 - (dp & 7);
							destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (index << shift));
						}
					}
					break;					
				case TYPE_INDEX_1_LSB:
					for (int dy = destHeight, sfy = sfyi; dy > 0; --dy, sp = spr += (sfy >>> 16) * srcStride, sfy = (sfy & 0xffff) + sfyi, dp = dpr += dpryi) {
						for (int dx = destWidth, sfx = sfxi; dx > 0; --dx, dp += dprxi, sfx = (sfx & 0xffff) + sfxi) {
							final int index = paletteMapping[(srcData[sp >> 3] >>> (sp & 7)) & 0x01];
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
					sp = spr += (sfy >>> 16) * srcStride,
					sfy = (sfy & 0xffff) + sfyi,
					dp = dpr += dpryi) {
				for (int dx = destWidth, sfx = sfxi; dx > 0; --dx,
						dp += dprxi,
						sfx = (sfx & 0xffff) + sfxi) {
					int index;
					/*** READ NEXT PIXEL ***/
					switch (stype) {
						case TYPE_INDEX_8:
							index = srcData[sp] & 0xff;
							sp += (sfx >>> 16);
							break;					
						case TYPE_INDEX_4:
							if ((sp & 1) != 0) index = srcData[sp >> 1] & 0x0f;
							else index = (srcData[sp >> 1] >>> 4) & 0x0f;
							sp += (sfx >>> 16);
							break;					
						case TYPE_INDEX_2:
							index = (srcData[sp >> 2] >>> (6 - (sp & 3) * 2)) & 0x03;
							sp += (sfx >>> 16);
							break;					
						case TYPE_INDEX_1_MSB:
							index = (srcData[sp >> 3] >>> (7 - (sp & 7))) & 0x01;
							sp += (sfx >>> 16);
							break;					
						case TYPE_INDEX_1_LSB:
							index = (srcData[sp >> 3] >>> (sp & 7)) & 0x01;
							sp += (sfx >>> 16);
							break;
						default:
							return;
					}
					/*** APPLY MASK ***/
					switch (alphaMode) {
						case ALPHA_MASK_UNPACKED: {
							final byte mask = alphaData[ap];
							ap += (sfx >> 16);
							if (mask == 0) continue;
						} break;
						case ALPHA_MASK_PACKED: {
							final int mask = alphaData[ap >> 3] & (1 << (ap & 7));
							ap += (sfx >> 16);
							if (mask == 0) continue;
						} break;
						case ALPHA_MASK_INDEX: {
							int i = 0;
							while (i < alphaData.length) {
								if (index == (alphaData[i] & 0xff)) break;
							}
							if (i < alphaData.length) continue;
						} break;
						case ALPHA_MASK_RGB: {
							final byte r = srcReds[index], g = srcGreens[index], b = srcBlues[index];
							int i = 0;
							while (i < alphaData.length) {
								if ((r == alphaData[i]) && (g == alphaData[i + 1]) && (b == alphaData[i + 2])) break;
								i += 3;
							}
							if (i < alphaData.length) continue;
						} break;
					}
					index = paletteMapping[index] & 0xff;
			
					/*** WRITE NEXT PIXEL ***/
					switch (dtype) {
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
		return;
	}
		
	/*** Comprehensive blit (apply transformations) ***/
	int alpha = alphaMode;
	int index = 0;
	int indexq = 0;
	int lastindex = 0, lastr = -1, lastg = -1, lastb = -1;
	final int[] rerr, gerr, berr;
	if (ditherEnabled) {
		rerr = new int[destWidth + 2];
		gerr = new int[destWidth + 2];
		berr = new int[destWidth + 2];
	} else {
		rerr = null; gerr = null; berr = null;
	}
	for (int dy = destHeight, sfy = sfyi; dy > 0; --dy,
			sp = spr += (sfy >>> 16) * srcStride,
			ap = apr += (sfy >>> 16) * alphaStride,
			sfy = (sfy & 0xffff) + sfyi,
			dp = dpr += dpryi) {
		int lrerr = 0, lgerr = 0, lberr = 0;
		for (int dx = destWidth, sfx = sfxi; dx > 0; --dx,
				dp += dprxi,
				sfx = (sfx & 0xffff) + sfxi) {
			/*** READ NEXT PIXEL ***/
			switch (stype) {
				case TYPE_INDEX_8:
					index = srcData[sp] & 0xff;
					sp += (sfx >>> 16);
					break;
				case TYPE_INDEX_4:
					if ((sp & 1) != 0) index = srcData[sp >> 1] & 0x0f;
					else index = (srcData[sp >> 1] >>> 4) & 0x0f;
					sp += (sfx >>> 16);
					break;
				case TYPE_INDEX_2:
					index = (srcData[sp >> 2] >>> (6 - (sp & 3) * 2)) & 0x03;
					sp += (sfx >>> 16);
					break;
				case TYPE_INDEX_1_MSB:
					index = (srcData[sp >> 3] >>> (7 - (sp & 7))) & 0x01;
					sp += (sfx >>> 16);
					break;
				case TYPE_INDEX_1_LSB:
					index = (srcData[sp >> 3] >>> (sp & 7)) & 0x01;
					sp += (sfx >>> 16);
					break;
			}

			/*** DO SPECIAL PROCESSING IF REQUIRED ***/
			int r = srcReds[index] & 0xff, g = srcGreens[index] & 0xff, b = srcBlues[index] & 0xff;
			switch (alphaMode) {
				case ALPHA_CHANNEL_SEPARATE:
					alpha = ((alphaData[ap] & 0xff) << 16) / 255;
					ap += (sfx >> 16);
					break;
				case ALPHA_MASK_UNPACKED:
					alpha = (alphaData[ap] != 0) ? 0x10000 : 0;
					ap += (sfx >> 16);
					break;						
				case ALPHA_MASK_PACKED:
					alpha = (alphaData[ap >> 3] << ((ap & 7) + 9)) & 0x10000;
					ap += (sfx >> 16);
					break;
				case ALPHA_MASK_INDEX: { // could speed up using binary search if we sorted the indices
					int i = 0;
					while (i < alphaData.length) {
						if (index == (alphaData[i] & 0xff)) break;
					}
					if (i < alphaData.length) continue;
				} break;
				case ALPHA_MASK_RGB: {
					int i = 0;
					while (i < alphaData.length) {
						if ((r == (alphaData[i] & 0xff)) &&
							(g == (alphaData[i + 1] & 0xff)) &&
							(b == (alphaData[i + 2] & 0xff))) break;
						i += 3;
					}
					if (i < alphaData.length) continue;
				} break;
			}
			if (alpha != 0x10000) {
				if (alpha == 0x0000) continue;
				switch (dtype) {
					case TYPE_INDEX_8:
						indexq = destData[dp] & 0xff;
						break;
					case TYPE_INDEX_4:
						if ((dp & 1) != 0) indexq = destData[dp >> 1] & 0x0f;
						else indexq = (destData[dp >> 1] >>> 4) & 0x0f;
						break;
					case TYPE_INDEX_2:
						indexq = (destData[dp >> 2] >>> (6 - (dp & 3) * 2)) & 0x03;
						break;
					case TYPE_INDEX_1_MSB:
						indexq = (destData[dp >> 3] >>> (7 - (dp & 7))) & 0x01;
						break;
					case TYPE_INDEX_1_LSB:
						indexq = (destData[dp >> 3] >>> (dp & 7)) & 0x01;
						break;
				}
				// Perform alpha blending
				final int rq = destReds[indexq] & 0xff;
				final int gq = destGreens[indexq] & 0xff;
				final int bq = destBlues[indexq] & 0xff;
				r = rq + ((r - rq) * alpha >> 16);
				g = gq + ((g - gq) * alpha >> 16);
				b = bq + ((b - bq) * alpha >> 16);
			}

			/*** MAP COLOR TO THE PALETTE ***/
			if (ditherEnabled) {
				// Floyd-Steinberg error diffusion
				r += rerr[dx] >> 4;
				if (r < 0) r = 0; else if (r > 255) r = 255;
				g += gerr[dx] >> 4;
				if (g < 0) g = 0; else if (g > 255) g = 255;
				b += berr[dx] >> 4;
				if (b < 0) b = 0; else if (b > 255) b = 255;
				rerr[dx] = lrerr;
				gerr[dx] = lgerr;
				berr[dx] = lberr;
			}
			if (r != lastr || g != lastg || b != lastb) {
				// moving the variable declarations out seems to make the JDK JIT happier...
				for (int j = 0, dr, dg, db, distance, minDistance = 0x7fffffff; j < destPaletteSize; ++j) {
					dr = (destReds[j] & 0xff) - r;
					dg = (destGreens[j] & 0xff) - g;
					db = (destBlues[j] & 0xff) - b;
					distance = dr * dr + dg * dg + db * db;
					if (distance < minDistance) {
						lastindex = j;
						if (distance == 0) break;
						minDistance = distance;
					}
				}
				lastr = r; lastg = g; lastb = b;
			}
			if (ditherEnabled) {
				// Floyd-Steinberg error diffusion, cont'd...
				final int dxm1 = dx - 1, dxp1 = dx + 1;
				int acc;
				rerr[dxp1] += acc = (lrerr = r - (destReds[lastindex] & 0xff)) + lrerr + lrerr;
				rerr[dx] += acc += lrerr + lrerr;
				rerr[dxm1] += acc + lrerr + lrerr;
				gerr[dxp1] += acc = (lgerr = g - (destGreens[lastindex] & 0xff)) + lgerr + lgerr;
				gerr[dx] += acc += lgerr + lgerr;
				gerr[dxm1] += acc + lgerr + lgerr;
				berr[dxp1] += acc = (lberr = b - (destBlues[lastindex] & 0xff)) + lberr + lberr;
				berr[dx] += acc += lberr + lberr;
				berr[dxm1] += acc + lberr + lberr;
			}

			/*** WRITE NEXT PIXEL ***/
			switch (dtype) {
				case TYPE_INDEX_8:
					destData[dp] = (byte) lastindex;
					break;
				case TYPE_INDEX_4:
					if ((dp & 1) != 0) destData[dp >> 1] = (byte)((destData[dp >> 1] & 0xf0) | lastindex);
					else destData[dp >> 1] = (byte)((destData[dp >> 1] & 0x0f) | (lastindex << 4));
					break;
				case TYPE_INDEX_2: {
					final int shift = 6 - (dp & 3) * 2;
					destData[dp >> 2] = (byte)(destData[dp >> 2] & ~(0x03 << shift) | (lastindex << shift));
				} break;					
				case TYPE_INDEX_1_MSB: {
					final int shift = 7 - (dp & 7);
					destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (lastindex << shift));
				} break;
				case TYPE_INDEX_1_LSB: {
					final int shift = dp & 7;
					destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (lastindex << shift));
				} break;					
			}
		}
	}
}

/**
 * Blits an index palette image into a direct palette image.
 */
static final void blit(int op,
	byte[] srcData, int srcDepth, int srcStride, int srcOrder,
	int srcX, int srcY, int srcWidth, int srcHeight,
	byte[] srcReds, byte[] srcGreens, byte[] srcBlues,
	int alphaMode, byte[] alphaData, int alphaStride,
	byte[] destData, int destDepth, int destStride, int destOrder,
	int destX, int destY, int destWidth, int destHeight,
	int destRedMask, int destGreenMask, int destBlueMask,
	boolean flipX, boolean flipY) {
	if ((destWidth <= 0) || (destHeight <= 0) || (alphaMode == ALPHA_TRANSPARENT)) return;

	// these should be supplied as params later
	final int destAlphaMask = 0;

	/*** Prepare scaling data ***/
	final int dwm1 = destWidth - 1;
	final int sfxi = (dwm1 != 0) ? ((srcWidth << 16) - 1) / dwm1 : 0;
	final int dhm1 = destHeight - 1;
	final int sfyi = (dhm1 != 0) ? ((srcHeight << 16) - 1) / dhm1 : 0;

	/*** Prepare source-related data ***/
	final int stype;
	switch (srcDepth) {
		case 8:
			stype = TYPE_INDEX_8;
			break;
		case 4:
			srcStride <<= 1;
			stype = TYPE_INDEX_4;
			break;
		case 2:
			srcStride <<= 2;
			stype = TYPE_INDEX_2;
			break;
		case 1:
			srcStride <<= 3;
			stype = (srcOrder == MSB_FIRST) ? TYPE_INDEX_1_MSB : TYPE_INDEX_1_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid source type");
			return;
	}			
	int spr = srcY * srcStride + srcX;

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
	int dpr = ((flipY) ? destY + dhm1 : destY) * destStride + ((flipX) ? destX + dwm1 : destX) * dbpp;
	final int dprxi = (flipX) ? -dbpp : dbpp;
	final int dpryi = (flipY) ? -destStride : destStride;

	/*** Prepare special processing data ***/
	int apr;
	if ((op & BLIT_ALPHA) != 0) {
		switch (alphaMode) {
			case ALPHA_MASK_UNPACKED:
			case ALPHA_CHANNEL_SEPARATE:
				apr = srcY * alphaStride + srcX;
				break;
			case ALPHA_MASK_PACKED:
				alphaStride <<= 3;
				apr = srcY * alphaStride + srcX;
				break;
			case ALPHA_CHANNEL_SOURCE:
				alphaMode = 0x10000;
				apr = 0;
				break;
			default:
				alphaMode = (alphaMode << 16) / 255; // prescale
			case ALPHA_MASK_RGB:
			case ALPHA_MASK_INDEX:
				apr = 0;
				break;
		}
	} else {
		alphaMode = 0x10000;
		apr = 0;
	}

	/*** Comprehensive blit (apply transformations) ***/
	final int destRedShift = getChannelShift(destRedMask);
	final int destRedWidth = getChannelWidth(destRedMask, destRedShift);
	final byte[] destReds = anyToEight[destRedWidth];
	final int destRedPreShift = 8 - destRedWidth;
	final int destGreenShift = getChannelShift(destGreenMask);
	final int destGreenWidth = getChannelWidth(destGreenMask, destGreenShift);
	final byte[] destGreens = anyToEight[destGreenWidth];
	final int destGreenPreShift = 8 - destGreenWidth;
	final int destBlueShift = getChannelShift(destBlueMask);
	final int destBlueWidth = getChannelWidth(destBlueMask, destBlueShift);
	final byte[] destBlues = anyToEight[destBlueWidth];
	final int destBluePreShift = 8 - destBlueWidth;
	final int destAlphaShift = getChannelShift(destAlphaMask);
	final int destAlphaWidth = getChannelWidth(destAlphaMask, destAlphaShift);
	final byte[] destAlphas = anyToEight[destAlphaWidth];
	final int destAlphaPreShift = 8 - destAlphaWidth;

	int dp = dpr;
	int sp = spr;
	int ap = apr, alpha = alphaMode;
	int r = 0, g = 0, b = 0, a = 0, index = 0;
	int rq = 0, gq = 0, bq = 0, aq = 0;
	for (int dy = destHeight, sfy = sfyi; dy > 0; --dy,
			sp = spr += (sfy >>> 16) * srcStride,
			ap = apr += (sfy >>> 16) * alphaStride,
			sfy = (sfy & 0xffff) + sfyi,
			dp = dpr += dpryi) {
		int lrerr = 0, lgerr = 0, lberr = 0;
		for (int dx = destWidth, sfx = sfxi; dx > 0; --dx,
				dp += dprxi,
				sfx = (sfx & 0xffff) + sfxi) {
			/*** READ NEXT PIXEL ***/
			switch (stype) {
				case TYPE_INDEX_8:
					index = srcData[sp] & 0xff;
					sp += (sfx >>> 16);
					break;
				case TYPE_INDEX_4:
					if ((sp & 1) != 0) index = srcData[sp >> 1] & 0x0f;
					else index = (srcData[sp >> 1] >>> 4) & 0x0f;
					sp += (sfx >>> 16);
					break;
				case TYPE_INDEX_2:
					index = (srcData[sp >> 2] >>> (6 - (sp & 3) * 2)) & 0x03;
					sp += (sfx >>> 16);
					break;
				case TYPE_INDEX_1_MSB:
					index = (srcData[sp >> 3] >>> (7 - (sp & 7))) & 0x01;
					sp += (sfx >>> 16);
					break;
				case TYPE_INDEX_1_LSB:
					index = (srcData[sp >> 3] >>> (sp & 7)) & 0x01;
					sp += (sfx >>> 16);
					break;
			}

			/*** DO SPECIAL PROCESSING IF REQUIRED ***/
			r = srcReds[index] & 0xff;
			g = srcGreens[index] & 0xff;
			b = srcBlues[index] & 0xff;
			switch (alphaMode) {
				case ALPHA_CHANNEL_SEPARATE:
					alpha = ((alphaData[ap] & 0xff) << 16) / 255;
					ap += (sfx >> 16);
					break;
				case ALPHA_MASK_UNPACKED:
					alpha = (alphaData[ap] != 0) ? 0x10000 : 0;
					ap += (sfx >> 16);
					break;						
				case ALPHA_MASK_PACKED:
					alpha = (alphaData[ap >> 3] << ((ap & 7) + 9)) & 0x10000;
					ap += (sfx >> 16);
					break;
				case ALPHA_MASK_INDEX: { // could speed up using binary search if we sorted the indices
					int i = 0;
					while (i < alphaData.length) {
						if (index == (alphaData[i] & 0xff)) break;
					}
					if (i < alphaData.length) continue;
				} break;
				case ALPHA_MASK_RGB: {
					int i = 0;
					while (i < alphaData.length) {
						if ((r == (alphaData[i] & 0xff)) &&
							(g == (alphaData[i + 1] & 0xff)) &&
							(b == (alphaData[i + 2] & 0xff))) break;
						i += 3;
					}
					if (i < alphaData.length) continue;
				} break;
			}
			if (alpha != 0x10000) {
				if (alpha == 0x0000) continue;
				switch (dtype) {
					case TYPE_GENERIC_8: {
						final int data = destData[dp] & 0xff;
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_16_MSB: {
						final int data = ((destData[dp] & 0xff) << 8) | (destData[dp + 1] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_16_LSB: {
						final int data = ((destData[dp + 1] & 0xff) << 8) | (destData[dp] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_24: {
						final int data = (( ((destData[dp] & 0xff) << 8) |
							(destData[dp + 1] & 0xff)) << 8) |
							(destData[dp + 2] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_32_MSB: {
						final int data = (( (( ((destData[dp] & 0xff) << 8) |
							(destData[dp + 1] & 0xff)) << 8) |
							(destData[dp + 2] & 0xff)) << 8) |
							(destData[dp + 3] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
					case TYPE_GENERIC_32_LSB: {
						final int data = (( (( ((destData[dp + 3] & 0xff) << 8) |
							(destData[dp + 2] & 0xff)) << 8) |
							(destData[dp + 1] & 0xff)) << 8) |
							(destData[dp] & 0xff);
						rq = destReds[(data & destRedMask) >>> destRedShift] & 0xff;
						gq = destGreens[(data & destGreenMask) >>> destGreenShift] & 0xff;
						bq = destBlues[(data & destBlueMask) >>> destBlueShift] & 0xff;
						aq = destAlphas[(data & destAlphaMask) >>> destAlphaShift] & 0xff;
					} break;
				}
				// Perform alpha blending
				a = aq + ((a - aq) * alpha >> 16);
				r = rq + ((r - rq) * alpha >> 16);
				g = gq + ((g - gq) * alpha >> 16);
				b = bq + ((b - bq) * alpha >> 16);
			}

			/*** WRITE NEXT PIXEL ***/
			final int data = 
				(r >>> destRedPreShift << destRedShift) |
				(g >>> destGreenPreShift << destGreenShift) |
				(b >>> destBluePreShift << destBlueShift) |
				(a >>> destAlphaPreShift << destAlphaShift);
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
 * Blits a direct palette image into an index palette image.
 */
static final void blit(int op,
	byte[] srcData, int srcDepth, int srcStride, int srcOrder,
	int srcX, int srcY, int srcWidth, int srcHeight,
	int srcRedMask, int srcGreenMask, int srcBlueMask,
	int alphaMode, byte[] alphaData, int alphaStride,
	byte[] destData, int destDepth, int destStride, int destOrder,
	int destX, int destY, int destWidth, int destHeight,
	byte[] destReds, byte[] destGreens, byte[] destBlues,
	boolean flipX, boolean flipY) {
	if ((destWidth <= 0) || (destHeight <= 0) || (alphaMode == ALPHA_TRANSPARENT)) return;

	// these should be supplied as params later
	final int srcAlphaMask = 0;

	/*** Prepare scaling data ***/
	final int dwm1 = destWidth - 1;
	final int sfxi = (dwm1 != 0) ? ((srcWidth << 16) - 1) / dwm1 : 0;
	final int dhm1 = destHeight - 1;
	final int sfyi = (dhm1 != 0) ? ((srcHeight << 16) - 1) / dhm1 : 0;

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
	int spr = srcY * srcStride + srcX * sbpp;

	/*** Prepare destination-related data ***/
	final int dtype;
	switch (destDepth) {
		case 8:
			dtype = TYPE_INDEX_8;
			break;
		case 4:
			destStride <<= 1;
			dtype = TYPE_INDEX_4;
			break;
		case 2:
			destStride <<= 2;
			dtype = TYPE_INDEX_2;
			break;
		case 1:
			destStride <<= 3;
			dtype = (destOrder == MSB_FIRST) ? TYPE_INDEX_1_MSB : TYPE_INDEX_1_LSB;
			break;
		default:
			//throw new IllegalArgumentException("Invalid source type");
			return;
	}			
	int dpr = ((flipY) ? destY + dhm1 : destY) * destStride + ((flipX) ? destX + dwm1 : destX);
	final int dprxi = (flipX) ? -1 : 1;
	final int dpryi = (flipY) ? -destStride : destStride;

	/*** Prepare special processing data ***/
	int apr;
	if ((op & BLIT_ALPHA) != 0) {
		switch (alphaMode) {
			case ALPHA_MASK_UNPACKED:
			case ALPHA_CHANNEL_SEPARATE:
				apr = srcY * alphaStride + srcX;
				break;
			case ALPHA_MASK_PACKED:
				alphaStride <<= 3;
				apr = srcY * alphaStride + srcX;
				break;
			case ALPHA_CHANNEL_SOURCE:
				alphaMode = 0x10000;
				apr = 0;
				break;
			default:
				alphaMode = (alphaMode << 16) / 255; // prescale
			case ALPHA_MASK_RGB:
			case ALPHA_MASK_INDEX:
				apr = 0;
				break;
		}
	} else {
		alphaMode = 0x10000;
		apr = 0;
	}
	final boolean ditherEnabled = (op & BLIT_DITHER) != 0;

	/*** Comprehensive blit (apply transformations) ***/
	final int srcRedShift = getChannelShift(srcRedMask);
	final byte[] srcReds = anyToEight[getChannelWidth(srcRedMask, srcRedShift)];
	final int srcGreenShift = getChannelShift(srcGreenMask);
	final byte[] srcGreens = anyToEight[getChannelWidth(srcGreenMask, srcGreenShift)];
	final int srcBlueShift = getChannelShift(srcBlueMask);
	final byte[] srcBlues = anyToEight[getChannelWidth(srcBlueMask, srcBlueShift)];
	final int srcAlphaShift = getChannelShift(srcAlphaMask);
	final byte[] srcAlphas = anyToEight[getChannelWidth(srcAlphaMask, srcAlphaShift)];

	int dp = dpr;
	int sp = spr;
	int ap = apr, alpha = alphaMode;
	int r = 0, g = 0, b = 0, a = 0;
	int indexq = 0;
	int lastindex = 0, lastr = -1, lastg = -1, lastb = -1;
	final int[] rerr, gerr, berr;
	int destPaletteSize = 1 << destDepth;
	if ((destReds != null) && (destReds.length < destPaletteSize)) destPaletteSize = destReds.length;
	if (ditherEnabled) {
		rerr = new int[destWidth + 2];
		gerr = new int[destWidth + 2];
		berr = new int[destWidth + 2];
	} else {
		rerr = null; gerr = null; berr = null;
	}
	for (int dy = destHeight, sfy = sfyi; dy > 0; --dy,
			sp = spr += (sfy >>> 16) * srcStride,
			ap = apr += (sfy >>> 16) * alphaStride,
			sfy = (sfy & 0xffff) + sfyi,
			dp = dpr += dpryi) {
		int lrerr = 0, lgerr = 0, lberr = 0;
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
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
				case TYPE_GENERIC_16_MSB: {
					final int data = ((srcData[sp] & 0xff) << 8) | (srcData[sp + 1] & 0xff);
					sp += (sfx >>> 16) * 2;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
				case TYPE_GENERIC_16_LSB: {
					final int data = ((srcData[sp + 1] & 0xff) << 8) | (srcData[sp] & 0xff);
					sp += (sfx >>> 16) * 2;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
				case TYPE_GENERIC_24: {
					final int data = (( ((srcData[sp] & 0xff) << 8) |
						(srcData[sp + 1] & 0xff)) << 8) |
						(srcData[sp + 2] & 0xff);
					sp += (sfx >>> 16) * 3;
					r = srcReds[(data & srcRedMask) >>> srcRedShift] & 0xff;
					g = srcGreens[(data & srcGreenMask) >>> srcGreenShift] & 0xff;
					b = srcBlues[(data & srcBlueMask) >>> srcBlueShift] & 0xff;
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
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
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
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
					a = srcAlphas[(data & srcAlphaMask) >>> srcAlphaShift] & 0xff;
				} break;
			}

			/*** DO SPECIAL PROCESSING IF REQUIRED ***/
			switch (alphaMode) {
				case ALPHA_CHANNEL_SEPARATE:
					alpha = ((alphaData[ap] & 0xff) << 16) / 255;
					ap += (sfx >> 16);
					break;
				case ALPHA_CHANNEL_SOURCE:
					alpha = (a << 16) / 255;
					break;
				case ALPHA_MASK_UNPACKED:
					alpha = (alphaData[ap] != 0) ? 0x10000 : 0;
					ap += (sfx >> 16);
					break;						
				case ALPHA_MASK_PACKED:
					alpha = (alphaData[ap >> 3] << ((ap & 7) + 9)) & 0x10000;
					ap += (sfx >> 16);
					break;
				case ALPHA_MASK_RGB:
					alpha = 0x10000;
					for (int i = 0; i < alphaData.length; i += 3) {
						if ((r == alphaData[i]) && (g == alphaData[i + 1]) && (b == alphaData[i + 2])) {
							alpha = 0x0000;
							break;
						}
					}
					break;
			}
			if (alpha != 0x10000) {
				if (alpha == 0x0000) continue;
				switch (dtype) {
					case TYPE_INDEX_8:
						indexq = destData[dp] & 0xff;
						break;
					case TYPE_INDEX_4:
						if ((dp & 1) != 0) indexq = destData[dp >> 1] & 0x0f;
						else indexq = (destData[dp >> 1] >>> 4) & 0x0f;
						break;
					case TYPE_INDEX_2:
						indexq = (destData[dp >> 2] >>> (6 - (dp & 3) * 2)) & 0x03;
						break;
					case TYPE_INDEX_1_MSB:
						indexq = (destData[dp >> 3] >>> (7 - (dp & 7))) & 0x01;
						break;
					case TYPE_INDEX_1_LSB:
						indexq = (destData[dp >> 3] >>> (dp & 7)) & 0x01;
						break;
				}
				// Perform alpha blending
				final int rq = destReds[indexq] & 0xff;
				final int gq = destGreens[indexq] & 0xff;
				final int bq = destBlues[indexq] & 0xff;
				r = rq + ((r - rq) * alpha >> 16);
				g = gq + ((g - gq) * alpha >> 16);
				b = bq + ((b - bq) * alpha >> 16);
			}

			/*** MAP COLOR TO THE PALETTE ***/
			if (ditherEnabled) {
				// Floyd-Steinberg error diffusion
				r += rerr[dx] >> 4;
				if (r < 0) r = 0; else if (r > 255) r = 255;
				g += gerr[dx] >> 4;
				if (g < 0) g = 0; else if (g > 255) g = 255;
				b += berr[dx] >> 4;
				if (b < 0) b = 0; else if (b > 255) b = 255;
				rerr[dx] = lrerr;
				gerr[dx] = lgerr;
				berr[dx] = lberr;
			}
			if (r != lastr || g != lastg || b != lastb) {
				// moving the variable declarations out seems to make the JDK JIT happier...
				for (int j = 0, dr, dg, db, distance, minDistance = 0x7fffffff; j < destPaletteSize; ++j) {
					dr = (destReds[j] & 0xff) - r;
					dg = (destGreens[j] & 0xff) - g;
					db = (destBlues[j] & 0xff) - b;
					distance = dr * dr + dg * dg + db * db;
					if (distance < minDistance) {
						lastindex = j;
						if (distance == 0) break;
						minDistance = distance;
					}
				}
				lastr = r; lastg = g; lastb = b;
			}
			if (ditherEnabled) {
				// Floyd-Steinberg error diffusion, cont'd...
				final int dxm1 = dx - 1, dxp1 = dx + 1;
				int acc;
				rerr[dxp1] += acc = (lrerr = r - (destReds[lastindex] & 0xff)) + lrerr + lrerr;
				rerr[dx] += acc += lrerr + lrerr;
				rerr[dxm1] += acc + lrerr + lrerr;
				gerr[dxp1] += acc = (lgerr = g - (destGreens[lastindex] & 0xff)) + lgerr + lgerr;
				gerr[dx] += acc += lgerr + lgerr;
				gerr[dxm1] += acc + lgerr + lgerr;
				berr[dxp1] += acc = (lberr = b - (destBlues[lastindex] & 0xff)) + lberr + lberr;
				berr[dx] += acc += lberr + lberr;
				berr[dxm1] += acc + lberr + lberr;
			}

			/*** WRITE NEXT PIXEL ***/
			switch (dtype) {
				case TYPE_INDEX_8:
					destData[dp] = (byte) lastindex;
					break;
				case TYPE_INDEX_4:
					if ((dp & 1) != 0) destData[dp >> 1] = (byte)((destData[dp >> 1] & 0xf0) | lastindex);
					else destData[dp >> 1] = (byte)((destData[dp >> 1] & 0x0f) | (lastindex << 4));
					break;
				case TYPE_INDEX_2: {
					final int shift = 6 - (dp & 3) * 2;
					destData[dp >> 2] = (byte)(destData[dp >> 2] & ~(0x03 << shift) | (lastindex << shift));
				} break;					
				case TYPE_INDEX_1_MSB: {
					final int shift = 7 - (dp & 7);
					destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (lastindex << shift));
				} break;
				case TYPE_INDEX_1_LSB: {
					final int shift = dp & 7;
					destData[dp >> 3] = (byte)(destData[dp >> 3] & ~(0x01 << shift) | (lastindex << shift));
				} break;					
			}
		}
	}
}

/**
 * Computes the required channel shift from a mask.
 */
private final static int getChannelShift(int mask) {
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
private final static int getChannelWidth(int mask, int shift) {
	if (mask == 0) return 0;
	int i;
	mask >>>= shift;
	for (i = shift; ((mask & 1) != 0) && (i < 32); ++i) {
		mask >>>= 1;
	}
	return i - shift;
}

/**
 * Extracts a field from packed RGB data given a mask for that field.
 */
private final static byte getChannelField(int data, int mask) {
	final int shift = getChannelShift(mask);
	return anyToEight[getChannelWidth(mask, shift)][(data & mask) >>> shift];
}

/**
 * Arbitrary channel width data to 8-bit conversion table
 */
private static final byte[][] anyToEight = new byte[9][];
static {
	for (int b = 0; b < 9; ++b) {
		byte[] data = anyToEight[b] = new byte[1 << b];
		if (b == 0) continue;
		int inc = 0;
		for (int bit = 0x10000; (bit >>= b) != 0;) inc |= bit;
		for (int v = 0, p = 0; v < 0x10000; v+= inc) data[p++] = (byte)(v >> 8);
	}
}
private static final byte[] oneToOneMapping = anyToEight[8];

/**
 * Stretches the source, a 32-bit image, into the destination, a 32-bit image.
 * The images must have the same red, green, and blue masks.
 */
static void stretch32(
	byte[] srcData, int srcStride,
	int srcX, int srcY, int srcWidth, int srcHeight,
	byte[] destData, int destStride,
	int destX, int destY, int destWidth, int destHeight,
	boolean flipX, boolean flipY) {
	blit(BLIT_SRC,
		srcData, 32, srcStride, MSB_FIRST, srcX, srcY, srcWidth, srcHeight, 0, 0, 0,
		ALPHA_OPAQUE, null, 0,
		destData, 32, destStride, MSB_FIRST, destX, destY, destWidth, destHeight, 0, 0, 0,
		flipX, flipY);
}

/**
 * Stretches the source, a 24-bit image, into the destination, a 24-bit image.
 * The images must have the same red, green, and blue masks.
 * The image are assumed to have 24 bits per pixel; many 24-bit images
 * use 32 bits per pixel.
 */
static void stretch24(
	byte[] srcData, int srcStride,
	int srcX, int srcY, int srcWidth, int srcHeight,
	byte[] destData, int destStride,
	int destX, int destY, int destWidth, int destHeight,
	boolean flipX, boolean flipY) {
	blit(BLIT_SRC,
		srcData, 24, srcStride, MSB_FIRST, srcX, srcY, srcWidth, srcHeight, 0, 0, 0,
		ALPHA_OPAQUE, null, 0,
		destData, 24, destStride, MSB_FIRST, destX, destY, destWidth, destHeight, 0, 0, 0,
		flipX, flipY);
}

/**
 * Stretches the source, a 16-bit image, into the destination, a 16-bit image.
 * The images are assumed to have the same red, green, and blue masks.
 */
static void stretch16(
	byte[] srcData, int srcStride,
	int srcX, int srcY, int srcWidth, int srcHeight,
	byte[] destData, int destStride,
	int destX, int destY, int destWidth, int destHeight,
	boolean flipX, boolean flipY) {
	blit(BLIT_SRC,
		srcData, 16, srcStride, MSB_FIRST, srcX, srcY, srcWidth, srcHeight, 0, 0, 0,
		ALPHA_OPAQUE, null, 0,
		destData, 16, destStride, MSB_FIRST, destX, destY, destWidth, destHeight, 0, 0, 0,
		flipX, flipY);
}

/**
 * Stretches the source, an 8-bit image, into the destination, an 8-bit image.
 * NOTE: Palette mapping ignored (for now)
 */
static void stretch8(
	byte[] srcData, int srcStride,
	int srcX, int srcY, int srcWidth, int srcHeight,
	byte[] destData, int destStride,
	int destX, int destY, int destWidth, int destHeight,
	int[] paletteMapping, boolean flipX, boolean flipY) {
	blit(BLIT_SRC,
		srcData, 8, srcStride, MSB_FIRST, srcX, srcY, srcWidth, srcHeight, null, null, null,
		ALPHA_OPAQUE, null, 0,
		destData, 8, destStride, MSB_FIRST, destX, destY, destWidth, destHeight, null, null, null,
		flipX, flipY);
}

/**
 * Stretches the source, a 4-bit image, into the destination, a 4-bit image.
 * NOTE: Palette mapping ignored (for now)
 */
static void stretch4(
	byte[] srcData, int srcStride,
	int srcX, int srcY, int srcWidth, int srcHeight,
	byte[] destData, int destStride,
	int destX, int destY, int destWidth, int destHeight,
	int[] paletteMapping, boolean flipX, boolean flipY) {
	blit(BLIT_SRC,
		srcData, 4, srcStride, MSB_FIRST, srcX, srcY, srcWidth, srcHeight, null, null, null,
		ALPHA_OPAQUE, null, 0,
		destData, 4, destStride, MSB_FIRST, destX, destY, destWidth, destHeight, null, null, null,
		flipX, flipY);
}

/**
 * Stretches the source, a 2-bit image, into the destination, a 2-bit image.
 * NOTE: Palette mapping ignored (for now)
 */
static void stretch2(
	byte[] srcData, int srcStride,
	int srcX, int srcY, int srcWidth, int srcHeight,
	byte[] destData, int destStride,
	int destX, int destY, int destWidth, int destHeight,
	int[] paletteMapping, boolean flipX, boolean flipY) {
	blit(BLIT_SRC,
		srcData, 2, srcStride, MSB_FIRST, srcX, srcY, srcWidth, srcHeight, null, null, null,
		ALPHA_OPAQUE, null, 0,
		destData, 2, destStride, MSB_FIRST, destX, destY, destWidth, destHeight, null, null, null,
		flipX, flipY);
}

/**
 * Stretches the source, a 1-bit image, into the destination, a 1-bit image.
 */
static void stretch1(
	byte[] srcData, int srcStride, int srcOrder,
	int srcX, int srcY, int srcWidth, int srcHeight,
	byte[] destData, int destStride, int destOrder,
	int destX, int destY, int destWidth, int destHeight,
	boolean flipX, boolean flipY) {
	blit(BLIT_SRC,
		srcData, 1, srcStride, srcOrder, srcX, srcY, srcWidth, srcHeight, null, null, null,
		ALPHA_OPAQUE, null, 0,
		destData, 1, destStride, destOrder, destX, destY, destWidth, destHeight, null, null, null,
		flipX, flipY);
}

}

