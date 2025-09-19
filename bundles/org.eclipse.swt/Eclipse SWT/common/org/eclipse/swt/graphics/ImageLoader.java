/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
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
 *     Hannes Wellmann - Unify ImageLoader implementations and extract differences into InternalImageLoader
 *******************************************************************************/
package org.eclipse.swt.graphics;

import java.io.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.DPIUtil.*;
import org.eclipse.swt.internal.image.*;

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
 *    <li>ERROR_IO - if an IO error occurs while reading the stream</li>
 *    <li>ERROR_INVALID_IMAGE - if the image stream contains invalid data</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image stream contains an unrecognized format</li>
 * </ul>
 */
public ImageData[] load(InputStream stream) {
	loadByZoom(stream, FileFormat.DEFAULT_ZOOM, FileFormat.DEFAULT_ZOOM);
	return data;
}

List<ElementAtZoom<ImageData>> loadByZoom(InputStream stream, int fileZoom, int targetZoom) {
	if (stream == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	reset();
	List<ElementAtZoom<ImageData>> images = NativeImageLoader.load(new ElementAtZoom<>(stream, fileZoom), this, targetZoom);
	data = images.stream().map(ElementAtZoom::element).toArray(ImageData[]::new);
	return images;
}

ImageData loadByTargetSize(InputStream stream, int targetWidth, int targetHeight) {
	if (stream == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	reset();
	ImageData image = NativeImageLoader.load(stream, this, targetWidth, targetHeight);
	data = new ImageData[] {image};
	return image;
}

static boolean canLoadAtZoom(InputStream stream, int fileZoom, int targetZoom) {
	if (stream == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return FileFormat.canLoadAtZoom(new ElementAtZoom<>(stream, fileZoom), targetZoom);
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
 *    <li>ERROR_IO - if an IO error occurs while reading the file</li>
 *    <li>ERROR_INVALID_IMAGE - if the image file contains invalid data</li>
 *    <li>ERROR_UNSUPPORTED_FORMAT - if the image file contains an unrecognized format</li>
 * </ul>
 */
public ImageData[] load(String filename) {
	loadByZoom(filename, FileFormat.DEFAULT_ZOOM, FileFormat.DEFAULT_ZOOM);
	return data;
}

List<ElementAtZoom<ImageData>> loadByZoom(String filename, int fileZoom, int targetZoom) {
	if (filename == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	try (InputStream stream = new FileInputStream(filename)) {
		return loadByZoom(stream, fileZoom, targetZoom);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	return null;
}

ImageData loadByTargetSize(String filename, int targetWidth, int targetHeight) {
	if (filename == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	try (InputStream stream = new FileInputStream(filename)) {
		return loadByTargetSize(stream, targetWidth, targetHeight);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	return null;
}

static boolean canLoadAtZoom(String filename, int fileZoom, int targetZoom) {
	if (filename == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	try (InputStream stream = new FileInputStream(filename)) {
		return canLoadAtZoom(stream, fileZoom, targetZoom);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
	}
	return false;
}

/**
 * Saves the image data in this ImageLoader to the specified stream.
 * The format parameter can have one of the following values:
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
	NativeImageLoader.save(stream, format, this);
}

/**
 * Saves the image data in this ImageLoader to a file with the specified name.
 * The format parameter can have one of the following values:
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
	try (OutputStream stream = new FileOutputStream(filename)) {
		save(stream, format);
	} catch (IOException e) {
		SWT.error(SWT.ERROR_IO, e);
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
