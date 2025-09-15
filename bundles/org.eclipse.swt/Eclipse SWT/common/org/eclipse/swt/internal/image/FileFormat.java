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
import org.eclipse.swt.internal.DPIUtil.*;

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
		try {
			FORMAT_FACTORIES.add(SVGFileFormat::new);
		} catch (NoClassDefFoundError e) { } // ignore format
	}

	public static final int DEFAULT_ZOOM = 100;

	private static Optional<FileFormat> determineFileFormat(LEDataInputStream stream) {
		return FORMAT_FACTORIES.stream().skip(1).map(Supplier::get).filter(f -> {
			try {
				return f.isFileFormat(stream);
			} catch (IOException e) {
				return false;
			}
		}).findFirst();
	}

	private static final int MAX_SIGNATURE_BYTES = 18 + 2; // e.g. Win-BMP or OS2-BMP plus a safety-margin

	public static boolean isDynamicallySizableFormat(InputStream is) {
		Optional<FileFormat> format = determineFileFormat(new LEDataInputStream(is, MAX_SIGNATURE_BYTES));
		return format.isPresent() && !(format.get() instanceof StaticImageFileFormat);
	}

	static abstract class StaticImageFileFormat extends FileFormat {

		abstract ImageData[] loadFromByteStream();

		@Override
		List<ElementAtZoom<ImageData>> loadFromByteStream(int fileZoom, int targetZoom) {
			return Arrays.stream(loadFromByteStream()).map(d -> new ElementAtZoom<>(d, fileZoom)).toList();
		}

		@Override
		ImageData loadFromByteStreamByTargetSize(int targetWidth, int targetHeight) {
			return loadFromByteStream()[0];
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

	/**
	 * Format that do not implement {@link StaticImageFileFormat} MUST return
	 * {@link ImageData} with the specified {@code targetZoom}.
	 */
	abstract List<ElementAtZoom<ImageData>> loadFromByteStream(int fileZoom, int targetZoom);

	abstract ImageData loadFromByteStreamByTargetSize(int targetWidth, int targetHeight);

/**
 * Read the specified input stream, and return the
 * device independent image array represented by the stream.
 */
public List<ElementAtZoom<ImageData>> loadFromStream(LEDataInputStream stream, int fileZoom, int targetZoom) {
	try {
		inputStream = stream;
		return loadFromByteStream(fileZoom, targetZoom);
	} catch (Exception e) {
		if (e instanceof IOException) {
			SWT.error(SWT.ERROR_IO, e);
		} else {
			SWT.error(SWT.ERROR_INVALID_IMAGE, e);
		}
		return null;
	}
}

public ImageData loadFromStreamByTargetSize(LEDataInputStream stream, int targetWidth, int targetHeight) {
	try {
		inputStream = stream;
		return loadFromByteStreamByTargetSize(targetWidth, targetHeight);
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
public static List<ElementAtZoom<ImageData>> load(ElementAtZoom<InputStream> is, ImageLoader loader, int targetZoom) {
	LEDataInputStream stream = new LEDataInputStream(is.element());
	FileFormat fileFormat = determineFileFormat(stream).orElseGet(() -> {
		SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
		return null;
	});
	fileFormat.loader = loader;
	return fileFormat.loadFromStream(stream, is.zoom(), targetZoom);
}

public static ImageData load(InputStream is, ImageLoader loader, int targetWidth, int targetHeight) {
	LEDataInputStream stream = new LEDataInputStream(is);
	FileFormat fileFormat = determineFileFormat(stream).orElseGet(() -> {
		SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
		return null;
	});
	fileFormat.loader = loader;
	return fileFormat.loadFromStreamByTargetSize(stream, targetWidth, targetHeight);
}

public static boolean canLoadAtZoom(ElementAtZoom<InputStream> is, int targetZoom) {
	return is.zoom() == targetZoom  || isDynamicallySizableFormat(is.element());
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
