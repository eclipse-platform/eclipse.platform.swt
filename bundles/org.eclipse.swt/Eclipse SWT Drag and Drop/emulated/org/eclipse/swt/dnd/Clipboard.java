package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * The <code>Clipboard</code> provides a mechanism for copying data from one
 * application to another or within an application.
 * 
 * <p>IMPORTANT: This class is <em>not</em> intended to be subclassed.</p>
 */
 
public class Clipboard {
	
	private Display display;

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
 *  Retrieve the data of the specified type currently available on the system clipboard.
 * 
 *  <code><pre>
 * 	Clipboard clipboard = new Clipboard(display);
 *		TextTransfer textTransfer = TextTransfer.getInstance();
 *		String textData = (String)clipboard.getContents(textTransfer);
 * 	if (textData != null) System.out.println("Text is "+textData);
 *		RTFTransfer rtfTransfer = RTFTransfer.getInstance();
 *  	String rtfData = (String)clipboard.getContents(rtfTransfer);
 * 	if (rtfData != null) System.out.println("RTF Text is "+rtfData);
 *		clipboard.dispose();
 * </code></pre>
 * 
 * @param transfer the transfer agent for the type of data being requested
 * 
 * @return the data obtained from the clipboard or null if no data of this type is available
 */
public Object getContents(Transfer transfer) {
	if (display.isDisposed() || !(transfer instanceof TextTransfer)) return null;
	return display.getData("TextTransfer");
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
public void setContents(Object[] data, Transfer[] transferAgents){
	
	if (data == null) {
		DND.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	if (transferAgents == null || data.length != transferAgents.length) {
		DND.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	
	if (display.isDisposed() )
		DND.error(DND.ERROR_CANNOT_SET_CLIPBOARD);
	
	for (int i = 0; i < transferAgents.length; i++) {
		if (transferAgents[i] instanceof TextTransfer && data[i] instanceof String){
			display.setData("TextTransfer", data[i]);
			return;
		}
	}
}
/**
 * Returns a platform specific list of the data types currently available on the 
 * system clipboard.
 * 
 * <p>Note: <code>getAvailableTypeNames</code> is a tool for writing a Transfer 
 * sub-class only.  It should NOT be used within an application because it provides 
 * platform specific information.</p>
 * 
 * @returns a platform specific list of the data types currently available on the 
 * system clipboard
 */
/*
 * Note: getAvailableTypeNames is a tool for writing a Transfer sub-class only.  It should
 * NOT be used within an application because it provides platform specfic 
 * information.
 */
public String[] getAvailableTypeNames() {
	return null;
}
}
