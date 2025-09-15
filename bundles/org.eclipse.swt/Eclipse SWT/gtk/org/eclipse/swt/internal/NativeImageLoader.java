/*******************************************************************************
 * Copyright (c) 2019, 2025 Red Hat and others. All rights reserved.
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
 *     Red Hat - initial implementation
 *     Hannes Wellmann - Unify ImageLoader implementations and extract differences into InternalImageLoader
 *******************************************************************************/
package org.eclipse.swt.internal;

import java.io.*;
import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.DPIUtil.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.image.*;
import org.eclipse.swt.widgets.*;

public class NativeImageLoader {

	/** If the 29th byte of the PNG file is not zero, then it is interlaced. */
	private static final int PNG_INTERLACE_METHOD_OFFSET = 28;

	// --- loading ---

	public static List<ElementAtZoom<ImageData>> load(ElementAtZoom<InputStream> streamAtZoom, ImageLoader imageLoader, int targetZoom) {
		// 1) Load InputStream into byte array
		byte[] data_buffer;
		InputStream stream = streamAtZoom.element();
		try (stream) {
			data_buffer = stream.readAllBytes();
		} catch (IOException e) {
			SWT.error(SWT.ERROR_IO);
			return null;
		}
		if (data_buffer.length == 0) {
			SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT); // empty stream
		}
		InputStream stream2 = new ByteArrayInputStream(data_buffer);
		if (FileFormat.isDynamicallySizableFormat(stream2)) {
			try {
				stream2.reset();
			} catch (IOException e) {
				SWT.error(SWT.ERROR_IO);
			}
			return FileFormat.load(new ElementAtZoom<>(stream2, streamAtZoom.zoom()), imageLoader, targetZoom);
		}
		List<ImageData> imgDataList = new ArrayList<>();
		long loader = GDK.gdk_pixbuf_loader_new();
		// 2) Copy byte array to C memory, write to GdkPixbufLoader
		long buffer_ptr = OS.g_malloc(data_buffer.length);
		C.memmove(buffer_ptr, data_buffer, data_buffer.length);
		long[] error = new long[1];
		GDK.gdk_pixbuf_loader_write(loader, buffer_ptr, data_buffer.length, error);
		if (error[0] != 0) {
			// Bug 576484
			// It is safe just to assume if this fails it is most likely an IO error
			// since unsupported format is checked before, and invalid image right after.
			// Still, check if it belongs to the G_FILE_ERROR domain and IO error code
			if (OS.g_error_matches(error[0], OS.g_file_error_quark(), OS.G_FILE_ERROR_IO)) {
				SWT.error(SWT.ERROR_IO, null, Display.extractFreeGError(error[0]));
			} else {
				OS.g_error_free(error[0]);
			}
		}
		GDK.gdk_pixbuf_loader_close(loader, null);

