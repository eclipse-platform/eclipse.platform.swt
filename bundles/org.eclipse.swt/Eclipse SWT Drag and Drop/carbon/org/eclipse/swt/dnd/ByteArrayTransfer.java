package org.eclipse.swt.dnd;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public abstract class ByteArrayTransfer extends Transfer {
	
public TransferData[] getSupportedTypes() {
	int[] types= getTypeIds();
	TransferData[] data= new TransferData[types.length];
	for (int i= 0; i < types.length; i++) {
		data[i]= new TransferData();
		data[i].type= types[i];
	}
	return data;
}

public boolean isSupportedType(TransferData transferData){
	if (transferData == null) return false;
	int[] types= getTypeIds();
	for (int i= 0; i < types.length; i++) {
		if (transferData.type == types[i])
			return true;
	}
	return false;
}

protected void javaToNative (Object object, TransferData transferData) {
	if ((object == null) || !(object instanceof byte[]) || !(isSupportedType(transferData))) {
		transferData.result = -1;
		return;
	}
	byte[] orig = (byte[])object;
	byte[] buffer= new byte[orig.length];
	System.arraycopy(orig, 0, buffer, 0, orig.length);
	transferData.data = buffer;
	transferData.result = 0;
}

protected Object nativeToJava(TransferData transferData) {
	if (!isSupportedType(transferData)) return null;
	return transferData.data;
}

}