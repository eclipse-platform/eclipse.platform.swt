package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
  
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.photon.OS;

/**
 * The <code>FileTransfer</code> class is used to transfer files in a drag and drop operation.
 */
public class FileTransfer extends ByteArrayTransfer {
	
	private static FileTransfer _instance = new FileTransfer();
	private static final String TYPENAME = "files";
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
	byte [] data = new byte[0];
	String[] filenames = (String[])object;
	for (int i = 0; i < filenames.length; i++) {
		byte [] buffer = Converter.wcsToMbcs (null, filenames[i], true);		
		byte [] temp = data;
		data = new byte[ data.length + buffer.length];
		System.arraycopy(temp, 0, data, 0, temp.length);
		System.arraycopy(buffer, 0, data, data.length - buffer.length, buffer.length);
	}
	super.javaToNative(data, transferData);
}
/**
 * Converts a platform specific representation of a list of file names to a Java array of String.
 *
 * @param transferData the platform specific representation of the data that has been transferred
 * @return a Java array of String containing a list of file names if the conversion was successful;
 *         otherwise null
 */
public Object nativeToJava(TransferData transferData) {
	if (transferData.pData == 0 || !(isSupportedType(transferData))) return null;
	int size = transferData.length;
	if (size == 0) return null;
	byte[] buffer = new byte[size];
	OS.memmove(buffer, transferData.pData, size);
	String[] filenames = new String[0];
	int lastMark = 0;
	for (int i = 0; i < buffer.length; i++) {
		if ( buffer[i] == 0 ) {
			String s = new String(buffer, lastMark, i - lastMark );
			String[] temp = filenames;
			filenames = new String[ filenames.length + 1];
			System.arraycopy(temp, 0, filenames, 0 , temp.length);		
			filenames[ filenames.length - 1] = s;
			lastMark = i + 1;
		}
	}
	return filenames;
}
protected String[] getTypeNames(){
	return new String[]{TYPENAME};
}
protected int[] getTypeIds(){
	return new int[]{TYPEID};
}
}
