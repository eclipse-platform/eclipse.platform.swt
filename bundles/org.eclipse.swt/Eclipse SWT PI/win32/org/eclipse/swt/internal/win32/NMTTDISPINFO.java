package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class NMTTDISPINFO extends NMHDR {
	public int lpszText;
//  char szText[80];
//	public char [] szText = new char [80];
	public int hinst;   
	public int uFlags;
	public int lParam;
	public static final int sizeof = OS.IsUnicode ? 188 : 108;
}
