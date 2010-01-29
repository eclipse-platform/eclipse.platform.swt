/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.ole.win32;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import org.eclipse.swt.*;
import org.eclipse.swt.internal.C;
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
 * @see <a href="http://www.eclipse.org/swt/snippets/#ole">OLE and ActiveX snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Examples: OLEExample, OleWebBrowser</a>
 */
public class OleClientSite extends Composite {
		
	// Interfaces for this Ole Client Container
	private COMObject  iUnknown;
	COMObject  iOleClientSite;
	private COMObject  iAdviseSink;
	private COMObject  iOleInPlaceSite;
	private COMObject  iOleDocumentSite;

	protected GUID appClsid;
	private GUID objClsid;
	private int  refCount;
	
	// References to the associated Frame.
	protected OleFrame frame;
	
	// Access to the embedded/linked Ole Object 
	protected IUnknown                  objIUnknown;
	protected IOleObject                 objIOleObject;
	protected IViewObject2             objIViewObject2;
	protected IOleInPlaceObject     objIOleInPlaceObject;
	protected IOleCommandTarget objIOleCommandTarget;
	protected IOleDocumentView    objDocumentView;
		   
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
		
	private static final String WORDPROGID = "Word.Document"; //$NON-NLS-1$

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
 * @exception IllegalArgumentException
 * <ul><li>ERROR_NULL_ARGUMENT when the parent is null
 *     <li>ERROR_INVALID_ARGUMENT when the parent is not an OleFrame</ul> 
 * @exception SWTException
 * <ul><li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *     <li>ERROR_CANNOT_CREATE_OBJECT when failed to create OLE Object
 *     <li>ERROR_CANNOT_OPEN_FILE when failed to open file
 *     <li>ERROR_INTERFACE_NOT_FOUND when unable to create callbacks for OLE Interfaces
 *     <li>ERROR_INVALID_CLASSID
 * </ul>
 */
public OleClientSite(Composite parent, int style, File file) {
	this(parent, style);
	try {

		if (file == null || file.isDirectory() || !file.exists())
			OLE.error(OLE.ERROR_INVALID_ARGUMENT);
			
		// Is there an associated CLSID?
		GUID fileClsid = new GUID();
		char[] fileName = (file.getAbsolutePath()+"\0").toCharArray();
		int result = COM.GetClassFile(fileName, fileClsid);
		if (result != COM.S_OK)	OLE.error(OLE.ERROR_INVALID_CLASSID, result);
		// associated CLSID may not be installed on this machine
		String progID = getProgID(fileClsid); 
		if (progID == null)	OLE.error(OLE.ERROR_INVALID_CLASSID, result);
			
		appClsid = fileClsid;
		OleCreate(appClsid, fileClsid, fileName, file);
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
 * @param progId the unique program identifier of am OLE Document application; 
 *               the value of the ProgID key or the value of the VersionIndependentProgID key specified
 *               in the registry for the desired OLE Document (for example, the VersionIndependentProgID
 *               for Word is Word.Document)
 *
 * @exception IllegalArgumentException
 *<ul>
 *     <li>ERROR_NULL_ARGUMENT when the parent is null
 *     <li>ERROR_INVALID_ARGUMENT when the parent is not an OleFrame
 *</ul>
 * @exception SWTException
 * <ul><li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *     <li>ERROR_INVALID_CLASSID when the progId does not map to a registered CLSID
 *     <li>ERROR_CANNOT_CREATE_OBJECT when failed to create OLE Object
 * </ul>
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
		int /*long*/[] address = new int /*long*/[1];
		int result = COM.OleCreate(appClsid, COM.IIDIUnknown, COM.OLERENDER_DRAW, null, iOleClientSite.getAddress(), tempStorage.getAddress(), address);
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
 * Create an OleClientSite child widget to edit the specified file using the specified OLE Document
 * application.  Use style bits to select a particular look or set of properties. 
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>OleClientSite</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 * @param parent a composite widget; must be an OleFrame
 * @param style the bitwise OR'ing of widget styles
 * @param progId the unique program identifier of am OLE Document application; 
 *               the value of the ProgID key or the value of the VersionIndependentProgID key specified
 *               in the registry for the desired OLE Document (for example, the VersionIndependentProgID
 *               for Word is Word.Document)
 * @param file the file that is to be opened in this OLE Document
 *
 * @exception IllegalArgumentException
 * <ul><li>ERROR_NULL_ARGUMENT when the parent is null
 *     <li>ERROR_INVALID_ARGUMENT when the parent is not an OleFrame</ul>
 * @exception SWTException
 * <ul><li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread
 *     <li>ERROR_INVALID_CLASSID when the progId does not map to a registered CLSID
 *     <li>ERROR_CANNOT_CREATE_OBJECT when failed to create OLE Object
 *     <li>ERROR_CANNOT_OPEN_FILE when failed to open file
 * </ul>
 */
public OleClientSite(Composite parent, int style, String progId, File file) {
	this(parent, style);
	try {
		if (file == null || file.isDirectory() || !file.exists()) OLE.error(OLE.ERROR_INVALID_ARGUMENT);				
		appClsid = getClassID(progId);
		if (appClsid == null) OLE.error(OLE.ERROR_INVALID_CLASSID);
		
		// Are we opening this file with the preferred OLE object?
		char[] fileName = (file.getAbsolutePath()+"\0").toCharArray();
		GUID fileClsid = new GUID();
		COM.GetClassFile(fileName, fileClsid);
	
		OleCreate(appClsid, fileClsid, fileName, file);
	} catch (SWTException e) {
		dispose();
		disposeCOMInterfaces();
		throw e;
	}
}

void OleCreate(GUID appClsid, GUID fileClsid, char[] fileName, File file) {
	
	/* Bug in Windows. In some machines running Windows Vista and 
	 * Office 2007, OleCreateFromFile() fails to open files from 
	 * Office Word 97 - 2003 and in some other cases it fails to 
	 * save files due to a lock. The fix is to detect this case and 
	 * create the activeX using CoCreateInstance().
	 */
	boolean isOffice2007 = isOffice2007(true);
	if (!isOffice2007 && COM.IsEqualGUID(appClsid, fileClsid)){
		// Using the same application that created file, therefore, use default mechanism.
		tempStorage = createTempStorage();
		// Create ole object with storage object
		int /*long*/[] address = new int /*long*/[1];
		int result = COM.OleCreateFromFile(appClsid, fileName, COM.IIDIUnknown, COM.OLERENDER_DRAW, null, iOleClientSite.getAddress(), tempStorage.getAddress(), address);
		if (result != COM.S_OK) OLE.error(OLE.ERROR_CANNOT_CREATE_OBJECT, result);
		objIUnknown = new IUnknown(address[0]);
	} else {
		// Not using the same application that created file, therefore, copy from original file to a new storage file
		IStorage storage = null;
		if (COM.StgIsStorageFile(fileName) == COM.S_OK) {
			int /*long*/[] address = new int /*long*/[1];
			int mode = COM.STGM_READ | COM.STGM_TRANSACTED | COM.STGM_SHARE_EXCLUSIVE;
			int result = COM.StgOpenStorage(fileName, 0, mode, 0, 0, address); //Does an AddRef if successful
			if (result != COM.S_OK) OLE.error(OLE.ERROR_CANNOT_OPEN_FILE, result);
			storage = new IStorage(address[0]);
		} else {
			// Original file is not a Storage file so copy contents to a stream in a new storage file
			int /*long*/[] address = new int /*long*/[1];
			int mode = COM.STGM_READWRITE | COM.STGM_DIRECT | COM.STGM_SHARE_EXCLUSIVE | COM.STGM_CREATE;
			int result = COM.StgCreateDocfile(null, mode | COM.STGM_DELETEONRELEASE, 0, address); // Increments ref count if successful
			if (result != COM.S_OK) OLE.error(OLE.ERROR_CANNOT_OPEN_FILE, result);
			storage = new IStorage(address[0]);
			// Create a stream on the storage object.
			// Word does not follow the standard and does not use "CONTENTS" as the name of
			// its primary stream
			String streamName = "CONTENTS"; //$NON-NLS-1$
			GUID wordGUID = getClassID(WORDPROGID);
			if (wordGUID != null && COM.IsEqualGUID(appClsid, wordGUID)) streamName = "WordDocument"; //$NON-NLS-1$
			if (isOffice2007) streamName = "Package"; //$NON-NLS-1$
			address = new int /*long*/[1];
			result = storage.CreateStream(streamName, mode, 0, 0, address); // Increments ref count if successful
			if (result != COM.S_OK) {
				storage.Release();
				OLE.error(OLE.ERROR_CANNOT_OPEN_FILE, result);
			}
			IStream stream = new IStream(address[0]);
			try {
				// Copy over data in file to named stream
				FileInputStream fileInput = new FileInputStream(file);
				int increment = 1024*4;
				byte[] buffer = new byte[increment];
				int count = 0;
				while((count = fileInput.read(buffer)) > 0){
					int /*long*/ pv = COM.CoTaskMemAlloc(count);
					OS.MoveMemory(pv, buffer, count);
					result = stream.Write(pv, count, null) ;
					COM.CoTaskMemFree(pv);
					if (result != COM.S_OK) {
						fileInput.close();
						stream.Release();
						storage.Release();
						OLE.error(OLE.ERROR_CANNOT_OPEN_FILE, result);
					}
				}
				fileInput.close();
				stream.Commit(COM.STGC_DEFAULT);
				stream.Release();
			} catch (IOException err) {
				stream.Release();
				storage.Release();
				OLE.error(OLE.ERROR_CANNOT_OPEN_FILE);
			}
		}
		
		// Open a temporary storage object
		tempStorage = createTempStorage();
		// Copy over contents of file
		int result = storage.CopyTo(0, null, null, tempStorage.getAddress());
		storage.Release();
		if (result != COM.S_OK) OLE.error(OLE.ERROR_CANNOT_OPEN_FILE, result);

		// create ole client
		int /*long*/[] ppv = new int /*long*/[1];
		result = COM.CoCreateInstance(appClsid, 0, COM.CLSCTX_INPROC_HANDLER | COM.CLSCTX_INPROC_SERVER, COM.IIDIUnknown, ppv);
		if (result != COM.S_OK) OLE.error(OLE.ERROR_CANNOT_CREATE_OBJECT, result);
		objIUnknown = new IUnknown(ppv[0]);
		// get the persistent storage of the ole client
		ppv = new int /*long*/[1];
		result = objIUnknown.QueryInterface(COM.IIDIPersistStorage, ppv);
		if (result != COM.S_OK) OLE.error(OLE.ERROR_CANNOT_CREATE_OBJECT, result);
		IPersistStorage iPersistStorage = new IPersistStorage(ppv[0]);
		// load the contents of the file into the ole client site
		result = iPersistStorage.Load(tempStorage.getAddress());
		iPersistStorage.Release();
		if (result != COM.S_OK)OLE.error(OLE.ERROR_CANNOT_CREATE_OBJECT, result);
	}
	
	// Init sinks
	addObjectReferences();
	
	if (COM.OleRun(objIUnknown.getAddress()) == OLE.S_OK) state = STATE_RUNNING;
}
protected void addObjectReferences() {
	//
	int /*long*/[] ppvObject = new int /*long*/[1];
	if (objIUnknown.QueryInterface(COM.IIDIPersist, ppvObject) == COM.S_OK) {
		IPersist objIPersist = new IPersist(ppvObject[0]);
		GUID tempid = new GUID();
		if (objIPersist.GetClassID(tempid) == COM.S_OK)
			objClsid = tempid;
		objIPersist.Release();
	}
	
	//
	ppvObject = new int /*long*/[1];
	int result = objIUnknown.QueryInterface(COM.IIDIViewObject2, ppvObject);
	if (result != COM.S_OK)
		OLE.error(OLE.ERROR_INTERFACE_NOT_FOUND, result);
	objIViewObject2 = new IViewObject2(ppvObject[0]);
	objIViewObject2.SetAdvise(aspect, 0, iAdviseSink.getAddress());

	//
	ppvObject = new int /*long*/[1];
	result = objIUnknown.QueryInterface(COM.IIDIOleObject, ppvObject);
	if (result != COM.S_OK)
		OLE.error(OLE.ERROR_INTERFACE_NOT_FOUND, result);
	objIOleObject = new IOleObject(ppvObject[0]);
	objIOleObject.SetClientSite(iOleClientSite.getAddress());
	int[] pdwConnection = new int[1];
	objIOleObject.Advise(iAdviseSink.getAddress(), pdwConnection);
	objIOleObject.SetHostNames("main", "main");  //$NON-NLS-1$ //$NON-NLS-2$

	// Notify the control object that it is embedded in an OLE container
	COM.OleSetContainedObject(objIUnknown.getAddress(), true);

	// Is OLE object linked or embedded?
	ppvObject = new int /*long*/[1];
	if (objIUnknown.QueryInterface(COM.IIDIOleLink, ppvObject) == COM.S_OK) {
		IOleLink objIOleLink = new IOleLink(ppvObject[0]);
		int /*long*/[] ppmk = new int /*long*/[1];
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
		public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
		public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
		public int /*long*/ method2(int /*long*/[] args) {return Release();}
	};
	
	iOleClientSite = new COMObject(new int[]{2, 0, 0, 0, 3, 1, 0, 1, 0}){
		public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
		public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
		public int /*long*/ method2(int /*long*/[] args) {return Release();}
		public int /*long*/ method3(int /*long*/[] args) {return SaveObject();}
		// method4 GetMoniker - not implemented
		public int /*long*/ method5(int /*long*/[] args) {return GetContainer(args[0]);}
		public int /*long*/ method6(int /*long*/[] args) {return ShowObject();}
		public int /*long*/ method7(int /*long*/[] args) {return OnShowWindow((int)/*64*/args[0]);}
		// method8 RequestNewObjectLayout - not implemented
	};
	
	iAdviseSink = new COMObject(new int[]{2, 0, 0, 2, 2, 1, 0, 0}){
		public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
		public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
		public int /*long*/ method2(int /*long*/[] args) {return Release();}
		public int /*long*/ method3(int /*long*/[] args) {return OnDataChange(args[0], args[1]);}
		public int /*long*/ method4(int /*long*/[] args) {return OnViewChange((int)/*64*/args[0], (int)/*64*/args[1]);}
		//method5 OnRename - not implemented
		public int /*long*/ method6(int /*long*/[] args) {OnSave();return 0;}
		public int /*long*/ method7(int /*long*/[] args) {return OnClose();}	
	};
	
	iOleInPlaceSite = new COMObject(new int[]{2, 0, 0, 1, 1, 0, 0, 0, 5, C.PTR_SIZEOF == 4 ? 2 : 1, 1, 0, 0, 0, 1}){
		public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
		public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
		public int /*long*/ method2(int /*long*/[] args) {return Release();}
		public int /*long*/ method3(int /*long*/[] args) {return GetWindow(args[0]);}
		public int /*long*/ method4(int /*long*/[] args) {return ContextSensitiveHelp((int)/*64*/args[0]);}
		public int /*long*/ method5(int /*long*/[] args) {return CanInPlaceActivate();}
		public int /*long*/ method6(int /*long*/[] args) {return OnInPlaceActivate();}
		public int /*long*/ method7(int /*long*/[] args) {return OnUIActivate();}
		public int /*long*/ method8(int /*long*/[] args) {return GetWindowContext(args[0], args[1], args[2], args[3], args[4]);}
		public int /*long*/ method9(int /*long*/[] args) {
			if (args.length == 2) {
				return Scroll((int)/*64*/args[0], (int)/*64*/args[1]);
			} else {
				return Scroll_64(args[0]);
			}
		}
		public int /*long*/ method10(int /*long*/[] args) {return OnUIDeactivate((int)/*64*/args[0]);}
		public int /*long*/ method11(int /*long*/[] args) {return OnInPlaceDeactivate();}
		// method12 DiscardUndoState - not implemented
		// method13 DeactivateAndUndoChange - not implemented
		public int /*long*/ method14(int /*long*/[] args) {return OnPosRectChange(args[0]);}
	};
	
	iOleDocumentSite = new COMObject(new int[]{2, 0, 0, 1}){
		public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
		public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
		public int /*long*/ method2(int /*long*/[] args) {return Release();}
		public int /*long*/ method3(int /*long*/[] args) {return ActivateMe(args[0]);}
	};	
}
protected IStorage createTempStorage() {
	int /*long*/[] tempStorage = new int /*long*/[1];
	int grfMode = COM.STGM_READWRITE | COM.STGM_SHARE_EXCLUSIVE | COM.STGM_DELETEONRELEASE;
	int result = COM.StgCreateDocfile(null, grfMode, 0, tempStorage);
	if (result != COM.S_OK) OLE.error(OLE.ERROR_CANNOT_CREATE_FILE, result);
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
	
	if (iOleDocumentSite != null)
		iOleDocumentSite.dispose();
	iOleDocumentSite = null;
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
	RECT rect = new RECT();
	OS.GetClientRect(handle, rect);
	int result = objIOleObject.DoVerb(verb, null, iOleClientSite.getAddress(), 0, handle, rect);

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
		int /*long*/[] address = new int /*long*/[1];
		if (objIUnknown.QueryInterface(COM.IIDIOleCommandTarget, address) != COM.S_OK)
			return OLE.ERROR_INTERFACE_NOT_FOUND;
		objIOleCommandTarget = new IOleCommandTarget(address[0]);
	}
	
	int /*long*/ inAddress = 0;
	if (in != null){
		inAddress = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, VARIANT.sizeof);
		in.getData(inAddress);
	}
	int /*long*/ outAddress = 0;
	if (out != null){
		outAddress = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, VARIANT.sizeof);
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
	int /*long*/[] ppvObject = new int /*long*/[1];
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
		if (result != COM.S_OK) return null;
	}
	return guid;
}
private int GetContainer(int /*long*/ ppContainer) {
	/* Simple containers that do not support links to their embedded 
	 * objects probably do not need to implement this method. Instead, 
	 * they can return E_NOINTERFACE and set ppContainer to NULL.
	 */
	if (ppContainer != 0)
		COM.MoveMemory(ppContainer, new int /*long*/[]{0}, OS.PTR_SIZEOF);
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
/**
 * Returns the indent value that would be used to compute the clipping area
 * of the active X object.
 * 
 * NOTE: The indent value is no longer being used by the client site.
 * 
 * @return the rectangle representing the indent
 */
public Rectangle getIndent() {
	return new Rectangle(indent.left, indent.right, indent.top, indent.bottom);
}
/**
 * Returns the program ID of the OLE Document or ActiveX Control.
 *
 * @return the program ID of the OLE Document or ActiveX Control
 */
public String getProgramID(){
	return getProgID(appClsid);
}
String getProgID(GUID clsid) {
	if (clsid != null){
		int /*long*/[] lplpszProgID = new int /*long*/[1];
		if (COM.ProgIDFromCLSID(clsid, lplpszProgID) == COM.S_OK) {
			int /*long*/ hMem = lplpszProgID[0];
			int length = OS.GlobalSize(hMem);
			int /*long*/ ptr = OS.GlobalLock(hMem);
			char[] buffer = new char[length];
			COM.MoveMemory(buffer, ptr, length);
			OS.GlobalUnlock(hMem);
			OS.GlobalFree(hMem);

			String result = new String(buffer);
			// remove null terminator
			int index = result.indexOf("\0");
			return result.substring(0, index);
		}
	}
	return null;
}
int ActivateMe(int /*long*/ pViewToActivate) {
	if (pViewToActivate == 0) {
		int /*long*/[] ppvObject = new int /*long*/[1];
		if (objIUnknown.QueryInterface(COM.IIDIOleDocument, ppvObject) != COM.S_OK) return COM.E_FAIL;
		IOleDocument objOleDocument = new IOleDocument(ppvObject[0]);
		if (objOleDocument.CreateView(iOleInPlaceSite.getAddress(), 0, 0, ppvObject) != COM.S_OK) return COM.E_FAIL;
		objOleDocument.Release();
		objDocumentView = new IOleDocumentView(ppvObject[0]);
	} else {
		objDocumentView = new IOleDocumentView(pViewToActivate);
		objDocumentView.AddRef();
		objDocumentView.SetInPlaceSite(iOleInPlaceSite.getAddress());
	}
	objDocumentView.UIActivate(1);//TRUE
	RECT rect = getRect();
	objDocumentView.SetRect(rect);
	objDocumentView.Show(1);//TRUE
	return COM.S_OK;
}
protected int GetWindow(int /*long*/ phwnd) {
	if (phwnd == 0)
		return COM.E_INVALIDARG;
	if (frame == null) {
		COM.MoveMemory(phwnd, new int /*long*/[] {0}, OS.PTR_SIZEOF);
		return COM.E_NOTIMPL;
	}
	
	// Copy the Window's handle into the memory passed in
	COM.MoveMemory(phwnd, new int /*long*/[] {handle}, OS.PTR_SIZEOF);
	return COM.S_OK;
}
RECT getRect() {
	Rectangle area = getClientArea();
	RECT rect = new RECT();
	rect.left   = area.x;
	rect.top    = area.y;
	rect.right  = area.x + area.width;
	rect.bottom = area.y + area.height;
	return rect;
}
private int GetWindowContext(int /*long*/ ppFrame, int /*long*/ ppDoc, int /*long*/ lprcPosRect, int /*long*/ lprcClipRect, int /*long*/ lpFrameInfo) {	
	if (frame == null || ppFrame == 0)
		return COM.E_NOTIMPL;

	// fill in frame handle
	int /*long*/ iOleInPlaceFrame = frame.getIOleInPlaceFrame();
	COM.MoveMemory(ppFrame, new int /*long*/[] {iOleInPlaceFrame}, OS.PTR_SIZEOF);
	frame.AddRef();

	// null out document handle
	if (ppDoc != 0) COM.MoveMemory(ppDoc, new int /*long*/[] {0}, OS.PTR_SIZEOF);

	// fill in position and clipping info
	RECT rect = getRect();
	if (lprcPosRect != 0) OS.MoveMemory(lprcPosRect, rect, RECT.sizeof);
	if (lprcClipRect != 0) OS.MoveMemory(lprcClipRect, rect, RECT.sizeof);

	// get frame info
	OLEINPLACEFRAMEINFO frameInfo = new OLEINPLACEFRAMEINFO();
	frameInfo.cb = OLEINPLACEFRAMEINFO.sizeof;
	frameInfo.fMDIApp = 0;
	frameInfo.hwndFrame = frame.handle;
	Shell shell = getShell();
	Menu menubar = shell.getMenuBar();
	if (menubar != null && !menubar.isDisposed()) {
		int /*long*/ hwnd = shell.handle;
		int cAccel = (int)/*64*/OS.SendMessage(hwnd, OS.WM_APP, 0, 0);
		if (cAccel != 0) {
			int /*long*/ hAccel = OS.SendMessage(hwnd, OS.WM_APP+1, 0, 0);
			if (hAccel != 0) {
				frameInfo.cAccelEntries = cAccel;
				frameInfo.haccel = hAccel;
			}
		}
	}
	COM.MoveMemory(lpFrameInfo, frameInfo, OLEINPLACEFRAMEINFO.sizeof);
	
	return COM.S_OK;
}
/**
 * Returns whether ole document is dirty by checking whether the content 
 * of the file representing the document is dirty.
 * 
 * @return <code>true</code> if the document has been modified,
 *         <code>false</code> otherwise.
 * @since 3.1
 */
public boolean isDirty() {
	/*
	 *  Note: this method must return true unless it is absolutely clear that the
	 * contents of the Ole Document do not differ from the contents in the file
	 * on the file system.
	 */
	
	// Get access to the persistent storage mechanism
	int /*long*/[] address = new int /*long*/[1];
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
	int /*long*/ focusHwnd = OS.GetFocus();
	if (objIOleInPlaceObject == null) return (handle == focusHwnd); 
	int /*long*/[] phwnd = new int /*long*/[1];
	objIOleInPlaceObject.GetWindow(phwnd);
	while (focusHwnd != 0) {
		if (phwnd[0] == focusHwnd) return true;
		focusHwnd = OS.GetParent(focusHwnd);
	}
	return false;
}
private boolean isOffice2007(boolean program) {
	String programID = getProgramID();
	if (programID == null) return false;
	if (program) {
		int lastDot = programID.lastIndexOf('.');
		if (lastDot != -1) {
			programID = programID.substring(0, lastDot);
			GUID guid = getClassID(programID); 
			programID = getProgID(guid);
			if (programID == null) return false;
		}
	}
	if (programID.equals("Word.Document.12")) return true; //$NON-NLS-1$ 
	if (programID.equals("Excel.Sheet.12")) return true; //$NON-NLS-1$ 
	if (programID.equals("PowerPoint.Show.12")) return true; //$NON-NLS-1$ 
	return false;
}
private int OnClose() {
	return COM.S_OK;
}
private int OnDataChange(int /*long*/ pFormatetc, int /*long*/ pStgmed) {
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
	int /*long*/[] phwnd = new int /*long*/[1];
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
	int /*long*/[] ppvObject = new int /*long*/[1];
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
	Shell shell = getShell();
	if (isFocusControl() || frame.isFocusControl()) {
		shell.traverse(SWT.TRAVERSE_TAB_NEXT);
	}
	return COM.S_OK;
}
private int OnPosRectChange(int /*long*/ lprcPosRect) {
	Point size = getSize();
	setExtent(size.x, size.y);
	return COM.S_OK;
}
private void onPaint(Event e) {
	if (state == STATE_RUNNING || state == STATE_INPLACEACTIVE) {
		SIZE size = getExtent();
		Rectangle area = getClientArea();
		RECT rect = new RECT();
		if (getProgramID().startsWith("Excel.Sheet")) { //$NON-NLS-1$
			rect.left = area.x; rect.right = area.x + (area.height * size.cx / size.cy);
			rect.top = area.y; rect.bottom = area.y + area.height;
		} else {
			rect.left = area.x; rect.right = area.x + size.cx;
			rect.top = area.y; rect.bottom = area.y + size.cy;
		}
		
		int /*long*/ pArea = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, RECT.sizeof);
		OS.MoveMemory(pArea, rect, RECT.sizeof);
		COM.OleDraw(objIUnknown.getAddress(), aspect, e.gc.handle, pArea);
		OS.GlobalFree(pArea);
	}
}
private void onResize(Event e) {
	setBounds();
}
private void OnSave() {
}
private int OnShowWindow(int fShow) {
	return COM.S_OK;
}
private int OnUIActivate() {
	if (objIOleInPlaceObject == null) return COM.E_FAIL;
	state = STATE_UIACTIVE;
	int /*long*/[] phwnd = new int /*long*/[1];
	if (objIOleInPlaceObject.GetWindow(phwnd) == COM.S_OK) {
		OS.SetWindowPos(phwnd[0], OS.HWND_TOP, 0, 0, 0, 0, OS.SWP_NOSIZE | OS.SWP_NOMOVE);
	}
	return COM.S_OK;
}
int OnUIDeactivate(int fUndoable) {
	// currently, we are ignoring the fUndoable flag
	if (frame == null || frame.isDisposed()) return COM.S_OK;
	state = STATE_INPLACEACTIVE;
	frame.SetActiveObject(0,0);
	redraw();
	Shell shell = getShell();
	if (isFocusControl() || frame.isFocusControl()) {
		shell.traverse(SWT.TRAVERSE_TAB_NEXT);
	}
	Menu menubar = shell.getMenuBar();
	if (menubar == null || menubar.isDisposed())
		return COM.S_OK;
		
	int /*long*/ shellHandle = shell.handle;
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
protected int QueryInterface(int /*long*/ riid, int /*long*/ ppvObject) {

	if (riid == 0 || ppvObject == 0)
		return COM.E_NOINTERFACE;
	GUID guid = new GUID();
	COM.MoveMemory(guid, riid, GUID.sizeof);

	if (COM.IsEqualGUID(guid, COM.IIDIUnknown)) {
		COM.MoveMemory(ppvObject, new int /*long*/[] {iUnknown.getAddress()}, OS.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID(guid, COM.IIDIAdviseSink)) {
		COM.MoveMemory(ppvObject, new int /*long*/[] {iAdviseSink.getAddress()}, OS.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID(guid, COM.IIDIOleClientSite)) {
		COM.MoveMemory(ppvObject, new int /*long*/[] {iOleClientSite.getAddress()}, OS.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID(guid, COM.IIDIOleInPlaceSite)) {
		COM.MoveMemory(ppvObject, new int /*long*/[] {iOleInPlaceSite.getAddress()}, OS.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID(guid, COM.IIDIOleDocumentSite )) {
		String progID = getProgramID();
		if (!progID.startsWith("PowerPoint")) { //$NON-NLS-1$
			COM.MoveMemory(ppvObject, new int /*long*/[] {iOleDocumentSite.getAddress()}, OS.PTR_SIZEOF);
			AddRef();
			return COM.S_OK;
		}
	}
	COM.MoveMemory(ppvObject, new int /*long*/[] {0}, OS.PTR_SIZEOF);
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
		int /*long*/[] address = new int /*long*/[1];
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
	
	if (objDocumentView != null){
		objDocumentView.Release();
	}
	objDocumentView = null;
	
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
/**
 * Saves the document to the specified file and includes OLE specific information if specified.  
 * This method must <b>only</b> be used for files that have an OLE Storage format.  For example, 
 * a word file edited with Word.Document should be saved using this method because there is 
 * formating information that should be stored in the OLE specific Storage format.
 *
 * @param file the file to which the changes are to be saved
 * @param includeOleInfo the flag to indicate whether OLE specific information should be saved.
 *
 * @return true if the save was successful
 */
public boolean save(File file, boolean includeOleInfo) {
	/*
	* Bug in Office 2007. Saving Office 2007 documents to compound file storage object
	* causes the output file to be corrupted. The fix is to detect Office 2007 documents
	* using the program ID and save only the content of the 'Package' stream. 
	*/
	if (isOffice2007(false)) {
		return saveOffice2007(file);
	}
	if (includeOleInfo)
		return saveToStorageFile(file);
	return saveToTraditionalFile(file);
}
private boolean saveFromContents(int /*long*/ address, File file) {

	boolean success = false;
	
	IStream tempContents = new IStream(address);
	tempContents.AddRef();

	try {
		FileOutputStream writer = new FileOutputStream(file);
		
		int increment = 1024 * 4;
		int /*long*/ pv = COM.CoTaskMemAlloc(increment);
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
private boolean saveFromOle10Native(int /*long*/ address, File file) {

	boolean success = false;
	
	IStream tempContents = new IStream(address);
	tempContents.AddRef();
	
	// The "\1Ole10Native" stream contains a DWORD header whose value is the length 
	// of the native data that follows.
	int /*long*/ pv = COM.CoTaskMemAlloc(4);
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
private boolean saveOffice2007(File file) {
	if (file == null || file.isDirectory()) return false;
	if (!updateStorage()) return false;
	boolean result = false;
	
	/* Excel fails to open the package stream when the PersistStorage is not in hands off mode */
	int /*long*/[] ppv = new int /*long*/[1];
	IPersistStorage iPersistStorage = null;
	if (objIUnknown.QueryInterface(COM.IIDIPersistStorage, ppv) == COM.S_OK) {
		iPersistStorage = new IPersistStorage(ppv[0]);
		tempStorage.AddRef();
		iPersistStorage.HandsOffStorage();
	}
	int /*long*/[] address = new int /*long*/[1];
	int grfMode = COM.STGM_DIRECT | COM.STGM_READ | COM.STGM_SHARE_EXCLUSIVE;
	if (tempStorage.OpenStream("Package", 0, grfMode, 0, address) == COM.S_OK) { //$NON-NLS-1$
		result = saveFromContents(address[0], file);
	}
	if (iPersistStorage != null) {
		iPersistStorage.SaveCompleted(tempStorage.getAddress());
		tempStorage.Release();
		iPersistStorage.Release();
	}
	return result;
}
/**
 * Saves the document to the specified file and includes OLE specific information.  This method 
 * must <b>only</b> be used for files that have an OLE Storage format.  For example, a word file 
 * edited with Word.Document should be saved using this method because there is formating information
 * that should be stored in the OLE specific Storage format.
 *
 * @param file the file to which the changes are to be saved
 *
 * @return true if the save was successful
 */
private boolean saveToStorageFile(File file) {
	// The file will be saved using the formating of the current application - this
	// may not be the format of the application that was originally used to create the file
	// e.g. if an Excel file is opened in Word, the Word application will save the file in the 
	// Word format
	// Note: if the file already exists, some applications will not overwrite the file
	// In these cases, you should delete the file first (probably save the contents of the file in case the
	// save fails)
	if (file == null || file.isDirectory()) return false;
	if (!updateStorage()) return false;
	
	// get access to the persistent storage mechanism
	int /*long*/[] address = new int /*long*/[1];
	if (objIOleObject.QueryInterface(COM.IIDIPersistStorage, address) != COM.S_OK) return false;
	IPersistStorage permStorage = new IPersistStorage(address[0]);
	try {
		address = new int /*long*/[1];
		char[] path = (file.getAbsolutePath()+"\0").toCharArray();
		int mode = COM.STGM_TRANSACTED | COM.STGM_READWRITE | COM.STGM_SHARE_EXCLUSIVE | COM.STGM_CREATE;
		int result = COM.StgCreateDocfile(path, mode, 0, address); //Does an AddRef if successful
		if (result != COM.S_OK) return false;
		IStorage storage =  new IStorage(address[0]);
		try {
			if (COM.OleSave(permStorage.getAddress(), storage.getAddress(), false) == COM.S_OK) {
				if (storage.Commit(COM.STGC_DEFAULT) == COM.S_OK) {
					return true;
				}
			}
		} finally {
			storage.Release();
		}
	} finally {
		permStorage.Release();
	}
	return false;
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
	
	int /*long*/[] address = new int /*long*/[1];
	// Look for a CONTENTS stream
	if (tempStorage.OpenStream("CONTENTS", 0, COM.STGM_DIRECT | COM.STGM_READ | COM.STGM_SHARE_EXCLUSIVE, 0, address) == COM.S_OK) //$NON-NLS-1$
		return saveFromContents(address[0], file);
		
	// Look for Ole 1.0 object stream
	if (tempStorage.OpenStream("\1Ole10Native", 0, COM.STGM_DIRECT | COM.STGM_READ | COM.STGM_SHARE_EXCLUSIVE, 0, address) == COM.S_OK) //$NON-NLS-1$
		return saveFromOle10Native(address[0], file);
		
	return false;
}
private int Scroll_64(int /*long*/ scrollExtent) {
	return COM.S_OK;
}
private int Scroll(int scrollExtent_cx, int scrollExtent_cy) {
	return COM.S_OK;
}
void setBorderSpace(RECT newBorderwidth) {
	borderWidths = newBorderwidth;
	// readjust size and location of client site
	setBounds();
}
void setBounds() {
	Rectangle area = frame.getClientArea();
	setBounds(borderWidths.left, 
		      borderWidths.top, 
			  area.width - borderWidths.left - borderWidths.right, 
			  area.height - borderWidths.top - borderWidths.bottom);
	setObjectRects();
}
private void setExtent(int width, int height){
	// Resize the width and height of the embedded/linked OLENatives object
	// to the specified values.

	if (objIOleObject == null || isStatic || inUpdate) return;
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
/**
 * The indent value is no longer being used by the client site.
 * 
 * @param newIndent the rectangle representing the indent amount
 */
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
	RECT rect = getRect();
	objIOleInPlaceObject.SetObjectRects(rect, rect);
}

private int ShowObject() {
	/* Tells the container to position the object so it is visible to 
	 * the user. This method ensures that the container itself is 
	 * visible and not minimized.
	 */
	setBounds();
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
	int /*long*/[] ppvObject = new int /*long*/[1];
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
	result = COM.OleCreatePropertyFrame(frame.handle, 10, 10, chTitle, 1, new int /*long*/[] {objIUnknown.getAddress()}, caGUID.cElems, caGUID.pElems, COM.LOCALE_USER_DEFAULT, 0, 0);

	// free the property page information
	COM.CoTaskMemFree(caGUID.pElems);
}
private boolean updateStorage() {

	if (tempStorage == null) return false;

	int /*long*/[] ppv = new int /*long*/[1];
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

	int /*long*/ hDC = OS.GetDC(0);
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

	int /*long*/ hDC = OS.GetDC(0);
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
