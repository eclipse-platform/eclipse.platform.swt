package org.eclipse.swt.dnd;

import org.eclipse.swt.internal.Converter;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
public class RTFTransfer extends ByteArrayTransfer {

	private static RTFTransfer _instance = new RTFTransfer();
	private static final String TYPENAME = "RTF";
	private static final int TYPEID = registerType(TYPENAME);

private RTFTransfer() {
}
public static RTFTransfer getInstance () {
	return _instance;
}
public void javaToNative (Object object, TransferData transferData){
	if (object == null || !(object instanceof String)) return;
	byte [] buffer = Converter.wcsToMbcs (null, (String)object, false);
	super.javaToNative(buffer, transferData);
}
public Object nativeToJava(TransferData transferData){
	/// get byte array from super
	byte[] buffer = (byte[])super.nativeToJava(transferData);
	if (buffer == null) return null;
	// convert byte array to a string
	char [] unicode = Converter.mbcsToWcs (null, buffer);
	return new String (unicode);
}
protected String[] getTypeNames(){
	return new String[]{TYPENAME};
}
protected int[] getTypeIds(){
	return new int[]{TYPEID};
}
}
