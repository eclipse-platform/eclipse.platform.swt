package org.eclipse.swt.dnd;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.internal.gtk.OS;

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
	if ( transferData == null ) return false;
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
	transferData.pValue = OS.g_malloc(buffer.length);
	OS.memmove(transferData.pValue, buffer, buffer.length);
	transferData.length = buffer.length;
	transferData.format = 8;
	transferData.result = 1;
}

protected Object nativeToJava(TransferData transferData){
	if ( !isSupportedType(transferData) ||  transferData.pValue == 0 ) return null;
	int size = transferData.format * transferData.length / 8;
	byte[] buffer = new byte[size];
	OS.memmove(buffer, transferData.pValue, size);
	return buffer;
}

}
