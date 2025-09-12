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
 */
public class SVGFileFormat extends FileFormat {

	/** The instance of the registered {@link SVGRasterizer}. */
	private static final SVGRasterizer RASTERIZER = ServiceLoader
			.load(SVGRasterizer.class, SVGFileFormat.class.getClassLoader()).findFirst().orElse(null);

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
			ImageData rasterizedImageData = RASTERIZER.rasterizeSVG(inputStream, 100 * targetZoom / fileZoom);
			return List.of(new ElementAtZoom<>(rasterizedImageData, targetZoom));
		} catch (IOException e) {
			SWT.error(SWT.ERROR_INVALID_IMAGE, e);
			return List.of();
		}
	}

	@Override
	ImageData loadFromByteStreamByTargetSize(int targetWidth, int targetHeight) {
		if (RASTERIZER == null) {
			SWT.error(SWT.ERROR_UNSUPPORTED_FORMAT, null, " [No SVG rasterizer found]");
		}
		if (targetWidth <= 0 || targetHeight <= 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, " [Cannot rasterize SVG for targetWidth or targetHeight <= 0]");
		}
		if (targetHeight <= 0 || targetHeight <= 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, " [Cannot rasterize SVG for targetHeight or targetHeight <= 0]");
		}
		try {
			ImageData rasterizedImageData = RASTERIZER.rasterizeSVG(inputStream, targetWidth, targetHeight);
			return rasterizedImageData;
		} catch (IOException e) {
			SWT.error(SWT.ERROR_INVALID_IMAGE, e);
			return null;
		}
	}

	@Override
	void unloadIntoByteStream(ImageLoader loader) {
		throw new UnsupportedOperationException();
	}
}
