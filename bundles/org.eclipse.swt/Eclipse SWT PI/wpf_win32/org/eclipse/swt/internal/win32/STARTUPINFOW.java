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

public class STARTUPINFOW {
	public int cb;
	/** @field cast=(LPWSTR) */
	public long /*int*/ lpReserved;
	/** @field cast=(LPWSTR) */
	public long /*int*/ lpDesktop;
	/** @field cast=(LPWSTR) */
	public long /*int*/ lpTitle;
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
	public long /*int*/ lpReserved2;
	/** @field cast=(HANDLE) */
	public long /*int*/ hStdInput;
	/** @field cast=(HANDLE) */
	public long /*int*/ hStdOutput;
	/** @field cast=(HANDLE) */
	public long /*int*/ hStdError;
	public static int sizeof = Win32.STARTUPINFOW_sizeof ();
}
