package org.eclipse.swt.dnd;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
import org.eclipse.swt.internal.ole.win32.COM;

/**
 *
 * The <code>TextTransfer</code> class is used to transfer text in a drag and drop operation.
 *
 */
public class TextTransfer extends ByteArrayTransfer {

	private static TextTransfer _instance = new TextTransfer();
private TextTransfer() {}
/**
 *
 * Returns the singleton instance of the TextTransfer class.
 *
 * @return the singleton instance of the TextTransfer class
 *
 */
public static TextTransfer getInstance () {
	return _instance;
}
/**
 *
 * Converts a Java String to a platform specific representation of the string. 
 *
 * <p>On a successful conversion, the transferData.result field will be set to OLE.S_OK.
 * If this transfer agent is unable to perform the conversion, the transferData.result field 
 * will be set to the failure value of OLE.DV_E_TYMED.</p>
 *
 * @param object a Java String containing the data to be transferred
 *
 * @param transferData an empty TransferData object; this object will be filled in on return
 *						with the platform specific format of the data
 *
 */
public void javaToNative (Object object, TransferData transferData){
	if (object == null || !(object instanceof String)) return;
		
	// CF_TEXT is stored as a null terminated byte array
	// create a byte array from object
	String text = (String) object+'\0';
	// pass byte array on to super to convert to native
	super.javaToNative(text.getBytes(), transferData);
}
/**
 *
 * Converts a platform specific representation of a string to a Java String.
 *
 * @param transferData the platform specific representation of the data that has been transferred
 *
 * @return a Java String containing the transferred data if the conversion was successful; otherwise null
 *
 */
public Object nativeToJava(TransferData transferData){
	// get byte array from super
	byte[] buffer = (byte[])super.nativeToJava(transferData);
	if (buffer == null) return null;
	// convert byte array to a string
	String string = new String(buffer);
	// remove null terminator
	int index = string.indexOf("\0");
	string = string.substring(0, index);
	return string;
}
protected int[] getTypeIds(){
	return new int[] {COM.CF_TEXT};
}
protected String[] getTypeNames(){
	return new String[] {"CF_TEXT"};
}
}
