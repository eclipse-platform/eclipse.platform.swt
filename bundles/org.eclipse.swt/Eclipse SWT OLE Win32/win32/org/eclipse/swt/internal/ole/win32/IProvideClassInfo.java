package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class IProvideClassInfo extends IUnknown
{
public IProvideClassInfo(int address) {
	super(address);
}
public int GetClassInfo(int[] ppTI) {
	return COM.VtblCall(3, address, ppTI);
}
}
