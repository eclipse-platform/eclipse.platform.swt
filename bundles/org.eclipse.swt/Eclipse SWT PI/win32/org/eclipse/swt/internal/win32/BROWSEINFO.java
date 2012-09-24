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

public class BROWSEINFO {
	/** @field cast=(HWND) */
	public long /*int*/ hwndOwner;
	/** @field cast=(LPCITEMIDLIST) */
	public long /*int*/ pidlRoot;
	/** @field cast=(LPTSTR) */
	public long /*int*/ pszDisplayName;
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpszTitle;
	public int ulFlags;
	/** @field cast=(BFFCALLBACK) */
	public long /*int*/ lpfn;
	public long /*int*/ lParam;
	public int iImage;
	public static final int sizeof = OS.BROWSEINFO_sizeof ();
}
