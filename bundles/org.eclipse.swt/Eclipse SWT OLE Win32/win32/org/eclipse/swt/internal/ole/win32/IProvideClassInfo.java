package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
