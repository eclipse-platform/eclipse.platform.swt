package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class MSG {
	public int hwnd;     
	public int message; 
	public int wParam; 
	public int lParam; 
	public int time; 
//	public POINT pt;
	public int x, y;
	public static final int sizeof = 28;
}
