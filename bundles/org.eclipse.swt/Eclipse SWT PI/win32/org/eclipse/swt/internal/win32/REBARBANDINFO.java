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

public class REBARBANDINFO {
	public int cbSize;
	public int fMask;
	public int fStyle;
	public int clrFore;
	public int clrBack;
	/** @field cast=(LPTSTR) */
	public long lpText;
	public int cch;
	public int iImage;
	/** @field cast=(HWND) */
	public long hwndChild;
	public int cxMinChild;
	public int cyMinChild;
	public int cx;
	/** @field cast=(HBITMAP) */
	public long hbmBack;
	public int wID;
	public int cyChild;
	public int cyMaxChild;
	public int cyIntegral;
	public int cxIdeal;
	public long lParam;
	public int cxHeader;
	public static final int sizeof = OS.REBARBANDINFO_sizeof ();
}
