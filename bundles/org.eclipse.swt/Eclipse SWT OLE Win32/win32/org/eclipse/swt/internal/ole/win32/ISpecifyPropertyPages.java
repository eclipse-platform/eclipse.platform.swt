package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class ISpecifyPropertyPages extends IUnknown {

public ISpecifyPropertyPages(int address) {
	super(address);
}
public int GetPages(CAUUID pPages){
	return COM.VtblCall(3, address, pPages);
}
}
