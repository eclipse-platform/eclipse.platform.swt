package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.TCHAR;
import org.eclipse.swt.internal.win32.DROPFILES;

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
	
	if (isSupportedType(transferData)) {

		String[] fileNames = (String[]) object;
		StringBuffer allFiles = new StringBuffer();
		for (int i = 0; i < fileNames.length; i++) {
			allFiles.append(fileNames[i]); 
			allFiles.append('\0'); // each name is null terminated
		}
		TCHAR buffer = new TCHAR(0, allFiles.toString(), true); // there is an extra null terminator at the very end
		
		DROPFILES dropfiles = new DROPFILES();
		dropfiles.pFiles = DROPFILES.sizeof;
		dropfiles.pt_x = dropfiles.pt_y = 0;
		dropfiles.fNC = 0;
		dropfiles.fWide = COM.IsUnicode ? 1 : 0;
		
		// Allocate the memory because the caller (DropTarget) has not handed it in
		// The caller of this method must release the data when it is done with it.
		int byteCount = buffer.length() * TCHAR.sizeof;
		int newPtr = COM.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, DROPFILES.sizeof + byteCount);
		COM.MoveMemory(newPtr, dropfiles, DROPFILES.sizeof);
		COM.MoveMemory(newPtr + DROPFILES.sizeof, buffer, byteCount);
		
		transferData.stgmedium = new STGMEDIUM();
		transferData.stgmedium.tymed = COM.TYMED_HGLOBAL;
		transferData.stgmedium.unionField = newPtr;
		transferData.stgmedium.pUnkForRelease = 0;
		transferData.result = COM.S_OK;
		return;
	}
	
	// did not match the TYMED
	transferData.stgmedium = new STGMEDIUM();
	transferData.result = COM.DV_E_TYMED;
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
		TCHAR lpszFile = new TCHAR(0, size);	
		// Get file name and append it to string
		COM.DragQueryFile(stgmedium.unionField, i, lpszFile, size);
		fileNames[i] = lpszFile.toString(0, lpszFile.strlen());
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
