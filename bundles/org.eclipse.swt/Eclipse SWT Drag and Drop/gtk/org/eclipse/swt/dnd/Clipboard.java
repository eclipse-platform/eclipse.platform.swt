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


import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.gtk.GtkSelectionData;
import org.eclipse.swt.internal.gtk.OS;
import org.eclipse.swt.widgets.Display;

/**
 * The <code>Clipboard</code> provides a mechanism for transferring data from one
 * application to another or within an application.
 * 
 * <p>IMPORTANT: This class is <em>not</em> intended to be subclassed.</p>
 */
public class Clipboard {

	private Display display;
	
	int pGtkClipboard;
	int pGtkPrimary;

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
	pGtkClipboard = OS.gtk_clipboard_get(OS.GDK_NONE);
	byte[] buffer = Converter.wcsToMbcs(null, "PRIMARY", true);
	int primary = OS.gdk_atom_intern(buffer, false);
	pGtkPrimary = OS.gtk_clipboard_get(primary);
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
	if (pGtkClipboard == 0) return;
	pGtkClipboard = 0;
	pGtkPrimary = 0;
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
	if (dataTypes.length == 0) return;
	ClipboardProxy proxy = ClipboardProxy._getInstance(display);
	if (!proxy.setData(data, dataTypes)) {
		DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
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
	byte[] buffer = Converter.wcsToMbcs(null, "TARGETS", true);
	int typeId = OS.gdk_atom_intern(buffer, false);
	// first try the primary clipboard
	int selection_data = OS.gtk_clipboard_wait_for_contents(pGtkPrimary, typeId);
	if (selection_data == 0) {
		// try the clipboard selection second
		selection_data  = OS.gtk_clipboard_wait_for_contents(pGtkClipboard, typeId);
	}
	if (selection_data == 0) {
		return new String[0]; // No types available
	}
	GtkSelectionData gtkSelectionData = new GtkSelectionData();
	OS.memmove(gtkSelectionData, selection_data, GtkSelectionData.sizeof);
	if (gtkSelectionData.length == 0) return new String[0];
	int[] atoms = new int[gtkSelectionData.length * 8 / gtkSelectionData.format];
	OS.memmove(atoms, gtkSelectionData.data, gtkSelectionData.length);
	String[] result = new String[atoms.length];
	for (int i = 0; i < atoms.length; i++) {
		int pName = OS.gdk_atom_name(atoms[i]);
		buffer = new byte [OS.strlen(pName)];
		OS.memmove (buffer, pName, buffer.length);
		OS.g_free (pName);
		result[i] = new String (Converter.mbcsToWcs (null, buffer));
	}
	OS.gtk_selection_data_free(selection_data);
	return result;
}
}

