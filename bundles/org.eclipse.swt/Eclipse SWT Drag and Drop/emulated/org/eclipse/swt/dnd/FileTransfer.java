package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
/**
 * The class <code>FileTransfer</code> provides a platform specific mechanism 
 * for converting a list of files represented as a java <code>String[]</code> to a 
 * platform specific representation of the data and vice versa.  
 * See <code>Transfer</code> for additional information.
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
 * This implementation of <code>javaToNative</code> converts a list of file names
 * represented by a java <code>String[]</code> to a platform specific representation.
 * Each <code>String</code> in the array contains the absolute path for a single 
 * file or directory.  For additional information see 
 * <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>String[]</code> containing the file names to be 
 * converted
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
public void javaToNative(Object object, TransferData transferData) {
}
/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of a list of file names to a java <code>String[]</code>.  
 * Each String in the array contains the absolute path for a single file or directory. 
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>String[]</code> containing a list of file names if the 
 * conversion was successful; otherwise null
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
