package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.*;
import org.eclipse.swt.internal.carbon.*;

public class FileDialog extends Dialog {
	String [] filterNames = new String [0];
	String [] filterExtensions = new String [0];
	String [] fileNames = new String[0];	
	String filterPath = "", fileName = "";

public FileDialog (Shell parent) {
	this (parent, SWT.APPLICATION_MODAL);
}

public FileDialog (Shell parent, int style) {
	super (parent, style);
	checkSubclass ();
}

public String getFileName () {
	return fileName;
}

public String [] getFileNames () {
	return fileNames;
}

public String [] getFilterExtensions () {
	return filterExtensions;
}

public String [] getFilterNames () {
	return filterNames;
}

public String getFilterPath () {
	return filterPath;
}

public String open () {
	String fullPath = null;
	fileNames = null;
		
	int titlePtr = 0;
	if (title != null) {
		char [] buffer = new char [title.length ()];
		title.getChars (0, buffer.length, buffer, 0);
		titlePtr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);
	}
	int fileNamePtr = 0;
	if (fileName != null) {
		char [] buffer = new char [fileName.length ()];
		fileName.getChars (0, buffer.length, buffer, 0);
		fileNamePtr = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, buffer, buffer.length);		
	}
		
	NavDialogCreationOptions options = new NavDialogCreationOptions ();
	options.windowTitle = options.clientName = titlePtr;
	options.parentWindow = OS.GetControlOwner (parent.handle);
	options.optionFlags = OS.kNavSupportPackages | OS.kNavAllowOpenPackages | OS.kNavAllowInvisibleFiles;
	options.location_h = -1;
	options.location_v = -1;
	options.saveFileName = fileNamePtr;

	int [] outDialog = new int [1];
	if ((style & SWT.SAVE) != 0) {
		// NEEDS WORK - filter extensions, start in filter path, allow user
		// to select existing files.
		OS.NavCreatePutFileDialog (options, 0, 0, 0, 0, outDialog);		
	} else {
		if ((style & SWT.MULTI) != 0) options.optionFlags |= OS.kNavAllowMultipleFiles;
		// NEEDS WORK - filter extensions, start in filter path, select file name if it exists
		OS.NavCreateGetFileDialog(options, 0, 0, 0, 0, 0, outDialog);
	}
	if (outDialog [0] != 0) {
		OS.NavDialogRun (outDialog [0]);
		int action = OS.NavDialogGetUserAction (outDialog [0]);
		switch (action) {
			case OS.kNavUserActionOpen:
			case OS.kNavUserActionChoose:							
			case OS.kNavUserActionSaveAs: {
				NavReplyRecord record = new NavReplyRecord ();
				OS.NavDialogGetReply (outDialog [0], record);
				AEDesc selection = new AEDesc ();
				selection.descriptorType = record.selection_descriptorType;
				selection.dataHandle = record.selection_dataHandle;
				int [] count = new int [1];
				OS.AECountItems (selection, count);
				if (count [0] > 0) {
					fileNames = new String [count [0]];
					int maximumSize = 80; // size of FSRef
					int dataPtr = OS.NewPtr (maximumSize);
					int[] aeKeyword = new int [1];
					int[] typeCode = new int [1];
					int[] actualSize = new int [1];
					int pathString = 0;
					int fullString = 0;
					int fileString = 0;
												
					if ((style & SWT.SAVE) != 0) {
						if (OS.AEGetNthPtr (selection, 1, OS.typeFSRef, aeKeyword, typeCode, dataPtr, maximumSize, actualSize) == OS.noErr) {
							byte[] fsRef = new byte[actualSize[0]];
							OS.memcpy (fsRef, dataPtr, actualSize [0]);
							int pathUrl = OS.CFURLCreateFromFSRef (OS.kCFAllocatorDefault, fsRef);
							int fullUrl = OS.CFURLCreateCopyAppendingPathComponent(OS.kCFAllocatorDefault, pathUrl, record.saveFileName, false);
							pathString = OS.CFURLCopyFileSystemPath(pathUrl, OS.kCFURLPOSIXPathStyle);
							fullString = OS.CFURLCopyFileSystemPath(fullUrl, OS.kCFURLPOSIXPathStyle);
							fileString = record.saveFileName;
							OS.CFRelease (pathUrl);
							OS.CFRelease (fullUrl);
						}
					} else {
						for (int i = 0; i < count [0]; i++) {
							if (OS.AEGetNthPtr (selection, i+1, OS.typeFSRef, aeKeyword, typeCode, dataPtr, maximumSize, actualSize) == OS.noErr) {
								byte[] fsRef = new byte[actualSize[0]];
								OS.memcpy (fsRef, dataPtr, actualSize [0]);
								int url = OS.CFURLCreateFromFSRef (OS.kCFAllocatorDefault, fsRef);
								if (i == 0) {
									int pathUrl = OS.CFURLCreateCopyDeletingLastPathComponent(OS.kCFAllocatorDefault, url);
									pathString = OS.CFURLCopyFileSystemPath (pathUrl, OS.kCFURLPOSIXPathStyle);
									fullString = OS.CFURLCopyFileSystemPath (url, OS.kCFURLPOSIXPathStyle);
									fileString = OS.CFURLCopyLastPathComponent (url);
									OS.CFRelease (pathUrl);
								} else {
									int lastString = OS.CFURLCopyLastPathComponent (url);
									int length = OS.CFStringGetLength (lastString);
									char [] buffer= new char [length];
									CFRange range = new CFRange ();
									range.length = length;
									OS.CFStringGetCharacters (lastString, range, buffer);
									fileNames [i] = new String (buffer);
									OS.CFRelease (lastString);
								}
								OS.CFRelease (url);
							}
						}
					}
					OS.DisposePtr (dataPtr);
					
					if (pathString != 0) {		
						int length = OS.CFStringGetLength (pathString);
						char [] buffer= new char [length];
						CFRange range = new CFRange ();
						range.length = length;
						OS.CFStringGetCharacters (pathString, range, buffer);
						OS.CFRelease (pathString);
						filterPath = new String (buffer);
					}
					if (fullString != 0) {
						int length = OS.CFStringGetLength (fullString);
						char [] buffer= new char [length];
						CFRange range = new CFRange ();
						range.length = length;
						OS.CFStringGetCharacters (fullString, range, buffer);
						OS.CFRelease (fullString);
						fullPath = new String (buffer);
					} 
					if (fileString != 0) {
						int length = OS.CFStringGetLength (fileString);
						char [] buffer= new char [length];
						CFRange range = new CFRange ();
						range.length = length;
						OS.CFStringGetCharacters (fileString, range, buffer);
						OS.CFRelease (fileString);
						fileName = fileNames [0] = new String (buffer);
					}
				}
			}
		}
	}

	if (titlePtr != 0) OS.CFRelease (titlePtr);
	if (fileNamePtr != 0) OS.CFRelease (fileNamePtr);	
	if (outDialog [0] != 0) OS.NavDialogDispose (outDialog [0]);

	return fullPath;	
}

public void setFileName (String string) {
	fileName = string;
}

public void setFilterExtensions (String [] extensions) {
	filterExtensions = extensions;
}

public void setFilterNames (String [] names) {
	filterNames = names;
}

public void setFilterPath (String string) {
	filterPath = string;
}
}
