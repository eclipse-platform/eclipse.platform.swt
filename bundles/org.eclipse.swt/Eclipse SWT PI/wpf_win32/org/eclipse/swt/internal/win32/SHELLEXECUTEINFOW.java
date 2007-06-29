/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class SHELLEXECUTEINFOW {
	public int cbSize;
	public int fMask; 
	public int /*long*/ hwnd;
	public int /*long*/ lpVerb;
	public int lpFile;
	public int /*long*/ lpParameters;
	public int /*long*/ lpDirectory;
	public int nShow; 
	public int /*long*/ hInstApp;
	// Optional members
	public int /*long*/ lpIDList; 
	public int /*long*/ lpClass;
	public int /*long*/ hkeyClass;
	public int dwHotKey;
//	union {
//		HANDLE hIcon;		
//		HANDLE hMonitor;
//	};
	public int /*long*/ hIcon;
	public int /*long*/ hProcess; 
	public static final int sizeof = Win32.SHELLEXECUTEINFOW_sizeof ();
}

