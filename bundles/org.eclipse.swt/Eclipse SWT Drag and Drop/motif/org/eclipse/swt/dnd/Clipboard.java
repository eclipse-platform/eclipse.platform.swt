/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
import org.eclipse.swt.internal.motif.*;

/**
 * The <code>Clipboard</code> provides a mechanism for transferring data from one
 * application to another or within an application.
 * 
 * <p>IMPORTANT: This class is <em>not</em> intended to be subclassed.</p>
 */
public class Clipboard {

	Display display;
	int shellHandle;
	int clipboardAtom, primaryAtom, targetsAtom;
	
	static int[][] convertData = new int[10][3];
	static Clipboard activeClipboard = null;
	static Clipboard activePrimaryClipboard = null;
	static Object[] ClipboardData;
	static Transfer[] ClipboardDataTypes;
	static Object[] PrimaryClipboardData;
	static Transfer[] PrimaryClipboardDataTypes;
	
	static boolean done = false;
	static Object selectionValue;
	static Transfer selectionTransfer;
	
	static byte [] CLIPBOARD = Converter.wcsToMbcs (null, "CLIPBOARD", true);
	static byte [] PRIMARY = Converter.wcsToMbcs (null, "PRIMARY", true);
	static byte [] TARGETS = Converter.wcsToMbcs (null, "TARGETS", true);
	static Callback XtConvertSelectionCallback;
	static Callback XtLoseSelectionCallback;
	static Callback XtSelectionDoneCallback;
	static Callback XtSelectionCallbackCallback;
	
