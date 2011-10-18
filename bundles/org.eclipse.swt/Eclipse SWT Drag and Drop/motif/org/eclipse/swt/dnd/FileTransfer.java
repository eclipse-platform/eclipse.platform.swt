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

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;

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
	
	static FileTransfer _instance = new FileTransfer();
	static final String URI_LIST = "text/uri-list"; //$NON-NLS-1$
	static final int URI_LIST_ID = registerType(URI_LIST);
	static final String URI_LIST_PREFIX = "file:"; //$NON-NLS-1$
	static final String URI_LIST_SEPARATOR = "\r"; //$NON-NLS-1$

FileTransfer() {/*empty*/}

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
	transferData.result = 0;
		if (!checkFile(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String[] files = (String[])object;
	StringBuffer sb = new StringBuffer();
	for (int i = 0, length = files.length; i < length; i++){
		sb.append(URI_LIST_PREFIX);
		sb.append(files[i]);
		sb.append(URI_LIST_SEPARATOR);
	}
	byte[] buffer = Converter.wcsToMbcs(null, sb.toString(), true);
	int pValue = OS.XtMalloc(buffer.length);
	if (pValue == 0) return;
	OS.memmove(pValue, buffer, buffer.length);
	transferData.length = buffer.length;
	transferData.format = 8;
	transferData.pValue = pValue;
	transferData.result = 1;
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
	if (!isSupportedType(transferData) ||  transferData.pValue == 0) return null;
	int size = transferData.format * transferData.length / 8;
	if (size == 0) return null;
	
	byte[] buffer = new byte[size];
	OS.memmove(buffer, transferData.pValue, size);
	char[] chars = Converter.mbcsToWcs(null, buffer);
	String string  = new String(chars);
	// parse data and convert string to array of files
	int start = string.indexOf(URI_LIST_PREFIX);
	if (start == -1) return null;
	start += URI_LIST_PREFIX.length();
	String[] fileNames = new String[0];
	while (start < string.length()) { 
		int end = string.indexOf(URI_LIST_SEPARATOR, start);
		if (end == -1) end = string.length() - 1;
		String fileName = string.substring(start, end);
		String[] newFileNames = new String[fileNames.length + 1];
		System.arraycopy(fileNames, 0, newFileNames, 0, fileNames.length);
		newFileNames[fileNames.length] = fileName;
		fileNames = newFileNames;
		start = string.indexOf(URI_LIST_PREFIX, end);
		if (start == -1) break;
		start += URI_LIST_PREFIX.length();
	}
	return fileNames;
}

protected int[] getTypeIds(){
	return new int[]{URI_LIST_ID};
}

protected String[] getTypeNames(){
	return new String[]{URI_LIST};
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
