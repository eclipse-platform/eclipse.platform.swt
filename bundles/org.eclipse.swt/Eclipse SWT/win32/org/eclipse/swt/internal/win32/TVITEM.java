package org.eclipse.swt.internal.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class TVITEM {
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
	public static final int sizeof = 40;
}
