package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class IClassFactory2 extends IUnknown
{
public IClassFactory2(int address) {
	super(address);
}
public int CreateInstanceLic(int pUnkOuter, int pUnkReserved, GUID riid, int bstrKey, int ppvObject[]) {
	return COM.VtblCall(7, address, pUnkOuter, pUnkReserved, riid, bstrKey, ppvObject);
}
public int GetLicInfo(LICINFO licInfo) {
	return COM.VtblCall(5, address, licInfo);
}
public int RequestLicKey(int dwReserved, int[] pBstrKey) {
	return COM.VtblCall(6, address, dwReserved, pBstrKey);
}
}
