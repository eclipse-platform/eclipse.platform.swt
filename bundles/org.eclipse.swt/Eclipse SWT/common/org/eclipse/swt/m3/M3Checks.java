/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.m3;

import org.eclipse.swt.*;

/* Argument checks of the M3 sort and filter classes, in one place (SWT.error idiom). */
final class M3Checks {

	private M3Checks () {
	}

	static <T> T nonNull (T value) {
		if (value == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		return value;
	}

	static int range (int index, int size) {
		if (index < 0 || index >= size) SWT.error (SWT.ERROR_INVALID_RANGE);
		return index;
	}

	static void span (int start, int count, int size) {
		if (start < 0 || count < 0 || start > size - count) SWT.error (SWT.ERROR_INVALID_RANGE);
	}

	static void argument (boolean valid) {
		if (!valid) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
}
