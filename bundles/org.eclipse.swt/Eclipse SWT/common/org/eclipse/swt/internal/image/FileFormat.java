/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.image;


import java.io.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public abstract class FileFormat {
	static final String FORMAT_PACKAGE = "org.eclipse.swt.internal.image"; //$NON-NLS-1$
	static final String FORMAT_SUFFIX = "FileFormat"; //$NON-NLS-1$
	static final String[] FORMATS = {"WinBMP", "WinBMP", "GIF", "WinICO", "JPEG", "PNG", "TIFF", "OS2BMP"}; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$//$NON-NLS-5$ //$NON-NLS-6$//$NON-NLS-7$//$NON-NLS-8$
	
	LEDataInputStream inputStream;
	LEDataOutputStream outputStream;
	ImageLoader loader;
	int compression;

byte[] bitInvertData(byte[] data, int startIndex, int endIndex) {
	// Destructively bit invert data in the given byte array.
	for (int i = startIndex; i < endIndex; i++) {
		data[i] = (byte)(255 - data[i - startIndex]);
	}
	return data;
}

/**
 * Return whether or not the specified input stream
 * represents a supported file format.
 */
abstract boolean isFileFormat(LEDataInputStream stream);

abstract ImageData[] loadFromByteStream();

public ImageData[] loadFromStream(LEDataInputStream stream) {
	try {
		inputStream = stream;
		return loadFromByteStream();
	} catch (Exception e) {
		SWT.error(SWT.ERROR_IO, e);
		return null;
	}
}

public static ImageData[] load(InputStream is, ImageLoader loader) {
	FileFormat fileFormat = null;
	LEDataInputStream stream = new LEDataInputStream(is);
	boolean isSupported = false;	
	for (int i = 1; i < FORMATS.length; i++) {
		if (FORMATS[i] != null) {
			try {
				Class clazz = Class.forName(FORMAT_PACKAGE + '.' + FORMATS[i] + FORMAT_SUFFIX);
				fileFormat = (FileFormat) clazz.newInstance();
				if (fileFormat.isFileFormat(stream)) {
					isSupported = true;
					break;
				}
			} catch (ClassNotFoundException e) {
				FORMATS[i] = null;
			} catch (Exception e) {
			}
		}
	}
	if (!isSupported) SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
	fileFormat.loader = loader;
	return fileFormat.loadFromStream(stream);
}

public static void save(OutputStream os, int format, ImageLoader loader) {
	if (format < 0 || format >= FORMATS.length) SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
	if (FORMATS[format] == null) SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);

	/* We do not currently support writing multi-image files,
	 * so we use the first image data in the loader's array. */
	ImageData data = loader.data[0];
	LEDataOutputStream stream = new LEDataOutputStream(os);
	FileFormat fileFormat = null;
	try {
		Class clazz = Class.forName(FORMAT_PACKAGE + '.' + FORMATS[format] + FORMAT_SUFFIX);
		fileFormat = (FileFormat) clazz.newInstance();
	} catch (Exception e) {
		SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
	}
	if (format == SWT.IMAGE_BMP_RLE) {
		switch (data.depth) {
			case 8: fileFormat.compression = 1; break;
			case 4: fileFormat.compression = 2; break;
		}
	}
	fileFormat.unloadIntoStream(data, stream);
}

abstract void unloadIntoByteStream(ImageData image);

public void unloadIntoStream(ImageData image, LEDataOutputStream stream) {
	try {
		outputStream = stream;
		unloadIntoByteStream(image);
		outputStream.close();
	} catch (Exception e) {
		try {outputStream.close();} catch (Exception f) {}
		SWT.error(SWT.ERROR_IO, e);
	}
}
}
