package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.*;
import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.win32.*; 

/**
 *
 * Class <code>DragSource</code> defines the source object for a drag and drop transfer.
 *
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 *
 * <p>This class defines the following items:<ul>
 *   <li>the <code>Control</code> that the user clicks on to intiate a drag;
 *   <li>the data that will be transferred on a successful drop; 
 *   <li>and the modes (move, copy, link) of transfer that are allowed.
 * </ul></p>
 *
 * <p>You may have several DragSources in an application but you can only have one DragSource 
 * per Control.  Data dragged from this DragSource can be dropped on a site within this application 
 * but it can also be dropped on another application such as an external Text editor.</p>
 * 
 * <p>The application supplies the content of the data being transferred by implementing the interface
 * <code>DragSourceListener</code> which uses the class <code>DragSourceEvent</code>.  
 * The application is required to take the appropriate action to remove the data from the drag source
 * when a successful move operation occurs.</p>
 *
 * <code><pre>
 *	// Enable a label as a Drag Source
 *	Label label = new Label(shell, SWT.NONE);
 *	// This example will allow text to be dragged
 *	Transfer[] types = new Transfer[] {TextTransfer.getInstance()};
 *	// This example will allow the text to be copied or moved to the drop target
 *	int operations = DND.DROP_MOVE | DND.DROP_COPY;
 *	
 *	DragSource source = new DragSource (label, operations);
 *	source.setTransfer(types);
 *	source.addDragListener (new DragSourceListener() {
 *		public void dragStart(DragSourceEvent e) {
 *			// Only start the drag if there is actually text in the
 *			// label - this text will be what is dropped on the target.
 *			if (label.getText().length() == 0) {
 *				event.doit = false;
 *			}
 *		};
 *		public void dragSetData (DragSourceEvent event) {
 *			// A drop has been performed, so provide the data of the 
 *			// requested type.
 *			// (Checking the type of the requested data is only 
 *			// necessary if the drag source supports more than 
 *			// one data type but is shown here as an example).
 *			if (TextTransfer.getInstance().isSupportedType(event.dataType)){
 *				event.data = label.getText();
 *			}
 *		}
 *		public void dragFinished(DragSourceEvent event) {
 *			// A Move operation has been performed so remove the data
 *			// from the source
 *			if (event.detail == DND.DROP_MOVE)
 *				label.setText("");
 *		}
 *	});
 * </pre></code>
 *
 *
 * <dl>
 *	<dt><b>Styles</b> <dd>DND.DROP_NONE, DND.DROP_COPY, DND.DROP_MOVE, DND.DROP_LINK 
 *	<dt><b>Events</b> <dd>DND.DragEnd, DND.DragSetData
 * </dl>
 */

public class DragSource extends Widget {
	
	// ole interfaces
	private COMObject iUnknown;
	private COMObject iDropSource;
	private COMObject iDataObject;
	private int refCount;

	// info for registering as a drag source
	private Control control;
	private Transfer[] transferAgents = new Transfer[0];

