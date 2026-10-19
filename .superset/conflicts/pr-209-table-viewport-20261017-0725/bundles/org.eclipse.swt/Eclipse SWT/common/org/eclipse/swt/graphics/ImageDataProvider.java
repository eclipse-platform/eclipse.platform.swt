/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
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
package org.eclipse.swt.graphics;

/**
 * Interface to provide a callback mechanism to get information about images
 * when the application is moved from a low DPI monitor to a high DPI monitor.
 * This provides API which will be called by SWT during the image rendering.
 *
 * This interface needs to be implemented by client code to provide the
 * image information on demand.
 *
 * @since 3.104
 */
public interface ImageDataProvider {


	/**
	 * Returns the image data for the given zoom level.
	 *
	 * <ul>
     *   <li>
     *     If no image is available for a particular zoom level, this method should
     *     return <code>null</code>. For <code>zoom == 100</code>, returning
     *     <code>null</code> is not allowed, and SWT will throw an exception.
     *   </li>
     *   <li>
     *     Implementations are expected to return {@link ImageData} that is
     *     <b>linearly scaled</b> with respect to the zoom level.
     *     For example, if <code>getImageData(100)</code> returns an image of
     *     width <code>w</code> and height <code>h</code>, then
     *     <code>getImageData(200)</code> must return an {@link ImageData}
     *     of width <code>2 * w</code> and height <code>2 * h</code>,
     *     if a non-{@code null} result is returned.
     *   </li>
     * </ul>
	 *
	 *
	 * @param zoom
	 *            The zoom level in % of the standard resolution (which is 1
	 *            physical monitor pixel == 1 SWT logical point). Typically 100,
	 *            150, or 200.
	 * @return the linearly scaled image data for the given zoom level,
	 *         or <code>null</code> if <code>zoom != 100</code>
	 *         and no image is available for the given zoom level.
	 * @since 3.104
	 */
	ImageData getImageData (int zoom);

}
