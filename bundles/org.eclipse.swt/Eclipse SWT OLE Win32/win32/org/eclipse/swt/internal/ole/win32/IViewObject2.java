package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
import org.eclipse.swt.internal.win32.*;

public class IViewObject2 extends IUnknown
{
public IViewObject2(int address) {
	super(address);
}
public int GetExtent(int dwAspect, int lindex, DVTARGETDEVICE ptd, SIZE lpsizel) {
	return COM.VtblCall(9, address, dwAspect, lindex, ptd, lpsizel);
}
public int SetAdvise(int dwAspects, int dwAdvf, int pIAdviseSink) {
	return COM.VtblCall(7, address, dwAspects, dwAdvf, pIAdviseSink);
}
}
