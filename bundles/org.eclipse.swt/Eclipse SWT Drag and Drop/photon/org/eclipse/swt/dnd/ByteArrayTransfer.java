package org.eclipse.swt.dnd;

import org.eclipse.swt.internal.photon.*;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
//	transferData.pValue = OS.XtMalloc(buffer.length + 1);
//	OS.memmove(transferData.pValue, buffer, buffer.length);
	transferData.length = buffer.length;
	transferData.format = 8;
	transferData.result = 1;
}
protected Object nativeToJava(TransferData transferData){

	if (transferData.pValue == 0 || !(isSupportedType(transferData))) return null;
	
	int size = transferData.format * transferData.length / 8;
	byte[] buffer = new byte[size];
//	OS.memmove(buffer, transferData.pValue, size);
	return buffer;
}
}
