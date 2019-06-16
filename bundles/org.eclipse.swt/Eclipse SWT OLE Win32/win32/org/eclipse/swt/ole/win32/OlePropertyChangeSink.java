/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.ole.win32;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

final class OlePropertyChangeSink {

	private OleControlSite controlSite;
	//private IUnknown objIUnknown;

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
	long[] ppvObject = new long[1];
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
	iPropertyNotifySink = new COMObject(new int[]{2, 0, 0, 1, 1}){
		@Override
		public long method0(long[] args) {return QueryInterface(args[0], args[1]);}
		@Override
		public long method1(long[] args) {return AddRef();}
		@Override
		public long method2(long[] args) {return Release();}
		@Override
		public long method3(long[] args) {return OnChanged((int)args[0]);}
		@Override
		public long method4(long[] args) {return OnRequestEdit((int)args[0]);}
	};
}
void disconnect(IUnknown objIUnknown) {

	// disconnect property notification sink
	if (propertyCookie != 0 && objIUnknown != null) {
		long[] ppvObject = new long[1];
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
private int QueryInterface(long riid, long ppvObject) {
	if (riid == 0 || ppvObject == 0)
		return COM.E_INVALIDARG;
	GUID guid = new GUID();
	COM.MoveMemory(guid, riid, GUID.sizeof);
	if (COM.IsEqualGUID(guid, COM.IIDIUnknown) || COM.IsEqualGUID(guid, COM.IIDIPropertyNotifySink)) {
		OS.MoveMemory(ppvObject, new long[] {iPropertyNotifySink.getAddress()}, C.PTR_SIZEOF);
		AddRef();
		return COM.S_OK;
	}
	OS.MoveMemory(ppvObject, new long[] {0}, C.PTR_SIZEOF);
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
