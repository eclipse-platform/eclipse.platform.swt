/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class ACTCTX {
	public int cbSize;
	public int dwFlags;
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpSource;
	public short wProcessorArchitecture;
	public short wLangId;
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpAssemblyDirectory;
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpResourceName;
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpApplicationName;
	/** @field cast=(HMODULE) */
	public long /*int*/ hModule;
	public static final int sizeof = OS.ACTCTX_sizeof ();
}
