/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class IStorage extends IUnknown
{
public IStorage(int address) {
	super(address);
}
public int Commit(int grfCommitFlag) {
	return COM.VtblCall(9, address, grfCommitFlag);
}
public int CopyTo(
	int ciidExclude,     //Number of elements in rgiidExclude
  	GUID rgiidExclude,   //Array of interface identifiers (IIDs)
  	String[] snbExclude, //Points to a block of stream names in the storage object
	int pstgDest         //Points to destination storage object
  ){
	// we only support snbExclude = null
	if (snbExclude != null) {
		return COM.E_INVALIDARG;
	}
	return COM.VtblCall(7, address, ciidExclude, rgiidExclude, 0, pstgDest);
}
public int CreateStorage(
	String pwcsName, //Pointer to the name of the new storage object
	int grfMode,     //Access mode for the new storage object  
	int reserved1,   //Reserved; must be zero
	int reserved2,   //Reserved; must be zero
	int[] ppStg      //Pointer to new storage object
){
	
	// create a null terminated array of char
	char[] buffer = null;
	if (pwcsName != null) {
		buffer = (pwcsName+"\0").toCharArray();
	}
	
	return COM.VtblCall(5, address, buffer, grfMode, reserved1, reserved2, ppStg);
}
public int CreateStream(
	String pwcsName, //Pointer to the name of the new stream
	int grfMode,     //Access mode for the new stream  
	int reserved1,   //Reserved; must be zero  
	int reserved2,   //Reserved; must be zero  
	int[] ppStm      //Pointer to new stream object
){
  
	// create a null terminated array of char
	char[] buffer = null;
	if (pwcsName != null) {
		buffer = (pwcsName+"\0").toCharArray();
	}

	return COM.VtblCall(3, address, buffer, grfMode, reserved1, reserved2, ppStm);
}
public int DestroyElement(String pwcsName) {

	// create a null terminated array of char
	char[] buffer = null;
	if (pwcsName != null) {
		buffer = (pwcsName+"\0").toCharArray();
	}
	return COM.VtblCall(12, address, buffer);
}
public int EnumElements( 
	int reserved1, //Reserved; must be zero
	int reserved2, //Reserved; must be NULL
	int reserved3, //Reserved; must be zero
	int[] ppenum   //Pointer to output variable that
				   // receives the IEnumSTATSTG interface 
){
	return COM.VtblCall(11, address, reserved1, reserved2, reserved3, ppenum);
}
public int OpenStorage(
	String pwcsName,     //Pointer to the name of the                           
	                     // storage object to open
	int pstgPriority,    //Must be NULL.
	int grfMode,         //Access mode for the new storage object
	String snbExclude[], //Must be NULL.
	int reserved,        //Reserved; must be zero
	int[] ppStg          //Pointer to opened storage object
){

	// create a null terminated array of char
	char[] buffer = null;
	if (pwcsName != null) {
		buffer = (pwcsName+"\0").toCharArray();
	}

	// we only support the case where snbExclude = null
	if (snbExclude != null) {
		return COM.E_INVALIDARG;
	}
	return COM.VtblCall(6, address, buffer, pstgPriority, grfMode, 0, reserved, ppStg);
}
public int OpenStream(
	String pwcsName, //Pointer to name of stream to open
	int reserved1,   //Reserved; must be NULL  
	int grfMode,     //Access mode for the new stream  
	int reserved2,   //Reserved; must be zero
	int[] ppStm      //Pointer to output variable
	                 // that receives the IStream interface pointer
) {
  
	// create a null terminated array of char
	char[] buffer = null;
	if (pwcsName != null) {
		buffer = (pwcsName+"\0").toCharArray();
	}

	return COM.VtblCall(4, address, buffer, reserved1, grfMode, reserved2, ppStm);
}
public int RenameElement(
	String pwcsOldName,  //Pointer to the name of the
						 // element to be changed
	String pwcsNewName   //Pointer to the new name for
						 // the specified element
){

	// create a null terminated array of char
	char[] buffer1 = null;
	if (pwcsOldName != null) {
		buffer1 = (pwcsOldName+"\0").toCharArray();
	}
	// create a null terminated array of char
	char[] buffer2 = null;
	if (pwcsNewName != null) {
		buffer2 = (pwcsNewName+"\0").toCharArray();
	}
	return COM.VtblCall(13, address, buffer1, buffer2);
}
public int Revert() {
	return COM.VtblCall(10, address);
}
public int SetClass(
	GUID clsid  //CLSID to be assigned to the storage object
){
	return COM.VtblCall(15, address, clsid);
}
}
