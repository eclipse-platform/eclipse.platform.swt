package org.eclipse.swt.dnd;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.ole.win32.*;

/**
 * The class <code>RTFTransfer</code> provides a platform specific mechanism 
 * for converting text in RTF format represented as a java <code>String</code> 
 * to a platform specific representation of the data and vice versa.  See 
 * <code>Transfer</code> for additional information.
 * 
 * <p>An example of a java <code>String</code> containing RTF text is shown 
 * below:</p>
 * 
 * <code><pre>
 *     String rtfData = "{\\rtf1{\\colortbl;\\red255\\green0\\blue0;}\\uc1\\b\\i Hello World}";
 * </code></pre>
 */
public class RTFTransfer extends ByteArrayTransfer {

	private static final String CF_RTF_NAME = "Rich Text Format";
	private static final int CF_RTF = registerType(CF_RTF_NAME);
	private static RTFTransfer _instance = new RTFTransfer();
	private static int CodePage = OS.GetACP ();

private RTFTransfer() {}
/**
 * Returns the singleton instance of the RTFTransfer class.
 *
 * @return the singleton instance of the RTFTransfer class
 */
public static RTFTransfer getInstance () {
	return _instance;
}

/**
 * This implementation of <code>javaToNative</code> converts RTF-formatted text
 * represented by a java <code>String</code> to a platform specific representation.
 * For additional information see <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>String</code> containing RTF text
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
public void javaToNative (Object object, TransferData transferData){
	if (object == null || !(object instanceof String)) {
		transferData.result = COM.E_FAIL;
		return;
	}
	// CF_RTF is stored as a null terminated byte array
	if (isSupportedType(transferData)) {
		String string = (String)object;
		int count = string.length ();
		char [] buffer = new char [count + 1];
		string.getChars (0, count, buffer, 0);
		int cchMultiByte = OS.WideCharToMultiByte (CodePage, 0, buffer, -1, null, 0, null, null);
		if (cchMultiByte == 0) {
			transferData.stgmedium = new STGMEDIUM();
			transferData.result = COM.DV_E_STGMEDIUM;
			return;
		}
		int lpMultiByteStr = COM.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, cchMultiByte);
		OS.WideCharToMultiByte (CodePage, 0, buffer, -1, lpMultiByteStr, cchMultiByte, null, null);
		transferData.stgmedium = new STGMEDIUM();
		transferData.stgmedium.tymed = COM.TYMED_HGLOBAL;
		transferData.stgmedium.unionField = lpMultiByteStr;
		transferData.stgmedium.pUnkForRelease = 0;
		transferData.result = COM.S_OK;
		return;
	}
	
	// did not match the TYMED
	transferData.stgmedium = new STGMEDIUM();
	transferData.result = COM.DV_E_TYMED;
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of RTF text to a java <code>String</code>.
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>String</code> containing RTF text if the 
 * conversion was successful; otherwise null
 */
public Object nativeToJava(TransferData transferData){
	if (!isSupportedType(transferData) || transferData.pIDataObject == 0) {
		transferData.result = COM.E_FAIL;
		return null;
	}
	
	IDataObject data = new IDataObject(transferData.pIDataObject);
	data.AddRef();
	STGMEDIUM stgmedium = new STGMEDIUM();
	FORMATETC formatetc = transferData.formatetc;
	stgmedium.tymed = COM.TYMED_HGLOBAL;	
	transferData.result = data.GetData(formatetc, stgmedium);
	data.Release();	
	if (transferData.result != COM.S_OK) return null;

	int hMem = stgmedium.unionField;
	int lpMultiByteStr = COM.GlobalLock(hMem);
	if (lpMultiByteStr != 0) {
		try {
			int cchWideChar  = OS.MultiByteToWideChar (CodePage, OS.MB_PRECOMPOSED, lpMultiByteStr, -1, null, 0);
			if (cchWideChar != 0) {
				char[] lpWideCharStr = new char [cchWideChar - 1];
				OS.MultiByteToWideChar (CodePage, OS.MB_PRECOMPOSED, lpMultiByteStr, -1, lpWideCharStr, lpWideCharStr.length);
				return new String(lpWideCharStr);
			}
		} finally {
			COM.GlobalUnlock(hMem);
		}
	}
	return null;
}
protected int[] getTypeIds(){
	return new int[] {CF_RTF};
}
protected String[] getTypeNames(){
	return new String[] {"CF_RTF"};
}
}
