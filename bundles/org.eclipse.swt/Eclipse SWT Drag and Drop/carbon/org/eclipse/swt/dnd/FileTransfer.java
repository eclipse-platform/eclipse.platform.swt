/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
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
 * See <code>Transfer</code> for additional information.
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
 */
public class FileTransfer extends ByteArrayTransfer {
	
	private static FileTransfer _instance = new FileTransfer();
	//the text/uri-list is used only for transfers within the same application
	private static final String URILIST = "text/uri-list";
	private static final String HFS = "hfs ";
	private static final int URILISTID = registerType(URILIST);
	private static final int HFSID = registerType(HFS);
	private static final String URILIST_PREFIX = "file:";
	private static final String URILIST_SEPARATOR = "\r";
	
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
 * file or directory.  For additional information see 
 * <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>String[]</code> containing the file names to be 
 * converted
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
public void javaToNative(Object object, TransferData transferData) {
	if (!_validate(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String[] files = (String[])object;
	transferData.result = -1;
	if (transferData.type == URILISTID) {
		// create a string separated by "new lines" to represent list of files
		StringBuffer sb = new StringBuffer();
		for (int i = 0, length = files.length; i < length; i++){
			sb.append(URILIST_PREFIX);
			sb.append(files[i]);
			sb.append(URILIST_SEPARATOR);
		}
		String str = sb.toString();
		char[] chars = new char[str.length()];
		str.getChars (0, chars.length, chars, 0);
		byte[] buffer = new byte[chars.length * 2];
		OS.memcpy(buffer, chars, buffer.length);
		transferData.data = new byte[1][];
		transferData.data[0] = buffer;
		transferData.result = 0;
	}
	if (transferData.type == HFSID) {
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
}
/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of a list of file names to a java <code>String[]</code>.  
 * Each String in the array contains the absolute path for a single file or directory. 
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>String[]</code> containing a list of file names if the 
 * conversion was successful; otherwise null
 */
public Object nativeToJava(TransferData transferData) {
	if (!isSupportedType(transferData) || transferData.data == null) return null;
	if (transferData.data.length == 0) return null;
	
	if (transferData.type == URILISTID) {
		byte[] data = transferData.data[0];
		if (data.length == 0) return null;
		char[] chars = new char[(data.length + 1) / 2];
		OS.memcpy(chars, data, data.length);
		String str = new String(chars);
		int start = str.indexOf(URILIST_PREFIX);
		if (start == -1) return null;
		start += URILIST_PREFIX.length();
		String[] fileNames = new String[0];
		while (start < str.length()) { 
			int end = str.indexOf(URILIST_SEPARATOR, start);
			if (end == -1) end = str.length() - 1;
			String fileName = str.substring(start, end);
			String[] newFileNames = new String[fileNames.length + 1];
			System.arraycopy(fileNames, 0, newFileNames, 0, fileNames.length);
			newFileNames[fileNames.length] = fileName;
			fileNames = newFileNames;
			start = str.indexOf(URILIST_PREFIX, end);
			if (start == -1) break;
			start += URILIST_PREFIX.length();
		}
		return fileNames;
	}
	if (transferData.type == HFSID) {
		int count = transferData.data.length;
		String[] fileNames = new String[count];
		for (int i=0; i<count; i++) {
			byte[] data = transferData.data[i];
			byte[] fsspec = new byte[data.length - 10];
			System.arraycopy(data, 10, fsspec, 0, fsspec.length);
			byte[] fsRef = new byte[80];
			if (OS.FSpMakeFSRef(fsspec, fsRef) != OS.noErr) return null;
			int url = OS.CFURLCreateFromFSRef(OS.kCFAllocatorDefault, fsRef);
			if (url == 0) return null;
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
	return null;
}

protected int[] getTypeIds(){
	return new int[] {URILISTID, HFSID};
}

protected String[] getTypeNames(){
	return new String[] {URILIST, HFS};
}

boolean _validate(Object object) {
	if (object == null || !(object instanceof String[]) || ((String[])object).length == 0) return false;
	String[] strings = (String[])object;
	for (int i = 0; i < strings.length; i++) {
		if (strings[i] == null || strings[i].length() == 0) return false;
	}
	return true;
}

protected boolean validate(Object object) {
	return _validate(object);
}
}
