package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class ISpecifyPropertyPages extends IUnknown {

public ISpecifyPropertyPages(int address) {
	super(address);
}
public int GetPages(CAUUID pPages){
	return COM.VtblCall(3, address, pPages);
}
}
