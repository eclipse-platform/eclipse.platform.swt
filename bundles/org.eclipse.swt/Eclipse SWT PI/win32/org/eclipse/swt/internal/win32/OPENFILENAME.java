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
	public int /*long*/ hwndOwner;
	/** @field cast=(HINSTANCE) */
	public int /*long*/ hInstance;
	/** @field cast=(LPCTSTR) */
	public int /*long*/ lpstrFilter;
	/** @field cast=(LPTSTR) */
	public int /*long*/ lpstrCustomFilter;
	public int nMaxCustFilter;
	public int nFilterIndex;
	/** @field cast=(LPTSTR) */
	public int /*long*/ lpstrFile;
	public int nMaxFile;
	/** @field cast=(LPTSTR) */
	public int /*long*/ lpstrFileTitle;
	public int nMaxFileTitle;
	/** @field cast=(LPCTSTR) */
	public int /*long*/ lpstrInitialDir;
	/** @field cast=(LPCTSTR) */
	public int /*long*/ lpstrTitle;
	public int Flags;
	public short nFileOffset;
	public short nFileExtension;
	/** @field cast=(LPCTSTR) */
	public int /*long*/ lpstrDefExt;
	public int /*long*/ lCustData;
	/** @field cast=(LPOFNHOOKPROC) */
	public int /*long*/ lpfnHook;
	/** @field cast=(LPCTSTR) */
	public int /*long*/ lpTemplateName;
	/** @field cast=(void *),flags=no_wince */
	public int /*long*/ pvReserved;
	/** @field flags=no_wince */
	public int dwReserved;
	/** @field flags=no_wince */
	public int FlagsEx;   
	public static final int sizeof = !OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 0) ? OS.OPENFILENAME_sizeof () : 76;
}
