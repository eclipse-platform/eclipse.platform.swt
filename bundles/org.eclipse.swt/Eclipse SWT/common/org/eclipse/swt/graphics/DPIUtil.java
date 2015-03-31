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

import java.util.*;

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
	 * Gets Map of image file path at specified zoom level and boolean flag
	 * as TRUE if expected image at zoom was found.
	 */
	@SuppressWarnings("unchecked")
	static Map<String, Boolean> getImagePathAtZoom (ImageFileNameProvider provider, int zoom) {
		if (provider == null) return Collections.EMPTY_MAP;
		String filename = provider.getImagePath (zoom);
		/* If image is null when (zoom != 100%), fall-back to image at 100% zoom */
		if (zoom != 100 && filename == null) return Collections.singletonMap (provider.getImagePath (100), Boolean.FALSE);
		return Collections.singletonMap (filename, Boolean.TRUE);
	}
	
	/**
	 * Gets Map of Image data at specified zoom level and boolean flag
	 * as TRUE if expected image at zoom was found.
	 */
	@SuppressWarnings("unchecked")
	static Map<ImageData, Boolean> getImageDataAtZoom (ImageDataProvider provider, int zoom) {
		if (provider == null) return Collections.EMPTY_MAP;
		ImageData data = provider.getImageData (zoom);
		/* If image is null when (zoom != 100%), fall-back to image at 100% zoom */
		if (zoom != 100 && data == null) return Collections.singletonMap (provider.getImageData (100), Boolean.FALSE);
		return Collections.singletonMap (data, Boolean.TRUE);
	}
}
