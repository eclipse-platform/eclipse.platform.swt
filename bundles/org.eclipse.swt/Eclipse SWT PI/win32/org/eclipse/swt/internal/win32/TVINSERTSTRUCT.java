package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class TVINSERTSTRUCT {
	public int hParent;
	public int hInsertAfter;
//	public TVITEM item;
	public int mask;
	public int hItem;
	public int state;
	public int stateMask;
	public int pszText;
  	public int cchTextMax;
  	public int iImage;
  	public int iSelectedImage;
	public int cChildren;
	public int lParam;
	public static final int sizeof = 48;
}
