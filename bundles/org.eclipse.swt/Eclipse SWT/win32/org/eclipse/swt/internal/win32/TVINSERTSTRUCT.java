package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class TVINSERTSTRUCT {
	public int hParent;
	public int hInsertAfter;
//	public TVITEM item;
	public int mask;
	public int hItem;
	public int state;
	public int stateMask;
	public int pszText;
  	public int cchTextMax;
  	public int iImage;
  	public int iSelectedImage;
	public int cChildren;
	public int lParam;
	public static final int sizeof = 48;
}
