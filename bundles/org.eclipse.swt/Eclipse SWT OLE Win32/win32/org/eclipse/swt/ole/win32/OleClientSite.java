package org.eclipse.swt.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import org.eclipse.swt.*;
import org.eclipse.swt.internal.Compatibility;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.win32.*;
/**
 * OleClientSite provides a site to manage an embedded OLE Document within a container.
 *
 * <p>The OleClientSite provides the following capabilities:
 * <ul>
 *  <li>creates the in-place editor for a blank document or opening an existing OLE Document
 * 	<li>lays the editor out
 *	<li>provides a mechanism for activating and deactivating the Document
 *	<li>provides a mechanism for saving changes made to the document
 * </ul>
 *
 * <p>This object implements the OLE Interfaces IUnknown, IOleClientSite, IAdviseSink, 
 * IOleInPlaceSite
 *
 * <p>Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 *	<dt><b>Styles</b> <dd>BORDER 
 *	<dt><b>Events</b> <dd>Dispose, Move, Resize
 * </dl>
 *
 */
public class OleClientSite extends Composite {
		
	// Interfaces for this Ole Client Container
	private COMObject  iUnknown;
	private COMObject  iOleClientSite;
	private COMObject  iAdviseSink;
	private COMObject  iOleInPlaceSite;

	protected GUID appClsid;
	private GUID objClsid;
	private int  refCount;
	
	// References to the associated Frame.
	protected OleFrame frame;
	
	// Access to the embedded/linked Ole Object 
	protected IUnknown          objIUnknown;
	protected IOleObject        objIOleObject;
	protected IViewObject2      objIViewObject2;
	protected IOleInPlaceObject objIOleInPlaceObject;
	protected IOleCommandTarget objIOleCommandTarget;
		   
	// Related storage information
	protected IStorage tempStorage;     // IStorage interface of the receiver
	
	// Internal state and style information
	private int     aspect;    // the display aspect of the embedded object, e.g., DvaspectContent or DvaspectIcon
	private int     type;      // Indicates the type of client that can be supported inside this container
	private boolean isStatic;  // Indicates item's display is static, i.e., a bitmap, metafile, etc.

	private RECT borderWidths = new RECT();
	private RECT indent = new RECT();
	private boolean inUpdate = false;
	private boolean inInit = true;
	private boolean inDispose = false;
		
	private static final String WORDPROGID = "Word.Document";

	private Listener listener;
	
