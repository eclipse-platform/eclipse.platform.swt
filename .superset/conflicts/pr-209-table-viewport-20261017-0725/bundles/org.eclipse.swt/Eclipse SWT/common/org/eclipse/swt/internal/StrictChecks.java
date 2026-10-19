/*******************************************************************************
 * Copyright (c) 2025 Yatta Solutions
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

public final class StrictChecks {

	private static final boolean STRICT_CHECKS_ENABLED = System
			.getProperty("org.eclipse.swt.internal.enableStrictChecks") != null;

	private static boolean temporarilyDisabled = false;

	private StrictChecks() {
	}

	public static void runIfStrictChecksEnabled(Runnable runnable) {
		if (STRICT_CHECKS_ENABLED && !temporarilyDisabled) {
			runnable.run();
		}
	}

	public static void runWithStrictChecksDisabled(Runnable runnable) {
		temporarilyDisabled = true;
		try {
			runnable.run();
		} finally {
			temporarilyDisabled = false;
		}
	}
}
