package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class IConnectionPointContainer extends IUnknown
{
public IConnectionPointContainer(int address) {
	super(address);
}
public int FindConnectionPoint(GUID riid, int[] ppCP) {
	return COM.VtblCall(4, address, riid, ppCP);
}
}
