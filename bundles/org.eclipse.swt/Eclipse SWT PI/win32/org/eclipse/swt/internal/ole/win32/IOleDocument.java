package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2003 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class IOleDocument extends IUnknown
{
public IOleDocument(int address) {
	super(address);
}
public int CreateView(int pIPSite,int pstm, int dwReserved,int[] ppView) {
	return COM.VtblCall(3, address, pIPSite, pstm, dwReserved, ppView);
}
}
