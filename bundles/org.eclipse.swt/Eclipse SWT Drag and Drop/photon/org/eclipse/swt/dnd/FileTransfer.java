package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
public class FileTransfer extends ByteArrayTransfer {
	
	private static FileTransfer _instance = new FileTransfer();
	private static final String TYPENAME = "text/uri-list\0";
	private static final int TYPEID = registerType(TYPENAME);

private FileTransfer() {}
public static FileTransfer getInstance () {
	return _instance;
}
public void javaToNative(Object object, TransferData transferData) {

	if (object == null || !(object instanceof String[])) return;
		
	// build a byte array from data
	String[] files = (String[])object;
	
	// create a string separated by "new lines" to represent list of files
	String nativeFormat = "file:";
	for (int i = 0, length = files.length; i < length; i++){
		nativeFormat += files[i]+"\r";
	}
	nativeFormat += "\0";
	// pass byte array on to super to convert to native
	super.javaToNative(nativeFormat.getBytes(), transferData);
}
public Object nativeToJava(TransferData transferData) {

	byte[] data = (byte[])super.nativeToJava(transferData);
	if (data == null) return null;
	String string  = new String(data);
	// parse data and convert string to array of files
	int start = string.indexOf("file:");
	if (start == -1) return null;
	start += 5;
	String[] fileNames = new String[0];
	while (start < string.length()) { 
		int end = string.indexOf("\r", start);
		if (end == -1) end = string.length() - 1;
		
		String fileName = string.substring(start, end);
		String[] newFileNames = new String[fileNames.length + 1];
		System.arraycopy(fileNames, 0, newFileNames, 0, fileNames.length);
		newFileNames[fileNames.length] = fileName;
		fileNames = newFileNames;

		start = string.indexOf("file:", end);
		if (start == -1) break;
		start += 5;
	}
	return fileNames;
}
protected String[] getTypeNames(){
	return new String[]{TYPENAME};
}
protected int[] getTypeIds(){
	return new int[]{TYPEID};
}
}
