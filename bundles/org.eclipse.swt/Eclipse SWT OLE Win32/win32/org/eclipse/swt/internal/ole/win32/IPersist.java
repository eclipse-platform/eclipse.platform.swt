package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
