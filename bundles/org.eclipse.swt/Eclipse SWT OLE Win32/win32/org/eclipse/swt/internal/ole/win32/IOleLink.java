package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class IOleLink extends IUnknown
{
public IOleLink(int address) {
	super(address);
}
public int BindIfRunning() {
	return COM.VtblCall(10, address);
}
public int GetSourceMoniker(int[] ppmk) {
	return COM.VtblCall(6, address, ppmk);
}
}
