package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.widgets.*;

/**
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 */ 
public class Clipboard {
	
	private Display display;
	private final int MAX_RETRIES = 10;
	private int shellHandle;


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
	
	int widgetClass = OS.TopLevelShellWidgetClass ();
	shellHandle = OS.XtAppCreateShell (null, null, widgetClass, display.xDisplay, null, 0);
	OS.XtSetMappedWhenManaged (shellHandle, false);
	OS.XtRealizeWidget (shellHandle);
}
protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = Clipboard.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}
public void dispose () {
	if (shellHandle != 0) OS.XtDestroyWidget (shellHandle);
	shellHandle = 0;
	display = null;
}
public Object getContents(Transfer transfer) {
	if (display.isDisposed() ) return null;
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0) return null;
	int xWindow = OS.XtWindow (shellHandle);
	if (xWindow == 0) return null;
	
	// Open clipboard for retrieval
	int retries = 0;
	int status = OS.XmClipboardStartRetrieve(xDisplay, xWindow, OS.XtLastTimestampProcessed(xDisplay));
	while ( status == OS.XmClipboardLocked && retries < MAX_RETRIES) {
		retries ++;
		status = OS.XmClipboardStartRetrieve(xDisplay, xWindow, OS.XtLastTimestampProcessed(xDisplay));
	};
	if (status != OS.XmClipboardSuccess) return null;
	
	// Does Clipboard have data in required format?
	byte[] type = null;
	int[] length = new int[1];
	String[] supportedTypes = transfer.getTypeNames();
	for (int i = 0; i < supportedTypes.length; i++) {
		byte[] bName = Converter.wcsToMbcs(null, supportedTypes[i], true);
		if (OS.XmClipboardInquireLength(xDisplay, xWindow, bName, length) == 1 /*OS.XmClipboardSuccess*/ ) {
			type = bName;
			break;
		}
	}
	
	// Retrieve data from Clipboard
	byte[] data = null;
	if (type != null) {
		data = new byte[length[0]];
		status = OS.XmClipboardRetrieve(xDisplay, xWindow, type, data, length[0], new int[1], new int[1]);
		if (status != OS.XmClipboardSuccess) {
			data = null;
		}
	}
	
	// Close Clipboard
	status = OS.XmClipboardEndRetrieve(xDisplay, xWindow);
	
	if (data == null) return null;

	// Pass data to transfer agent for conversion to a Java Object
	// Memory is allocated here to emulate the way Drag and Drop transfers data.
	TransferData transferData = new TransferData();
	/* Use the character encoding for the default locale */
	transferData.type = OS.XmInternAtom (xDisplay, type, true);
	transferData.pValue = OS.XtMalloc(data.length);
	OS.memmove(transferData.pValue, data, data.length);
	transferData.length = data.length;
	transferData.format = 8;
	transferData.result = 1;
	
	Object result = transfer.nativeToJava(transferData);
	
	// Clean up allocated memory
	OS.XtFree(transferData.pValue);
	
	return result;
}
public void setContents(Object[] data, Transfer[] dataTypes){
	
	if (data == null || dataTypes == null || data.length != dataTypes.length) {
		DND.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	
	if (display.isDisposed() )
		DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0)
		DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
	int xWindow = OS.XtWindow (shellHandle);
	if (xWindow == 0)
		DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
		
	// Open clipboard for setting
	int[] item_id = new int[1];			
	int retries = 0;
	int status = OS.XmClipboardStartCopy(xDisplay, xWindow, 0, OS.XtLastTimestampProcessed(xDisplay), shellHandle, 0, item_id);
	while ( status == OS.XmClipboardLocked && retries < MAX_RETRIES) {
		retries ++;
		status = OS.XmClipboardStartCopy(xDisplay, xWindow, 0, OS.XtLastTimestampProcessed(xDisplay), shellHandle, 0, item_id);
	};
	if (status != OS.XmClipboardSuccess)
		DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
	
	// copy data directly over to System clipboard (not deferred)
	for (int i = 0; i < dataTypes.length; i++) {
		int[] ids = dataTypes[i].getTypeIds();
		String[] names = dataTypes[i].getTypeNames();
		for (int j = 0; j < names.length; j++) {
			TransferData transferData = new TransferData();
			/* Use the character encoding for the default locale */
			transferData.type = ids[j];
			dataTypes[i].javaToNative(data[i], transferData);
			status = OS.XmClipboardFail;
			if (transferData.result == 1) {
				if (transferData.format == 8){
					byte[] buffer = new byte[transferData.length];
					OS.memmove(buffer, transferData.pValue, transferData.length);
					byte[] bName = Converter.wcsToMbcs(null, names[j], true);
					status = OS.XmClipboardCopy(xDisplay, xWindow, item_id[0], bName, buffer, transferData.length, 0, null);
				}
			}
			// Clean up allocated memory
			if (transferData.pValue != 0) {
				OS.XtFree(transferData.pValue);
			}
		}
	}
	
	// close clipboard  for setting
	OS.XmClipboardEndCopy(xDisplay, xWindow, item_id[0]);
	
	if (status != OS.XmClipboardSuccess)
		DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
	
}
/*
 * Note: getAvailableTypeNames is a tool for writing a Transfer sub-class only.  It should
 * NOT be used within an application because it provides platform specfic 
 * information.
 */
public String[] getAvailableTypeNames() {
	int[] count = new int[1];
	int[] max_length = new int[1];
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0)
		DND.error(SWT.ERROR_UNSPECIFIED);
	int xWindow = OS.XtWindow (shellHandle);
	if (xWindow == 0)
		DND.error(SWT.ERROR_UNSPECIFIED);
	if (OS.XmClipboardInquireCount(xDisplay, xWindow, count, max_length) != OS.XmClipboardSuccess)
		DND.error(SWT.ERROR_UNSPECIFIED);
	String[] types = new String[count[0]];
	for (int i = 0; i < count[0]; i++) {
		byte[] buffer = new byte[max_length[0]];
		int[] copied_length = new int[1];
		int rc = OS.XmClipboardInquireFormat(xDisplay, xWindow, i + 1, buffer, buffer.length, copied_length);
		if (rc == OS.XmClipboardNoData){
			types[i] = "";
			continue;
		}
		if (rc != OS.XmClipboardSuccess)
			DND.error(SWT.ERROR_UNSPECIFIED);
		byte[] buffer2 = new byte[copied_length[0]];
		System.arraycopy(buffer, 0, buffer2, 0, copied_length[0]);
		char [] unicode = Converter.mbcsToWcs (null, buffer2);
		types[i] = new String (unicode);
	}
	return types;
}
}
