package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class IOleWindow extends IUnknown {
/**
 * ProxyIOleWindow constructor comment.
 * @param address int
 */
public IOleWindow(int address) {
	super(address);
}
public int GetWindow(int phwnd[]) {
	return COM.VtblCall(3, address, phwnd);
}
}
