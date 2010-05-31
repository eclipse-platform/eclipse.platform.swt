/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
 * The class <code>TextTransfer</code> provides a platform specific mechanism 
 * for converting plain text represented as a java <code>String</code> 
 * to a platform specific representation of the data and vice versa.
 * 
 * <p>An example of a java <code>String</code> containing plain text is shown 
 * below:</p>
 * 
 * <code><pre>
 *     String textData = "Hello World";
 * </code></pre>
 * 
 * <p>Note the <code>TextTransfer</code> does not change the content of the text
 * data. For a better integration with the platform, the application should convert
 * the line delimiters used in the text data to the standard line delimiter used by the
 * platform.
 * </p>
 * 
 * @see Transfer
 */
public class TextTransfer extends ByteArrayTransfer {

	private static TextTransfer _instance = new TextTransfer();
	private static final String CF_UNICODETEXT = "CF_UNICODETEXT"; //$NON-NLS-1$
	private static final String CF_TEXT = "CF_TEXT"; //$NON-NLS-1$
	private static final int CF_UNICODETEXTID = COM.CF_UNICODETEXT;
	private static final int CF_TEXTID = COM.CF_TEXT;
	
private TextTransfer() {}

/**
 * Returns the singleton instance of the TextTransfer class.
 *
 * @return the singleton instance of the TextTransfer class
 */
public static TextTransfer getInstance () {
	return _instance;
}

/**
 * This implementation of <code>javaToNative</code> converts plain text
 * represented by a java <code>String</code> to a platform specific representation.
 * 
 * @param object a java <code>String</code> containing text
 * @param transferData an empty <code>TransferData</code> object that will
 *  	be filled in on return with the platform specific format of the data
 *  
 * @see Transfer#nativeToJava
 */
public void javaToNative (Object object, TransferData transferData){
	if (!checkText(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	transferData.result = COM.E_FAIL;
	String string = (String)object;
	switch (transferData.type) {
		case COM.CF_UNICODETEXT: {
			int charCount = string.length ();
			char[] chars = new char[charCount+1];
			string.getChars (0, charCount, chars, 0);
			int byteCount = chars.length * 2;
			int /*long*/ newPtr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, byteCount);
			OS.MoveMemory(newPtr, chars, byteCount);
			transferData.stgmedium = new STGMEDIUM();
			transferData.stgmedium.tymed = COM.TYMED_HGLOBAL;
			transferData.stgmedium.unionField = newPtr;
			transferData.stgmedium.pUnkForRelease = 0;
			transferData.result = COM.S_OK;
			break;
		}
		case COM.CF_TEXT: {
			int count = string.length();
			char[] chars = new char[count + 1];
			string.getChars(0, count, chars, 0);
			int codePage = OS.GetACP();
			int cchMultiByte = OS.WideCharToMultiByte(codePage, 0, chars, -1, null, 0, null, null);
			if (cchMultiByte == 0) {
				transferData.stgmedium = new STGMEDIUM();
				transferData.result = COM.DV_E_STGMEDIUM;
				return;
			}
			int /*long*/ lpMultiByteStr = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, cchMultiByte);
			OS.WideCharToMultiByte(codePage, 0, chars, -1, lpMultiByteStr, cchMultiByte, null, null);
			transferData.stgmedium = new STGMEDIUM();
			transferData.stgmedium.tymed = COM.TYMED_HGLOBAL;
			transferData.stgmedium.unionField = lpMultiByteStr;
			transferData.stgmedium.pUnkForRelease = 0;
			transferData.result = COM.S_OK;
			break;
		}
	}
	return;
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of plain text to a java <code>String</code>.
 * 
 * @param transferData the platform specific representation of the data to be converted
 * @return a java <code>String</code> containing text if the conversion was successful; otherwise null
 * 
 * @see Transfer#javaToNative
 */
public Object nativeToJava(TransferData transferData){
	if (!isSupportedType(transferData) || transferData.pIDataObject == 0) return null;
	
	IDataObject data = new IDataObject(transferData.pIDataObject);
	data.AddRef();
	FORMATETC formatetc = transferData.formatetc;
	STGMEDIUM stgmedium = new STGMEDIUM();
	stgmedium.tymed = COM.TYMED_HGLOBAL;	
	transferData.result = getData(data, formatetc, stgmedium);
	data.Release();
	if (transferData.result != COM.S_OK) return null;
	int /*long*/ hMem = stgmedium.unionField;
	try {
		switch (transferData.type) {
			case CF_UNICODETEXTID: {
				/* Ensure byteCount is a multiple of 2 bytes */
				int size = OS.GlobalSize(hMem) / 2 * 2;
				if (size == 0) return null;
				char[] chars = new char[size/2];
				int /*long*/ ptr = OS.GlobalLock(hMem);
				if (ptr == 0) return null;
				try {
					OS.MoveMemory(chars, ptr, size);
					int length = chars.length;
					for (int i=0; i<chars.length; i++) {
						if (chars [i] == '\0') {
							length = i;
							break;
						}
					}
					return new String (chars, 0, length);
				} finally {
					OS.GlobalUnlock(hMem);	
				}
			}
			case CF_TEXTID: {
				int /*long*/ lpMultiByteStr = OS.GlobalLock(hMem);
				if (lpMultiByteStr == 0) return null;
				try {
					int codePage = OS.GetACP();
					int cchWideChar = OS.MultiByteToWideChar (codePage, OS.MB_PRECOMPOSED, lpMultiByteStr, -1, null, 0);
					if (cchWideChar == 0) return null;
					char[] lpWideCharStr = new char [cchWideChar - 1];
					OS.MultiByteToWideChar (codePage, OS.MB_PRECOMPOSED, lpMultiByteStr, -1, lpWideCharStr, lpWideCharStr.length);
					return new String(lpWideCharStr);
				} finally {
					OS.GlobalUnlock(hMem);
				}
			}
		}
	} finally {
		OS.GlobalFree(hMem);
	}
	return null;
}

protected int[] getTypeIds(){
	return new int[] {CF_UNICODETEXTID, CF_TEXTID};
}

protected String[] getTypeNames(){
	return new String[] {CF_UNICODETEXT, CF_TEXT};
}

boolean checkText(Object object) {
	return (object != null  && object instanceof String && ((String)object).length() > 0);
}

protected boolean validate(Object object) {
	return checkText(object);
}
}
