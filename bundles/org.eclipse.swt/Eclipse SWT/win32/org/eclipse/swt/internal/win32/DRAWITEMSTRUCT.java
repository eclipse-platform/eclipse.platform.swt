package org.eclipse.swt.internal.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
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
