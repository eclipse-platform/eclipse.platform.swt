package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
