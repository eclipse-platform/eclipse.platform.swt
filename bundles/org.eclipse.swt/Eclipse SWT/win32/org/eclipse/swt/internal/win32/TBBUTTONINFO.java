package org.eclipse.swt.internal.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
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
