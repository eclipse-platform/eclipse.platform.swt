package org.eclipse.swt.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

final class OleEventSink
{
	private OleControlSite controlSite;
	
	private COMObject iUnknown;
	private COMObject iDispatch;
	private int refCount;

	private int  eventCookie;
	private GUID eventSinkDispInterface;

	private OleEventTable eventTable;
	
OleEventSink(OleControlSite controlSite) {

	this.controlSite = controlSite;

	createCOMInterfaces();
}
void addListener(int eventID, OleListener listener) {
	if (listener == null) OLE.error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) eventTable = new OleEventTable ();
	eventTable.hook(eventID, listener);
}
int AddRef() {
	refCount++;
	return refCount;
}
void connect(IUnknown objIUnknown) {

	// What is the custom Event Sink interface?
	eventSinkDispInterface = getEventSinkDispInterface(objIUnknown);

	// It is possible that the Control does not have an Event sink.
	// In this case, just carry on.
	if (eventSinkDispInterface == null) return;

	int[] ppvObject = new int[1];
	if (objIUnknown.QueryInterface(COM.IIDIConnectionPointContainer, ppvObject) == COM.S_OK) {
		IConnectionPointContainer cpc = new IConnectionPointContainer(ppvObject[0]);
		int[] ppCP = new int[1];
		if (cpc.FindConnectionPoint(eventSinkDispInterface, ppCP) == COM.S_OK) {
			IConnectionPoint cp = new IConnectionPoint(ppCP[0]);
			int[] pCookie = new int[1];
			if (cp.Advise(iUnknown.getAddress(), pCookie) == COM.S_OK)
				eventCookie = pCookie[0];
			cp.Release();
		}
		cpc.Release();
	}
}
private void createCOMInterfaces() {
	// register each of the interfaces that this object implements
	iUnknown = new COMObject(new int[]{2, 0, 0}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
	};
	iDispatch = new COMObject(new int[]{2, 0, 0, 1, 3, 4, 8}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		// method3 GetTypeInfoCount - not implemented
		// method4 GetTypeInfo - not implemented
		// method5 GetIDsOfNames - not implemented
		public int method6(int[] args) {return Invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);}
	};
}
void disconnect(IUnknown objIUnknown) {

	// disconnect event sink
	if (eventCookie != 0 && objIUnknown != null) {
		int[] ppvObject = new int[1];
		if (objIUnknown.QueryInterface(COM.IIDIConnectionPointContainer, ppvObject) == COM.S_OK) {
			IConnectionPointContainer cpc = new IConnectionPointContainer(ppvObject[0]);
			if (cpc.FindConnectionPoint(eventSinkDispInterface, ppvObject) == COM.S_OK) {
				IConnectionPoint cp = new IConnectionPoint(ppvObject[0]);
				if (cp.Unadvise(eventCookie) == COM.S_OK) {
					eventCookie = 0;
				}
				cp.Release();
			}
			cpc.Release();
		}
	}
}
private void disposeCOMInterfaces() {

	if (iUnknown != null);
		iUnknown.dispose();
	iUnknown = null;
	
	if (iDispatch != null)
		iDispatch.dispose();
	iDispatch = null;
	
}
private GUID getEventSinkDispInterface(IUnknown objIUnknown) {

	// get Event Sink I/F from IProvideClassInfo2
	int[] ppvObject = new int[1];
	if (objIUnknown.QueryInterface(COM.IIDIProvideClassInfo2, ppvObject) == COM.S_OK) {
		IProvideClassInfo2 pci2 = new IProvideClassInfo2(ppvObject[0]);
		GUID eventsinkdisp = new GUID();
		int result = pci2.GetGUID(COM.GUIDKIND_DEFAULT_SOURCE_DISP_IID, eventsinkdisp);
		pci2.Release();
		if (result == COM.S_OK) {
			eventSinkDispInterface = eventsinkdisp;
			return eventSinkDispInterface;
		}
		
	}

	// get Event Sink I/F from IProvideClassInfo
	if (objIUnknown.QueryInterface(COM.IIDIProvideClassInfo, ppvObject) == COM.S_OK) {
		int[] ppTI = new int[1];
		int[] ppEI = new int[1];
		TYPEATTR typeAttribute = null;
		IProvideClassInfo pci = new IProvideClassInfo(ppvObject[0]);
		int result = pci.GetClassInfo(ppTI);
		pci.Release();
		if (result != COM.S_OK) return null;
		
		if (ppTI != null && ppTI.length == 1) {
			ITypeInfo classInfo = new ITypeInfo(ppTI[0]);
			int[] ppTypeAttr = new int[1];
			result = classInfo.GetTypeAttr(ppTypeAttr);
			if (result != COM.S_OK) {
				classInfo.Release();
				return null;
			}
			typeAttribute = new TYPEATTR();
			COM.MoveMemory(typeAttribute, ppTypeAttr[0], TYPEATTR.sizeof);
			classInfo.ReleaseTypeAttr(ppTypeAttr[0]);
			int implMask = COM.IMPLTYPEFLAG_FDEFAULT | COM.IMPLTYPEFLAG_FSOURCE | COM.IMPLTYPEFLAG_FRESTRICTED;
			int implBits = COM.IMPLTYPEFLAG_FDEFAULT | COM.IMPLTYPEFLAG_FSOURCE;
			for (int i = 0; i < typeAttribute.cImplTypes; i++) {
				int[] pImplTypeFlags = new int[1];
				if (classInfo.GetImplTypeFlags(i, pImplTypeFlags) == COM.S_OK) {
					if ((pImplTypeFlags[0] & implMask) == implBits) {
						int[] pRefType = new int[1];
						if (classInfo.GetRefTypeOfImplType(i, pRefType) == COM.S_OK) {
							classInfo.GetRefTypeInfo(pRefType[0], ppEI);
						}
					}
				}
			}
			classInfo.Release();

			//
			if (ppEI != null && ppEI.length == 1) {
				ppTypeAttr = new int[1];
				ITypeInfo eventInfo = new ITypeInfo(ppEI[0]);
				if (eventInfo.GetTypeAttr(ppTypeAttr) == COM.S_OK) {
					typeAttribute = new TYPEATTR();
					COM.MoveMemory(typeAttribute, ppTypeAttr[0], TYPEATTR.sizeof);
					eventInfo.ReleaseTypeAttr(ppTypeAttr[0]);
					if (ppTypeAttr != null && ppTypeAttr[0] != 0) {
						eventSinkDispInterface = new GUID();
						eventSinkDispInterface.data1 = typeAttribute.guid_data1;
						eventSinkDispInterface.data2 = typeAttribute.guid_data2;
						eventSinkDispInterface.data3 = typeAttribute.guid_data3;
						eventSinkDispInterface.b0 = typeAttribute.guid_b0;
						eventSinkDispInterface.b1 = typeAttribute.guid_b1;
						eventSinkDispInterface.b2 = typeAttribute.guid_b2;
						eventSinkDispInterface.b3 = typeAttribute.guid_b3;
						eventSinkDispInterface.b4 = typeAttribute.guid_b4;
						eventSinkDispInterface.b5 = typeAttribute.guid_b5;
						eventSinkDispInterface.b6 = typeAttribute.guid_b6;
						eventSinkDispInterface.b7 = typeAttribute.guid_b7;
					}
				}
				eventInfo.Release();
			}
		}
	}
	return eventSinkDispInterface;
}
private int Invoke(int dispIdMember, int riid, int lcid, int dwFlags, int pDispParams, int pVarResult, int pExcepInfo, int pArgErr)
{
	if (eventTable == null || !eventTable.hooks(dispIdMember)) return COM.S_OK;
	
	// Construct an array of the parameters that are passed in
	// Note: parameters are passed in reverse order - here we will correct the order
	Variant[] eventInfo = null;
	if (pDispParams != 0) {	
		DISPPARAMS dispParams = new DISPPARAMS();
		COM.MoveMemory(dispParams, pDispParams, DISPPARAMS.sizeof);
		eventInfo = new Variant[dispParams.cArgs];
		int size = Variant.sizeof;
		int offset = (dispParams.cArgs - 1) * size;
		
		for (int j = 0; j < dispParams.cArgs; j++){
			eventInfo[j] = new Variant();
			eventInfo[j].setData(dispParams.rgvarg + offset);
			offset = offset - size;
		}
	}
		
	OleEvent event = new OleEvent();
	event.arguments = eventInfo;
	notifyListener(dispIdMember,event);

	return COM.S_OK;
}
/**
* Notify listeners of an event.
* <p>
*	This method notifies all listeners that an event
* has occurred.
*
* @param eventType the desired SWT event
* @param event the event data
*
* @exception SWTError <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
* 		<li>ERROR_NULL_ARGUMENT when handler is null</li>
*	</ul>
*/
private void notifyListener (int eventType, OleEvent event) {
	if (event == null) OLE.error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	event.type = eventType;
	event.widget = controlSite;
	eventTable.sendEvent (event);
}
private int QueryInterface(int riid, int ppvObject) {

	if (riid == 0 || ppvObject == 0)
		return COM.E_INVALIDARG;
	GUID guid = new GUID();
	COM.MoveMemory(guid, riid, GUID.sizeof);

	if ( COM.IsEqualGUID(guid, COM.IIDIUnknown) || COM.IsEqualGUID(guid, COM.IIDIDispatch) ||
			COM.IsEqualGUID(guid, eventSinkDispInterface)) {
		COM.MoveMemory(ppvObject, new int[] {iDispatch.getAddress()}, 4);
		AddRef();
		return OLE.S_OK;
	}

	COM.MoveMemory(ppvObject, new int[] {0}, 4);
	return COM.E_NOINTERFACE;
}
int Release() {
	refCount--;
	if (refCount == 0) {
		disposeCOMInterfaces();
	}
	
	return refCount;
}
void removeListener(int eventID, OleListener listener) {
	if (listener == null) OLE.error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventID, listener);
}
}
