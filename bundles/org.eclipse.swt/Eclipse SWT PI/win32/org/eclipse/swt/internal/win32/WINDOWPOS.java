package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class WINDOWPOS {
	public int hwnd;                     
	public int hwndInsertAfter;
	public int x;                        
	public int y;
	public int cx;                       
	public int cy;
	public int flags;  
	public static final int sizeof = 28;
}
