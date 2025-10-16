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
package org.eclipse.swt.widgets;

import java.time.*;
import java.util.concurrent.atomic.*;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.Control.*;

public final class DPITestUtil {

	private DPITestUtil() {
	}

	private static final int TIMEOUT_MILLIS = 10000;

	public static void changeDPIZoom (Shell shell, int nativeZoom) {
		DPIUtil.setDeviceZoom(nativeZoom);
		Event event = shell.createZoomChangedEvent(nativeZoom, true);
		shell.sendZoomChangedEvent(event, shell);
		DPIChangeExecution data = (DPIChangeExecution) event.data;
		waitForDPIChange(shell, TIMEOUT_MILLIS, data.taskCount);
	}

	private static void waitForDPIChange(Shell shell, int timeout, AtomicInteger scalingCounter) {
		waitForPassCondition(shell, timeout, scalingCounter);
	}

	private static void waitForPassCondition(Shell shell, int timeout, AtomicInteger scalingCounter) {
		final Instant timeOut = Instant.now().plusMillis(timeout);
		final Display display = shell == null ? Display.getDefault() : shell.getDisplay();

		while (Instant.now().isBefore(timeOut) && scalingCounter.get() != 0) {
			if (!display.isDisposed()) {
				display.readAndDispatch();
			}
		}
	}
}
