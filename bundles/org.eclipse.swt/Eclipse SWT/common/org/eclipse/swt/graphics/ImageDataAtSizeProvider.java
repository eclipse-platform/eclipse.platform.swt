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
package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.*;

/**
 * An extension of the {@link ImageDataProvider} that is capable of providing
 * image data at a specified size instead of or in addition to a specified zoom.
 * This requires an implementation of the {@link #getImageData(int, int)} method
 * for providing image data at a specified size. Optionally, the
 * {@link #getDefaultSize()} method can be overwritten such that the image data
 * can also be retrieved via a zoom that uses the default size as reference for
 * the 100% version. This serves two use cases:
 * <ol>
 * <li>The image data source is size independent, such that the provider can
 * specify at what size the image data is to be provided. As an example, an SVG
 * image source may be defined at arbitrary size and the implementation of this
 * provider can define at which default size the image is to be used at 100%
 * zoom. To achieve this, the {@link #getImageData(int, int)} has to be
 * implemented for the retrieval of image data at a given size and
 * {@link #getDefaultSize()} has to be overwritten to define the default size at
 * 100% zoom.
 * <li>An image created with this data provider is to be drawn at a custom size
 * (and not a specific zoom). Such a drawing can be performed via
 * {@link GC#drawImage(Image, int, int, int, int)}. For this case,
 * {@link #getDefaultSize()} does not need to be overwritten, as the zoom-based
 * version of the image data is not of interest.
 * </ol>
 *
 * @since 3.132
 */
public interface ImageDataAtSizeProvider extends ImageDataProvider {

	@Override
	default ImageData getImageData(int zoom) {
		Point defaultSize = getDefaultSize();
		if (defaultSize.x == -1 && defaultSize.y == -1) {
			if (zoom == 100) {
				return new ImageData(1, 1, 32, new PaletteData(0xFF0000, 0xFF00, 0xFF));
			}
			return null;
		}
		return getImageData(DPIUtil.pointToPixel(defaultSize.x, zoom), DPIUtil.pointToPixel(defaultSize.y, zoom));
	}

	/**
	 * Returns the default size of the image data returned by this provider. It
	 * conforms to the size of the data that is returned when calling
	 * {@link #getImageData(int)} with a zoom value of 100.
	 *
	 * This method can also return (-1, -1), in which case
	 * {@link #getImageData(int)} will return dummy data of size (1, 1). This
	 * reduces potential overheads for the use case in which image data is only
	 * retrieved size-based via {@link #getImageData(int, int)}.
	 *
	 * @return the default size of the data returned by this provider for 100% zoom
	 */
	default Point getDefaultSize() {
		return new Point(-1, -1);
	}

	/**
	 * Returns the {@link ImageData} for the given width and height.
	 *
	 * <p>
	 * <b>Implementation notes:</b>
	 * </p>
	 * <ul>
	 * <li>Returning <code>null</code> is not permitted. If <code>null</code> is
	 * returned, SWT will throw an exception when loading the image.</li>
	 * <li>The returned {@link ImageData} must match the requested width and height
	 * exactly. Implementations should ensure proper resizing and scaling of the
	 * image based on the height and width requested</li>
	 * </ul>
	 *
	 * @param width  the desired width of the {@link ImageData} to be returned
	 * @param height the desired height of the {@link ImageData} to be returned
	 * @return the {@link ImageData} that exactly matches the requested width and
	 *         height
	 */
	ImageData getImageData(int width, int height);

}