	static {
		XtConvertSelectionCallback = new Callback(Clipboard.class, "XtConvertSelection", 7); //$NON-NLS-1$
		XtLoseSelectionCallback = new Callback(Clipboard.class, "XtLoseSelection", 2); //$NON-NLS-1$
		XtSelectionDoneCallback = new Callback(Clipboard.class, "XtSelectionDone", 3); //$NON-NLS-1$
		XtSelectionCallbackCallback = new Callback(Clipboard.class, "XtSelectionCallback", 7); //$NON-NLS-1$
	}

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
	int xDisplay = OS.XtDisplay(shellHandle);
	clipboardAtom = OS.XmInternAtom(xDisplay, CLIPBOARD, false);
	primaryAtom = OS.XmInternAtom(xDisplay, PRIMARY, false);
	targetsAtom = OS.XmInternAtom(xDisplay, TARGETS, false);
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
 * If this clipboard is currently the owner of the data on the system clipboard,
 * clear the contents.  If this clipboard is not the owner, then nothing is done.
 * Note that there are clipboard assistant applications that take ownership of 
 * data or make copies of data when it is placed on the clipboard.  In these 
 * cases, it may not be possible to clear the clipboard.
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public void clearContents() {
	clearContents(DND.CLIPBOARD);
}

/**
 * If this clipboard is currently the owner of the data on the specified 
 * clipboard, clear the contents.  If this clipboard is not the owner, then 
 * nothing is done.
 * 
 * <p>Note that there are clipboard assistant applications that take ownership
 * of data or make copies of data when it is placed on the clipboard.  In these 
 * cases, it may not be possible to clear the clipboard.</p>
 * 
 * <p>The clipboards value is either one of the clipboard constants defined in
 * class <code>DND</code>, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>DND</code> clipboard constants.</p>
 * 
 * @param clipboards to be cleared
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see DND#CLIPBOARD
 * @see DND#SELECTION_CLIPBOARD
 * 
 * @since 3.1
 */
public void clearContents(int clipboards) {
	checkWidget();
	int xDisplay = OS.XtDisplay(shellHandle);
	if (xDisplay == 0) return;
	if ((clipboards & DND.CLIPBOARD) != 0 && activeClipboard == this) {
		OS.XtDisownSelection(shellHandle, clipboardAtom, OS.CurrentTime);
	}
	if ((clipboards & DND.SELECTION_CLIPBOARD) != 0 && activePrimaryClipboard == this) {
		OS.XtDisownSelection(shellHandle, primaryAtom, OS.CurrentTime);
	}
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
	if (shellHandle != 0) {
		int xDisplay = OS.XtDisplay(shellHandle);
		if (xDisplay != 0) {
			if (activeClipboard != null) {
				OS.XtDisownSelection(shellHandle, clipboardAtom, OS.CurrentTime);
			}
			if (activePrimaryClipboard != null) {
				OS.XtDisownSelection(shellHandle, primaryAtom, OS.CurrentTime);
			}
		}
		OS.XtDestroyWidget (shellHandle);
		shellHandle = 0;
	}
	display = null;
}

/**
 * Retrieve the data of the specified type currently available on the system 
 * clipboard.  Refer to the specific subclass of <code>Transfer</code> to 
 * determine the type of object returned.
 * 
 * <p>The following snippet shows text and RTF text being retrieved from the 
 * clipboard:</p>
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
 * @param transfer the transfer agent for the type of data being requested
 * @return the data obtained from the clipboard or null if no data of this type is available
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if transfer is null</li>
 * </ul>
 * 
 * @see Transfer
 */
public Object getContents(Transfer transfer) {
	return getContents(transfer, DND.CLIPBOARD);
}

/**
 * Retrieve the data of the specified type currently available on the specified 
 * clipboard.  Refer to the specific subclass of <code>Transfer</code> to 
 * determine the type of object returned.
 * 
 * <p>The following snippet shows text and RTF text being retrieved from the 
 * clipboard:</p>
 * 
 *    <code><pre>
 *    Clipboard clipboard = new Clipboard(display);
 *    TextTransfer textTransfer = TextTransfer.getInstance();
 *    String textData = (String)clipboard.getContents(textTransfer);
 *    if (textData != null) System.out.println("Text is "+textData);
 *    RTFTransfer rtfTransfer = RTFTransfer.getInstance();
 *    String rtfData = (String)clipboard.getContents(rtfTransfer, DND.CLIPBOARD);
 *    if (rtfData != null) System.out.println("RTF Text is "+rtfData);
 *    clipboard.dispose();
 *    </code></pre>
 * 
 * <p>The clipboards value is either one of the clipboard constants defined in
 * class <code>DND</code>, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>DND</code> clipboard constants.</p>
 * 
 * @param transfer the transfer agent for the type of data being requested
 * @param clipboards on which to look for data
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
 * 
 * @see Transfer
 * @see DND#CLIPBOARD
 * @see DND#SELECTION_CLIPBOARD
 * 
 * @since 3.1
 */
public Object getContents(Transfer transfer, int clipboards) {
	checkWidget();
	if (transfer == null) DND.error(SWT.ERROR_NULL_ARGUMENT);
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0) return null;
	Object result = null;
	if ((clipboards & DND.CLIPBOARD) != 0) {
		 result = getContents(xDisplay, transfer, clipboardAtom);
	}
	if (result != null) return result;
	if ((clipboards & DND.SELECTION_CLIPBOARD) != 0) {
		result = getContents(xDisplay, transfer, primaryAtom);
	}
	return result;
}
	
Object getContents(int xDisplay, Transfer transfer, int selection) {
	int[] types = getAvailableTypes(xDisplay, selection);
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
	OS.XtGetSelectionValue(shellHandle, selection, types[index], XtSelectionCallbackCallback.getAddress(), 0, OS.CurrentTime);
	if (!done) {
		int xtContext = OS.XtDisplayToApplicationContext(xDisplay);
		int selectionTimeout = OS.XtAppGetSelectionTimeout(xtContext);
		_wait(selectionTimeout);
		
	}
	return (!done) ? null : selectionValue;
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
 * Returns an array of the data types currently available on the system 
 * clipboard. Use with Transfer.isSupportedType.
 *
 * @return array of data types currently available on teh system clipboard
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
	return getAvailableTypes(DND.CLIPBOARD);
}

/**
 * Returns an array of the data types currently available on the specified 
 * clipboard. Use with Transfer.isSupportedType.
 * 
 * <p>The clipboards value is either one of the clipboard constants defined in
 * class <code>DND</code>, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>DND</code> clipboard constants.</p>
 * 
 * @param clipboards from which to get the data types
 * @return array of data types currently available on the specified clipboard
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Transfer#isSupportedType
 * @see DND#CLIPBOARD
 * @see DND#SELECTION_CLIPBOARD
 * 
 * @since 3.1
 */
