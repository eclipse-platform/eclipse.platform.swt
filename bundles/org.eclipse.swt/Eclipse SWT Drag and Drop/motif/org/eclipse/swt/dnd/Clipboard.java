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
		DND.error(SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.display = display;
	
	int widgetClass = OS.topLevelShellWidgetClass ();
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
 * Throws an <code>SWTException</code> if the receiver can not
 * be accessed by the caller. This may include both checks on
 * the state of the receiver and more generally on the entire
 * execution context. This method <em>should</em> be called by
 * widget implementors to enforce the standard SWT invariants.
 * <p>
 * Currently, it is an error to invoke any method (other than
 * <code>isDisposed()</code>) on a widget that has had its 
 * <code>dispose()</code> method called. It is also an error
 * to call widget methods from any thread that is different
 * from the thread that created the widget.
 * </p><p>
 * In future releases of SWT, there may be more or fewer error
 * checks and exceptions may be thrown for different reasons.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
protected void checkWidget () {
	Display display = this.display;
	if (display == null) DND.error (SWT.ERROR_WIDGET_DISPOSED);
	if (display.getThread() != Thread.currentThread ()) DND.error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (display.isDisposed()) DND.error(SWT.ERROR_WIDGET_DISPOSED);
}

/**
 * Disposes of the operating system resources associated with the clipboard. 
 * The data will still be available on the system clipboard after the dispose 
 * method is called.  
 * 
 * <p>NOTE: On some platforms the data will not be available once the application
 * has exited or the display has been disposed.</p>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 */
public void dispose () {
	if (isDisposed()) return;
	if (display.getThread() != Thread.currentThread()) DND.error(SWT.ERROR_THREAD_INVALID_ACCESS);
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
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if transfer is null</li>
 * </ul>
 */
public Object getContents(Transfer transfer) {
	checkWidget();
	if (transfer == null) DND.error(SWT.ERROR_NULL_ARGUMENT);
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0) return null;
	int xWindow = OS.XtWindow (shellHandle);
	if (xWindow == 0) return null;
	
	// Open clipboard for retrieval
	if (OS.XmClipboardStartRetrieve(xDisplay, xWindow, OS.XtLastTimestampProcessed(xDisplay)) != OS.XmClipboardSuccess) return null;
	byte[] data = null;
	byte[] type = null;
	try {
		// Does Clipboard have data in required format?
		int[] length = new int[1];
		String[] supportedTypes = transfer.getTypeNames();
		for (int i = 0; i < supportedTypes.length; i++) {
			byte[] bName = Converter.wcsToMbcs(null, supportedTypes[i], true);
			if (OS.XmClipboardInquireLength(xDisplay, xWindow, bName, length) == OS.XmClipboardSuccess) {
				type = bName;
				break;
			}
		}
		// Retrieve data from Clipboard
		if (type == null) return null;
		data = new byte[length[0]];
		if (OS.XmClipboardRetrieve(xDisplay, xWindow, type, data, length[0], new int[1], new int[1]) != OS.XmClipboardSuccess) {
			return null;
		}
	} finally {
		// Close Clipboard
		OS.XmClipboardEndRetrieve(xDisplay, xWindow);
	}
	
	// Pass data to transfer agent for conversion to a Java Object
	// Memory is allocated here to emulate the way Drag and Drop transfers data.
	int pValue = OS.XtMalloc(data.length);
	if (pValue == 0) return null;
	try {
		OS.memmove(pValue, data, data.length);
		TransferData transferData = new TransferData();
		transferData.type = OS.XmInternAtom (xDisplay, type, true);
		transferData.length = data.length;
		transferData.format = 8;
		transferData.pValue = pValue;
		transferData.result = 1;
		return transfer.nativeToJava(transferData);
	} finally {
		// Clean up allocated memory
		OS.XtFree(pValue);
	}
}

/**
 * Returns <code>true</code> if the clipboard has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the clipboard.
 * When a clipboard has been disposed, it is an error to
 * invoke any other method using the clipboard.
 * </p>
 *
 * @return <code>true</code> when the widget is disposed and <code>false</code> otherwise
 * 
 * @since 3.0
 */
public boolean isDisposed () {
	return (display == null);
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
 *    <li>ERROR_INVALID_ARGUMENT - if data is null or datatypes is null 
 *          or the length of data is not the same as the length of dataTypes</li>
 * </ul>
 *  @exception SWTError <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 *    <li>ERROR_CANNOT_SET_CLIPBOARD - if the clipboard is locked or otherwise unavailable</li>
  * </ul>
 */
public void setContents(Object[] data, Transfer[] dataTypes) {
	checkWidget();
	if (data == null || dataTypes == null || data.length != dataTypes.length) {
		DND.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0) DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
	int xWindow = OS.XtWindow (shellHandle);
	if (xWindow == 0) DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);	
	// Open clipboard for setting
	int[] item_id = new int[1];			
	if (OS.XmClipboardStartCopy(xDisplay, xWindow, 0, OS.XtLastTimestampProcessed(xDisplay), shellHandle, 0, item_id) != OS.XmClipboardSuccess){
		DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
	}
	try {
		// copy data directly over to System clipboard (not deferred)
		for (int i = 0; i < dataTypes.length; i++) {
			int[] ids = dataTypes[i].getTypeIds();
			String[] names = dataTypes[i].getTypeNames();
			for (int j = 0; j < names.length; j++) {
				TransferData transferData = new TransferData();
				transferData.type = ids[j];
				dataTypes[i].javaToNative(data[i], transferData);
				if (transferData.result != 1) DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
				try {
					if (transferData.format != 8) DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
					byte[] buffer = new byte[transferData.length];
					OS.memmove(buffer, transferData.pValue, transferData.length);
					byte[] bName = Converter.wcsToMbcs(null, names[j], true);
					if (OS.XmClipboardCopy(xDisplay, xWindow, item_id[0], bName, buffer, transferData.length, 0, null) != OS.XmClipboardSuccess) {
						DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
					}
				} finally {
					OS.XtFree(transferData.pValue);
				}
			}
		}
	} finally {
		// close clipboard  for setting
		OS.XmClipboardEndCopy(xDisplay, xWindow, item_id[0]);
	}
}

/**
 * Returns an array of the data types currently available on the system clipboard. Use
 * with Transfer.isSupportedType.
 *
 * @return array of TransferData
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Transfer#isSupportedType
 * 
 * @since 3.0
 */
public TransferData[] getAvailableTypes() {
	checkWidget();
	int xDisplay = OS.XtDisplay(shellHandle);
	if (xDisplay == 0) return new TransferData[0];
	byte[][] types = _getAvailableTypes();
	TransferData[] result = new TransferData[types.length];
	for (int i = 0; i < types.length; i++) {
		int atom = OS.XmInternAtom(xDisplay, types[i], true);
		result[i] = new TransferData();
		result[i].type = atom;
	}
	return result;
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
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String[] getAvailableTypeNames() {
	checkWidget();
	byte[][] types = _getAvailableTypes();
	String[] names = new String[types.length];
	for (int i = 0; i < names.length; i++) {
		/* Use the character encoding for the default locale */
		names[i] = new String(Converter.mbcsToWcs(null, types[i]));
	}
	return names;
}

private byte[][] _getAvailableTypes() {
	byte[][] types = new byte[0][];
	int[] count = new int[1];
	int[] max_length = new int[1];
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0) return types;
	int xWindow = OS.XtWindow (shellHandle);
	if (xWindow == 0) return types;
	if (OS.XmClipboardInquireCount(xDisplay, xWindow, count, max_length) == 0) {
		return types;
	}
	if (count[0] == 0) return types;
	types = new byte[count[0]][];
	int index = -1;
	for (int i = 0; i < count[0]; i++) {
		byte[] buffer = new byte[max_length[0]];
		int[] copied_length = new int[1];
		int rc = OS.XmClipboardInquireFormat(xDisplay, xWindow, i + 1, buffer, buffer.length, copied_length);
		if (rc != OS.XmClipboardSuccess) continue;
		index++;
		byte[] buffer2 = new byte[copied_length[0]];
		System.arraycopy(buffer, 0, buffer2, 0, copied_length[0]);
		types[index] = buffer2;
	}
	if (index == -1) {
		types = new byte[0][0];
	} else if (index + 1 < types.length) {
		byte[][] newTypes = new byte[index + 1][];
		System.arraycopy(types, 0, newTypes, 0, index + 1);
		types = newTypes;
	}
	return types;
}
}
