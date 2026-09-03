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
		waitForDPIChange(shell, (DPIChangeExecution) event.data);
	}

	/**
	 * Performs a zoom change like the operating system does when the shell is moved
	 * to a monitor with a different scaling, i.e. by processing the zoom change for
	 * the shell instead of sending the zoom changed event to it.
	 */
	public static void changeDPIZoomOnMonitorChange (Shell shell, int nativeZoom) {
		shell.handleMonitorSpecificDpiChange(nativeZoom, shell.getBoundsInPixels());
		waitForDPIChange(shell, (DPIChangeExecution) shell.lastDpiChangeEvent.data);
	}

	private static void waitForDPIChange(Shell shell, DPIChangeExecution execution) {
		final Instant timeOut = Instant.now().plusMillis(TIMEOUT_MILLIS);
		final Display display = shell == null ? Display.getDefault() : shell.getDisplay();

		while (Instant.now().isBefore(timeOut) && !execution.isComplete()) {
			if (!display.isDisposed()) {
				display.readAndDispatch();
			}
		}
	}
}
