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
package org.eclipse.swt.internal.win32;

/**
 * @see <a href="https://docs.microsoft.com/en-us/windows/desktop/api/shlobj_core/ns-shlobj_core-_ida">WinAPI documentation: CIDA</a>
 */
public class CIDA {
	public int cidl;
	/** @field accessor=aoffset[0] */
	public int aoffset;
	// minimum size since the actual size depends on the number of offsets
	public static final int sizeof = OS.CIDA_sizeof ();
}
