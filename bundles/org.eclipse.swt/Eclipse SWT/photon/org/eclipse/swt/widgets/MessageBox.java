package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public /*final*/ class MessageBox extends Dialog {
	String message = "";
public MessageBox (Shell parent) {
	this (parent, SWT.OK | SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL);
}
public MessageBox (Shell parent, int style) {
	super (parent, checkStyle (style));
}
static int checkStyle (int style) {
	if ((style & (SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) == 0) style |= SWT.APPLICATION_MODAL;
	int mask = (SWT.YES | SWT.NO | SWT.OK | SWT.CANCEL | SWT.ABORT | SWT.RETRY | SWT.IGNORE);
	int bits = style & mask;
	if (bits == SWT.OK || bits == SWT.CANCEL || bits == (SWT.OK | SWT.CANCEL)) return style;
	if (bits == SWT.YES || bits == SWT.NO || bits == (SWT.YES | SWT.NO) || bits == (SWT.YES | SWT.NO | SWT.CANCEL)) return style;
	if (bits == (SWT.RETRY | SWT.CANCEL) || bits == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) return style;
	style = (style & ~mask) | SWT.OK;
	return style;
}
public String getMessage () {
	return message;
}
public int open () {
	String[] buttons = null;
	if ((style & SWT.OK) == SWT.OK) buttons = new String[]{"&Ok"};
	if ((style & (SWT.OK | SWT.CANCEL)) == (SWT.OK | SWT.CANCEL)) buttons = new String[]{"&Ok", "&Cancel"};
	if ((style & (SWT.YES | SWT.NO)) == (SWT.YES | SWT.NO)) buttons = new String[]{"&Yes", "&No"};
	if ((style & (SWT.YES | SWT.NO | SWT.CANCEL)) == (SWT.YES | SWT.NO | SWT.CANCEL)) buttons = new String[]{"&Yes", "&No", "&Cancel"};
	if ((style & (SWT.RETRY | SWT.CANCEL)) == (SWT.RETRY | SWT.CANCEL)) buttons = new String[]{"&Retry", "&Cancel"};
	if ((style & (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) buttons = new String[]{"&Abort", "&Retry", "&Ignore"};
	if (buttons == null) buttons = new String[]{"&Ok"};
	
	int phImage = 0;
//	int iconBits = 0;
//	if ((style & SWT.ICON_ERROR) != 0) iconBits = OS.MB_ICONERROR;
//	if ((style & SWT.ICON_INFORMATION) != 0) iconBits = OS.MB_ICONINFORMATION;
//	if ((style & SWT.ICON_QUESTION) != 0) iconBits = OS.MB_ICONQUESTION;
//	if ((style & SWT.ICON_WARNING) != 0) iconBits = OS.MB_ICONWARNING;
//	if ((style & SWT.ICON_WORKING) != 0) iconBits = OS.MB_ICONINFORMATION;

	int parentHandle = 0;
	if (parent != null) parentHandle = parent.shellHandle;

	byte [] title = null;
	if (this.title != null) title = Converter.wcsToMbcs (null, this.title, true);
	byte [] message = null;
	if (this.message != null) message = Converter.wcsToMbcs (null, this.message, true);

	int[] buttonsPtr = new int [buttons.length];
	for (int i=0; i<buttons.length; i++) {
		byte[] text = Converter.wcsToMbcs (null, buttons [i], true);
		int textPtr = OS.malloc (text.length);
		OS.memmove (textPtr, text, text.length);
		buttonsPtr [i] = textPtr;
	}

	int button = OS.PtAlert (parentHandle, null, title, phImage, message, null, buttons.length, buttonsPtr, null, 0, buttons.length, OS.Pt_MODAL);

	for (int i=0; i<buttons.length; i++) {
		OS.free (buttonsPtr [i]);
	}

	if ((style & (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) == (SWT.ABORT | SWT.RETRY | SWT.IGNORE)) {
		if (button == 1) return SWT.ABORT;
		if (button == 2) return SWT.RETRY;
		return SWT.IGNORE;
	}
	if ((style & (SWT.RETRY | SWT.CANCEL)) == (SWT.RETRY | SWT.CANCEL)) {
		return (button == 1) ? SWT.RETRY : SWT.CANCEL;
	}
	if ((style & (SWT.YES | SWT.NO | SWT.CANCEL)) == (SWT.YES | SWT.NO | SWT.CANCEL)) {
		if (button == 1) return SWT.YES;
		if (button == 2) return SWT.NO;
		return SWT.CANCEL;
	}
	if ((style & (SWT.YES | SWT.NO)) == (SWT.YES | SWT.NO)) {
		return (button == 1) ? SWT.YES : SWT.NO;
	}
	if ((style & (SWT.OK | SWT.CANCEL)) == (SWT.OK | SWT.CANCEL)) {
		return (button == 1) ? SWT.OK : SWT.CANCEL;
	}
	if ((style & SWT.OK) == SWT.OK && button == 1) return SWT.OK;
	return SWT.CANCEL;
}
public void setMessage (String string) {
	message = string;
}
}
