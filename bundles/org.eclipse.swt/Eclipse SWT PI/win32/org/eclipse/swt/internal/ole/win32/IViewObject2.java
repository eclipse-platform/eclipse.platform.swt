/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

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
