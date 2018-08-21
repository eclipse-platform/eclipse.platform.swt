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

public class SHELLEXECUTEINFO {
	public int cbSize;
	public int fMask;
	/** @field cast=(HWND) */
	public long /*int*/ hwnd;
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpVerb;
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpFile;
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpParameters;
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpDirectory;
	public int nShow;
	/** @field cast=(HINSTANCE) */
	public long /*int*/ hInstApp;
	// Optional members
	/** @field cast=(LPVOID) */
	public long /*int*/ lpIDList;
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpClass;
	/** @field cast=(HKEY) */
	public long /*int*/ hkeyClass;
	public int dwHotKey;
//	union {
//		HANDLE hIcon;
//		HANDLE hMonitor;
//	};
	/** @field cast=(HANDLE) */
	public long /*int*/ hIcon;
	/** @field cast=(HANDLE) */
	public long /*int*/ hProcess;
	public static final int sizeof = OS.SHELLEXECUTEINFO_sizeof ();
}

