package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.ole.win32.*;

/**
 * The <code>RTFTransfer</code> class is used to transfer text with the RTF format
 * in a drag and drop operation.
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
 * Converts a RTF-formatted Java String to a platform specific representation. 
 * <p>
 * On a successful conversion, the transferData.result field will be set as follows:
 * <ul>
 * <li>Windows: OLE.S_OK
 * <li>Motif: 0
 * </ul>
 * If this transfer agent is unable to perform the conversion,
 * the transferData.result field will be set to a failure value as follows:
 * <ul>
 * <li>Windows: OLE.DV_E_TYMED
 * <li>Motif: 1
 * </ul></p>
 *
 * @param object a Java String containing the data to be transferred
 * @param transferData an empty TransferData object; this object will be filled in on return
 *        with the platform specific format of the data
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
 * Converts a platform specific representation of a string to a Java String.
 *
 * @param transferData the platform specific representation of the data that has been transferred
 * @return a Java String containing the transferred data if the conversion was successful;
 *         otherwise null
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

	int lpMultiByteStr = COM.GlobalLock(stgmedium.unionField);
	if (lpMultiByteStr != 0) {
		try {
			int cchWideChar  = OS.MultiByteToWideChar (CodePage, OS.MB_PRECOMPOSED, lpMultiByteStr, -1, null, 0);
			if (cchWideChar != 0) {
				char[] lpWideCharStr = new char [cchWideChar];
				OS.MultiByteToWideChar (CodePage, OS.MB_PRECOMPOSED, lpMultiByteStr, -1, lpWideCharStr, cchWideChar);
				return new String(lpWideCharStr);
			}
		} finally {
			COM.GlobalUnlock(lpMultiByteStr);
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
