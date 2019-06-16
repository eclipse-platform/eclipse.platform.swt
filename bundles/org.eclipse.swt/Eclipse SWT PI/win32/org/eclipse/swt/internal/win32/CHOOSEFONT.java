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

public class CHOOSEFONT {
	public int lStructSize;
	/** @field cast=(HWND) */
	public long hwndOwner;
	/** @field cast=(HDC) */
	public long hDC;
	/** @field cast=(LPLOGFONT) */
	public long lpLogFont;
	public int iPointSize;
	public int Flags;
	public int rgbColors;
	public long lCustData;
	/** @field cast=(LPCFHOOKPROC) */
	public long lpfnHook;
	/** @field cast=(LPCTSTR) */
	public long lpTemplateName;
	/** @field cast=(HINSTANCE) */
	public long hInstance;
	/** @field cast=(LPTSTR) */
	public long lpszStyle;
	public short nFontType;
	public int nSizeMin;
	public int nSizeMax;
	public static final int sizeof = OS.CHOOSEFONT_sizeof ();
}
