package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.internal.photon.*;

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
	transferData.pData = OS.malloc(buffer.length);
	OS.memmove(transferData.pData, buffer, buffer.length);
	transferData.length = buffer.length;
	transferData.result = 1;
}
protected Object nativeToJava(TransferData transferData){

	if (transferData.pData == 0 || !(isSupportedType(transferData))) return null;
	
	int size = transferData.length;
	if (size == 0) return null;
	byte[] buffer = new byte[size];
	OS.memmove(buffer, transferData.pData, size);
	return buffer;
}
}
