/*******************************************************************************
 * Copyright (c) 2003, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.widgets.*;

class FilePicker {
	XPCOMObject supports;
	XPCOMObject filePicker;

	int refCount = 0;
	short mode;
	int /*long*/ parentHandle;
	String[] files, masks;
	String defaultFilename, directory, title;

	static final String SEPARATOR = System.getProperty ("file.separator"); //$NON-NLS-1$

FilePicker () {
	createCOMInterfaces ();
}

int AddRef () {
	refCount++;
	return refCount;
}

void createCOMInterfaces () {
	/* Create each of the interfaces that this object implements */
	supports = new XPCOMObject (new int[] {2, 0, 0}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
	};

	filePicker = new XPCOMObject (new int[] {2, 0, 0, 3, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return Init (args[0], args[1], (short)args[2]);}
		public int /*long*/ method4 (int /*long*/[] args) {return AppendFilters ((int)/*64*/args[0]);}
		public int /*long*/ method5 (int /*long*/[] args) {return AppendFilter (args[0], args[1]);}
		public int /*long*/ method6 (int /*long*/[] args) {return GetDefaultString (args[0]);}
		public int /*long*/ method7 (int /*long*/[] args) {return SetDefaultString (args[0]);}
		public int /*long*/ method8 (int /*long*/[] args) {return GetDefaultExtension (args[0]);}
		public int /*long*/ method9 (int /*long*/[] args) {return SetDefaultExtension (args[0]);}
		public int /*long*/ method10 (int /*long*/[] args) {return GetFilterIndex (args[0]);}
		public int /*long*/ method11 (int /*long*/[] args) {return SetFilterIndex ((int)/*64*/args[0]);}
		public int /*long*/ method12 (int /*long*/[] args) {return GetDisplayDirectory (args[0]);}
		public int /*long*/ method13 (int /*long*/[] args) {return SetDisplayDirectory (args[0]);}
		public int /*long*/ method14 (int /*long*/[] args) {return GetFile (args[0]);}
		public int /*long*/ method15 (int /*long*/[] args) {return GetFileURL (args[0]);}
		public int /*long*/ method16 (int /*long*/[] args) {return GetFiles (args[0]);}
		public int /*long*/ method17 (int /*long*/[] args) {return Show (args[0]);}
	};
}

void disposeCOMInterfaces () {
	if (supports != null) {
		supports.dispose ();
		supports = null;
	}	
	if (filePicker != null) {
		filePicker.dispose ();
		filePicker = null;	
	}
}

int /*long*/ getAddress () {
	return filePicker.getAddress ();
}

