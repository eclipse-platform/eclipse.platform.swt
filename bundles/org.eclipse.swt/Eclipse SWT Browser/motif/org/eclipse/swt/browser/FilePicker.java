/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
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
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.motif.OS;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.widgets.*;

class FilePicker {
	XPCOMObject supports;
	XPCOMObject filePicker;

	int refCount = 0;

	int mode;
	String[] files, masks;
	String defaultFilename, directory, title;

	static final String SEPARATOR = System.getProperty ("file.separator"); //$NON-NLS-1$

public FilePicker () {
	createCOMInterfaces ();
}

int AddRef () {
	refCount++;
	return refCount;
}

void createCOMInterfaces () {
	/* Create each of the interfaces that this object implements */
	supports = new XPCOMObject (new int[] {2, 0, 0}){
		public int method0 (int [] args) {return queryInterface(args [0], args [1]);}
		public int method1 (int [] args) {return AddRef ();}
		public int method2 (int [] args) {return Release ();}
	};

	filePicker = new XPCOMObject (new int[] {2, 0, 0, 3, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}) {
		public int method0 (int [] args) {return queryInterface (args [0], args [1]);}
		public int method1 (int [] args) {return AddRef ();}
		public int method2 (int [] args) {return Release ();}
		public int method3 (int [] args) {return Init (args [0], args [1], args [2]);}
		public int method4 (int [] args) {return AppendFilters (args [0]);}
		public int method5 (int [] args) {return AppendFilter (args [0], args [1]);}
		public int method6 (int [] args) {return GetDefaultString (args [0]);}
		public int method7 (int [] args) {return SetDefaultString (args [0]);}
		public int method8 (int [] args) {return GetDefaultExtension (args [0]);}
		public int method9 (int [] args) {return SetDefaultExtension (args [0]);}
		public int method10 (int [] args) {return GetFilterIndex (args [0]);}
		public int method11 (int [] args) {return SetFilterIndex (args [0]);}
		public int method12 (int [] args) {return GetDisplayDirectory (args [0]);}
		public int method13 (int [] args) {return SetDisplayDirectory (args [0]);}
		public int method14 (int [] args) {return GetFile (args [0]);}
		public int method15 (int [] args) {return GetFileURL (args [0]);}
		public int method16 (int [] args) {return GetFiles (args [0]);}
		public int method17 (int [] args) {return Show (args [0]);}
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

int getAddress () {
	return filePicker.getAddress ();
}

int queryInterface (int riid, int ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID ();
	XPCOM.memmove (guid, riid, nsID.sizeof);

	if (guid.Equals (nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove (ppvObject, new int [] {supports.getAddress ()}, OS.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIFilePicker.NS_IFILEPICKER_IID)) {
		XPCOM.memmove(ppvObject, new int [] {filePicker.getAddress ()}, OS.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	XPCOM.memmove (ppvObject, new int [] {0}, OS.PTR_SIZEOF);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}

int Release () {
	refCount--;
	if (refCount == 0) disposeCOMInterfaces ();
	return refCount;
}

/* nsIFilePicker */

int Init (int parent, int title, int mode) {
	this.mode = (int)mode;
	if (title != 0) {
		int length = XPCOM.strlen_PRUnichar (title);
		char[] chars = new char [length];
		XPCOM.memmove (chars, title, length * 2);
		this.title = new String (chars);
	}

	return XPCOM.NS_OK;
}

int Show (int _retval) {
	if (mode == nsIFilePicker.modeGetFolder) {
		/* picking a directory */
		int result = showDirectoryPicker ();
		XPCOM.memmove (_retval, new int [] {result}, OS.PTR_SIZEOF);
		return XPCOM.NS_OK;
	}

	/* picking a file */
	int style = mode == nsIFilePicker.modeSave ? SWT.SAVE : SWT.OPEN;
	if (mode == nsIFilePicker.modeOpenMultiple) style |= SWT.MULTI;
	Shell parent = new Shell (Display.getCurrent ());
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
	XPCOM.memmove (_retval, new int [] {result}, OS.PTR_SIZEOF);
	return XPCOM.NS_OK;
}

int showDirectoryPicker () {
	Shell parent = new Shell (Display.getCurrent ());
	DirectoryDialog dialog = new DirectoryDialog (parent, SWT.NONE);
	if (title != null) dialog.setText (title);
	if (directory != null) dialog.setFilterPath (directory);
	directory = dialog.open ();
	title = defaultFilename = null;
	files = masks = null;
	return directory == null ? nsIFilePicker.returnCancel : nsIFilePicker.returnOK;
}

int GetFiles (int aFiles) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetFileURL (int aFileURL) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetFile (int aFile) {
	String filename = "";	//$NON-NLS-1$
	if (directory != null) filename += directory + SEPARATOR;
	if (files != null && files.length > 0) filename += files [0];
	nsEmbedString path = new nsEmbedString (filename);
	int [] file = new int [1];
	int rc = XPCOM.NS_NewLocalFile (path.getAddress (), true, file);
	path.dispose ();
	if (rc != XPCOM.NS_OK) Browser.error (rc);
	if (file [0] == 0) Browser.error (XPCOM.NS_ERROR_NULL_POINTER);
	XPCOM.memmove (aFile, file, OS.PTR_SIZEOF);
	return XPCOM.NS_OK;
}

int SetDisplayDirectory (int aDisplayDirectory) {
	if (aDisplayDirectory == 0) return XPCOM.NS_OK;
	nsILocalFile file = new nsILocalFile (aDisplayDirectory);
	int pathname = XPCOM.nsEmbedCString_new ();
	file.GetNativePath (pathname);
	int length = XPCOM.nsEmbedCString_Length (pathname);
	int buffer = XPCOM.nsEmbedCString_get (pathname);
	byte[] bytes = new byte [length];
	XPCOM.memmove (bytes, buffer, length);
	XPCOM.nsEmbedCString_delete (pathname);
	char[] chars = Converter.mbcsToWcs (null, bytes);
	directory = new String (chars);
	return XPCOM.NS_OK;
}

int GetDisplayDirectory (int aDisplayDirectory) {
	String directoryName = directory != null ? directory : "";	//$NON-NLS-1$
	nsEmbedString path = new nsEmbedString (directoryName);
	int [] file = new int [1];
	int rc = XPCOM.NS_NewLocalFile (path.getAddress (), true, file);
	path.dispose ();
	if (rc != XPCOM.NS_OK) Browser.error (rc);
	if (file [0] == 0) Browser.error (XPCOM.NS_ERROR_NULL_POINTER);
	XPCOM.memmove (aDisplayDirectory, file, OS.PTR_SIZEOF);
	return XPCOM.NS_OK;
}

int SetFilterIndex (int aFilterIndex) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetFilterIndex (int aFilterIndex) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetDefaultExtension (int aDefaultExtension) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetDefaultExtension (int aDefaultExtension) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetDefaultString (int aDefaultString) {
	if (aDefaultString == 0) return XPCOM.NS_OK;
	int length = XPCOM.strlen_PRUnichar (aDefaultString);
	char[] chars = new char [length];
	XPCOM.memmove (chars, aDefaultString, length * 2);
	defaultFilename = new String (chars);
	return XPCOM.NS_OK;
}

int GetDefaultString (int aDefaultString) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int AppendFilter (int title, int filter) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int AppendFilters (int filterMask) {
	String[] addFilters = null;
	switch ((int)filterMask) {
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
			String[] newFilters = new String [masks.length + addFilters.length];
			System.arraycopy (masks, 0, newFilters, 0, masks.length);
			System.arraycopy (addFilters, 0, newFilters, masks.length, addFilters.length);
			masks = newFilters;
		}
	}
	return XPCOM.NS_OK;
}
}
