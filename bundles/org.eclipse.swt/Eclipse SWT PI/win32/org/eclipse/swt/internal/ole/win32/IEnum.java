package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class IEnum extends IUnknown
{
public IEnum(int address) {
	super(address);
}
public int Clone( int[] ppenum  ){
	return COM.VtblCall(6, address, ppenum);
}
public int Next(int celt, int rgelt, int[] pceltFetched  ){
	return COM.VtblCall(3, address, celt, rgelt, pceltFetched);
}
public int Reset() {
	return COM.VtblCall(5, address);
}
public int Skip(int celt){
	return COM.VtblCall(4, address, celt);
}
}
