package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public /*final*/ class DirectoryDialog extends Dialog {
	String message = "", filterPath = "";
	
public DirectoryDialog (Shell parent) {
	this (parent, SWT.PRIMARY_MODAL);
}

public DirectoryDialog (Shell parent, int style) {
	super (parent, style);
}

public String getFilterPath () {
	return filterPath;
}

public String getMessage () {
	return message;
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
	byte [] file_spec = null;
	int flags = OS.Pt_FSR_NO_FCHECK | OS.Pt_FSR_NO_SELECT_FILES | OS.Pt_FSR_SELECT_DIRS;
	PtFileSelectionInfo_t info = new PtFileSelectionInfo_t ();
	OS.PtFileSelection (parentHandle, null, title, root_dir, file_spec, null, null, null, info, flags);
	if (info.ret == OS.Pt_FSDIALOG_BTN2) return null;
	int length = 0;
	while (length < info.path.length && info.path [length] != 0) length++;
	byte [] path = new byte [length];
	System.arraycopy (info.path, 0, path, 0, length);
	return new String (Converter.mbcsToWcs (null, path));
}

public void setFilterPath (String string) {
	filterPath = string;
}

public void setMessage (String string) {
	message = string;
}

}
