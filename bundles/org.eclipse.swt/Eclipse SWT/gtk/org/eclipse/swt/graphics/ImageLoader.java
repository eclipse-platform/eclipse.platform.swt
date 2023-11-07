/*******************************************************************************
 * Copyright (c) 2019 Red Hat and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


import java.io.*;
import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.image.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class are used to load images from,
 * and save images to, a file or stream.
 * <p>
 * Currently supported image formats are:
 * </p><ul>
 * <li>BMP (Windows or OS/2 Bitmap)</li>
 * <li>ICO (Windows Icon)</li>
 * <li>JPEG</li>
 * <li>GIF</li>
 * <li>PNG</li>
 * <li>TIFF</li>
 * </ul>
 * <code>ImageLoaders</code> can be used to:
 * <ul>
 * <li>load/save single images in all formats</li>
 * <li>load/save multiple images (GIF/ICO/TIFF)</li>
 * <li>load/save animated GIF images</li>
 * <li>load interlaced GIF/PNG images</li>
 * <li>load progressive JPEG images</li>
 * </ul>
 *
 * <p>
 * NOTE: <code>ImageLoader</code> is implemented in Java on some platforms, which has
 * certain performance implications. Performance and memory sensitive applications may
 * benefit from using one of the constructors provided by <code>Image</code>, as these
 * are implemented natively.</p>
 *
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ImageAnalyzer</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class ImageLoader {

	/**
	 * the array of ImageData objects in this ImageLoader.
	 * This array is read in when the load method is called,
	 * and it is written out when the save method is called
	 */
	public ImageData[] data;

	/**
	 * the width of the logical screen on which the images
	 * reside, in pixels (this corresponds to the GIF89a
	 * Logical Screen Width value)
	 */
	public int logicalScreenWidth;

	/**
	 * the height of the logical screen on which the images
	 * reside, in pixels (this corresponds to the GIF89a
	 * Logical Screen Height value)
	 */
	public int logicalScreenHeight;

	/**
	 * the background pixel for the logical screen (this
	 * corresponds to the GIF89a Background Color Index value).
	 * The default is -1 which means 'unspecified background'
	 */
	public int backgroundPixel;

	/**
	 * the number of times to repeat the display of a sequence
	 * of animated images (this corresponds to the commonly-used
	 * GIF application extension for "NETSCAPE 2.0 01").
	 * The default is 1. A value of 0 means 'display repeatedly'
	 */
	public int repeatCount;

	/**
	 * This is the compression used when saving jpeg and png files.
	 * <p>
	 * When saving jpeg files, the value is from 1 to 100,
	 * where 1 is very high compression but low quality, and 100 is
	 * no compression and high quality; default is 75.
	 * </p><p>
	 * When saving png files, the value is from 0 to 3, but they do not impact the quality
	 * because PNG is lossless compression. 0 is uncompressed, 1 is low compression and fast,
	 * 2 is default compression, and 3 is high compression but slow.
	 * </p>
	 *
	 * @since 3.8
	 */
	public int compression;

	/**
	 * If the 29th byte of the PNG file is not zero, then it is interlaced.
	 */
	final static int PNG_INTERLACE_METHOD_OFFSET = 28;

	/*
	 * the set of ImageLoader event listeners, created on demand
	 */
	List<ImageLoaderListener> imageLoaderListeners;

/**
 * Construct a new empty ImageLoader.
 */
public ImageLoader() {
	reset();
}

/**
 * Resets the fields of the ImageLoader, except for the
 * <code>imageLoaderListeners</code> field.
 */
void reset() {
	data = null;
	logicalScreenWidth = 0;
	logicalScreenHeight = 0;
	backgroundPixel = -1;
	repeatCount = 1;
	compression = -1;
}

