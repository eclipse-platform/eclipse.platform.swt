package org.eclipse.swt.dnd;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.widgets.*;

/**
 * The <code>Clipboard</code> provides a mechanism for transferring data from one
 * application to another or within an application.
 * 
 * <p>IMPORTANT: This class is <em>not</em> intended to be subclassed.</p>
 */
public class Clipboard {

	// ole interfaces
	private COMObject iDataObject;
	private int refCount;

	private final int MAX_RETRIES = 10;
	private Transfer[] transferAgents = new Transfer[0];
	private Object[] data = new Object[0];

/**
 * Constructs a new instance of this class.  Creating an instance of a Clipboard
 * may cause system resources to be allocated depending on the platform.  It is therefore
 * mandatory that the Clipboard instance be disposed when no longer required.
 *
 * @param display the display on which to allocate the clipboard
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see Clipboard#dispose
 * @see Clipboard#checkSubclass
 */
public Clipboard(Display display) {	
	checkSubclass ();
	if (display == null) {
		display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
	}
	if (display.getThread() != Thread.currentThread()) {
		SWT.error(SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	
	createCOMInterfaces();
	this.AddRef();
}
/**
 * Checks that this class can be subclassed.
 * <p>
 * The SWT class library is intended to be subclassed 
 * only at specific, controlled points. This method enforces this
 * rule unless it is overridden.
 * </p><p>
 * <em>IMPORTANT:</em> By providing an implementation of this
 * method that allows a subclass of a class which does not 
 * normally allow subclassing to be created, the implementer
 * agrees to be fully responsible for the fact that any such
 * subclass will likely fail between SWT releases and will be
 * strongly platform specific. No support is provided for
 * user-written classes which are implemented in this fashion.
 * </p><p>
 * The ability to subclass outside of the allowed SWT classes
 * is intended purely to enable those not on the SWT development
 * team to implement patches in order to get around specific
 * limitations in advance of when those limitations can be
 * addressed by the team. Subclassing should not be attempted
 * without an intimate and detailed understanding of the hierarchy.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = Clipboard.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}

/**
 * Disposes of the operating system resources associated with the clipboard. 
 * The data will still be available on the system clipboard after the dispose 
 * method is called.  
 * 
 * <p>NOTE: On some platforms the data will not be available once the application
 * has exited or the display has been disposed.</p>
 */
public void dispose () {
	this.Release();
}

/**
 * Retrieve the data of the specified type currently available on the system clipboard.  Refer to the 
 * specific subclass of <code>Tramsfer</code> to determine the type of object returned.
 * 
 * <p>The following snippet shows text and RTF text being retrieved from the clipboard:</p>
 * 
 *    <code><pre>
 *    Clipboard clipboard = new Clipboard(display);
 *    TextTransfer textTransfer = TextTransfer.getInstance();
 *    String textData = (String)clipboard.getContents(textTransfer);
 *    if (textData != null) System.out.println("Text is "+textData);
 *    RTFTransfer rtfTransfer = RTFTransfer.getInstance();
 *    String rtfData = (String)clipboard.getContents(rtfTransfer);
 *    if (rtfData != null) System.out.println("RTF Text is "+rtfData);
 *    clipboard.dispose();
 *    </code></pre>
 * 
 * @see Transfer
 * 
 * @param transfer the transfer agent for the type of data being requested
 * 
 * @return the data obtained from the clipboard or null if no data of this type is available
 */
public Object getContents(Transfer transfer) {
	int[] ppv = new int[1];
	int retries = 0;
	while ((COM.OleGetClipboard(ppv) != COM.S_OK) && retries < MAX_RETRIES) {
		// Clipboard may be in use by some other application.
		// Wait for 10 milliseconds before trying again.
		try {Thread.sleep(10);} catch (InterruptedException e) {}
		retries++;
	}
	if (retries == MAX_RETRIES) return null;
	
	IDataObject dataObject = new IDataObject(ppv[0]);
	
	TransferData[] allowed = transfer.getSupportedTypes();
	TransferData match = null;
	for (int i = 0; i < allowed.length; i++) {
		if (dataObject.QueryGetData(allowed[i].formatetc) == COM.S_OK) {
			match = allowed[i];
			break;
		}
	}
	if (match == null) return null;
	
	match.pIDataObject = ppv[0];
	return transfer.nativeToJava(match);
}
/**
 * Place data of the specified type on the system clipboard.  More than one type of
 * data can be placed on the system clipboard at the same time.  Setting the data 
 * clears any previous data of the same type from the system clipboard and also
 * clears data of any other type currently on the system clipboard.
 * 
 * <p>NOTE: On some platforms, the data is immediately copied to the system
 * clipboard but on other platforms it is provided upon request.  As a result, if the 
 * application modifes the data object it has set on the clipboard, that modification 
 * may or may not be available when the data is subsequently requested.</p>
 *
 * <p>The following snippet shows text and RTF text being set on the clipboard:</p>
 * 
 * <code><pre>
 * 	Clipboard clipboard = new Clipboard(display);
 *		String textData = "Hello World";
 *		String rtfData = "{\\rtf1\\b\\i Hello World}";
 *		TextTransfer textTransfer = TextTransfer.getInstance();
 *		RTFTransfer rtfTransfer = RTFTransfer.getInstance();
 *		clipboard.setContents(new Object[]{textData, rtfData}, new Transfer[]{textTransfer, rtfTransfer});
 *		clipboard.dispose();
 * </code></pre>
 *
 * @param data the data to be set in the clipboard
 * @param dataTypes the transfer agents that will convert the data to its platform 
 * specific format; each entry in the data array must have a corresponding dataType
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if data is null or datatypes is null 
 *          or the length of data is not the same as the length of dataTypes</li>
 * </ul>
 *  @exception SWTError <ul>
 *    <li>ERROR_CANNOT_SET_CLIPBOARD - if the clipboard is locked or 
 *         otherwise unavailable</li>
 * </ul>
 */
public void setContents(Object[] data, Transfer[] dataTypes){
	if (data == null || dataTypes == null || data.length != dataTypes.length) {
		DND.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	
	this.data = data;
	this.transferAgents = dataTypes;
	
	int retries = 0;
	int result = 0;
	while ((result = COM.OleSetClipboard(this.iDataObject.getAddress())) != COM.S_OK && retries < MAX_RETRIES){
		// Clipboard may be in use by some other application.
		// Wait for 10 milliseconds before trying again.
		try {Thread.sleep(10);} catch (InterruptedException e) {}
		retries++;
	}
	if (retries == MAX_RETRIES) {
		DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD, result);
	}
	
	if (COM.OleIsCurrentClipboard(this.iDataObject.getAddress()) != COM.S_OK) return;
	
	retries = 0;
	while ((COM.OleFlushClipboard() != COM.S_OK)  && (retries < MAX_RETRIES)) {
		try {Thread.sleep(10);} catch (InterruptedException e) {}
		retries++;
	}
	
	this.data = new Object[0];
	this.transferAgents = new Transfer[0];
}
private int AddRef() {
	refCount++;
	return refCount;
}
private void createCOMInterfaces() {
	// register each of the interfaces that this object implements
	iDataObject = new COMObject(new int[]{2, 0, 0, 2, 2, 1, 2, 3, 2, 4, 1, 1}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return GetData(args[0], args[1]);}
		// method4 GetDataHere - not implemented
		public int method5(int[] args) {return QueryGetData(args[0]);}
		// method6 GetCanonicalFormatEtc - not implemented
		// method7 SetData - not implemented
		public int method8(int[] args) {return EnumFormatEtc(args[0], args[1]);}
		// method9 DAdvise - not implemented
		// method10 DUnadvise - not implemented
		// method11 EnumDAdvise - not implemented
	};
}
private void disposeCOMInterfaces() {
	if (iDataObject != null)
		iDataObject.dispose();
	iDataObject = null;
}
private int EnumFormatEtc(int dwDirection, int ppenumFormatetc) {
	// only allow getting of data - SetData is not currently supported
	if (dwDirection == COM.DATADIR_SET) return COM.E_NOTIMPL;
	
	// what types have been registered?
	TransferData[] allowedDataTypes = new TransferData[0];
	for (int i = 0; i < transferAgents.length; i++){
		TransferData[] formats = transferAgents[i].getSupportedTypes();
		TransferData[] newAllowedDataTypes = new TransferData[allowedDataTypes.length + formats.length];
		System.arraycopy(allowedDataTypes, 0, newAllowedDataTypes, 0, allowedDataTypes.length);
		System.arraycopy(formats, 0, newAllowedDataTypes, allowedDataTypes.length, formats.length);
		allowedDataTypes = newAllowedDataTypes;
	}
	
	OleEnumFORMATETC enumFORMATETC = new OleEnumFORMATETC();
	enumFORMATETC.AddRef();
	
	FORMATETC[] formats = new FORMATETC[allowedDataTypes.length];
	for (int i = 0; i < formats.length; i++){
		formats[i] = allowedDataTypes[i].formatetc;
	}
	enumFORMATETC.setFormats(formats);
	
	COM.MoveMemory(ppenumFormatetc, new int[] {enumFORMATETC.getAddress()}, 4);
	return COM.S_OK;
}
private int GetData(int pFormatetc, int pmedium) {
	/* Called by a data consumer to obtain data from a source data object. 
	   The GetData method renders the data described in the specified FORMATETC 
	   structure and transfers it through the specified STGMEDIUM structure. 
	   The caller then assumes responsibility for releasing the STGMEDIUM structure.
	*/
	if (pFormatetc == 0 || pmedium == 0) return COM.E_INVALIDARG;

	if (QueryGetData(pFormatetc) != COM.S_OK) return COM.DV_E_FORMATETC;

	TransferData transferData = new TransferData();
	transferData.formatetc = new FORMATETC();
	COM.MoveMemory(transferData.formatetc, pFormatetc, FORMATETC.sizeof);
	transferData.type = transferData.formatetc.cfFormat;
	transferData.stgmedium = new STGMEDIUM();
	transferData.result = COM.E_FAIL;
		
	// get matching transfer agent to perform conversion
	int transferIndex = -1;
	for (int i = 0; i < transferAgents.length; i++){
		if (transferAgents[i].isSupportedType(transferData)){
			transferIndex = i;
			break;
		}
	}
	if (transferIndex == -1) return COM.DV_E_FORMATETC;
	
	transferAgents[transferIndex].javaToNative(data[transferIndex], transferData);
	COM.MoveMemory(pmedium, transferData.stgmedium, STGMEDIUM.sizeof);
	return transferData.result;
}
private int QueryGetData(int pFormatetc) {

	if (transferAgents == null) return COM.E_FAIL;

	TransferData transferData = new TransferData();
	transferData.formatetc = new FORMATETC();
	COM.MoveMemory(transferData.formatetc, pFormatetc, FORMATETC.sizeof);
	transferData.type = transferData.formatetc.cfFormat;
	
	// is this type supported by the transfer agent?
	for (int i = 0; i < transferAgents.length; i++){
		if (transferAgents[i].isSupportedType(transferData))
			return COM.S_OK;
	}
	
	return COM.DV_E_FORMATETC;
}
private int QueryInterface(int riid, int ppvObject) {
	
	if (riid == 0 || ppvObject == 0)
		return COM.E_INVALIDARG;
	GUID guid = new GUID();
	COM.MoveMemory(guid, riid, GUID.sizeof);
	
	if (COM.IsEqualGUID(guid, COM.IIDIUnknown) || COM.IsEqualGUID(guid, COM.IIDIDataObject) ) {
		COM.MoveMemory(ppvObject, new int[] {iDataObject.getAddress()}, 4);
		AddRef();
		return COM.S_OK;
	}
	
	COM.MoveMemory(ppvObject, new int[] {0}, 4);
	return COM.E_NOINTERFACE;
}
private int Release() {
	refCount--;
	if (refCount == 0) {
		disposeCOMInterfaces();
		COM.CoFreeUnusedLibraries();
	}
	
	return refCount;
}

/**
 * Returns a platform specific list of the data types currently available on the 
 * system clipboard.
 * 
 * <p>Note: <code>getAvailableTypeNames</code> is a utility for writing a Transfer 
 * sub-class.  It should NOT be used within an application because it provides 
 * platform specific information.</p>
 * 
 * @returns a platform specific list of the data types currently available on the 
 * system clipboard
 */
public String[] getAvailableTypeNames() {	
	int[] ppv = new int[1];
	int retrys = 0;
	while ((COM.OleGetClipboard(ppv) != COM.S_OK) && retrys < MAX_RETRIES) {
		retrys++;
	}
	if (retrys == MAX_RETRIES) return null;
	
	IDataObject dataObject = new IDataObject(ppv[0]);
	
	int[] ppFormatetc = new int[1];
	int rc = dataObject.EnumFormatEtc(COM.DATADIR_GET, ppFormatetc);
	dataObject.Release();
	if (rc != COM.S_OK)
		DND.error(SWT.ERROR_UNSPECIFIED);

	IEnumFORMATETC enum = new IEnumFORMATETC(ppFormatetc[0]);
	
	// Loop over enumerator and save any types that match what we are looking for
	int rgelt = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, FORMATETC.sizeof);
	int[] pceltFetched = new int[1];
	enum.Reset();
	String[] types = new String[0];
	while (enum.Next(1, rgelt, pceltFetched) == COM.S_OK && pceltFetched[0] == 1) {
		FORMATETC formatetc = new FORMATETC();
		COM.MoveMemory(formatetc, rgelt, FORMATETC.sizeof);
		int maxSize = 128;
		TCHAR buffer = new TCHAR(0, maxSize);
		int size = COM.GetClipboardFormatName(formatetc.cfFormat, buffer, maxSize);
		String type = null;
		if (size != 0) {
			type = buffer.toString(0, size);
		} else {
			switch (formatetc.cfFormat) {
				case COM.CF_HDROP:
					type = "CF_HDROP";
					break;
				case COM.CF_TEXT:
					type = "CF_TEXT";
					break;
				case COM.CF_BITMAP:
					type = "CF_BITMAP";
					break;
				case COM.CF_METAFILEPICT:
					type = "CF_METAFILEPICT";
					break;
				case COM.CF_SYLK:
					type = "CF_SYLK";
					break;
				case COM.CF_DIF:
					type = "CF_DIF";
					break;
				case COM.CF_TIFF:
					type = "CF_TIFF";
					break;
				case COM.CF_OEMTEXT:
					type = "CF_OEMTEXT";
					break;
				case COM.CF_DIB:
					type = "CF_DIB";
					break;
				case COM.CF_PALETTE:
					type = "CF_PALETTE";
					break;
				case COM.CF_PENDATA:
					type = "CF_PENDATA";
					break;
				case COM.CF_RIFF:
					type = "CF_RIFF";
					break;
				case COM.CF_WAVE:
					type = "CF_WAVE";
					break;
				case COM.CF_UNICODETEXT:
					type = "CF_UNICODETEXT";
					break;
				case COM.CF_ENHMETAFILE:
					type = "CF_ENHMETAFILE";
					break;
				case COM.CF_LOCALE:
					type = "CF_LOCALE";
					break;
				case COM.CF_MAX:
					type = "CF_MAX";
					break;
				default:
					continue;
			}
		}
		
		String[] newTypes = new String[types.length + 1];
		System.arraycopy(types, 0, newTypes, 0, types.length);
		newTypes[types.length] = type;
		types = newTypes;
	}
	OS.GlobalFree(rgelt);
	enum.Release();
	
	return types;

}

}
