package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
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
