package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class IConnectionPoint extends IUnknown
{
public IConnectionPoint(int address) {
	super(address);
}
public int Advise(int pUnk, int[] pdwCookie) {
	return COM.VtblCall(5, address, pUnk, pdwCookie);
}
public int Unadvise(int dwCookie) {
	return COM.VtblCall(6, address, dwCookie);
}
}
