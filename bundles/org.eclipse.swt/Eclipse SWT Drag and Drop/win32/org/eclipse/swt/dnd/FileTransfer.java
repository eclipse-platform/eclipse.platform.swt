/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
 * <pre><code>
 *     File file1 = new File("C:\\temp\\file1");
 *     File file2 = new File("C:\\temp\\file2");
 *     String[] fileData = new String[2];
 *     fileData[0] = file1.getAbsolutePath();
 *     fileData[1] = file2.getAbsolutePath();
 * </code></pre>
 *
 * @see Transfer
 */
public class FileTransfer extends ByteArrayTransfer {

	private static FileTransfer _instance = new FileTransfer();
	private static final String CF_HDROP = "CF_HDROP"; //$NON-NLS-1$
	private static final int CF_HDROPID = COM.CF_HDROP;
	private static final String CFSTR_SHELLIDLIST = "Shell IDList Array"; //$NON-NLS-1$
	private static final int CFSTR_SHELLIDLISTID = registerType(CFSTR_SHELLIDLIST);

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
@Override
public void javaToNative(Object object, TransferData transferData) {
	if (!checkFile(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String[] fileNames = (String[]) object;
	long newPtr = 0;
	if (transferData.type == CF_HDROPID) {
		StringBuilder allFiles = new StringBuilder();
		for (String fileName : fileNames) {
			allFiles.append(fileName);
			allFiles.append('\0'); // each name is null terminated
		}
		allFiles.append('\0'); // there is an extra null terminator at the very end
		char [] buffer = new char [allFiles.length()];
		allFiles.getChars(0, allFiles.length(), buffer, 0);
		DROPFILES dropfiles = new DROPFILES();
		dropfiles.pFiles = DROPFILES.sizeof;
		dropfiles.pt_x = dropfiles.pt_y = 0;
		dropfiles.fNC = 0;
		dropfiles.fWide = 1;
		// Allocate the memory because the caller (DropTarget) has not handed it in
		// The caller of this method must release the data when it is done with it.
		int byteCount = buffer.length * TCHAR.sizeof;
		newPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, DROPFILES.sizeof + byteCount);
		if (newPtr != 0) {
			OS.MoveMemory(newPtr, dropfiles, DROPFILES.sizeof);
			OS.MoveMemory(newPtr + DROPFILES.sizeof, buffer, byteCount);
		}
	} else if (transferData.type == CFSTR_SHELLIDLISTID) {
		newPtr =  generateCidaFromFilepaths(fileNames);
	}
	transferData.stgmedium = new STGMEDIUM();
	transferData.stgmedium.tymed = COM.TYMED_HGLOBAL;
	transferData.stgmedium.unionField = newPtr;
	transferData.stgmedium.pUnkForRelease = 0;
	transferData.result = newPtr != 0 ? COM.S_OK : COM.E_FAIL;
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
@Override
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
		int size = OS.DragQueryFile(stgmedium.unionField, i, null, 0);
		char [] lpszFile = new char [size + 1];
		// Get file name and append it to string
		OS.DragQueryFile(stgmedium.unionField, i, lpszFile, size + 1);
		fileNames[i] = new String(lpszFile, 0, size);
	}
	OS.DragFinish(stgmedium.unionField); // frees data associated with HDROP data
	return fileNames;
}

/**
 * Generate {@link CIDA} structure and trailing data to transfer filenames
 * as {@link #CFSTR_SHELLIDLIST}.
 * <p>
 * For more information on the {@link CIDA} structure see also {@link #resolveCidaToFilepaths(long)}.
 * </p>
 *
 * @param fileNames filenames to pack in {@link CIDA}.
 * @return pointer to global memory chunk filled with generated data or <code>0</code> on failure
 */
private long generateCidaFromFilepaths(String[] fileNames) {
	final int n = fileNames.length;
	long [] pidls = new long [n];
	try {
		CIDA cida = new CIDA();
		cida.cidl = n;
		int cidaSize = CIDA.sizeof + 4 * n;
		cida.aoffset = cidaSize; // offsets are from cida begin so the first is equal to cida size

		int[] pidlOffsets = new int[n];
		int[] pidlSizes = new int[n];
		int pidlSizeSum = 2; // initialize with 2 for the empty (but double null terminated) parent pidl
		for (int i = 0; i < n; i++) {
			TCHAR szfileName = new TCHAR(0, fileNames[i], true);
			long [] ppv = new long [1];
			int hr = COM.PathToPIDL(szfileName.chars, ppv);
			if (hr != OS.S_OK) {
				return 0;
			}
			pidls[i] = ppv[0];
			pidlSizes[i] = OS.ILGetSize(pidls[i]);
			pidlSizeSum += pidlSizes[i];

			if (i == 0) {
				pidlOffsets[0] = cidaSize + 2;
			}
			else {
				pidlOffsets[i] = pidlOffsets[i - 1] + pidlSizes[i - 1];
			}
		}

		long newPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, cidaSize + pidlSizeSum);
		if (newPtr != 0) {
			OS.MoveMemory(newPtr, cida, CIDA.sizeof);
			OS.MoveMemory(newPtr + CIDA.sizeof, pidlOffsets, 4 * cida.cidl);
			for (int i = 0; i < n; i++) {
				OS.MoveMemory(newPtr + pidlOffsets[i], pidls[i], pidlSizes[i]);
			}
		}
		return newPtr;
	} finally {
		for (int i = 0; i < n; i++) {
			if (pidls[i] != 0) {
				OS.CoTaskMemFree(pidls[i]);
			}
		}
	}
}

@Override
public boolean isSupportedType(TransferData transferData) {
	// filter Shell ID List Array transfer only for dropping
	if (transferData != null && transferData.pIDataObject != 0 && transferData.type == CFSTR_SHELLIDLISTID) {
		return false;
	}
	return super.isSupportedType(transferData);
}

@Override
protected int[] getTypeIds(){
	// Note: FileTransfer adds Shell ID List as transfer type but later
	//       limit this type for dragging only.
	return new int[] {CF_HDROPID, CFSTR_SHELLIDLISTID};
}

@Override
protected String[] getTypeNames(){
	return new String[] {CF_HDROP, CFSTR_SHELLIDLIST};
}
boolean checkFile(Object object) {
	if (object == null || !(object instanceof String[]) || ((String[])object).length == 0) return false;
	for (String string : (String[])object) {
		if (string == null || string.length() == 0) return false;
	}
	return true;
}

@Override
protected boolean validate(Object object) {
	return checkFile(object);
}
}
