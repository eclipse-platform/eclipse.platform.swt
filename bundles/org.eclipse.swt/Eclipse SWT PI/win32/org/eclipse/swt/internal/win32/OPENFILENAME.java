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

public class OPENFILENAME {
	public int lStructSize;
	/** @field cast=(HWND) */
	public long /*int*/ hwndOwner;
	/** @field cast=(HINSTANCE) */
	public long /*int*/ hInstance;
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpstrFilter;
	/** @field cast=(LPTSTR) */
	public long /*int*/ lpstrCustomFilter;
	public int nMaxCustFilter;
	public int nFilterIndex;
	/** @field cast=(LPTSTR) */
	public long /*int*/ lpstrFile;
	public int nMaxFile;
	/** @field cast=(LPTSTR) */
	public long /*int*/ lpstrFileTitle;
	public int nMaxFileTitle;
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpstrInitialDir;
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpstrTitle;
	public int Flags;
	public short nFileOffset;
	public short nFileExtension;
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpstrDefExt;
	public long /*int*/ lCustData;
	/** @field cast=(LPOFNHOOKPROC) */
	public long /*int*/ lpfnHook;
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpTemplateName;
	/** @field cast=(void *),flags=no_wince */
	public long /*int*/ pvReserved;
	/** @field flags=no_wince */
	public int dwReserved;
	/** @field flags=no_wince */
	public int FlagsEx;   
	public static final int sizeof = !OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 0) ? OS.OPENFILENAME_sizeof () : 76;
}
