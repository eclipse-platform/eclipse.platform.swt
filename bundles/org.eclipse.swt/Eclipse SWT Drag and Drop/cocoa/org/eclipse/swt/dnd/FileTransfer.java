/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Outhink - support for typeFileURL
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.internal.cocoa.*;
 
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
	static final String ID_NAME = OS.NSFilenamesPboardType.getString();
	static final int ID = registerType(ID_NAME);
	
FileTransfer() {}

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
	String[] files = (String[])object;
	int length = files.length;
	NSMutableArray array = NSMutableArray.arrayWithCapacity(length);
	for (int i = 0; i < length; i++) {
		String fileName = files[i];
		NSString string = NSString.stringWith(fileName);
		array.addObject(string);
	}
	transferData.data = array;
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
	if (!isSupportedType(transferData) || transferData.data == null) return null;
	NSArray array = (NSArray) transferData.data;
	if (array.count() == 0) return null;
	int count = (int)/*64*/array.count();
	String[] fileNames = new String[count];
	for (int i=0; i<count; i++) {
		NSString string = new NSString(array.objectAtIndex(i));
		fileNames[i] = string.getString();
	}
	return fileNames;
}

protected int[] getTypeIds(){
	return new int[] {ID};
}

protected String[] getTypeNames(){
	return new String[] {ID_NAME};
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
