package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class IProvideClassInfo2 extends IProvideClassInfo
{
public IProvideClassInfo2(int address) {
	super(address);
}
public int GetGUID(int dwGuidKind, GUID pGUID) {
	return COM.VtblCall(4, address, dwGuidKind, pGUID);
}
}
