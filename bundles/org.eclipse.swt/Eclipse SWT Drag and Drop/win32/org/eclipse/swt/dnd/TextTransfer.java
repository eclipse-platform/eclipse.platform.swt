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
	if (object == null || !(object instanceof String)) {
		transferData.result = COM.E_FAIL;
		return;
	}
	
	if (isSupportedType(transferData)) {
		TCHAR buffer = new TCHAR(0, (String)object, true);
		int byteCount = buffer.length() * TCHAR.sizeof;
		int newPtr = COM.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, byteCount);
		COM.MoveMemory(newPtr, buffer, byteCount);
		
		transferData.stgmedium = new STGMEDIUM();
		transferData.stgmedium.tymed = COM.TYMED_HGLOBAL;
		transferData.stgmedium.unionField = newPtr;
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
 * representation of plain text to a java <code>String</code>.
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>String</code> containing text if the 
 * conversion was successful; otherwise null
 */
public Object nativeToJava(TransferData transferData){
	if (!isSupportedType(transferData) || transferData.pIDataObject == 0) {
		transferData.result = COM.E_FAIL;
		return null;
	}
	
	IDataObject data = new IDataObject(transferData.pIDataObject);
	data.AddRef();
	
	FORMATETC formatetc = transferData.formatetc;

	STGMEDIUM stgmedium = new STGMEDIUM();
	stgmedium.tymed = COM.TYMED_HGLOBAL;	
	transferData.result = data.GetData(formatetc, stgmedium);
	data.Release();
		
	if (transferData.result != COM.S_OK) {
		return null;
	}
	
	int hMem = stgmedium.unionField;
	/* Ensure byteCount is a multiple of 2 bytes on UNICODE platforms */
	int size = COM.GlobalSize(hMem) / TCHAR.sizeof * TCHAR.sizeof;
	TCHAR buffer = new TCHAR(0, size / TCHAR.sizeof);
	int ptr = COM.GlobalLock(hMem);
	COM.MoveMemory(buffer, ptr, size);
	COM.GlobalUnlock(hMem);	
	COM.GlobalFree(hMem);
	return buffer.toString(0, buffer.strlen());
}
protected int[] getTypeIds(){
	return new int[] {COM.IsUnicode ? COM.CF_UNICODETEXT : COM.CF_TEXT};
}
protected String[] getTypeNames(){
	return new String[] {COM.IsUnicode ? "CF_UNICODETEXT" : "CF_TEXT"};  //$NON-NLS-1$//$NON-NLS-2$
}
}
