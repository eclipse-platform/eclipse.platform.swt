package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class IOleControl extends IUnknown
{
public IOleControl(int address) {
	super(address);
}
public int GetControlInfo(CONTROLINFO pCI) {
	return COM.VtblCall(3, address, pCI);
}
}