	static final int STATE_NONE = 0;
	static final int STATE_RUNNING = 1;
	static final int STATE_INPLACEACTIVE = 2;
	static final int STATE_UIACTIVE = 3;
	static final int STATE_ACTIVE = 4;
	int state = STATE_NONE;
	
protected OleClientSite(Composite parent, int style) {
	/*
	 * NOTE: this constructor should never be used by itself because it does
	 * not create an Ole Object
	 */
	super(parent, style);
	
	createCOMInterfaces();
	
	// install the Ole Frame for this Client Site
	while (parent != null) {
		if (parent instanceof OleFrame){
			frame = (OleFrame)parent;
			break;
		}
		parent = parent.getParent();
	}
	if (frame == null) OLE.error(SWT.ERROR_INVALID_ARGUMENT);
	frame.AddRef();
	
	aspect   = COM.DVASPECT_CONTENT;
	type     = COM.OLEEMBEDDED;
	isStatic = false;

	listener = new Listener() {
		public void handleEvent(Event e) {
			switch (e.type) {
			case SWT.Resize :
			case SWT.Move :    onResize(e); break;
			case SWT.Dispose : onDispose(e); break;
			case SWT.FocusIn:  onFocusIn(e); break;
			case SWT.FocusOut:  onFocusOut(e); break;
			case SWT.Paint:    onPaint(e); break;
			case SWT.Traverse: onTraverse(e); break;
			case SWT.KeyDown: /* required for traversal */ break;
			default :
				OLE.error(SWT.ERROR_NOT_IMPLEMENTED);
			}
		}
	};

	frame.addListener(SWT.Resize, listener);
	frame.addListener(SWT.Move, listener);
	addListener(SWT.Dispose, listener);
	addListener(SWT.FocusIn, listener);
	addListener(SWT.FocusOut, listener);
	addListener(SWT.Paint, listener);
	addListener(SWT.Traverse, listener);
	addListener(SWT.KeyDown, listener);
}
/**
 * Create an OleClientSite child widget using the OLE Document type associated with the
 * specified file.  The OLE Document type is determined either through header information in the file 
 * or through a Registry entry for the file extension. Use style bits to select a particular look 
 * or set of properties.
 *
 * @param parent a composite widget; must be an OleFrame
 * @param style the bitwise OR'ing of widget styles
 * @param file the file that is to be opened in this OLE Document
 *
 * @exception SWTError
 * <ul><li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *     <li>ERROR_ERROR_NULL_ARGUMENT when the parent is null</ul>
 * @exception SWTError
 * <ul><li>ERROR_CANNOT_CREATE_OBJECT when failed to create OLE Object
 *     <li>ERROR_INVALID_ARGUMENT when the parent is not an OleFrame
 *     <li>ERROR_CANNOT_OPEN_FILE when failed to open file
 *     <li>ERROR_INTERFACES_NOT_INITIALIZED when unable to create callbacks for OLE Interfaces</ul>
 *
 */
public OleClientSite(Composite parent, int style, File file) {
	this(parent, style);
	try {

		if (file == null || file.isDirectory() || !file.exists())
			OLE.error(OLE.ERROR_INVALID_ARGUMENT);
			
		// Is there an associated CLSID?
		appClsid = new GUID();
		char[] fileName = (file.getAbsolutePath()+"\0").toCharArray();
		int result = COM.GetClassFile(fileName, appClsid);
		if (result != COM.S_OK)
			OLE.error(OLE.ERROR_INVALID_CLASSID, result);
		// associated CLSID may not be installed on this machine
		if (getProgramID() == null)
			OLE.error(OLE.ERROR_INVALID_CLASSID, result);
			
		// Open a temporary storage object
		tempStorage = createTempStorage();

		// Create ole object with storage object
		int[] address = new int[1];
		result = COM.OleCreateFromFile(appClsid, fileName, COM.IIDIUnknown, COM.OLERENDER_DRAW, null, 0, tempStorage.getAddress(), address);
		if (result != COM.S_OK)
			OLE.error(OLE.ERROR_CANNOT_CREATE_OBJECT, result);

		objIUnknown = new IUnknown(address[0]);
		
		// Init sinks
		addObjectReferences();
		
		if (COM.OleRun(objIUnknown.getAddress()) == OLE.S_OK) state = STATE_RUNNING;
	} catch (SWTException e) {
		dispose();
		disposeCOMInterfaces();
		throw e;
	}
}
/**
 * Create an OleClientSite child widget to edit a blank document using the specified OLE Document
 * application.  Use style bits to select a particular look or set of properties.
 *
 * @param parent a composite widget; must be an OleFrame
 * @param style the bitwise OR'ing of widget styles
 * @param progID the unique program identifier of am OLE Document application; 
 *               the value of the ProgID key or the value of the VersionIndependentProgID key specified
 *               in the registry for the desired OLE Document (for example, the VersionIndependentProgID
 *               for Word is Word.Document)
 *
 * @exception SWTError
 * <ul><li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *     <li>ERROR_ERROR_NULL_ARGUMENT when the parent is null
 *     <li>ERROR_INVALID_CLASSID when the progId does not map to a registered CLSID
 *     <li>ERROR_INVALID_ARGUMENT when the parent is not an OleFrame
 *     <li>ERROR_CANNOT_CREATE_OBJECT when failed to create OLE Object
 *     <li>ERROR_INTERFACES_NOT_INITIALIZED when unable to create callbacks for OLE Interfaces</ul>
 *
 */
public OleClientSite(Composite parent, int style, String progId) {
	this(parent, style);
	try {
		appClsid = getClassID(progId);
		if (appClsid == null)
			OLE.error(OLE.ERROR_INVALID_CLASSID);
			
		// Open a temporary storage object
		tempStorage = createTempStorage();
	
		// Create ole object with storage object
		int[] address = new int[1];
		int result = COM.OleCreate(appClsid, COM.IIDIUnknown, COM.OLERENDER_DRAW, null, 0, tempStorage.getAddress(), address);
		if (result != COM.S_OK)
			OLE.error(OLE.ERROR_CANNOT_CREATE_OBJECT, result);

		objIUnknown = new IUnknown(address[0]);

		// Init sinks
		addObjectReferences();

		if (COM.OleRun(objIUnknown.getAddress()) == OLE.S_OK) state = STATE_RUNNING;
		
	} catch (SWTException e) {
		dispose();
		disposeCOMInterfaces();
		throw e;
	}
}
/**
 * @private
 *
 * Create an OleClientSite child widget to edit the specified file using the specified OLE Document
 * application.  Use style bits to select a particular look or set of properties. 
 *
 * @param parent a composite widget; must be an OleFrame
 * @param style the bitwise OR'ing of widget styles
 * @param progID the unique program identifier of am OLE Document application; 
 *               the value of the ProgID key or the value of the VersionIndependentProgID key specified
 *               in the registry for the desired OLE Document (for example, the VersionIndependentProgID
 *               for Word is Word.Document)
 * @param file the file that is to be opened in this OLE Document
 *
 * @exception SWTError
 * <ul><li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *     <li>ERROR_ERROR_NULL_ARGUMENT when the parent is null
 *     <li>ERROR_INVALID_CLASSID when the progId does not map to a registered CLSID
 *     <li>ERROR_CANNOT_CREATE_OBJECT when failed to create OLE Object
 *     <li>ERROR_CANNOT_OPEN_FILE when failed to open file
 *     <li>ERROR_INVALID_ARGUMENT when the parent is not an OleFrame
 *     <li>ERROR_INTERFACES_NOT_INITIALIZED when unable to create callbacks for OLE Interfaces</ul>
 *
 */
public OleClientSite(Composite parent, int style, String progId, File file) {
	this(parent, style);
	
	try {

		if (file == null || file.isDirectory() || !file.exists())
			OLE.error(OLE.ERROR_INVALID_ARGUMENT);
			
		appClsid = getClassID(progId);
		
		// Are we opening this file with the preferred OLE object?
		char[] fileName = (file.getAbsolutePath()+"\0").toCharArray();
		GUID fileClsid = new GUID();
		COM.GetClassFile(fileName, fileClsid);
	
		if (COM.IsEqualGUID(appClsid, fileClsid)){
			// use default mechanism
			// Open a temporary storage object
			tempStorage = createTempStorage();

			// Create ole object with storage object
			int[] address = new int[1];
			int result = COM.OleCreateFromFile(appClsid, fileName, COM.IIDIUnknown, COM.OLERENDER_DRAW, null, 0, tempStorage.getAddress(), address);
			if (result != COM.S_OK)
				OLE.error(OLE.ERROR_CANNOT_CREATE_OBJECT, result);

			objIUnknown = new IUnknown(address[0]);
		} else {
			// use a conversion mechanism
			
			// Word does not follow the standard and does not use "CONTENTS" as the name of
			// its primary stream
			String contentStream = "CONTENTS";
			GUID wordGUID = getClassID(WORDPROGID);
			if (COM.IsEqualGUID(appClsid, wordGUID)) contentStream = "WordDocument";

			// Copy over the contents of the file into a new temporary storage object
			OleFile oleFile = new OleFile(file, contentStream, OleFile.READ);
			IStorage storage = oleFile.getRootStorage();
			storage.AddRef();
			// Open a temporary storage object
			tempStorage = createTempStorage();
			// Copy over contents of file
			int result = storage.CopyTo(0, null, null, tempStorage.getAddress());
			storage.Release();
			if (result != COM.S_OK)
				OLE.error(OLE.ERROR_CANNOT_OPEN_FILE, result);
			oleFile.dispose();

			// create ole client
			int[] ppv = new int[1];
			result = COM.CoCreateInstance(appClsid, 0, COM.CLSCTX_INPROC_HANDLER | COM.CLSCTX_INPROC_SERVER, COM.IIDIUnknown, ppv);
			if (result != COM.S_OK){		
				tempStorage.Release();
				OLE.error(OLE.ERROR_CANNOT_CREATE_OBJECT, result);
			}
			objIUnknown = new IUnknown(ppv[0]);

			// get the persistant storage of the ole client
			ppv = new int[1];
			result = objIUnknown.QueryInterface(COM.IIDIPersistStorage, ppv);
			if (result != COM.S_OK){
				tempStorage.Release();
				objIUnknown.Release();
				OLE.error(OLE.ERROR_CANNOT_CREATE_OBJECT, result);
			}
			IPersistStorage iPersistStorage = new IPersistStorage(ppv[0]);

			// load the contents of the file into the ole client site
			result = iPersistStorage.Load(tempStorage.getAddress());
			iPersistStorage.Release();
			if (result != COM.S_OK){
				tempStorage.Release();
				tempStorage = null;
				objIUnknown.Release();
				objIUnknown = null;
				OLE.error(OLE.ERROR_CANNOT_CREATE_OBJECT, result);
			}
		}
		
		// Init sinks
		addObjectReferences();
		
		if (COM.OleRun(objIUnknown.getAddress()) == OLE.S_OK) state = STATE_RUNNING;
	} catch (SWTException e) {
		dispose();
		disposeCOMInterfaces();
		throw e;
	}
}
protected void addObjectReferences() {
	//
	int[] ppvObject = new int[1];
	if (objIUnknown.QueryInterface(COM.IIDIPersist, ppvObject) == COM.S_OK) {
		IPersist objIPersist = new IPersist(ppvObject[0]);
		GUID tempid = new GUID();
		if (objIPersist.GetClassID(tempid) == COM.S_OK)
			objClsid = tempid;
		objIPersist.Release();
	}
	
	//
	ppvObject = new int[1];
	int result = objIUnknown.QueryInterface(COM.IIDIViewObject2, ppvObject);
	if (result != COM.S_OK)
		OLE.error(OLE.ERROR_INTERFACE_NOT_FOUND, result);
	objIViewObject2 = new IViewObject2(ppvObject[0]);
	objIViewObject2.SetAdvise(aspect, 0, iAdviseSink.getAddress());

	//
	ppvObject = new int[1];
	result = objIUnknown.QueryInterface(COM.IIDIOleObject, ppvObject);
	if (result != COM.S_OK)
		OLE.error(OLE.ERROR_INTERFACE_NOT_FOUND, result);
	objIOleObject = new IOleObject(ppvObject[0]);
	objIOleObject.SetClientSite(iOleClientSite.getAddress());
	int[] pdwConnection = new int[1];
	objIOleObject.Advise(iAdviseSink.getAddress(), pdwConnection);
	objIOleObject.SetHostNames("main", "main");

	// Notify the control object that it is embedded in an OLE container
	COM.OleSetContainedObject(objIUnknown.getAddress(), true);

	// Is OLE object linked or embedded?
	ppvObject = new int[1];
	if (objIUnknown.QueryInterface(COM.IIDIOleLink, ppvObject) == COM.S_OK) {
		IOleLink objIOleLink = new IOleLink(ppvObject[0]);
		int[] ppmk = new int[1];
		if (objIOleLink.GetSourceMoniker(ppmk) == COM.S_OK) {
			IMoniker objIMoniker = new IMoniker(ppmk[0]);
			objIMoniker.Release();
			type = COM.OLELINKED;
			objIOleLink.BindIfRunning();
		} else {
			isStatic = true;
		}
		objIOleLink.Release();
	}
}
protected int AddRef() {
	refCount++;
	return refCount;
}
private int CanInPlaceActivate() {
	if (aspect == COM.DVASPECT_CONTENT && type == COM.OLEEMBEDDED)
		return COM.S_OK;
		
	return COM.S_FALSE;
}
private int ContextSensitiveHelp(int fEnterMode) {
	return COM.S_OK;
}
protected void createCOMInterfaces() {
	
	iUnknown = new COMObject(new int[]{2, 0, 0}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
	};
	
	iOleClientSite = new COMObject(new int[]{2, 0, 0, 0, 3, 1, 0, 1, 0}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return SaveObject();}
		// method4 GetMoniker - not implemented
		public int method5(int[] args) {return GetContainer(args[0]);}
		public int method6(int[] args) {return ShowObject();}
		public int method7(int[] args) {return OnShowWindow(args[0]);}
		// method8 RequestNewObjectLayout - not implemented
	};
	
