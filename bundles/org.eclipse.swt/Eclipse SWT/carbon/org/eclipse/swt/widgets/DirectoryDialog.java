package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.*;

/**
 * Instances of this class allow the user to navigate
 * the file system and select a directory.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class DirectoryDialog extends Dialog {
	String filterPath = "";
	String message = "";
/**
 * Constructs a new instance of this class given only its
 * parent.
 * <p>
 * Note: Currently, null can be passed in for the parent.
 * This has the effect of creating the dialog on the currently active
 * display if there is one. If there is no current display, the 
 * dialog is created on a "default" display. <b>Passing in null as
 * the parent is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public DirectoryDialog (Shell parent) {
	this (parent, SWT.PRIMARY_MODAL);
}
/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 * Note: Currently, null can be passed in for the parent.
 * This has the effect of creating the dialog on the currently active
 * display if there is one. If there is no current display, the 
 * dialog is created on a "default" display. <b>Passing in null as
 * the parent is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public DirectoryDialog (Shell parent, int style) {
	super (parent, style);
	checkSubclass ();
}
/**
 * Returns the path which the dialog will use to filter
 * the directories it shows.
 *
 * @return the filter path
 */
public String getFilterPath () {
	return filterPath;
}
/**
 * Returns the dialog's message, which is a description of
 * the purpose for which it was opened. This message will be
 * visible on the dialog while it is open.
 *
 * @return the message
 */
public String getMessage () {
	return message;
}
private String interpretOsAnswer(int dialog) {
	int[] tmp= new int[1];
	OS.NavDialogGetReply(dialog, tmp);
	int reply= tmp[0];
	
	int selection= OS.NavReplyRecordGetSelection(reply);
	OS.AECountItems(selection, tmp);
	int count= tmp[0];
	
	if (count > 0) {
		OS.AEGetNthPtr(selection, 1, tmp);
		return MacUtil.getStringAndRelease(tmp[0]);
	}
	return null;
}
/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return a string describing the absolute path of the selected directory,
 *         or null if the dialog was cancelled or an error occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public String open () {
	int dialog= 0;
	int titleHandle= 0;
	int messageHandle= 0;
	try {
		int[] dialogHandle= new int[1];
		
		int parentWindowHandle= 0;
		if (parent != null)
			parentWindowHandle= parent.shellHandle;
			
		titleHandle= OS.CFStringCreateWithCharacters(title);
		messageHandle= OS.CFStringCreateWithCharacters(message);
		
		NavDialogCreationOptions options = new NavDialogCreationOptions();
		OS.NavGetDefaultDialogCreationOptions(options);
		options.parentWindow= parentWindowHandle;
		options.windowTitle= titleHandle;
		options.message= messageHandle;
		OS.NavCreateChooseFolderDialog(options, 0, 0, 0, dialogHandle);
		dialog= dialogHandle[0];
		
		if (dialog != 0) {
					
			OS.NavDialogRun(dialog);
		
			switch (OS.NavDialogGetUserAction(dialog)) {
			case OS.kNavUserActionCancel:
				break;
				
			case OS.kNavUserActionOpen:
			case OS.kNavUserActionChoose:			
				return interpretOsAnswer(dialog);
			}
		}
		
		return null;

	} finally {
		if (titleHandle != 0)
			OS.CFRelease(titleHandle);
		if (messageHandle != 0)
			OS.CFRelease(messageHandle);
		if (dialog != 0)
			OS.NavDialogDispose(dialog);
	}
}
/**
 * Sets the dialog's message, which is a description of
 * the purpose for which it was opened. This message will be
 * visible on the dialog while it is open.
 *
 * @param string the message
 */
public void setMessage (String string) {
	message = string;
}
/**
 * Sets the path which the dialog will use to filter
 * the directories it shows to the argument, which may be
 * null.
 *
 * @param string the filter path
 */
public void setFilterPath (String string) {
	filterPath = string;
}
}
