package org.eclipse.swt.dnd;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

/**
 * The class <code>ByteArrayTransfer</code> provides a platform specific 
 * mechanism for converting a java <code>byte[]</code> to a platform 
 * specific representation of the byte array and vice versa.  See 
 * <code>Transfer</code> for additional information.
 *
 * <p><code>ByteArrayTransfer</code> is never used directly but is sub-classed 
 * by transfer agents that convert between data in a java format such as a
 * <code>String</code> and a platform specific byte array.
 * 
 * <p>If the data you are converting <b>does not</b> map to a 
 * <code>byte[]</code>, you should sub-class <code>Transfer</code> directly 
 * and do your own mapping to a platform data type.</p>
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
 * This implementation of <code>javaToNative</code> converts a java 
 * <code>byte[]</code> to a platform specific representation.  For additional
 * information see <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>byte[]</code> containing the data to be converted
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
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
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of a byte array to a java <code>byte[]</code>.   
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>byte[]</code> containing the converted data if the 
 * conversion was successful; otherwise null
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
