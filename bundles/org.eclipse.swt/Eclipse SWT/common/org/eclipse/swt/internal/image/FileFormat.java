/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
package org.eclipse.swt.internal.image;


import java.io.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;

/**
 * Abstract factory class for loading/unloading images from files or streams
 * in various image file formats.
 */
public abstract class FileFormat {
	static final String FORMAT_PACKAGE = "org.eclipse.swt.internal.image"; //$NON-NLS-1$
	static final String FORMAT_SUFFIX = "FileFormat"; //$NON-NLS-1$
	static final String[] FORMATS = {"WinBMP", "WinBMP", "GIF", "WinICO", "JPEG", "PNG", "TIFF", "OS2BMP"}; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$//$NON-NLS-5$ //$NON-NLS-6$//$NON-NLS-7$//$NON-NLS-8$

	LEDataInputStream inputStream;
	LEDataOutputStream outputStream;
	ImageLoader loader;
	int compression;

	/**
	 * The instance of the registered {@link SVGRasterizer}.
	 */
	private static final SVGRasterizer RASTERIZER = ServiceLoader.load(SVGRasterizer.class).findFirst().orElse(null);

static FileFormat getFileFormat (LEDataInputStream stream, String format) throws Exception {
	Class<?> clazz = Class.forName(FORMAT_PACKAGE + '.' + format + FORMAT_SUFFIX);
	FileFormat fileFormat = (FileFormat) clazz.getDeclaredConstructor().newInstance();
	if (fileFormat.isFileFormat(stream)) return fileFormat;
	return null;
}

/**
 * Return whether or not the specified input stream
 * represents a supported file format.
 */
abstract boolean isFileFormat(LEDataInputStream stream);

abstract ImageData[] loadFromByteStream();

/**
 * Read the specified input stream, and return the
 * device independent image array represented by the stream.
 */
public ImageData[] loadFromStream(LEDataInputStream stream) {
	try {
		inputStream = stream;
		return loadFromByteStream();
	} catch (Exception e) {
		if (e instanceof IOException) {
			SWT.error(SWT.ERROR_IO, e);
		} else {
			SWT.error(SWT.ERROR_INVALID_IMAGE, e);
		}
		return null;
	}
}

/**
 * Read the specified input stream using the specified loader, and
 * return the device independent image array represented by the stream.
 */
public static ImageData[] load(InputStream is, ImageLoader loader) {
	FileFormat fileFormat = null;
	LEDataInputStream stream = new LEDataInputStream(is);
	for (int i = 1; i < FORMATS.length; i++) {
		if (FORMATS[i] != null) {
			try {
				fileFormat = getFileFormat (stream, FORMATS[i]);
				if (fileFormat != null) break;
			} catch (ClassNotFoundException e) {
				FORMATS[i] = null;
			} catch (Exception e) {
			}
		}
	}
	if (fileFormat == null) SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
	fileFormat.loader = loader;
	return fileFormat.loadFromStream(stream);
}

/**
 * Loads an array of <code>ImageData</code> objects from the specified input stream.
 * Depending on the file type and zoom factor, this method either rasterizes the image
 * (for SVG files) or uses the provided {@link ImageLoader} to load the image data.
 *
 * <p>If the input stream is an SVG file and the specified zoom factor is not 0,
 * the method attempts to rasterize the SVG using the configured rasterizer.
 * Otherwise, it delegates the loading process to the {@link FileFormat#load(InputStream, ImageLoader)} method.
 *
 * @param stream the input stream to load the images from. The stream cannot be null.
 *               If the stream does not support marking, it is wrapped in a
 *               {@link BufferedInputStream}.
 * @param zoom the zoom factor to apply when rasterizing an SVG. A value of 0 uses
 *             the standard image loading method. A positive value specifies a scaling
 *             factor for the output image:
 *             <ul>
 *               <li><code>100</code>: Maintains the original size of the SVG when rasterized.</li>
 *               <li><code>200</code>: Doubles the size of the rasterized image.</li>
 *               <li><code>50</code>: Reduces the size of the rasterized image to half.</li>
 *             </ul>
 *             The scaling factor is applied uniformly to both width and height.
 * @param loader the {@link ImageLoader} instance used to load the image if the
 *               stream is not an SVG or if the zoom factor is 0.
 *
 * @return an array of <code>ImageData</code> objects loaded from the specified
 *         input stream, or <code>null</code> if an error occurs during loading.
 *
 * @exception IllegalArgumentException if the input stream is null.
 * @exception SWTException if an error occurs while loading the image data.
 *
 * @since 3.129
 */
public static ImageData[] load(InputStream stream, int zoom, ImageLoader loader) {
	if (stream == null) {
		throw new IllegalArgumentException("InputStream cannot be null");
	}
	if (!stream.markSupported()) {
		stream = new BufferedInputStream(stream);
	}
	try {
		if (RASTERIZER != null && zoom != 0 && isSVGFile(stream)) {
			return RASTERIZER.rasterizeSVG(stream, zoom);
		} else {
			return load(stream, loader);
		}
	} catch (IOException e) {
		SWT.error(SWT.ERROR_INVALID_IMAGE, e);
	}
	return null;
}

private static boolean isSVGFile(InputStream stream) throws IOException {
	if (stream == null) {
		throw new IllegalArgumentException("InputStream cannot be null");
	}
	stream.mark(1);
	try {
		int firstByte = stream.read();
		return firstByte == '<';
	} finally {
		stream.reset();
	}
}

/**
 * Write the device independent image array stored in the specified loader
 * to the specified output stream using the specified file format.
 */
public static void save(OutputStream os, int format, ImageLoader loader) {
	if (format < 0 || format >= FORMATS.length) SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
	if (FORMATS[format] == null) SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
	if (loader.data == null || loader.data.length < 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

	LEDataOutputStream stream = new LEDataOutputStream(os);
	FileFormat fileFormat = null;
	try {
		Class<?> clazz = Class.forName(FORMAT_PACKAGE + '.' + FORMATS[format] + FORMAT_SUFFIX);
		fileFormat = (FileFormat) clazz.getDeclaredConstructor().newInstance();
	} catch (Exception e) {
		SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
	}
	if (format == SWT.IMAGE_BMP_RLE) {
		switch (loader.data[0].depth) {
			case 8: fileFormat.compression = 1; break;
			case 4: fileFormat.compression = 2; break;
		}
	}
	fileFormat.unloadIntoStream(loader, stream);
}

abstract void unloadIntoByteStream(ImageLoader loader);

/**
 * Write the device independent image array stored in the specified loader
 * to the specified output stream.
 */
public void unloadIntoStream(ImageLoader loader, LEDataOutputStream stream) {
	try {
		outputStream = stream;
		unloadIntoByteStream(loader);
		outputStream.flush();
	} catch (Exception e) {
		try {outputStream.flush();} catch (Exception f) {}
		SWT.error(SWT.ERROR_IO, e);
	}
}
}
