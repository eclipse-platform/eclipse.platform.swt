package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class IUnknown
{
	int address;
public IUnknown(int address) {
	this.address = address;
}
public int AddRef() {
	return COM.VtblCall(1, address);
}
public int getAddress() {
	return address;
}
public int QueryInterface(GUID riid, int ppvObject[]) {
	return COM.VtblCall(0, address, riid, ppvObject);
}
public int Release() {
	return COM.VtblCall(2, address);
}
}
