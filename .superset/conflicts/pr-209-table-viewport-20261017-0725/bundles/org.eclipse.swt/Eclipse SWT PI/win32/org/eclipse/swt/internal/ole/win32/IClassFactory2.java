/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class IClassFactory2 extends IUnknown
{
public IClassFactory2(long address) {
	super(address);
}
public int CreateInstanceLic(long pUnkOuter, long pUnkReserved, GUID riid, long bstrKey, long ppvObject[]) {
	return COM.VtblCall(7, address, pUnkOuter, pUnkReserved, riid, bstrKey, ppvObject);
}
public int GetLicInfo(LICINFO licInfo) {
	return COM.VtblCall(5, address, licInfo);
}
public int RequestLicKey(int dwReserved, long[] pBstrKey) {
	return COM.VtblCall(6, address, dwReserved, pBstrKey);
}
}
