package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.*;


public class DirectoryDialog extends Dialog {
	String message = "", filterPath = "";

public DirectoryDialog (Shell parent) {
	this (parent, SWT.APPLICATION_MODAL);
}

public DirectoryDialog (Shell parent, int style) {
	super (parent, style);
	checkSubclass ();
}

public String getFilterPath () {
	return filterPath;
}

public String getMessage () {
	return message;
}

public String open () {
	String directoryPath = null;	
	int titlePtr = 0;
	int messagePtr = 0;
	if (title != null) {
		char [] buffer = new char [title.length ()];
		title.getChars (0, buffer.length, buffer, 0);
		titlePtr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	}
	if (message != null) {
		char [] buffer = new char [message.length ()];
		message.getChars (0, buffer.length, buffer, 0);
		messagePtr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	}

	NavDialogCreationOptions options = new NavDialogCreationOptions ();
	OS.NavGetDefaultDialogCreationOptions (options);
	options.parentWindow = OS.GetControlOwner (parent.handle);
	// NEEDS WORK - no title displayed
	options.windowTitle = titlePtr;
	options.message = messagePtr;
	int [] outDialog = new int [1];
	// NEEDS WORK - use inFilterProc to handle filtering
	if (OS.NavCreateChooseFolderDialog (options, 0, 0, 0, outDialog) == OS.noErr) {
		OS.NavDialogRun (outDialog [0]);
		switch (OS.NavDialogGetUserAction (outDialog [0])) {
			case OS.kNavUserActionCancel: break;
			case OS.kNavUserActionOpen:
			case OS.kNavUserActionChoose: {		
				NavReplyRecord record = new NavReplyRecord ();
				OS.NavDialogGetReply (outDialog [0], record);
				AEDesc selection = new AEDesc ();
				selection.descriptorType = record.selection_descriptorType;
				selection.dataHandle = record.selection_dataHandle;
				int [] count = new int [1];
				OS.AECountItems (selection, count);
				if (count [0] > 0) {
					int [] theAEKeyword = new int [1];
					int [] typeCode = new int [1];
					int maximumSize = 80; // size of FSRef
					int dataPtr = OS.NewPtr (maximumSize);
					int [] actualSize = new int [1];
					int status = OS.AEGetNthPtr (selection, 1, OS.typeFSRef, theAEKeyword, typeCode, dataPtr, maximumSize, actualSize);
					if (status == OS.noErr && typeCode [0] == OS.typeFSRef) {
						byte [] fsRef = new byte [actualSize [0]];
						OS.memcpy (fsRef, dataPtr, actualSize [0]);
						int anURL = OS.CFURLCreateFromFSRef (OS.kCFAllocatorDefault, fsRef);
						int ptr = OS.CFURLCopyFileSystemPath(anURL, OS.kCFURLPOSIXPathStyle);
						OS.CFRelease (anURL);						
						int length = OS.CFStringGetLength (ptr);
						char [] buffer= new char [length];
						CFRange range = new CFRange ();
						range.length = length;
						OS.CFStringGetCharacters (ptr, range, buffer);
						OS.CFRelease (ptr);
						directoryPath = new String (buffer);
					}
					OS.DisposePtr (dataPtr);
				}
			}
		}
	}
	
	if (titlePtr != 0) OS.CFRelease (titlePtr);	
	if (messagePtr != 0) OS.CFRelease (messagePtr);
	if (outDialog [0] != 0) OS.NavDialogDispose (outDialog [0]);
	
	return directoryPath;
}

public void setMessage (String string) {
	message = string;
}

public void setFilterPath (String string) {
	filterPath = string;
}
}
