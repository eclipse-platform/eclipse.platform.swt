package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class MSG {
	public int hwnd;     
	public int message; 
	public int wParam; 
	public int lParam; 
	public int time; 
//	public POINT pt;
	public int x, y;
	public static final int sizeof = 28;
}
