package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.internal.ole.win32.*;

/**
 * The <code>FileTransfer</code> class is used to transfer files in a drag and drop operation.
 */
public class FileTransfer extends ByteArrayTransfer {
	
	private static FileTransfer _instance = new FileTransfer();
	
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

	if (object == null || !(object instanceof String[])) {
		transferData.result = COM.E_FAIL;
		return;
	}
		
	// build a byte array from data
	String[] fileNames = (String[]) object;
	int fileNameSize = 0;
	byte[][] files = new byte[fileNames.length][];
	for (int i = 0; i < fileNames.length; i++) {
		files[i] = (fileNames[i]+'\0').getBytes(); // each name is null terminated
		fileNameSize += files[i].length;
	}
	byte[] buffer = new byte[DROPFILES.sizeof + fileNameSize + 1]; // there is an extra null terminator at the very end
	DROPFILES dropfiles = new DROPFILES();
	dropfiles.pFiles = DROPFILES.sizeof;
	dropfiles.pt_x = dropfiles.pt_y = 0;
	dropfiles.fNC = 0;
	dropfiles.fWide = 0;	
	COM.MoveMemory(buffer, dropfiles, DROPFILES.sizeof);
	
	int offset = DROPFILES.sizeof;
	for (int i = 0; i < fileNames.length; i++) {
		System.arraycopy(files[i], 0, buffer, offset, files[i].length);
		offset += files[i].length;
	}

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
	
	if (!isSupportedType(transferData) || transferData.pIDataObject == 0) {
		transferData.result = COM.E_FAIL;
		return null;
	}
	
	// get file names from IDataObject
	IDataObject dataObject = new IDataObject(transferData.pIDataObject);
	dataObject.AddRef();
	
	FORMATETC formatetc = new FORMATETC();
	formatetc.cfFormat = COM.CF_HDROP;
	formatetc.ptd = 0;
	formatetc.dwAspect = COM.DVASPECT_CONTENT;
	formatetc.lindex = -1;
	formatetc.tymed = COM.TYMED_HGLOBAL;

	STGMEDIUM stgmedium = new STGMEDIUM();
	stgmedium.tymed = COM.TYMED_HGLOBAL;
			
	transferData.result = dataObject.GetData(formatetc, stgmedium);
	dataObject.Release();
	if (transferData.result != COM.S_OK) {
		return null;
	}
	
	// How many files are there?
	int count = COM.DragQueryFile(stgmedium.unionField, 0xFFFFFFFF, null, 0);
	String[] fileNames = new String[count];
	for (int i = 0; i < count; i++){
		// How long is the name ?
		int size = COM.DragQueryFile(stgmedium.unionField, i, null, 0) + 1;
		byte[] lpszFile = new byte[size];	
		// Get file name and append it to string
		COM.DragQueryFile(stgmedium.unionField, i, lpszFile, size);
		String fileName = new String(lpszFile);
		// remove terminating '\0'
		int index = fileName.indexOf("\0");
		fileName = fileName.substring(0, index);
		fileNames[i] = fileName;
	}
	COM.DragFinish(stgmedium.unionField); // frees data associated with HDROP data
	return fileNames;
}
protected int[] getTypeIds(){
	return new int[] {COM.CF_HDROP};
}
protected String[] getTypeNames(){
	return new String[] {"CF_HDROP"};
}
}
