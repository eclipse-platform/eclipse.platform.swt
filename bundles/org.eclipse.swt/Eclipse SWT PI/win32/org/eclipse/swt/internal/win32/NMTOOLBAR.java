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
	public int dwData;
	public int iString;
	public int cchText;
	public int pszText;
//	RECT rcButton;
	public int left, top, right, bottom;
	/* Note in WinCE.  The field rcButton is not defined. */
	public static final int sizeof = OS.IsWinCE ? 44 : 60;
}
