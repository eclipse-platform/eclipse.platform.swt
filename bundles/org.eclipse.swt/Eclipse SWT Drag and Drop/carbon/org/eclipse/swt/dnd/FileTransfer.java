package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
/**
 * The <code>FileTransfer</code> class is used to transfer files in a drag and drop operation.
 */
public class FileTransfer extends ByteArrayTransfer {
	
private FileTransfer() {}
/**
 * Returns the singleton instance of the FileTransfer class.
 *
 * @return the singleton instance of the FileTransfer class
 */
public static FileTransfer getInstance () {
	return null;
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
}
/**
 * Converts a platform specific representation of a list of file names to a Java array of String.
 *
 * @param transferData the platform specific representation of the data that has been transferred
 * @return a Java array of String containing a list of file names if the conversion was successful;
 *         otherwise null
 */
public Object nativeToJava(TransferData transferData) {
	return null;
}
protected String[] getTypeNames(){
	return null;
}
protected int[] getTypeIds(){
	return null;
}
}
