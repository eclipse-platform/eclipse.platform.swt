package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class DOCINFO {
	public int cbSize; 
	public int lpszDocName; // LPCTSTR
	public int lpszOutput; // LPCTSTR
	public int lpszDatatype;// LPCTSTR
	public int fwType; // DWORD
	public static final int sizeof = 20;
}
