package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class NMTTDISPINFO extends NMHDR {
	public int lpszText;
//  public byte szText[80];
	public int pad0, pad1, pad2, pad3, pad4, pad5, pad6, pad7, pad8, pad9;
	public int pad10, pad11, pad12, pad13, pad14, pad15, pad16, pad17, pad18, pad19;
	public int hinst;   
	public int uFlags;
	public int lParam;
	public static final int sizeof = OS.IsUnicode ? 188 : 108;
}