	iAdviseSink = new COMObject(new int[]{2, 0, 0, 2, 2, 1, 0, 0}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return OnDataChange(args[0], args[1]);}
		public int method4(int[] args) {return OnViewChange(args[0], args[1]);}
		//method5 OnRename - not implemented
		public int method6(int[] args) {OnSave();return 0;}
		public int method7(int[] args) {return OnClose();}	
	};
	
	iOleInPlaceSite = new COMObject(new int[]{2, 0, 0, 1, 1, 0, 0, 0, 5, 1, 1, 0, 0, 0, 1}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return GetWindow(args[0]);}
		public int method4(int[] args) {return ContextSensitiveHelp(args[0]);}
		public int method5(int[] args) {return CanInPlaceActivate();}
		public int method6(int[] args) {return OnInPlaceActivate();}
		public int method7(int[] args) {return OnUIActivate();}
		public int method8(int[] args) {return GetWindowContext(args[0], args[1], args[2], args[3], args[4]);}
		public int method9(int[] args) {return Scroll(args[0]);}
		public int method10(int[] args) {return OnUIDeactivate(args[0]);}
		public int method11(int[] args) {return OnInPlaceDeactivate();}
		// method12 DiscardUndoState - not implemented
		// method13 DeactivateAndUndoChange - not implemented
		public int method14(int[] args) {return OnPosRectChange(args[0]);}
	};	
}
protected IStorage createTempStorage() {
	int[] tempStorage = new int[1];
	int grfMode = COM.STGM_READWRITE | COM.STGM_SHARE_EXCLUSIVE | COM.STGM_DELETEONRELEASE;
	int result = COM.StgCreateDocfile(null, grfMode, 0, tempStorage);
	if (result != COM.S_OK)
		OLE.error(OLE.ERROR_CANNOT_CREATE_FILE, result);
	return new IStorage(tempStorage[0]);
}
/**
 * Deactivates an active in-place object and discards the object's undo state.
 */
