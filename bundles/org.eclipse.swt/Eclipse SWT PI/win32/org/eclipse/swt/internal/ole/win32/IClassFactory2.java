/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

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
