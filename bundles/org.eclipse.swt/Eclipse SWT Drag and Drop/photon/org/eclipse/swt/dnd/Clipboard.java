package org.eclipse.swt.dnd;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.widgets.*;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

/*
 *
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 *
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
	
//	int widgetClass = OS.TopLevelShellWidgetClass ();
//	shellHandle = OS.XtAppCreateShell (null, null, widgetClass, display.xDisplay, null, 0);
//	OS.XtSetMappedWhenManaged (shellHandle, false);
//	OS.XtRealizeWidget (shellHandle);
}
protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = Clipboard.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}
public void dispose () {
//	if (shellHandle != 0) OS.XtDestroyWidget (shellHandle);
//	shellHandle = 0;
	display = null;
}
public Object getContents(Transfer transfer) {
	if (display.isDisposed() ) return null;
	return null;
}
public void setContents(Object[] data, Transfer[] transferAgents){
}
/*
 * Note: getAvailableTypeNames is a tool for writing a Transfer sub-class only.  It should
 * NOT be used within an application because it provides platform specfic 
 * information.
 */
public String[] getAvailableTypeNames() {
	int[] count = new int[1];
	int[] max_length = new int[1];
//	int xDisplay = OS.XtDisplay (shellHandle);
//	if (xDisplay == 0)
//		DND.error(SWT.ERROR_UNSPECIFIED);
//	int xWindow = OS.XtWindow (shellHandle);
//	if (xWindow == 0)
//		DND.error(SWT.ERROR_UNSPECIFIED);
//	if (OS.XmClipboardInquireCount(xDisplay, xWindow, count, max_length) != OS.XmClipboardSuccess)
//		DND.error(SWT.ERROR_UNSPECIFIED);
	String[] types = new String[count[0]];
//	for (int i = 0; i < count[0]; i++) {
//		byte[] buffer = new byte[max_length[0]];
//		int[] copied_length = new int[1];
//		int rc = OS.XmClipboardInquireFormat(xDisplay, xWindow, i + 1, buffer, buffer.length, copied_length);
//		if (rc == OS.XmClipboardNoData){
//			types[i] = "";
//			continue;
//		}
//		if (rc != OS.XmClipboardSuccess)
//			DND.error(SWT.ERROR_UNSPECIFIED);
//		byte[] buffer2 = new byte[copied_length[0]];
//		System.arraycopy(buffer, 0, buffer2, 0, copied_length[0]);
//		types[i] = new String(buffer2);
//	}
	return types;
}
}
