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

public class SHELLEXECUTEINFO {
	public int cbSize;
	public int fMask; 
	/** @field cast=(HWND) */
	public int /*long*/ hwnd;
	/** @field cast=(LPCTSTR) */
	public int /*long*/ lpVerb;
	/** @field cast=(LPCTSTR) */
	public int /*long*/ lpFile; 
	/** @field cast=(LPCTSTR) */
	public int /*long*/ lpParameters;
	/** @field cast=(LPCTSTR) */
	public int /*long*/ lpDirectory;
	public int nShow; 
	/** @field cast=(HINSTANCE) */
	public int /*long*/ hInstApp;
	// Optional members
	/** @field cast=(LPVOID) */
	public int /*long*/ lpIDList; 
	/** @field cast=(LPCTSTR) */
	public int /*long*/ lpClass;
	/** @field cast=(HKEY) */
	public int /*long*/ hkeyClass;
	public int dwHotKey;
//	union {
//		HANDLE hIcon;		
//		HANDLE hMonitor;
//	};
	/** @field cast=(HANDLE) */
	public int /*long*/ hIcon;
	/** @field cast=(HANDLE) */
	public int /*long*/ hProcess; 
	public static final int sizeof = OS.SHELLEXECUTEINFO_sizeof ();
}

