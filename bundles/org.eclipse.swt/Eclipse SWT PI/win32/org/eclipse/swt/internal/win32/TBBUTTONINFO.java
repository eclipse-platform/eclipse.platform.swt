package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class TBBUTTONINFO {
	public int cbSize;
	public int dwMask;
	public int idCommand;
	public int iImage;
	public byte fsState;
	public byte fsStyle;
	public short cx;
	public int lParam;
	public int pszText;
	public int cchText;
	public static final int sizeof = 32;
}
