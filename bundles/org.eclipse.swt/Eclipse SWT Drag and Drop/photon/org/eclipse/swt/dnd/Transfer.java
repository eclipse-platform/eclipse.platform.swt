package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
public abstract class Transfer {
abstract public TransferData[] getSupportedTypes();
abstract public boolean isSupportedType(TransferData transferData);
abstract protected String[] getTypeNames();
abstract protected int[] getTypeIds();
abstract protected void javaToNative (Object object, TransferData transferData);
abstract protected Object nativeToJava(TransferData transferData);
public static int registerType(String formatName){
	return 0;
}
}
