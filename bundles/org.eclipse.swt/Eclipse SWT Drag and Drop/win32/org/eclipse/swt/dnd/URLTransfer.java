/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import java.net.*;

import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

/**
 * The class <code>URLTransfer</code> provides a platform specific mechanism 
 * for converting text in URL format represented as a java <code>String[]</code> 
 * to a platform specific representation of the data and vice versa.  See 
 * <code>Transfer</code> for additional information. The first string in the 
 * array is mandatory and must contain the fully specified url.  The second 
 * string in the array is optional and if present contains the title for the
 * page.
 * 
 * <p>An example of a java <code>String[]</code> containing a URL is shown 
 * below:</p>
 * 
 * <code><pre>
 *     String[] urlData = new String[] {"http://www.eclipse.org", "Eclipse.org Main Page"};
 * </code></pre>
 */
/*public*/ class URLTransfer extends ByteArrayTransfer {

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
 * This implementation of <code>javaToNative</code> converts a URL and optionally a title
 * represented by a java <code>String[]</code> to a platform specific representation.
 * For additional information see <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>String[]</code> containing a URL and optionally, a title
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
public void javaToNative (Object object, TransferData transferData){
	if (!checkURL(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	transferData.result = COM.E_FAIL;
	// URL is stored as a null terminated byte array
	String url = ((String[])object)[0];
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
	int lpMultiByteStr = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, cchMultiByte);
	OS.WideCharToMultiByte(codePage, 0, chars, -1, lpMultiByteStr, cchMultiByte, null, null);
	transferData.stgmedium = new STGMEDIUM();
	transferData.stgmedium.tymed = COM.TYMED_HGLOBAL;
	transferData.stgmedium.unionField = lpMultiByteStr;
	transferData.stgmedium.pUnkForRelease = 0;
	transferData.result = COM.S_OK;
	return;
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of a URL and optionally, a title to a java <code>String[]</code>.
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>String[]</code> containing a URL and optionally a title if the 
 * conversion was successful; otherwise null
 */
public Object nativeToJava(TransferData transferData){
	if (!isSupportedType(transferData) || transferData.pIDataObject == 0) return null;
	IDataObject data = new IDataObject(transferData.pIDataObject);
	data.AddRef();
	STGMEDIUM stgmedium = new STGMEDIUM();
	FORMATETC formatetc = transferData.formatetc;
	stgmedium.tymed = COM.TYMED_HGLOBAL;	
	transferData.result = data.GetData(formatetc, stgmedium);
	data.Release();	
	if (transferData.result != COM.S_OK) return null;
	int hMem = stgmedium.unionField;
	try {
		int lpMultiByteStr = OS.GlobalLock(hMem);
		if (lpMultiByteStr == 0) return null;
		try {
			int codePage = OS.GetACP();
			int cchWideChar  = OS.MultiByteToWideChar (codePage, OS.MB_PRECOMPOSED, lpMultiByteStr, -1, null, 0);
			if (cchWideChar == 0) return null;
			char[] lpWideCharStr = new char [cchWideChar - 1];
			OS.MultiByteToWideChar (codePage, OS.MB_PRECOMPOSED, lpMultiByteStr, -1, lpWideCharStr, lpWideCharStr.length);
			return new String[]{new String(lpWideCharStr)};
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
	if (object == null  || !(object instanceof String[]) || ((String[])object).length == 0) return false;
	String[] strings = (String[])object;
	if (strings[0] == null || strings[0].length() == 0) return false;
	try {
		new URL(strings[0]);
	} catch (java.net.MalformedURLException e) {
		return false;
	}
	return true;
}

protected boolean validate(Object object) {
	return checkURL(object);
}
}