		// 3) Get GdkPixbufAnimation from loader
		long pixbuf_animation = GDK.gdk_pixbuf_loader_get_animation(loader);
		if (pixbuf_animation == 0) {
			SWT.error(SWT.ERROR_INVALID_IMAGE);
		}
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
			long animation_iter = GDK.gdk_pixbuf_animation_get_iter(pixbuf_animation, start_time);
			int delay_time = 0;
			int time_offset = 0;
			// Fix the number of GIF frames as GdkPixbufAnimation does not provide an API to
			// determine number of frames.
			int num_frames = 32;
			for (int i = 0; i < num_frames; i++) {
				// Calculate time offset from start_time to next frame
				delay_time = GDK.gdk_pixbuf_animation_iter_get_delay_time(animation_iter);
				time_offset += delay_time;
				OS.g_time_val_add(start_time, time_offset * 1000);
				boolean update = GDK.gdk_pixbuf_animation_iter_advance(animation_iter, start_time);
				if (update) {
					long curr_pixbuf = GDK.gdk_pixbuf_animation_iter_get_pixbuf(animation_iter);
					long pixbuf_copy = GDK.gdk_pixbuf_copy(curr_pixbuf); // copy because curr_pixbuf might get disposed
																			// on next advance
					ImageData imgData = pixbufToImageData(pixbuf_copy);
					if (imageLoader.logicalScreenHeight == 0 && imageLoader.logicalScreenWidth == 0) {
						imageLoader.logicalScreenHeight = imgData.height;
						imageLoader.logicalScreenWidth = imgData.width;
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
		ImageData[] imgDataArray = new ImageData[imgDataList.size()];
		for (int i = 0; i < imgDataList.size(); i++) {
			imgDataArray[i] = imgDataList.get(i);
			// Loading completed, notify listeners
			// listener should only be called when loading interlaced/progressive
			// PNG/JPG/GIF ?
			ImageData data = (ImageData) imgDataArray[i].clone();
			if (imageLoader.hasListeners() && imgDataArray != null) {
				if (data.type == SWT.IMAGE_PNG && isInterlacedPNG(data_buffer)) {
					imageLoader.notifyListeners(new ImageLoaderEvent(imageLoader, data, i, true));
				} else if (data.type != SWT.IMAGE_PNG) {
					imageLoader.notifyListeners(new ImageLoaderEvent(imageLoader, data, i, true));
				}
			}
		}
		OS.g_free(buffer_ptr);
		OS.g_object_unref(loader);
		return Arrays.stream(imgDataArray).map(data -> new ElementAtZoom<>(data, streamAtZoom.zoom())).toList();
	}

	public static ImageData load(InputStream streamAtZoom, ImageLoader imageLoader, int targetWidth, int targetHeight) {
		return FileFormat.load(streamAtZoom, imageLoader, targetWidth, targetHeight);
	}

	/**
	 * Return true if the image is an interlaced PNG file. This is used to check
	 * whether ImageLoaderEvent should be fired when loading images.
	 *
	 * @return true iff 29th byte of PNG files is not zero
	 */
	private static boolean isInterlacedPNG(byte[] imageAsByteArray) {
		return imageAsByteArray.length > PNG_INTERLACE_METHOD_OFFSET
				&& imageAsByteArray[PNG_INTERLACE_METHOD_OFFSET] != 0;
	}

	/**
	 * Return the type of file from which the image was read by inspecting
	 * GdkPixbufFormat from GdkPixbufLoader
	 *
	 * It is expressed as one of the following values:
	 * <dl>
	 * <dt>{@link SWT#IMAGE_BMP}</dt>
	 * <dd>Windows BMP file format, no compression</dd>
	 * <dt>{@link SWT#IMAGE_BMP_RLE}</dt>
	 * <dd>Windows BMP file format, RLE compression if appropriate</dd>
	 * <dt>{@link SWT#IMAGE_GIF}</dt>
	 * <dd>GIF file format</dd>
	 * <dt>{@link SWT#IMAGE_ICO}</dt>
	 * <dd>Windows ICO file format</dd>
	 * <dt>{@link SWT#IMAGE_JPEG}</dt>
	 * <dd>JPEG file format</dd>
	 * <dt>{@link SWT#IMAGE_PNG}</dt>
	 * <dd>PNG file format</dd>
	 * <dt>{@link SWT#IMAGE_TIFF}</dt>
	 * <dd>TIFF file format</dd>
	 * </dl>
	 */
	private static int getImageFormat(long loader) {
		long format = GDK.gdk_pixbuf_loader_get_format(loader);
		long name = GDK.gdk_pixbuf_format_get_name(format);
		String nameStr = Converter.cCharPtrToJavaString(name, false);
		OS.g_free(name);
		return switch (nameStr) {
		case "bmp" -> SWT.IMAGE_BMP;
		case "gif" -> SWT.IMAGE_GIF;
		case "ico" -> SWT.IMAGE_ICO;
		case "jpeg" -> SWT.IMAGE_JPEG;
		case "png" -> SWT.IMAGE_PNG;
		case "tiff" -> SWT.IMAGE_TIFF;
		case "svg" -> SWT.IMAGE_SVG;
		default -> SWT.IMAGE_UNDEFINED;
		};
	}

	/**
	 * Convert GdkPixbuf pointer to Java object ImageData
	 *
	 * @return ImageData with pixbuf data
	 */
	private static ImageData pixbufToImageData(long pixbuf) {
		boolean hasAlpha = GDK.gdk_pixbuf_get_has_alpha(pixbuf);
		int width = GDK.gdk_pixbuf_get_width(pixbuf);
		int height = GDK.gdk_pixbuf_get_height(pixbuf);
		int stride = GDK.gdk_pixbuf_get_rowstride(pixbuf);
		int n_channels = GDK.gdk_pixbuf_get_n_channels(pixbuf); // only 3 or 4 samples per pixel are supported
		int bits_per_sample = GDK.gdk_pixbuf_get_bits_per_sample(pixbuf); // only 8 bit per sample are supported
		long pixels = GDK.gdk_pixbuf_get_pixels(pixbuf);
		/*
		 * From GDK Docs: last row in the pixbuf may not be as wide as the full
		 * rowstride, but rather just as wide as the pixel data needs to be. Compute the
		 * width in bytes of the last row to copy raw pixbuf data.
		 */
		int lastRowWidth = width * ((n_channels * bits_per_sample + 7) / 8);
		byte[] srcData = new byte[stride * height];
		C.memmove(srcData, pixels, stride * (height - 1) + lastRowWidth);
		/*
		 * Note: GdkPixbuf only supports 3/4 n_channels and 8 bits_per_sample, This
		 * means all images are of depth 24 / depth 32. This means loading images will
		 * result in a direct PaletteData with RGB masks, since there is no way to
		 * determine indexed PaletteData info.
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

	// --- saving ---

	public static void save(OutputStream stream, int format, ImageLoader imageLoader) {
		if (format == -1) {
			SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
		}
		if (imageLoader.data == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		ImageData imgData = imageLoader.data[0];
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
			FileFormat.save(stream, format, imageLoader);
			return;
		}

		/*
		 * Destination offsets, GdkPixbuf data is stored in RGBA format.
		 */
		int da = 3, dr = 0, dg = 1, db = 2;

		/*
		 * ImageData offsets. These can vary depending on how the ImageData.data field
		 * was populated. In most cases it will be RGB format, so this case is assumed
		 * (blue shift is 0).
		 *
		 * If blue is negatively shifted, then we are dealing with BGR byte ordering, so
		 * adjust the offsets accordingly.
		 */
		int or = 0, og = 1, ob = 2;
		PaletteData palette = imgData.palette;
		if (palette.isDirect && palette.blueShift < 0) {
			or = 2;
			og = 1;
			ob = 0;
		}

		// We use alpha by default now so all images saved are 32 bit, if there is no
		// alpha we set it to 255
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
		long pixbuf = GDK.gdk_pixbuf_new_from_data(buffer_ptr, colorspace, true, 8, width, height, rowstride, 0, 0);
		if (pixbuf == 0) {
			OS.g_free(buffer_ptr);
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		// Write pixbuf to byte array and then to OutputStream
		String typeStr = switch (format) {
		case SWT.IMAGE_BMP_RLE -> "bmp";
		case SWT.IMAGE_BMP -> "bmp";
		case SWT.IMAGE_GIF -> "gif";
		case SWT.IMAGE_ICO -> "ico";
		case SWT.IMAGE_JPEG -> "jpeg";
		case SWT.IMAGE_PNG -> "png";
		case SWT.IMAGE_TIFF -> "tiff";
		case SWT.IMAGE_SVG -> "svg";
		default -> "";
		};
		byte[] type = Converter.wcsToMbcs(typeStr, true);

		long[] buffer = new long[1];
		if (type == null || typeStr == "") {
			OS.g_free(buffer_ptr);
			SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
		}
		long[] len = new long[1];
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

}
