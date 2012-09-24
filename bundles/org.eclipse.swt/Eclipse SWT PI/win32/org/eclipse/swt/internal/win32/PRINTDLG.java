/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class PRINTDLG {
	public int lStructSize; // DWORD
	/** @field cast=(HWND) */
	public long /*int*/ hwndOwner; // HWND
	/** @field cast=(HGLOBAL) */
	public long /*int*/ hDevMode; // HGLOBAL
	/** @field cast=(HGLOBAL) */
	public long /*int*/ hDevNames; // HGLOBAL
	/** @field cast=(HDC) */
	public long /*int*/ hDC; // HDC
	public int Flags; // DWORD
	public short nFromPage; // WORD
	public short nToPage; // WORD
	public short nMinPage; // WORD
	public short nMaxPage; // WORD
	public short nCopies; // WORD
	/** @field cast=(HINSTANCE) */
	public long /*int*/ hInstance; // HINSTANCE
	public long /*int*/ lCustData; // LPARAM
	/** @field cast=(LPPRINTHOOKPROC) */
	public long /*int*/ lpfnPrintHook; // LPPRINTHOOKPROC
	/** @field cast=(LPPRINTHOOKPROC) */
	public long /*int*/ lpfnSetupHook; // LPSETUPHOOKPROC
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpPrintTemplateName; // LPCTSTR
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpSetupTemplateName; // LPCTSTR
	/** @field cast=(HGLOBAL) */
	public long /*int*/ hPrintTemplate; // HGLOBAL
	/** @field cast=(HGLOBAL) */
	public long /*int*/ hSetupTemplate; // HGLOBAL
	public static final int sizeof = OS.PRINTDLG_sizeof ();
}
