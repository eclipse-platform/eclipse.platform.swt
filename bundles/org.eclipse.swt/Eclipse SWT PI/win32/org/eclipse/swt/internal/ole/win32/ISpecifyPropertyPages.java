package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class ISpecifyPropertyPages extends IUnknown {

public ISpecifyPropertyPages(int address) {
	super(address);
}
public int GetPages(CAUUID pPages){
	return COM.VtblCall(3, address, pPages);
}
}
