package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class OPENFILENAME {
	public int lStructSize;
	public int hwndOwner;
	public int hInstance;
	public int lpstrFilter;
	public int lpstrCustomFilter;
	public int nMaxCustFilter;
	public int nFilterIndex;
	public int lpstrFile;
	public int nMaxFile;
	public int lpstrFileTitle;
	public int nMaxFileTitle;
	public int lpstrInitialDir;
	public int lpstrTitle;
	public int Flags;
	public short nFileOffset;
	public short nFileExtension;
	public int lpstrDefExt;
	public int lCustData;
	public int lpfnHook;
	public int lpTemplateName;
	public static final int sizeof = 76;
}