/**
 * Loads an array of <code>ImageData</code> objects from the
 * specified input stream. Throws an error if either an error
 * occurs while loading the images, or if the images are not
 * of a supported type. Returns the loaded image data array.
 *
 * @param stream the input stream to load the images from
 * @return an array of <code>ImageData</code> objects loaded from the specified input stream
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the stream is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_IO - if an IO error occurs while reading from the stream</li>
 *    <li>ERROR_INVALID_IMAGE - if the image stream contains invalid data</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image stream contains an unrecognized format</li>
 * </ul>
 */
public ImageData[] load(InputStream stream) {
	if (stream == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	reset();
	ImageData [] imgDataArray = getImageDataArrayFromStream(stream);
	data = imgDataArray;
	return imgDataArray;
}

/**
 * Return true if the image is an interlaced PNG file.
 * This is used to check whether ImageLoaderEvent should be fired when loading images.
 * @return true iff 29th byte of PNG files is not zero
 */
boolean isInterlacedPNG(byte [] imageAsByteArray) {
	return imageAsByteArray.length > PNG_INTERLACE_METHOD_OFFSET && imageAsByteArray[PNG_INTERLACE_METHOD_OFFSET] != 0;
}

ImageData [] getImageDataArrayFromStream(InputStream stream) {
	long loader = GDK.gdk_pixbuf_loader_new();
	List<ImageData> imgDataList = new ArrayList<>();
	try {
		// 1) Load InputStream into byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		stream.transferTo(baos);
		baos.flush();
		byte[] data_buffer = baos.toByteArray();
		if (data_buffer.length == 0) SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);	// empty stream

		// 2) Copy byte array to C memory, write to GdkPixbufLoader
		long buffer_ptr = OS.g_malloc(data_buffer.length);
		C.memmove(buffer_ptr, data_buffer, data_buffer.length);
		long [] error = new long [1];
		GDK.gdk_pixbuf_loader_write(loader, buffer_ptr, data_buffer.length, error);
		if(error[0] != 0) {
			/* Bug 576484
			 * It is safe just to assume if this fails it is most likely an IO error
			 * since unsupported format is checked before, and invalid image right after.
			 * Still, check if it belongs to the G_FILE_ERROR domain and IO error code
			 */
			if(OS.g_error_matches(error[0], OS.g_file_error_quark(), OS.G_FILE_ERROR_IO)){
				SWT.error(SWT.ERROR_IO, null, Display.extractFreeGError(error[0]));
			} else {
				OS.g_error_free(error[0]);
			}
		}
		GDK.gdk_pixbuf_loader_close(loader, null);

		// 3) Get GdkPixbufAnimation from loader
		long pixbuf_animation = GDK.gdk_pixbuf_loader_get_animation(loader);
		if (pixbuf_animation == 0) SWT.error(SWT.ERROR_INVALID_IMAGE);

		boolean isStatic = GDK.gdk_pixbuf_animation_is_static_image(pixbuf_animation);
		if (isStatic) {
			// Static image, get as single pixbuf and convert it to ImageData
			long pixbuf = GDK.gdk_pixbuf_animation_get_static_image(pixbuf_animation);
			ImageData imgData = pixbufToImageData(pixbuf);
			imgData.type = getImageFormat(loader);
			imgDataList.add(imgData);
		} else {
			// Image with multiple frames, iterate through each frame and convert
			// each frame to ImageData
			long start_time = OS.g_malloc(8);
			OS.g_get_current_time(start_time);
			long animation_iter = GDK.gdk_pixbuf_animation_get_iter (pixbuf_animation, start_time);
			int delay_time = 0;
			int time_offset = 0;
			// Fix the number of GIF frames as GdkPixbufAnimation does not provide an API to
			// determine number of frames.
			int num_frames = 32;
			for (int i = 0; i < num_frames; i++) {
				// Calculate time offset from start_time to next frame
				delay_time = GDK.gdk_pixbuf_animation_iter_get_delay_time (animation_iter);
				time_offset += delay_time;
				OS.g_time_val_add(start_time, time_offset * 1000);
				boolean update = GDK.gdk_pixbuf_animation_iter_advance (animation_iter, start_time);
				if (update) {
					long curr_pixbuf = GDK.gdk_pixbuf_animation_iter_get_pixbuf (animation_iter);
					long pixbuf_copy = GDK.gdk_pixbuf_copy(curr_pixbuf); // copy because curr_pixbuf might get disposed on next advance
					ImageData imgData = pixbufToImageData(pixbuf_copy);
					if (this.logicalScreenHeight == 0 && this.logicalScreenWidth == 0) {
						this.logicalScreenHeight = imgData.height;
						this.logicalScreenWidth = imgData.width;
					}
					OS.g_object_unref(pixbuf_copy);
					imgData.type = getImageFormat(loader);
					imgData.delayTime = delay_time;
					imgDataList.add(imgData);
				} else {
					break;
				}
			}
		}
		ImageData [] imgDataArray = new ImageData [imgDataList.size()];
		for (int i = 0; i < imgDataList.size(); i++) {
			imgDataArray [i] = imgDataList.get(i);
			// Loading completed, notify listeners
			// listener should only be called when loading interlaced/progressive PNG/JPG/GIF ?
			ImageData data = (ImageData) imgDataArray [i].clone();
			if (this.hasListeners() && imgDataArray != null) {
				if (data.type == SWT.IMAGE_PNG && isInterlacedPNG(data_buffer)) {
					this.notifyListeners(new ImageLoaderEvent(this, data, i, true));
				} else if (data.type != SWT.IMAGE_PNG) {
					this.notifyListeners(new ImageLoaderEvent(this, data, i, true));
				}
			}
		}
		OS.g_free(buffer_ptr);
		OS.g_object_unref(loader);
		stream.close();
		return imgDataArray;
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO);
	}
	return null;
}

