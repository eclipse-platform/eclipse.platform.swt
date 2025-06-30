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

/**
 * This class hold common constants and utility functions w.r.t. to SWT high DPI
 * functionality.
 * <p>
 * The {@code autoScaleUp(..)} methods convert from API coordinates (in SWT
 * points) to internal high DPI coordinates (in pixels) that interface with
 * native widgets.
 * </p>
 * <p>
 * The {@code autoScaleDown(..)} convert from high DPI pixels to API coordinates
 * (in SWT points).
 * </p>
 *
 * @since 3.105
 */
public class CocoaDPIUtil {

	/**
	 * Auto-scale down int dimensions.
	 */
	public static int autoScaleDown(int size) {
		return DPIUtil.scaleDown(size, DPIUtil.deviceZoom);
	}

	/**
	 * Auto-scale down float dimensions.
	 */
	public static float autoScaleDown(float size) {
		return DPIUtil.scaleDown(size, DPIUtil.deviceZoom);
	}
}
