/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;;

/**
 * The <code>Clipboard</code> provides a mechanism for transferring data from one
 * application to another or within an application.
 * 
 * <p>IMPORTANT: This class is <em>not</em> intended to be subclassed.</p>
 */
public class Clipboard {

	private Display display;
	private final int MAX_RETRIES = 10;
	private int shellHandle;

/**
 * Constructs a new instance of this class.  Creating an instance of a Clipboard
 * may cause system resources to be allocated depending on the platform.  It is therefore
 * mandatory that the Clipboard instance be disposed when no longer required.
 *
 * @param display the display on which to allocate the clipboard
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see Clipboard#dispose
 * @see Clipboard#checkSubclass
 */
public Clipboard(Display display) {	
	checkSubclass ();
	if (display == null) {
		display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
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

/**
 * Checks that this class can be subclassed.
 * <p>
 * The SWT class library is intended to be subclassed 
 * only at specific, controlled points. This method enforces this
 * rule unless it is overridden.
 * </p><p>
 * <em>IMPORTANT:</em> By providing an implementation of this
 * method that allows a subclass of a class which does not 
 * normally allow subclassing to be created, the implementer
 * agrees to be fully responsible for the fact that any such
 * subclass will likely fail between SWT releases and will be
 * strongly platform specific. No support is provided for
 * user-written classes which are implemented in this fashion.
 * </p><p>
 * The ability to subclass outside of the allowed SWT classes
 * is intended purely to enable those not on the SWT development
 * team to implement patches in order to get around specific
 * limitations in advance of when those limitations can be
 * addressed by the team. Subclassing should not be attempted
 * without an intimate and detailed understanding of the hierarchy.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
protected void checkSubclass () {
	String name = getClass().getName ();
	String validName = Clipboard.class.getName();
	if (!validName.equals(name)) {
		DND.error (SWT.ERROR_INVALID_SUBCLASS);
	}
}

/**
 * Disposes of the operating system resources associated with the clipboard. 
 * The data will still be available on the system clipboard after the dispose 
 * method is called.  
 * 
 * <p>NOTE: On some platforms the data will not be available once the application
 * has exited or the display has been disposed.</p>
 */
public void dispose () {
	if (shellHandle != 0) OS.XtDestroyWidget (shellHandle);
	shellHandle = 0;
	display = null;
}

/**
 * Retrieve the data of the specified type currently available on the system clipboard.  Refer to the 
 * specific subclass of <code>Tramsfer</code> to determine the type of object returned.
 * 
 * <p>The following snippet shows text and RTF text being retrieved from the clipboard:</p>
 * 
 *    <code><pre>
 *    Clipboard clipboard = new Clipboard(display);
 *    TextTransfer textTransfer = TextTransfer.getInstance();
 *    String textData = (String)clipboard.getContents(textTransfer);
 *    if (textData != null) System.out.println("Text is "+textData);
 *    RTFTransfer rtfTransfer = RTFTransfer.getInstance();
 *    String rtfData = (String)clipboard.getContents(rtfTransfer);
 *    if (rtfData != null) System.out.println("RTF Text is "+rtfData);
 *    clipboard.dispose();
 *    </code></pre>
 * 
 * @see Transfer
 * 
 * @param transfer the transfer agent for the type of data being requested
 * 
 * @return the data obtained from the clipboard or null if no data of this type is available
 */
public Object getContents(Transfer transfer) {
	if (display == null) DND.error(SWT.ERROR_WIDGET_DISPOSED);
	if (display.isDisposed()) DND.error(SWT.ERROR_DEVICE_DISPOSED);
	if (transfer == null) DND.error(SWT.ERROR_NULL_ARGUMENT);
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
/**
 * Place data of the specified type on the system clipboard.  More than one type of
 * data can be placed on the system clipboard at the same time.  Setting the data 
 * clears any previous data of the same type from the system clipboard and also
 * clears data of any other type currently on the system clipboard.
 * 
 * <p>NOTE: On some platforms, the data is immediately copied to the system
 * clipboard but on other platforms it is provided upon request.  As a result, if the 
 * application modifes the data object it has set on the clipboard, that modification 
 * may or may not be available when the data is subsequently requested.</p>
 *
 * <p>The following snippet shows text and RTF text being set on the clipboard:</p>
 * 
 * <code><pre>
 * 	Clipboard clipboard = new Clipboard(display);
 *		String textData = "Hello World";
 *		String rtfData = "{\\rtf1\\b\\i Hello World}";
 *		TextTransfer textTransfer = TextTransfer.getInstance();
 *		RTFTransfer rtfTransfer = RTFTransfer.getInstance();
 *		clipboard.setContents(new Object[]{textData, rtfData}, new Transfer[]{textTransfer, rtfTransfer});
 *		clipboard.dispose();
 * </code></pre>
 *
 * @param data the data to be set in the clipboard
 * @param dataTypes the transfer agents that will convert the data to its platform 
 * specific format; each entry in the data array must have a corresponding dataType
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if data is null or datatypes is null 
 *          or the length of data is not the same as the length of dataTypes</li>
 * </ul>
 *  @exception SWTError <ul>
 *    <li>ERROR_CANNOT_SET_CLIPBOARD - if the clipboard is locked or 
 *         otherwise unavailable</li>
 * </ul>
 */
public void setContents(Object[] data, Transfer[] dataTypes) {
	if (display == null) DND.error(SWT.ERROR_WIDGET_DISPOSED);
	if (display.isDisposed()) DND.error(SWT.ERROR_DEVICE_DISPOSED);
	if (data == null || dataTypes == null || data.length != dataTypes.length) {
		DND.error(SWT.ERROR_INVALID_ARGUMENT);
	}

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
/**
 * Returns a platform specific list of the data types currently available on the 
 * system clipboard.
 * 
 * <p>Note: <code>getAvailableTypeNames</code> is a utility for writing a Transfer 
 * sub-class.  It should NOT be used within an application because it provides 
 * platform specific information.</p>
 * 
 * @return a platform specific list of the data types currently available on the 
 * system clipboard
 */
public String[] getAvailableTypeNames() {
	if (display == null) DND.error(SWT.ERROR_WIDGET_DISPOSED);
	if (display.isDisposed()) DND.error(SWT.ERROR_DEVICE_DISPOSED);
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
