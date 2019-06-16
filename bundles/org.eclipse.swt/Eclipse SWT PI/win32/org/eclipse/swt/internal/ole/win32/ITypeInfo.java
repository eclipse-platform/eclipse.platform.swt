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
package org.eclipse.swt.internal.ole.win32;


import org.eclipse.swt.internal.win32.*;

public class ITypeInfo extends IUnknown
{

public ITypeInfo(long address) {
	super(address);
}
public int GetDocumentation(int index, String[] name, String[] docString, int[] pdwHelpContext, String[] helpFile ) {
	long[] pBstrName = null;
	if (name != null) pBstrName = new long[1];
	long[] pBstrDocString = null;
	if (docString != null) pBstrDocString = new long[1];
	long[] pBstrHelpFile  = null;
	if (helpFile != null) pBstrHelpFile = new long[1];
	int rc = COM.VtblCall(12, address, index, pBstrName, pBstrDocString, pdwHelpContext, pBstrHelpFile);
	if (name != null && pBstrName[0] != 0) {
		int size = COM.SysStringByteLen(pBstrName[0]);
		if (size > 0){
			// get the unicode character array from the global memory and create a String
			char[] buffer = new char[(size + 1) /2]; // add one to avoid rounding errors
			OS.MoveMemory(buffer, pBstrName[0], size);
			name[0] = new String(buffer);
			int subindex = name[0].indexOf("\0");
			if (subindex > 0)
				name[0] = name[0].substring(0, subindex);
		}
		COM.SysFreeString(pBstrName[0]);
	}
	if (docString != null && pBstrDocString[0] != 0) {
		int size = COM.SysStringByteLen(pBstrDocString[0]);
		if (size > 0){
			// get the unicode character array from the global memory and create a String
			char[] buffer = new char[(size + 1) /2]; // add one to avoid rounding errors
			OS.MoveMemory(buffer, pBstrDocString[0], size);
			docString[0] = new String(buffer);
			int subindex = docString[0].indexOf("\0");
			if (subindex > 0)
				docString[0] = docString[0].substring(0, subindex);
		}
		COM.SysFreeString(pBstrDocString[0]);
	}
	if (helpFile != null && pBstrHelpFile[0] != 0) {
		int size = COM.SysStringByteLen(pBstrHelpFile[0]);
		if (size > 0){
			// get the unicode character array from the global memory and create a String
			char[] buffer = new char[(size + 1) /2]; // add one to avoid rounding errors
			OS.MoveMemory(buffer, pBstrHelpFile[0], size);
			helpFile[0] = new String(buffer);
			int subindex = helpFile[0].indexOf("\0");
			if (subindex > 0)
				helpFile[0] = helpFile[0].substring(0, subindex);
		}
		COM.SysFreeString(pBstrHelpFile[0]);
	}
	return rc;
}
public int GetFuncDesc(int index, long[] ppFuncDesc) {
	return COM.VtblCall(5, address, index, ppFuncDesc);
}
public int GetImplTypeFlags(int index, int[] pImplTypeFlags) {
	return COM.VtblCall(9, address, index, pImplTypeFlags);
}
public int GetNames(int memid, String[] names, int cMaxNames, int[] pcNames){

	int nameSize = names.length;
	long[] rgBstrNames = new long[nameSize];
	int rc = COM.VtblCall(7, address, memid, rgBstrNames, nameSize, pcNames);

	if (rc == COM.S_OK) {
		for (int i = 0; i < pcNames[0]; i++) {
			int size = COM.SysStringByteLen(rgBstrNames[i]);
			if (size > 0){
				// get the unicode character array from the global memory and create a String
				char[] buffer = new char[(size + 1) /2]; // add one to avoid rounding errors
				OS.MoveMemory(buffer, rgBstrNames[i], size);
				names[i] = new String(buffer);
				int subindex = names[i].indexOf("\0");
				if (subindex > 0)
					names[i] = names[i].substring(0, subindex);
			}
			COM.SysFreeString(rgBstrNames[i]);
		}
	}

	return rc;
}
public int GetRefTypeInfo(int hRefType, long[] ppTInfo) {
	return COM.VtblCall(14, address, hRefType, ppTInfo);
}
public int GetRefTypeOfImplType(int index, int[] pRefType) {
	return COM.VtblCall(8, address, index, pRefType);
}
public int GetTypeAttr(long[] ppTypeAttr) {
	return COM.VtblCall(3, address, ppTypeAttr);
}
public int GetVarDesc(int index, long[] ppVarDesc ) {
	return COM.VtblCall(6, address, index, ppVarDesc);
}
public int ReleaseFuncDesc(long pFuncDesc ) {
	return COM.VtblCall(20, address, pFuncDesc);
}
public int ReleaseTypeAttr(long pTypeAttr) {
	return COM.VtblCall(19, address, pTypeAttr);
}
public int ReleaseVarDesc(long pVarDesc ) {
	return COM.VtblCall(21, address, pVarDesc);
}
}
