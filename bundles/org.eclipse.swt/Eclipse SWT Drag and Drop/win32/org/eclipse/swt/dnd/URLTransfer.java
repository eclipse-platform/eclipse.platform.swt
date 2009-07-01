/*******************************************************************************
 * Copyright (c) 2000, 20007 IBM Corporation and others.
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
 * The class <code>URLTransfer</code> provides a platform specific mechanism 
 * for converting text in URL format represented as a java <code>String</code> 
 * to a platform specific representation of the data and vice versa. The string
 * must contain a fully specified url.
 * 
 * <p>An example of a java <code>String</code> containing a URL is shown below:</p>
 * 
 * <code><pre>
 *     String url = "http://www.eclipse.org";
 * </code></pre>
 *
 * @see Transfer
 * @since 3.4
 */
public class URLTransfer extends ByteArrayTransfer {

	static URLTransfer _instance = new URLTransfer();
	static final String CFSTR_INETURL = "UniformResourceLocator"; //$NON-NLS-1$
	static final int CFSTR_INETURLID = registerType(CFSTR_INETURL);

private URLTransfer() {}

/**
 * Returns the singleton instance of the URLTransfer class.
 *
 * @return the singleton instance of the URLTransfer class
 */
public static URLTransfer getInstance () {
	return _instance;
}

/**
 * This implementation of <code>javaToNative</code> converts a URL
 * represented by a java <code>String</code> to a platform specific representation.
 * 
 * @param object a java <code>String</code> containing a URL
 * @param transferData an empty <code>TransferData</code> object that will
 *  	be filled in on return with the platform specific format of the data
 * 
 * @see Transfer#nativeToJava
 */
public void javaToNative (Object object, TransferData transferData){
	if (!checkURL(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	transferData.result = COM.E_FAIL;
	// URL is stored as a null terminated byte array
	String url = ((String)object);
	int count = url.length();
	char[] chars = new char[count + 1];
	url.getChars(0, count, chars, 0);
	int codePage = OS.GetACP();
	int cchMultiByte = OS.WideCharToMultiByte(codePage, 0, chars, -1, null, 0, null, null);
	if (cchMultiByte == 0) {
		transferData.stgmedium = new STGMEDIUM();
		transferData.result = COM.DV_E_STGMEDIUM;
		return;
	}
	int /*long*/ lpMultiByteStr = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, cchMultiByte);
	OS.WideCharToMultiByte(codePage, 0, chars, -1, lpMultiByteStr, cchMultiByte, null, null);
	transferData.stgmedium = new STGMEDIUM();
	transferData.stgmedium.tymed = COM.TYMED_HGLOBAL;
	transferData.stgmedium.unionField = lpMultiByteStr;
	transferData.stgmedium.pUnkForRelease = 0;
	transferData.result = COM.S_OK;
	return;
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform 
 * specific representation of a URL to a java <code>String</code>.
 * 
 * @param transferData the platform specific representation of the data to be converted
 * @return a java <code>String</code> containing a URL if the conversion was successful;
 * 		otherwise null
 * 
 * @see Transfer#javaToNative
 */
public Object nativeToJava(TransferData transferData){
	if (!isSupportedType(transferData) || transferData.pIDataObject == 0) return null;
	IDataObject data = new IDataObject(transferData.pIDataObject);
	data.AddRef();
	STGMEDIUM stgmedium = new STGMEDIUM();
	FORMATETC formatetc = transferData.formatetc;
	stgmedium.tymed = COM.TYMED_HGLOBAL;	
	transferData.result = getData(data, formatetc, stgmedium);
	data.Release();	
	if (transferData.result != COM.S_OK) return null;
	int /*long*/ hMem = stgmedium.unionField;
	try {
		int /*long*/ lpMultiByteStr = OS.GlobalLock(hMem);
		if (lpMultiByteStr == 0) return null;
		try {
			int codePage = OS.GetACP();
			int cchWideChar  = OS.MultiByteToWideChar (codePage, OS.MB_PRECOMPOSED, lpMultiByteStr, -1, null, 0);
			if (cchWideChar == 0) return null;
			char[] lpWideCharStr = new char [cchWideChar - 1];
			OS.MultiByteToWideChar (codePage, OS.MB_PRECOMPOSED, lpMultiByteStr, -1, lpWideCharStr, lpWideCharStr.length);
			return new String(lpWideCharStr);
		} finally {
			OS.GlobalUnlock(hMem);
		}
	} finally {
		OS.GlobalFree(hMem);
	}
}

protected int[] getTypeIds(){
	return new int[] {CFSTR_INETURLID};
}

protected String[] getTypeNames(){
	return new String[] {CFSTR_INETURL}; 
}

boolean checkURL(Object object) {
	return object != null && (object instanceof String) && ((String)object).length() > 0;
}

protected boolean validate(Object object) {
	return checkURL(object);
}
}
