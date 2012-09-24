/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

public class IDispatch extends IUnknown {

public IDispatch(long /*int*/ address) {
	super(address);
}
public int GetIDsOfNames(GUID riid, String[] rgszNames, int cNames, int lcid, int[] rgDispId) {

	char[] buffer;
	int size = rgszNames.length;

	// create an array to hold the addresses
	long /*int*/ hHeap = OS.GetProcessHeap();
	long /*int*/ ppNames = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, size * OS.PTR_SIZEOF);
	long /*int*/[] memTracker = new long /*int*/[size];
	
	try {	
		// add the address of each string to the array
		
		for (int i=0; i<size; i++){
			// create a null terminated array of char for each String
			int nameSize = rgszNames[i].length();
			buffer = new char[nameSize +1];
			rgszNames[i].getChars(0, nameSize, buffer, 0);
			// get the address of the start of the array of char
			long /*int*/ pName = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, buffer.length * 2);
			OS.MoveMemory(pName, buffer, buffer.length * 2);
			// copy the address to the array of addresses
			COM.MoveMemory(ppNames + OS.PTR_SIZEOF * i, new long /*int*/[]{pName}, OS.PTR_SIZEOF);
			// keep track of the Global Memory so we can free it
			memTracker[i] = pName;
		}
	
		return COM.VtblCall(5, address, new GUID(), ppNames, cNames, lcid, rgDispId);
		
	} finally {
		// free the memory
		for (int i=0; i<memTracker.length; i++){
			OS.HeapFree(hHeap, 0, memTracker[i]);
		}
		OS.HeapFree(hHeap, 0, ppNames);
	}
}
public int GetTypeInfo(int iTInfo, int lcid, long /*int*/[] ppTInfo ){
	return COM.VtblCall(4, address, iTInfo, lcid, ppTInfo);
}
public int GetTypeInfoCount(int [] pctinfo ){
	return COM.VtblCall(3, address, pctinfo);
}
public int Invoke(int dispIdMember, GUID riid, int lcid, int dwFlags, DISPPARAMS pDispParams, long /*int*/ pVarResult, EXCEPINFO pExcepInfo, int[] pArgErr) {
	return COM.VtblCall(6, address, dispIdMember, riid, lcid, dwFlags, pDispParams, pVarResult, pExcepInfo, pArgErr);
}
}
