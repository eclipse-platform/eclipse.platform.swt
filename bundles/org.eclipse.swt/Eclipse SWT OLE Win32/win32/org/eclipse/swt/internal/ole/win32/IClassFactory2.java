package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
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
