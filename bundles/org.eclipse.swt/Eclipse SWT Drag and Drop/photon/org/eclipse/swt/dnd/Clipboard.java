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
import org.eclipse.swt.internal.photon.*;

/**
 * The <code>Clipboard</code> provides a mechanism for transferring data from one
 * application to another or within an application.
 * 
 * <p>IMPORTANT: This class is <em>not</em> intended to be subclassed.</p>
 */
public class Clipboard {
	
	private Display display;
	private final int MAX_RETRIES = 10;

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
			display =  Display.getDefault();
		}
	}
	if (display.getThread() != Thread.currentThread()) {
		SWT.error(SWT.ERROR_THREAD_INVALID_ACCESS);
	}
	this.display = display;
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
	
	Object result = null;
	
	int ig = OS.PhInputGroup(0);
	int cbdata = OS.PhClipboardPasteStart((short)ig);
	if (cbdata == 0) return result;
	try {
		String[] names = transfer.getTypeNames();
		int[] ids = transfer.getTypeIds();
		for (int i = 0; i < names.length; i++) {
			byte[] name = Converter.wcsToMbcs(null, names[i], true);
			name [Math.min(8, name.length) - 1] = (byte)0;
			int pClipHeader = OS.PhClipboardPasteType(cbdata, name);
			if (pClipHeader != 0) {
				PhClipHeader clipHeader = new PhClipHeader();
				OS.memmove(clipHeader, pClipHeader, PhClipHeader.sizeof);
				TransferData data = new TransferData();
				data.pData = clipHeader.data;
				data.length = clipHeader.length;
				data.type = ids[i];
				result = transfer.nativeToJava(data);
				break;
			}
		}
	} finally {
		OS.PhClipboardPasteFinish(cbdata);
	}
	
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
	
	PhClipHeader[] clips = new PhClipHeader[0];
	int count = 0;
	for (int i = 0; i < dataTypes.length; i++) {
		String[] names = dataTypes[i].getTypeNames();
		int[] ids = dataTypes[i].getTypeIds();
		for (int j = 0; j < names.length; j++) {
			TransferData transferData = new TransferData();
			transferData.type = ids[j];
			dataTypes[i].javaToNative(data[i], transferData);
			PhClipHeader clip = new PhClipHeader();
			clip.data = transferData.pData;
			clip.length = (short)transferData.length;
			byte[] temp = Converter.wcsToMbcs(null, names[j], false);
			byte[] type = new byte[8];
			System.arraycopy(temp, 0, type, 0, Math.min(type.length - 1, temp.length));
			clip.type_0 = type[0];
			clip.type_1 = type[1];
			clip.type_2 = type[2];
			clip.type_3 = type[3];
			clip.type_4 = type[4];
			clip.type_5 = type[5];
			clip.type_6 = type[6];
			clip.type_7 = type[7];
			PhClipHeader[] newClips = new PhClipHeader[count + 1];
			System.arraycopy(clips, 0, newClips, 0, clips.length);
			clips = newClips;
			clips[count++] = clip;
		}
	}
	
	if (count == 0) return;

	// Copy data to clipboard
	byte[] buffer = new byte[count * PhClipHeader.sizeof];
	byte[] temp = new byte[PhClipHeader.sizeof];
	for (int i = 0; i < count; i++) {
		OS.memmove(temp, clips[i], PhClipHeader.sizeof);
		System.arraycopy(temp, 0, buffer, i * PhClipHeader.sizeof, temp.length);
	}	
	int ig = OS.PhInputGroup(0);
	if (OS.PhClipboardCopy((short)ig, count, buffer) != 0) {
		DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
	}
	
	// Free allocated data
	for (int i = 0; i < count; i++) {
		OS.free(clips[i].data);
	}
}
/**
 * Returns a platform specific list of the data types currently available on the 
 * system clipboard.
 * 
 * <p>Note: <code>getAvailableTypeNames</code> is a utility for writing a Transfer 
 * sub-class.  It should NOT be used within an application because it provides 
 * platform specific information.</p>
 * 
 * @returns a platform specific list of the data types currently available on the 
 * system clipboard
 */
public String[] getAvailableTypeNames() {
	if (display == null) DND.error(SWT.ERROR_WIDGET_DISPOSED);
	if (display.isDisposed()) DND.error(SWT.ERROR_DEVICE_DISPOSED);
	
	String[] types = new String[0];
	int ig = OS.PhInputGroup(0);
	int cbdata = OS.PhClipboardPasteStart((short)ig);
	if (cbdata == 0) return types;
	try {
		int pClipHeader = 0;
		int n = 0;
		while ((pClipHeader = OS.PhClipboardPasteTypeN(cbdata, n++)) != 0) {
			PhClipHeader clipHeader = new PhClipHeader();
			OS.memmove(clipHeader, pClipHeader, PhClipHeader.sizeof);
			byte[] buffer = new byte[8];
			buffer[0] = clipHeader.type_0;
			buffer[1] = clipHeader.type_1;
			buffer[2] = clipHeader.type_2;
			buffer[3] = clipHeader.type_3;
			buffer[4] = clipHeader.type_4;
			buffer[5] = clipHeader.type_5;
			buffer[6] = clipHeader.type_6;
			buffer[7] = clipHeader.type_7;
			char [] unicode = Converter.mbcsToWcs (null, buffer);
			
			String[] newTypes = new String[types.length + 1];
			System.arraycopy(types, 0, newTypes, 0, types.length);
			newTypes[types.length] = new String (unicode).trim();
			types = newTypes;
		}
	} finally {
		OS.PhClipboardPasteFinish(cbdata);
	}
	return types;
}
}
