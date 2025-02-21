/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Michael Bangas (Vector Informatik GmbH) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.image;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.image.FileFormat.*;

public class SVGFileFormat extends DynamicImageFileFormat {

	/** The instance of the registered {@link SVGRasterizer}. */
	private static final SVGRasterizer RASTERIZER = ServiceLoader.load(SVGRasterizer.class).findFirst().orElse(null);

	@Override
	boolean isFileFormat(LEDataInputStream stream) throws IOException {
		byte[] firstBytes = new byte[5];
	    int bytesRead = stream.read(firstBytes);
	    stream.unread(firstBytes);
	    String header = new String(firstBytes, 0, bytesRead, StandardCharsets.UTF_8).trim();
	    return header.startsWith("<?xml") || header.startsWith("<svg");
	}

	@Override
	ImageData[] loadFromByteStream(int targetZoom) {
		try {
			if (RASTERIZER != null && targetZoom != 0) {
				return RASTERIZER.rasterizeSVG(inputStream, targetZoom);
			}
		} catch (IOException e) {
			SWT.error(SWT.ERROR_INVALID_IMAGE, e);
		}
		return null;
	}

	@Override
	void unloadIntoByteStream(ImageLoader loader) {
		throw new UnsupportedOperationException();
	}
}