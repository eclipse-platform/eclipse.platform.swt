package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class SHMENUBARINFO {
	public int cbSize;
	public int hwndParent;
	public int dwFlags;
	public int nToolBarId;
	public int hInstRes;
	public int nBmpId;
	public int cBmpImages;
	public int hwndMB;
	public static final int sizeof = 32;
}