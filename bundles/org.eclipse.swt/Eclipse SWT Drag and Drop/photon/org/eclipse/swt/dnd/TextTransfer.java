package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.photon.OS;

/**
 * The <code>TextTransfer</code> class is used to transfer text in a drag and drop operation.
 */
public class TextTransfer extends ByteArrayTransfer {

	private static TextTransfer _instance = new TextTransfer();
	private static final String TYPENAME = "TEXT";
	private static final int TYPEID = registerType(TYPENAME);

private TextTransfer() {
}
/**
 * Returns the singleton instance of the TextTransfer class.
 *
 * @return the singleton instance of the TextTransfer class
 */
public static TextTransfer getInstance () {
	return _instance;
}
/**
 * Converts a plain text Java String to a platform specific representation. 
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
 * @param object a Java String containing the data to be transferred
 * @param transferData an empty TransferData object; this object will be filled in on return
 *        with the platform specific format of the data
 */
public void javaToNative (Object object, TransferData transferData){
	if (object == null || !(object instanceof String)) return;
	byte [] buffer = Converter.wcsToMbcs (null, (String)object, true);
	super.javaToNative(buffer, transferData);
}
/**
 * Converts a platform specific representation of a string to a Java String.
 *
 * @param transferData the platform specific representation of the data that has been transferred
 * @return a Java String containing the transferred data if the conversion was successful;
 *         otherwise null
 */
public Object nativeToJava(TransferData transferData){

	if (transferData.pData == 0 || !(isSupportedType(transferData))) return null;
	
	int size = transferData.length;
	if (size == 0) return null;
	byte[] buffer = new byte[OS.strlen(transferData.pData)];
	OS.memmove(buffer, transferData.pData, buffer.length);

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
