package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class MENUINFO {
	public int cbSize;
	public int fMask;
	public int dwStyle;
	public int cyMax;
	public int hbrBack;
	public int dwContextHelpID;
	public int dwMenuData;
	public static final int sizeof = 28;
}
