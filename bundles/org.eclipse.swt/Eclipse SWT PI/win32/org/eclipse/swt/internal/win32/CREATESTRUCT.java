package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class CREATESTRUCT {
	public int lpCreateParams; 
	public int hInstance; 
	public int hMenu; 
	public int hwndParent; 
	public int cy; 
	public int cx; 
	public int y; 
	public int x; 
	public int style; 
	public int lpszName; 
	public int lpszClass; 
	public int dwExStyle;
	public static final int sizeof = 48;
}
