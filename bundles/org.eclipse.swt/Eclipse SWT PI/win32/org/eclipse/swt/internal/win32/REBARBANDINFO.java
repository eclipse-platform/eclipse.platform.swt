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

public class REBARBANDINFO {
	public int cbSize;
	public int fMask;
	public int fStyle;
	public int clrFore;
	public int clrBack;
	/** @field cast=(LPTSTR) */
	public long /*int*/ lpText;
	public int cch;
	public int iImage;
	/** @field cast=(HWND) */
	public long /*int*/ hwndChild;
	public int cxMinChild;
	public int cyMinChild;
	public int cx;
	/** @field cast=(HBITMAP) */
	public long /*int*/ hbmBack;
	public int wID;
	public int cyChild;  
	public int cyMaxChild;
	public int cyIntegral;
	public int cxIdeal;
	public long /*int*/ lParam;
	/** @field flags=no_wince */
	public int cxHeader;
	/* Note in WinCE.  The field cxHeader is not defined. */ 
	public static final int sizeof = OS.REBARBANDINFO_sizeof ();
}
