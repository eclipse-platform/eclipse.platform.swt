package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

public class OSVERSIONINFO {
	public int dwOSVersionInfoSize; 
	public int dwMajorVersion;
	public int dwMinorVersion;
	public int dwBuildNumber;
	public int dwPlatformId;
	//TCHAR szCSDVersion [128];
	//public char [] szCSDVersion = new char [128];
	public static final int sizeof = 20 + 128 * TCHAR.sizeof;
}
