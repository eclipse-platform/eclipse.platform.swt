/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.ole.win32;


import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;

final class OlePropertyChangeSink {

	private OleControlSite controlSite;
	//private IUnknown objIUnknown;
	
	private COMObject iUnknown;
	private COMObject iPropertyNotifySink;

	private int refCount;
	
	private int propertyCookie;

	private OleEventTable eventTable;
	
OlePropertyChangeSink(OleControlSite controlSite) {
	
	this.controlSite = controlSite;
	
	createCOMInterfaces();
}
void addListener(int propertyID, OleListener listener) {
	if (listener == null) OLE.error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) eventTable = new OleEventTable ();
	eventTable.hook(propertyID, listener);
}
int AddRef() {
	refCount++;
	return refCount;
}
void connect(IUnknown objIUnknown) {

	// Set up property change notification sink
	int /*long*/[] ppvObject = new int /*long*/[1];
	if (objIUnknown.QueryInterface(COM.IIDIConnectionPointContainer, ppvObject) == COM.S_OK) {
		IConnectionPointContainer cpc = new IConnectionPointContainer(ppvObject[0]);
		if (cpc.FindConnectionPoint(COM.IIDIPropertyNotifySink, ppvObject) == COM.S_OK) {
			IConnectionPoint cp = new IConnectionPoint(ppvObject[0]);
			int[] cookie = new int[1];
			if (cp.Advise(iPropertyNotifySink.getAddress(), cookie) == COM.S_OK) {
				propertyCookie = cookie[0];
			}
			cp.Release();
		}
		cpc.Release();
	}
}
private void createCOMInterfaces() {
	// register each of the interfaces that this object implements
	iUnknown = new COMObject(new int[]{2, 0, 0}){
		public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
		public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
		public int /*long*/ method2(int /*long*/[] args) {return Release();}
	};
	
	iPropertyNotifySink = new COMObject(new int[]{2, 0, 0, 1, 1}){
		public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
		public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
		public int /*long*/ method2(int /*long*/[] args) {return Release();}
		public int /*long*/ method3(int /*long*/[] args) {return OnChanged((int)/*64*/args[0]);}
		public int /*long*/ method4(int /*long*/[] args) {return OnRequestEdit((int)/*64*/args[0]);}
	};
}
void disconnect(IUnknown objIUnknown) {

	// disconnect property notification sink
	if (propertyCookie != 0 && objIUnknown != null) {
		int /*long*/[] ppvObject = new int /*long*/[1];
		if (objIUnknown.QueryInterface(COM.IIDIConnectionPointContainer, ppvObject) == COM.S_OK) {
			IConnectionPointContainer cpc = new IConnectionPointContainer(ppvObject[0]);
			if (cpc.FindConnectionPoint(COM.IIDIPropertyNotifySink, ppvObject) == COM.S_OK) {
				IConnectionPoint cp = new IConnectionPoint(ppvObject[0]);
				if (cp.Unadvise(propertyCookie) == COM.S_OK) {
					propertyCookie = 0;
				}
				cp.Release();
			}
			cpc.Release();
		}
	}
}
private void disposeCOMInterfaces() {
	if (iUnknown != null) iUnknown.dispose();
	iUnknown = null;
	if (iPropertyNotifySink != null) iPropertyNotifySink.dispose();
	iPropertyNotifySink = null;
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
* @exception IllegalArgumentException <ul>
* 		<li>ERROR_NULL_ARGUMENT when handler is null</li>
* </ul>
* @exception SWTException <ul>
*		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
*		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
*	</ul>
*/
private void notifyListener (int eventType, OleEvent event) {
	if (event == null) OLE.error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	event.type = eventType;
	event.widget = controlSite;
	eventTable.sendEvent (event);
}
private int OnChanged(int dispID) {
	if (eventTable == null || !eventTable.hooks(dispID)) return COM.S_OK;
	OleEvent event = new OleEvent();
	event.detail = OLE.PROPERTY_CHANGED;
	notifyListener(dispID,event);
	return COM.S_OK;
}
private int OnRequestEdit(int dispID) {
	if (eventTable == null || !eventTable.hooks(dispID)) return COM.S_OK;
	OleEvent event = new OleEvent();
	event.doit = true;
	event.detail = OLE.PROPERTY_CHANGING;
	notifyListener(dispID,event);
	return (event.doit) ? COM.S_OK : COM.S_FALSE;
}
private int QueryInterface(int /*long*/ riid, int /*long*/ ppvObject) {
	if (riid == 0 || ppvObject == 0)
		return COM.E_INVALIDARG;
	GUID guid = new GUID();
	COM.MoveMemory(guid, riid, GUID.sizeof);
	if (COM.IsEqualGUID(guid, COM.IIDIUnknown)) {
		COM.MoveMemory(ppvObject, new int /*long*/[] {iUnknown.getAddress()}, OS.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	if (COM.IsEqualGUID(guid, COM.IIDIPropertyNotifySink)) {
		COM.MoveMemory(ppvObject, new int /*long*/[] {iPropertyNotifySink.getAddress()}, OS.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	COM.MoveMemory(ppvObject, new int /*long*/[] {0}, OS.PTR_SIZEOF);
	return COM.E_NOINTERFACE;
}
int Release() {
	refCount--;
	if (refCount == 0) {
		disposeCOMInterfaces();
	}
	return refCount;
}
void removeListener(int propertyID, OleListener listener) {
	if (listener == null) OLE.error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (propertyID, listener);
}
}
