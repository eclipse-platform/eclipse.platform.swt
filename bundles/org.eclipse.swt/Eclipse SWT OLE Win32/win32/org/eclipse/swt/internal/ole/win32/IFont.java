package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class IFont extends IUnknown {
public IFont(int address) {
	super(address);
}
public int get_hFont(int[] phfont){
	return COM.VtblCall(3, address, phfont);
}
}
