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
 *******************************************************************************/
package org.eclipse.swt.internal.image;


import java.io.*;
import java.util.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;

/**
 * Abstract factory class for loading/unloading images from files or streams
 * in various image file formats.
 */
public abstract class FileFormat {
	private static final List<Supplier<FileFormat>> FORMAT_FACTORIES = new ArrayList<>();
	static {
		try {
			FORMAT_FACTORIES.add(WinBMPFileFormat::new);
		} catch (NoClassDefFoundError e) { } // ignore format
		try {
			FORMAT_FACTORIES.add(WinBMPFileFormat::new);
		} catch (NoClassDefFoundError e) { } // ignore format
		try {
			FORMAT_FACTORIES.add(GIFFileFormat::new);
		} catch (NoClassDefFoundError e) { } // ignore format
		try {
			FORMAT_FACTORIES.add(WinICOFileFormat::new);
		} catch (NoClassDefFoundError e) { } // ignore format
		try {
			FORMAT_FACTORIES.add(JPEGFileFormat::new);
		} catch (NoClassDefFoundError e) { } // ignore format
		try {
			FORMAT_FACTORIES.add(PNGFileFormat::new);
		} catch (NoClassDefFoundError e) { } // ignore format
		try {
			FORMAT_FACTORIES.add(TIFFFileFormat::new);
		} catch (NoClassDefFoundError e) { } // ignore format
		try {
			FORMAT_FACTORIES.add(OS2BMPFileFormat::new);
		} catch (NoClassDefFoundError e) { } // ignore format
	}

	public static final int DEFAULT_ZOOM = 100;

	static abstract class StaticImageFileFormat extends FileFormat {

		abstract ImageData[] loadFromByteStream();

		@Override
		ImageData[] loadFromByteStream(int zoom) {
			if (zoom == DEFAULT_ZOOM) {
				return loadFromByteStream();
			}
			return Arrays.stream(loadFromByteStream()).map(data -> {
				int width = DPIUtil.scaleUp(data.width, zoom);
				int height = DPIUtil.scaleUp(data.height, zoom);
				return data.scaledTo(width, height);
			}).toArray(ImageData[]::new);
		}
	}

	LEDataInputStream inputStream;
	LEDataOutputStream outputStream;
	ImageLoader loader;
	int compression;

	/**
	 * Return whether or not the specified input stream represents a supported file
	 * format.
	 */
	abstract boolean isFileFormat(LEDataInputStream stream) throws IOException;

	abstract ImageData[] loadFromByteStream(int zoom);

/**
 * Read the specified input stream, and return the
 * device independent image array represented by the stream.
 */
public ImageData[] loadFromStream(LEDataInputStream stream, int zoom) {
	try {
		inputStream = stream;
		return loadFromByteStream(zoom);
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
public static ImageData[] load(InputStream is, ImageLoader loader, int zoom) {
	LEDataInputStream stream = new LEDataInputStream(is);
	FileFormat fileFormat = FORMAT_FACTORIES.stream().skip(1) //
			.map(Supplier::get).filter(f -> {
				try {
					return f.isFileFormat(stream);
				} catch (IOException e) {
					return false;
				}
			}) //
			.findFirst().orElse(null);
	if (fileFormat == null) SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
	fileFormat.loader = loader;
	return fileFormat.loadFromStream(stream, zoom);
}

/**
 * Write the device independent image array stored in the specified loader
 * to the specified output stream using the specified file format.
 */
public static void save(OutputStream os, int format, ImageLoader loader) {
	if (format < 0 || format >= FORMAT_FACTORIES.size()) {
		SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
	}
	if (loader.data == null || loader.data.length < 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

	LEDataOutputStream stream = new LEDataOutputStream(os);
	FileFormat fileFormat = FORMAT_FACTORIES.get(format).get();
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
