package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

/**
 * The class <code>ByteArrayTransfer</code> provides a platform specific mechanism for transforming
 * a Java array of bytes into a format that can be passed around in a Drag and Drop operation and vice
 * versa.
 *
 * <p>This abstract class can be subclassed to provided utilities for transforming Java data types
 * into the byte array based platform specific drag and drop data types.  See TextTransfer and 
 * FileTransfer for examples.  If the data you are transferring <b>does not</b> map to a byte array, 
 * you should sub-class Transfer directly and do your own mapping to the platform data types.</p>
 */
public abstract class ByteArrayTransfer extends Transfer {
public TransferData[] getSupportedTypes(){
	
	int[] types = getTypeIds();
	TransferData[] data = new TransferData[types.length];
	for (int i = 0; i < types.length; i++) {
		data[i] = new TransferData();
		data[i].type = types[i];
		data[i].formatetc = new FORMATETC();
		data[i].formatetc.cfFormat = types[i];
		data[i].formatetc.dwAspect = COM.DVASPECT_CONTENT;
		data[i].formatetc.lindex = -1;
		data[i].formatetc.tymed = COM.TYMED_HGLOBAL;	
	}
	
	return data;
}
public boolean isSupportedType(TransferData transferData){
	
	int[] types = getTypeIds();
	for (int i = 0; i < types.length; i++) {
		FORMATETC format = transferData.formatetc;
		if (format.cfFormat == types[i] &&
		    (format.dwAspect & COM.DVASPECT_CONTENT) == COM.DVASPECT_CONTENT && 
		    (format.tymed & COM.TYMED_HGLOBAL) == COM.TYMED_HGLOBAL  )
		    return true;
	}
	return false;
}
/**
 * Converts a Java byte array to a platform specific representation. 
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
 * @param object a Java byte array containing the data to be transferred
 * @param transferData an empty TransferData object; this object will be filled in on return
 *        with the platform specific format of the data
 */
protected void javaToNative (Object object, TransferData transferData){
	if (object == null || !(object instanceof byte[])) {
		transferData.result = COM.E_FAIL;
		return;
	}

	byte[] data = (byte[])object;
	
	if (isSupportedType(transferData)) {
		// Allocate the memory because the caller (DropTarget) has not handed it in
		// The caller of this method must release the data when it is done with it.
		int size = data.length;
		int newPtr = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, size);
		OS.MoveMemory(newPtr, data, size);
		
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
 * Converts a platform specific representation of a byte array to a Java byte array. 
 *
 * @param transferData the platform specific representation of the data that has been transferred
 * @return a Java byte array containing the transferred data if the conversion was successful;
 *         otherwise null
 */
protected Object nativeToJava(TransferData transferData){
	
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
	
	int size = OS.GlobalSize(stgmedium.unionField);
	byte[] buffer = new byte[size];
	int ptr = OS.GlobalLock(stgmedium.unionField);
	OS.MoveMemory(buffer, ptr, size);
	OS.GlobalUnlock(ptr);	
	OS.GlobalFree(stgmedium.unionField);
		
	return buffer;
}
}
