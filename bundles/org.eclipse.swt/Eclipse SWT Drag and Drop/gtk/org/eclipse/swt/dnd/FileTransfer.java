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
import org.eclipse.swt.internal.gtk.*;

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
	private static final String URI_LIST = "text/uri-list"; //$NON-NLS-1$
	private static final int URI_LIST_ID = registerType(URI_LIST);
	private static final String GNOME_LIST = "x-special/gnome-copied-files"; //$NON-NLS-1$
	private static final int GNOME_LIST_ID = registerType(GNOME_LIST);

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
	transferData.gtk3().result = 0;
	if (!checkFile(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	boolean gnomeList = transferData.type == GNOME_LIST_ID;
	byte[] buffer, separator;
	if (gnomeList) {
		buffer = new byte[] {'c','o','p','y'};
		separator = new byte[] {'\n'};
	} else {
		buffer = new byte[0];
		separator = new byte[] {'\r', '\n'};
	}
	String[] files = (String[])object;
	for (int i = 0; i < files.length; i++) {
		String string = files[i];
		if (string == null) continue;
		int length = string.length();
		if (length == 0) continue;
		char[] chars = new char[length];
		string.getChars(0, length, chars, 0);
		long [] error = new long [1];
		long utf8Ptr = OS.g_utf16_to_utf8(chars, chars.length, null, null, error);
		if (error[0] != 0 || utf8Ptr == 0) continue;
		long localePtr = OS.g_filename_from_utf8(utf8Ptr, -1, null, null, error);
		OS.g_free(utf8Ptr);
		if (error[0] != 0 || localePtr == 0) continue;
		long uriPtr = OS.g_filename_to_uri(localePtr, 0, error);
		OS.g_free(localePtr);
		if (error[0] != 0 || uriPtr == 0) continue;
		length = C.strlen(uriPtr);
		byte[] temp = new byte[length];
		C.memmove (temp, uriPtr, length);
		OS.g_free(uriPtr);
		int newLength = (buffer.length > 0) ? buffer.length+separator.length+temp.length :  temp.length;
		byte[] newBuffer = new byte[newLength];
		int offset = 0;
		if (buffer.length > 0) {
			System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
			offset +=  buffer.length;
			System.arraycopy(separator, 0, newBuffer, offset, separator.length);
			offset += separator.length;
		}
		System.arraycopy(temp, 0, newBuffer, offset, temp.length);
		buffer = newBuffer;
	}
	if (buffer.length == 0) return;
	long ptr = OS.g_malloc(buffer.length+1);
	C.memset(ptr, '\0', buffer.length+1);
	C.memmove(ptr, buffer, buffer.length);
	transferData.gtk3().pValue = ptr;
	transferData.gtk3().length = buffer.length;
	transferData.gtk3().format = 8;
	transferData.gtk3().result = 1;
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
	if ( !isSupportedType(transferData) ||  transferData.gtk3().pValue == 0 ||  transferData.gtk3().length <= 0 ) return null;
	int length = transferData.gtk3().length;
	byte[] temp = new byte[length];
	C.memmove(temp, transferData.gtk3().pValue, length);
	boolean gnomeList = transferData.type == GNOME_LIST_ID;
	int sepLength = gnomeList ? 1 : 2;
	long [] files = new long [0];
	int offset = 0;
	for (int i = 0; i < temp.length - 1; i++) {
		boolean terminator = gnomeList ? temp[i] == '\n' : temp[i] == '\r' && temp[i+1] == '\n';
		if (terminator) {
			if (!(gnomeList && offset == 0)) {
				/* The content of the first line in a gnome-list is always either 'copy' or 'cut' */
				int size =  i - offset;
				long file = OS.g_malloc(size + 1);
				byte[] fileBuffer = new byte[size + 1];
				System.arraycopy(temp, offset, fileBuffer, 0, size);
				C.memmove(file, fileBuffer, size + 1);
				long [] newFiles = new long [files.length + 1];
				System.arraycopy(files, 0, newFiles, 0, files.length);
				newFiles[files.length] = file;
				files = newFiles;
			}
			offset = i + sepLength;
		}
	}
	if (offset < temp.length - sepLength) {
		int size =  temp.length - offset;
		long file = OS.g_malloc(size + 1);
		byte[] fileBuffer = new byte[size + 1];
		System.arraycopy(temp, offset, fileBuffer, 0, size);
		C.memmove(file, fileBuffer, size + 1);
		long [] newFiles = new long [files.length + 1];
		System.arraycopy(files, 0, newFiles, 0, files.length);
		newFiles[files.length] = file;
		files = newFiles;
	}
	String[] fileNames = new String[0];
	for (int i = 0; i < files.length; i++) {
		long [] error = new long [1];
		long localePtr = OS.g_filename_from_uri(files[i], null, error);
		OS.g_free(files[i]);
		if (error[0] != 0 || localePtr == 0) continue;
		long utf8Ptr = OS.g_filename_to_utf8(localePtr, -1, null, null, null);
		if (utf8Ptr == 0) utf8Ptr = OS.g_filename_display_name (localePtr);
		if (localePtr != utf8Ptr) OS.g_free (localePtr);
		if (utf8Ptr == 0) continue;
		long [] items_written = new long [1];
		long utf16Ptr = OS.g_utf8_to_utf16(utf8Ptr, -1, null, items_written, null);
		OS.g_free(utf8Ptr);
		if (utf16Ptr == 0) continue;
		length = (int)items_written[0];
		char[] buffer = new char[length];
		C.memmove(buffer, utf16Ptr, length * 2);
		OS.g_free(utf16Ptr);
		String name = new String(buffer);
		String[] newFileNames = new String[fileNames.length + 1];
		System.arraycopy(fileNames, 0, newFileNames, 0, fileNames.length);
		newFileNames[fileNames.length] = name;
		fileNames = newFileNames;
	}
	if (fileNames.length == 0) return null;
	return fileNames;
}

@Override
protected int[] getTypeIds(){
	return new int[]{URI_LIST_ID, GNOME_LIST_ID};
}

@Override
protected String[] getTypeNames(){
	return new String[]{URI_LIST, GNOME_LIST};
}

boolean checkFile(Object object) {
	if (object == null || !(object instanceof String[]) || ((String[])object).length == 0) return false;
	String[] strings = (String[])object;
	for (int i = 0; i < strings.length; i++) {
		if (strings[i] == null || strings[i].length() == 0) return false;
	}
	return true;
}

@Override
protected boolean validate(Object object) {
	return checkFile(object);
}
}
