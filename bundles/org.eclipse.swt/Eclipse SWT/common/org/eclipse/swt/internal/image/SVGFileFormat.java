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
 * <p>
 * The color resolving {@code currentColor} is global for all rasterizations,
 * defaults to black and can be set via {@link #setCurrentColor(RGB)} or the
 * system property {@code swt.svg.currentColor} as {@code #RRGGBB}.
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

	private static final RGB INITIAL_CURRENT_COLOR = readCurrentColorProperty();

	/** Volatile, as it may be set and read from different display threads. */
	private static volatile RGB currentColor = INITIAL_CURRENT_COLOR;

	/**
	 * Sets the color resolving {@code currentColor}. Images rasterize lazily per
	 * zoom, so set it before creating images that should use it.
	 *
	 * @param color the color to use, or {@code null} to restore the initial value
	 */
	public static void setCurrentColor(RGB color) {
		currentColor = color != null ? color : INITIAL_CURRENT_COLOR;
	}

	/**
	 * Returns the color resolving {@code currentColor}, never {@code null}.
	 */
	public static RGB getCurrentColor() {
		return currentColor;
	}

	private static RGB readCurrentColorProperty() {
		RGB black = new RGB(0, 0, 0);
		String value = System.getProperty("swt.svg.currentColor");
		if (value == null || !value.trim().matches("#?[0-9A-Fa-f]{6}")) {
			return black;
		}
		int rgb = Integer.parseInt(value.trim().replaceFirst("^#", ""), 16);
		return new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
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
		ImageData rasterizedImageData = RASTERIZER.rasterizeSVG(inputStream, 100 * targetZoom / fileZoom, currentColor);
		return List.of(new ElementAtZoom<>(rasterizedImageData, targetZoom));
	}

	@Override
	ImageData loadFromByteStreamBySize(int width, int height) {
		if (RASTERIZER == null) {
			SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT, null, " [No SVG rasterizer found]");
		}
		if (width <= 0 || height <= 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, " [Cannot rasterize SVG for width or height <= 0]");
		}
		ImageData rasterizedImageData = RASTERIZER.rasterizeSVG(inputStream, width, height, currentColor);
		return rasterizedImageData;
	}

	@Override
	void unloadIntoByteStream(ImageLoader loader) {
		throw new UnsupportedOperationException();
	}
}
