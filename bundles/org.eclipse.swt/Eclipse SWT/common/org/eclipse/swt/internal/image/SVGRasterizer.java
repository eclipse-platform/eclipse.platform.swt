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

import org.eclipse.swt.graphics.*;

/**
 * Defines the interface for an SVG rasterizer, responsible for converting SVG
 * data into rasterized images.
 */
public interface SVGRasterizer {
	/**
	 * Rasterizes an SVG image from the provided {@code InputStream} using the
	 * specified zoom.
	 *
	 * @param stream the SVG image as an {@link InputStream}.
	 * @param zoom   the scaling factor (in percent) e.g. {@code 200} for doubled
	 *               size. This value must be greater zero.
	 * @return the {@link ImageData} for the rasterized image, or {@code null} if
	 *         the input is not a valid SVG file or cannot be processed.
	 */
	public ImageData rasterizeSVG(InputStream stream, int zoom) throws IOException;
	
	public ImageData rasterizeSVG(InputStream stream, int width, int height) throws IOException;
}