public TransferData[] getAvailableTypes(int clipboards) {
	checkWidget();
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0) return null;
	int[] types = null;
	if ((clipboards & DND.CLIPBOARD) != 0) {
		types = getAvailableTypes(xDisplay, clipboardAtom);
	}
	if ((clipboards & DND.SELECTION_CLIPBOARD) != 0) {
		int[] primaryTypes = getAvailableTypes(xDisplay, primaryAtom);
		if (types == null) {
			types = primaryTypes;
		} else {
			int[] newTypes = new int[types.length + primaryTypes.length];
			System.arraycopy(types, 0, newTypes, 0, types.length);
			System.arraycopy(primaryTypes, 0, newTypes, types.length, primaryTypes.length);
			types = newTypes;
		}
	}
	if (types == null) return new TransferData[0];
	TransferData[] result = new TransferData[types.length];
	for (int i = 0; i < types.length; i++) {
		result[i] = new TransferData();
		result[i].type = types[i];
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
	int xDisplay = OS.XtDisplay (shellHandle);
	if (xDisplay == 0) return new String[0];
	int[] types = getAvailableTypes(xDisplay, clipboardAtom);
	int[] primaryTypes = getAvailableTypes(xDisplay, primaryAtom);
	String[] names = new String[types.length + primaryTypes.length];
	int index = 0;
	for (int i = 0; i < types.length; i++) {
		int ptr = OS.XmGetAtomName(xDisplay, types[i]);
		int length = OS.strlen(ptr);
		byte[] nameBuf = new byte[length];
		OS.memmove(nameBuf, ptr, length);
		OS.XtFree(ptr);
		/* Use the character encoding for the default locale */
		String name = new String(Converter.mbcsToWcs(null, nameBuf));
		names[index++] = "CLIPBOARD "+name;
	}
	for (int i = 0; i < primaryTypes.length; i++) {
		int ptr = OS.XmGetAtomName(xDisplay, primaryTypes[i]);
		int length = OS.strlen(ptr);
		byte[] nameBuf = new byte[length];
		OS.memmove(nameBuf, ptr, length);
		OS.XtFree(ptr);
		/* Use the character encoding for the default locale */
		String name = new String(Converter.mbcsToWcs(null, nameBuf));
		names[index++] = "PRIMARY "+name;
	}
	return names;
}

int[] getAvailableTypes(int xDisplay, int selection) {
	done = false;
	selectionValue = null; selectionTransfer = null;
	OS.XtGetSelectionValue(shellHandle, selection, targetsAtom, XtSelectionCallbackCallback.getAddress(), 0, OS.CurrentTime);
	if (!done) {
		int xtContext = OS.XtDisplayToApplicationContext(xDisplay);
		int selectionTimeout = OS.XtAppGetSelectionTimeout(xtContext);
		_wait(selectionTimeout);
		
	}
	return (!done || selectionValue == null) ? new int[0] : (int[])selectionValue;
}

/**
 * Place data of the specified type on the system clipboard.  More than one type
 * of data can be placed on the system clipboard at the same time.  Setting the
 * data clears any previous data from the system clipboard, regardless of type.
 * 
 * <p>NOTE: On some platforms, the data is immediately copied to the system
 * clipboard but on other platforms it is provided upon request.  As a result,
 * if the application modifes the data object it has set on the clipboard, that 
 * modification may or may not be available when the data is subsequently 
 * requested.</p>
 *
 * <p>The following snippet shows text and RTF text being set on the copy/paste
 * clipboard:
 * </p>
 * 
 * <code><pre>
 * 	Clipboard clipboard = new Clipboard(display);
 *	String textData = "Hello World";
 *	String rtfData = "{\\rtf1\\b\\i Hello World}";
 *	TextTransfer textTransfer = TextTransfer.getInstance();
 *	RTFTransfer rtfTransfer = RTFTransfer.getInstance();
 *	Transfer[] transfers = new Transfer[]{textTransfer, rtfTransfer};
 *	Object[] data = new Object[]{textData, rtfData};
 *	clipboard.setContents(data, transfers);
 *	clipboard.dispose();
 * </code></pre>
 *
 * @param data the data to be set in the clipboard
 * @param dataTypes the transfer agents that will convert the data to its 
 * platform specific format; each entry in the data array must have a 
 * corresponding dataType
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if data is null or datatypes is null 
 *          or the length of data is not the same as the length of dataTypes</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *  @exception SWTError <ul>
 *    <li>ERROR_CANNOT_SET_CLIPBOARD - if the clipboard is locked or otherwise unavailable</li>
 * </ul>
 * 
 * <p>NOTE: ERROR_CANNOT_SET_CLIPBOARD should be an SWTException, since it is a
 * recoverable error, but can not be changed due to backward compatability.</p>
 */
