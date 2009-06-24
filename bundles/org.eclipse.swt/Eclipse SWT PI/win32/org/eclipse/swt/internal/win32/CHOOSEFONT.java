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

public class CHOOSEFONT {
	public int lStructSize;
	/** @field cast=(HWND) */
	public int /*long*/ hwndOwner;
	/** @field cast=(HDC) */
	public int /*long*/ hDC;
	/** @field cast=(LPLOGFONT) */
	public int /*long*/ lpLogFont;
	public int iPointSize;
	public int Flags;
	public int rgbColors;
	public int /*long*/ lCustData;
	/** @field cast=(LPCFHOOKPROC) */
	public int /*long*/ lpfnHook;
	/** @field cast=(LPCTSTR) */
	public int /*long*/ lpTemplateName;
	/** @field cast=(HINSTANCE) */
	public int /*long*/ hInstance;
	/** @field cast=(LPTSTR) */
	public int /*long*/ lpszStyle;
	public short nFontType;
	public int nSizeMin;
	public int nSizeMax;
	public static final int sizeof = OS.CHOOSEFONT_sizeof ();
}
