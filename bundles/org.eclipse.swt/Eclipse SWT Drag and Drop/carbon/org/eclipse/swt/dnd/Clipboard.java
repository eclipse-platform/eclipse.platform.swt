package org.eclipse.swt.dnd;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.carbon.MacUtil;
import org.eclipse.swt.internal.carbon.OS;


/**
 * The <code>Clipboard</code> provides a mechanism for transferring data from one
 * application to another or within an application.
 * 
 * <p>IMPORTANT: This class is <em>not</em> intended to be subclassed.</p>
 */
public class Clipboard {
	
	private Display display;

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
	
	if (display.isDisposed())
		return null;
		
	if (transfer == null)
		return null;
		
	int[] scrapHandle= new int[1];
	OS.GetCurrentScrap(scrapHandle);
	int scrap= scrapHandle[0];
		
	// Does Clipboard have data in required format?
	int[] typeIds= transfer.getTypeIds();
	for (int i= 0; i < typeIds.length; i++) {
		int flavorType= typeIds[i];
		int[] size= new int[1];
		if (OS.GetScrapFlavorSize(scrap, flavorType, size) == OS.kNoErr) {
			if (size[0] > 0) {
				
				TransferData tdata= new TransferData();
	
				tdata.type= flavorType;		
				tdata.data= new byte[size[0]];
				OS.GetScrapFlavorData(scrap, flavorType, size, tdata.data);
				tdata.length= size[0];
				
				Object result= transfer.nativeToJava(tdata);
				if (result != null)
					return result;
			}
		}
	}		
		
		
	/*
	int[] flavorCount= new int[1];
	OS.GetScrapFlavorCount(scrap, flavorCount);
	
	System.out.println("Clipboard.getContents:");
	if (flavorCount[0] > 0) {
		int[] info= new int[flavorCount[0] * 2];
		OS.GetScrapFlavorInfoList(scrap, flavorCount, info);
		for (int i= 0; i < flavorCount[0]; i++) {
			int flavorType= info[i*2];
			String type= MacUtil.toString(flavorType);
			System.out.println("  " + i + ": " + type);
			if ("TEXT".equals(type) && transfer instanceof TextTransfer) {
				int[] size= new int[1];
				OS.GetScrapFlavorSize(scrap, flavorType, size);
				if (size[0] > 0) {
					byte[] data= new byte[size[0]];
					OS.GetScrapFlavorData(scrap, flavorType, size, data);
					return new String(data, 0, size[0]);
				}
			}
		}
	}
	*/
	
	return null;	// No data available for this transfer
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
	
	if (data == null) {
		DND.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (dataTypes == null || data.length != dataTypes.length) {
		DND.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	
	if (display.isDisposed())
		DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
	
	System.out.println("Clipboard.setContents:");
	for (int i = 0; i < dataTypes.length; i++) {
		System.out.println("  " + i + ": " + dataTypes[i]);
	}
	
	OS.ClearCurrentScrap();
	int[] scrapHandle= new int[1];
	OS.GetCurrentScrap(scrapHandle);
	int scrap= scrapHandle[0];
	
	/*
	for (int i = 0; i < transferAgents.length; i++) {
		if (transferAgents[i] instanceof RTFTransfer && data[i] instanceof String) {
			String s= (String) data[i];
			int flavorType= ('R'<<24) + ('T'<<16) + ('F'<<8) + ' ';
			if (OS.PutScrapFlavor(scrap, flavorType, 0, s.getBytes()) == OS.kNoErr)
				return;
		}
		if (transferAgents[i] instanceof TextTransfer && data[i] instanceof String) {
			String s= (String) data[i];
			int flavorType= ('T'<<24) + ('E'<<16) + ('X'<<8) + 'T';
			if (OS.PutScrapFlavor(scrap, flavorType, 0, s.getBytes()) == OS.kNoErr)
				return;
		}
	}
	*/
	
	int status= 1;
	
	// copy data directly over to System clipboard (not deferred)
	for (int i= 0; i < dataTypes.length; i++) {
		int[] ids= dataTypes[i].getTypeIds();
		for (int j= 0; j < ids.length; j++) {
			TransferData transferData= new TransferData();
			/* Use the character encoding for the default locale */
			transferData.type= ids[j];
			dataTypes[i].javaToNative(data[i], transferData);
			if (transferData.result == 1) {
				/*
				if (transferData.format == 8) {
					byte[] buffer = new byte[transferData.length];
					OS.memmove(buffer, transferData.pValue, transferData.length);
					byte[] bName = Converter.wcsToMbcs(null, names[j], true);
					status = OS.XmClipboardCopy(xDisplay, xWindow, item_id[0], bName, buffer, transferData.length, 0, null);
				}
				*/
				status= OS.PutScrapFlavor(scrap, transferData.type, 0, transferData.data);
			}
		}
	}
	
	if (status != OS.kNoErr)
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
 * @returns a platform specific list of the data types currently available on the 
 * system clipboard
 */
public String[] getAvailableTypeNames() {

	if (display.isDisposed())
		return null;
	
	int[] scrapHandle= new int[1];
	OS.GetCurrentScrap(scrapHandle);
	int scrap= scrapHandle[0];
	
	int[] flavorCount= new int[1];
	OS.GetScrapFlavorCount(scrap, flavorCount);
	
	System.out.println("Clipboard.getAvailableTypeNames:");
	if (flavorCount[0] > 0) {
		int[] info= new int[flavorCount[0] * 2];
		OS.GetScrapFlavorInfoList(scrap, flavorCount, info);
		int n= flavorCount[0];
		String[] result= new String[n];
		for (int i= 0; i < n; i++) {
			int flavorType= info[i*2];
			String type= MacUtil.toString(flavorType);
			System.out.println("  " + i + ": " + type);
			result[i]= type;
		}
		return result;
	}

	return null;
}
}
