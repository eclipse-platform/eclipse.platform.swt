package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class NMTOOLBAR extends NMHDR {
	public int iItem;
//	TBBUTTON tbButton;
	public int iBitmap;
	public int idCommand;
	public byte fsState;
	public byte fsStyle;
	public short ___MISSING_ALIGNMENT__; 
	public int dwData;
	public int iString;
	public int cchText;
	public int pszText;
//	RECT rcButton;
	public int left, top, right, bottom;
	public static final int sizeof = 60;
}
