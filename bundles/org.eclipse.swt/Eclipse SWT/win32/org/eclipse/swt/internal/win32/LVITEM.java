package org.eclipse.swt.internal.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class LVITEM {
	public int mask;
	public int iItem;
	public int iSubItem;
	public int state;
	public int stateMask;
	public int pszText;
	public int cchTextMax;
	public int iImage;
	public int lParam;
	public int iIndent;
	public static final int sizeof = 40;
}