/**
 * Loads an array of <code>ImageData</code> objects from the
 * file with the specified name. Throws an error if either
 * an error occurs while loading the images, or if the images are
 * not of a supported type. Returns the loaded image data array.
 *
 * @param filename the name of the file to load the images from
 * @return an array of <code>ImageData</code> objects loaded from the specified file
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
public ImageData[] load(String filename) {
	if (filename == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	InputStream stream = null;
	try {
		stream = new FileInputStream(filename);
		return load(stream);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	} finally {
		try {
			if (stream != null) stream.close();
		} catch (IOException e) {
			// Ignore error
		}
	}
	return null;
}

/**
 * Load GdkPixbuf directly using gdk_pixbuf_new_from_file,
 * without FileInputStream.
 */
ImageData[] loadFromFile(String filename) {
	long pixbuf = gdk_pixbuf_new_from_file(filename);
	if (pixbuf == 0) return null;
	ImageData imgData= pixbufToImageData(pixbuf);
	return data = new ImageData[] {imgData};
}

/**
 * Return the type of file from which the image was read
 * by inspecting GdkPixbufFormat from GdkPixbufLoader
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
int getImageFormat(long loader) {
	long format = GDK.gdk_pixbuf_loader_get_format(loader);
	long name = GDK.gdk_pixbuf_format_get_name(format);
	String nameStr = Converter.cCharPtrToJavaString(name, false);
	OS.g_free(name);
	switch (nameStr) {
		case "bmp": return SWT.IMAGE_BMP;
		case "gif": return SWT.IMAGE_GIF;
		case "ico": return SWT.IMAGE_ICO;
		case "jpeg": return SWT.IMAGE_JPEG;
		case "png": return SWT.IMAGE_PNG;
		case "svg": return SWT.IMAGE_SVG;
		default: return SWT.IMAGE_UNDEFINED;
	}
}

/**
 * Convert GdkPixbuf pointer to Java object ImageData
 * @return ImageData with pixbuf data
 */
