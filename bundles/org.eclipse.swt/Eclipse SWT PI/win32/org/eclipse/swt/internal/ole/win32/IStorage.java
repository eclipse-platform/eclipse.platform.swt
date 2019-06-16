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

public class IStorage extends IUnknown
{
public IStorage(long address) {
	super(address);
}
public int Commit(int grfCommitFlag) {
	return COM.VtblCall(9, address, grfCommitFlag);
}
public int CopyTo(
	int ciidExclude,     //Number of elements in rgiidExclude
	GUID rgiidExclude,   //Array of interface identifiers (IIDs)
	String[] snbExclude, //Points to a block of stream names in the storage object
	long pstgDest         //Points to destination storage object
	){
	// we only support snbExclude = null
	if (snbExclude != null) {
		return COM.E_INVALIDARG;
	}
	return COM.VtblCall(7, address, ciidExclude, rgiidExclude, 0, pstgDest);
}
public int CreateStream(
	String pwcsName, //Pointer to the name of the new stream
	int grfMode,     //Access mode for the new stream
	int reserved1,   //Reserved; must be zero
	int reserved2,   //Reserved; must be zero
	long[] ppStm      //Pointer to new stream object
){

	// create a null terminated array of char
	char[] buffer = null;
	if (pwcsName != null) {
		buffer = (pwcsName+"\0").toCharArray();
	}

	return COM.VtblCall(3, address, buffer, grfMode, reserved1, reserved2, ppStm);
}
public int OpenStream(
	String pwcsName, //Pointer to name of stream to open
	long reserved1,   //Reserved; must be NULL
	int grfMode,     //Access mode for the new stream
	int reserved2,   //Reserved; must be zero
	long[] ppStm      //Pointer to output variable
	                 // that receives the IStream interface pointer
) {

	// create a null terminated array of char
	char[] buffer = null;
	if (pwcsName != null) {
		buffer = (pwcsName+"\0").toCharArray();
	}

	return COM.VtblCall(4, address, buffer, reserved1, grfMode, reserved2, ppStm);
}
}
