package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class TOOLINFO {
	public int cbSize; 
	public int uFlags;
	public int hwnd; 
	public int uId; 
//	public RECT rect;
	public int left, top, right, bottom;
	public int hinst; 
	public int lpszText;
	public int lParam;
	public static int sizeof = 44;
}
