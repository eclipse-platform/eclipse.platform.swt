package org.eclipse.swt.internal.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class SCROLLINFO {
	public int cbSize;
	public int fMask;
	public int nMin;
	public int nMax;
	public int nPage;
	public int nPos;
	public int nTrackPos;
	public static final int sizeof = 28;
}
