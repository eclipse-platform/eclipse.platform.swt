/*******************************************************************************
 * Copyright (c) 2000, 2026 IBM Corporation and others.
 * This program and accompanying materials are made available under the terms
 * of the Eclipse Public License 2.0, https://www.eclipse.org/legal/epl-2.0/
 * SPDX-License-Identifier: EPL-2.0
 * Derived from the exact SWT owners recorded in this recipe's source seals.
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.SWT;

/** Shared default operation; native DND lifecycle remains in its platform owners. */
final class StylePolicy {
	private StylePolicy () {}
	static int normalize (int style) {
        if (style == SWT.NONE) {
            return DND.DROP_MOVE;
        }
		return style;
	}
}
