package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class IPersistStorage extends IPersist
{
public IPersistStorage(int address) {
	super(address);
}
public int IsDirty() {
	return COM.VtblCall(4, address);
}
public int InitNew(int pStg) {
	return COM.VtblCall(5, address, pStg);
}
public int Load(int pStg) {
	return COM.VtblCall(6, address, pStg);
}
public int Save(int pStgSave, boolean fSameAsLoad) {
	return COM.VtblCall(7, address, pStgSave, fSameAsLoad);
}
public int SaveCompleted(int pStgNew) {
	return COM.VtblCall(8, address, pStgNew);
}
public int HandsOffStorage(){
	return COM.VtblCall(9, address);
}
}
