package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class TOOLINFO {
	public int cbSize; 
	public int uFlags;
	public int hwnd; 
	public int uId; 
//	public RECT rect;
	public int left, top, right, bottom;
	public int hinst; 
	public int lpszText;
	public int lParam;
	public static int sizeof = 44;
}
