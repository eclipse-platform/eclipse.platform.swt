package org.eclipse.swt.internal.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class TCITEM {
	public int mask;
	public int dwState;
	public int dwStateMask;
	public int pszText;
	public int cchTextMax;
	public int iImage;
	public int lParam;
	public static final int sizeof = 28;
}
