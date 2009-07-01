/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class ACTCTX {
	public int cbSize;
	public int dwFlags;
	/** @field cast=(LPCTSTR) */
	public int /*long*/ lpSource;
	public short wProcessorArchitecture;
	public short wLangId;
	/** @field cast=(LPCTSTR) */
	public int /*long*/ lpAssemblyDirectory;
	/** @field cast=(LPCTSTR) */
	public int /*long*/ lpResourceName;
	/** @field cast=(LPCTSTR) */
	public int /*long*/ lpApplicationName;
	/** @field cast=(HMODULE) */
	public int /*long*/ hModule;
	public static final int sizeof = OS.ACTCTX_sizeof ();
}