public void deactivateInPlaceClient() {
	if (objIOleInPlaceObject != null) {
		objIOleInPlaceObject.InPlaceDeactivate();
	}
}
private void deleteTempStorage() {
	//Destroy this item's contents in the temp root IStorage.
	if (tempStorage != null){
		tempStorage.Release();
	}
	tempStorage = null;
}
protected void disposeCOMInterfaces() {
	if (iUnknown != null)
		iUnknown.dispose();
	iUnknown = null;
	
	if (iOleClientSite != null)
	iOleClientSite.dispose();
	iOleClientSite = null;
	
	if (iAdviseSink != null)
		iAdviseSink.dispose();
	iAdviseSink = null;
	
	if (iOleInPlaceSite != null)
		iOleInPlaceSite.dispose();
	iOleInPlaceSite = null;
}
/**
 * Requests that the OLE Document or ActiveX Control perform an action; actions are almost always
 * changes to the activation state.
 *
 * @param verb the operation that is requested.  This is one of the OLE.OLEIVERB_ values
 *
 * @return an HRESULT value indicating the success of the operation request; OLE.S_OK indicates
 *         success
 */
public int doVerb(int verb) {
	// Not all OLE clients (for example PowerPoint) can be set into the running state in the constructor.
	// The fix is to ensure that the client is in the running state before invoking any verb on it.
	if (state == STATE_NONE) {
		if (COM.OleRun(objIUnknown.getAddress()) == OLE.S_OK) state = STATE_RUNNING;
	}	
	if (state == STATE_NONE || isStatic)
		return COM.E_FAIL;
	
	// See PR: 1FV9RZW
	int result = objIOleObject.DoVerb(verb, null, iOleClientSite.getAddress(), 0, handle, null);

	if (state != STATE_RUNNING && inInit) {
		updateStorage();
		inInit = false;
	}
	return result;
}
/**
 * Asks the OLE Document or ActiveX Control to execute a command from a standard 
 * list of commands. The OLE Document or ActiveX Control must support the IOleCommandTarget
 * interface.  The OLE Document or ActiveX Control does not have to support all the commands
 * in the standard list.  To check if a command is supported, you can call queryStatus with
 * the cmdID.
 *
 * @param cmdID the ID of a command; these are the OLE.OLECMDID_ values - a small set of common
 *              commands
 * @param options the optional flags; these are the OLE.OLECMDEXECOPT_ values
 * @param in the argument for the command
 * @param out the return value of the command
 *
 * @return an HRESULT value; OLE.S_OK is returned if successful
 *
 */
public int exec(int cmdID, int options, Variant in, Variant out) {
	
	if (objIOleCommandTarget == null) {
		int[] address = new int[1];
		if (objIUnknown.QueryInterface(COM.IIDIOleCommandTarget, address) != COM.S_OK)
			return OLE.ERROR_INTERFACE_NOT_FOUND;
		objIOleCommandTarget = new IOleCommandTarget(address[0]);
	}
	
	int inAddress = 0;
	if (in != null){
		inAddress = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, Variant.sizeof);
		in.getData(inAddress);
	}
	int outAddress = 0;
	if (out != null){
		outAddress = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, Variant.sizeof);
		out.getData(outAddress);
	}
		
	int result = objIOleCommandTarget.Exec(null, cmdID, options, inAddress, outAddress);
	
	if (inAddress != 0){
		COM.VariantClear(inAddress);
		OS.GlobalFree(inAddress);
	}
	if (outAddress != 0) {
		out.setData(outAddress);
		COM.VariantClear(outAddress);
		OS.GlobalFree(outAddress);
	}
		
	return result;
}
IDispatch getAutomationObject() {
	int[] ppvObject = new int[1];
	if (objIUnknown.QueryInterface(COM.IIDIDispatch, ppvObject) != COM.S_OK)
		return null;
	return new IDispatch(ppvObject[0]);
}
protected GUID getClassID(String clientName) {
	// create a GUID struct to hold the result
	GUID guid = new GUID();

	// create a null terminated array of char
	char[] buffer = null;
	if (clientName != null) {
		int count = clientName.length();
		buffer = new char[count + 1];
		clientName.getChars(0, count, buffer, 0);
	}
	if (COM.CLSIDFromProgID(buffer, guid) != COM.S_OK){
		int result = COM.CLSIDFromString(buffer, guid);
		if (result != COM.S_OK)
			OLE.error(OLE.ERROR_INVALID_CLASSID, result);
	}
	return guid;
}
private int GetContainer(int ppContainer) {
	/* Simple containers that do not support links to their embedded 
	 * objects probably do not need to implement this method. Instead, 
	 * they can return E_NOINTERFACE and set ppContainer to NULL.
	 */
	if (ppContainer != 0)
		COM.MoveMemory(ppContainer, new int[]{0}, 4);
	return COM.E_NOINTERFACE;
}
private SIZE getExtent() {
	SIZE sizel = new SIZE();
	// get the current size of the embedded OLENatives object
	if (objIOleObject != null) {
		if ( objIViewObject2 != null && !COM.OleIsRunning(objIOleObject.getAddress())) {
			objIViewObject2.GetExtent(aspect, -1, null, sizel);
		} else {
			objIOleObject.GetExtent(aspect, sizel);
		}
	}
	return xFormHimetricToPixels(sizel);
}
public Rectangle getIndent() {
	return new Rectangle(indent.left, indent.right, indent.top, indent.bottom);
}
/**
 * Returns the program ID of the OLE Document or ActiveX Control.
 *
 * @return the program ID of the OLE Document or ActiveX Control
 */
