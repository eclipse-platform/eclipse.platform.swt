package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class NMTTDISPINFO extends NMHDR {
	public int lpszText;
//  char szText[80];
//	public char [] szText = new char [80];
	public int hinst;   
	public int uFlags;
	public int lParam;
	public static final int sizeofW = 188;
	public static final int sizeofA = 108;
	public static final int sizeof = OS.IsUnicode ? sizeofW : sizeofA;
}
