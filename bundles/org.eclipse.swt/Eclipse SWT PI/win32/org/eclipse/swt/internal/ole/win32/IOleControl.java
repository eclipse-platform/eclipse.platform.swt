package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class IOleControl extends IUnknown
{
public IOleControl(int address) {
	super(address);
}
public int GetControlInfo(CONTROLINFO pCI) {
	return COM.VtblCall(3, address, pCI);
}
}
