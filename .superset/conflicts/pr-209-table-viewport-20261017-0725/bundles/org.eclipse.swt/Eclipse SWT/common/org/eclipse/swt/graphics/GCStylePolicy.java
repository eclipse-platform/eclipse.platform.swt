/*******************************************************************************
 * Copyright (c) 2000, 2026 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-2.0/
 * SPDX-License-Identifier: EPL-2.0
 *
 * Derived from the exact SWT style owners recorded in the recipe provenance.
 * Original platform policy, constant aliasing and bit precedence are retained.
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.SWT;

/** Shared GC direction normalization; no native or graphics resources. */
final class GCStylePolicy {
	private GCStylePolicy () {
	}

	static int normalize (int style) {
        if ((style & SWT.LEFT_TO_RIGHT) != 0) {
            style &= ~SWT.RIGHT_TO_LEFT;
        }
		return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
	}
}
