package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class IOleWindow extends IUnknown {
public IOleWindow(int address) {
	super(address);
}
public int GetWindow(int phwnd[]) {
	return COM.VtblCall(3, address, phwnd);
}
}
