package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
public class RTFTransfer extends ByteArrayTransfer {

	private static RTFTransfer _instance = new RTFTransfer();
	private static final String TYPENAME1 = "text/rtf\0";
	private static final int TYPEID1 = registerType(TYPENAME1);
	private static final String TYPENAME2 = "TEXT/RTF\0";
	private static final int TYPEID2 = registerType(TYPENAME2);
	private static final String TYPENAME3 = "application/rtf\0";
	private static final int TYPEID3 = registerType(TYPENAME3);

private RTFTransfer() {
}
public static RTFTransfer getInstance () {
	return _instance;
}
public void javaToNative (Object object, TransferData transferData){
	if (object == null || !(object instanceof String)) return;

	String text = (String)object;
	super.javaToNative(text.getBytes(), transferData);
}
public Object nativeToJava(TransferData transferData){
	// get byte array from super
	byte[] buffer = (byte[])super.nativeToJava(transferData);
	if (buffer == null) return null;
	// convert byte array to a string
	return new String(buffer);
}
protected String[] getTypeNames(){
	return new String[]{TYPENAME1, TYPENAME2, TYPENAME3};
}
protected int[] getTypeIds(){
	return new int[]{TYPEID1, TYPEID2, TYPEID3};
}
}
