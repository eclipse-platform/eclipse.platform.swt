package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class NMLISTVIEW extends NMHDR {
	public int iItem;
	public int iSubItem;
	public int uNewState;
	public int uOldState;
	public int uChanged;
//	public POINT ptAction;
	public int x, y;
	public int lParam;
	public static int sizeof = 44;
}
