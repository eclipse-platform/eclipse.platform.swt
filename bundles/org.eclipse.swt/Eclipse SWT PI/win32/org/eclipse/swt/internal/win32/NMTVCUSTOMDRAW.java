package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
public class NMTVCUSTOMDRAW extends NMCUSTOMDRAW {
	public int clrText;
	public int clrTextBk;
	public int iLevel; // the iLevel field does not appear on WinCE
	public static final int sizeof = OS.IsWinCE ? 56 : 60;
}