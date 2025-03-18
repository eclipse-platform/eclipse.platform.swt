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
import java.util.List;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.DPIUtil.*;
import org.eclipse.swt.widgets.*;

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
		List<ElementAtZoom<ImageData>> loadFromByteStream(int fileZoom, int targetZoom, int flag) {
			switch (flag) {
				case SWT.IMAGE_COPY: {
					return Arrays.stream(loadFromByteStream()).map(d -> new ElementAtZoom<>(d, fileZoom)).toList();
				}
				case SWT.IMAGE_DISABLE: {
					ImageData originalData = loadFromByteStream()[0];
					ImageData data = applyDisableImageData(originalData, originalData.width, originalData.height);
					return List.of(new ElementAtZoom<>(data, fileZoom));
				}
				case SWT.IMAGE_GRAY: {
					ImageData originalData = loadFromByteStream()[0];
					ImageData data = applyGrayImageData(originalData, originalData.width, originalData.height);
					return List.of(new ElementAtZoom<>(data, fileZoom));
				} default:
				throw new IllegalArgumentException("Unexpected value: " + flag);
			}

		}

		private ImageData applyDisableImageData(ImageData data, int height, int width) {
			PaletteData palette = data.palette;
			RGB[] rgbs = new RGB[3];
			rgbs[0] = Display.getDefault().getSystemColor(SWT.COLOR_BLACK).getRGB();
			rgbs[1] = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW).getRGB();
			rgbs[2] = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND).getRGB();
			ImageData newData = new ImageData(width, height, 8, new PaletteData(rgbs));
			newData.alpha = data.alpha;
			newData.alphaData = data.alphaData;
			newData.maskData = data.maskData;
			newData.maskPad = data.maskPad;
			if (data.transparentPixel != -1) newData.transparentPixel = 0;

			/* Convert the pixels. */
			int[] scanline = new int[width];
			int[] maskScanline = null;
			ImageData mask = null;
			if (data.maskData != null) mask = data.getTransparencyMask();
			if (mask != null) maskScanline = new int[width];
			int redMask = palette.redMask;
			int greenMask = palette.greenMask;
			int blueMask = palette.blueMask;
			int redShift = palette.redShift;
			int greenShift = palette.greenShift;
			int blueShift = palette.blueShift;
			for (int y=0; y<height; y++) {
				int offset = y * newData.bytesPerLine;
				data.getPixels(0, y, width, scanline, 0);
				if (mask != null) mask.getPixels(0, y, width, maskScanline, 0);
				for (int x=0; x<width; x++) {
					int pixel = scanline[x];
					if (!((data.transparentPixel != -1 && pixel == data.transparentPixel) || (mask != null && maskScanline[x] == 0))) {
						int red, green, blue;
						if (palette.isDirect) {
							red = pixel & redMask;
							red = (redShift < 0) ? red >>> -redShift : red << redShift;
							green = pixel & greenMask;
							green = (greenShift < 0) ? green >>> -greenShift : green << greenShift;
							blue = pixel & blueMask;
							blue = (blueShift < 0) ? blue >>> -blueShift : blue << blueShift;
						} else {
							red = palette.colors[pixel].red;
							green = palette.colors[pixel].green;
							blue = palette.colors[pixel].blue;
						}
						int intensity = red * red + green * green + blue * blue;
						if (intensity < 98304) {
							newData.data[offset] = (byte)1;
						} else {
							newData.data[offset] = (byte)2;
						}
					}
					offset++;
				}
			}
			return newData;
		}

		private ImageData applyGrayImageData(ImageData data, int pHeight, int pWidth) {
			PaletteData palette = data.palette;
			ImageData newData = data;
			if (!palette.isDirect) {
				/* Convert the palette entries to gray. */
				RGB [] rgbs = palette.getRGBs();
				for (int i=0; i<rgbs.length; i++) {
					if (data.transparentPixel != i) {
						RGB color = rgbs [i];
						int red = color.red;
						int green = color.green;
						int blue = color.blue;
						int intensity = (red+red+green+green+green+green+green+blue) >> 3;
						color.red = color.green = color.blue = intensity;
					}
				}
				newData.palette = new PaletteData(rgbs);
			} else {
				/* Create a 8 bit depth image data with a gray palette. */
				RGB[] rgbs = new RGB[256];
				for (int i=0; i<rgbs.length; i++) {
					rgbs[i] = new RGB(i, i, i);
				}
				newData = new ImageData(pWidth, pHeight, 8, new PaletteData(rgbs));
				newData.alpha = data.alpha;
				newData.alphaData = data.alphaData;
				newData.maskData = data.maskData;
				newData.maskPad = data.maskPad;
				if (data.transparentPixel != -1) newData.transparentPixel = 254;

				/* Convert the pixels. */
				int[] scanline = new int[pWidth];
				int redMask = palette.redMask;
				int greenMask = palette.greenMask;
				int blueMask = palette.blueMask;
				int redShift = palette.redShift;
				int greenShift = palette.greenShift;
				int blueShift = palette.blueShift;
				for (int y=0; y<pHeight; y++) {
					int offset = y * newData.bytesPerLine;
					data.getPixels(0, y, pWidth, scanline, 0);
					for (int x=0; x<pWidth; x++) {
						int pixel = scanline[x];
						if (pixel != data.transparentPixel) {
							int red = pixel & redMask;
							red = (redShift < 0) ? red >>> -redShift : red << redShift;
							int green = pixel & greenMask;
							green = (greenShift < 0) ? green >>> -greenShift : green << greenShift;
							int blue = pixel & blueMask;
							blue = (blueShift < 0) ? blue >>> -blueShift : blue << blueShift;
							int intensity = (red+red+green+green+green+green+green+blue) >> 3;
							if (newData.transparentPixel == intensity) intensity = 255;
							newData.data[offset] = (byte)intensity;
						} else {
							newData.data[offset] = (byte)254;
						}
						offset++;
					}
				}
			}
			return newData;
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
	abstract List<ElementAtZoom<ImageData>> loadFromByteStream(int fileZoom, int targetZoom, int flag);

/**
 * Read the specified input stream, and return the
 * device independent image array represented by the stream.
 */
public List<ElementAtZoom<ImageData>> loadFromStream(LEDataInputStream stream, int fileZoom, int targetZoom, int flag) {
	try {
		inputStream = stream;
		return loadFromByteStream(fileZoom, targetZoom, flag);
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
public static List<ElementAtZoom<ImageData>> load(InputStream is, ImageLoader loader, int fileZoom, int targetZoom, int flag) {
	LEDataInputStream stream = new LEDataInputStream(is);
	FileFormat fileFormat = determineFileFormat(stream).orElseGet(() -> {
		SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT);
		return null;
	});
	fileFormat.loader = loader;
	return fileFormat.loadFromStream(stream, fileZoom, targetZoom, flag);
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
