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

import java.io.*;
import org.eclipse.swt.internal.carbon.*;
 
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
	static final String HFS = "hfs "; //$NON-NLS-1$
	static final String FURL = "furl"; //$NON-NLS-1$
	static final int HFSID = registerType(HFS);
	static final int FURLID = registerType(FURL);
	
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
	transferData.result = -1;
	byte[][] data = new byte[files.length][];
	for (int i = 0; i < data.length; i++) {
		File file = new File(files[i]);
		boolean isDirectory = file.isDirectory();
		String fileName = files[i];
		char [] chars = new char [fileName.length ()];
		fileName.getChars (0, chars.length, chars, 0);
		int cfstring = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, chars, chars.length);
		if (cfstring == 0) return;
		try {
			int url = OS.CFURLCreateWithFileSystemPath(OS.kCFAllocatorDefault, cfstring, OS.kCFURLPOSIXPathStyle, isDirectory);
			if (url == 0) return;
			try {
				if (transferData.type == HFSID) {
					byte[] fsRef = new byte[80];
					if (!OS.CFURLGetFSRef(url, fsRef)) return;
					byte[] fsSpec = new byte[70];
					if (OS.FSGetCatalogInfo(fsRef, 0, null, null, fsSpec, null) != OS.noErr) return;
					byte[] hfsflavor = new byte[10 + fsSpec.length];
					byte[] finfo = new byte[16];
					OS.FSpGetFInfo(fsSpec, finfo);
					System.arraycopy(finfo, 0, hfsflavor, 0, 10);
					System.arraycopy(fsSpec, 0, hfsflavor, 10, fsSpec.length);
					data[i] = hfsflavor;
				}
				if (transferData.type == FURLID) {
					int encoding = OS.CFStringGetSystemEncoding();
					int theData = OS.CFURLCreateData(OS.kCFAllocatorDefault, url, encoding, true);
					if (theData == 0) return;
					try {
						int length = OS.CFDataGetLength(theData);
						byte[] buffer = new byte[length];
						CFRange range = new CFRange();
						range.length = length;
						OS.CFDataGetBytes(theData, range, buffer);
						data[i] = buffer;
					} finally {
						OS.CFRelease(theData);
					}
				}
			} finally {
				OS.CFRelease(url);
			}
		} finally {
			OS.CFRelease(cfstring);
		}
	}
	transferData.data = data;
	transferData.result = 0;
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
	if (transferData.data.length == 0) return null;
	int count = transferData.data.length;
	String[] fileNames = new String[count];
	for (int i=0; i<count; i++) {
		byte[] data = transferData.data[i];
		int url = 0;
		if (transferData.type == HFSID) {
			byte[] fsspec = new byte[data.length - 10];
			System.arraycopy(data, 10, fsspec, 0, fsspec.length);
			byte[] fsRef = new byte[80];
			if (OS.FSpMakeFSRef(fsspec, fsRef) != OS.noErr) return null;
			url = OS.CFURLCreateFromFSRef(OS.kCFAllocatorDefault, fsRef);
			if (url == 0) return null;
		}
		if (transferData.type == FURLID) {
			int encoding = OS.kCFStringEncodingUTF8;
			url = OS.CFURLCreateWithBytes(OS.kCFAllocatorDefault, data, data.length, encoding, 0);
			if (url == 0) return null;
		}
		try {
			int path = OS.CFURLCopyFileSystemPath(url, OS.kCFURLPOSIXPathStyle);
			if (path == 0) return null;
			try {
				int length = OS.CFStringGetLength(path);
				if (length == 0) return null;
				char[] buffer= new char[length];
				CFRange range = new CFRange();
				range.length = length;
				OS.CFStringGetCharacters(path, range, buffer);
				fileNames[i] = new String(buffer);
			} finally {
				OS.CFRelease(path);
			}
		} finally {
			OS.CFRelease(url);
		}
	}
	return fileNames;
}

protected int[] getTypeIds(){
	return new int[] {FURLID, HFSID};
}

protected String[] getTypeNames(){
	return new String[] {FURL, HFS};
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
