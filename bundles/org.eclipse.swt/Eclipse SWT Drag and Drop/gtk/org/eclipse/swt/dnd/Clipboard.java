package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.gtk.GtkSelectionData;
import org.eclipse.swt.internal.gtk.OS;
import org.eclipse.swt.widgets.Display;

/**
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 */ 
public class Clipboard {
	
	Display display;
	int pGtkClipboard;
	int pGtkPrimary;

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
	pGtkClipboard = OS.gtk_clipboard_get(OS.GDK_NONE);
	byte[] buffer = Converter.wcsToMbcs(null, "PRIMARY", true);
	int primary = OS.gdk_atom_intern(buffer, false);
	pGtkPrimary = OS.gtk_clipboard_get(primary);
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
	pGtkClipboard = 0;
	pGtkPrimary = 0;
	display = null;
}

public Object getContents(Transfer transfer) {
	if (transfer == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int selection_data = 0;
	int[] typeIds = transfer.getTypeIds();
	for (int i = 0; i < typeIds.length; i++) {
		// try the primary selection first
		selection_data = OS.gtk_clipboard_wait_for_contents(pGtkPrimary, typeIds[i]);
		if( selection_data != 0) break;
	};
	if (selection_data == 0) {
		// try the clipboard selection second
		for (int i = 0; i < typeIds.length; i++) {
			selection_data = OS.gtk_clipboard_wait_for_contents(pGtkClipboard, typeIds[i]);
			if( selection_data != 0) break;
		};
	}
	if (selection_data == 0) {
		return null; // No data available for this transfer
	}
	
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

public void setContents(Object[] data, Transfer[] dataTypes){
	if (data == null || dataTypes == null || data.length != dataTypes.length) {
		DND.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (display.isDisposed() ) DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
	if (dataTypes.length == 0) return;
	ClipboardProxy proxy = ClipboardProxy._getInstance(display);
	if (!proxy.setData(data, dataTypes)) {
		DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
	}
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

