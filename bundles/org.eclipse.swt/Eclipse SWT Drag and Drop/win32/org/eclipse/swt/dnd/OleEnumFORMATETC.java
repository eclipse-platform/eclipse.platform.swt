/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

final class OleEnumFORMATETC {

	private COMObject iEnumFORMATETC;

	private int refCount;
	private int index;

	private FORMATETC[] formats;

OleEnumFORMATETC() {

	createCOMInterfaces();

}
int AddRef() {
	refCount++;
	return refCount;
}
private void createCOMInterfaces() {
	iEnumFORMATETC = new COMObject(new int[] {2, 0, 0, 3, 1, 0, 1}){
		@Override
		public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
		@Override
		public long method1(long[] args) {return AddRef();}
		@Override
		public long method2(long[] args) {return Release();}
		@Override
		public long method3(long[] args) {return Next((int)args[0], args[1], args[2]);}
		@Override
		public long method4(long[] args) {return Skip((int)args[0]);}
		@Override
		public long method5(long[] args) {return Reset();}
		// method6 Clone - not implemented
	};
}
private void disposeCOMInterfaces() {
	if (iEnumFORMATETC != null)
		iEnumFORMATETC.dispose();
	iEnumFORMATETC = null;
}
long getAddress() {
	return iEnumFORMATETC.getAddress();
}
private FORMATETC[] getNextItems(int numItems){

	if (formats == null || numItems < 1) return null;

	int endIndex = index + numItems - 1;
	if (endIndex > (formats.length - 1)) endIndex = formats.length - 1;
	if (index > endIndex) return null;

	FORMATETC[] items =  new FORMATETC[endIndex - index + 1];
	for (int i = 0; i < items.length; i++){
		items[i] = formats[index];
		index++;
	}

	return items;
}
private int Next(int celt, long rgelt, long pceltFetched) {
	/* Retrieves the next celt items in the enumeration sequence.
	   If there are fewer than the requested number of elements left in the sequence,
	   it retrieves the remaining elements.
	   The number of elements actually retrieved is returned through pceltFetched
	   (unless the caller passed in NULL for that parameter).
	*/

	if (rgelt == 0)	return COM.E_INVALIDARG;
	if (pceltFetched == 0 && celt != 1) return COM.E_INVALIDARG;

	FORMATETC[] nextItems = getNextItems(celt);
	if (nextItems != null) {
		for (int i = 0; i < nextItems.length; i++) {
			COM.MoveMemory(rgelt + i*FORMATETC.sizeof, nextItems[i], FORMATETC.sizeof);
		}

		if (pceltFetched != 0)
			OS.MoveMemory(pceltFetched, new int[] {nextItems.length}, 4);

		if (nextItems.length == celt) return COM.S_OK;

	} else {
		if (pceltFetched != 0)
			OS.MoveMemory(pceltFetched, new int[] {0}, 4);
		COM.MoveMemory(rgelt, new FORMATETC(), FORMATETC.sizeof);

	}
	return COM.S_FALSE;
}
private int QueryInterface(long riid, long ppvObject) {

	if (riid == 0 || ppvObject == 0) return COM.E_NOINTERFACE;

	GUID guid = new GUID();
	COM.MoveMemory(guid, riid, GUID.sizeof);

	if (COM.IsEqualGUID(guid, COM.IIDIUnknown) || COM.IsEqualGUID(guid, COM.IIDIEnumFORMATETC)) {
		OS.MoveMemory(ppvObject, new long[] {iEnumFORMATETC.getAddress()}, C.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	OS.MoveMemory(ppvObject, new long[] {0}, C.PTR_SIZEOF);
	return COM.E_NOINTERFACE;
}
int Release() {
	refCount--;

	if (refCount == 0) {
		disposeCOMInterfaces();
		if (COM.FreeUnusedLibraries) {
			COM.CoFreeUnusedLibraries();
		}
	}

	return refCount;
}
private int Reset() {
	//Resets the enumeration sequence to the beginning.
	index = 0;
	return COM.S_OK;
}
void setFormats(FORMATETC[] newFormats) {
	formats = newFormats;
	index = 0;
}
private int Skip(int celt) {
	//Skips over the next specified number of elements in the enumeration sequence.
	if (celt < 1 ) return COM.E_INVALIDARG;

	index += celt;
	if (index > (formats.length - 1)){
		index = formats.length - 1;
		return COM.S_FALSE;
	}
	return COM.S_OK;
}
}
