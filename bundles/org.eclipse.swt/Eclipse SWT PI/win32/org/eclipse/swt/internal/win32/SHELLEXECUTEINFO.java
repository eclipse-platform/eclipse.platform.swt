package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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

