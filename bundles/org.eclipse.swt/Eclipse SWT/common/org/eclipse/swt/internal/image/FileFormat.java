package org.eclipse.swt.internal.image;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import java.io.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public abstract class FileFormat {
	LEDataInputStream inputStream;
	LEDataOutputStream outputStream;
	ImageLoader loader;
byte[] bitInvertData(byte[] data, int startIndex, int endIndex) {
	// Destructively bit invert data in the given byte array.
	for (int i = startIndex; i < endIndex; i++) {
		data[i] = (byte)(255 - data[i - startIndex]);
	}
	return data;
}

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
	if (GIFFileFormat.isGIFFile(stream)) {
		fileFormat = new GIFFileFormat();
	} else if (WinBMPFileFormat.isBMPFile(stream)) {
		fileFormat = new WinBMPFileFormat();
	} else if (WinICOFileFormat.isICOFile(stream)) {
		fileFormat = new WinICOFileFormat();
	} else if (JPEGFileFormat.isJPEGFile(stream)) {
		fileFormat = new JPEGFileFormat();
	} else if (PNGFileFormat.isPNGFile(stream)) {
		fileFormat = new PNGFileFormat();
	} else {
		SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
	}
	fileFormat.loader = loader;
	return fileFormat.loadFromStream(stream);
}
public static void save(OutputStream os, int format, ImageLoader loader) {
	/* We do not currently support writing multi-image files,
	 * so we use the first image data in the loader's array. */
	ImageData data = loader.data[0];
	LEDataOutputStream stream = new LEDataOutputStream(os);
	switch (format) {
		case SWT.IMAGE_BMP:
			WinBMPFileFormat f = new WinBMPFileFormat();
			f.unloadIntoStream(data, stream);
			break;
		case SWT.IMAGE_BMP_RLE:
			f = new WinBMPFileFormat();
			if (data.depth == 8)
				f.compression = 1;
			if (data.depth == 4)
				f.compression = 2;
			f.unloadIntoStream(data, stream);
			break;
		case SWT.IMAGE_GIF:
			GIFFileFormat g = new GIFFileFormat();
			g.unloadIntoStream(data, stream);
			break;
		case SWT.IMAGE_ICO:
			WinICOFileFormat i = new WinICOFileFormat();
			i.unloadIntoStream(data, stream);
			break;
		case SWT.IMAGE_JPEG:
			JPEGFileFormat j = new JPEGFileFormat();
			j.unloadIntoStream(data, stream);
			break;
		case SWT.IMAGE_PNG:
			PNGFileFormat p = new PNGFileFormat();
			p.unloadIntoStream(data, stream);
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
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
