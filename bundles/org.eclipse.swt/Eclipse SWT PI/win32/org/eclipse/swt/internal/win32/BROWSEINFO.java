package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class BROWSEINFO {
	public int hwndOwner;
	public int pidlRoot;
	public int pszDisplayName;
	public int lpszTitle;
	public int ulFlags;
	public int lpfn;
	public int lParam;
	public int iImage;
	public static final int sizeof = 32;
}
