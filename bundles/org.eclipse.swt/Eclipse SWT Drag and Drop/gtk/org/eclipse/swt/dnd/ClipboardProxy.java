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
package org.eclipse.swt.dnd;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;

class ClipboardProxy {
	/* Data is not flushed to the clipboard immediately.
	 * This class will remember the data and provide it when requested.
	 */
	Object[] clipboardData;
	Transfer[] clipboardDataTypes;
	Object[] primaryClipboardData;
	Transfer[] primaryClipboardDataTypes;

	long clipboardOwner = GTK.gtk_window_new(0);
	Display display;
	Clipboard activeClipboard = null;
	Clipboard activePrimaryClipboard = null;
	Callback getFunc;
	Callback clearFunc;

	static String ID = "CLIPBOARD PROXY OBJECT"; //$NON-NLS-1$

static ClipboardProxy _getInstance(final Display display) {
	ClipboardProxy proxy = (ClipboardProxy) display.getData(ID);
	if (proxy != null) return proxy;
	proxy = new ClipboardProxy(display);
	display.setData(ID, proxy);
	display.addListener(SWT.Dispose, event -> {
		ClipboardProxy clipbordProxy = (ClipboardProxy)display.getData(ID);
		if (clipbordProxy == null) return;
		display.setData(ID, null);
		clipbordProxy.dispose();
	});
	return proxy;
}

ClipboardProxy(Display display) {
	this.display = display;
	getFunc = new Callback( this, "getFunc", 4); //$NON-NLS-1$
	if (getFunc.getAddress() == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
	clearFunc = new Callback( this, "clearFunc", 2); //$NON-NLS-1$
	if (clearFunc.getAddress() == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
}

void clear (Clipboard owner, int clipboards) {
	if ((clipboards & DND.CLIPBOARD) != 0 && activeClipboard == owner) {
		gtk_gdk_clipboard_clear(Clipboard.GTKCLIPBOARD);
	}
	if ((clipboards & DND.SELECTION_CLIPBOARD) != 0 && activePrimaryClipboard == owner) {
		gtk_gdk_clipboard_clear(Clipboard.GTKPRIMARYCLIPBOARD);
	}
}

void gtk_gdk_clipboard_clear(long clipboard) {
	if (GTK.GTK4) {
		GDK.gdk_clipboard_set_content(clipboard, 0);
	} else {
		GTK.gtk_clipboard_clear(clipboard);
	}
}

long clearFunc(long clipboard,long user_data_or_owner){
	if (clipboard == Clipboard.GTKCLIPBOARD) {
		activeClipboard = null;
		clipboardData = null;
		clipboardDataTypes = null;
	}
	if (clipboard == Clipboard.GTKPRIMARYCLIPBOARD) {
		activePrimaryClipboard = null;
		primaryClipboardData = null;
		primaryClipboardDataTypes = null;
	}
	return 1;
}

void dispose () {
	if (display == null) return;
	if (activeClipboard != null) {
		GTK.gtk_clipboard_store(Clipboard.GTKCLIPBOARD);
	}
	if (activePrimaryClipboard != null) {
		GTK.gtk_clipboard_store(Clipboard.GTKPRIMARYCLIPBOARD);
	}
	display = null;
	if (getFunc != null ) getFunc.dispose();
	getFunc = null;
	if (clearFunc != null) clearFunc.dispose();
	clearFunc = null;
	clipboardData = null;
	clipboardDataTypes = null;
	primaryClipboardData = null;
	primaryClipboardDataTypes = null;
	if (clipboardOwner != 0) GTK.gtk_widget_destroy (clipboardOwner);
	clipboardOwner = 0;
}

/**
 * This function provides the data to the clipboard on request.
 * When this clipboard is disposed, the data will no longer be available.
 */
long getFunc(long clipboard, long selection_data, long info, long user_data_or_owner){
	if (selection_data == 0) return 0;
	long target = GTK.gtk_selection_data_get_target(selection_data);
	TransferData tdata = new TransferData();
	tdata.type = target;
	Transfer[] types = (clipboard == Clipboard.GTKCLIPBOARD) ? clipboardDataTypes : primaryClipboardDataTypes;
	int index = -1;
	for (int i = 0; i < types.length; i++) {
		if (types[i].isSupportedType(tdata)) {
			index = i;
			break;
		}
	}
	if (index == -1) return 0;
	Object[] data = (clipboard == Clipboard.GTKCLIPBOARD) ? clipboardData : primaryClipboardData;
	types[index].javaToNative(data[index], tdata);
	if (tdata.format < 8 || tdata.format % 8 != 0) {
		return 0;
	}
	GTK.gtk_selection_data_set(selection_data, tdata.type, tdata.format, tdata.pValue, tdata.length);
	OS.g_free(tdata.pValue);
	return 1;
}

boolean setData(Clipboard owner, Object[] data, Transfer[] dataTypes, int clipboards) {
	GtkTargetEntry[] entries = new  GtkTargetEntry [0];
	long pTargetsList = 0;
	try {
		for (int i = 0; i < dataTypes.length; i++) {
			Transfer transfer = dataTypes[i];
			int[] typeIds = transfer.getTypeIds();
			String[] typeNames = transfer.getTypeNames();
			for (int j = 0; j < typeIds.length; j++) {
				GtkTargetEntry	entry = new GtkTargetEntry();
				entry.info = typeIds[j];
				byte[] buffer = Converter.wcsToMbcs(typeNames[j], true);
				long pName = OS.g_malloc(buffer.length);
				C.memmove(pName, buffer, buffer.length);
				entry.target = pName;
				GtkTargetEntry[] tmp = new GtkTargetEntry [entries.length + 1];
				System.arraycopy(entries, 0, tmp, 0, entries.length);
				tmp[entries.length] = entry;
				entries = tmp;
			}
		}

		pTargetsList = OS.g_malloc(GtkTargetEntry.sizeof * entries.length);
		int offset = 0;
		for (int i = 0; i < entries.length; i++) {
			OS.memmove(pTargetsList + offset, entries[i], GtkTargetEntry.sizeof);
			offset += GtkTargetEntry.sizeof;
		}
		if ((clipboards & DND.CLIPBOARD) != 0) {
			clipboardData = data;
			clipboardDataTypes = dataTypes;
			long getFuncProc = getFunc.getAddress();
			long clearFuncProc = clearFunc.getAddress();
			/*
			* Feature in GTK. When the contents are set again, clipboard_set_with_data()
			* invokes clearFunc and then, getFunc is not sequentially called.
			* If we clear the content before calling set_with_data(), then there is a fair
			* chance for other apps like Klipper to claim the ownership of the clipboard.
			* The fix is to make sure that the content is not cleared before the data is
			* set again. GTK does not invoke clearFunc for clipboard_set_with_owner()
			* though we set the data again. So, this API has to be used whenever we
			* are setting the contents.
			*/
			if (!GTK.gtk_clipboard_set_with_owner (Clipboard.GTKCLIPBOARD, pTargetsList, entries.length, getFuncProc, clearFuncProc, clipboardOwner)) {
				return false;
			}
			GTK.gtk_clipboard_set_can_store(Clipboard.GTKCLIPBOARD, 0, 0);
			activeClipboard = owner;
		}
		if ((clipboards & DND.SELECTION_CLIPBOARD) != 0) {
			primaryClipboardData = data;
			primaryClipboardDataTypes = dataTypes;
			long getFuncProc = getFunc.getAddress();
			long clearFuncProc = clearFunc.getAddress();
			if (!GTK.gtk_clipboard_set_with_owner (Clipboard.GTKPRIMARYCLIPBOARD, pTargetsList, entries.length, getFuncProc, clearFuncProc, clipboardOwner)) {
				return false;
			}
			GTK.gtk_clipboard_set_can_store(Clipboard.GTKPRIMARYCLIPBOARD, 0, 0);
			activePrimaryClipboard = owner;
		}
		return true;
	} finally {
		for (int i = 0; i < entries.length; i++) {
			GtkTargetEntry entry = entries[i];
			if( entry.target != 0) OS.g_free(entry.target);
		}
		if (pTargetsList != 0) OS.g_free(pTargetsList);
	}
}
}
