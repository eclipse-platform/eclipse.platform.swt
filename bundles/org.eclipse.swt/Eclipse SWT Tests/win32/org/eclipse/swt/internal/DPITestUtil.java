/*******************************************************************************
 * Copyright (c) 2024 Yatta Solutions
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal;

public final class DPITestUtil {
	private DPITestUtil() {
	}

	public static void setAutoScaleOnRunTime(boolean value) {
		DPIUtil.setAutoScaleOnRuntimeActive(value);
	}

	public static boolean isAutoScaleOnRuntimeActive() {
		return DPIUtil.isAutoScaleOnRuntimeActive();
	}
}
