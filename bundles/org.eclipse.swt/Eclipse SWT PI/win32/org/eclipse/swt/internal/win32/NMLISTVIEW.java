package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class NMLISTVIEW extends NMHDR {
	public int iItem;
	public int iSubItem;
	public int uNewState;
	public int uOldState;
	public int uChanged;
//	public POINT ptAction;
	public int x, y;
	public int lParam;
	public static int sizeof = 44;
}
