package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.motif.OS;

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
	}
	return data;
}
public boolean isSupportedType(TransferData transferData){
	if (transferData == null) return false;
	int[] types = getTypeIds();
	for (int i = 0; i < types.length; i++) {
		if (transferData.type == types[i]) return true;
	}
	return false;
}
protected void javaToNative (Object object, TransferData transferData){
	if ((object == null) || !(object instanceof byte[]) || !(isSupportedType(transferData))) {
		transferData.result = 0;
		return;
	}
	byte[] buffer = (byte[])object;	
	transferData.pValue = OS.XtMalloc(buffer.length);
	OS.memmove(transferData.pValue, buffer, buffer.length);
	transferData.length = buffer.length;
	transferData.format = 8;
	transferData.result = 1;
}
protected Object nativeToJava(TransferData transferData){
	if ( !(isSupportedType(transferData) || transferData.pValue == 0)) return null;
	int size = transferData.format * transferData.length / 8;
	byte[] buffer = new byte[size];
	OS.memmove(buffer, transferData.pValue, size);
	return buffer;
}
}
