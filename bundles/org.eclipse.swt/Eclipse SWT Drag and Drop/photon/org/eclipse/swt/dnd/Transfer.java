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
	if (formatName == "TEXT") return 10;
	if (formatName == "files") return 11;
	if (formatName == "RTF") return 12;
	return 0;
}
}
