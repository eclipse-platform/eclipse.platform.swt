package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class IFont extends IUnknown {
public IFont(int address) {
	super(address);
}
public int get_hFont(int[] phfont){
	return COM.VtblCall(3, address, phfont);
}
}