public String getProgramID(){
	if (appClsid != null){
		int[] lplpszProgID = new int[1];
		if (COM.ProgIDFromCLSID(appClsid, lplpszProgID) == COM.S_OK) {
			int length = OS.GlobalSize(lplpszProgID[0]);
			int ptr = OS.GlobalLock(lplpszProgID[0]);
			char[] buffer = new char[length];
			COM.MoveMemory(buffer, lplpszProgID[0], length);
			OS.GlobalUnlock(ptr);
			OS.GlobalFree(lplpszProgID[0]);

			String result = new String(buffer);
			// remove null terminator
			int index = result.indexOf("\0");
			return result.substring(0, index);
		}
	}
	return null;
}
protected int GetWindow(int phwnd) {
	if (phwnd == 0)
		return COM.E_INVALIDARG;
	if (frame == null) {
		COM.MoveMemory(phwnd, new int[] {0}, 4);
		return COM.E_NOTIMPL;
	}
	
	// Copy the Window's handle into the memory passed in
	COM.MoveMemory(phwnd, new int[] {frame.handle}, 4);
	return COM.S_OK;
}
private int GetWindowContext(int ppFrame, int ppDoc, int lprcPosRect, int lprcClipRect, int lpFrameInfo) {	
	if (frame == null || ppFrame == 0)
		return COM.E_NOTIMPL;

	// fill in frame handle
	int iOleInPlaceFrame = frame.getIOleInPlaceFrame();
	COM.MoveMemory(ppFrame, new int[] {iOleInPlaceFrame}, 4);
	frame.AddRef();

	// null out document handle
	if (ppDoc != 0) {
		COM.MoveMemory(ppDoc, new int[] {0}, 4);
	}

	// fill in position and clipping info
	Rectangle clientArea = this.getClientArea();
	Point clientLocation = this.getLocation();
	setExtent(clientArea.width - indent.left - indent.right, clientArea.height - indent.top - indent.bottom);
	
	RECT posRect = new RECT();
	posRect.left   = clientLocation.x + indent.left;
	posRect.top    = clientLocation.y + indent.top;
	posRect.right  = clientLocation.x + clientArea.width - indent.right;
	posRect.bottom = clientLocation.y + clientArea.height - indent.bottom;

	RECT clipRect = new RECT();
	Rectangle frameArea  = frame.getClientArea();
	clipRect.left   = frameArea.x;
	clipRect.top    = frameArea.y;
	clipRect.right  = frameArea.x + frameArea.width;
	clipRect.bottom = frameArea.y + frameArea.height;
	
	if (lprcPosRect != 0) {
		OS.MoveMemory(lprcPosRect, posRect, RECT.sizeof);
	}
	if (lprcClipRect != 0) {
		OS.MoveMemory(lprcClipRect, clipRect, RECT.sizeof);
	}

	// get frame info
	OLEINPLACEFRAMEINFO frameInfo = new OLEINPLACEFRAMEINFO();
	frameInfo.cb = OLEINPLACEFRAMEINFO.sizeof;
	frameInfo.fMDIApp = 0;
	frameInfo.hwndFrame = frame.handle;
	Shell shell = getShell();
	Menu menubar = shell.getMenuBar();
	if (menubar != null && !menubar.isDisposed()) {
		int hwnd = shell.handle;
		int cAccel = OS.SendMessage(hwnd, OS.WM_APP, 0, 0);
		if (cAccel != 0) {
			int hAccel = OS.SendMessage(hwnd, OS.WM_APP+1, 0, 0);
			if (hAccel != 0) {
				frameInfo.cAccelEntries = cAccel;
				frameInfo.haccel = hAccel;
			}
		}
	}
	COM.MoveMemory(lpFrameInfo, frameInfo, OLEINPLACEFRAMEINFO.sizeof);
	
	return COM.S_OK;
}
public boolean isDirty() {
	/*
	 *  Note: this method must return true unless it is absolutely clear that the
	 * contents of the Ole Document do not differ from the contents in the file
	 * on the file system.
	 */
	
	// Get access to the persistant storage mechanism
	int[] address = new int[1];
	if (objIOleObject.QueryInterface(COM.IIDIPersistFile, address) != COM.S_OK)
		return true;
	IPersistStorage permStorage = new IPersistStorage(address[0]);
	// Are the contents of the permanent storage different from the file?
	int result = permStorage.IsDirty();
	permStorage.Release();
	if (result == COM.S_FALSE) return false;
	return true;
}
public boolean isFocusControl () {
	checkWidget ();
	int focusHwnd = OS.GetFocus();
	if (objIOleInPlaceObject == null) return (handle == focusHwnd); 
	int[] phwnd = new int[1];
	objIOleInPlaceObject.GetWindow(phwnd);
	while (focusHwnd != 0) {
		if (phwnd[0] == focusHwnd) return true;
		focusHwnd = OS.GetParent(focusHwnd);
	}
	return false;
}
private int OnClose() {
	return COM.S_OK;
}
private int OnDataChange(int pFormatetc, int pStgmed) {
	return COM.S_OK;
}
private void onDispose(Event e) {
	inDispose = true;
	if (state != STATE_NONE)
		doVerb(OLE.OLEIVERB_DISCARDUNDOSTATE);
	deactivateInPlaceClient();
	releaseObjectInterfaces(); // Note, must release object interfaces before releasing frame
	deleteTempStorage();
	
	// remove listeners
	removeListener(SWT.Dispose, listener);
	removeListener(SWT.FocusIn, listener);
	removeListener(SWT.Paint, listener);
	removeListener(SWT.Traverse, listener);
	removeListener(SWT.KeyDown, listener);
	frame.removeListener(SWT.Resize, listener);
	frame.removeListener(SWT.Move, listener);
	
	frame.Release();
	frame = null;
}
void onFocusIn(Event e) {
	if (inDispose) return;
	if (state != STATE_UIACTIVE) doVerb(OLE.OLEIVERB_SHOW);
	if (objIOleInPlaceObject == null) return;
	if (isFocusControl()) return;
	int[] phwnd = new int[1];
	objIOleInPlaceObject.GetWindow(phwnd);
	if (phwnd[0] == 0) return;
	OS.SetFocus(phwnd[0]);
}
void onFocusOut(Event e) {
}
private int OnInPlaceActivate() {
	state = STATE_INPLACEACTIVE;
	frame.setCurrentDocument(this);
	if (objIOleObject == null)
		return COM.S_OK;
	int[] ppvObject = new int[1];
	if (objIOleObject.QueryInterface(COM.IIDIOleInPlaceObject, ppvObject) == COM.S_OK) {
		objIOleInPlaceObject = new IOleInPlaceObject(ppvObject[0]);
	}
	return COM.S_OK;
}
private int OnInPlaceDeactivate() {
	if (objIOleInPlaceObject != null) objIOleInPlaceObject.Release();
	objIOleInPlaceObject = null;
	state = STATE_RUNNING;
	redraw();
	if (getDisplay().getFocusControl() == null) {
		getShell().traverse(SWT.TRAVERSE_TAB_NEXT);
	}
	return COM.S_OK;
}
private int OnPosRectChange(int lprcPosRect) {
	// Not resetting object rects because this causes Word to loose its scrollbars
	//setObjectRects();
	return COM.S_OK;
}
private void onPaint(Event e) {
	if (state == STATE_RUNNING || state == STATE_INPLACEACTIVE) {
		SIZE size = getExtent();
		Rectangle area = getClientArea();
		RECT rect = new RECT();
		if (getProgramID().startsWith("Excel.Sheet")) {
			rect.left = area.x; rect.right = area.x + (area.height * size.cx / size.cy);
			rect.top = area.y; rect.bottom = area.y + area.height;
		} else {
			rect.left = area.x; rect.right = area.x + size.cx;
			rect.top = area.y; rect.bottom = area.y + size.cy;
		}
		
		int pArea = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, RECT.sizeof);
		OS.MoveMemory(pArea, rect, RECT.sizeof);
		COM.OleDraw(objIUnknown.getAddress(), aspect, e.gc.handle, pArea);
		OS.GlobalFree(pArea);
	}
}
private void onResize(Event e) {
	Rectangle area = frame.getClientArea();
	setBounds(borderWidths.left, 
		      borderWidths.top, 
			  area.width - borderWidths.left - borderWidths.right, 
			  area.height - borderWidths.top - borderWidths.bottom);

	setObjectRects();
}
private void OnSave() {
}
private int OnShowWindow(int fShow) {
	return COM.S_OK;
}
private int OnUIActivate() {
	state = STATE_UIACTIVE;
	int[] phwnd = new int[1];
	if (objIOleInPlaceObject.GetWindow(phwnd) == COM.S_OK) {
		OS.SetWindowPos(phwnd[0], OS.HWND_TOP, 0, 0, 0, 0, OS.SWP_NOSIZE | OS.SWP_NOMOVE);
	}
	return COM.S_OK;
}
private int OnUIDeactivate(int fUndoable) {
	// currently, we are ignoring the fUndoable flag
	if (frame == null || frame.isDisposed()) return COM.S_OK;
	state = STATE_INPLACEACTIVE;
	frame.SetActiveObject(0,0);
	redraw();
	if (getDisplay().getFocusControl() == frame) {
		getShell().traverse(SWT.TRAVERSE_TAB_NEXT);
	}
	Shell shell = getShell();
	Menu menubar = shell.getMenuBar();
	if (menubar == null || menubar.isDisposed())
		return COM.S_OK;
		
	int shellHandle = shell.handle;
	OS.SetMenu(shellHandle, menubar.handle);
	return COM.OleSetMenuDescriptor(0, shellHandle, 0, 0, 0);
}
private void onTraverse(Event event) {
	switch (event.detail) {
		case SWT.TRAVERSE_ESCAPE:
		case SWT.TRAVERSE_RETURN:
		case SWT.TRAVERSE_TAB_NEXT:
		case SWT.TRAVERSE_TAB_PREVIOUS:
		case SWT.TRAVERSE_PAGE_NEXT:
		case SWT.TRAVERSE_PAGE_PREVIOUS:
		case SWT.TRAVERSE_MNEMONIC:
			event.doit = true;
			break;
	}
}
private int OnViewChange(int dwAspect, int lindex) {
	return COM.S_OK;
}
private IStorage openStorage(IStorage storage, String name) {
	int mode = COM.STGM_TRANSACTED | COM.STGM_READWRITE | COM.STGM_SHARE_EXCLUSIVE;
	int[] ppStg = new int[1];
	if (storage.OpenStorage(name, 0, mode, null, 0, ppStg) != COM.S_OK) {
		// IStorage does not exist, so create one
		mode = mode | COM.STGM_CREATE;
		if (storage.CreateStorage(name, mode, 0, 0, ppStg) != COM.S_OK)
			return null;
	}
	return new IStorage(ppStg[0]);
}
protected int QueryInterface(int riid, int ppvObject) {

	if (riid == 0 || ppvObject == 0)
		return COM.E_NOINTERFACE;
	GUID guid = new GUID();
	COM.MoveMemory(guid, riid, GUID.sizeof);

	if (COM.IsEqualGUID(guid, COM.IIDIUnknown)) {
		COM.MoveMemory(ppvObject, new int[] {iUnknown.getAddress()}, 4);
		AddRef();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID(guid, COM.IIDIAdviseSink)) {
		COM.MoveMemory(ppvObject, new int[] {iAdviseSink.getAddress()}, 4);
		AddRef();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID(guid, COM.IIDIOleClientSite)) {
		COM.MoveMemory(ppvObject, new int[] {iOleClientSite.getAddress()}, 4);
		AddRef();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID(guid, COM.IIDIOleInPlaceSite)) {
		COM.MoveMemory(ppvObject, new int[] {iOleInPlaceSite.getAddress()}, 4);
		AddRef();
		return COM.S_OK;
	}
	COM.MoveMemory(ppvObject, new int[] {0}, 4);
	return COM.E_NOINTERFACE;
}
/**
 * Returns the status of the specified command.  The status is any bitwise OR'd combination of 
 * SWTOLE.OLECMDF_SUPPORTED, SWTOLE.OLECMDF_ENABLED, SWTOLE.OLECMDF_LATCHED, SWTOLE.OLECMDF_NINCHED.
 * You can query the status of a command before invoking it with OleClientSite.exec.  The
 * OLE Document or ActiveX Control must support the IOleCommandTarget to make use of this method.
 *
 * @param cmd the ID of a command; these are the OLE.OLECMDID_ values - a small set of common
 *            commands
 *
 * @return the status of the specified command or 0 if unable to query the OLE Object; these are the
 *			  OLE.OLECMDF_ values
 */ 