static ImageData pixbufToImageData(long pixbuf) {
	boolean hasAlpha = GDK.gdk_pixbuf_get_has_alpha(pixbuf);
	int width = GDK.gdk_pixbuf_get_width(pixbuf);
	int height = GDK.gdk_pixbuf_get_height(pixbuf);
	int stride = GDK.gdk_pixbuf_get_rowstride(pixbuf);
	int n_channels = GDK.gdk_pixbuf_get_n_channels(pixbuf); 			// only 3 or 4 samples per pixel are supported
	int bits_per_sample = GDK.gdk_pixbuf_get_bits_per_sample(pixbuf); 	// only 8 bit per sample are supported
	long pixels = GDK.gdk_pixbuf_get_pixels(pixbuf);
	/*
	 * From GDK Docs: last row in the pixbuf may not be as wide as the full rowstride,
	 * but rather just as wide as the pixel data needs to be. Compute the width in bytes
	 * of the last row to copy raw pixbuf data.
	 */
	int lastRowWidth = width * ((n_channels * bits_per_sample + 7) / 8);
	byte[] srcData = new byte[stride * height];
	C.memmove(srcData, pixels, stride * (height - 1) + lastRowWidth);
	/*
	 * Note: GdkPixbuf only supports 3/4 n_channels and 8 bits_per_sample,
	 * This means all images are of depth 24 / depth 32. This means loading
	 * images will result in a direct PaletteData with RGB masks, since
	 * there is no way to determine indexed PaletteData info.
	 *
	 * See https://www.eclipse.org/articles/Article-SWT-images/graphics-resources.html#PaletteData
	 */
	PaletteData palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
	ImageData imgData = new ImageData(width, height, bits_per_sample * n_channels, palette, stride, srcData);
	if (hasAlpha) {
		byte[] alphaData = imgData.alphaData = new byte[width * height];
		for (int y = 0, offset = 0, alphaOffset = 0; y < height; y++) {
			for (int x = 0; x < width; x++, offset += n_channels) {
				byte r = srcData[offset + 0];
				byte g = srcData[offset + 1];
				byte b = srcData[offset + 2];
				byte a = srcData[offset + 3];
				srcData[offset + 0] = 0;
				alphaData[alphaOffset++] = a;
				if (a != 0) {
					srcData[offset + 1] = r;
					srcData[offset + 2] = g;
					srcData[offset + 3] = b;
				}
			}
		}
	} else {
		for (int y = 0, offset = 0; y < height; y++) {
			for (int x = 0; x < width; x++, offset += n_channels) {
				byte r = srcData[offset + 0];
				byte g = srcData[offset + 1];
				byte b = srcData[offset + 2];
				srcData[offset + 0] = r;
				srcData[offset + 1] = g;
				srcData[offset + 2] = b;
			}
		}
	}
	return imgData;
}

/**
 * Returns GdkPixbuf pointer by loading an image from filename (Java string)
 */
static long gdk_pixbuf_new_from_file(String filename) {
	int length = filename.length ();
	char [] chars = new char [length];
	filename.getChars (0, length, chars, 0);
	byte [] buffer = Converter.wcsToMbcs(chars, true);
	return GDK.gdk_pixbuf_new_from_file(buffer, null);
}

/**
 * Saves the image data in this ImageLoader to the specified stream.
 * The format parameter can have one of the following values:
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
 *
 * @param stream the output stream to write the images to
 * @param format the format to write the images in
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the stream is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_IO - if an IO error occurs while writing to the stream</li>
 *    <li>ERROR_INVALID_IMAGE - if the image data contains invalid data</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image data cannot be saved to the requested format</li>
 * </ul>
 */
