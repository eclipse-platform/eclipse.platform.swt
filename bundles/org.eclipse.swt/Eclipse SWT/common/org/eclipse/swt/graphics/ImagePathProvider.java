/*******************************************************************************
 * Copyright (c) 2018, 2025 IBM Corporation and others.
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
 *     Hannes Wellmann - introduce ImageFileProvider as replacement for ImageFileNameProvider
 *******************************************************************************/
package org.eclipse.swt.graphics;

import java.nio.file.Path;

/**
 * Interface to provide a callback mechanism to get information about images
 * when the application is moved from a low DPI monitor to a high DPI monitor.
 * This provides API which will be called by SWT during the image rendering.
 *
 * This interface needs to be implemented by client code to provide the image
 * information on demand.
 *
 * @since 3.130
 */
@FunctionalInterface
public interface ImagePathProvider {

	/**
	 * Returns the image filePath for the given zoom level.
	 * <p>
	 * If no image is available for a particular zoom level, this method should
	 * return <code>null</code>. For <code>zoom == 100</code>, returning
	 * <code>null</code> is not allowed, and SWT will throw an exception.
	 *
	 * @param zoom The zoom level in % of the standard resolution (which is 1
	 *             physical monitor pixel == 1 SWT logical point). Typically 100,
	 *             150, or 200.
	 * @return the image filePath, or <code>null</code> if <code>zoom != 100</code>
	 *         and no image is available for the given zoom level.
	 * @since 3.129
	 */
	Path getImagePath(int zoom);

}
