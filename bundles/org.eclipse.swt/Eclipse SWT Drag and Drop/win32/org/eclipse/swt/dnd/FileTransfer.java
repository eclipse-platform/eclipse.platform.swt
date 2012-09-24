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
package org.eclipse.swt.dnd;

import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

/**
 * The class <code>FileTransfer</code> provides a platform specific mechanism 
 * for converting a list of files represented as a java <code>String[]</code> to a 
 * platform specific representation of the data and vice versa.  
 * Each <code>String</code> in the array contains the absolute path for a single 
 * file or directory.
 * 
 * <p>An example of a java <code>String[]</code> containing a list of files is shown 
 * below:</p>
 * 
 * <code><pre>
 *     File file1 = new File("C:\temp\file1");
 *     File file2 = new File("C:\temp\file2");
 *     String[] fileData = new String[2];
 *     fileData[0] = file1.getAbsolutePath();
 *     fileData[1] = file2.getAbsolutePath();
 * </code></pre>
 *
 * @see Transfer
 */
public class FileTransfer extends ByteArrayTransfer {
	
	private static FileTransfer _instance = new FileTransfer();
	private static final String CF_HDROP = "CF_HDROP "; //$NON-NLS-1$
	private static final int CF_HDROPID = COM.CF_HDROP;
	private static final String CF_HDROP_SEPARATOR = "\0"; //$NON-NLS-1$
	
private FileTransfer() {}

/**
 * Returns the singleton instance of the FileTransfer class.
 *
 * @return the singleton instance of the FileTransfer class
 */
public static FileTransfer getInstance () {
	return _instance;
}

/**
 * This implementation of <code>javaToNative</code> converts a list of file names
 * represented by a java <code>String[]</code> to a platform specific representation.
 * Each <code>String</code> in the array contains the absolute path for a single 
 * file or directory.
 * 
 * @param object a java <code>String[]</code> containing the file names to be converted
 * @param transferData an empty <code>TransferData</code> object that will
 *  	be filled in on return with the platform specific format of the data
 * 
 * @see Transfer#nativeToJava
 */
public void javaToNative(Object object, TransferData transferData) {
	if (!checkFile(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String[] fileNames = (String[]) object;
	StringBuffer allFiles = new StringBuffer();
	for (int i = 0; i < fileNames.length; i++) {
		allFiles.append(fileNames[i]); 
		allFiles.append(CF_HDROP_SEPARATOR); // each name is null terminated
	}
	TCHAR buffer = new TCHAR(0, allFiles.toString(), true); // there is an extra null terminator at the very end
	DROPFILES dropfiles = new DROPFILES();
	dropfiles.pFiles = DROPFILES.sizeof;
	dropfiles.pt_x = dropfiles.pt_y = 0;
	dropfiles.fNC = 0;
	dropfiles.fWide = OS.IsUnicode ? 1 : 0;
	// Allocate the memory because the caller (DropTarget) has not handed it in
	// The caller of this method must release the data when it is done with it.
	int byteCount = buffer.length() * TCHAR.sizeof;
	long /*int*/ newPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, DROPFILES.sizeof + byteCount);
	OS.MoveMemory(newPtr, dropfiles, DROPFILES.sizeof);
	OS.MoveMemory(newPtr + DROPFILES.sizeof, buffer, byteCount);
	transferData.stgmedium = new STGMEDIUM();
	transferData.stgmedium.tymed = COM.TYMED_HGLOBAL;
	transferData.stgmedium.unionField = newPtr;
	transferData.stgmedium.pUnkForRelease = 0;
	transferData.result = COM.S_OK;
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of a list of file names to a java <code>String[]</code>.  
 * Each String in the array contains the absolute path for a single file or directory. 
 * 
 * @param transferData the platform specific representation of the data to be converted
 * @return a java <code>String[]</code> containing a list of file names if the conversion
 * 		was successful; otherwise null
 * 
 * @see Transfer#javaToNative
 */
public Object nativeToJava(TransferData transferData) {
	if (!isSupportedType(transferData) || transferData.pIDataObject == 0)  return null;
	
	// get file names from IDataObject
	IDataObject dataObject = new IDataObject(transferData.pIDataObject);
	dataObject.AddRef();
	FORMATETC formatetc = new FORMATETC();
	formatetc.cfFormat = COM.CF_HDROP;
	formatetc.ptd = 0;
	formatetc.dwAspect = COM.DVASPECT_CONTENT;
	formatetc.lindex = -1;
	formatetc.tymed = COM.TYMED_HGLOBAL;
	STGMEDIUM stgmedium = new STGMEDIUM();
	stgmedium.tymed = COM.TYMED_HGLOBAL;
	transferData.result = getData(dataObject, formatetc, stgmedium);
	dataObject.Release();
	if (transferData.result != COM.S_OK) return null;
	// How many files are there?
	int count = OS.DragQueryFile(stgmedium.unionField, 0xFFFFFFFF, null, 0);
	String[] fileNames = new String[count];
	for (int i = 0; i < count; i++) {
		// How long is the name ?
		int size = OS.DragQueryFile(stgmedium.unionField, i, null, 0) + 1;
		TCHAR lpszFile = new TCHAR(0, size);	
		// Get file name and append it to string
		OS.DragQueryFile(stgmedium.unionField, i, lpszFile, size);
		fileNames[i] = lpszFile.toString(0, lpszFile.strlen());
	}
	OS.DragFinish(stgmedium.unionField); // frees data associated with HDROP data
	return fileNames;
}

protected int[] getTypeIds(){
	return new int[] {CF_HDROPID};
}

protected String[] getTypeNames(){
	return new String[] {CF_HDROP};
}
boolean checkFile(Object object) {
	if (object == null || !(object instanceof String[]) || ((String[])object).length == 0) return false;
	String[] strings = (String[])object;
	for (int i = 0; i < strings.length; i++) {
		if (strings[i] == null || strings[i].length() == 0) return false;
	}
	return true;
}

protected boolean validate(Object object) {
	return checkFile(object);
}
}
