package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class IFont extends IUnknown {
/**
 * IFont constructor comment.
 * @param address int
 */
public IFont(int address) {
	super(address);
}
public int get_hFont(int[] phfont){
	return COM.VtblCall(3, address, phfont);
}
}
