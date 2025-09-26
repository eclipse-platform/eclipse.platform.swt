/*******************************************************************************
 * Copyright (c) 2025 Yatta Solutions and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta Solutions - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

import org.eclipse.swt.graphics.*;

public class GtkDPIUtil {
	static {
	    DPIUtil.setUseSmoothScalingByDefaultProvider(() -> {
	        return DPIUtil.getDeviceZoom() / 100 * 100 != DPIUtil.getDeviceZoom();
	    });
	}

	/**
	 * Auto-scale up ImageData to device zoom that is at 100%.
	 */
	public static ImageData pointToPixel (Device device, final ImageData imageData) {
		int imageDataZoomFactor = 100;
		if (DPIUtil.getDeviceZoom() == imageDataZoomFactor || imageData == null || (device != null && !device.isAutoScalable())) return imageData;
		float scaleFactor = (float) DPIUtil.getDeviceZoom() / imageDataZoomFactor;
		return DPIUtil.autoScaleImageData(device, imageData, scaleFactor);
	}
}
