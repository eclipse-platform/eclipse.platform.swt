package org.eclipse.swt.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
import org.eclipse.swt.internal.ole.win32.*;

/**
 * since 2.1
 */
public abstract class Unknown {

	int refCount;
	COMObject iUnknown;
	
public Unknown() {
	createCOMInterfaces();
}
public int AddRef() {
	refCount++;
	return refCount;
}
protected void createCOMInterfaces() {
	iUnknown = new COMObject(new int[]{2, 0, 0}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
	};
}
protected void disposeCOMInterfaces() {
	if (iUnknown != null)
		iUnknown.dispose();
	iUnknown = null;	
}
public int getAddress() {
	return iUnknown.getAddress();
}
protected int QueryInterface(int riid, int ppvObject) {
	if (riid == 0 || ppvObject == 0)
		return OLE.E_NOINTERFACE;
	GUID guid = new GUID();
	COM.MoveMemory(guid, riid, GUID.sizeof);
	int[] ppsz = new int[1];
	int result = COM.StringFromCLSID(guid, ppsz);
	if (result != OLE.S_OK) {
		COM.MoveMemory(ppvObject, new int[] {0}, 4);
		return result;
	}
	int hMem = ppsz[0];
	int length = COM.GlobalSize(hMem);
	int ptr = COM.GlobalLock(hMem);
	char[] buffer = new char[length];
	COM.MoveMemory(buffer, ptr, length);
	COM.GlobalUnlock(hMem);
	COM.GlobalFree(hMem);
	String guidString = new String(buffer);
	int subindex = guidString.indexOf("\0");
	if (subindex > 0)
		guidString = guidString.substring(0, subindex);
	int[] address = new int[1];
	if (QueryInterface(guidString, address) == OLE.S_OK) {
		COM.MoveMemory(ppvObject, new int[] {address[0]}, 4);
		return OLE.S_OK;
	}
	COM.MoveMemory(ppvObject, new int[] {0}, 4);
	return OLE.E_NOINTERFACE;
}
public int QueryInterface(String riid, int[] address) {
	if (riid.equals("{00000000-0000-0000-C000-000000000046}")) {
		address[0] = iUnknown.getAddress();
		AddRef();
		return OLE.S_OK;
	}
	return OLE.E_NOINTERFACE;
}
public int Release() {
	refCount--;
	if (refCount == 0) {
		disposeCOMInterfaces();
	}
	return refCount;
}
}