public void save(OutputStream stream, int format) {
	if (stream == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (format == -1) SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
	if (this.data == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	ImageData imgData = this.data [0];
	int colorspace = GDK.GDK_COLORSPACE_RGB;
	boolean alpha_supported = format == SWT.IMAGE_TIFF || format == SWT.IMAGE_PNG || format == SWT.IMAGE_ICO;
	boolean has_alpha = imgData.alphaData != null && alpha_supported;
	int width = imgData.width;
	int height = imgData.height;
	// original n_channels. Native implementation will only be used in case of 3 or 4
	int n_channels = imgData.bytesPerLine / width;

	// Native implementation only supports a subset of possible image configurations.
	// Redirect the not supported variants to the Java implementation.
	// See also https://bugs.eclipse.org/bugs/show_bug.cgi?id=558043
	if (!imgData.palette.isDirect || n_channels < 3 || n_channels > 4) {
		FileFormat.save(stream, format, this);
		return;
	}

	/*
	 * Destination offsets, GdkPixbuf data is stored in RGBA format.
	 */
	int da = 3; int dr = 0; int dg = 1; int db = 2;

	/*
	 * ImageData offsets. These can vary depending on how the ImageData.data
	 * field was populated. In most cases it will be RGB format, so this case
	 * is assumed (blue shift is 0).
	 *
	 * If blue is negatively shifted, then we are dealing with BGR byte ordering, so
	 * adjust the offsets accordingly.
	 */
	int or = 0; int og = 1; int ob = 2;
	PaletteData palette = imgData.palette;
	if (palette.isDirect && palette.blueShift < 0) {
		or = 2;
		og = 1;
		ob = 0;
	}

	// We use alpha by default now so all images saved are 32 bit, if there is no alpha we set it to 255
	int bytes_per_pixel = 4;
	byte[] srcData = new byte[(width * height * bytes_per_pixel)];

	int alpha_offset = n_channels == 4 ? 1 : 0;
	if (has_alpha) {
		for (int y = 0, offset = 0, new_offset = 0, alphaIndex = 0; y < height; y++) {
			for (int x = 0; x < width; x++, offset += n_channels, new_offset += bytes_per_pixel) {
				byte a = imgData.alphaData[alphaIndex++];
				byte r = imgData.data[offset + alpha_offset + or];
				byte g = imgData.data[offset + alpha_offset + og];
				byte b = imgData.data[offset + alpha_offset + ob];

				// GdkPixbuf expects RGBA format
				srcData[new_offset + db] = b;
				srcData[new_offset + dg] = g;
				srcData[new_offset + dr] = r;
				srcData[new_offset + da] = a;
			}
		}
	} else {
		for (int y = 0, offset = 0, new_offset = 0; y < height; y++) {
			for (int x = 0; x < width; x++, offset += n_channels, new_offset += bytes_per_pixel) {
				byte r = imgData.data[offset + alpha_offset + or];
				byte g = imgData.data[offset + alpha_offset + og];
				byte b = imgData.data[offset + alpha_offset + ob];
				byte a = (byte) 255;

				srcData[new_offset + db] = b;
				srcData[new_offset + dg] = g;
				srcData[new_offset + dr] = r;
				srcData[new_offset + da] = a;
			}
		}
	}

	// Get GdkPixbuf from pixel data buffer
	long buffer_ptr = OS.g_malloc(srcData.length);
	C.memmove(buffer_ptr, srcData, srcData.length);
	int rowstride = srcData.length / height;
	// We use alpha in all cases, if no alpha is provided then it's just 255
	long pixbuf = GDK.gdk_pixbuf_new_from_data (buffer_ptr, colorspace, true, 8, width, height, rowstride, 0, 0);
	if (pixbuf == 0) {
		OS.g_free(buffer_ptr);
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}

	// Write pixbuf to byte array and then to OutputStream
	String typeStr = "";
	switch (format) {
		case SWT.IMAGE_BMP_RLE: typeStr = "bmp"; break;
		case SWT.IMAGE_BMP: typeStr = "bmp"; break;
		case SWT.IMAGE_GIF: typeStr = "gif"; break;
		case SWT.IMAGE_ICO: typeStr = "ico"; break;
		case SWT.IMAGE_JPEG: typeStr = "jpeg"; break;
		case SWT.IMAGE_PNG: typeStr = "png"; break;
		case SWT.IMAGE_TIFF: typeStr = "tiff"; break;
		case SWT.IMAGE_SVG: typeStr = "svg"; break;
	}
	byte [] type = Converter.wcsToMbcs(typeStr, true);

	long [] buffer = new long [1];
	if (type == null || typeStr == "") {
		OS.g_free(buffer_ptr);
		SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
	}
	long [] len = new long [1];
	GDK.gdk_pixbuf_save_to_bufferv(pixbuf, buffer, len, type, null, null, null);
	byte[] byteArray = new byte[(int) len[0]];
	C.memmove(byteArray, buffer[0], byteArray.length);
	try {
		stream.write(byteArray);
	} catch (IOException e) {
		OS.g_free(buffer_ptr);
		SWT.error(SWT.ERROR_IO);
	}
	// must free buffer_ptr last otherwise we get half/corrupted image
	OS.g_free(buffer_ptr);
}

/**
 * Saves the image data in this ImageLoader to a file with the specified name.
 * The format parameter can have one of the following values:
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
 *
 * @param filename the name of the file to write the images to
 * @param format the format to write the images in
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the file name is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_IO - if an IO error occurs while writing to the file</li>
 *    <li>ERROR_INVALID_IMAGE - if the image data contains invalid data</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image data cannot be saved to the requested format</li>
 * </ul>
 */
public void save(String filename, int format) {
	if (filename == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	OutputStream stream = null;
	try {
		stream = new FileOutputStream(filename);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	save(stream, format);
	try {
		stream.close();
	} catch (IOException e) {
	}
}

/**
 * Adds the listener to the collection of listeners who will be
 * notified when image data is either partially or completely loaded.
 * <p>
 * An ImageLoaderListener should be added before invoking
 * one of the receiver's load methods. The listener's
 * <code>imageDataLoaded</code> method is called when image
 * data has been partially loaded, as is supported by interlaced
 * GIF/PNG or progressive JPEG images.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 *
 * @see ImageLoaderListener
 * @see ImageLoaderEvent
 */
public void addImageLoaderListener(ImageLoaderListener listener) {
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (imageLoaderListeners == null) {
		imageLoaderListeners = new ArrayList<>();
	}
	imageLoaderListeners.add(listener);
}

/**
 * Removes the listener from the collection of listeners who will be
 * notified when image data is either partially or completely loaded.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 *
 * @see #addImageLoaderListener(ImageLoaderListener)
 */
public void removeImageLoaderListener(ImageLoaderListener listener) {
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (imageLoaderListeners == null) return;
	imageLoaderListeners.remove(listener);
}

/**
 * Returns <code>true</code> if the receiver has image loader
 * listeners, and <code>false</code> otherwise.
 *
 * @return <code>true</code> if there are <code>ImageLoaderListener</code>s, and <code>false</code> otherwise
 *
 * @see #addImageLoaderListener(ImageLoaderListener)
 * @see #removeImageLoaderListener(ImageLoaderListener)
 */
public boolean hasListeners() {
	return imageLoaderListeners != null && imageLoaderListeners.size() > 0;
}

/**
 * Notifies all image loader listeners that an image loader event
 * has occurred. Pass the specified event object to each listener.
 *
 * @param event the <code>ImageLoaderEvent</code> to send to each <code>ImageLoaderListener</code>
 */
public void notifyListeners(ImageLoaderEvent event) {
	if (!hasListeners()) return;
	int size = imageLoaderListeners.size();
	for (int i = 0; i < size; i++) {
		ImageLoaderListener listener = imageLoaderListeners.get(i);
		listener.imageDataLoaded(event);
	}
}

}
