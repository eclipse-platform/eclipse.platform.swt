package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
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
public void dispose () {
	display = null;
}
public Object getContents(Transfer transfer) {
	if (display.isDisposed() || !(transfer instanceof TextTransfer)) return null;
	return display.getData("TextTransfer");
}
/**
 * Set the data onto the clipboard.  NOTE: On some platforms, the data is
 * immediately flushed to the clipboard but on som eplatforms it is provided
 * upon request.  As a result, if the application modifes the data Object it has set 
 * on the clipboard, that modification may or may not be available when the 
 * object is subsequently pasted.
 * 
 * @param data the data to be set in the clipboard
 * @param dataTypes the corresponding transfer agent for data
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
/*
 * Note: getAvailableTypeNames is a tool for writing a Transfer sub-class only.  It should
 * NOT be used within an application because it provides platform specfic 
 * information.
 */
public String[] getAvailableTypeNames() {
	return null;
}
}