public int queryStatus(int cmd) {
	
	if (objIOleCommandTarget == null) {
		int[] address = new int[1];
		if (objIUnknown.QueryInterface(COM.IIDIOleCommandTarget, address) != COM.S_OK)
			return 0;
		objIOleCommandTarget = new IOleCommandTarget(address[0]);
	}
	
	OLECMD olecmd = new OLECMD();
	olecmd.cmdID = cmd;
	
	int result = objIOleCommandTarget.QueryStatus(null, 1, olecmd, null);

	if (result != COM.S_OK) return 0;

	return olecmd.cmdf;
}
protected int Release() {
	refCount--;
	
	if (refCount == 0) {
		disposeCOMInterfaces();
	}
	return refCount;
}
protected void releaseObjectInterfaces() {

	if (objIOleInPlaceObject!= null)
		objIOleInPlaceObject.Release();
	objIOleInPlaceObject = null;
	
	if (objIOleObject != null) {	
		objIOleObject.Close(COM.OLECLOSE_NOSAVE);
		objIOleObject.Release();
	}
	objIOleObject = null;
	
	if (objIViewObject2 != null) {
		objIViewObject2.SetAdvise(aspect, 0, 0);
		objIViewObject2.Release();
	}
	objIViewObject2 = null;
	
	if (objIOleCommandTarget != null)
		objIOleCommandTarget.Release();
	objIOleCommandTarget = null;
		
	if (objIUnknown != null){
		objIUnknown.Release();
	}
	objIUnknown = null;
	
	COM.CoFreeUnusedLibraries();
}
public boolean save(File file, boolean includeOleInfo) {
	if (includeOleInfo)
		return saveToStorageFile(file);
	return saveToTraditionalFile(file);
}
private boolean saveFromContents(int address, File file) {

	boolean success = false;
	
	IStream tempContents = new IStream(address);
	tempContents.AddRef();

	try {
		FileOutputStream writer = new FileOutputStream(file);
		
		int increment = 1024 * 4;
		int pv = COM.CoTaskMemAlloc(increment);
		int[] pcbWritten = new int[1];
		while (tempContents.Read(pv, increment, pcbWritten) == COM.S_OK && pcbWritten[0] > 0) {
			byte[] buffer = new byte[ pcbWritten[0]];
			OS.MoveMemory(buffer, pv, pcbWritten[0]);
			writer.write(buffer); // Note: if file does not exist, this will create the file the 
			                      // first time it is called
			success = true;
		}
		COM.CoTaskMemFree(pv);
			
		writer.close();
		
	} catch (IOException err) {
	}

	tempContents.Release();

	return success;
}
private boolean saveFromOle10Native(int address, File file) {

	boolean success = false;
	
	IStream tempContents = new IStream(address);
	tempContents.AddRef();
	
	// The "\1Ole10Native" stream contains a DWORD header whose value is the length 
	// of the native data that follows.
	int pv = COM.CoTaskMemAlloc(4);
	int[] size = new int[1];
	int rc = tempContents.Read(pv, 4, null);
	OS.MoveMemory(size, pv, 4);
	COM.CoTaskMemFree(pv);
	if (rc == COM.S_OK && size[0] > 0) {
		
		// Read the data
		byte[] buffer = new byte[size[0]];
		pv = COM.CoTaskMemAlloc(size[0]);
		rc = tempContents.Read(pv, size[0], null);
		OS.MoveMemory(buffer, pv, size[0]);
		COM.CoTaskMemFree(pv);

		// open the file and write data into it
		try {
			FileOutputStream writer = new FileOutputStream(file);
			writer.write(buffer); // Note: if file does not exist, this will create the file
			writer.close();

			success = true;
		} catch (IOException err) {
		}
	}
	tempContents.Release();

	return success;
}
private int SaveObject() {

	updateStorage();
		
	return COM.S_OK;
}
/**
 * Saves the document to the specified file and includes OLE spcific inforrmation.  This method 
 * must <b>only</b> be used for files that have an OLE Storage format.  For example, a word file 
 * edited with Word.Document should be saved using this method because there is formating information
 * that should be stored in the OLE specific Storage format.
 *
 * @param file the file to which the changes are to be saved
 *
 * @return true if the save was successful
 */
