/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
	public int /*long*/ hwnd; 
	public int /*long*/ uId; 
//	public RECT rect;
	public int left, top, right, bottom;
	public int /*long*/ hinst; 
	public int /*long*/ lpszText;
	public int /*long*/ lParam;
	public int /*long*/ lpReserved;
	public static int sizeof = !OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1) ? OS.TOOLINFO_sizeof () : 44;
}
