package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
public class FileTransfer extends ByteArrayTransfer {
	
	private static FileTransfer _instance = new FileTransfer();
	private static final String TYPENAME = "files";
	private static final int TYPEID = registerType(TYPENAME);

private FileTransfer() {}
public static FileTransfer getInstance () {
	return _instance;
}
public void javaToNative(Object object, TransferData transferData) {
	DND.error(org.eclipse.swt.SWT.ERROR_NOT_IMPLEMENTED);
}
public Object nativeToJava(TransferData transferData) {
	DND.error(org.eclipse.swt.SWT.ERROR_NOT_IMPLEMENTED);
	return null;
}
protected String[] getTypeNames(){
	return new String[]{TYPENAME};
}
protected int[] getTypeIds(){
	return new int[]{TYPEID};
}
}
