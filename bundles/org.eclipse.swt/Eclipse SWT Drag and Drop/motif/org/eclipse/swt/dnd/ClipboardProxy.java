/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.widgets.*;

class ClipboardProxy {

	Display display;
	int shellHandle;
	int atomAtom, clipboardAtom, motifClipboardAtom, primaryAtom, targetsAtom;
	int[][] convertData = new int[10][3];
	Clipboard activeClipboard = null;
	Clipboard activePrimaryClipboard = null;
	Object[] clipboardData;
	Transfer[] clipboardDataTypes;
	Object[] primaryClipboardData;
	Transfer[] primaryClipboardDataTypes;
	
	boolean done = false;
	Object selectionValue;
	Transfer selectionTransfer;
	
	Callback XtConvertSelectionCallback;
	Callback XtLoseSelectionCallback;
	Callback XtSelectionDoneCallback;
	Callback XtSelectionCallbackCallback;
	
	static byte [] ATOM = Converter.wcsToMbcs (null, "ATOM", true); //$NON-NLS-1$
	static byte [] CLIPBOARD = Converter.wcsToMbcs (null, "CLIPBOARD", true); //$NON-NLS-1$
	static byte [] PRIMARY = Converter.wcsToMbcs (null, "PRIMARY", true); //$NON-NLS-1$
	static byte [] TARGETS = Converter.wcsToMbcs (null, "TARGETS", true); //$NON-NLS-1$
	static byte [] _MOTIF_CLIPBOARD_TARGETS = Converter.wcsToMbcs (null, "_MOTIF_CLIPBOARD_TARGETS", true); //$NON-NLS-1$
	static String ID = "CLIPBOARD PROXY OBJECT"; //$NON-NLS-1$	
	
