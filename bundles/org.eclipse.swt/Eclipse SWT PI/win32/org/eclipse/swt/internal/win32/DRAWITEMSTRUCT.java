package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class DRAWITEMSTRUCT {
	public int CtlType;
	public int CtlID;
	public int itemID;
	public int itemAction;
	public int itemState;
	public int hwndItem;
	public int hDC;
// 	public RECT rcItem;
	public int left, top, bottom, right;
	public int itemData;
	public static final int sizeof = 48;
}
