package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class IUnknown
{
	int address;
public IUnknown(int address) {
	this.address = address;
}
public int AddRef() {
	return COM.VtblCall(1, address);
}
public int getAddress() {
	return address;
}
public int QueryInterface(GUID riid, int ppvObject[]) {
	return COM.VtblCall(0, address, riid, ppvObject);
}
public int Release() {
	return COM.VtblCall(2, address);
}
}
