package org.eclipse.swt.ole.win32;

/*
 * Copyright (c) 2000, 2003 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.internal.ole.win32.*;

/**
 *  @since 2.1
 */
public abstract class Dispatch extends Unknown {

	COMObject iDispatch;

public Dispatch() {
	super();
}
protected void createCOMInterfaces () {
	super.createCOMInterfaces();
	iDispatch = new COMObject(new int[]{2, 0, 0, 1, 3, 5, 8}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return GetTypeInfoCount(args[0]);}
		public int method4(int[] args) {return GetTypeInfo(args[0], args[1], args[2]);}
		public int method5(int[] args) {return GetIDsOfNames(args[0], args[1], args[2], args[3], args[4]);}
		public int method6(int[] args) {return Invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);}
	};
}
protected void disposeCOMInterfaces() {
	super.disposeCOMInterfaces();
	if (iDispatch != null)
		iDispatch.dispose();
	iDispatch = null;
}
public int getAddress() {
	return iDispatch.getAddress();
}
protected int GetIDsOfNames(int riid, int rgszNames, int cNames, int lcid, int rgDispId) {
	String[] names = new String[cNames];
	int[] ids = new int[cNames];
	int offset = 0;
	for (int i = 0; i < names.length; i++) {
		int[] pName = new int[1];
		COM.MoveMemory(pName, rgszNames + 4*i, 4);
		int hMem = pName[0];
		int length = COM.GlobalSize(hMem);
		int ptr = COM.GlobalLock(hMem);
		char[] buffer = new char[length];
		COM.MoveMemory(buffer, ptr, length);
		COM.GlobalUnlock(hMem);
		COM.GlobalFree(hMem);
		names[i] = new String(buffer);
		int subindex = names[i].indexOf("\0");
		if (subindex > 0)
			names[i] = names[i].substring(0, subindex);
	}
	int result = GetIDsOfNames(names, ids);
	if (result == OLE.S_OK) {
		COM.MoveMemory(rgDispId, ids, 4 * ids.length);
	}
	return result;
}
public abstract int GetIDsOfNames(String[] rgszNames, int[] rgDispId);

protected int GetTypeInfo(int iTInfo, int lcid, int ppTInfo ){
	return OLE.E_NOTIMPL;
}
protected int GetTypeInfoCount(int pctinfo ){
	return OLE.E_NOTIMPL;
}

protected int Invoke(int dispIdMember, int riid, int lcid, int dwFlags, int pDispParams, int pVarResult, int pExcepInfo, int pArgErr) {
	Variant[] rgvarg = null;
	int[] rgdispidNamedArgs = null;
	if (pDispParams != 0) {
		// create a DISPPARAMS structure for the input parameters
		DISPPARAMS dispParams = new DISPPARAMS();
		COM.MoveMemory(dispParams, pDispParams, DISPPARAMS.sizeof);
		rgvarg = new Variant[dispParams.cArgs];
		int offset = 0;
		for (int i = rgvarg.length - 1; i >= 0 ; i--) {
			rgvarg[i] = new Variant();
			rgvarg[i].setData(dispParams.rgvarg + offset);
			offset += Variant.sizeof;
		}
		int length = dispParams.cNamedArgs;
		int[] temp = new int[length];
		COM.MoveMemory(temp, dispParams.rgdispidNamedArgs, 4 * length);
		rgdispidNamedArgs = new int[length];
		for (int i = 0; i < length; i++) {
			rgdispidNamedArgs[length - 1 - i] = temp[i];
		}
	}
	Variant[] varResult = new Variant[1];
	int result = Invoke(dispIdMember, dwFlags, rgvarg, rgdispidNamedArgs, varResult);
	if (result == OLE.S_OK) {
		if (pVarResult != 0 && varResult[0] != null) varResult[0].getData(pVarResult);
		return result;
	}
	if (pVarResult != 0) COM.MoveMemory(pVarResult, new int[] {0}, 4);
	if (pExcepInfo != 0) COM.MoveMemory(pExcepInfo, new int[] {0}, 4);
	if (pArgErr != 0) COM.MoveMemory(pArgErr, new int[] {0}, 4);
	return OLE.E_NOTIMPL;	
}
public abstract int Invoke(int dispIdMember, int dwFlags, Variant[] rgvarg, int[] rgdispidNamedArgs, Variant[] varResult);

public int QueryInterface(String riid, int[] address) {
	int result = super.QueryInterface(riid, address);
	if (result == OLE.S_OK) return result;
	if (riid.equals("{00020400-0000-0000-C000-000000000046}")) {
		address[0] = iDispatch.getAddress();
		AddRef();
		return OLE.S_OK;
	}
	return OLE.E_NOINTERFACE;
}
}