public void setContents(Object[] data, Transfer[] dataTypes) {
	setContents(data, dataTypes, DND.CLIPBOARD);
}

/**
 * Place data of the specified type on the specified clipboard.  More than one 
 * type of data can be placed on the specified clipboard at the same time.
 * Setting the data clears any previous data from the specified
 * clipboard, regardless of type.
 * 
 * <p>NOTE: On some platforms, the data is immediately copied to the specified
 * clipboard but on other platforms it is provided upon request.  As a result, 
 * if the application modifes the data object it has set on the clipboard, that 
 * modification may or may not be available when the data is subsequently 
 * requested.</p>
 *
 * <p>The clipboards value is either one of the clipboard constants defined in
 * class <code>DND</code>, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>DND</code> clipboard constants.</p>
 * 
 * <p>The following snippet shows text and RTF text being set on the copy/paste
 * clipboard:
 * </p>
 * 
 * <code><pre>
 * 	Clipboard clipboard = new Clipboard(display);
 *	String textData = "Hello World";
 *	String rtfData = "{\\rtf1\\b\\i Hello World}";
 *	TextTransfer textTransfer = TextTransfer.getInstance();
 *	RTFTransfer rtfTransfer = RTFTransfer.getInstance();
 *	Transfer[] transfers = new Transfer[]{textTransfer, rtfTransfer};
 *	Object[] data = new Object[]{textData, rtfData};
 *	clipboard.setContents(data, transfers, DND.CLIPBOARD);
 *	clipboard.dispose();
 * </code></pre>
 *
 * @param data the data to be set in the clipboard
 * @param dataTypes the transfer agents that will convert the data to its 
 * platform specific format; each entry in the data array must have a 
 * corresponding dataType
 * @param clipboards on which to set the data
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if data is null or datatypes is null 
 *          or the length of data is not the same as the length of dataTypes</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *  @exception SWTError <ul>
 *    <li>ERROR_CANNOT_SET_CLIPBOARD - if the clipboard is locked or otherwise unavailable</li>
 * </ul>
 * 
 * <p>NOTE: ERROR_CANNOT_SET_CLIPBOARD should be an SWTException, since it is a
 * recoverable error, but can not be changed due to backward compatability.</p>
 * 
 * @see DND#CLIPBOARD
 * @see DND#SELECTION_CLIPBOARD
 * 
 * @since 3.1
 */
