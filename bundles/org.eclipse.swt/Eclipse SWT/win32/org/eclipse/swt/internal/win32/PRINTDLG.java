package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class PRINTDLG {
	public int lStructSize; // DWORD
	public int hwndOwner; // HWND
	public int hDevMode; // HGLOBAL
	public int hDevNames; // HGLOBAL
	public int hDC; // HDC
	public int Flags; // DWORD
	public short nFromPage; // WORD
	public short nToPage; // WORD
	public short nMinPage; // WORD
	public short nMaxPage; // WORD
	public short nCopies; // WORD
	public int hInstance; // HINSTANCE
	public int lCustData; // LPARAM
	public int lpfnPrintHook; // LPPRINTHOOKPROC
	public int lpfnSetupHook; // LPSETUPHOOKPROC
	public int lpPrintTemplateName; // LPCTSTR
	public int lpSetupTemplateName; // LPCTSTR
	public int hPrintTemplate; // HGLOBAL
	public int hSetupTemplate; // HGLOBAL
	public static final int sizeof = 66;
}