	static ClipboardProxy _getInstance(final Display display) {
		ClipboardProxy proxy = (ClipboardProxy) display.getData(ID);
		if (proxy != null) return proxy;
		proxy = new ClipboardProxy(display);
		display.setData(ID, proxy);
		display.addListener(SWT.Dispose, new Listener() {
			public void handleEvent(Event event) {
				ClipboardProxy clipbordProxy = (ClipboardProxy)display.getData(ID);
				if (clipbordProxy == null) return;
				display.setData(ID, null);
				clipbordProxy.dispose();
			}
		});
		return proxy;
	}	

ClipboardProxy(Display display) {	
	this.display = display;
	XtConvertSelectionCallback = new Callback(this, "XtConvertSelection", 7); //$NON-NLS-1$
	if (XtConvertSelectionCallback.getAddress() == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	XtLoseSelectionCallback = new Callback(this, "XtLoseSelection", 2); //$NON-NLS-1$
	if (XtLoseSelectionCallback.getAddress() == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	XtSelectionDoneCallback = new Callback(this, "XtSelectionDone", 3); //$NON-NLS-1$
	if (XtSelectionDoneCallback.getAddress() == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	XtSelectionCallbackCallback = new Callback(this, "XtSelectionCallback", 7); //$NON-NLS-1$
	if (XtSelectionCallbackCallback.getAddress() == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	
	int widgetClass = OS.topLevelShellWidgetClass ();
	shellHandle = OS.XtAppCreateShell (null, null, widgetClass, display.xDisplay, null, 0);
	OS.XtSetMappedWhenManaged (shellHandle, false);
	OS.XtRealizeWidget (shellHandle);
	int xDisplay = OS.XtDisplay(shellHandle);
	atomAtom = OS.XmInternAtom(xDisplay, ATOM, true);
	clipboardAtom = OS.XmInternAtom(xDisplay, CLIPBOARD, true);
	motifClipboardAtom = OS.XmInternAtom(xDisplay, _MOTIF_CLIPBOARD_TARGETS, true);
	primaryAtom = OS.XmInternAtom(xDisplay, PRIMARY, true);
	targetsAtom = OS.XmInternAtom(xDisplay, TARGETS, true);
}


 void clear(Clipboard owner, int clipboards) {
	int xDisplay = OS.XtDisplay(shellHandle);
	if (xDisplay == 0) return;
	if ((clipboards & DND.CLIPBOARD) != 0 && activeClipboard == owner) {
		OS.XtDisownSelection(shellHandle, clipboardAtom, OS.CurrentTime);
	}
	if ((clipboards & DND.SELECTION_CLIPBOARD) != 0 && activePrimaryClipboard == owner) {
		OS.XtDisownSelection(shellHandle, primaryAtom, OS.CurrentTime);
	}
}
	
 void dispose () {
	if (display == null) return;
	if (shellHandle != 0) {
		OS.XtDestroyWidget (shellHandle);
		shellHandle = 0;
	}
	if (XtConvertSelectionCallback != null) XtConvertSelectionCallback.dispose();
	XtConvertSelectionCallback = null;
	if (XtLoseSelectionCallback != null) XtLoseSelectionCallback.dispose();
	XtLoseSelectionCallback = null;
	if (XtSelectionCallbackCallback != null) XtSelectionCallbackCallback.dispose();
	XtSelectionCallbackCallback = null;
	if (XtSelectionDoneCallback != null) XtSelectionDoneCallback.dispose();
	XtSelectionDoneCallback = null;
	activeClipboard = null;
	activePrimaryClipboard = null;
	clipboardData = null;
	clipboardDataTypes = null;
	primaryClipboardData = null;
	primaryClipboardDataTypes = null;
}
 
Object getContents(Transfer transfer, int clipboardType) {
	int[] types = getAvailableTypes(clipboardType);
	int index = -1;
	TransferData transferData = new TransferData();
	for (int i = 0; i < types.length; i++) {
		transferData.type = types[i];
		if (transfer.isSupportedType(transferData)) {
			index = i;
			break;
		}
	}
	if (index == -1) return null;
	done = false;
	selectionValue = null; selectionTransfer = transfer;
	int selection = clipboardType == DND.CLIPBOARD ? clipboardAtom : primaryAtom;
	OS.XtGetSelectionValue(shellHandle, selection, types[index], XtSelectionCallbackCallback.getAddress(), 0, OS.CurrentTime);
	if (!done) {
		int xDisplay = OS.XtDisplay (shellHandle);
		if (xDisplay == 0) return null;
		int xtContext = OS.XtDisplayToApplicationContext(xDisplay);
		int selectionTimeout = OS.XtAppGetSelectionTimeout(xtContext);
		wait(selectionTimeout);
	}
	return (!done) ? null : selectionValue;
}

int[] getAvailableTypes(int clipboardType) {
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0) return new int[0];
	done = false;
	selectionValue = null; selectionTransfer = null;
	int selection = clipboardType == DND.CLIPBOARD ? clipboardAtom : primaryAtom;
	int target = targetsAtom;
	OS.XtGetSelectionValue(shellHandle, selection, target, XtSelectionCallbackCallback.getAddress(), 0, OS.CurrentTime);
	if (!done) {
		int xtContext = OS.XtDisplayToApplicationContext(xDisplay);
		int selectionTimeout = OS.XtAppGetSelectionTimeout(xtContext);
		wait(selectionTimeout);
		
	}
	if (done && selectionValue == null) {
		done = false;
		target = motifClipboardAtom;
		OS.XtGetSelectionValue(shellHandle, selection, target, XtSelectionCallbackCallback.getAddress(), 0, OS.CurrentTime);
		if (!done) {
			int xtContext = OS.XtDisplayToApplicationContext(xDisplay);
			int selectionTimeout = OS.XtAppGetSelectionTimeout(xtContext);
			wait(selectionTimeout);
			
		}
	}
	if (done && selectionValue == null) {
		done = false;
		target = atomAtom;
		OS.XtGetSelectionValue(shellHandle, selection, target, XtSelectionCallbackCallback.getAddress(), 0, OS.CurrentTime);
		if (!done) {
			int xtContext = OS.XtDisplayToApplicationContext(xDisplay);
			int selectionTimeout = OS.XtAppGetSelectionTimeout(xtContext);
			wait(selectionTimeout);
			
		}
	}
	return (!done || selectionValue == null) ? new int[0] : (int[])selectionValue;
}

void setContents(Clipboard owner, Object[] data, Transfer[] dataTypes, int clipboards) {
	if ((clipboards & DND.CLIPBOARD) != 0) {
		clipboardData = data;
		clipboardDataTypes = dataTypes;
		int XtConvertSelectionProc = XtConvertSelectionCallback.getAddress();
		int XtLoseSelectionProc = XtLoseSelectionCallback.getAddress();
		int XtSelectionDoneProc = XtSelectionDoneCallback.getAddress();
		if (!OS.XtOwnSelection(shellHandle, clipboardAtom, OS.CurrentTime, XtConvertSelectionProc, XtLoseSelectionProc, XtSelectionDoneProc)) {
			DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
		}
		activeClipboard = owner;
	}
	if ((clipboards & DND.SELECTION_CLIPBOARD) != 0) {
		primaryClipboardData = data;
		primaryClipboardDataTypes = dataTypes;
		int XtConvertSelectionProc = XtConvertSelectionCallback.getAddress();
		int XtLoseSelectionProc = XtLoseSelectionCallback.getAddress();
		int XtSelectionDoneProc = XtSelectionDoneCallback.getAddress();
		if (!OS.XtOwnSelection(shellHandle, primaryAtom, OS.CurrentTime, XtConvertSelectionProc, XtLoseSelectionProc, XtSelectionDoneProc)) {
			DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
		}
		activePrimaryClipboard = owner;
	}
}

void storePtr(int ptr, int selection, int target) {
	int index = -1;
	for (int i = 0; i < convertData.length; i++) {
		if (convertData[i][0] == 0){
			index = i;
			break;
		}
	}
	if (index == -1) {
		int[][] newConvertData = new int[convertData.length + 4][3];
		System.arraycopy(convertData, 0, newConvertData, 0, convertData.length);
		index = convertData.length;
		convertData = newConvertData;
	}
	convertData[index][0] = selection;
	convertData[index][1] = target;
	convertData[index][2] = ptr;
}

void wait(int timeout) {
	int xDisplay = OS.XtDisplay(shellHandle);
	if (xDisplay == 0) return;
	long start = System.currentTimeMillis();
	Callback checkEventCallback = new Callback(this, "checkEvent", 3); //$NON-NLS-1$
	int checkEventProc = checkEventCallback.getAddress();
	if (checkEventProc == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	int xEvent = OS.XtMalloc (XEvent.sizeof);
	display.timerExec(timeout, new Runnable() {
		public void run() {
			// timer required to force display.sleep() to wake up
			// in the case where no events are received
		}
	});
	while (!done && System.currentTimeMillis() - start <  timeout && display != null) {
		if (OS.XCheckIfEvent (xDisplay, xEvent, checkEventProc, 0) != 0) {
			OS.XtDispatchEvent(xEvent);
		} else {
			display.sleep();
		}
	}
	OS.XtFree (xEvent);
	checkEventCallback.dispose();
}
int checkEvent(int display, int event, int arg) {
	XEvent xEvent = new XEvent();
	OS.memmove (xEvent, event, XEvent.sizeof);
	switch (xEvent.type) {
		case OS.SelectionClear:
		case OS.SelectionNotify:
		case OS.SelectionRequest:
		case OS.PropertyNotify:
			return 1;
	}
	return 0;
}
int XtConvertSelection(int widget, int selection, int target, int type, int value, int length, int format) {
	int selectionAtom = 0;
	if (selection != 0) {
		int[] dest = new int[1];
		OS.memmove(dest, selection, 4);
		selectionAtom = dest[0];
	}
	if (selectionAtom == 0) return 0;
	Transfer[] types = null;
	if (selectionAtom == clipboardAtom) types = clipboardDataTypes;
	if (selectionAtom == primaryAtom) types = primaryClipboardDataTypes;
	if (types == null) return 0;
	
	int targetAtom = 0;
	if (target != 0) {
		int[] dest = new int[1];
		OS.memmove(dest, target, 4);
		targetAtom = dest[0];
	}
	if (targetAtom == atomAtom ||
		targetAtom == targetsAtom ||
		targetAtom == motifClipboardAtom) {
		int[] transferTypes = new int[] {targetAtom};
		for (int i = 0; i < types.length; i++) {
			TransferData[] subTypes = types[i].getSupportedTypes();
			int[] newtransferTypes = new int[transferTypes.length + subTypes.length];
			System.arraycopy(transferTypes, 0, newtransferTypes, 0, transferTypes.length);
			int index = transferTypes.length;
			transferTypes = newtransferTypes;
			for (int j = 0; j < subTypes.length; j++) {
				transferTypes[index++] = subTypes[j].type;
			}
		}
		int ptr = OS.XtMalloc(transferTypes.length*4);
		storePtr(ptr, selectionAtom, targetAtom);
		OS.memmove(ptr, transferTypes, transferTypes.length*4);
		OS.memmove(type, new int[]{targetAtom}, 4);
		OS.memmove(value, new int[] {ptr}, 4);
		OS.memmove(length, new int[]{transferTypes.length}, 4);
		OS.memmove(format, new int[]{32}, 4);		
		return 1;
	}
	
	TransferData tdata = new TransferData();
	tdata.type = targetAtom;
	int index = -1;
	for (int i = 0; i < types.length; i++) {
		if (types[i].isSupportedType(tdata)) {
			index = i;
			break;
		}
	}
	if (index == -1) return 0;
	Object[] data = selectionAtom == clipboardAtom ? clipboardData : primaryClipboardData;
	types[index].javaToNative(data[index], tdata);
	if (tdata.format < 8 || tdata.format % 8 != 0) {
		OS.XtFree(tdata.pValue);
		return 0;
	}
	// copy data back to value
	OS.memmove(type, new int[]{tdata.type}, 4);
	OS.memmove(value, new int[]{tdata.pValue}, 4);
	OS.memmove(length, new int[]{tdata.length}, 4);
	OS.memmove(format, new int[]{tdata.format}, 4);
	storePtr(tdata.pValue, selectionAtom, targetAtom);
	return 1;
}

int XtLoseSelection(int widget, int selection) {
	if (selection == clipboardAtom) {
		activeClipboard = null;
		clipboardData = null;
		clipboardDataTypes = null;
	}
	if (selection == primaryAtom) {
		activePrimaryClipboard = null;
		primaryClipboardData = null;
		primaryClipboardDataTypes = null;
	}
	return 0;
}

int XtSelectionCallback(int widget, int client_data, int selection, int type, int value, int length, int format) {
	done = true;
	int[] selectionType = new int[1];
	if (type != 0) OS.memmove(selectionType, type, 4);
	if (selectionType[0] == 0) return 0;
	int[] selectionLength = new int[1];
	if (length != 0) OS.memmove(selectionLength, length, 4);
	if (selectionLength[0] == 0) return 0;
	int[] selectionFormat = new int[1];
	if (format != 0) OS.memmove(selectionFormat, format, 4);
	if (selectionType[0] == atomAtom ||
		selectionType[0] == targetsAtom ||
		selectionType[0] == motifClipboardAtom) {
		int[] targets = new int[selectionLength[0]];
		OS.memmove(targets, value, selectionLength[0] * selectionFormat [0] / 8);
		selectionValue = targets;
		return 0;
	}
	if (selectionTransfer != null) {
		TransferData transferData = new TransferData();
		transferData.type = selectionType[0];
		transferData.length = selectionLength[0];
		transferData.format = selectionFormat[0];
		transferData.pValue = value;
		transferData.result = 1;
		selectionValue = selectionTransfer.nativeToJava(transferData);
	}
	return 0;
}

int XtSelectionDone(int widget, int selection, int target) {
	if (target == 0 || selection == 0) return 0;
	int[] selectionAtom = new int[1];
	OS.memmove(selectionAtom, selection, 4);
	int[] targetAtom = new int[1];
	OS.memmove(targetAtom, target, 4);
	for (int i = 0; i < convertData.length; i++) {
		if (convertData[i][0] == selectionAtom[0] && convertData[i][1] == targetAtom[0]) {
			OS.XtFree(convertData[i][2]);
			convertData[i][0] = convertData[i][1] = convertData[i][2] = 0;
			break;
		}
	}
	return 0;
}
}