int QueryInterface (int /*long*/ riid, int /*long*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID ();
	XPCOM.memmove (guid, riid, nsID.sizeof);
	
	if (guid.Equals (nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {supports.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIFilePicker.NS_IFILEPICKER_IID)) {
		XPCOM.memmove(ppvObject, new int /*long*/[] {filePicker.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIFilePicker_1_8.NS_IFILEPICKER_IID)) {
		XPCOM.memmove(ppvObject, new int /*long*/[] {filePicker.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}

	XPCOM.memmove (ppvObject, new int /*long*/[] {0}, C.PTR_SIZEOF);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}

int Release () {
	refCount--;
	if (refCount == 0) disposeCOMInterfaces ();
	return refCount;
}

Browser getBrowser (int /*long*/ aDOMWindow) {
	if (aDOMWindow == 0) return null;
	nsIDOMWindow window = new nsIDOMWindow (aDOMWindow);
	return Mozilla.findBrowser (window);
}

/*
 * As of Mozilla 1.8 some of nsIFilePicker's string arguments changed type.  This method
 * answers a java string based on the type of string that is appropriate for the Mozilla
 * version being used.
 */
String parseAString (int /*long*/ string) {
	return null;
}

/* nsIFilePicker */

int Init (int /*long*/ parent, int /*long*/ title, short mode) {
	parentHandle = parent;
	this.mode = mode;
	this.title = parseAString (title);
	return XPCOM.NS_OK;
}

int Show (int /*long*/ _retval) {
	if (mode == nsIFilePicker.modeGetFolder) {
		/* picking a directory */
		int result = showDirectoryPicker ();
		XPCOM.memmove (_retval, new short[] {(short)result}, 2); /* PRInt16 */
		return XPCOM.NS_OK;
	}

	/* picking a file */
	int style = mode == nsIFilePicker.modeSave ? SWT.SAVE : SWT.OPEN;
	if (mode == nsIFilePicker.modeOpenMultiple) style |= SWT.MULTI;
	Browser browser = getBrowser (parentHandle);
	Shell parent = null;
	if (browser != null) {
		parent = browser.getShell ();
	} else {
		parent = new Shell ();
	}
	FileDialog dialog = new FileDialog (parent, style);
	if (title != null) dialog.setText (title);
	if (directory != null) dialog.setFilterPath (directory);
	if (masks != null) dialog.setFilterExtensions (masks);
	if (defaultFilename != null) dialog.setFileName (defaultFilename);
	String filename = dialog.open ();
	files = dialog.getFileNames ();
	directory = dialog.getFilterPath ();
	title = defaultFilename = null;
	masks = null;
	int result = filename == null ? nsIFilePicker.returnCancel : nsIFilePicker.returnOK; 
	XPCOM.memmove (_retval, new short[] {(short)result}, 2); /* PRInt16 */
	return XPCOM.NS_OK;
}

int showDirectoryPicker () {
	Browser browser = getBrowser (parentHandle);
	Shell parent = null;
	if (browser != null) {
		parent = browser.getShell ();
	} else {
		parent = new Shell ();
	}
	DirectoryDialog dialog = new DirectoryDialog (parent, SWT.NONE);
	if (title != null) dialog.setText (title);
	if (directory != null) dialog.setFilterPath (directory);
	directory = dialog.open ();
	title = defaultFilename = null;
	files = masks = null;
	return directory == null ? nsIFilePicker.returnCancel : nsIFilePicker.returnOK;
}

int GetFiles (int /*long*/ aFiles) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetFileURL (int /*long*/ aFileURL) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetFile (int /*long*/ aFile) {
	String filename = "";	//$NON-NLS-1$
	if (directory != null) filename += directory + SEPARATOR;
	if (files != null && files.length > 0) filename += files[0];
	nsEmbedString path = new nsEmbedString (filename);
	int /*long*/[] file = new int /*long*/[1];
	int rc = XPCOM.NS_NewLocalFile (path.getAddress (), 1, file);
	path.dispose ();
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (file[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NULL_POINTER);
	XPCOM.memmove (aFile, file, C.PTR_SIZEOF);
	return XPCOM.NS_OK;
}

int SetDisplayDirectory (int /*long*/ aDisplayDirectory) {
	if (aDisplayDirectory == 0) return XPCOM.NS_OK;
	nsILocalFile file = new nsILocalFile (aDisplayDirectory);
	int /*long*/ pathname = XPCOM.nsEmbedCString_new ();
	file.GetNativePath (pathname);
	int length = XPCOM.nsEmbedCString_Length (pathname);
	int /*long*/ buffer = XPCOM.nsEmbedCString_get (pathname);
	byte[] bytes = new byte[length];
	XPCOM.memmove (bytes, buffer, length);
	XPCOM.nsEmbedCString_delete (pathname);
	char[] chars = MozillaDelegate.mbcsToWcs (null, bytes);
	directory = new String (chars);
	return XPCOM.NS_OK;
}

int GetDisplayDirectory (int /*long*/ aDisplayDirectory) {
	String directoryName = directory != null ? directory : "";	//$NON-NLS-1$
	nsEmbedString path = new nsEmbedString (directoryName);
	int /*long*/[] file = new int /*long*/[1];
	int rc = XPCOM.NS_NewLocalFile (path.getAddress (), 1, file);
	path.dispose ();
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (file[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NULL_POINTER);
	XPCOM.memmove (aDisplayDirectory, file, C.PTR_SIZEOF);
	return XPCOM.NS_OK;
}

int SetFilterIndex (int aFilterIndex) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetFilterIndex (int /*long*/ aFilterIndex) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetDefaultExtension (int /*long*/ aDefaultExtension) {
	/* note that the type of argument 1 changed as of Mozilla 1.8 */
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetDefaultExtension (int /*long*/ aDefaultExtension) {
	/* note that the type of argument 1 changed as of Mozilla 1.8 */
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetDefaultString (int /*long*/ aDefaultString) {
	defaultFilename = parseAString (aDefaultString);
	return XPCOM.NS_OK;
}

int GetDefaultString (int /*long*/ aDefaultString) {
	/* note that the type of argument 1 changed as of Mozilla 1.8 */
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int AppendFilter (int /*long*/ title, int /*long*/ filter) {
	/* note that the type of arguments 1 and 2 changed as of Mozilla 1.8 */
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int AppendFilters (int filterMask) {
	String[] addFilters = null;
	switch (filterMask) {
		case nsIFilePicker.filterAll:
		case nsIFilePicker.filterApps:
			masks = null;			/* this is equivalent to no filter */
			break;
		case nsIFilePicker.filterHTML:
			addFilters = new String[] {"*.htm;*.html"}; //$NON-NLS-1$
			break;
		case nsIFilePicker.filterImages:
			addFilters = new String[] {"*.gif;*.jpeg;*.jpg;*.png"};	//$NON-NLS-1$
			break;
		case nsIFilePicker.filterText:
			addFilters = new String[] {"*.txt"};	//$NON-NLS-1$
			break;
		case nsIFilePicker.filterXML:
			addFilters = new String[] {"*.xml"};	//$NON-NLS-1$
			break;
		case nsIFilePicker.filterXUL:
			addFilters = new String[] {"*.xul"};	//$NON-NLS-1$
			break;
	}
	if (masks == null) {
		masks = addFilters;
	} else {
		if (addFilters != null) {
			String[] newFilters = new String[masks.length + addFilters.length];
			System.arraycopy (masks, 0, newFilters, 0, masks.length);
			System.arraycopy (addFilters, 0, newFilters, masks.length, addFilters.length);
			masks = newFilters;
		}
	}
	return XPCOM.NS_OK;
}
}
