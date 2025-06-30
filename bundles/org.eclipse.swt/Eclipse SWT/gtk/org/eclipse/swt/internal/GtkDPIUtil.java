/*******************************************************************************
 * Copyright (c) 2022 IBM Corporation and others.
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
 *     Daniel Kruegler - #420 - [High DPI] "swt.autoScale" should add new "half" option
 *     Yatta Solutions - #131 - Additional methods to specify target zoom directly
 *******************************************************************************/
package org.eclipse.swt.internal;

import org.eclipse.swt.graphics.*;

/**
 * This class hold common constants and utility functions w.r.t. to SWT high DPI
 * functionality.
 * <p>
 * The {@code autoScaleUp(..)} methods convert from API coordinates (in
 * SWT points) to internal high DPI coordinates (in pixels) that interface with
 * native widgets.
 * </p>
 * <p>
 * The {@code autoScaleDown(..)} convert from high DPI pixels to API coordinates
 * (in SWT points).
 * </p>
 *
 * @since 3.105
 */
public class GtkDPIUtil {

	/**
	 * Auto-scale up ImageData to device zoom that is at 100%.
	 */
	public static ImageData autoScaleUp (Device device, final ImageData imageData) {
		return autoScaleImageData(device, imageData, 100);
	}

	/**
	 * Auto-scale ImageData to device zoom that are at given zoom factor.
	 */
	public static ImageData autoScaleImageData (Device device, final ImageData imageData, int imageDataZoomFactor) {
		if (DPIUtil.deviceZoom == imageDataZoomFactor || imageData == null || (device != null && !device.isAutoScalable())) return imageData;
		float scaleFactor = (float) DPIUtil.deviceZoom / imageDataZoomFactor;
		return DPIUtil.autoScaleImageData(device, imageData, scaleFactor);
	}

}