private boolean saveToStorageFile(File file) {
	// Note: if the file already exists, some applications will not overwrite the file
	// In these cases, you should delete the file first (probably save the contents of the file in case the
	// save fails)
	if (file == null || file.isDirectory())
		return false;
		
	if (!updateStorage())
		return false;
	
	// get access to the persistant storage mechanism
	int[] address = new int[1];
	if (objIOleObject.QueryInterface(COM.IIDIPersistStorage, address) != COM.S_OK)
		return false;
	IPersistStorage permStorage = new IPersistStorage(address[0]);
	
	// The file will be saved using the formating of the current application - this
	// may not be the format of the application that was originally used to create the file
	// e.g. if an Excel file is opened in Word, the Word application will save the file in the 
	// Word format

	boolean success = false;
	OleFile oleFile = new OleFile(file, null, OleFile.WRITE);
	IStorage storage = oleFile.getRootStorage();
	storage.AddRef();
	if (COM.OleSave(permStorage.getAddress(), storage.getAddress(), false) == COM.S_OK) {
		if (storage.Commit(COM.STGC_DEFAULT) == COM.S_OK)
			success = true;
	}
	storage.Release();
	oleFile.dispose();
	permStorage.Release();
	
	return success;
}
/**
 * Saves the document to the specified file.  This method must be used for
 * files that do not have an OLE Storage format.  For example, a bitmap file edited with MSPaint 
 * should be saved using this method because bitmap is a standard format that does not include any
 * OLE specific data.
 *
 * @param file the file to which the changes are to be saved
 *
 * @return true if the save was successful
 */
