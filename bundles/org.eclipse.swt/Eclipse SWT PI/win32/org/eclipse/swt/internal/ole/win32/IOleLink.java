package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
