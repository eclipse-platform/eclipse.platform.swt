package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class DOCINFO {
	public int cbSize; 
	public int lpszDocName; // LPCTSTR
	public int lpszOutput; // LPCTSTR
	public int lpszDatatype;// LPCTSTR
	public int fwType; // DWORD
	public static final int sizeof = 20;
}
