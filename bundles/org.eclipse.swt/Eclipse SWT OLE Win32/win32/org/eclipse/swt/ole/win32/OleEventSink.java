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

import org.eclipse.swt.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.internal.ole.win32.*;

final class OleEventSink
{
	private OleControlSite widget;
	
	private COMObject iDispatch;
	private int refCount;

	private IUnknown objIUnknown;
	private int  eventCookie;
	private GUID eventGuid;

	private OleEventTable eventTable;
	
OleEventSink(OleControlSite widget, int /*long*/ iUnknown, GUID riid) {

	this.widget = widget;
	this.eventGuid = riid;
	this.objIUnknown = new IUnknown(iUnknown);
	
	createCOMInterfaces();
}

void connect () {
	int /*long*/[] ppvObject = new int /*long*/[1];
	if (objIUnknown.QueryInterface(COM.IIDIConnectionPointContainer, ppvObject) == COM.S_OK) {
		IConnectionPointContainer cpc = new IConnectionPointContainer(ppvObject[0]);
		int /*long*/[] ppCP = new int /*long*/[1];
		if (cpc.FindConnectionPoint(eventGuid, ppCP) == COM.S_OK) {
			IConnectionPoint cp = new IConnectionPoint(ppCP[0]);
			int[] pCookie = new int[1];
			if (cp.Advise(iDispatch.getAddress(), pCookie) == COM.S_OK)
				eventCookie = pCookie[0];
			cp.Release();
		}
		cpc.Release();
	}
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
private void createCOMInterfaces() {
	iDispatch = new COMObject(new int[]{2, 0, 0, 1, 3, 4, 8}){
		public int /*long*/ method0(int /*long*/[] args) {return QueryInterface(args[0], args[1]);}
		public int /*long*/ method1(int /*long*/[] args) {return AddRef();}
		public int /*long*/ method2(int /*long*/[] args) {return Release();}
		// method3 GetTypeInfoCount - not implemented
		// method4 GetTypeInfo - not implemented
		// method5 GetIDsOfNames - not implemented
		public int /*long*/ method6(int /*long*/[] args) {return Invoke((int)/*64*/args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3], args[4], args[5], args[6], args[7]);}
	};
}
void disconnect() {
	// disconnect event sink
	if (eventCookie != 0 && objIUnknown != null) {
		int /*long*/[] ppvObject = new int /*long*/[1];
		if (objIUnknown.QueryInterface(COM.IIDIConnectionPointContainer, ppvObject) == COM.S_OK) {
			IConnectionPointContainer cpc = new IConnectionPointContainer(ppvObject[0]);
			if (cpc.FindConnectionPoint(eventGuid, ppvObject) == COM.S_OK) {
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
	if (iDispatch != null)
		iDispatch.dispose();
	iDispatch = null;
	
}
private int Invoke(int dispIdMember, int /*long*/ riid, int lcid, int dwFlags, int /*long*/ pDispParams, int /*long*/ pVarResult, int /*long*/ pExcepInfo, int /*long*/ pArgErr)
{
	if (eventTable == null || !eventTable.hooks(dispIdMember)) return COM.S_OK;
	
	// Construct an array of the parameters that are passed in
	// Note: parameters are passed in reverse order - here we will correct the order
	Variant[] eventInfo = null;
	if (pDispParams != 0) {	
		DISPPARAMS dispParams = new DISPPARAMS();
		COM.MoveMemory(dispParams, pDispParams, DISPPARAMS.sizeof);
		eventInfo = new Variant[dispParams.cArgs];
		int size = VARIANT.sizeof;
		int /*long*/ offset = (dispParams.cArgs - 1) * size;
		
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
	event.widget = widget;
	eventTable.sendEvent (event);
}
private int QueryInterface(int /*long*/ riid, int /*long*/ ppvObject) {

	if (riid == 0 || ppvObject == 0)
		return COM.E_INVALIDARG;
	GUID guid = new GUID();
	COM.MoveMemory(guid, riid, GUID.sizeof);

	if ( COM.IsEqualGUID(guid, COM.IIDIUnknown) || COM.IsEqualGUID(guid, COM.IIDIDispatch) ||
			COM.IsEqualGUID(guid, eventGuid)) {
		COM.MoveMemory(ppvObject, new int /*long*/[] {iDispatch.getAddress()}, OS.PTR_SIZEOF);
		AddRef();
		return OLE.S_OK;
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
void removeListener(int eventID, OleListener listener) {
	if (listener == null) OLE.error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (eventID, listener);
}
boolean hasListeners() {
	return eventTable.hasEntries();
}
}
