package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.win32.TCHAR;
import org.eclipse.swt.internal.ole.win32.STGMEDIUM;
import org.eclipse.swt.internal.ole.win32.FORMATETC;
import org.eclipse.swt.internal.ole.win32.IDataObject;

/**
 * The <code>TextTransfer</code> class is used to transfer text in a drag and drop operation.
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
 * Converts a plain text Java String to a platform specific representation. 
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
	
	FORMATETC formatetc = transferData.formatetc;

	STGMEDIUM stgmedium = new STGMEDIUM();
	stgmedium.tymed = COM.TYMED_HGLOBAL;	
	transferData.result = data.GetData(formatetc, stgmedium);
	data.Release();
		
	if (transferData.result != COM.S_OK) {
		return null;
	}
	
	int size = COM.GlobalSize(stgmedium.unionField);
	TCHAR buffer = new TCHAR(0, size / TCHAR.sizeof);
	int ptr = COM.GlobalLock(stgmedium.unionField);
	COM.MoveMemory(buffer, ptr, size);
	COM.GlobalUnlock(ptr);	
	COM.GlobalFree(stgmedium.unionField);
	return buffer.toString(0, buffer.strlen());
}
protected int[] getTypeIds(){
	return new int[] {COM.IsUnicode ? COM.CF_UNICODETEXT : COM.CF_TEXT};
}
protected String[] getTypeNames(){
	return new String[] {COM.IsUnicode ? "CF_UNICODETEXT" : "CF_TEXT"};
}
}
