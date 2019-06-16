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

public class STARTUPINFO {
	public int cb;
	/** @field cast=(LPTSTR) */
	public long lpReserved;
	/** @field cast=(LPTSTR) */
	public long lpDesktop;
	/** @field cast=(LPTSTR) */
	public long lpTitle;
	public int dwX;
	public int dwY;
	public int dwXSize;
	public int dwYSize;
	public int dwXCountChars;
	public int dwYCountChars;
	public int dwFillAttribute;
	public int dwFlags;
	public short wShowWindow;
	public short cbReserved2;
	/** @field cast=(LPBYTE) */
	public long lpReserved2;
	/** @field cast=(HANDLE) */
	public long hStdInput;
	/** @field cast=(HANDLE) */
	public long hStdOutput;
	/** @field cast=(HANDLE) */
	public long hStdError;
	public static int sizeof = OS.STARTUPINFO_sizeof ();
}
