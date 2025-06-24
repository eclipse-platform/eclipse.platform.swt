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

import java.util.function.*;

import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.internal.win32.version.*;

/**
 * This class is used in the win32 implementation only to provide
 * DPI related utility methods.
 * <p>
 * <b>IMPORTANT:</b> This class is <em>not</em> part of the public
 * API for SWT. It is marked public only so that it can be shared
 * within the packages provided by SWT. It is not available on all
 * platforms, and should never be called from application code.
 * </p>
 * @noreference This class is not intended to be referenced by clients
 */
public class Win32DPIUtils {
	public static boolean setDPIAwareness(int desiredDpiAwareness) {
		if (desiredDpiAwareness == OS.GetThreadDpiAwarenessContext()) {
			return true;
		}
		if (desiredDpiAwareness == OS.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2) {
			// "Per Monitor V2" only available in more recent Windows version
			boolean perMonitorV2Available = OsVersion.IS_WIN10_1809;
			if (!perMonitorV2Available) {
				System.err.println("***WARNING: the OS version does not support DPI awareness mode PerMonitorV2.");
				return false;
			}
		}
		long setDpiAwarenessResult = OS.SetThreadDpiAwarenessContext(desiredDpiAwareness);
		if (setDpiAwarenessResult == 0L) {
			System.err.println("***WARNING: setting DPI awareness failed.");
			return false;
		}
		return true;
	}

	public static <T> T runWithProperDPIAwareness(Supplier<T> operation) {
		// refreshing is only necessary, when monitor specific scaling is active
		long previousDPIAwareness = OS.GetThreadDpiAwarenessContext();
		try {
			if (!setDPIAwareness(OS.DPI_AWARENESS_CONTEXT_PER_MONITOR_AWARE_V2)) {
				// awareness was not changed, so no need to reset it
				previousDPIAwareness = 0;
			}
			return operation.get();
		} finally {
			if (previousDPIAwareness > 0) {
				OS.SetThreadDpiAwarenessContext(previousDPIAwareness);
			}
		}
	}
}
