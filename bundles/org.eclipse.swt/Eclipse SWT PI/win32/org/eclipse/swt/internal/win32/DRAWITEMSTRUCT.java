package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class DRAWITEMSTRUCT {
	public int CtlType;
	public int CtlID;
	public int itemID;
	public int itemAction;
	public int itemState;
	public int hwndItem;
	public int hDC;
// 	public RECT rcItem;
	public int left, top, bottom, right;
	public int itemData;
	public static final int sizeof = 48;
}