	private Listener controlListener;
	
/**
 * Creates a new <code>DragSource</code> to handle dragging from the specified <code>Control</code>.
 * 
 * @param control the <code>Control</code> that the user clicks on to initiate the drag
 *
 * @param style the bitwise OR'ing of allowed operations; this may be a combination of any of 
 *					DND.DROP_NONE, DND.DROP_COPY, DND.DROP_MOVE, DND.DROP_LINK
 *
 */
public DragSource(Control control, int style) {

	super (control, checkStyle (style));
	
	this.control = control;
	
	createCOMInterfaces();

	this.AddRef();

	controlListener = new Listener () {
		public void handleEvent (Event event) {
			if (event.type == SWT.Dispose){
				DragSource.this.dispose();
			}
			if (event.type == SWT.DragDetect) {
				DragSource.this.drag();
			}
		}
	};
	control.addListener (SWT.Dispose, controlListener);
	control.addListener (SWT.DragDetect, controlListener);
	
	this.addListener(SWT.Dispose, new Listener() {
		public void handleEvent(Event e) {
			onDispose();
		}
	});
}
/**	 
 * Adds the listener to receive events.
 *
 * @param listener the listener
 *
 * @exception SWTError 
 *	<ul><li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 * 		<li>ERROR_WIDGET_DISPOSED  when the widget has been disposed</li>
 * 		<li>ERROR_NULL_ARGUMENT when listener is null</li></ul>
 */
public void addDragListener(DragSourceListener listener) {
	if (listener == null) DND.error (SWT.ERROR_NULL_ARGUMENT);
	DNDListener typedListener = new DNDListener (listener);
	addListener (DND.DragStart, typedListener);
	addListener (DND.DragEnd, typedListener);
	addListener (DND.DragSetData, typedListener);
	
}
private int AddRef() {
	refCount++;
	return refCount;
}
static int checkStyle (int style) {
	if (style == SWT.NONE) return DND.DROP_MOVE;
	return style;
}
private void createCOMInterfaces() {
	// register each of the interfaces that this object implements
	iUnknown    = new COMObject(new int[]{2, 0, 0}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
	};
	
	iDropSource = new COMObject(new int[]{2, 0, 0, 2, 1}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return QueryContinueDrag(args[0], args[1]);}
		public int method4(int[] args) {return GiveFeedback(args[0]);}
	};
	
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
private void onDispose () {
	this.Release();
	
	if (control != null && controlListener != null){
		control.removeListener(SWT.Dispose, controlListener);
		control.removeListener(SWT.DragDetect, controlListener);
	}
	controlListener = null;	
	control = null;
	
	transferAgents = null;
}

private void disposeCOMInterfaces() {
	
	if (iUnknown != null)
		iUnknown.dispose();
	iUnknown = null;
	
	if (iDropSource != null)
		iDropSource.dispose();
	iDropSource = null;

	if (iDataObject != null)
		iDataObject.dispose();
	iDataObject = null;
}
private void drag() {
	
	if (transferAgents == null || getStyle() == 0) return;
	
	DNDEvent event = new DNDEvent();
	event.widget = this;
	event.time = OS.GetMessageTime();
	event.doit = true;
	
	try {
		notifyListeners(DND.DragStart,event);
	} catch (Throwable e) {
		return;
	}
	
	if (!event.doit) return;
		
	int[] pdwEffect = new int[1];
	int result = COM.DoDragDrop(iDataObject.getAddress(), iDropSource.getAddress(), getStyle(), pdwEffect);

	event = new DNDEvent();
	event.widget = this;
	event.time = OS.GetMessageTime();
	event.detail = pdwEffect[0];
	event.doit = (result == COM.DRAGDROP_S_DROP);
	
	try {
		notifyListeners(DND.DragEnd,event);
	} catch (Throwable e) {
	}

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
/**
 * Returns the Control which is registered for this DragSource.  This is the control that the 
 * user clicks in to initiate dragging.
 *
 * @return the Control which is registered for this DragSource
 */
public Control getControl () {
	return control;
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
	
	DNDEvent event = new DNDEvent();
	event.widget = this;
	event.time = OS.GetMessageTime();
	event.dataType = transferData;
	try {
		notifyListeners(DND.DragSetData,event);
	} catch (Throwable e) {
		return COM.E_FAIL;
	}
	
	if (event.data == null) return COM.DV_E_FORMATETC;
	
	// get matching transfer agent to perform conversion
	Transfer transfer = null;
	for (int i = 0; i < transferAgents.length; i++){
		if (transferAgents[i].isSupportedType(event.dataType )){
			transfer = transferAgents[i];
			break;
		}
	}
	if (transfer == null) return COM.DV_E_FORMATETC;
	
	transfer.javaToNative(event.data, transferData);
	COM.MoveMemory(pmedium, transferData.stgmedium, STGMEDIUM.sizeof);
	return transferData.result;
}
public Display getDisplay () {

	if (control == null) DND.error(SWT.ERROR_WIDGET_DISPOSED);
	return control.getDisplay ();
}
/**
 * Returns the list of data types that can be transferred by this DragSource.
 *
 * @return the list of data types that can be transferred by this DragSource
 */ 
public Transfer[] getTransfer(){
	return transferAgents;
}
private int GiveFeedback(int dwEffect) {
	return COM.DRAGDROP_S_USEDEFAULTCURSORS;
}
private int QueryContinueDrag(int fEscapePressed, int grfKeyState) {
	if (fEscapePressed != 0)
		return COM.DRAGDROP_S_CANCEL;
	if ((grfKeyState & (OS.MK_LBUTTON | OS.MK_MBUTTON | OS.MK_RBUTTON)) == 0)
		return COM.DRAGDROP_S_DROP;

	return COM.S_OK;
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
	
	if (COM.IsEqualGUID(guid, COM.IIDIUnknown)) {
		COM.MoveMemory(ppvObject, new int[] {iUnknown.getAddress()}, 4);
		AddRef();
		return COM.S_OK;
	}
	
	if (COM.IsEqualGUID(guid, COM.IIDIDropSource)) {
		COM.MoveMemory(ppvObject, new int[] {iDropSource.getAddress()}, 4);
		AddRef();
		return COM.S_OK;
	}

	if (COM.IsEqualGUID(guid, COM.IIDIDataObject) ) {
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
 * Removes the listener.
 *
 * @param listener the listener
 *
 * @exception SWTError
 *	<ul><li>ERROR_THREAD_INVALID_ACCESS	when called from the wrong thread</li>
 * 		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * 		<li>ERROR_NULL_ARGUMENT when listener is null</li></ul>
 */
public void removeDragListener(DragSourceListener listener) {
	if (listener == null) DND.error (SWT.ERROR_NULL_ARGUMENT);
	removeListener (DND.DragStart, listener);
	removeListener (DND.DragEnd, listener);
	removeListener (DND.DragSetData, listener);
}
/**
 * Specifies the list of data types that can be transferred by this DragSource.
 * The application must be able to provide data to match each of these types when
 * a successful drop has occurred.
 */
public void setTransfer(Transfer[] transferAgents){
	this.transferAgents = transferAgents;
}
protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = DragSource.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}


}
