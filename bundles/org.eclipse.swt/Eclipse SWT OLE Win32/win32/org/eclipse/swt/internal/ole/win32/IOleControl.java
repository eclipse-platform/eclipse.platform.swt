package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
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
