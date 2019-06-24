/*******************************************************************************
 * Copyright (c) 2019 Paul Pazderski and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Paul Pazderski - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32;

import java.util.function.BooleanSupplier;

import org.eclipse.swt.widgets.Display;

public class SwtWin32TestUtil {

/**
 * Dispatch events until <em>condition</em> is <code>true</code> or timeout reached.
 *
 * @param display display to dispatch events for (not <code>null</code>)
 * @param timeoutMs max time in milliseconds to process events
 * @param condition optional condition. If the condition returns <code>true</code> event processing is stopped.
 */
public static void processEvents(Display display, int timeoutMs, BooleanSupplier condition) throws InterruptedException {
	if (condition == null) {
		condition = () -> false;
	}
	long targetTimestamp = System.currentTimeMillis() + timeoutMs;
	while (!condition.getAsBoolean()) {
		if (!display.readAndDispatch()) {
			if (System.currentTimeMillis() < targetTimestamp) {
				Thread.sleep(50);
			} else {
				return;
			}
		}
	}
}
}
