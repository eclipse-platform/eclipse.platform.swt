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

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.win32.TCHAR;
import org.eclipse.swt.internal.ole.win32.STGMEDIUM;
import org.eclipse.swt.internal.ole.win32.FORMATETC;
import org.eclipse.swt.internal.ole.win32.IDataObject;

/**
 * The class <code>TextTransfer</code> provides a platform specific mechanism 
 * for converting plain text represented as a java <code>String</code> 
 * to a platform specific representation of the data and vice versa.  See 
 * <code>Transfer</code> for additional information.
 * 
 * <p>An example of a java <code>String</code> containing plain text is shown 
 * below:</p>
 * 
 * <code><pre>
 *     String textData = "Hello World";
 * </code></pre>
 */
public class TextTransfer extends ByteArrayTransfer {

	private static TextTransfer _instance = new TextTransfer();
	private static final String CF_UNICODETEXT = "CF_UNICODETEXT";
	private static final String CF_TEXT = "CF_TEXT";
	private static final int CF_UNICODETEXTID = COM.CF_UNICODETEXT;
	private static final int CF_TEXTID = COM.CF_TEXT;
	private static int CodePage = COM.GetACP();
	
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
 * For additional information see <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>String</code> containing text
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
public void javaToNative (Object object, TransferData transferData){
	transferData.result = COM.E_FAIL;
	if (object == null || !(object instanceof String)) return;
	String string = (String)object;
	if (string.length() == 0) return;
	if (!isSupportedType(transferData)) {
		// did not match the TYMED
		transferData.stgmedium = new STGMEDIUM();
		transferData.result = COM.DV_E_TYMED;
		return;
	}
	switch (transferData.type) {
		case COM.CF_UNICODETEXT: {
			int charCount = string.length ();
			char[] chars = new char[charCount+1];
			string.getChars (0, charCount, chars, 0);
			int byteCount = chars.length * 2;
			int newPtr = COM.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, byteCount);
			COM.MoveMemory(newPtr, chars, byteCount);
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
			int cchMultiByte = COM.WideCharToMultiByte(CodePage, 0, chars, -1, null, 0, null, null);
			if (cchMultiByte == 0) {
				transferData.stgmedium = new STGMEDIUM();
				transferData.result = COM.DV_E_STGMEDIUM;
				return;
			}
			int lpMultiByteStr = COM.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, cchMultiByte);
			COM.WideCharToMultiByte(CodePage, 0, chars, -1, lpMultiByteStr, cchMultiByte, null, null);
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
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>String</code> containing text if the 
 * conversion was successful; otherwise null
 */
public Object nativeToJava(TransferData transferData){
	if (!isSupportedType(transferData) || transferData.pIDataObject == 0) return null;
	
	IDataObject data = new IDataObject(transferData.pIDataObject);
	data.AddRef();
	FORMATETC formatetc = transferData.formatetc;
	STGMEDIUM stgmedium = new STGMEDIUM();
	stgmedium.tymed = COM.TYMED_HGLOBAL;	
	transferData.result = data.GetData(formatetc, stgmedium);
	data.Release();
	if (transferData.result != COM.S_OK) return null;
	int hMem = stgmedium.unionField;
	try {
		switch (transferData.type) {
			case CF_UNICODETEXTID: {
				/* Ensure byteCount is a multiple of 2 bytes */
				int size = COM.GlobalSize(hMem) / 2 * 2;
				if (size == 0) return null;
				char[] chars = new char[size/2];
				int ptr = COM.GlobalLock(hMem);
				if (ptr == 0) return null;
				try {
					COM.MoveMemory(chars, ptr, size);
					int length = chars.length;
					for (int i=0; i<chars.length; i++) {
						if (chars [i] == '\0') {
							length = i;
							break;
						}
					}
					return new String (chars, 0, length);
				} finally {
					COM.GlobalUnlock(hMem);	
				}
			}
			case CF_TEXTID: {
				int lpMultiByteStr = COM.GlobalLock(hMem);
				if (lpMultiByteStr == 0) return null;
				try {
					int cchWideChar = COM.MultiByteToWideChar (CodePage, COM.MB_PRECOMPOSED, lpMultiByteStr, -1, null, 0);
					if (cchWideChar == 0) return null;
					char[] lpWideCharStr = new char [cchWideChar - 1];
					COM.MultiByteToWideChar (CodePage, COM.MB_PRECOMPOSED, lpMultiByteStr, -1, lpWideCharStr, lpWideCharStr.length);
					return new String(lpWideCharStr);
				} finally {
					COM.GlobalUnlock(hMem);
				}
			}
		}
	} finally {
		COM.GlobalFree(hMem);
	}
	return null;
}

protected int[] getTypeIds(){
	return new int[] {CF_UNICODETEXTID, CF_TEXTID};
}

protected String[] getTypeNames(){
	return new String[] {CF_UNICODETEXT, CF_TEXT};
}

}
