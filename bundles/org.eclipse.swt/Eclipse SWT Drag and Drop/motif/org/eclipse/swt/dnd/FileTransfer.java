package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.internal.Converter;

/**
 * The <code>FileTransfer</code> class is used to transfer files in a drag and drop operation.
 */
public class FileTransfer extends ByteArrayTransfer {
	
	private static FileTransfer _instance = new FileTransfer();
	private static final String TYPENAME = "text/uri-list";
	private static final int TYPEID = registerType(TYPENAME);

private FileTransfer() {}
/**
 * Returns the singleton instance of the FileTransfer class.
 *
 * @return the singleton instance of the FileTransfer class
 */
public static FileTransfer getInstance () {
	return _instance;
}
/**
 * Converts a list of filenames to a platform specific representation. 
 * <p>
 * On a successful conversion, the transferData.result field will be set as follows:
 * <ul>
 * <li>Windows: OLE.S_OK
 * <li>Motif: 0
 * </ul>
 * If this transfer agent is unable to perform the conversion,
 * the transferData.result field will be set to a failure value as follows:
 * <ul>
 * <li>Windows: OLE.DV_E_TYMED
 * <li>Motif: 1
 * </ul></p>
 *
 * @param object a list of file names
 * @param transferData an empty TransferData object; this object will be filled in on return
 *        with the platform specific format of the data
 */
public void javaToNative(Object object, TransferData transferData) {
	if (object == null || !(object instanceof String[])) return;
	// build a byte array from data
	String[] files = (String[])object;
	
	// create a string separated by "new lines" to represent list of files
	String nativeFormat = "";
	for (int i = 0, length = files.length; i < length; i++){
		nativeFormat += "file:"+files[i]+"\r";
	}
	byte[] buffer = Converter.wcsToMbcs(null, nativeFormat, true);
	// pass byte array on to super to convert to native
	super.javaToNative(buffer, transferData);
}
/**
 * Converts a platform specific representation of a list of file names to a Java array of String.
 *
 * @param transferData the platform specific representation of the data that has been transferred
 * @return a Java array of String containing a list of file names if the conversion was successful;
 *         otherwise null
 */
public Object nativeToJava(TransferData transferData) {

	byte[] data = (byte[])super.nativeToJava(transferData);
	if (data == null) return null;
	char[] unicode = Converter.mbcsToWcs(null, data);
	String string  = new String(unicode);
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
