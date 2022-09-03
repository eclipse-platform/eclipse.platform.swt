/*******************************************************************************
 * Copyright (c) 2021 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class OSVERSIONINFOEX {
	public int    dwOSVersionInfoSize;
	public int    dwMajorVersion;
	public int    dwMinorVersion;
	public int    dwBuildNumber;
	public int    dwPlatformId;
	public char[] szCSDVersion = new char[128];
	/** @field cast=(WORD) */
	public int    wServicePackMajor;
	/** @field cast=(WORD) */
	public int    wServicePackMinor;
	/** @field cast=(WORD) */
	public int    wSuiteMask;
	/** @field cast=(BYTE) */
	public int    wProductType;
	/** @field cast=(BYTE) */
	public int    wReserved;

	public static final int sizeof = OS.OSVERSIONINFOEX_sizeof ();
}
