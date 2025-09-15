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

/**
 * Provides an API that is invoked by SWT when an image needs to be drawn at a
 * specified width and height.
 * <p>
 * Client code must implement this interface to supply {@link ImageData} on demand.
 * </p>
 *
 * @since 3.132
 * @noreference This class is still experimental API and might be subject to change.
 */
public interface ImageDataAtSizeProvider extends ImageDataProvider {

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
	 * @since 3.132
	 */
	ImageData getImageData(int width, int height);

}