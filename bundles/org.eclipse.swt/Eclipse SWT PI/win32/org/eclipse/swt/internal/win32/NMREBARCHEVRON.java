package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class NMREBARCHEVRON extends NMHDR {
	// NMHDR hdr;
	public int uBand;		// UINT
	public int wID;		// UINT
	public int lParam;	// LPARAM
	// RECT rc;
	public int left, top, right, bottom;
	public int lParamNM;	// LPARAM
	public static int sizeof = 44;
}
