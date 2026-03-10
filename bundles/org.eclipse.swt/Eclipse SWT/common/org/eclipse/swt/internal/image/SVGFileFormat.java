/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Michael Bangas (Vector Informatik GmbH) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.image;

import java.awt.image.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.DPIUtil.*;

/**
 * A {@link FileFormat} implementation for handling SVG (Scalable Vector
 * Graphics) files.
 * <p>
 * This class detects SVG files based on their header and uses a registered
 * {@link SVGRasterizer} service to rasterize SVG content.
 * </p>
 */
public class SVGFileFormat extends FileFormat {

	/** The instance of the registered {@link SVGRasterizer}. */
	private static final SVGRasterizer RASTERIZER;

	static {
		SVGRasterizer rasterizer = null;
		try {
			rasterizer = ServiceLoader
					.load(SVGRasterizer.class, SVGFileFormat.class.getClassLoader()).findFirst().orElse(null);
		} catch (ServiceConfigurationError e) {
			// rasterizer not in classpath or could not be instantiated
		}
		RASTERIZER = rasterizer;
	}

	@Override
	boolean isFileFormat(LEDataInputStream stream) throws IOException {
		byte[] firstBytes = new byte[5];
		int bytesRead = stream.read(firstBytes);
		stream.unread(firstBytes);
		String header = new String(firstBytes, 0, bytesRead, StandardCharsets.UTF_8).trim();
		return header.startsWith("<?xml") || header.startsWith("<svg");
	}

	@Override
	List<ElementAtZoom<ImageData>> loadFromByteStream(int fileZoom, int targetZoom) {
		if (RASTERIZER == null) {
			SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT, null, " [No SVG rasterizer found]");
		}
		if (targetZoom <= 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, " [Cannot rasterize SVG for zoom <= 0]");
		}
		try {
			ImageData rasterizedImageData = convertToSWTImageData(RASTERIZER.rasterizeSVG(inputStream, 100 * targetZoom / fileZoom));
			return List.of(new ElementAtZoom<>(rasterizedImageData, targetZoom));
		} catch (IllegalArgumentException e) {
			SWT.error(SWT.ERROR_INVALID_IMAGE);
			return null;
		}
	}

	@Override
	ImageData loadFromByteStreamBySize(int width, int height) {
		if (RASTERIZER == null) {
			SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT, null, " [No SVG rasterizer found]");
		}
		if (width <= 0 || height <= 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, " [Cannot rasterize SVG for width or height <= 0]");
		}
		try {
			ImageData rasterizedImageData = convertToSWTImageData(RASTERIZER.rasterizeSVG(inputStream, width, height));
			return rasterizedImageData;
		} catch (IllegalArgumentException e) {
			SWT.error(SWT.ERROR_INVALID_IMAGE);
			return null;
		}
	}

	private ImageData convertToSWTImageData(BufferedImage rasterizedImage) {
		int width = rasterizedImage.getWidth();
		int height = rasterizedImage.getHeight();
		int[] pixels = ((DataBufferInt) rasterizedImage.getRaster().getDataBuffer()).getData();
		PaletteData paletteData = new PaletteData(0xFF0000, 0x00FF00, 0x0000FF);
		ImageData imageData = new ImageData(width, height, 24, paletteData);
		int index = 0;
		for (int y = 0; y < imageData.height; y++) {
			for (int x = 0; x < imageData.width; x++) {
				int alpha = (pixels[index] >> 24) & 0xFF;
				imageData.setAlpha(x, y, alpha);
				imageData.setPixel(x, y, pixels[index++] & 0x00FFFFFF);
			}
		}
		return imageData;
	}

	@Override
	void unloadIntoByteStream(ImageLoader loader) {
		throw new UnsupportedOperationException();
	}
}
