package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public /*final*/ class FileDialog extends Dialog {
	String [] filterNames = new String [0];
	String [] filterExtensions = new String [0];
	String filterPath = "", fileName = "";
	static final String FILTER = "*";
	
public FileDialog (Shell parent) {
	this (parent, SWT.PRIMARY_MODAL);
}

public FileDialog (Shell parent, int style) {
	super (parent, style);
}

public String getFileName () {
	return fileName;
}

public String [] getFileNames () {
	return new String [] {fileName};
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
	int parentHandle = 0;
	if (parent != null) parentHandle = parent.shellHandle;
	byte [] title = null;
	if (this.title != null) title = Converter.wcsToMbcs (null, this.title, true);
	byte [] root_dir = null;
	if (filterPath != null) {
		root_dir = Converter.wcsToMbcs (null, filterPath, true);
	}
	
	/* Compute the filter */
	String mask = FILTER;
	/*
	* Photon does not support filter names.
	*/
	if (filterNames == null) filterNames = new String [0];
	/*
	* Photon supports only one filter with multiple patterns
	* separated by commas.
	*/
	if (filterExtensions == null) filterExtensions = new String [0];
	if (filterExtensions.length > 0) {
		String comma = ",";
		mask = comma;
		for (int i=0; i<filterExtensions.length; i++) {
			String ext = filterExtensions [i];
			int length = ext.length();
			int end, start = 0;
			do {
				int index = end = ext.indexOf(';', start);
				if (end < 0) end = length;
				String subExt = ext.substring(start, end).trim();
				if (subExt.length() > 0) {
					subExt += comma;
					if (mask.indexOf(comma + subExt) == -1) mask += subExt;
				}
				start = end + 1;
			} while (end < length);
		}
		mask = mask.substring(1, Math.max(1, mask.length() - 1));
	}
	byte [] file_spec = Converter.wcsToMbcs (null, mask, true);
	
	int flags = OS.Pt_FSR_NO_FCHECK;
	PtFileSelectionInfo_t info = new PtFileSelectionInfo_t ();
	OS.PtFileSelection (parentHandle, null, title, root_dir, file_spec, null, null, null, info, flags);
	if (info.ret == OS.Pt_FSDIALOG_BTN2) return null;
	int length = 0;
	while (length < info.path.length && info.path [length] != 0) length++;
	byte [] path = new byte [length];
	System.arraycopy (info.path, 0, path, 0, length);
	String fullPath = new String (Converter.mbcsToWcs (null, path));
	length = fullPath.length ();
	if (length != 0) {
		int index = length - 1;
		while (index >= 0 && (fullPath.charAt (index) != '/')) --index;
		fileName = fullPath.substring (index + 1, length);
		filterPath = fullPath.substring (0, index);
	}
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
