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
package org.eclipse.swt.graphics;

import java.io.*;

/**
 * Defines the interface for an SVG rasterizer, responsible for converting SVG
 * data into rasterized images.
 *
 * @since 3.129
 */
public interface SVGRasterizer {

	/**
	 * Rasterizes an SVG image from the provided byte array, using the specified
	 * zoom factor.
	 *
	 * @param stream        the SVG image as an {@link InputStream}.
	 * @param scalingFactor the scaling ratio e.g. 2.0 for doubled size.
	 * @return the {@link ImageData} for the rasterized image, or
	 *         {@code null} if the input is not a valid SVG file or cannot be
	 *         processed.
	 * @throws IOException if an error occurs while reading the SVG data.
	 */
	public ImageData rasterizeSVG(InputStream stream, float scalingFactor) throws IOException;

	/**
	 * Determines whether the given {@link InputStream} contains a SVG file.
	 *
	 * @param inputStream the input stream to check.
	 * @return {@code true} if the input stream contains SVG content; {@code false}
	 *         otherwise.
	 * @throws IOException              if an error occurs while reading the stream.
	 * @throws IllegalArgumentException if the input stream is {@code null}.
	 */
	public boolean isSVGFile(InputStream inputStream) throws IOException;
}
