package org.eclipse.swt.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
import java.io.*;
import org.eclipse.swt.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.internal.ole.win32.*;

final class OleFile {
	IStorage rootStorage;
	File file;
	String streamName;

	static int READ = 0;
	static int WRITE = 1;
OleFile(File file, String streamName, int mode) {
	if (file == null || file.isDirectory())
		OLE.error(OLE.ERROR_INVALID_ARGUMENT);

	this.file = file;
	this.streamName = streamName;

	if (mode == READ)
		openForRead();
	if (mode == WRITE)
		openForWrite();
}
void dispose() {
	
	rootStorage.Release();
	rootStorage = null;
	file = null;
	streamName = null;
}
IStorage getRootStorage() {
	return rootStorage;
}
private void openForRead() {

	if (!file.exists())	return;

	char[] path = (file.getAbsolutePath()+"\0").toCharArray();
	if (COM.StgIsStorageFile(path) == COM.S_OK) {
		readStorageFile(path);
	} else {
		readTraditionalFile(path);
	}
	
}
private void openForWrite() {
	char[] filePath = (file.getAbsolutePath()+"\0").toCharArray();
	int[] address = new int[1];
	int mode = COM.STGM_TRANSACTED | COM.STGM_READWRITE | COM.STGM_SHARE_EXCLUSIVE | COM.STGM_CREATE;
	
	int result = COM.StgCreateDocfile(filePath, mode, 0, address);
	if (result != COM.S_OK)
		OLE.error(OLE.ERROR_CANNOT_CREATE_FILE, result);

	IStorage storage = new IStorage(address[0]);
		
	rootStorage = storage;
}
private void readStorageFile(char[] path) {
	
	int mode = COM.STGM_READ | COM.STGM_TRANSACTED | COM.STGM_SHARE_EXCLUSIVE;
	int[] address = new int[1];

	int result = COM.StgOpenStorage(path, 0, mode, 0, 0, address);
	if (result != COM.S_OK)
		OLE.error(OLE.ERROR_CANNOT_OPEN_FILE, result);
		
	rootStorage = new IStorage(address[0]);
	rootStorage.AddRef();
}
private void readTraditionalFile(char[] path) {

	if (streamName == null) OLE.error(OLE.ERROR_NULL_ARGUMENT);

	int mode = COM.STGM_DIRECT | COM.STGM_SHARE_EXCLUSIVE | COM.STGM_READWRITE | COM.STGM_CREATE;
	
	// Create a temporary storage object
	int[] address = new int[1];
	int result = COM.StgCreateDocfile(null, mode | COM.STGM_DELETEONRELEASE, 0, address);
	if (result != COM.S_OK)
		OLE.error(OLE.ERROR_CANNOT_OPEN_FILE, result);
	rootStorage = new IStorage(address[0]);
	rootStorage.AddRef();

	// Create a stream on the storage object with the name specified in streamName
	address = new int[1];
	result = rootStorage.CreateStream(streamName, mode, 0, 0, address);
	if (result != COM.S_OK)
		OLE.error(OLE.ERROR_CANNOT_OPEN_FILE, result);

	// Copy over data in file to named stream
	IStream stream = new IStream(address[0]);
	stream.AddRef();
	try {
	
		FileInputStream fileInput = new FileInputStream(file);
	
		int increment = 1024*4;
		byte[] buffer = new byte[increment];
		int count = 0;
		
		while((count = fileInput.read(buffer)) > 0){
			int pv = COM.CoTaskMemAlloc(count);
			OS.MoveMemory(pv, buffer, count);
			result = stream.Write(pv, count, null) ;
			COM.CoTaskMemFree(pv);
			if (result != COM.S_OK)
				OLE.error(OLE.ERROR_CANNOT_OPEN_FILE, result);
		}
		stream.Commit(COM.STGC_DEFAULT);

		fileInput.close();
	} catch (IOException err) {
		OLE.error(OLE.ERROR_CANNOT_OPEN_FILE);
	}
		
	stream.Release();

}
}