public void setContents(Object[] data, Transfer[] dataTypes, int clipboards) {
	checkWidget();
	if (data == null || dataTypes == null || data.length != dataTypes.length || data.length == 0) {
		DND.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	for (int i = 0; i < data.length; i++) {
		if (data[i] == null || dataTypes[i] == null || !dataTypes[i].validate(data[i])) {
			DND.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	if ((clipboards & DND.CLIPBOARD) != 0) {
		ClipboardData = data;
		ClipboardDataTypes = dataTypes;
		_setContents(data, dataTypes, clipboardAtom);
		activeClipboard = this;
	}
	if ((clipboards & DND.SELECTION_CLIPBOARD) != 0) {
		PrimaryClipboardData = data;
		PrimaryClipboardDataTypes = dataTypes;
		_setContents(data, dataTypes, primaryAtom);
		activePrimaryClipboard = this;
	}
}
void _setContents(Object[] data, Transfer[] dataTypes, int selection) {
	int XtConvertSelectionProc = XtConvertSelectionCallback.getAddress();
	int XtLoseSelectionProc = XtLoseSelectionCallback.getAddress();
	int XtSelectionDoneProc = XtSelectionDoneCallback.getAddress();
	if (!OS.XtOwnSelection(shellHandle, selection, OS.CurrentTime, XtConvertSelectionProc, XtLoseSelectionProc, XtSelectionDoneProc)) {
		DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
	}
}

static void storePtr(int ptr, int selection, int target) {
	int index = -1;
	for (int i = 0; i < convertData.length; i++) {
		if (convertData[i][0] == 0){
			index = i;
			break;
		}
	}
	if (index == -1) {
		int[][] newConvertData = new int[convertData.length][3];
		System.arraycopy(convertData, 0, newConvertData, 0, convertData.length);
		index = convertData.length;
		convertData = newConvertData;
	}
	convertData[index][0] = selection;
	convertData[index][1] = target;
	convertData[index][2] = ptr;
}

void _wait(int timeout) {
	int xDisplay = OS.XtDisplay(shellHandle);
	if (xDisplay == 0) return;
	long start = System.currentTimeMillis();
	int xEvent = OS.XtMalloc (XEvent.sizeof);
	Callback checkEventCallback = new Callback(this, "checkEvent", 3);
	int checkEventProc = checkEventCallback.getAddress();
	display.timerExec(timeout, new Runnable() {
		public void run() {
			// timer required to force display.sleep() to wake up
			// in the case where no events are received
		}
	});
	while (!done && System.currentTimeMillis() - start <  timeout && !isDisposed()) {
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
static int XtConvertSelection(int widget, int selection, int target, int type, int value, int length, int format) {
	int xDisplay = OS.XtDisplay (widget);
	if (xDisplay == 0) return 0;
	int selectionAtom = 0;
	if (selection != 0) {
		int[] dest = new int[1];
		OS.memmove(dest, selection, 4);
		selectionAtom = dest[0];
	}
	if (selectionAtom == 0) return 0;
	int clipboardAtom = OS.XInternAtom (xDisplay, CLIPBOARD, false);
	int primaryAtom = OS.XInternAtom (xDisplay, PRIMARY, false);
	int targetsAtom = OS.XInternAtom (xDisplay, TARGETS, false);
	Transfer[] types = null;
	if (selectionAtom == clipboardAtom) types = ClipboardDataTypes;
	if (selectionAtom == primaryAtom) types = PrimaryClipboardDataTypes;
	if (types == null) return 0;
	
	int targetAtom = 0;
	if (target != 0) {
		int[] dest = new int[1];
		OS.memmove(dest, target, 4);
		targetAtom = dest[0];
	}
	if (targetAtom == targetsAtom) {
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
		OS.memmove(type, new int[]{targetsAtom}, 4);
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
	Object[] data = selectionAtom == clipboardAtom ? ClipboardData : PrimaryClipboardData;
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

static int XtLoseSelection(int widget, int selection) {
	int xDisplay = OS.XtDisplay (widget);
	int clipboardAtom = OS.XInternAtom (xDisplay, CLIPBOARD, false);
	if (selection == clipboardAtom) {
		activeClipboard = null;
		ClipboardData = null;
		ClipboardDataTypes = null;
	}
	int primaryAtom = OS.XInternAtom (xDisplay, PRIMARY, false);
	if (selection == primaryAtom) {
		activePrimaryClipboard = null;
		PrimaryClipboardData = null;
		PrimaryClipboardDataTypes = null;
	}
	return 0;
}

static int XtSelectionCallback(int widget, int client_data, int selection, int type, int value, int length, int format) {
	done = true;
	int[] selectionType = new int[1];
	if (type != 0) OS.memmove(selectionType, type, 4);
	if (selectionType[0] == 0) return 0;
	int[] selectionLength = new int[1];
	if (length != 0) OS.memmove(selectionLength, length, 4);
	if (selectionLength[0] == 0) return 0;
	int[] selectionFormat = new int[1];
	if (format != 0) OS.memmove(selectionFormat, format, 4);
	int xDisplay = OS.XtDisplay (widget);
	if (xDisplay == 0) return 0;
	int targetsAtom = OS.XInternAtom (xDisplay, TARGETS, false);
	if (selectionType[0] == targetsAtom) {
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

static int XtSelectionDone(int widget, int selection, int target) {
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
