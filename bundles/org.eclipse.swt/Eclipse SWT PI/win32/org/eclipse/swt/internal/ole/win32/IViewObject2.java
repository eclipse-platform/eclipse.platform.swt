package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
