package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class SHELLEXECUTEINFO {
	
	public int cbSize;
	public int fMask; 
	public int hwnd;
	public int lpVerb;
	public int lpFile; 
	public int lpParameters;
	public int lpDirectory;
	public int nShow; 
	public int hInstApp;
	
	// Optional members
	public int lpIDList; 
	public int lpClass;
	public int hkeyClass;
	public int dwHotKey;
//	union {
//		HANDLE hIcon;		
//		HANDLE hMonitor;
//	};
	public int hIcon;
	public int hProcess; 
	
	public static final int sizeof = 60;
}

