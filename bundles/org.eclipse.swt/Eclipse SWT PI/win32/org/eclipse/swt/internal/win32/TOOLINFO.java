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

public class TOOLINFO {
	public int cbSize; 
	public int uFlags;
	/** @field cast=(HWND) */
	public int /*long*/ hwnd; 
	public int /*long*/ uId; 
//	public RECT rect;
	/** @field accessor=rect.left */
	public int left; 
	/** @field accessor=rect.top */
	public int top; 
	/** @field accessor=rect.right */
	public int right; 
	/** @field accessor=rect.bottom */
	public int bottom;
	/** @field cast=(HINSTANCE) */
	public int /*long*/ hinst; 
	/** @field cast=(LPTSTR) */
	public int /*long*/ lpszText;
	public int /*long*/ lParam;
	/** @field cast=(void *) */
	public int /*long*/ lpReserved;
	public static int sizeof = !OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1) ? OS.TOOLINFO_sizeof () : 44;
}
