package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.GtkSelectionData;
import org.eclipse.swt.internal.gtk.GtkTargetEntry;
import org.eclipse.swt.internal.gtk.OS;
import org.eclipse.swt.widgets.*;

/**
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 */ 
public class Clipboard {
	
	Display display;
	Callback getFunc;
	Callback clearFunc;
	int pGtkClipboard;
	
	/* Data is not flushed to the clipboard immediately.
	 * This class will remember the data and provide it when requested. */
	Object[] data;
	Transfer[] dataTypes;

public Clipboard(Display display) {	
	checkSubclass ();
	if (display == null) {
		display = Display.getCurrent();
		if (display == null) {
			display =  Display.getDefault();
		}
	}
	if (display.getThread() != Thread.currentThread()) {
		SWT.error(SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.display = display;
	getFunc = new Callback( this, "getFunc", 4);
	clearFunc = new Callback( this, "clearFunc", 2);
	pGtkClipboard = OS.gtk_clipboard_get(OS.GDK_NONE);
}

int clearFunc(int clipboard,int user_data_or_owner){
	data = null;
	dataTypes = null;
	return 1;
}

protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = Clipboard.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}

public void dispose () {
	if (pGtkClipboard == 0) return;
	if (data != null) OS.gtk_clipboard_clear(pGtkClipboard);
	OS.g_free(pGtkClipboard);
	pGtkClipboard = 0;
	display = null;
	if (getFunc != null ) getFunc.dispose();
	getFunc = null;
	if (clearFunc != null) clearFunc.dispose();
	clearFunc = null;
}

public Object getContents(Transfer transfer) {
	if (transfer == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int selection_data = 0;
	int[] typeIds = transfer.getTypeIds();
	for (int i = 0; i < typeIds.length; i++) {
		selection_data = OS.gtk_clipboard_wait_for_contents(pGtkClipboard, typeIds[i]);
		if( selection_data != 0) break;
	};
	if (selection_data == 0) return null; // No data available for this transfer
	
	GtkSelectionData gtkSelectionData = new GtkSelectionData();
	OS.memmove(gtkSelectionData, selection_data, GtkSelectionData.sizeof);
	TransferData tdata = new TransferData();
	tdata.type = gtkSelectionData.target;
	tdata.pValue = gtkSelectionData.data;
	tdata.length = gtkSelectionData.length;
	tdata.format = gtkSelectionData.format;
	Object result = transfer.nativeToJava(tdata);
	OS.gtk_selection_data_free(selection_data);
	return result;
}

/**
 * This function provides the data to the clipboard on request.
 * When this clipboard is disposed, the data will no longer be available.
 */
int getFunc( int clipboard, int selection_data, int info, int user_data_or_owner){
	if (selection_data == 0) return 0;
	GtkSelectionData selectionData = new GtkSelectionData();
	OS.memmove(selectionData, selection_data, GtkSelectionData.sizeof);
	TransferData tdata = new TransferData();
	tdata.type = selectionData.target;
	int index = -1;
	for (int i = 0; i < dataTypes.length; i++) {
		if (dataTypes[i].isSupportedType(tdata)) {
			index = i;
			break;
		}
	}
	if (index == -1) return 0;
	dataTypes[index].javaToNative(data[index], tdata);
	OS.gtk_selection_data_set(selection_data, tdata.type, tdata.format, tdata.pValue, tdata.length);	
	return 1;
}

public void setContents(Object[] data, Transfer[] dataTypes){
	if (data == null || dataTypes == null || data.length != dataTypes.length) {
		DND.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (display.isDisposed() ) DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
	if (dataTypes.length == 0) return;	
	
	if (this.data != null) {
		OS.gtk_clipboard_clear(pGtkClipboard);
	}
		
	GtkTargetEntry[] entries = new  GtkTargetEntry [0];
	for (int i = 0; i < dataTypes.length; i++) {
		Transfer transfer = dataTypes[i];
		int[] typeIds = transfer.getTypeIds();
		String[] typeNames = transfer.getTypeNames();
		for (int j = 0; j < typeIds.length; j++) {
			GtkTargetEntry	entry = new GtkTargetEntry();						
			entry.info = typeIds[j];
			byte[] buffer = Converter.wcsToMbcs(null, typeNames[j], true);
			int pName = OS.g_malloc(buffer.length);
			OS.memmove(pName, buffer, buffer.length);
			entry.target = pName;
			GtkTargetEntry[] tmp = new GtkTargetEntry [entries.length + 1];
			System.arraycopy(entries, 0, tmp, 0, entries.length);
			tmp[entries.length] = entry;
			entries = tmp;				
		}	
	}
	
	int pTargetsList = OS.g_malloc(GtkTargetEntry.sizeof * entries.length);
	int offset = 0;
	for (int i = 0; i < entries.length; i++) {
		OS.memmove(pTargetsList + offset, entries[i], GtkTargetEntry.sizeof);
		offset += GtkTargetEntry.sizeof;
	}

	this.data = data;
	this.dataTypes = dataTypes;

	boolean result = OS.gtk_clipboard_set_with_data(pGtkClipboard, pTargetsList, entries.length, getFunc.getAddress(), clearFunc.getAddress(), 0);

	for (int i = 0; i < entries.length; i++) {
		GtkTargetEntry entry = entries[i];
		if( entry.target != 0) OS.g_free(entry.target);
	}
	if (pTargetsList != 0) OS.g_free(pTargetsList);
	
	if (!result) DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
}
/*
 * Note: getAvailableTypeNames is a tool for writing a Transfer sub-class only.  It should
 * NOT be used within an application because it provides platform specfic 
 * information.
 */
public String[] getAvailableTypeNames() {
	return new String[0];
}
}
