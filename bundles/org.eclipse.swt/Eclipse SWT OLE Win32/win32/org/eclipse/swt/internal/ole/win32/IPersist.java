package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class IPersist extends IUnknown
{
public IPersist(int address) {
	super(address);
}
public int GetClassID(GUID pClassID) {
	return COM.VtblCall(3, address, pClassID);
}
}