private boolean saveToTraditionalFile(File file) {
	// Note: if the file already exists, some applications will not overwrite the file
	// In these cases, you should delete the file first (probably save the contents of the file in case the
	// save fails)
	if (file == null || file.isDirectory())
		return false;
	if (!updateStorage())
		return false;
	
	int[] address = new int[1];
	// Look for a CONTENTS stream
	if (tempStorage.OpenStream("CONTENTS", 0, COM.STGM_DIRECT | COM.STGM_READ | COM.STGM_SHARE_EXCLUSIVE, 0, address) == COM.S_OK)
		return saveFromContents(address[0], file);
		
	// Look for Ole 1.0 object stream
	if (tempStorage.OpenStream("\1Ole10Native", 0, COM.STGM_DIRECT | COM.STGM_READ | COM.STGM_SHARE_EXCLUSIVE, 0, address) == COM.S_OK)
		return saveFromOle10Native(address[0], file);
		
	return false;
}
private int Scroll(int scrollExtant) {
	return COM.S_OK;
}
void setBorderSpace(RECT newBorderwidth) {
	borderWidths = newBorderwidth;

	// readjust size and location of client site
	Rectangle area = frame.getClientArea();
	setBounds(borderWidths.left, borderWidths.top, 
				area.width - borderWidths.left - borderWidths.right, 
				area.height - borderWidths.top - borderWidths.bottom);

	setObjectRects();
}
private void setExtent(int width, int height){
	// Resize the width and height of the embedded/linked OLENatives object
	// to the specified values.

	if (objIOleObject == null || isStatic) return;

	if (inUpdate) return;
	
	SIZE currentExtent = getExtent();
	if (width == currentExtent.cx && height == currentExtent.cy) return;

	SIZE newExtent = new SIZE();
	newExtent.cx = width; newExtent.cy = height;
	newExtent = xFormPixelsToHimetric(newExtent);
	
   // Get the server running first, then do a SetExtent, then show it
	boolean alreadyRunning = COM.OleIsRunning(objIOleObject.getAddress());
	if (!alreadyRunning)
		COM.OleRun(objIOleObject.getAddress());
	
	if (objIOleObject.SetExtent(aspect, newExtent) == COM.S_OK){
		inUpdate = true;
		objIOleObject.Update();
		inUpdate = false;
		if (!alreadyRunning)
			// Close server if it wasn't already running upon entering this method.
			objIOleObject.Close(COM.OLECLOSE_SAVEIFDIRTY);
	}

}
public void setIndent(Rectangle newIndent) {
	indent = new RECT();
	indent.left = newIndent.x;
	indent.right = newIndent.width;
	indent.top = newIndent.y;
	indent.bottom = newIndent.height;
}
private void setObjectRects() {
	if (objIOleInPlaceObject == null) return;

	// size the object to fill the available space
	// leave a border
	Rectangle clientArea = this.getClientArea();
	Point clientLocation = this.getLocation();
	setExtent(clientArea.width - indent.left - indent.right, clientArea.height - indent.top - indent.bottom);
	
	RECT posRect = new RECT();
	posRect.left   = clientLocation.x + indent.left;
	posRect.top    = clientLocation.y + indent.top;
	posRect.right  = clientLocation.x + clientArea.width - indent.right;
	posRect.bottom = clientLocation.y + clientArea.height - indent.bottom;

	RECT clipRect = new RECT();
	Rectangle frameArea  = frame.getClientArea();
	clipRect.left   = frameArea.x;
	clipRect.top    = frameArea.y;
	clipRect.right  = frameArea.x + frameArea.width;
	clipRect.bottom = frameArea.y + frameArea.height;

	objIOleInPlaceObject.SetObjectRects(posRect, clipRect);	
}

private int ShowObject() {
	/* Tells the container to position the object so it is visible to 
	 * the user. This method ensures that the container itself is 
	 * visible and not minimized.
	 */
	return COM.S_OK;
}
/**
 * Displays a dialog with the property information for this OLE Object.  The OLE Document or
 * ActiveX Control must support the ISpecifyPropertyPages interface.
 *
 * @param title the name that will appear in the titlebar of the dialog
 */
public void showProperties(String title) {

	// Get the Property Page information from the OLE Object
	int[] ppvObject = new int[1];
	if (objIUnknown.QueryInterface(COM.IIDISpecifyPropertyPages, ppvObject) != COM.S_OK) return;
	ISpecifyPropertyPages objISPP = new ISpecifyPropertyPages(ppvObject[0]);
	CAUUID caGUID = new CAUUID();
	int result = objISPP.GetPages(caGUID);
	objISPP.Release();
	if (result != COM.S_OK) return;

	// create a frame in which to display the pages
	char[] chTitle = null;
	if (title != null) {
		chTitle = new char[title.length()];
		title.getChars(0, title.length(), chTitle, 0);
	}
	result = COM.OleCreatePropertyFrame(frame.handle, 10, 10, chTitle, 1, new int[] {objIUnknown.getAddress()}, caGUID.cElems, caGUID.pElems, COM.LOCALE_USER_DEFAULT, 0, 0);

	// free the property page information
	COM.CoTaskMemFree(caGUID.pElems);
}
private boolean updateStorage() {

	if (tempStorage == null) return false;

	int[] ppv = new int[1];
	if (objIUnknown.QueryInterface(COM.IIDIPersistStorage, ppv) != COM.S_OK) return false;
	IPersistStorage iPersistStorage = new IPersistStorage(ppv[0]);

	int result = COM.OleSave(iPersistStorage.getAddress(), tempStorage.getAddress(), true);

	if (result != COM.S_OK){
		// OleSave will fail for static objects, so do what OleSave does.
		COM.WriteClassStg(tempStorage.getAddress(), objClsid);
		result = iPersistStorage.Save(tempStorage.getAddress(), true);
	}
	
	tempStorage.Commit(COM.STGC_DEFAULT);
	result = iPersistStorage.SaveCompleted(0);
	iPersistStorage.Release();	
		
	return true;
}
private SIZE xFormHimetricToPixels(SIZE aSize) {
	// Return a new Size which is the pixel transformation of a 
	// size in HIMETRIC units.

	int hDC = OS.GetDC(0);
	int xppi = OS.GetDeviceCaps(hDC, 88); // logical pixels/inch in x
	int yppi = OS.GetDeviceCaps(hDC, 90); // logical pixels/inch in y
	OS.ReleaseDC(0, hDC);
	int cx = Compatibility.round(aSize.cx * xppi, 2540); // 2540 HIMETRIC units per inch
	int cy = Compatibility.round(aSize.cy * yppi, 2540);
	SIZE size = new SIZE();
	size.cx = cx;
	size.cy = cy;
	return size;
}
private SIZE xFormPixelsToHimetric(SIZE aSize) {
	// Return a new size which is the HIMETRIC transformation of a 
	// size in pixel units.

	int hDC = OS.GetDC(0);
	int xppi = OS.GetDeviceCaps(hDC, 88); // logical pixels/inch in x
	int yppi = OS.GetDeviceCaps(hDC, 90); // logical pixels/inch in y
	OS.ReleaseDC(0, hDC);
	int cx = Compatibility.round(aSize.cx * 2540, xppi); // 2540 HIMETRIC units per inch
	int cy = Compatibility.round(aSize.cy * 2540, yppi);
	SIZE size = new SIZE();
	size.cx = cx;
	size.cy = cy;
	return size;
}
}
