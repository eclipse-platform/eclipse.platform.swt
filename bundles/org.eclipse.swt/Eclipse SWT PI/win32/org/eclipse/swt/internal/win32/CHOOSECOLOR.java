package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class CHOOSECOLOR {
	public int lStructSize; 
	public int hwndOwner;
	public int hInstance; 
	public int rgbResult;
	public int lpCustColors; 
	public int Flags;
	public int lCustData;
	public int lpfnHook; 
	public int lpTemplateName;
	public static final int sizeof = 36;
}
