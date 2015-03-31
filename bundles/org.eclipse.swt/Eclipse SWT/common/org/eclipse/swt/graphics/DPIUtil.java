/*******************************************************************************
 * Copyright (c) 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.*;

/**
 * This class hold common constants and utility functions w.r.t. to SWT high DPI
 * functionality.
 *
 * @since 3.104
 */
class DPIUtil {

	/* DPI Constants */
	static final int DPI_ZOOM_200 = 192;
	static final int DPI_ZOOM_150 = 144;

	/**
	 * Compute the zoom value based on the DPI value.
	 *
	 * @return zoom
	 */
	static int mapDPIToZoom (int dpi) {
		int zoom;
		if (dpi >= DPI_ZOOM_200) {
			zoom = 200;
		} else if (dpi >= DPI_ZOOM_150) {
			zoom = 150;
		} else {
			zoom = 100;
		}
		return zoom;
	}
	
	/**
	 * Gets Image file path at specified zoom level, if image is missing then
	 * fall-back to 100% image. If provider or fall-back image is not available,
	 * throw error.
	 */
	static String validateAndGetImagePathAtZoom (ImageFileNameProvider provider, int zoom, boolean[] found) {
		if (provider == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		String filename = provider.getImagePath (zoom);
		found [0] = (filename != null);
		/* If image is null when (zoom != 100%), fall-back to image at 100% zoom */
		if (zoom != 100 && !found [0]) filename = provider.getImagePath (100);
		if (filename == null) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		return filename;
	}
	
	/**
	 * Gets Image data at specified zoom level, if image is missing then
	 * fall-back to 100% image. If provider or fall-back image is not available,
	 * throw error.
	 */
	static ImageData validateAndGetImageDataAtZoom (ImageDataProvider provider, int zoom, boolean[] found) {
		if (provider == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		ImageData data = provider.getImageData (zoom);
		found [0] = (data != null);
		/* If image is null when (zoom != 100%), fall-back to image at 100% zoom */
		if (zoom != 100 && !found [0]) data = provider.getImageData (100);
		if (data == null) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		return data;
	}
}
