package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class TCITEM {
	public int mask;
	public int dwState;
	public int dwStateMask;
	public int pszText;
	public int cchTextMax;
	public int iImage;
	public int lParam;
	public static final int sizeof = 28;
}
