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

import org.eclipse.swt.*;
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
	 * @param zoom   the scaling percentage (e.g., 100 = original size, 200 = double size).
	 *  	   This value must be greater zero.
	 * @return the {@link ImageData} for the rasterized image.
	 *
	 * @exception SWTException
	 * <ul>
	 *    <li>ERROR_INVALID_IMAGE - if the SVG cannot be loaded</li>
	 * </ul>
	 * @exception IllegalArgumentException
	 * <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the zoom is less than zero</li>
	 * </ul>
	 */
	public ImageData rasterizeSVG(InputStream stream, int zoom);

	/**
	 * Rasterizes an SVG image from the provided {@code InputStream} into a raster
	 * image of the specified width and height.
	 *
	 * @param stream the SVG image as an {@link InputStream}.
	 * @param width  the width of the rasterized image in pixels (must be positive).
	 * @param height the height of the rasterized image in pixels (must be positive).
	 * @return the {@link ImageData} for the rasterized image.
	 *
	 * @exception SWTException
	 * <ul>
	 *    <li>ERROR_INVALID_IMAGE - if the SVG cannot be loaded</li>
	 * </ul>
	 * @exception IllegalArgumentException
	 * <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the width or height is less than zero</li>
	 * </ul>
	 */
	public ImageData rasterizeSVG(InputStream stream, int width, int height);
}
