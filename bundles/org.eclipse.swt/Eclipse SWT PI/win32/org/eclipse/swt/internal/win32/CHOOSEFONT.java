package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class CHOOSEFONT {
	public int lStructSize;     
	public int hwndOwner; 
	public int hDC;     
	public int lpLogFont;     
	public int iPointSize; 
	public int Flags;     
	public int rgbColors;     
	public int lCustData; 
	public int lpfnHook;     
	public int lpTemplateName; 
	public int hInstance;     
	public int lpszStyle; 
	public short nFontType;
	public int nSizeMin;     
	public int nSizeMax; 
	public static final int sizeof = 60;
}
