/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.internal.ole.win32.*;

/**
 * The <code>Clipboard</code> provides a mechanism for transferring data from one
 * application to another or within an application.
 * 
 * <p>IMPORTANT: This class is <em>not</em> intended to be subclassed.</p>
 */
public class Clipboard {

	private Display display;
	
	// ole interfaces
	private COMObject iDataObject;
	private int refCount;
	private Transfer[] transferAgents = new Transfer[0];
	private Object[] data = new Object[0];
	private int CFSTR_PREFERREDDROPEFFECT;

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
		DND.error(SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.display = display;
	TCHAR chFormatName = new TCHAR(0, "Preferred DropEffect", true); //$NON-NLS-1$
	CFSTR_PREFERREDDROPEFFECT = COM.RegisterClipboardFormat(chFormatName);
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
 * Throws an <code>SWTException</code> if the receiver can not
 * be accessed by the caller. This may include both checks on
 * the state of the receiver and more generally on the entire
 * execution context. This method <em>should</em> be called by
 * widget implementors to enforce the standard SWT invariants.
 * <p>
 * Currently, it is an error to invoke any method (other than
 * <code>isDisposed()</code>) on a widget that has had its 
 * <code>dispose()</code> method called. It is also an error
 * to call widget methods from any thread that is different
 * from the thread that created the widget.
 * </p><p>
 * In future releases of SWT, there may be more or fewer error
 * checks and exceptions may be thrown for different reasons.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
protected void checkWidget () {
	Display display = this.display;
	if (display == null) DND.error (SWT.ERROR_WIDGET_DISPOSED);
	if (display.getThread() != Thread.currentThread ()) DND.error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (display.isDisposed()) DND.error(SWT.ERROR_WIDGET_DISPOSED);
}

/**
 * Disposes of the operating system resources associated with the clipboard. 
 * The data will still be available on the system clipboard after the dispose 
 * method is called.  
 * 
 * <p>NOTE: On some platforms the data will not be available once the application
 * has exited or the display has been disposed.</p>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 */
public void dispose () {
	if (display.getThread() != Thread.currentThread()) DND.error(SWT.ERROR_THREAD_INVALID_ACCESS);
	if (display == null) return;
	this.Release();
	display = null;
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
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if transfer is null</li>
 * </ul>
 */
public Object getContents(Transfer transfer) {
	checkWidget();
	if (transfer == null) DND.error(SWT.ERROR_NULL_ARGUMENT);

	int[] ppv = new int[1];
	if (COM.OleGetClipboard(ppv) != COM.S_OK) return null;
	IDataObject dataObject = new IDataObject(ppv[0]);
	try {
		TransferData[] allowed = transfer.getSupportedTypes();
		for (int i = 0; i < allowed.length; i++) {
			if (dataObject.QueryGetData(allowed[i].formatetc) == COM.S_OK) {
				TransferData data = allowed[i];
				data.pIDataObject = ppv[0];
				return transfer.nativeToJava(data);
			}
		}		
	} finally {
		dataObject.Release();
	}
	return null; // No data available for this transfer
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
 *    <li>ERROR_INVALID_ARGUMENT - if data is null or datatypes is null 
 *          or the length of data is not the same as the length of dataTypes</li>
 *   </ul>
 *  @exception SWTError <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_CANNOT_SET_CLIPBOARD - if the clipboard is locked or otherwise unavailable</li>
  * </ul>
 */
public void setContents(Object[] data, Transfer[] dataTypes) {
	checkWidget();
	if (data == null || dataTypes == null || data.length != dataTypes.length) {
		DND.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	
	this.data = data;
	this.transferAgents = dataTypes;
	if (COM.OleSetClipboard(this.iDataObject.getAddress()) != COM.S_OK ) {
		DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
	}
	if (COM.OleIsCurrentClipboard(this.iDataObject.getAddress()) != COM.S_OK) {
		DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
	}
	
	int result = COM.OleFlushClipboard();
	/*
	* Bug in Windows. When a new application takes control
	* of the clipboard, other applications may open the 
	* clipboard to determine if they want to record the 
	* clipoard updates.  When this happens, the clipboard 
	* can not be flushed until the other aplication
	* is finished.
	* The fix is to call PeekMessage() with the flag 
	* PM_NOREMOVE to touch the event queue but not 
	* dispatch events.
	*/
	if (result != COM.S_OK) {
		MSG msg = new MSG();
		COM.PeekMessage (msg, 0, 0, 0, OS.PM_NOREMOVE);
		result = COM.OleFlushClipboard();
	}
	if (result != COM.S_OK) {
		DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
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
	FORMATETC[] formats = new FORMATETC[allowedDataTypes.length + 1];
	for (int i = 0; i < allowedDataTypes.length; i++){
		formats[i] = allowedDataTypes[i].formatetc;
	}
	// include the drop effect format to specify a copy operation
	FORMATETC dropeffect = new FORMATETC();
	dropeffect.cfFormat = CFSTR_PREFERREDDROPEFFECT;
	dropeffect.dwAspect = COM.DVASPECT_CONTENT;
	dropeffect.lindex = -1;
	dropeffect.tymed = COM.TYMED_HGLOBAL;
	formats[formats.length -1] = dropeffect;
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

	if (transferData.type == CFSTR_PREFERREDDROPEFFECT) {
		// specify that a copy operation is to be performed
		STGMEDIUM stgmedium = new STGMEDIUM();
		stgmedium.tymed = COM.TYMED_HGLOBAL;
		stgmedium.unionField = COM.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, 4);
		COM.MoveMemory(stgmedium.unionField, new int[] {COM.DROPEFFECT_COPY}, 4);
		stgmedium.pUnkForRelease = 0;
		COM.MoveMemory(pmedium, stgmedium, STGMEDIUM.sizeof);
		return COM.S_OK;
	}
		
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
	if (transferData.type == CFSTR_PREFERREDDROPEFFECT) return COM.S_OK;
	// is this type supported by the transfer agent?
	for (int i = 0; i < transferAgents.length; i++){
		if (transferAgents[i].isSupportedType(transferData))
			return COM.S_OK;
	}
	
	return COM.DV_E_FORMATETC;
}
private int QueryInterface(int riid, int ppvObject) {
	if (riid == 0 || ppvObject == 0) return COM.E_INVALIDARG;
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
 * Returns an array of the data types currently available on the system clipboard. Use
 * with Transfer.isSupportedType.
 *
 * @return array of TransferData
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Transfer#isSupportedType
 * 
 * @since 3.0
 */
public TransferData[] getAvailableTypes() {
	checkWidget();
	
	FORMATETC[] types = _getAvailableTypes();
	TransferData[] data = new TransferData[types.length];
	for (int i = 0; i < types.length; i++) {
		data[i] = new TransferData();
		data[i].type = types[i].cfFormat;
		data[i].formatetc = types[i];
	}
	return data;
}

/**
 * Returns a platform specific list of the data types currently available on the 
 * system clipboard.
 * 
 * <p>Note: <code>getAvailableTypeNames</code> is a utility for writing a Transfer 
 * sub-class.  It should NOT be used within an application because it provides 
 * platform specific information.</p>
 * 
 * @return a platform specific list of the data types currently available on the 
 * system clipboard
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String[] getAvailableTypeNames() {
	checkWidget();
	FORMATETC[] types = _getAvailableTypes();
	String[] names = new String[types.length];
	int maxSize = 128;
	for (int i = 0; i < types.length; i++){
		TCHAR buffer = new TCHAR(0, maxSize);
		int size = COM.GetClipboardFormatName(types[i].cfFormat, buffer, maxSize);
		if (size != 0) {
			names[i] = buffer.toString(0, size);
		} else {
			switch (types[i].cfFormat) {
				case COM.CF_HDROP: names[i] = "CF_HDROP"; break; //$NON-NLS-1$
				case COM.CF_TEXT: names[i] = "CF_TEXT"; break; //$NON-NLS-1$
				case COM.CF_BITMAP: names[i] = "CF_BITMAP"; break; //$NON-NLS-1$
				case COM.CF_METAFILEPICT: names[i] = "CF_METAFILEPICT"; break; //$NON-NLS-1$
				case COM.CF_SYLK: names[i] = "CF_SYLK"; break; //$NON-NLS-1$
				case COM.CF_DIF: names[i] = "CF_DIF"; break; //$NON-NLS-1$
				case COM.CF_TIFF: names[i] = "CF_TIFF"; break; //$NON-NLS-1$
				case COM.CF_OEMTEXT: names[i] = "CF_OEMTEXT"; break; //$NON-NLS-1$
				case COM.CF_DIB: names[i] = "CF_DIB"; break; //$NON-NLS-1$
				case COM.CF_PALETTE: names[i] = "CF_PALETTE"; break; //$NON-NLS-1$
				case COM.CF_PENDATA: names[i] = "CF_PENDATA"; break; //$NON-NLS-1$
				case COM.CF_RIFF: names[i] = "CF_RIFF"; break; //$NON-NLS-1$
				case COM.CF_WAVE: names[i] = "CF_WAVE"; break; //$NON-NLS-1$
				case COM.CF_UNICODETEXT: names[i] = "CF_UNICODETEXT"; break; //$NON-NLS-1$
				case COM.CF_ENHMETAFILE: names[i] = "CF_ENHMETAFILE"; break; //$NON-NLS-1$
				case COM.CF_LOCALE: names[i] = "CF_LOCALE"; break; //$NON-NLS-1$
				case COM.CF_MAX: names[i] = "CF_MAX"; break; //$NON-NLS-1$
				default: names[i] = "UNKNOWN";
			}
		}
	}
	return names;
}

private FORMATETC[] _getAvailableTypes() {
	FORMATETC[] types = new FORMATETC[0];
	int[] ppv = new int[1];
	if (COM.OleGetClipboard(ppv) != COM.S_OK) return types;
	IDataObject dataObject = new IDataObject(ppv[0]);
	int[] ppFormatetc = new int[1];
	int rc = dataObject.EnumFormatEtc(COM.DATADIR_GET, ppFormatetc);
	dataObject.Release();
	if (rc != COM.S_OK)return types;
	IEnumFORMATETC enum = new IEnumFORMATETC(ppFormatetc[0]);
	// Loop over enumerator and save any types that match what we are looking for
	int rgelt = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, FORMATETC.sizeof);
	int[] pceltFetched = new int[1];
	enum.Reset();
	while (enum.Next(1, rgelt, pceltFetched) == COM.S_OK && pceltFetched[0] == 1) {
		FORMATETC formatetc = new FORMATETC();
		COM.MoveMemory(formatetc, rgelt, FORMATETC.sizeof);
		FORMATETC[] newTypes = new FORMATETC[types.length + 1];
		System.arraycopy(types, 0, newTypes, 0, types.length);
		newTypes[types.length] = formatetc;
		types = newTypes;
	}
	OS.GlobalFree(rgelt);
	enum.Release();
	return types;
}
}
