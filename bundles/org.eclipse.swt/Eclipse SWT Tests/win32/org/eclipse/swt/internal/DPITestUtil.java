/*******************************************************************************
 * Copyright (c) 2024 Yatta Solutions
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

import org.eclipse.swt.widgets.*;

public final class DPITestUtil {

	private DPITestUtil() {
	}

	public static void changeDPIZoom (Shell shell, int nativeZoom) {
		DPIUtil.setDeviceZoom(nativeZoom);
		float scalingFactor = 1f * DPIUtil.getZoomForAutoscaleProperty(nativeZoom) / DPIUtil.getZoomForAutoscaleProperty(shell.nativeZoom);
		DPIZoomChangeRegistry.applyChange(shell, nativeZoom, scalingFactor);
	}

}
