/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class OPENFILENAME {
	public int lStructSize;
	/** @field cast=(HWND) */
	public long hwndOwner;
	/** @field cast=(HINSTANCE) */
	public long hInstance;
	/** @field cast=(LPCTSTR) */
	public long lpstrFilter;
	/** @field cast=(LPTSTR) */
	public long lpstrCustomFilter;
	public int nMaxCustFilter;
	public int nFilterIndex;
	/** @field cast=(LPTSTR) */
	public long lpstrFile;
	public int nMaxFile;
	/** @field cast=(LPTSTR) */
	public long lpstrFileTitle;
	public int nMaxFileTitle;
	/** @field cast=(LPCTSTR) */
	public long lpstrInitialDir;
	/** @field cast=(LPCTSTR) */
	public long lpstrTitle;
	public int Flags;
	public short nFileOffset;
	public short nFileExtension;
	/** @field cast=(LPCTSTR) */
	public long lpstrDefExt;
	public long lCustData;
	/** @field cast=(LPOFNHOOKPROC) */
	public long lpfnHook;
	/** @field cast=(LPCTSTR) */
	public long lpTemplateName;
	/** @field cast=(void *) */
	public long pvReserved;
	public int dwReserved;
	public int FlagsEx;
	public static final int sizeof = OS.OPENFILENAME_sizeof ();
}
