package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class HELPINFO {
	public int cbSize;
	public int iContextType;
	public int iCtrlId;
	public int hItemHandle;
	public int dwContextId;
//	POINT MousePos
	public int x;
	public int y;
	public static final int sizeof = 28;
}
